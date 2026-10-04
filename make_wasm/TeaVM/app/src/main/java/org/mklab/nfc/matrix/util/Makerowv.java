/*
 * $Id: Makerowv.java,v 1.5 2007/12/27 23:10:42 koga Exp $
 * 
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.matrix.util;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;


/**
 * 行ベクトルに変換するクラスです。
 * 
 * <p>Make a row vector
 * 
 * @author koga
 * @version $Revision: 1.5 $
 */
public final class Makerowv {
  /**
   * 新しく生成された<code>Makerowv</code>オブジェクトを初期化します。
   */
  private Makerowv() {
    // nothing to do
  }

  /**
   * 行ベクトルに変換します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型 
   * @param a 対象となるベクトル(行列)
   * @return 行ベクトル (row vector)
   */
  public static <S extends Scalar<S,M>, M extends Matrix<S,M>> M makerowv(final M a) {
    final int rowSize = a.getRowSize();
    final int columnSize = a.getColumnSize();

    if (columnSize == 1) {
      return a.transpose();
    }

    if (rowSize == 1) {
      return a.createClone();
    }

    return a.transpose().reshape(1, rowSize * columnSize);
  }

}