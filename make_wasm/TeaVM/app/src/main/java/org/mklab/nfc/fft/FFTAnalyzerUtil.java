/*
 * $Id: FFTUtil.java,v 1.3 2008/03/15 00:23:44 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.fft;

import org.mklab.nfc.matrix.AbstractNumericalComplexMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * 高速フーリエ変換(FFT)を行うためのユーティリティークラスです。
 * 
 * @author koga
 * @version $Revision: 1.3 $, 2004/06/17
 */
final class FFTAnalyzerUtil {

  /**
   * 新しく生成された<code>FFTAnalyzerUtil</code>オブジェクトを初期化します。
   */
  private FFTAnalyzerUtil() {
    // nothing
  }

  /**
   * 複素数の系列のFFTを求めます。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * 
   * @param reData 系列の実部
   * @param imData 系列の虚部
   * @param size 変換対象のデータ数
   */
  static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> void fft(
      final RS[] reData, final RS[] imData, final int size) {
    innerFFT(reData, imData, size, true);
  }

  /**
   * 複素数の系列のIFFTを求めます。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * 
   * @param reData 系列の実部
   * @param imData 系列の虚部
   * @param size 変換対象のデータ数
   */
  static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> void ifft(
      final RS[] reData, final RS[] imData, final int size) {
    innerFFT(reData, imData, size, false);
  }

  /**
   * 複素数の系列のFFTを求めます。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * 
   * @param reData 系列の実部
   * @param imData 系列の虚部
   * @param size 変換対象のデータ数
   * @param isForwardMode 正変換のならばtrue、逆変換ならばfalse
   */
  private static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> void innerFFT(
      final RS[] reData, final RS[] imData, final int size, final boolean isForwardMode) {
    final RS scalar = reData[0];

    final int n = 1 << size;
    final RS pi = (scalar.createUnit().multiply(-1)).acos();
    final RS v2 = pi.multiply(2).divide(n);

    for (int p = 0; p < size; p++) {
      final int gap = 1 << (size - p);
      final int g2 = gap / 2;

      // NumericalMatrixElement v = v2 * Math.pow(2,p);
      final RS v = v2.multiply(1 << p);
      final RS wr = v.cos(); // W^(2^p)
      RS wi = v.sin();

      if (isForwardMode) {
        wi = wi.unaryMinus(); // 正変換のとき
      }

      for (int k = 0; k < n; k += gap) {
        RS ur = scalar.createUnit(); // U = (1.0, 0.0)
        RS ui = scalar.createZero();

        for (int j = k; j < k + g2; j++) {
          int j2 = j + g2;
          RS vr = reData[j2]; // V = F[j2]
          RS vi = imData[j2];
          RS tr = reData[j].subtract(vr); // T = F[j] - F[j2]
          RS ti = imData[j].subtract(vi);

          // F[j] = F[j] + F[j2]
          reData[j] = reData[j].add(vr);
          imData[j] = imData[j].add(vi);

          // F[jl] = U * (F[j] - F[j2])
          reData[j2] = tr.multiply(ur).subtract(ti.multiply(ui));
          imData[j2] = tr.multiply(ui).add(ti.multiply(ur));

          // U = U * W
          final RS w = ur.multiply(wr).subtract(ui.multiply(wi));
          ui = ur.multiply(wi).add(ui.multiply(wr));
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
        final RS tr = reData[j];
        final RS ti = imData[j];
        reData[j] = reData[i];
        imData[j] = imData[i];
        reData[i] = tr;
        imData[i] = ti;
      }
    }

    if (isForwardMode == false) {
      // 逆変換の時は n で割る
      final RS w = scalar.createUnit().divide(n);
      for (int k = 0; k < n; k++) {
        reData[k] = reData[k].multiply(w);
        imData[k] = imData[k].multiply(w);
      }
    }
  }

}
