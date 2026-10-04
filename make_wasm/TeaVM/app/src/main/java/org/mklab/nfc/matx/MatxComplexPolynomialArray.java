/*
 * $Id: MatxPolynomialArray.java,v 1.5 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matx;

import org.mklab.nfc.matrix.DoubleComplexPolynomialMatrix;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.IntMatrix;
import org.mklab.nfc.scalar.DoubleComplexPolynomial;


/**
 * MaTXの多項式配列を表すクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.5 $
 */
public class MatxComplexPolynomialArray extends DoubleComplexPolynomialMatrix implements MatxArray<DoubleComplexPolynomial,DoubleComplexPolynomialMatrix>{

  /** シリアルバージョン。 */
  private static final long serialVersionUID = -7559931642386952368L;

  /**
   * 新しく生成された<code>MatxPolynomialArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxComplexPolynomialArray(final DoubleComplexPolynomial[] matrix) {
    super(matrix);
  }

  /**
   * 新しく生成された<code>MatxPolynomialArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxComplexPolynomialArray(final DoubleComplexPolynomial[][] matrix) {
    super(matrix);
  }

  /**
   * 新しく生成された<code>MatxPolynomialArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxComplexPolynomialArray(final IntMatrix matrix) {
    super(matrix.getRowSize(), matrix.getColumnSize());
    for (int row = 1; row <= matrix.getRowSize(); row++) {
      for (int column = 1; column <= matrix.getColumnSize(); column++) {
        setElement(row, column, matrix.getIntElement(row, column));
      }
    }
  }

  /**
   * 新しく生成された<code>MatxPolynomialArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxComplexPolynomialArray(final DoubleMatrix matrix) {
    super(matrix.getRowSize(), matrix.getColumnSize());
    for (int row = 1; row <= matrix.getRowSize(); row++) {
      for (int column = 1; column <= matrix.getColumnSize(); column++) {
        setElement(row, column, matrix.getDoubleElement(row, column));
      }
    }
  }

  /**
   * 新しく生成された<code>MatxPolynomialArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxComplexPolynomialArray(final DoubleComplexPolynomialMatrix matrix) {
    super(matrix.getRowSize(), matrix.getColumnSize());
    for (int row = 1; row <= matrix.getRowSize(); row++) {
      for (int column = 1; column <= matrix.getColumnSize(); column++) {
        setElement(row, column, matrix.getElement(row, column));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomialMatrix toMatrix() {
    final DoubleComplexPolynomialMatrix ans = new DoubleComplexPolynomialMatrix(getRowSize(), getColumnSize());

    for (int row = 1; row <= getRowSize(); row++) {
      for (int column = 1; column <= getColumnSize(); column++) {
        ans.setElement(row, column, getElement(row, column));
      }
    }

    return ans;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toMmString() {
    return "Array(" + super.toMmString() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
  }
}
