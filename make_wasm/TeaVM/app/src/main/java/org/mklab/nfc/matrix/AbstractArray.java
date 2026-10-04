/**
 * $Id: AbstractArray.java,v 1.41 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Masanobu Koga. All rights reserved.
 */
package org.mklab.nfc.matrix;

/**
 * 配列データを扱うためのクラスです。
 * 
 * @author koga
 * @version $Revision: 1.41 $
 * @param <M> 行列の型
 */
public abstract class AbstractArray<M extends Array<M>> extends AbstractGrid<M> implements Array<M> {

  /** シリアル番号。 */
  private static final long serialVersionUID = -8683375527148544795L;

  /**
   * 新しく生成された<code>AbstractArray</code>オブジェクトを初期化します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   */
  public AbstractArray(final int rowSize, final int columnSize) {
    super(rowSize, columnSize);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone() {
    try {
      final Object ans = super.clone();
      return ans;
    } catch (CloneNotSupportedException e) {
      throw new InternalError(e.getMessage());
    }
  }

  /**
   * {@inheritDoc}
   */
  public M createClone() {
    return (M)clone();
  }

  /**
   * {@inheritDoc}
   */
  public final M getRowVector(final int row) {
    return getSubMatrix(row, row, 1, getColumnSize());
  }

  /**
   * {@inheritDoc}
   */
  public final M getRowVectors(final int rowMin, final int rowMax) {
    return getSubMatrix(rowMin, rowMax, 1, getColumnSize());
  }

  /**
   * {@inheritDoc}
   */
  public final M getRowVectors(final IntMatrix rowIndex) {
    return getSubMatrix(rowIndex, 1, getColumnSize());
  }

  /**
   * {@inheritDoc}
   */
  public final M getColumnVector(final int column) {
    return getSubMatrix(1, getRowSize(), column, column);
  }

  /**
   * {@inheritDoc}
   */
  public final M getColumnVectors(final int columnMin, final int columnMax) {
    return getSubMatrix(1, getRowSize(), columnMin, columnMax);
  }

  /**
   * {@inheritDoc}
   */
  public final M getColumnVectors(final IntMatrix columnIndex) {
    return getSubMatrix(1, getRowSize(), columnIndex);
  }

  /**
   * {@inheritDoc}
   */
  public final M getSubMatrix(final int row, final int column, final Grid block) {
    final int blockRowSize = block.getRowSize();
    final int blockColumnSize = block.getColumnSize();
    final int rs = (row - 1) * blockRowSize + 1;
    final int cs = (column - 1) * blockColumnSize + 1;
    return getSubMatrix(rs, rs + blockRowSize - 1, cs, cs + blockColumnSize - 1);
  }

  /**
   * {@inheritDoc}
   */
  public final M getSubMatrix(final IntMatrix rowIndex, final int column) {
    return getSubMatrix(rowIndex, column, column);
  }

  /**
   * {@inheritDoc}
   */
  public final M getSubMatrix(final int row, final IntMatrix columnIndex) {
    return getSubMatrix(row, row, columnIndex);
  }

  /**
   * {@inheritDoc}
   */
  public final M getSubVector(final int min, final int max) {
    return getSubVector(min, max, 1);
  }

  /**
   * {@inheritDoc}
   */
  public final M getSubVector(final int min, final int max, final int by) {
    return getSubVector(IntMatrix.series(min, max, by));
  }

  /**
   * {@inheritDoc}
   */
  public final M flipLeftRight() {
    final M ans = createClone();
    int i = getColumnSize() / 2;
    int j = getColumnSize() - i + 1;
    while (i > 0) {
      ans.exchangeColumn(i--, j++);
    }
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final M flipUpDown() {
    final M ans =createClone();
    int i = getRowSize() / 2;
    int j = getRowSize() - i + 1;
    while (i > 0) {
      ans.exchangeRow(i--, j++);
    }
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final M rotateUp(final int number) {
    if (number == 0) {
      return createClone();
    }
    if (number > 0) {
      final M upper = getRowVectors(number + 1, getRowSize());
      final M lower = getRowVectors(1, number);
      return upper.appendDown(lower);
    }
    final M upper = getRowVectors(getRowSize() + number + 1, getRowSize());
    final M lower = getRowVectors(1, getRowSize() + number);
    return upper.appendDown(lower);
  }

  /**
   * {@inheritDoc}
   */
  public final M rotateLeft(final int number) {
    if (number == 0) {
      return createClone();
    }
    if (number > 0) {
      final M left = getColumnVectors(number + 1, getColumnSize());
      final M right = getColumnVectors(1, number);
      return left.appendRight(right);
    }
    final M left = getColumnVectors(getColumnSize() + number + 1, getColumnSize());
    final M right = getColumnVectors(1, getColumnSize() + number);
    return left.appendRight(right);
  }

  /**
   * {@inheritDoc}
   */
  public final void setRowVector(final int row, final M source) {
    setSubMatrix(row, row, 1, getColumnSize(), source);
  }

  /**
   * {@inheritDoc}
   */
  public final void setRowVectors(final int rowMin, final int rowMax, final M source) {
    setSubMatrix(rowMin, rowMax, 1, getColumnSize(), source);
  }

  /**
   * {@inheritDoc}
   */
  public final void setRowVectors(final IntMatrix rowIndex, final M source) {
    setSubMatrix(rowIndex, 1, getColumnSize(), source);
  }

  /**
   * {@inheritDoc}
   */
  public final void setColumnVector(final int column, final M source) {
    setSubMatrix(1, getRowSize(), column, column, source);
  }

  /**
   * {@inheritDoc}
   */
  public final void setColumnVectors(final int columnMin, final int columnMax, final M source) {
    setSubMatrix(1, getRowSize(), columnMin, columnMax, source);
  }

  /**
   * {@inheritDoc}
   */
  public final void setColumnVectors(final IntMatrix columnIndex, final M source) {
    setSubMatrix(1, getRowSize(), columnIndex, source);
  }

  /**
   * {@inheritDoc}
   */
  public final void setSubMatrix(final int row, final int column, final Grid block, final M source) {
    final int rs = (row - 1) * block.getRowSize() + 1;
    final int re = rs + source.getRowSize() - 1;
    final int cs = (column - 1) * block.getColumnSize() + 1;
    final int ce = cs + source.getColumnSize() - 1;

    if (re > getRowSize() || ce > getColumnSize()) {
      final int rmax = Math.max(re, getRowSize());
      final int cmax = Math.max(ce, getColumnSize());
      resize(rmax, cmax).setSubMatrix(rs, re, cs, ce, source);
    }

    setSubMatrix(rs, re, cs, ce, source);
  }

  /**
   * {@inheritDoc}
   */
  public final void setSubMatrix(final IntMatrix rowIndex, final int column, final M source) {
    setSubMatrix(rowIndex, column, column, source);
  }

  /**
   * {@inheritDoc}
   */
  public final void setSubMatrix(final int row, final IntMatrix columnIndex, final M source) {
    setSubMatrix(row, row, columnIndex, source);
  }

  /**
   * {@inheritDoc}
   */
  public final void setSubVector(final int min, final int max, final M source) {
    setSubVector(min, max, 1, source);
  }

  /**
   * {@inheritDoc}
   */
  public final void setSubVector(final int min, final int max, final int by, final M source) {
    setSubVector(IntMatrix.series(min, max, by), source);
  }
}