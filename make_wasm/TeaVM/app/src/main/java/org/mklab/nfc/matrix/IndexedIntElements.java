/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matrix;

/**
 * 行列とその指数を保持するためのクラスです。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 */
class IndexedIntElements {
  /** 並び替えた成分。 */
  int[][] elements;

  /** 指数。 */
  int[][] indices;

  /**
   * 新しく生成された<code>IndexedElements</code>オブジェクトを初期化します。
   * 
   * @param elements 成分
   * @param indices 指数
   */
  IndexedIntElements(final int[][] elements, final int[][] indices) {
    this.elements = elements;
    this.indices = indices;
  }

  /**
   * 成分を返します。
   * 
   * @return 成分
   */
  int[][] getElements() {
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
