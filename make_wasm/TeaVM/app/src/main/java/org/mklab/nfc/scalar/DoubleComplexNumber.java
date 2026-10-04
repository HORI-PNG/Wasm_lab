/*
 * $Id: DoubleComplexNumber.java,v 1.10 2008/07/16 04:58:02 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
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
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mklab.nfc.matrix.DoubleComplexMatrix;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matx.MatxObject;
import org.mklab.nfc.matx.MxDataHead;
import org.mklab.nfc.random.DoubleComplexUniformRandom;
import org.mklab.nfc.util.EndianTransformer;


/**
 * 倍精度(double)型の値を実部および虚部とする複素数を表わすクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.10 $, 2004/06/22
 */
public class DoubleComplexNumber extends AbstractNumericalScalar<DoubleComplexNumber,DoubleComplexMatrix> implements ComplexNumericalScalar<DoubleNumber,DoubleMatrix,DoubleComplexNumber,DoubleComplexMatrix>, MatxObject {

  /** シリアルバージョン。 */
  private static final long serialVersionUID = 9107781747897062449L;

//  /** 機種精度(Machine Epsilon)。 */
//  private static final DoubleNumber EPS = new DoubleNumber(DoubleNumberUtil.EPS);

  /** 実部。 */
  private double realPart;

  /** 虚部。 */
  private double imagPart;

  /**
   * 新しく生成された<code>Complex</code>オブジェクトを初期化します。
   * 
   * @param realPart 実部
   * @param imagPart 虚部
   */
  public DoubleComplexNumber(final double realPart, final double imagPart) {
    this.realPart = realPart;
    this.imagPart = imagPart;
  }

  /**
   * 新しく生成された<code>Complex</code>オブジェクトを初期化します。
   * 
   * @param realPart 実部
   */
  public DoubleComplexNumber(final double realPart) {
    this.realPart = realPart;
    this.imagPart = 0;
  }

  /**
   * 新しく生成された<code>Complex</code>オブジェクトを初期化します。
   * 
   * @param realPart 実部
   * @param imagPart 虚部
   */
  public DoubleComplexNumber(final DoubleNumber realPart, final double imagPart) {
    this.realPart = realPart.doubleValue();
    this.imagPart = imagPart;
  }

  /**
   * 新しく生成された<code>Complex</code>オブジェクトを初期化します。
   * 
   * @param realPart 実部
   */
  public DoubleComplexNumber(final DoubleNumber realPart) {
    this.realPart = realPart.doubleValue();
    this.imagPart = 0;
  }

  /**
   * 新しく生成された<code>Complex</code>オブジェクトを初期化します。
   * 
   * @param realPart 実部
   * @param imagPart 虚部
   */
  public DoubleComplexNumber(final double realPart, final DoubleNumber imagPart) {
    this.realPart = realPart;
    this.imagPart = imagPart.doubleValue();
  }

  /**
   * 新しく生成された<code>Complex</code>オブジェクトを初期化します。
   * 
   * @param realPart 実部
   * @param imagPart 虚部
   */
  public DoubleComplexNumber(final DoubleNumber realPart, final DoubleNumber imagPart) {
    this.realPart = realPart.doubleValue();
    this.imagPart = imagPart.doubleValue();
  }

//  /**
//   * 新しく生成された<code>Complex</code>オブジェクトを初期化します。
//   * 
//   * @param value 複素数
//   */
//  public DoubleComplexNumber(final BaseComplexNumericalScalar<DoubleNumber, DoubleMatrix> value) {
//    this.realPart = value.getRealPart().doubleValue();
//    this.imagPart = value.getImaginaryPart().doubleValue();
//  }

  /**
   * 成分が零である{@link DoubleComplexNumber}の2次元配列を返します。
   * 
   * @param rowSize 行の数
   * @param columnSize 行の数
   * @return 成分が零である{@link DoubleComplexNumber}の2次元配列を返します。
   */
  public static DoubleComplexNumber[][] createZeroArray(final int rowSize, final int columnSize) {
    final DoubleComplexNumber[][] elements = new DoubleComplexNumber[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        elements[i][j] = new DoubleComplexNumber(0, 0);
      }
    }
    return elements;
  }

  /**
   * 複素数の2次元配列を返します。
   * 
   * @param realPart 実部の配列
   * @param imaginaryPart 虚部の配列
   * @return 複素数の2次元配列
   */
  public static DoubleComplexNumber[][] createArray(final double[][] realPart, final double[][] imaginaryPart) {
    final int rowSize = realPart.length;
    final int columnSize = (rowSize == 0 || realPart[0] == null) ? 0 : realPart[0].length;

    final DoubleComplexNumber[][] elements = new DoubleComplexNumber[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        elements[i][j] = new DoubleComplexNumber(realPart[i][j], imaginaryPart[i][j]);
      }
    }
    return elements;
  }

  /**
   * 複素数の2次元配列を返します。
   * 
   * @param realPart 実部の配列
   * @return 複素数の2次元配列
   */
  public static DoubleComplexNumber[][] createArray(final double[][] realPart) {
    final int rowSize = realPart.length;
    final int columnSize = (rowSize == 0 || realPart[0] == null) ? 0 : realPart[0].length;

    final DoubleComplexNumber[][] elements = new DoubleComplexNumber[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        elements[i][j] = new DoubleComplexNumber(realPart[i][j], 0);
      }
    }
    return elements;
  }

  /**
   * 複素数の2次元配列を返します。
   * 
   * @param realPart 実部の配列
   * @param imaginaryPart 虚部の配列
   * @return 複素数の2次元配列
   */
  public static DoubleComplexNumber[][] createArray(final DoubleNumber[][] realPart, final DoubleNumber[][] imaginaryPart) {
    final int rowSize = realPart.length;
    final int columnSize = (rowSize == 0 || realPart[0] == null) ? 0 : realPart[0].length;

    final DoubleComplexNumber[][] elements = new DoubleComplexNumber[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        elements[i][j] = new DoubleComplexNumber(realPart[i][j], imaginaryPart[i][j]);
      }
    }
    return elements;
  }

  /**
   * 複素数の2次元配列を返します。
   * 
   * @param realPart 実部の配列
   * @return 複素数の2次元配列
   */
  public static DoubleComplexNumber[][] createArray(final DoubleNumber[][] realPart) {
    final int rowSize = realPart.length;
    final int columnSize = (rowSize == 0 || realPart[0] == null) ? 0 : realPart[0].length;

    final DoubleComplexNumber[][] elements = new DoubleComplexNumber[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        elements[i][j] = new DoubleComplexNumber(realPart[i][j], 0);
      }
    }
    return elements;
  }

