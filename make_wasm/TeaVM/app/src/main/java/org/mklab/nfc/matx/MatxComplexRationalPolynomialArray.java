/*
 * $Id: MatxRationalPolynomialArray.java,v 1.5 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matx;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.mklab.nfc.matrix.DoubleComplexRationalPolynomialMatrix;
import org.mklab.nfc.matrix.DoubleComplexRationalPolynomialMatrixUtil;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.IntMatrix;
import org.mklab.nfc.scalar.DoubleComplexRationalPolynomial;


/**
 * MaTXの有理多項式配列を表すクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.5 $, 2008/04/15
 */
public class MatxComplexRationalPolynomialArray extends DoubleComplexRationalPolynomialMatrix  implements MatxArray<DoubleComplexRationalPolynomial,DoubleComplexRationalPolynomialMatrix>{

  /** シリアルバージョン。 */
  private static final long serialVersionUID = -7559931642386952368L;

  /**
   * 新しく生成された<code>MatxRationalPolynomialArray</code>オブジェクトを初期化します。
   * 
   * @param matrix 配列のデータ
   */
  public MatxComplexRationalPolynomialArray(final DoubleComplexRationalPolynomial[] matrix) {
    super(matrix);
  }

  /**
   * 新しく生成された<code>MatxRationalPolynomialArray</code>オブジェクトを初期化します。
   * 
   * @param matrix 配列のデータ
   */
  public MatxComplexRationalPolynomialArray(final DoubleComplexRationalPolynomial[][] matrix) {
    super(matrix);
  }

  /**
   * 新しく生成された<code>MatxRationalPolynomialArray</code>オブジェクトを初期化します。
   * 
   * @param matrix 配列のデータ
   */
  public MatxComplexRationalPolynomialArray(final IntMatrix matrix) {
    super(matrix.getRowSize(), matrix.getColumnSize());
    for (int row = 1; row <= matrix.getRowSize(); row++) {
      for (int column = 1; column <= matrix.getColumnSize(); column++) {
        setElement(row, column, matrix.getIntElement(row, column));
      }
    }
  }

  /**
   * 新しく生成された<code>MatxRationalPolynomialArray</code>オブジェクトを初期化します。
   * 
   * @param matrix 配列のデータ
   */
  public MatxComplexRationalPolynomialArray(final DoubleMatrix matrix) {
    super(matrix.getRowSize(), matrix.getColumnSize());
    for (int row = 1; row <= matrix.getRowSize(); row++) {
      for (int column = 1; column <= matrix.getColumnSize(); column++) {
        setElement(row, column, matrix.getDoubleElement(row, column));
      }
    }
  }

  /**
   * // * 新しく生成された<code>MatxRationalPolynomialArray</code>オブジェクトを初期化します。 // * @param matrix 配列のデータ //
   */
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
   * 
   * @param matrix 配列のデータ
   */
  public MatxComplexRationalPolynomialArray(final DoubleComplexRationalPolynomialMatrix matrix) {
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
  public DoubleComplexRationalPolynomialMatrix toMatrix() {
    final DoubleComplexRationalPolynomialMatrix ans = new DoubleComplexRationalPolynomialMatrix(getRowSize(), getColumnSize());

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

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeMxFormat(final DataOutputStream output, final String name) throws IOException {
    DoubleComplexRationalPolynomialMatrixUtil.writeMxFormat(getElements(), output, name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeMxFormat(final File file, final String name) throws IOException {
    try (DataOutputStream output = new DataOutputStream(new FileOutputStream(file))) {
      writeMxFormat(output, name);
    }
  }
}
