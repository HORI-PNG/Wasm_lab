/*
 * $Id: DoubleRealEigenUtil.java,v 1.5 2008/02/17 02:44:25 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.scalar.DoubleNumberUtil;


/**
 * 倍精度(double)型の実行列の固有値を求めるためのユーティリティクラスです。
 * 
 * @author koga
 * @version $Revision: 1.5 $
 */
final class DoubleRealEigenSolverUtil {
  /**
   * 新しく生成された<code>DoubleRealEigenSolverUtil</code>オブジェクトを初期化します。
   */
  private DoubleRealEigenSolverUtil() {
    // nothing to do
  }

  /** 基数。 */
  private static final double RADIX = 2.0;
  /** 基数の2乗。 */
  private static final double B2 = RADIX * RADIX;
  /** 計算機イプシロン。 */
  private static final double MACHEP = DoubleNumberUtil.EPS;

  /**
   * 行列を直交同次変換により上ヘッセンベルグ行列へ変換します。
   * 
   * @param a 入力:対象となる行列、出力:ヘッセンベルグ行列
   * @param low 開始番号
   * @param igh 終了番号
   * @param ort 変換に関する情報
   */
  static void orthes(final double[][] a, final int low, final int igh, final double[] ort) {
    final int size = a.length;
    final int la = igh - 1;
    final int kp1 = low + 1;

    if (la < kp1) {
      return;
    }

    for (int m = kp1; m <= la; m++) {
      double h = 0.0;
      ort[m - 1] = 0.0;
      double scale = 0.0;

      // Scale Column (Algol tol then not needed )
      for (int i = m; i <= igh; i++) {
        scale += Math.abs(a[i - 1][m - 2]);
      }

      if (scale == 0.0) {
        continue;
      }

      for (int i = igh; i >= m; i--) {
        ort[i - 1] = a[i - 1][m - 2] / scale;
        final double oo = ort[i - 1]; 
        h += oo * oo;
      }

      final double a1 = Math.sqrt(h);
      final double g = ort[m - 1] >= 0.0 ? -Math.abs(a1) : Math.abs(a1);
      h -= ort[m - 1] * g;
      ort[m - 1] -= g;

      // (I - (U * U^T)/H) * A
      for (int j = m; j <= size; j++) {
        double f = 0.0;

        for (int i = igh; i >= m; i--) {
          f += ort[i - 1] * a[i - 1][j - 1];
        }

        f /= h;

        for (int i = m; i <= igh; i++) {
          a[i - 1][j - 1] -= f * ort[i - 1];
        }
      }

      // (I - (U * U^T)/H) * A * (I - (U * U^T)/H)
      for (int i = 1; i <= igh; i++) {
        double f = 0.0;

        final double[] ai = a[i - 1];
        for (int j = igh; j >= m; j--) {
          f += ort[j - 1] * ai[j - 1];
        }

        f /= h;

        for (int j = m; j <= igh; j++) {
          ai[j - 1] -= f * ort[j - 1];
        }
      }

      ort[m - 1] *= scale;
      a[m - 1][m - 2] = scale * g;
    }
  }

  /**
   * orthesによって直交同次変換を累積します。
   * 
   * @param a 直交変換に関する情報
   * @param low 開始番号
   * @param igh 終了番号
   * @param ort 直交変換に関する追加情報
   * @param z 直交同次変換を累積した行列
   */
  static void ortran(final double[][] a, final int low, final int igh, final double[] ort, final double[][] z) {
    final int size = a.length;

    // Initialize Z to identity matrix
    for (int i = 1; i <= size; i++) {
      for (int j = 1; j <= size; j++) {
        z[i - 1][j - 1] = 0.0;
      }
      z[i - 1][i - 1] = 1.0;
    }

    final int kl = igh - low - 1;

    if (kl < 1) {
      return;
    }

    for (int mp = igh - 1; mp >= low + 1; mp--) {
      if (a[mp - 1][mp - 2] == 0.0) {
        continue;
      }

      final int mp1 = mp + 1;

      for (int i = mp1; i <= igh; i++) {
        ort[i - 1] = a[i - 1][mp - 2];
      }

      for (int j = mp; j <= igh; j++) {
        double g = 0.0;

        for (int i = mp; i <= igh; i++) {
          g += ort[i - 1] * z[i - 1][j - 1];
        }

        /*
         * Divisor below is negative of H formed in orthes(). 
         * Double division avoids possible underflow.
         */
        g = (g / ort[mp - 1]) / a[mp - 1][mp - 2];

        for (int i = mp; i <= igh; i++) {
          z[i - 1][j - 1] += g * ort[i - 1];
        }
      }
    }
  }

