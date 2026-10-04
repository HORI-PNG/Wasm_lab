/*
 * $Id: DoubleComplexGeneralizedEigen.java,v 1.5 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.DoubleMatrixUtil;


/**
 * 倍精度(double)型の複素行列の一般化固有値問題を解くためのクラスです。
 * 
 * (現在未完成)
 * 
 * @author matsuki
 * @version $Revision: 1.5 $
 */
public final class DoubleComplexGeneralizedEigenSolver {

  /**
   * 倍精度(double)の複素行列の一般化固有値と固有ベクトルを返します。
   * 
   * <p>固有値は、実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。 <P>固有ベクトルはノルムが1.0となるよう正規化されます。
   * 
   * @param ar 複素行列Aの実部
   * @param ai 複素行列Aの虚部
   * @param br 複素行列Bの実部
   * @param bi 複素行列Bの虚部
   * @return 一般化固有値と固有ベクトル
   */
  public EigenSolutionDoubleElements solve(final double[][] ar, final double[][] ai, final double[][] br, final double[][] bi) {
    final int size = ar.length;

    final double[][] a2r = DoubleMatrixUtil.clone(ar);
    final double[][] a2i = DoubleMatrixUtil.clone(ai);
    final double[][] b2r = DoubleMatrixUtil.clone(br);
    final double[][] b2i = DoubleMatrixUtil.clone(bi);
    final double[][] zr = new double[size][size];
    final double[][] zi = new double[size][size];
    final double[][] qtr = new double[size][size];
    final double[][] qti = new double[size][size];

    DoubleComplexGeneralizedEigenSolverUtil.qzHes(a2r, a2i, b2r, b2i, qtr, qti, zr, zi, false, false);

    final double[] tolerance = new double[1];

    final int errorCode1 = DoubleComplexGeneralizedEigenSolverUtil.qzIt(a2r, a2i, b2r, b2i, qtr, qti, zr, zi, false, false, tolerance);

    if (errorCode1 != 0) {
      throw new RuntimeException(Messages.getString("DoubleComplexGeneralizedEigen.2")); //$NON-NLS-1$
    }

    final double[] valRe = new double[size];
    final double[] valIm = new double[size];

    DoubleComplexGeneralizedEigenSolverUtil.qzVal(a2r, a2i, b2r, b2i, valRe, valIm, tolerance[0]);

    final double[][] vecr = new double[size][size];
    final double[][] veci = new double[size][size];

    final int errorCode2 = DoubleComplexGeneralizedEigenSolverUtil.qzVec(ar, ai, br, bi, valRe, valIm, vecr, veci, tolerance[0]);

    if (errorCode2 != 0) {
      throw new RuntimeException(Messages.getString("DoubleComplexGeneralizedEigen.3")); //$NON-NLS-1$
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
          DoubleMatrixUtil.exchangeColumn(vecr, j - 1, j);
          DoubleMatrixUtil.exchangeColumn(veci, j - 1, j);
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
          DoubleMatrixUtil.exchangeColumn(vecr, j - 1, j);
          DoubleMatrixUtil.exchangeColumn(veci, j - 1, j);
        }
      }
    }

    return new EigenSolutionDoubleElements(valRe, valIm, vecr, veci);
  }

  /**
   * 倍精度(double)の複素行列の一般化固有値を返します。
   * 
   * <p>固有値は、実部の降順に並べられます。
   * 
   * @param ar 複素行列Aの実部
   * @param ai 複素行列Aの虚部
   * @param br 複素行列Bの実部
   * @param bi 複素行列Bの虚部
   * @return 一般化固有値 {valRe, valIm}
   */
  public double[][] getEigenValue(final double[][] ar, final double[][] ai, final double[][] br, final double[][] bi) {
    final int size = ar.length;

    final double[][] a2r = DoubleMatrixUtil.clone(ar);
    final double[][] a2i = DoubleMatrixUtil.clone(ai);
    final double[][] b2r = DoubleMatrixUtil.clone(br);
    final double[][] b2i = DoubleMatrixUtil.clone(bi);
    final double[][] zr = new double[size][size];
    final double[][] zi = new double[size][size];
    final double[][] qtr = new double[size][size];
    final double[][] qti = new double[size][size];

    DoubleComplexGeneralizedEigenSolverUtil.qzHes(a2r, a2i, b2r, b2i, qtr, qti, zr, zi, false, false);
    
//    new DoubleComplexMatrix(a2r, a2i).print("a2");
//    new DoubleComplexMatrix(b2r, b2i).print("b2");
    
    final double[] epsb = new double[1];
    final int errorCode = DoubleComplexGeneralizedEigenSolverUtil.qzIt(a2r, a2i, b2r, b2i, qtr, qti, zr, zi, false, false, epsb);

    if (errorCode != 0) {
      throw new RuntimeException(Messages.getString("DoubleComplexGeneralizedEigen.4")); //$NON-NLS-1$
    }

    final double[] valRe = new double[size];
    final double[] valIm = new double[size];

    DoubleComplexGeneralizedEigenSolverUtil.qzVal(a2r, a2i, b2r, b2i, valRe, valIm, epsb[0]);

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
   * 倍精度(double)の複素行列の一般化固有ベクトルを返します。
   * 
   * <p>固有値は、実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。 <P>固有ベクトルはノルムが1.0となるよう正規化されます。
   * 
   * @param ar 複素行列Aの実部
   * @param ai 複素行列Aの虚部
   * @param br 複素行列Bの実部
   * @param bi 複素行列bの虚部
   * @return 一般化固有ベクトル {vecRe, vecIm}
   */
  public double[][][] getEigenVector(final double[][] ar, final double[][] ai, final double[][] br, final double[][] bi) {
    final EigenSolutionDoubleElements eig = solve(ar, ai, br, bi);
    final double[][] vecRe = eig.getReVector();
    final double[][] vecIm = eig.getImVector();
    return new double[][][] {vecRe, vecIm};
  }
}