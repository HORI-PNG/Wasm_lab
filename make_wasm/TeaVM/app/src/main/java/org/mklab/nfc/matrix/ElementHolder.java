/*
 * Created on 2008/02/02
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matrix;

/**
 * 成分とその指数を保持するためのクラスです。
 * 
 * @author koga
 * @version $Revision: 1.1 $, 2008/02/02
 * @param <S> スカラーの型
 */
public class ElementHolder<S extends GridElement<S>> {

  /** 成分。 */
  private S element;
  /** 行番号。 */
  private int row;
  /** 列番号。 */
  private int column;

  /**
   * 新しく生成された<code>ElementHolder</code>オブジェクトを初期化します。
   * 
   * @param element 成分
   * @param row 行番号
   * @param column 列番号
   */
  public ElementHolder(final S element, final int row, final int column) {
    this.element = element.clone();
    this.row = row;
    this.column = column;
  }

  /**
   * 成分を返します。
   * 
   * @return 成分
   */
  public final S getElement() {
    return this.element;
  }

  /**
   * 行番号を返します。
   * 
   * @return 行番号
   */
  public final int getRow() {
    return this.row;
  }

  /**
   * 列番号を返します。
   * 
   * @return 列番号
   */
  public final int getColumn() {
    return this.column;
  }
}
