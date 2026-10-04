/*
 * $Id: DoubleRealHessenberg.java,v 1.3 2008/02/03 12:43:17 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.DoubleMatrixUtil;


/**
 * 倍精度(double)型の実行列のヘッセンベルグ分解を求めるクラスです。
 * 
 * @author koga
 * @version $Revision: 1.3 $
 */
public final class DoubleRealHessenbergDecomposer {

  /**
   * 倍精度(double)の実行列のヘッセンベルグ分解を求め、対応する直交行列とともに返します。
   * 
   * <p>実行列をA、直交行列をQ、上ヘッセンベルグ行列をHとするとき、これらの行列の間には、
   * 
   * <blockquote> A = Q * H * Q<sup>T </sup> </blockquote>
   * 
   * <blockquote> Q <sup>T </sup>* Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param a 対象となる行列
   * @return ヘッセンベルグ分解の結果
   */
  public HessenbergDecompositionDoubleRealElements decompose(final double[][] a) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final double[][] a2 = DoubleMatrixUtil.clone(a);
    final double[] ort = new double[rowSize];

    DoubleRealEigenSolverUtil.orthes(a2, 1, rowSize, ort);

    final double[][] h = new double[rowSize][columnSize];
    for (int i = 1; i <= rowSize; i++) {
      for (int j = 1; j <= rowSize; j++) {
        if (i <= j + 1) {
          h[i - 1][j - 1] = a2[i - 1][j - 1];
        } else {
          h[i - 1][j - 1] = 0.0;
        }
      }
    }

    final double[][] q = new double[rowSize][columnSize];

    DoubleRealEigenSolverUtil.ortran(a2, 1, rowSize, ort, q);

    return new HessenbergDecompositionDoubleRealElements(q, h);
  }
}