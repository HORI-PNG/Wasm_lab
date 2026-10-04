/**
 * $Id: EquationSolver.java,v 1.31 2008/06/30 14:55:19 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.ode;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mklab.nfc.SolverException;
import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * 方程式の解を求めるソルバーを表わす抽象クラスです。
 * 
 * @author koga
 * @version $Revision: 1.31 $
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS>  type of complex scalar
 * @param <CM> type of complex matrix
 */
public abstract class EquationSolver<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> {

  /** 方程式を解くことを停止させる場合true。 */
  private Map<Thread, Boolean> stoppingSolver = new HashMap<>();

  /** ソルバーを停止させた例外。 */
  private SolverException stoppingException;

  /** ソルバーを停止させた例外に関する情報を標準エラー出力へ出力するならばtrue。 */
  private boolean showStoppingMessage = false;

  /** 仮の値を引数として式の評価を呼び出した回数。 */
  private static int trialCount = 0;

  /** 時系列データの最大個数。 */
  private int maximumDataSize = 100000;
  /** 時系列を保存する最小時間間隔。 */
  //private RS minimumSavingInterval =  this.sunit.create(1.0E-3);
  private RS minimumSavingInterval =  this.sunit.create(10).power(3).inverse();
  /** 固定刻み幅(時間)。 */
  //private RS timeStep = this.sunit.create(4.0E-2);
  private RS timeStep = this.sunit.create(4).divide(100);
  /** サンプル点でデータを保存する場合true。 */
  private boolean saveAtSamplingPoint = false;
  /** 不連続点でデータを保存する場合true。 */
  private boolean saveAtDiscontinuousPoint = true;

  /** データを保存する時刻ならばtrue。 */
  private boolean atSavingPoint = false;

  /** データを保存する時刻の系列。 */
  private RM timeSeries;
  /** 連続時間システムの状態の時系列(微分方程式の解)。 */
  private RM differentialSolution;
  /** 離散時間システムの状態の時系列(差分方程式の解)。 */
  private RM differenceSolution;
  /** 入出力の時系列(代数方程式の解)。 */
  private RM algebraicSolution;
  /** 微分代数方程式の解。 */
  private RM differentialAlgebraicSolution;

  /** {@link EquationSolver}のオブザーバー 。*/
  private List<EquationSolverObserver<RS,RM,CS,CM>> solverObservers = new ArrayList<>();
  
  /** unit of scalar */
  protected RS sunit;
  
  /**
   * Creates {@link EquationSolver}.
   * @param sunit unit of scalar
   */
  public EquationSolver(RS sunit) {
    this.sunit = sunit;
  }

  /**
   * {@link EquationSolver}のオブザーバーをリストに登録します。
   * 
   * @param observer {@link EquationSolver}のオブザーバー
   */
  public final void registerObserver(final EquationSolverObserver<RS,RM,CS,CM> observer) {
    this.solverObservers.add(observer);
  }

  /**
   * {@link EquationSolver}のオブザーバーをリストから削除します。
   * 
   * @param observer {@link EquationSolver}のオブザーバー
   */
  public final void unregisterObserver(final EquationSolverObserver<RS,RM,CS,CM> observer) {
    if (this.solverObservers.contains(observer)) {
      this.solverObservers.remove(observer);
    }
  }

  /**
   * {@link EquationSolver}のオブザーバーに計算時間が進んだことを知らせます。
   * 
   * @param t 計算結果が確定した時間
   * @throws InterruptedException 計算の進行がキャンセルされた場合
   */
  protected final void notifyObservers(final RS t) throws InterruptedException {
    for (final EquationSolverObserver<RS,RM,CS,CM> observer : this.solverObservers) {
      observer.notify(t);
    }
  }

  /**
   * データを保存する時刻であるか判定します。
   * 
   * @return データを保存する時刻ならばtrue、そうでなければfalse
   */
  public final boolean isAtSavingPoint() {
    return this.atSavingPoint;
  }

  /**
   * データを保存する時刻であるか設定します。
   * 
   * @param atSavingPoint データを保存する時刻ならばtrue、そうでなければfalse
   */
  public final void setAtSavingPoint(final boolean atSavingPoint) {
    this.atSavingPoint = atSavingPoint;
  }

