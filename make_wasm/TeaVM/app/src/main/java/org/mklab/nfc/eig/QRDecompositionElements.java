/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 並べ替え付きQR分解(A*P=Q*R)の成分を保持するためのクラスです。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 * @param <S> スカラーの型
 * @param <M> 行列の型 
 */
public class QRDecompositionElements<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {

  /** Q(直交行列)。 */
  private S[][] q;
  /** R(上三角行列)。 */
  private S[][] r;
  /** P(並び替え行列、直交行列)。 */
  private int[][] p;
  /** 並び替え行列を含むならばtrue。 */
  private boolean containP = false;

  /**
   * 新しく生成された{@link QRDecompositionElements}オブジェクトを初期化します。
   * 
   * @param q L(下三角行列)
   * @param r U(上三角行列)
   * @param p P(並び替え行列、直交行列)
   */
  public QRDecompositionElements(final S[][] q, final S[][] r, final int[][] p) {
    this.q = q;
    this.r = r;
    this.p = p;
    this.containP = true;
  }

  /**
   * 新しく生成された{@link QRDecompositionElements}オブジェクトを初期化します。
   * 
   * @param q Q(直交行列)
   * @param r R(上三角行列)
   */
  public QRDecompositionElements(final S[][] q, final S[][] r) {
    this.q = q;
    this.r = r;
    this.containP = false;
  }

  /**
   * Q(直交行列)を返します。
   * 
   * @return Q(直交行列)
   */
  public final S[][] getQ() {
    return this.q;
  }

  /**
   * R(上三角行列)を返します。
   * 
   * @return R(上三角行列)
   */
  public final S[][] getR() {
    return this.r;
  }

  /**
   * P(並び替え行列、直交行列)を返します。
   * 
   * @return P(並び替え行列、直交行列)
   */
  public final int[][] getP() {
    return this.p;
  }

  /**
   * 並び替え行列を含むか判定します。
   * 
   * @return 並び替え行列を含むならばtrue
   */
  public final boolean containP() {
    return this.containP;
  }
}
