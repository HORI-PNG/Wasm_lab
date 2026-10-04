/*
 * Created on 2009/12/31
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.DoubleComplexMatrixUtil;
import org.mklab.nfc.matrix.DoubleMatrixUtil;
import org.mklab.nfc.scalar.DoubleComplexNumberUtil;
import org.mklab.nfc.scalar.DoubleNumberUtil;
import org.mklab.nfc.svd.DoubleComplexSingularValueDecomposer;


/**
 * 倍精度(double)型の複素行列の一般化固有値問題を解くためのユーティリティクラスです。
 * 
 * (現在未完成)
 * 
 * @author koga
 * @version $Revision$, 2009/12/31
 */
public final class DoubleComplexGeneralizedEigenSolverUtil {
  /**
   * 新しく生成された<code>DoubleComplexGeneralizedEigenSolverUtil</code>オブジェクトを初期化します。
   */
  private DoubleComplexGeneralizedEigenSolverUtil() {
    // nothing to do
  }

  /**
   * 一般の複素行列の一般化固有値問題(a x = lambda b x)を等価な問題 (aが上ヘッセンベルグ行列、bが上三角行列)に変換します。
   * 
   * @param ar a行列の実部
   * @param ai a行列の虚部
   * @param br b行列の実部
   * @param bi b行列の虚部
   * @param qr QZ分解のQ行列の実部
   * @param qi QZ分解のQ行列の虚部
   * @param zr QZ分解のZ行列の実部
   * @param zi QZ分解のZ行列の虚部
   * @param qDesired QZ分解のQ行列を求めるならば
   * @param zDesired QZ分解のZ行列を求めるならば
   */
  static void qzHes(final double[][] ar, final double[][] ai, final double[][] br, final double[][] bi, final double[][] qr, final double[][] qi, final double[][] zr, final double[][] zi, final boolean qDesired, final boolean zDesired) {
    final int size = ar.length;

    houseQr(br, bi, qr, qi);

    final double[][] a2r = DoubleMatrixUtil.clone(ar);
    final double[][] a2i = DoubleMatrixUtil.clone(ai);
    final double[][] qtr = DoubleMatrixUtil.transpose(qr);
    final double[][] qti = DoubleMatrixUtil.unaryMinus(DoubleMatrixUtil.transpose(qi));
    DoubleComplexMatrixUtil.multiply(ar, ai, qtr, qti, a2r, a2i);

    // z2 = C_MatIDef(n);
    final double[][] z2r = new double[size][size];
    final double[][] z2i = new double[size][size];
    for (int i = 0; i < size; i++) {
      z2r[i][i] = 1;
    }

    // p = C_MatDef("", 2, 2);
    double[][] pr = new double[2][2];
    double[][] pi = new double[2][2];
    
    // p2 = C_MatDef("", 2, 2);
    final double[][] p2r = new double[2][2];
    final double[][] p2i = new double[2][2];

    for (int j = 1; j <= size - 2; j++) {
      for (int i = size; i >= j + 2; i--) {
        // c_house_mat2(p, AC(i-1,j), AC(i,j), 1);
        houseMatrix2(pr, pi, ar[i - 2][j - 1], ai[i - 2][j - 1], ar[i - 1][j - 1], ai[i - 1][j - 1], 1);
        
        // c_left_diag_mul(a, p, i-2);
        multiplyDiagonalBlockFromLeft(ar, ai, pr, pi, i - 2);
        
        // c_left_diag_mul(b, p, i-2);
        multiplyDiagonalBlockFromLeft(br, bi, pr, pi, i - 2);
        
        if (qDesired) {
          // c_left_diag_mul(qt, p, i-2);
          multiplyDiagonalBlockFromLeft(qtr, qti, pr, pi, i - 2);
        }

        // ComplexValueConj(&c1_, BC(i,i-1));
        final double c1r = br[i - 1][i - 2];
        final double c1i = -bi[i - 1][i - 2];
        
        // ComplexValueConj(&c2_, BC(i,i));
        final double c2r = br[i - 1][i - 1];
        final double c2i = -bi[i - 1][i - 1];

        // c_house_mat2(p2, &c1_, &c2_, 2);
        houseMatrix2(p2r, p2i, c1r, c1i, c2r, c2i, 2);
        
        // C_Mat_ConjTrans(p, p2);
        pr = DoubleMatrixUtil.transpose(p2r);
        pi = DoubleMatrixUtil.unaryMinus(DoubleMatrixUtil.transpose(p2i));
        
        // c_right_diag_mul(a, p, i-2);
        multiplyDiagonalBlockFromRight(ar, ai, pr, pi, i - 2);
        
        // c_right_diag_mul(b, p, i-2);
        multiplyDiagonalBlockFromRight(br, bi, pr, pi, i - 2);
        if (qDesired || zDesired) {
          // c_right_diag_mul(z2, p, i-2);
          multiplyDiagonalBlockFromRight(z2r, z2i, pr, pi, i - 2);
        }
      }
    }


    // MatCopy(q, qt);
    DoubleMatrixUtil.copy(qtr, qr);
    DoubleMatrixUtil.copy(qti, qi);

    // MatCopy(z, z2);
    DoubleMatrixUtil.copy(z2r, zr);
    DoubleMatrixUtil.copy(z2i, zi);
  }

