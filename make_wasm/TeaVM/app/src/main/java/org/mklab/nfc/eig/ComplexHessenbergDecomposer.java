/*
 * $Id: ComplexHessenbergDecomposition.java,v 1.4 2008/03/18 00:15:02 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.BaseMatrixUtil;
import org.mklab.nfc.matrix.AbstractNumericalComplexMatrix;
import org.mklab.nfc.matrix.AbstractNumericalMatrixUtil;
import org.mklab.nfc.matrix.GridUtil;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * 複素行列のヘッセンベルグ分解を求めるクラスです。
 * 
 * 
 * @author matsuki
 * @version $Revision: 1.4 $
 * 
 * @param <RS> 実スカラーの型
 * @param <RM> 実行列の型
 * @param <CS> 複素スカラーの型 
 * @param <CM> 複素行列の型
 */
public final class ComplexHessenbergDecomposer<RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> {

  /**
   * 複素行列のヘッセンベルグ分解を求め、対応するユニタリ行列とともに返します。
   * 
   * <p>複素行列をA、ユニタリ行列をQ、上ヘッセンベルグ行列をHとするとき、これらの行列の間には、
   * 
   * <blockquote> A = Q * H * Q<sup>#</sup> </blockquote>
   * 
   * <blockquote> Q<sup>#</sup> * Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * 
   * @param a 複素行列
   * @return ヘッセンベルグ分解の結果
   */
  public HessenbergDecompositionElements<CS,CM> decompose(final CS[][] a) {
    final RS[][] aRe = AbstractNumericalMatrixUtil.getRealPartElements(a);
    final RS[][] aIm = AbstractNumericalMatrixUtil.getImaginaryPartElements(a);

    final int size = aRe.length;

    final RS[][] a2Re = GridUtil.clone(aRe);
    final RS[][] a2Im = GridUtil.clone(aIm);

    final RS unit = aRe[0][0].createUnit();

    final RS[][] zRe = BaseMatrixUtil.createUnit(aRe, size, size);
    final RS[][] zIm = GridUtil.createZero(aRe, size, size);

    final RS[] ortRe = GridUtil.createZero(aRe[0], size);
    final RS[] ortIm = GridUtil.createZero(aRe[0], size);

    ComplexEigenSolverUtil.corth(a2Re, a2Im, 1, size, ortRe, ortIm);

    final RS[][] hRe = GridUtil.clone(a2Re);
    final RS[][] hIm = GridUtil.clone(a2Im);

    for (int i = 1; i <= size; i++) {
      for (int j = 1; j < i - 1; j++) {
        hRe[i - 1][j - 1] = unit.createZero();
        hIm[i - 1][j - 1] = unit.createZero();
      }
    }

    ComplexEigenSolverUtil.cortb(a2Re, a2Im, 1, size, ortRe, ortIm, zRe, zIm, size);

    final RS[][] qRe = GridUtil.clone(zRe);
    final RS[][] qIm = GridUtil.clone(zIm);

    final CS[][] q = qRe[0][0].createComplexArray(qRe, qIm);
    final CS[][] h = hRe[0][0].createComplexArray(hRe, hIm);

    return new HessenbergDecompositionElements<>(q, h);
  }
}