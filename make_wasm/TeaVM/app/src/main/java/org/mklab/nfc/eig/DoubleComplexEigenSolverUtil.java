/*
 * $Id: DoubleComplexEigenUtil.java,v 1.5 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.scalar.DoubleNumberUtil;


/**
 * 倍精度(double)型の複素行列の固有値を求めるためのユーティリティクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.5 $
 */
final class DoubleComplexEigenSolverUtil {
  /**
   * 新しく生成された<code>DoubleComplexEigenSolverUtil</code>オブジェクトを初期化します。
   */
  private DoubleComplexEigenSolverUtil() {
    // nothing to do
  }

  /** 基数。 */
  private static final double RADIX = 2;
  /** 基数の2乗。 */
  private static final double B2 = RADIX * RADIX;
  /** 計算機イプシロン。 */
  private static final double MACHEP = DoubleNumberUtil.EPS;

  /**
   * 複素数の平方根を返します。
   * 
   * @param re 複素数の実部
   * @param im 複素数の虚部
   * @param or 求めた平方根の実部
   * @param oi 求めた平方根の虚部
   */
  private static void csqrt(final double re, final double im, final double[] or, final double[] oi) {
    final double w = Math.sqrt(re * re + im * im);

    or[0] = Math.sqrt(Math.abs((w + re) * 0.5));
    oi[0] = Math.sqrt(Math.abs((w - re) * 0.5));

    if (im < 0.0) {
      oi[0] = -oi[0];
    }
  }

  /**
   * 複素数の割り算を行います。
   * 
   * @param re1 割られる複素数の実部
   * @param im1 割られる複素数の虚部
   * @param re2 割る複素数の実部
   * @param im2 割る複素数の虚部
   * @param or 割り算の結果の実部
   * @param oi 割り算の結果の虚部
   */
  private static void cdiv(final double re1, final double im1, final double re2, final double im2, final double[] or, final double[] oi) {
    if (re2 == 0.0 && im2 == 0.0) {
      throw new RuntimeException(Messages.getString("DoubleComplexEigenUtil.0")); //$NON-NLS-1$
    }

    if (Math.abs(re2) > Math.abs(im2)) {
      double d = im2 / re2;
      double s = re2 + im2 * d;
      or[0] = (re1 + im1 * d) / s;
      oi[0] = (-re1 * d + im1) / s;
    } else {
      double d = re2 / im2;
      double s = re2 * d + im2;
      or[0] = (re1 * d + im1) / s;
      oi[0] = (-re1 + im1 * d) / s;
    }
  }

