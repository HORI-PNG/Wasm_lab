/*
 * $Id: DifferentialSystem.java,v 1.14 2007/05/28 03:59:43 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.IntMatrix;


/**
 * 常微分方程式で表現されるシステムを表すインターフェースです。
 * 
 * <p>このインターフェースを実装したクラスで表現されるシステムの シミュレーション(時間応答を求めること)は {@link org.mklab.nfc.ode.DifferentialEquationSolver}クラスを用いてできます。
 * 
 * @author matsuki
 * @version $Revision: 1.14 $, 2004/05/08
 */
public interface DoubleDifferentialSystem {
  /**
   * 時刻<code>t</code>と状態<code>x</code>から外部信号(入力と出力)を返します。
   * 
   * @param t 時刻
   * @param x 時刻<code>t</code>における状態
   * @return 時刻<code>t</code>おける外部信号(入力と出力)
   * @exception SolverStopException ソルバーが停止された場合
   */
  DoubleMatrix inputOutputEquation(double t, DoubleMatrix x) throws SolverStopException;
  
  /**
   * 指数を返します。
   * 
   * @return 指数
   */
  IntMatrix getIndex();
  
  /**
   * 指数を設定します。
   * 
   * @param index 指数
   */
  void setIndex(IntMatrix index);
  
  /**
   * 状態の微分の初期値を設定します。
   * @param initialStateDerivative 状態の微分の初期値
   */
  void setInitialStateDerivative(final DoubleMatrix initialStateDerivative);

  /**
   * 状態の微分の初期値を返します。
   * @return 状態の微分の初期値
   */
  DoubleMatrix getInitialStateDerivative();
}