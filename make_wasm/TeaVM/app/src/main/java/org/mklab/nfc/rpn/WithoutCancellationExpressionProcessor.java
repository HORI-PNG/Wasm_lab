/*
 * Created on 2008/01/23
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.rpn;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;

/**
 * 逆ポーランド記法を数式に関して0要素のキャンセルをせずに解釈するクラスです。
 * 
 * @author Anan
 * @version $Revision: 1.5 $, 2008/01/23
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public class WithoutCancellationExpressionProcessor<S extends Scalar<S,M>, M extends Matrix<S,M>> extends ExpressionProcessor<S,M> {

  /**
   * 新しく生成された<code>ReversePolishNotationExpressionWithoutCancellationProcessor</code>オブジェクトを初期化します。
   */
  public WithoutCancellationExpressionProcessor() {
    setHasCancellation(false);
  }
}
