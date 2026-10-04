/*
 * $Id: MatxComplexArray.java,v 1.10 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matx;

import org.mklab.nfc.matrix.DoubleComplexMatrix;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.IntMatrix;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.DoubleComplexNumber;


/**
 * MaTXのComplexArray型を表すクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.10 $, 2004/06/22
 */
public class MatxComplexArray extends DoubleComplexMatrix implements MatxArray<DoubleComplexNumber,DoubleComplexMatrix> {

  /** シリアルバージョン。  */
  private static final long serialVersionUID = -9138685565549821873L;

  /**
   * 新しく生成された<code>MatxComplexArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxComplexArray(final DoubleComplexNumber[] matrix) {
    super(matrix);
  }
  
  /**
   * 新しく生成された<code>MatxComplexArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxComplexArray(final DoubleComplexNumber[][] matrix) {
    super(matrix);
  }

  /**
   * 新しく生成された<code>MatxComplexArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxComplexArray(final IntMatrix matrix) {
    super(matrix.getRowSize(), matrix.getColumnSize());
    for (int row = 1; row <= matrix.getRowSize(); row++) {
      for (int column = 1; column <= matrix.getColumnSize(); column++) {
        setElement(row, column, new DoubleComplexNumber(matrix.getIntElement(row, column), 0));
      }
    }
  }

  /**
   * 新しく生成された<code>MatxComplexArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxComplexArray(final DoubleMatrix matrix) {
    super(matrix.getRowSize(), matrix.getColumnSize());
    for (int row = 1; row <= matrix.getRowSize(); row++) {
      for (int column = 1; column <= matrix.getColumnSize(); column++) {
        setElement(row, column, new DoubleComplexNumber(matrix.getDoubleElement(row, column), 0));
      }
    }
  }

  /**
   * 新しく生成された<code>MatxComplexArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxComplexArray(final NumericalMatrix<DoubleComplexNumber,DoubleComplexMatrix> matrix) {
    super(matrix.getRowSize(), matrix.getColumnSize());
    for (int row = 1; row <= matrix.getRowSize(); row++) {
      for (int column = 1; column <= matrix.getColumnSize(); column++) {
        setElement(row, column, matrix.getElement(row, column).clone());
      }
    }
  }
  
  /**
   * 新しく生成された<code>MatxComplexArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxComplexArray(final DoubleComplexMatrix matrix) {
    super(matrix.getRowSize(), matrix.getColumnSize());
    for (int row = 1; row <= matrix.getRowSize(); row++) {
      for (int column = 1; column <= matrix.getColumnSize(); column++) {
        setElement(row, column, matrix.getElement(row, column).clone());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexMatrix toMatrix() {
    DoubleComplexMatrix ans = new DoubleComplexMatrix(getRowSize(), getColumnSize());

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
