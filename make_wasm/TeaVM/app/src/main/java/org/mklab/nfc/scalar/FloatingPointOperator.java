/*
 * Created on 2011/01/18
 * Copyright (C) 2011 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.scalar;

/**
 * 浮動小数点数を表すインターフェースです。
 * @author nakashima
 * @version $Revision$, 2011/01/18
 */
public interface FloatingPointOperator {

  /**
   * 正の無限大方向で隣接する浮動小数点値を返します。
   * @return 正の無限大方向で隣接する浮動小数点値
   */
  FloatingPointOperator nextUp();

  /**
   * 負の無限大方向で隣接する浮動小数点値を返します。
   * 
   * @return 負の無限大方向で隣接する浮動小数点値
   */
  FloatingPointOperator nextDown();

  /**
   * 第一引数に隣接する第二引数の方向の浮動小数点数を返します。
   * 
   * @param direction 比較対象
   * @return 第一引数に隣接する第二引数の方向の浮動小数点数
   */
  FloatingPointOperator nextAfter(FloatingPointOperator direction);
}