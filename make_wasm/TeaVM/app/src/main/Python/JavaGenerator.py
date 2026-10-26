"""
JavaParser が出力した API の JSON（nfc_api.json）から、TeaVM 用のブリッジコードを jinja2 で生成する。

生成するもの:
  - org/example/generated/〇〇Wasm.java  … nfc のパッケージごとのブリッジ（型の変換 → 呼び出し → pack）
  - org/example/generated/NfcWasm.java   … @JSExport を付けた公開窓口
  - org/example/generated/Matrices.java  … 配列の変換（手書き版とほぼ同じ。null を空の行列として扱う）
  - webapp/nfc.generated.js              … JS から 2 次元配列のまま呼べるラッパー
  - build/codegen_report.md              … 生成したメソッドと、対象外にしたメソッドの一覧

手順は WASM_EXPORT_GUIDE.md に対応している:
  手順A・B … select_entry_classes()   入口クラスを選ぶ
  手順C   … build_method()           メソッドの static・引数・戻り値を見て変換方法を決める
  手順D   … find_getters()           戻り値が「箱」なら getter を探す

使い方（TeaVM フォルダで）:
  ./gradlew :app:parseJava --args="app/src/main/java/org/mklab/nfc --json build/nfc_api.json"
  python3 app/src/main/Python/JavaGenerator.py
"""

import argparse
import json
import re
from pathlib import Path

from jinja2 import Environment, FileSystemLoader

# ---------------------------------------------------------------------------
# 設定
# ---------------------------------------------------------------------------

# このファイルは TeaVM/app/src/main/Python/ にあるので、4 つ上が TeaVM フォルダ
BASE_DIR = Path(__file__).resolve().parent
PROJECT_ROOT = BASE_DIR.parents[3]

DEFAULT_JSON = PROJECT_ROOT / "build" / "nfc_api.json"
JAVA_PACKAGE = "org.example.generated"
JAVA_OUT_DIR = PROJECT_ROOT / "app" / "src" / "main" / "java" / Path(*JAVA_PACKAGE.split("."))
JS_OUT_FILE = PROJECT_ROOT / "app" / "src" / "main" / "webapp" / "nfc.generated.js"
REPORT_FILE = PROJECT_ROOT / "build" / "codegen_report.md"

# 手順A：入口クラスとみなすクラス名の末尾
ENTRY_SUFFIXES = ("Solver", "Decomposer", "Analyzer", "ExponentialMatrix")
# 手順B：倍精度の実数版・複素数版だけを対象にする（JS 側の名前の先頭に付ける語）
ENTRY_PREFIXES = {"DoubleReal": "real", "DoubleComplex": "complex"}
# 公開名を短くするための言い換え（クラス名の中心部分 → 公開名で使う語）
CORE_ABBREVIATIONS = {"SingularValue": "Svd"}

# ---------------------------------------------------------------------------
# 型の変換表
# ---------------------------------------------------------------------------

# 引数：Java の型 → ブリッジでの受け取り方
#   java : ブリッジ／NfcWasm の引数（{n} は元の引数名）
#   arg  : 元のメソッドに渡す式
#   js   : JS 側の扱い（"matrix" なら 2 次元配列を flatten して渡す）
PARAM_RULES = {
    "double": {"java": ["double {n}"], "arg": "{n}", "js": "plain"},
    "int": {"java": ["int {n}"], "arg": "{n}", "js": "plain"},
    "boolean": {"java": ["boolean {n}"], "arg": "{n}", "js": "plain"},
    "double[]": {"java": ["double[] {n}"], "arg": "{n}", "js": "plain"},
    "double[][]": {
        "java": ["double[] {n}", "int {n}Rows", "int {n}Columns"],
        "arg": "Matrices.toMatrix({n}, {n}Rows, {n}Columns)",
        "js": "matrix",
    },
    "DoubleMatrix": {
        "java": ["double[] {n}", "int {n}Rows", "int {n}Columns"],
        "arg": "new DoubleMatrix(Matrices.toMatrix({n}, {n}Rows, {n}Columns))",
        "js": "matrix",
    },
}

