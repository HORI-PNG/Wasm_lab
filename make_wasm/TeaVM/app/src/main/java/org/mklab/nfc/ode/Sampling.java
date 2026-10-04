/*
 * $Id: Sampling.java,v 1.5 2006/09/15 14:13:42 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;

/**
 * サンプリングを表現するインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.5 $
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 */
public interface Sampling<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> {

  /**
   * 次のサンプリング点の時間を返します。
   * 
   * @param t 現在の時間
   * @param tolerance 許容誤差
   * @return 次のサンプリング点の時間
   */
  RS getNextSamplingTime(RS t, RS tolerance);

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
