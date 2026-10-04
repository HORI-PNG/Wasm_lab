/*
 * $Id: DoubleComplexMatrixUtil.java,v 1.6 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matrix;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.io.Writer;
import java.util.Random;

import org.mklab.nfc.leq.DoubleComplexLUDecomposer;
import org.mklab.nfc.matx.MxDataHead;
import org.mklab.nfc.random.DoubleComplexNormalRandom;
import org.mklab.nfc.random.DoubleComplexUniformRandom;
import org.mklab.nfc.scalar.DoubleComplexNumber;
import org.mklab.nfc.scalar.DoubleComplexNumberUtil;
import org.mklab.nfc.scalar.DoubleNumber;
import org.mklab.nfc.svd.DoubleComplexSingularValueDecomposer;
import org.mklab.nfc.util.EndianTransformer;


/**
 * 倍精度(double)型の複素行列{@link DoubleComplexMatrix}のユーティリティクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.6 $, 2004/06/23
 */
public final class DoubleComplexMatrixUtil {
  /**
   * 新しく生成された<code>DoubleComplexMatrixUtil</code>オブジェクトを初期化します。
   */
  private DoubleComplexMatrixUtil() {
    // nothing to do
  }

  /**
   * 実部配列と虚部配列から複素行列を生成します。
   * 
   * @param realPart 実部配列
   * @param imagPart 複素配列
   * @return 複素配列
   */
  public static DoubleComplexNumber[] createArray(final double[] realPart, final double[] imagPart) {
    int realRowSize = realPart.length;
    int imagRowSize = imagPart.length;

    if (realRowSize != imagRowSize) {
      throw new MatrixSizeException(MatrixSizeException.NOT_SAME_SIZE);
    }

    DoubleComplexNumber[] ans = new DoubleComplexNumber[realRowSize];
    for (int i = 0; i < realRowSize; i++) {
      ans[i] = new DoubleComplexNumber(realPart[i], imagPart[i]);
    }

    return ans;
  }
  
  /**
   * 実部配列と虚部配列から複素行列を生成します。
   * 
   * @param realPart 実部配列
   * @param imagPart 複素配列
   * @return 複素配列
   */
  public static DoubleComplexNumber[][] createArray(final double[][] realPart, final double[][] imagPart) {
    int realRowSize = realPart.length;
    int realColumnSize = realRowSize == 0 ? 0 : realPart[0].length;
    int imagRowSize = imagPart.length;
    int imagColumnSize = imagRowSize == 0 ? 0 : imagPart[0].length;

    if (realRowSize != imagRowSize || realColumnSize != imagColumnSize) {
      throw new MatrixSizeException(MatrixSizeException.NOT_SAME_SIZE);
    }

    DoubleComplexNumber[][] ans = new DoubleComplexNumber[realRowSize][imagColumnSize];
    for (int i = 0; i < realRowSize; i++) {
      for (int j = 0; j < realColumnSize; j++) {
        ans[i][j] = new DoubleComplexNumber(realPart[i][j], imagPart[i][j]);
      }
    }

    return ans;
  }

