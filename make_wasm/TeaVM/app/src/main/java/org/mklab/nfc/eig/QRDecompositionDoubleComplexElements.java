/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.eig;

/**
 * 倍精度(double)型の複素行列の並べ替え付きQR分解(A*P=Q*R)を保持するためのクラスです。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 */
public class QRDecompositionDoubleComplexElements {

  /** Q(ユニタリー行列)の実部。 */
  private double[][] reQ;
  /** Q(ユニタリー行列)の虚部。 */
  private double[][] imQ;
  /** R(上三角行列)の実部。 */
  private double[][] reR;
  /** R(上三角行列)の虚部。 */
  private double[][] imR;
  /** P(並び替え行列、直交行列)。 */
  private int[][] p;
  /** 並び替え行列を含むならばtrue。 */
  private boolean containP = false;

  /**
   * 新しく生成された{@link QRDecompositionDoubleComplexElements}オブジェクトを初期化します。
   * 
   * @param reQ Q(ユニタリー行列)の実部
   * @param imQ Q(ユニタリー行列)の虚部
   * @param reR R(上三角行列)の実部
   * @param imR R(上三角行列)の虚部
   * @param p P(並び替え行列、直交行列)
   */
  public QRDecompositionDoubleComplexElements(final double[][] reQ, final double[][] imQ, final double[][] reR, final double[][] imR, final int[][] p) {
    this.reQ = reQ;
    this.imQ = imQ;
    this.reR = reR;
    this.imR = imR;
    this.p = p;
    this.containP = true;
  }

  /**
   * 新しく生成された{@link QRDecompositionDoubleComplexElements}オブジェクトを初期化します。
   * 
   * @param reQ Q(ユニタリー行列)の実部
   * @param imQ Q(ユニタリー行列)の虚部
   * @param reR R(上三角行列)の実部
   * @param imR R(上三角行列)の虚部
   */
  public QRDecompositionDoubleComplexElements(final double[][] reQ, final double[][] imQ, final double[][] reR, final double[][] imR) {
    this.reQ = reQ;
    this.imQ = imQ;
    this.reR = reR;
    this.imR = imR;
    this.containP = false;
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
   * R(上三角行列)の実部を返します。
   * 
   * @return R(上三角行列)の実部
   */
  public final double[][] getReR() {
    return this.reR;
  }

  /**
   * R(上三角行列)の虚部を返します。
   * 
   * @return R(上三角行列)の虚部
   */
  public final double[][] getImR() {
    return this.imR;
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
