/*
 * $Id: DifferentialEquationAutoSolver.java,v 1.41 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.scalar.DoubleNumberUtil;


/**
 * 刻み幅を自動調節して常微分方程式の解を求めるソルバーを表す抽象クラスです。
 * 
 * <p>具体的な解法アルゴリズムはこのクラスを継承した子クラスで実装します。
 * 
 * @author matsuki
 * @version $Revision: 1.41 $, 2004/05/08
 * 
 */
public abstract class DoubleDifferentialEquationAutoSolver extends DoubleDifferentialEquationSolver implements DoubleEquationAutoSolver {

  /** 安全係数。 */
  private static final double SAFETY = 0.9;
  /** 絶対許容誤差 。*/
  private double absoluteTolerance = 1.0E-6;
  /** 相対許容誤差 。*/
  private double relativeTolerance = 1.0E-6;
  /** 初期ステップ幅。*/
  private double initialStepSize = 1.0E-6;
  /** 刻み幅の変動可能最小値。 */
  private double minimumTimeStep = DoubleNumberUtil.EPS;
  /** 刻み幅の変動可能最大値。 */
  private double maximumTimeStep = Double.MAX_VALUE;
  /** 不連続点の時刻に関する許容誤差。 */
  private double toleranceOfDiscontinuity = 1.0E-10;

  /**
   * {@inheritDoc}
   */
  public final void setAbsoluteTolerance(final double absoluteTolerance) {
    this.absoluteTolerance = absoluteTolerance;
  }

  /**
   * {@inheritDoc}
   */
  public final double getAbsoluteTolerance() {
    return this.absoluteTolerance;
  }
  
  /**
   * {@inheritDoc}
   */
  public final void setRelativeTolerance(final double relativeTolerance) {
    this.relativeTolerance = relativeTolerance;
  }

  /**
   * {@inheritDoc}
   */
  public final double getRelativeTolerance() {
    return this.relativeTolerance;
  }
  
  /**
   * {@inheritDoc}
   */
  public final void setInitialStepSize(final double initialStepSize) {
    this.initialStepSize = initialStepSize;
  }

  /**
   * {@inheritDoc}
   */
  public final double getInitialStepSize() {
    return this.initialStepSize;
  }

  /**
   * {@inheritDoc}
   */
  public final void setToleranceOfDiscontinuity(final double toleranceOfDiscontinuity) {
    this.toleranceOfDiscontinuity = toleranceOfDiscontinuity;
  }

  /**
   * {@inheritDoc}
   */
  public final double getToleranceOfDiscontinuity() {
    return this.toleranceOfDiscontinuity;
  }

  /**
   * {@inheritDoc}
   */
  public final void setMinimumTimeStep(final double minimumTimeStep) {
    this.minimumTimeStep = minimumTimeStep;
  }

  /**
   * {@inheritDoc}
   */
  public final double getMinimumTimeStep() {
    return this.minimumTimeStep;
  }

  /**
   * {@inheritDoc}
   */
  public final void setMaximumTimeStep(final double maximumTimeStep) {
    this.maximumTimeStep = maximumTimeStep;
  }

  /**
   * {@inheritDoc}
   */
  public final double getMaximumTimeStep() {
    return this.maximumTimeStep;
  }

