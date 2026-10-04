/*
 * Created on 2011/06/27
 * Copyright (C) 2011 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matx;

import org.mklab.nfc.matrix.DoubleComplexMatrix;
import org.mklab.nfc.matrix.DoubleComplexPolynomialMatrix;
import org.mklab.nfc.matrix.DoubleComplexRationalPolynomialMatrix;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.DoublePolynomialMatrix;
import org.mklab.nfc.matrix.DoubleRationalPolynomialMatrix;
import org.mklab.nfc.matrix.IntMatrix;


/**
 * Matrix型からMatxArray型を生成するためのクラスです。
 * 
 * @author Ryota
 * @version 1.0, 2011/06/27
 */
public final class MatxArrayCreater {
  /**
   * 新しく生成された<code>MatxArrayCreater</code>オブジェクトを初期化します。
   */
  private MatxArrayCreater() {
    // nothing to do
  }

  /**
   * 行列の型により対応するMatxArrayを返します。
   * 
   * @param value 行列
   * @return 行列に対応したMatxArray
   */
  public static  MatxIntegerArray create(final IntMatrix value) {
    return new MatxIntegerArray(value);
  }

  /**
   * 行列の型により対応するMatxArrayを返します。
   * 
   * @param value 行列
   * @return 行列に対応したMatxArray
   */
  public static  MatxRealArray create(final DoubleMatrix value) {
    return new MatxRealArray(value);
  }

  /**
   * 行列の型により対応するMatxArrayを返します。
   * 
   * @param value 行列
   * @return 行列に対応したMatxArray
   */
  public static  MatxComplexArray create(final DoubleComplexMatrix value) {
    return new MatxComplexArray(value);
  }

  /**
   * 行列の型により対応するMatxArrayを返します。
   * 
   * @param value 行列
   * @return 行列に対応したMatxArray
   */
  public static  MatxPolynomialArray create(final DoublePolynomialMatrix value) {
    return new MatxPolynomialArray(value);
  }

  /**
   * 行列の型により対応するMatxArrayを返します。
   * 
   * @param value 行列
   * @return 行列に対応したMatxArray
   */
  public static  MatxComplexPolynomialArray create(final DoubleComplexPolynomialMatrix value) {
    return new MatxComplexPolynomialArray(value);
  }

  /**
   * 行列の型により対応するMatxArrayを返します。
   * 
   * @param value 行列
   * @return 行列に対応したMatxArray
   */
  public static  MatxRationalPolynomialArray create(final DoubleRationalPolynomialMatrix value) {
    return new MatxRationalPolynomialArray(value);
  }

  /**
   * 行列の型により対応するMatxArrayを返します。
   * 
   * @param value 行列
   * @return 行列に対応したMatxArray
   */
  public static  MatxComplexRationalPolynomialArray create(final DoubleComplexRationalPolynomialMatrix value) {
    return new MatxComplexRationalPolynomialArray(value);
  }

}
