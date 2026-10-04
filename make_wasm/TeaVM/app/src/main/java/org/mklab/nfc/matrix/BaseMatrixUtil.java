/**
 * $Id: BaseMatrixUtil.java,v 1.18 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Masanobu Koga. All rights reserved.
 */
package org.mklab.nfc.matrix;

import org.mklab.nfc.scalar.Scalar;


/**
 * {@link BaseMatrix}のユーティリティクラスです。
 * 
 * @author koga
 * @version $Revision: 1.18 $
 */
public final class BaseMatrixUtil {

  /**
   * 新しく生成された<code>BaseMatrixUtil</code>オブジェクトを初期化します。
   */
  private BaseMatrixUtil() {
    // nothing to do
  }

  /**
   * 2個の配列の成分が全て等しか判定します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param a1 第一行列
   * @param a2 第二行列
   * @param tolerance 許容誤差
   * @return 配列の成分が等しければtrue、そうでなければfalseを返します。
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> boolean equals(final S[][] a1, final S[][] a2, final double tolerance) {
    int rowSize1 = a1.length;
    int columnSize1 = rowSize1 == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (rowSize1 != rowSize2 || columnSize1 != columnSize2) {
      return false;
    }

    for (int i = 0; i < rowSize1; i++) {
      S[] a1i = a1[i];
      S[] a2i = a2[i];
      for (int j = 0; j < columnSize1; j++) {
        //        if (a1i[j] == null && a2i[j] == null) {
        //          continue;
        //        }
        if (!a1i[j].equals(a2i[j], tolerance)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * 各成分と<code>scalar</code>を<code>operator</code>で指定された演算子で比較し, 計算結果を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param scalar 比較対象
   * @return 計算結果を成分とする行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> boolean[][] compareElementWise(final S[][] matrix, final String operator, final double scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].compare(operator, scalar);
      }
    }

    return ans;
  }

  /**
   * 各成分と<code>sclar</code>を<code>operator</code>で指定された演算子で比較し, 計算結果を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param scalar 比較対象
   * @return 計算結果を成分とする行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> boolean[][] compareElementWise(final S[][] matrix, final String operator, final int scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].compare(operator, scalar);
      }
    }

    return ans;
  }

  /**
   * 各成分と<code>scalar</code>を<code>operator</code>で指定された演算子で比較し, 計算結果を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param scalar 比較対象
   * @return 計算結果を成分とする行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> boolean[][] compareElementWise(final S[][] matrix, final String operator, final S scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].compare(operator, scalar);
      }
    }

    return ans;
  }

  /**
   * 2個の行列を成分毎にoperatorで指定された演算子で比較し, 計算結果を成分とするbooleanの行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param a1 第一行列
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param a2 第二行列
   * @return 計算結果を成分とするbooleanの行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> boolean[][] compareElementWise(final S[][] a1, final String operator, final double[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      boolean[] ansi = ans[i];
      S[] a1i = a1[i];
      double[] a2i = a2[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a1i[j].compare(operator, a2i[j]);
      }
    }
    return ans;
  }

  /**
   * 2個の行列を成分毎にoperatorで指定された演算子で比較し, 計算結果を成分とするbooleanの行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param a1 第一行列
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param a2 第二行列
   * @return 計算結果を成分とするbooleanの行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> boolean[][] compareElementWise(final S[][] a1, final String operator, final int[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      boolean[] ansi = ans[i];
      S[] a1i = a1[i];
      int[] a2i = a2[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a1i[j].compare(operator, a2i[j]);
      }
    }
    return ans;
  }

  /**
   * 行列<code>source</code>の各成分を行列<code>destination</code>の各成分にコピーします。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param source コピー元行列
   * @param destination コピー先行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> void copy(final double[][] source, final S[][] destination) {
    int rowSizeFrom = source.length;
    int columnSizeFrom = rowSizeFrom == 0 ? 0 : source[0].length;
    int rowSizeTo = destination.length;
    int columnSizeTo = rowSizeTo == 0 ? 0 : destination[0].length;

    if (rowSizeTo != rowSizeFrom || columnSizeTo != columnSizeFrom) {
      throw new MatrixSizeException(Messages.getString("BaseMatrixUtil.0")); //$NON-NLS-1$
    }

    for (int i = 0; i < rowSizeFrom; i++) {
      S[] toi = destination[i];
      double[] fromi = source[i];
      for (int j = 0; j < columnSizeFrom; j++) {
        toi[j] = toi[j].create(fromi[j]);
      }
    }
  }

  /**
   * 行列<code>source</code>の各成分を行列<code>destination</code>の各成分にコピーします。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param source コピー元行列
   * @param destination コピー先行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> void copy(final int[][] source, final S[][] destination) {
    int rowSizeFrom = source.length;
    int columnSizeFrom = rowSizeFrom == 0 ? 0 : source[0].length;
    int rowSizeTo = destination.length;
    int columnSizeTo = rowSizeTo == 0 ? 0 : destination[0].length;

    if (rowSizeTo != rowSizeFrom || columnSizeTo != columnSizeFrom) {
      throw new MatrixSizeException(Messages.getString("BaseMatrixUtil.1")); //$NON-NLS-1$
    }

    for (int i = 0; i < rowSizeFrom; i++) {
      S[] toi = destination[i];
      int[] fromi = source[i];
      for (int j = 0; j < columnSizeFrom; j++) {
        toi[j] = toi[j].create(fromi[j]);
      }
    }
  }

  /**
   * 行列の和を求めます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param a1 加えられる行列
   * @param a2 加える行列
   * @return 行列の和
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] add(final S[][] a1, final S[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, a1);
    }

    S ans00 = a1[0][0].add(a2[0][0]);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] a1i = a1[i];
      S[] a2i = a2[i];
      S[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a1i[j].add(a2i[j]);
      }
    }
    return ans;
  }

  /**
   * 行列の差を求めます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param a1 引かれる行列
   * @param a2 引く行列
   * @return 行列の差
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] subtract(final S[][] a1, final S[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, a1);
    }

    S ans00 = a1[0][0].subtract(a2[0][0]);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] a1i = a1[i];
      S[] a2i = a2[i];
      S[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a1i[j].subtract(a2i[j]);
      }
    }
    return ans;
  }

  /**
   * 2個の行列の積を求めます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param a1 掛けられる行列
   * @param a2 掛ける行列
   * @return 行列の積
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] multiply(final S[][] a1, final S[][] a2) {
    int rowSize1 = a1.length;
    int columnSize1 = (rowSize1 == 0 || a1[0] == null) ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = (rowSize2 == 0 || a2[0] == null) ? 0 : a2[0].length;

    if (columnSize1 != rowSize2) {
      throw new MatrixSizeException(Messages.getString("BaseMatrixUtil.2")); //$NON-NLS-1$
    }

    if (rowSize1 == 0 || columnSize2 == 0) {
      return GridUtil.<S> createArray(rowSize1, columnSize2, a1);
    }

    S ans00 = a1[0][0].multiply(a2[0][0]);
    S[][] ans = ans00.createArray(rowSize1, columnSize2);

    for (int i = 0; i < rowSize1; i++) {
      S[] a1i = a1[i];
      S[] ansi = ans[i];
      for (int j = 0; j < columnSize2; j++) {
        S d = a1i[0].multiply(a2[0][j]);
        for (int k = 1; k < columnSize1; k++) {
          d = d.add(a1i[k].multiply(a2[k][j]));
        }
        ansi[j] = d;
      }
    }
    return ans;
  }

  /**
   * 共役行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 元の行列
   * @return 共役行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] conjugate(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0].conjugate();
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].conjugate();
      }
    }
    return ans;
  }

  /**
   * 成分の符号を反転した行列を生成します。
   * 
   * @param <S> 成分の型 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 成分の符号を反転した行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] unaryMinus(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0].unaryMinus();
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] matrixi = matrix[i];
      S[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].unaryMinus();
      }
    }
    return ans;
  }

  /**
   * 単位行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 単位行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] createUnit(final S[][] matrix, final int rowSize, final int columnSize) {
    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S value = matrix[0][0];
    return createUnit(value, rowSize, columnSize);
  }

  /**
   * 単位行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param value 対象となる値
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 単位行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] createUnit(final S value, final int rowSize, final int columnSize) {
    S[][] ans = value.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      final S[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        if (i == j) {
          ansi[j] = value.createUnit();
        } else {
          ansi[j] = value.createZero();
        }
      }
    }

    return ans;
  }

  /**
   * 全ての成分が１である行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 全ての成分が１である行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] createOnes(final S[][] matrix, final int rowSize, final int columnSize) {
    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S value = matrix[0][0];
    return createOnes(value, rowSize, columnSize);
  }

  /**
   * 全ての成分が１である行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param value 対象となる値
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 全ての成分が１である行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] createOnes(final S value, final int rowSize, final int columnSize) {
    S[][] ans = value.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      final S[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = value.createUnit();
      }
    }

    return ans;
  }

  /**
   * 共役複素転置行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 元の行列
   * @return 共役複素転置行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] conjugateTranspose(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(columnSize, rowSize, matrix);
    }

    S ans00 = matrix[0][0].conjugate();
    S[][] ans = ans00.createArray(columnSize, rowSize);

    for (int i = 0; i < rowSize; i++) {
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ans[j][i] = matrixi[j].conjugate();
      }
    }
    return ans;
  }

  /**
   * 行列に整数を掛けます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param scalar 乗じる整数
   * @return 整数を掛けた結果
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] multiply(final S[][] matrix, final int scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0].multiply(scalar);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].multiply(scalar);
      }
    }
    return ans;
  }

  /**
   * 行列に実数を掛けます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param scalar 乗じる実数
   * @return 実数を掛けた結果
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] multiply(final S[][] matrix, final double scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0].multiply(scalar);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].multiply(scalar);
      }
    }
    return ans;
  }

  /**
   * 実行列とスカラーの積を求めます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 実行列
   * @param scalar スカラー
   * @return 実行列とスカラーの積
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] multiply(final double[][] matrix, final S scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return scalar.createArray(rowSize, columnSize);
    }

    S ans00 = scalar.multiply(matrix[0][0]);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = scalar.multiply(matrixi[j]);
      }
    }
    return ans;
  }

  /**
   * 成分毎に乗算を行います。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param a1 掛けられる行列
   * @param a2 掛ける行列
   * @return 掛けた結果の行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] multiplyElementWise(final S[][] a1, final S[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, a1);
    }

    S ans00 = a1[0][0].multiply(a2[0][0]);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] a1i = a1[i];
      S[] a2i = a2[i];
      S[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a1i[j].multiply(a2i[j]);
      }
    }
    return ans;
  }

  /**
   * 成分毎の割り算の結果を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param a1 割られる行列
   * @param a2 割る行列
   * @return 割り算の結果
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] divideElementWise(final S[][] a1, final S[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, a1);
    }

    S ans00 = a1[0][0].divide(a2[0][0]);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] a1i = a1[i];
      S[] a2i = a2[i];
      S[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a1i[j].divide(a2i[j]);
      }
    }
    return ans;
  }

  /**
   * 成分毎の割り算(左が分母、右が分子)の結果を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param a1 割る行列
   * @param a2 割られる行列
   * @return 割り算の結果
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] leftDivideElementWise(final S[][] a1, final S[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, a1);
    }

    S ans00 = a1[0][0].leftDivide(a2[0][0]);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] a1i = a1[i];
      S[] a2i = a2[i];
      S[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a1i[j].leftDivide(a2i[j]);
      }
    }
    return ans;
  }

  /**
   * 絶対値が小さい成分を0に丸めます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param tolerance 許容誤差
   * @return 丸められた結果
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] roundToZeroElementWise(final S[][] matrix, final double tolerance) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0].roundToZero(tolerance);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] matrixi = matrix[i];
      S[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].roundToZero(tolerance);
      }
    }
    return ans;
  }

  /**
   * 零行列か判定します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 調べる行列
   * @param tolerance 許容誤差
   * @return 零行列ならばtrue、そうでなければfalse
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> boolean isZero(final S[][] matrix, final double tolerance) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    for (int i = 0; i < rowSize; i++) {
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        if (!matrixi[j].isZero(tolerance)) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * 単位行列であるか判定します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 調べる行列
   * @param tolerance 許容誤差
   * @return 単位行列ならばtrue、そうでなければfalse
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> boolean isUnit(final S[][] matrix, final double tolerance) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize != columnSize) {
      return false;
    }

    if (rowSize == 0 && columnSize == 0) {
      return false;
    }

    for (int i = 0; i < rowSize; i++) {
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        if (i == j) {
          if (!matrixi[j].isUnit(tolerance)) {
            return false;
          }
        } else {
          if (!matrixi[j].isZero(tolerance)) {
            return false;
          }
        }
      }
    }

    return true;
  }

  /**
   * 各成分の非数性の真偽を成分にもつ行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 非数性の真偽行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> boolean[][] isNanElementWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      S[] matrixi = matrix[i];
      boolean[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].isNaN();
      }
    }
    return ans;
  }

  /**
   * 各成分の有限性の真偽を成分にもつ行列を返します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 有限性の真偽行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> boolean[][] isFiniteElementWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      boolean[] ansi = ans[i];
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].isFinite();
      }
    }
    return ans;
  }

  /**
   * 各成分の無限性の真偽を成分にもつ行列を返します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 無限性の真偽行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> boolean[][] isInfiniteElementWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      boolean[] ansi = ans[i];
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].isInfinite();
      }
    }
    return ans;
  }

  /**
   * 行列の全ての成分にスカラーを加えた行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param scalar 加える値
   * @return 生成された行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] addElementWise(final S[][] matrix, final S scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0].add(scalar);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] matrixi = matrix[i];
      S[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].add(scalar);
      }
    }
    return ans;
  }

  /**
   * 行列の全ての成分に実数を加えた行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param scalar 加える実数
   * @return 生成された行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] addElementWise(final S[][] matrix, final double scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0].add(scalar);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].add(scalar);
      }
    }
    return ans;
  }

  /**
   * 行列の全ての成分にスカラーを引いた行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param scalar 引く値
   * @return 生成された行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] subtractElementWise(final S[][] matrix, final S scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0].subtract(scalar);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].subtract(scalar);
      }
    }
    return ans;
  }

  /**
   * 行列の全ての成分に実数を引いた行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param scalar 引く実数
   * @return 生成された行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] subtractElementWise(final S[][] matrix, final double scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0].subtract(scalar);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].subtract(scalar);
      }
    }
    return ans;
  }

  /**
   * 行列の全ての成分の逆数を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 元の行列
   * @return 成分の逆数を成分とする行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] inverseElementWise(final S[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    final S ans00 = matrix[0][0].inverse();
    final S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      final S[] ansi = ans[i];
      final S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].inverse();
      }
    }
    return ans;
  }

  /**
   * 行列にスカラーを掛けます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param scalar 乗じるスカラー
   * @return スカラーを掛けた結果
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] multiply(final S[][] matrix, final S scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0].multiply(scalar);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].multiply(scalar);
      }
    }
    return ans;
  }

  /**
   * 全ての成分をスカラーで割る。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param scalar 割るスカラー
   * @return 計算結果
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] divide(final S[][] matrix, final S scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0].divide(scalar);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].divide(scalar);
      }
    }
    return ans;
  }

  /**
   * 行列を整数で割る。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param scalar 割る整数
   * @return 整数で割った結果
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] divide(final S[][] matrix, final int scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0].divide(scalar);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].divide(scalar);
      }
    }
    return ans;
  }

  /**
   * 行列を実数で割る。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param scalar 割る実数
   * @return 実数で割った結果
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] divide(final S[][] matrix, final double scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0].divide(scalar);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].divide(scalar);
      }
    }
    return ans;
  }

  /**
   * 行列の全ての成分毎に累乗を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 累乗の対象となる値を成分とする行列
   * @param scalar 累乗の指数
   * @return 生成された行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] powerElementWise(final S[][] matrix, final int scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0].power(scalar);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].power(scalar);
      }
    }
    return ans;
  }

  /**
   * 行列の全ての成分毎に累乗を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param a1 累乗の対象となる値を成分とする行列
   * @param a2 累乗の指数を成分とする行列
   * @return 生成された行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] powerElementWise(final S[][] a1, final int[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, a1);
    }

    S ans00 = a1[0][0].power(a2[0][0]);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      S[] matrixi = a1[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = matrixi[j].power(a2[i][j]);
      }
    }
    return ans;
  }

  /**
   * スカラーの累乗を行列の成分毎に計算し、計算結果を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param scalar 累乗されるスカラー
   * @param matrix 累乗の指数を成分とする行列
   * @return 生成された行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] powerElementWise(final S scalar, final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return scalar.createArray(rowSize, columnSize);
    }

    S ans00 = scalar.power(matrix[0][0]);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      int[] mmi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = scalar.power(mmi[j]);
      }
    }
    return ans;
  }

  /**
   * 行列式を返します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 行列式
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S determinant(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    S[][] aa = matrix;

    if (rowSize == 1) {
      S det = (aa[0][0].clone());
      return det;
    }

    if (rowSize == 2) {
      S[] aa0 = aa[0];
      S[] aa1 = aa[1];

      S det = aa0[0].multiply(aa1[1]).subtract(aa0[1].multiply(aa1[0]));
      return det;
    }

    if (rowSize == 3) {
      S[] aa0 = aa[0];
      S[] aa1 = aa[1];
      S[] aa2 = aa[2];

      S tmpp1 = aa0[0].multiply(aa1[1]);
      S det = tmpp1.multiply(aa2[2]); /* a(1,1)*a(2,2)*a(3,3) */

