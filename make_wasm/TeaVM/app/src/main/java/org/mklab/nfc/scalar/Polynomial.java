/**
 * $Id: Polynomial.java,v 1.172 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.scalar;

import java.io.PrintStream;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.matrix.PolynomialMatrix;
import org.mklab.nfc.matrix.RationalPolynomialMatrix;


/**
 * 多項式を表すクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.172 $
 * @param <PS> 多項式スカラーの型
 * @param <PM> 多項式行列の型
 * @param <RS> 有理多項式スカラーの型
 * @param <RM> 有理多項式行列の型
 * @param <ES> 係数スカラーの型
 * @param <EM> 係数行列の型
  */
public interface Polynomial<PS extends Polynomial<PS,PM,RS,RM,ES,EM>, PM extends PolynomialMatrix<PS,PM,RS,RM,ES,EM>, RS extends RationalPolynomial<PS,PM,RS,RM,ES,EM>, RM extends RationalPolynomialMatrix<PS,PM,RS,RM,ES,EM>, ES extends NumericalScalar<ES,EM>,EM extends NumericalMatrix<ES,EM>> extends SymbolicScalar<PS,PM,ES,EM> {
  /**
   * <code>constant</code>を係数とする0次多項式を生成します。
   * 
   * <p>多項式変数はデフォルトの"<i>s</i>"です。
   * 
   * @param constant 0次の係数(定数項)
   * @return 多項式
   */
  PS create(final int constant);

  /**
   * <code>constant</code>を係数とする0次多項式を生成します。
   * 
   * <p>多項式変数はデフォルトの"<i>s</i>"です。
   * 
   * @param constant 0次の係数(定数項)
   * @return 多項式
   */
  PS create(final double constant);

  /**
   * 0次の実多項式(定数)を生成します。
   * 
   * @param constant 0次の係数
   * @param variableName 多項式変数
   * @return 多項式
   */
  PS create(final double constant, final String variableName);

  /**
   * 多項式変数(1次の係数が1、0次の係数が0である1次の多項式)を生成します。
   * 
   * @param variableName 多項式変数
   * @return 多項式
   */
  PS create(final String variableName);

  /**
   * <code>constant</code>を係数とする0次の多項式を生成します。
   * 
   * <p>多項式変数はデフォルトの"<i>s</i>"です。
   * 
   * @param constant 0次の係数
   * @return 多項式
   */
 PS create(final ES constant);

 /**
   * <code>constant</code>を係数とする0次の多項式を生成します。
   * 
   * @param constant 0次の係数
   * @param variableName 多項式変数
   * @return 多項式
   */
 PS create(final ES constant, final String variableName);

  /**
   * <code>coefficients</code>を係数とする実多項式を生成します。
   * 
   * <p>多項式変数はデフォルトの"<i>s</i>"です。例えば、
   * 
   * <blockquote> <code>double[] coefficients = new double {3, 2, 1};</code> </blockquote>
   * 
   * とすると、
   * 
   * <blockquote> 3 + 2 s + s^2</blockquote>
   * 
   * となります。
   * 
   * @param coefficients 係数の配列
   * @return 多項式
   */
 PS create(final double[] coefficients);

  /**
   * <code>coefficients</code>を係数とする実多項式を生成します。
   * 
   * <p>例えば、
   * 
   * <blockquote> <code>double[] coefficients = new double {3, 2, 1};</code> </blockquote>
   * 
   * <blockquote> <code>String variable = "x";</code> </blockquote>
   * 
   * とすると、
   * 
   * <blockquote> <code>3 + 2 x + x^2</code> </blockquote>
   * 
   * となります。
   * 
   * @param coefficients 係数の配列
   * @param variableName 多項式変数
   * @return 多項式
   */
  PS create(final double[] coefficients, final String variableName);

  /**
   * <code>coefficients</code>を係数とする多項式を生成します。
   * 
   * <p>多項式変数はデフォルトの"<i>s</i>"です。
   * 
   * @param coefficients 係数の配列
   * @return 多項式
   */
  PS create(final ES[] coefficients);

