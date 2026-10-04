/*
 * $Id: Chol.java,v 1.26 2008/07/16 15:40:00 koga Exp $
 * 
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.leq;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.DoubleComplexNumber;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * コレスキー分解を求めるクラスです。
 * 
 * <p> Cholesky decomposition
 * 
 * @param <M> 行列の型
 * @param <S> スカラーの型
 * @author koga
 * @version $Revision: 1.26 $
 */
public class CholeskyDecomposer<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {

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
  public final M decompose(final M A, final S tolerance) {    
    final int n = A.getColumnSize();

    final S dx = A.subtract(A.conjugateTranspose()).absElementWise().max();

    if (dx.isGreaterThan(tolerance)) {
      System.err.println(Messages.getString("Chol.0") + dx); //$NON-NLS-1$
      System.err.println(Messages.getString("Chol.1")); //$NON-NLS-1$
      throw new IllegalArgumentException("CholeskyDecomposer.decompose()"); //$NON-NLS-1$
    }

    final M R = A.createClone();
    for (int j = 1; j <= n; j++) {
      if (j > 1) {
        R.setSubMatrix(j, n, j, j, R.getSubMatrix(j, n, j, j).subtract(R.getSubMatrix(j, n, 1, j - 1).multiply(R.getSubMatrix(j, j, 1, j - 1).conjugateTranspose())));
      }
      final S element = R.getElement(j, j);
      if (element instanceof DoubleComplexNumber) {
        if (((DoubleComplexNumber)element).getRealPart().doubleValue() < 0) {
          throw new IllegalArgumentException(Messages.getString("Chol.2")); //$NON-NLS-1$
        }
      } else if (element.isLessThan(0)) {
        throw new IllegalArgumentException(Messages.getString("Chol.2")); //$NON-NLS-1$
      }
      R.setSubMatrix(j, n, j, j, R.getSubMatrix(j, n, j, j).divide(R.getElement(j, j).sqrt()));
    }

    for (int j = 1; j <= n - 1; j++) {
      R.setSubMatrix(j, j, j + 1, R.getColumnSize(), A.createZero(1, n - j));
    }

    final S dr = R.multiply(R.conjugateTranspose()).subtract(A).absElementWise().max();
    if (dr.isGreaterThan(tolerance)) {
      System.err.println(Messages.getString("Chol.3") + dr); //$NON-NLS-1$
      System.err.println(Messages.getString("Chol.4")); //$NON-NLS-1$
    }

    return R.conjugateTranspose();
  }
}