  /**
   * 一般の複素行列をバランス化し、できるだけ固有値が分離するようにします。
   * 
   * @param ar 実部行列
   * @param ai 虚部行列
   * @param low 開始番号
   * @param igh 終了番号
   * @param scale スケーリング情報
   */
  static void cbal(final double[][] ar, final double[][] ai, final int[] low, final int[] igh, final double[] scale) {
    int m = 0, iexc = 0;
    int j = 0;

    final int size = ar.length;
    int k = 1;
    int l = size;

    boolean goto100 = true; // goto L100;
    // L20:
    do {
      boolean goto20 = false;
      boolean goto140 = false;

      if (!goto100) {
        scale[m - 1] = j;

        if (j != m) {
          for (int i = 1; i <= l; i++) {
            // ComplexValueSwap(AC(i,j), AC(i,m));
            double tmpr = ar[i - 1][j - 1]; // //11/07/15:55
            double tmpi = ai[i - 1][j - 1]; // //11/07/15:55
            ar[i - 1][j - 1] = ar[i - 1][m - 1]; // //11/07/15:55
            ai[i - 1][j - 1] = ai[i - 1][m - 1]; // //11/07/15:55//11/08/17:34
            ar[i - 1][m - 1] = tmpr; // //11/07/15:55
            ai[i - 1][m - 1] = tmpi; // //11/07/15:55
          }
          for (int i = k; i <= size; i++) {
            // ComplexValueSwap(AC(j,i), AC(m,i));
            double tmpr = ar[j - 1][i - 1];
            double tmpi = ai[j - 1][i - 1];
            ar[j - 1][i - 1] = ar[m - 1][i - 1];
            ai[j - 1][i - 1] = ai[m - 1][i - 1];
            ar[m - 1][i - 1] = tmpr;
            ai[m - 1][i - 1] = tmpi;
          }
        }

        if (iexc == 2) {
          k++;
          goto140 = true; // goto L140;
        }

        if (!goto140) {
          if (l == 1) { // 11/12/10:23
            // goto L280;
            low[0] = k;
            igh[0] = l;
            return;
          }

          l--;
        }
      }
      goto100 = false;
      if (!goto140) {
        // L100:
        for (int jj = 1; jj <= l; jj++) {
          boolean goto120 = false;
          j = l + 1 - jj;
          for (int i = 1; i <= l; i++) {
            if (i == j) {
              continue;
            }

            // if (ComplexValueAbs(AC(j,i)) != 0.0)
            if (complexAbs(ar[j - 1][i - 1], ai[j - 1][i - 1]) != 0.0) {
              goto120 = true; // goto L120;
              break;
            }
          }
          if (goto120) {
            continue;
          }
          m = l;
          iexc = 1;
          goto20 = true; //
          break; // goto L20; /// up
          // L120: continue;
        }
        if (goto20) {
          continue;
        }
      }
      goto140 = false;

      // L140:
      for (j = k; j <= l; j++) { // / L170
        boolean goto170 = false;
        for (int i = k; i <= l; i++) {
          if (i == j) {
            continue;
          }

          // if (ComplexValueAbs(AC(i,j)) != 0.0)
          if (complexAbs(ar[i - 1][j - 1], ai[i - 1][j - 1]) != 0.0) { // 11/08/17:59
            goto170 = true; // goto L170;
            break;
          }
        }
        if (goto170) {
          // if (!goto170) { // 2004.5.14 by M.Koga
          continue; // ///break;
        }

        m = k;
        iexc = 2;
        goto20 = true;
        break; // goto L20; /// up
        // L170: continue;
      }
      if (goto20) {
        continue; // /11/08/18:11
      }

      break; // 11/7/14:10
    } while (true); // to L20

    for (int i = k; i <= l; i++) {
      scale[i - 1] = 1.0;
    }
    // L190:
    boolean noconv;
    do {
      noconv = false;

      for (int i = k; i <= l; i++) {
        double c = 0.0;
        double r = 0.0;

        for (j = k; j <= l; j++) {
          if (j == i) {
            continue;
          }

          c += Math.abs(ar[j - 1][i - 1]) + Math.abs(ai[j - 1][i - 1]);
          r += Math.abs(ar[i - 1][j - 1]) + Math.abs(ai[i - 1][j - 1]);
        }
        if (c == 0.0 || r == 0.0) {
          continue;
        }

        double g = r / RADIX;
        double f = 1.0;
        double s = c + r;
        while (c < g) {
          f *= RADIX;
          c *= B2;
        }
        g = r * RADIX;
        while (c >= g) {
          f /= RADIX;
          c /= B2;
        }
        if ((c + r) / f >= (0.95 * s)) {
          continue;
        }
        g = 1.0 / f;
        scale[i - 1] *= f;
        noconv = true;

        for (j = k; j <= size; j++) {
          // ComplexValueMulSelf2(AC(i,j), g);
          ar[i - 1][j - 1] *= g;
          ai[i - 1][j - 1] *= g;
        }
        for (j = 1; j <= l; j++) {
          // ComplexValueMulSelf2(AC(j,i), f);
          ar[j - 1][i - 1] *= f;
          ai[j - 1][i - 1] *= f;
        }
      }
      // if (noconv) goto L190; /// up
    } while (noconv);
    // L280:
    low[0] = k;
    igh[0] = l;
  }

  /**
   * 一般の複素行列をユニタリ変換により上ヘッセンベルグ行列へ変換します。
   * 
   * @param ar 実部行列
   * @param ai 虚部行列
   * @param low 開始番号
   * @param igh 終了番号
   * @param ortr 変換に関する実部の追加情報
   * @param orti 変換に関する虚部の追加情報
   */
  static void corth(final double[][] ar, final double[][] ai, final int low, final int igh, final double[] ortr, final double[] orti) {
    int size = ar.length;

    int la = igh - 1;
    int kp1 = low + 1;

    if (la < kp1) {
      return;
    }

    int m;
    for (m = kp1; m <= la; m++) {
      ortr[m - 1] = 0.0;
      orti[m - 1] = 0.0;

      double scale = 0.0;
      for (int i = m; i <= igh; i++) {
        scale += Math.abs(ar[i - 1][m - 2]) + Math.abs(ai[i - 1][m - 2]);
      }

      if (scale == 0.0) {
        continue;
      }

      int mp = m + igh;

      double h = 0.0;
      for (int ii = m; ii <= igh; ii++) {
        int i = mp - ii;
        ortr[i - 1] = ar[i - 1][m - 2] / scale;
        orti[i - 1] = ai[i - 1][m - 2] / scale;
        h += ortr[i - 1] * ortr[i - 1] + orti[i - 1] * orti[i - 1];
      }

      double g = Math.sqrt(h);
      double f = Math.sqrt(ortr[m - 1] * ortr[m - 1] + orti[m - 1] * orti[m - 1]);

      if (f != 0.0) {
        h += f * g;
        g /= f;
        ortr[m - 1] *= (1.0 + g);
        orti[m - 1] *= (1.0 + g);
      } else {
        ortr[m - 1] = g;
        ar[m - 1][m - 2] = scale;
      }

      for (int j = m; j <= size; j++) {
        double fr = 0.0;
        double fi = 0.0;
        for (int ii = m; ii <= igh; ii++) {
          int i = mp - ii;
          fr += ortr[i - 1] * ar[i - 1][j - 1] + orti[i - 1] * ai[i - 1][j - 1];
          fi += ortr[i - 1] * ai[i - 1][j - 1] - orti[i - 1] * ar[i - 1][j - 1];
        }
        fr /= h;
        fi /= h;
        for (int i = m; i <= igh; i++) {
          ar[i - 1][j - 1] -= fr * ortr[i - 1] - fi * orti[i - 1];
          ai[i - 1][j - 1] -= fr * orti[i - 1] + fi * ortr[i - 1];
        }
      }

      for (int i = 1; i <= igh; i++) {
        double fr = 0.0;
        double fi = 0.0;
        for (int jj = m; jj <= igh; jj++) {
          int j = mp - jj;
          fr += ortr[j - 1] * ar[i - 1][j - 1] - orti[j - 1] * ai[i - 1][j - 1];
          fi += ortr[j - 1] * ai[i - 1][j - 1] + orti[j - 1] * ar[i - 1][j - 1];
        }

        fr /= h;
        fi /= h;
        for (int j = m; j <= igh; j++) {
          ar[i - 1][j - 1] -= fr * ortr[j - 1] + fi * orti[j - 1];
          ai[i - 1][j - 1] += fr * orti[j - 1] - fi * ortr[j - 1];
        }
      }

      ortr[m - 1] *= scale;
      orti[m - 1] *= scale;
      ar[m - 1][m - 2] *= -g;
      ai[m - 1][m - 2] *= -g;
    }
  }

