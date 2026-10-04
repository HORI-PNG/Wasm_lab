/*
 * $Id: DoubleComplexLU.java,v 1.4 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.leq;

import org.mklab.nfc.matrix.DoubleMatrixUtil;
import org.mklab.nfc.scalar.DoubleComplexNumberUtil;
import org.mklab.nfc.scalar.DoubleNumberUtil;


/**
 * 倍精度(double)型の複素行列のLU分解(P*A=L*U)を求めるためのクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.4 $
 */
public final class DoubleComplexLUDecomposer {

  /**
   * 複素行列のLU分解を行います。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return LU分解の結果
   */
  private Object[] luDecompose(final double[][] aRe, final double[][] aIm, final double tolerance, boolean stopIfSingular) {
    final int rowSize = aRe.length;
    final int columnSize = aRe[0].length;

    final double[][] luRe = DoubleMatrixUtil.clone(aRe);
    final double[][] luIm = DoubleMatrixUtil.clone(aIm);

    final int[] index = new int[rowSize];
    final double[] scaling = new double[rowSize];
    final int[] sign = new int[] {1};

    for (int i = 0; i < rowSize; i++) {
      double max = 0.;
      for (int j = 0; j < columnSize; j++) {
        final double temp = DoubleComplexNumberUtil.abs(luRe[i][j], luIm[i][j]);
        if (temp > max) {
          max = temp;
        }
      }
      if (Math.abs(max) < tolerance) {
        if (stopIfSingular) {
          throw new IllegalArgumentException(Messages.getString("DoubleComplexLU.0")); //$NON-NLS-1$
        }
        System.err.println(Messages.getString("DoubleComplexLU.0")); //$NON-NLS-1$
        final double eps = DoubleNumberUtil.EPS;
        if (max < eps) {
          max = max + eps;
        }
      }
      scaling[i] = 1 / max;
    }

    for (int j = 0; j < columnSize; j++) {
      for (int i = 0; i < j; i++) {
        double sumRe = luRe[i][j];
        double sumIm = luIm[i][j];
        for (int k = 0; k < i; k++) {
          // sum = sum.sub(lu[i][k].multiply(lu[k][j]));
          sumRe -= luRe[i][k] * luRe[k][j] - luIm[i][k] * luIm[k][j];
          sumIm -= luRe[i][k] * luIm[k][j] + luIm[i][k] * luRe[k][j];
        }
        // lu[i][j] = (Complex)sum.clone();
        luRe[i][j] = sumRe;
        luIm[i][j] = sumIm;
      }

      double max = 0.;
      int maxIndex = 0;
      for (int i = j; i < rowSize; i++) {
        double sumRe = luRe[i][j];
        double sumIm = luIm[i][j];
        for (int k = 0; k < j; k++) {
          // sum = sum.sub(lu[i][k].multiply(lu[k][j]));
          sumRe -= luRe[i][k] * luRe[k][j] - luIm[i][k] * luIm[k][j];
          sumIm -= luRe[i][k] * luIm[k][j] + luIm[i][k] * luRe[k][j];
        }
        // lu[i][j] = (Complex)sum.clone();
        luRe[i][j] = sumRe;
        luIm[i][j] = sumIm;

        double temp = scaling[i] * DoubleComplexNumberUtil.abs(sumRe, sumIm);
        if (temp >= max) {
          max = temp;
          maxIndex = i;
        }
      }

      if (j != maxIndex) {
        for (int k = 0; k < columnSize; k++) {
          // Complex temp = (Complex)lu[maxIndex][k].clone();
          final double tmpRe = luRe[maxIndex][k];
          final double tmpIm = luIm[maxIndex][k];
          // lu[maxIndex][k] = (Complex)lu[j][k].clone();
          luRe[maxIndex][k] = luRe[j][k];
          luIm[maxIndex][k] = luIm[j][k];
          // lu[j][k] = (Complex)temp.clone();
          luRe[j][k] = tmpRe;
          luIm[j][k] = tmpIm;
        }
        sign[0] = -sign[0];
        scaling[maxIndex] = scaling[j];
      }

      index[j] = maxIndex;

      // if (lu[j][j].abs() == 0.) {
      if (DoubleComplexNumberUtil.abs(luRe[j][j], luIm[j][j]) == 0.0) {
        if (stopIfSingular) {
          throw new IllegalArgumentException(Messages.getString("DoubleComplexLU.0")); //$NON-NLS-1$
        }
        System.err.println(Messages.getString("DoubleComplexLU.0")); //$NON-NLS-1$
        // lu[j][j] = new Complex(Double.MIN_VALUE ,0);
        luRe[j][j] = DoubleNumberUtil.EPS;
        luIm[j][j] = 0;
      }

      if (j != columnSize) {
        // Complex temp = lu[j][j].inverse();
        final double den = luRe[j][j] * luRe[j][j] + luIm[j][j] * luIm[j][j];
        final double tmpRe = luRe[j][j] / den;
        final double tmpIm = -luIm[j][j] / den;

        for (int i = j + 1; i < rowSize; i++) {
          // lu[i][j] = lu[i][j].multiply(temp);
          // lur[i][j] = lur[i][j]*tempr - lui[i][j]*tempi;
          final double tmp = luRe[i][j] * tmpRe - luIm[i][j] * tmpIm;
          luIm[i][j] = luRe[i][j] * tmpIm + luIm[i][j] * tmpRe;
          luRe[i][j] = tmp;
        }
      }
    }

    return new Object[] {luRe, luIm, index, sign};
  }

