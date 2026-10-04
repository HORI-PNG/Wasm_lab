/*
 * $Id: DoubleRealSVD.java,v 1.4 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.svd;

import org.mklab.nfc.matrix.DoubleMatrixUtil;


/**
 * 倍精度(double)型の実行列の特異値分解を行うためのクラスです。
 * 
 * @author koga
 * @version $Revision: 1.4 $
 */
public final class DoubleRealSingularValueDecomposer {
  /** 最大反復数 */
  private int maxIteration = 30;
  
  
  /**
   * 実行列の特異値分解を返します。
   * 
   * <p>対象となる行列をA、特異値を対角成分とする対角行列を D、左特異ベクトルからなる直交行列をU、右特異ベクトルからなる直交行列をVとすると、
   * 
   * <blockquote> A = U * D * V <sup>T </sup> </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param a 対象となる行列
   * @return 実行列の特異値分解
   */
  public SingularValueDecompositionDoubleRealElements decompose(final double[][] a) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;
    final int size = Math.min(rowSize, columnSize);

    final double[] values = new double[size];
    final double[][] leftVector = new double[rowSize][rowSize];
    final double[][] rightVector = new double[columnSize][columnSize];

    decompose(a, values, leftVector, rightVector);

    final double[][] diagonalValues = DoubleMatrixUtil.vectorToDiagonal(values);
    final double[][] valueMatrix = new double[rowSize][columnSize];
    DoubleMatrixUtil.setSubMatrix(valueMatrix, 0, size - 1, 0, size - 1, diagonalValues);
    return new SingularValueDecompositionDoubleRealElements(leftVector, valueMatrix, rightVector);
  }

  /**
   * 実行列の特異値を返します。
   * 
   * @param a 対象となる行列
   * @return 特異値を成分とする配列
   */
  public double[] singularValue(final double[][] a) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final double[] values = new double[Math.min(rowSize, columnSize)];
    final double[][] leftVector = new double[rowSize][rowSize];
    final double[][] rightVector = new double[columnSize][columnSize];

    decompose(a, values, leftVector, rightVector);

    return values;
  }

  /**
   * 実行列の最大特異値を返します。
   * 
   * @param a 対象となる行列
   * @return 最大特異値
   */
  public double maximumSingularValue(final double[][] a) {
    return singularValue(a)[0];
  }

  /**
   * 実行列の最小特異値を返します。
   * 
   * @param a 対象となる行列
   * @return 最小特異値
   */
  public double minimumSingularValue(final double[][] a) {
    double[] singularValues = singularValue(a);
    return singularValues[singularValues.length - 1];
  }

  /**
   * 実行列が非正則であるか判定します。
   * 
   * @param a 非正則性を調べる行列
   * @param tolerance 許容誤差
   * @return 非正則ならばtrue、そうでなければfalse
   */
  public boolean isSingular(final double[][] a, final double tolerance) {
    final double[] singularValues = singularValue(a);
    return singularValues[singularValues.length - 1] < tolerance;
  }

  /**
   * 実行列のランク(階数)を返します。
   * 
   * @param a 対象となる行列
   * @param tolerance 許容誤差
   * @return ランク
   */
  public int rank(final double[][] a, final double tolerance) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final double[][] a2;
    if (rowSize > columnSize) {
      a2 = DoubleMatrixUtil.transpose(a);
    } else {
      a2 = a;
    }

    final double[] singularValues = singularValue(a2);

    int rank = 0;
    for (int i = 0; i < singularValues.length; i++) {
      if (singularValues[i] > tolerance) {
        rank++;
      }
    }

    return rank;
  }

  /**
   * 実行列がフルランクであるか判定します。
   * 
   * @param a 対象となる行列
   * @param tolerance 許容誤差
   * @return フルランクならばtrue、そうでなければfalse
   */
  public boolean isFullRank(final double[][] a, final double tolerance) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final int rank = rank(a, tolerance);

    if (rank == rowSize || rank == columnSize) {
      return true;
    }
    return false;
  }

  /**
   * 実行列の擬似逆行列を返します。
   * 
   * @param a 対象となる行列
   * @param tolerance 許容誤差
   * @return 擬似逆行列
   */
  public double[][] pseudoInverse(final double[][] a, final double tolerance) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    // A = u diag(val) v'
    SingularValueDecompositionDoubleRealElements udv = decompose(a);
    final double[][] u = udv.getU();
    final double[][] d = udv.getD();
    final double[][] v = udv.getV();

    int rank = 0;
    for (int i = 0; i < Math.min(rowSize, columnSize); i++) {
      if (d[i][i] > tolerance) {
        rank++;
      }
    }

    // pseudoInverse(A) := v diag(val)~ u'
    final double[][] dInv = new double[columnSize][rowSize];

    for (int i = 1; i <= rank; i++) {
      dInv[i - 1][i - 1] = 1.0 / d[i - 1][i - 1];
    }

    return DoubleMatrixUtil.multiply(v, DoubleMatrixUtil.multiply(dInv, DoubleMatrixUtil.transpose(u)));
  }

  /**
   * 線形方程式の最小二乗解を返します。
   * 
   * <p>aで表される複素行列をA, bで表される複素行列をBとすとき、
   * 
   * <blockquote> A * X = B </blockquote>
   * 
   * の解即ち
   * 
   * <blockquote> X = A<sup>-1</sup> * B </blockquote>
   * 
   * を返します。
   * 
   * @param a 行列
   * @param b 行列
   * @param tolerance 許容誤差
   * @return 線形方程式の解
   */
  public double[][] leastSquare(final double[][] a, final double[][] b, final double tolerance) {
    return DoubleMatrixUtil.multiply(pseudoInverse(a, tolerance), b);
  }

  /**
   * 複素行列のカーネル(零空間)を張るベクトルからなる行列を返します。<br>
   * 
   * 許容誤差より小さい特異値をゼロと見なします。
   * 
   * @param a 行列
   * @param tolerance 許容誤差
   * @return カーネル
   */
  public double[][] kernel(final double[][] a, final double tolerance) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final double[][] a2;
    if (rowSize < columnSize) {
      a2 = DoubleMatrixUtil.appendDown(a, new double[columnSize - rowSize][columnSize]);
    } else {
      a2 = a;
    }

    SingularValueDecompositionDoubleRealElements udv = decompose(a2);
    final double[][] d = udv.getD();
    final double[][] v = udv.getV();

    int rank = 0;
    for (int i = 0; i < Math.min(rowSize, columnSize); i++) {
      if (d[i][i] > tolerance) {
        rank++;
      }
    }

    if (rank == Math.max(rowSize, columnSize)) {
      throw new IllegalArgumentException(Messages.getString("DoubleRealSVD.0")); //$NON-NLS-1$
    }

    return DoubleMatrixUtil.getSubMatrix(v, 0, columnSize - 1, rank, columnSize - 1);
  }

  /**
   * 実行列の最大特異値(2-ノルム)を返します。
   * 
   * @param a 対象となる行列
   * @return 最大特異値(2-ノルム)
   */
  public double norm(final double[][] a) {
    return maximumSingularValue(a);
  }

  /**
   * 実行列の条件数を返します。
   * 
   * @param a 対象となる行列
   * @return 条件数
   */
  public double conditionNumber(final double[][] a) {
    if (a.length == 0 || a[0].length == 0) {
      return Double.NaN;
    }

    final double[] singularValues = singularValue(a);

    if (DoubleMatrixUtil.anyZero(singularValues)) {
      return Double.POSITIVE_INFINITY;
    }

    return DoubleMatrixUtil.max(singularValues) / DoubleMatrixUtil.min(singularValues);
  }

  /*
   * A = U * D * V'
   * 
   * U := leftVector
   * D := diag2vec(values)
   * V := rightvector
   * 
   */
  /**
   * 特異値分解を返します。
   * 
   * @param a 対象となる行列
   * @param values 特異値
   * @param leftVector 左特異値行列
   * @param rightVector 右特異値行列
   */
  private void decompose(final double[][] a, final double[] values, final double[][] leftVector, final double[][] rightVector) {
    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;

    final int size = Math.max(rowSize, columnSize);

    final double[][] a2;
    if (rowSize > columnSize) {
      a2 = DoubleMatrixUtil.transpose(a);
    } else {
      a2 = a;
    }

    final double[][] lvec = new double[size][size];
    final double[][] rvec = new double[size][size];
    final double[] val = new double[size];

    final int errorCode = svd(a2, true, lvec, true, rvec, val);

    if (0 < errorCode) {
      throw new RuntimeException(Messages.getString("DoubleRealSVD.2")); //$NON-NLS-1$
    }

    // Sorting with restpect to the singular valute
    for (int i = 1; i < size; i++) {
      for (int j = 1; j < size; j++) {
        if (val[j - 1] < val[j]) {
          final double dd = val[j - 1];
          val[j - 1] = val[j];
          val[j] = dd;
          DoubleMatrixUtil.exchangeColumn(lvec, j - 1, j);
          DoubleMatrixUtil.exchangeColumn(rvec, j - 1, j);
        }
      }
    }

    if (rowSize > columnSize) {
      DoubleMatrixUtil.setSubMatrix(leftVector, 0, 0, rvec, 0, leftVector.length - 1, 0, leftVector[0].length - 1);
      DoubleMatrixUtil.setSubMatrix(rightVector, 0, 0, lvec, 0, rightVector.length - 1, 0, rightVector[0].length - 1);
      DoubleMatrixUtil.setSubVector(values, 0, val, 0, values.length - 1);
    } else {
      DoubleMatrixUtil.setSubMatrix(leftVector, 0, 0, lvec, 0, leftVector.length - 1, 0, leftVector[0].length - 1);
      DoubleMatrixUtil.setSubMatrix(rightVector, 0, 0, rvec, 0, rightVector.length - 1, 0, rightVector[0].length - 1);
      DoubleMatrixUtil.setSubVector(values, 0, val, 0, values.length - 1);
    }
  }

  /**
   * @param a 対象となる行列
   * @param uDesired 左特異ベクトルが必要ならばtrue
   * @param u 左特異ベクトル
   * @param vDesired 右特異ベクトルが必要ならばtrue
   * @param v 右特異ベクトル
   * @param w 特異値
   * @return 特異値分解の結果
   */
  private int svd(final double[][] a, final boolean uDesired, final double[][] u, final boolean vDesired, final double[][] v, final double[] w) {
    int l = 0;
    int l1 = 0;
    int ierr = 0;
    double f, h, s, z;

    final int rowSize = a.length;
    final int columnSize = rowSize == 0 ? 0 : a[0].length;
    
    final double[] rv1 = new double[columnSize];

    for (int i = 1; i <= rowSize; i++) {
      for (int j = 1; j <= columnSize; j++) {
        u[i - 1][j - 1] = a[i - 1][j - 1];
      }
    }

    // Householder reduction to bidiagonal form
    double g = 0.0;
    double scale = 0.0;
    double x = 0.0;

    for (int i = 1; i <= columnSize; i++) {
      l = i + 1;
      rv1[i - 1] = scale * g;
      g = 0.0;
      s = 0.0;
      scale = 0.0;

      if (i <= rowSize) {
        for (int k = i; k <= rowSize; k++) {
          scale += Math.abs(u[k - 1][i - 1]);
        }

        if (scale != 0.0) {
          for (int k = i; k <= rowSize; k++) {
            u[k - 1][i - 1] /= scale;
            s += u[k - 1][i - 1] * u[k - 1][i - 1];
          }

          f = u[i - 1][i - 1];
          g = -fsign(Math.sqrt(s), f);
          h = f * g - s;
          u[i - 1][i - 1] = f - g;

          if (i != columnSize) {
            for (int j = l; j <= columnSize; j++) {
              s = 0.0;

              for (int k = i; k <= rowSize; k++) {
                s += u[k - 1][i - 1] * u[k - 1][j - 1];
              }

              f = s / h;

              for (int k = i; k <= rowSize; k++) {
                u[k - 1][j - 1] += f * u[k - 1][i - 1];
              }
            }
          }

          for (int k = i; k <= rowSize; k++) {
            u[k - 1][i - 1] *= scale;
          }
        }
      }

      w[i - 1] = scale * g;
      g = 0.0;
      s = 0.0;
      scale = 0.0;

      if (i > rowSize || i == columnSize) {
        x = Math.max(x, Math.abs(w[i - 1]) + Math.abs(rv1[i - 1]));
        continue;
      }

      for (int k = l; k <= columnSize; k++) {
        scale += Math.abs(u[i - 1][k - 1]);
      }

      if (scale == 0.0) {
        x = Math.max(x, Math.abs(w[i - 1]) + Math.abs(rv1[i - 1]));
        continue;
      }

      for (int k = l; k <= columnSize; k++) {
        u[i - 1][k - 1] /= scale;
        s += u[i - 1][k - 1] * u[i - 1][k - 1];
      }

      f = u[i - 1][l - 1];
      g = -fsign(Math.sqrt(s), f);
      h = f * g - s;
      u[i - 1][l - 1] = f - g;

      for (int k = l; k <= columnSize; k++) {
        rv1[k - 1] = u[i - 1][k - 1] / h;
      }

      if (i != rowSize) {
        for (int j = l; j <= rowSize; j++) {
          s = 0.0;

          for (int k = l; k <= columnSize; k++) {
            s += u[j - 1][k - 1] * u[i - 1][k - 1];
          }

          for (int k = l; k <= columnSize; k++) {
            u[j - 1][k - 1] += s * rv1[k - 1];
          }
        }
      }

      for (int k = l; k <= columnSize; k++) {
        u[i - 1][k - 1] *= scale;
      }

      x = Math.max(x, Math.abs(w[i - 1]) + Math.abs(rv1[i - 1]));
    }

    // Accumulation of right-hand transformations.
    if (vDesired) {
      for (int ii = 1; ii <= columnSize; ii++) {
        int i = columnSize + 1 - ii;

        if (i != columnSize) {
          if (g != 0.0) {
            // Double division avoids possible underflow
            for (int j = l; j <= columnSize; j++) {
              v[j - 1][i - 1] = (u[i - 1][j - 1] / u[i - 1][l - 1]) / g;
            }

            for (int j = l; j <= columnSize; j++) {
              s = 0.0;

              for (int k = l; k <= columnSize; k++) {
                s += u[i - 1][k - 1] * v[k - 1][j - 1];
              }

              for (int k = l; k <= columnSize; k++) {
                v[k - 1][j - 1] += s * v[k - 1][i - 1];
              }
            }
          }

          for (int j = l; j <= columnSize; j++) {
            v[i - 1][j - 1] = 0.0;
            v[j - 1][i - 1] = 0.0;
          }
        }

        v[i - 1][i - 1] = 1.0;
        g = rv1[i - 1];
        l = i;
      }
    }

    // Accumulation of left-hand transformations
    if (uDesired) {
      int mn = columnSize;
      if (rowSize < columnSize) {
        mn = rowSize;
      }

      for (int ii = 1; ii <= mn; ii++) {
        int i = mn + 1 - ii;
        l = i + 1;
        g = w[i - 1];

        if (i != columnSize) {
          for (int j = l; j <= columnSize; j++) {
            u[i - 1][j - 1] = 0.0;
          }
        }

        if (g == 0.0) {
          for (int j = i; j <= rowSize; j++) {
            u[j - 1][i - 1] = 0.0;
          }
        } else {
          if (i != mn) {
            for (int j = l; j <= columnSize; j++) {
              s = 0.0;

              for (int k = l; k <= rowSize; k++) {
                s += u[k - 1][i - 1] * u[k - 1][j - 1];
              }

              // Double division avoids possible underflow
              f = (s / u[i - 1][i - 1]) / g;

              for (int k = i; k <= rowSize; k++) {
                u[k - 1][j - 1] += f * u[k - 1][i - 1];
              }
            }
          }

          for (int j = i; j <= rowSize; j++) {
            u[j - 1][i - 1] /= g;
          }
        }

        u[i - 1][i - 1] += 1.0;
      }
    }

    // Diagonaliztion of the bidagonal form.
    // eps = meps * x;
    double tst1 = x;

    for (int kk = 1; kk <= columnSize; kk++) {
      int k1 = columnSize - kk;
      int k = k1 + 1;
      int its = 0;

      // L520:
      do {
        // Test for splitting.
        double tst2;
        boolean goto565 = false;

        for (int ll = 1; ll <= k; ll++) {
          l1 = k - ll;
          l = l1 + 1;

          tst2 = tst1 + Math.abs(rv1[l - 1]);

          if (Double.doubleToLongBits(tst2) == Double.doubleToLongBits(tst1)) {
            goto565 = true; // goto L565;
            break;
          }
          // RV1(1) is always zero, so there is no exit through the bottom of the loop

          tst2 = tst1 + Math.abs(w[l1 - 1]);
          if (Double.doubleToLongBits(tst2) == Double.doubleToLongBits(tst1)) {
            break;
          }
        }

        if (!goto565) {

          // Cancellation of RV1(l) if l greater than 1
          double c = 0.0;
          s = 1.0;

          for (int i = l; i <= k; i++) {
            f = s * rv1[i - 1];
            rv1[i - 1] *= c;

            tst2 = tst1 + Math.abs(f);
            if (Double.doubleToLongBits(tst2) == Double.doubleToLongBits(tst1)) {
              break;
            }

            g = w[i - 1];
            h = Math.sqrt(f * f + g * g);
            w[i - 1] = h;
            c = g / h;
            s = -f / h;

            if (uDesired) {
              for (int j = 1; j <= rowSize; j++) {
                double y = u[j - 1][l1 - 1];
                z = u[j - 1][i - 1];
                u[j - 1][l1 - 1] = y * c + z * s;
                u[j - 1][i - 1] = -y * s + z * c;
              }
            }
          }
        }

        // Test for convergence
        // L565:
        z = w[k - 1];
        if (l == k) {
          break; // goto L650;
        }

        // Shift from botom 2 by 2 minor
        if (its == this.maxIteration) {
          // Set error -- no convergence to a singular value after max iterations
          ierr = k;
          break; // goto L650;
        }

        its++;
        x = w[l - 1];
        double y = w[k1 - 1];
        g = rv1[k1 - 1];
        h = rv1[k - 1];
        f = ((y - z) * (y + z) + (g - h) * (g + h)) / (2.0 * h * y);
        g = Math.sqrt(f * f + 1.0);
        f = ((x - z) * (x + z) + h * (y / (f + fsign(g, f)) - h)) / x;

        // Next QR transformation
        double c = 1.0;
        s = 1.0;

        for (int i1 = l; i1 <= k1; i1++) {
          int i = i1 + 1;
          g = rv1[i - 1];
          y = w[i - 1];
          h = s * g;
          g = c * g;
          z = Math.sqrt(f * f + h * h);
          rv1[i1 - 1] = z;
          c = f / z;
          s = h / z;
          f = x * c + g * s;
          g = -x * s + g * c;
          h = y * s;
          y = y * c;

          // Update of matrix V
          if (vDesired) {
            for (int j = 1; j <= columnSize; j++) {
              x = v[j - 1][i1 - 1];
              z = v[j - 1][i - 1];
              v[j - 1][i1 - 1] = x * c + z * s;
              v[j - 1][i - 1] = -x * s + z * c;
            }
          }

          z = Math.sqrt(f * f + h * h);
          w[i1 - 1] = z;

          // Rotation can be arbitrary if z is zero
          if (z != 0.0) {
            c = f / z;
            s = h / z;
          }

          f = c * g + s * y;
          x = -s * g + c * y;

          // Update of matrix U
          if (uDesired) {
            for (int j = 1; j <= rowSize; j++) {
              y = u[j - 1][i1 - 1];
              z = u[j - 1][i - 1];
              u[j - 1][i1 - 1] = y * c + z * s;
              u[j - 1][i - 1] = -y * s + z * c;
            }
          }
        }

        rv1[l - 1] = 0.0;
        rv1[k - 1] = f;
        w[k - 1] = x;
      } while (true);

      // goto L520;

      // L650:
      // Convergence
      if (z >= 0.0) {
        continue;
      }

      // w(k) is made non-negative
      w[k - 1] = -z;

      if (vDesired) {
        for (int j = 1; j <= columnSize; j++) {
          v[j - 1][k - 1] = -v[j - 1][k - 1];
        }
      }
    }

    return ierr;
  }

  /**
   * 絶対値と符号を2個の実数で決めます。
   * 
   * @param a 絶対値を決める実数
   * @param b 符号を決める実数
   * @return 2個の実数から作られた実数
   */
  private double fsign(final double a, final double b) {
    return (((b) >= 0.0) ? (Math.abs(a)) : (-Math.abs(a)));
  }
  
  /**
   * 最大反復数を設定します。
   * @param maxIteration 最大反復数
   */
  public void setMaxIteration(final int maxIteration) {
    this.maxIteration = maxIteration;
  }

}