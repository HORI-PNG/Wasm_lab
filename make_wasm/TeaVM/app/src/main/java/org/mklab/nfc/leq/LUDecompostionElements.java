/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.leq;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 並べ替え付きLU分解(P*A=L*U)の成分を保持するためのクラスです。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public class LUDecompostionElements<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {

  /** L(下三角行列)。 */
  private S[][] lower;
  /** U(上三角行列)。 */
  private S[][] upper;
  /** P(並び替え行列、直交行列)。 */
  private int[][] permutation;
  /** 並び替え行列を含むならばtrue。 */
  private boolean containPermutation = false;

  /**
   * 新しく生成された<code>IndexedElements</code>オブジェクトを初期化します。
   * 
   * @param lower L(下三角行列)
   * @param upper U(上三角行列)
   * @param permutation P(並び替え行列、直交行列)
   */
  public LUDecompostionElements(final S[][] lower, final S[][] upper, final int[][] permutation) {
    this.lower = lower;
    this.upper = upper;
    this.permutation = permutation;
    this.containPermutation = true;
  }

  /**
   * 新しく生成された<code>IndexedElements</code>オブジェクトを初期化します。
   * 
   * @param lower L(下三角行列)
   * @param upper U(上三角行列)
   */
  public LUDecompostionElements(final S[][] lower, final S[][] upper) {
    this.lower = lower;
    this.upper = upper;
    this.containPermutation = false;
  }

  /**
   * L(下三角行列)を返します。
   * 
   * @return L(下三角行列)
   */
  public final S[][] getL() {
    return this.lower;
  }

  /**
   * U(上三角行列)を返します。
   * 
   * @return U(上三角行列)
   */
  public final S[][] getU() {
    return this.upper;
  }

  /**
   * P(並び替え行列、直交行列)を返します。
   * 
   * @return P(並び替え行列、直交行列)
   */
  public final int[][] getP() {
    return this.permutation;
  }

  /**
   * 並び替え行列を含むか判定します。
   * 
   * @return 並び替え行列を含むならばtrue
   */
  public final boolean containPermutation() {
    return this.containPermutation;
  }
}
