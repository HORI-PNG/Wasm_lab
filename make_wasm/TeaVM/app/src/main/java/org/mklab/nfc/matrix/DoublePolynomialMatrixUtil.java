/*
 * $Id: PolynomialMatrixUtil.java,v 1.79 2008/07/15 15:27:15 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.matrix;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.mklab.nfc.matx.MxDataHead;
import org.mklab.nfc.scalar.DoubleComplexNumber;
import org.mklab.nfc.scalar.DoubleComplexPolynomial;
import org.mklab.nfc.scalar.DoubleNumber;
import org.mklab.nfc.scalar.DoublePolynomial;
import org.mklab.nfc.util.EndianTransformer;
import org.mklab.nfc.util.PolynomialTokenizer;


/**
 * 多項式行列に関するユーティリティクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.79 $, 2004/07/02
 */

public final class DoublePolynomialMatrixUtil {

  /**
   * 新しく生成された<code>PolynomialMatrixUtil</code>オブジェクトを初期化します。
   */
  private DoublePolynomialMatrixUtil() {
    // nothing to do
  }

  /**
   * 実行列の各成分を定数項とする多項式を成分とする多項式行列に変換します。
   * 
   * @param matrix 実行列
   * @return 実行列の各成分を定数項とする多項式を成分とする多項式行列
   */
  public static DoublePolynomial[][] createArray(final double[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final DoublePolynomial[][] ans = new DoublePolynomial[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      final DoublePolynomial[] ansi = ans[i];
      final double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = new DoublePolynomial(matrixi[j]);
      }
    }
    return ans;
  }

