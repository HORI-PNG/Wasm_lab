/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.eig;

/**
 * 倍精度(double)型の行列の固有値と固有ベクトルを表すクラスです。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 */
public class EigenSolutionDoubleElements {

  /** 固有値ベクトルの実部。 */
  private double[] reValue;
  /** 固有値ベクトルの虚部。 */
  private double[] imValue;
  /** 固有ベクトルからなる行列の実部。 */
  private double[][] reVector;
  /** 固有ベクトルからなる行列の虚部。 */
  private double[][] imVector;

  /**
   * 新しく生成された{@link EigenSolutionDoubleElements}オブジェクトを初期化します。
   * 
   * @param reValue 固有値ベクトルの実部
   * @param imValue 固有値ベクトルの虚部
   * @param reVector 固有ベクトルからなる行列の実部
   * @param imVector 固有ベクトルからなる行列の虚部
   */
  public EigenSolutionDoubleElements(final double[] reValue, final double[] imValue, final double[][] reVector, final double[][] imVector) {
    this.reValue = reValue;
    this.imValue = imValue;
    this.reVector = reVector;
    this.imVector = imVector;
  }

  /**
   * 固有値ベクトルの実部を返します。
   * 
   * <p>固有値は、実部の降順に並べられます。
   * 
   * @return 固有値ベクトルの実部
   */
  public final double[] getReValue() {
    return this.reValue;
  }

  /**
   * 固有値ベクトルの虚部を返します。
   * 
   * <p>固有値は、実部の降順に並べられます。
   * 
   * @return 固有値ベクトルの虚部
   */
  public final double[] getImValue() {
    return this.imValue;
  }

  /**
   * 固有ベクトルからなる行列の実部を返します。
   * 
   * @return 固有ベクトルからなる行列の実部
   */
  public final double[][] getReVector() {
    return this.reVector;
  }

  /**
   * 固有ベクトルからなる行列の虚部を返します。
   * 
   * @return 固有ベクトルからなる行列の虚部
   */
  public final double[][] getImVector() {
    return this.imVector;
  }
}
