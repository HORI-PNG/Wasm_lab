/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.svd;

/**
 * 倍精度(double)型の実行列の特異値分解を保持するためのクラスです。
 * 
 * <p>対象となる行列をA、特異値を対角成分とする対角行列を D、左特異ベクトルからなる直交行列をU、右特異ベクトルからなる直交行列をVとすると、
 * 
 * <blockquote> A = U * D * V <sup>T </sup> </blockquote>
 * 
 * 　の関係が成り立ちます。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 */
public class SingularValueDecompositionDoubleRealElements {

  /** U(左特異ベクトルからなる行列、直交行列)。 */
  private double[][] u;
  /** D(特異値を対角成分とする対角行列)。 */
  private double[][] d;
  /** V(右特異ベクトルならなる行列、直交行列)。 */
  private double[][] v;

  /**
   * 新しく生成された{@link SingularValueDecompositionDoubleRealElements}オブジェクトを初期化します。
   * 
   * @param u U(左特異ベクトルからなる行列、直交行列)
   * @param d D(特異値を対角成分とする対角行列)
   * @param v V(右特異ベクトルならなる行列、直交行列)
   */
  public SingularValueDecompositionDoubleRealElements(final double[][] u, final double[][] d, final double[][] v) {
    this.u = u;
    this.d = d;
    this.v = v;
  }

  /**
   * U(左特異ベクトルからなる行列、直交行列)を返します。
   * 
   * @return U(左特異ベクトルからなる行列、直交行列)
   */
  public final double[][] getU() {
    return this.u;
  }

  /**
   * D(特異値を対角成分とする対角行列)を返します。
   * 
   * @return D(特異値を対角成分とする対角行列)
   */
  public final double[][] getD() {
    return this.d;
  }

  /**
   * V(右特異ベクトルからなる行列、直交行列)を返します。
   * 
   * @return V(右特異ベクトルからなる行列、直交行列)
   */
  public final double[][] getV() {
    return this.v;
  }
}
