"""
JavaParser が出力した API の JSON（nfc_api.json）から、TeaVM 用のブリッジコードを jinja2 で生成する。

生成するもの:
  - org/example/generated/〇〇Wasm.java  … nfc のパッケージごとのブリッジ（型の変換 → 呼び出し → pack）
  - org/example/generated/NfcWasm.java   … @JSExport を付けた公開窓口
  - org/example/generated/Matrices.java  … 配列の変換（手書き版とほぼ同じ。null を空の行列として扱う）
  - webapp/nfc.generated.js              … JS から 2 次元配列のまま呼べるラッパー
  - build/codegen_report.md              … 生成したメソッドと、対象外にしたメソッドの一覧

NFC に合わせた値（入口クラスの条件・型の変換ルール・名前の言い換え・出力先）は、
コードには書かず、設定ファイル codegen/nfc.yaml に書いている（step02 で分けた）。
このファイルには「どう選ぶか・どう変換するか」という手順だけを書く。

手順は WASM_EXPORT_GUIDE.md に対応している:
  手順A・B … select_entry_classes()   入口クラスを選ぶ
  手順C   … build_method()           メソッドの static・引数・戻り値を見て変換方法を決める
  手順D   … find_getters()           戻り値が「箱」なら getter を探す

使い方（TeaVM フォルダで。Python はプロジェクトの仮想環境 .venv を使う）:
  ./gradlew :app:parseJava --args="app/src/main/java/org/mklab/nfc --json build/nfc_api.json"
  .venv/bin/python app/src/main/Python/JavaGenerator.py                       # codegen/nfc.yaml を使う
  .venv/bin/python app/src/main/Python/JavaGenerator.py --config codegen/nfc.yaml
"""

import argparse
import json
import re
from pathlib import Path

import yaml
from jinja2 import Environment, FileSystemLoader

# ---------------------------------------------------------------------------
# 設定ファイルの読み込み
# ---------------------------------------------------------------------------

# このファイルは TeaVM/app/src/main/Python/ にあるので、4 つ上が TeaVM フォルダ
BASE_DIR = Path(__file__).resolve().parent
PROJECT_ROOT = BASE_DIR.parents[3]

DEFAULT_JSON = PROJECT_ROOT / "build" / "nfc_api.json"
DEFAULT_CONFIG = PROJECT_ROOT / "codegen" / "nfc.yaml"

# 設定ファイルに必要な項目と、その値の形（validate_config で確かめる）
CONFIG_SCHEMA = {
    "library": {"name": str},
    "output": {"java_package": str, "java_dir": str, "js_file": str, "report_file": str},
    "entry": {"class_suffixes": list, "class_prefixes": dict},
    "naming": {"abbreviations": dict},
    "types": {"params": dict, "returns": dict, "getters": dict},
}

# 変換ルール 1 つ分に必要な項目と、その値の形（タプルは「どれかの形ならよい」。type(None) は null を表す）
RULE_SCHEMA = {
    "params": {"java": list, "arg": str, "js": str},
    "returns": {"java": str, "expr": (str, type(None)), "js": str},
    "getters": {"expr": str, "js": str},
}

# エラーメッセージで使う、値の形の日本語名
TYPE_NAMES = {str: "文字列", list: "リスト", dict: "対応表（名前: 値）", type(None): "null"}


def type_name(expected):
    """値の形（型、または型のタプル）を日本語にする。"""
    if isinstance(expected, tuple):
        return " または ".join(TYPE_NAMES[t] for t in expected)
    return TYPE_NAMES[expected]