  /**
   * 整数行列を複素行列(元の行列を実部とする)に変換します。
   * 
   * @param matrix 整数行列
   * @return 複素行列
   */
  public static DoubleComplexNumber[][] createArray(final int[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    DoubleComplexNumber[][] ans = new DoubleComplexNumber[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      int[] matrixi = matrix[i];
      DoubleComplexNumber[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = new DoubleComplexNumber(matrixi[j], 0);
      }
    }
    return ans;
  }

  /**
   * 実行列を複素行列(元の行列を実部とする)に変換します。
   * 
   * @param matrix 実行列
   * @return 複素行列
   */
  public static DoubleComplexNumber[][] createArray(final double[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = (rowSize == 0 || matrix[0] == null) ? 0 : matrix[0].length;
    DoubleComplexNumber[][] ans = new DoubleComplexNumber[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      DoubleComplexNumber[] ansi = ans[i];
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = new DoubleComplexNumber(matrixi[j], 0);
      }
    }

    return ans;
  }

  /**
   * 実行列を複素行列(元の行列を実部とする)に変換します。
   * 
   * @param matrix 実行列
   * @return 複素行列
   */
  public static DoubleComplexNumber[][] createArray(final DoubleNumber[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = (rowSize == 0 || matrix[0] == null) ? 0 : matrix[0].length;
    DoubleComplexNumber[][] ans = new DoubleComplexNumber[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      DoubleComplexNumber[] ansi = ans[i];
      DoubleNumber[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = new DoubleComplexNumber(matrixi[j], 0);
      }
    }

    return ans;
  }

  /**
   * 第一行列に第二行列を加えます。
   * 
   * @param a1Re 第一行列の実部
   * @param a1Im 第一行列の虚部
   * @param a2Re 第二行列の実部
   * @param a2Im 第二行列の虚部
   */
  public static void addSelf(final double[][] a1Re, final double[][] a1Im, final double[][] a2Re, final double[][] a2Im) {
    int rowSize = a1Re.length;
    int columnSize = rowSize == 0 ? 0 : a1Re[0].length;

    for (int i = 0; i < rowSize; i++) {
      double[] a1ri = a1Re[i];
      double[] a2ri = a2Re[i];
      double[] a1ii = a1Im[i];
      double[] a2ii = a2Im[i];
      for (int j = 0; j < columnSize; j++) {
        a1ri[j] += a2ri[j];
        a1ii[j] += a2ii[j];
      }
    }
  }

  /**
   * 行列に複素数を乗じます。
   * 
   * @param aRe 対象となる行列の実部
   * @param aIm 対象となる行列の虚部
   * @param dRe 複素数の実部
   * @param dIm 複素数の虚部
   */
  public static void multiplySelf(final double[][] aRe, final double[][] aIm, final double dRe, final double dIm) {
    int rowSize = aRe.length;
    int columnSize = rowSize == 0 ? 0 : aRe[0].length;

    for (int i = 0; i < rowSize; i++) {
      double[] ari = aRe[i];
      double[] aii = aIm[i];
      for (int j = 0; j < columnSize; j++) {
        double tmp = ari[j] * dRe - aii[j] * dIm;
        aii[j] = ari[j] * dIm + aii[j] * dRe;
        ari[j] = tmp;
      }
    }
  }

  /**
   * 行列に複素数を掛けた行列を生成します。
   * 
   * @param aRe 対象となる行列の実部
   * @param aIm 対象となる行列の虚部
   * @param dRe 複素数の虚部
   * @param dIm 複素数の実部
   * @return 生成された行列
   */
  public static double[][][] multiply(final double[][] aRe, final double[][] aIm, final double dRe, final double dIm) {
    int rowSize = aRe.length;
    int columnSize = rowSize == 0 ? 0 : aRe[0].length;

    double[][] cRe = new double[rowSize][columnSize];
    double[][] cIm = new double[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      double[] ari = aRe[i];
      double[] aii = aIm[i];
      double[] cri = cRe[i];
      double[] cii = cIm[i];
      for (int j = 0; j < columnSize; j++) {
        cri[j] = ari[j] * dRe - aii[j] * dIm;
        cii[j] = ari[j] * dIm + aii[j] * dRe;
      }
    }
    return new double[][][] {cRe, cIm};
  }

  /**
   * 行列に複素数を掛けた結果を引数として与えられた行列に代入します。
   * 
   * @param ansRe 結果を代入する行列の実部
   * @param ansIm 結果を代入する行列の虚部
   * @param aRe 対象となる行列の実部
   * @param aIm 対象となる行列の虚部
   * @param dRe 複素数の実部
   * @param dIm 複素数の虚部
   */
  public static void multiply(final double[][] ansRe, final double[][] ansIm, final double[][] aRe, final double[][] aIm, final double dRe, final double dIm) {
    int rowSize = aRe.length;
    int columnSize = rowSize == 0 ? 0 : aRe[0].length;

    for (int i = 0; i < rowSize; i++) {
      double[] ari = aRe[i];
      double[] aii = aIm[i];
      double[] cri = ansRe[i];
      double[] cii = ansIm[i];
      for (int j = 0; j < columnSize; j++) {
        cri[j] = ari[j] * dRe - aii[j] * dIm;
        cii[j] = ari[j] * dIm + aii[j] * dRe;
      }
    }
  }

  /**
   * 複素ベクトルのスカラー積(内積)を返します。
   * 
   * @param aRe 第一ベクトルの実部
   * @param aIm 第一ベクトルの虚部
   * @param bRe 第二ベクトルの実部
   * @param bIm 第二ベクトルの虚部
   * @return スカラー積(内積)
   */
  public static double[] scalarProduct(final double[][] aRe, final double[][] aIm, final double[][] bRe, final double[][] bIm) {
    final int rowSize1 = aRe.length;
    final int columnSize1 = rowSize1 == 0 ? 0 : aRe[0].length;

    final int rowSize2 = bRe.length;
    final int columnSize2 = rowSize2 == 0 ? 0 : bRe[0].length;

    final int number1 = rowSize1 * columnSize1;
    final int number2 = rowSize2 * columnSize2;
    
    if (number1 != number2) {
      throw new MatrixSizeException(Messages.getString("DoubleComplexMatrixUtil.0")); //$NON-NLS-1$
    }

    final double[] ans = new double[2];

    int count = number1;
    while (count-- != 0) {
      final int aRow = count / columnSize1;
      final int aCol = count % columnSize1;
      final int bRow = count / columnSize2;
      final int bCol = count % columnSize2;
      ans[0] += aRe[aRow][aCol] * bRe[bRow][bCol] - aIm[aRow][aCol] * bIm[bRow][bCol];
      ans[1] += aRe[aRow][aCol] * bIm[bRow][bCol] + aIm[aRow][aCol] * bRe[bRow][bCol];
    }
    return ans;
  }

  /**
   * 複素行列のフロベニウスノルムを返します。
   * 
   * @param aRe 実部行列
   * @param aIm 虚部行列
   * @return フロベニウスノルム
   */
  public static double frobNorm(final double[][] aRe, final double[][] aIm) {
    int rowSize = aRe.length;
    int columnSize = rowSize == 0 ? 0 : aRe[0].length;
    double sum = 0;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        sum += aRe[i][j] * aRe[i][j] + aIm[i][j] * aIm[i][j];
      }
    }
    return Math.sqrt(sum);
  }

//  /**
//   * 複素行列の逆行列を返します。
//   * 
//   * @param aRe 実部行列
//   * @param aIm 虚部行列
//   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
//   * @return 逆行列の実部行列と虚部行列の配列
//   */
//  public static double[][][] inverse(final double[][] aRe, final double[][] aIm, final boolean stopIfSingular) {
//    return new DoubleComplexLUDecomposer().inverse(aRe, aIm, DoubleComplexMatrixUtil.frobNorm(aRe, aIm) * DoubleNumberUtil.EPS, stopIfSingular);
//  }
  
