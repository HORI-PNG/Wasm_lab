/*
 * $Id: RationalPolynomialMatrix.java,v 1.139 2008/07/16 04:58:03 koga Exp $
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
import org.mklab.nfc.scalar.RationalPolynomial;


/**
 * 有理多項式({@link RationalPolynomial})を成分とする行列を表すクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.139 $
 */
public class DoubleRationalPolynomialMatrix extends AbstractSymbolicMatrix<DoubleRationalPolynomial,DoubleRationalPolynomialMatrix,DoubleNumber,DoubleMatrix> implements RealRationalPolynomialMatrix<DoublePolynomial,DoublePolynomialMatrix,DoubleComplexPolynomial, DoubleComplexPolynomialMatrix,DoubleRationalPolynomial,DoubleRationalPolynomialMatrix,DoubleComplexRationalPolynomial, DoubleComplexRationalPolynomialMatrix,DoubleNumber,DoubleMatrix,DoubleComplexNumber, DoubleComplexMatrix>, MatxObject {

  /** シリアルバージョン。 */
  private static final long serialVersionUID = -4226987180573680079L;

  /**
   * 新しく生成された<code>RationalPolynomialMatrix</code>オブジェクトを初期化します。
   */
  public DoubleRationalPolynomialMatrix() {
    this(0, 0);
  }

  /**
   * 新しく生成された<code>RationalPolynomialMatrix</code>オブジェクトを初期化します。
   * 
   * @param elements ベクトルの成分をもつ配列
   */
  public DoubleRationalPolynomialMatrix(final DoubleRationalPolynomial[] elements) {
    this(elements.length == 0 ? 0 : 1, elements.length, elements.length == 0 ? new DoubleRationalPolynomial[0][0] : new DoubleRationalPolynomial[][] {elements});
  }

  /**
   * 新しく生成された<code>RationalPolynomialMatrix</code>オブジェクトを初期化します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   */
  public DoubleRationalPolynomialMatrix(final int rowSize, final int columnSize) {
    this(rowSize, columnSize, (String)null);
  }

  /**
   * 新しく生成された<code>RationalPolynomialMatrix</code>オブジェクトを初期化します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param variableName 変数名
   */
  public DoubleRationalPolynomialMatrix(final int rowSize, final int columnSize, final String variableName) {
    super(rowSize, columnSize, DoubleRationalPolynomial.createZeroArray(rowSize, columnSize, variableName));
  }

  /**
   * 新しく生成された<code>RationalPolynomialMatrix</code>オブジェクトを初期化します。
   * 
   * @param elements 行列の成分をもつ配列
   */
  public DoubleRationalPolynomialMatrix(final DoubleRationalPolynomial[][] elements) {
    this(elements.length, (elements.length == 0 || elements[0] == null) ? 0 : elements[0].length, elements);
  }

  /**
   * 新しく生成された<code>RationalPolynomialMatrix</code>オブジェクトを初期化します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 行列の成分を持つ配列
   */
  public DoubleRationalPolynomialMatrix(final int rowSize, final int columnSize, final DoubleRationalPolynomial[][] elements) {
    super(rowSize, columnSize, elements);
  }

  /**
   * 新しく生成された<code>RationalPolynomialMatrix</code>オブジェクトを初期化します。
   * 
   * @param matrix 整数行列
   */
  public DoubleRationalPolynomialMatrix(final IntMatrix matrix) {
    this(DoubleRationalPolynomialMatrixUtil.createArray(matrix.getIntElements()));
  }

  /**
   * 新しく生成された<code>RationalPolynomialMatrix</code>オブジェクトを初期化します。
   * 
   * @param matrix 実数行列
   */
  public DoubleRationalPolynomialMatrix(final DoubleMatrix matrix) {
    this(DoubleRationalPolynomialMatrixUtil.createArray(matrix.getDoubleElements()));
  }

//  /**
//   * 新しく生成された<code>RationalPolynomialMatrix</code>オブジェクトを初期化します。
//   * 
//   * @param matrix 複素行列
//   */
//  public DoubleRationalPolynomialMatrix(final DoubleMatrix matrix) {
//    this(DoubleRationalPolynomialMatrixUtil.createArray(matrix.getElements()));
//  }

