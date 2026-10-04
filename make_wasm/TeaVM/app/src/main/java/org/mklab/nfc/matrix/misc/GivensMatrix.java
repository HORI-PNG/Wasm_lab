/*
 * $Id: Givens.java,v 1.11 2008/03/15 00:23:40 koga Exp $
 * 
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.matrix.misc;

import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.DoubleNumber;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * ギブンス回転行列を生成するクラスです。
 * 
 * <p> Givens rotation matrix
 * 
 * @author koga
 * @version $Revision: 1.11 $
 */
public final class GivensMatrix {
  /**
   * 新しく生成された<code>GivensMatrix</code>オブジェクトを初期化します。
   */
  private GivensMatrix() {
    // nothing to do
  }
  
  /**
   * 2×2の複素ギブンス回転行列
   * 
   * <pre><code> | c s | | x | | r | G = | | ただし G * | | = | | |-conj(s) c | | y | | 0 | </code></pre>
   * 
   * を返します。ただし、<code>c</code>は実数、<code>s</code>は複素数であり、 <code>c^2 + s^2 = 1</code>を満たす。
   * 
   * @param <M> 行列の型 
   * @param <S> 成分の型
   * @param x 数値1
   * @param y 数値2
   * @return ギブンンス回転行列 (givens rotation matrix)
   */
  public static <S extends NumericalScalar<S,M>,M extends NumericalMatrix<S,M>> M create(final S x, final S y) {
    final S l = x.abs().add(y.abs());
    final S xl = x.abs();

    final S unit = l.createUnit();

    if (xl.isZero()) {
      final S c = unit.createZero();
      final S s = unit;
      final S[][] matrix = s.createArray(2, 2);
      matrix[0][0] = c;
      matrix[0][1] = s;
      matrix[1][0] = s.conjugate().unaryMinus();
      matrix[1][1] = c.clone();
      return c.createGrid(2, 2, matrix);
    }

    final S tmp1 = x.divide(l).multiply(x.conjugate().divide(l));
    final S tmp2 = y.divide(l).multiply(y.conjugate().divide(l));

    final S p = tmp1.add(tmp2).sqrt().multiply(l);
    final S c = xl.divide(p);
    final S s = x.divide(xl).multiply(y.conjugate().divide(p));
    
    final S[][] matrix = s.createArray(2, 2);
    matrix[0][0] = c;
    matrix[0][1] = s;
    matrix[1][0] = s.conjugate().unaryMinus();
    matrix[1][1] = c.clone();
    
    return c.createGrid(2, 2, matrix);
  }
  
  /**
   * 2×2の複素ギブンス回転行列
   * 
   * <pre><code> | c s | | x | | r | G = | | ただし G * | | = | | |-conj(s) c | | y | | 0 | </code></pre>
   * 
   * を返します。ただし、<code>c</code>は実数、<code>s</code>は複素数であり、 <code>c^2 + s^2 = 1</code>を満たす。
   * 
   * @param x 数値1
   * @param y 数値2
   * @return ギブンンス回転行列 (givens rotation matrix)
   */
  public static DoubleMatrix create(final double x, final double y) {
    return create(new DoubleNumber(x), new DoubleNumber(y));
  }
}
