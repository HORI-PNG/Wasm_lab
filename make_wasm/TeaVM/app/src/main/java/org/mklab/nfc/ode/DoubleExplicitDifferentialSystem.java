/*
 * $Id: DifferentialSystem.java,v 1.14 2007/05/28 03:59:43 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.DoubleMatrix;


/**
 * 陽的常微分方程式で表現されるシステムを表すインターフェースです。
 * 
 * <p>このインターフェースを実装したクラスで表現されるシステムの シミュレーション(時間応答を求めること)は {@link org.mklab.nfc.ode.DifferentialEquationSolver}クラスを用いてできます。
 * 
 * @author matsuki
 * @version $Revision: 1.14 $, 2004/05/08
 */
public interface DoubleExplicitDifferentialSystem extends DoubleDifferentialSystem {

  /**
   * 時刻<code>t</code>、状態<code>x</code>、外部信号(入力と出力)<code>inputOutput</code> から状態の微分を返します。
   * 
   * @param t 時刻
   * @param x 時刻<code>t</code>における状態
   * @param inputOutput 時刻<code>t</code>における外部信号(入力と出力)
   * @return 状態の微分
   * @exception SolverStopException ソルバーが停止された場合
   */
  DoubleMatrix differentialEquation(double t, DoubleMatrix x, DoubleMatrix inputOutput) throws SolverStopException;
  
  /**
   * 方程式Mx'=f(t,x,u)のM行列を返します。
   * 
   * @return M行列
   */
  DoubleMatrix getMatrixM();
  
  /**
   * Dx f(t,x,u)を返します。
   * 
   * @param t 時刻
   * @param x 時刻<code>t</code>における状態
   * @param u 時刻<code>t</code>における入力
   * 
   * @return ヤコビ行列
   */
  DoubleMatrix getJacobianMatrix(double t, DoubleMatrix x, DoubleMatrix u);
  
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