  /**
   * 指定された許容誤差でシミュレーション計算を行います。
   * 
   * @param system シミュレーション対象システム
   * @param t0 シミュレーション開始時刻
   * @param t1 シミュレーション終了時刻
   * @param x0 初期状態
   */
  public void solveAuto(final DoubleExplicitDifferentialSystem system, final double t0, final double t1, final DoubleMatrix x0) {
    resetStopper();

    double[] trialTimeStep = new double[] {Math.exp(Math.log(this.absoluteTolerance) / 5.0 * DoubleDifferentialEquationAutoSolver.SAFETY)};

    double t = t0;
    double lastSavingTime = t;
    if (Math.min(0.1, Math.abs(t1 - t0) / 5) < this.maximumTimeStep) {
      this.maximumTimeStep = Math.min(0.1, Math.abs(t1 - t0) / 5);
    }
    double localTimeStep = (t1 >= t0) ? Math.min(this.maximumTimeStep, Math.abs(trialTimeStep[0])) : -Math.min(this.maximumTimeStep, Math.abs(trialTimeStep[0]));
    int count = 0;
    int kmax = Math.min(getMaximumDataSize(), (int)(Math.abs(t1 - t0) / getMinimumSavingInterval()) + 10);

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

    double[] actualStepNextTrialStep = new double[2];
    DoubleMatrix x = x0;

    while (true) {
      boolean ending = (t - t1) * (t1 - t0) >= 0.0;
      boolean saving = Math.abs(t - lastSavingTime) - Math.abs(getMinimumSavingInterval()) >= -DoubleNumberUtil.EPS * 1.0e6;

      if (saving || ending || isStopping() || count == 0 || isAtSavingPoint()) {
        if (count < kmax) {
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
          setAtSavingPoint(false);

          try {
            notifyObservers(t);
          } catch (InterruptedException e) {
            stop(new SolverInterruptedException(e));
            break;
          }
        } else {
          stop(new SolverTooManyDataException(Messages.getString("DifferentialEquationAutoSolver.0"))); //$NON-NLS-1$
          break;
        }
      }

      if (ending) {
        trialTimeStep[0] = actualStepNextTrialStep[0];
        break;
      }

      /* Last step size */
      if ((t + localTimeStep - t1) * (t + localTimeStep - t0) >= 0.0) {
        localTimeStep = t1 - t;
      }

      try {
        x = stepAuto(system, t, x, localTimeStep, this.minimumTimeStep, this.maximumTimeStep, this.absoluteTolerance, actualStepNextTrialStep);
      } catch (SolverStopException e) {
        stop(e);
        break;
      }

      t = t + actualStepNextTrialStep[0];
      localTimeStep = actualStepNextTrialStep[1];
    }

    setTimeSeries(ttData.getColumnVectors(1, count));
    setDifferentialSolution(xcData.getColumnVectors(1, count));
    setDifferenceSolution(null);
    setAlgebraicSolution(ioData.getColumnVectors(1, count));
  }

