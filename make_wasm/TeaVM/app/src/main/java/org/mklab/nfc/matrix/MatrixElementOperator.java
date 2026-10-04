/*
 * Created on 2008/01/27
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matrix;

import org.mklab.nfc.scalar.Scalar;


/**
 * 行列の成分に関する演算を表わすインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.5 $, 2008/01/27
 * @param <S> スカラーの型
 * @param <M> 行列の型
  */
public interface MatrixElementOperator<S extends Scalar<S,M>, M extends Matrix<S,M>> {

  /**
   * <code>row</code>行<code>column</code>列の成分を返します。
   * 
   * @param row 行番号(1から始まる)
   * @param column 列番号(1から始まる)
   * @return row行column列の成分
   */
  S getElement(int row, int column);

  /**
   * 成分を行毎に数え<code>index</code>で指定した成分を返します。
   * 
   * @param index 成分の番号(1から始まる)
   * 
   * @return 指定された成分
   */
  S getElement(int index);

  /**
   * 指定した成分に<code>value</code>を代入します。
   * 
   * @param row 行番号(1から始まる)
   * 
   * @param column 列番号(1から始まる)
   * 
   * @param value 代入する値
   * 
   */
  void setElement(int row, int column, int value);

  /**
   * 指定した成分に<code>value</code>を代入します。
   * 
   * @param row 行番号(1から始まる)
   * 
   * @param column 列番号(1から始まる)
   * 
   * @param value 代入する値
   * 
   */
  void setElement(int row, int column, double value);

  /**
   * 指定した成分に<code>value</code>を代入します。
   * 
   * @param row 行番号(1から始まる)
   * 
   * @param column 列番号(1から始まる)
   * 
   * @param value 代入する値
   * 
   */
  void setElement(int row, int column, S value);

  /**
   * 成分を行毎に数え<code>index</code>で指定した位置に<code>value</code>を代入します。
   * 
   * @param index 成分の番号(1から始まる)
   * 
   * @param value 代入する値
   * 
   */
  void setElement(int index, int value);

  /**
   * 成分を行毎に数え<code>index</code>で指定した位置に<code>value</code>を代入します。
   * 
   * @param index 成分の番号(1から始まる)
   * 
   * @param value 代入する値
   * 
   */
  void setElement(int index, double value);

  /**
   * 成分を行毎に数え<code>index</code>で指定した位置に<code>value</code>を代入します。
   * 
   * @param index 成分の番号(1から始まる)
   * 
   * @param value 代入する値
   * 
   */
  void setElement(int index, S value);

  /**
   * 全ての成分の合計を返します。
   * 
   * @return 全ての成分の合計
   */
  S sum();

  /**
   * 全ての成分の平均値を返します。
   * 
   * @return 全ての成分の平均値
   */
  S mean();

  /**
   * 全ての成分の分散を返します。
   * 
   * @return 分散
   */
  S variance();

  /**
   * 全対角成分の和を返します。
   * 
   * @return 対角成分の合計
   */
  S trace();

  /**
   * 全ての成分積を返します。
   * 
   * @return 全ての成分積
   */
  S product();

  /**
   * 行列式を返します。
   * 
   * @return 行列式
   */
  S determinant();

///**
//* 行列<code>source</code>の成分をコピーします。
//* 
//* @param source コピー元の整数行列
//*/
//  void copy(IntMatrix source);
//
//  /**
//   * 行列<code>source</code>の成分をコピーします。
//   * 
//   * @param source コピー元の実数行列
//   */
//  void copy(DoubleMatrix source);
  
  
  /**
   * 行列を1行の文字列に変換します。
   * 
   * @param format 成分の出力フォーマット
   * @return 1行の文字列
   */
  String toString(String format);
}
