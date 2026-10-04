/*
 * $Id: Matrix4.java,v 1.2 2006/08/09 13:49:42 koga Exp $
 * 
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.matrix.util;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;


/**
 * 4個の行列を結合するユーティリティクラスです。
 * 
 * @author koga
 * @version $Revision: 1.2 $
 */
public final class Matrix4 {
  /**
   * 新しく生成された<code>Matrix4</code>オブジェクトを初期化します。
   */
  private Matrix4() {
    // nothing to do
  }

  /**
   * 4個の行列を結合した行列を返します。
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param a11 11行列
   * @param a12 12行列
   * @param a21 21行列
   * @param a22 22行列
   * @return 4個の行列を結合してできる行列
   */
  public static <S extends Scalar<S,M>, M extends Matrix<S,M>> M matrix4(final M a11, final M a12, final M a21, final M a22) {
    return a11.appendRight(a12).appendDown(a21.appendRight(a22));
  }
}