  /**
   * 指定された許容誤差でシミュレーション計算を行い、状態の時系列を返します。
   * 
   * @param system シミュレーション対象システム
   * @param t0 シミュレーション開始時刻
   * @param t1 シミュレーション終了時刻
   * @param xc0 連続時間システムの初期状態
   * @param xd0 離散時間システムの初期状態
   */
  public final void solveAuto(final DoubleDifferentialDifferenceSystem system, final double t0, final double t1, final DoubleMatrix xc0, final DoubleMatrix xd0) {
    resetStopper();

    final double[] trialTimeStep = new double[] {Math.exp(Math.log(this.absoluteTolerance) / 5.0 * DoubleDifferentialEquationAutoSolver.SAFETY)};

    double t = t0;
    double lastSavingTime = t;
    if (Math.min(0.1, Math.abs(t1 - t0) / 5) < this.maximumTimeStep) {
      this.maximumTimeStep = Math.min(0.1, Math.abs(t1 - t0) / 5);
    }
    double localTimeStep = (t1 >= t0) ? Math.min(this.maximumTimeStep, Math.abs(trialTimeStep[0])) : -Math.min(this.maximumTimeStep, Math.abs(trialTimeStep[0]));
    int count = 0;
    int kmax = Math.min(getMaximumDataSize(), (int)(Math.abs(t1 - t0) / getMinimumSavingInterval()) * 4 + 10);

    final double samplingTimeTolerance = 1.0E-6;
    if (isSaveAtSamplingPoint()) {
      final double minimumSamplingTime = system.getNextSamplingTime(t0, samplingTimeTolerance);
      kmax = Math.max(kmax, (int)(Math.abs(t1 - t0) / minimumSamplingTime) * 4 + 10);
    }

    DoubleMatrix io = null;
    try {
      system.setAtSamplingPoint(true);
      io = system.inputOutputEquation(t, xc0, xd0);
      system.setAtSamplingPoint(false);
    } catch (SolverStopException e) {
      stop(e);
      return;
    }

    final DoubleMatrix ttData =xc0.createZero(1, kmax);
    final DoubleMatrix ioData = xc0.createZero(io.getRowSize(), kmax);
    final DoubleMatrix xcData = xc0.createZero(xc0.getRowSize(), kmax);
    final DoubleMatrix xdData = xc0.createZero(xd0.getRowSize(), kmax);

    boolean atSamplingPoint = true;
    double nextSamplingTime = t0;
    DoubleMatrix xc = xc0;
    DoubleMatrix xd = xd0;
    DoubleMatrix xdNext = xd0;

    double[] actualStepNextTrialStep = new double[2];

    while (true) {
      boolean ending = (t - t1) * (t1 - t0) >= 0.0;
      boolean saving = Math.abs(t - lastSavingTime) - Math.abs(getMinimumSavingInterval()) >= -DoubleNumberUtil.EPS * 1.0e6;

      if (saving || ending || isStopping() || count == 0 || (atSamplingPoint && isSaveAtSamplingPoint()) || isAtSavingPoint()) {
        if (count < kmax) {
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
          setAtSavingPoint(false);

          try {
            notifyObservers(t);
          } catch (InterruptedException e) {
            stop(new SolverInterruptedException(e));
            break;
          }
        } else {
          stop(new SolverTooManyDataException(Messages.getString("DifferentialEquationAutoSolver.1"))); //$NON-NLS-1$
          break;
        }
      }

      if (atSamplingPoint) {
        atSamplingPoint = false;

        try {
          system.setAtSamplingPoint(true);
          xd = xdNext;
          final boolean isNotInitialTime = Double.doubleToLongBits(t) != Double.doubleToLongBits(t0);
          if (isNotInitialTime) {
            io = system.inputOutputEquation(t, xc, xd);
          }
          xdNext = system.differenceEquation(t, xc, xd, io);
          system.setAtSamplingPoint(false);
        } catch (SolverStopException e) {
          stop(e);
          break;
        }

        nextSamplingTime = system.getNextSamplingTime(t, samplingTimeTolerance);

        if (isSaveAtSamplingPoint()) {
          if (count < kmax) {
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
          } else {
            stop(new SolverTooManyDataException(Messages.getString("DifferentialEquationAutoSolver.2"))); //$NON-NLS-1$
            break;
          }
        }
      }

      if (ending) {
        trialTimeStep[0] = actualStepNextTrialStep[0];
        break;
      }

      if ((t + localTimeStep - nextSamplingTime) * (t + localTimeStep - t0) >= 0.0) {
        localTimeStep = nextSamplingTime - t;
        atSamplingPoint = true;
      }

      /* Last step size */
      if (Math.abs(t + localTimeStep - t1) < samplingTimeTolerance) {
        localTimeStep = t1 - t;
      }

      try {
        xc = stepAuto(system, t, xc, xd, localTimeStep, this.minimumTimeStep, nextSamplingTime - t, this.absoluteTolerance, actualStepNextTrialStep);
      } catch (SolverStopException e) {
        stop(e);
        break;
      }

      if (atSamplingPoint && actualStepNextTrialStep[0] < localTimeStep) {
        atSamplingPoint = false;
      }

      t = t + actualStepNextTrialStep[0];
      localTimeStep = actualStepNextTrialStep[1];
    }

    setTimeSeries(ttData.getColumnVectors(1, count));
    setDifferentialSolution(xcData.getColumnVectors(1, count));
    setDifferenceSolution(xdData.getColumnVectors(1, count));
    setAlgebraicSolution(ioData.getColumnVectors(1, count));
  }

