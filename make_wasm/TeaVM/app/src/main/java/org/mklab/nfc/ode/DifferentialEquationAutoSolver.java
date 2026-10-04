/*
 * $Id: DifferentialEquationAutoSolver.java,v 1.41 2008/07/16 08:00:37 koga Exp $
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
 * 刻み幅を自動調節して常微分方程式の解を求めるソルバーを表す抽象クラスです。
 * 
 * <p>具体的な解法アルゴリズムはこのクラスを継承した子クラスで実装します。
 * 
 * @author matsuki
 * @version $Revision: 1.41 $, 2004/05/08
 * 
 * @param <RM> type of real scalar
 * @param <RS>  type of real scalar
 * @param <CS> type of complex scalar
 * @param <CM> type of complex matrix
 */
public abstract class DifferentialEquationAutoSolver<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> extends DifferentialEquationSolver<RS,RM,CS,CM> implements EquationAutoSolver<RS,RM,CS,CM> {

  /** 安全係数。 */
  private final RS SAFETY = this.sunit.create(9).divide(10);
  /** 絶対許容誤差 。*/
  //private RS absoluteTolerance = this.sunit.create(1.0E-6);
  private RS absoluteTolerance = this.sunit.create(10).power(6).inverse();
  /** 相対許容誤差 。*/
  //private RS relativeTolerance = this.sunit.create(1.0E-6);
  private RS relativeTolerance = this.sunit.create(10).power(6).inverse();
  /** 初期ステップ幅。*/
  //private RS initialStepSize = this.sunit.create(1.0E-6);
  private RS initialStepSize = this.sunit.create(10).power(6).inverse();
  /** 刻み幅の変動可能最小値。 */
  //private RS minimumTimeStep = this.sunit.create(DoubleNumberUtil.EPS);
  private RS minimumTimeStep = this.sunit.getMachineEpsilon();
  /** 刻み幅の変動可能最大値。 */
  //private RS maximumTimeStep = this.sunit.create(Double.MAX_VALUE);
  private RS maximumTimeStep = this.sunit.getInfinity();
  /** 不連続点の時刻に関する許容誤差。 */
  //private RS toleranceOfDiscontinuity = this.sunit.create(1.0E-10);
  private RS toleranceOfDiscontinuity = this.sunit.create(10).power(10).inverse();
  
  /**
   * Creates {@link DifferentialEquationAutoSolver}.
   * @param sunit unit of scalar
   */
  public DifferentialEquationAutoSolver(RS sunit) {
    super(sunit);
  }

  /**
   * {@inheritDoc}
   */
  public final void setAbsoluteTolerance(final RS absoluteTolerance) {
    this.absoluteTolerance = absoluteTolerance;
  }

  /**
   * {@inheritDoc}
   */
  public final RS getAbsoluteTolerance() {
    return this.absoluteTolerance;
  }
  
  /**
   * {@inheritDoc}
   */
  public final void setRelativeTolerance(final RS relativeTolerance) {
    this.relativeTolerance = relativeTolerance;
  }

  /**
   * {@inheritDoc}
   */
  public final RS getRelativeTolerance() {
    return this.relativeTolerance;
  }
  
  /**
   * {@inheritDoc}
   */
  public final void setInitialStepSize(final RS initialStepSize) {
    this.initialStepSize = initialStepSize;
  }

  /**
   * {@inheritDoc}
   */
  public final RS getInitialStepSize() {
    return this.initialStepSize;
  }

  /**
   * {@inheritDoc}
   */
  public final void setToleranceOfDiscontinuity(final RS toleranceOfDiscontinuity) {
    this.toleranceOfDiscontinuity = toleranceOfDiscontinuity;
  }

  /**
   * {@inheritDoc}
   */
  public final RS getToleranceOfDiscontinuity() {
    return this.toleranceOfDiscontinuity;
  }

  /**
   * {@inheritDoc}
   */
  public final void setMinimumTimeStep(final RS minimumTimeStep) {
    this.minimumTimeStep = minimumTimeStep;
  }

  /**
   * {@inheritDoc}
   */
  public final RS getMinimumTimeStep() {
    return this.minimumTimeStep;
  }

