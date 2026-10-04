/*
 * $Id: RealQrDecomposition.java,v 1.4 2008/03/15 00:23:43 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.BaseMatrixUtil;
import org.mklab.nfc.matrix.AbstractNumericalMatrixUtil;
import org.mklab.nfc.matrix.GridUtil;
import org.mklab.nfc.matrix.IntMatrixUtil;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 実行列のQR分解を行うためのクラスです。
 * 
 * @param <S> スカラーの型
 * @param <M> 行列の型 
 * @author koga
 * @version $Revision: 1.4 $
 */
public final class RealQRDecomposer<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {

  /**
   * 実行列をQR分解を返します。
   * 
   * <p>実行列をA、直交行列をQ、上三角行列をRとすると、これらの行列の間には、
   * 
   * <blockquote> A = Q * R </blockquote>
   * 
   * <blockquote> Q<sup>T </sup>* Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * 
   * @param a 対象となる実行列
   * @return QR分解の結果
   */
  public QRDecompositionElements<S,M> decompose(final S[][] a) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final S[][] q = GridUtil.createZero(a, rowSize, rowSize);
    final S[][] r = GridUtil.createZero(a, rowSize, columnSize);
    final int[][] p = new int[columnSize][columnSize];
    decompose(a, q, r, p, false);
    return new QRDecompositionElements<>(q, r);
  }

  /**
   * 実行列の並べ替え付きQR分解を返します。
   * 
   * <p>実行列をA、直交行列をQ、上三角行列を R、並べ替え行列をPとすると、 これらの行列の間には、
   * 
   * <blockquote> A * P = Q * R </blockquote>
   * 
   * <blockquote> Q <sup>T </sup>* Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param a 対象となる実行列
   * @return QR分解の結果
   */
  public QRDecompositionElements<S,M> decomposeWithPermutation(final S[][] a) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;
    final S[][] q = GridUtil.createZero(a, rowSize, rowSize);
    final S[][] r = GridUtil.createZero(a, rowSize, columnSize);
    final int[][] p = new int[columnSize][columnSize];
    decompose(a, q, r, p, true);
    return new QRDecompositionElements<>(q, r, p);
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
  private void decompose(final S[][] a, final S[][] q, final S[][] r, final int[][] p, final boolean isPDesired) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    if (rowSize < columnSize) {
      /*
       * a = [a2 | x] a2 * p2 = q2 * r2
       * 
       * [a2 | x] [[p2 Z] = q * [r2 | q'*x] [Z I]]
       */

      final S[][] a2 = GridUtil.getSubMatrix(a, 0, rowSize - 1, 0, rowSize - 1);
      final S[][] q2 = GridUtil.createZero(a, rowSize, rowSize);
      final S[][] r2 = GridUtil.createZero(a, rowSize, rowSize);
      final int[][] p2 = new int[rowSize][rowSize];

      decompose(a2, q2, r2, p2, isPDesired);
      GridUtil.copy(q2, q);

      final S[][] x = GridUtil.getSubMatrix(a, 0, rowSize - 1, rowSize, columnSize - 1);
      final S[][] qt = GridUtil.transpose(q);
      final S[][] x2 = BaseMatrixUtil.multiply(qt, x);

      GridUtil.setSubMatrix(r, 0, r2.length - 1, 0, r2[0].length - 1, r2);
      GridUtil.setSubMatrix(r, 0, x2.length - 1, rowSize, rowSize + x2[0].length - 1, x2);

      if (isPDesired) {
        IntMatrixUtil.setSubMatrix(p, 0, p2.length - 1, 0, p2[0].length - 1, p2);

        final int[][] i2 = IntMatrixUtil.createUnit(columnSize - rowSize, columnSize - rowSize);
        IntMatrixUtil.setSubMatrix(p, rowSize, rowSize + i2.length - 1, rowSize, rowSize + i2[0].length - 1, i2);
      }
    } else {
      final int[] piv = new int[columnSize]; // int *piv
      final int[] rk = new int[1];
      decomposeWithPermutation(a, q, r, piv, rk, isPDesired);

      if (isPDesired) {
        IntMatrixUtil.copy(IntMatrixUtil.createUnit(columnSize, columnSize), p);
        permutate(p, piv, rk[0], false); // rk-1?
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
  private void decomposeWithPermutation(final S[][] a, final S[][] q, final S[][] r, final int[] piv, final int[] pivotCount, final boolean isPDesired) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final S unit = a[0][0].createUnit();

    final S[][] a2 = GridUtil.clone(a);
    final S[][] v = GridUtil.createZero(a, rowSize, 1);
    final S[] tm = GridUtil.createZero(a[0], columnSize);

    int k;
    S tau;

    if (isPDesired) {
      for (int j = 1; j <= columnSize; j++) {
        final S[][] tmp1 = GridUtil.getSubMatrix(a2, 0, rowSize - 1, j - 1, j - 1);
        final S dd = AbstractNumericalMatrixUtil.frobNorm(tmp1);
        tm[j - 1] = dd.multiply(dd);
      }

      tau = tm[0].clone();
      k = 1;
      for (int i = 2; i <= columnSize; i++) {
        if (tau.isLessThan(tm[i - 1])) {
          tau = tm[i - 1].clone();
          k = i;
        }
      }
    } else {
      k = 1;
      tau = unit.createUnit();
    }

    int rk = 0;
    while (tau.isZero() == false) {
      rk++;

      if (isPDesired) {
        piv[rk - 1] = k;

        GridUtil.exchangeColumn(a2, rk - 1, k - 1);
        S dd = tm[rk - 1];
        tm[rk - 1] = tm[k - 1];
        tm[k - 1] = dd;
      }

      final S[][] tmp1 = GridUtil.getSubMatrix(a2, rk - 1, rowSize - 1, rk - 1, rk - 1);
      final S[][] hVector = RealHouseHolderUtil.houseHolderVector(tmp1);
      GridUtil.setSubMatrix(v, rk - 1, (rk - 1) + hVector.length - 1, 0, hVector[0].length - 1, hVector);

      final S[][] tmp2 = GridUtil.getSubMatrix(a2, rk - 1, rowSize - 1, rk - 1, columnSize - 1);
      final S[][] tmp3 = GridUtil.getSubMatrix(v, rk - 1, rowSize - 1, 0, 0);
      final S[][] hVector2 = RealHouseHolderUtil.multiplyHouseHolderFromLeft(tmp2, tmp3);
      GridUtil.setSubMatrix(a2, rk - 1, (rk - 1) + hVector2.length - 1, rk - 1, (rk - 1) + hVector2[0].length - 1, hVector2);

      if (rk == rowSize) {
        break;
      }

      areaCopy(a2, rk, rk - 1, v, rk, 0, rowSize - 1, 0);

      for (int i = rk + 1; i <= columnSize; i++) {
        tm[i - 1] = tm[i - 1].subtract(a2[rk - 1][i - 1].multiply(a2[rk - 1][i - 1]));
      }

      if (rk < columnSize) {
        if (isPDesired) {
          tau = tm[rk].clone();
          k = rk + 1;
          for (int i = rk + 2; i <= columnSize; i++) {
            if (tau.isLessThan(tm[i - 1])) {
              tau = tm[i - 1].clone();
              k = i;
            }
          }
        }
      } else {
        tau = unit.createZero();
      }
    }

    GridUtil.setZero(r);
    for (int i = 1; i <= rowSize; i++) {
      for (int j = i; j <= columnSize; j++) {
        r[i - 1][j - 1] = a2[i - 1][j - 1].clone();
      }
    }

    final S[][] q2 = BaseMatrixUtil.createUnit(a, rowSize, rowSize);
    for (int j = columnSize; j >= 1; j--) {
      v[j - 1][0] = unit.createUnit();
      if (j < rowSize) {
        areaCopy(v, j, 0, a2, j, j - 1, rowSize - 1, j - 1);
      }
      final S[][] tmp1 = GridUtil.getSubMatrix(q2, j - 1, rowSize - 1, j - 1, rowSize - 1);
      final S[][] tmp2 = GridUtil.getSubMatrix(v, j - 1, rowSize - 1, 0, 0);
      final S[][] hVector = RealHouseHolderUtil.multiplyHouseHolderFromLeft(tmp1, tmp2);
      GridUtil.setSubMatrix(q2, j - 1, (j - 1) + hVector.length - 1, j - 1, (j - 1) + hVector[0].length - 1, hVector);
    }

    GridUtil.copy(q2, q);
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
  private S[][] areaCopy(final S[][] destination, final int destinationRow, final int destinationColumn, final S[][] source, final int sourceRow1, final int sourceColumn1, final int sourceRow2, final int sourceColumn2) {
    final int rsize = sourceRow2 - sourceRow1 + 1;
    final int csize = sourceColumn2 - sourceColumn1 + 1;

    final S[][] bCopy = GridUtil.clone(source);

    for (int i = 0; i < rsize; i++) {
      final S[] ai = destination[destinationRow + i];
      final S[] bi = bCopy[sourceRow1 + i];
      System.arraycopy(bi, sourceColumn1, ai, destinationColumn, csize);
    }

    return destination;
  }
}