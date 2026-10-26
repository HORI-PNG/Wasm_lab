# `org.mklab.nfc` Java クラス概要

## 1. 概要

`app/src/main/java/org/mklab/nfc` 以下は、アプリケーション固有の処理というより、数値計算を行うためのライブラリ群です。

主な対象は次のとおりです。

- スカラー値、複素数、多項式
- 実数・複素数行列
- 連立一次方程式、固有値問題、特異値分解
- FFT（高速フーリエ変換）
- 行列指数関数
- 常微分方程式（ODE）、微分代数方程式（DAE）
- 非線形方程式
- 乱数
- 逆ポーランド記法による式処理
- MATLAB 風の値・配列表現

Java ファイルの多くは、次の3種類の実装を共通の設計で提供しています。

- **汎用型**：ジェネリック型で任意のスカラー・行列型に対応
- **倍精度実数型**：`Double...`
- **倍精度複素数型**：`DoubleComplex...`

また、クラス名から次のように役割を判断できます。

| 命名 | 役割 |
|---|---|
| `Abstract...` | 共通処理を実装した継承用の抽象基底クラス |
| `...Util` | 生成、変換、演算などの補助処理 |
| `...Solver` | 方程式や固有値問題などを解くクラス |
| `...Decomposer` | 行列分解を計算するクラス |
| `...Decomposition` | 分解結果を保持・提供するクラス |
| `...Elements` | 分解結果や内部データを構成する要素型 |
| `Double...` | 倍精度実数を対象とする実装 |
| `DoubleComplex...` | 倍精度複素数を対象とする実装 |
| `Messages` | リソースバンドルからメッセージを取得するクラス |
| `package-info.java` | パッケージのJavadoc |

## 2. パッケージ一覧

| パッケージ | 機能 |
|---|---|
| `scalar` | 数値、複素数、記号スカラー、多項式 |
| `matrix` | 行列、配列、行列演算 |
| `matrix.misc` | 特殊構造行列・ベクトルの生成 |
| `matrix.util` | 行列生成の小規模な補助クラス |
| `leq` | 連立一次方程式、LU分解、Cholesky分解 |
| `eig` | 固有値、固有ベクトル、QR/QZ/Schur/Hessenberg分解 |
| `svd` | 特異値分解 |
| `fft` | 高速フーリエ変換 |
| `elf` | 行列指数関数 |
| `ode` | 常微分方程式、差分方程式、数値積分 |
| `dae` | 微分代数方程式 |
| `nleq` | 非線形方程式、Newton法 |
| `random` | 一様乱数、正規乱数 |
| `rpn` | 逆ポーランド記法と式処理 |
| `matx` | MATLAB風の値・配列表現 |
| `util` | 時間計測、丸め、Endian変換など |

## 3. ルートパッケージ

### `org.mklab.nfc`

- `SolverException.java`  
  数値ソルバー共通の検査例外です。
- `Messages.java`  
  `ResourceBundle` を利用して、共通メッセージを外部リソースから取得します。
- `package-info.java`  
  数値計算パッケージ全体の説明です。

## 4. `scalar`：スカラー・数値型・多項式

### 基本数値型

- `DoubleNumber.java`：倍精度実数スカラー
- `DoubleComplexNumber.java`：倍精度複素数スカラー
- `IntNumber.java`：整数スカラー

### APIと抽象基底

- `Scalar.java`：スカラー共通API
- `NumericalScalar.java`：数値演算可能なスカラー
- `RealNumericalScalar.java`：実数スカラー
- `ComplexNumericalScalar.java`：複素数スカラー
- `SymbolicScalar.java`：記号スカラー
- `RealSymbolicScalar.java`、`ComplexSymbolicScalar.java`：実・複素の記号スカラー
- `AbstractScalar.java`：スカラー実装の共通基底
- `AbstractNumericalScalar.java`：数値スカラーの基底
- `AbstractRealNumericalScalar.java`、`AbstractComplexNumericalScalar.java`：実・複素数値スカラーの基底
- `AbstractSymbolicScalar.java`、`AbstractRealSymbolicScalar.java`、`AbstractComplexSymbolicScalar.java`：記号スカラーの基底

