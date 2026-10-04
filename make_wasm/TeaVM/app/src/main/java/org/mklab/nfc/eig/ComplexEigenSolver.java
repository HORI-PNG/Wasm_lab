/*
 * Created on 2008/03/17
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.AbstractNumericalComplexMatrix;
import org.mklab.nfc.matrix.AbstractNumericalMatrixUtil;
import org.mklab.nfc.matrix.GridUtil;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * 複素行列の固有値問題を解くためのクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.6 $, 2008/03/17
 * 
 * @param <RS> 実スカラーの型
 * @param <RM> 実行列の型
 * @param <CS> 複素スカラーの型 
 * @param <CM> 複素行列の型
 */
public class ComplexEigenSolver<RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> {
  /**
   * 複素行列の固有値と固有ベクトルを返します。
   * 
   * <p>固有値は、実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。 <p>固有ベクトルはノルムが1.0となるよう正規化されます。
   * 
   * @param a 対象となる複素行列
   * @return 行列の固有値と固有ベクトル
   */
  public final EigenSolutionElements<RS,RM,CS,CM> solve(final CS[][] a) {
    final RS[][] aRe = AbstractNumericalMatrixUtil.getRealPartElements(a);
    final RS[][] aIm = AbstractNumericalMatrixUtil.getImaginaryPartElements(a);
    final int size = aRe.length;

    final RS[][] a2Re = GridUtil.clone(aRe);
    final RS[][] a2Im = GridUtil.clone(aIm);

    final RS[] fv1 = GridUtil.createZero(aRe[0], size);

    final int[] low = new int[1];
    final int[] igh = new int[1];

    ComplexEigenSolverUtil.cbal(a2Re, a2Im, low, igh, fv1);

    final RS[] fv2 = GridUtil.createZero(aRe[0], size);
    final RS[] fv3 = GridUtil.createZero(aRe[0], size);

    ComplexEigenSolverUtil.corth(a2Re, a2Im, low[0], igh[0], fv2, fv3);

    final RS[] wRe = GridUtil.createZero(aRe[0], size);
    final RS[] wIm = GridUtil.createZero(aRe[0], size);
    final RS[][] vecRe = GridUtil.createZero(aRe, size, size);
    final RS[][] vecIm = GridUtil.createZero(aRe, size, size);

    final int errorCode = ComplexEigenSolverUtil.comqr2(a2Re, a2Im, low[0], igh[0], fv2, fv3, wRe, wIm, vecRe, vecIm, false);

    if (errorCode != 0) {
      throw new RuntimeException(Messages.getString("ComplexEigen.0")); //$NON-NLS-1$
    }

    ComplexEigenSolverUtil.cbabk2(low[0], igh[0], fv1, a2Re.length, vecRe, vecIm);

    final RS[] valRe = GridUtil.createZero(aRe[0], size);
    final RS[] valIm = GridUtil.createZero(aRe[0], size);

    GridUtil.copy(wRe, valRe);
    GridUtil.copy(wIm, valIm);

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
   * 複素行列の固有ベクトルを返します。
   * 
   * <p>固有値は、実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。 <p>固有ベクトルはノルムが1.0となるよう正規化されます。
   * 
   * @param a 対象となる複素行列
   * 
   * @return 固有ベクトル
   */
  public final CS[][] getEigenVector(final CS[][] a) {
    final EigenSolutionElements<RS,RM,CS,CM> eig = solve(a);

    final RS[][] vecRe = eig.getReVector();
    final RS[][] vecIm = eig.getImVector();

    return vecRe[0][0].createComplexArray(vecRe, vecIm);
  }

  /**
   * 複素行列の固有値を返します。
   * 
   * <p>固有値は、実部の降順に並べられます。
   * 
   * @param a 対象となる複素行列
   * 
   * @return 固有値
   */
  public final CS[] getEigenValue(final CS[][] a) {
    final RS[][] aRe = AbstractNumericalMatrixUtil.getRealPartElements(a);
    final RS[][] aIm = AbstractNumericalMatrixUtil.getImaginaryPartElements(a);

    final int size = aRe.length;

    final RS[][] a2Re = GridUtil.clone(aRe);
    final RS[][] a2Im = GridUtil.clone(aIm);
    final RS[] fv1 = GridUtil.createZero(aRe[0], size);
    final int[] low = new int[1];
    final int[] igh = new int[1];

    ComplexEigenSolverUtil.cbal(a2Re, a2Im, low, igh, fv1);

    final RS[] fv2 = GridUtil.createZero(aRe[0], size);
    final RS[] fv3 = GridUtil.createZero(aRe[0], size);

    ComplexEigenSolverUtil.corth(a2Re, a2Im, low[0], igh[0], fv2, fv3);

    final RS[] wRe = GridUtil.createZero(aRe[0], size);
    final RS[] wIm = GridUtil.createZero(aRe[0], size);

    final int errorCode = ComplexEigenSolverUtil.comqr(a2Re, a2Im, low[0], igh[0], wRe, wIm);

    if (errorCode != 0) {
      throw new RuntimeException(Messages.getString("ComplexEigen.1")); //$NON-NLS-1$
    }

    final RS[] valRe = GridUtil.createZero(aRe[0], size);
    final RS[] valIm = GridUtil.createZero(aRe[0], size);

    GridUtil.copy(wRe, valRe);
    GridUtil.copy(wIm, valIm);

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
}
