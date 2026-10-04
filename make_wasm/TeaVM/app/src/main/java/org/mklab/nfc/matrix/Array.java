/**
 * $Id: Array.java,v 1.10 2008/04/16 23:50:11 koga Exp $
 *
 * Copyright (C) 2004 Masanobu Koga. All rights reserved.
 */
package org.mklab.nfc.matrix;

/**
 * 配列データを表わすインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.10 $
 * @param <M> 行列の型
 */
public interface Array<M extends Array<M>> extends Grid {
  
  /**
   * 複製(クローン)を返します。
   * 
   * @return 複製(クローン)
   */
  M createClone();

  /**
   * <code>original</code>の成分をコピーします。
   * 
   * @param original コピーする行列
   */
  void copy(M original);

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
   * 転置行列(this <sup>T </sup>)を生成します。
   * 
   * @return 転置行列
   */
  M transpose();

  /**
   * 部分行列A(rowMin:rowMax,columnMin:columnMax)を生成します。
   * 
   * @param rowMin 行の始まり
   * @param rowMax 行の終わり
   * @param columnMin 列の始まり
   * @param columnMax 列の終わり
   * @return 指定された部分行列
   */
  M getSubMatrix(int rowMin, int rowMax, int columnMin, int columnMax);

  /**
   * <code>column</code>列ベクトルの<code>rowIndex</code>で指定された成分からなる列ベクトルを生成します。
   * 
   * @param rowIndex 行指定ベクトル
   * @param column 列指定
   * @return 指定された部分行列
   */
  M getSubMatrix(IntMatrix rowIndex, int column);

  /**
   * <code>columnMin</code>列から<code>columnMax</code>列まで、<code>rowIndex</code> で指定された行を成分とする部分行列を生成します。
   * 
   * @param rowIndex 行指定ベクトル
   * @param columnMin 列のはじまり
   * @param columnMax 列の終わり
   * @return 指定された部分行列
   */
  M getSubMatrix(IntMatrix rowIndex, int columnMin, int columnMax);

  /**
   * <code>row</code>行ベクトルの<code>columnIndex</code>で指定された成分からなる行ベクトルを生成します。
   * 
   * @param row 行指定
   * @param columnIndex 列指定ベクトル
   * @return 部分行列
   */
  M getSubMatrix(int row, IntMatrix columnIndex);

  /**
   * rowMin行からrowMax行目で、columnIndexで指定された列を成分とする部分行列を生成します。
   * 
   * @param rowMin 行の始まり
   * @param rowMax 行の終わり
   * @param columnIndex 列指定ベクトル
   * @return 部分行列
   */
  M getSubMatrix(int rowMin, int rowMax, IntMatrix columnIndex);

  /**
   * <code>rowIndex</code>で指定された行、<code>columnIndex</code> で指定された列を成分とする部分行列を生成します。
   * 
   * @param rowIndex 行指定ベクトル
   * @param columnIndex 列指定ベクトル
   * @return 部分行列
   */
  M getSubMatrix(IntMatrix rowIndex, IntMatrix columnIndex);

  /**
   * 行毎に数え<code>index</code>で指定した成分を成分とする部分行列を生成します。
   * 
   * @param index 行番号を含むベクトル
   * @return 部分行列
   */
  M getSubVector(IntMatrix index);

  /**
   * 対角成分をからなる縦ベクトルを生成します。
   * 
   * @return 対角成分からなる縦ベクトル
   */
  M diagonalToVector();

  /**
   * 縦ベクトルまたは横ベクトルの各成分を対角成分に持つ行列を生成します。
   * 
   * @return 対角成分からなる縦ベクトル
   */
  M vectorToDiagonal();

  /**
   * サイズを<code>newRowSize</code>*<code>newColumnSize</code>に変更した行列を生成します。
   * 
   * <p>成分は、行方向の成分順に並べ替えられます。 <code>newRowSize</code> * <code>newColumnSize</code>個の成分を持たなければ、エラーとなります。
   * 
   * @param newRowSize 行の数
   * @param newColumnSize 列の数
   * @return サイズ変更された行列
   */
  M reshape(int newRowSize, int newColumnSize);

