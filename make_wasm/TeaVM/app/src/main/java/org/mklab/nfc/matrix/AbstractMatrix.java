/*
 * $Id: AbstractMatrix.java,v 1.66 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

import org.mklab.nfc.scalar.Scalar;


/**
 * 行列を統一的に扱うためのクラスです。
 * 
 * @author koga
 * @version $Revision: 1.66 $
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public abstract class AbstractMatrix<S extends Scalar<S,M>, M extends Matrix<S,M>> extends AbstractFundamentalMatrix<S,M> implements Matrix<S,M> {

  /** シリアル番号。 */
  private static final long serialVersionUID = 1962484570705012850L;

  /**
   * 子クラスから呼ばれ <code>rowSize&nbsp;*&nbsp;columnSize</code> の行列を生成します。
   * 
   * <p>(実際には作成されずに、成分数を <code>rowSize&nbsp;*&nbsp;columnSize</code> にするだけです。)
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * 
   */
  protected AbstractMatrix(final int rowSize, final int columnSize) {
    super(rowSize, columnSize);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isZero() {
    return isZero(0);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit() {
    return isUnit(0);
  }

  /**
   * {@inheritDoc}
   */
  public final M createZero() {
    return createZero(getRowSize(), getColumnSize());
  }

  /**
   * {@inheritDoc}
   */
  public final M createZero(final int size) {
    return createZero(size, size);
  }

  /**
   * {@inheritDoc}
   */
  public final M createZero(final int rowNumber, final int columnNumber, final Grid block) {
    return createZero(block.getRowSize() * rowNumber, block.getColumnSize() * columnNumber);
  }

  /**
   * {@inheritDoc}
   */
  public final M createUnit() {
    return createUnit(getRowSize(), getColumnSize());
  }

  /**
   * {@inheritDoc}
   */
  public final M createUnit(final int size) {
    return createUnit(size, size);
  }

  /**
   * {@inheritDoc}
   */
  public final M createUnit(final int rowNumber, final int columnNumber, final Grid block) {
    return createUnit(block.getRowSize() * rowNumber, block.getColumnSize() * columnNumber);
  }

  /**
   * {@inheritDoc}
   */
  public final M createOnes() {
    return createOnes(getRowSize(), getColumnSize());
  }

  /**
   * {@inheritDoc}
   */
  public final M createOnes(final int size) {
    return createOnes(size, size);
  }

  /**
   * {@inheritDoc}
   */
  public final M createOnes(final int rowNumber, final int columnNumber, final Grid block) {
    return createOnes(block.getRowSize() * rowNumber, block.getColumnSize() * columnNumber);
  }

  /**
   * {@inheritDoc}
   */
  public final M shiftUp(final int number) {
    if (number == 0) {
      return createClone();
    }
    if (number > 0) {
      M upper = getRowVectors(number + 1, getRowSize());
      M lower = createZero(number, getColumnSize());
      return upper.appendDown(lower);
    }
    M upper = createZero(Math.abs(number), getColumnSize());
    M lower = getRowVectors(1, getRowSize() + number);
    return upper.appendDown(lower);
  }

  /**
   * {@inheritDoc}
   */
  public final M shiftLeft(final int number) {
    if (number == 0) {
      return createClone();
    }
    if (number > 0) {
      M left = getColumnVectors(number + 1, getColumnSize());
      M right = createZero(getRowSize(), number);
      return left.appendRight(right);
    }
    M left = createZero(getRowSize(), Math.abs(number));
    M right = getColumnVectors(1, getColumnSize() + number);
    return left.appendRight(right);
  }

  /**
   * {@inheritDoc}
   */
  public M leftDivide(final int value) {
    return inverse().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public M leftDivide(final double value) {
    return inverse().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public M leftDivide(final S value) {
    return inverse().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public M power(final int order) {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    if (order <= -2) {
      return inverse().power(-order);
    }
    if (order == -1) {
      return inverse();
    }
    if (order == 0) {
      return createUnit();
    }
    if (order == 1) {
      return (createClone());
    }

    int number = order;

    M a = (M)this;
    M ans;
    M b = createUnit();

    while (true) {
      if ((number % 2) != 0) {
        ans = b.multiply(a);
        number /= 2;
        if (number != 0) {
          b = ans.createClone();
          a = a.multiply(a);
        } else {
          break;
        }
      } else {
        number /= 2;
        a = a.multiply(a);
      }
    }

    return ans;
  }

//  /**
//   * {@inheritDoc}
//   */
//  public boolean isTransformableTo(final Matrix<?,?> value) {
//    if (this.getClass().equals(value.getClass())) {
//      return true;
//    }
//
//    return false;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public Matrix<?,?> transformTo(final Matrix<?,?> value) {
//    if (this.getClass().equals(value.getClass())) {
//      return (Matrix<?,?>)this.clone();
//    }
//
//    throw new IllegalArgumentException(Messages.getString("AbstractMatrix.12") + value); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public boolean isTransformableFrom(final Matrix<?,?> value) {
//    if (value == null) {
//      return false;
//    }
//    
//    if (this.getClass().equals(value.getClass())) {
//      return true;
//    }
//
//    return false;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @SuppressWarnings("unchecked")
//  public M transformFrom(final Matrix<?,?> value) {
//    if (this.getClass().equals(value.getClass())) {
//      return (M)value.clone();
//    }
//
//    throw new IllegalArgumentException(Messages.getString("AbstractMatrix.11") + value); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public Matrix<?,?> powerElementWise(final Matrix<?,?> order) {
//    if (order instanceof IntMatrix) {
//      return powerElementWise((IntMatrix)order);
//    }
//
//    throw new RuntimeException(Messages.getString("AbstractMatrix.10")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public M add(final M value) {
//    if (AbstractMatrix.isTransformableToSameClass(this, value)) {
//      final Matrix<?,?>[] mm = AbstractMatrix.transformToSameClass(this, value);
//
//      if (mm[0] instanceof DoubleMatrix) {
//        return (M)((DoubleMatrix)mm[0]).add((DoubleMatrix)mm[1]);
//      }
//      if (mm[0] instanceof IntMatrix) {
//        return (M)((IntMatrix)mm[0]).add((IntMatrix)mm[1]);
//      }
//      if (mm[0] instanceof TransformableMatrix<?,?>) {
//        return (M)((TransformableMatrix<?,?>)mm[0]).add((TransformableMatrix<?,?>)mm[1]);
//      }
//    }
//
//    throw new IllegalArgumentException(Messages.getString("AbstractMatrix.0")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public M subtract(final M value) {
//    if (AbstractMatrix.isTransformableToSameClass(this, value)) {
//      final Matrix<?,?>[] mm = AbstractMatrix.transformToSameClass(this, value);
//      if (mm[0] instanceof DoubleMatrix) {
//        return (M)((DoubleMatrix)mm[0]).subtract((DoubleMatrix)mm[1]);
//      }
//      if (mm[0] instanceof IntMatrix) {
//        return (M)((IntMatrix)mm[0]).subtract((IntMatrix)mm[1]);
//      }
//      if (mm[0] instanceof TransformableMatrix<?,?>) {
//        return (M)((TransformableMatrix<?,?>)mm[0]).subtract((TransformableMatrix<?,?>)mm[1]);
//      }
//    }
//
//    throw new IllegalArgumentException(Messages.getString("AbstractMatrix.1")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public M multiply(final M value) {
//    if (AbstractMatrix.isTransformableToSameClass(this, value)) {
//      final Matrix<?,?>[] mm = AbstractMatrix.transformToSameClass(this, value);
//      if (mm[0] instanceof DoubleMatrix) {
//        return (M)((DoubleMatrix)mm[0]).multiply((DoubleMatrix)mm[1]);
//      }
//      if (mm[0] instanceof IntMatrix) {
//        return (M)((IntMatrix)mm[0]).multiply((IntMatrix)mm[1]);
//      }
//      if (mm[0] instanceof TransformableMatrix<?,?>) {
//        return (M)((TransformableMatrix<?,?>)mm[0]).multiply((TransformableMatrix<?,?>)mm[1]);
//      }
//    }
//
//    throw new IllegalArgumentException(Messages.getString("AbstractMatrix.2")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public M divide(final M value) {
//    if (AbstractMatrix.isTransformableToSameClass(this, value)) {
//      final Matrix<?,?>[] mm = AbstractMatrix.transformToSameClass(this, value);
//      if (mm[0] instanceof DoubleMatrix) {
//        return (M)((DoubleMatrix)mm[0]).divide((DoubleMatrix)mm[1]);
//      }
//      if (mm[0] instanceof IntMatrix) {
//        return (M)((IntMatrix)mm[0]).divide((IntMatrix)mm[1]);
//      }
//      if (mm[0] instanceof TransformableMatrix<?,?>) {
//        return (M)((TransformableMatrix<?,?>)mm[0]).divide((TransformableMatrix<?,?>)mm[1]);
//      }
//    }
//
//    throw new IllegalArgumentException(Messages.getString("AbstractMatrix.3")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public M leftDivide(final M value) {
//    if (AbstractMatrix.isTransformableToSameClass(this, value)) {
//      final Matrix<?,?>[] mm = AbstractMatrix.transformToSameClass(this, value);
//      if (mm[0] instanceof DoubleMatrix) {
//        return (M)((DoubleMatrix)mm[0]).leftDivide((DoubleMatrix)mm[1]);
//      }
//      if (mm[0] instanceof IntMatrix) {
//        return (M)((IntMatrix)mm[0]).leftDivide((IntMatrix)mm[1]);
//      }
//      if (mm[0] instanceof TransformableMatrix<?,?>) {
//        return (M)((TransformableMatrix<?,?>)mm[0]).leftDivide((TransformableMatrix<?,?>)mm[1]);
//      }
//    }
//
//    throw new IllegalArgumentException(Messages.getString("AbstractMatrix.4")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public M multiplyElementWise(final M value) {
//    if (AbstractMatrix.isTransformableToSameClass(this, value)) {
//      final Matrix<?,?>[] mm = AbstractMatrix.transformToSameClass(this, value);
//      if (mm[0] instanceof DoubleMatrix) {
//        return (M)((DoubleMatrix)mm[0]).multiplyElementWise((DoubleMatrix)mm[1]);
//      }
//      if (mm[0] instanceof IntMatrix) {
//        return (M)((IntMatrix)mm[0]).multiplyElementWise((IntMatrix)mm[1]);
//      }
//      if (mm[0] instanceof TransformableMatrix<?,?>) {
//        return (M)((TransformableMatrix<?,?>)mm[0]).multiplyElementWise((TransformableMatrix<?,?>)mm[1]);
//      }
//    }
//
//    throw new IllegalArgumentException(Messages.getString("AbstractMatrix.7")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public M divideElementWise(final M value) {
//    if (AbstractMatrix.isTransformableToSameClass(this, value)) {
//      final Matrix<?,?>[] mm = AbstractMatrix.transformToSameClass(this, value);
//      if (mm[0] instanceof DoubleMatrix) {
//        return (M)((DoubleMatrix)mm[0]).divideElementWise((DoubleMatrix)mm[1]);
//      }
//      if (mm[0] instanceof IntMatrix) {
//        return (M)((IntMatrix)mm[0]).divideElementWise((IntMatrix)mm[1]);
//      }
//      if (mm[0] instanceof TransformableMatrix<?,?>) {
//        return (M)((TransformableMatrix<?,?>)mm[0]).divideElementWise((TransformableMatrix<?,?>)mm[1]);
//      }
//    }
//
//    throw new IllegalArgumentException(Messages.getString("AbstractMatrix.8")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public M covariance(final M value) {
//    if (AbstractMatrix.isTransformableToSameClass(this, value)) {
//      final Matrix<?,?>[] mm = AbstractMatrix.transformToSameClass(this, value);
//      if (mm[0] instanceof DoubleMatrix) {
//        return (M)((DoubleMatrix)mm[0]).covariance((DoubleMatrix)mm[1]);
//      }
//      if (mm[0] instanceof IntMatrix) {
//        return (M)((IntMatrix)mm[0]).covariance((IntMatrix)mm[1]);
//      }
//      if (mm[0] instanceof TransformableMatrix<?,?>) {
//        return (M)((TransformableMatrix<?,?>)mm[0]).covariance((TransformableMatrix<?,?>)mm[1]);
//      }
//    }
//
//    throw new IllegalArgumentException(Messages.getString("AbstractMatrix.6")); //$NON-NLS-1$
//  }

//  /**
//   * 2個の行列を同じ型の行列に変換できるか判定します。
//   * 
//   * @param m1 行列1
//   * @param m2 行列2
//   * @return 同じ型の行列に変換できるならばtrue、そうでなければfalse
//   */
//  protected static boolean isTransformableToSameClass(final Matrix<?,?> m1, final Matrix<?,?> m2) {
//    if (m1.getClass().equals(m2.getClass())) {
//      return true;
//    }
//  
//    if (m1.isTransformableFrom(m2)) {
//      return true;
//    }
//  
//    if (m1.isTransformableTo(m2)) {
//      return true;
//    }
//  
//    if (m2.isTransformableFrom(m1)) {
//      return true;
//    }
//  
//    if (m2.isTransformableTo(m1)) {
//      return true;
//    }
//  
//    return false;
//  }

//  /**
//   * 2個の行列を同じ型の行列に変換します。
//   * 
//   * @param m1 行列1
//   * @param m2 行列2
//   * @return 同じ型の行列を成分とする配列
//   */
//  protected static Matrix<?,?>[] transformToSameClass(final Matrix<?,?> m1, final Matrix<?,?> m2) {
//    if (m1.getClass().equals(m2.getClass())) {
//      return new Matrix<?,?>[] {m1, m2};
//    }
//  
//    if (m1.isTransformableFrom(m2)) {
//      return new Matrix<?,?>[] {m1, m1.transformFrom(m2)};
//    }
//  
//    if (m1.isTransformableTo(m2)) {
//      return new Matrix<?,?>[] {m1.transformTo(m2), m2};
//    }
//  
//    if (m2.isTransformableFrom(m1)) {
//      return new Matrix<?,?>[] {m2.transformFrom(m1), m2};
//    }
//  
//    if (m2.isTransformableTo(m1)) {
//      return new Matrix<?,?>[] {m1, m2.transformTo(m1)};
//    }
//  
//    throw new IllegalArgumentException(Messages.getString("AbstractMatrix.13")); //$NON-NLS-1$
//  }
}