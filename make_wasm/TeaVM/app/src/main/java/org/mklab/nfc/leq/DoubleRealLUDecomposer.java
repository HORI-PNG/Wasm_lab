/*
 * $Id: DoubleRealLU.java,v 1.4 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.leq;

import org.mklab.nfc.matrix.DoubleMatrixUtil;
import org.mklab.nfc.scalar.DoubleNumberUtil;


/**
 * 倍精度(double)型の実行列のLU分解(P*A=L*U)を求めるためのクラスです。
 * 
 * Numerical Recipes in C 2.3節 参照
 * 
 * @author koga
 * @version $Revision: 1.4 $
 */
public final class DoubleRealLUDecomposer {

  /**
   * 実行列のLU分解を行います。
   * 
   * @param matrix 対象となる行列
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return LU分解の結果
   */
  private Object[] luDecompose(final double[][] matrix, final double tolerance, final boolean stopIfSingular) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final double[][] lu = DoubleMatrixUtil.clone(matrix);
    final int[] index = new int[rowSize];
    final double[] scaling = new double[rowSize];
    final int[] sign = new int[] {1};

    for (int i = 0; i < rowSize; i++) {
      double max = Math.abs(lu[i][0]);
      for (int j = 1; j < columnSize; j++) {
        final double temp = lu[i][j];
        if (Math.abs(temp) > max) {
          max = Math.abs(temp);
        }
      }

      if (max < tolerance) {
        if (stopIfSingular) {
          throw new IllegalArgumentException(Messages.getString("DoubleRealLU.0")); //$NON-NLS-1$
        }
        System.err.println(Messages.getString("DoubleRealLU.0")); //$NON-NLS-1$
        final double eps = DoubleNumberUtil.EPS;
        if (max < eps) {
          max = max + eps;
        }
      }

      scaling[i] = 1 / max;
    }

