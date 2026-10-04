/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * QZ分解の成分を保持するためのクラスです。
 * 
 * <p>行列の間には、
 * 
 * <blockquote> A = Q * AA * Z </blockquote>
 * 
 * <blockquote> B = Q * BB * Z </blockquote>
 * 
 * <blockquote> Q <sup># </sup>* Q =I </blockquote>
 * 
 * <blockquote> Z <sup># </sup>* Z = I </blockquote>
 * 
 * の関係が成り立ちます。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 * @param <S> スカラーの型
 * @param <M> 行列の型 
 */
public class QZDecompositionElements<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {

  /** AA(上三角行列)。 */
  private S[][] aa;
  /** BB(上三角行列)。 */
  private S[][] bb;
  /** Q(ユニタリー行列)。 */
  private S[][] q;
  /** Z(ユニタリー行列)。 */
  private S[][] z;
  /** X(一般化固有ベクトル)の実部。 */
  private S[][] reX;
  /** X(一般化固有ベクトル)の虚部。 */
  private S[][] imX;

  /**
   * 新しく生成された{@link QZDecompositionElements}オブジェクトを初期化します。
   * 
   * @param aa AA(上三角行列)
   * @param bb BB(上三角行列)
   * @param q Q(ユニタリー行列)
   * @param z Z(ユニタリー行列)
   * @param reX Re(X)(一般化固有ベクトルの実部)
   * @param imX Im(X)(一般化固有ベクトルの虚部)
   */
  public QZDecompositionElements(final S[][] aa, final S[][] bb, final S[][] q, final S[][] z, final S[][] reX, final S[][] imX) {
    this.aa = aa;
    this.bb = bb;
    this.q = q;
    this.z = z;
    this.reX = reX;
    this.imX = imX;
  }

  /**
   * 左変換行列Q(ユニタリー行列)を返します。
   * 
   * @return 左変換行列Q(ユニタリー行列)
   */
  public final S[][] getQ() {
    return this.q;
  }

  /**
   * 右変換行列Z(ユニタリー行列)を返します。
   * 
   * @return 右変換行列Z(ユニタリー行列)
   */
  public final S[][] getZ() {
    return this.z;
  }

  /**
   * AA(上三角行列)を返します。
   * 
   * @return AA(上三角行列)
   */
  public final S[][] getAA() {
    return this.aa;
  }

  /**
   * BB(上三角行列)を返します。
   * 
   * @return BB(上三角行列)
   */
  public final S[][] getBB() {
    return this.bb;
  }

  /**
   * 一般化固有ベクトルの実部Re(X)を返します。
   * 
   * @return 一般化固有ベクトルの実部Re(X)
   */
  public final S[][] getReX() {
    return this.reX;
  }

  /**
   * 一般化固有ベクトルの虚部Im(X)を返します。
   * 
   * @return 一般化固有ベクトルの虚部Im(X)
   */
  public final S[][] getImX() {
    return this.imX;
  }

}
