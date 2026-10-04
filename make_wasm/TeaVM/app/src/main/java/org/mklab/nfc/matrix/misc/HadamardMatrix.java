/*
 * $Id: Hadamard.java,v 1.8 2008/04/06 02:16:45 koga Exp $
 * 
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.matrix.misc;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.matrix.util.Matrix4;
import org.mklab.nfc.scalar.Scalar;


/**
 * アダマール行列を生成するクラスです。
 * 
 * <p> Hadamard matrix
 * 
 * @author koga
 * @version $Revision: 1.8 $
 */
public final class HadamardMatrix {
  /**
   * 新しく生成された<code>HadamardMatrix</code>オブジェクトを初期化します。
   */
  private HadamardMatrix() {
    // nothing to do
  }

  /**
   * <code>2^size</code>次のアダマール行列を返します。
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param size 次数
   * @param unit  unit
   * @return アダマール行列 (hadamard matrix)
   */
  public static <S extends Scalar<S,M>, M extends Matrix<S,M>> M create(final int size, final S unit) {
    if (size < 1) {
      return unit.createOnesGrid(1);
    }

    final M a = create(size - 1, unit);
    return Matrix4.matrix4(a, a, a, a.unaryMinus());
  }
}
