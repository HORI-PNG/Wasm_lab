/**
 * $Id: RationalPolynomial.java,v 1.123 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.scalar;

import java.io.PrintStream;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.matrix.PolynomialMatrix;
import org.mklab.nfc.matrix.RationalPolynomialMatrix;


/**
 * 有理多項式を表現するクラスです。
 * 
 * @author koga
 * @version $Revision: 1.123 $, 2008/02/16
 * @param <PS> 多項式スカラーの型
 * @param <PM> 多項式行列の型
 * @param <RS> 有理多項式スカラーの型
 * @param <RM> 有理多項式行列の型
 * @param <ES> 係数スカラーの型
 * @param <EM> 係数行列の型
 */
public interface RationalPolynomial<PS extends Polynomial<PS,PM,RS,RM,ES,EM>, PM extends PolynomialMatrix<PS,PM,RS,RM,ES,EM>, RS extends RationalPolynomial<PS,PM,RS,RM,ES,EM>, RM extends RationalPolynomialMatrix<PS,PM,RS,RM,ES,EM>, ES extends NumericalScalar<ES,EM>,EM extends NumericalMatrix<ES,EM>> extends SymbolicScalar<RS,RM,ES,EM> {
  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子定数(実数)
   * @param denominator 分母多項式
   * @return 有理多項式 
   */
  RS create(final double numerator, final PS denominator);

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子定数(スカラー)
   * @param denominator 分母多項式
   * @return 有理多項式 
   */
  RS create(final ES numerator, final  PS denominator);

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子多項式
   * @param denominator 分母多項式
   * @return 有理多項式
   *  
   */
  RS create(final PS numerator, final PS denominator);
  
//  /**
//   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
//   * 
//   * @param numerator 倍精度分子多項式
//   * @param denominator 倍精度分母多項式
//   * @return 有理多項式
//   *  
//   */
//  S create(final DoublePolynomial numerator, final DoublePolynomial denominator);

//  /**
//   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
//   * @param value value
//   * @return 有理多項式
//   *   
//   */
//  S create(DoubleRationalPolynomial value);

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子多項式
   * @param denominator 分母定数(実数)
   * @return 有理多項式
   *  
   */
  RS create(final PS numerator, final double denominator);

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子多項式
   * @param denominator 分母定数(スカラー)
   * @return 有理多項式
   *  
   */
  RS create(final PS numerator, final ES denominator);

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子多項式
   * @return 有理多項式
   *  
   */
  RS create(final PS numerator);

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子実数
   * @return 有理多項式
   *  
   */
  RS create(final int numerator);
  
  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子実数
   * @param variableName 多項式変数
   * @return 有理多項式
   *  
   */
  RS create(final int numerator, String variableName);

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子実数
   * @return 有理多項式
   *  
   */
  RS create(final double numerator);

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子実数
   * @param variableName 多項式変数
   * @return 有理多項式
   *  
   */
  RS create(final double numerator, final String variableName);

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子スカラー
   * @return 有理多項式
   *  
   */
  RS create(final ES numerator);

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子複素数
   * @param variableName 多項式変数
   * @return 有理多項式
   *  
   */
  RS create(final ES numerator, final String variableName);

  /**
   * 分子多項式の次数を返します。
   * 
   * @return 分子多項式の次数
   */
  int getNumeratorDegree();

  /**
   * 分母多項式の次数を返します。
   * 
   * @return 分母多項式の次数
   */
  int getDenominatorDegree();

  /**
   * 分子多項式を返します。
   * 
   * @return 分子多項式
   */
  PS getNumerator();

  /**
   * 分母多項式を返します。
   * 
   * @return 分母多項式
   */
  PS getDenominator();
  
//  /**
//   * 数値を足した有理多項式を生成します。
//   * 
//   * @param value 加える数値
//   * @return 足し算の結果
//   */
//  S add(final ES value);


  /**
   * 多項式を加えた有理多項式を生成します。
   * 
   * @param value 加える多項式
   * @return 足し算の結果
   */
  RS add(final PS value);

  /**
   * 多項式を引いた有理多項式を生成します。
   * 
   * @param value 引く多項式
   * @return 引き算の結果
   */
  RS subtract(final PS value);

