/*
 * $Id: ArrayElement.java,v 1.10 2008/01/14 15:06:28 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

/**
 * 配列の成分を表すインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.10 $
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public interface ArrayElement<S extends ArrayElement<S,M>, M extends Array<M>> extends GridElement<S> {

  /**
   * 行列を生成します。
   * 
   * @param elements 配列の成分
   * @return 生成した行列
   */
  M createGrid(S[][] elements);

  /**
   * 行ベクトルを生成します。
   * 
   * @param elements ベクトルの成分
   * @return 生成したベクトル
   */
  M createGrid(S[] elements);
}
