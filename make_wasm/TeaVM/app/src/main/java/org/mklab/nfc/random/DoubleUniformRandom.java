/*
 * Created on 2008/03/09
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.random;

import java.util.Random;

import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.scalar.DoubleNumber;


/**
 * 0-1の範囲の倍精度型の実数の一様乱数生成器を表わすクラスです。
 * 
 * @author koga
 * @version $Revision: 1.1 $, 2008/03/09
 */
public class DoubleUniformRandom implements RandomGenerator<DoubleNumber,DoubleMatrix> {

  /** 乱数生成器。 */
  private Random random;

  /**
   * 新しく生成された<code>DoubleRandom</code>オブジェクトを初期化します。
   */
  public DoubleUniformRandom() {
    this.random = new Random();
  }

  /**
   * 新しく生成された<code>DoubleRandom</code>オブジェクトを初期化します。
   * 
   * @param seed 乱数生成器の種
   */
  public DoubleUniformRandom(final long seed) {
    this.random = new Random(seed);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber nextValue() {
    return new DoubleNumber(this.random.nextDouble());
  }

  /**
   * {@inheritDoc}
   */
  public final void setSeed(final long seed) {
    this.random = new Random(seed);
  }

}
