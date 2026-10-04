/*
 * Created on 2005/08/12
 * Copyright (C) 2005 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matrix;

/**
 * 倍精度(double)型の実数関数を定義するためのクラスです。
 * 
 * @author root
 * @version $Revision: 1.1 $
 */
interface DoubleRealFunction {

  /**
   * 関数の評価(計算)結果を返します。
   * 
   * @param argument 関数の引数
   * @return 関数の評価(計算)結果
   */
  double evaluate(double argument);
}