/*
 * $Id: DoubleRealGeneralizedEigenUtil.java,v 1.1 2008/01/27 02:09:47 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

/**
 * 倍精度(double)型の実行列の一般化固有値を求めるためのユーティリティクラスです。
 * 
 * @author koga
 * @version $Revision: 1.1 $
 */
final class DoubleRealGeneralizedEigenSolverUtil {
  /**
   * 新しく生成された<code>DoubleRealGeneralizedEigenSolverUtil</code>オブジェクトを初期化します。
   */
  private DoubleRealGeneralizedEigenSolverUtil() {
    // nothing to do
  }

  /**
   * 一般の実行列aとbの一般化固有値問題(a x = lambda b x)を等価な問題 (aが上ヘッセンベルグ行列、bが上三角行列)に変換します。
   * 
   * @param a 対象となる行列
   * @param b 対象となる行列
   * @param q QZ分解のQ
   * @param z QZ分解のZ
   * @param zDesired z行列を求めるならばtrue
   * @param qzDecompositionDesired QZ分解を求めるならばtrue
   */
  static void qzhes(final double[][] a, final double[][] b, final double[][] q, final double[][] z, final boolean zDesired, final boolean qzDecompositionDesired) {
    int size = a.length;

    // if (qz == true);/////////////// qq = q->elm.r;

    if (zDesired == true) {
      for (int i = 1; i <= size; i++) {
        for (int j = 1; j <= size; j++) {
          z[i - 1][j - 1] = 0.0;
        }
        z[i - 1][i - 1] = 1.0;
      }
    }

    if (qzDecompositionDesired == true) {
      for (int i = 1; i <= size; i++) {
        for (int j = 1; j <= size; j++) {
          q[i - 1][j - 1] = 0.0;
        }
        q[i - 1][i - 1] = 1.0;
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
      double s = 0.0;

      for (int i = l1; i <= size; i++) {
        s += Math.abs(b[i - 1][l - 1]);
      }

      /* if (s <= EPS) */
      if (s == 0.0) {
        continue;
      }

      s += Math.abs(b[l - 1][l - 1]);
      double r = 0.0;

      for (int i = l; i <= size; i++) {
        b[i - 1][l - 1] /= s;
        r += b[i - 1][l - 1] * b[i - 1][l - 1];
      }

      r = fsign(Math.sqrt(r), b[l - 1][l - 1]);
      b[l - 1][l - 1] += r;
      double rho = r * b[l - 1][l - 1];

      for (int j = l1; j <= size; j++) {
        double t = 0.0;
        for (int i = l; i <= size; i++) {
          t += b[i - 1][l - 1] * b[i - 1][j - 1];
        }

        t /= -rho;

        for (int i = l; i <= size; i++) {
          b[i - 1][j - 1] += t * b[i - 1][l - 1];
        }

      }

      for (int j = 1; j <= size; j++) {
        double t = 0.0;
        for (int i = l; i <= size; i++) {
          t += b[i - 1][l - 1] * a[i - 1][j - 1];
        }

        t /= -rho;

        for (int i = l; i <= size; i++) {
          a[i - 1][j - 1] += t * b[i - 1][l - 1];
        }
      }

      if (qzDecompositionDesired) {
        for (int j = 1; j <= size; j++) {
          double t = 0.0;
          for (int i = l; i <= size; i++) {
            t += b[i - 1][l - 1] * q[i - 1][j - 1];
          }

          t /= -rho;

          for (int i = l; i <= size; i++) {
            q[i - 1][j - 1] += t * b[i - 1][l - 1];
          }
        }
      }

      b[l - 1][l - 1] = -s * r;

      for (int i = l1; i <= size; i++) {
        b[i - 1][l - 1] = 0.0;
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
        double s = Math.abs(a[l - 1][k - 1]) + Math.abs(a[l1 - 1][k - 1]);

        /* if (s <= EPS) */
        if (s == 0.0) {
          continue;
        }

        double u1 = a[l - 1][k - 1] / s;
        double u2 = a[l1 - 1][k - 1] / s;
        double r = fsign(Math.sqrt(u1 * u1 + u2 * u2), u1);
        double v1 = -(u1 + r) / r;
        double v2 = -u2 / r;
        u2 = v2 / v1;

        for (int j = k; j <= size; j++) {
          double t = a[l - 1][j - 1] + u2 * a[l1 - 1][j - 1];
          a[l - 1][j - 1] += t * v1;
          a[l1 - 1][j - 1] += t * v2;
        }

        a[l1 - 1][k - 1] = 0.0;

        if (qzDecompositionDesired) {
          for (int j = 1; j <= size; j++) {
            double t = q[l - 1][j - 1] + u2 * q[l1 - 1][j - 1];
            q[l - 1][j - 1] += t * v1;
            q[l1 - 1][j - 1] += t * v2;
          }
        }

        for (int j = l; j <= size; j++) {
          double t = b[l - 1][j - 1] + u2 * b[l1 - 1][j - 1];
          b[l - 1][j - 1] += t * v1;
          b[l1 - 1][j - 1] += t * v2;
        }

        /*
         * Zero B(l+1,l)
         */
        s = Math.abs(b[l1 - 1][l1 - 1]) + Math.abs(b[l1 - 1][l - 1]);

        /* if (s <= EPS) */
        if (s == 0.0) {
          continue;
        }

        u1 = b[l1 - 1][l1 - 1] / s;
        u2 = b[l1 - 1][l - 1] / s;
        r = fsign(Math.sqrt(u1 * u1 + u2 * u2), u1);
        v1 = -(u1 + r) / r;
        v2 = -u2 / r;
        u2 = v2 / v1;

        for (int i = 1; i <= l1; i++) {
          double t = b[i - 1][l1 - 1] + u2 * b[i - 1][l - 1];
          b[i - 1][l1 - 1] += t * v1;
          b[i - 1][l - 1] += t * v2;
        }

        b[l1 - 1][l - 1] = 0.0;

        for (int i = 1; i <= size; i++) {
          double t = a[i - 1][l1 - 1] + u2 * a[i - 1][l - 1];
          a[i - 1][l1 - 1] += t * v1;
          a[i - 1][l - 1] += t * v2;
        }

        if (zDesired) {
          for (int i = 1; i <= size; i++) {
            double t = z[i - 1][l1 - 1] + u2 * z[i - 1][l - 1];
            z[i - 1][l1 - 1] += t * v1;
            z[i - 1][l - 1] += t * v2;
          }
        }
      }
    }
  }

  /**
   * 一般化固有値問題(a x = lambda b x)(aが上ヘッセンベルグ行列、bが上三角行列)を 等価な問題(aが擬似上三角行列、bが上三角行列)へ変換します。
   * 
   * @param a 上ヘッセンベルグ行列
   * @param b 上三角行列
   * @param q QZ分解のQ行列
   * @param z QZ分解のZ行列
   * @param tolerance 許容誤差
   * @param zDesired QZ分解のZを求めるならばtrue
   * @param qzDecompositionDesired QZ分解を求めるならばtrue
   * @return 計算の結果
   */
  static int qzit(final double[][] a, final double[][] b, final double[][] q, final double[][] z, final double tolerance, final boolean zDesired, final boolean qzDecompositionDesired) {
    int l = 0, k1 = 0, k2 = 0, ld = 0, ll = 0, l1 = 0;
    int na = 0;
    int ish = 0, its = 0, itn = 0, km1 = 0, lm1 = 0, enm2 = 0;

    double a1 = 0, a2 = 0, a3 = 0, sh = 0;
    double ani, a11 = 0, a12 = 0, a21 = 0, a22 = 0, a33 = 0, a34 = 0, a43 = 0, a44 = 0;
    double bni, b11 = 0, b12 = 0, b22 = 0, b33 = 0, b34 = 0, b44 = 0;

    int size = a[0].length;

    int ierr = 0;

    /*
     * Compute epsa, epsb
     */
    double anorm = 0.0;
    double bnorm = 0.0;

    for (int i = 1; i <= size; i++) {
      ani = 0.0;
      if (i != 1) {
        ani = Math.abs(a[i - 1][i - 2]);
      }
      bni = 0.0;

      for (int j = 1; j <= size; j++) {
        ani += Math.abs(a[i - 1][j - 1]);
        bni += Math.abs(b[i - 1][j - 1]);
      }

      if (ani > anorm) {
        anorm = ani;
      }
      if (bni > bnorm) {
        bnorm = bni;
      }
    }

    /* if (anorm <= EPS) anorm = 1.0; */
    if (anorm == 0.0) {
      anorm = 1.0;
    }
    /* if (bnorm <= EPS) bnorm = 1.0; */
    if (bnorm == 0.0) {
      bnorm = 1.0;
    }

    double ep = tolerance;

    /*
     * Compute roundoff level if eps1 is zero
     */
    // if (ep > 0.0) goto L50;
    if (!(ep > 0.0)) {

      ep = 1.0;
      do {
        ep /= 2.0;
      } while (1.0 + ep > 1.0);
    }

    // L50:
    double epsa = ep * anorm;
    double epsb = ep * bnorm;

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
            b[size - 1][0] = epsb;
          }
          return ierr;
        }

