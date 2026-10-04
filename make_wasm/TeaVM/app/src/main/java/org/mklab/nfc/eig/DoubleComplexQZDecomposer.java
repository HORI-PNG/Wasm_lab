/*
 * Created on 2009/12/31
 * Copyright (C) 2009 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.DoubleMatrixUtil;


/**
 * 倍精度(double)型の複素行列に関するQZ分解を行うためのクラスです。
 * 
 * @author koga
 * @version $Revision$, 2009/12/31
 */
public class DoubleComplexQZDecomposer {

  /**
   * 倍精度(double)の複素行列のQZ分解を返します。
   * 
   * @param aRe 対象となる複素行列Aの実部
   * @param aIm 対象となる複素行列Aの虚部
   * @param bRe 対象となる複素行列Bの実部
   * @param bIm 対象となる複素行列Bの虚部
   * @return QZ分解の結果
   */
  public final QZDecompositionDoubleComplexElements qzDecompose(final double[][] aRe, final double[][] aIm, final double[][] bRe, final double[][] bIm) {
    final int size = aRe.length;
    final double[][] aar = new double[size][size];
    final double[][] aai = new double[size][size];
    final double[][] bbr = new double[size][size];
    final double[][] bbi = new double[size][size];
    final double[][] qr = new double[size][size];
    final double[][] qi = new double[size][size];
    final double[][] zr = new double[size][size];
    final double[][] zi = new double[size][size];
    final double[][] vecRe = new double[size][size];
    final double[][] vecIm = new double[size][size];

    matQZ(aRe, aIm, bRe, bIm, aar, aai, bbr, bbi, qr, qi, zr, zi, vecRe, vecIm);

    return new QZDecompositionDoubleComplexElements(aar, aai, bbr, bbi, qr, qi, zr, zi, vecRe, vecIm);
  }

  /**
   * aとbのQZ分解を求めます。
   * 
   * @param ar a行列の実部
   * @param ai a行列の虚部
   * @param br b行列の実部
   * @param bi b行列の虚部
   * @param aar a行列に対応する上三角行列の実部
   * @param aai a行列に対応する上三角行列の虚部
   * @param bbr b行列に対応する上三角行列の実部
   * @param bbi b行列に対応する上三角行列の虚部
   * @param qr q行列の実部
   * @param qi q行列の虚部
   * @param zr z行列の実部
   * @param zi z行列の虚部
   * @param vecr 一般化固有ベクトルの実部
   * @param veci 一般化固有ベクトルの虚部
   */
  private void matQZ(final double[][] ar, final double[][] ai, final double[][] br, final double[][] bi, final double[][] aar, final double[][] aai, final double[][] bbr, final double[][] bbi, final double[][] qr, final double[][] qi, final double[][] zr,
      final double[][] zi, final double[][] vecr, final double[][] veci) {

    final int size = ar.length;

    final double[][] a2r = DoubleMatrixUtil.clone(ar);
    final double[][] a2i = DoubleMatrixUtil.clone(ai);
    final double[][] b2r = DoubleMatrixUtil.clone(br);
    final double[][] b2i = DoubleMatrixUtil.clone(bi);
    final double[][] qtr = new double[size][size];
    final double[][] qti = new double[size][size];

    DoubleComplexGeneralizedEigenSolverUtil.qzHes(a2r, a2i, b2r, b2i, qtr, qti, zr, zi, true, true);

    final double[] epsb = new double[1];

    final int errorCode1 = DoubleComplexGeneralizedEigenSolverUtil.qzIt(a2r, a2i, b2r, b2i, qtr, qti, zr, zi, true, true, epsb);

    if (errorCode1 != 0) {
      throw new RuntimeException(Messages.getString("DoubleComplexGeneralizedEigen.0")); //$NON-NLS-1$
    }

    final double[] valRe = new double[size];
    final double[] valIm = new double[size];

    DoubleComplexGeneralizedEigenSolverUtil.qzVal(a2r, a2i, b2r, b2i, valRe, valIm, epsb[0]);

    DoubleMatrixUtil.copy(DoubleMatrixUtil.transpose(qtr), qr);
    DoubleMatrixUtil.copy(DoubleMatrixUtil.unaryMinus(DoubleMatrixUtil.transpose(qti)), qi);

    for (int i = 1; i <= size; i++) {
      for (int j = 1; j <= size; j++) {
        if (i <= j) {
          aar[i - 1][j - 1] = a2r[i - 1][j - 1];
          aai[i - 1][j - 1] = a2i[i - 1][j - 1];
          bbr[i - 1][j - 1] = b2r[i - 1][j - 1];
          bbi[i - 1][j - 1] = b2i[i - 1][j - 1];
        } else {
          aar[i - 1][j - 1] = 0;
          aai[i - 1][j - 1] = 0;
          bbr[i - 1][j - 1] = 0;
          bbi[i - 1][j - 1] = 0;
        }
      }
    }
    
    final int errorCode2 = DoubleComplexGeneralizedEigenSolverUtil.qzVec(ar, ai, br, bi, valRe, valIm, vecr, veci, epsb[0]);
    
    if (errorCode2 != 0) {
      throw new RuntimeException(Messages.getString("DoubleComplexGeneralizedEigen.1")); //$NON-NLS-1$
    }
    
//    /*
//     * Sort eigenvalues with respect to the imaginary part of them so that the
//     * one which has plus imaginary part comes first than the complex conjugate
//     * one.
//     */
//    for (int i = 1; i < size; i++) {
//      for (int j = 1; j < size; j++) {
//        if (valIm[j - 1] < valIm[j]) {
//          final double tmpRe = valRe[j - 1];
//          final double tmpIm = valIm[j - 1];
//          valRe[j - 1] = valRe[j];
//          valIm[j - 1] = valIm[j];
//          valRe[j] = tmpRe;
//          valIm[j] = tmpIm;
//          DoubleMatrixUtil.exchangeColumn(vecr, j-1, j);
//          DoubleMatrixUtil.exchangeColumn(veci, j-1, j);
//        }
//      }
//    }
//
//    for (int i = 1; i < size; i++) {
//      for (int j = 1; j < size; j++) {
//        if (valRe[j - 1] < valRe[j]) {
//          final double tmpRe = valRe[j - 1];
//          final double tmpIm = valIm[j - 1];
//          valRe[j - 1] = valRe[j];
//          valIm[j - 1] = valIm[j];
//          valRe[j] = tmpRe;
//          valIm[j] = tmpIm;
//          DoubleMatrixUtil.exchangeColumn(vecr, j-1, j);
//          DoubleMatrixUtil.exchangeColumn(veci, j-1, j);
//        }
//      }
//    }

  }

}
