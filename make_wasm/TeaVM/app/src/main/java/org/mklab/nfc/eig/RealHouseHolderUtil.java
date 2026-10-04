/*
 * $Id: RealHouseHolder.java,v 1.4 2008/03/15 00:23:43 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.BaseMatrixUtil;
import org.mklab.nfc.matrix.AbstractNumericalMatrixUtil;
import org.mklab.nfc.matrix.GridUtil;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 実行列のハウスホルダー行列に関するクラスです。
 * 
 * @author koga
 * @version $Revision: 1.4 $
 */
final class RealHouseHolderUtil {

  /**
   * 新しく生成された<code>RealHouseHolder</code>オブジェクトを初期化します。
   */
  private RealHouseHolderUtil() {
    // nothing to do
  }

  /**
   * 実行列のハウスホルダーベクトルを求めます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param x 対象となる実行列
   * @return ハウスホルダーベクトル
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] houseHolderVector(final S[][] x) {
    final S mu = AbstractNumericalMatrixUtil.frobNorm(x);

    if (mu.isZero() == false) {
      S beta = x[0][0];

      if (beta.isGreaterThan(x[0][0].createZero())) {
        beta = beta.add(mu);
      } else {
        beta = beta.subtract(mu);
      }

      final S[][] xx = BaseMatrixUtil.<S, M> multiply(x, beta.inverse());
      GridUtil.copy(xx, x);
    }

    x[0][0] = x[0][0].createUnit();
    return x;
  }

  /**
   * ハルスホルダー行列を左から乗じます。
   * 
   * <blockquote> A = P * A </blockquote>
   * 
   * <blockquote> P = I - 2v*v<sup>T</sup>/(v<sup>T</sup>*v) </blockquote>
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param a 対象となる行列
   * @param v ハウスホルダー行列
   * @return 掛けた結果
   */
  static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] multiplyHouseHolderFromLeft(final S[][] a, final S[][] v) {
    final S dd = AbstractNumericalMatrixUtil.frobNorm(v);
    final S beta = dd.createUnit().unaryMinus().multiply(2).divide(dd.multiply(dd));

    final S[][] vt = GridUtil.transpose(v);
    final S[][] vtA = BaseMatrixUtil.multiply(vt, a);

    final S[][] wt = BaseMatrixUtil.multiply(vtA, beta);

    final S[][] vWt = BaseMatrixUtil.multiply(v, wt);

    final S[][] ans = BaseMatrixUtil.add(a, vWt); /* a = a + v*wt */
    return ans;
  }

  /**
   * ハウスホルダー行列を右から乗じます。
   * 
   * <blockquote> A = A * P </blockquote>
   * 
   * <blockquote> P = I - 2v*v<sup>T</sup>/(v<sup>T</sup>*v) </blockquote>
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param a 対象となる行列
   * @param v ハウスホルダー行列
   * @return 掛けた結果
   */
  static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] multiplyHouseHolderFromRight(final S[][] a, final S[][] v) {
    final S dd = AbstractNumericalMatrixUtil.frobNorm(v);
    final S beta = dd.createUnit().unaryMinus().multiply(2).divide(dd.multiply(dd));

    final S[][] vt = GridUtil.transpose(v);
    final S[][] aV = BaseMatrixUtil.multiply(a, v);

    final S[][] w = BaseMatrixUtil.multiply(aV, beta);

    final S[][] wVt = BaseMatrixUtil.multiply(w, vt);

    final S[][] ans = BaseMatrixUtil.add(a, wVt); /* a = a + w*vt */
    return ans;
  }
}