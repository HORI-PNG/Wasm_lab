/*
 * $Id: NonLinearFunction.java,v 1.1 2008/03/12 14:38:29 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.nleq;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.ode.SolverStopException;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 非線形関数を表すインターフェースです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.1 $, 2004/11/10
 * 
 * @param <M> 行列の型
 * @param <S> 成分の型
 */
public interface NonLinearFunction<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {

  /**
   * 連立非線形関数の値を返します。
   * 
   * @param x 関数の引数
   * @return 関数の値
   * @exception SolverStopException ソルバーが停止された場合
   */
  M eval(final M x) throws SolverStopException;
}