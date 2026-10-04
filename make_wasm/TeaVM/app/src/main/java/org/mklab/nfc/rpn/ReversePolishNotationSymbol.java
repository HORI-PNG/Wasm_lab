package org.mklab.nfc.rpn;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;

/**
 * 逆ポーランド記法のシンボルを表すインターフェースです。
 * 
 * @author Anan
 * @version $Revision: 1.1 $, 2007/08/30
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public interface ReversePolishNotationSymbol<S extends Scalar<S,M>, M extends Matrix<S,M>> {
  /**
   * シンボルの文字列を返します。
   * 
   * @return シンボルの文字列
   */
  String getStringOfSymbol();
  
  /**
   * Returns operand.
   * 
   * @return operand
   */
  ReversePolishNotationOperand<S,M> toOperand();
  
  /**
   * Returns single-operand operator.
   * 
   * @return single-operand operator
   */
  SingleOperandOperator<S,M> toSingleOperandOperator();
    
    /**
     * Returns double-operand operator.
     * 
     * @return double-operand operator
     */
    DoubleOperandOperator<S,M> toDoubleOperandOperator();
}