def validate_config(data):
    """設定の中身を確かめ、問題の一覧（文字列のリスト）を返す。問題がなければ空のリスト。"""
    problems = []
    if not isinstance(data, dict):
        return ["ファイルの一番外側が「名前: 値」の形になっていない"]

    for section, keys in CONFIG_SCHEMA.items():
        if not isinstance(data.get(section), dict):
            problems.append(f"項目 {section} がない（または「名前: 値」の形になっていない）")
            continue
        for key, expected in keys.items():
            if key not in data[section]:
                problems.append(f"項目 {section}.{key} がない")
            elif not isinstance(data[section][key], expected):
                problems.append(f"項目 {section}.{key} は{type_name(expected)}で書く")

    # 入口の条件：中身もすべて文字列か
    entry = data.get("entry") if isinstance(data.get("entry"), dict) else {}
    if isinstance(entry.get("class_suffixes"), list):
        for value in entry["class_suffixes"]:
            if not isinstance(value, str):
                problems.append(f"項目 entry.class_suffixes の {value!r} は文字列で書く")
    for mapping_name in ("entry.class_prefixes", "naming.abbreviations"):
        section, key = mapping_name.split(".")
        mapping = data.get(section, {}).get(key) if isinstance(data.get(section), dict) else None
        if isinstance(mapping, dict):
            for name, value in mapping.items():
                if not isinstance(name, str) or not isinstance(value, str):
                    problems.append(f"項目 {mapping_name} の {name!r}: {value!r} は、名前も値も文字列で書く")

    # 型の変換ルール：ルール 1 つずつに必要な項目があるか
    types = data.get("types") if isinstance(data.get("types"), dict) else {}
    for kind, keys in RULE_SCHEMA.items():
        rules = types.get(kind)
        if not isinstance(rules, dict):
            continue
        for java_type, rule in rules.items():
            where = f"types.{kind}.{java_type}"
            if not isinstance(rule, dict):
                problems.append(f"項目 {where} が「名前: 値」の形になっていない")
                continue
            for key, expected in keys.items():
                if key not in rule:
                    problems.append(f"項目 {where}.{key} がない")
                elif not isinstance(rule[key], expected):
                    problems.append(f"項目 {where}.{key} は{type_name(expected)}で書く")
            if isinstance(rule.get("java"), list):
                for value in rule["java"]:
                    if not isinstance(value, str):
                        problems.append(f"項目 {where}.java の {value!r} は文字列で書く")
    return problems


class Config:
    """設定ファイル（codegen/*.yaml）の中身。生成器のあちこちで使う値をまとめて持つ。"""

    def __init__(self, data, path):
        self.path = path
        self.library_name = data["library"]["name"]
        # 生成物の出力先（設定ファイルには TeaVM フォルダからの相対パスで書く）
        self.java_package = data["output"]["java_package"]
        self.java_out_dir = PROJECT_ROOT / data["output"]["java_dir"]
        self.js_out_file = PROJECT_ROOT / data["output"]["js_file"]
        self.report_file = PROJECT_ROOT / data["output"]["report_file"]
        # 手順A：入口クラスとみなすクラス名の末尾（str.endswith に渡すのでタプルにする）
        self.entry_suffixes = tuple(data["entry"]["class_suffixes"])
        # 手順B：対象にするクラス名の先頭 → JS の公開名の先頭に付ける語
        self.entry_prefixes = data["entry"]["class_prefixes"]
        # 公開名を短くするための言い換え（クラス名の中心部分 → 公開名で使う語）
        self.abbreviations = data["naming"]["abbreviations"]
        # 手順C・D：型の変換ルール（中の項目の意味は codegen/nfc.yaml のコメントを参照）
        self.param_rules = data["types"]["params"]
        self.return_rules = data["types"]["returns"]
        self.getter_rules = data["types"]["getters"]


def load_config(path):
    """設定ファイルを読み込んで確かめる。問題があれば、何が問題かを表示して止まる。"""
    try:
        with open(path, encoding="utf-8") as f:
            data = yaml.safe_load(f)
    except FileNotFoundError:
        raise SystemExit(f"設定ファイルが見つからない: {path}")
    except yaml.YAMLError as e:
        raise SystemExit(f"設定ファイルを YAML として読めない: {path}\n{e}")

    problems = validate_config(data)
    if problems:
        lines = "\n".join(f"  - {p}" for p in problems)
        raise SystemExit(f"設定ファイルに問題がある: {path}\n{lines}")
    return Config(data, path)


class Skip(Exception):
    """このメソッドは生成の対象外（理由をメッセージに入れる）。"""


# ---------------------------------------------------------------------------
# 手順A・B：入口クラスを選ぶ
# ---------------------------------------------------------------------------

def select_entry_classes(classes, config):
    """クラス名と形から、JS に公開する入口クラスだけを選ぶ。"""
    entries = []
    for c in classes:
        is_entry_name = c["name"].endswith(config.entry_suffixes)
        is_double = c["name"].startswith(tuple(config.entry_prefixes))
        is_concrete_class = c["kind"] == "class" and "abstract" not in c["modifiers"]
        is_not_generic = not c["typeParameters"]
        if is_entry_name and is_double and is_concrete_class and is_not_generic:
            entries.append(c)
    return entries


# ---------------------------------------------------------------------------
# 手順D：戻り値が「箱」なら getter を探す
# ---------------------------------------------------------------------------

def find_getters(box, class_by_name, getter_rules):
    """箱のクラス（親クラスも含む）から、get で始まり引数 0 個で、変換できる型を返す getter を集める。"""
    getters = []
    current = box
    while current is not None:
        for m in current["methods"]:
            if (m["name"].startswith("get") and not m["parameters"] and not m["static"]
                    and m["returnType"] in getter_rules):
                getters.append(m)
        # extends が JSON にあるクラスなら、親クラスもたどる（List<String> のような <...> は外す）
        parent_name = current["extends"][0].split("<")[0] if current["extends"] else None
        current = class_by_name.get(parent_name)
    return getters


