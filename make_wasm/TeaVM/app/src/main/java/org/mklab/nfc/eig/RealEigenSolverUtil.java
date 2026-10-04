/*
 * $Id: RealEigenUtil.java,v 1.6 2008/03/15 00:23:43 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 実行列の固有値を求めるためのユーティリティクラスです。
 * 
 * @author koga
 * @version $Revision: 1.6 $
 */
final class RealEigenSolverUtil {
  /**
   * 新しく生成された<code>RealEigenSolverUtil</code>オブジェクトを初期化します。
   */
  private RealEigenSolverUtil() {
    // nothing to do
  }

  /**
   * 行列を直交同次変換により上ヘッセンベルグ行列へ変換します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param a 入力:対象となる行列、出力:ヘッセンベルグ行列
   * @param low 開始番号
   * @param igh 終了番号
   * @param ort 変換に関する情報
   */
  static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> void orthes(final S[][] a, final int low, final int igh, final S[] ort) {
    final S unit = a[0][0].createUnit();

    final int size = a.length;
    final int la = igh - 1;
    final int kp1 = low + 1;

    if (la < kp1) {
      return;
    }

    for (int m = kp1 - 1; m < la; m++) {
      S h = unit.createZero();
      ort[m] = unit.createZero();
      S scale = unit.createZero();
      int m1 = m - 1;

      /*
       * Scale Column (Algol tol then not needed )
       */

      for (int i = m; i < igh; i++) {
        scale = scale.add(a[i][m1].abs());
      }

      if (scale.isZero()) {
        continue;
      }

      for (int i = igh - 1; i >= m; i--) {
        S oo = a[i][m1].divide(scale);
        ort[i] = oo.clone();
        h = h.add(oo.multiply(oo));
      }
      S a1 = h.sqrt();

      S g = (ort[m].isGreaterThanOrEquals(unit.abs().createZero()) ? a1.abs() : a1.abs().unaryMinus()).unaryMinus();
      h = h.subtract(ort[m].multiply(g));
      ort[m] = ort[m].subtract(g);

      /*
       * (I - (U * U^T)/H) * A
       */
      for (int j = m; j < size; j++) {
        S f = unit.createZero();

        for (int i = igh - 1; i >= m; i--) {
          f = f.add(ort[i].multiply(a[i][j]));
        }

        f = f.divide(h);

        for (int i = m; i < igh; i++) {
          a[i][j] = a[i][j].subtract(f.multiply(ort[i]));
        }
      }

      /*
       * (I - (U * U^T)/H) * A * (I - (U * U^T)/H)
       */
      for (int i = 0; i < igh; i++) {
        S f = unit.createZero();

        S[] ai = a[i];
        for (int j = igh - 1; j >= m; j--) {
          f = f.add(ort[j].multiply(ai[j]));
        }

        f = f.divide(h);

        for (int j = m; j < igh; j++) {
          ai[j] = ai[j].subtract(f.multiply(ort[j]));
        }
      }

      ort[m] = ort[m].multiply(scale);
      a[m][m1] = scale.multiply(g);
    }
  }

  /**
   * orthesによって直交同次変換を累積します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param a 直交変換に関する情報
   * @param low 開始番号
   * @param igh 終了番号
   * @param ort 直交変換に関する追加情報
   * @param z 直交同次変換を累積した行列
   */
  static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> void ortran(final S[][] a, final int low, final int igh, final S[] ort, final S[][] z) {
    int size = a.length;

    S unit = a[0][0].createUnit();

    /*
     * Initialize Z to identity matrix
     */
    for (int i = 1; i <= size; i++) {
      for (int j = 1; j <= size; j++) {
        z[i - 1][j - 1] = unit.createZero();
      }
      z[i - 1][i - 1] = unit.createUnit();
    }

    int kl = igh - low - 1;

    if (kl < 1) {
      return;
    }

    for (int mp = igh - 1; mp >= low + 1; mp--) {
      if (a[mp - 1][mp - 2].isZero()) {
        continue;
      }

      int mp1 = mp + 1;

      for (int i = mp1; i <= igh; i++) {
        ort[i - 1] = a[i - 1][mp - 2];
      }

      for (int j = mp; j <= igh; j++) {
        S g = unit.createZero();

        for (int i = mp; i <= igh; i++) {
          g = g.add(ort[i - 1].multiply(z[i - 1][j - 1]));
        }

        /*
         * Divisor below is negative of H formed in orthes(). Double division
         * avoids possible underflow.
         */
        g = (g.divide(ort[mp - 1])).divide(a[mp - 1][mp - 2]);

        for (int i = mp; i <= igh; i++) {
          z[i - 1][j - 1] = z[i - 1][j - 1].add(g.multiply(ort[i - 1]));
        }
      }
    }
  }

