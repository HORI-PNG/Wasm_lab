package org.mklab.nfc.dae;

import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * 時変線形陰的微分代数方程式M(t,x)x'=f(t,x)を表現するインターフェースです。 
 * 
 * @author kageyama
 * @version $Revision$, 2011/09/12
 * 
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS> type of complex scalar
 * @param <CM> type of complex matrix
 */
public interface TimeVaryingImplicitDifferentialAlgebraicEquation<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> extends BandMatrixCoefficient {

  /**
   * 時変線形陰的微分代数方程式M(t,x)x'=f(t,x)における係数行列Mを返すメソッドです。
   * 
   * @return 係数行列
   */
  RM getM();

  /**
   * 時刻tと状態xから微分代数方程式M(t,x)x'=f(t,x)における右辺を返すメソッドです。
   * 
   * @param t 現在の時刻
   * @param x 現在の状態
   * @return 微分代数方程式の右辺値
   */
  RM getf(RS t, RM x);

}
