/*
 * Created on 2007/01/17
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.nleq;

import org.mklab.nfc.SolverException;


/**
 * 解が収束しない場合に発生する例外を表すクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.1 $, 2007/01/17
 */
public class NotConvergedException extends SolverException {

  /** シリアル番号。 */
  private static final long serialVersionUID = -6768869273584511929L;

  /**
   * 新しく生成された<code>NotConveredException</code>オブジェクトを初期化します。
   */
  public NotConvergedException() {
    // nothing
  }

  /**
   * 新しく生成された<code>NotConveredException</code>オブジェクトを初期化します。
   * 
   * @param message メッセージ
   */
  public NotConvergedException(final String message) {
    super(message);
  }

  /**
   * 新しく生成された<code>NotConveredException</code>オブジェクトを初期化します。
   * 
   * @param cause 例外の原因
   */
  public NotConvergedException(final Throwable cause) {
    super(cause);
  }

  /**
   * 新しく生成された<code>NotConveredException</code>オブジェクトを初期化します。
   * 
   * @param message メッセージ
   * @param cause 例外の原因
   */
  public NotConvergedException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
