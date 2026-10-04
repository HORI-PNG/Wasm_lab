/**
 * $Id: DoubleMatrix.java,v 1.28 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.Writer;
import java.nio.charset.Charset;

import org.mklab.nfc.eig.BalancedDecompositionDoubleRealElements;
import org.mklab.nfc.eig.DoubleBalancedDecomposition;
import org.mklab.nfc.eig.DoubleEigenSolution;
import org.mklab.nfc.eig.DoubleHessenbergDecomposition;
import org.mklab.nfc.eig.DoubleQRDecomposition;
import org.mklab.nfc.eig.DoubleQZDecomposition;
import org.mklab.nfc.eig.DoubleRealBalanceDecomposer;
import org.mklab.nfc.eig.DoubleRealEigenSolver;
import org.mklab.nfc.eig.DoubleRealGeneralizedEigenSolver;
import org.mklab.nfc.eig.DoubleRealHessenbergDecomposer;
import org.mklab.nfc.eig.DoubleRealQRDecomposer;
import org.mklab.nfc.eig.DoubleRealQZDecomposer;
import org.mklab.nfc.eig.DoubleRealSchurDecomposer;
import org.mklab.nfc.eig.DoubleSchurDecomposition;
import org.mklab.nfc.eig.EigenSolutionDoubleElements;
import org.mklab.nfc.eig.HessenbergDecompositionDoubleRealElements;
import org.mklab.nfc.eig.QRDecompositionDoubleRealElements;
import org.mklab.nfc.eig.QZDecompositionDoubleRealElements;
import org.mklab.nfc.eig.SchurDecomposition;
import org.mklab.nfc.eig.SchurDecompositionDoubleRealElements;
import org.mklab.nfc.elf.DoubleRealExponentialMatrix;
import org.mklab.nfc.fft.DoubleRealFFTAnalyzer;
import org.mklab.nfc.leq.DoubleLUDecomposition;
import org.mklab.nfc.leq.DoubleLUDecompositionElements;
import org.mklab.nfc.leq.DoubleRealCholeskyDecomposer;
import org.mklab.nfc.leq.DoubleRealGaussianEliminationSolver;
import org.mklab.nfc.leq.DoubleRealLUDecomposer;
import org.mklab.nfc.matx.MatxObject;
import org.mklab.nfc.matx.MxDataHead;
import org.mklab.nfc.scalar.DoubleComplexNumber;
import org.mklab.nfc.scalar.DoubleNumber;
import org.mklab.nfc.scalar.DoubleNumberUtil;
import org.mklab.nfc.svd.DoubleRealSingularValueDecomposer;
import org.mklab.nfc.svd.DoubleSingularValueDecomposition;
import org.mklab.nfc.svd.SingularValueDecompositionDoubleRealElements;


/**
 * 倍精度(double)型の値を成分とする行列を表すクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.28 $
 */
public class DoubleMatrix extends AbstractMatrix<DoubleNumber, DoubleMatrix> implements RealNumericalMatrix<DoubleNumber,DoubleMatrix,DoubleComplexNumber,DoubleComplexMatrix>, MatxObject {

  /** シリアルバージョン。 */
  private static final long serialVersionUID = 7565662802411310550L;

  /** 成分の出力フォーマット。 */
  private static String defaultElementFormat = "%16.8E"; //$NON-NLS-1$

  /** 行列の各成分。 */
  private double[][] elements;

  /**
   * 成分のデフォルト出力フォーマットを設定します。
   * 
   * @param format 成分のデフォルト出力フォーマット
   */
  public static void setDefaultElementFormat(final String format) {
    DoubleMatrix.defaultElementFormat = format;
  }

  /**
   * 成分のデフォルト出力フォーマットを返します。
   * 
   * @return 成分のデフォルト出力フォーマット
   */
  public static String getDefaultElementFormat() {
    return DoubleMatrix.defaultElementFormat;
  }

  /**
   * 0*0の{@link DoubleMatrix}を生成します。
   */
  public DoubleMatrix() {
    this(0, 0);
  }

  /**
   * <code>elements</code>で与えられた成分を持つ行ベクトルを生成します。
   * 
   * @param elements ベクトルの成分をもつ配列
   */
  public DoubleMatrix(final double[] elements) {
    this(elements.length == 0 ? 0 : 1, elements.length, elements.length == 0 ? new double[0][0] : new double[][] {elements});
  }

  /**
   * <code>elements</code>で与えられた成分を持つ行ベクトルを生成します。
   * 
   * @param elements ベクトルの成分をもつ配列
   */
  public DoubleMatrix(final DoubleNumber[] elements) {
    this(elements.length == 0 ? 0 : 1, elements.length, elements.length == 0 ? new DoubleNumber[0][0] : new DoubleNumber[][] {elements});
  }

  /**
   * <code>rowSize&nbsp;*&nbsp;columnSize</code> の{@link DoubleMatrix}零行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   */
  public DoubleMatrix(final int rowSize, final int columnSize) {
    this(rowSize, columnSize, new double[rowSize][columnSize]);
  }

  /**
   * 与えられた成分をもつ{@link DoubleMatrix}を生成します。
   * 
   * @param elements 行列の成分
   */
  public DoubleMatrix(final double[][] elements) {
    this(elements.length, (elements.length == 0 || elements[0] == null) ? 0 : elements[0].length, elements);
  }

  /**
   * 与えられた成分をもつ{@link DoubleMatrix}を生成します。
   * 
   * @param elements 行列の成分
   */
  public DoubleMatrix(final DoubleNumber[][] elements) {
    this(elements.length, (elements.length == 0 || elements[0] == null) ? 0 : elements[0].length, elements);
  }

  /**
   * 与えられた成分をもつ <code>rowSize&nbsp;*&nbsp;columnSize</code> の {@link DoubleMatrix}を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 行列の成分
   */
  public DoubleMatrix(final int rowSize, final int columnSize, final double[][] elements) {
    super(rowSize, columnSize);
    if (elements == null) {
      throw new MatrixSizeException(MatrixSizeException.INCORRECT_SIZE);
    }
    if (rowSize != 0 && columnSize != 0 && rowSize != elements.length) {
      throw new MatrixSizeException(MatrixSizeException.INCORRECT_SIZE);
    }
    if (rowSize != 0 && columnSize != 0 && columnSize != elements[0].length) {
      throw new MatrixSizeException(MatrixSizeException.INCORRECT_SIZE);
    }

    this.elements = elements;
    setElementFormat(DoubleMatrix.defaultElementFormat);
    setElementAlignment(GridElementAlignment.RIGHT);
  }

  /**
   * 与えられた成分をもつ <code>rowSize&nbsp;*&nbsp;columnSize</code> の {@link DoubleMatrix}を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 行列の成分
   */
  public DoubleMatrix(final int rowSize, final int columnSize, final DoubleNumber[][] elements) {
    super(rowSize, columnSize);
    if (elements == null) {
      throw new MatrixSizeException(MatrixSizeException.INCORRECT_SIZE);
    }
    if (rowSize != 0 && columnSize != 0 && rowSize != elements.length) {
      throw new MatrixSizeException(MatrixSizeException.INCORRECT_SIZE);
    }
    if (rowSize != 0 && columnSize != 0 && columnSize != elements[0].length) {
      throw new MatrixSizeException(MatrixSizeException.INCORRECT_SIZE);
    }

    this.elements = DoubleNumberUtil.createDoubleArray(elements);
    setElementFormat(DoubleMatrix.defaultElementFormat);
    setElementAlignment(GridElementAlignment.RIGHT);
  }

