/*
 * Created on 2005/08/25
 * Copyright (C) 2005 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.leq;

import org.mklab.nfc.matrix.AbstractNumericalMatrixUtil;
import org.mklab.nfc.matrix.GridUtil;
import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.NumericalScalar;
import org.mklab.nfc.scalar.Scalar;


/**
 * 逆行列をガウスの消去法で求めるためのクラスです。
 * 
 * @author koga
 * @version $Revision: 1.5 $, 2005/08/25
 * 
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public class GaussianEliminationSolver<S extends Scalar<S,M>, M extends Matrix<S,M>> {
  /**
   * 逆行列をガウスの消去法で求めます。
   * 
   * 
   * @param matrix 逆行列を求める行列
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return 逆行列と行列式
   */
  public final GaussianEliminationElements<S,M> inverse(final S[][] matrix, final double tolerance, final boolean stopIfSingular) {
    final S det;
    final S[][] ans = GridUtil.clone(matrix);
    final int rowSize = matrix.length;

    if (rowSize == 1) {
      det = ans[0][0].clone();
      ans[0][0] = det.inverse();
      return new GaussianEliminationElements<>(ans, det);
    }

    final int[] work = new int[rowSize];
    S w1 = ans[0][0].createUnit();
    for (int i = 0; i < rowSize; i++) {
      work[i] = i;
    }

    for (int k = 0; k < rowSize; k++) {
      int r = k;
      if (ans[0][0] instanceof NumericalScalar) {
        final int wMaxSize = rowSize - k;
        if (0 < wMaxSize) {
          final S[][] ws = ans[0][0].createArray(wMaxSize, 1);
          for (int j = 0; j < wMaxSize; j++) {
            ws[j][0] = ans[k + j][k].clone();
          }
          final int[] wMax = AbstractNumericalMatrixUtil.indexOfMaximum(ws);
          r = k + wMax[0] - 1;
        }
      } else {
        for (int i = k; i < rowSize; i++) {
          if (ans[i][k].isZero() == false) {
            r = i;
            break;
          }
        }
      }

      S pivot = ans[r][k].clone();

      w1 = w1.multiply(pivot);

      if (r != k) {
        w1 = w1.unaryMinus();
        final int iw = work[k];
        work[k] = work[r];
        work[r] = iw;

        for (int j = 0; j < rowSize; j++) {
          S w = ans[k][j];
          ans[k][j] = ans[r][j];
          ans[r][j] = w;
        }
      }

      for (int i = 0; i < rowSize; i++) {
        ans[k][i] = ans[k][i].divide(pivot);
      }

      for (int i = 0; i < rowSize; i++) {
        if (i != k) {
          final S w = ans[i][k].clone();

          if (!w.isZero()) {
            for (int j = 0; j < rowSize; j++) {
              if (j != k) {
                ans[i][j] = ans[i][j].subtract(w.multiply(ans[k][j]));
              }
            }

            ans[i][k] = w.unaryMinus().divide(pivot);
          }
        }
      }

      ans[k][k] = pivot.inverse();
    }

    for (int i = 0; i < rowSize; i++) {
      while (true) {
        final int k = work[i];
        if (k == i) {
          break;
        }

        final int iw = work[k];
        work[k] = work[i];
        work[i] = iw;

        for (int j = 0; j < rowSize; j++) {
          final S w = ans[j][i];
          ans[j][i] = ans[j][k];
          ans[j][k] = w;
        }
      }
    }

    det = w1;
    return new GaussianEliminationElements<>(ans, det);
  }

}