# ---------------------------------------------------------------------------
# 手順C：メソッド 1 つ分の変換方法を決める
# ---------------------------------------------------------------------------

def build_method(entry, method, class_by_name, config):
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
        rule = config.param_rules.get(p["type"])
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
    if return_type in config.return_rules:
        rule = config.return_rules[return_type]
        result = {"java": rule["java"], "expr": rule["expr"], "js": rule["js"], "fields": []}
        if return_type in class_by_name:
            imports.add(class_by_name[return_type]["qualifiedName"])
    elif return_type in class_by_name:
        # 手順D：箱 → getter で取り出して 1 本の配列に詰める
        box = class_by_name[return_type]
        getters = find_getters(box, class_by_name, config.getter_rules)
        if not getters:
            raise Skip(f"戻り値 {return_type} に取り出せる getter がない")
        imports.add(box["qualifiedName"])
        fields = [{
            "key": g["name"][len("get"):],
            "expr": config.getter_rules[g["returnType"]]["expr"].format(g=f"result.{g['name']}()"),
            "js": config.getter_rules[g["returnType"]]["js"],
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
        "export_name": base_export_name(entry["name"], method["name"], config),
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


def base_export_name(class_name, method_name, config):
    """DoubleRealSchurDecomposer.decompose → realSchurDecompose のように名前を作る。"""
    prefix = next(v for k, v in config.entry_prefixes.items() if class_name.startswith(k))
    core = class_name[len(next(k for k in config.entry_prefixes if class_name.startswith(k))):]
    for suffix in config.entry_suffixes:
        if core.endswith(suffix):
            core = core[: -len(suffix)]
            break
    core = camel_word(config.abbreviations.get(core, core))
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

def build_bridges(classes, config):
    """入口クラスのメソッドを変換し、ライブラリのパッケージごとにまとめる。"""
    class_by_name = {c["name"]: c for c in classes}
    bridges = {}   # パッケージ名（eig など）→ ブリッジ 1 つ分
    skipped = []

    for entry in select_entry_classes(classes, config):
        package = entry["package"].split(".")[-1]
        bridge = bridges.setdefault(package, {
            "package": entry["package"],
            "class_name": package.capitalize() + "Wasm",
            "methods": [],
            "imports": set(),
        })
        for method in entry["methods"]:
            try:
                m = build_method(entry, method, class_by_name, config)
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


def render_all(bridges, skipped, json_path, config):
    env = Environment(
        loader=FileSystemLoader(BASE_DIR / "templates"),
        trim_blocks=True,          # {% %} の直後の改行を消す
        lstrip_blocks=True,        # {% %} の前のインデントを消す
        keep_trailing_newline=True,
    )
    common = {"package": config.java_package, "bridges": bridges, "skipped": skipped, "json_path": json_path}

    java_dir = config.java_out_dir
    java_dir.mkdir(parents=True, exist_ok=True)
    outputs = {java_dir / "Matrices.java": env.get_template("matrices.java.j2").render(common),
               java_dir / "NfcWasm.java": env.get_template("nfc_wasm.java.j2").render(common),
               config.js_out_file: env.get_template("nfc.js.j2").render(common),
               config.report_file: env.get_template("report.md.j2").render(common)}
    for bridge in bridges:
        outputs[java_dir / f"{bridge['class_name']}.java"] = \
            env.get_template("bridge.java.j2").render(common, bridge=bridge)

    for path, text in outputs.items():
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(text, encoding="utf-8")
        print(f"生成: {path.relative_to(PROJECT_ROOT)}")


def main():
    parser = argparse.ArgumentParser(description="nfc_api.json から TeaVM 用のブリッジコードを生成する")
    parser.add_argument("--json", type=Path, default=DEFAULT_JSON, help="JavaParser が出力した JSON")
    parser.add_argument("--config", type=Path, default=DEFAULT_CONFIG, help="生成の設定ファイル（YAML）")
    args = parser.parse_args()

    config = load_config(args.config)

    with open(args.json, encoding="utf-8") as f:
        api = json.load(f)

    bridges, skipped = build_bridges(api["classes"], config)
    render_all(bridges, skipped, args.json, config)

    count = sum(len(b["methods"]) for b in bridges)
    print(f"公開したメソッド: {count} 個 / 対象外: {len(skipped)} 個（理由は {config.report_file.relative_to(PROJECT_ROOT)}）")


if __name__ == "__main__":
    main()
