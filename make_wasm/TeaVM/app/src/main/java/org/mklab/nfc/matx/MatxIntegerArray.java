/*
 * $Id: MatxIntegerArray.java,v 1.6 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matx;

import org.mklab.nfc.matrix.BooleanMatrix;
import org.mklab.nfc.matrix.IntMatrix;
import org.mklab.nfc.scalar.IntNumber;


/**
 * MaTXの整数配列を表すクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.6 $
 */
public class MatxIntegerArray extends IntMatrix implements MatxArray<IntNumber,IntMatrix> {

  /** シリアルバージョン。 */
  private static final long serialVersionUID = -7559931642386952368L;

  /**
   * 新しく生成された<code>MatxIntegerArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxIntegerArray(final int[] matrix) {
    super(matrix);
  }

  /**
   * 新しく生成された<code>MatxIntegerArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxIntegerArray(final int[][] matrix) {
    super(matrix);
  }
  
  /**
   * 新しく生成された<code>MatxIntegerArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxIntegerArray(final IntMatrix matrix) {
    super(matrix.getRowSize(), matrix.getColumnSize());
    for (int i = 1; i <= matrix.getRowSize(); i++) {
      for (int j = 1; j <= matrix.getColumnSize(); j++) {
        setElement(i, j, matrix.getIntElement(i, j));
      }
    }
  }

  /**
   * 新しく生成された<code>MatxIntegerArray</code>オブジェクトを初期化します。
   * @param matrix 配列のデータ
   */
  public MatxIntegerArray(final BooleanMatrix matrix) {
    super(matrix.getRowSize(), matrix.getColumnSize());
    final IntMatrix index = matrix.find();
    for (int i = 1; i <= index.getColumnSize(); i++) {
      setElement(index.getIntElement(i), 1);
    }
  }

  /**
   * 新しく生成された<code>MatxIntegerArray</code>オブジェクトを初期化します。
   */
  public MatxIntegerArray() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  public IntMatrix toMatrix() {
    final IntMatrix ans = new IntMatrix(getRowSize(), getColumnSize());

    for (int i = 1; i <= getRowSize(); i++) {
      for (int j = 1; j <= getColumnSize(); j++) {
        ans.setElement(i, j, getIntElement(i, j));
      }
    }

    return ans;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toMmString() {
    return "Array(" +super.toMmString() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
  }
}