    for (int j = 0; j < columnSize; j++) {
      for (int i = 0; i < j; i++) {
        double sum = lu[i][j];
        for (int k = 0; k < i; k++) {
          sum -= lu[i][k] * lu[k][j];
        }
        lu[i][j] = sum;
      }

      double max = 0.;
      int maxIndex = 0;

      for (int i = j; i < rowSize; i++) {
        double sum = lu[i][j];
        for (int k = 0; k < j; k++) {
          sum -= lu[i][k] * lu[k][j];
        }

        lu[i][j] = sum;
        final double temp = scaling[i] * Math.abs(sum);
        if (temp >= max) {
          max = temp;
          maxIndex = i;
        }
      }

      if (j != maxIndex) {
        for (int k = 0; k < columnSize; k++) {
          final double temp = lu[maxIndex][k];
          lu[maxIndex][k] = lu[j][k];
          lu[j][k] = temp;
        }
        sign[0] = -sign[0];
        scaling[maxIndex] = scaling[j];
      }

      index[j] = maxIndex;

      if (lu[j][j] == 0) {
        if (stopIfSingular) {
          throw new IllegalArgumentException(Messages.getString("DoubleRealLU.0")); //$NON-NLS-1$
        }
        System.err.println(Messages.getString("DoubleRealLU.0")); //$NON-NLS-1$
        lu[j][j] = DoubleNumberUtil.EPS;
      }
      if (j != columnSize) {
        final double temp = 1 / lu[j][j];
        for (int i = j + 1; i < rowSize; i++) {
          lu[i][j] *= temp;
        }
      }
    }
    return new Object[] {lu, index, sign};
  }

  /**
   * 実行列をLU分解します。
   * 
   * <p>元の行列をA、下三角行列を行置換した行列をL、上三角行列をU とすると、これらの行列の間には、
   * 
   * <blockquote> A = Q * H * Q <sup>T </sup> </blockquote>
   * 
   * <blockquote> Q <sup>T </sup>* Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param matrix 対象となる行列
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return LとU
   */
  public DoubleLUDecompositionElements decompose(final double[][] matrix, final double tolerance, final boolean stopIfSingular) {
    final Object[] luD = luDecompose(matrix, tolerance, stopIfSingular);
    final double[][] lu = (double[][])luD[0];
    final int[] index = (int[])luD[1];

    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final double[][] lowerElement = new double[rowSize][columnSize];
    final double[][] upperElement = new double[rowSize][columnSize];

    // l行列とu行列に分解
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        for (int k = 0; k < columnSize; k++) {
          if (k < i) {
            lowerElement[i][k] = lu[i][k];
            upperElement[i][k] = 0;
          } else if (k == i) {
            lowerElement[i][k] = 1;
            upperElement[i][k] = lu[i][k];
          } else {
            lowerElement[i][k] = 0;
            upperElement[i][k] = lu[i][k];
          }
        }
      }
    }
    // 行入れ替え
    for (int i = rowSize - 1; i >= 0; i--) {
      final int k = index[i];
      if (k != i) {
        for (int j = 0; j < rowSize; j++) {
          double tmp = lowerElement[i][j];
          lowerElement[i][j] = lowerElement[k][j];
          lowerElement[k][j] = tmp;
        }
      }
    }
    return new DoubleLUDecompositionElements(lowerElement, upperElement);
  }

  /**
   * 実行列を並べ替え付きLU分解します。
   * 
   * <p>元の実行列をA、下三角行列を行置換した行列をL、上三角行列をU、 置換行列をPとすると、これらの行列の間には、
   * 
   * <blockquote> P * A = Q * H * Q <sup>T </sup> </blockquote>
   * 
   * <blockquote> Q <sup>T </sup>* Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param matrix 対象となる行列
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return LとUとP
   */
  public DoubleLUDecompositionElements decomposeWithPermutation(final double[][] matrix, final double tolerance, final boolean stopIfSingular) {
    final Object[] luD = luDecompose(matrix, tolerance, stopIfSingular);
    final double[][] lu = (double[][])luD[0];
    final int[] index = (int[])luD[1];

    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final double[][] lowerElement = new double[rowSize][columnSize];
    final double[][] upperElement = new double[rowSize][columnSize];

    // l行列とu行列に分解
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        for (int k = 0; k < columnSize; k++) {
          if (k < i) {
            lowerElement[i][k] = lu[i][k];
            upperElement[i][k] = 0.;
          } else if (k == i) {
            lowerElement[i][k] = 1.;
            upperElement[i][k] = lu[i][k];
          } else {
            lowerElement[i][k] = 0.;
            upperElement[i][k] = lu[i][k];
          }
        }
      }
    }

    // 行入れ替え行列p
    final int[][] p = new int[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      p[i][i] = 1;
    }

    // for (int i = rowSize - 1; i >= 0; i--) {
    for (int i = 0; i < rowSize; i++) { // / 02/10/28
      final int k = index[i];
      if (k != i) {
        for (int j = 0; j < rowSize; j++) {
          final int tmp = p[i][j];
          p[i][j] = p[k][j];
          p[k][j] = tmp;
        }
      }
    }
    return new DoubleLUDecompositionElements(lowerElement, upperElement, p);
  }

  /**
   * 線形方程式の解を返します。
   * 
   * @param luD 係数行列のLU分解
   * @param b 右辺のベクトル
   * @return 線形方程式の解
   */
  private double[] solveLinearEquation(final Object[] luD, final double[] b) {
    final double[][] lu = (double[][])luD[0];
    final int[] index = (int[])luD[1];

    final int columnSize = lu.length;
    final int rowSize = lu[0].length;

    final double[] x = new double[columnSize];
    for (int i = 0; i < columnSize; i++) {
      x[i] = b[i];
    }

    int ii = -1;
    for (int i = 0; i < rowSize; i++) {
      int ip = index[i];
      double sum = x[ip];
      x[ip] = x[i];
      if (ii >= 0) {
        for (int j = ii; j < i; j++) {
          sum -= lu[i][j] * x[j];
        }
      } else if (sum != 0) {
        ii = i;
      }
      x[i] = sum;
    }

    for (int i = rowSize - 1; i >= 0; i--) {
      double sum = x[i];
      for (int j = i + 1; j < rowSize; j++) {
        sum -= lu[i][j] * x[j];
      }
      x[i] = sum / lu[i][i];
    }
    return x;
  }

  /**
   * 行列の逆行列を返します。
   * 
   * @param a 対象となる行列
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return 逆行列
   */
  public double[][] inverse(final double[][] a, final double tolerance, final boolean stopIfSingular) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    if (rowSize != columnSize) {
      throw new IllegalArgumentException(Messages.getString("DoubleRealLU.2")); //$NON-NLS-1$
    }

    final double[][] inversedMatrix = new double[rowSize][columnSize];
    final Object[] lu = luDecompose(a, tolerance, stopIfSingular);

    for (int j = 0; j < rowSize; j++) {
      final double[] b = new double[rowSize];
      b[j] = 1.0;
      final double[] x = solveLinearEquation(lu, b);

      for (int i = 0; i < rowSize; i++) {
        inversedMatrix[i][j] = x[i];
      }
    }
    return inversedMatrix;
  }

  /**
   * 線形方程式の解を返します。
   * 
   * <p>線形方程式
   * 
   * <blockquote> A * x = B </blockquote>
   * 
   * の解xを返します。
   * 
   * @param a 行列
   * @param b 行列
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return 線形方程式の解
   */
  public double[][] leftDivide(final double[][] a, final double[][] b, final double tolerance, final boolean stopIfSingular) {
    final int rowSize1 = a.length;
    final int columnSize1 = rowSize1 == 0 ? 0 : a[0].length;
    final int columnSize2 = b.length == 0 ? 0 : b[0].length;

    final Object[] lu = luDecompose(a, tolerance, stopIfSingular);
    
    final double[][] ans = new double[columnSize1][columnSize2]; // 11/28
    for (int j = 0; j < columnSize2; j++) {
      final double[] bb = new double[rowSize1];
      for (int i = 0; i < rowSize1; i++) {
        bb[i] = b[i][j];
      }
      
      final double[] x = solveLinearEquation(lu, bb);
      for (int i = 0; i < rowSize1; i++) {
        ans[i][j] = x[i];
      }
    }
    return ans;
  }

  /**
   * 行列式を返します。
   * 
   * @param matrix 対象となる行列
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return 行列式
   */
  public double getDeterminant(final double[][] matrix, final double tolerance, final boolean stopIfSingular) {
    final Object[] lu = luDecompose(matrix, tolerance, stopIfSingular);
    final double[][] lower = (double[][])lu[0];
    final int[] sign = (int[])lu[2];

    double determinant = 1;
    for (int i = 0; i < lower.length; i++) {
      determinant *= lower[i][i];
    }
    return determinant * sign[0];
  }
}