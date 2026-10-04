/*
                             * $Id: Scalar.java,v 1.4 2008/03/15 00:36:44 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.scalar;

import org.mklab.nfc.matrix.GridElement;
import org.mklab.nfc.matrix.Matrix;


/**
 * スカラーを表すインターフェースです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.4 $, 2004/07/05
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public interface Scalar<S extends Scalar<S,M>, M extends Matrix<S,M>> extends GridElement<S>, RoundableToInteger<S>{

  /**
   * 行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 行列の成分をもつ配列
   * @return 生成した行列
   */
  M createGrid(int rowSize, int columnSize, S[][] elements);

  /**
   * 行列を生成します。
   * 
   * @param elements 行列の成分をもつ配列
   * @return 生成した行列
   */
  M createGrid(S[][] elements);

  /**
   * 行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 行列の成分をもつ配列
   * @return 生成した行列
   */
  M createGrid(int rowSize, int columnSize, int[][] elements);

  /**
   * 行列を生成します。
   * 
   * @param elements 行列の成分をもつ配列
   * @return 生成した行列
   */
  M createGrid(int[][] elements);

  /**
   * 行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 行列の成分をもつ配列
   * @return 生成した行列
   */
  M createGrid(int rowSize, int columnSize, double[][] elements);

  /**
   * 行列を生成します。
   * 
   * @param elements 行列の成分をもつ配列
   * @return 生成した行列
   */
  M createGrid(double[][] elements);

  /**
   * 零行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 生成した零行列
   */
  M createZeroGrid(int rowSize, int columnSize);

  /**
   * 単位行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 生成した単位行列
   */
  M createUnitGrid(int rowSize, int columnSize);

  /**
   * 単位行列を生成します。
   * 
   * @param size 行列の次数
   * @return 生成した単位行列
   */
  M createUnitGrid(int size);
  
  /**
   * 全ての成分が１である行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 生成した全ての成分が１である行列
   */
  M createOnesGrid(int rowSize, int columnSize);

  /**
   * ベクトルを生成します。
   * 
   * @param elements ベクトルの成分をもつ配列
   * @return 生成したベクトル
   */
  M createGrid(S[] elements);

  /**
   * ベクトルを生成します。
   * 
   * @param elements ベクトルの成分をもつ配列
   * @return 生成したベクトル
   */
  M createGrid(int[] elements);

  /**
   * ベクトルを生成します。
   * 
   * @param elements ベクトルの成分をもつ配列
   * @return 生成したベクトル
   */
  M createGrid(double[] elements);

  /**
   * 零行列を生成します。
   * 
   * @param size 行列の次数
   * @return 生成した零行列
   */
  M createZeroGrid(int size);

  /**
   * 全ての成分が１である行列を生成します。
   * 
   * @param size 行列の次数
   * @return 生成した全ての成分が１である行列
   */
  M createOnesGrid(int size);

  /**
   * 値を加えた成分を生成します。
   * 
   * @param value 加える値
   * @return 足し算の結果
   */
  S add(S value);

  /**
   * 値を引きます。
   * 
   * @param value 引く値
   * @return 引き算の結果
   */
  S subtract(S value);

  /**
   * 値を掛けます。
   * 
   * @param value 掛ける値
   * @return 掛け算の結果
   */
  S multiply(S value);

  /**
   * 値で割ります。
   * 
   * @param value 割る値
   * @return 割り算の結果
   */
  S divide(S value);

  /**
   * 値を割ります。
   * 
   * @param value 割られる値
   * @return 割り算の結果
   */
  S leftDivide(S value);

  /**
   * 値を加えます。
   * 
   * @param value 加える値
   * @return 足し算の結果
   */
  S add(int value);

  /**
   * 値を引く。
   * 
   * @param value 引く値
   * @return 引き算の結果
   */
  S subtract(int value);

  /**
   * 値を掛けます。
   * 
   * @param value 掛ける値
   * @return 掛け算の結果
   */
  S multiply(int value);

  /**
   * 値で割ります。
   * 
   * @param value 割る値
   * @return 割り算の結果
   */
  S divide(int value);

  /**
   * 値を割ります。
   * 
   * @param value 割られる値
   * @return 割り算の結果
   */
  S leftDivide(int value);

  /**
   * 値を加えます。
   * 
   * @param value 加える値
   * @return 足し算の結果
   */
  S add(double value);

  /**
   * 値を引く。
   * 
   * @param value 引く値
   * @return 引き算の結果
   */
  S subtract(double value);

  /**
   * 値を掛けます。
   * 
   * @param value 掛ける値
   * @return 掛け算の結果
   */
  S multiply(double value);

  /**
   * 値で割ります。
   * 
   * @param value 割る値
   * @return 割り算の結果
   */
  S divide(double value);

  /**
   * 値を割ります。
   * 
   * @param value 割られる値
   * @return 割り算の結果
   */
  S leftDivide(double value);

  /**
   * 逆数を求めます。
   * 
   * @return 逆数
   */
  S inverse();

  /**
   * 共役数を返します。
   * 
   * @return 共役数
   */
  S conjugate();

  /**
   * 符号を反転した値を返します。
   * 
   * @return 符号を反転した値
   */
  S unaryMinus();

  /**
   * 累乗を返します。
   * 
   * @param scalar 指数
   * @return 累乗
   */
  S power(int scalar);

  /**
   * 単位成分を生成します。
   * 
   * @return 単位成分
   */
  S createUnit();

  /**
   * 与えられたint型に対応する値を返します。
   * 
   * @param value int型の値
   * @return 与えられたint型に対応する値
   */
  S create(int value);

  /**
   * 与えられたdouble型に対応する値を返します。
   * 
   * @param value int型の値
   * @return 与えられたint型に対応する値
   */
  S create(double value);

  /**
   * 許容範囲内で等しいか判定します。
   * 
   * @param opponent 比較する値
   * @param tolerance 許容誤差
   * @return 等しければ(差の絶対値がtolerance以下)true、そうでなければfalse
   */
  boolean equals(S opponent, double tolerance);

  /**
   * <code>opponent</code>を<code>operator</code>で指定された演算子で比較します。
   * 
   * @param operator 比較演算子(".==", ".!=")
   * @param opponent 比較対象
   * 
   * @return 比較式が正しければtrue、そうでなければfalse
   */
  boolean compare(String operator, int opponent);

  /**
   * <code>opponent</code>を<code>operator</code>で指定された演算子で比較します。
   * 
   * @param operator 比較演算子 (".==", ".!=")
   * @param opponent 比較対象
   * 
   * @return 比較式が正しければtrue、そうでなければfalse
   */
  boolean compare(String operator, double opponent);

  /**
   * 零であるか判定します。
   * 
   * @param tolerance 許容誤差
   * @return 零(大きさがtolerance以下)ならばtrue、そうでなければfalse
   */
  boolean isZero(double tolerance);

  /**
   * １(単位元)であるか判定します。
   * 
   * @return １(単位元)ならばtrue、そうでなければfalse
   */
  boolean isUnit();

  /**
   * 1(単位元)であるか判定します。
   * 
   * @param tolerance 許容誤差
   * @return 1(単位元)(1との差の絶対値がtolerance以下)ならばtrue、そうでなければfalse
   */
  boolean isUnit(double tolerance);

  /**
   * NaNであるか判定します。
   * 
   * @return NaNであればtrue、そうでなければfalse
   */
  boolean isNaN();

  /**
   * 有限であるか(無限大でなく、NaNでない)判定します。
   * 
   * @return 有限(無限大でなく、NaNでない)であればtrue、そうでなければfalse
   */
  boolean isFinite();

  /**
   * 無限大であるか判定します。
   * 
   * @return 無限大ならばtrue、そうでなければfalse
   */
  boolean isInfinite();

  /**
   * 実数であるか判定します。
   * 
   * @return 実数ならばtrue、そうでなければ false
   */
  boolean isReal();

  /**
   * 複素数であるか判定します。
   * 
   * @return 複素数ならばtrue、そうでなければfalse
   */
  boolean isComplex();

