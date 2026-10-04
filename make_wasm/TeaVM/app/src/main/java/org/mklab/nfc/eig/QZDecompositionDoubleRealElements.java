/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.eig;

/**
 * QZ分解を保持するクラスです。
 * 
 * <p>行列の間には、
 * 
 * <blockquote> A = Q * AA * Z </blockquote>
 * 
 * <blockquote> B = Q * BB * Z </blockquote>
 * 
 * <blockquote> Q <sup>T </sup>* Q =I </blockquote>
 * 
 * <blockquote> Z <sup>T </sup>* Z = I </blockquote>
 * 
 * の関係が成り立ちます。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 */
public class QZDecompositionDoubleRealElements {

  /** AA(上三角行列)。 */
  private double[][] aa;
  /** BB(上 三角行列)。 */
  private double[][] bb;
  /** Q(直交行列)。 */
  private double[][] q;
  /** Z(直交行列)。 */
  private double[][] z;
  /** X(一般化固有ベクトル)の実部。 */
  private double[][] reX;
  /** X(一般化固有ベクトル)の虚部。 */
  private double[][] imX;

  /**
   * 新しく生成された{@link QZDecompositionDoubleRealElements}オブジェクトを初期化します。
   * 
   * @param aa AA(上三角行列)
   * @param bb BB(上三角行列)
   * @param q Q(直交行列)
   * @param z Z(直交行列)
   * @param reX X(一般化固有ベクトルの実部)
   * @param imX X(一般化固有ベクトルの虚部)
   */
  public QZDecompositionDoubleRealElements(final double[][] aa, final double[][] bb, final double[][] q, final double[][] z, final double[][] reX, final double[][] imX) {
    this.aa = aa;
    this.bb = bb;
    this.q = q;
    this.z = z;
    this.reX = reX;
    this.imX = imX;
  }

  /**
   * 左変換行列Q(直交行列)を返します。
   * 
   * @return 左変換行列Q(直交行列)
   */
  public final double[][] getQ() {
    return this.q;
  }

  /**
   * 右変換行列Z(直交行列)を返します。
   * 
   * @return 右変換行列Z(直交行列)
   */
  public final double[][] getZ() {
    return this.z;
  }

  /**
   * AA(上三角行列)を返します。
   * 
   * @return AA(上三角行列)
   */
  public final double[][] getAA() {
    return this.aa;
  }

  /**
   * BB(上三角行列)を返します。
   * 
   * @return BB(上三角行列)
   */
  public final double[][] getBB() {
    return this.bb;
  }

  /**
   * X(一般化固有ベクトル)の実部を返します。
   * 
   * @return X(一般化固有ベクトル)の実部
   */
  public final double[][] getReX() {
    return this.reX;
  }

  /**
   * X(一般化固有ベクトル)の虚部を返します。
   * 
   * @return X(一般化固有ベクトル)の虚部
   */
  public final double[][] getImX() {
    return this.imX;
  }
}
