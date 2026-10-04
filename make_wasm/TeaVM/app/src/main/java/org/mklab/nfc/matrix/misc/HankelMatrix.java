/*
 * $Id: Hankel.java,v 1.12 2008/07/16 15:40:00 koga Exp $
 * 
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.matrix.misc;

import org.mklab.nfc.matrix.BaseMatrixOperator;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.IntMatrix;
import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.matrix.util.Makecolv;
import org.mklab.nfc.scalar.Scalar;


/**
 * ハンケル行列を生成するクラスです。
 * 
 * <p> Hankel matrix
 * 
 * @author koga
 * @version $Revision: 1.12 $
 */
public final class HankelMatrix {

  /**
   * 新しく生成された<code>HankelMatrix</code>オブジェクトを初期化します。
   */
  private HankelMatrix() {
    // nothing to do
  }

  /**
   * 第1列が<code>a</code>であり、第1非対角より下の成分がゼロ であるハンンケル行列を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param a データ
   * @return ハンケル行列 (hankel matrix)
   */
  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> M create(final M a) {
    final M x = Makecolv.makecolv(a);
    final int nx = x.length();

    final M ans = x.createZero(nx, nx);
    for (int j = 1; j <= nx; j++) {
      ans.setSubMatrix(1, nx - j + 1, j, j, x.getSubVector(j, nx));
    }

    return ans;
  }

  /**
   * 第1列が<code>a</code>であり、最終行が<code>b</code>であるハンンケル行列 を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param a データ1
   * @param b データ2
   * @return ハンケル行列 (hankel matrix)
   */
  public static <S extends Scalar<S, M>, M extends BaseMatrixOperator<S, M>> M create(final M a, final M b) {
    final M x = Makecolv.makecolv(a);
    final M y = Makecolv.makecolv(b);

    final int nx = x.length();
    final int ny = y.length();

    final S lastX = x.getElement(nx);
    final S firstY = y.getElement(1);

    if (lastX.equals(firstY) == false) {
      throw new IllegalArgumentException(Messages.getString("HankelMatrix.0")); //$NON-NLS-1$
    }

    final M ans = x.createZero(nx, ny);

    for (int j = 1; j <= ny; j++) {
      if (j <= nx) {
        ans.setColumnVector(j, x.getSubVector(j, nx).appendDown(y.getSubVector(2, j)).getSubVector(1, nx));
      } else {
        ans.setColumnVector(j, y.getSubVector(j - nx + 1, j));
      }
    }

    return ans;
  }

  /**
   * 第1列が<code>a</code>であり、最終行が<code>b</code>であるハンンケル行列 を返します。
   * 
   * @param a データ1
   * @param b データ2
   * @return ハンケル行列 (hankel matrix)
   */
  public static DoubleMatrix create(final DoubleMatrix a, final DoubleMatrix b) {
    final DoubleMatrix x = Makecolv.makecolv(a);
    final DoubleMatrix y = Makecolv.makecolv(b);

    final int nx = x.length();
    final int ny = y.length();

    final double lastX = x.getDoubleElement(nx);
    final double firstY = y.getDoubleElement(1);

    if (lastX != firstY) {
      throw new IllegalArgumentException(Messages.getString("HankelMatrix.0")); //$NON-NLS-1$
    }

    final DoubleMatrix ans = x.createZero(nx, ny);

    for (int j = 1; j <= ny; j++) {
      if (j <= nx) {
        ans.setColumnVector(j, x.getSubVector(j, nx).appendDown(y.getSubVector(2, j)).getSubVector(1, nx));
      } else {
        ans.setColumnVector(j, y.getSubVector(j - nx + 1, j));
      }
    }

    return ans;
  }

  /**
   * 第1列が<code>a</code>であり、最終行が<code>b</code>であるハンンケル行列 を返します。
   * 
   * @param a データ1
   * @param b データ2
   * @return ハンケル行列 (hankel matrix)
   */
  public static IntMatrix create(final IntMatrix a, final IntMatrix b) {
    final IntMatrix x = Makecolv.makecolv(a);
    final IntMatrix y = Makecolv.makecolv(b);

    final int nx = x.length();
    final int ny = y.length();

    final int lastX = x.getIntElement(nx);
    final int firstY = y.getIntElement(1);

    if (lastX != firstY) {
      throw new IllegalArgumentException(Messages.getString("HankelMatrix.0")); //$NON-NLS-1$
    }

    final IntMatrix ans = x.createZero(nx, ny);

    for (int j = 1; j <= ny; j++) {
      if (j <= nx) {
        ans.setColumnVector(j, x.getSubVector(j, nx).appendDown(y.getSubVector(2, j)).getSubVector(1, nx));
      } else {
        ans.setColumnVector(j, y.getSubVector(j - nx + 1, j));
      }
    }

    return ans;
  }

}
