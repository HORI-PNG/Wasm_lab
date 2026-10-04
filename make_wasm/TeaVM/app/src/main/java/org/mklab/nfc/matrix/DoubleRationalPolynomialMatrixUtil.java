/*
 * $Id: RationalPolynomialMatrixUtil.java,v 1.80 2008/07/15 15:27:15 koga Exp $
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
import java.io.Writer;

import org.mklab.nfc.matx.MxDataHead;
import org.mklab.nfc.scalar.DoubleComplexNumber;
import org.mklab.nfc.scalar.DoubleComplexRationalPolynomial;
import org.mklab.nfc.scalar.DoubleNumber;
import org.mklab.nfc.scalar.DoublePolynomial;
import org.mklab.nfc.scalar.DoubleRationalPolynomial;
import org.mklab.nfc.scalar.Polynomial;
import org.mklab.nfc.util.EndianTransformer;


/**
 * 有理多項式行列に関するユーティリティクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.80 $, 2004/07/02
 */

public final class DoubleRationalPolynomialMatrixUtil {

  /**
   * 新しく生成された<code>RationalPolynomialMatrixUtil</code>オブジェクトを初期化します。
   */
  private DoubleRationalPolynomialMatrixUtil() {
    // nothing to do
  }

  /**
   * 実行列の各成分を定数項とする有理多項式を成分とする有理多項式行列に変換します。
   * 
   * @param matrix 実行列
   * @return 実行列の各成分を定数項とする有理多項式を成分とする有理多項式行列
   */
  public static DoubleRationalPolynomial[][] createArray(final double[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final DoubleRationalPolynomial[][] ans = new DoubleRationalPolynomial[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      final DoubleRationalPolynomial[] ansi = ans[i];
      final double[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = new DoubleRationalPolynomial(matrixi[j]);
      }
    }
    return ans;
  }

  /**
   * 行列の各成分を定数項とする有理多項式を成分とする有理多項式行列に変換します。
   * 
   * @param matrix 定数行列
   * @return 行列の各成分を定数項とする有理多項式を成分とする有理多項式行列
   */
  public static DoubleRationalPolynomial[][] createArray(final DoubleNumber[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final DoubleRationalPolynomial[][] ans = new DoubleRationalPolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = new DoubleRationalPolynomial(matrix[i][j]);
      }
    }
    return ans;
  }

  /**
   * 整数行列を有理多項式行列(元の行列を分子の定数項とする)に変換します。
   * 
   * @param matrix 整数行列
   * @return 有理多項式行列
   */
  public static DoubleRationalPolynomial[][] createArray(final int[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final DoubleRationalPolynomial[][] ans = new DoubleRationalPolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      final int[] matrixi = matrix[i];
      final DoubleRationalPolynomial[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = new DoubleRationalPolynomial(matrixi[j]);
      }
    }
    return ans;
  }