  /**
   * 複素行列のLU分解を返します。
   * 
   * <p>複素行列をA、下三角行列を行置換した行列をL、上三角行列をUとすると、これらの行列の間には、
   * 
   * <blockquote> A = Q * H * Q<sup>#</sup> </blockquote>
   * 
   * <blockquote> Q<sup>#</sup> * Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return Lの実部、Lの虚部、Uの実部、Uの虚部
   */
  public DoubleComplexLUDecompositionElements decompose(final double[][] aRe, final double[][] aIm, final double tolerance, boolean stopIfSingular) {
    final int rowSize = aRe.length;
    final int columnSize = aRe[0].length;

    final Object[] luD = luDecompose(aRe, aIm, tolerance, stopIfSingular);

    final double[][] luRe = (double[][])luD[0];
    final double[][] luIm = (double[][])luD[1];
    final int[] index = (int[])luD[2];

    final double[][] lRe = new double[rowSize][columnSize];
    final double[][] lIm = new double[rowSize][columnSize];
    final double[][] uRe = new double[rowSize][columnSize];
    final double[][] uIm = new double[rowSize][columnSize];

    // l行列とu行列に分解
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        for (int k = 0; k < columnSize; k++) {
          if (k < i) {
            lRe[i][k] = luRe[i][k];
            lIm[i][k] = luIm[i][k];
            uRe[i][k] = 0;
            uIm[i][k] = 0;
          } else if (k == i) {
            lRe[i][k] = 1;
            lIm[i][k] = 0;
            uRe[i][k] = luRe[i][k];
            uIm[i][k] = luIm[i][k];
          } else {
            lRe[i][k] = 0;
            lIm[i][k] = 0;
            uRe[i][k] = luRe[i][k];
            uIm[i][k] = luIm[i][k];
          }
        }
      }
    }

    // 行入れ替え
    for (int i = rowSize - 1; i >= 0; i--) {
      int k = index[i];
      if (k != i) {
        for (int j = 0; j < rowSize; j++) {
          final double tmpRe = lRe[i][j];
          final double tmpIm = lIm[i][j];
          lRe[i][j] = lRe[k][j];
          lIm[i][j] = lIm[k][j];
          lRe[k][j] = tmpRe;
          lIm[k][j] = tmpIm;
        }
      }

    }

    return new DoubleComplexLUDecompositionElements(lRe, lIm, uRe, uIm);
  }

  /**
   * 複素行列の並べ替え付きLU分解を返します。
   * 
   * <p>複素行列をA、下三角行列を行置換した行列をL、上三角行列をU、置換行列をPとすると、これらの行列の間には、
   * 
   * <blockquote> P * A = Q * H * Q<sup>#</sup> </blockquote>
   * 
   * <blockquote> Q<sup>#</sup> * Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return Lの実部、Lの虚部、Uの実部、Uの虚部、Pの実部、Pの虚部
   */
  public DoubleComplexLUDecompositionElements decomposeWithPermutation(final double[][] aRe, final double[][] aIm, final double tolerance, boolean stopIfSingular) {
    final int rowSize = aRe.length;
    final int columnSize = aRe[0].length;
    final Object[] luD = luDecompose(aRe, aIm, tolerance, stopIfSingular);

    final double[][] luRe = (double[][])luD[0];
    final double[][] luIm = (double[][])luD[1];
    final int[] index = (int[])luD[2];

    final double[][] lRe = new double[rowSize][columnSize];
    final double[][] lIm = new double[rowSize][columnSize];

    final double[][] uRe = new double[rowSize][columnSize];
    final double[][] uIm = new double[rowSize][columnSize];

    // l行列とu行列に分解
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        for (int k = 0; k < columnSize; k++) {
          if (k < i) {
            lRe[i][k] = luRe[i][k]; // l[i][k] = lu[i][k];
            lIm[i][k] = luIm[i][k];
            uRe[i][k] = 0; // u = (0,0);
            uIm[i][k] = 0; // u = (0,0);
          } else if (k == i) {
            lRe[i][k] = 1; // l[i][k] = (1,0);
            lIm[i][k] = 0;
            uRe[i][k] = luRe[i][k];
            uIm[i][k] = luIm[i][k];
          } else {
            lRe[i][k] = 0; // l[i][k] = (0,0);
            lIm[i][k] = 0; // l[i][k] = (0,0);
            uRe[i][k] = luRe[i][k]; // u[i][k] = lu[i][k];
            uIm[i][k] = luIm[i][k];
          }
        }
      }
    }

    // 行入れ替え
    final int[][] p = new int[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      p[i][i] = 1;
    }

    for (int i = rowSize - 1; i >= 0; i--) {
      final int k = index[i];
      if (k != i) {
        for (int j = 0; j < rowSize; j++) {
          final double tmpRe = lRe[i][j]; // temp = l[i][j];
          final double tmpIm = lIm[i][j];
          lRe[i][j] = lRe[k][j]; // l[i][j] = l[k][j];
          lIm[i][j] = lIm[k][j];
          lRe[k][j] = tmpRe; // l_element[k][j] = temp;
          lIm[k][j] = tmpIm; // l_element[k][j] = temp;
        }
      }
    }

    return new DoubleComplexLUDecompositionElements(lRe, lIm, uRe, uIm, p);
  }

  /**
   * 線形方程式の解を返します。
   * 
   * @param lu 変数ベクトルの係数行列のLU分解の結果
   * @param bRe 右辺のベクトルの実部
   * @param bIm 右辺のベクトルの虚部
   * @return 解の実部と虚部
   */
  private double[][] solveLinearEquation(final Object[] lu, final double[] bRe, final double[] bIm) {
    final double[][] luRe = (double[][])lu[0];
    final double[][] luIm = (double[][])lu[1];
    final int[] index = (int[])lu[2];

    final int columnSize = luRe.length;
    final int rowSize = luRe[0].length;

//    if (columnSize < bRe.length) {
//      throw new IllegalArgumentException(Messages.getString("DoubleComplexLU.1")); //$NON-NLS-1$
//    }

    final double[] xRe = new double[columnSize];
    final double[] xIm = new double[columnSize];

    for (int i = 0; i < columnSize; i++) {
      xRe[i] = bRe[i];
      xIm[i] = bIm[i];
    }

    int ii = -1;
    for (int i = 0; i < rowSize; i++) {
      int ip = index[i];
      double sumRe = xRe[ip];
      double sumIm = xIm[ip];
      xRe[ip] = xRe[i];
      xIm[ip] = xIm[i];

      if (ii >= 0) {
        for (int j = ii; j < i; j++) {
          sumRe -= luRe[i][j] * xRe[j] - luIm[i][j] * xIm[j];
          sumIm -= luRe[i][j] * xIm[j] + luIm[i][j] * xRe[j];
        }
      } else if (DoubleComplexNumberUtil.abs(sumRe, sumIm) != 0) {
        ii = i;
      }

      xRe[i] = sumRe;
      xIm[i] = sumIm;
    }

    for (int i = rowSize - 1; i >= 0; i--) {
      double sumRe = xRe[i];
      double sumIm = xIm[i];

      for (int j = i + 1; j < rowSize; j++) {
        sumRe -= luRe[i][j] * xRe[j] - luIm[i][j] * xIm[j];
        sumIm -= luRe[i][j] * xIm[j] + luIm[i][j] * xRe[j];
      }

      final double den = luRe[i][i] * luRe[i][i] + luIm[i][i] * luIm[i][i];
      final double invRe = luRe[i][i] / den;
      final double invIm = -luIm[i][i] / den;
      xRe[i] = sumRe * invRe - sumIm * invIm;
      xIm[i] = sumRe * invIm + sumIm * invRe;
    }

    return new double[][] {xRe, xIm};
  }

  /**
   * 複素行列の逆行列を返します。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます
   * @return 複素行列の逆行列
   */
  public double[][][] inverse(final double[][] aRe, final double[][] aIm, final double tolerance, final boolean stopIfSingular) {
    final int rowSize = aRe.length;
    final int columnSize = rowSize == 0 ? 0 : aRe[0].length;

    if (rowSize != columnSize) {
      throw new IllegalArgumentException(Messages.getString("DoubleComplexLU.2")); //$NON-NLS-1$
    }

    final double[][] inversedMatrixRe = new double[rowSize][columnSize];
    final double[][] inversedMatrixIm = new double[rowSize][columnSize];
    final Object[] lu = luDecompose(aRe, aIm, tolerance, stopIfSingular);

    for (int j = 0; j < rowSize; j++) {
      final double[] bRe = new double[rowSize];
      final double[] bIm = new double[rowSize];
      bRe[j] = 1;

      final double[][] x = solveLinearEquation(lu, bRe, bIm);
      for (int i = 0; i < rowSize; i++) {
        inversedMatrixRe[i][j] = x[0][i];
        inversedMatrixIm[i][j] = x[1][i];
      }
    }
    return new double[][][] {inversedMatrixRe, inversedMatrixIm};
  }

  /**
   * @param aRe 左行列の実部
   * @param aIm 左行列の虚部
   * @param bRe 右行列の実部
   * @param bIm 右行列の虚部
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return 割り算の結果の実部と虚部
   */
  public double[][][] leftDivide(final double[][] aRe, final double[][] aIm, final double[][] bRe, final double[][] bIm, final double tolerance, final boolean stopIfSingular) {
    final int rowSize1 = aRe.length;
    final int columnSize1 = rowSize1 == 0 ? 0 : aRe[0].length;
    final int columnSize2 = bRe.length == 0 ? 0 : bRe[0].length;

    final Object[] lu = luDecompose(aRe, aIm, tolerance, stopIfSingular);

    final double[][] ansRe = new double[columnSize1][columnSize2];
    final double[][] asnIm = new double[columnSize1][columnSize2];

    for (int j = 0; j < columnSize2; j++) {
      final double[] bbRe = new double[rowSize1];
      final double[] bbIm = new double[rowSize1];
      for (int i = 0; i < rowSize1; i++) {
        bbRe[i] = bRe[i][j];
        bbIm[i] = bIm[i][j];
      }

      final double[][] x = solveLinearEquation(lu, bbRe, bbIm);

      for (int i = 0; i < rowSize1; i++) {
        ansRe[i][j] = x[0][i];
        asnIm[i][j] = x[1][i];
      }
    }
    return new double[][][] {ansRe, asnIm};
  }

  /**
   * 行列式を返します。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return 行列式(第1成分に実部,第2成分に虚部)
   */
  public double[] getDeterminant(final double[][] aRe, final double[][] aIm, final double tolerance, final boolean stopIfSingular) {
    final Object[] lu = luDecompose(aRe, aIm, tolerance, stopIfSingular);
    final double[][] lowerRe = (double[][])lu[0];
    final double[][] lowerIm = (double[][])lu[1];

    final int[] sign = (int[])lu[3];
    double determinantRe = 1;
    double determinantIm = 0;

    for (int i = 0; i < lowerRe.length; i++) {
      final double tmp = determinantRe * lowerRe[i][i] - determinantIm * lowerIm[i][i];
      determinantIm = determinantRe * lowerIm[i][i] + determinantIm * lowerRe[i][i];
      determinantRe = tmp;
    }

    return new double[] {sign[0] * determinantRe, sign[0] * determinantIm};
  }
}