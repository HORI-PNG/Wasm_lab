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
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
 * {@link DoubleComplexNumber}を係数とする有理多項式です。
 * 
 * @author koga
 * @version $Revision$, 2021/07/15
 */
public class DoubleComplexRationalPolynomial extends
    AbstractRationalPolynomial<DoubleComplexPolynomial, DoubleComplexPolynomialMatrix, DoubleComplexRationalPolynomial, DoubleComplexRationalPolynomialMatrix, DoubleComplexNumber, DoubleComplexMatrix>
    implements
    ComplexRationalPolynomial<DoublePolynomial, DoublePolynomialMatrix, DoubleComplexPolynomial, DoubleComplexPolynomialMatrix, DoubleRationalPolynomial, DoubleRationalPolynomialMatrix, DoubleComplexRationalPolynomial, DoubleComplexRationalPolynomialMatrix, DoubleNumber, DoubleMatrix, DoubleComplexNumber, DoubleComplexMatrix>,
    MatxObject {

  /** */
  private static final long serialVersionUID = -4891222816928244891L;

  /**
   * Creates {@link DoubleComplexRationalPolynomial}.
   * 
   * @param numerator 分子多項式
   * @param denominator 分母多項式
   */
  public DoubleComplexRationalPolynomial(double numerator, DoubleComplexPolynomial denominator) {
    this(new DoubleComplexNumber(numerator, 0), denominator);
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomial}.
   * 
   * @param numerator 分子多項式
   * @param variableName 多項式変数
   */
  public DoubleComplexRationalPolynomial(double numerator, String variableName) {
    this(new DoubleComplexNumber(numerator, 0), variableName);
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomial}.
   * 
   * @param rePart 実部分子多項式
   * @param imPart 虚部分子多項式
   * @param variableName 多項式変数
   */
  public DoubleComplexRationalPolynomial(double rePart, double imPart, String variableName) {
    this(new DoubleComplexNumber(rePart, imPart), variableName);
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomial}.
   * 
   * @param numerator 分子多項式
   */
  public DoubleComplexRationalPolynomial(double numerator) {
    this(new DoubleComplexNumber(numerator, 0));
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomial}.
   * 
   * @param rePart 実部分子多項式
   * @param imPart 虚部分子多項式
   */
  public DoubleComplexRationalPolynomial(double rePart, double imPart) {
    this(new DoubleComplexNumber(rePart, imPart));
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomial}.
   * 
   * @param numerator 分子多項式
   * @param denominator 分母多項式
   */
  public DoubleComplexRationalPolynomial(DoubleComplexNumber numerator, DoubleComplexPolynomial denominator) {
    super(numerator, denominator);
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomial}.
   * 
   * @param numerator 分子多項式
   * @param variableName 多項式変数
   */
  public DoubleComplexRationalPolynomial(DoubleComplexNumber numerator, String variableName) {
    this(new DoubleComplexPolynomial(numerator, variableName));
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomial}.
   * 
   * @param numerator 分子多項式
   */
  public DoubleComplexRationalPolynomial(DoubleComplexNumber numerator) {
    this(new DoubleComplexPolynomial(numerator));
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomial}.
   * 
   * @param numerator 分子多項式
   * @param denominator 分母多項式
   */
  public DoubleComplexRationalPolynomial(DoublePolynomial numerator, DoublePolynomial denominator) {
    this(new DoubleComplexPolynomial(numerator), new DoubleComplexPolynomial(denominator));
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomial}.
   * 
   * @param numerator 分子多項式
   * @param denominator 分母多項式
   */
  public DoubleComplexRationalPolynomial(DoubleComplexPolynomial numerator, DoublePolynomial denominator) {
    this(numerator, new DoubleComplexPolynomial(denominator));
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomial}.
   * 
   * @param numerator 分子多項式
   * @param denominator 分母多項式
   */
  public DoubleComplexRationalPolynomial(DoublePolynomial numerator, DoubleComplexPolynomial denominator) {
    this(new DoubleComplexPolynomial(numerator), denominator);
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomial}.
   * 
   * @param value value
   */
  public DoubleComplexRationalPolynomial(DoubleRationalPolynomial value) {
    this(new DoubleComplexPolynomial(value.getNumerator()), new DoubleComplexPolynomial(value.getDenominator()));
  }

  /**
   * Creates {@link DoubleComplexPolynomial}.
   * 
   * @param rePart 倍精度実部実多項式
   * @param imPart 倍精度虚部実多項式
   */
  public DoubleComplexRationalPolynomial(DoubleRationalPolynomial rePart, DoubleRationalPolynomial imPart) {
    this(new DoubleComplexPolynomial(rePart.getNumerator().multiply(imPart.getDenominator()), imPart.getNumerator().multiply(rePart.getDenominator())), new DoubleComplexPolynomial(rePart.getDenominator().multiply(imPart.getDenominator())));
  }

  
  /**
   * Creates {@link DoubleComplexRationalPolynomial}.
   * 
   * @param numerator 分子多項式
   * @param variableName 多項式変数
   */
  public DoubleComplexRationalPolynomial(int numerator, String variableName) {
    this(new DoubleComplexNumber(numerator, 0), variableName);
  }

  //  /**
  //   * Creates {@link DoubleComplexRationalPolynomial}.
  //   * 
  //   * @param numerator 分子多項式
  //   */
  //  public DoubleComplexRationalPolynomial(int numerator) {
  //    this(new DoubleComplexNumber(numerator, 0));
  //  }

  /**
   * Creates {@link DoubleComplexRationalPolynomial}.
   * 
   * @param numerator 分子多項式
   * @param denominator 分母多項式
   */
  public DoubleComplexRationalPolynomial(DoubleComplexPolynomial numerator, double denominator) {
    this(numerator, new DoubleComplexNumber(denominator, 0));
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomial}.
   * 
   * @param numerator 分子多項式
   * @param denominator 分母多項式
   */
  public DoubleComplexRationalPolynomial(DoubleComplexPolynomial numerator, DoubleComplexNumber denominator) {
    super(numerator, denominator);
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomial}.
   * 
   * @param numerator 分子多項式
   * @param denominator 分母多項式
   */
  public DoubleComplexRationalPolynomial(DoubleComplexPolynomial numerator, DoubleComplexPolynomial denominator) {
    super(numerator, denominator);
  }

  /**
   * Creates {@link DoubleComplexRationalPolynomial}.
   * 
   * @param numerator 分子多項式
   */
  public DoubleComplexRationalPolynomial(DoubleComplexPolynomial numerator) {
    super(numerator);
  }

  //  /**
  //   * Creates {@link DoubleComplexRationalPolynomial}.
  //   * @param rationalPolynomial 有理多項式
  //   */
  //  public DoubleComplexRationalPolynomial(RationalPolynomial<DoubleComplexNumber,DoubleComplexMatrix> rationalPolynomial) {
  //    this(new DoubleComplexPolynomial(rationalPolynomial.getNumerator()), new DoubleComplexPolynomial(rationalPolynomial.getDenominator()));
  //  }

  /**
   * 実部有理多項式を返します。
   * 
   * @return 実部有理多項式
   */
  public final DoubleRationalPolynomial getRealPart() {
    final DoubleComplexMatrix numeratorCoefficients = getNumerator().getCoefficients();
    final DoubleComplexMatrix denominatorCoefficients = getDenominator().getCoefficients();

    final DoublePolynomial numr = new DoublePolynomial(numeratorCoefficients.getRealPart());
    final DoublePolynomial numi = new DoublePolynomial(numeratorCoefficients.getImaginaryPart());
    final DoublePolynomial denr = new DoublePolynomial(denominatorCoefficients.getRealPart());
    final DoublePolynomial deni = new DoublePolynomial(denominatorCoefficients.getImaginaryPart());

    if (deni.isZero()) {
      numr.simplify();
      denr.simplify();
      return new DoubleRationalPolynomial(numr, denr);
    }

    final DoublePolynomial ansNumerator = numr.multiply(denr).add(numi.multiply(deni));
    final DoublePolynomial ansDenominator = denr.multiply(denr).add(deni.multiply(deni));
    return new DoubleRationalPolynomial(ansNumerator, ansDenominator);
  }

  /**
   * 虚部有理多項式を返します。
   * 
   * @return 虚部多項式
   */
  public final DoubleRationalPolynomial getImaginaryPart() {
    final DoubleComplexMatrix numeratorCoefficients = getNumerator().getCoefficients();
    final DoubleComplexMatrix denominatorCoefficients = getDenominator().getCoefficients();

    final DoublePolynomial numr = new DoublePolynomial(numeratorCoefficients.getRealPart());
    final DoublePolynomial numi = new DoublePolynomial(numeratorCoefficients.getImaginaryPart());
    final DoublePolynomial denr = new DoublePolynomial(denominatorCoefficients.getRealPart());
    final DoublePolynomial deni = new DoublePolynomial(denominatorCoefficients.getImaginaryPart());

    if (deni.isZero()) {
      numi.simplify();
      denr.simplify();
      return new DoubleRationalPolynomial(numi, denr);
    }

    final DoublePolynomial ansNumerator = numi.multiply(denr).subtract(numr.multiply(deni));
    final DoublePolynomial ansDenominator = denr.multiply(denr).add(deni.multiply(deni));
    return new DoubleRationalPolynomial(ansNumerator, ansDenominator);
  }

  /**
   * 複素有理多項式の実部有理多項式を設定します。
   * 
   * @param realPart 実部有理多項式
   */
  public final void setRealPart(final DoubleRationalPolynomial realPart) {
    final DoubleComplexNumber j = new DoubleComplexNumber(0, 1).createImaginaryUnit();
    final DoubleComplexRationalPolynomial ans = new DoubleComplexRationalPolynomial(realPart).add(getImaginaryPart().multiply(j));

    setNumerator(ans.getNumerator());
    setDenominator(ans.getDenominator());
  }

  /**
   * 複素有理多項式の虚部有理多項式を設定します。
   * 
   * @param imagPart 虚部有理多項式
   */
  public final void setImaginaryPart(final DoubleRationalPolynomial imagPart) {
    final DoubleComplexNumber j = new DoubleComplexNumber(0, 1).createImaginaryUnit();
    final DoubleComplexRationalPolynomial ans = new DoubleComplexRationalPolynomial(getRealPart()).add(imagPart.multiply(j));

    setNumerator(ans.getNumerator());
    setDenominator(ans.getDenominator());
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @Override
  //  public boolean isTransformableFrom(final GridElement<?> value) {
  //    if (super.isTransformableFrom(value)) {
  //      return true;
  //    }
  //
  //    if (value instanceof DoubleRationalPolynomial) {
  //      return true;
  //    }
  //
  //    return false;
  //  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @Override
  //  public final DoubleComplexRationalPolynomial transformFrom(final GridElement<? extends GridElement<?>> value) {
  //    if (super.isTransformableFrom(value)) {
  //      return super.transformFrom(value);
  //    }
  //
  //    if (value instanceof DoubleRationalPolynomial) {
  //      return new DoubleComplexRationalPolynomial(((DoubleRationalPolynomial)value));
  //    }
  //
  //    throw new IllegalArgumentException(Messages.getString("Polynomial.51")); //$NON-NLS-1$
  //  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial create(double numerator, DoubleComplexPolynomial denominator) {
    return new DoubleComplexRationalPolynomial(numerator, denominator);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial create(DoubleComplexNumber numerator, DoubleComplexPolynomial denominator) {
    return new DoubleComplexRationalPolynomial(numerator, denominator);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial create(DoubleComplexPolynomial numerator, DoubleComplexPolynomial denominator) {
    return new DoubleComplexRationalPolynomial(numerator, denominator);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial create(DoubleComplexPolynomial numerator, double denominator) {
    return new DoubleComplexRationalPolynomial(numerator, denominator);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial create(DoubleComplexPolynomial numerator, DoubleComplexNumber denominator) {
    return new DoubleComplexRationalPolynomial(numerator, denominator);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial create(DoubleComplexPolynomial numerator) {
    return new DoubleComplexRationalPolynomial(numerator);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial create(int numerator) {
    return new DoubleComplexRationalPolynomial(numerator);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial create(int numerator, String variableName) {
    return new DoubleComplexRationalPolynomial(numerator, variableName);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial create(double numerator) {
    return new DoubleComplexRationalPolynomial(numerator);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial create(double numerator, String variableName) {
    return new DoubleComplexRationalPolynomial(numerator, variableName);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial create(DoubleComplexNumber numerator) {
    return new DoubleComplexRationalPolynomial(numerator);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial create(DoubleComplexNumber numerator, String variableName) {
    return new DoubleComplexRationalPolynomial(numerator, variableName);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public DoubleComplexRationalPolynomial multiply(DoubleComplexPolynomial value) {
  //    return new DoubleComplexRationalPolynomial(getNumerator().multiply(value), getDenominator());
  //  }
  //
  //  public DoubleComplexRationalPolynomial leftDivide(DoubleComplexPolynomial value) {
  //    // TODO stub automaticaly generated
  //    return null;
  //  }
  //
  //  public DoubleComplexRationalPolynomial subtract(DoubleComplexNumber value) {
  //    // TODO stub automaticaly generated
  //    return null;
  //  }
  //
  //  public DoubleComplexRationalPolynomial multiply(double value) {
  //    // TODO stub automaticaly generated
  //    return null;
  //  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial createUnit() {
    return new DoubleComplexRationalPolynomial(1);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial createZero() {
    return new DoubleComplexRationalPolynomial(0);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial[] createArray(int size) {
    return new DoubleComplexRationalPolynomial[size];
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public DoubleComplexRationalPolynomial[] createArray(GridElement<?>[] elements) {
  //    final int size = elements.length;
  //
  //    if (size != 0 && (elements[0] instanceof RationalPolynomial) == false) {
  //      throw new IllegalArgumentException();
  //    }
  //
  //    final DoubleComplexRationalPolynomial[] array = new DoubleComplexRationalPolynomial[size];
  //    System.arraycopy(elements, 0, array, 0, size);
  //    return array;
  //  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial[][] createArray(int rowSize, int columnSize) {
    return new DoubleComplexRationalPolynomial[rowSize][columnSize];
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public DoubleComplexRationalPolynomial[][] createArray(GridElement<?>[][] elements) {
  //    final int rowSize = elements.length;
  //    final int columnSize = rowSize == 0 ? 0 : elements[0].length;
  //
  //    if (rowSize != 0 && columnSize != 0 && (elements[0][0] instanceof RationalPolynomial) == false) {
  //      throw new IllegalArgumentException();
  //    }
  //
  //    final DoubleComplexRationalPolynomial[][] array = new DoubleComplexRationalPolynomial[rowSize][columnSize];
  //    for (int row = 0; row < rowSize; row++) {
  //      System.arraycopy(elements[row], 0, array[row], 0, columnSize);
  //    }
  //    return array;
  //  }

  //  /**
  //   * 倍精度複素数の2次元配列を生成します。
  //   * 
  //   * @param values 倍精度複素数の配列 
  //   * @return 倍精度複素数の2次元配列
  //   */
  //  public static DoubleComplexRationalPolynomial[][] createArray(final RationalPolynomial<DoubleComplexNumber,DoubleComplexMatrix>[][] values) {
  //    final int rowSize = values.length;
  //    final int columnSize = (rowSize == 0 || values[0] == null) ? 0 : values[0].length;
  //
  //    final DoubleComplexRationalPolynomial[][] elements = new DoubleComplexRationalPolynomial[rowSize][columnSize];
  //
  //    for (int i = 0; i < rowSize; i++) {
  //      for (int j = 0; j < columnSize; j++) {
  //        final DoubleComplexPolynomial numerator = new DoubleComplexPolynomial(values[i][j].getNumerator());
  //        final DoubleComplexPolynomial denominator = new DoubleComplexPolynomial(values[i][j].getDenominator());
  //        elements[i][j] = new DoubleComplexRationalPolynomial(numerator, denominator);
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
  //  public static DoubleComplexRationalPolynomial[] createArray(final RationalPolynomial<DoubleComplexNumber,DoubleComplexMatrix>[] values) {
  //    final int size = values.length;
  //  
  //    final DoubleComplexRationalPolynomial[] elements = new DoubleComplexRationalPolynomial[size];
  //
  //    for (int i = 0; i < size; i++) {
  //        final DoubleComplexPolynomial numerator = new DoubleComplexPolynomial(values[i].getNumerator());
  //        final DoubleComplexPolynomial denominator = new DoubleComplexPolynomial(values[i].getDenominator());
  //        elements[i] = new DoubleComplexRationalPolynomial(numerator, denominator);
  //    }
  //    return elements;
  //  }

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
    final String numeratorString = getNumerator().toMmString(coefficientFormat);
    final String denominatorString = getDenominator().toMmString(coefficientFormat);
    return "(" + numeratorString + ") / (" + denominatorString + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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

    final String var = getVariable();
    final int varlen;
    final byte[] b;
    if (var != null) {
      varlen = var.length() + 1;
      b = (var + "\0").getBytes(Charset.forName("UTF-8")); //$NON-NLS-1$ //$NON-NLS-2$
    } else {
      varlen = 0;
      b = new byte[0];
    }

    output.writeInt(varlen);
    output.write(b, 0, b.length);

    final DoubleComplexMatrix ndata = getNumerator().getCoefficients();
    final DoubleComplexMatrix ddata = getDenominator().getCoefficients();
    for (int i = 0; i < ndata.getColumnSize(); i++) {
      output.writeDouble(ndata.getElement(i + 1).getRealPart().doubleValue());
      output.writeDouble(ndata.getElement(i + 1).getImaginaryPart().doubleValue());
    }
    for (int i = 0; i < ddata.getColumnSize(); i++) {
      output.writeDouble(ddata.getElement(i + 1).getRealPart().doubleValue());
      output.writeDouble(ddata.getElement(i + 1).getImaginaryPart().doubleValue());
    }

    output.flush();
  }

  /**
   * MX形式のデータをファイルから入力します。
   * 
   * @param file ファイル
   * @return 読み込んだ有理多項式
   * @throws IOException ファイルから入力できない場合
   */
  public static DoubleComplexRationalPolynomial readMxFormat(final File file) throws IOException {
    try (final DataInputStream ds = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
      final DoubleComplexRationalPolynomial ans = readMxFormat(ds);
      return ans;
    }
  }

  /**
   * MX形式のデータを入力ストリームから入力します。
   * 
   * @param input 入力ストリーム
   * @return 入力した有理多項式
   * @throws IOException 入力ストリームから入力できない場合
   */
  public static DoubleComplexRationalPolynomial readMxFormat(final InputStream input) throws IOException {
    final MxDataHead head = new MxDataHead();
    head.read(input);
    return readMxFormat(head, input);
  }

  /**
   * MX形式のデータを入力ストリームから入力します。 ヘッダ情報は先に指定している。
   * 
   * @param input 入力ストリーム
   * @param head ヘッダ情報
   * @return 入力した有理多項式
   * @throws IOException 入力ストリームから入力できない場合
   */
  public static DoubleComplexRationalPolynomial readMxFormat(final MxDataHead head, final InputStream input) throws IOException {
    final DataInputStream is = new DataInputStream(input);

    final int varlen;
    if (head.isSameEndian()) {
      varlen = is.readInt();
    } else {
      varlen = EndianTransformer.flip(is.readInt());
    }

    final String var;
    if (varlen == 0) {
      var = null;
    } else {
      byte[] b = new byte[varlen];
      new DataInputStream(is).readFully(b);
      var = new String(b, 0, varlen - 1, Charset.forName("UTF-8")); //$NON-NLS-1$
    }

    final int realImag = head.getRealOrComplex();
    final int ndeg = head.getNumeratorDegree();
    final int ddeg = head.getDenominatorDegree();

    if (realImag == 0) {
      final double[] ndata = new double[ndeg + 1];
      final double[] ddata = new double[ddeg + 1];
      if (head.isSameEndian()) {
        for (int i = 0; i <= ndeg; i++) {
          ndata[i] = is.readDouble();
        }
        for (int i = 0; i <= ddeg; i++) {
          ddata[i] = is.readDouble();
        }
      } else {
        for (int i = 0; i <= ndeg; i++) {
          long d = EndianTransformer.flip(is.readLong());
          ndata[i] = Double.longBitsToDouble(d);
        }
        for (int i = 0; i <= ddeg; i++) {
          long d = EndianTransformer.flip(is.readLong());
          ddata[i] = Double.longBitsToDouble(d);
        }
      }

      return new DoubleComplexRationalPolynomial(new DoubleComplexPolynomial(ndata, var), new DoubleComplexPolynomial(ddata, var));
    }

    final DoubleComplexNumber[] ndata = new DoubleComplexNumber[ndeg + 1];
    final DoubleComplexNumber[] ddata = new DoubleComplexNumber[ddeg + 1];

    if (head.isSameEndian()) {
      for (int i = 0; i <= ndeg; i++) {
        final double real = is.readDouble();
        final double imag = is.readDouble();
        ndata[i] = new DoubleComplexNumber(real, imag);
      }
      for (int i = 0; i <= ddeg; i++) {
        final double real = is.readDouble();
        final double imag = is.readDouble();
        ddata[i] = new DoubleComplexNumber(real, imag);
      }
    } else {
      for (int i = 0; i <= ndeg; i++) {
        final long dr = EndianTransformer.flip(is.readLong());
        final long di = EndianTransformer.flip(is.readLong());
        final double real = Double.longBitsToDouble(dr);
        final double imag = Double.longBitsToDouble(di);
        ndata[i] = new DoubleComplexNumber(real, imag);
      }
      for (int i = 0; i <= ddeg; i++) {
        final long dr = EndianTransformer.flip(is.readLong());
        final long di = EndianTransformer.flip(is.readLong());
        final double real = Double.longBitsToDouble(dr);
        final double imag = Double.longBitsToDouble(di);
        ddata[i] = new DoubleComplexNumber(real, imag);
      }
    }

    return new DoubleComplexRationalPolynomial(new DoubleComplexPolynomial(ndata, var), new DoubleComplexPolynomial(ddata, var));
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
    final StringBuffer sb = new StringBuffer();

    if (name.length() != 0) {
      sb.append(name);
      sb.append(" = "); //$NON-NLS-1$
    }

    sb.append(toMmString());

    if (withNewLine) {
      String newLine = System.getProperty("line.separator"); //$NON-NLS-1$
      sb.append(";"); //$NON-NLS-1$
      sb.append(newLine);
      sb.append(newLine);
    }

    output.write(sb.toString());
    output.flush();
  }

  /**
   * 倍精度複素数の2次元配列を生成します。
   * 
   * @param rowSize 行数
   * @param columnSize 列数
   * @param variableName 変数名
   * 
   * @return 倍精度複素数の2次元配列
   */
  public static DoubleComplexRationalPolynomial[][] createZeroArray(int rowSize, int columnSize, String variableName) {
    final DoubleComplexRationalPolynomial[][] elements = new DoubleComplexRationalPolynomial[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        elements[i][j] = new DoubleComplexRationalPolynomial(0, variableName);
      }
    }
    return elements;
  }

  /**
   * 値を加えた成分を生成します。
   * 
   * @param value 加える値
   * @return 足し算の結果
   */
  public DoubleComplexRationalPolynomial add(DoubleRationalPolynomial value) {
    return add(new DoubleComplexRationalPolynomial(value));
  }

  /**
   * 値を引きます。
   * 
   * @param value 引く値
   * @return 引き算の結果
   */
  public DoubleComplexRationalPolynomial subtract(DoubleRationalPolynomial value) {
    return subtract(new DoubleComplexRationalPolynomial(value));
  }

  /**
   * 値を掛けます。
   * 
   * @param value 掛ける値
   * @return 掛け算の結果
   */
  public DoubleComplexRationalPolynomial multiply(DoubleRationalPolynomial value) {
    return multiply(new DoubleComplexRationalPolynomial(value));
  }

  /**
   * 値で割ります。
   * 
   * @param value 割る値
   * @return 割り算の結果
   */
  public DoubleComplexRationalPolynomial divide(DoubleRationalPolynomial value) {
    return divide(new DoubleComplexRationalPolynomial(value));
  }

  /**
   * 値を割ります。
   * 
   * @param value 割られる値
   * @return 割り算の結果
   */
  public DoubleComplexRationalPolynomial leftDivide(DoubleRationalPolynomial value) {
    return leftDivide(new DoubleComplexRationalPolynomial(value));
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexMatrix getZeros() {
    return getNumerator().getRoots();
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexMatrix getPoles() {
    return getDenominator().getRoots();
  }
  
  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix createGrid(final int rowSize, final int columnSize, final DoubleComplexRationalPolynomial[][] elements) {
    return new DoubleComplexRationalPolynomialMatrix(rowSize, columnSize, elements);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomialMatrix createGrid(final DoubleComplexRationalPolynomial[] elements) {
    return new DoubleComplexRationalPolynomialMatrix(elements);
  }

  /**
   * {@inheritDoc}
   */
  public void setRealPart(int realPart) {
    setRealPart(new DoubleRationalPolynomial(realPart));
  }

  /**
   * {@inheritDoc}
   */
  public void setRealPart(double realPart) {
    setRealPart(new DoubleRationalPolynomial(realPart));
  }

  /**
   * {@inheritDoc}
   */
  public void setImaginaryPart(int imagPart) {
    setImaginaryPart(new DoubleRationalPolynomial(imagPart));
  }

  /**
   * {@inheritDoc}
   */
  public void setImaginaryPart(double imagPart) {
    setImaginaryPart(new DoubleRationalPolynomial(imagPart));
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial create(DoubleRationalPolynomial rePart, DoubleRationalPolynomial imPart) {
    DoubleComplexPolynomial num = new DoubleComplexPolynomial(rePart.getNumerator().multiply(imPart.getDenominator()), imPart.getNumerator().multiply(rePart.getDenominator()));
    DoubleComplexPolynomial den = new DoubleComplexPolynomial(rePart.getDenominator().multiply(imPart.getDenominator()));
    return new DoubleComplexRationalPolynomial(num,den);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial create(DoubleRationalPolynomial rePart) {
    return new DoubleComplexRationalPolynomial(rePart);
  }
}
