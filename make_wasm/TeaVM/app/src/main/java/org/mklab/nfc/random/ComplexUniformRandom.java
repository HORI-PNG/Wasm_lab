/*
 * Created on 2008/03/09
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.random;

import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * 0〜1の範囲の倍精度複素数一様乱数生成器を表わすクラスです。
 * 
 * @author koga
 * @version $Revision: 1.2 $, 2008/03/09
 * @param <RS> 実スカラーの型
 * @param <RM> 実行列の型
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 */
public class ComplexUniformRandom<RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> implements RandomGenerator<CS,CM> {
  /** 乱数生成器。 */
  private RandomGenerator<RS,RM> generator;
  /** initial value */
  private CS value;

  /**
   * 新しく生成された<code>ComplexUniformRandom</code>オブジェクトを初期化します。
   * @param value 実部と虚部の型を表わすための値
   */
  public ComplexUniformRandom(final CS value) {
    this.value = value;
    this.generator = value.getRealPart().createUniformRandomGenerator();
  }

  /**
   * {@inheritDoc}
   */
  public final CS nextValue() {
    return this.value.create(this.generator.nextValue(), this.generator.nextValue());
  }

  /**
   * {@inheritDoc}
   */
  public final void setSeed(final long seed) {
    this.generator.setSeed(seed);
  }

}
