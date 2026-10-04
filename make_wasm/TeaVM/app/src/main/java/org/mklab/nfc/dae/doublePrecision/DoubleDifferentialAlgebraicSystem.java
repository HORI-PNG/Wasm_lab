package org.mklab.nfc.dae.doublePrecision;

import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.ode.ExplicitDifferentialSystem;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * 線形陰的微分代数方程式Mx'=f(t,x,u)で表現されるシステムを表すインターフェースです。
 * 
 * @author kageyama
 * @version $Revision$, 2011/09/07
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 */
public interface DoubleDifferentialAlgebraicSystem<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> extends ExplicitDifferentialSystem<RS,RM,CS,CM> {
  // nothing to do
}
