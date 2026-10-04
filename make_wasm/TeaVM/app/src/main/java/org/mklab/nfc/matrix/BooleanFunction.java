/*
 * Created on 2005/08/12
 * Copyright (C) 2005 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matrix;

/**
 * boolean関数を表すインターフェースです。
 * 
 * @author root
 * @version $Revision: 1.11 $
 */
interface BooleanFunction {

  /**
   * 関数の評価(計算)結果を返します。
   * 
   * @param argument 引数
   * @return 関数の評価(計算)結果
   */
  boolean evaluate(double argument);
}