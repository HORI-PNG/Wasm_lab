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
 * <blockquote> Q <sup># </sup>* Q =I </blockquote>
 * 
 * <blockquote> Z <sup># </sup>* Z = I </blockquote>
 * 
 * の関係が成り立ちます。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 */
public class QZDecompositionDoubleComplexElements {

  /** AA(上三角行列)の実部。 */
  private double[][] reAA;
  /** AA(上三角行列)の虚部。 */
  private double[][] imAA;
  /** BB(上 三角行列)の実部。 */
  private double[][] reBB;
  /** BB(上 三角行列)の虚部。 */
  private double[][] imBB;
  /** Q(ユニタリー行列)の実部。 */
  private double[][] reQ;
  /** Q(ユニタリー行列)の虚部。 */
  private double[][] imQ;
  /** Z(ユニタリー行列)の実部。 */
  private double[][] reZ;
  /** Z(ユニタリー行列)の虚部。 */
  private double[][] imZ;
  /** X(一般化固有ベクトル)の実部。 */
  private double[][] reX;
  /** X(一般化固有ベクトル)の虚部。 */
  private double[][] imX;

  /**
   * 新しく生成された{@link QZDecompositionDoubleComplexElements}オブジェクトを初期化します。
   * 
   * @param reAA AA(上三角行列)の実部
   * @param imAA AA(上三角行列)の虚部
   * @param reBB BB(上三角行列)の実部
   * @param imBB BB(上三角行列)の虚部
   * @param reQ Q(ユニタリー行列)の実部
   * @param imQ Q(ユニタリー行列)の虚部
   * @param reZ Z(ユニタリー行列)の実部
   * @param imZ Z(ユニタリー行列)の虚部
   * @param reX X(一般化固有ベクトルの実部)
   * @param imX X(一般化固有ベクトルの虚部)
   */
  public QZDecompositionDoubleComplexElements(final double[][] reAA, final double[][] imAA, final double[][] reBB, final double[][] imBB, final double[][] reQ, final double[][] imQ, final double[][] reZ, final double[][] imZ, final double[][] reX,
      final double[][] imX) {
    this.reAA = reAA;
    this.imAA = imAA;
    this.reBB = reBB;
    this.imBB = imBB;
    this.reQ = reQ;
    this.imQ = imQ;
    this.reZ = reZ;
    this.imZ = imZ;
    this.reX = reX;
    this.imX = imX;
  }

  /**
   * 左変換行列Q(ユニタリー行列)の実部を返します。
   * 
   * @return 左変換行列Q(ユニタリー行列)の実部
   */
  public final double[][] getReQ() {
    return this.reQ;
  }

  /**
   * 左変換行列Q(ユニタリー行列)の虚部を返します。
   * 
   * @return 左変換行列Q(ユニタリー行列)の虚部
   */
  public final double[][] getImQ() {
    return this.imQ;
  }

  /**
   * 右変換行列Z(ユニタリー行列)の実部を返します。
   * 
   * @return 右変換行列Z(ユニタリー行列)の実部
   */
  public final double[][] getReZ() {
    return this.reZ;
  }

  /**
   * 右変換行列Z(ユニタリー行列)の虚部を返します。
   * 
   * @return 右変換行列Z(ユニタリー行列)の虚部
   */
  public final double[][] getImZ() {
    return this.imZ;
  }

  /**
   * AA(上三角行列)の実部を返します。
   * 
   * @return AA(上三角行列)の実部
   */
  public final double[][] getReAA() {
    return this.reAA;
  }

  /**
   * AA(上三角行列)の虚部を返します。
   * 
   * @return AA(上三角行列)の虚部
   */
  public final double[][] getImAA() {
    return this.imAA;
  }

  /**
   * BB(上三角行列)の実部を返します。
   * 
   * @return BB(上三角行列)の実部
   */
  public final double[][] getReBB() {
    return this.reBB;
  }

  /**
   * BB(上三角行列)の虚部を返します。
   * 
   * @return BB(上三角行列)の虚部
   */
  public final double[][] getImBB() {
    return this.imBB;
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
