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
package org.mklab.nfc.scalar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.channels.UnsupportedAddressTypeException;
import java.nio.charset.Charset;

import org.mklab.nfc.matrix.DoubleComplexMatrix;
import org.mklab.nfc.matrix.DoubleComplexPolynomialMatrix;
import org.mklab.nfc.matrix.DoubleComplexRationalPolynomialMatrix;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.DoublePolynomialMatrix;
import org.mklab.nfc.matrix.DoubleRationalPolynomialMatrix;
import org.mklab.nfc.matx.MatxObject;
import org.mklab.nfc.matx.MxDataHead;
import org.mklab.nfc.util.EndianTransformer;


/**
 * {@link DoubleComplexNumber}を係数とする多項式です。
 * 
 * @author koga
 * @version $Revision$, 2021/07/15
 */
public class DoubleComplexPolynomial extends AbstractComplexPolynomial<DoublePolynomial,DoublePolynomialMatrix,DoubleComplexPolynomial, DoubleComplexPolynomialMatrix,DoubleRationalPolynomial,DoubleRationalPolynomialMatrix,DoubleComplexRationalPolynomial,DoubleComplexRationalPolynomialMatrix,DoubleNumber,DoubleMatrix,DoubleComplexNumber, DoubleComplexMatrix> implements  MatxObject {