  /**
   * 直交相似変換の累積を求めます。
   * 
   * @param ar ユニタリ変換に関する実部の情報
   * @param ai ユニタリ変換に関する虚部の情報
   * @param low 開始番号
   * @param igh 終了番号
   * @param ortr 変換に関する実部の追加情報
   * @param orti 変換に関する虚部の追加情報
   * @param zr 固有ベクトルの実部
   * @param zi 固有ベクトルの虚部
   * @param m 逆変換されるべき固有ベクトルの数
   */
  static void cortb(final double[][] ar, final double[][] ai, final int low, final int igh, final double[] ortr, final double[] orti, final double[][] zr, final double[][] zi, final int m) {
    int la = igh - 1;
    int kp1 = low + 1;

    if (la < kp1) {
      return;
    }

    for (int mp = igh - 1; mp >= low + 1; mp--) {
      if (ar[mp - 1][mp - 2] == 0.0 && ai[mp - 1][mp - 2] == 0.0) {
        continue;
      }

      /*
       * H below is negative of H formed in corth().
       */
      double h = ar[mp - 1][mp - 2] * ortr[mp - 1] + ai[mp - 1][mp - 2] * orti[mp - 1];
      int mp1 = mp + 1;

      for (int i = mp1; i <= igh; i++) {
        ortr[i - 1] = ar[i - 1][mp - 2];
        orti[i - 1] = ai[i - 1][mp - 2];
      }

      for (int j = 1; j <= m; j++) {
        double gr = 0.0;
        double gi = 0.0;

        for (int i = mp; i <= igh; i++) {
          gr += ortr[i - 1] * zr[i - 1][j - 1] + orti[i - 1] * zi[i - 1][j - 1];
          gi += ortr[i - 1] * zi[i - 1][j - 1] - orti[i - 1] * zr[i - 1][j - 1];
        }

        gr /= h;
        gi /= h;

        for (int i = mp; i <= igh; i++) {
          zr[i - 1][j - 1] += gr * ortr[i - 1] - gi * orti[i - 1];
          zi[i - 1][j - 1] += gr * orti[i - 1] + gi * ortr[i - 1];
        }
      }
    }

  }

