/*
 * $Id: ExponentialMatrix.java,v 1.2 2008/03/15 00:23:44 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.elf;

import org.mklab.nfc.matrix.BaseMatrixUtil;
import org.mklab.nfc.matrix.AbstractNumericalMatrixUtil;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 指数関数行列を求めるためのクラスです。
 * 
 * @author koga
 * @version $Revision: 1.2 $
 */
public final class ExponentialMatrix {
  /**
   * 新しく生成された<code>ExponentialMatrix</code>オブジェクトを初期化します。
   */
  private ExponentialMatrix() {
    // nothing to do
  }

  /**
   * 指数関数行列を返します。
   * 
   * <p>行列をAとするとき、このメソッドは
   * 
   * <blockquote> I + A + A^2/(2!) + ... + A^n/(n!) + ... </blockquote>
   * 
   * を求めます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param a 数値行列
   * @return 指数関数行列
   */
  public static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> S[][] exp(final S[][] a) {
    return ExponentialMatrix.<S,M> exp(a, AbstractNumericalMatrixUtil.frobNorm(a).multiply(a[0][0].getMachineEpsilon()));
  }

  /**
   * 指数関数行列を返します。
   * 
   * <p>行列をAとするとき、このメソッドは
   * 
   * <blockquote> Y = I + A + A^2/(2!) + ... + A^n/(n!) + ... </blockquote>
   * 
   * を求めます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param a 数値行列
   * @param tolerance 許容誤差
   * @return 指数関数行列
   */
  public static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> S[][] exp(final S[][] a, final S tolerance) {
    final int rowSize = a.length;
    final int columnSize = a[0].length;

    final S two = a[0][0].createUnit().multiply(2);
    final S scale = AbstractNumericalMatrixUtil.frobNorm(a);
    final S squaring = scale.log().divide(two.log()).ceil();

    final S[][] scaledMatrix = a[0][0].createArray(rowSize, columnSize);
    final S d = two.power(squaring).inverse();

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        scaledMatrix[i][j] = a[i][j].multiply(d);
      }
    }

    S[][] scaledExp = exp1(scaledMatrix, tolerance);
    
    for (int i = 0; squaring.isGreaterThan(i); i++) {
      scaledExp = BaseMatrixUtil.<S,M> multiply(scaledExp, scaledExp);
    }
    return scaledExp;
  }

  /**
   * の指数関数行列を求めます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param a 行列
   * @param tolerance 許容誤差
   * @return 指数関数行列
   */
  private static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> S[][] exp1(final S[][] a, final S tolerance) {
    final int rowSize = a.length;
    final int columnSize = a[0].length;
    
    S[][] x = BaseMatrixUtil.createUnit(a, rowSize, columnSize);
    S[][] ans = BaseMatrixUtil.createUnit(a, rowSize, columnSize);

    for (int n = 1; true; n++) {

      x = BaseMatrixUtil.<S,M> multiply(x, a);

      for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          x[i][j] = x[i][j].divide(n);
        }
      }

      boolean flag = true;
      out: for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          if (x[i][j].abs().isGreaterThan(tolerance.abs())) {
            flag = false;
            break out;
          }
        }
      }
      if (flag == true) {
        return ans;
      }

      ans = BaseMatrixUtil.<S,M> add(ans, x);      
    }
  }
}
