/*
 * $Id: DoubleComplexEigen.java,v 1.4 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.DoubleMatrixUtil;


/**
 * 倍精度(double)型の複素行列の固有値問題を解くためのクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.4 $
 */
public final class DoubleComplexEigenSolver {

  /**
   * 倍精度(double)の複素行列の固有値と固有ベクトルを返します。
   * 
   * <p>固有値は、実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。 
   * 
   * <P>固有ベクトルはノルムが1.0となるよう正規化されます。
   * 
   * @param aRe 対象となる複素行列の実部
   * @param aIm 対象となる複素行列の虚部
   * @return 固有値と固有ベクトル
   */
  public EigenSolutionDoubleElements solve(final double[][] aRe, final double[][] aIm) {
    final int size = aRe.length;

    final double[][] a2Re = DoubleMatrixUtil.clone(aRe);
    final double[][] a2Im = DoubleMatrixUtil.clone(aIm);
    final double[] fv1 = new double[size];
    final int[] low = new int[1];
    int[] igh = new int[1];

    DoubleComplexEigenSolverUtil.cbal(a2Re, a2Im, low, igh, fv1);

    final double[] fv2 = new double[size];
    final double[] fv3 = new double[size];

    DoubleComplexEigenSolverUtil.corth(a2Re, a2Im, low[0], igh[0], fv2, fv3);

    final double[][] vecRe = new double[size][size];
    final double[][] vecIm = new double[size][size];
    final double[] wRe = new double[size];
    final double[] wIm = new double[size];

    final int errorCode = DoubleComplexEigenSolverUtil.comqr2(a2Re, a2Im, low[0], igh[0], fv2, fv3, wRe, wIm, vecRe, vecIm, false);

    if (errorCode != 0) {
      throw new RuntimeException(Messages.getString("DoubleComplexEigen.0")); //$NON-NLS-1$
    }

    DoubleComplexEigenSolverUtil.cbabk2(low[0], igh[0], fv1, a2Re.length, vecRe, vecIm);

    final double[] valRe = new double[size];
    final double[] valIm = new double[size];

    DoubleMatrixUtil.copy(wRe, valRe);
    DoubleMatrixUtil.copy(wIm, valIm);

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
          DoubleMatrixUtil.exchangeColumn(vecRe, j - 1, j); // 03/02/09
          DoubleMatrixUtil.exchangeColumn(vecIm, j - 1, j); // 03/02/09
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
          DoubleMatrixUtil.exchangeColumn(vecRe, j - 1, j); // 03/02/09
          DoubleMatrixUtil.exchangeColumn(vecIm, j - 1, j); // 03/02/09
        }
      }
    }

    return new EigenSolutionDoubleElements(valRe, valIm, vecRe, vecIm);
  }

  /**
   * 倍精度(double)の複素行列の固有値の実部と虚部をまとめて返します。
   * 
   * <p>固有値は、実部の降順に並べられます。
   * 
   * @param aRe 対象となる複素行列の実部
   * @param aIm 対象となる複素行列の虚部
   * @return 固有値の実部と虚部の配列 {valRe, valIm}
   */
  public double[][] getEigenValue(final double[][] aRe, final double[][] aIm) {
    final int size = aRe.length;

    final double[][] a2Re = DoubleMatrixUtil.clone(aRe);
    final double[][] a2Im = DoubleMatrixUtil.clone(aIm);
    final double[] fv1 = new double[size];
    final int[] low = new int[1];
    final int[] igh = new int[1];

    DoubleComplexEigenSolverUtil.cbal(a2Re, a2Im, low, igh, fv1);

    final double[] fv2 = new double[size];
    final double[] fv3 = new double[size];

    DoubleComplexEigenSolverUtil.corth(a2Re, a2Im, low[0], igh[0], fv2, fv3);

    final double[] wRe = new double[size];
    final double[] wIm = new double[size];

    final int errorCode = DoubleComplexEigenSolverUtil.comqr(a2Re, a2Im, low[0], igh[0], wRe, wIm);

    final double[] valRe = new double[size];
    final double[] valIm = new double[size];

    if (errorCode != 0) {
      throw new RuntimeException(Messages.getString("DoubleComplexEigen.1")); //$NON-NLS-1$
    }

    DoubleMatrixUtil.copy(wRe, valRe);
    DoubleMatrixUtil.copy(wIm, valIm);

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
   * 倍精度(double)の複素行列の固有ベクトルの実部と虚部をまとめて返します。
   * 
   * <p>固有値は、実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。 
   * 
   * <P>固有ベクトルはノルムが1.0となるよう正規化されます。
   * 
   * @param aRe 対象となる複素行列の実部
   * @param aIm 対象となる複素行列の虚部
   * @return 固有ベクトルの実部と虚部の配列  {vecRe, vecIm}
   */
  public double[][][] getEigenVector(final double[][] aRe, final double[][] aIm) {
    EigenSolutionDoubleElements eig = solve(aRe, aIm);
    final double[][] vecRe = eig.getReVector();
    final double[][] vecIm = eig.getImVector();
    return new double[][][] {vecRe, vecIm};
  }
}