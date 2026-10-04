/**
 * $Id: IntMatrixUtil.java,v 1.63 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.matrix;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

import org.mklab.nfc.matx.MxDataHead;
import org.mklab.nfc.scalar.IntNumber;
import org.mklab.nfc.scalar.Scalar;
import org.mklab.nfc.util.EndianTransformer;


/**
 * int型を成分とする整数行列{@link IntMatrix}に関するユーティリティクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.63 $
 */
public final class IntMatrixUtil {

  /**
   * 新しく生成された<code>IntMatrixUtil</code>オブジェクトを初期化します。
   */
  private IntMatrixUtil() {
    // nothing to do
  }

  /**
   * 行列の全ての成分に整数を加えた行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param scalar 加える整数
   * @return 生成された行列
   */
  public static int[][] addElementWise(final int[][] matrix, final int scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j] + scalar;
      }
    }
    return ans;
  }

  /**
   * ベクトルの全ての成分に整数を加えたベクトルを生成します。
   * 
   * @param vector 対象となるベクトル
   * @param scalar 加える整数
   * @return 生成されたベクトル
   */
  public static int[] addElementWise(final int[] vector, final int scalar) {
    int rowSize = vector.length;
    int[] ans = new int[rowSize];

    for (int i = 0; i < rowSize; i++) {
      ans[i] = vector[i] + scalar;
    }
    return ans;
  }

