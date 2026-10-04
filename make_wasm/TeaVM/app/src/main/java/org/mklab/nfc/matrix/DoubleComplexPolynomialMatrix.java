/**
 * Copyright (C) 2021 MKLab.org (Koga Laboratory)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mklab.nfc.matrix;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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


/**
 * {@link DoubleComplexNumber}を係数とする多項式を成分とする行列です。
 * 
 * @author koga
 * @version $Revision$, 2021/07/15
 */
public class DoubleComplexPolynomialMatrix extends AbstractSymbolicMatrix<DoubleComplexPolynomial, DoubleComplexPolynomialMatrix,DoubleComplexNumber,DoubleComplexMatrix> implements ComplexPolynomialMatrix<DoublePolynomial,DoublePolynomialMatrix,DoubleComplexPolynomial,DoubleComplexPolynomialMatrix,DoubleRationalPolynomial,DoubleRationalPolynomialMatrix,DoubleComplexRationalPolynomial, DoubleComplexRationalPolynomialMatrix,DoubleNumber,DoubleMatrix,DoubleComplexNumber,DoubleComplexMatrix>, MatxObject {

  /** */
  private static final long serialVersionUID = -1567470028205326521L;

  /**
   * Creates {@link DoubleComplexPolynomialMatrix}.
   * 
   * @param matrix 0次成分行列
   */
  public DoubleComplexPolynomialMatrix(DoublePolynomialMatrix matrix) {
    this(DoublePolynomialMatrixUtil.createComplexArray(matrix.getElements()));
  }

  /**
   * Creates {@link DoubleComplexPolynomialMatrix}.
   * 
   * @param rePart 実部行列
   * @param imPart 虚部行列
   */
  public DoubleComplexPolynomialMatrix(DoublePolynomialMatrix rePart, DoublePolynomialMatrix imPart) {
    this(DoublePolynomialMatrixUtil.createComplexArray(rePart.getElements(), imPart.getElements()));
  }

//  /**
//   * Creates {@link DoubleComplexPolynomialMatrix}.
//   * 
//   * @param matrix 倍精度複素行列
//   */
//  public DoubleComplexPolynomialMatrix(DoubleComplexPolynomialMatrix matrix) {
//    this(matrix.getElements());
//  }

  /**
   * Creates {@link DoubleComplexPolynomialMatrix}.
   * 
   * @param matrix 0次成分行列
   */
  public DoubleComplexPolynomialMatrix(DoubleComplexMatrix matrix) {
    this(DoubleComplexPolynomialMatrixUtil.createArray(matrix.getElements()));
  }

  /**
   * Creates {@link DoubleComplexPolynomialMatrix}.
   * 
   * @param matrix matrix
   */
  public DoubleComplexPolynomialMatrix(DoubleMatrix matrix) {
    this(new DoubleComplexMatrix(matrix));
  }

  /**
   * Creates {@link DoubleComplexPolynomialMatrix}.
   * 
   * @param rowSize 行数
   * @param columnSize 列数
   * @param elements 成分
   */
  public DoubleComplexPolynomialMatrix(int rowSize, int columnSize, DoubleComplexPolynomial[][] elements) {
    super(rowSize, columnSize, elements);
  }

  /**
   * Creates {@link DoubleComplexPolynomialMatrix}.
   * 
   * @param rowSize 行数
   * @param columnSize 列数
   * @param variableName 多項式変数
   */
  public DoubleComplexPolynomialMatrix(int rowSize, int columnSize, String variableName) {
    super(DoubleComplexPolynomial.createZeroArray(rowSize, columnSize, variableName));
  }

  /**
   * Creates {@link DoubleComplexPolynomialMatrix}.
   * 
   * @param rowSize 行数
   * @param columnSize 列数
   */
  public DoubleComplexPolynomialMatrix(int rowSize, int columnSize) {
    this(rowSize, columnSize, (String)null);
  }

  /**
   * Creates {@link DoubleComplexPolynomialMatrix}.
   *  
   * @param matrix 0次の係数行列
   */
  public DoubleComplexPolynomialMatrix(IntMatrix matrix) {
    this(new DoubleComplexMatrix(matrix));
  }

  /**
   * Creates {@link DoubleComplexPolynomialMatrix}.
   * 
   * @param elements 成分
   */
  public DoubleComplexPolynomialMatrix(DoubleComplexPolynomial[] elements) {
    super(elements);
  }