  /**
   * <code>coefficients</code>を係数とする多項式を生成します。
   * 
   * @param coefficients 係数の配列
   * @param variableName 多項式変数
   * @return 多項式
   */
  PS create(final ES[] coefficients, final String variableName);

  /**
   * 新しく生成された<code>Polynomial</code>オブジェクトを初期化します。
   * @param coefficientVector 係数をもつベクトル(行列)
   * @return 多項式
   */
  PS create(final EM coefficientVector);

  /**
   * 新しく生成された<code>Polynomial</code>オブジェクトを初期化します。
   * @param coefficientVector 係数をもつベクトル(行列)
   * @param variableName 多項式変数
   * @return 多項式
   */
  PS create(final EM coefficientVector, final String variableName);

  /**
   * Creates polynomial matrix.
   * 
   * @param constants 定数行列
   * @return 多項式行列
   */
  PM createGrid(final EM constants);

  /**
   * Creates polynomial matrix.
   * 
   * @param constants 定数行列
   * @param variableName 多項式変数
   * @return 多項式行列
   */
  PM createGrid(final EM constants, final String variableName);

  /**
   * 多項式の次数を返します。
   * 
   * @return 次数
   */
  int getDegree();
  
  /**
   * 多項式の係数を成分とする行ベクトル(行列)を返します。
   * 
   * @return 多項式の係数
   */
  EM getCoefficients();

  /**
   * <code>order</code>次の係数を返します。
   * 
   * @param order 次数
   * @return 項式の係数
   */
  ES getCoefficient(final int order);

  /**
   * <code>order</code>次の係数を設定します。
   * 
   * @param order 次数
   * @param value 係数
   */
  void setCoefficient(final int order, final double value);

  /**
   * <code>order</code>次の係数を設定します。
   * 
   * @param order 次数
   * @param value 係数
   */
  void setCoefficient(final int order, final int value);

  /**
   * <code>order</code>次の係数を設定します。
   * 
   * @param order 次数
   * @param value 係数
   */
  void setCoefficient(final int order, final ES value);

  /**
   * 多項式変数が等しいか判定します。
   * 
   * @param opponent 比較する多項式
   * 
   * @return 変数が等しければtrue、そうでなればfalse
   */
  boolean hasSameVariable(final PS opponent);

  /**
   * 次数が多項式<code>opponent</code>の次数と等しいか判定します。
   * 
   * @param opponent 比較する多項式
   * @return 次数が等しければtrue、そうでなければfalse
   */
  boolean hasSameDegree(final PS opponent);

  /**
   * 同じクラス(実多項式、複素多項式)であるか判定します。
   * 
   * @param opponent 比較する多項式
   * @return 同じクラスならばtrue、そうでなければfalse
   */
  boolean isSameClass(final PS opponent);

  /**
   * 定数であるか判定します。
   * 
   * @return 定数ならばtrue、そうでなければfalse
   */
  boolean isConstant();

  /**
   * 定数であるか判定します。
   * @param tolerance 許容誤差
   * @return 定数ならばtrue、そうでなければfalse
   */
  boolean isConstant(double tolerance);
  
  /**
   * 定数であるか判定します。
   * @param tolerance 許容誤差
   * @return 定数ならばtrue、そうでなければfalse
   */
  boolean isConstant(final ES tolerance);

  /**
   * 多項式変数に多項式を代入した評価結果を返します。
   * 
   * @param value 代入する多項式
   * @return 多項式変数に多項式を代入した評価結果
   */
  PS evaluate(final PS value);

  /**
   * 式変数に行列の各成分を代入した結果からなる行列を生成します。
   * 
   * @param value 代入する行列
   * @return 式変数に行列の各成分を代入した結果からなる行列
   */
  EM evaluateElementWise(final EM value);

//  /**
//   * 式変数に行列の各成分を代入した結果からなる行列を生成します。
//   * 
//   * @param value 代入する行列
//   * @return 式変数に行列の各成分を代入した結果からなる行列
//   */
//  Matrix<?,?> evaluateElementWise(final Matrix<?,?> value);

