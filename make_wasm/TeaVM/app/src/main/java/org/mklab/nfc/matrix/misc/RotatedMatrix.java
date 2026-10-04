/*
 * $Id: Rot90.java,v 1.14 2008/04/13 02:12:38 koga Exp $
 * 
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.matrix.misc;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;


/**
 * 成分を反時計方向に90度回転させてできる行列を生成するクラスです。
 * 
 * <p>Rotate matrix 90 degrees
 * 
 * @author koga
 * @version $Revision: 1.14 $
 */
public final class RotatedMatrix {
  /**
   * 新しく生成された<code>RotatedMatrix</code>オブジェクトを初期化します。
   */
  private RotatedMatrix() {
    // nothing to do
  }

  /**
   * <code>a</code> の成分を反時計方向に <code>90</code> 度回転します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型 
   * @param a 対象となる行列
   * @return 成分を反時計方向に90度回転させてできる行列 (rotated matrix)
   */
  public static <S extends Scalar<S,M>, M extends Matrix<S,M>> M create(final M a) {
    final int count = 1;
    return create(a, count);
  }

  /**
   * <code>a</code> の成分を反時計方向に <code>count*90</code> 度回転します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型 
   * @param a 対象となる行列
   * @param count 回転回数
   * @return 成分を反時計方向に90度回転させてできる行列 (rotated matrix)
   */
  public static <S extends Scalar<S,M>, M extends Matrix<S,M>>  M create(final M a, final int count) {
    int n = count % 4;
    if (n < 0) {
      n = n + 4;
    }

    if (n == 1) {
      return a.transpose().flipUpDown();
    }

    if (n == 2) {
      return a.flipUpDown().flipLeftRight();
    }

    if (n == 3) {
      return a.flipUpDown().transpose();
    }

    return a.createClone();
  }
}