  /**
   * サンプル点でデータを保存することを指示します。
   * 
   * @param save サンプル点でデータを保存するならばtrue
   */
  public final void setSaveAtSamplingPoint(final boolean save) {
    this.saveAtSamplingPoint = save;
  }

  /**
   * サンプル点でデータを保存するか判定します。
   * 
   * @return サンプル点でデータを保存するならばtrue、そうでなければfalse
   */
  public final boolean isSaveAtSamplingPoint() {
    return this.saveAtSamplingPoint;
  }

  /**
   * 不連続点でデータを保存することを指示します。
   * 
   * @param save 不連続でデータを保存するならばtrue
   */
  public final void setSaveAtDiscontinuousPoint(final boolean save) {
    this.saveAtDiscontinuousPoint = save;
  }

  /**
   * 不連続点でデータを保存するか判定します。
   * 
   * @return 不連続点でデータを保存するならばtrue、そうでなければfalse
   */
  public final boolean isSaveAtDiscontinuousPoint() {
    return this.saveAtDiscontinuousPoint;
  }

  /**
   * 微分方程式を解くために、仮の値を引数としてdiffEqsやioEqsを呼び出し中なら真を返します。
   * 
   * @return 仮の値を引数とする呼び出し中の真偽
   */
  public static boolean isTrial() {
    if (trialCount > 0) {
      return true;
    }
    return false;

  }

  /**
   * 仮の値を引数として方程式を呼び出し中であることを設定します。
   * 
   * @param trial 仮の値を引数とする呼び出し中ならばtrue、そうでなければfalse
   */
  public static void setTrial(final boolean trial) {
    if (trial) {
      trialCount += 1;
    } else {
      trialCount -= 1;
    }

  }
  
  /**
   * 仮の値を引数として方程式を呼び出した回数をリセットします。
   */
  public static void resetTrial() {
    trialCount = 0;
  }

  /**
   * シミュレーション計算を停止します。
   */
  public final void stop() {
    stop(new SolverStopException());
  }

  /**
   * シミュレーション計算を停止します。
   * 
   * @param e ソルバーを停止させた例外
   */
  public final void stop(final SolverException e) {
    this.stoppingSolver.put(Thread.currentThread(), Boolean.TRUE);
    this.stoppingException = e;
    
    resetTrial();

    if (this.showStoppingMessage) {
      final StringWriter out = new StringWriter();
      
      try (final PrintWriter writer = new PrintWriter(out)) {
        e.printStackTrace(writer);
      }

      final String lineSeparator = System.getProperty("line.separator"); //$NON-NLS-1$
      warning(e.getMessage() + lineSeparator + out.toString());
    }
  }

  
  /**
   * showStoppingMessageを設定します。
   * @param showStoppingMessage ソルバーを停止させた例外に関する情報を標準エラー出力へ出力するならばtrue
   */
  public void setShowStoppingMessage(boolean showStoppingMessage) {
    this.showStoppingMessage = showStoppingMessage;
  }

  /**
   * シミュレーション計算を停止する命令を解除します。
   */
  public final void resetStopper() {
    this.stoppingSolver.put(Thread.currentThread(), Boolean.FALSE);
    this.stoppingException = null;
  }

  /**
   * シミュレーション計算を停止する途中であるか判定します。
   * 
   * @return シミュレーション計算を停止する途中であればtrue、そうでなければfalse
   */
  public final boolean isStopping() {
    return this.stoppingSolver.get(Thread.currentThread()).booleanValue();
  }

  /**
   * ソルバーを停止させた例外を返します。
   * 
   * @return ソルバーを停止させた例外
   */
  public final SolverException getStoppingException() {
    return this.stoppingException;
  }

  /**
   * 警告を出力します。
   * 
   * @param message メッセージ
   */
  public final void warning(final String message) {
    System.err.println(message);
  }

  /**
   * 固定刻み幅(時間)を設定します。
   * 
   * @param timeStep 固定刻み幅(時間)
   */
  public final void setTimeStep(final RS timeStep) {
    this.timeStep = timeStep;
    setMinimumSavingInterval(timeStep);
  }