### 多項式と有理多項式

- `Polynomial.java`：多項式API
- `RealPolynomial.java`、`ComplexPolynomial.java`：実・複素多項式API
- `RationalPolynomial.java`：有理多項式API
- `RealRationalPolynomial.java`、`ComplexRationalPolynomial.java`：実・複素有理多項式API
- `DoublePolynomial.java`：倍精度実数係数の多項式
- `DoubleComplexPolynomial.java`：倍精度複素係数の多項式
- `DoubleRationalPolynomial.java`：倍精度実数の有理多項式
- `DoubleComplexRationalPolynomial.java`：倍精度複素数の有理多項式
- `AnyRealPolynomial.java`、`AnyComplexPolynomial.java`：任意の実・複素スカラー型を係数にできる多項式
- `AnyRealRationalPolynomial.java`、`AnyComplexRationalPolynomial.java`：任意型に対応する有理多項式
- `AbstractPolynomial.java`、`AbstractRealPolynomial.java`、`AbstractComplexPolynomial.java`：多項式の共通基底
- `AbstractRationalPolynomial.java`、`AbstractRealRationalPolynomial.java`、`AbstractComplexRationalPolynomial.java`：有理多項式の共通基底

### 補助クラス

- `DoubleNumberUtil.java`：倍精度実数の生成・変換・演算補助
- `DoubleComplexNumberUtil.java`：倍精度複素数の補助
- `IntNumberUtil.java`：整数スカラーの補助
- `ComplexNumericalScalarUtil.java`：複素数値スカラーの共通補助
- `ComplexSymbolicScalarUtil.java`：複素記号スカラーの補助
- `RoundableToInteger.java`：整数へ丸められる値を表すAPI
- `FloatingPointOperator.java`：浮動小数点演算を抽象化するAPI

## 5. `matrix`：行列・配列

### 行列API

- `Matrix.java`：行列共通API
- `NumericalMatrix.java`：数値行列API
- `RealNumericalMatrix.java`、`ComplexNumericalMatrix.java`：実数・複素数の数値行列
- `SymbolicMatrix.java`、`RealSymbolicMatrix.java`、`ComplexSymbolicMatrix.java`：記号行列
- `PolynomialMatrix.java`：多項式行列
- `RealPolynomialMatrix.java`、`ComplexPolynomialMatrix.java`：実・複素多項式行列
- `RationalPolynomialMatrix.java`、`RealRationalPolynomialMatrix.java`、`ComplexRationalPolynomialMatrix.java`：有理多項式行列

### 具体的な行列・配列

- `DoubleMatrix.java`：倍精度実数行列
- `DoubleComplexMatrix.java`：倍精度複素行列
- `IntMatrix.java`：整数行列
- `BooleanMatrix.java`：真偽値行列
- `IndexedMatrix.java`：インデックス付き行列
- `DoubleIndexedMatrix.java`：倍精度要素のインデックス付き行列
- `Array.java`：一般配列API
- `FundamentalArray.java`：基本要素を保持する配列
- `FundamentalMatrix.java`：基本要素を保持する行列
- `Grid.java`：表形式データを扱うグリッド

### 抽象基底と共通実装

- `AbstractArray.java`：配列実装の基底
- `AbstractMatrix.java`：行列実装の共通基底
- `AbstractNumericalMatrix.java`：数値行列の基底
- `AbstractNumericalRealMatrix.java`、`AbstractNumericalComplexMatrix.java`：実・複素数値行列の基底
- `AbstractSymbolicMatrix.java`、`AbstractSymbolicRealMatrix.java`、`AbstractSymbolicComplexMatrix.java`：記号行列の基底
- `AbstractFundamentalMatrix.java`：基本行列の基底
- `AbstractGrid.java`：グリッドの基底
- `BaseArray.java`、`BaseMatrix.java`：配列・行列の共通実装

### 行列ユーティリティ

