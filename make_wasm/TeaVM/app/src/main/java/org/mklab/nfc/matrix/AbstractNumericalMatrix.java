/**
 * $Id: NumericalMatrix.java,v 1.88 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

import org.mklab.nfc.eig.BalancedDecomposer;
import org.mklab.nfc.eig.BalancedDecomposition;
import org.mklab.nfc.eig.BalancedDecompositionElements;
import org.mklab.nfc.eig.HessenbergDecomposition;
import org.mklab.nfc.eig.HessenbergDecompositionElements;
import org.mklab.nfc.eig.QRDecomposition;
import org.mklab.nfc.eig.QRDecompositionElements;
import org.mklab.nfc.eig.RealHessenbergDecomposer;
import org.mklab.nfc.eig.RealQRDecomposer;
import org.mklab.nfc.eig.RealSchurDecomposer;
import org.mklab.nfc.eig.SchurDecomposition;
import org.mklab.nfc.eig.SchurDecompositionElements;
import org.mklab.nfc.elf.ExponentialMatrix;
import org.mklab.nfc.leq.CholeskyDecomposer;
import org.mklab.nfc.leq.LUDecomposer;
import org.mklab.nfc.leq.LUDecomposition;
import org.mklab.nfc.leq.LUDecompostionElements;
import org.mklab.nfc.leq.NumericalGaussianEliminationElements;
import org.mklab.nfc.leq.NumericalGaussianEliminationSolver;
import org.mklab.nfc.scalar.NumericalScalar;
import org.mklab.nfc.svd.RealSingularValueDecomposer;
import org.mklab.nfc.svd.SingularValueDecomposition;
import org.mklab.nfc.svd.SingularValueDecompositionElements;


/**
 * {@link NumericalScalar}を成分とする行列を表わすクラスです。
 * 
 * @author koga
 * @version $Revision: 1.88 $
 * @param <M> 行列の型
 * @param <S> スカラーの型
 */
public abstract class AbstractNumericalMatrix<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> extends BaseMatrix<S,M> implements NumericalMatrix<S,M> {

  /** シリアル番号。 */
  private static final long serialVersionUID = -1276042345835008737L;

  /** 成分の出力フォーマット 。*/
  private static String defaultElementFormat = "%16.8E"; //$NON-NLS-1$

  /**
   * 成分のデフォルト出力フォーマットを設定します。
   * 
   * @param format 成分のデフォルト出力フォーマット
   */
  public static void setDefaultElementFormat(final String format) {
    AbstractNumericalMatrix.defaultElementFormat = format;
  }

  /**
   * 成分のデフォルト出力フォーマットを返します。
   * 
   * @return 成分のデフォルト出力フォーマット
   */
  public static String getDefaultElementFormat() {
    return AbstractNumericalMatrix.defaultElementFormat;
  }

  /**
   * 新しく生成された<code>BaseNumericalMatrix</code>オブジェクトを初期化します。
   * @param elements 成分
   */
  public AbstractNumericalMatrix(final S[] elements) {
    super(elements);
    setElementFormat(defaultElementFormat);
    setElementAlignment(GridElementAlignment.LEFT);
  }

  /**
   * elementsで与えられた成分を持つ数値行列を生成します。
   * 
   * @param elements 成分
   */
  public AbstractNumericalMatrix(final S[][] elements) {
    super(elements);
    setElementFormat(defaultElementFormat);
    setElementAlignment(GridElementAlignment.LEFT);
  }