  /**
   * 1階積分を求めます。
   * 
   * @return 1階積分
   */
  PS integral();

  /**
   * 不定積分を求めます。
   * 
   * @param order 積分の次数
   * @return 不定積分
   */
  PS integral(final int order);

  /**
   * <code>tolerance</code>以下の数を0と見なし、係数を単純化します。
   * 
   * <p>同時に次数も変更します。
   */
  void simplify();

  /**
   * <code>tolerance</code>以下の数を0と見なし、係数を単純化します。
   * 
   * <p>同時に次数も変更します。
   * 
   * @param tolerance 許容誤差
   */
  void simplify(final double tolerance);

  /**
   * <code>tolerance</code>以下の数を0と見なし、係数を単純化します。
   * 
   * <p>同時に次数も変更します。
   * 
   * @param tolerance 許容誤差
   */
  void simplify(final ES tolerance);

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
   * 出力ストリームに出力します。
   * 
   * @param name 名前
   * @param output 出力ストリーム
   */
  void print(final String name, final PrintStream output);

  /**
   * 文字列に変換します。
   * 
   * @param asExpression 式として評価できるようにするならばtrue、そうでなければfalse
   * @return 変換で生成された文字列
   */
  String toString(final boolean asExpression);

  /**
   * 文字列に変換します。
   * 
   * @param asExpression 式として評価できるようにするならばtrue、そうでなければfalse
   * @param coefficientFormat 係数の出力フォーマット
   * @return 変換で生成された文字列
   */
  String toString(final boolean asExpression, final String coefficientFormat);

  /**
   * 多項式の係数を自身にコピーします。
   * 
   * <p>ただし、コピー元の多項式は同次数で、係数の型も同じでなければなりません。
   * 
   * @param source コピーする多項式
   */
  void copy(final PS source);

//  /**
//   * データのみを出力ストリームに出力します。
//   * 
//   * @param output 出力ストリーム
//   * @param asComplex 複素数として出力するならばtrue、そうでなければfalse
//   * @throws IOException ストリームに出力できない場合
//   */
//  void writeMxFormatWithoutHeader(final OutputStream output, final boolean asComplex) throws IOException;
  
  /**
   * <code>operator</code>で指定された演算子で比較します。
   * 
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param opponent 比較対象
   * @return 比較式が等しければtrue、そうでなければfalse
   */
  boolean compare(final String operator, final PS opponent);

  /**
   * <code>operator</code>で指定された演算子で比較 します。
   * 
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param opponent 比較対象
   * @return 比較式が正しければtrue、そうでなければfalse
   */
  boolean compare(final String operator, final int opponent);

  /**
   * <code>operator</code>で指定された演算子で比較します。
   * 
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param opponent 比較対象
   * @return 比較式が等しければtrue、そうでなければfalse
   */
  boolean compare(final String operator, final double opponent);

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
  
  /**
   * <code>toMin</code>次から<code>toMax</code>次までの係数に多項式 <code>source</code>の<code> fromMin</code>次から<code>fromMax</code>次までの係数をコピーします。
   * 
   * @param toMin 変更開始次数
   * @param toMax 変更終了次数
   * @param source コピーする多項式
   * @param fromMin コピー開始次数
   * @param fromMax コピー終了次数
   */
  void partCopy(final int toMin, final int toMax, final PS source, final int fromMin, final int fromMax);
  
  /**
   * 零成分を用いて次数を拡張します。
   * 
   * @param newDegree 新しい次数
   * @return 次数を拡張した多項式
   */
  PS expand(final int newDegree);
  
  /**
   * Generates rational polynomial with denominator of 1.
   * 
   * @return rational polynomial
   */
  RS toRational();
}