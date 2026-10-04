/*
 * $Id: DifferentialEquationSolver.java,v 1.34 2008/07/16 08:00:37 koga Exp $
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
 * 常微分方程式の解を求めるソルバーを表す抽象クラスです。
 * 
 * <p>具体的な解法アルゴリズムはこのクラスを継承した子クラスで実装します。
 * 
 * @author matsuki
 * @version $Revision: 1.34 $, 2004/05/08
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 */
public abstract class DifferentialEquationSolver<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> extends EquationSolver<RS,RM,CS,CM> {
  
  /**
   * Creates {@link DifferentialEquationSolver}.
   * @param sunit unit of scalar
   */
  public DifferentialEquationSolver(RS sunit) {
    super(sunit);
  }

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
  public abstract RM step(ExplicitDifferentialSystem<RS,RM,CS,CM> system, RS t, RM x, RS h) throws SolverStopException;

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
  public abstract RM step(DifferentialDifferenceSystem<RS,RM,CS,CM> system, RS t, RM xc, RM xd, RS h) throws SolverStopException;

  /**
   * <code>t0</code>秒から<code>t1</code>秒までのシミュレーションを行い, 結果を {@link org.mklab.nfc.matrix.Matrix}の配列として返します。
   * 
   * @param system シミュレーション対象
   * @param t0 シミュレーション開始時刻
   * @param t1 シミュレーション終了時刻
   * @param x0 初期状態
   */
  public final void solve(final ExplicitDifferentialSystem<RS,RM,CS,CM> system, final RS t0, final RS t1, final RM x0) {
    resetStopper();
    boolean ending = false;

    RS t = t0;
    RS lastSavingTime = t;
    RS localTimeStep = (t1.isGreaterThanOrEquals(t0)) ? getTimeStep().abs() : getTimeStep().abs().unaryMinus();
    int ii = 0;
    int count = 0;
    final int kmax = (int)(t1.subtract(t0).abs().divide(getMinimumSavingInterval()).add(10).toDouble());

    // 初期時刻における入出力
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

    RM x = x0;

    while (count < kmax) {
      //boolean timeToSave = t.subtract(lastSavingTime).abs().subtract(getMinimumSavingInterval().abs()).isGreaterThanOrEquals(-DoubleNumberUtil.EPS * 1.0e6);
      boolean timeToSave = t.subtract(lastSavingTime).abs().subtract(getMinimumSavingInterval().abs()).isGreaterThanOrEquals(this.sunit.getMachineEpsilon().multiply(1000000).unaryMinus());
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
      if (t.add(localTimeStep).subtract(t1).multiply(t.add(localTimeStep).subtract(t0)).isGreaterThanOrEquals(0)) {
        ending = true;
        try {
          x = step(system, t, x, t1.subtract(t));
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
        t = t0 .add(localTimeStep.multiply(ii));
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
  public final void solve(final DifferentialDifferenceSystem<RS,RM,CS,CM> system, final RS t0, final RS t1, final RM xc0, final RM xd0) {
    resetStopper();
    boolean ending = false;

    RS t = t0;
    RS lastSavingTime = t;
    RS localTimeStep = t1.isGreaterThanOrEquals(t0) ? getTimeStep().abs() : getTimeStep().abs().unaryMinus();

    int ii = 0;
    int count = 0;
    final int kmax = (int)(t1.subtract(t0).abs() .divide(getMinimumSavingInterval()).add(10)).toDouble();

    // 初期時刻における入出力
    RM io = null;
    try {
      system.setAtSamplingPoint(true);
      io = system.inputOutputEquation(t, xc0, xd0);
      system.setAtSamplingPoint(false);
    } catch (SolverStopException e) {
      stop(e);
      return;
    }

    final RM ttData = xc0.createZero(1, kmax);
    final RM ioData = xc0.createZero(io.getRowSize(), kmax);
    final RM xcData = xc0.createZero(xc0.getRowSize(), kmax);
    final RM xdData = xc0.createZero(xd0.getRowSize(), kmax);

    boolean samplingPoint = true;
    //final RS samplingTimeTolerance = this.sunit.create(1.0E-6);
    final RS samplingTimeTolerance = this.sunit.create(10).power(6).inverse();
    RS nextSamplingTime = t0; 
    RS prevSamplingTime = t0;

    RM xc = xc0;
    RM xd = xd0;

    while (count < kmax) {
      //boolean timeToSave = t.subtract(lastSavingTime).abs().subtract(getMinimumSavingInterval().abs()).isGreaterThanOrEquals(-DoubleNumberUtil.EPS * 1.0e6);
      boolean timeToSave = t.subtract(lastSavingTime).abs().subtract(getMinimumSavingInterval().abs()).isGreaterThanOrEquals(this.sunit.getMachineEpsilon().multiply(1000000).unaryMinus());
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
          //final boolean isNotInitialTime = Double.doubleToLongBits(t) != Double.doubleToLongBits(t0);
          final boolean isNotInitialTime = (t.equals(t0) == false); 
              
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
      if (t.add(localTimeStep).subtract(t1).multiply(t.add(localTimeStep).subtract(t0)).isGreaterThanOrEquals(0)) {
        ending = true;
        try {
          xc = step(system, t, xc, xd, t1.subtract(t));
        } catch (SolverStopException e) {
          stop(e);
          break;
        }

        t = t1;
      } else if (t.add(localTimeStep).subtract(nextSamplingTime).multiply(t.add(localTimeStep).subtract(t0)).isGreaterThanOrEquals(0)) {
        /* Final step size to sampling point */
        try {
          xc = step(system, t, xc, xd, nextSamplingTime.subtract(t));
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
        t = prevSamplingTime.add(localTimeStep.multiply(ii));
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
  public abstract RM step(ExplicitDifferentialEquation<RS,RM,CS,CM> equation, RS t, RM x, RS h) throws SolverStopException;

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
  public abstract RM step(DifferentialDifferenceEquation<RS,RM,CS,CM> equation, RS t, RM xc, RM xd, RS h) throws SolverStopException;

  /**
   * <code>t0</code>秒から<code>t1</code>秒までの解を求め, 結果を {@link org.mklab.nfc.matrix.Matrix} の配列として返します。
   * 
   * @param equation 常微分方程式
   * @param t0 初期時刻
   * @param t1 最終時刻
   * @param x0 初期値
   */
  public final void solve(final ExplicitDifferentialEquation<RS,RM,CS,CM> equation, final RS t0, final RS t1, final RM x0) {
    resetStopper();
    boolean ending = false;

    RS t = t0;
    RS lastSavingTime = t;
    RS localTimeStep = t1.isGreaterThanOrEquals(t0) ? getTimeStep().abs() : getTimeStep().abs().unaryMinus();
    int ii = 0;
    int count = 0;
    int kmax = (int)(t1.subtract(t0).abs().divide(getMinimumSavingInterval()).add(10)).toDouble();

    final RM ttData = x0.createZero(1, kmax);
    final RM xcData = x0.createZero(x0.getRowSize(), kmax);

    RM x = x0;

    while (count < kmax) {
      //boolean timeToSave = t.subtract(lastSavingTime).abs().subtract(getMinimumSavingInterval().abs()).isGreaterThanOrEquals(-DoubleNumberUtil.EPS * 1.0e6);
      boolean timeToSave = t.subtract(lastSavingTime).abs().subtract(getMinimumSavingInterval().abs()).isGreaterThanOrEquals(this.sunit.getMachineEpsilon().multiply(1000000).unaryMinus());
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
      if (t.add(localTimeStep).subtract(t1).multiply(t.add(localTimeStep).subtract(t0)).isGreaterThanOrEquals(0)) {
        ending = true;
        try {
          x = step(equation, t, x, t1.subtract(t));
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
        t = t0.add(localTimeStep.multiply(ii));
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
  public final void solve(final DifferentialDifferenceEquation<RS,RM,CS,CM> equation, final RS t0, final RS t1, final RM xc0, final RM xd0) {
    resetStopper();
    boolean ending = false;

    RS t = t0;
    RS lastSavingTime = t;
    RS localTimeStep = t1.isGreaterThanOrEquals(t0) ? getTimeStep().abs() : getTimeStep().abs().unaryMinus();

    int ii = 0;
    int count = 0;
    int kmax = (int)(t1.subtract(t0).abs().divide(getMinimumSavingInterval()).add(10)).toDouble();

    final RM ttData = xc0.createZero(1, kmax);
    final RM xcData = xc0.createZero(xc0.getRowSize(), kmax);
    final RM xdData = xc0.createZero(xd0.getRowSize(), kmax);

    boolean samplingPoint = true;
    //RS samplingTimeTolerance = this.sunit.create(1.0E-6);
    RS samplingTimeTolerance = this.sunit.create(10).power(6).inverse();
    RS nextSamplingTime = t0; 
    RS prevSamplingTime = t0;

    RM xc = xc0;
    RM xd = xd0;
    RM xdNext = xd0;

    while (count < kmax) {
      //boolean timeToSave = t.subtract(lastSavingTime).abs().subtract(getMinimumSavingInterval().abs()).isGreaterThanOrEquals(-DoubleNumberUtil.EPS * 1.0e6);
      boolean timeToSave = t.subtract(lastSavingTime).abs().subtract(getMinimumSavingInterval().abs()).isGreaterThanOrEquals(this.sunit.getMachineEpsilon().multiply(1000000).unaryMinus());
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

      if (t.add(localTimeStep).subtract(t1).multiply(t.add(localTimeStep).subtract(t0)).isGreaterThanOrEquals(0)) {
        /* Final step size to ending point */
        ending = true;
        try {
          xc = step(equation, t, xc, xd, t1.subtract(t));
        } catch (SolverStopException e) {
          stop(e);
          break;
        }

        t = t1;
      } else if (t.add(localTimeStep).subtract(nextSamplingTime).multiply(t.add(localTimeStep).subtract(t0)).isGreaterThanOrEquals(0)) {
        /* Final step size to sampling point */
        try {
          xc = step(equation, t, xc, xd, nextSamplingTime.subtract(t));
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
        t = prevSamplingTime.add(localTimeStep.multiply(ii));
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
  public final RM getSolution() {
    return getDifferentialSolution();
  }
}