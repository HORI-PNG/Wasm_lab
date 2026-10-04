/*
 * $Id: DoubleComplexMatrix.java,v 1.15 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.Writer;
import java.nio.charset.Charset;

import org.mklab.nfc.eig.BalancedDecompositionDoubleComplexElements;
import org.mklab.nfc.eig.DoubleComplexBalanceDecomposer;
import org.mklab.nfc.eig.DoubleComplexBalancedDecomposition;
import org.mklab.nfc.eig.DoubleComplexEigenSolver;
import org.mklab.nfc.eig.DoubleComplexGeneralizedEigenSolver;
import org.mklab.nfc.eig.DoubleComplexHessenbergDecomposer;
import org.mklab.nfc.eig.DoubleComplexHessenbergDecomposition;
import org.mklab.nfc.eig.DoubleComplexQRDecomposer;
import org.mklab.nfc.eig.DoubleComplexQRDecomposition;
import org.mklab.nfc.eig.DoubleComplexQZDecomposer;
import org.mklab.nfc.eig.DoubleComplexQZDecomposition;
import org.mklab.nfc.eig.DoubleComplexSchurDecomposer;
import org.mklab.nfc.eig.DoubleComplexSchurDecomposition;
import org.mklab.nfc.eig.DoubleEigenSolution;
import org.mklab.nfc.eig.EigenSolutionDoubleElements;
import org.mklab.nfc.eig.HessenbergDecompositionComplexRealElements;
import org.mklab.nfc.eig.QRDecompositionDoubleComplexElements;
import org.mklab.nfc.eig.QZDecompositionDoubleComplexElements;
import org.mklab.nfc.eig.SchurDecompositionDoubleComplexElements;
import org.mklab.nfc.fft.DoubleComplexFFTAnalyzer;
import org.mklab.nfc.leq.DoubleComplexLUDecomposer;
import org.mklab.nfc.matx.MatxObject;
import org.mklab.nfc.matx.MxDataHead;
import org.mklab.nfc.scalar.DoubleComplexNumber;
import org.mklab.nfc.scalar.DoubleNumber;
import org.mklab.nfc.scalar.DoubleNumberUtil;
import org.mklab.nfc.svd.DoubleComplexSingularValueDecomposer;
import org.mklab.nfc.svd.DoubleComplexSingularValueDecomposition;
import org.mklab.nfc.svd.SingularValueDecompositionDoubleComplexElements;


/**
 * 倍精度(double)型の複素数{@link org.mklab.nfc.scalar.DoubleComplexNumber}を成分とする行列を表すクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.15 $
 */
public class DoubleComplexMatrix extends AbstractNumericalComplexMatrix<DoubleNumber,DoubleMatrix,DoubleComplexNumber,DoubleComplexMatrix> implements MatxObject {

  /** シリアルバージョン。 */
  private static final long serialVersionUID = -3550806106286754932L;

  /**
   * <code>rowSize</code>*<code>columnSize</code>の複素数行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   */
  public DoubleComplexMatrix(final int rowSize, final int columnSize) {
    this(rowSize, columnSize, DoubleComplexNumber.createZeroArray(rowSize, columnSize));
  }

  /**
   * <code>elements</code>で与えられた行ベクトルを生成します。
   * 
   * @param elements 複素数の配列
   */
  public DoubleComplexMatrix(final DoubleComplexNumber[] elements) {
    this(elements.length == 0 ? 0 : 1, elements.length, elements.length == 0 ? new DoubleComplexNumber[0][0] : new DoubleComplexNumber[][] {elements});
  }

//  /**
//   * <code>elements</code>で与えられた行ベクトルを生成します。
//   * 
//   * @param elements 複素数の配列
//   */
//  public DoubleComplexMatrix(final BaseComplexNumericalScalar<DoubleNumber,DoubleMatrix>[] elements) {
//    this(elements.length == 0 ? 0 : 1, elements.length, elements.length == 0 ? new DoubleComplexNumber[0][0] : new DoubleComplexNumber[][] {DoubleComplexNumber.createArray(elements)});
//  }

//  /**
//   * <code>elements</code>で与えられた成分を持つ複素数行列を生成します。
//   * @param matrix 複素行列
//   */
//  public DoubleComplexMatrix(BaseNumericalComplexMatrix<DoubleNumber,DoubleMatrix> matrix) {
//    this(matrix.getElements());
//  }

  /**
   * <code>elements</code>で与えられた成分を持つ複素数行列を生成します。
   * 
   * @param elements 成分を含む配列
   */
  public DoubleComplexMatrix(final DoubleComplexNumber[][] elements) {
    this(elements.length, (elements.length == 0 || elements[0] == null) ? 0 : elements[0].length, elements);
  }

//  /**
//   * <code>elements</code>で与えられた成分を持つ複素数行列を生成します。
//   * 
//   * @param elements 成分を含む配列
//   */
//  public DoubleComplexMatrix(final BaseComplexNumericalScalar<DoubleNumber,DoubleMatrix>[][] elements) {
//    this(elements.length, (elements.length == 0 || elements[0] == null) ? 0 : elements[0].length, elements);
//  }