//  /**
//   * 倍精度複素数の2次元配列を生成します。
//   * 
//   * @param values 倍精度複素数の配列
//   * @return 倍精度複素数の2次元配列
//   */
//  public static DoubleComplexNumber[][] createArray(final BaseComplexNumericalScalar<DoubleNumber, DoubleMatrix>[][] values) {
//    final int rowSize = values.length;
//    final int columnSize = (rowSize == 0 || values[0] == null) ? 0 : values[0].length;
//
//    final DoubleComplexNumber[][] elements = new DoubleComplexNumber[rowSize][columnSize];
//
//    for (int i = 0; i < rowSize; i++) {
//      for (int j = 0; j < columnSize; j++) {
//        elements[i][j] = new DoubleComplexNumber(values[i][j].getRealPart().doubleValue(), values[i][j].getImaginaryPart().doubleValue());
//      }
//    }
//    return elements;
//  }

//  /**
//   * 倍精度複素数の1次元配列を生成します。
//   * 
//   * @param elements 倍精度複素数の配列
//   * @return 倍精度複素数の配列
//   */
//  public static DoubleComplexNumber[] createArray(final BaseComplexNumericalScalar<DoubleNumber, DoubleMatrix>[] elements) {
//    final int size = elements.length;
//
//    final DoubleComplexNumber[] array = new DoubleComplexNumber[size];
//    for (int i = 0; i < size; i++) {
//      array[i] = new DoubleComplexNumber(elements[i]);
//    }
//
//    return array;
//  }

  /**
   * 
   * 複素数の1次元配列を返します。
   * 
   * @param realPart 実部の配列
   * @param imaginaryPart 虚部の配列
   * @return 複素数の1次元配列
   */
  public static DoubleComplexNumber[] createArray(final double[] realPart, final double[] imaginaryPart) {
    final DoubleComplexNumber[] elements = new DoubleComplexNumber[realPart.length];

    for (int i = 0; i < realPart.length; i++) {
      elements[i] = new DoubleComplexNumber(realPart[i], imaginaryPart[i]);
    }
    return elements;
  }

  /**
   * 
   * 複素数の1次元配列を返します。
   * 
   * @param realPart 実部の配列
   * @return 複素数の1次元配列
   */
  public static DoubleComplexNumber[] createArray(final double[] realPart) {
    final DoubleComplexNumber[] elements = new DoubleComplexNumber[realPart.length];

    for (int i = 0; i < realPart.length; i++) {
      elements[i] = new DoubleComplexNumber(realPart[i], 0);
    }
    return elements;
  }

  /**
   * 複素数の1次元配列を返します。
   * 
   * @param realPart 実部の配列
   * @param imaginaryPart 虚部の配列
   * @return 複素数の1次元配列
   */
  public static DoubleComplexNumber[] createArray(final DoubleNumber[] realPart, final DoubleNumber[] imaginaryPart) {
    final DoubleComplexNumber[] elements = new DoubleComplexNumber[realPart.length];

    for (int i = 0; i < realPart.length; i++) {
      elements[i] = new DoubleComplexNumber(realPart[i], imaginaryPart[i]);
    }
    return elements;
  }

  /**
   * 複素数の1次元配列を返します。
   * 
   * @param realPart 実部の配列
   * @return 複素数の1次元配列
   */
  public static DoubleComplexNumber[] createArray(final DoubleNumber[] realPart) {
    final DoubleComplexNumber[] elements = new DoubleComplexNumber[realPart.length];

    for (int i = 0; i < realPart.length; i++) {
      elements[i] = new DoubleComplexNumber(realPart[i], 0);
    }
    return elements;
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

    final DoubleComplexNumber c = (DoubleComplexNumber)opponent;

    final boolean isRealPartEqual = DoubleNumberUtil.equals(this.realPart, c.realPart);

    if (isRealPartEqual == false) {
      return false;
    }

    final boolean isImagPartEqual = DoubleNumberUtil.equals(this.imagPart, c.imagPart);

    return isImagPartEqual;
  }

  /**
   * Override hashCode.
   * 
   * @return the Objects hash code.
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    final int prime = 31;
    hashCode = prime * hashCode + (int)(+serialVersionUID ^ (serialVersionUID >>> (prime + 1)));
    hashCode = prime * hashCode + (int)(Double.doubleToLongBits(this.realPart) ^ (Double.doubleToLongBits(this.realPart) >>> (prime + 1)));
    hashCode = prime * hashCode + (int)(Double.doubleToLongBits(this.imagPart) ^ (Double.doubleToLongBits(this.imagPart) >>> (prime + 1)));
    return hashCode;
  }

  /**
   * 許容範囲内で等しいか判定します。
   * 
   * @param opponent 比較する複素数成分
   * @param tolerance 許容誤差
   * @return 許容範囲内で等しければtrue、そうでなければfalse
   */
  public final boolean equals(final DoubleComplexNumber opponent, final DoubleNumber tolerance) {
    return equals(opponent, tolerance.doubleValue());
  }
  
  /**
   * 許容範囲内で等しいか判定します。
   * 
   * @param opponent 比較する複素数成分
   * @param tolerance 許容誤差
   * @return 許容範囲内で等しければtrue、そうでなければfalse
   */
  public final boolean equals(final DoubleComplexNumber opponent, final double tolerance) {
    if (tolerance == 0) {
      return equals(opponent);
    }

    final boolean reEqual = DoubleNumberUtil.equals(this.realPart, opponent.realPart, tolerance);

    if (reEqual == false) {
      return false;
    }

    boolean imEqual = DoubleNumberUtil.equals(this.imagPart, opponent.imagPart, tolerance);

    return imEqual;
  }

  /**
   * 許容範囲内で等しいか判定します。
   * 
   * @param opponent 比較する複素数成分
   * @param tolerance 許容誤差
   * @return 許容範囲内で等しければtrue、そうでなければfalse
   */
  public final boolean equals(final DoubleComplexNumber opponent, final DoubleComplexNumber tolerance) {
    if (tolerance.isZero()) {
      return equals(opponent);
    }

    boolean reEqual = false;
    if (DoubleNumberUtil.isFinite(this.realPart) && DoubleNumberUtil.isFinite(opponent.realPart)) {
      reEqual = tolerance.isGreaterThan(Math.abs(this.realPart - opponent.realPart));
    } else {
      reEqual = DoubleNumberUtil.equals(this.realPart, opponent.realPart);
    }

    if (reEqual == false) {
      return false;
    }

    boolean imEqual = false;
    if (DoubleNumberUtil.isFinite(this.imagPart) && DoubleNumberUtil.isFinite(opponent.imagPart)) {
      imEqual = tolerance.isGreaterThan(Math.abs(this.imagPart - opponent.imagPart));
    } else {
      imEqual = DoubleNumberUtil.equals(this.imagPart, opponent.imagPart);
    }

    return imEqual;
  }

  /**
   * 実数と等しいか判定します。
   * 
   * @param opponent 比較する実数
   * @return 等しければtrue、そうでなければfalse
   */
  public final boolean equals(final double opponent) {
    if (Math.abs(this.imagPart) != 0) {
      return false;
    }

    return DoubleNumberUtil.equals(this.realPart, opponent);
  }

  /**
   * 実数と等しいか判定します。
   * 
   * @param opponent 比較する実数
   * @return 等しければtrue、そうでなければfalse
   */
  public final boolean equals(final DoubleNumber opponent) {
    return equals(opponent.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DoubleComplexNumber clone() {
    final DoubleComplexNumber ans = super.clone();
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return toString(getFormat());
  }

  /**
   * {@inheritDoc}
   */
  public final String toString(final String valueFormat) {
    return "(" + DoubleNumber.toString(this.realPart, valueFormat) + "," + DoubleNumber.toString(this.imagPart, valueFormat) + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

  /**
   * 実部を設定します。
   * 
   * @param realPart 実部
   */
  public final void setRealPart(final int realPart) {
    this.realPart = realPart;
  }

  /**
   * 実部を設定します。
   * 
   * @param realPart 実部
   */
  public final void setRealPart(final double realPart) {
    this.realPart = realPart;
  }

  /**
   * 実部を設定します。
   * 
   * @param realPart 実部
   */
  public final void setRealPart(final DoubleNumber realPart) {
    this.realPart = realPart.doubleValue();
  }

  //  /**
  //   * 実部を設定します。
  //   * 
  //   * @param realPart 実部
  //   */
  //  public final void setRealPart(final Scalar<?> realPart) {
  //    if (realPart instanceof DoubleNumber) {
  //      this.realPart = ((DoubleNumber)realPart).doubleValue();
  //      return;
  //    }
  //
  //    throw new IllegalArgumentException(Messages.getString("DoubleComplexNumber.4")); //$NON-NLS-1$
  //  }

  /**
   * 実部を返します。
   * 
   * @return 実部
   */
  public final DoubleNumber getRealPart() {
    return new DoubleNumber(this.realPart);
  }

  /**
   * 虚部を設定します。
   * 
   * @param imaginaryPart 虚部
   */
  public final void setImaginaryPart(final int imaginaryPart) {
    this.imagPart = imaginaryPart;
  }

  /**
   * 虚部を設定します。
   * 
   * @param imaginaryPart 虚部
   */
  public final void setImaginaryPart(final double imaginaryPart) {
    this.imagPart = imaginaryPart;
  }

  /**
   * 虚部を設定します。
   * 
   * @param imaginaryPart 虚部
   */
  public final void setImaginaryPart(final DoubleNumber imaginaryPart) {
    this.imagPart = imaginaryPart.doubleValue();
  }

  //  /**
  //   * 虚部を設定します。
  //   * 
  //   * @param imaginaryPart 虚部
  //   */
  //  public final void setImaginaryPart(final Scalar<?> imaginaryPart) {
  //    if (imaginaryPart instanceof DoubleNumber) {
  //      this.imagPart = ((DoubleNumber)imaginaryPart).doubleValue();
  //      return;
  //    }
  //
  //    throw new IllegalArgumentException(Messages.getString("DoubleComplexNumber.5")); //$NON-NLS-1$
  //  }

  /**
   * 虚部を返します。
   * 
   * @return 虚部
   */
  public final DoubleNumber getImaginaryPart() {
    return new DoubleNumber(this.imagPart);
  }

  /**
   * 複素数との和を返します。
   * 
   * @param value 加える複素数
   * @return 複素数との和
   */
  public final DoubleComplexNumber add(final DoubleComplexNumber value) {
    return new DoubleComplexNumber(this.realPart + value.realPart, this.imagPart + value.imagPart);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber add(final double value) {
    return new DoubleComplexNumber(this.realPart + value, this.imagPart);
  }

  /**
   * 値を加えます。
   * 
   * @param value 加える値
   * @return 足し算の結果
   */
  public final DoubleComplexNumber add(final DoubleNumber value) {
    return add(value.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber add(final int value) {
    return new DoubleComplexNumber(this.realPart + value, this.imagPart);
  }

//  /**
//   * 自身に実数を加えます。
//   * 
//   * @param value 加える実数
//   * @return 自身
//   */
//  public final DoubleComplexNumber addSelft(final double value) {
//    this.realPart += value;
//    return this;
//  }

  /**
   * 複素数との差を返します。
   * 
   * @param value 引く複素数
   * @return 複素数との差
   */
  public final DoubleComplexNumber subtract(final DoubleComplexNumber value) {
    return new DoubleComplexNumber(this.realPart - value.realPart, this.imagPart - value.imagPart);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber subtract(final double value) {
    return new DoubleComplexNumber(this.realPart - value, this.imagPart);
  }

  /**
   * 値を引く。
   * 
   * @param value 引く値
   * @return 引き算の結果
   */
  public final DoubleComplexNumber subtract(final DoubleNumber value) {
    return subtract(value.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber subtract(final int value) {
    return new DoubleComplexNumber(this.realPart - value, this.imagPart);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber multiply(final DoubleComplexNumber value) {
    return new DoubleComplexNumber(this.realPart * value.realPart - this.imagPart * value.imagPart, this.realPart * value.imagPart + this.imagPart * value.realPart);
  }

//  /**
//   * 自身に複素数を乗じます。
//   * 
//   * @param value 乗じる複素数
//   * @return 自身
//   */
//  public final DoubleComplexNumber multiplySelf(final DoubleComplexNumber value) {
//    final double re = this.realPart * value.realPart - this.imagPart * value.imagPart;
//    final double im = this.realPart * value.imagPart + this.imagPart * value.realPart;
//    this.realPart = re;
//    this.imagPart = im;
//    return this;
//  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber multiply(final double value) {
    return new DoubleComplexNumber(this.realPart * value, this.imagPart * value);
  }

  /**
   * 値を掛けます。
   * 
   * @param value 掛ける値
   * @return 掛け算の結果
   */
  public final DoubleComplexNumber multiply(final DoubleNumber value) {
    return multiply(value.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber multiply(final int value) {
    return new DoubleComplexNumber(this.realPart * value, this.imagPart * value);
  }

  /**
   * 自身に実数を乗じます。
   * 
   * @param value 乗じる実数
   * @return 自身
   */
  public final DoubleComplexNumber multiplySelf(final double value) {
    this.realPart *= value;
    this.imagPart *= value;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber conjugate() {
    return new DoubleComplexNumber(this.realPart, -this.imagPart);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber inverse() {
    final DoubleComplexNumber conj = this.conjugate();
    final double denominator = this.multiply(conj).realPart;
    return new DoubleComplexNumber(conj.realPart / denominator, conj.imagPart / denominator);
  }

  /**
   * 複素数との商(<code>this</code>*<code>value</code> <sup>-1 </sup>)を返します。
   * 
   * @param value 割る複素数
   * @return 複素数との商(this*c <sup>-1 </sup>)
   */
  public final DoubleComplexNumber divide(final DoubleComplexNumber value) {
    return multiply(value.inverse());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber divide(final double value) {
    return new DoubleComplexNumber(this.realPart / value, this.imagPart / value);
  }

  /**
   * 値で割ります。
   * 
   * @param value 割る値
   * @return 割り算の結果
   */
  public final DoubleComplexNumber divide(final DoubleNumber value) {
    return divide(value.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber divide(final int value) {
    return new DoubleComplexNumber(this.realPart / value, this.imagPart / value);
  }

  /**
   * 自身の逆数と複素数cの積(<code>this</code> <sup>-1 </sup>*<code>value</code>)を返します。
   * 
   * @param value 複素数
   * @return 自身の逆数とcの積
   */
  public final DoubleComplexNumber leftDivide(final DoubleComplexNumber value) {
    return inverse().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber leftDivide(final double value) {
    return DoubleComplexNumberUtil.multiply(value, inverse());
  }

  /**
   * 値を割ります。
   * 
   * @param value 割られる値
   * @return 割り算の結果
   */
  public final DoubleComplexNumber leftDivide(final DoubleNumber value) {
    return leftDivide(value.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber leftDivide(final int value) {
    return DoubleComplexNumberUtil.multiply(value, inverse());
  }

  /**
   * 複素数<code>scalar</code>乗(<code>this</code> <sup><code>scalar</code></sup>)を返します。
   * 
   * @param scalar 複素数
   * @return 自身のc乗
   */
  public final DoubleComplexNumber power(final DoubleComplexNumber scalar) {
    final double t = Math.atan2(this.imagPart, this.realPart);
    final DoubleComplexNumber r = DoubleComplexNumberUtil.power(Math.sqrt(this.realPart * this.realPart + this.imagPart * this.imagPart), scalar).multiply(Math.pow(Math.E, -scalar.imagPart * t));
    final double th = scalar.realPart * t;
    return r.multiply(new DoubleComplexNumber(Math.cos(th), Math.sin(th)));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber power(final int scalar) {
    return power((double)scalar);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber power(final double scalar) {
    final double length = Math.pow(Math.sqrt(this.realPart * this.realPart + this.imagPart * this.imagPart), scalar);
    final double th = Math.atan2(this.imagPart, this.realPart) * scalar;
    return new DoubleComplexNumber(length * Math.cos(th), length * Math.sin(th));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber unaryMinus() {
    return new DoubleComplexNumber(-this.realPart, -this.imagPart);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber fix() {
    return new DoubleComplexNumber(DoubleNumberUtil.fix(this.realPart), DoubleNumberUtil.fix(this.imagPart));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber round() {
    return new DoubleComplexNumber(Math.rint(this.realPart), Math.rint(this.imagPart));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber roundToZero(final double tolerance) {
    return new DoubleComplexNumber(DoubleNumberUtil.roundToZero(this.realPart, tolerance), DoubleNumberUtil.roundToZero(this.imagPart, tolerance));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber roundToZero(final DoubleComplexNumber tolerance) {
    final double doubleTolerance = tolerance.getRealPart().doubleValue();
    return new DoubleComplexNumber(DoubleNumberUtil.roundToZero(this.realPart, doubleTolerance), DoubleNumberUtil.roundToZero(this.imagPart, doubleTolerance));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber ceil() {
    return new DoubleComplexNumber(Math.ceil(this.realPart), Math.ceil(this.imagPart));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber floor() {
    return new DoubleComplexNumber(Math.floor(this.realPart), Math.floor(this.imagPart));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber abs() {
    return new DoubleComplexNumber(Math.sqrt(this.realPart * this.realPart + this.imagPart * this.imagPart), 0);
  }

  /**
   * (値/絶対値)を返します。
   * 
   * @return (値/絶対値)
   */
  public final DoubleComplexNumber signum() {
    if (isZero()) {
      return new DoubleComplexNumber(0, 0);
    }
    return this.divide(this.abs());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber abs2() {
    return new DoubleComplexNumber(this.realPart * this.realPart + this.imagPart * this.imagPart, 0);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber sqrt() {
    final double[] ans = DoubleComplexNumberUtil.sqrt(this.realPart, this.imagPart);
    return new DoubleComplexNumber(ans[0], ans[1]);
  }

  /**
   * 標準出力に出力します。
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
  public final boolean isZero() {
    return this.realPart == 0 && this.imagPart == 0;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero(final double tolerance) {
    return Math.abs(this.realPart) <= tolerance && Math.abs(this.imagPart) <= tolerance;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero(final DoubleComplexNumber tolerance) {
    final DoubleNumber realTolerance = tolerance.getRealPart();
    return realTolerance.isGreaterThanOrEquals(Math.abs(this.realPart)) && realTolerance.isGreaterThanOrEquals(Math.abs(this.imagPart));
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit() {
    final boolean isRealEqual = Double.doubleToLongBits(this.realPart - 1) == 0L;
    return isRealEqual && this.imagPart == 0;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit(final double tolerance) {
    return Math.abs(this.realPart - 1) <= tolerance && Math.abs(this.imagPart) <= tolerance;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit(final DoubleComplexNumber tolerance) {
    final DoubleNumber realTolerance = tolerance.getRealPart();
    return realTolerance.isGreaterThanOrEquals(Math.abs(this.realPart - 1)) && realTolerance.isGreaterThanOrEquals(Math.abs(this.imagPart));
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isNaN() {
    return Double.isNaN(this.realPart) || Double.isNaN(this.imagPart);
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
    return Double.isInfinite(this.realPart) || Double.isInfinite(this.imagPart);
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
    final MxDataHead head = MxDataHead.createDataHeadForComplexNumber(name);
    head.write(output);

    output.writeDouble(this.realPart);
    output.writeDouble(this.imagPart);
    output.flush();
  }

  /**
   * MXフォーマットでファイルから読込む。
   * 
   * @param file ファイル
   * @return 読込んだ複素数
   * @throws IOException ファイルから読込めない場合
   */
  public static DoubleComplexNumber readMxFormat(final File file) throws IOException {
    try (final DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
      final DoubleComplexNumber ans = readMxFormat(input);
      return ans;
    }
  }

  /**
   * MXフォーマットで入力ストリームから読込む。
   * 
   * @param input 入力ストリーム
   * @return 読込んだ複素数
   * @throws IOException 入力ストリームから読込めない場合
   */
  public static DoubleComplexNumber readMxFormat(final InputStream input) throws IOException {
    final MxDataHead head = new MxDataHead();
    head.read(input);
    return readMxFormat(head, input);
  }

  /**
   * MXフォーマットで入力ストリームから読込む。 ヘッダ情報は指定している。
   * 
   * @param input 入力ストリーム
   * @param head ヘッダ情報
   * @return 読込んだ複素数
   * @throws IOException 入力ストリームから読込めない場合
   */
  public static DoubleComplexNumber readMxFormat(final MxDataHead head, final InputStream input) throws IOException {
    final DataInputStream is = new DataInputStream(input);

    double real, imag;
    if (head.isSameEndian()) {
      real = is.readDouble();
      imag = is.readDouble();
    } else {
      real = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
      imag = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
    }
    return new DoubleComplexNumber(real, imag);
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
    final StringBuffer code = new StringBuffer();

    if (name.length() != 0) {
      code.append(name);
      code.append(" = "); //$NON-NLS-1$
    }

    code.append(toString());

    if (withNewLine) {
      final String newLineChar = System.getProperty("line.separator"); //$NON-NLS-1$
      code.append(";"); //$NON-NLS-1$
      code.append(newLineChar);
      code.append(newLineChar);
    }

    output.write(code.toString());
    output.flush();
  }

  /**
   * 偏角を返します。
   * 
   * @return 偏角
   */
  public final DoubleNumber arg() {
    return new DoubleNumber(Math.atan2(this.imagPart, this.realPart));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber log() {
    final double w1 = this.realPart * this.realPart + this.imagPart * this.imagPart;
    double w2;

    if (this.realPart == 0.0) {
      w2 = Math.PI / 2.0;
      if (this.imagPart < 0.0) {
        w2 = -w2;
      }
    } else {
      w2 = Math.atan2(this.imagPart, this.realPart);
    }

    return new DoubleComplexNumber(Math.log(Math.sqrt(w1)), w2);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber log10() {
    final DoubleComplexNumber b = this.log();
    final double re = b.realPart / Math.log(10);
    final double im = b.imagPart / Math.log(10);
    return new DoubleComplexNumber(re, im);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber exp() {
    final double w = Math.exp(this.realPart);
    final double im = this.imagPart;
    return new DoubleComplexNumber(w * Math.cos(im), w * Math.sin(im));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber sin() {
    final double w1 = Math.exp(this.imagPart);
    final double w2 = 1.0 / w1;
    return new DoubleComplexNumber(Math.sin(this.realPart) * (w1 + w2) * 0.5, Math.cos(this.realPart) * (w1 - w2) * 0.5);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber asin() {
    // asin(a) = - i log(i*a + sqrt(1 - a*a))

    DoubleComplexNumber w1 = new DoubleComplexNumber(1.0 - this.realPart * this.realPart + this.imagPart * this.imagPart, -2.0 * this.realPart * this.imagPart);
    w1 = w1.sqrt();
    w1.realPart = w1.realPart - this.imagPart;
    w1.imagPart = w1.imagPart + this.realPart;
    w1 = w1.log();

    final double tmp = -w1.realPart;
    w1.realPart = w1.imagPart;
    w1.imagPart = tmp;

    return w1;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber sinh() {
    // sinh(a) = (exp(a) - exp(-a))/2
    final double w1 = Math.exp(this.realPart);
    final double w2 = Math.exp(-this.realPart);

    return new DoubleComplexNumber((w1 * Math.cos(this.imagPart) - w2 * Math.cos(-this.imagPart)) / 2.0, (w1 * Math.sin(this.imagPart) - w2 * Math.sin(-this.imagPart)) / 2.0);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber asinh() {
    // asinh(a) = log(a + sqrt(a*a + 1))
    DoubleComplexNumber w1 = new DoubleComplexNumber(this.realPart * this.realPart - this.imagPart * this.imagPart + 1.0, 2.0 * this.realPart * this.imagPart);
    w1 = w1.sqrt();

    w1.realPart = w1.realPart + this.realPart;
    w1.imagPart = w1.imagPart + this.imagPart;
    w1 = w1.log();
    return w1;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber cos() {
    final double w1 = Math.exp(this.imagPart);
    final double w2 = 1.0 / w1;
    return new DoubleComplexNumber(Math.cos(this.realPart) * (w1 + w2) * 0.5, Math.sin(this.realPart) * (w2 - w1) * 0.5);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber acos() {
    // acos(a) = - i log(a + sqrt(a*a - 1.0))
    DoubleComplexNumber w1 = new DoubleComplexNumber(this.realPart * this.realPart - this.imagPart * this.imagPart - 1.0, 2.0 * this.realPart * this.imagPart);
    w1 = w1.sqrt();
    w1.realPart = w1.realPart + this.realPart;
    w1.imagPart = w1.imagPart + this.imagPart;
    w1 = w1.log();

    final double tmp = -w1.realPart;
    w1.realPart = w1.imagPart;
    w1.imagPart = tmp;

    return w1;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber acosh() {
    // acosh(a) = log(a + sqrt(a*a - 1))
    DoubleComplexNumber w1 = new DoubleComplexNumber(this.realPart * this.realPart - this.imagPart * this.imagPart - 1.0, 2.0 * this.realPart * this.imagPart);
    w1 = w1.sqrt();
    w1.realPart = w1.realPart + this.realPart;
    w1.imagPart = w1.imagPart + this.imagPart;
    w1 = w1.log();
    return w1;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber cosh() {
    // cosh(a) = (exp(a) + exp(-a))/2
    final double w1 = Math.exp(this.realPart);
    final double w2 = Math.exp(-this.realPart);

    return new DoubleComplexNumber((w1 * Math.cos(this.imagPart) + w2 * Math.cos(-this.imagPart)) / 2.0, (w1 * Math.sin(this.imagPart) + w2 * Math.sin(-this.imagPart)) / 2.0);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber tan() {
    final DoubleComplexNumber w1 = this.sin();
    final DoubleComplexNumber w2 = this.cos();
    return w1.divide(w2);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber atan() {
    // atan(a) = 1/2i log((1+i*a)/(1-i*a))
    DoubleComplexNumber w1 = new DoubleComplexNumber(1.0 - this.imagPart, this.realPart);
    final DoubleComplexNumber w2 = new DoubleComplexNumber(1.0 + this.imagPart, -this.realPart);
    w1 = w1.divide(w2).log();

    w2.realPart = w1.imagPart / 2.0;
    w2.imagPart = -w1.realPart / 2.0;

    return w2;
  }

  /**
   * 逆正接(2)の値を返します。
   * 
   * @param value 分母側の数
   * @return 逆正接(2)の値
   */
  @Override
  public final DoubleComplexNumber atan2(final DoubleComplexNumber value) {
    if (getImaginaryPart().doubleValue() == 0 && value.getImaginaryPart().doubleValue() == 0) {
      return new DoubleComplexNumber(Math.atan2(getRealPart().doubleValue(), value.getRealPart().doubleValue()), 0);
    }
    final DoubleComplexNumber xy = this.divide(value);
    return xy.atan();
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber atan2(final int value) {
    if (getImaginaryPart().doubleValue() == 0) {
      return new DoubleComplexNumber(Math.atan2(getRealPart().doubleValue(), value), 0);
    }
    final DoubleComplexNumber xy = this.divide(value);
    return xy.atan();
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber atan2(final double value) {
    if (getImaginaryPart().doubleValue() == 0) {
      return new DoubleComplexNumber(Math.atan2(getRealPart().doubleValue(), value), 0);
    }
    final DoubleComplexNumber xy = this.divide(value);
    return xy.atan();
  }

  /**
   * 逆正接(2)の値を返します。
   * 
   * @param value 分母側の数
   * @return 逆正接(2)の値
   */
  public final DoubleComplexNumber atan2(final DoubleNumber value) {
    return atan2(value.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber tanh() {
    final DoubleComplexNumber w1 = this.sinh();
    final DoubleComplexNumber w2 = this.cosh();
    return w1.divide(w2);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber atanh() {
    // atanh(a) = 1/2 log((1+a)/(1-a))
    DoubleComplexNumber w1 = new DoubleComplexNumber(1.0 + this.realPart, this.imagPart);
    final DoubleComplexNumber w2 = new DoubleComplexNumber(1.0 - this.realPart, -this.imagPart);
    w1 = w1.divide(w2).log();

    w2.realPart = w1.realPart / 2.0;
    w2.imagPart = w1.imagPart / 2.0;
    return w2;
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
   * <code>opponent</code>を<code>operator</code>で指定された演算子で比較します。
   * 
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param opponent 比較対象
   * @return 比較式が正しければtrue、そうでなければfalse
   */
  public final boolean compare(final String operator, final DoubleComplexNumber opponent) {
    if (operator.equals(".!=")) { //$NON-NLS-1$
      return equals(opponent) == false;
    } else if (operator.equals(".==")) { //$NON-NLS-1$
      return equals(opponent);
    } else if (operator.equals(".<")) { //$NON-NLS-1$
      return isLessThan(opponent);
    } else if (operator.equals(".<=")) { //$NON-NLS-1$
      return isLessThanOrEquals(opponent);
    } else if (operator.equals(".>")) { //$NON-NLS-1$
      return isGreaterThan(opponent);
    } else if (operator.equals(".>=")) { //$NON-NLS-1$
      return isGreaterThanOrEquals(opponent);
    }

    throw new IllegalArgumentException();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean compare(final String operator, final double opponent) {
    if (operator.equals(".==")) { //$NON-NLS-1$
      if (this.imagPart != 0) {
        return false;
      }

      return DoubleNumberUtil.equals(this.realPart, opponent);

      //      final boolean isRealNotEqual = Double.doubleToLongBits(this.realPart) != Double.doubleToLongBits(opponent);
      //      if (isRealNotEqual || this.imagPart != 0) {
      //        return false;
      //      }
    }

    if (operator.equals(".!=")) { //$NON-NLS-1$
      if (this.imagPart != 0) {
        return true;
      }

      return DoubleNumberUtil.equals(this.realPart, opponent) == false;

      //final boolean isRealPartEqual = Double.doubleToLongBits(this.realPart) == Double.doubleToLongBits(opponent);
      //      final boolean isRealPartEqual = Double.doubleToLongBits(this.realPart) == Double.doubleToLongBits(opponent);
      //      if (isRealPartEqual && this.imagPart == 0) {
      //        return false;
      //      }
    }

    throw new IllegalArgumentException();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean compare(final String operator, final int opponent) {
    if (operator.equals(".==")) { //$NON-NLS-1$
      if (this.imagPart != 0) {
        return false;
      }

      return this.realPart == opponent;
    }

    if (operator.equals(".!=")) { //$NON-NLS-1$
      if (this.imagPart != 0) {
        return true;
      }

      return this.realPart != opponent;
    }
    
    if (operator.equals(".<")) { //$NON-NLS-1$
      if (this.imagPart != 0) {
        throw new IllegalArgumentException();
      }
      
      return this.realPart < opponent;
    }

    if (operator.equals(".<=")) { //$NON-NLS-1$
      if (this.imagPart != 0) {
        throw new IllegalArgumentException();
      }
      
      return this.realPart <= opponent;
    }
    
    if (operator.equals(".>")) { //$NON-NLS-1$
      if (this.imagPart != 0) {
        throw new IllegalArgumentException();
      }
      
      return this.realPart > opponent;
    }

    if (operator.equals(".>=")) { //$NON-NLS-1$
      if (this.imagPart != 0) {
        throw new IllegalArgumentException();
      }
      
      return this.realPart >= opponent;
    }


    throw new IllegalArgumentException();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isGreaterThan(final DoubleComplexNumber opponent) {
    return abs().getRealPart().doubleValue() > opponent.abs().getRealPart().doubleValue();
  }

  /**
   * <code>opponent</code>より大きいか判定します。
   * 
   * @param opponent 比較対象
   * @return <code>opponent</code>より大きいならばtrue、そうでなければfalse
   */
  public final boolean isGreaterThan(final DoubleNumber opponent) {
    return abs().getRealPart().doubleValue() > opponent.abs().doubleValue();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isGreaterThanOrEquals(final DoubleComplexNumber opponent) {
    return abs().getRealPart().doubleValue() >= opponent.abs().getRealPart().doubleValue();
  }

  /**
   * <code>opponent</code>以上であるか判定します。
   * 
   * @param opponent 比較対象
   * @return <code>opponent</code>以上ならばtrue、そうでなければfalse
   */
  public final boolean isGreaterThanOrEquals(final DoubleNumber opponent) {
    return abs().getRealPart().doubleValue() >= opponent.abs().doubleValue();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isLessThan(final DoubleComplexNumber opponent) {
    return abs().getRealPart().doubleValue() < opponent.abs().getRealPart().doubleValue();
  }

  /**
   * <code>opponent</code>より小さいか判定します。
   * 
   * @param opponent 比較対象
   * @return <code>opponent</code>より小さいならばtrue、そうでなければfalse
   */
  public final boolean isLessThan(final DoubleNumber opponent) {
    return abs().getRealPart().doubleValue() < opponent.abs().doubleValue();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isLessThanOrEquals(final DoubleComplexNumber opponent) {
    return abs().getRealPart().doubleValue() <= opponent.abs().getRealPart().doubleValue();
  }

  /**
   * <code>opponent</code>より小さいか判定します。
   * 
   * @param opponent 比較対象
   * @return <code>opponent</code>より小さいならばtrue、そうでなければfalse
   */
  public final boolean isLessThanOrEquals(final DoubleNumber opponent) {
    return abs().getRealPart().doubleValue() < opponent.abs().doubleValue();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isGreaterThan(final int opponent) {
    return abs().getRealPart().doubleValue() > opponent;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isGreaterThan(final double opponent) {
    return abs().getRealPart().doubleValue() > opponent;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isGreaterThanOrEquals(final int opponent) {
    return abs().getRealPart().doubleValue() >= opponent;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isGreaterThanOrEquals(final double opponent) {
    return abs().getRealPart().doubleValue() >= opponent;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isLessThan(final int opponent) {
    return abs().getRealPart().doubleValue() < opponent;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isLessThan(final double opponent) {
    return abs().getRealPart().doubleValue() < opponent;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isLessThanOrEquals(final int opponent) {
    return abs().getRealPart().doubleValue() <= opponent;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isLessThanOrEquals(final double opponent) {
    return abs().getRealPart().doubleValue() <= opponent;
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final DoubleComplexMatrix createGrid(final int rowSize, final int columnSize, final DoubleComplexNumber[][] elements) {
  //    return new DoubleComplexMatrix(rowSize, columnSize, (DoubleComplexNumber[][])elements);
  //  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix createGrid(final int rowSize, final int columnSize, final DoubleComplexNumber[][] elements) {
    return new DoubleComplexMatrix(rowSize, columnSize, elements);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix createGrid(final DoubleComplexNumber[] elements) {
    return new DoubleComplexMatrix(elements);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final DoubleComplexMatrix createGrid(final DoubleComplexNumber[] elements) {
  //    return new DoubleComplexMatrix(elements);
  //  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber createUnit() {
    return new DoubleComplexNumber(1, 0);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber createZero() {
    return new DoubleComplexNumber(0, 0);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final boolean compare(final String operator, final GridElement<?> opponent) {
//    if (!(opponent instanceof DoubleComplexNumber)) {
//      return false;
//    }
//    return compare(operator, (DoubleComplexNumber)opponent);
//  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber[] createArray(final int size) {
    return new DoubleComplexNumber[size];
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber[][] createArray(final int rowSize, final int columnSize) {
    return new DoubleComplexNumber[rowSize][columnSize];
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleComplexNumber[] createArray(final GridElement<?>[] elements) {
//    final int size = elements.length;
//
//    if (size != 0 && (elements[0] instanceof DoubleComplexNumber) == false) {
//      throw new IllegalArgumentException();
//    }
//
//    final DoubleComplexNumber[] array = new DoubleComplexNumber[size];
//    System.arraycopy(elements, 0, array, 0, size);
//    return array;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleComplexNumber[][] createArray(final GridElement<?>[][] elements) {
//    final int rowSize = elements.length;
//    final int columnSize = rowSize == 0 ? 0 : elements[0].length;
//
//    if (rowSize != 0 && columnSize != 0 && (elements[0][0] instanceof DoubleComplexNumber) == false) {
//      throw new IllegalArgumentException();
//    }
//
//    final DoubleComplexNumber[][] array = new DoubleComplexNumber[rowSize][columnSize];
//    for (int row = 0; row < rowSize; row++) {
//      System.arraycopy(elements[row], 0, array[row], 0, columnSize);
//    }
//    return array;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleComplexNumber transformFrom(final int value) {
//    return new DoubleComplexNumber(value, 0);
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleComplexNumber transformFrom(final double value) {
//    return new DoubleComplexNumber(value, 0);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public final boolean isTransformableFrom(final GridElement<?> value) {
//    if (super.isTransformableFrom(value)) {
//      return true;
//    }
//
//    if (value instanceof DoubleNumber) {
//      return true;
//    }
//
//    return false;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public final DoubleComplexNumber transformFrom(final GridElement<? extends GridElement<?>> value) {
//    if (super.isTransformableFrom(value)) {
//      return super.transformFrom(value);
//    }
//
//    if (value instanceof DoubleNumber) {
//      return new DoubleComplexNumber(((DoubleNumber)value).doubleValue(), 0);
//    }
//
//    throw new IllegalArgumentException(Messages.getString("DoubleComplexNumber.12")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getAddOperator() {
//    return DoubleComplexNumberAddOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getDivideOperator() {
//    return DoubleComplexNumberDivideOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getLeftDivideOperator() {
//    return DoubleComplexNumberLeftDivideOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getMultiplyOperator() {
//    return DoubleComplexNumberMultiplyOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getSubtractOperator() {
//    return DoubleComplexNumberSubtractOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getPowerOperator() {
//    return DoubleComplexNumberPowerOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getAtan2Operator() {
//    return DoubleComplexNumberAtan2Operator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final NumericalScalarEqual getEqualOperator() {
//    return DoubleComplexNumberEqual.getInstance();
//  }

  /**
   * {@inheritDoc}
   */
  public final boolean isComplex() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isReal() {
    return false;
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final DoubleComplexNumber toComplex() {
  //    return clone();
  //  }

  /**
   * 虚部単位を返します。
   * 
   * @return 虚部単位
   */
  public final DoubleComplexNumber createImaginaryUnit() {
    return new DoubleComplexNumber(0, 1);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber createPI() {
    return new DoubleComplexNumber(Math.PI, 0);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber createE() {
    return new DoubleComplexNumber(Math.E, 0);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber create(final int value) {
    return new DoubleComplexNumber(value, 0);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber create(final double value) {
    return new DoubleComplexNumber(value, 0);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber create(final DoubleNumber value) {
    return create(value.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber create(final DoubleNumber rePart, final DoubleNumber imPart) {
    return new DoubleComplexNumber(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber valueOf(final String numberString) {
    final Pattern pattern = Pattern.compile("\\(\\s*([^,\\s]+)\\s*,\\s*([^)\\s]+)\\s*\\)"); //$NON-NLS-1$
    final Matcher matcher = pattern.matcher(numberString);

    if (matcher.find() == false) {
      throw new NumberFormatException(numberString);
    }

    final double rePart = Double.parseDouble(matcher.group(1));
    final double imPart = Double.parseDouble(matcher.group(2));
    return new DoubleComplexNumber(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber getMachineEpsilon() {
    return new DoubleComplexNumber(DoubleNumber.EPS, 0);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber getInfinity() {
    return new DoubleComplexNumber(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber getNaN() {
    return new DoubleComplexNumber(Double.NaN, Double.NaN);
  }

  /**
   * {@inheritDoc}
   */
  //public final RandomGenerator<DoubleComplexNumber, DoubleComplexMatrix> createUniformRandomGenerator() {
  public final DoubleComplexUniformRandom createUniformRandomGenerator() {
    return new DoubleComplexUniformRandom();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DoubleComplexNumber remainder(final DoubleComplexNumber value3) {
    if (value3.isZero()) {
      return this.clone();
    }

    final DoubleNumber value2 = value3.getRealPart();

//    if (value2 instanceof DoubleNumber == false) {
//      return super.remainder(value3);
//    }

    final double realValue = this.realPart / value2.doubleValue();
    final double imagValue = this.imagPart / value2.doubleValue();

    double realResult;
    if (realValue < 0) {
      realResult = this.realPart + Math.floor(-realValue) * value2.doubleValue();
    } else {
      realResult = this.realPart - Math.floor(realValue) * value2.doubleValue();
    }

    double imagResult;
    if (imagValue < 0) {
      imagResult = this.imagPart + Math.floor(-imagValue) * value2.doubleValue();
    } else {
      imagResult = this.imagPart - Math.floor(imagValue) * value2.doubleValue();
    }

    return new DoubleComplexNumber(realResult, imagResult);
  }
}