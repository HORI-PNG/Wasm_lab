/*
 * $Id: Linspace.java,v 1.10 2008/03/15 00:23:40 koga Exp $
 * 
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.matrix.misc;

import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.IntMatrix;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 等間隔で分割されたデータ列を生成するクラスです。
 * 
 * <p> Linearly spaced vector
 * 
 * @author koga
 * @version $Revision: 1.10 $
 */
public final class LinearlySpacedVector {
  /**
   * 新しく生成された<code>LinearlySpacedVector</code>オブジェクトを初期化します。
   */
  private LinearlySpacedVector() {
    // nothing to do
  }

  /**
   * <code>x1</code>と<code>x2</code>の間を<code>100</code> 等分した100個の点を成分にもつベクトルを返します。
   * 
   * @param x1 始点
   * @param x2 終点
   * @return 等間隔で分割されたデータ列 (linearly spaced array)
   */
  public static DoubleMatrix create(final double x1, final double x2) {
    return create(x1, x2, 100);
  }

  /**
   * <code>x1</code>と<code>x2</code>の間を<code>100</code> 等分した100個の点を成分にもつベクトルを返します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param x1 始点
   * @param x2 終点
   * @return 等間隔で分割されたデータ列 (linearly spaced array)
   */
  public static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> M create(final S x1, final S x2) {
    return create(x1, x2, 100);
  }

  /**
   * <code>x1</code>と<code>x2</code>の間を <code>n</code>等分したn個の点を成分にもつベクトルを返します。
   * 
   * @param x1 始点
   * @param x2 終点
   * @param splitSize 分割数
   * @return 等間隔で分割されたデータ列 (linearly spaced array)
   */
  public static DoubleMatrix create(final double x1, final double x2, final int splitSize) {
    return new DoubleMatrix(IntMatrix.series(0, splitSize - 2)).multiply((x2 - x1) / (splitSize - 1)).addElementWise(x1).appendRight(new DoubleMatrix(new double[] {x2}));
  }

  /**
   * <code>x1</code>と<code>x2</code>の間を <code>n</code>等分したn個の点を成分にもつベクトルを返します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param x1 始点
   * @param x2 終点
   * @param splitSize 分割数
   * @return 等間隔で分割されたデータ列 (linearly spaced array)
   */
  public static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> M create(final S x1, final S x2, final int splitSize) {
//    final S[] lastPoint = x2.createArray(1);
//    lastPoint[0] = x2;
//    final M last = x2.createGrid(lastPoint);
//    return IntMatrix.series(0, splitSize - 2).multiply(x2.subtract(x1).divide(splitSize - 1)).addElementWise(x1).appendRight(last);
    
    final M ans = x1.createZeroGrid(1, splitSize);
    for (int i = 0; i < ans.length(); i++) {
      ans.setElement(i+1, x1.add(x2.subtract(x1).divide(splitSize - 1).multiply(i)));
    }
    ans.setElement(ans.length(), x2);
    
    return ans;
  }
}
