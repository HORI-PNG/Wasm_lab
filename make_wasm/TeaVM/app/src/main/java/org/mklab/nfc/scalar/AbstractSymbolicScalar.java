/*
 * Created on 2008/02/04
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.scalar;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.matrix.SymbolicMatrix;

/**
 * 抽象数式スカラーを表すクラスです。
 * 
 * @author koga
 * @version $Revision: 1.4 $, 2008/02/04
 * @param <S> スカラーの型
 * @param <M> 行列の型
 * @param <ES> 係数スカラーの型
 * @param <EM> 係数行列の型
 */
public abstract class AbstractSymbolicScalar<S extends SymbolicScalar<S,M,ES,EM>, M extends SymbolicMatrix<S,M,ES,EM>, ES extends NumericalScalar<ES,EM>, EM extends NumericalMatrix<ES,EM>> extends AbstractScalar<S,M> implements SymbolicScalar<S,M,ES,EM> {
  /** シリアル番号。 */
  private static final long serialVersionUID = -4653169025955820594L;

  /**
   * {@inheritDoc}
   */
  public final S shiftHigher() {
    return shiftHigher(1);
  }

  /**
   * {@inheritDoc}
   */
  public final S shiftLower() {
    return shiftLower(1);
  }

  /**
   * {@inheritDoc}
   */
  public final S derivative() {
    return derivative(1);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setRealPart(final int realPart) {
//    if (this.isReal()) {
//      throw new IllegalArgumentException(Messages.getString("AbstractSymbolicScalar.0")); //$NON-NLS-1$
//    }
//
//    setRealPart(transformFrom(realPart));
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final void setRealPart(final double realPart) {
//    if (this.isReal()) {
//      throw new IllegalArgumentException(Messages.getString("AbstractSymbolicScalar.1")); //$NON-NLS-1$
//    }
//
//    setRealPart(transformFrom(realPart));
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final void setImaginaryPart(final int imagPart) {
//    if (this.isReal()) {
//      throw new IllegalArgumentException(Messages.getString("AbstractSymbolicScalar.2")); //$NON-NLS-1$
//    }
//
//    setImaginaryPart(transformFrom(imagPart));
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final void setImaginaryPart(final double imagPart) {
//    if (this.isReal()) {
//      throw new IllegalArgumentException(Messages.getString("AbstractSymbolicScalar.3")); //$NON-NLS-1$
//    }
//
//    setImaginaryPart(transformFrom(imagPart));
//  }
}
