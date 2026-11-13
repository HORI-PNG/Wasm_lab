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

- 起動時に YAML を読み込む（`--config` で指定。省略時は `codegen/nfc.yaml`）。読み込みには PyYAML を使う（入れ方は「5. 決めてほしいこと」の d と「6. 補足」を参照）。
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
| d | 生成器（Python）が使うライブラリ（jinja2・PyYAML）の入れ方 | ① 今のまま、Ubuntu のシステムに入っているものを使う ② プロジェクト専用の仮想環境（`TeaVM/.venv/`）を作り、`requirements.txt` で版を固定する | **②**：他の PC や他の人でも同じ版で動かせ、研究の再現性が上がる。システムの Python を汚さない。ただし、生成器を動かすコマンドが `.venv/bin/python …` に変わる |

## 6. 補足

### 設定ファイルを JSON ではなく YAML にする理由（2026-11-13 に確認）

ファイルの形式は、「誰が書いて、誰が読むか」で選ぶ。

| ファイル | 書く人 | 読む人 | 形式 |
|---|---|---|---|
| `build/verify/jvm-reference.json`（step01 の正解データ） | プログラム（Java） | プログラム（JavaScript） | **JSON**：言語をまたいでプログラム同士で受け渡すため。人は手で書かない |
| `codegen/nfc.yaml`（この step の生成設定） | **人**（ユーザーや Claude が手で書き足す） | プログラム（Python の生成器だけ） | **YAML**：人が編集するため |

- **プログラムにとっての渡しやすさは同じ**：生成設定を読むのは Python の生成器だけで、読み込んだ後は YAML でも JSON でも同じ Python の辞書になる。差が出るのは、人が書くときの手間。
- **YAML の利点**：
  - コメントが書ける。除外の理由など「なぜそうしたか」を設定の中に残せる（JSON はコメントを書けない）。
  - `"`・`{ }`・`,` が少なく、手で書いても間違えにくい（JSON は、最後の要素の後に `,` を付けるとエラーになる、など）。
- **YAML の注意点**（PyYAML 6.0.1 で実際に試して確認）：
  - `1e-12` は**文字列**として読まれる。数にするには `1.0e-12` と書く。
  - `no`・`yes` は**真偽値**になる。
  - `double[]` のように `[ ]` を含む文字列は `"` で囲む。
  - インデントの間違いで意味が変わる。
  - → 対策として、生成器が設定を読むときに「項目が足りない・形が違う」を調べ、分かりやすいエラーで止まるようにする（2-3）。
- YAML は JSON の書き方もそのまま読めるので、後で JSON の書き方に寄せたくなっても生成器は変えずに済む。

### 生成器が使うライブラリの入れ方（d の補足）

2026-11-13 に調べた今の状態：

| ライブラリ | 版 | 入っている場所 | 入れ方 |
|---|---|---|---|
| jinja2 | 3.1.2 | `/usr/lib/python3/dist-packages/` | Ubuntu のパッケージ（`apt` の `python3-jinja2`） |
| PyYAML | 6.0.1 | `/usr/lib/python3/dist-packages/` | Ubuntu のパッケージ（`apt` の `python3-yaml`） |

- どちらも、ホームフォルダ（`~/.local`）でも仮想環境でもなく、**Ubuntu のシステム全体**に入っている。Claude が入れたものではなく、もともと入っていた。
- この Python（3.12）は、システムに `pip install` できない設定（`EXTERNALLY-MANAGED`）になっている。版を変えたり、ライブラリを追加したりするには、`apt` か仮想環境が必要。

| | ① システムのまま | ② 仮想環境（`.venv/`） |
|---|---|---|
| 準備 | 不要 | `python3 -m venv .venv` と `.venv/bin/pip install -r requirements.txt` |
| 版 | Ubuntu の更新で変わりうる | `requirements.txt` で固定できる |
| 他の PC・他の人 | 同じ版が入っているとは限らない | 同じ手順で同じ版をそろえられる |
| 実行のしかた | `python3 app/src/main/Python/JavaGenerator.py` | `.venv/bin/python app/src/main/Python/JavaGenerator.py` |
| git | 何も足さない | `requirements.txt` を足す。`.venv/` 自体は git に入れない（`.gitignore` に追加） |

d で ② を選んだ場合、2-3 の前に「仮想環境を作り、jinja2・PyYAML を入れ、`requirements.txt` と `.gitignore` を追加する」作業を足し、`CLAUDE.md` のコマンドも書き換える。

### その他

- 生成器（Python）は Claude が書き、あとで詳しい解説を作る、という以前の取り決めに従う。今回の変更点の説明は `Artifact.md` にまとめ、詳しい解説が必要なら別に作る。
- 作業時間の目安：生成器の変更と確認を合わせて、1 回の作業で終わる規模。
