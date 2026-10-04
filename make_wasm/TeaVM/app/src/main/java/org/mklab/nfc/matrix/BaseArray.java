/*
 * $Id: BaseArray.java,v 1.10 2008/07/16 04:58:02 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.matrix;

import java.io.Writer;


/**
 * {@link ArrayElement}を成分とする配列を表わすクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.10 $, 2004/07/05
 * @param <M> 行列の型
 * @param <S> スカラーの型
 */
public class BaseArray<S extends ArrayElement<S,M>, M extends BaseArray<S,M>> extends AbstractArray<M> implements FundamentalArray<S,M> {

  /** シリアル番号。 */
  private static final long serialVersionUID = 8830133755380267071L;

  /** 配列成分。 */
  protected S[][] elements;

  /**
   * 新しく生成された<code>ArrayObject</code>オブジェクトを初期化します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   */
  public BaseArray(final int rowSize, final int columnSize) {
    super(rowSize, columnSize);
  }

  /**
   * <code>elements</code>で与えられた行ベクトルを生成します。
   * 
   * @param elements ベクトルの成分をもつ配列
   */
  public BaseArray(final S[] elements) {
    super(elements.length == 0 ? 0 : 1, elements.length);
    
    this.elements = GridUtil.<S>createArray(1,  0, elements);
    this.elements[0] = elements;
  }

  /**
   * <code>elements</code>で与えられた成分をもつ配列を生成します。
   * 
   * @param elements 成分をもつ配列
   */
  public BaseArray(final S[][] elements) {
    this(elements.length, elements.length == 0 ? 0 : elements[0].length, elements);
  }

  /**
   * <code>elements</code>で与えられた成分をもつ <code>rowSize&nbsp;*&nbsp;columnSize</code> の配列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 成分をもつ配列
   */
  public BaseArray(final int rowSize, final int columnSize, final S[][] elements) {
    super(rowSize, columnSize);
    this.elements = elements;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object opponent) {
    if (this == opponent) {
      return true;
    }
    if (opponent == null) {
      return false;
    }
    if (opponent.getClass() != getClass()) {
      return false;
    }

    return GridUtil.equals(this.elements, ((M)opponent).getElements());
  }

