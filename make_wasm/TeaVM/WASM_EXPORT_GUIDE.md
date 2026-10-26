# NFC を Wasm 化するときの「入口」の選び方と書き方

`org/mklab/nfc` の Java コードを TeaVM で Wasm にし、JS から呼べるようにするための手順をまとめる。

---

## 0. 全体の仕組み

```
JS ──▶ NfcWasm（@JSExport の公開窓口）──▶ EigenWasm など（型の変換）──▶ org.mklab.nfc（実際の計算）
```

- **`org/mklab/nfc` のコードは一切変更しない。** `org/example` のクラスから import して使うだけ。
- **TeaVM が JS に公開するのは `mainClass` の `@JSExport` 付き static メソッドだけ。**
  そのため `@JSExport` は `NfcWasm` にまとめて書く。NFC のクラスに付けても意味はない。
- **`…Util` などの内部クラスは選ばなくてよい。** 入口から呼ばれていれば、TeaVM が依存関係をたどって自動的に Wasm に含める。

つまり、やることは「**入口を選ぶ**」と「**JS が扱える型に変換する**」の2つだけ。

---

## 1. `org/example` のファイル構成とその理由

| ファイル | 役割 | 作った理由 |
|---|---|---|
| `NfcWasm.java` | 公開窓口（`mainClass`） | TeaVM は mainClass しか公開しないため、`@JSExport` をここに集める |
| `EigenWasm.java` | `eig` パッケージ用ブリッジ | NFC のパッケージ単位で分けると読みやすい |
| `LeqWasm.java` | `leq` パッケージ用ブリッジ | 同上 |
| `SvdWasm.java` | `svd` パッケージ用ブリッジ | 同上 |
| `FftWasm.java` | `fft` パッケージ用ブリッジ | 同上 |
| `ElfWasm.java` | `elf` パッケージ用ブリッジ | 同上 |
| `Matrices.java` | 配列の変換（共通部品） | どのブリッジでも使う処理を1か所にまとめる |

> ブリッジを作らず全部 `NfcWasm` に書いても動く。分けているのは読みやすさのため。

---

## 2. 入口の選び方

### 手順A：クラス名で振り分ける

| クラス名のパターン | 扱い | 例 |
|---|---|---|
| `…Solver` / `…Decomposer` / `…Analyzer` / `…ExponentialMatrix` | ✅ **入口**（import して呼ぶ） | `DoubleRealEigenSolver` |
| `…Elements` / `…Decomposition` | 📦 **結果の箱**（入口の戻り値として受け取り、getter で中身を取り出す） | `SchurDecompositionDoubleRealElements` |
| `…Util` / `Abstract…` / `Messages` / `package-info` | ❌ **内部用**（import しない。必要なら TeaVM が自動で含める） | `DoubleRealEigenSolverUtil` |
| インタフェース（`interface`） | ❌ 単体では呼べない | `RandomGenerator` |

### 手順B：型で絞り込む

| 種類 | 扱い | 理由 |
|---|---|---|
| `DoubleReal…`（倍精度・実数） | ✅ 最優先 | 引数・戻り値が `double[][]` など。JS と相性がよい |
| `DoubleComplex…`（倍精度・複素数） | ✅ 対象 | 実部と虚部を別々の `double[][]` で渡すので、そのまま扱える |
| `Real…<S, M>` / `Complex…<RS, RM, …>`（ジェネリック版） | ❌ 対象外 | 型パラメータのオブジェクトを JS から渡せない。double で使うなら `Double…` 版と同じ機能になる |

### 手順C：入口クラスの public メソッドを一覧にする

```bash
grep -nE '^\s*public [^=]*\(' eig/DoubleRealSchurDecomposer.java
```

```
34:  public SchurDecompositionDoubleRealElements decompose(final double[][] a) {
```

この1行から次の3つを読み取る。