  /**
   * 時系列を保存する最小時間間隔を設定します。
   * 
   * @param interval 時系列を保存する最小時間間隔
   */
  public final void setMinimumSavingInterval(final RS interval) {
    this.minimumSavingInterval = interval;
  }

  /**
   * 時系列を保存する最小時間間隔を返します。
   * 
   * @return 時系列を保存する最小時間間隔
   */
  public final RS getMinimumSavingInterval() {
    return this.minimumSavingInterval;
  }

  /**
   * 固定刻み幅(時間)を返します。
   * 
   * @return 固定刻み幅(時間)
   */
  public final RS getTimeStep() {
    return this.timeStep;
  }

  /**
   * データの保存時刻の時系列を返します。
   * 
   * @return データの保存時刻の時系列
   */
  public final RM getTimeSeries() {
    return this.timeSeries;
  }

  /**
   * データの保存時刻の時系列を設定します。
   * 
   * @param timeSeries データの保存時刻の時系列
   */
  protected final void setTimeSeries(final RM timeSeries) {
    this.timeSeries = timeSeries;
  }

  /**
   * 連続時間システムの状態(微分方程式の解)の時系列を返します。
   * 
   * @return 連続時間システムの状態(微分方程式の解)の時系列
   */
  public final RM getContinuousStateSeries() {
    return this.differentialSolution;
  }

  /**
   * 離散時間システムの状態(差分方程式の解)の時系列を返します。
   * 
   * @return 離散時間システムの状態(差分方程式の解)の時系列
   */
  public final RM getDiscreteStateSeries() {
    return this.differenceSolution;
  }

  /**
   * 入出力の時系列を返します。
   * 
   * @return 入出力の時系列
   */
  public final RM getInputOutputSeries() {
    return this.algebraicSolution;
  }

  /**
   * 出力の時系列を返します。
   * 
   * @return 出力の時系列
   */
  public final RM getOutputSeries() {
    return this.algebraicSolution;
  }

  /**
   * 微分方程式の解を返します。
   * 
   * @return 微分方程式の解
   */
  public final RM getDifferentialSolution() {
    return this.differentialSolution;
  }

  /**
   * 微分方程式の解を設定します。
   * 
   * @param differentialSolution 微分方程式の解
   */
  protected final void setDifferentialSolution(final RM differentialSolution) {
    this.differentialSolution = differentialSolution;
  }

  /**
   * 差分方程式の解を返します。
   * 
   * @return 差分方程式の解
   */
  public final RM getDifferenceSolution() {
    return this.differenceSolution;
  }

  /**
   * 差分方程式の解を返します。
   * 
   * @param differneceSolution 差分方程式の解
   */
  protected final void setDifferenceSolution(final RM differneceSolution) {
    this.differenceSolution = differneceSolution;
  }

  /**
   * 代数方程式の解を返します。
   * 
   * @return 代数方程式の解
   */
  public final RM getAlgebraicSolution() {
    return this.algebraicSolution;
  }

  /**
   * 代数方程式の解を設定します。
   * 
   * @param algebraicSolution 代数方程式の解
   */
  protected final void setAlgebraicSolution(final RM algebraicSolution) {
    this.algebraicSolution = algebraicSolution;
  }

  /**
   * 微分代数方程式の解を返します。
   * 
   * @return 微分代数方程式の解
   */
  public final RM getDifferentialAlgebraicSolution() {
    return this.differentialAlgebraicSolution;
  }

  /**
   * 微分代数方程式の解を設定します。
   * 
   * @param differentialAlgebraicSolution 微分代数方程式の解
   */
  protected final void setDifferentialAlgebraicSolution(final RM differentialAlgebraicSolution) {
    this.differentialAlgebraicSolution = differentialAlgebraicSolution;
  }

  /**
   * 時系列データの最大個数を返します。
   * 
   * @return 時系列データの最大個数
   */
  public final int getMaximumDataSize() {
    return this.maximumDataSize;
  }
  
  /**
   * 時系列データの最大個数を設定します。
   * @param maximumDataSize 時系列データの最大個数
   */
  public final void setMaximumDataSize(final int maximumDataSize) {
    this.maximumDataSize = maximumDataSize;
  }
}
