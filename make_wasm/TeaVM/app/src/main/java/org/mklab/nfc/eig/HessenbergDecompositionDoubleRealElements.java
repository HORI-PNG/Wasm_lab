/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.eig;

/**
 * 倍精度(double)型の実行列のHessenberg分解(A=Q*H*Q^T)を保持するためのクラスです。
 * 
 * <p>実行列をA、直交行列Q、実上Hessenberg行列H とすると、 これらの行列の間には、
 * 
 * <blockquote> A = Q * H * Q <sup>T </sup> </blockquote>
 * 
 * <blockquote> Q <sup>T </sup> Q = I </blockquote>
 * 
 * の関係が成り立ちます。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 */
public class HessenbergDecompositionDoubleRealElements {

  /** Q(直交行列)。 */
  private double[][] q;
  /** H(実上Hessenberg行列)。 */
  private double[][] h;

  /**
   * 新しく生成された{@link HessenbergDecompositionDoubleRealElements}オブジェクトを初期化します。
   * 
   * @param q Q(直交行列)
   * @param h H(実上Hessenberg行列)
   */
  public HessenbergDecompositionDoubleRealElements(final double[][] q, final double[][] h) {
    this.q = q;
    this.h = h;
  }

  /**
   * Q(直交行列)を返します。
   * 
   * @return Q(直交行列)
   */
  public final double[][] getQ() {
    return this.q;
  }

  /**
   * H(実上Hessenberg行列)を返します。
   * 
   * @return H(実上Hessenberg行列)
   */
  public final double[][] getH() {
    return this.h;
  }
}
