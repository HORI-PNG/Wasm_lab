/*
 * $Id: ComplexSchurDecomposition.java,v 1.4 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.AbstractNumericalMatrixUtil;
import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.GridUtil;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * 複素行列のシュア分解を行うクラスです。
 * 
 * @author koga
 * @version $Revision: 1.4 $
 * 
 * @param <RS> 実スカラーの型
 * @param <RM> 実行列の型
 * @param <CS> 複素スカラーの型 
 * @param <CM> 複素行列の型
 */
public final class ComplexSchurDecomposer<RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> {

  /**
   * 複素行列のSchur分解を返します。
   * 
   * <p>複素数値行列をA、ユニタリー行列 U、Schur行列 T とすると、 これらの行列の間には、
   * 
   * <blockquote> A = U * T * U <sup># </sup> </blockquote>
   * 
   * <blockquote> U <sup># </sup>* U = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param a 対象となる複素行列
   * @return Schur分解の結果
   */
  public SchurDecompositionElements<CS,CM> decompose(final CS[][] a) {
    final RS[][] aRe = AbstractNumericalMatrixUtil.getRealPartElements(a);
    final RS[][] aIm = AbstractNumericalMatrixUtil.getImaginaryPartElements(a);
    final RS unit = aRe[0][0].createUnit();

    final int size = aRe.length;

    final RS[][] tRe = GridUtil.clone(aRe);
    final RS[][] tIm = GridUtil.clone(aIm);

    final RS[] fv2 = GridUtil.createZero(aRe[0], size);
    final RS[] fv3 = GridUtil.createZero(aRe[0], size);

    ComplexEigenSolverUtil.corth(tRe, tIm, 1, size, fv2, fv3);

    final RS[] wRe = GridUtil.createZero(aRe[0], size);
    final RS[] wIm = GridUtil.createZero(aRe[0], size);
    final RS[][] uRe = GridUtil.createZero(aRe, size, size);
    final RS[][] uIm = GridUtil.createZero(aRe, size, size);

    final int errorCode = ComplexEigenSolverUtil.comqr2(tRe, tIm, 1, size, fv2, fv3, wRe, wIm, uRe, uIm, true);

    if (errorCode != 0) {
      throw new RuntimeException(Messages.getString("ComplexSchurDecomposition.0")); //$NON-NLS-1$
    }

    for (int i = 1; i <= size; i++) {
      for (int j = 1; j < i; j++) {
        tRe[i - 1][j - 1] = unit.createZero();
        tIm[i - 1][j - 1] = unit.createZero();
      }
    }

    final CS[][] u =uRe[0][0].createComplexArray(uRe, uIm);
    final CS[][] t = tRe[0][0].createComplexArray(tRe, tIm);

    return new SchurDecompositionElements<>(u, t);
  }
}