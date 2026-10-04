/*
 * $Id: NewtonRaphsonFixedPointSolver.java,v 1.5 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.nleq;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.ode.SolverStopException;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * ニュートン・ラフソン法で「x = f(x)」を満たす不動点(連立非線形方程式の解)を求めるクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.5 $, 2004/11/10
 * 
 * @param <M> 行列の型
 * @param <S> 成分の型
 */
public class NewtonRaphsonFixedPointSolver<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> extends NewtonRaphsonSolver<S,M> {

  /**
   * 連立方程式のそれぞれの残差の絶対値が許容誤差(<code>toleranceOfFunction</code>)より小さい、または、 それぞれの解の変化量の絶対値が許容誤差(<code>toleranceOfSolution</code>)より小さければ、収束したと判定し、 解を返します。
   * 
   * もし、最大繰り返し回(<code>matxTrial</code>)で解が収束しなければ、警告を表示し、 その時の値を返します。
   * 
   * {@inheritDoc}
   */
  @Override
  public final M solve(final NonLinearFunction<S,M> function, final M initialValue) throws NotConvergedException, SolverStopException {
    setupParameters(initialValue);
    S sunit = initialValue.getElement(1,1).createUnit();
    final JacobianSolver<S,M> jacobianSolver = new JacobianSolver<>(sunit);
    jacobianSolver.setDeltaRate(getDeltaJacobian());
    final S scalingFactorOfTolerance = sunit.create(10);
    
    M x = initialValue.createClone();
    M dx = x.createZero();

    S localToleranceOfFunction = getToleranceOfFunction();
    S localToleranceOfSolution = getToleranceOfSolution();
    
    final long seed = 32198541;

    do {
      for (int i = 0; i < getMaxTrial(); i++) {
        if (isTracable()) {
          x.print("x"); //$NON-NLS-1$
        }

        setTrial(true);
        final M f = function.eval(x);
        setTrial(false);

        if (x.subtract(f).absElementWise().compareElementWise(".<", max(1, x.frobNorm()).multiply(localToleranceOfFunction)).allTrue()) { //$NON-NLS-1$
          return x;
        }

        setTrial(true);
        final M J = jacobianSolver.getJacobianAt(function, x);
        setTrial(false);

        final M IJ = J.createUnit().subtract(J);

        if (isUsingPerturbation() &&  IJ.isFullRank(max(1, IJ.frobNorm()).multiply(getToleranceOfJacobian())) == false) {
          x = x.add(x.createUniformRandom(x.length(), 1, seed).multiply(getDeltaSolution()));
          continue;
          // throw new IllPosedException("(I - Jacobian) is singular");
        }

        final M newX;
        if (isUsingPseudoInverse()) {
          newX = IJ.pseudoInverse().multiply(f.subtract(J.multiply(x)));
          dx = newX.subtract(x);
        } else {
          newX = IJ.leftDivide(f.subtract(J.multiply(x)));
          dx = newX.subtract(x);
        }
        
        if (dx.absElementWise().compareElementWise(".<", max(1, x.frobNorm()).multiply(localToleranceOfSolution)).allTrue()) { //$NON-NLS-1$
          return newX;
        }

        x = newX;
      }

      localToleranceOfFunction = localToleranceOfFunction.multiply(scalingFactorOfTolerance);
      localToleranceOfSolution = localToleranceOfSolution.multiply(scalingFactorOfTolerance);

      warning(Messages.getString("NewtonRaphsonFixedPointSolver.0") + localToleranceOfFunction); //$NON-NLS-1$
      warning(Messages.getString("NewtonRaphsonFixedPointSolver.1") + localToleranceOfSolution); //$NON-NLS-1$

    } while (localToleranceOfFunction.isLessThan(1) && localToleranceOfSolution.isLessThan(1));

    final S maximumResidue = dx.absElementWise().max();
    throw new NotConvergedException(Messages.getString("NewtonRaphsonFixedPointSolver.2") + maximumResidue); //$NON-NLS-1$
  }

  /**
   * 大きい値を返します。
   * 
   * @param value1 値1
   * @param value2 値2
   * @return 大きい値
   */
  private S max(final int value1, final S value2) {
    if (value2.isGreaterThan(value1)) {
      return value2;
    }

    return value2.create(value1);
  }
}