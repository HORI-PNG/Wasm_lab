/*
 * $Id: DoubleComplexQrDecomposition.java,v 1.3 2008/02/03 12:43:17 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.DoubleComplexMatrixUtil;
import org.mklab.nfc.matrix.DoubleMatrixUtil;
import org.mklab.nfc.matrix.IntMatrixUtil;


/**
 * 倍精度(double)型の複素行列のQR分解を行うためのクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.3 $
 */
public final class DoubleComplexQRDecomposer {

  /**
   * 倍精度(double)の複素行列をQR分解を返します。
   * 
   * <p>複素行列をA、ユニタリ行列をQ、上三角行列をRとすると、これらの行列の間には、
   * 
   * <blockquote> A = Q * R </blockquote>
   * 
   * <blockquote> Q<sup>#</sup> * Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @return QR分解の結果
   */
  public QRDecompositionDoubleComplexElements decompose(final double[][] aRe, final double[][] aIm) {
    final int rowSize = aRe.length;
    final int columnSize = rowSize == 0 ? 0 : aRe[0].length;
    final double[][] qRe = new double[rowSize][rowSize];
    final double[][] qIm = new double[rowSize][rowSize];
    final double[][] rRe = new double[rowSize][columnSize];
    final double[][] rIm = new double[rowSize][columnSize];
    final int[][] p = new int[columnSize][columnSize];

    decompose(aRe, aIm, qRe, qIm, rRe, rIm, p, false);

    return new QRDecompositionDoubleComplexElements(qRe, qIm, rRe, rIm);
  }

  /**
   * 倍精度(double)の複素行列の並べ替え付きQR分解を返します。
   * 
   * <p>複素行列をA、ユニタリ行列をQ、上三角行列をR、並べ替え行列をPとすると、 これらの行列の間には、
   * 
   * <blockquote> A * P = Q * R </blockquote>
   * 
   * <blockquote> Q<sup>#</sup> * Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @return QR分解の結果
   */
  public QRDecompositionDoubleComplexElements decomposeWithPermutation(final double[][] aRe, final double[][] aIm) {
    final int rowSize = aRe.length;
    final int columnSize = rowSize == 0 ? 0 : aRe[0].length;
    final double[][] qRe = new double[rowSize][rowSize];
    final double[][] qIm = new double[rowSize][rowSize];
    final double[][] rRe = new double[rowSize][columnSize];
    final double[][] rIm = new double[rowSize][columnSize];
    final int[][] p = new int[columnSize][columnSize];

    decompose(aRe, aIm, qRe, qIm, rRe, rIm, p, true);

    return new QRDecompositionDoubleComplexElements(qRe, qIm, rRe, rIm, p);
  }

