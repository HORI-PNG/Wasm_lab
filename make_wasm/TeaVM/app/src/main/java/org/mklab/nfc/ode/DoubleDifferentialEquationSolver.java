/*
 * $Id: DifferentialEquationSolver.java,v 1.34 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.scalar.DoubleNumberUtil;


/**
 * 常微分方程式の解を求めるソルバーを表す抽象クラスです。
 * 
 * <p>具体的な解法アルゴリズムはこのクラスを継承した子クラスで実装します。
 * 
 * @author matsuki
 * @version $Revision: 1.34 $, 2004/05/08
 */
public abstract class DoubleDifferentialEquationSolver extends DoubleEquationSolver {

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
  public abstract DoubleMatrix step(DoubleExplicitDifferentialSystem system, double t, DoubleMatrix x, double h) throws SolverStopException;

  /**
   * <code>h</code>秒後の状態を返します。
   * 
   * @param system シミュレーション対象
   * @param t 現在の時刻
   * @param xc 現在の連続時間システムの状態
   * @param xd 現在の離散時間システムの状態
   * @param h 経過時間
   * 
   * @return h秒後の状態
   * @exception SolverStopException ソルバーが停止された場合
   */
  public abstract DoubleMatrix step(DoubleDifferentialDifferenceSystem system, double t, DoubleMatrix xc, DoubleMatrix xd, double h) throws SolverStopException;