  /**
   * <code>newRowSize</code>*<code>newColumnSize</code>にサイズ変更します。
   * 
   * <p>{@link #reshape}とは異なり、成分位置の変更はせず, 自身より大きなサイズに変更する時は、 0が埋められ、自身より小さなサイズに変更する時は余分な成分は切り取られます。
   * 
   * @param newRowSize 指定行の数
   * @param newColmunSize 指定列の数
   * @return サイズ変更後の行列
   */
  M resize(int newRowSize, int newColmunSize);

  //  /**
  //   * 指定された行を削除します。
  //   * 
  //   * @param rowIndex 指定行
  //   */
  //  void removeRowVector(int rowIndex);
  //
  //  /**
  //   * 指定された列を削除します。
  //   * 
  //   * @param columnIndex 指定列
  //   */
  //  void removeColumnVector(int columnIndex);

  /**
   * 指定された行を返します。
   * 
   * @param row 行番号
   * @return 指定された行
   */
  M getRowVector(int row);

  /**
   * <code>rowMin</code>行から<code>rowMax</code>行までの部分行列を生成します。
   * 
   * @param rowMin 行の始まり
   * @param rowMax 行のおわり
   * @return 部分行列
   */
  M getRowVectors(int rowMin, int rowMax);

  /**
   * <code>rowIndex</code>で指定された行からなる部分行列を生成します。
   * 
   * @param rowIndex 取り出す行番号
   * @return 部分行列
   */
  M getRowVectors(IntMatrix rowIndex);

  /**
   * 指定された列を返します。
   * 
   * @param column 列番号
   * @return 指定された列
   */
  M getColumnVector(int column);

  /**
   * <code>columnMin</code>列から<code>columnMax</code>列までの部分行列を生成します。
   * 
   * @param columnMin 列の始まり
   * @param columnMax 列の終わり
   * @return 部分行列
   */
  M getColumnVectors(int columnMin, int columnMax);

  /**
   * <code>columnIndex</code>で指定された列からなる部分行列を生成します。
   * 
   * @param columnIndex 取り出す列番号
   * @return 部分行列
   */
  M getColumnVectors(IntMatrix columnIndex);

  /**
   * <code>block</code>のサイズで分割したときの<code>row</code>行、 <code>column</code>列番目のブロック行列を返します。
   * 
   * @param row 行番号
   * @param column 列番号
   * @param block 基本ブロック行列
   * @return 部分行列
   */
  M getSubMatrix(int row, int column, Grid block);

  /**
   * 成分を行毎に数え、<code>min</code>から<code>max</code>までの成分からなるベクトルを生成します。
   * 
   * @param min 成分取り出し開始位置
   * @param max 成分取り出し終了位置
   * @return fromからtoまでの成分を持つ横ベクトル
   */
  M getSubVector(int min, int max);

  /**
   * 成分を行毎に数え、<code>min</code>から<code>max</code>までの<code>by</code> 飛び成分からなるベクトルを生成します。
   * 
   * @param min 成分取り出し開始位置
   * @param max 成分取り出し終了位置
   * @param by 飛ばす数
   * @return fromからtoまでの成分を持つ横ベクトル
   */
  M getSubVector(int min, int max, int by);

  /**
   * <code>opponent</code>と成分毎に<code>operator</code>で指定された演算子で比較した結果を {@link BooleanMatrix}で返します。
   * 
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param opponent 比較対象
   * 
   * @return 各成分に比較結果が入った{@link BooleanMatrix}
   */
  BooleanMatrix compareElementWise(String operator, M opponent);

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
   * 指定された行に<code>source</code>を代入します。
   * 
   * @param row 行番号
   * @param source 代入する行列
   */
  void setRowVector(int row, M source);

  /**
   * <code>rowMin</code>行から<code>rowMax</code>行に<code>source</code>を代入します。
   * 
   * @param rowMin 行番号(始まり)
   * @param rowMax 行番号(おわり)
   * @param source 代入する行列
   */
  void setRowVectors(int rowMin, int rowMax, M source);