  /**
   * 指定された許容誤差を満たすように微分方程式の解を求めます。
   * 
   * @param equation 微分方程式
   * @param t0 初期時刻
   * @param t1 最終時刻
   * @param x0 初期値
   */
  public final void solveAuto(final DoubleExplicitDifferentialEquation equation, final double t0, final double t1, final DoubleMatrix x0) {
    resetStopper();

    double[] trialTimeStep = new double[] {Math.exp(Math.log(this.absoluteTolerance) / 5.0 * DoubleDifferentialEquationAutoSolver.SAFETY)};

    double t = t0;
    double lastSavingTime = t;
    if (Math.min(0.1, Math.abs(t1 - t0) / 5) < this.maximumTimeStep) {
      this.maximumTimeStep = Math.min(0.1, Math.abs(t1 - t0) / 5);
    }
    double localTimeStep = (t1 >= t0) ? Math.min(this.maximumTimeStep, Math.abs(trialTimeStep[0])) : -Math.min(this.maximumTimeStep, Math.abs(trialTimeStep[0]));
    int count = 0;
    int kmax = Math.min(getMaximumDataSize(), (int)(Math.abs(t1 - t0) / getMinimumSavingInterval()) + 10);

    final DoubleMatrix ttData = x0.createZero(1, kmax);
    final DoubleMatrix xcData = x0.createZero(x0.getRowSize(), kmax);

    double[] actualStepNextTrialStep = new double[2];
    DoubleMatrix x = x0;

    while (true) {
      boolean ending = (t - t1) * (t1 - t0) >= 0.0;
      boolean saving = Math.abs(t - lastSavingTime) - Math.abs(getMinimumSavingInterval()) >= -DoubleNumberUtil.EPS * 1.0e6;

      if (saving || ending || isStopping() || count == 0 || isAtSavingPoint()) {
        if (count < kmax) {
          count++;
          ttData.setElement(count, t);
          xcData.setColumnVector(count, x);
          lastSavingTime = t;
          setAtSavingPoint(false);

          try {
            notifyObservers(t);
          } catch (InterruptedException e) {
            stop(new SolverInterruptedException(e));
            break;
          }
        } else {
          stop(new SolverTooManyDataException(Messages.getString("DifferentialEquationAutoSolver.3"))); //$NON-NLS-1$
          break;
        }
      }

      if (ending) {
        trialTimeStep[0] = actualStepNextTrialStep[0];
        break;
      }

      /* Last step size */
      if ((t + localTimeStep - t1) * (t + localTimeStep - t0) >= 0.0) {
        localTimeStep = t1 - t;
      }

      try {
        x = stepAuto(equation, t, x, localTimeStep, this.minimumTimeStep, this.maximumTimeStep, this.absoluteTolerance, actualStepNextTrialStep);
      } catch (SolverStopException e) {
        stop(e);
        break;
      }

      t = t + actualStepNextTrialStep[0];
      localTimeStep = actualStepNextTrialStep[1];
    }

    setTimeSeries(ttData.getColumnVectors(1, count));
    setDifferentialSolution(xcData.getColumnVectors(1, count));
    setDifferenceSolution(null);
    setAlgebraicSolution(null);
  }

