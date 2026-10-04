/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.eig;

/**
 * 倍精度(double)型の実行列のSchur分解(A=U*T*U^T)を保持するためのクラスです。
 * 
 * <p>実行列をA、直交行列 U、実Schur行列(ブロック上三角行列) T とすると、 これらの行列の間には、
 * 
 * <blockquote> A = U * T * U <sup>T </sup> </blockquote>
 * 
 * <blockquote> U <sup>T </sup>* U = I </blockquote>
 * 
 * の関係が成り立ちます。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 */
public class SchurDecompositionDoubleRealElements {

  /** U(直交行列)。 */
  private double[][] u;
  /** T(実Schur行列、ブロック上三角行列)。 */
  private double[][] t;

  /**
   * 新しく生成された{@link SchurDecompositionDoubleRealElements}オブジェクトを初期化します。
   * 
   * @param u U(直交行列)
   * @param t T(実Schur行列、ブロック上三角行列)
   */
  public SchurDecompositionDoubleRealElements(final double[][] u, final double[][] t) {
    this.u = u;
    this.t = t;
  }

  /**
   * U(直交行列)を返します。
   * 
   * @return U(直交行列)
   */
  public final double[][] getU() {
    return this.u;
  }

  /**
   * T(実Schur行列、ブロック上三角行列)を返します。
   * 
   * @return T(実Schur行列、ブロック上三角行列)
   */
  public final double[][] getT() {
    return this.t;
  }
}
