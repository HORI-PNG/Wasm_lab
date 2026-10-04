/*
 * Created on 2008/09/09
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.rpn;

/**
 * 逆ポーランド記法に基づく数式を評価するプロセッサを表すインターフェースです。
 * 
 * @author koga
 * @version $Revision$, 2008/09/09
 */
public interface ReversePolishNotationExpressionProcessor {

  /**
   * 左括弧を返します。
   * 
   * @return String 左括弧
   */
  String getLeftParenthesis();

  /**
   * 右括弧を返します。
   * 
   * @return String 右括弧
   */
  String getRightParenthesis();

  /**
   * 逆数(逆行列)を表す文字列を返します。
   * 
   * @return 逆数(逆行列)を表す文字列
   */
  String getInverseString();

  /**
   * 乗算を表す文字列を返します。
   * 
   * @return 乗算を表す文字列
   */
  String getMultiplicationString();

}