  /**
   * 実行列の各成分を定数項とする多項式を成分とする多項式行列に変換します。
   * 
   * @param matrix 実行列
   * @param variable 多項式変数
   * @return 実行列の各成分を定数項とする多項式を成分とする多項式行列
   */
  public static DoublePolynomial[][] createArray(final double[][] matrix, String variable) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final DoublePolynomial[][] ans = new DoublePolynomial[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      final DoublePolynomial[] ansi = ans[i];
      final double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = new DoublePolynomial(matrixi[j], variable);
      }
    }
    return ans;
  }

  /**
   * 行列の各成分を定数項とする多項式を成分とする多項式行列に変換します。
   * 
   * @param matrix 行列
   * @return 行列の各成分を定数項とする多項式を成分とする多項式行列
   */
  public static DoublePolynomial[][] createArray(final DoubleNumber[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final DoublePolynomial[][] ans = new DoublePolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = new DoublePolynomial(matrix[i][j]);
      }
    }
    return ans;
  }

  /**
   * 整数行列を多項式行列(元の行列を定数項とする)に変換します。
   * 
   * @param matrix 整数行列
   * @return 多項式行列
   */
  public static DoublePolynomial[][] createArray(final int[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final DoublePolynomial[][] ans = new DoublePolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      final int[] matrixi = matrix[i];
      final DoublePolynomial[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = new DoublePolynomial(matrixi[j]);
      }
    }
    return ans;
  }
  
  /**
   * {@link DoubleComplexNumber}を係数とする多項式の配列を生成します。
   * 
   * @param elements {@link DoubleNumber}を係数とする多項式の配列
   * @return {@link DoubleComplexNumber}を係数とする多項式の配列
   */
  public static DoubleComplexPolynomial[] createComplexArray(DoublePolynomial[] elements) {
    final int elementsSize = elements.length;
    final DoubleComplexPolynomial[] complexElements = new DoubleComplexPolynomial[elementsSize];

    for (int i = 0; i < elementsSize; i++) {
      complexElements[i] = new DoubleComplexPolynomial(elements[i]);
    }

    return complexElements;
  }

  /**
   * {@link DoubleComplexNumber}を係数とする多項式の配列を生成します。
   * 
   * @param rePart {@link DoubleNumber}を係数とする実部多項式の配列
   * @param imPart {@link DoubleNumber}を係数とする虚部多項式の配列
   * @return {@link DoubleComplexNumber}を係数とする多項式の配列
   */
  public static DoubleComplexPolynomial[] createComplexArray(DoublePolynomial[] rePart, DoublePolynomial[] imPart) {
    final int elementsSize = rePart.length;
    final DoubleComplexPolynomial[] complexElements = new DoubleComplexPolynomial[elementsSize];

    for (int i = 0; i < elementsSize; i++) {
      complexElements[i] = new DoubleComplexPolynomial(rePart[i], imPart[i]);
    }

    return complexElements;
  }

  /**
   * {@link DoubleComplexNumber}を係数とする多項式の配列を生成します。
   * 
   * @param elements {@link DoubleNumber}を係数とする多項式の配列
   * @return {@link DoubleComplexNumber}を係数とする多項式の配列
   */
  public static DoubleComplexPolynomial[][] createComplexArray(DoublePolynomial[][] elements) {
    final int elementsRowSize = elements.length;
    final int elementsColumnSize = elements[0].length;
    final DoubleComplexPolynomial[][] complexElements = new DoubleComplexPolynomial[elementsRowSize][elementsColumnSize];

    for (int i = 0; i < elementsRowSize; i++) {
      for (int j = 0; j < elementsColumnSize; j++) {
        complexElements[i][j] = new DoubleComplexPolynomial(elements[i][j]);
      }
    }

    return complexElements;
  }

  /**
   * {@link DoubleComplexNumber}を係数とする多項式の配列を生成します。
   * 
   * @param rePart {@link DoubleNumber}を係数とする実部多項式の配列
   * @param imPart {@link DoubleNumber}を係数とする虚部多項式の配列
   * @return {@link DoubleComplexNumber}を係数とする多項式の配列
   */
  public static DoubleComplexPolynomial[][] createComplexArray(DoublePolynomial[][] rePart, DoublePolynomial[][] imPart) {
    final int elementsRowSize = rePart.length;
    final int elementsColumnSize = rePart[0].length;
    final DoubleComplexPolynomial[][] complexElements = new DoubleComplexPolynomial[elementsRowSize][elementsColumnSize];

    for (int i = 0; i < elementsRowSize; i++) {
      for (int j = 0; j < elementsColumnSize; j++) {
        complexElements[i][j] = new DoubleComplexPolynomial(rePart[i][j], imPart[i][j]);
      }
    }

    return complexElements;
  }

  /**
   * 単位行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param variableName 変数名
   * @return 単位行列
   */
  public static DoublePolynomial[][] unit(final int rowSize, final int columnSize, final String variableName) {
    final DoublePolynomial[][] ans = new DoublePolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        if (i == j) {
          ans[i][j] = new DoublePolynomial(1.0, variableName);
        } else {
          ans[i][j] = new DoublePolynomial(0.0, variableName);
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
   * @param variableName 変数名
   * @return 全ての成分が1である行列
   */
  public static DoublePolynomial[][] ones(final int rowSize, final int columnSize, final String variableName) {
    final DoublePolynomial[][] matrix = new DoublePolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j] = new DoublePolynomial(1.0, variableName);
      }
    }
    return matrix;
  }

  /**
   * 行列を出力ストリームに出力(MXフォーマット)します。
   * 
   * @param matrix 対象となる行列
   * @param output 出力ストリーム
   * @param name 行列の名前
   * @throws IOException ストリームに出力できない場合
   */
  public static void writeMxFormat(final DoublePolynomial[][] matrix, final DataOutputStream output, final String name) throws IOException {
    final MxDataHead head = new MxDataHead(matrix, name);

    head.write(output);

    final int rowSize = matrix.length;
    final int columnSize = matrix.length == 0 ? 0 : matrix[0].length;

    if (head.getMatrixType() == MxDataHead.REAL_POLYNOMIAL_MATRIX) {
      for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          output.writeInt((matrix[i][j]).getDegree());
          (matrix[i][j]).writeMxFormatWithoutHeader(output, false);
        }
      }
    } else {
      // assert head.getMatrixType() == mxDataHead.COMPLEX_POLYNOMIAL_MATRIX;
      for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          output.writeInt((matrix[i][j]).getDegree());
          (matrix[i][j]).writeMxFormatWithoutHeader(output, true);
          // 全成分を複素多項式として保存
        }
      }
    }

    output.flush();
  }

  /**
   * 入力ストリームから行列データ(MXフォーマット)を読み込みます。
   * 
   * @param input 入力ストリーム
   * @param head MXフォーマットのヘッダ情報
   * @return 読み込んだ行列
   * @throws IOException 入力ストリームから読み込めない場合
   */
  public static DoublePolynomial[][] readMxFormat(final InputStream input, final MxDataHead head) throws IOException {
    final int rowSize = head.getRowSize();
    final int columnSize = head.getColumnSize();
    final int matrixType = head.getMatrixType();
    //final int version = head.getVersion();

    final DataInputStream is = new DataInputStream(input);

    if (matrixType == MxDataHead.REAL_POLYNOMIAL_MATRIX) {
      final DoublePolynomial[][] ans = new DoublePolynomial[rowSize][columnSize];
      
      if (head.isSameEndian()) {
        for (int i = 0; i < rowSize; i++) {
          for (int j = 0; j < columnSize; j++) {
            final int degree = is.readInt();
            final double[] coef = new double[degree + 1];
            for (int k = 0; k <= degree; k++) {
              coef[k] = is.readDouble();
            }
            ans[i][j] = new DoublePolynomial(coef);
          }
        }
      } else {
        for (int i = 0; i < rowSize; i++) {
          for (int j = 0; j < columnSize; j++) {
            final int degree = EndianTransformer.flip(is.readInt());
            final double[] coef = new double[degree + 1];
            for (int k = 0; k <= degree; k++) {
              coef[k] = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
            }
            ans[i][j] = new DoublePolynomial(coef);
          }
        }
      }
      
      return ans;
      
    } 
    
    // ComplexPolynomialMatrix
    throw new UnsupportedOperationException();
      
//      final Polynomial[][] ans = new Polynomial[rowSize][columnSize];
//      
//      // assert matrixType == 3;
//      if (head.isSameEndian()) {
//        for (int i = 0; i < rowSize; i++) {
//          for (int j = 0; j < columnSize; j++) {
//            final int degree = is.readInt();
//            final DoubleComplexNumber[] coef = new DoubleComplexNumber[degree + 1];
//            for (int k = 0; k <= degree; k++) {
//              final double real = is.readDouble();
//              final double imag = is.readDouble();
//              coef[k] = new DoubleComplexNumber(real, imag);
//            }
//            ans[i][j] = new Polynomial(coef);
//          }
//        }
//      } else {
//        for (int i = 0; i < rowSize; i++) {
//          for (int j = 0; j < columnSize; j++) {
//            final int degree = EndianTransformer.flip(is.readInt());
//            final DoubleComplexNumber[] coef = new DoubleComplexNumber[degree + 1];
//            for (int k = 0; k <= degree; k++) {
//              final double real = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
//              final double imag = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
//              coef[k] = new DoubleComplexNumber(real, imag);
//            }
//            ans[i][j] = new Polynomial(coef);
//          }
//        }
//      }
//      
//      return ans;
  }

  /**
   * 行列をMMフォーマットの文字列に変換します。
   * 
   * @param matrix 対象となる行列
   * @param elementFormat 成分の出力フォーマット
   * @return MMフォーマットの文字列
   */
  public static String toMmString(final DoublePolynomial[][] matrix, final String elementFormat) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final StringBuffer sb = new StringBuffer();
    final String newLine = System.getProperty("line.separator"); //$NON-NLS-1$

    if (rowSize == 0 || columnSize == 0) {
      return "[[]]"; //$NON-NLS-1$
    }

    if (columnSize == 1 && rowSize != 1) {
      return toMmString(GridUtil.transpose(matrix), elementFormat) + "'"; //$NON-NLS-1$
    }

    if (rowSize != 1) {
      sb.append("["); //$NON-NLS-1$
    }

    final int displayColumnSize = 1;

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
          sb.append((matrix[i][j]).toMmString(elementFormat));
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
   * 空白文字列を出力します。
   * 
   * @param count 空白の数
   * @param output 出力先のプリントライター
   */
  private static void outputSpaces(final int count, final PrintWriter output) {
    final char[] spaces = new char[count];
    for (int i = 0; i < count; i++) {
      spaces[i] = ' ';
    }
    output.print(spaces);
  }

  /**
   * 複数個の多項式をプリントライターに出力します。
   * 
   * @param elements 多項式の配列
   * @param output 出力先のプリントライター
   * @param printingColumnLengthes 多項式の出力文字列の長さ
   * @param coefficientFormat 係数の出力フォーマット
   * @param bottomAlign 表示が数行になる場合に下に揃えるならばtrue、上に揃えるならばfalse
   */
  static void printElement(final DoublePolynomial[] elements, final PrintWriter output, final int[] printingColumnLengthes, final String coefficientFormat, final boolean bottomAlign) {
    final int leftMargin = GridFormat.LEFT_MARGIN;
    final int columnSeparation = GridFormat.COLUMN_SEPARATION;

    final String[][] stringLines = new String[elements.length][];

    int maxLines = 0;
    for (int column = 0; column < elements.length; column++) {
      final int printingColumnLength = printingColumnLengthes[column];
      stringLines[column] = PolynomialTokenizer.split(elements[column].toString(coefficientFormat), printingColumnLength);
      if (maxLines < stringLines[column].length) {
        maxLines = stringLines[column].length;
      }
    }

    for (int i = 0; i < maxLines; i++) {
      if (i != 0) {
        DoublePolynomialMatrixUtil.outputSpaces(leftMargin, output);
      }

      for (int column = 0; column < stringLines.length; column++) {
        final int printingColumnLength = printingColumnLengthes[column];
        final String[] lines = stringLines[column];

        if (bottomAlign) {
          if (i < maxLines - lines.length) {
            DoublePolynomialMatrixUtil.outputSpaces(printingColumnLength, output);
          } else {
            final int k = i - (maxLines - lines.length);

            int length = lines[k].length();
            if (lines.length == 1) {
              final int margin = (printingColumnLength - length) / 2;
              DoublePolynomialMatrixUtil.outputSpaces(margin, output);
              length += margin;
            }

            output.print(lines[k]);
            DoublePolynomialMatrixUtil.outputSpaces(1, output);
            DoublePolynomialMatrixUtil.outputSpaces((printingColumnLength - length), output);
          }
        } else {
          if (lines.length <= i) {
            DoublePolynomialMatrixUtil.outputSpaces(printingColumnLength, output);
          } else {
            int length = lines[i].length();
            if (lines.length == 1) {
              final int margin = (printingColumnLength - length) / 2;
              DoublePolynomialMatrixUtil.outputSpaces(margin, output);
              length += margin;
            }

            output.print(lines[i]);
            DoublePolynomialMatrixUtil.outputSpaces(1, output);
            DoublePolynomialMatrixUtil.outputSpaces((printingColumnLength - length), output);
          }
        }

        if (column != stringLines.length - 1) {
          DoublePolynomialMatrixUtil.outputSpaces(columnSeparation, output);
        }
      }
      output.println();
    }
  }

  /**
   * 多項式の出力文字列の最大値を返します。
   * 
   * @param polynomials 多項式の配列
   * @param coefficientFormat 係数の出力フォーマット
   * @return 多項式の出力文字列の最大値
   */
  static int getMaxLength(final DoublePolynomial[] polynomials, final String coefficientFormat) {
    int maxLength = 0;
    for (final DoublePolynomial polynomial : polynomials) {
      final int length = polynomial.toString(coefficientFormat).length();
      maxLength = Math.max(maxLength, length);
    }

    return maxLength;
  }

  /**
   * 列番号を出力します。
   * 
   * @param columnOffset 列のオフセット
   * @param printingColumnSize 出力する列の数
   * @param printingColumnLengthes 出力する列の長さ
   * @param output 出力先のプリントライター
   */
  static void printColumnNumbers(final int columnOffset, final int printingColumnSize, final int[] printingColumnLengthes, final PrintWriter output) {
    outputSpaces(GridFormat.LEFT_MARGIN, output);
    for (int i = 0; i < printingColumnSize; i++) {
      final int printingLength = printingColumnLengthes[i];

      if (i != 0) {
        outputSpaces(GridFormat.COLUMN_SEPARATION, output);
      }

      final String number = String.format("(%3d)", Integer.valueOf(columnOffset + i + 1)); //$NON-NLS-1$

      final int leftMargin = (printingLength - number.length() - 2) / 2;

      output.print("["); //$NON-NLS-1$
      outputSpaces(leftMargin, output);
      output.print(number);
      outputSpaces(printingLength - number.length() - leftMargin - 1, output);
      output.print("]"); //$NON-NLS-1$
    }
    output.println();
  }

  /**
   * 各成分の不定積分を返します。
   * 
   * @param matrix 対象となる行列
   * @param order 階数
   * @return 多項式行列の不定積分
   */
  public static DoublePolynomial[][] integral(final DoublePolynomial[][] matrix, final int order) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final DoublePolynomial[][] ans = GridUtil.<DoublePolynomial> createArray(rowSize, columnSize, matrix);

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = (matrix[i][j]).integral(order);
      }
    }
    return ans;
  }
}
