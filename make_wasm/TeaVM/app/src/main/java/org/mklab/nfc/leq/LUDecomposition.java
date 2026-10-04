/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.leq;

import org.mklab.nfc.matrix.IntMatrix;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * LU分解(A=LU)および並び替え付きLU分解(P*A=L*U)を保持するクラスです。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public class LUDecomposition<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {

  /** L(下三角行列)。 */
  private M lower;
  /** U(上三角行列)。 */
  private M upper;
  /** P(並び替え行列、直交行列)。 */
  private IntMatrix permutation;
  /** 並び替え行列を含むならばtrue。 */
  private boolean containP = false;

  /**
   * 新しく生成された<code>IndexedMatrix</code>オブジェクトを初期化します。
   * 
   * @param lower L(下三角行列)
   * @param upper U(上三角行列)
   * @param permutation P(並び替え行列、直交行列)
   */
  public LUDecomposition(final M lower, final M upper, final IntMatrix permutation) {
    this.lower = lower;
    this.upper = upper;
    this.permutation = permutation;
    this.containP = true;
  }

  /**
   * 新しく生成された<code>IndexedMatrix</code>オブジェクトを初期化します。
   * 
   * @param lower L(下三角行列)
   * @param upper U(上三角行列)
   */
  public LUDecomposition(final M lower, final M upper) {
    this.lower = lower;
    this.upper = upper;
    this.containP = false;
  }

  /**
   * L(下三角行列)を返します。
   * 
   * @return L(下三角行列)
   */
  public final M getL() {
    return this.lower;
  }

  /**
   * U(上三角行列)を返します。
   * 
   * @return U(上三角行列)
   */
  public final M getU() {
    return this.upper;
  }

  /**
   * P(並び替え行列、直交行列)を返します。
   * 
   * @return P(並び替え行列、直交行列)
   */
  public final IntMatrix getP() {
    return this.permutation;
  }

  /**
   * 並び替え行列を含むか判定します。
   * 
   * @return 並び替え行列を含むならばtrue
   */
  public final boolean containPermuation() {
    return this.containP;
  }
}
