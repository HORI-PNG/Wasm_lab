/*
 * $Id: DoubleComplexExponentialMatrix.java,v 1.3 2008/02/03 12:43:17 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.elf;

import org.mklab.nfc.matrix.DoubleComplexMatrixUtil;
import org.mklab.nfc.matrix.DoubleMatrixUtil;
import org.mklab.nfc.scalar.DoubleComplexNumberUtil;
import org.mklab.nfc.scalar.DoubleNumberUtil;


/**
 * 倍精度(double)型の複素行列の指数関数行列を求めるためのクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.3 $
 */
public final class DoubleComplexExponentialMatrix {
  /**
   * 新しく生成された<code>DoubleComplexExponentialMatrix</code>オブジェクトを初期化します。
   */
  private DoubleComplexExponentialMatrix() {
    // nothing to do
  }

  /**
   * 複素行列の指数関数行列を返します。
   * 
   * <p>複素行列をAとするとき、このメソッドは
   * 
   * <blockquote> I + A + A^2/(2!) + ... + A^n/(n!) + ... </blockquote>
   * 
   * を求めます。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @return 指数関数行列
   */
  public static double[][][] exp(final double[][] aRe, final double[][] aIm) {
    return exp(aRe, aIm, DoubleNumberUtil.EPS * DoubleComplexMatrixUtil.frobNorm(aRe, aIm));
  }

  /**
   * 複素行列の指数関数行列を返します。
   * 
   * <p>複素行列をAとするとき、このメソッドは
   * 
   * <blockquote> I + A + A^2/(2!) + ... + A^n/(n!) + ... </blockquote>
   * 
   * を求めます。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @param tolerance 許容誤差
   * @return 指数関数行列
   */
  public static double[][][] exp(final double[][] aRe, final double[][] aIm, final double tolerance) {
    final int rowSize = aRe.length;
    final int columnSize = aRe[0].length;

    final double fNorm = DoubleComplexMatrixUtil.frobNorm(aRe, aIm);
    final int squaring = (int)Math.ceil(Math.log(fNorm) / Math.log(2));

    final double[][] scalingMatrixR = new double[rowSize][columnSize];
    final double[][] scalingMatrixI = new double[rowSize][columnSize];
    final double d = 1 / Math.pow(2, squaring);
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        scalingMatrixR[i][j] = aRe[i][j] * d;
        scalingMatrixI[i][j] = aIm[i][j] * d;
      }
    }

    double[][][] scalingMatrix = exp1(scalingMatrixR, scalingMatrixI, tolerance);

    for (int i = 0; i < squaring; i++) {
      scalingMatrix = DoubleComplexMatrixUtil.multiply(scalingMatrix[0], scalingMatrix[1], scalingMatrix[0], scalingMatrix[1]);
    }
    return scalingMatrix;
  }

  /**
   * 複素行列の指数関数行列を求めます。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @param tolerance 許容誤差
   * @return 指数関数行列
   */
  private static double[][][] exp1(final double[][] aRe, final double[][] aIm, final double tolerance) {
    final int rowSize = aRe.length;
    final int columnSize = aRe[0].length;

    final double[][] xRe = DoubleMatrixUtil.createUnit(rowSize, columnSize);
    final double[][] xIm = new double[rowSize][columnSize];
    double[][][] x = new double[][][] {xRe, xIm};

    final double[][] ansRe = DoubleMatrixUtil.createUnit(rowSize, columnSize);
    final double[][] ansIm = new double[rowSize][columnSize];
    final double[][][] ans = new double[][][] {ansRe, ansIm};

    for (int n = 1; true; n++) {
      x = DoubleComplexMatrixUtil.multiply(x[0], x[1], aRe, aIm);

      final double tmp = 1.0 / n;
      for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          x[0][i][j] *= tmp;
          x[1][i][j] *= tmp;
        }
      }
      boolean flag = true;
      out: for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          if (DoubleComplexNumberUtil.abs(x[0][i][j], x[1][i][j]) > tolerance) {
            flag = false;
            break out;
          }
        }
      }
      if (flag == true) {
        return ans;
      }
      DoubleComplexMatrixUtil.addSelf(ans[0], ans[1], x[0], x[1]);

    }
  }

}
