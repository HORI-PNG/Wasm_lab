/*
 * Created on 2006/08/28
 * Copyright (C) 2006 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matrix;

/**
 * 行列{@link Grid}のフォーマットを表すクラスです。
 * 
 * @author koga
 * @version $Revision: 1.3 $, 2006/08/28
 */
public final class GridFormat {
  /**
   * 新しく生成された<code>GridFormat</code>オブジェクトを初期化します。
   */
  private GridFormat() {
    // nothing to do
  }

  /** 多項式の長さ。 */
  public static final int POLYNOMIAL_LENGTH = 34;
  /** 折り返された長い多項式の2行目以降の先頭のスペースの数。 */
  public static final int LEFT_MARGIN = 7;
  /** 同一行の多項式間のスペースの数。 */
  public static final int COLUMN_SEPARATION = 4;
}
