package org.mklab.nfc.dae.doublePrecision;

import org.mklab.nfc.dae.BandMatrixCoefficient;
import org.mklab.nfc.matrix.DoubleMatrix;


/**
 * 線形陰的微分代数方程式Mx'=f(t,x)を表すインターフェースです。 
 * 
 * @author kageyama
 * @version $Revision$, 2011/09/12
 */
public interface DoubleDifferentialAlgebraicEquation extends BandMatrixCoefficient {

  /**
   * 線形陰的微分代数方程式Mx'=f(t,x)の係数行列Mを返します。
   * 
   * @return 係数行列
   */
  DoubleMatrix getM();

  /**
   * 時刻tと状態xから微分代数方程式Mx'=f(t,x)の右辺の計算結果を返します。
   * 
   * @param t 現在の時刻
   * @param x 現在の状態
   * @return 微分代数方程式の右辺値
   */
  DoubleMatrix getF(double t, DoubleMatrix x);
}
