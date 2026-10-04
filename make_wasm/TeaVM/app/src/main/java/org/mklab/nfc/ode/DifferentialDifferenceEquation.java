/*
 * $Id: DifferentialDifferenceEquation.java,v 1.17 2007/05/28 03:59:42 koga Exp $
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
 * 微分差分方程式を表すインターフェースです。
 * 
 * <p>このインターフェースを実装したクラスで表現される方程式は、 {@link org.mklab.nfc.ode.DifferentialEquationSolver}クラスを用いて解く (変数の時系列を求める)ことができます。
 * 
 * @author koga
 * @version $Revision: 1.17 $, 2004/05/0
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 * @see org.mklab.nfc.ode.ExplicitDifferentialEquation
 * @see org.mklab.nfc.ode.DifferenceEquation
 */
public interface DifferentialDifferenceEquation<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> extends Sampling<RS,RM,CS,CM> {

  /**
   * 時刻<code>t</code>、連続変数<code>xc</code>、離散変数<code>xd</code>から連続変数の微分値を返します。
   * 
   * @param t 時刻
   * @param xc 時刻tにおける連続変数の値
   * @param xd 時刻tにおける離散変数の値
   * @return 時刻tにおける連続変数の微分
   * @exception SolverStopException ソルバーが停止された場合
   */
  RM differentialEquation(RS t, RM xc, RM xd) throws SolverStopException;

  /**
   * 時刻<code>t</code>、連続変数<code>xc</code>、離散変数<code>xd</code>から離散変数の次ステップの値を返します 。
   * 
   * @param t 時刻
   * @param xc 時刻tにおける連続変数の値
   * @param xd 時刻tにおける離散変数の値
   * @return 時刻tにおける連続変数の微分
   * @exception SolverStopException ソルバーが停止された場合
   */
  RM differenceEquation(RS t, RM xc, RM xd) throws SolverStopException;

}