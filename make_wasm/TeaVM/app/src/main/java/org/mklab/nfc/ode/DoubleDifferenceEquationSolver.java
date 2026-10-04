/*
 * $Id: DifferenceEquationSolver.java,v 1.33 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.scalar.DoubleNumberUtil;


/**
 * 差分方程式の解を求めるソルバーを表すクラスです。
 * 
 * @author koga
 * @version $Revision: 1.33 $, 2004/05/08
 */
public class DoubleDifferenceEquationSolver extends DoubleEquationSolver {
  
  /**
   * <code>t0</code>から<code>t1</code>までのシミュレーションを行い, 結果を {@link org.mklab.nfc.matrix.Matrix}の配列として返します。
   * 
   * @param system シミュレーション対象
   * @param t0 シミュレーション開始時刻
   * @param t1 シミュレーション終了時刻
   * @param xd0 離散時間システムの初期状態
   */
  public final void solve(final DoubleDifferenceSystem system, final double t0, final double t1, final DoubleMatrix xd0) {
    resetStopper();
    boolean ending = false;

    double t = t0;
    double tsav = t;
    int count = 0;
    final int kmax = Math.min(getMaximumDataSize(), (int)(Math.abs(t1 - t0) / getMinimumSavingInterval()) * 2 + 10);

    // 初期時刻における入出力
    DoubleMatrix io0 = null;
    try {
      system.setAtSamplingPoint(true);
      io0 = system.inputOutputEquation(t, xd0);
      system.setAtSamplingPoint(false);
    } catch (SolverStopException e) {
      stop(e);
      return;
    }

    DoubleMatrix ttData = xd0.createZero(1, kmax);
    DoubleMatrix ioData = xd0.createZero(io0.getRowSize(), kmax);
    DoubleMatrix xdData = xd0.createZero(xd0.getRowSize(), kmax);

    final double samplingTimeTolerance = 1.0E-6;
    double nextSamplingTime = t0;

    DoubleMatrix io = io0;
    DoubleMatrix xd = xd0;
    DoubleMatrix xdNext = xd0;

    while (count < kmax) {
      try {
        system.setAtSamplingPoint(true);
        xd = xdNext;
        if (count != 0) {
          io = system.inputOutputEquation(t, xd);
        }
        xdNext = system.differenceEquation(t, xd, io);
        system.setAtSamplingPoint(false);
      } catch (SolverStopException e) {
        stop(e);
        break;
      }

      boolean timeToSave = Math.abs(t - tsav) - Math.abs(getMinimumSavingInterval()) >= -DoubleNumberUtil.EPS * 1.0e6;
      boolean saving = timeToSave || ending || isStopping() || count == 0 || isSaveAtSamplingPoint();
      if (saving && count < kmax) {
        count++;
        ttData.setElement(count, t);
        xdData.setColumnVector(count, xd);
        ioData.setColumnVector(count, io);
        tsav = t;

        try {
          notifyObservers(t);
        } catch (InterruptedException e) {
          stop(new SolverInterruptedException(e));
          break;
        }
      }

      if (ending) {
        break;
      }

      nextSamplingTime = system.getNextSamplingTime(t, samplingTimeTolerance);

      if ((nextSamplingTime - t1) * (nextSamplingTime - t0) >= 0.0) {
        ending = true;
        t = t1;
      } else {
        t = nextSamplingTime;
      }

      if (timeToSave || isStopping() || count == 1 || isSaveAtSamplingPoint()) {
        try {
          io = system.inputOutputEquation(t, xd);
        } catch (SolverStopException e) {
          stop(e);
          break;
        }

        if (count < kmax) {
          count++;
          ttData.setElement(count, t);
          xdData.setColumnVector(count, xd);
          ioData.setColumnVector(count, io);
        }
      }

    }

    ttData = ttData.getColumnVectors(1, count);
    xdData = xdData.getColumnVectors(1, count);
    ioData = ioData.getColumnVectors(1, count);

    setTimeSeries(ttData);
    setDifferentialSolution(null);
    setDifferenceSolution(xdData);
    setAlgebraicSolution(ioData);
  }

  /**
   * <code>t0</code>から<code>t1</code>までの解を求め, 結果を{@link org.mklab.nfc.matrix.Matrix} の配列として返します。
   * 
   * @param equation 差分方程式
   * @param t0 初期時刻
   * @param t1 最終時刻
   * @param xd0 差分方程式の解の初期値
   */
  public final void solve(final DoubleDifferenceEquation equation, final double t0, final double t1, final DoubleMatrix xd0) {
    resetStopper();
    boolean ending = false;

    double t = t0;
    double tsav = t;
    int count = 0;
    int kmax = Math.min(getMaximumDataSize(), (int)(Math.abs(t1 - t0) / getMinimumSavingInterval()) * 2 + 10);

    DoubleMatrix ttData = xd0.createZero(1, kmax);
    DoubleMatrix xdData = xd0.createZero(xd0.getRowSize(), kmax);

    double samplingTimeTolerance = 1.0E-6;
    double nextSamplingTime = t0;

    DoubleMatrix xd = xd0;
    DoubleMatrix xdNext = xd0;

    while (count < kmax) {
      try {
        equation.setAtSamplingPoint(true);
        xd = xdNext;
        xdNext = equation.differenceEquation(t, xd);
        equation.setAtSamplingPoint(false);
      } catch (SolverStopException e) {
        stop(e);
        break;
      }

      boolean timeToSave = Math.abs(t - tsav) - Math.abs(getMinimumSavingInterval()) >= -DoubleNumberUtil.EPS * 1.0e6;
      boolean savingFirst = timeToSave || ending || isStopping() || count == 0 || isSaveAtSamplingPoint();
      if (savingFirst && count < kmax) {
        count++;
        ttData.setElement(count, t);
        xdData.setColumnVector(count, xd);
        tsav = t;

        try {
          notifyObservers(t);
        } catch (InterruptedException e) {
          stop(new SolverInterruptedException(e));
          break;
        }
      }

      if (ending) {
        break;
      }

      nextSamplingTime = equation.getNextSamplingTime(t, samplingTimeTolerance);

      if ((nextSamplingTime - t1) * (nextSamplingTime - t0) >= 0.0) {
        ending = true;
        t = t1;
      } else {
        t = nextSamplingTime;
      }

      boolean savingSecond = timeToSave || isStopping() || count == 1 || isSaveAtSamplingPoint();
      if (savingSecond && count < kmax) {
        count++;
        ttData.setElement(count, t);
        xdData.setColumnVector(count, xd);
      }
    }

    ttData = ttData.getColumnVectors(1, count);
    xdData = xdData.getColumnVectors(1, count);

    setTimeSeries(ttData);
    setDifferentialSolution(null);
    setDifferenceSolution(xdData);
    setAlgebraicSolution(null);
  }

  /**
   * 方程式の解を返します。
   * 
   * @return 方程式の解
   */
  public final DoubleMatrix getSolution() {
    return getDifferenceSolution();
  }
}