package org.mklab.nfc.rpn;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;

/**
 * 逆ポーランド記法を数式に関して評価するクラスです。
 * 
 * @author Anan
 * @version $Revision: 1.16 $, 2007/08/31
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public class ExpressionProcessor<S extends Scalar<S,M>, M extends Matrix<S,M>> extends AbstractExpressionProcessor<S,M> {

  /**
   * {@inheritDoc}
   */
  public final String getLeftParenthesis() {
    return "("; //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final String getRightParenthesis() {
    return ")"; //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final String getInverseString() {
    return "~"; //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final String getMultiplicationString() {
    return "*"; //$NON-NLS-1$
  }
}
