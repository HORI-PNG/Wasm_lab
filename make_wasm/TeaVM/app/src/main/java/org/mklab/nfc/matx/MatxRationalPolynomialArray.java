/*
 * $Id: MatxRationalPolynomialArray.java,v 1.5 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matx;

import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.DoubleRationalPolynomialMatrix;
import org.mklab.nfc.matrix.IntMatrix;
import org.mklab.nfc.scalar.DoubleRationalPolynomial;


/**
 * MaTXの有理多項式配列を表すクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.5 $, 2008/04/15
 */
public class MatxRationalPolynomialArray extends DoubleRationalPolynomialMatrix implements MatxArray<DoubleRationalPolynomial,DoubleRationalPolynomialMatrix> {

  /** シリアルバージョン。 */
  private static final long serialVersionUID = -7559931642386952368L;

  /**
   * 新しく生成された<code>MatxRationalPolynomialArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxRationalPolynomialArray(final DoubleRationalPolynomial[] matrix) {
    //super(DoubleRationalPolynomialMatrix.createArray(matrix));
    super(matrix);
  }
  
  /**
   * 新しく生成された<code>MatxRationalPolynomialArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxRationalPolynomialArray(final DoubleRationalPolynomial[][] matrix) {
    //super(DoubleRationalPolynomialMatrix.createArray(matrix));
    super(matrix);
  }

  /**
   * 新しく生成された<code>MatxRationalPolynomialArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxRationalPolynomialArray(final IntMatrix matrix) {
    super(matrix.getRowSize(), matrix.getColumnSize());
    for (int row = 1; row <= matrix.getRowSize(); row++) {
      for (int column = 1; column <= matrix.getColumnSize(); column++) {
        setElement(row, column, matrix.getIntElement(row, column));
      }
    }
  }

  /**
   * 新しく生成された<code>MatxRationalPolynomialArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxRationalPolynomialArray(final DoubleMatrix matrix) {
    super(matrix.getRowSize(), matrix.getColumnSize());
    for (int row = 1; row <= matrix.getRowSize(); row++) {
      for (int column = 1; column <= matrix.getColumnSize(); column++) {
        setElement(row, column, matrix.getDoubleElement(row, column));
      }
    }
  }

  /**
//   * 新しく生成された<code>MatxRationalPolynomialArray</code>オブジェクトを初期化します。
//   * @param matrix 配列のデータ
//   */
//  public MatxRationalPolynomialArray(final DoubleComplexMatrix matrix) {
//    super(matrix.getRowSize(), matrix.getColumnSize());
//    for (int row = 1; row <= matrix.getRowSize(); row++) {
//      for (int column = 1; column <= matrix.getColumnSize(); column++) {
//        setElement(row, column, new RationalPolynomial<>(matrix.getElement(row, column)));
//      }
//    }
//  }

//  /**
//   * 新しく生成された<code>MatxRationalPolynomialArray</code>オブジェクトを初期化します。
//   * @param matrix 配列のデータ
//   */
//  public MatxRationalPolynomialArray(final DoubleRationalPolynomialMatrix matrix) {
//    super(matrix.getRowSize(), matrix.getColumnSize());
//    for (int row = 1; row <= matrix.getRowSize(); row++) {
//      for (int column = 1; column <= matrix.getColumnSize(); column++) {
//        setElement(row, column, new RationalPolynomial<>(matrix.getElement(row, column)));
//      }
//    }
//  }

  /**
   * 新しく生成された<code>MatxRationalPolynomialArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxRationalPolynomialArray(final DoubleRationalPolynomialMatrix matrix) {
    super(matrix.getRowSize(), matrix.getColumnSize());
    for (int row = 1; row <= matrix.getRowSize(); row++) {
      for (int column = 1; column <= matrix.getColumnSize(); column++) {
        setElement(row, column, matrix.getElement(row, column));
      }
    }
  }
  
  /**
   * 新しく生成された<code>MatxRationalPolynomialArray</code>オブジェクトを初期化します。
   */
  public MatxRationalPolynomialArray() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomialMatrix toMatrix() {
    final DoubleRationalPolynomialMatrix ans = new DoubleRationalPolynomialMatrix(getRowSize(), getColumnSize());

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
