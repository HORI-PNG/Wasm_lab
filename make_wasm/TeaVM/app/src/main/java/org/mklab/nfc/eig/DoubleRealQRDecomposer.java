/*
 * $Id: DoubleRealQrDecomposition.java,v 1.3 2008/02/03 12:43:17 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.DoubleMatrixUtil;
import org.mklab.nfc.matrix.IntMatrixUtil;


/**
 * 倍精度(double)型の実行列のQR分解(A=Q*R)を行うためのクラスです。
 * 
 * @author koga
 * @version $Revision: 1.3 $
 */
public final class DoubleRealQRDecomposer {

  /**
   * 倍精度(double)の実行列をQR分解(A=Q*R)を返します。
   * 
   * <p>実行列をA、直交行列をQ、上三角行列をRとすると、これらの行列の間には、
   * 
   * <blockquote> A = Q * R </blockquote>
   * 
   * <blockquote> Q<sup>T </sup>* Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param a 行列
   * @return QR分解の結果
   */
  public QRDecompositionDoubleRealElements decompose(final double[][] a) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;
    final double[][] q = new double[rowSize][rowSize];
    final double[][] r = new double[rowSize][columnSize];
    final int[][] p = new int[columnSize][columnSize];
    decompose(a, q, r, p, false);
    return new QRDecompositionDoubleRealElements(q, r);
  }

  /**
   * 倍精度(double)の実行列の並べ替え付きQR分解を返します。
   * 
   * <p>実行列をA、直交行列をQ、上三角行列を R、並べ替え行列をPとすると、 これらの行列の間には、
   * 
   * <blockquote> A * P = Q * R </blockquote>
   * 
   * <blockquote> Q <sup>T </sup>* Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param a 行列
   * @return QR分解の結果
   */
  public QRDecompositionDoubleRealElements decomposeWithPermutation(final double[][] a) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;
    final double[][] q = new double[rowSize][rowSize];
    final double[][] r = new double[rowSize][columnSize];
    final int[][] p = new int[columnSize][columnSize];
    decompose(a, q, r, p, true);
    return new QRDecompositionDoubleRealElements(q, r, p);
  }

  /**
   * 列ピボット付きでハウスホルダー行列を用いてa行列のQR分解(a*p = q*r)を求めます。
   * 
   * @param a 対象となる行列
   * @param q QR分解のQ行列
   * @param r QR分解のR行列
   * @param p パーミュテーション行列
   * @param isPDesired パーミュテーション行列を求めるならばtrue
   */
  private void decompose(final double[][] a, final double[][] q, final double[][] r, final int[][] p, final boolean isPDesired) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    if (rowSize < columnSize) {
      /*
       * a = [a2 | x] a2 * p2 = q2 * r2
       * 
       * [a2 | x] [[p2 Z] = q * [r2 | q'*x] [Z I]]
       */

      final double[][] a2 = DoubleMatrixUtil.getSubMatrix(a, 0, rowSize - 1, 0, rowSize - 1);
      final double[][] q2 = new double[rowSize][rowSize];
      final double[][] r2 = new double[rowSize][rowSize];
      final int[][] p2 = new int[rowSize][rowSize];

      decompose(a2, q2, r2, p2, isPDesired);
      DoubleMatrixUtil.copy(q2, q);

      final double[][] x = DoubleMatrixUtil.getSubMatrix(a, 0, rowSize - 1, rowSize, columnSize - 1);
      final double[][] qt = DoubleMatrixUtil.transpose(q);
      final  double[][] x2 = DoubleMatrixUtil.multiply(qt, x);

      DoubleMatrixUtil.setSubMatrix(r, 0, r2.length - 1, 0, r2[0].length - 1, r2);
      DoubleMatrixUtil.setSubMatrix(r, 0, x2.length - 1, rowSize, rowSize + x2[0].length - 1, x2);

      if (isPDesired) {
        IntMatrixUtil.setSubMatrix(p, 0, p2.length - 1, 0, p2[0].length - 1, p2);

        final int[][] i2 = IntMatrixUtil.createUnit(columnSize - rowSize, columnSize - rowSize);
        IntMatrixUtil.setSubMatrix(p, rowSize, rowSize + i2.length - 1, rowSize, rowSize + i2[0].length - 1, i2);
      }
    } else {
      final int[] piv = new int[columnSize]; // int *piv
      final int[] pivotCount = new int[1];
      decomposeWithPermutation(a, q, r, piv, pivotCount, isPDesired);

      if (isPDesired) {
        IntMatrixUtil.copy(IntMatrixUtil.createUnit(columnSize, columnSize), p);
        permutate(p, piv, pivotCount[0], false); // rk-1?
      }
    }
  }

  /**
   * 列ピボット付きでハウスホルダー行列を用いてa行列のQR分解(a*p = q*r)を求めます。
   * 
   * @param a a行列
   * @param q QR分解のQ行列
   * @param r QR分解のR行列
   * @param piv ピボット情報
   * @param pivotCount ピボットの回数
   * @param isPDesired パーミュテーション行列を求めるならばtrue
   */
  private void decomposeWithPermutation(final double[][] a, final double[][] q, final double[][] r, final int[] piv, final int[] pivotCount, final boolean isPDesired) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final double[][] a2 = DoubleMatrixUtil.clone(a);
    final double[][] v = new double[rowSize][1];
    final double[] tm = new double[columnSize];

    int k;
    double tau;

    if (isPDesired) {
      for (int j = 1; j <= columnSize; j++) {
        final double[][] tmp1 = DoubleMatrixUtil.getSubMatrix(a2, 0, rowSize - 1, j - 1, j - 1);
        final double dd = DoubleMatrixUtil.frobNorm(tmp1);
        tm[j - 1] = dd * dd;
      }

      tau = tm[0];
      k = 1;
      for (int i = 2; i <= columnSize; i++) {
        if (tau < tm[i - 1]) {
          tau = tm[i - 1];
          k = i;
        }
      }
    } else {
      k = 1;
      tau = 1.0;
    }

    int rk = 0;
    while (tau != 0.0) {
      rk++;

      if (isPDesired) {
        piv[rk - 1] = k;

        DoubleMatrixUtil.exchangeColumn(a2, rk - 1, k - 1);
        final double dd = tm[rk - 1];
        tm[rk - 1] = tm[k - 1];
        tm[k - 1] = dd;
      }

      final double[][] tmp1 = DoubleMatrixUtil.getSubMatrix(a2, rk - 1, rowSize - 1, rk - 1, rk - 1);
      final double[][] hVector = DoubleRealHouseHolderUtil.houseHolderVector(tmp1);
      DoubleMatrixUtil.setSubMatrix(v, rk - 1, (rk - 1) + hVector.length - 1, 0, hVector[0].length - 1, hVector);

      final double[][] tmp2 = DoubleMatrixUtil.getSubMatrix(a2, rk - 1, rowSize - 1, rk - 1, columnSize - 1);
      final double[][] tmp3 = DoubleMatrixUtil.getSubMatrix(v, rk - 1, rowSize - 1, 0, 0);
      final double[][] hVector2 = DoubleRealHouseHolderUtil.multiplyHouseHolderFromLeft(tmp2, tmp3);
      DoubleMatrixUtil.setSubMatrix(a2, rk - 1, (rk - 1) + hVector2.length - 1, rk - 1, (rk - 1) + hVector2[0].length - 1, hVector2);

      if (rk == rowSize) {
        break;
      }

      areaCopy(a2, rk, rk - 1, v, rk, 0, rowSize - 1, 0);

      for (int i = rk + 1; i <= columnSize; i++) {
        tm[i - 1] -= a2[rk - 1][i - 1] * a2[rk - 1][i - 1];
      }

      if (rk < columnSize) {
        if (isPDesired) {
          tau = tm[rk];
          k = rk + 1;
          for (int i = rk + 2; i <= columnSize; i++) {
            if (tau < tm[i - 1]) {
              tau = tm[i - 1];
              k = i;
            }
          }
        }
      } else {
        tau = 0.0;
      }
    }

    DoubleMatrixUtil.setZero(r);
    for (int i = 1; i <= rowSize; i++) {
      for (int j = i; j <= columnSize; j++) {
        r[i - 1][j - 1] = a2[i - 1][j - 1];
      }
    }

    final double[][] q2 = DoubleMatrixUtil.createUnit(rowSize, rowSize);
    for (int j = columnSize; j >= 1; j--) {
      v[j - 1][0] = 1.0;
      if (j < rowSize) {
        areaCopy(v, j, 0, a2, j, j - 1, rowSize - 1, j - 1);
      }
      final double[][] tmp1 = DoubleMatrixUtil.getSubMatrix(q2, j - 1, rowSize - 1, j - 1, rowSize - 1);
      final double[][] tmp2 = DoubleMatrixUtil.getSubMatrix(v, j - 1, rowSize - 1, 0, 0);
      final double[][] hVector = DoubleRealHouseHolderUtil.multiplyHouseHolderFromLeft(tmp1, tmp2);
      DoubleMatrixUtil.setSubMatrix(q2, j - 1, (j - 1) + hVector.length - 1, j - 1, (j - 1) + hVector[0].length - 1, hVector);
    }

    DoubleMatrixUtil.copy(q2, q);
    pivotCount[0] = rk;
  }

  /**
   * パーミュテーション行列を掛けます。
   * 
   * @param a 対象となる行列
   * @param piv ピボット情報
   * @param pivotCount ピボットの回数
   * @param left 左から掛けるならばtrue
   * @return パーミュテーション行列を掛けた結果
   */
  private int[][] permutate(final int[][] a, final int[] piv, final int pivotCount, final boolean left) { // rk
    for (int i = 1; i <= pivotCount; i++) {
      if (left) {
        IntMatrixUtil.exchangeRow(a, i - 1, piv[i - 1] - 1);
      } else {
        IntMatrixUtil.exchangeColumn(a, i - 1, piv[i - 1] - 1);
      }
    }
    return a;
  }

  /**
   * コピー元のブロック成分をコピー先へコピーします。
   * 
   * @param destination コピー先
   * @param destinationRow コピー先の行番号
   * @param destinationColumn コピー先の列番号
   * @param source コピー元
   * @param sourceRow1 コピー元のブロック成分の行の開始番号
   * @param sourceColumn1 コピー元のブロック成分の列の開始番号
   * @param sourceRow2 コピー元のブロック成分の行の終了番号
   * @param sourceColumn2 コピー元のブロック成分の列の終了番号
   * @return コピー先
   */
  private double[][] areaCopy(final double[][] destination, final int destinationRow, final int destinationColumn, final double[][] source, final int sourceRow1, final int sourceColumn1, final int sourceRow2, final int sourceColumn2) {
    final int rsize = sourceRow2 - sourceRow1 + 1;
    final int csize = sourceColumn2 - sourceColumn1 + 1;

    for (int i = 0; i < rsize; i++) {
      final double[] ai = destination[destinationRow + i];
      final double[] bi = source[sourceRow1 + i];
      System.arraycopy(bi, sourceColumn1, ai, destinationColumn, csize);
    }

    return destination;
  }
}