  /**
   * 新しく生成された{@link DoubleMatrix}オブジェクトを初期化します。
   * 
   * @param matrix 整数行列
   */
  public DoubleMatrix(final IntMatrix matrix) {
    this(DoubleMatrixUtil.createArray(matrix.getIntElements()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone() {
    final DoubleMatrix ans = (DoubleMatrix)super.clone();
    ans.elements = DoubleMatrixUtil.clone(this.elements);
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object opponent) {
    if (this == opponent) {
      return true;
    }
    if (opponent == null) {
      return false;
    }
    if (opponent.getClass() != getClass()) {
      return false;
    }

    if (isSameSize((DoubleMatrix)opponent) == false) {
      return false;
    }

    return DoubleMatrixUtil.equals(this.elements, ((DoubleMatrix)opponent).elements);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean equals(final DoubleMatrix opponent, final DoubleNumber tolerance) {
    return equals(opponent, tolerance.doubleValue());
  }

  /**
   * 許容誤差内で行列<code>opponent</code>と等しいか判定します。
   * 
   * @param opponent 実行列
   * @param tolerance 許容誤差
   * @return <code>opponent</code>と等しければtrue、そうでなければfalse
   */
  public final boolean equals(final DoubleMatrix opponent, final double tolerance) {
    if (isSameSize(opponent) == false) {
      return false;
    }
    double localTolerance = tolerance;
    if (tolerance < 0) {
      localTolerance = length() * Math.max(norm(NormType.ONE).doubleValue(), 1.0) * DoubleNumberUtil.EPS;
    }

    return DoubleMatrixUtil.equals(this.elements, opponent.elements, localTolerance);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final boolean equals(final DoubleMatrix opponent, final double tolerance) {
  //    if (!(opponent instanceof DoubleMatrix)) {
  //      return false;
  //    }
  //    return equals((DoubleMatrix)opponent, tolerance);
  //  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int hashCode = super.hashCode();
    hashCode = 31 * hashCode + (int)(+serialVersionUID ^ (serialVersionUID >>> 32));
    for (int i0 = 0; this.elements != null && i0 < this.elements.length; i0++) {
      for (int i1 = 0; this.elements != null && i1 < this.elements[0].length; i1++) {
        hashCode = 31 * hashCode + (int)(Double.doubleToLongBits(this.elements[i0][i1]) ^ (Double.doubleToLongBits(this.elements[i0][i1]) >>> 32));
      }
    }
    return hashCode;
  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public final boolean isTransformableFrom(final Matrix<?, ?> value) {
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
//  public final DoubleMatrix transformFrom(final Matrix<?, ?> value) {
//    if (value instanceof IntMatrix) {
//      return new DoubleMatrix((IntMatrix)value);
//    }
//
//    if (super.isTransformableFrom(value)) {
//      return super.transformFrom(value);
//    }
//
//    throw new IllegalArgumentException(Messages.getString("DoubleMatrix.1") + value); //$NON-NLS-1$
//  }

  /**
   * 行列<code>value</code>との和行列を生成します。
   * 
   * @param value 実数行列
   * @return <code>value</code>との和
   */
  @Override
  public final DoubleMatrix add(final DoubleMatrix value) {
    if (isSameSize(value) == false) {
      throw new MatrixSizeException(this, value, MatrixSizeException.NOT_SAME_SIZE);
    }

    return new DoubleMatrix(getRowSize(), getColumnSize(), DoubleMatrixUtil.add(this.elements, value.elements));
  }

  /**
   * 行列<code>value</code>との差行列を生成します。
   * 
   * @param value 実数行列
   * @return <code>value</code>との差
   */
  @Override
  public final DoubleMatrix subtract(final DoubleMatrix value) {
    if (isSameSize(value) == false) {
      throw new MatrixSizeException(this, value, MatrixSizeException.NOT_SAME_SIZE);
    }

    return new DoubleMatrix(getRowSize(), getColumnSize(), DoubleMatrixUtil.subtract(this.elements, value.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix multiply(final int value) {
    return new DoubleMatrix(DoubleMatrixUtil.multiply(this.elements, value));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix multiply(final double value) {
    return new DoubleMatrix(DoubleMatrixUtil.multiply(this.elements, value));
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final Matrix<?,?> multiply(final Scalar<?,?> value) {
  //    if (value instanceof DoubleNumber) {
  //      return multiply(((DoubleNumber)value).doubleValue());
  //    }
  //
  //    final Scalar<?,?>[][] ans = BaseMatrixUtil.<Scalar<?,?>> multiply(this.elements, value);
  //    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  //  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix multiply(final DoubleNumber value) {
    return multiply(value.doubleValue());
  }

  /**
   * 行列<code>value</code>との積行列を生成します。
   * 
   * @param value 実数行列
   * @return <code>value</code>との積
   */
  @Override
  public final DoubleMatrix multiply(final DoubleMatrix value) {
    if (getColumnSize() != value.getRowSize()) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_SIZE);
    }

    final double[][] ans;
    if (isEmpty() || value.isEmpty()) {
      ans = new double[getRowSize()][value.getColumnSize()];
    } else {
      ans = DoubleMatrixUtil.multiply(this.elements, value.elements);
    }

    return new DoubleMatrix(getRowSize(), value.getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix conjugate() {
    return createClone();
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix transpose() {
    return new DoubleMatrix(DoubleMatrixUtil.transpose(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix conjugateTranspose() {
    return new DoubleMatrix(DoubleMatrixUtil.transpose(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber determinant() {
    final Object[] inverseAndDeterminant = new DoubleRealGaussianEliminationSolver().inverse(this.elements, this.frobNorm().doubleValue() * DoubleNumberUtil.EPS, false);
    return new DoubleNumber(((Double)inverseAndDeterminant[1]).doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix inverse(final double tolerance, final boolean stopIfSingular) {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    final Object[] inverseAndDeterminant = new DoubleRealGaussianEliminationSolver().inverse(this.elements, tolerance, stopIfSingular);
    return new DoubleMatrix((double[][])inverseAndDeterminant[0]);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix inverse(final DoubleNumber tolerance, final boolean stopIfSingular) {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    //    if ((tolerance instanceof DoubleNumber) == false) {
    //      throw new IllegalArgumentException();
    //    }

    final Object[] inverseAndDeterminant = new DoubleRealGaussianEliminationSolver().inverse(this.elements, tolerance.doubleValue(), stopIfSingular);
    return new DoubleMatrix((double[][])inverseAndDeterminant[0]);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix inverseElementWise() {
    return new DoubleMatrix(DoubleMatrixUtil.inverseElementWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix divide(final int value) {
    return new DoubleMatrix(DoubleMatrixUtil.divide(this.elements, value));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix divide(final double value) {
    return new DoubleMatrix(DoubleMatrixUtil.divide(this.elements, value));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix divide(final DoubleNumber value) {
    return divide(value.doubleValue());
  }

  /**
   * 行列<code>value</code>の逆行列との積(<code>this</code>*<code>value</code> <sup>-1 </sup>)を生成します。
   * 
   * @param value 実数行列
   * @return <code>value</code>の逆行列との積
   */
  @Override
  public final DoubleMatrix divide(final DoubleMatrix value) {
    return multiply(value.inverse());
  }

  /**
   * 自身の逆行列と行列<code>value</code>の積(<code>this</code> <sup>-1 </sup>*<code>value</code>)を生成します。
   * 
   * @param value 実数行列
   * @return 自身の逆行列と<code>value</code>の積
   */
  @Override
  public final DoubleMatrix leftDivide(final DoubleMatrix value) {
    if (getRowSize() != value.getRowSize()) {
      throw new MatrixSizeException(this, value, Messages.getString("DoubleMatrix.3")); //$NON-NLS-1$
    }

    final double tolerance = this.frobNorm().doubleValue() * DoubleNumberUtil.EPS;

    if (getRowSize() != getColumnSize()) {
      return new DoubleMatrix(new DoubleRealSingularValueDecomposer().leastSquare(this.elements, value.elements, tolerance));
    }

    return new DoubleMatrix(new DoubleRealLUDecomposer().leftDivide(this.elements, value.elements, tolerance, false));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix unaryMinus() {
    return new DoubleMatrix(getRowSize(), getColumnSize(), DoubleMatrixUtil.unaryMinus(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix getSubMatrix(final int rowMin, final int rowMax, final int columnMin, final int columnMax) {
    final double[][] ans = DoubleMatrixUtil.getSubMatrix(this.elements, rowMin - 1, rowMax - 1, columnMin - 1, columnMax - 1);
    return new DoubleMatrix(rowMax - rowMin + 1, columnMax - columnMin + 1, ans);
  }

  /**
   * {@inheritDoc}
   */
  public final void removeRowVectors(final int rowMin, final int rowMax) {
    this.elements = DoubleMatrixUtil.removeRowVectors(this.elements, rowMin - 1, rowMax - 1);
    setRowSize(getRowSize() - (rowMax - rowMin + 1));
  }

  /**
   * {@inheritDoc}
   */
  public final void removeRowVectors(final IntMatrix rowIndex) {
    if (rowIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrix.4")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    final double[][] ans = DoubleMatrixUtil.removeRowVectors(this.elements, index);

    this.elements = ans;
    setRowSize(ans.length);
  }

  /**
   * {@inheritDoc}
   */
  public final void removeColumnVectors(final int columnMin, final int columnMax) {
    this.elements = DoubleMatrixUtil.removeColumnVectors(this.elements, columnMin - 1, columnMax - 1);
    setColumnSize(getColumnSize() - (columnMax - columnMin + 1));
  }

  /**
   * {@inheritDoc}
   */
  public final void removeColumnVectors(final IntMatrix columnIndex) {
    if (columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrix.5")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    final double[][] ans = DoubleMatrixUtil.removeColumnVectors(this.elements, index);

    this.elements = ans;
    int newColumnSize = ans.length == 0 ? 0 : ans[0].length;
    setColumnSize(newColumnSize);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix getSubMatrix(final IntMatrix rowIndex, final int columnMin, final int columnMax) {
    if (rowIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrix.7")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    return new DoubleMatrix(DoubleMatrixUtil.getSubMatrix(this.elements, index, columnMin - 1, columnMax - 1));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix getSubMatrix(final int rowMin, final int rowMax, final IntMatrix columnIndex) {
    if (columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrix.9")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    return new DoubleMatrix(DoubleMatrixUtil.getSubMatrix(this.elements, rowMin - 1, rowMax - 1, index));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix getSubMatrix(final IntMatrix rowIndex, final IntMatrix columnIndex) {
    if (rowIndex.getRowSize() != 1 || columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrix.10")); //$NON-NLS-1$
    }

    final int[] rowIdx = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    final int[] colIdx = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    return new DoubleMatrix(DoubleMatrixUtil.getSubMatrix(this.elements, rowIdx, colIdx));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix getSubVector(final IntMatrix index) {
    if (index.getRowSize() == 0) {
      return new DoubleMatrix(new double[0]);
    }
    if (index.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrix.11")); //$NON-NLS-1$
    }

    final int[] idx = IntMatrixUtil.decrement(index.getIntElements()[0]);

    if (getColumnSize() == 1) {
      final double[] mat = transpose().elements[0];
      return new DoubleMatrix(DoubleMatrixUtil.getSubVector(mat, idx)).transpose();
    }

    if (getRowSize() == 1) {
      return new DoubleMatrix(DoubleMatrixUtil.getSubVector(this.elements[0], idx));
    }

    throw new MatrixSizeException(MatrixSizeException.NOT_A_VECTOR_MATRIX);
  }

  /**
   * <code>row</code>行<code>column</code>列の成分を返します。
   * 
   * @param row 行番号(1から始まる)
   * @param column 列番号(1から始まる)
   * @return row行column列の成分
   */
  public final double getDoubleElement(final int row, final int column) {
    return this.elements[row - 1][column - 1];
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber getElement(final int row, final int column) {
    return new DoubleNumber(this.elements[row - 1][column - 1]);
  }

  /**
   * 成分を行毎に数え、指定した位置の成分を返します。
   * 
   * @param index 成分の番号(1から始まる)
   * 
   * @return 指定した成分
   */
  public final double getDoubleElement(final int index) {
    return this.elements[(index - 1) / getColumnSize()][(index - 1) % getColumnSize()];
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber getElement(final int index) {
    return new DoubleNumber(this.elements[(index - 1) / getColumnSize()][(index - 1) % getColumnSize()]);
  }

  /**
   * 全成分を <code>double</code> の2次元配列として返します。
   * 
   * @return 全成分
   */
  final public double[][] getDoubleElements() {
    return this.elements;
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubMatrix(final IntMatrix rowIndex, final int columnMin, final int columnMax, final Matrix<?, ?> source) {
//    if (source instanceof DoubleMatrix) {
//      setSubMatrix(rowIndex, columnMin, columnMax, (DoubleMatrix)source);
//      return;
//    }
//    if (source instanceof IntMatrix) {
//      setSubMatrix(rowIndex, columnMin, columnMax, new DoubleMatrix((IntMatrix)source));
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("DoubleMatrix.12")); //$NON-NLS-1$
//  }

  /**
   * <code>rowIndex</code>で指定した行の<code>columnMin</code>列から<code>columnMax</code> 列までの行列<code>value</code>を代入します。
   * 
   * @param rowIndex 行番号を指定する指数(1から始まる)
   * @param columnMin 列の始まり
   * @param columnMax 列の終わり
   * @param source 代入する行列
   */
  public final void setSubMatrix(final IntMatrix rowIndex, final int columnMin, final int columnMax, final DoubleMatrix source) {
    if (rowIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrix.13")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    DoubleMatrixUtil.setSubMatrix(this.elements, index, columnMin - 1, columnMax - 1, source.elements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubMatrix(final IntMatrix rowIndex, final IntMatrix columnIndex, final Matrix<?, ?> source) {
//    if (source instanceof IntMatrix) {
//      setSubMatrix(rowIndex, columnIndex, new DoubleMatrix((IntMatrix)source));
//      return;
//    }
//    if (source instanceof DoubleMatrix) {
//      setSubMatrix(rowIndex, columnIndex, (DoubleMatrix)source);
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("DoubleMatrix.14")); //$NON-NLS-1$
//  }

  /**
   * <code>rowIndex</code>で指定した行の<code>columnIndex</code>で指定した列に行列<code>value</code>を代入します。
   * 
   * @param rowIndex 行番号を指定する指数(1から始まる)
   * @param columnIndex 列番号を指定する指数(1から始まる)
   * @param source 代入する行列
   */
  public final void setSubMatrix(final IntMatrix rowIndex, final IntMatrix columnIndex, final DoubleMatrix source) {
    if (rowIndex.getRowSize() != 1 || columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrix.15")); //$NON-NLS-1$
    }

    final int[] rowidx = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    final int[] colidx = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    DoubleMatrixUtil.setSubMatrix(this.elements, rowidx, colidx, source.elements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubMatrix(final int rowMin, final int rowMax, final IntMatrix columnIndex, final Matrix<?, ?> source) {
//    if (source instanceof DoubleMatrix) {
//      setSubMatrix(rowMin, rowMax, columnIndex, (DoubleMatrix)source);
//      return;
//    }
//    if (source instanceof IntMatrix) {
//      setSubMatrix(rowMin, rowMax, columnIndex, new DoubleMatrix((IntMatrix)source));
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("DoubleMatrix.16")); //$NON-NLS-1$
//  }

  /**
   * <code>rowMin</code>列から<code>rowMax</code>列目の成分の<code>columnIndex</code> で指定された行に行列<code>value</code>の成分を代入します。
   * 
   * @param rowMin 行指定
   * @param rowMax 行指定
   * @param columnIndex 列指定ベクトル
   * @param source 行列
   */
  public final void setSubMatrix(final int rowMin, final int rowMax, final IntMatrix columnIndex, final DoubleMatrix source) {
    if (columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrix.17")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    DoubleMatrixUtil.setSubMatrix(this.elements, rowMin - 1, rowMax - 1, index, source.elements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubMatrix(final int rowMin, final int rowMax, final int columnMin, final int columnMax, final Matrix<?, ?> source) {
//    if (source instanceof DoubleMatrix) {
//      setSubMatrix(rowMin, rowMax, columnMin, columnMax, (DoubleMatrix)source);
//      return;
//    }
//    if (source instanceof IntMatrix) {
//      setSubMatrix(rowMin, rowMax, columnMin, columnMax, new DoubleMatrix((IntMatrix)source));
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("DoubleMatrix.19")); //$NON-NLS-1$
//  }

  /**
   * 指定した成分に行列<code>source</code>を代入します。
   * 
   * @param rowMin 開始行番号(1から始まります)
   * @param rowMax 終了行番号(1から始まります)
   * @param columnMin 開始列番号(1から始まります)
   * @param columnMax 終了列番号(1から始まります)
   * @param source 代入する行列
   */
  public final void setSubMatrix(final int rowMin, final int rowMax, final int columnMin, final int columnMax, final DoubleMatrix source) {
    if ((rowMax - rowMin + 1) != source.getRowSize()) {
      throw new MatrixSizeException(Messages.getString("AbstractFundamentalMatrix.2")); //$NON-NLS-1$
    }
    if ((columnMax - columnMin + 1) != source.getColumnSize()) {
      throw new MatrixSizeException(Messages.getString("AbstractFundamentalMatrix.3")); //$NON-NLS-1$
    }

    DoubleMatrixUtil.setSubMatrix(this.elements, rowMin - 1, rowMax - 1, columnMin - 1, columnMax - 1, source.elements);
  }

  /**
   * 成分を行毎に数え、<code>min</code>から<code>max</code>までに<code>source</code>の成分を代入します。
   * 
   * @param min 開始位置(1から始まります)
   * @param max 終了位置(1から始まります)
   * @param source 代入するベクトル
   */
  public final void setSubVector(final int min, final int max, final DoubleMatrix source) {
    if ((max - min + 1) != source.getColumnSize()) {
      throw new MatrixSizeException(Messages.getString("AbstractFundamentalMatrix.2")); //$NON-NLS-1$
    }

    DoubleMatrixUtil.setSubVector(this.elements[0], min - 1, max - 1, source.elements[0]);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubVector(final int min, final int max, final Matrix<?, ?> source) {
//    if (source instanceof DoubleMatrix) {
//      setSubVector(min, max, (DoubleMatrix)source);
//      return;
//    }
//    if (source instanceof IntMatrix) {
//      setSubVector(min, max, new DoubleMatrix((IntMatrix)source));
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("DoubleMatrix.19")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubVector(final IntMatrix index, final Matrix<?, ?> source) {
//    if (source instanceof DoubleMatrix) {
//      setSubVector(index, (DoubleMatrix)source);
//      return;
//    }
//    if (source instanceof IntMatrix) {
//      setSubVector(index, new DoubleMatrix((IntMatrix)source));
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("DoubleMatrix.19")); //$NON-NLS-1$
//  }

  /**
   * <code>index</code>で指定した各成分に行列<code>value</code>の成分を代入します。
   * 
   * @param index 位置をもつベクトル
   * @param source 設定する行列
   */
  public final void setSubVector(final IntMatrix index, final DoubleMatrix source) {
    if (index.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrix.20")); //$NON-NLS-1$
    }

    final int[] idx = IntMatrixUtil.decrement(index.getIntElements()[0]);
    DoubleMatrixUtil.setElements(this.elements, idx, source.elements);
  }

  /**
   * {@inheritDoc}
   */
  public final void setElement(final int row, final int column, final int value) {
    this.elements[row - 1][column - 1] = value;
  }

  /**
   * {@inheritDoc}
   */
  public final void setElement(final int row, final int column, final double value) {
    this.elements[row - 1][column - 1] = value;
  }

  /**
   * {@inheritDoc}
   */
  public final void setElement(final int row, final int column, final DoubleNumber value) {
    setElement(row, column, value.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final void setElement(final int index, final int value) {
    this.elements[(index - 1) / getColumnSize()][(index - 1) % getColumnSize()] = value;
  }

  /**
   * {@inheritDoc}
   */
  public final void setElement(final int index, final DoubleNumber value) {
    setElement(index, value.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final void setElement(final int index, final double value) {
    this.elements[(index - 1) / getColumnSize()][(index - 1) % getColumnSize()] = value;
  }

  /**
   * 右側に<code>value</code>を連結した行列を生成します。
   * 
   * @param value 連結する行列
   * @return 右側に<code>value</code>を連結行列した
   */
  @Override
  public final DoubleMatrix appendRight(final DoubleMatrix value) {
    if (hasSameRowSize(value) == false && this.getRowSize() != 0 && value.getRowSize() != 0) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_ROW_NUMBER);
    }

    final double[][] ans = DoubleMatrixUtil.appendRight(this.elements, value.elements);
    return new DoubleMatrix(getRowSize(), getColumnSize() + value.getColumnSize(), ans);
  }

  /**
   * 下側に実数行列<code>value</code>を連結した行列を生成します。
   * 
   * @param value 連結する実数行列
   * @return 下側に<code>value</code>を連結した行列
   */
  @Override
  public final DoubleMatrix appendDown(final DoubleMatrix value) {
    if (hasSameColumnSize(value) == false && getColumnSize() != 0 && value.getColumnSize() != 0) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_COLUMN_NUMBER);
    }

    final double[][] ans = DoubleMatrixUtil.appendDown(this.elements, value.elements);
    return new DoubleMatrix(getRowSize() + value.getRowSize(), getColumnSize(), ans);
  }

  /**
   * MATフォーマットで指定したファイルに保存します。
   * 
   * @param file ファイル名
   * @exception IOException ファイルに出力できない場合
   * 
   */
  public final void writeMatFormat(final File file) throws IOException {
    try (final Writer output = new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8"))) { //$NON-NLS-1$
      writeMatFormat(output);
    }
  }

  /**
   * MATフォーマットで出力ストリームに出力します。
   * 
   * @param output 出力ストリーム
   * @exception IOException 入出力エラーが発生した場合
   */
  public final void writeMatFormat(final Writer output) throws IOException {
    DoubleMatrixUtil.writeMatFormat(this.elements, output);
  }

  /**
   * CSVフォーマットで指定したファイルに保存します。
   * 
   * @param fileName ファイル名
   * @exception IOException ファイルに出力できない場合
   * 
   */
  public final void writeCsvFormat(final File fileName) throws IOException {
    try (final Writer output = new OutputStreamWriter(new FileOutputStream(fileName), Charset.forName("UTF-8"))) { //$NON-NLS-1$
      writeCsvFormat(output);
    }
  }

  /**
   * CSVフォーマットでライターに出力します。
   * 
   * @param output ライター
   * @exception IOException ライターが発生した場合
   */
  public final void writeCsvFormat(final Writer output) throws IOException {
    DoubleMatrixUtil.writeCsvFormat(this.elements, output, ","); //$NON-NLS-1$
  }

  /**
   * SSVフォーマット(空白で分離)で指定したファイルに保存します。
   * 
   * @param fileName ファイル名
   * @exception IOException ファイルに出力できない場合
   * 
   */
  public final void writeSsvFormat(final File fileName) throws IOException {
    try (final Writer output = new OutputStreamWriter(new FileOutputStream(fileName), Charset.forName("UTF-8"))) { //$NON-NLS-1$
      writeSsvFormat(output);
    }
  }

  /**
   * SSVフォーマット(空白で分離)でライターに出力します。
   * 
   * @param output ライター
   * @exception IOException ライターが発生した場合
   */
  public final void writeSsvFormat(final Writer output) throws IOException {
    DoubleMatrixUtil.writeCsvFormat(this.elements, output, " "); //$NON-NLS-1$
  }

  /**
   * {@link org.mklab.nfc.matx.MatxMatrix#readMatFormat(Reader)}用の中間メソッドです。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param stringTokennizer matファイルから読み込んだ {@link java.io.StreamTokenizer}
   * @return matファイルから読み込み,生成された行列
   * @exception IOException 入出力エラーが発生した場合
   */
  public static DoubleMatrix readMatFormat(final int rowSize, final int columnSize, final StreamTokenizer stringTokennizer) throws IOException {
    return new DoubleMatrix(rowSize, columnSize, DoubleMatrixUtil.readMatFormat(rowSize, columnSize, stringTokennizer));
  }

  /**
   * CSVフォーマットのデータをファイルから読み込んで、行列を生成します。
   * 
   * @param file ファイル
   * @return 読み込んだ行列
   * @throws IOException データを読み込めない場合
   */
  public static DoubleMatrix readCsvFormat(final File file) throws IOException {
    try (final Reader input = new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"))) { //$NON-NLS-1$
      final DoubleMatrix ans = readCsvFormat(input);
      return ans;
    }
  }

  /**
   * CSVフォーマットのデータをファイルから読み込んで、行列を生成します。
   * 
   * @param file ファイル
   * @param indices indices of column to read (starts from 1)
   * @return 読み込んだ行列
   * @throws IOException データを読み込めない場合
   */
  public static DoubleMatrix readCsvFormat(final File file, IntMatrix indices) throws IOException {
    try (final Reader input = new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"))) { //$NON-NLS-1$
      final DoubleMatrix ans = readCsvFormat(input, indices);
      return ans;
    }
  }

  /**
   * CSVフォーマットのデータをリーダーから読み込んで、行列を生成します。
   * 
   * @param input リーダー
   * @return 読み込んだ行列
   * @throws IOException データを読み込めない場合
   */
  public static DoubleMatrix readCsvFormat(final Reader input) throws IOException {
    double[][] ans = DoubleMatrixUtil.readCsvFormat(input);
    return new DoubleMatrix(ans);
  }

  /**
   * CSVフォーマットのデータをリーダーから読み込んで、行列を生成します。
   * 
   * @param input リーダー
   * @param indices indices of column to read (starts from 1)
   * @return 読み込んだ行列
   * @throws IOException データを読み込めない場合
   */
  public static DoubleMatrix readCsvFormat(final Reader input, IntMatrix indices) throws IOException {
    final int[] columnIndices = new int[indices.getColumnSize()];
    for (int i = 0; i < columnIndices.length; i++) {
      columnIndices[i] = indices.getIntElement(i+1) - 1;
    }
    
    final double[][] ans = DoubleMatrixUtil.readCsvFormat(input, columnIndices);
    return new DoubleMatrix(ans);
  }

  /**
   * SSVフォーマット(空白による分離)のデータをファイルから読み込んで、行列を生成します。
   * 
   * @param file ファイル
   * @return 読み込んだ行列
   * @throws IOException データを読み込めない場合
   */
  public static DoubleMatrix readSsvFormat(final File file) throws IOException {
    try (final Reader input = new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"))) { //$NON-NLS-1$
      final DoubleMatrix ans = readSsvFormat(input);
      return ans;
    }
  }

  /**
   * SSVフォーマット(空白による分離)のデータをファイルから読み込んで、行列を生成します。
   * 
   * @param file ファイル
   * @param indices indices of column to read (starts from 1)
   * @return 読み込んだ行列
   * @throws IOException データを読み込めない場合
   */
  public static DoubleMatrix readSsvFormat(final File file, IntMatrix indices) throws IOException {
    try (final Reader input = new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"))) { //$NON-NLS-1$
      final DoubleMatrix ans = readSsvFormat(input, indices);
      return ans;
    }
  }

  /**
   * SSVフォーマット(空白による分離)のデータをリーダーから読み込んで、行列を生成します。
   * 
   * @param input リーダー
   * @return 読み込んだ行列
   * @throws IOException データを読み込めない場合
   */
  public static DoubleMatrix readSsvFormat(final Reader input) throws IOException {
    final double[][] ans = DoubleMatrixUtil.readSsvFormat(input);
    return new DoubleMatrix(ans);
  }

  /**
   * SSVフォーマット(空白による分離)のデータをリーダーから読み込んで、行列を生成します。
   * 
   * @param input リーダー
   * @param indices indices of column to read (starts from 1)
   * @return 読み込んだ行列
   * @throws IOException データを読み込めない場合
   */
  public static DoubleMatrix readSsvFormat(final Reader input, IntMatrix indices) throws IOException {
    final int[] columnIndices = new int[indices.getColumnSize()];
    for (int i = 0; i < columnIndices.length; i++) {
      columnIndices[i] = indices.getIntElement(i+1) - 1;
    }

    final double[][] ans = DoubleMatrixUtil.readSsvFormat(input, columnIndices);
    return new DoubleMatrix(ans);
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMxFormat(final DataOutputStream output, final String name) throws IOException {
    DoubleMatrixUtil.writeMxFormat(this.elements, output, name);
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMxFormat(final File file, final String name) throws IOException {
    try (DataOutputStream output = new DataOutputStream(new FileOutputStream(file))) {
      writeMxFormat(output, name);
    }
  }

  /**
   * {@link org.mklab.nfc.matx.MatxMatrix#readMxFormat(InputStream)} から呼ばれる中間メソッドです。
   * 
   * <p>このメソッドは直接使わず
   * 
   * <blockquote> {@link org.mklab.nfc.matrix.Matrix}A = Matrix. {@link org.mklab.nfc.matx.MatxMatrix#readMxFormat(InputStream)} </blockquote>
   * 
   * の形で使用してください。
   * 
   * @param input 入力ストリーム
   * @param head ヘッダー
   * @return mxファイルから読み込み,生成された行列
   * @exception IOException 入力ストリームから読み込めない場合
   */
  public static DoubleMatrix readMxFormat(final InputStream input, final MxDataHead head) throws IOException {
    return new DoubleMatrix(DoubleMatrixUtil.readMxFormat(input, head));
  }

  /**
   * {@inheritDoc}
   */
  public String toMmString() {
    return DoubleMatrixUtil.toMmString(this.elements, getElementFormat());
  }

  /**
   * {@inheritDoc}
   */
  public final String toMmString(final String format) {
    return DoubleMatrixUtil.toMmString(this.elements, format);
  }

  /**
   * {@inheritDoc}
   */
  public String toString(String format) {
    return DoubleMatrixUtil.toString(getDoubleElements(), format);
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMmFormat(final File file, final String name) throws IOException {
    try (final Writer output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")))) { //$NON-NLS-1$
      writeMmFormat(output, name, true);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMmFormat(final Writer output, final String name, final boolean withNewLine) throws IOException {
    final String newLine = System.getProperty("line.separator"); //$NON-NLS-1$

    final StringBuffer sb = new StringBuffer();
    if (name.length() != 0) {
      sb.append(name);
      sb.append(" = "); //$NON-NLS-1$
      sb.append(newLine);
    }

    sb.append(toMmString());

    if (withNewLine) {
      sb.append(";"); //$NON-NLS-1$
      sb.append(newLine);
      sb.append(newLine);
    }

    output.write(sb.toString());
    output.flush();
  }

  /**
   * 行列<code>matrix</code>と同サイズの零行列を生成します。
   * 
   * @param matrix 行列
   * @return <code>value</code>と同サイズの零行列
   */
  public static DoubleMatrix zero(final Grid matrix) {
    return new DoubleMatrix(matrix.getRowSize(), matrix.getColumnSize());
  }

  /**
   * 行列<code>block</code>の<code>rowNumber</code> * <code>columnNumber</code>倍の零行列を生成します。
   * 
   * @param rowNumber 行方向の倍数
   * @param columnNumber 列方向の倍数
   * @param block 基本となる行列
   * @return <code>block</code>の<code>rowNumber</code> * <code>columnNumber</code>倍の零行列
   */
  public static DoubleMatrix zero(final int rowNumber, final int columnNumber, final Grid block) {
    return new DoubleMatrix(block.getRowSize() * rowNumber, block.getColumnSize() * columnNumber);
  }

  /**
   * <code>size</code>*<code>size</code>の全成分が1である行列を生成します。
   * 
   * @param size サイズ指定
   * @return size*sizeの全成分が1である行列
   */
  public static DoubleMatrix ones(final int size) {
    return ones(size, size);
  }

  /**
   * <code>rowSize</code>*<code>columnSize</code>の全成分が1である行列を生成します。
   * 
   * @param rowSize 行番号の指定
   * @param columnSize 列番号の指定
   * @return <code>rowSize</code>*<code>columnSize</code>の全成分が1である行列
   */
  public static DoubleMatrix ones(final int rowSize, final int columnSize) {
    return new DoubleMatrix(DoubleMatrixUtil.createOnes(rowSize, columnSize));
  }

  /**
   * 行列<code>matrix</code>と同サイズの全成分が1である行列を生成します。
   * 
   * @param matrix 行列
   * @return <code>matrix</code>と同サイズの全成分が1である行列
   */
  public static DoubleMatrix ones(final Grid matrix) {
    return ones(matrix.getRowSize(), matrix.getColumnSize());
  }

  /**
   * 行列<code>block</code>の<code>rowNumber</code> * <code>columnNumber</code>倍の全成分が1である行列を生成します。
   * 
   * @param rowNumber 行方向の倍数
   * @param columnNumber 列方向の倍数
   * @param block 基本となる行列
   * @return <code>block</code>の<code>rowNumber</code> * <code>columnNumber</code>倍の全成分が1である行列
   */
  public static DoubleMatrix ones(final int rowNumber, final int columnNumber, final Grid block) {
    return ones(block.getRowSize() * rowNumber, block.getColumnSize() * columnNumber);
  }

  /**
   * 0以上1より小さい一様分布のランダムな成分を持つ<code>matrix</code>と同じ大きさの実行列を生成します。
   * 
   * @param matrix 行列
   * @return ランダムな成分をもつ実行列
   */
  public static DoubleMatrix uniformRandom(final Grid matrix) {
    return uniformRandom(matrix.getRowSize(), matrix.getColumnSize());
  }

  /**
   * 0以上1より小さい一様分布のランダムな成分を持つ<code>size</code>*<code>size</code>の実行列を生成します。
   * 
   * @param size 次数
   * @return ランダムな成分をもつ実行列
   */
  public static DoubleMatrix uniformRandom(final int size) {
    return uniformRandom(size, size);
  }

  /**
   * 0以上1より小さい一様分布のランダムな成分を持つ<code>rowSize</code>*<code>columnSize</code> の実行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return ランダムな成分をもつ実行列
   */
  public static DoubleMatrix uniformRandom(final int rowSize, final int columnSize) {
    return new DoubleMatrix(rowSize, columnSize, DoubleMatrixUtil.createUniformRandom(rowSize, columnSize));
  }

  /**
   * 一様乱数の種をseedで指定し, その種によって生成される、0以上1未満の一様分布の 乱数成分をもつ<code>rowSize</code>*<code>columnSize</code>の 実行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param seed 一様乱数の種
   * @return ランダムな成分をもつ実行列
   */
  public static DoubleMatrix uniformRandom(final int rowSize, final int columnSize, final long seed) {
    return new DoubleMatrix(rowSize, columnSize, DoubleMatrixUtil.createUniformRandom(rowSize, columnSize, seed));
  }

  /**
   * 0以上1より小さい正規分布のランダムな成分をもつ<code>size</code>*<code>size</code>の実行列を生成します。
   * 
   * @param size 次数
   * @return ランダムな成分をもつ実行列
   */
  public static DoubleMatrix normalRandom(final int size) {
    return normalRandom(size, size);
  }

  /**
   * 0以上1より小さい正規分布のランダムな成分をもつ<code>rowSize</code>*<code>columnSize</code> の実行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return ランダムな成分をもつ実行列
   */
  public static DoubleMatrix normalRandom(final int rowSize, final int columnSize) {
    return new DoubleMatrix(rowSize, columnSize, DoubleMatrixUtil.createNormalRandom(rowSize, columnSize));
  }

  /**
   * 0以上1より小さい正規分布のランダムな成分をもつ<code>matrix</code>と同じ大きさの実行列を生成します。
   * 
   * @param matrix 行列
   * @return ランダムな成分をもつ実行列
   */
  public static DoubleMatrix normalRandom(final Grid matrix) {
    return normalRandom(matrix.getRowSize(), matrix.getColumnSize());
  }

  /**
   * 正規乱数の種を<code>seed</code>で指定し, その種によって生成される、0以上1未満の正規分布の乱数成分を もつ<code>rowSize</code>*<code>columnSize</code>の実行列を得る。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param seed 正規乱数の種
   * @return ランダムな成分をもつ実行列
   */
  public static DoubleMatrix normalRandom(final int rowSize, final int columnSize, final long seed) {
    return new DoubleMatrix(rowSize, columnSize, DoubleMatrixUtil.createNormalRandom(rowSize, columnSize, seed));
  }

  /**
   * <code>from</code>から<code>to</code>までの1飛びの実数を成分に持つ行ベクトルを生成します。
   * 
   * @param from 始点
   * @param to 終点
   * @return 連続する実数を成分とする行ベクトル
   */
  public static DoubleMatrix series(final double from, final double to) {
    if (from <= to) {
      return series(from, to, 1);
    }
    return series(from, to, -1);
  }

  /**
   * <code>from</code>から<code>to</code>までの<code>by</code>飛びの実数を成分に持つ行ベクトルを生成します。
   * 
   * @param from 始点
   * @param to 終点
   * @param by 間隔
   * @return fromからtoまでのby飛びの実数をもつ行ベクトル
   */
  public static DoubleMatrix series(final double from, final double to, final double by) {
    return new DoubleMatrix(DoubleMatrixUtil.series(from, to, by));
  }

  /**
   * 対角行列を生成します。
   * 
   * @param diagonalElements 対角成分
   * @return 対角行列
   */
  public static DoubleMatrix diagonal(final double[] diagonalElements) {
    return new DoubleMatrix(DoubleMatrixUtil.vectorToDiagonal(diagonalElements));
  }

  /**
   * {@inheritDoc}
   */
  public final void printElements(final Writer output) {
    final int maxColumnSize = Integer.MAX_VALUE;
    printElements(output, maxColumnSize);
  }

  /**
   * {@inheritDoc}
   */
  public final void printElements(final Writer output, final int maxColumnSize) {
    DoubleMatrixUtil.print(this.elements, output, getElementFormat(), getElementAlignment(), maxColumnSize);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getGridClassName() {
    return "Matrix"; //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final void exchangeRow(final int row1, final int row2) {
    DoubleMatrixUtil.exchangeRow(this.elements, row1 - 1, row2 - 1);
  }

  /**
   * {@inheritDoc}
   */
  public final void exchangeColumn(final int column1, final int column2) {
    DoubleMatrixUtil.exchangeColumn(this.elements, column1 - 1, column2 - 1);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void copy(final Matrix<?, ?> source) {
//    if (isSameSize(source) == false) {
//      throw new MatrixSizeException(this, source, MatrixSizeException.NOT_SAME_SIZE);
//    }
//    if (source instanceof IntMatrix) {
//      copy((IntMatrix)source);
//      return;
//    }
//    if (source instanceof DoubleMatrix) {
//      copy((DoubleMatrix)source);
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("DoubleMatrix.27")); //$NON-NLS-1$
//  }

  /**
   * 実数行列<code>source</code>の各成分をコピーします。
   * 
   * @param source コピー元の実数行列
   */
  public final void copy(final DoubleMatrix source) {
    if (isSameSize(source) == false) {
      throw new MatrixSizeException(this, source, MatrixSizeException.NOT_SAME_SIZE);
    }

    DoubleMatrixUtil.copy(source.elements, this.elements);
  }

  /**
   * 整数行列<code>source</code>の各成分をコピーします。
   * 
   * @param source コピー元の整数行列
   */
  public final void copy(final IntMatrix source) {
    if (isSameSize(source) == false) {
      throw new MatrixSizeException(this, source, MatrixSizeException.NOT_SAME_SIZE);
    }

    DoubleMatrixUtil.copy(source.getIntElements(), this.elements);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix diagonalToVector() {
    return new DoubleMatrix(DoubleMatrixUtil.diagonalToVector(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix vectorToDiagonal() {
    if (getRowSize() == 1) {
      final double[][] ans = DoubleMatrixUtil.vectorToDiagonal(this.elements[0]);
      return new DoubleMatrix(ans);
    }

    if (getColumnSize() == 1) {
      final double[][] vector = DoubleMatrixUtil.transpose(this.elements);
      final double[][] ans = DoubleMatrixUtil.vectorToDiagonal(vector[0]);
      return new DoubleMatrix(ans);
    }

    throw new MatrixSizeException(MatrixSizeException.INCONSISTENT_SIZE);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix reshape(final int newRowSize, final int newColumnSize) {
    if (getRowSize() * getColumnSize() != newRowSize * newColumnSize) {
      throw new MatrixSizeException(MatrixSizeException.INCONSISTENT_SIZE);
    }
    return new DoubleMatrix(DoubleMatrixUtil.reshape(this.elements, newRowSize, newColumnSize));
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero(final double tolerance) {
    return DoubleMatrixUtil.isZero(this.elements, tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero(final DoubleNumber tolerance) {
    return DoubleMatrixUtil.isZero(this.elements, tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit(final double tolerance) {
    return DoubleMatrixUtil.isUnit(this.elements, tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit(final DoubleNumber tolerance) {
    return DoubleMatrixUtil.isUnit(this.elements, tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix compareElementWise(final String operator, final int value) {
    return compareElementWise(operator, (double)value);
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix compareElementWise(final String operator, final double value) {
    return new BooleanMatrix(DoubleMatrixUtil.compareElements(this.elements, operator, value));
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final BooleanMatrix compareElementWise(final String operator, final Scalar<?,?> value) {
  //    return new BooleanMatrix(DoubleMatrixUtil.compareElements(this.elements, operator, value));
  //  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix compareElementWise(final String operator, final DoubleNumber value) {
    return new BooleanMatrix(DoubleMatrixUtil.compareElements(this.elements, operator, value));
  }

  /**
   * <code>opponent</code>と成分毎に<code>operator</code>で指定された演算子で比較し, {@link BooleanMatrix}で返します。
   * 
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param opponent 比較対象
   * 
   * @return 各成分に比較結果が入った{@link BooleanMatrix}
   */
  public final BooleanMatrix compareElementWise(final String operator, final DoubleMatrix opponent) {
    return new BooleanMatrix(DoubleMatrixUtil.compareElements(this.elements, operator, opponent.elements));
  }

  /**
   * <code>opponent</code>と成分毎に<code>operator</code>で指定された演算子で比較し, {@link BooleanMatrix}で返します。
   * 
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param opponent 比較対象
   * 
   * @return 各成分に比較結果が入った{@link BooleanMatrix}
   */
  public final BooleanMatrix compareElementWise(final String operator, final IntMatrix opponent) {
    return new BooleanMatrix(DoubleMatrixUtil.compareElements(this.elements, operator, opponent.getIntElements()));
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final BooleanMatrix compareElementWise(final String operator, final DoubleMatrix opponent) {
  //
  //    if (opponent instanceof IntMatrix) {
  //      return compareElementWise(operator, (IntMatrix)opponent);
  //    }
  //
  //    if (opponent instanceof DoubleMatrix) {
  //      return compareElementWise(operator, (DoubleMatrix)opponent);
  //    }
  //
  //    if (opponent instanceof BaseMatrix<?, ?>) {
  //      if (operator.equals(".<")) { //$NON-NLS-1$
  //        return ((BaseMatrix<?, ?>)opponent).compareElementWise(".>", this); //$NON-NLS-1$
  //      }
  //      if (operator.equals(".<=")) { //$NON-NLS-1$
  //        return ((BaseMatrix<?, ?>)opponent).compareElementWise(".>=", this); //$NON-NLS-1$
  //      }
  //      if (operator.equals(".>")) { //$NON-NLS-1$
  //        return ((BaseMatrix<?, ?>)opponent).compareElementWise(".<", this); //$NON-NLS-1$
  //      }
  //      if (operator.equals(".>=")) { //$NON-NLS-1$
  //        return ((BaseMatrix<?, ?>)opponent).compareElementWise(".<=", this); //$NON-NLS-1$
  //      }
  //      if (operator.equals(".==")) { //$NON-NLS-1$
  //        return ((BaseMatrix<?, ?>)opponent).compareElementWise(".==", this); //$NON-NLS-1$
  //      }
  //      if (operator.equals(".!=")) { //$NON-NLS-1$
  //        return ((BaseMatrix<?, ?>)opponent).compareElementWise(".!=", this); //$NON-NLS-1$
  //      }
  //    }
  //
  //    throw new IllegalArgumentException(Messages.getString("DoubleMatrix.40")); //$NON-NLS-1$
  //  }

  /**
   * 自身と<code>value</code>の成分毎の積を成分とする行列を生成します。
   * 
   * @param value 実数行列
   * @return 掛け算の結果
   */
  @Override
  public final DoubleMatrix multiplyElementWise(final DoubleMatrix value) {
    if (isSameSize(value) == false) {
      throw new MatrixSizeException(this, value, MatrixSizeException.NOT_SAME_SIZE);
    }
    return new DoubleMatrix(DoubleMatrixUtil.multiplyElementWise(this.elements, value.elements));
  }

  /**
   * 自身と<code>value</code>の成分毎の商を成分とする行列を生成します。
   * 
   * @param value 実数行列
   * @return 割り算の結果
   */
  @Override
  public final DoubleMatrix divideElementWise(final DoubleMatrix value) {
    if (isSameSize(value) == false) {
      throw new MatrixSizeException(this, value, MatrixSizeException.NOT_SAME_SIZE);
    }
    return new DoubleMatrix(DoubleMatrixUtil.divideElementWise(this.elements, value.elements));
  }

  /**
   * 自身と<code>value</code>の成分毎の左からの商を成分とする行列を生成します。
   * 
   * @param value 実数行列
   * @return 割り算の結果
   */
  @Override
  public final DoubleMatrix leftDivideElementWise(final DoubleMatrix value) {
    if (isSameSize(value) == false) {
      throw new MatrixSizeException(this, value, MatrixSizeException.NOT_SAME_SIZE);
    }
    return new DoubleMatrix(DoubleMatrixUtil.leftDivideElementWise(this.elements, value.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix resize(final int newRowSize, final int newColumnSize) {
    this.elements = DoubleMatrixUtil.resize(this.elements, newRowSize, newColumnSize);
    setRowSize(newRowSize);
    setColumnSize(newColumnSize);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix powerElementWise(final int order) {
    return powerElementWise((double)order);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix powerElementWise(final IntMatrix value) {
    if (!this.isSameSize(value)) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrix.41")); //$NON-NLS-1$
    }

    return new DoubleMatrix(DoubleMatrixUtil.powerElementWise(this.elements, value.getIntElements()));
  }

//  /**
//   * 整数の累乗(行列成分毎)を成分とする行列を生成します。
//   * 
//   * @param scalar 累乗の対象
//   * @param matrix 累乗の指数を成分とする行列
//   * @return 累乗の結果
//   */
//  public static DoubleMatrix powerElementWise(final int scalar, final Matrix<?, ?> matrix) {
//    if (matrix instanceof DoubleMatrix) {
//      return DoubleMatrix.powerElementWise((double)scalar, (DoubleMatrix)matrix);
//    }
//    throw new IllegalArgumentException();
//  }

//  /**
//   * 実数の累乗(行列成分毎)を成分とする行列を生成します。
//   * 
//   * @param scalar 実数
//   * @param matrix 累乗の指数を成分とする行列
//   * @return 累乗の結果
//   */
//  public static DoubleMatrix powerElementWise(final double scalar, final Matrix<?, ?> matrix) {
//    if (matrix instanceof IntMatrix) {
//      return DoubleMatrix.powerElementWise(scalar, (IntMatrix)matrix);
//    }
//    if (matrix instanceof DoubleMatrix) {
//      return DoubleMatrix.powerElementWise(scalar, (DoubleMatrix)matrix);
//    }
//    throw new IllegalArgumentException();
//  }

  /**
   * 整数の累乗(行列成分毎)を成分とする行列を生成します。
   * 
   * @param scalar 累乗の対象
   * @param matrix 累乗の指数を成分とする行列
   * @return 累乗の結果
   */
  public static DoubleMatrix powerElementWise(final int scalar, final DoubleMatrix matrix) {
    return powerElementWise((double)scalar, matrix);
  }

  /**
   * 実数の累乗(行列成分毎)を成分とする行列を生成します。
   * 
   * @param scalar 実数
   * @param matrix 累乗の指数を成分とする行列
   * @return 累乗の結果
   */
  public static DoubleMatrix powerElementWise(final double scalar, final DoubleMatrix matrix) {
    return new DoubleMatrix(DoubleMatrixUtil.powerElementWise(scalar, matrix.elements));
  }

  /**
   * 1個の実数について、行列の各成分の累乗を成分とする行列を生成します。
   * 
   * @param scalar 累乗の対象
   * @param matrix 累乗の指数を成分とする行列
   * @return 累乗の結果
   */
  public static DoubleMatrix powerElementWise(final double scalar, final IntMatrix matrix) {
    return new DoubleMatrix(DoubleMatrixUtil.powerElementWise(scalar, matrix.getIntElements()));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix cumulativeSum() {
    return new DoubleMatrix(DoubleMatrixUtil.cumulativeSum(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix cumulativeProduct() {
    return new DoubleMatrix(DoubleMatrixUtil.cumulativeProduct(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix cumulativeSumRowWise() {
    return new DoubleMatrix(DoubleMatrixUtil.cumulativeSumRowWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix cumulativeSumColumnWise() {
    return new DoubleMatrix(DoubleMatrixUtil.cumulativeSumColumnWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix cumulativeProductRowWise() {
    return new DoubleMatrix(DoubleMatrixUtil.cumulativeProductRowWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix cumulativeProductColumnWise() {
    return new DoubleMatrix(DoubleMatrixUtil.cumulativeProductColumnWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix meanColumnWise() {
    return new DoubleMatrix(DoubleMatrixUtil.meanColumnWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix meanRowWise() {
    return new DoubleMatrix(DoubleMatrixUtil.meanRowWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix sumColumnWise() {
    return new DoubleMatrix(DoubleMatrixUtil.sumColumnWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix sumRowWise() {
    return new DoubleMatrix(DoubleMatrixUtil.sumRowWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix productColumnWise() {
    return new DoubleMatrix(DoubleMatrixUtil.productColumnWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix productRowWise() {
    return new DoubleMatrix(DoubleMatrixUtil.productRowWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix addElementWise(final int value) {
    return addElementWise((double)value);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix addElementWise(final double value) {
    return new DoubleMatrix(DoubleMatrixUtil.addElementWise(this.elements, value));
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final Matrix<?,?> addElementWise(final Scalar<?,?> value) {
  //    if (value instanceof DoubleNumber) {
  //      return addElementWise(((DoubleNumber)value).doubleValue());
  //    }
  //
  //    return value.createGrid(getRowSize(), getColumnSize(), this.elements).addElementWise(value);
  //  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix addElementWise(final DoubleNumber value) {
    return addElementWise(value.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix subtractElementWise(final int value) {
    return subtractElementWise((double)value);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix subtractElementWise(final double value) {
    return new DoubleMatrix(DoubleMatrixUtil.subtractElementWise(this.elements, value));
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final Matrix<?,?> subtractElementWise(final Scalar<?,?> value) {
  //    if (value instanceof DoubleNumber) {
  //      return subtractElementWise(((DoubleNumber)value).doubleValue());
  //    }
  //
  //    return value.createGrid(getRowSize(), getColumnSize(), this.elements).subtractElementWise(value);
  //  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix subtractElementWise(final DoubleNumber value) {
    return subtractElementWise(value.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber max() {
    return new DoubleNumber(DoubleMatrixUtil.max(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber min() {
    return new DoubleNumber(DoubleMatrixUtil.min(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber sum() {
    return new DoubleNumber(DoubleMatrixUtil.sum(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber product() {
    return new DoubleNumber(DoubleMatrixUtil.product(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber mean() {
    return new DoubleNumber(DoubleMatrixUtil.mean(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber median() {
    return new DoubleNumber(DoubleMatrixUtil.median(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber variance() {
    return new DoubleNumber(DoubleMatrixUtil.variance(this.elements));
  }

  /**
   * yとの共分散行列を返します。
   * 
   * @param opponent 対となるベクトル
   * @return 共分散行列 (Covariance)
   */
  @Override
  public final DoubleMatrix covariance(final DoubleMatrix opponent) {
    return new DoubleMatrix(DoubleMatrixUtil.covariance(this.elements, opponent.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber std() {
    return new DoubleNumber(DoubleMatrixUtil.std(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber trace() {
    return new DoubleNumber(DoubleMatrixUtil.trace(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix isFiniteElementWise() {
    return elementWiseFunction(new IsFiniteFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.28 $, 2006/08/31
   */
  static class IsFiniteFunction implements BooleanFunction {

    /**
     * {@inheritDoc}
     */
    public boolean evaluate(final double value) {
      return DoubleNumberUtil.isFinite(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix isInfiniteElementWise() {
    return elementWiseFunction(new IsInfiniteFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.28 $, 2006/08/31
   */
  static class IsInfiniteFunction implements BooleanFunction {

    /**
     * {@inheritDoc}
     */
    public boolean evaluate(final double value) {
      return Double.isInfinite(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix isNanElementWise() {
    return elementWiseFunction(new IsNanFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.28 $, 2006/08/31
   */
  static class IsNanFunction implements BooleanFunction {

    /**
     * {@inheritDoc}
     */
    public boolean evaluate(final double value) {
      return Double.isNaN(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix floorElementWise() {
    return elementWiseFunction(new FloorFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.28 $, 2006/08/31
   */
  static class FloorFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return Math.floor(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix ceilElementWise() {
    return elementWiseFunction(new CeilFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.28 $, 2006/08/31
   */
  static class CeilFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return Math.ceil(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix fixElementWise() {
    return elementWiseFunction(new FixFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.28 $, 2006/08/31
   */
  static class FixFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return DoubleNumberUtil.fix(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix roundElementWise() {
    return elementWiseFunction(new RoundFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.28 $, 2006/08/31
   */
  static class RoundFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return Math.rint(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix roundToZeroElementWise(final double tolerance) {
    return elementWiseFunction(tolerance, new RoundToZeroFunction());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleMatrix roundToZeroElementWise() {
    return roundToZeroElementWise(DoubleNumberUtil.EPS);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix roundToZeroElementWise(final DoubleNumber tolerance) {
    return roundToZeroElementWise(tolerance.doubleValue());
  }

  /**
   * 実数をゼロ方向に丸めるためのクラスです。
   * 
   * @author koga
   */
  static class RoundToZeroFunction implements DoubleRealFunctionWithTwoArguments {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value, final double tolerance) {
      return DoubleNumberUtil.roundToZero(value, tolerance);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix sqrtElementWise() {
    return elementWiseFunction(new SqrtFunction());
  }

  /**
   * 平方根を計算する実数関数を定義するためのクラスです。
   * 
   * @author koga
   */
  static class SqrtFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return Math.sqrt(value);
    }
  }

  /**
   * 各成分の剰余関数の結果を成分とする行列を生成します。
   * 
   * @param value 割る数
   * @return 成分剰余関数行列
   */
  public final DoubleMatrix remainderElementWise(final double value) {
    return elementWiseFunction(value, new RemainderFunction());
  }

  /**
   * 各成分の剰余関数の結果を成分とする行列を生成します。
   * 
   * @param matrix 割る数を成分とする行列
   * @return 成分剰余関数行列
   */
  public final DoubleMatrix remainderElementWise(final DoubleMatrix matrix) {
    return elementWiseFunction(matrix, new RemainderFunction());
  }

  /**
   * 各成分の剰余関数の結果を成分とする行列を生成します。
   * 
   * @param matrix 割る数を成分とする行列
   * @return 成分剰余関数行列
   */
  public final DoubleMatrix remainderElementWise(final IntMatrix matrix) {
    return elementWiseFunction(new DoubleMatrix(matrix), new RemainderFunction());
  }

  /**
   * 剰余関数を計算する実数関数を定義するためのクラスです。
   * 
   * @author koga
   */
  static class RemainderFunction implements DoubleRealFunctionWithTwoArguments {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value1, final double value2) {
      return DoubleNumberUtil.remainder(value1, value2);
    }
  }

  /**
   * 各成分の符合付剰余関数の結果を成分とする行列を生成します。
   * 
   * @param value 割る数
   * @return 成分符号付剰余関数行列
   */
  public final DoubleMatrix modulusElementWise(final double value) {
    return elementWiseFunction(value, new ModulusFunction());
  }

  /**
   * 各成分の符号付剰余関数の結果を成分とする行列を生成します。
   * 
   * @param matrix 割る数を成分とする行列
   * @return 成分符号付剰余関数行列
   */
  public final DoubleMatrix modulusElementWise(final DoubleMatrix matrix) {
    return elementWiseFunction(matrix, new ModulusFunction());
  }

  /**
   * 各成分の符号付剰余関数の結果を成分とする行列を生成します。
   * 
   * @param matrix 割る数を成分とする行列
   * @return 成分符号付剰余関数行列
   */
  public final DoubleMatrix modulusElementWise(final IntMatrix matrix) {
    return elementWiseFunction(new DoubleMatrix(matrix), new ModulusFunction());
  }

  /**
   * 符合付剰余関数を計算する実数関数を定義するためのクラスです。
   * 
   * @author koga
   */
  static class ModulusFunction implements DoubleRealFunctionWithTwoArguments {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value1, final double value2) {
      return DoubleNumberUtil.modulus(value1, value2);
    }
  }

  /**
   * 各成分の正接(2)関数の結果を成分とする行列を生成します。
   * 
   * @param matrix 分母側の数を成分とする行列
   * @return 成分毎正接(2)関数行列
   */
  public final DoubleMatrix atan2ElementWise(final DoubleMatrix matrix) {
    return elementWiseFunction(matrix, new Atan2Function());
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final DoubleMatrix atan2ElementWise(final DoubleMatrix order) {
  //    if (order instanceof DoubleMatrix) {
  //      return atan2ElementWise((DoubleMatrix)order);
  //    }
  //
  //    throw new IllegalArgumentException();
  //  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix atan2ElementWise(final DoubleNumber value) {
    return elementWiseFunction(value.doubleValue(), new Atan2Function());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix remainderElementWise(final DoubleNumber value) {
    return elementWiseFunction(value.doubleValue(), new RemainderFunction());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix modulusElementWise(final DoubleNumber value) {
    return elementWiseFunction(value.doubleValue(), new ModulusFunction());
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleMatrix atan2ElementWise(final IntMatrix order) {
//    return atan2ElementWise((new DoubleMatrix(order)));
//  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final DoubleMatrix remainderElementWise(final DoubleMatrix order) {
  //    if (order instanceof DoubleMatrix) {
  //      return remainderElementWise((DoubleMatrix)order);
  //    }
  //
  //    throw new IllegalArgumentException();
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final DoubleMatrix modulusElementWise(final DoubleMatrix order) {
  //    if (order instanceof DoubleMatrix) {
  //      return modulusElementWise((DoubleMatrix)order);
  //    }
  //
  //    throw new IllegalArgumentException();
  //  }

  /**
   * 逆正接(2)関数を計算する実数関数を定義するためのクラスです。
   * 
   * @author koga
   */
  static class Atan2Function implements DoubleRealFunctionWithTwoArguments {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value1, final double value2) {
      return Math.atan2(value1, value2);
    }
  }

  /**
   * 各成分について実数関数の評価値を成分とする行列を生成します。
   * 
   * @param function 実数関数
   * @return 関数の評価値を成分とする行列
   */
  private DoubleMatrix elementWiseFunction(final DoubleRealFunction function) {
    return new DoubleMatrix(DoubleMatrixUtil.elementWiseFunction(this.elements, function));
  }

  /**
   * 各成分について実数関数の評価値を成分とする行列を生成します。
   * 
   * @param matrix 第二引数を成分とする行列
   * @param function 引数を2個もつ実数関数
   * @return 関数の評価値を成分とする行列
   */
  private DoubleMatrix elementWiseFunction(final DoubleMatrix matrix, final DoubleRealFunctionWithTwoArguments function) {
    return new DoubleMatrix(DoubleMatrixUtil.elementWiseFunction(this.elements, matrix.elements, function));
  }

  /**
   * 各成分について実数関数の評価値を成分とする行列生成します。
   * 
   * @param value 第二引数(共通)
   * @param function 引数を2個もつ実数関数
   * @return 関数の評価値を成分とする行列
   */
  private DoubleMatrix elementWiseFunction(final double value, final DoubleRealFunctionWithTwoArguments function) {
    return new DoubleMatrix(DoubleMatrixUtil.elementWiseFunction(this.elements, value, function));
  }

  /**
   * 各成分についてboolean関数の評価値を成分とする行列を生成します。
   * 
   * @param function boolean関数
   * @return 関数の評価値を成分とする行列
   */
  private BooleanMatrix elementWiseFunction(final BooleanFunction function) {
    return new BooleanMatrix(DoubleMatrixUtil.elementWiseFunction(this.elements, function));
  }

  /**
   * *************************************************************************** **
   */
  /* 以下は NumericalMatrix の実装メソッドです。 */
  /**
   * *************************************************************************** **
   */

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix eigenValue() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    final double[][] values = new DoubleRealEigenSolver().getEigenValue(getDoubleElements());
    return new DoubleComplexMatrix(DoubleMatrixUtil.transpose(values[0]), DoubleMatrixUtil.transpose(values[1]));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix eigenVector() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    final double[][][] vectors = new DoubleRealEigenSolver().getEigenVector(this.elements);
    final double[][] vectorsReal = vectors[0];
    final double[][] vectorsImag = vectors[1];
    return new DoubleComplexMatrix(getRowSize(), getColumnSize(), vectorsReal, vectorsImag);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleEigenSolution eigenDecompose() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    final EigenSolutionDoubleElements eig = new DoubleRealEigenSolver().solve(this.elements);

    double[] reValue = eig.getReValue();
    double[] imValue = eig.getImValue();

    final double[][] valuesReal = new double[getRowSize()][getColumnSize()];
    final double[][] valuesImag = new double[getRowSize()][getColumnSize()];

    for (int i = 0; i < getRowSize(); i++) {
      valuesReal[i][i] = reValue[i];
      valuesImag[i][i] = imValue[i];
    }

    final DoubleMatrix realValues = new DoubleMatrix(valuesReal);
    final DoubleMatrix imagValues = new DoubleMatrix(valuesImag);

    final DoubleMatrix realVectors = new DoubleMatrix(eig.getReVector());
    final DoubleMatrix imagVectors = new DoubleMatrix(eig.getImVector());

    return new DoubleEigenSolution(realValues, imagValues, realVectors, imagVectors);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final DoubleComplexMatrix eigenValue(final NumericalMatrix<?,?> b) {
  //    if (b instanceof DoubleMatrix) {
  //      return eigenValueReal((DoubleMatrix)b);
  //    }
  //    if (b instanceof DoubleComplexMatrix) {
  //      return new DoubleComplexMatrix(this).eigenValue(b);
  //    }
  //
  //    throw new IllegalArgumentException(Messages.getString("DoubleMatrix.46")); //$NON-NLS-1$
  //  }

  /**
   * 一般化固有値を対角成分とする対角行列 D と対応する一般化固有ベクトルを列とする行列 X を成分とする配列を返します。
   * 
   * @param b 一般化固有分解を求める対となる行列
   * @return 第1成分D,第2成分Xとする {@link org.mklab.nfc.matrix.Matrix}の配列
   */
  public final DoubleComplexMatrix eigenValue(final DoubleMatrix b) {
    if (isSquare() == false || b.isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    if (getRowSize() != b.getRowSize()) {
      throw new MatrixSizeException(this, b, MatrixSizeException.INCORRECT_SIZE);
    }

    final double[][] values = new DoubleRealGeneralizedEigenSolver().getEigenValue(this.elements, b.elements);
    return new DoubleComplexMatrix(values[0], values[1]).transpose();
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final DoubleComplexMatrix eigenVector(final DoubleMatrix b) {
  ////    if (b instanceof DoubleMatrix) {
  //      return eigenVector((DoubleMatrix)b);
  ////    }
  ////    if (b instanceof DoubleComplexMatrix) {
  ////      return new DoubleComplexMatrix(this).eigenVector(b);
  ////    }
  ////    throw new IllegalArgumentException(Messages.getString("DoubleMatrix.47")); //$NON-NLS-1$
  //  }

  /**
   * 一般化固有ベクトルを返します。
   * 
   * @param b 一般化固有ベクトルを求める対となる行列
   * @return 一般化固有ベクトル
   */
  public final DoubleComplexMatrix eigenVector(final DoubleMatrix b) {
    if (isSquare() == false || b.isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    if (getRowSize() != b.getRowSize()) {
      throw new MatrixSizeException(this, b, MatrixSizeException.INCORRECT_SIZE);
    }

    final double[][][] vectors = new DoubleRealGeneralizedEigenSolver().getEigenVector(this.elements, b.elements);
    return new DoubleComplexMatrix(vectors[0], vectors[1]);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final EigenSolution<DoubleNumber,DoubleMatrix> eigenDecompose(final NumericalMatrix<?,?> b) {
  //    if (b instanceof DoubleMatrix) {
  //      return eigenDecompose((DoubleMatrix)b);
  //    }
  //    if (b instanceof DoubleComplexMatrix) {
  //      return new DoubleComplexMatrix(this).eigenDecompose((DoubleComplexMatrix)b);
  //    }
  //
  //    throw new IllegalArgumentException(Messages.getString("DoubleMatrix.48")); //$NON-NLS-1$
  //  }

  /**
   * 一般化固有値を対角成分とする対角行列 D と対応する一般化固有ベクトルを列とする行列 X を成分とする配列を返します。
   * 
   * @param b 一般化固有分解を求める対となる行列
   * @return 第1成分D,第2成分Xとする {@link org.mklab.nfc.matrix.Matrix}
   */
  public final DoubleEigenSolution eigenDecompose(final DoubleMatrix b) {
    if (isSquare() == false || b.isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    if (hasSameRowSize(b) == false) {
      throw new MatrixSizeException(this, b, MatrixSizeException.INCORRECT_SIZE);
    }

    final EigenSolutionDoubleElements eig = new DoubleRealGeneralizedEigenSolver().solve(this.elements, b.elements);
    double[] reValue = eig.getReValue();
    double[] imValue = eig.getImValue();

    final double[][] valuesReal = new double[getRowSize()][getColumnSize()];
    final double[][] valuesImag = new double[getRowSize()][getColumnSize()];

    for (int i = 0; i < getRowSize(); i++) {
      valuesReal[i][i] = reValue[i];
      valuesImag[i][i] = imValue[i];
    }

    final DoubleMatrix realValues = new DoubleMatrix(valuesReal);
    final DoubleMatrix imagValues = new DoubleMatrix(valuesImag);

    final DoubleMatrix realVectors = new DoubleMatrix(eig.getReVector());
    final DoubleMatrix imagVectors = new DoubleMatrix(eig.getImVector());

    return new DoubleEigenSolution(realValues, imagValues, realVectors, imagVectors);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleQRDecomposition qrDecompose() {
    final QRDecompositionDoubleRealElements qr = new DoubleRealQRDecomposer().decompose(this.elements);
    final DoubleMatrix q = new DoubleMatrix(qr.getQ());
    final DoubleMatrix r = new DoubleMatrix(qr.getR());
    return new DoubleQRDecomposition(q, r);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleQRDecomposition qrDecomposeWithPermutation() {
    final QRDecompositionDoubleRealElements qr = new DoubleRealQRDecomposer().decomposeWithPermutation(this.elements);
    final DoubleMatrix q = new DoubleMatrix(qr.getQ());
    final DoubleMatrix r = new DoubleMatrix(qr.getR());
    final IntMatrix p = new IntMatrix(qr.getP());
    return new DoubleQRDecomposition(q, r, p);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final QZDecomposition<DoubleComplexMatrix> qzDecompose(final DoubleMatrix b) {
  ////    if (b instanceof DoubleMatrix) {
  //      return qzDecompose(b);
  ////    }
  ////    if (b instanceof DoubleComplexMatrix) {
  ////      return new DoubleComplexMatrix(this).qzDecompose(b);
  ////    }
  //
  //    throw new IllegalArgumentException(Messages.getString("DoubleMatrix.49")); //$NON-NLS-1$
  //  }

  /**
   * {@inheritDoc}
   */
  public final DoubleQZDecomposition qzDecompose(final DoubleMatrix b) {
    final QZDecompositionDoubleRealElements qz = new DoubleRealQZDecomposer().decompose(this.elements, b.elements);
    final DoubleMatrix aa = new DoubleMatrix(qz.getAA());
    final DoubleMatrix bb = new DoubleMatrix(qz.getBB());
    final DoubleMatrix q = new DoubleMatrix(qz.getQ());
    final DoubleMatrix z = new DoubleMatrix(qz.getZ());
    final DoubleComplexMatrix x = new DoubleComplexMatrix(qz.getReX(), qz.getImX());
    return new DoubleQZDecomposition(aa, bb, q, z, x);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleSchurDecomposition schurDecompose() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    final SchurDecompositionDoubleRealElements ut = new DoubleRealSchurDecomposer().decompose(this.elements);
    final DoubleMatrix u = new DoubleMatrix(getRowSize(), getColumnSize(), ut.getU());
    final DoubleMatrix t = new DoubleMatrix(getRowSize(), getColumnSize(), ut.getT());

    return new DoubleSchurDecomposition(u, t);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix choleskyDecompose() {
    final double tolerance = frobNorm().doubleValue() * DoubleNumberUtil.EPS;
    return choleskyDecompose(tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix choleskyDecompose(double tolerance) {
    return new DoubleRealCholeskyDecomposer().decompose(this, tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix choleskyDecompose(DoubleNumber tolerance) {
    return choleskyDecompose(tolerance.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleBalancedDecomposition balancedDecompose() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    final BalancedDecompositionDoubleRealElements db = new DoubleRealBalanceDecomposer().decompose(this.elements);

    final DoubleMatrix d = new DoubleMatrix(getRowSize(), getColumnSize(), db.getD());
    final DoubleMatrix b = new DoubleMatrix(getRowSize(), getColumnSize(), db.getB());
    return new DoubleBalancedDecomposition(d, b);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix kernel() {
    return kernel(this.frobNorm().doubleValue() * DoubleNumberUtil.EPS);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix kernel(final double tolerance) {
    return new DoubleMatrix(new DoubleRealSingularValueDecomposer().kernel(this.elements, tolerance));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix kernel(final DoubleNumber tolerance) {
    return new DoubleMatrix(new DoubleRealSingularValueDecomposer().kernel(this.elements, tolerance.doubleValue()));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleHessenbergDecomposition hessenbergDecompose() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    final HessenbergDecompositionDoubleRealElements qh = new DoubleRealHessenbergDecomposer().decompose(this.elements);

    final DoubleMatrix q = new DoubleMatrix(getRowSize(), getColumnSize(), qh.getQ());
    final DoubleMatrix h = new DoubleMatrix(getRowSize(), getColumnSize(), qh.getH());

    return new DoubleHessenbergDecomposition(q, h);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleLUDecomposition luDecompose(final boolean stopIfSingular) {
    return luDecompose(this.frobNorm().doubleValue() * DoubleNumberUtil.EPS, stopIfSingular);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleLUDecomposition luDecomposeWithPermutation(final boolean stopIfSingular) {
    return luDecomposeWithPermutation(this.frobNorm().doubleValue() * DoubleNumberUtil.EPS, stopIfSingular);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleLUDecomposition luDecompose(final double tolerance, final boolean stopIfSingular) {
    final DoubleLUDecompositionElements lu = new DoubleRealLUDecomposer().decompose(this.elements, tolerance, stopIfSingular);

    double[][] ll = lu.getL();
    double[][] uu = lu.getU();

    final DoubleMatrix l = new DoubleMatrix(getRowSize(), getColumnSize(), ll);
    final DoubleMatrix u = new DoubleMatrix(getRowSize(), getColumnSize(), uu);

    return new DoubleLUDecomposition(l, u);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleLUDecomposition luDecompose(final DoubleNumber tolerance, final boolean stopIfSingular) {
    return luDecompose(tolerance.doubleValue(), stopIfSingular);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleLUDecomposition luDecomposeWithPermutation(final double tolerance, final boolean stopIfSingular) {
    final DoubleLUDecompositionElements lup = new DoubleRealLUDecomposer().decomposeWithPermutation(this.elements, tolerance, stopIfSingular);

    double[][] ll = lup.getL();
    double[][] uu = lup.getU();
    int[][] pp = lup.getP();

    final DoubleMatrix l = new DoubleMatrix(getRowSize(), getColumnSize(), ll);
    final DoubleMatrix u = new DoubleMatrix(getRowSize(), getColumnSize(), uu);
    final IntMatrix p = new IntMatrix(getRowSize(), getColumnSize(), pp);

    return new DoubleLUDecomposition(l, u, p);
  }

  /**
   * {@inheritDoc}
   */
  public final  DoubleLUDecomposition luDecomposeWithPermutation(final DoubleNumber tolerance, final boolean stopIfSingular) {
    return luDecomposeWithPermutation(tolerance.doubleValue(), stopIfSingular);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix pseudoInverse() {
    return pseudoInverse(this.frobNorm().doubleValue() * DoubleNumberUtil.EPS);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix pseudoInverse(final double tolerance) {
    return new DoubleMatrix(new DoubleRealSingularValueDecomposer().pseudoInverse(this.elements, tolerance));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix pseudoInverse(final DoubleNumber tolerance) {
    return pseudoInverse(tolerance.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final int rank() {
    return rank(this.frobNorm().doubleValue() * DoubleNumberUtil.EPS);
  }

  /**
   * {@inheritDoc}
   */
  public final int rank(final double tolerance) {
    return new DoubleRealSingularValueDecomposer().rank(this.elements, tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public final int rank(final DoubleNumber tolerance) {
    return rank(tolerance.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isFullRank() {
    return isFullRank(this.frobNorm().doubleValue() * DoubleNumberUtil.EPS);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isFullRank(final double tolerance) {
    return new DoubleRealSingularValueDecomposer().isFullRank(this.elements, tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isFullRank(final DoubleNumber tolerance) {
    return isFullRank(tolerance.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleSingularValueDecomposition singularValueDecompose() {
    final SingularValueDecompositionDoubleRealElements udv = new DoubleRealSingularValueDecomposer().decompose(this.elements);

    final DoubleMatrix u = new DoubleMatrix(udv.getU());
    final DoubleMatrix d = new DoubleMatrix(udv.getD());
    final DoubleMatrix v = new DoubleMatrix(udv.getV());
    return new DoubleSingularValueDecomposition(u, d, v);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix singularValue() {
    final double[] values = new DoubleRealSingularValueDecomposer().singularValue(this.elements);
    return new DoubleMatrix(values).transpose();
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber maxSingularValue() {
    return new DoubleNumber(new DoubleRealSingularValueDecomposer().maximumSingularValue(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber minSingularValue() {
    return new DoubleNumber(new DoubleRealSingularValueDecomposer().minimumSingularValue(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber conditionNumber() {
    return new DoubleNumber(new DoubleRealSingularValueDecomposer().conditionNumber(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix fft() {
    int dataSize = Math.max(getRowSize(), getColumnSize());
    dataSize = (int)Math.pow(2, Math.ceil(Math.log(dataSize) / Math.log(2)));
    return fft(dataSize);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix fft(final int dataSize) {
    if (getRowSize() == 1) {
      final double[][] ans = DoubleRealFFTAnalyzer.fft(this.elements[0], dataSize);
      return new DoubleComplexMatrix(ans[0], ans[1]);
    }

    if (getColumnSize() == 1) {
      return (this.transpose()).fft(dataSize).transpose();
    }

    throw new MatrixSizeException(Messages.getString("DoubleMatrix.57")); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix fftRowWise() {
    int dataSize = getColumnSize();
    dataSize = (int)Math.pow(2, Math.ceil(Math.log(dataSize) / Math.log(2)));
    return fftRowWise(dataSize);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix fftRowWise(final int dataSize) {
    final double[][] ansReal = new double[getRowSize()][getColumnSize()];
    final double[][] ansImag = new double[getRowSize()][getColumnSize()];
    double[][] tmp;
    for (int i = 0; i < getRowSize(); i++) {
      tmp = DoubleRealFFTAnalyzer.fft(this.elements[i], dataSize);
      ansReal[i] = tmp[0];
      ansImag[i] = tmp[1];
    }
    return new DoubleComplexMatrix(ansReal, ansImag);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix fftColumnWise() {
    int dataSize = getRowSize();
    dataSize = (int)Math.pow(2, Math.ceil(Math.log(dataSize) / Math.log(2)));
    return fftColumnWise(dataSize);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix fftColumnWise(final int dataSize) {
    return (this.transpose()).fftRowWise(dataSize).transpose();
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix ifft() {
    int dataSize = Math.max(getRowSize(), getColumnSize());
    dataSize = (int)Math.pow(2, Math.ceil(Math.log(dataSize) / Math.log(2)));
    return ifft(dataSize);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix ifft(final int dataSize) {
    if (getRowSize() == 1) {
      final double[][] ans = DoubleRealFFTAnalyzer.ifft(this.elements[0], dataSize);
      return new DoubleComplexMatrix(ans[0], ans[1]);
    }

    if (getColumnSize() == 1) {
      return (this.transpose()).ifft(dataSize).transpose();
    }

    throw new MatrixSizeException(Messages.getString("DoubleMatrix.60")); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix ifftRowWise() {
    int dataSize = getColumnSize();
    dataSize = (int)Math.pow(2, Math.ceil(Math.log(dataSize) / Math.log(2)));
    return ifftRowWise(dataSize);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix ifftRowWise(final int dataSize) {
    final double[][] ansReal = new double[getRowSize()][getColumnSize()];
    final double[][] ansImag = new double[getRowSize()][getColumnSize()];
    double[][] tmp;

    for (int i = 0; i < getRowSize(); i++) {
      tmp = DoubleRealFFTAnalyzer.ifft(this.elements[i], dataSize);
      ansReal[i] = tmp[0];
      ansImag[i] = tmp[1];
    }
    return new DoubleComplexMatrix(ansReal, ansImag);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix ifftColumnWise() {
    int dataSize = getRowSize();
    dataSize = (int)Math.pow(2, Math.ceil(Math.log(dataSize) / Math.log(2)));
    return ifftColumnWise(dataSize);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix ifftColumnWise(final int dataSize) {
    return (this.transpose()).ifftRowWise(dataSize).transpose();
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix log() {
    return matrixFunction(new LogFunction());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix sqrt() {
    return new DoubleComplexMatrix(this).sqrt();
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix exp() {
    return exp(DoubleNumberUtil.EPS * this.frobNorm().doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix exp(final double tolerance) {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    return DoubleRealExponentialMatrix.exp(this, tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix exp(final DoubleNumber tolerance) {
    return exp(tolerance.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix sinElementWise() {
    return elementWiseFunction(new SinFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.28 $, 2006/08/31
   */
  static class SinFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return Math.sin(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix sinhElementWise() {
    return elementWiseFunction(new SinhFunction());
  }

  /**
   * 双曲線正弦関数を計算する実数関数を定義するためのクラスです。
   * 
   * @author koga
   * @version $Revision: 1.28 $, 2006/08/31
   */
  static class SinhFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return Math.sinh(value);
      //return DoubleNumberUtil.sinh(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix asinElementWise() {
    return elementWiseFunction(new AsinFunction());
  }

  /**
   * 逆正弦関数を計算する実数関数を定義するためのクラスです。
   * 
   * @author koga
   */
  static class AsinFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return Math.asin(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix asinhElementWise() {
    return elementWiseFunction(new AsinhFunction());
  }

  /**
   * 逆双曲線正弦関数を計算する実数関数を定義するためのクラスです。
   * 
   * @author koga
   */
  static class AsinhFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return DoubleNumberUtil.asinh(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix cosElementWise() {
    return elementWiseFunction(new CosFunction());
  }

  /**
   * 余弦関数を計算する実数関数を定義するためのクラスです。
   * 
   * @author koga
   */
  static class CosFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return Math.cos(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix coshElementWise() {
    return elementWiseFunction(new CoshFunction());
  }

  /**
   * 双曲線余弦関数を計算する実数関数を定義するためのクラスです。
   * 
   * @author koga
   */
  static class CoshFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return Math.cosh(value);
      //return DoubleNumberUtil.cosh(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix acosElementWise() {
    return elementWiseFunction(new AcosFunction());
  }

  /**
   * 逆余弦関数を計算する実数関数を定義するためのクラスです。
   * 
   * @author koga
   */
  static class AcosFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return Math.acos(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix acoshElementWise() {
    return elementWiseFunction(new AcoshFunction());
  }

  /**
   * 逆双曲線余弦関数を計算する実数関数を定義するためのクラスです。
   * 
   * @author koga
   */
  static class AcoshFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return DoubleNumberUtil.acosh(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix tanElementWise() {
    return elementWiseFunction(new TanFunction());
  }

  /**
   * 正接関数を計算する実数関数を定義するためクラスです。
   * 
   * @author koga
   */
  static class TanFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return Math.tan(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix tanhElementWise() {
    return elementWiseFunction(new TanhFunction());
  }

  /**
   * 双曲線正接関数を計算する実数関数を定義するためのクラスです。
   * 
   * @author koga
   */
  static class TanhFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return Math.tanh(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix atanElementWise() {
    return elementWiseFunction(new AtanFunction());
  }

  /**
   * 逆正接関数を計算する実数関数を定義するためのクラスです。
   * 
   * @author koga
   */
  static class AtanFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return Math.atan(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix atanhElementWise() {
    return elementWiseFunction(new AtanhFunction());
  }

  /**
   * 逆双曲線正接関数を計算する実数関数を定義するためのクラスです。
   * 
   * @author koga
   */
  static class AtanhFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return DoubleNumberUtil.atanh(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix expElementWise() {
    return elementWiseFunction(new ExpFunction());
  }

  /**
   * 指数関数を計算する実数関数を定義するためのクラスです。
   * 
   * @author Koga Laboratory
   * @version $Revision: 1.28 $
   */
  static class ExpFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return Math.exp(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix logElementWise() {
    return elementWiseFunction(new LogFunction());
  }

  /**
   * 自然対数関数を計算する実数関数を定義するためのクラスです。
   * 
   * @author koga
   */
  static class LogFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return Math.log(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix log10ElementWise() {
    return elementWiseFunction(new Log10Function());
  }

  /**
   * 常用対数を計算する関数を定義するためのクラスです。
   * 
   * @author koga
   */
  static class Log10Function implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return Math.log10(value);
    }
  }

  /**
   * 各成分の偏角を成分に持つ行列を返します。
   * 
   * @return 偏角行列
   */
  public final DoubleMatrix argumentElementWise() {
    return elementWiseFunction(new ArgFunction());
  }

  /**
   * 偏角を計算する関数を定義するためのクラスです。
   * 
   * @author Koga Laboratory
   * @version $Revision: 1.28 $
   */
  static class ArgFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return 0;
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix signumElementWise() {
    return elementWiseFunction(new SignumFunction());
  }

  /**
   * 符号を計算する関数を定義するためのクラスです。
   * 
   * @author Koga Laboratory
   * @version $Revision: 1.28 $
   */
  static class SignumFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return DoubleNumberUtil.signum(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix absElementWise() {
    return elementWiseFunction(new AbsFunction());
  }

  /**
   * 絶対値を計算する関数を定義するためのクラスです。
   * 
   * @author Koga Laboratory
   * @version $Revision: 1.28 $
   */
  static class AbsFunction implements DoubleRealFunction {

    /**
     * {@inheritDoc}
     */
    public double evaluate(final double value) {
      return Math.abs(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix stdRowWise() {
    return new DoubleMatrix(DoubleMatrixUtil.stdRowWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix stdColumnWise() {
    return new DoubleMatrix(DoubleMatrixUtil.stdColumnWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleIndexedMatrix sortRowWise() {
    final IndexedDoubleElements ans = DoubleMatrixUtil.sortRowWise(this.elements);
    return new DoubleIndexedMatrix(new DoubleMatrix(ans.getElements()), new IntMatrix(ans.getIndices()));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleIndexedMatrix sortColumnWise() {
    final IndexedDoubleElements ans = DoubleMatrixUtil.sortColumnWise(this.elements);
    return new DoubleIndexedMatrix(new DoubleMatrix(ans.getElements()), new IntMatrix(ans.getIndices()));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix maxRowWise() {
    return new DoubleMatrix(DoubleMatrixUtil.maxRowWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix maxColumnWise() {
    return new DoubleMatrix(DoubleMatrixUtil.maxColumnWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final ElementHolder<DoubleNumber> maximum() {
    final Object[] ans = DoubleMatrixUtil.maximum(this.elements);
    final int rowNumber, columnNumber;
    if (this.elements.length == 1) {
      rowNumber = 1;
      columnNumber = ((Integer)ans[1]).intValue();
    } else if (this.elements[0].length == 1) {
      rowNumber = ((Integer)ans[1]).intValue();
      columnNumber = 1;
    } else {
      rowNumber = ((Integer)ans[1]).intValue();
      columnNumber = ((Integer)ans[2]).intValue();
    }

    return new ElementHolder<>(new DoubleNumber(((Double)ans[0]).doubleValue()), rowNumber, columnNumber);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleIndexedMatrix maximumRowWise() {
    final Object[] maxIndex = DoubleMatrixUtil.maximumRowWise(this.elements);
    final double[][] ans = (double[][])maxIndex[0];
    final int[] index = (int[])maxIndex[1];
    return new DoubleIndexedMatrix(new DoubleMatrix(ans), new IntMatrix(index));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleIndexedMatrix maximumColumnWise() {
    final Object[] maxIndex = DoubleMatrixUtil.maximumColumnWise(this.elements);
    final double[] ans = (double[])maxIndex[0];
    final int[] index = (int[])maxIndex[1];
    return new DoubleIndexedMatrix(new DoubleMatrix(ans), new IntMatrix(index));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix minRowWise() {
    return new DoubleMatrix(DoubleMatrixUtil.minRowWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix minColumnWise() {
    return new DoubleMatrix(DoubleMatrixUtil.minColumnWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final ElementHolder<DoubleNumber> minimum() {
    final Object[] ans = DoubleMatrixUtil.minimum(this.elements);

    final int rowNumber, columnNumber;
    if (this.elements.length == 1) {
      rowNumber = 1;
      columnNumber = ((Integer)ans[1]).intValue();
    } else if (this.elements[0].length == 1) {
      rowNumber = ((Integer)ans[1]).intValue();
      columnNumber = 1;
    } else {
      rowNumber = ((Integer)ans[1]).intValue();
      columnNumber = ((Integer)ans[2]).intValue();
    }

    return new ElementHolder<>(new DoubleNumber(((Double)ans[0]).doubleValue()), rowNumber, columnNumber);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleIndexedMatrix minimumRowWise() {
    final Object[] minIndex = DoubleMatrixUtil.minimumRowWise(this.elements);
    final double[][] ans = (double[][])minIndex[0];
    final int[] index = (int[])minIndex[1];
    return new DoubleIndexedMatrix(new DoubleMatrix(ans), new IntMatrix(index));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleIndexedMatrix minimumColumnWise() {
    final Object[] minIndex = DoubleMatrixUtil.minimumColumnWise(this.elements);
    final double[] ans = (double[])minIndex[0];
    final int[] index = (int[])minIndex[1];
    return new DoubleIndexedMatrix(new DoubleMatrix(ans), new IntMatrix(index));
  }

  /**
   * 各成分の大きい方の値を成分とする行列を生成します。
   * 
   * @param opponent 比較対象
   * @return 各成分の最大値を持つ行列
   */
  public final DoubleMatrix maxElementWise(final IntMatrix opponent) {
    return maxElementWise(new DoubleMatrix(opponent));
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final NumericalMatrix<?,?> maxElementWise(final NumericalMatrix<?,?> opponent) {
  //    if (AbstractMatrix.isTransformableToSameClass(this, opponent) == false) {
  //      throw new IllegalArgumentException(Messages.getString("DoubleMatrix.63")); //$NON-NLS-1$  
  //    }
  //    
  //    final Matrix<?,?>[] mm = AbstractMatrix.transformToSameClass(this, opponent);
  //
  //    if (mm[0] instanceof DoubleMatrix) {
  //      return ((DoubleMatrix)mm[0]).maxElementWise((DoubleMatrix)mm[1]);
  //    }
  //
  //    return ((NumericalMatrix<?,?>)mm[0]).maxElementWise((NumericalMatrix<?,?>)mm[1]);
  //  }

  /**
   * 各成分の小さい方の値を成分とする行列を生成します。
   * 
   * @param opponent 比較対象
   * @return 各成分の最小値を持つ行列
   */
  public final DoubleMatrix minElementWise(final IntMatrix opponent) {
    return minElementWise(new DoubleMatrix(opponent));
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final DoubleMatrix minElementWise(final DoubleMatrix opponent) {
  ////    if (AbstractMatrix.isTransformableToSameClass(this, opponent) == false) {
  ////      throw new IllegalArgumentException(Messages.getString("DoubleMatrix.64")); //$NON-NLS-1$  
  ////    }
  ////    
  ////    final Matrix<?,?>[] mm = AbstractMatrix.transformToSameClass(this, opponent);
  //
  //   // if (mm[0] instanceof DoubleMatrix) {
  //   // return ((DoubleMatrix)mm[0]).minElementWise((DoubleMatrix)mm[1]);
  //    //}
  //
  //    //return ((NumericalMatrix<?,?>)mm[0]).minElementWise((NumericalMatrix<?,?>)mm[1]);
  //  }

  /**
   * 各成分の大きい方の値を成分とする行列を生成します。
   * 
   * @param opponent 比較対象
   * @return 各成分の最大値を持つ行列
   */
  public final DoubleMatrix maxElementWise(final DoubleMatrix opponent) {
    if (isSameSize(opponent) == false) {
      throw new MatrixSizeException(this, opponent, MatrixSizeException.NOT_SAME_SIZE);
    }
    return new DoubleMatrix(DoubleMatrixUtil.maxElementWise(this.elements, opponent.elements));
  }

  /**
   * 各成分の小さい方の値を成分とする行列を生成します。
   * 
   * @param opponent 比較対象
   * @return 各成分の最小値を持つ行列
   */
  public final DoubleMatrix minElementWise(final DoubleMatrix opponent) {
    if (isSameSize(opponent) == false) {
      throw new MatrixSizeException(this, opponent, MatrixSizeException.NOT_SAME_SIZE);
    }

    return new DoubleMatrix(DoubleMatrixUtil.minElementWise(this.elements, opponent.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix medianColumnWise() {
    return new DoubleMatrix(DoubleMatrixUtil.medianColumnWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix medianRowWise() {
    return new DoubleMatrix(DoubleMatrixUtil.medianRowWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber norm(final NormType type) {
    if (type == NormType.FROBENIUS) {
      return this.frobNorm();
    }

    if (type == NormType.INFINITY) {
      return this.infNorm();
    }

    if (type == NormType.ONE || type == NormType.TWO) {
      return new DoubleNumber(DoubleMatrixUtil.norm(this.elements, type));
    }

    throw new IllegalArgumentException(Messages.getString("DoubleMatrix.65")); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber frobNorm() {
    return new DoubleNumber(DoubleMatrixUtil.frobNorm(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix frobNormRowWise() {
    return new DoubleMatrix(DoubleMatrixUtil.frobNormRowWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix frobNormColumnWise() {
    return new DoubleMatrix(DoubleMatrixUtil.frobNormColumnWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber infNorm() {
    return new DoubleNumber(DoubleMatrixUtil.infNorm(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix powerElementWise(final double order) {
    return new DoubleMatrix(DoubleMatrixUtil.powerElementWise(this.elements, order));
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @Override
  //  public final Matrix<?,?> powerElementWise(final Matrix<?,?> order) {
  //    if (order instanceof IntMatrix) {
  //      return powerElementWise((IntMatrix)order);
  //    }
  //    if (order instanceof DoubleMatrix) {
  //      return powerElementWise((DoubleMatrix)order);
  //    }
  //    if (order instanceof DoubleComplexMatrix) {
  //      return powerElementWise(order);
  //    }
  //
  //    throw new IllegalArgumentException(Messages.getString("AbstractMatrix.10")); //$NON-NLS-1$
  //  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix powerElementWise(final DoubleNumber order) {
    //    if (order instanceof DoubleComplexNumber) {
    //      return new DoubleComplexMatrix(DoubleComplexMatrixUtil.powerElementWise(this.elements, (DoubleComplexNumber)order));
    //    } 

    //if (order instanceof DoubleNumber) {
    return powerElementWise(order.doubleValue());
    //}

    //throw new IllegalArgumentException();
  }

  /**
   * 各成分の累乗した値を成分とする行列を生成します。
   * 
   * @param value 累乗の指数を成分とする行列
   * @return 累乗の結果
   */
  public final DoubleMatrix powerElementWise(final DoubleMatrix value) {
    if (!isSameSize(value)) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrix.66")); //$NON-NLS-1$
    }

    return new DoubleMatrix(DoubleMatrixUtil.powerElementWise(this.elements, value.elements));
  }

  //  /**
  //   * 各成分の累乗した値を成分とする行列を生成します。
  //   * 
  //   * @param order 累乗の指数を成分とする行列
  //   * @return 累乗の結果
  //   */
  //  public final NumericalMatrix<?,?> powerElementWise(final BaseNumericalMatrix<?,?> order) {
  //    if (order.isTransformableFrom(this) == false) {
  //      throw new IllegalArgumentException(Messages.getString("DoubleMatrix.68")); //$NON-NLS-1$
  //    }
  //      
  //    if (!this.isSameSize(order)) {
  //      throw new MatrixSizeException(Messages.getString("DoubleMatrix.67")); //$NON-NLS-1$
  //    }
  //
  //    return order.transformFrom(this).powerElementWise(order);
  //  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final NumericalMatrix<?,?> powerElementWise(final NumericalMatrix<?,?> order) {
  //    if (order instanceof DoubleMatrix) {
  //      return powerElementWise((DoubleMatrix)order);
  //    }
  //
  //    if (order.isTransformableFrom(this) == false) {
  //      throw new IllegalArgumentException(Messages.getString("DoubleMatrix.68")); //$NON-NLS-1$
  //    }
  //    
  //    return powerElementWise((BaseNumericalMatrix<?,?>)order);
  //  }

  /**
   * {@inheritDoc}
   */
  public final DoubleIndexedMatrix sort() {
    final int elementNumber = getRowSize() * getColumnSize();
    final IndexedMatrix<DoubleNumber, DoubleMatrix> mm = reshape(1, elementNumber).sortRowWise();
    return new DoubleIndexedMatrix(mm.getMatrix().reshape(getRowSize(), getColumnSize()), mm.getIndices().reshape(getRowSize(), getColumnSize()));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix inverse() {
    return inverse(this.frobNorm().doubleValue() * DoubleNumberUtil.EPS, false);
  }

  /**
   * <code>size</code>*<code>size</code>の単位行列を生成します。
   * 
   * @param size サイズ指定
   * @return size*sizeの単位行列
   */
  public static DoubleMatrix unit(final int size) {
    return unit(size, size);
  }

  /**
   * <code>rowSize</code>*<code>columnSize</code>の実単位行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return <code>rowSize</code>*<code>columnSize</code>の単位行列
   */
  public static DoubleMatrix unit(final int rowSize, final int columnSize) {
    return new DoubleMatrix(rowSize, columnSize, DoubleMatrixUtil.createUnit(rowSize, columnSize));
  }

  /**
   * 行列<code>matrix</code>と同サイズの実単位行列を生成します。
   * 
   * @param matrix 行列
   * @return 単位行列
   */
  public static DoubleMatrix unit(final Grid matrix) {
    return unit(matrix.getRowSize(), matrix.getColumnSize());
  }

  /**
   * 行列<code>block</code>の<code>rowNumber</code>*<code>columnNumber</code> 倍の実単位行列を生成します。
   * 
   * @param rowNumber 行方向の倍数
   * @param columnNumber 列方向の倍数
   * @param block 基本となる行列
   * @return <code>block</code>の<code>rowNumber</code>* <code>columnNumber</code>倍の実単位行列
   */
  public static DoubleMatrix unit(final int rowNumber, final int columnNumber, final Grid block) {
    return unit(block.getRowSize() * rowNumber, block.getColumnSize() * columnNumber);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix createUnit(final int rowSize, final int columnSize) {
    return new DoubleMatrix(rowSize, columnSize, DoubleMatrixUtil.createUnit(rowSize, columnSize));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix createZero(final int rowSize, final int columnSize) {
    return new DoubleMatrix(rowSize, columnSize);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix createOnes(final int rowSize, final int columnSize) {
    return new DoubleMatrix(rowSize, columnSize, DoubleMatrixUtil.createOnes(rowSize, columnSize));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix createUniformRandom() {
    return createUniformRandom(getRowSize(), getColumnSize());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix createUniformRandom(final int rowNumber, final int columnNumber, final Grid block) {
    return createUniformRandom(block.getRowSize() * rowNumber, block.getColumnSize() * columnNumber);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix createUniformRandom(final int rowSize, final int columnSize) {
    return new DoubleMatrix(rowSize, columnSize, DoubleMatrixUtil.createUniformRandom(rowSize, columnSize));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix createUniformRandom(final int rowSize, final int columnSize, final long seed) {
    return new DoubleMatrix(rowSize, columnSize, DoubleMatrixUtil.createUniformRandom(rowSize, columnSize, seed));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix createNormalRandom() {
    return createNormalRandom(getRowSize(), getColumnSize());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix createNormalRandom(final int rowNumber, final int columnNumber, final Grid block) {
    return createNormalRandom(block.getRowSize() * rowNumber, block.getColumnSize() * columnNumber);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix createNormalRandom(final int rowSize, final int columnSize) {
    return new DoubleMatrix(rowSize, columnSize, DoubleMatrixUtil.createNormalRandom(rowSize, columnSize));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix createNormalRandom(final int rowSize, final int columnSize, final long seed) {
    return new DoubleMatrix(rowSize, columnSize, DoubleMatrixUtil.createNormalRandom(rowSize, columnSize, seed));
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isComplex() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isReal() {
    return true;
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final DoubleMatrix getImaginaryPart() {
  //    return createZero(getRowSize(), getColumnSize());
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final DoubleMatrix getRealPart() {
  //    return clone();
  //  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final void setRealPart( final Matrix<?,?> realPart) {
  //    throw new UnsupportedOperationException(Messages.getString("DoubleMatrix.69")); //$NON-NLS-1$
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final void setRealPart( final IntMatrix realPart) {
  //    throw new UnsupportedOperationException(Messages.getString("DoubleMatrix.70")); //$NON-NLS-1$
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final void setRealPart( final DoubleMatrix realPart) {
  //    throw new UnsupportedOperationException(Messages.getString("DoubleMatrix.71")); //$NON-NLS-1$
  //  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final void setRealPart( final BaseMatrix<?, ?> realPart) {
  //    throw new UnsupportedOperationException(Messages.getString("DoubleMatrix.72")); //$NON-NLS-1$
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final void setImaginaryPart( final Matrix<?,?> imaginaryPart) {
  //    throw new UnsupportedOperationException(Messages.getString("DoubleMatrix.73")); //$NON-NLS-1$
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final void setImaginaryPart( final IntMatrix imaginaryPart) {
  //    throw new UnsupportedOperationException(Messages.getString("DoubleMatrix.74")); //$NON-NLS-1$
  //  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final void setImaginaryPart( final DoubleMatrix imaginaryPart) {
  //    throw new UnsupportedOperationException(Messages.getString("DoubleMatrix.75")); //$NON-NLS-1$
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final void setImaginaryPart( final BaseMatrix<?, ?> imaginaryPart) {
  //    throw new UnsupportedOperationException(Messages.getString("DoubleMatrix.76")); //$NON-NLS-1$
  //  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final DoubleComplexMatrix toComplex() {
  //    return new DoubleComplexMatrix(this);
  //  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix createComplex(final DoubleMatrix realPart, final DoubleMatrix imagPart) {
    return new DoubleComplexMatrix(realPart, imagPart);
  }

//  /**
//   * {@link DoubleNumber}を成分とする{@link NumericalMatrix}を{@link DoubleMatrix}へ変換します。
//   * 
//   * @param matrix {@link DoubleNumber}を成分とする{@link NumericalMatrix}
//   * @return 変換結果
//   */
//  public static DoubleMatrix toDoubleMatrix(final NumericalMatrix<DoubleNumber, ?> matrix) {
//    final int rowSize = matrix.getRowSize();
//    final int columnSize = matrix.getColumnSize();
//    final DoubleMatrix ans = new DoubleMatrix(rowSize, columnSize);
//
//    for (int i = 1; i <= rowSize; i++) {
//      for (int j = 1; j <= columnSize; j++) {
//        ans.setElement(i, j, matrix.getElement(i, j).doubleValue());
//      }
//    }
//
//    return ans;
//
//  }

  /**
   * 行列関数の値を返します。
   * 
   * @param function 複素数関数
   * @return 行列関数
   */
  final DoubleMatrix matrixFunction(final DoubleRealFunction function) {
    final int n = getColumnSize();

    final DoubleMatrix b = createClone();
    DoubleMatrix f = this.createZero(b.getRowSize(), b.getColumnSize());

    final SchurDecomposition<DoubleNumber,DoubleMatrix> tmp = b.schurDecompose();
    final DoubleMatrix u = tmp.getU();
    final DoubleMatrix t = tmp.getT();

    for (int i = 1; i <= n; i++) {
      f.setElement(i, i, function.evaluate(t.getElement(i, i).doubleValue()));
    }

    for (int p = 1; p <= n - 1; p++) {
      for (int i = 1; i <= n - p; i++) {
        int j = i + p;
        /*
         * s = TTC(i,j)*(FFC(j,j) - FFC(i,i));
         */
        final double tmp1 = f.getElement(j, j).doubleValue() - f.getElement(i, i).doubleValue();
        double s = t.getElement(i, j).doubleValue() * tmp1;

        for (int k = i + 1; k <= j - 1; k++) {
          /*
           * s = s + TTC(i,k)*FFC(k,j) - FFC(i,k)*TTC(k,j);
           */
          final double tmp2 = t.getElement(i, k).doubleValue() * f.getElement(k, j).doubleValue();
          final double tmp3 = f.getElement(i, k).doubleValue() * t.getElement(k, j).doubleValue();
          s = s + tmp2 - tmp3;
        }
        /*
         * FFC(i,j) = s/(TTC(j,j) - TTC(i,i));
         */
        double tmp4 = t.getElement(j, j).doubleValue() - t.getElement(i, i).doubleValue();

        /* Revised by Koga 1996.4.3 */
        if (tmp4 == 0) {
          tmp4 = DoubleNumberUtil.EPS;
        }

        try {
          f.setElement(i, j, s/tmp4);
        } catch (RuntimeException e) {
          throw new RuntimeException(Messages.getString("NumericalMatrix.10"), e); //$NON-NLS-1$
        }
      }
    }

    /*
     * u * func(t) * u# = u * f * u#
     */
    final DoubleMatrix c = u.multiply(f);
    final DoubleMatrix ut = u.conjugateTranspose();
    f = c.multiply(ut);
    return f;
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexMatrix toComplex() {
    return new DoubleComplexMatrix(this);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleNumber[][] getElements() {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public void setElements(DoubleNumber[][] elements) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexMatrix add(DoubleComplexMatrix value) {
    return toComplex().add(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexMatrix subtract(DoubleComplexMatrix value) {
    return toComplex().subtract(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexMatrix multiply(DoubleComplexMatrix value) {
    return toComplex().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexMatrix divide(DoubleComplexMatrix value) {
    return toComplex().divide(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexMatrix leftDivide(DoubleComplexMatrix value) {
    return toComplex().leftDivide(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexMatrix multiply(DoubleComplexNumber value) {
    return toComplex().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexMatrix divide(DoubleComplexNumber value) {
    return toComplex().divide(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexMatrix leftDivide(DoubleComplexNumber value) {
    return toComplex().leftDivide(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexMatrix appendDown(DoubleComplexMatrix value) {
    return toComplex().appendDown(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexMatrix appendRight(DoubleComplexMatrix value) {
    return toComplex().appendRight(value);
  }
}