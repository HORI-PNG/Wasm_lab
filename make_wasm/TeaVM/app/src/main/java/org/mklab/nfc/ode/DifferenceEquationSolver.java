/*
 * $Id: DifferenceEquationSolver.java,v 1.33 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * 差分方程式の解を求めるソルバーを表すクラスです。
 * 
 * @author koga
 * @version $Revision: 1.33 $, 2004/05/08
 * @param <RM> type of real scalar
 * @param <RS> type of real matrix
 * @param <CS> type of complex scalar
 * @param <CM> type of complex matrix
 */
public class DifferenceEquationSolver<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> extends EquationSolver<RS,RM,CS,CM> {
  
  /**
   * Creates {@link DifferenceEquationSolver}.
   * @param sunit unit of scalar
   */
  public DifferenceEquationSolver(RS sunit) {
    super(sunit);
  }
  
  /**
   * <code>t0</code>から<code>t1</code>までのシミュレーションを行い, 結果を {@link org.mklab.nfc.matrix.Matrix}の配列として返します。
   * 
   * @param system シミュレーション対象
   * @param t0 シミュレーション開始時刻
   * @param t1 シミュレーション終了時刻
   * @param xd0 離散時間システムの初期状態
   */
  public final void solve(final DifferenceSystem<RS,RM,CS,CM> system, final RS t0, final RS t1, final RM xd0) {
    resetStopper();
    boolean ending = false;

    RS t = t0;
    RS tsav = t;
    int count = 0;
    final int kmax = Math.min(getMaximumDataSize(), (int)(t1.subtract(t0).abs().divide(getMinimumSavingInterval()).multiply(2).add(10)).toDouble());

    // 初期時刻における入出力
    RM io0 = null;
    try {
      system.setAtSamplingPoint(true);
      io0 = system.inputOutputEquation(t, xd0);
      system.setAtSamplingPoint(false);
    } catch (SolverStopException e) {
      stop(e);
      return;
    }

    RM ttData = xd0.createZero(1, kmax);
    RM ioData = xd0.createZero(io0.getRowSize(), kmax);
    RM xdData = xd0.createZero(xd0.getRowSize(), kmax);

    //final RS samplingTimeTolerance = this.sunit.create(1.0E-6);
    final RS samplingTimeTolerance = this.sunit.create(10).power(6).inverse();
    RS nextSamplingTime = t0;

    RM io = io0;
    RM xd = xd0;
    RM xdNext = xd0;

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

      //boolean timeToSave = t.subtract(tsav).abs().subtract(getMinimumSavingInterval().abs()).isGreaterThanOrEquals(-DoubleNumberUtil.EPS * 1.0e6);
      boolean timeToSave = t.subtract(tsav).abs().subtract(getMinimumSavingInterval().abs()).isGreaterThanOrEquals(this.sunit.getMachineEpsilon().multiply(1000000).unaryMinus());
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

      if (nextSamplingTime.subtract(t1).multiply(nextSamplingTime.subtract(t0)).isGreaterThanOrEquals(0)) {
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
  public final void solve(final DifferenceEquation<RS,RM,CS,CM> equation, final RS t0, final RS t1, final RM xd0) {
    resetStopper();
    boolean ending = false;

    RS t = t0;
    RS tsav = t;
    int count = 0;
    int kmax = Math.min(getMaximumDataSize(), (int)(t1.subtract(t0).abs().divide(getMinimumSavingInterval()).multiply(2).add(10)).toDouble());

    RM ttData = xd0.createZero(1, kmax);
    RM xdData = xd0.createZero(xd0.getRowSize(), kmax);

    //RS samplingTimeTolerance = this.sunit.create(1.0E-6);
    RS samplingTimeTolerance = this.sunit.create(10).power(6).inverse();
    RS nextSamplingTime = t0;

    RM xd = xd0;
    RM xdNext = xd0;

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

      final RS tolerance = this.sunit.getMachineEpsilon().multiply(1000000);
      boolean timeToSave = t.subtract(tsav).abs().subtract(getMinimumSavingInterval().abs()).isGreaterThanOrEquals(tolerance.unaryMinus());
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

      if (nextSamplingTime.subtract(t1).multiply(nextSamplingTime.subtract(t0)).isGreaterThanOrEquals(0)) {
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
  public final RM getSolution() {
    return getDifferenceSolution();
  }
}