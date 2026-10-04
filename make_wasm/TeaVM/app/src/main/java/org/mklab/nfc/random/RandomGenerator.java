/*
 * Created on 2008/03/09
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.random;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 乱数生成器を表わすインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.2 $, 2008/03/09
 * @param <S> 成分の型
 * @param <M> 行列の型
 */
public interface RandomGenerator<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {

  /**
   * 次の乱数を返します。
   * 
   * @return 次の乱数
   */
  S nextValue();

  /**
   * 乱数生成器の種を設定します。
   * 
   * @param seed 乱数生成器の種
   */
  void setSeed(long seed);
}
