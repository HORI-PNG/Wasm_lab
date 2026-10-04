/*
 * $Id: RationalPolynomialMatrixUtil.java,v 1.80 2008/07/15 15:27:15 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.matrix;

import java.io.PrintWriter;
import java.io.Writer;

import org.mklab.nfc.scalar.NumericalScalar;
import org.mklab.nfc.scalar.Polynomial;
import org.mklab.nfc.scalar.RationalPolynomial;


/**
 * 有理多項式行列に関するユーティリティクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.80 $, 2004/07/02
 */

public final class RationalPolynomialMatrixUtil {

  /**
   * 新しく生成された<code>RationalPolynomialMatrixUtil</code>オブジェクトを初期化します。
   */
  private RationalPolynomialMatrixUtil() {
    // nothing to do
  }

  //  /**
  //   * 実行列の各成分を定数項とする有理多項式を成分とする有理多項式行列に変換します。
  //   * @param <CS> 係数スカラーの型
  //   * @param <CM> 係数行列の型
  //   * 
  //   * @param matrix 実行列
  //   * @return 実行列の各成分を定数項とする有理多項式を成分とする有理多項式行列
  //   */
  //  public static<S extends RationalPolynomial<S,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends NumericalMatrix<CS,CM>> S[][] createArray(final double[][] matrix) {
  //    final int rowSize = matrix.length;
  //    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //    final S[][] ans = (S[][])new RationalPolynomial[rowSize][columnSize];
  //
  //    for (int i = 0; i < rowSize; i++) {
  //      final S[] ansi = ans[i];
  //      final double[] matrixi = matrix[i];
  //      for (int j = 0; j < columnSize; j++) {
  //        ansi[j] = (S)new RationalPolynomial<>(matrixi[j]);
  //      }
  //    }
  //    return ans;
  //  }

  //  /**
  //   * 行列の各成分を定数項とする有理多項式を成分とする有理多項式行列に変換します。
  //   * @param <CS> 係数スカラーの型
  //   * @param <CM> 係数行列の型
  //   * 
  //   * @param matrix 行列
  //   * @return 行列の各成分を定数項とする有理多項式を成分とする有理多項式行列
  //   */
  //  public static <S extends RationalPolynomial<S,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends NumericalMatrix<CS,CM>> S[][] createArray(final CS[][] matrix) {
  //    final int rowSize = matrix.length;
  //    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //
  //    final S[][] ans = (S[][])new RationalPolynomial[rowSize][columnSize];
  //    for (int i = 0; i < rowSize; i++) {
  //      for (int j = 0; j < columnSize; j++) {
  //        ans[i][j] = (S)new RationalPolynomial<>(matrix[i][j]);
  //      }
  //    }
  //    return ans;
  //  }

  //  /**
  //   * 整数行列を有理多項式行列(元の行列を分子の定数項とする)に変換します。
  //   * @param <CS> 係数スカラーの型
  //   * @param <CM> 係数行列の型
  //   * 
  //   * @param matrix 整数行列
  //   * @return 有理多項式行列
  //   */
  //  public static <S extends RationalPolynomial<S,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends NumericalMatrix<CS,CM>> S[][] createArray(final int[][] matrix) {
  //    final int rowSize = matrix.length;
  //    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //
  //    final S[][] ans = (S[][])new RationalPolynomial[rowSize][columnSize];
  //    for (int i = 0; i < rowSize; i++) {
  //      final int[] matrixi = matrix[i];
  //      final S[] ansi = ans[i];
  //      for (int j = 0; j < columnSize; j++) {
  //        ansi[j] = (S)new RationalPolynomial<>(matrixi[j]);
  //      }
  //    }
  //    return ans;
  //  }

  //  /**
  //   * 多項式行列を有理多項式行列(元の行列を分子の定数項とする)に変換します。
  //   * @param <CS> 係数スカラーの型
  //   * @param <CM> 係数行列の型
  //   * 
  //   * @param matrix 多項式行列
  //   * @return 有理多項式行列
  //   */
  //  public static <S extends RationalPolynomial<S,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends NumericalMatrix<CS,CM>> S[][] createArray(final Polynomial<CS,CM>[][] matrix) {
  //    final int rowSize = matrix.length;
  //    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //
  //    final S[][] ans = new RationalPolynomial[rowSize][columnSize];
  //    for (int i = 0; i < rowSize; i++) {
  //      final S[] ansi = ans[i];
  //      final Polynomial<CS,CM>[] matrixi = matrix[i];
  //      for (int j = 0; j < columnSize; j++) {
  //        ansi[j] = new RationalPolynomial<>(matrixi[j]);
  //      }
  //    }
  //    return ans;
  //  }