  /**
   * Golubの本を参考にしたコード
   * 
   * @param ar a行列の実部
   * @param ai a行列の虚部
   * @param br b行列の実部
   * @param bi b行列の虚部
   * @param qr QZ分解のQ行列の実部
   * @param qi QZ分解のQ行列の虚部
   * @param zr QZ分解のZ行列の実部
   * @param zi QZ分解のZ行列の虚部
   * @param qDesired QZ分解のQ行列を求めるならばtrue
   * @param zDesired QZ分解のZ行列を求めるならばtrue
   */
  private static void qzStep(final double[][] ar, final double[][] ai, final double[][] br, final double[][] bi, final double[][] qr, final double[][] qi, final double[][] zr, final double[][] zi, final boolean qDesired, final boolean zDesired) {
    final int size = ar.length;
    final double tolerance = DoubleComplexMatrixUtil.frobNorm(br, bi) * DoubleNumberUtil.EPS;
    final double[][][] bInv = DoubleComplexMatrixUtil.inverse(br, bi, tolerance, false);

    final double[][][] m = DoubleComplexMatrixUtil.multiply(ar, ai, bInv[0], bInv[1]);
    final double[][] mr = m[0];
    final double[][] mi = m[1];

    final double[][] tmp1r = DoubleMatrixUtil.getSubMatrix(mr, size - 2, size - 1, size - 2, size - 1);
    final double[][] tmp1i = DoubleMatrixUtil.getSubMatrix(mi, size - 2, size - 1, size - 2, size - 1);

    final double[][] valtmp = new DoubleComplexEigenSolver().getEigenValue(tmp1r, tmp1i);
    final double[][] valr = new double[][] { {valtmp[0][0]}, {valtmp[0][1]}};
    final double[][] vali = new double[][] { {valtmp[1][0]}, {valtmp[1][1]}};

    final double[][] eyer = new double[size][size];
    final double[][] eyei = new double[size][size];
    for (int i = 0; i < size; i++) {
      eyer[i][i] = 1;
    }

    final double[][][] alphaI = DoubleComplexMatrixUtil.multiply(eyer, eyei, valr[0][0], vali[0][0]);
    final double[][] alphaIr = alphaI[0];
    final double[][] alphaIi = alphaI[1];

    final double[][] mAlphaIr = DoubleMatrixUtil.subtract(mr, alphaIr);
    final double[][] mAlphaIi = DoubleMatrixUtil.subtract(mi, alphaIi);

    final double[][][] betaI = DoubleComplexMatrixUtil.multiply(eyer, eyei, valr[1][0], vali[1][0]);
    final double[][] betaIr = betaI[0];
    final double[][] betaIi = betaI[1];

    final double[][] mBetaIr = DoubleMatrixUtil.subtract(mr, betaIr);
    final double[][] mBetaIi = DoubleMatrixUtil.subtract(mi, betaIi);

    final double[][][] tmp5 = DoubleComplexMatrixUtil.multiply(mAlphaIr, mAlphaIi, mBetaIr, mBetaIi);
    final double[][] tmp4r = tmp5[0];
    final double[][] tmp4i = tmp5[1];

    double xxr = tmp4r[0][0];
    double xxi = tmp4i[0][0];
    double yyr = tmp4r[1][0];
    double yyi = tmp4i[1][0];

    if (3 <= size) {
      double zzr = tmp4r[2][0];
      double zzi = tmp4i[2][0];

      final double[][] qkr = new double[3][3];
      final double[][] qki = new double[3][3];
      final double[][] zk1tr = new double[3][3];
      final double[][] zk1ti = new double[3][3];
      final double[][] zk2tr = new double[2][2];
      final double[][] zk2ti = new double[2][2];
      final double[][] xyzr = new double[3][1];
      final double[][] xyzi = new double[3][1];

      final double[][] gr = new double[3][1];
      final double[][] gi = new double[3][1];

      for (int k = 1; k <= size - 2; k++) {
        xyzr[0][0] = xxr;
        xyzi[0][0] = xxi;
        xyzr[1][0] = yyr;
        xyzi[1][0] = yyi;
        xyzr[2][0] = zzr;
        xyzi[2][0] = zzi;

        houseMatrix(qkr, qki, xyzr, xyzi, 1);
        multiplyDiagonalBlockFromLeft(ar, ai, qkr, qki, k - 1);
        multiplyDiagonalBlockFromLeft(br, bi, qkr, qki, k - 1);
        if (qDesired == true) {
          multiplyDiagonalBlockFromLeft(qr, qi, qkr, qki, k - 1);
        }

        gr[0][0] = br[k + 1][k - 1];
        gi[0][0] = -bi[k + 1][k - 1];
        gr[1][0] = br[k + 1][k];
        gi[1][0] = -bi[k + 1][k];
        gr[2][0] = br[k + 1][k + 1];
        gi[2][0] = -bi[k + 1][k + 1];
        houseMatrix(zk1tr, zk1ti, gr, gi, 3);

        final double[][] zk1r = DoubleMatrixUtil.transpose(zk1tr);
        final double[][] zk1i = DoubleMatrixUtil.unaryMinus(DoubleMatrixUtil.transpose(zk1ti));

        multiplyDiagonalBlockFromRight(ar, ai, zk1r, zk1i, k - 1);
        multiplyDiagonalBlockFromRight(br, bi, zk1r, zk1i, k - 1);
        if (qDesired == true || zDesired == true) {
          multiplyDiagonalBlockFromRight(zr, zi, zk1r, zk1i, k - 1);
        }

        xxr = br[k][k - 1];
        xxi = -bi[k][k - 1];
        yyr = br[k][k];
        yyi = -bi[k][k];
        houseMatrix2(zk2tr, zk2ti, xxr, xxi, yyr, yyi, 2);

        final double[][] zk2r = DoubleMatrixUtil.transpose(zk2tr);
        final double[][] zk2i = DoubleMatrixUtil.unaryMinus(DoubleMatrixUtil.transpose(zk2ti));

        multiplyDiagonalBlockFromRight(ar, ai, zk2r, zk2i, k - 1);
        multiplyDiagonalBlockFromRight(br, bi, zk2r, zk2i, k - 1);
        if (qDesired == true || zDesired == true) {
          multiplyDiagonalBlockFromRight(zr, zi, zk2r, zk2i, k - 1);
       }

        xxr = ar[k][k - 1];
        xxi = ai[k][k - 1];
        yyr = ar[k + 1][k - 1];
        yyi = ai[k + 1][k - 1];
        if (k < size - 2) {
          zzr = ar[k + 2][k - 1];
          zzi = ai[k + 2][k - 1];
        }
      }
    } else {
      xxr = ar[0][0];
      xxi = ai[0][0];
      yyr = ar[1][0];
      yyi = ai[1][0];
    }

    final double[][] qnr = new double[2][2];
    final double[][] qni = new double[2][2];
    houseMatrix2(qnr, qni, xxr, xxi, yyr, yyi, 1);

    multiplyDiagonalBlockFromLeft(ar, ai, qnr, qni, size - 2);
    multiplyDiagonalBlockFromLeft(br, bi, qnr, qni, size - 2);
    if (qDesired == true) {
      multiplyDiagonalBlockFromLeft(qr, qi, qnr, qni, size - 2);
    }

    xxr = br[size - 1][size - 2];
    xxi = -bi[size - 1][size - 2];
    yyr = br[size - 1][size - 1];
    yyi = -bi[size - 1][size - 1];
    
    final double[][] znr = new double[2][2];
    final double[][] zni = new double[2][2];
    houseMatrix2(znr, zni, xxr, xxi, yyr, yyi, 2);
    final double[][] zntr = DoubleMatrixUtil.transpose(znr);
    final double[][] znti = DoubleMatrixUtil.unaryMinus(DoubleMatrixUtil.transpose(zni));

    multiplyDiagonalBlockFromRight(ar, ai, zntr, znti, size - 2);
    multiplyDiagonalBlockFromRight(br, bi, zntr, znti, size - 2);
    if (qDesired == true || zDesired == true) {
      multiplyDiagonalBlockFromRight(zr, zi, zntr, znti, size - 2);
    }
  }

