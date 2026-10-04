/*
 * $Id: ImprovedEuler.java,v 1.19 2007/11/05 10:25:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.DoubleMatrix;


/**
 * 改良Euler法を用いて常微分方程式の解を求めるソルバーを表すクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.19 $, 2004/05/08
 */
public class DoubleImprovedEuler extends DoubleDifferentialEquationSolver {

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleMatrix step(final DoubleExplicitDifferentialSystem system, final double t0, final DoubleMatrix x0, final double h) throws SolverStopException {
    setTrial(true);
    final DoubleMatrix u1 = system.inputOutputEquation(t0, x0);
    final DoubleMatrix k1 = system.differentialEquation(t0, x0, u1);
    final DoubleMatrix nx = x0.add(k1.multiply(h));
    final DoubleMatrix u2 = system.inputOutputEquation(t0 + h, nx);
    final DoubleMatrix k2 = system.differentialEquation(t0 + h, nx, u2);
    setTrial(false);
    return x0.add(k1.add(k2).multiply(h * 0.5));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleMatrix step(final DoubleDifferentialDifferenceSystem system, final double t0, final DoubleMatrix xc0, final DoubleMatrix xd0, final double h) throws SolverStopException {
    setTrial(true);
    final DoubleMatrix u1 = system.inputOutputEquation(t0, xc0, xd0);
    final DoubleMatrix k1 = system.differentialEquation(t0, xc0, xd0, u1);
    final DoubleMatrix nx = xc0.add(k1.multiply(h));
    final DoubleMatrix u2 = system.inputOutputEquation(t0 + h, nx, xd0);
    final DoubleMatrix k2 = system.differentialEquation(t0 + h, nx, xd0, u2);
    setTrial(false);
    return xc0.add(k1.add(k2).multiply(h * 0.5));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleMatrix step(final DoubleExplicitDifferentialEquation equation, final double t0, final DoubleMatrix x0, final double h) throws SolverStopException {
    setTrial(true);
    final DoubleMatrix k1 = equation.differentialEquation(t0, x0);
    final DoubleMatrix nx =x0.add(k1.multiply(h));
    final DoubleMatrix k2 = equation.differentialEquation(t0 + h, nx);
    setTrial(false);
    return x0.add(k1.add(k2).multiply(h * 0.5));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleMatrix step(final DoubleDifferentialDifferenceEquation equation, final double t0, final DoubleMatrix xc0, final DoubleMatrix xd0, final double h) throws SolverStopException {
    setTrial(true);
    final DoubleMatrix k1 = equation.differentialEquation(t0, xc0, xd0);
    final DoubleMatrix nx = xc0.add(k1.multiply(h));
    final DoubleMatrix k2 = equation.differentialEquation(t0 + h, nx, xd0);
    setTrial(false);
    return xc0.add(k1.add(k2).multiply(h * 0.5));
  }

}