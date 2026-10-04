/*
 * $Id: Sampling.java,v 1.5 2006/09/15 14:13:42 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.ode;

/**
 * サンプリングを表現するインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.5 $
 */
public interface DoubleSampling {

  /**
   * 次のサンプリング点の時間を返します。
   * 
   * @param t 現在の時間
   * @param tolerance 許容誤差
   * @return 次のサンプリング点の時間
   */
  double getNextSamplingTime(double t, double tolerance);

  /**
   * サンプリング点であるか判定します。
   * 
   * @return サンプリング点ならばtrue、そうでなければfalse
   */
  boolean isAtSamplingPoint();

  /**
   * サンプル点であるかを設定します。
   * 
   * @param samplingPoint サンプル点ならばtrue、そうでなければfalse
   */
  void setAtSamplingPoint(boolean samplingPoint);
}
