/*
 * Created on 2007/11/04
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;

/**
 * {@link EquationSolver}のオブザーバーを表わすインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.2 $, 2007/11/04
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 */
public interface EquationSolverObserver<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> {

  /**
   * {@link EquationSolver}の計算時間が進んだことを知らせます。
   * 
   * @param t 計算結果が確定した時間
   * @throws InterruptedException 計算の進行がキャンセルされば場合
   */
  void notify(final RS t) throws InterruptedException;
}
