/*
 * Created on 2007/12/14
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.scalar;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mklab.nfc.matrix.DoubleComplexMatrix;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matx.MatxDouble;
import org.mklab.nfc.matx.MatxObject;
import org.mklab.nfc.matx.MxDataHead;
import org.mklab.nfc.random.DoubleUniformRandom;
import org.mklab.nfc.random.RandomGenerator;


/**
 * 倍精度型(double)の実数を表わすクラスです。
 * 
 * @author koga
 * @version $Revision: 1.46 $, 2007/12/14
 */
public class DoubleNumber extends AbstractNumericalScalar<DoubleNumber,DoubleMatrix> implements RealNumericalScalar<DoubleNumber,DoubleMatrix,DoubleComplexNumber,DoubleComplexMatrix>, MatxObject, FloatingPointOperator {

  /** シリアルバージョン。 */
  private static final long serialVersionUID = -4887458643703527016L;

  /** ゼロを「０」と表示するならばtrue。 */
  private static boolean displayAsZero = true;

  /** 整数を整数形式で表示するならばtrue。 */
  private static boolean displayAsIntegerValue = true;

  /** 機種精度(Machine Epsilon)。 */
  public static final DoubleNumber EPS = new DoubleNumber(DoubleNumberUtil.EPS);

  /** 実数データ 。*/
  private double data;
  
  /**
   * Mmフォーマットでファイルから読込む。
   * 
   * @param file ファイル
   * @return 読込んだ複素数
   * @throws IOException ファイルから読込めない場合
   */
  public static DoubleNumber readMmFormat(final File file) throws IOException {
    try (final Reader input = new InputStreamReader(new FileInputStream(file), "UTF-8")) { //$NON-NLS-1$
      final DoubleNumber ans = readMmFormat(input);
      return ans;
    }
  }

  /**
   * Mmフォーマットで入力ストリームから読込む。
   * 
   * @param input 入力ストリーム
   * @return 読込んだ複素数
   * @throws IOException 入力ストリームから読込めない場合
   */
  public static DoubleNumber readMmFormat(final Reader input) throws IOException {
    final BufferedReader reader = new BufferedReader(input);
    final String text = reader.readLine();
    
    final Pattern pattern = Pattern.compile("\\s*[^,\\s=]+\\s*=\\s*([^\\s;]+)\\s*;"); //$NON-NLS-1$
    final Matcher matcher = pattern.matcher(text);

    if (matcher.find() == false) {
      throw new NumberFormatException(text);
    }

    final double value = Double.parseDouble(matcher.group(1));
    
    return new DoubleNumber(value);
  }
  
  /**
   * MXフォーマットでファイルから読込みます。
   * 
   * @param file ファイル
   * @return 読込んだ実数
   * @throws IOException ファイルから読込めない場合
   */
  public static DoubleNumber readMxFormat(final File file) throws IOException {
    try (final DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
      final DoubleNumber ans = readMxFormat(input);
      return ans;
    }
  }

  /**
   * MXフォーマットで入力ストリームから読込みます。
   * 
   * @param input 入力ストリーム
   * @return 読込んだ実数
   * @throws IOException 入力ストリームから読込めない場合
   */
  public static DoubleNumber readMxFormat(final InputStream input) throws IOException {
    final MxDataHead head = new MxDataHead();
    head.read(input);
    return readMxFormat(head, input);
  }

  /**
   * MXフォーマットで入力ストリームから読込みます。 ヘッダ情報は先に取得しています。
   * 
   * @param input 入力ストリーム
   * @param head ヘッダ情報
   * @return 読込んだ実数
   * @throws IOException 入力ストリームから読込めない場合
   */
  public static DoubleNumber readMxFormat(final MxDataHead head, final InputStream input) throws IOException {
    final double ans= MatxDouble.readMxFormat(head, input);
    return new DoubleNumber(ans);
  }

  /**
   * 新しく生成された<code>DoubleNumber</code>オブジェクトを初期化します。
   * 
   * @param value データ
   */
  public DoubleNumber(final double value) {
    this.data = value;
  }

  /**
   * double型の値を返します。
   * 
   * @return double型の値
   */
  public final double doubleValue() {
    return this.data;
  }
  
