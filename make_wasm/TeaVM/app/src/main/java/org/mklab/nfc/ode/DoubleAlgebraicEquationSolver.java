/*
 * $Id: AlgebraicEquationSolver.java,v 1.32 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.scalar.DoubleNumberUtil;


/**
 * 代数方程式の解を求めるソルバーを表すクラスです。
 * 
 * @author koga
 * @version $Revision: 1.32 $, 2004/05/08
 */
public class DoubleAlgebraicEquationSolver extends DoubleEquationSolver {
  

  /**
   * <code>t0</code>秒から<code>t1</code>秒までの連続時間代数システムの入出力を返します。
   * 
   * @param system 連続時間代数システム
   * @param t0 シミュレーション開始時刻
   * @param t1 シミュレーション終了時刻
   * @exception SolverStopException ソルバーが停止された場合
   */
  public final void solve(final DoubleContinuousAlgebraicSystem system, final double t0, final double t1) throws SolverStopException {
    resetStopper();
    boolean ending = false;

    double t = t0;
    double tsav = t;
    final double localTimeStep = (t1 >= t0) ? Math.abs(getTimeStep()) : -Math.abs(getTimeStep());
    double nextTimeStep = localTimeStep;
    int count = 0;
    
    final int kmax;
    if (system instanceof DoublePiecewiseContinuousAlgebraicSystem) {
      kmax = Math.min(getMaximumDataSize(), (int)(Math.abs(t1 - t0) / getMinimumSavingInterval())*10 + 10);
    } else {
      kmax = Math.min(getMaximumDataSize(), (int)(Math.abs(t1 - t0) / getMinimumSavingInterval()) + 10);
    }

    // 初期時刻における入出力
    DoubleMatrix io = null;
    try {
      io = system.inputOutputEquation(t);
    } catch (SolverStopException e) {
      stop(e);
      return;
    }

    final DoubleMatrix ttData = io.createZero(1, kmax);
    final DoubleMatrix ioData = io.createZero(io.getRowSize(), kmax);

    while (count < kmax) {
      boolean timeToSave = Math.abs(t - tsav) - Math.abs(getMinimumSavingInterval()) >= -DoubleNumberUtil.EPS * 1.0e6;
      boolean saving = timeToSave || ending || isStopping() || count == 0 || isAtSavingPoint();
      if (saving && count < kmax) {
        if (count != 0) {
          try {
            io = system.inputOutputEquation(t);
          } catch (SolverStopException e) {
            stop(e);
            break;
          }
        }
        
        count++;
        ttData.setElement(count, t);
        ioData.setColumnVector(count, io);
        tsav = t;
        setAtSavingPoint(false);
        
        try {
          notifyObservers(t);
        } catch (InterruptedException e) {
          stop(new SolverInterruptedException(e));
          break;
        }
      }

      if (system instanceof DoublePiecewiseContinuousAlgebraicSystem) {
        setTrial(true);
        final DoubleMatrix newIO = system.inputOutputEquation(t + nextTimeStep);
        final double discontinuousPoint = ((DoublePiecewiseContinuousAlgebraicSystem)system).getDiscontinuousPoint(t, io, t + nextTimeStep, newIO);
        setTrial(false);

        final double toleranceOfDiscontinuity = 1.0E-10;

        if (Double.isNaN(discontinuousPoint) == false) {
          if (isSaveAtDiscontinuousPoint()) {
            setAtSavingPoint(true);
          }

          if (discontinuousPoint - t > toleranceOfDiscontinuity) {
            final double safetyFactor = 0.8;
            nextTimeStep = (discontinuousPoint - t) * safetyFactor;
          } else {
            nextTimeStep = toleranceOfDiscontinuity;
          }
        } else {
          nextTimeStep = localTimeStep;
        }
      }

      if (ending) {
        break;
      }

      // Last step size
      if ((t + nextTimeStep - t1) * (t + nextTimeStep - t0) >= 0.0) {
        ending = true;
        t = t1;
      } else {
        t = t + nextTimeStep;
      }
    }

    setTimeSeries(ttData.getColumnVectors(1, count));
    setDifferentialSolution(null);
    setDifferenceSolution(null);
    setAlgebraicSolution(ioData.getColumnVectors(1, count));
  }