| 見るところ | この例では | どう書くか |
|---|---|---|
| `static` の有無 | `static` なし | `new DoubleRealSchurDecomposer().decompose(...)` とインスタンス化して呼ぶ（FFT は static なので `DoubleRealFFTAnalyzer.fft(...)` と直接呼ぶ） |
| 引数の型 | `double[][]` | JS からは1次元配列で受け取り、`Matrices.toMatrix` で変換 |
| 戻り値の型 | `…Elements` | JS に渡せないので、手順Dで中身を取り出す |

### 手順D：戻り値が「箱」なら、そのクラスの getter を見る

```bash
grep -nE 'public .*get' eig/SchurDecompositionDoubleRealElements.java
```

```
public final double[][] getU()
public final double[][] getT()
```

### JS に渡せる型・渡せない型

| Java の型 | JS との受け渡し | 対応 |
|---|---|---|
| `int`, `double`, `boolean` | ✅ そのまま | — |
| `double[]` | ✅ そのまま | — |
| `double[][]`, `int[][]` | ❌ | 「1次元配列 + 行数・列数」に変換（`Matrices.toMatrix`／`Matrices.pack`） |
| `…Elements` などの結果クラス | ❌ | getter で配列を取り出して `Matrices.pack` |
| `DoubleMatrix` | ❌ | `new DoubleMatrix(double[][])` で作り、`getDoubleElements()` で戻す |
| インタフェース（関数を渡す設計） | ❌ | JS の関数を呼び出す仕組みが別途必要（→ 4章「難しいもの」） |

---

## 3. 書き方（テンプレート）

### ブリッジ側（例：`EigenWasm.java`）

```java
import org.mklab.nfc.eig.DoubleRealSchurDecomposer;             // 手順Aで選んだ入口
import org.mklab.nfc.eig.SchurDecompositionDoubleRealElements;  // 手順Cで分かった戻り値の型

static double[] schur(double[] values, int size) {
    double[][] a = Matrices.toSquareMatrix(values, size);        // ① 引数の変換
    SchurDecompositionDoubleRealElements result =
            new DoubleRealSchurDecomposer().decompose(a);        // ② new して呼ぶ
    return Matrices.pack(result.getU(), result.getT());          // ③ getter で取り出して配列に
}
```

### 公開窓口（`NfcWasm.java`）

```java
@JSExport
public static double[] schur(double[] values, int size) {
    return EigenWasm.schur(values, size);
}
```

### JS 側（`webapp/nfc.js`）

```javascript
schur(a) {
  const f = square(a);                                  // 2次元配列 → 1次元配列
  const [U, T] = unpack(ex.schur(f.values, f.rows));    // pack 形式 → 2次元配列
  return { U, T };
},
```

### 1機能を追加するときのチェックリスト

1. クラス名で入口を選ぶ（手順A・B）
2. public メソッドの `static` の有無・引数・戻り値を見る（手順C）
3. 戻り値が箱なら getter を見る（手順D）
4. ブリッジに「引数の変換 → 呼ぶ → 配列に詰める」を書く
5. `NfcWasm` に `@JSExport` 付きの委譲メソッドを足す
6. `nfc.js` に2次元配列で呼べるラッパーを足す
7. `./gradlew build` でビルドし、結果を検算する

---

## 4. 対応状況と、まだ Wasm にできるもの

### ✅ 対応済み

| パッケージ | 入口クラス |
|---|---|
| `eig` | `DoubleRealEigenSolver`, `DoubleRealGeneralizedEigenSolver`, `DoubleRealHessenbergDecomposer`, `DoubleRealSchurDecomposer`, `DoubleRealQRDecomposer`, `DoubleRealQZDecomposer`, `DoubleRealBalanceDecomposer` |
| `leq` | `DoubleRealLUDecomposer` |
| `svd` | `DoubleRealSingularValueDecomposer` |
| `fft` | `DoubleRealFFTAnalyzer`, `DoubleComplexFFTAnalyzer` |
| `elf` | `DoubleRealExponentialMatrix` |

### 🟢 すぐできるもの（引数・戻り値が `double[][]` 系で、今と同じ書き方で足せる）

