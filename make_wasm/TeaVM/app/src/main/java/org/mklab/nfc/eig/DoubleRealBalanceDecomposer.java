/*
 * $Id: DoubleRealBalance.java,v 1.3 2008/02/03 12:43:17 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.DoubleMatrixUtil;


/**
 * 倍精度(double)型の実行列のバランス化分解(A=D*B*D^(-1), B=D\A*D)を行うための クラスです。
 * 
 * <p>実行列をA、バランス化された行列をB、スケーリング行列(対角行列)をDとすると、これらの行列の間には
 * 
 * <blockquote> A = D * B * D <sup>-1</sup> </blockquote>
 * 
 * <blockquote> B = D <sup>-1</sup> A * D </blockquote>
 * 
 * の関係が成り立ちます。
 * 
 * @author matsuki
 * @version $Revision: 1.3 $
 */
public final class DoubleRealBalanceDecomposer {

  /** 基数。 */
  private static final double RADIX = 2;
  /** 基数の2乗。 */
  private static final double B2 = RADIX * RADIX;

  /**
   * 倍精度(double)の実行列のバランス化を行い、対角成分が 2 のべき乗である対角行列 D と、バランス化された行列 B を返します。
   * 
   * <p>A、B、Dには、
   * 
   * <blockquote> B = D \ A * D </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param a 対象となる行列
   * @return バランス化分解の結果
   */
  public BalancedDecompositionDoubleRealElements decompose(final double[][] a) {
    final int size = a.length;

    final double[][] b = DoubleMatrixUtil.clone(a);
    final double[] scale = new double[size];

    decompose(b, scale);

    final double[][] d = DoubleMatrixUtil.vectorToDiagonal(scale);

    return new BalancedDecompositionDoubleRealElements(d, b);
  }

  /**
   * 実行列の成分をバランス化します。
   * 
   * @param a 対象となる行列
   * @param scale スケーリング情報
   */
  private void decompose(final double[][] a, final double[] scale) {
    final int size = a.length;

    /*
     * Now balance the submatrix in rows 1 to n
     */
    for (int i = 1; i <= size; i++) {
      scale[i - 1] = 1.0;
    }

    /*
     * Iterative loop for norm reduction
     */

    // L190:
    boolean noconv;
    do {
      noconv = false;
      for (int i = 1; i <= size; i++) {
        double c = 0.0;
        double r = 0.0;
        for (int j = 1; j <= size; j++) {
          if (j != i) {
            c += Math.abs(a[j - 1][i - 1]);
            r += Math.abs(a[i - 1][j - 1]);
          }
        }
        /* if (fabs(c*r) > EPS) { */
        if (c * r != 0.0) {
          double g1 = r / DoubleRealBalanceDecomposer.RADIX;
          double f = 1.0;
          double s = c + r;
          while (c < g1) {
            f *= DoubleRealBalanceDecomposer.RADIX;
            c *= DoubleRealBalanceDecomposer.B2;
          }
          
          double g2 = r * DoubleRealBalanceDecomposer.RADIX;
          while (c >= g2) {
            f /= DoubleRealBalanceDecomposer.RADIX;
            c /= DoubleRealBalanceDecomposer.B2;
          }
          /*
           * Now balanc
           */
          if (((c + r) / f) < (0.95 * s)) {
            double g3 = 1.0 / f;
            scale[i - 1] *= f;
            noconv = true;
            for (int j = 1; j <= size; j++) {
              a[i - 1][j - 1] *= g3;
              a[j - 1][i - 1] *= f;
            }
          }
        }
      }
    } while (noconv);
  }
}