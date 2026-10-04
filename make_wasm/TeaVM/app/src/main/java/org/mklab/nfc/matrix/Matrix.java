/*
 * $Id: Matrix.java,v 1.146 2008/04/16 23:50:11 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

import org.mklab.nfc.scalar.Scalar;


/**
 * 行列を表わすインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.146 $
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public interface Matrix<S extends Scalar<S,M>, M extends Matrix<S,M>> extends FundamentalMatrix<S,M>, MatrixElementOperator<S,M>, MatrixElementWiseOperator<S,M> {
  /**
   * <code>value</code>との和を返します。
   * 
   * @param value 行列
   * @return <code>value</code>との和
   */
  M add(M value);

  /**
   * <code>value</code>との差を返します。
   * 
   * @param value 引く行列
   * @return <code>value</code>との差
   */
  M subtract(M value);

  /**
   * 各成分と整数<code>value</code>の積を(<code>this</code>*<code>value</code>)を返します。
   * 
   * @param value 整数
   * @return 各成分と整数<code>value</code>の積
   */
  M multiply(int value);

  /**
   * 各成分と実数<code>value</code>の積を(<code>this</code>*<code>value</code>)を返します。
   * 
   * @param value 実数
   * @return 各成分と実数<code>value</code>の積
   */
  M multiply(double value);

  /**
   * 各成分とスカラー<code>value</code>の積を返します。
   * 
   * @param value スカラー
   * @return 各成分と<code>value</code>の積
   */
  M multiply(S value);

  /**
   * 行列<code>value</code>との積(<code>this</code>*<code>value</code>)を返します。
   * 
   * @param value 行列
   * @return <code>value</code>との積
   */
  M multiply(M value);

  /**
   * 各成分と整数valueの商(<code>this</code>/<code>value</code>)を返します。
   * 
   * @param value 割る整数
   * @return 各成分と<code>value</code>との商
   */
  M divide(int value);

  /**
   * 各成分と実数valueの商(<code>this</code>/<code>value</code>)を返します。
   * 
   * @param value 割る実数
   * @return 各成分と<code>value</code>との商
   */
  M divide(double value);

  /**
   * 各成分とスカラーの商からなる行列を返します。
   * 
   * @param value スカラー
   * @return 各成分と<code>value</code>の商
   */
  M divide(S value);

  /**
   * 逆行列と整数<code>value</code>の積(<code>this</code> <sup>-1 </sup>*<code>value</code>)を返します。
   * 
   * @param value 割る整数
   * @return 逆行列とvalueとの積
   */
  M leftDivide(int value);

  /**
   * 逆行列と実数<code>value</code>の積(<code>this</code> <sup>-1 </sup>*<code>value</code>)を返します。
   * 
   * @param value 割る実数
   * @return 逆行列とvalueとの積
   */
  M leftDivide(double value);

  /**
   * 逆行列とスカラー<code>value</code>の積(<code>this</code> <sup>-1 </sup>*<code>value</code>)を返します。
   * 
   * @param value スカラー
   * @return 逆行列と<code>value</code>の積
   */
  M leftDivide(S value);
 
  /**
   * 行列<code>value</code>の逆行列との積(<code>this</code>*value <sup>-1 </sup>)を返します。
   * 
   * @param value 割る行列
   * @return <code>value</code>の逆行列との積
   */
  M divide(M value);
  
  /**
   * 逆行列と行列<code>value</code>の積(<code>this</code> <sup>-1 </sup>*<code>value</code>)を返します。
   * 
   * @param value 割る行列
   * @return 逆行列と<code>value</code>との積
   */
  M leftDivide(M value);

  /**
   * 逆行列(<code>this</code> <sup>-1 </sup>)を返します。
   * 
   * @return 逆行列
   */
  M inverse();

  /**
   * 逆行列(<code>this</code> <sup>-1 </sup>)を返します。
   * 
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return 逆行列
   */
  M inverse(double tolerance, boolean stopIfSingular);

  /**
   * 符号を反転した値(-this)を返します。
   * 
   * @return 符号を反転した値
   */
  M unaryMinus();

  /**
   * 共役複素行列を返します。
   * 
   * @return 共役複素行列
   */
  M conjugate();

  /**
   * 各成分の共役複素数の転置行列を返します。
   * 
   * @return 複素共役転置
   */
  M conjugateTranspose();

  /**
   * 整数order乗(<code>this</code> <sup>order </sup>)を返します。
   * 
   * @param order 指数
   * @return order乗
   */
  M power(int order);

  /**
   * 零行列であるか判定します。
   * 
   * @param tolerance 許容誤差
   * @return 零行列ならばtrue、そうでなければfalse
   */
  boolean isZero(double tolerance);

  //  /**
  //   * 零行列であるか判定します。
  //   * 
  //   * @return 零行列ならtrue、そうでなければfalse
  //   */
  //  boolean isZero();

  /**
   * 単位行列であるか判定します。
   * 
   * @return 単位行列ならばtrue、そうでなければfalse
   */
  boolean isUnit();

  /**
   * 単位行列であるか判定します。
   * 
   * @param tolerance 許容誤差
   * @return 単位行列ならばtrue、そうでなければfalse
   */
  boolean isUnit(double tolerance);

  /**
   * 実成分のみをもつか(複素成分をもたないか)判定します。
   * 
   * @return 実成分のみをもつならばtrue、そうでなければfalse
   */
  boolean isReal();

  /**
   * 複素成分をもつか判定します。
   * 
   * @return 複素成分をもつならばtrue、そうでなければfalse
   */
  boolean isComplex();

  /**
   * 同サイズの零行列を生成します。
   * 
   * @return 零行列
   */
  M createZero();

  /**
   * <code>size</code>*<code>size</code>の零行列を生成します。
   * 
   * @param size サイズ指定
   * @return size*sizeの零行列
   */
  M createZero(final int size);

  /**
   * <code>rowSize</code>*<code>columnSize</code>の零行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return <code>rowSize</code>*<code>columnSize</code>の零行列
   */
  M createZero(final int rowSize, final int columnSize);

  /**
   * 行列<code>block</code>の<code>rowNumber</code>*<code>columnNumber</code> 倍の零行列を生成します。
   * 
   * @param rowNumber 行方向の倍数
   * @param columnNumber 列方向の倍数
   * @param block 基本となる行列
   * @return <code>block</code>の<code>rowNumber</code>* <code>columnNumber</code>倍の零行列
   */
  M createZero(final int rowNumber, final int columnNumber, final Grid block);

  /**
   * 同サイズの単位行列を生成します。
   * 
   * @return 単位行列
   */
  M createUnit();

  /**
   * <code>size</code>*<code>size</code>の単位行列を生成します。
   * 
   * @param size サイズ指定
   * @return size*sizeの単位行列
   */
  M createUnit(final int size);

  /**
   * <code>rowSize</code>*<code>columnSize</code>の単位行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return <code>rowSize</code>*<code>columnSize</code>の単位行列
   */
  M createUnit(final int rowSize, final int columnSize);

  /**
   * 行列<code>block</code>の<code>rowNumber</code>*<code>columnNumber</code> 倍の単位行列を生成します。
   * 
   * @param rowNumber 行方向の倍数
   * @param columnNumber 列方向の倍数
   * @param block 基本となる行列
   * @return <code>block</code>の<code>rowNumber</code>* <code>columnNumber</code>倍の単位行列
   */
  M createUnit(final int rowNumber, final int columnNumber, final Grid block);

  /**
   * 同サイズの全成分が1である行列を生成します。
   * 
   * @return 全成分が1である行列
   */
  M createOnes();

  /**
   * <code>size</code>*<code>size</code>の全成分が1である行列を生成します。
   * 
   * @param size サイズ指定
   * @return size*sizeの全成分が1である行列
   */
  M createOnes(final int size);

  /**
   * <code>rowSize</code>*<code>columnSize</code>の全成分が1である行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return <code>rowSize</code>*<code>columnSize</code>の全成分が1である行列
   */
  M createOnes(final int rowSize, final int columnSize);

  /**
   * 行列<code>block</code>の<code>rowNumber</code>*<code>columnNumber</code> 倍の全成分が1である行列を生成します。
   * 
   * @param rowNumber 行方向の倍数
   * @param columnNumber 列方向の倍数
   * @param block 基本となる行列
   * @return <code>block</code>の<code>rowNumber</code>* <code>columnNumber</code>倍の全成分が1である行列
   */
  M createOnes(final int rowNumber, final int columnNumber, final Grid block);

  /**
   * 各要の累積和からなる行列を返します。
   * 
   * @return 累積和行列
   */
  M cumulativeSum();

  /**
   * 各要の累積積からなる行列を返します。
   * 
   * @return 累積積行列
   */
  M cumulativeProduct();

  /**
   * 行毎に加えた列ベクトルを返します。
   * 
   * @return 行毎加算列ベクトル
   */
  M sumRowWise();

  /**
   * 列毎に加えた行ベクトルを返します。
   * 
   * @return 列毎加算行ベクトル
   */
  M sumColumnWise();

  /**
   * 行毎に掛けた列ベクトルを返します。
   * 
   * @return 行毎積列ベクトル
   */
  M productRowWise();

  /**
   * 列毎に掛けた行ベクトルを返します。
   * 
   * @return 列毎積行ベクトル
   */
  M productColumnWise();

  /**
   * 行毎の累積和行列を返します。
   * 
   * @return 行毎の累積和行列
   */
  M cumulativeSumRowWise();

  /**
   * 列毎の累積和行列を返します。
   * 
   * @return 列毎の累積和行列
   */
  M cumulativeSumColumnWise();

  /**
   * 行毎の累積積行列を返します。
   * 
   * @return 行毎の累積積行列
   */
  M cumulativeProductRowWise();

  /**
   * 列毎の累積積行列を返します。
   * 
   * @return 列毎の累積積行列
   */
  M cumulativeProductColumnWise();

  /**
   * 各成分行毎の平均値列ベクトルを返します。
   * 
   * @return 行毎平均値ベクトル
   */
  M meanRowWise();

  /**
   * 各成分列毎の平均値行ベクトルを返します。
   * 
   * @return 列毎行平均値ベクトル
   */
  M meanColumnWise();

  /**
   * <code>opponent</code>との共分散行列を返します。
   * 
   * @param value 対となるベクトル
   * @return 共分散行列 (Covariance)
   */
  M covariance(M value);

