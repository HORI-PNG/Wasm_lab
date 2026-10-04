/*
 * $Id: JacobianSolver.java,v 1.3 2008/05/27 15:03:53 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.nleq;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.ode.SolverStopException;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * ヤコビ行列(Jacobian)を計算するクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.3 $, 2004/11/12
 * @param <M> 行列の型 
 * @param <S> 成分の型
 */
public class JacobianSolver<S extends NumericalScalar<S,M>,M extends NumericalMatrix<S,M>> {
  /** 繰り返しの最大回数。 */
  private int maxTrial = 10;
  
  /** 数値微分を求める際に利用する微少変化量を求めるための変化率。 */
  private S deltaRate;

  /** 繰り返しにおける倍率 */
  private S scalingFactor;

  /** パラメータを設定するならばtrue、そうでなければfalse。 */
  private boolean settingParameters = true;
  
  /** 引数の最小の変化量 */
  private S minimumDx;

  /** 関数の最小の変化量 */
  private S minimumDf;
  
  /**
   * Creates {@link JacobianSolver}.
   * @param sunit unit of scalar
   */
  public JacobianSolver(S sunit) {
    this.scalingFactor = sunit.create(10);
  }

  /**
   * パラメータを設定するか判定します。
   * 
   * @return パラメータを設定するならばtrue、そうでなければfalse
   */
  private boolean isSettingParamters() {
    return this.settingParameters;
  }

  /**
   * パラメータを設定するか設定します。
   * 
   * @param settingParameters パラメータを設定するならばtrue、そうでなければfalse
   */
  private void setSettingParameters(final boolean settingParameters) {
    this.settingParameters = settingParameters;
  }
  
  /**
   * 数値微分を求める際に利用する微少変化量を求めるための変化率を設定します。
   * 
   * @param deltaRate 微少変化量を求めるための変化率
   */
  public final void setDeltaRate(final S deltaRate) {
    this.deltaRate = deltaRate.clone();
  }

  /**
   * 数値微分を求める際に利用する微少変化量を求めるための変化率を返します。
   * 
   * @return 微少変化量を求めるための変化率
   */
  public final S getDeltaRate() {
    return this.deltaRate.clone();
  }

  /**
   * ヤコビ行列を返します。
   * @param function ヤコビ行列を計算したい連立非線形方程式
   * @param x0 ヤコビ行列を求める点
   * @return ヤコビ行列
   * @exception SolverStopException ソルバーが停止された場合
   */
  public final M getJacobianAt(final NonLinearFunction<S,M> function, final M x0) throws SolverStopException {
    setupParameters(x0);
    
    final int xSize = x0.getRowSize();
    final M f0 = function.eval(x0);
    final M jacobian = x0.createZero(f0.getRowSize(), xSize);

    final M fn = function.eval(x0.multiply(Math.pow(10, this.maxTrial)));

    if (f0.equals(fn)) {
      return jacobian;
    }

    for (int i = 1; i <= xSize; i++) {
      final M x = x0.createClone();
      final S xi = x.getElement(i);
      S dx = xi.abs().multiply(this.deltaRate);
      if (dx.isLessThan(this.minimumDx)) {
        dx = this.minimumDx;
      }

      M df = null;
      for (int j = 0; j < this.maxTrial; j++) {
        x.setElement(i, xi.add(dx));
        final M fx = function.eval(x);
        df = fx.subtract(f0).divide(dx);

        if (df.absElementWise().compareElementWise(".>", this.minimumDf).anyTrue()) { //$NON-NLS-1$
          break;
        }

        if (j >= this.maxTrial - 1) {
          break;
        }

        dx = dx.multiply(this.scalingFactor);
      }

      jacobian.setColumnVector(i, df);
    }

    return jacobian;
  }
  
  /**
   * 収束計算の繰り返しの最大数を設定します。
   * 
   * @param maxTrial 繰り返しの最大数
   */
  public final void setMaxTrial(final int maxTrial) {
    this.maxTrial = maxTrial;
  }

  /**
   * 収束計算の繰り返しの最大数を返します。
   * 
   * @return 収束計算の繰り返しの最大数
   */
  public final int getMaxTrial() {
    return this.maxTrial;
  }
  
  /**
   * 繰り返しにおける倍率を設定します。
   * 
   * @param scalingFactor 繰り返しにおける倍率
   */
  public final void setScalingFactor(final S scalingFactor) {
    this.scalingFactor = scalingFactor;
  }

  /**
   * 繰り返しにおける倍率を返します。
   * 
   * @return 繰り返しにおける倍率
   */
  public final S getScalingFactor() {
    return this.scalingFactor;
  }
  
  /**
   * パラメータを設定します。
   * 
   * @param value 参照とする値
   */
  public final void setupParameters(final M value) {
    if (isSettingParamters() == false) {
      return;
    }
    setSettingParameters(false);

    final S unit = value.getElement(1, 1).createUnit();

    this.minimumDx = unit.getMachineEpsilon();
    this.minimumDf = unit.getMachineEpsilon();
    this.deltaRate = unit.multiply(10).power(-10);
  }
}