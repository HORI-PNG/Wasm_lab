/*
 * $Id: MatxRealArray.java,v 1.10 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matx;

import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.DoubleMatrixUtil;
import org.mklab.nfc.matrix.IntMatrix;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.DoubleNumber;


/**
 * MaTXのRealArray型を表すクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.10 $
 */
public class MatxRealArray extends DoubleMatrix implements MatxArray<DoubleNumber,DoubleMatrix> {

  /** シリアルバージョン。 */
  private static final long serialVersionUID = -7559931642386952368L;

  /**
   * 新しく生成された<code>MatxRealArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxRealArray(final double[] matrix) {
    super(matrix);
  }

  /**
   * 新しく生成された<code>MatxRealArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxRealArray(final double[][] matrix) {
    super(matrix);
  }
  
  /**
   * 新しく生成された<code>MatxRealArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxRealArray(final IntMatrix matrix) {
    super(matrix.getRowSize(), matrix.getColumnSize());
    for (int row = 1; row <= matrix.getRowSize(); row++) {
      for (int column = 1; column <= matrix.getColumnSize(); column++) {
        setElement(row, column, matrix.getIntElement(row, column));
      }
    }
  }

  
//  /**
//   * 新しく生成された<code>MatxRealArray</code>オブジェクトを初期化します。
//   * @param matrix 配列のデータ
//   */
//  public MatxRealArray(final DoubleNumberMatrix matrix) {
//    super(matrix.getRowSize(), matrix.getColumnSize());
//    for (int row = 1; row <= matrix.getRowSize(); row++) {
//      for (int column = 1; column <= matrix.getColumnSize(); column++) {
//        setElement(row, column, matrix.getElement(row, column));
//      }
//    }
//  }
  
  /**
   * 新しく生成された<code>MatxRealArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxRealArray(final NumericalMatrix<DoubleNumber,DoubleMatrix> matrix) {
    super(matrix.getRowSize(), matrix.getColumnSize());
    for (int row = 1; row <= matrix.getRowSize(); row++) {
      for (int column = 1; column <= matrix.getColumnSize(); column++) {
        setElement(row, column, matrix.getElement(row, column));
      }
    }
  }

  /**
   * 新しく生成された<code>MatxRealArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxRealArray(final DoubleMatrix matrix) {
    super(matrix.getRowSize(), matrix.getColumnSize());
    for (int row = 1; row <= matrix.getRowSize(); row++) {
      for (int column = 1; column <= matrix.getColumnSize(); column++) {
        setElement(row, column, matrix.getDoubleElement(row, column));
      }
    }
  }

  /**
   * 新しく生成された<code>MatxRealArray</code>オブジェクトを初期化します。
   * 
   * @param from 配列の始め
   * @param to 配列の最後
   * @param by 配列の間隔
   * 
   */
  public MatxRealArray(final double from, final double to, final double by) {
    super(DoubleMatrixUtil.series(from, to, by));
  }

  /**
   * 新しく生成された<code>MatxRealArray</code>オブジェクトを初期化します。
   */
  public MatxRealArray() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  public DoubleMatrix toMatrix() {
    final DoubleMatrix ans = new DoubleMatrix(getRowSize(), getColumnSize());

    for (int row = 1; row <= getRowSize(); row++) {
      for (int column = 1; column <= getColumnSize(); column++) {
        ans.setElement(row, column, getDoubleElement(row, column));
      }
    }

    return ans;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getGridClassName() {
    return "Array"; //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toMmString() {
    return "Array(" + super.toMmString() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
  }
}
