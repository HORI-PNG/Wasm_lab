# Artifact：step02-config-yaml（生成設定を YAML に分ける）

## 1. 概要

コード生成器（`JavaGenerator.py`）の中に直接書かれていた **NFC 専用の値**（入口クラスの条件・型の変換ルール・名前の言い換え・出力先）を、設定ファイル **`codegen/nfc.yaml`** に分けた。
あわせて、生成器が使う Python のライブラリ（jinja2・PyYAML）を、プロジェクト専用の**仮想環境 `.venv/`** に入れて使うようにした。
**生成されるコードは 1 文字も変わっていない**ことを確かめた。

- ブランチ：`step02-config-yaml`（`step01-generated-verify` から作成）
- 日付：2026-11-13
- 対応する資料：「P6P7_対策の検討.pdf」p.2（進める順番）・p.4（P6 ②⑥）
- 承認：2026-11-13 に計画を承認（決めてほしいこと：a① `codegen/nfc.yaml`、b① Java の式も YAML に出す、c① `rename` などの枠は作らない、d② 仮想環境を作る、e① システムのライブラリは削除しない）。計画は [`plan.md`](plan.md)

## 2. タスク

- [x] ブランチ `step02-config-yaml` を作る
- [x] `requirements.txt` を作る（`Jinja2==3.1.2`、`PyYAML==6.0.1`）
- [x] 仮想環境 `.venv/` を作り、ライブラリを入れる。`.gitignore` に `.venv/` を足す
- [x] 仮想環境のライブラリが使われていることを確かめる
- [x] 変更前の生成器を仮想環境で動かし、生成物に差分が出ないこと（基準）を確かめる
- [x] `codegen/nfc.yaml` を作り、値が今の生成器と同じであることを確かめる
- [x] 生成器が YAML を読み込むように書き換える（`--config`、項目の確認）
- [x] 書き換えた後も生成物が完全に同じであることを確かめる
- [x] 設定に問題があるとき、分かりやすいエラーで止まることを確かめる
- [x] `verify-tools/run.sh` で検算する
- [x] `CLAUDE.md` を更新する

## 3. 実装計画

### 「手順」はコードに、「値」は設定ファイルに

| 置き場所 | 書くもの | 例 |
|---|---|---|
| `JavaGenerator.py`（コード） | どう選ぶか・どう変換するか（手順） | 入口クラスを「名前の末尾・先頭・具象クラス・ジェネリックでない」で選ぶ処理、getter を探す処理、`@Deprecated` やジェネリックメソッドを除く処理、公開名の重複を解決する処理 |
| `codegen/nfc.yaml`（設定） | その手順で使う値（NFC に合わせたもの） | 末尾 `Solver` / `Decomposer` …、先頭 `DoubleReal` → `real`、`SingularValue` → `Svd`、型の変換ルール、出力先 |

こう分けると、P7 で他のライブラリを試すときは、生成器を変えずに設定ファイル（例：`codegen/jama.yaml`）を書けばよくなる。step03 で変換ルールを増やすときも、YAML に足すだけで済む。

### 「何も変えていない」ことを示す方法

変更の前後で、生成物（`org/example/generated/*.java`、`nfc.generated.js`、`build/codegen_report.md`）が 1 文字も変わらないことを確かめる。

1. 書き換える前の生成器で、仮想環境を使って再生成 → 差分がないこと（基準）
2. YAML の値が、生成器の元の値（Python の辞書・タプル）とまったく同じであることを、プログラムで比べる
3. 書き換えた後の生成器で再生成 → 差分がないこと

### 設定の確かめ方

YAML は書き間違いをしやすい（`1e-12` が文字列になる、インデントで意味が変わる、など）。そこで、読み込んだ直後に「必要な項目があるか」「値の形（文字列・リスト・対応表）が合っているか」を調べ、問題があれば**全部まとめて**表示して止まるようにした。

## 4. ウォークスルー

