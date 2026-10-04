/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.svd;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 行列の特異値分解を保持するためのクラスです。
 * 
 * <p>対象となる行列をA、特異値を対角成分とする対角行列を D、左特異ベクトルからなる直交行列(ユニタリー行列)をU、右特異ベクトルからなる直交行列(ユニタリー行列)をVとすると、
 * 
 * <blockquote> A = U * D * V <sup>T </sup> </blockquote>
 * 
 * <blockquote> A = U * D * V <sup># </sup> </blockquote>
 * 
 * 　の関係が成り立ちます。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public class SingularValueDecompositionElements<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {

  /** U(左特異ベクトルからなる行列、直交行列(ユニタリー))。 */
  private S[][] u;
  /** D(特異値を対角成分とする対角行列)。 */
  private S[][] d;
  /** V(右特異ベクトルならなる行列、直交行列(ユニタリー))。 */
  private S[][] v;

  /**
   * 新しく生成された{@link SingularValueDecompositionElements}オブジェクトを初期化します。
   * 
   * @param u U(左特異ベクトルからなる行列、直交行列(ユニタリー行列))
   * @param d D(特異値を対角成分とする対角行列)
   * @param v V(右特異ベクトルならなる行列、直交行列(ユニタリー行列))
   */
  public SingularValueDecompositionElements(final S[][] u, final S[][] d, final S[][] v) {
    this.u = u;
    this.d = d;
    this.v = v;
  }

  /**
   * U(左特異ベクトルからなる行列、直交行列(ユニタリー行列))を返します。
   * 
   * @return U(左特異ベクトルからなる行列、直交行列(ユニタリー行列))
   */
  public final S[][] getU() {
    return this.u;
  }

  /**
   * D(特異値を対角成分とする対角行列)を返します。
   * 
   * @return D(特異値を対角成分とする対角行列)
   */
  public final S[][] getD() {
    return this.d;
  }

  /**
   * V(右特異ベクトルからなる行列、直交行列(ユニタリー行列))を返します。
   * 
   * @return V(右特異ベクトルからなる行列、直交行列(ユニタリー行列))
   */
  public final S[][] getV() {
    return this.v;
  }
}
