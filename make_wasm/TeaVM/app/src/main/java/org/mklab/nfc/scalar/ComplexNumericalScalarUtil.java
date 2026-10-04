/*
 * $Id: ComplexNumericalScalarUtil.java,v 1.2 2008/06/26 10:10:33 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.scalar;

import org.mklab.nfc.matrix.AbstractNumericalComplexMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;

/**
 * 複素数に関するユーティリティクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.2 $, 2004/06/23
 */
public final class ComplexNumericalScalarUtil {
  /**
   * 新しく生成された<code>ComplexNumericalScalarUtil</code>オブジェクトを初期化します。
   */
  private ComplexNumericalScalarUtil() {
    // nothing to do
  }

//  /**
//   * 実数の逆数と複素数の積を返します。
//   * 
//   * @param <RS> 実部と虚部の型
//   * @param <RM> 行列の型
//   * 
//   * @param realNumber 実数
//   * @param complexNumber 複素数
//   * @return 実数の逆数と複素数の積
//   */
//  public static <RS extends NumericalScalar<RS, RM>, RM extends NumericalMatrix<RS, RM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> CS leftDivide(final double realNumber, final CS complexNumber) {
//    final RS rePart = complexNumber.getRealPart().divide(realNumber);
//    final RS impart = complexNumber.getImaginaryPart().divide(realNumber);
//    return new BaseComplexNumericalScalar<>(rePart, impart);
//  }
//
//  /**
//   * 整数の逆数と複素数の積を返します。
//   * 
//   * @param <S> 実部と虚部の型
//   * @param <M> 行列の型
//   * 
//   * @param intNumber 整数
//   * @param complexNumber 複素数
//   * @return 整数の逆数と複素数の積
//   */
//  public static <RS extends NumericalScalar<RS, RM>, RM extends NumericalMatrix<RS, RM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> CS leftDivide(final int intNumber, final CS complexNumber) {
//    final RS rePart = complexNumber.getRealPart().divide(intNumber);
//    final RS impart = complexNumber.getImaginaryPart().divide(intNumber);
//    return new BaseComplexNumericalScalar<>(rePart, impart);
//  }

  /**
   * 実数の複素数乗を返します。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * 
   * @param length 実数
   * @param scalar 複素数
   * @return 実数の複素数乗
   */
  public static <RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> CS power(final RS length, final CS scalar) {
    final RS logr = length.log();
    final RS e = length.createE();
    final RS d = e.power(scalar.getRealPart().multiply(logr));
    final RS th = scalar.getImaginaryPart().multiply(logr);
    final RS rePart = d.multiply(th.cos());
    final RS imPart = d.multiply(th.sin());
    return scalar.create(rePart, imPart);
  }
}