  /**
   * 列ピボット付きでハウスホルダー行列を用いてa行列のQR分解(a*p = q*r)を求めます。
   * 
   * @param ar 対象となる行列の実部
   * @param ai 対象となる行列の虚部
   * @param qr QR分解のQ行列の実部
   * @param qi QR分解のQ行列の虚部
   * @param rr QR分解のR行列の実部
   * @param ri QR分解のR行列の虚部
   * @param p パーミュテーション行列
   * @param isPDesired パーミュテーション行列を求めるならばtrue
   */
  private void decompose(final double[][] ar, final double[][] ai, final double[][] qr, final double[][] qi, final double[][] rr, final double[][] ri, final int[][] p, final boolean isPDesired) {
    final int rowSize = ar.length;
    final int columnSize = rowSize == 0 ? 0 : ar[0].length;

    if (rowSize < columnSize) {
      /*
       * a = [a2 | x] a2 * p2 = q2 * r2
       * 
       * [a2 | x] [[p2 Z] = q * [r2 | q'*x] [Z I]]
       */

      final double[][] a2r = DoubleMatrixUtil.getSubMatrix(ar, 0, rowSize - 1, 0, rowSize - 1);
      final double[][] a2i = DoubleMatrixUtil.getSubMatrix(ai, 0, rowSize - 1, 0, rowSize - 1);
      final double[][] q2r = new double[rowSize][rowSize];
      final double[][] q2i = new double[rowSize][rowSize];
      final double[][] r2r = new double[rowSize][rowSize];
      final double[][] r2i = new double[rowSize][rowSize];
      final int[][] p2 = new int[rowSize][rowSize];

      decompose(a2r, a2i, q2r, q2i, r2r, r2i, p2, isPDesired);

      DoubleMatrixUtil.copy(q2r, qr);
      DoubleMatrixUtil.copy(q2i, qi);

      final double[][] xr = DoubleMatrixUtil.getSubMatrix(ar, 0, rowSize - 1, rowSize, columnSize - 1);
      final double[][] xi = DoubleMatrixUtil.getSubMatrix(ai, 0, rowSize - 1, rowSize, columnSize - 1);
      final double[][] qtr = DoubleMatrixUtil.transpose(qr);
      final double[][] qti = DoubleMatrixUtil.unaryMinus(DoubleMatrixUtil.transpose(qi));
      final double[][][] x2 = DoubleComplexMatrixUtil.multiply(qtr, qti, xr, xi);
      final double[][] x2r = x2[0];
      final double[][] x2i = x2[1];

      final int row2 = r2r.length - 1;
      final int col2 = r2r[0].length - 1;
      DoubleMatrixUtil.setSubMatrix(rr, 0, row2, 0, col2, r2r);

      final int row3 = r2i.length - 1;
      final int col3 = r2i[0].length - 1;
      DoubleMatrixUtil.setSubMatrix(ri, 0, row3, 0, col3, r2i);

      final int row4 = x2r.length - 1;
      final int col4 = rowSize + x2r[0].length - 1;
      DoubleMatrixUtil.setSubMatrix(rr, 0, row4, rowSize, col4, x2r);

      final int row5 = x2i.length - 1;
      final int col5 = rowSize + x2i[0].length - 1;
      DoubleMatrixUtil.setSubMatrix(ri, 0, row5, rowSize, col5, x2i);

      if (isPDesired) {
        final int row6 = p2.length - 1;
        final int col6 = p2[0].length - 1;
        IntMatrixUtil.setSubMatrix(p, 0, row6, 0, col6, p2);

        final int[][] i2 = IntMatrixUtil.createUnit(columnSize - rowSize, columnSize - rowSize);
        final int row7 = rowSize + i2.length - 1;
        final int col7 = rowSize + i2[0].length - 1;
        IntMatrixUtil.setSubMatrix(p, rowSize, row7, rowSize, col7, i2);
      } 
    } else {
      final int[] piv = new int[columnSize];
      final int[] rk = new int[1];

      decomposeWithPermutation(ar, ai, qr, qi, rr, ri, piv, rk, isPDesired);

      if (isPDesired) {
        IntMatrixUtil.copy(IntMatrixUtil.createUnit(columnSize, columnSize), p);
        IntMatrixUtil.permutateSelf(p, piv, rk[0], false);
      }
    }
  }