  /**
   * 実上ヘッセンベルグ行列の固有値と固有ベクトルを求めます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param h 上ヘッセンベルグ行列
   * @param low 開始番号
   * @param igh 終了番号
   * @param wr 固有値の実部
   * @param wi 固有値の虚部
   * @param z 固有ベクトルの実部と虚部
   * @param schur シュアー分解のみを行うならばtrue
   * @return 計算結果
   */
  static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> int hqr2(final S[][] h, final int low, final int igh, final S[] wr, final S[] wi, final S[][] z, final boolean schur) {
    S unit = h[0][0].createUnit();

    S p = unit.createZero();
    S q = unit.createZero();
    S r = unit.createZero();
    S s = unit.createZero();
    S zz = unit.createZero();

    int size = h.length;
    S x;
    S y = unit.createZero();
    S w = unit.createZero();

    /*
     * MACHEP is a machine dependent parameter specifying the relative precision
     * of floating point arithmetic.
     */
    S meps = unit.getMachineEpsilon();

    /*
     * Store roots isolated by balance() and compute matrix norm.
     */
    S norm = unit.createZero();
    int k = 1;
    for (int i = 1; i <= size; i++) {
      for (int j = k; j <= size; j++) {
        norm = norm.add(h[i - 1][j - 1].abs());
      }

      k = i;
      if (i < low || i > igh) {
        wr[i - 1] = h[i - 1][i - 1];
        wi[i - 1] = unit.createZero();
      }
    }
    int en = igh;
    S t = unit.createZero();
    int itn = 100 * size;

    /*
     * Serch for next eigenvalue
     */

    // L60:
    do {
      if (en < low) {
        break; // goto L340
      }

      int its = 0;
      int na = en - 1;
      int enm2 = na - 1;
      /*
       * Look for single small sub-diagonal element
       */
      // L70:
      boolean goto280;
      do {
        goto280 = false;
        int l;
        for (l = en; l >= low; l--) {
          if (l == low) {
            break;
          }
          s = h[l - 2][l - 2].abs().add(h[l - 1][l - 1].abs());
          if (s.isZero()) {
            s = norm;
          }
          if (h[l - 1][l - 2].abs().isLessThanOrEquals(meps.multiply(s))) {
            break;
          }
        }

        /*
         * Form shift
         */
        x = h[en - 1][en - 1];
        if (l == en) {
          break; // goto L270
        }
        y = h[na - 1][na - 1];
        w = h[en - 1][na - 1].multiply(h[na - 1][en - 1]);
        if (l == na) {
          goto280 = true; // goto L280 leave do loop
          break;
        }
        if (itn == 0) {
          return en;
        }

        /*
         * Form exceptional shift
         */
        if (its != 0 && (its % 10) == 0) {
          t = t.add(x);
          for (int i = low; i <= en; i++) {
            h[i - 1][i - 1] = h[i - 1][i - 1].subtract(x);
          }

          s = h[en - 1][na - 1].abs().add(h[na - 1][enm2 - 1].abs());

          S z75 = unit.multiply(75).divide(100);
          S z4375 = unit.multiply(4375).divide(10000);

          x = z75.multiply(s);
          y = x.clone();
          w = z4375.unaryMinus().multiply(s).multiply(s);
        }
        its++;
        itn--;

        /*
         * Look for two consecutive small sub-diagonal elements
         */
        int m;
        for (m = enm2; m >= l; m--) {
          zz = h[m - 1][m - 1];
          r = x.subtract(zz);
          s = y.subtract(zz);
          p = r.multiply(s).subtract(w).divide(h[m][m - 1]).add(h[m - 1][m]);
          q = h[m][m].subtract(zz).subtract(r).subtract(s);
          r = h[m + 1][m];
          s = p.abs().add(q.abs()).add(r.abs());
          p = p.divide(s);
          q = q.divide(s);
          r = r.divide(s);
          if (m == l) {
            break;
          }
          if (h[m - 1][m - 2].abs().multiply(q.abs().add(r.abs())).isLessThanOrEquals(meps.multiply(p.abs()).multiply((h[m - 2][m - 2].abs().add(zz.abs()).add(h[m][m].abs()))))) {
            break;
          }
        }
        int mp2 = m + 2;
        for (int i = mp2; i <= en; i++) {
          h[i - 1][i - 3] = unit.createZero();
          if (i != mp2) {
            h[i - 1][i - 4] = unit.createZero();
          }
        }

        /*
         * Double QR step involving rows l to en and columns m to en
         */
        for (k = m; k <= na; k++) { // 260
          boolean notlas = (k != na ? true : false);
          if (k != m) {
            p = h[k - 1][k - 2];
            q = h[k][k - 2];
            r = unit.createZero();
            if (notlas) {
              r = h[k + 1][k - 2];
            }
            x = p.abs().add(q.abs()).add(r.abs());
            if (x.isZero()) {
              continue; // goto L260; //260
            }
            p = p.divide(x);
            q = q.divide(x);
            r = r.divide(x);
          }
          S a = (p.multiply(p).add(q.multiply(q)).add(r.multiply(r))).sqrt();
          S b = p;
          s = b.isGreaterThanOrEquals(unit.createZero()) ? a.abs() : a.abs().unaryMinus();
          if (k != m) {
            h[k - 1][k - 2] = s.unaryMinus().multiply(x);
          } else if (l != m) {
            h[k - 1][k - 2] = h[k - 1][k - 2].unaryMinus();
          }
          p = p.add(s);
          x = p.divide(s);
          y = q.divide(s);
          zz = r.divide(s);
          q = q.divide(p);
          r = r.divide(p);

          /*
           * Row modification
           */
          int j;
          for (j = k; j <= size; j++) {
            p = h[k - 1][j - 1].add(q.multiply(h[k][j - 1]));
            if (notlas) {
              p = p.add(r.multiply(h[k + 1][j - 1]));
              h[k + 1][j - 1] = h[k + 1][j - 1].subtract(p.multiply(zz));
            }
            h[k][j - 1] = h[k][j - 1].subtract(p.multiply(y));
            h[k - 1][j - 1] = h[k - 1][j - 1].subtract(p.multiply(x));
          }
          j = en > (k + 3) ? (k + 3) : en;

          /*
           * Column modification
           */
          for (int i = 1; i <= j; i++) {
            p = x.multiply(h[i - 1][k - 1]).add(y.multiply(h[i - 1][k]));
            if (notlas) {
              p = p.add(zz.multiply(h[i - 1][k + 1]));
              h[i - 1][k + 1] = h[i - 1][k + 1].subtract(p.multiply(r));
            }
            h[i - 1][k] = h[i - 1][k].subtract(p.multiply(q));
            h[i - 1][k - 1] = h[i - 1][k - 1].subtract(p);

          }

          /*
           * Accumulate transformation
           */
          for (int i = low; i <= igh; i++) {
            p = x.multiply(z[i - 1][k - 1]).add(y.multiply(z[i - 1][k]));
            if (notlas) {
              p = p.add(zz.multiply(z[i - 1][k + 1]));
              z[i - 1][k + 1] = z[i - 1][k + 1].subtract(p.multiply(r));
            }
            z[i - 1][k] = z[i - 1][k].subtract(p.multiply(q));
            z[i - 1][k - 1] = z[i - 1][k - 1].subtract(p);
          }
          // L260: continue;
        }
      } while (true); // goto L70;

      // L270:
      if (!goto280) {

        /*
         * One root found
         */

        h[en - 1][en - 1] = x.add(t);
        wr[en - 1] = h[en - 1][en - 1].clone();

        wi[en - 1] = unit.createZero();
        en = na;
        continue; // goto L60;
      }
      /*
       * Two roots found
       */
      goto280 = false;
      // L280:

      p = y.subtract(x).divide(2);
      q = p.multiply(p).add(w);
      zz = q.abs().sqrt();
      h[en - 1][en - 1] = x.add(t);
      x = h[en - 1][en - 1];
      h[na - 1][na - 1] = y.add(t);

      /*
       * Real pair
       */
      if (q.isGreaterThanOrEquals(unit.createZero())) {
        S a = zz.clone();
        zz = p.add(p.isGreaterThanOrEquals(unit.createZero()) ? a.abs() : a.abs().unaryMinus());
        wr[na - 1] = x.add(zz);
        wr[en - 1] = wr[na - 1];
        if (zz.isZero() == false) {
          wr[en - 1] = x.subtract(w.divide(zz));
        }
        wi[na - 1] = unit.createZero();
        wi[en - 1] = unit.createZero();

        x = h[en - 1][na - 1];
        s = x.abs().add(zz.abs());
        p = x.divide(s);
        q = zz.divide(s);
        r = (p.multiply(p).add(q.multiply(q))).sqrt();
        p = p.divide(r);
        q = q.divide(r);

        /*
         * Row modification
         */
        for (int j = na; j <= size; j++) {
          zz = h[na - 1][j - 1];
          h[na - 1][j - 1] = q.multiply(zz).add(p.multiply(h[en - 1][j - 1]));
          h[en - 1][j - 1] = q.multiply(h[en - 1][j - 1]).subtract(p.multiply(zz));
        }

        /*
         * Colume modification
         */
        for (int i = 1; i <= en; i++) {
          zz = h[i - 1][na - 1];
          h[i - 1][na - 1] = q.multiply(zz).add(p.multiply(h[i - 1][en - 1]));
          h[i - 1][en - 1] = q.multiply(h[i - 1][en - 1]).subtract(p.multiply(zz));
        }

        /*
         * Accumulate transformation
         */
        for (int i = low; i <= igh; i++) {
          zz = z[i - 1][na - 1];
          z[i - 1][na - 1] = q.multiply(zz).add(p.multiply(z[i - 1][en - 1]));
          z[i - 1][en - 1] = q.multiply(z[i - 1][en - 1]).subtract(p.multiply(zz));
        }
      } else {
        /*
         * Ccomplex pair
         */
        wr[na - 1] = x.add(p);
        wr[en - 1] = x.add(p);
        wi[na - 1] = zz;
        wi[en - 1] = zz.unaryMinus();
      }
      en = enm2;
    } while (true); // goto L60;

    /*
     * All roots found. Backsubstitute to find vectors of upper triangular form
     */

    // L340:
    if (norm.isZero()) {
      return 0;
    }

    if (schur == true) {
      return 0;
    }

    for (en = size; en >= 1; en--) {
      p = wr[en - 1]; // 800
      q = wi[en - 1];
      int na = en - 1;
      boolean goto710 = false;

      if (q.isLessThan(unit.createZero())) {
        goto710 = true; // goto L710;
      } else if (q.isGreaterThan(unit.createZero())) {
        continue; // goto L800; to 800
      }
      // else if (q == 0.0); //goto L600;

      /*
       * Real vector
       */

      // L600:
      if (!goto710) {
        int m = en;
        h[en - 1][en - 1] = unit.createUnit();
        for (int i = na; i >= 1; i--) {
          w = h[i - 1][i - 1].subtract(p);
          r = h[i - 1][en - 1];
          if (m <= na) {
            for (int j = m; j <= na; j++) {
              r = r.add(h[i - 1][j - 1].multiply(h[j - 1][en - 1]));
            }
          }

          if (wi[i - 1].isLessThan(unit.createZero())) {
            zz = w;
            s = r;
          } else if (wi[i - 1].isZero()) {
            m = i;
            t = w;
            if (w.isZero()) {
              t = meps.multiply(norm);
            }
            h[i - 1][en - 1] = r.divide(t).unaryMinus();
          } else {

            /*
             * Solve real equations
             */
            m = i;
            x = h[i - 1][i];
            y = h[i][i - 1];
            q = wr[i - 1].subtract(p).multiply(wr[i - 1].subtract(p)).add(wi[i - 1].multiply(wi[i - 1]));
            t = x.multiply(s).subtract(zz.multiply(r)).divide(q);
            h[i - 1][en - 1] = t;
            if (x.abs().isGreaterThan(zz.abs())) {
              h[i][en - 1] = r.unaryMinus().subtract(w.multiply(t)).divide(x);
            } else {
              h[i][en - 1] = s.unaryMinus().subtract(y.multiply(t)).divide(zz);
            }
          }
        }
        continue; // goto L800;

        /*
         * Complex vector
         */
      }
      goto710 = false;
      // L710:
      int m = na;
      if (h[en - 1][na - 1].abs().isGreaterThan(h[na - 1][en - 1].abs())) {
        h[na - 1][na - 1] = q.divide(h[en - 1][na - 1]);
        h[na - 1][en - 1] = h[en - 1][en - 1].subtract(p).unaryMinus().divide(h[en - 1][na - 1]);
      } else {
        S xrr = h[na - 1][na - 1].subtract(p).multiply(h[na - 1][na - 1].subtract(p)).add(q.multiply(q));
        S xzr = q.unaryMinus().multiply(h[na - 1][en - 1]);
        S xzi = h[na - 1][en - 1].unaryMinus().multiply(h[na - 1][na - 1].subtract(p));
        h[na - 1][na - 1] = xzr.divide(xrr);
        h[na - 1][en - 1] = xzi.divide(xrr);
      }
      h[en - 1][na - 1] = unit.createZero();
      h[en - 1][en - 1] = unit.createUnit();
      int enm2 = na - 1;
      for (int i = enm2; i >= 1; i--) {
        w = h[i - 1][i - 1].subtract(p);
        S ra = unit.createZero();
        S sa = h[i - 1][en - 1];
        for (int j = m; j <= na; j++) {
          ra = ra.add(h[i - 1][j - 1].multiply(h[j - 1][na - 1]));
          sa = sa.add(h[i - 1][j - 1].multiply(h[j - 1][en - 1]));
        }

        if (wi[i - 1].isLessThan(unit.createZero())) {
          zz = w;
          r = ra;
          s = sa;
        } else if (wi[i - 1].isZero()) {
          m = i;
          S xrr = w.multiply(w).add(q.multiply(q));
          h[i - 1][na - 1] = ra.multiply(w).add(sa.multiply(q)).unaryMinus().divide(xrr);
          h[i - 1][en - 1] = (ra.multiply(q).subtract(sa.multiply(w))).divide(xrr);
        } else {
          /*
           * Solve complex equation
           */
          m = i;
          x = h[i - 1][i];
          y = h[i][i - 1];
          /*
           * #ifdef MACINTOSH vr = (wr[i - 1]-p)*(wr[i - 1]-p); vr += wi[i -
           * 1]*wi[i - 1] - q*q; #else
           */
          S vr = wr[i - 1].subtract(p).multiply(wr[i - 1].subtract(p)).add(wi[i - 1].multiply(wi[i - 1])).subtract(q.multiply(q));
          S vi = wr[i - 1].subtract(p).multiply(2).multiply(q);
          if (vr.isZero() && vi.isZero()) {
            vr = meps.multiply(norm).multiply(w.abs().add(q.abs()).add(x.abs()).add(y.abs()).add(zz.abs()));
          }
          S xzr = x.multiply(r).subtract(zz.multiply(ra)).add(q.multiply(sa));
          S xzi = x.multiply(s).subtract(zz.multiply(sa)).subtract(q.multiply(ra));
          S xrr = vr.multiply(vr).add(vi.multiply(vi));
          h[i - 1][na - 1] = xzr.multiply(vr).add(xzi.multiply(vi)).divide(xrr);
          h[i - 1][en - 1] = xzr.unaryMinus().multiply(vi).add(xzi.multiply(vr)).divide(xrr);
          if (x.abs().isGreaterThan(zz.abs().add(q.abs()))) {
            h[i][na - 1] = ra.unaryMinus().subtract(w.multiply(h[i - 1][na - 1])).add(q.multiply(h[i - 1][en - 1])).divide(x);
            h[i][en - 1] = sa.unaryMinus().subtract(w.multiply(h[i - 1][en - 1])).subtract(q.multiply(h[i - 1][na - 1])).divide(x);
          } else {
            xzr = r.unaryMinus().subtract(y.multiply(h[i - 1][na - 1]));
            xzi = s.unaryMinus().subtract(y.multiply(h[i - 1][en - 1]));
            xrr = zz.multiply(zz).add(q.multiply(q));
            h[i][na - 1] = xzr.multiply(zz).add(xzi.multiply(q)).divide(xrr);
            h[i][en - 1] = xzr.unaryMinus().multiply(q).add(xzi.multiply(zz)).divide(xrr);
          }
        }
      }
      // L800:
      continue;
    }

    /*
     * End back substitution. Vectors of isolated root.
     */
    for (int i = 1; i <= size; i++) {
      if (i < low || i > igh) {
        for (int j = i; j <= size; j++) {
          z[i - 1][j - 1] = h[i - 1][j - 1];
        }
      }
    }

    /*
     * Multiply by transformation matrix to give vectors of original full matrix
     */
    for (int j = size; j >= low; j--) {
      int m = j > igh ? igh : j;
      for (int i = low; i <= igh; i++) {
        zz = unit.createZero();
        for (k = low; k <= m; k++) {
          zz = zz.add(z[i - 1][k - 1].multiply(h[k - 1][j - 1]));
        }
        z[i - 1][j - 1] = zz;
      }
    }

    /*
     * Set error -- No convergence to an eigenvalue after 30 iterations
     */
    return 0;
  }

