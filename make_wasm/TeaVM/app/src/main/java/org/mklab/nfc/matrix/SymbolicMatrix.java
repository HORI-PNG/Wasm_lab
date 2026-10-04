/*
 * $Id: SymbolicMatrixOperator.java,v 1.9 2008/03/15 00:23:42 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

import org.mklab.nfc.scalar.NumericalScalar;
import org.mklab.nfc.scalar.SymbolicScalar;


/**
 * 数式行列を表すインターフェースです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.9 $
 * @param <S> スカラーの型
 * @param <M> 行列の型
 * @param <ES> 係数スカラーの型
 * @param <EM> 係数行列の型
 */
public interface SymbolicMatrix<S extends SymbolicScalar<S,M,ES,EM>, M extends SymbolicMatrix<S,M,ES,EM>, ES extends NumericalScalar<ES,EM>, EM extends NumericalMatrix<ES,EM>> extends BaseMatrixOperator<S,M> {

  /**
   * 各成分の1階導関数を成分とする行列を生成します。
   * 
   * @return 各成分の1階導関数を成分とする行列
   */
  M derivative();

  /**
   * 各成分の<code>order</code>階導関数を成分とする行列を生成します。
   * 
   * @param order 階数
   * @return 各成分の<code>order</code>t階導関数を成分とする行列
   */
  M derivative(int order);

  /**
   * 各成分の係数を高次方向に1回シフトした式を成分とする行列を生成します。
   * 
   * @return 各成分の係数を高次方向に1回シフトした式を成分とする行列
   */
  M shiftHigher();

  /**
   * 各成分の係数を高次方向に<code>count</code>回シフトした式を成分とする行列を生成します。
   * 
   * @param count シフトの数
   * @return 各成分の係数を高次方向に<code>count</code>回シフトした式を成分とする行列
   */
  M shiftHigher(int count);

  /**
   * 各成分の係数を提示方向に1回シフトした式を成分とする行列を生成します。
   * 
   * @return 各成分の係数を提示方向に1回シフトした式を成分とする行列
   */
  M shiftLower();

  /**
   * 各成分の係数を提示方向に<code>count</code>回シフトした式を成分とする行列を生成します。
   * 
   * @param count シフトの数
   * @return 各成分の係数を提示方向に<code>count</code>回シフトした式を成分とする行列
   */
  M shiftLower(int count);

  /**
   * 数式行列の変数を<code>variableName</code>で指定した文字列に変更します。
   * 
   * @param variableName 設定する式変数
   */
  void setVariable(String variableName);

  /**
   * 変数に整数を代入して評価します。
   * 
   * @param argument 変数に代入する整数
   * @return 評価した結果
   */
  EM evaluate(int argument);

  /**
   * 変数に倍精度実数を代入して評価します。
   * 
   * @param argument 変数に代入する倍精度実数
   * @return 評価した結果
   */
  EM evaluate(double argument);

  /**
   * 変数に倍精度実数を代入して評価します。
   * 
   * @param argument 変数に代入する倍精度実数
   * @return 評価した結果
   */
  EM evaluate(ES argument);

  /**
   * 変数に値を代入して、評価します。
   * 
   * @param argument 変数に代入する値
   * @return 評価結果
   */
  M evaluate(S argument);

  /**
   * 変数に行列の成分を代入して、評価します。
   * 
   * @param argument 変数に代入する成分をもつ行列
   * @return 評価結果
   */
  EM evaluate(IntMatrix argument);

//  /**
//   * 変数に行列の成分を代入して、評価します。
//   * 
//   * @param argument 変数に代入する成分をもつ行列
//   * @return 評価結果
//   */
//  NumericalMatrix<?,?> evaluate(DoubleMatrix argument);

  /**
   * 変数に行列の成分を代入して、評価します。
   * 
   * @param argument 変数に代入する成分をもつ行列
   * @return 評価結果
   */
  EM evaluate(EM argument);

  /**
   * 変数に行列を代入して、評価します。
   * 
   * @param argument 変数に代入する行列
   * @return 評価結果
   */
  EM evaluateElementWise(EM argument);
  
  /**
   * Multiply a scalar.
   * 
   * @param value value
   * @return result of multiplication
   */
  M multiply(ES value);

  /**
   * Divide by a scalar.
   * 
   * @param value value
   * @return result of division
   */
  M divide(ES value);
}
