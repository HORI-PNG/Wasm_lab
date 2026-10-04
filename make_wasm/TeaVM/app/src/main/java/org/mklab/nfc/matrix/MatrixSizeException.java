/*
 * $Id: MatrixSizeException.java,v 1.16 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

/**
 * 行列に関する演算において、演算不可能な行の数または列の数 であった場合に発生する例外クラスです。
 * 
 * @author yuri
 * @version $Revision: 1.16 $
 */
public class MatrixSizeException extends RuntimeException {

  /** シリアルバージョン。 */
  private static final long serialVersionUID = 4571527619440531840L;

  /** エラーメッセージ"Not same size"。 */
  public static final String NOT_SAME_SIZE = Messages.getString("MatrixSizeException.0"); //$NON-NLS-1$

  /** エラーメッセージ"Inconsistent size"。 */
  public static final String INCONSISTENT_SIZE = Messages.getString("MatrixSizeException.1"); //$NON-NLS-1$

  /** エラーメッセージ"Inconsistent row number"。 */
  public static final String INCONSISTENT_ROW_NUMBER = Messages.getString("MatrixSizeException.2"); //$NON-NLS-1$

  /** エラーメッセージ"Inconsistent column number"。 */
  public static final String INCONSISTENT_COLUMN_NUMBER = Messages.getString("MatrixSizeException.3"); //$NON-NLS-1$

  /** エラーメッセージ"Not a square matrix"。 */
  public static final String NOT_A_SQUARE_MATRIX = Messages.getString("MatrixSizeException.4"); //$NON-NLS-1$

  /** エラーメッセージ"Incorrect size of matrices"。 */
  public static final String INCORRECT_SIZE = Messages.getString("MatrixSizeException.5"); //$NON-NLS-1$

  /** エラーメッセージ"Not a vector matrix"。 */
  public static final String NOT_A_VECTOR_MATRIX = Messages.getString("MatrixSizeException.6"); //$NON-NLS-1$

  /**
   * 標準の例外オブジェクトを作る。
   * 
   * @param message メッセージ
   */
  public MatrixSizeException(final String message) {
    super(message);
  }

  /**
   * コンストラクター<br>
   * 
   * 例外メッセージ中に行列のサイズを含めた例外オブジェクトを生成します。
   * 
   * @param left 左側の行列
   * @param right 右側の行列
   * @param message メッセージ
   */
  public MatrixSizeException(final Grid left, final Grid right, final String message) {
    super(getSizeString(left) + ", " + getSizeString(right) + ", " + message); //$NON-NLS-1$ //$NON-NLS-2$
  }

  /**
   * "(rowSize*columnSize)"という文字列を返します。
   * 
   * @param grid グリッド
   * @return 行の数x列の数 の文字列
   */
  private static String getSizeString(final Grid grid) {
    return "(" + grid.getRowSize() + "x" + grid.getColumnSize() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }
}
