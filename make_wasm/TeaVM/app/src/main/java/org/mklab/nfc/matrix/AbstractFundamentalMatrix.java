/**
 * $Id: AbstractFundamentalMatrix.java,v 1.9 2008/07/18 14:33:14 koga Exp $
 *
 * Copyright (C) 2004 Masanobu Koga. All rights reserved.
 */
package org.mklab.nfc.matrix;

import org.mklab.nfc.scalar.Scalar;

/**
 * 行列データを扱うための抽象クラスです。
 * 
 * @author koga
 * @version $Revision: 1.9 $
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public abstract class AbstractFundamentalMatrix<S extends Scalar<S,M>, M extends Matrix<S,M>> extends AbstractGrid<M> implements FundamentalMatrix<S,M> {

  /** シリアル番号。 */
  private static final long serialVersionUID = -5661296516948449176L;

  /**
   * 新しく生成された<code>AbstractMatrix</code>オブジェクトを初期化します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   */
  public AbstractFundamentalMatrix(final int rowSize, final int columnSize) {
    super(rowSize, columnSize);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public M appendDown(final M value) {
//    if (AbstractMatrix.isTransformableToSameClass((Matrix<?,?>)this, value)) {
//      final Matrix<?,?>[] mm = AbstractMatrix.transformToSameClass((Matrix<?,?>)this, value);
//      if (mm[0] instanceof DoubleMatrix) {
//        return (M)((DoubleMatrix)mm[0]).appendDown((DoubleMatrix)mm[1]);
//      } else if (mm[0] instanceof IntMatrix) {
//        return (M)((IntMatrix)mm[0]).appendDown((IntMatrix)mm[1]);
//      } else if (mm[0] instanceof TransformableMatrix<?,?>) {
//        return (M)((TransformableMatrix<?,?>)mm[0]).appendDown((TransformableMatrix<?,?>)mm[1]);
//      }
//    }
//
//    throw new IllegalArgumentException(Messages.getString("AbstractFundamentalMatrix.1")); //$NON-NLS-1$
//  }

//  /**
//   * 下側に行列<code>value</code>を付けた行列を生成します。
//   * 
//   * @param value 付ける行列
//   * @return 下側に<code>value</code>をつけた行列
//   */
//  public M appendDown(final M value) {
//    return (M)appendDown((Matrix<?,?>)value);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public M appendRight(final M value) {
//    if (AbstractMatrix.isTransformableToSameClass((Matrix<?,?>)this, value)) {
//      final Matrix<?,?>[] mm = AbstractMatrix.transformToSameClass((Matrix<?,?>)this, value);
//      if (mm[0] instanceof DoubleMatrix) {
//        return (M)((DoubleMatrix)mm[0]).appendRight((DoubleMatrix)mm[1]);
//      } else if (mm[0] instanceof IntMatrix) {
//        return (M)((IntMatrix)mm[0]).appendRight((IntMatrix)mm[1]);
//      } else if (mm[0] instanceof TransformableMatrix<?,?>) {
//        return (M)((TransformableMatrix<?,?>)mm[0]).appendRight((TransformableMatrix<?,?>)mm[1]);
//      }
//    }
//    
//    throw new IllegalArgumentException(Messages.getString("AbstractFundamentalMatrix.1")); //$NON-NLS-1$
//  }

//  /**
//   * 右側に<code>value</code>を付けた行列を生成します。
//   * 
//   * @param value 付ける複素数
//   * @return 右側に<code>value</code>を付けた行列
//   */
//  public M appendRight(final M value) {
//    return (M)appendRight((Matrix<?,?>)value);
//  }
  
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
  public final M getRowVector(final int row) {
    return getSubMatrix(row, row, 1, getColumnSize());
  }

  /**
   * {@inheritDoc}
   */
  public final M getRowVectors(final int min, final int max) {
    return getSubMatrix(min, max, 1, getColumnSize());
  }

  /**
   * {@inheritDoc}
   */
  public final M getRowVectors(final IntMatrix index) {
    return getSubMatrix(index, 1, getColumnSize());
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
  public final M getColumnVectors(final int min, final int max) {
    return getSubMatrix(1, getRowSize(), min, max);
  }

  /**
   * {@inheritDoc}
   */
  public final M getColumnVectors(final IntMatrix index) {
    return getSubMatrix(1, getRowSize(), index);
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
  public final M getSubVector(final int min, final int max) {
    return getSubVector(min, max, 1);
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
    final M ans = createClone();
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
  public void setRowVector(final int row, final M source) {
    if (getColumnSize() == 0 && source.getColumnSize() == 0) {
      return;
    }
    setSubMatrix(row, row, 1, getColumnSize(), source);
  }

  /**
   * {@inheritDoc}
   */
  public void setRowVectors(final int min, final int max, final M source) {
    if (getColumnSize() == 0 && source.getColumnSize() == 0) {
      return;
    }
    setSubMatrix(min, max, 1, getColumnSize(), source);
  }

  /**
   * {@inheritDoc}
   */
  public void setRowVectors(final IntMatrix index, final M source) {
    if (getColumnSize() == 0 && source.getColumnSize() == 0) {
      return;
    }
    setSubMatrix(index, 1, getColumnSize(), source);
  }

  /**
   * {@inheritDoc}
   */
  public void setColumnVector(final int column, final M source) {
    if (getRowSize() == 0 && source.getRowSize() == 0) {
      return;
    }
    setSubMatrix(1, getRowSize(), column, column, source);
  }

  /**
   * {@inheritDoc}
   */
  public void setColumnVectors(final int min, final int max, final M source) {
    if (getRowSize() == 0 && source.getRowSize() == 0) {
      return;
    }

    setSubMatrix(1, getRowSize(), min, max, source);
  }

  /**
   * {@inheritDoc}
   */
  public void setColumnVectors(final IntMatrix index, final M source) {
    if (getRowSize() == 0 && source.getRowSize() == 0) {
      return;
    }

    setSubMatrix(1, getRowSize(), index, source);
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
  public void setSubMatrix(final int row, final IntMatrix columnIndex, final M source) {
    setSubMatrix(row, row, columnIndex, source);
  }

  /**
   * {@inheritDoc}
   */
  public final void setSubVector(final int min, final int max, final int by, final M source) {
    setSubVector(IntMatrix.series(min, max, by), source);
  }
}