- `DoubleMatrixUtil.java`：倍精度実数行列の生成・変換・演算
- `DoubleComplexMatrixUtil.java`：倍精度複素行列の補助
- `IntMatrixUtil.java`：整数行列の補助
- `BooleanMatrixUtil.java`：真偽値行列の補助
- `PolynomialMatrixUtil.java`：多項式行列の共通補助
- `RationalPolynomialMatrixUtil.java`：有理多項式行列の補助
- `DoublePolynomialMatrixUtil.java`、`DoubleComplexPolynomialMatrixUtil.java`：実・複素多項式行列の補助
- `DoubleRationalPolynomialMatrixUtil.java`、`DoubleComplexRationalPolynomialMatrixUtil.java`：実・複素有理多項式行列の補助
- `BaseMatrixOperator.java`：行列演算を抽象化する共通演算子
- `MatrixElementOperator.java`、`MatrixElementWiseOperator.java`：要素単位の演算
- `GridUtil.java`：グリッド生成・整形・入出力

### その他の型

- `ArrayElement.java`、`ElementHolder.java`：配列要素の抽象化・保持
- `IndexedElements.java`、`IndexedDoubleElements.java`、`IndexedIntElements.java`：インデックス付き要素
- `GridElement.java`：グリッド要素
- `GridElementAlignment.java`：グリッド要素の配置指定
- `GridFormat.java`：グリッド表示形式
- `ScalarFunction.java`：スカラー関数
- `NumericalScalarFunction.java`：数値スカラー関数
- `NumericalScalarFunctionWithTwoArguments.java`：2引数数値関数
- `DoubleRealFunction.java`、`DoubleRealFunctionWithTwoArguments.java`：倍精度実数関数
- `BooleanFunction.java`、`BooleanFunctionWithTwoArguments.java`：真偽値関数
- `MatrixFileType.java`：行列ファイル形式
- `NormType.java`：ノルムの種類
- `MatrixSizeException.java`：行列サイズ不整合の例外

## 6. `matrix.misc`：特殊構造行列

数学的に決まった構造を持つ行列や、数値計算用のベクトルを生成します。

- `DiagonalMatrix.java`：対角行列
- `LowerTriangleMatrix.java`：下三角行列
- `UpperTriangleMatrix.java`：上三角行列
- `HankelMatrix.java`：Hankel行列
- `ToeplitzMatrix.java`：Toeplitz行列
- `HilbertMatrix.java`：Hilbert行列
- `InverseHilbertMatrix.java`：逆Hilbert行列
- `HadamardMatrix.java`：Hadamard行列
- `CompanionMatrix.java`：多項式のcompanion行列
- `VandermondeMatrix.java`：Vandermonde行列
- `GivensMatrix.java`：Givens回転行列
- `RotatedMatrix.java`：回転変換された行列
- `LinearlySpacedVector.java`：等間隔ベクトル
- `LogarithmicallySpacedVector.java`：対数間隔ベクトル

## 7. `leq`：連立一次方程式と行列分解

`leq` は、`Ax=b` のような連立一次方程式を解くためのパッケージです。

- `GaussianEliminationSolver.java`：汎用Gaussian消去法ソルバー
- `NumericalGaussianEliminationSolver.java`：数値型向けGaussian消去法
- `DoubleRealGaussianEliminationSolver.java`：倍精度実数版Gaussian消去法
- `LUDecomposer.java`：汎用LU分解器
- `DoubleRealLUDecomposer.java`：倍精度実数LU分解
- `DoubleComplexLUDecomposer.java`：倍精度複素LU分解
- `LUDecomposition.java`、`DoubleLUDecomposition.java`：LU分解結果
- `CholeskyDecomposer.java`：汎用Cholesky分解器
- `DoubleRealCholeskyDecomposer.java`：倍精度実数Cholesky分解
- `...Elements.java`：各アルゴリズムの内部要素・分解結果を保持する型

## 8. `eig`：固有値問題

### 利用者向けソルバー

