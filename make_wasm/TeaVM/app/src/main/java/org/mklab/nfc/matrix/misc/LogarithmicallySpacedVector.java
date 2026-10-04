/*
 * $Id: Logspace.java,v 1.11 2008/03/15 00:23:40 koga Exp $
 * 
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.matrix.misc;

import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 対数的に等間隔に分割されたデータ列を生成するクラスです。
 * 
 * <P> Logarithmically spaced vector
 * 
 * @author koga
 * @version $Revision: 1.11 $
 */
public final class LogarithmicallySpacedVector {
  /**
   * 新しく生成された<code>LogarithmicallySpacedVector</code>オブジェクトを初期化します。
   */
  private LogarithmicallySpacedVector() {
    // nothing to do
  }

  /**
   * <code>10^x1</code>と<code>10^x2</code>の間を対数的に <code>50</code>等分割した点をもつベクトルを返します。
   * 
   * <p> もし<code>x2</code>が<code>PI</code>なら <code>10^x2</code>と<code>PI</code>の間を対数的に 等分割した点を求めます。
   * 
   * @param x1 始点
   * @param x2 終点
   * @return 対数的に等間隔に分割されたデータ列 (logarithmically spaced array)
   */
  public static DoubleMatrix create(final double x1, final double x2) {
    return create(x1, x2, 50);
  }

  /**
   * <code>10^x1</code>と<code>10^x2</code>の間を対数的に <code>50</code>等分割した点をもつベクトルを返します。
   * 
   * <p> もし<code>x2</code>が<code>PI</code>なら <code>10^x2</code>と<code>PI</code>の間を対数的に 等分割した点を求めます。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param x1 始点
   * @param x2 終点
   * @return 対数的に等間隔に分割されたデータ列 (logarithmically spaced array)
   */
  public static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> M create(final S x1, final S x2) {
    return create(x1, x2, 50);
  }

  /**
   * <code>splitSize</code>分割した点を返します。
   * 
   * @param x1 始点
   * @param x2 終点
   * @param splitSize 分割数
   * @return 対数的に等間隔に分割されたデータ列 (logarithmically spaced array)
   */
  public static DoubleMatrix create(final double x1, final double x2, final int splitSize) {
    double min = x1;
    double max = x2;

    if (max == Math.PI) {
      max = log10(Math.PI);
    }

    if (0 < min && 0 < max && (Math.abs(min) <= 0.1 || 10.0 <= Math.abs(max))) {
      min = log10(min);
      max = log10(max);
    }

    final double nn = Math.abs((max - min) / (splitSize - 1));
    final double[] ans = new double[splitSize];
    for (int i = 0; i < splitSize; i++) {
      ans[i] = Math.pow(10, min + nn * i);
    }
    return new DoubleMatrix(ans);
  }

  /**
   * <code>splitSize</code>分割した点を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param x1 始点
   * @param x2 終点
   * @param splitSize 分割数
   * @return 対数的に等間隔に分割されたデータ列 (logarithmically spaced array)
   */
  public static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> M create(final S x1, final S x2, final int splitSize) {
    S min = x1;
    S max = x2;

    final S pi = x1.createPI();

    if (max.equals(pi)) {
      max = pi.log10();
    }

    final S c01 = x1.createUnit().divide(10);
    final S c10 = x1.createUnit().multiply(10);

    if (min.isGreaterThan(0) && max.isGreaterThan(0) && (min.abs().isLessThanOrEquals(c01) || max.abs().isGreaterThanOrEquals(c10))) {
      min = min.log10();
      max = max.log10();
    }

    final S nn = (max.subtract(min)).abs().divide(splitSize - 1);
    final S[] ans = x1.createArray(splitSize);
    for (int i = 0; i < splitSize; i++) {
      ans[i] = c10.power(min.add(nn.multiply(i)));
    }
    return x1.createGrid(ans);
  }

  /**
   * 常用対数の値を返します。
   * 
   * @param value 引数
   * @return 常用対数の値
   */
  private static double log10(final double value) {
    return Math.log(value) / Math.log(10);
  }
}
