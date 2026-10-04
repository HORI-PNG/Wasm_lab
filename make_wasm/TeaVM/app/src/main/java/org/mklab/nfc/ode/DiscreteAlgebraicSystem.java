/*
 * $Id: DiscreteAlgebraicSystem.java,v 1.14 2007/05/28 03:59:42 koga Exp $
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
 * 離散時間代数方程式で表現されるシステムを表わすインターフェースです。
 * 
 * <p>このインターフェースを実装したクラスで表現されるシステムの シミュレーション(時間応答を求めること)は、 {@link org.mklab.nfc.ode.AlgebraicEquationSolver}クラスを用いてできます。
 * 
 * @author koga
 * @version $Revision: 1.14 $, 2005/08/09
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 * @see org.mklab.nfc.ode.ExplicitDifferentialSystem
 * @see org.mklab.nfc.ode.DifferentialEquationSolver
 */
public interface DiscreteAlgebraicSystem<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> extends Sampling<RS,RM,CS,CM> {

  /**
   * ステップ<code>k</code>における外部信号(入力と出力)を返します。
   * 
   * @param k ステップ
   * @return ステップkにおける外部信号(入力と出力)
   * @exception SolverStopException ソルバーが停止された場合
   */
  RM inputOutputEquation(int k) throws SolverStopException;

  /**
   * 時刻<code>t</code> における外部信号(入力と出力)を返します。
   * 
   * @param t 時刻
   * @return 時刻tにおける外部信号(入力と出力)
   * @exception SolverStopException ソルバーが停止された場合
   */
  RM inputOutputEquation(RS t) throws SolverStopException;

}