  /** */
  private static final long serialVersionUID = -4127142338639579201L;

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param constant 0次の係数
   * @param variableName 多項式変数
   */
  public DoubleComplexPolynomial(double constant, String variableName) {
    this(new DoubleComplexNumber(constant, 0), variableName);
  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param rePart 0次の実部係数
   * @param imPart 0次の虚部係数
   * @param variableName 多項式変数
   */
  public DoubleComplexPolynomial(double rePart, double imPart, String variableName) {
    this(new DoubleComplexNumber(rePart, imPart), variableName);
  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param constant 0次の係数
   */
  public DoubleComplexPolynomial(double constant) {
    this(new DoubleComplexNumber(constant, 0));
  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param rePart 0次の実部係数
   * @param imPart 0次の虚部係数
   */
  public DoubleComplexPolynomial(double rePart, double imPart) {
    this(new DoubleComplexNumber(rePart, imPart));
  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param coefficients 係数配列
   * @param variableName 多項式変数
   */
  public DoubleComplexPolynomial(double[] coefficients, String variableName) {
    this(DoubleComplexNumber.createArray(coefficients, new double[coefficients.length]), variableName);
  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param rePart 実部係数配列
   * @param imPart 虚部係数配列
   * @param variableName 多項式変数
   */
  public DoubleComplexPolynomial(double[] rePart, double[] imPart, String variableName) {
    this(DoubleComplexNumber.createArray(rePart, imPart), variableName);
  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param coefficients 0係数配列
   */
  public DoubleComplexPolynomial(double[] coefficients) {
    this(DoubleComplexNumber.createArray(coefficients, new double[coefficients.length]));
  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param rePart 実部係数配列
   * @param imPart 虚部係数配列
   */
  public DoubleComplexPolynomial(double[] rePart, double[] imPart) {
    this(DoubleComplexNumber.createArray(rePart, imPart));
  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param coefficientVector 係数ベクトル
   * @param variableName 多項式変数
   */
  public DoubleComplexPolynomial(DoubleComplexMatrix coefficientVector, String variableName) {
    super(coefficientVector, variableName);
  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param coefficientVector 係数ベクトル
   */
  public DoubleComplexPolynomial(DoubleMatrix coefficientVector) {
    this(new DoubleComplexMatrix(coefficientVector));
  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param reCoefficientVector 実部係数ベクトル
   * @param imCoefficientVector 虚部係数ベクトル
   */
  public DoubleComplexPolynomial(DoubleMatrix reCoefficientVector, DoubleMatrix imCoefficientVector) {
    this(new DoubleComplexMatrix(reCoefficientVector, imCoefficientVector));
  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param polynomial 倍精度実多項式
   */
  public DoubleComplexPolynomial(DoublePolynomial polynomial) {
    this(polynomial.getCoefficients());
  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param rePart 倍精度実部実多項式
   * @param imPart 倍精度虚部実多項式
   */
  public DoubleComplexPolynomial(DoublePolynomial rePart, DoublePolynomial imPart) {
    this(rePart.expand(Math.max(rePart.getDegree(), imPart.getDegree())).getCoefficients(), imPart.expand(Math.max(rePart.getDegree(), imPart.getDegree())).getCoefficients());
  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param coefficientVector 係数ベクトル
   */
  public DoubleComplexPolynomial(DoubleComplexMatrix coefficientVector) {
    super(coefficientVector);
  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param constant 0次の係数
   * @param variableName 多項式変数
   */
  public DoubleComplexPolynomial(DoubleComplexNumber constant, String variableName) {
    super(constant, variableName);
  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param constant 0次の係数
   */
  public DoubleComplexPolynomial(DoubleComplexNumber constant) {
    super(constant);
  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param coefficients 係数配列
   * @param variableName 多項式変数
   */
  public DoubleComplexPolynomial(DoubleComplexNumber[] coefficients, String variableName) {
    super(coefficients, variableName);
  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param coefficients 係数配列
   */
  public DoubleComplexPolynomial(DoubleComplexNumber[] coefficients) {
    super(coefficients);
  }

//  /**
//   * Creates {@link DoubleComplexPolynomial}.
//   * 
//   * @param constant 0次の係数
//   */
//  public DoubleComplexPolynomial(int constant) {
//    this(new DoubleComplexNumber(constant, 0));
//  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param variableName 多項式変数
   */
  public DoubleComplexPolynomial(String variableName) {
    this(new DoubleComplexNumber(0, 0), variableName);
  }

  //  /**
  //   * Creates {@link DoubleComplexPolynomial}.
  //   * @param polynomial 多項式
  //   */
  //  public DoubleComplexPolynomial(Polynomial<DoubleComplexNumber,DoubleComplexMatrix> polynomial) {
  //    this(polynomial.getCoefficients(), polynomial.getVariable());
  //  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial getRealPart() {
    return new DoublePolynomial(getCoefficients().getRealPart());
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial getImaginaryPart() {
    return new DoublePolynomial(getCoefficients().getImaginaryPart());
  }

//  /**
//   * {@inheritDoc}
//   */
//  public void setRealPart(final DoublePolynomial realPart) {
//    if (getDegree() < realPart.getDegree()) {
//      final DoubleComplexMatrix expandedCoefficients = expand(realPart.getDegree()).getCoefficients();
//      expandedCoefficients.setRealPart(realPart.getCoefficients());
//      setCoefficients(expandedCoefficients);
//    } else if (getDegree() == realPart.getDegree()) {
//      final DoubleComplexMatrix coefficients = getCoefficients();
//      coefficients.setRealPart(realPart.getCoefficients());
//      setCoefficients(coefficients);
//    } else {
//      final DoubleMatrix expandedRealPart = realPart.expand(getDegree()).getCoefficients();
//      final DoubleComplexMatrix coefficients = getCoefficients();
//      coefficients.setRealPart(expandedRealPart);
//      setCoefficients(coefficients);
//    }
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public void setImaginaryPart(final DoublePolynomial imaginaryPart) {
//    if (getDegree() < imaginaryPart.getDegree()) {
//      final DoubleComplexMatrix expandedCoefficients = expand(imaginaryPart.getDegree()).getCoefficients();
//      expandedCoefficients.setImaginaryPart(imaginaryPart.getCoefficients());
//      setCoefficients(expandedCoefficients);
//    } else if (getDegree() == imaginaryPart.getDegree()) {
//      final DoubleComplexMatrix coefficients = getCoefficients();
//      coefficients.setImaginaryPart(imaginaryPart.getCoefficients());
//      setCoefficients(coefficients);
//    } else {
//      final DoubleMatrix expandedRealPart = imaginaryPart.expand(getDegree()).getCoefficients();
//      final DoubleComplexMatrix coefficients = getCoefficients();
//      coefficients.setImaginaryPart(expandedRealPart);
//      setCoefficients(coefficients);
//    }
//  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @Override
  //  public boolean isTransformableFrom(final GridElement<?> value) {
  //    if (super.isTransformableFrom(value)) {
  //      return true;
  //    }
  //    
  //    if (value instanceof DoubleComplexNumber) {
  //      return true;
  //    }
  //
  //    if (value instanceof DoublePolynomial) {
  //      return true;
  //    }
  //
  //    return false;
  //  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @Override
  //  public final DoubleComplexPolynomial transformFrom(final GridElement<? extends GridElement<?>> value) {
  //    if (super.isTransformableFrom(value)) {
  //      return super.transformFrom(value);
  //    }
  //
  //    if (value instanceof DoubleComplexNumber) {
  //      return new DoubleComplexPolynomial(((DoubleComplexNumber)value));
  //    }
  //
  //    if (value instanceof DoublePolynomial) {
  //      return new DoubleComplexPolynomial(((DoublePolynomial)value));
  //    }
  //
  //    throw new IllegalArgumentException(Messages.getString("Polynomial.51")); //$NON-NLS-1$
  //  }

  //  /**
  //   * 倍精度複素数の2次元配列を生成します。
  //   * 
  //   * @param values 倍精度複素数の配列 
  //   * @return 倍精度複素数の2次元配列
  //   */
  //  public static DoubleComplexPolynomial[][] createArray(final Polynomial<DoubleComplexNumber,DoubleComplexMatrix>[][] values) {
  //    final int rowSize = values.length;
  //    final int columnSize = (rowSize == 0 || values[0] == null) ? 0 : values[0].length;
  //
  //    final DoubleComplexPolynomial[][] elements = new DoubleComplexPolynomial[rowSize][columnSize];
  //
  //    for (int i = 0; i < rowSize; i++) {
  //      for (int j = 0; j < columnSize; j++) {
  //        elements[i][j] = new DoubleComplexPolynomial(values[i][j].getCoefficients());
  //      }
  //    }
  //    return elements;
  //  }
  //
  //  /**
  //   * 倍精度複素数の1次元配列を生成します。
  //   * 
  //   * @param values 倍精度複素数の配列 
  //   * @return 倍精度複素数の1次元配列
  //   */
  //  public static DoubleComplexPolynomial[] createArray(final Polynomial<DoubleComplexNumber,DoubleComplexMatrix>[] values) {
  //    final int size = values.length;
  //
  //    final DoubleComplexPolynomial[] elements = new DoubleComplexPolynomial[size];
  //
  //    for (int i = 0; i < size; i++) {
  //      elements[i] = new DoubleComplexPolynomial(values[i].getCoefficients());
  //    }
  //    return elements;
  //  }

  /**
   * 倍精度複素数の2次元配列を生成します。
   * 
   * @param rowSize 行数
   * @param columnSize 列数
   * @param variableName 変数名
   * 
   * @return 倍精度複素数の2次元配列
   */
  public static DoubleComplexPolynomial[][] createZeroArray(int rowSize, int columnSize, String variableName) {
    final DoubleComplexPolynomial[][] elements = new DoubleComplexPolynomial[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        elements[i][j] = new DoubleComplexPolynomial(0, variableName);
      }
    }
    return elements;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexPolynomial[] createArray(final int size) {
    return new DoubleComplexPolynomial[size];
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleComplexPolynomial[][] createArray(final int rowSize, final int columnSize) {
    return new DoubleComplexPolynomial[rowSize][columnSize];
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @Override
  //  public final DoubleComplexPolynomial[] createArray(final GridElement<?>[] elements) {
  //    final int size = elements.length;
  //    
  //    if (size !=0 && (elements[0] instanceof Polynomial) == false) {
  //      throw new IllegalArgumentException();
  //    }
  //    
  //    final DoubleComplexPolynomial[] array = new DoubleComplexPolynomial[size];
  //    System.arraycopy(elements, 0, array, 0, size);
  //    return array;
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  @Override
  //  public final DoubleComplexPolynomial[][] createArray(final GridElement<?>[][] elements) {
  //    final int rowSize = elements.length;
  //    final int columnSize = rowSize == 0 ? 0 : elements[0].length;
  //    
  //    if (rowSize != 0 && columnSize !=0 && (elements[0][0] instanceof Polynomial) == false) {
  //      throw new IllegalArgumentException();
  //    }
  //    
  //    final DoubleComplexPolynomial[][] array = new DoubleComplexPolynomial[rowSize][columnSize];
  //    for (int row = 0; row < rowSize; row++) {
  //      System.arraycopy(elements[row], 0, array[row], 0, columnSize);
  //    }
  //    return array;
  //  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DoubleComplexPolynomial create(int constant) {
    return new DoubleComplexPolynomial(constant);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DoubleComplexPolynomial create(double constant) {
    return new DoubleComplexPolynomial(constant);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DoubleComplexPolynomial create(double constant, String variableName) {
    return new DoubleComplexPolynomial(constant, variableName);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomial create(String variableName) {
    return new DoubleComplexPolynomial(variableName);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomial create(DoubleComplexNumber constant) {
    return new DoubleComplexPolynomial(constant);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomial create(DoubleComplexNumber constant, String variableName) {
    return new DoubleComplexPolynomial(constant, variableName);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public DoubleComplexPolynomial create(double[] coefficients) {
//    return new DoubleComplexPolynomial(coefficients);
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public DoubleComplexPolynomial create(double[] coefficients, String variableName) {
//    return new DoubleComplexPolynomial(coefficients, variableName);
//  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomial create(DoubleComplexNumber[] coefficients) {
    return new DoubleComplexPolynomial(coefficients);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomial create(DoubleComplexNumber[] coefficients, String variableName) {
    return new DoubleComplexPolynomial(coefficients, variableName);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomial create(DoubleComplexMatrix coefficientVector) {
    return new DoubleComplexPolynomial(coefficientVector);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomial create(DoubleComplexMatrix coefficientVector, String variableName) {
    return new DoubleComplexPolynomial(coefficientVector, variableName);
  }

  /**
   * MX形式のデータをファイルから読込む。
   * 
   * @param file ファイル
   * @return 読込んだ多項式
   * @throws IOException ファイルから読込めない場合
   */
  public static DoubleComplexPolynomial readMxFormat(final File file) throws IOException {
    try (final DataInputStream ds = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
      final DoubleComplexPolynomial ans = readMxFormat(ds);
      ds.close();
      return ans;
    }
  }

  /**
   * MX形式のデータを入力ストリームから読込む。
   * 
   * @param input 入力ストリーム
   * @return 読込んだ多項式
   * @throws IOException 入力ストリームから読込めない場合
   */
  public static DoubleComplexPolynomial readMxFormat(final InputStream input) throws IOException {
    final MxDataHead head = new MxDataHead();
    head.read(input);
    return readMxFormat(head, input);
  }

  /**
   * MX形式のデータを入力ストリームから読込む。 ヘッダ情報は先に指定している。
   * 
   * @param input 入力ストリーム
   * @param head ヘッダ情報
   * @return 読込んだ多項式
   * @throws IOException 入力ストリームから読込めない場合
   */
  public static DoubleComplexPolynomial readMxFormat(final MxDataHead head, final InputStream input) throws IOException {
    final DataInputStream is = new DataInputStream(input);

    final int varlen;
    if (head.isSameEndian()) {
      varlen = is.readInt();
    } else {
      varlen = EndianTransformer.flip(is.readInt());
    }

    final String var;
    if (varlen == 0) {
      var = "s"; //$NON-NLS-1$
    } else {
      byte[] b = new byte[varlen];
      new DataInputStream(is).readFully(b);
      var = new String(b, 0, varlen - 1, Charset.forName("UTF-8")); //$NON-NLS-1$
    }

    final boolean realPolynomial = (head.getRealOrComplex() == 0);
    final int deg = head.getDegree();

    if (realPolynomial) {
      throw new UnsupportedAddressTypeException();
    }

    final DoubleComplexNumber[] comlexCoef = new DoubleComplexNumber[deg + 1];
    if (head.isSameEndian()) {
      for (int i = 0; i <= deg; i++) {
        final double real = is.readDouble();
        final double imag = is.readDouble();
        comlexCoef[i] = new DoubleComplexNumber(real, imag);
      }
    } else {
      for (int i = 0; i <= deg; i++) {
        final double real = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
        final double imag = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
        comlexCoef[i] = new DoubleComplexNumber(real, imag);
      }
    }
    final DoubleComplexPolynomial ret = new DoubleComplexPolynomial(comlexCoef, var);
    return ret;
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
   * {@inheritDoc}
   */
  public final void writeMxFormat(final DataOutputStream output, final String name) throws IOException {
    final MxDataHead head = new MxDataHead(this, name);
    head.write(output);

    final int varlen;
    final byte[] b;
    final String variable = getVariable();
    if (variable != null) {
      varlen = variable.length() + 1;
      b = (variable + "\0").getBytes(Charset.forName("UTF-8")); //$NON-NLS-1$ //$NON-NLS-2$
    } else {
      varlen = 0;
      b = new byte[0];
    }

    output.writeInt(varlen);
    output.write(b, 0, b.length);

    writeMxFormatWithoutHeader(output, false);

    output.flush();
  }

  /**
   * {@inheritDoc}
   */
  public final String toMmString() {
    return toMmString("%G"); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final String toMmString(final String coefficientFormat) {
    return toMmStringFromDoublePolynomial(coefficientFormat);
  }

  /**
   * 倍精度実多項式を文字列に変換します。
   * 
   * @param coefficientFormat 係数の出力フォーマット
   * @return 変換で生成された文字列
   */
  private String toMmStringFromDoublePolynomial(final String coefficientFormat) {
    final String variable = getVariable();
    final String coefficientString = getCoefficients().flipLeftRight().toMmString(coefficientFormat);
    return "Polynomial(" + coefficientString + ", \"" + (variable == null ? "s" : variable) + "\")"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
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
    final StringBuffer sb = new StringBuffer();

    if (name.length() != 0) {
      sb.append(name);
      sb.append(" = "); //$NON-NLS-1$
    }

    sb.append(toMmString());

    if (withStatementSeparator) {
      String newLine = System.getProperty("line.separator"); //$NON-NLS-1$
      sb.append(";"); //$NON-NLS-1$
      sb.append(newLine);
      sb.append(newLine);
    }

    output.write(sb.toString());
    output.flush();
  }

//  /**
//   * {@inheritDoc}
//   */
  /**
   * データのみを出力ストリームに出力します。
   * 
   * @param output 出力ストリーム
   * @param asComplex 複素数として出力するならばtrue、そうでなければfalse
   * @throws IOException ストリームに出力できない場合
   */
  public void writeMxFormatWithoutHeader(final OutputStream output, final boolean asComplex) throws IOException {
    final DataOutputStream ds = new DataOutputStream(new BufferedOutputStream(output));

    final DoubleComplexMatrix data = getCoefficients();
    for (int i = 0; i <= getDegree(); i++) {
      ds.writeDouble(data.getElement(i + 1).getRealPart().doubleValue());
      ds.writeDouble(data.getElement(i + 1).getImaginaryPart().doubleValue());
    }

    ds.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DoubleComplexPolynomial create(DoublePolynomial rePart, DoublePolynomial imPart) {
    return new DoubleComplexPolynomial(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DoubleComplexPolynomial create(DoublePolynomial rePart) {
    return new DoubleComplexPolynomial(rePart);
  }
  

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexPolynomialMatrix createGrid(final int rowSize, final int columnSize, final DoubleComplexPolynomial[][] elements) {
    return new DoubleComplexPolynomialMatrix(rowSize, columnSize, elements);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexPolynomialMatrix createGrid(final DoubleComplexPolynomial[] elements) {
    return new DoubleComplexPolynomialMatrix(elements);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial toRational() {
    return new DoubleComplexRationalPolynomial(this);
  }
}
