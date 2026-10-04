/*
 * Created on 2007/02/17
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.ode;

import java.util.List;

import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * 区分的微分可能システム(区分的微分可能な方程式で表現されるシステム)を表わすインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.5 $, 2007/02/17
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 */
public interface PiecewiseDifferentialSystem<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> {

  /**
   * 区分の番号を返します。
   * 
   * @param t 時刻
   * @param x 状態
   * @param u 入力
   * 
   * @return 区分の番号
   * @exception SolverStopException ソルバーが停止された場合
   */
  List<Integer> getPiece(RS t, RM x, RM u) throws SolverStopException;

  /**
   * 指定された区間内([(t1,x1),(t2,x2)]の不連続点の時刻を返します。
   * 
   * @param t1 不連続点の前の時刻
   * @param x1 不連続点の前の状態
   * @param u1 不連続点の前の入力
   * @param t2 不連続点の後の時刻
   * @param x2 不連続点の後の状態
   * @param u2 不連続点の後の入力
   * @return 指定された区間内([(t1,x1,u1),(t2,x2,u2)]の不連続点の時刻 <p>区間内に不連続点がなければNaN
   * @exception SolverStopException ソルバーが停止された場合
   */
  RS getDiscontinuousPoint(RS t1, RM x1, RM u1, RS t2, RM x2, RM u2) throws SolverStopException;
}
