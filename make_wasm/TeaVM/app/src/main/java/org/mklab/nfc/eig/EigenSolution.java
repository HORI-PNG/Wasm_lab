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
 * 行列の固有値を対角成分とする対角行列をD、固有値に対応する固有ベクトルを横方向に並べた行列をXとすると、
 * 
 * <blockquote> A * X = X * D </blockquote>
 * 
 * の関係が成り立ちます。
 * 
 * <p>固有ベクトルはノルムが1.0となるよう正規化されています。 固有値は実部の降順に並べられています。固有ベクトルは、固有値に対応して並べられています。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 */
public class EigenSolution<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>>  {

  /** 固有値を対角成分とする対角行列の実部。 */
  private RM realValue;
  /** 固有値を対角成分とする対角行列の虚部。 */
  private RM imagValue;
  /** 固有ベクトルを横方向に並べた行列の実部。 */
  private RM realVector;
  /** 固有ベクトルを横方向に並べた行列の虚部。 */
  private RM imagVector;

  /**
   * 新しく生成された{@link EigenSolution}オブジェクトを初期化します。
   * 
   * @param realValue 固有値を対角成分とする対角行列の実部
   * @param imagValue 固有値を対角成分とする対角行列の虚部
   * @param realVector 固有ベクトルからなる行列の実部
   * @param imagVector 固有ベクトルからなる行列の虚部
   */
  public EigenSolution(final RM realValue, final RM imagValue, final RM realVector, final RM imagVector) {
    this.realValue = realValue;
    this.imagValue = imagValue;
    this.realVector = realVector;
    this.imagVector = imagVector;
  }

  /**
   * 固有値を対角成分とする対角行列の実部を返します。
   * 
   * @return 固有値を対角成分とする対角行列の実部
   */
  public final RM getRealValue() {
    return this.realValue;
  }

  /**
   * 固有値を対角成分とする対角行列の虚部を返します。
   * 
   * @return 固有値を対角成分とする対角行列の虚部
   */
  public final RM getImagValue() {
    return this.imagValue;
  }

  /**
   * 固有ベクトルを横方向に並べた行列の実部を返します。
   * 
   * @return 固有ベクトルを横方向に並べた行列の実部
   */
  public final RM getRealVector() {
    return this.realVector;
  }

  /**
   * 固有ベクトルを横方向に並べた行列の虚部を返します。
   * 
   * @return 固有ベクトルを横方向に並べた行列の虚部
   */
  public final RM getImagVector() {
    return this.imagVector;
  }
  
  /**
   * Returns eigen value.
   * 
   * @return eigen value.
   */
  public final CM getValue() {
    return this.realValue.createComplex(this.realValue, this.imagValue);
  }
  
  /**
   * Returns eigen vector.
   * 
   * @return eigen vector
   */
  public final CM getVector() {
    return this.realVector.createComplex(this.realVector, this.imagVector);
  }

}
