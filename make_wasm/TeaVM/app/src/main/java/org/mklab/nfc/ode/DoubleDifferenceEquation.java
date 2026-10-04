/*
 * $Id: DifferenceEquation.java,v 1.18 2007/05/28 03:59:43 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.DoubleMatrix;

/**
 * 差分方程式を表すインターフェースです。
 * 
 * <p>このインターフェースを実装したクラスで表現される方程式は、 {@link org.mklab.nfc.ode.DifferenceEquationSolver} クラスを用いて解く(変数の時系列を求める)ことができます。
 * 
 * @author koga
 * @version $Revision: 1.18 $, 2005/08/09
 * @see org.mklab.nfc.ode.ExplicitDifferentialEquation
 * @see org.mklab.nfc.ode.DifferentialDifferenceEquation
 * @see org.mklab.nfc.ode.DifferentialEquationSolver
 */
public interface DoubleDifferenceEquation extends DoubleSampling {

  /**
   * ステップ<code>k</code>と変数<code>x</code>からステップ<code>(k+1)</code>変数の値を返します。
   * 
   * @param k ステップ
   * @param x 変数のステップ k における値
   * @return 変数のステップ(k+1) における値
   * @exception SolverStopException ソルバーが停止された場合
   */
  DoubleMatrix differenceEquation(int k, DoubleMatrix x) throws SolverStopException;

  /**
   * 時刻<code>t</code>と変数<code>x</code>からステップ<code>(t/T+1)</code>変数の値を返します。
   * 
   * <p>ただし、<code>T</code>はサンプリング周期です。
   * 
   * @param t 時刻
   * @param x 変数の時刻 t における値
   * @return 変数のステップ(t/T+1) における値
   * @exception SolverStopException ソルバーが停止された場合
   */
  DoubleMatrix differenceEquation(double t, DoubleMatrix x) throws SolverStopException;

}
