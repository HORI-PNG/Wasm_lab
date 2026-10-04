/*
 * Created on 2007/05/28
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.ode;

import org.mklab.nfc.SolverException;


/**
 * データ系列の個数が最大数を超えたことを表す例外クラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.1 $, 2007/05/28
 */
public class SolverTooManyDataException extends SolverException {

  /** シリアル番号。 */
  private static final long serialVersionUID = 5107518201934631603L;

  /**
   * 新しく生成された<code>SolverDataSizeOutOfBoundsException</code>オブジェクトを初期化します。
   */
  public SolverTooManyDataException() {
    // TODO Auto-generated constructor stub
  }

  /**
   * 新しく生成された<code>SolverDataSizeOutOfBoundsException</code>オブジェクトを初期化します。
   * 
   * @param message メッセージ
   */
  public SolverTooManyDataException(final String message) {
    super(message);
    // TODO Auto-generated constructor stub
  }

  /**
   * 新しく生成された<code>SolverDataSizeOutOfBoundsException</code>オブジェクトを初期化します。
   * 
   * @param cause 例外の原因
   */
  public SolverTooManyDataException(final Throwable cause) {
    super(cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * 新しく生成された<code>SolverDataSizeOutOfBoundsException</code>オブジェクトを初期化します。
   * 
   * @param message メッセージ
   * @param cause 例外の原因
   */
  public SolverTooManyDataException(final String message, final Throwable cause) {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

}