//  /**
//   * 整数から成分を生成します。
//   * 
//   * @param value 整数
//   * @return 整数から生成された成分
//   */
//  S transformFrom(int value);
//
//  /**
//   * 実数から成分を生成します。
//   * 
//   * @param value 実数
//   * @return 実数から生成された成分
//   */
//  S transformFrom(double value);

//  /**
//   * 足し算のオペレータを返します。
//   * 
//   * @return 足し算のオペレータ
//   */
//  ScalarOperator getAddOperator();
//
//  /**
//   * 引き算のオペレータを返します。
//   * 
//   * @return 引き算のオペレータ
//   */
//  ScalarOperator getSubtractOperator();
//
//  /**
//   * 掛け算のオペレータを返します。
//   * 
//   * @return 掛け算のオペレータ
//   */
//  ScalarOperator getMultiplyOperator();
//
//  /**
//   * 割り算のオペレータを返します。
//   * 
//   * @return 割り算のオペレータ
//   */
//  ScalarOperator getDivideOperator();
//
//  /**
//   * 左からの割り算のオペレータを返します。
//   * 
//   * @return 左からの割り算のオペレータ
//   */
//  ScalarOperator getLeftDivideOperator();
//
//  /**
//   * 等号オペレータを返します。
//   * 
//   * @return 等号オペレータ
//   */
//  ScalarEqual getEqualOperator();

//  /**
//   * 実部を返します。
//   * 
//   * @return 実部
//   */
//  Scalar<?> getRealPart();
//
///**
//* 虚部を返します。
//* 
//* @return 虚部
//*/
//  Scalar<?> getImaginaryPart();
//
///**
//* 実部を設定します。
//* 
//* @param realPart 実部
//*/
//  void setRealPart(int realPart);
//
//  /**
//   * 実部を設定します。
//   * 
//   * @param realPart 実部
//   */
//  void setRealPart(double realPart);
//
//  /**
//   * 実部を設定します。
//   * 
//   * @param realPart 実部
//   */
//  void setRealPart(Scalar<?> realPart);
//
//  /**
//   * 虚部を設定します。
//   * 
//   * @param imagPart 虚部
//   */
//  void setImaginaryPart(int imagPart);
//
//  /**
//   * 虚部を設定します。
//   * 
//   * @param imagPart 虚部
//   */
//  void setImaginaryPart(double imagPart);
//
///**
//* 虚部を設定します。
//* 
//* @param imagPart 虚部
//*/
//  void setImaginaryPart(Scalar<?> imagPart);

//  /**
//   * 複素成分に変換します。
//   * 
//   * @return 複素成分
//   */
//  Scalar<?> toComplex();

  /**
   * 出力フォーマットを設定します。
   * 
   * @param format 出力フォーマット
   */
  void setFormat(String format);

  /**
   * 出力フォーマットを返します。
   * 
   * @return 出力フォーマット
   */
  String getFormat();
}
