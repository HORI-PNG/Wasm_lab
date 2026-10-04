/**
 * $Id: BooleanMatrixUtil.java,v 1.53 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Masanobu Koga. All rights reserved.
 */
package org.mklab.nfc.matrix;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

import org.mklab.nfc.matx.MxDataHead;


/**
 * {@link BooleanMatrix}のユーティリティクラスです。
 * 
 * @author koga
 * @version $Revision: 1.53 $
 */
public final class BooleanMatrixUtil {
  /**
   * 新しく生成された<code>BooleanMatrixUtil</code>オブジェクトを初期化します。
   */
  private BooleanMatrixUtil() {
    // nothing to do
  }

  /**
   * 行列の複製を生成します。
   * 
   * @param matrix 複製の元となる行列
   * @return 複製された行列
   */
  public static boolean[][] clone(final boolean[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      boolean[] ansi = ans[i];
      boolean[] matrixi = matrix[i];
      System.arraycopy(matrixi, 0, ansi, 0, columnSize);
    }
    return ans;
  }

  /**
   * <code>column1</code>列と<code>column2</code>列を入れ替えます。
   * 
   * @param matrix 対象の行列
   * @param column1 指定列１
   * @param column2 指定列２
   */
  public static void exchangeColumn(final boolean[][] matrix, final int column1, final int column2) {
    if (column1 == column2) {
      return;
    }

    int rowSize = matrix.length;

    for (int i = 0; i < rowSize; i++) {
      boolean tmp = matrix[i][column1];
      matrix[i][column1] = matrix[i][column2];
      matrix[i][column2] = tmp;
    }
  }

  /**
   * <code>row1</code>行と<code>row2</code>行を入れ替えます。
   * 
   * @param matrix 対象の行列
   * @param row1 指定行1
   * @param row2 指定行2
   */
  public static void exchangeRow(final boolean[][] matrix, final int row1, final int row2) {
    if (row1 == row2) {
      return;
    }

    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    for (int i = 0; i < columnSize; i++) {
      boolean tmp = matrix[row1][i];
      matrix[row1][i] = matrix[row2][i];
      matrix[row2][i] = tmp;
    }
  }

  /**
   * <code>newRowSize</code>*<code>newColumnSize</code>にサイズ変更します。
   * 
   * <p>{@link #reshape}とは異なり、成分位置の変更はせず、自身より大きなサイズに変更する時は、 0が埋められ、自身より小さなサイズに変更する時は余分な成分は切り取られます。
   * 
   * @param matrix 対象となる行列
   * @param newRowSize 新しい行の数
   * @param newColumnSize 新しいレスう
   * @return サイズ変更後の行列
   */
  public static boolean[][] resize(final boolean[][] matrix, final int newRowSize, final int newColumnSize) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    boolean[][] ans = new boolean[newRowSize][newColumnSize];
    int re = Math.min(newRowSize, rowSize);
    int ce = Math.min(newColumnSize, columnSize);

