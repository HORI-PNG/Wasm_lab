/*
 * Created on 2007/11/05
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.ode;

/**
 * 仮の値で評価を行うことができることを表すインターフェースです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.1 $, 2007/11/05
 */
public interface TriallyEvaluable {

  /**
   * 仮の値を引数として評価をしている最中であるか判定します。
   * 
   * @return 仮の値を引数として評価をしている最中ならばtrue、そうでなければfalse
   */
  boolean isTrial();

  /**
   * 仮の値を引数として評価中であることを設定します。
   * 
   * @param trial 仮の値を引数とする評価中ならばtrue、そうでなければfalse
   */
  void setTrial(final boolean trial);
}