  /**
   * elementsで与えられた成分をもつrowSize*columnSizeの数値行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 成分
   */
  public AbstractNumericalMatrix(final int rowSize, final int columnSize, final S[][] elements) {
    super(rowSize, columnSize, elements);
    setElementFormat(defaultElementFormat);
    setElementAlignment(GridElementAlignment.LEFT);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public BaseNumericalComplexMatrix<S,M> createComplex(final M realPart, final M imagPart) {
//    BaseComplexNumericalScalar<S,M>[][] ans = BaseNumericalMatrixUtil.createComplexArray(realPart.getElements(), imagPart.getElements());
//    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
//  }

  /**
   * {@inheritDoc}
   */
  public M createUniformRandom() {
    return createUniformRandom(getRowSize(), getColumnSize());
  }

  /**
   * {@inheritDoc}
   */
  public M createUniformRandom(final int rowNumber, final int columnNumber, final Grid block) {
    return createUniformRandom(block.getRowSize() * rowNumber, block.getColumnSize() * columnNumber);
  }

  /**
   * {@inheritDoc}
   */
  public M createUniformRandom(final int rowSize, final int columnSize) {
    S[][] ans = AbstractNumericalMatrixUtil.createUniformRandom(getElements(), rowSize, columnSize);
    return ans[0][0].createGrid(rowSize, columnSize, ans);
  }

  /**
   * {@inheritDoc}
   */
  public M createUniformRandom(final int rowSize, final int columnSize, final long seed) {
    S[][] ans = AbstractNumericalMatrixUtil.createUniformRandom(getElements(), rowSize, columnSize, seed);
    return ans[0][0].createGrid(rowSize, columnSize, ans);
  }

  /**
   * {@inheritDoc}
   */
  public M createNormalRandom() {
    return createNormalRandom(getRowSize(), getColumnSize());
  }

  /**
   * {@inheritDoc}
   */
  public M createNormalRandom(final int rowNumber, final int columnNumber, final Grid block) {
    return createNormalRandom(block.getRowSize() * rowNumber, block.getColumnSize() * columnNumber);
  }

  /**
   * {@inheritDoc}
   */
  public M createNormalRandom(final int rowSize, final int columnSize) {
    final S[][] ans = AbstractNumericalMatrixUtil.createNormalRandom(getElements(), rowSize, columnSize);
    return ans[0][0].createGrid(rowSize, columnSize, ans);
  }

  /**
   * {@inheritDoc}
   */
  public M createNormalRandom(final int rowSize, final int columnSize, final long seed) {
    final S[][] ans = AbstractNumericalMatrixUtil.createNormalRandom(getElements(), rowSize, columnSize, seed);
    return ans[0][0].createGrid(rowSize, columnSize, ans);
  }

  /**
   * {@inheritDoc}
   */
  public M medianColumnWise() {
    final S[][] ans = AbstractNumericalMatrixUtil.medianColumnWise(getElements());
    final int localRowSize = ans.length;
    final int localColumnSize = localRowSize == 0 || ans[0] == null ? 0 : ans[0].length;
    return ans[0][0].createGrid(localRowSize, localColumnSize, ans);
  }

  /**
   * {@inheritDoc}
   */
  public M medianRowWise() {
    final S[][] ans = AbstractNumericalMatrixUtil.medianRowWise(getElements());
    final int localRowSize = ans.length;
    final int localColumnSize = localRowSize == 0 || ans[0] == null ? 0 : ans[0].length;
    return ans[0][0].createGrid(localRowSize, localColumnSize, ans);
  }

  /**
   * {@inheritDoc}
   */
  public S median() {
    return AbstractNumericalMatrixUtil.median(getElements());
  }

  /**
   * {@inheritDoc}
   */
  public IndexedMatrix<S,M> sortRowWise() {
    final IndexedElements<S,M> ret = AbstractNumericalMatrixUtil.sortRowWiseWithIndex(getElements());
    final S[][] ans = ret.getElements();
    final int[][] index = ret.getIndices();
    return new IndexedMatrix<>(ans[0][0].createGrid(getRowSize(), Math.max(1, getColumnSize()), ans), new IntMatrix(index));
  }

  /**
   * {@inheritDoc}
   */
  public IndexedMatrix<S,M> sortColumnWise() {
    final IndexedElements<S,M> ret = AbstractNumericalMatrixUtil.sortColumnWiseWithIndex(getElements());
    final S[][] ans = ret.getElements();
    final int[][] index = ret.getIndices();
    return new IndexedMatrix<>(ans[0][0].createGrid(Math.max(1, getRowSize()), getColumnSize(), ans), new IntMatrix(index));
  }

  /**
   * {@inheritDoc}
   */
  public M stdRowWise() {
    final S[][] ans = AbstractNumericalMatrixUtil.stdRowWise(getElements());
    return ans[0][0].createGrid(getRowSize(), Math.min(1, getColumnSize()), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M stdColumnWise() {
    final S[][] ans = AbstractNumericalMatrixUtil.stdColumnWise(getElements());
    return ans[0][0].createGrid(Math.min(1, getRowSize()), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public S std() {
    return AbstractNumericalMatrixUtil.std(getElements());
  }

  /**
   * {@inheritDoc}
   */
  public M absElementWise() {
    final S[][] ans = AbstractNumericalMatrixUtil.absElementWise(getElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public M argumentElementWise() {
//    final S[][] ans = BaseNumericalMatrixUtil.argumentElementWise(getElements());
//    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
//  }

  /**
   * {@inheritDoc}
   */
  public S frobNorm() {
    return AbstractNumericalMatrixUtil.frobNorm(getElements());
  }

  /**
   * {@inheritDoc}
   */
  public M frobNormRowWise() {
    final S[][] ans = AbstractNumericalMatrixUtil.frobNormRowWise(getElements());
    return ans[0][0].createGrid(getRowSize(), Math.min(1, getColumnSize()), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M frobNormColumnWise() {
    final S[][] ans = AbstractNumericalMatrixUtil.frobNormColumnWise(getElements());
    return ans[0][0].createGrid(Math.min(1, getRowSize()), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public S infNorm() {
    return AbstractNumericalMatrixUtil.infNorm(getElements());
  }

  /**
   * {@inheritDoc}
   */
  public S max() {
    return AbstractNumericalMatrixUtil.max(getElements());
  }

  /**
   * {@inheritDoc}
   */
  public S min() {
    return AbstractNumericalMatrixUtil.min(getElements());
  }

  /**
   * 自身と<code>opponent</code>を成分毎に比較し、両者の成分毎の最大値を成分にもつ行列を生成します。
   * 
   * <p>自身が複素行列のときは、絶対値で比較されます。
   * 
   * @param opponent 比較する行列
   * @return 大きいほうの成分をもつ行列
   */
  public M maxElementWise(final M opponent) {
    if (isSameSize(opponent) == false) {
      throw new MatrixSizeException(this, opponent, MatrixSizeException.NOT_SAME_SIZE);
    }

    final S[][] ans = AbstractNumericalMatrixUtil.maxElementWise(getElements(), opponent.getElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * 自身と<code>opponent</code>を成分毎に比較し、両者の成分毎の最小値を成分に持つ行列を生成します。
   * 
   * <p>自身が複素行列のときは、絶対値で比較されます。
   * 
   * @param opponent 比較する行列
   * @return 小さい方の値を成分とする行列
   */
  public M minElementWise(final M opponent) {
    if (this.isSameSize(opponent) == false) {
      throw new MatrixSizeException(this, opponent, MatrixSizeException.NOT_SAME_SIZE);
    }

    final S[][] ans = AbstractNumericalMatrixUtil.minElementWise(getElements(), opponent.getElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M powerElementWise(final double scalar) {
    final S[][] ans = AbstractNumericalMatrixUtil.powerElementWise(getElements(), scalar);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M powerElementWise(final S scalar) {
    final S[][] ans = AbstractNumericalMatrixUtil.powerElementWise(getElements(), scalar);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * 成分毎に累乗します。
   * 
   * @param value 累乗の指数を成分とする行列
   * @return 累乗の結果
   */
  public M powerElementWise(final DoubleMatrix value) {
    if (!this.isSameSize(value)) {
      throw new MatrixSizeException(Messages.getString("NumericalMatrix.1")); //$NON-NLS-1$
    }

    final S[][] ans = AbstractNumericalMatrixUtil.powerElementWise(getElements(), value.getDoubleElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * 成分毎に累乗します。
   * 
   * @param value 累乗の指数を成分とする行列
   * @return 累乗の結果
   */
  public M powerElementWise(final M value) {
    if (!this.isSameSize(value)) {
      throw new MatrixSizeException(Messages.getString("NumericalMatrix.2")); //$NON-NLS-1$
    }

    final S[][] ans = AbstractNumericalMatrixUtil.powerElementWise(getElements(), value.getElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M maxRowWise() {
    final S[][] ans = AbstractNumericalMatrixUtil.maxRowWise(getElements());
    return ans[0][0].createGrid(getRowSize(), Math.min(1, getColumnSize()), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M maxColumnWise() {
    final S[][] ans = AbstractNumericalMatrixUtil.maxColumnWise(getElements());
    return ans[0][0].createGrid(Math.min(1, getRowSize()), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M minRowWise() {
    final S[][] ans = AbstractNumericalMatrixUtil.minRowWise(getElements());
    return ans[0][0].createGrid(getRowSize(), Math.min(1, getColumnSize()), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M minColumnWise() {
    final S[][] ans = AbstractNumericalMatrixUtil.minColumnWise(getElements());
    return ans[0][0].createGrid(Math.min(1, getRowSize()), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public ElementHolder<S> maximum() {
    final Object[] ans = AbstractNumericalMatrixUtil.maximum(getElements());
    return new ElementHolder<>((S)ans[0], ((Integer)ans[1]).intValue(), ((Integer)ans[2]).intValue());
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings({"unchecked", "unused"})
  public IndexedMatrix<S,M> maximumRowWise() {
    final Object[] ret = AbstractNumericalMatrixUtil.maximumRowWise(getElements());
    final S[][] ans = (S[][])ret[0];
    final int[] index = (int[])ret[1];
    return new IndexedMatrix<S,M>(ans[0][0].createGrid(getRowSize(), 1, ans), new IntMatrix(index));
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public IndexedMatrix<S,M> maximumColumnWise() {
    final Object[] ret = AbstractNumericalMatrixUtil.maximumColumnWise(getElements());
    final S[][] ans = (S[][])ret[0];
    final int[] index = (int[])ret[1];
    return new IndexedMatrix<>(ans[0][0].createGrid(1, getColumnSize(), ans), new IntMatrix(index));
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public ElementHolder<S> minimum() {
    final Object[] ans = AbstractNumericalMatrixUtil.minimum(getElements());
    return new ElementHolder<>((S)ans[0], ((Integer)ans[1]).intValue(), ((Integer)ans[2]).intValue());
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public IndexedMatrix<S,M> minimumRowWise() {
    final Object[] ret = AbstractNumericalMatrixUtil.minimumRowWise(getElements());
    final S[][] ans = (S[][])ret[0];
    final int[] index = (int[])ret[1];
    return new IndexedMatrix<>(ans[0][0].createGrid(getRowSize(), 1, ans), new IntMatrix(index));
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public IndexedMatrix<S,M> minimumColumnWise() {
    final Object[] ret = AbstractNumericalMatrixUtil.minimumColumnWise(getElements());
    final S[][] ans = (S[][])ret[0];
    final int[] index = (int[])ret[1];
    return new IndexedMatrix<>(ans[0][0].createGrid(1, getColumnSize(), ans), new IntMatrix(index));
  }

  /**
   * {@inheritDoc}
   */
  public M signumElementWise() {
    final S[][] ans = AbstractNumericalMatrixUtil.signumElementWise(getElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * スカラーの成分毎の累乗を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param scalar 累乗の対象となるスカラー
   * @param matrix 累乗の指数を成分とする行列
   * @return 累乗を成分とする行列
   */
  public static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> M powerElementWise(final S scalar, final DoubleMatrix matrix) {
    final S[][] ans = AbstractNumericalMatrixUtil.powerElementWise(scalar, matrix.getDoubleElements());
    return ans[0][0].createGrid(matrix.getRowSize(), matrix.getColumnSize(), ans);
  }

  /**
   * スカラーの成分毎の累乗を成分とする行列を生成します。
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param scalar 累乗の対象となるスカラー
   * @param matrix 累乗の指数を成分とする行列
   * @return 累乗を成分とする行列
   */
  public static <S extends NumericalScalar<S,M>, M extends AbstractNumericalMatrix<S,M>> M  powerElementWise(final S scalar, final AbstractNumericalMatrix<S,M> matrix) {
    final S[][] ans = AbstractNumericalMatrixUtil.powerElementWise(scalar, matrix.getElements());
    return ans[0][0].createGrid(matrix.getRowSize(), matrix.getColumnSize(), ans);
  }

  /**
   * スカラーの成分毎の累乗を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型ー
   * 
   * @param scalar 累乗の対象となるスカラー
   * @param matrix 累乗の指数を成分とする行列
   * @return 累乗を成分とする行列
   */
  public static <S extends NumericalScalar<S,M>,M extends NumericalMatrix<S,M>> M powerElementWise(final S scalar, final IntMatrix matrix) {
    final S[][] ans = BaseMatrixUtil.powerElementWise(scalar, matrix.getIntElements());
    return ans[0][0].createGrid(matrix.getRowSize(), matrix.getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public IndexedMatrix<S,M> sort() {
    final IndexedMatrix<S,M> ret = reshape(1, getRowSize() * getColumnSize()).sortRowWise();
    return new IndexedMatrix<>(ret.getMatrix().reshape(getRowSize(), getColumnSize()), ret.getIndices().reshape(getRowSize(), getColumnSize()));
  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public M powerElementWise(final M value) {
////    if (value instanceof IntMatrix) {
////      return powerElementWise((IntMatrix)value);
////    }
////    if (value instanceof DoubleMatrix) {
////      return powerElementWise((DoubleMatrix)value);
////    }
//    if (value instanceof BaseNumericalMatrix<?,?>) {
//      return powerElementWise((BaseNumericalMatrix<?,?>)value);
//    }
//
//    throw new IllegalArgumentException(Messages.getString("NumericalMatrix.3")); //$NON-NLS-1$
//  }
  
//  /**
//   * 成分毎計算した関数の値を成分とする行列を生成します。
//   * 
//   * @param function 複素数関数
//   * @return 成分毎計算した関数の値を成分とする行列
//   */
//  private M elementWiseFunction(final ScalarFunction<S,M> function) {
//    final S[][] ans = BaseMatrixUtil.elementWiseFunction(getElements(), function);
//    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
//  }


  /**
   * {@inheritDoc}
   */
  public M sinElementWise() {
    return elementWiseFunction(new SinFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.88 $, 2006/08/31
   */
  class SinFunction implements NumericalScalarFunction<S,M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.sin();
    }
  }

  /**
   * {@inheritDoc}
   */
  public M sinhElementWise() {
    return elementWiseFunction(new SinhFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.88 $, 2006/08/31
   */
  class SinhFunction implements NumericalScalarFunction<S,M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.sinh();
    }
  }

  /**
   * {@inheritDoc}
   */
  public M asinElementWise() {
    return elementWiseFunction(new AsinFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.88 $, 2006/08/31
   */
  class AsinFunction implements NumericalScalarFunction<S,M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.asin();
    }
  }

  /**
   * {@inheritDoc}
   */
  public M asinhElementWise() {
    return elementWiseFunction(new AsinhFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.88 $, 2006/08/31
   */
  class AsinhFunction implements NumericalScalarFunction<S,M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.asinh();
    }
  }

  /**
   * {@inheritDoc}
   */
  public M cosElementWise() {
    return elementWiseFunction(new CosFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.88 $, 2006/08/31
   */
  class CosFunction implements NumericalScalarFunction<S,M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.cos();
    }
  }

  /**
   * {@inheritDoc}
   */
  public M coshElementWise() {
    return elementWiseFunction(new CoshFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.88 $, 2006/08/31
   */
  class CoshFunction implements NumericalScalarFunction<S,M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.cosh();
    }
  }

  /**
   * {@inheritDoc}
   */
  public M acosElementWise() {
    return elementWiseFunction(new AcosFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.88 $, 2006/08/31
   */
  class AcosFunction implements NumericalScalarFunction<S,M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.acos();
    }
  }

  /**
   * {@inheritDoc}
   */
  public M acoshElementWise() {
    return elementWiseFunction(new AcoshFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.88 $, 2006/08/31
   */
  class AcoshFunction implements NumericalScalarFunction<S,M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.acosh();
    }
  }

  /**
   * {@inheritDoc}
   */
  public M tanElementWise() {
    return elementWiseFunction(new TanFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.88 $, 2006/08/31
   */
  class TanFunction implements NumericalScalarFunction<S,M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.tan();
    }
  }

  /**
   * {@inheritDoc}
   */
  public M tanhElementWise() {
    return elementWiseFunction(new TanhFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.88 $, 2006/08/31
   */
  class TanhFunction implements NumericalScalarFunction<S,M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.tanh();
    }
  }

  /**
   * {@inheritDoc}
   */
  public M atanElementWise() {
    return elementWiseFunction(new AtanFunction());
  }

  /**
   * {@inheritDoc}
   */
  public M atan2ElementWise(final M value) {
    return elementWiseFunction(value, new Atan2Function());
  }

//  /**
//   * {@inheritDoc}
//   */
//  public M atan2ElementWise(final IntMatrix value) {
//    if (AbstractMatrix.isTransformableToSameClass(this, value)) {
//      final Matrix<?,?>[] mm = AbstractMatrix.transformToSameClass(this, value);
//      if (mm[0] instanceof BaseNumericalMatrix<?,?>) {
//        return elementWiseFunction((M)mm[1], new Atan2Function());
//      }
//    }
//
//    throw new IllegalArgumentException(Messages.getString("NumericalMatrix.4")); //$NON-NLS-1$
//  }

  /**
   * {@inheritDoc}
   */
  public M atan2ElementWise(final S value) {
    return elementWiseFunction(value, new Atan2Function());
  }

  /**
   * {@inheritDoc}
   */
  public M remainderElementWise(final S value) {
    return elementWiseFunction(value, new RemainderFunction());
  }

  /**
   * {@inheritDoc}
   */
  public M modulusElementWise(final S value) {
    return elementWiseFunction(value, new ModulusFunction());
  }

  /**
   * {@inheritDoc}
   */
  public M remainderElementWise(final M value) {
//    if (AbstractMatrix.isTransformableToSameClass(this, value)) {
//      final Matrix<?,?>[] mm = AbstractMatrix.transformToSameClass(this, value);
//      if (mm[0] instanceof BaseNumericalMatrix<?,?>) {
        return elementWiseFunction(value, new RemainderFunction());
//      }
//    }

//    throw new IllegalArgumentException(Messages.getString("NumericalMatrix.5")); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public M modulusElementWise(final M value) {
//    if (AbstractMatrix.isTransformableToSameClass(this, value)) {
//      final Matrix<?,?>[] mm = AbstractMatrix.transformToSameClass(this, value);
    
//      if (mm[0] instanceof BaseNumericalMatrix<?,?>) {
        return elementWiseFunction(value, new ModulusFunction());
//      }
//    }

//    throw new IllegalArgumentException(Messages.getString("NumericalMatrix.6")); //$NON-NLS-1$
  }

  /**
   * 各成分の正接(2)関数の結果を成分とする行列を生成します。
   * 
   * @param value 分母側の数を成分とする行列
   * @return 成分毎正接(2)関数行列
   */
  public M atan2ElementWise(final DoubleMatrix value) {
    if (!this.isSameSize(value)) {
      throw new MatrixSizeException(Messages.getString("NumericalMatrix.7")); //$NON-NLS-1$
    }

    final S[][] ans = AbstractNumericalMatrixUtil.atan2ElementWise(getElements(), value.getDoubleElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * @author koga
   * @version $Revision: 1.88 $, 2006/08/31
   */
  class AtanFunction implements NumericalScalarFunction<S,M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.atan();
    }
  }

  /**
   * @author koga
   * @version $Revision: 1.88 $, 2008/03/07
   */
  class Atan2Function implements NumericalScalarFunctionWithTwoArguments<S,M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument1, final S argument2) {
      return argument1.atan2(argument2);
    }
  }

  /**
   * @author koga
   * @version $Revision: 1.88 $, 2008/03/07
   */
  class RemainderFunction implements NumericalScalarFunctionWithTwoArguments<S,M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument1, final S argument2) {
      return argument1.remainder(argument2);
    }

  }

  /**
   * @author koga
   * @version $Revision: 1.88 $, 2008/03/07
   */
  class ModulusFunction implements NumericalScalarFunctionWithTwoArguments<S,M>{

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument1, final S argument2) {
      return argument1.modulus(argument2);
    }

  }

  /**
   * {@inheritDoc}
   */
  public M atanhElementWise() {
    return elementWiseFunction(new AtanhFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.88 $, 2006/08/31
   */
  class AtanhFunction implements NumericalScalarFunction<S,M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.atanh();
    }
  }

  /**
   * {@inheritDoc}
   */
  public M expElementWise() {
    return elementWiseFunction(new ExpFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.88 $, 2006/08/31
   */
  class ExpFunction implements NumericalScalarFunction<S,M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.exp();
    }
  }

  /**
   * {@inheritDoc}
   */
  public M logElementWise() {
    return elementWiseFunction(new LogFunction());
  }

  /**
   * {@inheritDoc}
   */
  public M log10ElementWise() {
    return elementWiseFunction(new Log10Function());
  }

  /**
   * @author koga
   * @version $Revision: 1.88 $, 2006/08/31
   */
  class Log10Function implements NumericalScalarFunction<S,M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.log().divide(argument.create(10).log());
    }
  }

  /**
   * {@inheritDoc}
   */
  public M sqrtElementWise() {
    return elementWiseFunction(new SqrtFunction());
  }

  /**
   * 平方根を返す関数を表すクラスです。
   * 
   * @author koga
   * @version $Revision: 1.88 $, 2006/08/31
   */
  class SqrtFunction implements NumericalScalarFunction<S,M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.sqrt();
    }
  }

  /**
   * 成分毎計算した関数の値を成分とする行列を求めます。
   * 
   * @param function {@link NumericalScalar}を引数とする関数
   * @return 成分毎計算した関数の値を成分とする行列
   */
  private M elementWiseFunction(final NumericalScalarFunction<S,M> function) {
    final S[][] ans = AbstractNumericalMatrixUtil.elementWiseFunction(getElements(), function);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * 成分毎計算した関数の値を成分とする行列を求めます。
   * 
   * @param matrix 対象となる第二行列
   * @param function {@link NumericalScalar}を引数とする関数
   * @return 成分毎計算した関数の値を成分とする行列
   */
  private M elementWiseFunction(final M matrix, final NumericalScalarFunctionWithTwoArguments<S,M> function) {
    final S[][] ans = AbstractNumericalMatrixUtil.elementWiseFunction(getElements(), matrix.getElements(), function);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * 成分毎計算した関数の値を成分とする行列を求めます。
   * 
   * @param value 対象となる第二引数
   * @param function {@link NumericalScalar}を引数とする関数
   * @return 成分毎計算した関数の値を成分とする行列
   */
  private M elementWiseFunction(final S value, final NumericalScalarFunctionWithTwoArguments<S,M> function) {
    final S[][] ans = AbstractNumericalMatrixUtil.elementWiseFunction(getElements(), value, function);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public M maxElementWise(final M opponent) {
//    if (AbstractMatrix.isTransformableToSameClass(this, opponent)) {
//      final Matrix<?,?>[] mm = AbstractMatrix.transformToSameClass(this, opponent);      
//
//      if (mm[0] instanceof BaseNumericalMatrix<?,?>) {
//        return ((BaseNumericalMatrix<?,?>)mm[0]).maxElementWise((BaseNumericalMatrix<?,?>)mm[1]);
//      }
//    }
//
//    throw new IllegalArgumentException(Messages.getString("NumericalMatrix.8")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public M maxElementWise(final IntMatrix opponent) {
//    if (AbstractMatrix.isTransformableToSameClass(this, opponent)) {
//      final Matrix<?,?>[] mm = AbstractMatrix.transformToSameClass(this, opponent);
//      
//      if (mm[0] instanceof BaseNumericalMatrix<?,?>) {
//        return ((M)mm[0]).maxElementWise((M)mm[1]);
//      }
//    }
//
//    throw new IllegalArgumentException(Messages.getString("NumericalMatrix.8")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public NumericalMatrix<?,?> minElementWise(final NumericalMatrix<?,?> opponent) {
//    if (AbstractMatrix.isTransformableToSameClass(this, opponent)) {
//      final Matrix<?,?>[] mm = AbstractMatrix.transformToSameClass(this, opponent);
//
//      if (mm[0] instanceof BaseNumericalMatrix<?,?>) {
//        return ((BaseNumericalMatrix<?,?>)mm[0]).minElementWise((BaseNumericalMatrix<?,?>)mm[1]);
//      }
//    }
//
//    throw new IllegalArgumentException(Messages.getString("NumericalMatrix.9")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public M minElementWise(final IntMatrix opponent) {
//    if (AbstractMatrix.isTransformableToSameClass(this, opponent)) {
//      final  M[] mm = (M[])AbstractMatrix.transformToSameClass(this, opponent);
//
//      if (mm[0] instanceof BaseNumericalMatrix<?,?>) {
//        return mm[0].minElementWise(mm[1]);
//      }
//    }
//
//    throw new IllegalArgumentException(Messages.getString("NumericalMatrix.9")); //$NON-NLS-1$
//  }


  /**
   * {@inheritDoc}
   */
  public M log() {
    return matrixFunction(new LogFunction());
  }

  /**
   * 自然対数を返す関数を表すクラスです。
   * 
   * @author koga
   * @version $Revision: 1.88 $, 2006/08/31
   */
  class LogFunction implements NumericalScalarFunction<S,M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.log();
    }
  }

  /**
   * {@inheritDoc}
   */
  public M kernel() {
    return kernel(this.frobNorm().multiply(getElement(1, 1).getMachineEpsilon()));
  }

  /**
   * {@inheritDoc}
   */
  public LUDecomposition<S,M> luDecompose(final double tolerance, final boolean stopIfSingular) {
    return luDecompose(getElement(1,1).create(tolerance), stopIfSingular);
  }

  /**
   * {@inheritDoc}
   */
  public LUDecomposition<S,M> luDecompose(final S tolerance, final boolean stopIfSingular) {
    final LUDecompostionElements<S,M> lu = new LUDecomposer<S,M>().decompose(getElements(), tolerance, stopIfSingular);

    final S[][] ll = lu.getL();
    final S[][] uu = lu.getU();

    final M l = ll[0][0].createGrid(getRowSize(), getColumnSize(), ll);
    final M u =uu[0][0].createGrid(getRowSize(), getColumnSize(), uu);

    return new LUDecomposition<>(l, u);
  }

  /**
   * {@inheritDoc}
   */
  public LUDecomposition<S,M> luDecomposeWithPermutation(final S tolerance, final boolean stopIfSingular) {
    final LUDecompostionElements<S,M> lup = new LUDecomposer<S,M>().decomposeWithPermutation(getElements(), tolerance, stopIfSingular);

    final S[][] ll = lup.getL();
    final S[][] uu = lup.getU();
    final int[][] pp = lup.getP();

    final M l = ll[0][0].createGrid(getRowSize(), getColumnSize(), ll);
    final M u =uu[0][0].createGrid(getColumnSize(), getColumnSize(), uu);
    final IntMatrix p = new IntMatrix(getRowSize(), getRowSize(), pp);

    return new LUDecomposition<>(l, u, p);
  }

  /**
   * {@inheritDoc}
   */
  public LUDecomposition<S,M> luDecomposeWithPermutation(final double tolerance, final boolean stopIfSingular) {
    return luDecomposeWithPermutation(getElement(1, 1).create(tolerance), stopIfSingular);
  }

  /**
   * {@inheritDoc}
   */
  public LUDecomposition<S,M> luDecompose(final boolean stopIfSingular) {
    return luDecompose(this.frobNorm().multiply(getElement(1, 1).getMachineEpsilon()), stopIfSingular);
  }

  /**
   * {@inheritDoc}
   */
  public LUDecomposition<S,M> luDecomposeWithPermutation(final boolean stopIfSingular) {
    return luDecomposeWithPermutation(this.frobNorm().multiply(getElement(1, 1).getMachineEpsilon()), stopIfSingular);
  }

  /**
   * {@inheritDoc}
   */
  public M pseudoInverse() {
    return pseudoInverse(this.frobNorm().multiply(getElement(1, 1).getMachineEpsilon()));
  }

  /**
   * 自身の逆行列と行列<code>value</code>の積(<code>this</code> <sup>-1 </sup>*<code>value</code>)を生成します。
   * 
   * @param value 実数行列
   * @return 自身の逆行列と<code>value</code>の積
   */
  @Override
  public M leftDivide(final M value) {
    if (getRowSize() != value.getRowSize()) {
      throw new MatrixSizeException(this, value, Messages.getString("NumericalMatrix.0")); //$NON-NLS-1$
    }

    final S norm = this.frobNorm();
    final S tolerance = norm.multiply(norm.getMachineEpsilon());

    if (getRowSize() != getColumnSize()) {
      final S[][] ans = new RealSingularValueDecomposer<S,M>().leastSquare(getElements(), value.getElements(), tolerance);
      final int rowSize = ans.length;
      final int columnSize = (rowSize == 0 || ans[0] == null) ? 0 : ans[0].length; 
      return ans[0][0].createGrid(rowSize, columnSize, ans);
    }

    final S[][] ans = new LUDecomposer<S,M>().leftDivide(getElements(), value.getElements(), tolerance, false);
    final int rowSize = ans.length;
    final int columnSize = (rowSize == 0 || ans[0] == null) ? 0 : ans[0].length; 
    
    return ans[0][0].createGrid(rowSize, columnSize, ans);
  }
  
  /**
   * {@inheritDoc}
   */
  public int rank() {
    final S tolerance = this.frobNorm().multiply(getElement(1, 1).getMachineEpsilon());
    return rank(tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public int rank(final double tolerance) {
    return rank(getElement(1, 1).create(tolerance));
  }

  /**
   * {@inheritDoc}
   */
  public int rank(final S tolerance) {
    final M singularValues = singularValue();
    
    int rank = 0;
    for (int i = 1; i <= singularValues.length(); i++) {
      if (singularValues.getElement(i).isGreaterThan(tolerance)) {
        rank++;
      }
    }
    
    return rank;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isFullRank() {
    final S tolerance = this.frobNorm().multiply(getElement(1, 1).getMachineEpsilon());
    return isFullRank(tolerance);
  }



  /**
   * {@inheritDoc}
   */
  public M exp() {
    return exp(getElement(1, 1).getMachineEpsilon().multiply(this.frobNorm()));
  }

  /**
   * {@inheritDoc}
   */
  public M exp(final double tolerance) {
    return exp(getElement(1,1).create(tolerance));
  }

  /**
   * {@inheritDoc}
   */
  public M exp(final S tolerance) {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    final S[][] exp = ExponentialMatrix.exp(this.getElements(), tolerance);
    return exp[0][0].createGrid(getRowSize(), getColumnSize(), exp);
  }

  /**
   * {@inheritDoc}
   */
  public BalancedDecomposition<S,M> balancedDecompose() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    final BalancedDecompositionElements<S,M> db = new BalancedDecomposer<S,M>().decompose(this.getElements());

    final S[][] dd = db.getD();
    final S[][] bb = db.getB();

    final S dd00 = dd[0][0];
    final M d = dd00.createGrid(getRowSize(), getColumnSize(), dd);
    final M b = dd00.createGrid(getRowSize(), getColumnSize(), bb);
    return new BalancedDecomposition<>(d, b);
  }

  /**
   * {@inheritDoc}
   */
  public S conditionNumber() {
    final M values = singularValue();
    
    if (values.compareElementWise(".==", 0).anyTrue()) { //$NON-NLS-1$
      return values.getElement(1).getInfinity();
    }
    
    return values.max().divide(values.min());
  }

//  /**
//   * {@inheritDoc}
//   */
//  public EigenSolution<?,?> eigenDecompose() {
//    if (isSquare() == false) {
//      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
//    }
//    final RealEigenSolver<S,M> eigenSolver = new RealEigenSolver<>();
//    final EigenSolutionElements<S,M> eig = eigenSolver.solve(getElements());
//
//    final S[][] realValues = GridUtil.diagonal(eig.getReValue());
//    final S[][] imagValues = GridUtil.diagonal(eig.getImValue());
//    final M realValue = realValues[0][0].createGrid(realValues);
//    final M imagValue = imagValues[0][0].createGrid(imagValues);
//    
//    final S[][] realVectors = eig.getReVector();
//    final S[][] imagVectors = eig.getImVector();
//    final M realVector = realVectors[0][0].createGrid(realValues);
//    final M imagVector = imagVectors[0][0].createGrid(imagVectors);
//
//    return new EigenSolution<>(realValue, imagValue,  realVector, imagVector);
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public EigenSolution<?,?> eigenDecompose(final M b) {
//    if (isSquare() == false || b.isSquare() == false) {
//      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
//    }
//    if (hasSameRowSize(b) == false) {
//      throw new MatrixSizeException(this, b, MatrixSizeException.INCORRECT_SIZE);
//    }
//
//    final EigenSolutionElements<S,M> eig = new RealGeneralizedEigenSolver<S,M>().solve(getElements(), b.getElements());
//    final S[][] realValues = GridUtil.diagonal(eig.getReValue());
//    final S[][] imagValues = GridUtil.diagonal(eig.getImValue());
//    final M realValue = realValues[0][0].createGrid(realValues);
//    final M imagValue = imagValues[0][0].createGrid(imagValues);
//    
//    final S[][] realVectors = eig.getReVector();
//    final S[][] imagVectors = eig.getImVector();
//    final M realVector = realVectors[0][0].createGrid(realVectors);
//    final M imagVector = imagVectors[0][0].createGrid(imagVectors);
//
//    return new EigenSolution<>(realValue, imagValue, realVector, imagVector);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public NumericalMatrix<?,?> eigenValue() {
//    if (isSquare() == false) {
//      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
//    }
//
//    final RealEigenSolver<S,M> eigenSolver = new RealEigenSolver<>();
//    final BaseComplexNumericalScalar<S,M>[] values = eigenSolver.getEigenValue(getElements());
//    return new BaseNumericalMatrix<>(GridUtil.transpose(values));
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public NumericalMatrix<?,?> eigenValue(final M b) {
//    if (isSquare() == false || b.isSquare() == false) {
//      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
//    }
//    if (getRowSize() != b.getRowSize()) {
//      throw new MatrixSizeException(this, b, MatrixSizeException.INCORRECT_SIZE);
//    }
//
//    final RealGeneralizedEigenSolver<S,M> eigenSolver = new RealGeneralizedEigenSolver<>();
//    final BaseComplexNumericalScalar<S,M,?,?>[] values = eigenSolver.getEigenValue(getElements(), b.getElements());
//    
//    BaseComplexNumericalScalar<S, M, ?, ?>[][] eigenValues = GridUtil.transpose(values);
//    return new BaseNumericalMatrix<>(eigenValues);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public NumericalMatrix<?,?> eigenVector() {
//    if (isSquare() == false) {
//      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
//    }
//
//    final RealEigenSolver<S,M> eigenSolver = new RealEigenSolver<>();
//    final BaseComplexNumericalScalar<S,M,?,?>[][] vectors = eigenSolver.getEigenVector(getElements());
//    return new BaseNumericalMatrix<>(vectors);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public NumericalMatrix<?,?> eigenVector(final M b) {
//    if (isSquare() == false || b.isSquare() == false) {
//      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
//    } 
//    if (getRowSize() != b.getRowSize()) {
//      throw new MatrixSizeException(this, b, MatrixSizeException.INCORRECT_SIZE);
//    }
//
//    final BaseComplexNumericalScalar<S,M,?,?>[][] vectors = new RealGeneralizedEigenSolver<S,M>().getEigenVector(getElements(), b.getElements());
//    return new BaseNumericalMatrix<>(vectors);
//  }

  /**
   * {@inheritDoc}
   */
  public HessenbergDecomposition<S,M> hessenbergDecompose() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    final HessenbergDecompositionElements<S,M> qh = new RealHessenbergDecomposer<S,M>().decompose(getElements());
    final S[][] qq = qh.getQ();
    final S[][] hh = qh.getH();
    final M q = qq[0][0].createGrid(qq);
    final M h =hh[0][0].createGrid(hh);

    return new HessenbergDecomposition<>(q, h);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isFullRank(final double tolerance) {
    final int rank = rank(tolerance);
    return rank == getRowSize() || rank == getColumnSize();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isFullRank(final S tolerance) {
    final int rank = rank(tolerance);
    return rank == getRowSize() || rank == getColumnSize();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isZero(final S tolerance) {
    return AbstractNumericalMatrixUtil.isZero(getElements(), tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isUnit(final S tolerance) {
    return AbstractNumericalMatrixUtil.isUnit(getElements(), tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public M kernel(final double tolerance) {
    return kernel(getElement(1,1).create(tolerance));
  }

  /**
   * {@inheritDoc}
   */
  public M kernel(final S tolerance) {
    final S[][] kernel = new RealSingularValueDecomposer<S,M>().kernel(getElements(), tolerance);
    return kernel[0][0].createGrid(kernel);
    //return (M)new BaseNumericalMatrix<>(kernel);
  }

  /**
   * {@inheritDoc}
   */
  public S maxSingularValue() {
    final M values = singularValue();
    return values.getElement(1);
  }

  /**
   * {@inheritDoc}
   */
  public S minSingularValue() {
    final M values = singularValue();
    return values.getElement(values.length());
  }

  /**
   * {@inheritDoc}
   */
  public S norm(final NormType normType) {
    if (normType == NormType.FROBENIUS) {
      return this.frobNorm();
    } 
    if (normType == NormType.INFINITY) {
      return this.infNorm();
    } 
    if (normType == NormType.ONE || normType == NormType.TWO) {
      return AbstractNumericalMatrixUtil.norm(getElements(), normType);
    }
    throw new IllegalArgumentException(Messages.getString("NumericalMatrix.20")); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public M pseudoInverse(final double tolerance) {
    return pseudoInverse(getElement(1,1).create(tolerance));
  }

  /**
   * {@inheritDoc}
   */
  public M pseudoInverse(final S tolerance) {
    final S[][] pseudoInverse = new RealSingularValueDecomposer<S,M>().pseudoInverse(getElements(), tolerance);
    return pseudoInverse[0][0].createGrid(pseudoInverse);
    //return (M)new BaseNumericalMatrix<>(pseudoInverse);
  }

  /**
   * {@inheritDoc}
   */
  public QRDecomposition<S,M> qrDecompose() {
    final QRDecompositionElements<S,M> qr = new RealQRDecomposer<S,M>().decompose(getElements());
    final S[][] qq = qr.getQ();
    final S[][] rr = qr.getR();
    final M q = qq[0][0].createGrid(qq);
    final M r = rr[0][0].createGrid(rr);
    return new QRDecomposition<>(q, r);
  }

  /**
   * {@inheritDoc}
   */
  public QRDecomposition<S,M> qrDecomposeWithPermutation() {
    final QRDecompositionElements<S,M> qrp = new RealQRDecomposer<S,M>().decomposeWithPermutation(getElements());
    final S[][] qq = qrp.getQ();
    final S[][] rr = qrp.getR();
    final int[][] pp = qrp.getP();
    final M q = qq[0][0].createGrid(qq);
    final M r = rr[0][0].createGrid(rr);
    final IntMatrix p = new IntMatrix(pp);
    return new QRDecomposition<>(q, r, p);
  }

//  /**
//   * {@inheritDoc}
//   */
//  @SuppressWarnings("unchecked")
//  public QZDecomposition<S,M> qzDecompose(final M b) {
//    final QZDecompositionElements<S,M> qz = new RealQZDecomposer<S,M>().decompose(getElements(), b.getElements());
//    S[][] aaa = qz.getAA();
//    final M aa =aaa[0][0].createGrid(aaa);
//    S[][] bbb = qz.getBB();
//    final M bb = bbb[0][0].createGrid(aaa);
//    S[][] qqq = qz.getQ();
//    final M q =qqq[0][0].createGrid(aaa);
//    S[][] zzz = qz.getZ();
//    final M z = zzz[0][0].createGrid(aaa);
//    S[][] xxxRe = qz.getReX();
//    final M xReal = xxxRe[0][0].createGrid(aaa);
//    S[][] xxxIm = qz.getImX();
//    final M xImag = xxxIm[0][0].createGrid(aaa);
//    final BaseNumericalComplexMatrix<S,M> x = xReal.createComplexMatrix(xReal, xImag);
//    return new QZDecomposition<>(aa, bb, q, z, x);
//  }

  /**
   * {@inheritDoc}
   */
  public SchurDecomposition<S,M> schurDecompose() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    final SchurDecompositionElements<S,M> ut = new RealSchurDecomposer<S,M>().decompose(getElements());
    final S[][] uu = ut.getU();
    final S[][] tt = ut.getT();
    final M u = uu[0][0].createGrid(uu);
    final M t = tt[0][0].createGrid(tt);

    return new SchurDecomposition<>(u, t);
  }

  /**
   * {@inheritDoc}
   */
  public M singularValue() {
    final S[][] elements = getElements();
    final S[] values = new RealSingularValueDecomposer<S,M>().singularValue(elements);
    return elements[0][0].createGrid(values).transpose();
  }

  /**
   * {@inheritDoc}
   */
  public SingularValueDecomposition<S,M> singularValueDecompose() {
    final SingularValueDecompositionElements<S,M> udv = new RealSingularValueDecomposer<S,M>().decompose(getElements());

    final S[][] uu = udv.getU();

    final S[][] dd = udv.getD();
    final S[][] vv = udv.getV();

    final M u =uu[0][0].createGrid(uu);
    final M d = dd[0][0].createGrid(dd);
    final M v = vv[0][0].createGrid(vv);
    return new SingularValueDecomposition<>(u, d, v);
  }

  /**
   * {@inheritDoc}
   */
  public M choleskyDecompose() {
    final S norm = frobNorm();
    final S tolerance = norm.multiply(norm.getMachineEpsilon()); 
    return choleskyDecompose(tolerance);
  }
  
  /**
   * {@inheritDoc}
   */
  public M choleskyDecompose(S tolerance) {
    return new CholeskyDecomposer<S,M>().decompose((M)this, tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public M choleskyDecompose(double tolerance) {
    return choleskyDecompose(getElement(1,1).create(tolerance));
  }

  /**
   * {@inheritDoc}
   */
  public boolean equals(final M opponent, final S tolerance) {
//    if (opponent instanceof BaseNumericalMatrix == false) {
//      return false;
//    }
    return AbstractNumericalMatrixUtil.equals(getElements(), opponent.getElements(), tolerance);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public M inverse(final double tolerance, final boolean stopIfSingular) {
    return super.inverse(tolerance, stopIfSingular);
  }

  /**
   * {@inheritDoc}
   */
  public M inverse(final S tolerance, final boolean stopIfSingular) {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    final S[][] ans = new LUDecomposer<S,M>().inverse(getElements(), tolerance, stopIfSingular);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public M inverse() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    return inverse(getElement(1, 1).getMachineEpsilon(), false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public M inverseElementWise() {
    return super.inverseElementWise();
  }

  /**
   * 行列式を返します。
   * 
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return 行列式
   */
  public S determinant(final S tolerance, final boolean stopIfSingular) {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    final NumericalGaussianEliminationElements<S,M> tmp = new NumericalGaussianEliminationSolver<S,M>().inverse(getElements(), tolerance, stopIfSingular);
    return tmp.getDeterminat();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public M roundToZeroElementWise() {
    return roundToZeroElementWise(getElement(1, 1).getMachineEpsilon());
  }

  /**
   * {@inheritDoc}
   */
  public M roundToZeroElementWise(final S tolerance) {
    final S[][] ans = AbstractNumericalMatrixUtil.roundToZeroElementWise(getElements(), tolerance);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public boolean isTransformableFrom(final Matrix<?,?> value) {
//    if (super.isTransformableFrom(value)) {
//      return true;
//    }
//
//    if (isEmpty()) {
//      return false;
//    }
//    
//    if (value instanceof BaseNumericalMatrix<?,?> && (value.isEmpty() || getElement(1, 1).isTransformableFrom(((BaseMatrix<?, ?>)value).getElement(1, 1)))) {
//      return true;
//    }
//    if (value instanceof IntMatrix) {
//      return true;
//    }
//    if (value instanceof DoubleMatrix) {
//      return true;
//    }
//
//    return false;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public M transformFrom(final Matrix<?,?> value) {
//    if (super.isTransformableFrom(value)) {
//      return super.transformFrom(value);
//    }
//    
//    if (isEmpty()) {
//      throw new IllegalArgumentException(Messages.getString("BaseMatrix.26") + value); //$NON-NLS-1$
//    }
//    
//    final int valueRowSize = value.getRowSize();
//    final int valueColumnSize = value.getColumnSize();
//    final S element = getElement(1,1);
//    
//    if (value instanceof BaseNumericalMatrix<?,?> && (value.isEmpty() || element.isTransformableFrom(((BaseMatrix<?, ?>)value).getElement(1, 1)))) {
//      return element.createGrid(valueRowSize, valueColumnSize, createArrayWithTransformationFrom(((M)value).getElements()));
//      //return (M)new BaseNumericalMatrix<>(valueRowSize, valueColumnSize, createArrayWithTransformationFrom(((BaseMatrix<?, ?>)value).getElements()));
//    }
//    
//    if (value instanceof IntMatrix) {
//      return element.createGrid(valueRowSize, valueColumnSize, createArray(((IntMatrix)value).getIntElements()));
//    }
//    
//    if (value instanceof DoubleMatrix) {
//      return element.createGrid(valueRowSize, valueColumnSize, createArray(((DoubleMatrix)value).getDoubleElements()));
//      //return (M)new BaseNumericalMatrix<>(valueRowSize, valueColumnSize, createArray(((DoubleMatrix)value).getDoubleElements()));
//    }
//
//    throw new IllegalArgumentException(Messages.getString("BaseMatrix.26") + value); //$NON-NLS-1$
//  }

//  /**
//   * int型の配列をE型の配列に変換します。
//   * 
//   * @param intElements int型の配列
//   * @return E型の配列に変換します。
//   */
//  private S[][] createArray(final int[][] intElements) {
//    final int ansRowSize = intElements.length;
//    final int ansColumnSize = ansRowSize == 0 ? 0 : intElements[0].length;
//    final S value = getElement(1, 1);
//    final S[][] ansElements = value.createArray(ansRowSize, ansColumnSize);
//
//    for (int row = 0; row < ansRowSize; row++) {
//      for (int column = 0; column < ansColumnSize; column++) {
//        ansElements[row][column] = value.transformFrom(intElements[row][column]);
//      }
//    }
//
//    return ansElements;
//  }

//  /**
//   * double型の配列をE型の配列に変換します。
//   * 
//   * @param doubleElements double型の配列
//   * @return E型の配列に変換します。
//   */
//  private S[][] createArray(final double[][] doubleElements) {
//    final int ansRowSize = doubleElements.length;
//    final int ansColumnSize = ansRowSize == 0 ? 0 : doubleElements[0].length;
//    final S value = getElement(1, 1);
//    final S[][] ansElements = value.createArray(ansRowSize, ansColumnSize);
//
//    for (int row = 0; row < ansRowSize; row++) {
//      for (int column = 0; column < ansColumnSize; column++) {
//        ansElements[row][column] = value.transformFrom(doubleElements[row][column]);
//      }
//    }
//
//    return ansElements;
//  }

//  /**
//   * MatrixElement型の配列をE型の配列に変換します。
//   * 
//   * @param matrixElements MatrixElement型の配列
//   * @return E型の配列に変換します。
//   */
//  private S[][] createArrayWithTransformationFrom(final S[][] matrixElements) {
//    final int ansRowSize = matrixElements.length;
//    final int ansColumnSize = ansRowSize == 0 ? 0 : matrixElements[0].length;
//    final S value = getElement(1, 1);
//    final S[][] ansElements = value.createArray(ansRowSize, ansColumnSize);
//
//    for (int row = 0; row < ansRowSize; row++) {
//      for (int column = 0; column < ansColumnSize; column++) {
//        ansElements[row][column] = matrixElements[row][column].clone();
//        //ansElements[row][column] = value.transformFrom(matrixElements[row][column]);
//      }
//    }
//
//    return ansElements;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public NumericalMatrix<?,?> getRealPart() {
//    return (NumericalMatrix<?,?>)super.getRealPart();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public NumericalMatrix<?,?> getImaginaryPart() {
//    return (NumericalMatrix<?,?>)super.getImaginaryPart();
//  }
  
  
  /**
   * 行列関数の値を返します。
   * 
   * @param function 複素数関数
   * @return 行列関数
   */
  final M matrixFunction(final NumericalScalarFunction<S,M> function) {
    final int n = getColumnSize();

    final M b = createClone();
    M f = this.createZero(b.getRowSize(), b.getColumnSize());

    final SchurDecomposition<S,M> tmp = b.schurDecompose();
    final M u = tmp.getU();
    final M t = tmp.getT();

    for (int i = 1; i <= n; i++) {
      f.setElement(i, i, function.evaluate(t.getElement(i, i)));
    }

    for (int p = 1; p <= n - 1; p++) {
      for (int i = 1; i <= n - p; i++) {
        int j = i + p;
        /*
         * s = TTC(i,j)*(FFC(j,j) - FFC(i,i));
         */
        final S tmp1 = f.getElement(j, j).subtract(f.getElement(i, i));
        S s = t.getElement(i, j).multiply(tmp1);

        for (int k = i + 1; k <= j - 1; k++) {
          /*
           * s = s + TTC(i,k)*FFC(k,j) - FFC(i,k)*TTC(k,j);
           */
          final S tmp2 = t.getElement(i, k).multiply(f.getElement(k, j));
          final S tmp3 = f.getElement(i, k).multiply(t.getElement(k, j));
          s = s.add(tmp2).subtract(tmp3);
        }
        /*
         * FFC(i,j) = s/(TTC(j,j) - TTC(i,i));
         */
        S tmp4 = t.getElement(j, j).subtract(t.getElement(i, i));

        /* Revised by Koga 1996.4.3 */
        if (tmp4.isZero()) {
          tmp4 = tmp4.getMachineEpsilon();
          //tmp4 = tmp4.transformFrom(tmp4.getMachineEpsilon());
        }

        try {
          f.setElement(i, j, s.divide(tmp4));
        } catch (RuntimeException e) {
          throw new RuntimeException(Messages.getString("NumericalMatrix.10"), e); //$NON-NLS-1$
        }
      }
    }

    /*
     * u * func(t) * u# = u * f * u#
     */
    final M c = u.multiply(f);
    final M ut = u.conjugateTranspose();
    f = c.multiply(ut);
    return f;
  }
}
