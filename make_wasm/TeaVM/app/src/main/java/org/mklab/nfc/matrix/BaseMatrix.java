/*
 * $Id: BaseMatrix.java,v 1.31 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.matrix;

import java.io.Writer;

import org.mklab.nfc.leq.GaussianEliminationElements;
import org.mklab.nfc.leq.GaussianEliminationSolver;
import org.mklab.nfc.scalar.DoubleNumberUtil;
import org.mklab.nfc.scalar.Scalar;


/**
 * {@link Scalar}を成分とする行列を表わすクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.31 $, 2004/07/05
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public abstract class BaseMatrix<S extends Scalar<S, M>, M extends BaseMatrixOperator<S, M>> extends AbstractMatrix<S, M> implements BaseMatrixOperator<S, M> {

  /** シリアル番号。 */
  private static final long serialVersionUID = 547879979862633150L;

  /** 行列成分。 */
  private S[][] elements;

  /**
   * Creates {@link BaseMatrix}.
   * 
   * @param matrix 行列
   */
  public BaseMatrix(M matrix) {
    this(matrix.getElements());
  }

  /**
   * 新しく生成された<code>MatrixObject</code>オブジェクトを初期化します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   */
  public BaseMatrix(final int rowSize, final int columnSize) {
    super(rowSize, columnSize);
  }

  /**
   * <code>elements</code>で与えられた成分をもつ行ベクトルを生成します。
   * 
   * @param elements ベクトルの成分をもつ配列
   */
  public BaseMatrix(final S[] elements) {
    super(elements.length == 0 ? 0 : 1, elements.length);

    this.elements = GridUtil.<S> createArray(1, 0, elements);
    this.elements[0] = elements;
  }

  /**
   * <code>elements</code>で与えられた成分をもつ行列を生成します。
   * 
   * @param elements 行列の成分をもつ配列
   */
  public BaseMatrix(final S[][] elements) {
    this(elements.length, elements.length == 0 ? 0 : elements[0].length, elements);
  }

  /**
   * <code>elements</code>で与えられた成分をもつ<code>rowSize</code>*<code>columnSize</code >の行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 成分を含む配列
   */
  public BaseMatrix(final int rowSize, final int columnSize, final S[][] elements) {
    super(rowSize, columnSize);

    if (elements == null) {
      throw new MatrixSizeException(MatrixSizeException.INCORRECT_SIZE);
    }
    if (rowSize != 0 && columnSize != 0 && rowSize != elements.length) {
      throw new MatrixSizeException(MatrixSizeException.INCORRECT_SIZE);
    }
    if (rowSize != 0 && columnSize != 0 && columnSize != elements[0].length) {
      throw new MatrixSizeException(MatrixSizeException.INCORRECT_SIZE);
    }

    //    if (rowSize != 0 && columnSize != 0) {
    //      this.elements = elements[0][0].createArray(elements);
    //    } else {
    //this.elements = GridUtil.<S> createSameClassArray(elements);
    this.elements = elements;
    //    }
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
    // if (opponent.getClass() != getClass()) return false;

    if ((opponent instanceof BaseMatrix<?, ?>) == false) {
      return false;
    }

    return GridUtil.equals(this.elements, ((M)opponent).getElements());
  }

  /**
   * Override hashCode.
   * 
   * @return the Objects hashcode.
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int hashCode = super.hashCode();

    for (int row = 0; this.elements != null && row < this.elements.length; row++) {
      for (int column = 0; this.elements != null && column < this.elements[0].length; column++) {
        hashCode = prime * hashCode + (this.elements[row][column].hashCode());
      }
    }
    return hashCode;
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public boolean equals(final M opponent, final double tolerance) {
  //    if (!(opponent instanceof BaseMatrix<?, ?>)) {
  //      return false;
  //    }
  //    return equals((BaseMatrix<?, ?>)opponent, tolerance);
  //  }

  /**
   * {@inheritDoc}
   */
  public boolean equals(final M opponent, final double tolerance) {
    return BaseMatrixUtil.equals(this.elements, opponent.getElements(), tolerance);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone() {
    M ans = (M)super.clone();
    ans.setElements(GridUtil.clone(this.elements));
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public void exchangeRow(final int row1, final int row2) {
    if (row1 == row2) {
      return;
    }

    GridUtil.exchangeRow(this.elements, row1 - 1, row2 - 1);
  }

  /**
   * {@inheritDoc}
   */
  public void exchangeColumn(final int column1, final int column2) {
    if (column1 == column2) {
      return;
    }

    GridUtil.exchangeColumn(this.elements, column1 - 1, column2 - 1);
  }

  /**
   * 行列<code>source</code>の成分をコピーします。
   * 
   * @param source コピー元の整数行列
   */
  public void copy(final IntMatrix source) {
    BaseMatrixUtil.copy(source.getIntElements(), this.elements);
  }

  /**
   * 行列<code>source</code>の成分をコピーします。
   * 
   * @param source コピー元の整数行列
   */
  public void copy(final DoubleMatrix source) {
    BaseMatrixUtil.copy(source.getDoubleElements(), this.elements);
  }

  /**
   * {@inheritDoc}
   */
  public void copy(final M source) {
    GridUtil.copy(source.getElements(), this.elements);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public void copy(final M source) {
  //    if (isSameSize(source) == false) {
  //      throw new MatrixSizeException(this, source, MatrixSizeException.NOT_SAME_SIZE);
  //    }
  //
  ////    if (source instanceof IntMatrix) {
  ////      copy((IntMatrix)source);
  ////      return;
  ////    }
  ////
  ////    if (source instanceof DoubleMatrix) {
  ////      copy((DoubleMatrix)source);
  ////      return;
  ////    }
  //
  //    if (source instanceof BaseMatrix<?, ?>) {
  //      copy((BaseMatrix<?, ?>)source);
  //      return;
  //    }
  //
  //    throw new IllegalArgumentException(Messages.getString("BaseMatrix.0")); //$NON-NLS-1$
  //  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isZero() {
    return GridUtil.isZero(this.elements);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public BaseMatrix<?, ?> appendDown(final TransformableMatrix<?,?> value) {
  //    if (value instanceof BaseMatrix<?, ?>) {
  //      return appendDown(value);
  //    }
  //
  //    throw new IllegalArgumentException();
  //  }

  /**
   * {@inheritDoc}
   */
  public M appendDown(final M value) {
    if (getColumnSize() != value.getColumnSize()) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_COLUMN_NUMBER);
    }

    S[][] ans = GridUtil.<S> appendDown(this.elements, value.getElements());
    return ans[0][0].createGrid(getRowSize() + value.getRowSize(), getColumnSize(), ans);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public BaseMatrix<?, ?> appendRight(final TransformableMatrix<?,?> value) {
  //    if (value instanceof BaseMatrix<?, ?>) {
  //      return appendRight((BaseMatrix<?, ?>)value);
  //    }
  //
  //    throw new IllegalArgumentException();
  //  }

  /**
   * {@inheritDoc}
   */
  public M appendRight(final M value) {
    if (hasSameRowSize(value) == false) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_ROW_NUMBER);
    }

    S[][] ans = GridUtil.<S> appendRight(this.elements, value.getElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize() + value.getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M transpose() {
    S[][] ans = GridUtil.transpose(this.elements);
    return ans[0][0].createGrid(getColumnSize(), getRowSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public void removeRowVectors(final int rowMin, final int rowMax) {
    this.elements = GridUtil.removeRowVectors(this.elements, rowMin - 1, rowMax - 1);
    setRowSize(getRowSize() - (rowMax - rowMin + 1));
  }

  /**
   * {@inheritDoc}
   */
  public void removeRowVectors(final IntMatrix rowIndex) {
    if (rowIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseMatrix.1")); //$NON-NLS-1$
    }

    int[] index = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    S[][] ans = GridUtil.removeRowVectors(this.elements, index);

    this.elements = ans;
    setRowSize(ans.length);
  }

  /**
   * {@inheritDoc}
   */
  public void removeColumnVectors(final int columnMin, final int columnMax) {
    this.elements = GridUtil.removeColumnVectors(this.elements, columnMin - 1, columnMax - 1);
    setColumnSize(getColumnSize() - (columnMax - columnMin + 1));
  }

  /**
   * {@inheritDoc}
   */
  public void removeColumnVectors(final IntMatrix columnIndex) {
    if (columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseMatrix.2")); //$NON-NLS-1$
    }

    int[] index = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    S[][] ans = GridUtil.removeColumnVectors(this.elements, index);

    this.elements = ans;
    setColumnSize(ans.length);
  }

  /**
   * {@inheritDoc}
   */
  public M getSubMatrix(final int rowMin, final int rowMax, final int columnMin, final int columnMax) {
    S[][] ans = GridUtil.getSubMatrix(this.elements, rowMin - 1, rowMax - 1, columnMin - 1, columnMax - 1);
    return ans[0][0].createGrid(rowMax - rowMin + 1, columnMax - columnMin + 1, ans);
  }

  /**
   * {@inheritDoc}
   */
  public M getSubMatrix(final IntMatrix rowIndex, final int columnMin, final int columnMax) {
    if (rowIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseMatrix.4")); //$NON-NLS-1$
    }

    int[] index = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    S[][] ans = GridUtil.getSubMatrix(this.elements, index, columnMin - 1, columnMax - 1);
    return ans[0][0].createGrid(rowIndex.length(), columnMax - columnMin + 1, ans);
  }

  /**
   * {@inheritDoc}
   */
  public M getSubMatrix(final int rowMin, final int rowMax, final IntMatrix columnIndex) {
    if (columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseMatrix.6")); //$NON-NLS-1$
    }

    int[] index = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    S[][] ans = GridUtil.getSubMatrix(this.elements, rowMin - 1, rowMax - 1, index);
    return ans[0][0].createGrid(rowMax - rowMin + 1, columnIndex.length(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M getSubMatrix(final IntMatrix rowIndex, final IntMatrix columnIndex) {
    if (rowIndex.getRowSize() != 1 || columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseMatrix.7")); //$NON-NLS-1$
    }

    int[] rowIdx = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    int[] columnIdx = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    S[][] ans = GridUtil.getSubMatrix(this.elements, rowIdx, columnIdx);
    return ans[0][0].createGrid(rowIndex.length(), columnIndex.length(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M getSubVector(final IntMatrix index) {
    if (index.getRowSize() == 0) {
      return this.elements[0][0].createGrid(this.elements[0][0].createArray(0));
    }

    if (index.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseMatrix.8")); //$NON-NLS-1$
    }

    final int[] idx = IntMatrixUtil.decrement(index.getIntElements()[0]);

    if (getColumnSize() == 1) {
      final S[] mat = transpose().getElements()[0];
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
  public M diagonalToVector() {
    S[] ans = GridUtil.diagonalToVector(this.elements);
    return ans[0].createGrid(ans).transpose();
  }

  /**
   * {@inheritDoc}
   */
  public M vectorToDiagonal() {
    final S[][] ans;

    if (getRowSize() == 1) {
      ans = GridUtil.vectorToDiagonal(this.elements[0]);
    } else if (getColumnSize() == 1) {
      S[][] vector = GridUtil.transpose(this.elements);
      ans = GridUtil.vectorToDiagonal(vector[0]);
    } else {
      throw new MatrixSizeException(Messages.getString("BaseMatrix.9")); //$NON-NLS-1$
    }

    int size = Math.max(getRowSize(), getColumnSize());
    return ans[0][0].createGrid(size, size, ans);
  }

  /**
   * 対角行列を生成します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param diagonalElements 対角成分
   * @return 対角行列
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> M diagonal(final S[] diagonalElements) {
    final S[][] ans = GridUtil.diagonal(diagonalElements);
    final int size = diagonalElements.length;
    return ans[0][0].createGrid(size, size, ans);
  }

  /**
   * {@inheritDoc}
   */
  public M reshape(final int newRowSize, final int newColumnSize) {
    if (getRowSize() * getColumnSize() != newRowSize * newColumnSize) {
      throw new MatrixSizeException(""); //$NON-NLS-1$
    }

    final S[][] ans = GridUtil.reshape(this.elements, newRowSize, newColumnSize);
    return ans[0][0].createGrid(newRowSize, newColumnSize, ans);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public M resize(final int newRowSize, final int newColumnSize) {
    this.elements = GridUtil.resize(this.elements, newRowSize, newColumnSize);
    setRowSize(newRowSize);
    setColumnSize(newColumnSize);
    return (M)this;
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public BooleanMatrix compareElementWise(final String operator, final M opponent) {
  //    if (opponent instanceof BaseMatrix<?, ?>) {
  //      return compareElementWise(operator, (BaseMatrix<?, ?>)opponent);
  //    }
  //
  ////    if (opponent instanceof DoubleMatrix) {
  ////      return compareElementWise(operator, (DoubleMatrix)opponent);
  ////    }
  ////
  ////    if (opponent instanceof IntMatrix) {
  ////      return compareElementWise(operator, (IntMatrix)opponent);
  ////    }
  //
  //    throw new IllegalArgumentException(Messages.getString("BaseMatrix.11")); //$NON-NLS-1$
  //  }

  /**
   * {@inheritDoc}
   */
  public BooleanMatrix compareElementWise(final String operator, final M opponent) {
    final boolean[][] ans = GridUtil.compareElementWise(this.elements, operator, opponent.getElements());
    return new BooleanMatrix(ans);
  }

  /**
   * 行列<code>opponent</code>の各成分と成分毎に<code>operator</code>で指定された演算子で比較し, それぞれの結果を成分とする{@link BooleanMatrix}を生成します。
   * 
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param opponent 比較対象
   * 
   * @return 比較結果を成分とする{@link BooleanMatrix}
   */
  public BooleanMatrix compareElementWise(final String operator, final DoubleMatrix opponent) {
    final boolean[][] ans = BaseMatrixUtil.compareElementWise(this.elements, operator, opponent.getDoubleElements());
    return new BooleanMatrix(ans);
  }

  /**
   * 行列<code>opponent</code>の各成分と成分毎に<code>operator</code>で指定された演算子で比較し, それぞれの結果を成分とする{@link BooleanMatrix}を生成します。
   * 
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param opponent 比較対象
   * 
   * @return 比較結果を成分とする{@link BooleanMatrix}
   */
  public BooleanMatrix compareElementWise(final String operator, final IntMatrix opponent) {
    final boolean[][] ans = BaseMatrixUtil.compareElementWise(this.elements, operator, opponent.getIntElements());
    return new BooleanMatrix(ans);
  }

  /**
   * {@inheritDoc}
   */
  public void setSubMatrix(final int rowMin, final int rowMax, final int columnMin, final int columnMax, final M source) {
    if ((rowMax - rowMin + 1) != source.getRowSize()) {
      throw new MatrixSizeException(Messages.getString("AbstractFundamentalMatrix.2")); //$NON-NLS-1$
    }

    if ((columnMax - columnMin + 1) != source.getColumnSize()) {
      throw new MatrixSizeException(Messages.getString("AbstractFundamentalMatrix.3")); //$NON-NLS-1$
    }

    //    if (source instanceof BaseMatrix<?, ?>) {
    BaseMatrixUtil.setSubMatrix(this.elements, rowMin - 1, rowMax - 1, columnMin - 1, columnMax - 1, source.getElements());
    //      return;
    //    } 

    //    if (source instanceof IntMatrix) {
    //      BaseMatrixUtil.setSubMatrix(this.elements, rowMin - 1, rowMax - 1, columnMin - 1, columnMax - 1, ((IntMatrix)source).getIntElements());
    //      return;
    //    } 
    //    
    //    if (source instanceof DoubleMatrix) {
    //      BaseMatrixUtil.setSubMatrix(this.elements, rowMin - 1, rowMax - 1, columnMin - 1, columnMax - 1, ((DoubleMatrix)source).getDoubleElements());
    //      return;
    //    }

    //    throw new IllegalArgumentException(Messages.getString("BaseMatrix.12")); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public void setSubMatrix(final IntMatrix rowIndex, final int columnMin, final int columnMax, final M source) {
    if (rowIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseMatrix.13")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);

    //    if (source instanceof BaseMatrix<?, ?>) {
    GridUtil.setSubMatrix(this.elements, index, columnMin - 1, columnMax - 1, source.getElements());
    //      return;
    //    } 
    //    
    //    if (source instanceof IntMatrix) {
    //      BaseMatrixUtil.setSubMatrix(this.elements, index, columnMin - 1, columnMax - 1, ((IntMatrix)source).getIntElements());
    //      return;
    //    }
    //    
    //    if (source instanceof DoubleMatrix) {
    //      BaseMatrixUtil.setSubMatrix(this.elements, index, columnMin - 1, columnMax - 1, ((DoubleMatrix)source).getDoubleElements());
    //      return;
    //    }
    //
    //    throw new IllegalArgumentException(Messages.getString("BaseMatrix.14")); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public void setSubMatrix(final int rowMin, final int rowMax, final IntMatrix columnIndex, final M source) {
    if (columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseMatrix.15")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);

    //    if (source instanceof BaseMatrix<?, ?>) {
    GridUtil.setSubMatrix(this.elements, rowMin - 1, rowMax - 1, index, source.getElements());
    //      return;
    //    } 
    //    
    //    if (source instanceof IntMatrix) {
    //      BaseMatrixUtil.setSubMatrix(this.elements, rowMin - 1, rowMax - 1, index, ((IntMatrix)source).getIntElements());
    //      return;
    //    } 
    //    
    //    if (source instanceof DoubleMatrix) {
    //      BaseMatrixUtil.setSubMatrix(this.elements, rowMin - 1, rowMax - 1, index, ((DoubleMatrix)source).getDoubleElements());
    //      return;
    //    }
    //
    //    throw new IllegalArgumentException(Messages.getString("BaseMatrix.16")); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public void setSubMatrix(final IntMatrix rowIndex, final IntMatrix columnIndex, final M source) {
    if (rowIndex.getRowSize() != 1 || columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseMatrix.17")); //$NON-NLS-1$
    }

    final int[] rowIdx = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    final int[] colIdx = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);

    //    if (source instanceof BaseMatrix<?, ?>) {
    GridUtil.setSubMatrix(this.elements, rowIdx, colIdx, source.getElements());
    //      return;
    //    } 
    //    
    //    if (source instanceof IntMatrix) {
    //      BaseMatrixUtil.setSubMatrix(this.elements, rowIdx, colIdx, ((IntMatrix)source).getIntElements());
    //      return;
    //    } 
    //    
    //    if (source instanceof DoubleMatrix) {
    //      BaseMatrixUtil.setSubMatrix(this.elements, rowIdx, colIdx, ((DoubleMatrix)source).getDoubleElements());
    //      return;
    //    }
    //
    //    throw new IllegalArgumentException(Messages.getString("BaseMatrix.18")); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public void setSubVector(final IntMatrix index, final M source) {
    if (index.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BaseMatrix.19")); //$NON-NLS-1$
    }

    final int[] idx = IntMatrixUtil.decrement(index.getIntElements()[0]);

    //    if (source instanceof BaseMatrix<?, ?>) {
    GridUtil.setElements(this.elements, idx, source.getElements());
    //      return;
    //    } 
    //    
    //    if (source instanceof IntMatrix) {
    //      BaseMatrixUtil.setElements(this.elements, idx, ((IntMatrix)source).getIntElements());
    //      return;
    //    } 
    //    
    //    if (source instanceof DoubleMatrix) {
    //      BaseMatrixUtil.setElements(this.elements, idx, ((DoubleMatrix)source).getDoubleElements());
    //      return;
    //    }
    //
    //    throw new IllegalArgumentException(Messages.getString("BaseMatrix.20")); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public void setSubVector(final int min, final int max, final M source) {
    if ((max - min) + 1 != source.getColumnSize()) {
      throw new MatrixSizeException(Messages.getString("BaseMatrix.19")); //$NON-NLS-1$
    }

    //    if (source instanceof BaseMatrix<?, ?>) {
    GridUtil.setSubVector(this.elements[0], min - 1, max - 1, source.getElements()[0]);
    //      return;
    //    } 
    //    
    //    if (source instanceof IntMatrix) {
    //      BaseMatrixUtil.setSubVector(this.elements[0], min - 1, max - 1, ((IntMatrix)source).getIntElements()[0]);
    //      return;
    //    } 
    //    
    //    if (source instanceof DoubleMatrix) {
    //      BaseMatrixUtil.setSubVector(this.elements[0], min - 1, max - 1, ((DoubleMatrix)source).getDoubleElements()[0]);
    //      return;
    //    }
    //
    //    throw new IllegalArgumentException(Messages.getString("BaseMatrix.20")); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public S getElement(final int row, final int column) {
    return this.elements[row - 1][column - 1];
  }

  /**
   * {@inheritDoc}
   */
  public S getElement(final int index) {
    return (this.elements[(index - 1) / getColumnSize()][(index - 1) % getColumnSize()]);
  }

  /**
   * 全ての成分の2次元配列を返します。
   * 
   * @return 全ての成分の2次元配列
   */
  public final S[][] getElements() {
    return this.elements;
  }

  /**
   * 全ての成分を設定します。
   * 
   * @param elements 全ての成分の2次元配列
   */
  public final void setElements(final S[][] elements) {
    this.elements = elements;
  }

  /**
   * {@inheritDoc}
   */
  public void setElement(final int row, final int column, final int value) {
    this.elements[row - 1][column - 1] = this.elements[0][0].create(value);
  }

  /**
   * {@inheritDoc}
   */
  public void setElement(final int row, final int column, final double value) {
    this.elements[row - 1][column - 1] = this.elements[0][0].create(value);
  }

  /**
   * {@inheritDoc}
   */
  public void setElement(final int row, final int column, final S value) {
    this.elements[row - 1][column - 1] = value.clone();
    //this.elements[row - 1][column - 1] = this.elements[0][0].transformFrom(value);
  }

  /**
   * {@inheritDoc}
   */
  public void setElement(final int index, final int value) {
    this.elements[(index - 1) / getColumnSize()][(index - 1) % getColumnSize()] = this.elements[0][0].create(value);
  }

  /**
   * {@inheritDoc}
   */
  public void setElement(final int index, final double value) {
    this.elements[(index - 1) / getColumnSize()][(index - 1) % getColumnSize()] = this.elements[0][0].create(value);
  }

  /**
   * {@inheritDoc}
   */
  public void setElement(final int index, final S value) {
    //this.elements[(index - 1) / getColumnSize()][(index - 1) % getColumnSize()] = this.elements[0][0].transformFrom(value);
    this.elements[(index - 1) / getColumnSize()][(index - 1) % getColumnSize()] = value.clone();
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public BaseMatrix<?, ?> add(final TransformableMatrix<?,?> value) {
  //    if (value instanceof BaseMatrix<?, ?>) {
  //      return add((BaseMatrix<?, ?>)value);
  //    }
  //
  //    throw new IllegalArgumentException();
  //  }

  /**
   * {@inheritDoc}
   */
  public M add(final M value) {
    if (isSameSize(value) == false) {
      throw new MatrixSizeException(Messages.getString("BaseMatrix.21")); //$NON-NLS-1$
    }

    final S[][] ans = BaseMatrixUtil.<S, M> add(this.elements, value.getElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public BaseMatrix<?, ?> subtract(final TransformableMatrix<?,?> value) {
  //    if (value instanceof BaseMatrix<?, ?>) {
  //      return subtract((BaseMatrix<?, ?>)value);
  //    }
  //
  //    throw new IllegalArgumentException();
  //  }

  /**
   * {@inheritDoc}
   */
  public M subtract(final M value) {
    if (isSameSize(value) == false) {
      throw new MatrixSizeException(Messages.getString("BaseMatrix.22")); //$NON-NLS-1$
    }

    final S[][] ans = BaseMatrixUtil.<S, M> subtract(this.elements, value.getElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M multiply(final S value) {
    final S[][] ans = BaseMatrixUtil.<S, M> multiply(this.elements, value);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M multiply(final int value) {
    final S[][] ans = BaseMatrixUtil.multiply(this.elements, value);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M multiply(final double value) {
    final S[][] ans = BaseMatrixUtil.multiply(this.elements, value);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public BaseMatrix<?, ?> multiply(final TransformableMatrix<?,?> value) {
  //    if (value instanceof BaseMatrix<?, ?>) {
  //      return multiply((BaseMatrix<?, ?>)value);
  //    }
  //
  //    throw new IllegalArgumentException();
  //  }

  /**
   * {@inheritDoc}
   */
  public M multiply(final M value) {
    if (getColumnSize() != value.getRowSize()) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_SIZE);
    }

    final S[][] ans = BaseMatrixUtil.<S, M> multiply(this.elements, value.getElements());
    return ans[0][0].createGrid(getRowSize(), value.getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M divide(final S value) {
    final S[][] ans = BaseMatrixUtil.divide(this.elements, value);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M divide(final int value) {
    final S[][] ans = BaseMatrixUtil.divide(this.elements, value);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M divide(final double value) {
    final S[][] ans = BaseMatrixUtil.divide(this.elements, value);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public BaseMatrix<?, ?> divide(final TransformableMatrix<?,?> value) {
  //    if (value instanceof BaseMatrix<?, ?>) {
  //      return divide((BaseMatrix<?, ?>)value);
  //    }
  //
  //    throw new IllegalArgumentException();
  //  }

  /**
   * {@inheritDoc}
   */
  public M divide(final M value) {
    if (getColumnSize() != value.getRowSize()) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_SIZE);
    }

    return this.multiply(value.inverse());
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public BaseMatrix<?, ?> leftDivide(final TransformableMatrix<?,?> value) {
  //    if (value instanceof BaseMatrix<?, ?>) {
  //      return leftDivide((BaseMatrix<?, ?>)value);
  //    }
  //
  //    throw new IllegalArgumentException();
  //  }

  /**
   * {@inheritDoc}
   */
  public M leftDivide(final M value) {
    if (getRowSize() != value.getRowSize()) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_SIZE);
    }

    return inverse().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public M unaryMinus() {
    final S[][] ans = BaseMatrixUtil.unaryMinus(this.elements);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M inverse() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    return inverse(DoubleNumberUtil.EPS, false);
  }

  /**
   * {@inheritDoc}
   */
  public M inverse(final double tolerance, final boolean stopIfSingular) {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    final GaussianEliminationElements<S, M> tmp = new GaussianEliminationSolver<S, M>().inverse(this.elements, tolerance, stopIfSingular);
    final S[][] ans = tmp.getInverse();
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M conjugate() {
    final S[][] ans = BaseMatrixUtil.conjugate(this.elements);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M conjugateTranspose() {
    final S[][] ans = BaseMatrixUtil.conjugateTranspose(this.elements);
    return ans[0][0].createGrid(getColumnSize(), getRowSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isZero(final double tolerance) {
    return BaseMatrixUtil.isZero(this.elements, tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isUnit(final double tolerance) {
    return BaseMatrixUtil.isUnit(this.elements, tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public M addElementWise(final S scalar) {
    final S[][] ans = BaseMatrixUtil.addElementWise(this.elements, scalar);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M addElementWise(final int value) {
    return addElementWise((double)value);
  }

  /**
   * {@inheritDoc}
   */
  public M addElementWise(final double value) {
    final S[][] ans = BaseMatrixUtil.addElementWise(this.elements, value);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M subtractElementWise(final int value) {
    return subtractElementWise((double)value);
  }

  /**
   * {@inheritDoc}
   */
  public M subtractElementWise(final S value) {
    final S[][] ans = BaseMatrixUtil.subtractElementWise(this.elements, value);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M subtractElementWise(final double value) {
    final S[][] ans = BaseMatrixUtil.subtractElementWise(this.elements, value);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public BaseMatrix<?, ?> multiplyElementWise(final TransformableMatrix<?,?> value) {
  //    if (value instanceof BaseMatrix<?, ?>) {
  //      return multiplyElementWise((BaseMatrix<?, ?>)value);
  //    }
  //
  //    throw new IllegalArgumentException();
  //  }

  /**
   * {@inheritDoc}
   */
  public M multiplyElementWise(final M value) {
    if (isSameSize(value) == false) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_SIZE);
    }

    final S[][] ans = BaseMatrixUtil.multiplyElementWise(this.elements, value.getElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public BaseMatrix<?, ?> divideElementWise(final TransformableMatrix<?,?> value) {
  //    if (value instanceof BaseMatrix<?, ?>) {
  //      return divideElementWise((BaseMatrix<?, ?>)value);
  //    }
  //
  //    throw new IllegalArgumentException();
  //  }

  /**
   * {@inheritDoc}
   */
  public M divideElementWise(final M value) {
    if (isSameSize(value) == false) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_SIZE);
    }

    final S[][] ans = BaseMatrixUtil.divideElementWise(this.elements, value.getElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public BaseMatrix<?, ?> leftDivideElementWise(final TransformableMatrix<?,?> value) {
  //    if (value instanceof BaseMatrix<?, ?>) {
  //      return leftDivideElementWise((BaseMatrix<?, ?>)value);
  //    }
  //
  //    throw new IllegalArgumentException();
  //  }

  /**
   * {@inheritDoc}
   */
  public M leftDivideElementWise(final M value) {
    if (isSameSize(value) == false) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_SIZE);
    }

    final S[][] ans = BaseMatrixUtil.leftDivideElementWise(this.elements, value.getElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public M leftDivideElementWise(final M value) {
  //    return inverseElementWise().multiplyElementWise(value);
  //    //return value.divideElementWise((M)this);
  //  }

  /**
   * {@inheritDoc}
   */
  public M inverseElementWise() {
    final S[][] ans = BaseMatrixUtil.inverseElementWise(this.elements);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M powerElementWise(final int num) {
    final S[][] ans = BaseMatrixUtil.powerElementWise(this.elements, num);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M powerElementWise(final IntMatrix matrix) {
    final S[][] ans = BaseMatrixUtil.powerElementWise(this.elements, matrix.getIntElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  //  public M powerElementWise(final M matrix) {
  //    final S[][] ans = BaseMatrixUtil.powerElementWise(this.elements, matrix.getElements());
  //    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  //  }

  /**
   * {@inheritDoc}
   */
  public BooleanMatrix isNanElementWise() {
    return new BooleanMatrix(BaseMatrixUtil.isNanElementWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public BooleanMatrix isFiniteElementWise() {
    return new BooleanMatrix(BaseMatrixUtil.isFiniteElementWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public BooleanMatrix isInfiniteElementWise() {
    return new BooleanMatrix(BaseMatrixUtil.isInfiniteElementWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public BooleanMatrix compareElementWise(final String operator, final int value) {
    return new BooleanMatrix(BaseMatrixUtil.compareElementWise(this.elements, operator, value));
  }

  /**
   * {@inheritDoc}
   */
  public BooleanMatrix compareElementWise(final String operator, final double value) {
    return new BooleanMatrix(BaseMatrixUtil.compareElementWise(this.elements, operator, value));
  }

  /**
   * {@inheritDoc}
   */
  public BooleanMatrix compareElementWise(final String operator, final S value) {
    return new BooleanMatrix(BaseMatrixUtil.compareElementWise(this.elements, operator, value));
  }

  /**
   * {@inheritDoc}
   */
  public M sumColumnWise() {
    final S[][] ans = BaseMatrixUtil.sumColumnWise(this.elements);
    return ans[0][0].createGrid(Math.min(1, getRowSize()), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M sumRowWise() {
    final S[][] ans = BaseMatrixUtil.sumRowWise(this.elements);
    return ans[0][0].createGrid(getRowSize(), Math.min(1, getColumnSize()), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M cumulativeSum() {
    final S[][] ans = BaseMatrixUtil.cumulativeSum(this.elements);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M cumulativeSumRowWise() {
    final S[][] ans = BaseMatrixUtil.cumulativeSumRowWise(this.elements);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
    //return (M)ans[0][0].createGrid(getRowSize(), Math.min(1, getColumnSize()), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M cumulativeSumColumnWise() {
    final S[][] ans = BaseMatrixUtil.cumulativeSumColumnWise(this.elements);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
    //return (M)ans[0][0].createGrid(Math.min(1, getRowSize()), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M productRowWise() {
    final S[][] ans = BaseMatrixUtil.productRowWise(this.elements);
    return ans[0][0].createGrid(getRowSize(), Math.min(1, getColumnSize()), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M productColumnWise() {
    final S[][] ans = BaseMatrixUtil.productColumnWise(this.elements);
    return ans[0][0].createGrid(Math.min(1, getRowSize()), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M cumulativeProduct() {
    final S[][] ans = BaseMatrixUtil.cumulativeProduct(this.elements);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M cumulativeProductRowWise() {
    final S[][] ans = BaseMatrixUtil.cumulativeProductRowWise(this.elements);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
    //return (M)ans[0][0].createGrid(getRowSize(), Math.min(1, getColumnSize()), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M cumulativeProductColumnWise() {
    final S[][] ans = BaseMatrixUtil.cumulativeProductColumnWise(this.elements);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
    //return (M)ans[0][0].createGrid(Math.min(1, getRowSize()), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M meanRowWise() {
    final S[][] ans = BaseMatrixUtil.meanRowWise(this.elements);
    return ans[0][0].createGrid(getRowSize(), Math.min(1, getColumnSize()), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M meanColumnWise() {
    final S[][] ans = BaseMatrixUtil.meanColumnWise(this.elements);
    return ans[0][0].createGrid(Math.min(1, getRowSize()), getColumnSize(), ans);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public BaseMatrix<?, ?> covariance(final TransformableMatrix<?,?> value) {
  //    if (value instanceof BaseMatrix<?, ?>) {
  //      return covariance((BaseMatrix<?, ?>)value);
  //    }
  //
  //    throw new IllegalArgumentException();
  //  }

  /**
   * {@inheritDoc}
   */
  public M covariance(final M opponent) {
    final S[][] ans = BaseMatrixUtil.<S, M> covariance(this.elements, opponent.getElements());
    return ans[0][0].createGrid(Math.max(2, getRowSize()), Math.max(2, getColumnSize()), ans);
  }

  /**
   * {@inheritDoc}
   */
  public S sum() {
    return BaseMatrixUtil.sum(this.elements);
  }

  /**
   * {@inheritDoc}
   */
  public S mean() {
    return BaseMatrixUtil.mean(this.elements);
  }

  /**
   * {@inheritDoc}
   */
  public S variance() {
    return BaseMatrixUtil.variance(this.elements);
  }

  /**
   * {@inheritDoc}
   */
  public S trace() {
    return BaseMatrixUtil.trace(this.elements);
  }

  /**
   * {@inheritDoc}
   */
  public S product() {
    return BaseMatrixUtil.product(this.elements);
  }

  /**
   * {@inheritDoc}
   */
  public S determinant() {
    if (isSquare() == false) {
      throw new MatrixSizeException(Messages.getString("BaseMatrix.23")); //$NON-NLS-1$
    }

    return BaseMatrixUtil.determinant(this.elements);
  }

  /**
   * 成分毎計算した関数の値を成分とする行列を生成します。
   * 
   * @param function 複素数関数
   * @return 成分毎計算した関数の値を成分とする行列
   */
  private M elementWiseFunction(final ScalarFunction<S, M> function) {
    final S[][] ans = BaseMatrixUtil.elementWiseFunction(this.elements, function);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M ceilElementWise() {
    return elementWiseFunction(new CeilFunction());
  }

  /**
   * @author koga
   * @version $Revision: 1.31 $, 2006/08/31
   */
  class CeilFunction implements ScalarFunction<S, M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.ceil();
    }
  }

  /**
   * {@inheritDoc}
   */
  public M floorElementWise() {
    return elementWiseFunction(new FloorFunction<S, M>());
  }

  /**
   * @author koga
   * @version $Revision: 1.31 $, 2006/08/31
   * @param <S> 成分の型
   * @param <M> 行列の型
   */
  static class FloorFunction<S extends Scalar<S, M>, M extends Matrix<S, M>> implements ScalarFunction<S, M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.floor();
    }
  }

  /**
   * {@inheritDoc}
   */
  public M fixElementWise() {
    return elementWiseFunction(new FixFunction<S, M>());
  }

  /**
   * @author koga
   * @version $Revision: 1.31 $, 2006/08/31
   * @param <S> スカラーの型
   * @param <M> 行列の型
   */
  static class FixFunction<S extends Scalar<S, M>, M extends Matrix<S, M>> implements ScalarFunction<S, M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.fix();
    }
  }

  /**
   * {@inheritDoc}
   */
  public M roundToZeroElementWise(final double tolerance) {
    final S[][] ans = BaseMatrixUtil.roundToZeroElementWise(this.elements, tolerance);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M roundToZeroElementWise() {
    return roundToZeroElementWise(DoubleNumberUtil.EPS);
  }

  /**
   * {@inheritDoc}
   */
  public M roundElementWise() {
    return elementWiseFunction(new RoundFunction<S, M>());
  }

  /**
   * @author koga
   * @version $Revision: 1.31 $, 2006/08/31
   * @param <S> スカラーの型
   * @param <M> 行列の型
   */
  static class RoundFunction<S extends Scalar<S, M>, M extends Matrix<S, M>> implements ScalarFunction<S, M> {

    /**
     * {@inheritDoc}
     */
    public S evaluate(final S argument) {
      return argument.round();
    }
  }

  /**
   * {@inheritDoc}
   */
  public M createUnit(final int rowSize, final int columnSize) {
    final S[][] ans = BaseMatrixUtil.createUnit(this.elements, rowSize, columnSize);
    return this.elements[0][0].createGrid(rowSize, columnSize, ans);
  }

  /**
   * {@inheritDoc}
   */
  public M createZero(final int rowSize, final int columnSize) {
    final S[][] ans = GridUtil.createZero(this.elements, rowSize, columnSize);
    return this.elements[0][0].createGrid(rowSize, columnSize, ans);
  }

  /**
   * {@inheritDoc}
   */
  public M createOnes(final int rowSize, final int columnSize) {
    final S[][] ans = BaseMatrixUtil.createOnes(this.elements, rowSize, columnSize);
    return this.elements[0][0].createGrid(rowSize, columnSize, ans);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isComplex() {
    return BaseMatrixUtil.isComplex(getElements());
  }

  /**
   * {@inheritDoc}
   */
  public boolean isReal() {
    return !isComplex();
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public Matrix<?,?> getRealPart() {
  //    final Scalar<?,?>[][] ans = BaseMatrixUtil.getRealPartElements(getElements());
  //    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  //  }
  //  
  //  /**
  //   * 実部の成分を返します。
  //   * 
  //   * @return 実部の成分
  //   */
  //  protected Scalar<?,?>[][] getRealPartElements() {
  //    final Scalar<?,?>[][] ans = BaseMatrixUtil.getRealPartElements(getElements());
  //    return ans;
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public Matrix<?,?> getImaginaryPart() {
  //    final Scalar<?,?>[][] ans = BaseMatrixUtil.getImagPartElements(getElements());
  //    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  //  }
  //  
  //  /**
  //   * 虚部の成分を返します。
  //   * 
  //   * @return 虚部の成分
  //   */
  //  protected Scalar<?,?>[][] getImaginaryPartElements() {
  //    final Scalar<?,?>[][] ans = BaseMatrixUtil.getImagPartElements(getElements());
  //    return ans;
  //  }
  //  
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public void setRealPart(final Matrix<?,?> realPart) {
  //    if (realPart instanceof IntMatrix) {
  //      setRealPart((IntMatrix)realPart);
  //      return;
  //    }
  //    if (realPart instanceof DoubleMatrix) {
  //      setRealPart((DoubleMatrix)realPart);
  //      return;
  //    }
  //    if (realPart instanceof BaseMatrix<?, ?>) {
  //      setRealPart((BaseMatrix<?, ?>)realPart);
  //      return;
  //    }
  //
  //    throw new IllegalArgumentException(Messages.getString("BaseMatrix.24")); //$NON-NLS-1$
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public void setRealPart(final IntMatrix realPart) {
  //    BaseMatrixUtil.setRealPartElements(getElements(), realPart.getIntElements());
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public void setRealPart(final DoubleMatrix realPart) {
  //    BaseMatrixUtil.setRealPartElements(getElements(), realPart.getDoubleElements());
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public void setRealPart(final BaseMatrix<?, ?> realPart) {
  //    BaseMatrixUtil.setRealPartElements(getElements(), realPart.getElements());
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public void setImaginaryPart(final Matrix<?,?> imagPart) {
  //    if (imagPart instanceof IntMatrix) {
  //      setImaginaryPart((IntMatrix)imagPart);
  //      return;
  //    }
  //    if (imagPart instanceof DoubleMatrix) {
  //      setImaginaryPart((DoubleMatrix)imagPart);
  //      return;
  //    }
  //    if (imagPart instanceof BaseMatrix<?, ?>) {
  //      setImaginaryPart((BaseMatrix<?, ?>)imagPart);
  //      return;
  //    }
  //
  //    throw new IllegalArgumentException(Messages.getString("BaseMatrix.25")); //$NON-NLS-1$
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public void setImaginaryPart(final IntMatrix imaginaryPart) {
  //    BaseMatrixUtil.setImagPartElements(getElements(), imaginaryPart.getIntElements());
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public void setImaginaryPart(final DoubleMatrix imaginaryPart) {
  //    BaseMatrixUtil.setImagPartElements(getElements(), imaginaryPart.getDoubleElements());
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public void setImaginaryPart(final BaseMatrix<?, ?> imaginaryPart) {
  //    BaseMatrixUtil.setImagPartElements(getElements(), imaginaryPart.getElements());
  //  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public BaseMatrix<?,?> toComplex() {
  //    final Scalar<?,?>[][] ans = BaseMatrixUtil.toComplexElements(getElements());
  //    return (BaseMatrix<?,?>)ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  //  }

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

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @Override
  //  public boolean isTransformableFrom(final Matrix<?,?> value) {
  //    if (super.isTransformableFrom(value)) {
  //      return true;
  //    }
  //
  //    return false;
  //  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @Override
  //  public M transformFrom(final Matrix<?,?> value) {
  //    if (super.isTransformableFrom(value)) {
  //      return super.transformFrom(value);
  //    }
  //
  //    throw new IllegalArgumentException(Messages.getString("BaseMatrix.26") + value); //$NON-NLS-1$
  //  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @Override
  //  public boolean isTransformableTo(final Matrix<?,?> value) {
  //    if (super.isTransformableTo(value)) {
  //      return true;
  //    }
  //
  //    return false;
  //  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @Override
  //  public Matrix<?,?> transformTo(final Matrix<?,?> value) {
  //    if (super.isTransformableTo(value)) {
  //      return super.transformTo(value);
  //    }
  //
  //    throw new IllegalArgumentException(Messages.getString("BaseMatrix.27") + value); //$NON-NLS-1$
  //  }

  /**
   * {@inheritDoc}
   */
  public String toString(String format) {
    return GridUtil.toString(getElements(), format);
  }
}