  //  /**
  //   * 倍精度有理多項式行列を有理多項式行列に変換します。
  //   * @param <CS> 係数スカラーの型
  //   * @param <CM> 係数行列の型
  //   * 
  //   * @param matrix 倍精度有理多項式行列
  //   * @return 有理多項式行列
  //   */
  //  public static <S extends RationalPolynomial<S,M,P,PM,CS,CM>, P extends Polynomial<P,PM,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends NumericalMatrix<CS,CM>> S[][] createArray(final S[][] matrix) {
  //    final int rowSize = matrix.length;
  //    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //
  //    final S[][] ans = (S[][])new RationalPolynomial[rowSize][columnSize];
  //    for (int i = 0; i < rowSize; i++) {
  //      final S[] ansi = ans[i];
  //      final S[] matrixi = matrix[i];
  //      for (int j = 0; j < columnSize; j++) {
  //        final P num = matrixi[j].getNumerator();
  //        final P den = matrixi[j].getDenominator();
  //        ansi[j] = new RationalPolynomial<>(num, den);
  //      }
  //    }
  //    return ans;
  //  }

  //  /**
  //   * 倍精度有理多項式行列を有理多項式行列に変換します。
  //   * @param <CS> 係数スカラーの型
  //   * @param <CM> 係数行列の型
  //   * 
  //   * @param matrix 倍精度有理多項式行列
  //   * @return 有理多項式行列
  //   */
  //  public static <S extends RationalPolynomial<S,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends NumericalMatrix<CS,CM>> S[] createArray(final S[] matrix) {
  //    final int rowSize = matrix.length;
  //
  //    final S[] ans = (S[])new RationalPolynomial[rowSize];
  //        
  //    for (int i = 0; i < rowSize; i++) {
  //      final Polynomial<CS,CM> num = new Polynomial<>(matrix[i].getNumerator().getCoefficients());
  //      final Polynomial<CS,CM> den = new Polynomial<>(matrix[i].getDenominator().getCoefficients());
  //      ans[i] = new RationalPolynomial<>(num, den);
  //    }
  //    return ans;
  //  }

  //  /**
  //   * 単位行列を生成します。
  //   * @param <CS> 係数スカラーの型
  //   * @param <CM> 係数行列の型
  //   * 
  //   * @param rowSize 行の数
  //   * @param columnSize 列の数
  //   * @param variableName 変数名
  //   * @return 単位行列
  //   */
  //  public static <S extends RationalPolynomial<S,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends NumericalMatrix<CS,CM>> S[][] unit(final int rowSize, final int columnSize, final String variableName) {
  //    final S[][] ans = (S[][])new RationalPolynomial[rowSize][columnSize];
  //    for (int i = 0; i < rowSize; i++) {
  //      for (int j = 0; j < columnSize; j++) {
  //        if (i == j) {
  //          ans[i][j] = (S)new RationalPolynomial<>(1.0, variableName);
  //        } else {
  //          ans[i][j] = (S)new RationalPolynomial<>(0.0, variableName);
  //        }
  //      }
  //    }
  //    return ans;
  //  }

  //  /**
  //   * 全ての成分が1である行列を生成します。
  //   * @param <CS> 係数スカラーの型
  //   * @param <CM> 係数行列の型
  //   * 
  //   * @param rowSize 行の数
  //   * @param columnSize 列の数
  //   * @param variableName 変数名
  //   * @return 全ての成分が1である行列
  //   */
  //  public static <S extends RationalPolynomial<S,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends NumericalMatrix<CS,CM>> S[][] ones(final int rowSize, final int columnSize, final String variableName) {
  //    final S[][] ans = (S[][])new RationalPolynomial[rowSize][columnSize];
  //    for (int i = 0; i < rowSize; i++) {
  //      for (int j = 0; j < columnSize; j++) {
  //        ans[i][j] = (S)new RationalPolynomial<>(1.0, variableName);
  //      }
  //    }
  //    return ans;
  //  }