  /**
   * <code>elements</code>で与えられた成分をもつ<code>rowSize</code>*<code>columnSize</code >の複素数行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 成分を含む配列
   */
  public DoubleComplexMatrix(final int rowSize, final int columnSize, final DoubleComplexNumber[][] elements) {
    super(rowSize, columnSize, elements);
    setElementAlignment(GridElementAlignment.RIGHT);
  }

//  /**
//   * <code>elements</code>で与えられた成分をもつ<code>rowSize</code>*<code>columnSize</code >の複素数行列を生成します。
//   * 
//   * @param rowSize 行の数
//   * @param columnSize 列の数
//   * @param elements 成分を含む配列
//   */
//  public DoubleComplexMatrix(final int rowSize, final int columnSize, final BaseComplexNumericalScalar<DoubleNumber,DoubleMatrix>[][] elements) {
//    this(rowSize, columnSize, DoubleComplexNumber.createArray(elements));
//  }

  /**
   * <code>realPart</code>を実部、<code>imaginaryPart</code>を虚部とする複素行列を生成します。
   * 
   * @param realPart 実部行列
   * @param imaginaryPart 虚部行列
   */
  public DoubleComplexMatrix(final DoubleMatrix realPart, final DoubleMatrix imaginaryPart) {
    this(realPart.getDoubleElements(), imaginaryPart.getDoubleElements());
  }

  /**
   * 新しく生成された<code>DoubleComplexMatrix</code>オブジェクトを初期化します。
   * 
   * @param realPart 整数行列
   */
  public DoubleComplexMatrix(final IntMatrix realPart) {
    this(DoubleComplexMatrixUtil.createArray(realPart.getIntElements()));
  }

  /**
   * 新しく生成された<code>DoubleComplexMatrix</code>オブジェクトを初期化します。
   * 
   * @param realPart 実数行列
   */
  public DoubleComplexMatrix(final DoubleMatrix realPart) {
    this(DoubleComplexMatrixUtil.createArray(realPart.getDoubleElements()));
  }

//  /**
//   * 新しく生成された<code>DoubleComplexMatrix</code>オブジェクトを初期化します。
//   * 
//   * @param realPart 実数行列
//   */
//  public DoubleComplexMatrix(final DoubleMatrix realPart) {
//    this(DoubleComplexMatrixUtil.createArray(realPart.getElements()));
//  }

  /**
   * <code>realPart</code>を実部、<code>imaginaryPart</code>を虚部とする複素行列を生成します。
   * 
   * @param realPart 実部行列
   */
  public DoubleComplexMatrix(final double[][] realPart) {
    this(realPart, DoubleMatrixUtil.createZero(realPart.length, realPart.length == 0  ? 0 : realPart[0].length));
  }

  /**
   * <code>realPart</code>を実部、<code>imaginaryPart</code>を虚部とする複素行列を生成します。
   * 
   * @param realPart 実部行列
   */
  public DoubleComplexMatrix(final DoubleNumber[][] realPart) {
    this(realPart, DoubleNumberUtil.createArray(realPart.length, realPart.length == 0 ? 0 : realPart[0].length));
  }

  /**
   * <code>realPart</code>を実部、<code>imaginaryPart</code>を虚部とする複素行列を生成します。
   * 
   * @param realPart 実部行列
   * @param imaginaryPart 虚部行列
   */
  public DoubleComplexMatrix(final double[][] realPart, final double[][] imaginaryPart) {
    this(DoubleComplexNumber.createArray(realPart, imaginaryPart));
  }

  /**
   * <code>realPart</code>を実部、<code>imaginaryPart</code>を虚部とする複素行列を生成します。
   * 
   * @param realPart 実部行列
   * @param imaginaryPart 虚部行列
   */
  public DoubleComplexMatrix(final DoubleNumber[][] realPart, final DoubleNumber[][] imaginaryPart) {
    this(DoubleComplexNumber.createArray(realPart, imaginaryPart));
  }

  /**
   * <code>realPart</code>を実部、<code>imaginaryPart</code>を虚部とする<code>rowSize</code>*<code>columnSize</code>の複素行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param realPart 実部行列
   */
  public DoubleComplexMatrix(final int rowSize, final int columnSize, final double[][] realPart) {
    this(rowSize, columnSize, realPart, DoubleMatrixUtil.createZero(rowSize, columnSize));
  }

  /**
   * <code>realPart</code>を実部、<code>imaginaryPart</code>を虚部とする<code>rowSize</code>*<code>columnSize</code>の複素行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param realPart 実部行列
   * @param imaginaryPart 虚部行列
   */
  public DoubleComplexMatrix(final int rowSize, final int columnSize, final double[][] realPart, final double[][] imaginaryPart) {
    super(rowSize, columnSize, DoubleComplexNumber.createArray(realPart, imaginaryPart));
    setElementAlignment(GridElementAlignment.RIGHT);
  }

  /**
   * <code>realPart</code>を実部、<code>imaginaryPart</code>を虚部とする行ベクトルを生成します。
   * 
   * @param realPart 実部ベクトル
   */
  public DoubleComplexMatrix(final double[] realPart) {
    this(realPart, DoubleMatrixUtil.createZero(realPart.length));
  }

