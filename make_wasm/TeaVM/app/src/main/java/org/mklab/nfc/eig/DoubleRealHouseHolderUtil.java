/*
 * $Id: DoubleRealHouseHolder.java,v 1.3 2008/02/03 12:43:17 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.DoubleMatrixUtil;


/**
 * 倍精度(double)型の実行列のハウスホルダー行列に関するクラスです。
 * 
 * @author koga
 * @version $Revision: 1.3 $
 */
public final class DoubleRealHouseHolderUtil {
  /**
   * 新しく生成された<code>DoubleRealHouseHolder</code>オブジェクトを初期化します。
   */
  private DoubleRealHouseHolderUtil() {
    // nothing to do
  }

  /**
   * 倍精度(double)の実行列のハウスホルダーベクトルを求めます。
   * 
   * @param x 元となる行列
   * @return ハウスホルダーベクトル
   */
  public static double[][] houseHolderVector(final double[][] x) {
    final double mu = DoubleMatrixUtil.frobNorm(x);
    if (mu != 0.0) {
      double beta = x[0][0];

      if (beta > 0.0) {
        beta += mu;
      } else {
        beta -= mu;
      }

      DoubleMatrixUtil.multiplySelf(x, 1.0 / beta);
    }

    x[0][0] = 1.0;
    return x;
  }

  /**
   * ハルスホルダー行列を左から乗じます。
   * 
   * <blockquote> A = P * A </blockquote>
   * 
   * <blockquote> P = I - 2v*v<sup>T</sup>/(v<sup>T</sup>*v) </blockquote>
   * 
   * @param a 対象となる行列
   * @param v ハウスホルダー行列
   * @return 掛けた結果
   */
  public static double[][] multiplyHouseHolderFromLeft(final double[][] a, final double[][] v) {
    final double dd = DoubleMatrixUtil.frobNorm(v);
    final double beta = -2.0 / (dd * dd);

    final double[][] vt = DoubleMatrixUtil.transpose(v);
    final double[][] vtA = DoubleMatrixUtil.multiply(vt, a);

    DoubleMatrixUtil.multiplySelf(vtA, beta);
    final double[][] wt = vtA;

    DoubleMatrixUtil.addSelf(a, DoubleMatrixUtil.multiply(v, wt));

    return a;
  }

  /**
   * ハウスホルダー行列を右から乗じます。
   * 
   * <blockquote> A = A * P </blockquote>
   * 
   * <blockquote> P = I - 2v*v<sup>T</sup>/(v<sup>T</sup>*v) </blockquote>
   * 
   * @param a 対象となる行列
   * @param v ハウスホルダー行列
   * @return 掛けた結果
   */
  public static double[][] multiplyHouseHolderFromRight(final double[][] a, final double[][] v) {
    final double dd = DoubleMatrixUtil.frobNorm(v);
    final double beta = -2.0 / (dd * dd);

    final double[][] vt = DoubleMatrixUtil.transpose(v);
    final double[][] aV = DoubleMatrixUtil.multiply(a, v);

    DoubleMatrixUtil.multiplySelf(aV, beta);
    final double[][] w = aV;

    // a = a + w*vt
    DoubleMatrixUtil.addSelf(a, DoubleMatrixUtil.multiply(w, vt)); 
    return a;
  }
}