- `RealEigenSolver.java`、`DoubleRealEigenSolver.java`：実数行列の固有値問題
- `ComplexEigenSolver.java`、`DoubleComplexEigenSolver.java`：複素行列の固有値問題
- `RealGeneralizedEigenSolver.java`：実数の一般化固有値問題
- `DoubleRealGeneralizedEigenSolver.java`：倍精度実数の一般化固有値問題
- `DoubleComplexGeneralizedEigenSolver.java`：倍精度複素数の一般化固有値問題

### 分解結果

- `EigenSolution.java`、`DoubleEigenSolution.java`：固有値・固有ベクトルの結果
- `HessenbergDecomposition.java`：Hessenberg分解結果
- `QRDecomposition.java`：QR分解結果
- `QZDecomposition...`：一般化固有値問題用のQZ分解結果
- `SchurDecomposition.java`：Schur分解結果
- `BalancedDecomposition.java`：行列バランシング結果
- `...Elements.java`：分解結果を構成する型

### アルゴリズム実装と補助

- `...HessenbergDecomposer.java`：Hessenberg化
- `...QRDecomposer.java`：QR分解
- `...QZDecomposer.java`：QZ分解
- `...SchurDecomposer.java`：Schur分解
- `...BalanceDecomposer.java`：行列バランシング
- `...HouseHolderUtil.java`：Householder変換
- `...EigenSolverUtil.java`、`...GeneralizedEigenSolverUtil.java`：固有値計算の補助

`DoubleReal...` は倍精度実数版、`DoubleComplex...` は倍精度複素数版です。

## 9. `svd`：特異値分解

行列を概念的に `A = U Σ V*` の形へ分解します。

- `SingularValueDecomposition.java`：汎用SVD結果API
- `DoubleSingularValueDecomposition.java`：倍精度実数SVD結果
- `DoubleComplexSingularValueDecomposition.java`：倍精度複素SVD結果
- `RealSingularValueDecomposer.java`：実数SVD分解器
- `ComplexSingularValueDecomposer.java`：複素SVD分解器
- `DoubleRealSingularValueDecomposer.java`：倍精度実数SVD分解器
- `DoubleComplexSingularValueDecomposer.java`：倍精度複素SVD分解器
- `...Elements.java`：SVD結果を構成する要素型
- `ComplexValue.java`：SVD内部で使用する複素値

`DoubleSingularValueDecompostion.java` は、クラス名に `Decompostion` という綴りを持つ別名・互換用のクラスです。

## 10. `fft`：高速フーリエ変換

- `RealFFTAnalyzer.java`：実数系列FFTのAPI
- `ComplexFFTAnalyzer.java`：複素数系列FFTのAPI
- `DoubleRealFFTAnalyzer.java`：倍精度実数FFT
- `DoubleComplexFFTAnalyzer.java`：倍精度複素FFT
- `FFTAnalyzerUtil.java`：FFT共通補助
- `DoubleFFTAnalyzerUtil.java`：倍精度FFT補助

## 11. `elf`：行列指数関数

- `ExponentialMatrix.java`：行列指数関数の共通API
- `DoubleRealExponentialMatrix.java`：倍精度実数行列の `exp(A)`
- `DoubleComplexExponentialMatrix.java`：倍精度複素行列の `exp(A)`

線形微分方程式や状態空間モデルなどで利用されます。

## 12. `ode`：常微分方程式・差分方程式

### 方程式とシステムのモデル

- `DifferentialEquation.java`：微分方程式共通API
- `ExplicitDifferentialEquation.java`：陽的微分方程式
- `ImplicitDifferentialEquation.java`：陰的微分方程式
- `DifferentialSystem.java`：微分方程式系
- `ExplicitDifferentialSystem.java`：陽的微分方程式系
- `ImplicitDifferentialSystem.java`：陰的微分方程式系
- `DifferenceEquation.java`、`DifferenceSystem.java`：差分方程式・差分方程式系
- `DifferentialDifferenceEquation.java`、`DifferentialDifferenceSystem.java`：微分差分方程式
- `PiecewiseDifferentialEquation.java`、`PiecewiseDifferentialSystem.java`：区分的微分方程式
- `ContinuousAlgebraicSystem.java`：連続代数系
- `DiscreteAlgebraicSystem.java`：離散代数系
- `ContinuousDiscreteAlgebraicSystem.java`：連続・離散混在系
- `TriallyEvaluable.java`：試行評価可能なモデルのAPI

