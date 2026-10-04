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
 * 1次元実ベクトルに対して高速フーリエ変換と逆高速フーリエ変換を行うクラスです。
 * 
 * @author koga
 * @version $Revision: 1.3 $, 2004/06/17
 */
public final class RealFFTAnalyzer {

  /**
   * 新しく生成された<code>RealFFTAnalyzer</code>オブジェクトを初期化します。
   */
  private RealFFTAnalyzer() {
    // nothing to do
  }

  /**
   * 実ベクトルに対しsize点までの高速フーリエ変換を行います。
   * 
   * <p>ベクトルの長さが size より短いとき、ゼロが後ろに付け加えられ、 ベクトルの長さが size より長いとき、size 番目以降が切り捨てられます。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * 
   * @param data 実ベクトル
   * @param size 変換対象のデータ数
   * @return フーリエ変換結果
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> RS[][] fft(
      final RS[] data, final int size) {
    return ComplexFFTAnalyzer.fft(data, GridUtil.createZero(data, data.length), size);
  }

  /**
   * 実ベクトルに対しsize点までの逆高速フーリエ変換を行います。
   * 
   * <p>ベクトルの長さが size より短いとき、ゼロが後ろに付け加えられ、 ベクトルの長さが size より長いとき、size 番目以降が切り捨てられます。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * 
   * @param data 実ベクトル
   * @param size 変換対象のデータ数
   * @return 逆フーリエ変換結果
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> RS[][] ifft(
      final RS[] data, final int size) {
    return ComplexFFTAnalyzer.ifft(data, GridUtil.createZero(data, data.length), size);
  }

}