  /**
   * <code>realPart</code>を実部、<code>imaginaryPart</code>を虚部とする行ベクトルを生成します。
   * 
   * @param realPart 実部ベクトル
   */
  public DoubleComplexMatrix(final DoubleNumber[] realPart) {
    this(realPart, DoubleNumberUtil.createArray(realPart.length));
  }

  /**
   * <code>realPart</code>を実部、<code>imaginaryPart</code>を虚部とする行ベクトルを生成します。
   * 
   * @param realPart 実部ベクトル
   * @param imaginaryPart 虚部ベクトル
   */
  public DoubleComplexMatrix(final double[] realPart, final double[] imaginaryPart) {
    this(DoubleComplexNumber.createArray(realPart, imaginaryPart));
  }

  /**
   * <code>realPart</code>を実部、<code>imaginaryPart</code>を虚部とする行ベクトルを生成します。
   * 
   * @param realPart 実部ベクトル
   * @param imaginaryPart 虚部ベクトル
   */
  public DoubleComplexMatrix(final DoubleNumber[] realPart, final DoubleNumber[] imaginaryPart) {
    this(DoubleComplexNumber.createArray(realPart, imaginaryPart));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void printElements(final Writer output, final int maxColumnSize) {
    DoubleComplexMatrixUtil.print(getElements(), output, getElementFormat(), getElementAlignment(), maxColumnSize);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getGridClassName() {
    return "CoMatrix"; //$NON-NLS-1$
  }

  /**
   * データをMATフォーマットで指定したファイルに保存します。
   * 
   * @param file ファイル
   * @exception IOException ファイルに出力できない場合
   * 
   */
  public final void writeMatFormat(final File file) throws IOException {
    try (final Writer output = new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8"))) { //$NON-NLS-1$
      writeMatFormat(output);
    }
  }

  /**
   * データをMATフォーマットでライターに出力します。
   * 
   * @param output ライター
   * @exception IOException 入出力エラーが発生した場合
   */
  public final void writeMatFormat(final Writer output) throws IOException {
    DoubleComplexMatrixUtil.writeMatFormat(getElements(), output);
  }

  /**
   * {@link org.mklab.nfc.matx.MatxMatrix#readMatFormat(Reader)}用の中間メソッドです。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param st matファイルから読み込んだ {@link java.io.StreamTokenizer}
   * @return matファイルから読み込み,生成された行列
   * @exception IOException 入出力エラーが発生した場合
   */
  public static DoubleComplexMatrix readMatFormat(final int rowSize, final int columnSize, final StreamTokenizer st) throws IOException {
    return new DoubleComplexMatrix(DoubleComplexMatrixUtil.readMatFormat(rowSize, columnSize, st));
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMxFormat(final DataOutputStream output, final String name) throws IOException {
    DoubleComplexMatrixUtil.writeMxFormat(getElements(), output, name);
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
   * <p>このメソッドを直接使わず
   * 
   * <blockquote><code> {@link org.mklab.nfc.matrix.Matrix} A = Matrix. {@link org.mklab.nfc.matx.MatxMatrix#readMxFormat(InputStream)} </code></blockquote>
   * 
   * の形で使用してください。
   * 
   * @param input 入力ストリーム
   * @param head ヘッダー
   * 
   * @return mxファイルから読み込み,生成された行列
   * @exception IOException 入力ストリームから読み込めない場合
   */
  public static DoubleComplexMatrix readMxFormat(final InputStream input, final MxDataHead head) throws IOException {
    return new DoubleComplexMatrix(DoubleComplexMatrixUtil.readMxFormat(input, head));
  }

  /**
   * {@inheritDoc}
   */
  public String toMmString() {
    return DoubleComplexMatrixUtil.toMmString(getElements(), getElementFormat());
  }

  /**
   * {@inheritDoc}
   */
  public final String toMmString(final String format) {
    return DoubleComplexMatrixUtil.toMmString(getElements(), format);
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

    final StringBuffer code = new StringBuffer();
    if (name.length() != 0) {
      code.append(name);
      code.append(" = "); //$NON-NLS-1$
      code.append(newLine);
    }

    code.append(toMmString());

    if (withNewLine) {
      code.append(";"); //$NON-NLS-1$
      code.append(newLine);
      code.append(newLine);
    }

    output.write(code.toString());
    output.flush();
  }

  /**
   * <code>size</code>*<code>size</code>の単位複素行列を生成します。
   * 
   * @param size 次数
   * @return <code>size</code>*<code>size</code>の単位複素行列
   */
  public static DoubleComplexMatrix unit(final int size) {
    return unit(size, size);
  }

  /**
   * <code>rowSize</code>*<code>columnSize</code>の単位複素行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return rowSize*colSizeの単位複素行列
   */
  public static DoubleComplexMatrix unit(final int rowSize, final int columnSize) {
    return new DoubleComplexMatrix(rowSize, columnSize, DoubleComplexMatrixUtil.unit(rowSize, columnSize));
  }

  /**
   * 行列<code>matrix</code>と同サイズの単位複素行列を生成します。
   * 
   * @param matrix 行列
   * @return 行列<code>matrix</code>と同サイズの単位複素行列
   */
  public static DoubleComplexMatrix unit(final Grid matrix) {
    return unit(matrix.getRowSize(), matrix.getColumnSize());
  }

  /**
   * <code>size</code>*<code>size</code>の全成分1の複素行列を生成します。
   * 
   * @param size サイズ指定
   * @return <code>size</code>*<code>size</code>の全成分1の複素行列
   */
  public static DoubleComplexMatrix ones(final int size) {
    return ones(size, size);
  }

  /**
   * <code>rowSize</code>*<code>columnSize</code>の全成分1の複素行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return <code>rowSize</code>*<code>columnSize</code>の全成分1の行列
   */
  public static DoubleComplexMatrix ones(final int rowSize, final int columnSize) {
    return new DoubleComplexMatrix(rowSize, columnSize, DoubleComplexMatrixUtil.ones(rowSize, columnSize));
  }

  /**
   * 行列<code>matrix</code>と同サイズの全成分1の複素行列を生成します。
   * 
   * @param matrix 行列
   * @return 全成分1の複素行列
   */
  public static DoubleComplexMatrix ones(final Grid matrix) {
    return ones(matrix.getRowSize(), matrix.getColumnSize());
  }

  /**
   * 実部をdoubleの2次元配列で返します。
   * 
   * @return 全実部
   */
  double[][] getRealPartDoubleElements() {
    return DoubleComplexMatrixUtil.getRealPartElements(getElements());
  }

  /**
   * 虚部をdoubleの2次元配列で返します。
   * 
   * @return 全虚部
   */
  double[][] getImaginaryPartDoubleElements() {
    return DoubleComplexMatrixUtil.getImaginaryPartElements(getElements());
  }

  /**
   * 一様分布の乱数を成分とする複素行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 一様分布の乱数を成分とする行列
   */
  public static DoubleComplexMatrix uniformRandom(final int rowSize, final int columnSize) {
    final DoubleComplexNumber[][] ans = DoubleComplexMatrixUtil.createUniformRandom(rowSize, columnSize);
    return new DoubleComplexMatrix(ans);
  }

  /**
   * 一様乱数の種を<code>seed</code>で指定し, その種によって生成される乱数成分をもつ <code>rowSize</code>*<code>columnSize</code>の複素行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param seed 一様乱数の種
   * @return ランダムな成分を持つ複素行列
   */
  public static DoubleComplexMatrix uniformRandom(final int rowSize, final int columnSize, final long seed) {
    final DoubleComplexNumber[][] ans = DoubleComplexMatrixUtil.createUniformRandom(rowSize, columnSize, seed);
    return new DoubleComplexMatrix(ans);

  }

  /**
   * 整数の成分毎の累乗を成分とする行列を生成します。
   * 
   * @param scalar 累乗の対象となる整数
   * @param matrix 累乗の指数を成分とする行列
   * @return 累乗を成分とする行列
   */
  public static DoubleComplexMatrix powerElementWise(final int scalar, final DoubleComplexMatrix matrix) {
    return DoubleComplexMatrix.powerElementWise((double)scalar, matrix);
  }

  /**
   * 実数の成分毎の累乗を成分とする行列を生成します。
   * 
   * @param scalar 累乗の対象となる実数
   * @param matrix 累乗の指数を成分とする行列
   * @return 累乗を成分とする行列
   */
  public static DoubleComplexMatrix powerElementWise(final double scalar, final DoubleComplexMatrix matrix) {
    return new DoubleComplexMatrix(DoubleComplexMatrixUtil.powerElementWise(scalar, matrix.getElements()));
  }

  /**
   * *************************************************************************** **********************
   */

  /* NumericalMatrixの実装メソッド */
  /**
   * *************************************************************************** **********************
   */

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexMatrix eigenValue() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    final double[][] eigval = new DoubleComplexEigenSolver().getEigenValue(this.getRealPartDoubleElements(), this.getImaginaryPartDoubleElements());
    return new DoubleComplexMatrix(eigval[0], eigval[1]).transpose();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexMatrix eigenVector() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    final double[][][] eigVec = new DoubleComplexEigenSolver().getEigenVector(this.getRealPartDoubleElements(), this.getImaginaryPartDoubleElements());
    final double[][] eigVecReal = eigVec[0];
    final double[][] eigVecImag = eigVec[1];
    return new DoubleComplexMatrix(getRowSize(), getColumnSize(), eigVecReal, eigVecImag);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleEigenSolution eigenDecompose() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    final EigenSolutionDoubleElements eig = new DoubleComplexEigenSolver().solve(this.getRealPartDoubleElements(), this.getImaginaryPartDoubleElements());
    final double[] reValue = eig.getReValue();
    final double[] imValue = eig.getImValue();

    final double[][] realValues = new double[getRowSize()][getColumnSize()];
    final double[][] imagValues = new double[getRowSize()][getColumnSize()];

    for (int i = 0; i < getRowSize(); i++) {
      realValues[i][i] = reValue[i];
      imagValues[i][i] = imValue[i];
    }

    final DoubleMatrix realValue = new DoubleMatrix(realValues);
    final DoubleMatrix imagValue = new DoubleMatrix(imagValues);

    final DoubleMatrix realVector = new DoubleMatrix(eig.getReVector());
    final DoubleMatrix imagVector = new DoubleMatrix(eig.getImVector());

    return new DoubleEigenSolution(realValue, imagValue, realVector, imagVector);
  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public final DoubleComplexMatrix eigenValue(final DoubleComplexMatrix b) {
////    if (b instanceof DoubleMatrix) {
////      return eigenValue((DoubleMatrix)b);
////    }
////    if (b instanceof DoubleComplexMatrix) {
//      return eigenValue((DoubleComplexMatrix)b);
////    }
////    throw new IllegalArgumentException(Messages.getString("DoubleComplexMatrix.4")); //$NON-NLS-1$
//  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexMatrix eigenValue(final DoubleComplexMatrix b) {
    if (isSquare() == false || b.isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    if (hasSameRowSize(b) == false) {
      throw new MatrixSizeException(this, b, MatrixSizeException.INCORRECT_SIZE);
    }

    final double[][] eigVal = new DoubleComplexGeneralizedEigenSolver().getEigenValue(getRealPartDoubleElements(), getImaginaryPartDoubleElements(), b.getRealPartDoubleElements(),
        b.getImaginaryPartDoubleElements());
    return new DoubleComplexMatrix(DoubleMatrixUtil.transpose(eigVal[0]), DoubleMatrixUtil.transpose(eigVal[1]));
  }

  /**
   * 一般化固有値からなる列ベクトルを返します。
   * 
   * @param b 一般化固有値を求める対となる行列
   * @return 固有値を降順に並べた列ベクトル
   */
  public final DoubleComplexMatrix eigenValue(final DoubleMatrix b) {
    return eigenValue(new DoubleComplexMatrix(b));
  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public final DoubleComplexMatrix eigenVector(final NumericalMatrix<?, ?> b) {
//    if (b instanceof DoubleMatrix) {
//      return eigenVector((DoubleMatrix)b);
//    }
//    if (b instanceof DoubleComplexMatrix) {
//      return eigenVector((DoubleComplexMatrix)b);
//    }
//    throw new IllegalArgumentException(Messages.getString("DoubleComplexMatrix.5")); //$NON-NLS-1$
//  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexMatrix eigenVector(final DoubleComplexMatrix b) {
    if (isSquare() == false || b.isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    if (hasSameRowSize(b) == false) {
      throw new MatrixSizeException(this, b, MatrixSizeException.INCORRECT_SIZE);
    }

    final double[][][] eigVec = new DoubleComplexGeneralizedEigenSolver().getEigenVector(getRealPartDoubleElements(), getImaginaryPartDoubleElements(), b.getRealPartDoubleElements(),
        b.getImaginaryPartDoubleElements());

    final DoubleComplexNumber[][] vec = DoubleComplexMatrixUtil.createArray(eigVec[0], eigVec[1]);
    return new DoubleComplexMatrix(vec);
  }

  /**
   * 一般化固有ベクトルを返します。
   * 
   * @param b 一般化固有ベクトルを求める対となる行列
   * @return 一般化固有ベクトル
   */
  public final DoubleComplexMatrix eigenVector(final DoubleMatrix b) {
    return eigenVector(new DoubleComplexMatrix(b));
  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public final EigenSolution<DoubleNumber,DoubleMatrix> eigenDecompose(final NumericalMatrix<?, ?> b) {
//    if (b instanceof DoubleMatrix) {
//      return eigenDecompose((DoubleMatrix)b);
//    }
//    if (b instanceof DoubleComplexMatrix) {
//      return eigenDecompose((DoubleComplexMatrix)b);
//    }
//    throw new IllegalArgumentException(Messages.getString("DoubleComplexMatrix.6")); //$NON-NLS-1$
//  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleEigenSolution eigenDecompose(final DoubleComplexMatrix b) {
    if (isSquare() == false || b.isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    if (getRowSize() != b.getRowSize()) {
      throw new MatrixSizeException(this, b, MatrixSizeException.INCORRECT_SIZE);
    }

    final EigenSolutionDoubleElements eigValVec = new DoubleComplexGeneralizedEigenSolver().solve(getRealPartDoubleElements(), getImaginaryPartDoubleElements(), b.getRealPartDoubleElements(),
        b.getImaginaryPartDoubleElements());
    final double[] reValue = eigValVec.getReValue();
    final double[] imValue = eigValVec.getImValue();

    final double[][] realValues = new double[getRowSize()][getColumnSize()];
    final double[][] imagValues = new double[getRowSize()][getColumnSize()];

    for (int i = 0; i < getRowSize(); i++) {
      realValues[i][i] = reValue[i];
      imagValues[i][i] = imValue[i];
    }

    final DoubleMatrix realValue = new DoubleMatrix(realValues);
    final DoubleMatrix imagValue = new DoubleMatrix(imagValues);

    final double[][] realVectors = eigValVec.getReVector();
    final double[][] imagVectors = eigValVec.getImVector();
    final DoubleMatrix realVector = new DoubleMatrix(realVectors);
    final DoubleMatrix imagVector = new DoubleMatrix(imagVectors);

    return new DoubleEigenSolution(realValue, imagValue, realVector, imagVector);
  }

  /**
   * 一般化固有値を対角成分とする対角行列 D と対応する一般化固有ベクトルを列とする行列 X を成分とする配列を返します。
   * 
   * @param b 一般化固有分解を求める対となる行列
   * @return 第1成分D,第2成分Xとする {@link org.mklab.nfc.matrix.Matrix}の配列
   */
  public final DoubleEigenSolution eigenDecompose(final DoubleMatrix b) {
    return eigenDecompose(new DoubleComplexMatrix(b));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexQRDecomposition qrDecompose() {
    final QRDecompositionDoubleComplexElements qr = new DoubleComplexQRDecomposer().decompose(this.getRealPartDoubleElements(), this.getImaginaryPartDoubleElements());
    final DoubleComplexMatrix q = new DoubleComplexMatrix(qr.getReQ(), qr.getImQ());
    final DoubleComplexMatrix r = new DoubleComplexMatrix(qr.getReR(), qr.getImR());
    return new DoubleComplexQRDecomposition(q, r);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexQRDecomposition qrDecomposeWithPermutation() {
    final QRDecompositionDoubleComplexElements qr = new DoubleComplexQRDecomposer().decomposeWithPermutation(this.getRealPartDoubleElements(), this.getImaginaryPartDoubleElements());
    final DoubleComplexMatrix q = new DoubleComplexMatrix(qr.getReQ(), qr.getImQ());
    final DoubleComplexMatrix r = new DoubleComplexMatrix(qr.getReR(), qr.getImR());
    final IntMatrix p = new IntMatrix(qr.getP());
    return new DoubleComplexQRDecomposition(q, r, p);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @Override
  //  public final QZDecomposition<DoubleComplexMatrix> qzDecompose(final M b) {
  //    if (b instanceof DoubleMatrix) {
  //      return qzDecompose(b);
  //    }
  //    if (b instanceof DoubleComplexMatrix) {
  //      return qzDecompose(b);
  //    }
  //
  //    throw new IllegalArgumentException(Messages.getString("DoubleComplexMatrix.7")); //$NON-NLS-1$
  //  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexQZDecomposition qzDecompose(final DoubleComplexMatrix b) {
    final QZDecompositionDoubleComplexElements qz = new DoubleComplexQZDecomposer().qzDecompose(getRealPartDoubleElements(), getImaginaryPartDoubleElements(), b.getRealPartDoubleElements(),
        b.getImaginaryPartDoubleElements());
    final DoubleComplexMatrix aa = new DoubleComplexMatrix(qz.getReAA(), qz.getImAA());
    final DoubleComplexMatrix bb = new DoubleComplexMatrix(qz.getReBB(), qz.getImBB());
    final DoubleComplexMatrix q = new DoubleComplexMatrix(qz.getReQ(), qz.getImQ());
    final DoubleComplexMatrix z = new DoubleComplexMatrix(qz.getReZ(), qz.getImZ());
    final DoubleComplexMatrix x = new DoubleComplexMatrix(qz.getReX(), qz.getImX());
    return new DoubleComplexQZDecomposition(aa, bb, q, z, x);
  }

  //  /**
  //   * QZ分解の実行結果を返します。
  //   * 
  //   * @param b QZ分解をする対となる行列
  //   * 
  //   * @return QZ分解の結果AA,BB,Q,Z, Xの実部,Xの虚部
  //   */
  //  public final QZDecomposition<NumericalMatrix<?>> qzDecompose(final DoubleMatrix b) {
  //    return qzDecompose(new DoubleComplexMatrix(b));
  //  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexSchurDecomposition schurDecompose() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    final SchurDecompositionDoubleComplexElements ut = new DoubleComplexSchurDecomposer().decompose(getRealPartDoubleElements(), getImaginaryPartDoubleElements());
    final DoubleComplexMatrix u = new DoubleComplexMatrix(getRowSize(), getColumnSize(), ut.getReU(), ut.getImU());
    final DoubleComplexMatrix t = new DoubleComplexMatrix(getRowSize(), getColumnSize(), ut.getReT(), ut.getImT());
    return new DoubleComplexSchurDecomposition(u, t);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexMatrix kernel(final double tolerance) {
    final double[][][] kernel = new DoubleComplexSingularValueDecomposer().kernel(this.getRealPartDoubleElements(), this.getImaginaryPartDoubleElements(), tolerance);
    return new DoubleComplexMatrix(kernel[0], kernel[1]);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexMatrix kernel(final DoubleComplexNumber tolerance) {
    return kernel(tolerance.getRealPart().doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexHessenbergDecomposition hessenbergDecompose() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    final HessenbergDecompositionComplexRealElements qh = new DoubleComplexHessenbergDecomposer().decompose(this.getRealPartDoubleElements(), this.getImaginaryPartDoubleElements());

    final DoubleComplexMatrix q = new DoubleComplexMatrix(getRowSize(), getColumnSize(), qh.getReQ(), qh.getImQ());
    final DoubleComplexMatrix h = new DoubleComplexMatrix(getRowSize(), getColumnSize(), qh.getReH(), qh.getImH());
    return new DoubleComplexHessenbergDecomposition(q, h);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexMatrix pseudoInverse(final double tolerance) {
    final double[][][] pInv = new DoubleComplexSingularValueDecomposer().pseudoInverse(this.getRealPartDoubleElements(), this.getImaginaryPartDoubleElements(), tolerance);
    return new DoubleComplexMatrix(pInv[0], pInv[1]);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexMatrix pseudoInverse(final DoubleComplexNumber tolerance) {
    return pseudoInverse(tolerance.getRealPart() .doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexSingularValueDecomposition singularValueDecompose() {
    final SingularValueDecompositionDoubleComplexElements udv = new DoubleComplexSingularValueDecomposer().decompose(this.getRealPartDoubleElements(), this.getImaginaryPartDoubleElements());
    final DoubleComplexMatrix u = new DoubleComplexMatrix(udv.getReU(), udv.getImU());
    final DoubleComplexMatrix d = new DoubleComplexMatrix(udv.getD());
    final DoubleComplexMatrix v = new DoubleComplexMatrix(udv.getReV(), udv.getImV());
    return new DoubleComplexSingularValueDecomposition(u, d, v);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexMatrix singularValue() {
    final double[] singval = new DoubleComplexSingularValueDecomposer().singularValue(this.getRealPartDoubleElements(), this.getImaginaryPartDoubleElements());
    return new DoubleComplexMatrix(singval).transpose();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexMatrix fft(final int dataSize) {
    if (getRowSize() == 1) {
      final double[][] ans = DoubleComplexFFTAnalyzer.fft(this.getRealPartDoubleElements()[0], this.getImaginaryPartDoubleElements()[0], dataSize);
      return new DoubleComplexMatrix(ans[0], ans[1]);
    }
    if (getColumnSize() == 1) {
      return this.transpose().fft(dataSize).transpose();
    }

    throw new MatrixSizeException(Messages.getString("DoubleComplexMatrix.13")); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexMatrix fftRowWise(final int dataSize) {
    final double[][] ansr = new double[getRowSize()][getColumnSize()];
    final double[][] ansi = new double[getRowSize()][getColumnSize()];
    final double[][] mr = this.getRealPartDoubleElements();
    final double[][] mi = this.getImaginaryPartDoubleElements();

    for (int i = 0; i < getRowSize(); i++) {
      final double[][] tmp = DoubleComplexFFTAnalyzer.fft(mr[i], mi[i], dataSize);
      ansr[i] = tmp[0];
      ansi[i] = tmp[1];
    }
    return new DoubleComplexMatrix(ansr, ansi);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexMatrix ifft(final int dataSize) {
    if (getRowSize() == 1) {
      final double[][] ans = DoubleComplexFFTAnalyzer.ifft(this.getRealPartDoubleElements()[0], this.getImaginaryPartDoubleElements()[0], dataSize);
      return new DoubleComplexMatrix(ans[0], ans[1]);
    }

    if (getColumnSize() == 1) {
      return this.transpose().ifft(dataSize).transpose();
    }

    throw new MatrixSizeException(Messages.getString("DoubleComplexMatrix.16")); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexMatrix ifftRowWise(final int dataCount) {
    final double[][] ansr = new double[getRowSize()][getColumnSize()];
    final double[][] ansi = new double[getRowSize()][getColumnSize()];
    final double[][] mr = this.getRealPartDoubleElements();
    final double[][] mi = this.getImaginaryPartDoubleElements();

    for (int i = 0; i < getRowSize(); i++) {
      final double[][] tmp = DoubleComplexFFTAnalyzer.ifft(mr[i], mi[i], dataCount);
      ansr[i] = tmp[0];
      ansi[i] = tmp[1];
    }
    return new DoubleComplexMatrix(ansr, ansi);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexNumber norm(final NormType type) {
    if (type == NormType.FROBENIUS) {
      return this.frobNorm();
    }
    if (type == NormType.INFINITY) {
      return this.infNorm();
    }
    if (type == NormType.ONE || type == NormType.TWO) {
      return new DoubleComplexNumber(DoubleComplexMatrixUtil.norm(getElements(), type),0);
    }

    throw new RuntimeException(Messages.getString("DoubleComplexMatrix.18")); //$NON-NLS-1$
  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public final boolean isTransformableFrom(final Matrix<?,?> value) {
//    if (value instanceof IntMatrix) {
//      return true;
//    }
//    if (value instanceof DoubleMatrix) {
//      return true;
//    }
//    if (value instanceof DoubleComplexMatrix) {
//      return true;
//    }
//
//    return super.isTransformableFrom(value);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public final DoubleComplexMatrix transformFrom(final Matrix<?,?> value) {
//    if (value instanceof IntMatrix) {
//      return new DoubleComplexMatrix((IntMatrix)value);
//    }
//    if (value instanceof DoubleMatrix) {
//      return new DoubleComplexMatrix((DoubleMatrix)value);
//    }
//    if (value instanceof DoubleComplexMatrix) {
//      return clone();
//    }
//    if (super.isTransformableFrom(value)) {
//      return super.transformFrom(value);
//    }
//
//    throw new IllegalArgumentException(Messages.getString("BaseMatrix.26") + value); //$NON-NLS-1$
//  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexBalancedDecomposition balancedDecompose() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    final BalancedDecompositionDoubleComplexElements db = new DoubleComplexBalanceDecomposer().decompose(getRealPartDoubleElements(), getImaginaryPartDoubleElements());

    final double[][] dd = db.getD();
    final double[][] reBB = db.getReB();
    final double[][] imBB = db.getImB();

    final DoubleComplexMatrix d = new DoubleComplexMatrix(dd);
    final DoubleComplexMatrix b = new DoubleComplexMatrix(reBB, imBB);
    return new DoubleComplexBalancedDecomposition(d, b);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DoubleComplexMatrix leftDivide(final DoubleComplexMatrix value) {
    if (getRowSize() != value.getRowSize()) {
      throw new MatrixSizeException(this, value, Messages.getString("NumericalMatrix.0")); //$NON-NLS-1$
    }

    final DoubleComplexNumber norm = this.frobNorm();
    final DoubleNumber realNorm = norm.getRealPart();
    final double tolerance = realNorm.multiply(realNorm.getMachineEpsilon()).doubleValue();

    if (getRowSize() != getColumnSize()) {
      double[][][] ans = new DoubleComplexSingularValueDecomposer().leastSquare(getRealPartDoubleElements(), getImaginaryPartDoubleElements(), value.getRealPartDoubleElements(),
          value.getImaginaryPartDoubleElements(), tolerance);
      return new DoubleComplexMatrix(ans[0], ans[1]);
    }

    double[][][] ans = new DoubleComplexLUDecomposer().leftDivide(getRealPartDoubleElements(), getImaginaryPartDoubleElements(), value.getRealPartDoubleElements(),
        value.getImaginaryPartDoubleElements(), tolerance, false);
    return new DoubleComplexMatrix(ans[0], ans[1]);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexMatrix createUniformRandom(final int rowSize, final int columnSize) {
    DoubleComplexNumber[][] ans = DoubleComplexMatrixUtil.createUniformRandom(rowSize, columnSize);
    return ans[0][0].createGrid(rowSize, columnSize, ans);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexMatrix createUniformRandom(final int rowSize, final int columnSize, final long seed) {
    DoubleComplexNumber[][] ans = DoubleComplexMatrixUtil.createUniformRandom(rowSize, columnSize, seed);
    return ans[0][0].createGrid(rowSize, columnSize, ans);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexMatrix createNormalRandom(final int rowSize, final int columnSize) {
    final DoubleComplexNumber[][] ans = DoubleComplexMatrixUtil.createNormalRandom(rowSize, columnSize);
    return ans[0][0].createGrid(rowSize, columnSize, ans);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexMatrix createNormalRandom(final int rowSize, final int columnSize, final long seed) {
    final DoubleComplexNumber[][] ans = DoubleComplexMatrixUtil.createNormalRandom(rowSize, columnSize, seed, 2*seed);
    return ans[0][0].createGrid(rowSize, columnSize, ans);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int rank(final DoubleComplexNumber tolerance) {
    final DoubleComplexMatrix complexSingularValues = singularValue();
    final DoubleMatrix singularValues = complexSingularValues.getRealPart();
    
    int rank = 0;
    for (int i = 1; i <= singularValues.length(); i++) {
      if (singularValues.getElement(i).isGreaterThan(tolerance.getRealPart())) {
        rank++;
      }
    }
    
    return rank;
  }
  
  
  /**
   * {@inheritDoc}
   */
  @Override
  public DoubleComplexMatrix absElementWise() {
    final DoubleComplexNumber[][] ans = DoubleComplexMatrixUtil.absElementWise(getElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public DoubleComplexMatrix  frobNormRowWise() {
    final DoubleComplexNumber[][] ans =AbstractNumericalMatrixUtil.frobNormRowWise(getElements());
    return ans[0][0].createGrid(getRowSize(), Math.min(1, getColumnSize()), ans);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleMatrix getRealPart() {
    return new DoubleMatrix(DoubleComplexMatrixUtil.getRealPartElements(getElements()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleMatrix getImaginaryPart() {
    return new DoubleMatrix(DoubleComplexMatrixUtil.getImaginaryPartElements(getElements()));
  }

//  /**
//   * {@inheritDoc}
//   */
//  public DoubleComplexMatrix toComplex() {
//    return this;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public void setRealPart(IntMatrix realPart) {
//    DoubleComplexMatrixUtil.setRealPartElements(getElements(), realPart.getIntElements());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public void setImaginaryPart(IntMatrix imaginaryPart) {
//    DoubleComplexMatrixUtil.setImaginaryPartElements(getElements(), imaginaryPart.getIntElements());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public DoubleMatrix argumentElementWise() {
//    final DoubleNumber[][] ans = DoubleComplexMatrixUtil.argumentElementWise(getElements());
//    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
//  }
//  
//  /**
//   * {@inheritDoc}
//   */
//  public void setRealPart(DoubleMatrix realPart) {
//    DoubleComplexMatrixUtil.setRealPartElements(getElements(), realPart.getDoubleElements());
//  }
//  
//  /**
//   * {@inheritDoc}
//   */
//  public void setImaginaryPart(DoubleMatrix imaginaryPart) {
//    DoubleComplexMatrixUtil.setImaginaryPartElements(getElements(), imaginaryPart.getDoubleElements());
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public DoubleComplexMatrix create(DoubleMatrix rePart, DoubleMatrix imPart) {
//    return new DoubleComplexMatrix(rePart, imPart);
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public DoubleComplexMatrix create(DoubleMatrix rePart) {
//    return new DoubleComplexMatrix(rePart);
//  }

}