  /**
   * 成分毎の分子多項式を成分とする行列を{@link Polynomial}の2次元配列で返します。
   * 
   * @param <PS> 多項式の型
   * @param <PM> 多項式行列の型
   * @param <RS> 有理多項式の型
   * @param <RM> 有理多項式行列の型
   * @param <ES> 係数スカラーの型
   * @param <EM> 係数行列の型
   * 
   * @param matrix 対象となる行列
   * @return 成分毎の分子多項式を成分とする行列({@link Polynomial}の2次元配列)
   */
  public static <PS extends Polynomial<PS, PM, RS, RM, ES, EM>, PM extends PolynomialMatrix<PS, PM, RS, RM, ES, EM>, RS extends RationalPolynomial<PS, PM, RS, RM, ES, EM>, RM extends RationalPolynomialMatrix<PS, PM, RS, RM, ES, EM>, ES extends NumericalScalar<ES, EM>, EM extends NumericalMatrix<ES, EM>> PS[][] getNumeratorElementWise(
      final RS[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final PS polynomial = matrix[0][0].getNumerator();

    final PS[][] ans = polynomial.createArray(rowSize, columnSize);
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
   * @param <PS> 多項式の型
   * @param <PM> 多項式行列
   * @param <RS> 有理多項式の型
   * @param <RM> 有理多項式行列の型
   * @param <ES> 係数スカラーの型
   * @param <EM> 係数行列の型
   * 
   * @param matrix 対象となる行列
   * @return 成分毎の分母多項式を成分とする行列({@link Polynomial}の2次元配列)
   */
  public static <PS extends Polynomial<PS, PM, RS, RM, ES, EM>, PM extends PolynomialMatrix<PS, PM, RS, RM, ES, EM>, RS extends RationalPolynomial<PS, PM, RS, RM, ES, EM>, RM extends RationalPolynomialMatrix<PS, PM, RS, RM, ES, EM>, ES extends NumericalScalar<ES, EM>, EM extends NumericalMatrix<ES, EM>> PS[][] getDenominatorElementWise(
      final RS[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final PS polynomial = matrix[0][0].getDenominator();
    final PS[][] ans = polynomial.createArray(rowSize, columnSize);
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
   * @param <PS> 多項式の型
   * @param <PM> 多項式行列の型
   * @param <RS> 有理多項式の型
   * @param <RM> 有理多項式行列の型
   * @param <ES> 係数スカラーの型
   * @param <EM> 係数行列の型
   * 
   * @param matrix 対象となる行列
   * @return 成分毎の商多項式を成分とする行列({@link Polynomial}の2次元配列)
   */
  public static <PS extends Polynomial<PS, PM, RS, RM, ES, EM>, PM extends PolynomialMatrix<PS, PM, RS, RM, ES, EM>, RS extends RationalPolynomial<PS, PM, RS, RM, ES, EM>, RM extends RationalPolynomialMatrix<PS, PM, RS, RM, ES, EM>, ES extends NumericalScalar<ES, EM>, EM extends NumericalMatrix<ES, EM>> PS[][] getQuotientElementWise(
      final RS[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final PS polynomial = matrix[0][0].getNumerator();
    final PS[][] ans = polynomial.createArray(rowSize, columnSize);
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
   * @param <PS> 多項式の型
   * @param <PM> 多項式行列の型
   * @param <RS> 有理多項式の型
   * @param <RM> 有理多項式行列の型
   * @param <EM> 係数行列の型
   * @param <ES> 係数スカラーの型
   * 
   * @param matrix 対象となる行列
   * @return 成分毎の剰余多項式を成分とする行列({@link Polynomial}の2次元配列)
   */
  public static <PS extends Polynomial<PS, PM, RS, RM, ES, EM>, PM extends PolynomialMatrix<PS, PM, RS, RM, ES, EM>, RS extends RationalPolynomial<PS, PM, RS, RM, ES, EM>, RM extends RationalPolynomialMatrix<PS, PM, RS, RM, ES, EM>, ES extends NumericalScalar<ES, EM>, EM extends NumericalMatrix<ES, EM>> PS[][] getRemainderElementWise(
      final RS[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final PS polynomial = matrix[0][0].getNumerator();
    final PS[][] ans = polynomial.createArray(rowSize, columnSize);
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].getRemainder();
      }
    }
    return ans;
  }

  /**
   * 複数個の有理多項式をプリントライターに出力します。
   * 
   * @param <PS> 多項式スカラーの型
   * @param <PM> 多項式行列の型
   * @param <RS> 有理多項式の型
   * @param <RM> 有理多項式行列の型
   * @param <ES> 係数スカラーの型
   * @param <EM> 係数行列の型
   * 
   * @param elements 有理多項式の配列
   * @param row 行番号
   * @param output 出力先のプリントライター
   * @param printingColumnLengthes 出力する列の長さ
   * @param coefficientFormat 係数の出力フォーマット
   */
  private static <PS extends Polynomial<PS, PM, RS, RM, ES, EM>, PM extends PolynomialMatrix<PS, PM, RS, RM, ES, EM>, RS extends RationalPolynomial<PS, PM, RS, RM, ES, EM>, RM extends RationalPolynomialMatrix<PS, PM, RS, RM, ES, EM>, ES extends NumericalScalar<ES, EM>, EM extends NumericalMatrix<ES, EM>> void printElement(
      final RS[] elements, final int row, final PrintWriter output, final int[] printingColumnLengthes, final String coefficientFormat) {
    final PS polynomial = elements[0].getNumerator();

    final PS[] numerators = polynomial.createArray(elements.length);
    for (int i = 0; i < elements.length; i++) {
      numerators[i] = elements[i].getNumerator();
    }

    final PS[] denominators = polynomial.createArray(elements.length);
    for (int i = 0; i < elements.length; i++) {
      denominators[i] = elements[i].getDenominator();
    }

    RationalPolynomialMatrixUtil.outputSpaces(GridFormat.LEFT_MARGIN, output);
    PolynomialMatrixUtil.printElement(numerators, output, printingColumnLengthes, coefficientFormat, true);

    RationalPolynomialMatrixUtil.printRowNumber(row, output);

    for (int column = 0; column < elements.length; column++) {
      RationalPolynomialMatrixUtil.outputSpaces(1, output);
      RationalPolynomialMatrixUtil.outputLines(printingColumnLengthes[column], output);
      if (column != elements.length - 1) {
        RationalPolynomialMatrixUtil.outputSpaces(GridFormat.COLUMN_SEPARATION, output);
      }
    }

    output.println();

    RationalPolynomialMatrixUtil.outputSpaces(GridFormat.LEFT_MARGIN, output);
    PolynomialMatrixUtil.printElement(denominators, output, printingColumnLengthes, coefficientFormat, false);
  }

  /**
   * 有理多項式行列の各列の出力文字列の長さの配列を返します。
   * 
   * @param <PS> 多項式スカラーの型
   * @param <PM> 多項式行列の型
   * @param <RS> 有理多項式の型
   * @param <RM> 有理多項式行列の型
   * @param <ES> 係数スカラーの型
   * @param <EM> 係数行列の型
   * 
   * @param elements 行列
   * @param coefficientFormat 多項式の係数フォーマット
   * @return 有理多項式行列の各列の出力文字列の長さの配列
   */
  private static <PS extends Polynomial<PS, PM, RS, RM, ES, EM>, PM extends PolynomialMatrix<PS, PM, RS, RM, ES, EM>, RS extends RationalPolynomial<PS, PM, RS, RM, ES, EM>, RM extends RationalPolynomialMatrix<PS, PM, RS, RM, ES, EM>, ES extends NumericalScalar<ES, EM>, EM extends NumericalMatrix<ES, EM>> int[] getPrintingColumnLengthes(
      final RS[][] elements, final String coefficientFormat) {
    final int rowSize = elements.length;
    final int columnSize = rowSize == 0 ? 0 : elements[0].length;
    final int[] printingColumnLengthes = new int[columnSize];

    final PS polynomial = elements[0][0].getNumerator();

    for (int column = 0; column < columnSize; column++) {
      final PS[] numerators = polynomial.createArray(rowSize);
      for (int row = 0; row < rowSize; row++) {
        numerators[row] = elements[row][column].getNumerator();
      }

      final PS[] denominators = polynomial.createArray(rowSize);
      for (int row = 0; row < rowSize; row++) {
        denominators[row] = elements[row][column].getDenominator();
      }

      final int maxNumeratorLength = PolynomialMatrixUtil.getMaxLength(numerators, coefficientFormat);
      final int maxDenominatorLength = PolynomialMatrixUtil.getMaxLength(denominators, coefficientFormat);
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
   * @param <PS> 多項式スカラーの型
   * @param <PM> 多項式行列の型
   * @param <RS> 有理多項式の型
   * @param <RM> 有理多項式行列の型
   * @param <ES> 係数スカラーの型
   * @param <EM> 係数行列の型
   * 
   * @param matrix 対象となる行列
   * @param output ライター
   * @param coefficientFormat 係数の出力フォーマット
   * @param maxColumnSize 最大列数
   */
  public static <PS extends Polynomial<PS, PM, RS, RM, ES, EM>, PM extends PolynomialMatrix<PS, PM, RS, RM, ES, EM>, RS extends RationalPolynomial<PS, PM, RS, RM, ES, EM>, RM extends RationalPolynomialMatrix<PS, PM, RS, RM, ES, EM>, ES extends NumericalScalar<ES, EM>, EM extends NumericalMatrix<ES, EM>> void print(
      final RS[][] matrix, final Writer output, final String coefficientFormat, final int maxColumnSize) {
    final PrintWriter writer = new PrintWriter(output);
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    int remain = columnSize;
    int columnOffset = 0;

    final int[] printingColumnLengthes = getPrintingColumnLengthes(matrix, coefficientFormat);

    while (remain > 0) {
      final int printingColumnSize = Math.min(maxColumnSize, remain);
      PolynomialMatrixUtil.printColumnNumbers(columnOffset, printingColumnSize, printingColumnLengthes, writer);

      for (int i = 0; i < rowSize; i++) {
        final RS[] elements = matrix[0][0].createArray(printingColumnSize);
        for (int j = 0; j < printingColumnSize; j++) {
          elements[j] = matrix[i][columnOffset + j];
        }

        RationalPolynomialMatrixUtil.printElement(elements, i, writer, printingColumnLengthes, coefficientFormat);

        if (i != rowSize - 1) {
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

}
