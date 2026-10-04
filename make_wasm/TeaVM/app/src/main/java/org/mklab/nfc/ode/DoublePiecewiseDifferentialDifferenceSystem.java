/*
 * Created on 2007/02/17
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.ode;

import java.util.List;

import org.mklab.nfc.matrix.DoubleMatrix;


/**
 * 区分的微分可能差分システム(区分的微分可能な方程式で表現されるシステム と差分方程式で表現されるシステムが結合したシステム)を表わすインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.4 $, 2007/02/17
 */
public interface DoublePiecewiseDifferentialDifferenceSystem {

  /**
   * 区分の番号を返します。
   * 
   * @param t 時刻
   * @param xc 時刻tにおける連続状態
   * @param xd 時刻tにおける離散状態
   * @param u 時刻tにおける入力
   * 
   * @return 区分の番号
   * @exception SolverStopException ソルバーが停止された場合
   */
  List<Integer> getPiece(double t, DoubleMatrix xc, DoubleMatrix xd, DoubleMatrix u) throws SolverStopException;

  /**
   * 指定された区間内([(t1,xc1,xd1,u1),(t2,xc2,xd2,u2)]の不連続点の時刻を返します。
   * 
   * @param t1 不連続点の前の時刻
   * @param xc1 不連続点の前の連続状態
   * @param xd1 不連続点の前の離散状態
   * @param u1 不連続点の前の入力
   * @param t2 不連続点の後の時刻
   * @param xc2 不連続点の後の連続状態
   * @param xd2 不連続点の後の離散状態
   * @param u2 不連続点の後の入力
   * @return 指定された区間内([(t1,xc1,xd1,u1),(t2,xc2,xd1,u2)]の不連続点の時刻 <p>区間内に不連続点がなければNaN
   * @exception SolverStopException ソルバーが停止された場合
   */
  double getDiscontinuousPoint(double t1, DoubleMatrix xc1, DoubleMatrix xd1, DoubleMatrix u1, double t2, DoubleMatrix xc2, DoubleMatrix xd2, DoubleMatrix u2) throws SolverStopException;
}
