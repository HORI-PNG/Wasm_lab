/*
 * $Id: EndianTransformer.java,v 1.4 2006/08/25 00:43:40 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.util;

/**
 * エンディアンの変換を行うクラスです。
 * 
 * @author koga
 * @version $Revision: 1.4 $
 */
public final class EndianTransformer {
  /**
   * 新しく生成された<code>EndianTransformer</code>オブジェクトを初期化します。
   */
  private EndianTransformer() {
    // nothing do do
  }

  /**
   * ビッグエンディアンとリトルエンディアンを相互変換します。
   * 
   * @param data 変換するデータ
   * @return 変換結果
   */
  public static double flip(final double data) {
    final long a = Double.doubleToLongBits(data);
    final long b = flip(a);
    return Double.longBitsToDouble(b);
  }

  /**
   * ビッグエンディアンとリトルエンディアンを相互変換します。
   * 
   * @param data 変換するデータ
   * @return 変換結果
   */
  public static float flip(final float data) {
    final int a = Float.floatToIntBits(data);
    final int b = flip(a);
    return Float.intBitsToFloat(b);
  }

  /**
   * ビッグエンディアンとリトルエンディアンを相互変換します。
   * 
   * @param data 変換するデータ
   * @return 変換結果
   */
  public static long flip(final long data) {
    long b = 0;
    for (int i = 0; i < 8; i++) {
      b |= ((data >>> 8 * i) & 0xFF) << 8 * (7 - i);
    }

    return b;
  }

  /**
   * ビッグエンディアンとリトルエンディアンを相互変換します。
   * 
   * @param data 変換するデータ
   * @return 変換結果
   */
  public static int flip(final int data) {
    int b = 0;
    for (int i = 0; i < 4; i++) {
      b |= ((data >>> 8 * i) & 0xFF) << 8 * (3 - i);
    }

    return b;
  }
}