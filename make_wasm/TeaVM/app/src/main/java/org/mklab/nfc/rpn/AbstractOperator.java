package org.mklab.nfc.rpn;

import java.util.Objects;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;

/**
 * 逆ポーランド記法の演算子(オペレータ)の抽象クラスを表すクラスです。
 * 
 * @author Anan
 * @version $Revision: 1.2 $, 2007/08/30
 * @param <S> スカラーの型 
 * @param <M> 行列の型
 */
public abstract class AbstractOperator<S extends Scalar<S,M>, M extends Matrix<S,M>> implements ReversePolishNotationOperator<S,M> {
  /** 演算子。 */
  private String operator;

  /**
   * 新しく生成された<code>AbstractOperator</code>オブジェクトを初期化します。
   * @param operator 演算子
   */
  AbstractOperator(final String operator) {
    this.operator = operator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.operator);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object opponent) {
    if (this == opponent) {
      return true;
    }
    if (opponent == null) {
      return false;
    }
    if (getClass() != opponent.getClass()) {
      return false;
    }
    final AbstractOperator<S,M> other = (AbstractOperator<S,M>)opponent;
    return Objects.equals(this.operator, other.operator);
  }


  /**
   * {@inheritDoc}
   */
  public final String getOperator() {
    return this.operator;
  }
}
