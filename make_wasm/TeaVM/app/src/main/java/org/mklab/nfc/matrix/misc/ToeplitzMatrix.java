/*
 * $Id: Toeplitz.java,v 1.15 2008/07/16 15:40:00 koga Exp $
 * 
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.matrix.misc;

import org.mklab.nfc.matrix.BaseMatrixOperator;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.IntMatrix;
import org.mklab.nfc.matrix.util.Makecolv;
import org.mklab.nfc.matrix.util.Makerowv;
import org.mklab.nfc.scalar.Scalar;


/**
 * テプリッツ行列を生成するクラスです。
 * 
 * <p>Toeplitz matrix
 * 
 * @author koga
 * @version $Revision: 1.15 $
 */
public final class ToeplitzMatrix {

  /**
   * 新しく生成された<code>ToeplitzMatrix</code>オブジェクトを初期化します。
   */
  private ToeplitzMatrix() {
    // nothing to do
  }

  /**
   * 対称(エルミート)テプリッツ行列を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param a データ
   * @return テプリッツ行列 (toeplitz matrix)
   */
  public static <S extends Scalar<S, M>, M extends BaseMatrixOperator<S, M>> M create(final M a) {
    final int size = a.getColumnSize();

    M x, y;
    if (size == 1) {
      x = a;
      y = a.transpose();
    } else {
      x = a.transpose();
      y = a;
      if (size > 0) {
        x.setElement(1, 1, y.getElement(1));
      }
    }

    final int nx = x.length();
    final int ny = y.length();
    x = Makecolv.makecolv(x);
    y = Makerowv.makerowv(y);
    final M ans = x.createOnes(nx, ny);

    for (int i = 1; i <= Math.min(nx, ny); i++) {
      ans.setSubMatrix(i, i, i, ny, y.getSubVector(1, ny - i + 1));
      ans.setSubMatrix(i, nx, i, i, x.getSubVector(1, nx - i + 1));
    }
    return ans;
  }

  /**
   * <code>a</code>が第１列、<code>b</code>が第１行の 非対称テプリッツ行列を返します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param a データ1
   * @param b データ2
   * @return テプリッツ行列 (toeplitz matrix)
   */
  public static <S extends Scalar<S, M>, M extends BaseMatrixOperator<S, M>> M create(final M a, final M b) {
    M x = a;
    M y = b;

    if (x.isEmpty() == false && y.isEmpty() == false) {
      final S y1 = y.getElement(1);
      final S x1 = x.getElement(1);
      if (y1.equals(x1) == false) {
        throw new IllegalArgumentException(Messages.getString("Toeplitz.0")); //$NON-NLS-1$
      }
    }

    final int nx = x.length();
    final int ny = y.length();
    x = Makecolv.makecolv(x);
    y = Makerowv.makerowv(y);
    final M ans = x.createOnes(nx, ny);

    for (int i = 1; i <= Math.min(nx, ny); i++) {
      ans.setSubMatrix(i, i, i, ny, y.getSubVector(1, ny - i + 1));
      ans.setSubMatrix(i, nx, i, i, x.getSubVector(1, nx - i + 1));
    }

    return ans;
  }
  
  /**
   * 対称(エルミート)テプリッツ行列を返します。
   * 
   * @param a データ
   * @return テプリッツ行列 (toeplitz matrix)
   */
  public static DoubleMatrix create(final DoubleMatrix a) {
    final int size = a.getColumnSize();

    DoubleMatrix x, y;
    if (size == 1) {
      x = a;
      y = a.transpose();
    } else {
      x = a.transpose();
      y = a;
      if (size > 0) {
        x.setElement(1, 1, y.getDoubleElement(1));
      }
    }

    final int nx = x.length();
    final int ny = y.length();
    x = Makecolv.makecolv(x);
    y = Makerowv.makerowv(y);
    final DoubleMatrix ans = x.createOnes(nx, ny);

    for (int i = 1; i <= Math.min(nx, ny); i++) {
      ans.setSubMatrix(i, i, i, ny, y.getSubVector(1, ny - i + 1));
      ans.setSubMatrix(i, nx, i, i, x.getSubVector(1, nx - i + 1));
    }
    return ans;
  }

  /**
   * <code>a</code>が第１列、<code>b</code>が第１行の 非対称テプリッツ行列を返します。
   * 
   * @param a データ1
   * @param b データ2
   * @return テプリッツ行列 (toeplitz matrix)
   */
  public static DoubleMatrix create(final DoubleMatrix a, final DoubleMatrix b) {
    DoubleMatrix x = a;
    DoubleMatrix y = b;

    if (x.isEmpty() == false && y.isEmpty() == false) {
      final double y1 = y.getDoubleElement(1);
      final double x1 = x.getDoubleElement(1);
      if (y1 != x1) {
        throw new IllegalArgumentException(Messages.getString("Toeplitz.0")); //$NON-NLS-1$
      }
    }

    final int nx = x.length();
    final int ny = y.length();
    x = Makecolv.makecolv(x);
    y = Makerowv.makerowv(y);
    final DoubleMatrix ans = x.createOnes(nx, ny);

    for (int i = 1; i <= Math.min(nx, ny); i++) {
      ans.setSubMatrix(i, i, i, ny, y.getSubVector(1, ny - i + 1));
      ans.setSubMatrix(i, nx, i, i, x.getSubVector(1, nx - i + 1));
    }

    return ans;
  }
  
  /**
   * 対称(エルミート)テプリッツ行列を返します。
   * 
   * @param a データ
   * @return テプリッツ行列 (toeplitz matrix)
   */
  public static IntMatrix create(final IntMatrix a) {
    final int size = a.getColumnSize();

    IntMatrix x, y;
    if (size == 1) {
      x = a;
      y = a.transpose();
    } else {
      x = a.transpose();
      y = a;
      if (size > 0) {
        x.setElement(1, 1, y.getIntElement(1));
      }
    }

    final int nx = x.length();
    final int ny = y.length();
    x = Makecolv.makecolv(x);
    y = Makerowv.makerowv(y);
    final IntMatrix ans = x.createOnes(nx, ny);

    for (int i = 1; i <= Math.min(nx, ny); i++) {
      ans.setSubMatrix(i, i, i, ny, y.getSubVector(1, ny - i + 1));
      ans.setSubMatrix(i, nx, i, i, x.getSubVector(1, nx - i + 1));
    }
    return ans;
  }

  /**
   * <code>a</code>が第１列、<code>b</code>が第１行の 非対称テプリッツ行列を返します。
   * 
   * @param a データ1
   * @param b データ2
   * @return テプリッツ行列 (toeplitz matrix)
   */
  public static IntMatrix create(final IntMatrix a, final IntMatrix b) {
    IntMatrix x = a;
    IntMatrix y = b;

    if (x.isEmpty() == false && y.isEmpty() == false) {
      final double y1 = y.getIntElement(1);
      final double x1 = x.getIntElement(1);
      if (y1 != x1) {
        throw new IllegalArgumentException(Messages.getString("Toeplitz.0")); //$NON-NLS-1$
      }
    }

    final int nx = x.length();
    final int ny = y.length();
    x = Makecolv.makecolv(x);
    y = Makerowv.makerowv(y);
    final IntMatrix ans = x.createOnes(nx, ny);

    for (int i = 1; i <= Math.min(nx, ny); i++) {
      ans.setSubMatrix(i, i, i, ny, y.getSubVector(1, ny - i + 1));
      ans.setSubMatrix(i, nx, i, i, x.getSubVector(1, nx - i + 1));
    }

    return ans;
  }

}
