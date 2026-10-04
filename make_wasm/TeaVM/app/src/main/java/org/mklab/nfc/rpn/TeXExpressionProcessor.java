/*
 * Created on 2007/11/13
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.rpn;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;

/**
 * 逆ポーランド記法を評価(処理)し，TeX形式に変換するクラスです。
 * 
 * @author Anan
 * @version $Revision: 1.8 $, 2007/11/13
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public class TeXExpressionProcessor<S extends Scalar<S,M>, M extends Matrix<S,M>> extends AbstractExpressionProcessor<S,M> {
  
  /**
   * 新しく生成された<code>TeXExpressionProcessor</code>オブジェクトを初期化します。
   */
  public TeXExpressionProcessor() {
    setHasCancellation(false);
  }

  /**
   * {@inheritDoc}
   */
  public final String getLeftParenthesis() {
    return "\\left("; //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final String getRightParenthesis() {
    return "\\right)"; //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final String getInverseString() {
    return "^{-1}"; //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final String getMultiplicationString() {
    return " \\times "; //$NON-NLS-1$
  }
}
