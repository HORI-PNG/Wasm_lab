# Artifact：step03-extra-exports（手書きの関数を自動生成版に差し込む）

## 1. 概要

WebGPU と Wasm（CPU）の速さ比べの計測で使う手書きの関数（`multiply`・`prepareMultiply`・`multiplyPrepared`）を、自動生成版の公開窓口（`org.example.generated.NfcWasm`）から呼べるようにした。
設定ファイル `codegen/nfc.yaml` に **`extra_exports`**（「手書きのブリッジのこの関数も公開する」という一覧）を書くと、生成器が公開窓口に「手書きの関数を呼ぶだけ」のメソッドを足す。
今までの 69 個の生成物は変わらず、3 個の関数が足されただけであることを確かめた。

- ブランチ：`step03-extra-exports`（`step02-config-yaml` から作成）
- 日付：2026-11-13
- 対応する資料：「P6P7_対策の検討.pdf」p.4（P6 ②）
- 承認：2026-11-13 に計画を承認（a① step03 は ② だけにし、③ は step04 で計画し直す、b① 差し込んだ関数は wasm の関数を直接呼ぶ、c① `rename`・`defaults` は作らない、d① MarkupSafe の版も固定する）。計画は [`plan.md`](plan.md)

## 2. タスク

- [x] ブランチ `step03-extra-exports` を作る
- [x] `requirements.txt` に `MarkupSafe==3.0.4` を足す（step02 の残った課題）
- [x] `MatrixWasm` と 3 つのメソッドに `public` を付ける（計算の中身は変えない）
- [x] `codegen/nfc.yaml` に `extra_exports` を足す
- [x] 生成器：`extra_exports` を読み、確かめ（項目・形・名前の重なり）、公開窓口に差し込む
- [x] テンプレート：`nfc_wasm.java.j2` に差し込む関数、`report.md.j2` に一覧を足す
- [x] 検算：`wasm-verify.mjs` に積の確認 3 項目、`JvmReference.java` に手書き版との比較 1 組を足す
- [x] 再生成し、今までの生成物が変わらず、3 個が足されただけであることを確かめる
- [x] `verify-tools/run.sh` で検算する
- [x] `extra_exports` の書き間違いで止まることを確かめる
- [x] `CLAUDE.md` を更新する

## 3. 実装計画

### 自動化の「決まった抜け道」

`prepareMultiply` と `multiplyPrepared` は、Java 側に行列を保持しておく（状態を持つ）関数で、「NFC のクラスを見つけて、型を変換して呼ぶ」という自動生成のルールでは作れない。
そこで、生成器に**手書きの関数をつなぐ口**を用意した。

```text
MatrixWasm.java（手書きの Java。計算の中身はここ。変えない）
   ^ 呼ぶだけ
NfcWasm.java（自動生成の公開窓口 = mainClass）
   今までの 69 個 ＋ 「MatrixWasm の関数を呼ぶだけ」のメソッド 3 個
   ^ 何を足すかは
codegen/nfc.yaml の extra_exports（name・call・params・returns）
```

- 手作業の部分は、**YAML の 3 項目**と、もともとある手書きの Java だけ。生成物は手で直さないので、再生成しても消えない。
- 何を手で足したかは、YAML と `build/codegen_report.md` の「手書きのブリッジから差し込んだ関数」の表で分かる。

### 守り

- 設定の確認（`validate_config()`）に、`extra_exports` の確認を足した：項目の不足、値の形、`params` が「型 名前」の形か、`name` が Java の名前として使えるか、`call` が「パッケージ名.クラス名.メソッド名」の形か、`name` どうしの重なり。
- 差し込む関数の名前が、自動生成した 69 個の関数の名前と重なったら、生成を止める（`check_extra_export_names()`）。書き出しの前に確かめるので、止まったときは生成物は書き換わらない。
- `extra_exports` は書かなくてもよい（書かなければ何も差し込まない）。他のライブラリの設定には不要なため。

## 4. ウォークスルー

