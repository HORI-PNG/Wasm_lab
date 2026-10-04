/*
 * Created on 2005/08/12
 * Copyright (C) 2005 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matrix;

/**
 * 2個の引数を取るboolean関数を表すインターフェースです。
 * 
 * @author root
 * @version $Revision: 1.9 $
 */
interface BooleanFunctionWithTwoArguments {

  /**
   * 関数の値を評価(計算)結果を返します。
   * 
   * @param argument1 第一引数
   * @param argument2 第二引数
   * @return 関数の評価(計算)結果
   */
  boolean evaluate(double argument1, double argument2);
}