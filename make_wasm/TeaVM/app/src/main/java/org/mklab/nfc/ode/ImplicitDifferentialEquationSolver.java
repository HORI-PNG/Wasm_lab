/*
 * Created on 2015/11/15
 * Copyright (C) 2015 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * 陰的な常微分方程式の解を求めるソルバーを表すインターフェースです。
 * 
 * @author koga
 * @version $Revision$, 2015/11/15
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 */
public interface ImplicitDifferentialEquationSolver<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> {

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
  RM step(ImplicitDifferentialSystem<RS,RM,CS,CM> system, RS t, RM x, RS h) throws SolverStopException;

  /**
   * <code>t0</code>秒から<code>t1</code>秒までのシミュレーションを行い, 結果を {@link org.mklab.nfc.matrix.Matrix}の配列として返します。
   * 
   * @param system シミュレーション対象
   * @param t0 シミュレーション開始時刻
   * @param t1 シミュレーション終了時刻
   * @param x0 初期状態
   */
  void solve(ImplicitDifferentialSystem<RS,RM,CS,CM> system, RS t0, RS t1, RM x0);

  /**
   * 指定された許容誤差でシミュレーション計算を行います。
   * 
   * @param system シミュレーション対象システム
   * @param t0 シミュレーション開始時刻
   * @param t1 シミュレーション終了時刻
   * @param x0 初期状態
   */
  void solveAuto(ImplicitDifferentialSystem<RS,RM,CS,CM> system, RS t0, RS t1, RM x0);
}