  /**
   * 多項式行列を有理多項式行列(元の行列を分子の定数項とする)に変換します。
   * 
   * @param numerators 多項式行列
   * @return 有理多項式行列
   */
  public static DoubleRationalPolynomial[][] createArray(final DoublePolynomial[][] numerators) {
    final int rowSize = numerators.length;
    final int columnSize = rowSize == 0 ? 0 : numerators[0].length;

    final DoubleRationalPolynomial[][] ans = new DoubleRationalPolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      final DoubleRationalPolynomial[] ansi = ans[i];
      final DoublePolynomial[] matrixi = numerators[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = new DoubleRationalPolynomial(matrixi[j]);
      }
    }
    return ans;
  }

  /**
   * 多項式行列を有理多項式行列(元の行列を分子の定数項とする)に変換します。
   * 
   * @param numerators 分子多項式行列
   * @param denominators 分母多項式行列
   * @return 有理多項式行列
   */
  public static DoubleRationalPolynomial[][] createArray(final DoublePolynomial[][] numerators, final DoublePolynomial[][] denominators) {
    final int rowSize = numerators.length;
    final int columnSize = rowSize == 0 ? 0 : numerators[0].length;

    final DoubleRationalPolynomial[][] ans = new DoubleRationalPolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      final DoubleRationalPolynomial[] ansi = ans[i];
      final DoublePolynomial[] numerator = numerators[i];
      final DoublePolynomial[] denominator = denominators[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = new DoubleRationalPolynomial(numerator[j], denominator[j]);
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
  public static DoubleComplexRationalPolynomial[][] createComplexArray(DoubleRationalPolynomial[][] elements) {
    final int elementsRowSize = elements.length;
    final int elementsColumnSize = elements[0].length;
    final DoubleComplexRationalPolynomial[][] complexElements = new DoubleComplexRationalPolynomial[elementsRowSize][elementsColumnSize];

    for (int i = 0; i < elementsRowSize; i++) {
      for (int j = 0; j < elementsColumnSize; j++) {
        complexElements[i][j] = new DoubleComplexRationalPolynomial(elements[i][j]);
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
  public static DoubleComplexRationalPolynomial[][] createComplexArray(DoubleRationalPolynomial[][] rePart, DoubleRationalPolynomial[][] imPart) {
    final int elementsRowSize = rePart.length;
    final int elementsColumnSize = rePart[0].length;
    final DoubleComplexRationalPolynomial[][] complexElements = new DoubleComplexRationalPolynomial[elementsRowSize][elementsColumnSize];

    for (int i = 0; i < elementsRowSize; i++) {
      for (int j = 0; j < elementsColumnSize; j++) {
        complexElements[i][j] = new DoubleComplexRationalPolynomial(rePart[i][j], imPart[i][j]);
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
  public static DoubleRationalPolynomial[][] unit(final int rowSize, final int columnSize, final String variableName) {
    final DoubleRationalPolynomial[][] ans = new DoubleRationalPolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        if (i == j) {
          ans[i][j] = new DoubleRationalPolynomial(1.0, variableName);
        } else {
          ans[i][j] = new DoubleRationalPolynomial(0.0, variableName);
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
  public static DoubleRationalPolynomial[][] ones(final int rowSize, final int columnSize, final String variableName) {
    final DoubleRationalPolynomial[][] ans = new DoubleRationalPolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = new DoubleRationalPolynomial(1.0, variableName);
      }
    }
    return ans;
  }

  /**
   * Returns result of multiplication.
   * 
   * @param matrix 対象となる行列
   * @param polynomial polynomial
   * @return 成分毎の分子多項式を成分とする行列({@link Polynomial}の2次元配列)
   */
  public static DoubleRationalPolynomial[][] multiply(final DoubleRationalPolynomial[][] matrix, DoublePolynomial polynomial) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final DoubleRationalPolynomial[][] ans = new DoubleRationalPolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].multiply(polynomial);
      }
    }
    return ans;
  }

  /**
   * Returns result of multiplication.
   * 
   * @param matrix 対象となる行列
   * @param polynomial polynomial
   * @return 成分毎の分子多項式を成分とする行列({@link Polynomial}の2次元配列)
   */
  public static DoubleRationalPolynomial[][] divide(final DoubleRationalPolynomial[][] matrix, DoublePolynomial polynomial) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final DoubleRationalPolynomial[][] ans = new DoubleRationalPolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].divide(polynomial);
      }
    }
    return ans;
  }

  /**
   * 成分毎の分子多項式を成分とする行列を{@link Polynomial}の2次元配列で返します。
   * 
   * @param matrix 対象となる行列
   * @return 成分毎の分子多項式を成分とする行列({@link Polynomial}の2次元配列)
   */
  public static DoublePolynomial[][] getNumeratorElementWise(final DoubleRationalPolynomial[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final DoublePolynomial[][] ans = new DoublePolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].getNumerator();
      }
    }
    return ans;
  }

  /**
   * 成分毎の分母多項式を成分とする行列を{@link Polynomial}の2次元配列で返します。
   * 
   * @param matrix 対象となる行列
   * @return 成分毎の分母多項式を成分とする行列({@link Polynomial}の2次元配列)
   */
  public static DoublePolynomial[][] getDenominatorElementWise(final DoubleRationalPolynomial[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final DoublePolynomial[][] ans = new DoublePolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].getDenominator();
      }
    }
    return ans;
  }

  /**
   * 成分毎の商多項式を成分とする行列を{@link Polynomial}の2次元配列で返します。
   * 
   * @param matrix 対象となる行列
   * @return 成分毎の商多項式を成分とする行列({@link Polynomial}の2次元配列)
   */
  public static DoublePolynomial[][] getQuotientElementWise(final DoubleRationalPolynomial[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final DoublePolynomial[][] ans = new DoublePolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].getQuotient();
      }
    }
    return ans;
  }

  /**
   * 成分毎の剰余多項式を成分とする行列を{@link Polynomial}の2次元配列で返します。
   * 
   * @param matrix 対象となる行列
   * @return 成分毎の剰余多項式を成分とする行列({@link Polynomial}の2次元配列)
   */
  public static DoublePolynomial[][] getRemainderElementWise(final DoubleRationalPolynomial[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final DoublePolynomial[][] ans = new DoublePolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].getRemainder();
      }
    }
    return ans;
  }

  /**
   * 行列をMMフォーマットの文字列に変換します。
   * 
   * @param matrix 対象となる行列
   * @param elementFormat 成分の出力フォーマット
   * @return MMフォーマットの文字列
   */
  public static String toMmString(final DoubleRationalPolynomial[][] matrix, final String elementFormat) {
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
          final DoubleRationalPolynomial element = matrix[i][j];
          final DoublePolynomial numerator = element.getNumerator();
          final DoublePolynomial denominator = element.getDenominator();
          final String[] nm = new String[] {numerator.toMmString(elementFormat), denominator.toMmString(elementFormat)};
          sb.append("("); //$NON-NLS-1$
          sb.append(nm[0]);
          sb.append(")/"); //$NON-NLS-1$
          sb.append(newLine);
          sb.append("  ("); //$NON-NLS-1$
          sb.append(nm[1]);
          sb.append(")"); //$NON-NLS-1$
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
   * 複数個の有理多項式をプリントライターに出力します。
   * 
   * @param elements 有理多項式の配列
   * @param row 行番号
   * @param output 出力先のプリントライター
   * @param printingColumnLengthes 出力する列の長さ
   * @param coefficientFormat 係数の出力フォーマット
   */
  private static void printElement(final DoubleRationalPolynomial[] elements, final int row, final PrintWriter output, final int[] printingColumnLengthes, final String coefficientFormat) {
    final DoublePolynomial[] numerators = new DoublePolynomial[elements.length];
    for (int i = 0; i < elements.length; i++) {
      numerators[i] = elements[i].getNumerator();
    }

    final DoublePolynomial[] denominators = new DoublePolynomial[elements.length];
    for (int i = 0; i < elements.length; i++) {
      denominators[i] = elements[i].getDenominator();
    }

    DoubleRationalPolynomialMatrixUtil.outputSpaces(GridFormat.LEFT_MARGIN, output);
    DoublePolynomialMatrixUtil.printElement(numerators, output, printingColumnLengthes, coefficientFormat, true);

    DoubleRationalPolynomialMatrixUtil.printRowNumber(row, output);

    for (int column = 0; column < elements.length; column++) {
      DoubleRationalPolynomialMatrixUtil.outputSpaces(1, output);
      DoubleRationalPolynomialMatrixUtil.outputLines(printingColumnLengthes[column], output);
      if (column != elements.length - 1) {
        DoubleRationalPolynomialMatrixUtil.outputSpaces(GridFormat.COLUMN_SEPARATION, output);
      }
    }

    output.println();

    DoubleRationalPolynomialMatrixUtil.outputSpaces(GridFormat.LEFT_MARGIN, output);
    DoublePolynomialMatrixUtil.printElement(denominators, output, printingColumnLengthes, coefficientFormat, false);
  }

  /**
   * 有理多項式行列の各列の出力文字列の長さの配列を返します。
   * 
   * @param elements 行列
   * @param coefficientFormat 多項式の係数フォーマット
   * @return 有理多項式行列の各列の出力文字列の長さの配列
   */
  private static int[] getPrintingColumnLengthes(final DoubleRationalPolynomial[][] elements, final String coefficientFormat) {
    final int rowSize = elements.length;
    final int columnSize = rowSize == 0 ? 0 : elements[0].length;
    final int[] printingColumnLengthes = new int[columnSize];

    for (int column = 0; column < columnSize; column++) {
      final DoublePolynomial[] numerators = new DoublePolynomial[rowSize];
      for (int row = 0; row < rowSize; row++) {
        numerators[row] = elements[row][column].getNumerator();
      }

      final DoublePolynomial[] denominators = new DoublePolynomial[rowSize];
      for (int row = 0; row < rowSize; row++) {
        denominators[row] = elements[row][column].getDenominator();
      }

      final int maxNumeratorLength = DoublePolynomialMatrixUtil.getMaxLength(numerators, coefficientFormat);
      final int maxDenominatorLength = DoublePolynomialMatrixUtil.getMaxLength(denominators, coefficientFormat);
      printingColumnLengthes[column] = Math.max(Math.max(maxNumeratorLength, maxDenominatorLength), 6);
    }

    return printingColumnLengthes;
  }

  /**
   * 行番号を出力します。
   * 
   * @param row 行番号
   * @param output 出力先のプリントライター
   */
  private static void printRowNumber(final int row, final PrintWriter output) {
    output.print(String.format(" (%3d)", Integer.valueOf(row + 1))); //$NON-NLS-1$
  }

  /**
   * マイナス文字列を出力します。
   * 
   * @param count マイナスの数
   * @param output 出力先のプリントライター
   */
  private static void outputLines(final int count, final PrintWriter output) {
    final char[] lines = new char[count];
    for (int i = 0; i < count; i++) {
      lines[i] = '-';
    }
    output.print(lines);
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
   * ライターに出力します。
   * 
   * @param matrix 対象となる行列
   * @param output ライター
   * @param coefficientFormat 係数の出力フォーマット
   * @param maxColumnSize 最大列数
   */
  public static void print(final DoubleRationalPolynomial[][] matrix, final Writer output, final String coefficientFormat, final int maxColumnSize) {
    final PrintWriter writer = new PrintWriter(output);
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int remain = columnSize;
    int columnOffset = 0;

    final int[] printingColumnLengthes = getPrintingColumnLengthes(matrix, coefficientFormat);

    while (remain > 0) {
      final int printingColumnSize = Math.min(maxColumnSize, remain);
      PolynomialMatrixUtil.printColumnNumbers(columnOffset, printingColumnSize, printingColumnLengthes, writer);

      for (int row = 0; row < rowSize; row++) {
        final DoubleRationalPolynomial[] elements = new DoubleRationalPolynomial[printingColumnSize];
        for (int j = 0; j < printingColumnSize; j++) {
          elements[j] = matrix[row][columnOffset + j];
        }

        DoubleRationalPolynomialMatrixUtil.printElement(elements, row, writer, printingColumnLengthes, coefficientFormat);

        if (row != rowSize - 1) {
          writer.println();
        }
      }

      columnOffset += printingColumnSize;
      remain -= printingColumnSize;

      if (remain > 0) {
        writer.println();
      }
    }

    writer.flush();
  }

  /**
   * 行列をMXフォーマットで出力ストリームに出力します。
   * 
   * @param matrix 対象となる行列
   * @param output 出力ストリーム
   * @param name 行列の名前
   * @throws IOException ストリームに出力できない場合
   */
  public static void writeMxFormat(final DoubleRationalPolynomial[][] matrix, final DataOutputStream output, final String name) throws IOException {
    final MxDataHead head = new MxDataHead(matrix, name);

    head.write(output);

    final int rowSize = matrix.length;
    final int columnSize = matrix.length == 0 ? 0 : matrix[0].length;

    if (head.getMatrixType() == MxDataHead.REAL_RATIONAL_POLYNOMIAL_MATRIX) {
      for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          output.writeInt(matrix[i][j].getNumeratorDegree());
          output.writeInt(matrix[i][j].getDenominatorDegree());
          matrix[i][j].getNumerator().writeMxFormatWithoutHeader(output, false);
          matrix[i][j].getDenominator().writeMxFormatWithoutHeader(output, false);
        }
      }
    } else {
      // assert head.getMatrixType() == 3;
      for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          output.writeInt(matrix[i][j].getNumeratorDegree());
          output.writeInt(matrix[i][j].getDenominatorDegree());
          matrix[i][j].getNumerator().writeMxFormatWithoutHeader(output, true);
          matrix[i][j].getDenominator().writeMxFormatWithoutHeader(output, true);
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
  public static DoubleRationalPolynomial[][] readMxFormat(final InputStream input, final MxDataHead head) throws IOException {
    final int rowSize = head.getRowSize();
    final int columnSize = head.getColumnSize();
    final int matrixType = head.getMatrixType();
    //final int version = head.getVersion();

    final DataInputStream is = new DataInputStream(input);

    if (matrixType == MxDataHead.REAL_RATIONAL_POLYNOMIAL_MATRIX) {
      final DoubleRationalPolynomial[][] ans = new DoubleRationalPolynomial[rowSize][columnSize];

      if (head.isSameEndian()) {
        for (int i = 0; i < rowSize; i++) {
          for (int j = 0; j < columnSize; j++) {
            final int nDegree = is.readInt();
            final int dDegree = is.readInt();
            final double[] nCoef = new double[nDegree + 1];
            for (int k = 0; k <= nDegree; k++) {
              nCoef[k] = is.readDouble();
            }
            final double[] dCoef = new double[dDegree + 1];
            for (int k = 0; k <= dDegree; k++) {
              dCoef[k] = is.readDouble();
            }

            ans[i][j] = new DoubleRationalPolynomial(new DoublePolynomial(nCoef, "s"), new DoublePolynomial(dCoef, "s")); //$NON-NLS-1$ //$NON-NLS-2$
          }
        }
      } else {
        for (int i = 0; i < rowSize; i++) {
          for (int j = 0; j < columnSize; j++) {
            final int nDegree = EndianTransformer.flip(is.readInt());
            final int dDegree = EndianTransformer.flip(is.readInt());
            final double[] nCoef = new double[nDegree + 1];
            for (int k = 0; k <= nDegree; k++) {
              nCoef[k] = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
            }
            final double[] dCoef = new double[dDegree + 1];
            for (int k = 0; k <= dDegree; k++) {
              dCoef[k] = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
            }
            ans[i][j] = new DoubleRationalPolynomial(new DoublePolynomial(nCoef, "s"), new DoublePolynomial(dCoef, "s")); //$NON-NLS-1$ //$NON-NLS-2$
          }
        }
      }

      return ans;
    }
    
    throw new UnsupportedOperationException();

//    // ComplexRationalPolynomialMatrix
//    // assert matrixType == 3;
//
//    final RationalPolynomial[][] ans = new RationalPolynomial[rowSize][columnSize];
//
//    if (head.isSameEndian()) {
//      for (int i = 0; i < rowSize; i++) {
//        for (int j = 0; j < columnSize; j++) {
//          final int nDegree = is.readInt();
//          final int dDegree = is.readInt();
//          final DoubleComplexNumber[] nCoef = new DoubleComplexNumber[nDegree + 1];
//          for (int k = 0; k <= nDegree; k++) {
//            final double real = is.readDouble();
//            final double imag = is.readDouble();
//            nCoef[k] = new DoubleComplexNumber(real, imag);
//          }
//          final DoubleComplexNumber[] dCoef = new DoubleComplexNumber[dDegree + 1];
//          for (int k = 0; k <= dDegree; k++) {
//            final double real = is.readDouble();
//            final double imag = is.readDouble();
//            dCoef[k] = new DoubleComplexNumber(real, imag);
//          }
//          ans[i][j] = new RationalPolynomial(new Polynomial(nCoef, "s"), new Polynomial(dCoef, "s")); //$NON-NLS-1$ //$NON-NLS-2$
//        }
//      }
//    } else {
//      for (int i = 0; i < rowSize; i++) {
//        for (int j = 0; j < columnSize; j++) {
//          final int nDegree = EndianTransformer.flip(is.readInt());
//          final int dDegree = EndianTransformer.flip(is.readInt());
//          final DoubleComplexNumber[] nCoef = new DoubleComplexNumber[nDegree + 1];
//          for (int k = 0; k <= nDegree; k++) {
//            final double real = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
//            final double imag = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
//            nCoef[k] = new DoubleComplexNumber(real, imag);
//          }
//          final DoubleComplexNumber[] dCoef = new DoubleComplexNumber[dDegree + 1];
//          for (int k = 0; k <= dDegree; k++) {
//            final double real = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
//            final double imag = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
//            dCoef[k] = new DoubleComplexNumber(real, imag);
//          }
//          ans[i][j] = new RationalPolynomial(new Polynomial(nCoef, "s"), new Polynomial(dCoef, "s")); //$NON-NLS-1$ //$NON-NLS-2$
//        }
//      }
//
//    }
//
//    return ans;
  }
}