  /**
   * 実行列の成分をバランス化し、固有値をできる限り分離します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param a 入力:対象となる、出力:バランス化した行列
   * @param low 開始番号
   * @param igh 終了番号
   * @param scale スケーリング情報
   */
  static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> void balance(final S[][] a, final int[] low, final int[] igh, final S[] scale) {
    S unit = a[0][0].createUnit();

    /** 基数 */
    final S radix = unit.createUnit().multiply(2);
    /** 基数の2乗 */
    final S b2 = radix.multiply(radix);

    int iexc = 0; // ////////////
    int m = 0;
    int j = 0;

    int n = a.length;
    int k = 1;
    int l = n;

    boolean goto100 = true;
    boolean goto140 = false;

    /*
     * In-line procedure for row and column exchange.
     */

    // L20
    do {
      boolean goto20 = false;

      if (goto100 == false) {
        scale[m - 1] = unit.createUnit().multiply(j);
        if (j != m) {
          for (int i = 0; i < l; i++) {
            S tmp = a[i][j - 1];
            a[i][j - 1] = a[i][m - 1];
            a[i][m - 1] = tmp;
          }

          S[] aj = a[j - 1];
          S[] am = a[m - 1];
          for (int i = k - 1; i < n; i++) {
            S tmp = a[j - 1][i];
            aj[i] = a[m - 1][i];
            am[i] = tmp;
          }
        }
        if (iexc == 2) {
          k++;
          goto140 = true; // goto L140;
        }

        if (goto140 == false) {
          /*
           * Search for rows isolating an eigenvalue and push them down.
           * 
           * ***** RETURN FROM THE METHOD *****
           */
          if (l == 1) { // go to L280
            low[0] = k;
            igh[0] = l;
            return;
          }
          l--;
        }
      }
      // L100:
      goto100 = false;
      if (goto140 == false) {

        for (j = l; j >= 1; j--) {
          boolean goto120 = false;
          S[] aj = a[j - 1];
          for (int i = 1; i <= l; i++) {
            if (i != j && aj[i - 1].isZero() == false) {
              goto120 = true;
              break;
            }
          }
          if (goto120 == true) {
            continue;
          }

          m = l;
          iexc = 1;
          goto20 = true;
          break;
          // L120:
        }
        if (goto20) {
          continue;
        }

        /*
         * Search for columns isolating an eigenvalue and push them down.
         */
      }
      // L140:
      goto140 = false;
      for (j = k; j <= l; j++) {
        boolean goto170 = false; // 170
        for (int i = k; i <= l; i++) {
          if (i != j && a[i - 1][j - 1].isZero() == false) {
            goto170 = true; // to170
            break;
          }
        }
        if (goto170 == true) {
          continue;
        }

        m = k;
        iexc = 2;
        goto20 = true;
        break;
        // L170:
      }
      if (goto20) {
        continue;
      }

      if (goto20 == false) {
        break;
      }
    } while (true);

    /*
     * Now balance the submatrix in rows k to l
     */
    for (int i = k - 1; i < l; i++) {
      scale[i] = unit.createUnit();
    }

    /*
     * Iterative loop for norm reduction
     */
    // L190
    boolean noconv;
    do {
      noconv = false;
      for (int i = k; i <= l; i++) {
        S r = unit.createZero();
        S c = unit.createZero();
        S[] ai = a[i - 1];
        for (j = k; j <= l; j++) {
          if (j != i) {
            c = c.add(a[j - 1][i - 1].abs());
            r = r.add(ai[j - 1].abs());
          }
        }

        if (c.multiply(r).isZero() == false) {
          S g = r.divide(radix);
          S f = unit.createUnit();
          S s = c.add(r);
          while (c.isLessThan(g)) {
            f = f.multiply(radix);
            c = c.multiply(b2);
          }
          g = r.multiply(radix);
          while (c.isGreaterThanOrEquals(g)) {
            f = f.divide(radix);
            c = c.divide(b2);
          }
          /*
           * Now balanc
           */
          S z95 = unit.createUnit().multiply(95).divide(100);

          if ((c.add(r).divide(f)).isLessThan(s.multiply(z95))) {
            g = unit.createUnit().divide(f);
            scale[i - 1] = scale[i - 1].multiply(f);
            noconv = true;
            for (j = k; j <= n; j++) {
              ai[j - 1] = ai[j - 1].multiply(g);
            }
            for (j = 1; j <= l; j++) {
              a[j - 1][i - 1] = a[j - 1][i - 1].multiply(f);
            }
          }
        }
      }
    } while (noconv);

    low[0] = k;
    igh[0] = l;
  }

