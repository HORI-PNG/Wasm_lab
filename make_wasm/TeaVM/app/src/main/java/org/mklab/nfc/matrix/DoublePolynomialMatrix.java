/*
 * $Id: PolynomialMatrix.java,v 1.144 2008/07/16 04:58:03 koga Exp $
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
import java.io.Writer;
import java.nio.charset.Charset;

import org.mklab.nfc.matx.MatxObject;
import org.mklab.nfc.matx.MxDataHead;
import org.mklab.nfc.scalar.DoubleComplexNumber;
import org.mklab.nfc.scalar.DoubleComplexPolynomial;
import org.mklab.nfc.scalar.DoubleComplexRationalPolynomial;
import org.mklab.nfc.scalar.DoubleNumber;
import org.mklab.nfc.scalar.DoublePolynomial;
import org.mklab.nfc.scalar.DoubleRationalPolynomial;
import org.mklab.nfc.scalar.Polynomial;


/**
 * 多項式({@link Polynomial})を成分とする行列を表わすクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.144 $
 */
public class DoublePolynomialMatrix extends AbstractSymbolicMatrix<DoublePolynomial, DoublePolynomialMatrix, DoubleNumber,DoubleMatrix> implements RealPolynomialMatrix<DoublePolynomial,DoublePolynomialMatrix,DoubleComplexPolynomial,DoubleComplexPolynomialMatrix,DoubleRationalPolynomial,DoubleRationalPolynomialMatrix,DoubleComplexRationalPolynomial, DoubleComplexRationalPolynomialMatrix,DoubleNumber,DoubleMatrix,DoubleComplexNumber,DoubleComplexMatrix>, MatxObject {

  /** シリアルバージョン。 */
  private static final long serialVersionUID = -306639853710050430L;

  /**
   * 0*0の多項式行列を作成します。
   */
  public DoublePolynomialMatrix() {
    this(0, 0);
  }

  /**
   * <code>elements</code>で与えられた横ベクトルを生成します。
   * 
   * @param elements ベクトルの成分をもつ配列
   */
  public DoublePolynomialMatrix(final DoublePolynomial[] elements) {
    this(elements.length == 0 ? 0 : 1, elements.length, elements.length == 0 ? new DoublePolynomial[0][0] : new DoublePolynomial[][] {elements});
  }

  /**
   * <code>rowSize&nbsp;*&nbsp;columnSize</code> の多項式行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   */
  public DoublePolynomialMatrix(final int rowSize, final int columnSize) {
    this(rowSize, columnSize, (String)null);
  }

  /**
   * <code>rowSize&nbsp;*&nbsp;columnSize</code> の 多項式行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param variableName 変数の名前
   */
  public DoublePolynomialMatrix(final int rowSize, final int columnSize, final String variableName) {
    super(rowSize, columnSize, DoublePolynomial.createZeroArray(rowSize, columnSize, variableName));
  }

  /**
   * <code>elements</code>で与えられた成分をもつ多項式行列を生成します。
   * 
   * @param elements 行列の成分をもつ配列
   */
  public DoublePolynomialMatrix(final DoublePolynomial[][] elements) {
    this(elements.length, (elements.length == 0 || elements[0] == null) ? 0 : elements[0].length, elements);
  }

  /**
   * 新しく生成された<code>PolynomialMatrix</code>オブジェクトを初期化します。
   * 
   * @param matrix 整数行列
   */
  public DoublePolynomialMatrix(final IntMatrix matrix) {
    this(DoublePolynomialMatrixUtil.createArray(matrix.getIntElements()));
  }

  /**
   * 新しく生成された<code>PolynomialMatrix</code>オブジェクトを初期化します。
   * 
   * @param matrix 実数行列
   */
  public DoublePolynomialMatrix(final DoubleMatrix matrix) {
    this(DoublePolynomialMatrixUtil.createArray(matrix.getDoubleElements()));
  }

  /**
   * 新しく生成された<code>PolynomialMatrix</code>オブジェクトを初期化します。
   * 
   * @param matrix 実数行列
   * @param variable 多項式変数
   */
  public DoublePolynomialMatrix(final DoubleMatrix matrix, String variable) {
    this(DoublePolynomialMatrixUtil.createArray(matrix.getDoubleElements(), variable));
  }

  //  /**
  //   * 新しく生成された<code>PolynomialMatrix</code>オブジェクトを初期化します。
  //   * @param matrix 複素行列
  //   */
  //  public DoublePolynomialMatrix(final BaseMatrix<?, ?> matrix) {
  //    this(DoublePolynomialMatrixUtil.createArray(matrix.getElements()));
  //  }

