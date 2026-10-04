/*
 * Created on 2007/11/13
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.rpn;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;

/**
 * 逆ポーランド記法の逆数(Inverse)演算子を表すクラスです。
 * 
 * @author Anan
 * @version $Revision: 1.5 $, 2007/11/13
 * @param <S> スカラーの型 
 * @param <M> 行列の型
 */
public final class InverseOperator<S extends Scalar<S,M>, M extends Matrix<S,M>> extends AbstractOperator<S,M> implements SingleOperandOperator<S,M> {

  /** 演算子 。*/
  //private static InverseOperator<DoubleNumber,DoubleMatrix> operator = new InverseOperator<>();

  /**
   * 新しく生成された<code>InverseOperator</code>オブジェクトを初期化します。
   */
  public InverseOperator() {
    super("%"); //$NON-NLS-1$
  }

//  /**
//   * 逆数(Inverse)演算子を返します。
//   * 
//   * @return 逆数(Inverse)演算子
//   */
//  public static InverseOperator<DoubleNumber,DoubleMatrix> getInstance() {
//    return operator;
//  }

  /**
   * {@inheritDoc}
   */
  public ReversePolishNotationOperand<S,M> operate(final ReversePolishNotationProcessor<S,M> processor, final ReversePolishNotationOperand<S,M> operand) {
    return processor.inverseOperation(operand);
  }

  /**
   * {@inheritDoc}
   */
  public String getStringOfSymbol() {
    return getOperator();
  }
  
  /**
   * {@inheritDoc}
   */
  public ReversePolishNotationOperand<S, M> toOperand() {
    throw new UnsupportedOperationException();
  }
  
  /**
   * {@inheritDoc}
   */
  public SingleOperandOperator<S, M> toSingleOperandOperator() {
    return this;
  }
  
  /**
   * {@inheritDoc}
   */
  public DoubleOperandOperator<S, M> toDoubleOperandOperator() {
    throw new UnsupportedOperationException();
  }
}