```bash
git switch -c step02-config-yaml

# 2-0. 仮想環境
#   requirements.txt に Jinja2==3.1.2、PyYAML==6.0.1
python3 -m venv .venv
.venv/bin/pip install -r requirements.txt
#   .gitignore に .venv/ を追加
.venv/bin/python -c "import yaml, jinja2; print(yaml.__file__, jinja2.__file__)"
#   → どちらも .venv/lib/python3.12/site-packages/ の中を指す

# 2-1. 基準：変更前の生成器を仮想環境で動かす → 生成物の差分なし、レポートも同じ
.venv/bin/python app/src/main/Python/JavaGenerator.py

# 2-2. codegen/nfc.yaml を作成
#   生成器を import して、YAML の値と元の値をプログラムで比べた → 9 項目すべて一致

# 2-3. 生成器の書き換え
#   - 定数（ENTRY_SUFFIXES・PARAM_RULES など）を削除
#   - load_config()・validate_config()・Config クラスを追加
#   - 手順の関数（select_entry_classes など）が config を引数で受け取るように変更
#   - --config 引数を追加（省略時は codegen/nfc.yaml）

# 2-4. 確認
.venv/bin/python app/src/main/Python/JavaGenerator.py   # 生成物の差分なし、レポートも同じ
verify-tools/run.sh                                      # 207/207・25/25・27/27、終了コード 0
```

途中で気づいたこと：

- `pip install` で、Jinja2 が使う **MarkupSafe 3.0.4** も一緒に入った。計画では Jinja2 と PyYAML の 2 つだけを固定することにしていたので、MarkupSafe は `requirements.txt` に書いていない（残った課題に記録）。

## 5. 変更したファイル

| ファイル | 内容 |
|---|---|
| `codegen/nfc.yaml` | 新規（125 行）。NFC 用の生成設定。書くときの注意（YAML の落とし穴）もコメントで書いた |
| `app/src/main/Python/JavaGenerator.py` | 定数を削除し、設定の読み込み・確認を追加（342 行 → 413 行。追加 163 行・削除 92 行） |
| `requirements.txt` | 新規。`Jinja2==3.1.2`、`PyYAML==6.0.1` |
| `.gitignore`（TeaVM フォルダ） | `.venv/` を追加 |
| `CLAUDE.md` | 仮想環境での実行方法、`codegen/` と `requirements.txt` の説明、生成のしくみの説明を更新 |
| `logs/step02-config-yaml/plan.md` | `next-plan/` から移動（承認済み・実行済み） |
| `logs/README.md`、このファイル | 記録 |

生成物（`org/example/generated/`・`nfc.generated.js`）は**変わっていない**ので、コミットにも含まれない。

### 生成器の主な変更点

| 変更前 | 変更後 |
|---|---|
| `ENTRY_SUFFIXES = ("Solver", …)` などの定数 | なし（`codegen/nfc.yaml` に移動） |
| `select_entry_classes(classes)` | `select_entry_classes(classes, config)` |
| `find_getters(box, class_by_name)` | `find_getters(box, class_by_name, getter_rules)` |
| `build_method(entry, method, class_by_name)` | `build_method(entry, method, class_by_name, config)` |
| `base_export_name(class_name, method_name)` | `base_export_name(class_name, method_name, config)` |
| `build_bridges(classes)` / `render_all(…)` | `config` を受け取る |
| （なし） | `CONFIG_SCHEMA`・`RULE_SCHEMA`（必要な項目と値の形の表）、`validate_config()`（問題の一覧を作る）、`load_config()`（読み込み・確認・エラー表示）、`Config`（設定の値をまとめて持つクラス） |
| `main()` | `--config` 引数を追加し、最初に `load_config()` を呼ぶ |

## 6. 検証結果

| 確認 | 結果 |
|---|---|
| 仮想環境のライブラリが使われているか | **はい**。`yaml`・`jinja2` とも `.venv/lib/python3.12/site-packages/` から読み込まれた |
| 変更前（基準）の生成物 | 仮想環境で再生成しても、差分なし・レポートも同じ |
| YAML の値と元の値 | 引数・戻り値・getter の変換ルール、末尾、先頭（順番も）、言い換え、パッケージ、出力先 3 つ、**すべて一致** |
| 変更後の生成物 | **差分なし**（69 個公開・対象外 3 個も同じ）。`build/codegen_report.md` も変更前と同じ |
| `--config codegen/nfc.yaml` を明示した場合 | 同じ結果 |
| 設定に問題があるとき | 下の 6 通りすべてで、問題を表示して終了コード 1 で止まり、生成物は書き換えなかった |
| `verify-tools/run.sh` | **JVM との一致 207/207、数学的な性質 25/25、手書き版との一致 27/27、終了コード 0**（step01 と同じ） |

設定に問題があるときの表示（実際の出力）：

| わざと作った問題 | 表示 |
|---|---|
| `entry.class_suffixes` を消す | `項目 entry.class_suffixes がない` |
| `double[][]` の引数ルールの `arg` を消す | `項目 types.params.double[][].arg がない` |
| `class_suffixes` をリストではなく文字列にする | `項目 entry.class_suffixes はリストで書く` |
| `output` をまるごと消す | `項目 output がない（または「名前: 値」の形になっていない）` |
| YAML の書き方を間違える（インデント） | `設定ファイルを YAML として読めない: …`（どこが違うかも表示） |
| 存在しないファイルを指定 | `設定ファイルが見つからない: codegen/none.yaml` |

