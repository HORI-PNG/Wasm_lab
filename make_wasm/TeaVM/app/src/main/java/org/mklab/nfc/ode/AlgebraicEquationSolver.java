/*
 * $Id: AlgebraicEquationSolver.java,v 1.32 2008/07/16 08:00:37 koga Exp $
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
 * 代数方程式の解を求めるソルバーを表すクラスです。
 * 
 * @author koga
 * @version $Revision: 1.32 $, 2004/05/08
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 */
public class AlgebraicEquationSolver<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> extends EquationSolver<RS,RM,CS,CM> {
  
  /**
   * Creates {@link AlgebraicEquationSolver}.
   * @param sunit unit of scalar
   */
  public AlgebraicEquationSolver(RS sunit) {
    super(sunit);
  }

  /**
   * <code>t0</code>秒から<code>t1</code>秒までの連続時間代数システムの入出力を返します。
   * 
   * @param system 連続時間代数システム
   * @param t0 シミュレーション開始時刻
   * @param t1 シミュレーション終了時刻
   * @exception SolverStopException ソルバーが停止された場合
   */
  public final void solve(final ContinuousAlgebraicSystem<RS,RM,CS,CM> system, final RS t0, final RS t1) throws SolverStopException {
    resetStopper();
    boolean ending = false;

    RS t = t0;
    RS tsav = t;
    final RS localTimeStep = (t1.isGreaterThanOrEquals(t0)) ? getTimeStep().abs() : getTimeStep().abs().unaryMinus();
    RS nextTimeStep = localTimeStep;
    int count = 0;
    
    final int kmax;
    if (system instanceof PiecewiseContinuousAlgebraicSystem) {
      kmax = Math.min(getMaximumDataSize(), (int)(t1.subtract(t0).abs().divide(getMinimumSavingInterval()).multiply(10).add(10).toDouble()));
    } else {
      kmax = Math.min(getMaximumDataSize(), (int)(t1.subtract(t0).abs().divide(getMinimumSavingInterval()).add(10)).toDouble());
    }

    // 初期時刻における入出力
    RM io = null;
    try {
      io = system.inputOutputEquation(t);
    } catch (SolverStopException e) {
      stop(e);
      return;
    }

    final RM ttData = io.createZero(1, kmax);
    final RM ioData = io.createZero(io.getRowSize(), kmax);

    while (count < kmax) {
      final RS tolerance =  this.sunit.getMachineEpsilon().multiply(1000000);
      boolean timeToSave = t.subtract(tsav).abs().subtract(getMinimumSavingInterval().abs()).isGreaterThanOrEquals(tolerance.unaryMinus());
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

      if (system instanceof PiecewiseContinuousAlgebraicSystem) {
        setTrial(true);
        final RM newIO = system.inputOutputEquation(t.add(nextTimeStep));
        final RS discontinuousPoint = ((PiecewiseContinuousAlgebraicSystem<RS,RM,CS,CM>)system).getDiscontinuousPoint(t, io, t.add(nextTimeStep), newIO);
        setTrial(false);

        //final RS toleranceOfDiscontinuity = this.sunit.create(1.0E-10);
        final RS toleranceOfDiscontinuity = this.sunit.create(10).power(10).inverse();

        if (discontinuousPoint.isNaN() == false) {
          if (isSaveAtDiscontinuousPoint()) {
            setAtSavingPoint(true);
          }

          if (discontinuousPoint.subtract(t).isGreaterThan(toleranceOfDiscontinuity)) {
            final RS safetyFactor = this.sunit.create(8).divide(10);
            nextTimeStep = discontinuousPoint.subtract(t).multiply(safetyFactor);
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
      if ((t.add(nextTimeStep).subtract(t1)).multiply((t.add(nextTimeStep).subtract(t0))).isGreaterThanOrEquals(0)) {
        ending = true;
        t = t1;
      } else {
        t = t.add(nextTimeStep);
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
  public final void solve(final DiscreteAlgebraicSystem<RS,RM,CS,CM> system, final RS t0, final RS t1) {
    resetStopper();
    boolean ending = false;

    RS t = t0;
    RS tsav = t;
    int count = 0;
    final int kmax = Math.min(getMaximumDataSize(), (int)(t1.subtract(t0).abs().divide(getMinimumSavingInterval()).add(10)).toDouble());

    // 初期時刻における入出力
    RM io = null;
    try {
      system.setAtSamplingPoint(true);
      io = system.inputOutputEquation(t);
      system.setAtSamplingPoint(false);
    } catch (SolverStopException e) {
      stop(e);
      return;
    }

    final RM ttData = io.createZero(1, kmax);
    final RM ioData =io.createZero(io.getRowSize(), kmax);

    //RS samplingTimeTolerance = this.sunit.create(1.0E-6);
    RS samplingTimeTolerance = this.sunit.create(10).power(6).inverse();
    RS nextSamplingTime = t0;

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

      final RS tolerance = this.sunit.getMachineEpsilon().multiply(1000000);
      boolean timeToSave = t.subtract(tsav).abs().subtract(getMinimumSavingInterval().abs()).isGreaterThanOrEquals(tolerance);
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

      if (nextSamplingTime.subtract(t1).multiply(nextSamplingTime.subtract(t0)).isGreaterThanOrEquals(0)) {
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
  public final void solve(final ContinuousDiscreteAlgebraicSystem<RS,RM,CS,CM> system, final RS t0, final RS t1) throws SolverStopException {
    resetStopper();
    boolean ending = false;

    RS t = t0;
    RS tsav = t;
    final RS localTimeStep = (t1.isGreaterThanOrEquals(t0) ) ? getTimeStep().abs() : getTimeStep().abs().unaryMinus();
    RS nextTimeStep = localTimeStep;

    int count = 0;
    final int kmax;
    if (system instanceof PiecewiseContinuousDiscreteAlgebraicSystem) {
      kmax = Math.min(getMaximumDataSize(), (int)(t1.subtract(t0).abs().divide(getMinimumSavingInterval()).multiply(10).add(10)).toDouble());
    } else {
      kmax = Math.min(getMaximumDataSize(), (int)(t1.subtract(t0).abs().divide(getMinimumSavingInterval()).add(10)).toDouble());
    }

    // 初期時刻における入出力
    RM io = null;
    try {
      system.setAtSamplingPoint(true);
      io = system.inputOutputEquation(t);
      system.setAtSamplingPoint(false);
    } catch (SolverStopException e) {
      stop(e);
      return;
    }

    final RM ttData = io.createZero(1, kmax);
    final RM ioData = io.createZero(io.getRowSize(), kmax);

    boolean samplingPoint = true;
    //final RS samplingTimeTolerance = this.sunit.create(1.0E-6);
    final RS samplingTimeTolerance = this.sunit.create(10).power(6).inverse();
    RS nextSamplingTime = t0;

    while (count < kmax) {
      final RS tolerance = this.sunit.getMachineEpsilon().multiply(1000000);
      boolean timeToSave =t.subtract(tsav).abs().subtract(getMinimumSavingInterval().abs()).isGreaterThanOrEquals(tolerance);
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

      if (system instanceof PiecewiseContinuousDiscreteAlgebraicSystem) {
        setTrial(true);
        final RM newIO = system.inputOutputEquation(t.add(nextTimeStep));
        final RS discontinuousPoint = ((PiecewiseContinuousDiscreteAlgebraicSystem<RS,RM,CS,CM>)system).getDiscontinuousPoint(t, io, t.add(nextTimeStep), newIO);
        setTrial(false);

        //final RS toleranceOfDiscontinuity = this.sunit.create(1.0E-10);
        final RS toleranceOfDiscontinuity = this.sunit.create(10).power(10).inverse();

        if (discontinuousPoint.isNaN() == false) {
          if (isSaveAtDiscontinuousPoint()) {
            setAtSavingPoint(true);
          }

          if (discontinuousPoint.subtract(t).isGreaterThan(toleranceOfDiscontinuity)) {
            final RS safetyFactor = this.sunit.create(8).divide(10);
            nextTimeStep = discontinuousPoint.subtract(t).multiply(safetyFactor);
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
          final boolean isNotInitialTime = (t.equals(t0) == false);
          //final boolean isNotInitialTime = Double.doubleToLongBits(t) != Double.doubleToLongBits(t0);
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
      if ((t.add(nextTimeStep).subtract(t1)).multiply((t.add(nextTimeStep).subtract(t0))).isGreaterThanOrEquals(0)) {
        ending = true;
        t = t1;
      } else if ((t.add(nextTimeStep).subtract(nextSamplingTime)).multiply((t.add(nextTimeStep).subtract(t0))).isGreaterThanOrEquals(0)) {
        // last step size to sampling point
        t = nextSamplingTime;
        samplingPoint = true;
      } else {
        t = t.add(nextTimeStep);
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
  public final RM getSolution() {
    return getAlgebraicSolution();
  }
}