/*
 * $Id: Vander.java,v 1.8 2008/03/03 15:18:35 koga Exp $
 * 
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.matrix.misc;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.matrix.util.Makecolv;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * ヴァンデルモンド行列を生成するクラスです。
 * 
 * <p>Vandermonde matrix
 * 
 * @author koga
 * @version $Revision: 1.8 $
 */
public final class VandermondeMatrix {
  /**
   * 新しく生成された<code>VandermondeMatrix</code>オブジェクトを初期化します。
   */
  private VandermondeMatrix() {
    // nothing to do
  }

  /**
   * 2番目から最後までの列が<code>x</code>であるヴァンデルモンド行列を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型 
   * @param x データ
   * @return ヴァンデルモンンド行列 (vandermonde matrix)
   */
  public static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> M create(final M x) {
    final int size = x.length();
    final M y = Makecolv.makecolv(x);
    final M ans = x.createOnes(size, size);
    for (int column = 1; column <= size; column++) {
      ans.setColumnVector(column, y.powerElementWise(size - column));
    }
    return ans;
  }
}
