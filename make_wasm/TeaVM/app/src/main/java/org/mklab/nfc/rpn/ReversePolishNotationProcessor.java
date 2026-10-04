/*
 * Created on 2007/11/02
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.rpn;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;

/**
 * 逆ポーランド記法を評価するプロセッサを表すインターフェースです。
 * 
 * @author Anan
 * @version $Revision: 1.5 $, 2007/11/02
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public interface ReversePolishNotationProcessor<S extends Scalar<S,M>, M extends Matrix<S,M>> {

  /**
   * オペランドを逆ポーランド記法により評価した結果を返します。
   * 
   * @param operand 対象となるオペランド
   * @return 評価した結果のオペランド
   */
  ReversePolishNotationOperand<S,M> evaluate(ReversePolishNotationOperand<S,M> operand);

  /**
   * オペランドを評価した結果を返します。
   * 
   * @param operand 対象となるオペランド
   * @return オペランドを評価した結果
   */
  String getResult(ReversePolishNotationOperand<S,M> operand);

  /**
   * 数値の出力フォーマットを返します。
   * 
   * @return 数値の出力フォーマット
   */
  String getFormat();

  /**
   * 数値の出力フォーマットを設定します。
   * 
   * @param format 数値の出力フォーマット
   */
  void setFormat(final String format);

  /**
   * オペランドの逆数を返します。
   * 
   * @param operand オペランド
   * @return オペランドの逆数
   */
  ReversePolishNotationOperand<S,M> inverseOperation(final ReversePolishNotationOperand<S,M> operand);

  /**
   * 2個のオペランドの和を返します。
   * 
   * @param left 左オペランド
   * @param right 左オペランド
   * @return 2個のオペランドの和
   */
  ReversePolishNotationOperand<S,M> addOperation(final ReversePolishNotationOperand<S,M> left, final ReversePolishNotationOperand<S,M> right);

  /**
   * 2個のオペランドの積を返します。
   * 
   * @param left オペランド
   * @param right オペランド
   * @return 2個のオペランドの積
   */
  ReversePolishNotationOperand<S,M> multiplyOperation(final ReversePolishNotationOperand<S,M> left, final ReversePolishNotationOperand<S,M> right);
}