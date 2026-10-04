/*
 * $Id: DoubleRealSchurDecomposition.java,v 1.4 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.DoubleMatrixUtil;


/**
 * 倍精度(double)型の実行列のシュア分解を行うクラスです。
 * 
 * @author koga
 * @version $Revision: 1.4 $
 */
public final class DoubleRealSchurDecomposer {

  /**
   * 倍精度(double)の実行列のSchur分解を返します。
   * 
   * <p>実行列をA、直交行列 U、Schur行列 T とすると、 これらの行列の間には、
   * 
   * <blockquote> A = U * T * U <sup>T </sup> </blockquote>
   * 
   * <blockquote> U <sup>T </sup>* U = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param a 対象となる行列
   * @return Schur分解の結果
   */
  public SchurDecompositionDoubleRealElements decompose(final double[][] a) {
    final int size = a.length;

    final double[][] t = DoubleMatrixUtil.clone(a);
    final double[][] u = new double[size][size];
    final double[] ort = new double[size];

    DoubleRealEigenSolverUtil.orthes(t, 1, size, ort);
    DoubleRealEigenSolverUtil.ortran(t, 1, size, ort, u);

    final double[] wRe = new double[size];
    final double[] wIm = new double[size];

    final int errorCode = DoubleRealEigenSolverUtil.hqr2(t, 1, size, wRe, wIm, u, true);

    if (errorCode != 0) {
      throw new RuntimeException(Messages.getString("DoubleRealSchurDecomposition.0")); //$NON-NLS-1$
    }

    for (int i = 1; i <= size; i++) {
      for (int j = 1; j < i - 1; j++) {
        t[i - 1][j - 1] = 0.0;
      }
    }

    return new SchurDecompositionDoubleRealElements(u, t);
  }

}