```bash
git switch -c step03-extra-exports

# MarkupSafe の固定
#   requirements.txt：Jinja2==3.1.2、MarkupSafe==3.0.4、PyYAML==6.0.1
.venv/bin/pip install -r requirements.txt

# 手書きのブリッジを public に
#   app/src/main/java/org/example/MatrixWasm.java：class と multiply・prepareMultiply・multiplyPrepared に public

# 設定と生成器
#   codegen/nfc.yaml：extra_exports を追加
#   JavaGenerator.py：EXTRA_EXPORT_SCHEMA、validate_extra_exports()、check_extra_export_names()、Config.extra_exports
#   templates/nfc_wasm.java.j2、templates/report.md.j2
.venv/bin/python app/src/main/Python/JavaGenerator.py
#   → 公開したメソッド 69 個 / 対象外 3 個 ＋ 手書きのブリッジから差し込んだ関数 3 個
#   → 変わった生成物は NfcWasm.java だけ（最後に 17 行追加。削除なし）

# 検算
#   wasm-verify.mjs：multiply（4x4 × 4x4、5x3 × 3x2）、prepareMultiply → multiplyPrepared の確認
#   JvmReference.java：手書き版との比較に multiply を追加
verify-tools/run.sh
```

## 5. 変更したファイル

| ファイル | 内容 |
|---|---|
| `codegen/nfc.yaml` | `extra_exports`（3 項目）と、その書き方の説明のコメントを追加（21 行） |
| `app/src/main/Python/JavaGenerator.py` | `extra_exports` の読み込み・確認・名前の重なりの確認、表示を追加（413 行 → 479 行） |
| `app/src/main/Python/templates/nfc_wasm.java.j2` | 差し込む関数を公開窓口に書く部分を追加 |
| `app/src/main/Python/templates/report.md.j2` | 「手書きのブリッジから差し込んだ関数」の表を追加 |
| `app/src/main/java/org/example/MatrixWasm.java` | クラスと 3 つのメソッドに `public`、説明のコメントを追加（計算は変えていない） |
| `app/src/main/java/org/example/generated/NfcWasm.java` | 生成物。最後に 3 つのメソッドが追加された（17 行） |
| `app/src/verify/java/verify/JvmReference.java` | 手書き版との比較に `multiply` を追加 |
| `verify-tools/wasm-verify.mjs` | 差し込んだ関数の確認 3 項目と、そのための `flat`・`unpackPacked` を追加 |
| `requirements.txt` | `MarkupSafe==3.0.4` を追加 |
| `CLAUDE.md` | `extra_exports` の説明を追加 |
| `logs/step03-extra-exports/`、`logs/README.md` | 記録 |

## 6. 検証結果

`verify-tools/run.sh` の結果（終了コード 0）：

| 確認 | step02 まで | step03 |
|---|---|---|
| 公開関数 | 69 個 | **72 個**（69 ＋ 差し込み 3） |
| ① JVM との一致 | 207/207 | **216/216**（完全一致。例外 45 → 54 回） |
| ② 数学的な性質 | 25/25 | **28/28**（下の 3 項目を追加。どれも誤差 0） |
| ③ 手書き版との一致 | 27/27 | **28/28**（`multiply` を追加。値が完全に同じ） |

足した性質の確認（Wasm の関数を直接呼ぶ）：

| 確認 | 相対誤差 |
|---|---|
| `multiply`（4x4 × 4x4）が、JS で計算した `A·B` と一致する | 0 |
| `multiply`（5x3 × 3x2）が、JS で計算した `R·M` と一致する | 0 |
| `prepareMultiply` の後に `multiplyPrepared` を呼ぶと、積の「先頭の成分 ＋ 最後の成分」が返る | 0 |

生成物の確認：

- 再生成で変わったのは `NfcWasm.java` だけで、変更は**末尾に 17 行の追加のみ**（削除・変更なし）。他のブリッジ（`EigWasm.java` など）と `nfc.generated.js` は変わっていない。
- `build/codegen_report.md` も、末尾に「手書きのブリッジから差し込んだ関数（3 個）」の表が足されただけ。

設定の書き間違いのときの表示（実際の出力。どれも終了コード 1 で止まり、生成物は書き換えなかった）：

| わざと作った問題 | 表示 |
|---|---|
| `name` を自動生成の関数と同じ `realExp` にする | `extra_exports の name が、自動生成した関数の名前と重なっている: ['realExp']` |
| `returns` を消す | `項目 extra_exports[1].returns がない` |
| 引数を `"double[]a"`（空白なし）と書く | `項目 extra_exports[0].params の 'double[]a' は「型 名前」の形の文字列で書く` |
| 2 つの `name` を同じにする | `項目 extra_exports の name が重なっている: ['multiply']` |

## 7. 分かったこと・残った課題

