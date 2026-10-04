/*
 * $Id$
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.DoubleMatrix;


/**
 * 指定された許容誤差を満たす方程式の解を求めるソルバーを表すインターフェースです。
 * 
 * @author koga
 * @version $Revision$
 * 
 */
public interface DoubleEquationAutoSolver {

  /**
   * 絶対許容誤差を設定します。
   * 
   * @param absoluteTolerance 絶対許容誤差
   */
  void setAbsoluteTolerance(double absoluteTolerance);

  /**
   * 絶対許容誤差を返します。
   * 
   * @return 絶対許容誤差
   */
  double getAbsoluteTolerance();
  
  /**
   * 相対許容誤差を設定します。
   * 
   * @param relativeTolerance 相対許容誤差
   */
  void setRelativeTolerance(double relativeTolerance);

  /**
   * 相対許容誤差を返します。
   * 
   * @return 相対許容誤差
   */
  double getRelativeTolerance();
  
  /**
   * 初期ステップ幅を設定します。
   * 
   * @param initialStepSize 初期ステップ幅
   */
  void setInitialStepSize(double initialStepSize);

  /**
   * 初期ステップ幅を返します。
   * 
   * @return 初期ステップ幅
   */
  double getInitialStepSize();

  /**
   * 不連続点の時刻に関する許容誤差を設定します。
   * 
   * @param toleranceOfDiscontinuity 不連続点の時刻に関する許容誤差
   */
  void setToleranceOfDiscontinuity(double toleranceOfDiscontinuity);

  /**
   * 不連続点の時刻に関する許容誤差を返します。
   * 
   * @return 不連続点の時刻に関する許容誤差
   */
  double getToleranceOfDiscontinuity();

  /**
   * 刻み幅の変動可能最小値を設定します。
   * 
   * @param minimumTimeStep 刻み幅の変動可能最小値
   */
  void setMinimumTimeStep(double minimumTimeStep);

  /**
   * 刻み幅の変動可能最小値を返します。
   * 
   * @return 刻み幅の変動可能最小値
   */
  double getMinimumTimeStep();

  /**
   * 刻み幅の変動可能最大値を設定します。
   * 
   * @param maximumTimeStep 刻み幅の変動可能最大値
   */
  void setMaximumTimeStep(double maximumTimeStep);

  /**
   * 刻み幅の変動可能最大値を返します。
   * 
   * @return 刻み幅の変動可能最大値
   */
  double getMaximumTimeStep();

  /**
   * 指定された許容誤差を満たす次の時刻の状態を求めます。
   * 
   * @param equation 常微分方程式
   * @param t0 現時刻
   * @param x0 現状態
   * @param trialTimeStep 刻み幅の候補
   * @param minTimeStep 刻み幅の変動可能最小値
   * @param maxTimeStep 刻み幅の変動可能最大値
   * @param absoluteTolerance 絶対許容誤差
   * @param actualStepNextTrialStep 採用された刻み幅と次の時刻の刻み幅の候補を成分とする配列
   * 
   * @return 微分方程式の解{t,x}
   * @exception SolverStopException ソルバーが停止された場合
   */
  DoubleMatrix stepAuto(DoubleExplicitDifferentialEquation equation, double t0, DoubleMatrix x0, double trialTimeStep, double minTimeStep, double maxTimeStep, double absoluteTolerance, double[] actualStepNextTrialStep)
      throws SolverStopException;

  /**
   * 指定された許容誤差を満たす次の時刻の状態を求めます。
   * 
   * @param equation 微分差分方程式
   * @param t0 現在の時刻
   * @param xc0 微分方程式の現在の状態
   * @param xd0 差分方程式の現在の状態
   * @param trialTimeStep 刻み幅の候補
   * @param minTimeStep 刻み幅の変動可能最小値
   * @param maxTimeStep 刻み幅の変動可能最大値
   * @param absoluteTolerance 絶対許容誤差
   * @param actualStepNextTrialStep 採用された刻み幅と次の時刻の刻み幅の候補を成分とする配列
   * 
   * @return 微分差分方程式の解{t,x}
   * @exception SolverStopException ソルバーが停止された場合
   */
  DoubleMatrix stepAuto(DoubleDifferentialDifferenceEquation equation, double t0, DoubleMatrix xc0, DoubleMatrix xd0, double trialTimeStep, double minTimeStep, double maxTimeStep, double absoluteTolerance,
      double[] actualStepNextTrialStep) throws SolverStopException;

  /**
   * 指定された許容誤差を満たすシミュレーション結果を計算します。
   * 
   * <p>刻み幅の候補を引数として与える。実際に採用された刻み幅と次の時刻の刻み幅の候補が返されます。
   * 
   * @param system シミュレーション対象
   * @param t0 現時刻
   * @param x0 現状態
   * @param trialTimeStep 刻み幅の候補
   * @param minTimeStep 変動する刻み幅の最小値
   * @param maxTimeStep 変動する刻み幅の最大値
   * @param absoluteTolerance 絶対許容誤差
   * @param actualStepNextTrialStep 実際に採用された刻み幅と次の時刻の刻み幅の候補を成分とする配列
   * 
   * @return シミュレーション結果{t,x,io}
   * @exception SolverStopException ソルバーが停止された場合
   */
  DoubleMatrix stepAuto(DoubleExplicitDifferentialSystem system, double t0, DoubleMatrix x0, double trialTimeStep, double minTimeStep, double maxTimeStep, double absoluteTolerance, double[] actualStepNextTrialStep)
      throws SolverStopException;

  /**
   * 許容誤差を満たすシミュレーション計算を行います。
   * 
   * <p>刻み幅の候補を引数として与える。実際に採用された刻み幅と次の時刻の刻み幅の候補が返されます。
   * 
   * @param system シミュレーション対象
   * @param t0 現在の時刻
   * @param xc0 現在の連続時間システムの状態
   * @param xd0 現在の離散時間システムの状態
   * @param trialTimeStep 刻み幅の候補
   * @param minTimeStep 変動する刻み幅の最小値
   * @param maxTimeStep 変動する刻み幅の最大値
   * @param absoluteTolerance 絶対許容誤差
   * @param actualStepNextTrialStep 実際に採用された刻み幅と次の時刻の刻み幅の候補を成分とする配列
   * 
   * @return シミュレーション結果{t,xc,xd,io}
   * @exception SolverStopException ソルバーが停止された場合
   */
  DoubleMatrix stepAuto(DoubleDifferentialDifferenceSystem system, double t0, DoubleMatrix xc0, DoubleMatrix xd0, double trialTimeStep, double minTimeStep, double maxTimeStep, double absoluteTolerance,
      double[] actualStepNextTrialStep) throws SolverStopException;
}