  /**
   * Override hashCode.
   * 
   * @return the Objects hash code.
   */
  @Override
  public int hashCode() {
    int hashCode = super.hashCode();
    final int bitSize = 32;
    for (int i0 = 0; this.elements != null && i0 < this.elements.length; i0++) {
      for (int i1 = 0; this.elements != null && i1 < this.elements[0].length; i1++) {
        hashCode = (bitSize - 1) * hashCode + (this.elements[i0][i1].hashCode());
      }
    }
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone() {
    final M ans = (M)super.clone();
    ans.elements = GridUtil.clone(this.elements);
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final void exchangeRow(final int row1, final int row2) {
    if (row1 == row2) {
      return;
    }

    GridUtil.exchangeRow(this.elements, row1 - 1, row2 - 1);
  }

  /**
   * {@inheritDoc}
   */
  public final void exchangeColumn(final int column1, final int column2) {
    if (column1 == column2) {
      return;
    }

    GridUtil.exchangeColumn(this.elements, column1 - 1, column2 - 1);
  }

  /**
   * {@inheritDoc}
   */
  public final void copy(final M source) {
    GridUtil.copy(source.elements, this.elements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void copy(final M source) {
//    if (!(source instanceof FundamentalArray)) {
//      throw new IllegalArgumentException(Messages.getString("BaseArray.0")); //$NON-NLS-1$
//    }
//
//    copy((FundamentalArray<?, ?>)source);
//  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero() {
    return GridUtil.isZero(this.elements);
  }

  /**
   * {@inheritDoc}
   */
  public final M appendDown(final M value) {
    if (getColumnSize() != value.getColumnSize()) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_COLUMN_NUMBER);
    }

    S[][] ans = GridUtil.<S> appendDown(this.elements, value.getElements());
    return ans[0][0].createGrid(ans);
  }

  /**
   * {@inheritDoc}
   */
  public final M appendRight(final M value) {
    if (hasSameRowSize(value) == false) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_ROW_NUMBER);
    }

    if (this.getClass().equals(value.getClass())) {
      S[][] ans = GridUtil.<S> appendRight(this.elements, value.getElements());
      return ans[0][0].createGrid(ans);
    }
    throw new IllegalArgumentException(Messages.getString("BaseArray.2")); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final M transpose() {
    final S[][] ans = GridUtil.transpose(this.elements);
    return ans[0][0].createGrid(ans);
  }

  /**
   * {@inheritDoc}
   */
  public final void removeRowVectors(final int rowMin, final int rowMax) {
    this.elements = GridUtil.removeRowVectors(this.elements, rowMin - 1, rowMax - 1);
    setRowSize(getRowSize() - (rowMax - rowMin + 1));
  }

  /**
   * {@inheritDoc}
   */
  public final void removeRowVectors(final IntMatrix rowIndex) {
    if (rowIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseArray.3")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    final S[][] ans = GridUtil.removeRowVectors(this.elements, index);

    this.elements = ans;
    setRowSize(ans.length);
  }

  /**
   * {@inheritDoc}
   */
  public final void removeColumnVectors(final int columnMin, final int columnMax) {
    setColumnSize(getColumnSize() - (columnMax - columnMin + 1));
    this.elements = GridUtil.removeColumnVectors(this.elements, columnMin - 1, columnMax - 1);
  }

  /**
   * {@inheritDoc}
   */
  public final void removeColumnVectors(final IntMatrix columnIndex) {
    if (columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseArray.4")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    final S[][] ans = GridUtil.removeColumnVectors(this.elements, index);

    this.elements = ans;
    setColumnSize(ans[0].length);
  }

  /**
   * {@inheritDoc}
   */
  public final M getSubMatrix(final int rowMin, final int rowMax, final int columnMin, final int columnMax) {
    final S[][] ans = GridUtil.getSubMatrix(this.elements, rowMin - 1, rowMax - 1, columnMin - 1, columnMax - 1);
    return ans[0][0].createGrid(ans);
  }

  /**
   * {@inheritDoc}
   */
  public final M getSubMatrix(final IntMatrix rowIndex, final int columnMin, final int columnMax) {
    if (rowIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseArray.6")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    final S[][] ans = GridUtil.getSubMatrix(this.elements, index, columnMin - 1, columnMax - 1);
    return ans[0][0].createGrid(ans);
  }

  /**
   * {@inheritDoc}
   */
  public final M getSubMatrix(final int rowMin, final int rowMax, final IntMatrix columnIndex) {
    if (columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseArray.8")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    final S[][] ans = GridUtil.getSubMatrix(this.elements, rowMin - 1, rowMax - 1, index);
    return ans[0][0].createGrid(ans);
  }

  /**
   * {@inheritDoc}
   */
  public final M getSubMatrix(final IntMatrix rowIndex, final IntMatrix columnIndex) {
    if (rowIndex.getRowSize() != 1 || columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseArray.9")); //$NON-NLS-1$
    }

    final int[] rowIdx = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    final int[] columnIdx = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    final S[][] ans = GridUtil.getSubMatrix(this.elements, rowIdx, columnIdx);
    return ans[0][0].createGrid(ans);
  }

  /**
   * {@inheritDoc}
   */
  public final M getSubVector(final IntMatrix index) {
    if (index.getRowSize() == 0) {
      return this.elements[0][0].createGrid(this.elements[0][0].createArray(0));
    }

    if (index.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseArray.10")); //$NON-NLS-1$
    }

    final int[] idx = IntMatrixUtil.decrement(index.getIntElements()[0]);

    if (getColumnSize() == 1) {
      final S[] mat = ((transpose()).elements)[0];
      final S[] ans = GridUtil.getSubVector(mat, idx);
      return (ans[0].createGrid(ans)).transpose();
    } 
    
    if (getRowSize() == 1) {
      final S[] ans = GridUtil.getSubVector(this.elements[0], idx);
      return ans[0].createGrid(ans);
    } 

    throw new MatrixSizeException(MatrixSizeException.NOT_A_VECTOR_MATRIX);
  }

  /**
   * {@inheritDoc}
   */
  public final M diagonalToVector() {
    final S[] ans = GridUtil.diagonalToVector(this.elements);
    return ans[0].createGrid(ans).transpose();
  }

  /**
   * {@inheritDoc}
   */
  public final M vectorToDiagonal() {
    final S[][] ans;

    if (getRowSize() == 1) {
      ans = GridUtil.vectorToDiagonal(this.elements[0]);
    } else if (getColumnSize() == 1) {
      final S[][] vector = GridUtil.transpose(this.elements);
      ans = GridUtil.vectorToDiagonal(vector[0]);
    } else {
      throw new MatrixSizeException(Messages.getString("BaseArray.11")); //$NON-NLS-1$
    }

    
    return ans[0][0].createGrid(ans);
  }

  /**
   * 対角配列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * 
   * @param diagonalElements 対角成分
   * @return 対角配列
   */
  public static <S extends ArrayElement<S,M>, M extends Array<M>> M diagonal(final S[] diagonalElements) {
    final S[][] ans = GridUtil.diagonal(diagonalElements);
    return ans[0][0].createGrid(ans);
  }

  /**
   * {@inheritDoc}
   */
  public final M reshape(final int newRowSize, final int newColumnSize) {
    if (getRowSize() * getColumnSize() != newRowSize * newColumnSize) {
      throw new MatrixSizeException(""); //$NON-NLS-1$
    }

    final S[][] ans = GridUtil.reshape(this.elements, newRowSize, newColumnSize);
    return ans[0][0].createGrid(ans);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public final M resize(final int newRowSize, final int newColumnSize) {
    this.elements = GridUtil.resize(this.elements, newRowSize, newColumnSize);
    setRowSize(newRowSize);
    setColumnSize(newColumnSize);
    return (M)this;
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix compareElementWise(final String operator, final M opponent) {
    final boolean[][] ans = GridUtil.compareElementWise(this.elements, operator, opponent.elements);
    return new BooleanMatrix(ans);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final BooleanMatrix compareElementWise(final String operator, final M opponent) {
//    return compareElementWise(operator, (FundamentalArray<?, ?>)opponent);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public void setSubMatrix(int rowMinimum, int rowMaximum, int columnMinimum, int columnMaximum, FundamentalArray<?, ?> source) {
//    GridUtil.setSubMatrix(this.elements, rowMinimum - 1, rowMaximum - 1, columnMinimum - 1, columnMaximum - 1, ((BaseArray<?, ?>)source).elements);
//  }

  /**
   * {@inheritDoc}
   */
  public final void setSubMatrix(final IntMatrix rowIndex, final int columnMin, final int columnMax, final M source) {
    if (rowIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseArray.15")); //$NON-NLS-1$
    }

    int[] index = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    GridUtil.setSubMatrix(this.elements, index, columnMin - 1, columnMax - 1, source.getElements());
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubMatrix(final IntMatrix rowIndex, final int columnMin, final int columnMax, final M source) {
//    if (!(source instanceof FundamentalArray<?, ?>)) {
//      throw new IllegalArgumentException(Messages.getString("BaseArray.16")); //$NON-NLS-1$
//    }
//
//    setSubMatrix(rowIndex, columnMin, columnMax, source);
//  }

  /**
   * {@inheritDoc}
   */
  public final void setSubMatrix(final int rowMin, final int rowMax, final IntMatrix columnIndex, final M source) {
    if (columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseArray.17")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    GridUtil.setSubMatrix(this.elements, rowMin - 1, rowMax - 1, index, source.getElements());
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubMatrix(final int rowMin, final int rowMax, final IntMatrix columnIndex, final M source) {
//    if (!(source instanceof FundamentalArray<?, ?>)) {
//      throw new IllegalArgumentException(Messages.getString("BaseArray.18")); //$NON-NLS-1$
//    }
//    setSubMatrix(rowMin, rowMax, columnIndex, source);
//  }

  /**
   * {@inheritDoc}
   */
  public final void setSubMatrix(final IntMatrix rowIndex, final IntMatrix columnIndex, final M source) {
    if (rowIndex.getRowSize() != 1 || columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseArray.19")); //$NON-NLS-1$
    }

    final int[] rowIdx = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    final int[] colIdx = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    GridUtil.setSubMatrix(this.elements, rowIdx, colIdx, source.getElements());
  }

  /**
   * {@inheritDoc}
   */
  public final void setSubMatrix(final int rowMin, final int rowMax, final int columnMin, final int columnMax, final M source) {
    if ((rowMax - rowMin + 1) != source.getRowSize()) {
      throw new MatrixSizeException(Messages.getString("AbstractFundamentalMatrix.2")); //$NON-NLS-1$
    }
    if ((columnMax - columnMin + 1) != source.getColumnSize()) {
      throw new MatrixSizeException(Messages.getString("AbstractFundamentalMatrix.3")); //$NON-NLS-1$
    }

    GridUtil.setSubMatrix(this.elements, rowMin - 1, rowMax - 1, columnMin - 1, columnMax - 1, source.getElements());
  }

  
//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubMatrix(final IntMatrix rowIndex, final IntMatrix columnIndex, final M source) {
//    if (!(source instanceof FundamentalArray<?, ?>)) {
//      throw new IllegalArgumentException(Messages.getString("BaseArray.20")); //$NON-NLS-1$
//    }
//
//    setSubMatrix(rowIndex, columnIndex, )source);
//  }

  /**
   * {@inheritDoc}
   */
  public final void setSubVector(final IntMatrix index, final M source) {
    if (index.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseArray.21")); //$NON-NLS-1$
    }

    final int[] idx = IntMatrixUtil.decrement(index.getIntElements()[0]);
    GridUtil.setElements(this.elements, idx,  source.getElements());
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubVector(final IntMatrix index, final M source) {
//    if (!(source instanceof FundamentalArray<?, ?>)) {
//      throw new IllegalArgumentException(Messages.getString("BaseArray.22")); //$NON-NLS-1$
//    }
//
//    setSubVector(index, source);
//  }

  /**
   * {@inheritDoc}
   */
  public final S getElement(final int row, final int column) {
    return this.elements[row - 1][column - 1];
  }

  /**
   * {@inheritDoc}
   */
  public final S getElement(final int index) {
    return this.elements[(index - 1) / getColumnSize()][(index - 1) % getColumnSize()];
  }

  /**
   * 全ての成分の2次元配列を返します。
   * 
   * @return 全ての成分の2次元配列
   */
  protected S[][] getElements() {
    return this.elements;
  }

  /**
   * {@inheritDoc}
   */
  public final void setElement(final int row, final int column, final S value) {
    this.elements[row - 1][column - 1] = value.clone();
  }

  /**
   * {@inheritDoc}
   */
  public void printElements(final Writer output) {
    final int maxColumnSize = Integer.MAX_VALUE;
    printElements(output, maxColumnSize);
  }

  /**
   * {@inheritDoc}
   */
  public void printElements(final Writer output, final int maxColumnSize) {
    GridUtil.print(getElements(), output, getElementFormat(), getElementAlignment(), maxColumnSize);
  }
}
