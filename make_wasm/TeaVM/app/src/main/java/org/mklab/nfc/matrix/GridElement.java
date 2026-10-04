/*
 * $Id: GridElement.java,v 1.25 2008/01/14 14:52:13 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

/**
 * グリッド(格子状)データの成分を表すインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.25 $
 * @param <S> スカラーの型
 */
public interface GridElement<S extends GridElement<S>> {

  /**
   * 複製を生成します。
   * 
   * @return 生成した複製
   */
  S clone();

  /**
   * 文字列に変換します。
   * 
   * @return 変換結果の文字列
   */
  String toString();

  /**
   * 文字列に変換します。
   * 
   * @param valueFormat 値のフォーマット
   * @return 変換結果の文字列
   */
  String toString(String valueFormat);

  /**
   * 零(デフォルトの初期値)を生成します。
   * 
   * @return 零(デフォルトの初期値)
   */
  S createZero();

  /**
   * 零(デフォルトの初期値)であるか判定します。
   * 
   * @return 零(デフォルトの初期値)ならばtrue、そうでなければfalse
   */
  boolean isZero();

  /**
   * <code>opponent</code>を<code>operator</code>で指定された演算子で比較します。
   * 
   * @param operator 比較演算子 (".==", ".!=")
   * @param opponent 比較対象
   * 
   * @return 比較式が正しければtrue、そうでなければfalse
   */
  boolean compare(String operator, S opponent);

//  /**
//   * 引数で与えられた型からこの型へ変換します。
//   * 
//   * @param value 変換元
//   * @return 変換で生成された値
//   */
//  S transformFrom(GridElement<? extends GridElement<?>> value);
//
//  /**
//   * この型から引数で与えられた型へ変換します。
//   * 
//   * @param value 変換先
//   * @return 変換で生成された値
//   */
//  GridElement<?> transformTo(GridElement<? extends GridElement<?>> value);

//  /**
//   * 引数で与えられた型からこの型へ変換可能か判定します。
//   * 
//   * @param value 変換元
//   * @return 変換可能ならtrue、そうでなければfalse
//   */
//  boolean isTransformableFrom(GridElement<? extends GridElement<?>> value);

//  /**
//   * この型から引数で与えられた型へ変換可能か判定します。
//   * 
//   * @param value 変換先
//   * @return 変換可能ならtrue、そうでなければfalse
//   */
//  boolean isTransformableTo(GridElement<? extends GridElement<?>> value);

  /**
   * グリッドの一次元配列を生成します。
   * 
   * @param size 成分の数
   * @return グリッドの一次元配列
   */
  S[] createArray(int size);

//  /**
//   * グリッドの一次元配列を生成します。
//   * 
//   * @param elements 成分
//   * @return グリッドの一次元配列
//   */
//  S[] createArray(GridElement<?>[] elements);

  /**
   * グリッドの2次元配列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return グリッドの2次元配列
   */
  S[][] createArray(int rowSize, int columnSize);

//  /**
//   * グリッドの2次元配列を生成します。
//   * 
//   * @param elements 成分
//   * @return グリッドの2次元配列
//   */
//  S[][] createArray(GridElement<?>[][] elements);
}
