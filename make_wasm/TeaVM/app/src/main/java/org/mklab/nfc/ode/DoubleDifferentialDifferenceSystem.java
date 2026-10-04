/*
 * $Id: DifferentialDifferenceSystem.java,v 1.18 2007/05/28 03:59:43 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.DoubleMatrix;


/**
 * 微分方程式で表現されるシステムと差分方程式で表現されるシステムが結合したシステムを表わすインターフェースです。
 * 
 * <p>このインターフェースを実装したクラスで表現されるシステムの シミュレーション(時間応答を求めること)は、 {@link org.mklab.nfc.ode.DifferentialEquationSolver}クラスを用いてできます。
 * 
 * @author koga
 * @version $Revision: 1.18 $, 2004/05/08
 * @see org.mklab.nfc.ode.ExplicitDifferentialSystem
 * @see org.mklab.nfc.ode.DifferenceSystem
 * @see org.mklab.nfc.ode.DifferentialEquationSolver
 */
public interface DoubleDifferentialDifferenceSystem extends DoubleSampling {

  /**
   * 時刻<code>t</code>、連続状態<code>xc</code>、離散状態<code>xd</code>、外部信号(入力と出力) <code>inputOutput</code>から連続状態の微分値を返します。
   * 
   * @param t 時刻
   * @param xc 時刻tにおける連続状態
   * @param xd 時刻tにおける離散状態
   * @param inputOutput 時刻tにおける外部信号(入力と出力)
   * @return 状態の微分
   * @exception SolverStopException ソルバーが停止された場合
   */
  DoubleMatrix differentialEquation(double t, DoubleMatrix xc, DoubleMatrix xd, DoubleMatrix inputOutput) throws SolverStopException;

  /**
   * 時刻<code>t</code>、連続状態<code>xc</code>、離散状態<code>xd</code>、外部信号(入力と出力) <code>inputOutput</code>から離散状態の次ステップの値を返します。
   * 
   * @param t 時刻
   * @param xc 時刻tにおける連続状態
   * @param xd 時刻tにおける離散状態
   * @param inputOutput 時刻tにおける外部信号(入力と出力)
   * @return 状態の微分
   * @exception SolverStopException ソルバーが停止された場合
   */
  DoubleMatrix differenceEquation(double t, DoubleMatrix xc, DoubleMatrix xd, DoubleMatrix inputOutput) throws SolverStopException;

  /**
   * 時刻<code>t</code>、連続状態<code>xc</code>、離散状態<code>xd</code>から外部信号(入力と出力)を返します。
   * 
   * @param t 時刻
   * @param xc 時刻tにおける連続状態
   * @param xd 時刻tにおける離散状態
   * @return 時刻tおける外部信号(入力と出力)
   * @exception SolverStopException ソルバーが停止された場合
   */
  DoubleMatrix inputOutputEquation(double t, DoubleMatrix xc, DoubleMatrix xd) throws SolverStopException;
}