  /**
   * Returns double number.
   * 
   * @param value value
   * @return double number
   */
  public static DoubleNumber valueOf(double value) {
    return new DoubleNumber(value);
  }

  /**
   * ゼロを「０」と表示するかを設定します。
   * 
   * @param displayAsZero ゼロを「０」と表示するならばtrue
   */
  public static void setDisplayAsZero(final boolean displayAsZero) {
    DoubleNumber.displayAsZero = displayAsZero;
  }

  /**
   * ゼロを「０」と表示するか判定します。
   * 
   * @return ゼロを「０」と表示するならばtrue
   */
  public static boolean isDisplayAsZero() {
    return DoubleNumber.displayAsZero;
  }

  /**
   * 整数を整数形式で表示するかを設定します。
   * 
   * @param displayAsIntegerFormat 整数を整数形式で表示するならばtrue、そうでなければfalse
   */
  public static void setDisplayAsIntegerFormat(final boolean displayAsIntegerFormat) {
    DoubleNumber.displayAsIntegerValue = displayAsIntegerFormat;
  }

  /**
   * 整数を整数形式で表示するか判定します。
   * 
   * @return 整数を整数形式で表示するならばtrue、そうでなければfalse
   */
  public static boolean isDisplayAsIntegerFormat() {
    return DoubleNumber.displayAsIntegerValue;
  }

