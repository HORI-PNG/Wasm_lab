/**
 * $Id: NumericalMatrixUtil.java,v 1.25 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

import org.mklab.nfc.random.NormalRandom;
import org.mklab.nfc.random.RandomGenerator;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.DoubleComplexNumber;
import org.mklab.nfc.scalar.NumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;
import org.mklab.nfc.scalar.Scalar;
import org.mklab.nfc.svd.RealSingularValueDecomposer;


/**
 * {@link AbstractNumericalMatrix}のユーティリティクラスです。
 * 
 * @author koga
 * @version $Revision: 1.25 $
 */
public final class AbstractNumericalMatrixUtil {

  /**
   * 新しく生成された<code>BaseNumericalMatrixUtil</code>オブジェクトを初期化します。
   */
  private AbstractNumericalMatrixUtil() {
    // nothing to do
  }

  /**
   * 列毎のメジアンを成分とする行ベクトルを返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 中間値(メジアン)
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] medianColumnWise(final S[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    //    if (rowSize == 1) {
    //      return GridUtil.transpose(medianRowWise(matrix));
    //    }

    final S[][] sortedMatrix = GridUtil.<S> createZero(matrix[0][0], rowSize, columnSize);

    for (int i = 0; i < columnSize; i++) {
      final S[][] xi = sortColumnWise(GridUtil.getSubMatrix(matrix, 0, rowSize - 1, i, i));
      GridUtil.setSubMatrix(sortedMatrix, 0, rowSize - 1, i, i, xi);
    }

    if (rowSize % 2 != 0) {
      return GridUtil.getSubMatrix(sortedMatrix, rowSize / 2, rowSize / 2, 0, columnSize - 1);
    }

    final S[][] x1 = GridUtil.getSubMatrix(sortedMatrix, rowSize / 2 - 1, rowSize / 2 - 1, 0, columnSize - 1);
    final S[][] x2 = GridUtil.getSubMatrix(sortedMatrix, rowSize / 2, rowSize / 2, 0, columnSize - 1);
    return BaseMatrixUtil.divide(BaseMatrixUtil.<S, M> add(x1, x2), 2);
  }

  /**
   * 行毎のメジアンを成分とする列ベクトルを返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 中間値(メジアン)
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] medianRowWise(final S[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    //    if (columnSize == 1) {
    //      return GridUtil.transpose(medianColumnWise(matrix));
    //    }

    final S[][] sortedMatrix = GridUtil.<S> createZero(matrix[0][0], rowSize, columnSize);
    for (int i = 0; i < rowSize; i++) {
      final S[][] xi = sortRowWise(GridUtil.getSubMatrix(matrix, i, i, 0, columnSize - 1));
      GridUtil.setSubMatrix(sortedMatrix, i, i, 0, columnSize - 1, xi);
    }

    if (columnSize % 2 != 0) {
      return GridUtil.getSubMatrix(sortedMatrix, 0, rowSize - 1, columnSize / 2, columnSize / 2);
    }

    final S[][] x1 = GridUtil.getSubMatrix(sortedMatrix, 0, rowSize - 1, columnSize / 2 - 1, columnSize / 2 - 1);
    final S[][] x2 = GridUtil.getSubMatrix(sortedMatrix, 0, rowSize - 1, columnSize / 2, columnSize / 2);
    return BaseMatrixUtil.divide(BaseMatrixUtil.<S, M> add(x1, x2), 2);
  }

  /**
   * 全ての成分の中間値(メジアン)を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 全ての成分の中間値(メジアン)
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S median(final S[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final S[][] vector = GridUtil.reshape(matrix, 1, rowSize * columnSize);
    final S[][] ans = medianRowWise(vector);
    return ans[0][0];
  }

  /**
   * 行毎に昇順に並び替えた(絶対値でソートした)行列と元の位置を示す指数を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 行毎に昇順に並び替えた行列と元の位置を示す指数
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> IndexedElements<S, M> sortRowWiseWithIndex(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] index = new int[rowSize][columnSize];

    final S scalar = matrix[0][0];

    //    if (scalar instanceof DoubleComplexNumber) {
    //      DoubleComplexNumber[][] work = (DoubleComplexNumber[][])scalar.createArray(rowSize, columnSize);
    //      for (int i = 0; i < rowSize; i++) {
    //        int[] indexi = index[i];
    //        DoubleComplexNumber[] worki = work[i];
    //        DoubleComplexNumber[] matrixi = (DoubleComplexNumber[])matrix[i];
    //        for (int j = 0; j < columnSize; j++) {
    //          indexi[j] = j + 1;
    //          worki[j] = matrixi[j];
    //        }
    //        BaseNumericalMatrixUtil.quickSortByImaginaryPart(work[i], index[i], 0, columnSize - 1); // 虚部に対してソート
    //        BaseNumericalMatrixUtil.quickSortByRealPart(work[i], index[i], 0, columnSize - 1); // 実部に対してソート
    //      }
    //    } else 
    //      if (scalar instanceof BaseComplexNumericalScalar<?, ?>) {
    //      BaseComplexNumericalScalar<S, M>[][] work = (BaseComplexNumericalScalar<S, M>[][])scalar.createArray(rowSize, columnSize);
    //      for (int i = 0; i < rowSize; i++) {
    //        int[] indexi = index[i];
    //        BaseComplexNumericalScalar<S, M>[] worki = work[i];
    //        S[] matrixi = matrix[i];
    //        for (int j = 0; j < columnSize; j++) {
    //          indexi[j] = j + 1;
    //          worki[j] = (BaseComplexNumericalScalar<S, M>)matrixi[j];
    //        }
    //        BaseNumericalMatrixUtil.quickSortByImaginaryPart(work[i], index[i], 0, columnSize - 1); // 虚部に対してソート
    //        BaseNumericalMatrixUtil.quickSortByRealPart(work[i], index[i], 0, columnSize - 1); // 実部に対してソート
    //      }
    //    } else {
    S[][] work = scalar.createArray(rowSize, columnSize);
    for (int i = 0; i < rowSize; i++) {
      int[] indexi = index[i];
      S[] worki = work[i];
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        indexi[j] = j + 1;
        worki[j] = matrixi[j];
      }
      AbstractNumericalMatrixUtil.quickSort(work[i], index[i], 0, columnSize - 1); // 絶対値に対してソート
    }
    //    }

    S[][] ans = GridUtil.<S> createArray(rowSize, columnSize, matrix);

    for (int i = 0; i < rowSize; i++) {
      ans[i] = (GridUtil.getSubMatrix(matrix, i, IntMatrixUtil.decrement(index[i])))[0];
    }

    return new IndexedElements<>(ans, index);
  }

  /**
   * 行毎に昇順に並び替えた(絶対値でソートした)行列と元の位置を示す指数を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 行毎に昇順に並び替えた行列と元の位置を示す指数
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] sortRowWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] index = new int[rowSize][columnSize];

    S scalar = matrix[0][0];

    //    if (scalar instanceof DoubleComplexNumber) {
    //      DoubleComplexNumber[][] work = (DoubleComplexNumber[][])scalar.createArray(rowSize, columnSize);
    //      for (int i = 0; i < rowSize; i++) {
    //        int[] indexi = index[i];
    //        DoubleComplexNumber[] worki = work[i];
    //        DoubleComplexNumber[] matrixi = (DoubleComplexNumber[])matrix[i];
    //        for (int j = 0; j < columnSize; j++) {
    //          indexi[j] = j + 1;
    //          worki[j] = matrixi[j];
    //        }
    //        BaseNumericalMatrixUtil.quickSortByImaginaryPart(work[i], index[i], 0, columnSize - 1); // 虚部に対してソート
    //        BaseNumericalMatrixUtil.quickSortByRealPart(work[i], index[i], 0, columnSize - 1); // 実部に対してソート
    //      }
    //    } else
    //      if (scalar instanceof BaseComplexNumericalScalar<?, ?>) {
    //      BaseComplexNumericalScalar<S, M>[][] work = (BaseComplexNumericalScalar<S, M>[][])scalar.createArray(rowSize, columnSize);
    //      for (int i = 0; i < rowSize; i++) {
    //        int[] indexi = index[i];
    //        BaseComplexNumericalScalar<S, M>[] worki = work[i];
    //        BaseComplexNumericalScalar<S, M>[] matrixi = (BaseComplexNumericalScalar<S, M>[])matrix[i];
    //        for (int j = 0; j < columnSize; j++) {
    //          indexi[j] = j + 1;
    //          worki[j] = matrixi[j];
    //        }
    //        BaseNumericalMatrixUtil.quickSortByImaginaryPart(work[i], index[i], 0, columnSize - 1); // 虚部に対してソート
    //        BaseNumericalMatrixUtil.quickSortByRealPart(work[i], index[i], 0, columnSize - 1); // 実部に対してソート
    //      }
    //    } else {
    S[][] work = scalar.createArray(rowSize, columnSize);
    for (int i = 0; i < rowSize; i++) {
      int[] indexi = index[i];
      S[] worki = work[i];
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        indexi[j] = j + 1;
        worki[j] = matrixi[j];
      }
      AbstractNumericalMatrixUtil.quickSort(work[i], index[i], 0, columnSize - 1); // 絶対値に対してソート
    }
    //    }

    S[][] ans = GridUtil.<S> createArray(rowSize, columnSize, matrix);

    for (int i = 0; i < rowSize; i++) {
      ans[i] = (GridUtil.getSubMatrix(matrix, i, IntMatrixUtil.decrement(index[i])))[0];
    }

    return ans;
  }

  /**
   * 列毎に昇順に並び替えた(絶対値でソートした)行列と元の位置を示す指数を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 列毎に昇順に並び替えた行列と元の位置を示す指数
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> IndexedElements<S, M> sortColumnWiseWithIndex(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] index = new int[columnSize][rowSize];

    final S scalar = matrix[0][0];

    //    if (scalar instanceof DoubleComplexNumber) {
    //      DoubleComplexNumber[][] work = (DoubleComplexNumber[][])scalar.createArray(columnSize, rowSize);
    //      for (int i = 0; i < columnSize; i++) {
    //        int[] indexi = index[i];
    //        DoubleComplexNumber[] worki = work[i];
    //        for (int j = 0; j < rowSize; j++) {
    //          indexi[j] = j + 1;
    //          worki[j] = (DoubleComplexNumber)matrix[j][i];
    //        }
    //        BaseNumericalMatrixUtil.quickSortByImaginaryPart(work[i], index[i], 0, rowSize - 1); // 虚部に対してソート
    //        BaseNumericalMatrixUtil.quickSortByRealPart(work[i], index[i], 0, rowSize - 1); // 実部に対してソート
    //      }
    //    } else
    //      if (scalar instanceof BaseComplexNumericalScalar<?, ?>) {
    //      BaseComplexNumericalScalar<S, M>[][] work = (BaseComplexNumericalScalar<S, M>[][])scalar.createArray(columnSize, rowSize);
    //      for (int i = 0; i < columnSize; i++) {
    //        int[] indexi = index[i];
    //        BaseComplexNumericalScalar<S, M>[] worki = work[i];
    //        for (int j = 0; j < rowSize; j++) {
    //          indexi[j] = j + 1;
    //          worki[j] = (BaseComplexNumericalScalar<S, M>)matrix[j][i];
    //        }
    //        BaseNumericalMatrixUtil.quickSortByImaginaryPart(work[i], index[i], 0, rowSize - 1); // 虚部に対してソート
    //        BaseNumericalMatrixUtil.quickSortByRealPart(work[i], index[i], 0, rowSize - 1); // 実部に対してソート
    //      }
    //    } else {
    S[][] work = scalar.createArray(columnSize, rowSize);
    for (int i = 0; i < columnSize; i++) {
      int[] indexi = index[i];
      S[] worki = work[i];
      for (int j = 0; j < rowSize; j++) {
        indexi[j] = j + 1;
        worki[j] = matrix[j][i];
      }
      AbstractNumericalMatrixUtil.quickSort(work[i], index[i], 0, rowSize - 1); // 絶対値に対してソート
    }
    //    }

    S[][] ans = GridUtil.<S> createArray(columnSize, rowSize, matrix);
    for (int i = 0; i < columnSize; i++) {
      ans[i] = (GridUtil.transpose(GridUtil.getSubMatrix(matrix, IntMatrixUtil.decrement(index[i]), i)))[0];
    }

    return new IndexedElements<>(GridUtil.transpose(ans), IntMatrixUtil.transpose(index));
  }

  /**
   * 列毎に昇順に並び替えた(絶対値でソートした)行列と元の位置を示す指数を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 列毎に昇順に並び替えた行列と元の位置を示す指数
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] sortColumnWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    int[][] index = new int[columnSize][rowSize];

    final S scalar = matrix[0][0];

    //    if (scalar instanceof DoubleComplexNumber) {
    //      DoubleComplexNumber[][] work = (DoubleComplexNumber[][])scalar.createArray(columnSize, rowSize);
    //      for (int i = 0; i < columnSize; i++) {
    //        int[] indexi = index[i];
    //        DoubleComplexNumber[] worki = work[i];
    //        for (int j = 0; j < rowSize; j++) {
    //          indexi[j] = j + 1;
    //          worki[j] = (DoubleComplexNumber)matrix[j][i];
    //        }
    //        BaseNumericalMatrixUtil.quickSortByImaginaryPart(work[i], index[i], 0, rowSize - 1); // 虚部に対してソート
    //        BaseNumericalMatrixUtil.quickSortByRealPart(work[i], index[i], 0, rowSize - 1); // 実部に対してソート
    //      }
    //    } else 
    //      if (scalar instanceof BaseComplexNumericalScalar<?, ?>) {
    //      BaseComplexNumericalScalar<S, M>[][] work = (BaseComplexNumericalScalar<S, M>[][])scalar.createArray(columnSize, rowSize);
    //      for (int i = 0; i < columnSize; i++) {
    //        int[] indexi = index[i];
    //        BaseComplexNumericalScalar<S, M>[] worki = work[i];
    //        for (int j = 0; j < rowSize; j++) {
    //          indexi[j] = j + 1;
    //          worki[j] = (BaseComplexNumericalScalar<S, M>)matrix[j][i];
    //        }
    //        BaseNumericalMatrixUtil.quickSortByImaginaryPart(work[i], index[i], 0, rowSize - 1); // 虚部に対してソート
    //        BaseNumericalMatrixUtil.quickSortByRealPart(work[i], index[i], 0, rowSize - 1); // 実部に対してソート
    //      }
    //    } else {
    S[][] work = scalar.createArray(columnSize, rowSize);
    for (int i = 0; i < columnSize; i++) {
      int[] indexi = index[i];
      S[] worki = work[i];
      for (int j = 0; j < rowSize; j++) {
        indexi[j] = j + 1;
        worki[j] = matrix[j][i];
      }
      AbstractNumericalMatrixUtil.quickSort(work[i], index[i], 0, rowSize - 1); // 絶対値に対してソート
    }
    //    }

    S[][] ans = GridUtil.<S> createArray(columnSize, rowSize, matrix);
    for (int i = 0; i < columnSize; i++) {
      ans[i] = (GridUtil.transpose(GridUtil.getSubMatrix(matrix, IntMatrixUtil.decrement(index[i]), i)))[0];
    }

    return GridUtil.transpose(ans);
  }

  /**
   * 行毎に標準偏差を計算し、計算結果を成分とする列ベクトルを生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする列ベクトル
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] stdRowWise(final S[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final S[][] mean = BaseMatrixUtil.meanRowWise(matrix);
    final S[][] ans = GridUtil.<S> createArray(rowSize, 1, matrix);

    for (int i = 0; i < rowSize; i++) {
      S sum = matrix[0][0].createZero();
      for (int j = 0; j < columnSize; j++) {
        sum = sum.add(matrix[i][j].subtract(mean[i][0]).power(2));
      }
      ans[i][0] = sum.divide(columnSize - 1).sqrt();
    }
    return ans;
  }

  /**
   * 列毎に標準偏差を計算し、計算結果を成分とする列ベクトルを生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする列ベクトル
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] stdColumnWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    S[][] mean = BaseMatrixUtil.<S, M> meanColumnWise(matrix);
    S[][] ans = GridUtil.<S> createArray(1, columnSize, matrix);
    for (int j = 0; j < columnSize; j++) {
      S sum = matrix[0][0].createZero();
      for (int i = 0; i < rowSize; i++) {
        sum = sum.add(matrix[i][j].subtract(mean[0][j]).power(2));
      }
      ans[0][j] = sum.divide(rowSize - 1).sqrt();
    }
    return ans;
  }

  /**
   * 行列の全ての成分の標準偏差を求めます。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 行列の全ての成分の標準偏差
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S std(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    S m = BaseMatrixUtil.<S, M> mean(matrix);
    S sum = matrix[0][0].createZero();
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        sum = sum.add(matrix[i][j].subtract(m).power(2));
      }
    }
    return sum.divide(rowSize * columnSize - 1).sqrt();
  }

  /**
   * 全ての成分の絶対値を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 元の行列
   * @return 成分の絶対値を成分とする行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] absElementWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    S[][] ans = matrix[0][0].createArray(rowSize, columnSize);
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].abs();
      }
    }
    return ans;
  }

  /**
   * 行列のフロベニウスノムルを返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 行列のフロベニウスノムル
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S frobNorm(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    //    if (matrix[0][0] instanceof DoubleComplexNumber) {
    //      double sum = 0;
    //      for (int i = 0; i < rowSize; i++) {
    //        for (int j = 0; j < columnSize; j++) {
    //          sum += ((DoubleComplexNumber)matrix[i][j]).abs2().getRealPart().doubleValue();
    //        }
    //      }
    //      return matrix[0][0].create(Math.sqrt(sum));
    //    }

    S sum = matrix[0][0].abs2().createZero();
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        //sum.addSelf((matrix[i][j]).abs2());
        sum = sum.add((matrix[i][j]).abs2());
      }
    }
    return sum.sqrt();

  }

  /**
   * 行毎のフロベニウスノムルを成分とする列ベクトル返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 行毎のフロベニウスノムルを成分とする列ベクトル
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] frobNormRowWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    S[][] ans = matrix[0][0].createArray(rowSize, 1);

    //    if (matrix[0][0] instanceof DoubleComplexNumber) {
    //      for (int i = 0; i < rowSize; i++) {
    //        double sum = 0;
    //        for (int j = 0; j < columnSize; j++) {
    //          sum += ((DoubleComplexNumber)matrix[i][j]).abs2().getRealPart().doubleValue();
    //        }
    //        ans[i][0] = (S)new DoubleComplexNumber(Math.sqrt(sum), 0);
    //      }
    //      return ans;
    //    }

    for (int i = 0; i < rowSize; i++) {
      S sum = matrix[0][0].createZero();
      for (int j = 0; j < columnSize; j++) {
        //sum.addSelf((matrix[i][j]).abs2());
        sum = sum.add((matrix[i][j]).abs2());
      }
      ans[i][0] = sum.sqrt();
    }
    return ans;

  }

  /**
   * 列毎のフロベニウスノムルを成分とする行ベクトル返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 列毎のフロベニウスノムルを成分とする行ベクトル
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] frobNormColumnWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    S[][] ans = matrix[0][0].createArray(1, columnSize);

    //    if (matrix[0][0] instanceof DoubleComplexNumber) {
    //      for (int i = 0; i < columnSize; i++) {
    //        double sum = 0;
    //        for (int j = 0; j < rowSize; j++) {
    //          sum += ((DoubleComplexNumber)matrix[j][i]).abs2().getRealPart().doubleValue();
    //        }
    //        ans[0][i] = (S)new DoubleComplexNumber(Math.sqrt(sum), 0);
    //      }
    //      return ans;
    //    }

    for (int i = 0; i < columnSize; i++) {
      S sum = matrix[0][0].createZero();
      for (int j = 0; j < rowSize; j++) {
        //sum.addSelf((matrix[j][i]).abs2());
        sum = sum.add((matrix[j][i]).abs2());
      }
      ans[0][i] = sum.sqrt();
    }
    return ans;

  }

  /**
   * 複素行列のフロベニウスノルムを返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrixRe 実部行列
   * @param matrixIm 虚部行列
   * @return フロベニウスノルム
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S frobNorm(final S[][] matrixRe, final S[][] matrixIm) {
    int rowSize = matrixRe.length;
    int columnSize = rowSize == 0 ? 0 : matrixRe[0].length;
    S sum = matrixRe[0][0].createZero();
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        sum = sum.add(matrixRe[i][j].multiply(matrixRe[i][j]).add(matrixIm[i][j].multiply(matrixIm[i][j])));
      }
    }
    return sum.sqrt();
  }

  /**
   * 行列の無限大ノルムを返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 行列の無限大ノルム
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S infNorm(final S[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    //    if (matrix[0][0] instanceof DoubleComplexNumber) {
    //      double max = 0;
    //      for (int i = 0; i < rowSize; i++) {
    //        double d = 0;
    //        for (int j = 0; j < columnSize; j++) {
    //          d += ((DoubleComplexNumber)matrix[i][j]).abs().getRealPart().doubleValue();
    //        }
    //        if (max < d) {
    //          max = d;
    //        }
    //      }
    //      return (S)new DoubleComplexNumber(max, 0);
    //    }

    final S[][] max = matrix[0][0].createArray(rowSize, 1);
    for (int i = 0; i < rowSize; i++) {
      S d = matrix[0][0].createZero();
      for (int j = 0; j < columnSize; j++) {
        d = d.add(matrix[i][j].abs());
      }
      max[i][0] = d;
    }

    final int[] index = AbstractNumericalMatrixUtil.indexOfMaximum(max);
    return max[index[0] - 1][index[1] - 1];
  }

  /**
   * 行列の最大成分を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 最大成分
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S max(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int rowIdx = 0, colIdx = 0;
    S max = matrix[0][0];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        final S value = matrix[i][j];
        if (max.isLessThan(value)) {
          max = value;
          rowIdx = i;
          colIdx = j;
        }
      }
    }
    return matrix[rowIdx][colIdx].clone();
  }

  /**
   * ベクトルの最大成分を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 最大成分
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S max(final S[] matrix) {
    int rowSize = matrix.length;

    int rowIdx = 0;
    S max = matrix[0];
    for (int i = 0; i < rowSize; i++) {
      final S value = matrix[i];
      if (max.isLessThan(value)) {
        max = value;
        rowIdx = i;
      }
    }
    return matrix[rowIdx].clone();
  }

  /**
   * 行列の最小成分を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 最小成分
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S min(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int rowIdx = 0;
    int colIdx = 0;
    S min = matrix[0][0];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        S value = matrix[i][j];
        if (min.isGreaterThan(value)) {
          min = value;
          rowIdx = i;
          colIdx = j;
        }
      }
    }
    return matrix[rowIdx][colIdx].clone();
  }

  /**
   * ベクトルの最小成分を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 最小成分
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S min(final S[] matrix) {
    int rowSize = matrix.length;

    int rowIdx = 0;
    S min = matrix[0];
    for (int i = 0; i < rowSize; i++) {
      S value = matrix[i];
      if (min.isGreaterThan(value)) {
        min = value;
        rowIdx = i;
      }
    }
    return matrix[rowIdx].clone();
  }

  /**
   * 成分毎に大きさを比較し、大きい方を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param a1 第一行列
   * @param a2 第二行列
   * @return 生成された行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] maxElementWise(final S[][] a1, final S[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (rowSize != rowSize2 || columnSize != columnSize2) {
      throw new MatrixSizeException(Messages.getString("NumericalMatrixUtil.0")); //$NON-NLS-1$
    }

    S[][] ans = GridUtil.<S> createArray(rowSize, columnSize, a1);
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        if (a1[i][j].isGreaterThanOrEquals(a2[i][j])) {
          ans[i][j] = a1[i][j].clone();
        } else {
          ans[i][j] = a2[i][j].clone();
        }
      }
    }
    return ans;
  }

  /**
   * 成分毎に大きさを比較し、小さい方を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param a1 第一行列
   * @param a2 第二行列
   * @return 生成された行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] minElementWise(final S[][] a1, final S[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (rowSize != rowSize2 || columnSize != columnSize2) {
      throw new MatrixSizeException(Messages.getString("NumericalMatrixUtil.1")); //$NON-NLS-1$
    }

    S[][] ans = GridUtil.<S> createArray(rowSize, columnSize, a1);
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        if (a1[i][j].isLessThanOrEquals(a2[i][j])) {
          ans[i][j] = a1[i][j].clone();
        } else {
          ans[i][j] = a2[i][j].clone();
        }
      }
    }
    return ans;
  }

  /**
   * 行列の全ての成分毎に累乗を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 累乗の対象となる値を成分とする行列
   * @param scalar 累乗の指数
   * @return 生成された行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] powerElementWise(final S[][] matrix, final double scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0].power(scalar);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].power(scalar);
      }
    }
    return ans;
  }

  /**
   * 行列の全ての成分毎に累乗を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 累乗の対象となる値を成分とする行列
   * @param scalar 累乗の指数
   * @return 生成された行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] powerElementWise(final S[][] matrix, final S scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    S ans00 = matrix[0][0].power(scalar);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].power(scalar);
      }
    }
    return ans;
  }

  //  /**
  //   * 行列の全ての成分毎に累乗を計算し、計算結果を成分とする行列を生成します。
  //   * 
  //   * @param <S> スカラーの型
  //   * @param <M> 行列の型
  //   * 
  //   * @param matrix 累乗の対象となる値を成分とする行列
  //   * @param scalar 累乗の指数
  //   * @return 生成された行列
  //   */
  //  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] powerElementWise(final int[][] matrix, final S scalar) {
  //    int rowSize = matrix.length;
  //    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //
  //    if (rowSize == 0 || columnSize == 0) {
  //      final S[] value = scalar.createArray(1);
  //      value[0] = scalar;
  //      return GridUtil.<S> createArray(rowSize, columnSize, value);
  //    }
  //
  //    S realScalar;
  //    if (scalar.isComplex()) {
  //      if (scalar instanceof DoubleComplexNumber) {
  //        realScalar = (S)((DoubleComplexNumber)scalar).getRealPart();
  //      } else {
  //        realScalar = (S)((BaseComplexNumericalScalar<?, ?>)scalar).getRealPart();
  //      }
  //    } else {
  //      realScalar = scalar;
  //    }
  //
  //    S ans00 = realScalar.transformFrom(matrix[0][0]).power(scalar);
  //    S[][] ans = ans00.createArray(rowSize, columnSize);
  //
  //    for (int i = 0; i < rowSize; i++) {
  //      for (int j = 0; j < columnSize; j++) {
  //        ans[i][j] = realScalar.transformFrom(matrix[i][j]).power(scalar);
  //      }
  //    }
  //    return ans;
  //  }