### 数値解法

- `ImprovedEuler.java`：改良Euler法
- `ModifiedEuler.java`：修正Euler法
- `RungeKutta4.java`：4次Runge–Kutta法
- `RungeKutta4Auto.java`：自動刻み幅のRK4
- `RungeKuttaFehlberg.java`：誤差推定付きRunge–Kutta–Fehlberg法
- `Sampling.java`：解のサンプリング
- `PiecewiseUtil.java`：区分的モデルの共通補助

### ソルバー基底と例外

- `EquationSolver.java`：方程式ソルバー共通API
- `EquationAutoSolver.java`：自動刻み幅ソルバーAPI
- `DifferentialEquationSolver.java`：微分方程式ソルバーAPI
- `DifferentialEquationAutoSolver.java`：自動制御微分方程式ソルバー
- `DifferenceEquationSolver.java`：差分方程式ソルバーAPI
- `ImplicitDifferentialEquationSolver.java`：陰的微分方程式ソルバー
- `AlgebraicEquationSolver.java`：代数方程式ソルバー
- `EquationSolverObserver.java`：ソルバーの進行状況を監視するAPI
- `SolverInterruptedException.java`：ソルバー中断例外
- `SolverStopException.java`：ソルバー停止例外
- `SolverTooManyDataException.java`：出力データ過多例外

`DoubleImprovedEuler`、`DoubleModifiedEuler`、`DoubleRungeKutta4`、`DoubleRungeKutta4Auto`、`DoubleRungeKuttaFehlberg`、`DoubleSampling` などは、上記の倍精度実数専用版です。

## 13. `dae`：微分代数方程式

微分方程式と代数的な拘束条件を同時に扱います。

- `DifferentialAlgebraicEquationSolver.java`：DAEソルバーAPI
- `DifferentialAlgebraicSystem.java`：DAEシステムのAPI
- `TimeVaryingImplicitDifferentialAlgebraicEquation.java`：時変・陰的DAE
- `JacobianMatrix.java`：DAEのJacobian行列API
- `BandMatrixCoefficient.java`：帯行列係数

### `dae.doublePrecision`

- `DoubleDifferentialAlgebraicEquation.java`：倍精度DAE
- `DoubleDifferentialAlgebraicEquationSolver.java`：倍精度DAEソルバー
- `DoubleDifferentialAlgebraicSystem.java`：倍精度DAEシステム
- `DoubleJacobianMatrix.java`：倍精度Jacobian
- `DoubleTimeVaryingLinearlyImplicitDifferentialAlgebraicEquation.java`：倍精度・時変・線形陰的DAE

## 14. `nleq`：非線形方程式

- `NonLinearFunction.java`：非線形関数API
- `NonLinearEquationSolver.java`：非線形方程式ソルバーAPI
- `NewtonRaphsonSolver.java`：Newton–Raphson法の共通実装
- `NewtonRaphsonRootSolver.java`：1変数の根を求めるソルバー
- `NewtonRaphsonFixedPointSolver.java`：固定点形式のNewton法
- `JacobianSolver.java`：Jacobianを使う非線形連立方程式ソルバー
- `NotConvergedException.java`：反復計算が収束しない場合の例外

## 15. `random`：乱数

- `RandomGenerator.java`：乱数生成器の共通API
- `DoubleUniformRandom.java`：倍精度実数の一様乱数
- `ComplexUniformRandom.java`：汎用複素数の一様乱数
- `DoubleComplexUniformRandom.java`：倍精度複素数の一様乱数
- `NormalRandom.java`：正規乱数の共通API
- `DoubleComplexNormalRandom.java`：倍精度複素正規乱数

## 16. `rpn`：逆ポーランド記法・式処理