  /**
   * 複素行列の逆行列を返します。
   * 
   * @param aRe 実部行列
   * @param aIm 虚部行列
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return 逆行列の実部行列と虚部行列の配列
   */
  public static double[][][] inverse(final double[][] aRe, final double[][] aIm, final double tolerance, final boolean stopIfSingular) {
    return new DoubleComplexLUDecomposer().inverse(aRe, aIm, tolerance, stopIfSingular);
  }

  /**
   * 複素行列と複素行列の積を返します。
   * 
   * @param aRe 第一行列の実部
   * @param aIm 第一行列の虚部
   * @param bRe 第二行列の実部
   * @param bIm 第二行列の虚部
   * @return 複素行列と複素行列の積
   */
  public static double[][][] multiply(final double[][] aRe, final double[][] aIm, final double[][] bRe, final double[][] bIm) {
    int rowSize1 = aRe.length;
    int columnlSize1 = rowSize1 == 0 ? 0 : aRe[0].length;
    int rowSize2 = bRe.length;
    int columnSize2 = rowSize2 == 0 ? 0 : bRe[0].length;
    double[][] cRe = new double[rowSize1][columnSize2];
    double[][] cIm = new double[rowSize1][columnSize2];
    for (int i = 0; i < rowSize1; i++) {
      for (int j = 0; j < columnSize2; j++) {
        double dr = 0;
        double di = 0;
        for (int k = 0; k < columnlSize1; k++) {
          dr += aRe[i][k] * bRe[k][j] - aIm[i][k] * bIm[k][j];
          di += aRe[i][k] * bIm[k][j] + aIm[i][k] * bRe[k][j];
        }
        cRe[i][j] = dr;
        cIm[i][j] = di;
      }
    }
    return new double[][][] {cRe, cIm};
  }

  /**
   * 複素行列と複素行列の積を返します。
   * 
   * @param ansRe 計算結果の実部
   * @param ansIm 計算結果の虚部
   * @param aRe 第一行列の実部
   * @param aIm 第一行列の虚部
   * @param bRe 第二行列の実部
   * @param bIm 第二行列の虚部
   */
  public static void multiply(final double[][] ansRe, final double[][] ansIm, final double[][] aRe, final double[][] aIm, final double[][] bRe, final double[][] bIm) {
    final int rowSize1 = aRe.length;
    final int columnSize1 = rowSize1 == 0 ? 0 : aRe[0].length;
    final int rowSize2 = bRe.length;
    final int columnSize2 = rowSize2 == 0 ? 0 : bRe[0].length;

    for (int i = 0; i < rowSize1; i++) {
      double[] aRei = aRe[i];
      double[] aImi = aIm[i];
      double[] ansRei = ansRe[i];
      double[] ansImi = ansIm[i];
      for (int j = 0; j < columnSize2; j++) {
        double dRe = 0;
        double dIm = 0;
        for (int k = 0; k < columnSize1; k++) {
          final double aReik = aRei[k];
          final double aImik = aImi[k];
          final double[] bRek = bRe[k];
          final double[] bImk = bIm[k];
          dRe += aReik * bRek[j] - aImik * bImk[j];
          dIm += aReik * bImk[j] + aImik * bRek[j];
        }
        ansRei[j] = dRe;
        ansImi[j] = dIm;
      }
    }
  }

