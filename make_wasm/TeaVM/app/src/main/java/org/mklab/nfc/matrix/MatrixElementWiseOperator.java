/*
 * Created on 2007/11/13
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matrix;

import org.mklab.nfc.scalar.Scalar;


/**
 * 行列の成分毎の演算を表すインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.6 $, 2007/11/13
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public interface MatrixElementWiseOperator<S extends Scalar<S,M>, M extends Matrix<S,M>> {

  /**
   * 成分毎に整数を加えます。
   * 
   * @param value 加える整数
   * @return 加算の結果
   */
  M addElementWise(int value);

  /**
   * 成分毎に実数を加えます。
   * 
   * @param value 加える実数
   * @return 加算の結果
   */
  M addElementWise(double value);

  /**
   * 成分毎にスカラーを加えます。
   * 
   * @param value 加えるスカラー
   * @return 加算の結果
   */
  M addElementWise(S value);

  /**
   * 成分毎に実数を引きます。
   * 
   * @param value 引く実数
   * @return 減算の結果
   */
  M subtractElementWise(int value);

  /**
   * 成分毎に実数を引きます。
   * 
   * @param value 引く実数
   * @return 減算の結果
   */
  M subtractElementWise(double value);

  /**
   * 成分毎にスカラーを引きます。
   * 
   * @param value 引くスカラー
   * @return 減算の結果
   */
  M subtractElementWise(S value);

  /**
   * <code>value</code>との成分毎の積を成分にもつ行列を返します。
   * 
   * @param value 乗じる行列
   * @return 乗算の結果
   */
  M multiplyElementWise(M value);

  /**
   * <code>value</code>との成分毎の商を成分にもつ行列を返します。
   * 
   * @param value 割る行列
   * @return 割り算の結果
   */
  M divideElementWise(M value);

  /**
   * <code>value</code>との成分毎の左からの商を成分にもつ行列を返します。
   * 
   * @param value 割られる行列
   * @return 割り算の結果
   */
  M leftDivideElementWise(M value);

  /**
   * 成分毎の逆数からなる行列を返します。
   * 
   * @return 成分毎逆数行列
   */
  M inverseElementWise();

  /**
   * 成分毎に累乗します。
   * 
   * @param order 累乗の指数
   * @return 累乗の結果
   */
  M powerElementWise(int order);

  /**
   * 成分毎に累乗します。
   * 
   * @param order 累乗の指数を成分とする行列
   * @return 累乗の結果
   */
  M powerElementWise(M order);

  /**
   * 成分毎に累乗します。
   * 
   * @param order 累乗の指数を成分とする行列
   * @return 累乗の結果
   */
  M powerElementWise(IntMatrix order);

  /**
   * 小さい整数に丸めます。
   * 
   * @return 丸められた結果
   */
  M floorElementWise();

  /**
   * 大きい整数に丸めます。
   * 
   * @return 丸められた結果
   */
  M ceilElementWise();

  /**
   * ゼロ方向の整数に丸めます。
   * 
   * @return 丸められた結果
   */
  M fixElementWise();

  /**
   * 最も近い整数に丸めます。
   * 
   * @return 丸められた結果
   */
  M roundElementWise();

  /**
   * 絶対値が小さい成分を0に丸めます。
   * 
   * @return 丸められた結果
   */
  M roundToZeroElementWise();

  /**
   * 絶対値が小さい成分を0に丸めます。
   * 
   * @param tolerance 許容誤差
   * @return 丸められた結果
   */
  M roundToZeroElementWise(double tolerance);

  /**
   * 各成分の有限性(無限大でなく、NaNでない)の真偽を成分にもつ行列を返します。
   * 
   * @return 有限性(無限大でなく、NaNでない)のboolean行列
   */
  BooleanMatrix isFiniteElementWise();

  /**
   * 各成分の無限性の真偽を成分にもつ行列を返します。
   * 
   * @return 無限性のboolean行列
   */
  BooleanMatrix isInfiniteElementWise();

  /**
   * 各成分の非数性の真偽を成分にもつ行列を返します。
   * 
   * @return 非数性のboolean行列
   */
  BooleanMatrix isNanElementWise();

  /**
   * 各成分と<code>value</code>を<code>operator</code>で指定された演算子で比較し, {@link BooleanMatrix}で返します。
   * 
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param value 比較対象
   * 
   * @return 各成分に比較結果が入ったBooleanMatrix
   */
  BooleanMatrix compareElementWise(String operator, int value);

  /**
   * 各成分と<code>value</code>を<code>operator</code>で指定された演算子で比較し, {@link BooleanMatrix}で返します。
   * 
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param value 比較対象
   * 
   * @return 各成分に比較結果が入ったBooleanMatrix
   */
  BooleanMatrix compareElementWise(String operator, double value);

  /**
   * 各成分と<code>value</code>を<code>operator</code>で指定された演算子で比較し, {@link BooleanMatrix}で返します。
   * 
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param value 比較対象
   * 
   * @return 各成分に比較結果が入ったBooleanMatrix
   */
  BooleanMatrix compareElementWise(String operator, S value);
}
