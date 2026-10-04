/*
 * $Id: DoubleRealGaussianElimination.java,v 1.4 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.leq;

import org.mklab.nfc.matrix.DoubleMatrixUtil;


/**
 * 倍精度(double)型の実行列の逆行列をガウスの消去法で求めるクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.4 $
 */
public class DoubleRealGaussianEliminationSolver {

  /**
   * 逆行列をガウスの消去法で求めます。
   * 
   * @param a 対象となる行列
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return 逆行列と行列式
   */
  public final Object[] inverse(final double[][] a, final double tolerance, final boolean stopIfSingular) {
    final double[][] a2 = DoubleMatrixUtil.clone(a);
    final int rowSize = a.length;

    if (rowSize == 1) {
      final double det = a2[0][0];
      a2[0][0] = 1 / det;
      return new Object[] {a2, Double.valueOf(det)};
    }

    final int[] work = new int[rowSize];
    double w1 = 1.0;
    for (int i = 0; i < rowSize; i++) {
      work[i] = i;
    }

    for (int k = 0; k < rowSize; k++) {
      final double[] matk = a2[k];

      int r = k; /* Added 1995.3.3 */
      double wmax = 0.0;
      for (int i = k; i < rowSize; i++) {
        double w = Math.abs(a2[i][k]);
        if (w > wmax) {
          r = i;
          wmax = w;
        }
      }
      double pivot = a2[r][k];
      final double api = Math.abs(pivot);

      if (api <= tolerance) {
        if (stopIfSingular) {
          throw new RuntimeException(Messages.getString("DoubleRealGaussianElimination.0")); //$NON-NLS-1$
        }

        System.err.println(Messages.getString("DoubleRealGaussianElimination.1")); //$NON-NLS-1$

        if (api < tolerance) {
          if (pivot >= 0) {
            pivot = tolerance;
          } else {
            pivot = -tolerance;
          }
        }
      }

      w1 *= pivot;

      if (r != k) {
        w1 = -w1;
        final int iw = work[k];
        work[k] = work[r];
        work[r] = iw;

        double[] matr = a2[r];
        for (int j = 0; j < rowSize; j++) {
          final double w = matk[j];
          matk[j] = matr[j];
          matr[j] = w;
        }
      }

      for (int i = 0; i < rowSize; i++) {
        matk[i] /= pivot;
      }

      for (int i = 0; i < rowSize; i++) {
        final double[] mati = a2[i];
        if (i != k) {
          final double w = mati[k];
          if (w != 0.0) {
            for (int j = 0; j < rowSize; j++) {
              if (j != k) {
                mati[j] -= w * matk[j];
              }
            }
            mati[k] = -w / pivot;
          }
        }
      }
      matk[k] = 1.0 / pivot;
    }

    for (int i = 0; i < rowSize; i++) {
      while (true) {
        final int k = work[i];

        if (k == i) {
          break;
        }

        final int iw = work[k];
        work[k] = work[i];
        work[i] = iw;

        for (int j = 0; j < rowSize; j++) {
          final double w = a2[j][i];
          a2[j][i] = a2[j][k];
          a2[j][k] = w;
        }
      }
    }

    final double det = w1;
    return new Object[] {a2, Double.valueOf(det)};
  }

}