  /**
   * 実上ヘッセンベルグ行列の固有値と固有ベクトルを求めます。
   * 
   * @param h 上ヘッセンベルグ行列
   * @param low 開始番号
   * @param igh 終了番号
   * @param wr 固有値の実部
   * @param wi 固有値の虚部
   * @param z 固有ベクトルの実部と虚部
   * @param schur シュアー分解のみを行うならばtrue
   * @return 計算結果
   */
  static int hqr2(final double[][] h, final int low, final int igh, final double[] wr, final double[] wi, final double[][] z, final boolean schur) {
    double p = 0; 
    double q = 0; 
    double r = 0;
    double s = 0; 
    double zz = 0;

    final int size = h.length;
    double x;
    double y = 0;
    double w = 0;

    final double meps = MACHEP;

    // Store roots isolated by balance() and compute matrix norm.
    double norm = 0.0;
    int k = 1;
    for (int i = 1; i <= size; i++) {
      for (int j = k; j <= size; j++) {
        norm += Math.abs(h[i - 1][j - 1]);
      }

      k = i;
      if (i < low || i > igh) {
        wr[i - 1] = h[i - 1][i - 1];
        wi[i - 1] = 0.0;
      }
    }
    int en = igh;
    double t = 0.0;
    int itn = 30 * size;

    // Serch for next eigenvalue

    // L60:
    do {
      if (en < low) {
        break; // goto L340
      }

      int its = 0;
      final int na = en - 1;
      final int enm2 = na - 1;

      // Look for single small sub-diagonal element
      // L70:
      boolean goto280;
      do {
        goto280 = false;
        int l;
        for (l = en; l >= low; l--) {
          if (l == low) {
            break;
          }
          s = Math.abs(h[l - 2][l - 2]) + Math.abs(h[l - 1][l - 1]);
          if (s == 0.0) {
            s = norm;
          }
          if (Math.abs(h[l - 1][l - 2]) <= meps * s) {
            break;
          }
        }

        // Form shift
        x = h[en - 1][en - 1];
        if (l == en) {
          break; // goto L270
        }
        y = h[na - 1][na - 1];
        w = h[en - 1][na - 1] * h[na - 1][en - 1];
        if (l == na) {
          goto280 = true; // goto L280 leave do loop
          break;
        }
        if (itn == 0) {
          return en;
        }

        // Form exceptional shift
        if (its != 0 && (its % 10) == 0) {
          t += x;
          for (int i = low; i <= en; i++) {
            h[i - 1][i - 1] -= x;
          }

          s = Math.abs(h[en - 1][na - 1]) + Math.abs(h[na - 1][enm2 - 1]);
          x = 0.75 * s;
          y = x;
          w = -0.4375 * s * s;
        }
        its++;
        itn--;

        //  Look for two consecutive small sub-diagonal elements
        int m;
        for (m = enm2; m >= l; m--) {
          zz = h[m - 1][m - 1];
          r = x - zz;
          s = y - zz;
          p = (r * s - w) / h[m][m - 1] + h[m - 1][m];
          q = h[m][m] - zz - r - s;
          r = h[m + 1][m];
          s = Math.abs(p) + Math.abs(q) + Math.abs(r);
          p /= s;
          q /= s;
          r /= s;
          if (m == l) {
            break;
          }
          if (Math.abs(h[m - 1][m - 2]) * (Math.abs(q) + Math.abs(r)) <= meps * Math.abs(p) * (Math.abs(h[m - 2][m - 2]) + Math.abs(zz) + Math.abs(h[m][m]))) {
            break;
          }
        }
        int mp2 = m + 2;
        for (int i = mp2; i <= en; i++) {
          h[i - 1][i - 3] = 0.0;
          if (i != mp2) {
            h[i - 1][i - 4] = 0.0;
          }
        }

        // Double QR step involving rows l to en and columns m to en
        for (k = m; k <= na; k++) { // 260
          boolean notlas = (k != na ? true : false);
          if (k != m) {
            p = h[k - 1][k - 2];
            q = h[k][k - 2];
            r = 0.0;
            if (notlas) {
              r = h[k + 1][k - 2];
            }
            x = Math.abs(p) + Math.abs(q) + Math.abs(r);
            if (x == 0.0) {
              continue; // goto L260; //260
            }
            p /= x;
            q /= x;
            r /= x;
          }
          double a = Math.sqrt(p * p + q * q + r * r);
          double b = p;
          s = (((b) >= 0.0) ? (Math.abs(a)) : (-Math.abs(a)));
          if (k != m) {
            h[k - 1][k - 2] = -s * x;
          } else if (l != m) {
            h[k - 1][k - 2] = -h[k - 1][k - 2];
          }
          p = p + s;
          x = p / s;
          y = q / s;
          zz = r / s;
          q /= p;
          r /= p;

          // Row modification
          int j;
          for (j = k; j <= size; j++) {
            p = h[k - 1][j - 1] + q * h[k][j - 1];
            if (notlas) {
              p += r * h[k + 1][j - 1];
              h[k + 1][j - 1] -= p * zz;
            }
            h[k][j - 1] -= p * y;
            h[k - 1][j - 1] -= p * x;
          }
          j = en > (k + 3) ? (k + 3) : en;

          // Column modification
          for (int i = 1; i <= j; i++) {
            p = x * h[i - 1][k - 1] + y * h[i - 1][k];
            if (notlas) {
              p += zz * h[i - 1][k + 1];
              h[i - 1][k + 1] -= p * r;
            }
            h[i - 1][k] -= p * q;
            h[i - 1][k - 1] -= p;

          }

          // Accumulate transformation
          for (int i = low; i <= igh; i++) {
            p = x * z[i - 1][k - 1] + y * z[i - 1][k];
            if (notlas) {
              p += zz * z[i - 1][k + 1];
              z[i - 1][k + 1] -= p * r;
            }
            z[i - 1][k] -= p * q;
            z[i - 1][k - 1] -= p;
          }
          // L260: continue;
        }
      } while (true); // goto L70;

      // L270:
      if (!goto280) {

        // One root found

        h[en - 1][en - 1] = x + t;
        wr[en - 1] = h[en - 1][en - 1];

        wi[en - 1] = 0.0;
        en = na;
        continue; // goto L60;
      }

      // Two roots found

      goto280 = false;
      // L280:

      p = (y - x) / 2.0;
      q = p * p + w;
      zz = Math.sqrt(Math.abs(q));
      h[en - 1][en - 1] = x + t;
      x = h[en - 1][en - 1];
      h[na - 1][na - 1] = y + t;

      // Real pair
      if (q >= 0.0) {
        double a = zz;
        zz = p + (((p) >= 0.0) ? (Math.abs(a)) : (-Math.abs(a)));
        wr[na - 1] = x + zz;
        wr[en - 1] = wr[na - 1];
        if (zz != 0.0) {
          wr[en - 1] = x - w / zz;
        }
        wi[na - 1] = 0.0;
        wi[en - 1] = 0.0;

        x = h[en - 1][na - 1];
        s = Math.abs(x) + Math.abs(zz);
        p = x / s;
        q = zz / s;
        r = Math.sqrt(p * p + q * q);
        p /= r;
        q /= r;

        // Row modification
        for (int j = na; j <= size; j++) {
          zz = h[na - 1][j - 1];
          h[na - 1][j - 1] = q * zz + p * h[en - 1][j - 1];
          h[en - 1][j - 1] = q * h[en - 1][j - 1] - p * zz;
        }

        // Column modification
        for (int i = 1; i <= en; i++) {
          zz = h[i - 1][na - 1];
          h[i - 1][na - 1] = q * zz + p * h[i - 1][en - 1];
          h[i - 1][en - 1] = q * h[i - 1][en - 1] - p * zz;
        }

        // Accumulate transformation
        for (int i = low; i <= igh; i++) {
          zz = z[i - 1][na - 1];
          z[i - 1][na - 1] = q * zz + p * z[i - 1][en - 1];
          z[i - 1][en - 1] = q * z[i - 1][en - 1] - p * zz;
        }
      } else {
        // Complex pair
        wr[na - 1] = x + p;
        wr[en - 1] = x + p;
        wi[na - 1] = zz;
        wi[en - 1] = -zz;
      }
      en = enm2;
    } while (true); // goto L60;

    // All roots found. Back substitute to find vectors of upper triangular form

    // L340:
    if (norm == 0.0) {
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

      if (q < 0.0) {
        goto710 = true; // goto L710;
      } else if (q > 0.0) {
        continue; // goto L800; to 800
      }
      // else if (q == 0.0); //goto L600;

      // Real vector

      // L600:
      if (!goto710) {
        int m = en;
        h[en - 1][en - 1] = 1.0;
        for (int i = na; i >= 1; i--) {
          w = h[i - 1][i - 1] - p;
          r = h[i - 1][en - 1];
          if (m <= na) {
            for (int j = m; j <= na; j++) {
              r += h[i - 1][j - 1] * h[j - 1][en - 1];
            }
          }

          if (wi[i - 1] < 0.0) {
            zz = w;
            s = r;
          } else if (wi[i - 1] == 0.0) {
            m = i;
            t = w;
            if (w == 0.0) {
              t = meps * norm;
            }
            h[i - 1][en - 1] = -r / t;
          } else {
            // Solve real equations
            m = i;
            x = h[i - 1][i];
            y = h[i][i - 1];
            q = (wr[i - 1] - p) * (wr[i - 1] - p) + wi[i - 1] * wi[i - 1];
            t = (x * s - zz * r) / q;
            h[i - 1][en - 1] = t;
            if (Math.abs(x) > Math.abs(zz)) {
              h[i][en - 1] = (-r - w * t) / x;
            } else {
              h[i][en - 1] = (-s - y * t) / zz;
            }
          }
        }
        continue; // goto L800;

        // Complex vector
      }
      goto710 = false;
      // L710:
      int m = na;
      if (Math.abs(h[en - 1][na - 1]) > Math.abs(h[na - 1][en - 1])) {
        h[na - 1][na - 1] = q / h[en - 1][na - 1];
        h[na - 1][en - 1] = -(h[en - 1][en - 1] - p) / h[en - 1][na - 1];
      } else {
        double xrr = (h[na - 1][na - 1] - p) * (h[na - 1][na - 1] - p) + q * q;
        double xzr = -q * h[na - 1][en - 1];
        double xzi = -h[na - 1][en - 1] * (h[na - 1][na - 1] - p);
        h[na - 1][na - 1] = xzr / xrr;
        h[na - 1][en - 1] = xzi / xrr;
      }
      h[en - 1][na - 1] = 0.0;
      h[en - 1][en - 1] = 1.0;
      int enm2 = na - 1;
      for (int i = enm2; i >= 1; i--) {
        w = h[i - 1][i - 1] - p;
        double ra = 0.0;
        double sa = h[i - 1][en - 1];
        for (int j = m; j <= na; j++) {
          ra += h[i - 1][j - 1] * h[j - 1][na - 1];
          sa += h[i - 1][j - 1] * h[j - 1][en - 1];
        }

        if (wi[i - 1] < 0.0) {
          zz = w;
          r = ra;
          s = sa;
        } else if (wi[i - 1] == 0.0) {
          m = i;
          double xrr = w * w + q * q;
          h[i - 1][na - 1] = -(ra * w + sa * q) / xrr;
          h[i - 1][en - 1] = (ra * q - sa * w) / xrr;
        } else {
          // Solve complex equation
          m = i;
          x = h[i - 1][i];
          y = h[i][i - 1];
          /*
           * #ifdef MACINTOSH
           * vr = (wr[i - 1]-p)*(wr[i - 1]-p);
           * vr += wi[i - 1]*wi[i - 1] - q*q;
           * #else
           */
          double vr = (wr[i - 1] - p) * (wr[i - 1] - p) + wi[i - 1] * wi[i - 1] - q * q;
          double vi = (wr[i - 1] - p) * 2.0 * q;
          if (vr == 0.0 && vi == 0.0) {
            vr = meps * norm * (Math.abs(w) + Math.abs(q) + Math.abs(x) + Math.abs(y) + Math.abs(zz));
          }
          double xzr = x * r - zz * ra + q * sa;
          double xzi = x * s - zz * sa - q * ra;
          double xrr = vr * vr + vi * vi;
          h[i - 1][na - 1] = (xzr * vr + xzi * vi) / xrr;
          h[i - 1][en - 1] = (-xzr * vi + xzi * vr) / xrr;
          if (Math.abs(x) > Math.abs(zz) + Math.abs(q)) {
            h[i][na - 1] = (-ra - w * h[i - 1][na - 1] + q * h[i - 1][en - 1]) / x;
            h[i][en - 1] = (-sa - w * h[i - 1][en - 1] - q * h[i - 1][na - 1]) / x;
          } else {
            xzr = -r - y * h[i - 1][na - 1];
            xzi = -s - y * h[i - 1][en - 1];
            xrr = zz * zz + q * q;
            h[i][na - 1] = (xzr * zz + xzi * q) / xrr;
            h[i][en - 1] = (-xzr * q + xzi * zz) / xrr;
          }
        }
      }
      // L800:
      continue;
    }


    // End back substitution. Vectors of isolated root.
    for (int i = 1; i <= size; i++) {
      if (i < low || i > igh) {
        for (int j = i; j <= size; j++) {
          z[i - 1][j - 1] = h[i - 1][j - 1];
        }
      }
    }

    // Multiply by transformation matrix to give vectors of original full matrix
    for (int j = size; j >= low; j--) {
      int m = j > igh ? igh : j;
      for (int i = low; i <= igh; i++) {
        zz = 0.0;
        for (k = low; k <= m; k++) {
          zz += z[i - 1][k - 1] * h[k - 1][j - 1];
        }
        z[i - 1][j - 1] = zz;
      }
    }

    // Set error -- No convergence to an eigenvalue after 30 iterations
    return 0;
  }

