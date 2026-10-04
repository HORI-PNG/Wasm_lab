/*
 * $Id: DoubleRealFFT.java,v 1.1 2008/01/27 02:09:48 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.fft;

import org.mklab.nfc.matrix.DoubleMatrixUtil;

/**
 * 倍精度(double)型の1次元の実ベクトルに対して高速フーリエ変換と逆高速フーリエ変換を行うクラスです。
 * 
 * @author koga
 * @version $Revision: 1.1 $, 2004/06/17
 */
public final class DoubleRealFFTAnalyzer {
  /**
   * 新しく生成された<code>DoubleRealFFTAnalyzer</code>オブジェクトを初期化します。
   */
  private DoubleRealFFTAnalyzer() {
    // nothing to do
  }

  /**
   * 実ベクトルに対しsize点までの高速フーリエ変換を行います。
   * 
   * <p>ベクトルの長さがsizeより短いとき、ゼロが後ろに付け加えられ、 ベクトルの長さがsizeより長いとき、size 番目以降が切り捨てられます。
   * 
   * @param data 実ベクトル
   * @param size 変換対象のデータ数
   * @return フーリエ変換結果
   */
  public static double[][] fft(final double[] data, final int size) {
    return DoubleComplexFFTAnalyzer.fft(data, DoubleMatrixUtil.createZero(data.length), size);
  }

  /**
   * 実ベクトルに対しsize点までの逆高速フーリエ変換を行います。
   * 
   * <p>ベクトルの長さがsizeより短いとき、ゼロが後ろに付け加えられ、 ベクトルの長さがsizeより長いとき、size 番目以降が切り捨てられます。
   * 
   * @param data 実ベクトル
   * @param size 変換対象のデータ数
   * @return 逆フーリエ変換結果
   */
  public static double[][] ifft(final double[] data, final int size) {
    return DoubleComplexFFTAnalyzer.ifft(data, DoubleMatrixUtil.createZero(data.length), size);
  }
}