| パッケージ | 入口クラス | メソッド |
|---|---|---|
| `eig` | `DoubleComplexEigenSolver` | `solve(aRe, aIm)` |
| `eig` | `DoubleComplexGeneralizedEigenSolver` | `solve(ar, ai, br, bi)` |
| `eig` | `DoubleComplexHessenbergDecomposer` | `decompose(aRe, aIm)` |
| `eig` | `DoubleComplexSchurDecomposer` | `decompose(aRe, aIm)` |
| `eig` | `DoubleComplexQRDecomposer` | `decompose` / `decomposeWithPermutation` |
| `eig` | `DoubleComplexQZDecomposer` | `qzDecompose(aRe, aIm, bRe, bIm)` |
| `eig` | `DoubleComplexBalanceDecomposer` | `decompose(aRe, aIm)` |
| `leq` | `DoubleComplexLUDecomposer` | `decompose` / `inverse` / `leftDivide` / `getDeterminant` |
| `leq` | `DoubleRealGaussianEliminationSolver` | `inverse`（戻り値が `Object[]` なので取り出し方に注意） |
| `svd` | `DoubleComplexSingularValueDecomposer` | `decompose` / `singularValue` / `rank` / `pseudoInverse` など |
| `elf` | `DoubleComplexExponentialMatrix` | `exp(aRe, aIm)`（static） |

→ これを足せば、**`eig`・`leq`・`svd`・`fft`・`elf` は倍精度版がすべて揃う**。

### 🟡 少し工夫が必要なもの（`DoubleMatrix` などのオブジェクトを使う）

| パッケージ | 入口クラス | 工夫するところ |
|---|---|---|
| `leq` | `DoubleRealCholeskyDecomposer` | 引数・戻り値が `DoubleMatrix` → `new DoubleMatrix(double[][])` と `getDoubleElements()` で変換 |
| `random` | `DoubleUniformRandom` など | 戻り値が `DoubleNumber` → `doubleValue()` で取り出す。乱数生成器の状態を保持する設計にする |
| `scalar` | `DoublePolynomial` など | `new DoublePolynomial(double[] 係数)` で作り、`getRoots()`（多項式の根）などの結果を配列に変換する |
| `matrix` | `DoubleMatrix` の演算 | 行列演算（積・転置など）を配列で受け渡す。JS で書いても済む処理が多い |

### 🔴 難しいもの（解きたい方程式を Java のインタフェースで渡す設計）

| パッケージ | 例 | 難しい理由 |
|---|---|---|
| `ode` | `DoubleRungeKutta4`, `DoubleRungeKuttaFehlberg` | 微分方程式 `dx/dt = f(t, x)` を `DoubleDifferentialSystem` などのインタフェースで渡す。JS の関数を Java から呼び出す仕組み（`@JSFunctor` など）が必要 |
| `dae` | `DoubleDifferentialAlgebraicEquationSolver` | 同上 |
| `nleq` | `NewtonRaphsonRootSolver` | 非線形関数 `NonLinearFunction` を渡す。さらにジェネリック版しかない |

### ⚪ 対象外（計算機能ではない、または JS で済む）

| パッケージ | 理由 |
|---|---|
| `matx` | MATLAB 形式のデータ表現 |
| `rpn` | 式処理（逆ポーランド記法） |
| `util` | 時間計測・丸め・エンディアン変換などの補助 |
| ジェネリック版（`Real…`, `Complex…`） | JS から型パラメータを渡せない。double で使うなら `Double…` 版と同じ |

---

## 5. 注意点

- **エラーメッセージが `Bundle not found` になる。**
  NFC の `Messages` クラスは `messages.properties` を `ResourceBundle` で読むが、このプロジェクトには `.properties` ファイルがない。例外自体は JS に投げられるので計算結果への影響はない。
- **wasm のファイルは1つにまとめるのがおすすめ。**
  wasm を分けると、Java のランタイムや `matrix`・`scalar` などの共通クラスが wasm ごとに重複する。また、wasm 同士で Java のオブジェクトを受け渡せない。パッケージごとに分けたいときは、JS 側で `nfc.eig.…` のように名前空間を分ければよい。
