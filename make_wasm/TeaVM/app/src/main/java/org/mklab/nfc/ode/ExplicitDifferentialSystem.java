/*
 * $Id: DifferentialSystem.java,v 1.14 2007/05/28 03:59:43 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * 陽的常微分方程式で表現されるシステムを表すインターフェースです。
 * 
 * <p>このインターフェースを実装したクラスで表現されるシステムの シミュレーション(時間応答を求めること)は {@link org.mklab.nfc.ode.DifferentialEquationSolver}クラスを用いてできます。
 * 
 * @author matsuki
 * @version $Revision: 1.14 $, 2004/05/08
 * 
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 */
public interface ExplicitDifferentialSystem<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> extends DifferentialSystem<RS,RM,CS,CM> {

  /**
   * 時刻<code>t</code>、状態<code>x</code>、外部信号(入力と出力)<code>inputOutput</code> から状態の微分を返します。
   * 
   * @param t 時刻
   * @param x 時刻<code>t</code>における状態
   * @param inputOutput 時刻<code>t</code>における外部信号(入力と出力)
   * @return 状態の微分
   * @exception SolverStopException ソルバーが停止された場合
   */
  RM differentialEquation(RS t, RM x, RM inputOutput) throws SolverStopException;
  
  /**
   * 方程式Mx'=f(t,x,u)のM行列を返します。
   * 
   * @return M行列
   */
  RM getMatrixM();
  
  /**
   * Dx f(t,x,u)を返します。
   * 
   * @param t 時刻
   * @param x 時刻<code>t</code>における状態
   * @param u 時刻<code>t</code>における入力
   * 
   * @return ヤコビ行列
   */
  RM getJacobianMatrix(RS t, RM x, RM u);
  
  /**
   * 微分代数方程式で表されるシステムであるか判別します。
   * 
   * @return 微分代数方程式で表されるシステムならばtrue
   */
  boolean isDifferentialAlgebraicSystem();
  
  /**
   * ヤコビ行列を持っているか判定します。
   * 
   * @return ヤコビ行列を持っていればtrue
   */
  boolean hasJacobianMatrix();
  
  /**
   * 状態と状態の微分の初期値が整合しているか判定します。
   * 
   * @return 状態と状態の微分の初期値が整合していればtrue
   */
  boolean hasConsistentInitialValue();
}