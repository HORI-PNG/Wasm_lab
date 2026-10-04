/*
 * $Id: ComplexSingularValueDecomposition.java,v 1.4 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.svd;

import org.mklab.nfc.matrix.BaseMatrixUtil;
import org.mklab.nfc.matrix.AbstractNumericalMatrixUtil;
import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.GridUtil;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * 複素行列の特異値分解を行うためのクラスです。
 * 
 * @param <RS> 実スカラーの型
 * @param <RM> 実行列の型
 * @param <CS> 複素スカラーの型 
 * @param <CM> 複素行列の型
 */
public final class ComplexSingularValueDecomposer<RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> {
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
   * @param a 複素行列
   * @return {U, D V}
   */
  public SingularValueDecompositionElements<CS,CM> decompose(final CS[][] a) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a.length;
    final int size = Math.min(rowSize, columnSize);

    final CS[] values = GridUtil.createZero(a[0], size);
    final CS[][] leftVector = GridUtil.createZero(a, rowSize, rowSize);
    final CS[][] rightVector = GridUtil.createZero(a, columnSize, columnSize);

    decompose(a, values, leftVector, rightVector);

    final CS[][] diagonalValues = GridUtil.vectorToDiagonal(values);
    final CS[][] valueMatrix = GridUtil.createZero(a, rowSize, columnSize);
    GridUtil.setSubMatrix(valueMatrix, 0, size - 1, 0, size - 1, diagonalValues);

