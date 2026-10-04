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
 * 行列の固有値と固有ベクトルを表すクラスです。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 * @param <RS> 実スカラーの型
 * @param <RM> 実行列の型
 * @param <CS> 複素スカラーの型 
 * @param <CM> 複素行列の型
 */
public class EigenSolutionElements<RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> {

  /** 固有値ベクトルの実部。 */
  private RS[] reValue;
  /** 固有値ベクトルの虚部。 */
  private RS[] imValue;
  /** 固有ベクトルからなる行列の実部。 */
  private RS[][] reVector;
  /** 固有ベクトルからなる行列の虚部。 */
  private RS[][] imVector;

  /**
   * 新しく生成された{@link EigenSolutionElements}オブジェクトを初期化します。
   * 
   * @param reValue 固有値ベクトルの実部
   * @param imValue 固有値ベクトルの虚部
   * @param reVector 固有ベクトルからなる行列の実部
   * @param imVector 固有ベクトルからなる行列の虚部
   */
  public EigenSolutionElements(final RS[] reValue, final RS[] imValue, final RS[][] reVector, final RS[][] imVector) {
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
  public final RS[] getReValue() {
    return this.reValue;
  }

  /**
   * 固有値ベクトルの虚部を返します。
   * 
   * <p>固有値は、実部の降順に並べられます。
   * 
   * @return 固有値ベクトルの虚部
   */
  public final RS[] getImValue() {
    return this.imValue;
  }

  /**
   * 固有ベクトルからなる行列の実部を返します。
   * 
   * @return 固有ベクトルからなる行列の実部
   */
  public final RS[][] getReVector() {
    return this.reVector;
  }

  /**
   * 固有ベクトルからなる行列の虚部を返します。
   * 
   * @return 固有ベクトルからなる行列の虚部
   */
  public final RS[][] getImVector() {
    return this.imVector;
  }
}