  /**
   * 一般化固有値問題(a x = lambda b x)(aが上三角行列、bが上三角行列)の 一般化固有値を求めます。
   * 
   * @param aar a行列の実部
   * @param aai a行列の虚部
   * @param bbr b行列の実部
   * @param bbi b行列の虚部
   * @param valr 一般化固有値の実部
   * @param vali 一般化固有値の虚部
   * @param tolerance 許容誤差
   */
  static void qzVal(final double[][] aar, final double[][] aai, final double[][] bbr, final double[][] bbi, final double[] valr, final double[] vali, final double tolerance) {
    final int size = aar.length;

    for (int i = 1; i <= size; i++) {
      if (DoubleComplexNumberUtil.isZero(bbr[i - 1][i - 1], bbi[i - 1][i - 1], tolerance)) {
        if (DoubleComplexNumberUtil.isZero(aar[i - 1][i - 1], aai[i - 1][i - 1], tolerance)) {
          // ComplexValueSetNaN(VALC(i));
          valr[i - 1] = Double.NaN;
          vali[i - 1] = Double.NaN;
        } else {
          // ComplexValueSetInf(VALC(i));
          valr[i - 1] = Double.POSITIVE_INFINITY;
          vali[i - 1] = Double.POSITIVE_INFINITY;
        }
      } else {
        final double[] tmp = divideComplexNumber(aar[i - 1][i - 1], aai[i - 1][i - 1], bbr[i - 1][i - 1], bbi[i - 1][i - 1]);
        valr[i - 1] = tmp[0];
        vali[i - 1] = tmp[1];
        if (valr[i - 1] == 0 && vali[i - 1] == 0) {
          throw new RuntimeException(Messages.getString("DoubleComplexGeneralizedEigen.5")); //$NON-NLS-1$
        }
      }
    }
  }

