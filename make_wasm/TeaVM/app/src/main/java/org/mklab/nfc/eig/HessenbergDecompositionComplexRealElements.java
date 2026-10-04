/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.eig;

/**
 * 倍精度(double)型の複素行列のHessenberg分解(A=Q*H*Q^#)を保持するためのクラスです。
 * 
 * <p>複素行列をA、ユニタリー行列Q、複素上Hessenberg行列H とすると、 これらの行列の間には、
 * 
 * <blockquote> A = Q * H * Q <sup># </sup> </blockquote>
 * 
 * <blockquote> Q <sup># </sup> Q = I </blockquote>
 * 
 * の関係が成り立ちます。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 */
public class HessenbergDecompositionComplexRealElements {

  /** Q(ユニタリー行列)の実部。 */
  private double[][] reQ;
  /** Q(ユニタリー行列)の虚部。 */
  private double[][] imQ;
  /** H(複素上Hessenberg行列)の実部。 */
  private double[][] reH;
  /** H(複素上Hessenberg行列)の虚部。 */
  private double[][] imH;

  /**
   * 新しく生成された{@link HessenbergDecompositionComplexRealElements}オブジェクトを初期化します。
   * 
   * @param reQ Q(ユニタリー行列)の実部
   * @param imQ Q(ユニタリー行列)の虚部
   * @param reH H(複素上Hessenberg行列)の実部
   * @param imH H(複素上Hessenberg行列)の虚部
   */
  public HessenbergDecompositionComplexRealElements(final double[][] reQ, final double[][] imQ, final double[][] reH, final double[][] imH) {
    this.reQ = reQ;
    this.imQ = imQ;
    this.reH = reH;
    this.imH = imH;
  }

  /**
   * Q(ユニタリー行列)の実部を返します。
   * 
   * @return Q(ユニタリー行列)の実部
   */
  public final double[][] getReQ() {
    return this.reQ;
  }

  /**
   * Q(ユニタリー行列)の虚部を返します。
   * 
   * @return Q(ユニタリー行列)の虚部
   */
  public final double[][] getImQ() {
    return this.imQ;
  }

  /**
   * H(複素上Hessenberg行列)の実部を返します。
   * 
   * @return H(複素上Hessenberg行列)の実部
   */
  public final double[][] getReH() {
    return this.reH;
  }

  /**
   * H(複素上Hessenberg行列)の虚部を返します。
   * 
   * @return H(複素上Hessenberg行列)の虚部
   */
  public final double[][] getImH() {
    return this.imH;
  }
}
