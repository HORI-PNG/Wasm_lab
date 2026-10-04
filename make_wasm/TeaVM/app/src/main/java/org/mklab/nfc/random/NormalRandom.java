/*
 * Created on 2008/03/08
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.random;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 平均0、分散1の正規分布乱数生成器を表わすクラスです。
 * 
 * @author koga
 * @version $Revision: 1.2 $, 2008/03/08
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public class NormalRandom<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> implements RandomGenerator<S,M> {

  /** 0〜1の範囲の一様分布乱数生成器。 */
  private RandomGenerator<S,M> uniformRandom;

  /** 次の乱数。 */
  private S nextValue;

  /**
   * 新しく生成された<code>NormalRandom</code>オブジェクトを初期化します。
   * 
   * @param uniformRandom 一様乱数生成器
   */
  public NormalRandom(final RandomGenerator<S,M> uniformRandom) {
    this.uniformRandom = uniformRandom;
  }

  /**
   * {@inheritDoc}
   */
  public final S nextValue() {
    if (this.nextValue == null) {
      final S alpha = this.uniformRandom.nextValue();
      final S beta = this.uniformRandom.nextValue();
      
      final S nextValue1;
      final S nextValue2;
      
      final S gain = alpha.log().multiply(-2).sqrt();
      final S phase = beta.multiply(2).multiply(beta.createPI());
      nextValue1 = gain.multiply(phase.cos());
      nextValue2 = gain.multiply(phase.sin()); 
      
      this.nextValue = nextValue2;
      return nextValue1;
    }
    
    final S returnValue = this.nextValue;
    this.nextValue = null;
    return returnValue;
  }
  
//  /**
//   * {@inheritDoc}
//   */
//  @SuppressWarnings("unchecked")
//  public final T nextValue() {
//    if (this.nextValue == null) {
//      final T alpha = this.uniformRandom.nextValue();
//      final T beta = this.uniformRandom.nextValue();
//      
//      final T nextValue1;
//      final T nextValue2;
//
//      if (alpha.isComplex()) {
//        final BaseComplexNumericalScalar<?> gain1 = (BaseComplexNumericalScalar<?>)((BaseComplexNumericalScalar<?>)alpha).getRealPart().log().multiply(-2).sqrt(); 
//        final BaseComplexNumericalScalar<?> phase1 = (BaseComplexNumericalScalar<?>)((BaseComplexNumericalScalar<?>)alpha).getImaginaryPart().multiply(2).multiply(((BaseComplexNumericalScalar<?>)alpha).getImaginaryPart().createPI());
//        
//        final BaseComplexNumericalScalar<?> gain2 = (BaseComplexNumericalScalar<?>)((BaseComplexNumericalScalar<?>)beta).getRealPart().log().multiply(-2).sqrt();
//        final BaseComplexNumericalScalar<?> phase2 = (BaseComplexNumericalScalar<?>)((BaseComplexNumericalScalar<?>)beta).getImaginaryPart().multiply(2).multiply(((BaseComplexNumericalScalar<?>)beta).getImaginaryPart().createPI()); 
//        
//        nextValue1 = (T)gain1.multiply(phase1.cos());
//        nextValue2 = (T)gain2.multiply(phase2.sin());
//      } else {
//        final T gain = alpha.log().multiply(-2).sqrt();
//        final T phase = beta.multiply(2).multiply(beta.createPI());
//        nextValue1 = gain.multiply(phase.cos());
//        nextValue2 = gain.multiply(phase.sin()); 
//      }
//      
//      this.nextValue = nextValue2;
//      return nextValue1;
//    }
//
//    final T returnValue = this.nextValue;
//    this.nextValue = null;
//    return returnValue;
//  }

  /**
   * {@inheritDoc}
   */
  public final void setSeed(final long seed) {
    this.uniformRandom.setSeed(seed);
  }
}
