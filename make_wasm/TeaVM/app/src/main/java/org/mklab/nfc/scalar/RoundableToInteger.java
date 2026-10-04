/*
 * Created on 2006/12/21
 * Copyright (C) 2006 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.scalar;

/**
 * 整数に丸める方法を表すインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.5 $, 2006/12/21
 * @param <S> 丸められた結果のスカラーの型
 */
public interface RoundableToInteger<S> {

  /**
   * 大きい整数に丸めます。
   * 
   * @return 丸めた結果
   */
  S ceil();

  /**
   * 小さい整数に丸めます。
   * 
   * @return 丸めた結果
   */
  S floor();

  /**
   * ゼロ方向の整数に丸めます。
   * 
   * @return 丸めた結果
   */
  S fix();

  /**
   * 最も近い整数に丸めます。
   * 
   * @return 丸めた結果
   */
  S round();

  /**
   * 絶対値が小さい成分を0に丸めます。
   * 
   * @param tolerance 許容誤差
   * @return 丸めた結果
   */
  S roundToZero(double tolerance);
}
