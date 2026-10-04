/*
 * $Id: DoubleComplexBalance.java,v 1.4 2008/03/18 00:15:02 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.DoubleMatrixUtil;


/**
 * 倍精度(double)型の複素行列のバランス化分解(A=D*B*D^(-1), B=D\A*D)を行うためのクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.4 $, 2004/06/22
 */
public final class DoubleComplexBalanceDecomposer {

  /** 基数。 */
  private static final double RADIX = 2;
  /** 基数の2乗。 */
  private static final double B2 = RADIX * RADIX;

  /**
   * 倍精度(double)の複素行列のバランス化を行い、対角成分が 2 のべき乗である対角行列 D と、バランス化された行列 B を返します。
   * 
   * <p>A、B、Dには
   * 
   * <blockquote> B = D \ A * D </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @return バランス化の結果
   */
  public BalancedDecompositionDoubleComplexElements decompose(final double[][] aRe, final double[][] aIm) {
    final int size = aRe.length;

    final double[][] bRe = DoubleMatrixUtil.clone(aRe);
    final double[][] bIm = DoubleMatrixUtil.clone(aIm);
    final double[] scale = new double[size];

    decompose(bRe, bIm, scale);

    final double[][] d = DoubleMatrixUtil.vectorToDiagonal(scale);

    return new BalancedDecompositionDoubleComplexElements(d, bRe, bIm);
  }

  /**
   * 複素行列の成分をバランス化します。
   * 
   * @param aRe 実部行列
   * @param aIm 虚部行列
   * @param scale スケーリング情報
   */
  private void decompose(final double[][] aRe, final double[][] aIm, final double[] scale) {
    final int size = aRe.length;

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
          if (j == i) {
            continue;
          }

          c += Math.abs(aRe[j - 1][i - 1]) + Math.abs(aIm[j - 1][i - 1]);
          r += Math.abs(aRe[i - 1][j - 1]) + Math.abs(aIm[i - 1][j - 1]);
        }

        if (c == 0.0 || r == 0.0) {
          continue;
        }

        double g1 = r / RADIX;
        double f = 1.0;
        double s = c + r;
        while (c < g1) {
          f *= RADIX;
          c *= B2;
        }
        
        double g2 = r * RADIX;
        while (c >= g2) {
          f /= RADIX;
          c /= B2;
        }
        /*
         * Now balanc
         */
        if ((c + r) / f >= (0.95 * s)) {
          continue;
        }

        double g3 = 1.0 / f;
        scale[i - 1] = scale[i - 1] * f;
        noconv = true;

        for (int j = 1; j <= size; j++) {
          // ComplexValueMulSelf2(AC(i,j), g); //////// AC(i,j) = AC(i,j) * g
          aRe[i - 1][j - 1] *= g3;
          aIm[i - 1][j - 1] *= g3;
          // ComplexValueMulSelf2(AC(j,i), f);
          aRe[j - 1][i - 1] *= f;
          aIm[j - 1][i - 1] *= f;
        }
      }
    } while (noconv); // if(noconv)goto L190;
  }

}