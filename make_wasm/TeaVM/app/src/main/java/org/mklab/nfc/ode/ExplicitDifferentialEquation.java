/*
 * $Id: DifferentialEquation.java,v 1.14 2007/11/01 10:40:41 koga Exp $
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
 * 陽的な常微分方程式を表すインターフェースです。
 * 
 * <p>このインターフェースを実装したクラスで表現される方程式は、 {@link org.mklab.nfc.ode.DifferentialEquationSolver}クラスを用いて を解く(変数の時系列を求める)ことができます。
 * 
 * @author koga
 * @version $Revision: 1.14 $, 2004/05/08
 * 
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 */
public interface ExplicitDifferentialEquation<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> {
  
  /**
   * 時刻<code>t</code>と変数<code>x</code>から変数<code>x</code>の微分を返します。
   * 
   * @param t 時刻
   * @param x 時刻<code>t</code>における変数<code>x</code>の値
   * @return 時刻<code>t</code>における変数<code>x</code>の微分
   * @throws SolverStopException ソルバーが停止された場合
   */
  RM differentialEquation(RS t, RM x) throws SolverStopException;
}