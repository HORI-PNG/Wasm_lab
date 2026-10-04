/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matrix;

import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 行列とその指数を保持するためのクラスです。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
class IndexedElements<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {
  /** 並び替えた成分。 */
  private S[][] elements;

  /** 指数。 */
  private int[][] indices;
  
  /**
   * 新しく生成された<code>IndexedElements</code>オブジェクトを初期化します。
   * 
   * @param elements 成分
   * @param indices 指数
   */
  IndexedElements(final S[][] elements, final int[][] indices) {
    this.elements = elements;
    this.indices = indices;
  }

  /**
   * 成分を返します。
   * 
   * @return 成分
   */
  S[][] getElements() {
    return this.elements;
  }

  /**
   * 指数を返します。
   * 
   * @return 指数
   */
  int[][] getIndices() {
    return this.indices;
  }
}
