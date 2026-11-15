# Artifact：step04-utility-entry（入口のクラスを広げ、変換ルールを足す）

## 1. 概要

static メソッドを集めた「道具箱」のクラス **`DoubleMatrixUtil`**（行列の和・積・転置・連結・累積和など）を入口に加え、JS から呼べる関数を **72 個 → 165 個**（93 個増）にした。
そのために、変換ルール（引数 `int[]`・`int[][]`、戻り値 `int[]`・`boolean[][]`）を YAML に足し、乱数の関数と「引数を書き換えるだけの関数」を理由つきで外した。
**今までの 72 個の生成物は 1 行も変わっていない**（追加だけ）ことと、検算がすべて通ることを確かめた。

- ブランチ：`step04-utility-entry`（`step03-extra-exports` から作成）
- 日付：2026-11-16
- 対応する資料：「P6P7_対策の検討.pdf」p.5（P6 ③ 変換ルールを増やす）
- 承認：2026-11-16 に計画を承認（a① `DoubleMatrixUtil` だけ、b① YAML にクラス名を書く、c① 公開名は `matrixMultiply` の形、d② ブリッジ名は `MatrixUtilWasm`、e① 乱数は除外）。計画は [`plan.md`](plan.md)

## 2. タスク

- [x] ブランチ `step04-utility-entry` を作る
- [x] YAML：`entry.utility_classes`（道具箱のクラス）、`exclude`（公開しないメソッドと理由）、変換ルール 4 つを足す
- [x] 生成器：道具箱のクラスの public static メソッドを公開する（公開名は「先頭の語 ＋ メソッド名」、ブリッジは `MatrixUtilWasm`）
- [x] 生成器：「引数の配列を書き換えるだけの static void メソッド」を外す手順を足す
- [x] 生成器：設定の確かめ（`utility_classes`・`exclude` の形、`exclude` の書き間違い）を足す
- [x] テンプレート：`Matrices` に `toIntMatrix`・`toDouble(int[])`・`toDouble(boolean[][])` を足す
- [x] 再生成し、今までの 72 個の生成物が変わらないことを確かめる
- [x] `JvmReference.java`：`int[]`・`int[][]` の引数に入れる値を決める
- [x] `wasm-verify.mjs`：数学的な性質の確認を 10 項目足す
- [x] `verify-tools/run.sh` で検算する
- [x] `CLAUDE.md` を更新する

## 3. 実装計画

### 入口の広げ方

```yaml
entry:
  class_suffixes: [...]            # 今までどおり（名前の決まりで選ぶ入口）
  class_prefixes: {...}
  utility_classes:                 # step04：入口にするクラスを直接書く
    "DoubleMatrixUtil": "matrix"   # クラス名 → 公開名の先頭の語
exclude:                           # step04：公開しないメソッド → 理由
  "DoubleMatrixUtil.createUniformRandom": "乱数なので…"
  "DoubleMatrixUtil.createNormalRandom": "乱数なので…"
```

- 道具箱のクラスは、名前の決まり（`…Solver` など）とは別に、`build_bridges()` で入口に加える。public static メソッドだけを対象にし、手順 C・D（型の変換、getter）は今までと同じものを使う。
- 公開名は `DoubleMatrixUtil.multiply` → `matrixMultiply`。ブリッジのクラス名は「先頭の語 ＋ `UtilWasm`」で `MatrixUtilWasm`（手書きの `org.example.MatrixWasm` と取り違えないため。d②）。

### 外すもの

| 種類 | どこに書くか | 理由 |
|---|---|---|
| 乱数（`createUniformRandom`・`createNormalRandom`） | YAML の `exclude`（NFC に合わせた値） | 呼ぶたびに答えが変わり、JVM と Wasm を比べられない |
| 引数の配列を書き換えるだけの static void メソッド（`addSelf`・`setZero`・`setSubMatrix` など） | 生成器の手順（Java 一般の判断） | ブリッジは JS の配列を Java の配列に作り直して渡すので、書き換えた結果が JS に戻らない |

### 公開名の重なりの解決（計画の「`With〇〇` を付ける」決まりの延長）

`DoubleMatrixUtil` には、**引数の名前が同じで型だけが違う**オーバーロードがある（例：`transpose(double[][] matrix)` と `transpose(double[] matrix)`、`divide(matrix, int scalar)` と `divide(matrix, double scalar)`）。今までの「基準にない引数の名前を付ける」決まりでは区別できず、`matrixTransposeWith` のような名前になったり、名前が重なって止まったりした。そこで決まりを次のように足した。

1. 今までどおり、引数の少ないものを基準にし、ほかには「基準にない引数の名前」を付ける。引数の数が同じなら、`int` を含まないもの（double 版）を基準にする。
2. 引数の名前で区別できないものがグループに 1 つでもあれば、そのグループは基準以外のすべてに**引数の型**を付ける（`double[]` → `Vector`、`double[][]` → `Matrix`、`int[][]` → `IntMatrix` など）。

