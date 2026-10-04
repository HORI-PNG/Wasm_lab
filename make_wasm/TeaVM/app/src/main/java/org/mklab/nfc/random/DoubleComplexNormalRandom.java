/*
 * Created on 2008/03/08
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.random;

import org.mklab.nfc.matrix.DoubleComplexMatrix;
import org.mklab.nfc.scalar.DoubleComplexNumber;
import org.mklab.nfc.scalar.DoubleNumber;


/**
 * 平均0、分散1の正規分布乱数生成器を表わすクラスです。
 * 
 * @author koga
 * @version $Revision: 1.2 $, 2008/03/08
 */
public class DoubleComplexNormalRandom  implements RandomGenerator<DoubleComplexNumber,DoubleComplexMatrix> {

  /** 0〜1の範囲の一様分布乱数生成器。 */
  private DoubleComplexUniformRandom uniformRandom;

  /** 次の乱数。 */
  private DoubleComplexNumber nextValue;

  /**
   * 新しく生成された<code>NormalRandom</code>オブジェクトを初期化します。
   * 
   * @param uniformRandom 一様乱数生成器
   */
  public DoubleComplexNormalRandom(final DoubleComplexUniformRandom uniformRandom) {
    this.uniformRandom = uniformRandom;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber nextValue() {
    if (this.nextValue == null) {
      final DoubleComplexNumber alpha = this.uniformRandom.nextValue();
      final DoubleComplexNumber beta = this.uniformRandom.nextValue();
      
      final DoubleComplexNumber nextValue1;
      final DoubleComplexNumber nextValue2;
      
      final DoubleNumber realGain = alpha.getRealPart().log().multiply(-2).sqrt();
      final DoubleNumber imagGain = alpha.getImaginaryPart().log().multiply(-2).sqrt();
      
      final DoubleNumber realPhase = beta.getRealPart().multiply(2).multiply(beta.getRealPart().createPI());
      final DoubleNumber imagPhase = beta.getImaginaryPart().multiply(2).multiply(beta.getImaginaryPart().createPI());

      final DoubleNumber realNextValue1 = realGain.multiply(realPhase.cos());
      final DoubleNumber imagNextValue1 = imagGain.multiply(imagPhase.cos());
      nextValue1 = new DoubleComplexNumber(realNextValue1, imagNextValue1);
      
      
      final DoubleNumber realNextValue2 = realGain.multiply(realPhase.sin()); 
      final DoubleNumber imagNextValue2 = imagGain.multiply(imagPhase.sin()); 
      nextValue2 = new DoubleComplexNumber(realNextValue2, imagNextValue2);
      
      this.nextValue = nextValue2;
      return nextValue1;
    }
    
    final DoubleComplexNumber returnValue = this.nextValue;
    this.nextValue = null;
    return returnValue;
  }

  /**
   * {@inheritDoc}
   */
  public final void setSeed(final long seed) {
    this.uniformRandom.setSeed(seed);
  }
}
