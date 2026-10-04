/*
 * $Id: SolverStopException.java,v 1.1 2007/05/28 03:59:43 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.ode;

import org.mklab.nfc.SolverException;


/**
 * 方程式のソルバーを停止するための例外クラスです。
 * 
 * @author koga
 * @version $Revision: 1.1 $
 */
public class SolverStopException extends SolverException {

  /** シリアルバージョン。 */
  private static final long serialVersionUID = 2131557221429291468L;

  /**
   * 新しく生成された<code>EquationStopException</code>オブジェクトを初期化します。
   */
  public SolverStopException() {
    super();
  }

  /**
   * 新しく生成された<code>EquationStopException</code>オブジェクトを初期化します。
   * 
   * @param message メッセージ
   */
  public SolverStopException(final String message) {
    super(message);
  }

  /**
   * 新しく生成された<code>EquationStopException</code>オブジェクトを初期化します。
   * 
   * @param cause 例外の原因
   */
  public SolverStopException(final Throwable cause) {
    super(cause);
  }

  /**
   * 新しく生成された<code>EquationStopException</code>オブジェクトを初期化します。
   * 
   * @param message メッセージ
   * @param cause 例外の原因
   */
  public SolverStopException(final String message, final Throwable cause) {
    super(message, cause);
  }
}