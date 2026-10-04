/*
 * $Id: GridUtil.java,v 1.17 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.matrix;

import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Array;


/**
 * グリッド{@link Grid}のユーティリティクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.17 $
 */
public final class GridUtil {

  /**
   * 新しく生成された<code>GridUtil</code>オブジェクトを初期化します。
   */
  private GridUtil() {
    // nothing to do
  }

  /**
   * グリッドの複製を生成します。
   * 
   * @param <S> 成分の型
   * @param grid 複製の元となるグリッド
   * @return 複製されたグリッド
   */
  public static <S extends GridElement<S>> S[][] clone(final S[][] grid) {
    final int rowSize = grid.length;
    final int columnSize = rowSize == 0 ? 0 : grid[0].length;
    final S[][] ans = GridUtil.<S> createArray(rowSize, columnSize, grid);

    for (int i = 0; i < rowSize; i++) {
      final S[] ansi = ans[i];
      final S[] gridi = grid[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = gridi[j].clone();
      }
    }
    return ans;
  }

  /**
   * グリッドの複製を生成します。
   * 
   * @param <S> 成分の型
   * @param grid 複製の元となるグリッド
   * @return 複製されたグリッド
   */
  public static <S extends GridElement<S>> S[][] createSameClassArray(final S[][] grid) {
    int rowSize = grid.length;
    int columnSize = rowSize == 0 ? 0 : grid[0].length;
    S[][] ans = GridUtil.<S> createArray(rowSize, columnSize, grid);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      S[] gridi = grid[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = gridi[j];
      }
    }
    return ans;
  }

  /**
   * グリッドの複製を生成します。
   * 
   * @param <S> 成分の型
   * @param grid 複製の元となるグリッド
   * @return 複製されたグリッド
   */
  public static <S extends GridElement<S>> S[] clone(final S[] grid) {
    int rowSize = grid.length;
    S[] ans = grid[0].createArray(rowSize);

    for (int i = 0; i < rowSize; i++) {
      ans[i] = grid[i].clone();
    }
    return ans;
  }

  /**
   * <code>row1</code>行と<code>row2</code>行を入れ替えます。
   * 
   * @param <S> 成分の型
   * @param grid 対象のグリッド
   * @param row1 行番号1
   * @param row2 行番号2
   */
  public static <S extends GridElement<S>> void exchangeRow(final S[][] grid, final int row1, final int row2) {
    int rowSize = grid.length;
    int columnSize = rowSize == 0 ? 0 : grid[0].length;

    S[] elements1 = grid[row1];
    S[] elements2 = grid[row2];

    for (int i = 0; i < columnSize; i++) {
      S tmp = elements1[i];
      elements1[i] = elements2[i];
      elements2[i] = tmp;
    }
  }

  /**
   * <code>column1</code>列と<code>column2</code>列を入れ替えます。
   * 
   * @param <S> 成分の型
   * @param grid 対象のグリッド
   * @param column1 列番号1
   * @param column2 列番号2
   */
  public static <S extends GridElement<S>> void exchangeColumn(final S[][] grid, final int column1, final int column2) {
    int rowSize = grid.length;

    for (int i = 0; i < rowSize; i++) {
      S tmp = grid[i][column1];
      grid[i][column1] = grid[i][column2];
      grid[i][column2] = tmp;
    }
  }

  /**
   * 2個のグリッドの成分が全て等しか判定します。
   * 
   * @param <S> スカラーの型
   * 
   * @param a1 第一グリッド
   * @param a2 第二グリッド
   * @return グリッドの成分が等しければtrue、そうでなければfalseを返します。
   */
  public static <S extends GridElement<S>> boolean equals(final S[][] a1, final S[][] a2) {
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
        if (a1i[j] == null && a2i[j] == null) {
          continue;
        }
        if (!a1i[j].equals(a2i[j])) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * グリッド<code>source</code>の各成分をグリッド<code>destination</code>の各成分にコピーします。
   * 
   * @param <S> スカラーの型
   * @param source コピー元グリッド
   * @param destination コピー先グリッド
   */
  public static <S extends GridElement<S>> void copy(final S[][] source, final S[][] destination) {
    int rowSizeFrom = source.length;
    int columnSizeFrom = rowSizeFrom == 0 ? 0 : source[0].length;
    int rowSizeTo = destination.length;
    int columnSizeTo = rowSizeTo == 0 ? 0 : destination[0].length;

    if (rowSizeTo != rowSizeFrom || columnSizeTo != columnSizeFrom) {
      throw new MatrixSizeException(Messages.getString("GridUtil.0")); //$NON-NLS-1$
    }

    for (int i = 0; i < rowSizeFrom; i++) {
      S[] toi = destination[i];
      S[] fromi = source[i];
      for (int j = 0; j < columnSizeFrom; j++) {
        toi[j] = fromi[j].clone();
        //toi[j] = destination[0][0].transformFrom(fromi[j]);
      }
    }
  }

  /**
   * グリッド<code>source</code>の各成分をグリッド<code>destination</code>の各成分にコピーします。
   * 
   * @param <S> スカラーの型
   * @param source コピー元グリッド
   * @param destination コピー先グリッド
   */
  public static <S extends GridElement<S>> void copy(final S[] source, final S[] destination) {
    int rowSizeFrom = source.length;
    int rowSizeTo = destination.length;

    if (rowSizeTo != rowSizeFrom) {
      throw new MatrixSizeException(Messages.getString("GridUtil.1")); //$NON-NLS-1$
    }

    for (int i = 0; i < rowSizeFrom; i++) {
      //destination[i] = destination[0].transformFrom(source[i]);
      destination[i] = source[i].clone();
    }
  }

  /**
   * 2個のグリッドを縦に接続したグリッドを生成します。
   * 
   * @param <S> 成分の型
   * @param a1 上側のグリッド
   * @param a2 下側のグリッド
   * @return 接続されたグリッド
   */
  public static <S extends GridElement<S>> S[][] appendDown(final S[][] a1, final S[][] a2) {
    int rowSize1 = a1.length;
    int columnSize1 = rowSize1 == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (columnSize1 != columnSize2 && rowSize1 != 0 && rowSize2 != 0) {
      throw new MatrixSizeException(Messages.getString("GridUtil.2")); //$NON-NLS-1$
    }

    int rowSize = rowSize1 + rowSize2;
    int columnSize;
    if (columnSize1 == 0) {
      columnSize = columnSize2;
    } else {
      columnSize = columnSize1;
    }

    S[][] ans = GridUtil.<S> createArray(rowSize, columnSize, a1);

    for (int j = 0; j < columnSize; j++) {
      for (int i = 0; i < rowSize1; i++) {
        ans[i][j] = a1[i][j].clone();
      }
      for (int i = 0; i < rowSize2; i++) {
        ans[i + rowSize1][j] = a2[i][j].clone();
      }
    }
    return ans;
  }

  /**
   * 2個のグリッドを横に接続したグリッドを生成します。
   * 
   * @param <S> 成分の型
   * @param a1 左側のグリッド
   * @param a2 右側のグリッド
   * @return 接続されたグリッド
   */
  public static <S extends GridElement<S>> S[][] appendRight(final S[][] a1, final S[][] a2) {
    int rowSize1 = a1.length;
    int columnSize1 = rowSize1 == 0 ? 0 : a1[0].length;
    int rowSize2 = a2.length;
    int columnSize2 = rowSize2 == 0 ? 0 : a2[0].length;

    if (rowSize1 != rowSize2 && rowSize1 != 0 && rowSize2 != 0) {
      throw new MatrixSizeException(Messages.getString("GridUtil.3")); //$NON-NLS-1$
    }

    int columnSize = columnSize1 + columnSize2;
    int rowSize;
    if (rowSize1 == 0) {
      rowSize = rowSize2;
    } else {
      rowSize = rowSize1;
    }

    S[][] ans = GridUtil.<S> createArray(rowSize, columnSize, a1);

    for (int i = 0; i < rowSize1; i++) {
      S[] ansi = ans[i];
      S[] a1i = a1[i];
      for (int j = 0; j < columnSize1; j++) {
        ansi[j] = a1i[j].clone();
      }
    }

    for (int i = 0; i < rowSize2; i++) {
      S[] ansi = ans[i];
      S[] a2i = a2[i];
      for (int j = 0; j < columnSize2; j++) {
        ansi[j + columnSize1] = a2i[j].clone();
      }
    }

    return ans;
  }

  /**
   * 転置グリッドを生成します。
   * 
   * @param <S> 成分の型
   * @param grid 元のグリッド
   * @return 転置グリッド
   */
  public static <S extends GridElement<S>> S[][] transpose(final S[] grid) {
    int rowSize = 1;
    int columnSize = grid.length;
    S[][] ans = GridUtil.<S> createArray(columnSize, rowSize, grid);

    for (int i = 0; i < columnSize; i++) {
      ans[i][0] = grid[i].clone();
    }
    return ans;
  }

  /**
   * 転置グリッドを生成します。
   * 
   * @param <S> 成分の型
   * @param grid 元のグリッド
   * @return 転置グリッド
   */
  public static <S extends GridElement<S>> S[][] transpose(final S[][] grid) {
    int rowSize = grid.length;
    int columnSize = rowSize == 0 ? 0 : grid[0].length;
    S[][] ans = GridUtil.<S> createArray(columnSize, rowSize, grid);

    for (int i = 0; i < rowSize; i++) {
      S[] gridi = grid[i];
      for (int j = 0; j < columnSize; j++) {
        ans[j][i] = gridi[j].clone();
      }
    }
    return ans;
  }

  /**
   * 転置行列の成分を設定します。
   * 
   * @param <S> 成分の型
   * @param matrix 元の行列
   * @param result 転置行列の成分を代入する行列
   */
  public static <S extends GridElement<S>> void transpose(final S[][] matrix, final S[][] result) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    for (int i = 0; i < rowSize; i++) {
      final S[] m2i = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        result[j][i] = m2i[j].clone();
      }
    }
  }

  /**
   * 指定された列を削除したグリッドを生成します。
   * 
   * @param <S> 成分の型
   * @param grid 対象となるグリッド
   * @param min 開始列
   * @param max 終了列
   * @return 列を削除されたグリッド
   */
  public static <S extends GridElement<S>> S[][] removeColumnVectors(final S[][] grid, final int min, final int max) {
    int rowSize = grid.length;
    int columnSize = rowSize == 0 ? 0 : grid[0].length;
    int newColumnSize = columnSize - (max - min + 1);
    S[][] ans = GridUtil.<S> createArray(rowSize, newColumnSize, grid);

    for (int i = 0; i < rowSize; i++) {
      S[] gridi = grid[i];
      S[] ansi = ans[i];
      for (int j = 0, j2 = 0; j < columnSize; j++) {
        if (min <= j && j <= max) {
          continue;
        }
        ansi[j2] = gridi[j].clone();
        j2++;
      }
    }

    return ans;
  }

  /**
   * 指定された列を削除したグリッドを生成します。
   * 
   * @param <S> 成分の型
   * @param grid 元のグリッド
   * @param removingIndex 削除する列の番号
   * @return 列を削除されたグリッド
   */
  public static <S extends GridElement<S>> S[][] removeColumnVectors(final S[][] grid, final int[] removingIndex) {
    int rowSize = grid.length;
    int columnSize = rowSize == 0 ? 0 : grid[0].length;

    int columnSize2 = removingIndex.length;

    // 削除すべき列番号にtrue
    boolean[] removedColumn = new boolean[columnSize];
    for (int i = 0; i < columnSize2; i++) {
      removedColumn[removingIndex[i]] = true;
    }

    // 新しい列の数
    int newColumnSize = 0;
    for (int i = 0; i < columnSize; i++) {
      if (removedColumn[i]) {
        continue;
      }
      newColumnSize++;
    }

    S[][] ans = GridUtil.<S> createArray(rowSize, newColumnSize, grid);
    for (int i = 0; i < rowSize; i++) {
      S[] gridi = grid[i];
      S[] ansi = ans[i];
      for (int j = 0, j2 = 0; j < columnSize; j++) {
        if (removedColumn[j]) {
          continue;
        }
        // ansi[j2] = (GridElement)(gridi[j].clone());
        ansi[j2] = gridi[j].clone();
        //ansi[j2] = (S)gridi[j].transformTo(gridi[j]);
        j2++;
      }
    }

    return ans;
  }

  /**
   * 指定された行を削除したグリッドを生成します。
   * 
   * @param <S> 成分の型
   * @param grid 対象となるグリッド
   * @param min 開始行
   * @param max 終了行
   * @return 行を削除されたグリッド
   */
  public static <S extends GridElement<S>> S[][] removeRowVectors(final S[][] grid, final int min, final int max) {
    int rowSize = grid.length;
    int columnSize = rowSize == 0 ? 0 : grid[0].length;
    int newRowSize = rowSize - (max - min + 1);

    S[][] ans = GridUtil.<S> createArray(newRowSize, columnSize, grid);

    for (int i = 0, i2 = 0; i < rowSize; i++) {
      if (min <= i && i <= max) {
        continue;
      }
      S[] gridi = grid[i];
      S[] ansi = ans[i2];
      for (int j = 0; j < columnSize; j++) {
        //ansi[j] = (S)gridi[j].transformTo(gridi[j]);
        ansi[j] = gridi[j].clone();
      }
      i2++;
    }

    return ans;
  }

  /**
   * 指定された行を削除したグリッドを生成します。
   * 
   * @param <S> 成分の型
   * @param grid 元のグリッド
   * @param removingIndex 削除する行の番号
   * @return 行を削除されたグリッド
   */
  public static <S extends GridElement<S>> S[][] removeRowVectors(final S[][] grid, final int[] removingIndex) {
    int rowSize = grid.length;
    int columnSize = rowSize == 0 ? 0 : grid[0].length;

    int size = removingIndex.length;

    // 削除すべき行番号にtrue
    boolean[] removedRow = new boolean[rowSize];
    for (int i = 0; i < size; i++) {
      removedRow[removingIndex[i]] = true;
    }

    // 新しい行の数
    int newRowSize = 0;
    for (int i = 0; i < rowSize; i++) {
      if (removedRow[i]) {
        continue;
      }
      newRowSize++;
    }

    S[][] ans = GridUtil.<S> createArray(newRowSize, columnSize, grid);

    for (int i = 0, i2 = 0; i < rowSize; i++) {
      if (removedRow[i]) {
        continue;
      }
      S[] gridi = grid[i];
      S[] ansi = ans[i2];
      for (int j = 0; j < columnSize; j++) {
        // ansi[j] = (GridElement)(gridi[j].clone());
        //ansi[j] = (S)gridi[j].transformTo(gridi[j]);
        ansi[j] = gridi[j].clone();
      }
      i2++;
    }

    return ans;
  }

  /**
   * 部分グリッドを生成します。
   * 
   * @param <S> 成分の型
   * @param grid 対象となるグリッド
   * @param rowMin 始端行の数(0から始まる)
   * @param rowMax 終端行の数(0から始まる)
   * @param columnMin 始端列の数(0から始まる)
   * @param columnMax 終端列の数(0から始まる)
   * @return 部分行列
   */
  public static <S extends GridElement<S>> S[][] getSubMatrix(final S[][] grid, final int rowMin, final int rowMax, final int columnMin, final int columnMax) {
    int subRowSize = rowMax - rowMin + 1;
    int subColSize = columnMax - columnMin + 1;
    S[][] ans = GridUtil.<S> createArray(subRowSize, subColSize, grid);

    for (int i = 0; i < subRowSize; i++) {
      S[] ansi = ans[i];
      S[] gridi = grid[i + rowMin];
      for (int j = 0; j < subColSize; j++) {
        ansi[j] = gridi[j + columnMin].clone();
      }
    }
    return ans;
  }

  /**
   * 部分グリッドを生成します。
   * 
   * @param <S> 成分の型
   * @param grid 対象となるグリッド
   * @param index 該当する行の番号
   * @param column 列番号
   * @return 部分グリッド
   */
  public static <S extends GridElement<S>> S[][] getSubMatrix(final S[][] grid, final int[] index, final int column) {
    int rowSize = index.length;
    S[][] ans = GridUtil.<S> createArray(rowSize, 1, grid);

    for (int i = 0; i < rowSize; i++) {
      ans[i][0] = grid[index[i]][column].clone();
    }
    return ans;
  }

  /**
   * <code>columnMin</code>行目から<code>columnMax</code>行目までの成分の<code>rowIndex</code> で指定された列の部分グリッドを生成します。
   * 
   * @param <S> 成分の型
   * @param grid 対象となるグリッド
   * @param rowIndex 列指定ベクトル
   * @param columnMin 列指定
   * @param columnMax 列指定
   * @return 部分グリッド
   */
  public static <S extends GridElement<S>> S[][] getSubMatrix(final S[][] grid, final int[] rowIndex, final int columnMin, final int columnMax) {
    int rowSize = rowIndex.length;
    int columnSize = columnMax - columnMin + 1;
    S[][] ans = GridUtil.<S> createArray(rowSize, columnSize, grid);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      S[] gridi = grid[rowIndex[i]];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = gridi[j + columnMin].clone();
      }
    }

    return ans;
  }

  /**
   * <code>row</code>列目の成分の<code>columnIndex</code>で指定された行ベクトルを生成します。
   * 
   * @param <S> 成分の型
   * @param grid 対象となるグリッド
   * @param row 行番号
   * @param columnIndex 列指定ベクトル
   * @return 部分グリッド
   */
  public static <S extends GridElement<S>> S[][] getSubMatrix(final S[][] grid, final int row, final int[] columnIndex) {
    int columnSize = columnIndex.length;
    S[][] ans = GridUtil.<S> createArray(1, columnSize, grid);

    S[] ans0 = ans[0];
    S[] gridRow = grid[row];
    for (int i = 0; i < columnSize; i++) {
      ans0[i] = gridRow[columnIndex[i]].clone();
    }

    return ans;
  }

  /**
   * <code>rowMin</code>列から<code>rowMax</code>列目の成分の<code>columnIndex</code> で指定された行の部分グリッドを生成します。
   * 
   * @param <S> 成分の型
   * @param grid 対象となるグリッド
   * @param rowMin 開始行
   * @param rowMax 終了行
   * @param columnIndex 列指定ベクトル
   * @return 部分グリッド
   */
  public static <S extends GridElement<S>> S[][] getSubMatrix(final S[][] grid, final int rowMin, final int rowMax, final int[] columnIndex) {
    int rowSize = rowMax - rowMin + 1;
    int columnSize = columnIndex.length;
    S[][] ans = GridUtil.<S> createArray(rowSize, columnSize, grid);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      S[] gridi = grid[rowMin + i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = gridi[columnIndex[j]].clone();
      }
    }

    return ans;
  }

  /**
   * 部分グリッドを生成します。
   * 
   * @param <S> 成分の型
   * @param grid 元のグリッド
   * @param rowIndex 該当する行の番号
   * @param columnIndex 該当する列の番号
   * @return 部分グリッド
   */
  public static <S extends GridElement<S>> S[][] getSubMatrix(final S[][] grid, final int[] rowIndex, final int[] columnIndex) {
    int rowSize = rowIndex.length;
    int columnSize = columnIndex.length;
    S[][] ans = GridUtil.<S> createArray(rowSize, columnSize, grid);

    for (int i = 0; i < rowSize; i++) {
      S[] ansi = ans[i];
      S[] gridi = grid[rowIndex[i]];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = gridi[columnIndex[j]].clone();
      }
    }
    return ans;
  }

  /**
   * 部分ベクトルを生成します。
   * 
   * @param <S> 成分の型
   * @param grid 元のベクトル
   * @param index 該当する行の番号
   * @return 部分ベクトル
   */
  public static <S extends GridElement<S>> S[] getSubVector(final S[] grid, final int[] index) {
    if (index.length == 0) {
      return GridUtil.<S> createArray(0, grid);
    }

    int size = index.length;
    S[] ans = GridUtil.<S> createArray(size, grid);

    for (int i = 0; i < size; i++) {
      ans[i] = grid[index[i]].clone();
    }
    return ans;
  }

  /**
   * 対角成分を取り出し列ベクトルとして返します。
   * 
   * @param <S> 成分の型
   * @param grid 対象となるグリッド
   * @return 対角成分からなる縦ベクトル
   */
  public static <S extends GridElement<S>> S[] diagonalToVector(final S[][] grid) {
    int rowSize = grid.length;
    int columnSize = rowSize == 0 ? 0 : grid[0].length;
    int size = Math.min(rowSize, columnSize);
    S[] ans = GridUtil.<S> createArray(size, grid);

    for (int i = 0; i < size; i++) {
      ans[i] = grid[i][i].clone();
    }
    return ans;
  }

  /**
   * ベクトルの成分を対角成分とする対角グリッドを生成します。
   * 
   * @param <S> 成分の型
   * @param vector 対象となるベクトル
   * @return 対角グリッド
   */
  public static <S extends GridElement<S>> S[][] vectorToDiagonal(final S[] vector) {
    int size = vector.length;

    S[][] ans = GridUtil.<S> createArray(size, size, vector);

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (i == j) {
          ans[i][j] = vector[i].clone();
        } else {
          ans[i][j] = vector[0].createZero();
        }
      }
    }
    return ans;
  }

  /**
   * 対角グリッドを生成します。
   * 
   * @param <S> 成分の型
   * @param vector 対角成分
   * @return 対角グリッド
   */
  public static <S extends GridElement<S>> S[][] diagonal(final S[] vector) {
    int size = vector.length;
    S[][] ans = GridUtil.<S> createArray(size, size, vector);

    for (int i = 0; i < size; i++) {
      S[] elementi = ans[i];
      for (int j = 0; j < size; j++) {
        if (i != j) {
          elementi[j] = vector[0].createZero();
        } else {
          elementi[j] = vector[i].clone();
        }
      }
    }
    return ans;
  }

  /**
   * グリッドの成分を変えずに、グリッドの大きさ(行の数と列の数)を変形します。
   * 
   * @param <S> 成分の型
   * @param grid 対象となるグリッド
   * @param newRowSize 変更後の行の数
   * @param newColumnSize 変更後の列の数
   * @return 変形したグリッド
   */
  public static <S extends GridElement<S>> S[][] reshape(final S[][] grid, final int newRowSize, final int newColumnSize) {
    int rowSize = grid.length;
    int columnSize = rowSize == 0 ? 0 : grid[0].length;
    S[][] ans = GridUtil.<S> createArray(newRowSize, newColumnSize, grid);

    int num = 0;
    for (int i = 0; i < newRowSize; i++) {
      S[] ansi = ans[i];
      for (int j = 0; j < newColumnSize; j++) {
        ansi[j] = grid[num / columnSize][num % columnSize].clone();
        num++;
      }
    }
    return ans;
  }

  /**
   * <code>newRowSize</code>*<code>newColSize</code>にサイズ変更します。 <p> {@link #reshape}とは異なり、成分位置の変更はせず, 自身より大きなサイズに変更する時は,零が埋められ、 自身より小さなサイズに変更する時は余分な成分は切り取られます。
   * 
   * @param <S> 成分の型
   * @param grid 対象となるグリッド
   * @param newRowSize 新しい行の数
   * @param newColumnSize 新しい列の数
   * @return サイズ変更後のグリッド
   */
  public static <S extends GridElement<S>> S[][] resize(final S[][] grid, final int newRowSize, final int newColumnSize) {
    int rowSize = grid.length;
    int columnSize = rowSize == 0 ? 0 : grid[0].length;
    S[][] ans = GridUtil.<S> createArray(newRowSize, newColumnSize, grid);

    int re = Math.min(newRowSize, rowSize);
    int ce = Math.min(newColumnSize, columnSize);

    // MatrixUtil.areaCopy(ans, 0, 0, matrix, 0, 0, re, ce);
    for (int i = 0; i < re; i++) {
      for (int j = 0; j < ce; j++) {
        ans[i][j] = grid[i][j].clone();
      }
      for (int j = ce; j < newColumnSize; j++) {
        ans[i][j] = grid[0][0].createZero();
      }
    }
    for (int i = re; i < newRowSize; i++) {
      for (int j = 0; j < ce; j++) {
        ans[i][j] = grid[0][0].createZero();
      }
      for (int j = ce; j < newColumnSize; j++) {
        ans[i][j] = grid[0][0].createZero();
      }
    }
    return ans;
  }

  /**
   * 2個のグリッドを成分毎にoperatorで指定された演算子で比較し, 計算結果を成分とするbooleanのグリッドを生成します。
   * 
   * @param <S> スカラーの型
   * 
   * @param a1 第一グリッド
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param a2 第二グリッド
   * @return 計算結果を成分とするbooleanのグリッド
   */
  public static <S extends GridElement<S>> boolean[][] compareElementWise(final S[][] a1, final String operator, final S[][] a2) {
    int rowSize = a1.length;
    int columnSize = rowSize == 0 ? 0 : a1[0].length;
    boolean[][] ans = new boolean[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      boolean[] ansi = ans[i];
      S[] a1i = a1[i];
      S[] a2i = a2[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = a1i[j].compare(operator, a2i[j]);
      }
    }
    return ans;
  }

  /**
   * <code>rowTo</code>行<code>columnTo</code>列を始点として、 グリッド<code>source</code>の <code>rowMin</code>行から<code>rowMax</code>行、 <code>columnMin</code>列から <code>columnMax</code>列までの値をコピーします。
   * 
   * @param <S> スカラーの型
   * 
   * @param destination コピー先のグリッド
   * @param rowTo 変更開始行
   * @param columnTo 変更開始列
   * @param source コピー元のグリッド
   * @param rowMin コピー開始行
   * @param rowMax コピー終了行
   * @param columnMin コピー開始列
   * @param columnMax コピー終了列
   */
  public static <S extends GridElement<S>> void setSubMatrix(final S[][] destination, final int rowTo, final int columnTo, final S[][] source, final int rowMin, final int rowMax, final int columnMin,
      final int columnMax) {
    int subRowSize = rowMax - rowMin;
    int subColumnSize = columnMax - columnMin;

    for (int i = 0; i <= subRowSize; i++) {
      S[] toi = destination[rowTo + i];
      S[] fromi = source[rowMin + i];
      for (int j = 0; j <= subColumnSize; j++) {
        toi[columnTo + j] = fromi[columnMin + j].clone();
        //toi[columnTo + j] = destination[0][0].transformFrom(fromi[columnMin + j]);
      }
    }
  }

  /**
   * 指定された場所に値を代入します。
   * 
   * @param <S> スカラーの型
   * @param destination 値を設定するグリッド
   * @param rowIndex 指定する行を含む指数
   * @param columnMin 列の始まり
   * @param columnMax 列の終り
   * @param source 代入するグリッド
   */
  public static <S extends GridElement<S>> void setSubMatrix(final S[][] destination, final int[] rowIndex, final int columnMin, final int columnMax, final S[][] source) {
    int idxcol = rowIndex.length;
    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;
    int columnSize = columnMax - columnMin + 1;

    if (mrow != idxcol || mcol != columnSize) {
      throw new MatrixSizeException(Messages.getString("GridUtil.4")); //$NON-NLS-1$
    }

    for (int i = 0; i < idxcol; i++) {
      for (int j = 0; j < columnSize; j++) {
        destination[rowIndex[i]][j + columnMin] = source[i][j].clone();
        //destination[rowIndex[i]][j + columnMin] = destination[0][0].transformFrom(source[i][j]);
      }
    }
  }

  /**
   * 指定された場所に値を代入します。
   * 
   * @param <S> スカラーの型
   * @param destination 値を代入するグリッド
   * @param rowMin 行の始まり
   * @param rowMax 行の終り
   * @param columnIndex 指定する列を含む指数
   * @param source 代入するグリッド
   */
  public static <S extends GridElement<S>> void setSubMatrix(final S[][] destination, final int rowMin, final int rowMax, final int[] columnIndex, final S[][] source) {
    int idxcol = columnIndex.length;
    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;
    int rowSize = rowMax - rowMin + 1;

    if (mrow != rowSize || mcol != idxcol) {
      throw new MatrixSizeException(Messages.getString("GridUtil.5")); //$NON-NLS-1$
    }

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < idxcol; j++) {
        destination[i + rowMin][columnIndex[j]] = source[i][j].clone();
        //destination[i + rowMin][columnIndex[j]] = destination[0][0].transformFrom(source[i][j]);
      }
    }
  }

  /**
   * 指定された場所に値を代入します。
   * 
   * @param <S> スカラーの型
   * @param destination 値を代入する行列
   * @param rowIndex 指定する行を含む指数
   * @param columnIndex 指定する列を含む指数
   * @param source 代入する行列
   */
  public static <S extends GridElement<S>> void setSubMatrix(final S[][] destination, final int[] rowIndex, final int[] columnIndex, final S[][] source) {
    int idxcol1 = rowIndex.length;
    int idxcol2 = columnIndex.length;

    int mrow = source.length;
    int mcol = mrow == 0 ? 0 : source[0].length;

    if (mrow != idxcol1 || mcol != idxcol2) {
      throw new MatrixSizeException(Messages.getString("GridUtil.6")); //$NON-NLS-1$
    }

    for (int i = 0; i < idxcol1; i++) {
      for (int j = 0; j < idxcol2; j++) {
        destination[rowIndex[i]][columnIndex[j]] = source[i][j].clone();
        //destination[rowIndex[i]][columnIndex[j]] = destination[0][0].transformFrom(source[i][j]);
      }
    }
  }

  /**
   * 配列<code>destination</code>の<code>to</code>行を始点として、 配列<code>source</code>の <code>min</code>行から<code>max</code>行までの値をコピーします。
   * 
   * @param <S> スカラーの型
   * @param destination コピー先
   * @param to 変更開始番号
   * @param source コピー元
   * @param min コピー開始番号
   * @param max コピー終了番号
   */
  public static <S extends GridElement<S>> void setSubVector(final S[] destination, final int to, final S[] source, final int min, final int max) {
    int subSize = max - min + 1;

    for (int i = 0; i < subSize; i++) {
      destination[to + i] = source[min + i].clone();
      //destination[to + i] = destination[0].transformFrom(source[min + i]);
    }
  }

  /**
   * 配列<code>destination</code>の<code>min</code>から<code>max</code>まで 配列 <code>source</code>の値をコピーします。
   * 
   * @param <S> スカラーの型
   * @param destination コピー先
   * @param min コピー先の開始番号
   * @param max コピー先の終了番号
   * @param source コピー元
   */
  public static <S extends GridElement<S>> void setSubVector(final S[] destination, final int min, final int max, final S[] source) {
    int size = max - min + 1;

    for (int i = 0; i < size; i++) {
      destination[i + min] = source[i].clone();
      //destination[i + min] = destination[0].transformFrom(source[i]);
    }
  }

  //  /**
  //   * 配列<code>destination</code>の<code>min</code>から<code>max</code>まで 配列 <code>source</code>の値をコピーします。
  //   * 
  //   * @param destination コピー先
  //   * @param min コピー先の開始番号
  //   * @param max コピー先の終了番号
  //   * @param source コピー元
  //   * @deprecated このメソッドは型の安全性が一切保証されていないため利用すべきではありません。AndroidのDalvik VMで実行した際に、BaseMatrixのE [][]がObject[][]として認識され、setSubVector(GridElement ...)を見つけ出せないため作成しました。
  //   */
  //  @SuppressWarnings({"rawtypes", "unchecked"})
  //  @Deprecated
  //  public static <S extends GridElement<S>> void setSubVector(final Object[] destination, final int min, final int max, final Object[] source) {
  //    int size = max - min + 1;
  //
  //    for (int i = 0; i < size; i++) {
  //      destination[i + min] = source[i];
  //      //destination[i + min] = ((GridElement)destination[0]).transformFrom((GridElement)source[i]);
  //    }
  //  }

  /**
   * 指定したブロックに値を代入します。
   * 
   * @param <S> スカラーの型
   * @param destination 値を設定するグリッド
   * @param rowMin 開始行
   * @param rowMax 終了行
   * @param columnMin 開始列
   * @param columnMax 終了列
   * @param source 代入するグリッド
   */
  public static <S extends GridElement<S>> void setSubMatrix(final S[][] destination, final int rowMin, final int rowMax, final int columnMin, final int columnMax, final S[][] source) {
    int rowSize = source.length;
    int columnSize = rowSize == 0 ? 0 : source[0].length;

    if (rowSize != rowMax - rowMin + 1 || columnSize != columnMax - columnMin + 1) {
      throw new MatrixSizeException(Messages.getString("GridUtil.7")); //$NON-NLS-1$
    }

    for (int i = 0; i < rowSize; i++) {
      S[] toi = destination[rowMin + i];
      S[] fromi = source[i];
      for (int j = 0; j < columnSize; j++) {
        toi[columnMin + j] = fromi[j].clone();
        //toi[columnMin + j] = destination[0][0].transformFrom(fromi[j]);
      }
    }
  }

  /**
   * 指定された番号の成分に値を代入します。
   * 
   * @param <S> スカラーの型
   * @param destination 値を設定するグリッド
   * @param index 指定する成分番号を含む指数
   * @param source 代入するグリッド
   */
  public static <S extends GridElement<S>> void setElements(final S[][] destination, final int[] index, final S[][] source) {
    int rowSize = destination.length;
    int columnSize = rowSize == 0 ? 0 : destination[0].length;

    int size = index.length;
    int fromRowSize = source.length;
    int fromColumnSize = fromRowSize == 0 ? 0 : source[0].length;

    if (size > fromRowSize * fromColumnSize) {
      throw new MatrixSizeException(Messages.getString("GridUtil.8")); //$NON-NLS-1$
    }

    for (int i = 0; i < size; i++) {
      int row = (index[i]) / columnSize;
      int column = (index[i]) % columnSize;
      destination[row][column] = source[i / fromColumnSize][i % fromColumnSize].clone();
      //destination[row][column] = destination[0][0].transformFrom(source[i / fromColumnSize][i % fromColumnSize]);
    }
  }

  /**
   * 零行列であるか判定します。
   * 
   * @param grid 調べる行列
   * @return 零行列ならばtrue、そうでなければfalse
   */
  public static boolean isZero(final GridElement<?>[][] grid) {
    int rowSize = grid.length;
    int columnSize = rowSize == 0 ? 0 : grid[0].length;
    for (int i = 0; i < rowSize; i++) {
      GridElement<?>[] gridi = grid[i];
      for (int j = 0; j < columnSize; j++) {
        if (!gridi[j].isZero()) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * 各列ベクトルを縦に結合し、長い列ベクトルを生成します。
   * 
   * @param <S> 成分の型
   * @param grid 対象となるグリッド
   * @return 列ベクトル
   */
  public static <S extends GridElement<S>> S[][] makeColumnVector(final S[][] grid) {
    int rowSize = grid.length;
    int columnSize = rowSize == 0 ? 0 : grid[0].length;

    if (rowSize == 1 || columnSize == 1) {
      return reshape(grid, rowSize * columnSize, 1);
    }
    return reshape(transpose(grid), rowSize * columnSize, 1);
  }

  /**
   * グリッドの全ての成分に零を代入します。
   * 
   * @param <S> 成分の型
   * @param grid 対象となるグリッド
   */
  public static <S extends GridElement<S>> void setZero(final S[][] grid) {
    int rowSize = grid.length;
    int columnSize = rowSize == 0 ? 0 : grid[0].length;

    for (int i = 0; i < rowSize; i++) {
      S[] gridi = grid[i];
      for (int j = 0; j < columnSize; j++) {
        gridi[j] = gridi[j].createZero();
      }
    }
  }

  /**
   * 指定された成分と同じ型の配列を生成します。
   * 
   * @param <S> 成分の型
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 型を指定するため成分
   * @return 指定された成分と同じ型の配列
   */
  public static <S extends GridElement<S>> S[][] createArray(final int rowSize, final int columnSize, final S[][] elements) {
    if (elements.length == 0 || elements[0].length == 0) {
      return createTwoDimensionalArray(rowSize, columnSize, elements);
    }

    return elements[0][0].createArray(rowSize, columnSize);
  }

  /**
   * 指定された成分と同じ型の配列を生成します。
   * 
   * @param <S> 成分の型
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 型を指定するため成分
   * @return 指定された成分と同じ型の配列
   */
  public static <S extends GridElement<S>> S[][] createArray(final int rowSize, final int columnSize, final S[] elements) {
    if (elements.length == 0) {
      return createTwoDimensionalArray(rowSize, columnSize, elements);
    }

    return elements[0].createArray(rowSize, columnSize);
  }

  /**
   * 指定された成分と同じ型の2次元配列を生成します。
   * 
   * @param <S> 成分の型
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 型を指定するため成分
   * @return 指定された成分と同じ型の2次元配列
   */
  @SuppressWarnings("unchecked")
  private static <S extends GridElement<S>> S[][] createTwoDimensionalArray(final int rowSize, final int columnSize, final S[][] elements) {
    return (S[][])Array.newInstance(getComponentType(elements.getClass()), new int[] {rowSize, columnSize});
  }

  /**
   * 指定された成分と同じ型の2次元配列を生成します。
   * 
   * @param <S> 成分の型
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 型を指定するため成分
   * @return 指定された成分と同じ型の2次元配列
   */
  @SuppressWarnings("unchecked")
  private static <S extends GridElement<S>> S[][] createTwoDimensionalArray(final int rowSize, final int columnSize, final S[] elements) {
    return (S[][])Array.newInstance(getComponentType(elements.getClass()), new int[] {rowSize, columnSize});
  }

  /**
   * 指定された成分と同じ型の1次元配列を生成します。
   * 
   * @param <S> 成分の型
   * @param size 成分の数
   * @param elements 型を指定するため成分
   * @return 指定された成分と同じ型の1次元配列
   */
  @SuppressWarnings("unchecked")
  private static <S extends GridElement<S>> S[] createOneDimensionalArray(final int size, final S[] elements) {
    return (S[])Array.newInstance(getComponentType(elements.getClass()), new int[] {size});
  }

  /**
   * 指定された成分と同じ型の1次元配列を生成します。
   * 
   * @param <S> 成分の型
   * @param size 成分の数
   * @param elements 型を指定するため成分
   * @return 指定された成分と同じ型の1次元配列
   */
  @SuppressWarnings("unchecked")
  private static <S extends GridElement<S>> S[] createOneDimensionalArray(final int size, final S[][] elements) {
    return (S[])Array.newInstance(getComponentType(elements.getClass()), new int[] {size});
  }

  /**
   * コンポーネントタイプを返します。
   * 
   * @param clazz 対象とするクラス
   * @return コンポーネントタイプ
   */
  private static Class<?> getComponentType(Class<?> clazz) {
    if (clazz.isArray()) {
      return getComponentType(clazz.getComponentType());
    }
    return clazz;
  }

  /**
   * 指定された成分と同じ型の配列を生成します。
   * 
   * @param <S> 成分の型
   * @param size 成分の数
   * @param elements 型を指定するため成分
   * @return 指定された成分と同じ型の配列
   */
  public static <S extends GridElement<S>> S[] createArray(final int size, final S[] elements) {
    if (elements.length == 0) {
      return createOneDimensionalArray(size, elements);
    }

    return elements[0].createArray(size);
  }

  /**
   * 指定された成分と同じ型の配列を生成します。
   * 
   * @param <S> 成分の型
   * @param size 成分の数
   * @param elements 型を指定するため成分
   * @return 指定された成分と同じ型の配列
   */
  public static <S extends GridElement<S>> S[] createArray(final int size, final S[][] elements) {
    if (elements.length == 0 || elements[0].length == 0) {
      return createOneDimensionalArray(size, elements);
    }

    return elements[0][0].createArray(size);
  }

  /**
   * ライターに出力します。
   * 
   * @param <S> スカラーの型
   * @param matrix 対象となる行列
   * @param output ライター
   * @param format 成分の出力フォーマット
   * @param alignment 成分の出力配置
   * @param maxColumnSize 最大列の数
   */
  public static <S extends GridElement<S>> void print(final S[][] matrix, final Writer output, final String format, final GridElementAlignment alignment, final int maxColumnSize) {
    final PrintWriter writer = new PrintWriter(output);
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int remain = columnSize;
    int columnOffset = 0;

    final String form = format.charAt(format.indexOf('%') + 1) == ' ' ? format : format.replace("%", "% "); //$NON-NLS-1$ //$NON-NLS-2$
    final int[] printingColumnWidthes = getPrintingColumnWidthes(matrix, form);

    while (remain > 0) {
      final int printColumnSize = Math.min(maxColumnSize, remain);
      GridUtil.printColumnNumber(columnOffset, printColumnSize, printingColumnWidthes, writer);

      for (int row = 0; row < rowSize; row++) {
        GridUtil.printRowNumber(row, writer);
        GridUtil.outputSpaces(2, writer);

        final S[] matrixi = matrix[row];
        final S[] elements = matrix[0][0].createArray(printColumnSize);
        for (int column = 0; column < printColumnSize; column++) {
          elements[column] = matrixi[columnOffset + column];
        }

        GridUtil.printElements(elements, writer, printingColumnWidthes, form, alignment);
      }
      columnOffset += printColumnSize;
      remain -= printColumnSize;
    }

    writer.flush();
  }

  /**
   * 複数個の成分をプリントライターに出力する。
   * 
   * @param <S> スカラーの型
   * @param elements 成分の配列
   * @param output プリントライター
   * @param printingColumnWidthes 多項式の出力文字列の長さ(幅)
   * @param format 成分の出力フォーマット
   * @param alignment 成分の出力配置
   */
  private static <S extends GridElement<S>> void printElements(final S[] elements, final PrintWriter output, final int[] printingColumnWidthes, final String format,
      final GridElementAlignment alignment) {
    for (int column = 0; column < elements.length; column++) {
      final int printingColumnWidth = printingColumnWidthes[column];

      final String element = elements[column].toString(format);

      final int elementWidth = element.length();
      final int gap = printingColumnWidth - elementWidth + 2;

      if (alignment == GridElementAlignment.RIGHT) {
        GridUtil.outputSpaces(gap, output);
      }

      if (alignment == GridElementAlignment.CENTER) {
        GridUtil.outputSpaces((gap - 1) / 2, output);
      }

      output.print(element);

      if (alignment == GridElementAlignment.CENTER) {
        GridUtil.outputSpaces(gap - (gap - 1) / 2, output);
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
   * 行列の各列の出力文字列の長さ(幅)の配列を返します。
   * 
   * @param <S> スカラーの型
   * @param elements 行列
   * @param format 成分の出力フォーマット
   * @return 行列の各列の出力文字列の長さ(幅)の配列
   */
  private static <S extends GridElement<S>> int[] getPrintingColumnWidthes(final S[][] elements, final String format) {
    final int rowSize = elements.length;
    final int columnSize = rowSize == 0 ? 0 : elements[0].length;
    final int[] printingColumnWidthes = new int[columnSize];

    final int minimumColumnWidth = 6;

    for (int column = 0; column < columnSize; column++) {
      final S[] columnElements = elements[0][0].createArray(rowSize);
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
   * 成分の出力文字列の幅の最大値を返します。
   * 
   * @param <S> スカラーの型
   * @param elements 成分の配列
   * @param format 出力フォーマット
   * @return 成分の出力文字列の幅の最大値
   */
  static <S extends GridElement<S>> int getElementWidth(final S[] elements, final String format) {
    int maxLength = 0;
    for (final S element : elements) {
      final int length = element.toString(format).length();
      maxLength = Math.max(maxLength, length);
    }

    return maxLength;
  }

  /**
   * 列番号を表示する。
   * 
   * @param columnOffset 列のオフセット
   * @param printColumnSize 出力する列の数
   * @param printingColumnLengthes 出力する列の幅
   * @param output 出力先のプリントライター
   */
  private static void printColumnNumber(final int columnOffset, final int printColumnSize, final int[] printingColumnLengthes, final PrintWriter output) {
    outputSpaces(GridFormat.LEFT_MARGIN, output);

    for (int column = 0; column < printColumnSize; column++) {
      final int printingWidth = printingColumnLengthes[column];

      if (column != 0) {
        outputSpaces(GridFormat.COLUMN_SEPARATION, output);
      }

      final int formatLength = 3;
      final String columnNumber = String.format("(%" + formatLength + "d)", Integer.valueOf(columnOffset + column + 1)); //$NON-NLS-1$ //$NON-NLS-2$
      final int columnNumberWidth = columnNumber.length();

      final int leftMarginWidth = (printingWidth - columnNumberWidth + 1) / 2;
      final int rightMerginWidth = printingWidth - columnNumberWidth - leftMarginWidth;

      output.print("["); //$NON-NLS-1$
      outputSpaces(leftMarginWidth, output);
      output.print(columnNumber);
      outputSpaces(rightMerginWidth, output);
      output.print("]"); //$NON-NLS-1$
    }
    output.println();
  }

  /**
   * 空白文字列を出力します。
   * 
   * @param count 空白の数
   * @param output 出力先のプリントライター
   */
  static void outputSpaces(final int count, final PrintWriter output) {
    char[] spaces = new char[count];
    for (int i = 0; i < count; i++) {
      spaces[i] = ' ';
    }
    output.print(spaces);
  }

  /**
   * 行番号を出力します。
   * 
   * @param row 行番号
   * @param output 出力先のプリントライター
   */
  static void printRowNumber(final int row, final PrintWriter output) {
    final int formatLength = 3;
    output.print(String.format(" (%" + formatLength + "d)", Integer.valueOf(row + 1))); //$NON-NLS-1$ //$NON-NLS-2$
  }

  /**
   * 少なくとも1個は零の成分がベクトルに含まれるか判定します。
   * 
   * @param <S> 成分の型
   * @param vector 判定する対象のベクトル
   * @return 少なくとも1個は零の成分があればtrue、そうでなければfalse
   */
  public static <S extends GridElement<S>> boolean anyZero(final S[] vector) {
    int rowSize = vector.length;

    for (int i = 0; i < rowSize; i++) {
      if (vector[i].isZero()) {
        return true;
      }
    }
    return false;
  }

  /**
   * 零行列を生成します。
   * 
   * @param <S> 成分の型
   * @param elements 対象となる行列
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 零行列
   */
  public static <S extends GridElement<S>> S[][] createZero(final S[][] elements, final int rowSize, final int columnSize) {
    if (elements.length == 0 || elements[0].length == 0) {
      return createArray(rowSize, columnSize, elements);
    }

    final S value = elements[0][0];
    return createZero(value, rowSize, columnSize);
  }

  /**
   * 零行列を生成します。
   * 
   * @param <S> 成分の型
   * @param value 対象となる値
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 零行列
   */
  public static <S extends GridElement<S>> S[][] createZero(final S value, final int rowSize, final int columnSize) {
    S[][] ans = value.createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      final S[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = value.createZero();
      }
    }

    return ans;
  }

  /**
   * 零ベクトルを生成します。
   * 
   * @param <S> 成分の型
   * @param elements 対象となる行列
   * @param rowSize 行の数
   * @return 零ベクトル
   */
  public static <S extends GridElement<S>> S[] createZero(final S[] elements, final int rowSize) {
    if (elements.length == 0) {
      final S[] ans = createArray(rowSize, elements);
      return ans;
    }

    final S value = elements[0];
    return createZero(value, rowSize);
  }

  /**
   * 零ベクトルを生成します。
   * 
   * @param <S> 成分の型
   * @param value 対象となる値
   * @param size 行の数
   * @return 零ベクトル
   */
  public static <S extends GridElement<S>> S[] createZero(final S value, final int size) {
    S[] ans = value.createArray(size);

    for (int i = 0; i < size; i++) {
      ans[i] = value.createZero();
    }

    return ans;
  }

  /**
   * 行列を1行の文字列に変換します。
   * 
   * @param <S> 成分の型
   * 
   * @param matrix 対象となる行列
   * @param elementFormat 成分の出力フォーマット
   * @return 1行の文字列
   */
  public static <S extends GridElement<S>> String toString(final S[][] matrix, final String elementFormat) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final StringBuffer sb = new StringBuffer();

    if (rowSize == 0 || columnSize == 0) {
      return "[[]]"; //$NON-NLS-1$
    }

    if (columnSize == 1 && rowSize != 1) {
      return toString(transpose(matrix), elementFormat) + "'"; //$NON-NLS-1$
    }

    if (rowSize != 1) {
      sb.append("["); //$NON-NLS-1$
    }

    final int displayColumnSize = 1;

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
          sb.append((matrix[i][j]).toString(elementFormat));
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
