/*
 * Created on 2007/10/03
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.rpn;

import java.util.List;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;


/**
 * 逆ポーランド記法のオペランドを表すインターフェースです。
 * 
 * @author Anan
 * @version $Revision: 1.5 $, 2007/10/03
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public interface ReversePolishNotationOperand<S extends Scalar<S,M>, M extends Matrix<S,M>> extends ReversePolishNotationSymbol<S,M> {

  /**
   * thisにopponentを加えたオペランドを返します。
   * 
   * @param opponent 加える値
   * @return thisにopponentを加えたオペランド
   */
  ReversePolishNotationOperand<S,M> add(final ReversePolishNotationOperand<S,M> opponent);

  /**
   * thisにopponentを掛けたオペランドを返します。
   * 
   * @param opponent 掛ける値
   * @return thisにopponentを掛けたオペランド
   */
  ReversePolishNotationOperand<S,M> multiply(final ReversePolishNotationOperand<S,M> opponent);

  /**
   * 逆(逆数)を返します。
   * 
   * @return 逆(逆数)
   */
  ReversePolishNotationOperand<S,M> inverse();

  /**
   * opponent、this、オペレーターのスタック(リスト)を返します。
   * 
   * <p>オペランドのスタックは展開されます。
   * 
   * @param opponent 演算の対象オペランド
   * @param operator オペレーター
   * @return opponent、this、オペレーターのスタック(リスト)
   */
  List<ReversePolishNotationSymbol<S,M>> createSymbolStack(final ReversePolishNotationOperand<S,M> opponent, final ReversePolishNotationOperator<S,M> operator);

  /**
   * 逆ポーランド記法のスタック(リスト)を返します。
   * 
   * @return 逆ポーランド記法のスタック(リスト)
   */
  List<ReversePolishNotationSymbol<S,M>> getSymbolStack();

  /**
   * 逆ポーランド記法のスタック(リスト)をセットします。
   * 
   * @param symbolStack 逆ポーランド記法のスタック(リスト)
   */
  void setSymbolStack(List<ReversePolishNotationSymbol<S,M>> symbolStack);

  /**
   * symbolを逆ポーランド記法のスタック(リスト)に追加します。
   * 
   * @param symbol 加えるシンボル
   */
  void addSymbol(final ReversePolishNotationSymbol<S,M> symbol);

  /**
   * symbolのリストを逆ポーランド記法のスタック(リスト)に追加します。
   * 
   * @param symbols 加えるシンボルのリスト
   */
  void addSymbols(final List<ReversePolishNotationSymbol<S,M>> symbols);

  /**
   * 指定された値をもつオペランドを返します。
   * 
   * @param value 値
   * @return 指定された値をもつオペランド
   */
  ReversePolishNotationOperand<S,M> createOperand(M value);

  /**
   * 単位値をもつオペランドを返します。
   * 
   * @param size 大きさ
   * @return 単位値をもつオペランド
   */
  ReversePolishNotationOperand<S,M> createUnitOperand(int size);

  /**
   * 負の単位値をもつオペランドを返します。
   * 
   * @param size 大きさ
   * @return 負の単位値をもつオペランド
   */
  ReversePolishNotationOperand<S,M> createNegativeUnitOperand(int size);

  /**
   * 符号付き数式表現を返します。
   * 
   * @return 符号付き数式表現
   */
  String getSignedExpression();

  /**
   * 数式表現を返します。
   * 
   * @return 数式表現
   */
  String getExpression();

  /**
   * オペランドの値を返します。
   * 
   * @return オペランドの値
   */
  M getOperandValue();

  /**
   * 数式表現をセットします。
   * 
   * @param expression 数式表現
   */
  void setExpression(String expression);

  /**
   * リストの中の要素を{@link #isNegative}を評価し、add結合させてoperandにセットして返します。
   * 
   * @return オペランド
   */
  ReversePolishNotationOperand<S,M> getParsedOperand();

  /**
   * ゼロであるかを判定します。
   * 
   * @return ゼロならばtrue,そうでなければfalse
   */
  boolean isZeroOperand();

  /**
   * 単位値であるかを判定します。
   * 
   * @return 単位値ならばtrue,そうでなければfalse
   */
  boolean isUnitOperand();

  /**
   * 負の単位値であるかを判定します。
   * 
   * @return 負の単位値ならばtrue,そうでなければfalse
   */
  boolean isNegativeUnitOperand();

  /**
   * 符号が負であるか判定します。
   * 
   * @return 負ならばtrue、正なら(ゼロを含む)false
   */
  boolean isNegative();

  /**
   * 符号を設定します。
   * 
   * @param isNegative 負ならばtrue、正(ゼロを含む)ならばfalse
   */
  void setNegative(boolean isNegative);

  /**
   * 符号を反転した値を返します。
   * 
   * @return 符号を反転した値
   */
  ReversePolishNotationOperand<S,M> invertSign();

  /**
   * 1個の項からなるシステムであるか判定します。
   * 
   * @return 1個の項からなるシステムならばtrue、そうでなければfalse
   */
  boolean isSingleTerm();

  /**
   * 1個の項からなるシステムであるかを設定します。
   * 
   * @param singleTerm 1個の項からなるシステムならばtrue、そうでなければfalse
   */
  void setSingleTerm(boolean singleTerm);

  /**
   * 変数として扱うかを判定します。
   * 
   * @return 変数として扱うならばtrue、そうでなければfalse
   */
  boolean isVariable();

  /**
   * 変数として扱うかを設定します。
   * 
   * @param isVariable 変数として扱うならばtrue、そうでなければfalse
   */
  void setVariable(boolean isVariable);
}