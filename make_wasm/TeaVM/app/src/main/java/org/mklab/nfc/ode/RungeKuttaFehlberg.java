/*
 * $Id: RungeKuttaFehlberg.java,v 1.50 2008/07/16 08:00:37 koga Exp $
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
 * ルンゲ・クッタ・フェールベルグ法を用いて常微分方程式の解を求めるソルバーを表すクラスです。
 * 
 * @author koga
 * @version $Revision: 1.50 $, 2004/05/08
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 */
public class RungeKuttaFehlberg<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> extends DifferentialEquationAutoSolver<RS,RM,CS,CM> {
  
  /**
   * Creates {@link RungeKuttaFehlberg}.
   * @param sunit unit of scalar
   */
  public RungeKuttaFehlberg(RS sunit) {
    super(sunit);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final RM step(final ExplicitDifferentialEquation<RS,RM,CS,CM> equation, final RS t0, final RM x0, final RS h) throws SolverStopException {
    final RS h1 = h.divide(4);
    final RS h2 = h.divide(32);
    final RS h3 = h.divide(2197);
    final RS h4 = h.divide(4104);
    final RS h5 = h.divide(20520);
    final RS h6 = h.divide(7618050);

    /* dx0 */
    final RM dx0 = equation.differentialEquation(t0, x0);

    setTrial(true);

    /* dx1 */
    final RM x1 = x0.add(dx0.multiply(h1));
    final RM dx1 = equation.differentialEquation(t0.add(h.divide(4)), x1);

    /* dx2 */
    final RM x2 = x0.add(dx0.multiply(3).add(dx1.multiply(9)).multiply(h2));
    final RM dx2 = equation.differentialEquation(t0.add(h.multiply(3).divide(8)), x2);

    /* dx3 */
    final RM x3 = x0.add(dx0.multiply(1932).add(dx1.multiply(-7200)).add(dx2.multiply(7296)).multiply(h3));
    final RM dx3 = equation.differentialEquation(t0.add(h.multiply(12).divide(13)), x3);

    /* dx4 */
    final RM x4 =x0.add(dx0.multiply(8341).add(dx1.multiply(-32832)).add(dx2.multiply(29440)).add(dx3.multiply(-845)).multiply(h4));
    final RM dx4 = equation.differentialEquation(t0.add(h), x4);

    /* dx5 */
    final RM x5 = x0.add(dx0.multiply(-6080).add(dx1.multiply(41040)).add(dx2.multiply(-28352)).add(dx3.multiply(9295)).add(dx4.multiply(-5643)).multiply(h5));
    final RM dx5 = equation.differentialEquation(t0.add(h.divide(2)), x5);

    setTrial(false);

    return x0.add(dx0.multiply(902880).add(dx2.multiply(3953664)).add(dx3.multiply(3855735)).add(dx4.multiply(-1371249)).add(dx5.multiply(277020)).multiply(h6));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final RM step(final DifferentialDifferenceEquation<RS,RM,CS,CM> equation, final RS t0, final RM xc0, final RM xd0, final RS step) throws SolverStopException {
    final RS h1 = step.divide(4);
    final RS h2 = step.divide(32);
    final RS h3 = step.divide(2197);
    final RS h4 = step.divide(4104);
    final RS h5 = step.divide(20520);
    final RS h6 = step.divide(7618050);

    /* dxc0 */
    final RM dxc0 = equation.differentialEquation(t0, xc0, xd0);

    setTrial(true);

    /* dxc1 */
    final RM xc1 = xc0.add(dxc0.multiply(h1));
    final RM dxc1 = equation.differentialEquation(t0.add(step.divide(4)), xc1, xd0);

    /* dxc2 */
    final RM xc2 = xc0.add(dxc0.multiply(3).add(dxc1.multiply(9)).multiply(h2));
    final RM dxc2 = equation.differentialEquation(t0.add(step.multiply(3).divide(8)), xc2, xd0);

    /* dxc3 */
    final RM xc3 = xc0.add(dxc0.multiply(1932).add(dxc1.multiply(-7200)).add(dxc2.multiply(7296)).multiply(h3));
    final RM dxc3 = equation.differentialEquation(t0.add(step.multiply(12).divide(13)), xc3, xd0);

    /* dxc4 */
    final RM xc4 = xc0.add(dxc0.multiply(8341).add(dxc1.multiply(-32832)).add(dxc2.multiply(29440)).add(dxc3.multiply(-845)).multiply(h4));
    final RM dxc4 = equation.differentialEquation(t0.add(step), xc4, xd0);

    /* dxc5 */
    final RM xc5 = xc0.add(dxc0.multiply(-6080).add(dxc1.multiply(41040)).add(dxc2.multiply(-28352)).add(dxc3.multiply(9295)).add(dxc4.multiply(-5643)).multiply(h5));
    final RM dxc5 = equation.differentialEquation(t0.add(step.divide(2)), xc5, xd0);

    setTrial(false);

    return xc0.add(dxc0.multiply(902880).add(dxc2.multiply(3953664)).add(dxc3.multiply(3855735)).add(dxc4.multiply(-1371249)).add(dxc5.multiply(277020)).multiply(h6));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final RM step(final ExplicitDifferentialSystem<RS,RM,CS,CM> system, final RS t0, final RM x0, final RS h) throws SolverStopException {
    final RS h1 = h.divide(4);
    final RS h2 = h.divide(32);
    final RS h3 = h.divide(2197);
    final RS h4 = h.divide(4104);
    final RS h5 = h.divide(20520);
    final RS h6 = h.divide(7618050);

    /* dx0 */
    final RM io0 = system.inputOutputEquation(t0, x0);
    final RM dx0 = system.differentialEquation(t0, x0, io0);

    setTrial(true);

    /* dx1 */
    final RM x1 = x0.add(dx0.multiply(h1));
    final RM io1 = system.inputOutputEquation(t0.add(h.divide(4)), x1);
    final RM dx1 = system.differentialEquation(t0.add(h.divide(4)), x1, io1);

    /* dx2 */
    final RM x2 = x0.add(dx0.multiply(3).add(dx1.multiply(9)).multiply(h2));
    final RM io2 = system.inputOutputEquation(t0.add(h.multiply(3).divide(8)), x2);
    final RM dx2 = system.differentialEquation(t0.add(h.multiply(3).divide(8)), x2, io2);

    /* dx3 */
    final RM x3 = x0.add(dx0.multiply(1932).add(dx1.multiply(-7200)).add(dx2.multiply(7296)).multiply(h3));
    final RM io3 = system.inputOutputEquation(t0.add(h.multiply(12).divide(13)), x3);
    final RM dx3 = system.differentialEquation(t0.add(h.multiply(12).divide(13)), x3, io3);

    /* dx4 */
    final RM x4 = x0.add(dx0.multiply(8341).add(dx1.multiply(-32832)).add(dx2.multiply(29440)).add(dx3.multiply(-845)).multiply(h4));
    final RM io4 = system.inputOutputEquation(t0.add(h), x4);
    final RM dx4 = system.differentialEquation(t0.add(h), x4, io4);

    /* dx5 */
    final RM x5 = x0.add(dx0.multiply(-6080).add(dx1.multiply(41040)).add(dx2.multiply(-28352)).add(dx3.multiply(9295)).add(dx4.multiply(-5643)).multiply(h5));
    final RM io5 = system.inputOutputEquation(t0.add(h.divide(2)), x5);
    final RM dx5 = system.differentialEquation(t0.add(h.divide(2)), x5, io5);

    setTrial(false);

    return x0.add(dx0.multiply(902880).add(dx2.multiply(3953664)).add(dx3.multiply(3855735)).add(dx4.multiply(-1371249)).add(dx5.multiply(277020)).multiply(h6));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final RM step(final DifferentialDifferenceSystem<RS,RM,CS,CM> system, final RS t0, final RM xc0, final RM xd0, final RS h) throws SolverStopException {
    final RS h1 = h.divide(4);
    final RS h2 = h.divide(32);
    final RS h3 = h.divide(2197);
    final RS h4 = h.divide(4104);
    final RS h5 = h.divide(20520);
    final RS h6 = h.divide(7618050);

    /* dx0 */
    final RM io0 = system.inputOutputEquation(t0, xc0, xd0);
    final RM dx0 = system.differentialEquation(t0, xc0, xd0, io0);

    setTrial(true);

    /* dx1 */
    final RM x1 = xc0.add(dx0.multiply(h1));
    final RM io1 = system.inputOutputEquation(t0.add(h.divide(4)), x1, xd0);
    final RM dx1 = system.differentialEquation(t0.add(h.divide(4)), x1, xd0, io1);

    /* dx2 */
    final RM x2 = xc0.add(dx0.multiply(3).add(dx1.multiply(9)).multiply(h2));
    final RM io2 = system.inputOutputEquation(t0.add(h.multiply(3).divide(8)), x2, xd0);
    final RM dx2 = system.differentialEquation(t0.add(h.multiply(3).divide(8)), x2, xd0, io2);

    /* dx3 */
    final RM x3 = xc0.add(dx0.multiply(1932).add(dx1.multiply(-7200)).add(dx2.multiply(7296)).multiply(h3));
    final RM io3 = system.inputOutputEquation(t0.add(h.multiply(12).divide(13)), x3, xd0);
    final RM dx3 = system.differentialEquation(t0.add(h.multiply(12).divide(13)), x3, xd0, io3);

    /* dx4 */
    final RM x4 = xc0.add(dx0.multiply(8341).add(dx1.multiply(-32832)).add(dx2.multiply(29440)).add(dx3.multiply(-845)).multiply(h4));
    final RM io4 = system.inputOutputEquation(t0.add(h), x4, xd0);
    final RM dx4 = system.differentialEquation(t0.add(h), x4, xd0, io4);

    /* dx5 */
    final RM x5 = xc0.add(dx0.multiply(-6080).add(dx1.multiply(41040)).add(dx2.multiply(-28352)).add(dx3.multiply(9295)).add(dx4.multiply(-5643)).multiply(h5));
    final RM io5 = system.inputOutputEquation(t0.add(h.divide(2)), x5, xd0);
    final RM dx5 = system.differentialEquation(t0.add(h.divide(2)), x5, xd0, io5);

    setTrial(false);

    return xc0.add(dx0.multiply(902880).add(dx2.multiply(3953664)).add(dx3.multiply(3855735)).add(dx4.multiply(-1371249)).add(dx5.multiply(277020)).multiply(h6));
  }

  /**
   * {@inheritDoc}
   */
  public final RM stepAuto(final ExplicitDifferentialSystem<RS,RM,CS,CM> system, final RS t0, final RM x0, final RS trialTimeStep, final RS minTimeStep, final RS maxTimeStep, final RS tolerance,
      final RS[] actualStepNextTrialStep) throws SolverStopException {
    RS h = trialTimeStep;
    RS nextTimeStep = h;

    boolean atDiscontinuousPoint = false;

    /* dx0 */
    final RM io0 = system.inputOutputEquation(t0, x0);
    final RM dx0 = system.differentialEquation(t0, x0, io0);

    RM newX = null;
    
    setTrial(true);
    boolean calculating = true;

    while (calculating) {
      final RS h1 = h.divide(4);
      final RS h2 = h.divide(32);
      final RS h3 = h.divide(2197);
      final RS h4 = h.divide(4104);
      final RS h5 = h.divide(20520);
      final RS h6 = h.divide(7618050);
      final RS h7 = h.divide(752400);

      /* dx1 */
      final RM x1 = x0.add(dx0.multiply(h1));
      final RM io1 = system.inputOutputEquation(t0.add(h.divide(4)), x1);
      final RM dx1 = system.differentialEquation(t0.add(h.divide(4)), x1, io1);

      /* dx2 */
      final RM x2 = x0.add(dx0.multiply(3).add(dx1.multiply(9)).multiply(h2));
      final RM io2 = system.inputOutputEquation(t0.add(h.multiply(3).divide(8)), x2);
      final RM dx2 = system.differentialEquation(t0.add(h.multiply(3).divide(8)), x2, io2);

      /* dx3 */
      final RM x3 = x0.add(dx0.multiply(1932).add(dx1.multiply(-7200)).add(dx2.multiply(7296)).multiply(h3));
      final RM io3 = system.inputOutputEquation(t0.add(h.multiply(12).divide(13)), x3);
      final RM dx3 = system.differentialEquation(t0.add(h.multiply(12).divide(13)), x3, io3);

      /* dx4 */
      final RM x4 = x0.add(dx0.multiply(8341).add(dx1.multiply(-32832)).add(dx2.multiply(29440)).add(dx3.multiply(-845)).multiply(h4));
      final RM io4 = system.inputOutputEquation(t0.add(h), x4);
      final RM dx4 = system.differentialEquation(t0.add(h), x4, io4);

      /* dx5 */
      final RM x5 = x0.add(dx0.multiply(-6080).add(dx1.multiply(41040)).add(dx2.multiply(-28352)).add(dx3.multiply(9295)).add(dx4.multiply(-5643)).multiply(h5));
      final RM io5 = system.inputOutputEquation(t0.add(h.divide(2)), x5);
      final RM dx5 = system.differentialEquation(t0.add(h.divide(2)), x5, io5);

      final RM xx = dx0.multiply(-2090).add(dx2.multiply(22528)).add(dx3.multiply(21970)).add(dx4.multiply(-15048)).add(dx5.multiply(-27360)).multiply(h7);
      final RS estimationOfError = xx.infNorm();

      final RS estimationOfAcceptableError = tolerance.multiply(x0.infNorm().max(1));

      /* Update the step size */
      if (estimationOfError.isGreaterThan(0)) {
        nextTimeStep = minTimeStep.max(maxTimeStep.min(h.abs().multiply(this.sunit.create(8).divide(10)).multiply(estimationOfAcceptableError.divide(estimationOfError).power(this.sunit.create(2).divide(10)))));
      } else {
        nextTimeStep = maxTimeStep;
      }

      if (estimationOfError.isLessThanOrEquals(estimationOfAcceptableError)) {
        calculating = false;
      }

      if (calculating && h.abs().isLessThanOrEquals(minTimeStep)) {
        calculating = false;
        warning(Messages.getString("RungeKuttaFehlberg.0") + estimationOfError); //$NON-NLS-1$
        nextTimeStep = minTimeStep;
      }

      if (trialTimeStep.isLessThan(0)) {
        nextTimeStep = nextTimeStep.unaryMinus();
      }

      if (calculating == false) {
        newX = x0.add(dx0.multiply(902880).add(dx2.multiply(3953664)).add(dx3.multiply(3855735)).add(dx4.multiply(-1371249)).add(dx5.multiply(277020)).multiply(h6));

        if (system instanceof PiecewiseDifferentialSystem) {
          final RM newU = system.inputOutputEquation(t0.add(h), newX);
          final RS discontinuousPoint = ((PiecewiseDifferentialSystem<RS,RM,CS,CM>)system).getDiscontinuousPoint(t0, x0, io0, t0.add(h), newX, newU);
          final RS toleranceOfDiscontinuity = getToleranceOfDiscontinuity();

          if (discontinuousPoint.isNaN() == false) {
            atDiscontinuousPoint = true;
            if (isSaveAtDiscontinuousPoint()) {
              setAtSavingPoint(true);
            }

            if (discontinuousPoint.subtract(t0).isGreaterThan(toleranceOfDiscontinuity)) {
              //final RS safetyFactor = this.sunit.create(0.8);
              final RS safetyFactor = this.sunit.create(8).divide(10);
              h = discontinuousPoint.subtract(t0).multiply(safetyFactor);
              actualStepNextTrialStep[1] = discontinuousPoint.subtract(t0).subtract(h);
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
  public final RM stepAuto(final DifferentialDifferenceSystem<RS,RM,CS,CM> system, final RS t0, final RM xc0, final RM xd0, final RS trialTimeStep, final RS minTimeStep,
      final RS maxTimeStep, final RS tolerance, final RS[] actualStepNextTrialStep) throws SolverStopException {
    RS h = trialTimeStep;
    RS nextTimeStep = h;

    boolean atDiscontinuousPoint = false;

    /* dxc0 */
    final RM io0 = system.inputOutputEquation(t0, xc0, xd0);
    final RM dxc0 = system.differentialEquation(t0, xc0, xd0, io0);

    RM newXc = null;
    
    setTrial(true);
    boolean calculating = true;

    while (calculating) {
      final RS h1 = h.divide(4);
      final RS h2 = h.divide(32);
      final RS h3 = h.divide(2197);
      final RS h4 = h.divide(4104);
      final RS h5 = h.divide(20520);
      final RS h6 = h.divide(7618050);
      final RS h7 = h.divide(752400);

      /* dxc1 */
      final RM xc1 = xc0.add(dxc0.multiply(h1));
      final RM io1 = system.inputOutputEquation(t0.add(h.divide(4)), xc1, xd0);
      final RM dxc1 = system.differentialEquation(t0.add(h.divide(4)), xc1, xd0, io1);

      /* dxc2 */
      final RM xc2 = xc0.add(dxc0.multiply(3).add(dxc1.multiply(9)).multiply(h2));
      final RM io2 = system.inputOutputEquation(t0.add(h.multiply(3).divide(8)), xc2, xd0);
      final RM dxc2 = system.differentialEquation(t0.add(h.multiply(3).divide(8)), xc2, xd0, io2);

      /* dxc3 */
      final RM xc3 = xc0.add(dxc0.multiply(1932).add(dxc1.multiply(-7200)).add(dxc2.multiply(7296)).multiply(h3));
      final RM io3 = system.inputOutputEquation(t0.add(h.multiply(12).divide(13)), xc3, xd0);
      final RM dxc3 = system.differentialEquation(t0.add(h.multiply(12).divide(13)), xc3, xd0, io3);

      /* dxc4 */
      final RM xc4 = xc0.add(dxc0.multiply(8341).add(dxc1.multiply(-32832)).add(dxc2.multiply(29440)).add(dxc3.multiply(-845)).multiply(h4));
      final RM io4 = system.inputOutputEquation(t0.add(h), xc4, xd0);
      final RM dxc4 = system.differentialEquation(t0.add(h), xc4, xd0, io4);

      /* dxc5 */
      final RM xc5 = xc0.add(dxc0.multiply(-6080).add(dxc1.multiply(41040)).add(dxc2.multiply(-28352)).add(dxc3.multiply(9295)).add(dxc4.multiply(-5643)).multiply(h5));
      final RM io5 = system.inputOutputEquation(t0.add(h.divide(2)), xc5, xd0);
      final RM dxc5 = system.differentialEquation(t0.add(h.divide(2)), xc5, xd0, io5);

      final RM xx = dxc0.multiply(-2090).add(dxc2.multiply(22528)).add(dxc3.multiply(21970)).add(dxc4.multiply(-15048)).add(dxc5.multiply(-27360)).multiply(h7);
      final RS estimationOfError = xx.infNorm();

      final RS estimationOfAcceptableError = tolerance.multiply(xc0.infNorm().max(1));

      /* Update the step size */
      if (estimationOfError.isGreaterThan(0)) {
        nextTimeStep = minTimeStep.max(maxTimeStep.min(h.abs().multiply(this.sunit.create(8).divide(10)).multiply(estimationOfAcceptableError.divide(estimationOfError).power(this.sunit.create(2).divide(10)))));
      } else {
        nextTimeStep = maxTimeStep;
      }

      if (estimationOfError.isLessThanOrEquals(estimationOfAcceptableError)) {
        calculating = false;
      }

      if (calculating && h.abs().isLessThanOrEquals(minTimeStep)) {
        calculating = false;
        warning(Messages.getString("RungeKuttaFehlberg.2") + estimationOfError); //$NON-NLS-1$
        nextTimeStep = minTimeStep;
      }

      if (trialTimeStep.isLessThan(0) ) {
        nextTimeStep = nextTimeStep.unaryMinus();
      }

      if (calculating == false) {
        newXc = xc0.add(dxc0.multiply(902880).add(dxc2.multiply(3953664)).add(dxc3.multiply(3855735)).add(dxc4.multiply(-1371249)).add(dxc5.multiply(277020)).multiply(h6));

        if (system instanceof PiecewiseDifferentialDifferenceSystem) {
          final RM newU = system.inputOutputEquation(t0.add(h), newXc, xd0);
          final RS discontinuousPoint = ((PiecewiseDifferentialDifferenceSystem<RS,RM,CS,CM>)system).getDiscontinuousPoint(t0, xc0, xd0, io0, t0.add(h), newXc, xd0, newU);
          final RS toleranceOfDiscontinuity = getToleranceOfDiscontinuity();

          if (discontinuousPoint.isNaN() == false) {
            atDiscontinuousPoint = true;
            if (isSaveAtDiscontinuousPoint()) {
              setAtSavingPoint(true);
            }

            if (discontinuousPoint.subtract(t0).isGreaterThan(toleranceOfDiscontinuity)) {
              //final RS safetyFactor = this.sunit.create(0.8);
              final RS safetyFactor = this.sunit.create(8).divide(10);
              h = discontinuousPoint.subtract(t0).multiply(safetyFactor);
              actualStepNextTrialStep[1] = discontinuousPoint.subtract(t0).subtract(h);
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
  public final RM stepAuto(final ExplicitDifferentialEquation<RS,RM,CS,CM> equation, final RS t0, final RM x0, final RS trialTimeStep, final RS minTimeStep, final RS maxTimeStep, final RS tolerance,
      final RS[] actualStepNextTrialStep) throws SolverStopException {
    RS h = trialTimeStep;
    RS nextTimeStep = h;
    boolean atDiscontinuousPoint = false;

    /* dx0 */
    final RM dx0 = equation.differentialEquation(t0, x0);

    RM newX = null;
    
    setTrial(true);
    boolean calculating = true;

    while (calculating) {
      final RS h1 = h.divide(4);
      final RS h2 = h.divide(32);
      final RS h3 = h.divide(2197);
      final RS h4 = h.divide(4104);
      final RS h5 = h.divide(20520);
      final RS h6 = h.divide(7618050);
      final RS h7 = h.divide(752400);

      /* dx1 */
      final RM x1 = x0.add(dx0.multiply(h1));
      final RM dx1 = equation.differentialEquation(t0.add(h.divide(4)), x1);

      /* dx2 */
      final RM x2 = x0.add(dx0.multiply(3).add(dx1.multiply(9)).multiply(h2));
      final RM dx2 = equation.differentialEquation(t0.add(h.multiply(3).divide(8)), x2);

      /* dx3 */
      final RM x3 = x0.add(dx0.multiply(1932).add(dx1.multiply(-7200)).add(dx2.multiply(7296)).multiply(h3));
      final RM dx3 = equation.differentialEquation(t0.add(h.multiply(12).divide(13)), x3);

      /* dx4 */
      final RM x4 = x0.add(dx0.multiply(8341).add(dx1.multiply(-32832)).add(dx2.multiply(29440)).add(dx3.multiply(-845)).multiply(h4));
      final RM dx4 = equation.differentialEquation(t0.add(h), x4);

      /* dx5 */
      final RM x5 = x0.add(dx0.multiply(-6080).add(dx1.multiply(41040)).add(dx2.multiply(-28352)).add(dx3.multiply(9295)).add(dx4.multiply(-5643)).multiply(h5));
      final RM dx5 = equation.differentialEquation(t0.add(h.divide(2)), x5);

      final RM xx = dx0.multiply(-2090).add(dx2.multiply(22528)).add(dx3.multiply(21970)).add(dx4.multiply(-15048)).add(dx5.multiply(-27360)).multiply(h7);
      final RS estimationOfError = xx.infNorm();

      final RS estimationOfAcceptableError = tolerance.multiply(x0.infNorm().max(1));

      /* Update the step size */
      if (estimationOfError.isGreaterThan(0)) {
        RS nextStep = minTimeStep.max(maxTimeStep.min(h.abs().multiply(this.sunit.create(8).divide(10)).multiply(estimationOfAcceptableError.divide(estimationOfError).power(this.sunit.create(2).divide(10)))));
        nextTimeStep = nextStep;
      } else {
        nextTimeStep = maxTimeStep;
      }

      if (estimationOfError.isLessThanOrEquals(estimationOfAcceptableError)) {
        calculating = false;
      }

      if (calculating && h.abs().isLessThanOrEquals(minTimeStep)) {
        calculating = false;
        warning(Messages.getString("RungeKuttaFehlberg.4") + estimationOfError); //$NON-NLS-1$
        nextTimeStep = minTimeStep;
      }

      if (trialTimeStep.isLessThan(0)) {
        nextTimeStep = nextTimeStep.unaryMinus();
      }

      if (calculating == false) {
        newX = x0.add(dx0.multiply(902880).add(dx2.multiply(3953664)).add(dx3.multiply(3855735)).add(dx4.multiply(-1371249)).add(dx5.multiply(277020)).multiply(h6));

        if (equation instanceof PiecewiseDifferentialEquation) {
          final RS discontinuousPoint = ((PiecewiseDifferentialEquation<RS,RM,CS,CM>)equation).getDiscontinuousPoint(t0, x0, t0.add(h), newX);
          final RS toleranceofDiscontinuity = getToleranceOfDiscontinuity();

          if (discontinuousPoint.isNaN() == false) {
            atDiscontinuousPoint = true;
            if (isSaveAtDiscontinuousPoint()) {
              setAtSavingPoint(true);
            }

            if (discontinuousPoint.subtract(t0).isGreaterThan(toleranceofDiscontinuity)) {
              //final RS safetyFactor = this.sunit.create(0.8);
              final RS safetyFactor = this.sunit.create(8).divide(10);
              h = discontinuousPoint.subtract(t0).multiply(safetyFactor);
              actualStepNextTrialStep[1] = discontinuousPoint.subtract(t0).subtract(h);
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
  public final RM stepAuto(final DifferentialDifferenceEquation<RS,RM,CS,CM> equation, final RS t0, final RM xc0, final RM xd0, final RS trialTimeStep, final RS minTimeStep,
      final RS maxTimeStep, final RS tolerance, final RS[] actualStepNextTrialStep) throws SolverStopException {
    RS h = trialTimeStep;
    RS nextTimeStep = h;

    boolean atDiscontinuousPoint = false;

    /* dxc0 */
    final RM dxc0 = equation.differentialEquation(t0, xc0, xd0);

    RM newXc = null;
    
    setTrial(true);
    boolean calculating = true;

    while (calculating) {
      final RS h1 = h.divide(4);
      final RS h2 = h.divide(32);
      final RS h3 = h.divide(2197);
      final RS h4 = h.divide(4104);
      final RS h5 = h.divide(20520);
      final RS h6 = h.divide(7618050);
      final RS h7 = h.divide(752400);

      /* dxc1 */
      final RM xc1 = xc0.add(dxc0.multiply(h1));
      final RM dxc1 = equation.differentialEquation(t0.add(h.divide(4)), xc1, xd0);

      /* dxc2 */
      final RM xc2 = xc0.add(dxc0.multiply(3).add(dxc1.multiply(9)).multiply(h2));
      final RM dxc2 = equation.differentialEquation(t0.add(h.multiply(3).divide(8)), xc2, xd0);

      /* dxc3 */
      final RM xc3 = xc0.add(dxc0.multiply(1932).add(dxc1.multiply(-7200)).add(dxc2.multiply(7296)).multiply(h3));
      final RM dxc3 = equation.differentialEquation(t0.add(h.multiply(12).divide(13)), xc3, xd0);

      /* dxc4 */
      final RM xc4 = xc0.add(dxc0.multiply(8341).add(dxc1.multiply(-32832)).add(dxc2.multiply(29440)).add(dxc3.multiply(-845)).multiply(h4));
      final RM dxc4 = equation.differentialEquation(t0.add(h), xc4, xd0);

      /* dxc5 */
      final RM xc5 = xc0.add(dxc0.multiply(-6080).add(dxc1.multiply(41040)).add(dxc2.multiply(-28352)).add(dxc3.multiply(9295)).add(dxc4.multiply(-5643)).multiply(h5));
      final RM dxc5 = equation.differentialEquation(t0.add(h.divide(2)), xc5, xd0);

      final RM xx = dxc0.multiply(-2090).add(dxc2.multiply(22528)).add(dxc3.multiply(21970)).add(dxc4.multiply(-15048)).add(dxc5.multiply(-27360)).multiply(h7);
      final RS estimationOfError = xx.infNorm();

      final RS estimationOfAcceptableError = tolerance.multiply(xc0.infNorm().max(1));

      /* Update the step size */
      if (estimationOfError.isGreaterThan(0)) {
        nextTimeStep = minTimeStep.max(maxTimeStep.min(h.abs().multiply(this.sunit.create(8).divide(10)).multiply(estimationOfAcceptableError.divide(estimationOfError).power(this.sunit.create(2).divide(10)))));
      } else {
        nextTimeStep = maxTimeStep;
      }

      if (estimationOfError.isLessThanOrEquals(estimationOfAcceptableError)) {
        calculating = false;
      }

      if (calculating && h.abs().isLessThanOrEquals(minTimeStep)) {
        calculating = false;
        warning(Messages.getString("RungeKuttaFehlberg.6") + estimationOfError); //$NON-NLS-1$
        nextTimeStep = minTimeStep;
      }

      if (trialTimeStep.isLessThan(0)) {
        nextTimeStep = nextTimeStep.unaryMinus();
      }

      if (calculating == false) {
        newXc = xc0.add(dxc0.multiply(902880).add(dxc2.multiply(3953664)).add(dxc3.multiply(3855735)).add(dxc4.multiply(-1371249)).add(dxc5.multiply(277020)).multiply(h6));

        if (equation instanceof PiecewiseDifferentialDifferenceEquation) {
          final RS discontinuousPoint = ((PiecewiseDifferentialDifferenceEquation<RS,RM,CS,CM>)equation).getDiscontinuousPoint(t0, xc0, xd0, t0.add(h), newXc, xd0);
          final RS toleranceofDiscontinuity = getToleranceOfDiscontinuity();

          if (discontinuousPoint.isNaN() == false) {
            atDiscontinuousPoint = true;
            if (isSaveAtDiscontinuousPoint()) {
              setAtSavingPoint(true);
            }

            if (discontinuousPoint.subtract(t0).isGreaterThan(toleranceofDiscontinuity)) {
              //final RS safetyFactor = this.sunit.create(0.8);
              final RS safetyFactor = this.sunit.create(8).divide(10);
              h = discontinuousPoint.subtract(t0).multiply(safetyFactor);
              actualStepNextTrialStep[1] = discontinuousPoint.subtract(t0).subtract(h);
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