  /**
   * <code>t0</code>秒から<code>t1</code>秒までのシミュレーションを行い, 結果を {@link org.mklab.nfc.matrix.Matrix}の配列として返します。
   * 
   * @param system シミュレーション対象
   * @param t0 シミュレーション開始時刻
   * @param t1 シミュレーション終了時刻
   * @param x0 初期状態
   */
  public final void solve(final DoubleExplicitDifferentialSystem system, final double t0, final double t1, final DoubleMatrix x0) {
    resetStopper();
    boolean ending = false;

    double t = t0;
    double lastSavingTime = t;
    double localTimeStep = (t1 >= t0) ? Math.abs(getTimeStep()) : -Math.abs(getTimeStep());
    int ii = 0;
    int count = 0;
    final int kmax = (int)(Math.abs(t1 - t0) / getMinimumSavingInterval()) + 10;

    // 初期時刻における入出力
    DoubleMatrix io = null;
    try {
      io = system.inputOutputEquation(t, x0);
    } catch (SolverStopException e) {
      stop(e);
      return;
    }

    final DoubleMatrix ttData = x0.createZero(1, kmax);
    final DoubleMatrix ioData = x0.createZero(io.getRowSize(), kmax);
    final DoubleMatrix xcData = x0.createZero(x0.getRowSize(), kmax);

    DoubleMatrix x = x0;

    while (count < kmax) {
      boolean timeToSave = Math.abs(t - lastSavingTime) - Math.abs(getMinimumSavingInterval()) >= -DoubleNumberUtil.EPS * 1.0e6;
      boolean saving = timeToSave || ending || isStopping() || count == 0;
      if (saving && count < kmax) {
        if (count != 0) {
          try {
            io = system.inputOutputEquation(t, x);
          } catch (SolverStopException e) {
            stop(e);
            break;
          }
        }

        count++;
        ttData.setElement(count, t);
        xcData.setColumnVector(count, x);
        ioData.setColumnVector(count, io);
        lastSavingTime = t;
        
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

      /* Final step size */
      if ((t + localTimeStep - t1) * (t + localTimeStep - t0) >= 0.0) {
        ending = true;
        try {
          x = step(system, t, x, t1 - t);
        } catch (SolverStopException e) {
          stop(e);
          break;
        }

        t = t1;
      } else {
        try {
          x = step(system, t, x, localTimeStep);
        } catch (SolverStopException e) {
          stop(e);
          break;
        }

        ii++;
        t = t0 + ii * localTimeStep;
      }
    }

    setTimeSeries(ttData.getColumnVectors(1, count));
    setDifferentialSolution(xcData.getColumnVectors(1, count));
    setDifferenceSolution(null);
    setAlgebraicSolution(ioData.getColumnVectors(1, count));
  }

  /**
   * <code>t0</code>秒から<code>t1</code>秒までのシミュレーションを行い, 結果を {@link org.mklab.nfc.matrix.Matrix}の配列として返します。
   * 
   * @param system シミュレーション対象
   * @param t0 シミュレーション開始時刻
   * @param t1 シミュレーション終了時刻
   * @param xc0 連続時間システムの初期状態
   * @param xd0 離散時間システムの初期状態
   */
  public final void solve(final DoubleDifferentialDifferenceSystem system, final double t0, final double t1, final DoubleMatrix xc0, final DoubleMatrix xd0) {
    resetStopper();
    boolean ending = false;

    double t = t0;
    double lastSavingTime = t;
    double localTimeStep = (t1 >= t0) ? Math.abs(getTimeStep()) : -Math.abs(getTimeStep());

    int ii = 0;
    int count = 0;
    final int kmax = (int)(Math.abs(t1 - t0) / getMinimumSavingInterval()) + 10;

    // 初期時刻における入出力
    DoubleMatrix io = null;
    try {
      system.setAtSamplingPoint(true);
      io = system.inputOutputEquation(t, xc0, xd0);
      system.setAtSamplingPoint(false);
    } catch (SolverStopException e) {
      stop(e);
      return;
    }

    final DoubleMatrix ttData = xc0.createZero(1, kmax);
    final DoubleMatrix ioData = xc0.createZero(io.getRowSize(), kmax);
    final DoubleMatrix xcData = xc0.createZero(xc0.getRowSize(), kmax);
    final DoubleMatrix xdData = xc0.createZero(xd0.getRowSize(), kmax);

    boolean samplingPoint = true;
    final double samplingTimeTolerance = 1.0E-6;
    double nextSamplingTime = t0; 
    double prevSamplingTime = t0;

    DoubleMatrix xc = xc0;
    DoubleMatrix xd = xd0;

    while (count < kmax) {
      boolean timeToSave = Math.abs(t - lastSavingTime) - Math.abs(getMinimumSavingInterval()) >= -DoubleNumberUtil.EPS * 1.0e6;
      boolean saving = timeToSave || ending || isStopping() || count == 0 || (samplingPoint && isSaveAtSamplingPoint());
      if (saving && count < kmax) {
        if (count != 0) {
          try {
            io = system.inputOutputEquation(t, xc, xd);
          } catch (SolverStopException e) {
            stop(e);
            break;
          }
        }

        count++;
        ttData.setElement(count, t);
        xcData.setColumnVector(count, xc);
        xdData.setColumnVector(count, xd);
        ioData.setColumnVector(count, io);
        lastSavingTime = t;

        try {
          notifyObservers(t);
        } catch (InterruptedException e) {
          stop(new SolverInterruptedException(e));
          break;
        }
      }

      if (samplingPoint) {
        samplingPoint = false;

        try {
          system.setAtSamplingPoint(true);
          final boolean isNotInitialTime = Double.doubleToLongBits(t) != Double.doubleToLongBits(t0);
          if (isNotInitialTime) {
            io = system.inputOutputEquation(t, xc, xd);
          }
          xd = system.differenceEquation(t, xc, xd, io);
          system.setAtSamplingPoint(false);
        } catch (SolverStopException e) {
          stop(e);
          break;
        }

        prevSamplingTime = t;
        nextSamplingTime = system.getNextSamplingTime(t, samplingTimeTolerance);
        ii = 0;

        if (isSaveAtSamplingPoint() && count < kmax) {
          count++;
          ttData.setElement(count, t);
          xcData.setColumnVector(count, xc);
          xdData.setColumnVector(count, xd);
          ioData.setColumnVector(count, io);
          lastSavingTime = t;

          try {
            notifyObservers(t);
          } catch (InterruptedException e) {
            stop(new SolverInterruptedException(e));
            break;
          }
        }
      }

      if (ending) {
        break;
      }

      /* Final step size */
      if ((t + localTimeStep - t1) * (t + localTimeStep - t0) >= 0.0) {
        ending = true;
        try {
          xc = step(system, t, xc, xd, t1 - t);
        } catch (SolverStopException e) {
          stop(e);
          break;
        }

        t = t1;
      } else if ((t + localTimeStep - nextSamplingTime) * (t + localTimeStep - t0) >= 0.0) {
        /* Final step size to sampling point */
        try {
          xc = step(system, t, xc, xd, nextSamplingTime - t);
        } catch (SolverStopException e) {
          stop(e);
          break;
        }

        t = nextSamplingTime;
        samplingPoint = true;
      } else {
        try {
          xc = step(system, t, xc, xd, localTimeStep);
        } catch (SolverStopException e) {
          stop(e);
          break;
        }

        ii++;
        t = prevSamplingTime + ii * localTimeStep;
      }
    }

    setTimeSeries(ttData.getColumnVectors(1, count));
    setDifferentialSolution(xcData.getColumnVectors(1, count));
    setDifferenceSolution(xdData.getColumnVectors(1, count));
    setAlgebraicSolution(ioData.getColumnVectors(1, count));
  }

  /**
   * <code>h</code>秒後の微分方程式の解を返します。
   * 
   * @param equation 常微分方程式
   * @param t 現在の時刻
   * @param x 現在の値
   * @param h 経過時間
   * 
   * @return h秒後の微分方程式の解
   * @exception SolverStopException ソルバーが停止された場合
   */
  public abstract DoubleMatrix step(DoubleExplicitDifferentialEquation equation, double t, DoubleMatrix x, double h) throws SolverStopException;

  /**
   * <code>h</code>秒後の微分方程式の解を返します。
   * 
   * @param equation 微分差分方程式
   * @param t 現在の時刻
   * @param xc 現在の微分方程式の値
   * @param xd 現在の差分方程式の値
   * @param h 経過時間
   * 
   * @return h秒後の微分方程式の解
   * @exception SolverStopException ソルバーが停止された場合
   */
  public abstract DoubleMatrix step(DoubleDifferentialDifferenceEquation equation, double t, DoubleMatrix xc, DoubleMatrix xd, double h) throws SolverStopException;

  /**
   * <code>t0</code>秒から<code>t1</code>秒までの解を求め, 結果を {@link org.mklab.nfc.matrix.Matrix} の配列として返します。
   * 
   * @param equation 常微分方程式
   * @param t0 初期時刻
   * @param t1 最終時刻
   * @param x0 初期値
   */
  public final void solve(final DoubleExplicitDifferentialEquation equation, final double t0, final double t1, final DoubleMatrix x0) {
    resetStopper();
    boolean ending = false;

    double t = t0;
    double lastSavingTime = t;
    double localTimeStep = (t1 >= t0) ? Math.abs(getTimeStep()) : -Math.abs(getTimeStep());
    int ii = 0;
    int count = 0;
    int kmax = (int)(Math.abs(t1 - t0) / getMinimumSavingInterval()) + 10;

    final DoubleMatrix ttData = x0.createZero(1, kmax);
    final DoubleMatrix xcData = x0.createZero(x0.getRowSize(), kmax);

    DoubleMatrix x = x0;

    while (count < kmax) {
      boolean timeToSave = Math.abs(t - lastSavingTime) - Math.abs(getMinimumSavingInterval()) >= -DoubleNumberUtil.EPS * 1.0e6;
      boolean saving = timeToSave || ending || isStopping() || count == 0;
      if (saving && count < kmax) {
        count++;
        ttData.setElement(count, t);
        xcData.setColumnVector(count, x);
        lastSavingTime = t;

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

      /* Final step size */
      if ((t + localTimeStep - t1) * (t + localTimeStep - t0) >= 0.0) {
        ending = true;
        try {
          x = step(equation, t, x, t1 - t);
        } catch (SolverStopException e) {
          stop(e);
          break;
        }

        t = t1;
      } else {
        try {
          x = step(equation, t, x, localTimeStep);
        } catch (SolverStopException e) {
          stop(e);
          break;
        }

        ii++;
        t = t0 + ii * localTimeStep;
      }
    }

    setTimeSeries(ttData.getColumnVectors(1, count));
    setDifferentialSolution(xcData.getColumnVectors(1, count));
    setDifferenceSolution(null);
    setAlgebraicSolution(null);
  }

  /**
   * <code>t0</code>秒から<code>t1</code>秒までの解を求め, 結果を {@link org.mklab.nfc.matrix.Matrix} の配列として返します。
   * 
   * @param equation 微分差分方程式
   * @param t0 初期時刻
   * @param t1 最終時刻
   * @param xc0 微分方程式の解の初期値
   * @param xd0 差分方程式の解の初期値
   */
  public final void solve(final DoubleDifferentialDifferenceEquation equation, final double t0, final double t1, final DoubleMatrix xc0, final DoubleMatrix xd0) {
    resetStopper();
    boolean ending = false;

    double t = t0;
    double lastSavingTime = t;
    double localTimeStep = (t1 >= t0) ? Math.abs(getTimeStep()) : -Math.abs(getTimeStep());

    int ii = 0;
    int count = 0;
    int kmax = (int)(Math.abs(t1 - t0) / getMinimumSavingInterval()) + 10;

    final DoubleMatrix ttData = xc0.createZero(1, kmax);
    final DoubleMatrix xcData = xc0.createZero(xc0.getRowSize(), kmax);
    final DoubleMatrix xdData = xc0.createZero(xd0.getRowSize(), kmax);

    boolean samplingPoint = true;
    double samplingTimeTolerance = 1.0E-6;
    double nextSamplingTime = t0; 
    double prevSamplingTime = t0;

    DoubleMatrix xc = xc0;
    DoubleMatrix xd = xd0;
    DoubleMatrix xdNext = xd0;

    while (count < kmax) {
      boolean timeToSave = Math.abs(t - lastSavingTime) - Math.abs(getMinimumSavingInterval()) >= -DoubleNumberUtil.EPS * 1.0e6;
      boolean saving = timeToSave || ending || isStopping() || count == 0 || (samplingPoint && isSaveAtSamplingPoint());
      if (saving && count < kmax) {
        count++;
        ttData.setElement(count, t);
        xcData.setColumnVector(count, xc);
        xdData.setColumnVector(count, xd);
        lastSavingTime = t;

        try {
          notifyObservers(t);
        } catch (InterruptedException e) {
          stop(new SolverInterruptedException(e));
          break;
        }
      }

      if (samplingPoint) {
        samplingPoint = false;

        try {
          equation.setAtSamplingPoint(true);
          xd = xdNext;
          xdNext = equation.differenceEquation(t, xc, xd);
          equation.setAtSamplingPoint(false);
        } catch (SolverStopException e) {
          stop(e);
          break;
        }

        prevSamplingTime = t;
        nextSamplingTime = equation.getNextSamplingTime(t, samplingTimeTolerance);
        ii = 0;

        if (isSaveAtSamplingPoint() && count < kmax) {
          count++;
          ttData.setElement(count, t);
          xcData.setColumnVector(count, xc);
          xdData.setColumnVector(count, xd);
          lastSavingTime = t;

          try {
            notifyObservers(t);
          } catch (InterruptedException e) {
            stop(new SolverInterruptedException(e));
            break;
          }
        }
      }

      if (ending) {
        break;
      }

      if ((t + localTimeStep - t1) * (t + localTimeStep - t0) >= 0.0) {
        /* Final step size to ending point */
        ending = true;
        try {
          xc = step(equation, t, xc, xd, t1 - t);
        } catch (SolverStopException e) {
          stop(e);
          break;
        }

        t = t1;
      } else if ((t + localTimeStep - nextSamplingTime) * (t + localTimeStep - t0) >= 0.0) {
        /* Final step size to sampling point */
        try {
          xc = step(equation, t, xc, xd, nextSamplingTime - t);
        } catch (SolverStopException e) {
          stop(e);
          break;
        }

        t = nextSamplingTime;
        samplingPoint = true;
      } else {
        try {
          xc = step(equation, t, xc, xd, localTimeStep);
        } catch (SolverStopException e) {
          stop(e);
          break;
        }

        ii++;
        t = prevSamplingTime + ii * localTimeStep;
      }
    }

    setTimeSeries(ttData.getColumnVectors(1, count));
    setDifferentialSolution(xcData.getColumnVectors(1, count));
    setDifferenceSolution(xdData.getColumnVectors(1, count));
    setAlgebraicSolution(null);
  }

  /**
   * 方程式の解を返します。
   * 
   * @return 方程式の解
   */
  public final DoubleMatrix getSolution() {
    return getDifferentialSolution();
  }
}