  /**
   * 複素上ヘッセンベルグ行列の固有値と固有ベクトルを求めます。
   * 
   * @param hr 複素上ヘッセンベルグ行列の実部
   * @param hi 複素上ヘッセンベルグ行列の虚部
   * @param low 開始番号
   * @param igh 終了番号
   * @param ortr ユニタリ変換の実部の情報
   * @param orti ユニタリ変換の虚部の情報
   * @param wr 固有値の実部
   * @param wi 固有値の虚部
   * @param zr 固有ベクトルの実部
   * @param zi 固有ベクトルの虚部
   * @param schur シュアー分解のみを求めるならばtrue
   * @return 計算結果
   */
  static int comqr2(final double[][] hr, final double[][] hi, final int low, final int igh, final double[] ortr, final double[] orti, final double[] wr, final double[] wi, final double[][] zr, final double[][] zi, final boolean schur) {
    int l = 0;
    double[] z3r = new double[1];
    double[] z3i = new double[1];

    int size = hr.length;

    double machep = MACHEP;
    int ierr = 0;

    /*
     * Initialize eigenvector matrix.
     */
    for (int i = 1; i <= size; i++) {
      for (int j = 1; j <= size; j++) {
        zr[i - 1][j - 1] = 0.0;
        zi[i - 1][j - 1] = 0.0;
        if (i == j) {
          zr[i - 1][j - 1] = 1.0;
        }
      }
    }

    /*
     * Form the matrix of accumulated transformations from the information left by corth().
     */
    int iend = igh - low - 1;
    boolean goto180 = false;
    boolean goto150 = false;
    if (iend < 0) {
      goto180 = true; // goto L180;
    } else if (iend == 0) {
      goto150 = true; // goto L150;
    }

    if (!goto180) {

      if (!goto150) {

        /*
         * for i=igh-1 step -1 until low+1 do --
         */
        for (int ii = 1; ii <= iend; ii++) {
          int i = igh - ii;
          if (ortr[i - 1] == 0.0 && orti[i - 1] == 0.0) {
            continue;
          }
          if (hr[i - 1][i - 2] == 0.0 && hi[i - 1][i - 2] == 0.0) {
            continue;
          }

          /*
           * Norm below is negative of H formed in corth().
           */
          double norm = hr[i - 1][i - 2] * ortr[i - 1] + hi[i - 1][i - 2] * orti[i - 1];
          int ip1 = i + 1;
          for (int k = ip1; k <= igh; k++) {
            ortr[k - 1] = hr[k - 1][i - 2];
            orti[k - 1] = hi[k - 1][i - 2];
          }
          for (int j = i; j <= igh; j++) {
            double sr = 0.0;
            double si = 0.0;
            for (int k = i; k <= igh; k++) {
              sr += ortr[k - 1] * zr[k - 1][j - 1] + orti[k - 1] * zi[k - 1][j - 1];
              si += ortr[k - 1] * zi[k - 1][j - 1] - orti[k - 1] * zr[k - 1][j - 1];
            }
            sr /= norm;
            si /= norm;
            for (int k = i; k <= igh; k++) {
              zr[k - 1][j - 1] += sr * ortr[k - 1] - si * orti[k - 1];
              zi[k - 1][j - 1] += sr * orti[k - 1] + si * ortr[k - 1];
            }
          }
        }
      }
      goto150 = false;

      /*
       * Create real subdiagonal elements.
       */
      // L150:
      l = low + 1;
      for (int i = l; i <= igh; i++) {
        int ll = Math.min(i + 1, igh);
        if (hi[i - 1][i - 2] == 0.0) {
          continue;
        }

        double norm = Math.sqrt(hr[i - 1][i - 2] * hr[i - 1][i - 2] + hi[i - 1][i - 2] * hi[i - 1][i - 2]);
        double yr = hr[i - 1][i - 2] / norm;
        double yi = hi[i - 1][i - 2] / norm;
        hr[i - 1][i - 2] = norm;
        hi[i - 1][i - 2] = 0.0;
        for (int j = i; j <= size; j++) {
          double si = yr * hi[i - 1][j - 1] - yi * hr[i - 1][j - 1];
          hr[i - 1][j - 1] = yr * hr[i - 1][j - 1] + yi * hi[i - 1][j - 1];
          hi[i - 1][j - 1] = si;
        }
        for (int j = 1; j <= ll; j++) {
          double si = yr * hi[j - 1][i - 1] + yi * hr[j - 1][i - 1];
          hr[j - 1][i - 1] = yr * hr[j - 1][i - 1] - yi * hi[j - 1][i - 1];
          hi[j - 1][i - 1] = si;
        }
        for (int j = low; j <= igh; j++) {
          double si = yr * zi[j - 1][i - 1] + yi * zr[j - 1][i - 1];
          zr[j - 1][i - 1] = yr * zr[j - 1][i - 1] - yi * zi[j - 1][i - 1];
          zi[j - 1][i - 1] = si;
        }
      }

    }
    goto180 = false;

    /*
     * Store roots isolated by cbal()
     */
    // L180:
    for (int i = 1; i <= size; i++) {
      if (i >= low && i <= igh) {
        continue;
      }
      wr[i - 1] = hr[i - 1][i - 1];
      wi[i - 1] = hi[i - 1][i - 1];
    }
    int en = igh;
    double tr = 0.0;
    double ti = 0.0;

    /*
     * Search for next eigenvalue
     */
    // L220:
    do {
      if (en < low) {
        break; // goto L680;
      }
      int its = 0;
      int itn = 30 * size;
      int enm1 = en - 1;

      /*
       * Look for single small sub-diagonal element for l=en step -1 until low
       * do
       */
      // L240:
      do {
        for (int ll = low; ll <= en; ll++) {
          l = en + low - ll;
          if (l == low) {
            break;
          }
          if (Math.abs(hr[l - 1][l - 2]) <= machep * (Math.abs(hr[l - 2][l - 2]) + Math.abs(hi[l - 2][l - 2]) + Math.abs(hr[l - 1][l - 1]) + Math.abs(hi[l - 1][l - 1]))) {
            break;
          }
        }

        /*
         * Form shift
         */

        if (l == en) {
          break; // goto L660;
        }

        if (itn == 0) { // goto L1000;
          // L1000:
          ierr = en;
          return ierr;
        }

        double sr;
        double si;
        if (its != 0 && (its % 10) == 0) {
          /*
           * Form exceptional shift
           */
          sr = Math.abs(hr[en - 1][enm1 - 1]) + Math.abs(hr[enm1 - 1][en - 3]);
          si = 0.0;
        } else {
          sr = hr[en - 1][en - 1];
          si = hi[en - 1][en - 1];
          double xr = hr[enm1 - 1][en - 1] * hr[en - 1][enm1 - 1];
          double xi = hi[enm1 - 1][en - 1] * hr[en - 1][enm1 - 1];
          if (!(xr == 0.0 && xi == 0.0)) {
            double yr = (hr[enm1 - 1][enm1 - 1] - sr) / 2.0;
            double yi = (hi[enm1 - 1][enm1 - 1] - si) / 2.0;
            csqrt(yr * yr - yi * yi + xr, 2.0 * yr * yi + xi, z3r, z3i);
            double zzr = z3r[0];
            double zzi = z3i[0];
            if (yr * zzr + yi * zzi < 0.0) {
              zzr = -zzr;
              zzi = -zzi;
            }
            cdiv(xr, xi, yr + zzr, yi + zzi, z3r, z3i);
            sr -= z3r[0];
            si -= z3i[0];
          }
        }

        for (int i = low; i <= en; i++) {
          hr[i - 1][i - 1] -= sr;
          hi[i - 1][i - 1] -= si;
        }

        tr += sr;
        ti += si;
        its++;
        itn--;

        /*
         * Reduce to triangle (rows).
         */
        int lp1 = l + 1;
        for (int i = lp1; i <= en; i++) {
          sr = hr[i - 1][i - 2];
          hr[i - 1][i - 2] = 0.0;
          double norm = Math.sqrt(hr[i - 2][i - 2] * hr[i - 2][i - 2] + hi[i - 2][i - 2] * hi[i - 2][i - 2] + sr * sr);
          double xr = hr[i - 2][i - 2] / norm;
          wr[i - 2] = xr;
          double xi = hi[i - 2][i - 2] / norm;
          wi[i - 2] = xi;
          hr[i - 2][i - 2] = norm;
          hi[i - 2][i - 2] = 0.0;
          hi[i - 1][i - 2] = sr / norm;

          for (int j = i; j <= size; j++) {
            double yr = hr[i - 2][j - 1];
            double yi = hi[i - 2][j - 1];
            double zzr = hr[i - 1][j - 1];
            double zzi = hi[i - 1][j - 1];
            hr[i - 2][j - 1] = xr * yr + xi * yi + hi[i - 1][i - 2] * zzr;
            hi[i - 2][j - 1] = xr * yi - xi * yr + hi[i - 1][i - 2] * zzi;
            hr[i - 1][j - 1] = xr * zzr - xi * zzi - hi[i - 1][i - 2] * yr;
            hi[i - 1][j - 1] = xr * zzi + xi * zzr - hi[i - 1][i - 2] * yi;
          }
        }
        si = hi[en - 1][en - 1];
        if (si != 0.0) {
          double norm = Math.sqrt(hr[en - 1][en - 1] * hr[en - 1][en - 1] + si * si);
          sr = hr[en - 1][en - 1] / norm;
          si /= norm;
          hr[en - 1][en - 1] = norm;
          hi[en - 1][en - 1] = 0.0;
          if (en != size) {
            int ip1 = en + 1;
            for (int j = ip1; j <= size; j++) {
              double yr = hr[en - 1][j - 1];
              double yi = hi[en - 1][j - 1];
              hr[en - 1][j - 1] = sr * yr + si * yi;
              hi[en - 1][j - 1] = sr * yi - si * yr;
            }
          }
        }

        /*
         * Inverse operation (columns).
         */
        for (int j = lp1; j <= en; j++) {
          double xr = wr[j - 2];
          double xi = wi[j - 2];

          for (int i = 1; i <= j; i++) {
            double yr = hr[i - 1][j - 2];
            double yi = 0.0;
            double zzr = hr[i - 1][j - 1];
            double zzi = hi[i - 1][j - 1];
            if (i != j) {
              yi = hi[i - 1][j - 2];
              hi[i - 1][j - 2] = xr * yi + xi * yr + hi[j - 1][j - 2] * zzi;
            }
            hr[i - 1][j - 2] = xr * yr - xi * yi + hi[j - 1][j - 2] * zzr;
            hr[i - 1][j - 1] = xr * zzr + xi * zzi - hi[j - 1][j - 2] * yr;
            hi[i - 1][j - 1] = xr * zzi - xi * zzr - hi[j - 1][j - 2] * yi;
          }
          for (int i = low; i <= igh; i++) {
            double yr = zr[i - 1][j - 2];
            double yi = zi[i - 1][j - 2];
            double zzr = zr[i - 1][j - 1];
            double zzi = zi[i - 1][j - 1];
            zr[i - 1][j - 2] = xr * yr - xi * yi + hi[j - 1][j - 2] * zzr;
            zi[i - 1][j - 2] = xr * yi + xi * yr + hi[j - 1][j - 2] * zzi;
            zr[i - 1][j - 1] = xr * zzr + xi * zzi - hi[j - 1][j - 2] * yr;
            zi[i - 1][j - 1] = xr * zzi - xi * zzr - hi[j - 1][j - 2] * yi;
          }
        }
        if (si == 0.0) {
          continue; // goto L240;
        }
        for (int i = 1; i <= en; i++) {
          double yr = hr[i - 1][en - 1];
          double yi = hi[i - 1][en - 1];
          hr[i - 1][en - 1] = sr * yr - si * yi;
          hi[i - 1][en - 1] = sr * yi + si * yr;
        }
        for (int i = low; i <= igh; i++) {
          double yr = zr[i - 1][en - 1];
          double yi = zi[i - 1][en - 1];
          zr[i - 1][en - 1] = sr * yr - si * yi;
          zi[i - 1][en - 1] = sr * yi + si * yr;
        }
        // break point
        // new RealMatrix(n,n,zr).print("zr");
        // new RealMatrix(n,n,zi).print("zi");
        // new RealMatrix(n,n,hr).print("hr");
        // new RealMatrix(n,n,hi).print("hi");
        // new RealMatrix(wr).print("wr");
        // new RealMatrix(wi).print("wi");

      } while (true); // goto L240;

      /*
       * A root found.
       */

      // L660:
      hr[en - 1][en - 1] += tr;
      wr[en - 1] = hr[en - 1][en - 1];
      hi[en - 1][en - 1] += ti;
      wi[en - 1] = hi[en - 1][en - 1];
      en = enm1;

      // new RealMatrix(wr).print("wr");
      // new RealMatrix(wi).print("wi");
      // new RealMatrix(n,n,zr).print("zr");
      // new RealMatrix(n,n,zi).print("zi");
      // new RealMatrix(n,n,hr).print("hr");
      // new RealMatrix(n,n,hi).print("hi");

    } while (true); // goto L220; //up

    /*
     * All roots found. Backsubstitute to find vectors of upper triangular form.
     */
    // L680:
    // do {
    if (schur == true) {
      return ierr; // break;// goto L1001;
    }

    double norm = 0.0;

    for (int i = 1; i <= size; i++) {
      for (int j = i; j <= size; j++) {
        norm += Math.abs(hr[i - 1][j - 1]) + Math.abs(hi[i - 1][j - 1]);
      }
    }
    if (size == 1 || norm == 0.0) {
      return ierr; // break; //goto L1001;
    }

    /*
     * For en=n step -1 until 2 do --.
     */
    for (int nn = 2; nn <= size; nn++) {
      en = size + 2 - nn;
      double xr = wr[en - 1];
      double xi = wi[en - 1];
      int enm1 = en - 1;

      /*
       * For i=en-1 step -1 until 1 do --.
       */
      for (int ii = 1; ii <= enm1; ii++) {
        int i = en - ii;
        double zzr = hr[i - 1][en - 1];
        double zzi = hi[i - 1][en - 1];
        if (i != enm1) {
          int ip1 = i + 1;
          for (int j = ip1; j <= enm1; j++) {
            zzr += hr[i - 1][j - 1] * hr[j - 1][en - 1] - hi[i - 1][j - 1] * hi[j - 1][en - 1];
            zzi += hr[i - 1][j - 1] * hi[j - 1][en - 1] + hi[i - 1][j - 1] * hr[j - 1][en - 1];
          }
        }
        double yr = xr - wr[i - 1];
        double yi = xi - wi[i - 1];
        if (yr == 0.0 && yi == 0.0) {
          yr = machep * norm;
        }
        cdiv(zzr, zzi, yr, yi, z3r, z3i);
        hr[i - 1][en - 1] = z3r[0];
        hi[i - 1][en - 1] = z3i[0];
      }
    }

    /*
     * End backsubstitution.
     */
    int enm1 = size - 1;

    /*
     * Vectors of isolated roots.
     */
    for (int i = 1; i <= enm1; i++) {
      if (i >= low && i <= igh) {
        continue;
      }

      int ip1 = i + 1;
      for (int j = ip1; j <= size; j++) {
        zr[i - 1][j - 1] = hr[i - 1][j - 1];
        zi[i - 1][j - 1] = hi[i - 1][j - 1];
      }
    }

    /*
     * Multiply by transformation matrix to give vectors of original full
     * matrix. For j=n step -1 until low+1 do --.
     */
    for (int jj = low; jj <= enm1; jj++) {
      int j = size + low - jj;
      int m = Math.min(j - 1, igh);
      for (int i = low; i <= igh; i++) {
        double zzr = zr[i - 1][j - 1];
        double zzi = zi[i - 1][j - 1];
        for (int k = low; k <= m; k++) {
          zzr += zr[i - 1][k - 1] * hr[k - 1][j - 1] - zi[i - 1][k - 1] * hi[k - 1][j - 1];
          zzi += zr[i - 1][k - 1] * hi[k - 1][j - 1] + zi[i - 1][k - 1] * hr[k - 1][j - 1];
        }
        zr[i - 1][j - 1] = zzr;
        zi[i - 1][j - 1] = zzi;
      }
    }
    return ierr; // break;//goto L1001;

    /*
     * Set error -- No convergence to an eigenvalu after 30 iterations.
     */
  }