    return new SingularValueDecompositionElements<>(leftVector, valueMatrix, rightVector);
  }

  /**
   * 複素行列の特異値を返します。
   * 
   * @param a 複素行列
   * @return 特異値
   */
  public RS[] singularValue(final CS[][] a) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;
    final int size = Math.min(rowSize, columnSize);

    final CS[] val = GridUtil.createZero(a[0], size);
    final CS[][] lvec = GridUtil.createZero(a, rowSize, rowSize);
    final CS[][] rvec = GridUtil.createZero(a, columnSize, columnSize);

    decompose(a, val, lvec, rvec);

    final RS[] singularValues = a[0][0].getRealPart().createArray(size);
    for (int i = 0; i < size; i++) {
      singularValues[i] = val[i].getRealPart();
    }

    return singularValues;
  }

  /**
   * 複素行列の最大特異値を返します。
   * 
   * @param a 複素行列
   * @return 最大特異値
   */
  public RS maximumSingularValue(final CS[][] a) {
    return singularValue(a)[0];
  }

  /**
   * 複素行列の最小特異値を返します。
   * 
   * @param a 複素行列の実部
   * @return 最小特異値
   */
  public RS minimumSingularValue(final CS[][] a) {
    final RS[] singularValues = singularValue(a);
    return singularValues[singularValues.length - 1];
  }

  /**
   * 複素行列が非正則であるか判定します。
   * 
   * @param a 複素行列
   * @param tolerance 許容誤差
   * @return 非正則ならばtrue、そうでなければfalse
   */
  public boolean isSingular(final CS[][] a, final CS tolerance) {
    final RS[] singularValues = singularValue(a);
    return singularValues[singularValues.length - 1].isLessThan(tolerance.getRealPart());
  }

  /**
   * 複素行列のランク(階数)を返します。
   * 
   * @param a 複素行列
   * @param tolerance 許容誤差
   * @return ランク
   */
  public int rank(final CS[][] a, final CS tolerance) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    CS[][] a2;
    if (rowSize > columnSize) {
      a2 = GridUtil.transpose(a);
    } else {
      a2 = a;
    }

    final RS[] singularValues = singularValue(a2);

    int rank = 0;
    for (int i = 0; i < singularValues.length; i++) {
      if (singularValues[i].isGreaterThan(tolerance.getRealPart())) {
        rank++;
      }
    }

    return rank;
  }

  /**
   * 複素行列がフルランクであるか判定します。
   * 
   * @param a 複素行列
   * @param tolerance 許容誤差
   * @return フルランクならばtrue、そうでなければfalse
   */
  public boolean isFullRank(final CS[][] a, final CS tolerance) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final int rank = rank(a, tolerance);

    if (rank == rowSize || rank == columnSize) {
      return true;
    }

    return false;
  }

  /**
   * 複素行列の擬似逆行列を返します。
   * 
   * @param a 複素行列
   * @param tolerance 許容誤差
   * @return 擬似逆行列
   */
  public CS[][] pseudoInverse(final CS[][] a, final CS tolerance) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final SingularValueDecompositionElements<CS,CM> svd = decompose(a);

    final CS[][] u = svd.getU();
    final RS[][] d = AbstractNumericalMatrixUtil.getRealPartElements(svd.getD());
    final CS[][] v = svd.getV();

    int rank = 0;
    for (int i = 0; i < Math.min(rowSize, columnSize); i++) {
      if (d[i][i].isGreaterThan(tolerance.getRealPart())) {
        rank++;
      }
    }

    final CS[][] ut = BaseMatrixUtil.conjugateTranspose(u);

    final CS[][] z = GridUtil.createZero(a, columnSize, rowSize);

    //final RS unit = d[0][0].createUnit();
    for (int i = 1; i <= rank; i++) {
      z[i - 1][i - 1] = a[0][0].create(d[i - 1][i - 1].inverse());
    }

    final CS[][] tmp = BaseMatrixUtil.<CS,CM> multiply(z, ut);
    return BaseMatrixUtil.<CS,CM> multiply(v, tmp);
  }

  /**
   * 線形方程式の解を返します。
   * 
   * <p>複素行列をA、複素行列をBとすとき、
   * 
   * <blockquote> A * X = B </blockquote>
   * 
   * の解すなわち
   * 
   * <blockquote> X = A<sup>-1</sup> * B </blockquote>
   * 
   * を返します。
   * 
   * @param a 係数行列
   * @param b 係数行列
   * @param tolerance 許容誤差
   * @return 線形方程式の解
   */
  public CS[][] leastSquare(final CS[][] a, final CS[][] b, final CS tolerance) {
    final CS[][] aInv = pseudoInverse(a, tolerance);
    return BaseMatrixUtil.<CS,CM> multiply(aInv, b);
  }

  /**
   * 複素行列のカーネル(零空間)を張るベクトルからなる行列を返します。
   * 
   * <p>許容誤差より小さい特異値をゼロと見なします。
   * 
   * @param a 複素行列
   * @param tolerance 許容誤差
   * @return カーネル
   */
  public CS[][] kernel(final CS[][] a, final CS tolerance) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    CS[][] a2;
    if (rowSize < columnSize) {
      a2 = GridUtil.<CS> appendDown(a, GridUtil.<CS> createZero(a, columnSize - rowSize, columnSize));
    } else {
      a2 = GridUtil.clone(a);
    }

    final SingularValueDecompositionElements<CS,CM> svd = decompose(a2);
    final RS[][] d = AbstractNumericalMatrixUtil.getRealPartElements(svd.getD());
    final CS[][] rightVec = svd.getV();

    int rank = 0;
    for (int i = 0; i < Math.min(rowSize, columnSize); i++) {
      if (d[i][i].isGreaterThan(tolerance.getRealPart())) {
        rank++;
      }
    }

    if (rank == Math.max(rowSize, columnSize)) {
      return GridUtil.createArray(rowSize, 0, a);
      //throw new IllegalArgumentException(Messages.getString("ComplexSingularValueDecomposition.0")); //$NON-NLS-1$
    }

    return GridUtil.getSubMatrix(rightVec, 0, columnSize - 1, rank, columnSize - 1);
  }

  /*
   * 2-norm of matrix
   */

  /**
   * 複素行列の最大特異値(2-ノルム)を返します。
   * 
   * @param a 複素行列
   * @return 最大特異値
   */
  public RS norm(final CS[][] a) {
    return maximumSingularValue(a);
  }

  /**
   * 複素行列の条件数を返します。
   * 
   * @param a 複素行列
   * @return 条件数
   */
  public RS conditionNumber(final CS[][] a) {
    if (a.length == 0 || a[0].length == 0) {
      throw new IllegalArgumentException(Messages.getString("ComplexSingularValueDecomposition.4")); //$NON-NLS-1$
      //return a[0][0].getRealPart().getNaN();
    }

    final RS[] singularValues = singularValue(a);

    // 特異行列
    if (GridUtil.anyZero(singularValues)) {
      return a[0][0].getRealPart().getInfinity();
    }

    return AbstractNumericalMatrixUtil.max(singularValues).divide(AbstractNumericalMatrixUtil.min(singularValues));
  }

  /*
   * Singular Value Decomposition of complex matrices
   * 
   * A = U * D * V#
   * 
   * U := lvec D := vec2diag(val) V := rvec
   * 
   * a: Matrix m x n val: Singular Value min(m,n) x 1 lvec: Left Singular Vector
   * m x m rvec; Right Singular Vector n x n
   */

  /**
   * @param a 複素行列
   * @param val 特異値
   * @param lvec 左特異ベクトル
   * @param rvec 右特異ベクトル
   */
  private void decompose(final CS[][] a, final CS[] val, final CS[][] lvec, final CS[][] rvec) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final int mnMin = Math.min(rowSize, columnSize);
    final int mnMax = Math.max(rowSize, columnSize);

    final CS[][] b;
    if (rowSize < columnSize) {
      b = BaseMatrixUtil.conjugateTranspose(a);
    } else {
      b = GridUtil.clone(a);
    }

    final CS[][] rvec2 = GridUtil.createArray(mnMin, mnMin, a);
    final CS[][] lvec2 = GridUtil.createArray(mnMax, mnMax, a);
    final CS[][] eeee = GridUtil.createArray(mnMin, 1, a);
    final CS[][] val2 = GridUtil.createArray(mnMin + 1, 1, a);

    final CS unit = a[0][0].createZero();

    // 初期化
    for (int i = 0; i < mnMin; i++) {
      eeee[i][0] = unit.createZero();
      val2[i][0] = unit.createZero();
      for (int j = 0; j < mnMin; j++) {
        rvec2[i][j] = unit.createZero();
        lvec2[i][j] = unit.createZero();
      }
      for (int j = mnMin; j < mnMax; j++) {
        lvec2[i][j] = unit.createZero();
      }
    }
    val2[mnMin][0] = unit.createZero();

    for (int i = mnMin; i < mnMax; i++) {
      for (int j = 0; j < mnMax; j++) {
        lvec2[i][j] = unit.createZero();
      }
    }

    final int ierr = zsvdc(b, lvec2, val2, rvec2, eeee, true, true);

    if (0 < ierr) {
      throw new RuntimeException(Messages.getString("ComplexSingularValueDecomposition.3")); //$NON-NLS-1$
    }

    if (rowSize < columnSize) {
      GridUtil.copy(lvec2, rvec);
      GridUtil.copy(rvec2, lvec);
    } else {
      GridUtil.copy(lvec2, lvec);
      GridUtil.copy(rvec2, rvec);
    }

    for (int i = 0; i < mnMin; i++) {
      val[i] =unit.create(val2[i][0].getRealPart());
    }

  }

  /**
   * 特異値分解を求めます。
   * 
   * @param x 対象となる行列
   * @param u 左特異ベクトル
   * @param s 特異値
   * @param v 右特異ベクトル
   * @param e 左特異ベクトルが必要ならばtrue
   * @param wantu 左特異ベクトルが必要ならばtrue
   * @param wantv 右特異ベクトルが必要ならばtrue
   * @return 分解の結果
   */
  private int zsvdc(final CS[][] x, final CS[][] u, final CS[][] s, final CS[][] v, final CS[][] e, final boolean wantu,
      final boolean wantv) {
    RS realUnit = x[0][0].getRealPart();
    CS complexUnit = x[0][0];

    int n, p;

    int j, i, k, kk, ll, l = 0, lls;
    int iter, kase, lm1, lp1, ls = 0, m;
    int mm, mm1, mp1, nctp1, ncu, nrtp1;
    RS b, c, el, emm1, scale, shift, sl, sm;
    RS smm1, test, ztest;
    RS[] t1 = realUnit.createArray(1);
    RS[] f = realUnit.createArray(1);
    RS[] g = realUnit.createArray(1);
    RS[] sn = realUnit.createArray(1);
    RS[] cs = realUnit.createArray(1);

    ncu = x.length;
    n = x.length;
    p = x[0].length;

    CS[][] w = x[0][0].createArray(n, 1);
    for (int ii = 0; ii < n; ii++) {
      w[ii][0] = x[0][0].createZero();
    }
    CS t = x[0][0].createZero();
    CS r = x[0][0].createZero();
    CS one = x[0][0].createUnit();
    CS tmpc1 = x[0][0].createZero();
    CS tmpc = x[0][0].createZero();

    /*
     * set the maximum number of iterations.
     */
    //int maxit = 30 * n;

    /*
     * reduce x to bidiagonal form, storing the diagonal elements in s and the
     * super-diagonal elements in e.
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
           * compute the transformation for the l-th column and place the l-th
           * diagonal in SS(l).
           */

          // ComplexValueSetValue(SS(l), dznrm2(n-l+1, XX(l,l), 1, xn), 0.0);
          s[l - 1][0] =  complexUnit.create(dznrm2(n - l + 1, x, l - 1, l - 1, 1));

          if (abs1(s[l - 1][0]).isZero() == false) {
            if (abs1(x[l - 1][l - 1]).isZero() == false) {
              s[l - 1][0] = sign(s[l - 1][0], x[l - 1][l - 1]);
            }

            tmpc1 = s[l - 1][0].inverse();
            zscal(n - l + 1, tmpc1, x, l - 1, l - 1);
            x[l - 1][l - 1] = x[l - 1][l - 1].add(one);
          }

          s[l - 1][0] = s[l - 1][0].unaryMinus();
        }

        if (p >= lp1) {
          for (j = lp1; j <= p; j++) {
            if (l <= nct && abs1(s[l - 1][0]).isZero() == false) {
              /*
               * apply the transformation.
               */
              /*
               * t = -zdotc(n-l+1,XX(l,l),1,XX(l,j),1)/XX(l,l);
               */
              tmpc = zdotc(n - l + 1, x, l - 1, l - 1, x, l - 1, j - 1);
              tmpc1 = tmpc.divide(x[l - 1][l - 1]);
              t = tmpc1.unaryMinus();
              zaxpy(n - l + 1, t, x, l - 1, l - 1, x, l - 1, j - 1);
            }
            /*
             * place the l-th row of x into e for the subsequent calculation of
             * the row transformation.
             */
            e[j - 1][0] = x[l - 1][j - 1].conjugate();
          }
        }
        if (wantu && l <= nct) {
          /*
           * place the transformation in u for subsequent back multiplication.
           */
          for (i = l; i <= n; i++) {
            u[i - 1][l - 1] = x[i - 1][l - 1].clone();
          }
        }

        if (l <= nrt) {
          /*
           * compute the l-th row transformation and place the l-th
           * super-diagonal in EE(l).
           */
          e[l - 1][0] = complexUnit.create(dznrm2(p - l, e, lp1 - 1, 0, 1));

          if (abs1(e[l - 1][0]).isZero() == false) {
            if (abs1(e[lp1 - 1][0]).isZero() == false) {
              e[l - 1][0] = sign(e[l - 1][0], e[lp1 - 1][0]);
            }

            tmpc1 = e[l - 1][0].inverse();
            zscal(p - l, tmpc1, e, lp1 - 1, 0);
            e[lp1 - 1][0] = e[lp1 - 1][0].add(one);
          }

          e[l - 1][0] = e[l - 1][0].conjugate().unaryMinus();

          if (lp1 <= n && abs1(e[l - 1][0]).isZero() == false) {
            /*
             * apply the transformation.
             */
            for (i = lp1; i <= n; i++) {
              w[i - 1][0] = x[0][0].createZero();
            }
            for (j = lp1; j <= p; j++) {
              zaxpy(n - l, e[j - 1][0], x, lp1 - 1, j - 1, w, lp1 - 1, 0);
            }
            for (j = lp1; j <= p; j++) {
              /*
               * zaxpy(n-l,dconjg(-EE(j)/EE(lp1)),WW(lp1),1, XX(lp1,j),1);
               */
              tmpc1 = e[j - 1][0].divide(e[lp1 - 1][0]).unaryMinus().conjugate();
              zaxpy(n - l, tmpc1, w, lp1 - 1, 0, x, lp1 - 1, j - 1);
            }
          }
          if (wantv) {
            /*
             * place the transformation in v for subsequent back multiplication.
             */
            for (i = lp1; i <= p; i++) {
              v[i - 1][l - 1] = e[i - 1][0].clone();
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
      s[nctp1 - 1][0] = x[nctp1 - 1][nctp1 - 1].clone();
    }
    if (n < m) {
      s[m - 1][0] = x[0][0].createZero();
    }
    if (nrtp1 < m) {
      e[nrtp1 - 1][0] = x[nrtp1 - 1][m - 1].clone();
    }
    e[m - 1][0] = x[0][0].createZero();

    /*
     * if required, generate u.
     */
    if (wantu) {
      if (ncu >= nctp1) {
        for (j = nctp1; j <= ncu; j++) {
          for (i = 1; i <= n; i++) {
            u[i - 1][j - 1] = x[0][0].createZero();
          }
          u[j - 1][j - 1] = x[0][0].createUnit();
        }
      }
      if (nct >= 1) {
        for (ll = 1; ll <= nct; ll++) {
          l = nct - ll + 1;
          if (abs1(s[l - 1][0]).isZero() == false) {
            lp1 = l + 1;
            if (ncu >= lp1) {
              for (j = lp1; j <= ncu; j++) {
                /*
                 * t = -zdotc(n-l+1,UU(l,l),1,UU(l,j),1)/UU(l,l);
                 */
                tmpc = zdotc(n - l + 1, u, l - 1, l - 1, u, l - 1, j - 1);
                tmpc1 = tmpc.divide(u[l - 1][l - 1]);
                t = tmpc1.unaryMinus();
                zaxpy(n - l + 1, t, u, l - 1, l - 1, u, l - 1, j - 1);
              }
            }
            zscal(n - l + 1, one.unaryMinus(), u, l - 1, l - 1);
            u[l - 1][l - 1] = u[l - 1][l - 1].add(one);
            lm1 = l - 1;
            if (lm1 >= 1) {
              for (i = 1; i <= lm1; i++) {
                u[i - 1][l - 1] = x[0][0].createZero();
              }
            }
            continue;
          }

          for (i = 1; i <= n; i++) {
            u[i - 1][l - 1] = x[0][0].createZero();
          }
          u[l - 1][l - 1] = x[0][0].createUnit();
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
        if (l <= nrt && abs1(e[l - 1][0]).isZero() == false) {
          for (j = lp1; j <= p; j++) {
            /*
             * t = -zdotc(p-l,VV(lp1,l),1,VV(lp1,j),1)/VV(lp1,l);
             */
            tmpc = zdotc(p - l, v, lp1 - 1, l - 1, v, lp1 - 1, j - 1);
            tmpc1 = tmpc.divide(v[lp1 - 1][l - 1]);
            t = tmpc1.unaryMinus();
            zaxpy(p - l, t, v, lp1 - 1, l - 1, v, lp1 - 1, j - 1);
          }
        }
        for (i = 1; i <= p; i++) {
          v[i - 1][l - 1] = x[0][0].createZero();
        }
        v[l - 1][l - 1] = x[0][0].createUnit();
      }
    }
    /*
     * transform s and e so that they are double precision.
     */
    for (i = 1; i <= m; i++) {
      if (abs1(s[i - 1][0]).isZero() == false) {
        t = s[i - 1][0].abs();

        r = s[i - 1][0].divide(t);
        s[i - 1][0] = t.clone();
        if (i < m) {
          e[i - 1][0] = e[i - 1][0].divide(r);
        }
        if (wantu) {
          zscal(n, r, u, 0, i - 1);
        }
      }
      if (i == m) {
        break;
      }
      if (abs1(e[i - 1][0]).isZero() == false) {
        t = e[i - 1][0].abs();
        r = t.divide(e[i - 1][0]);
        e[i - 1][0] = t.clone();
        s[i][0] = s[i][0].multiply(r);

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
        test = s[l - 1][0].abs().getRealPart().add(s[l][0].abs().getRealPart());
        ztest = test.add(e[l - 1][0].abs().getRealPart());
        if (ztest.equals(test)) {
          e[l - 1][0] = x[0][0].createZero();
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
          test = realUnit.createZero();
          if (ls != m) {
            test = test.add(e[ls - 1][0].abs().getRealPart());
          }
          if (ls != l + 1) {
            test = test.add(e[ls - 2][0].abs().getRealPart());
          }
          ztest = test.add(s[ls - 1][0].abs().getRealPart());
          if (ztest.equals(test)) {
            s[ls - 1][0] = x[0][0].createZero();
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
          f[0] = e[m - 2][0].getRealPart();
          e[m - 2][0] = x[0][0].createZero();

          for (kk = l; kk <= mm1; kk++) {
            k = mm1 - kk + l;
            t1[0] = s[k - 1][0].getRealPart();
            drotg(t1, f, cs, sn);

            s[k - 1][0] = complexUnit.create(t1[0]);

            if (k != l) {
              f[0] = sn[0].unaryMinus().multiply(e[k - 2][0].getRealPart());
              e[k - 2][0] = e[k - 2][0].multiply(complexUnit.create(cs[0]));
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
          f[0] = e[l - 2][0].getRealPart();
          e[l - 2][0] = x[0][0].createZero();
          for (k = l; k <= m; k++) {
            t1[0] = s[k - 1][0].getRealPart();
            drotg(t1, f, cs, sn);
            s[k - 1][0] = complexUnit.create(t1[0]);

            f[0] = sn[0].unaryMinus().multiply(e[k - 1][0].getRealPart());
            e[k - 1][0] = e[k - 1][0].multiply(complexUnit.create(cs[0]));

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
           * scale = dmax1(cdabs(SS(m)),cdabs(SS(m-1)),cdabs(EE(m-1)),
           * cdabs(SS(l)),cdabs(EE(l)));
           */
          scale = s[m - 1][0].abs().getRealPart().max(s[m - 2][0].abs().getRealPart());
          scale = scale.max(e[m - 2][0].abs().getRealPart());
          scale = scale.max(s[l - 1][0].abs().getRealPart());
          scale = scale.max(e[l - 1][0].abs().getRealPart());

          sm = s[m - 1][0].getRealPart().divide(scale);
          smm1 = s[m - 2][0].getRealPart().divide(scale);
          emm1 = e[m - 2][0].getRealPart().divide(scale);
          sl = s[l - 1][0].getRealPart().divide(scale);
          el = e[l - 1][0].getRealPart().divide(scale);
          b = (smm1.add(sm).multiply(smm1.subtract(sm)).add(emm1.multiply(emm1))).divide(2);
          c = sm.multiply(emm1).multiply(sm.multiply(emm1));
          shift = realUnit.createZero();
          if (b.isZero() == false || c.isZero() == false) {
            shift = (b.multiply(b).add(c)).sqrt();
            if (b.isLessThan(0)) {
              shift = shift.unaryMinus();
            }
            shift = c.divide(b.add(shift));
          }
          f[0] = sl.add(sm).multiply(sl.subtract(sm)).add(shift);
          g[0] = sl.multiply(el);
          /*
           * chase zeros.
           */
          mm1 = m - 1;
          for (k = l; k <= mm1; k++) {
            drotg(f, g, cs, sn);
            if (k != l) {
              e[k - 2][0] = complexUnit.create(f[0]);
            }
            f[0] = cs[0].multiply(s[k - 1][0].getRealPart()).add(sn[0].multiply(e[k - 1][0].getRealPart()));

            /* EE(k) = cs*EE(k) - sn*SS(k); */
            tmpc1 = complexUnit.create(cs[0]).multiply(e[k - 1][0]);
            tmpc =  complexUnit.create(sn[0].unaryMinus()).multiply(s[k - 1][0]);
            e[k - 1][0] = tmpc1.add(tmpc);

            g[0] = sn[0].multiply(s[k][0].getRealPart());
            s[k][0] = s[k][0].multiply(complexUnit .create(cs[0]));

            if (wantv) {
              zdrot(p, v, 0, k - 1, v, 0, k, cs[0], sn[0]);
            }
            drotg(f, g, cs, sn);
            s[k - 1][0] = complexUnit.create(f[0]);
            f[0] = cs[0].multiply(e[k - 1][0].getRealPart()).add(sn[0].multiply(s[k][0].getRealPart()));

            /* SS(k+1) = -sn*EE(k) + cs*SS(k+1); */
            tmpc1 = complexUnit .create(sn[0].unaryMinus()).multiply(e[k - 1][0]);
            tmpc = complexUnit.create(cs[0]).multiply(s[k][0]);
            s[k][0] = tmpc1.add(tmpc);

            g[0] = sn[0].multiply(e[k][0].getRealPart());
            e[k][0] = e[k][0].multiply(complexUnit .create(cs[0]));

            if (wantu && k < n) {
              zdrot(n, u, 0, k - 1, u, 0, k, cs[0], sn[0]);
            }
          }
          e[m - 2][0] = complexUnit .create(f[0]);

          iter++;
          break;
        case 4:
          /*
           * convergence.
           */
          /*
           * make the singular value positive
           */
          if (s[l - 1][0].getRealPart().isLessThan(0)) {
            s[l - 1][0] = s[l - 1][0].unaryMinus();
            if (wantv) {
              zscal(p, one.unaryMinus(), v, 0, l - 1);
            }
          }
          /*
           * order the singular value.
           */
          while (l != mm) {
            if (s[l - 1][0].getRealPart().isGreaterThan(s[l][0].getRealPart())) {
              break;
            }
            CS tmp = s[l - 1][0];
            s[l - 1][0] = s[l][0];
            s[l][0] = tmp;

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
  private void zdrot(final int n, final CS[][] zx, final int zxrow, final int zxcol, final CS[][] zy, final int zyrow, final int zycol, final RS c, final RS s) {
    final CS unit = zx[0][0].createUnit();
    CS ztemp = unit.createZero();
    CS tmpc1 = unit.createZero();
    CS tmpc2 = unit.createZero();

    for (int i = 1; i <= n; i++) {
      // ztemp = c*ZX(i) + s*ZY(i);
      tmpc1 =   unit.create(c).multiply(zx[zxrow + i - 1][zxcol]);
      tmpc2 = unit.create(s).multiply(zy[zyrow + i - 1][zycol]);
      ztemp = tmpc1.add(tmpc2);

      // ZY(i) = c*ZY(i) - s*ZX(i);
      tmpc1 = unit.create(c).multiply(zy[zyrow + i - 1][zycol]);
      tmpc2 = unit.create(s).unaryMinus().multiply(zx[zxrow + i - 1][zxcol]);
      zy[zyrow + i - 1][zycol] = tmpc1.add(tmpc2);
      zx[zxrow + i - 1][zxcol] = ztemp.clone();
    }
  }

  /**
   * ギブンスの回転行列を求めます。
   * 
   * @param da ベクトル1
   * @param db ベクトル2
   * @param c 回転角度のcosine
   * @param s 回転角度のsine
   */
  private void drotg(final RS[] da, final RS[] db, final RS[] c, final RS[] s) {
    RS unit = da[0].createUnit();

    RS r;

    RS roe = db[0];
    if (da[0].abs().isGreaterThan(db[0].abs())) {
      roe = da[0];
    }

    RS scale = da[0].abs().add(db[0].abs());
    if (scale.isZero()) {
      c[0] = unit.createUnit();
      s[0] = unit.createZero();
      r = unit.createZero();
    } else {
      r = scale.multiply((da[0].divide(scale).multiply(da[0].divide(scale)).add(db[0].divide(scale).multiply(db[0].divide(scale)))).sqrt());
      r = dsign(unit.createUnit(), roe).multiply(r);
      c[0] = da[0].divide(r);
      s[0] = db[0].divide(r);
    }

    RS z = unit.createUnit();
    if (da[0].abs().isGreaterThan(db[0].abs())) {
      z = s[0];
    }
    if (db[0].abs().isGreaterThanOrEquals(da[0].abs()) && c[0].isZero() == false) {
      z = c[0].inverse();
    }

    da[0] = r;
    db[0] = z;
  }

  /**
   * 2ノルムを求めます。
   * 
   * @param n ベクトルの成分の数
   * @param zx 行列
   * @param zxrow 行列の行番号
   * @param zxcol 行列の列番号
   * @param incx 番号の増分
   * @return 2ノルム
   */
  private RS dznrm2(final int n, final CS[][] zx, final int zxrow, final int zxcol, final int incx) {
    RS unit = zx[0][0].getRealPart();

    int i;
    boolean imag = false;
    boolean scale = false;
    int next, nn;
    RS cutlo = unit.createZero(), cuthi = unit.createZero(), hitest = unit.createZero(), sum, xmax = unit.createZero(), norm;
    RS absx = unit.createZero(); // ///

    int gotoflag;
    if (n <= 0) {
      norm = unit.createZero();
      return norm;
    }
    next = 30;
    sum = unit.createZero();
    nn = n * incx;

    /*
     * begin main loop
     */
    for (i = 1; i <= nn; i += incx) {
      gotoflag = 0;
      do {
        if (gotoflag < 30) {
          absx = zx[i - 1 + zxrow][zxcol].getRealPart().abs();
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
          if (absx.isGreaterThan(cutlo)) {
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
          if (absx.isZero()) {
            gotoflag = 200; // goto L200;
          } else if (absx.isGreaterThan(cutlo)) {
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
          sum = sum.divide(absx).divide(absx);
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
          if (absx.isGreaterThan(cutlo)) {
            gotoflag = 175; // goto L175;
          } else {
            gotoflag = 0;
          }
        }
        /*
         * common code for phases 2 and 4. in phase 4 sum is large. scale to
         * avoid overflow.
         */
        // L110:
        if (gotoflag < 115) {
          if (absx.isLessThanOrEquals(xmax)) {
            gotoflag = 115; // goto L115;
          } else {
            sum = unit.createUnit().add(sum.multiply(xmax.divide(absx)).multiply(xmax.divide(absx)));
            xmax = absx;
            gotoflag = 200; // goto L200;
          }
        }
        // L115:
        if (gotoflag < 175) {
          sum = sum.add(absx.divide(xmax).multiply(absx.divide(xmax)));
          gotoflag = 200; // goto L200;
        }
        /*
         * prepare for phase 3.
         */
        // L175:
        if (gotoflag < 185) {
          sum = sum.multiply(xmax).multiply(xmax);
          gotoflag = 0;
        }
        // L185:
        if (gotoflag < 190) {
          next = 190;
          scale = false;
          /*
           * for real or d.p. set hitest = cuthi/n for complex set hitest =
           * cuthi/(2*n)
           */
          hitest = cuthi.divide(n);
          /*
           * phase 3. sum is mid-range. no scaling.
           */
          gotoflag = 0;
        }
        // L190:
        if (gotoflag < 200) {
          if (absx.isGreaterThanOrEquals(hitest)) {
            gotoflag = 100; // goto L100;
            continue; // to do loop
          }

          sum = sum.add(absx.multiply(absx));
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
          absx = zx[i - 1 + zxrow][zxcol].getImaginaryPart().abs();
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
    norm = sum.sqrt();
    if (scale) {
      norm = norm.multiply(xmax);
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
  private void zaxpy(final int n, final CS za, final CS[][] zx, final int zxrow, final int zxcol, final CS[][] zy, final int zyrow, final int zycol) {
    if (abs1(za).isZero()) {
      return;
    }

    for (int i = 1; i <= n; i++) {
      // ZY(i) = ZY(i) + za*ZX(i)
      zy[i - 1 + zyrow][zycol] = zy[i - 1 + zyrow][zycol].add(za.multiply(zx[i - 1 + zxrow][zxcol]));
    }
  }

  /**
   * ベクトルの内積を求めます。
   * 
   * @param n ベクトルの成分の数
   * @param zx 行列1
   * @param zxrow 行列1の行番号
   * @param zxcol 行列1の列番号
   * @param zy 行列2
   * @param zyrow 行列2の行番号
   * @param zycol 行列2の列番号
   * @return ベクトルの内積
   */
  private CS zdotc(final int n, final CS[][] zx, final int zxrow, final int zxcol, final CS[][] zy, final int zyrow, final int zycol) {
    CS ans = zx[0][0].createZero();

    for (int i = 1; i <= n; i++) {
      // ztemp = ztemp + dconjg(ZX(i))*ZY(i);
      ans = ans.add(zx[i - 1 + zxrow][zxcol].conjugate().multiply(zy[i - 1 + zyrow][zycol]));
    }

    return ans;
  }

  /**
   * ベクトルを定数(スカラー)倍します。
   * 
   * @param n ベクトルの成分の数
   * @param za スカラー
   * @param zx 対象となる行列
   * @param zxrow 行列の行番号
   * @param zxcol 行列の列番号
   */
  private void zscal(final int n, final CS za, final CS[][] zx, final int zxrow, final int zxcol) {
    for (int i = 1; i <= n; i++) {
      zx[i - 1 + zxrow][zxcol] = zx[i - 1 + zxrow][zxcol].multiply(za);
    }
  }

  /**
   * 2個のベクトルの成分を交換します。
   * 
   * @param n 交換する成分の数
   * @param zx 行列1
   * @param zxrow 行列1の行番号
   * @param zxcol 行列1の列番号
   * @param zy 行列2
   * @param zyrow 行列2の行番号
   * @param zycol 行列2の列番号
   */
  private void zswap(final int n, final CS[][] zx, final int zxrow, final int zxcol, final CS[][] zy, final int zyrow, final int zycol) {
    for (int i = 1; i <= n; i++) {
      final CS tmp = zx[i - 1 + zxrow][zxcol];
      zx[i - 1 + zxrow][zxcol] = zy[i - 1 + zyrow][zycol];
      zy[i - 1 + zyrow][zycol] = tmp;
    }
  }

  /**
   * bが正なら、aの絶対値、bが負ならaの絶対値の符号を負にした数を求めます。
   * 
   * @param a 絶対値を決める数
   * @param b 符号を決める数
   * @return 計算結果
   */
  private RS dsign(final RS a, final RS b) {
    return b.isGreaterThanOrEquals(0) ? a.abs() :a.abs().unaryMinus();
  }

  /**
   * 実部の絶対値と虚部の絶対値の和を返します。
   * 
   * @param c 対象となる複素数
   * @return 実部の絶対値と虚部の絶対値の和
   */
  private RS abs1(final CS c) {
    return c.getRealPart().abs().add(c.getImaginaryPart().abs());
  }

  /**
   * ベクトルの方向がbと同じ、大きさがaと同じ複素数を返します。
   * 
   * @param a 対象となる複素数
   * @param b 対象となる複素数
   * @return ベクトルの方向がbと同じ、大きさがaと同じ複素数
   */
  private CS sign(final CS a, final CS b) {
    return b.multiply(a.abs().divide(b.abs()));
  }
  
  /**
   * 最大反復数を設定します。
   * @param maxIteration 最大反復数
   */
  public void setMaxIteration(final int maxIteration) {
    this.maxIteration = maxIteration;
  }
}