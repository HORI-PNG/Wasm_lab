/**
 * $Id: FundamentalMatrix.java,v 1.2 2008/04/16 23:50:11 koga Exp $
 *
 * Copyright (C) 2004 Masanobu Koga. All rights reserved.
 */
package org.mklab.nfc.matrix;

import org.mklab.nfc.scalar.Scalar;

/**
 * 行列データを表わすインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.2 $
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public interface FundamentalMatrix<S extends Scalar<S,M>, M extends Matrix<S,M>> extends Grid {

  /**
   * 複製(クローン)を返します。
   * 
   * @return 複製(クローン)
   */
  M createClone(); 

  /**
   * 行列<code>opponent</code>と値が許容誤差以内で等しいか判定します。
   * 
   * @param opponent 比較する行列
   * @param tolerance 許容誤差
   * @return 等しければtrue、そうでなければfalse
   */
  boolean equals(M opponent, double tolerance);

  /**
   * <code>source</code>の成分をコピーします。
   *    * @param source コピー元の行列
   */
  void copy(M source);

  /**
   * 下側に行列<code>value</code>を付けた行列を生成します。
   * 
   * @param value 付ける行列
   * @return 下側に<code>value</code>をつけた行列
   */
  M appendDown(M value);

  /**
   * 右側に<code>value</code>を付けた行列を生成します。
   * 
   * @param value 付ける複素数
   * @return 右側に<code>value</code>を付けた行列
   */
  M appendRight(M value);

  /**
   * 転置行列(<code>this</code> <sup>T </sup>)を生成します。
   * 
   * @return 転置行列
   */
  M transpose();

  /**
   * 部分行列<code>this(rowMinimum:rowMaximum,columnMinimum:columnMaximum)</code>を生成します。
   * 
   * @param rowMinimum 開始行番号(1から始まります)
   * @param rowMaximum 終了行番号(1から始まります)
   * @param columnMinimum 開始列番号(1から始まります)
   * @param columnMaximum 終了列番号(1から始まります)
   * @return 自身の部分行列
   */
  M getSubMatrix(int rowMinimum, int rowMaximum, int columnMinimum, int columnMaximum);

  /**
   * <code>column</code>列ベクトルの<code>rowIndex</code>で指定された成分からなる縦ベクトルを生成します。
   * 
   * @param rowIndex 行指定ベクトル
   * @param column 列指定
   * @return 部分行列
   */
  M getSubMatrix(IntMatrix rowIndex, int column);

  /**
   * <code>columnMinimum</code>列から<code>columnMaximum</code>列まで、 <code>rowIndex</code>で指定された行を成分とする部分行列を生成します。
   * 
   * @param rowIndex 行指定ベクトル
   * @param columnMinimum 開始列番号(1から始まります)
   * @param columnMaximum 終了列番号(1から始まります)
   * @return 部分行列
   */
  M getSubMatrix(IntMatrix rowIndex, int columnMinimum, int columnMaximum);

  /**
   * <code>row</code>行ベクトルの<code>columnIndex</code>で指定された成分からなる横ベクトルを生成します。
   * 
   * @param row 行指定
   * @param columnIndex 列指定ベクトル
   * @return 部分行列
   */
  M getSubMatrix(int row, IntMatrix columnIndex);

  /**
   * <code>rowMinimum</code>行から<code>rowMaximum</code>行目で、<code>columnIndex</code> で指定された列を成分とする部分行列を生成します。
   * 
   * @param rowMinimum 開始行番号(1から始まります)
   * @param rowMaximum 終了行番号(1から始まります)
   * @param columnIndex 列指定ベクトル
   * @return 部分行列
   */
  M getSubMatrix(int rowMinimum, int rowMaximum, IntMatrix columnIndex);

  /**
   * <code>rowIndex</code>で指定された行で、<code>columnIndex</code> で指定された列を成分とする部分行列を生成します。
   * 
   * @param rowIndex 行指定ベクトル
   * @param columnIndex 列指定ベクトル
   * @return 部分行列
   */
  M getSubMatrix(IntMatrix rowIndex, IntMatrix columnIndex);

  /**
   * <code>block</code>のサイズで分割したときの<code>row</code>行<code>column</code> 列番目のブロック行列を返します。
   * 
   * @param row 行番号
   * @param column 列番号
   * @param block 基本ブロック行列
   * @return 部分行列
   */
  M getSubMatrix(int row, int column, Grid block);

  /**
   * 行毎に数え<code>index</code>で指定した成分を成分とする部分行列を生成します。
   * 
   * @param index 行番号を含むベクトル
   * @return 部分行列
   */
  M getSubVector(IntMatrix index);

  /**
   * 成分を行毎に数え、<code>minimum</code>から<code>maximum</code>までの<code>by</code> 飛び成分からなるベクトルを生成します。
   * 
   * @param minimum 開始位置(1から始まります)
   * @param maximum 終了位置(1から始まります)
   * @param by 飛ばす数
   * @return fromからtoまでの成分を持つ横ベクトル
   */
  M getSubVector(int minimum, int maximum, int by);

  /**
   * 成分を行毎に数え、<code>minimum</code>から<code>maximum</code>までの成分からなるベクトルを生成します。
   * 
   * @param minimum 開始位置(1から始まります)
   * @param maximum 終了位置(1から始まります)
   * @return fromからtoまでの成分を持つ横ベクトル
   */
  M getSubVector(int minimum, int maximum);

  /**
   * 指定された行を返します。
   * 
   * @param row 行番号(1から始まります)
   * @return 指定された行
   */
  M getRowVector(int row);

  /**
   * <code>minimum</code>行から<code>maximum</code>行までの部分行列を生成します。
   * 
   * @param minimum 開始行番号(1から始まります)
   * @param maximum 終了行番号(1から始まります)
   * @return 部分行列
   */
  M getRowVectors(int minimum, int maximum);

  /**
   * <code>index</code>で指定された行からなる部分行列を生成します。
   * 
   * @param index 取り出す行番号
   * @return 部分行列
   */
  M getRowVectors(IntMatrix index);

  /**
   * 指定された列を返します。
   * 
   * @param column 列番号
   * @return 指定された列
   */
  M getColumnVector(int column);

  /**
   * <code>minimum</code>列から<code>maximum</code>列までの部分行列を生成します。
   * 
   * @param minimum 開始列番号(1から始まります)
   * @param maximum 終了列番号(1から始まります)
   * @return 部分行列
   */
  M getColumnVectors(int minimum, int maximum);

  /**
   * <code>index</code>で指定された列からなる部分行列を生成します。
   * 
   * @param index 取り出す列番号
   * @return 部分行列
   */
  M getColumnVectors(IntMatrix index);

  /**
   * 対角成分をからなる列ベクトルを生成します。
   * 
   * @return 対角成分からなる列ベクトル
   */
  M diagonalToVector();

  /**
   * 列ベクトルまたは行ベクトルの各成分を対角成分に持つ行列を生成します。
   * 
   * @return 対角成分からなる縦ベクトル
   */
  M vectorToDiagonal();

  /**
   * サイズを<code>newRowSize</code>*<code>newColumnSize</code>に変更した行列を生成します。
   * 
   * <p>成分は、行方向の成分順に並べ替えられます。 <code>newRowSize</code> * <code>newColumnSize</code>個の成分を持っていないと、エラーになる。
   * 
   * @param newRowSize 行の数
   * @param newColumnSize 列の数
   * @return サイズ変更された行列
   */
  M reshape(int newRowSize, int newColumnSize);

  /**
   * <code>newRowSize</code>*<code>newColumnSize</code>にサイズ変更します。
   * 
   * <p>{@link #reshape}とは異なり、成分位置の変更はせず, 自身より大きなサイズに変更する時は、0で埋められ、 自身より小さなサイズに変更する時は余分な成分は切り取られます。
   * 
   * @param newRowSize 指定行の数
   * @param newColmunSize 指定列の数
   * @return サイズ変更後の行列
   */
  M resize(int newRowSize, int newColmunSize);

  /**
   * 左右の列を反転した行列を生成します。
   * 
   * @return 左右反転した行列
   */
  M flipLeftRight();

  /**
   * 上下の行を反転した行列を生成します。
   * 
   * @return 上下反転した行列
   */
  M flipUpDown();

  /**
   * 全ての成分を上方向へ回転します。numberが負の場合、下方向へ回転します。
   * 
   * @param number 回転で進む数
   * @return 全ての成分を上方向へ回転した行列
   */
  M rotateUp(final int number);

  /**
   * 全ての成分を左方向へ回転します。numberが負の場合、右方向へ回転します。
   * 
   * @param number 回転で進む数
   * @return 全ての成分を左方向へ回転した行列
   */
  M rotateLeft(final int number);

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
   * <code>rowIndex</code>で指定した行の<code>column</code>列に行列<code>source</code> を代入します。
   * 
   * @param rowIndex 行番号のリスト
   * @param column 列番号
   * @param source 代入する行列
   */
  void setSubMatrix(IntMatrix rowIndex, int column, M source);

  /**
   * <code>rowIndex</code>で指定した行の<code>columnMinimum</code>列から<code>columnMaximum</code> 列までの行列<code>source</code>を代入します。
   * 
   * @param rowIndex 行番号のリスト
   * @param columnMinimum 開始列番号(1から始まります)
   * @param columnMaximum 終了列番号(1から始まります)
   * @param source 代入する行列
   */
  void setSubMatrix(IntMatrix rowIndex, int columnMinimum, int columnMaximum, M source);

  /**
   * <code>row</code>列目の成分の<code>columnIndex</code>で指定された行の成分に行列<code>source</code>を代入します。
   * 
   * @param row 行番号
   * @param columnIndex 列番号のリスト
   * @param source 代入する行列
   */
  void setSubMatrix(int row, IntMatrix columnIndex, M source);

  /**
   * <code>rowMinimum</code>列目から<code>rowMaximum</code>列目の成分の<code>columnIndex</code> で指定された行の成分に行列<code>source</code>を代入します。
   * 
   * @param rowMinimum 開始行番号(1から始まります)
   * @param rowMaximum 終了行番号(1から始まります)
   * @param columnIndex 列番号のリスト
   * @param source 代入する行列
   */
  void setSubMatrix(int rowMinimum, int rowMaximum, IntMatrix columnIndex, M source);

  /**
   * <code>rowIndex</code>で指定した行の<code>columnIndex</code>で指定した列に行列<code>source</code>を代入します。
   * 
   * @param rowIndex 行番号のリスト
   * @param columnIndex 列番号のリスト
   * @param source 代入する行列
   */
  void setSubMatrix(IntMatrix rowIndex, IntMatrix columnIndex, M source);

  /**
   * 指定した成分に行列<code>source</code>を代入します。
   * 
   * @param row 行番号
   * @param column 列番号
   * @param block 基本ブロック行列
   * @param source 代入する行列
   */
  void setSubMatrix(int row, int column, Grid block, M source);

  /**
   * <code>index</code>で指定した各成分に行列<code>source</code>の成分を代入します。
   * 
   * @param index 成分の番号のリスト
   * @param source 代入する行列
   */
  void setSubVector(IntMatrix index, M source);

  /**
   * 成分を行毎に数え、<code>minimum</code>から<code>maximum</code>までに<code>source</code>の成分を代入します。
   * 
   * @param minimum 開始位置(1から始まります)
   * @param maximum 終了位置(1から始まります)
   * @param source 代入するベクトル
   */
  void setSubVector(int minimum, int maximum, M source);

  /**
   * 成分を行毎に数え、<code>minimum</code>から<code>maximum</code>までの成分を<code>by</code>飛びに代入します。
   * 
   * @param minimum 開始位置(1から始まります)
   * @param maximum 終了位置(1から始まります)
   * @param by 飛ばす数
   * @param source 代入するベクトル
   */
  void setSubVector(int minimum, int maximum, int by, M source);

  /**
   * 指定された行に<code>source</code>を代入します。
   * 
   * @param row 行番号
   * @param source 代入する行列
   */
  void setRowVector(int row, M source);

  /**
   * <code>minimum</code>行から<code>maximum</code>行に<code>source</code>を代入します。
   * 
   * @param minimum 開始行番号(1から始まります)
   * @param maximum 終了行番号(1から始まります)
   * @param source 代入する行列
   */
  void setRowVectors(int minimum, int maximum, M source);

  /**
   * 指定された複数の行に<code>source</code>を代入します。
   * 
   * @param index 行番号
   * @param source 代入する行列
   */
  void setRowVectors(IntMatrix index, M source);

  /**
   * 指定された列に<code>source</code>を代入します。
   * 
   * @param column 列番号
   * @param source 代入する行列
   */
  void setColumnVector(int column, M source);

  /**
   * <code>minimum</code>列から<code>maximum</code>列に<code>source</code>を代入します。
   * 
   * @param minimum 開始列番号(1から始まります)
   * @param maximum 終了列番号(1から始まります)
   * @param source 代入する行列
   */
  void setColumnVectors(int minimum, int maximum, M source);

  /**
   * 指定された列に<code>source</code>を代入します。
   * 
   * @param index 列番号
   * @param source 代入する行列
   */
  void setColumnVectors(IntMatrix index, M source);

  /**
   * <code>opponent</code>と成分毎に<code>operator</code>で指定された演算子で比較した結果を {@link BooleanMatrix}で返します。
   * 
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param opponent 比較対象
   * 
   * @return 各成分に比較結果が入った{@link BooleanMatrix}
   */
  BooleanMatrix compareElementWise(String operator, M opponent);
}