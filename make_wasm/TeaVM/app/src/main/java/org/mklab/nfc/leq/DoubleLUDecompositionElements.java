/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.leq;

/**
 * 並べ替え付きLU分解(P*A=L*U)の成分を保持するためのクラスです。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 */
public class DoubleLUDecompositionElements {

  /** L(下三角行列)。 */
  private double[][] lower;
  /** U(上三角行列)。 */
  private double[][] upper;
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
  public DoubleLUDecompositionElements(final double[][] lower, final double[][] upper, final int[][] permutation) {
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
  public DoubleLUDecompositionElements(final double[][] lower, final double[][] upper) {
    this.lower = lower;
    this.upper = upper;
    this.containPermutation = false;
  }

  /**
   * L(下三角行列)を返します。
   * 
   * @return L(下三角行列)
   */
  public final double[][] getL() {
    return this.lower;
  }

  /**
   * U(上三角行列)を返します。
   * 
   * @return U(上三角行列)
   */
  public final double[][] getU() {
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
