/*
 * $Id: RealSingularValueDecomposition.java,v 1.4 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.svd;

import org.mklab.nfc.matrix.BaseMatrixUtil;
import org.mklab.nfc.matrix.AbstractNumericalMatrixUtil;
import org.mklab.nfc.matrix.GridUtil;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 実行列の特異値分解を行うためのクラスです。
 * 
 * @author koga
 * @version $Revision: 1.4 $
 * 
 *  @param <S> 成分の型
 * @param <M> 行列の型  
 */
public final class RealSingularValueDecomposer<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {
  /** 最大反復数 */
  private int maxIteration = 30;
  
  /**
   * 実行列の特異値分解を返します。
   * 
   * <p>対象となる行列をA、特異値を対角成分とする対角行列を D、左特異ベクトルからなる直交行列をU、右特異ベクトルからなる直交行列をVとすると、
   * 
   * <blockquote> A = U * D * V <sup>T </sup> </blockquote>
   * 
   * 　の関係が成り立ちます。
   * 
   * @param a 対象となる行列
   * @return U, D, V
   */
  public SingularValueDecompositionElements<S,M> decompose(final S[][] a) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;
    final int size = Math.min(rowSize, columnSize);

    final S[] values = GridUtil.createZero(a[0], size);
    final S[][] leftVector = GridUtil.createZero(a, rowSize, rowSize);
    final S[][] rightVector = GridUtil.createZero(a, columnSize, columnSize);

    decompose(a, values, leftVector, rightVector);

