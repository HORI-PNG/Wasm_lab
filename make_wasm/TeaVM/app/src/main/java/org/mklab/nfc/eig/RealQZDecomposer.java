/*
 * $Id: RealQzDecomposition.java,v 1.5 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.GridUtil;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 実行列に関するQZ分解を行うためのクラスです。
 * 
 * @author koga
 * @version $Revision: 1.5 $
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public final class RealQZDecomposer<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {
  

  /**
   * 2個の実正方行列AとBに関するQZ分解を行い、上三角行列 AA と BB、変換のための行列 QとZ、一般化固有ベクトルからなる行列 Xを返します。
   * 
   * <p>これらの行列の間には、
   * 
   * <blockquote> A = Q * AA * Z </blockquote>
   * 
   * <blockquote> B = Q * BB * Z </blockquote>
   * 
   * <blockquote> Q <sup>T </sup>* Q =I </blockquote>
   * 
   * <blockquote> Z <sup>T </sup>* Z = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param a 対象となる正方行列
   * @param b aと同サイズの対象となる正方行列
   * @return QZ分解の結果
   */
  public QZDecompositionElements<S,M> decompose(final S[][] a, final S[][] b) {
    final int size = a.length;

    final S[][] aa = GridUtil.createZero(a, size, size);
    final S[][] bb = GridUtil.createZero(a, size, size);
    final S[][] q = GridUtil.createZero(a, size, size);
    final S[][] z = GridUtil.createZero(a, size, size);
    final S[][] xRe = GridUtil.createZero(a, size, size);
    final S[][] xIm = GridUtil.createZero(a, size, size);

    decompose(a, b, aa, bb, q, z, xRe, xIm);

    return new QZDecompositionElements<>(aa, bb, q, z, xRe, xIm);
  }

  /**
   * a(擬似上三角行列)とb(上三角行列)のQZ分解を求めます。
   * 
   * @param a a行列
   * @param b b行列
   * @param aa a行列に対応する上三角行列
   * @param bb b行列に対応する上三角行列
   * @param q QZ分解のQ行列
   * @param z QZ分解のZ行列
   * @param xRe 一般化固有ベクトルの実部
   * @param xIm 一般化固有ベクトルの虚部
   */
  private void decompose(final S[][] a, final S[][] b, final S[][] aa, final S[][] bb, final S[][] q, final S[][] z, final S[][] xRe, final S[][] xIm) {
    final int size = a.length;

    final S unit = a[0][0].createUnit();

    final S[][] a2 = GridUtil.clone(a);
    final S[][] b2 = GridUtil.clone(b);
    final S[][] qt = GridUtil.createZero(a, size, size);

    RealGeneralizedEigenSolverUtil.qzhes(a2, b2, qt, z, true, true);

    final int ierr = RealGeneralizedEigenSolverUtil.qzit(a2, b2, qt, z, unit.getMachineEpsilon(), true, true);

    if (ierr != 0) {
      throw new RuntimeException(Messages.getString("RealQzDecomposition.0")); //$NON-NLS-1$
    }

    final S[] alfr = GridUtil.createZero(a[0], size);
    final S[] alfi = GridUtil.createZero(a[0], size);
    final S[] beta = GridUtil.createZero(a[0], size);

    RealGeneralizedEigenSolverUtil.qzval(a2, b2, qt, z, alfr, alfi, beta, true, true);

    GridUtil.transpose(qt, q);

    for (int i = 1; i <= size; i++) {
      for (int j = 1; j <= size; j++) {
        if (i <= j + 1) {
          aa[i - 1][j - 1] = a2[i - 1][j - 1];
        } else {
          aa[i - 1][j - 1] = unit.createZero();
        }

        if (i <= j) {
          bb[i - 1][j - 1] = b2[i - 1][j - 1];
        } else {
          bb[i - 1][j - 1] = unit.createZero();
        }
      }
    }

    final S[][] z2 = GridUtil.clone(z);
    RealGeneralizedEigenSolverUtil.qzvec(a2, b2, z2, alfr, alfi, beta);

    final S[] valRe = GridUtil.createZero(a[0], size);
    final S[] valIm = GridUtil.createZero(a[0], size);

    /* 一般化固有値 */
    for (int i = 0; i < size; i++) {
      // ar = *(alfr + i);
      final S ar = alfr[i];
      final S ai = alfi[i];
      final S be = beta[i];

      if (be.isZero()) {
        if (ar.isZero()) {
          valRe[i] = unit.getNaN();
        } else {
          valRe[i] = unit.getInfinity();
        }
        if (ai.isZero()) {
          valIm[i] = unit.getNaN();
        } else {
          valIm[i] = unit.getInfinity();
        }
      } else {
        valRe[i] = ar.divide(be);
        valIm[i] = ai.divide(be);
        if (valIm[i].abs().isLessThan(unit.getMachineEpsilon())) {
          valIm[i] = unit.createZero();
        }
      }
    }

    // zz2 = z2->elm.r;
    /* 一般化固有ベクトル */
    for (int i = 1; i <= size; i++) {
      if (valIm[i - 1].isZero() || valIm[i - 1].isInfinite() || valIm[i - 1].isNaN()) {
        for (int j = 1; j <= size; j++) {
          xRe[j - 1][i - 1] = z2[j - 1][i - 1];
          xIm[j - 1][i - 1] = unit.createZero();
        }
      } else {
        for (int j = 1; j <= size; j++) {
          xRe[j - 1][i - 1] = z2[j - 1][i - 1];
          xIm[j - 1][i - 1] = z2[j - 1][i];

          xRe[j - 1][i] = z2[j - 1][i - 1];
          xIm[j - 1][i] = z2[j - 1][i].unaryMinus();
        }
        i++;
      }
    }

    RealEigenSolverUtil.normalizeVector(valRe, valIm, xRe, xIm);

    /*
     * Sort eigenvalues with respect to the imaginary part of them so that the
     * one which has plus imaginary part comes first than the complex conjugate
     * one.
     */
    for (int i = 1; i < size; i++) {
      for (int j = 1; j < size; j++) {
        if (valIm[j-1].isInfinite() || valIm[j-1].isNaN() || valIm[j].isInfinite() || valIm[j].isNaN()) {
          continue;
        }
        
        if (valIm[j - 1].isLessThan(valIm[j])) {
          final S tmpRe = valRe[j - 1];
          final S tmpIm = valIm[j - 1];
          valRe[j - 1] = valRe[j];
          valIm[j - 1] = valIm[j];
          valRe[j] = tmpRe;
          valIm[j] = tmpIm;
          GridUtil.exchangeColumn(xRe, j - 1, j);
          GridUtil.exchangeColumn(xIm, j - 1, j);
        }
      }
    }

    for (int i = 1; i < size; i++) {
      for (int j = 1; j < size; j++) {
        if (valRe[j-1].isInfinite() || valRe[j-1].isNaN() || valRe[j].isInfinite() || valRe[j].isNaN()) {
          continue;
        }
        
        if (valRe[j - 1].isLessThan(valRe[j])) {
          final S tmpRe = valRe[j - 1];
          final S tmpIm = valIm[j - 1];
          valRe[j - 1] = valRe[j];
          valIm[j - 1] = valIm[j];
          valRe[j] = tmpRe;
          valIm[j] = tmpIm;
          GridUtil.exchangeColumn(xRe, j - 1, j);
          GridUtil.exchangeColumn(xIm, j - 1, j);
        }
      }
    }
  }
}