    // MatrixUtil.areaCopy(ans, 0, 0, matrix, 0, 0, re, ce);
    for (int i = 0; i < re; i++) {
      for (int j = 0; j < ce; j++) {
        ans[i][j] = matrix[i][j];
      }
      for (int j = ce; j < newColumnSize; j++) {
        ans[i][j] = false;
      }
    }
    for (int i = re; i < newRowSize; i++) {
      for (int j = 0; j < ce; j++) {
        ans[i][j] = false;
      }
      for (int j = ce; j < newColumnSize; j++) {
        ans[i][j] = false;
      }
    }
    return ans;
  }

  /**
   * 指定された列を削除した行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param columnMin 開始列
   * @param columnMax 終了列
   * @return 列を削除された行列
   */
  public static boolean[][] removeColumnVectors(final boolean[][] matrix, final int columnMin, final int columnMax) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int newColumnSize = columnSize - (columnMax - columnMin + 1);

    boolean[][] ans = new boolean[rowSize][newColumnSize];
    for (int i = 0; i < rowSize; i++) {
      boolean[] matrixi = matrix[i];
      boolean[] ansi = ans[i];
      for (int j = 0, j2 = 0; j < columnSize; j++) {
        if (columnMin <= j && j <= columnMax) {
          continue;
        }
        ansi[j2] = matrixi[j];
        j2++;
      }
    }

    return ans;
  }

  /**
   * 指定された列を削除した行列を生成します。
   * 
   * @param matrix 元の行列
   * @param index 削除する列の番号
   * @return 列を削除された行列
   */
  public static boolean[][] removeColumnVectors(final boolean[][] matrix, final int[] index) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int columnSize2 = index.length;

    // 削除すべき列番号にtrue
    boolean[] removedColumn = new boolean[columnSize];
    for (int i = 0; i < columnSize2; i++) {
      removedColumn[index[i]] = true;
    }

    // 新しい列の数
    int newColumnSize = 0;
    for (int i = 0; i < columnSize; i++) {
      if (removedColumn[i]) {
        continue;
      }
      newColumnSize++;
    }

    boolean[][] ans = new boolean[rowSize][newColumnSize];
    for (int i = 0; i < rowSize; i++) {
      boolean[] matrixi = matrix[i];
      boolean[] ansi = ans[i];
      for (int j = 0, j2 = 0; j < columnSize; j++) {
        if (removedColumn[j]) {
          continue;
        }
        ansi[j2] = matrixi[j];
        j2++;
      }
    }

    return ans;
  }

  /**
   * 指定された行を削除した行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param min 開始行
   * @param max 終了行
   * @return 行を削除された行列
   */
  public static boolean[][] removeRowVectors(final boolean[][] matrix, final int min, final int max) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int newRowSize = rowSize - (max - min + 1);

    boolean[][] ans = new boolean[newRowSize][columnSize];

    for (int i = 0, i2 = 0; i < rowSize; i++) {
      if (min <= i && i <= max) {
        continue;
      }
      boolean[] matrixi = matrix[i];
      boolean[] ansi = ans[i2];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j];
      }
      i2++;
    }

    return ans;
  }

  /**
   * 指定された行を削除した行列を生成します。
   * 
   * @param matrix 元の行列
   * @param index 削除する行の番号
   * @return 行を削除された行列
   */
  public static boolean[][] removeRowVectors(final boolean[][] matrix, final int[] index) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int size = index.length;

    // 削除すべき行番号にtrue
    boolean[] removedRow = new boolean[rowSize];
    for (int i = 0; i < size; i++) {
      removedRow[index[i]] = true;
    }

    // 新しい行の数
    int newRowSize = 0;
    for (int i = 0; i < rowSize; i++) {
      if (removedRow[i]) {
        continue;
      }
      newRowSize++;
    }

    boolean[][] ans = new boolean[newRowSize][columnSize];
    for (int i = 0, i2 = 0; i < rowSize; i++) {
      if (removedRow[i]) {
        continue;
      }
      boolean[] matrixi = matrix[i];
      boolean[] ansi = ans[i2];
      System.arraycopy(matrixi, 0, ansi, 0, columnSize);
      i2++;
    }

    return ans;
  }

  /**
   * 部分行列を生成します。
   * 
   * @param matrix 元の行列
   * @param rowMin 始まりの行
   * @param rowMax 終わりの行
   * @param columnIndex 該当する列の番号
   * @return 部分行列
   */
  public static boolean[][] getSubMatrix(final boolean[][] matrix, final int rowMin, final int rowMax, final int[] columnIndex) {
    int rowSize = rowMax - rowMin + 1;
    int columnSize = columnIndex.length;
    boolean[][] ans = new boolean[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[rowMin + i][columnIndex[j]];
      }
    }
    return ans;
  }

  /**
   * 部分行列を生成します。
   * 
   * @param matrix 元の行列
   * @param rowIndex 該当する行の番号
   * @param columnIndex 該当する列の番号
   * @return 部分行列
   */
  public static boolean[][] getSubMatrix(final boolean[][] matrix, final int[] rowIndex, final int[] columnIndex) {
    int rowSize = rowIndex.length;
    int columnSize = columnIndex.length;

    boolean[][] ans = new boolean[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[rowIndex[i]][columnIndex[j]];
      }
    }
    return ans;
  }

  /**
   * 部分行列を生成します。
   * 
   * @param matrix 元の行列
   * @param rowIndex 該当する行の番号
   * @param columnMin 始まりの列
   * @param columnMax 終わりの列
   * @return 部分行列
   */
  public static boolean[][] getSubMatrix(final boolean[][] matrix, final int[] rowIndex, final int columnMin, final int columnMax) {
    int rowSize = rowIndex.length;
    int columnSize = columnMax - columnMin + 1;
    boolean[][] ans = new boolean[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[rowIndex[i]][j + columnMin];
      }
    }
    return ans;
  }

//  /**
//   * 部分行列を生成します。
//   * 
//   * @param matrix 元の行列
//   * @param rowIndex 該当する行の番号
//   * @param column 列番号
//   * @return 部分行列
//   */
//  public static boolean[][] getSubMatrix(final boolean[][] matrix, final int[] rowIndex, final int column) {
//    int rowSize = rowIndex.length;
//
//    boolean[][] ans = new boolean[rowSize][1];
//    for (int i = 0; i < rowSize; i++) {
//      ans[i][0] = matrix[rowIndex[i]][column];
//    }
//    return ans;
//  }

