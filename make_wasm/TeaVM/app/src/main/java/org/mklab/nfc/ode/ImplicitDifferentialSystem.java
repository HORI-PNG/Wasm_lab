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
 * 陰的常微分方程式で表現されるシステムを表すインターフェースです。
 * 
 * <p>このインターフェースを実装したクラスで表現されるシステムの シミュレーション(時間応答を求めること)は {@link org.mklab.nfc.ode.DifferentialEquationSolver}クラスを用いてできます。
 * 
 * @author koga
 * 
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 */
public interface ImplicitDifferentialSystem<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> extends DifferentialSystem<RS,RM,CS,CM> {

  /**
   * 時刻<code>t</code>、状態<code>x</code>、状態の微分<code>dx</code>、外部信号(入力と出力)<code>inputOutput</code> から陰的常微分方程式の残差を返します。
   * 
   * @param t 時刻
   * @param x 時刻<code>t</code>における状態
   * @param dx 時刻<code>t</code>における状態の微分
   * @param inputOutput 時刻<code>t</code>における外部信号(入力と出力)
   * @return 陰的常微分方程式の残差
   * @exception SolverStopException ソルバーが停止された場合
   */
  RM differentialEquation(RS t, RM x, RM dx, RM inputOutput) throws SolverStopException;
}