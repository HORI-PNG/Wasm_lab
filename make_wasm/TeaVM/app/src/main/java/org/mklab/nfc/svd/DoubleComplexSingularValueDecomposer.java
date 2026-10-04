/*
 * $Id: DoubleComplexSVD.java,v 1.4 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.svd;

import org.mklab.nfc.matrix.DoubleComplexMatrixUtil;
import org.mklab.nfc.matrix.DoubleMatrixUtil;


/**
 * 倍精度(double)型の複素行列の特異値分解を行うためのクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.4 $
 */
public final class DoubleComplexSingularValueDecomposer {
  /** 最大反復数 */
  private int maxIteration = 30;
  
  /**
   * 複素行列を特異値分解を返します。
   * 
   * <p>対象となる行列をA、特異値を対角成分とする対角行列を D、左特異ベクトルからなるユニタリー行列をU、右特異ベクトルからなるユニタリー行列をVとすると、
   * 
   * <blockquote> A = U * D * V <sup># </sup> </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @return Uの実部、Uの虚部、Dの実部、Dの虚部(全て零)、Vの実部、Vの虚部
   */
  public SingularValueDecompositionDoubleComplexElements decompose(final double[][] aRe, final double[][] aIm) {
    final int rowSize = aRe.length;
    final int columnSize = rowSize == 0 ? 0 : aRe[0].length;
    final int size = Math.min(rowSize, columnSize);

    final ComplexValue[][] a = new ComplexValue[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        a[i][j] = new ComplexValue(aRe[i][j], aIm[i][j]);
      }
    }

    final ComplexValue[][] val = new ComplexValue[size][1];
    final ComplexValue[][] lvec = new ComplexValue[rowSize][rowSize];
    final ComplexValue[][] rvec = new ComplexValue[columnSize][columnSize];

