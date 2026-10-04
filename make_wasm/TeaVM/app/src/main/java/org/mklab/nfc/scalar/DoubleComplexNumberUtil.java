/*
 * $Id: DoubleComplexNumberUtil.java,v 1.2 2008/02/02 05:53:01 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.scalar;

/**
 * 倍精度(double)型の複素数に関するユーティリティクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.2 $, 2004/06/23
 */
public final class DoubleComplexNumberUtil {
  /**
   * 新しく生成された<code>DoubleComplexNumberUtil</code>オブジェクトを初期化します。
   */
  private DoubleComplexNumberUtil() {
    // nothing to do
  }

  /**
   * 複素数の絶対値を返します。
   * 
   * @param realPart 複素数の実部
   * @param imagPart 複素数の虚部
   * @return 複素数の絶対値
   */
  public static double abs(final double realPart, final double imagPart) {
    return Math.sqrt(realPart * realPart + imagPart * imagPart);
  }

  /**
   * 複素数が零(実部の絶対値と虚部の絶対値が許容誤差以下)であるか判定します。
   * 
   * @param realPart 複素数の実部
   * @param imagPart 複素数の虚部
   * @param tolerance 許容誤差
   * @return 複数が零ならばtrue、そうでなければfalse
   */
  public static boolean isZero(final double realPart, final double imagPart, final double tolerance) {
    return (Math.abs(realPart) <= tolerance && Math.abs(imagPart) <= tolerance);
  }

  /**
   * 複素数の平方根を返します。
   * 
   * @param realPart 複素数の実部
   * @param imagPart 複素数の虚部
   * @return 複素数の平方根
   */
  public static double[] sqrt(final double realPart, final double imagPart) {
    final double w = Math.sqrt(realPart * realPart + imagPart * imagPart);

    final double[] ans = new double[2];
    ans[0] = Math.sqrt(Math.abs((w + realPart) * 0.5));
    ans[1] = Math.sqrt(Math.abs((w - realPart) * 0.5));

    if (imagPart < 0.0) {
      ans[1] = -ans[1];
    }

    return ans;
  }

  /**
   * 実数と複素数の積を返します。
   * 
   * @param realNumber 実数
   * @param complexNumber 複素数
   * @return 実数と複素数の積
   */
  public static DoubleComplexNumber multiply(final double realNumber, final DoubleComplexNumber complexNumber) {
    return new DoubleComplexNumber(realNumber * complexNumber.getRealPart().doubleValue(), realNumber * complexNumber.getImaginaryPart().doubleValue());
  }

  /**
   * 整数と複素数の積を返します。
   * 
   * @param intNumber 整数
   * @param complexNumber 複素数
   * @return 整数と複素数の積
   */
  public static DoubleComplexNumber multiply(final int intNumber, final DoubleComplexNumber complexNumber) {
    return new DoubleComplexNumber(intNumber * complexNumber.getRealPart().doubleValue(), intNumber * complexNumber.getImaginaryPart().doubleValue());
  }

  /**
   * 実数の複素数乗(a^b)を返します。
   * 
   * @param a 実数
   * @param b 複素数
   * @return 実数の複素数乗(a^b)
   */
  public static DoubleComplexNumber power(final double a, final DoubleComplexNumber b) {
    double br = b.getRealPart().doubleValue();
    double bi = b.getImaginaryPart().doubleValue();
    
    final double logr = Math.log(Math.abs(a));
    final double d = Math.pow(a, br)*(a >= 0 ? 1 : Math.pow(Math.E, -Math.PI*bi));
    final double th = bi * logr;
    return new DoubleComplexNumber(d * Math.cos(th), d * Math.sin(th));
  }
}