/*
 * Created on 2015/11/15
 * Copyright (C) 2015 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.DoubleMatrix;


/**
 * 陰的な常微分方程式の解を求めるソルバーを表すインターフェースです。
 * 
 * @author koga
 * @version $Revision$, 2015/11/15
 */
public interface DoubleImplicitDifferentialEquationSolver {

  /**
   * <code>h</code>秒後の状態を返します。
   * 
   * @param system シミュレーション対象
   * @param t 現在の時刻
   * @param x 現在の状態
   * @param h 経過時間
   * 
   * @return h秒後の状態
   * @exception SolverStopException ソルバーが停止された場合
   */
  DoubleMatrix step(DoubleImplicitDifferentialSystem system, double t, DoubleMatrix x, double h) throws SolverStopException;

  /**
   * <code>t0</code>秒から<code>t1</code>秒までのシミュレーションを行い, 結果を {@link org.mklab.nfc.matrix.Matrix}の配列として返します。
   * 
   * @param system シミュレーション対象
   * @param t0 シミュレーション開始時刻
   * @param t1 シミュレーション終了時刻
   * @param x0 初期状態
   */
  void solve(DoubleImplicitDifferentialSystem system, double t0, double t1, DoubleMatrix x0);

  /**
   * 指定された許容誤差でシミュレーション計算を行います。
   * 
   * @param system シミュレーション対象システム
   * @param t0 シミュレーション開始時刻
   * @param t1 シミュレーション終了時刻
   * @param x0 初期状態
   */
  void solveAuto(DoubleImplicitDifferentialSystem system, double t0, double t1, DoubleMatrix x0);
}