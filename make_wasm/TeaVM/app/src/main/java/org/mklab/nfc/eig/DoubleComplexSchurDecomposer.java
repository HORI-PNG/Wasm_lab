/*
 * $Id: DoubleComplexSchurDecomposition.java,v 1.4 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.DoubleMatrixUtil;


/**
 * 倍精度(double)型の複素行列の複素シュア分解を行うクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.4 $
 */
public final class DoubleComplexSchurDecomposer {

  /**
   * 倍精度(double)の複素行列のSchur型を求めます。
   * 
   * <p>複素行列をA、ユニタリ行列 U、Schur行列 T とすると、 これらの行列の間には、
   * 
   * <blockquote> A = U * T * U<sup>#</sup> </blockquote>
   * 
   * <blockquote> U<sup>#</sup> * U = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @return Schur分解の結果
   */
  public SchurDecompositionDoubleComplexElements decompose(final double[][] aRe, final double[][] aIm) {
    final int size = aRe.length;

    final double[][] tRe = DoubleMatrixUtil.clone(aRe);
    final double[][] tIm = DoubleMatrixUtil.clone(aIm);

    final double[] fv2 = new double[size];
    final double[] fv3 = new double[size];

    DoubleComplexEigenSolverUtil.corth(tRe, tIm, 1, size, fv2, fv3);

    final double[] wRe = new double[size];
    final double[] wIm = new double[size];
    final double[][] uRe = new double[size][size];
    final double[][] uIm = new double[size][size];

    final int ierr = DoubleComplexEigenSolverUtil.comqr2(tRe, tIm, 1, size, fv2, fv3, wRe, wIm, uRe, uIm, true);

    if (ierr != 0) {
      throw new RuntimeException(Messages.getString("DoubleComplexSchurDecomposition.0")); //$NON-NLS-1$
    }

    for (int i = 1; i <= size; i++) {
      for (int j = 1; j < i; j++) {
        tRe[i - 1][j - 1] = 0.0;
        tIm[i - 1][j - 1] = 0.0;
      }
    }

    return new SchurDecompositionDoubleComplexElements(uRe, uIm, tRe, tIm);
  }
}