  /**
   * 一般化固有値問題(a x = lambda b x)(aが上ヘッセンベルグ行列、bが上三角行列)を等価な問題(aが上三角行列、bが上三角行列)へ変換します。
   * 
   * @param ar a行列の実部
   * @param ai a行列の虚部
   * @param br b行列の実部
   * @param bi b行列の虚部
   * @param qr QZ分解のQ行列の実部
   * @param qi QZ分解のQ行列の虚部
   * @param zr QZ分解のZ行列の実部
   * @param zi QZ分解のZ行列の虚部
   * @param qDesired QZ分解のQ行列を求めるならばtrue
   * @param zDesired QZ分解のZ行列を求めるならばtrue
   * @param tolerance 許容誤差
   * @return 計算結果
   */
  static int qzIt(final double[][] ar, final double[][] ai, final double[][] br, final double[][] bi, final double[][] qr, final double[][] qi, final double[][] zr, final double[][] zi, final boolean qDesired, final boolean zDesired, final double[] tolerance) {
    int its = 0;

    final int size = ar.length;

    // Compute epsa
    double anorm = 0.0;
    double bnorm = 0.0;

    double ani;
    double bni;
    for (int i = 1; i <= size; i++) {
      if (i != 1) {
        ani = DoubleComplexNumberUtil.abs(ar[i - 1][i - 2], ai[i - 1][i - 2]);
      } else {
        ani = 0.0;
      }
      bni = 0.0;

      for (int j = 1; j <= size; j++) {
        ani += DoubleComplexNumberUtil.abs(ar[i - 1][j - 1], ai[i - 1][j - 1]);
        bni += DoubleComplexNumberUtil.abs(br[i - 1][j - 1], bi[i - 1][j - 1]);
      }

      if (ani > anorm) {
        anorm = ani;
      }
      if (bni > bnorm) {
        bnorm = bni;
      }
    }

    if (anorm == 0.0) {
      anorm = 1.0;
    }
    if (bnorm == 0.0) {
      bnorm = 1.0;
    }

    final double epsa = DoubleNumberUtil.EPS * anorm;
    tolerance[0] = DoubleNumberUtil.EPS * bnorm;

    int pi = 0;
    int qii = 0;
    int oldPi = 0;
    int oldQi = 0;
    
    while (qii < size) {
      if (oldPi != pi || oldQi != qii) {
        its = 0;
      }

      oldPi = pi;
      oldQi = qii;

      if (its == 30) {
        return size - pi - qii;
      }
      its++;

      int i;
      for (i = 1; i <= size; i++) {
        for (int j = 1; j <= size; j++) {
          if (i > j + 1) {
            // ComplexValueSetZero(AC(i,j));
            ar[i - 1][j - 1] = 0;
            ai[i - 1][j - 1] = 0;
          }
          if (i > j) {
            // ComplexValueSetZero(BC(i,j));
            br[i - 1][j - 1] = 0;
            bi[i - 1][j - 1] = 0;
          }
        }
      }

      for (i = 2; i <= size; i++) {
        if (DoubleComplexNumberUtil.abs(ar[i - 1][i - 2], ai[i - 1][i - 2]) <= epsa) {
          // ComplexValueSetZero(AC(i,i-1));
          ar[i - 1][i - 2] = 0;
          ai[i - 1][i - 2] = 0;
        }
      }

      for (i = 2; i <= size; i++) {
        double d1 = DoubleComplexNumberUtil.abs(ar[i - 2][i - 2], ai[i - 2][i - 2]);
        double d2 = DoubleComplexNumberUtil.abs(ar[i - 1][i - 1], ai[i - 1][i - 1]);
        if (d1 < 1.0) {
          d1 = 1.0;
        }
        if (d2 < 1.0) {
          d2 = 1.0;
        }

        if (DoubleComplexNumberUtil.abs(ar[i - 1][i - 2], ai[i - 1][i - 2]) <= epsa * (d1 + d2)) {
          // ComplexValueSetZero(AC(i,i-1));
          ar[i - 1][i - 2] = 0;
          ai[i - 1][i - 2] = 0;
        }
      }

      qii = size;
      for (i = size; i >= 2; i--) {
        if (DoubleComplexNumberUtil.abs(ar[i - 1][i - 2], ai[i - 1][i - 2]) > epsa) {
          qii = size - i;
          break;
        }
        
        // ComplexValueSetZero(AC(i,i-1));
        ar[i - 1][i - 2] = 0;
        ai[i - 1][i - 2] = 0;
      }

      pi = 0;
      for (i = size - qii; i >= 2; i--) {
        if (DoubleComplexNumberUtil.abs(ar[i - 1][i - 2], ai[i - 1][i - 2]) <= epsa) {
          // ComplexValueSetZero(AC(i,i-1));
          ar[i - 1][i - 2] = 0;
          ai[i - 1][i - 2] = 0;
          
          pi = i - 1;
          break;
        }
      }

//      new DoubleComplexMatrix(ar,ai).print("A");
//      System.out.println("pi = " + pi);
//      System.out.println("qii = " + qii);

      if (qii < size) {
        final double[][] b22r = DoubleMatrixUtil.getSubMatrix(br, pi, size - qii - 1, pi, size - qii - 1);
        final double[][] b22i = DoubleMatrixUtil.getSubMatrix(bi, pi, size - qii - 1, pi, size - qii - 1);

        if (new DoubleComplexSingularValueDecomposer().isSingular(b22r, b22i, DoubleNumberUtil.EPS * DoubleComplexMatrixUtil.frobNorm(b22r, b22i))) {
          // ComplexValueSetZero(AC(n-qii,n-qii-1));
          ar[size - qii - 1][size - qii - 2] = 0;
          ai[size - qii - 1][size - qii - 2] = 0;
        } else {
          final double[][] a22r = DoubleMatrixUtil.getSubMatrix(ar, pi, size - qii - 1, pi, size - qii - 1);
          final double[][] a22i = DoubleMatrixUtil.getSubMatrix(ai, pi, size - qii - 1, pi, size - qii - 1);
          // q22 = C_MatIDef(Rows(a22));
          // z22 = C_MatIDef(Rows(a22));
          final int tmpN = a22r.length;
          final double[][] q22r = new double[tmpN][tmpN];
          final double[][] q22i = new double[tmpN][tmpN];
          final double[][] z22r = new double[tmpN][tmpN];
          final double[][] z22i = new double[tmpN][tmpN];
          for (int nn = 0; nn < tmpN; nn++) {
            q22r[nn][nn] = 1;
            z22r[nn][nn] = 1;
          }

          qzStep(a22r, a22i, b22r, b22i, q22r, q22i, z22r, z22i, true, true);

          // A = diag(Ip, Q22, Iq) * A * diag(Ip, Z22, Iq)
          // B = diag(Ip, Q22, Iq) * B * diag(Ip, Z22, Iq)
          // Qt = diag(Ip, Q22, Iq) * Qt
          // Z = Z * diag(Ip, Z22, Iq)
          multiplyDiagonalBlockFromLeft(ar, ai, q22r, q22i, pi);
          multiplyDiagonalBlockFromLeft(br, bi, q22r, q22i, pi);

          if (qDesired == true) {
            multiplyDiagonalBlockFromLeft(qr, qi, q22r, q22i, pi);
          }

          multiplyDiagonalBlockFromRight(ar, ai, z22r, z22i, pi);
          multiplyDiagonalBlockFromRight(br, bi, z22r, z22i, pi);

          if (qDesired == true || zDesired == true) {
            multiplyDiagonalBlockFromRight(zr, zi, z22r, z22i, pi);
          }

        }
      }
    }

    return 0;
  }

