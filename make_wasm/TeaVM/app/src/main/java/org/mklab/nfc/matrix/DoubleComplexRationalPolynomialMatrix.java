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


/**
 * {@link DoubleComplexNumber}を係数とする有理多項式を成分とする行列です。
 * 
 * @author koga
 * @version $Revision$, 2021/07/15
 */
public class DoubleComplexRationalPolynomialMatrix extends AbstractSymbolicMatrix<DoubleComplexRationalPolynomial, DoubleComplexRationalPolynomialMatrix,DoubleComplexNumber,DoubleComplexMatrix> implements ComplexRationalPolynomialMatrix<DoublePolynomial,DoublePolynomialMatrix,DoubleComplexPolynomial, DoubleComplexPolynomialMatrix,DoubleRationalPolynomial,DoubleRationalPolynomialMatrix,DoubleComplexRationalPolynomial, DoubleComplexRationalPolynomialMatrix,DoubleNumber,DoubleMatrix,DoubleComplexNumber, DoubleComplexMatrix>, 
MatxObject {

  /** */
  private static final long serialVersionUID = -6986082851401153200L;

  /**
   * Creates {@link DoubleComplexPolynomialMatrix}.
   * 
   * @param matrix 0次成分行列
   */
  public DoubleComplexRationalPolynomialMatrix(DoubleRationalPolynomialMatrix matrix) {
    this(matrix.getElements());
  }

  /**
   * Creates {@link DoubleComplexPolynomialMatrix}.
   * 
   * @param elements 成分
   */
  public DoubleComplexRationalPolynomialMatrix(DoubleRationalPolynomial[][] elements) {
    this(DoubleRationalPolynomialMatrixUtil.createComplexArray(elements));
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomialMatrix}.
   * 
   * @param matrix 0次成分行列
   */
  public DoubleComplexRationalPolynomialMatrix(DoubleComplexMatrix matrix) {
    this(new DoubleComplexPolynomialMatrix(matrix));
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomialMatrix}.
   * 
   * @param matrix 0次成分行列
   */
  public DoubleComplexRationalPolynomialMatrix(DoubleMatrix matrix) {
    this(new DoubleComplexMatrix(matrix));
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomialMatrix}.
   * 
   * @param matrix 行列
   */
  public DoubleComplexRationalPolynomialMatrix(DoubleComplexRationalPolynomialMatrix matrix) {
    this(matrix.getElements());
  }
  
  /**
   * Creates {@link DoubleComplexPolynomialMatrix}.
   * 
   * @param rePart 実部行列
   * @param imPart 虚部行列
   */
  public DoubleComplexRationalPolynomialMatrix(DoubleRationalPolynomialMatrix rePart, DoubleRationalPolynomialMatrix imPart) {
    this(DoubleRationalPolynomialMatrixUtil.createComplexArray(rePart.getElements(), imPart.getElements()));
  }


  /**
   * Creates {@link DoubleComplexRationalPolynomialMatrix}.
   * 
   * @param rowSize 行数
   * @param columnSize 列数
   * @param elements 成分
   */
  public DoubleComplexRationalPolynomialMatrix(int rowSize, int columnSize, DoubleComplexRationalPolynomial[][] elements) {
    super(rowSize, columnSize, elements);
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomialMatrix}.
   * 
   * @param rowSize 行数
   * @param columnSize 列数
   * @param variableName 多項式変数
   */
  public DoubleComplexRationalPolynomialMatrix(int rowSize, int columnSize, String variableName) {
    super(DoubleComplexRationalPolynomial.createZeroArray(rowSize, columnSize, variableName));
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomialMatrix}.
   * 
   * @param rowSize 行数
   * @param columnSize 列数
   */
  public DoubleComplexRationalPolynomialMatrix(int rowSize, int columnSize) {
    this(rowSize, columnSize, (String)null);
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomialMatrix}.
   * 
   * @param matrix 0次成分行列
   */
  public DoubleComplexRationalPolynomialMatrix(IntMatrix matrix) {
    this(new DoubleComplexMatrix(matrix));
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomialMatrix}.
   * 
   * @param matrix 0次成分行列
   */
  public DoubleComplexRationalPolynomialMatrix(DoubleComplexPolynomialMatrix matrix) {
    super(DoubleComplexRationalPolynomialMatrixUtil.createArray(matrix.getElements()));
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomialMatrix}.
   * 
   * @param elements 成分
   */
  public DoubleComplexRationalPolynomialMatrix(DoubleComplexRationalPolynomial[] elements) {
    super(elements);
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomialMatrix}.
   * 
   * @param elements 成分
   */
  public DoubleComplexRationalPolynomialMatrix(DoubleComplexRationalPolynomial[][] elements) {
    super(elements);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix multiply(DoubleComplexPolynomial polynomial) {
    DoubleComplexRationalPolynomial[][] ans = DoubleComplexRationalPolynomialMatrixUtil.multiply(getElements(), polynomial);
    return new DoubleComplexRationalPolynomialMatrix(ans);    
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix divide(DoubleComplexPolynomial polynomial) {
    DoubleComplexRationalPolynomial[][] ans = DoubleComplexRationalPolynomialMatrixUtil.divide(getElements(), polynomial);
    return new DoubleComplexRationalPolynomialMatrix(ans);    
  }

  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomialMatrix getRealPart() {
    DoubleRationalPolynomial[][] ans = getRealPartElements();
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * 実部の成分を返します。
   * 
   * @return 実部の成分
   */
  protected DoubleRationalPolynomial[][] getRealPartElements() {
    DoubleComplexRationalPolynomial[][] elements = getElements();

    final DoubleRationalPolynomial[][] ans = new DoubleRationalPolynomial[getRowSize()][getColumnSize()];
    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        ans[i][j] = elements[i][j].getRealPart();
      }
    }
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomialMatrix getImaginaryPart() {
    DoubleRationalPolynomial[][] ans = getImaginaryPartElements();
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * 虚部の成分を返します。
   * 
   * @return 虚部の成分
   */
  protected DoubleRationalPolynomial[][] getImaginaryPartElements() {
    DoubleComplexRationalPolynomial[][] elements = getElements();

    final DoubleRationalPolynomial[][] ans = new DoubleRationalPolynomial[getRowSize()][getColumnSize()];
    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        ans[i][j] = elements[i][j].getImaginaryPart();
      }
    }
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final void setRealPart(final DoubleRationalPolynomialMatrix realPart) {
    final DoubleComplexRationalPolynomial[][] elements = getElements();
    final DoubleRationalPolynomial[][] realPartElements = realPart.getElements();

    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        elements[i][j].setRealPart(realPartElements[i][j]);
      }
    }

    setElements(elements);
  }

  /**
   * {@inheritDoc}
   */
  public final void setRealPart(final IntMatrix realPart) {
    final DoubleComplexRationalPolynomial[][] elements = getElements();
    final int[][] realPartElements = realPart.getIntElements();

    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        final DoubleRationalPolynomial realElement = new DoubleRationalPolynomial(realPartElements[i][j]);
        elements[i][j].setRealPart(realElement);
      }
    }

    setElements(elements);
  }

  /**
   * {@inheritDoc}
   */
  public final void setRealPart(final DoubleMatrix realPart) {
    final DoubleComplexRationalPolynomial[][] elements = getElements();
    final double[][] realPartElements = realPart.getDoubleElements();

    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        final DoubleRationalPolynomial realElement = new DoubleRationalPolynomial(realPartElements[i][j]);
        elements[i][j].setRealPart(realElement);
      }
    }

    setElements(elements);
  }

  /**
   * {@inheritDoc}
   */
  public final void setImaginaryPart(final DoubleRationalPolynomialMatrix imagPart) {
    final DoubleComplexRationalPolynomial[][] elements = getElements();
    final DoubleRationalPolynomial[][] imaginaryPartElements = imagPart.getElements();

    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        elements[i][j].setImaginaryPart(imaginaryPartElements[i][j]);
      }
    }

    setElements(elements);
  }

  /**
   * {@inheritDoc}
   */
  public final void setImaginaryPart(final IntMatrix imaginaryPart) {
    final DoubleComplexRationalPolynomial[][] elements = getElements();
    final int[][] realPartElements = imaginaryPart.getIntElements();

    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        final DoubleRationalPolynomial imaginaryElement = new DoubleRationalPolynomial(realPartElements[i][j]);
        elements[i][j].setImaginaryPart(imaginaryElement);
      }
    }

    setElements(elements);
  }

  /**
   * {@inheritDoc}
   */
  public final void setImaginaryPart(final DoubleMatrix imaginaryPart) {
    final DoubleComplexRationalPolynomial[][] elements = getElements();
    final double[][] imaginaryPartElements = imaginaryPart.getDoubleElements();

    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        final DoubleRationalPolynomial imaginaryElement = new DoubleRationalPolynomial(imaginaryPartElements[i][j]);
        elements[i][j].setImaginaryPart(imaginaryElement);
      }
    }

    setElements(elements);
  }

  /**
   * {@inheritDoc}
   */
  public String toMmString() {
    final String ans = DoubleComplexRationalPolynomialMatrixUtil.toMmString(getElements(), getElementFormat());
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public String toMmString(final String format) {
    final String ans = DoubleComplexRationalPolynomialMatrixUtil.toMmString(getElements(), format);
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public void writeMxFormat(final DataOutputStream output, final String name) throws IOException {
    DoubleComplexRationalPolynomialMatrixUtil.writeMxFormat(getElements(), output, name);
  }

  /**
   * {@inheritDoc}
   */
  public void writeMxFormat(final File file, final String name) throws IOException {
    try (DataOutputStream output = new DataOutputStream(new FileOutputStream(file))) {
      writeMxFormat(output, name);
    }
  }

  //  /**
  //   * 入力ストリームからMX形式のデータを読み込みます。
  //   * 
  //   * @param input 入力ストリーム
  //   * @param head ヘッダー
  //   * @return 読み込んだデータ
  //   * @throws IOException 入力ストリームから読み込みない場合
  //   */
  //  public static DoubleComplexRationalPolynomialMatrix readMxFormat(final InputStream input, final MxDataHead head) throws IOException {
  //    final DoubleComplexRationalPolynomial[][] elements = DoubleComplexRationalPolynomialMatrix.readMxFormat(input, head);
  //    return new DoubleComplexRationalPolynomialMatrix(elements);
  //    //final DoubleComplexRationalPolynomialMatrix ans = new DoubleComplexRationalPolynomialMatrix(DoubleComplexRationalPolynomialMatrix.readMxFormat(input, head));
  //    //return ans;
  //  }

  /**
   * 入力ストリームからMX形式の行列データを読み込みます。
   * 
   * @param input 入力ストリーム
   * @return 読込んだ行列
   * @throws IOException 入力ストリームからデータを読込めない場合
   */
  public static DoubleComplexRationalPolynomialMatrix readMxFormat(final InputStream input) throws IOException {
    final MxDataHead head = new MxDataHead();
    head.read(input);
    final DoubleComplexRationalPolynomial[][] elements = DoubleComplexRationalPolynomialMatrixUtil.readMxFormat(input, head);
    return new DoubleComplexRationalPolynomialMatrix(elements);
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
  public void printElements(final Writer output, final int maxColumnSize) {
    RationalPolynomialMatrixUtil.print(getElements(), output, getElementFormat(), maxColumnSize);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix add(DoubleRationalPolynomialMatrix value) {
    return add(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix subtract(DoubleRationalPolynomialMatrix value) {
    return subtract(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix multiply(DoubleRationalPolynomialMatrix value) {
    return multiply(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix divide(DoubleRationalPolynomialMatrix value) {
    return divide(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix leftDivide(DoubleRationalPolynomialMatrix value) {
    return leftDivide(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix multiply(DoubleRationalPolynomial value) {
    return multiply(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix divide(DoubleRationalPolynomial value) {
    return divide(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix leftDivide(DoubleRationalPolynomial value) {
    return leftDivide(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix appendDown(DoubleRationalPolynomialMatrix value) {
    return appendDown(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix appendRight(DoubleRationalPolynomialMatrix value) {
    return appendRight(value.toComplex());
  }

}