# 戻り値：Java の型 → JS に返す形
#   java : ブリッジ／NfcWasm の戻り値の型
#   expr : 変数 result を JS に返す式（None なら何も返さない）
#   js   : JS 側で unpack した後の形
RETURN_RULES = {
    "void": {"java": "void", "expr": None, "js": "none"},
    "double": {"java": "double", "expr": "result", "js": "plain"},
    "int": {"java": "int", "expr": "result", "js": "plain"},
    "boolean": {"java": "boolean", "expr": "result", "js": "plain"},
    "double[]": {"java": "double[]", "expr": "Matrices.pack(Matrices.rowVector(result))", "js": "vector"},
    "double[][]": {"java": "double[]", "expr": "Matrices.pack(result)", "js": "matrix"},
    "double[][][]": {"java": "double[]", "expr": "Matrices.pack(result)", "js": "matrices"},
    "int[][]": {"java": "double[]", "expr": "Matrices.pack(Matrices.toDouble(result))", "js": "matrix"},
    "DoubleMatrix": {"java": "double[]", "expr": "Matrices.pack(result.getDoubleElements())", "js": "matrix"},
}

# 手順D：箱の getter の戻り値の型 → pack に渡せる double[][] にする式（{g} は getter の呼び出し）
GETTER_RULES = {
    "double[][]": {"expr": "{g}", "js": "matrix"},
    "double[]": {"expr": "Matrices.rowVector({g})", "js": "vector"},
    "int[][]": {"expr": "Matrices.toDouble({g})", "js": "matrix"},
}


class Skip(Exception):
    """このメソッドは生成の対象外（理由をメッセージに入れる）。"""


# ---------------------------------------------------------------------------
# 手順A・B：入口クラスを選ぶ
# ---------------------------------------------------------------------------

def select_entry_classes(classes):
    """クラス名と形から、JS に公開する入口クラスだけを選ぶ。"""
    entries = []
    for c in classes:
        is_entry_name = c["name"].endswith(ENTRY_SUFFIXES)
        is_double = c["name"].startswith(tuple(ENTRY_PREFIXES))
        is_concrete_class = c["kind"] == "class" and "abstract" not in c["modifiers"]
        is_not_generic = not c["typeParameters"]
        if is_entry_name and is_double and is_concrete_class and is_not_generic:
            entries.append(c)
    return entries


# ---------------------------------------------------------------------------
# 手順D：戻り値が「箱」なら getter を探す
# ---------------------------------------------------------------------------

def find_getters(box, class_by_name):
    """箱のクラス（親クラスも含む）から、get で始まり引数 0 個で、変換できる型を返す getter を集める。"""
    getters = []
    current = box
    while current is not None:
        for m in current["methods"]:
            if (m["name"].startswith("get") and not m["parameters"] and not m["static"]
                    and m["returnType"] in GETTER_RULES):
                getters.append(m)
        # extends が JSON にあるクラスなら、親クラスもたどる（List<String> のような <...> は外す）
        parent_name = current["extends"][0].split("<")[0] if current["extends"] else None
        current = class_by_name.get(parent_name)
    return getters


# ---------------------------------------------------------------------------
# 手順C：メソッド 1 つ分の変換方法を決める
# ---------------------------------------------------------------------------