  /**
   * 一般化固有値問題(a x = lambda b x)(aが上三角行列、bが上三角行列)の 一般化固有ベクトルを求めます。
   * 
   * @param ar a行列の実部
   * @param ai a行列の虚部
   * @param br b行列の実部
   * @param bi b行列の虚部
   * @param valr 一般化固有値の実部
   * @param vali 一般化固有値の虚部
   * @param vecr 一般化固有ベクトルの実部
   * @param veci 一般化固有ベクトルの虚部
   * @param tolerance 許容誤差
   * @return 計算結果
   */
  static int qzVec(final double[][] ar, final double[][] ai, final double[][] br, final double[][] bi, final double[] valr, final double[] vali, final double[][] vecr, final double[][] veci, final double tolerance) {
    int size = ar.length;

//    /*
//     * Sort eigenvalues with respect to the imaginary part of them so that the
//     * one which has plus imaginary part comes first than the complex conjugate
//     * one.
//     */
//    for (int i = 1; i < size; i++) {
//      for (int j = 1; j < size; j++) {
//        if (vali[j - 1] < vali[j]) {
//          // ComplexValueSwap(VALC(j), VALC(j+1));
//          final double tmpRe = valr[j - 1];
//          valr[j - 1] = valr[j];
//          valr[j] = tmpRe;
//          
//          final double tmpIm = vali[j - 1];
//          vali[j - 1] = vali[j];
//          vali[j] = tmpIm;
//        }
//      }
//    }
//
//    for (int i = 1; i < size; i++) {
//      for (int j = 1; j < size; j++) {
//        if (valr[j - 1] < valr[j]) {
//          // ComplexValueSwap(VALC(j), VALC(j+1));
//          final double tmpRe = valr[j - 1];
//          valr[j - 1] = valr[j];
//          valr[j] = tmpRe;
//          
//          final double tmpIm = vali[j - 1];
//          vali[j - 1] = vali[j];
//          vali[j] = tmpIm;
//        }
//      }
//    }

    int r = 0;
    double oldr = 0;
    double oldi = 0;
    final double[][] cr = new double[size][size];
    final double[][] ci = new double[size][size];
    final double[][] mubr = new double[size][size];
    final double[][] mubi = new double[size][size];

    for (int i = 1; i <= size; i++) {
      if (DoubleComplexNumberUtil.abs(valr[i - 1], vali[i - 1]) <= tolerance) {
        break;
      }

      final double dr = valr[i - 1] - oldr;
      final double di = vali[i - 1] - oldi;

      if (Math.sqrt(dr * dr + di * di) <= tolerance) {
        continue;
      }

      // ComplexValueCopy(&old_, VALC(i));
      oldr = valr[i - 1];
      oldi = vali[i - 1];

      // vc = ComplexValueToComp(VALC(i));
      final double vcr = valr[i - 1];
      final double vci = vali[i - 1];
      
      // C_Mat_ScaleC(mub, b, vc);
      DoubleComplexMatrixUtil.multiply(mubr, mubi, br, bi, vcr, vci); // ////

      // C_Mat_Sub(c, a, mub);
      DoubleComplexMatrixUtil.subtract(cr, ci, ar, ai, mubr, mubi);
      final double[][][] ker = new DoubleComplexSingularValueDecomposer().kernel(cr, ci, tolerance);
      
      // MatPut(vec, 1, r+1, ker);
      final int row2 = ker[0].length - 1;
      final int column2 = r + ker[0][0].length - 1;
      DoubleMatrixUtil.setSubMatrix(vecr, 0, row2, r, column2, ker[0]);

      final int row3 = ker[1].length - 1;
      final int column3 = r + ker[1][0].length - 1;
      DoubleMatrixUtil.setSubMatrix(veci, 0, row3, r, column3, ker[1]);

      // r += Cols(ker);
      r += ker[0][0].length;
    }

    if (r != size) {
      return r;
    }

    return 0;
  }

