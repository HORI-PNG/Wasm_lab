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
 * 4次のルンゲ・クッタ法を用いて常微分方程式の解を求めるソルバーを表すクラスです。
 * 
 * @author koga
 * @version $Revision: 1.51 $, 2004/05/08
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 */
public class RungeKutta4<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> extends DifferentialEquationSolver<RS,RM,CS,CM> {
  
  /**
   * Creates {@link RungeKutta4}.
   * @param sunit unit of scalar
   */
  public RungeKutta4(RS sunit) {
    super(sunit);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final RM step(final ExplicitDifferentialEquation<RS,RM,CS,CM> equation, final RS t0, final RM x0, final RS h) throws SolverStopException {
    final RM dx0 = equation.differentialEquation(t0, x0);

    setTrial(true);

    final RM x1 = x0.add(dx0.multiply(h.divide(2)));
    final RM dx1 = equation.differentialEquation(t0.add(h.divide(2)), x1);

    final RM x2 = x0.add(dx1.multiply(h.divide(2)));
    final RM dx2 = equation.differentialEquation(t0.add(h.divide(2)), x2);

    final RM x3 = x0.add(dx2.multiply(h));
    final RM dx3 = equation.differentialEquation(t0.add(h), x3);

    setTrial(false);

    return x0.add(dx0.add(dx3).add(dx2.add(dx1).multiply(2)).multiply(h.divide(6)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final RM step(final DifferentialDifferenceEquation<RS,RM,CS,CM> equation, final RS t0, final RM xc0, final RM xd0, final RS h) throws SolverStopException {
    final RM dxc0 = equation.differentialEquation(t0, xc0, xd0);

    setTrial(true);

    final RM xc1 = xc0.add(dxc0.multiply(h.divide(2)));
    final RM dxc1 = equation.differentialEquation(t0.add(h.divide(2)), xc1, xd0);

    final RM xc2 = xc0.add(dxc1.multiply(h.divide(2)));
    final RM dxc2 = equation.differentialEquation(t0.add(h.divide(2)), xc2, xd0);

    final RM xc3 = xc0.add(dxc2.multiply(h));
    final RM dxc3 = equation.differentialEquation(t0.add(h), xc3, xd0);

    setTrial(false);

    return xc0.add(dxc0.add(dxc3).add(dxc2.add(dxc1).multiply(2)).multiply(h.divide(6)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final RM step(final ExplicitDifferentialSystem<RS,RM,CS,CM> system, final RS t0, final RM x0, final RS h) throws SolverStopException {
    final RM io0 = system.inputOutputEquation(t0, x0);
    final RM dx0 = system.differentialEquation(t0, x0, io0);

    setTrial(true);

    final RM x1 = x0.add(dx0.multiply(h.divide(2)));
    final RM io1 = system.inputOutputEquation(t0.add(h.divide(2)), x1);
    final RM dx1 = system.differentialEquation(t0.add(h.divide(2)), x1, io1);

    final RM x2 = x0.add(dx1.multiply(h.divide(2)));
    final RM io2 = system.inputOutputEquation(t0.add(h.divide(2)), x2);
    final RM dx2 = system.differentialEquation(t0.add(h.divide(2)), x2, io2);

    final RM x3 = x0.add(dx2.multiply(h));
    final RM io3 = system.inputOutputEquation(t0.add(h), x3);
    final RM dx3 = system.differentialEquation(t0.add(h), x3, io3);

    setTrial(false);

    return x0.add(dx0.add(dx3).add(dx2.add(dx1).multiply(2)).multiply(h.divide(6)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final RM step(final DifferentialDifferenceSystem<RS,RM,CS,CM> system, final RS t0, final RM xc0, final RM xd0, final RS h) throws SolverStopException {
    final RM io0 = system.inputOutputEquation(t0, xc0, xd0);
    final RM dxc0 = system.differentialEquation(t0, xc0, xd0, io0);

    setTrial(true);

    final RM xc1 = xc0.add(dxc0.multiply(h.divide(2)));
    final RM io1 = system.inputOutputEquation(t0.add(h.divide(2)), xc1, xd0);
    final RM dxc1 = system.differentialEquation(t0.add(h.divide(2)), xc1, xd0, io1);

    final RM xc2 = xc0.add(dxc1.multiply(h.divide(2)));
    final RM io2 = system.inputOutputEquation(t0.add(h.divide(2)), xc2, xd0);
    final RM dxc2 = system.differentialEquation(t0.add(h.divide(2)), xc2, xd0, io2);

    final RM xc3 = xc0.add(dxc2.multiply(h));
    final RM io3 = system.inputOutputEquation(t0.add(h), xc3, xd0);
    final RM dxc3 = system.differentialEquation(t0.add(h), xc3, xd0, io3);

    setTrial(false);

    return xc0.add(dxc0.add(dxc3).add(dxc2.add(dxc1).multiply(2)).multiply(h.divide(6)));
  }
}