`JavaGenerator.py` に NFC 固有の値（`Solver`、`DoubleReal`、`SingularValue` など）は、説明のコメントの例を除いて残っていないことを `grep` で確かめた。

## 7. 分かったこと・残った課題

1. **生成器と NFC の設定が分かれた**：NFC に合わせた値は `codegen/nfc.yaml` だけにある。step03 で変換ルールを増やすときは、YAML に足して再生成し、`verify-tools/run.sh` で確かめる流れになる。
2. **まだ生成器に残っている NFC 寄りの部分**（計画の「やらないこと」どおり、P7 で扱う）：
   - 入力の JSON の既定のパス `build/nfc_api.json`（`--json` で変えられる）
   - テンプレートとファイル名の中の名前（`NfcWasm.java`、`loadNfcGenerated` など）
   - 入口クラスを「名前」で選ぶ手順そのもの（型で選ぶ方式は P7）
3. **MarkupSafe の版が固定されていない**：Jinja2 が使う MarkupSafe（今は 3.0.4）は `requirements.txt` に書いていない。他の PC で入れ直すと、新しい版が入る可能性がある。生成結果に影響する可能性は低いが、完全に固定するなら `requirements.txt` に足す（次の計画で相談する）。
4. **システムの jinja2・PyYAML は残っている**（e① の決定どおり）。プロジェクトは `.venv/bin/python` で動かす限り、システムのものは使わない。`python3 app/src/main/Python/JavaGenerator.py`（システムの Python）でも今は動いてしまうので、実行のしかたを間違えないよう注意する。

## 保存（コミットと push）

| 日付 | コミット | 内容 | GitHub（origin） | Gitblit（ユーザーが push） |
|---|---|---|---|---|
| 2026-11-13 | この記録を含むコミット | step02 の作業 | push 済み | ユーザーが push |

## 8. 確認用コマンド

すべて `make_wasm/TeaVM` フォルダで実行する。

**① このステップのブランチに切り替える**

```bash
git switch step02-config-yaml
```

期待する結果：`Switched to branch 'step02-config-yaml'`。

**② 仮想環境を作る**（初めてのときだけ。この PC ではもう作ってあるので不要）

```bash
python3 -m venv .venv
```

```bash
.venv/bin/pip install -r requirements.txt
```

期待する結果：エラーなく終わる。

**③ 仮想環境のライブラリが使われているかを見る**

```bash
.venv/bin/python -c "import yaml, jinja2; print(yaml.__file__); print(jinja2.__file__)"
```

期待する結果：2 行とも `…/TeaVM/.venv/lib/python3.12/site-packages/…` と表示される（`/usr/lib/python3/…` ではない）。

**④ 生成器を動かす**

```bash
.venv/bin/python app/src/main/Python/JavaGenerator.py
```

期待する結果：最後に `公開したメソッド: 69 個 / 対象外: 3 個（理由は build/codegen_report.md）`。

**⑤ 生成物が変わっていないかを見る**（④の直後に実行する）

```bash
git status --short app/src/main/java/org/example/generated app/src/main/webapp/nfc.generated.js
```

期待する結果：**何も表示されない**（変更されたファイルがない）。

**⑥ 設定に問題があるときに止まるかを試す**（設定の一部を消した写しを作って使う）

```bash
grep -v '"Solver", "Decomposer"' codegen/nfc.yaml > /tmp/bad.yaml && .venv/bin/python app/src/main/Python/JavaGenerator.py --config /tmp/bad.yaml
```

期待する結果：`設定ファイルに問題がある: /tmp/bad.yaml` と `項目 entry.class_suffixes がない` が表示され、生成は行われない。

**⑦ ビルドから検算までまとめて実行する**（目安：数分）

```bash
verify-tools/run.sh
```

期待する結果：最後の 3 行が `JVM との一致: 207 回中 完全一致 207・誤差内 0・不一致 0`、`数学的な性質: 25 項目中 OK 25`、`手書き版との比較: 27 組中 一致 27`。

**⑧ Gitblit に push する**（ユーザーが実行する）

```bash
git push gitblit step02-config-yaml
```

期待する結果：`* [new branch]  step02-config-yaml -> step02-config-yaml`。応答がないまま止まる場合は、Gitblit のサーバーにつながっていない（学外からの接続など）。
