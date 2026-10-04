/**
 * $Id: AbstractScalar.java,v 1.4 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.scalar;

import org.mklab.nfc.matrix.BaseMatrixUtil;
import org.mklab.nfc.matrix.GridUtil;
import org.mklab.nfc.matrix.Matrix;


/**
 * 抽象スカラーを表すクラスです。
 * 
 * @author koga
 * @version $Revision: 1.4 $
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public abstract class AbstractScalar<S extends Scalar<S,M>, M extends Matrix<S,M>> implements Scalar<S,M>, Cloneable, java.io.Serializable {

  /** シリアル番号。 */
  private static final long serialVersionUID = -8704188122084134448L;

  /** デフォルトの出力フォーマット。 */
  private static String defaultFormat = "%.7G"; //$NON-NLS-1$

  /** 成分の出力フォーマット。 */
  private String format = AbstractScalar.defaultFormat;

  /** オペレーターの仲介。 */
  //private static ScalarMediator mediator = ScalarMediator.getInstance();

  /**
   * デフォルト出力フォーマットを設定します。
   * 
   * @param format デフォルト出力フォーマット
   */
  public static void setDefaultFormat(final String format) {
    AbstractScalar.defaultFormat = format;
  }

  /**
   * デフォルト出力フォーマットを返します。
   * 
   * @return デフォルト出力フォーマット
   */
  public static String getDefaultFormat() {
    return AbstractScalar.defaultFormat;
  }

  /**
   * {@inheritDoc}
   */
  public final void setFormat(final String format) {
    this.format = format;
  }

  /**
   * {@inheritDoc}
   */
  public final String getFormat() {
    return this.format;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public S clone() {
    try {
      return (S)super.clone();
    } catch (CloneNotSupportedException e) {
      throw new InternalError(e.getMessage());
    }
  }

//  /**
//   * {@inheritDoc}
//   */
//  public boolean isTransformableFrom(final GridElement<? extends GridElement<?>> value) {
//    if (value == null) {
//      return false;
//    }
//    
//    if (this.getClass().equals(value.getClass())) {
//      return true;
//    }
//    return false;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @SuppressWarnings("unchecked")
//  public S transformFrom(final GridElement<? extends GridElement<?>> value) {
//    if (this.getClass().equals(value.getClass())) {
//      return (S)value.clone();
//    }
//
//    throw new IllegalArgumentException(Messages.getString("AbstractScalar.0")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public boolean isTransformableTo(final GridElement<? extends GridElement<?>> value) {
//    if (value == null) {
//      return false;
//    }
//    
//    if (this.getClass().equals(value.getClass())) {
//      return true;
//    }
//    return false;
//  }
  
//  /**
//   * {@inheritDoc}
//   */
//  public GridElement<?> transformTo(final GridElement<? extends GridElement<?>> value) {
//    if (this.getClass().equals(value.getClass())) {
//      return this.clone();
//    }
//
//    throw new IllegalArgumentException(Messages.getString("AbstractScalar.1")); //$NON-NLS-1$
//  }

  /**
   * {@inheritDoc}
   */
  public M createGrid(final int rowSize, final int columnSize, final int[][] elements) {
    final S[][] newElements = createArray(rowSize, columnSize);
    for (int row = 0; row < rowSize; row++) {
      for (int column = 0; column < columnSize; column++) {
        newElements[row][column] = create(elements[row][column]);
      }
    }

    return createGrid(rowSize, columnSize, newElements);
  }
  
  /**
   * {@inheritDoc}
   */
  public M createGrid(final int[][] elements) {
    final int rowSize = elements != null ? elements.length : 0;
    final int columnSize = elements != null && rowSize != 0 ?  elements[0].length : 0;
    return createGrid(rowSize, columnSize, elements);
  }

  /**
   * {@inheritDoc}
   */
  public M createGrid(final S[][] elements) {
    final int rowSize = elements != null ? elements.length : 0;
    final int columnSize = elements != null && rowSize != 0 ?  elements[0].length : 0;
    return createGrid(rowSize, columnSize, elements);
  }

  /**
   * {@inheritDoc}
   */
  public M createGrid(final int rowSize, final int columnSize, final double[][] elements) {
    final S[][] newElements = createArray(rowSize, columnSize);
    for (int row = 0; row < rowSize; row++) {
      for (int column = 0; column < columnSize; column++) {
        newElements[row][column] = create(elements[row][column]);
      }
    }

    return  createGrid(rowSize, columnSize, newElements);
  }

  /**
   * {@inheritDoc}
   */
  public M createGrid(final double[][] elements) {
    final int rowSize = elements != null ? elements.length : 0;
    final int columnSize = elements != null && rowSize != 0 ?  elements[0].length : 0;
    return createGrid(rowSize, columnSize, elements);
  }

  /**
   * {@inheritDoc}
   */
  public M createGrid(final int[] elements) {
    final S[] newElements = createArray(elements.length);
    for (int row = 0; row < elements.length; row++) {
      newElements[row] = create(elements[row]);
    }

    return createGrid(newElements);
  }

  /**
   * {@inheritDoc}
   */
  public M createGrid(final double[] elements) {
    final S[] newElements = createArray(elements.length);
    for (int row = 0; row < elements.length; row++) {
      newElements[row] = create(elements[row]);
    }

    return createGrid(newElements);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public M createZeroGrid(final int rowSize, final int columnSize) {
    return createGrid(rowSize, columnSize, GridUtil.createZero((S)this, rowSize, columnSize));
  }

  /**
   * {@inheritDoc}
   */
  public M createZeroGrid(final int size) {
    return createZeroGrid(size, size);
    //return createGrid(GridUtil.createZero((T)this, size));
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public final M createUnitGrid(final int rowSize, final int columnSize) {
    return createGrid(rowSize, columnSize, BaseMatrixUtil.createUnit((S)this, rowSize, columnSize));
  }

  /**
   * {@inheritDoc}
   */
  public final M createUnitGrid(final int size) {
    return createUnitGrid(size, size);
  }
  
  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public final M createOnesGrid(final int rowSize, final int columnSize) {
    return createGrid(rowSize, columnSize, BaseMatrixUtil.createOnes((S)this, rowSize, columnSize));
  }

  /**
   * {@inheritDoc}
   */
  public final M createOnesGrid(final int size) {
    return createOnesGrid(size, size);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public Scalar<?,?> add(final Scalar<?,?> value) {
//    return mediator.operate(this.getAddOperator(), value.getAddOperator(), this, value);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public Scalar<?,?> subtract(final Scalar<?,?> value) {
//    return mediator.operate(this.getSubtractOperator(), value.getSubtractOperator(), this, value);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public Scalar<?,?> multiply(final Scalar<?,?> value) {
//    return mediator.operate(this.getMultiplyOperator(), value.getMultiplyOperator(), this, value);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public S divide(final S value) {
//    return (S)mediator.operate(this.getDivideOperator(), value.getDivideOperator(), this, value);
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public S leftDivide(final S value) {
//    return (S)mediator.operate(this.getLeftDivideOperator(), value.getLeftDivideOperator(), this, value);
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public boolean equals(final S opponent, final double tolerance) {
//    return mediator.equals(this.getEqualOperator(), opponent.getEqualOperator(), this, opponent, tolerance);
//  }
}