1. **差し込んだ 3 つの関数は、「① JVM との一致」では例外どうしの一致にしかならない**（計画で予想したとおり）。
   - `JvmReference` の引数の決め方（`double[]` の後に `int, int` なら行列）は、`multiply(a, aRows, aColumns, b, bColumns)` の `b` に当てはまらない。`b` の後の `int` は 1 つなので、`b` はベクトル（長さ 8）とみなされ、行列の大きさが合わずに例外になる（JVM・Wasm とも）。
   - `multiplyPrepared` は名前順で `prepareMultiply` より先に呼ばれるので、行列が用意されておらず例外になる（JVM・Wasm とも）。
   - → 正しさは、② の性質の確認（3 項目、誤差 0）と、③ の手書き版との比較で確かめている。`JvmReference` の引数の決め方を広げるかは、step04（入口を広げると引数の形も増える）で一緒に考える。
2. **JS ラッパー（`nfc.generated.js`）には入れていない**（b① の決定どおり）。JS からは `nfc.exports.multiply(1 次元配列, 行数, 列数, 1 次元配列, 列数)` のように呼ぶ。戻り値は pack 形式（`[1, 行数, 列数, データ…]`）。
3. **③ 変換ルールの追加は step04 へ**：今の入口クラスではルールを足しても関数がほぼ増えないので、入口の広げ方（`DoubleMatrixUtil` などの static メソッドの集まりのクラス）とあわせて計画し直す。

## 保存（コミットと push）

| 日付 | コミット | 内容 | GitHub（origin） | Gitblit（ユーザーが push） |
|---|---|---|---|---|
| 2026-11-13 | この記録を含むコミット | step03 の作業 | push 済み | ユーザーが push |

## 8. 確認用コマンド

すべて `make_wasm/TeaVM` フォルダで実行する。

**① このステップのブランチに切り替える**

```bash
git switch step03-extra-exports
```

期待する結果：`Switched to branch 'step03-extra-exports'`。

**② ライブラリの版をそろえる**（MarkupSafe を足したので、1 回だけ）

```bash
.venv/bin/pip install -r requirements.txt
```

期待する結果：エラーなく終わる。`.venv/bin/pip freeze` で `Jinja2==3.1.2`・`MarkupSafe==3.0.4`・`PyYAML==6.0.1` が並ぶ。

**③ 生成器を動かす**

```bash
.venv/bin/python app/src/main/Python/JavaGenerator.py
```

期待する結果：最後の 2 行が `公開したメソッド: 69 個 / 対象外: 3 個（理由は build/codegen_report.md）` と `手書きのブリッジから差し込んだ関数: 3 個（multiply, prepareMultiply, multiplyPrepared）`。

**④ 生成物が変わっていないかを見る**（③の直後に実行する）

```bash
git status --short app/src/main/java/org/example/generated app/src/main/webapp/nfc.generated.js
```

期待する結果：**何も表示されない**（このブランチの生成物と同じ）。

**⑤ 差し込んだ関数を見る**

```bash
tail -20 app/src/main/java/org/example/generated/NfcWasm.java
```

期待する結果：`// ---- 手書きのブリッジから差し込んだ関数` の下に、`multiply`・`prepareMultiply`・`multiplyPrepared` が `org.example.MatrixWasm.…` を呼ぶ形で並ぶ。

**⑥ ビルドから検算までまとめて実行する**（目安：数分）

```bash
verify-tools/run.sh
```

期待する結果：最後の 3 行が `JVM との一致: 216 回中 完全一致 216・誤差内 0・不一致 0`、`数学的な性質: 28 項目中 OK 28`、`手書き版との比較: 28 組中 一致 28`。

**⑦ 差し込んだ関数を Node から直接呼ぶ**（⑥の後に実行する）

```bash
node -e 'require("./app/build/generated/teavm/wasm-gc/nfc.wasm-runtime.js"); TeaVM.wasmGC.load("./app/build/generated/teavm/wasm-gc/nfc.wasm", {nodejs: true}).then(t => console.log(Array.from(t.exports.multiply([1,2,3,4], 2, 2, [5,6,7,8], 2))))'
```

期待する結果：`1, 2, 2, 19, 22, 43, 50` の 7 つの数が表示される（数行に折り返されることがある）。2x2 の行列の積 `[[19, 22], [43, 50]]` が pack 形式（`[個数, 行数, 列数, データ…]`）で返っている。

**⑧ Gitblit に push する**（ユーザーが実行する）

```bash
git push gitblit step03-extra-exports
```

期待する結果：`* [new branch]  step03-extra-exports -> step03-extra-exports`。
