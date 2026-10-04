package org.mklab.nfc.rpn;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;

/**
 * 逆ポーランド記法の積(multiply)演算子を表すクラスです。
 * 
 * @author Anan
 * @version $Revision: 1.8 $, 2007/08/30
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public final class MultiplyOperator<S extends Scalar<S,M>, M extends Matrix<S,M>> extends AbstractOperator<S,M> implements DoubleOperandOperator<S,M> {

  /** 演算子。 */
  //private static MultiplyOperator<?,?> operator;

  /**
   * 新しく生成された<code>ReversePolishNotationMultiplyOperator</code>オブジェクトを初期化します。
   */
  public MultiplyOperator() {
    super("*"); //$NON-NLS-1$
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
  public ReversePolishNotationOperand<S,M> operate(final ReversePolishNotationProcessor<S,M> processor, final ReversePolishNotationOperand<S,M> left, final ReversePolishNotationOperand<S,M> right) {
    return processor.multiplyOperation(left, right);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String toString(){
    return "*"; //$NON-NLS-1$
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
    throw new UnsupportedOperationException();   
  }
  
  /**
   * {@inheritDoc}
   */
  public DoubleOperandOperator<S, M> toDoubleOperandOperator() {
    return this;
  }
}
