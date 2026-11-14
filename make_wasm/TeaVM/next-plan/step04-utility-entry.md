# 計画：step04-utility-entry（入口のクラスを広げ、変換ルールを足す）

- 状態：**確認待ち**（承認されるまで実行しない）
- 対応する資料：「P6P7_対策の検討.pdf」p.5（P6 ③ 変換ルールを増やす）。P7（汎用化）の入口の選び方にもつながる
- ブランチ：`step04-utility-entry`（`step03-extra-exports` から作る）

## 0. 計画を書く前に調べたこと

step03 で分かったとおり、今の入口クラス（名前で選んだ 24 個）では、変換ルールを足しても関数はほぼ増えない。そこで、**static メソッドを集めた道具箱のクラス**（`…Util`）を入口に加えた場合を調べた（2026-11-14、`build/nfc_api.json` から）。

| クラス | public static メソッド | 今のルールで公開できる | `int[]` などのルールを足すと |
|---|---|---|---|
| `DoubleMatrixUtil`（行列の基本演算） | 145 | **92** | **114** |
| `DoubleNumberUtil`（実数の関数） | 21 | 13 | 13 |
| `DoubleComplexMatrixUtil`（複素行列） | 39 | 11 | 11 |
| `DoubleComplexNumberUtil`（複素数の関数） | 6 | 3 | 6（`DoubleComplexNumber` のルールが必要） |

`DoubleMatrixUtil` で外れている主な理由：引数 `int[]`（14 個）、`int[][]`（9 個）、`String`（9 個）、戻り値 `Object[]`（6 個）、`boolean[][]`（5 個）、ファイルの読み書き（`Reader`・`Writer`）など。

**そのまま入れると困るもの**も見つかった：

| 種類 | 例 | 困ること |
|---|---|---|
| 乱数 | `createUniformRandom`・`createNormalRandom` | 呼ぶたびに答えが変わるので、JVM と Wasm の答えが一致しない（検算が「不一致」になる） |
| 引数を書き換えるだけで、何も返さない関数 | `addSelf`・`multiplySelf`・`setZero`・`exchangeRow`・`setSubMatrix`・`copy` など（static で戻り値が `void`、引数に配列） | ブリッジは JS の配列を Java の配列に作り直して渡すので、書き換えた結果は JS に戻らない。呼んでも JS からは何も起きない |
| 名前の重なり | 生成されるブリッジのクラス名が `MatrixWasm`（パッケージ `matrix` から作る）になる | 手書きの `org.example.MatrixWasm` と名前が同じになる（パッケージが違うので動くが、紛らわしい） |

## 1. 目的

- 入口のクラスに `DoubleMatrixUtil`（行列の基本演算：和・差・積・転置・連結・累積和など）を加え、JS から呼べる関数を増やす。
- それに必要な変換ルール（`int[]`・`int[][]` など）を YAML に足す。資料の ③ にあたる。
- 乱数や「引数を書き換えるだけの関数」のように、公開しても意味のないものは、きちんと外す。

## 2. やること

### 2-1. 入口のクラスを YAML で追加できるようにする

```yaml
entry:
  class_suffixes: [...]          # 今までどおり（名前で選ぶ入口）
  class_prefixes: {...}
  # 名前の決まりとは別に、入口にするクラスを直接書く（static メソッドだけを公開する）
  utility_classes:
    "DoubleMatrixUtil": "matrix"   # クラス名 → 公開名の先頭（例：multiply → matrixMultiply）
```

- 生成器は、`utility_classes` に書いたクラスの **public static メソッド**を、今までと同じ手順 C・D（型の変換、getter）で公開する。
- 公開名は「先頭の語 ＋ メソッド名」（例：`DoubleMatrixUtil.multiply` → `matrixMultiply`）。オーバーロード（同じ名前で引数が違うもの）は、今までと同じ「`With〇〇` を付ける」決まりで区別する。

### 2-2. 変換ルールを足す（YAML の `types`）

| 種類 | 型 | JS での形 | 変換 |
|---|---|---|---|
| 引数 | `int[]` | 数の配列 | そのまま |
| 引数 | `int[][]` | 2 次元配列 | 1 次元配列＋行数＋列数で受け取り、`int[][]` に組み立て直す（`Matrices` に関数を足す） |
| 戻り値 | `int[]` | ベクトル | 1×n の行列として pack |
| 戻り値 | `boolean[][]` | 2 次元配列（1 と 0） | double の行列にして pack（`Matrices` に関数を足す） |

### 2-3. 意味のないものを外す

