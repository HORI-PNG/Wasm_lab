/*
 * $Id: DoubleComplexHessenberg.java,v 1.3 2008/02/03 12:43:17 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.DoubleMatrixUtil;


/**
 * 倍精度(double)型の複素行列のヘッセンベルグ分解を求めるクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.3 $
 */
public final class DoubleComplexHessenbergDecomposer {

  /**
   * 倍精度(double)の複素行列のヘッセンベルグ分解を求め、対応するユニタリ行列とともに返します。
   * 
   * <p>複素行列をA、ユニタリ行列をQ、上ヘッセンベルグ行列をHとするとき、これらの行列の間には、
   * 
   * <blockquote> A = Q * H * Q<sup>#</sup> </blockquote>
   * 
   * <blockquote> Q<sup>#</sup> * Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @return ヘッセンベルグ分解の結果
   */
  public HessenbergDecompositionComplexRealElements decompose(final double[][] aRe, final double[][] aIm) {
    final int size = aRe.length;

    final double[][] a2Re = DoubleMatrixUtil.clone(aRe);
    final double[][] a2Im = DoubleMatrixUtil.clone(aIm);

    final double[][] zRe = DoubleMatrixUtil.createUnit(size, size);
    final double[][] zIm = new double[size][size];

    final double[] ortRe = new double[size];
    final double[] ortIm = new double[size];

    DoubleComplexEigenSolverUtil.corth(a2Re, a2Im, 1, size, ortRe, ortIm);

    final double[][] hRe = DoubleMatrixUtil.clone(a2Re);
    final double[][] hIm = DoubleMatrixUtil.clone(a2Im);

    for (int i = 1; i <= size; i++) {
      for (int j = 1; j < i - 1; j++) {
        hRe[i - 1][j - 1] = 0;
        hIm[i - 1][j - 1] = 0;
      }
    }

    DoubleComplexEigenSolverUtil.cortb(a2Re, a2Im, 1, size, ortRe, ortIm, zRe, zIm, size);

    final double[][] qRe = DoubleMatrixUtil.clone(zRe);
    final double[][] qIm = DoubleMatrixUtil.clone(zIm);

    return new HessenbergDecompositionComplexRealElements(qRe, qIm, hRe, hIm);
  }

}