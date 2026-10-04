/*
 * $Id: DoubleFFTUtil.java,v 1.2 2008/03/15 00:23:44 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.fft;

/**
 * 倍精度(double)型の高速フーリエ変換(FFT)を行うためのユーティリティークラスです。
 * 
 * @author koga
 * @version $Revision: 1.2 $, 2004/06/17
 */
final class DoubleFFTAnalyzerUtil {
  /**
   * 新しく生成された<code>DoubleFFTAnalyzerUtil</code>オブジェクトを初期化します。
   */
  private DoubleFFTAnalyzerUtil() {
    // nothing
  }

  /**
   * 複素数の系列のFFTを求めます。
   * 
   * @param reData 系列の実部
   * @param imData 系列の虚部
   * @param size 変換対象のデータ数
   */
  static void fft(final double[] reData, final double[] imData, final int size) {
    innerFFT(reData, imData, size, true);
  }

  /**
   * 複素数の系列のIFFTを求めます。
   * 
   * @param reData 系列の実部
   * @param imData 系列の虚部
   * @param size 変換対象のデータ数
   */
  static void ifft(final double[] reData, final double[] imData, final int size) {
    innerFFT(reData, imData, size, false);
  }

  /**
   * 複素数の系列のFFTを求めます。
   * 
   * @param reData 系列の実部
   * @param imData 系列の虚部
   * @param size 変換対象のデータ数
   * @param isForwardMode 正変換のならばtrue、逆変換ならばfalse
   */
  private static void innerFFT(final double[] reData, final double[] imData, final int size, final boolean isForwardMode) {
    // int n = (int)Math.pow(2,m); // 2^m
    int n = 1 << size;
    double v2 = 2.0 * Math.PI / n;

    for (int p = 0; p < size; p++) {
      // int gap = (int)Math.pow(2,m-p); // 2^(m-p)
      final int gap = 1 << (size - p);
      final int g2 = gap / 2;

      // double v = v2 * Math.pow(2,p);
      final double v = v2 * (1 << p);
      final double wr = Math.cos(v); // W^(2^p)
      double wi = Math.sin(v);

      if (isForwardMode) {
        wi = -wi; // 正変換のとき
      }

      for (int k = 0; k < n; k += gap) {
        double ur = 1.0; // U = (1.0, 0.0)
        double ui = 0.0;

        for (int j = k; j < k + g2; j++) {
          final int j2 = j + g2;
          final double vr = reData[j2]; // V = F[j2]
          final double vi = imData[j2];
          final double tr = reData[j] - vr; // T = F[j] - F[j2]
          final double ti = imData[j] - vi;

          // F[j] = F[j] + F[j2]
          reData[j] += vr;
          imData[j] += vi;

          // F[jl] = U * (F[j] - F[j2])
          reData[j2] = tr * ur - ti * ui;
          imData[j2] = tr * ui + ti * ur;

          // U = U * W
          final double w = ur * wr - ui * wi;
          ui = ur * wi + ui * wr;
          ur = w;
        }
      }
    }

    // ビット逆順並び替え
    for (int i = 0; i < n - 1; i++) {
      int p = i;
      int j = 0;

      // j = (iのビット逆順)
      for (int k = 0; k < size; k++) {
        // j *= 2;
        j <<= 1;
        j += p % 2;
        // p /= 2;
        p >>= 1;
      }

      if (i < j) {
        // 成分の入れ替え
        final double tr = reData[j];
        final double ti = imData[j];
        reData[j] = reData[i];
        imData[j] = imData[i];
        reData[i] = tr;
        imData[i] = ti;
      }
    }

    if (isForwardMode == false) {
      // 逆変換の時は n で割る
      final double w = 1.0 / n;
      for (int k = 0; k < n; k++) {
        reData[k] *= w;
        imData[k] *= w;
      }
    }
  }
}
