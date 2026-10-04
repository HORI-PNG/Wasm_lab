/*
 * Created on 2007/11/02
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.rpn;

import java.util.LinkedList;
import java.util.List;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;


/**
 * 逆ポーランド記法を評価(処理)するプロセッサの抽象クラスです。
 * 
 * @author Anan
 * @version $Revision: 1.4 $, 2007/11/02
 * @param <S> スカラーの型 
 * @param <M> 行列の型
 */
public abstract class AbstractProcessor<S extends Scalar<S,M>, M extends Matrix<S,M>> implements ReversePolishNotationProcessor<S,M> {

  /** 数値のフォーマット。 */
  private String format = "%G"; //$NON-NLS-1$

  /**
   * {@inheritDoc}
   */
  public final String getFormat() {
    return this.format;
  }

  /**
   * {@inheritDoc}
   */
  public final void setFormat(final String format) {
    this.format = format;
    //AbstractGrid.setDefaultElementFormat(format);
  }

  /**
   * {@inheritDoc}
   */
  public final ReversePolishNotationOperand<S,M> evaluate(final ReversePolishNotationOperand<S,M> operand) {
    final List<ReversePolishNotationSymbol<S,M>> symbols = operand.getSymbolStack();
    final LinkedList<ReversePolishNotationOperand<S,M>> symbolStack = new LinkedList<>();

    for (final ReversePolishNotationSymbol<S,M> symbol : symbols) {
      if (symbol instanceof ReversePolishNotationOperand) {
        symbolStack.push((ReversePolishNotationOperand<S,M>)symbol);
        continue;
      }

      if (symbol instanceof DoubleOperandOperator) {
        final ReversePolishNotationOperand<S,M> leftOperand = symbolStack.pop();
        final ReversePolishNotationOperand<S,M> rightOperand = symbolStack.pop();
        
        final ReversePolishNotationOperand<S, M> result = symbol.toDoubleOperandOperator().operate(this, leftOperand, rightOperand);
        symbolStack.push(result);
        continue;
      }

      if (symbol instanceof SingleOperandOperator) {
        final ReversePolishNotationOperand<S,M> singleOperand = symbolStack.pop();
        final ReversePolishNotationOperand<S, M> result = symbol.toSingleOperandOperator().operate(this, singleOperand);
        symbolStack.push(result);
        continue;
      }

      throw new IllegalArgumentException(Messages.getString("AbstractReversePolishNotationProcessor.0")); //$NON-NLS-1$
    }
    
    return symbolStack.peek();
  }
}
