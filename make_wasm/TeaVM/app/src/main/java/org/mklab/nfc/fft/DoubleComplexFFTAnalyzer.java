/*
 * $Id: DoubleComplexFFT.java,v 1.1 2008/01/27 02:09:48 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.fft;

/**
 * 倍精度(double)型の1次元複素ベクトルに対して高速フーリエ変換と逆高速フーリエ変換を行うクラスです。
 * 
 * @author koga
 * @version $Revision: 1.1 $, 2004/06/17
 */
public final class DoubleComplexFFTAnalyzer {
  /**
   * 新しく生成された<code>DoubleComplexFFTAnalyzer</code>オブジェクトを初期化します。
   */
  private DoubleComplexFFTAnalyzer() {
    // nothing to do
  }

  /**
   * 複素ベクトルに対し<code>size</code>点までの高速フーリエ変換を行います。
   * 
   * <p>ベクトルの長さが<code>size</code>より短いとき、ゼロが後ろに付け加えられ、 ベクトルの長さが<code>size</code>より長いとき、<code>size</code> 番目以降が切り捨てられます。
   * 
   * @param reData 実部
   * @param imData 虚部
   * @param size 変換対象のデータ数
   * @return フーリエ変換結果
   */
  public static double[][] fft(final double[] reData, final double[] imData, final int size) {
    final int dataSize = reData.length;

    final double[] fRe = new double[size];
    final double[] fIm = new double[size];
        
    if (dataSize != size) {
      final int n = Math.min(size, dataSize);
      System.arraycopy(reData, 0, fRe, 0, n);
      System.arraycopy(imData, 0, fIm, 0, n);
      if (dataSize < size) {
        for (int i = dataSize; i < size; i++) {
          fRe[i] = 0;
          fIm[i] = 0;
        }
      }
    } else {
      System.arraycopy(reData, 0, fRe, 0, size);
      System.arraycopy(imData, 0, fIm, 0, size);
    }
    
    final int m = (int)(Math.log(size) / Math.log(2));
    DoubleFFTAnalyzerUtil.fft(fRe, fIm, m);
    return new double[][] {fRe, fIm};
  }

  /**
   * 複素ベクトルに対し<code>size</code>点までの逆高速フーリエ変換を行います。
   * 
   * <p>ベクトルの長さが<code>size</code>より短いとき、ゼロが後ろに付け加えられ、 ベクトルの長さが<code>size</code>より長いとき、<code>size</code>番目以降が切り捨てられます。
   * 
   * @param reData 実部
   * @param imData 虚部
   * @param size 変換対象のデータ数
   * @return 逆フーリエ変換結果
   */
  public static double[][] ifft(final double[] reData, final double[] imData, final int size) {
    final int dataSize = reData.length;

    final double[] fRe = new double[size];
    final double[] fIm = new double[size];

    if (dataSize != size) {
      final int n = Math.min(size, dataSize);
      System.arraycopy(reData, 0, fRe, 0, n);
      System.arraycopy(imData, 0, fIm, 0, n);
      if (dataSize < size) {
        for (int i = dataSize; i < size; i++) {
          fRe[i] = 0;
          fIm[i] = 0;
        }
      }
    } else {
      System.arraycopy(reData, 0, fRe, 0, size);
      System.arraycopy(imData, 0, fIm, 0, size);
    }

    final int m = (int)(Math.log(size) / Math.log(2));
    DoubleFFTAnalyzerUtil.ifft(fRe, fIm, m);
    return new double[][] {fRe, fIm};
  }
}