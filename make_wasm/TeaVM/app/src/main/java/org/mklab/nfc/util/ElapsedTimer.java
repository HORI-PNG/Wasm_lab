/*
 * $Id: ActualStopwatch.java,v 1.11 2006/08/25 04:19:38 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.util;

/**
 * 時間を測定するためのクラスです。
 * 
 * @author koga
 * @version $Revision: 1.11 $
 */
public class ElapsedTimer {

  /** 開始時間。 */
  private long startTime = -1;

  /** 終了時間。 */
  private long stopTime = -1;

  /** 動作中ならtrue、そうでなければfalse。 */
  private boolean running = false;

  /**
   * CPU時間の測定を開始します。
   */
  public final void start() {
    this.startTime = System.currentTimeMillis();
    this.running = true;
  }

  /**
   * 時間の測定を終了します。 経過時間は{#getElapsedTime}で得られます。
   */
  public final void stop() {
    this.stopTime = System.currentTimeMillis();
    this.running = false;
  }

  /**
   * 経過時間[ms]を返します。
   * 
   * @return 経過時間[ms]
   */
  public final double getElapsedTime() {
    if (this.startTime == -1) {
      return 0;
    }

    if (this.running) {
      return (System.currentTimeMillis() - this.startTime) / 1000.0;
    }
    return (this.stopTime - this.startTime) / 1000.0;
  }

  /**
   * このオブジェクトをリセットします。
   * 
   * <p>経過時間等の情報は破棄されます。
   */
  public final void reset() {
    this.startTime = -1;
    this.stopTime = -1;
    this.running = false;
  }
}