/**
 * $Id: DoubleMatrixUtil.java,v 1.15 2008/07/16 04:58:02 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.mklab.nfc.matx.MxDataHead;
import org.mklab.nfc.scalar.DoubleNumber;
import org.mklab.nfc.scalar.NumericalScalar;
import org.mklab.nfc.scalar.Scalar;
import org.mklab.nfc.svd.DoubleRealSingularValueDecomposer;
import org.mklab.nfc.util.EndianTransformer;


/**
 * 倍精度(double)型の実行列のユーティリティクラスです。
 * 
 * @author koga
 * @version $Revision: 1.15 $
 */
public final class DoubleMatrixUtil {
  /**
   * 新しく生成された<code>DoubleMatrixUtil</code>オブジェクトを初期化します。
   */
  private DoubleMatrixUtil() {
    // nothing to do
  }

  /**
   * 2個の配列の成分が全て等しか判定します。
   * 
   * @param a1 第一行列
   * @param a2 第二行列
   * @return 配列の成分が等しければtrue、そうでなければfalseを返します。
   */
  public static boolean equals(final double[][] a1, final double[][] a2) {
    final int rowSize1 = a1.length;
    final int columnSize1 = rowSize1 == 0 ? 0 : a1[0].length;
    final int rowSize2 = a2.length;
    final int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (rowSize1 != rowSize2 || columnSize1 != columnSize2) {
      return false;
    }

    for (int row = 0; row < rowSize1; row++) {
      final double[] a1Row = a1[row];
      final double[] a2Row = a2[row];
      for (int column = 0; column < columnSize1; column++) {
        final double value1 = a1Row[column];
        final double value2 = a2Row[column];
        
        if (Double.isNaN(value1) && Double.isNaN(value2)) {
          continue;
        }
        if (Double.isInfinite(value1) && Double.isInfinite(value2)) {
          continue;
        }
        
        if (value1 != value2) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * 2個の配列の成分の差の絶対値が許容誤差以下であるか判定します。
   * 
   * @param a1 第一行列
   * @param a2 第二行列
   * @param tolerance 許容誤差
   * @return 配列の成分の差が許容誤差以下ならばtrue、そうでなければfalse
   */
  public static boolean equals(final double[][] a1, final double[][] a2, final double tolerance) {
    final int rowSize1 = a1.length;
    final int columnSize1 = rowSize1 == 0 ? 0 : a1[0].length;
    final int rowSize2 = a2.length;
    final int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (rowSize1 != rowSize2 || columnSize1 != columnSize2) {
      return false;
    }

    for (int row = 0; row < rowSize1; row++) {
      final double[] a1Row = a1[row];
      final double[] a2Row = a2[row];
      for (int column = 0; column < columnSize1; column++) {
        final double value1 = a1Row[column];
        final double value2 = a2Row[column];
        
        if (Double.isNaN(value1) && Double.isNaN(value2)) {
          continue;
        }
        if (Double.isInfinite(value1) && Double.isInfinite(value2)) {
          continue;
        }
        
        if (Math.abs(value1 - value2) > tolerance) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * 部分行列を生成します。
   * 
   * @param matrix 元の行列
   * @param rowMin 始まり行(0から始まります)
   * @param rowMax 終わり行(0から始まります)
   * @param columnMin 始まり列(0から始まります)
   * @param columnMax 終わり列(0から始まります)
   * @return 部分行列
   */
  public static double[][] getSubMatrix(final double[][] matrix, final int rowMin, final int rowMax, final int columnMin, final int columnMax) {
    int rowSize = rowMax - rowMin + 1;
    int columnSize = columnMax - columnMin + 1;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      double[] matrixi = matrix[i + rowMin];
      System.arraycopy(matrixi, columnMin, ansi, 0, columnSize);
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
  public static double[][] getSubMatrix(final double[][] matrix, final int[] rowIndex, final int columnMin, final int columnMax) {
    int rowSize = rowIndex.length;
    int columnSize = columnMax - columnMin + 1;

    double[][] ans = new double[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      double[] matrixi = matrix[rowIndex[i]];
      System.arraycopy(matrixi, columnMin, ansi, 0, columnSize);
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
  public static double[][] getSubMatrix(final double[][] matrix, final int rowMin, final int rowMax, final int[] columnIndex) {
    int rowSize = rowMax - rowMin + 1;
    int columnSize = columnIndex.length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      double[] matrixi = matrix[i + rowMin];
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
  public static double[][] getSubMatrix(final double[][] matrix, final int[] rowIndex, final int[] columnIndex) {
    int rowSize = rowIndex.length;
    int columnSize = columnIndex.length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      double[] matrixi = matrix[rowIndex[i]];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[columnIndex[j]];
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
  public static double[] getSubVector(final double[] vector, final int[] index) {
    int size = index.length;
    if (size == 0) {
      return new double[0];
    }
    double[] ans = new double[size];

    for (int i = 0; i < size; i++) {
      ans[i] = vector[index[i]];
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
  public static double[][] appendDown(final double[][] a1, final double[][] a2) {
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

    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize1; i++) {
      double[] ansi = ans[i];
      double[] a1i = a1[i];
      System.arraycopy(a1i, 0, ansi, 0, columnSize);
    }
    for (int i = rowSize1; i < rowSize; i++) {
      double[] ansi = ans[i];
      double[] a2i = a2[i - rowSize1];
      System.arraycopy(a2i, 0, ansi, 0, columnSize);
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
  public static double[][] appendRight(final double[][] a1, final double[][] a2) {
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

    double[][] ans = new double[rowSize][columnSize];
    
    for (int i = 0; i < rowSize1; i++) {
      double[] ansi = ans[i];
      double[] a1i = a1[i];
      System.arraycopy(a1i, 0, ansi, 0, columnSize1);
    }

    for (int i = 0; i < rowSize2; i++) {
      double[] ansi = ans[i];
      double[] a2i = a2[i];
      System.arraycopy(a2i, 0, ansi, columnSize1, columnSize2);
    }

    return ans;
  }

  /**
   * 少なくとも1個は零の成分がベクトルに含まれるか判定します。
   * 
   * @param vector 判定する対象のベクトル
   * @return 少なくとも1個は零の成分があればtrue、そうでなければfalse
   */
  public static boolean anyZero(final double[] vector) {
    int rowSize = vector.length;

    for (int i = 0; i < rowSize; i++) {
      if (vector[i] == 0) {
        return true;
      }
    }
    return false;
  }

  /**
   * 行列の複製を生成します。
   * 
   * @param matrix 複製の元となる行列
   * @return 複製された行列
   */
  public static double[][] clone(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      double[] matrixi = matrix[i];
      System.arraycopy(matrixi, 0, ansi, 0, columnSize);
    }
    return ans;
  }

  /**
   * 行列の成分をコピーします。
   * 
   * @param source コピー元
   * @param destination コピー先
   */
  public static void copy(final double[][] source, final double[][] destination) {
    int rowSize = destination.length;
    int columnSize = rowSize == 0 ? 0 : destination[0].length;

    for (int i = 0; i < rowSize; i++) {
      double[] toi = destination[i];
      double[] fromi = source[i];
      System.arraycopy(fromi, 0, toi, 0, columnSize);
    }
  }

  /**
   * 行列の成分をコピーします。
   * 
   * @param source コピー元
   * @param destination コピー先
   */
  public static void copy(final int[][] source, final double[][] destination) {
    int rowSize = destination.length;
    int columnSize = rowSize == 0 ? 0 : destination[0].length;

    for (int i = 0; i < rowSize; i++) {
      double[] toi = destination[i];
      int[] fromi = source[i];
      for (int j = 0; j < columnSize; j++) {
        toi[j] = fromi[j];
      }
    }
  }

  /**
   * 行列の成分をコピーします。
   * 
   * @param source コピー元
   * @param destination コピー先
   */
  public static void copy(final double[] source, final double[] destination) {
    System.arraycopy(source, 0, destination, 0, source.length);
  }

  /**
   * 零行列であるか判定します。
   * 
   * @param matrix 判定する行列
   * @param tolerance 許容誤差
   * @return 零行列ならばtrue、そうでなければfalse
   */
  public static boolean isZero(final double[][] matrix, final double tolerance) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    
    for (int i = 0; i < rowSize; i++) {
      final double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        final double value = matrixi[j];
        if (Math.abs(value) > tolerance || Double.isNaN(value) || Double.isInfinite(value)) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * 零行列であるか判定します。
   * 
   * @param matrix 判定する行列
   * @param tolerance 許容誤差
   * @return 零行列ならばtrue、そうでなければfalse
   */
  public static boolean isZero(final double[][] matrix, final NumericalScalar<?,?> tolerance) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    
    for (int i = 0; i < rowSize; i++) {
      final double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        final double value = matrixi[j];
        if (tolerance.isLessThan(Math.abs(value)) || Double.isNaN(value) || Double.isInfinite(value)) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * 単位行列であるか判定します。
   * 
   * @param matrix 判定する行列
   * @param tolerance 許容誤差
   * @return 単位行列ならばtrue、そうでなければfalse
   */
  public static boolean isUnit(final double[][] matrix, final double tolerance) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize != columnSize) {
      return false;
    }

    if (rowSize == 0 && columnSize == 0) {
      return false;
    }

    for (int row = 0; row < rowSize; row++) {
      double[] rowVector = matrix[row];
      for (int column = 0; column < columnSize; column++) {
        final double value = rowVector[column];

        if (Double.isNaN(value) || Double.isInfinite(value)) {
          return false;
        }

        if (row == column) {
          if (Math.abs(value - 1) > tolerance) {
            return false;
          }
        } else {
          if (Math.abs(value) > tolerance) {
            return false;
          }
        }
      }
    }

    return true;
  }

  /**
   * 単位行列であるか判定します。
   * 
   * @param matrix 判定する行列
   * @param tolerance 許容誤差
   * @return 単位行列ならばtrue、そうでなければfalse
   */
  public static boolean isUnit(final double[][] matrix, final NumericalScalar<?,?> tolerance) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize != columnSize) {
      return false;
    }
    
    if (rowSize == 0 && columnSize == 0) {
      return false;
    }

    for (int row = 0; row < rowSize; row++) {
      final double[] rowVector = matrix[row];
      for (int column = 0; column < columnSize; column++) {
        final double value = rowVector[column];

        if (Double.isNaN(value) || Double.isInfinite(value)) {
          return false;
        }

        if (row == column) {
          if (tolerance.isLessThan(Math.abs(value - 1))) {
            return false;
          }
        } else {
          if (tolerance.isLessThan(Math.abs(value))) {
            return false;
          }
        }
      }
    }

    return true;
  }

  /**
   * 行列の全ての成分に零を代入します。
   * 
   * @param matrix 零を代入する行列
   */
  public static void setZero(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    for (int row = 0; row < rowSize; row++) {
      double[] rowVector = matrix[row];
      for (int column = 0; column < columnSize; column++) {
        rowVector[column] = 0;
      }
    }
  }

  /**
   * 転置行列を生成します。
   * 
   * @param matrix 元の行列
   * @return 転置行列
   */
  public static double[][] transpose(final double[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final double[][] ans = new double[columnSize][rowSize];

    for (int i = 0; i < rowSize; i++) {
      final double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ans[j][i] = matrixi[j];
      }
    }
    return ans;
  }
  
  /**
   * 転置行列を生成します。
   * 
   * @param matrix 元の行列
   * @return 転置行列
   */
  public static double[][] transpose(final double[] matrix) {
    return transpose(new double[][]{matrix});
  }

  /**
   * 転置行列の成分を設定します。
   * 
   * @param matrix 元の行列
   * @param result 転置行列の成分を代入する行列
   */
  public static void transpose(final double[][] matrix, final double[][] result) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    for (int i = 0; i < rowSize; i++) {
      final double[] m2i = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        result[j][i] = m2i[j];
      }
    }
  }

  /**
   * 行列の全ての成分の逆数を成分とする行列を生成します。
   * 
   * @param matrix 元の行列
   * @return 成分の逆数を成分とする行列
   */
  public static double[][] inverseElementWise(final double[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final double[][] ans = new double[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      final double[] ansi = ans[i];
      final double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = 1 / matrixi[j];
      }
    }

    return ans;
  }

  /**
   * 第1行列に第2行列を加えます。
   * 
   * @param a1 第1行列
   * @param a2 第2行列
   */
  public static void addSelf(final double[][] a1, final double[][] a2) {
    final int rowSize = a1.length;
    final int columnSize = rowSize == 0 ? 0 : a1[0].length;

    for (int i = 0; i < rowSize; i++) {
      final double[] a1i = a1[i];
      final double[] a2i = a2[i];
      for (int j = 0; j < columnSize; j++) {
        a1i[j] += a2i[j];
      }
    }
  }

  /**
   * 2個の行列の積行列を生成します。
   * 
   * @param a1 掛けられる行列
   * @param a2 掛ける行列
   * @return 行列の積
   */
  public static double[][] multiply(final double[][] a1, final double[][] a2) {
    final int rowSize1 = a1.length;
    final int columnSize1 = rowSize1 == 0 ? 0 : a1[0].length;
    final int rowSize2 = a2.length;
    final int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (columnSize1 != rowSize2) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrixUtil.1")); //$NON-NLS-1$
    }

    final double[][] ans = new double[rowSize1][columnSize2];
    final int bigMatrixSize = 100; // Integer.MAX_VALUE;

    if (columnSize1 > bigMatrixSize) {
      final double[][] a2t = DoubleMatrixUtil.transpose(a2);
      for (int i = 0; i < rowSize1; i++) {
        final double[] a1i = a1[i];
        final double[] ansi = ans[i];
        for (int j = 0; j < columnSize2; j++) {
          double d = 0;
          final double[] a2tj = a2t[j];
          for (int k = 0; k < columnSize1; k++) {
            d += a1i[k] * a2tj[k];
          }
          ansi[j] = d;
        }
      }
    } else {
      for (int i = 0; i < rowSize1; i++) {
        final double[] a1i = a1[i];
        final double[] ansi = ans[i];
        for (int j = 0; j < columnSize2; j++) {
          double d = 0;
          for (int k = 0; k < columnSize1; k++) {
            d += a1i[k] * a2[k][j];
          }
          ansi[j] = d;
        }
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
  public static double[][] createUnit(final int rowSize, final int columnSize) {
    double[][] ans = new double[rowSize][columnSize];

    int size = rowSize < columnSize ? rowSize : columnSize;

    for (int i = 0; i < size; i++) {
      ans[i][i] = 1;
    }

    return ans;
  }

  /**
   * 全ての成分が1である行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 全ての成分が1である行列
   */
  public static double[][] createOnes(final int rowSize, final int columnSize) {
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = 1;
      }
    }
    return ans;
  }

  /**
   * 零行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 零行列
   */
  public static double[][] createZero(final int rowSize, final int columnSize) {
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      final double[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = 0;
      }
    }

    return ans;
  }

  /**
   * 零ベクトルを生成します。
   * 
   * @param size 行の数
   * @return 零ベクトル
   */
  public static double[] createZero(final int size) {
    double[] ans = new double[size];

    for (int i = 0; i < size; i++) {
      ans[i] = 0;
    }

    return ans;
  }
  
  
  /**
   * 0〜1の範囲の一様分布の乱数を成分とする行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 0〜1の範囲の一様分布の乱数を成分とする行列
   */
  public static double[][] createUniformRandom(final int rowSize, final int columnSize) {
    final Random rand = new Random();
    final double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      final double[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = rand.nextDouble();
      }
    }
    return ans;
  }

  /**
   * 0〜1の範囲の一様分布の乱数を成分とする行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param seed 乱数の種
   * @return 0〜1の範囲の一様分布の乱数を成分とする行列
   */
  public static double[][] createUniformRandom(final int rowSize, final int columnSize, final long seed) {
    final Random rand = new Random(seed);
    final double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      final double[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = rand.nextDouble();
      }
    }
    return ans;
  }

  /**
   * 平均0、分散1の正規分布の乱数を成分とする行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 平均0、分散1の正規分布の乱数を成分とする行列
   */
  public static double[][] createNormalRandom(final int rowSize, final int columnSize) {
    final Random rand = new Random();

    final double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      final double[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = rand.nextGaussian();
      }
    }
    return ans;
  }

  /**
   * 平均0、分散1の正規分布の乱数を成分とする行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param seed 乱数の種
   * @return 平均0、分散1の正規分布の乱数を成分とする行列
   */
  public static double[][] createNormalRandom(final int rowSize, final int columnSize, final long seed) {
    final Random rand = new Random(seed);

    final double[][] ans = new double[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      final double[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = rand.nextGaussian();
      }
    }
    return ans;
  }

  /**
   * <code>from</code>から<code>to</code>までの<code>by</code>飛びの実数を成分とする行ベクトルを返します。
   * 
   * @param from 始点
   * @param to 終点
   * @param by 間隔
   * @return fromからtoまでのby飛びの実数を成分とする行ベクトル
   */
  public static double[] series(final double from, final double to, final double by) {
    final double count = (to - from) / by;
    
    if (count < 0) {
      throw new IllegalArgumentException("DoubleMatrix.series(" + from + ",  " + to + ", " + by + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
    
    final int size = (int)(Math.floor(count)) + 1;
    final int size2 = size + (from + by*(size-1) < to ? 1 : 0);

    final double[] ans = new double[size2];
    for (int i = 0; i < size; i++) {
      ans[i] = i * by + from;
    }
    
    if (size < size2) {
      ans[size] = to;
    }
    
    return ans;
  }

  /**
   * 配列<code>to</code>の<code>rowTo</code>行<code>columnTo</code>列を始点として、 配列<code>from</code>の<code>rowMin</code>行<code>columnMin</code>列から <code>rowMax</code>行<code>columnMax</code>列までの値をコピーします。
   * 
   * @param destination コピー先
   * @param rowTo 変更開始行
   * @param columnTo 変更開始列
   * @param source コピー元
   * @param rowMin コピー開始行
   * @param rowMax コピー開始列
   * @param columnMin コピー終了行
   * @param columnMax コピー終了列
   */
  public static void setSubMatrix(final double[][] destination, final int rowTo, final int columnTo, final double[][] source, final int rowMin, final int rowMax, final int columnMin, final int columnMax) {
    int nrow = rowMax - rowMin + 1;
    int ncol = columnMax - columnMin + 1;

    for (int i = 0; i < nrow; i++) {
      double[] toi = destination[rowTo + i];
      double[] fromi = source[rowMin + i];
      System.arraycopy(fromi, columnMin, toi, columnTo, ncol);
    }
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
  public static void setSubMatrix(final double[][] destination, final int rowMin, final int rowMax, final int columnMin, final int columnMax, final double[][] source) {
    int rowSize = source.length;
    int columnSize = rowSize == 0 ? 0 : source[0].length;

    if (rowSize != rowMax - rowMin + 1 || columnSize != columnMax - columnMin + 1) {
      throw new MatrixSizeException(MatrixSizeException.INCONSISTENT_SIZE);
    }

    for (int i = 0; i < rowSize; i++) {
      double[] toi = destination[i + rowMin];
      double[] fromi = source[i];

      System.arraycopy(fromi, 0, toi, columnMin, columnSize);
    }
  }

  /**
   * 与えられた位置に行列を代入します。
   * 
   * @param destination 値を設定する行列
   * @param rowIndex 指定する行を含む指数
   * @param columnMin 列の始まり
   * @param columnMax 列の終り
   * @param source 代入する行列
   */
  public static void setSubMatrix(final double[][] destination, final int[] rowIndex, final int columnMin, final int columnMax, final double[][] source) {
    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;
    int idxcol = rowIndex.length;
    int columnSize = columnMax - columnMin + 1;

    if (mrow != idxcol || mcol != columnSize) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrixUtil.2")); //$NON-NLS-1$
    }

    for (int i = 0; i < idxcol; i++) {
      double[] fromi = source[i];
      double[] toi = destination[rowIndex[i]];
      System.arraycopy(fromi, 0, toi, columnMin, columnSize);
    }
  }

  /**
   * 与えられた位置に行列を代入します。
   * 
   * @param destination 値を設定する行列
   * @param rowIndex 指定する行を含む指数
   * @param columnIndex 指定する列を含む指数
   * @param source 代入する行列
   */
  public static void setSubMatrix(final double[][] destination, final int[] rowIndex, final int[] columnIndex, final double[][] source) {
    int idxcol1 = rowIndex.length;
    int idxcol2 = columnIndex.length;

    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;

    if (mrow != idxcol1 || mcol != idxcol2) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrixUtil.3")); //$NON-NLS-1$
    }

    for (int i = 0; i < idxcol1; i++) {
      double[] fromi = source[i];
      double[] toi = destination[rowIndex[i]];
      for (int j = 0; j < idxcol2; j++) {
        toi[columnIndex[j]] = fromi[j];
      }
    }
  }

  /**
   * 与えられた位置に行列を代入します。
   * 
   * @param destination 値を設定する行列
   * @param rowMin 行の始まり
   * @param rowMax 行の終り
   * @param columnIndex 指定する列を含む指数
   * @param source 代入する行列
   */
  public static void setSubMatrix(final double[][] destination, final int rowMin, final int rowMax, final int[] columnIndex, final double[][] source) {
    int idxcol = columnIndex.length;
    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;
    int rowSize = rowMax - rowMin + 1;

    if (mrow != rowSize || mcol != idxcol) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrixUtil.4")); //$NON-NLS-1$
    }

    for (int i = 0; i < rowSize; i++) {
      double[] fromi = source[i];
      double[] toi = destination[i + rowMin];
      for (int j = 0; j < idxcol; j++) {
        toi[columnIndex[j]] = fromi[j];
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
  public static void setSubVector(final double[] destination, final int min, final int max, final double[] source) {
    if (max - min + 1 != source.length) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrixUtil.5")); //$NON-NLS-1$
    }
    System.arraycopy(source, 0, destination, min, source.length);
  }

  /**
   * 配列<code>destination</code>の<code>rowTo</code>行を始点として、 配列<code>source</code>の <code>rowMin</code>行から<code>rowMax</code>行までの値をコピーします。
   * 
   * @param destination コピー先
   * @param to 変更開始行
   * @param source コピー元
   * @param min コピー開始行
   * @param max コピー開始列
   */
  public static void setSubVector(final double[] destination, final int to, final double[] source, final int min, final int max) {
    int nrow = max - min + 1;
    System.arraycopy(source, min, destination, to, nrow);
  }

  /**
   * 指定された位置に行列を代入します。
   * 
   * @param destination 値を設定する行列
   * @param index 指定する成分の番号を含む指数
   * @param source 代入する行列
   */
  static void setElements(final double[][] destination, final int[] index, final double[][] source) {
    int size = index.length;
    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;

    if (size != mrow * mcol) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrixUtil.6")); //$NON-NLS-1$
    }

    int columnSize = destination[0].length;

    for (int i = 0; i < size; i++) {
      int row = (index[i]) / columnSize;
      int col = (index[i]) % columnSize;
      destination[row][col] = source[i / mcol][i % mcol];
    }
  }

  /**
   * column1列とcolumn2列を入れ替えます。
   * 
   * @param matrix 対象の行列
   * @param column1 指定列１(0から始まります)
   * @param column2 指定列２(0から始まります)
   */
  public static void exchangeColumn(final double[][] matrix, final int column1, final int column2) {
    if (column1 == column2) {
      return;
    }

    int rowSize = matrix.length;

    for (int i = 0; i < rowSize; i++) {
      double tmp = matrix[i][column1];
      matrix[i][column1] = matrix[i][column2];
      matrix[i][column2] = tmp;
    }
  }

  /**
   * row1行とrow2行を入れ替えます。
   * 
   * @param matrix 対象の行列
   * @param row1 指定行１(0から始まります)
   * @param row2 指定行２(0から始まります)
   */
  public static void exchangeRow(final double[][] matrix, final int row1, final int row2) {
    if (row1 == row2) {
      return;
    }

    int columnSize = matrix.length;

    double[] matrixRow1 = matrix[row1];
    double[] matrixRow2 = matrix[row2];

    for (int i = 0; i < columnSize; i++) {
      double tmp = matrixRow1[i];
      matrixRow1[i] = matrixRow2[i];
      matrixRow2[i] = tmp;
    }
  }

  /**
   * 行列自身に実数を掛けます。
   * 
   * @param matrix 対象となる行列
   * @param scalar 乗じる実数
   */
  public static void multiplySelf(final double[][] matrix, final double scalar) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    for (int i = 0; i < rowSize; i++) {
      final double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        matrixi[j] *= scalar;
      }
    }
  }

  /**
   * 行列に整数を掛けた行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param scalar 乗じる整数
   * @return 整数を掛けた結果
   */
  public static double[][] multiply(final double[][] matrix, final int scalar) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      final double[] matrixi = matrix[i];
      final double[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j] * scalar;
      }
    }
    return ans;
  }

  /**
   * 行列に実数を掛けた行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param scalar 乗じる実数
   * @return 実数を掛けた結果
   */
  public static double[][] multiply(final double[][] matrix, final double scalar) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      final double[] matrixi = matrix[i];
      final double[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j] * scalar;
      }
    }
    return ans;
  }

  /**
   * 成分の符号を反転した行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @return 成分の符号を反転した行列
   */
  public static double[][] unaryMinus(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] matrixi = matrix[i];
      double[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = -matrixi[j];
      }
    }
    return ans;
  }

  /**
   * ベクトルの成分を対角成分とする対角行列を生成します。
   * 
   * @param vector 対象となるベクトル
   * @return 対角行列
   */
  public static double[][] vectorToDiagonal(final double[] vector) {
    int size = vector.length;
    double[][] ans = new double[size][size];

    for (int i = 0; i < size; i++) {
      ans[i][i] = vector[i];
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
  public static double[][] removeRowVectors(final double[][] matrix, final int rowMin, final int rowMax) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int newRowSize = rowSize - (rowMax - rowMin + 1);

    double[][] ans = new double[newRowSize][columnSize];

    for (int i = 0, i2 = 0; i < rowSize; i++) {
      if (rowMin <= i && i <= rowMax) {
        continue;
      }
      double[] matrixi = matrix[i];
      double[] ansi = ans[i2];
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
   * @param rowIndex 削除する行の番号(0から始まります)
   * @return 行を削除された行列
   */
  public static double[][] removeRowVectors(final double[][] matrix, final int[] rowIndex) {
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

    double[][] ans = new double[newRowSize][columnSize];
    for (int i = 0, i2 = 0; i < rowSize; i++) {
      if (removedRow[i]) {
        continue;
      }
      double[] matrixi = matrix[i];
      double[] ansi = ans[i2];
      System.arraycopy(matrixi, 0, ansi, 0, columnSize);
      i2++;
    }

    return ans;
  }

  /**
   * 指定された列を削除した行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param columnMin 開始列(0から始まります)
   * @param columnMax 終了列(0から始まります)
   * @return 列を削除された行列
   */
  public static double[][] removeColumnVectors(final double[][] matrix, final int columnMin, final int columnMax) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int newColumnSize = columnSize - (columnMax - columnMin + 1);

    double[][] ans = new double[rowSize][newColumnSize];
    for (int i = 0; i < rowSize; i++) {
      double[] matrixi = matrix[i];
      double[] ansi = ans[i];
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
   * @param columnIndex 削除する列の番号(0から始まります)
   * @return 列を削除された行列
   */
  public static double[][] removeColumnVectors(final double[][] matrix, final int[] columnIndex) {
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

    double[][] ans = new double[rowSize][newColumnSize];
    for (int i = 0; i < rowSize; i++) {
      double[] matrixi = matrix[i];
      double[] ansi = ans[i];
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
   * 行列の成分を変えずに、行列の大きさ(行の数と列の数)を変形します。
   * 
   * @param matrix 対象となる行列
   * @param newRowSize 変更後の行の数
   * @param newColumnSize 変更後の列の数
   * @return 変形した行列
   */
  public static double[][] reshape(final double[][] matrix, final int newRowSize, final int newColumnSize) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    if (rowSize * columnSize != newRowSize * newColumnSize) {
      throw new MatrixSizeException(MatrixSizeException.INCONSISTENT_SIZE);
    }
    double[][] ans = new double[newRowSize][newColumnSize];
    int num = 0;

    for (int i = 0; i < newRowSize; i++) {
      double[] ansi = ans[i];
      for (int j = 0; j < newColumnSize; j++) {
        ansi[j] = matrix[num / columnSize][num % columnSize];
        num++;
      }
    }
    return ans;
  }

  /**
   * 成分毎の乗算結果を成分とする行列を生成します。
   * 
   * @param a1 掛けられる行列
   * @param a2 掛ける行列
   * @return 掛けた結果の行列
   */
  public static double[][] multiplyElementWise(final double[][] a1, final double[][] a2) {
    final int rowSize = a1.length;
    final int columnSize = rowSize == 0 ? 0 : a1[0].length;
    final double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      final double[] a1i = a1[i];
      final double[] a2i = a2[i];
      final double[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a1i[j] * a2i[j];
      }
    }
    return ans;
  }

  /**
   * 成分毎に大きさを比較し、大きい方を成分とする行列を生成します。
   * 
   * @param a1 第一行列
   * @param a2 第二行列
   * @return 生成された行列
   */
  public static double[][] maxElementWise(final double[][] a1, final double[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (rowSize != rowSize2 || columnSize != columnSize2) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrixUtil.8")); //$NON-NLS-1$
    }
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      double[] a1i = a1[i];
      double[] a2i = a2[i];
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
  public static double[][] minElementWise(final double[][] a1, final double[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (rowSize != rowSize2 || columnSize != columnSize2) {
      throw new MatrixSizeException(Messages.getString("DoubleMatrixUtil.9")); //$NON-NLS-1$
    }
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      double[] a1i = a1[i];
      double[] a2i = a2[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = Math.min(a1i[j], a2i[j]);
      }
    }
    return ans;
  }

  /**
   * 成分を整数で割った値を成分とする行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param scalar 割る整数
   * @return 計算結果
   */
  public static double[][] divide(final double[][] matrix, final int scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][columnSize];

    double di = 1.0 / scalar;
    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      double[] ai = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = ai[j] * di;
      }
    }

    return ans;
  }

  /**
   * 成分を実数で割った値を成分とする行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param scalar 割る実数
   * @return 計算結果
   */
  public static double[][] divide(final double[][] matrix, final double scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][columnSize];

    double di = 1.0 / scalar;
    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      double[] ai = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = ai[j] * di;
      }
    }

    return ans;
  }

  /**
   * 成分毎の割り算の結果を成分とする行列を生成します。
   * 
   * @param a1 割られる行列
   * @param a2 割る行列
   * @return 割り算の結果
   */
  public static double[][] divideElementWise(final double[][] a1, final double[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] a1i = a1[i];
      double[] a2i = a2[i];
      double[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a1i[j] / a2i[j];
      }
    }
    return ans;
  }

  /**
   * 成分毎の割り算(左が分母、右が分子)の結果を成分とする行列を生成します。
   * 
   * @param a1 割る行列
   * @param a2 割られる行列
   * @return 割り算の結果
   */
  public static double[][] leftDivideElementWise(final double[][] a1, final double[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] a1i = a1[i];
      double[] a2i = a2[i];
      double[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a2i[j] / a1i[j];
      }
    }
    return ans;
  }

  /**
   * 行列の全ての成分に実数を加えた行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param scalar 加える実数
   * @return 生成された行列
   */
  public static double[][] addElementWise(final double[][] matrix, final double scalar) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      final double[] ansi = ans[i];
      final double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j] + scalar;
      }
    }
    return ans;
  }

  /**
   * 行列の全ての成分に実数を加えた行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param scalar 加える実数
   * @return 生成された行列
   */
  public static double[][] addElementWise(final int[][] matrix, final double scalar) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      final double[] ansi = ans[i];
      final int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j] + scalar;
      }
    }
    return ans;
  }
  
  /**
   * 行列の全ての成分から実数を引いた行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param scalar 引く実数
   * @return 生成された行列
   */
  public static double[][] subtractElementWise(final double[][] matrix, final double scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j] - scalar;
      }
    }
    return ans;
  }
  
  /**
   * 行列の全ての成分から実数を引いた行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param scalar 引く実数
   * @return 生成された行列
   */
  public static double[][] subtractElementWise(final int[][] matrix, final double scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j] - scalar;
      }
    }
    return ans;
  }

  /**
   * 実数の累乗を行列の全ての成分毎に計算し、計算結果を成分とする行列を生成します。
   * 
   * @param scalar 累乗される実数
   * @param matrix 累乗の指数を成分とする行列
   * @return 生成された行列
   */
  public static double[][] powerElementWise(final double scalar, final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      double[] mmi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = Math.pow(scalar, mmi[j]);
      }
    }
    return ans;
  }

  /**
   * 行列の全ての成分毎に累乗を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param matrix 累乗の対象となる値を成分とする行列
   * @param scalar 累乗の指数
   * @return 生成された行列
   */
  public static double[][] powerElementWise(final double[][] matrix, final double scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = Math.pow(matrixi[j], scalar);
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
  public static double[][] powerElementWise(final double[][] a1, final double[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      double[] a1i = a1[i];
      double[] a2i = a2[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = Math.pow(a1i[j], a2i[j]);
      }
    }
    return ans;
  }

  /**
   * 行列の成分毎に累乗を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param a1 累乗の対象となる値を成分とする行列
   * @param a2 累乗の指数(整数)を成分とする行列
   * @return 生成された行列
   */
  public static double[][] powerElementWise(final double[][] a1, final int[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      double[] matrixi = a1[i];
      int[] mmi = a2[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = Math.pow(matrixi[j], mmi[j]);
      }
    }
    return ans;
  }

  /**
   * 行列の和行列を生成します。
   * 
   * @param a1 加えられる行列
   * @param a2 加える行列
   * @return 行列の和
   */
  public static double[][] add(final double[][] a1, final double[][] a2) {
    final int rowSize = a1.length;
    final int columnSize = rowSize == 0 ? 0 : a1[0].length;
    final double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      final double[] a1i = a1[i];
      final double[] a2i = a2[i];
      final double[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a1i[j] + a2i[j];
      }
    }

    return ans;
  }

  /**
   * 行列の差行列を生成します。
   * 
   * @param a1 引かれる行列
   * @param a2 引く行列
   * @return 行列の差
   */
  public static double[][] subtract(final double[][] a1, final double[][] a2) {
    final int rowSize = a1.length;
    final int columnSize = rowSize == 0 ? 0 : a1[0].length;
    final double[][] ans = new double[rowSize][columnSize];

    for (int row = 0; row < rowSize; row++) {
      final double[] a1i = a1[row];
      final double[] a2i = a2[row];
      final double[] ansi = ans[row];
      for (int column = 0; column < columnSize; column++) {
        ansi[column] = a1i[column] - a2i[column];
      }
    }

    return ans;
  }

  /**
   * 行列の最大成分を返します。
   * 
   * @param matrix 対象となる行列
   * @return 最大成分
   */
  public static double max(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    double max = matrix[0][0];

    for (int i = 0; i < rowSize; i++) {
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        if (max < matrixi[j]) {
          max = matrixi[j];
        }
      }
    }
    return max;
  }

  /**
   * ベクトルの最大成分を返します。
   * 
   * @param vector 対象となるベクトル
   * @return 最大成分
   */
  public static double max(final double[] vector) {
    int rowSize = vector.length;
    double max = vector[0];
    for (int i = 1; i < rowSize; i++) {
      if (max < vector[i]) {
        max = vector[i];
      }
    }
    return max;
  }

  /**
   * 行列の最小成分を返します。
   * 
   * @param matrix 対象となる行列
   * @return 最小成分
   */
  public static double min(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    double min = matrix[0][0];
    for (int i = 0; i < rowSize; i++) {
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        if (min > matrixi[j]) {
          min = matrixi[j];
        }
      }
    }
    return min;
  }

  /**
   * ベクトルの最小成分を返します。
   * 
   * @param vector 対象となるベクトル
   * @return 最小成分
   */
  public static double min(final double[] vector) {
    int rowSize = vector.length;
    double min = vector[0];

    for (int i = 1; i < rowSize; i++) {
      if (min > vector[i]) {
        min = vector[i];
      }
    }
    return min;
  }

  /**
   * 全ての成分の和を返します。
   * 
   * @param matrix 対象となる行列
   * @return 全ての成分も和
   */
  public static double sum(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double sum = 0;

    for (int i = 0; i < rowSize; i++) {
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        sum += matrixi[j];
      }
    }
    return sum;
  }

  /**
   * 全ての成分の積を返します。
   * 
   * @param matrix 対象となる行列
   * @return 全ての成分の積
   */
  public static double product(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double ans = 1;

    for (int i = 0; i < rowSize; i++) {
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ans *= matrixi[j];
      }
    }
    return ans;
  }

  /**
   * 全ての成分の平均値を返します。
   * 
   * @param matrix 対象となる行列
   * @return 全ての成分の平均値
   */
  public static double mean(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    return sum(matrix) / (rowSize * columnSize);
  }

  /**
   * 全ての成分の標準偏差を返します。
   * 
   * @param matrix 対象となる行列
   * @return 全ての成分の標準偏差
   */
  public static double std(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    double m = mean(matrix);
    double d = 0;

    for (int i = 0; i < rowSize; i++) {
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        double s = matrixi[j] - m;
        d += s * s;
      }
    }
    return Math.sqrt(d / (rowSize * columnSize - 1));
  }

  /**
   * 行列のフロベニウスノムルを返します。
   * 
   * @param matrix 対象となる行列
   * @return 行列のフロベニウスノムル
   */
  public static double frobNorm(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    double sum = 0;
    for (int i = 0; i < rowSize; i++) {
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        sum += matrixi[j] * matrixi[j];
      }
    }
    return Math.sqrt(sum);
  }

  /**
   * 行毎のフロベニウスノムルを成分とする列ベクトルを返します。
   * 
   * @param matrix 対象となる行列
   * @return 行毎のフロベニウスノムルを成分とする列ベクトル
   */
  public static double[][] frobNormRowWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final double[][] ans = new double[rowSize][1];

    for (int i = 0; i < rowSize; i++) {
      double sum = 0;
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        sum += matrixi[j] * matrixi[j];
      }
      ans[i][0] = Math.sqrt(sum);
    }
    return ans;
  }

  /**
   * 列毎のフロベニウスノムルを成分とする行ベクトルを返します。
   * 
   * @param matrix 対象となる行列
   * @return 列毎のフロベニウスノムルを成分とする行ベクトル
   */
  public static double[][] frobNormColumnWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final double[][] ans = new double[1][columnSize];

    for (int i = 0; i < columnSize; i++) {
      double sum = 0;
      for (int j = 0; j < columnSize; j++) {
        sum += matrix[j][i] * matrix[j][i];
      }
      ans[0][i] = Math.sqrt(sum);
    }
    return ans;
  }

  /**
   * 
   * 行列のノルムを返します。
   * 
   * @param matrix 対象となる行列
   * @param type ノルムの種類(NormType.ONE:1ノルム、NormType.TWO:2ノルム(最大特異値))
   * @return 行列のノルム
   */
  public static double norm(final double[][] matrix, final NormType type) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return 0;
    }

    if (type == NormType.ONE) {
      double maxsum = 0;
      for (int i = 0; i < columnSize; i++) {
        double sum = 0;
        for (int j = 0; j < rowSize; j++) {
          sum += Math.abs(matrix[j][i]);
        }
        if (sum > maxsum) {
          maxsum = sum;
        }
      }
      return maxsum;
    } else if (type == NormType.TWO) {
      return new DoubleRealSingularValueDecomposer().norm(matrix);
    } else {
      throw new IllegalArgumentException("DoubleMatrix.norm(): " + Messages.getString("DoubleMatrixUtil.10")); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * 行列の無限大ノルムを返します。
   * 
   * @param matrix 対象となる行列
   * @return 行列の無限大ノルム
   */
  public static double infNorm(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return 0;
    }

    double max = 0;

    for (int i = 0; i < rowSize; i++) {
      double d = 0;
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        d += Math.abs(matrixi[j]);
      }
      if (max < d) {
        max = d;
      }
    }
    return max;
  }

  /**
   * 全対角成分の和(トレース)を返します。
   * 
   * @param matrix 対象となる行列
   * @return 対角成分の合計(トレース)
   */
  public static double trace(final double[][] matrix) {
    int size = matrix.length;
    double ans = 0;
    for (int i = 0; i < size; i++) {
      ans += matrix[i][i];
    }
    return ans;
  }

  /**
   * 対角成分からなる縦ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 対角成分からなる縦ベクトル
   */
  public static double[][] diagonalToVector(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int min = Math.min(rowSize, columnSize);
    double[][] ans = new double[min][1];

    for (int i = 0; i < min; i++) {
      ans[i][0] = matrix[i][i];
    }
    return ans;
  }

  /**
   * ベクトルの成分を昇順にソートした結果を返します。
   * 
   * @param vector 対象となるベクトル
   * @param index 並び替えた成分の番号を記憶する配列
   * @param start ソートの対象となる成分の開始番号
   * @param end ソートの対象となる成分の終了番号
   */
  public static void quickSort(final double[] vector, final int[] index, final int start, final int end) {
    if (start >= end) {
      return;
    }

    // 基準値 piv
    double piv = vector[(start + end) >>> 1]; // divide by 2
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
        double tmp = vector[i];
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
   * 行毎に昇順に並び替えた行列と元の位置を示す指数を返します。
   * 
   * @param matrix 対象となる行列
   * @return 行毎に昇順に並び替えた行列と元の位置を示す指数
   */
  public static IndexedDoubleElements sortRowWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] index = new int[rowSize][columnSize];

    double[][] ans = clone(matrix);
    for (int i = 0; i < rowSize; i++) {
      int[] indexi = index[i];
      for (int j = 0; j < columnSize; j++) {
        indexi[j] = j + 1;
      }
      DoubleMatrixUtil.quickSort(ans[i], index[i], 0, columnSize - 1);
    }

    final IndexedDoubleElements sortedElements = new IndexedDoubleElements(ans, index);
    return sortedElements;
  }

  /**
   * 列毎とに昇順に並び替えた行列と元の位置を示す指数を返します。
   * 
   * @param matrix 対象となる行列
   * @return 列毎に昇順に並び替えた行列と元の位置を示す指数
   */
  public static IndexedDoubleElements sortColumnWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] index = new int[columnSize][rowSize];

    double[][] ans = transpose(matrix);
    for (int i = 0; i < columnSize; i++) {
      int[] indexi = index[i];
      for (int j = 0; j < rowSize; j++) {
        indexi[j] = j + 1;
      }
      DoubleMatrixUtil.quickSort(ans[i], index[i], 0, rowSize - 1);
    }
    return new IndexedDoubleElements(transpose(ans), IntMatrixUtil.transpose(index));
  }

  /**
   * ストリームトークンナイザで読み込んだ行列を返します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param st データを読み込むストリームトークンナイザ
   * @return 読み込んだ行列
   * @throws IOException ストリームトークンナイザで読み込めない場合
   */
  public static double[][] readMatFormat(final int rowSize, final int columnSize, final StreamTokenizer st) throws IOException {
    double[][] ans = new double[rowSize][columnSize];

    for (int j = 0; j < columnSize; j++) {
      for (int i = 0; i < rowSize; i++) {
        if (st.nextToken() == StreamTokenizer.TT_EOF) {
          throw new IOException(Messages.getString("DoubleMatrixUtil.11")); //$NON-NLS-1$
        }
        if (st.ttype == StreamTokenizer.TT_WORD) {
          ans[i][j] = Double.parseDouble(st.sval);
        } else {
          throw new IOException(Messages.getString("DoubleMatrixUtil.12")); //$NON-NLS-1$
        }
      }
    }
    return ans;
  }

  /**
   * リーダーから読み込んだ行列を返します。
   * 
   * @param input データの読み込み元
   * @return 読み込んだ行列
   * @throws IOException データを読み込めない場合
   */
  public static double[][] readCsvFormat(final Reader input) throws IOException {
    final BufferedReader reader = new BufferedReader(input);

    final List<double[]> matrix = new ArrayList<>();
    String line = ""; //$NON-NLS-1$
    int columnSize = 0;
    
    final Pattern pattern = Pattern.compile("[0-9a-zA-Z\\+\\-\\.]"); //$NON-NLS-1$
    while ((line = reader.readLine()) != null) {
      final String trimedLine = line.trim();
      if (trimedLine.length() == 0 ||   pattern.matcher(trimedLine.substring(0, 1)).find() == false) {
        continue;
      }
      
      final String[] strings = trimedLine.split("[,]"); //$NON-NLS-1$
      final int size = strings.length;
      if (columnSize != 0 && size != columnSize) {
        throw new IOException(Messages.getString("DoubleMatrixUtil.14")); //$NON-NLS-1$
      }
      columnSize = size;
      final double[] rowVector = new double[columnSize];
      for (int i = 0; i < columnSize; i++) {
        rowVector[i] = Double.parseDouble(strings[i]);
      }
      matrix.add(rowVector);
    }

    final int rowSize = matrix.size();
    return matrix.toArray(new double[rowSize][columnSize]);
  }
  
  /**
   * リーダーから指定した列を読み込み，行列を返します。
   * 
   * @param input データの読み込み元
   * @param indices indices of column to read (start from 0)
   * @return 読み込んだ行列
   * @throws IOException データを読み込めない場合
   */
  public static double[][] readCsvFormat(final Reader input, int[] indices) throws IOException {
    final BufferedReader reader = new BufferedReader(input);

    final List<double[]> matrix = new ArrayList<>();
    String line = ""; //$NON-NLS-1$
    final int columnSize = indices.length;
    
    final Pattern pattern = Pattern.compile("[0-9a-zA-Z\\+\\-\\.]"); //$NON-NLS-1$
    while ((line = reader.readLine()) != null) {
      final String trimedLine = line.trim();
      if (trimedLine.length() == 0 ||  pattern.matcher(trimedLine.substring(0, 1)).find() == false) {
        continue;
      }
      
      final String[] strings = trimedLine.split("[,]"); //$NON-NLS-1$

      final double[] rowVector = new double[columnSize];
      for (int i = 0; i < columnSize; i++) {
        final int index = indices[i];
        rowVector[i] = Double.parseDouble(strings[index].trim());
      }
      matrix.add(rowVector);
    }

    final int rowSize = matrix.size();
    return matrix.toArray(new double[rowSize][columnSize]);
  }
  
  /**
   * リーダーから読み込んだ行列を返します。
   * 
   * @param input データの読み込み元
   * @return 読み込んだ行列
   * @throws IOException データを読み込めない場合
   */
  public static double[][] readSsvFormat(final Reader input) throws IOException {
    final BufferedReader reader = new BufferedReader(input);
    
    final List<double[]> matrix = new ArrayList<>();
    String line = ""; //$NON-NLS-1$
    int columnSize = 0;
    
    final Pattern pattern = Pattern.compile("[0-9a-zA-Z\\+\\-\\.]"); //$NON-NLS-1$
    while ((line = reader.readLine()) != null) {
      String trimedLine = line.trim();
      if (pattern.matcher(trimedLine.substring(0, 1)).find() == false) {
        continue;
      }

      trimedLine = trimedLine.replaceAll("[ \\t]+", " "); //$NON-NLS-1$ //$NON-NLS-2$
            
      final String[] strings = trimedLine.split("[ ]"); //$NON-NLS-1$
      final int size = strings.length;
      if (columnSize != 0 && size != columnSize) {
        throw new IOException(Messages.getString("DoubleMatrixUtil.14")); //$NON-NLS-1$
      }
      columnSize = size;
      final double[] rowVector = new double[columnSize];
      for (int i = 0; i < columnSize; i++) {
        rowVector[i] = Double.parseDouble(strings[i]);
      }
      matrix.add(rowVector);
    }

    final int rowSize = matrix.size();
    return matrix.toArray(new double[rowSize][columnSize]);
  }

  /**
   * リーダーから読み込んだ行列を返します。
   * 
   * @param input データの読み込み元
   * @param indices indices of column to read  (start from 0)
   * @return 読み込んだ行列
   * @throws IOException データを読み込めない場合
   */
  public static double[][] readSsvFormat(final Reader input, int[] indices) throws IOException {
    final BufferedReader reader = new BufferedReader(input);
    
    final List<double[]> matrix = new ArrayList<>();
    String line = ""; //$NON-NLS-1$
    final int columnSize = indices.length;
    
    final Pattern pattern = Pattern.compile("[0-9a-zA-Z\\+\\-\\.]"); //$NON-NLS-1$
    while ((line = reader.readLine()) != null) {
      String trimedLine = line.trim();
      if (trimedLine.length() == 0 ||  pattern.matcher(trimedLine.substring(0, 1)).find() == false) {
        continue;
      }
      
      trimedLine = trimedLine.replaceAll("[ \\t]+", " "); //$NON-NLS-1$ //$NON-NLS-2$
      
      final String[] strings = trimedLine.split("[ ]"); //$NON-NLS-1$
      final double[] rowVector = new double[columnSize];
      for (int i = 0; i < columnSize; i++) {
        final int index = indices[i];
        rowVector[i] = Double.parseDouble(strings[index]);
      }
      matrix.add(rowVector);
    }

    final int rowSize = matrix.size();
    return matrix.toArray(new double[rowSize][columnSize]);
  }

  /**
   * ライターに行列をCSVフォーマットで出力します。
   * 
   * @param matrix 対象となる行列
   * @param output ライター
   * @param separator 分離用の文字列
   * @throws IOException 出力できない場合
   */
  public static void writeCsvFormat(final double[][] matrix, final Writer output, final String separator) throws IOException {
    final BufferedWriter bw = new BufferedWriter(output);
    final int rowSize = matrix.length;
    final int columnSize = matrix[0].length;

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        final double d = matrix[i][j];
        if (Double.isInfinite(d)) {
          bw.write(" " + Double.toString(d)); //$NON-NLS-1$
        } else if (Double.isNaN(d)) {
          bw.write(" " + Double.toString(d)); //$NON-NLS-1$
        } else {
          bw.write(String.format("%16.8E", Double.valueOf(d))); //$NON-NLS-1$
        }
        if (j != columnSize - 1) {
          bw.write(separator);
        }
      }
      bw.newLine();
    }
    bw.flush();
  }

  /**
   * ライターに行列をMATフォーマットで出力します。
   * 
   * @param matrix 対象となる行列
   * @param output ライター
   * @throws IOException ライターに出力できない場合
   */
  public static void writeMatFormat(final double[][] matrix, final Writer output) throws IOException {
    final BufferedWriter bw = new BufferedWriter(output);
    final int rowSize = matrix.length;
    final int columnSize = matrix[0].length;

    bw.write("# " + rowSize + " " + columnSize); //$NON-NLS-1$ //$NON-NLS-2$
    bw.newLine();
    for (int j = 0; j < columnSize; j++) {
      for (int i = 0; i < rowSize; i++) {
        final double d = matrix[i][j];
        if (Double.isInfinite(d)) {
          bw.write(Double.toString(d));
        } else if (Double.isNaN(d)) {
          bw.write(Double.toString(d));
        } else {
          bw.write(String.format("%16.8E", Double.valueOf(d))); //$NON-NLS-1$
        }
        if (i != rowSize - 1) {
          bw.write(" "); //$NON-NLS-1$
        }
      }
      bw.newLine();
    }
    bw.flush();
  }

  /**
   * 行列を出力ストリームにMXフォーマットで出力します。
   * 
   * @param matrix 対象となる行列
   * @param output 出力ストリーム
   * @param name 行列の名前
   * @throws IOException ストリームに出力できない場合
   */
  public static void writeMxFormat(final double[][] matrix, final OutputStream output, final String name) throws IOException {
    MxDataHead head = new MxDataHead(matrix, name);

    head.write(output);

    DataOutputStream ds = new DataOutputStream(new BufferedOutputStream(output));

    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    for (int i = 0; i < rowSize; i++) {
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ds.writeDouble(matrixi[j]);
      }
    }

    ds.flush();
  }

  /**
   * 入力ストリームから読み込んだ(MXフォーマット)行列を返します。
   * 
   * @param input 入力ストリーム
   * @param head MXフォーマットのヘッダ情報
   * @return 読み込んだ行列
   * @throws IOException 入力ストリームから読み込めない場合
   */
  public static double[][] readMxFormat(final InputStream input, final MxDataHead head) throws IOException {
    int rowSize = head.getRowSize();
    int columnSize = head.getColumnSize();
    double[][] ans = new double[rowSize][columnSize];

    DataInputStream is = new DataInputStream(input);

    if (head.isSameEndian()) {
      for (int i = 0; i < rowSize; i++) {
        double[] ansi = ans[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = is.readDouble();
        }
      }
    } else {
      for (int i = 0; i < rowSize; i++) {
        double[] ansi = ans[i];
        for (int j = 0; j < columnSize; j++) {
          long d = EndianTransformer.flip(is.readLong());
          ansi[j] = Double.longBitsToDouble(d);
        }
      }
    }
    return ans;
  }

  /**
   * 行列をMMフォーマットの文字列に変換します。
   * 
   * @param matrix 対象となる行列
   * @param format 成分の出力フォーマット
   * @return MMフォーマットの文字列
   */
  public static String toMmString(final double[][] matrix, final String format) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    StringBuffer sb = new StringBuffer();
    String newLine = System.getProperty("line.separator"); //$NON-NLS-1$

    if (rowSize == 0 || columnSize == 0) {
      return "[[]]"; //$NON-NLS-1$
    }

    if (columnSize == 1 && rowSize != 1) {
      return toMmString(transpose(matrix), format) + "'"; //$NON-NLS-1$
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
          sb.append(DoubleNumber.toString(matrix[i][j], format));
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
   * 行列を1行文字列に変換します。
   * 
   * @param matrix 対象となる行列
   * @param format 成分の出力フォーマット
   * @return 1行文字列
   */
  public static String toString(final double[][] matrix, final String format) {
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
          sb.append(DoubleNumber.toString(matrix[i][j], format));
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

  
  /**
   * 行毎に全ての成分の積を計算し、計算結果を成分とする列ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする列ベクトル
   */
  public static double[][] productRowWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][1];

    for (int i = 0; i < rowSize; i++) {
      double prod = 1;
      double[] matrixi = matrix[i];
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
  public static double[][] productColumnWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[1][columnSize];

    for (int i = 0; i < columnSize; i++) {
      double prod = 1;
      for (int j = 0; j < rowSize; j++) {
        prod *= matrix[j][i];
      }
      ans[0][i] = prod;
    }

    return ans;
  }

  /**
   * 行毎に全ての成分の和を計算し、計算結果を成分とする列ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする列ベクトル
   */
  public static double[][] sumRowWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][1];

    for (int i = 0; i < rowSize; i++) {
      double sum = 0;
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        sum += matrixi[j];
      }
      ans[i][0] = sum;
    }

    return ans;
  }

  /**
   * 列毎に全ての成分の和を計算し、計算結果を成分とする行ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行ベクトル
   */
  public static double[][] sumColumnWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[1][columnSize];

    for (int i = 0; i < columnSize; i++) {
      double sum = 0;
      for (int j = 0; j < rowSize; j++) {
        sum += matrix[j][i];
      }
      ans[0][i] = sum;
    }

    return ans;
  }

  /**
   * 列毎に全ての成分の平均値を計算し、計算結果を成分とする行ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行ベクトル
   */
  public static double[][] meanColumnWise(final double[][] matrix) {
    int rowSize = matrix.length;
    return divide(sumColumnWise(matrix), rowSize);
  }

  /**
   * 行毎に全ての成分の平均値を計算し、計算結果を成分とする列ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする列ベクトル
   */
  public static double[][] meanRowWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    return divide(sumRowWise(matrix), columnSize);
  }

  /**
   * 列毎に累積積を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行列
   */
  public static double[][] cumulativeProductColumnWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < columnSize; i++) {
      double prod = 1;
      for (int j = 0; j < rowSize; j++) {
        prod *= matrix[j][i];
        ans[j][i] = prod;
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
  public static double[][] cumulativeProductRowWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double prod = 1;
      double[] matrixi = matrix[i];
      double[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        prod *= matrixi[j];
        ansi[j] = prod;
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
  public static double[][] cumulativeSumColumnWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < columnSize; i++) {
      double sum = 0;
      for (int j = 0; j < rowSize; j++) {
        sum += matrix[j][i];
        ans[j][i] = sum;
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
  public static double[][] cumulativeSumRowWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double sum = 0;
      double[] matrixi = matrix[i];
      double[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        sum += matrixi[j];
        ansi[j] = sum;
      }
    }

    return ans;
  }

  /**
   * 列毎に最小値を計算し、計算結果を成分とする行ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行ベクトル
   */
  public static double[][] minColumnWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[1][columnSize];

    for (int i = 0; i < columnSize; i++) {
      double min = matrix[0][i];
      for (int j = 1; j < rowSize; j++) {
        if (min > matrix[j][i]) {
          min = matrix[j][i];
        }
      }
      ans[0][i] = min;
    }

    return ans;
  }

  /**
   * 行毎に最小値を計算し、計算結果を成分とする行ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行ベクトル
   */
  public static double[][] minRowWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][1];

    for (int i = 0; i < rowSize; i++) {
      double min = matrix[i][0];
      double[] matrixi = matrix[i];
      for (int j = 1; j < columnSize; j++) {
        if (min > matrixi[j]) {
          min = matrixi[j];
        }
      }
      ans[i][0] = min;
    }

    return ans;
  }

  /**
   * 列毎に最大値を計算し、計算結果を成分とする行ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行ベクトル
   */
  public static double[][] maxColumnWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[1][columnSize];

    for (int i = 0; i < columnSize; i++) {
      double max = matrix[0][i];
      for (int j = 1; j < rowSize; j++) {
        if (max < matrix[j][i]) {
          max = matrix[j][i];
        }
      }
      ans[0][i] = max;
    }

    return ans;
  }

  /**
   * 行毎に最大値を計算し、計算結果を成分とする列ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする列ベクトル
   */
  public static double[][] maxRowWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][1];

    for (int i = 0; i < rowSize; i++) {
      double max = matrix[i][0];
      double[] matrixi = matrix[i];
      for (int j = 1; j < columnSize; j++) {
        if (max < matrixi[j]) {
          max = matrixi[j];
        }
      }
      ans[i][0] = max;
    }

    return ans;
  }

  /**
   * 列毎に標準偏差を計算し、計算結果を成分とする列ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする列ベクトル
   */
  public static double[][] stdColumnWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] mn = meanColumnWise(matrix);
    double[][] ans = new double[1][columnSize];

    for (int j = 0; j < columnSize; j++) {
      double sum = 0;
      for (int i = 0; i < rowSize; i++) {
        double x = matrix[i][j] - mn[0][j];
        sum += x * x;
      }
      ans[0][j] = Math.sqrt(sum / (rowSize - 1));
    }
    return ans;
  }

  /**
   * 行毎に標準偏差を計算し、計算結果を成分とする列ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする列ベクトル
   */
  public static double[][] stdRowWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] mn = meanRowWise(matrix);
    double[][] ans = new double[rowSize][1];

    for (int i = 0; i < rowSize; i++) {
      double sum = 0;
      double[] matrixi = matrix[i];
      double[] mni = mn[i];
      for (int j = 0; j < columnSize; j++) {
        double x = matrixi[j] - mni[0];
        sum += x * x;
      }
      ans[i][0] = Math.sqrt(sum / (columnSize - 1));
    }
    return ans;
  }

  /**
   * 全ての成分の累積積を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行列
   */
  public static double[][] cumulativeProduct(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][columnSize];

    double prod = 1;
    for (int i = 0; i < rowSize; i++) {
      double[] matrixi = matrix[i];
      double[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        prod *= matrixi[j];
        ansi[j] = prod;
      }
    }
    return ans;
  }

  /**
   * 全ての成分の累積和を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行列
   */
  public static double[][] cumulativeSum(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][columnSize];
    double sum = 0;

    for (int i = 0; i < rowSize; i++) {
      double[] matrixi = matrix[i];
      double[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        sum += matrixi[j];
        ansi[j] = sum;
      }
    }
    return ans;
  }

  /**
   * 最小成分と指数を返します。
   * 
   * @param matrix 対象となる行列
   * @return 最小成分とその指数
   */
  public static Object[] minimum(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[] index = indexOfMinimum(matrix);

    if (rowSize == 1) {
      return new Object[] {Double.valueOf(matrix[0][index[1] - 1]), Integer.valueOf(index[1])};
    } else if (columnSize == 1) {
      return new Object[] {Double.valueOf(matrix[index[0] - 1][0]), Integer.valueOf(index[0])};
    } else {
      return new Object[] {Double.valueOf(matrix[index[0] - 1][index[1] - 1]), Integer.valueOf(index[0]), Integer.valueOf(index[1])};
    }
  }

  /**
   * 最小成分の指数を返します。
   * 
   * @param matrix 対象となる行列
   * @return 最小成分の指数
   */
  public static int[] indexOfMinimum(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double min = Double.MAX_VALUE;
    int mk = 0, nk = 0;

    for (int i = 0; i < rowSize; i++) {
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        double d = matrixi[j];
        if (min > d) {
          min = d;
          mk = i + 1;
          nk = j + 1;
        }
      }
    }

    int[] index = new int[] {mk, nk};
    return index;
  }

  /**
   * 列毎に最小成分とその指数を返します。
   * 
   * @param matrix 対象となる行列
   * @return 列毎の最小成分とその指数
   */
  public static Object[] minimumColumnWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[] ans = new double[columnSize];
    int[] idx = new int[rowSize];

    for (int i = 0; i < columnSize; i++) {
      double min = matrix[0][i];
      idx[i] = 1;
      for (int j = 1; j < rowSize; j++) {
        if (min > matrix[j][i]) {
          min = matrix[j][i];
          idx[i] = j + 1;
        }
      }
      ans[i] = min;
    }

    return new Object[] {ans, idx};
  }

  /**
   * 行毎の最小成分とその指数を返します。
   * 
   * @param matrix 対象となる行列
   * @return 行毎の最小成分とその指数
   */
  public static Object[] minimumRowWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][1];
    int[] idx = new int[rowSize];

    for (int i = 0; i < rowSize; i++) {
      double min = matrix[i][0];
      idx[i] = 1;
      double[] matrixi = matrix[i];
      for (int j = 1; j < columnSize; j++) {
        if (min > matrixi[j]) {
          min = matrixi[j];
          idx[i] = j + 1;
        }
      }
      ans[i][0] = min;
    }

    return new Object[] {ans, idx};
  }

  /**
   * 最大成分とその指数を返します。
   * 
   * @param matrix 対象となる行列
   * @return 最大成分とその指数
   */
  public static Object[] maximum(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int[] index = indexOfMaximum(matrix);

    if (rowSize == 1) {
      return new Object[] {Double.valueOf(matrix[0][index[1] - 1]), Integer.valueOf(index[1])};
    } else if (columnSize == 1) {
      return new Object[] {Double.valueOf(matrix[index[0] - 1][0]), Integer.valueOf(index[0])};
    } else {
      return new Object[] {Double.valueOf(matrix[index[0] - 1][index[1] - 1]), Integer.valueOf(index[0]), Integer.valueOf(index[1])};
    }
  }

  /**
   * 最大成分の指数を求めます。
   * 
   * @param matrix 対象となる行列
   * @return 最大成分の指数
   */
  public static int[] indexOfMaximum(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double max = Double.MIN_VALUE;
    int mk = 0;
    int nk = 0;

    for (int i = 0; i < rowSize; i++) {
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        double d = matrixi[j];
        if (max < d) {
          max = d;
          mk = i + 1;
          nk = j + 1;
        }
      }
    }

    int[] index = new int[] {mk, nk};
    return index;
  }

  /**
   * 列毎の最大成分とその指数を返します。
   * 
   * @param matrix 対象となる行列
   * @return 列毎の最大成分とその指数
   */
  public static Object[] maximumColumnWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[] ans = new double[columnSize];
    int[] idx = new int[rowSize];

    for (int i = 0; i < columnSize; i++) {
      double max = matrix[0][i];
      idx[i] = 1;
      for (int j = 1; j < rowSize; j++) {
        if (max < matrix[j][i]) {
          max = matrix[j][i];
          idx[i] = j + 1;
        }
      }
      ans[i] = max;
    }

    return new Object[] {ans, idx};
  }

  /**
   * 行毎の最大成分とその指数を返します。
   * 
   * @param matrix 対象となる行列
   * @return 行毎の最大成分とその指数
   */
  public static Object[] maximumRowWise(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][1];
    int[] idx = new int[rowSize];

    for (int i = 0; i < rowSize; i++) {
      double max = matrix[i][0];
      idx[i] = 1;
      double[] matrixi = matrix[i];
      for (int j = 1; j < columnSize; j++) {
        if (max < matrixi[j]) {
          max = matrixi[j];
          idx[i] = j + 1;
        }
      }
      ans[i][0] = max;
    }

    return new Object[] {ans, idx};
  }

  /**
   * 成分毎に関数の計算をし、計算結果を成分とする行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param function 実数関数(引数1個)
   * @return 計算結果を成分とする行列
   */
  public static double[][] elementWiseFunction(final double[][] matrix, final DoubleRealFunction function) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = function.evaluate(matrixi[j]);
      }
    }
    return ans;
  }

  /**
   * 成分毎に関数の計算をし、計算結果を成分とする行列を生成します。
   * 
   * @param a1 第一引数を成分とする行列
   * @param a2 第二引数を成分とする行列
   * @param function 実数関数(引数2個)
   * @return 計算結果を成分とする行列
   */
  public static double[][] elementWiseFunction(final double[][] a1, final double[][] a2, final DoubleRealFunctionWithTwoArguments function) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] matrixi = a1[i];
      double[] ansi = ans[i];
      double[] ni = a2[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = function.evaluate(matrixi[j], ni[j]);
      }
    }
    return ans;
  }

  /**
   * 成分毎に関数の計算をし、計算結果を成分とする行列を生成します。
   * 
   * @param matrix 第一引数を成分とする行列
   * @param d2 第二引数
   * @param function 実数関数(引数2個)
   * @return 計算結果を成分とする行列
   */
  public static double[][] elementWiseFunction(final double[][] matrix, final double d2, final DoubleRealFunctionWithTwoArguments function) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] matrixi = matrix[i];
      double[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = function.evaluate(matrixi[j], d2);
      }
    }
    return ans;
  }

  /**
   * 成分毎に関数の計算をし、計算結果を成分とする行列を生成します。
   * 
   * @param matrix 関数の引数を成分とする行列
   * @param function ブーリアン関数(引数1個)
   * @return 計算結果を成分とする行列(ブーリアン)
   */
  public static boolean[][] elementWiseFunction(final double[][] matrix, final BooleanFunction function) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      boolean[] ansi = ans[i];
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = function.evaluate(matrixi[j]);
      }
    }
    return ans;
  }

  /**
   * 各成分とvalueをoperatorで指定された演算子で比較し、 計算結果を成分とする行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param opponent 比較対象
   * @return 計算結果を成分とする行列
   */
  public static boolean[][] compareElements(final double[][] matrix, final String operator, final double opponent) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];

    if (operator.equals(".>")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = matrixi[j] > opponent;
        }
      }
    } else if (operator.equals(".>=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = matrixi[j] >= opponent;
        }
      }
    } else if (operator.equals(".<")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = matrixi[j] < opponent;
        }
      }
    } else if (operator.equals(".<=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = matrixi[j] <= opponent;
        }
      }
    } else if (operator.equals(".==")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = matrixi[j] == opponent;
        }
      }
    } else if (operator.equals(".!=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = matrixi[j] != opponent;
        }
      }
    } else {
      throw new IllegalArgumentException(Messages.getString("DoubleMatrixUtil.27")); //$NON-NLS-1$
    }
    return ans;
  }

  /**
   * 各成分とvalueをoperatorで指定された演算子で比較し、 計算結果を成分とする行列を生成します。
   * 
   * @param matrix 対象となる行列
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param opponent 比較対象
   * @return 計算結果を成分とする行列
   */
  public static boolean[][] compareElements(final double[][] matrix, final String operator, final Scalar<?,?> opponent) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];

    if (operator.equals(".>")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = opponent.compare(".<", matrixi[j]); //$NON-NLS-1$
        }
      }
    } else if (operator.equals(".>=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = opponent.compare(".<=", matrixi[j]); //$NON-NLS-1$
        }
      }
    } else if (operator.equals(".<")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = opponent.compare(".>", matrixi[j]); //$NON-NLS-1$
        }
      }
    } else if (operator.equals(".<=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = opponent.compare(".>=", matrixi[j]); //$NON-NLS-1$
        }
      }
    } else if (operator.equals(".==")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = opponent.equals(new DoubleNumber(matrixi[j]));
        }
      }
    } else if (operator.equals(".!=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] matrixi = matrix[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = (opponent.equals(new DoubleNumber(matrixi[j])) == false);
        }
      }
    } else {
      throw new IllegalArgumentException(Messages.getString("DoubleMatrixUtil.32")); //$NON-NLS-1$
    }
    return ans;
  }

  /**
   * 2個の行列を成分毎にoperatorで指定された演算子で比較し、 計算結果を成分とする行列を生成します。
   * 
   * @param a1 第一行列
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param a2 第二行列
   * @return 計算結果を成分とする行列
   */
  public static boolean[][] compareElements(final double[][] a1, final String operator, final double[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];

    if (operator.equals(".>")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] a1i = a1[i];
        double[] a2i = a2[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = a1i[j] > a2i[j];
        }
      }
    } else if (operator.equals(".>=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] a1i = a1[i];
        double[] a2i = a2[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = a1i[j] >= a2i[j];
        }
      }
    } else if (operator.equals(".<")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] a1i = a1[i];
        double[] a2i = a2[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = a1i[j] < a2i[j];
        }
      }
    } else if (operator.equals(".<=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] a1i = a1[i];
        double[] a2i = a2[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = a1i[j] <= a2i[j];
        }
      }
    } else if (operator.equals(".==")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] a1i = a1[i];
        double[] a2i = a2[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = a1i[j] == a2i[j];
        }
      }
    } else if (operator.equals(".!=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] a1i = a1[i];
        double[] a2i = a2[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = a1i[j] != a2i[j];
        }
      }
    } else {
      throw new IllegalArgumentException(Messages.getString("DoubleMatrixUtil.33")); //$NON-NLS-1$
    }
    return ans;
  }

  /**
   * 2個の行列を成分毎にoperatorで指定された演算子で比較し、 計算結果を成分とする行列を生成します。
   * 
   * @param a1 第一行列
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param a2 第二行列
   * @return 計算結果を成分とする行列
   */
  public static boolean[][] compareElements(final double[][] a1, final String operator, final int[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];

    if (operator.equals(".>")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] a1i = a1[i];
        int[] a2i = a2[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = a1i[j] > a2i[j];
        }
      }
    } else if (operator.equals(".>=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] a1i = a1[i];
        int[] a2i = a2[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = a1i[j] >= a2i[j];
        }
      }
    } else if (operator.equals(".<")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] a1i = a1[i];
        int[] a2i = a2[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = a1i[j] < a2i[j];
        }
      }
    } else if (operator.equals(".<=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] a1i = a1[i];
        int[] a2i = a2[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = a1i[j] <= a2i[j];
        }
      }
    } else if (operator.equals(".==")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] a1i = a1[i];
        int[] a2i = a2[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = a1i[j] == a2i[j];
        }
      }
    } else if (operator.equals(".!=")) { //$NON-NLS-1$
      for (int i = 0; i < rowSize; i++) {
        boolean[] ansi = ans[i];
        double[] a1i = a1[i];
        int[] a2i = a2[i];
        for (int j = 0; j < columnSize; j++) {
          ansi[j] = a1i[j] != a2i[j];
        }
      }
    } else {
      throw new IllegalArgumentException(Messages.getString("DoubleMatrixUtil.34")); //$NON-NLS-1$
    }
    return ans;
  }

  /**
   * <code>newRowSize</code>*<code>newColumnSize</code>にサイズ変更します。
   * 
   * <p>{@link #reshape(double[][], int, int)}とは異なり、成分位置の変更はせず, 自身より大きなサイズに変更する時は,0が埋められ、 自身より小さなサイズに変更する時は余分な成分は切り取られます。
   * 
   * @param matrix 対象となる行列
   * @param newRowSize 新しい行の数
   * @param newColumnSize 新しいレスう
   * @return サイズ変更後の行列
   */
  public static double[][] resize(final double[][] matrix, final int newRowSize, final int newColumnSize) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    double[][] ans = new double[newRowSize][newColumnSize];
    int re = Math.min(newRowSize, rowSize) - 1;
    int ce = Math.min(newColumnSize, columnSize) - 1;
    setSubMatrix(ans, 0, 0, matrix, 0, re, 0, ce);
    return ans;
  }

  /**
   * 全ての成分の累乗を成分とする行列を生成します。
   * 
   * @param matrix 元の行列
   * @param d 累乗の指数
   * @return 成分の累乗を成分とする行列
   */
  public static double[][] powerElementWise(final int[][] matrix, final double d) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    double[][] ans = new double[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = Math.pow(matrixi[j], d);
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
  public static double[][] powerElementWise(final int[][] a1, final double[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      int[] a1i = a1[i];
      double[] a2i = a2[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = Math.pow(a1i[j], a2i[j]);
      }
    }
    return ans;
  }

  /**
   * 実数の累乗を行列の全ての成分毎に計算し、計算結果を成分とする行列を生成します。
   * 
   * @param scalar 累乗される実数
   * @param matrix 累乗の指数を成分とする行列
   * @return 生成された行列
   */
  public static double[][] powerElementWise(final double scalar, final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    double[][] ans = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      int[] mmi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = Math.pow(scalar, mmi[j]);
      }
    }
    return ans;
  }

  /**
   * 整数行列を実数行列に変換します。
   * 
   * @param matrix 整数行列
   * @return 実数行列
   */
  public static double[][] createArray(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    double[][] ans = new double[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      double[] ansi = ans[i];
      int[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j];
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
   * @param alignment 成分の出力配置
   * @param maxColumnSize 最大列の数
   */
  public static void print(final double[][] matrix, final Writer output, final String format, final GridElementAlignment alignment, final int maxColumnSize) {
    final PrintWriter writer = new PrintWriter(output);
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int remain = columnSize;
    int columnOffset = 0;

    final int[] printingColumnWidthes = getPrintingColumnWidthes(matrix, format);

    while (remain > 0) {
      final int printColumnSize = Math.min(maxColumnSize, remain);
      DoubleMatrixUtil.printColumnNumber(columnOffset, printColumnSize, printingColumnWidthes, writer);

      for (int row = 0; row < rowSize; row++) {
        GridUtil.printRowNumber(row, writer);

        final double[] matrixi = matrix[row];

        final double[] elements = new double[printColumnSize];
        for (int column = 0; column < printColumnSize; column++) {
          elements[column] = matrixi[column + columnOffset];
        }

        DoubleMatrixUtil.printElements(elements, writer, printingColumnWidthes, format, alignment);
      }
      columnOffset += printColumnSize;
      remain -= printColumnSize;
    }

    writer.flush();
  }

  /**
   * 行列の各列の出力文字列の長さ(幅)の配列を返します。
   * 
   * @param elements 行列
   * @param format 成分の出力フォーマット
   * @return 行列の各列の出力文字列の長さ(幅)の配列
   */
  private static int[] getPrintingColumnWidthes(final double[][] elements, final String format) {
    final int rowSize = elements.length;
    final int columnSize = rowSize == 0 ? 0 : elements[0].length;
    final int[] printingColumnWidthes = new int[columnSize];

    final int minimumColumnWidth = 6;
    
    for (int column = 0; column < columnSize; column++) {
      final double[] columnElements = new double[rowSize];
      for (int row = 0; row < rowSize; row++) {
        columnElements[row] = elements[row][column];
      }

      final int formatLength = 3;
      final String columnNumber = String.format("(%" + formatLength + "d)", Integer.valueOf(column)); //$NON-NLS-1$ //$NON-NLS-2$
      final int columnNumberWidth = columnNumber.length();
      final int elementWidth = getElementWidth(columnElements, format);
      printingColumnWidthes[column] = Math.max(Math.max(columnNumberWidth, elementWidth), minimumColumnWidth);
    }

    return printingColumnWidthes;
  }

  /**
   * 成分の出力文字列の最大値を返します。
   * 
   * @param elements 成分の配列
   * @param format 出力フォーマット
   * @return 成分の出力文字列の最大値
   */
  static int getElementWidth(final double[] elements, final String format) {
    int maxLength = 0;
    for (final double element : elements) {
      final int length = DoubleNumber.toString(element, format).length();
      maxLength = Math.max(maxLength, length);
    }

    return maxLength;
  }

  /**
   * 複数個の実数をプリントライターに出力する。
   * 
   * @param elements 実数の配列
   * @param output プリントライター
   * @param printingColumnWidthes 列の幅
   * @param format 成分の出力フォーマット
   * @param alignment 成分の出力配置
   */
  private static void printElements(final double[] elements, final PrintWriter output, final int[] printingColumnWidthes, final String format, final GridElementAlignment alignment) {
    for (int column = 0; column < elements.length; column++) {
      final int printingColumnWidth = printingColumnWidthes[column];

      final String element = DoubleNumber.toString(elements[column], format);

      final int elementWidth = element.length();
      final int gap = printingColumnWidth - elementWidth + 2;

      if (alignment == GridElementAlignment.RIGHT) {
        GridUtil.outputSpaces(gap, output);
      }

      if (alignment == GridElementAlignment.CENTER) {
        GridUtil.outputSpaces(gap / 2, output);
      }

      output.print(element);

      if (alignment == GridElementAlignment.CENTER) {
        GridUtil.outputSpaces(gap - gap / 2, output);
      }

      if (alignment == GridElementAlignment.LEFT) {
        GridUtil.outputSpaces(gap, output);
      }

      if (column != elements.length - 1) {
        GridUtil.outputSpaces(GridFormat.COLUMN_SEPARATION, output);
      }
    }
    output.println();
  }

  /**
   * 列番号を表示する。
   * 
   * @param columnOffset 列のオフセット
   * @param printColumnSize 出力する列の数
   * @param printingColumnWidthes 出力する列の幅
   * @param output 出力先のプリントライター
   */
  private static void printColumnNumber(final int columnOffset, final int printColumnSize, final int[] printingColumnWidthes, final PrintWriter output) {
    GridUtil.outputSpaces(GridFormat.LEFT_MARGIN, output);

    for (int column = 0; column < printColumnSize; column++) {
      final int printingWidth = printingColumnWidthes[column];

      if (column != 0) {
        GridUtil.outputSpaces(GridFormat.COLUMN_SEPARATION, output);
      }

      final int formatLength = 3;
      final String columnNumber = String.format("(%" + formatLength + "d)", Integer.valueOf(columnOffset + column + 1)); //$NON-NLS-1$ //$NON-NLS-2$
      final int columnNumberWidth = columnNumber.length();
      
      final int leftMarginWidth = (printingWidth - columnNumberWidth + 1) / 2;
      final int rightMerginWidth = printingWidth - columnNumberWidth - leftMarginWidth;
      
      output.print(" "); //$NON-NLS-1$
      GridUtil.outputSpaces(leftMarginWidth, output);
      output.print(columnNumber);
      GridUtil.outputSpaces(rightMerginWidth, output);

      if (column != printColumnSize - 1) {
        output.print(" "); //$NON-NLS-1$
      }
    }
    output.println();
  }

  /**
   * 全ての成分のメジアンを返します。
   * 
   * @param matrix 対象となる行列
   * @return 全ての成分のメジアン
   */
  public static double median(final double[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final int size = rowSize * columnSize;

    if (size == 0) {
      throw new IllegalArgumentException(Messages.getString("DoubleMatrixUtil.13")); //$NON-NLS-1$
    }

    final double[][] rowVector = reshape(matrix, 1, size);
    final double[][] median = medianRowWise(rowVector);
    return median[0][0];
  }

  /**
   * 列毎のメジアンを成分とする行ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 中間値(メジアン)
   */
  public static double[][] medianColumnWise(final double[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final IndexedDoubleElements sortedElements = sortColumnWise(matrix);
    final double[][] sortedMatrix = sortedElements.getElements();

    if (rowSize % 2 != 0) {
      return getSubMatrix(sortedMatrix, rowSize / 2, rowSize / 2, 0, columnSize - 1);
    }

    final double[][] x1 = getSubMatrix(sortedMatrix, rowSize / 2 - 1, rowSize / 2 - 1, 0, columnSize - 1);
    final double[][] x2 = getSubMatrix(sortedMatrix, rowSize / 2, rowSize / 2, 0, columnSize - 1);
    return divide(add(x1, x2), 2);
  }

  /**
   * 行毎のメジアンを成分とする列ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 中間値(メジアン)
   */
  public static double[][] medianRowWise(final double[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    
    final IndexedDoubleElements sortedElements = sortRowWise(matrix);
    double[][] sortedMatrix = sortedElements.getElements();

    if (columnSize % 2 != 0) {
      return getSubMatrix(sortedMatrix, 0, rowSize - 1, columnSize / 2, columnSize / 2);
    }

    final double[][] x1 = getSubMatrix(sortedMatrix, 0, rowSize - 1, columnSize / 2 - 1, columnSize / 2 - 1);
    final double[][] x2 = getSubMatrix(sortedMatrix, 0, rowSize - 1, columnSize / 2, columnSize / 2);
    return divide(add(x1, x2), 2);
  }

  /**
   * 共分散行列を生成します。
   * 
   * @param a1 データ列1 (行ベクトル又は列ベクトル)
   * @param a2 データ列2 (行ベクトル又は列ベクトル)
   * @return 共分散 (covariance)
   */
  public static double[][] covariance(final double[][] a1, final double[][] a2) {
    final int rowSize1 = a1.length;
    final int columnSize1 = rowSize1 == 0 ? 0 : a1[0].length;
    final int rowSize2 = a2.length;
    final int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (columnSize1 != columnSize2 && rowSize1 != 0 && rowSize2 != 0) {
      throw new MatrixSizeException(MatrixSizeException.INCONSISTENT_COLUMN_NUMBER);
    }

    if (rowSize1 != 1 && columnSize1 != 1) {
      throw new IllegalArgumentException(Messages.getString("DoubleMatrixUtil.41")); //$NON-NLS-1$
    }

    final double[][] xy = appendRight(makeColumnVector(a1), makeColumnVector(a2));

    final int size = xy.length;
    final double[][] xyZeroMean = subtract(xy, multiply(createOnes(size, 1), meanColumnWise(xy)));

    return divide(multiply(transpose(xyZeroMean), xyZeroMean), size - 1);
  }

  /**
   * 分散を返します。
   * 
   * @param matrix データ列 (行ベクトル又は列ベクトル)
   * @return 分散
   */
  public static double variance(final double[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize != 1 && columnSize != 1) {
      throw new IllegalArgumentException(Messages.getString("DoubleMatrixUtil.42")); //$NON-NLS-1$
    }

    final double[][] x = makeColumnVector(matrix);

    final int size = x.length;
    final double[][] xZeroMean = subtract(x, multiply(createOnes(size, 1), meanColumnWise(x)));

    return divide(multiply(transpose(xZeroMean), xZeroMean), size - 1)[0][0];
  }

  /**
   * 各列ベクトルを縦に結合し、長い列ベクトルを生成します。
   * 
   * @param matrix 対象となる行列
   * @return 列ベクトル
   */
  public static double[][] makeColumnVector(final double[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 1 || columnSize == 1) {
      return reshape(matrix, rowSize * columnSize, 1);
    }
    return reshape(transpose(matrix), rowSize * columnSize, 1);
  }

}