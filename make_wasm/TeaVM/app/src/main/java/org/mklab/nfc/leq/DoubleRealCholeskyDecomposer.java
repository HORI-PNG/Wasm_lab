/*
 * $Id: Chol.java,v 1.26 2008/07/16 15:40:00 koga Exp $
 * 
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.leq;

import org.mklab.nfc.matrix.DoubleMatrix;


/**
 * 倍精度(double)型の実行列のコレスキー分解を求めるクラスです。
 * 
 * <p> Cholesky decomposition
 * 
 * @author koga
 * @version $Revision: 1.26 $
 */
public class DoubleRealCholeskyDecomposer {

  /**
   * 対称行列のコレスキー分解を計算します。
   * 
   * <p>もし、<code>A</code>が正定なら、
   * 
   * <blockquote>R'*R = A</blockquote>
   * 
   * を満たす、上三角行列<code>R</code>を求めます。 このメソッドは<code>A</code>の対角と上三角部分のみを利用します。 下三角部分は、上三角部分の転置であると仮定されます。
   * 
   * @param A 対象となる行列
   * @param tolerance 許容誤差
   * @return R コレスキー分解 (cholesky decomposition)
   */
  public final DoubleMatrix decompose(final DoubleMatrix A, double tolerance) {
    final int n = A.getColumnSize();

    final double dx = ((A.subtract(A.transpose()).absElementWise())).max().doubleValue();

    if (dx > tolerance) {
      System.err.println(Messages.getString("Chol.0") + dx); //$NON-NLS-1$
      System.err.println(Messages.getString("Chol.1")); //$NON-NLS-1$
      throw new IllegalArgumentException("DoubleRealCholeskyDecomposer.decompose()"); //$NON-NLS-1$
    }

    final DoubleMatrix R = A.createClone();
    for (int j = 1; j <= n; j++) {
      if (j > 1) {
        R.setSubMatrix(j, n, j, j, R.getSubMatrix(j, n, j, j).subtract(R.getSubMatrix(j, n, 1, j - 1).multiply(R.getSubMatrix(j, j, 1, j - 1).conjugateTranspose())));
      }
      if (R.getElement(j, j).doubleValue() < 0) {
        throw new IllegalArgumentException(Messages.getString("Chol.2")); //$NON-NLS-1$
      }
      R.setSubMatrix(j, n, j, j, R.getSubMatrix(j, n, j, j).divide(R.getElement(j, j).sqrt()));
    }

    for (int j = 1; j <= n - 1; j++) {
      R.setSubMatrix(j, j, j + 1, R.getColumnSize(), A.createZero(1, n - j));
    }
    
    final double dr = (((R.multiply(R.conjugateTranspose()).subtract(A)).absElementWise())).max().doubleValue();
    if (dr > tolerance) {
      System.err.println(Messages.getString("Chol.3") + dr); //$NON-NLS-1$
      System.err.println(Messages.getString("Chol.4")); //$NON-NLS-1$
    }

    return R.transpose();
  }
}