    for (int i = 0; i < size; i++) {
      val[i][0] = new ComplexValue(0, 0);
    }

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < rowSize; j++) {
        lvec[i][j] = new ComplexValue(0, 0);
      }
    }

    for (int i = 0; i < columnSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        rvec[i][j] = new ComplexValue(0, 0);
      }
    }

    matSVD(a, val, lvec, rvec);

    final double[][] leftVecRe = new double[rowSize][rowSize];
    final double[][] leftVecIm = new double[rowSize][rowSize];
    final double[][] rightVecRe = new double[columnSize][columnSize];
    final double[][] rightVecIm = new double[columnSize][columnSize];
    final double[][] values = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < rowSize; j++) {
        leftVecRe[i][j] = lvec[i][j].re;
        leftVecIm[i][j] = lvec[i][j].im;
      }
    }

    for (int i = 0; i < columnSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        rightVecRe[i][j] = rvec[i][j].re;
        rightVecIm[i][j] = rvec[i][j].im;
      }
    }

    for (int i = 0; i < size; i++) {
      values[i][i] = val[i][0].re;
    }

    return new SingularValueDecompositionDoubleComplexElements(leftVecRe, leftVecIm, values, rightVecRe, rightVecIm);
  }

  /**
   * 複素行列の特異値を返します。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @return 特異値
   */
  public double[] singularValue(final double[][] aRe, final double[][] aIm) {
    final int rowSize = aRe.length;
    final int columnSize = rowSize == 0 ? 0 : aRe[0].length;
    final int size = Math.min(rowSize, columnSize);

    final ComplexValue[][] a = new ComplexValue[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        a[i][j] = new ComplexValue(aRe[i][j], aIm[i][j]);
      }
    }

    final ComplexValue[][] val = new ComplexValue[size][1];
    final ComplexValue[][] lvec = new ComplexValue[rowSize][rowSize];
    final ComplexValue[][] rvec = new ComplexValue[columnSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < rowSize; j++) {
        lvec[i][j] = new ComplexValue(0, 0);
      }
    }

    for (int i = 0; i < columnSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        rvec[i][j] = new ComplexValue(0, 0);
      }
    }

    for (int i = 0; i < size; i++) {
      val[i][0] = new ComplexValue(0, 0);
    }

    matSVD(a, val, lvec, rvec);

    final double[] singularValues = new double[size];
    for (int i = 0; i < size; i++) {
      singularValues[i] = val[i][0].re;
    }

    return singularValues;
  }

  /**
   * 複素行列の最大特異値を返します。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @return 最大特異値
   */
  public double maximumSingularValue(final double[][] aRe, final double[][] aIm) {
    return singularValue(aRe, aIm)[0];
  }

  /**
   * 複素行列の最小特異値を返します。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @return 最小特異値
   */
  public double minimumSingularValue(final double[][] aRe, final double[][] aIm) {
    final double[] singularValues = singularValue(aRe, aIm);
    return singularValues[singularValues.length - 1];
  }

  /**
   * 複素行列が非正則であるか判定します。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @param tolerance 許容誤差
   * @return 非正則ならばtrue、そうでなければfalse
   */
  public boolean isSingular(final double[][] aRe, final double[][] aIm, final double tolerance) {
    final double[] singularValues = singularValue(aRe, aIm);
    return singularValues[singularValues.length - 1] < tolerance;
  }

  /**
   * 複素行列のランク(階数)を返します。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @param tolerance 許容誤差
   * @return ランク
   */
  public int rank(final double[][] aRe, final double[][] aIm, final double tolerance) {
    final int rowSize = aRe.length;
    final int columnSize = rowSize == 0 ? 0 : aRe[0].length;

    final double[][] a2Re, a2Im;
    if (rowSize > columnSize) {
      a2Re = DoubleMatrixUtil.transpose(aRe);
      a2Im = DoubleMatrixUtil.transpose(aIm);
    } else {
      a2Re = aRe;
      a2Im = aIm;
    }

    final double[] singularValues = singularValue(a2Re, a2Im);

    int rank = 0;
    for (int i = 0; i < singularValues.length; i++) {
      if (singularValues[i] > tolerance) {
        rank++;
      }
    }

    return rank;
  }

  /**
   * 複素行列がフルランクであるか判定します。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @param tolerance 許容誤差
   * @return フルランクならばtrue、そうでなければfalse
   */
  public boolean isFullRank(final double[][] aRe, final double[][] aIm, final double tolerance) {
    final int rowSize = aRe.length;
    final int columnSize = rowSize == 0 ? 0 : aRe[0].length;

    final int rank = rank(aRe, aIm, tolerance);

    if (rank == rowSize || rank == columnSize) {
      return true;
    }

    return false;
  }

  /**
   * 複素行列の擬似逆行列を返します。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @param tolerance 許容誤差
   * @return 擬似逆行列
   */
  public double[][][] pseudoInverse(final double[][] aRe, final double[][] aIm, final double tolerance) {
    final int rowSize = aRe.length;
    final int columnSize = rowSize == 0 ? 0 : aRe[0].length;

    SingularValueDecompositionDoubleComplexElements svd = decompose(aRe, aIm);

    final double[][] ur = svd.getReU();
    final double[][] ui = svd.getImU();
    final double[][] d = svd.getD();
    final double[][] vr = svd.getReV();
    final double[][] vi = svd.getImV();

    int rank = 0;
    for (int i = 0; i < Math.min(rowSize, columnSize); i++) {
      if (d[i][i] > tolerance) {
        rank++;
      }
    }

    final double[][] utr = DoubleMatrixUtil.transpose(ur);
    final double[][] uti = DoubleMatrixUtil.unaryMinus(DoubleMatrixUtil.transpose(ui));

    final double[][] zr = new double[columnSize][rowSize];
    final double[][] zi = new double[columnSize][rowSize];

    for (int i = 1; i <= rank; i++) {
      zr[i - 1][i - 1] = 1.0 / d[i - 1][i - 1];
    }

    final double[][][] tmp = DoubleComplexMatrixUtil.multiply(zr, zi, utr, uti);

    return DoubleComplexMatrixUtil.multiply(vr, vi, tmp[0], tmp[1]);
  }

  /**
   * 線形方程式の解を返します。
   * 
   * <p>実部aRe、虚部aImで表される複素行列をA、実部bRe、虚部bImで表される複素行列をBとすとき、
   * 
   * <blockquote> A * X = B </blockquote>
   * 
   * の解即ち
   * 
   * <blockquote> X = A<sup>-1</sup> * B </blockquote>
   * 
   * を返します。
   * 
   * @param aRe 係数行列の実部
   * @param aIm 係数行列の虚部
   * @param bRe 実部
   * @param bIm 虚部
   * @param toleance 許容誤差
   * @return 線形方程式の解
   */
  public double[][][] leastSquare(final double[][] aRe, final double[][] aIm, final double[][] bRe, final double[][] bIm, final double toleance) {
    final double[][][] aInv = pseudoInverse(aRe, aIm, toleance);
    return DoubleComplexMatrixUtil.multiply(aInv[0], aInv[1], bRe, bIm);
  }

  /**
   * 複素行列のカーネル(零空間)を張るベクトルからなる行列を返します。
   * 
   * <p>許容誤差より小さい特異値をゼロと見なします。
   * 
   * @param aRe 複素行列の実部
   * @param aIm 複素行列の虚部
   * @param tolerance 許容誤差
   * @return カーネル
   */
  public double[][][] kernel(final double[][] aRe, final double[][] aIm, final double tolerance) {
    final int rowSize = aRe.length;
    final int columnSize = rowSize == 0 ? 0 : aRe[0].length;

    final double[][] a2Re, a2Im;
    if (rowSize < columnSize) {
      a2Re = DoubleMatrixUtil.appendDown(aRe, new double[columnSize - rowSize][columnSize]);
      a2Im = DoubleMatrixUtil.appendDown(aIm, new double[columnSize - rowSize][columnSize]);
    } else {
      a2Re = aRe;
      a2Im = aIm;
    }

    SingularValueDecompositionDoubleComplexElements svd = decompose(a2Re, a2Im);
    final double[][] d = svd.getD();
    final double[][] rightVecRe = svd.getReV();
    final double[][] rightVecIm = svd.getImV();

    int rank = 0;
    for (int i = 0; i < Math.min(rowSize, columnSize); i++) {
      if (d[i][i] > tolerance) {
        rank++;
      }
    }

    if (rank == Math.max(rowSize, columnSize)) {
      return new double[][][]{new double[rowSize][], new double[rowSize][]};
      //throw new RuntimeException(Messages.getString("DoubleComplexSVD.0")); //$NON-NLS-1$
    }

    return new double[][][] {DoubleMatrixUtil.getSubMatrix(rightVecRe, 0, columnSize - 1, rank, columnSize - 1), DoubleMatrixUtil.getSubMatrix(rightVecIm, 0, columnSize - 1, rank, columnSize - 1)};
  }

  /**
   * 複素行列の最大特異値(2-ノルム)を返します。
   * 
   * @param ar 複素行列の実部
   * @param ai 複素行列の虚部
   * @return 最大特異値
   */
  public double norm(final double[][] ar, final double[][] ai) {
    return maximumSingularValue(ar, ai);
  }

  /**
   * 複素行列の条件数を返します。
   * 
   * @param ar 複素行列の実部
   * @param ai 複素行列の虚部
   * @return 条件数
   */
  public double conditionNumber(final double[][] ar, final double[][] ai) {
    if (ar.length == 0 || ar[0].length == 0) {
      return Double.NaN;
    }

    double[] singularValues = singularValue(ar, ai);

    if (DoubleMatrixUtil.anyZero(singularValues)) {
      return Double.POSITIVE_INFINITY;
    }

    return DoubleMatrixUtil.max(singularValues) / DoubleMatrixUtil.min(singularValues);
  }

  /**
   * @param a 対象となる行列
   * @param val 特異値
   * @param lvec 左特異ベクトル
   * @param rvec 右特異ベクトル
   */
  private void matSVD(final ComplexValue[][] a, final ComplexValue[][] val, final ComplexValue[][] lvec, final ComplexValue[][] rvec) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final int mnMin = Math.min(rowSize, columnSize);
    final int mnMax = Math.max(rowSize, columnSize);

    final ComplexValue[][] b;
    if (rowSize < columnSize) {
      b = ComplexValue.conjugateTranspose(a);
    } else {
      b = ComplexValue.duplicate(a);
    }

    final ComplexValue[][] rvec2 = new ComplexValue[mnMin][mnMin];
    final ComplexValue[][] lvec2 = new ComplexValue[mnMax][mnMax];
    final ComplexValue[][] eeee = new ComplexValue[mnMin][1];
    final ComplexValue[][] val2 = new ComplexValue[mnMin + 1][1];

    // 初期化
    for (int i = 0; i < mnMin; i++) {
      eeee[i][0] = new ComplexValue(0, 0);
      val2[i][0] = new ComplexValue(0, 0);
      for (int j = 0; j < mnMin; j++) {
        rvec2[i][j] = new ComplexValue(0, 0);
        lvec2[i][j] = new ComplexValue(0, 0);
      }
      for (int j = mnMin; j < mnMax; j++) {
        lvec2[i][j] = new ComplexValue(0, 0);
      }
    }
    val2[mnMin][0] = new ComplexValue(0, 0);

    for (int i = mnMin; i < mnMax; i++) {
      for (int j = 0; j < mnMax; j++) {
        lvec2[i][j] = new ComplexValue(0, 0);
      }
    }

    final int ierr = zsvdc(b, lvec2, val2, rvec2, eeee, true, true);

    if (0 < ierr) {
      throw new RuntimeException(Messages.getString("DoubleComplexSVD.3")); //$NON-NLS-1$
    }

    if (rowSize < columnSize) {
      ComplexValue.copy(rvec, lvec2);
      ComplexValue.copy(lvec, rvec2);
    } else {
      ComplexValue.copy(lvec, lvec2);
      ComplexValue.copy(rvec, rvec2);
    }

    for (int i = 0; i < mnMin; i++) {
      val[i][0].re = val2[i][0].re;
      val[i][0].im = 0;
    }

  }

  /**
   * @param x 対象となる行列
   * @param u 左特異ベクトル
   * @param s 特異値
   * @param v 右特異ベクトル
   * @param e 作業用行列
   * @param wantu 左特異ベクトルが必要ならばtrue
   * @param wantv 右特異ベクトルが必要ならばtrue
   * @return 特異値分解
   */
  private int zsvdc(final ComplexValue[][] x, final ComplexValue[][] u, final ComplexValue[][] s, final ComplexValue[][] v, final ComplexValue[][] e, final boolean wantu, final boolean wantv) {
    int j, i, k, kk, ll, l = 0, lls;
    int iter, kase, lm1, lp1, ls = 0, m;
    int mm, mm1, mp1, nctp1, nrtp1;
    double b, c, el, emm1, scale, shift, sl, sm;
    double smm1, test, ztest;
    double[] t1 = new double[1];
    double[] f = new double[1];
    double[] g = new double[1];
    double[] sn = new double[1];
    double[] cs = new double[1];

    int n = x.length;
    int ncu = n;
    int p = x[0].length;

    ComplexValue[][] w = new ComplexValue[n][1];
    for (int ii = 0; ii < n; ii++) {
      w[ii][0] = new ComplexValue(0, 0);
    }
    ComplexValue t = new ComplexValue(0, 0);
    ComplexValue r = new ComplexValue(0, 0);
    ComplexValue one = new ComplexValue(1, 0);
    ComplexValue tmpc1 = new ComplexValue(0, 0);
    ComplexValue tmpc2 = new ComplexValue(0, 0);

    // set the maximum number of iterations.
    //int maxit = 30 * n;

    /*
     * reduce x to bidiagonal form, storing the diagonal elements in s and the super-diagonal elements in e.
     */
    int info = 0;
    int nct = Math.min(n - 1, p);
    int nrt = Math.max(0, Math.min(p - 2, n));
    int lu = Math.max(nct, nrt);
    if (lu >= 1) {
      for (l = 1; l <= lu; l++) {
        lp1 = l + 1;
        if (l <= nct) {
          /*
           * compute the transformation for the l-th column and place the l-th diagonal in SS(l).
           */
          // ComplexValueSetValue(SS(l), dznrm2(n-l+1, XX(l,l), 1, xn), 0.0);
          ComplexValue.setValue(s[l - 1][0], dznrm2(n - l + 1, x, l - 1, l - 1, 1), 0);

          if (ComplexValue.cabs1(s[l - 1][0]) != 0.0) {
            if (ComplexValue.cabs1(x[l - 1][l - 1]) != 0.0) {
              ComplexValue.copy(s[l - 1][0], ComplexValue.csign(tmpc1, s[l - 1][0], x[l - 1][l - 1]));
            }

            ComplexValue.inverse(tmpc1, s[l - 1][0]);
            zscal(n - l + 1, tmpc1, x, l - 1, l - 1);
            ComplexValue.addSelf(x[l - 1][l - 1], one);
          }

          ComplexValue.negateSelf(s[l - 1][0]);
        }

        if (p >= lp1) {
          for (j = lp1; j <= p; j++) {
            if (l <= nct && ComplexValue.cabs1(s[l - 1][0]) != 0.0) {
              //apply the transformation.
              // t = -zdotc(n-l+1,XX(l,l),1,XX(l,j),1)/XX(l,l);
              zdotc(tmpc2, n - l + 1, x, l - 1, l - 1, x, l - 1, j - 1);
              ComplexValue.divide(tmpc1, tmpc2, x[l - 1][l - 1]);
              ComplexValue.negate(t, tmpc1);
              zaxpy(n - l + 1, t, x, l - 1, l - 1, x, l - 1, j - 1);
            }
            /*
             * place the l-th row of x into e for the subsequent calculation of the row transformation.
             */
            ComplexValue.conjugate(e[j - 1][0], x[l - 1][j - 1]);
          }
        }
        if (wantu && l <= nct) {
          // place the transformation in u for subsequent back multiplication.
          for (i = l; i <= n; i++) {
            ComplexValue.copy(u[i - 1][l - 1], x[i - 1][l - 1]);
          }
        }

        if (l <= nrt) {
          /*
           * compute the l-th row transformation and place the l-th super-diagonal in EE(l).
           */
          ComplexValue.setValue(e[l - 1][0], dznrm2(p - l, e, lp1 - 1, 0, 1), 0.0);

          if (ComplexValue.cabs1(e[l - 1][0]) != 0.0) {
            if (ComplexValue.cabs1(e[lp1 - 1][0]) != 0.0) {
              ComplexValue.copy(e[l - 1][0], ComplexValue.csign(tmpc1, e[l - 1][0], e[lp1 - 1][0]));
            }

            ComplexValue.inverse(tmpc1, e[l - 1][0]);
            zscal(p - l, tmpc1, e, lp1 - 1, 0);
            ComplexValue.addSelf(e[lp1 - 1][0], one);
          }

          ComplexValue.negate(e[l - 1][0], ComplexValue.conjugate(tmpc1, e[l - 1][0]));
          if (lp1 <= n && ComplexValue.cabs1(e[l - 1][0]) != 0.0) {
            // apply the transformation.
            for (i = lp1; i <= n; i++) {
              ComplexValue.setZero(w[i - 1][0]);
            }
            for (j = lp1; j <= p; j++) {
              zaxpy(n - l, e[j - 1][0], x, lp1 - 1, j - 1, w, lp1 - 1, 0);
            }
            for (j = lp1; j <= p; j++) {
              // zaxpy(n-l,dconjg(-EE(j)/EE(lp1)),WW(lp1),1, XX(lp1,j),1);
              ComplexValue.divide(tmpc1, e[j - 1][0], e[lp1 - 1][0]);
              ComplexValue.negateSelf(tmpc1);
              ComplexValue.conjugateSelf(tmpc1);
              zaxpy(n - l, tmpc1, w, lp1 - 1, 0, x, lp1 - 1, j - 1);
            }
          }
          if (wantv) {
            // place the transformation in v for subsequent back multiplication.
            for (i = lp1; i <= p; i++) {
              ComplexValue.copy(v[i - 1][l - 1], e[i - 1][0]);
            }
          }
        }
      }
    }
    /*
     * set up the final bidiagonal matrix or order m.
     */
    m = Math.min(p, n + 1);
    nctp1 = nct + 1;
    nrtp1 = nrt + 1;
    if (nct < p) {
      ComplexValue.copy(s[nctp1 - 1][0], x[nctp1 - 1][nctp1 - 1]);
    }
    if (n < m) {
      ComplexValue.setZero(s[m - 1][0]);
    }
    if (nrtp1 < m) {
      ComplexValue.copy(e[nrtp1 - 1][0], x[nrtp1 - 1][m - 1]);
    }
    ComplexValue.setZero(e[m - 1][0]);
    /*
     * if required, generate u.
     */
    if (wantu) {
      if (ncu >= nctp1) {
        for (j = nctp1; j <= ncu; j++) {
          for (i = 1; i <= n; i++) {
            ComplexValue.setZero(u[i - 1][j - 1]);
          }
          ComplexValue.setUnit(u[j - 1][j - 1]);
        }
      }
      if (nct >= 1) {
        for (ll = 1; ll <= nct; ll++) {
          l = nct - ll + 1;
          if (ComplexValue.cabs1(s[l - 1][0]) != 0.0) {
            lp1 = l + 1;
            if (ncu >= lp1) {
              for (j = lp1; j <= ncu; j++) {
                /*
                 * t = -zdotc(n-l+1,UU(l,l),1,UU(l,j),1)/UU(l,l);
                 */
                zdotc(tmpc2, n - l + 1, u, l - 1, l - 1, u, l - 1, j - 1);
                ComplexValue.divide(tmpc1, tmpc2, u[l - 1][l - 1]);
                ComplexValue.negate(t, tmpc1);
                zaxpy(n - l + 1, t, u, l - 1, l - 1, u, l - 1, j - 1);
              }
            }
            zscal(n - l + 1, ComplexValue.negate(tmpc1, one), u, l - 1, l - 1);
            ComplexValue.addSelf(u[l - 1][l - 1], one);
            lm1 = l - 1;
            if (lm1 >= 1) {
              for (i = 1; i <= lm1; i++) {
                ComplexValue.setZero(u[i - 1][l - 1]);
              }
            }
            continue;
          }

          for (i = 1; i <= n; i++) {
            ComplexValue.setZero(u[i - 1][l - 1]);
          }
          ComplexValue.setUnit(u[l - 1][l - 1]);
        }
      }
    }
    /*
     * if it is required, generate v.
     */
    if (wantv) {
      for (ll = 1; ll <= p; ll++) {
        l = p - ll + 1;
        lp1 = l + 1;
        if (l <= nrt && ComplexValue.cabs1(e[l - 1][0]) != 0.0) {
          for (j = lp1; j <= p; j++) {
            /*
             * t = -zdotc(p-l,VV(lp1,l),1,VV(lp1,j),1)/VV(lp1,l);
             */
            zdotc(tmpc2, p - l, v, lp1 - 1, l - 1, v, lp1 - 1, j - 1);
            ComplexValue.divide(tmpc1, tmpc2, v[lp1 - 1][l - 1]);
            ComplexValue.negate(t, tmpc1);
            zaxpy(p - l, t, v, lp1 - 1, l - 1, v, lp1 - 1, j - 1);
          }
        }
        for (i = 1; i <= p; i++) {
          ComplexValue.setZero(v[i - 1][l - 1]);
        }
        ComplexValue.setUnit(v[l - 1][l - 1]);
      }
    }
    /*
     * transform s and e so that they are double precision.
     */
    for (i = 1; i <= m; i++) {
      if (ComplexValue.cabs1(s[i - 1][0]) != 0.0) {
        t.re = ComplexValue.cdabs(s[i - 1][0]);
        t.im = 0;
        ComplexValue.divide(r, s[i - 1][0], t);
        ComplexValue.copy(s[i - 1][0], t);
        if (i < m) { 
          ComplexValue.divideSelf(e[i - 1][0], r);
        }
        if (wantu) {
          zscal(n, r, u, 0, i - 1);
        }
      }
      if (i == m) {
        break;
      }
      if (ComplexValue.cabs1(e[i - 1][0]) != 0.0) {
        t.re = ComplexValue.cdabs(e[i - 1][0]);
        t.im = 0.0;

        ComplexValue.divide(r, t, e[i - 1][0]);
        ComplexValue.copy(e[i - 1][0], t);
        ComplexValue.multiplylSelf(s[i][0], r);
        if (wantv) {
          zscal(p, r, v, 0, i);
        }
      }
    }

    /*
     * main iteration loop for the singular values.
     */
    mm = m;
    iter = 0;
    // L400:
    do {
      /*
       * quit if all the singular values have been found.
       */
      if (m == 0) {
        w = null;
        return info;
      }
      /*
       * if too many iterations have been performed, set flag and return.
       */
      if (iter == this.maxIteration) {
        info = m;
        w = null;
        return info;
      }
      /*
       * this section of the program inspects for negligible elements in the s
       * and e arrays. on completion the variables kase and l are set as
       * follows.
       * 
       * kase = 1 if SS(m) and EE(l-1) are negligible and l <m kase = 2 if SS(l)
       * is negligible and l <m kase = 3 if EE(l-1) is negligible, l <m, and
       * SS(l), ..., SS(m) are not negligible (qr step). kase = 4 if EE(m-1) is
       * negligible (convergence).
       */
      for (ll = 1; ll <= m; ll++) {
        l = m - ll;
        if (l == 0) {
          break;
        }
        test = ComplexValue.cdabs(s[l - 1][0]) + ComplexValue.cdabs(s[l][0]);
        ztest = test + ComplexValue.cdabs(e[l - 1][0]);
        if (Double.doubleToLongBits(ztest) == Double.doubleToLongBits(test)) {
          ComplexValue.setZero(e[l - 1][0]);
          break;
        }
      }
      if (l == m - 1) {
        kase = 4;
      } else {
        lp1 = l + 1;
        mp1 = m + 1;
        for (lls = lp1; lls <= mp1; lls++) {
          ls = m - lls + lp1;
          if (ls == l) {
            break;
          }
          test = 0.0;
          if (ls != m) {
            test += ComplexValue.cdabs(e[ls - 1][0]);
          }
          if (ls != l + 1) {
            test += ComplexValue.cdabs(e[ls - 2][0]);
          }
          ztest = test + ComplexValue.cdabs(s[ls - 1][0]);
          if (Double.doubleToLongBits(ztest) == Double.doubleToLongBits(test)) {
            ComplexValue.setZero(s[ls - 1][0]);
            break;
          }
        }
        if (ls == l) {
          kase = 3;
        } else if (ls == m) {
          kase = 1;
        } else {
          kase = 2;
          l = ls;
        }
      }

      l++;
      /*
       * perform the task indicated by kase.
       */
      switch (kase) {
        case 1:
          /*
           * deflate negligible SS(m).
           */
          mm1 = m - 1;
          // f = Re(EE(m-1));
          f[0] = e[m - 2][0].re;
          ComplexValue.setZero(e[m - 2][0]);
          for (kk = l; kk <= mm1; kk++) {
            k = mm1 - kk + l;
            t1[0] = s[k - 1][0].re;
            drotg(t1, f, cs, sn);
            ComplexValue.setValue(s[k - 1][0], t1[0], 0.0);
            if (k != l) {
              f[0] = -sn[0] * e[k - 2][0].re;
              ComplexValue.multiplySelf(e[k - 2][0], cs[0]);
            }
            if (wantv) {
              zdrot(p, v, 0, k - 1, v, 0, m - 1, cs[0], sn[0]);
            }
          }
          break;
        case 2:
          /*
           * split at negligible SS(l).
           */
          f[0] = e[l - 2][0].re;
          ComplexValue.setZero(e[l - 2][0]);
          for (k = l; k <= m; k++) {
            t1[0] = s[k - 1][0].re;
            drotg(t1, f, cs, sn);
            ComplexValue.setValue(s[k - 1][0], t1[0], 0.0);
            f[0] = -sn[0] * e[k - 1][0].re;
            ComplexValue.multiplySelf(e[k - 1][0], cs[0]);
            if (wantu) {
              zdrot(n, u, 0, k - 1, u, 0, l - 2, cs[0], sn[0]);
            }
          }
          break;
        case 3:
          /*
           * perform one qr step.
           */
          /*
           * calculate the shift.
           */
          /*
           * scale = dmax1(cdabs(SS(m)),cdabs(SS(m-1)),cdabs(EE(m-1)), cdabs(SS(l)),cdabs(EE(l)));
           */
          scale = Math.max(ComplexValue.cdabs(s[m - 1][0]), ComplexValue.cdabs(s[m - 2][0]));
          scale = Math.max(scale, ComplexValue.cdabs(e[m - 2][0]));
          scale = Math.max(scale, ComplexValue.cdabs(s[l - 1][0]));
          scale = Math.max(scale, ComplexValue.cdabs(e[l - 1][0]));

          sm = s[m - 1][0].re / scale;
          smm1 = s[m - 2][0].re / scale;
          emm1 = e[m - 2][0].re / scale;
          sl = s[l - 1][0].re / scale;
          el = e[l - 1][0].re / scale;
          b = ((smm1 + sm) * (smm1 - sm) + emm1 * emm1) / 2.0;
          c = (sm * emm1) * (sm * emm1);
          shift = 0.0;
          if (b != 0.0 || c != 0.0) {
            shift = Math.sqrt(b * b + c);
            if (b < 0.0) {
              shift = -shift;
            }
            shift = c / (b + shift);
          }
          f[0] = (sl + sm) * (sl - sm) + shift;
          g[0] = sl * el;
          /*
           * chase zeros.
           */
          mm1 = m - 1;
          for (k = l; k <= mm1; k++) {
            drotg(f, g, cs, sn);
            if (k != l) {
              ComplexValue.setValue(e[k - 2][0], f[0], 0.0);
            }
            f[0] = cs[0] * s[k - 1][0].re + sn[0] * e[k - 1][0].re;

            /* EE(k) = cs*EE(k) - sn*SS(k); */
            ComplexValue.multiply(tmpc1, cs[0], e[k - 1][0]);
            ComplexValue.multiply(tmpc2, -sn[0], s[k - 1][0]);
            ComplexValue.add(e[k - 1][0], tmpc1, tmpc2);

            g[0] = sn[0] * s[k][0].re;
            ComplexValue.multiplySelf(s[k][0], cs[0]);
            if (wantv) {
              zdrot(p, v, 0, k - 1, v, 0, k, cs[0], sn[0]);
            }
            drotg(f, g, cs, sn);
            ComplexValue.setValue(s[k - 1][0], f[0], 0.0);
            f[0] = cs[0] * e[k - 1][0].re + sn[0] * s[k][0].re;

            /* SS(k+1) = -sn*EE(k) + cs*SS(k+1); */
            ComplexValue.multiply(tmpc1, -sn[0], e[k - 1][0]);
            ComplexValue.multiply(tmpc2, cs[0], s[k][0]);
            ComplexValue.add(s[k][0], tmpc1, tmpc2);

            g[0] = sn[0] * e[k][0].re;
            ComplexValue.multiplySelf(e[k][0], cs[0]);
            if (wantu && k < n) {
              zdrot(n, u, 0, k - 1, u, 0, k, cs[0], sn[0]);
            }
          }
          ComplexValue.setValue(e[m - 2][0], f[0], 0.0);
          iter++;
          break;
        case 4:
          /*
           * convergence.
           */
          /*
           * make the singular value positive
           */
          if (s[l - 1][0].re < 0.0) {
            ComplexValue.negateSelf(s[l - 1][0]);
            if (wantv) {
              zscal(p, ComplexValue.negate(tmpc1, one), v, 0, l - 1);
            }
          }
          /*
           * order the singular value.
           */
          while (l != mm) {
            if (s[l - 1][0].re > s[l][0].re) {
              break;
            }
            ComplexValue.swap(s[l - 1][0], s[l][0]);
            if (wantv && l < p) {
              zswap(p, v, 0, l - 1, v, 0, l);
            }
            if (wantu && l < n) {
              zswap(n, u, 0, l - 1, u, 0, l);
            }
            l++;
          }
          iter = 0;
          m--;
          break;
        default:
          throw new RuntimeException("Illegal kase"); //$NON-NLS-1$
      }
    } while (true);
    // goto L400;
  }

  /**
   * @param n ベクトルの成分の数
   * @param zx 行列1
   * @param zxrow 行列1の行番号
   * @param zxcol 行列1の列番号
   * @param zy 行列2
   * @param zyrow 行列2の行番号
   * @param zycol 行列2の列番号
   * @param c 回転角度のcosine
   * @param s 回転角度のsine
   */
  private void zdrot(final int n, final ComplexValue[][] zx, final int zxrow, final int zxcol, final ComplexValue[][] zy, final int zyrow, final int zycol, 
      final double c, final double s) {
    ComplexValue ztemp = new ComplexValue(0, 0);
    ComplexValue tmpc1 = new ComplexValue(0, 0);
    ComplexValue tmpc2 = new ComplexValue(0, 0);

    for (int i = 1; i <= n; i++) {
      /* ztemp = c*ZX(i) + s*ZY(i); */
      ComplexValue.multiply(tmpc1, c, zx[zxrow + i - 1][zxcol]);
      ComplexValue.multiply(tmpc2, s, zy[zyrow + i - 1][zycol]);
      ComplexValue.add(ztemp, tmpc1, tmpc2);

      /* ZY(i) = c*ZY(i) - s*ZX(i); */
      ComplexValue.multiply(tmpc1, c, zy[zyrow + i - 1][zycol]);
      ComplexValue.multiply(tmpc2, -s, zx[zxrow + i - 1][zxcol]);
      ComplexValue.add(zy[zyrow + i - 1][zycol], tmpc1, tmpc2);
      
      ComplexValue.copy(zx[zxrow + i - 1][zxcol], ztemp);
    }
  }

  /**
   * @param da ベクトル1
   * @param db ベクトル2
   * @param c 回転角度のcosine
   * @param s 回転角度のsine
   */
  private void drotg(final double[] da, final double[] db, final double[] c, final double[] s) {
    double r;

    double roe = db[0];
    if (Math.abs(da[0]) > Math.abs(db[0])) {
      roe = da[0];
    }

    double scale = Math.abs(da[0]) + Math.abs(db[0]);
    if (scale == 0.0) {
      c[0] = 1.0;
      s[0] = 0.0;
      r = 0.0;
    } else {
      r = scale * Math.sqrt((da[0] / scale) * (da[0] / scale) + (db[0] / scale) * (db[0] / scale));
      r = dsign(1.0, roe) * r;
      c[0] = da[0] / r;
      s[0] = db[0] / r;
    }

    double z = 1.0;
    if (Math.abs(da[0]) > Math.abs(db[0])) {
      z = s[0];
    }
    if (Math.abs(db[0]) >= Math.abs(da[0]) && c[0] != 0.0) {
      z = 1.0 / c[0];
    }

    da[0] = r;
    db[0] = z;
  }

  /**
   * @param n ベクトルの成分の数
   * @param zx 行列
   * @param zxrow 行列の行番号
   * @param zxcol 行列の列番号
   * @param incx 行列の番号増分
   * @return 2ノルム
   */
  private double dznrm2(final int n, final ComplexValue[][] zx, final int zxrow, final int zxcol, final int incx) {
    boolean imag = false;
    boolean scale = false;
    int i, next, nn;
    double cutlo = 0;
    double cuthi = 0;
    double hitest = 0;
    double xmax = 0;
    double absx = 0;
    double sum, norm;

    int gotoflag;
    if (n <= 0) {
      norm = 0.0;
      return norm;
    }
    next = 30;
    sum = 0.0;
    nn = n * incx;

    /*
     * begin main loop
     */
    for (i = 1; i <= nn; i += incx) {
      gotoflag = 0;
      do {
        if (gotoflag < 30) {
          absx = Math.abs(zx[i - 1 + zxrow][zxcol].re);
          imag = false;

          switch (next) {
            case 30:
              gotoflag = 30;
              break; // goto L30;
            case 50:
              gotoflag = 50;
              break; // goto L50;
            case 107:
              gotoflag = 107;
              break; // goto L107;
            case 190:
              gotoflag = 190;
              break; // goto L190;
            case 110:
              gotoflag = 110;
              break; // goto L110;
            default:
              gotoflag = 0;
          }
        }

        // L30:
        if (gotoflag < 50) {
          if (absx > cutlo) {
            gotoflag = 185; // goto L185;
          } else {
            next = 50;
            scale = false;
            /*
             * phase 1. sum is 0.0
             */
            gotoflag = 0;
          }
        }

        // L50:
        if (gotoflag < 100) {
          if (absx == 0.0) {
            gotoflag = 200; // goto L200;
          } else if (absx > cutlo) {
            gotoflag = 185; // goto L185;
          } else {
            /*
             * prepare for phase 2.
             */
            next = 107;
            gotoflag = 105; // goto L105;
            /*
             * prepare for phase 4.
             */
          }
        }
        // L100:
        if (gotoflag < 105) {
          next = 110;
          sum = (sum / absx) / absx;
          gotoflag = 0;
        }
        // L105:
        if (gotoflag < 107) {
          scale = true;
          xmax = absx;
          gotoflag = 115; // goto L115;
        }
        /*
         * phase 2. sum is small. scale to avoid destructive underflow.
         */
        // L107:
        if (gotoflag < 110) {
          if (absx > cutlo) {
            gotoflag = 175; // goto L175;
          } else {
            gotoflag = 0;
          }
        }
        /*
         * common code for phases 2 and 4. in phase 4 sum is large. scale to avoid overflow.
         */
        // L110:
        if (gotoflag < 115) {
          if (absx <= xmax) {
            gotoflag = 115; // goto L115;
          } else {
            sum = 1.0 + sum * (xmax / absx) * (xmax / absx);
            xmax = absx;
            gotoflag = 200; // goto L200;
          }
        }
        // L115:
        if (gotoflag < 175) {
          sum = sum + (absx / xmax) * (absx / xmax);
          gotoflag = 200; // goto L200;
        }
        /*
         * prepare for phase 3.
         */
        // L175:
        if (gotoflag < 185) {
          sum = (sum * xmax) * xmax;
          gotoflag = 0;
        }
        // L185:
        if (gotoflag < 190) {
          next = 190;
          scale = false;
          /*
           * for real or d.p. set hitest = cuthi/n for complex set hitest = cuthi/(2*n)
           */
          hitest = cuthi / n;
          /*
           * phase 3. sum is mid-range. no scaling.
           */
          gotoflag = 0;
        }
        // L190:
        if (gotoflag < 200) {
          if (absx >= hitest) {
            gotoflag = 100; // goto L100;
            continue; // to do loop
          }

          sum += absx * absx;
          gotoflag = 0;
        }
        // L200:
        if (gotoflag < 201) {
          /*
           * control selection of real and imaginary parts.
           */
          if (imag) {
            break; // continue; //goto for loop
          }
          absx = Math.abs(zx[i - 1 + zxrow][zxcol].im);
          imag = true;

          switch (next) {
            case 50:
              gotoflag = 50;
              break; // goto L50;
            case 107:
              gotoflag = 107;
              break; // goto L107;
            case 190:
              gotoflag = 190;
              break; // goto L190;
            case 110:
              gotoflag = 110;
              break; // goto L110;
            default:
              gotoflag = 0;
          }
        }

        if (gotoflag == 0) {
          break; // to for loop
        }

        continue;
      } while (true);
    }
    /*
     * end of main loop. compute square root and adjust for scaling.
     */
    norm = Math.sqrt(sum);
    if (scale) {
      norm *= xmax;
    }
    return norm;
  }

  /**
   * @param n ベクトルの成分数
   * @param za スカラー
   * @param zx 行列1
   * @param zxrow 行列1の行番号
   * @param zxcol 行列1の列番号
   * @param zy 行列2
   * @param zyrow 行列2の行番号
   * @param zycol 行列2の列番号
   */
  private void zaxpy(final int n, final ComplexValue za, final ComplexValue[][] zx, final int zxrow, final int zxcol, final ComplexValue[][] zy, final int zyrow, final int zycol
      ) {
    
    if (ComplexValue.cabs1(za) == 0.0) {
      return;
    }

    ComplexValue tmpc = new ComplexValue(0.0, 0.0);

    for (int i = 1; i <= n; i++) {
      /* ZY(i) = ZY(i) + za*ZX(i); */
      ComplexValue.addSelf(zy[i - 1 + zyrow][zycol], ComplexValue.multiply(tmpc, za, zx[i - 1 + zxrow][zxcol]));
    }
  }

  /**
   * @param dotc ベクトルの内積
   * @param n ベクトルの成分の数
   * @param zx 行列1
   * @param zxrow 行列1の行番号
   * @param zxcol 行列1の列番号
   * @param zy 行列2
   * @param zyrow 行列2の行番号
   * @param zycol 行列2の列番号
   */
  private void zdotc(final ComplexValue dotc, final int n, final ComplexValue[][] zx, final int zxrow, final int zxcol, final ComplexValue[][] zy, final int zyrow, final int zycol) {
    ComplexValue tmp = new ComplexValue(0, 0);
    ComplexValue.setValue(dotc, 0.0, 0.0);

    for (int i = 1; i <= n; i++) {
      /* ztemp = ztemp + dconjg(ZX(i))*ZY(i); */
      ComplexValue.addSelf(dotc, ComplexValue.multiplylSelf(ComplexValue.conjugate(tmp, zx[i - 1 + zxrow][zxcol]), zy[i - 1 + zyrow][zycol]));
    }
  }

  /**
   * @param n ベクトルの成分の数
   * @param za スカラー
   * @param zx 対象となる行列
   * @param zxrow 行列の行番号
   * @param zxcol 行列の列番号
   */
  private void zscal(final int n, final ComplexValue za, final ComplexValue[][] zx, final int zxrow, final int zxcol) {
    for (int i = 1; i <= n; i++) {
      ComplexValue.multiplylSelf(zx[i - 1 + zxrow][zxcol], za);
    }
  }

  /**
   * 2個のベクトルの成分を交換します。
   * 
   * @param n 交換する成分の数
   * @param x 行列1
   * @param zxrow 行列1の行番号
   * @param zxcol 行列1の列番号
   * @param y 行列2
   * @param zyrow 行列2の行番号
   * @param zycol 行列2の列番号
   */
  private void zswap(final int n, final ComplexValue[][] x, final int zxrow, final int zxcol, final ComplexValue[][] y, final int zyrow, final int zycol) {
    for (int i = 1; i <= n; i++) {
      ComplexValue.swap(x[i - 1 + zxrow][zxcol], y[i - 1 + zyrow][zycol]);
    }
  }

  /**
   * bが正なら、aの絶対値、bが負ならaの絶対値の符号を負にした数を求めます。
   * 
   * @param a 絶対値を決める数
   * @param b 符号を決める数
   * @return 計算結果
   */
  private double dsign(final double a, final double b) {
    return ((b) >= 0 ? Math.abs(a) : -Math.abs(a));
  }
  
  /**
   * 最大反復数を設定します。
   * @param maxIteration 最大反復数
   */
  public void setMaxIteration(final int maxIteration) {
    this.maxIteration = maxIteration;
  }
}