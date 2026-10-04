/*
 * $Id: DoubleRealExponentialMatrix.java,v 1.3 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.elf;

import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.scalar.DoubleNumberUtil;


/**
 * 倍精度(double)型の実行列の指数関数行列を求めるためのクラスです。
 * 
 * @author koga
 * @version $Revision: 1.3 $
 */
public final class DoubleRealExponentialMatrix {
  /** 反復の最大回数 */
  private static int MAX_ITERATION = 100;
  
  /**
   * 反復の最大回数を返します。
   * @return 反復の最大回数
   */
  public static int getMaxIteration() {
    return MAX_ITERATION;
  }
  
  /**
   * 反復の最大回数を設定します。
   * @param maxIteration 反復の最大回数
   */
  public static void setMaxIteration(final int maxIteration) {
    DoubleRealExponentialMatrix.MAX_ITERATION = maxIteration;
  }
  
  /**
   * 新しく生成された<code>DoubleRealExponentialMatrix</code>オブジェクトを初期化します。
   */
  private DoubleRealExponentialMatrix() {
    // nothing to do
  }

  /**
   * 実行列の指数関数行列を返します。
   * 
   * <p>実行列をAとするとき、このメソッドは、
   * 
   * <blockquote> I + A + A^2/(2!) + ... + A^n/(n!) + ... </blockquote>
   * 
   * を返します。
   * 
   * @param a 対象となる行列
   * @return 実行列の指数関数行列
   */
  public static DoubleMatrix exp(final DoubleMatrix a) {
    return exp(a, DoubleNumberUtil.EPS * a.frobNorm().doubleValue());
  }

  /**
   * 実行列の指数関数行列を返します。
   * 
   * <p>実行列をAとするとき、このメソッドは、
   * 
   * <blockquote> I + A + A^2/(2!) + ... + A^n/(n!) + ... </blockquote>
   * 
   * を返します。
   * 
   * @param a 対象となる行列
   * @param tolerance 許容誤差
   * @return 実行列の指数関数行列
   */
  public static DoubleMatrix exp(final DoubleMatrix a, final double tolerance) {
    final double nr = a.frobNorm().doubleValue();

    int n;
    if (nr > 0.0) {
      n = 1 + (int)(Math.floor(Math.log(nr) / Math.log(2)));
    } else {
      n = 1;
    }

    n = (n > 0 ? n : 0);
    final int m = (int)(Math.pow(2.0, n));

    return exp1(a.multiply(1.0 / m), tolerance).power(m);
  }

  /**
   * 実行列の指数関数行列を求めます。
   * 
   * @param a 対象となる行列
   * @param tolerance 許容誤差
   * @return 実行列の指数関数行列
   */
  private static DoubleMatrix exp1(final DoubleMatrix a, final double tolerance) {
    final int size = a.getColumnSize();
    DoubleMatrix newValue = DoubleMatrix.unit(size);
    DoubleMatrix oldValue = DoubleMatrix.unit(size);
    DoubleMatrix ans = DoubleMatrix.unit(size);

    double n = 1;
    int i;
    for (i = 1; i <= MAX_ITERATION; i++) {
      newValue = oldValue.multiply(a).multiply(1.0 / n);
      oldValue.copy(newValue);
      ans = ans.add(newValue);
      if (newValue.frobNorm().doubleValue() <= tolerance) {
        break;
      }
      n += 1;
    }

    if (i >= MAX_ITERATION) {
      throw new RuntimeException(Messages.getString("DoubleRealExponentialMatrix.0")); //$NON-NLS-1$
    }

    return ans;
  }
}