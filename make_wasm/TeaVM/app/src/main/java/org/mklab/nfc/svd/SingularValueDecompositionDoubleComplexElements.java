/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.svd;

/**
 * 倍精度(double)型の複素行列の特異値分解を保持するためのクラスです。
 * 
 * <p>対象となる行列をA、特異値を対角成分とする対角行列を D、左特異ベクトルからなるユニタリー行列をU、右特異ベクトルからなるユニタリー行列をVとすると、
 * 
 * <blockquote> A = U * D * V <sup># </sup> </blockquote>
 * 
 * 　の関係が成り立ちます。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 */
public class SingularValueDecompositionDoubleComplexElements {

  /** U(左特異ベクトルからなる行列、直交行列)の実部。 */
  private double[][] reU;
  /** U(左特異ベクトルからなる行列、直交行列)の虚部。 */
  private double[][] imU;
  /** D(特異値を対角成分とする対角行列)。 */
  private double[][] d;
  /** V(右特異ベクトルならなる行列、直交行列)の実部。 */
  private double[][] reV;
  /** V(右特異ベクトルならなる行列、直交行列)の虚部。 */
  private double[][] imV;

  /**
   * 新しく生成された{@link SingularValueDecompositionDoubleComplexElements}オブジェクトを初期化します。
   * 
   * @param reU U(左特異ベクトルからなる行列、ユニタリー行列)の実部
   * @param imU U(左特異ベクトルからなる行列、ユニタリー行列)の虚部
   * @param d D(特異値を対角成分とする対角行列)
   * @param reV V(右特異ベクトルならなる行列、ユニタリー行列)の実部
   * @param imV V(右特異ベクトルならなる行列、ユニタリー行列)の虚部
   */
  public SingularValueDecompositionDoubleComplexElements(final double[][] reU, final double[][] imU, final double[][] d, final double[][] reV, final double[][] imV) {
    this.reU = reU;
    this.imU = imU;
    this.d = d;
    this.reV = reV;
    this.imV = imV;
  }

  /**
   * U(左特異ベクトルからなる行列、ユニタリー行列)の実部を返します。
   * 
   * @return U(左特異ベクトルからなる行列、ユニタリー行列)の実部
   */
  public final double[][] getReU() {
    return this.reU;
  }

  /**
   * U(左特異ベクトルからなる行列、ユニタリー行列)の虚部を返します。
   * 
   * @return U(左特異ベクトルからなる行列、ユニタリー行列)の虚部
   */
  public final double[][] getImU() {
    return this.imU;
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
   * V(右特異ベクトルからなる行列、ユニタリー行列)の実部を返します。
   * 
   * @return V(右特異ベクトルからなる行列、ユニタリー行列)の実部
   */
  public final double[][] getReV() {
    return this.reV;
  }

  /**
   * V(右特異ベクトルからなる行列、ユニタリー行列)の虚部を返します。
   * 
   * @return V(右特異ベクトルからなる行列、ユニタリー行列)の虚部
   */
  public final double[][] getImV() {
    return this.imV;
  }
}
