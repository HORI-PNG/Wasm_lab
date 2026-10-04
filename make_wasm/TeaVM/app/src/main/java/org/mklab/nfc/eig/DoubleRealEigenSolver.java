/*
 * $Id: DoubleRealEigen.java,v 1.4 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.DoubleMatrixUtil;


/**
 * 倍精度(double)型の実行列の固有値問題を解くためのクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.4 $
 */
public final class DoubleRealEigenSolver {

  /**
   * 倍精度(double)の実行列の固有値と固有ベクトルを返します。
   * 
   * <p>固有値は、実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。 <P>固有ベクトルはノルムが1.0となるよう正規化されます。
   * 
   * @param a 対象となる実行列
   * @return 固有値と固有ベクトル
   */
  public EigenSolutionDoubleElements solve(final double[][] a) {
    final int size = a.length;

    final double[] s = new double[size];
    final double[][] a2 = DoubleMatrixUtil.clone(a);
    final int[] is1 = new int[1];
    final int[] is2 = new int[1];

    DoubleRealEigenSolverUtil.balance(a2, is1, is2, s);

    final double[] ort = new double[size];

    DoubleRealEigenSolverUtil.orthes(a2, is1[0], is2[0], ort);

    final double[][] z = new double[size][size];

    DoubleRealEigenSolverUtil.ortran(a2, is1[0], is2[0], ort, z);

    final double[] wr = new double[size];
    final double[] wi = new double[size];

    final int ierr = DoubleRealEigenSolverUtil.hqr2(a2, is1[0], is2[0], wr, wi, z, false);

    if (ierr != 0) {
      throw new RuntimeException(Messages.getString("DoubleRealEigen.0")); //$NON-NLS-1$
    }

    final double[] valRe = new double[size];
    final double[] valIm = new double[size];
    final double[][] vecRe = new double[size][size];
    final double[][] vecIm = new double[size][size];

    DoubleRealEigenSolverUtil.balbak(is1[0], is2[0], s, size, z);

    DoubleMatrixUtil.copy(wr, valRe);
    DoubleMatrixUtil.copy(wi, valIm);

    for (int i = 1; i <= size; i++) {
      if (valIm[i - 1] == 0.0) {
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

  /**
   * 倍精度(double)の実行列の固有値の実部と虚部をまとめて返します。
   * 
   * <p>固有値は、実部の降順に並べられます。
   * 
   * @param a 対象となる実行列
   * 
   * @return double[][] {valr, vali}
   */
  public double[][] getEigenValue(final double[][] a) {
    final int size = a.length;

    final int[] is1 = new int[1];
    final int[] is2 = new int[1];
    final double[] s = new double[size];
    final double[][] a2 = DoubleMatrixUtil.clone(a);

    DoubleRealEigenSolverUtil.balance(a2, is1, is2, s);

    final double[] ort = new double[size];

    DoubleRealEigenSolverUtil.orthes(a2, is1[0], is2[0], ort);

    final double[] wr = new double[size];
    final double[] wi = new double[size];

    final int errorCode = DoubleRealEigenSolverUtil.hqr(a2, is1[0], is2[0], wr, wi);

    if (errorCode != 0) {
      throw new RuntimeException(Messages.getString("DoubleRealEigen.1")); //$NON-NLS-1$
    }

    final double[] valRe = new double[size];
    final double[] valIm = new double[size];

    DoubleMatrixUtil.copy(wr, valRe);
    DoubleMatrixUtil.copy(wi, valIm);

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

  /**
   * 倍精度(double)の実行列の固有ベクトルの実部と虚部をまとめて返します。
   * 
   * <p>固有値は、実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。 <P>固有ベクトルはノルムが1.0となるよう正規化されます。
   * 
   * @param a 対象となる実行列
   * 
   * @return double[][][] {vecr, veci}
   */
  public double[][][] getEigenVector(final double[][] a) {
    EigenSolutionDoubleElements eig = solve(a);
    final double[][] vecRe = eig.getReVector();
    final double[][] vecIm = eig.getImVector();
    return new double[][][] {vecRe, vecIm};
  }
}