/*
 * $Id: SolverInterruptedException.java,v 1.1 2008/05/31 14:04:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.ode;

import org.mklab.nfc.SolverException;


/**
 * 方程式のソルバーへの割り込みを表す例外クラスです。
 * 
 * @author koga
 * @version $Revision: 1.1 $
 */
public class SolverInterruptedException extends SolverException {

  /** シリアル番号。 */
  private static final long serialVersionUID = 709255231407047825L;

  /**
   * 新しく生成された<code>EquationInterruptedException</code>オブジェクトを初期化します。
   */
  public SolverInterruptedException() {
    super();
  }

  /**
   * 新しく生成された<code>EquationInterruptedException</code>オブジェクトを初期化します。
   * 
   * @param message メッセージ
   */
  public SolverInterruptedException(final String message) {
    super(message);
  }

  /**
   * 新しく生成された<code>EquationInterruptedException</code>オブジェクトを初期化します。
   * 
   * @param cause 例外の原因
   */
  public SolverInterruptedException(final Throwable cause) {
    super(cause);
  }

  /**
   * 新しく生成された<code>EquationInterruptedException</code>オブジェクトを初期化します。
   * 
   * @param message メッセージ
   * @param cause 例外の原因
   */
  public SolverInterruptedException(final String message, final Throwable cause) {
    super(message, cause);
  }
}