    final S[][] diagonalValues = GridUtil.vectorToDiagonal(values);
    final S[][] valueMatrix = GridUtil.createZero(a, rowSize, columnSize);
    GridUtil.setSubMatrix(valueMatrix, 0, size - 1, 0, size - 1, diagonalValues);
    return new SingularValueDecompositionElements<>(leftVector, valueMatrix, rightVector);
  }

  /**
   * 実行列の特異値を返します。
   * 
   * @param a 対象となる行列
   * @return 特異値を成分とする配列
   */
  public S[] singularValue(final S[][] a) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final S[] values = GridUtil.createZero(a[0], Math.min(rowSize, columnSize));
    final S[][] leftVector = GridUtil.createZero(a, rowSize, rowSize);
    final S[][] rightVector = GridUtil.createZero(a, columnSize, columnSize);

    decompose(a, values, leftVector, rightVector);

    return values;
  }

  /**
   * 実行列の最大特異値を返します。
   * 
   * @param a 対象となる行列
   * @return 最大特異値
   */
  public S maximumSingularValue(final S[][] a) {
    return singularValue(a)[0];
  }

  /**
   * 実行列の最小特異値を返します。
   * 
   * @param a 対象となる行列
   * @return 最小特異値
   */
  public S minimumSingularValue(final S[][] a) {
    final S[] singularValues = singularValue(a);
    return singularValues[singularValues.length - 1];
  }

  /**
   * 実行列が非正則であるか判定します。
   * 
   * @param a 非正則性を調べる行列
   * @param tolerance 許容誤差
   * @return 非正則ならばtrue、そうでなければfalse
   */
  public boolean isSingular(final S[][] a, final S tolerance) {
    final S[] singularValues = singularValue(a);
    return singularValues[singularValues.length - 1].isLessThan(tolerance);
  }

  /**
   * 実行列のランク(階数)を返します。
   * 
   * @param a 対象となる行列
   * @param tolerance 許容誤差
   * @return ランク
   */
  public int rank(final S[][] a, final S tolerance) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final S[][] a2;
    if (rowSize > columnSize) {
      a2 = GridUtil.transpose(a);
    } else {
      a2 = a;
    }

    final S[] singularValues = singularValue(a2);

    int rank = 0;
    for (int i = 0; i < singularValues.length; i++) {
      if (singularValues[i].isGreaterThan(tolerance)) {
        rank++;
      }
    }

    return rank;
  }

  /**
   * 実行列がフルランクであるか判定します。
   * 
   * @param a 対象となる行列
   * @param tolerance 許容誤差
   * @return フルランクならばtrue、そうでなければfalse
   */
  public boolean isFullRank(final S[][] a, final S tolerance) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final int rank = rank(a, tolerance);

    if (rank == rowSize || rank == columnSize) {
      return true;
    }
    return false;
  }

  /**
   * 実行列の擬似逆行列を返します。
   * 
   * @param a 対象となる行列
   * @param tolerance 許容誤差
   * @return 擬似逆行列
   */
  public S[][] pseudoInverse(final S[][] a, final S tolerance) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    /*
     * A = u diag(val) v'
     */
    SingularValueDecompositionElements<S,M> udv = decompose(a);
    final S[][] u = udv.getU();
    final S[][] d = udv.getD();
    final     S[][] v = udv.getV();

    int rank = 0;
    for (int i = 0; i < Math.min(rowSize, columnSize); i++) {
      if (d[i][i].isGreaterThan(tolerance)) {
        rank++;
      }
    }

    /*
     * pseudoInverse(A) := v diag(val)~ u'
     */
    final S[][] dInv = GridUtil.createZero(a, columnSize, rowSize);

    for (int i = 1; i <= rank; i++) {
      dInv[i - 1][i - 1] = d[i - 1][i - 1].inverse();
    }

    final S[][] ut = GridUtil.transpose(u);
    final S[][] dInvUt = BaseMatrixUtil.multiply(dInv, ut);
    return BaseMatrixUtil.multiply(v, dInvUt);
  }

  /**
   * 線形方程式の最小二乗解を返します。
   * 
   * <p>aで表される実行列をA, bで表される実行列をBとすとき、
   * 
   * <blockquote> A * X = B </blockquote>
   * 
   * の解即ち
   * 
   * <blockquote> X = A<sup>-1</sup> * B </blockquote>
   * 
   * を返します。
   * 
   * @param a 行列
   * @param b 行列
   * @param tolerance 許容誤差
   * @return 線形方程式の解
   */
  public S[][] leastSquare(final S[][] a, final S[][] b, final S tolerance) {
    return BaseMatrixUtil.multiply(pseudoInverse(a, tolerance), b);
  }

  /**
   * 実行列のカーネル(零空間)を張るベクトルからなる行列を返します。
   * 
   * <p>許容誤差より小さい特異値をゼロと見なします。
   * 
   * @param a 行列
   * @param tolerance 許容誤差
   * @return カーネル
   */
  public S[][] kernel(final S[][] a, final S tolerance) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final S[][] a2;
    if (rowSize < columnSize) {
      S[][] zeros = GridUtil.createZero(a, columnSize - rowSize, columnSize);

      a2 = GridUtil.appendDown(a, zeros);
    } else {
      a2 = a;
    }

    SingularValueDecompositionElements<S,M> udv = decompose(a2);
    final S[][] d = udv.getD();
    final S[][] v = udv.getV();

    int rank = 0;
    for (int i = 0; i < Math.min(rowSize, columnSize); i++) {
      if (d[i][i].isGreaterThan(tolerance)) {
        rank++;
      }
    }

    if (rank == Math.max(rowSize, columnSize)) {
      throw new RuntimeException(Messages.getString("RealSingularValueDecomposition.0")); //$NON-NLS-1$
    }

    return GridUtil.getSubMatrix(v, 0, columnSize - 1, rank, columnSize - 1);
  }

  /**
   * 実行列の最大特異値(2-ノルム)を返します。
   * 
   * @param a 対象となる行列
   * @return 最大特異値(2-ノルム)
   */
  public S norm(final S[][] a) {
    return maximumSingularValue(a);
  }

  /**
   * 実行列の条件数を返します。
   * 
   * @param a 対象となる行列
   * @return 条件数
   */
  public S conditionNumber(final S[][] a) {
    if (a.length == 0 || a[0].length == 0) {
      throw new IllegalArgumentException(Messages.getString("RealSingularValueDecomposition.1")); //$NON-NLS-1$
    }

    final S[] singularValues = singularValue(a);

    if (GridUtil.anyZero(singularValues)) {
      return a[0][0].getInfinity();
    }

    return AbstractNumericalMatrixUtil.max(singularValues).divide(AbstractNumericalMatrixUtil.min(singularValues));
  }

  /**
   * 特異値分解を返します。
   * 
   * @param a 対象となる行列
   * @param values 特異値
   * @param leftVector 左特異値行列
   * @param rightVector 右特異値行列
   */
  private void decompose(final S[][] a, final S[] values, final S[][] leftVector, final S[][] rightVector) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final int size = Math.max(rowSize, columnSize);

    final S[][] a2;
    if (rowSize > columnSize) {
      a2 = GridUtil.transpose(a);
    } else {
      a2 = a;
    }

    final S[][] lvec = GridUtil.createZero(a, size, size);
    final S[][] rvec = GridUtil.createZero(a, size, size);
    final S[] val = GridUtil.createZero(a[0], size);

    final int errorCode = svd(a2, true, lvec, true, rvec, val);

    if (0 < errorCode) {
      throw new RuntimeException(Messages.getString("RealSingularValueDecomposition.3")); //$NON-NLS-1$
    }

    /*
     * Sorting with restpect to the singular valute
     */
    for (int i = 1; i < size; i++) {
      for (int j = 1; j < size; j++) {
        if (val[j - 1].isLessThan(val[j])) {
          final S dd = val[j - 1];
          val[j - 1] = val[j];
          val[j] = dd;
          GridUtil.exchangeColumn(lvec, j - 1, j);
          GridUtil.exchangeColumn(rvec, j - 1, j);
        }
      }
    }

    if (rowSize > columnSize) {
      GridUtil.setSubMatrix(leftVector, 0, 0, rvec, 0, leftVector.length - 1, 0, leftVector[0].length - 1);
      GridUtil.setSubMatrix(rightVector, 0, 0, lvec, 0, rightVector.length - 1, 0, rightVector[0].length - 1);
      GridUtil.setSubVector(values, 0, val, 0, values.length - 1);
    } else {
      GridUtil.setSubMatrix(leftVector, 0, 0, lvec, 0, leftVector.length - 1, 0, leftVector[0].length - 1);
      GridUtil.setSubMatrix(rightVector, 0, 0, rvec, 0, rightVector.length - 1, 0, rightVector[0].length - 1);
      GridUtil.setSubVector(values, 0, val, 0, values.length - 1);
    }
  }

  /**
   * @param a 対象となる行列
   * @param uDesired Uを求めるならばtrue
   * @param u Uの値を代入する配列
   * @param vDesired Vを求めるならばtrue
   * @param v Vの値を代入する配列
   * @param w Wの値を代入する配列
   * @return 特異値分解の結果
   */
  private int svd(final S[][] a, final boolean uDesired, final S[][] u, final boolean vDesired, final S[][] v, final S[] w) {
    final S unit = a[0][0].createUnit();

    int l = 0, l1 = 0, ierr = 0;
    S f, h, s, z;

    int rowSize = a.length;
    int columnSize = rowSize == 0 ? 0 : a[0].length;

    S[] rv1 = GridUtil.createZero(a[0], columnSize); // unit.createArray(columnSize);

    for (int i = 1; i <= rowSize; i++) {
      for (int j = 1; j <= columnSize; j++) {
        u[i - 1][j - 1] = a[i - 1][j - 1];
      }
    }

    /*
     * Householder reduction to bidiagonal form
     */
    S g = unit.createZero();
    S scale = unit.createZero();
    S x = unit.createZero();

    for (int i = 1; i <= columnSize; i++) {
      l = i + 1;
      rv1[i - 1] = scale.multiply(g);
      g = unit.createZero();
      s = unit.createZero();
      scale = unit.createZero();

      if (i <= rowSize) {
        for (int k = i; k <= rowSize; k++) {
          scale = scale.add(u[k - 1][i - 1].abs());
        }

        /* if (Math.abs(scale) > my_eps) { */
        if (scale.isZero() == false) {
          for (int k = i; k <= rowSize; k++) {
            u[k - 1][i - 1] = u[k - 1][i - 1].divide(scale);
            s = s.add(u[k - 1][i - 1].multiply(u[k - 1][i - 1]));
          }

          f = u[i - 1][i - 1];
          g = fsign(s.sqrt(), f).unaryMinus();
          h = f.multiply(g).subtract(s);
          u[i - 1][i - 1] = f.subtract(g);

          if (i != columnSize) {
            for (int j = l; j <= columnSize; j++) {
              s = unit.createZero();

              for (int k = i; k <= rowSize; k++) {
                s = s.add(u[k - 1][i - 1].multiply(u[k - 1][j - 1]));
              }

              f = s.divide(h);

              for (int k = i; k <= rowSize; k++) {
                u[k - 1][j - 1] = u[k - 1][j - 1].add(f.multiply(u[k - 1][i - 1]));
              }
            }
          }

          for (int k = i; k <= rowSize; k++) {
            u[k - 1][i - 1] = u[k - 1][i - 1].multiply(scale);
          }
        }
      }

      w[i - 1] = scale.multiply(g);
      g = unit.createZero();
      s = unit.createZero();
      scale = unit.createZero();

      if (i > rowSize || i == columnSize) {
        S tmp = w[i - 1].abs().add(rv1[i - 1].abs());
        x = x.isGreaterThan(tmp) ? x.clone() : tmp;
        continue;
      }

      for (int k = l; k <= columnSize; k++) {
        scale = scale.add(u[i - 1][k - 1].abs());
      }

      /* if (Math.abs(scale) <= my_eps) { */
      if (scale.isZero()) {
        S tmp = w[i - 1].abs().add(rv1[i - 1].abs());
        x = x.isGreaterThan(tmp) ? x.clone() : tmp;
        continue;
      }

      for (int k = l; k <= columnSize; k++) {
        u[i - 1][k - 1] = u[i - 1][k - 1].divide(scale);
        s = s.add(u[i - 1][k - 1].multiply(u[i - 1][k - 1]));
      }

      f = u[i - 1][l - 1];
      g = fsign(s.sqrt(), f).unaryMinus();
      h = f.multiply(g).subtract(s);
      u[i - 1][l - 1] = f.subtract(g);

      for (int k = l; k <= columnSize; k++) {
        rv1[k - 1] = u[i - 1][k - 1].divide(h);
      }

      if (i != rowSize) {
        for (int j = l; j <= rowSize; j++) {
          s = unit.createZero();

          for (int k = l; k <= columnSize; k++) {
            s = s.add(u[j - 1][k - 1].multiply(u[i - 1][k - 1]));
          }

          for (int k = l; k <= columnSize; k++) {
            u[j - 1][k - 1] = u[j - 1][k - 1].add(s.multiply(rv1[k - 1]));
          }
        }
      }

      for (int k = l; k <= columnSize; k++) {
        u[i - 1][k - 1] = u[i - 1][k - 1].multiply(scale);
      }

      final S tmp = w[i - 1].abs().add(rv1[i - 1].abs());
      x = x.isGreaterThan(tmp) ? x.clone() : tmp;
      //x = isGeaterThan(x, tmp) ? x.clone() : tmp;
    }

    /*
     * Accumulation of right-hand transformations.
     */
    if (vDesired) {
      for (int ii = 1; ii <= columnSize; ii++) {
        int i = columnSize + 1 - ii;

        if (i != columnSize) {
          /* if (fabs(g) > my_eps) { */
          if (g.isZero() == false) {
            /*
             * Double division avoids possible underflow
             */
            for (int j = l; j <= columnSize; j++) {
              v[j - 1][i - 1] = (u[i - 1][j - 1].divide(u[i - 1][l - 1])).divide(g);
            }

            for (int j = l; j <= columnSize; j++) {
              s = unit.createZero();

              for (int k = l; k <= columnSize; k++) {
                s = s.add(u[i - 1][k - 1].multiply(v[k - 1][j - 1]));
              }

              for (int k = l; k <= columnSize; k++) {
                v[k - 1][j - 1] = v[k - 1][j - 1].add(s.multiply(v[k - 1][i - 1]));
              }
            }
          }

          for (int j = l; j <= columnSize; j++) {
            v[i - 1][j - 1] = unit.createZero();
            v[j - 1][i - 1] = unit.createZero();
          }
        }

        v[i - 1][i - 1] = unit.createUnit();
        g = rv1[i - 1];
        l = i;
      }
    }

    /*
     * Accumulation of left-hand transformations
     */
    if (uDesired) {
      int mn = columnSize;
      if (rowSize < columnSize) {
        mn = rowSize;
      }

      for (int ii = 1; ii <= mn; ii++) {
        int i = mn + 1 - ii;
        l = i + 1;
        g = w[i - 1];

        if (i != columnSize) {
          for (int j = l; j <= columnSize; j++) {
            u[i - 1][j - 1] = unit.createZero();
          }
        }

        /* if (fabs(g) <= my_eps) { */
        if (g.isZero()) {
          for (int j = i; j <= rowSize; j++) {
            u[j - 1][i - 1] = unit.createZero();
          }
        } else {
          if (i != mn) {
            for (int j = l; j <= columnSize; j++) {
              s = unit.createZero();

              for (int k = l; k <= rowSize; k++) {
                s = s.add(u[k - 1][i - 1].multiply(u[k - 1][j - 1]));
              }

              /*
               * Double division avoids possible underflow
               */
              f = (s.divide(u[i - 1][i - 1])).divide(g);

              for (int k = i; k <= rowSize; k++) {
                u[k - 1][j - 1] = u[k - 1][j - 1].add(f.multiply(u[k - 1][i - 1]));
              }
            }
          }

          for (int j = i; j <= rowSize; j++) {
            u[j - 1][i - 1] = u[j - 1][i - 1].divide(g);
          }
        }

        u[i - 1][i - 1] = u[i - 1][i - 1].add(unit.createUnit());
      }
    }

    /*
     * Diagonaliztion of the bidagonal form.
     */
    // eps = meps * x;
    S tst1 = x;

    for (int kk = 1; kk <= columnSize; kk++) {
      int k1 = columnSize - kk;
      int k = k1 + 1;
      int its = 0;

      // L520:
      do {
        /*
         * Test for splitting.
         */
        S tst2;
        boolean goto565 = false;

        for (int ll = 1; ll <= k; ll++) {
          l1 = k - ll;
          l = l1 + 1;

          tst2 = tst1.add(rv1[l - 1].abs());

          if (tst2.equals(tst1)) {
            goto565 = true; // goto L565;
            break;
          }
          /*
           * RV1(1) is always zero, so there is no exit through the bottom of
           * the loop
           */

          tst2 = tst1.add(w[l1 - 1].abs());
          if (tst2.equals(tst1)) {
            break;
          }
        }

        if (!goto565) {

          /*
           * Cancellation of RV1(l) if l greater than 1
           */
          S c = unit.createZero();
          s = unit.createUnit();

          for (int i = l; i <= k; i++) {
            f = s.multiply(rv1[i - 1]);
            rv1[i - 1] = rv1[i - 1].multiply(c);

            tst2 = tst1.add(f.abs());
            if (tst2.equals(tst1)) {
              break;
            }

            g = w[i - 1];
            h = (f.multiply(f).add(g.multiply(g))).sqrt();
            w[i - 1] = h;
            c = g.divide(h);
            s = f.unaryMinus().divide(h);

            if (uDesired) {
              for (int j = 1; j <= rowSize; j++) {
                S y = u[j - 1][l1 - 1];
                z = u[j - 1][i - 1];
                u[j - 1][l1 - 1] = y.multiply(c).add(z.multiply(s));
                u[j - 1][i - 1] = y.unaryMinus().multiply(s).add(z.multiply(c));
              }
            }
          }
        }
        /*
         * Test for convergence
         */
        // L565:
        z = w[k - 1];
        if (l == k) {
          break; // goto L650;
        }

        /*
         * Shift from botom 2 by 2 minor
         */
        if (its == this.maxIteration) {
          //  Set error -- no convergence to a singular value after 30 iterations
          ierr = k;
          break; // goto L650;
        }

        its++;
        x = w[l - 1];
        S y = w[k1 - 1];
        g = rv1[k1 - 1];
        h = rv1[k - 1];
        f = y.subtract(z).multiply(y.add(z)).add(g.subtract(h).multiply(g.add(h))).divide(h.multiply(y).multiply(2));
        g = (f.multiply(f).add(unit.createUnit())).sqrt();
        f = x.subtract(z).multiply(x.add(z)).add(h.multiply((y.divide(f.add(fsign(g, f))).subtract(h)))).divide(x);

        /*
         * Next QR transformation
         */
        S c = unit.createUnit();
        s = unit.createUnit();

        for (int i1 = l; i1 <= k1; i1++) {
          int i = i1 + 1;
          g = rv1[i - 1];
          y = w[i - 1];
          h = s.multiply(g);
          g = c.multiply(g);
          z = (f.multiply(f).add(h.multiply(h))).sqrt();
          rv1[i1 - 1] = z.clone();
          c = f.divide(z);
          s = h.divide(z);
          f = x.multiply(c).add(g.multiply(s));
          g = x.unaryMinus().multiply(s).add(g.multiply(c));
          h = y.multiply(s);
          y = y.multiply(c);

          /* Update of matrix V */
          if (vDesired) {
            for (int j = 1; j <= columnSize; j++) {
              x = v[j - 1][i1 - 1];
              z = v[j - 1][i - 1];
              v[j - 1][i1 - 1] = x.multiply(c).add(z.multiply(s));
              v[j - 1][i - 1] = x.unaryMinus().multiply(s).add(z.multiply(c));
            }
          }

          z = (f.multiply(f).add(h.multiply(h))).sqrt();
          w[i1 - 1] = z.clone();

          /*
           * Rotation can be arbitrary if z is zero
           */
          if (z.isZero() == false) {
            c = f.divide(z);
            s = h.divide(z);
          }

          f = c.multiply(g).add(s.multiply(y));
          x = s.unaryMinus().multiply(g).add(c.multiply(y));

          /* Update of matrix U */
          if (uDesired) {
            for (int j = 1; j <= rowSize; j++) {
              y = u[j - 1][i1 - 1];
              z = u[j - 1][i - 1];
              u[j - 1][i1 - 1] = y.multiply(c).add(z.multiply(s));
              u[j - 1][i - 1] = y.unaryMinus().multiply(s).add(z.multiply(c));
            }
          }
        }

        rv1[l - 1] = unit.createZero();
        rv1[k - 1] = f;
        w[k - 1] = x;
      } while (true);

      // goto L520;

      // L650:
      /*
       * Convergence
       */
      if (z.isGreaterThanOrEquals(unit.createZero())) {
        continue;
      }

      /* w(k) is made non-negative */
      w[k - 1] = z.unaryMinus();

      if (vDesired) {
        for (int j = 1; j <= columnSize; j++) {
          v[j - 1][k - 1] = v[j - 1][k - 1].unaryMinus();
        }
      }
    }

    return ierr;
  }

  /**
   * 絶対値と符号を2個の実数で決めます。
   * 
   * @param a 絶対値を決める実数
   * @param b 符号を決める実数
   * @return 2個の実数から作られた実数
   */
  private S fsign(final S a, final S b) {
//    if (b.isReal()) {
      return b.isGreaterThanOrEquals(a.createZero()) ? a.abs() : a.abs().unaryMinus();
//    }
//    NumericalScalar<?,?> bReal = ((ComplexScalar<?>)b).getRealPart();
//    return (E)(bReal.isGreaterThanOrEquals(bReal.createZero()) ? a.abs() : a.abs().unaryMinus());
  }
  
  /**
   * 最大反復数を設定します。
   * @param maxIteration 最大反復数
   */
  public void setMaxIteration(final int maxIteration) {
    this.maxIteration = maxIteration;
  }
}