  /**
   * Creates {@link DoubleComplexPolynomialMatrix}.
   * 
   * @param elements 成分
   */
  public DoubleComplexPolynomialMatrix(DoubleComplexPolynomial[][] elements) {
    super(elements);
  }

  /**
   * 実部を返します。
   * 
   * @return 実部
   */
  public DoublePolynomialMatrix getRealPart() {
    DoublePolynomial[][] ans = getRealPartElements();
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * 実部の成分を返します。
   * 
   * @return 実部の成分
   */
  protected DoublePolynomial[][] getRealPartElements() {
    DoubleComplexPolynomial[][] elements = getElements();

    final DoublePolynomial[][] ans = new DoublePolynomial[getRowSize()][getColumnSize()];
    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        ans[i][j] = new DoublePolynomial(elements[i][j].getCoefficients().getRealPart());
      }
    }
    return ans;
  }

  /**
   * 虚部を返します。
   * 
   * @return 虚部
   */
  public DoublePolynomialMatrix getImaginaryPart() {
    DoublePolynomial[][] ans = getImaginaryPartElements();
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * 虚部の成分を返します。
   * 
   * @return 虚部の成分
   */
  protected DoublePolynomial[][] getImaginaryPartElements() {
    DoubleComplexPolynomial[][] elements = getElements();

    final DoublePolynomial[][] ans = new DoublePolynomial[getRowSize()][getColumnSize()];
    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        ans[i][j] = new DoublePolynomial(elements[i][j].getCoefficients().getImaginaryPart());
      }
    }
    return ans;
  }

  /**
   * Set real parts.
   * 
   * @param realPart real part
   */
  public final void setRealPart(final DoublePolynomialMatrix realPart) {
    final DoubleComplexPolynomial[][] elements = getElements();
    final DoublePolynomial[][] realPartElements = realPart.getElements();

    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        final DoubleComplexMatrix coefficients = elements[i][j].getCoefficients();
        coefficients.setRealPart(realPartElements[i][j].getCoefficients());
        final DoubleComplexPolynomial polynomial = new DoubleComplexPolynomial(coefficients);
        elements[i][j] = polynomial;
      }
    }

    setElements(elements);
  }

  /**
   * Sets real parts.
   * 
   * @param realPart real part
   */
  public final void setRealPart(final IntMatrix realPart) {
    final DoubleComplexPolynomial[][] elements = getElements();
    final int[][] realPartElements = realPart.getIntElements();

    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        final DoubleComplexMatrix coefficients = elements[i][j].getCoefficients();
        coefficients.setRealPart(new DoubleMatrix(1, 1, new double[][] {{realPartElements[i][j]}}));
        final DoubleComplexPolynomial polynomial = new DoubleComplexPolynomial(coefficients);
        elements[i][j] = polynomial;
      }
    }

    setElements(elements);
  }

  /**
   * Sets real part.
   * 
   * @param realPart real part
   */
  public final void setRealPart(final DoubleMatrix realPart) {
    final DoubleComplexPolynomial[][] elements = getElements();
    final double[][] realPartElements = realPart.getDoubleElements();

    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        final DoubleComplexMatrix coefficients = elements[i][j].getCoefficients();
        coefficients.setRealPart(new DoubleMatrix(1, 1, new double[][] {{realPartElements[i][j]}}));
        final DoubleComplexPolynomial polynomial = new DoubleComplexPolynomial(coefficients);
        elements[i][j] = polynomial;
      }
    }

    setElements(elements);
  }

  /**
   * Set imaginary part.
   * 
   * @param imagPart imaginary part.
   */
  public final void setImaginaryPart(final DoublePolynomialMatrix imagPart) {
    DoubleComplexPolynomial[][] elements = getElements();
    DoublePolynomial[][] imagPartElements = imagPart.getElements();

    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        final DoubleComplexMatrix coefficients = elements[i][j].getCoefficients();
        coefficients.setImaginaryPart(imagPartElements[i][j].getCoefficients());
        final DoubleComplexPolynomial polynomial = new DoubleComplexPolynomial(coefficients);
        elements[i][j] = polynomial;
      }
    }

    setElements(elements);
  }

  /**
   * Set imaginary part.
   * 
   * @param imaginaryPart imaginary part. 
   */
  public final void setImaginaryPart(final IntMatrix imaginaryPart) {
    final DoubleComplexPolynomial[][] elements = getElements();
    final int[][] realPartElements = imaginaryPart.getIntElements();

    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        final DoubleComplexMatrix coefficients = elements[i][j].getCoefficients();
        coefficients.setImaginaryPart(new DoubleMatrix(1, 1, new double[][] {{realPartElements[i][j]}}));
        final DoubleComplexPolynomial polynomial = new DoubleComplexPolynomial(coefficients);
        elements[i][j] = polynomial;
      }
    }

    setElements(elements);
  }

  /**
   * Set imaginary part.
   * 
   * @param imaginaryPart imaginary part.
   */
  public final void setImaginaryPart(final DoubleMatrix imaginaryPart) {
    final DoubleComplexPolynomial[][] elements = getElements();
    final double[][] realPartElements = imaginaryPart.getDoubleElements();

    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        final DoubleComplexMatrix coefficients = elements[i][j].getCoefficients();
        coefficients.setImaginaryPart(new DoubleMatrix(1, 1, new double[][] {{realPartElements[i][j]}}));
        final DoubleComplexPolynomial polynomial = new DoubleComplexPolynomial(coefficients);
        elements[i][j] = polynomial;
      }
    }

    setElements(elements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public DoubleComplexPolynomialMatrix transformFrom(final Matrix<?, ?> value) {
//    if (super.isTransformableFrom(value)) {
//      return (DoubleComplexPolynomialMatrix)super.transformFrom(value);
//    }
//
//    if (value instanceof DoubleComplexMatrix) {
//      return new DoubleComplexPolynomialMatrix(PolynomialMatrixUtil.<DoubleComplexNumber, DoubleComplexMatrix> createArray(((DoubleComplexMatrix)value).getElements()));
//    }
//
//    throw new IllegalArgumentException(Messages.getString("PolynomialMatrix.0") + value); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public boolean isTransformableFrom(final Matrix<?, ?> value) {
//    if (super.isTransformableFrom(value)) {
//      return true;
//    }
//
//    if (value instanceof DoubleComplexMatrix) {
//      return true;
//    }
//
//    return false;
//  }

  /**
   * {@inheritDoc}
   */
  public void writeMxFormat(final DataOutputStream output, final String name) throws IOException {
    DoubleComplexPolynomialMatrixUtil.writeMxFormat(getElements(), output, name);
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
   * ファイルからMX形式の行列データを読み込みます。
   * 
   * @param input ファイル
   * @return 読込んだ行列
   * @throws IOException 入力ストリームからデータを読込めない場合
   */
  public static DoubleComplexPolynomialMatrix readMxFormat(final File input) throws IOException {
    try (final FileInputStream stream = new FileInputStream(input)) {
      final MxDataHead head = new MxDataHead();
      head.read(stream);
      final DoubleComplexPolynomial[][] elements = DoubleComplexPolynomialMatrixUtil.readMxFormat(stream, head);
      return new DoubleComplexPolynomialMatrix(elements);
    }
  }

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
//  public static DoubleComplexPolynomialMatrix readMxFormat(final InputStream input, final MxDataHead head) throws IOException {
//    final DoubleComplexPolynomial[][] elements = PolynomialMatrixUtil.readMxFormat(input, head);
//    return new DoubleComplexPolynomialMatrix(elements);
//  }

  /**
   * {@inheritDoc}
   */
  public String toMmString() {
    return DoubleComplexPolynomialMatrixUtil.toMmString(getElements(), getElementFormat());
  }

  /**
   * {@inheritDoc}
   */
  public String toMmString(final String format) {
    return DoubleComplexPolynomialMatrixUtil.toMmString(getElements(), format);
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
  public DoubleComplexRationalPolynomialMatrix toRational() {
    return new DoubleComplexRationalPolynomialMatrix(this);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix add(DoublePolynomialMatrix value) {
    return add(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix subtract(DoublePolynomialMatrix value) {
    return subtract(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix multiply(DoublePolynomialMatrix value) {
    return multiply(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix divide(DoublePolynomialMatrix value) {
    return divide(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix leftDivide(DoublePolynomialMatrix value) {
    return leftDivide(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix multiply(DoublePolynomial value) {
    return multiply(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix divide(DoublePolynomial value) {
    return divide(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix leftDivide(DoublePolynomial value) {
    return leftDivide(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix appendDown(DoublePolynomialMatrix value) {
    return appendDown(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix appendRight(DoublePolynomialMatrix value) {
    return appendRight(value.toComplex());
  }

}
