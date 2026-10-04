/*
 * Created on 2007/02/17
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * 区分的微分可能な方程式を表わすインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.3 $, 2007/02/17
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 */
public interface PiecewiseDifferentialEquation<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> {

  /**
   * 区分の番号を返します。
   * 
   * @param t 時刻
   * @param x 時刻<code>t</code>における変数の値
   * 
   * @return 区分の番号
   */
  int getPiece(RS t, RM x);

  /**
   * 指定された区間内([(t1,x1),(t2,x2)]の不連続点の時刻を返します。
   * 
   * @param t1 不連続点の前の時刻
   * @param x1 不連続点の前の変数の値
   * @param t2 不連続点の後の時刻
   * @param x2 不連続点の後の変数の値
   * @return 指定された区間内([(t1,x1),(t2,x2)]の不連続点の時刻 <p>区間内に不連続点がなければNaN
   */
  RS getDiscontinuousPoint(RS t1, RM x1, RS t2, RM x2);
}