  /**
   * balanceによってバランス化された行列の固有ベクトルを逆変換します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param low 開始番号
   * @param igh 終了番号
   * @param scale スケーリング情報
   * @param m 逆変換する固有ベクトルの数
   * @param z 入力:対象となる固有ベクトル、出力:逆変換された固有ベクトル
   */
  static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> void balbak(final int low, final int igh, final S[] scale, final int m, final S[][] z) {
    int n = z.length;

    if (m == 0) {
      return;
    }

    if (igh != low) {
      for (int i = low; i <= igh; i++) {
        S s = scale[i - 1];
        S[] zi = z[i - 1];
        for (int j = 1; j <= m; j++) {
          zi[j - 1] = zi[j - 1].multiply(s);
        }
      }
    }
    for (int ii = 1; ii <= n; ii++) {
      int i = ii;

      if (i >= low && i <= igh) {
        continue;
      }

      if (i < low) {
        i = low - ii;
      }

      int k = (int)Double.parseDouble(scale[i - 1].round().toString());
      if (k != i) {
        S[] zi = z[i - 1];
        S[] zk = z[k - 1];
        for (int j = 1; j <= m; j++) {
          S tmp = zi[j - 1];
          zi[j - 1] = zk[j - 1];
          zk[j - 1] = tmp;
        }
      }
    }
  }

