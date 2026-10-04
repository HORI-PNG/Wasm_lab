/*
 * Created on 2007/01/17
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc;

/**
 * ソルバーに関する例外を表すクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.2 $, 2007/01/17
 */
public class SolverException extends Exception {

  /** シリアルバージョン。 */
  private static final long serialVersionUID = 7188413310611141301L;

  /**
   * 新しく生成された<code>SolverException</code>オブジェクトを初期化します。
   */
  public SolverException() {
    // nothing
  }

  /**
   * 新しく生成された<code>SolverException</code>オブジェクトを初期化します。
   * 
   * @param message メッセージ
   */
  public SolverException(final String message) {
    super(message);
  }

  /**
   * 新しく生成された<code>SolverException</code>オブジェクトを初期化します。
   * 
   * @param cause 例外の原因
   */
  public SolverException(final Throwable cause) {
    super(cause);
  }

  /**
   * 新しく生成された<code>SolverException</code>オブジェクトを初期化します。
   * 
   * @param message メッセージ
   * @param cause 例外の原因
   */
  public SolverException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
