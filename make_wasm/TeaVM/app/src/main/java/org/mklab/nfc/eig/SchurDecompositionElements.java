/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 行列のSchur分解(A=U*T*U^#)を保持するためのクラスです。
 * 
 * <p>行列をA、直交（ユニタリー）行列 U、Schur行列(ブロック上三角行列、上三角行列) T とすると、 これらの行列の間には、
 * 
 * <blockquote> A = U * T * U <sup># </sup> </blockquote>
 * 
 * <blockquote> U <sup># </sup>* U = I </blockquote>
 * 
 * の関係が成り立ちます。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 * @param <S> スカラーの型
 * @param <M> 行列の型 
 */
public class SchurDecompositionElements<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {

  /** U(ユニタリー行列、直交行列)。 */
  private S[][] u;
  /** T(Schur行列、ブロック上三角行列、上三角行列)。 */
  private S[][] t;

  /**
   * 新しく生成された{@link SchurDecompositionElements}オブジェクトを初期化します。
   * 
   * @param u U(ユニタリー行列、直交行列)
   * @param t T(Schur行列、ブロック上三角行列、上三角行列)
   */
  public SchurDecompositionElements(final S[][] u, final S[][] t) {
    this.u = u;
    this.t = t;
  }

  /**
   * U(ユニタリー行列、直交行列)を返します。
   * 
   * @return U(ユニタリー行列、直交行列)
   */
  public final S[][] getU() {
    return this.u;
  }

  /**
   * T(Schur行列、ブロック上三角行列、上三角行列)を返します。
   * 
   * @return T(Schur行列、ブロック上三角行列、上三角行列)
   */
  public final S[][] getT() {
    return this.t;
  }
}
