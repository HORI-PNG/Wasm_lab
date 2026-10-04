package org.mklab.nfc.dae.doublePrecision;

import org.mklab.nfc.matrix.DoubleMatrix;


/**
 * 時変線形陰的微分代数方程式式M(t,x)x'=f(t,x)を表すインターフェースです。
 * 
 * @author kageyama
 * @version $Revision$, 2012/02/10
 */
public interface DoubleTimeVaryingLinearlyImplicitDifferentialAlgebraicEquation {
  
  /**
   * 時変線形陰的微分代数方程式M(t,x)x'=f(t,x)における係数行列Mを返すメソッドです。
   * 
   * @return 係数行列
   */
  DoubleMatrix getM();

  /**
   * 時刻tと状態xから微分代数方程式式M(t,x)x'=f(t,x)における右辺を返すメソッドです。
   * 
   * @param t 現在の時刻
   * @param x 現在の状態
   * @return 微分代数方程式の右辺値
   */
  DoubleMatrix getF(double t, DoubleMatrix x);
}
