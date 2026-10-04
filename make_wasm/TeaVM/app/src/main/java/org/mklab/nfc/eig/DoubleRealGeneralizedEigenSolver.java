/*
 * $Id: DoubleRealGeneralizedEigen.java,v 1.4 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.DoubleMatrixUtil;
import org.mklab.nfc.scalar.DoubleNumberUtil;


/**
 * 倍精度(double)型の実行列に関する一般化固有値問題を解くためのクラスです。
 * 
 * @author koga
 * @version $Revision: 1.4 $
 */
public final class DoubleRealGeneralizedEigenSolver {

  /*
   * a: real matrix N*N b: real matrix N*N val: complex matrix N*1 vec: complex
   * matrix N*N
   */

  /**
   * 倍精度(double)の実行列aと実行列bの一般化固有値と一般化固有ベクトルを返します。
   * 
   * <p>固有値は、実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。 <P>固有ベクトルはノルムが1.0となるよう正規化されます。
   * 
   * @param a 対象となる行列
   * @param b 対象となる行列
   * @return 一般化固有値と一般化固有ベクトル
   */
  public EigenSolutionDoubleElements solve(final double[][] a, final double[][] b) {
    final int size = a.length;

    final double[][] a2 = DoubleMatrixUtil.clone(a);
    final double[][] b2 = DoubleMatrixUtil.clone(b);
    final double[][] z = new double[size][size];
    final double[][] q = new double[size][size];

    DoubleRealGeneralizedEigenSolverUtil.qzhes(a2, b2, q, z, true, false);

    final int ierr = DoubleRealGeneralizedEigenSolverUtil.qzit(a2, b2, q, z, DoubleNumberUtil.EPS, true, false);

    if (ierr != 0) {
      throw new RuntimeException(Messages.getString("DoubleRealGeneralizedEigen.0")); //$NON-NLS-1$
    }

    final double[] alfr = new double[size];
    final double[] alfi = new double[size];
    final double[] beta = new double[size];

    DoubleRealGeneralizedEigenSolverUtil.qzval(a2, b2, q, z, alfr, alfi, beta, true, false);
    DoubleRealGeneralizedEigenSolverUtil.qzvec(a2, b2, z, alfr, alfi, beta);

    final double[] valRe = new double[size];
    final double[] valIm = new double[size];

    for (int i = 0; i < size; i++) {
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

    final double[][] vecRe = new double[size][size];
    final double[][] vecIm = new double[size][size];

    for (int i = 1; i <= size; i++) {
      if (valIm[i - 1] == 0.0 || Double.isInfinite(valIm[i - 1]) || Double.isNaN(valIm[i - 1])) {
        for (int j = 1; j <= size; j++) {
          vecRe[j - 1][i - 1] = z[j - 1][i - 1];
          vecIm[j - 1][i - 1] = 0.0;
        }
      } else {
        for (int j = 1; j <= size; j++) {
          vecRe[j - 1][i - 1] = z[j - 1][i - 1];
          vecIm[j - 1][i - 1] = z[j - 1][i];

          vecRe[j - 1][i] = z[j - 1][i - 1];
          vecIm[j - 1][i] = -z[j - 1][i];
        }
        i++;
      }
    }

    DoubleRealEigenSolverUtil.normalizeVector(valRe, valIm, vecRe, vecIm);

    /*
     * Sort eigenvalues with respect to the imaginary part of them so that the
     * one which has plus imaginary part comes first than the complex conjugate
     * one.
     */
    for (int i = 1; i < size; i++) {
      for (int j = 1; j < size; j++) {
        if (valIm[j - 1] < valIm[j]) {
          final double tmpRe = valRe[j - 1];
          final double tmpIm = valIm[j - 1];
          valRe[j - 1] = valRe[j];
          valIm[j - 1] = valIm[j];
          valRe[j] = tmpRe;
          valIm[j] = tmpIm;
          DoubleMatrixUtil.exchangeColumn(vecRe, j - 1, j);
          DoubleMatrixUtil.exchangeColumn(vecIm, j - 1, j);
        }
      }
    }

    for (int i = 1; i < size; i++) {
      for (int j = 1; j < size; j++) {
        if (valRe[j - 1] < valRe[j]) {
          final double tmpRe = valRe[j - 1];
          final double tmpIm = valIm[j - 1];
          valRe[j - 1] = valRe[j];
          valIm[j - 1] = valIm[j];
          valRe[j] = tmpRe;
          valIm[j] = tmpIm;
          DoubleMatrixUtil.exchangeColumn(vecRe, j - 1, j);
          DoubleMatrixUtil.exchangeColumn(vecIm, j - 1, j);
        }
      }
    }

    return new EigenSolutionDoubleElements(valRe, valIm, vecRe, vecIm);
  }

  /*
   * val: complex matrix N x 1 a: real matrix N x N b: real matrix N x N
   */

  /**
   * 倍精度(double)の実行列の一般化固有値を返します。
   * 
   * <p>固有値は、実部の降順に並べられます。
   * 
   * @param a 対象となる行列
   * @param b 対象となる行列
   * @return 一般化固有値の実部と虚部
   */
  public double[][] getEigenValue(final double[][] a, final double[][] b) {
    final int size = a.length;

    final double[][] a2 = DoubleMatrixUtil.clone(a);
    final double[][] b2 = DoubleMatrixUtil.clone(b);
    final double[][] z = new double[size][size];
    final double[][] q = new double[size][size];

    DoubleRealGeneralizedEigenSolverUtil.qzhes(a2, b2, q, z, false, false);   

    final int ierr = DoubleRealGeneralizedEigenSolverUtil.qzit(a2, b2, q, z, DoubleNumberUtil.EPS, false, false);

    if (ierr != 0) {
      throw new RuntimeException(Messages.getString("DoubleRealGeneralizedEigen.1")); //$NON-NLS-1$
    }

    final double[] alfr = new double[size];
    final double[] alfi = new double[size];
    final double[] beta = new double[size];
    final double[] valRe = new double[size];
    final double[] valIm = new double[size];

    DoubleRealGeneralizedEigenSolverUtil.qzval(a2, b2, q, z, alfr, alfi, beta, false, false);

    for (int i = 0; i < size; i++) {
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

    /*
     * Sort eigenvalues with respect to the imaginary part of them so that the
     * one which has plus imaginary part comes first than the complex conjugate
     * one.
     */
    for (int i = 1; i < size; i++) {
      for (int j = 1; j < size; j++) {
        if (valIm[j - 1] < valIm[j]) {
          final double tmpRe = valRe[j - 1];
          final double tmpIm = valIm[j - 1];
          valRe[j - 1] = valRe[j];
          valIm[j - 1] = valIm[j];
          valRe[j] = tmpRe;
          valIm[j] = tmpIm;
        }
      }
    }

    for (int i = 1; i < size; i++) {
      for (int j = 1; j < size; j++) {
        if (valRe[j - 1] < valRe[j]) {
          final double tmpRe = valRe[j - 1];
          final double tmpIm = valIm[j - 1];
          valRe[j - 1] = valRe[j];
          valIm[j - 1] = valIm[j];
          valRe[j] = tmpRe;
          valIm[j] = tmpIm;
        }
      }
    }

    return new double[][] {valRe, valIm};
  }

  /*
   * a: real matrix n x n b: real matrix n x n vec: complex matrix n x n
   */
  /**
   * 倍精度(double)の実行列の一般化固有ベクトルを返します。
   * 
   * <p>固有値は、実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。 <P>固有ベクトルはノルムが1.0となるよう正規化されます。
   * 
   * @param a 対象となる行列
   * @param b 対象となる行列
   * @return 一般化固有ベクトルの実部と虚部
   */
  public double[][][] getEigenVector(final double[][] a, final double[][] b) {
    EigenSolutionDoubleElements eig = solve(a, b);
    final double[][] vecRe = eig.getReVector();
    final double[][] vecIm = eig.getImVector();
    return new double[][][] {vecRe, vecIm};
  }
}