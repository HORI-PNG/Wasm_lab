/*
 * $Id: RealGeneralizedEigen.java,v 1.5 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.GridUtil;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * 実行列に関する一般化固有値問題を解くためのクラスです。
 * 
 * @author koga
 * @version $Revision: 1.5 $
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 */
public final class RealGeneralizedEigenSolver<RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> {

  /*
   * a: real matrix N*N b: real matrix N*N val: complex matrix N*1 vec: complex
   * matrix N*N
   */

  /**
   * 実行列aと実行列bの一般化固有値と一般化固有ベクトルを返します。
   * 
   * <p>固有値は、実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。 <P>固有ベクトルはノルムが1.0となるよう正規化されます。
   * 
   * @param a 対象となる実行列
   * @param b 対象となる実行列
   * @return 一般化固有値と一般化固有ベクトル
   */
  public EigenSolutionElements<RS,RM,CS,CM> solve(final RS[][] a, final RS[][] b) {
    final int size = a.length;

    final RS unit = a[0][0].createUnit();

    final RS[][] a2 = GridUtil.clone(a);
    final RS[][] b2 = GridUtil.clone(b);
    final RS[][] z = GridUtil.createZero(a, size, size);
    final RS[][] q = GridUtil.createZero(a, size, size);

    RealGeneralizedEigenSolverUtil.qzhes(a2, b2, q, z, true, false);

    final int ierr = RealGeneralizedEigenSolverUtil.qzit(a2, b2, q, z, unit.getMachineEpsilon(), true, false);

    if (ierr != 0) {
      throw new RuntimeException(Messages.getString("RealGeneralizedEigen.0")); //$NON-NLS-1$
    }

    final RS[] alfr = GridUtil.createZero(a[0], size);
    final RS[] alfi = GridUtil.createZero(a[0], size);
    final RS[] beta = GridUtil.createZero(a[0], size);

    RealGeneralizedEigenSolverUtil.qzval(a2, b2, q, z, alfr, alfi, beta, true, false);
    RealGeneralizedEigenSolverUtil.qzvec(a2, b2, z, alfr, alfi, beta);

    final RS[] valRe = GridUtil.createZero(a[0], size);
    final RS[] valIm = GridUtil.createZero(a[0], size);

    for (int i = 0; i < size; i++) {
      final RS ar = alfr[i];
      final RS ai = alfi[i];
      final RS be = beta[i];

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

    final RS[][] vecRe = GridUtil.createZero(a, size, size);
    final RS[][] vecIm = GridUtil.createZero(a, size, size);

    for (int i = 1; i <= size; i++) {
      if (valIm[i - 1].isZero() || valIm[i - 1].isInfinite() || valIm[i - 1].isNaN()) {
        for (int j = 1; j <= size; j++) {
          vecRe[j - 1][i - 1] = z[j - 1][i - 1];
          vecIm[j - 1][i - 1] = unit.createZero();
        }
      } else {
        for (int j = 1; j <= size; j++) {
          vecRe[j - 1][i - 1] = z[j - 1][i - 1];
          vecIm[j - 1][i - 1] = z[j - 1][i];

          vecRe[j - 1][i] = z[j - 1][i - 1];
          vecIm[j - 1][i] = z[j - 1][i].unaryMinus();
        }
        i++;
      }
    }

    RealEigenSolverUtil.normalizeVector(valRe, valIm, vecRe, vecIm);

    /*
     * Sort eigenvalues with respect to the imaginary part of them so that the
     * one which has plus imaginary part comes first than the complex conjugate
     * one.
     */
    for (int i = 1; i < size; i++) {
      for (int j = 1; j < size; j++) {
        if (valIm[j - 1].isLessThan(valIm[j])) {
          final RS tmpRe = valRe[j - 1];
          final RS tmpIm = valIm[j - 1];
          valRe[j - 1] = valRe[j];
          valIm[j - 1] = valIm[j];
          valRe[j] = tmpRe;
          valIm[j] = tmpIm;
          GridUtil.exchangeColumn(vecRe, j - 1, j);
          GridUtil.exchangeColumn(vecIm, j - 1, j);
        }
      }
    }

    for (int i = 1; i < size; i++) {
      for (int j = 1; j < size; j++) {
        if (valRe[j - 1].isLessThan(valRe[j])) {
          final RS tmpRe = valRe[j - 1];
          final RS tmpIm = valIm[j - 1];
          valRe[j - 1] = valRe[j];
          valIm[j - 1] = valIm[j];
          valRe[j] = tmpRe;
          valIm[j] = tmpIm;
          GridUtil.exchangeColumn(vecRe, j - 1, j);
          GridUtil.exchangeColumn(vecIm, j - 1, j);
        }
      }
    }

    return new EigenSolutionElements<>(valRe, valIm, vecRe, vecIm);
  }

  /*
   * val: complex matrix N x 1 a: real matrix N x N b: real matrix N x N
   */

  /**
   * 実行列の一般化固有値を返します。
   * 
   * <p>固有値は、実部の降順に並べられます。
   * 
   * @param a 対象となる実行列
   * @param b 対象となる実行列
   * @return 実行列の一般化固有値
   */
  public CS[] getEigenValue(final RS[][] a, final RS[][] b) {
    final int size = a.length;

    final RS unit = a[0][0].createUnit();

    final RS[][] a2 = GridUtil.clone(a);
    final RS[][] b2 = GridUtil.clone(b);
    final RS[][] z = GridUtil.createZero(a, size, size);
    final RS[][] q = GridUtil.createZero(a, size, size);

    RealGeneralizedEigenSolverUtil.qzhes(a2, b2, q, z, false, false);

    final int ierr = RealGeneralizedEigenSolverUtil.qzit(a2, b2, q, z, unit.getMachineEpsilon(), false, false);
    
    if (ierr != 0) {
      throw new RuntimeException(Messages.getString("RealGeneralizedEigen.1")); //$NON-NLS-1$
    }

    final RS[] alfr = GridUtil.createZero(a[0], size);
    final RS[] alfi = GridUtil.createZero(a[0], size);
    final RS[] beta = GridUtil.createZero(a[0], size);
    final RS[] valRe = GridUtil.createZero(a[0], size);
    final RS[] valIm = GridUtil.createZero(a[0], size);

    RealGeneralizedEigenSolverUtil.qzval(a2, b2, q, z, alfr, alfi, beta, false, false);

    for (int i = 0; i < size; i++) {
      final RS ar = alfr[i];
      final RS ai = alfi[i];
      final RS be = beta[i];

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

    /*
     * Sort eigenvalues with respect to the imaginary part of them so that the
     * one which has plus imaginary part comes first than the complex conjugate
     * one.
     */
    for (int i = 1; i < size; i++) {
      for (int j = 1; j < size; j++) {
        if (valIm[j - 1].isLessThan(valIm[j])) {
          final RS tmpRe = valRe[j - 1];
          final RS tmpIm = valIm[j - 1];
          valRe[j - 1] = valRe[j];
          valIm[j - 1] = valIm[j];
          valRe[j] = tmpRe;
          valIm[j] = tmpIm;
        }
      }
    }

    for (int i = 1; i < size; i++) {
      for (int j = 1; j < size; j++) {
        if (valRe[j - 1].isLessThan(valRe[j])) {
          final RS tmpRe = valRe[j - 1];
          final RS tmpIm = valIm[j - 1];
          valRe[j - 1] = valRe[j];
          valIm[j - 1] = valIm[j];
          valRe[j] = tmpRe;
          valIm[j] = tmpIm;
        }
      }
    }

    return valRe[0].createComplexArray(valRe, valIm);
  }

  /*
   * a: real matrix n x n b: real matrix n x n vec: complex matrix n x n
   */
  /**
   * 実行列の一般化固有ベクトルを返します。
   * 
   * <p>固有値は、実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。 <P>固有ベクトルはノルムが1.0となるよう正規化されます。
   * 
   * @param a 対象となる実行列
   * @param b 対象となる実行列
   * @return 実行列の一般化固有ベクトル
   */
  public CS[][] getEigenVector(final RS[][] a, final RS[][] b) {
    final EigenSolutionElements<RS,RM,CS,CM> eig = solve(a, b);
    final RS[][] vecRe = eig.getReVector();
    final RS[][] vecIm = eig.getImVector();
    return vecRe[0][0].createComplexArray(vecRe, vecIm);
  }
}