//  /**
//   * 実部行列を返します。
//   * 
//   * @return 実部行列
//   */
//  Matrix<?,?> getRealPart();
//
//  /**
//   * 虚部行列を返します。
//   * 
//   * @return 虚部行列
//   */
//  Matrix<?,?> getImaginaryPart();
//
//  /**
//   * 実部行列を設定します。
//   * 
//   * @param realPart 実部行列
//   */
//  void setRealPart(final Matrix<?,?> realPart);
//
//  /**
//   * 実部行列を設定します。
//   * 
//   * @param realPart 実部行列
//   */
//  void setRealPart(final IntMatrix realPart);
//
//  /**
//   * 実部行列を設定します。
//   * 
//   * @param realPart 実部行列
//   */
//  void setRealPart(final DoubleMatrix realPart);
//
//  /**
//   * 実部行列を設定します。
//   * 
//   * @param realPart 実部行列
//   */
//  void setRealPart(final BaseMatrix<?, ?> realPart);
//
//  /**
//   * 虚部行列を設定します。
//   * 
//   * @param imaginaryPart 虚部行列
//   */
//  void setImaginaryPart(final Matrix<?,?> imaginaryPart);
//
//  /**
//   * 虚部行列を設定します。
//   * 
//   * @param imaginaryPart 虚部行列
//   */
//  void setImaginaryPart(final IntMatrix imaginaryPart);
//
//  /**
//   * 虚部行列を設定します。
//   * 
//   * @param imaginaryPart 虚部行列
//   */
//  void setImaginaryPart(final DoubleMatrix imaginaryPart);
//
//  /**
//   * 虚部行列を設定します。
//   * 
//   * @param imaginaryPart 虚部行列
//   */
//  void setImaginaryPart(final BaseMatrix<?, ?> imaginaryPart);

  /**
   * 全ての成分を上方向へシフトします。 上側には零が代入されます。numberが負の場合、下方向へシフトします。
   * 
   * @param number シフトで進む数
   * @return 全ての成分を上方向へシフトした行列
   */
  M shiftUp(final int number);

  /**
   * 全ての成分を左方向へシフトします。 右側には零が代入されます。numberが負の場合、右方向へシフトします。
   * 
   * @param number シフトで進む数
   * @return 全ての成分を左方向へシフトした行列
   */
  M shiftLeft(final int number);

//  /**
//   * 複素成分行列へ変換します。
//   * 
//   * @return 複素成分行列
//   */
//  Matrix<?,?> toComplex();

//  /**
//   * 引数で与えられた型から、この型へ変換可能か判定します。
//   * 
//   * @param value 変換元
//   * @return 変換可能ならtrue、そうでなければfalse
//   */
//  boolean isTransformableFrom(Matrix<?,?> value);

//  /**
//   * 引数で与えられた型からこの型へ変換します。
//   * 
//   * @param value 変換元
//   * @return 変換で生成された値
//   */
//  M transformFrom(Matrix<?,?> value);

//  /**
//   * この型から引数で与えられた型へ変換可能か判定します。
//   * 
//   * @param value 変換先
//   * @return 変換可能ならtrue、そうでなければfalse
//   */
//  boolean isTransformableTo(Matrix<?,?> value);

//  /**
//   * この型から引数で与えられた型へ変換します。
//   * 
//   * @param value 変換先
//   * @return 変換で生成された値
//   */
//  Matrix<?,?> transformTo(Matrix<?,?> value);
}