- **乱数**：YAML に除外の一覧（`exclude`）を作り、`DoubleMatrixUtil.createUniformRandom`・`createNormalRandom` を書く（理由をコメントで残す）。
- **引数を書き換えるだけの関数**：生成器の手順に「static で戻り値が `void`、かつ引数に配列があるメソッドは対象外（書き換えの結果が JS に戻らないため）」を足す。これは NFC に限らない Java の一般的な判断なので、コード（手順）に書く。
  - 引数に配列がない static void（例：今ある `realSetMaxIteration(int)`）は、設定を変える意味があるので、今までどおり公開する。
- 外したものは、理由とあわせて `build/codegen_report.md` の「対象外」に出る。

### 2-4. 検算を広げる

- `JvmReference.java`：新しい引数の型（`int[]`・`int[][]`）に入れる値を決める（例：`int[]` は `{0, 1}` のような小さな添字の配列、`int[][]` は小さな整数の行列）。今は想定外の型で止まるので、これがないと動かない。
- 増えた関数も、JvmReference が自動で見つけ、JVM との一致を比べる。行列の大きさが合わずに例外になる関数もあるが、JVM と Wasm で同じなら「一致」になる。
- `wasm-verify.mjs` の数学的な性質に、代表的なものを足す（例：`matrixMultiply` が JS で計算した積と一致、`matrixTranspose` を 2 回すると元に戻る、`matrixAdd` と `matrixSubtract` が逆の関係）。

### 2-5. 確認と記録

1. 再生成し、**今までの 72 個の関数の生成物が変わらない**ことを確かめる（変わるのは、`NfcWasm.java` への追加、新しいブリッジのファイル、`Matrices.java` への関数の追加、`nfc.generated.js` への追加だけ）。
2. `verify-tools/run.sh` で検算する。`nfc.wasm` の大きさの変化も記録する（今は 373 KB）。
3. `logs/step04-utility-entry/Artifact.md` を書き、この計画を `plan.md` として移す。`CLAUDE.md` を更新する。
4. ブランチ `step04-utility-entry` でコミットし、GitHub に push する。

## 3. やらないこと

- `DoubleNumber`・`DoubleComplexNumber` のルール（`DoubleMatrixUtil` では効果がない。`DoubleNumberUtil` などを入口にするときに一緒に） → 決めてほしいこと a
- `String`・`Reader`・`Writer` などのファイルの読み書きの関数（ブラウザでは意味がない）
- 戻り値が `Object[]` の関数（中身の型が分からない）
- 名前ではなく型で入口を選ぶ仕組み → P7

## 4. 確認方法（「できた」の基準）

- [ ] `DoubleMatrixUtil` の関数が、JS から（`nfc.matrixMultiply(…)` などで）呼べる
- [ ] 今までの 72 個の関数の生成物が変わっていない
- [ ] 乱数と「引数を書き換えるだけの関数」が、理由つきで対象外になっている
- [ ] `verify-tools/run.sh` がすべて OK（新しく足す性質の確認も含む）

## 5. 決めてほしいこと

| # | 内容 | 選択肢 | おすすめ |
|---|---|---|---|
| a | 入口に加えるクラス | ① `DoubleMatrixUtil` だけ ② `DoubleMatrixUtil`・`DoubleNumberUtil`・`DoubleComplexMatrixUtil`・`DoubleComplexNumberUtil` の 4 つ | **①**：増える関数の大部分（114 個中）がここにある。他の 3 つは増える数が少なく、`DoubleNumber[]` などの別のルールも必要になる。まず 1 つで仕組みを作って確かめる |
| b | 入口の指定方法 | ① YAML にクラス名を直接書く（`utility_classes`） ② 「名前が `Util` で終わる」という決まりで選ぶ | **①**：NFC の `…Util` には、内部用の補助クラス（例：`DoubleRealEigenSolverUtil`）も多く、手順書（`WASM_EXPORT_GUIDE.md`）でも「選ばない」としている。決まりで選ぶと、それらまで入ってしまう |
| c | 公開名の付け方 | ① 先頭の語 ＋ メソッド名（`matrixMultiply`） ② メソッド名だけ（`multiply`） | **①**：② だと、step03 で差し込んだ計測用の `multiply` と名前が重なる。他のクラスを足したときにも重なりにくい |
| d | 生成されるブリッジのクラス名 | ① 今の決まりのまま（`org.example.generated.MatrixWasm`。手書きの `org.example.MatrixWasm` と同名だがパッケージは別） ② 道具箱のクラス用に別の名前にする（例：`MatrixUtilWasm`） | **②**：同じ名前のクラスが 2 つあると、読むときに取り違えやすい |
| e | 乱数の関数 | ① 除外する ② 公開し、検算では「両方とも同じ大きさの行列が返ればよい」と特別に比べる | **①**：今の目的（関数を増やし、正しさを確かめる）では、検算の仕組みを複雑にしてまで必要ではない |
