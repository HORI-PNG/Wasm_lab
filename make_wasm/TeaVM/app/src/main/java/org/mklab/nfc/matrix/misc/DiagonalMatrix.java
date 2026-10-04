/*
 * $Id: Diag.java,v 1.7 2008/03/03 15:18:35 koga Exp $
 * 
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.matrix.misc;

import java.util.List;

import org.mklab.nfc.matrix.BaseMatrix;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.IntMatrix;
import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;


/**
 * 対角行列を生成するクラスです。
 * 
 * <p>Diagonal matrix
 * 
 * @author koga
 * @version $Revision: 1.7 $
 */
public final class DiagonalMatrix {
  /**
   * 新しく生成された<code>DiagonalMatrix</code>オブジェクトを初期化します。
   */
  private DiagonalMatrix() {
    // nothing to do
  }

  /**
   * 対角行列を生成します。
   * 
   * @param elements 対角成分
   * @return 対角行列
   */
  public static DoubleMatrix create(final double... elements) {
    return DoubleMatrix.diagonal(elements);
  }

  /**
   * 対角行列を生成します。
   * 
   * @param elements 対角成分
   * @return 対角行列
   */
  public static IntMatrix create(final int... elements) {
    return IntMatrix.diagonal(elements);
  }

  /**
   * 対角行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param elements 対角成分
   * @return 対角行列
   */
  @SafeVarargs
  public static <S extends Scalar<S,M>, M extends Matrix<S,M>> M create(final S... elements) {
    return BaseMatrix.<S,M>diagonal(elements);
  }

  /**
   * 対角ブロック行列を生成します。
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrices 対角ブロック行列
   * @return 対角ブロック行列
   */
  public static <S extends Scalar<S,M>, M extends Matrix<S,M>>M create(final M... matrices) {
    int rSize = 0;
    int cSize = 0;
    M matrix = matrices[0];

    for (int i = 0; i < matrices.length; i++) {
      rSize += matrices[i].getRowSize();
      cSize += matrices[i].getColumnSize();
    }

    final M ans = matrix.createZero(rSize, cSize);

    int row = 1;
    int column = 1;
    for (int i = 0; i < matrices.length; i++) {
      final M x = matrices[i];
      final int xRowSize = x.getRowSize();
      final int xColumnSize = x.getColumnSize();
      ans.setSubMatrix(row, row + xRowSize - 1, column, column + xColumnSize - 1, x);

      row += xRowSize;
      column += xColumnSize;
    }

    return ans;
  }

  /**
   * 対角ブロック行列を生成します。
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param matrices 対角ブロック行列
   * @return 対角ブロック行列
   */
  public static <S extends Scalar<S,M>, M extends Matrix<S,M>>M create(final List<M>  matrices) {
    int rSize = 0;
    int cSize = 0;
    M matrix = matrices.get(0);

    for (int i = 0; i < matrices.size(); i++) {
      rSize += matrices.get(i).getRowSize();
      cSize += matrices.get(i).getColumnSize();
    }

    final M ans = matrix.createZero(rSize, cSize);

    int row = 1;
    int column = 1;
    for (int i = 0; i < matrices.size(); i++) {
      final M x = matrices.get(i);
      final int xRowSize = x.getRowSize();
      final int xColumnSize = x.getColumnSize();
      ans.setSubMatrix(row, row + xRowSize - 1, column, column + xColumnSize - 1, x);

      row += xRowSize;
      column += xColumnSize;
    }

    return ans;
  }

}
