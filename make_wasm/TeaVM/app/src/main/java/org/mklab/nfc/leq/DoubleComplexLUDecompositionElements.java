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
public class DoubleComplexLUDecompositionElements {

  /** L(下三角行列)の実部。 */
  private double[][] realL;
  /** L(下三角行列)の虚部。 */
  private double[][] imaginaryL;
  /** U(上三角行列)の実部。 */
  private double[][] realU;
  /** U(上三角行列)の虚部。 */
  private double[][] imaginaryU;
  /** P(並び替え行列、直交行列)。 */
  private int[][] permutation;
  /** 並び替え行列を含むならばtrue。 */
  private boolean containPermutation = false;

  /**
   * 新しく生成された<code>IndexedElements</code>オブジェクトを初期化します。
   * 
   * @param realL L(下三角行列)の実部
   * @param imaginaryL L(下三角行列)の虚部
   * @param realU U(上三角行列)の実部
   * @param imaginaryU U(上三角行列)の虚部
   * @param permutation P(並び替え行列、直交行列)
   */
  public DoubleComplexLUDecompositionElements(final double[][] realL, final double[][] imaginaryL, final double[][] realU, final double[][] imaginaryU, final int[][] permutation) {
    this.realL = realL;
    this.imaginaryL = imaginaryL;
    this.realU = realU;
    this.imaginaryU = imaginaryU;
    this.permutation = permutation;
    this.containPermutation = true;
  }

  /**
   * 新しく生成された<code>IndexedElements</code>オブジェクトを初期化します。
   * 
   * @param realL L(下三角行列)の実部
   * @param imaginaryL L(下三角行列)の虚部
   * @param realU U(上三角行列)の実部
   * @param imaginaryU U(上三角行列)の虚部
   */
  public DoubleComplexLUDecompositionElements(final double[][] realL, final double[][] imaginaryL, final double[][] realU, final double[][] imaginaryU) {
    this.realL = realL;
    this.imaginaryL = imaginaryL;
    this.realU = realU;
    this.imaginaryU = imaginaryU;
    this.containPermutation = false;
  }

  /**
   * L(下三角行列)の実部を返します。
   * 
   * @return L(下三角行列)の実部
   */
  public final double[][] getRealL() {
    return this.realL;
  }

  /**
   * L(下三角行列)の虚部を返します。
   * 
   * @return L(下三角行列)の虚部
   */
  public final double[][] getImaginaryL() {
    return this.imaginaryL;
  }

  /**
   * U(上三角行列)の実部を返します。
   * 
   * @return U(上三角行列)の実部
   */
  public final double[][] getRealU() {
    return this.realU;
  }

  /**
   * U(上三角行列)の虚部を返します。
   * 
   * @return U(上三角行列)の虚部
   */
  public final double[][] getImaginaryU() {
    return this.imaginaryU;
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