  /**
   * 指定された複数の行に<code>source</code>を代入します。
   * 
   * @param rowIndex 行番号
   * @param source 代入する行列
   */
  void setRowVectors(IntMatrix rowIndex, M source);

  /**
   * 指定された列に<code>source</code>を代入します。
   * 
   * @param column 列番号
   * @param source 代入する行列
   */
  void setColumnVector(int column, M source);

  /**
   * <code>columnMin</code>列から<code>columnMax</code>列に<code>source</code>を代入します。
   * 
   * @param columnMin 列の始まり
   * @param columnMax 列のおわり
   * @param source 代入する行列
   */
  void setColumnVectors(int columnMin, int columnMax, M source);

  /**
   * 指定された列に<code>source</code>を代入します。
   * 
   * @param columnIndex 列番号
   * @param source 代入する行列
   */
  void setColumnVectors(IntMatrix columnIndex, M source);

  /**
   * 指定した成分に行列<code>source</code>を代入します。
   * 
   * @param rowMin 開始行番号
   * @param rowMax 終了行番号
   * @param columnMin 開始列番号
   * @param columnMax 終了列番号
   * @param source 代入する行列
   */
  void setSubMatrix(int rowMin, int rowMax, int columnMin, int columnMax, M source);

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
   * <code>rowIndex</code>で指定した行の<code>column</code>列に行列<code>source</code> を代入します。
   * 
   * @param rowIndex 行番号のリスト
   * @param column 列番号
   * @param source 代入する行列
   */
  void setSubMatrix(IntMatrix rowIndex, int column, M source);

  /**
   * <code>rowIndex</code>で指定した行の<code>columnMin</code>列から<code>columnMax</code> 列までの行列<code>source</code>を代入します。
   * 
   * @param rowIndex 行番号のリスト
   * @param columnMin 列の始まり
   * @param columnMax 列の終わり
   * @param source 代入する行列
   */
  void setSubMatrix(IntMatrix rowIndex, int columnMin, int columnMax, M source);

  /**
   * <code>row</code>列目の成分の<code>columnIndex</code>で指定された行の成分に行列<code>source</code>を代入します。
   * 
   * @param row 行番号
   * @param columnIndex 列番号のリスト
   * @param source 代入する行列
   */
  void setSubMatrix(int row, IntMatrix columnIndex, M source);

  /**
   * <code>rowMin</code>列目から<code>rowMax</code>列目の成分の<code>columnIndex</code> で指定された行の成分に行列<code>source</code>を代入します。
   * 
   * @param rowMin 行の始まり
   * @param rowMax 行の終わり
   * @param columnIndex 列番号のリスト
   * @param source 代入する行列
   */
  void setSubMatrix(int rowMin, int rowMax, IntMatrix columnIndex, M source);

  /**
   * <code>rowIndex</code>で指定した行の<code>columnIndex</code>で指定した列に行列<code>source</code>を代入します。
   * 
   * @param rowIndex 行番号のリスト
   * @param columnIndex 列番号のリスト
   * @param source 代入する行列
   */
  void setSubMatrix(IntMatrix rowIndex, IntMatrix columnIndex, M source);

  /**
   * <code>index</code>で指定した各成分に行列<code>source</code>の成分に代入します。
   * 
   * @param index 成分の番号のリスト
   * @param source 代入する行列
   */
  void setSubVector(IntMatrix index, M source);

  /**
   * 成分を行毎に数え、<code>min</code>から<code>max</code>までに<code>source</code>の成分を代入します。
   * 
   * @param min 成分の代入開始位置
   * @param max 成分の代入終了位置
   * @param source 代入するベクトル
   */
  void setSubVector(int min, int max, M source);

  /**
   * 成分を行毎に数え、<code>min</code>から<code>max</code>までの成分に<code>by</code>飛びで <code>source</code>の成分を代入します。
   * 
   * @param min 成分の代入開始位置
   * @param max 成分の代入終了位置
   * @param by 飛ばす数
   * @param source 代入するベクトル
   */
  void setSubVector(int min, int max, int by, M source);
}