  /**
   * 固有ベクトルを逆変換します。
   * 
   * @param low 開始番号
   * @param igh 終了番号
   * @param scale スケーリング情報
   * @param m 逆変換する固有ベクトルの数
   * @param zr 変換後の固有ベクトルの実部
   * @param zi 変換後の固有ベクトルの虚部
   */
  static void cbabk2(final int low, final int igh, final double[] scale, final int m, final double[][] zr, final double[][] zi) {
    int size = zr.length;

    if (m == 0) {
      return;
    }

    if (igh != low) {
      for (int i = low; i <= igh; i++) {
        double s = scale[i - 1];
        for (int j = 1; j <= m; j++) {
          zr[i - 1][j - 1] *= s;
          zi[i - 1][j - 1] *= s;
        }
      }
    }

    for (int ii = 1; ii <= size; ii++) {
      int i = ii;
      if (i >= low && i <= igh) {
        continue;
      }
      if (i < low) {
        i = low - ii;
      }

      int k = (int)scale[i - 1];

      if (k == i) {
        continue;
      }

      for (int j = 1; j <= m; j++) {
        double s = zr[i - 1][j - 1];
        zr[i - 1][j - 1] = zr[k - 1][j - 1];
        zr[k - 1][j - 1] = s; // //////11/07/14:58
        s = zi[i - 1][j - 1];
        zi[i - 1][j - 1] = zi[k - 1][j - 1];
        zi[k - 1][j - 1] = s;
      }
    }

  }