  /**
   * {@inheritDoc}
   */
  public final void setMaximumTimeStep(final RS maximumTimeStep) {
    this.maximumTimeStep = maximumTimeStep;
  }

  /**
   * {@inheritDoc}
   */
  public final RS getMaximumTimeStep() {
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
  public void solveAuto(final ExplicitDifferentialSystem<RS,RM,CS,CM> system, final RS t0, final RS t1, final RM x0) {
    resetStopper();

    RS[] trialTimeStep = this.sunit.createArray(1);
    trialTimeStep[0] = this.absoluteTolerance.log().divide(5).multiply(this.SAFETY).exp();

    RS t = t0;
    RS lastSavingTime = t;
    if (t1.subtract(t0).abs().divide(5).min(this.sunit.create(1).divide(10)).isLessThan(this.maximumTimeStep)) {
      this.maximumTimeStep =  t1.subtract(t0).abs().divide(5).min(this.sunit.create(1).divide(10));
    }
    RS localTimeStep = t1.isGreaterThanOrEquals(t0) ? this.maximumTimeStep.min(trialTimeStep[0].abs()) : this.maximumTimeStep.min(trialTimeStep[0].abs()).unaryMinus();
    int count = 0;
    int kmax = Math.min(getMaximumDataSize(), (int)(t1.subtract(t0).abs().divide(getMinimumSavingInterval()).add(10)).toDouble());

    RM io = null;
    try {
      io = system.inputOutputEquation(t, x0);
    } catch (SolverStopException e) {
      stop(e);
      return;
    }

    final RM ttData = x0.createZero(1, kmax);
    final RM ioData = x0.createZero(io.getRowSize(), kmax);
    final RM xcData = x0.createZero(x0.getRowSize(), kmax);

    RS[] actualStepNextTrialStep = this.sunit.createArray(2);

    RM x = x0;

    while (true) {
      boolean ending = t.subtract(t1).multiply(t1.subtract(t0)).isGreaterThanOrEquals(0);
      final RS tolerance = this.sunit.getMachineEpsilon().multiply(1000000);
      boolean saving = t.subtract(lastSavingTime).abs().subtract(getMinimumSavingInterval().abs()).isGreaterThanOrEquals(tolerance.unaryMinus());

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
      if (t.add(localTimeStep).subtract(t1).multiply(t.add(localTimeStep).subtract(t0)).isGreaterThanOrEquals(0)) {
        localTimeStep = t1.subtract(t);
      }

      try {
        x = stepAuto(system, t, x, localTimeStep, this.minimumTimeStep, this.maximumTimeStep, this.absoluteTolerance, actualStepNextTrialStep);
      } catch (SolverStopException e) {
        stop(e);
        break;
      }

      t = t.add(actualStepNextTrialStep[0]);
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
  public final void solveAuto(final DifferentialDifferenceSystem<RS,RM,CS,CM> system, final RS t0, final RS t1, final RM xc0, final RM xd0) {
    resetStopper();

    final RS[] trialTimeStep = this.sunit.createArray(1);
    trialTimeStep[0] = this.absoluteTolerance.log().divide(5).multiply(this.SAFETY).exp();

    RS t = t0;
    RS lastSavingTime = t;
    if (t1.subtract(t0).abs().divide(5).min(this.sunit.create(1).divide(10)).isLessThan(this.maximumTimeStep)) {
      this.maximumTimeStep = t1.subtract(t0).abs().divide(5).min(this.sunit.create(1).divide(10));
    }
    RS localTimeStep = t1.isGreaterThanOrEquals(t0) ? this.maximumTimeStep.min(trialTimeStep[0].abs()) : this.maximumTimeStep.min(trialTimeStep[0].abs()).unaryMinus();
    int count = 0;
    int kmax = Math.min(getMaximumDataSize(), (int)(t1.subtract(t0).abs().divide(getMinimumSavingInterval()).multiply(4).add(10)).toDouble());

    //final RS samplingTimeTolerance = this.sunit.create(1.0E-6);
    final RS samplingTimeTolerance = this.sunit.create(10).power(6).inverse();
    if (isSaveAtSamplingPoint()) {
      final RS minimumSamplingTime = system.getNextSamplingTime(t0, samplingTimeTolerance);
      kmax = Math.max(kmax, (int)(t1.subtract(t0).abs().divide(minimumSamplingTime).multiply(4).add(10)).toDouble());
    }

    RM io = null;
    try {
      system.setAtSamplingPoint(true);
      io = system.inputOutputEquation(t, xc0, xd0);
      system.setAtSamplingPoint(false);
    } catch (SolverStopException e) {
      stop(e);
      return;
    }

    final RM ttData =xc0.createZero(1, kmax);
    final RM ioData = xc0.createZero(io.getRowSize(), kmax);
    final RM xcData = xc0.createZero(xc0.getRowSize(), kmax);
    final RM xdData = xc0.createZero(xd0.getRowSize(), kmax);

    boolean atSamplingPoint = true;
    RS nextSamplingTime = t0;
    RM xc = xc0;
    RM xd = xd0;
    RM xdNext = xd0;

    RS[] actualStepNextTrialStep = this.sunit.createArray(2);

    while (true) {
      boolean ending = t.subtract(t1).multiply(t1.subtract(t0)).isGreaterThanOrEquals(0);
      final RS tolerance = this.sunit.getMachineEpsilon().multiply(1000000);
      boolean saving = t.subtract(lastSavingTime).abs().subtract(getMinimumSavingInterval().abs()).isGreaterThanOrEquals(tolerance.unaryMinus());

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
          final boolean isNotInitialTime = (t.equals(t0) == false);
          //final boolean isNotInitialTime = Double.doubleToLongBits(t) != Double.doubleToLongBits(t0);
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

      if (t.add(localTimeStep).subtract(nextSamplingTime).multiply(t.add(localTimeStep).subtract(t0)).isGreaterThanOrEquals(0)) {
        localTimeStep = nextSamplingTime.subtract(t);
        atSamplingPoint = true;
      }

      /* Last step size */
      if (t.add(localTimeStep).subtract(t1).abs().isLessThan(samplingTimeTolerance)) {
        localTimeStep = t1.subtract(t);
      }

      try {
        xc = stepAuto(system, t, xc, xd, localTimeStep, this.minimumTimeStep, nextSamplingTime.subtract(t), this.absoluteTolerance, actualStepNextTrialStep);
      } catch (SolverStopException e) {
        stop(e);
        break;
      }

      if (atSamplingPoint && actualStepNextTrialStep[0].isLessThan(localTimeStep)) {
        atSamplingPoint = false;
      }

      t = t.add(actualStepNextTrialStep[0]);
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
  public final void solveAuto(final ExplicitDifferentialEquation<RS,RM,CS,CM> equation, final RS t0, final RS t1, final RM x0) {
    resetStopper();

    RS[] trialTimeStep = this.sunit.createArray(1);
    trialTimeStep[0] = this.absoluteTolerance.log().divide(5).multiply(this.SAFETY).exp();

    RS t = t0;
    RS lastSavingTime = t;
    if (t1.subtract(t0).abs().divide(5).min(this.sunit.create(1).divide(10)).isLessThan(this.maximumTimeStep)) {
      this.maximumTimeStep = t1.subtract(t0).abs().divide(5).min(this.sunit.create(1).divide(10));
    }
    RS localTimeStep = (t1.isGreaterThanOrEquals(t0)) ? this.maximumTimeStep.min(trialTimeStep[0].abs()) : this.maximumTimeStep.min(trialTimeStep[0].abs()).unaryMinus();
    int count = 0;
    int kmax = Math.min(getMaximumDataSize(), (int)(t1.subtract(t0).abs().divide(getMinimumSavingInterval()).add(10)).toDouble());

    final RM ttData = x0.createZero(1, kmax);
    final RM xcData = x0.createZero(x0.getRowSize(), kmax);

    RS[] actualStepNextTrialStep = this.sunit.createArray(2);
    RM x = x0;

    while (true) {
      boolean ending = t.subtract(t1).multiply(t1.subtract(t0)).isGreaterThanOrEquals(0);
      final RS tolerance = this.sunit.getMachineEpsilon().multiply(1000000);
      boolean saving = t.subtract(lastSavingTime).abs().subtract(getMinimumSavingInterval().abs()).isGreaterThanOrEquals(tolerance.unaryMinus());

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
      if (t.add(localTimeStep).subtract(t1).multiply(t.add(localTimeStep).subtract(t0)).isGreaterThanOrEquals(0)) {
        localTimeStep = t1.subtract(t);
      }

      try {
        x = stepAuto(equation, t, x, localTimeStep, this.minimumTimeStep, this.maximumTimeStep, this.absoluteTolerance, actualStepNextTrialStep);
      } catch (SolverStopException e) {
        stop(e);
        break;
      }

      t = t.add(actualStepNextTrialStep[0]);
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
  public final void solveAuto(final DifferentialDifferenceEquation<RS,RM,CS,CM> equation, final RS t0, final RS t1, final RM xc0, final RM xd0) {
    resetStopper();
    RS[] trialTimeStep = this.sunit.createArray(1);
    trialTimeStep[0] = this.absoluteTolerance.log().divide(5).multiply(this.SAFETY).exp();

    RS t = t0;
    RS lastSavingTime = t;
    if (t1.subtract(t0).abs().divide(5).min(this.sunit.create(1).divide(10)).isLessThan(this.maximumTimeStep)) {
      this.maximumTimeStep = t1.subtract(t0).abs().divide(5).min(this.sunit.create(1).divide(10));
    }
    RS localTimeStep = t1.isGreaterThanOrEquals(t0) ? this.maximumTimeStep.min(trialTimeStep[0].abs()) : this.maximumTimeStep.min(trialTimeStep[0].abs()).unaryMinus();
    int count = 0;
    int kmax = Math.min(getMaximumDataSize(), (int)(t1.subtract(t0).abs().divide(getMinimumSavingInterval()).multiply(2).add(10)).toDouble());

    //RS samplingTimeTolerance =  this.sunit.create(1.0E-6);
    RS samplingTimeTolerance =  this.sunit.create(10).power(6).inverse();
    if (isSaveAtSamplingPoint()) {
      RS minimumSamplingTime = equation.getNextSamplingTime(t0, samplingTimeTolerance);
      kmax = Math.max(kmax, (int)(t1.subtract(t0).abs().divide(minimumSamplingTime).multiply(2).add(10)).toDouble());
    }

    final RM ttData = xc0.createZero(1, kmax);
    final RM xcData = xc0.createZero(xc0.getRowSize(), kmax);
    final RM xdData = xc0.createZero(xd0.getRowSize(), kmax);

    boolean atSamplingPoint = true;
    RS nextSamplingTime = t0;
    RM xc = xc0;
    RM xd = xd0;
    RM xdNext = xd0;

    RS[] actualStepNextTrialStep = this.sunit.createArray(2);

    while (true) {
      boolean ending = t.subtract(t1).multiply(t1.subtract(t0)).isGreaterThanOrEquals(0);
      final RS tolerance = this.sunit.getMachineEpsilon().multiply(1000000);
      boolean saving = t.subtract(lastSavingTime).abs().subtract(getMinimumSavingInterval().abs()).isGreaterThanOrEquals(tolerance.unaryMinus());

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

      if (t.add(localTimeStep).subtract(nextSamplingTime).multiply(t.add(localTimeStep).subtract(t0)).isGreaterThanOrEquals(0)) {
        /* last step size to sampling point */
        localTimeStep = nextSamplingTime.subtract(t);
        atSamplingPoint = true;
      }

      /* Last step size */
      if (t.add(localTimeStep).subtract(t1).abs().isLessThan(samplingTimeTolerance)) {
        localTimeStep = t1.subtract(t);
      }

      try {
        xc = stepAuto(equation, t, xc, xd, localTimeStep, this.minimumTimeStep, nextSamplingTime.subtract(t), this.absoluteTolerance, actualStepNextTrialStep);
      } catch (SolverStopException e) {
        stop(e);
        break;
      }

      if (atSamplingPoint && actualStepNextTrialStep[0].isLessThan(localTimeStep)) {
        atSamplingPoint = false;
      }

      t = t.add(actualStepNextTrialStep[0]);
      localTimeStep = actualStepNextTrialStep[1];
    }

    setTimeSeries(ttData.getColumnVectors(1, count));
    setDifferentialSolution(xcData.getColumnVectors(1, count));
    setDifferenceSolution(xdData.getColumnVectors(1, count));
    setAlgebraicSolution(null);
  }
}