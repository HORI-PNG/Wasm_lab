/*
 * $Id: ModifiedEuler.java,v 1.19 2007/11/05 10:25:36 koga Exp $
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
 * 修正Euler法を用いて常微分方程式の解を求めるソルバーを表すクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.19 $, 2004/05/08
 * 
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 */
public class ModifiedEuler<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> extends DifferentialEquationSolver<RS,RM,CS,CM> {
  
  /**
   * Creates {@link ModifiedEuler}.
   * @param sunit unit of scalar
   */
  public ModifiedEuler(RS sunit) {
    super(sunit);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final RM step(final ExplicitDifferentialSystem<RS,RM,CS,CM> system, final RS t0, final RM x0, final RS h) throws SolverStopException {
    setTrial(true);
    final RM u1 = system.inputOutputEquation(t0, x0);
    final RM k1 = system.differentialEquation(t0, x0, u1);
    final RM nx = x0.add(k1.multiply( h.divide(2)));
    final RM u2 = system.inputOutputEquation(t0.add(h.divide(2)), nx);
    final RM k2 = system.differentialEquation(t0.add(h.divide(2)), nx, u2);
    setTrial(false);
    return x0.add(k2.multiply(h));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final RM step(final DifferentialDifferenceSystem<RS,RM,CS,CM> system, final RS t0, final RM xc0, final RM xd0, final RS h) throws SolverStopException {
    setTrial(true);
    final RM u1 = system.inputOutputEquation(t0, xc0, xd0);
    final RM k1 = system.differentialEquation(t0, xc0, xd0, u1);
    final RM nx = xc0.add(k1.multiply(h.divide(2)));
    final RM u2 = system.inputOutputEquation(t0.add(h.divide(2)), nx, xd0);
    final RM k2 = system.differentialEquation(t0.add(h.divide(2)), nx, xd0, u2);
    setTrial(false);
    return xc0.add(k2.multiply(h));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final RM step(final ExplicitDifferentialEquation<RS,RM,CS,CM> equation, final RS t0, final RM x0, final RS h) throws SolverStopException {
    setTrial(true);
    final RM k1 = equation.differentialEquation(t0, x0);
    final RM nx = x0.add(k1.multiply(h.divide(2)));
    final RM k2 = equation.differentialEquation(t0.add(h.divide(2)), nx);
    setTrial(false);
    return x0.add(k2.multiply(h));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final RM step(final DifferentialDifferenceEquation<RS,RM,CS,CM> equation, final RS t0, final RM xc0, final RM xd0, final RS h) throws SolverStopException {
    setTrial(true);
    final RM k1 = equation.differentialEquation(t0, xc0, xd0);
    final RM nx = xc0.add(k1.multiply(h.divide(2)));
    final RM k2 = equation.differentialEquation(t0.add(h.divide(2)), nx, xd0);
    setTrial(false);
    return xc0.add(k2.multiply(h));
  }

}