def build_method(entry, method, class_by_name):
    """入口クラスのメソッド 1 つを、テンプレートに渡す辞書に変換する。対象外なら Skip を投げる。"""
    if any(a.startswith("@Deprecated") for a in method["annotations"]):
        raise Skip("@Deprecated")
    if method["typeParameters"]:
        raise Skip("ジェネリックメソッド")

    # 呼び出し方：static なら「クラス名.メソッド」、そうでなければ new してから呼ぶ
    if method["static"]:
        receiver = entry["name"]
    else:
        if method["returnType"] == "void":
            raise Skip("毎回 new したインスタンスに対する void メソッドなので、呼んでも効果がない")
        if not any(not k["parameters"] for k in entry["constructors"]):
            raise Skip("引数なしの public コンストラクタがない")
        receiver = f"new {entry['name']}()"

    imports = {entry["qualifiedName"]}

    # 引数
    params = []
    for p in method["parameters"]:
        rule = PARAM_RULES.get(p["type"])
        if rule is None or p["varArgs"]:
            raise Skip(f"引数の型 {p['type']}{'...' if p['varArgs'] else ''} を JS から渡せない")
        if p["type"] in class_by_name:
            imports.add(class_by_name[p["type"]]["qualifiedName"])
        params.append({
            "name": p["name"],
            "java": [s.format(n=p["name"]) for s in rule["java"]],
            "arg": rule["arg"].format(n=p["name"]),
            "js": rule["js"],
        })

    # 戻り値
    return_type = method["returnType"]
    if return_type in RETURN_RULES:
        rule = RETURN_RULES[return_type]
        result = {"java": rule["java"], "expr": rule["expr"], "js": rule["js"], "fields": []}
        if return_type in class_by_name:
            imports.add(class_by_name[return_type]["qualifiedName"])
    elif return_type in class_by_name:
        # 手順D：箱 → getter で取り出して 1 本の配列に詰める
        box = class_by_name[return_type]
        getters = find_getters(box, class_by_name)
        if not getters:
            raise Skip(f"戻り値 {return_type} に取り出せる getter がない")
        imports.add(box["qualifiedName"])
        fields = [{
            "key": g["name"][len("get"):],
            "expr": GETTER_RULES[g["returnType"]]["expr"].format(g=f"result.{g['name']}()"),
            "js": GETTER_RULES[g["returnType"]]["js"],
        } for g in getters]
        result = {
            "java": "double[]",
            "expr": "Matrices.pack(" + ", ".join(f["expr"] for f in fields) + ")",
            "js": "object",
            "fields": fields,
        }
    else:
        raise Skip(f"戻り値の型 {return_type} を JS に返せない")

    # ブリッジ／NfcWasm の引数（double[][] は 3 つに分かれるので、ここで 1 列に並べておく）
    java_params = [decl for p in params for decl in p["java"]]

    return {
        "source": f"{entry['name']}.{method['name']}",
        "export_name": base_export_name(entry["name"], method["name"]),
        "original_type": return_type,
        "call": f"{receiver}.{method['name']}(" + ", ".join(p["arg"] for p in params) + ")",
        "throws": method["throws"],
        "params": params,
        "java_params": java_params,                              # 例: ["double[] a", "int aRows", "int aColumns"]
        "java_args": [decl.split()[-1] for decl in java_params], # 例: ["a", "aRows", "aColumns"]
        "js_args": [arg for p in params for arg in                # 例: ["aF.values", "aF.rows", "aF.cols"]
                    ([f"{p['name']}F.values", f"{p['name']}F.rows", f"{p['name']}F.cols"]
                     if p["js"] == "matrix" else [p["name"]])],
        "result": result,
        "imports": imports,
    }


# ---------------------------------------------------------------------------
# 公開名（JS から呼ぶときの名前）
# ---------------------------------------------------------------------------

def camel_word(word):
    """FFT → Fft、QZ → Qz のように、大文字の連続を 1 語として扱う。"""
    return re.sub(r"[A-Z]{2,}(?=[A-Z][a-z]|$)", lambda m: m.group(0).capitalize(), word)


def base_export_name(class_name, method_name):
    """DoubleRealSchurDecomposer.decompose → realSchurDecompose のように名前を作る。"""
    prefix = next(v for k, v in ENTRY_PREFIXES.items() if class_name.startswith(k))
    core = class_name[len(next(k for k in ENTRY_PREFIXES if class_name.startswith(k))):]
    for suffix in ENTRY_SUFFIXES:
        if core.endswith(suffix):
            core = core[: -len(suffix)]
            break
    core = camel_word(CORE_ABBREVIATIONS.get(core, core))
    # メソッド名がクラスの中心部分で始まるなら重ねない（FFT.fft → realFft、QZ.qzDecompose → realQzDecompose）
    if core and method_name.lower().startswith(core.lower()):
        core = ""
    return prefix + core + method_name[0].upper() + method_name[1:]


