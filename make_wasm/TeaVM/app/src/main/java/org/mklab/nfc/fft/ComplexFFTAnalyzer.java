/*
 * Created on 2008/01/27
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.fft;

import org.mklab.nfc.matrix.AbstractNumericalComplexMatrix;
import org.mklab.nfc.matrix.GridUtil;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * 1次元複素ベクトルに対して高速フーリエ変換と逆高速フーリエ変換を行うクラスです。
 * 
 * @author koga
 * @version $Revision: 1.3 $, 2004/06/17
 */
public final class ComplexFFTAnalyzer {

  /**
   * 新しく生成された<code>ComplexFFTAnalyzer</code>オブジェクトを初期化します。
   */
  private ComplexFFTAnalyzer() {
    // nothing to do
  }

  /**
   * 複素ベクトルに対し<code>size</code>点までの高速フーリエ変換を行います。
   * 
   * <p>ベクトルの長さが<code>size</code>より短いとき、ゼロが後ろに付け加えられ、 ベクトルの長さが<code>size</code>より長いとき、<code>size</code> 番目以降が切り捨てられます。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * 
   * @param reData 実部
   * @param imData 虚部
   * @param size 変換対象のデータ数
   * @return フーリエ変換結果
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> RS[][] fft(
      final RS[] reData, final RS[] imData, final int size) {
    final int dataSize = reData.length;

    final RS[] fRe = reData[0].createArray(size);
    final RS[] fIm = imData[0].createArray(size);

    if (dataSize != size) {
      final int n = Math.min(size, dataSize);
      System.arraycopy(reData, 0, fRe, 0, n);
      System.arraycopy(imData, 0, fIm, 0, n);
      if (dataSize < size) {
        for (int i = dataSize; i < size; i++) {
          fRe[i] = reData[0].createZero();
          fIm[i] = imData[0].createZero();
        }
      }
    } else {
      System.arraycopy(reData, 0, fRe, 0, size);
      System.arraycopy(imData, 0, fIm, 0, size);
    }

    final int m = (int)(Math.log(size) / Math.log(2));
    FFTAnalyzerUtil.fft(fRe, fIm, m);
    final RS unit = fRe[0];
    final RS[][] matrix = unit.createArray(2, 1);
    matrix[0] = fRe;
    matrix[1] = fIm;

    return matrix;
    //return unit.createArray(new NumericalScalar[][] {fRe, fIm});
  }

  /**
   * 複素ベクトルに対し<code>size</code>点までの逆高速フーリエ変換を行います。
   * 
   * <p>ベクトルの長さが<code>size</code>より短いとき、ゼロが後ろに付け加えられ、 ベクトルの長さが<code>size</code>より長いとき、<code>size</code>番目以降が切り捨てられます。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * 
   * @param reData 実部
   * @param imData 虚部
   * @param size 変換対象のデータ数
   * @return 逆フーリエ変換結果
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> RS[][] ifft(
      final RS[] reData, final RS[] imData, final int size) {
    final int dataSize = reData.length;

    final RS[] fRe = reData[0].createArray(size);
    final RS[] fIm = imData[0].createArray(size);

    if (dataSize != size) {
      final int n = Math.min(size, dataSize);
      System.arraycopy(GridUtil.clone(reData), 0, fRe, 0, n);
      System.arraycopy(GridUtil.clone(imData), 0, fIm, 0, n);
      if (dataSize < size) {
        for (int i = dataSize; i < size; i++) {
          fRe[i] = reData[0].createZero();
          fIm[i] = imData[0].createZero();
        }
      }
    } else {
      System.arraycopy(GridUtil.clone(reData), 0, fRe, 0, size);
      System.arraycopy(GridUtil.clone(imData), 0, fIm, 0, size);
    }

    final int m = (int)(Math.log(size) / Math.log(2));
    FFTAnalyzerUtil.ifft(fRe, fIm, m);
    final RS unit = fRe[0];
    final RS[][] matrix = unit.createArray(2, 1);
    matrix[0] = fRe;
    matrix[1] = fIm;
    return matrix;
    //return unit.createArray(new NumericalScalar[][] {fRe, fIm});
  }

}
