/*
 * $Id: RungeKutta4.java,v 1.51 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.DoubleMatrix;


/**
 * 4次のルンゲ・クッタ法を用いて刻み幅を自動調節しながら常微分方程式の解を求めるソルバーを表すクラスです。
 * 
 * @author koga
 * @version $Revision: 1.51 $, 2004/05/08
 */
public class DoubleRungeKutta4Auto extends DoubleDifferentialEquationAutoSolver {
  /** Runge Kutta4 solver */
  private DoubleRungeKutta4 kutta4 = new DoubleRungeKutta4();

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleMatrix step(final DoubleExplicitDifferentialEquation equation, final double t0, final DoubleMatrix x0, final double h) throws SolverStopException {
    return this.kutta4.step(equation, t0, x0, h);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleMatrix step(final DoubleDifferentialDifferenceEquation equation, final double t0, final DoubleMatrix xc0, final DoubleMatrix xd0, final double h) throws SolverStopException {
    return this.kutta4.step(equation, t0, xc0, xd0, h);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleMatrix step(final DoubleExplicitDifferentialSystem system, final double t0, final DoubleMatrix x0, final double h) throws SolverStopException {
    return this.kutta4.step(system, t0, x0, h);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleMatrix step(final DoubleDifferentialDifferenceSystem system, final double t0, final DoubleMatrix xc0, final DoubleMatrix xd0, final double h) throws SolverStopException {
    return this.kutta4.step(system, t0, xc0, xd0, h);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix stepAuto(final DoubleExplicitDifferentialSystem system, final double t0, final DoubleMatrix x0, final double trialTimeStep, final double minTimeStep, final double maxTimeStep, final double tolerance,
      final double[] actualStepNextTrialStep) throws SolverStopException {
    final double errcon = 6.0E-4;
    final double pgrow = -0.20;
    final double pshrnk = -0.25;
    final double safety = 0.9;
    final double fcor = 1.0 / 15.0;
    final double tiny = 1.0E-30;

    boolean atDiscontinuousPoint = false;

    /* Set the initial trial value to step size */
    double h = trialTimeStep;

    final DoubleMatrix io0 = system.inputOutputEquation(t0, x0);
    final DoubleMatrix dx0 = system.differentialEquation(t0, x0, io0);
    final DoubleMatrix xscal =x0.absElementWise().add(dx0.multiply(h).absElementWise()).addElementWise(tiny);

    DoubleMatrix newX = null;
    
    double nextTimeStep = h;
    boolean calculating = true;
    setTrial(true);
    
    while (calculating) {
      final DoubleMatrix xc1 = step(system, t0, x0, h / 2);
      final DoubleMatrix xc2 = step(system, t0 + h / 2, xc1, h / 2);
      final DoubleMatrix xc3 = step(system, t0, x0, h);
      final DoubleMatrix estimationOfError = xc2.subtract(xc3);
      double estimationOfMaxError = (estimationOfError.divideElementWise(xscal).absElementWise()).max().doubleValue();

      /* Scale relative to required tolerance */
      estimationOfMaxError /= tolerance;

      double nextStep = safety * Math.abs(h) * Math.exp(pshrnk * Math.log(estimationOfMaxError));
      nextTimeStep = Math.max(minTimeStep, Math.min(maxTimeStep, nextStep));

      /* Step succeeded. Compute size of next step */
      if (estimationOfMaxError <= 1.0) {
        calculating = false;
        nextTimeStep = (estimationOfMaxError > errcon ? safety * Math.abs(h) * Math.exp(pgrow * Math.log(estimationOfMaxError)) : 4.0 * Math.abs(h));
        nextTimeStep = Math.max(minTimeStep, Math.min(maxTimeStep, nextTimeStep));
      }

      if (Math.abs(h) <= minTimeStep) {
        calculating = false;
        warning(Messages.getString("RungeKutta4.0") + estimationOfMaxError * tolerance); //$NON-NLS-1$
        nextTimeStep = minTimeStep;
      }

      if (trialTimeStep < 0.0) {
        nextTimeStep = -nextTimeStep;
      }

      if (calculating == false) {
        /* Mop up fifth-order truncation error */
        newX = xc2.add(estimationOfError.multiply(fcor));

        if (system instanceof DoublePiecewiseDifferentialSystem) {
          final DoubleMatrix newIO = system.inputOutputEquation(t0 + h, newX);
          final double discontinuousPoint = ((DoublePiecewiseDifferentialSystem)system).getDiscontinuousPoint(t0, x0, io0, t0 + h, newX, newIO);
          final double toleranceOfDiscontinuity = getToleranceOfDiscontinuity();

          if (Double.isNaN(discontinuousPoint) == false) {
            atDiscontinuousPoint = true;
            if (isSaveAtDiscontinuousPoint()) {
              setAtSavingPoint(true);
            }

            if ((discontinuousPoint - t0) > toleranceOfDiscontinuity) {
              final double safetyFactor = 0.8;
              h = (discontinuousPoint - t0) * safetyFactor;
              actualStepNextTrialStep[1] = (discontinuousPoint - t0) - h;
              calculating = true;
              continue;
            }
          }
        }

        actualStepNextTrialStep[0] = h;
        if (atDiscontinuousPoint == false) {
          actualStepNextTrialStep[1] = nextTimeStep;
        }
        setTrial(false);

      }

      h = nextTimeStep;
    }

    return newX;
    
    //assert false : Messages.getString("RungeKutta4.1"); //$NON-NLS-1$
    //return null;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix stepAuto(final DoubleDifferentialDifferenceSystem system, final double t0, final DoubleMatrix xc0, final DoubleMatrix xd0, final double trialTimeStep, final double minTimeStep,
      final double maxTimeStep, final double tolerance, final double[] actualStepNextTrialStep) throws SolverStopException {
    final double errcon = 6.0E-4;
    final double pgrow = -0.20;
    final double pshrnk = -0.25;
    final double safety = 0.9;
    final double fcor = 1.0 / 15.0;
    final double tiny = 1.0E-30;

    boolean atDiscontinuousPoint = false;

    /* Set the initial trial value to step size */
    double h = trialTimeStep;

    final DoubleMatrix io0 = system.inputOutputEquation(t0, xc0, xd0);
    final DoubleMatrix dxc0 = system.differentialEquation(t0, xc0, xd0, io0);
    final DoubleMatrix xscal =xc0.absElementWise().add(dxc0.multiply(h).absElementWise()).addElementWise(tiny);

    DoubleMatrix newX = null;
    
    double nextTimeStep = h;
    boolean calculating = true;
    setTrial(true);
    
    while (calculating) {
      final DoubleMatrix xc1 = step(system, t0, xc0, xd0, h / 2);
      final DoubleMatrix xc2 = step(system, t0 + h / 2, xc1, xd0, h / 2);
      final DoubleMatrix xc3 = step(system, t0, xc0, xd0, h);
      final DoubleMatrix estimationOfError = xc2.subtract(xc3);
      double estimationOfMaxError = (estimationOfError.divideElementWise(xscal).absElementWise()).max().doubleValue();

      /* Scale relative to required tolerance */
      estimationOfMaxError /= tolerance;

      double nextStep = safety * Math.abs(h) * Math.exp(pshrnk * Math.log(estimationOfMaxError));
      nextTimeStep = Math.max(minTimeStep, Math.min(maxTimeStep, nextStep));

      /* Step succeeded. Compute size of next step */
      if (estimationOfMaxError <= 1.0) {
        calculating = false;
        nextTimeStep = (estimationOfMaxError > errcon ? safety * Math.abs(h) * Math.exp(pgrow * Math.log(estimationOfMaxError)) : 4.0 * Math.abs(h));
        nextTimeStep = Math.max(minTimeStep, Math.min(maxTimeStep, nextTimeStep));
      }

      if (Math.abs(h) <= minTimeStep) {
        calculating = false;
        warning(Messages.getString("RungeKutta4.2") + estimationOfMaxError * tolerance); //$NON-NLS-1$
        nextTimeStep = minTimeStep;
      }

      if (trialTimeStep < 0.0) {
        nextTimeStep = -nextTimeStep;
      }

      if (calculating == false) {
        /* Mop up fifth-order truncation error */
        newX = xc2.add(estimationOfError.multiply(fcor));

        if (system instanceof DoublePiecewiseDifferentialDifferenceSystem) {
          final DoubleMatrix newIO = system.inputOutputEquation(t0 + h, newX, xd0);
          final double discontinuousPoint = ((DoublePiecewiseDifferentialDifferenceSystem)system).getDiscontinuousPoint(t0, xc0, xd0, io0, t0 + h, newX, xd0, newIO);
          final double toleranceOfDiscontinuity = getToleranceOfDiscontinuity();

          if (Double.isNaN(discontinuousPoint) == false) {
            atDiscontinuousPoint = true;
            if (isSaveAtDiscontinuousPoint()) {
              setAtSavingPoint(true);
            }

            if ((discontinuousPoint - t0) > toleranceOfDiscontinuity) {
              final double safetyFactor = 0.8;
              h = (discontinuousPoint - t0) * safetyFactor;
              actualStepNextTrialStep[1] = (discontinuousPoint - t0) - h;
              calculating = true;
              continue;
            }
          }
        }

        actualStepNextTrialStep[0] = h;
        if (atDiscontinuousPoint == false) {
          actualStepNextTrialStep[1] = nextTimeStep;
        }
        setTrial(false);

      }

      h = nextTimeStep;
    }

    return newX;
    
    //assert false : Messages.getString("RungeKutta4.3"); //$NON-NLS-1$
    //return null;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix stepAuto(final DoubleExplicitDifferentialEquation equation, final double t0, final DoubleMatrix x0, final double trialTimeStep, final double minTimeStep, final double maxTimeStep, final double tolerance,
      final double[] actualStepNextTrialStep) throws SolverStopException {
    final double errcon = 6.0E-4;
    final double pgrow = -0.20;
    final double pshrnk = -0.25;
    final double safety = 0.9;
    final double fcor = 1.0 / 15.0;
    final double tiny = 1.0E-30;

    boolean atDiscontinuousPoint = false;

    /* Set the initial trial value to step size */
    double h = trialTimeStep;

    final DoubleMatrix dxc0 = equation.differentialEquation(t0, x0);
    final DoubleMatrix xscal =x0.absElementWise().add(dxc0.multiply(h).absElementWise()).addElementWise(tiny);

    DoubleMatrix newX = null;
    
    double nextTimeStep = h;
    boolean calculating = true;
    setTrial(true);
    
    while (calculating) {
      final DoubleMatrix xc1 = step(equation, t0, x0, h / 2);
      final DoubleMatrix xc2 = step(equation, t0 + h / 2, xc1, h / 2);
      final DoubleMatrix xc3 = step(equation, t0, x0, h);
      final DoubleMatrix estimationOfError = xc2.subtract(xc3);
      double estimationOfMaxError = (estimationOfError.divideElementWise(xscal).absElementWise()).max().doubleValue();

      /* Scale relative to required tolerance */
      estimationOfMaxError /= tolerance;

      double nextStep = safety * Math.abs(h) * Math.exp(pshrnk * Math.log(estimationOfMaxError));
      nextTimeStep = Math.max(minTimeStep, Math.min(maxTimeStep, nextStep));

      /* Step succeeded. Compute size of next step */
      if (estimationOfMaxError <= 1.0) {
        calculating = false;
        nextTimeStep = (estimationOfMaxError > errcon ? safety * Math.abs(h) * Math.exp(pgrow * Math.log(estimationOfMaxError)) : 4.0 * Math.abs(h));
        nextTimeStep = Math.max(minTimeStep, Math.min(maxTimeStep, nextTimeStep));
      }

      if (Math.abs(h) <= minTimeStep) {
        calculating = false;
        warning(Messages.getString("RungeKutta4.4") + estimationOfMaxError * tolerance); //$NON-NLS-1$
        nextTimeStep = minTimeStep;
      }

      if (trialTimeStep < 0.0) {
        nextTimeStep = -nextTimeStep;
      }

      if (calculating == false) {
        /* Mop up fifth-order truncation error */
        newX = xc2.add(estimationOfError.multiply(fcor));

        if (equation instanceof DoublePiecewiseDifferentialEquation) {
          final double discontinuousPoint = ((DoublePiecewiseDifferentialEquation)equation).getDiscontinuousPoint(t0, x0, t0 + h, newX);
          final double toleranceofDiscontinuity = getToleranceOfDiscontinuity();

          if (Double.isNaN(discontinuousPoint) == false) {
            atDiscontinuousPoint = true;
            if (isSaveAtDiscontinuousPoint()) {
              setAtSavingPoint(true);
            }

            if ((discontinuousPoint - t0) > toleranceofDiscontinuity) {
              final double safetyFactor = 0.8;
              h = (discontinuousPoint - t0) * safetyFactor;
              actualStepNextTrialStep[1] = (discontinuousPoint - t0) - h;
              calculating = true;
              continue;
            }
          }
        }

        actualStepNextTrialStep[0] = h;
        if (atDiscontinuousPoint == false) {
          actualStepNextTrialStep[1] = nextTimeStep;
        }
        setTrial(false);
        
      }

      h = nextTimeStep;
    }

    return newX;
    
    //assert false : Messages.getString("RungeKutta4.5"); //$NON-NLS-1$
    //return null;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix stepAuto(final DoubleDifferentialDifferenceEquation equation, final double t0, final DoubleMatrix xc0, final DoubleMatrix xd0, final double trialTimeStep, final double minTimeStep,
      final double maxTimeStep, final double tolerance, final double[] actualStepNextTrialStep) throws SolverStopException {
    final double errcon = 6.0E-4;
    final double pgrow = -0.20;
    final double pshrnk = -0.25;
    final double safety = 0.9;
    final double fcor = 1.0 / 15.0;
    final double tiny = 1.0E-30;

    boolean atDiscontinuousPoint = false;

    /* Set the initial trial value to the step size */
    double h = trialTimeStep;

    final DoubleMatrix dxc0 = equation.differentialEquation(t0, xc0, xd0);
    final DoubleMatrix xscal =xc0.absElementWise().add(dxc0.multiply(h).absElementWise()).addElementWise(tiny);

    DoubleMatrix newX = null;
    
    double nextTimeStep = h;
    boolean calculating = true;
    setTrial(true);
    
    while (calculating) {
      final DoubleMatrix xc1 = step(equation, t0, xc0, xd0, h / 2);
      final DoubleMatrix xc2 = step(equation, t0 + h / 2, xc1, xd0, h / 2);
      final DoubleMatrix xc3 = step(equation, t0, xc0, xd0, h);
      final DoubleMatrix estimationOfError =xc2.subtract(xc3);
      double estimationOfMaxError = (estimationOfError.divideElementWise(xscal).absElementWise()).max().doubleValue();

      /* Scale relative to required tolerance */
      estimationOfMaxError /= tolerance;

      final double nextStep = safety * Math.abs(h) * Math.exp(pshrnk * Math.log(estimationOfMaxError));
      nextTimeStep = Math.max(minTimeStep, Math.min(maxTimeStep, nextStep));

      /* Step succeeded. Compute size of next step */
      if (estimationOfMaxError <= 1.0) {
        calculating = false;
        nextTimeStep = (estimationOfMaxError > errcon ? safety * Math.abs(h) * Math.exp(pgrow * Math.log(estimationOfMaxError)) : 4.0 * Math.abs(h));
        nextTimeStep = Math.max(minTimeStep, Math.min(maxTimeStep, nextTimeStep));
      }

      if (Math.abs(h) <= minTimeStep) {
        calculating = false;
        warning(Messages.getString("RungeKutta4.6") + estimationOfMaxError * tolerance); //$NON-NLS-1$
        nextTimeStep = minTimeStep;
      }

      if (trialTimeStep < 0.0) {
        nextTimeStep = -nextTimeStep;
      }

      if (calculating == false) {
        /* Mop up fifth-order truncation error */
        newX =xc2.add(estimationOfError.multiply(fcor));
        
        if (equation instanceof DoublePiecewiseDifferentialDifferenceEquation) {
          final double discontinuousPoint = ((DoublePiecewiseDifferentialDifferenceEquation)equation).getDiscontinuousPoint(t0, xc0, xd0, t0 + h, newX, xd0);
          final double toleranceofDiscontinuity = getToleranceOfDiscontinuity();

          if (Double.isNaN(discontinuousPoint) == false) {
            atDiscontinuousPoint = true;
            if (isSaveAtDiscontinuousPoint()) {
              setAtSavingPoint(true);
            }

            if ((discontinuousPoint - t0) > toleranceofDiscontinuity) {
              final double safetyFactor = 0.8;
              h = (discontinuousPoint - t0) * safetyFactor;
              actualStepNextTrialStep[1] = (discontinuousPoint - t0) - h;
              calculating = true;
              continue;
            }
          }
        }

        actualStepNextTrialStep[0] = h;
        if (atDiscontinuousPoint == false) {
          actualStepNextTrialStep[1] = nextTimeStep;
        }
        setTrial(false);
        
      }

      h = nextTimeStep;
    }

    return newX;
    
    //assert false : Messages.getString("RungeKutta4.7"); //$NON-NLS-1$
    //return null;
  }

}