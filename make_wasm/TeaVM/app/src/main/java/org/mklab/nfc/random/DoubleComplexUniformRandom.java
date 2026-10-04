/*
 * Created on 2008/03/09
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.random;

import java.util.Random;

import org.mklab.nfc.matrix.DoubleComplexMatrix;
import org.mklab.nfc.scalar.DoubleComplexNumber;


/**
 * 0-1の範囲の倍精度(double)型の複素数の一様乱数生成器を表わすクラスです。
 * 
 * @author koga
 * @version $Revision: 1.1 $, 2008/03/09
 */
public class DoubleComplexUniformRandom implements RandomGenerator<DoubleComplexNumber,DoubleComplexMatrix> {
  /** 乱数生成器。 */
  private Random realRandom;
  /** 乱数生成器。 */
  private Random imagRandom;

  /**
   * 新しく生成された<code>DoubleComplexUniformRandom</code>オブジェクトを初期化します。
   */
  public DoubleComplexUniformRandom() {
    this.realRandom = new Random();
    this.imagRandom = new Random();
  }

  /**
   * 新しく生成された<code>DoubleComplexUniformRandom</code>オブジェクトを初期化します。
   * 
   * @param realSeed 実部の乱数生成器の種
   * @param imagSeed 虚部の乱数生成器の種
   */
  public DoubleComplexUniformRandom(final long realSeed, final long imagSeed) {
    this.realRandom = new Random(realSeed);
    this.imagRandom = new Random(imagSeed);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexNumber nextValue() {
    return new DoubleComplexNumber(this.realRandom.nextDouble(), this.imagRandom.nextDouble());
  }

  /**
   * {@inheritDoc}
   */
  public final void setSeed(final long seed) {
    this.realRandom = new Random(seed);
  }

  /**
   * Sets seed of real part of random.
   * 
   * @param seed seed
   */
  public final void setRealSeed(final long seed) {
    this.realRandom = new Random(seed);
  }

  /**
   * Sets seed of imaginary part of random.
   * 
   * @param seed seed
   */
  public final void setImaginarySeed(final long seed) {
    this.imagRandom = new Random(seed);
  }

}
