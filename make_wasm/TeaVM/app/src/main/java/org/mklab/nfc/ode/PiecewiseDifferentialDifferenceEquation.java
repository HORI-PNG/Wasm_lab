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
 * 区分的に連続な微分差分方程式を表わすインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.1 $, 2007/02/17
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 */
public interface PiecewiseDifferentialDifferenceEquation<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> {

  /**
   * 区分の番号を返します。
   * 
   * @param t 時刻
   * @param xc 時刻<code>t</code>における連続変数の値
   * @param xd 時刻<code>t</code>における離散変数の値
   * 
   * @return 区分の番号
   */
  int getPiece(RS t, RM xc, RM xd);

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
  RS getDiscontinuousPoint(RS t1, RM xc1, RM xd1, RS t2, RM xc2, RM xd2);
}
