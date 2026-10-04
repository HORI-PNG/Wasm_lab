/**
 * $Id: Grid.java,v 1.47 2008/01/16 23:27:02 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

import java.io.Writer;


/**
 * グリッド(格子状)データを表わすインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.47 $
 */
public interface Grid {
  /**
   * 零行列(全ての成分がデフォルトの初期値)であるか判定します。
   * 
   * @return 零行列(全ての成分がデフォルトの初期値)ならばtrue、そうでなければfalse
   */
  boolean isZero();

  /**
   * 行の数を返します。
   * 
   * @return 行の数
   */
  int getRowSize();

  /**
   * 列の数を返します。
   * 
   * @return 列の数
   */
  int getColumnSize();

  /**
   * 行と列の長い方の数を返します。
   * 
   * @return 行と列の長い方の数
   */
  int length();

  /**
   * 成分の個数を返します。
   * 
   * @return 成分の個数
   */
  int count();

  /**
   * 0*0の行列(空行列)であるか判定します。
   * 
   * @return 空行列ならばtrue、そうでなければfalse
   */
  boolean isEmpty();

  /**
   * 正方(行の数と列の数が等しい)か判定します。
   * 
   * @return 正方(行の数と列の数が等しい)ならばtrue、そうでなければfalse
   */
  boolean isSquare();

  /**
   * 同一サイズであるか判定します。
   * 
   * @param opponent 比較対象
   * @return 同一サイズならばtrue、そうでなければfalse
   */
  boolean isSameSize(Grid opponent);

  /**
   * 行の数が等しいか判定します。
   * 
   * @param opponent 比較対象
   * @return 行の数が等しければtrue、そうでなければfalse
   */
  boolean hasSameRowSize(Grid opponent);

  /**
   * 列の数が等しいか判定します。
   * 
   * @param opponent 比較対象
   * @return 列の数が等しければtrue、そうでなければfalse
   */
  boolean hasSameColumnSize(Grid opponent);

  /**
   * <code>row1</code>行と<code>row2</code>行を入れ替えます。
   * 
   * @param row1 行番号1
   * @param row2 行番号1
   */
  void exchangeRow(int row1, int row2);

  /**
   * <code>column1</code>列と<code>column2</code>列を入れ替えます。
   * 
   * @param column1 列番号1
   * @param column2 列番号2
   */
  void exchangeColumn(int column1, int column2);

  /**
   * <code>rowMin</code>行から<code>rowMax</code>行までを削除します。
   * 
   * @param rowMinimum 行の始まり
   * @param rowMaximum 行の終わり
   */
  void removeRowVectors(int rowMinimum, int rowMaximum);

  /**
   * <code>rowIndex</code>で指定された行を削除します。
   * 
   * @param rowIndex 行指定ベクトル
   */
  void removeRowVectors(IntMatrix rowIndex);

  /**
   * <code>columnMin</code>列から<code>columnMax</code>列までを削除します。
   * 
   * @param columnMinimum 列の始まり
   * @param columnMaximum 列の終わり
   */
  void removeColumnVectors(int columnMinimum, int columnMaximum);

  /**
   * <code>columnIndex</code>で指定された列を削除します。
   * 
   * @param columnIndex 列指定ベクトル
   */
  void removeColumnVectors(IntMatrix columnIndex);

  /**
   * 指定された行を削除します。
   * 
   * @param index 指定行
   */
  void removeRowVector(int index);

  /**
   * 指定された列を削除します。
   * 
   * @param index 指定列
   */
  void removeColumnVector(int index);

  /**
   * 標準出力に"ans"という名前で出力します。
   */
  void print();

  /**
   * 名前を付けて標準出力に出力します。
   * 
   * @param name 名前
   */
  void print(String name);

  /**
   * ライターに出力します。
   * 
   * @param name 行列の名前
   * @param output ライター
   */
  void print(String name, Writer output);

  /**
   * ライターに成分を出力します。
   * 
   * @param output ライター
   */
  void printElements(Writer output);

  /**
   * ライターに成分を出力します。
   * 
   * @param output ライター
   * @param maxColumnSize 1行の出力する列の最大数
   */
  void printElements(Writer output, int maxColumnSize);

  /**
   * 表示文字列を返します。
   * 
   * @param name 名前
   * @return 表示文字列
   */
  String getPrintingString(String name);

  /**
   * 成分の表示文字列を返します。
   * 
   * @param maxColumnSize 列の数の最大値
   * @return 成分の表示文字列
   */
  String getPrintingElementsString(int maxColumnSize);

  /**
   * 成分の出力フォーマットを設定します。
   * 
   * @param format 成分の出力フォーマット
   */
  void setElementFormat(String format);

  /**
   * 成分の出力フォーマットを返します。
   * 
   * @return 成分の出力フォーマット
   */
  String getElementFormat();

  /**
   * 成分の出力配置を設定します。
   * 
   * @param alignment 成分の出力配置
   */
  void setElementAlignment(GridElementAlignment alignment);

  /**
   * 成分の出力配置を返します。
   * 
   * @return 成分の出力配置
   */
  GridElementAlignment getElementAlignment();
}