  /**
   * ハウスホルダー行列によりQR分解を行います。
   * 
   * @param ar 対象となる行列の実部
   * @param ai 対象となる行列の虚部
   * @param qr QR分解のQ行列の実部
   * @param qi QR分解のQ行列の虚部
   */
  private static void houseQr(final double[][] ar, final double[][] ai, final double[][] qr, final double[][] qi) {
    int size = ar.length;

    for (int i = 1; i <= size; i++) {
      for (int j = 1; j <= size; j++) {
        if (i == j) {
          qr[i - 1][j - 1] = 1;
          qi[i - 1][j - 1] = 0;
        } else {
          qr[i - 1][j - 1] = 0;
          qi[i - 1][j - 1] = 0;
        }
      }
    }

    for (int j = 1; j <= size; j++) {
      final double[][][] v = DoubleComplexHouseHolderUtil.houseHolderVector(DoubleMatrixUtil.getSubMatrix(ar, j - 1, size - 1, j - 1, j - 1), DoubleMatrixUtil.getSubMatrix(ai, j - 1, size - 1, j - 1, j - 1), 1);
      final double[][] vr = v[0];
      final double[][] vi = v[1];

      final double[][] tmpRe = DoubleMatrixUtil.getSubMatrix(ar, j - 1, size - 1, j - 1, size - 1);
      final double[][] tmpIm = DoubleMatrixUtil.getSubMatrix(ai, j - 1, size - 1, j - 1, size - 1);
      final double[][][] mul = DoubleComplexHouseHolderUtil.multiplyHouseHolderFromLeft(tmpRe, tmpIm, vr, vi);

      final int row2 = (j - 1) + mul[0].length - 1;
      final int col2 = (j - 1) + mul[0][0].length - 1;
      DoubleMatrixUtil.setSubMatrix(ar, j - 1, row2, j - 1, col2, mul[0]);

      final int row3 = (j - 1) + mul[1].length - 1;
      final int col3 = (j - 1) + mul[1][0].length - 1;
      DoubleMatrixUtil.setSubMatrix(ai, j - 1, row3, j - 1, col3, mul[1]);

      if (j < size) {
        final double d1 = ar[j - 1][j - 1];
        final double d2 = ai[j - 1][j - 1];

        final int col4 = (j - 1) + vr[0].length - 1;
        DoubleMatrixUtil.setSubMatrix(ar, j - 1, row2, j - 1, col4, vr);

        final int row5 = (j - 1) + vi.length - 1;
        final int col5 = (j - 1) + vi[0].length - 1;
        DoubleMatrixUtil.setSubMatrix(ai, j - 1, row5, j - 1, col5, vi);

        ar[j - 1][j - 1] = d1;
        ai[j - 1][j - 1] = d2;
      }
    }

    for (int j = size; j >= 1; j--) {
      final double[][] vr = DoubleMatrixUtil.getSubMatrix(ar, j - 1, size - 1, j - 1, j - 1);
      final double[][] vi = DoubleMatrixUtil.getSubMatrix(ai, j - 1, size - 1, j - 1, j - 1);
      
      // ComplexValueSetOne(V_C(1,1));
      vr[0][0] = 1;
      vi[0][0] = 0;
      final double[][] tmpr = DoubleMatrixUtil.getSubMatrix(qr, j - 1, size - 1, j - 1, size - 1);
      final double[][] tmpi = DoubleMatrixUtil.getSubMatrix(qi, j - 1, size - 1, j - 1, size - 1);
      final double[][][] mul = DoubleComplexHouseHolderUtil.multiplyHouseHolderFromLeft(tmpr, tmpi, vr, vi);

      final int row2 = (j - 1) + mul[0].length - 1;
      final int col2 = (j - 1) + mul[0][0].length - 1;
      DoubleMatrixUtil.setSubMatrix(qr, j - 1, row2, j - 1, col2, mul[0]);

      final int row3 = (j - 1) + mul[1].length - 1;
      final int col3 = (j - 1) + mul[1][0].length - 1;
      DoubleMatrixUtil.setSubMatrix(qi, j - 1, row3, j - 1, col3, mul[1]);
    }

    for (int i = 1; i <= size; i++) {
      for (int j = 1; j < i; j++) {
        ar[i - 1][j - 1] = 0;
        ai[i - 1][j - 1] = 0;
      }
    }
  }

