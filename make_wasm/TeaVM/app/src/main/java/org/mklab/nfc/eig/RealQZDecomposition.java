/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


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
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
 */
public class RealQZDecomposition<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> {

  /** AA(上三角行列)。 */
  private RM aa;
  /** BB(上 三角行列)。 */
  private RM bb;
  /** Q(ユニタリー行列)。 */
  private RM q;
  /** Z(ユニタリー行列)。 */
  private RM z;
  /** X(一般化固有ベクトル)。 */
  private CM x;

  /**
   * 新しく生成された{@link RealQZDecomposition}オブジェクトを初期化します。
   * 
   * @param aa AA(上三角行列)
   * @param bb BB(上三角行列)
   * @param q Q(ユニタリー行列)
   * @param z Z(ユニタリー行列)
   * @param x X(一般化固有ベクトルの実部)
   */
  public RealQZDecomposition(final RM aa, final RM bb, final RM q, final RM z, final CM x) {
    this.aa = aa;
    this.bb = bb;
    this.q = q;
    this.z = z;
    this.x = x;
  }

  /**
   * 左変換行列Q(ユニタリー行列)を返します。
   * 
   * @return 左変換行列Q(ユニタリー行列)
   */
  public final RM getQ() {
    return this.q;
  }

  /**
   * 右変換行列Z(ユニタリー行列)を返します。
   * 
   * @return 右変換行列Z(ユニタリー行列)
   */
  public final RM getZ() {
    return this.z;
  }

  /**
   * AA(上三角行列)を返します。
   * 
   * @return AA(上三角行列)
   */
  public final RM getAA() {
    return this.aa;
  }

  /**
   * BB(上三角行列)を返します。
   * 
   * @return BB(上三角行列)
   */
  public final RM getBB() {
    return this.bb;
  }

  /**
   * X(一般化固有ベクトル)を返します。
   * 
   * @return X(一般化固有ベクトル)
   */
  public final CM getX() {
    return this.x;
  }
}
