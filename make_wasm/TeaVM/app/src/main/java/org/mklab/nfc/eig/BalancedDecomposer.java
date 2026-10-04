/*
 * $Id: BalancedMatrix.java,v 1.4 2008/03/15 00:23:43 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.GridUtil;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 行列のバランス化分解(A=D*B*D^(-1), B=D\A*D)を行うための クラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.4 $
 * 
 * @param <S> スカラーの型
 * @param <M> 行列の型 
 */
public final class BalancedDecomposer<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {

  /**
   * 行列のバランス化を行い, 対角成分が 2 のべき乗である対角行列 D と、バランス化された行列 B を返します。
   * 
   * <p>A、B、Dには、
   * 
   * <blockquote> B = D \ A * D </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * 
   * 
   * @param a バランス化したい行列
   * @return バランス化の結果
   */
  public BalancedDecompositionElements<S,M> decompose(final S[][] a) {
    final int size = a.length;

    final S[][] b = a.clone();
    final S[] scale = a[0][0].createArray(size);

    decompose(b, scale);

    final S[][] d = GridUtil.vectorToDiagonal(scale);

    return new BalancedDecompositionElements<>(d, b);
  }

  /**
   * 行列の成分をバランス化します。
   * 
   * @param a バランス化する行列
   * @param scale スケーリング情報
   */
  private void decompose(final S[][] a, final S[] scale) {
    final S scalar = a[0][0].abs();
    /** 基数 */
    final S two = scalar.createUnit().multiply(2);
    final S radix = two;
    /** 基数の2乗 */
    final S b2 = radix.multiply(radix);

    int size = a.length;

    /*
     * Now balance the submatrix in rows 1 to n
     */
    for (int i = 1; i <= size; i++) {
      scale[i - 1] = a[0][0].createUnit();
    }

    /*
     * Iterative loop for norm reduction
     */

    // L190:
    boolean noconv;
    do {
      noconv = false;
      for (int i = 1; i <= size; i++) {
        S c = scalar.createZero();
        S r = scalar.createZero();
        for (int j = 1; j <= size; j++) {
          if (j != i) {
            c = c.add(a[j - 1][i - 1].abs());
            r = r.add(a[i - 1][j - 1].abs());
          }
        }
        /* if (fabs(c*r) > EPS) { */
        if (c.multiply(r).isZero() == false) {
          final S g1 = r.divide(radix);
          S f = scalar.createUnit();
          S s = c.add(r);
          while (c.isLessThan(g1)) {
            f = f.multiply(radix);
            c =c.multiply(b2);
          }
          
          
          final S g2 = r.multiply(radix);
          while (c.isGreaterThanOrEquals(g2)) {
            f = f.divide(radix);
            c =c.divide(b2);
          }
          /*
           * Now balanc
           */
          if ((c.add(r).divide(f)).isLessThan(s.multiply(c.createUnit().multiply(95).divide(100)))) {
            final S g3 = f.inverse();
            scale[i - 1] = scale[i - 1].multiply(f);
            noconv = true;
            for (int j = 1; j <= size; j++) {
              a[i - 1][j - 1] = a[i - 1][j - 1].multiply(g3);
              a[j - 1][i - 1] = a[j - 1][i - 1].multiply(f);
            }
          }
        }
      }
    } while (noconv);
  }
}