  /**
   * <code>t0</code>秒から<code>t1</code>秒までの離散時間代数システムの入出力を返します。
   * 
   * @param system 離散時間代数システム
   * @param t0 シミュレーション開始時刻
   * @param t1 シミュレーション終了時刻
   */
  public final void solve(final DoubleDiscreteAlgebraicSystem system, final double t0, final double t1) {
    resetStopper();
    boolean ending = false;

    double t = t0;
    double tsav = t;
    int count = 0;
    final int kmax = Math.min(getMaximumDataSize(), (int)(Math.abs(t1 - t0) / getMinimumSavingInterval()) + 10);

    // 初期時刻における入出力
    DoubleMatrix io = null;
    try {
      system.setAtSamplingPoint(true);
      io = system.inputOutputEquation(t);
      system.setAtSamplingPoint(false);
    } catch (SolverStopException e) {
      stop(e);
      return;
    }

    final DoubleMatrix ttData = io.createZero(1, kmax);
    final DoubleMatrix ioData =io.createZero(io.getRowSize(), kmax);

    double samplingTimeTolerance = 1.0E-6;
    double nextSamplingTime = t0;

    while (count < kmax) {
      if (count != 0) {
        try {
          system.setAtSamplingPoint(true);
          io = system.inputOutputEquation(t);
          system.setAtSamplingPoint(false);
        } catch (SolverStopException e) {
          stop(e);
          break;
        }
      }

      boolean timeToSave = Math.abs(t - tsav) - Math.abs(getMinimumSavingInterval()) >= -DoubleNumberUtil.EPS * 1.0e6;
      boolean saving = timeToSave || ending || isStopping() || count == 0 || isSaveAtSamplingPoint();
      if (saving && count < kmax) {
        count++;
        ttData.setElement(count, t);
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
          io = system.inputOutputEquation(t);
        } catch (SolverStopException e) {
          stop(e);
          break;
        }

        if (count < kmax) {
          count++;
          ttData.setElement(count, t);
          ioData.setColumnVector(count, io);
        }
      }
    }

