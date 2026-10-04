/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 行列のバランス化分解(A=D*B*D^(-1), B=D\A*D)を保持するためのクラスです。
 * 
 * <p>行列をA、バランス化された行列をB、スケーリング行列(対角行列)をDとすると、これらの行列の間には
 * 
 * <blockquote> A = D * B * D <sup>-1</sup> </blockquote>
 * 
 * <blockquote> B = D <sup>-1</sup> A * D </blockquote>
 * 
 * の関係が成り立ちます。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public class BalancedDecomposition<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {

  /** D(スケーリング行列、対角行列)。 */
  private M d;
  /** B(バランス化された行列)。 */
  private M b;

  /**
   * 新しく生成された{@link BalancedDecomposition}オブジェクトを初期化します。
   * 
   * @param d D(スケーリング行列、対角行列)
   * @param b B(バランス化された行列)
   */
  public BalancedDecomposition(final M d, final M b) {
    this.d = d;
    this.b = b;
  }

  /**
   * B(バランス化された行列)を返します。
   * 
   * @return B(バランス化された行列)
   */
  public final M getB() {
    return this.b;
  }

  /**
   * D(スケーリング行列、対角行列)を返します。
   * 
   * @return D(スケーリング行列、対角行列)
   */
  public final M getD() {
    return this.d;
  }
}
