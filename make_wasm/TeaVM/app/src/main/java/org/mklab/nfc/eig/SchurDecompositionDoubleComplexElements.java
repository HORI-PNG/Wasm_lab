/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.eig;

/**
 * 倍精度(double)型の複素行列のSchur分解(A=U*T*U^#)を保持するためのクラスです。
 * 
 * <p>複素行列をA、ユニタリー行列 U、複素Schur行列(上三角行列) T とすると、 これらの行列の間には、
 * 
 * <blockquote> A = U * T * U <sup># </sup> </blockquote>
 * 
 * <blockquote> U <sup># </sup>* U = I </blockquote>
 * 
 * の関係が成り立ちます。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 */
public class SchurDecompositionDoubleComplexElements {

  /** U(ユニタリー行列)の実部。 */
  private double[][] reU;
  /** U(ユニタリー行列)の虚部。 */
  private double[][] imU;
  /** T(複素Schur行列、上三角行列)の実部。 */
  private double[][] reT;
  /** T(複素Schur行列、上三角行列)の虚部。 */
  private double[][] imT;

  /**
   * 新しく生成された{@link SchurDecompositionDoubleComplexElements}オブジェクトを初期化します。
   * 
   * @param reU U(ユニタリー行列)の実部
   * @param imU U(ユニタリー行列)の虚部
   * @param reT T(複素Schur行列、上三角行列)の実部
   * @param imT T(複素Schur行列、上三角行列)の虚部
   */
  public SchurDecompositionDoubleComplexElements(final double[][] reU, final double[][] imU, final double[][] reT, final double[][] imT) {
    this.reU = reU;
    this.imU = imU;
    this.reT = reT;
    this.imT = imT;
  }

  /**
   * U(ユニタリー行列)の実部を返します。
   * 
   * @return U(ユニタリー行列)の実部
   */
  public final double[][] getReU() {
    return this.reU;
  }

  /**
   * U(ユニタリー行列)の虚部を返します。
   * 
   * @return U(ユニタリー行列)の虚部
   */
  public final double[][] getImU() {
    return this.imU;
  }

  /**
   * T(複素Schur行列、上三角行列)の実部を返します。
   * 
   * @return T(複素Schur行列、上三角行列)の実部
   */
  public final double[][] getReT() {
    return this.reT;
  }

  /**
   * T(複素Schur行列、上三角行列)の虚部を返します。
   * 
   * @return T(複素Schur行列、上三角行列)の虚部
   */
  public final double[][] getImT() {
    return this.imT;
  }
}
