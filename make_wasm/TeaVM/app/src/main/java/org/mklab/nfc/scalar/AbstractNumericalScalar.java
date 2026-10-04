/*
 * Created on 2008/01/16
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.scalar;

import org.mklab.nfc.matrix.NumericalMatrix;


/**
 * 抽象数値スカラーを表すクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.4 $, 2008/01/16
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public abstract class AbstractNumericalScalar<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> extends AbstractScalar<S,M> implements NumericalScalar<S,M> {

  /** シリアル番号。 */
  private static final long serialVersionUID = -8678398750915878266L;

  /** オペレーターの仲介。 */
  //private static ScalarMediator mediator = ScalarMediator.getInstance();

  /**
   * {@inheritDoc}
   */
  public S remainder(final S value2) {
    if (value2.isZero()) {
      return this.clone();
    }

    final S value = this.divide(value2);

    if (value.isReal() && value.isLessThan(0)) {
      return this.add(value.unaryMinus().floor().multiply(value2));
    }

    return this.subtract(value.floor().multiply(value2));
  }

  /**
   * {@inheritDoc}
   */
  public final S remainder(final int value2) {
    if (value2 == 0) {
      return this.clone();
    }

    final S value = this.divide(value2);

    if (value.isGreaterThanOrEquals(0)) {
      return this.subtract(value.floor().multiply(value2));
    }

    return this.subtract(value.ceil().multiply(value2));
  }

  /**
   * {@inheritDoc}
   */
  public final S remainder(final double value2) {
    if (value2 == 0) {
      return this.clone();
    }

    final S value = this.divide(value2);

    if (value.isGreaterThanOrEquals(0)) {
      return this.subtract(value.floor().multiply(value2));
    }

    return this.subtract(value.ceil().multiply(value2));
  }

  /**
   * {@inheritDoc}
   */
  public S modulus(final S value2) {
    if (value2.isZero()) {
      return this.clone();
    }
    return this.subtract(this.divide(value2).floor().multiply(value2));
  }

  /**
   * {@inheritDoc}
   */
  public final S modulus(final int value2) {
    if (value2 == 0) {
      return this.clone();
    }
    return this.subtract(this.divide(value2).floor().multiply(value2));
  }

  /**
   * {@inheritDoc}
   */
  public final S modulus(final double value2) {
    if (value2 == 0) {
      return this.clone();
    }
    return this.subtract(this.divide(value2).floor().multiply(value2));
  }

//  /**
//   * {@inheritDoc}
//   */
//  public NumericalScalar<?> createImaginaryUnit() {
//    @SuppressWarnings("unchecked")
//    final T value = (T)getRealPart();
//    return new BaseComplexNumericalScalar<T>(value.createZero(), value.createUnit());
//  }

  /**
   * {@inheritDoc}
   */
  public final S max(final int value) {
    if (this.isGreaterThanOrEquals(value)) {
      return clone();
    }
    return create(value);
  }

  /**
   * {@inheritDoc}
   */
  public final S max(final double value) {
    if (this.isGreaterThanOrEquals(value)) {
      return clone();
    }
    return createUnit().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public final S max(final S value) {
    if (this.isGreaterThanOrEquals(value)) {
      return clone();
    }
    return value.clone();
  }

  /**
   * {@inheritDoc}
   */
  public final S min(final int value) {
    if (this.isLessThanOrEquals(value)) {
      return clone();
    }

    return create(value);
  }

  /**
   * {@inheritDoc}
   */
  public final S min(final double value) {
    if (this.isLessThanOrEquals(value)) {
      return clone();
    }
    return createUnit().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public final S min(final S value) {
    if (this.isLessThanOrEquals(value)) {
      return clone();
    }
    return value.clone();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public M createGrid(final int rowSize, final int columnSize, final int[][] elements) {
    return super.createGrid(rowSize, columnSize, elements);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final M createGrid(final int rowSize, final int columnSize, final double[][] elements) {
    return super.createGrid(rowSize, columnSize, elements);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final M createGrid(final int[] elements) {
    return super.createGrid(elements);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final M createGrid(final double[] elements) {
    return super.createGrid(elements);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final M createZeroGrid(final int rowSize, final int columnSize) {
    return super.createZeroGrid(rowSize, columnSize);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final M createZeroGrid(final int size) {
    return super.createZeroGrid(size);
  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public S atan2(final S scalar) {
//    return (S)mediator.operate(this.getAtan2Operator(), scalar.getAtan2Operator(), this, scalar);
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public S power(final S value) {
//    return (S)mediator.operate(this.getPowerOperator(), value.getPowerOperator(), this, value);
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public boolean equals(final S opponent, final S tolerance) {
//    return mediator.equals(this.getEqualOperator(), opponent.getEqualOperator(), this, opponent, tolerance);
//  }
}
