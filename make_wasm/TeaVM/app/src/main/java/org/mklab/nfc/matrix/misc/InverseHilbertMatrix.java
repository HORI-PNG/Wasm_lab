/*
 * $Id: Ihilbert.java,v 1.5 2008/02/25 08:35:52 koga Exp $
 * 
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.matrix.misc;

import org.mklab.nfc.matrix.BaseMatrixOperator;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.scalar.Scalar;


/**
 * 逆ヒルベルト行列を生成するクラスです。
 * 
 * <p>Inverse Hilbert matrix
 * 
 * @author koga
 * @version $Revision: 1.5 $
 */
public final class InverseHilbertMatrix {
  /**
   * 新しく生成された<code>InverseHilbertMatrix</code>オブジェクトを初期化します。
   */
  private InverseHilbertMatrix() {
    // nothing to do
  }

  /**
   * <code>size</code>×<code>size</code>の逆ヒルベルト行列を求めます。
   * 
   * @param size 次数
   * @return 逆ヒルベルト行列 (inverse hilbert matrix)
   */
  public static DoubleMatrix createDouble(final int size) {
    double a = size;
    final DoubleMatrix ans = new DoubleMatrix(size, size);

    for (int i = 1; i <= size; i++) {
      if (i > 1) {
        a = ((size - i + 1.0) * a * (size + i - 1.0)) / Math.pow(i - 1.0, 2);
      }
      double b = a * a;
      ans.setElement(i, i, b / (2 * i - 1.0));
      for (int j = i + 1; j <= size; j++) {
        b = -((size - j + 1.0) * b * (size + j - 1.0)) / Math.pow(j - 1.0, 2);
        ans.setElement(i, j, b / (i + j - 1.0));
        ans.setElement(j, i, b / (i + j - 1.0));
      }
    }
    return ans;
  }

  /**
   * <code>size</code>×<code>size</code>の逆ヒルベルト行列を求めます。
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param size 次数
   * @param scalar  scalar
   * @return 逆ヒルベルト行列 (inverse hilbert matrix)
   */
  public static <S extends Scalar<S,M>, M extends BaseMatrixOperator<S,M>> M createDouble(final int size, S scalar) {
    S a = scalar.create(size);
    final M ans = scalar.createZeroGrid(size, size);

    for (int i = 1; i <= size; i++) {
      if (i > 1) {
        a = scalar.create(size).add(- i + 1).multiply( a).multiply(scalar.create(size).add(i-1)).divide(scalar.create(i-1).power(2));
      }
      S b = a.multiply(a);
      ans.setElement(i, i,  b.divide(2 * i - 1));
      for (int j = i + 1; j <= size; j++) {
        b = scalar.create(size).unaryMinus().add(- j + 1).multiply(b).multiply(scalar.create(size).add(j - 1)).divide(scalar.create(j - 1).power(2));
        ans.setElement(i, j, b.divide((i + j - 1)));
        ans.setElement(j, i, b.divide((i + j - 1)));
      }
    }
    return ans;
  }

}
