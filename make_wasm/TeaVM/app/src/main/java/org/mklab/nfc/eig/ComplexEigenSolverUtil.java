/*
 * $Id: ComplexEigenUtil.java,v 1.8 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 複素行列の固有値を求めるためのユーティリティクラスです。
 */
final class ComplexEigenSolverUtil {
  /**
   * 新しく生成された<code>ComplexEigenSolverUtil</code>オブジェクトを初期化します。
   */
  private ComplexEigenSolverUtil() {
    // nothing to do
  }

  /** 基数。 */
  private static final int RADIX = 2;
  /** 基数の2乗。 */
  private static final int B2 = RADIX * RADIX;

  /**
   * 複素数の平方根を返します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param re 複素数の実部
   * @param im 複素数の虚部
   * @param or 求めた平方根の実部
   * @param oi 求めた平方根の虚部
   */
  private static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> void csqrt(final S re, final S im, final S[] or, final S[] oi) {
    final S w = (re.multiply(re).add(im.multiply(im))).sqrt();

    or[0] = (w.add(re).divide(2)).abs().sqrt();
    oi[0] = (w.subtract(re).divide(2)).abs().sqrt();

    if (im.isLessThan(0)) {
      oi[0] = oi[0].unaryMinus();
    }
  }

  /**
   * 複素数の割り算を行います。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型 
   * 
   * @param re1 割られる複素数の実部
   * @param im1 割られる複素数の虚部
   * @param re2 割る複素数の実部
   * @param im2 割る複素数の虚部
   * @param or 割り算の結果の実部
   * @param oi 割り算の結果の虚部
   */
  private static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> void cdiv(final S re1, final S im1, final S re2, final S im2, final S[] or, final S[] oi) {
    if (re2.isZero() && im2.isZero()) {
      throw new RuntimeException(Messages.getString("ComplexEigenUtil.0")); //$NON-NLS-1$
    }

    if (re2.abs().isGreaterThan(im2.abs())) {
      final S d = im2.divide(re2);
      final S s = re2.add(im2.multiply(d));
      or[0] = re1.add(im1.multiply(d)).divide(s);
      oi[0] = re1.unaryMinus().multiply(d).add(im1).divide(s);
    } else {
      final S d = re2.divide(im2);
      final S s = re2.multiply(d).add(im2);
      or[0] = re1.multiply(d).add(im1).divide(s);
      oi[0] = re1.unaryMinus().add(im1.multiply(d)).divide(s);
    }
  }

//  /**
//   * 複素行列をバランス化(平衡化)した行列を返します。
//   * 
//   * @param <E> 成分の型
//   * 
//   * @param aRe 複素行列の実部
//   * @param aIm 複素行列の虚部
//   * @return d(スケーリング実対角), br(バランス化された行列の実部), bi(バランスかされた行列の虚部)
//   */
//  @SuppressWarnings("unchecked")
//  static <E extends NumericalScalar<E>> E[][][] matBlance(final E[][] aRe, final E[][] aIm) {
//    final E unit = aRe[0][0].createUnit();
//
//    final int size = aRe.length;
//
//    final E[][] a2Re = GridUtil.clone(aRe);
//    final E[][] a2Im = GridUtil.clone(aIm);
//
//    final E[] s = GridUtil.createZero(aRe[0], size);
//
//    ComplexEigenSolverUtil.balance(a2Re, a2Im, s);
//
//    final E[] scale = unit.createArray(size);
//    for (int i = 0; i < size; i++) {
//      scale[i] = unit.transformFrom(s[i]);
//    }
//
//    final E[][] d = GridUtil.vectorToDiagonal(scale);
//
//    return (E[][][])new NumericalScalar[][][] {d, a2Re, a2Im};
//  }

//  /*
//   * Function to balance a Complex General Matrix.
//   * 
//   * This function balances a complex matrix
//   * 
//   * on input
//   * 
//   * ar and ai contain the real and imaginary parts, respectively, of the
//   * complex matrix to be balanced.
//   * 
//   * on output
//   * 
//   * ar and ai contain the real and imaginary parts, respectively, of the
//   * balanced matrix.
//   * 
//   * scale contains information determining the scaling factors used.
//   * 
//   * arithmetic is real throughout.
//   */
//  /**
//   * @param <E> 成分の型
//   * @param aRe 実部行列
//   * @param aIm 居部行列
//   * @param scale スケーリングベクトル
//   */
//  @SuppressWarnings("unchecked")
//  private static <E extends NumericalScalar<E>> void balance(final E[][] aRe, final E[][] aIm, final E[] scale) {
//    final int size = aRe.length;
//
//    final E unit = aRe[0][0].createUnit();
//
//    /*
//     * Now balance the submatrix in rows 1 to n
//     */
//    for (int i = 1; i <= size; i++) {
//      scale[i - 1] = unit.createUnit();
//    }
//    /*
//     * Iterative loop for norm reduction
//     */
//    // L190:
//    boolean noconv;
//    do {
//      noconv = false;
//
//      for (int i = 1; i <= size; i++) {
//        E c = unit.createZero();
//        E r = unit.createZero();
//        for (int j = 1; j <= size; j++) {
//          if (j == i) {
//            continue;
//          }
//
//          c = (E)c.add(aRe[j - 1][i - 1].abs()).add(aIm[j - 1][i - 1].abs());
//          r = (E)r.add(aRe[i - 1][j - 1].abs()).add(aIm[i - 1][j - 1].abs());
//        }
//
//        if (c.isZero() || r.isZero()) {
//          continue;
//        }
//
//        E g = r.divide(RADIX);
//        E f = unit.createUnit();
//        E s = c.add(r);
//        while (c.isLessThan(g)) {
//          f = f.multiply(RADIX);
//          c = c.multiply(B2);
//        }
//        g = r.multiply(RADIX);
//        while (c.isGreaterThanOrEquals(g)) {
//          f = f.divide(RADIX);
//          c = c.divide(B2);
//        }
//        /*
//         * Now balanc
//         */
//        E c095 = unit.multiply(95).divide(100);
//        if ((c.add(r).divide(f)).isGreaterThanOrEquals(c095.multiply(s))) {
//          continue;
//        }
//
//        g = unit.transformFrom(f).inverse();
//        scale[i - 1] = scale[i - 1].multiply(f);
//        noconv = true;
//
//        for (int j = 1; j <= size; j++) { // //////
//          // ComplexValueMulSelf2(AC(i,j), g); //////// AC(i,j) = AC(i,j) * g
//          aRe[i - 1][j - 1] = aRe[i - 1][j - 1].multiply(g);
//          aIm[i - 1][j - 1] = aIm[i - 1][j - 1].multiply(g);
//          // ComplexValueMulSelf2(AC(j,i), f); ////////
//          aRe[j - 1][i - 1] = aRe[j - 1][i - 1].multiply(f);
//          aIm[j - 1][i - 1] = aIm[j - 1][i - 1].multiply(f);
//        }
//      }
//    } while (noconv); // if(noconv)goto L190;
//  }

