/*
 * $Id: RealGeneralizedEigenUtil.java,v 1.4 2008/03/15 00:23:43 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 実行列の一般化固有値を求めるためのユーティリティクラスです。
 * 
 * @author koga
 * @version $Revision: 1.4 $
 */
final class RealGeneralizedEigenSolverUtil {
  /**
   * 新しく生成された<code>RealGeneralizedEigenSolverUtil</code>オブジェクトを初期化します。
   */
  private RealGeneralizedEigenSolverUtil() {
    // nothing to do
  }

  /**
   * 一般の実行列aとbの一般化固有値問題(a x = lambda b x)を等価な問題 (aが上ヘッセンベルグ行列、bが上三角行列)に変換します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param a 対象となる行列
   * @param b 対象となる行列
   * @param q QZ分解のQ
   * @param z QZ分解のZ
   * @param zDesired z行列を求めるならばtrue
   * @param qzDecompositionDesired QZ分解を求めるならばtrue
   */
  protected static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> void qzhes(final S[][] a, final S[][] b, final S[][] q, final S[][] z, final boolean zDesired, final boolean qzDecompositionDesired) {
    S unit = a[0][0].createUnit();

    int size = a.length;

    // if (qz == true);/////////////// qq = q->elm.r;

    if (zDesired == true) {
      for (int i = 1; i <= size; i++) {
        for (int j = 1; j <= size; j++) {
          z[i - 1][j - 1] = unit.createZero();
        }
        z[i - 1][i - 1] = unit.createUnit();
      }
    }

    if (qzDecompositionDesired == true) {
      for (int i = 1; i <= size; i++) {
        for (int j = 1; j <= size; j++) {
          q[i - 1][j - 1] = unit.createZero();
        }
        q[i - 1][i - 1] = unit.createUnit();
      }
    }

    /*
     * Reduce B to upper triangular form.
     */
    if (size <= 1) {
      return;
    }

    int nm1 = size - 1;

    for (int l = 1; l <= nm1; l++) {
      int l1 = l + 1;
      S s = unit.createZero();

      for (int i = l1; i <= size; i++) {
        s = s.add(b[i - 1][l - 1].abs());
      }

      /* if (s <= EPS) */
      if (s.isZero()) {
        continue;
      }

      s = s.add(b[l - 1][l - 1].abs());
      S r = unit.createZero();

      for (int i = l; i <= size; i++) {
        b[i - 1][l - 1] = b[i - 1][l - 1].divide(s);
        r = r.add(b[i - 1][l - 1].multiply(b[i - 1][l - 1]));
      }

      r = fsign(r.sqrt(), b[l - 1][l - 1]);
      b[l - 1][l - 1] = b[l - 1][l - 1].add(r);
      S rho = r.multiply(b[l - 1][l - 1]);

      for (int j = l1; j <= size; j++) {
        S t = unit.createZero();
        for (int i = l; i <= size; i++) {
          t = t.add(b[i - 1][l - 1].multiply(b[i - 1][j - 1]));
        }

        t = t.divide(rho.unaryMinus());

        for (int i = l; i <= size; i++) {
          b[i - 1][j - 1] = b[i - 1][j - 1].add(t.multiply(b[i - 1][l - 1]));
        }

      }

      for (int j = 1; j <= size; j++) {
        S t = unit.createZero();
        for (int i = l; i <= size; i++) {
          t = t.add(b[i - 1][l - 1].multiply(a[i - 1][j - 1]));
        }

        t = t.divide(rho.unaryMinus());

        for (int i = l; i <= size; i++) {
          a[i - 1][j - 1] = a[i - 1][j - 1].add(t.multiply(b[i - 1][l - 1]));
        }
      }

      if (qzDecompositionDesired) {
        for (int j = 1; j <= size; j++) {
          S t = unit.createZero();
          for (int i = l; i <= size; i++) {
            t = t.add(b[i - 1][l - 1].multiply(q[i - 1][j - 1]));
          }

          t = t.divide(rho.unaryMinus());

          for (int i = l; i <= size; i++) {
            q[i - 1][j - 1] = q[i - 1][j - 1].add(t.multiply(b[i - 1][l - 1]));
          }
        }
      }

      b[l - 1][l - 1] = s.unaryMinus().multiply(r);

      for (int i = l1; i <= size; i++) {
        b[i - 1][l - 1] = unit.createZero();
      }
    }

    /*
     * Reduce A to upper hessenberg form, while keeping B triangular.
     */
    if (size == 2) {
      return;
    }

    int nm2 = size - 2;

    for (int k = 1; k <= nm2; k++) {
      for (int l = size - 1; l >= k + 1; l--) {
        int l1 = l + 1;

        /*
         * Zero A(l+1,k)
         */
        S s = a[l - 1][k - 1].abs().add(a[l1 - 1][k - 1].abs());

        /* if (s <= EPS) */
        if (s.isZero()) {
          continue;
        }

        S u1 = a[l - 1][k - 1].divide(s);
        S u2 = a[l1 - 1][k - 1].divide(s);
        S r = fsign((u1.multiply(u1).add(u2.multiply(u2))).sqrt(), u1);
        S v1 = u1.add(r).unaryMinus().divide(r);
        S v2 = u2.unaryMinus().divide(r);
        u2 = v2.divide(v1);

        for (int j = k; j <= size; j++) {
          S t = a[l - 1][j - 1].add(u2.multiply(a[l1 - 1][j - 1]));
          a[l - 1][j - 1] = a[l - 1][j - 1].add(t.multiply(v1));
          a[l1 - 1][j - 1] = a[l1 - 1][j - 1].add(t.multiply(v2));
        }

        a[l1 - 1][k - 1] = unit.createZero();

        if (qzDecompositionDesired) {
          for (int j = 1; j <= size; j++) {
            S t = q[l - 1][j - 1].add(u2.multiply(q[l1 - 1][j - 1]));
            q[l - 1][j - 1] = q[l - 1][j - 1].add(t.multiply(v1));
            q[l1 - 1][j - 1] = q[l1 - 1][j - 1].add(t.multiply(v2));
          }
        }

        for (int j = l; j <= size; j++) {
          S t = b[l - 1][j - 1].add(u2.multiply(b[l1 - 1][j - 1]));
          b[l - 1][j - 1] = b[l - 1][j - 1].add(t.multiply(v1));
          b[l1 - 1][j - 1] = b[l1 - 1][j - 1].add(t.multiply(v2));
        }

        /*
         * Zero B(l+1,l)
         */
        s = b[l1 - 1][l1 - 1].abs().add(b[l1 - 1][l - 1].abs());

        /* if (s <= EPS) */
        if (s.isZero()) {
          continue;
        }

        u1 = b[l1 - 1][l1 - 1].divide(s);
        u2 = b[l1 - 1][l - 1].divide(s);
        r = fsign((u1.multiply(u1).add(u2.multiply(u2))).sqrt(), u1);
        v1 = u1.add(r).unaryMinus().divide(r);
        v2 = u2.unaryMinus().divide(r);
        u2 = v2.divide(v1);

        for (int i = 1; i <= l1; i++) {
          S t = b[i - 1][l1 - 1].add(u2.multiply(b[i - 1][l - 1]));
          b[i - 1][l1 - 1] = b[i - 1][l1 - 1].add(t.multiply(v1));
          b[i - 1][l - 1] = b[i - 1][l - 1].add(t.multiply(v2));
        }

        b[l1 - 1][l - 1] = unit.createZero();

        for (int i = 1; i <= size; i++) {
          S t = a[i - 1][l1 - 1].add(u2.multiply(a[i - 1][l - 1]));
          a[i - 1][l1 - 1] = a[i - 1][l1 - 1].add(t.multiply(v1));
          a[i - 1][l - 1] = a[i - 1][l - 1].add(t.multiply(v2));
        }

        if (zDesired) {
          for (int i = 1; i <= size; i++) {
            S t = z[i - 1][l1 - 1].add(u2.multiply(z[i - 1][l - 1]));
            z[i - 1][l1 - 1] = z[i - 1][l1 - 1].add(t.multiply(v1));
            z[i - 1][l - 1] = z[i - 1][l - 1].add(t.multiply(v2));
          }
        }
      }
    }
  }

