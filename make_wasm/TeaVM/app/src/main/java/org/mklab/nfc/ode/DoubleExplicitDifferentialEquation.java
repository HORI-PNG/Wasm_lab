/*
 * $Id: DifferentialEquation.java,v 1.14 2007/11/01 10:40:41 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.DoubleMatrix;


/**
 * 陽的な常微分方程式を表すインターフェースです。
 * 
 * <p>このインターフェースを実装したクラスで表現される方程式は、 {@link org.mklab.nfc.ode.DifferentialEquationSolver}クラスを用いて を解く(変数の時系列を求める)ことができます。
 * 
 * @author koga
 * @version $Revision: 1.14 $, 2004/05/08
 * 
 */
public interface DoubleExplicitDifferentialEquation {
  
  /**
   * 時刻<code>t</code>と変数<code>x</code>から変数<code>x</code>の微分を返します。
   * 
   * @param t 時刻
   * @param x 時刻<code>t</code>における変数<code>x</code>の値
   * @return 時刻<code>t</code>における変数<code>x</code>の微分
   * @throws SolverStopException ソルバーが停止された場合
   */
  DoubleMatrix differentialEquation(double t, DoubleMatrix x) throws SolverStopException;
}