  /**
   * ハウスホルダー行列を生成します。
   * 
   * @param pr ハウスホールダー行列の実部
   * @param pi ハウスホールダー行列の虚部
   * @param xr 対象とするベクトルの実部
   * @param xi 対象とするベクトルの虚部
   * @param number 非ゼロにする成分の番号(1から始まる)
   */
  private static void houseMatrix(final double[][] pr, final double[][] pi, final double[][] xr, final double[][] xi, final int number) {
    int size = xr.length;
    final double[][][] v = DoubleComplexHouseHolderUtil.houseHolderVector(DoubleMatrixUtil.clone(xr), DoubleMatrixUtil.clone(xi), number);
    final double[][] vr = v[0];
    final double[][] vi = v[1];

    final double[][] tmp1r = DoubleMatrixUtil.transpose(vr);
    final double[][] tmp1i = DoubleMatrixUtil.unaryMinus(DoubleMatrixUtil.transpose(vi));

    final double[] c = DoubleComplexMatrixUtil.scalarProduct(tmp1r, tmp1i, vr, vi); // //

    final double[][] tmp2r = DoubleMatrixUtil.multiply(vr, -2.0 / c[0]);
    final double[][] tmp2i = DoubleMatrixUtil.multiply(vi, -2.0 / c[0]);

    DoubleComplexMatrixUtil.multiply(pr, pi, tmp2r, tmp2i, tmp1r, tmp1i);

    while (size-- != 0) {
      pr[size][size] += 1.0;
    }
  }

  /**
   * 2次のハウスホルダー行列を生成します。
   * 
   * @param pr ハウスホルダー行列の実部
   * @param pi ハウスホルダー行列の虚部
   * @param ar 値1の実部
   * @param ai 値1の虚部
   * @param br 値2の実部
   * @param bi 値2の虚部
   * @param number 非ゼロにする成分の番号(1または2)
   */
  private static void houseMatrix2(final double[][] pr, final double[][] pi, final double ar, final double ai, final double br, final double bi, final int number) {
    final double[][][] v = houseVector2(ar, ai, br, bi, number);
    final double[][] vr = v[0];
    final double[][] vi = v[1];

    final int vcols = vr[0].length;
    int vc = 0;
    int vrow = 0;
    int vcol = 0;

    double dd = vr[vrow][vcol] * vr[vrow][vcol] + vi[vrow][vcol] * vi[vrow][vcol];
    
    vc++;
    vrow = vc / vcols;
    vcol = vc % vcols;
    
    // dd += vc->real*vc->real + vc->imag*vc->imag;
    dd += vr[vrow][vcol] * vr[vrow][vcol] + vi[vrow][vcol] * vi[vrow][vcol];

    dd = -2.0 / dd;

    int ppc = 0;
    final int pcols = pr[0].length;
    
    vc = 0;
    vrow = 0;
    vcol = 0;

    int m = 2;

    while (m-- != 0) {
      int n = 2;
      int vc2 = 0;
      while (n-- != 0) {
        final int prow = ppc / pcols;
        final int pcol = ppc % pcols;
        
        final int vc2row = vc2 / vcols;
        final int vc2col = vc2 % vcols;

        // ppc->real = dd * ( vc->real*vc2->real + vc->imag*vc2->imag);
        // ppc->imag = dd * (- vc->real*vc2->imag + vc->imag*vc2->real);
        pr[prow][pcol] = dd * (vr[vrow][vcol] * vr[vc2row][vc2col] + vi[vrow][vcol] * vi[vc2row][vc2col]);
        pi[prow][pcol] = dd * (-vr[vrow][vcol] * vi[vc2row][vc2col] + vi[vrow][vcol] * vr[vc2row][vc2col]);
        
        ppc++;
        vc2++;
      }
      vc++;
      vrow = vc / vcols;
      vcol = vc % vcols;
    }

    // P_R(1,1) += 1.0;
    // P_R(2,2) += 1.0;
    pr[0][0] += 1.0;
    pr[1][1] += 1.0;

  }

