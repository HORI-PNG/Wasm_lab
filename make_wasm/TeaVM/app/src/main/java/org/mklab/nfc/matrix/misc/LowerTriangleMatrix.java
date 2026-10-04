/*
 * $Id: Tril.java,v 1.11 2008/04/13 02:12:38 koga Exp $
 * 
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.matrix.misc;

import org.mklab.nfc.matrix.BaseMatrixOperator;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.IntMatrix;
import org.mklab.nfc.scalar.Scalar;


/**
 * 下三角部分以外をゼロにした行列を生成するクラスです。
 * 
 * <p>Lower triangle part of matrix
 * 
 * @author koga
 * @version $Revision: 1.11 $
 */
public final class LowerTriangleMatrix {

  /**
   * 新しく生成された<code>LowerTriangleMatrix</code>オブジェクトを初期化します。
   */
  private LowerTriangleMatrix() {
    // nothing to do
  }

  /**
   * <code>x</code>の下三角部分を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param x 対象となる行列
   * @return 下三角部分以外をゼロにした行列
   */
  public static <S extends Scalar<S, M>, M extends BaseMatrixOperator<S, M>> M create(final M x) {
    final int distance = 0;
    return create(x, distance);
  }

  /**
   * <code>x</code>の<code>distance</code>次対角より下の成分を返します。
   * 
   * <pre>
   * 
   * distance = 0 : 主対角 distance &gt; 0 : 対角より上 distance &lt; 0 : 対角より下 </pre>
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param x 対象となる行列
   * @param distance 対角からの距離(正:上側、零:対角、負:下側)
   * @return 下三角部分以外をゼロにした行列
   */
  public static <S extends Scalar<S, M>, M extends BaseMatrixOperator<S, M>> M create(final M x, final int distance) {
    final int rowSize = x.getRowSize();
    final int columnSize = x.getColumnSize();
    final M ans = x.createZero(rowSize, columnSize);

    for (int row = Math.max(1, 1 - distance); row <= rowSize; row++) {
      for (int column = 1; column <= Math.min(columnSize, row + distance); column++) {
        ans.setElement(row, column, x.getElement(row, column));
      }
    }
    return ans;
  }

  /**
   * <code>x</code>の下三角部分を返します。
   * 
   * @param x 対象となる行列
   * @return 下三角部分以外をゼロにした行列
   */
  public static  DoubleMatrix create(final DoubleMatrix x) {
    final int distance = 0;
    return create(x, distance);
  }

  /**
   * <code>x</code>の<code>distance</code>次対角より下の成分を返します。
   * 
   * <pre>
   * 
   * distance = 0 : 主対角 distance &gt; 0 : 対角より上 distance &lt; 0 : 対角より下 </pre>
   * 
   * @param x 対象となる行列
   * @param distance 対角からの距離(正:上側、零:対角、負:下側)
   * @return 下三角部分以外をゼロにした行列
   */
  public static DoubleMatrix create(final DoubleMatrix x, final int distance) {
    final int rowSize = x.getRowSize();
    final int columnSize = x.getColumnSize();
    final DoubleMatrix ans = x.createZero(rowSize, columnSize);

    for (int row = Math.max(1, 1 - distance); row <= rowSize; row++) {
      for (int column = 1; column <= Math.min(columnSize, row + distance); column++) {
        ans.setElement(row, column, x.getDoubleElement(row, column));
      }
    }
    return ans;
  }

  /**
   * <code>x</code>の下三角部分を返します。
   * 
   * @param x 対象となる行列
   * @return 下三角部分以外をゼロにした行列
   */
  public static  IntMatrix create(final IntMatrix x) {
    final int distance = 0;
    return create(x, distance);
  }

  /**
   * <code>x</code>の<code>distance</code>次対角より下の成分を返します。
   * 
   * <pre>
   * 
   * distance = 0 : 主対角 distance &gt; 0 : 対角より上 distance &lt; 0 : 対角より下 </pre>
   * 
   * @param x 対象となる行列
   * @param distance 対角からの距離(正:上側、零:対角、負:下側)
   * @return 下三角部分以外をゼロにした行列
   */
  public static IntMatrix create(final IntMatrix x, final int distance) {
    final int rowSize = x.getRowSize();
    final int columnSize = x.getColumnSize();
    final IntMatrix ans = x.createZero(rowSize, columnSize);

    for (int row = Math.max(1, 1 - distance); row <= rowSize; row++) {
      for (int column = 1; column <= Math.min(columnSize, row + distance); column++) {
        ans.setElement(row, column, x.getIntElement(row, column));
      }
    }
    return ans;
  }

}