  /**
   * 行列の成分毎に累乗を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param a1 累乗の対象となる値を成分とする行列
   * @param a2 累乗の指数(実数)を成分とする行列
   * @return 生成された行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] powerElementWise(final S[][] a1, final double[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (rowSize != rowSize2 || columnSize != columnSize2) {
      throw new MatrixSizeException(Messages.getString("NumericalMatrixUtil.2")); //$NON-NLS-1$
    }

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, a1);
    }

    S ans00 = a1[0][0].power(a2[0][0]);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = a1[i][j].power(a2[i][j]);
      }
    }
    return ans;
  }

  /**
   * 行列の成分毎に累乗を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param a1 累乗の対象となる値を成分とする行列
   * @param a2 累乗の指数を成分とする行列
   * @return 生成された行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] powerElementWise(final S[][] a1, final S[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (rowSize != rowSize2 || columnSize != columnSize2) {
      throw new MatrixSizeException(Messages.getString("NumericalMatrixUtil.3")); //$NON-NLS-1$
    }

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, a1);
    }

    S ans00 = a1[0][0].power(a2[0][0]);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = a1[i][j].power(a2[i][j]);
      }
    }
    return ans;
  }

  /**
   * 行毎に最大値を計算し、計算結果を成分とする列ベクトルを生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする列ベクトル
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] maxRowWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    S[][] ans = GridUtil.<S> createArray(rowSize, 1, matrix);

    for (int i = 0; i < rowSize; i++) {
      int column = 0;
      S max = matrix[i][0];
      for (int j = 1; j < columnSize; j++) {
        S value = matrix[i][j];
        if (max.isLessThan(value)) {
          max = value;
          column = j;
        }
      }
      ans[i][0] = matrix[i][column].clone();
    }
    return ans;
  }

  /**
   * 列毎に最大値を計算し、計算結果を成分とする列ベクトルを生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする列ベクトル
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] maxColumnWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    S[][] ans = GridUtil.<S> createArray(1, columnSize, matrix);

    for (int i = 0; i < columnSize; i++) {
      int row = 0;
      S max = matrix[0][i];
      for (int j = 1; j < rowSize; j++) {
        S value = matrix[j][i];
        if (max.isLessThan(value)) {
          max = value;
          row = j;
        }
      }
      ans[0][i] = matrix[row][i].clone();
    }
    return ans;
  }

  /**
   * 行毎に最小値を計算し、計算結果を成分とする行ベクトルを生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする行ベクトル
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] minRowWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    S[][] ans = GridUtil.<S> createArray(rowSize, 1, matrix);

    for (int i = 0; i < rowSize; i++) {
      int column = 0;
      S min = matrix[i][0];
      for (int j = 1; j < columnSize; j++) {
        S value = matrix[i][j];
        if (min.isGreaterThan(value)) {
          min = value;
          column = j;
        }
      }
      ans[i][0] = matrix[i][column].clone();
    }
    return ans;
  }

  /**
   * 列毎に最小値を計算し、計算結果を成分とする列ベクトルを生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 計算結果を成分とする列ベクトル
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] minColumnWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    S[][] ans = GridUtil.<S> createArray(1, columnSize, matrix);

    for (int i = 0; i < columnSize; i++) {
      int row = 0;
      S min = matrix[0][i];
      for (int j = 1; j < rowSize; j++) {
        S value = matrix[j][i];
        if (min.isGreaterThan(value)) {
          min = value;
          row = j;
        }
      }
      ans[0][i] = matrix[row][i].clone();
    }
    return ans;
  }

  /**
   * 最大成分とその行番号(1から始まります)と列番号(1から始まります)を求めます。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 最大成分とその行番号(1から始まります)と列番号(1から始まります)
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> Object[] maximum(final S[][] matrix) {
    final int[] index = indexOfMaximum(matrix);
    final S maximumValue = matrix[index[0] - 1][index[1] - 1].clone();
    return new Object[] {maximumValue, Integer.valueOf(index[0]), Integer.valueOf(index[1])};
  }

  /**
   * 最大成分の行番号(1から始まります)と列番号(1から始まります)を求めます。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 最大成分の行番号(1から始まります)と列番号(1から始まります)
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> int[] indexOfMaximum(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final S element00 = matrix[0][0];
    S max = element00;
    int row = 0;
    int column = 0;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        final S element = matrix[i][j];
        //S value = element.isReal() ? element : element.abs();
        S value = element;
        if (max.isLessThan(value)) {
          max = value;
          row = i;
          column = j;
        }
      }
    }

    return new int[] {row + 1, column + 1};
  }

  /**
   * 最大成分の行番号(1から始まります)と列番号(1から始まります)を求めます。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 最大成分の行番号(1から始まります)と列番号(1から始まります)
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> int[] indexOfMaximum(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final S element00 = matrix[0][0];
    S max = element00;
    int row = 0;
    int column = 0;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        final S element = matrix[i][j];
        final S value = element;
        //if (max.isLessThan(value)) {
        final S err = max.subtract(value);
        if ((err.isReal() && err.compare(".<", 0)) || (err.isComplex() && ((NumericalScalar<?, ?>)err).abs().compare(".<", 0))) { //$NON-NLS-1$ //$NON-NLS-2$
          max = value;
          row = i;
          column = j;
        }
      }
    }

    return new int[] {row + 1, column + 1};
  }

  /**
   * 行毎の最大成分とその指数を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 行毎の最大成分とその指数
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> Object[] maximumRowWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    S[][] ans = GridUtil.<S> createArray(rowSize, 1, matrix);

    int[] index = new int[rowSize];
    for (int i = 0; i < rowSize; i++) {
      S max = matrix[i][0];
      index[i] = 1;
      for (int j = 1; j < columnSize; j++) {
        S value = matrix[i][j];
        if (max.isLessThan(value)) {
          max = value;
          index[i] = j + 1;
        }
      }
      ans[i][0] = matrix[i][index[i] - 1].clone();
    }
    return new Object[] {ans, index};
  }

  /**
   * 列毎の最大成分とその指数を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 列毎の最大成分とその指数
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> Object[] maximumColumnWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    S[][] ans = GridUtil.<S> createArray(1, columnSize, matrix);

    int[] index = new int[rowSize];

    for (int i = 0; i < columnSize; i++) {
      S max = matrix[0][i];
      index[i] = 1;
      for (int j = 1; j < rowSize; j++) {
        S value = matrix[j][i];
        if (max.isLessThan(value)) {
          max = value;
          index[i] = j + 1;
        }
      }
      ans[0][i] = matrix[index[i] - 1][i].clone();
    }

    return new Object[] {ans, index};
  }

  /**
   * 最小成分と行番号(1から始まります)と列番号(1から始まります)を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 最小成分とその行番号(1から始まります)と列番号(1から始まります)
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> Object[] minimum(final S[][] matrix) {
    final int[] index = indexOfMinimum(matrix);
    final S minimumValue = matrix[index[0] - 1][index[1] - 1].clone();
    return new Object[] {minimumValue, Integer.valueOf(index[0]), Integer.valueOf(index[1])};
  }

  /**
   * 最小成分の行番号(1から始まります)と列番号(1から始まります)を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 最小成分の行番号(1から始まります)と列番号(1から始まります)
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> int[] indexOfMinimum(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    S min = matrix[0][0];
    int row = 0;
    int column = 0;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        S value = matrix[i][j];
        if (min.isGreaterThan(value)) {
          min = value;
          row = i;
          column = j;
        }
      }
    }

    //    if (rowSize == 1) {
    //      return new int[] {1, column + 1};
    //    }
    //
    //    if (columnSize == 1) {
    //      return new int[] {row + 1, 1};
    //    }

    return new int[] {row + 1, column + 1};
  }

  /**
   * 行毎の最小成分とその指数を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 行毎の最小成分とその指数
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> Object[] minimumRowWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    S[][] ans = GridUtil.<S> createArray(rowSize, 1, matrix);

    int[] index = new int[rowSize];
    for (int i = 0; i < rowSize; i++) {
      S min = matrix[i][0];
      index[i] = 1;
      for (int j = 1; j < columnSize; j++) {
        S value = matrix[i][j];
        if (min.isGreaterThan(value)) {
          min = value;
          index[i] = j + 1;
        }
      }
      ans[i][0] = matrix[i][index[i] - 1].clone();
    }

    return new Object[] {ans, index};
  }

  /**
   * 列毎に最小成分とその指数を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 列毎の最小成分とその指数
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> Object[] minimumColumnWise(final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    S[][] ans = GridUtil.<S> createArray(1, columnSize, matrix);

    int[] index = new int[rowSize];

    for (int i = 0; i < columnSize; i++) {
      S min = matrix[0][i];
      index[i] = 1;
      for (int j = 1; j < rowSize; j++) {
        S value = matrix[j][i];
        if (min.isGreaterThan(value)) {
          min = value;
          index[i] = j + 1;
        }
      }
      ans[0][i] = matrix[index[i] - 1][i].clone();
    }

    return new Object[] {ans, index};
  }

  /**
   * 自身の各成分の(x/abs(x)を成分に持つ行列を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 符合行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] signumElementWise(final S[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    final S c00 = matrix[0][0];
    final S ans00 = c00.divide(c00.abs());
    final S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        final S c = matrix[i][j];
        if (c.isZero()) {
          ans[i][j] = c.createZero();
        } else {
          ans[i][j] = c.divide(c.abs());
        }
      }
    }
    return ans;
  }

  /**
   * スカラーの累乗を実行列の成分毎に求めます。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param scalar 累乗の対象となるスカラー
   * @param matrix 実行列(累乗の指数を成分とする)
   * @return スカラーの累乗を成分とする行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] powerElementWise(final S scalar, final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return scalar.createArray(rowSize, columnSize);
    }

    S ans00 = scalar.power(matrix[0][0]);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      double[] mmi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = scalar.power(mmi[j]);
      }
    }
    return ans;
  }

  /**
   * スカラーの累乗を複素行列の成分毎に求めます。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param scalar 累乗の対象となるスカラー
   * @param matrix 累乗の指数を成分とする行列
   * @return スカラーの累乗を成分とする行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] powerElementWise(final S scalar, final S[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return scalar.createArray(rowSize, columnSize);
    }

    S ans00 = scalar.power(matrix[0][0]);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = scalar.power(matrix[i][j]);
      }
    }
    return ans;
  }

  /**
   * ベクトルの成分を昇順にソートした結果を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param vector 対象となるベクトル
   * @param index 並び替えた成分の番号を記憶する配列
   * @param start ソートの対象となる成分の開始番号
   * @param end ソートの対象となる成分の終了番号
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> void quickSort(final S[] vector, final int[] index, final int start, final int end) {
    if (start >= end) {
      return;
    }

    // 基準値 piv
    S piv = vector[(start + end) >>> 1];
    int i = start;
    int j = end;
    while (i <= j) {
      // piv 以上の成分を探す
      while (vector[i].isLessThan(piv)) {
        i++;
      }
      // piv 以下の成分を探す
      while (vector[j].isGreaterThan(piv)) {
        j--;
      }

      if (i <= j) { // swaping
        S tmp = vector[i];
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
   * ベクトルの成分を虚部の昇順にソートした結果を返します。
   * 
   * @param vector 対象となるベクトル
   * @param index 並び替えた成分の番号を記憶する配列
   * @param start ソートの対象となる成分の開始番号
   * @param end ソートの対象となる成分の終了番号
   */
  public static void quickSortByImaginaryPart(final DoubleComplexNumber[] vector, final int[] index, final int start, final int end) {
    if (start >= end) {
      return;
    }

    // 基準値 piv
    DoubleComplexNumber piv = vector[(start + end) >>> 1];
    int i = start;
    int j = end;
    while (i <= j) {

      // piv 以上の成分を探す
      while (vector[i].getImaginaryPart().isLessThan(piv.getImaginaryPart())) {
        i++;
      }
      // piv 以下の成分を探す
      while (vector[j].getImaginaryPart().isGreaterThan(piv.getImaginaryPart())) {
        j--;
      }

      if (i <= j) { // swaping
        DoubleComplexNumber tmp = vector[i];
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
      quickSortByImaginaryPart(vector, index, start, j);
    }
    // piv より右の部分配列をソート
    if (i < end) {
      quickSortByImaginaryPart(vector, index, i, end);
    }
  }

  /**
   * ベクトルの成分を虚部の昇順にソートした結果を返します。
   * 
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
   * 
   * @param vector 対象となるベクトル
   * @param index 並び替えた成分の番号を記憶する配列
   * @param start ソートの対象となる成分の開始番号
   * @param end ソートの対象となる成分の終了番号
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> void quickSortByImaginaryPart(final CS[] vector, final int[] index,
      final int start, final int end) {
    if (start >= end) {
      return;
    }

    // 基準値 piv
    CS piv = vector[(start + end) >>> 1];
    int i = start;
    int j = end;
    while (i <= j) {
      while (vector[i].getImaginaryPart().isLessThan(piv.getImaginaryPart())) {
        i++;
      }
      // piv 以下の成分を探す
      while (vector[j].getImaginaryPart().isGreaterThan(piv.getImaginaryPart())) {
        j--;
      }

      if (i <= j) { // swaping
        CS tmp = vector[i];
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
      quickSortByImaginaryPart(vector, index, start, j);
    }
    // piv より右の部分配列をソート
    if (i < end) {
      quickSortByImaginaryPart(vector, index, i, end);
    }
  }

  /**
   * ベクトルの成分を実部の昇順にソートした結果を返します。
   * 
   * @param vector 対象となるベクトル
   * @param index 並び替えた成分の番号を記憶する配列
   * @param start ソートの対象となる成分の開始番号
   * @param end ソートの対象となる成分の終了番号
   */
  public static void quickSortByRealPart(final DoubleComplexNumber[] vector, final int[] index, final int start, final int end) {
    if (start >= end) {
      return;
    }

    // 基準値 piv
    DoubleComplexNumber piv = vector[(start + end) >>> 1];
    int i = start;
    int j = end;
    while (i <= j) {
      // piv 以上の成分を探す
      while (vector[i].getRealPart().isLessThan(piv.getRealPart())) {
        i++;
      }
      // piv 以下の成分を探す
      while (vector[j].getRealPart().isGreaterThan(piv.getRealPart())) {
        j--;
      }

      if (i <= j) { // swaping
        DoubleComplexNumber tmp = vector[i];
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
      quickSortByRealPart(vector, index, start, j);
    }
    // piv より右の部分配列をソート
    if (i < end) {
      quickSortByRealPart(vector, index, i, end);
    }
  }

  /**
   * ベクトルの成分を実部の昇順にソートした結果を返します。
   * 
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
   * 
   * @param vector 対象となるベクトル
   * @param index 並び替えた成分の番号を記憶する配列
   * @param start ソートの対象となる成分の開始番号
   * @param end ソートの対象となる成分の終了番号
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> void quickSortByRealPart(final CS[] vector, final int[] index, final int start,
      final int end) {
    if (start >= end) {
      return;
    }

    // 基準値 piv
    CS piv = vector[(start + end) >>> 1];
    int i = start;
    int j = end;
    while (i <= j) {
      // piv 以上の成分を探す
      while (vector[i].getRealPart().isLessThan(piv.getRealPart())) {
        i++;
      }
      // piv 以下の成分を探す
      while (vector[j].getRealPart().isGreaterThan(piv.getRealPart())) {
        j--;
      }

      if (i <= j) { // swaping
        CS tmp = vector[i];
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
      quickSortByRealPart(vector, index, start, j);
    }
    // piv より右の部分配列をソート
    if (i < end) {
      quickSortByRealPart(vector, index, i, end);
    }
  }

  /**
   * 行列の成分毎に逆正接(2)を計算し、計算結果を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param a1 分子側の値を成分とする行列
   * @param a2 分母側の値を成分とする行列
   * @return 生成された行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] atan2ElementWise(final S[][] a1, final double[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (rowSize != rowSize2 || columnSize != columnSize2) {
      throw new MatrixSizeException(Messages.getString("NumericalMatrixUtil.4")); //$NON-NLS-1$
    }

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, a1);
    }

    S ans00 = a1[0][0].atan2(a2[0][0]);
    S[][] ans = ans00.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = a1[i][j].atan2(a2[i][j]);
      }
    }
    return ans;
  }

  /**
   * 成分毎に関数の計算をし、計算結果を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型」
   * 
   * @param matrix 対象となる行列
   * @param function 複素数関数(引数１個)
   * @return 計算結果を成分とする行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] elementWiseFunction(final S[][] matrix, final NumericalScalarFunction<S, M> function) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
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
   * 成分毎に関数の計算をし、計算結果を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix1 対象となる行列
   * @param matrix2 対象となる行列
   * @param function 関数(引数２個)
   * @return 計算結果を成分とする行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] elementWiseFunction(final S[][] matrix1, final S[][] matrix2,
      final NumericalScalarFunctionWithTwoArguments<S, M> function) {
    int rowSize = matrix1.length;
    int columnSize = rowSize == 0 ? 0 : matrix1[0].length;
    int rowSize2 = matrix2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : matrix2[0].length;

    if (rowSize != rowSize2 || columnSize != columnSize2) {
      throw new MatrixSizeException(Messages.getString("NumericalMatrixUtil.6")); //$NON-NLS-1$
    }

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix1);
    }

    S ans00 = function.evaluate(matrix1[0][0], matrix2[0][0]);
    S[][] ans = ans00.createArray(rowSize, columnSize);
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = function.evaluate(matrix1[i][j], matrix2[i][j]);
      }
    }
    return ans;
  }

  /**
   * 成分毎に関数の計算をし、計算結果を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix1 対象となる行列
   * @param value 対象となる値
   * @param function 関数(引数２個)
   * @return 計算結果を成分とする行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] elementWiseFunction(final S[][] matrix1, final S value,
      final NumericalScalarFunctionWithTwoArguments<S, M> function) {
    int rowSize = matrix1.length;
    int columnSize = rowSize == 0 ? 0 : matrix1[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix1);
    }

    S ans00 = function.evaluate(matrix1[0][0], value);
    S[][] ans = ans00.createArray(rowSize, columnSize);
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = function.evaluate(matrix1[i][j], value);
      }
    }
    return ans;
  }

  /**
   * 行列のノルムを返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param type ノルムの種類(NormType.ONE:1ノルム、NormType.TWO:2ノルム(最大特異値))
   * @return 行列のノルム
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S norm(final S[][] matrix, final NormType type) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      throw new MatrixSizeException("Empty matrix"); //$NON-NLS-1$
    }

    S scalar = matrix[0][0].abs();

    if (type == NormType.ONE) {
      S maxsum = scalar.createZero();
      for (int i = 0; i < rowSize; i++) {
        S sum = scalar.createZero();
        for (int j = 0; j < rowSize; j++) {
          sum = sum.add(matrix[j][i].abs());
        }
        if (sum.isGreaterThan(maxsum)) {
          maxsum = sum;
        }
      }
      return maxsum;
    }

    if (type == NormType.TWO) {
      return new RealSingularValueDecomposer<S, M>().norm(matrix);
    }

    throw new IllegalArgumentException(Messages.getString("NumericalMatrixUtil.7")); //$NON-NLS-1$
  }

//  /**
//   * 複素行列の成分を返します。
//   * 
//   * @param <S> スカラーの型
//   * @param <M> 行列の型
//   * 
//   * @param rePart 実部
//   * @param imPart 虚部
//   * @return 複素行列の成分
//   */
//  public static <RS extends NumericalScalar<RS, RM>, RM extends BaseNumericalMatrix<RS, RM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends BaseNumericalComplexMatrix<RS,RM,CS,CM>> CS[][] createComplexArray(final RS[][] rePart, final RS[][] imPart) {
//    int rowSize = rePart.length;
//    int columnSize = rowSize == 0 ? 0 : rePart[0].length;
//    int rowSize2 = imPart.length;
//    int columnSize2 = rowSize2 == 0 ? 0 : imPart[0].length;
//
//    if (rowSize != rowSize2 || columnSize != columnSize2) {
//      throw new MatrixSizeException(Messages.getString("NumericalMatrixUtil.8")); //$NON-NLS-1$
//    }
//
//    final CS scalar = new BaseComplexNumericalScalar<>(rePart[0][0], imPart[0][0]);
//
//    final CS[][] ans = scalar.createArray(rowSize, columnSize);
//    for (int row = 0; row < rowSize; row++) {
//      for (int column = 0; column < columnSize; column++) {
//        ans[row][column] = new BaseComplexNumericalScalar<>(rePart[row][column], imPart[row][column]);
//      }
//    }
//
//    return ans;
//  }

//  /**
//   * 複素行列の成分を返します。
//   * 
//   * @param <S> スカラーの型
//   * @param <M> 行列の型
//   * 
//   * @param rePart 実部
//   * @return 複素行列の成分
//   */
//  public static <S extends NumericalScalar<S, M>, M extends BaseNumericalMatrix<S, M>> BaseComplexNumericalScalar<S, M>[][] createComplexArray(final S[][] rePart) {
//    int rowSize = rePart.length;
//    int columnSize = rowSize == 0 ? 0 : rePart[0].length;
//
//    final BaseComplexNumericalScalar<S, M> scalar = new BaseComplexNumericalScalar<>(rePart[0][0], rePart[0][0].createZero());
//
//    final BaseComplexNumericalScalar<S, M>[][] ans = scalar.createArray(rowSize, columnSize);
//    for (int row = 0; row < rowSize; row++) {
//      for (int column = 0; column < columnSize; column++) {
//        ans[row][column] = new BaseComplexNumericalScalar<>(rePart[row][column], rePart[row][column].createZero());
//      }
//    }
//
//    return ans;
//  }

//  /**
//   * 複素ベクトルの成分を返します。
//   * 
//   * @param <S> スカラーの型
//   * @param <M> 行列の型
//   * 
//   * @param rePart 実部
//   * @param imPart 虚部
//   * @return 複素ベクトルの成分
//   */
//  public static <S extends NumericalScalar<S, M>, M extends BaseNumericalMatrix<S, M>> BaseComplexNumericalScalar<S, M>[] createComplexArray(final S[] rePart, final S[] imPart) {
//    int size1 = rePart.length;
//    int size2 = imPart.length;
//
//    if (size1 != size2) {
//      throw new MatrixSizeException(Messages.getString("NumericalMatrixUtil.9")); //$NON-NLS-1$
//    }
//    BaseComplexNumericalScalar<S, M> scalar = new BaseComplexNumericalScalar<>(rePart[0], imPart[0]);
//
//    BaseComplexNumericalScalar<S, M>[] ans = scalar.createArray(size1);
//    for (int i = 0; i < size1; i++) {
//      ans[i] = new BaseComplexNumericalScalar<>(rePart[i], imPart[i]);
//    }
//
//    return ans;
//  }

  /**
   * 実部ベクトルの成分を返します。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * @param elements 成分
   * @return 実部ベクトルの成分
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> RS[] getRealPartElements(
      CS[] elements) {
    final int size = elements.length;

    final RS[] ans = elements[0].getRealPart().createArray(size);

    for (int i = 0; i < size; i++) {
      ans[i] = elements[i].getRealPart();
    }

    return ans;
  }

//  /**
//   * 複素ベクトルの成分を返します。
//   * 
//   * @param <S> スカラーの型
//   * @param <M> 行列の型
//   * 
//   * @param rePart 実部
//   * @return 複素ベクトルの成分
//   */
//  public static <S extends NumericalScalar<S, M>, M extends BaseNumericalMatrix<S, M>> BaseComplexNumericalScalar<S, M>[] createComplexArray(final S[] rePart) {
//    int size1 = rePart.length;
//
//    BaseComplexNumericalScalar<S, M> scalar = new BaseComplexNumericalScalar<>(rePart[0], rePart[0].createZero());
//
//    BaseComplexNumericalScalar<S, M>[] ans = scalar.createArray(size1);
//    for (int i = 0; i < size1; i++) {
//      ans[i] = new BaseComplexNumericalScalar<>(rePart[i], rePart[i].createZero());
//    }
//
//    return ans;
//  }

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
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> boolean equals(final S[][] a1, final S[][] a2, final S tolerance) {
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
   * 絶対値が小さい成分を0に丸めます。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param tolerance 許容誤差
   * @return 丸められた結果
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] roundToZeroElementWise(final S[][] matrix, final S tolerance) {
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
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 調べる行列
   * @param tolerance 許容誤差
   * @return 零行列ならばtrue、そうでなければfalse
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> boolean isZero(final S[][] matrix, final S tolerance) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    for (int i = 0; i < rowSize; i++) {
      S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        if (matrixi[j].isZero(tolerance) == false) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * 単位行列であるか判定します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrix 調べる行列
   * @param tolerance 許容誤差
   * @return 単位行列ならばtrue、そうでなければfalse
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> boolean isUnit(final S[][] matrix, final S tolerance) {
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
          if (matrixi[j].isUnit(tolerance) == false) {
            return false;
          }
        } else {
          if (matrixi[j].isZero(tolerance) == false) {
            return false;
          }
        }
      }
    }

    return true;
  }

  /**
   * 0〜1の範囲の一様分布の乱数を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param matrix 対象となる行列
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 一様分布の乱数を成分とする行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] createUniformRandom(final S[][] matrix, final int rowSize, final int columnSize) {
    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    RandomGenerator<S, M> random = matrix[0][0].createUniformRandomGenerator();
    S[][] ans = matrix[0][0].createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      final S[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = random.nextValue();
      }
    }

    return ans;
  }

  /**
   * 0〜1の範囲の一様分布の乱数を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param matrix 対象となる行列
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param seed 乱数の種
   * @return 一様分布の乱数を成分とする行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] createUniformRandom(final S[][] matrix, final int rowSize, final int columnSize, final long seed) {
    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    final RandomGenerator<S, M> random = matrix[0][0].createUniformRandomGenerator();
    random.setSeed(seed);
    final S[][] ans = matrix[0][0].createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      final S[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = random.nextValue();
      }
    }

    return ans;
  }

  /**
   * 平均0、分散1の正規分布の乱数を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param matrix 対象となる行列
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 平均0、分散1の正規分布の乱数を成分とする行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] createNormalRandom(final S[][] matrix, final int rowSize, final int columnSize) {
    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    final NormalRandom<S, M> random = new NormalRandom<>(matrix[0][0].createUniformRandomGenerator());
    final S[][] ans = matrix[0][0].createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      final S[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = random.nextValue();
      }
    }

    return ans;
  }

  /**
   * 平均0、分散1の正規分布の乱数を成分とする行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param matrix 対象となる行列
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param seed 乱数の種
   * @return 平均0、分散1の正規分布の乱数を成分とする行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][] createNormalRandom(final S[][] matrix, final int rowSize, final int columnSize, final long seed) {
    if (rowSize == 0 || columnSize == 0) {
      return GridUtil.<S> createArray(rowSize, columnSize, matrix);
    }

    final NormalRandom<S, M> random = new NormalRandom<>(matrix[0][0].createUniformRandomGenerator());
    random.setSeed(seed);
    final S[][] ans = matrix[0][0].createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      final S[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = random.nextValue();
      }
    }

    return ans;
  }

  /**
   * 複素行列と複素行列の積を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param aRe 第一行列の実部
   * @param aIm 第一行列の虚部
   * @param bRe 第二行列の実部
   * @param bIm 第二行列の虚部
   * @return 複素行列と複素行列の積
   */
  @SuppressWarnings("unchecked")
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> S[][][] multiply(final S[][] aRe, final S[][] aIm, final S[][] bRe, final S[][] bIm) {
    int rowSize1 = aRe.length;
    int columnlSize1 = rowSize1 == 0 ? 0 : aRe[0].length;
    int rowSize2 = bRe.length;
    int columnSize2 = rowSize2 == 0 ? 0 : bRe[0].length;

    S unit = aRe[0][0].createUnit();

    S[][] cRe = unit.createArray(rowSize1, columnSize2);
    S[][] cIm = unit.createArray(rowSize1, columnSize2);
    for (int i = 0; i < rowSize1; i++) {
      for (int j = 0; j < columnSize2; j++) {
        S dr = unit.createZero();
        S di = unit.createZero();
        for (int k = 0; k < columnlSize1; k++) {
          dr = dr.add(aRe[i][k].multiply(bRe[k][j])).subtract(aIm[i][k].multiply(bIm[k][j]));
          di = di.add(aRe[i][k].multiply(bIm[k][j])).add(aIm[i][k].multiply(bRe[k][j]));
        }
        cRe[i][j] = dr;
        cIm[i][j] = di;
      }
    }
    return (S[][][])new NumericalScalar[][][] {cRe, cIm};
  }

  /**
   * 実部の2次元配列を返します。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * 
   * @param matrix 対象となる行列
   * @return 実部の2次元配列
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> RS[][] getRealPartElements(final CS[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final RS[][] ans = matrix[0][0].getRealPart().createArray(rowSize, columnSize);

    for (int row = 0; row < rowSize; row++) {
      for (int column = 0; column < columnSize; column++) {
        ans[row][column] = matrix[row][column].getRealPart();
      }
    }
    return ans;
  }

  /**
   * 虚部ベクトルの成分を返します。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * @param elements 成分
   * @return 虚部ベクトルの成分
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> RS[] getImaginaryPartElements(CS[] elements) {
    final int size = elements.length;

    final RS[] ans = elements[0].getImaginaryPart().createArray(size);

    for (int i = 0; i < size; i++) {
      ans[i] = elements[i].getImaginaryPart();
    }

    return ans;
  }

  /**
   * 虚部の2次元配列を返します。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * 
   * @param matrix 対象となる行列
   * @return 虚部の2次元配列
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> RS[][] getImaginaryPartElements(final CS[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final RS[][] ans = matrix[0][0].getImaginaryPart().createArray(rowSize, columnSize);

    for (int row = 0; row < rowSize; row++) {
      for (int column = 0; column < columnSize; column++) {
        ans[row][column] = matrix[row][column].getImaginaryPart();
      }
    }
    return ans;
  }

  /**
   * 実部を設定します。
   * 
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
   * 
   * @param elements 対象となる行列
   * @param realPart 変更値
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> void setRealPartElements(final CS[] elements, final int[] realPart) {
    int size = elements.length;

    for (int i = 0; i < size; i++) {
      elements[i].setRealPart(realPart[i]);
    }
  }

  /**
   * 実部を設定します。
   * 
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param realPart 変更値
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> void setRealPartElements(final CS[][] matrix, final int[][] realPart) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j].setRealPart(realPart[i][j]);
      }
    }
  }

  /**
   * 虚部を設定します。
   * 
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
   * 
   * @param elements 対象となる行列
   * @param imagPart 変更値
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> void setImagPartElements(
      final CS[] elements, final int[] imagPart) {
    int size = elements.length;
    for (int i = 0; i < size; i++) {
      elements[i].setImaginaryPart(imagPart[i]);
    }
  }

  /**
   * 虚部を設定します。
   * 
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
   * 
   * @param elements 対象となる行列
   * @param imagPart 変更値
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> void setImagPartElements(final CS[][] elements, final int[][] imagPart) {
    int rowSize = elements.length;
    int columnSize = rowSize == 0 ? 0 : elements[0].length;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        elements[i][j].setImaginaryPart(imagPart[i][j]);
      }
    }
  }

  /**
   * 実部を設定します。
   * 
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
   * 
   * @param elements 対象となる行列
   * @param realPart 変更値
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> void setRealPartElements(final CS[] elements, final double[] realPart) {
    int size = elements.length;
    for (int i = 0; i < size; i++) {
      elements[i].setRealPart(realPart[i]);
    }
  }

  /**
   * 実部を設定します。
   * 
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param realPart 変更値
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> void setRealPartElements(final CS[][] matrix, final double[][] realPart) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j].setRealPart(realPart[i][j]);
      }
    }
  }

  /**
   * 虚部を設定します。
   * 
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
   * 
   * @param elements 対象となる行列
   * @param imagPart 変更値
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> void setImagPartElements(final CS[] elements, final double[] imagPart) {
    int rowSize = elements.length;
    for (int i = 0; i < rowSize; i++) {
      elements[i].setImaginaryPart(imagPart[i]);
    }
  }

  /**
   * 虚部を設定します。
   * 
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param imagPart 変更値
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> void setImagPartElements(final CS[][] matrix, final double[][] imagPart) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j].setImaginaryPart(imagPart[i][j]);
      }
    }
  }

  /**
   * 実部を設定します。
   * 
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
   * 
   * @param elements 対象となる行列
   * @param realPart 変更値
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> void setRealPartElements(final CS[] elements, final RS[] realPart) {
    int size = elements.length;
    for (int i = 0; i < size; i++) {
      elements[i].setRealPart(realPart[i]);
    }
  }

  /**
   * 実部を設定します。
   * 
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param realPart 変更値
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> void setRealPartElements(final CS[][] matrix, final RS[][] realPart) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j].setRealPart(realPart[i][j]);
      }
    }
  }

  /**
   * 実部を設定します。
   * 
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param realPart 変更値
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends AbstractNumericalRealMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> void setRealPartElements(final CM matrix, final RM realPart) {
    setRealPartElements(matrix.getElements(), realPart.getElements());
  }

  /**
   * 虚部を設定します。
   * 
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
   * 
   * @param matrix 対象となる行列
   * @param imagPart 変更値
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends AbstractNumericalRealMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> void setImagPartElements(final CM matrix, final RM imagPart) {
    setImagPartElements(matrix.getElements(), imagPart.getElements());
  }
  
  /**
   * 虚部を設定します。
   * 
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
   * 
   * @param elements 対象となる行列
   * @param imagPart 変更値
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> void setImagPartElements(final CS[] elements, final RS[] imagPart) {
    int size = elements.length;
    for (int i = 0; i < size; i++) {
      elements[i].setImaginaryPart(imagPart[i]);
    }
  }


  /**
   * 虚部を設定します。
   * 
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
   * 
   * @param elements 対象となる行列
   * @param imagPart 変更値
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> void setImagPartElements(final CS[][] elements, final RS[][] imagPart) {
    int rowSize = elements.length;
    int columnSize = rowSize == 0 ? 0 : elements[0].length;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        elements[i][j].setImaginaryPart(imagPart[i][j]);
      }
    }
  }

  /**
   * 複素配列を生成します。
   * 
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
   * 
   * @param matrix 対象となる行列
   * @return 複素配列 
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends AbstractNumericalRealMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> CS[][] createComplexArray(final RM matrix) {
    return matrix.getElement(1, 1).createComplexArray(matrix.getElements());
  }

  /**
   * 複素配列を生成します。
   * 
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
   * 
   * @param realPart 実部
   * @param imagPart 虚部
   * @return 複素配列 
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends AbstractNumericalRealMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> CS[][] createComplexArray(final RM realPart, final RM imagPart) {
    return realPart.getElement(1, 1).createComplexArray(realPart.getElements(), imagPart.getElements());
  }

}
