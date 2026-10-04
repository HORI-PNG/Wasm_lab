/*
 * $Id: BaseArrayOperator.java,v 1.1 2008/01/14 15:06:28 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.matrix;

/**
 * 配列データをを表わすインターフェースです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.1 $, 2004/07/05
 * @param <M> 行列の型
 * @param <S> スカラーの型
 */
public interface FundamentalArray<S extends ArrayElement<S,M>,M extends Array<M>> extends Array<M> {

  /**
   * 各成分に配列<code>source</code>各成分をコピーします。
   * 
   * @param source コピー元の配列
   */
  void copy(M source);

  /**
   * 配列<code>opponent</code>の各成分と成分毎に<code>operator</code>で指定された演算子で比較し, それぞれの結果を成分とする{@link BooleanMatrix}を返します。
   * 
   * @param operator 比較演算子(".==", ".!=")
   * @param opponent 比較対象
   * 
   * @return 比較結果を成分とするBooleanMatrix
   */
  BooleanMatrix compareElementWise(String operator, M opponent);

//  /**
//   * 配列<code>source</code>の<code>rowMin</code>行から<code>rowMax行</code>、 <code>columnMin</code>列から<code>columnMax</code>列までの部分配列を、 <code>rowTo</code>行<code>columnTo</code>列を始点として代入します。
//   * 
//   * @param rowTo 代入開始行
//   * @param columnTo 代入開始列
//   * @param source 代入する配列
//   * @param rowMinimum コピー開始行
//   * @param rowMaximum コピー終了行
//   * @param columnMinimum コピー開始列
//   * @param columnMaximum コピー終了列
//   */
//  void setSubMatrix(int rowTo, int columnTo, FundamentalArray<?, ?> source, int rowMinimum, int rowMaximum, int columnMinimum, int columnMaximum);

  /**
   * 指定した成分に行列<code>source</code>を代入します。
   * 
   * @param rowMinimum 開始行番号(1から始まります)
   * @param rowMaximum 終了行番号(1から始まります)
   * @param columnMinimum 開始列番号(1から始まります)
   * @param columnMaximum 終了列番号(1から始まります)
   * @param source 代入する行列
   */
  void setSubMatrix(int rowMinimum, int rowMaximum, int columnMinimum, int columnMaximum, M source);
  
  /**
   * <code>rowIndex</code>で指定した行の<code>columnMin</code>列から<code>columnMax</code> 列までの 配列<code>source</code>を代入します。
   * 
   * @param rowIndex 列指定
   * @param columnMin 行指定
   * @param columnMax 行指定
   * @param source 行列
   */
  void setSubMatrix(IntMatrix rowIndex, int columnMin, int columnMax, M source);

  /**
   * <code>rowMin</code>列から<code>rowMax</code>列目の成分の<code>columnIndex</code> で指定された行に、 配列<code>source</code>の成分を代入します。
   * 
   * @param rowMin 行指定
   * @param rowMax 行指定
   * @param columnIndex 列指定ベクトル
   * @param source 代入する配列
   */
  void setSubMatrix(int rowMin, int rowMax, IntMatrix columnIndex, M source);

  /**
   * <code>rowIndex</code>で指定した行の<code>columnIndex</code>で指定した列に配列<code>source</code>を代入します。
   * 
   * @param rowIndex 行指定
   * @param columnIndex 列指定
   * @param source 配列
   */
  void setSubMatrix(IntMatrix rowIndex, IntMatrix columnIndex, M source);

  /**
   * <code>index</code>で指定した各成分に配列<code>source</code>の成分を代入します。
   * 
   * @param index 成分指定
   * @param source 代入する部分ベクトル
   * 
   */
  void setSubVector(IntMatrix index, M source);

  /**
   * <code>row</code>行<code>column</code>列の成分を返します。
   * 
   * @param row 行番号
   * @param column 列番号
   * @return row行column列の成分
   */
  S getElement(int row, int column);

  /**
   * 成分を行毎に数え<code>index</code>で指定した成分を返します。
   * 
   * @param index 成分の番号
   * 
   * @return 指定された成分
   */
  S getElement(int index);

  /**
   * 指定した成分に<code>value</code>を代入します。
   * 
   * @param row 行番号
   * 
   * @param column 列番号
   * 
   * @param value 代入する値
   * 
   */
  void setElement(int row, int column, S value);
}
