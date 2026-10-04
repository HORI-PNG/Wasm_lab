/*
 * $Id: RungeKutta4.java,v 1.51 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.ode;

import org.mklab.nfc.matrix.DoubleMatrix;

/**
 * 4次のルンゲ・クッタ法を用いて常微分方程式の解を求めるソルバーを表すクラスです。
 * 
 * @author koga
 * @version $Revision: 1.51 $, 2004/05/08
 */
public class DoubleRungeKutta4 extends DoubleDifferentialEquationSolver {

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleMatrix step(final DoubleExplicitDifferentialEquation equation, final double t0, final DoubleMatrix x0, final double h) throws SolverStopException {
    final DoubleMatrix dx0 = equation.differentialEquation(t0, x0);

    setTrial(true);

    final DoubleMatrix x1 = x0.add(dx0.multiply(h / 2));
    final DoubleMatrix dx1 = equation.differentialEquation(t0 + h / 2, x1);

    final DoubleMatrix x2 = x0.add(dx1.multiply(h / 2));
    final DoubleMatrix dx2 = equation.differentialEquation(t0 + h / 2, x2);

    final DoubleMatrix x3 = x0.add(dx2.multiply(h));
    final DoubleMatrix dx3 = equation.differentialEquation(t0 + h, x3);

    setTrial(false);

    return x0.add(dx0.add(dx3).add(dx2.add(dx1).multiply(2)).multiply(h / 6));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleMatrix step(final DoubleDifferentialDifferenceEquation equation, final double t0, final DoubleMatrix xc0, final DoubleMatrix xd0, final double h) throws SolverStopException {
    final DoubleMatrix dxc0 = equation.differentialEquation(t0, xc0, xd0);

    setTrial(true);

    final DoubleMatrix xc1 = xc0.add(dxc0.multiply(h / 2));
    final DoubleMatrix dxc1 = equation.differentialEquation(t0 + h / 2, xc1, xd0);

    final DoubleMatrix xc2 = xc0.add(dxc1.multiply(h / 2));
    final DoubleMatrix dxc2 = equation.differentialEquation(t0 + h / 2, xc2, xd0);

    final DoubleMatrix xc3 = xc0.add(dxc2.multiply(h));
    final DoubleMatrix dxc3 = equation.differentialEquation(t0 + h, xc3, xd0);

    setTrial(false);

    return xc0.add(dxc0.add(dxc3).add(dxc2.add(dxc1).multiply(2)).multiply(h / 6));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleMatrix step(final DoubleExplicitDifferentialSystem system, final double t0, final DoubleMatrix x0, final double h) throws SolverStopException {
    final DoubleMatrix io0 = system.inputOutputEquation(t0, x0);
    final DoubleMatrix dx0 = system.differentialEquation(t0, x0, io0);

    setTrial(true);

    final DoubleMatrix x1 = x0.add(dx0.multiply(h / 2));
    final DoubleMatrix io1 = system.inputOutputEquation(t0 + h / 2, x1);
    final DoubleMatrix dx1 = system.differentialEquation(t0 + h / 2, x1, io1);

    final DoubleMatrix x2 = x0.add(dx1.multiply(h / 2));
    final DoubleMatrix io2 = system.inputOutputEquation(t0 + h / 2, x2);
    final DoubleMatrix dx2 = system.differentialEquation(t0 + h / 2, x2, io2);

    final DoubleMatrix x3 = x0.add(dx2.multiply(h));
    final DoubleMatrix io3 = system.inputOutputEquation(t0 + h, x3);
    final DoubleMatrix dx3 = system.differentialEquation(t0 + h, x3, io3);

    setTrial(false);

    return x0.add(dx0.add(dx3).add(dx2.add(dx1).multiply(2)).multiply(h / 6));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final DoubleMatrix step(final DoubleDifferentialDifferenceSystem system, final double t0, final DoubleMatrix xc0, final DoubleMatrix xd0, final double h) throws SolverStopException {
    final DoubleMatrix io0 = system.inputOutputEquation(t0, xc0, xd0);
    final DoubleMatrix dxc0 = system.differentialEquation(t0, xc0, xd0, io0);

    setTrial(true);

    final DoubleMatrix xc1 = xc0.add(dxc0.multiply(h / 2));
    final DoubleMatrix io1 = system.inputOutputEquation(t0 + h / 2, xc1, xd0);
    final DoubleMatrix dxc1 = system.differentialEquation(t0 + h / 2, xc1, xd0, io1);

    final DoubleMatrix xc2 = xc0.add(dxc1.multiply(h / 2));
    final DoubleMatrix io2 = system.inputOutputEquation(t0 + h / 2, xc2, xd0);
    final DoubleMatrix dxc2 = system.differentialEquation(t0 + h / 2, xc2, xd0, io2);

    final DoubleMatrix xc3 = xc0.add(dxc2.multiply(h));
    final DoubleMatrix io3 = system.inputOutputEquation(t0 + h, xc3, xd0);
    final DoubleMatrix dxc3 = system.differentialEquation(t0 + h, xc3, xd0, io3);

    setTrial(false);

    return xc0.add(dxc0.add(dxc3).add(dxc2.add(dxc1).multiply(2)).multiply(h / 6));
  }
}