    setTimeSeries(ttData.getColumnVectors(1, count));
    setDifferentialSolution(null);
    setDifferenceSolution(null);
    setAlgebraicSolution(ioData.getColumnVectors(1, count));
  }

  /**
   * <code>t0</code>秒から<code>t1</code>秒までの連続時間代数システムの入出力を返します。
   * 
   * @param system 連続時間代数システム
   * @param t0 シミュレーション開始時刻
   * @param t1 シミュレーション終了時刻
   * @exception SolverStopException ソルバーが停止された場合
   */
  public final void solve(final DoubleContinuousDiscreteAlgebraicSystem system, final double t0, final double t1) throws SolverStopException {
    resetStopper();
    boolean ending = false;

    double t = t0;
    double tsav = t;
    final double localTimeStep = (t1 >= t0) ? Math.abs(getTimeStep()) : -Math.abs(getTimeStep());
    double nextTimeStep = localTimeStep;

    int count = 0;
    final int kmax;
    if (system instanceof DoublePiecewiseContinuousDiscreteAlgebraicSystem) {
      kmax = Math.min(getMaximumDataSize(), (int)(Math.abs(t1 - t0) / getMinimumSavingInterval())*10 + 10);
    } else {
      kmax = Math.min(getMaximumDataSize(), (int)(Math.abs(t1 - t0) / getMinimumSavingInterval()) + 10);
    }

    // 初期時刻における入出力
    DoubleMatrix io = null;
    try {
      system.setAtSamplingPoint(true);
      io = system.inputOutputEquation(t);
      system.setAtSamplingPoint(false);
    } catch (SolverStopException e) {
      stop(e);
      return;
    }

    final DoubleMatrix ttData = io.createZero(1, kmax);
    final DoubleMatrix ioData = io.createZero(io.getRowSize(), kmax);

    boolean samplingPoint = true;
    final double samplingTimeTolerance = 1.0E-6;
    double nextSamplingTime = t0;

    while (count < kmax) {
      boolean timeToSave = Math.abs(t - tsav) - Math.abs(getMinimumSavingInterval()) >= -DoubleNumberUtil.EPS * 1.0e6;
      boolean saving = timeToSave || ending || isStopping() || count == 0 || isAtSavingPoint() || (samplingPoint && isSaveAtSamplingPoint());
      if (saving && count < kmax) {
        if (count != 0) {
          try {
            io = system.inputOutputEquation(t);
          } catch (SolverStopException e) {
            stop(e);
            break;
          }
        }

        count++;
        ttData.setElement(count, t);
        ioData.setColumnVector(count, io);
        tsav = t;
        setAtSavingPoint(false);
        
        try {
          notifyObservers(t);
        } catch (InterruptedException e) {
          stop(new SolverInterruptedException(e));
          break;
        }
      }

      if (system instanceof DoublePiecewiseContinuousDiscreteAlgebraicSystem) {
        setTrial(true);
        final DoubleMatrix newIO = system.inputOutputEquation(t + nextTimeStep);
        final double discontinuousPoint = ((DoublePiecewiseContinuousDiscreteAlgebraicSystem)system).getDiscontinuousPoint(t, io, t + nextTimeStep, newIO);
        setTrial(false);

        final double toleranceOfDiscontinuity = 1.0E-10;

        if (Double.isNaN(discontinuousPoint) == false) {
          if (isSaveAtDiscontinuousPoint()) {
            setAtSavingPoint(true);
          }

          if (discontinuousPoint - t > toleranceOfDiscontinuity) {
            final double safetyFactor = 0.8;
            nextTimeStep = (discontinuousPoint - t) * safetyFactor;
          } else {
            nextTimeStep = toleranceOfDiscontinuity;
          }
        } else {
          nextTimeStep = localTimeStep;
        }
      }

      if (ending) {
        break;
      }

      if (samplingPoint) {
        samplingPoint = false;

        try {
          system.setAtSamplingPoint(true);
          final boolean isNotInitialTime = Double.doubleToLongBits(t) != Double.doubleToLongBits(t0);
          if (isNotInitialTime) {
            io = system.inputOutputEquation(t);
          }
          system.setAtSamplingPoint(false);
        } catch (SolverStopException e) {
          stop(e);
          break;
        }

        nextSamplingTime = system.getNextSamplingTime(t, samplingTimeTolerance);

        if (isSaveAtSamplingPoint() && count < kmax) {
          count++;
          ttData.setElement(count, t);
          ioData.setColumnVector(count, io);
          tsav = t;
        }
      }

      // Last step size
      if ((t + nextTimeStep - t1) * (t + nextTimeStep - t0) >= 0.0) {
        ending = true;
        t = t1;
      } else if ((t + nextTimeStep - nextSamplingTime) * (t + nextTimeStep - t0) >= 0.0) {
        // last step size to sampling point
        t = nextSamplingTime;
        samplingPoint = true;
      } else {
        t = t + nextTimeStep;
      }
    }

    setTimeSeries(ttData.getColumnVectors(1, count));
    setDifferentialSolution(null);
    setDifferenceSolution(null);
    setAlgebraicSolution(ioData.getColumnVectors(1, count));
  }

  /**
   * 方程式の解を返します。
   * 
   * @return 方程式の解
   */
  public final DoubleMatrix getSolution() {
    return getAlgebraicSolution();
  }
}