/*
 * $Id: RealEigen.java,v 1.5 2008/07/16 08:00:36 koga Exp $
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
 * 実行列の固有値問題を解くためのクラスです。
 * 
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @author matsuki
 * @version $Revision: 1.5 $
 */
public final class RealEigenSolver<RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> {

  /**
   * 実行列の固有値と固有ベクトルを返します。
   * 
   * <p>固有値は、実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。 <P>固有ベクトルはノルムが1.0となるよう正規化されます。
   * 
   * @param a 対象となる実行列
   * @return 固有値と固有ベクトル
   */
  public EigenSolutionElements<RS,RM,CS,CM> solve(final RS[][] a) {
    final int size = a.length;

    final RS unit = a[0][0].createUnit();

    final RS[] s = unit.createArray(size);
    final RS[][] a2 = GridUtil.clone(a);
    final int[] is1 = new int[1];
    final int[] is2 = new int[1];

    RealEigenSolverUtil.balance(a2, is1, is2, s);

    final RS[] ort = unit.createArray(size);

    RealEigenSolverUtil.orthes(a2, is1[0], is2[0], ort);

    final RS[][] z = unit.createArray(size, size);

    RealEigenSolverUtil.ortran(a2, is1[0], is2[0], ort, z);

    final RS[] wr = unit.createArray(size);
    final RS[] wi = unit.createArray(size);

    final int ierr = RealEigenSolverUtil.hqr2(a2, is1[0], is2[0], wr, wi, z, false);

    if (ierr != 0) {
      throw new RuntimeException(Messages.getString("RealEigen.0")); //$NON-NLS-1$
    }

    RealEigenSolverUtil.balbak(is1[0], is2[0], s, size, z);

    final RS[] valRe = GridUtil.clone(wr);
    final RS[] valIm = GridUtil.clone(wi);

    final RS[][] vecRe = unit.createArray(size, size);
    final RS[][] vecIm = unit.createArray(size, size);

    for (int i = 1; i <= size; i++) {
      if (valIm[i - 1].isZero()) {
        for (int j = 1; j <= size; j++) {
          vecRe[j - 1][i - 1] = z[j - 1][i - 1].clone();
          vecIm[j - 1][i - 1] = a[0][0].createZero();
        }
      } else {
        for (int j = 1; j <= size; j++) {
          vecRe[j - 1][i - 1] = z[j - 1][i - 1].clone();
          vecIm[j - 1][i - 1] = z[j - 1][i].clone();

          vecRe[j - 1][i] = z[j - 1][i - 1].clone();
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

  /**
   * 実行列の固有値を返します。
   * 
   * <p>固有値は、実部の降順に並べられます。
   * 
   * @param a 対象となる実行列
   * 
   * @return 固有値
   */
  public CS[] getEigenValue(final RS[][] a) {
    final int size = a.length;

    final RS unit = a[0][0].createUnit();

    final int[] is1 = new int[1];
    final int[] is2 = new int[1];
    final RS[] s = unit.createArray(size);
    final RS[][] a2 = GridUtil.clone(a);

    RealEigenSolverUtil.balance(a2, is1, is2, s);

    final RS[] ort = unit.createArray(size);

    RealEigenSolverUtil.orthes(a2, is1[0], is2[0], ort);

    final RS[] wr = unit.createArray(size);
    final RS[] wi = unit.createArray(size);

    final int errorCode = RealEigenSolverUtil.hqr(a2, is1[0], is2[0], wr, wi);

    if (errorCode != 0) {
      throw new RuntimeException(Messages.getString("RealEigen.1")); //$NON-NLS-1$
    }

    final RS[] valRe = GridUtil.clone(wr);
    final RS[] valIm = GridUtil.clone(wi);

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

  /**
   * 実行列の固有ベクトルを返します。
   * 
   * <p>固有値は、実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。 <P>固有ベクトルはノルムが1.0となるよう正規化されます。
   * 
   * @param a 対象となる実行列
   * 
   * @return 固有ベクトル
   */
  public CS[][] getEigenVector(final RS[][] a) {
    final EigenSolutionElements<RS,RM,CS,CM> eig = solve(a);
    final RS[][] vecRe = eig.getReVector();
    final RS[][] vecIm = eig.getImVector();

    return vecRe[0][0].createComplexArray(vecRe, vecIm);
  }
}