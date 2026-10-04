/*
 * $Id: SymbolicScalar.java,v 1.2 2008/03/15 00:36:44 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.scalar;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.matrix.SymbolicMatrix;


/**
 * 数式スカラーを表すインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.2 $
 * @param <S> スカラーの型
 * @param <M> 行列の型
 * @param <ES> 係数スカラーの型
 * @param <EM> 係数行列の型
 */
public interface SymbolicScalar<S extends SymbolicScalar<S,M,ES,EM>, M extends SymbolicMatrix<S,M,ES,EM>, ES extends NumericalScalar<ES,EM>, EM extends NumericalMatrix<ES,EM>> extends Scalar<S,M> {
  /**
   * 1階微分を返します。
   * 
   * @return 1階微分
   */
  S derivative();

  /**
   * 導関数を返します。
   * 
   * @param order 微分の階数
   * @return 導関数
   */
  S derivative(int order);

  /**
   * 係数を次数の低い方向へシフトした値を返します。
   * 
   * @return 係数をシフトして得られる値
   */
  S shiftLower();

  /**
   * 係数を次数の低い方向へシフトした値を返します。
   * 
   * @param count シフトする数
   * @return 係数をシフトして得られる値
   */
  S shiftLower(int count);

  /**
   * 係数を次数の高い方向へシフトした値を返します。
   * 
   * @return 係数をシフトして得られる値
   */
  S shiftHigher();

  /**
   * 係数を次数の高い方向へシフトした値を返します。
   * 
   * @param count シフトする数
   * @return 係数をシフトして得られる値
   */
  S shiftHigher(int count);

  /**
   * 式変数を表す文字列を返します。
   * 
   * @return 式変数を表す文字列
   */
  String getVariable();

  /**
   * 式変数を指定した文字列に変更します。
   * 
   * @param variableName 設定する式変数
   */
  void setVariable(String variableName);

  /**
   * 式変数に整数を代入した評価結果を返します。
   * 
   * @param value 代入する整数
   * 
   * @return 式変数に整数を代入した評価結果
   */
  ES evaluate(int value);

  /**
   * 式変数に倍精度実数を代入した評価結果を返します。
   * 
   * @param value 代入する倍精度実数
   * 
   * @return 式変数に倍精度実数を代入した評価結果
   */
  ES evaluate(double value);

  /**
   * 式変数に倍精度複素数を代入した評価結果を返します。
   * 
   * @param value 代入する倍精度複素数
   * 
   * @return 式変数に倍精度複素数を代入した評価結果
   */
  ES evaluate(ES value);

  /**
   * 式変数にスカラーを代入した評価結果を返します。
   * 
   * @param scalar 代入するスカラー
   * 
   * @return 式変数にスカラーを代入した評価結果
   */
  S evaluate(S scalar);

  /**
   * 式変数に行列を代入した評価結果を返します。
   * 
   * @param argument 代入する行列
   * @return 式変数に行列を代入した評価結果
   */
  EM evaluate(EM argument);

  /**
   * 許容範囲内で等しいか判定します。
   * 
   * @param opponent 比較する値
   * @param tolerance 許容誤差
   * @return 等しければtrue、そうでなければfalse
   */
  boolean equals(S opponent, ES tolerance);
  
  /**
   * 零であるか判定します。
   * 
   * @param tolerance 許容誤差
   * @return 零(絶対値がtolerance以下)ならばtrue、そうでなければfalse
   */
  boolean isZero(ES tolerance);

  /**
   * 1(単位元)であるか判定します。
   * 
   * @param tolerance 許容誤差
   * @return 1(単位元)(1との差の絶対値がtolerance以下)ならばtrue、そうでなければfalse
   */
  boolean isUnit(ES tolerance);


  /**
   * 絶対値が小さい成分を0に丸めます。
   * 
   * @param tolerance 許容誤差
   * @return 丸めた結果
   */
  S roundToZero(ES tolerance);
  
  /**
   * 数値との和を生成します。
   * 
   * @param value 数値
   * @return 数値との和
   */
  S add(final ES value);
  
  /**
   * 数値との商を生成します。
   * 
   * @param value 数値
   * @return 数値との商
   */
  S divide(final ES value);

  /**
   * 数値との左商を生成します。
   * 
   * @param value 数値
   * @return 数値との商
   */
  S leftDivide(final ES value);

  /**
   * 数値との差を生成します。
   * 
   * @param value 数値
   * @return 数値との差
   */
  S subtract(final ES value);

  /**
   * 数値との積を生成します。
   * 
   * @param value 数値
   * @return 数値との積
   */
  S multiply(final ES value);




}
