/*
 * $Id: ContinuousAlgebraicSystem.java,v 1.11 2007/05/28 03:59:42 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.DoubleMatrix;


/**
 * 連続代数方程式で表現されるシステムを表わすインターフェースです。
 * 
 * <p>このインターフェースを実装したクラスで表現されるシステムの シミュレーション(時間応答を求めること)は、 {@link org.mklab.nfc.ode.AlgebraicEquationSolver}クラスを用いてできます。
 * 
 * 
 * @author koga
 * @version $Revision: 1.11 $, 2004/05/08
 * @see org.mklab.nfc.ode.DifferentialDifferenceSystem
 * @see org.mklab.nfc.ode.DifferenceSystem
 */
public interface DoubleContinuousAlgebraicSystem {
  

  /**
   * 時刻<code>t</code>における外部信号(入力と出力)を返します。
   * 
   * @param t 時刻
   * @return 時刻tおける外部信号(入力と出力)
   * @exception SolverStopException ソルバーが停止された場合
   */
  DoubleMatrix inputOutputEquation(double t) throws SolverStopException;
}