  /**
   * 上ヘッセンベルグ行列の固有値を求めます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param h 上ヘッセンベルグ行列
   * @param low 開始番号
   * @param igh 終了番号
   * @param wr 固有値の実部
   * @param wi 固有値の虚部
   * @return 計算結果
   */
  @SuppressWarnings({"null"})
  static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> int hqr(final S[][] h, final int low, final int igh, final S[] wr, final S[] wi) {
    S unit = h[0][0].createUnit();

    S p = unit.createZero();
    S q = unit.createZero();
    S r = unit.createZero();

    S x;

    int n = h.length;
    S y = unit.createZero();
    S w = unit.createZero();

    S norm = unit.createZero();
    int k = 1;

    /*
     * Store roots isolated by balance() and compute matrix norm.
     */
    for (int i = 1 - 1; i < n; i++) {
      S[] hi = h[i];
      for (int j = k - 1; j < n; j++) {
        norm = norm.add(hi[j].abs());
      }

      k = i + 1;
      if (i < low || i + 1 > igh) {
        wr[i] = hi[i];
        wi[i] = unit.createZero();
      }
    }
    int en = igh;
    S t = unit.createZero();
    int itn = 100 * n;

    /*
     * Serch for next eigenvalue
     */
    // L60:
    do {
      if (en < low) {
        return 0;
      }

      int its = 0;
      int na = en - 1;
      int enm2 = na - 1;
      /*
       * Look for single small sub-diagonal element.
       */
      // L70:
      boolean goto280 = false;
      do {
        int l;
        for (l = en - 1; l >= low - 1; l--) {
          int lm = l - 1;

          if (l + 1 == low) {
            break;
          }

          S s = h[lm][lm].abs().add(h[l][l].abs());
          if (s.isZero()) {
            s = norm;
          }
          if (h[l][lm].abs().isLessThanOrEquals(unit.getMachineEpsilon().multiply(s))) {
            break;
          }
        }
        l = l + 1;

        /*
         * Form shift
         */
        x = h[en - 1][en - 1];
        if (l == en) {
          break; // goto L270;
        }
        y = h[na - 1][na - 1];
        w = h[en - 1][na - 1].multiply(h[na - 1][en - 1]);
        if (l == na) {
          goto280 = true; // goto L280;
          break;
        }
        if (itn == 0) {
          return en;
        }

        /*
         * Form exceptional shift.
         */
        if (its == 10 || its == 20) {
          t = t.add(x);
          for (int i = low - 1; i < en; i++) {
            h[i][i] = h[i][i].subtract(x);
          }

          S s = h[en - 1][na - 1].abs().add(h[na - 1][enm2 - 1].abs());
          S z075 = unit.multiply(75).divide(100);
          x = z075.multiply(s);
          y = x.clone();
          S z4375 = unit.multiply(4375).divide(10000);

          w = z4375.unaryMinus().multiply(s).multiply(s);
        }
        its++;
        itn--;

        /*
         * Look for two consecutive small sub-diagonal elements
         */
        int m;
        for (m = enm2; m >= l; m--) {
          int m1 = m - 1;

          S zz = h[m1][m1];
          r = x.subtract(zz);
          S s = y.subtract(zz);
          p = r.multiply(s).subtract(w).divide(h[m][m1]).add(h[m1][m]);
          q = h[m][m].subtract(zz).subtract(r).subtract(s);
          r = h[m + 1][m];
          s = p.abs().add(q.abs()).add(r.abs());
          p = p.divide(s);
          q = q.divide(s);
          r = r.divide(s);
          if (m == l) {
            break;
          }
          if (h[m1][m - 2].abs().multiply(q.abs().add(r.abs())).isLessThanOrEquals(unit.getMachineEpsilon().multiply(p.abs())
              .multiply((h[m - 2][m - 2].abs().add(zz.abs()).add(h[m][m].abs()))))) {
            break;
          }
        }

        int mp2 = m + 2;
        for (int i = mp2 - 1; i < en; i++) {
          h[i][i - 2] = unit.createZero();
          if (i + 1 != mp2) {
            h[i][i - 3] = unit.createZero();
          }
        }

        /*
         * Double QR step involving rows l to en and columns m to en
         */
        for (k = m; k <= na; k++) { // 260
          int kp1 = k + 1;
          int km1 = k - 1;

          boolean notlas = (k != na ? true : false);
          if (k != m) {
            p = h[k - 1][k - 2];
            q = h[k][k - 2];
            r = unit.createZero();
            if (notlas) {
              r = h[k + 1][k - 2];
            }
            x = p.abs().add(q.abs()).add(r.abs());
            if (x.isZero()) {
              continue; // goto 260;
            }
            p = p.divide(x);
            q = q.divide(x);
            r = r.divide(x);
          }
          S a = (p.multiply(p).add(q.multiply(q)).add(r.multiply(r))).sqrt();
          S b = p;
          S s = b.isGreaterThanOrEquals(unit.createZero()) ? a.abs() : a.abs().unaryMinus();
          if (k != m) {
            h[k - 1][k - 2] = s.unaryMinus().multiply(x); // /10/16/13:00
          } else if (l != m) {
            h[k - 1][k - 2] = h[k - 1][k - 2].unaryMinus();
          }
          p = p.add(s);
          x = p.divide(s);
          y = q.divide(s);
          S zz = r.divide(s);
          q = q.divide(p);
          r = r.divide(p);

          /*
           * Row modification
           */
          int j;
          S[] hkm = h[k - 1];
          S[] hk = h[k];
          S[] hkp = null;
          if (notlas) {
            hkp = h[k + 1];
          }
          for (j = k - 1; j < en; j++) {
            p = hkm[j].add(q.multiply(hk[j]));
            if (notlas) {
              p = p.add(r.multiply(hkp[j]));
              hkp[j] = hkp[j].subtract(p.multiply(zz));
            }
            hk[j] = hk[j].subtract(p.multiply(y));
            hkm[j] = hkm[j].subtract(p.multiply(x));
          }
          j = j + 1;

          j = en > (k + 3) ? (k + 3) : en;

          /*
           * Column modification
           */
          for (int i = l - 1; i < j; i++) {
            p = x.multiply(h[i][km1]).add(y.multiply(h[i][k]));
            if (notlas) {
              p = p.add(zz.multiply(h[i][kp1]));
              h[i][kp1] = h[i][kp1].subtract(p.multiply(r));
            }
            h[i][k] = h[i][k].subtract(p.multiply(q));
            h[i][km1] = h[i][km1].subtract(p);
          }
          // L260: continue;
        }
      } while (true); // goto L70;

      if (goto280 == false) {
        /*
         * One root found
         */
        // L270:
        wr[en - 1] = x.add(t);
        wi[en - 1] = unit.createZero();
        en = na;
        continue; // goto L60;
      }
      goto280 = false;

      /*
       * Two roots found
       */
      // L280:
      p = y.subtract(x).divide(2);
      q = p.multiply(p).add(w);
      S zz = q.abs().sqrt();
      x = x.add(t);

      /*
       * Real pair
       */
      if (q.isGreaterThanOrEquals(unit.createZero())) {
        S a = zz;
        zz = p.add(p.isGreaterThanOrEquals(unit.createZero()) ? a.abs() : a.abs().unaryMinus());
        wr[na - 1] = x.add(zz);
        wr[en - 1] = wr[na - 1];
        if (zz.isZero() == false) {
          wr[en - 1] = x.subtract(w.divide(zz));
        }
        wi[na - 1] = unit.createZero();
        wi[en - 1] = unit.createZero();
      } else {
        /*
         * Ccomplex pair
         */
        wr[na - 1] = x.add(p);
        wr[en - 1] = x.add(p);
        wi[na - 1] = zz.clone();
        wi[en - 1] = zz.unaryMinus();
      }
      /*
       * Set error -- No convergence to an eigenvalue after 30 iterrations.
       */
      en = enm2;
    } while (true); // goto L60;
  }