  /**
   * 整数として出力するか判定します。
   * 
   * @param value 実数
   * @return 整数として出力するならばtrue、そうでなければfalse
   */
  private static boolean isDisplayingInteger(final double value) {
    return Math.round(value) == value;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber abs() {
    return new DoubleNumber(Math.abs(this.data));
  }

  /**
   * 符合(-1,0,1)を返します。
   * 
   * @return 符合(-1,0,1)
   */
  public final DoubleNumber signum() {
    return new DoubleNumber(DoubleNumberUtil.signum(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber abs2() {
    return new DoubleNumber(this.data * this.data);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber acos() {
    return new DoubleNumber(Math.acos(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber acosh() {
    return new DoubleNumber(DoubleNumberUtil.acosh(this.data));
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleNumber arg() {
//    return new DoubleNumber(0);
//  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber asin() {
    return new DoubleNumber(Math.asin(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber asinh() {
    return new DoubleNumber(DoubleNumberUtil.asinh(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber atan() {
    return new DoubleNumber(Math.atan(this.data));
  }

  /**
   * 逆正接(2)の値を返します。
   * 
   * @param value 分母側の数
   * @return 逆正接(2)の値
   */
  public final DoubleNumber atan2(final DoubleNumber value) {
    return new DoubleNumber(Math.atan2(this.data, value.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber atan2(final int value) {
    return new DoubleNumber(Math.atan2(this.data, value));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber atan2(final double value) {
    return new DoubleNumber(Math.atan2(this.data, value));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber atanh() {
    return new DoubleNumber(DoubleNumberUtil.atanh(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber cos() {
    return new DoubleNumber(Math.cos(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber cosh() {
    return new DoubleNumber(Math.cosh(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber exp() {
    return new DoubleNumber(Math.exp(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber log() {
    return new DoubleNumber(Math.log(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber log10() {
    return new DoubleNumber(Math.log10(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber power(final int scalar) {
    return new DoubleNumber(Math.pow(this.data, scalar));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber power(final double scalar) {
    return new DoubleNumber(Math.pow(this.data, scalar));
  }

  /**
   * 実数<code>scalar</code>乗(<code>this</code> <sup><code>scalar</code></sup>)を返します。
   * 
   * @param scalar 実数
   * @return scalar乗
   */
  public final DoubleNumber power(final DoubleNumber scalar) {
    return new DoubleNumber(Math.pow(this.data, scalar.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber sin() {
    return new DoubleNumber(Math.sin(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber sinh() {
    return new DoubleNumber(Math.sinh(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber sqrt() {
    return new DoubleNumber(Math.sqrt(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber tan() {
    return new DoubleNumber(Math.tan(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber tanh() {
    return new DoubleNumber(Math.tanh(this.data));
  }

  /**
   * 倍精度実数の和を返します。
   * 
   * @param value 加える倍精度実数
   * @return 倍精度実数の和
   */
  public final DoubleNumber add(final DoubleNumber value) {
    return new DoubleNumber(this.doubleValue() + value.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber add(final double value) {
    return new DoubleNumber(this.data + value);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber add(final int value) {
    return new DoubleNumber(this.data + value);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean compare(final String operator, final int opponent) {
    if (operator.equals(".!=")) { //$NON-NLS-1$
      if (this.data != opponent) {
        return true;
      }
    } else if (operator.equals(".==")) { //$NON-NLS-1$
      if (this.data == opponent) {
        return true;
      }
    } else if (operator.equals(".<")) { //$NON-NLS-1$
      if (this.data < opponent) {
        return true;
      }
    } else if (operator.equals(".<=")) { //$NON-NLS-1$
      if (this.data <= opponent) {
        return true;
      }
    } else if (operator.equals(".>")) { //$NON-NLS-1$
      if (this.data > opponent) {
        return true;
      }
    } else if (operator.equals(".>=")) { //$NON-NLS-1$
      if (this.data >= opponent) {
        return true;
      }
    } else {
      throw new IllegalArgumentException();
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean compare(final String operator, final double opponent) {
    if (operator.equals(".!=")) { //$NON-NLS-1$
      if (this.data != opponent) {
        return true;
      }
    } else if (operator.equals(".==")) { //$NON-NLS-1$
      if (this.data == opponent) {
        return true;
      }
    } else if (operator.equals(".<")) { //$NON-NLS-1$
      if (this.data < opponent) {
        return true;
      }
    } else if (operator.equals(".<=")) { //$NON-NLS-1$
      if (this.data <= opponent) {
        return true;
      }
    } else if (operator.equals(".>")) { //$NON-NLS-1$
      if (this.data > opponent) {
        return true;
      }
    } else if (operator.equals(".>=")) { //$NON-NLS-1$
      if (this.data >= opponent) {
        return true;
      }
    } else {
      throw new IllegalArgumentException();
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isGreaterThan(final DoubleNumber opponent) {
    return this.data > opponent.data;  
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isGreaterThanOrEquals(final DoubleNumber opponent) {
    return this.data >= opponent.data;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isLessThan(final DoubleNumber opponent) {
    return this.data < opponent.data;  
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isLessThanOrEquals(final DoubleNumber opponent) {
    return this.data <= opponent.data;  
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isGreaterThan(final int opponent) {
    return this.data > opponent;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isGreaterThan(final double opponent) {
    return this.data > opponent;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isGreaterThanOrEquals(final int opponent) {
    return this.data >= opponent;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isGreaterThanOrEquals(final double opponent) {
    return this.data >= opponent;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isLessThan(final int opponent) {
    return this.data < opponent;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isLessThan(final double opponent) {
    return this.data < opponent;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isLessThanOrEquals(final int opponent) {
    return this.data <= opponent;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isLessThanOrEquals(final double opponent) {
    return this.data <= opponent;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber conjugate() {
    return new DoubleNumber(this.data);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix createGrid(final int rowSize, final int columnSize, final DoubleNumber[][] elements) {
    return new DoubleMatrix(rowSize, columnSize, elements);
    
//    final double[][] realElements = new double[rowSize][columnSize];
//    for (int row = 0; row < rowSize; row++) {
//      for (int column = 0; column < columnSize; column++) {
//        realElements[row][column] = elements[row][column].doubleValue();
//      }
//    }
//
//    return new DoubleMatrix(realElements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleNumberMatrix createGrid(final int rowSize, final int columnSize, final DoubleNumber[][] elements) {
//    final double[][] realElements = new double[rowSize][columnSize];
//    for (int row = 0; row < rowSize; row++) {
//      for (int column = 0; column < columnSize; column++) {
//        realElements[row][column] = elements[row][column].doubleValue();
//      }
//    }
//
//    return new DoubleNumberMatrix(realElements);
//  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix createGrid(final DoubleNumber[] elements) {
    return new DoubleMatrix(elements);
    
//    final double[] realElements = new double[elements.length];
//    for (int row = 0; row < elements.length; row++) {
//      realElements[row] = elements[row].doubleValue();
//    }
//
//    return new DoubleMatrix(realElements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleNumberMatrix createGrid(final DoubleNumber[] elements) {
//    final double[] realElements = new double[elements.length];
//    for (int row = 0; row < elements.length; row++) {
//      realElements[row] = elements[row].doubleValue();
//    }
//
//    return new DoubleNumberMatrix(realElements);
//  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber createUnit() {
    return new DoubleNumber(1);
  }

  /**
   * 倍精度実数との商を返します。
   * 
   * @param value 割る
   * @return 倍精度実数との商
   */
  public final DoubleNumber divide(final DoubleNumber value) {
    return new DoubleNumber(this.doubleValue() / value.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber divide(final double value) {
    return new DoubleNumber(this.data / value);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber divide(final int value) {
    return new DoubleNumber(this.data / value);
  }

  /**
   * 許容範囲内で等しいか判定します。
   * 
   * @param opponent 比較する倍精度実数
   * @param tolerance 許容誤差
   * @return 許容範囲内で等しければtrue、そうでなければfalse
   */
  public final boolean equals(final DoubleNumber opponent, final double tolerance) {
    if (tolerance == 0) {
      return equals(opponent);
    }
    
    return DoubleNumberUtil.equals(this.data, opponent.data, tolerance);
  }

  /**
   * 許容範囲内で等しいか判定します。
   * 
   * @param opponent 比較する倍精度実数
   * @param tolerance 許容誤差
   * @return 許容範囲内で等しければtrue、そうでなければfalse
   */
  @Override
  public final boolean equals(final DoubleNumber opponent, final DoubleNumber tolerance) {
    if (tolerance.isZero()) {
      return equals(opponent);
    }
    
    if (isFinite() && opponent.isFinite()) {
      return new DoubleNumber(Math.abs(doubleValue() - opponent.doubleValue())).isLessThanOrEquals(tolerance);
    }
    
    return DoubleNumberUtil.equals(this.data, opponent.data);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber inverse() {
    return new DoubleNumber(1 / this.data);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isFinite() {
    return (!isInfinite()) && (!isNaN());
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isInfinite() {
    return Double.isInfinite(this.data);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isNaN() {
    return Double.isNaN(this.data);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit() {
    return this.data == 1;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit(final double tolerance) {
    return Math.abs(this.data - 1) <= tolerance;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit(final DoubleNumber tolerance) {
    return tolerance.isGreaterThanOrEquals(Math.abs(this.data - 1));
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero(final double tolerance) {
    return Math.abs(this.data) <= tolerance;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero(final DoubleNumber tolerance) {
    return tolerance.isGreaterThanOrEquals(Math.abs(this.data));
  }

  /**
   * 倍精度実数の左からの商を返します。
   * 
   * @param value 割られる数
   * @return 倍精度実数の左からの商
   */
  public final DoubleNumber leftDivide(final DoubleNumber value) {
    return new DoubleNumber(value.doubleValue() / doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber leftDivide(final double value) {
    return new DoubleNumber(value / this.data);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber leftDivide(final int value) {
    return new DoubleNumber(value / this.data);
  }

  /**
   * 倍精度実数の積を返します。
   * 
   * @param value 乗じる数
   * @return 倍精度実数の積
   */
  public final DoubleNumber multiply(final DoubleNumber value) {
    return new DoubleNumber(doubleValue() * value.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber multiply(final double value) {
    return new DoubleNumber(this.data * value);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber multiply(final int value) {
    return new DoubleNumber(this.data * value);
  }

  /**
   * 倍精度実数との差を返します。
   * 
   * @param value 引く数
   * @return 倍精度実数との差
   */
  public final DoubleNumber subtract(final DoubleNumber value) {
    return new DoubleNumber(doubleValue() - value.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber subtract(final double value) {
    return new DoubleNumber(this.data - value);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber subtract(final int value) {
    return new DoubleNumber(this.data - value);
  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public boolean isTransformableTo(final GridElement<? extends GridElement<?>> value) {
//    if (super.isTransformableTo(value)) {
//      return true;
//    }
//
//    if (value instanceof DoubleComplexNumber) {
//      return true;
//    }
//    
//    if (value instanceof Polynomial) {
//      return true;
//    }
//    
//    if (value instanceof RationalPolynomial) {
//      return true;
//    }
//
//    return false;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public GridElement<?> transformTo(final GridElement<? extends GridElement<?>> value) {
//    if (super.isTransformableTo(value)) {
//      return super.transformTo(value);
//    }
//
//    if (value instanceof DoubleComplexNumber) {
//      return new DoubleComplexNumber(doubleValue(), 0);
//    }
//    
//    if (value instanceof Polynomial) {
//      return ((Polynomial)value).create(doubleValue());
//    }
//    
//    if (value instanceof RationalPolynomial) {
//      return ((RationalPolynomial)value).create(doubleValue());
//    }
//
//    throw new IllegalArgumentException(Messages.getString("DoubleNumber.0")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public DoubleNumber transformFrom(final int value) {
//    return new DoubleNumber(value);
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public DoubleNumber transformFrom(final double value) {
//    return new DoubleNumber(value);
//  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber unaryMinus() {
    return new DoubleNumber(-this.data);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean compare(final String operator, final DoubleNumber opponent) {
    //if (opponent instanceof DoubleNumber) {
      return compare(operator, opponent.doubleValue());
    //}

    //return false;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber[] createArray(final int size) {
    return new DoubleNumber[size];
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleNumber[] createArray(final DoubleNumber[] elements) {
//    final int size = elements.length;
//
//    if (size != 0 && (elements[0] instanceof DoubleNumber) == false) {
//      throw new IllegalArgumentException();
//    }
//    
//    final DoubleNumber[] array = new DoubleNumber[size];
//    System.arraycopy(elements, 0, array, 0, size);
//    return array;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleNumber[][] createArray(final DoubleNumber[][] elements) {
//    final int rowSize = elements.length;
//    final int columnSize = rowSize == 0 ? 0 : elements[0].length;
//    
//    if (rowSize != 0 && columnSize !=0 && (elements[0][0] instanceof DoubleNumber) == false) {
//      throw new IllegalArgumentException();
//    }
//    
//    final DoubleNumber[][] array = new DoubleNumber[rowSize][columnSize];
//    for (int row = 0; row < rowSize; row++) {
//      System.arraycopy(elements[row], 0, array[row], 0, columnSize);
//    }
//    return array;
//  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber[][] createArray(final int rowSize, final int columnSize) {
    return new DoubleNumber[rowSize][columnSize];
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber create(final int value) {
    return new DoubleNumber(value);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber create(final double value) {
    return new DoubleNumber(value);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber createZero() {
    return new DoubleNumber(0);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero() {
    return this.data == 0;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber ceil() {
    return new DoubleNumber(Math.ceil(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber fix() {
    return new DoubleNumber(DoubleNumberUtil.fix(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber floor() {
    return new DoubleNumber(Math.floor(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber round() {
    return new DoubleNumber(Math.round(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber roundToZero(final double tolerance) {
    return new DoubleNumber(DoubleNumberUtil.roundToZero(this.data, tolerance));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber roundToZero(final DoubleNumber tolerance) {
      return new DoubleNumber(DoubleNumberUtil.roundToZero(this.data, tolerance.doubleValue()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DoubleNumber clone() {
    final DoubleNumber ans = super.clone();
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final String toMmString() {
    return toString();
  }

  /**
   * {@inheritDoc}
   */
  public final String toMmString(final String format) {
    return toString(format);
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMmFormat(final File file, final String dataName) throws IOException {
    new MatxDouble(doubleValue()).writeMmFormat(file, dataName);
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMmFormat(final Writer output, final String dataName, final boolean withNewLine) throws IOException {
    new MatxDouble(doubleValue()).writeMmFormat(output, dataName, withNewLine);
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMxFormat(final File file, final String dataName) throws IOException {
    new MatxDouble(doubleValue()).writeMxFormat(file, dataName);
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMxFormat(final DataOutputStream output, final String dataName) throws IOException {
    new MatxDouble(doubleValue()).writeMxFormat(output, dataName);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getAddOperator() {
//    return DoubleNumberAddOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getDivideOperator() {
//    return DoubleNumberDivideOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getLeftDivideOperator() {
//    return DoubleNumberLeftDivideOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getMultiplyOperator() {
//    return DoubleNumberMultiplyOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getSubtractOperator() {
//    return DoubleNumberSubtractOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getPowerOperator() {
//    return DoubleNumberPowerOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getAtan2Operator() {
//    return DoubleNumberAtan2Operator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleNumberEqual getEqualOperator() {
//    return DoubleNumberEqual.getInstance();
//  }

  /**
   * 文字列に変換します。
   * 
   * @param value 値
   * @param valueFormat 値のフォーマット
   * @return 変換結果の文字列
   */
  public static String toString(final double value, final String valueFormat) {
    if (Double.isNaN(value)) {
      return "NaN"; //$NON-NLS-1$
    }

    if (Double.isInfinite(value)) {
      if (value > 0) {
        return "Inf"; //$NON-NLS-1$
      }
      return "-Inf"; //$NON-NLS-1$
    }

    final boolean containG = valueFormat.contains("g") || valueFormat.contains("G"); //$NON-NLS-1$ //$NON-NLS-2$

    if (containG && DoubleNumber.displayAsIntegerValue && DoubleNumber.isDisplayingInteger(value)) {
      return String.valueOf(Math.round(value));
    }

    if (DoubleNumber.displayAsZero && value == 0) {
      return "0"; //$NON-NLS-1$
    }

    final String valueString = String.format(valueFormat, Double.valueOf(value));

    if (valueString.toUpperCase().contains("E")) { //$NON-NLS-1$
      return valueString;
    }

    return valueString.replaceFirst("(([1-9])|\\.)0+$", "$2").replaceFirst("^\\s+", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    // final String valueString2 = Format.sprintf(valueFormat, value).trim();
  }

  /**
   * {@inheritDoc}
   */
  public String toString(final String valueFormat) {
    return DoubleNumber.toString(this.data, valueFormat);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return toString(getFormat());
  }
  
  /**
   * 標準出力に出力(表示)します。 <p> 変数名はansです。
   */
  public final void print() {
    print("ans"); //$NON-NLS-1$
  }

  /**
   * 標準出力に出力します。
   * 
   * @param name 名前
   */
  public final void print(final String name) {
    try {
      PrintStream output = new PrintStream(System.out, false, "UTF-8"); //$NON-NLS-1$
      print(name, output);
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException(name, e);
    }
  }

  /**
   * 出力ストリームに出力します。
   * 
   * @param name 名前
   * @param output 出力ストリーム
   */
  public final void print(final String name, final PrintStream output) {
    output.println(name + " = " + toString()); //$NON-NLS-1$
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
//  public final DoubleNumber getImaginaryPart() {
//    return new DoubleNumber(0);
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleNumber getRealPart() {
//    return clone();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final void setRealPart(final int realPart) {
//    throw new UnsupportedOperationException(Messages.getString("DoubleNumber.6")); //$NON-NLS-1$
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final void setRealPart(final double realPart) {
//    throw new UnsupportedOperationException(Messages.getString("DoubleNumber.7")); //$NON-NLS-1$
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final void setRealPart(final Scalar<?> realPart) {
//    throw new UnsupportedOperationException(Messages.getString("DoubleNumber.8")); //$NON-NLS-1$
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final void setImaginaryPart(final int imagPart) {
//    throw new UnsupportedOperationException(Messages.getString("DoubleNumber.9")); //$NON-NLS-1$
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final void setImaginaryPart(final double imagPart) {
//    throw new UnsupportedOperationException(Messages.getString("DoubleNumber.10")); //$NON-NLS-1$
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final void setImaginaryPart(final Scalar<?> imagPart) {
//    throw new UnsupportedOperationException(Messages.getString("DoubleNumber.11")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final BaseComplexSymbolicScalar<DoubleNumber> toComplex() {
//    return new BaseComplexSymbolicScalar<>(this.clone(), createZero());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public final DoubleComplexNumber createImaginaryUnit() {
//    return new DoubleComplexNumber(0, 1);
//  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber createPI() {
    return new DoubleNumber(Math.PI);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber createE() {
    return new DoubleNumber(Math.E);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber valueOf(final String numberString) {
    return new DoubleNumber(Double.parseDouble(numberString));
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
    
    return DoubleNumberUtil.equals(this.data, ((DoubleNumber)opponent).data);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    final int prime = 31;
    hashCode = prime * hashCode + (int)(+serialVersionUID ^ (serialVersionUID >>> (prime + 1)));
    hashCode = prime * hashCode + (int)(Double.doubleToLongBits(this.data) ^ (Double.doubleToLongBits(this.data) >>> (prime + 1)));
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber getMachineEpsilon() {
    return DoubleNumber.EPS;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber getInfinity() {
    return new DoubleNumber(Double.POSITIVE_INFINITY);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber getNaN() {
    return new DoubleNumber(Double.NaN);
  }

  /**
   * {@inheritDoc}
   */
  public final RandomGenerator<DoubleNumber,DoubleMatrix>createUniformRandomGenerator() {
    return new DoubleUniformRandom();
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber nextUp() {
    return new DoubleNumber(Math.nextUp(this.data));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber nextDown() {
    return new DoubleNumber(Math.nextAfter(this.data, Double.NEGATIVE_INFINITY));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber nextAfter(final FloatingPointOperator direction) {
    if (direction instanceof DoubleNumber) {
      return new DoubleNumber(Math.nextAfter(this.data, ((DoubleNumber)direction).data));
    }
    throw new IllegalArgumentException();
  }
  
  /**
   * 値を加えた成分を生成します。
   * 
   * @param value 加える値
   * @return 足し算の結果
   */
  public DoubleComplexNumber add(DoubleComplexNumber value) {
    return  new DoubleComplexNumber(this).add(value);
  }

  /**
   * 値を引きます。
   * 
   * @param value 引く値
   * @return 引き算の結果
   */
  public DoubleComplexNumber subtract(DoubleComplexNumber value) {
    return  new DoubleComplexNumber(this).subtract(value);
  }

  /**
   * 値を掛けます。
   * 
   * @param value 掛ける値
   * @return 掛け算の結果
   */
  public DoubleComplexNumber multiply(DoubleComplexNumber value) {
    return  new DoubleComplexNumber(this).multiply(value);
  }


  /**
   * 値で割ります。
   * 
   * @param value 割る値
   * @return 割り算の結果
   */
  public DoubleComplexNumber divide(DoubleComplexNumber value) {
    return  new DoubleComplexNumber(this).divide(value);
  }

  /**
   * 値を割ります。
   * 
   * @param value 割られる値
   * @return 割り算の結果
   */
  public DoubleComplexNumber leftDivide(DoubleComplexNumber value) {
    return  new DoubleComplexNumber(this).leftDivide(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexNumber[] createComplexArray(DoubleNumber[] realPart, DoubleNumber[] imagPart) {
    final int size = realPart.length;
    final DoubleComplexNumber[] ans = new DoubleComplexNumber[size];
    for (int i = 0; i < size; i++) {
      ans[i] = new DoubleComplexNumber(realPart[i], imagPart[i]);
    }
    
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexNumber[] createComplexArray(DoubleNumber[] realPart) {
    final int size = realPart.length;
    final DoubleComplexNumber[] ans = new DoubleComplexNumber[size];
    for (int i = 0; i < size; i++) {
      ans[i] = new DoubleComplexNumber(realPart[i]);
    }
    
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexNumber[][] createComplexArray(DoubleNumber[][] realPart, DoubleNumber[][] imagPart) {
    final int rowSize = realPart.length;
    final int columnSize = realPart[0].length;
    final DoubleComplexNumber[][] ans = new DoubleComplexNumber[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
      ans[i][j] = new DoubleComplexNumber(realPart[i][j], imagPart[i][j]);
      }
    }
    
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexNumber[][] createComplexArray(DoubleNumber[][] realPart) {
    final int rowSize = realPart.length;
    final int columnSize = realPart[0].length;
    final DoubleComplexNumber[][] ans = new DoubleComplexNumber[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
      ans[i][j] = new DoubleComplexNumber(realPart[i][j]);
      }
    }
    
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexNumber toComplex() {
    return new DoubleComplexNumber(this);
  }

  /**
   * {@inheritDoc}
   */
  public double toDouble() {
    return this.data;
  }


}
