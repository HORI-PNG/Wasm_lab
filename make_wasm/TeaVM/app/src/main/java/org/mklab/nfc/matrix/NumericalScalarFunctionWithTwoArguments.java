/*
 * $Id: NumericalScalarFunctionWithTwoArguments.java,v 1.2 2008/03/15 00:36:44 koga Exp $
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
 * @param <CS> 係数スカラーの型
 * @param <CM> 係数行列の型
 */
interface NumericalScalarFunctionWithTwoArguments<CS extends NumericalScalar<CS,CM>, CM extends NumericalMatrix<CS,CM>> {

  /**
   * 関数の評価(計算)結果を返します。
   * 
   * @param argument1 関数の引数1
   * @param argument2 関数の引数2
   * @return 関数の評価(計算)結果
   */
  CS evaluate(CS argument1, CS argument2);

//  /**
//   * 関数の評価(計算)結果を返します。
//   * 
//   * @param argument1 関数の引数1
//   * @param argument2 関数の引数2
//   * @return 関数の評価(計算)結果
//   */
//  NumericalScalar<?> evaluate(NumericalScalar<?> argument1, int argument2);
}
