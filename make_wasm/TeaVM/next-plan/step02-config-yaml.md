# 計画：step02-config-yaml（生成設定を YAML に分ける）

- 状態：**確認待ち**（承認されるまで実行しない）
- 対応する資料：「P6P7_対策の検討.pdf」p.2（進める順番）・p.4（P6 ②⑥）
- ブランチ：`step02-config-yaml`（`step01-generated-verify` から作る）

## 1. 目的

コード生成器（`JavaGenerator.py`）の中に直接書かれている **NFC 専用の値**を、設定ファイル（YAML）に分ける。

- これ以降の ③（変換ルールの追加）が「YAML に足すだけ」の作業になるようにする。
- P7（汎用化）の第一歩として、「生成器は共通、ライブラリごとに違うのは設定ファイルだけ」という形にする。
- **このステップでは、生成されるコードは 1 文字も変えない**（書き換えたのは置き場所だけ、と示せるようにする）。

## 2. やること

### 2-1. 事前の基準づくり

1. ブランチ `step02-config-yaml` を作る。
2. 今の生成器のまま再生成し、生成物（`org/example/generated/*.java`、`nfc.generated.js`）に**差分が出ないこと**を確かめる。
   - 差分が出た場合は、今のリポジトリの生成物と生成器がずれているということなので、作業を止めて報告する。

### 2-2. YAML に出す値

今の `JavaGenerator.py` で、NFC に依存している値は次のとおり。これを YAML に移す。

| YAML の項目 | 今の生成器の場所 | 内容 |
|---|---|---|
| `library.name` | （なし） | ライブラリ名（`nfc`） |
| `output.java_package` | `JAVA_PACKAGE` | 生成する Java のパッケージ（`org.example.generated`） |
| `output.java_dir`・`output.js_file`・`output.report_file` | `JAVA_OUT_DIR` など | 出力先 |
| `entry.class_suffixes` | `ENTRY_SUFFIXES` | 手順 A：入口とみなすクラス名の末尾 |
| `entry.class_prefixes` | `ENTRY_PREFIXES` | 手順 B：対象にするクラス名の先頭と、公開名の先頭に付ける語（`DoubleReal` → `real`） |
| `naming.abbreviations` | `CORE_ABBREVIATIONS` | 公開名を短くする言い換え（`SingularValue` → `Svd`） |
| `types.params` | `PARAM_RULES` | 手順 C：引数の型の変換ルール |
| `types.returns` | `RETURN_RULES` | 手順 C：戻り値の型の変換ルール |
| `types.getters` | `GETTER_RULES` | 手順 D：箱の getter の変換ルール |

YAML のイメージ（`codegen/nfc.yaml`）：

```yaml
library:
  name: nfc

output:
  java_package: org.example.generated
  java_dir: app/src/main/java/org/example/generated
  js_file: app/src/main/webapp/nfc.generated.js
  report_file: build/codegen_report.md

entry:                          # 手順 A・B（NFC の名前の付け方に依存）
  class_suffixes: [Solver, Decomposer, Analyzer, ExponentialMatrix]
  class_prefixes:               # クラス名の先頭 → 公開名の先頭
    DoubleReal: real
    DoubleComplex: complex

naming:
  abbreviations:
    SingularValue: Svd

types:                          # 手順 C・D（{n} は引数名）
  params:
    double[][]:
      java: ["double[] {n}", "int {n}Rows", "int {n}Columns"]
      arg: "Matrices.toMatrix({n}, {n}Rows, {n}Columns)"
      js: matrix
    # …（今の PARAM_RULES をそのまま移す）
  returns:
    DoubleMatrix:
      java: "double[]"
      expr: "Matrices.pack(result.getDoubleElements())"
      js: matrix
    # …
  getters:
    # …
```

### 2-3. 生成器の変更

- 起動時に YAML を読み込む（`--config` で指定。省略時は `codegen/nfc.yaml`）。読み込みには PyYAML を使う（この環境にはインストール済み）。
- 必要な項目が足りないときは、「どの項目が足りないか」を表示して止まるようにする。
- 判断の手順（手順 A〜D の処理、ジェネリックや `@Deprecated` の除外、名前の重複の解決）はコードに残す。YAML に書くのは、その手順で使う**値**だけにする。

### 2-4. 確認と記録

1. 新しい生成器で再生成し、生成物が 2-1 と**完全に同じ**であることを `git diff` で確かめる。
2. `verify-tools/run.sh` を実行し、step01 と同じ結果（JVM との一致 207/207、数学的な性質 25/25、手書き版との一致 27/27）になることを確かめる。
3. YAML の項目をわざと 1 つ消して、分かりやすいエラーで止まることを確かめる。
4. `logs/step02-config-yaml/Artifact.md` を書き、この計画を `logs/step02-config-yaml/plan.md` に移す。`CLAUDE.md` の説明も更新する。
5. ブランチ `step02-config-yaml` でコミットする（push はしない）。

## 3. やらないこと

- 新しい変換ルールの追加（`DoubleNumber` など）→ step03（③）
- 公開名の書き換え（`rename`）・引数の既定値（`defaults`）・手書きの追加分（`extra_bridges`）→ step03（②）
- テンプレートの中の NFC 固有の名前（`NfcWasm`、`loadNfcGenerated` など）の設定化 → P7（汎用化）でまとめて扱う
- 名前ではなく型で入口を選ぶ仕組み → P7

## 4. 確認方法（「できた」の基準）

- [ ] 生成器を変える前と後で、生成物の差分がない
- [ ] `verify-tools/run.sh` が step01 と同じ結果で成功する
- [ ] YAML の項目が足りないとき、どの項目かが分かるエラーで止まる
- [ ] `JavaGenerator.py` に NFC 固有の値（`Solver`、`DoubleReal` など）が直接書かれていない

## 5. 決めてほしいこと

| # | 内容 | 選択肢 | おすすめ |
|---|---|---|---|
| a | YAML の置き場所 | ① `codegen/nfc.yaml`（TeaVM フォルダ直下に「生成の設定」フォルダを作る）<br>② `app/src/main/Python/config/nfc.yaml`（生成器の隣） | **①**：生成器のコードと設定が分かれ、P7 で他のライブラリの設定（`codegen/jama.yaml` など）を並べやすい |
| b | 変換ルールの Java の式（`Matrices.toMatrix({n}, …)` など）も YAML に出すか | ① 出す ② 型の名前の一覧だけ出し、式はコードに残す | **①**：③で型を増やす作業が YAML だけで済む。ただし YAML の中に Java の式が入るので、少し読みにくくなる |
| c | `rename`・`defaults`・`extra_bridges` の枠を今回作っておくか | ① 今回は作らない ② 空の枠だけ作る | **①**：使うのは step03 なので、そのときに中身と一緒に作る方が、動かない設定が残らない |

## 6. 補足

- 生成器（Python）は Claude が書き、あとで詳しい解説を作る、という以前の取り決めに従う。今回の変更点の説明は `Artifact.md` にまとめ、詳しい解説が必要なら別に作る。
- 作業時間の目安：生成器の変更と確認を合わせて、1 回の作業で終わる規模。
