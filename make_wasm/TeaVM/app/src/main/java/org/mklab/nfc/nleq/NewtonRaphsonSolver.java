/*
 * Created on 2008/03/11
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.nleq;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * ニュートン・ラフソン法で連立非線形方程式の解を求める抽象クラスです。
 * 
 * @author koga
 * @version $Revision: 1.3 $, 2008/03/11
 * 
 * @param <M> 行列の型
 * @param <S> 成分の型
 */
public abstract class NewtonRaphsonSolver<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> extends NonLinearEquationSolver<S,M> {
  

  /** 数値微分を求める際に利用する微少変化量を求めるための変化率。 */
  private S deltaJacobian;
  /** ヤコビ行列が非正則な場合に用いる解の修正量。 */
  private S deltaSolution;
  /** 残差の許容誤差。 */
  private S toleranceOfFunction;
  /** 解の許容誤差。 */
  private S toleranceOfSolution;
  /** ヤコビ行列の正則性に関する許容誤差。 */
  private S toleranceOfJacobian;
  /** 繰り返しの最大回数。 */
  private int maxTrial = 1000;
  /** 繰り返しの途中経過を表示するならばtrue。 */
  private boolean tracable = false;
  
  /** true if pseudo inverse is used */
  private boolean usingPseudoInverse = false;
  
  /** true if perturbation is used */
  private boolean usingPerturbation = true;

  /** パラメータを設定するならばtrue、そうでなければfalse。 */
  private boolean settingParameters = true;

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
   * 数値微分を求める際に利用する微少変化量を求めるための変化率を返します。
   * 
   * @return 数値微分を求める際に利用する微少変化量を求めるための変化率
   */
  public final S getDeltaJacobian() {
    return this.deltaJacobian.clone();
  }

  /**
   * 数値微分を求める際に利用する微少変化量を求めるための変化率を設定します。
   * 
   * @param deltaJacobian 数値微分を求める際に利用する微少変化量を求めるための変化率
   */
  public final void setDeltaJacobian(final S deltaJacobian) {
    this.deltaJacobian = deltaJacobian.clone();
  }

  /**
   * ヤコビ行列が非正則な場合に用いる解の修正量を返します。
   * 
   * @return ヤコビ行列が非正則な場合に用いる解の修正量
   */
  public final S getDeltaSolution() {
    return this.deltaSolution.clone();
  }

  /**
   * 連立方程式のそれぞれの残差の絶対値の許容誤差(収束判定に使われる)を設定します。
   * 
   * @param toleranceOfFunction 許容誤差
   */
  public final void setToleranceOfFunction(final S toleranceOfFunction) {
    this.toleranceOfFunction = toleranceOfFunction.clone();
  }

  /**
   * 連立方程式のそれぞれの残差の絶対値の許容誤差(収束判定に使われる)を返します。
   * 
   * @return 許容誤差
   */
  public final S getToleranceOfFunction() {
    return this.toleranceOfFunction.clone();
  }

  /**
   * 連立方程式のそれぞれの解の変化量の絶対値の許容誤差(収束判定に使われる)を設定します。
   * 
   * @param toleranceOfSolution 許容誤差
   */
  public final void setToleranceOfSolution(final S toleranceOfSolution) {
    this.toleranceOfSolution = toleranceOfSolution.clone();
  }

  /**
   * 連立方程式のそれぞれの解の変化量の絶対値の許容誤差(収束判定に使われる)を返します。
   * 
   * @return 許容誤差
   */
  public final S getToleranceOfSolution() {
    return this.toleranceOfSolution.clone();
  }

  /**
   * ヤコビ行列の正則性に関する許容誤差を返します。
   * 
   * @return ヤコビ行列の正則性に関する許容誤差
   */
  public final S getToleranceOfJacobian() {
    return this.toleranceOfJacobian.clone();
  }

  /**
   * ヤコビ行列の正則性に関する許容誤差を設定します。
   * 
   * @param toleranceOfJacobian ヤコビ行列の正則性に関する許容誤差
   */
  public final void setToleranceOfJacobian(final S toleranceOfJacobian) {
    this.toleranceOfJacobian = toleranceOfJacobian.clone();
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
   * 繰り返しの途中で解を表示するかを設定します。
   * 
   * @param tracable trueならば、繰り返しの途中で解を表示します。デフォルトは、falseです。
   */
  public final void setTracable(final boolean tracable) {
    this.tracable = tracable;
  }

  /**
   * 繰り返しの途中で解を表示するか判定します。
   * 
   * @return 繰り返しの途中での解の表示するならばtrue、そうでなければfalse
   */
  public final boolean isTracable() {
    return this.tracable;
  }
  
  /**
   * Sets true if pseudo inverse is used. 
   * 
   * @param usingPseudoInverse true if pseudo inverse is used
   */
  public final void setUsingPseudoInverse(boolean usingPseudoInverse) {
    this.usingPseudoInverse = usingPseudoInverse;
  }
  
  /**
   * Returns true if pseudo inverse is used.
   * 
   * @return true if pseudo inverse is used
   */
  public final boolean isUsingPseudoInverse() {
    return this.usingPseudoInverse;
  }
  
  /**
   * Sets true if perturbation is used 
   * 
   * @param usingPerturbation true if perturbation is used.
   */
  public void setUsingPerturbation(boolean usingPerturbation) {
    this.usingPerturbation = usingPerturbation;
  }
  
  /**
   * Returns true if perturbation is used.
   * 
   * @return true if perturbation is used
   */
  public boolean isUsingPerturbation() {
    return this.usingPerturbation;
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

    this.deltaJacobian = unit.multiply(10).power(-10);
    this.deltaSolution = unit.multiply(10).power(-6);
    this.toleranceOfFunction = unit.getMachineEpsilon().multiply(100);
    this.toleranceOfSolution = unit.getMachineEpsilon().multiply(100);
    this.toleranceOfJacobian = unit.getMachineEpsilon();
  }
}