- `ReversePolishNotationProcessor.java`：RPN処理のAPI
- `ReversePolishNotationExpressionProcessor.java`：RPN式の処理器
- `ReversePolishNotationSymbol.java`：RPNの記号
- `ReversePolishNotationOperand.java`：RPNのオペランド
- `ReversePolishNotationOperator.java`：RPN演算子のAPI
- `AddOperator.java`：加算演算子
- `MultiplyOperator.java`：乗算演算子
- `InverseOperator.java`：逆元演算子
- `SingleOperandOperator.java`：単項演算子の基底
- `DoubleOperandOperator.java`：二項演算子の基底
- `ExpressionProcessor.java`：一般的な式処理
- `TeXExpressionProcessor.java`：TeX形式の式処理
- `WithoutCancellationExpressionProcessor.java`：式変形時に約分を行わない処理
- `AbstractProcessor.java`、`AbstractExpressionProcessor.java`、`AbstractOperator.java`：共通基底

## 17. `matx`：MATLAB風の値・配列

MATLABのように、実数、複素数、文字列、論理値、行列などを統一的なオブジェクトとして扱うための層です。

- `MatxObject.java`：Matxオブジェクト共通API
- `MatxAbstractObject.java`：共通抽象基底
- `MatxArray.java`：配列共通API
- `MatxArrayCreater.java`：配列生成器
- `MatxMatrix.java`：行列ラッパー
- `MatxList.java`：Matx値のリスト
- `MatxRealArray.java`：実数配列
- `MatxComplexArray.java`：複素数配列
- `MatxIntegerArray.java`：整数配列
- `MatxPolynomialArray.java`：多項式配列
- `MatxRationalPolynomialArray.java`：有理多項式配列
- `MatxComplexPolynomialArray.java`：複素多項式配列
- `MatxComplexRationalPolynomialArray.java`：複素有理多項式配列
- `MatxDouble.java`、`MatxComplex.java`、`MatxInteger.java`：基本数値オブジェクト
- `MatxBoolean.java`：論理値オブジェクト
- `MatxString.java`：文字列オブジェクト
- `MxDataHead.java`：Matxデータのヘッダ情報

## 18. `util`：共通ユーティリティ

- `ElapsedTimer.java`：経過時間の計測
- `Pause.java`：実行の一時停止
- `EndianTransformer.java`：Endian変換
- `PolynomialTokenizer.java`：多項式文字列のトークン化
- `RoundMode.java`：丸め方式の定義
- `RoundModeManager.java`：丸め方式の管理
- `RoundModeSelector.java`：丸め方式の選択

## 19. `matrix.util`

小規模な行列・ベクトル生成補助です。

- `Matrix4.java`：4×4行列関連の補助
- `Makerowv.java`：行ベクトル生成の補助
- `Makecolv.java`：列ベクトル生成の補助

## 20. 処理の依存関係

概念的には、次のような層構造になっています。

```text
scalar
  ↓
matrix
  ↓
leq / eig / svd / fft / elf
  ↓
nleq / ode / dae
  ↓
matx / rpn / util
```

- `scalar` が数値要素を提供します。
- `matrix` が、それらを格納・演算する行列を提供します。
- `leq`、`eig`、`svd` などが行列アルゴリズムを実装します。
- `ode`、`dae`、`nleq` は、行列演算や線形方程式ソルバーを利用して方程式を解きます。
- `matx` と `rpn` は、値の統一的な扱いや式処理を担当します。

## 21. まず読むとよいファイル

ライブラリの構造を理解する場合は、次の順番で読むと把握しやすくなります。

1. `scalar/DoubleNumber.java`
2. `scalar/DoubleComplexNumber.java`
3. `matrix/DoubleMatrix.java`
4. `matrix/DoubleComplexMatrix.java`
5. `leq/DoubleRealLUDecomposer.java`
6. `eig/DoubleRealEigenSolver.java`
7. `svd/DoubleRealSingularValueDecomposer.java`
8. `ode/DoubleRungeKutta4.java`
9. `dae/doublePrecision/DoubleDifferentialAlgebraicEquationSolver.java`

低レベルのアルゴリズム詳細より先に、インターフェース、抽象基底、倍精度実装の関係を見ると、同じ設計パターンを他のパッケージにも適用して読めます。

