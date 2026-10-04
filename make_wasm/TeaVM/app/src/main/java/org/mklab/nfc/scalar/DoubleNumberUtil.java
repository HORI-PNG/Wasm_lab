/*
 * $Id: DoubleNumberUtil.java,v 1.6 2008/07/16 04:58:02 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.scalar;

/**
 * 倍精度(double)型の実数のユーティリティクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.6 $
 */
public final class DoubleNumberUtil {
  /**
   * 新しく生成された<code>DoubleNumberUtil</code>オブジェクトを初期化します。
   */
  private DoubleNumberUtil() {
    // nothing to do
  }

  /** 機種精度(Machine Epsilon)。 */
  public static final double EPS = createMachineEpsilon();

  /**
   * 0方向の整数へ丸めた値を返します。
   * 
   * @param value 丸める対象の実数
   * @return 丸めた結果
   */
  public static double fix(final double value) {
    if (value < 0) {
      return Math.ceil(value);
    }
    return Math.floor(value);
  }

  /**
   * 絶対値が許容誤差より小さければ、ゼロへ丸めた値を返します。
   * 
   * @param value 丸める対象の実数
   * @param tolerance 許容誤差
   * @return 丸めた結果
   */
  public static double roundToZero(final double value, final double tolerance) {
    if (Math.abs(value) <= tolerance) {
      return 0;
    }
    return value;
  }

  /**
   * 逆双曲線余弦関数を計算します。
   * 
   * @param value 値を求める対象
   * @return 逆双曲線余弦関数の値
   */
  public static double acosh(final double value) {
    return Math.log(value + Math.sqrt(value * value - 1));
  }
  
  /**
   * 逆双曲線正弦関数を計算します。
   * 
   * @param value 値を求める対象
   * @return 逆双曲線正弦関数の値
   */
  public static double asinh(final double value) {
    return Math.log(value + Math.sqrt(value * value + 1));
  }

  /**
   * 逆双曲線正接関数を計算します。
   * 
   * @param value 値を求める対象
   * @return 逆双曲線正接関数の値
   */
  public static double atanh(final double value) {
    return Math.log((1 + value) / (1 - value)) / 2;
  }

  /**
   * 有限性を判定します。
   * 
   * @param value 真偽を調べる対象
   * @return 有限(無限でなく、かつNaNでない)ならtrue
   */
  public static boolean isFinite(final double value) {
    return (!Double.isInfinite(value)) && (!Double.isNaN(value));
  }

  /**
   * 符合(-1,0,1)を求めます。
   * 
   * @param value 符号を求める対象
   * @return 符合(-1,0,1)
   */
  public static double signum(final double value) {
    if (value > 0) {
      return 1;
    } else if (value == 0) {
      return 0;
    } else {
      return -1;
    }
  }

  /**
   * 階乗を計算します。
   * 
   * @param value 値を求める対象
   * @return 階乗
   */
  public static double fact(final int value) {
    if (value < 0) {
      throw new IllegalArgumentException(Messages.getString("DoubleNumberUtil.18")); //$NON-NLS-1$
    }

    if (value == 0 || value == 1) {
      return 1;
    }
    int ans = 1;
    for (int i = value; 1 < i; i = i - 1) {
      ans = ans * i;
    }

    return ans;
  }

  /**
   * 階乗を計算します。
   * 
   * @param value 値を求める対象
   * @return 階乗
   */
  public static double fact(final double value) {
    if (value < 0) {
      throw new IllegalArgumentException(Messages.getString("DoubleNumberUtil.19")); //$NON-NLS-1$
    }

    if (value == 0) {
      return 1;
    }

    if (value < 1) {
      return value;
    }

    double ans = 1;
    for (double d = value; 1 < d; d = d - 1) {
      ans = ans * d;
    }

    return ans;
  }

  /**
   * 剰余関数を計算します。
   * 
   * @param value1 割られる数
   * @param value2 割る数
   * @return 剰余(value1の符号と同じ)
   */
  public static double remainder(final double value1, final double value2) {
    if (value2 == 0) {
      return value1;
    }

    return value1 - DoubleNumberUtil.fix(value1 / value2) * value2;
  }

  /**
   * 符合付剰余関数を計算します。
   * 
   * @param value1 割られる数
   * @param value2 割る数
   * @return 符合付剰余(value2の符号と同じ)
   */
  public static double modulus(final double value1, final double value2) {
    if (value2 == 0) {
      return value1;
    }
    return value1 - Math.floor(value1 / value2) * value2;
  }

  /**
   * 機種精度(Machine Epsilon)を返します。
   * 
   * @return 機種精度(Machine Epsilon)
   */
  private static double createMachineEpsilon() {
    final double unit = 1;
    double value = unit / 2;

    while ((unit + value) != unit) {
      value = value / 2;
    }

    return value * 2;
  }

