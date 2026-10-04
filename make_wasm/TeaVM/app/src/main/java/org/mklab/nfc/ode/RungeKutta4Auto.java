/*
 * $Id: RungeKutta4.java,v 1.51 2008/07/16 08:00:37 koga Exp $
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
 * 4次のルンゲ・クッタ法を用いて刻み幅を自動調節しながら常微分方程式の解を求めるソルバーを表すクラスです。
 * 
 * @author koga
 * @version $Revision: 1.51 $, 2004/05/08
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 */
public class RungeKutta4Auto<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> extends DifferentialEquationAutoSolver<RS,RM,CS,CM> {
  /** Runge Kutta4 solver */
  private RungeKutta4<RS,RM,CS,CM> kutta4 = new RungeKutta4<>(this.sunit);
  
  /**
   * Creates {@link RungeKutta4Auto}.
   * @param sunit unit of scalar
   */
  public RungeKutta4Auto(RS sunit) {
    super(sunit);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final RM step(final ExplicitDifferentialEquation<RS,RM,CS,CM> equation, final RS t0, final RM x0, final RS h) throws SolverStopException {
    return this.kutta4.step(equation, t0, x0, h);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final RM step(final DifferentialDifferenceEquation<RS,RM,CS,CM> equation, final RS t0, final RM xc0, final RM xd0, final RS h) throws SolverStopException {
    return this.kutta4.step(equation, t0, xc0, xd0, h);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final RM step(final ExplicitDifferentialSystem<RS,RM,CS,CM> system, final RS t0, final RM x0, final RS h) throws SolverStopException {
    return this.kutta4.step(system, t0, x0, h);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final RM step(final DifferentialDifferenceSystem<RS,RM,CS,CM> system, final RS t0, final RM xc0, final RM xd0, final RS h) throws SolverStopException {
    return this.kutta4.step(system, t0, xc0, xd0, h);
  }

  /**
   * {@inheritDoc}
   */
  public final RM stepAuto(final ExplicitDifferentialSystem<RS,RM,CS,CM> system, final RS t0, final RM x0, final RS trialTimeStep, final RS minTimeStep, final RS maxTimeStep, final RS tolerance,
      final RS[] actualStepNextTrialStep) throws SolverStopException {
    //final RS errcon = this.sunit.create(6.0E-4);
    final RS errcon = this.sunit.create(6).divide(10000);
    //final RS pgrow = this.sunit.create(-0.20);
    final RS pgrow = this.sunit.create(2).divide(10).unaryMinus();
    //final RS pshrnk = this.sunit.create(-0.25);
    final RS pshrnk = this.sunit.create(25).divide(100).unaryMinus();
    //final RS safety = this.sunit.create(0.9);
    final RS safety = this.sunit.create(9).divide(10);
    //final RS fcor = this.sunit.create(1.0 / 15.0);
    final RS fcor = this.sunit.create(1).divide(15);
    //final RS tiny = this.sunit.create(1.0E-30);
    final RS tiny = this.sunit.create(10).power(30).inverse();

    boolean atDiscontinuousPoint = false;

    /* Set the initial trial value to step size */
    RS h = trialTimeStep;

    final RM io0 = system.inputOutputEquation(t0, x0);
    final RM dx0 = system.differentialEquation(t0, x0, io0);
    final RM xscal =x0.absElementWise().add(dx0.multiply(h).absElementWise()).addElementWise(tiny);

    RM newX = null;
    
    RS nextTimeStep = h;
    boolean calculating = true;
    setTrial(true);
    
    while (calculating) {
      final RM xc1 = step(system, t0, x0, h.divide(2));
      final RM xc2 = step(system, t0.add(h.divide(2)), xc1, h.divide(2));
      final RM xc3 = step(system, t0, x0, h);
      final RM estimationOfError = xc2.subtract(xc3);
      RS estimationOfMaxError = ((estimationOfError.divideElementWise(xscal).absElementWise())).max();

      /* Scale relative to required tolerance */
      estimationOfMaxError = estimationOfMaxError.divide(tolerance);

      RS nextStep = safety.multiply(h.abs()).multiply(pshrnk.multiply(estimationOfMaxError.log()).exp());
      nextTimeStep = minTimeStep.max(maxTimeStep.min(nextStep));

      /* Step succeeded. Compute size of next step */
      if (estimationOfMaxError.isLessThanOrEquals(1)) {
        calculating = false;
        nextTimeStep = (estimationOfMaxError .isGreaterThan(errcon) ? safety.multiply(h.abs()).multiply(pgrow.multiply(estimationOfMaxError.log()).exp()) : h.abs().multiply(4));
        nextTimeStep = minTimeStep.max(maxTimeStep.min(nextTimeStep));
      }

      if (h.abs().isLessThanOrEquals(minTimeStep)) {
        calculating = false;
        warning(Messages.getString("RungeKutta4.0") + estimationOfMaxError.multiply(tolerance)); //$NON-NLS-1$
        nextTimeStep = minTimeStep;
      }

      if (trialTimeStep.isLessThan(0)) {
        nextTimeStep = nextTimeStep.unaryMinus();
      }

      if (calculating == false) {
        /* Mop up fifth-order truncation error */
        newX = xc2.add(estimationOfError.multiply(fcor));

        if (system instanceof PiecewiseDifferentialSystem) {
          final RM newIO = system.inputOutputEquation(t0.add(h), newX);
          final RS discontinuousPoint = ((PiecewiseDifferentialSystem<RS,RM,CS,CM>)system).getDiscontinuousPoint(t0, x0, io0, t0.add(h), newX, newIO);
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
    
    //assert false : Messages.getString("RungeKutta4.1"); //$NON-NLS-1$
    //return null;
  }

  /**
   * {@inheritDoc}
   */
  public final RM stepAuto(final DifferentialDifferenceSystem<RS,RM,CS,CM> system, final RS t0, final RM xc0, final RM xd0, final RS trialTimeStep, final RS minTimeStep,
      final RS maxTimeStep, final RS tolerance, final RS[] actualStepNextTrialStep) throws SolverStopException {
    //final RS errcon = this.sunit.create(6.0E-4);
    final RS errcon = this.sunit.create(6).divide(10000);
    //final RS pgrow = this.sunit.create(-0.20);
    final RS pgrow = this.sunit.create(2).divide(10).unaryMinus();
    //final RS pshrnk = this.sunit.create(-0.25);
    final RS pshrnk = this.sunit.create(25).divide(100).unaryMinus();
    //final RS safety = this.sunit.create(0.9);
    final RS safety = this.sunit.create(9).divide(10);
    //final RS fcor = this.sunit.create(1.0 / 15.0);
    final RS fcor = this.sunit.create(1).divide(15);
    //final RS tiny = this.sunit.create(1.0E-30);
    final RS tiny = this.sunit.create(10).power(30).inverse();

    boolean atDiscontinuousPoint = false;

    /* Set the initial trial value to step size */
    RS h = trialTimeStep;

    final RM io0 = system.inputOutputEquation(t0, xc0, xd0);
    final RM dxc0 = system.differentialEquation(t0, xc0, xd0, io0);
    final RM xscal =xc0.absElementWise().add(dxc0.multiply(h).absElementWise()).addElementWise(tiny);

    RM newX = null;
    
    RS nextTimeStep = h;
    boolean calculating = true;
    setTrial(true);
    
    while (calculating) {
      final RM xc1 = step(system, t0, xc0, xd0, h.divide(2));
      final RM xc2 = step(system, t0.add(h.divide(2)), xc1, xd0, h.divide(2));
      final RM xc3 = step(system, t0, xc0, xd0, h);
      final RM estimationOfError = xc2.subtract(xc3);
      RS estimationOfMaxError = ((estimationOfError.divideElementWise(xscal).absElementWise())).max();

      /* Scale relative to required tolerance */
      estimationOfMaxError = estimationOfMaxError.divide(tolerance);

      RS nextStep = safety.multiply(h.abs()).multiply(pshrnk.multiply(estimationOfMaxError.log()).exp());
      nextTimeStep = minTimeStep.max(maxTimeStep.min(nextStep));

      /* Step succeeded. Compute size of next step */
      if (estimationOfMaxError.isLessThanOrEquals(1)) {
        calculating = false;
        nextTimeStep = (estimationOfMaxError.isGreaterThan(errcon) ? safety.multiply(h.abs()).multiply(pgrow.multiply(estimationOfMaxError.log()).exp()) : h.abs().multiply(4));
        nextTimeStep = minTimeStep.max(maxTimeStep.min(nextTimeStep));
      }

      if (h.abs().isLessThanOrEquals(minTimeStep)) {
        calculating = false;
        warning(Messages.getString("RungeKutta4.2") + estimationOfMaxError.multiply(tolerance)); //$NON-NLS-1$
        nextTimeStep = minTimeStep;
      }

      if (trialTimeStep.isLessThan(0)) {
        nextTimeStep = nextTimeStep.unaryMinus();
      }

      if (calculating == false) {
        /* Mop up fifth-order truncation error */
        newX = xc2.add(estimationOfError.multiply(fcor));

        if (system instanceof PiecewiseDifferentialDifferenceSystem) {
          final RM newIO = system.inputOutputEquation(t0.add(h), newX, xd0);
          final RS discontinuousPoint = ((PiecewiseDifferentialDifferenceSystem<RS,RM,CS,CM>)system).getDiscontinuousPoint(t0, xc0, xd0, io0, t0.add(h), newX, xd0, newIO);
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
    
    //assert false : Messages.getString("RungeKutta4.3"); //$NON-NLS-1$
    //return null;
  }

  /**
   * {@inheritDoc}
   */
  public final RM stepAuto(final ExplicitDifferentialEquation<RS,RM,CS,CM> equation, final RS t0, final RM x0, final RS trialTimeStep, final RS minTimeStep, final RS maxTimeStep, final RS tolerance,
      final RS[] actualStepNextTrialStep) throws SolverStopException {
    //final RS errcon = this.sunit.create(6.0E-4);
    final RS errcon = this.sunit.create(6).divide(10000);
    //final RS pgrow = this.sunit.create(-0.20);
    final RS pgrow = this.sunit.create(2).divide(10).unaryMinus();
    //final RS pshrnk = this.sunit.create(-0.25);
    final RS pshrnk = this.sunit.create(25).divide(100).unaryMinus();
    //final RS safety = this.sunit.create(0.9);
    final RS safety = this.sunit.create(9).divide(10);
    //final RS fcor = this.sunit.create(1.0 / 15.0);
    final RS fcor = this.sunit.create(1).divide(15);
    //final RS tiny = this.sunit.create(1.0E-30);
    final RS tiny = this.sunit.create(10).power(30).inverse();

    boolean atDiscontinuousPoint = false;

    /* Set the initial trial value to step size */
    RS h = trialTimeStep;

    final RM dxc0 = equation.differentialEquation(t0, x0);
    final RM xscal =x0.absElementWise().add(dxc0.multiply(h).absElementWise()).addElementWise(tiny);

    RM newX = null;
    
    RS nextTimeStep = h;
    boolean calculating = true;
    setTrial(true);
    
    while (calculating) {
      final RM xc1 = step(equation, t0, x0, h.divide(2));
      final RM xc2 = step(equation, t0.add(h.divide(2)), xc1, h.divide(2));
      final RM xc3 = step(equation, t0, x0, h);
      final RM estimationOfError = xc2.subtract(xc3);
      RS estimationOfMaxError = ((estimationOfError.divideElementWise(xscal).absElementWise())).max();

      /* Scale relative to required tolerance */
      estimationOfMaxError = estimationOfMaxError.divide(tolerance);

      RS nextStep = safety.multiply(h.abs()).multiply(pshrnk.multiply(estimationOfMaxError.log()).exp());
      nextTimeStep = minTimeStep.max(maxTimeStep.min(nextStep));

      /* Step succeeded. Compute size of next step */
      if (estimationOfMaxError.isLessThanOrEquals(1)) {
        calculating = false;
        nextTimeStep = (estimationOfMaxError.isGreaterThan(errcon) ? safety.multiply(h.abs()).multiply(pgrow.multiply(estimationOfMaxError.log()).exp()) :  h.abs().multiply(4));
        nextTimeStep = minTimeStep.max(maxTimeStep.min(nextTimeStep));
      }

      if (h.abs().isLessThanOrEquals(minTimeStep)) {
        calculating = false;
        warning(Messages.getString("RungeKutta4.4") + estimationOfMaxError.multiply(tolerance)); //$NON-NLS-1$
        nextTimeStep = minTimeStep;
      }

      if (trialTimeStep.isLessThan(0)) {
        nextTimeStep = nextTimeStep.unaryMinus();
      }

      if (calculating == false) {
        /* Mop up fifth-order truncation error */
        newX = xc2.add(estimationOfError.multiply(fcor));

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
    
    //assert false : Messages.getString("RungeKutta4.5"); //$NON-NLS-1$
    //return null;
  }

  /**
   * {@inheritDoc}
   */
  public final RM stepAuto(final DifferentialDifferenceEquation<RS,RM,CS,CM> equation, final RS t0, final RM xc0, final RM xd0, final RS trialTimeStep, final RS minTimeStep,
      final RS maxTimeStep, final RS tolerance, final RS[] actualStepNextTrialStep) throws SolverStopException {
    //final RS errcon = this.sunit.create(6.0E-4);
    final RS errcon = this.sunit.create(6).divide(10000);
    //final RS pgrow = this.sunit.create(-0.20);
    final RS pgrow = this.sunit.create(2).divide(10).unaryMinus();
    //final RS pshrnk = this.sunit.create(-0.25);
    final RS pshrnk = this.sunit.create(25).divide(100).unaryMinus();
    //final RS safety = this.sunit.create(0.9);
    final RS safety = this.sunit.create(9).divide(10);
    //final RS fcor = this.sunit.create(1.0 / 15.0);
    final RS fcor = this.sunit.create(1).divide(15);
    //final RS tiny = this.sunit.create(1.0E-30);
    final RS tiny = this.sunit.create(10).power(30).inverse();

    boolean atDiscontinuousPoint = false;

    /* Set the initial trial value to the step size */
    RS h = trialTimeStep;

    final RM dxc0 = equation.differentialEquation(t0, xc0, xd0);
    final RM xscal =xc0.absElementWise().add(dxc0.multiply(h).absElementWise()).addElementWise(tiny);

    RM newX = null;
    
    RS nextTimeStep = h;
    boolean calculating = true;
    setTrial(true);
    
    while (calculating) {
      final RM xc1 = step(equation, t0, xc0, xd0, h.divide(2));
      final RM xc2 = step(equation, t0.add(h.divide(2)), xc1, xd0, h.divide(2));
      final RM xc3 = step(equation, t0, xc0, xd0, h);
      final RM estimationOfError =xc2.subtract(xc3);
      RS estimationOfMaxError = ((estimationOfError.divideElementWise(xscal).absElementWise())).max();

      /* Scale relative to required tolerance */
      estimationOfMaxError = estimationOfMaxError.divide(tolerance);

      final RS nextStep = safety.multiply(h.abs()).multiply(pshrnk.multiply(estimationOfMaxError.log()).exp());
      nextTimeStep = minTimeStep.max(maxTimeStep.min(nextStep));

      /* Step succeeded. Compute size of next step */
      if (estimationOfMaxError.isLessThanOrEquals(1)) {
        calculating = false;
        nextTimeStep = (estimationOfMaxError.isGreaterThan(errcon) ? safety.multiply(h.abs()).multiply(pgrow.multiply(estimationOfMaxError.log()).exp()) :  h.abs().multiply(4));
        nextTimeStep = minTimeStep.max(maxTimeStep.min(nextTimeStep));
      }

      if (h.abs().isLessThanOrEquals(minTimeStep)) {
        calculating = false;
        warning(Messages.getString("RungeKutta4.6") + estimationOfMaxError.multiply(tolerance)); //$NON-NLS-1$
        nextTimeStep = minTimeStep;
      }

      if (trialTimeStep.isLessThan(0)) {
        nextTimeStep = nextTimeStep.unaryMinus();
      }

      if (calculating == false) {
        /* Mop up fifth-order truncation error */
        newX =xc2.add(estimationOfError.multiply(fcor));
        
        if (equation instanceof PiecewiseDifferentialDifferenceEquation) {
          final RS discontinuousPoint = ((PiecewiseDifferentialDifferenceEquation<RS,RM,CS,CM>)equation).getDiscontinuousPoint(t0, xc0, xd0, t0.add(h), newX, xd0);
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
    
    //assert false : Messages.getString("RungeKutta4.7"); //$NON-NLS-1$
    //return null;
  }

}