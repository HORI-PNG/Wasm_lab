/*
 * $Id: RealSchurDecomposition.java,v 1.6 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.GridUtil;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 実行列のシュア分解を行うクラスです。
 * 
 * @param <S> スカラーの型
 * @param <M> 行列の型 
 * @author koga
 * @version $Revision: 1.6 $
 */
public final class RealSchurDecomposer<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {

  /**
   * 実行列のSchur分解を返します。
   * 
   * <p>実行列をA、直交行列 U、Schur行列 T とすると、 これらの行列の間には、
   * 
   * <blockquote> A = U * T * U <sup>T </sup> </blockquote>
   * 
   * <blockquote> U <sup>T </sup>* U = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param a 対象となる実行列
   * @return Schur分解の結果
   */
  public SchurDecompositionElements<S,M> decompose(final S[][] a) {
    final int size = a.length;
    final S unit = a[0][0].createUnit();

    final S[][] t = GridUtil.clone(a);
    final S[][] u = unit.createArray(size, size);
    final S[] ort = unit.createArray(size);

    RealEigenSolverUtil.orthes(t, 1, size, ort);
    RealEigenSolverUtil.ortran(t, 1, size, ort, u);

    final S[] wRe = unit.createArray(size);
    final S[] wIm = unit.createArray(size);

    final int errorCode = RealEigenSolverUtil.hqr2(t, 1, size, wRe, wIm, u, true);

    if (errorCode != 0) {
      throw new RuntimeException(Messages.getString("RealSchurDecomposition.0")); //$NON-NLS-1$
    }

    for (int i = 1; i <= size; i++) {
      for (int j = 1; j < i - 1; j++) {
        t[i - 1][j - 1] = unit.createZero();
      }
    }

    return new SchurDecompositionElements<>(u, t);
  }

}