  /**
   * double型の2次元配列を返します。
   * 
   * @param elements 元のデータ
   * @return double型の2次元配列
   */
  public static double[][] createDoubleArray(final DoubleNumber[][] elements) {
    final int rowSize = elements.length;
    final int columnSize = rowSize == 0 ? 0 : elements[0].length;

    final double[][] matrix = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j] = elements[i][j].doubleValue();
      }
    }

    return matrix;
  }

  /**
   * double型の1次元配列を返します。
   * 
   * @param elements 元のデータ
   * @return double型の1次元配列
   */
  public static double[] createArray(final DoubleNumber[] elements) {
    final int rowSize = elements.length;

    final double[] matrix = new double[rowSize];

    for (int i = 0; i < rowSize; i++) {
      matrix[i] = elements[i].doubleValue();
    }

    return matrix;
  }

  /**
   * DoubleNumber型の2次元配列を返します。
   * 
   * @param elements 元のデータ
   * @return DoubleNumber型の2次元配列
   */
  public static DoubleNumber[][] createArray(final double[][] elements) {
    final int rowSize = elements.length;
    final int columnSize = rowSize == 0 ? 0 : elements[0].length;

    final DoubleNumber[][] matrix = new DoubleNumber[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j] = new DoubleNumber(elements[i][j]);
      }
    }

    return matrix;
  }

  /**
   * DoubleNumber型の2次元配列を返します。
   * 
   * @param rowSize 行数
   * @param columnSize 列数
   * @return DoubleNumber型の2次元配列
   */
  public static DoubleNumber[][] createArray(final int rowSize, final int columnSize) {
    final DoubleNumber[][] matrix = new DoubleNumber[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j] = new DoubleNumber(0);
      }
    }

    return matrix;
  }

  /**
   * DoubleNumber型の2次元配列を返します。
   * 
   * @param elements 元のデータ
   * @return DoubleNumber型の2次元配列
   */
  public static DoubleNumber[][] createArray(final int[][] elements) {
    final int rowSize = elements.length;
    final int columnSize = rowSize == 0 ? 0 : elements[0].length;

    final DoubleNumber[][] matrix = new DoubleNumber[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j] = new DoubleNumber(elements[i][j]);
      }
    }

    return matrix;
  }
  
  /**
   * DoubleNumber型の1次元配列を返します。
   * 
   * @param size サイズ 
   * @return DoubleNumber型の1次元配列
   */
  public static DoubleNumber[] createArray(final int size) {
    final DoubleNumber[] matrix = new DoubleNumber[size];

    for (int i = 0; i < size; i++) {
      matrix[i] = new DoubleNumber(0);
    }

    return matrix;
  }

  /**
   * DoubleNumber型の1次元配列を返します。
   * 
   * @param elements 元のデータ
   * @return DoubleNumber型の1次元配列
   */
  public static DoubleNumber[] createArray(final double[] elements) {
    final int rowSize = elements.length;
  
    final DoubleNumber[] matrix = new DoubleNumber[rowSize];

    for (int i = 0; i < rowSize; i++) {
      matrix[i] = new DoubleNumber(elements[i]);
    }

    return matrix;
  }

  /**
   * DoubleNumber型の1次元配列を返します。
   * 
   * @param elements 元のデータ
   * @return DoubleNumber型の1次元配列
   */
  public static DoubleNumber[] createArray(final int[] elements) {
    final int rowSize = elements.length;
  
    final DoubleNumber[] matrix = new DoubleNumber[rowSize];

    for (int i = 0; i < rowSize; i++) {
      matrix[i] = new DoubleNumber(elements[i]);
    }

    return matrix;
  }
  
  /**
   * 2個の倍精度実数が等しいか判定します。
   * @param a 比較対象
   * @param b 比較対象
   * @return 2個の値が等しければtrue,そうでなければfalse
   */
  public static boolean equals(double a, double b) {
    if (DoubleNumberUtil.isFinite(a)) {
      return Double.doubleToLongBits(Math.abs(a - b)) == 0;
    }
    
    return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
  }
  
  /**
   * 2個の倍精度実数が等しいか判定します。
   * @param a 比較対象
   * @param b 比較対象
   * @param tolerance 許容誤差
   * @return 2個の値が等しければtrue,そうでなければfalse
   */
  public static boolean equals(double a, double b, double tolerance) {
    if (DoubleNumberUtil.isFinite(a) && DoubleNumberUtil.isFinite(b)) {
      return Math.abs(a - b) <= tolerance;
    }
    
    return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
  }

}
