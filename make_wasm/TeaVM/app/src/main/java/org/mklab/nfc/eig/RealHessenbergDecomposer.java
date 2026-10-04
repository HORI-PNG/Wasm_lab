/*
 * $Id: RealHessenbergDecomposition.java,v 1.2 2008/03/15 00:23:43 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.GridUtil;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 実行列のヘッセンベルグ分解を求めるクラスです。
 * 
 * @param <S> スカラーの型
 * @param <M> 行列の型 
 * @author koga
 * @version $Revision: 1.2 $
 */
public final class RealHessenbergDecomposer<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {

  /**
   * 実行列のヘッセンベルグ分解を求め、対応する直交行列とともに返します。
   * 
   * <p>実行列をA、直交行列をQ、上ヘッセンベルグ行列をHとするとき、これらの行列の間には、
   * 
   * <blockquote> A = Q * H * Q<sup>T </sup> </blockquote>
   * 
   * <blockquote> Q <sup>T </sup>* Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param a 対象となる実行列
   * @return ヘッセンベルグ分解の結果
   */
  public HessenbergDecompositionElements<S,M> decompose(final S[][] a) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final S unit = a[0][0].createUnit();

    final S[][] a2 = GridUtil.clone(a);
    final S[] ort = unit.createArray(rowSize);

    RealEigenSolverUtil.orthes(a2, 1, rowSize, ort);

    final S[][] h = unit.createArray(rowSize, columnSize);
    for (int i = 1; i <= rowSize; i++) {
      for (int j = 1; j <= rowSize; j++) {
        if (i <= j + 1) {
          h[i - 1][j - 1] = a2[i - 1][j - 1];
        } else {
          h[i - 1][j - 1] = unit.createZero();
        }
      }
    }

    final S[][] q = unit.createArray(rowSize, columnSize);

    RealEigenSolverUtil.ortran(a2, 1, rowSize, ort, q);

    return new HessenbergDecompositionElements<>(q, h);
  }
}