      tmpp1 = aa0[1].multiply(aa1[2]);
      S tmpp2 = tmpp1.multiply(aa2[0]); /* a(1,2)*a(2,3)*a(3,1) */
      det = det.add(tmpp2);

      tmpp1 = aa0[2].multiply(aa1[0]);
      tmpp2 = tmpp1.multiply(aa2[1]); /* a(1,3)*a(2,1)*a(3,2) */
      det = det.add(tmpp2);

      tmpp1 = aa0[2].multiply(aa1[1]);
      tmpp2 = tmpp1.multiply(aa2[0]); /* a(1,3)*a(2,2)*a(3,1) */
      det = det.subtract(tmpp2);

      tmpp1 = aa0[1].multiply(aa1[0]);
      tmpp2 = tmpp1.multiply(aa2[2]); /* a(1,2)*a(2,1)*a(3,3) */
      det = det.subtract(tmpp2);

      tmpp1 = aa0[0].multiply(aa1[2]);
      tmpp2 = tmpp1.multiply(aa2[1]); /* a(1,1)*a(2,3)*a(3,2) */
      det = det.subtract(tmpp2);
      return det;
    }

    S det = aa[0][0].createZero();
    /* Discard the first column */
    S[][] m1 = GridUtil.getSubMatrix(matrix, 0, rowSize - 1, 1, columnSize - 1);

    int m1r = m1.length;
    int m1c = m1r == 0 ? 0 : m1[0].length;

    for (int i = 1; i <= m1r; i++) {
      S[][] m2;
      if (i == 1) {
        m2 = GridUtil.getSubMatrix(m1, 1, m1r - 1, 0, m1c - 1);
      } else if (i == m1r) {
        m2 = GridUtil.getSubMatrix(m1, 0, m1r - 2, 0, m1c - 1);
      } else {
        S[][] m3 = GridUtil.getSubMatrix(m1, 0, i - 2, 0, m1c - 1);
        S[][] m4 = GridUtil.getSubMatrix(m1, i, m1r - 1, 0, m1c - 1);
        m2 = GridUtil.<S> appendDown(m3, m4);
      }

      S subDet = aa[i - 1][0].multiply(determinant(m2));

      if (i % 2 != 0) {
        det = det.add(subDet);
      } else {
        det = det.subtract(subDet);
      }
    }
    return det;
  }

  /**
   * 行列の全ての和を返します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 行列の全ての成分も和
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S sum(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    S ans = matrix[0][0].createZero();

    for (int i = 0; i < rowSize; i++) {
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ans = ans.add(matrixi[j]);
      }
    }
    return ans;
  }

  /**
   * 全ての成分の平均値を返します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 全ての成分の平均値
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S mean(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    return sum(matrix).divide(rowSize * columnSize);
  }

  /**
   * 列毎に全ての成分の和を計算し、計算結果を成分とする行ベクトルを生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする横ベクトル
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] sumColumnWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(Math.min(rowSize, 1), columnSize, matrix);
    }

    S ans00 = matrix[0][0];
    S[][] ans = ans00.createArray(1, columnSize);

    for (int i = 0; i < columnSize; i++) {
      S sum = matrix[0][0].createZero();
      for (int j = 0; j < rowSize; j++) {
        sum = sum.add(matrix[j][i]);
      }
      ans[0][i] = sum;
    }
    return ans;
  }

  /**
   * 行毎に全ての成分の和を計算し、計算結果を成分とする列ベクトルを生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする縦ベクトル
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] sumRowWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, Math.min(columnSize, 1), matrix);
    }

    S ans00 = matrix[0][0];
    S[][] ans = ans00.createArray(rowSize, 1);

    for (int i = 0; i < rowSize; i++) {
      S sum = matrix[0][0].createZero();
      for (int j = 0; j < columnSize; j++) {
        sum = sum.add(matrix[i][j]);
      }
      ans[i][0] = sum;
    }
    return ans;
  }

  /**
   * 全ての成分の累積和を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] cumulativeSum(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0];
    S[][] ans = ans00.createArray(rowSize, columnSize);

    S sum = matrix[0][0].createZero();
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        sum = sum.add(matrix[i][j]);
        ans[i][j] = sum;
      }
    }
    return ans;
  }

  /**
   * 行毎に累積和を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] cumulativeSumRowWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0];
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S sum = matrix[0][0].createZero();
      for (int j = 0; j < columnSize; j++) {
        sum = sum.add(matrix[i][j]);
        ans[i][j] = sum;
      }
    }
    return ans;
  }

  /**
   * 列毎に累積和を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] cumulativeSumColumnWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0];
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < columnSize; i++) {
      S sum = matrix[0][0].createZero();
      for (int j = 0; j < rowSize; j++) {
        sum = sum.add(matrix[j][i]);
        ans[j][i] = sum;
      }
    }
    return ans;
  }

  /**
   * 全ての成分の累積積を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] cumulativeProduct(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0];
    S[][] ans = ans00.createArray(rowSize, columnSize);

    S prod = matrix[0][0].createUnit();
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        prod = prod.multiply(matrix[i][j]);
        ans[i][j] = prod;
      }
    }
    return ans;
  }

  /**
   * 行毎に累積積を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] cumulativeProductRowWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0];
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S prod = matrix[0][0].createUnit();
      for (int j = 0; j < columnSize; j++) {
        prod = prod.multiply(matrix[i][j]);
        ans[i][j] = prod;
      }
    }
    return ans;
  }

  /**
   * 列毎に累積積を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] cumulativeProductColumnWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0];
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < columnSize; i++) {
      S prod = matrix[0][0].createUnit();
      for (int j = 0; j < rowSize; j++) {
        prod = prod.multiply(matrix[j][i]);
        ans[j][i] = prod;
      }
    }
    return ans;
  }

  /**
   * 行毎に全ての成分の平均値を計算し、計算結果を成分とする列ベクトルを生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする縦ベクトル
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] meanRowWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    return divide(sumRowWise(matrix), columnSize);
  }

  /**
   * 列毎に全ての成分の平均値を計算し、計算結果を成分とする行ベクトルを生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする横ベクトル
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] meanColumnWise(final S[][] matrix) {
    int rowSize = matrix.length;
    return divide(sumColumnWise(matrix), rowSize);
  }

  /**
   * 行列の全ての成分の積を返します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 行列の全ての成分の積
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S product(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    S ans = matrix[0][0].createUnit();

    for (int i = 0; i < rowSize; i++) {
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ans = ans.multiply(matrixi[j]);
      }
    }
    return ans;
  }

  /**
   * 行毎に全ての成分の積を計算し、計算結果を成分とする列ベクトルを生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする列ベクトル
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] productRowWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, Math.min(columnSize, 1), matrix);
    }

    S ans00 = matrix[0][0];
    S[][] ans = ans00.createArray(rowSize, 1);

    for (int i = 0; i < rowSize; i++) {
      S pro = matrix[0][0].createUnit();
      for (int j = 0; j < columnSize; j++) {
        pro = pro.multiply(matrix[i][j]);
      }
      ans[i][0] = pro;
    }
    return ans;
  }

  /**
   * 列毎に全ての成分の積を計算し、計算結果を成分とする行ベクトルを生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行ベクトル
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] productColumnWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(Math.min(rowSize, 1), columnSize, matrix);
    }

    S ans00 = matrix[0][0];
    S[][] ans = ans00.createArray(1, columnSize);

    for (int i = 0; i < columnSize; i++) {
      S pro = matrix[0][0].createUnit();
      for (int j = 0; j < rowSize; j++) {
        pro = pro.multiply(matrix[j][i]);
      }
      ans[0][i] = pro;
    }
    return ans;
  }

  /**
   * 全対角成分の和(トレース)を返します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 対角成分の合計(トレース)
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S trace(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int size = Math.min(rowSize, columnSize);

    S ans = matrix[0][0].createZero();
    for (int i = 0; i < size; i++) {
      ans = ans.add(matrix[i][i]);
    }
    return ans;
  }

  /**
   * 共分散行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param a1 データ列1 (行ベクトル又は列ベクトル)
   * @param a2 データ列2 (行ベクトル又は列ベクトル)
   * @return 共分散 (Covariance)
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] covariance(final S[][] a1, final S[][] a2) {
    int rowSize1 = a1.length;
    int columnSize1 = rowSize1 == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (columnSize1 != columnSize2 && rowSize1 != 0 && rowSize2 != 0) {
      throw new MatrixSizeException(MatrixSizeException.INCONSISTENT_COLUMN_NUMBER);
    }

    if (rowSize1 != 1 && columnSize1 != 1) {
      throw new MatrixSizeException(Messages.getString("BaseMatrixUtil.3")); //$NON-NLS-1$
    }

    S[][] xy = GridUtil.<S> appendRight(GridUtil.makeColumnVector(a1), GridUtil.makeColumnVector(a2));

    int size = xy.length;
    S ans00 = a1[0][0].multiply(a2[0][0]);
    S[][] one = ans00.createArray(size, 1);
    for (int i = 0; i < size; i++) {
      one[i][0] = a1[0][0].createUnit();
    }

    S[][] xyZeroMean = subtract(xy, BaseMatrixUtil.<S, M> multiply(one, meanColumnWise(xy)));

    return divide(BaseMatrixUtil.<S, M> multiply(BaseMatrixUtil.conjugateTranspose(xyZeroMean), xyZeroMean), size - 1);
  }

  /**
   * 分散を返します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrix データ列 (行ベクトル又は列ベクトル)
   * @return 分散
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S variance(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize != 1 && columnSize != 1) {
      throw new MatrixSizeException(Messages.getString("BaseMatrixUtil.4")); //$NON-NLS-1$
    }

    S[][] x = GridUtil.makeColumnVector(matrix);

    int size = x.length;
    S ans00 = matrix[0][0];
    S[][] one = ans00.createArray(size, 1);
    for (int i = 0; i < size; i++) {
      one[i][0] = matrix[0][0].createUnit();
    }
    S[][] xZeroMean = subtract(x, BaseMatrixUtil.<S, M> multiply(one, BaseMatrixUtil.<S, M> meanColumnWise(x)));

    return divide(BaseMatrixUtil.<S, M> multiply(GridUtil.transpose(xZeroMean), xZeroMean), size - 1)[0][0];
  }

  /**
   * 成分毎に関数の計算をし、計算結果を成分とする行列を生成します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param matrix 対象となる行列
   * @param function 行列成分数関数(引数１個)
   * @return 計算結果を成分とする行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> S[][] elementWiseFunction(final S[][] matrix, final ScalarFunction<S, M> function) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(Math.min(rowSize, 1), columnSize, matrix);
    }

    S ans00 = function.evaluate(matrix[0][0]);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = function.evaluate(matrix[i][j]);
      }
    }
    return ans;
  }

  /**
   * 与えられた位置に行列を代入します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param destination 値を設定する行列
   * @param rowMin 行の始まり
   * @param rowMax 行の終わり
   * @param columnMin 列の始まり
   * @param columnMax 列の終わり
   * @param source 設定する行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> void setSubMatrix(final S[][] destination, final int rowMin, final int rowMax, final int columnMin, final int columnMax,
      final S[][] source) {
    int rowSize = source.length;
    int columnSize = rowSize == 0 ? 0 : source[0].length;

    if (rowSize != rowMax - rowMin + 1 || columnSize != columnMax - columnMin + 1) {
      throw new MatrixSizeException(MatrixSizeException.INCONSISTENT_SIZE);
    }

    for (int i = 0; i < rowSize; i++) {
      S[] toi = destination[i + rowMin];
      S[] fromi = source[i];
      for (int j = 0; j < columnSize; j++) {
        toi[j + columnMin] = fromi[j].clone();
        //toi[j + columnMin] = destination[0][0].transformFrom(fromi[j]);
      }
    }
  }

  /**
   * 与えられた位置に行列を代入します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param destination 値を設定する行列
   * @param rowMin 行の始まり
   * @param rowMax 行の終わり
   * @param columnMin 列の始まり
   * @param columnMax 列の終わり
   * @param source 設定する行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> void setSubMatrix(final S[][] destination, final int rowMin, final int rowMax, final int columnMin, final int columnMax,
      final int[][] source) {
    int rowSize = source.length;
    int columnSize = rowSize == 0 ? 0 : source[0].length;

    if (rowSize != rowMax - rowMin + 1 || columnSize != columnMax - columnMin + 1) {
      throw new MatrixSizeException(MatrixSizeException.INCONSISTENT_SIZE);
    }

    for (int i = 0; i < rowSize; i++) {
      S[] toi = destination[i + rowMin];
      int[] fromi = source[i];
      for (int j = 0; j < columnSize; j++) {
        toi[j + columnMin] = destination[0][0].create(fromi[j]);
      }
    }
  }

  /**
   * 与えられた位置に行列を代入します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param destination 値を設定する行列
   * @param rowMin 行の始まり
   * @param rowMax 行の終わり
   * @param columnMin 列の始まり
   * @param columnMax 列の終わり
   * @param source 設定する行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> void setSubMatrix(final S[][] destination, final int rowMin, final int rowMax, final int columnMin, final int columnMax,
      final double[][] source) {
    int rowSize = source.length;
    int columnSize = rowSize == 0 ? 0 : source[0].length;

    if (rowSize != rowMax - rowMin + 1 || columnSize != columnMax - columnMin + 1) {
      throw new MatrixSizeException(MatrixSizeException.INCONSISTENT_SIZE);
    }

    for (int i = 0; i < rowSize; i++) {
      S[] toi = destination[i + rowMin];
      double[] fromi = source[i];
      for (int j = 0; j < columnSize; j++) {
        toi[j + columnMin] = destination[0][0].create(fromi[j]);
      }
    }
  }

  /**
   * 指定された場所に値を代入します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param destination 値を設定するグリッド
   * @param rowIndex 指定する行を含む指数
   * @param columnMin 列の始まり
   * @param columnMax 列の終り
   * @param source 代入するグリッド
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> void setSubMatrix(final S[][] destination, final int[] rowIndex, final int columnMin, final int columnMax, final int[][] source) {
    int idxcol = rowIndex.length;
    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;
    int columnSize = columnMax - columnMin + 1;

    if (mrow != idxcol || mcol != columnSize) {
      throw new MatrixSizeException(Messages.getString("BaseMatrixUtil.5")); //$NON-NLS-1$
    }

    for (int i = 0; i < idxcol; i++) {
      for (int j = 0; j < columnSize; j++) {
        destination[rowIndex[i]][j + columnMin] = destination[0][0].create(source[i][j]);
      }
    }
  }

  /**
   * 指定された場所に値を代入します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param destination 値を設定するグリッド
   * @param rowIndex 指定する行を含む指数
   * @param columnMin 列の始まり
   * @param columnMax 列の終り
   * @param soruce 代入するグリッド
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> void setSubMatrix(final S[][] destination, final int[] rowIndex, final int columnMin, final int columnMax, final double[][] soruce) {
    int idxcol = rowIndex.length;
    int mrow = soruce.length;
    int mcol = mrow == 0 ? 0 : soruce[0].length;
    int columnSize = columnMax - columnMin + 1;

    if (mrow != idxcol || mcol != columnSize) {
      throw new MatrixSizeException(Messages.getString("BaseMatrixUtil.6")); //$NON-NLS-1$
    }

    for (int i = 0; i < idxcol; i++) {
      for (int j = 0; j < columnSize; j++) {
        destination[rowIndex[i]][j + columnMin] = destination[0][0].create(soruce[i][j]);
      }
    }
  }

  /**
   * 指定された場所に値を代入します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param destination 値を代入するグリッド
   * @param rowMin 行の始まり
   * @param rowMax 行の終り
   * @param columnIndex 指定する列を含む指数
   * @param source 代入するグリッド
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> void setSubMatrix(final S[][] destination, final int rowMin, final int rowMax, final int[] columnIndex, final int[][] source) {
    int idxcol = columnIndex.length;
    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;
    int rowSize = rowMax - rowMin + 1;

    if (mrow != rowSize || mcol != idxcol) {
      throw new MatrixSizeException(Messages.getString("BaseMatrixUtil.7")); //$NON-NLS-1$
    }

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < idxcol; j++) {
        destination[i + rowMin][columnIndex[j]] = destination[0][0].create(source[i][j]);
      }
    }
  }

  /**
   * 指定された場所に値を代入します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param destination 値を代入するグリッド
   * @param rowMin 行の始まり
   * @param rowMax 行の終り
   * @param columnIndex 指定する列を含む指数
   * @param source 代入するグリッド
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> void setSubMatrix(final S[][] destination, final int rowMin, final int rowMax, final int[] columnIndex, final double[][] source) {
    int idxcol = columnIndex.length;
    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;
    int rowSize = rowMax - rowMin + 1;

    if (mrow != rowSize || mcol != idxcol) {
      throw new MatrixSizeException(Messages.getString("BaseMatrixUtil.8")); //$NON-NLS-1$
    }

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < idxcol; j++) {
        destination[i + rowMin][columnIndex[j]] = destination[0][0].create(source[i][j]);
      }
    }
  }

  /**
   * 指定された場所に値を代入します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param destination 値を代入する行列
   * @param rowIndex 指定する行を含む指数
   * @param columnIndex 指定する列を含む指数
   * @param source 代入する行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> void setSubMatrix(final S[][] destination, final int[] rowIndex, final int[] columnIndex, final int[][] source) {
    int idxcol1 = rowIndex.length;
    int idxcol2 = columnIndex.length;

    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;

    if (mrow != idxcol1 || mcol != idxcol2) {
      throw new MatrixSizeException(Messages.getString("BaseMatrixUtil.9")); //$NON-NLS-1$
    }

    for (int i = 0; i < idxcol1; i++) {
      for (int j = 0; j < idxcol2; j++) {
        destination[rowIndex[i]][columnIndex[j]] = destination[0][0].create(source[i][j]);
      }
    }
  }

  /**
   * 指定された場所に値を代入します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param destination 値を代入する行列
   * @param rowIndex 指定する行を含む指数
   * @param columnIndex 指定する列を含む指数
   * @param source 代入する行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> void setSubMatrix(final S[][] destination, final int[] rowIndex, final int[] columnIndex, final double[][] source) {
    int idxcol1 = rowIndex.length;
    int idxcol2 = columnIndex.length;

    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;

    if (mrow != idxcol1 || mcol != idxcol2) {
      throw new MatrixSizeException(Messages.getString("BaseMatrixUtil.10")); //$NON-NLS-1$
    }

    for (int i = 0; i < idxcol1; i++) {
      for (int j = 0; j < idxcol2; j++) {
        destination[rowIndex[i]][columnIndex[j]] = destination[0][0].create(source[i][j]);
      }
    }
  }

  /**
   * 配列<code>destination</code>の<code>min</code>から<code>max</code>まで 配列<code>source</code>の値をコピーします。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param destination コピー先
   * @param min コピー先の開始番号
   * @param max コピー先の終了番号
   * @param source コピー元
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> void setSubVector(final S[] destination, final int min, final int max, final int[] source) {
    int size = max - min + 1;

    for (int i = 0; i < size; i++) {
      destination[i + min] = destination[0].create(source[i]);
    }
  }

  /**
   * 配列<code>destination</code>の<code>min</code>から<code>max</code>まで 配列<code>source</code>の値をコピーします。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param destination コピー先
   * @param min コピー先の開始番号
   * @param max コピー先の終了番号
   * @param source コピー元
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> void setSubVector(final S[] destination, final int min, final int max, final double[] source) {
    int size = max - min + 1;

    for (int i = 0; i < size; i++) {
      destination[i + min] = destination[0].create(source[i]);
    }
  }

  /**
   * 指定された番号の成分に値を代入します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param destination 値を設定するグリッド
   * @param index 指定する成分番号を含む指数
   * @param source 代入するグリッド
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> void setElements(final S[][] destination, final int[] index, final int[][] source) {
    int rowSize = destination.length;
    int columnSize = rowSize == 0 ? 0 : destination[0].length;

    int size = index.length;
    int fromRowSize = source.length;
    int fromColumnSize = fromRowSize == 0 ? 0 : source[0].length;

    if (size > fromRowSize * fromColumnSize) {
      throw new MatrixSizeException(Messages.getString("BaseMatrixUtil.11")); //$NON-NLS-1$
    }

    for (int i = 0; i < size; i++) {
      int row = (index[i]) / columnSize;
      int column = (index[i]) % columnSize;
      destination[row][column] = destination[0][0].create(source[i / fromColumnSize][i % fromColumnSize]);
    }
  }

  /**
   * 指定された番号の成分に値を代入します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param destination 値を設定するグリッド
   * @param index 指定する成分番号を含む指数
   * @param source 代入するグリッド
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> void setElements(final S[][] destination, final int[] index, final double[][] source) {
    int rowSize = destination.length;
    int columnSize = rowSize == 0 ? 0 : destination[0].length;

    int size = index.length;
    int fromRowSize = source.length;
    int fromColumnSize = fromRowSize == 0 ? 0 : source[0].length;

    if (size > fromRowSize * fromColumnSize) {
      throw new MatrixSizeException(Messages.getString("BaseMatrixUtil.12")); //$NON-NLS-1$
    }

    for (int i = 0; i < size; i++) {
      int row = (index[i]) / columnSize;
      int column = (index[i]) % columnSize;
      destination[row][column] = destination[0][0].create(source[i / fromColumnSize][i % fromColumnSize]);
    }
  }

  /**
   * 複素数成分をもつか判定します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param matrix 対象となる行列
   * @return 複素数成分をもつならばtrue、そうでなければfalse
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> boolean isComplex(final S[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    for (int row = 0; row < rowSize; row++) {
      for (int column = 0; column < columnSize; column++) {
        if (matrix[row][column].isComplex()) {
          return true;
        }
      }
    }

    return false;
  }

  //  /**
  //   * 実部の2次元配列を返します。
  //   * 
  //   * @param matrix 対象となる行列
  //   * @return 実部の2次元配列
  //   */
  //  public static Scalar<?,?>[][] getRealPartElements(final Scalar<?,?>[][] matrix) {
  //    final int rowSize = matrix.length;
  //    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //
  //    final Scalar<?,?>[][] ans = matrix[0][0].getRealPart().createArray(rowSize, columnSize);
  //
  //    for (int row = 0; row < rowSize; row++) {
  //      for (int column = 0; column < columnSize; column++) {
  //        ans[row][column] = matrix[row][column].getRealPart();
  //      }
  //    }
  //    return ans;
  //  }
  //
  //  /**
  //   * 虚部の2次元配列を返します。
  //   * 
  //   * @param matrix 対象となる行列
  //   * @return 虚部の2次元配列
  //   */
  //  public static Scalar<?,?>[][] getImagPartElements(final Scalar<?,?>[][] matrix) {
  //    final int rowSize = matrix.length;
  //    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //
  //    final Scalar<?,?>[][] ans = matrix[0][0].getImaginaryPart().createArray(rowSize, columnSize);
  //
  //    for (int row = 0; row < rowSize; row++) {
  //      for (int column = 0; column < columnSize; column++) {
  //        ans[row][column] = matrix[row][column].getImaginaryPart();
  //      }
  //    }
  //    return ans;
  //  }
  //
  //  /**
  //   * 実部を設定します。
  //   * 
  //   * @param matrix 対象となる行列
  //   * @param realPart 変更値
  //   */
  //  public static void setRealPartElements(final Scalar<?,?>[][] matrix, final int[][] realPart) {
  //    int rowSize = matrix.length;
  //    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //    for (int i = 0; i < rowSize; i++) {
  //      for (int j = 0; j < columnSize; j++) {
  //        matrix[i][j].setRealPart(realPart[i][j]);
  //      }
  //    }
  //  }
  //
  //  /**
  //   * 虚部を設定します。
  //   * 
  //   * @param matrix 対象となる行列
  //   * @param imagPart 変更値
  //   */
  //  public static void setImagPartElements(final Scalar<?,?>[][] matrix, final int[][] imagPart) {
  //    int rowSize = matrix.length;
  //    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //    for (int i = 0; i < rowSize; i++) {
  //      for (int j = 0; j < columnSize; j++) {
  //        matrix[i][j].setImaginaryPart(imagPart[i][j]);
  //      }
  //    }
  //  }
  //
  //  /**
  //   * 実部を設定します。
  //   * 
  //   * @param matrix 対象となる行列
  //   * @param realPart 変更値
  //   */
  //  public static void setRealPartElements(final Scalar<?,?>[][] matrix, final double[][] realPart) {
  //    int rowSize = matrix.length;
  //    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //    for (int i = 0; i < rowSize; i++) {
  //      for (int j = 0; j < columnSize; j++) {
  //        matrix[i][j].setRealPart(realPart[i][j]);
  //      }
  //    }
  //  }
  //
  //  /**
  //   * 虚部を設定します。
  //   * 
  //   * @param matrix 対象となる行列
  //   * @param imagPart 変更値
  //   */
  //  public static void setImagPartElements(final Scalar<?,?>[][] matrix, final double[][] imagPart) {
  //    int rowSize = matrix.length;
  //    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //    for (int i = 0; i < rowSize; i++) {
  //      for (int j = 0; j < columnSize; j++) {
  //        matrix[i][j].setImaginaryPart(imagPart[i][j]);
  //      }
  //    }
  //  }
  //
  //  /**
  //   * 実部を設定します。
  //   * 
  //   * @param matrix 対象となる行列
  //   * @param realPart 変更値
  //   */
  //  public static void setRealPartElements(final Scalar<?,?>[][] matrix, final Scalar<?,?>[][] realPart) {
  //    int rowSize = matrix.length;
  //    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //    for (int i = 0; i < rowSize; i++) {
  //      for (int j = 0; j < columnSize; j++) {
  //        matrix[i][j].setRealPart(realPart[i][j]);
  //      }
  //    }
  //  }
  //
  //  /**
  //   * 虚部を設定します。
  //   * 
  //   * @param matrix 対象となる行列
  //   * @param imagPart 変更値
  //   */
  //  public static void setImagPartElements(final Scalar<?,?>[][] matrix, final Scalar<?,?>[][] imagPart) {
  //    int rowSize = matrix.length;
  //    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //    for (int i = 0; i < rowSize; i++) {
  //      for (int j = 0; j < columnSize; j++) {
  //        matrix[i][j].setImaginaryPart(imagPart[i][j]);
  //      }
  //    }
  //  }

  //  /**
  //   * 複素成分の2次元配列を返します。
  //   * 
  //   * @param matrix 対象となる行列
  //   * @return 複素成分の2次元配列
  //   */
  //  @SuppressWarnings("cast")
  //  public static Scalar<?,?>[][] toComplexElements(final Scalar<?,?>[][] matrix) {
  //    final int rowSize = matrix.length;
  //    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //
  //    final Scalar<?,?>[][] ans = (Scalar[][])matrix[0][0].toComplex().createArray(rowSize, columnSize);
  //
  //    for (int row = 0; row < rowSize; row++) {
  //      for (int column = 0; column < columnSize; column++) {
  //        ans[row][column] = matrix[row][column].toComplex();
  //      }
  //    }
  //    return ans;
  //  }

  //  /**
  //   * 複素成分の2次元配列を返します。
  //   * 
  //   * @param <T> 成分の型 
  //   * @param real 実部配列 
  //   * @param imag  虚部配列
  //   * @return 複素成分の2次元配列
  //   */
  //  public static <T extends NumericalScalar<T>>  BaseComplexNumericalScalar<T>[][] toComplexElements(final T[][] real, final T[][] imag) {
  //    final int rowSize = real.length;
  //    final int columnSize = rowSize == 0 ? 0 : real[0].length;
  //
  //    final BaseComplexNumericalScalar<T>[][] ans = (BaseComplexNumericalScalar<T>[][])real[0][0].toComplex().createArray(rowSize, columnSize);
  //
  //    for (int row = 0; row < rowSize; row++) {
  //      for (int column = 0; column < columnSize; column++) {
  //        ans[row][column] = (BaseComplexNumericalScalar<T>)real[row][column].toComplex();
  //        ans[row][column].setImaginaryPart(imag[row][column]);
  //      }
  //    }
  //    return ans;
  //  }

}
