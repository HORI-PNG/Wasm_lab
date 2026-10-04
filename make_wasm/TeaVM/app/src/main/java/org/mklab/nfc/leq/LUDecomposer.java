/*
 * $Id: LuDecomposition.java,v 1.5 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.leq;

import org.mklab.nfc.matrix.GridUtil;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * 数値行列のLU分解(P*A=L*U)を求めるためのクラスです。
 * 
 * @author koga
 * @version $Revision: 1.5 $
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public final class LUDecomposer<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> {
  

  /**
   * 数値行列のLU分解を行います。
   * 
   * 
   * @param a 数値行列
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return LU分解の結果
   */
  private LuIndexSign<S,M> luDecompose(final S[][] a, final S tolerance, final boolean stopIfSingular) {
    final int rowSize = a.length;
    final int columnSize = a[0].length;

    final S[][] lu = GridUtil.clone(a);

    final int[] index = new int[rowSize];
    final S[] scaling = a[0][0].abs().createArray(rowSize);
    final int[] sign = new int[] {1};

    for (int i = 0; i < rowSize; i++) {
      S max = lu[i][0].abs();
      for (int j = 1; j < columnSize; j++) {
        if (lu[i][j].abs().isGreaterThan(max)) {
          max = lu[i][j].abs();
        }
      }
      if (max.isZero(tolerance.abs())) {
        if (stopIfSingular) {
          throw new IllegalArgumentException(Messages.getString("LuDecomposition.0")); //$NON-NLS-1$
        }
        System.err.println(Messages.getString("LuDecomposition.0")); //$NON-NLS-1$
        final S eps = max.getMachineEpsilon();
        if (max.isZero(eps)) {
          max = max.add(eps);
        }
      }
      scaling[i] = max.inverse();
    }

    for (int j = 0; j < columnSize; j++) {
      for (int i = 0; i < j; i++) {
        S sum = lu[i][j];
        for (int k = 0; k < i; k++) {
          sum = sum.subtract(lu[i][k].multiply(lu[k][j]));
        }
        lu[i][j] = sum.clone();
      }

      S max = a[0][0].abs().createZero();
      int maxIndex = 0;

      for (int i = j; i < rowSize; i++) {
        S sum = lu[i][j];
        for (int k = 0; k < j; k++) {
          sum = sum.subtract(lu[i][k].multiply(lu[k][j]));
        }
        lu[i][j] = sum;

        final S temp = scaling[i].multiply(sum.abs());
        if (temp.isGreaterThanOrEquals(max)) {
          max = temp;
          maxIndex = i;
        }
      }

      if (j != maxIndex) {
        for (int k = 0; k < columnSize; k++) {
          final S temp = lu[maxIndex][k].clone();
          lu[maxIndex][k] = lu[j][k].clone();
          lu[j][k] = temp;
        }
        sign[0] = -sign[0];
        scaling[maxIndex] = scaling[j];
      }

      index[j] = maxIndex;

      if (lu[j][j].isZero()) {
        if (stopIfSingular) {
          throw new IllegalArgumentException(Messages.getString("LuDecomposition.0")); //$NON-NLS-1$
        }
        System.err.println(Messages.getString("LuDecomposition.0")); //$NON-NLS-1$
        lu[j][j] = (a[0][0].createZero()).add(a[0][0].getMachineEpsilon());
      }

      if (j != columnSize) {
        final S temp = lu[j][j].inverse();

        for (int i = j + 1; i < rowSize; i++) {
          lu[i][j] = lu[i][j].multiply(temp);
        }
      }
    }

    return new LuIndexSign<>(lu, index, sign);
  }

  /**
   * 数値行列のLU分解を返します。
   * 
   * <p>数値行列をA、下三角行列を行置換した行列をL、 上三角行列をUとすると、これらの行列の間には、
   * 
   * <blockquote> A = Q * H * Q<sup>#</sup> </blockquote>
   * 
   * <blockquote> Q<sup>#</sup> * Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param a 数値行列
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return {L, U}
   */
  public LUDecompostionElements<S,M> decompose(final S[][] a, final S tolerance, final boolean stopIfSingular) {
    final LuIndexSign<S,M> luDecompostion = luDecompose(a, tolerance, stopIfSingular);
    final S[][] lu = luDecompostion.getLu();
    final int[] index = luDecompostion.getIndex();

    final int rowSize = a.length;
    final int columnSize = a[0].length;

    final S[][] lower = a[0][0].createArray(rowSize, columnSize);
    final S[][] upper = a[0][0].createArray(rowSize, columnSize);

    // l行列とu行列に分解
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        for (int k = 0; k < columnSize; k++) {
          if (k < i) {
            lower[i][k] = lu[i][k];
            upper[i][k] = a[0][0].createZero();
          } else if (k == i) {
            lower[i][k] = a[0][0].createUnit();
            upper[i][k] = lu[i][k];
          } else {
            lower[i][k] = a[0][0].createZero();
            upper[i][k] = lu[i][k];
          }
        }
      }
    }

    // 行入れ替え
    for (int i = rowSize - 1; i >= 0; i--) {
      int k = index[i];
      if (k != i) {
        for (int j = 0; j < rowSize; j++) {
          final S tmp = lower[i][j];
          lower[i][j] = lower[k][j];
          lower[k][j] = tmp;
        }
      }

    }

    return new LUDecompostionElements<>(lower, upper);
  }

  /**
   * 数値行列の並べ替え付きLU分解を返します。
   * 
   * <p>数値行列をA、下三角行列を行置換した行列をL、上三角行列をU、置換行列をPとすると、これらの行列の間には、
   * 
   * <blockquote> P * A = Q * H * Q<sup>#</sup> </blockquote>
   * 
   * <blockquote> Q<sup>#</sup> * Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * 
   * @param a 数値行列
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return {L, U, P}
   */
  public LUDecompostionElements<S,M> decomposeWithPermutation(final S[][] a, final S tolerance, final boolean stopIfSingular) {
    final LuIndexSign<S,M> luDecompostion = luDecompose(a, tolerance, stopIfSingular);
    final S[][] lu = luDecompostion.getLu();
    final int[] index = luDecompostion.getIndex();

    final int rowSize = a.length;
    final int columnSize = a[0].length;

    final S[][] lower = a[0][0].createArray(rowSize, columnSize);
    final S[][] upper = a[0][0].createArray(columnSize, columnSize);

    //lower
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        for (int k = 0; k < columnSize; k++) {
          if (k < i) {
            lower[i][k] = lu[i][k];
          } else if (k == i) {
            lower[i][k] = a[0][0].createUnit();
          } else {
            lower[i][k] = a[0][0].createZero();
          }
        }
      }
    }

    //upper
    for (int i = 0; i < columnSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        for (int k = 0; k < columnSize; k++) {
          if (k < i) {
            upper[i][k] = a[0][0].createZero();
          } else {
            upper[i][k] = lu[i][k];
          }
        }
      }
    }

    // 行入れ替え
    final int[][] p = new int[rowSize][rowSize];
    for (int i = 0; i < rowSize; i++) {
      p[i][i] = 1;
    }

    for (int i = 0; i < rowSize; i++) {
      int k = index[i];
      if (k != i) {
        for (int j = 0; j < rowSize; j++) {
          final int tmp = p[i][j];
          p[i][j] = p[k][j];
          p[k][j] = tmp;
        }
      }
    }

    return new LUDecompostionElements<>(lower, upper, p);
  }

  /**
   * 線形方程式の解を返します。
   * 
   * @param lu 変数ベクトルの係数行列のLU分解の結果
   * @param index 変数ベクトルの係数行列のLU分解の結果
   * @param b 右辺のベクトルの
   * @return 解
   */
  private S[] solveLinearEquation(S[][] lu, final int[] index, final S[] b) {
    final int columnSize = lu.length;
    final int rowSize = lu[0].length;

    final S[] x = lu[0][0].createArray(columnSize);

    for (int i = 0; i < columnSize; i++) {
      x[i] = b[i];
    }

    int ii = -1;
    for (int i = 0; i < rowSize; i++) {
      int ip = index[i];
      S sum = x[ip];
      x[ip] = x[i];

      if (ii >= 0) {
        for (int j = ii; j < i; j++) {
          sum = sum.subtract(lu[i][j].multiply(x[j]));
        }
      } else if (sum.abs().isZero() == false) {
        ii = i;
      }

      x[i] = sum;
    }

    for (int i = rowSize - 1; i >= 0; i--) {
      S sum = x[i];

      for (int j = i + 1; j < rowSize; j++) {
        sum = sum.subtract(lu[i][j].multiply(x[j]));
      }

      final S invRe = lu[i][i].inverse();
      x[i] = sum.multiply(invRe);
    }

    return x;
  }

  /**
   * 逆行列を返します。
   * 
   * @param a 対象となる行列
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return 逆行列
   */
  public S[][] inverse(final S[][] a, final S tolerance, final boolean stopIfSingular) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    if (rowSize != columnSize) {
      throw new IllegalArgumentException(Messages.getString("LuDecomposition.2")); //$NON-NLS-1$
    }

    final S[][] inversedMatrix = a[0][0].createArray(rowSize, columnSize);
    final LuIndexSign<S,M> luDecompostion = luDecompose(a, tolerance, stopIfSingular);
    final S[][] lu = luDecompostion.getLu();
    final int[] index = luDecompostion.getIndex();

    for (int j = 0; j < rowSize; j++) {
      final S[] b = a[0][0].createArray(rowSize);
      for (int i = 0; i < rowSize; i++) {
        if (i == j) {
          b[i] = a[0][0].createUnit();
        } else {
          b[i] = a[0][0].createZero();
        }
      }

      final S[] x = solveLinearEquation(lu, index, b);
      for (int i = 0; i < rowSize; i++) {
        inversedMatrix[i][j] = x[i];
      }
    }
    return inversedMatrix;
  }

  /**
   * 左行列で右行列を割った結果を返します。
   * 
   * @param a 左行列
   * @param b 右行列
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return 割り算の結果
   */
  public S[][] leftDivide(final S[][] a, final S[][] b, final S tolerance, final boolean stopIfSingular) {
    final int rowSize1 = a.length;
    final int columnSize1 = rowSize1 == 0 ? 0 : a[0].length;
    final int columnSize2 = b.length == 0 ? 0 : b[0].length;

    final LuIndexSign<S,M> luDecompostion = luDecompose(a, tolerance, stopIfSingular);
    final S[][] lu = luDecompostion.getLu();
    final int[] index = luDecompostion.getIndex();
    
    final S[][] ans = a[0][0].createArray(columnSize1, columnSize2);

    for (int j = 0; j < columnSize2; j++) {
      final S[] bb = a[0][0].createArray(rowSize1);
      for (int i = 0; i < rowSize1; i++) {
        bb[i] = b[i][j];
      }


      final S[] x = solveLinearEquation(lu, index, bb);

      for (int i = 0; i < rowSize1; i++) {
        ans[i][j] = x[i];
      }
    }
    return ans;
  }

  /**
   * 行列式を返します。
   * 
   * @param a 対象となる行列
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return 行列式
   */
  public S getDeterminant(final S[][] a, final S tolerance, final boolean stopIfSingular) {
    final LuIndexSign<S,M> luDecompostion = luDecompose(a, tolerance, stopIfSingular);
    final S[][] lu = luDecompostion.getLu();
    final int[] sign = luDecompostion.getSign();

    S determinant = a[0][0].createUnit();
    for (int i = 0; i < lu.length; i++) {
      determinant = determinant.multiply(lu[i][i]);
    }

    return determinant.multiply(sign[0]);
  }
  
  /**
   * @author koga
   * @version $Revision$, 2021/08/13
   * @param <SS> スカラーの型
   * @param <MM> 行列の型
   */
  class LuIndexSign<SS extends NumericalScalar<SS,MM>, MM extends NumericalMatrix<SS,MM>> {
    /** lu. */
    private SS[][] lu;
    /** index. */
    private int[] index;
    /** sign. */
    private int[] sign;
    
    /**
     * Creates LuIndexSign.
     * 
     * @param lu lu
     * @param index index
     * @param sign sign
     */
    LuIndexSign(SS[][] lu, int[] index, int[] sign) {
      this.lu = lu;
      this.index = index;
      this.sign = sign;
    }
    
    /**
     * Returns lu.
     * 
     * @return lu
     */
    SS[][] getLu() {
      return this.lu;
    }
    
    /**
     * Returns index.
     * 
     * @return index
     */
    int[] getIndex() {
      return this.index;
    }
    
    /**
     * Returns sign.
     * 
     * @return sign
     */
    int[] getSign() {
      return this.sign;
    }
  }
}