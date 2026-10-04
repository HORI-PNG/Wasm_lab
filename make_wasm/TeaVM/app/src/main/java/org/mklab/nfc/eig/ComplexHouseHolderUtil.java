/*
 * $Id: ComplexHouseHolder.java,v 1.3 2008/03/15 06:49:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.eig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mklab.nfc.matrix.BaseMatrixUtil;
import org.mklab.nfc.matrix.AbstractNumericalMatrixUtil;
import org.mklab.nfc.matrix.GridUtil;
import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;
import org.mklab.nfc.scalar.Scalar;


/**
 * 複素行列のハウスホルダー変換を行うためのクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.3 $
 */
public final class ComplexHouseHolderUtil {

  /**
   * 新しく生成された<code>ComplexHouseHolder</code>オブジェクトを初期化します。
   */
  private ComplexHouseHolderUtil() {
    // nothing to do
  }

  /**
   * 複素行列のハウスホルダー行列を作るためのベクトルを返します。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param xRe 複素ベクトルの実部
   * @param xIm 複素ベクトルの虚部
   * @param size ベクトルの成分の数
   * @return 複素行列のハウスホルダー行列を作るためのベクトル
   */
  public static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> List<S[][]> houseHolderVector(final S[][] xRe, final S[][] xIm, final int size) {
    final S unit = xRe[0][0].createUnit();

    S d = AbstractNumericalMatrixUtil.frobNorm(xRe, xIm);
    d = d.multiply(d);

    S xnRe = xRe[size - 1][0];
    S xnIm = xIm[size - 1][0];
    S dd = xnRe.multiply(xnRe).add(xnIm.multiply(xnIm));

    final S a, b;

    if (dd.isZero() == false) {
      final S b2 = (d.multiply(xnIm).multiply(xnIm)).divide(dd);
      final S a2 = d.subtract(b2);
      a = a2.sqrt();
      b = b2.sqrt();
    } else {
      a = d.sqrt();
      b = unit.createZero();
    }

    if (dd.isZero() == false) {
      if (xnRe.isGreaterThan(0)) {
        if ((xnRe.multiply(xnIm)).isGreaterThan(0)) {
          xnIm = xnIm.add(b);
        } else {

          xnIm = xnIm.subtract(b);
        }
        xnRe = xnRe.add(a);
      } else {
        if ((xnRe.multiply(xnIm)).isGreaterThan(0)) {

          xnIm = xnIm.subtract(b);
        } else {

          xnIm = xnIm.add(b);
        }
        xnRe = xnRe.subtract(a);
      }

      dd = xnRe.multiply(xnRe).add(xnIm.multiply(xnIm));
      xnRe = xnRe.divide(dd);
      xnIm = xnIm.unaryMinus().divide(dd);
      multiplySelf(xRe, xIm, xnRe, xnIm);
    }

    xnRe = unit.createUnit();
    xnIm = unit.createZero();
    xRe[size - 1][0] = xnRe;
    xIm[size - 1][0] = xnIm;
    
    final List<S[][]> ans = new ArrayList<>();
    ans.add(xRe);
    ans.add(xIm);
    return ans;
  }

  /**
   * ハウスホルダー行列を左から掛けます。
   * 
   * <blockquote> A = P * A </blockquote>
   * 
   * <blockquote> P = I - 2v*v<sup>T</sup>/(v<sup>T</sup>*v) </blockquote>
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param aRe 変換される行列の実部
   * @param aIm 変換される行列の虚部
   * @param vRe ハウスホルダー行列を作るためのベクトルの実部
   * @param vIm ハウスホルダー行列を作るためのベクトルの虚部
   * @return ハウスホルダー行列を左から掛けた結果
   */
  public static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> List<S[][]> multiplyHouseHolderFromLeft(final S[][] aRe, final S[][] aIm, final S[][] vRe, final S[][] vIm) {
    final S unit = aRe[0][0].createUnit();

    final S dd = AbstractNumericalMatrixUtil.frobNorm(vRe, vIm);
    final S beta = unit.unaryMinus().multiply(2).divide(dd.multiply(dd));

    final S[][] vtRe = GridUtil.transpose(vRe);
    final S[][] vtIm = BaseMatrixUtil.unaryMinus(GridUtil.transpose(vIm));

    final S[][][] vtA = AbstractNumericalMatrixUtil.multiply(vtRe, vtIm, aRe, aIm);
    final S[][] vtARe = vtA[0];
    final S[][] vtAIm = vtA[1];

    multiplySelf(vtARe, beta);
    multiplySelf(vtAIm, beta);
    final S[][] wtRe = vtARe;
    final S[][] wtIm = vtAIm;

    final S[][][] vWt = AbstractNumericalMatrixUtil.multiply(vRe, vIm, wtRe, wtIm);
    final S[][] vWtRe = vWt[0];
    final S[][] vWtIm = vWt[1];

    final S[][] a2Re = BaseMatrixUtil.add(aRe, vWtRe);
    final S[][] a2Im = BaseMatrixUtil.add(aIm, vWtIm);
    
    final List<S[][]> ans = new ArrayList<>();
    ans.add(a2Re);
    ans.add(a2Im);
    return ans;
  }

