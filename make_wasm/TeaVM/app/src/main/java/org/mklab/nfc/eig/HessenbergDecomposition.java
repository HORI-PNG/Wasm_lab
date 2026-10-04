/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 行列のHessenberg分解(A=Q*H*Q^#)を保持するためのクラスです。
 * 
 * <p>行列をA、直交行列(ユニタリー行列)Q、上Hessenberg行列H とすると、 これらの行列の間には、
 * 
 * <blockquote> A = Q * H * Q <sup># </sup> </blockquote>
 * 
 * <blockquote> Q <sup># </sup> Q = I </blockquote>
 * 
 * の関係が成り立ちます。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public class HessenbergDecomposition<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {

  /** Q(直交行列)。 */
  private M q;
  /** H(上Hessenberg行列)。 */
  private M h;

  /**
   * 新しく生成された{@link HessenbergDecomposition}オブジェクトを初期化します。
   * 
   * @param q Q(直交行列、ユニタリー行列)
   * @param h H(上Hessenberg行列)
   */
  public HessenbergDecomposition(final M q, final M h) {
    this.q = q;
    this.h = h;
  }

  /**
   * Q(直交行列、ユニタリー行列)を返します。
   * 
   * @return Q(直交行列、ユニタリー行列)
   */
  public final M getQ() {
    return this.q;
  }

  /**
   * H(上Hessenberg行列)を返します。
   * 
   * @return H(上Hessenberg行列)
   */
  public final M getH() {
    return this.h;
  }
}
