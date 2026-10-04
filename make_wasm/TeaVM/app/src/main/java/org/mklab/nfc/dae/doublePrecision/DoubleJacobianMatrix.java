package org.mklab.nfc.dae.doublePrecision;

import org.mklab.nfc.matrix.DoubleMatrix;


/**
 * ヤコビ行列を表すインターフェースです。
 * 
 * @author kageyama
 * @version $Revision$, 2011/11/05
 */
public interface DoubleJacobianMatrix {

  /**
   * 微分代数方程式Mx'=f(t,x)におけるfのヤコビ行列を返すメソッドです。
   * 
   * @param x 現在の状態
   * @return ヤコビ行列
   */
  DoubleMatrix getJacobianMatrix(DoubleMatrix x);

  //  /**
  //   * ヤコビ行列を解析的に解くか数値的に解くかの判断を行うメソッドです。
  //   * <p>
  //   * 解析的に解く(手計算でfをxで偏微分する)場合はtrue、 数値的に解く(ソルバーの内部アルゴリズムでfをxで偏微分する)場合はfalseを返す。
  //   * 数値的に解く場合、getJacobianMatrixメソッドはダミーメソッドとして扱う。
  //   * 
  //   * @return 解析的に解く場合true、数値的に解く場合false
  //   */
  //  boolean isAnalyticalJacobianMatrix();

  /**
   * ヤコビ行列の上部バンド幅の値を返すメソッドです。
   * 
   * <p>
   * ヤコビ行列が密行列の場合、ダミーメソッドとして扱います。 ヤコビ行列が帯行列の場合、上部バンド幅を返します。
   * </p>
   * 
   * @return ヤコビ行列の上部バンド幅
   */
  int getUpperBandWidthOfJacobianMatrix();

  /**
   * ヤコビ行列の下部バンド幅の値を返すメソッドです。 
   * 
   * <p>
   * ヤコビ行列が密行列の場合、方程式の数を返します。 ヤコビ行列が帯行列の場合、下部バンド幅を返します。
   * </p>
   * 
   * @return ヤコビ行列の下部バンド幅
   */
  int getLowerBandWidthOfJacobianMatrix();

}