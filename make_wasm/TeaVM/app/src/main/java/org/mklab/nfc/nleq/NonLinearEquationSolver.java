/*
 * $Id: NonLinearEquationSolver.java,v 1.2 2008/03/12 15:13:55 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.nleq;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.ode.SolverStopException;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 連立非線形方程式の解を求めるクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.2 $, 2004/11/10
 * 
 * @param <M> 行列の型
 * @param <S> 成分の型
 */
public abstract class NonLinearEquationSolver<S extends NumericalScalar<S,M>,M extends NumericalMatrix<S,M>> {
  

  /** 仮の値を引数として方程式を呼び中の回数。 */
  private static int trialCount = 0;

  /**
   * 連立非線形方程式を解き、方程式の解を返します。
   * 
   * @param function 非線形ベクトル関数
   * 
   * @param initialValue 解の初期値
   * @return 方程式の解
   * @throws NotConvergedException 解が収束しない場合
   * @throws SolverStopException ソルバーが停止された場合
   */
  public abstract M solve(NonLinearFunction<S,M> function, M initialValue) throws NotConvergedException, SolverStopException;

  /**
   * 非線形方程式を解くために、仮の値を引数として方程式を呼び出し中であるか判定します。
   * 
   * @return 仮の値を引数とする呼び出し中ならばtrue、そうでなければfalse
   */
  public static boolean isTrial() {
    if (trialCount > 0) {
      return true;
    }
    return false;

  }

  /**
   * 非線形方程式を解くために、仮の値を引数として方程式を呼び出し中であることを設定します。
   * 
   * @param trial 仮の値を引数とする呼び出し中ならばtrue、そうでなければfalse
   */
  protected final void setTrial(final boolean trial) {
    if (trial) {
      trialCount++;
    } else {
      trialCount--;
    }
  }

  /**
   * 警告を出力します。
   * 
   * @param message メッセージ
   */
  public final void warning(final String message) {
    System.err.println(message);
  }

}