  /**
   * ベクトルを正規化します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param valr 固有値の実部
   * @param vali 固有値の虚部
   * @param vecr 固有ベクトルの実部
   * @param veci 固有ベクトルの虚部
   */
  static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> void normalizeVector(final S[] valr, final S[] vali, final S[][] vecr, final S[][] veci) {
    int size = valr.length;

    S scalar = valr[0].clone();

    for (int i = 1; i <= size; i++) {
      if (vali[i - 1].isZero()) {
        S dd = scalar.createZero();
        for (int j = 1; j <= size; j++) {
          S vv = vecr[j - 1][i - 1];
          dd = dd.add(vv.multiply(vv));
        }
        dd = dd.sqrt();
        for (int j = 1; j <= size; j++) {
          vecr[j - 1][i - 1] = vecr[j - 1][i - 1].divide(dd);
        }
      } else {
        S dd = scalar.createZero();
        for (int j = 1; j <= size; j++) {
          S vvr = vecr[j - 1][i - 1];
          S vvi = veci[j - 1][i - 1];
          dd = dd.add(vvr.multiply(vvr).add(vvi.multiply(vvi)));
        }
        dd = dd.sqrt();

        for (int j = 1; j <= size; j++) {
          vecr[j - 1][i - 1] = vecr[j - 1][i - 1].divide(dd);
          veci[j - 1][i - 1] = veci[j - 1][i - 1].divide(dd);
        }
      }
    }
  }

}