  /**
   * 一般化固有値問題(a x = lambda b x)(aが上ヘッセンベルグ行列、bが上三角行列)を 等価な問題(aが擬似上三角行列、bが上三角行列)へ変換します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param a 上ヘッセンベルグ行列
   * @param b 上三角行列
   * @param q QZ分解のQ行列
   * @param z QZ分解のZ行列
   * @param tolerance 許容誤差
   * @param zDesired QZ分解のZを求めるならばtrue
   * @param qzDecompositionDesired QZ分解を求めるならばtrue
   * @return 計算の結果
   */
  protected static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> int qzit(final S[][] a, final S[][] b, final S[][] q, final S[][] z, final S tolerance, final boolean zDesired, final boolean qzDecompositionDesired) {
    int l = 0, k1 = 0, k2 = 0, ld = 0, ll = 0, l1 = 0;
    int na = 0;
    int ish = 0, its = 0, itn = 0, km1 = 0, lm1 = 0, enm2 = 0;

    S unit = a[0][0].createUnit();

    S a1 = unit.createZero();
    S a2 = unit.createZero();
    S a3 = unit.createZero();
    S sh = unit.createZero();

    S a11 = unit.createZero();
    S a12 = unit.createZero();
    S a21 = unit.createZero();
    S a22 = unit.createZero();
    S a33 = unit.createZero();
    S a34 = unit.createZero();
    S a43 = unit.createZero();
    S a44 = unit.createZero();

    S b11 = unit.createZero();
    S b12 = unit.createZero();
    S b22 = unit.createZero();
    S b33 = unit.createZero();
    S b34 = unit.createZero();
    S b44 = unit.createZero();

    int size = a[0].length;

    int ierr = 0;

    /*
     * Compute epsa, epsb
     */
    S anorm = unit.createZero();
    S bnorm = unit.createZero();

    for (int i = 1; i <= size; i++) {
      S ani = unit.createZero();
      if (i != 1) {
        ani = a[i - 1][i - 2].abs();
      }
      S bni = unit.createZero();

      for (int j = 1; j <= size; j++) {
        ani = ani.add(a[i - 1][j - 1].abs());
        bni = bni.add(b[i - 1][j - 1].abs());
      }

      if (ani.isGreaterThan(anorm)) {
        anorm = ani.clone();
      }
      if (bni.isGreaterThan(bnorm)) {
        bnorm = bni.clone();
      }
    }

    /* if (anorm <= EPS) anorm = 1.0; */
    if (anorm.isZero()) {
      anorm = unit.createUnit();
    }
    /* if (bnorm <= EPS) bnorm = 1.0; */
    if (bnorm.isZero()) {
      bnorm = unit.createUnit();
    }

    S ep = tolerance.clone();

    /*
     * Compute roundoff level if eps1 is zero
     */
    // if (ep > 0.0) goto L50;
    if (ep.isLessThanOrEquals(unit.createZero())) {

      ep = unit.createUnit();
      do {
        ep = ep.divide(2);
      } while (ep.add(1).isGreaterThan(1));
    }

    // L50:
    S epsa = ep.multiply(anorm);
    S epsb = ep.multiply(bnorm);

    /*
     * Reduce A to quasi-traiangular form, while keeping B traiangular.
     */
    int lor1 = 1;
    int enorn = size;
    int en = size;

    /*
     * Begin QZ Step
     */

    // L60:
    int gotoflag = 0;
    do {

      if (gotoflag < 70) {
        if (en <= 2) {
          // goto L1001;
          if (size > 1) {
            b[size - 1][0] = epsb.clone();
          }
          return ierr;
        }

        if (!zDesired) {
          enorn = en;
        }

        its = 0;
        itn = 100 * size;
        na = en - 1;
        enm2 = na - 1;
      }

      // L70:
      if (gotoflag == 70) {
        gotoflag = 0;
      }

      if (gotoflag < 90) {
        ish = 2;

        /*
         * Check for convergence or reducibility. for l=en step -1 until 1 do --
         */
        for (ll = 1; ll <= en; ll++) {
          lm1 = en - ll;
          l = lm1 + 1;

          if (l == 1) { // goto L95;
            gotoflag = 95;
            break;
          }

          if (a[l - 1][lm1 - 1].abs().isLessThanOrEquals(epsa)) {
            break;
          }

        }
      }
      // L90:
      if (gotoflag == 90) {
        gotoflag = 0;
      }

      if (gotoflag < 95) {
        a[l - 1][lm1 - 1] = unit.createZero();

        if (l >= na) {
          /*
           * 1-by-1 or 2-by-2 block isolated.
           */
          en = lm1;
          gotoflag = 0; // goto L60; //60 -> 0 ok
          continue;
        }
      }

      /*
       * Check for small top of B.
       */
      // L95:
      if (gotoflag == 95) {
        gotoflag = 0;
      }

      if (gotoflag < 100) {
        ld = l;
      }
      // L100:
      if (gotoflag == 100) {
        gotoflag = 0;
      }
      if (gotoflag < 120) {
        l1 = l + 1;
        b11 = b[l - 1][l - 1].clone();

        if (b11.abs().isGreaterThan(epsb)) { // goto L120;
          gotoflag = 120;
        } else {
          b[l - 1][l - 1] = unit.createZero();
          S s = a[l - 1][l - 1].abs().add(a[l1 - 1][l - 1].abs());
          S u1 = a[l - 1][l - 1].divide(s);
          S u2 = a[l1 - 1][l - 1].divide(s);
          S r = fsign((u1.multiply(u1).add(u2.multiply(u2))).sqrt(), u1);
          S v1 = u1.add(r).unaryMinus().divide(r);
          S v2 = u2.unaryMinus().divide(r);
          u2 = v2.divide(v1);

          for (int j = l; j <= enorn; j++) {
            S t = a[l - 1][j - 1].add(u2.multiply(a[l1 - 1][j - 1]));
            a[l - 1][j - 1] = a[l - 1][j - 1].add(t.multiply(v1));
            a[l1 - 1][j - 1] = a[l1 - 1][j - 1].add(t.multiply(v2));

            t = b[l - 1][j - 1].add(u2.multiply(b[l1 - 1][j - 1]));
            b[l - 1][j - 1] = b[l - 1][j - 1].add(t.multiply(v1));
            b[l1 - 1][j - 1] = b[l1 - 1][j - 1].add(t.multiply(v2));
          }

          if (qzDecompositionDesired) {
            for (int j = 1; j <= size; j++) {
              S t = q[l - 1][j - 1].add(u2.multiply(q[l1 - 1][j - 1]));
              q[l - 1][j - 1] = q[l - 1][j - 1].add(t.multiply(v1));
              q[l1 - 1][j - 1] = q[l1 - 1][j - 1].add(t.multiply(v2));
            }
          }

          if (l != 1) {
            a[l - 1][lm1 - 1] = a[l - 1][lm1 - 1].unaryMinus();
          }

          lm1 = l;
          l = l1;

          gotoflag = 90; // goto L90;
          continue;
        }
      }

      // L120:
      if (gotoflag == 120) {
        gotoflag = 0;
      }
      if (gotoflag < 140) {
        a11 = a[l - 1][l - 1].divide(b11);
        a21 = a[l1 - 1][l - 1].divide(b11);

        if (ish == 1) { // goto L140;
          gotoflag = 140;
        } else {
          /*
           * Iteration strategy
           */
          if (itn == 0) {
            gotoflag = 1000; // goto L1000;
            break;
          }

          if (its != 0 && (its % 10) == 0) {
            gotoflag = 155; // goto L155;
          } else {
            /*
             * Determine type of shift
             */
            b22 = b[l1 - 1][l1 - 1].clone();
            if (b22.abs().isLessThan(epsb)) {
              b22 = epsb.clone();
            }
            b33 = b[na - 1][na - 1];
            if (b33.abs().isLessThan(epsb)) {
              b33 = epsb.clone();
            }
            b44 = b[en - 1][en - 1];
            if (b44.abs().isLessThan(epsb)) {
              b44 = epsb.clone();
            }
            a33 = a[na - 1][na - 1].divide(b33);
            a34 = a[na - 1][en - 1].divide(b44);
            a43 = a[en - 1][na - 1].divide(b33);
            a44 = a[en - 1][en - 1].divide(b44);
            b34 = b[na - 1][en - 1].divide(b44);
            S t = (a43.multiply(b34).subtract(a33).subtract(a44)).divide(2);
            S r = t.multiply(t).add(a34.multiply(a43)).subtract(a33.multiply(a44));

            if (r.isLessThan(unit.createZero())) {
              gotoflag = 150; // goto L150;
            } else {

              /*
               * Determine single shift zeroth column of A.
               */
              ish = 1;
              r = r.sqrt();
              sh = t.unaryMinus().add(r);
              S s = t.unaryMinus().subtract(r);
              if ((s.subtract(a44)).abs().isLessThan((sh.subtract(a44)).abs())) {
                sh = s.clone();
              }

              /*
               * Look for two consecutive small sub-diagonal elements of A. For
               * l=en-2 step -1 until ld do --.
               */
              for (ll = ld; ll <= enm2; ll++) {
                l = enm2 + ld - ll;
                if (l == ld) {
                  break;
                }
                lm1 = l - 1;
                l1 = l + 1;
                t = a[l - 1][l - 1].clone();
                if (b[l - 1][l - 1].abs().isGreaterThan(epsb)) {
                  t = t.subtract(sh.multiply(b[l - 1][l - 1]));
                }

                if (a[l - 1][lm1 - 1].abs().isLessThanOrEquals((t.divide(a[l1 - 1][l - 1])).abs().multiply(epsa))) {
                  gotoflag = 100; // goto L100;
                  break;
                }
              }
              if (gotoflag == 100) {
                continue; // jump do loop
              }
            }
          }
        }
      }
      // L140:
      if (gotoflag == 140) {
        gotoflag = 0;
      }
      if (gotoflag < 150) {
        a1 = a11.subtract(sh);
        a2 = a21.clone();
        if (l != ld) {
          a[l - 1][lm1 - 1] = a[l - 1][lm1 - 1].unaryMinus();
        }

        gotoflag = 160; // goto L160;

        /*
         * Determine double shift zeroth column of A.
         */
      }
      // L150:
      if (gotoflag == 150) {
        gotoflag = 0;
      }
      if (gotoflag < 155) {
        a12 = a[l - 1][l1 - 1].divide(b22);
        a22 = a[l1 - 1][l1 - 1].divide(b22);
        b12 = b[l - 1][l1 - 1].divide(b22);
        a1 = a33.subtract(a11).multiply(a44.subtract(a11)).subtract(a34.multiply(a43)).add(a43.multiply(b34).multiply(a11)).divide(a21).add(a12).subtract(a11.multiply(b12));
        a2 = a22.subtract(a11).subtract(a21.multiply(b12)).subtract(a33.subtract(a11)).subtract(a44.subtract(a11)).add(a43.multiply(b34));
        a3 = a[l1][l1 - 1].divide(b22);
        gotoflag = 160; // goto L160;

        /*
         * AD HOC SHIFT
         */
      }
      // L155:
      if (gotoflag == 155) {
        gotoflag = 0;
      }
      if (gotoflag < 160) {
        a1 = unit.createZero();
        a2 = unit.createUnit();
        S c11605 = unit.multiply(11605).divide(10000);
        a3 = c11605;
      }
      // L160:
      if (gotoflag == 160) {
        gotoflag = 0;
      }
      if (gotoflag < 170) {
        its++;
        itn--;

        if (!zDesired) {
          lor1 = ld;
        }
      }

      /*
       * Main Loop
       */
      boolean notlas;
      for (int k = l; k <= na; k++) {
        if (gotoflag < 170) {
          notlas = ((k != na) && (ish == 2));
          k1 = k + 1;
          k2 = k + 2;
          km1 = Math.max(k - 1, l);
          ll = Math.min(en, k1 + ish);

          if (notlas) {
            gotoflag = 190; // goto L190;
          } else {
            /*
             * Zero A(k+1,k-1)
             */
            // if (k == l) goto L170;
            if (k == l) {
              gotoflag = 170;
            } else {
              a1 = a[k - 1][km1 - 1].clone();
              a2 = a[k1 - 1][km1 - 1].clone();
            }
          }
        }

        // L170:
        if (gotoflag == 170) {
          gotoflag = 0;
        }
        if (gotoflag < 190) {
          S s = a1.abs().add(a2.abs());

          /* if (s <= EPS) goto L70; */
          if (s.isZero()) {
            gotoflag = 70; // goto L70;
            break;
          }

          S u1 = a1.divide(s);
          S u2 = a2.divide(s);
          S r = fsign((u1.multiply(u1).add(u2.multiply(u2))).sqrt(), u1);
          S v1 = (u1.add(r)).unaryMinus().divide(r);
          S v2 = u2.unaryMinus().divide(r);
          u2 = v2.divide(v1);

          for (int j = km1; j <= enorn; j++) {
            S t = a[k - 1][j - 1].add(u2.multiply(a[k1 - 1][j - 1]));
            a[k - 1][j - 1] = a[k - 1][j - 1].add(t.multiply(v1));
            a[k1 - 1][j - 1] = a[k1 - 1][j - 1].add(t.multiply(v2));

            t = b[k - 1][j - 1].add(u2.multiply(b[k1 - 1][j - 1]));
            b[k - 1][j - 1] = b[k - 1][j - 1].add(t.multiply(v1));
            b[k1 - 1][j - 1] = b[k1 - 1][j - 1].add(t.multiply(v2));
          }

          if (qzDecompositionDesired) {
            for (int j = 1; j <= size; j++) {
              S t = q[k - 1][j - 1].add(u2.multiply(q[k1 - 1][j - 1]));
              q[k - 1][j - 1] = q[k - 1][j - 1].add(t.multiply(v1));
              q[k1 - 1][j - 1] = q[k1 - 1][j - 1].add(t.multiply(v2));
            }
          }

          if (k != l) {
            a[k1 - 1][km1 - 1] = unit.createZero();
          }

          gotoflag = 240; // goto L240;

          /*
           * Zero A(k+1,k-1) and A(k+2,k-1)
           */
        }
        // L190:
        if (gotoflag == 190) {
          gotoflag = 0;
        }
        if (gotoflag < 240) {
          if (k != l) {
            a1 = a[k - 1][km1 - 1].clone();
            a2 = a[k1 - 1][km1 - 1].clone();
            a3 = a[k2 - 1][km1 - 1].clone();
          }

          S s = a1.abs().add(a2.abs()).add(a3.abs());

          /* if (s <= EPS) */
          if (s.isZero()) {
            continue;
          }

          S u1 = a1.divide(s);
          S u2 = a2.divide(s);
          S u3 = a3.divide(s);
          S r = fsign((u1.multiply(u1).add(u2.multiply(u2)).add(u3.multiply(u3))).sqrt(), u1);
          S v1 = u1.add(r).unaryMinus().divide(r);
          S v2 = u2.unaryMinus().divide(r);
          S v3 = u3.unaryMinus().divide(r);
          u2 = v2.divide(v1);
          u3 = v3.divide(v1);

          for (int j = km1; j <= enorn; j++) {
            S t = a[k - 1][j - 1].add(u2.multiply(a[k1 - 1][j - 1])).add(u3.multiply(a[k2 - 1][j - 1]));
            a[k - 1][j - 1] = a[k - 1][j - 1].add(t.multiply(v1));
            a[k1 - 1][j - 1] = a[k1 - 1][j - 1].add(t.multiply(v2));
            a[k2 - 1][j - 1] = a[k2 - 1][j - 1].add(t.multiply(v3));

            t = b[k - 1][j - 1].add(u2.multiply(b[k1 - 1][j - 1])).add(u3.multiply(b[k2 - 1][j - 1]));
            b[k - 1][j - 1] = b[k - 1][j - 1].add(t.multiply(v1));
            b[k1 - 1][j - 1] = b[k1 - 1][j - 1].add(t.multiply(v2));
            b[k2 - 1][j - 1] = b[k2 - 1][j - 1].add(t.multiply(v3));
          }

          if (qzDecompositionDesired) {
            for (int j = 1; j <= size; j++) {
              S t = q[k - 1][j - 1].add(u2.multiply(q[k1 - 1][j - 1])).add(u3.multiply(q[k2 - 1][j - 1]));
              q[k - 1][j - 1] = q[k - 1][j - 1].add(t.multiply(v1));
              q[k1 - 1][j - 1] = q[k1 - 1][j - 1].add(t.multiply(v2));
              q[k2 - 1][j - 1] = q[k2 - 1][j - 1].add(t.multiply(v3));
            }
          }

          if (k != l) {
            a[k1 - 1][km1 - 1] = unit.createZero();
            a[k2 - 1][km1 - 1] = unit.createZero();
          }

          /*
           * Zero B(k+2,k+1) and B(k+2,k)
           */
          s = b[k2 - 1][k2 - 1].abs().add(b[k2 - 1][k1 - 1].abs()).add(b[k2 - 1][k - 1].abs());

          /* if (s <= EPS) goto L240; */
          if (s.isZero()) {
            gotoflag = 240; // goto L240;
          } else {

            u1 = b[k2 - 1][k2 - 1].divide(s);
            u2 = b[k2 - 1][k1 - 1].divide(s);
            u3 = b[k2 - 1][k - 1].divide(s);
            r = fsign((u1.multiply(u1).add(u2.multiply(u2)).add(u3.multiply(u3))).sqrt(), u1);
            v1 = u1.add(r).unaryMinus().divide(r);
            v2 = u2.unaryMinus().divide(r);
            v3 = u3.unaryMinus().divide(r);
            u2 = v2.divide(v1);
            u3 = v3.divide(v1);

            for (int i = lor1; i <= ll; i++) {
              S t = a[i - 1][k2 - 1].add(u2.multiply(a[i - 1][k1 - 1])).add(u3.multiply(a[i - 1][k - 1]));
              a[i - 1][k2 - 1] = a[i - 1][k2 - 1].add(t.multiply(v1));
              a[i - 1][k1 - 1] = a[i - 1][k1 - 1].add(t.multiply(v2));
              a[i - 1][k - 1] = a[i - 1][k - 1].add(t.multiply(v3));

              t = b[i - 1][k2 - 1].add(u2.multiply(b[i - 1][k1 - 1])).add(u3.multiply(b[i - 1][k - 1]));
              b[i - 1][k2 - 1] = b[i - 1][k2 - 1].add(t.multiply(v1));
              b[i - 1][k1 - 1] = b[i - 1][k1 - 1].add(t.multiply(v2));
              b[i - 1][k - 1] = b[i - 1][k - 1].add(t.multiply(v3));
            }

            b[k2 - 1][k - 1] = unit.createZero();
            b[k2 - 1][k1 - 1] = unit.createZero();

            if (zDesired) {
              for (int i = 1; i <= size; i++) {
                S t = z[i - 1][k2 - 1].add(u2.multiply(z[i - 1][k1 - 1])).add(u3.multiply(z[i - 1][k - 1]));
                z[i - 1][k2 - 1] = z[i - 1][k2 - 1].add(t.multiply(v1));
                z[i - 1][k1 - 1] = z[i - 1][k1 - 1].add(t.multiply(v2));
                z[i - 1][k - 1] = z[i - 1][k - 1].add(t.multiply(v3));
              }
            }
            // gotoflag = 0; ////
          }

          /*
           * Zero B(k+1,k)
           */
        }

        // L240:
        if (gotoflag == 240) {
          gotoflag = 0;
        }
        S s = b[k1 - 1][k1 - 1].abs().add(b[k1 - 1][k - 1].abs());

        /* if (s <= EPS) */
        if (s.isZero()) {
          continue;
        }

        S u1 = b[k1 - 1][k1 - 1].divide(s);
        S u2 = b[k1 - 1][k - 1].divide(s);
        S r = fsign((u1.multiply(u1).add(u2.multiply(u2))).sqrt(), u1);
        S v1 = u1.add(r).unaryMinus().divide(r);
        S v2 = u2.unaryMinus().divide(r);
        u2 = v2.divide(v1);

        for (int i = lor1; i <= ll; i++) {
          S t = a[i - 1][k1 - 1].add(u2.multiply(a[i - 1][k - 1]));
          a[i - 1][k1 - 1] = a[i - 1][k1 - 1].add(t.multiply(v1));
          a[i - 1][k - 1] = a[i - 1][k - 1].add(t.multiply(v2));

          t = b[i - 1][k1 - 1].add(u2.multiply(b[i - 1][k - 1]));
          b[i - 1][k1 - 1] = b[i - 1][k1 - 1].add(t.multiply(v1));
          b[i - 1][k - 1] = b[i - 1][k - 1].add(t.multiply(v2));
        }

        b[k1 - 1][k - 1] = unit.createZero();

        if (zDesired) {
          for (int i = 1; i <= size; i++) {
            S t = z[i - 1][k1 - 1].add(u2.multiply(z[i - 1][k - 1]));
            z[i - 1][k1 - 1] = z[i - 1][k1 - 1].add(t.multiply(v1));
            z[i - 1][k - 1] = z[i - 1][k - 1].add(t.multiply(v2));
          }
        }

      } // end of main loop

      /*
       * if(gotoflag != 70) continue;
       */

      /*
       * End QZ Step
       */
      gotoflag = 70; // goto L70;
    } while (true);

    /*
     * Set error -- neither bottom subdiagonal element has become negligible
     * after 50 iterations
     */
    // L1000:
    ierr = en;

    /*
     * Save epsb for use by qzval() and qzvec()
     */
    // L1001:
    if (size > 1) {
      b[size - 1][0] = epsb.clone();
    }

    return ierr;

  }

