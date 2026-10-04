/*
 * $Id: Hilbert.java,v 1.6 2008/02/25 08:35:51 koga Exp $
 * 
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.matrix.misc;

import org.mklab.nfc.matrix.BaseMatrixOperator;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.scalar.Scalar;


/**
 * ヒルベルト行列を生成するクラスです。
 * 
 * <p> Hilbert matrix
 * 
 * @author koga
 * @version $Revision: 1.6 $
 */
public final class HilbertMatrix {
  /**
   * 新しく生成された<code>HilbertMatrix</code>オブジェクトを初期化します。
   */
  private HilbertMatrix() {
    // nothing to do
  }

  /**
   * <code>size</code>×<code>size</code>のヒルベルト行列を返します。
   * 
   * @param size 次数
   * @return ヒルベルト行列 (hilbert matrix)
   */
  public static DoubleMatrix createDouble(final int size) {
    final DoubleMatrix ans = new DoubleMatrix(size, size);
    for (int row = 1; row <= size; row++) {
      for (int column = 1; column <= size; column++) {
        ans.setElement(row, column, 1.0 / (row + column - 1));
      }
    }
    return ans;
  }

  /**
   * <code>size</code>×<code>size</code>のヒルベルト行列を返します。
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param size 次数
   * @param scalar scalar
   * @return ヒルベルト行列 (hilbert matrix)
   */
  public static <S extends Scalar<S,M>, M extends BaseMatrixOperator<S,M>> M create(final int size, S scalar) {
    final M ans = scalar.createZeroGrid(size);
    for (int row = 1; row <= size; row++) {
      for (int column = 1; column <= size; column++) {
        ans.setElement(row, column,  scalar.create(row + column - 1).inverse());
      }
    }
    return ans;
  }

}
