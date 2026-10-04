/*
 * Created on 2007/02/17
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.DoubleMatrix;


/**
 * 区分的に連続な微分差分方程式を表わすインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.1 $, 2007/02/17
 */
public interface DoublePiecewiseDifferentialDifferenceEquation {

  /**
   * 区分の番号を返します。
   * 
   * @param t 時刻
   * @param xc 時刻<code>t</code>における連続変数の値
   * @param xd 時刻<code>t</code>における離散変数の値
   * 
   * @return 区分の番号
   */
  int getPiece(double t, DoubleMatrix xc, DoubleMatrix xd);

  /**
   * 指定された区間内([(t1,xc1,xd1),(t2,xc2,xd2)]の不連続点の時刻を返します。
   * 
   * @param t1 不連続点の前の時刻
   * @param xc1 不連続点の前の連続変数の値
   * @param xd1 不連続点の前の離散変数の値
   * @param t2 不連続点の後の時刻
   * @param xc2 不連続点の後の連続変数の値
   * @param xd2 不連続点の後の離散変数の値
   * @return 指定された区間内([(t1,xc1,xd1),(t2,xc2,xd2)]の不連続点の時刻 <p>区間内に不連続点がなければNaN
   */
  double getDiscontinuousPoint(double t1, DoubleMatrix xc1, DoubleMatrix xd1, double t2, DoubleMatrix xc2, DoubleMatrix xd2);
}