  /**
   * ハウスホルダー行列を右から掛けます。
   * 
   * <blockquote> A = A * P </blockquote>
   * 
   * <blockquote> P = I - 2v*v<sup>T</sup>/(v<sup>T</sup>*v) </blockquote>
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * @param aRe 変換される行列の実部
   * @param aIm 変換される行列の虚部
   * @param vRe ハウスホルダー行列を作るためのベクトルの実部
   * @param vIm ハウスホルダー行列を作るためのベクトルの虚部
   * @return ハウスホルダー行列を左から掛けた結果
   */
  public static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> List<S[][]> multiplyHouseHolderFromRight(final S[][] aRe, final S[][] aIm, final S[][] vRe, final S[][] vIm) {
    final S unit = aRe[0][0].createUnit();

    final S dd = AbstractNumericalMatrixUtil.frobNorm(vRe, vIm);
    final S beta = unit.unaryMinus().multiply(2).divide(dd.multiply(dd));

    final S[][] vtRe = GridUtil.transpose(vRe);
    final S[][] vtIm = BaseMatrixUtil.unaryMinus(GridUtil.transpose(vIm));

    final S[][][] aV = AbstractNumericalMatrixUtil.multiply(aRe, aIm, vRe, vIm);
    final S[][] aVRe = aV[0];
    final S[][] aVIm = aV[1];

    multiplySelf(aVRe, beta);
    multiplySelf(aVIm, beta);
    final S[][] wRe = aVRe;
    final S[][] wIm = aVIm;

    final S[][][] wVt = AbstractNumericalMatrixUtil.multiply(wRe, wIm, vtRe, vtIm);
    final S[][] wVtRe = wVt[0];
    final S[][] wVtIm = wVt[1];

    final S[][] a2Re = BaseMatrixUtil.add(aRe, wVtRe);
    final S[][] a2Im = BaseMatrixUtil.add(aIm, wVtIm);
    return Arrays.asList(a2Re, a2Im);
  }

  /**
   * 行列自身に数を掛けます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型 
   * @param matrix 対象となる行列
   * @param scalar 乗じる数
   */
  private static <S extends Scalar<S,M>, M extends Matrix<S,M>> void multiplySelf(final S[][] matrix, final S scalar) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    for (int i = 0; i < rowSize; i++) {
      final S[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        matrixi[j] = matrixi[j].multiply(scalar);
      }
    }
  }

  /**
   * 行列に複素数を乗じます。
   * 
   * @param <S> 成分の型
   * @param <M> 行列の型
   * 
   * @param matrixRe 対象となる行列の実部
   * @param matrixIm 対象となる行列の虚部
   * @param valueRe 複素数の実部
   * @param valueIm 複素数の虚部
   */
  private static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> void multiplySelf(final S[][] matrixRe, final S[][] matrixIm, final S valueRe, final S valueIm) {
    final int rowSize = matrixRe.length;
    final int columnSize = rowSize == 0 ? 0 : matrixRe[0].length;

    for (int i = 0; i < rowSize; i++) {
      final S[] ari = matrixRe[i];
      final S[] aii = matrixIm[i];
      for (int j = 0; j < columnSize; j++) {
        final S tmp = ari[j].multiply(valueRe).subtract(aii[j].multiply(valueIm));
        aii[j] = ari[j].multiply(valueIm).add(aii[j].multiply(valueRe));
        ari[j] = tmp;
      }
    }
  }

//  /**
//   * 行列に実数を乗じます。
//   * 
//   * @param <E> 成分の型
//   * 
//   * @param matrixRe 対象となる行列の実部
//   * @param matrixIm 対象となる行列の虚部
//   * @param scalar 実数
//   */
//  static <E extends NumericalScalar<E>> void multiplySelf(final E[][] matrixRe, final E[][] matrixIm, final E scalar) {
//    final int rowSize = matrixRe.length;
//    final int columnSize = rowSize == 0 ? 0 : matrixRe[0].length;
//
//    for (int i = 0; i < rowSize; i++) {
//      final E[] ari = matrixRe[i];
//      final E[] aii = matrixIm[i];
//      for (int j = 0; j < columnSize; j++) {
//        ari[j] = ari[j].multiply(scalar);
//        aii[j] = aii[j].multiply(scalar);
//      }
//    }
//  }
}