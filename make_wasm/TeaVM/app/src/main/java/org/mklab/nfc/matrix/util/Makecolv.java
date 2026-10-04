/*
 * $Id: Makecolv.java,v 1.6 2007/12/27 23:10:42 koga Exp $
 * 
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.matrix.util;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;


/**
 * 列ベクトルに変換するクラスです。
 * 
 * <p>Make a column vector
 * 
 * @author koga
 * @version $Revision: 1.6 $
 */
public final class Makecolv {
  /**
   * 新しく生成された<code>Makecolv</code>オブジェクトを初期化します。
   */
  private Makecolv() {
    // nothing to do
  }

  /**
   * 列ベクトルに変換します。
   * @param <S> スカラーの型
   * @param <M> 行列の型 
   * @param a 元のベクトル(行列)
   * @return 列ベクトル (column vector)
   */
  public static <S extends Scalar<S,M>, M extends Matrix<S,M>> M makecolv(final M a) {
    final int rowSize = a.getRowSize();
    final int columnSize = a.getColumnSize();

    if (columnSize == 1) {
      return a.createClone();
    }

    if (rowSize == 1) {
      return a.transpose();
    }

    return a.transpose().reshape(rowSize * columnSize, 1);
  }
}