  /**
   * 列ピボット付きでハウスホルダー行列を用いてa行列のQR分解(a*p = q*r)を求めます。
   * 
   * @param ar a行列の実部
   * @param ai a行列の虚部
   * @param qr QR分解のQ行列の実部
   * @param qi QR分解のQ行列の虚部
   * @param rr QR分解のR行列の実部
   * @param ri QR分解のR行列の虚部
   * @param piv ピボット情報
   * @param pivotCount ピボットの回数
   * @param isPDesired パーミュテーション行列を求めるならばtrue
   */
  private void decomposeWithPermutation(final double[][] ar, final double[][] ai, final double[][] qr, final double[][] qi, final double[][] rr, final double[][] ri, final int[] piv, final int[] pivotCount, final boolean isPDesired) {
    final int rowSize = ar.length;
    final int columnSize = rowSize == 0 ? 0 : ar[0].length;

    final double[][] a2r = DoubleMatrixUtil.clone(ar);
    final double[][] a2i = DoubleMatrixUtil.clone(ai);
    final double[] tm = new double[columnSize];

    int k;
    double tau;
    if (isPDesired) {
      for (int j = 1; j <= columnSize; j++) {
        final double[][] tmp1r = DoubleMatrixUtil.getSubMatrix(a2r, 0, rowSize - 1, j - 1, j - 1);
        final double[][] tmp1i = DoubleMatrixUtil.getSubMatrix(a2i, 0, rowSize - 1, j - 1, j - 1);
        final double dd = DoubleComplexMatrixUtil.frobNorm(tmp1r, tmp1i);
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
      tau = 1.0; /* dummy */
    }

    int rk = 0;
    while (tau != 0.0) {
      rk++;

      if (isPDesired) {
        piv[rk - 1] = k;

        DoubleMatrixUtil.exchangeColumn(a2r, rk - 1, k - 1);
        DoubleMatrixUtil.exchangeColumn(a2i, rk - 1, k - 1);
        double dd = tm[rk - 1];
        tm[rk - 1] = tm[k - 1];
        tm[k - 1] = dd;
      }
      final double[][][] v = DoubleComplexHouseHolderUtil.houseHolderVector(DoubleMatrixUtil.getSubMatrix(a2r, rk - 1, rowSize - 1, rk - 1, rk - 1),
          DoubleMatrixUtil.getSubMatrix(a2i, rk - 1, rowSize - 1, rk - 1, rk - 1), 1);
      final double[][] vr = v[0];
      final double[][] vi = v[1];

      final double[][] tmp1r = DoubleMatrixUtil.getSubMatrix(a2r, rk - 1, rowSize - 1, rk - 1, columnSize - 1);
      final double[][] tmp1i = DoubleMatrixUtil.getSubMatrix(a2i, rk - 1, rowSize - 1, rk - 1, columnSize - 1);
      final double[][][] mul = DoubleComplexHouseHolderUtil.multiplyHouseHolderFromLeft(tmp1r, tmp1i, vr, vi);

      final int row2 = (rk - 1) + mul[0].length - 1;
      final int col2 = (rk - 1) + mul[0][0].length - 1;
      DoubleMatrixUtil.setSubMatrix(a2r, rk - 1, row2, rk - 1, col2, mul[0]);

      final int row3 = (rk - 1) + mul[1].length - 1;
      final int col3 = (rk - 1) + mul[1][0].length - 1;
      DoubleMatrixUtil.setSubMatrix(a2i, rk - 1, row3, rk - 1, col3, mul[1]);

      if (rk == rowSize) {
        break;
      }

      final double d1 = a2r[rk - 1][rk - 1];
      final double d2 = a2i[rk - 1][rk - 1];

      final int row4 = (rk - 1) + vr.length - 1;
      final int col4 = (rk - 1) + vr[0].length - 1;
      DoubleMatrixUtil.setSubMatrix(a2r, rk - 1, row4, rk - 1, col4, vr);

      final int row5 = (rk - 1) + vi.length - 1;
      final int col5 = (rk - 1) + vi[0].length - 1;
      DoubleMatrixUtil.setSubMatrix(a2i, rk - 1, row5, rk - 1, col5, vi);
      a2r[rk - 1][rk - 1] = d1;
      a2i[rk - 1][rk - 1] = d2;

      for (int i = rk + 1; i <= columnSize; i++) {
        tm[i - 1] -= a2r[rk - 1][i - 1] * a2r[rk - 1][i - 1] + a2i[rk - 1][i - 1] * a2i[rk - 1][i - 1];
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

    DoubleMatrixUtil.setZero(rr);
    DoubleMatrixUtil.setZero(ri);

    for (int i = 1; i <= rowSize; i++) {
      for (int j = i; j <= columnSize; j++) {
        rr[i - 1][j - 1] = a2r[i - 1][j - 1];
        ri[i - 1][j - 1] = a2i[i - 1][j - 1];
      }
    }

    final double[][] q2r = DoubleMatrixUtil.createUnit(rowSize, rowSize);
    final double[][] q2i = new double[rowSize][rowSize];

    for (int j = columnSize; j >= 1; j--) {
      final double[][] vr = DoubleMatrixUtil.getSubMatrix(a2r, j - 1, rowSize - 1, j - 1, j - 1);
      final double[][] vi = DoubleMatrixUtil.getSubMatrix(a2i, j - 1, rowSize - 1, j - 1, j - 1);
      vr[0][0] = 1.0;
      vi[0][0] = 0;
      final double[][][] tmp1 = DoubleComplexHouseHolderUtil.multiplyHouseHolderFromLeft(DoubleMatrixUtil.getSubMatrix(q2r, j - 1, rowSize - 1, j - 1, rowSize - 1),
          DoubleMatrixUtil.getSubMatrix(q2i, j - 1, rowSize - 1, j - 1, rowSize - 1), vr, vi);
      final double[][] tmp1r = tmp1[0];
      final double[][] tmp1i = tmp1[1];

      final int row2 = (j - 1) + tmp1r.length - 1;
      final int col2 = (j - 1) + tmp1r[0].length - 1;
      DoubleMatrixUtil.setSubMatrix(q2r, j - 1, row2, j - 1, col2, tmp1r);

      final int row3 = (j - 1) + tmp1i.length - 1;
      final int col3 = (j - 1) + tmp1i[0].length - 1;
      DoubleMatrixUtil.setSubMatrix(q2i, j - 1, row3, j - 1, col3, tmp1i);
    }

    DoubleMatrixUtil.copy(q2r, qr);
    DoubleMatrixUtil.copy(q2i, qi);

    pivotCount[0] = rk;
  }

}