例：`matrixTransposeWithVector`、`matrixDivideWithMatrixInt`、`matrixPowerElementWiseWithMatrixDouble`。今までの 69 個は 1 だけで区別できていたので、名前は変わらない（下の検証で確認）。

### 検算の入力

`JvmReference` は引数の型の並びから入力を決めている。新しい型のために、次を足した。

| 引数 | 入れる値 | 理由 |
|---|---|---|
| `int[]` | `{0, 1}` | 行・列の番号の配列。どの入力の行列（最小で 5x2）にもある番号にする |
| 行列の直後に続く `int`（間に `int[]` があってもよい） | `1` | 行・列の番号（`rowMin` など）や整数の倍率。今までの 50 では範囲外になる |
| `int[][]` | 行列の値を四捨五入したもの | 生成器は `int[][]` を double の行列と同じ形（1 次元配列＋行数＋列数）で受け取るので、型の並びだけでは区別できない。ブリッジが「整数でない」というエラーを返したら、四捨五入した入力でやり直す |

今までの 72 個の関数には「行列の直後に `int`」や `int[]` の引数がないので、入力は変わらない（下の検証で確認）。

## 4. ウォークスルー

```bash
git switch -c step04-utility-entry

# 変更前の生成物と検算データを保存（比較の基準）

# 1. codegen/nfc.yaml：utility_classes、exclude、変換ルール 4 つ
# 2. JavaGenerator.py：道具箱のクラス、exclude、void メソッドの除外、設定の確かめ
.venv/bin/python app/src/main/Python/JavaGenerator.py
#   → 「公開名が重複しています: matrixMultiplyWithMatrixScalar, matrixPowerElementWiseWith, …」で止まった
# 3. resolve_name_collisions() に「名前で区別できなければ型で区別する」を追加 → 162 個
# 4. matrices.java.j2：toIntMatrix・toDouble(int[])・toDouble(boolean[][])
./gradlew :app:build          # 成功。nfc.wasm 373 KB → 428 KB。TeaVM は int[] の引数も受け付けた
# 5. Node から試し呼び：matrixGetSubMatrix(A, [0,2], [1,2]) → [[2,3],[8,9]] など
# 6. JvmReference.java：int[]・int[][] の入力
./gradlew :app:verifyJvm      # 165 関数、495 回（例外 65 回）
# 7. wasm-verify.mjs：性質の確認 10 項目
verify-tools/run.sh
```

途中で直したこと：

- `print`・`writeCsvFormat` などが「引数の配列を書き換えるだけの void」という理由で外れていた。本当の理由は引数の `Writer` なので、void の確認を引数の変換の後に移した（理由が正しく出るように）。
- `exclude` のメソッド名を書き間違えると、何も起きずに無視されていた。どのメソッドにも当たらない `exclude` があれば止まるようにした。
- `getSubMatrix(matrix, int[] rowIndex, columnMin, columnMax)` の `columnMin` に 50 が入って範囲外になっていた。`int[]` の後の `int` も行・列の番号とみなすように直した。

## 5. 変更したファイル

| ファイル | 内容 |
|---|---|
| `codegen/nfc.yaml` | `entry.utility_classes`、`exclude`、変換ルール（引数 `int[]`・`int[][]`、戻り値 `int[]`・`boolean[][]`）を追加（146 行 → 180 行） |
| `app/src/main/Python/JavaGenerator.py` | 道具箱のクラス、`exclude`、void メソッドの除外、公開名の型による区別、設定の確かめを追加（479 行 → 575 行） |
| `app/src/main/Python/templates/matrices.java.j2` | `toIntMatrix`・`toDouble(int[])`・`toDouble(boolean[][])` を追加 |
| `app/src/main/java/org/example/generated/MatrixUtilWasm.java` | 生成物（新規、570 行）。`DoubleMatrixUtil` のブリッジ |
| `app/src/main/java/org/example/generated/NfcWasm.java` | 生成物。93 個の窓口を追加（467 行追加、削除なし） |
| `app/src/main/java/org/example/generated/Matrices.java` | 生成物。3 つの関数を追加（47 行追加、削除なし） |
| `app/src/main/webapp/nfc.generated.js` | 生成物。93 個の関数を追加（472 行追加、削除なし） |
| `app/src/verify/java/verify/JvmReference.java` | `int[]`・`int[][]` の入力、呼び出しのやり直し |
| `verify-tools/wasm-verify.mjs` | `DoubleMatrixUtil` の性質の確認 10 項目 |
| `CLAUDE.md` | 道具箱のクラスと `exclude` の説明 |
| `logs/step04-utility-entry/`、`logs/README.md` | 記録（計画は `next-plan/` から `plan.md` に移動） |