  /**
   * 実行列の成分をバランス化し、固有値をできる限り分離します。
   * 
   * @param a 入力:対象となる、出力:バランス化した行列
   * @param low 開始番号
   * @param igh 終了番号
   * @param scale スケーリング情報
   */
  static void balance(final double[][] a, final int[] low, final int[] igh, final double[] scale) {
    int iexc = 0;
    int m = 0;
    int j = 0;

    final int n = a.length;
    int k = 1;
    int l = n;

    boolean goto100 = true;
    boolean goto140 = false;

    // In-line procedure for row and column exchange.

    // L20
    do {
      boolean goto20 = false;

      if (goto100 == false) {
        scale[m - 1] = j;
        if (j != m) {
          for (int i = 0; i < l; i++) {
            final double tmp = a[i][j - 1];
            a[i][j - 1] = a[i][m - 1];
            a[i][m - 1] = tmp;
          }

          final double[] aj = a[j - 1];
          final double[] am = a[m - 1];
          for (int i = k - 1; i < n; i++) {
            final double tmp = a[j - 1][i];
            aj[i] = a[m - 1][i];
            am[i] = tmp;
          }
        }
        if (iexc == 2) {
          k++;
          goto140 = true; // goto L140;
        }

        if (goto140 == false) {
           // Search for rows isolating an eigenvalue and push them down.

          // ***** RETURN FROM THE METHOD *****

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
          final double[] aj = a[j - 1];
          for (int i = 1; i <= l; i++) {
            if (i != j && aj[i - 1] != 0.0) {
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

        // Search for columns isolating an eigenvalue and push them down.
      }
      // L140:
      goto140 = false;
      for (j = k; j <= l; j++) {
        boolean goto170 = false; // 170
        for (int i = k; i <= l; i++) {
          if (i != j && a[i - 1][j - 1] != 0.0) {
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

    // Now balance the submatrix in rows k to l
    for (int i = k - 1; i < l; i++) {
      scale[i] = 1.0;
    }

    // Iterative loop for norm reduction
    // L190
    boolean noconv;
    do {
      noconv = false;
      for (int i = k; i <= l; i++) {
        double r = 0;
        double c = 0.0;
        final double[] ai = a[i - 1];
        for (j = k; j <= l; j++) {
          if (j != i) {
            c += Math.abs(a[j - 1][i - 1]);
            r += Math.abs(ai[j - 1]);
          }
        }

        if (c * r != 0.0) {
          double g = r / RADIX;
          double f = 1.0;
          final double s = c + r;
          while (c < g) {
            f *= RADIX;
            c *= B2;
          }
          g = r * RADIX;
          while (c >= g) {
            f /= RADIX;
            c /= B2;
          }

          // Now balanc
          if (((c + r) / f) < (0.95 * s)) {
            g = 1.0 / f;
            scale[i - 1] *= f;
            noconv = true;
            for (j = k; j <= n; j++) {
              ai[j - 1] *= g;
            }
            for (j = 1; j <= l; j++) {
              a[j - 1][i - 1] *= f;
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
   * @param low 開始番号
   * @param igh 終了番号
   * @param scale スケーリング情報
   * @param m 逆変換する固有ベクトルの数
   * @param z 入力:対象となる固有ベクトル、出力:逆変換された固有ベクトル
   */
  static void balbak(final int low, final int igh, final double[] scale, final int m, final double[][] z) {
    final int n = z.length;

    if (m == 0) {
      return;
    }

    if (igh != low) {
      for (int i = low; i <= igh; i++) {
        final double s = scale[i - 1];
        final double[] zi = z[i - 1];
        for (int j = 1; j <= m; j++) {
          zi[j - 1] *= s;
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

      final int k = (int)scale[i - 1];
      if (k != i) {
        final double[] zi = z[i - 1];
        final double[] zk = z[k - 1];
        for (int j = 1; j <= m; j++) {
          final double tmp = zi[j - 1];
          zi[j - 1] = zk[j - 1];
          zk[j - 1] = tmp;
        }
      }
    }
  }

  /**
   * 上ヘッセンベルグ行列の固有値を求めます。
   * 
   * @param h 上ヘッセンベルグ行列
   * @param low 開始番号
   * @param igh 終了番号
   * @param wr 固有値の実部
   * @param wi 固有値の虚部
   * @return 計算結果
   */
  @SuppressWarnings("null")
  static int hqr(final double[][] h, final int low, final int igh, final double[] wr, final double[] wi) {
    double p = 0; 
    double q = 0; 
    double r = 0;
    double x, zz;

    final int n = h.length;
    double y = 0;
    double w = 0;

    double norm = 0.0;
    int k = 1;

    // Store roots isolated by balance() and compute matrix norm.
    for (int i = 1 - 1; i < n; i++) {
      final double[] hi = h[i];
      for (int j = k - 1; j < n; j++) {
        norm += Math.abs(hi[j]);
      }

      k = i + 1;
      if (i < low || i + 1 > igh) {
        wr[i] = hi[i];
        wi[i] = 0.0;
      }
    }
    int en = igh;
    double t = 0.0;
    int itn = 30 * n;

    // Serch for next eigenvalue
    // L60:
    do {
      if (en < low) {
        return 0;
      }

      int its = 0;
      final int na = en - 1;
      final int enm2 = na - 1;

      // Look for single small sub-diagonal element.
      // L70:
      boolean goto280 = false;
      do {
        int l;
        for (l = en - 1; l >= low - 1; l--) {
          final int lm = l - 1;

          if (l + 1 == low) {
            break;
          }

          double s = Math.abs(h[lm][lm]) + Math.abs(h[l][l]);
          if (s == 0.0) {
            s = norm;
          }
          if (Math.abs(h[l][lm]) <= MACHEP * s) {
            break;
          }
        }
        l = l + 1;

        // Form shift
        x = h[en - 1][en - 1];
        if (l == en) {
          break; // goto L270;
        }
        y = h[na - 1][na - 1];
        w = h[en - 1][na - 1] * h[na - 1][en - 1];
        if (l == na) {
          goto280 = true; // goto L280;
          break;
        }
        if (itn == 0) {
          return en;
        }

        // Form exceptional shift.
        if (its == 10 || its == 20) {
          t += x;
          for (int i = low - 1; i < en; i++) {
            h[i][i] -= x;
          }

          double s = Math.abs(h[en - 1][na - 1]) + Math.abs(h[na - 1][enm2 - 1]);
          x = 0.75 * s;
          y = x;
          w = -0.4375 * s * s;
        }
        its++;
        itn--;

        // Look for two consecutive small sub-diagonal elements
        int m;
        for (m = enm2; m >= l; m--) {
          final int m1 = m - 1;

          zz = h[m1][m1];
          r = x - zz;
          double s = y - zz;
          p = (r * s - w) / h[m][m1] + h[m1][m];
          q = h[m][m] - zz - r - s;
          r = h[m + 1][m];
          s = Math.abs(p) + Math.abs(q) + Math.abs(r);
          p /= s;
          q /= s;
          r /= s;
          if (m == l) {
            break;
          }
          if (Math.abs(h[m1][m - 2]) * (Math.abs(q) + Math.abs(r)) <= MACHEP * Math.abs(p) * (Math.abs(h[m - 2][m - 2]) + Math.abs(zz) + Math.abs(h[m][m]))) {
            break;
          }
        }

        final int mp2 = m + 2;
        for (int i = mp2 - 1; i < en; i++) {
          h[i][i - 2] = 0.0;
          if (i + 1 != mp2) {
            h[i][i - 3] = 0.0;
          }
        }
        
        // Double QR step involving rows l to en and columns m to en
        for (k = m; k <= na; k++) { // 260
          final int kp1 = k + 1;
          final int km1 = k - 1;

          final boolean notlas = (k != na ? true : false);
          if (k != m) {
            p = h[k - 1][k - 2];
            q = h[k][k - 2];
            r = 0.0;
            if (notlas) {
              r = h[k + 1][k - 2];
            }
            x = Math.abs(p) + Math.abs(q) + Math.abs(r);
            if (x == 0.0) {
              continue; // goto 260;
            }
            p /= x;
            q /= x;
            r /= x;
          }
          final double a = Math.sqrt(p * p + q * q + r * r);
          final double b = p;
          final double s = (((b) >= 0.0) ? (Math.abs(a)) : (-Math.abs(a)));
          if (k != m) {
            h[k - 1][k - 2] = -s * x; // /10/16/13:00
          } else if (l != m) {
            h[k - 1][k - 2] = -h[k - 1][k - 2];
          }
          p = p + s;
          x = p / s;
          y = q / s;
          zz = r / s;
          q /= p;
          r /= p;

          // Row modification
          int j;
          final double[] hkm = h[k - 1];
          final double[] hk = h[k];
          double[] hkp = null;
          if (notlas) {
            hkp = h[k + 1];
          }
          for (j = k - 1; j < en; j++) {
            p = hkm[j] + q * hk[j];
            if (notlas) {
              p += r * hkp[j];
              hkp[j] -= p * zz;
            }
            hk[j] -= p * y;
            hkm[j] -= p * x;
          }
          j = j + 1;

          j = en > (k + 3) ? (k + 3) : en;

          // Column modification
          for (int i = l - 1; i < j; i++) {
            p = x * h[i][km1] + y * h[i][k];
            if (notlas) {
              p += zz * h[i][kp1];
              h[i][kp1] -= p * r;
            }
            h[i][k] -= p * q;
            h[i][km1] -= p;
          }
          // L260: continue;
        }
      } while (true); // goto L70;

      if (goto280 == false) {
        // One root found

        // L270:
        wr[en - 1] = x + t;
        wi[en - 1] = 0.0;
        en = na;
        continue; // goto L60;
      }
      goto280 = false;


      // Two roots found

      // L280:
      p = (y - x) / 2.0;
      q = p * p + w;
      zz = Math.sqrt(Math.abs(q));
      x = x + t;

      // Real pair
      if (q >= 0.0) {
        final double a = zz;
        zz = p + (((p) >= 0.0) ? (Math.abs(a)) : (-Math.abs(a)));
        wr[na - 1] = x + zz;
        wr[en - 1] = wr[na - 1];
        if (zz != 0.0) {
          wr[en - 1] = x - w / zz;
        }
        wi[na - 1] = 0.0;
        wi[en - 1] = 0.0;
      } else {
        // Ccomplex pair
        wr[na - 1] = x + p;
        wr[en - 1] = x + p;
        wi[na - 1] = zz;
        wi[en - 1] = -zz;
      }

      // Set error -- No convergence to an eigenvalue after 30 iterrations.

      en = enm2;
    } while (true); // goto L60;
  }

  /**
   * ベクトルを正規化します。
   * 
   * @param valr 固有値の実部
   * @param vali 固有値の虚部
   * @param vecr 固有ベクトルの実部
   * @param veci 固有ベクトルの虚部
   */
  static void normalizeVector(final double[] valr, final double[] vali, final double[][] vecr, final double[][] veci) {
    final int size = valr.length;

    for (int i = 1; i <= size; i++) {
      if (vali[i - 1] == 0.0) {
        double dd = 0;
        for (int j = 1; j <= size; j++) {
          final double vv = vecr[j - 1][i - 1];
          dd += vv * vv;
        }
        dd = Math.sqrt(dd);
        for (int j = 1; j <= size; j++) {
          vecr[j - 1][i - 1] /= dd;
        }
      } else {
        double dd = 0;
        for (int j = 1; j <= size; j++) {
          final double vvr = vecr[j - 1][i - 1];
          final double vvi = veci[j - 1][i - 1];
          dd += vvr * vvr + vvi * vvi;
        }
        dd = Math.sqrt(dd);

        for (int j = 1; j <= size; j++) {
          vecr[j - 1][i - 1] /= dd;
          veci[j - 1][i - 1] /= dd;
        }
      }
    }
  }

}