  /**
   * 2次のハウスホルダーベクトルを生成します。
   * 
   * @param c1r 値1の実部
   * @param c1i 値2の実部
   * @param c2r 値2の実部
   * @param c2i 値2の虚部
   * @param number 非ゼロにする成分の番号(1または2)
   * @return ハウスホルダーベクトル
   */
  private static double[][][] houseVector2(final double c1r, final double c1i, final double c2r, final double c2i, final int number) {
    double ynr = 0;
    double yni = 0;

    double mu = c1r * c1r + c1i * c1i;
    mu += c2r * c2r + c2i * c2i;

    double xnr, xni;
    if (number == 1) {
      xnr = c1r;
      xni = c1i;
    } else {
      xnr = c2r;
      xni = c2i;
    }

    final double a, b;
    final double d = xnr * xnr + xni * xni;
    if (d != 0.0) {
      double b2 = (mu * xni * xni) / d;
      a = Math.sqrt(mu - b2);
      b = Math.sqrt(b2);
    } else {
      a = Math.sqrt(mu);
      b = 0.0;
    }

    final double[][] vr, vi;
    if (mu != 0.0) {
      if (xnr > 0.0) {
        if (xnr * xni > 0.0) {
          xni += b;
        } else {
          xni -= b;
        }
        xnr += a;

      } else {
        if (xnr * xni > 0.0) {
          xni -= b;
        } else {
          xni += b;
        }

        xnr -= a;
      }

      final double xnl = Math.sqrt(xnr * xnr + xni * xni);
      xnr /= xnl;
      xni /= xnl;

      double dd = xnr * xnr + xni * xni;
      double f1r, f1i, f2r, f2i;

      if (number == 1) {
        // ynl = ComplexValueAbs(c2);
        final double ynl = DoubleComplexNumberUtil.abs(c2r, c2i);
        
        if (ynl != 0.0) {
          ynr = c2r / ynl;
          yni = c2i / ynl;
        }
        dd = (ynl / xnl) / dd;

        // ComplexValueSetValue(&f1_, 1.0, 0.0);
        f1r = 1;
        f1i = 0;
        
        // ComplexValueSetValue(&f2_,(xnr*ynr+xni*yni)*dd,(yni*xnr - ynr*xni)*dd);
        f2r = (xnr * ynr + xni * yni) * dd;
        f2i = (yni * xnr - ynr * xni) * dd;
      } else {
        // ynl = ComplexValueAbs(c1);
        final double ynl = DoubleComplexNumberUtil.abs(c1r, c1i);
        
        if (ynl != 0.0) {
          ynr = c1r / ynl;
          yni = c1i / ynl;
        }
        dd = (ynl / xnl) / dd;

        // ComplexValueSetValue(&f1_,(xnr*ynr+xni*yni)*dd,(yni*xnr - ynr*xni)*dd);
        f1r = (xnr * ynr + xni * yni) * dd;
        f1i = (yni * xnr - ynr * xni) * dd;
      
        // ComplexValueSetValue(&f2_, 1.0, 0.0);
        f2r = 1.0;
        f2i = 0.0;
      }

      // v = C_MatColumnVec(2, &f1_, &f2_);
      vr = new double[][] { {f1r}, {f2r}};
      vi = new double[][] { {f1i}, {f2i}};
    } else {
      // v = C_MatColumnVec(2, c1, c2);
      vr = new double[][] { {c1r}, {c2r}};
      vi = new double[][] { {c1i}, {c2i}};

      // ComplexValueSetOne(V_C(n,1));
      vr[number - 1][0] = 1;
      vi[number - 1][0] = 0;
    }

    return new double[][][] {vr, vi};
  }

  /**
   * ブロック対角成分に左から行列を掛けます。
   * 
   * @param ar 対象となる行列の実部
   * @param ai 対象となる行列の虚部
   * @param qr 対角行列の実部
   * @param qi 対角行列の虚部
   * @param n 対角成分の番号(0から始まる)
   */
  private static void multiplyDiagonalBlockFromLeft(final double[][] ar, final double[][] ai, final double[][] qr, final double[][] qi, final int n) {
    final int qn = qr[0].length;
    final int an = ar[0].length;
    final double[][] a22r = DoubleMatrixUtil.getSubMatrix(ar, n, n + qn - 1, 0, an - 1);
    final double[][] a22i = DoubleMatrixUtil.getSubMatrix(ai, n, n + qn - 1, 0, an - 1);
    final double[][][] tmp = DoubleComplexMatrixUtil.multiply(qr, qi, a22r, a22i);

    final int row2 = n + tmp[0].length - 1;
    final int col2 = tmp[0][0].length - 1;
    DoubleMatrixUtil.setSubMatrix(ar, n, row2, 0, col2, tmp[0]);
    DoubleMatrixUtil.setSubMatrix(ai, n, row2, 0, col2, tmp[1]);
  }

  /**
   * ブロック対角成分に右から行列を掛けます。
   * 
   * @param ar 対象となる行列の実部
   * @param ai 対象となる行列の虚部
   * @param zr 対角行列の実部
   * @param zi 対角行列の虚部
   * @param m 対角成分の番号(0から始まる)
   */
  private static void multiplyDiagonalBlockFromRight(final double[][] ar, final double[][] ai, final double[][] zr, final double[][] zi, final int m) {
    final int qm = zr[0].length;
    final int am = ar[0].length;

    final double[][] a2r = DoubleMatrixUtil.getSubMatrix(ar, 0, am - 1, m, m + qm - 1);
    final double[][] a2i = DoubleMatrixUtil.getSubMatrix(ai, 0, am - 1, m, m + qm - 1);
    final double[][][] tmp = DoubleComplexMatrixUtil.multiply(a2r, a2i, zr, zi);

    final int row2 = tmp[0].length - 1;
    final int col2 = m + tmp[0][0].length - 1;
    DoubleMatrixUtil.setSubMatrix(ar, 0, row2, m, col2, tmp[0]);
    DoubleMatrixUtil.setSubMatrix(ai, 0, row2, m, col2, tmp[1]);
  }

  /**
   * 複素数の割り算を行います。
   * 
   * @param ar 割られる数の実部
   * @param ai 割られる数の虚部
   * @param br 割る数の実部
   * @param bi 割る数の虚部
   * @return 割り算の結果
   */
  private static double[] divideComplexNumber(final double ar, final double ai, final double br, final double bi) {
    final double[] div = new double[2];

    if ((Math.abs(br) + Math.abs(bi)) == 0.0) {
      throw new RuntimeException(Messages.getString("DoubleComplexGeneralizedEigen.6")); //$NON-NLS-1$
    }

    if (Math.abs(br) > Math.abs(bi)) {
      final double d = bi / br;
      final double s = br + bi * d;
      div[0] = (ar + ai * d) / s;
      div[1] = (-ar * d + ai) / s;
    } else {
      final double d = br / bi;
      final double s = br * d + bi;
      div[0] = (ar * d + ai) / s;
      div[1] = (-ar + ai * d) / s;
    }
    return div;
  }

}
