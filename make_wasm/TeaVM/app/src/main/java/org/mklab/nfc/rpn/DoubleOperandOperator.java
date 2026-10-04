/*
 * Created on 2007/12/07
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.rpn;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;

/**
 * 2個のオペランドを処理するオペレーターを表すインターフェースです。
 * 
 * @author Anan
 * @version $Revision: 1.4 $, 2007/12/07
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public interface DoubleOperandOperator<S extends Scalar<S,M>, M extends Matrix<S,M>> {

  /**
   * オペランドを評価した結果を返します。
   * 
   * @param processor プロセッサー
   * @param left 左オペランド
   * @param right 右オペランド
   * @return オペランドを評価した結果
   */
  ReversePolishNotationOperand<S,M> operate(ReversePolishNotationProcessor<S,M> processor, ReversePolishNotationOperand<S,M> left, ReversePolishNotationOperand<S,M> right);
}