  /**
   * 多項式を乗じた有理多項式を生成します。
   * 
   * @param value 乗じる多項式
   * @return 掛け算の結果
   */
  RS multiply(final PS value);

  /**
   * 多項式で割った有理多項式を生成します。
   * 
   * @param value 割る多項式
   * @return 割り算の結果
   */
  RS divide(final PS value);

//  /**
//   * 多項式で割った有理多項式を生成します。
//   * 
//   * @param value 割る多項式
//   * @return 割り算の結果
//   */
//  S divide(final DoublePolynomial value);

  /**
   * 多項式を割った有理多項式を生成します。
   * 
   * @param value 割られる多項式
   * @return 割り算の結果
   */
  RS leftDivide(final PS  value);
  
  /**
   * 式変数に多項式を代入する評価します。
   * 
   * @param value 変数に代入する多項式
   * @return 評価の結果
   */
  RS evaluate(final  PS value);

  /**
   * 式変数に有理多項式を代入する評価します。
   * 
   * @param value 代入する有理多項式
   * @return 評価の結果
   */
  RS evaluate(final RS value);

  /**
   * 式変数に行列の各成分を代入した結果からなる行列を生成します。
   * 
   * @param value 代入する行列
   * @return 式変数に行列の各成分を代入した結果からなる行列
   */
  EM evaluateElementWise(final EM value);
  
  /**
   * 分母多項式による分子多項式の除算の商多項式を返します。
   * 
   * @return 分母多項式による分子多項式の除算の商多項式
   */
  PS getQuotient();

  /**
   * 分母多項式による分子多項式の除算の剰余(余り)多項式を返します。
   * 
   * @return 分母多項式による分子多項式の除算の剰余(余り)多項式
   */
  PS getRemainder();

  /**
   * 標準出力に出力(表示)します。 <p> 変数名はansです。
   */
  void print();

  /**
   * 標準出力に出力(表示)します。
   * 
   * @param name 名前
   */
  void print(final String name);

  /**
   * 表示文字列を返します。
   * 
   * @param name 名前
   * @return 表示文字列
   */
  String getPrintingString(final String name);

  /**
   * 表示文字列を返します。
   * 
   * @param name 名前
   * @param coefficientFormat 出力フォーマット
   * @return 表示文字列
   */
  String getPrintingString(final String name, final String coefficientFormat);

  /**
   * 出力ストリームに出力します。
   * 
   * @param name 名前
   * @param output 出力ストリーム
   */
  void print(final String name, final PrintStream output);

  /**
   * 出力ストリームに出力します。
   * 
   * @param name 名前
   * @param output 出力ストリーム
   * @param coefficientFormat 出力フォーマット
   */
  void print(final String name, final PrintStream output, final String coefficientFormat);

  /**
   * 文字列に変換します。
   * 
   * @param saving ファイルに保存するならばtrue、そうでなければfalse
   * @return 生成された文字列
   */
  String[] toString(final boolean saving);

  /**
   * 文字列に変換します。
   * 
   * @param saving ファイルに保存するならばtrue、そうでなければfalse
   * @param coefficientFormat 係数の出力フォーマット
   * @return 生成された文字列
   */
  String[] toString(final boolean saving, final String coefficientFormat);

//  /**
//   * 数値を引いた有理多項式を生成します。
//   * 
//   * @param value 引かれる数値
//   * @return 引き算の結果
//   */
//  S subtract(final ES value);
//
//  /**
//   * 数値を乗じた有理多項式を生成します。
//   * 
//   * @param value 乗じられる数値
//   * @return 掛け算の結果
//   */
//  S multiply(final ES value);
//
//  /**
//   * 数値で割った有理多項式を生成します。
//   * 
//   * @param value 割る数値
//   * @return 割り算の結果
//   */
//  S divide(ES value);
//
//  /**
//   * 数値を割った有理多項式を生成します。
//   * 
//   * @param value 割られる数値
//   * @return 掛け算の結果
//   */
//  S leftDivide(final ES value);

  /**
   * 出力の幅を設定します。
   * 
   * @param displayWidth 出力の幅
   */
  void setDisplayWidth(final int displayWidth);

  /**
   * 出力の幅を返します。
   * 
   * @return 出力の幅
   */
  int getDisplayWidth();
}