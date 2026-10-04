/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.eig;

/**
 * 倍精度(double)型の実行列のバランス化分解(A=D*B*D^(-1), B=D\A*D)を保持するためのクラスです。
 * 
 * <p>実行列をA、バランス化された行列をB、スケーリング行列(対角行列)をDとすると、これらの行列の間には
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
public class BalancedDecompositionDoubleRealElements {

  /** D(スケーリング行列、対角行列)。 */
  private double[][] d;
  /** B(バランス化された行列)。 */
  private double[][] b;

  /**
   * 新しく生成された{@link BalancedDecompositionDoubleRealElements}オブジェクトを初期化します。
   * 
   * @param d D(スケーリング行列、対角行列)
   * @param b B(バランス化された行列)
   */
  public BalancedDecompositionDoubleRealElements(final double[][] d, final double[][] b) {
    this.d = d;
    this.b = b;
  }

  /**
   * B(バランス化された行列)を返します。
   * 
   * @return B(バランス化された行列)
   */
  public final double[][] getB() {
    return this.b;
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
