/*
 * Created on 2007/02/17
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.DoubleMatrix;


/**
 * 区分的微分可能な方程式を表わすインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.3 $, 2007/02/17
 */
public interface DoublePiecewiseDifferentialEquation {

  /**
   * 区分の番号を返します。
   * 
   * @param t 時刻
   * @param x 時刻<code>t</code>における変数の値
   * 
   * @return 区分の番号
   */
  int getPiece(double t, DoubleMatrix x);

  /**
   * 指定された区間内([(t1,x1),(t2,x2)]の不連続点の時刻を返します。
   * 
   * @param t1 不連続点の前の時刻
   * @param x1 不連続点の前の変数の値
   * @param t2 不連続点の後の時刻
   * @param x2 不連続点の後の変数の値
   * @return 指定された区間内([(t1,x1),(t2,x2)]の不連続点の時刻 <p>区間内に不連続点がなければNaN
   */
  double getDiscontinuousPoint(double t1, DoubleMatrix x1, double t2, DoubleMatrix x2);
}
