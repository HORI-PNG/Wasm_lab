/*
 * $Id: NumericalScalarFunction.java,v 1.2 2008/03/15 00:36:44 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 数値スカラー関数を定義するためのインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.2 $
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
interface NumericalScalarFunction<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {

  /**
   * 関数の評価(計算)結果を返します。
   * 
   * @param argument 関数の引数
   * @return 関数の評価(計算)結果
   */
  S evaluate(S argument);
}