  /**
   * 一般化固有値問題(a x = lambda b x)(aが擬似上三角行列、bが上三角行列)を 等価な問題(aが非零の対角成分を減らした行列、bが上三角行列)に変換します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param a 擬似上三角行列
   * @param b 上三角行列
   * @param q QZ分解のQ行列
   * @param z QZ分解のZ行列
   * @param alfr aを完全に三角行列へ変換したときの対角成分の実部
   * @param alfi aを完全に三角行列へ変換したときの対角成分の虚部
   * @param beta bを完全に三角行列へ変換したときの対角成分
   * @param zDesired QZ分解のZ行列を求めるならばtrue
   * @param qzDecompositionDesired QZ分解を求めるならばtrue
   */
  protected static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> void qzval(final S[][] a, final S[][] b, final S[][] q, final S[][] z, final S[] alfr, final S[] alfi, final S[] beta, final boolean zDesired, final boolean qzDecompositionDesired) {
    S unit = a[0][0].createUnit();

    S c = unit.createZero();
    S d = unit.createZero();
    S e = unit.createZero();

    S an2 = unit.createZero();
    S a1 = unit.createZero();
    S a2 = unit.createZero();
    S bn2 = unit.createZero();

    S ti = unit.createZero();
    S tr = unit.createZero();

    S a11 = unit.createZero();
    S a12 = unit.createZero();

    S a21 = unit.createZero();
    S a22 = unit.createZero();
    S b11 = unit.createZero();
    S b12 = unit.createZero();
    S b22 = unit.createZero();

    int size = a[0].length;

    S epsb = b[size - 1][0].clone();
    int isw = 1;

    /*
     * Find eigenvalues of eqasi-traiangular matrices.
     */

    boolean goto460 = false;
    boolean goto480 = false;
    boolean goto503 = false;
    for (int en = size; en >= 1; en--) {
      int na = en - 1;

      if (isw == 2) {
        isw = 3 - isw;
        continue;
      }

      /*
       * 1-by-1 block, one real root
       */
      /* if (en == 1 || Math.abs(a[en,na)) <= EPS) { */
      if (en == 1 || a[en - 1][na - 1].isZero()) {
        alfr[en - 1] = a[en - 1][en - 1].clone();
        if (b[en - 1][en - 1].isLessThan(unit.createZero())) {
          alfr[en - 1] = alfr[en - 1].unaryMinus();
        }
        beta[en - 1] = b[en - 1][en - 1].abs();
        alfi[en - 1] = unit.createZero();
        continue;
      }

      /*
       * 2-by-2 block
       */
      // boolean goto460 = false;
      if (b[na - 1][na - 1].abs().isLessThanOrEquals(epsb)) {
        a1 = a[na - 1][na - 1].clone();
        a2 = a[en - 1][na - 1].clone();
        goto460 = true; // goto L460;
      }

      if (!goto460) {
        if (b[en - 1][en - 1].abs().isLessThanOrEquals(epsb)) {
          a1 = a[en - 1][en - 1].clone();
          a2 = a[en - 1][na - 1].clone();
          bn2 = unit.createZero();
        } else {
          an2 = a[na - 1][na - 1].abs().add(a[na - 1][en - 1].abs()).add(a[en - 1][na - 1].abs()).add(a[en - 1][en - 1].abs());
          bn2 = b[na - 1][na - 1].abs().add(b[na - 1][en - 1].abs()).add(b[en - 1][en - 1].abs());
          a11 = a[na - 1][na - 1].divide(an2);
          a12 = a[na - 1][en - 1].divide(an2);
          a21 = a[en - 1][na - 1].divide(an2);
          a22 = a[en - 1][en - 1].divide(an2);
          b11 = b[na - 1][na - 1].divide(bn2);
          b12 = b[na - 1][en - 1].divide(bn2);
          b22 = b[en - 1][en - 1].divide(bn2);
          e = a11.divide(b11);
          S ei = a22.divide(b22);
          S s = a21.divide(b11.multiply(b22));
          S t = (a22.subtract(e.multiply(b22))).divide(b22);

          if (e.abs().isGreaterThan(ei.abs())) {
            e = ei.clone();
            t = (a11.subtract(e.multiply(b11))).divide(b11);
          }

          c = (t.subtract(s.multiply(b12))).divide(2);
          d = c.multiply(c).add(s.multiply((a12.subtract(e.multiply(b12)))));
          // boolean goto480 = false;
          if (d.isLessThan(unit.createZero())) {
            goto480 = true; // goto L480;
          }

          if (!goto480) {
            /*
             * Two real roots. Zero both A(en,na) and B(en,na).
             */
            e = e.add(c.add(fsign(d.sqrt(), c)));
            a11 = a11.subtract(e.multiply(b11));
            a12 = a12.subtract(e.multiply(b12));
            a22 = a22.subtract(e.multiply(b22));

            if (a11.abs().add(a12.abs()).isLessThan(a21.abs().add(a22.abs()))) {
              a1 = a22.clone();
              a2 = a21.clone();
            } else {
              a1 = a12.clone();
              a2 = a11.clone();
            }
          }
        }
      }
      if (!goto480) {
        if (!goto460) {
          /*
           * Choose and apply real Z
           */
          S s = a1.abs().add(a2.abs());
          S u1 = a1.divide(s);
          S u2 = a2.divide(s);
          S r = fsign((u1.multiply(u1).add(u2.multiply(u2))).sqrt(), u1);
          S v1 = (u1.add(r)).unaryMinus().divide(r);
          S v2 = u2.divide(r).unaryMinus();
          u2 = v2.divide(v1);

          for (int i = 1; i <= en; i++) {
            S t = a[i - 1][en - 1].add(u2.multiply(a[i - 1][na - 1]));
            a[i - 1][en - 1] = a[i - 1][en - 1].add(t.multiply(v1));
            a[i - 1][na - 1] = a[i - 1][na - 1].add(t.multiply(v2));

            t = b[i - 1][en - 1].add(u2.multiply(b[i - 1][na - 1]));
            b[i - 1][en - 1] = b[i - 1][en - 1].add(t.multiply(v1));
            b[i - 1][na - 1] = b[i - 1][na - 1].add(t.multiply(v2));
          }

          if (zDesired) {
            for (int i = 1; i <= size; i++) {
              S t = z[i - 1][en - 1].add(u2.multiply(z[i - 1][na - 1]));
              z[i - 1][en - 1] = z[i - 1][en - 1].add(t.multiply(v1));
              z[i - 1][na - 1] = z[i - 1][na - 1].add(t.multiply(v2));
            }
          }
        }

        /* if (Math.abs(bn2) > EPS) { */
        if (bn2.isZero() == false || goto460) {
          if (!goto460) {
            if (an2.isLessThan(e.abs().multiply(bn2))) {
              a1 = a[na - 1][na - 1].clone();
              a2 = a[en - 1][na - 1].clone();
            } else {
              a1 = b[na - 1][na - 1].clone();
              a2 = b[en - 1][na - 1].clone();
            }

            /*
             * Choose and apply real Q
             */
          }
          goto460 = false;

          // L460:
          S s = a1.abs().add(a2.abs());

          /* if (Math.abs(s) > EPS) { */
          if (s.isZero() == false) {
            S u1 = a1.divide(s);
            S u2 = a2.divide(s);
            S r = fsign((u1.multiply(u1).add(u2.multiply(u2))).sqrt(), u1);
            S v1 = (u1.add(r)).unaryMinus().divide(r);
            S v2 = u2.unaryMinus().divide(r);
            u2 = v2.divide(v1);

            for (int j = na; j <= size; j++) {
              S t = a[na - 1][j - 1].add(u2.multiply(a[en - 1][j - 1]));
              a[na - 1][j - 1] = a[na - 1][j - 1].add(t.multiply(v1));
              a[en - 1][j - 1] = a[en - 1][j - 1].add(t.multiply(v2));

              t = b[na - 1][j - 1].add(u2.multiply(b[en - 1][j - 1]));
              b[na - 1][j - 1] = b[na - 1][j - 1].add(t.multiply(v1));
              b[en - 1][j - 1] = b[en - 1][j - 1].add(t.multiply(v2));
            }

            if (qzDecompositionDesired) {
              for (int j = 1; j <= size; j++) {
                S t = q[na - 1][j - 1].add(u2.multiply(q[en - 1][j - 1]));
                q[na - 1][j - 1] = q[na - 1][j - 1].add(t.multiply(v1));
                q[en - 1][j - 1] = q[en - 1][j - 1].add(t.multiply(v2));
              }
            }
          }
        }

        a[en - 1][na - 1] = unit.createZero();
        b[en - 1][na - 1] = unit.createZero();
        alfr[na - 1] = a[na - 1][na - 1].clone();
        alfr[en - 1] = a[en - 1][en - 1].clone();
        if (b[na - 1][na - 1].isLessThan(unit.createZero())) {
          alfr[na - 1] = alfr[na - 1].unaryMinus();
        }
        if (b[en - 1][en - 1].isLessThan(unit.createZero())) {
          alfr[en - 1] = alfr[en - 1].unaryMinus();
        }
        beta[na - 1] = b[na - 1][na - 1].abs();
        beta[en - 1] = b[en - 1][en - 1].abs();
        alfi[en - 1] = unit.createZero();
        alfi[na - 1] = unit.createZero();

        isw = 3 - isw;
        continue;

        /*
         * Two complex roots
         */
      }
      goto480 = false;
      // L480:
      e = e.add(c);
      S ei = (d.unaryMinus()).sqrt();
      S a11r = a11.subtract(e.multiply(b11));
      S a11i = ei.multiply(b11);
      S a12r = a12.subtract(e.multiply(b12));
      S a12i = ei.multiply(b12);
      S a22r = a22.subtract(e.multiply(b22));
      S a22i = ei.multiply(b22);
      
      S a1i;
      S a2i;
      if (a11r.abs().add(a11i.abs()).add(a12r.abs()).add(a12i.abs()).isLessThan(a21.abs().add(a22r.abs()).add(a22i.abs()))) {
        a1 = a22r.clone();
        a1i = a22i.clone();
        a2 = a21.unaryMinus();
        a2i = unit.createZero();
      } else {
        a1 = a12r.clone();
        a1i = a12i.clone();
        a2 = a11r.unaryMinus();
        a2i = a11i.unaryMinus();
      }

      /*
       * Choose complex Z
       */
      S cz = (a1.multiply(a1).add(a1i.multiply(a1i))).sqrt();

      /* if (Math.abs(cz) <= EPS) { */
      S szr;
      S szi;
      if (cz.isZero()) {
        szr = unit.createUnit();
        szi = unit.createZero();
      } else {
        szr = (a1.multiply(a2).add(a1i.multiply(a2i))).divide(cz);
        szi = (a1.multiply(a2i).subtract(a1i.multiply(a2))).divide(cz);
        S r = (cz.multiply(cz).add(szr.multiply(szr)).add(szi.multiply(szi))).sqrt();
        cz = cz.divide(r);
        szr = szr.divide(r);
        szi = szi.divide(r);
      }

      if (an2.isLessThan((e.abs().add(ei)).multiply(bn2))) {
        a1 = cz.multiply(a11).add(szr.multiply(a12));
        a1i = szi.multiply(a12);
        a2 = cz.multiply(a21).add(szr.multiply(a22));
        a2i = szi.multiply(a22);
      } else {
        a1 = cz.multiply(b11).add(szr.multiply(b12));
        a1i = szi.multiply(b12);
        a2 = szr.multiply(b22);
        a2i = szi.multiply(b22);
      }

      /*
       * Choose complex Q
       */
      S cq = (a1.multiply(a1).add(a1i.multiply(a1i))).sqrt();

      /* if (Math.abs(cq) <= EPS) { */
      S sqr;
      S sqi;
      if (cq.isZero()) {
        sqr = unit.createUnit();
        sqi = unit.createZero();
      } else {
        sqr = (a1.multiply(a2).add(a1i.multiply(a2i))).divide(cq);
        sqi = (a1.multiply(a2i).subtract(a1i.multiply(a2))).divide(cq);
        S r = (cq.multiply(cq).add(sqr.multiply(sqr)).add(sqi.multiply(sqi))).sqrt();
        cq = cq.divide(r);
        sqr = sqr.divide(r);
        sqi = sqi.divide(r);
      }

      /*
       * Compute diagonal elements that would result if transformations were
       * applied.
       */
      S ssr = sqr.multiply(szr).add(sqi.multiply(szi));
      S ssi = sqr.multiply(szi).subtract(sqi.multiply(szr));
      int i = 1;
      tr = cq.multiply(cz).multiply(a11).add(cq.multiply(szr).multiply(a12)).add(sqr.multiply(cz).multiply(a21)).add(ssr.multiply(a22));
      ti = cq.multiply(szi).multiply(a12).subtract(sqi.multiply(cz).multiply(a21)).add(ssi.multiply(a22));
      S dr = cq.multiply(cz).multiply(b11).add(cq.multiply(szr).multiply(b12)).add(ssr.multiply(b22));
      S di = cq.multiply(szi).multiply(b12).add(ssi.multiply(b22));

      goto503 = true; // goto L503;

      // L502:
      do {
        if (!goto503) {
          i = 2;
          tr = ssr.multiply(a11).subtract(sqr.multiply(cz).multiply(a12)).subtract(cq.multiply(szr).multiply(a21)).add(cq.multiply(cz).multiply(a22));
          ti = ssi.multiply(a11).unaryMinus().subtract(sqi.multiply(cz).multiply(a12)).add(cq.multiply(szi).multiply(a21));
          dr = ssr.multiply(b11).subtract(sqr.multiply(cz).multiply(b12)).add(cq.multiply(cz).multiply(b22));
          di = ssi.multiply(b11).unaryMinus().subtract(sqi.multiply(cz).multiply(b12));
        }
        goto503 = false;
        // L503:
        S t = ti.multiply(dr).subtract(tr.multiply(di));
        int j = na;
        if (t.isLessThan(unit.createZero())) {
          j = en;
        }
        S r = (dr.multiply(dr).add(di.multiply(di))).sqrt();
        beta[j - 1] = bn2.multiply(r);
        alfr[j - 1] = an2.multiply(tr.multiply(dr).add(ti.multiply(di))).divide(r);
        alfi[j - 1] = an2.multiply(t).divide(r);

      } while (i == 1); // if (i == 1) goto L502;

      isw = 3 - isw;
    }
  }

