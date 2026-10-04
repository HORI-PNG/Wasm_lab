/*
 * $Id: ComplexQrDecomposition.java,v 1.5 2008/03/19 11:17:52 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import java.util.List;

import org.mklab.nfc.matrix.BaseMatrixUtil;
import org.mklab.nfc.matrix.AbstractNumericalMatrixUtil;
import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.GridUtil;
import org.mklab.nfc.matrix.IntMatrixUtil;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * 複素行列のQR分解を行うためのクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.5 $
 * 
 * @param <RS> 実スカラーの型
 * @param <RM> 実行列の型
 * @param <CS> 複素スカラーの型 
 * @param <CM> 複素行列の型
 */
public final class ComplexQRDecomposer<RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> {

  /**
   * 複素行列をQR分解を返します。
   * 
   * <p>複素行列をA、ユニタリ行列をQ、上三角行列をRとすると、これらの行列の間には、
   * 
   * <blockquote> A = Q * R </blockquote>
   * 
   * <blockquote> Q<sup>#</sup> * Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * 
   * 
   * @param a 複素行列
   * @return QR分解の結果
   */
  public QRDecompositionElements<CS,CM> decompose(final CS[][] a) {
    final RS[][] aRe = AbstractNumericalMatrixUtil.getRealPartElements(a);
    final RS[][] aIm = AbstractNumericalMatrixUtil.getImaginaryPartElements(a);
    final int rowSize = aRe.length;
    final int columnSize = rowSize == 0 ? 0 : aRe[0].length;

    final RS unit = aRe[0][0].createUnit();

    final RS[][] qRe = GridUtil.createZero(unit, rowSize, rowSize);
    final RS[][] qIm = GridUtil.createZero(unit, rowSize, rowSize);
    final RS[][] rRe = GridUtil.createZero(unit, rowSize, columnSize);
    final RS[][] rIm = GridUtil.createZero(unit, rowSize, columnSize);
    final int[][] p = new int[columnSize][columnSize];

    decompose(aRe, aIm, qRe, qIm, rRe, rIm, p, false);

    final CS[][] q = qRe[0][0].createComplexArray(qRe,qIm);
    final CS[][] r = rRe[0][0].createComplexArray(rRe, rIm);

    return new QRDecompositionElements<>(q, r);
  }