  /**
   * 新しく生成された<code>RationalPolynomialMatrix</code>オブジェクトを初期化します。
   * 
   * @param numerators 分子多項式行列
   */
  public DoubleRationalPolynomialMatrix(final DoublePolynomialMatrix numerators) {
    this(DoubleRationalPolynomialMatrixUtil.createArray(numerators.getElements()));
  }

  /**
   * 新しく生成された<code>RationalPolynomialMatrix</code>オブジェクトを初期化します。
   * 
   * @param numerators 分子多項式行列
   * @param denominators 分母多項式行列
   */
  public DoubleRationalPolynomialMatrix(final DoublePolynomialMatrix numerators, DoublePolynomialMatrix denominators) {
    this(DoubleRationalPolynomialMatrixUtil.createArray(numerators.getElements(), denominators.getElements()));
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
//    if (value instanceof IntMatrix) {
//      return true;
//    }
//
//    if (value instanceof DoubleMatrix) {
//      return true;
//    }
//
//    if (value instanceof PolynomialMatrix) {
//      return true;
//    }
//
////    if (value instanceof RationalPolynomialMatrix) {
////      return true;
////    }
//
//    if (value instanceof BaseMatrix<?,?>) {
//      return true;
//    }
//
//    return false;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public DoubleRationalPolynomialMatrix transformFrom(final Matrix<?,?> value) {
//    if (super.isTransformableFrom(value)) {
//      return super.transformFrom(value);
//    }
//
//    if (value instanceof IntMatrix) {
//      return new DoubleRationalPolynomialMatrix((IntMatrix)value);
//    }
//
//    if (value instanceof DoubleMatrix) {
//      return new DoubleRationalPolynomialMatrix((DoubleMatrix)value);
//    }
//
//    if (value instanceof DoublePolynomialMatrix) {
//      return new DoubleRationalPolynomialMatrix((DoublePolynomialMatrix)value);
//    }
//
////    if (value instanceof RationalPolynomialMatrix) {
////      return (RationalPolynomialMatrix)value.clone();
////    }
//
////    if (value instanceof DoubleNumberMatrix) {
////      return new DoubleRationalPolynomialMatrix((DoubleNumberMatrix)value);
////    }
//
//    throw new IllegalArgumentException(Messages.getString("RationalPolynomialMatrix.0") + value); //$NON-NLS-1$
//  }

  /**
   * 単位行列を生成します。
   * 
   * @param size 行列の大きさ
   * @return 単位行列
   */
  public static DoubleRationalPolynomialMatrix unit(final int size) {
    return unit(size, size);
  }

  /**
   * 単位行列を生成します。
   * 
   * @param size 行列の大きさ
   * @param variableName 変数名
   * @return 単位行列
   */
  public static DoubleRationalPolynomialMatrix unit(final int size, final String variableName) {
    return unit(size, size, variableName);
  }

  /**
   * 単位行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 単位行列
   */
  public static DoubleRationalPolynomialMatrix unit(final int rowSize, final int columnSize) {
    return unit(rowSize, columnSize, (String)null);
  }

  /**
   * 単位行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param variableName 変数名
   * @return 単位行列
   */
  public static DoubleRationalPolynomialMatrix unit(final int rowSize, final int columnSize, final String variableName) {
    final DoubleRationalPolynomial[][] ans = DoubleRationalPolynomialMatrixUtil.unit(rowSize, columnSize, variableName);
    return new DoubleRationalPolynomialMatrix(ans);
  }

  /**
   * 全ての成分が１である正方行列を生成します。
   * 
   * @param size 行列の大きさ
   * @return 全ての成分が１である正方行列
   */
  public static DoubleRationalPolynomialMatrix ones(final int size) {
    return ones(size, size);
  }

  /**
   * 全ての成分が１である正方行列を生成します。
   * 
   * @param size 行列の大きさ
   * @param variableName 変数名
   * @return 全ての成分が１である正方行列
   */
  public static DoubleRationalPolynomialMatrix ones(final int size, final String variableName) {
    return ones(size, size, variableName);
  }

  /**
   * 全ての成分が１である行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 全ての成分が１である行列
   */
  public static DoubleRationalPolynomialMatrix ones(final int rowSize, final int columnSize) {
    return ones(rowSize, columnSize, (String)null);
  }

  /**
   * 全ての成分が1である行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param variableName 変数名
   * @return 全ての成分が１である行列
   */
  public static DoubleRationalPolynomialMatrix ones(final int rowSize, final int columnSize, final String variableName) {
    final DoubleRationalPolynomial[][] ans = DoubleRationalPolynomialMatrixUtil.ones(rowSize, columnSize, variableName);
    return new DoubleRationalPolynomialMatrix(ans);
  }

  /**
   * 対角行列を生成します。
   * 
   * @param diagonalElement 対角成分
   * @return 対角行列
   */
  public static DoubleRationalPolynomialMatrix diagonal(final DoubleRationalPolynomial[] diagonalElement) {
    final DoubleRationalPolynomial[][] ans = GridUtil.vectorToDiagonal(diagonalElement);
    return new DoubleRationalPolynomialMatrix(ans);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomialMatrix divide(DoublePolynomial polynomial) {
    final DoubleRationalPolynomial[][] ans = DoubleRationalPolynomialMatrixUtil.divide(getElements(), polynomial);
    return new DoubleRationalPolynomialMatrix(ans);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomialMatrix multiply(DoublePolynomial polynomial) {
    final DoubleRationalPolynomial[][] ans = DoubleRationalPolynomialMatrixUtil.multiply(getElements(), polynomial);
    return new DoubleRationalPolynomialMatrix(ans);
  }

  
  /**
   * 成分毎の分子多項式を成分とする行列を返します。
   * 
   * @return 成分毎の分子多項式を成分とする行列
   */
  public DoublePolynomialMatrix getNumeratorElementWise() {
    final DoublePolynomial[][] ans = DoubleRationalPolynomialMatrixUtil.getNumeratorElementWise(getElements());
    return new DoublePolynomialMatrix(ans);
  }

  /**
   * 成分毎の分母多項式を成分とする行列を返します。
   * 
   * @return 成分毎の分母多項式を成分とする行列
   */
  public DoublePolynomialMatrix getDenominatorElementWise() {
    final DoublePolynomial[][] ans = DoubleRationalPolynomialMatrixUtil.getDenominatorElementWise(getElements());
    return new DoublePolynomialMatrix(ans);
  }

  /**
   * 成分毎の商多項式を成分とする行列を返します。
   * 
   * @return 成分毎の商多項式を成分とする行列
   */
  public DoublePolynomialMatrix getQuotientElementWise() {
    final DoublePolynomial[][] ans = DoubleRationalPolynomialMatrixUtil.getQuotientElementWise(getElements());
    return new DoublePolynomialMatrix(ans);
  }

  /**
   * 成分毎の剰余多項式を成分とする行列を返します。
   * 
   * @return 成分毎の剰余多項式を成分とする行列
   */
  public DoublePolynomialMatrix getRemainderElementWise() {
    final DoublePolynomial[][] ans = DoubleRationalPolynomialMatrixUtil.getRemainderElementWise(getElements());
    return new DoublePolynomialMatrix(ans);
  }

  /**
   * {@inheritDoc}
   */
  public void writeMxFormat(final DataOutputStream output, final String name) throws IOException {
    DoubleRationalPolynomialMatrixUtil.writeMxFormat(getElements(), output, name);
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
   * 入力ストリームからMX形式のデータを読み込みます。
   * 
   * @param input 入力ストリーム
   * @param head ヘッダー
   * @return 読み込んだデータ
   * @throws IOException 入力ストリームから読み込みない場合
   */
  public static DoubleRationalPolynomialMatrix readMxFormat(final InputStream input, final MxDataHead head) throws IOException {
    final DoubleRationalPolynomialMatrix ans = new DoubleRationalPolynomialMatrix(DoubleRationalPolynomialMatrixUtil.readMxFormat(input, head));
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public String toMmString() {
    final String ans = DoubleRationalPolynomialMatrixUtil.toMmString(getElements(), getElementFormat());
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public String toMmString(final String format) {
    final String ans = DoubleRationalPolynomialMatrixUtil.toMmString(getElements(), format);
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public void writeMmFormat(final File file, final String name) throws IOException {
    try (final Writer output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")))) { //$NON-NLS-1$
      writeMmFormat(output, name, true);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void writeMmFormat(final Writer output, final String name, final boolean withNewLine) throws IOException {
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
   * {@inheritDoc}
   */
  @Override
  protected String getGridClassName() {
    return "RaMatrix"; //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void printElements(final Writer output, final int maxColumnSize) {
    DoubleRationalPolynomialMatrixUtil.print(getElements(), output, getElementFormat(), maxColumnSize);
  }

//  /**
//   * 1個の有理多項式について、行列の各成分の累乗を求めます。
//   * 
//   * @param scalar 累乗の対象
//   * @param matrix 累乗の指数を成分とする行列
//   * @return 累乗の結果
//   */
//  public static DoubleRationalPolynomialMatrix powerElementWise(final DoubleRationalPolynomial scalar, final Matrix<?,?> matrix) {
//    if (matrix instanceof IntMatrix) {
//      return powerElementWise(scalar, matrix);
//    }
//
//    throw new IllegalArgumentException();
//  }

  /**
   * 1個の有理多項式について、行列の各成分の累乗を求めます。
   * 
   * @param scalar 累乗の対象
   * @param matrix 累乗の指数を成分とする行列
   * @return 累乗の結果
   */
  public static DoubleRationalPolynomialMatrix powerElementWise(final DoubleRationalPolynomial scalar, final IntMatrix matrix) {
    return new DoubleRationalPolynomialMatrix(BaseMatrixUtil.powerElementWise(scalar, matrix.getIntElements()));
  }
  
//  /**
//   * Generates PolynomialMatrix.
//   * 
//   * @return PolynomialMatrix
//   */
//  public DoubleRationalPolynomialMatrix toRationalPolynomialMatrix() {
//    final DoubleRationalPolynomial[][] elements = getElements();
//    final DoubleRationalPolynomial[][] rationalPolynomials = new DoubleRationalPolynomial[getRowSize()][getColumnSize()];
//    for (int i = 0; i < getRowSize(); i++) {
//      for (int j = 0; j < getColumnSize(); j++) {
//        rationalPolynomials[i][j] = new DoubleRationalPolynomial(elements[i][j]);
//      }
//    }
//
//    return new DoubleRationalPolynomialMatrix(rationalPolynomials);
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
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix toComplex() {
    return new DoubleComplexRationalPolynomialMatrix(this);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix add(DoubleComplexRationalPolynomialMatrix value) {
    return toComplex().add(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix subtract(DoubleComplexRationalPolynomialMatrix value) {
    return toComplex().subtract(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix multiply(DoubleComplexRationalPolynomialMatrix value) {
    return toComplex().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix divide(DoubleComplexRationalPolynomialMatrix value) {
    return toComplex().divide(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix leftDivide(DoubleComplexRationalPolynomialMatrix value) {
    return toComplex().leftDivide(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix multiply(DoubleComplexRationalPolynomial value) {
    return toComplex().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix divide(DoubleComplexRationalPolynomial value) {
    return toComplex().divide(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix leftDivide(DoubleComplexRationalPolynomial value) {
    return toComplex().leftDivide(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix appendDown(DoubleComplexRationalPolynomialMatrix value) {
    return toComplex().appendDown(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix appendRight(DoubleComplexRationalPolynomialMatrix value) {
    return toComplex().appendRight(value);
  }

//  /**
//   * 倍精度有理多項式行列を有理多項式行列に変換します。
//   * 
//   * @param matrix 倍精度有理多項式行列
//   * @return 有理多項式行列
//   */
//  public static DoubleRationalPolynomial[][] createArray(final DoubleRationalPolynomial[][] matrix) {
//    final int rowSize = matrix.length;
//    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
//  
//    final DoubleRationalPolynomial[][] ans = new DoubleRationalPolynomial[rowSize][columnSize];
//    for (int i = 0; i < rowSize; i++) {
//      final DoubleRationalPolynomial[] ansi = ans[i];
//      final DoubleRationalPolynomial[] matrixi = matrix[i];
//      for (int j = 0; j < columnSize; j++) {
//        final DoublePolynomial num = new DoublePolynomial(matrixi[j].getNumerator().getCoefficients());
//        final DoublePolynomial den = new DoublePolynomial(matrixi[j].getDenominator().getCoefficients());
//        ansi[j] = new DoubleRationalPolynomial(num, den);
//      }
//    }
//    return ans;
//  }
//
//  /**
//   * 倍精度有理多項式行列を有理多項式行列に変換します。
//   * 
//   * @param matrix 倍精度有理多項式行列
//   * @return 有理多項式行列
//   */
//  public static DoubleRationalPolynomial[] createArray(final DoubleRationalPolynomial[] matrix) {
//    final int rowSize = matrix.length;
//  
//    final DoubleRationalPolynomial[] ans = new DoubleRationalPolynomial[rowSize];
//        
//    for (int i = 0; i < rowSize; i++) {
//      final DoublePolynomial num = new DoublePolynomial(matrix[i].getNumerator().getCoefficients());
//      final DoublePolynomial den = new DoublePolynomial(matrix[i].getDenominator().getCoefficients());
//      ans[i] = new DoubleRationalPolynomial(num, den);
//    }
//    return ans;
//  }

//  /**
//   * 倍精度有理多項式行列を有理多項式行列に変換します。
//   * 
//   * @param matrix 倍精度有理多項式行列
//   * @return 有理多項式行列
//   */
//  public static DoubleRationalPolynomial[][] createArray(final DoublePolynomial[][] matrix) {
//    final int rowSize = matrix.length;
//    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
//  
//    final DoubleRationalPolynomial[][] ans = new DoubleRationalPolynomial[rowSize][columnSize];
//    for (int i = 0; i < rowSize; i++) {
//      final DoubleRationalPolynomial[] ansi = ans[i];
//      final DoublePolynomial[] matrixi = matrix[i];
//      for (int j = 0; j < columnSize; j++) {
//        final DoublePolynomial num = new DoublePolynomial(matrixi[j].getCoefficients());
//        final DoublePolynomial den = new DoublePolynomial(1);
//        ansi[j] = new DoubleRationalPolynomial(num, den);
//      }
//    }
//    return ans;
//  }

//  /**
//   * 倍精度有理多項式行列を有理多項式行列に変換します。
//   * 
//   * @param numerators 分子多項式行列
//   * @return 有理多項式行列
//   */
//  public static DoubleRationalPolynomial[] createArray(final DoublePolynomial[] numerators) {
//    final int rowSize = numerators.length;
//  
//    final DoubleRationalPolynomial[] ans = new DoubleRationalPolynomial[rowSize];
//        
//    for (int i = 0; i < rowSize; i++) {
//      final DoublePolynomial num = new DoublePolynomial(numerators[i].getCoefficients());
//      final DoublePolynomial den = new DoublePolynomial(1);
//      ans[i] = new DoubleRationalPolynomial(num, den);
//    }
//    return ans;
//  }

  
}