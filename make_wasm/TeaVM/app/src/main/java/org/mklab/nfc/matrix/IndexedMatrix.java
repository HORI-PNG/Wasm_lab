/*
 * Created on 2009/12/18
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matrix;

import org.mklab.nfc.scalar.Scalar;

/**
 * 指数付きの行列を表すクラスです。
 * 
 * @author koga
 * @version $Revision$, 2009/12/18
 * @param <S> スカラー型
 * @param <M> 行列の型
 */
public class IndexedMatrix<S extends Scalar<S,M>, M extends Matrix<S,M>> {

  /** 行列。 */
  M matrix;
  /** 指数。 */
  IntMatrix indices;

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.indices == null) ? 0 : this.indices.hashCode());
    result = prime * result + ((this.matrix == null) ? 0 : this.matrix.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    @SuppressWarnings("unchecked")
    IndexedMatrix<S,M> other = (IndexedMatrix<S,M>)obj;
    if (this.indices == null) {
      if (other.indices != null) {
        return false;
      }
    } else if (!this.indices.equals(other.indices)) {
      return false;
    }
    if (this.matrix == null) {
      if (other.matrix != null) {
        return false;
      }
    } else if (!this.matrix.equals(other.matrix)) {
      return false;
    }
    return true;
  }

  /**
   * 新しく生成された<code>IndexedMatrix</code>オブジェクトを初期化します。
   * 
   * @param matrix 行列
   * @param indices 指数
   */
  public IndexedMatrix(final M matrix, final IntMatrix indices) {
    this.matrix = matrix;
    this.indices = indices;
  }

  /**
   * 行列を返します。
   * 
   * @return 行列
   */
  public M getMatrix() {
    return this.matrix;
  }

  /**
   * 指数を返します。
   * 
   * @return 指数
   */
  public IntMatrix getIndices() {
    return this.indices;
  }
}