  /**
   * 複素行列の並べ替え付きQR分解を返します。
   * 
   * <p>複素行列をA、ユニタリ行列をQ、上三角行列をR、並べ替え行列をPとすると、 これらの行列の間には、
   * 
   * <blockquote> A * P = Q * R </blockquote>
   * 
   * <blockquote> Q<sup>#</sup> * Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param a 複素行列
   * @return QR分解の結果
   */
  public QRDecompositionElements<CS,CM> decomposeWithPermutation(final CS[][] a) {
    final RS[][] aRe = AbstractNumericalMatrixUtil.getRealPartElements(a);
    final RS[][] aIm = AbstractNumericalMatrixUtil.getImaginaryPartElements(a);
    final int rowSize = aRe.length;
    final int columnSize = rowSize == 0 ? 0 : aRe[0].length;

    final RS[][] qRe = GridUtil.createZero(aRe, rowSize, rowSize);
    final RS[][] qIm = GridUtil.createZero(aRe, rowSize, rowSize);
    final RS[][] rRe = GridUtil.createZero(aRe, rowSize, columnSize);
    final RS[][] rIm = GridUtil.createZero(aRe, rowSize, columnSize);
    final int[][] p = new int[columnSize][columnSize];

    decompose(aRe, aIm, qRe, qIm, rRe, rIm, p, true);

    final CS[][] q = qRe[0][0].createComplexArray(qRe, qIm);
    final CS[][] r = rRe[0][0].createComplexArray(rRe, rIm);

    return new QRDecompositionElements<>(q, r, p);
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
  private void decompose(final RS[][] ar, final RS[][] ai, final RS[][] qr, final RS[][] qi, final RS[][] rr, final RS[][] ri, final int[][] p, final boolean isPDesired) {
    final int rowSize = ar.length;
    final int columnSize = rowSize == 0 ? 0 : ar[0].length;

    if (rowSize < columnSize) {
      /*
       * a = [a2 | x] a2 * p2 = q2 * r2
       * 
       * [a2 | x] [[p2 Z] = q * [r2 | q'*x] [Z I]]
       */

      final RS[][] a2r = GridUtil.getSubMatrix(ar, 0, rowSize - 1, 0, rowSize - 1);
      final RS[][] a2i = GridUtil.getSubMatrix(ai, 0, rowSize - 1, 0, rowSize - 1);
      final RS[][] q2r = GridUtil.createZero(ar, rowSize, rowSize);
      final RS[][] q2i = GridUtil.createZero(ar, rowSize, rowSize);
      final RS[][] r2r = GridUtil.createZero(ar, rowSize, rowSize);
      final RS[][] r2i = GridUtil.createZero(ar, rowSize, rowSize);
      final int[][] p2 = new int[rowSize][rowSize];

      decompose(a2r, a2i, q2r, q2i, r2r, r2i, p2, isPDesired);

      GridUtil.copy(q2r, qr);
      GridUtil.copy(q2i, qi);

      final RS[][] xr = GridUtil.getSubMatrix(ar, 0, rowSize - 1, rowSize, columnSize - 1);
      final RS[][] xi = GridUtil.getSubMatrix(ai, 0, rowSize - 1, rowSize, columnSize - 1);
      final RS[][] qtr = GridUtil.transpose(qr);
      final RS[][] qti = BaseMatrixUtil.unaryMinus(GridUtil.transpose(qi));
      final RS[][][] x2 = AbstractNumericalMatrixUtil.multiply(qtr, qti, xr, xi);
      final RS[][] x2r = x2[0];
      final RS[][] x2i = x2[1];

      final int row2 = r2r.length - 1;
      final int col2 = r2r[0].length - 1;
      GridUtil.setSubMatrix(rr, 0, row2, 0, col2, r2r);

      final int row3 = r2i.length - 1;
      final int col3 = r2i[0].length - 1;
      GridUtil.setSubMatrix(ri, 0, row3, 0, col3, r2i);

      final int row4 = x2r.length - 1;
      final int col4 = rowSize + x2r[0].length - 1;
      GridUtil.setSubMatrix(rr, 0, row4, rowSize, col4, x2r);

      final int row5 = x2i.length - 1;
      final int col5 = rowSize + x2i[0].length - 1;
      GridUtil.setSubMatrix(ri, 0, row5, rowSize, col5, x2i);

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
  private void decomposeWithPermutation(final RS[][] ar, final RS[][] ai, final RS[][] qr, final RS[][] qi, final RS[][] rr, final RS[][] ri, final int[] piv, final int[] pivotCount, final boolean isPDesired) {
    int k;
    RS tau;

    final int rowSize = ar.length;
    final int columnSize = rowSize == 0 ? 0 : ar[0].length;

    final RS unit = ar[0][0].createUnit();

    final RS[][] a2r = GridUtil.clone(ar);
    final RS[][] a2i = GridUtil.clone(ai);
    final RS[] tm =  GridUtil.createZero(ar[0][0], columnSize);

    if (isPDesired) {
      for (int j = 1; j <= columnSize; j++) {
        final RS[][] tmp1r = GridUtil.getSubMatrix(a2r, 0, rowSize - 1, j - 1, j - 1);
        final RS[][] tmp1i = GridUtil.getSubMatrix(a2i, 0, rowSize - 1, j - 1, j - 1);
        final RS dd = AbstractNumericalMatrixUtil.frobNorm(tmp1r, tmp1i);
        tm[j - 1] = dd.multiply(dd);
      }

      tau = tm[0];
      k = 1;
      for (int i = 2; i <= columnSize; i++) {
        if (tau.isLessThan(tm[i - 1])) {
          tau = tm[i - 1];
          k = i;
        }
      }
    } else {
      k = 1;
      tau = unit.createUnit(); /* dummy */
    }

    int rk = 0;
    while (tau.isZero() == false) {
      rk++;

      if (isPDesired) {
        piv[rk - 1] = k;

        GridUtil.exchangeColumn(a2r, rk - 1, k - 1);
        GridUtil.exchangeColumn(a2i, rk - 1, k - 1);
        RS dd = tm[rk - 1];
        tm[rk - 1] = tm[k - 1];
        tm[k - 1] = dd;
      }
      final List<RS[][]> v = ComplexHouseHolderUtil.houseHolderVector(GridUtil.getSubMatrix(a2r, rk - 1, rowSize - 1, rk - 1, rk - 1), GridUtil.getSubMatrix(a2i, rk - 1, rowSize - 1, rk - 1, rk - 1), 1);
      final RS[][] vr = v.get(0);
      final RS[][] vi = v.get(1);

      final RS[][] tmp1r = GridUtil.getSubMatrix(a2r, rk - 1, rowSize - 1, rk - 1, columnSize - 1);
      final RS[][] tmp1i = GridUtil.getSubMatrix(a2i, rk - 1, rowSize - 1, rk - 1, columnSize - 1);
      final List<RS[][]> mul = ComplexHouseHolderUtil.multiplyHouseHolderFromLeft(tmp1r, tmp1i, vr, vi);
      final RS[][] mul0 = mul.get(0);
      final RS[][] mul1 = mul.get(1);
      

      final int row2 = (rk - 1) + mul0.length - 1;
      final int col2 = (rk - 1) + mul0[0].length - 1;
      GridUtil.setSubMatrix(a2r, rk - 1, row2, rk - 1, col2, mul0);

      final int row3 = (rk - 1) + mul1.length - 1;
      final int col3 = (rk - 1) + mul1[0].length - 1;
      GridUtil.setSubMatrix(a2i, rk - 1, row3, rk - 1, col3, mul1);

      if (rk == rowSize) {
        break;
      }

      final RS d1 = a2r[rk - 1][rk - 1];
      final RS d2 = a2i[rk - 1][rk - 1];

      final int row4 = (rk - 1) + vr.length - 1;
      final int col4 = (rk - 1) + vr[0].length - 1;
      GridUtil.setSubMatrix(a2r, rk - 1, row4, rk - 1, col4, vr);

      final int row5 = (rk - 1) + vi.length - 1;
      final int col5 = (rk - 1) + vi[0].length - 1;
      GridUtil.setSubMatrix(a2i, rk - 1, row5, rk - 1, col5, vi);
      a2r[rk - 1][rk - 1] = d1;
      a2i[rk - 1][rk - 1] = d2;

      for (int i = rk + 1; i <= columnSize; i++) {
        tm[i - 1] = tm[i - 1].subtract((a2r[rk - 1][i - 1].multiply(a2r[rk - 1][i - 1]).add(a2i[rk - 1][i - 1].multiply(a2i[rk - 1][i - 1]))));
      }

      if (rk < columnSize) {
        if (isPDesired) {
          tau = tm[rk];
          k = rk + 1;
          for (int i = rk + 2; i <= columnSize; i++) {
            if (tau.isLessThan(tm[i - 1])) {
              tau = tm[i - 1];
              k = i;
            }
          }
        }
      } else {
        tau = unit.createZero();
      }
    }

    GridUtil.setZero(rr);
    GridUtil.setZero(ri);

    for (int i = 1; i <= rowSize; i++) {
      for (int j = i; j <= columnSize; j++) {
        rr[i - 1][j - 1] = a2r[i - 1][j - 1];
        ri[i - 1][j - 1] = a2i[i - 1][j - 1];
      }
    }

    final RS[][] q2r = BaseMatrixUtil.createUnit(ar, rowSize, rowSize);
    final RS[][] q2i = GridUtil.createZero(ar, rowSize, rowSize);

    for (int j = columnSize; j >= 1; j--) {
      final RS[][] vr = GridUtil.getSubMatrix(a2r, j - 1, rowSize - 1, j - 1, j - 1);
      final RS[][] vi = GridUtil.getSubMatrix(a2i, j - 1, rowSize - 1, j - 1, j - 1);
      vr[0][0] = unit.createUnit();
      vi[0][0] = unit.createZero();
      final List<RS[][]> tmp1 = ComplexHouseHolderUtil.multiplyHouseHolderFromLeft(GridUtil.getSubMatrix(q2r, j - 1, rowSize - 1, j - 1, rowSize - 1), GridUtil.getSubMatrix(q2i, j - 1, rowSize - 1, j - 1, rowSize - 1), vr, vi);
      final RS[][] tmp1r = tmp1.get(0);
      final RS[][] tmp1i = tmp1.get(1);

      final int row2 = (j - 1) + tmp1r.length - 1;
      final int col2 = (j - 1) + tmp1r[0].length - 1;
      GridUtil.setSubMatrix(q2r, j - 1, row2, j - 1, col2, tmp1r);

      final int row3 = (j - 1) + tmp1i.length - 1;
      final int col3 = (j - 1) + tmp1i[0].length - 1;
      GridUtil.setSubMatrix(q2i, j - 1, row3, j - 1, col3, tmp1i);
    }

    GridUtil.copy(q2r, qr);
    GridUtil.copy(q2i, qi);

    pivotCount[0] = rk;
  }

}