//  /**
//   * 部分行列を生成します。
//   * 
//   * @param matrix 元の行列
//   * @param row 行の番号
//   * @param columnIndex 該当する列の番号
//   * @return 部分行列
//   */
//  public static boolean[][] getSubMatrix(final boolean[][] matrix, final int row, final int[] columnIndex) {
//    int columnSize = columnIndex.length;
//
//    boolean[][] ans = new boolean[1][columnSize];
//    for (int i = 0; i < columnSize; i++) {
//      ans[0][i] = matrix[row][columnIndex[i]];
//    }
//
//    return ans;
//  }

  /**
   * 部分ベクトルを生成します。
   * 
   * @param matrix 元のベクトル
   * @param index 該当する行の番号
   * @return 部分ベクトル
   */
  public static boolean[] getSubVector(final boolean[] matrix, final int[] index) {
    if (index.length == 0) {
      return new boolean[0];
    }

    int size = index.length;
    boolean[] ans = new boolean[size];
    for (int i = 0; i < size; i++) {
      ans[i] = matrix[index[i]];
    }
    return ans;
  }

  /**
   * 行列をMMフォーマットの文字列に変換します。
   * 
   * @param matrix 対象となる行列
   * @return MMフォーマットの文字列
   */
  public static String toMmString(final boolean[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    StringBuffer sb = new StringBuffer();
    String newLine = System.getProperty("line.separator"); //$NON-NLS-1$

    if (rowSize == 0 || columnSize == 0) {
      return "[]"; //$NON-NLS-1$
    }
    
    if (columnSize == 1 && rowSize != 1) {
      return toMmString(transpose(matrix)) + "'"; //$NON-NLS-1$
    }

    if (rowSize != 1) {
      sb.append("["); //$NON-NLS-1$
    }

    final int displayColumnSize = columnSize;

    for (int i = 0; i < rowSize; i++) {
      if (i != 0) {
        sb.append(" "); //$NON-NLS-1$
      }

      for (int k = 0; k < columnSize;) {
        if (k == 0) {
          sb.append("["); //$NON-NLS-1$
        } else {
//          sb.append(" "); //$NON-NLS-1$
//          if (rowSize != 1) {
//            sb.append(" "); //$NON-NLS-1$
//          }
        }

        int j;
        for (j = k; j < k + displayColumnSize && j < columnSize; j++) {
          if (matrix[i][j]) {
            sb.append("1"); //$NON-NLS-1$
          } else {
            sb.append("0"); //$NON-NLS-1$
          }

          if (j != columnSize - 1) {
            sb.append(","); //$NON-NLS-1$
          }
        }

        if (j == columnSize) {
          sb.append("]"); //$NON-NLS-1$
          if (i != rowSize - 1) {
            sb.append(newLine);
          }
        } else {
//          sb.append(newLine);
        }

        k += displayColumnSize;
      }
    }

    if (rowSize != 1) {
      sb.append("]"); //$NON-NLS-1$
    }

    return sb.toString();
  }

  /**
   * 行列<code>source</code>の各成分を行列<code>destination</code>の各成分にコピーします。
   * 
   * @param source コピー元行列
   * @param destination コピー先行列
   */
  public static void copy(final boolean[][] source, final boolean[][] destination) {
    int rowSizeFrom = source.length;
    int columnSizeFrom = rowSizeFrom == 0 ? 0 : source[0].length;
    int rowSizeTo = destination.length;
    int columnSizeTo = rowSizeTo == 0 ? 0 : destination[0].length;

    if (rowSizeTo != rowSizeFrom || columnSizeTo != columnSizeFrom) {
      throw new MatrixSizeException(Messages.getString("BooleanMatrixUtil.11")); //$NON-NLS-1$
    }

    for (int i = 0; i < rowSizeFrom; i++) {
      boolean[] toi = destination[i];
      boolean[] fromi = source[i];
      for (int j = 0; j < columnSizeFrom; j++) {
        toi[j] = fromi[j];
      }
    }
  }

//  /**
//   * <code>source</code>の各成分を調べ、falseなら<code>destination</code>の対応する位置にfalse、 trueなら<code>destination</code>の対応する位置にtureを代入します。
//   * 
//   * @param destination boolean行列の成分をもつ配列
//   * @param source boolean行列の成分をもつ配列
//   */
//  static void setValues(final boolean[][] destination, final boolean[][] source) {
//    copy(source, destination);
//  }

  /**
   * 全成分を調べtrueが1個でもあればtrue、そうでなければfalseを返します。
   * 
   * @param matrix 対象となる行列
   * @return 全成分を調べtrueが1個でもあればtrue、そうでなければfalse
   */
  public static boolean anyTrue(final boolean[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        if (matrix[i][j]) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 行毎の成分を調べ、行にtrueが1個でもあればtrue、そうでなければfalseを対応させ、 booleanを含むboolean行列を返します。
   * 
   * @param matrix 対象となる行列
   * @return 調査の結果を成分とするboolean行列
   */
  public static boolean[][] anyTrueRowWise(final boolean[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[][] ans = new boolean[rowSize][1];
    for (int i = 0; i < rowSize; i++) {
      ans[i][0] = false;
      for (int j = 0; j < columnSize; j++) {
        if (matrix[i][j]) {
          ans[i][0] = true;
          break;
        }
      }
    }
    return ans;
  }

  /**
   * 列毎の成分を調べ、行にtrueが1個でもあればtrue、そうでなければfalseを対応させ、 booleanを含むboolean行列を返します。
   * 
   * @param matrix 対象となる行列
   * @return 調査の結果を成分とするboolean行列
   */
  public static boolean[] anyTrueColumnWise(final boolean[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[] ans = new boolean[columnSize];
    for (int i = 0; i < columnSize; i++) {
      ans[i] = false;
      for (int j = 0; j < rowSize; j++) {
        if (matrix[j][i]) {
          ans[i] = true;
          break;
        }
      }
    }
    return ans;
  }

  /**
   * 全成分を調べ、全成分がtrueならtrue、そうでなければfalseを返します。
   * 
   * @param matrix 対象となる行列
   * @return 調査の結果
   */
  public static boolean allTrue(final boolean[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        if (!matrix[i][j]) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * 成分を行毎に調べ、行の全成分がtrueならtrue、そうでなければfalseを対応させ、 行毎のbooleanからなるboolean行列を返します。
   * 
   * @param matrix 対象となる行列
   * @return 調査の結果を成分とするboolean行列
   */
  public static boolean[][] allTrueRowWise(final boolean[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[][] ans = new boolean[rowSize][1];
    for (int i = 0; i < rowSize; i++) {
      ans[i][0] = true;
      for (int j = 0; j < columnSize; j++) {
        if (!matrix[i][j]) {
          ans[i][0] = false;
          break;
        }
      }
    }
    return ans;
  }

  /**
   * 成分を列毎に調べ、列の全成分がtrueならtrue、そうでなければfalseを対応させ、行毎のbooleanからなる boolean行列を返します。
   * 
   * @param matrix 対象となる行列
   * @return 調査の結果を成分とするboolean行列
   */
  public static boolean[] allTrueColumnWise(final boolean[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[] ans = new boolean[columnSize];
    for (int i = 0; i < columnSize; i++) {
      ans[i] = true;
      for (int j = 0; j < rowSize; j++) {
        if (!matrix[j][i]) {
          ans[i] = false;
          break;
        }
      }
    }
    return ans;
  }

  /**
   * 各成分の否定(trueならfalse、falseならtrue)を成分にもつboolean行列を返します。
   * 
   * @param matrix 対象となる行列
   * @return 調査の結果を成分とするboolean行列
   */
  public static boolean[][] notElementWise(final boolean[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = !matrix[i][j];
      }
    }
    return ans;
  }

  /**
   * <code>a1</code>と<code>a2</code>の各成分の論理積を成分にもつboolean行列を返します。
   * 
   * @param a1 演算の対象
   * @param a2 演算の対象
   * 
   * @return 演算の結果を成分とするboolean行列
   */

  public static boolean[][] andElementWise(final boolean[][] a1, final boolean[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = a1[i][j] && a2[i][j];
      }
    }
    return ans;
  }

  /**
   * 2個の行列の積(成分毎の論理積)を求めます。
   * 
   * @param a1 掛けられる行列
   * @param a2 掛ける行列
   * @return 積(成分毎の論理積)
   */
  public static boolean[][] multiply(final boolean[][] a1, final boolean[][] a2) {
    int rowSize1 = a1.length;
    int columnSize1 = rowSize1 == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (columnSize1 != rowSize2) {
      throw new MatrixSizeException(Messages.getString("BooleanMatrixUtil.12")); //$NON-NLS-1$
    }

    boolean[][] ans = new boolean[rowSize1][columnSize2];

    for (int i = 0; i < rowSize1; i++) {
      boolean[] a1i = a1[i];
      boolean[] ansi = ans[i];
      for (int j = 0; j < columnSize2; j++) {
        boolean d = false;
        for (int k = 0; k < columnSize1; k++) {
          if (a1i[k] && a2[k][j]) {
            d = true;
            break;
          }
        }
        ansi[j] = d;
      }
    }
    return ans;
  }

  /**
   * 行列の各成分と<code>scalar</code>の論理積を成分にもつboolean行列を返します。
   * 
   * @param matrix 対象となる行列
   * @param scalar 対象となるboolean値
   * 
   * @return 演算の結果を成分とするboolean行列
   */
  public static boolean[][] andElementWise(final boolean[][] matrix, final boolean scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j] && scalar;
      }
    }
    return ans;
  }

  /**
   * 2個の行列の各成分の論理和を成分にもつboolean行列を返します。
   * 
   * @param a1 対象となる行列
   * @param a2 対象となる行列
   * 
   * @return 演算の結果を成分とするboolean行列
   */
  public static boolean[][] orElementWise(final boolean[][] a1, final boolean[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = a1[i][j] || a2[i][j];
      }
    }
    return ans;
  }

  /**
   * 行列の各成分と<code>scalar</code>の論理和を成分にもつboolean行列を返します。
   * 
   * @param matrix 対象となる行列
   * @param scalar 全ての演算に用いるboolean
   * @return 演算の結果を成分とするboolean行列
   */
  public static boolean[][] orElementWise(final boolean[][] matrix, final boolean scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j] || scalar;
      }
    }
    return ans;
  }

  /**
   * 2個の行列の各成分の排他的論理和を成分にもつboolean行列を返します。
   * 
   * @param a1 対象となる行列
   * @param a2 対象となる行列
   * 
   * @return 演算の結果を成分とするboolean行列
   */
  public static boolean[][] exorElementWise(final boolean[][] a1, final boolean[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        if (a1[i][j] != a2[i][j]) {
          ans[i][j] = true;
        }
      }
    }
    return ans;
  }

  /**
   * 行列の各成分と<code>scalar</code>の排他的論理和を成分にもつboolean行列を返します。
   * 
   * @param matrix 対象となる行列
   * @param scalar 全ての演算に用いるboolean
   * @return 演算の結果を成分とするboolean行列
   */
  public static boolean[][] exorElementWise(final boolean[][] matrix, final boolean scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        if (matrix[i][j] != scalar) {
          ans[i][j] = true;
        }
      }
    }
    return ans;
  }

  /**
   * ライターに出力します。
   * 
   * @param matrix 対象となる行列
   * @param output ライター
   * @param format 成分の出力フォーマット
   * @param maxColumnSize 最大列の数
   */
  public static void print(final boolean[][] matrix, final Writer output, final String format, final int maxColumnSize) {
    final PrintWriter pw = new PrintWriter(output);
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int remain = columnSize;
    int columnOffset = 0;
    while (remain > 0) {
      int printColumnSize = Math.min(remain, maxColumnSize);

      // 列番号を表示する
      BooleanMatrixUtil.printColumnNumber(columnOffset, printColumnSize, pw);

      for (int i = 0; i < rowSize; i++) {
        BooleanMatrixUtil.printRowNumber(i, pw);

        boolean[] elements = new boolean[printColumnSize];
        for (int j = 0; j < printColumnSize; j++) {
          elements[j] = matrix[i][columnOffset + j];
        }

        BooleanMatrixUtil.printElements(elements, pw, format);
      }
      columnOffset += printColumnSize;
      remain -= printColumnSize;
    }

    pw.flush();
  }

  /**
   * 複数個のbooleanをプリントライターに出力します。
   * 
   * @param elements booleanの配列
   * @param output 出力先のプリントライター
   * @param format 成分の出力フォーマット
   */
  private static void printElements(final boolean[] elements, final PrintWriter output, final String format) {
    for (int i = 0; i < elements.length; i++) {
      output.print(" "); //$NON-NLS-1$
      output.print(String.format(format, Boolean.valueOf(elements[i]).toString()));
    }
    output.println();
  }

  /**
   * 行番号を出力します。
   * 
   * @param row 行番号
   * @param output 出力先のプリントライター
   */
  private static void printRowNumber(final int row, final PrintWriter output) {
    output.print(String.format(" (%3d)", Integer.valueOf(row + 1))); //$NON-NLS-1$
  }

  /**
   * 列番号を出力します。
   * 
   * @param columnOffset 列のオフセット
   * @param printColumnSize 出力する列の数
   * @param output 出力先のプリントライター
   */
  private static void printColumnNumber(final int columnOffset, final int printColumnSize, final PrintWriter output) {
    output.print("      "); //$NON-NLS-1$
    for (int i = 0; i < printColumnSize; i++) {
      String str = String.format("   (%3d)  ", Integer.valueOf(columnOffset + i + 1)); //$NON-NLS-1$
//      if (columnOffset + i + 1 > 1000) {
//        str = str.substring(0, str.length() - 1);
//        if (columnOffset + i + 1 > 10000) {
//          str = str.substring(0, str.length() - 1);
//        }
//      }
      output.print(str);
    }
    output.println();
  }

  /**
   * 行列の成分を変えずに、行列の大きさ(行の数と列の数)を変形します。
   * 
   * @param matrix 対象となる行列
   * @param newRowSize 変更後の行の数
   * @param newColumnSize 変更後の列の数
   * @return 変形した行列
   */
  public static boolean[][] reshape(final boolean[][] matrix, final int newRowSize, final int newColumnSize) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    boolean[][] ans = new boolean[newRowSize][newColumnSize];
    int num = 0;
    for (int i = 0; i < newRowSize; i++) {
      for (int j = 0; j < newColumnSize; j++) {
        ans[i][j] = matrix[num / columnSize][num % columnSize];
        num++;
      }
    }
    return ans;
  }

  /**
   * 成分のtrueの数を返します。
   * 
   * @param matrix booleanをもつ配列
   * @return trueの数
   */
  public static int getNumberOfTrue(final boolean[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int count = 0;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        if (matrix[i][j]) {
          count++;
        }
      }
    }
    return count;
  }

  /**
   * 各成分を調べ、trueの位置を順にも整数ベクトルを返します。
   * 
   * @param matrix booleanをもつ配列
   * @return trueの位置を順にもつ整数ベクトル
   */
  public static int[] find(final boolean[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int size = getNumberOfTrue(matrix);

    int[] ans = new int[size];
    int count = 0;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        if (matrix[i][j]) {
          ans[count++] = i * columnSize + j + 1;
        }
      }
    }
    return ans;
  }

  /**
   * <code>scalar</code>乗(<code>this</code> <sup><code>scalar</code></sup>)を返します。
   * 
   * @param matrix 対象となる行列
   * @param scalar 指数
   * @return num乗
   */
  public static boolean[][] power(final boolean[][] matrix, final int scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    boolean[][] ans = new boolean[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      ans[i][i] = true;
    }

    for (int j = 0; j < scalar; j++) {
      ans = multiply(ans, matrix);
    }
    return ans;
  }

  /**
   * 単位行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 単位行列
   */
  public static boolean[][] unit(final int rowSize, final int columnSize) {
    boolean[][] ans = new boolean[rowSize][columnSize];
    int size = rowSize < columnSize ? rowSize : columnSize;

    for (int i = 0; i < size; i++) {
      ans[i][i] = true;
    }
    return ans;
  }

  /**
   * <code>rowSize</code>*<code>columnSize</code>の全成分trueの行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 全ての成分が1である行列
   */
  public static boolean[][] ones(final int rowSize, final int columnSize) {
    boolean[][] ans = new boolean[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = true;
      }
    }
    return ans;
  }

  /**
   * ベクトルの成分を対角成分とする対角行列を生成します。
   * 
   * @param diagonalElements 対象となるベクトル
   * @return 対角行列
   */
  public static boolean[][] vectorToDiagonal(final boolean[] diagonalElements) {
    int size = diagonalElements.length;
    boolean[][] ans = new boolean[size][size];
    for (int i = 0; i < size; i++) {
      ans[i][i] = diagonalElements[i];
    }
    return ans;
  }

  /**
   * 行列を出力ストリームにMXフォーマットで出力します。
   * 
   * @param matrix 対象となる行列
   * @param output 出力ストリーム
   * @param name 行列の名前
   * @throws IOException ストリームに出力できない場合
   */
  public static void writeMxFormat(final boolean[][] matrix, final OutputStream output, final String name) throws IOException {
    MxDataHead head = new MxDataHead(matrix, name);
    head.write(output);

    DataOutputStream ds = new DataOutputStream(output);

    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ds.writeBoolean(matrix[i][j]);
      }
    }
    ds.flush();
  }

  /**
   * データ入力ストリームから行列データ(MXフォーマット)を読み込みます。
   * 
   * @param input 入力ストリーム
   * @param head MXフォーマットのヘッダ情報
   * @return 読み込んだ行列
   * @throws IOException ストリームに出力できない場合
   */
  public static boolean[][] readMxFormat(final InputStream input, final MxDataHead head) throws IOException {
    final int rowSize = head.getRowSize();
    final int columnSize = head.getColumnSize();
    final boolean[][] ans = new boolean[rowSize][columnSize];

    final DataInputStream is = new DataInputStream(input);
    
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = is.readBoolean();
      }
    }
    return ans;
  }

  /**
   * 2個の配列の成分が全て等しいか判定します。
   * 
   * @param a1 第一行列
   * @param a2 第二行列
   * @return 配列の成分が等しければtrue、そうでなければfalse
   */
  public static boolean equals(final boolean[][] a1, final boolean[][] a2) {
    int rowSize1 = a1.length;
    int columnSize1 = rowSize1 == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (rowSize1 != rowSize2 || columnSize1 != columnSize2) {
      return false;
    }

    for (int i = 0; i < rowSize1; i++) {
      for (int j = 0; j < columnSize1; j++) {
        if (a1[i][j] != a2[i][j]) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * 転置行列を生成します。
   * 
   * @param matrix 元の行列
   * @return 転置行列
   */
  public static boolean[][] transpose(final boolean[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[][] ans = new boolean[columnSize][rowSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[j][i] = matrix[i][j];
      }
    }
    return ans;
  }

  /**
   * 部分行列を生成します。
   * 
   * @param matrix 元の行列
   * @param rowMin 始まり行
   * @param rowMax 終わり行
   * @param columnMin 始まり列
   * @param columnMax 終わり列
   * @return 部分行列
   */
  public static boolean[][] getSubMatrix(final boolean[][] matrix, final int rowMin, final int rowMax, final int columnMin, final int columnMax) {
    int rowSize = rowMax - rowMin + 1;
    int columnSize = columnMax - columnMin + 1;

    boolean[][] ans = new boolean[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i + rowMin][j + columnMin];
      }
    }
    return ans;
  }

  /**
   * 対角成分を取り出し縦ベクトルとして返します。
   * 
   * @param matrix 対象となる行列
   * @return 対角成分からなる縦ベクトル
   */
  public static boolean[] diagonalToVector(final boolean[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int min = Math.min(rowSize, columnSize);
    boolean[] ans = new boolean[min];
    for (int i = 0; i < min; i++) {
      ans[i] = matrix[i][i];
    }
    return ans;
  }

//  /**
//   * 配列<code>destination</code>の<code>rowTo</code>行<code>columnTo</code>列を始点として、 配列<code>source</code>の<code>rowMin</code>行<code>columnMin</code>列から
//   * <code>rowMax</code>行<code>columnMax</code>列までの値をコピーします。
//   * 
//   * @param destination コピー先
//   * @param rowTo 変更開始行
//   * @param columnTo 変更開始列
//   * @param source コピー元
//   * @param rowMin コピー開始行
//   * @param rowMax コピー開始列
//   * @param columnMin コピー終了行
//   * @param columnMax コピー終了列
//   */
//  public static void setSubMatrix(final boolean[][] destination, final int rowTo, final int columnTo, final boolean[][] source, final int rowMin, final int rowMax, final int columnMin, final int columnMax) {
//    int nrow = rowMax - rowMin;
//    int ncol = columnMax - columnMin;
//    for (int i = 0; i <= nrow; i++) {
//      for (int j = 0; j <= ncol; j++) {
//        destination[rowTo - 1 + i][columnTo - 1 + j] = source[rowMin - 1 + i][columnMin - 1 + j];
//      }
//    }
//  }
  
  /**
   * 与えられた位置に行列を代入します。
   * 
   * @param destination 値を設定する行列
   * @param rowMin 行の始まり
   * @param rowMax 行の終わり
   * @param columnMin 列の始まり
   * @param columnMax 列の終わり
   * @param source 設定する行列
   */
  public static void setSubMatrix(final boolean[][] destination, final int rowMin, final int rowMax, final int columnMin, final int columnMax, final boolean[][] source) {
    int rowSize = source.length;
    int columnSize = rowSize == 0 ? 0 : source[0].length;

    if (rowSize != rowMax - rowMin + 1 || columnSize != columnMax - columnMin + 1) {
      throw new MatrixSizeException(MatrixSizeException.INCONSISTENT_SIZE);
    }

    for (int i = 0; i < rowSize; i++) {
      boolean[] toi = destination[i + rowMin];
      boolean[] fromi = source[i];

      System.arraycopy(fromi, 0, toi, columnMin, columnSize);
    }
  }

  /**
   * @param destination 値を設定する行列
   * @param rowIndex 指定する行を含む指数
   * @param columnMin 列の始まり
   * @param columnMax 列の終り
   * @param source 代入する行列
   */
  public static void setSubMatrix(final boolean[][] destination, final int[] rowIndex, final int columnMin, final int columnMax, final boolean[][] source) {
    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;
    int idxcol = rowIndex.length;
    int columnSize = columnMax - columnMin + 1;

    if (mrow != idxcol || mcol != columnSize) {
      throw new MatrixSizeException(Messages.getString("BooleanMatrixUtil.19")); //$NON-NLS-1$
    }

    for (int i = 0; i < idxcol; i++) {
      for (int j = 0; j < columnSize; j++) {
        destination[rowIndex[i]][j + columnMin] = source[i][j];
      }
    }
  }

  /**
   * @param destination 値を設定する行列
   * @param rowMin 行の始まり
   * @param rowMax 行の終り
   * @param columnIndex 指定する列を含む指数
   * @param source 代入する行列
   */
  public static void setSubMatrix(final boolean[][] destination, final int rowMin, final int rowMax, final int[] columnIndex, final boolean[][] source) {
    int idxcol = columnIndex.length;
    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;
    int rowSize = rowMax - rowMin + 1;

    if (mrow != rowSize || mcol != idxcol) {
      throw new MatrixSizeException(Messages.getString("BooleanMatrixUtil.20")); //$NON-NLS-1$
    }

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < idxcol; j++) {
        destination[i + rowMin][columnIndex[j]] = source[i][j];
      }
    }
  }

  /**
   * @param destination 値を設定する行列
   * @param rowIndex 指定する行を含む指数
   * @param columnIndex 指定する列を含む指数
   * @param source 代入する行列
   */
  public static void setSubMatrix(final boolean[][] destination, final int[] rowIndex, final int[] columnIndex, final boolean[][] source) {
    int idxcol1 = rowIndex.length;
    int idxcol2 = columnIndex.length;

    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;

    if (mrow != idxcol1 || mcol != idxcol2) {
      throw new MatrixSizeException(Messages.getString("BooleanMatrixUtil.21")); //$NON-NLS-1$
    }

    for (int i = 0; i < idxcol1; i++) {
      for (int j = 0; j < idxcol2; j++) {
        destination[rowIndex[i]][columnIndex[j]] = source[i][j];
      }
    }
  }

  /**
   * @param destination 成分を代入する行列
   * @param index 成分の番号を指定する指数
   * @param source 代入するベクトル
   */
  public static void setElements(final boolean[][] destination, final int[] index, final boolean[][] source) {
    int size = index.length;
    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;

    if (size != mrow * mcol) {
      throw new MatrixSizeException(Messages.getString("BooleanMatrixUtil.22")); //$NON-NLS-1$
    }

    int columnSize = destination[0].length;

    for (int i = 0; i < size; i++) {
      int row = (index[i]) / columnSize;
      int col = (index[i]) % columnSize;
      destination[row][col] = source[i / mcol][i % mcol];
    }
  }

  /**
   * 2個の行列を縦に接続した行列を生成します。
   * 
   * @param a1 上側の行列
   * @param a2 下側の行列
   * @return 接続された行列
   */
  public static boolean[][] appendDown(final boolean[][] a1, final boolean[][] a2) {
    int rowSize1 = a1.length;
    int columnSize1 = rowSize1 == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (columnSize1 != columnSize2 && rowSize1 != 0 && rowSize2 != 0) {
      throw new MatrixSizeException(Messages.getString("BooleanMatrixUtil.23")); //$NON-NLS-1$
    }

    int rowSize = rowSize1 + rowSize2;
    int columnSize;
    if (columnSize1 == 0) {
      columnSize = columnSize2;
    } else {
      columnSize = columnSize1;
    }

    boolean[][] ans = new boolean[rowSize][columnSize];
    
    for (int i = 0; i < rowSize1; i++) {
      boolean[] ansi = ans[i];
      boolean[] a1i = a1[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a1i[j];
      }
    }
    
    for (int i = rowSize1; i < rowSize; i++) {
      boolean[] ansi = ans[i];
      boolean[] a2i = a2[i - rowSize1];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a2i[j];
      }
    }

    return ans;
  }

  /**
   * ベクトルの右側に行列を接続した行列を生成します。
   * 
   * @param a1 左側のベクトル
   * @param a2 右側の行列
   * @return 接続された行列
   */
  public static boolean[][] appendRight(final boolean[][] a1, final boolean[][] a2) {
    int rowSize1 = a1.length;
    int columnSize1 = rowSize1 == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (rowSize1 != rowSize2 && rowSize1 != 0 && rowSize2 != 0) {
      throw new MatrixSizeException(Messages.getString("BooleanMatrixUtil.24")); //$NON-NLS-1$
    }

    int columnSize = columnSize1 + columnSize2;
    int rowSize;
    if (rowSize1 == 0) {
      rowSize = rowSize2;
    } else {
      rowSize = rowSize1;
    }

    boolean[][] ans = new boolean[rowSize][columnSize];
        
    for (int i = 0; i < rowSize1; i++) {
      boolean[] ansi = ans[i];
      boolean[] a1i = a1[i];
      System.arraycopy(a1i, 0, ansi, 0, columnSize1);
    }

    for (int i = 0; i < rowSize2; i++) {
      boolean[] ansi = ans[i];
      boolean[] a2i = a2[i];
      System.arraycopy(a2i, 0, ansi, columnSize1, columnSize2);
    }

    return ans;
  }

  /**
   * 2個の行列を成分毎に<code>operator</code>で指定された演算子で比較し, 計算結果を成分とする行列を生成します。
   * 
   * @param a1 第一行列
   * @param operator 比較演算子(".==", ".!=")
   * @param a2 第二行列
   * @return 計算結果を成分とする行列
   */
  public static boolean[][] compareElements(final boolean[][] a1, final String operator, final boolean[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;

    boolean[][] ans = new boolean[rowSize][columnSize];
    if (operator.equals(".!=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          ans[i][j] = !(a1[i][j] == a2[i][j]);
        }
      }
    } else if (operator.equals(".==")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          ans[i][j] = (a1[i][j] == a2[i][j]);
        }
      }
    } else {
      throw new IllegalArgumentException(Messages.getString("BooleanMatrixUtil.25")); //$NON-NLS-1$
    }
    return ans;
  }

  /**
   * 零行列(全ての成分がfalse)であるか判定します。
   * 
   * @param matrix 調べる行列
   * @return 零行列(全ての成分がfalse)ならtrue、そうでなければfalse
   */
  public static boolean isZero(final boolean[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    for (int i = 0; i < rowSize; i++) {
      boolean[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        if (matrixi[j]) {
          return false;
        }
      }
    }

    return true;
  }

//  /**
//   * 行列の全ての成分に零(false)を代入します。
//   * 
//   * @param matrix 零(false)を代入する行列
//   */
//  public static void setZero(final boolean[][] matrix) {
//    int rowSize = matrix.length;
//    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
//
//    for (int i = 0; i < rowSize; i++) {
//      boolean[] matrixi = matrix[i];
//      for (int j = 0; j < columnSize; j++) {
//        matrixi[j] = false;
//      }
//    }
//  }
}