  /**
   * <code>elements</code>で与えられた成分をもつ <code>rowSize&nbsp;*&nbsp;colSize</code> の 多項式行列を作成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 行列の成分をもつ配列
   */
  public DoublePolynomialMatrix(final int rowSize, final int columnSize, final DoublePolynomial[][] elements) {
    super(rowSize, columnSize, elements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public boolean isTransformableFrom(final Matrix<?, ?> value) {
//    if (super.isTransformableFrom(value)) {
//      return true;
//    }
//
//    if (value instanceof IntMatrix) {
//      return true;
//    }
//
//    if (value instanceof DoubleMatrix) {
//      return true;
//    }
//
//    //    if (value instanceof PolynomialMatrix) {
//    //      return true;
//    //    }
//
//    if (value instanceof RationalPolynomialMatrix) {
//      return false;
//    }
//
//    //    if (value instanceof BaseMatrix<?,?>) {
//    //      return true;
//    //    }
//
//    return false;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public DoublePolynomialMatrix transformFrom(final Matrix<?, ?> value) {
//    if (super.isTransformableFrom(value)) {
//      return super.transformFrom(value);
//    }
//
//    if (value instanceof IntMatrix) {
//      return new DoublePolynomialMatrix((IntMatrix)value);
//    }
//
//    if (value instanceof DoubleMatrix) {
//      return new DoublePolynomialMatrix((DoubleMatrix)value);
//    }
//
//    //    if (value instanceof PolynomialMatrix) {
//    //      return (PolynomialMatrix)value.clone();
//    //    }
//
//    //    if (value instanceof BaseMatrix<?,?>) {
//    //      return new DoublePolynomialMatrix((BaseMatrix<?,?>)value);
//    //    }
//
//    throw new IllegalArgumentException(Messages.getString("PolynomialMatrix.0") + value); //$NON-NLS-1$
//  }

  /**
   * 単位行列を生成します。
   * 
   * @param size 行列の大きさ
   * @return 単位行列
   */
  public static DoublePolynomialMatrix unit(final int size) {
    return unit(size, size);
  }

  /**
   * 単位行列を生成します。
   * 
   * @param size 行列の大きさ
   * @param variableName 変数の名前
   * @return 単位行列
   */
  public static DoublePolynomialMatrix unit(final int size, final String variableName) {
    return unit(size, size, variableName);
  }

  /**
   * 単位行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 単位行列
   */
  public static DoublePolynomialMatrix unit(final int rowSize, final int columnSize) {
    return unit(rowSize, columnSize, (String)null);
  }

  /**
   * 単位行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param variableName 変数の名前
   * @return 単位行列
   */
  public static DoublePolynomialMatrix unit(final int rowSize, final int columnSize, final String variableName) {
    return new DoublePolynomialMatrix(DoublePolynomialMatrixUtil.unit(rowSize, columnSize, variableName));
  }

  /**
   * 全成分1の行列を生成します。
   * 
   * @param size 行列の大きさ
   * @return 全成分1の行列
   */
  public static DoublePolynomialMatrix ones(final int size) {
    return ones(size, size);
  }

  /**
   * 全成分1の行列を生成します。
   * 
   * @param size 行列の大きさ
   * @param variableName 変数の名前
   * @return 全成分1の行列
   */
  public static DoublePolynomialMatrix ones(final int size, final String variableName) {
    return ones(size, size, variableName);
  }

  /**
   * 全成分1の行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 全成分1の行列
   */
  public static DoublePolynomialMatrix ones(final int rowSize, final int columnSize) {
    return ones(rowSize, columnSize, (String)null);
  }

  /**
   * 全成分1の行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param variableName 変数の名前
   * @return 全成分1の行列
   */
  public static DoublePolynomialMatrix ones(final int rowSize, final int columnSize, final String variableName) {
    return new DoublePolynomialMatrix(DoublePolynomialMatrixUtil.ones(rowSize, columnSize, variableName));
  }

  /**
   * 各成分の1階不定積分を返します。
   * 
   * @return 多項式行列の1階不定積分
   */
  public DoublePolynomialMatrix integral() {
    return integral(1);
  }

  /**
   * 各成分の不定積分を返します。
   * 
   * @param order 階数
   * @return 多項式行列の不定積分
   */
  public DoublePolynomialMatrix integral(final int order) {
    return new DoublePolynomialMatrix(getRowSize(), getColumnSize(), DoublePolynomialMatrixUtil.integral(getElements(), order));
  }

  /**
   * {@inheritDoc}
   */
  public void writeMxFormat(final DataOutputStream output, final String name) throws IOException {
    DoublePolynomialMatrixUtil.writeMxFormat(getElements(), output, name);
  }

  /**
   * {@inheritDoc}
   */
  public void writeMxFormat(final File file, final String name) throws IOException {
    try (DataOutputStream output = new DataOutputStream(new FileOutputStream(file))) {
      writeMxFormat(output, name);
    }
  }

  /**
   * {@link org.mklab.nfc.matx.MatxMatrix#readMxFormat(InputStream)} から呼ばれる中間メソッドです。
   * 
   * <p>このメソッドは直接使わず
   * 
   * <blockquote><code> {@link org.mklab.nfc.matrix.Matrix}A = Matrix. {@link org.mklab.nfc.matx.MatxMatrix#readMxFormat(InputStream)} </code></blockquote>
   * 
   * の形で使用してください。
   * 
   * @param input 入力ストリーム
   * 
   * @param head ヘッダー
   * 
   * @return mxファイルから読み込み,生成された行列
   * @exception IOException 入力ストリームから読み込めない場合
   */
  public static DoublePolynomialMatrix readMxFormat(final InputStream input, final MxDataHead head) throws IOException {
    return new DoublePolynomialMatrix(DoublePolynomialMatrixUtil.readMxFormat(input, head));
  }

  /**
   * {@inheritDoc}
   */
  public String toMmString() {
    return DoublePolynomialMatrixUtil.toMmString(getElements(), getElementFormat());
  }

  /**
   * {@inheritDoc}
   */
  public String toMmString(final String format) {
    return DoublePolynomialMatrixUtil.toMmString(getElements(), format);
  }

  /**
   * {@inheritDoc}
   */
  public void writeMmFormat(final File file, final String name) throws IOException {
    try (Writer output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")))) { //$NON-NLS-1$
      writeMmFormat(output, name, true);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void writeMmFormat(final Writer output, final String name, final boolean withStatementSeparator) throws IOException {
    final String newLine = System.getProperty("line.separator"); //$NON-NLS-1$

    final StringBuffer sb = new StringBuffer();
    if (name.length() != 0) {
      sb.append(name);
      sb.append(" = "); //$NON-NLS-1$
      sb.append(newLine);
    }

    sb.append(toMmString());

    if (withStatementSeparator) {
      sb.append(";"); //$NON-NLS-1$
      sb.append(newLine);
      sb.append(newLine);
    }

    output.write(sb.toString());
    output.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getGridClassName() {
    return "PoMatrix"; //$NON-NLS-1$
  }

//  /**
//   * 1個の多項式について、行列の各成分の累乗を求めます。
//   * 
//   * @param scalar 累乗の対象
//   * @param matrix 累乗の指数を成分とする行列
//   * @return 累乗の結果
//   */
//  public static DoublePolynomialMatrix powerElementWise(final DoublePolynomial scalar, final Matrix<?, ?> matrix) {
//    if (matrix instanceof IntMatrix) {
//      return powerElementWise(scalar, matrix);
//    }
//    throw new IllegalArgumentException();
//  }

  /**
   * 1個の多項式について、行列の各成分の累乗を求めます。
   * 
   * @param scalar 累乗の対象
   * @param matrix 累乗の指数を成分とする行列
   * @return 累乗の結果
   */
  public static DoublePolynomialMatrix powerElementWise(final DoublePolynomial scalar, final IntMatrix matrix) {
    return new DoublePolynomialMatrix(BaseMatrixUtil.powerElementWise(scalar, matrix.getIntElements()));
  }

  /**
   * 対角行列を生成します。
   * 
   * @param diagonalElements 対角成分
   * @return 対角行列
   */
  public static DoublePolynomialMatrix diagonal(final DoublePolynomial[] diagonalElements) {
    final DoublePolynomial[][] ans = GridUtil.vectorToDiagonal(diagonalElements);
    return new DoublePolynomialMatrix(ans);
  }

//  /**
//   * Generates PolynomialMatrix.
//   * 
//   * @return PolynomialMatrix
//   */
//  public PolynomialMatrix<DoubleNumber, DoubleMatrix> toPolynomialMatrix() {
//    final DoublePolynomial[][] elements = getElements();
//    final Polynomial<DoubleNumber, DoubleMatrix>[][] polynomials = new Polynomial[getRowSize()][getColumnSize()];
//    for (int i = 0; i < getRowSize(); i++) {
//      for (int j = 0; j < getColumnSize(); j++) {
//        polynomials[i][j] = new Polynomial<>(elements[i][j].getCoefficients());
//      }
//    }
//
//    return new PolynomialMatrix<>(polynomials);
//  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DoubleMatrix evaluate(final DoubleMatrix value) {
    final int argRowSize = value.getRowSize();
    final int argColumnSize = value.getColumnSize();

    final DoubleMatrix unit = evaluate(value.getDoubleElement(1, 1));
    final DoubleMatrix ans = unit.createZero(getRowSize() * argRowSize, getColumnSize() * argColumnSize);

    for (int row = 1; row <= argRowSize; row++) {
      for (int column = 1; column <= argColumnSize; column++) {
        ans.setSubMatrix(row, column, this, evaluate(value.getDoubleElement(row, column)));
      }
    }

    return ans;
  }

  /**
   * 各成分の不定積分を返します。
   * 
   * @param matrix 対象となる行列
   * @param order 階数
   * @return 多項式行列の不定積分
   */
  public static DoublePolynomial[][] integral(final DoublePolynomial[][] matrix, final int order) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  
    final DoublePolynomial[][] ans = GridUtil.<DoublePolynomial> createArray(rowSize, columnSize, matrix);
  
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = (matrix[i][j]).integral(order);
      }
    }
    return ans;
  }

//  /**
//   * 倍精度多項式行列を多項式行列(元の行列を定数項とする)に変換します。
//   * 
//   * @param matrix 倍精度多項式行列
//   * @return 多項式行列
//   */
//  public static DoublePolynomial[][] createArray(final DoublePolynomial[][] matrix) {
//    final int rowSize = matrix.length;
//    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
//  
//    final DoublePolynomial[][] ans = new DoublePolynomial[rowSize][columnSize];
//    for (int i = 0; i < rowSize; i++) {
//      final DoublePolynomial[] matrixi = matrix[i];
//      final DoublePolynomial[] ansi = ans[i];
//      for (int j = 0; j < columnSize; j++) {
//        ansi[j] = new DoublePolynomial(matrixi[j].getCoefficients());
//      }
//    }
//    return ans;
//  }

  /**
   * 倍精度多項式行列を多項式行列(元の行列を定数項とする)に変換します。
   * 
   * @param matrix 倍精度多項式行列
   * @return 多項式行列
   */
  public static DoublePolynomial[][] createArray(final DoublePolynomialMatrix matrix) {
    return matrix.getElements();
    //return DoublePolynomialMatrix.createArray(matrix.getElements());    
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix toComplex() {
    return new DoubleComplexPolynomialMatrix(this);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomialMatrix toRational() {
    return new DoubleRationalPolynomialMatrix(this);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix add(DoubleComplexPolynomialMatrix value) {
    return toComplex().add(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix subtract(DoubleComplexPolynomialMatrix value) {
    return toComplex().subtract(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix multiply(DoubleComplexPolynomialMatrix value) {
    return toComplex().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix divide(DoubleComplexPolynomialMatrix value) {
    return toComplex().divide(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix leftDivide(DoubleComplexPolynomialMatrix value) {
    return toComplex().leftDivide(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix multiply(DoubleComplexPolynomial value) {
    return toComplex().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix divide(DoubleComplexPolynomial value) {
    return toComplex().divide(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix leftDivide(DoubleComplexPolynomial value) {
    return toComplex().leftDivide(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix appendDown(DoubleComplexPolynomialMatrix value) {
    return toComplex().appendDown(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix appendRight(DoubleComplexPolynomialMatrix value) {
    return toComplex().appendRight(value);
  }

//  /**
//   * 倍精度多項式行列を多項式行列(元の行列を定数項とする)に変換します。
//   * 
//   * @param matrix 倍精度多項式行列
//   * @return 多項式行列
//   */
//  public static DoublePolynomial[] createArray(final DoublePolynomial[] matrix) {
//    final int size = matrix.length;
//  
//    final DoublePolynomial[] ans = new DoublePolynomial[size];
//    for (int i = 0; i < size; i++) {
//      ans[i] = new DoublePolynomial(matrix[i].getCoefficients());
//    }
//    return ans;
//  }
  
//  /**
//   * {@link org.mklab.nfc.matx.MatxMatrix#readMxFormat(InputStream)} から呼ばれる中間メソッドです。
//   * 
//   * <p>このメソッドは直接使わず
//   * 
//   * <blockquote><code> {@link org.mklab.nfc.matrix.Matrix}A = Matrix. {@link org.mklab.nfc.matx.MatxMatrix#readMxFormat(InputStream)} </code></blockquote>
//   * 
//   * の形で使用してください。
//   * 
//   * @param input 入力ストリーム
//   * 
//   * @param head ヘッダー
//   * 
//   * @return mxファイルから読み込み,生成された行列
//   * @exception IOException 入力ストリームから読み込めない場合
//   */
//  public static DoublePolynomialMatrix readMxFormat(final InputStream input, final MxDataHead head) throws IOException {
//    final DoublePolynomial[][] elements = DoublePolynomialMatrixUtil.readMxFormat(input, head);
//    final int rowSize = elements == null ? 0 : elements.length;
//    final int columnSize = rowSize == 0 ? 0 : elements[0].length;
//    return elements[0][0].createGrid(rowSize, columnSize, elements);
//  }

}