  /**
   * 複素行列の差を返します。
   * 
   * @param ansRe 複素行列の差の実部
   * @param ansIm 複素行列の差の虚部
   * @param aRe 引かれる行列の実部
   * @param aIm 引かれる行列の虚部
   * @param bRe 引く行列の実部
   * @param bIm 引く行列の虚部
   */
  public static void subtract(final double[][] ansRe, final double[][] ansIm, final double[][] aRe, final double[][] aIm, final double[][] bRe, final double[][] bIm) {
    int rowSize = aRe.length;
    int columnSize = rowSize == 0 ? 0 : aRe[0].length;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ansRe[i][j] = aRe[i][j] - bRe[i][j];
        ansIm[i][j] = aIm[i][j] - bIm[i][j];
      }
    }
  }
  
  /**
   * 複素行列の差を返します。
   * 
   * @param aRe 引かれる行列の実部
   * @param aIm 引かれる行列の虚部
   * @param bRe 引く行列の実部
   * @param bIm 引く行列の虚部
   * @return 生成された行列
   */
  public static double[][][] subtract(final double[][] aRe, final double[][] aIm, final double[][] bRe, final double[][] bIm) {
    int rowSize = aRe.length;
    int columnSize = rowSize == 0 ? 0 : aRe[0].length;
    
    final double[][] ansRe = new double[rowSize][columnSize];
    final double[][] ansIm = new double[rowSize][columnSize];
    
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ansRe[i][j] = aRe[i][j] - bRe[i][j];
        ansIm[i][j] = aIm[i][j] - bIm[i][j];
      }
    }
     
    return new double[][][]{ansRe,ansIm};
  }

  /**
   * 複素行列のデータを(MATフォーマット)で出力ストリームに出力します。
   * 
   * @param matrix 複素行列
   * @param output 出力ストリーム
   * @exception IOException 出力エラーが発生した場合
   */
  public static void writeMatFormat(final DoubleComplexNumber[][] matrix, final Writer output) throws IOException {
    BufferedWriter bw = new BufferedWriter(output);

    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    bw.write("# " + rowSize + " " + columnSize + " C"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    bw.newLine();

    for (int j = 0; j < columnSize; j++) {
      for (int i = 0; i < rowSize; i++) {
        double d1 = matrix[i][j].getRealPart().doubleValue();
        double d2 = matrix[i][j].getImaginaryPart().doubleValue();

        if (Double.isInfinite(d1)) {
          bw.write(" " + Double.toString(d1)); //$NON-NLS-1$
        } else if (Double.isNaN(d1)) {
          bw.write(" " + Double.toString(d1)); //$NON-NLS-1$
        } else {
          bw.write(DoubleNumber.toString(d1, "%16.8E")); //$NON-NLS-1$
        }

        if (Double.isInfinite(d2)) {
          bw.write(" " + Double.toString(d2)); //$NON-NLS-1$
        } else if (Double.isNaN(d2)) {
          bw.write(" " + Double.toString(d2)); //$NON-NLS-1$
        } else {
          bw.write(DoubleNumber.toString(d2, "%16.8E")); //$NON-NLS-1$
        }

        if (i != rowSize - 1) {
          bw.write(" "); //$NON-NLS-1$
        }
      }
      bw.newLine();
    }
    bw.flush();
  }

  /**
   * 実行列の成分毎の累乗(指数は複素数)を返します。
   * 
   * @param matrix 実行列
   * @param scalar 複素数(指数)
   * @return 実行列の成分毎の累乗(指数は複素数)を成分とする行列
   */
  public static DoubleComplexNumber[][] powerElementWise(final double[][] matrix, final DoubleComplexNumber scalar) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    DoubleComplexNumber[][] ans = new DoubleComplexNumber[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      DoubleComplexNumber[] ansi = ans[i];
      double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = DoubleComplexNumberUtil.power(matrixi[j], scalar);
      }
    }
    return ans;
  }

  /**
   * ストリームトークンナイザから行列(MATフォーマット)を読み込みます。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param st データを読み込むストリームトークンナイザ
   * @return 読み込んだ行列
   * @throws IOException ストリームトークンナイザから読み込めない場合
   */
  public static DoubleComplexNumber[][] readMatFormat(final int rowSize, final int columnSize, final StreamTokenizer st) throws IOException {
    DoubleComplexNumber[][] ans = new DoubleComplexNumber[rowSize][columnSize];

    for (int j = 0; j < columnSize; j++) {
      for (int i = 0; i < rowSize; i++) {
        if (st.nextToken() == StreamTokenizer.TT_EOF) {
          throw new IOException(Messages.getString("DoubleComplexMatrixUtil.7")); //$NON-NLS-1$
        }

        if (st.ttype == StreamTokenizer.TT_WORD) {
          double rPart = Double.parseDouble(st.sval);

          if (st.nextToken() == StreamTokenizer.TT_EOF) {
            throw new IOException(Messages.getString("DoubleComplexMatrixUtil.8")); //$NON-NLS-1$
          } else if (st.ttype == StreamTokenizer.TT_WORD) {
            double iPart = Double.parseDouble(st.sval);
            ans[i][j] = new DoubleComplexNumber(rPart, iPart);
          }
        } else {
          throw new IOException(Messages.getString("DoubleComplexMatrixUtil.9")); //$NON-NLS-1$
        }
      }
    }
    return ans;
  }

  /**
   * 行列を出力ストリームに(MXフォーマット)で出力します。
   * 
   * @param matrix 対象となる行列
   * @param output 出力ストリーム
   * @param name 行列の名前
   * @throws IOException ストリームに出力できない場合
   */
  public static void writeMxFormat(final DoubleComplexNumber[][] matrix, final OutputStream output, final String name) throws IOException {
    MxDataHead head = new MxDataHead(matrix, name);
    head.write(output);

    DataOutputStream ds = new DataOutputStream(new BufferedOutputStream(output));

    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ds.writeDouble(matrix[i][j].getRealPart().doubleValue());
        ds.writeDouble(matrix[i][j].getImaginaryPart().doubleValue());
      }
    }

    ds.flush();
  }

  /**
   * 入力ストリームから行列データ(MXフォーマット)を読み込みます。
   * 
   * @param input 入力ストリーム
   * @param head MXフォーマットのヘッダ情報
   * @return 読み込んだ行列
   * @throws IOException 入力ストリームから読み込めない場合
   */
  public static DoubleComplexNumber[][] readMxFormat(final InputStream input, final MxDataHead head) throws IOException {
    int rowSize = head.getRowSize();
    int columnSize = head.getColumnSize();
    DoubleComplexNumber[][] ans = new DoubleComplexNumber[rowSize][columnSize];

    DataInputStream is = new DataInputStream(input);

    if (head.isSameEndian()) {
      for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          double realPart = is.readDouble();
          double imagPart = is.readDouble();
          ans[i][j] = new DoubleComplexNumber(realPart, imagPart);
        }
      }
    } else {
      for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          long d = EndianTransformer.flip(is.readLong());
          double realPart = Double.longBitsToDouble(d);
          d = EndianTransformer.flip(is.readLong());
          double imagPart = Double.longBitsToDouble(d);
          ans[i][j] = new DoubleComplexNumber(realPart, imagPart);
        }
      }
    }
    return ans;
  }

  /**
   * 行列をMMフォーマットの文字列に変換します。
   * 
   * @param matrix 対象となる行列
   * @param elementFormat 出力フォーマット
   * @return MMフォーマットの文字列
   */
  public static String toMmString(final DoubleComplexNumber[][] matrix, final String elementFormat) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    StringBuffer sb = new StringBuffer();
    String newLine = System.getProperty("line.separator"); //$NON-NLS-1$

    if (rowSize == 0 || columnSize == 0) {
      return "[[]]"; //$NON-NLS-1$
    }

    if (columnSize == 1 && rowSize != 1) {
      return toMmString(GridUtil.transpose(matrix), elementFormat) + "'"; //$NON-NLS-1$
    }

    if (rowSize != 1) {
      sb.append("["); //$NON-NLS-1$
    }
    
    int displayColumnSize = 2;

    for (int i = 0; i < rowSize; i++) {
      if (i != 0) {
        sb.append(" "); //$NON-NLS-1$
      }

      for (int k = 0; k < columnSize;) {
        if (k == 0) {
          sb.append("["); //$NON-NLS-1$
        } else {
          sb.append(" "); //$NON-NLS-1$
          if (rowSize != 1) {
            sb.append(" "); //$NON-NLS-1$
          }
        }

        int j;
        for (j = k; j < k + displayColumnSize && j < columnSize; j++) {
          final DoubleComplexNumber element = matrix[i][j];
          sb.append(element.toMmString(elementFormat));
          if (j != columnSize - 1) {
            sb.append(","); //$NON-NLS-1$
          }
        }

        if (j == columnSize) {
          sb.append("]"); //$NON-NLS-1$
          if (i != rowSize - 1) {
            sb.append(newLine);
          }
        } else {
          sb.append(newLine);
        }

        k += displayColumnSize;
      }
    }

    if (rowSize != 1) {
      sb.append("]"); //$NON-NLS-1$
    }

    return sb.toString();
  }

  /**
   * 行列のノルムを返します。
   * 
   * @param matrix 対象となる行列
   * @param type ノルムの種類(NormType.ONE:1ノルム、NormType.TWO:2ノルム(最大特異値))
   * @return 行列のノルム
   */
  public static double norm(final DoubleComplexNumber[][] matrix, final NormType type) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    if (rowSize == 0 || columnSize == 0) {
      return 0;
    }

    if (type == NormType.ONE) {
      double maxsum = 0;
      for (int i = 0; i < rowSize; i++) {
        double sum = 0;
        for (int j = 0; j < rowSize; j++) {
          sum += matrix[j][i].abs().getRealPart().doubleValue();
        }
        if (sum > maxsum) {
          maxsum = sum;
        }
      }
      return maxsum;
    } else if (type == NormType.TWO) {
      return new DoubleComplexSingularValueDecomposer().norm(getRealPartElements(matrix), getImaginaryPartElements(matrix));
    } else {
      throw new IllegalArgumentException(Messages.getString("DoubleComplexMatrixUtil.20")); //$NON-NLS-1$
    }
  }

  /**
   * 実部をdoubleの2次元配列で返します。
   * 
   * @param matrix 対象となる行列
   * @return 実部の2次元配列
   */
  public static double[][] getRealPartElements(final DoubleComplexNumber[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    double[][] ans = new double[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = (matrix[i][j]).getRealPart().doubleValue();
      }
    }
    return ans;
  }

  /**
   * 虚部をdoubleの2次元配列で返します。
   * 
   * @param matrix 対象となる行列
   * @return 虚部の2次元配列
   */
  public static double[][] getImaginaryPartElements(final DoubleComplexNumber[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    double[][] ans = new double[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].getImaginaryPart().doubleValue();
      }
    }
    return ans;
  }
  
  /**
   * 実部行列を設定します。
   * 
   * @param matrix 対象となる行列
   * @param realPart 実部行列
   */
  public static void setRealPartElements(final DoubleComplexNumber[][] matrix, int[][] realPart) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j].setRealPart(realPart[i][j]);
      }
    }
  }

  /**
   * 実部行列を設定します。
   * 
   * @param matrix 対象となる行列
   * @param realPart 実部行列
   */
  public static void setRealPartElements(final DoubleComplexNumber[][] matrix, double[][] realPart) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j].setRealPart(realPart[i][j]);
      }
    }
  }
  
  /**
   * 虚部行列を設定します。
   * 
   * @param matrix 対象となる行列
   * @param imaginaryPart 虚部行列
   */
  public static void setImaginaryPartElements(final DoubleComplexNumber[][] matrix, int[][] imaginaryPart) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j].setImaginaryPart(imaginaryPart[i][j]);
      }
    }
  }

  /**
   * 虚部行列を設定します。
   * 
   * @param matrix 対象となる行列
   * @param imaginaryPart 虚部行列
   */
  public static void setImaginaryPartElements(final DoubleComplexNumber[][] matrix, double[][] imaginaryPart) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j].setImaginaryPart(imaginaryPart[i][j]);
      }
    }
  }



  /**
   * 単位行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 単位行列
   */
  public static DoubleComplexNumber[][] unit(final int rowSize, final int columnSize) {
    DoubleComplexNumber[][] ans = new DoubleComplexNumber[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        if (i == j) {
          ans[i][j] = new DoubleComplexNumber(1, 0);
        } else {
          ans[i][j] = new DoubleComplexNumber(0, 0);
        }
      }
    }
    return ans;
  }

  /**
   * 全ての成分が1である行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 全ての成分が1である行列
   */
  public static DoubleComplexNumber[][] ones(final int rowSize, final int columnSize) {
    DoubleComplexNumber[][] ans = new DoubleComplexNumber[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = new DoubleComplexNumber(1, 0);
      }
    }
    return ans;
  }

  /**
   * 実数の成分毎の累乗を成分とする行列を生成します。
   * 
   * @param scalar 累乗の対象となる実数
   * @param matrix 累乗の指数を成分とする行列
   * @return 累乗を成分とする行列
   */
  public static DoubleComplexNumber[][] powerElementWise(final double scalar, final DoubleComplexNumber[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    DoubleComplexNumber[][] ans = new DoubleComplexNumber[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = DoubleComplexNumberUtil.power(scalar, matrix[i][j]);
      }
    }
    return ans;
  }

  /**
   * ライターに出力します。
   * 
   * @param matrix 対象となる行列
   * @param output ライター
   * @param format 成分の出力フォーマット
   * @param alignment 成分の出力配置
   * @param maxColumnSize 最大列の数
   */
  public static void print(final DoubleComplexNumber[][] matrix, final Writer output, final String format, final GridElementAlignment alignment, final int maxColumnSize) {
    final PrintWriter writer = new PrintWriter(output);
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int remain = columnSize;
    int columnOffset = 0;

    final int[] printingColumnLengthes = getPrintingColumnLengthes(matrix, format);

    while (remain > 0) {
      final int printColumnSize = Math.min(maxColumnSize, remain);
      DoubleComplexMatrixUtil.printColumnNumber(columnOffset, printColumnSize, printingColumnLengthes, writer);

      for (int row = 0; row < rowSize; row++) {
        GridUtil.printRowNumber(row, writer);

        final DoubleComplexNumber[] elements = new DoubleComplexNumber[printColumnSize];
        for (int column = 0; column < printColumnSize; column++) {
          elements[column] = matrix[row][columnOffset + column];
        }

        DoubleComplexMatrixUtil.printElements(elements, writer, printingColumnLengthes, format, alignment);
      }
      columnOffset += printColumnSize;
      remain -= printColumnSize;
    }

    writer.flush();
  }

  /**
   * 行列の各列の出力文字列の長さの配列を返します。
   * 
   * @param elements 行列
   * @param format 成分の出力フォーマット
   * @return 行列の各列の出力文字列の長さの配列
   */
  private static int[] getPrintingColumnLengthes(final DoubleComplexNumber[][] elements, final String format) {
    final int rowSize = elements.length;
    final int columnSize = rowSize == 0 ? 0 : elements[0].length;
    final int[] printingColumnLengthes = new int[columnSize];

    for (int column = 0; column < columnSize; column++) {
      final DoubleComplexNumber[] columnElements = elements[0][0].createArray(rowSize);
      for (int row = 0; row < rowSize; row++) {
        columnElements[row] = elements[row][column];
      }

      printingColumnLengthes[column] = Math.max(getMaxLength(columnElements, format), 12 * 2 + 3);
    }

    return printingColumnLengthes;
  }

  /**
   * 成分の出力文字列の最大値を返します。
   * 
   * @param elements 成分の配列
   * @param format 出力フォーマット
   * @return 成分の出力文字列の最大値
   */
  static int getMaxLength(final DoubleComplexNumber[] elements, final String format) {
    int maxLength = 0;

    for (final DoubleComplexNumber element : elements) {
      final String realPart = DoubleNumber.toString(element.getRealPart().doubleValue(), format);
      int realLength = realPart.length();
      if (realPart.startsWith("-") == false) { //$NON-NLS-1$
        realLength++;
      }

      final String imagPart = DoubleNumber.toString(element.getImaginaryPart().doubleValue(), format);
      int imagLength = imagPart.length();
      if (imagPart.startsWith("-") == false) { //$NON-NLS-1$
        imagLength++;
      }

      final int length = Math.max(realLength, imagLength) * 2 + 3;
      maxLength = Math.max(maxLength, length);
    }

    return maxLength;
  }

  /**
   * 複数個の複素数をプリントライターに出力する。
   * 
   * @param elements 複素数の配列
   * @param output プリントスライター
   * @param printingColumnLengthes 各列の出力文字列の長さの配列
   * @param format 成分の出力フォーマット
   * @param alignment 成分の出力配置
   */
  private static void printElements(final DoubleComplexNumber[] elements, final PrintWriter output, final int[] printingColumnLengthes, final String format, final GridElementAlignment alignment) {
    for (int column = 0; column < elements.length; column++) {
      final DoubleComplexNumber element = elements[column];
      String realPart = DoubleNumber.toString(element.getRealPart().doubleValue(), format);
      if (realPart.startsWith("-") == false) { //$NON-NLS-1$
        realPart = " " + realPart; //$NON-NLS-1$
      }

      String imagPart = DoubleNumber.toString(element.getImaginaryPart().doubleValue(), format);
      if (imagPart.startsWith("-") == false) { //$NON-NLS-1$
        imagPart = " " + imagPart; //$NON-NLS-1$
      }

      // final int width = Math.max(realPart.length(), imagPart.length());
      final int width = (printingColumnLengthes[column] - 3) / 2;

      output.print(" "); //$NON-NLS-1$

      output.print(" "); //$NON-NLS-1$
      outputValue(realPart, output, width, alignment);

      output.print(" "); //$NON-NLS-1$
      outputValue(imagPart, output, width, alignment);
      output.print(" "); //$NON-NLS-1$
    }
    output.println();
  }

  /**
   * 実部あるいは虚部の値を出力します。
   * 
   * @param value 値
   * @param output プリントライター
   * @param width 幅
   * @param alignment 配列
   */
  private static void outputValue(final String value, final PrintWriter output, final int width, final GridElementAlignment alignment) {
    final int gap = width - value.length();
    if (0 < gap) {
      if (alignment == GridElementAlignment.RIGHT) {
        GridUtil.outputSpaces(gap, output);
      }
      if (alignment == GridElementAlignment.CENTER) {
        GridUtil.outputSpaces(gap / 2, output);
      }
    }

    output.print(value);

    if (0 < gap) {
      if (alignment == GridElementAlignment.LEFT) {
        GridUtil.outputSpaces(gap, output);
      }
      if (alignment == GridElementAlignment.CENTER) {
        GridUtil.outputSpaces(gap - gap / 2, output);
      }
    }
  }

  /**
   * 列番号を出力します。
   * 
   * @param columnOffset 列のオフセット
   * @param printColumnSize 出力する列の数
   * @param printingColumnLengthes 出力する列の幅
   * @param output 出力先のプリントライター
   */
  private static void printColumnNumber(final int columnOffset, final int printColumnSize, final int[] printingColumnLengthes, final PrintWriter output) {
    GridUtil.outputSpaces(GridFormat.LEFT_MARGIN, output);

    for (int column = 0; column < printColumnSize; column++) {
      final int printingLength = printingColumnLengthes[column];

      if (column != 0) {
        GridUtil.outputSpaces(1, output);
      }

      final String number = String.format("%3d", Integer.valueOf(columnOffset + column + 1)); //$NON-NLS-1$
      final String realNumber = "(" + number + ")-Real"; //$NON-NLS-1$ //$NON-NLS-2$
      final String imagNumber = "(" + number + ")-Imag"; //$NON-NLS-1$ //$NON-NLS-2$

      final int margin = printingLength - realNumber.length() - imagNumber.length() - 2;
      final int leftMargin = margin / 4;
      final int rightMargin = margin / 4;
      final int centerGap = margin - leftMargin - rightMargin;

      output.print("["); //$NON-NLS-1$
      GridUtil.outputSpaces(leftMargin, output);
      output.print(realNumber);
      GridUtil.outputSpaces(centerGap, output);
      output.print(imagNumber);
      GridUtil.outputSpaces(rightMargin, output);

      output.print("]"); //$NON-NLS-1$
    }
    output.println();
  }

  /**
   * 一様分布の乱数を成分とする行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 一様分布の乱数を成分とする行列
   */
  public static DoubleComplexNumber[][] createUniformRandom(final int rowSize, final int columnSize) {
    final Random rand = new Random();
    final DoubleComplexNumber[][] ans = new DoubleComplexNumber[rowSize][columnSize];
    
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = new DoubleComplexNumber(rand.nextDouble(), rand.nextDouble());
      }
    }
    return ans;
  }

  /**
   * 一様分布の乱数を成分とする行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param seed 乱数の種
   * @return 一様分布の乱数を成分とする行列
   */
  public static DoubleComplexNumber[][] createUniformRandom(final int rowSize, final int columnSize, final long seed) {
    final Random rand = new Random(seed);
    final DoubleComplexNumber[][] ans = new DoubleComplexNumber[rowSize][columnSize];
    
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = new DoubleComplexNumber(rand.nextDouble(), rand.nextDouble());
      }
    }
    return ans;
  }
  
  /**
   * 正規分布の乱数を成分とする行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 正規分布の乱数を成分とする行列
   */
  public static DoubleComplexNumber[][] createNormalRandom(final int rowSize, final int columnSize) {
    final DoubleComplexUniformRandom uniformRandom = new DoubleComplexUniformRandom();
    final DoubleComplexNormalRandom random = new DoubleComplexNormalRandom(uniformRandom);
    final DoubleComplexNumber[][] ans = new DoubleComplexNumber[rowSize][columnSize];
    
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = random.nextValue();
      }
    }
    return ans;
  }

  /**
   * 正規分布の乱数を成分とする行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param realSeed 実部の乱数の種
   * @param imaginarySeed 虚部の乱数の種
   * @return 正規分布の乱数を成分とする行列
   */
  public static DoubleComplexNumber[][] createNormalRandom(final int rowSize, final int columnSize, final long realSeed, final long imaginarySeed) {
    final DoubleComplexUniformRandom uniformRandom = new DoubleComplexUniformRandom(realSeed, imaginarySeed);
    final DoubleComplexNormalRandom random = new DoubleComplexNormalRandom(uniformRandom);
    final DoubleComplexNumber[][] ans = new DoubleComplexNumber[rowSize][columnSize];
    
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = random.nextValue();
      }
    }
    return ans;
  }

  /**
   * 全ての成分の絶対値を成分とする行列を生成します。
   * 
   * @param matrix 元の行列
   * @return 成分の絶対値を成分とする行列
   */
  public static DoubleComplexNumber[][] absElementWise(final DoubleComplexNumber[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    DoubleComplexNumber[][] ans = matrix[0][0].createArray(rowSize, columnSize);
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = new DoubleComplexNumber(matrix[i][j].abs().getRealPart(), 0);
      }
    }
    return ans;
  }

  /**
   * 自身の各成分の偏角を成分に持つ行列を返します。
   * 
   * @param matrix 対象となる行列
   * @return 偏角行列
   */
  public static DoubleNumber[][] argumentElementWise(final DoubleComplexNumber[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    DoubleNumber[][] ans = matrix[0][0].getRealPart().createArray(rowSize, columnSize);
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].arg();
      }
    }
    return ans;
  }

}