  /**
   * 一般化固有値問題(a x = lambda b x)(aが擬似上三角行列、bが上三角行列)の 固有ベクトルを求めます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param a 擬似上三角行列
   * @param b 上三角行列
   * @param z 一般化固有ベクトルの実部と虚部
   * @param alfr aを完全に上三角行列に変換したときの対角成分の実部
   * @param alfi aを完全に上三角行列に変換したときの対角成分の虚部
   * @param beta bを上三角行列に変換したときの対角成分
   */
  protected static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> void qzvec(final S[][] a, final S[][] b, final S[][] z, final S[] alfr, final S[] alfi, final S[] beta) {
    S unit = a[0][0].createUnit();

    S r = unit.createZero();
    S s = unit.createZero();

    S w = unit.createZero();
    S x = unit.createZero();

    S di = unit.createZero();
    S dr = unit.createZero();
    S ra = unit.createZero();

    S sa = unit.createZero();
    S ti = unit.createZero();
    S tr = unit.createZero();
    S t1 = unit.createZero();
    S t2 = unit.createZero();
    S w1 = unit.createZero();
    S x1 = unit.createZero();
    S zz = unit.createZero();
    S z1 = unit.createZero();

    int size = a[0].length;

    S epsb = b[size - 1][0].clone();
    int isw = 1;

    for (int en = size; en >= 1; en--) {
      int na = en - 1;

      if (isw == 2) {
        isw = 3 - isw;
        continue;
      }

      if (alfi[en - 1].isZero()) {
        /*
         * Real vector
         */
        int m = en;
        b[en - 1][en - 1] = unit.createUnit();

        if (na == 0) {
          continue;
        }

        S alfm = alfr[m - 1].clone();
        S betm = beta[m - 1].clone();

        for (int i = en - 1; i >= 1; i--) {
          w = betm.multiply(a[i - 1][i - 1]).subtract(alfm.multiply(b[i - 1][i - 1]));
          r = unit.createZero();

          for (int j = m; j <= en; j++) {
            r = r.add((betm.multiply(a[i - 1][j - 1]).subtract(alfm.multiply(b[i - 1][j - 1]))).multiply(b[j - 1][en - 1]));
          }

          if (i != 1 && isw != 2 && betm.multiply(a[i - 1][i - 2]).isZero() == false) {
            zz = w.clone();
            s = r.clone();

            isw = 3 - isw;
            continue;
          }

          m = i;

          if (isw != 2) {
            /*
             * Real 1-by-1 block
             */
            S t = w.clone();
            if (w.isZero()) {
              t = epsb.clone();
            }
            b[i - 1][en - 1] = r.unaryMinus().divide(t);

            continue;
          }

          /*
           * Real 2-by-2 block
           */
          x = betm.multiply(a[i - 1][i]).subtract(alfm.multiply(b[i - 1][i]));
          S y = betm.multiply(a[i][i - 1]);
          S q = w.multiply(zz).subtract(x.multiply(y));
          S t = (x.multiply(s).subtract(zz.multiply(r))).divide(q);
          b[i - 1][en - 1] = t.clone();

          if (x.abs().isLessThanOrEquals(zz.abs())) {
            b[i][en - 1] = (s.unaryMinus().subtract(y.multiply(t))).divide(zz);
          } else {
            b[i][en - 1] = (r.unaryMinus().subtract(w.multiply(t))).divide(x);
          }

          isw = 3 - isw;
        }

        /*
         * End real vector
         */
        continue;
      }

      /*
       * Complex vector
       */
      int m = na;
      S almr = alfr[m - 1].clone();
      S almi = alfi[m - 1].clone();
      S betm = beta[m - 1].clone();

      /*
       * Last vector component chosen imaginary so that eigenvector matrix is
       * triangular.
       */
      S y = betm.multiply(a[en - 1][na - 1]);
      b[na - 1][na - 1] = almi.unaryMinus().multiply(b[en - 1][en - 1]).divide(y);
      b[na - 1][en - 1] = (almr.multiply(b[en - 1][en - 1]).subtract(betm.multiply(a[en - 1][en - 1]))).divide(y);
      b[en - 1][na - 1] = unit.createZero();
      b[en - 1][en - 1] = unit.createUnit();
      int enm2 = na - 1;

      if (enm2 == 0) {
        isw = 3 - isw;
        continue;
      }

      boolean contflag = false;
      int gotoflag;
      for (int i = en - 2; i >= 1; i--) {
        gotoflag = 0;
        do {
          if (gotoflag < 773) {
            w = betm.multiply(a[i - 1][i - 1]).subtract(almr.multiply(b[i - 1][i - 1]));
            w1 = almi.unaryMinus().multiply(b[i - 1][i - 1]);
            ra = unit.createZero();
            sa = unit.createZero();

            for (int j = m; j <= en; j++) {
              x = betm.multiply(a[i - 1][j - 1]).subtract(almr.multiply(b[i - 1][j - 1]));
              x1 = almi.unaryMinus().multiply(b[i - 1][j - 1]);
              ra = ra.add(x.multiply(b[j - 1][na - 1]).subtract(x1.multiply(b[j - 1][en - 1])));
              sa = sa.add(x.multiply(b[j - 1][en - 1]).add(x1.multiply(b[j - 1][na - 1])));
            }

            if (i != 1 && isw != 2 && betm.multiply(a[i - 1][i - 2]).isZero() == false) {
              zz = w.clone();
              z1 = w1.clone();
              r = ra.clone();
              s = sa.clone();
              isw = 2;

              // continue;
              contflag = true;
              break;
            }

            m = i;

          }
          if (isw != 2 || gotoflag == 773 || gotoflag == 775) {

            if (gotoflag < 773) {
              /*
               * Complex 1-by-1 block
               */
              tr = ra.unaryMinus();
              ti = sa.unaryMinus();
            }
            // L773:
            if (gotoflag == 773) {
              gotoflag = 0;
            }

            if (gotoflag < 775) {
              dr = w.clone();
              di = w1.clone();

              /*
               * Complex divide (t1,t2) = (tr,ti) / (dr,di)
               */
            }
            if (gotoflag == 775) {
              gotoflag = 0;
            }
            
            // L775:
            S d;
            if (di.abs().isLessThanOrEquals(dr.abs())) {
              S rr = di.divide(dr);
              d = dr.add(di.multiply(rr));
              t1 = (tr.add(ti.multiply(rr))).divide(d);
              t2 = (ti.subtract(tr.multiply(rr))).divide(d);

              // switch (isw) {
              // case 1: goto L787;
              // case 2: goto L782;
              // }
            } else {
              S rr = dr.divide(di);
              d = dr.multiply(rr).add(di);
              t1 = (tr.multiply(rr).add(ti)).divide(d);
              t2 = (ti.multiply(rr).subtract(tr)).divide(d);

              // switch (isw) {
              // case 1: goto L787;
              // case 2: goto L782;
              // }
            }
            switch (isw) {
              case 1:
                gotoflag = 787;
                break; // goto L787;
              case 2:
                gotoflag = 782;
                break; // goto L782;
              default:
                throw new RuntimeException();
            }
            if (gotoflag == 787) {
              break;
            }
          }

          if (gotoflag < 782) {
            /*
             * Complex 2-by-2 block
             */
            x = betm.multiply(a[i - 1][i]).subtract(almr.multiply(b[i - 1][i]));
            x1 = almi.unaryMinus().multiply(b[i - 1][i]);
            y = betm.multiply(a[i][i - 1]);
            tr = y.multiply(ra).subtract(w.multiply(r)).add(w1.multiply(s));
            ti = y.multiply(sa).subtract(w.multiply(s)).subtract(w1.multiply(r));
            dr = w.multiply(zz).subtract(w1.multiply(z1)).subtract(x.multiply(y));
            di = w.multiply(z1).add(w1.multiply(zz)).subtract(x1.multiply(y));

            if (dr.isZero() && di.isZero()) {
              dr = epsb.clone();
            }

            gotoflag = 775; // goto L775;
            continue;

          }
          if (gotoflag == 782) {
            gotoflag = 0;
          }
          // L782:
          if (gotoflag < 787) {
            b[i][na - 1] = t1.clone();
            b[i][en - 1] = t2.clone();
            isw = 1;

            if (y.abs().isLessThanOrEquals(w.abs().add(w1.abs()))) {
              tr = ra.unaryMinus().subtract(x.multiply(b[i][na - 1])).add(x1.multiply(b[i][en - 1]));
              ti = sa.unaryMinus().subtract(x.multiply(b[i][en - 1])).subtract(x1.multiply(b[i][na - 1]));

              gotoflag = 773; // goto L773;
              continue; // 03/01/09
            }

            t1 = (r.unaryMinus().subtract(zz.multiply(b[i][na - 1])).add(z1.multiply(b[i][en - 1]))).divide(y);
            t2 = (s.unaryMinus().subtract(zz.multiply(b[i][en - 1])).subtract(z1.multiply(b[i][na - 1]))).divide(y);
          }
        } while (true);
        if (contflag) {
          contflag = false;
          continue;
        }
        // L787:
        if (gotoflag == 787) {
          gotoflag = 0;
        }

        b[i - 1][na - 1] = t1.clone();
        b[i - 1][en - 1] = t2.clone();
      }

      /*
       * End Complex vector
       */
      isw = 3 - isw;
    }

    /*
     * End back substitution. Transform to original coordinate system.
     */
    for (int j = size; j >= 1; j--) {
      for (int i = 1; i <= size; i++) {
        zz = unit.createZero();

        for (int k = 1; k <= j; k++) {
          zz = zz.add(z[i - 1][k - 1].multiply(b[k - 1][j - 1]));
        }

        z[i - 1][j - 1] = zz.clone();
      }
    }

    /*
     * Normalize so that modulus of largest component of each vector is 1. (isw
     * is 1 initially from before)
     */
    for (int j = 1; j <= size; j++) {
      S d = unit.createZero();

      if (isw != 2) {
        if (alfi[j - 1].isZero() == false) {
          isw = 3 - isw;
          continue;
        }

        for (int i = 1; i <= size; i++) {
          if (z[i - 1][j - 1].abs().isGreaterThan(d)) {
            d = z[i - 1][j - 1].abs();
          }
        }

        for (int i = 1; i <= size; i++) {
          z[i - 1][j - 1] = z[i - 1][j - 1].divide(d);
        }

        continue;
      }

      for (int i = 1; i <= size; i++) {
        r = z[i - 1][j - 2].abs().add(z[i - 1][j - 1].abs());
        if (r.isZero() == false) {
          r = r.multiply(((z[i - 1][j - 2].divide(r)).multiply((z[i - 1][j - 2].divide(r))).add((z[i - 1][j - 1].divide(r)).multiply((z[i - 1][j - 1].divide(r))))).sqrt());
        }
        if (r.isGreaterThan(d)) {
          d = r.clone();
        }
      }

      for (int i = 1; i <= size; i++) {
        z[i - 1][j - 2] = z[i - 1][j - 2].divide(d);
        z[i - 1][j - 1] = z[i - 1][j - 1].divide(d);
      }

      isw = 3 - isw;
    }
  }

  /**
   * 符号がbと同じで、大きさがaと同じ値を返します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param a 大きさ決める値
   * @param b 符号を決める値
   * @return 符号がbと同じで、大きさがaと同じ値
   */
  private static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> S fsign(final S a, final S b) {
    return b.isGreaterThanOrEquals(a.createZero()) ? a.abs() : a.abs().unaryMinus();
  }
}