## 6. 検証結果

### 今までの生成物が変わっていないか

変更前の生成物を保存しておき、再生成した後と比べた。

| ファイル | 削除した行 | 追加した行 |
|---|---|---|
| `EigWasm`・`ElfWasm`・`FftWasm`・`LeqWasm`・`SvdWasm` | 0 | 0 |
| `Matrices.java` | 0 | 47 |
| `NfcWasm.java` | 0 | 467 |
| `nfc.generated.js` | 0 | 472 |

`build/codegen_report.md` も、変わったのは見出しの個数（69 → 162、3 → 55）だけで、今までの行はそのまま。

### 検算（`verify-tools/run.sh`、終了コード 0）

| 確認 | step03 | step04 |
|---|---|---|
| 公開関数 | 72 個 | **165 個**（自動生成 162 ＋ 差し込み 3） |
| ① JVM との一致 | 216/216 | **495/495**（すべて完全一致。うち 65 回は両方とも例外） |
| ② 数学的な性質 | 28/28 | **38/38**（下の 10 項目を追加） |
| ③ 手書き版との一致 | 28/28 | 28/28 |
| `nfc.wasm` の大きさ | 373 KB | **428 KB** |

今までの 216 回の呼び出しは、入力（引数）がすべて step03 と同じだった。結果も 215 回は JSON の中身まで同じで、残りの 1 回（`realCholeskyDecompose` の `rect`）は、両方とも例外のまま、例外の種類だけが変わった（下の「分かったこと」2）。

足した性質の確認：

| 確認 | 相対誤差 |
|---|---|
| `matrixMultiply(A, B)` が JS で計算した `A·B` と一致（4x4、5x3 × 3x2） | 0 |
| `(Aᵀ)ᵀ = A`、長方形の転置が JS と一致 | 0 |
| `(A + B) − B = A`、`A + B` が JS と一致 | 4.4e-17 |
| `matrixAppendDown`・`matrixAppendRight` が JS の連結と一致 | 0 |
| `matrixGetSubMatrix(A, [0, 2], [1, 3])`（`int[]` の引数）が JS と一致 | 0 |
| `matrixIndexOfMinimum`（`int[]` の戻り値）が JS と一致 | 0 |
| `matrixAddElementWiseWithIntMatrixDouble`（`int[][]` の引数）が JS と一致 | 0 |
| 整数の行列に整数でない値を渡すとエラーになる | OK |
| `matrixSum`・`matrixTrace` が JS と一致 | 0 |

### 設定の書き間違いのとき（どれも終了コード 1 で止まり、生成物は書き換えなかった）

| わざと作った問題 | 表示 |
|---|---|
| `utility_classes` のクラス名を `DoubleMatrixUtill` にする | `entry.utility_classes のクラス DoubleMatrixUtill が、解析した JSON にない` |
| `exclude` を `"createNormalRandom"`（クラス名なし）にする | `項目 exclude の 'createNormalRandom' は「クラス名.メソッド名」の形で書く` |
| `utility_classes` の値を `1`（文字列でない）にする | `項目 entry.utility_classes の 'DoubleMatrixUtil': 1 は、名前も値も文字列で書く` |
| `exclude` のメソッド名を `createNormalRandam` にする | `exclude に書いたメソッドが、入口クラスに見つからない: ['DoubleMatrixUtil.createNormalRandam']` |

### `DoubleMatrixUtil` の内訳（145 個の public static メソッド）

- 公開した：**93 個**
- 対象外：52 個
  - 引数の型が渡せない（`String`・`Reader`・`Writer`・関数のインタフェース・`NumericalScalar` など）：23 個
  - 引数の配列を書き換えるだけの void メソッド：17 個
  - 戻り値の型が返せない（`Object[]`・`IndexedDoubleElements`）：8 個
  - 乱数（`exclude`）：4 個（`seed` 付きも含む。同じ名前のオーバーロードはすべて外れる）
  - 名前の決まりで選んだ入口クラスの対象外（step03 までの 3 個）は変わらない

## 7. 分かったこと・残った課題

