/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.eig;

/**
 * 倍精度(double)型の複素行列のバランス化分解(A=D*B*D^(-1), B=D\A*D)を保持するためのクラスです。
 * 
 * <p>複素行列をA、バランス化された行列をB、スケーリング行列(対角行列)をDとすると、これらの行列の間には
 * 
 * <blockquote> A = D * B * D <sup>-1</sup> </blockquote>
 * 
 * <blockquote> B = D <sup>-1</sup> A * D </blockquote>
 * 
 * の関係が成り立ちます。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 */
public class BalancedDecompositionDoubleComplexElements {

  /** D(スケーリング行列、対角行列)。 */
  private double[][] d;
  /** B(バランス化された行列)の実部。 */
  private double[][] reB;
  /** B(バランス化された行列)の虚部。 */
  private double[][] imB;

  /**
   * 新しく生成された{@link BalancedDecompositionDoubleComplexElements}オブジェクトを初期化します。
   * 
   * @param d D(スケーリング行列、対角行列)
   * @param reB B(バランス化された行列)の実部
   * @param imB B(バランス化された行列)の虚部
   */
  public BalancedDecompositionDoubleComplexElements(final double[][] d, final double[][] reB, final double[][] imB) {
    this.d = d;
    this.reB = reB;
    this.imB = imB;
  }

  /**
   * B(バランス化された行列)の実部を返します。
   * 
   * @return B(バランス化された行列)の実部
   */
  public final double[][] getReB() {
    return this.reB;
  }

  /**
   * B(バランス化された行列)の虚部を返します。
   * 
   * @return B(バランス化された行列)の虚部
   */
  public final double[][] getImB() {
    return this.imB;
  }

  /**
   * D(スケーリング行列、対角行列)を返します。
   * 
   * @return D(スケーリング行列、対角行列)
   */
  public final double[][] getD() {
    return this.d;
  }
}
