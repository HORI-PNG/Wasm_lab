/*
 * Created on 2007/02/17
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.ode;

import java.util.List;

import org.mklab.nfc.matrix.DoubleMatrix;


/**
 * 区分的連続代数システム(区分的連続な代数方程式で表現されるシステム)を表わすインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.5 $, 2007/02/17
 */
public interface DoublePiecewiseContinuousAlgebraicSystem {

  /**
   * 区分の番号を返します。
   * 
   * @param t 時刻
   * @param u 入力
   * 
   * @return 区分の番号
   * @exception SolverStopException ソルバーが停止された場合
   */
  List<Integer> getPiece(double t, DoubleMatrix u) throws SolverStopException;

  /**
   * 指定された区間内([t1,t2]の不連続点の時刻を返します。
   * 
   * @param t1 不連続点の前の時刻
   * @param u1 不連続点の前の入力
   * @param t2 不連続点の後の時刻
   * @param u2 不連続点の後の入力
   * @return 指定された区間内([(t1,u1),(t2,u2)]の不連続点の時刻 <p>区間内に不連続点がなければNaN
   * @exception SolverStopException ソルバーが停止された場合
   */
  double getDiscontinuousPoint(double t1, DoubleMatrix u1, double t2, DoubleMatrix u2) throws SolverStopException;
}
