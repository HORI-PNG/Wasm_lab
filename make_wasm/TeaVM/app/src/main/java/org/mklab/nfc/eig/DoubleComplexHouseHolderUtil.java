/*
 * $Id: DoubleComplexHouseHolder.java,v 1.4 2008/03/24 11:53:42 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.DoubleComplexMatrixUtil;
import org.mklab.nfc.matrix.DoubleMatrixUtil;


/**
 * 倍精度(double)型の複素行列のハウスホルダー変換を行うためのクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.4 $
 */
public final class DoubleComplexHouseHolderUtil {
  /**
   * 新しく生成された<code>DoubleComplexHouseHolder</code>オブジェクトを初期化します。
   */
  private DoubleComplexHouseHolderUtil() {
    // nothing to do
  }

  /**
   * 倍精度(double)のハウスホルダー行列を作るためのベクトルを返します。
   * 
   * @param xRe 複素ベクトルの実部
   * @param xIm 複素ベクトルの虚部
   * @param number 非零にする成分の番号(1から始まる)
   * @return ハウスホルダー行列を作るためのベクトル
   */
  public static double[][][] houseHolderVector(final double[][] xRe, final double[][] xIm, final int number) {
    double d = DoubleComplexMatrixUtil.frobNorm(xRe, xIm);
    d *= d;

    double xnRe = xRe[number - 1][0];
    double xnIm = xIm[number - 1][0];
    double dd = xnRe * xnRe + xnIm * xnIm;

    double a, b;

    if (dd != 0.0) {
      final double b2 = (d * xnIm * xnIm) / dd;
      final double a2 = d - b2;
      a = Math.sqrt(a2);
      b = Math.sqrt(b2);
    } else {
      a = Math.sqrt(d);
      b = 0.0;
    }

    if (dd != 0.0) {
      if (xnRe > 0.0) {
        if (xnRe * xnIm > 0.0) {
          xnIm += b;
        } else {
          xnIm -= b;
        }
        xnRe += a;
      } else {
        if (xnRe * xnIm > 0.0) {
          xnIm -= b;
        } else {
          xnIm += b;
        }
        xnRe -= a;
      }

      dd = xnRe * xnRe + xnIm * xnIm;
      xnRe = xnRe / dd;
      xnIm = -xnIm / dd;
      DoubleComplexMatrixUtil.multiplySelf(xRe, xIm, xnRe, xnIm);
    }

    xnRe = 1.0;
    xnIm = 0.0;
    xRe[number - 1][0] = xnRe;
    xIm[number - 1][0] = xnIm;
    return new double[][][] {xRe, xIm};
  }

  /**
   * ハウスホルダー行列を左から掛けます。
   * 
   * <blockquote> A = P * A </blockquote>
   * 
   * <blockquote> P = I - 2v*v<sup>T</sup>/(v<sup>T</sup>*v) </blockquote>
   * 
   * @param aRe 変換される行列の実部
   * @param aIm 変換される行列の虚部
   * @param vRe ハウスホルダー行列を作るためのベクトルの実部
   * @param vIm ハウスホルダー行列を作るためのベクトルの虚部
   * @return ハウスホルダー行列を左から掛けた結果
   */
  public static double[][][] multiplyHouseHolderFromLeft(final double[][] aRe, final double[][] aIm, final double[][] vRe, final double[][] vIm) {
    final double dd = DoubleComplexMatrixUtil.frobNorm(vRe, vIm);
    final double beta = -2.0 / (dd * dd);

    final double[][] vtRe = DoubleMatrixUtil.transpose(vRe);
    final double[][] vtIm = DoubleMatrixUtil.unaryMinus(DoubleMatrixUtil.transpose(vIm));

    final double[][][] vtA = DoubleComplexMatrixUtil.multiply(vtRe, vtIm, aRe, aIm);
    final double[][] vtARe = vtA[0];
    final double[][] vtAIm = vtA[1];

    DoubleMatrixUtil.multiplySelf(vtARe, beta);
    DoubleMatrixUtil.multiplySelf(vtAIm, beta);
    final double[][] wtRe = vtARe;
    final double[][] wtIm = vtAIm;

    final double[][][] vWt = DoubleComplexMatrixUtil.multiply(vRe, vIm, wtRe, wtIm);
    final double[][] vWtRe = vWt[0];
    final double[][] vWtIm = vWt[1];

    final double[][] a2Re = DoubleMatrixUtil.add(aRe, vWtRe);
    final double[][] a2Im = DoubleMatrixUtil.add(aIm, vWtIm);
    return new double[][][] {a2Re, a2Im};
  }

  /**
   * ハウスホルダー行列を右から掛けます。
   * 
   * <blockquote> A = A * P </blockquote>
   * 
   * <blockquote> P = I - 2v*v<sup>T</sup>/(v<sup>T</sup>*v) </blockquote>
   * 
   * @param aRe 変換される行列の実部
   * @param aIm 変換される行列の虚部
   * @param vRe ハウスホルダー行列を作るためのベクトルの実部
   * @param vIm ハウスホルダー行列を作るためのベクトルの虚部
   * @return ハウスホルダー行列を左から掛けた結果
   */
  public static double[][][] multiplyHouseHolderFromRight(final double[][] aRe, final double[][] aIm, final double[][] vRe, final double[][] vIm) {
    final double dd = DoubleComplexMatrixUtil.frobNorm(vRe, vIm);
    final double beta = -2.0 / (dd * dd);

    final double[][] vtRe = DoubleMatrixUtil.transpose(vRe);
    final double[][] vtIm = DoubleMatrixUtil.unaryMinus(DoubleMatrixUtil.transpose(vIm));

    final double[][][] aV = DoubleComplexMatrixUtil.multiply(aRe, aIm, vRe, vIm);
    final double[][] aVRe = aV[0];
    final double[][] aVIm = aV[1];

    DoubleMatrixUtil.multiplySelf(aVRe, beta);
    DoubleMatrixUtil.multiplySelf(aVIm, beta);
    final double[][] wRe = aVRe;
    final double[][] wIm = aVIm;

    final double[][][] wVt = DoubleComplexMatrixUtil.multiply(wRe, wIm, vtRe, vtIm);
    final double[][] wVtRe = wVt[0];
    final double[][] wVtIm = wVt[1];

    final double[][] a2Re = DoubleMatrixUtil.add(aRe, wVtRe);
    final double[][] a2Im = DoubleMatrixUtil.add(aIm, wVtIm);
    return new double[][][] {a2Re, a2Im};
  }
}