        if (!zDesired) {
          enorn = en;
        }

        its = 0;
        itn = 30 * size;
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

          if (Math.abs(a[l - 1][lm1 - 1]) <= epsa) {
            break;
          }

        }
      }
      // L90:
      if (gotoflag == 90) {
        gotoflag = 0;
      }

      if (gotoflag < 95) {
        a[l - 1][lm1 - 1] = 0.0;

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
        b11 = b[l - 1][l - 1];

        if (Math.abs(b11) > epsb) { // goto L120;
          gotoflag = 120;
        } else {
          b[l - 1][l - 1] = 0.0;
          double s = Math.abs(a[l - 1][l - 1]) + Math.abs(a[l1 - 1][l - 1]);
          double u1 = a[l - 1][l - 1] / s;
          double u2 = a[l1 - 1][l - 1] / s;
          double r = fsign(Math.sqrt(u1 * u1 + u2 * u2), u1);
          double v1 = -(u1 + r) / r;
          double v2 = -u2 / r;
          u2 = v2 / v1;

          for (int j = l; j <= enorn; j++) {
            double t = a[l - 1][j - 1] + u2 * a[l1 - 1][j - 1];
            a[l - 1][j - 1] += t * v1;
            a[l1 - 1][j - 1] += t * v2;

            t = b[l - 1][j - 1] + u2 * b[l1 - 1][j - 1];
            b[l - 1][j - 1] += t * v1;
            b[l1 - 1][j - 1] += t * v2;
          }

          if (qzDecompositionDesired) {
            for (int j = 1; j <= size; j++) {
              double t = q[l - 1][j - 1] + u2 * q[l1 - 1][j - 1];
              q[l - 1][j - 1] += t * v1;
              q[l1 - 1][j - 1] += t * v2;
            }
          }

          if (l != 1) {
            a[l - 1][lm1 - 1] = -a[l - 1][lm1 - 1];
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
        a11 = a[l - 1][l - 1] / b11;
        a21 = a[l1 - 1][l - 1] / b11;

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
            b22 = b[l1 - 1][l1 - 1];
            if (Math.abs(b22) < epsb) {
              b22 = epsb;
            }
            b33 = b[na - 1][na - 1];
            if (Math.abs(b33) < epsb) {
              b33 = epsb;
            }
            b44 = b[en - 1][en - 1];
            if (Math.abs(b44) < epsb) {
              b44 = epsb;
            }
            a33 = a[na - 1][na - 1] / b33;
            a34 = a[na - 1][en - 1] / b44;
            a43 = a[en - 1][na - 1] / b33;
            a44 = a[en - 1][en - 1] / b44;
            b34 = b[na - 1][en - 1] / b44;
            double t = 0.5 * (a43 * b34 - a33 - a44);
            double r = t * t + a34 * a43 - a33 * a44;

            if (r < 0.0) {
              gotoflag = 150; // goto L150;
            } else {

              /*
               * Determine single shift zeroth column of A.
               */
              ish = 1;
              r = Math.sqrt(r);
              sh = -t + r;
              double s = -t - r;
              if (Math.abs(s - a44) < Math.abs(sh - a44)) {
                sh = s;
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
                t = a[l - 1][l - 1];
                if (Math.abs(b[l - 1][l - 1]) > epsb) {
                  t -= sh * b[l - 1][l - 1];
                }

                if (Math.abs(a[l - 1][lm1 - 1]) <= Math.abs(t / a[l1 - 1][l - 1]) * epsa) {
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
        a1 = a11 - sh;
        a2 = a21;
        if (l != ld) {
          a[l - 1][lm1 - 1] = -a[l - 1][lm1 - 1];
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
        a12 = a[l - 1][l1 - 1] / b22;
        a22 = a[l1 - 1][l1 - 1] / b22;
        b12 = b[l - 1][l1 - 1] / b22;
        a1 = ((a33 - a11) * (a44 - a11) - a34 * a43 + a43 * b34 * a11) / a21 + a12 - a11 * b12;
        a2 = (a22 - a11) - a21 * b12 - (a33 - a11) - (a44 - a11) + a43 * b34;
        a3 = a[l1][l1 - 1] / b22;
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
        a1 = 0.0;
        a2 = 1.0;
        a3 = 1.1605;
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
              a1 = a[k - 1][km1 - 1];
              a2 = a[k1 - 1][km1 - 1];
            }
          }
        }

        // L170:
        if (gotoflag == 170) {
          gotoflag = 0;
        }
        if (gotoflag < 190) {
          double s = Math.abs(a1) + Math.abs(a2);

          /* if (s <= EPS) goto L70; */
          if (s == 0.0) {
            gotoflag = 70; // goto L70;
            break;
          }

          double u1 = a1 / s;
          double u2 = a2 / s;
          double r = fsign(Math.sqrt(u1 * u1 + u2 * u2), u1);
          double v1 = -(u1 + r) / r;
          double v2 = -u2 / r;
          u2 = v2 / v1;

          for (int j = km1; j <= enorn; j++) {
            double t = a[k - 1][j - 1] + u2 * a[k1 - 1][j - 1];
            a[k - 1][j - 1] += t * v1;
            a[k1 - 1][j - 1] += t * v2;

            t = b[k - 1][j - 1] + u2 * b[k1 - 1][j - 1];
            b[k - 1][j - 1] += t * v1;
            b[k1 - 1][j - 1] += t * v2;
          }

          if (qzDecompositionDesired) {
            for (int j = 1; j <= size; j++) {
              double t = q[k - 1][j - 1] + u2 * q[k1 - 1][j - 1];
              q[k - 1][j - 1] += t * v1;
              q[k1 - 1][j - 1] += t * v2;
            }
          }

          if (k != l) {
            a[k1 - 1][km1 - 1] = 0.0;
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
            a1 = a[k - 1][km1 - 1];
            a2 = a[k1 - 1][km1 - 1];
            a3 = a[k2 - 1][km1 - 1];
          }

          double s  = Math.abs(a1) + Math.abs(a2) + Math.abs(a3);

          /* if (s <= EPS) */
          if (s == 0.0) {
            continue;
          }

          double u1 = a1 / s;
          double u2 = a2 / s;
          double u3 = a3 / s;
          double r = fsign(Math.sqrt(u1 * u1 + u2 * u2 + u3 * u3), u1);
          double v1 = -(u1 + r) / r;
          double v2 = -u2 / r;
          double v3 = -u3 / r;
          u2 = v2 / v1;
          u3 = v3 / v1;

          for (int j = km1; j <= enorn; j++) {
            double t = a[k - 1][j - 1] + u2 * a[k1 - 1][j - 1] + u3 * a[k2 - 1][j - 1];
            a[k - 1][j - 1] += t * v1;
            a[k1 - 1][j - 1] += t * v2;
            a[k2 - 1][j - 1] += t * v3;

            t = b[k - 1][j - 1] + u2 * b[k1 - 1][j - 1] + u3 * b[k2 - 1][j - 1];
            b[k - 1][j - 1] += t * v1;
            b[k1 - 1][j - 1] += t * v2;
            b[k2 - 1][j - 1] += t * v3;
          }

          if (qzDecompositionDesired) {
            for (int j = 1; j <= size; j++) {
              double t = q[k - 1][j - 1] + u2 * q[k1 - 1][j - 1] + u3 * q[k2 - 1][j - 1];
              q[k - 1][j - 1] += t * v1;
              q[k1 - 1][j - 1] += t * v2;
              q[k2 - 1][j - 1] += t * v3;
            }
          }

          if (k != l) {
            a[k1 - 1][km1 - 1] = 0.0;
            a[k2 - 1][km1 - 1] = 0.0;
          }

          /*
           * Zero B(k+2,k+1) and B(k+2,k)
           */
          s = Math.abs(b[k2 - 1][k2 - 1]) + Math.abs(b[k2 - 1][k1 - 1]) + Math.abs(b[k2 - 1][k - 1]);

          /* if (s <= EPS) goto L240; */
          if (s == 0.0) {
            gotoflag = 240; // goto L240;
          } else {

            u1 = b[k2 - 1][k2 - 1] / s;
            u2 = b[k2 - 1][k1 - 1] / s;
            u3 = b[k2 - 1][k - 1] / s;
            r = fsign(Math.sqrt(u1 * u1 + u2 * u2 + u3 * u3), u1);
            v1 = -(u1 + r) / r;
            v2 = -u2 / r;
            v3 = -u3 / r;
            u2 = v2 / v1;
            u3 = v3 / v1;

            for (int i = lor1; i <= ll; i++) {
              double t = a[i - 1][k2 - 1] + u2 * a[i - 1][k1 - 1] + u3 * a[i - 1][k - 1];
              a[i - 1][k2 - 1] += t * v1;
              a[i - 1][k1 - 1] += t * v2;
              a[i - 1][k - 1] += t * v3;

              t = b[i - 1][k2 - 1] + u2 * b[i - 1][k1 - 1] + u3 * b[i - 1][k - 1];
              b[i - 1][k2 - 1] += t * v1;
              b[i - 1][k1 - 1] += t * v2;
              b[i - 1][k - 1] += t * v3;
            }

            b[k2 - 1][k - 1] = 0.0;
            b[k2 - 1][k1 - 1] = 0.0;

            if (zDesired) {
              for (int i = 1; i <= size; i++) {
                double t = z[i - 1][k2 - 1] + u2 * z[i - 1][k1 - 1] + u3 * z[i - 1][k - 1];
                z[i - 1][k2 - 1] += t * v1;
                z[i - 1][k1 - 1] += t * v2;
                z[i - 1][k - 1] += t * v3;
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
        double s = Math.abs(b[k1 - 1][k1 - 1]) + Math.abs(b[k1 - 1][k - 1]);

        /* if (s <= EPS) */
        if (s == 0.0) {
          continue;
        }

        double u1 = b[k1 - 1][k1 - 1] / s;
        double u2 = b[k1 - 1][k - 1] / s;
        double r = fsign(Math.sqrt(u1 * u1 + u2 * u2), u1);
        double v1 = -(u1 + r) / r;
        double v2 = -u2 / r;
        u2 = v2 / v1;

        for (int i = lor1; i <= ll; i++) {
          double t = a[i - 1][k1 - 1] + u2 * a[i - 1][k - 1];
          a[i - 1][k1 - 1] += t * v1;
          a[i - 1][k - 1] += t * v2;

          t = b[i - 1][k1 - 1] + u2 * b[i - 1][k - 1];
          b[i - 1][k1 - 1] += t * v1;
          b[i - 1][k - 1] += t * v2;
        }

        b[k1 - 1][k - 1] = 0.0;

        if (zDesired) {
          for (int i = 1; i <= size; i++) {
            double t = z[i - 1][k1 - 1] + u2 * z[i - 1][k - 1];
            z[i - 1][k1 - 1] += t * v1;
            z[i - 1][k - 1] += t * v2;
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
      b[size - 1][0] = epsb;
    }

    return ierr;

  }

  /**
   * 一般化固有値問題(a x = lambda b x)(aが擬似上三角行列、bが上三角行列)を 等価な問題(aが非零の対角成分を減らした行列、bが上三角行列)に変換します。
   * 
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
  static void qzval(final double[][] a, final double[][] b, final double[][] q, final double[][] z, final double[] alfr, final double[] alfi, final double[] beta, final boolean zDesired, final boolean qzDecompositionDesired) {
    double c = 0, d = 0, e = 0, an2 = 0, a1 = 0, a2 = 0, bn2 = 0;
    double ti = 0, tr = 0, a1i, a11 = 0, a12 = 0, a2i, a21 = 0, a22 = 0;
    double b11 = 0, b12 = 0, b22 = 0;

    int size = a[0].length;

    double epsb = b[size - 1][0];
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
      if (en == 1 || a[en - 1][na - 1] == 0.0) {
        alfr[en - 1] = a[en - 1][en - 1];
        if (b[en - 1][en - 1] < 0.0) {
          alfr[en - 1] = -alfr[en - 1];
        }
        beta[en - 1] = Math.abs(b[en - 1][en - 1]);
        alfi[en - 1] = 0.0;
        continue;
      }

      /*
       * 2-by-2 block
       */
      // boolean goto460 = false;
      if (Math.abs(b[na - 1][na - 1]) <= epsb) {
        a1 = a[na - 1][na - 1];
        a2 = a[en - 1][na - 1];
        goto460 = true; // goto L460;
      }

      if (!goto460) {
        if (Math.abs(b[en - 1][en - 1]) <= epsb) {
          a1 = a[en - 1][en - 1];
          a2 = a[en - 1][na - 1];
          bn2 = 0.0;
        } else {
          an2 = Math.abs(a[na - 1][na - 1]) + Math.abs(a[na - 1][en - 1]) + Math.abs(a[en - 1][na - 1]) + Math.abs(a[en - 1][en - 1]);
          bn2 = Math.abs(b[na - 1][na - 1]) + Math.abs(b[na - 1][en - 1]) + Math.abs(b[en - 1][en - 1]);
          a11 = a[na - 1][na - 1] / an2;
          a12 = a[na - 1][en - 1] / an2;
          a21 = a[en - 1][na - 1] / an2;
          a22 = a[en - 1][en - 1] / an2;
          b11 = b[na - 1][na - 1] / bn2;
          b12 = b[na - 1][en - 1] / bn2;
          b22 = b[en - 1][en - 1] / bn2;
          e = a11 / b11;
          double ei = a22 / b22;
          double s = a21 / (b11 * b22);
          double t = (a22 - e * b22) / b22;

          if (Math.abs(e) > Math.abs(ei)) {
            e = ei;
            t = (a11 - e * b11) / b11;
          }

          c = 0.5 * (t - s * b12);
          d = c * c + s * (a12 - e * b12);
          // boolean goto480 = false;
          if (d < 0.0) {
            goto480 = true; // goto L480;
          }

          if (!goto480) {
            /*
             * Two real roots. Zero both A(en,na) and B(en,na).
             */
            e += (c + fsign(Math.sqrt(d), c));
            a11 -= e * b11;
            a12 -= e * b12;
            a22 -= e * b22;

            if (Math.abs(a11) + Math.abs(a12) < Math.abs(a21) + Math.abs(a22)) {
              a1 = a22;
              a2 = a21;
            } else {
              a1 = a12;
              a2 = a11;
            }
          }
        }
      }
      if (!goto480) {
        if (!goto460) {
          /*
           * Choose and apply real Z
           */
          double s = Math.abs(a1) + Math.abs(a2);
          double u1 = a1 / s;
          double u2 = a2 / s;
          double r = fsign(Math.sqrt(u1 * u1 + u2 * u2), u1);
          double v1 = -(u1 + r) / r;
          double v2 = -u2 / r;
          u2 = v2 / v1;

          for (int i = 1; i <= en; i++) {
            double t = a[i - 1][en - 1] + u2 * a[i - 1][na - 1];
            a[i - 1][en - 1] += t * v1;
            a[i - 1][na - 1] += t * v2;

            t = b[i - 1][en - 1] + u2 * b[i - 1][na - 1];
            b[i - 1][en - 1] += t * v1;
            b[i - 1][na - 1] += t * v2;
          }

          if (zDesired) {
            for (int i = 1; i <= size; i++) {
              double t = z[i - 1][en - 1] + u2 * z[i - 1][na - 1];
              z[i - 1][en - 1] += t * v1;
              z[i - 1][na - 1] += t * v2;
            }
          }
        }

        /* if (Math.abs(bn2) > EPS) { */
        if (bn2 != 0.0 || goto460) {
          if (!goto460) {
            if (an2 < Math.abs(e) * bn2) {
              a1 = a[na - 1][na - 1];
              a2 = a[en - 1][na - 1];
            } else {
              a1 = b[na - 1][na - 1];
              a2 = b[en - 1][na - 1];
            }

            /*
             * Choose and apply real Q
             */
          }
          goto460 = false;

          // L460:
          double s = Math.abs(a1) + Math.abs(a2);

          /* if (Math.abs(s) > EPS) { */
          if (s != 0.0) {
            double u1 = a1 / s;
            double u2 = a2 / s;
            double r = fsign(Math.sqrt(u1 * u1 + u2 * u2), u1);
            double v1 = -(u1 + r) / r;
            double v2 = -u2 / r;
            u2 = v2 / v1;

            for (int j = na; j <= size; j++) {
              double t = a[na - 1][j - 1] + u2 * a[en - 1][j - 1];
              a[na - 1][j - 1] += t * v1;
              a[en - 1][j - 1] += t * v2;

              t = b[na - 1][j - 1] + u2 * b[en - 1][j - 1];
              b[na - 1][j - 1] += t * v1;
              b[en - 1][j - 1] += t * v2;
            }

            if (qzDecompositionDesired) {
              for (int j = 1; j <= size; j++) {
                double t = q[na - 1][j - 1] + u2 * q[en - 1][j - 1];
                q[na - 1][j - 1] += t * v1;
                q[en - 1][j - 1] += t * v2;
              }
            }
          }
        }

        a[en - 1][na - 1] = 0.0;
        b[en - 1][na - 1] = 0.0;
        alfr[na - 1] = a[na - 1][na - 1];
        alfr[en - 1] = a[en - 1][en - 1];
        if (b[na - 1][na - 1] < 0.0) {
          alfr[na - 1] = -alfr[na - 1];
        }
        if (b[en - 1][en - 1] < 0.0) {
          alfr[en - 1] = -alfr[en - 1];
        }
        beta[na - 1] = Math.abs(b[na - 1][na - 1]);
        beta[en - 1] = Math.abs(b[en - 1][en - 1]);
        alfi[en - 1] = 0.0;
        alfi[na - 1] = 0.0;

        isw = 3 - isw;
        continue;

        /*
         * Two complex roots
         */
      }
      goto480 = false;
      // L480:
      e += c;
      double ei = Math.sqrt(-d);
      double a11r = a11 - e * b11;
      double a11i = ei * b11;
      double a12r = a12 - e * b12;
      double a12i = ei * b12;
      double a22r = a22 - e * b22;
      double a22i = ei * b22;
      if (Math.abs(a11r) + Math.abs(a11i) + Math.abs(a12r) + Math.abs(a12i) < Math.abs(a21) + Math.abs(a22r) + Math.abs(a22i)) {
        a1 = a22r;
        a1i = a22i;
        a2 = -a21;
        a2i = 0.0;
      } else {
        a1 = a12r;
        a1i = a12i;
        a2 = -a11r;
        a2i = -a11i;
      }

      /*
       * Choose complex Z
       */
      double cz = Math.sqrt(a1 * a1 + a1i * a1i);

      double szr;
      double szi;
      
      /* if (Math.abs(cz) <= EPS) { */
      if (cz == 0.0) {
        szr = 1.0;
        szi = 0.0;
      } else {
        szr = (a1 * a2 + a1i * a2i) / cz;
        szi = (a1 * a2i - a1i * a2) / cz;
        double r = Math.sqrt(cz * cz + szr * szr + szi * szi);
        cz /= r;
        szr /= r;
        szi /= r;
      }

      if (an2 < (Math.abs(e) + ei) * bn2) {
        a1 = cz * a11 + szr * a12;
        a1i = szi * a12;
        a2 = cz * a21 + szr * a22;
        a2i = szi * a22;
      } else {
        a1 = cz * b11 + szr * b12;
        a1i = szi * b12;
        a2 = szr * b22;
        a2i = szi * b22;
      }

      /*
       * Choose complex Q
       */
      double cq = Math.sqrt(a1 * a1 + a1i * a1i);

      double sqr;
      double sqi;
      
      /* if (Math.abs(cq) <= EPS) { */
      if (cq == 0.0) {
        sqr = 1.0;
        sqi = 0.0;
      } else {
        sqr = (a1 * a2 + a1i * a2i) / cq;
        sqi = (a1 * a2i - a1i * a2) / cq;
        double r = Math.sqrt(cq * cq + sqr * sqr + sqi * sqi);
        cq /= r;
        sqr /= r;
        sqi /= r;
      }

      /*
       * Compute diagonal elements that would result if transformations were
       * applied.
       */
      double ssr = sqr * szr + sqi * szi;
      double ssi = sqr * szi - sqi * szr;
      int i = 1;
      tr = cq * cz * a11 + cq * szr * a12 + sqr * cz * a21 + ssr * a22;
      ti = cq * szi * a12 - sqi * cz * a21 + ssi * a22;
      double dr = cq * cz * b11 + cq * szr * b12 + ssr * b22;
      double di = cq * szi * b12 + ssi * b22;

      goto503 = true; // goto L503;

      // L502:
      do {
        if (!goto503) {
          i = 2;
          tr = ssr * a11 - sqr * cz * a12 - cq * szr * a21 + cq * cz * a22;
          ti = -ssi * a11 - sqi * cz * a12 + cq * szi * a21;
          dr = ssr * b11 - sqr * cz * b12 + cq * cz * b22;
          di = -ssi * b11 - sqi * cz * b12;
        }
        goto503 = false;
        // L503:
        double t = ti * dr - tr * di;
        int j = na;
        if (t < 0.0) {
          j = en;
        }
        double r = Math.sqrt(dr * dr + di * di);
        beta[j - 1] = bn2 * r;
        alfr[j - 1] = an2 * (tr * dr + ti * di) / r;
        alfi[j - 1] = an2 * t / r;

      } while (i == 1); // if (i == 1) goto L502;

      isw = 3 - isw;
    }
  }

  /**
   * 一般化固有値問題(a x = lambda b x)(aが擬似上三角行列、bが上三角行列)の 固有ベクトルを求めます。
   * 
   * @param a 擬似上三角行列
   * @param b 上三角行列
   * @param z 一般化固有ベクトルの実部と虚部
   * @param alfr aを完全に上三角行列に変換したときの対角成分の実部
   * @param alfi aを完全に上三角行列に変換したときの対角成分の虚部
   * @param beta bを上三角行列に変換したときの対角成分
   */
  static void qzvec(final double[][] a, final double[][] b, final double[][] z, final double[] alfr, final double[] alfi, final double[] beta) {
    double r = 0, s = 0, w = 0, x = 0, di = 0, dr = 0, ra = 0, sa = 0, ti = 0, tr = 0;
    double t1 = 0, t2 = 0, w1 = 0, x1 = 0, zz = 0, z1 = 0;

    int size = a[0].length;

    double epsb = b[size - 1][0];
    int isw = 1;

    for (int en = size; en >= 1; en--) {
      int na = en - 1;

      if (isw == 2) {
        isw = 3 - isw;
        continue;
      }

      if (alfi[en - 1] == 0.0) {
        /*
         * Real vector
         */
        int m = en;
        b[en - 1][en - 1] = 1.0;

        if (na == 0) {
          continue;
        }

        double alfm = alfr[m - 1];
        double betm = beta[m - 1];

        for (int i = en - 1; i >= 1; i--) {
          w = betm * a[i - 1][i - 1] - alfm * b[i - 1][i - 1];
          r = 0.0;

          for (int j = m; j <= en; j++) {
            r += (betm * a[i - 1][j - 1] - alfm * b[i - 1][j - 1]) * b[j - 1][en - 1];
          }

          if (i != 1 && isw != 2 && betm * a[i - 1][i - 2] != 0.0) {
            zz = w;
            s = r;

            isw = 3 - isw;
            continue;
          }

          m = i;

          if (isw != 2) {
            /*
             * Real 1-by-1 block
             */
            double t = w;
            if (w == 0.0) {
              t = epsb;
            }
            b[i - 1][en - 1] = -r / t;

            continue;
          }

          /*
           * Real 2-by-2 block
           */
          x = betm * a[i - 1][i] - alfm * b[i - 1][i];
          double y = betm * a[i][i - 1];
          double q = w * zz - x * y;
          double t = (x * s - zz * r) / q;
          b[i - 1][en - 1] = t;

          if (Math.abs(x) <= Math.abs(zz)) {
            b[i][en - 1] = (-s - y * t) / zz;
          } else {
            b[i][en - 1] = (-r - w * t) / x;
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
      double almr = alfr[m - 1];
      double almi = alfi[m - 1];
      double betm = beta[m - 1];

      /*
       * Last vector component chosen imaginary so that eigenvector matrix is
       * triangular.
       */
      double y = betm * a[en - 1][na - 1];
      b[na - 1][na - 1] = -almi * b[en - 1][en - 1] / y;
      b[na - 1][en - 1] = (almr * b[en - 1][en - 1] - betm * a[en - 1][en - 1]) / y;
      b[en - 1][na - 1] = 0.0;
      b[en - 1][en - 1] = 1.0;
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
            w = betm * a[i - 1][i - 1] - almr * b[i - 1][i - 1];
            w1 = -almi * b[i - 1][i - 1];
            ra = 0.0;
            sa = 0.0;

            for (int j = m; j <= en; j++) {
              x = betm * a[i - 1][j - 1] - almr * b[i - 1][j - 1];
              x1 = -almi * b[i - 1][j - 1];
              ra += x * b[j - 1][na - 1] - x1 * b[j - 1][en - 1];
              sa += x * b[j - 1][en - 1] + x1 * b[j - 1][na - 1];
            }

            if (i != 1 && isw != 2 && betm * a[i - 1][i - 2] != 0.0) {
              zz = w;
              z1 = w1;
              r = ra;
              s = sa;
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
              tr = -ra;
              ti = -sa;
            }
            // L773:
            if (gotoflag == 773) {
              gotoflag = 0;
            }

            if (gotoflag < 775) {
              dr = w;
              di = w1;

              /*
               * Complex divide (t1,t2) = (tr,ti) / (dr,di)
               */
            }
            if (gotoflag == 775) {
              gotoflag = 0;
            }
            // L775:
            if (Math.abs(di) <= Math.abs(dr)) {
              double rr = di / dr;
              double d = dr + di * rr;
              t1 = (tr + ti * rr) / d;
              t2 = (ti - tr * rr) / d;

              // switch (isw) {
              // case 1: goto L787;
              // case 2: goto L782;
              // }
            } else {
              double rr = dr / di;
              double d = dr * rr + di;
              t1 = (tr * rr + ti) / d;
              t2 = (ti * rr - tr) / d;

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
            x = betm * a[i - 1][i] - almr * b[i - 1][i];
            x1 = -almi * b[i - 1][i];
            y = betm * a[i][i - 1];
            tr = y * ra - w * r + w1 * s;
            ti = y * sa - w * s - w1 * r;
            dr = w * zz - w1 * z1 - x * y;
            di = w * z1 + w1 * zz - x1 * y;

            if (dr == 0.0 && di == 0.0) {
              dr = epsb;
            }

            gotoflag = 775; // goto L775;
            continue;

          }
          if (gotoflag == 782) {
            gotoflag = 0;
          }
          // L782:
          if (gotoflag < 787) {
            b[i][na - 1] = t1;
            b[i][en - 1] = t2;
            isw = 1;

            if (Math.abs(y) <= Math.abs(w) + Math.abs(w1)) {
              tr = -ra - x * b[i][na - 1] + x1 * b[i][en - 1];
              ti = -sa - x * b[i][en - 1] - x1 * b[i][na - 1];

              gotoflag = 773; // goto L773;
              continue; // 03/01/09
            }

            t1 = (-r - zz * b[i][na - 1] + z1 * b[i][en - 1]) / y;
            t2 = (-s - zz * b[i][en - 1] - z1 * b[i][na - 1]) / y;
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

        b[i - 1][na - 1] = t1;
        b[i - 1][en - 1] = t2;
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
        zz = 0.0;

        for (int k = 1; k <= j; k++) {
          zz += z[i - 1][k - 1] * b[k - 1][j - 1];
        }

        z[i - 1][j - 1] = zz;
      }
    }

    /*
     * Normalize so that modulus of largest component of each vector is 1. (isw
     * is 1 initially from before)
     */
    for (int j = 1; j <= size; j++) {
      double d = 0.0;

      if (isw != 2) {
        if (alfi[j - 1] != 0.0) {
          isw = 3 - isw;
          continue;
        }

        for (int i = 1; i <= size; i++) {
          if (Math.abs(z[i - 1][j - 1]) > d) {
            d = Math.abs(z[i - 1][j - 1]);
          }
        }

        for (int i = 1; i <= size; i++) {
          z[i - 1][j - 1] /= d;
        }

        continue;
      }

      for (int i = 1; i <= size; i++) {
        r = Math.abs(z[i - 1][j - 2]) + Math.abs(z[i - 1][j - 1]);
        if (r != 0.0) {
          r *= Math.sqrt((z[i - 1][j - 2] / r) * (z[i - 1][j - 2] / r) + (z[i - 1][j - 1] / r) * (z[i - 1][j - 1] / r));
        }
        if (r > d) {
          d = r;
        }
      }

      for (int i = 1; i <= size; i++) {
        z[i - 1][j - 2] /= d;
        z[i - 1][j - 1] /= d;
      }

      isw = 3 - isw;
    }
  }

  /**
   * 符号がbと同じで、大きさがaと同じ値を返します。
   * 
   * @param a 大きさ決める値
   * @param b 符号を決める値
   * @return 符号がbと同じで、大きさがaと同じ値
   */
  private static double fsign(final double a, final double b) {
    return (((b) >= 0.0) ? (Math.abs(a)) : (-Math.abs(a)));
  }
}