1. **`boolean[][]` の戻り値のルールは、今は使われていない**。`boolean[][]` を返すのは `compareElements`（引数に `String`）と `elementWiseFunction`（引数に関数）だけで、どちらも引数のほうが渡せない。ルールは計画どおり足したが、効いてくるのは `String` のルールを足したとき。
2. **NFC のメッセージの問題が、呼ぶ順番で見え方を変える**。メッセージファイルがないため、`Messages` クラスの初期化に最初に失敗した呼び出しは `ExceptionInInitializerError`、2 回目からは `NoClassDefFoundError` になる。step04 で `matrix…` の関数が名前の順で先に呼ばれるようになり、`realCholeskyDecompose`（`rect`）の例外の種類が変わった。⑦（エラー情報）でまとめて扱う。
3. **想定どおりの例外**：`matrixVariance`（ベクトルが必要）、`matrixTrace`（正方行列が必要）、`matrixReshape`（大きさが合わない）などは、入力の形が合わず例外になる（JVM・Wasm とも）。ただしメッセージは 2 の問題で失われている。
4. **NFC の番号の数え方がそろっていない**：`getSubMatrix` などは行・列の番号を 0 から数えるが、`indexOfMinimum`・`indexOfMaximum` は 1 から数えて返す。ブリッジは NFC の動きをそのまま公開している。JS から使うときは注意が要る。
5. **JS から見ると同じ意味のオーバーロードがある**：`matrixDivide`（double）と `matrixDivideWithMatrixInt`（int）のように、JS では整数と小数を区別しないので、int 版は double 版とほぼ同じになる。今回はすべて公開した。減らすなら「int 版は double 版があれば外す」という手順を足す（次の計画で相談）。
6. **公開名が長いものがある**：`matrixGetSubMatrixWithRowMinRowMaxColumnMinColumnMax` など。決まりで機械的に付けているため。
7. **`nfc.wasm` が 373 KB → 428 KB**（+55 KB）。`DoubleMatrixUtil` は NFC のほかのクラスからも使われているので、増え方は小さかった。

## 保存（コミットと push）

| 日付 | コミット | 内容 | GitHub（origin） | Gitblit（ユーザーが push） |
|---|---|---|---|---|
| 2026-11-16 | この記録を含むコミット | step04 の作業 | push 済み | ユーザーが push |

## 8. 確認用コマンド

すべて `make_wasm/TeaVM` フォルダで実行する。

**① このステップのブランチに切り替える**

```bash
git switch step04-utility-entry
```

期待する結果：`Switched to branch 'step04-utility-entry'`。

**② 生成器を動かす**

```bash
.venv/bin/python app/src/main/Python/JavaGenerator.py
```

期待する結果：最後の 2 行が `公開したメソッド: 162 個 / 対象外: 55 個（理由は build/codegen_report.md）` と `手書きのブリッジから差し込んだ関数: 3 個（multiply, prepareMultiply, multiplyPrepared）`。

**③ 生成物が変わっていないかを見る**（②の直後に実行する）

```bash
git status --short app/src/main/java/org/example/generated app/src/main/webapp/nfc.generated.js
```

期待する結果：**何も表示されない**（このブランチの生成物と同じ）。

**④ 今までのブリッジが step03 から変わっていないことを見る**

```bash
git diff --stat step03-extra-exports -- app/src/main/java/org/example/generated
```

期待する結果：`MatrixUtilWasm.java`（新規）、`Matrices.java`、`NfcWasm.java` の 3 つだけが出る。`EigWasm.java` などは出ない。

**⑤ 対象外にしたものと理由を見る**

```bash
grep DoubleMatrixUtil build/codegen_report.md | grep -v '^| `matrix'
```

期待する結果：`createUniformRandom`・`createNormalRandom`（乱数）、`addSelf`・`setZero` など（引数の配列を書き換えるだけ）、`readCsvFormat`（`Reader`）などが、理由つきで並ぶ。

**⑥ ビルドから検算までまとめて実行する**（目安：数分）

```bash
verify-tools/run.sh
```

期待する結果：最後の 3 行が `JVM との一致: 495 回中 完全一致 495・誤差内 0・不一致 0`、`数学的な性質: 38 項目中 OK 38`、`手書き版との比較: 28 組中 一致 28`。

**⑦ 新しい関数を Node から呼ぶ**（⑥の後に実行する）

```bash
node -e 'require("./app/build/generated/teavm/wasm-gc/nfc.wasm-runtime.js"); TeaVM.wasmGC.load("./app/build/generated/teavm/wasm-gc/nfc.wasm", {nodejs: true}).then(t => console.log(Array.from(t.exports.matrixGetSubMatrix([1,2,3,4,5,6,7,8,9], 3, 3, [0,2], [1,2]))))'
```

期待する結果：`1, 2, 2, 2, 3, 8, 9` の 7 つの数。3x3 の行列 `[[1,2,3],[4,5,6],[7,8,9]]` から、行 0・2 と列 1・2 を取り出した `[[2,3],[8,9]]` が pack 形式で返っている（`int[]` の引数 `[0,2]`・`[1,2]` がそのまま渡せている）。

**⑧ Gitblit に push する**（ユーザーが実行する）

```bash
git push gitblit step04-utility-entry
```

期待する結果：`* [new branch]  step04-utility-entry -> step04-utility-entry`。
