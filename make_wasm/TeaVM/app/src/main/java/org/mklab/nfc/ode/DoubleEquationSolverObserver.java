/*
 * Created on 2007/11/04
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.ode;

/**
 * {@link EquationSolver}のオブザーバーを表わすインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.2 $, 2007/11/04
 */
public interface DoubleEquationSolverObserver {

  /**
   * {@link EquationSolver}の計算時間が進んだことを知らせます。
   * 
   * @param t 計算結果が確定した時間
   * @throws InterruptedException 計算の進行がキャンセルされば場合
   */
  void notify(final double t) throws InterruptedException;
}
