/*
 * $Id: NumericalScalar.java,v 1.2 2008/03/15 00:36:44 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.scalar;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.random.RandomGenerator;


/**
 * 数値スカラーを表すインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.2 $
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public interface NumericalScalar<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> extends Scalar<S,M> {
  /**
   * 絶対値を返します。
   * 
   * @return 絶対値
   */
  S abs();

  /**
   * 絶対値の2乗を返します。
   * 
   * @return 絶対値の2乗
   */
  S abs2();

  /**
   * 平方根を返します。
   * 
   * @return 平方根
   */
  S sqrt();

  /**
   * この値と引き数の最大値を返します。
   * 
   * @param value 比較する値
   * @return この値と引き数の最大値
   */
  S max(int value);

  /**
   * この値と引き数の最大値を返します。
   * 
   * @param value 比較する値
   * @return この値と引き数の最大値
   */
  S max(double value);

  /**
   * この値と引き数の最大値を返します。
   * 
   * @param value 比較する値
   * @return この値と引き数の最大値
   */
  S max(S value);

  /**
   * この値と引き数の最小値を返します。
   * 
   * @param value 比較する値
   * @return この値と引き数の最小値
   */
  S min(int value);

  /**
   * この値と引き数の最小値を返します。
   * 
   * @param value 比較する値
   * @return この値と引き数の最小値
   */
  S min(double value);

  /**
   * この値と引き数の最小値を返します。
   * 
   * @param value 比較する値
   * @return この値と引き数の最小値
   */
  S min(S value);

  /**
   * 実数<code>scalar</code>乗(<code>this</code> <sup><code>scalar</code></sup>)を返します。
   * 
   * @param scalar 実数
   * @return scalar乗
   */
  S power(double scalar);

  /**
   * <code>scalar</code>乗(<code>this</code> <sup><code>scalar</code></sup>)を返します。
   * 
   * @param scalar スカラー
   * @return scalar乗
   */
  S power(S scalar);

  /**
   * 正弦関数の値を返します。
   * 
   * @return 正弦関数の値
   */
  S sin();

  /**
   * 双曲線正弦関数の値を返します。 sinh(a) = (exp(a) - exp(-a))/2
   * 
   * @return 双曲線正弦関数の値
   */
  S sinh();

  /**
   * 逆正弦関数の値を返します。 asin(a) = - i log(i*a + sqrt(1 - a*a))
   * 
   * @return 逆正弦関数の値
   */
  S asin();

  /**
   * 逆双曲線関数の値を返します。 asinh(a) = log(a + sqrt(a*a + 1))
   * 
   * @return 逆双曲線関数の値
   */
  S asinh();

  /**
   * 余弦関数の値を返します。 cos(a)
   * 
   * @return 余弦関数の値
   */
  S cos();

  /**
   * 双曲線関数の値を返します。 cosh(a) = (exp(a) + exp(-a))/2
   * 
   * @return 双曲線関数の値
   */
  S cosh();

  /**
   * 逆余弦関数の値を返します。 acos(a) = - i log(a + sqrt(a*a - 1.0))
   * 
   * @return 逆余弦関数の値
   */
  S acos();

  /**
   * 逆双曲線余弦関数の値を返します。 acosh(a) = log(a + sqrt(a*a - 1))
   * 
   * @return 逆双曲線余弦関数の値
   */
  S acosh();

  /**
   * 正接関数の値を返します。 tan()
   * 
   * @return 正接関数の値
   */
  S tan();

  /**
   * 双曲線正接関数の値を返します。 tanh()
   * 
   * @return 双曲線正接関数の値
   */
  S tanh();

  /**
   * 逆正接の値を返します。 atan(a) = 1/2i log((1+i*a)/(1-i*a))
   * 
   * @return 逆正接関数の値
   */
  S atan();

  /**
   * 逆正接(2)の値を返します。
   * 
   * @param scalar 分母側の数
   * @return 逆正接(2)の値
   */
  S atan2(S scalar);

  /**
   * 逆正接(2)の値を返します。
   * 
   * @param scalar 分母側の数
   * @return 逆正接(2)の値
   */
  S atan2(int scalar);

  /**
   * 逆正接(2)の値を返します。
   * 
   * @param scalar 分母側の数
   * @return 逆正接(2)の値
   */
  S atan2(double scalar);

  /**
   * 逆双曲線正接関数の値を返します。 atanh(a) = 1/2 log((1+a)/(1-a))
   * 
   * @return 逆双曲線正接関数の値
   */
  S atanh();

  /**
   * 指数関数の値を返します。
   * 
   * @return 指数関数の値
   */
  S exp();

  /**
   * 自然対数の値を返します。
   * 
   * @return 自然対数の値
   */
  S log();

  /**
   * 常用対数の値を返します。
   * 
   * @return 常用対数の値
   */
  S log10();

  /**
   * 剰余関数を計算します。
   * 
   * @param value2 割る数
   * @return 剰余
   */
  S remainder(S value2);

  /**
   * 剰余関数を計算します。
   * 
   * @param value2 割る数
   * @return 剰余
   */
  S remainder(int value2);

  /**
   * 剰余関数を計算します。
   * 
   * @param value2 割る数
   * @return 剰余
   */
  S remainder(double value2);

  /**
   * 符合付剰余関数を計算します。
   * 
   * @param value2 割る数
   * @return 符合付剰余
   */
  S modulus(int value2);

  /**
   * 符合付剰余関数を計算します。
   * 
   * @param value2 割る数
   * @return 符合付剰余
   */
  S modulus(double value2);

  /**
   * 符合付剰余関数を計算します。
   * 
   * @param value2 割る数
   * @return 符合付剰余
   */
  S modulus(S value2);

  /**
   * <code>opponent</code>より小さいか判定します。
   * 
   * @param opponent 比較対象
   * @return <code>opponent</code>より小さいならばtrue、そうでなければfalse
   */
  boolean isLessThan(S opponent);

  /**
   * <code>opponent</code>以下であるか判定します。
   * 
   * @param opponent 比較対象
   * @return <code>opponent</code>以下ならばtrue、そうでなければfalse
   */
  boolean isLessThanOrEquals(S opponent);

  /**
   * <code>opponent</code>より大きいか判定します。
   * 
   * @param opponent 比較対象
   * @return <code>opponent</code>より大きいならばtrue、そうでなければfalse
   */
  boolean isGreaterThan(S opponent);

  /**
   * <code>opponent</code>以上であるか判定します。
   * 
   * @param opponent 比較対象
   * @return <code>opponent</code>以上ならばtrue、そうでなければfalse
   */
  boolean isGreaterThanOrEquals(S opponent);

  /**
   * <code>opponent</code>より小さいか判定します。
   * 
   * @param opponent 比較対象
   * @return <code>opponent</code>より小さいならばtrue、そうでなければfalse
   */
  boolean isLessThan(int opponent);

  /**
   * <code>opponent</code>より小さいか判定します。
   * 
   * @param opponent 比較対象
   * @return <code>opponent</code>より小さいならばtrue、そうでなければfalse
   */
  boolean isLessThan(double opponent);

  /**
   * <code>opponent</code>以下であるか判定します。
   * 
   * @param opponent 比較対象
   * @return <code>opponent</code>以下ならばtrue、そうでなければfalse
   */
  boolean isLessThanOrEquals(int opponent);

  /**
   * <code>opponent</code>以下であるか判定します。
   * 
   * @param opponent 比較対象
   * @return <code>opponent</code>以下ならばtrue、そうでなければfalse
   */
  boolean isLessThanOrEquals(double opponent);

  /**
   * <code>opponent</code>より大きいか判定します。
   * 
   * @param opponent 比較対象
   * @return <code>opponent</code>より大きいならばtrue、そうでなければfalse
   */
  boolean isGreaterThan(int opponent);

  /**
   * <code>opponent</code>より大きいか判定します。
   * 
   * @param opponent 比較対象
   * @return <code>opponent</code>より大きいならばtrue、そうでなければfalse
   */
  boolean isGreaterThan(double opponent);

  /**
   * <code>opponent</code>以上であるか判定します。
   * 
   * @param opponent 比較対象
   * @return <code>opponent</code>以上ならばtrue、そうでなければfalse
   */
  boolean isGreaterThanOrEquals(int opponent);

  /**
   * <code>opponent</code>以上であるか判定します。
   * 
   * @param opponent 比較対象
   * @return <code>opponent</code>以上ならばtrue、そうでなければfalse
   */
  boolean isGreaterThanOrEquals(double opponent);

  /**
   * 許容範囲内で等しいか判定します。
   * 
   * @param opponent 比較する値
   * @param tolerance 許容誤差
   * @return 等しい(差の絶対値がtolerance以下)ならばtrue、そうでなければfalse
   */
  boolean equals(S opponent, S tolerance);
  
  /**
   * 零であるか判定します。
   * 
   * @param tolerance 許容誤差
   * @return 零(絶対値がtolerance以下)ならばtrue、そうでなければfalse
   */
  boolean isZero(S tolerance);

  /**
   * 1(単位元)であるか判定します。
   * 
   * @param tolerance 許容誤差
   * @return 1(単位元)(1との差の絶対値がtolerance以下)ならばtrue、そうでなければfalse
   */
  boolean isUnit(S tolerance);

  /**
   * 機種精度(Machine Epsilon)を返します。
   * 
   * @return 機種精度(Machine Epsilon)
   */
  S getMachineEpsilon();

  /**
   * 無限大を返します。
   * 
   * @return 無限大
   */
  S getInfinity();

  /**
   * NaN(Not a Number)を返します。
   * 
   * @return NaN(Not a Number)
   */
  S getNaN();

  /**
   * 円周率PIを返します。
   * 
   * @return 円周率PI
   */
  S createPI();

  /**
   * ネイピアの数(自然対数の底)を返します。
   * 
   * @return ネイピアの数(自然対数の底)
   */
  S createE();

  /**
   * 文字列に対応する数を返します。
   * 
   * @param numberString 数を表す文字列
   * @return 文字列に対応する数
   */
  S valueOf(String numberString);

  /**
   * 一様分布の乱数生成器を返します。
   * 
   * @return 一様分布の乱数生成器
   */
  RandomGenerator<S,M> createUniformRandomGenerator();

  /**
   * 絶対値が小さい成分を0に丸めます。
   * 
   * @param tolerance 許容誤差
   * @return 丸めた結果
   */
  S roundToZero(S tolerance);
}