  /*
   * Function to balance a Complex General Matrix.
   * 
   * This function balances a complex matrix and isolates eigenvalues whenever
   * possible.
   * 
   * on input
   * 
   * ar and ai contain the real and imaginary parts, respectively, of the
   * complex matrix to be balanced.
   * 
   * on output
   * 
   * ar and ai contain the real and imaginary parts, respectively, of the
   * balanced matrix.
   * 
   * low and igh are two integers such that ar(i,j) and ai(i,j) are equal to
   * zero if (1) i is greater than j and (2) j=1,...,low-1 or i=igh+1,...,n.
   * 
   * scale contains information determining the permutations and scaling factors
   * used.
   * 
   * suppose that the principal submatrix in rows low through igh has been
   * balanced, that p(j) denotes the index interchanged with j during the
   * permutation step, and that the elements of the diagonal matrix used are
   * denoted by d(i,j). then scale(j) = p(j), for j = 1,...,low-1 = d(j,j) j =
   * low,...,igh = p(j) j = igh+1,...,n. the order in which the interchanges are
   * made is n to igh+1, then 1 to low-1.
   * 
   * note that 1 is returned for igh if igh is zero formally.
   * 
   * the algol procedure exc contained in cbalance appears in cbal() in line.
   * (note that the algol roles of identifiers k,l have been reversed.)
   * 
   * arithmetic is real throughout.
   */
  // static void cbal(Matrix a, int *low, int *igh, double *scale) {
  /**
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param ar 実部行列
   * @param ai 虚部行列
   * @param low 開始番号
   * @param igh 終了番号
   * @param scale スケーリングベクトル
   */
  static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> void cbal(final S[][] ar, final S[][] ai, final int[] low, final int[] igh, final S[] scale) {
    final S unit = ar[0][0].createUnit();

    int m = 0; 
    int iexc = 0;
    int j = 0;

    int size = ar.length;
    int k = 1;
    int l = size;

    boolean goto100 = true; // goto L100;
    // L20:
    do {
      boolean goto20 = false;
      boolean goto140 = false;

      if (!goto100) {
        scale[m - 1] = unit.create(j);

        if (j != m) {
          for (int i = 1; i <= l; i++) {
            // ComplexValueSwap(AC(i,j), AC(i,m));
            S tmpr = ar[i - 1][j - 1]; // //11/07/15:55
            S tmpi = ai[i - 1][j - 1]; // //11/07/15:55
            ar[i - 1][j - 1] = ar[i - 1][m - 1]; // //11/07/15:55
            ai[i - 1][j - 1] = ai[i - 1][m - 1]; // //11/07/15:55//11/08/17:34
            ar[i - 1][m - 1] = tmpr; // //11/07/15:55
            ai[i - 1][m - 1] = tmpi; // //11/07/15:55
          }
          for (int i = k; i <= size; i++) {
            // ComplexValueSwap(AC(j,i), AC(m,i));
            S tmpr = ar[j - 1][i - 1];
            S tmpi = ai[j - 1][i - 1];
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
            if (complexAbs(ar[j - 1][i - 1], ai[j - 1][i - 1]).isZero() == false) {
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
          if (complexAbs(ar[i - 1][j - 1], ai[i - 1][j - 1]).isZero() == false) { // 11/08/17:59
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
      scale[i - 1] = unit.createUnit();
    }
    // L190:
    boolean noconv;
    do {
      noconv = false;

      for (int i = k; i <= l; i++) {
        S c = unit.createZero();
        S r = unit.createZero();

        for (j = k; j <= l; j++) {
          if (j == i) {
            continue;
          }

          c = c.add(ar[j - 1][i - 1].abs()).add(ai[j - 1][i - 1].abs());
          r = r.add(ar[i - 1][j - 1].abs()).add(ai[i - 1][j - 1].abs());
        }
        if (c.isZero() || r.isZero()) {
          continue;
        }

        S g = r.divide(RADIX);
        S f = unit.createUnit();
        S s = c.add(r);
        while (c.isLessThan(g)) {
          f = f.multiply(RADIX);
          c = c.multiply(B2);
        }
        g = r.multiply(RADIX);
        while (c.isGreaterThanOrEquals(g)) {
          f = f.divide(RADIX);
          c = c.divide(B2);
        }
        S c095 = unit.multiply(95).divide(100);

        if ((c.add(r).divide(f)).isGreaterThanOrEquals(c095.multiply(s))) {
          continue;
        }
        g = f.inverse();
        //g = unit.transformFrom(f).inverse();
        scale[i - 1] = scale[i - 1].multiply(f);
        noconv = true;

        for (j = k; j <= size; j++) {
          // ComplexValueMulSelf2(AC(i,j), g);
          ar[i - 1][j - 1] = ar[i - 1][j - 1].multiply(g);
          ai[i - 1][j - 1] = ai[i - 1][j - 1].multiply(g);
        }
        for (j = 1; j <= l; j++) {
          // ComplexValueMulSelf2(AC(j,i), f);
          ar[j - 1][i - 1] = ar[j - 1][i - 1].multiply(f);
          ai[j - 1][i - 1] = ai[j - 1][i - 1].multiply(f);
        }
      }
      // if (noconv) goto L190; /// up
    } while (noconv);
    // L280:
    low[0] = k;
    igh[0] = l;
  }

  /*
   * Function to reduce a Complex General Matrix to Upper Hessenberg Form using
   * Unitary Transformations.
   * 
   * Given a complex general matrix, this function reduces a submatrix situated
   * in rows and columns low through igh to upper hessenberg form by unitary
   * similarity transformations.
   * 
   * on input
   * 
   * low and igh are integers determined by the balancing function cbal(). if
   * cbal() has not been used, set low=1, igh=n.
   * 
   * ar and ai contain the real and imaginary parts, respectively, of the
   * complex input matrix.
   * 
   * on output
   * 
   * ar and ai contain the real and imaginary parts, respectively, of the
   * hessenberg matrix. information about the unitary transformations used in
   * the reduction is stored in the remaining triangles under the hessenberg
   * matrix.
   * 
   * ortr and orti contain further information about the transformations. only
   * elements low through igh are used.
   */
  // static void corth(Matrix a, int low, int igh, double *ortr, double *orti) {
  /**
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param ar 実部行列
   * @param ai 虚部行列
   * @param low 開始番号
   * @param igh 終了番号
   * @param ortr 変換に関する実部の情報
   * @param orti 変換に関する虚部の情報
   */
  static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> void corth(final S[][] ar, final S[][] ai, final int low, final int igh, final S[] ortr, final S[] orti) {
    S unit = ar[0][0].createUnit();

    int size = ar.length;

    int la = igh - 1;
    int kp1 = low + 1;

    if (la < kp1) {
      return;
    }

    int m;
    for (m = kp1; m <= la; m++) {
      ortr[m - 1] = unit.createZero();
      orti[m - 1] = unit.createZero();

      S scale = unit.createZero();
      for (int i = m; i <= igh; i++) {
        scale = scale.add(ar[i - 1][m - 2].abs()).add(ai[i - 1][m - 2].abs());
      }

      if (scale.isZero()) {
        continue;
      }

      int mp = m + igh;

      S h = unit.createZero();
      for (int ii = m; ii <= igh; ii++) {
        int i = mp - ii;
        ortr[i - 1] = ar[i - 1][m - 2].divide(scale);
        orti[i - 1] = ai[i - 1][m - 2].divide(scale);
        h = h.add(ortr[i - 1].multiply(ortr[i - 1])).add(orti[i - 1].multiply(orti[i - 1]));
      }

      S g = h.sqrt();
      S f = (ortr[m - 1].multiply(ortr[m - 1]).add(orti[m - 1].multiply(orti[m - 1]))).sqrt();

      if (f.isZero() == false) {
        h = h.add(f.multiply(g));
        g = g.divide(f);
        ortr[m - 1] = ortr[m - 1].multiply(g.add(1));
        orti[m - 1] = orti[m - 1].multiply(g.add(1));
      } else {
        ortr[m - 1] = g.clone();
        ar[m - 1][m - 2] = scale.clone();
      }

      for (int j = m; j <= size; j++) {
        S fr = unit.createZero();
        S fi = unit.createZero();
        for (int ii = m; ii <= igh; ii++) {
          int i = mp - ii;
          fr = fr.add(ortr[i - 1].multiply(ar[i - 1][j - 1]).add(orti[i - 1].multiply(ai[i - 1][j - 1])));
          fi = fi.add(ortr[i - 1].multiply(ai[i - 1][j - 1]).subtract(orti[i - 1].multiply(ar[i - 1][j - 1])));
        }
        fr = fr.divide(h);
        fi = fi.divide(h);
        for (int i = m; i <= igh; i++) {
          ar[i - 1][j - 1] = ar[i - 1][j - 1].subtract(fr.multiply(ortr[i - 1]).subtract(fi.multiply(orti[i - 1])));
          ai[i - 1][j - 1] = ai[i - 1][j - 1].subtract(fr.multiply(orti[i - 1]).add(fi.multiply(ortr[i - 1])));
        }
      }

      for (int i = 1; i <= igh; i++) {
        S fr = unit.createZero();
        S fi = unit.createZero();
        for (int jj = m; jj <= igh; jj++) {
          int j = mp - jj;
          fr = fr.add(ortr[j - 1].multiply(ar[i - 1][j - 1]).subtract(orti[j - 1].multiply(ai[i - 1][j - 1])));
          fi = fi.add(ortr[j - 1].multiply(ai[i - 1][j - 1]).add(orti[j - 1].multiply(ar[i - 1][j - 1])));
        }

        fr = fr.divide(h);
        fi = fi.divide(h);
        for (int j = m; j <= igh; j++) {
          ar[i - 1][j - 1] = ar[i - 1][j - 1].subtract(fr.multiply(ortr[j - 1]).add(fi.multiply(orti[j - 1])));
          ai[i - 1][j - 1] = ai[i - 1][j - 1].add(fr.multiply(orti[j - 1]).subtract(fi.multiply(ortr[j - 1])));
        }
      }

      ortr[m - 1] = ortr[m - 1].multiply(scale);
      orti[m - 1] = orti[m - 1].multiply(scale);
      ar[m - 1][m - 2] = ar[m - 1][m - 2].multiply(g.unaryMinus());
      ai[m - 1][m - 2] = ai[m - 1][m - 2].multiply(g.unaryMinus());
    }
  }

  /**
   * 直交相似変換の累積を求めます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param ar ユニタリ変換に関する実部の情報
   * @param ai ユニタリ変換に関する虚部の情報
   * @param low 開始番号
   * @param igh 終了番号
   * @param ortr 変換に関する実部の追加情報
   * @param orti 変換に関する虚部の追加情報
   * @param zr 固有ベクトルの実部
   * @param zi 固有ベクトルの虚部
   * @param m 逆変換されるべき固有ベクトルの数m
   */
  static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> void cortb(final S[][] ar, final S[][] ai, final int low, final int igh, final S[] ortr, final S[] orti, final S[][] zr, final S[][] zi, final int m) {
    S unit = ar[0][0].createUnit();

    int la = igh - 1;
    int kp1 = low + 1;

    if (la < kp1) {
      return;
    }

    for (int mp = igh - 1; mp >= low + 1; mp--) {
      if (ar[mp - 1][mp - 2].isZero() && ai[mp - 1][mp - 2].isZero()) {
        continue;
      }

      /*
       * H below is negative of H formed in corth().
       */
      S h = ar[mp - 1][mp - 2].multiply(ortr[mp - 1]).add(ai[mp - 1][mp - 2].multiply(orti[mp - 1]));
      int mp1 = mp + 1;

      for (int i = mp1; i <= igh; i++) {
        ortr[i - 1] = ar[i - 1][mp - 2].clone();
        orti[i - 1] = ai[i - 1][mp - 2].clone();
      }

      for (int j = 1; j <= m; j++) {
        S gr = unit.createZero();
        S gi = unit.createZero();

        for (int i = mp; i <= igh; i++) {
          gr = gr.add(ortr[i - 1].multiply(zr[i - 1][j - 1]).add(orti[i - 1].multiply(zi[i - 1][j - 1])));
          gi = gi.add(ortr[i - 1].multiply(zi[i - 1][j - 1]).subtract(orti[i - 1].multiply(zr[i - 1][j - 1])));
        }

        gr = gr.divide(h);
        gi = gi.divide(h);

        for (int i = mp; i <= igh; i++) {
          zr[i - 1][j - 1] = zr[i - 1][j - 1].add(gr.multiply(ortr[i - 1]).subtract(gi.multiply(orti[i - 1])));
          zi[i - 1][j - 1] = zi[i - 1][j - 1].add(gr.multiply(orti[i - 1]).add(gi.multiply(ortr[i - 1])));
        }
      }
    }

  }

  /**
   * 複素上ヘッセンベルグ行列の固有値と固有ベクトルを求めます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
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
  static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> int comqr2(final S[][] hr, final S[][] hi, final int low, final int igh, final S[] ortr, final S[] orti, final S[] wr, final S[] wi, final S[][] zr, final S[][] zi, final boolean schur) {
    S unit = hr[0][0].createUnit();

    int l = 0;
    S[] z3r = unit.createArray(1);
    S[] z3i = unit.createArray(1);

    int size = hr.length;

    /*
     * MACHEP is a machine dependent parameter specifying the relative precision
     * of floating point arithmetic.
     */
    S machep = unit.getMachineEpsilon();
    int ierr = 0;

    /*
     * Initialize eigenvector matrix.
     */

    for (int i = 1; i <= size; i++) {
      for (int j = 1; j <= size; j++) {
        zr[i - 1][j - 1] = unit.createZero();
        zi[i - 1][j - 1] = unit.createZero();
        if (i == j) {
          zr[i - 1][j - 1] = unit.createUnit();
        }
      }
    }

    /*
     * Form the matrix of accumulated transformations from the information left
     * by corth().
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
          if (ortr[i - 1].isZero() && orti[i - 1].isZero()) {
            continue;
          }
          if (hr[i - 1][i - 2].isZero() && hi[i - 1][i - 2].isZero()) {
            continue;
          }

          /*
           * Norm below is negative of H formed in corth().
           */
          S norm = hr[i - 1][i - 2].multiply(ortr[i - 1]).add(hi[i - 1][i - 2].multiply(orti[i - 1]));
          int ip1 = i + 1;
          for (int k = ip1; k <= igh; k++) {
            ortr[k - 1] = hr[k - 1][i - 2].clone();
            orti[k - 1] = hi[k - 1][i - 2].clone();
          }
          for (int j = i; j <= igh; j++) {
            S sr = unit.createZero();
            S si = unit.createZero();
            for (int k = i; k <= igh; k++) {
              sr = sr.add(ortr[k - 1].multiply(zr[k - 1][j - 1]).add(orti[k - 1].multiply(zi[k - 1][j - 1])));
              si = si.add(ortr[k - 1].multiply(zi[k - 1][j - 1]).subtract(orti[k - 1].multiply(zr[k - 1][j - 1])));
            }
            sr = sr.divide(norm);
            si = si.divide(norm);
            for (int k = i; k <= igh; k++) {
              zr[k - 1][j - 1] = zr[k - 1][j - 1].add(sr.multiply(ortr[k - 1]).subtract(si.multiply(orti[k - 1])));
              zi[k - 1][j - 1] = zi[k - 1][j - 1].add(sr.multiply(orti[k - 1]).add(si.multiply(ortr[k - 1])));
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
        if (hi[i - 1][i - 2].isZero()) {
          continue;
        }

        S norm = (hr[i - 1][i - 2].multiply(hr[i - 1][i - 2]).add(hi[i - 1][i - 2].multiply(hi[i - 1][i - 2]))).sqrt();
        S yr = hr[i - 1][i - 2].divide(norm);
        S yi = hi[i - 1][i - 2].divide(norm);
        hr[i - 1][i - 2] = norm.clone();
        hi[i - 1][i - 2] = unit.createZero();
        for (int j = i; j <= size; j++) {
          S si = yr.multiply(hi[i - 1][j - 1]).subtract(yi.multiply(hr[i - 1][j - 1]));
          hr[i - 1][j - 1] = yr.multiply(hr[i - 1][j - 1]).add(yi.multiply(hi[i - 1][j - 1]));
          hi[i - 1][j - 1] = si.clone();
        }
        for (int j = 1; j <= ll; j++) {
          S si = yr.multiply(hi[j - 1][i - 1]).add(yi.multiply(hr[j - 1][i - 1]));
          hr[j - 1][i - 1] = yr.multiply(hr[j - 1][i - 1]).subtract(yi.multiply(hi[j - 1][i - 1]));
          hi[j - 1][i - 1] = si.clone();
        }
        for (int j = low; j <= igh; j++) {
          S si = yr.multiply(zi[j - 1][i - 1]).add(yi.multiply(zr[j - 1][i - 1]));
          zr[j - 1][i - 1] = yr.multiply(zr[j - 1][i - 1]).subtract(yi.multiply(zi[j - 1][i - 1]));
          zi[j - 1][i - 1] = si.clone();
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
      wr[i - 1] = hr[i - 1][i - 1].clone();
      wi[i - 1] = hi[i - 1][i - 1].clone();
    }
    int en = igh;
    S tr = unit.createZero();
    S ti = unit.createZero();

    /*
     * Search for next eigenvalue
     */
    // L220:
    do {
      if (en < low) {
        break; // goto L680;
      }
      int its = 0;
      int itn = 100 * size;
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
          if (hr[l - 1][l - 2].abs().isLessThanOrEquals(machep.multiply((hr[l - 2][l - 2].abs().add(hi[l - 2][l - 2].abs()).add(hr[l - 1][l - 1].abs()).add(hi[l - 1][l - 1].abs()))))) {
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

        S sr;
        S si;
        if (its != 0 && (its % 10) == 0) {
          /*
           * Form exceptional shift
           */
          sr = hr[en - 1][enm1 - 1].abs().add(hr[enm1 - 1][en - 3].abs());
          si = unit.createZero();
        } else {
          sr = hr[en - 1][en - 1].clone();
          si = hi[en - 1][en - 1].clone();
          S xr = hr[enm1 - 1][en - 1].multiply(hr[en - 1][enm1 - 1]);
          S xi = hi[enm1 - 1][en - 1].multiply(hr[en - 1][enm1 - 1]);
          if (!(xr.isZero() && xi.isZero())) {
            S yr = (hr[enm1 - 1][enm1 - 1].subtract(sr)).divide(2);
            S yi = (hi[enm1 - 1][enm1 - 1].subtract(si)).divide(2);
            csqrt(yr.multiply(yr).subtract(yi.multiply(yi)).add(xr), yr.multiply(yi).multiply(2).add(xi), z3r, z3i);
            S zzr = z3r[0].clone();
            S zzi = z3i[0].clone();
            if ((yr.multiply(zzr).add(yi.multiply(zzi))).isLessThan(0)) {
              zzr = zzr.unaryMinus();
              zzi = zzi.unaryMinus();
            }
            cdiv(xr, xi, yr.add(zzr), yi.add(zzi), z3r, z3i);
            sr = sr.subtract(z3r[0]);
            si = si.subtract(z3i[0]);
          }
        }

        for (int i = low; i <= en; i++) {
          hr[i - 1][i - 1] = hr[i - 1][i - 1].subtract(sr);
          hi[i - 1][i - 1] = hi[i - 1][i - 1].subtract(si);
        }

        tr = tr.add(sr);
        ti = ti.add(si);
        its++;
        itn--;

        /*
         * Reduce to triangle (rows).
         */
        int lp1 = l + 1;
        for (int i = lp1; i <= en; i++) {
          sr = hr[i - 1][i - 2].clone();
          hr[i - 1][i - 2] = unit.createZero();
          S norm = (hr[i - 2][i - 2].multiply(hr[i - 2][i - 2]).add(hi[i - 2][i - 2].multiply(hi[i - 2][i - 2])).add(sr.multiply(sr))).sqrt();
          S xr = hr[i - 2][i - 2].divide(norm);
          wr[i - 2] = xr.clone();
          S xi = hi[i - 2][i - 2].divide(norm);
          wi[i - 2] = xi.clone();
          hr[i - 2][i - 2] = norm.clone();
          hi[i - 2][i - 2] = unit.createZero();
          hi[i - 1][i - 2] = sr.divide(norm);

          for (int j = i; j <= size; j++) {
            S yr = hr[i - 2][j - 1].clone();
            S yi = hi[i - 2][j - 1].clone();
            S zzr = hr[i - 1][j - 1].clone();
            S zzi = hi[i - 1][j - 1].clone();
            hr[i - 2][j - 1] = xr.multiply(yr).add(xi.multiply(yi)).add(hi[i - 1][i - 2].multiply(zzr));
            hi[i - 2][j - 1] = xr.multiply(yi).subtract(xi.multiply(yr)).add(hi[i - 1][i - 2].multiply(zzi));
            hr[i - 1][j - 1] = xr.multiply(zzr).subtract(xi.multiply(zzi)).subtract(hi[i - 1][i - 2].multiply(yr));
            hi[i - 1][j - 1] = xr.multiply(zzi).add(xi.multiply(zzr)).subtract(hi[i - 1][i - 2].multiply(yi));
          }
        }
        si = hi[en - 1][en - 1].clone();
        if (si.isZero() == false) {
          S norm = (hr[en - 1][en - 1].multiply(hr[en - 1][en - 1]).add(si.multiply(si))).sqrt();
          sr = hr[en - 1][en - 1].divide(norm);
          si = si.divide(norm);
          hr[en - 1][en - 1] = norm.clone();
          hi[en - 1][en - 1] = unit.createZero();
          if (en != size) {
            int ip1 = en + 1;
            for (int j = ip1; j <= size; j++) {
              S yr = hr[en - 1][j - 1].clone();
              S yi = hi[en - 1][j - 1].clone();
              hr[en - 1][j - 1] = sr.multiply(yr).add(si.multiply(yi));
              hi[en - 1][j - 1] = sr.multiply(yi).subtract(si.multiply(yr));
            }
          }
        }

        /*
         * Inverse operation (columns).
         */
        for (int j = lp1; j <= en; j++) {
          S xr = wr[j - 2].clone();
          S xi = wi[j - 2].clone();

          for (int i = 1; i <= j; i++) {
            S yr = hr[i - 1][j - 2].clone();
            S yi = unit.createZero();
            S zzr = hr[i - 1][j - 1].clone();
            S zzi = hi[i - 1][j - 1].clone();
            if (i != j) {
              yi = hi[i - 1][j - 2].clone();
              hi[i - 1][j - 2] = xr.multiply(yi).add(xi.multiply(yr)).add(hi[j - 1][j - 2].multiply(zzi));
            }
            hr[i - 1][j - 2] = xr.multiply(yr).subtract(xi.multiply(yi)).add(hi[j - 1][j - 2].multiply(zzr));
            hr[i - 1][j - 1] = xr.multiply(zzr).add(xi.multiply(zzi)).subtract(hi[j - 1][j - 2].multiply(yr));
            hi[i - 1][j - 1] = xr.multiply(zzi).subtract(xi.multiply(zzr)).subtract(hi[j - 1][j - 2].multiply(yi));
          }
          for (int i = low; i <= igh; i++) {
            S yr = zr[i - 1][j - 2].clone();
            S yi = zi[i - 1][j - 2].clone();
            S zzr = zr[i - 1][j - 1].clone();
            S zzi = zi[i - 1][j - 1].clone();
            zr[i - 1][j - 2] = xr.multiply(yr).subtract(xi.multiply(yi)).add(hi[j - 1][j - 2].multiply(zzr));
            zi[i - 1][j - 2] = xr.multiply(yi).add(xi.multiply(yr)).add(hi[j - 1][j - 2].multiply(zzi));
            zr[i - 1][j - 1] = xr.multiply(zzr).add(xi.multiply(zzi)).subtract(hi[j - 1][j - 2].multiply(yr));
            zi[i - 1][j - 1] = xr.multiply(zzi).subtract(xi.multiply(zzr)).subtract(hi[j - 1][j - 2].multiply(yi));
          }
        }
        if (si.isZero()) {
          continue; // goto L240;
        }
        for (int i = 1; i <= en; i++) {
          S yr = hr[i - 1][en - 1].clone();
          S yi = hi[i - 1][en - 1].clone();
          hr[i - 1][en - 1] = sr.multiply(yr).subtract(si.multiply(yi));
          hi[i - 1][en - 1] = sr.multiply(yi).add(si.multiply(yr));
        }
        for (int i = low; i <= igh; i++) {
          S yr = zr[i - 1][en - 1].clone();
          S yi = zi[i - 1][en - 1].clone();
          zr[i - 1][en - 1] = sr.multiply(yr).subtract(si.multiply(yi));
          zi[i - 1][en - 1] = sr.multiply(yi).add(si.multiply(yr));
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
      hr[en - 1][en - 1] = hr[en - 1][en - 1].add(tr);
      wr[en - 1] = hr[en - 1][en - 1].clone();
      hi[en - 1][en - 1] = hi[en - 1][en - 1].add(ti);
      wi[en - 1] = hi[en - 1][en - 1].clone();
      en = enm1;
    } while (true); // goto L220; //up

    /*
     * All roots found. Backsubstitute to find vectors of upper triangular form.
     */
    // L680:
    // do {
    if (schur == true) {
      return ierr; // break;// goto L1001;
    }

    S norm = unit.createZero();

    for (int i = 1; i <= size; i++) {
      for (int j = i; j <= size; j++) {
        norm = norm.add(hr[i - 1][j - 1].abs().add(hi[i - 1][j - 1].abs()));
      }
    }
    if (size == 1 || norm.isZero()) {
      return ierr; // break; //goto L1001;
    }

    /*
     * For en=n step -1 until 2 do --.
     */
    for (int nn = 2; nn <= size; nn++) {
      en = size + 2 - nn;
      S xr = wr[en - 1].clone();
      S xi = wi[en - 1].clone();
      int enm1 = en - 1;

      /*
       * For i=en-1 step -1 until 1 do --.
       */
      for (int ii = 1; ii <= enm1; ii++) {
        int i = en - ii;
        S zzr = hr[i - 1][en - 1].clone();
        S zzi = hi[i - 1][en - 1].clone();
        if (i != enm1) {
          int ip1 = i + 1;
          for (int j = ip1; j <= enm1; j++) {
            zzr = zzr.add(hr[i - 1][j - 1].multiply(hr[j - 1][en - 1]).subtract(hi[i - 1][j - 1].multiply(hi[j - 1][en - 1])));
            zzi = zzi.add(hr[i - 1][j - 1].multiply(hi[j - 1][en - 1]).add(hi[i - 1][j - 1].multiply(hr[j - 1][en - 1])));
          }
        }
        S yr = xr.subtract(wr[i - 1]);
        S yi = xi.subtract(wi[i - 1]);
        if (yr.isZero() && yi.isZero()) {
          yr = machep.multiply(norm);
        }
        cdiv(zzr, zzi, yr, yi, z3r, z3i);
        hr[i - 1][en - 1] = z3r[0].clone();
        hi[i - 1][en - 1] = z3i[0].clone();
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
        zr[i - 1][j - 1] = hr[i - 1][j - 1].clone();
        zi[i - 1][j - 1] = hi[i - 1][j - 1].clone();
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
        S zzr = zr[i - 1][j - 1].clone();
        S zzi = zi[i - 1][j - 1].clone();
        for (int k = low; k <= m; k++) {
          zzr = zzr.add(zr[i - 1][k - 1].multiply(hr[k - 1][j - 1]).subtract(zi[i - 1][k - 1].multiply(hi[k - 1][j - 1])));
          zzi = zzi.add(zr[i - 1][k - 1].multiply(hi[k - 1][j - 1]).add(zi[i - 1][k - 1].multiply(hr[k - 1][j - 1])));
        }
        zr[i - 1][j - 1] = zzr.clone();
        zi[i - 1][j - 1] = zzi.clone();
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
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param low 開始番号
   * @param igh 終了番号
   * @param scale スケーリング情報
   * @param m 逆変換する固有ベクトルの数
   * @param zr 変換後の固有ベクトルの実部
   * @param zi 変換後の固有ベクトルの虚部
   */
  static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> void cbabk2(final int low, final int igh, final S[] scale, final int m, final S[][] zr, final S[][] zi) {
    int size = zr.length;

    if (m == 0) {
      return;
    }

    if (igh != low) {
      for (int i = low; i <= igh; i++) {
        S s = scale[i - 1].clone();
        for (int j = 1; j <= m; j++) {
          zr[i - 1][j - 1] = zr[i - 1][j - 1].multiply(s);
          zi[i - 1][j - 1] = zi[i - 1][j - 1].multiply(s);
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

      int k = (int)Double.parseDouble(scale[i - 1].toString("%16.8E")); //$NON-NLS-1$

      if (k == i) {
        continue;
      }

      for (int j = 1; j <= m; j++) {
        S s = zr[i - 1][j - 1];
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
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param hr 複素上ヘッセンベルグ行列の実部
   * @param hi 複素上ヘッセンベルグ行列の虚部
   * @param low 開始番号
   * @param igh 終了番号
   * @param wr 固有値の実部
   * @param wi 固有値の虚部
   * @return 計算結果
   */
  static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> int comqr(final S[][] hr, final S[][] hi, final int low, final int igh, final S[] wr, final S[] wi) {
    S unit = hr[0][0].createUnit();

    int l = 0;
    S[] z3r = unit.createArray(1);
    S[] z3i = unit.createArray(1);

    int size = hr.length;

    S machep = unit.getMachineEpsilon();
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
        if (hi[i - 1][i - 2].isZero()) {
          continue;
        }

        S norm = (hr[i - 1][i - 2].multiply(hr[i - 1][i - 2]).add(hi[i - 1][i - 2].multiply(hi[i - 1][i - 2]))).sqrt();
        S yr = hr[i - 1][i - 2].divide(norm);
        S yi = hi[i - 1][i - 2].divide(norm);
        hr[i - 1][i - 2] = norm.clone();
        hi[i - 1][i - 2] = unit.createZero();
        for (int j = i; j <= igh; j++) {
          S si = yr.multiply(hi[i - 1][j - 1]).subtract(yi.multiply(hr[i - 1][j - 1]));
          hr[i - 1][j - 1] = yr.multiply(hr[i - 1][j - 1]).add(yi.multiply(hi[i - 1][j - 1]));
          hi[i - 1][j - 1] = si.clone();
        }
        for (int j = low; j <= ll; j++) {
          S si = yr.multiply(hi[j - 1][i - 1]).add(yi.multiply(hr[j - 1][i - 1]));
          hr[j - 1][i - 1] = yr.multiply(hr[j - 1][i - 1]).subtract(yi.multiply(hi[j - 1][i - 1]));
          hi[j - 1][i - 1] = si.clone();
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
      wr[i - 1] = hr[i - 1][i - 1].clone();
      wi[i - 1] = hi[i - 1][i - 1].clone();
    }
    int en = igh;
    S tr = unit.createZero();
    S ti = unit.createZero();

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
          if (hr[l - 1][l - 2].abs().isLessThanOrEquals(machep.multiply(hr[l - 2][l - 2].abs().add(hi[l - 2][l - 2].abs()).add(hr[l - 1][l - 1].abs()).add(hi[l - 1][l - 1].abs())))) {
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
        
        S sr;
        S si;
        if (its == 10 || its == 20 || its == 30 || its == 40 || its == 50 || its == 60 || its == 70 || its == 80 || its == 90) {
          /*
           * Form exceptional shift.
           */
          sr = hr[en - 1][enm1 - 1].abs().add(hr[enm1 - 1][en - 3].abs());
          si = unit.createZero();
        } else {
          sr = hr[en - 1][en - 1].clone();
          si = hi[en - 1][en - 1].clone();
          S xr = hr[enm1 - 1][en - 1].multiply(hr[en - 1][enm1 - 1]);
          S xi = hi[enm1 - 1][en - 1].multiply(hr[en - 1][enm1 - 1]); // ////
          if (!(xr.isZero() && xi.isZero())) {
            S yr = hr[enm1 - 1][enm1 - 1].subtract(sr).divide(2);
            S yi = hi[enm1 - 1][enm1 - 1].subtract(si).divide(2);
            csqrt(yr.multiply(yr).subtract(yi.multiply(yi)).add(xr), yr.multiply(yi).multiply(2).add(xi), z3r, z3i);
            S zzr = z3r[0].clone();
            S zzi = z3i[0].clone();
            if ((yr.multiply(zzr).add(yi.multiply(zzi))).isLessThan(0)) {
              zzr = zzr.unaryMinus();
              zzi = zzi.unaryMinus();
            }
            cdiv(xr, xi, yr.add(zzr), yi.add(zzi), z3r, z3i);
            sr = sr.subtract(z3r[0]);
            si = si.subtract(z3i[0]);
          }
        }
        for (int i = low; i <= en; i++) {
          hr[i - 1][i - 1] = hr[i - 1][i - 1].subtract(sr);
          hi[i - 1][i - 1] = hi[i - 1][i - 1].subtract(si);
        }
        tr = tr.add(sr);
        ti = ti.add(si);
        its++;

        /*
         * Reduce to triangle (rows).
         */
        int lp1 = l + 1;
        for (int i = lp1; i <= en; i++) {
          sr = hr[i - 1][i - 2].clone();
          hr[i - 1][i - 2] = unit.createZero();
          S norm = (hr[i - 2][i - 2].multiply(hr[i - 2][i - 2]).add(hi[i - 2][i - 2].multiply(hi[i - 2][i - 2])).add(sr.multiply(sr))).sqrt();
          S xr = hr[i - 2][i - 2].divide(norm);
          wr[i - 2] = xr.clone();
          S xi = hi[i - 2][i - 2].divide(norm);
          wi[i - 2] = xi.clone();
          hr[i - 2][i - 2] = norm.clone();
          hi[i - 2][i - 2] = unit.createZero();
          hi[i - 1][i - 2] = sr.divide(norm);

          for (int j = i; j <= en; j++) {
            S yr = hr[i - 2][j - 1].clone();
            S yi = hi[i - 2][j - 1].clone();
            S zzr = hr[i - 1][j - 1].clone();
            S zzi = hi[i - 1][j - 1].clone();
            hr[i - 2][j - 1] = xr.multiply(yr).add(xi.multiply(yi)).add(hi[i - 1][i - 2].multiply(zzr));
            hi[i - 2][j - 1] = xr.multiply(yi).subtract(xi.multiply(yr)).add(hi[i - 1][i - 2].multiply(zzi));
            hr[i - 1][j - 1] = xr.multiply(zzr).subtract(xi.multiply(zzi)).subtract(hi[i - 1][i - 2].multiply(yr));
            hi[i - 1][j - 1] = xr.multiply(zzi).add(xi.multiply(zzr)).subtract(hi[i - 1][i - 2].multiply(yi));
          }
        }

        si = hi[en - 1][en - 1].clone();

        if (si.isZero() == false) {
          S norm = (hr[en - 1][en - 1].multiply(hr[en - 1][en - 1]).add(si.multiply(si))).sqrt();
          sr = hr[en - 1][en - 1].divide(norm);
          si = si.divide(norm);
          hr[en - 1][en - 1] = norm.clone();
          hi[en - 1][en - 1] = unit.createZero();
        }

        /*
         * Inverse operation (columns).
         */
        for (int j = lp1; j <= en; j++) {
          S xr = wr[j - 2].clone();
          S xi = wi[j - 2].clone();

          for (int i = l; i <= j; i++) {
            S yr = hr[i - 1][j - 2].clone();
            S yi = unit.createZero();
            S zzr = hr[i - 1][j - 1].clone();
            S zzi = hi[i - 1][j - 1].clone();
            if (i != j) {
              yi = hi[i - 1][j - 2].clone();
              hi[i - 1][j - 2] = xr.multiply(yi).add(xi.multiply(yr)).add(hi[j - 1][j - 2].multiply(zzi));
            }
            hr[i - 1][j - 2] = xr.multiply(yr).subtract(xi.multiply(yi)).add(hi[j - 1][j - 2].multiply(zzr));
            hr[i - 1][j - 1] = xr.multiply(zzr).add(xi.multiply(zzi)).subtract(hi[j - 1][j - 2].multiply(yr));
            hi[i - 1][j - 1] = xr.multiply(zzi).subtract(xi.multiply(zzr)).subtract(hi[j - 1][j - 2].multiply(yi));
          }
        }
        if (si.isZero()) {
          continue; // goto L240;
        }
        for (int i = l; i <= en; i++) {
          S yr = hr[i - 1][en - 1].clone();
          S yi = hi[i - 1][en - 1].clone();
          hr[i - 1][en - 1] = sr.multiply(yr).subtract(si.multiply(yi));
          hi[i - 1][en - 1] = sr.multiply(yi).add(si.multiply(yr));
        }
      } while (true); // goto L240;

      /*
       * A root found.
       */
      // L660:
      wr[en - 1] = hr[en - 1][en - 1].add(tr);
      wi[en - 1] = hi[en - 1][en - 1].add(ti); // //11/07/15:57
      en = enm1;
    } while (true); // goto L220;

    /*
     * Set error -- No convergence to an eigenvalue after 50 iterrations.
     */
  }

  /**
   * 複素数の絶対値を求めます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param re 複素数の実部
   * @param im 複素数の虚部
   * @return 複素数の絶対値
   */
  private static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> S complexAbs(final S re, final S im) {
    return (re.multiply(re).add(im.multiply(im))).sqrt();
  }
}