  /**
   * 複素上ヘッセンベルグ行列の固有値を求めます。
   * 
   * @param hr 複素上ヘッセンベルグ行列の実部
   * @param hi 複素上ヘッセンベルグ行列の虚部
   * @param low 開始番号
   * @param igh 終了番号
   * @param wr 固有値の実部
   * @param wi 固有値の虚部
   * @return 計算結果
   */
  static int comqr(final double[][] hr, final double[][] hi, final int low, final int igh, final double[] wr, final double[] wi) {
    int l = 0;
    double[] z3r = new double[1];
    double[] z3i = new double[1];

    int size = hr.length;

    /*
     * MACHEP is a machine dependent parameter specifying the relative precision
     * of floating arithmetic.
     */
    double machep = MACHEP;
    int ierr = 0;

    boolean goto180 = false;
    if (low == igh) {
      goto180 = true; // goto L180;
    }

    if (goto180 == false) {
      /*
       * Create real subdiagonal elements.
       */
      l = low + 1;

      for (int i = l; i <= igh; i++) {
        int ll = Math.min(i + 1, igh);
        if (hi[i - 1][i - 2] == 0.0) {
          continue;
        }

        double norm = Math.sqrt(hr[i - 1][i - 2] * hr[i - 1][i - 2] + hi[i - 1][i - 2] * hi[i - 1][i - 2]);
        double yr = hr[i - 1][i - 2] / norm;
        double yi = hi[i - 1][i - 2] / norm;
        hr[i - 1][i - 2] = norm;
        hi[i - 1][i - 2] = 0.0;
        for (int j = i; j <= igh; j++) {
          double si = yr * hi[i - 1][j - 1] - yi * hr[i - 1][j - 1];
          hr[i - 1][j - 1] = yr * hr[i - 1][j - 1] + yi * hi[i - 1][j - 1];
          hi[i - 1][j - 1] = si;
        }
        for (int j = low; j <= ll; j++) {
          double si = yr * hi[j - 1][i - 1] + yi * hr[j - 1][i - 1];
          hr[j - 1][i - 1] = yr * hr[j - 1][i - 1] - yi * hi[j - 1][i - 1];
          hi[j - 1][i - 1] = si;
        }
      }
    }

    /*
     * Store roots isolated by cbal().
     */
    // L180:
    for (int i = 1; i <= size; i++) {
      if (i >= low && i <= igh) {
        continue;
      }
      wr[i - 1] = hr[i - 1][i - 1];
      wi[i - 1] = hi[i - 1][i - 1];
    }
    int en = igh;
    double tr = 0.0;
    double ti = 0.0;

    /*
     * Search for next eigenvalue.
     */
    // L220:
    do {
      if (en < low) { // goto L1001;
        // hc = MatRealAndImag(hr, hi);
        // MatCopy(h, hc);
        // MatMultiUndefs(3, hr, hi, hc);
        return ierr;
      }
      int its = 0;
      int enm1 = en - 1;

      /*
       * Look for single small sub-diagonal element. for l=en step -1 until low
       * --.
       */
      // L240:
      do {
        for (int ll = low; ll <= en; ll++) {
          l = en + low - ll;
          if (l == low) {
            break;
          }
          if (Math.abs(hr[l - 1][l - 2]) <= machep * (Math.abs(hr[l - 2][l - 2]) + Math.abs(hi[l - 2][l - 2]) + Math.abs(hr[l - 1][l - 1]) + Math.abs(hi[l - 1][l - 1]))) {
            break;
          }
        }

        /*
         * Form shift.
         */
        if (l == en) {
          break; // goto L660;
        }
        if (its == 100) { // goto L1000;
          ierr = en;
          // hc = MatRealAndImag(hr, hi);
          // MatCopy(h, hc);
          // MatMultiUndefs(3, hr, hi, hc);
          return ierr;
        }
        
        double sr;
        double si;
        if (its == 10 || its == 20 || its == 30 || its == 40 || its == 50 || its == 60 || its == 70 || its == 80 || its == 90) {
          /*
           * Form exceptional shift.
           */
          sr = Math.abs(hr[en - 1][enm1 - 1]) + Math.abs(hr[enm1 - 1][en - 3]);
          si = 0.0;
        } else {
          sr = hr[en - 1][en - 1];
          si = hi[en - 1][en - 1];
          double xr = hr[enm1 - 1][en - 1] * hr[en - 1][enm1 - 1];
          double xi = hi[enm1 - 1][en - 1] * hr[en - 1][enm1 - 1]; // ////
          if (!(xr == 0.0 && xi == 0.0)) {
            double yr = (hr[enm1 - 1][enm1 - 1] - sr) / 2.0;
            double yi = (hi[enm1 - 1][enm1 - 1] - si) / 2.0;
            csqrt(yr * yr - yi * yi + xr, 2.0 * yr * yi + xi, z3r, z3i);
            double zzr = z3r[0];
            double zzi = z3i[0];
            if (yr * zzr + yi * zzi < 0.0) {
              zzr = -zzr;
              zzi = -zzi;
            }
            cdiv(xr, xi, yr + zzr, yi + zzi, z3r, z3i);
            sr -= z3r[0];
            si -= z3i[0];
          }
        }
        for (int i = low; i <= en; i++) {
          hr[i - 1][i - 1] -= sr;
          hi[i - 1][i - 1] -= si;
        }
        tr += sr;
        ti += si;
        its++;

        /*
         * Reduce to triangle (rows).
         */
        int lp1 = l + 1;
        for (int i = lp1; i <= en; i++) {
          sr = hr[i - 1][i - 2];
          hr[i - 1][i - 2] = 0.0;
          double norm = Math.sqrt(hr[i - 2][i - 2] * hr[i - 2][i - 2] + hi[i - 2][i - 2] * hi[i - 2][i - 2] + sr * sr);
          double xr = hr[i - 2][i - 2] / norm;
          wr[i - 2] = xr;
          double xi = hi[i - 2][i - 2] / norm;
          wi[i - 2] = xi;
          hr[i - 2][i - 2] = norm;
          hi[i - 2][i - 2] = 0.0;
          hi[i - 1][i - 2] = sr / norm;

          for (int j = i; j <= en; j++) {
            double yr = hr[i - 2][j - 1];
            double yi = hi[i - 2][j - 1];
            double zzr = hr[i - 1][j - 1];
            double zzi = hi[i - 1][j - 1];
            hr[i - 2][j - 1] = xr * yr + xi * yi + hi[i - 1][i - 2] * zzr;
            hi[i - 2][j - 1] = xr * yi - xi * yr + hi[i - 1][i - 2] * zzi;
            hr[i - 1][j - 1] = xr * zzr - xi * zzi - hi[i - 1][i - 2] * yr;
            hi[i - 1][j - 1] = xr * zzi + xi * zzr - hi[i - 1][i - 2] * yi;
          }
        }

        si = hi[en - 1][en - 1];

        if (si != 0.0) {
          double norm = Math.sqrt(hr[en - 1][en - 1] * hr[en - 1][en - 1] + si * si);
          sr = hr[en - 1][en - 1] / norm;
          si /= norm;
          hr[en - 1][en - 1] = norm;
          hi[en - 1][en - 1] = 0.0;
        }

        /*
         * Inverse operation (columns).
         */
        for (int j = lp1; j <= en; j++) {
          double xr = wr[j - 2];
          double xi = wi[j - 2];

          for (int i = l; i <= j; i++) {
            double yr = hr[i - 1][j - 2];
            double yi = 0.0;
            double zzr = hr[i - 1][j - 1];
            double zzi = hi[i - 1][j - 1];
            if (i != j) {
              yi = hi[i - 1][j - 2];
              hi[i - 1][j - 2] = xr * yi + xi * yr + hi[j - 1][j - 2] * zzi;
            }
            hr[i - 1][j - 2] = xr * yr - xi * yi + hi[j - 1][j - 2] * zzr;
            hr[i - 1][j - 1] = xr * zzr + xi * zzi - hi[j - 1][j - 2] * yr;
            hi[i - 1][j - 1] = xr * zzi - xi * zzr - hi[j - 1][j - 2] * yi;
          }
        }
        if (si == 0.0) {
          continue; // goto L240;
        }
        for (int i = l; i <= en; i++) {
          double yr = hr[i - 1][en - 1];
          double yi = hi[i - 1][en - 1];
          hr[i - 1][en - 1] = sr * yr - si * yi;
          hi[i - 1][en - 1] = sr * yi + si * yr;
        }
      } while (true); // goto L240;

      /*
       * A root found.
       */
      // L660:
      wr[en - 1] = hr[en - 1][en - 1] + tr;
      wi[en - 1] = hi[en - 1][en - 1] + ti; // //11/07/15:57
      en = enm1;
    } while (true); // goto L220;

    /*
     * Set error -- No convergence to an eigenvalue after 50 iterrations.
     */
  }

  /**
   * 複素数の絶対値を求めます。
   * 
   * @param re 複素数の実部
   * @param im 複素数の虚部
   * @return 複素数の絶対値
   */
  private static double complexAbs(final double re, final double im) {
    return Math.sqrt(re * re + im * im);
  }
}