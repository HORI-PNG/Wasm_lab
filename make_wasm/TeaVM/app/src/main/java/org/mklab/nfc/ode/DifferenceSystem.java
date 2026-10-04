/*
 * $Id: DifferenceSystem.java,v 1.19 2007/05/28 03:59:43 koga Exp $
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
 * 差分方程式で表現されるシステムを表すインターフェースです。
 * 
 * <p>このインターフェースを実装したクラスで表現されるシステムの シミュレーション(時間応答を求めること)は、 {@link org.mklab.nfc.ode.DifferenceEquationSolver}クラスを用いてできます。
 * 
 * @author koga
 * @version $Revision: 1.19 $, 2005/08/09
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 * @see org.mklab.nfc.ode.ExplicitDifferentialSystem
 * @see org.mklab.nfc.ode.DifferentialEquationSolver
 */
public interface DifferenceSystem<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> extends Sampling<RS,RM,CS,CM> {

  /**
   * ステップ<code>k</code>、状態<code>x</code>、外部信号(入力と出力)<code>inputOutput</code>から ステップ<code>(k+1)</code>の状態を返します。
   * 
   * @param k ステップ
   * @param x ステップkにおける状態
   * @param inputOutput ステップkにおける外部信号(入力と出力)
   * @return ステップ(k+1)における状態
   * @exception SolverStopException ソルバーが停止された場合
   */
  RM differenceEquation(int k, RM x, RM inputOutput) throws SolverStopException;

  /**
   * 時刻<code>t</code>、状態<code>x</code>、外部信号(入力と出力)<code>inputOutput</code>から ステップ<code>(t/T+1)</code>の状態を返します。
   * 
   * <p>ただし、<code>T</code>はサンプリング周期です。
   * 
   * @param t 時刻
   * @param x 時刻 t における状態
   * @param inputOutput 時刻 t における外部信号(入力と出力)
   * @return ステップ(t/T+1)における状態
   * @exception SolverStopException ソルバーが停止された場合
   */
  RM differenceEquation(RS t, RM x, RM inputOutput) throws SolverStopException;

  /**
   * ステップ<code>k</code>と状態<code>x</code>から外部信号(入力と出力)を返します。
   * 
   * @param k ステップ
   * @param x ステップkにおける状態
   * @return ステップkにおける外部信号(入力と出力)
   * @exception SolverStopException ソルバーが停止された場合
   */
  RM inputOutputEquation(int k, RM x) throws SolverStopException;

  /**
   * 時刻<code>t</code>と状態<code>x</code>から外部信号(入力と出力)を返します。
   * 
   * @param t 時刻
   * @param x 時刻kにおける状態
   * @return 時刻kにおける外部信号(入力と出力)
   * @exception SolverStopException ソルバーが停止された場合
   */
  RM inputOutputEquation(RS t, RM x) throws SolverStopException;

}