/*
 * $Id: RungeKuttaFehlberg.java,v 1.50 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.DoubleMatrix;


/**
 * ルンゲ・クッタ・フェールベルグ法を用いて常微分方程式の解を求めるソルバーを表すクラスです。
 * 
 * @author koga
 * @version $Revision: 1.50 $, 2004/05/08
 */
public class DoubleRungeKuttaFehlberg extends DoubleDifferentialEquationAutoSolver {

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleMatrix step(final DoubleExplicitDifferentialEquation equation, final double t0, final DoubleMatrix x0, final double h) throws SolverStopException {
    final double h1 = h / 4.0;
    final double h2 = h / 32.0;
    final double h3 = h / 2197.0;
    final double h4 = h / 4104.0;
    final double h5 = h / 20520.0;
    final double h6 = h / 7618050.0;

    /* dx0 */
    final DoubleMatrix dx0 = equation.differentialEquation(t0, x0);

    setTrial(true);

    /* dx1 */
    final DoubleMatrix x1 = x0.add(dx0.multiply(h1));
    final DoubleMatrix dx1 = equation.differentialEquation(t0 + h / 4, x1);

    /* dx2 */
    final DoubleMatrix x2 = x0.add(dx0.multiply(3).add(dx1.multiply(9)).multiply(h2));
    final DoubleMatrix dx2 = equation.differentialEquation(t0 + h * (3.0 / 8.0), x2);

    /* dx3 */
    final DoubleMatrix x3 = x0.add(dx0.multiply(1932).add(dx1.multiply(-7200)).add(dx2.multiply(7296)).multiply(h3));
    final DoubleMatrix dx3 = equation.differentialEquation(t0 + h * (12.0 / 13.0), x3);

    /* dx4 */
    final DoubleMatrix x4 =x0.add(dx0.multiply(8341).add(dx1.multiply(-32832)).add(dx2.multiply(29440)).add(dx3.multiply(-845)).multiply(h4));
    final DoubleMatrix dx4 = equation.differentialEquation(t0 + h, x4);

    /* dx5 */
    final DoubleMatrix x5 = x0.add(dx0.multiply(-6080).add(dx1.multiply(41040)).add(dx2.multiply(-28352)).add(dx3.multiply(9295)).add(dx4.multiply(-5643)).multiply(h5));
    final DoubleMatrix dx5 = equation.differentialEquation(t0 + h / 2.0, x5);

    setTrial(false);

    return x0.add(dx0.multiply(902880.0).add(dx2.multiply(3953664.0)).add(dx3.multiply(3855735.0)).add(dx4.multiply(-1371249.0)).add(dx5.multiply(277020.0)).multiply(h6));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleMatrix step(final DoubleDifferentialDifferenceEquation equation, final double t0, final DoubleMatrix xc0, final DoubleMatrix xd0, final double step) throws SolverStopException {
    final double h1 = step / 4.0;
    final double h2 = step / 32.0;
    final double h3 = step / 2197.0;
    final double h4 = step / 4104.0;
    final double h5 = step / 20520.0;
    final double h6 = step / 7618050.0;

    /* dxc0 */
    final DoubleMatrix dxc0 = equation.differentialEquation(t0, xc0, xd0);

    setTrial(true);

    /* dxc1 */
    final DoubleMatrix xc1 = xc0.add(dxc0.multiply(h1));
    final DoubleMatrix dxc1 = equation.differentialEquation(t0 + step / 4, xc1, xd0);

    /* dxc2 */
    final DoubleMatrix xc2 = xc0.add(dxc0.multiply(3).add(dxc1.multiply(9)).multiply(h2));
    final DoubleMatrix dxc2 = equation.differentialEquation(t0 + step * (3.0 / 8.0), xc2, xd0);

    /* dxc3 */
    final DoubleMatrix xc3 = xc0.add(dxc0.multiply(1932).add(dxc1.multiply(-7200)).add(dxc2.multiply(7296)).multiply(h3));
    final DoubleMatrix dxc3 = equation.differentialEquation(t0 + step * (12.0 / 13.0), xc3, xd0);

    /* dxc4 */
    final DoubleMatrix xc4 = xc0.add(dxc0.multiply(8341).add(dxc1.multiply(-32832)).add(dxc2.multiply(29440)).add(dxc3.multiply(-845)).multiply(h4));
    final DoubleMatrix dxc4 = equation.differentialEquation(t0 + step, xc4, xd0);

    /* dxc5 */
    final DoubleMatrix xc5 = xc0.add(dxc0.multiply(-6080).add(dxc1.multiply(41040)).add(dxc2.multiply(-28352)).add(dxc3.multiply(9295)).add(dxc4.multiply(-5643)).multiply(h5));
    final DoubleMatrix dxc5 = equation.differentialEquation(t0 + step / 2.0, xc5, xd0);

    setTrial(false);

    return xc0.add(dxc0.multiply(902880.0).add(dxc2.multiply(3953664.0)).add(dxc3.multiply(3855735.0)).add(dxc4.multiply(-1371249.0)).add(dxc5.multiply(277020.0)).multiply(h6));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleMatrix step(final DoubleExplicitDifferentialSystem system, final double t0, final DoubleMatrix x0, final double h) throws SolverStopException {
    final double h1 = h / 4.0;
    final double h2 = h / 32.0;
    final double h3 = h / 2197.0;
    final double h4 = h / 4104.0;
    final double h5 = h / 20520.0;
    final double h6 = h / 7618050.0;

    /* dx0 */
    final DoubleMatrix io0 = system.inputOutputEquation(t0, x0);
    final DoubleMatrix dx0 = system.differentialEquation(t0, x0, io0);

    setTrial(true);

    /* dx1 */
    final DoubleMatrix x1 = x0.add(dx0.multiply(h1));
    final DoubleMatrix io1 = system.inputOutputEquation(t0 + h / 4, x1);
    final DoubleMatrix dx1 = system.differentialEquation(t0 + h / 4, x1, io1);

    /* dx2 */
    final DoubleMatrix x2 = x0.add(dx0.multiply(3).add(dx1.multiply(9)).multiply(h2));
    final DoubleMatrix io2 = system.inputOutputEquation(t0 + h * (3.0 / 8.0), x2);
    final DoubleMatrix dx2 = system.differentialEquation(t0 + h * (3.0 / 8.0), x2, io2);

    /* dx3 */
    final DoubleMatrix x3 = x0.add(dx0.multiply(1932).add(dx1.multiply(-7200)).add(dx2.multiply(7296)).multiply(h3));
    final DoubleMatrix io3 = system.inputOutputEquation(t0 + h * (12.0 / 13.0), x3);
    final DoubleMatrix dx3 = system.differentialEquation(t0 + h * (12.0 / 13.0), x3, io3);

    /* dx4 */
    final DoubleMatrix x4 = x0.add(dx0.multiply(8341).add(dx1.multiply(-32832)).add(dx2.multiply(29440)).add(dx3.multiply(-845)).multiply(h4));
    final DoubleMatrix io4 = system.inputOutputEquation(t0 + h, x4);
    final DoubleMatrix dx4 = system.differentialEquation(t0 + h, x4, io4);

    /* dx5 */
    final DoubleMatrix x5 = x0.add(dx0.multiply(-6080).add(dx1.multiply(41040)).add(dx2.multiply(-28352)).add(dx3.multiply(9295)).add(dx4.multiply(-5643)).multiply(h5));
    final DoubleMatrix io5 = system.inputOutputEquation(t0 + h / 2.0, x5);
    final DoubleMatrix dx5 = system.differentialEquation(t0 + h / 2.0, x5, io5);

    setTrial(false);

    return x0.add(dx0.multiply(902880.0).add(dx2.multiply(3953664.0)).add(dx3.multiply(3855735.0)).add(dx4.multiply(-1371249.0)).add(dx5.multiply(277020.0)).multiply(h6));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleMatrix step(final DoubleDifferentialDifferenceSystem system, final double t0, final DoubleMatrix xc0, final DoubleMatrix xd0, final double h) throws SolverStopException {
    final double h1 = h / 4.0;
    final double h2 = h / 32.0;
    final double h3 = h / 2197.0;
    final double h4 = h / 4104.0;
    final double h5 = h / 20520.0;
    final double h6 = h / 7618050.0;

    /* dx0 */
    final DoubleMatrix io0 = system.inputOutputEquation(t0, xc0, xd0);
    final DoubleMatrix dx0 = system.differentialEquation(t0, xc0, xd0, io0);

    setTrial(true);

    /* dx1 */
    final DoubleMatrix x1 = xc0.add(dx0.multiply(h1));
    final DoubleMatrix io1 = system.inputOutputEquation(t0 + h / 4, x1, xd0);
    final DoubleMatrix dx1 = system.differentialEquation(t0 + h / 4, x1, xd0, io1);

    /* dx2 */
    final DoubleMatrix x2 = xc0.add(dx0.multiply(3).add(dx1.multiply(9)).multiply(h2));
    final DoubleMatrix io2 = system.inputOutputEquation(t0 + h * (3.0 / 8.0), x2, xd0);
    final DoubleMatrix dx2 = system.differentialEquation(t0 + h * (3.0 / 8.0), x2, xd0, io2);

    /* dx3 */
    final DoubleMatrix x3 = xc0.add(dx0.multiply(1932).add(dx1.multiply(-7200)).add(dx2.multiply(7296)).multiply(h3));
    final DoubleMatrix io3 = system.inputOutputEquation(t0 + h * (12.0 / 13.0), x3, xd0);
    final DoubleMatrix dx3 = system.differentialEquation(t0 + h * (12.0 / 13.0), x3, xd0, io3);

    /* dx4 */
    final DoubleMatrix x4 = xc0.add(dx0.multiply(8341).add(dx1.multiply(-32832)).add(dx2.multiply(29440)).add(dx3.multiply(-845)).multiply(h4));
    final DoubleMatrix io4 = system.inputOutputEquation(t0 + h, x4, xd0);
    final DoubleMatrix dx4 = system.differentialEquation(t0 + h, x4, xd0, io4);

    /* dx5 */
    final DoubleMatrix x5 = xc0.add(dx0.multiply(-6080).add(dx1.multiply(41040)).add(dx2.multiply(-28352)).add(dx3.multiply(9295)).add(dx4.multiply(-5643)).multiply(h5));
    final DoubleMatrix io5 = system.inputOutputEquation(t0 + h / 2.0, x5, xd0);
    final DoubleMatrix dx5 = system.differentialEquation(t0 + h / 2.0, x5, xd0, io5);

    setTrial(false);

    return xc0.add(dx0.multiply(902880.0).add(dx2.multiply(3953664.0)).add(dx3.multiply(3855735.0)).add(dx4.multiply(-1371249.0)).add(dx5.multiply(277020.0)).multiply(h6));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix stepAuto(final DoubleExplicitDifferentialSystem system, final double t0, final DoubleMatrix x0, final double trialTimeStep, final double minTimeStep, final double maxTimeStep, final double tolerance,
      final double[] actualStepNextTrialStep) throws SolverStopException {
    double h = trialTimeStep;
    double nextTimeStep = h;

    boolean atDiscontinuousPoint = false;

    /* dx0 */
    final DoubleMatrix io0 = system.inputOutputEquation(t0, x0);
    final DoubleMatrix dx0 = system.differentialEquation(t0, x0, io0);

    DoubleMatrix newX = null;
    
    setTrial(true);
    boolean calculating = true;

    while (calculating) {
      final double h1 = h / 4.0;
      final double h2 = h / 32.0;
      final double h3 = h / 2197.0;
      final double h4 = h / 4104.0;
      final double h5 = h / 20520.0;
      final double h6 = h / 7618050.0;
      final double h7 = h / 752400.0;

      /* dx1 */
      final DoubleMatrix x1 = x0.add(dx0.multiply(h1));
      final DoubleMatrix io1 = system.inputOutputEquation(t0 + h / 4.0, x1);
      final DoubleMatrix dx1 = system.differentialEquation(t0 + h / 4.0, x1, io1);

      /* dx2 */
      final DoubleMatrix x2 = x0.add(dx0.multiply(3.0).add(dx1.multiply(9.0)).multiply(h2));
      final DoubleMatrix io2 = system.inputOutputEquation(t0 + h * 3.0 / 8.0, x2);
      final DoubleMatrix dx2 = system.differentialEquation(t0 + h * 3.0 / 8.0, x2, io2);

      /* dx3 */
      final DoubleMatrix x3 = x0.add(dx0.multiply(1932.0).add(dx1.multiply(-7200.0)).add(dx2.multiply(7296.0)).multiply(h3));
      final DoubleMatrix io3 = system.inputOutputEquation(t0 + h * 12.0 / 13.0, x3);
      final DoubleMatrix dx3 = system.differentialEquation(t0 + h * 12.0 / 13.0, x3, io3);

      /* dx4 */
      final DoubleMatrix x4 = x0.add(dx0.multiply(8341.0).add(dx1.multiply(-32832.0)).add(dx2.multiply(29440.0)).add(dx3.multiply(-845.0)).multiply(h4));
      final DoubleMatrix io4 = system.inputOutputEquation(t0 + h, x4);
      final DoubleMatrix dx4 = system.differentialEquation(t0 + h, x4, io4);

      /* dx5 */
      final DoubleMatrix x5 = x0.add(dx0.multiply(-6080.0).add(dx1.multiply(41040.0)).add(dx2.multiply(-28352.0)).add(dx3.multiply(9295.0)).add(dx4.multiply(-5643.0)).multiply(h5));
      final DoubleMatrix io5 = system.inputOutputEquation(t0 + h / 2.0, x5);
      final DoubleMatrix dx5 = system.differentialEquation(t0 + h / 2.0, x5, io5);

      final DoubleMatrix xx = dx0.multiply(-2090.0).add(dx2.multiply(22528.0)).add(dx3.multiply(21970.0)).add(dx4.multiply(-15048.0)).add(dx5.multiply(-27360.0)).multiply(h7);
      final double estimationOfError = xx.infNorm().doubleValue();

      final double estimationOfAcceptableError = tolerance * Math.max(x0.infNorm().doubleValue(), 1.0);

      /* Update the step size */
      if (estimationOfError > 0.0) {
        nextTimeStep = Math.max(minTimeStep, Math.min(maxTimeStep, 0.8 * Math.abs(h) * Math.pow((estimationOfAcceptableError / estimationOfError), 0.2)));
      } else {
        nextTimeStep = maxTimeStep;
      }

      if (estimationOfError <= estimationOfAcceptableError) {
        calculating = false;
      }

      if (calculating && Math.abs(h) <= minTimeStep) {
        calculating = false;
        warning(Messages.getString("RungeKuttaFehlberg.0") + estimationOfError); //$NON-NLS-1$
        nextTimeStep = minTimeStep;
      }

      if (trialTimeStep < 0.0) {
        nextTimeStep = -nextTimeStep;
      }

      if (calculating == false) {
        newX = x0.add(dx0.multiply(902880.0).add(dx2.multiply(3953664.0)).add(dx3.multiply(3855735.0)).add(dx4.multiply(-1371249.0)).add(dx5.multiply(277020.0)).multiply(h6));

        if (system instanceof DoublePiecewiseDifferentialSystem) {
          final DoubleMatrix newU = system.inputOutputEquation(t0 + h, newX);
          final double discontinuousPoint = ((DoublePiecewiseDifferentialSystem)system).getDiscontinuousPoint(t0, x0, io0, t0 + h, newX, newU);
          final double toleranceOfDiscontinuity = getToleranceOfDiscontinuity();

          if (Double.isNaN(discontinuousPoint) == false) {
            atDiscontinuousPoint = true;
            if (isSaveAtDiscontinuousPoint()) {
              setAtSavingPoint(true);
            }

            if (discontinuousPoint - t0 > toleranceOfDiscontinuity) {
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
    
    //assert false : Messages.getString("RungeKuttaFehlberg.1"); //$NON-NLS-1$
    //return null;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix stepAuto(final DoubleDifferentialDifferenceSystem system, final double t0, final DoubleMatrix xc0, final DoubleMatrix xd0, final double trialTimeStep, final double minTimeStep,
      final double maxTimeStep, final double tolerance, final double[] actualStepNextTrialStep) throws SolverStopException {
    double h = trialTimeStep;
    double nextTimeStep = h;

    boolean atDiscontinuousPoint = false;

    /* dxc0 */
    final DoubleMatrix io0 = system.inputOutputEquation(t0, xc0, xd0);
    final DoubleMatrix dxc0 = system.differentialEquation(t0, xc0, xd0, io0);

    DoubleMatrix newXc = null;
    
    setTrial(true);
    boolean calculating = true;

    while (calculating) {
      final double h1 = h / 4.0;
      final double h2 = h / 32.0;
      final double h3 = h / 2197.0;
      final double h4 = h / 4104.0;
      final double h5 = h / 20520.0;
      final double h6 = h / 7618050.0;
      final double h7 = h / 752400.0;

      /* dxc1 */
      final DoubleMatrix xc1 = xc0.add(dxc0.multiply(h1));
      final DoubleMatrix io1 = system.inputOutputEquation(t0 + h / 4.0, xc1, xd0);
      final DoubleMatrix dxc1 = system.differentialEquation(t0 + h / 4.0, xc1, xd0, io1);

      /* dxc2 */
      final DoubleMatrix xc2 = xc0.add(dxc0.multiply(3.0).add(dxc1.multiply(9.0)).multiply(h2));
      final DoubleMatrix io2 = system.inputOutputEquation(t0 + h * 3.0 / 8.0, xc2, xd0);
      final DoubleMatrix dxc2 = system.differentialEquation(t0 + h * 3.0 / 8.0, xc2, xd0, io2);

      /* dxc3 */
      final DoubleMatrix xc3 = xc0.add(dxc0.multiply(1932.0).add(dxc1.multiply(-7200.0)).add(dxc2.multiply(7296.0)).multiply(h3));
      final DoubleMatrix io3 = system.inputOutputEquation(t0 + h * 12.0 / 13.0, xc3, xd0);
      final DoubleMatrix dxc3 = system.differentialEquation(t0 + h * 12.0 / 13.0, xc3, xd0, io3);

      /* dxc4 */
      final DoubleMatrix xc4 = xc0.add(dxc0.multiply(8341.0).add(dxc1.multiply(-32832.0)).add(dxc2.multiply(29440.0)).add(dxc3.multiply(-845.0)).multiply(h4));
      final DoubleMatrix io4 = system.inputOutputEquation(t0 + h, xc4, xd0);
      final DoubleMatrix dxc4 = system.differentialEquation(t0 + h, xc4, xd0, io4);

      /* dxc5 */
      final DoubleMatrix xc5 = xc0.add(dxc0.multiply(-6080.0).add(dxc1.multiply(41040.0)).add(dxc2.multiply(-28352.0)).add(dxc3.multiply(9295.0)).add(dxc4.multiply(-5643.0)).multiply(h5));
      final DoubleMatrix io5 = system.inputOutputEquation(t0 + h / 2.0, xc5, xd0);
      final DoubleMatrix dxc5 = system.differentialEquation(t0 + h / 2.0, xc5, xd0, io5);

      final DoubleMatrix xx = dxc0.multiply(-2090.0).add(dxc2.multiply(22528.0)).add(dxc3.multiply(21970.0)).add(dxc4.multiply(-15048.0)).add(dxc5.multiply(-27360.0)).multiply(h7);
      final double estimationOfError = xx.infNorm().doubleValue();

      final double estimationOfAcceptableError = tolerance * Math.max(xc0.infNorm().doubleValue(), 1.0);

      /* Update the step size */
      if (estimationOfError > 0.0) {
        nextTimeStep = Math.max(minTimeStep, Math.min(maxTimeStep, 0.8 * Math.abs(h) * Math.pow((estimationOfAcceptableError / estimationOfError), 0.2)));
      } else {
        nextTimeStep = maxTimeStep;
      }

      if (estimationOfError <= estimationOfAcceptableError) {
        calculating = false;
      }

      if (calculating && Math.abs(h) <= minTimeStep) {
        calculating = false;
        warning(Messages.getString("RungeKuttaFehlberg.2") + estimationOfError); //$NON-NLS-1$
        nextTimeStep = minTimeStep;
      }

      if (trialTimeStep < 0.0) {
        nextTimeStep = -nextTimeStep;
      }

      if (calculating == false) {
        newXc = xc0.add(dxc0.multiply(902880.0).add(dxc2.multiply(3953664.0)).add(dxc3.multiply(3855735.0)).add(dxc4.multiply(-1371249.0)).add(dxc5.multiply(277020.0)).multiply(h6));

        if (system instanceof DoublePiecewiseDifferentialDifferenceSystem) {
          final DoubleMatrix newU = system.inputOutputEquation(t0 + h, newXc, xd0);
          final double discontinuousPoint = ((DoublePiecewiseDifferentialDifferenceSystem)system).getDiscontinuousPoint(t0, xc0, xd0, io0, t0 + h, newXc, xd0, newU);
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

    return newXc;
    
    //assert false : Messages.getString("RungeKuttaFehlberg.3"); //$NON-NLS-1$
    //return null;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix stepAuto(final DoubleExplicitDifferentialEquation equation, final double t0, final DoubleMatrix x0, final double trialTimeStep, final double minTimeStep, final double maxTimeStep, final double tolerance,
      final double[] actualStepNextTrialStep) throws SolverStopException {
    double h = trialTimeStep;
    double nextTimeStep = h;
    boolean atDiscontinuousPoint = false;

    /* dx0 */
    final DoubleMatrix dx0 = equation.differentialEquation(t0, x0);

    DoubleMatrix newX = null;
    
    setTrial(true);
    boolean calculating = true;

    while (calculating) {
      final double h1 = h / 4.0;
      final double h2 = h / 32.0;
      final double h3 = h / 2197.0;
      final double h4 = h / 4104.0;
      final double h5 = h / 20520.0;
      final double h6 = h / 7618050.0;
      final double h7 = h / 752400.0;

      /* dx1 */
      final DoubleMatrix x1 = x0.add(dx0.multiply(h1));
      final DoubleMatrix dx1 = equation.differentialEquation(t0 + h / 4.0, x1);

      /* dx2 */
      final DoubleMatrix x2 = x0.add(dx0.multiply(3.0).add(dx1.multiply(9.0)).multiply(h2));
      final DoubleMatrix dx2 = equation.differentialEquation(t0 + h * 3.0 / 8.0, x2);

      /* dx3 */
      final DoubleMatrix x3 = x0.add(dx0.multiply(1932.0).add(dx1.multiply(-7200.0)).add(dx2.multiply(7296.0)).multiply(h3));
      final DoubleMatrix dx3 = equation.differentialEquation(t0 + h * 12.0 / 13.0, x3);

      /* dx4 */
      final DoubleMatrix x4 = x0.add(dx0.multiply(8341.0).add(dx1.multiply(-32832.0)).add(dx2.multiply(29440.0)).add(dx3.multiply(-845.0)).multiply(h4));
      final DoubleMatrix dx4 = equation.differentialEquation(t0 + h, x4);

      /* dx5 */
      final DoubleMatrix x5 = x0.add(dx0.multiply(-6080.0).add(dx1.multiply(41040.0)).add(dx2.multiply(-28352.0)).add(dx3.multiply(9295.0)).add(dx4.multiply(-5643.0)).multiply(h5));
      final DoubleMatrix dx5 = equation.differentialEquation(t0 + h / 2.0, x5);

      final DoubleMatrix xx = dx0.multiply(-2090.0).add(dx2.multiply(22528.0)).add(dx3.multiply(21970.0)).add(dx4.multiply(-15048.0)).add(dx5.multiply(-27360.0)).multiply(h7);
      final double estimationOfError = xx.infNorm().doubleValue();

      final double estimationOfAcceptableError = tolerance * Math.max(x0.infNorm().doubleValue(), 1.0);

      /* Update the step size */
      if (estimationOfError > 0.0) {
        double nextStep = Math.max(minTimeStep, Math.min(maxTimeStep, 0.8 * Math.abs(h) * Math.pow((estimationOfAcceptableError / estimationOfError), 0.2)));
        nextTimeStep = nextStep;
      } else {
        nextTimeStep = maxTimeStep;
      }

      if (estimationOfError <= estimationOfAcceptableError) {
        calculating = false;
      }

      if (calculating && Math.abs(h) <= minTimeStep) {
        calculating = false;
        warning(Messages.getString("RungeKuttaFehlberg.4") + estimationOfError); //$NON-NLS-1$
        nextTimeStep = minTimeStep;
      }

      if (trialTimeStep < 0.0) {
        nextTimeStep = -nextTimeStep;
      }

      if (calculating == false) {
        newX = x0.add(dx0.multiply(902880.0).add(dx2.multiply(3953664.0)).add(dx3.multiply(3855735.0)).add(dx4.multiply(-1371249.0)).add(dx5.multiply(277020.0)).multiply(h6));

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

    //assert false : Messages.getString("RungeKuttaFehlberg.5"); //$NON-NLS-1$
    //return null;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix stepAuto(final DoubleDifferentialDifferenceEquation equation, final double t0, final DoubleMatrix xc0, final DoubleMatrix xd0, final double trialTimeStep, final double minTimeStep,
      final double maxTimeStep, final double tolerance, final double[] actualStepNextTrialStep) throws SolverStopException {
    double h = trialTimeStep;
    double nextTimeStep = h;

    boolean atDiscontinuousPoint = false;

    /* dxc0 */
    final DoubleMatrix dxc0 = equation.differentialEquation(t0, xc0, xd0);

    DoubleMatrix newXc = null;
    
    setTrial(true);
    boolean calculating = true;

    while (calculating) {
      final double h1 = h / 4.0;
      final double h2 = h / 32.0;
      final double h3 = h / 2197.0;
      final double h4 = h / 4104.0;
      final double h5 = h / 20520.0;
      final double h6 = h / 7618050.0;
      final double h7 = h / 752400.0;

      /* dxc1 */
      final DoubleMatrix xc1 = xc0.add(dxc0.multiply(h1));
      final DoubleMatrix dxc1 = equation.differentialEquation(t0 + h / 4.0, xc1, xd0);

      /* dxc2 */
      final DoubleMatrix xc2 = xc0.add(dxc0.multiply(3.0).add(dxc1.multiply(9.0)).multiply(h2));
      final DoubleMatrix dxc2 = equation.differentialEquation(t0 + h * 3.0 / 8.0, xc2, xd0);

      /* dxc3 */
      final DoubleMatrix xc3 = xc0.add(dxc0.multiply(1932.0).add(dxc1.multiply(-7200.0)).add(dxc2.multiply(7296.0)).multiply(h3));
      final DoubleMatrix dxc3 = equation.differentialEquation(t0 + h * 12.0 / 13.0, xc3, xd0);

      /* dxc4 */
      final DoubleMatrix xc4 = xc0.add(dxc0.multiply(8341.0).add(dxc1.multiply(-32832.0)).add(dxc2.multiply(29440.0)).add(dxc3.multiply(-845.0)).multiply(h4));
      final DoubleMatrix dxc4 = equation.differentialEquation(t0 + h, xc4, xd0);

      /* dxc5 */
      final DoubleMatrix xc5 = xc0.add(dxc0.multiply(-6080.0).add(dxc1.multiply(41040.0)).add(dxc2.multiply(-28352.0)).add(dxc3.multiply(9295.0)).add(dxc4.multiply(-5643.0)).multiply(h5));
      final DoubleMatrix dxc5 = equation.differentialEquation(t0 + h / 2.0, xc5, xd0);

      final DoubleMatrix xx = dxc0.multiply(-2090.0).add(dxc2.multiply(22528.0)).add(dxc3.multiply(21970.0)).add(dxc4.multiply(-15048.0)).add(dxc5.multiply(-27360.0)).multiply(h7);
      final double estimationOfError = xx.infNorm().doubleValue();

      final double estimationOfAcceptableError = tolerance * Math.max(xc0.infNorm().doubleValue(), 1.0);

      /* Update the step size */
      if (estimationOfError > 0.0) {
        nextTimeStep = Math.max(minTimeStep, Math.min(maxTimeStep, 0.8 * Math.abs(h) * Math.pow((estimationOfAcceptableError / estimationOfError), 0.2)));
      } else {
        nextTimeStep = maxTimeStep;
      }

      if (estimationOfError <= estimationOfAcceptableError) {
        calculating = false;
      }

      if (calculating && Math.abs(h) <= minTimeStep) {
        calculating = false;
        warning(Messages.getString("RungeKuttaFehlberg.6") + estimationOfError); //$NON-NLS-1$
        nextTimeStep = minTimeStep;
      }

      if (trialTimeStep < 0.0) {
        nextTimeStep = -nextTimeStep;
      }

      if (calculating == false) {
        newXc = xc0.add(dxc0.multiply(902880.0).add(dxc2.multiply(3953664.0)).add(dxc3.multiply(3855735.0)).add(dxc4.multiply(-1371249.0)).add(dxc5.multiply(277020.0)).multiply(h6));

        if (equation instanceof DoublePiecewiseDifferentialDifferenceEquation) {
          final double discontinuousPoint = ((DoublePiecewiseDifferentialDifferenceEquation)equation).getDiscontinuousPoint(t0, xc0, xd0, t0 + h, newXc, xd0);
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

    return newXc;
    
    //assert false : Messages.getString("RungeKuttaFehlberg.7"); //$NON-NLS-1$
    //return null;
  }

}