//  /**
//   * 行列の全ての成分を１減少させた行列を生成します。
//   * 
//   * @param matrix 対象となる行列
//   * @return 生成された行列
//   */
//  public static int[][] decrement(final int[][] matrix) {
//    return addElementWise(matrix, -1);
//  }

  /**
   * ベクトルの全ての成分を１減少させたベクトルを生成します。
   * 
   * @param vector 対象となるベクトル
   * @return 生成されたベクトル
   */
  public static int[] decrement(final int[] vector) {
    return addElementWise(vector, -1);
  }

  /**
   * 行列を出力ストリームにMATフォーマットで出力します。
   * 
   * @param matrix 対象となる行列
   * @param output 出力ストリーム
   * @throws IOException ストリームに出力できない場合
   */
  public static void writeMatFormat(final int[][] matrix, final Writer output) throws IOException {
    //BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output, Charset.forName("UTF-8"))); //$NON-NLS-1$
    BufferedWriter bw = new BufferedWriter(output);
    int rowSize = matrix.length;
    int columnSize = matrix[0].length;
    final String lineSeparator = System.getProperty("line.separator"); //$NON-NLS-1$
    bw.write("# " + rowSize + " " + columnSize + lineSeparator); //$NON-NLS-1$ //$NON-NLS-2$
    for (int j = 0; j < columnSize; j++) {
      for (int i = 0; i < rowSize; i++) {
        bw.write(String.valueOf(matrix[i][j]));
        if (i < rowSize - 1) {
          bw.write(" "); //$NON-NLS-1$
        }
      }
      bw.write(lineSeparator);
    }
    bw.flush();
  }

  /**
   * 行列の符号を反転した行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @return 符号を反転した行列
   */
  public static int[][] unaryMinus(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      int[] matrixi = matrix[i];
      int[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = -matrixi[j];
      }
    }
    return ans;
  }

  /**
   * 実行列を整数行列に変換(最も近い整数に丸める)します。
   * 
   * @param matrix 対象となる実行列
   * @return 生成された整数行列
   */
  public static int[][] createArray(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = (int)matrixi[j];
      }
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
  public static int[][] createUnit(final int rowSize, final int columnSize) {
    int[][] ans = new int[rowSize][columnSize];

    int size = rowSize < columnSize ? rowSize : columnSize;

    for (int i = 0; i < size; i++) {
      ans[i][i] = 1;
    }

    return ans;
  }

  /**
   * 零行列であるか判定します。
   * 
   * @param matrix 調べる行列
   * @param tolerance 許容誤差
   * @return 零行列ならtrue、そうでなければfalse
   */
  public static boolean isZero(final int[][] matrix, final double tolerance) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    for (int i = 0; i < rowSize; i++) {
      int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        if (Math.abs(matrixi[j]) > tolerance) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * 単位行列であるか判定します。
   * 
   * @param matrix 調べる行列
   * @param tolerance 許容誤差
   * @return 単位行列ならばtrue、そうでなければfalse
   */
  public static boolean isUnit(final int[][] matrix, final double tolerance) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize != columnSize) {
      return false;
    }
    
    if (rowSize == 0 && columnSize == 0) {
      return false;
    }

    for (int i = 0; i < rowSize; i++) {
      int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        if (i == j) {
          if (Math.abs(matrixi[j] - 1) > tolerance) {
            return false;
          }
        } else {
          if (Math.abs(matrixi[j]) > tolerance) {
            return false;
          }
        }
      }
    }

    return true;
  }

  /**
   * <code>newRowSize</code>*<code>newColSize</code>にサイズ変更します。
   * 
   * <p>{@link #reshape}とは異なり、成分位置の変更はせず, 自身より大きなサイズに変更する時は,0が埋められ、 自身より小さなサイズに変更する時は余分な成分は切り取られます。
   * 
   * @param matrix 対象となる行列
   * @param newRowSize 新しい行の数
   * @param newColSize 新しいレスう
   * @return サイズ変更後の行列
   */
  public static int[][] resize(final int[][] matrix, final int newRowSize, final int newColSize) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int[][] ans = new int[newRowSize][newColSize];
    int re = Math.min(newRowSize, rowSize);
    int ce = Math.min(newColSize, columnSize);

    // MatrixUtil.areaCopy(ans, 0, 0, matrix, 0, 0, re, ce);
    for (int i = 0; i < re; i++) {
      for (int j = 0; j < ce; j++) {
        ans[i][j] = matrix[i][j];
      }
      for (int j = ce; j < newColSize; j++) {
        ans[i][j] = 0;
      }
    }
    for (int i = re; i < newRowSize; i++) {
      for (int j = 0; j < ce; j++) {
        ans[i][j] = 0;
      }
      for (int j = ce; j < newColSize; j++) {
        ans[i][j] = 0;
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
  public static int[][] removeColumnVectors(final int[][] matrix, final int columnMin, final int columnMax) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int newColumnSize = columnSize - (columnMax - columnMin + 1);

    int[][] ans = new int[rowSize][newColumnSize];
    for (int i = 0; i < rowSize; i++) {
      int[] matrixi = matrix[i];
      int[] ansi = ans[i];
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
   * @param columnIndex 削除する列の番号
   * @return 列を削除された行列
   */
  public static int[][] removeColumnVectors(final int[][] matrix, final int[] columnIndex) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int columnSize2 = columnIndex.length;

    // 削除すべき列番号にtrue
    boolean[] removedColumn = new boolean[columnSize];
    for (int i = 0; i < columnSize2; i++) {
      removedColumn[columnIndex[i]] = true;
    }

    // 新しい列の数
    int newColumnSize = 0;
    for (int i = 0; i < columnSize; i++) {
      if (removedColumn[i]) {
        continue;
      }
      newColumnSize++;
    }

    int[][] ans = new int[rowSize][newColumnSize];
    for (int i = 0; i < rowSize; i++) {
      int[] matrixi = matrix[i];
      int[] ansi = ans[i];
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
   * @param rowMin 開始行
   * @param rowMax 終了行
   * @return 行を削除された行列
   */
  public static int[][] removeRowVectors(final int[][] matrix, final int rowMin, final int rowMax) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int newRowSize = rowSize - (rowMax - rowMin + 1);

    int[][] ans = new int[newRowSize][columnSize];

    for (int i = 0, i2 = 0; i < rowSize; i++) {
      if (rowMin <= i && i <= rowMax) {
        continue;
      }
      int[] matrixi = matrix[i];
      int[] ansi = ans[i2];
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
   * @param rowIndex 削除する行の番号
   * @return 行を削除された行列
   */
  public static int[][] removeRowVectors(final int[][] matrix, final int[] rowIndex) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int size = rowIndex.length;

    // 削除すべき行番号にtrue
    boolean[] removedRow = new boolean[rowSize];
    for (int i = 0; i < size; i++) {
      removedRow[rowIndex[i]] = true;
    }

    // 新しい行の数
    int newRowSize = 0;
    for (int i = 0; i < rowSize; i++) {
      if (removedRow[i]) {
        continue;
      }
      newRowSize++;
    }

    int[][] ans = new int[newRowSize][columnSize];
    for (int i = 0, i2 = 0; i < rowSize; i++) {
      if (removedRow[i]) {
        continue;
      }
      int[] matrixi = matrix[i];
      int[] ansi = ans[i2];
      System.arraycopy(matrixi, 0, ansi, 0, columnSize);
      i2++;
    }

    return ans;
  }

//  /**
//   * double型の1次元配列を返します。
//   * 
//   * @param elements 元のデータ
//   * @return double型の1次元配列
//   */
//  public static double[] toDouble(final int[] elements) {
//    final int rowSize = elements.length;
//
//    final double[] matrix = new double[rowSize];
//
//    for (int i = 0; i < rowSize; i++) {
//      matrix[i] = elements[i];
//    }
//
//    return matrix;
//  }
  
  /**
   * double型の2次元配列を返します。
   * 
   * @param elements 元のデータ
   * @return double型の2次元配列
   */
  public static double[][] toDouble(final int[][] elements) {
    final int rowSize = elements.length;
    final int columnSize = rowSize == 0 ? 0 : elements[0].length;

    final double[][] matrix = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j] = elements[i][j];
      }
    }

    return matrix;
  }
  
  /**
   * 行列をMMフォーマットの文字列に変換します。
   * 
   * @param matrix 対象となる行列
   * @return MMフォーマットの文字列
   */
  public static String toMmString(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    StringBuffer sb = new StringBuffer();
    String newLine = System.getProperty("line.separator"); //$NON-NLS-1$

    if (rowSize == 0 || columnSize == 0) {
      return "[[]]"; //$NON-NLS-1$
    }

    if (columnSize == 1 && rowSize != 1) {
      return toMmString(transpose(matrix)) + "'"; //$NON-NLS-1$
    }

    if (rowSize != 1) {
      sb.append("["); //$NON-NLS-1$
    }
    
    final int displayColumnSize = 4;

    for (int i = 0; i < rowSize; i++) {
      if (i != 0) {
        sb.append(" "); //$NON-NLS-1$
      }

      for (int k = 0; k < columnSize;) {
        if (k == 0) {
          sb.append("["); //$NON-NLS-1$
        } else {
          sb.append(" "); //$NON-NLS-1$
          if (rowSize != 1) {
            sb.append(" "); //$NON-NLS-1$
          }
        }

        int j;
        for (j = k; j < k + displayColumnSize && j < columnSize; j++) {
          sb.append(matrix[i][j]);
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
          sb.append(newLine);
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
   * 各成分の有限性の真偽を成分にもつ行列を返します。
   * 
   * @param matrix 対象となる行列
   * @return 有限性のboolean行列
   */
  public static boolean[][] isFiniteElementWise(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      boolean[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = true;
      }
    }
    return ans;
  }

  /**
   * 各成分の無限性の真偽を成分にもつ行列を返します。
   * 
   * @param matrix 対象となる行列
   * @return 無限性のboolean行列
   */
  public static boolean[][] isInfiniteElementWise(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    return new boolean[rowSize][columnSize];
  }

  /**
   * 各成分の非数性の真偽を成分にもつ行列を返します。
   * 
   * @param matrix 対象となる行列
   * @return 非数性のboolean行列
   */
  public static boolean[][] isNanElementWise(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    return new boolean[rowSize][columnSize];
  }

  /**
   * rowSize*columnSizeの全成分1の行列を生成します。
   * 
   * @param rowSize 行番号の指定
   * @param columnSize 列番号の指定
   * @return rowSize*columnSizeの全成分1の行列
   */
  public static int[][] ones(final int rowSize, final int columnSize) {
    int[][] ans = new int[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = 1;
      }
    }
    return ans;
  }

  /**
   * 対角行列を生成する。
   * 
   * @param diagonalElements 対角成分
   * @return 対角行列
   */
  public static int[][] diagonal(final int[] diagonalElements) {
    int size = diagonalElements.length;
    int[][] element = new int[size][size];
    for (int i = 0; i < size; i++) {
      element[i][i] = diagonalElements[i];
    }
    return element;
  }

  /**
   * rowSize*columnSizeの単位行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return rowSize*columnSizeの単位行列
   */
  public static int[][] unit(final int rowSize, final int columnSize) {
    int[][] ans = new int[rowSize][columnSize];
    int size = rowSize < columnSize ? rowSize : columnSize;
    for (int i = 0; i < size; i++) {
      ans[i][i] = 1;
    }
    return ans;
  }

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
  public static void setSubMatrix(final int[][] destination, final int rowMin, final int rowMax, final int columnMin, final int columnMax, final int[][] source) {
    int rowSize = source.length;
    int columnSize = rowSize == 0 ? 0 : source[0].length;

    if (rowSize != rowMax - rowMin + 1 || columnSize != columnMax - columnMin + 1) {
      throw new MatrixSizeException(MatrixSizeException.INCORRECT_SIZE);
    }

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        destination[rowMin + i][columnMin + j] = source[i][j];
      }
    }
  }

  /**
   * @param destination 値を設定する行列
   * @param rowIndex 指定する行を含む指数
   * @param columnMin 列の始まり
   * @param columnMax 列の終り
   * @param source 代入する行列
   */
  public static void setSubMatrix(final int[][] destination, final int[] rowIndex, final int columnMin, final int columnMax, final int[][] source) {
    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;
    int idxcol = rowIndex.length;
    int columnSize = columnMax - columnMin + 1;

    if (mrow != idxcol || mcol != columnSize) {
      throw new MatrixSizeException(Messages.getString("IntMatrixUtil.14")); //$NON-NLS-1$
    }

    for (int i = 0; i < idxcol; i++) {
      for (int j = 0; j < columnSize; j++) {
        destination[rowIndex[i]][j + columnMin] = source[i][j];
      }
    }
  }

  /**
   * @param destination 値を設定する行列
   * @param rowIndex 指定する行を含む指数
   * @param columnIndex 指定する列を含む指数
   * @param source 代入する行列
   */
  public static void setSubMatrix(final int[][] destination, final int[] rowIndex, final int[] columnIndex, final int[][] source) {
    int idxcol1 = rowIndex.length;
    int idxcol2 = columnIndex.length;

    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;

    if (mrow != idxcol1 || mcol != idxcol2) {
      throw new MatrixSizeException(Messages.getString("IntMatrixUtil.15")); //$NON-NLS-1$
    }

    for (int i = 0; i < idxcol1; i++) {
      for (int j = 0; j < idxcol2; j++) {
        destination[rowIndex[i]][columnIndex[j]] = source[i][j];
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
  public static void setSubMatrix(final int[][] destination, final int rowMin, final int rowMax, final int[] columnIndex, final int[][] source) {
    int idxcol = columnIndex.length;
    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;
    int rowSize = rowMax - rowMin + 1;

    if (mrow != rowSize || mcol != idxcol) {
      throw new MatrixSizeException(Messages.getString("IntMatrixUtil.16")); //$NON-NLS-1$
    }

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < idxcol; j++) {
        destination[i + rowMin][columnIndex[j]] = source[i][j];
      }
    }
  }

  /**
   * 与えられた位置にベクトルを代入します。
   * 
   * @param destination 値を設定するベクトル
   * @param min 成分の始まり
   * @param max 成分の終り
   * @param source 代入するベクトル
   */
  public static void setSubVector(final int[] destination, final int min, final int max, final int[] source) {
    if (max - min + 1 != source.length) {
      throw new MatrixSizeException(Messages.getString("IntMatrixUtil.17")); //$NON-NLS-1$
    }
    System.arraycopy(source, 0, destination, min, source.length);
  }

  /**
   * <code>index</code>で指定した各成分に行列<code>source</code>の成分を代入します。
   * 
   * @param destination 成分を代入する行列
   * @param index 成分の番号を指定する指数
   * @param source 代入するベクトル
   */
  public static void setElements(final int[][] destination, final int[] index, final int[][] source) {
    int size = index.length;
    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;

    if (size != mrow * mcol) {
      throw new MatrixSizeException(Messages.getString("IntMatrixUtil.17")); //$NON-NLS-1$
    }

    int columnSize = destination[0].length;

    for (int i = 0; i < size; i++) {
      int row = (index[i]) / columnSize;
      int col = (index[i]) % columnSize;
      destination[row][col] = source[i / mcol][i % mcol];
    }
  }

  /**
   * <code>from</code>から<code>to</code>までの<code>by</code>飛びの整数を成分にもつ行ベクトルを返します。
   * 
   * @param from 始点
   * @param to 終点
   * @param by 間隔
   * @return fromからtoまでのby飛びの整数をもつベクトル
   */
  public static int[] series(final int from, final int to, final int by) {
    int size = ((to - from) / by) + 1;

    int[] ans = new int[size];
    for (int i = 0; i < size; i++) {
      ans[i] = i * by + from;
    }
    return ans;
  }

  /**
   * 行列の和を求めます。
   * 
   * @param a1 加えられる行列
   * @param a2 加える行列
   * @return 行列の和
   */
  public static int[][] add(final int[][] a1, final int[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;

    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      int[] a1i = a1[i];
      int[] a2i = a2[i];
      int[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a1i[j] + a2i[j];
      }
    }
    return ans;
  }

  /**
   * 行列の差を求めます。
   * 
   * @param a1 引かれる行列
   * @param a2 引く行列
   * @return 行列の差
   */
  public static int[][] subtract(final int[][] a1, final int[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      int[] a1i = a1[i];
      int[] a2i = a2[i];
      int[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a1i[j] - a2i[j];
      }
    }
    return ans;
  }

  /**
   * 行列の全ての成分から整数を引いた行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param scalar 引く整数
   * @return 生成された行列
   */
  public static int[][] subtractElementWise(final int[][] matrix, final int scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j] - scalar;
      }
    }
    return ans;
  }

  /**
   * 行列に整数を掛けます。
   * 
   * @param matrix 対象となる行列
   * @param scalar 乗じる整数
   * @return 整数を掛けた結果
   */
  public static int[][] multiply(final int[][] matrix, final int scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j] * scalar;
      }
    }
    return ans;
  }

  /**
   * 2個の行列の積を求めます。
   * 
   * @param a1 掛けられる行列
   * @param a2 掛ける行列
   * @return 行列の積
   */
  public static int[][] multiply(final int[][] a1, final int[][] a2) {
    int rowSize1 = a1.length;
    int columnSize1 = rowSize1 == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (columnSize1 != rowSize2) {
      throw new MatrixSizeException(Messages.getString("IntMatrixUtil.18")); //$NON-NLS-1$
    }

    int[][] ans = new int[rowSize1][columnSize2];

    for (int i = 0; i < rowSize1; i++) {
      int[] a1i = a1[i];
      int[] ansi = ans[i];
      for (int j = 0; j < columnSize2; j++) {
        int d = 0;
        for (int k = 0; k < columnSize1; k++) {
          d += a1i[k] * a2[k][j];
        }
        ansi[j] = d;
      }
    }
    return ans;
  }

  /**
   * 成分毎に乗算を行います。
   * 
   * @param a1 掛けられる行列
   * @param a2 掛ける行列
   * @return 掛けた結果の行列
   */
  public static int[][] multiplyElementWise(final int[][] a1, final int[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      int[] a1i = a1[i];
      int[] a2i = a2[i];
      int[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a1i[j] * a2i[j];
      }
    }
    return ans;
  }

  /**
   * 全ての成分の累乗を成分とする行列を生成します。
   * 
   * @param matrix 元の行列
   * @param scalar 累乗の指数
   * @return 成分の累乗を成分とする行列
   */
  public static int[][] powerElementWise(final int[][] matrix, final int scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int[][] ans = new int[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = (int)(Math.pow(matrixi[j], scalar));
      }
    }
    return ans;
  }

  /**
   * 行列の成分毎に累乗を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param a1 累乗の対象となる値を成分とする行列
   * @param a2 累乗の指数(実数)を成分とする行列
   * @return 生成された行列
   */
  public static int[][] powerElementWise(final int[][] a1, final int[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] a1i = a1[i];
      int[] a2i = a2[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = (int)(Math.pow(a1i[j], a2i[j]));
      }
    }
    return ans;
  }

  /**
   * 1個の整数について、行列の各成分の累乗を求めます。
   * 
   * @param scalar 累乗の対象
   * @param matrix 累乗の指数を成分とする行列
   * @return 累乗の結果
   */
  public static int[][] powerElementWise(final int scalar, final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] mmi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = (int)(Math.pow(scalar, mmi[j]));
      }
    }
    return ans;
  }

  /**
   * 全ての成分を整数で割る。
   * 
   * @param matrix 対象となる行列
   * @param scalar 割る整数
   * @return 計算結果
   */
  public static int[][] divide(final int[][] matrix, final int scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int[][] ans = new int[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j] / scalar;
      }
    }
    return ans;
  }

  /**
   * 2個の配列の成分が全て等しか判定します。
   * 
   * @param a1 第一行列
   * @param a2 第二行列
   * @return 配列の成分が等しければtrue、そうでなければfalse
   */
  public static boolean equals(final int[][] a1, final int[][] a2) {
    int rowSize1 = a1.length;
    int columnSize1 = rowSize1 == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (rowSize1 != rowSize2 || columnSize1 != columnSize2) {
      return false;
    }

    for (int i = 0; i < rowSize1; i++) {
      int[] a1i = a1[i];
      int[] a2i = a2[i];
      for (int j = 0; j < columnSize1; j++) {
        if (a1i[j] != a2i[j]) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * 2個の配列の成分が全て等しか判定します。
   * 
   * @param a1 第一行列
   * @param a2 第二行列
   * @param tolerance 許容誤差
   * @return 配列の成分が等しければtrue、そうでなければfalse
   */
  public static boolean equals(final int[][] a1, final int[][] a2, final double tolerance) {
    int rowSize1 = a1.length;
    int columnSize1 = rowSize1 == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (rowSize1 != rowSize2 || columnSize1 != columnSize2) {
      return false;
    }

    for (int i = 0; i < rowSize1; i++) {
      for (int j = 0; j < columnSize1; j++) {
        int[] a1i = a1[i];
        int[] a2i = a2[i];
        if (Math.abs(a1i[j] - a2i[j]) > tolerance) {
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
  public static int[][] transpose(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int[][] ans = new int[columnSize][rowSize];
    for (int i = 0; i < rowSize; i++) {
      int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ans[j][i] = matrixi[j];
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
  public static int[][] getSubMatrix(final int[][] matrix, final int rowMin, final int rowMax, final int columnMin, final int columnMax) {
    int rowSize = rowMax - rowMin + 1;
    int columnSize = columnMax - columnMin + 1;
    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] matrixi = matrix[i + rowMin];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j + columnMin];
      }
    }
    return ans;
  }

  /**
   * 部分ベクトルを生成します。
   * 
   * @param vector 元のベクトル
   * @param index 該当する行の番号
   * @return 部分ベクトル
   */
  public static int[] getSubVector(final int[] vector, final int[] index) {
    int size = index.length;
    if (size == 0) {
      return new int[0];
    }

    int[] ans = new int[size];

    for (int i = 0; i < size; i++) {
      ans[i] = vector[index[i]];
    }

    return ans;
  }

//  /**
//   * 部分ベクトルを生成します。
//   * 
//   * @param vector 元のベクトル
//   * @param rowMin 開始位置
//   * @param rowMax 終了位置
//   * @return 部分ベクトル
//   */
//  private static int[] getSubVector(final int[] vector, final int rowMin, final int rowMax) {
//    int size = rowMax - rowMin + 1;
//    int[] ans = new int[size];
//    System.arraycopy(vector, rowMin, ans, 0, size);
//    return ans;
//  }

//  /**
//   * 部分行列を生成します。
//   * 
//   * @param matrix 元の行列
//   * @param rowIndex 該当する行の番号
//   * @param column 列番号
//   * @return 部分行列
//   */
//  public static int[][] getSubMatrix(final int[][] matrix, final int[] rowIndex, final int column) {
//    int rowSize = rowIndex.length;
//    int[][] ans = new int[rowSize][1];
//
//    for (int i = 0; i < rowSize; i++) {
//      ans[i][0] = matrix[rowIndex[i]][column];
//    }
//    return ans;
//  }

  /**
   * 部分行列を生成します。
   * 
   * @param matrix 元の行列
   * @param rowIndex 該当する行の番号
   * @param columnMin 始まりの列
   * @param columnMax 終わりの列
   * @return 部分行列
   */
  public static int[][] getSubMatrix(final int[][] matrix, final int[] rowIndex, final int columnMin, final int columnMax) {
    int rowSize = rowIndex.length;
    int columnSize = columnMax - columnMin + 1;

    int[][] ans = new int[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] matrixi = matrix[rowIndex[i]];
      System.arraycopy(matrixi, columnMin, ansi, 0, columnSize);
    }
    return ans;
  }

//  /**
//   * 部分行列を生成します。
//   * 
//   * @param matrix 元の行列
//   * @param row 行の番号
//   * @param columnIndex 該当する列の番号
//   * @return 部分行列
//   */
//  public static int[][] getSubMatrix(final int[][] matrix, final int row, final int[] columnIndex) {
//    int columnSize = columnIndex.length;
//
//    int[][] ans = new int[1][columnSize];
//    int[] ans0 = ans[0];
//    int[] matrix0 = matrix[row];
//
//    for (int i = 0; i < columnSize; i++) {
//      ans0[i] = matrix0[columnIndex[i]];
//    }
//    return ans;
//  }

  /**
   * 部分行列を生成します。
   * 
   * @param matrix 元の行列
   * @param rowMin 始まりの行
   * @param rowMax 終わりの行
   * @param columnIndex 該当する列の番号
   * @return 部分行列
   */
  public static int[][] getSubMatrix(final int[][] matrix, final int rowMin, final int rowMax, final int[] columnIndex) {
    int rowSize = rowMax - rowMin + 1;
    int columnSize = columnIndex.length;
    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] matrixi = matrix[i + rowMin];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[columnIndex[j]];
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
  public static int[][] getSubMatrix(final int[][] matrix, final int[] rowIndex, final int[] columnIndex) {
    int rowSize = rowIndex.length;
    int columnSize = columnIndex.length;

    int[][] ans = new int[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] matrixi = matrix[rowIndex[i]];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[columnIndex[j]];
      }
    }
    return ans;
  }

  /**
   * 2個の行列を縦に接続した行列を生成します。
   * 
   * @param a1 上側の行列
   * @param a2 下側の行列
   * @return 接続された行列
   */
  public static int[][] appendDown(final int[][] a1, final int[][] a2) {
    int rowSize1 = a1.length;
    int columnSize1 = rowSize1 == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (columnSize1 != columnSize2 && rowSize1 != 0 && rowSize2 != 0) {
      throw new MatrixSizeException(MatrixSizeException.INCONSISTENT_COLUMN_NUMBER);
    }

    int rowSize = rowSize1 + rowSize2;
    int columnSize;
    if (columnSize1 == 0) {
      columnSize = columnSize2;
    } else {
      columnSize = columnSize1;
    }

    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < rowSize1; i++) {
      int[] ansi = ans[i];
      int[] a1i = a1[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a1i[j];
      }
    }
    
    for (int i = rowSize1; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] a2i = a2[i - rowSize1];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a2i[j];
      }
    }

    return ans;
  }

  /**
   * 2個の行列を横に接続した行列を生成します。
   * 
   * @param a1 左側の行列
   * @param a2 右側の行列
   * @return 接続された行列
   */
  public static int[][] appendRight(final int[][] a1, final int[][] a2) {
    int rowSize1 = a1.length;
    int columnSize1 = rowSize1 == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (rowSize1 != rowSize2 && rowSize1 != 0 && rowSize2 != 0) {
      throw new MatrixSizeException(MatrixSizeException.INCONSISTENT_ROW_NUMBER);
    }

    int columnSize = columnSize1 + columnSize2;
    int rowSize;
    if (rowSize1 == 0) {
      rowSize = rowSize2;
    } else {
      rowSize = rowSize1;
    }

    int[][] ans = new int[rowSize][columnSize];
    
    for (int i = 0; i < rowSize1; i++) {
      int[] ansi = ans[i];
      int[] a1i = a1[i];
      System.arraycopy(a1i, 0, ansi, 0, columnSize1);
    }

    for (int i = 0; i < rowSize2; i++) {
      int[] ansi = ans[i];
      int[] a2i = a2[i];
      System.arraycopy(a2i, 0, ansi, columnSize1, columnSize2);
    }
    
    return ans;
  }

  /**
   * 行列の複製を生成します。
   * 
   * @param matrix 複製の元となる行列
   * @return 複製された行列
   */
  public static int[][] clone(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] matrixi = matrix[i];
      System.arraycopy(matrixi, 0, ansi, 0, columnSize);
    }
    return ans;
  }

  /**
   * column1列とcolumn2列を入れ替えます。
   * 
   * @param matrix 対象の行列
   * @param column1 指定列１
   * @param column2 指定列２
   */
  public static void exchangeColumn(final int[][] matrix, final int column1, final int column2) {
    if (column1 == column2) {
      return;
    }

    int rowSize = matrix.length;

    for (int i = 0; i < rowSize; i++) {
      int tmp = matrix[i][column1];
      matrix[i][column1] = matrix[i][column2];
      matrix[i][column2] = tmp;
    }
  }

  /**
   * row1行とrow2行を入れ替えます。
   * 
   * @param matrix 対象の行列
   * @param row1 指定行１
   * @param row2 指定行２
   */
  public static void exchangeRow(final int[][] matrix, final int row1, final int row2) {
    if (row1 == row2) {
      return;
    }

    int columnSize = matrix.length;
    int[] matrixRow1 = matrix[row1];
    int[] matrixRow2 = matrix[row2];

    for (int i = 0; i < columnSize; i++) {
      int tmp = matrixRow1[i];
      matrixRow1[i] = matrixRow2[i];
      matrixRow2[i] = tmp;
    }
  }

  /**
   * 行列の成分をコピーします。
   * 
   * @param source コピー元
   * @param destination コピー先
   */
  public static void copy(final int[][] source, final int[][] destination) {
    int rowSize = destination.length;
    int columnSize = rowSize == 0 ? 0 : destination[0].length;

    for (int i = 0; i < rowSize; i++) {
      int[] toi = destination[i];
      int[] fromi = source[i];
      System.arraycopy(fromi, 0, toi, 0, columnSize);
    }
  }

  /**
   * 対角成分からなる列ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 対角成分からなる列ベクトル
   */
  public static int[][] diagonalToVector(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int min = Math.min(rowSize, columnSize);
    int[][] ans = new int[min][1];
    for (int i = 0; i < min; i++) {
      ans[i][0] = matrix[i][i];
    }
    return ans;
  }

  /**
   * ベクトルの成分を対角成分とする対角行列を生成します。
   * 
   * @param vector 対象となるベクトル
   * @return 対角行列
   */
  public static int[][] vectorToDiagonal(final int[] vector) {
    int size = vector.length;
    int[][] ans = new int[size][size];

    for (int i = 0; i < size; i++) {
      ans[i][i] = vector[i];
    }
    return ans;
  }

  /**
   * 行列の成分を変えずに、行列の大きさ(行の数と列の数)を変形します。
   * 
   * @param matrix 対象となる行列
   * @param newRowSize 変更後の行の数
   * @param newColumnSize 変更後の列の数
   * @return 変形した行列
   */
  public static int[][] reshape(final int[][] matrix, final int newRowSize, final int newColumnSize) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    if (rowSize * columnSize != newRowSize * newColumnSize) {
      throw new MatrixSizeException(MatrixSizeException.INCONSISTENT_SIZE);
    }

    int[][] ans = new int[newRowSize][newColumnSize];
    int num = 0;
    for (int i = 0; i < newRowSize; i++) {
      int[] ansi = ans[i];
      for (int j = 0; j < newColumnSize; j++) {
        ansi[j] = matrix[num / columnSize][num % columnSize];
        num++;
      }
    }
    return ans;
  }

  /**
   * 行毎に昇順に並び替えた行列と元の位置を示す指数を返します。
   * 
   * @param matrix 対象となる行列
   * @return 行毎に昇順に並び替えた行列と元の位置を示す指数
   */
  public static IndexedIntElements sortRowWise(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] idx1 = new int[rowSize][columnSize];

    int[][] m1 = clone(matrix);
    for (int i = 0; i < rowSize; i++) {
      int[] idx1i = idx1[i];
      for (int j = 0; j < columnSize; j++) {
        idx1i[j] = j + 1;
      }
      IntMatrixUtil.quickSort(m1[i], idx1[i], 0, columnSize - 1);
    }
    return new IndexedIntElements(m1, idx1);
  }

  /**
   * 列毎に昇順に並び替えた行列と元の位置を示す指数を返します。
   * 
   * @param matrix 対象となる行列
   * @return 列毎に昇順に並び替えた行列と元の位置を示す指数
   */
  public static IndexedIntElements sortColumnWise(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] idx1 = new int[columnSize][rowSize];

    int[][] m1 = transpose(matrix);
    for (int i = 0; i < columnSize; i++) {
      int[] idx1i = idx1[i];
      for (int j = 0; j < rowSize; j++) {
        idx1i[j] = j + 1;
      }
      IntMatrixUtil.quickSort(m1[i], idx1[i], 0, rowSize - 1);
    }
    return new IndexedIntElements(transpose(m1), IntMatrixUtil.transpose(idx1));
  }

  /**
   * 行列の全ての成分も和を求めます。
   * 
   * @param matrix 対象となる行列
   * @return 行列の全ての成分も和
   */
  public static int sum(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int sum = 0;

    for (int i = 0; i < rowSize; i++) {
      int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        sum += matrixi[j];
      }
    }
    return sum;
  }

  /**
   * 列毎に全ての成分の和を計算し、計算結果を成分とする行ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行ベクトル
   */
  public static int[][] sumColumnWise(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] ans = new int[1][columnSize];

    for (int i = 0; i < columnSize; i++) {
      int sum = 0;
      for (int j = 0; j < rowSize; j++) {
        sum += matrix[j][i];
      }
      ans[0][i] = sum;
    }

    return ans;
  }

  /**
   * 行毎に全ての成分の和を計算し、計算結果を成分とする列ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする列ベクトル
   */
  public static int[][] sumRowWise(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] ans = new int[rowSize][1];

    for (int i = 0; i < rowSize; i++) {
      int sum = 0;
      int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        sum += matrixi[j];
      }
      ans[i][0] = sum;
    }

    return ans;
  }

  /**
   * 全ての成分の累積和を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行列
   */
  public static int[][] cumulativeSum(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] ans = new int[rowSize][columnSize];
    int sum = 0;

    for (int i = 0; i < rowSize; i++) {
      int[] matrixi = matrix[i];
      int[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        sum += matrixi[j];
        ansi[j] = sum;
      }
    }
    return ans;
  }

  /**
   * 行毎に累積和を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行列
   */
  public static int[][] cumulativeSumRowWise(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      int sum = 0;
      int[] matrixi = matrix[i];
      int[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        sum += matrixi[j];
        ansi[j] = sum;
      }
    }

    return ans;
  }

  /**
   * 列毎に累積和を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行列
   */
  public static int[][] cumulativeSumColumnWise(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < columnSize; i++) {
      int sum = 0;
      for (int j = 0; j < rowSize; j++) {
        sum += matrix[j][i];
        ans[j][i] = sum;
      }
    }

    return ans;
  }

  /**
   * 全ての成分の累積積を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行列
   */
  public static int[][] cumulativeProduct(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] ans = new int[rowSize][columnSize];

    int prod = 1;
    for (int i = 0; i < rowSize; i++) {
      int[] matrixi = matrix[i];
      int[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        prod *= matrixi[j];
        ansi[j] = prod;
      }
    }
    return ans;
  }

  /**
   * 行毎に累積積を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行列
   */
  public static int[][] cumulativeProductRowWise(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      int prod = 1;
      int[] matrixi = matrix[i];
      int[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        prod *= matrixi[j];
        ansi[j] = prod;
      }
    }

    return ans;
  }

  /**
   * 列毎に累積積を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行列
   */
  public static int[][] cumulativeProductColumnWise(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < columnSize; i++) {
      int prod = 1;
      for (int j = 0; j < rowSize; j++) {
        prod *= matrix[j][i];
        ans[j][i] = prod;
      }
    }

    return ans;
  }

  /**
   * 行列の全ての成分の積を求めます。
   * 
   * @param matrix 対象となる行列
   * @return 行列の全ての成分の積
   */
  public static int product(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int ans = 1;
    for (int i = 0; i < rowSize; i++) {
      int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ans *= matrixi[j];
      }
    }
    return ans;
  }

  /**
   * 行毎に全ての成分の積を計算し、計算結果を成分とする列ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする列ベクトル
   */
  public static int[][] productRowWise(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] ans = new int[rowSize][1];

    for (int i = 0; i < rowSize; i++) {
      int prod = 1;
      int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        prod *= matrixi[j];
      }
      ans[i][0] = prod;
    }

    return ans;
  }

  /**
   * 列毎に全ての成分の積を計算し、計算結果を成分とする行ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行ベクトル
   */
  public static int[][] productColumnWise(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] ans = new int[1][columnSize];

    for (int i = 0; i < columnSize; i++) {
      int prod = 1;
      for (int j = 0; j < rowSize; j++) {
        prod *= matrix[j][i];
      }
      ans[0][i] = prod;
    }

    return ans;
  }

  /**
   * 行毎に全ての成分の平均値を計算し、計算結果を成分とする列ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする列ベクトル
   */
  public static int[][] meanRowWise(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    return divide(sumRowWise(matrix), columnSize);
  }

  /**
   * 列毎に全ての成分の平均値を計算し、計算結果を成分とする行ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行ベクトル
   */
  public static int[][] meanColumnWise(final int[][] matrix) {
    int rowSize = matrix.length;
    return divide(sumColumnWise(matrix), rowSize);
  }

  /**
   * 各成分とvalueをoperatorで指定された演算子で比較し、計算結果を成分とする行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param opponent 比較対象
   * @return 計算結果を成分とする行列
   */
  public static boolean[][] compareElements(final int[][] matrix, final String operator, final double opponent) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    boolean[][] ans = new boolean[rowSize][columnSize];
    if (operator.equals(".>")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = matrixi[j] > opponent;
        }
      }
    } else if (operator.equals(".>=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = matrixi[j] >= opponent;
        }
      }
    } else if (operator.equals(".<")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = matrixi[j] < opponent;
        }
      }
    } else if (operator.equals(".<=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = matrixi[j] <= opponent;
        }
      }
    } else if (operator.equals(".==")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = matrixi[j] == opponent;
        }
      }
    } else if (operator.equals(".!=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = matrixi[j] != opponent;
        }
      }
    } else {
      throw new IllegalArgumentException(Messages.getString("IntMatrixUtil.19")); //$NON-NLS-1$
    }
    return ans;
  }

  /**
   * 各成分とvalueをoperatorで指定された演算子で比較し、計算結果を成分とする行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param opponent 比較対象
   * @return 計算結果を成分とする行列
   */
  public static boolean[][] compareElements(final int[][] matrix, final String operator, final Scalar<?,?> opponent) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    boolean[][] ans = new boolean[rowSize][columnSize];
    if (operator.equals(".>")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = opponent.compare(".<", matrixi[j]); //$NON-NLS-1$
        }
      }
    } else if (operator.equals(".>=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = opponent.compare(".<=", matrixi[j]); //$NON-NLS-1$
        }
      }
    } else if (operator.equals(".<")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = opponent.compare(".>", matrixi[j]); //$NON-NLS-1$
        }
      }
    } else if (operator.equals(".<=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = opponent.compare(".>=", matrixi[j]); //$NON-NLS-1$
        }
      }
    } else if (operator.equals(".==")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = opponent.compare(".==", matrixi[j]); //$NON-NLS-1$
        }
      }
    } else if (operator.equals(".!=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = opponent.compare(".!=", matrixi[j]); //$NON-NLS-1$
        }
      }
    } else {
      throw new IllegalArgumentException(Messages.getString("IntMatrixUtil.24")); //$NON-NLS-1$
    }
    return ans;
  }

  /**
   * 各成分とvalueをoperatorで指定された演算子で比較し、計算結果を成分とする行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param opponent 比較対象
   * @return 計算結果を成分とする行列
   */
  public static boolean[][] compareElements(final int[][] matrix, final String operator, final int opponent) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    boolean[][] ans = new boolean[rowSize][columnSize];
    if (operator.equals(".>")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = matrixi[j] > opponent;
        }
      }
    } else if (operator.equals(".>=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = matrixi[j] >= opponent;
        }
      }
    } else if (operator.equals(".<")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = matrixi[j] < opponent;
        }
      }
    } else if (operator.equals(".<=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = matrixi[j] <= opponent;
        }
      }
    } else if (operator.equals(".==")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = matrixi[j] == opponent;
        }
      }
    } else if (operator.equals(".!=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = matrixi[j] != opponent;
        }
      }
    } else {
      throw new IllegalArgumentException(Messages.getString("IntMatrixUtil.25")); //$NON-NLS-1$
    }
    return ans;
  }

  /**
   * 2個の行列を成分毎にoperatorで指定された演算子で比較し、計算結果を成分とする行列を生成します。
   * 
   * @param a1 第一行列
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param a2 第二行列
   * @return 計算結果を成分とする行列
   */
  public static boolean[][] compareElements(final int[][] a1, final String operator, final int[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];

    if (operator.equals(".>")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] a1i = a1[i];
        int[] a2i = a2[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = a1i[j] > a2i[j];
        }
      }
    } else if (operator.equals(".>=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] a1i = a1[i];
        int[] a2i = a2[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = a1i[j] >= a2i[j];
        }
      }
    } else if (operator.equals(".<")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] a1i = a1[i];
        int[] a2i = a2[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = a1i[j] < a2i[j];
        }
      }
    } else if (operator.equals(".<=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] a1i = a1[i];
        int[] a2i = a2[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = a1i[j] <= a2i[j];
        }
      }
    } else if (operator.equals(".==")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] a1i = a1[i];
        int[] a2i = a2[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = a1i[j] == a2i[j];
        }
      }
    } else if (operator.equals(".!=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        int[] a1i = a1[i];
        int[] a2i = a2[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = a1i[j] != a2i[j];
        }
      }
    } else {
      throw new IllegalArgumentException(Messages.getString("IntMatrixUtil.26")); //$NON-NLS-1$
    }
    return ans;
  }

  /**
   * 行列を出力ストリームに出力(MXフォーマット)します。
   * 
   * @param matrix 対象となる行列
   * @param output 出力ストリーム
   * @param name 行列の名前
   * @throws IOException ストリームに出力できない場合
   */
  public static void writeMxFormat(final int[][] matrix, final OutputStream output, final String name) throws IOException {
    MxDataHead head = new MxDataHead(matrix, name);
    head.write(output);

    DataOutputStream ds = new DataOutputStream(new BufferedOutputStream(output));

    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    for (int i = 0; i < rowSize; i++) {
      int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ds.writeInt(matrixi[j]);
      }
    }

    ds.flush();
  }

  /**
   * 入力ストリームから行列データ(MXフォーマット)を読み込みます。
   * 
   * @param input 入力ストリーム
   * @param head MXフォーマットのヘッダ情報
   * @return 読み込んだ行列
   * @throws IOException 入力ストリームから読み込めない場合
   */
  public static int[][] readMxFormat(final InputStream input, final MxDataHead head) throws IOException {
    int rowSize = head.getRowSize();
    int columnSize = head.getColumnSize();
    int[][] ans = new int[rowSize][columnSize];

    DataInputStream is = new DataInputStream(input);

    if (head.isSameEndian()) {
      for (int i = 0; i < rowSize; i++) {
        int[] ansi = ans[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = is.readInt();
        }
      }
    } else {
      for (int i = 0; i < rowSize; i++) {
        int[] ansi = ans[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = EndianTransformer.flip(is.readInt());
        }
      }
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
//  public static void setSubMatrix(final int[][] destination, final int rowTo, final int columnTo, final int[][] source, final int rowMin, final int rowMax, final int columnMin, final int columnMax) {
//    int nrow = rowMax - rowMin + 1;
//    int ncol = columnMax - columnMin + 1;
//
//    for (int i = 0; i < nrow; i++) {
//      int[] toi = destination[rowTo + i];
//      int[] fromi = source[rowMin + i];
//      System.arraycopy(fromi, columnMin, toi, columnTo, ncol);
//    }
//  }

  /**
   * 成分毎の剰余関数の結果からなる行列を返します。
   * 
   * @param matrix 割られる行列
   * @param scalar 割る数
   * @return 成分剰余関数行列
   */
  public static int[][] remainderElementWise(final int[][] matrix, final int scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int[][] ans = new int[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        int ii = matrixi[j];
        ansi[j] = ii % scalar;
      }
    }
    return ans;
  }

  /**
   * 成分毎の剰余関数の結果からなる行列を返します。
   * 
   * @param matrix 割られる行列
   * @param scalar 割る数
   * @return 成分剰余関数行列
   */
  public static int[][] remainderElementWise(final int[][] matrix, final double scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int[][] ans = new int[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        int ii = matrixi[j];
        ansi[j] = (int)(ii - Math.floor(ii / scalar) * scalar);
      }
    }
    return ans;
  }

  /**
   * 成分毎の剰余関数の結果からなる行列を返します。
   * 
   * @param a1 割られる行列
   * @param a2 割る数の行列
   * @return 成分剰余関数行列
   */
  public static int[][] remainderElementWise(final int[][] a1, final int[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;

    int[][] ans = new int[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] a1i = a1[i];
      int[] a2i = a2[i];
      for (int j = 0; j < columnSize; j++) {
        int a1ij = a1i[j];
        int a2ij = a2i[j];
        ansi[j] = a1ij % a2ij;
      }
    }
    return ans;
  }

  /**
   * 成分毎の剰余関数の結果からなる行列を返します。
   * 
   * @param a1 割られる行列
   * @param a2 割る数の行列
   * @return 成分剰余関数行列
   */
  public static int[][] remainderElementWise(final int[][] a1, final double[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;

    int[][] ans = new int[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] a1i = a1[i];
      double[] a2i = a2[i];
      for (int j = 0; j < columnSize; j++) {
        int ii = a1i[j];
        double scalar = a2i[j];
        ansi[j] = (int)(ii - Math.floor(ii / scalar) * scalar);
      }
    }
    return ans;
  }

  /**
   * ベクトルの成分を昇順にソートします。
   * 
   * @param vector 対象となるベクトル
   * @param index 並び替えた成分の番号を記憶する配列
   * @param start ソートの対象となる成分の開始番号
   * @param end ソートの対象となる成分の終了番号
   */
  public static void quickSort(final int[] vector, final int[] index, final int start, final int end) {
    if (start >= end) {
      return;
    }

    // 基準値 piv
    int piv = vector[(start + end) >>> 1];
    int i = start;
    int j = end;
    while (i <= j) {
      // piv 以上の成分を探す
      while (vector[i] < piv) {
        i++;
      }
      // piv 以下の成分を探す
      while (vector[j] > piv) {
        j--;
      }

      if (i <= j) { // swaping
        int tmp = vector[i];
        vector[i] = vector[j];
        vector[j] = tmp;
        int idxtmp = index[i];
        index[i] = index[j];
        index[j] = idxtmp;
        i++;
        j--;
      } else {
        break;
      }
    }

    // piv より左の部分配列をソート
    if (j > start) {
      quickSort(vector, index, start, j);
    }
    // piv より右の部分配列をソート
    if (i < end) {
      quickSort(vector, index, i, end);
    }
  }

  /**
   * 列毎のメジアンを成分とする行ベクトルを返します。
   * 
   * @param matrix 対象となる行列
   * @return 中間値(メジアン)
   */
  public static int[][] medianColumnWise(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

//    if (columnSize == 1) {
//      int[][] sortedVector = sortRowWise(matrix).getElements();
//      if (columnSize % 2 != 0) {
//        return new int[][] {getSubVector(sortedVector[0], rowSize / 2, rowSize / 2)};
//      }
//
//      return new int[][] {{(sortedVector[0][rowSize / 2 - 1] + sortedVector[0][rowSize / 2]) >>> 1}};
//    }

    final IndexedIntElements sortedElements = sortColumnWise(matrix);
    final int[][] sortedMatrix = sortedElements.getElements();
            
//    int[][] sortedMatrix = new int[rowSize][columnSize];
//    for (int i = 0; i < columnSize; i++) {
//      int[][] xi = sortColumnWise(getSubMatrix(matrix, 0, rowSize - 1, i, i)).getElements();
//      setSubMatrix(sortedMatrix, 0, rowSize - 1, i, i, xi);
//    }

    if (rowSize % 2 != 0) {
      return getSubMatrix(sortedMatrix, rowSize / 2, rowSize / 2, 0, columnSize - 1);
    }

    int[][] x1 = getSubMatrix(sortedMatrix, rowSize / 2 - 1, rowSize / 2 - 1, 0, columnSize - 1);
    int[][] x2 = getSubMatrix(sortedMatrix, rowSize / 2, rowSize / 2, 0, columnSize - 1);
    return divide(add(x1, x2), 2);
  }

  /**
   * 行毎のメジアンを成分とする列ベクトルを返します。
   * 
   * @param matrix 対象となる行列
   * @return 中間値(メジアン)
   */
  public static int[][] medianRowWise(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

//    if (rowSize == 1) {
//      int[][] sortedVector = sortColumnWise(matrix).getElements();
//      if (columnSize % 2 != 0) {
//        return new int[][] {getSubVector(sortedVector[0], columnSize / 2, columnSize / 2)};
//      }
//
//      return new int[][] {{(sortedVector[columnSize / 2 - 1][0] + sortedVector[columnSize / 2][0]) >>> 1}};
//    }

    //int[][] sortedMatrix = new int[rowSize][columnSize];
    //for (int i = 0; i < columnSize; i++) {
    //  int[][] xi = sortRowWise(getSubMatrix(matrix, i, i, 0, columnSize - 1)).getElements();
    //  setSubMatrix(sortedMatrix, i, i, 0, columnSize - 1, xi);
    //}
    
    final IndexedIntElements sortedElements = sortRowWise(matrix);
    int[][] sortedMatrix = sortedElements.getElements();

    if (columnSize % 2 != 0) {
      return getSubMatrix(sortedMatrix, 0, rowSize - 1, columnSize / 2, columnSize / 2);
    }

    int[][] x1 = getSubMatrix(sortedMatrix, 0, rowSize - 1, columnSize / 2 - 1, columnSize / 2 - 1);
    int[][] x2 = getSubMatrix(sortedMatrix, 0, rowSize - 1, columnSize / 2, columnSize / 2);
    return divide(add(x1, x2), 2);
  }

  /**
   * 共分散行列を求めます。
   * 
   * @param a1 データ列1 (行ベクトル又は列ベクトル)
   * @param a2 データ列2 (行ベクトル又は列ベクトル)
   * @return 共分散 (Covariance)
   */
  public static int[][] covariance(final int[][] a1, final int[][] a2) {
    int rowSize1 = a1.length;
    int columnSize1 = rowSize1 == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (columnSize1 != columnSize2 && rowSize1 != 0 && rowSize2 != 0) {
      throw new MatrixSizeException(MatrixSizeException.INCONSISTENT_COLUMN_NUMBER);
    }

    if (rowSize1 != 1 && columnSize1 != 1) {
      throw new IllegalArgumentException(Messages.getString("IntMatrixUtil.27")); //$NON-NLS-1$
    }

    int[][] xy = appendRight(makeColumnVector(a1), makeColumnVector(a2));

    int size = xy.length;
    int[][] xyZeroMean = subtract(xy, multiply(ones(size, 1), meanColumnWise(xy)));

    return divide(multiply(transpose(xyZeroMean), xyZeroMean), size - 1);
  }

  /**
   * 分散を求めます。
   * 
   * @param matrix データ列 (行ベクトル又は列ベクトル)
   * @return 分散
   */
  public static int variance(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize != 1 && columnSize != 1) {
      throw new IllegalArgumentException(Messages.getString("IntMatrixUtil.28")); //$NON-NLS-1$
    }

    int[][] x = makeColumnVector(matrix);

    int size = x.length;
    int[][] xZeroMean = subtract(x, multiply(ones(size, 1), meanColumnWise(x)));

    return divide(multiply(transpose(xZeroMean), xZeroMean), size - 1)[0][0];
  }

  /**
   * 各列ベクトルを縦に結合し、長い列ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 列ベクトル
   */
  public static int[][] makeColumnVector(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 1 || columnSize == 1) {
      return reshape(matrix, rowSize * columnSize, 1);
    }
    return reshape(transpose(matrix), rowSize * columnSize, 1);
  }

  /**
   * 成分毎に大きさを比較し、大きい方を成分とする行列を生成します。
   * 
   * @param a1 第一行列
   * @param a2 第二行列
   * @return 生成された行列
   */
  public static int[][] maxElementWise(final int[][] a1, final int[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (rowSize != rowSize2 || columnSize != columnSize2) {
      throw new MatrixSizeException(Messages.getString("IntMatrixUtil.29")); //$NON-NLS-1$
    }
    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] a1i = a1[i];
      int[] a2i = a2[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = Math.max(a1i[j], a2i[j]);
      }
    }
    return ans;
  }

  /**
   * 成分毎に大きさを比較し、小さい方を成分とする行列を生成します。
   * 
   * @param a1 第一行列
   * @param a2 第二行列
   * @return 生成された行列
   */
  public static int[][] minElementWise(final int[][] a1, final int[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (rowSize != rowSize2 || columnSize != columnSize2) {
      throw new MatrixSizeException(Messages.getString("IntMatrixUtil.30")); //$NON-NLS-1$
    }
    int[][] ans = new int[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      int[] ansi = ans[i];
      int[] a1i = a1[i];
      int[] a2i = a2[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = Math.min(a1i[j], a2i[j]);
      }
    }
    return ans;
  }

  /**
   * 出力ストリームに出力します。
   * 
   * @param matrix 対象となる行列
   * @param output 出力ストリーム
   * @param format 成分の出力フォーマット
   * @param maxColumnSize 最大列の数
   */
  public static void print(final int[][] matrix, final Writer output, final String format, final int maxColumnSize) {
    PrintWriter pw = new PrintWriter(output);
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int remain = columnSize;
    int columnOffset = 0;
    while (remain > 0) {
      int printColumnSize = Math.min(maxColumnSize, remain);

      // 列番号を表示する
      IntMatrixUtil.printColumnNumber(columnOffset, printColumnSize, pw);

      for (int i = 0; i < rowSize; i++) {
        IntMatrixUtil.printRowNumber(i, pw);

        int[] elements = new int[printColumnSize];
        for (int j = 0; j < printColumnSize; j++) {
          elements[j] = matrix[i][j + columnOffset];
        }

        IntMatrixUtil.printElements(elements, pw, format);
      }
      columnOffset += printColumnSize;
      remain -= printColumnSize;
    }

    pw.flush();
  }

  /**
   * 複数個の整数をプリントライターに出力する。
   * 
   * @param elements 整数の配列
   * @param output プリントライター
   * @param format 成分の出力フォーマット
   */
  private static void printElements(final int[] elements, final PrintWriter output, final String format) {
    // final int formatLength = 10;

    for (int j = 0; j < elements.length; j++) {
      output.print("  "); //$NON-NLS-1$
      output.print(String.format(format, Integer.valueOf(elements[j])));
      // output.print(IntUtil.format(elements[j], formatLength));
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
    output.print(" "); //$NON-NLS-1$
    output.print("      "); //$NON-NLS-1$
    for (int i = 0; i < printColumnSize; i++) {
      String str = String.format("       (%3d)", Integer.valueOf(columnOffset + i + 1)); //$NON-NLS-1$
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
   * 行や列を交換します。
   * 
   * @param matrix 対象となる行列
   * @param pivot 交換する行番号や列番号の配列(1から始まる)
   * @param count 交換する数
   * @param rowExchange 行を交換する場合true
   */
  public static void permutateSelf(final int[][] matrix, final int[] pivot, final int count, final boolean rowExchange) {
    if (rowExchange) {
      for (int i = 0; i < count; i++) {
        exchangeRow(matrix, i, pivot[i] - 1);
      }
      return;
    } 
    
    for (int i = 0; i < count; i++) {
      exchangeColumn(matrix, i, pivot[i] - 1);
    }
  }
  
  /**
   * 全対角成分の和(トレース)を返します。
   * 
   * @param matrix 対象となる行列
   * @return 対角成分の合計(トレース)
   */
  public static int trace(final int[][] matrix) {
    int size = matrix.length;
    int ans = 0;
    for (int i = 0; i < size; i++) {
      ans += matrix[i][i];
    }
    return ans;
  }

  /**
   * 行列式を返します。
   * 
   * @param matrix 対象となる行列
   * @return 行列式
   */
  public static int determinant(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int[][] aa = matrix;

    if (rowSize == 1) {
      int det = aa[0][0];
      return det;
    }

    if (rowSize == 2) {
      int[] aa0 = aa[0];
      int[] aa1 = aa[1];

      int det = aa0[0]*aa1[1] - aa0[1]*aa1[0];
      return det;
    }

    if (rowSize == 3) {
      int[] aa0 = aa[0];
      int[] aa1 = aa[1];
      int[] aa2 = aa[2];

      int tmpp1 = aa0[0] * aa1[1];
      int det = tmpp1 * aa2[2]; /* a(1,1)*a(2,2)*a(3,3) */

      tmpp1 = aa0[1] * aa1[2];
      int tmpp2 = tmpp1 * aa2[0]; /* a(1,2)*a(2,3)*a(3,1) */
      det = det + tmpp2;

      tmpp1 = aa0[2] * aa1[0];
      tmpp2 = tmpp1 * aa2[1]; /* a(1,3)*a(2,1)*a(3,2) */
      det = det + tmpp2;

      tmpp1 = aa0[2] * aa1[1];
      tmpp2 = tmpp1 * aa2[0]; /* a(1,3)*a(2,2)*a(3,1) */
      det = det - tmpp2;

      tmpp1 = aa0[1] * aa1[0];
      tmpp2 = tmpp1 * aa2[2]; /* a(1,2)*a(2,1)*a(3,3) */
      det = det - tmpp2;

      tmpp1 = aa0[0] * aa1[2];
      tmpp2 = tmpp1 * aa2[1]; /* a(1,1)*a(2,3)*a(3,2) */
      det = det - tmpp2;
      return det;
    }

    int det = 0;
    /* Discard the first column */
    int[][] m1 = IntMatrixUtil.getSubMatrix(matrix, 0, rowSize - 1, 1, columnSize - 1);

    int m1r = m1.length;
    int m1c = m1r == 0 ? 0 : m1[0].length;

    for (int i = 1; i <= m1r; i++) {
      int[][] m2;
      if (i == 1) {
        m2 = IntMatrixUtil.getSubMatrix(m1, 1, m1r - 1, 0, m1c - 1);
      } else if (i == m1r) {
        m2 = IntMatrixUtil.getSubMatrix(m1, 0, m1r - 2, 0, m1c - 1);
      } else {
        int[][] m3 = IntMatrixUtil.getSubMatrix(m1, 0, i - 2, 0, m1c - 1);
        int[][] m4 = IntMatrixUtil.getSubMatrix(m1, i, m1r - 1, 0, m1c - 1);
        m2 = IntMatrixUtil.appendDown(m3, m4);
      }

      int subDet = aa[i - 1][0] * determinant(m2);

      if (i % 2 != 0) {
        det = det + subDet;
      } else {
        det = det - subDet;
      }
    }
    return det;
  }

  /**
   * 行列を1行文字列に変換します。
   * 
   * @param matrix 対象となる行列
   * @param format 成分の出力フォーマット
   * @return 1行文字列
   */
  public static String toString(final int[][] matrix, final String format) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    StringBuffer sb = new StringBuffer();

    if (rowSize == 0 || columnSize == 0) {
      return "[[]]"; //$NON-NLS-1$
    }

    if (columnSize == 1 && rowSize != 1) {
      return toString(transpose(matrix), format) + "'"; //$NON-NLS-1$
    }

    if (rowSize != 1) {
      sb.append("["); //$NON-NLS-1$
    }
    
    final int displayColumnSize = 4;

    for (int i = 0; i < rowSize; i++) {
      if (i != 0) {
        sb.append(" "); //$NON-NLS-1$
      }

      for (int k = 0; k < columnSize;) {
        if (k == 0) {
          sb.append("["); //$NON-NLS-1$
        } else {
          sb.append(" "); //$NON-NLS-1$
          if (rowSize != 1) {
            sb.append(" "); //$NON-NLS-1$
          }
        }

        int j;
        for (j = k; j < k + displayColumnSize && j < columnSize; j++) {
          sb.append(IntNumber.toString(matrix[i][j], format));
          if (j != columnSize - 1) {
            sb.append(","); //$NON-NLS-1$
          }
        }

        if (j == columnSize) {
          sb.append("]"); //$NON-NLS-1$
        }

        k += displayColumnSize;
      }
    }

    if (rowSize != 1) {
      sb.append("]"); //$NON-NLS-1$
    }

    return sb.toString();
  }

}