  /**
   * 指定された許容誤差を満たすように微分差分方程式の解を求めます。
   * 
   * @param equation 微分差分方程式
   * @param t0 初期時刻
   * @param t1 最終時刻
   * @param xc0 微分方程式の初期値
   * @param xd0 差分方程式の初期値
   */
  public final void solveAuto(final DoubleDifferentialDifferenceEquation equation, final double t0, final double t1, final DoubleMatrix xc0, final DoubleMatrix xd0) {
    resetStopper();
    double[] trialTimeStep = new double[] {Math.exp(Math.log(this.absoluteTolerance) / 5.0 * DoubleDifferentialEquationAutoSolver.SAFETY)};

    double t = t0;
    double lastSavingTime = t;
    if (Math.min(0.1, Math.abs(t1 - t0) / 5) < this.maximumTimeStep) {
      this.maximumTimeStep = Math.min(0.1, Math.abs(t1 - t0) / 5);
    }
    double localTimeStep = (t1 >= t0) ? Math.min(this.maximumTimeStep, Math.abs(trialTimeStep[0])) : -Math.min(this.maximumTimeStep, Math.abs(trialTimeStep[0]));
    int count = 0;
    int kmax = Math.min(getMaximumDataSize(), (int)(Math.abs(t1 - t0) / getMinimumSavingInterval()) * 2 + 10);

    double samplingTimeTolerance = 1.0E-6;
    if (isSaveAtSamplingPoint()) {
      double minimumSamplingTime = equation.getNextSamplingTime(t0, samplingTimeTolerance);
      kmax = Math.max(kmax, (int)(Math.abs(t1 - t0) / minimumSamplingTime) * 2 + 10);
    }

    final DoubleMatrix ttData = xc0.createZero(1, kmax);
    final DoubleMatrix xcData = xc0.createZero(xc0.getRowSize(), kmax);
    final DoubleMatrix xdData = xc0.createZero(xd0.getRowSize(), kmax);

    boolean atSamplingPoint = true;
    double nextSamplingTime = t0;
    DoubleMatrix xc = xc0;
    DoubleMatrix xd = xd0;
    DoubleMatrix xdNext = xd0;

    double[] actualStepNextTrialStep = new double[2];

    while (true) {
      boolean ending = (t - t1) * (t1 - t0) >= 0.0;
      boolean saving = Math.abs(t - lastSavingTime) - Math.abs(getMinimumSavingInterval()) >= -DoubleNumberUtil.EPS * 1.0e6;

      if (saving || ending || isStopping() || count == 0 || (atSamplingPoint && isSaveAtSamplingPoint()) || isAtSavingPoint()) {
        if (count < kmax) {
          count++;
          ttData.setElement(count, t);
          xcData.setColumnVector(count, xc);
          xdData.setColumnVector(count, xd);
          lastSavingTime = t;
          setAtSavingPoint(false);

          try {
            notifyObservers(t);
          } catch (InterruptedException e) {
            stop(new SolverInterruptedException(e));
            break;
          }
        } else {
          stop(new SolverTooManyDataException(Messages.getString("DifferentialEquationAutoSolver.4"))); //$NON-NLS-1$
          break;
        }
      }

      if (atSamplingPoint) {
        atSamplingPoint = false;
        equation.setAtSamplingPoint(true);
        xd = xdNext;
        
        try {
          xdNext = equation.differenceEquation(t, xc, xd);
        } catch (SolverStopException e) {
          stop(e);
          break;
        }
        
        equation.setAtSamplingPoint(false);

        nextSamplingTime = equation.getNextSamplingTime(t, samplingTimeTolerance);

        if (isSaveAtSamplingPoint()) {
          if (count < kmax) {
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
          } else {
            stop(new SolverTooManyDataException(Messages.getString("DifferentialEquationAutoSolver.5"))); //$NON-NLS-1$
            break;
          }
        }
      }

      if (ending) {
        trialTimeStep[0] = actualStepNextTrialStep[0];
        break;
      }

      if ((t + localTimeStep - nextSamplingTime) * (t + localTimeStep - t0) >= 0.0) {
        /* last step size to sampling point */
        localTimeStep = nextSamplingTime - t;
        atSamplingPoint = true;
      }

      /* Last step size */
      if (Math.abs(t + localTimeStep - t1) < samplingTimeTolerance) {
        localTimeStep = t1 - t;
      }

      try {
        xc = stepAuto(equation, t, xc, xd, localTimeStep, this.minimumTimeStep, nextSamplingTime - t, this.absoluteTolerance, actualStepNextTrialStep);
      } catch (SolverStopException e) {
        stop(e);
        break;
      }

      if (atSamplingPoint && actualStepNextTrialStep[0] < localTimeStep) {
        atSamplingPoint = false;
      }

      t = t + actualStepNextTrialStep[0];
      localTimeStep = actualStepNextTrialStep[1];
    }

    setTimeSeries(ttData.getColumnVectors(1, count));
    setDifferentialSolution(xcData.getColumnVectors(1, count));
    setDifferenceSolution(xdData.getColumnVectors(1, count));
    setAlgebraicSolution(null);
  }
}