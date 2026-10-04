package org.mklab.nfc.rpn;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;

/**
 * 逆ポーランド記法の演算子を表すインターフェースです。
 * 
 * @author Anan
 * @version $Revision: 1.3 $, 2007/08/30
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public interface ReversePolishNotationOperator<S extends Scalar<S,M>, M extends Matrix<S,M>> extends ReversePolishNotationSymbol<S,M> {

  /**
   * オペレータ名を返します。
   * 
   * @return オペレータ名
   */
  String getOperator();
}