def resolve_name_collisions(methods):
    """オーバーロードなどで公開名が重なったら、引数の多いほうに With〇〇 を付けて区別する。"""
    groups = {}
    for m in methods:
        groups.setdefault(m["export_name"], []).append(m)
    for group in groups.values():
        if len(group) == 1:
            continue
        group.sort(key=lambda m: len(m["params"]))
        base_params = {p["name"] for p in group[0]["params"]}
        for m in group[1:]:
            extra = [p["name"] for p in m["params"] if p["name"] not in base_params]
            m["export_name"] += "With" + "".join(n[0].upper() + n[1:] for n in extra)
    names = [m["export_name"] for m in methods]
    duplicates = {n for n in names if names.count(n) > 1}
    if duplicates:
        raise SystemExit(f"公開名が重複しています: {sorted(duplicates)}")


# ---------------------------------------------------------------------------
# まとめ：テンプレートに渡すデータを作る
# ---------------------------------------------------------------------------

def build_bridges(classes):
    """入口クラスのメソッドを変換し、nfc のパッケージごとにまとめる。"""
    class_by_name = {c["name"]: c for c in classes}
    bridges = {}   # パッケージ名（eig など）→ ブリッジ 1 つ分
    skipped = []

    for entry in select_entry_classes(classes):
        package = entry["package"].split(".")[-1]
        bridge = bridges.setdefault(package, {
            "package": entry["package"],
            "class_name": package.capitalize() + "Wasm",
            "methods": [],
            "imports": set(),
        })
        for method in entry["methods"]:
            try:
                m = build_method(entry, method, class_by_name)
            except Skip as reason:
                skipped.append({"source": f"{entry['name']}.{method['name']}", "reason": str(reason)})
                continue
            m["bridge"] = bridge["class_name"]
            bridge["methods"].append(m)
            bridge["imports"] |= m["imports"]

    bridges = [b for b in bridges.values() if b["methods"]]
    resolve_name_collisions([m for b in bridges for m in b["methods"]])
    for b in bridges:
        b["imports"] = sorted(b["imports"])
    return sorted(bridges, key=lambda b: b["class_name"]), skipped


def render_all(bridges, skipped, json_path):
    env = Environment(
        loader=FileSystemLoader(BASE_DIR / "templates"),
        trim_blocks=True,          # {% %} の直後の改行を消す
        lstrip_blocks=True,        # {% %} の前のインデントを消す
        keep_trailing_newline=True,
    )
    common = {"package": JAVA_PACKAGE, "bridges": bridges, "skipped": skipped, "json_path": json_path}

    JAVA_OUT_DIR.mkdir(parents=True, exist_ok=True)
    outputs = {JAVA_OUT_DIR / "Matrices.java": env.get_template("matrices.java.j2").render(common),
               JAVA_OUT_DIR / "NfcWasm.java": env.get_template("nfc_wasm.java.j2").render(common),
               JS_OUT_FILE: env.get_template("nfc.js.j2").render(common),
               REPORT_FILE: env.get_template("report.md.j2").render(common)}
    for bridge in bridges:
        outputs[JAVA_OUT_DIR / f"{bridge['class_name']}.java"] = \
            env.get_template("bridge.java.j2").render(common, bridge=bridge)

    for path, text in outputs.items():
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(text, encoding="utf-8")
        print(f"生成: {path.relative_to(PROJECT_ROOT)}")


def main():
    parser = argparse.ArgumentParser(description="nfc_api.json から TeaVM 用のブリッジコードを生成する")
    parser.add_argument("--json", type=Path, default=DEFAULT_JSON, help="JavaParser が出力した JSON")
    args = parser.parse_args()

    with open(args.json, encoding="utf-8") as f:
        api = json.load(f)

    bridges, skipped = build_bridges(api["classes"])
    render_all(bridges, skipped, args.json)

    count = sum(len(b["methods"]) for b in bridges)
    print(f"公開したメソッド: {count} 個 / 対象外: {len(skipped)} 個（理由は {REPORT_FILE.relative_to(PROJECT_ROOT)}）")


if __name__ == "__main__":
    main()
