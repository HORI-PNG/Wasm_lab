/*
 * $Id: DoubleRealQzDecomposition.java,v 1.4 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.DoubleMatrixUtil;
import org.mklab.nfc.scalar.DoubleNumberUtil;


/**
 * 倍精度(double)型の実行列に関するQZ分解を行うためのクラスです。
 * 
 * @author koga
 * @version $Revision: 1.4 $
 */
public final class DoubleRealQZDecomposer {

  /**
   * 2個の倍精度(double)の実正方行列AとBに関するQZ分解を行い、上三角行列 AA と BB、変換のための行列 Qと Z、一般化固有ベクトルからなる行列 X を返します。
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
  public QZDecompositionDoubleRealElements decompose(final double[][] a, final double[][] b) {
    final int size = a.length;

    final double[][] aa = new double[size][size];
    final double[][] bb = new double[size][size];
    final double[][] q = new double[size][size];
    final double[][] z = new double[size][size];
    final double[][] xRe = new double[size][size];
    final double[][] xIm = new double[size][size];

    decompose(a, b, aa, bb, q, z, xRe, xIm);

    return new QZDecompositionDoubleRealElements(aa, bb, q, z, xRe, xIm);
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
  private void decompose(final double[][] a, final double[][] b, final double[][] aa, final double[][] bb, final double[][] q, final double[][] z, final double[][] xRe, final double[][] xIm) {
    final int size = a.length;

    final double[][] a2 = DoubleMatrixUtil.clone(a);
    final double[][] b2 = DoubleMatrixUtil.clone(b);
    final double[][] qt = new double[size][size];

    DoubleRealGeneralizedEigenSolverUtil.qzhes(a2, b2, qt, z, true, true);

    final int errorCode = DoubleRealGeneralizedEigenSolverUtil.qzit(a2, b2, qt, z, DoubleNumberUtil.EPS, true, true);

    if (errorCode != 0) {
      throw new RuntimeException(Messages.getString("DoubleRealQzDecomposition.0")); //$NON-NLS-1$
    }

    final double[] alfr = new double[size];
    final double[] alfi = new double[size];
    final double[] beta = new double[size];

    DoubleRealGeneralizedEigenSolverUtil.qzval(a2, b2, qt, z, alfr, alfi, beta, true, true);

    DoubleMatrixUtil.transpose(qt, q);

    for (int i = 1; i <= size; i++) {
      for (int j = 1; j <= size; j++) {
        if (i <= j + 1) {
          aa[i - 1][j - 1] = a2[i - 1][j - 1];
        } else {
          aa[i - 1][j - 1] = 0.0;
        }

        if (i <= j) {
          bb[i - 1][j - 1] = b2[i - 1][j - 1];
        } else {
          bb[i - 1][j - 1] = 0.0;
        }
      }
    }

    final double[][] z2 = DoubleMatrixUtil.clone(z);
    DoubleRealGeneralizedEigenSolverUtil.qzvec(a2, b2, z2, alfr, alfi, beta);

    final double[] valRe = new double[size];
    final double[] valIm = new double[size];

    /* 一般化固有値 */
    for (int i = 0; i < size; i++) {
      // ar = *(alfr + i);
      final double ar = alfr[i];
      final double ai = alfi[i];
      final double be = beta[i];

      if (be == 0.0) {
        if (ar == 0.0) {
          valRe[i] = Double.NaN;
        } else {
          valRe[i] = Double.POSITIVE_INFINITY;
        }
        if (ai == 0.0) {
          valIm[i] = Double.NaN;
        } else {
          valIm[i] = Double.POSITIVE_INFINITY;
        }
      } else {
        valRe[i] = ar / be;
        valIm[i] = ai / be;
      }
    }
    
    // zz2 = z2->elm.r;
    /* 一般化固有ベクトル */
    for (int i = 1; i <= size; i++) {
      if (valIm[i - 1] == 0.0 || Double.isInfinite(valIm[i - 1]) || Double.isNaN(valIm[i - 1])) {
        for (int j = 1; j <= size; j++) {
          xRe[j - 1][i - 1] = z2[j - 1][i - 1];
          xIm[j - 1][i - 1] = 0.0;
        }
      } else {
        for (int j = 1; j <= size; j++) {
          xRe[j - 1][i - 1] = z2[j - 1][i - 1];
          xIm[j - 1][i - 1] = z2[j - 1][i];

          xRe[j - 1][i] = z2[j - 1][i - 1];
          xIm[j - 1][i] = -z2[j - 1][i];
        }
        i++;
      }
    }

    DoubleRealEigenSolverUtil.normalizeVector(valRe, valIm, xRe, xIm);

    /*
     * Sort eigenvalues with respect to the imaginary part of them so that the
     * one which has plus imaginary part comes first than the complex conjugate
     * one.
     */

    for (int i = 1; i < size; i++) {
      for (int j = 1; j < size; j++) {
        if (Double.isInfinite(valIm[j-1]) || Double.isNaN(valIm[j-1]) || Double.isInfinite(valIm[j]) || Double.isNaN(valIm[j])) {
          continue;
        }
          
        if (valIm[j - 1] < valIm[j]) {
          final double tmpRe = valRe[j - 1];
          final double tmpIm = valIm[j - 1];
          valRe[j - 1] = valRe[j];
          valIm[j - 1] = valIm[j];
          valRe[j] = tmpRe;
          valIm[j] = tmpIm;
          DoubleMatrixUtil.exchangeColumn(xRe, j - 1, j);
          DoubleMatrixUtil.exchangeColumn(xIm, j - 1, j);
        }
      }
    }

    for (int i = 1; i < size; i++) {
      for (int j = 1; j < size; j++) {
        if (Double.isInfinite(valRe[j-1]) || Double.isNaN(valRe[j-1]) || Double.isInfinite(valRe[j]) || Double.isNaN(valRe[j])) {
          continue;
        }
        
        if (valRe[j - 1] < valRe[j]) {
          final double tmpRe = valRe[j - 1];
          final double tmpIm = valIm[j - 1];
          valRe[j - 1] = valRe[j];
          valIm[j - 1] = valIm[j];
          valRe[j] = tmpRe;
          valIm[j] = tmpIm;
          DoubleMatrixUtil.exchangeColumn(xRe, j - 1, j);
          DoubleMatrixUtil.exchangeColumn(xIm, j - 1, j);
        }
      }
    }
  }
}