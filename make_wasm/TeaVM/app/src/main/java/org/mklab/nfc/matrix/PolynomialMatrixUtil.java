/*
 * $Id: PolynomialMatrixUtil.java,v 1.79 2008/07/15 15:27:15 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.matrix;

import java.io.PrintWriter;

import org.mklab.nfc.scalar.NumericalScalar;
import org.mklab.nfc.scalar.Polynomial;
import org.mklab.nfc.scalar.RationalPolynomial;
import org.mklab.nfc.util.PolynomialTokenizer;


/**
 * 多項式行列に関するユーティリティクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.79 $, 2004/07/02
 */

public final class PolynomialMatrixUtil {

  /**
   * 新しく生成された<code>PolynomialMatrixUtil</code>オブジェクトを初期化します。
   */
  private PolynomialMatrixUtil() {
    // nothing to do
  }

  //  /**
  //   * 実行列の各成分を定数項とする多項式を成分とする多項式行列に変換します。
  //   * @param <CS> 係数スカラーの型
  //   * @param <CM> 係数行列の型
  //   * 
  //   * @param matrix 実行列
  //   * @return 実行列の各成分を定数項とする多項式を成分とする多項式行列
  //   */
  //  public static <S extends Polynomial<S,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends NumericalMatrix<CS,CM>> S[] createArray(final double[] matrix) {
  //    final int size = matrix.length;
  //    final S[] ans = (S[])new Polynomial[size];
  //
  //    for (int i = 0; i < size; i++) {
  //      ans[i] = (S)new Polynomial<>(matrix[i]);
  //    }
  //    return ans;
  //  }

  //  /**
  //   * 実行列の各成分を定数項とする多項式を成分とする多項式行列に変換します。
  //   * @param <CS> 係数スカラーの型
  //   * @param <CM> 係数行列の型
  //   * 
  //   * @param matrix 実行列
  //   * @return 実行列の各成分を定数項とする多項式を成分とする多項式行列
  //   */
  //  public static <S extends Polynomial<S,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends NumericalMatrix<CS,CM>> S[][] createArray(final double[][] matrix) {
  //    final int rowSize = matrix.length;
  //    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //    final S[][] ans = (S[][])new Polynomial[rowSize][columnSize];
  //
  //    for (int i = 0; i < rowSize; i++) {
  //      final S[] ansi = ans[i];
  //      final double[] matrixi = matrix[i];
  //      for (int j = 0; j < columnSize; j++) {
  //        ansi[j] = (S)new Polynomial<>(matrixi[j]);
  //      }
  //    }
  //    return ans;
  //  }

  //  /**
  //   * 行列の各成分を定数項とする多項式を成分とする多項式行列に変換します。
  //   * @param <CS> 係数スカラーの型
  //   * @param <CM> 係数行列の型
  //   * 
  //   * @param matrix 行列
  //   * @return 行列の各成分を定数項とする多項式を成分とする多項式行列
  //   */
  //  public static <S extends Polynomial<S,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends NumericalMatrix<CS,CM>> S[][] createArray(final CS[][] matrix) {
  //    final int rowSize = matrix.length;
  //    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //
  //    final S[][] ans = (S[][])new Polynomial[rowSize][columnSize];
  //    for (int i = 0; i < rowSize; i++) {
  //      for (int j = 0; j < columnSize; j++) {
  //        ans[i][j] = (S)new Polynomial<>(matrix[i][j]);
  //      }
  //    }
  //    return ans;
  //  }

  //  /**
  //   * 整数行列を多項式行列(元の行列を定数項とする)に変換します。
  //   * @param <CM> 係数行列の型
  //   * @param <CS> 係数スカラーの型
  //   * 
  //   * @param matrix 整数行列
  //   * @return 多項式行列
  //   */
  //  public static <S extends Polynomial<S,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends NumericalMatrix<CS,CM>> S[] createArray(final int[] matrix) {
  //    final int rowSize = matrix.length;
  //    
  //    final S[] ans = (S[])new Polynomial[rowSize];
  //    for (int i = 0; i < rowSize; i++) {
  //      ans[i] = (S)new Polynomial<>(matrix[i]);
  //    }
  //    return ans;
  //  }

  //  /**
  //   * 整数行列を多項式行列(元の行列を定数項とする)に変換します。
  //   * @param <CM> 係数行列の型
  //   * @param <CS> 係数スカラーの型
  //   * 
  //   * @param matrix 整数行列
  //   * @return 多項式行列
  //   */
  //  public static <S extends Polynomial<S,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends NumericalMatrix<CS,CM>> S[][] createArray(final int[][] matrix) {
  //    final int rowSize = matrix.length;
  //    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //
  //    final S[][] ans = (S[][])new Polynomial[rowSize][columnSize];
  //    for (int i = 0; i < rowSize; i++) {
  //      final int[] matrixi = matrix[i];
  //      final S[] ansi = ans[i];
  //      for (int j = 0; j < columnSize; j++) {
  //        ansi[j] = (S)new Polynomial<>(matrixi[j]);
  //      }
  //    }
  //    return ans;
  //  }

  //  /**
  //   * 倍精度多項式行列を多項式行列(元の行列を定数項とする)に変換します。
  //   * @param <CM> 係数行列の型
  //   * @param <CS> 係数スカラーの型
  //   * 
  //   * @param matrix 倍精度多項式行列
  //   * @return 多項式行列
  //   */
  //  public static <S extends Polynomial<S,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends NumericalMatrix<CS,CM>> S[][] createArray(final S[][] matrix) {
  //    final int rowSize = matrix.length;
  //    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  //
  //    final S[][] ans = (S[][])new Polynomial[rowSize][columnSize];
  //    for (int i = 0; i < rowSize; i++) {
  //      final S[] matrixi = matrix[i];
  //      final S[] ansi = ans[i];
  //      for (int j = 0; j < columnSize; j++) {
  //        ansi[j] = (S)new Polynomial<>(matrixi[j].getCoefficients());
  //      }
  //    }
  //    return ans;
  //  }

  

  //  /**
  //   * 倍精度多項式行列を多項式行列(元の行列を定数項とする)に変換します。
  //   * @param <CS> 係数スカラーの型
  //   * @param <CM> 係数行列の型
  //   * 
  //   * @param matrix 倍精度多項式行列
  //   * @return 多項式行列
  //   */
  //  public static <S extends Polynomial<S,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends NumericalMatrix<CS,CM>> S[] createArray(final S[] matrix) {
  //    final int size = matrix.length;
  //
  //    final S[] ans = (S[])new Polynomial[size];
  //    for (int i = 0; i < size; i++) {
  //      ans[i] = (S)new Polynomial<>(matrix[i].getCoefficients());
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
  //  public static <S extends Polynomial<S,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends NumericalMatrix<CS,CM>> S[][] unit(final int rowSize, final int columnSize, final String variableName) {
  //    final S[][] ans = (S[][])new Polynomial[rowSize][columnSize];
  //    for (int i = 0; i < rowSize; i++) {
  //      for (int j = 0; j < columnSize; j++) {
  //        if (i == j) {
  //          ans[i][j] = (S)new Polynomial<>(1.0, variableName);
  //        } else {
  //          ans[i][j] = (S)new Polynomial<>(0.0, variableName);
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
  //  public static <S extends Polynomial<S,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends NumericalMatrix<CS,CM>> S[][] ones(final int rowSize, final int columnSize, final String variableName) {
  //    final S[][] matrix = (S[][])new Polynomial[rowSize][columnSize];
  //    for (int i = 0; i < rowSize; i++) {
  //      for (int j = 0; j < columnSize; j++) {
  //        matrix[i][j] = (S)new Polynomial<>(1.0, variableName);
  //      }
  //    }
  //    return matrix;
  //  }



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
   * @param <PS> 多項式スカラーの型
   * @param <PM> 多項式行列の型
   * @param <RS> 有理多項式の型
   * @param <RM> 有理多項式行列の型
   * @param <ES> 係数スカラーの型
   * @param <EM> 係数行列の型
   * 
   * @param elements 多項式の配列
   * @param output 出力先のプリントライター
   * @param printingColumnLengthes 多項式の出力文字列の長さ
   * @param coefficientFormat 係数の出力フォーマット
   * @param bottomAlign 表示が数行になる場合に下に揃えるならばtrue、上に揃えるならばfalse
   */
  static <PS extends Polynomial<PS,PM,RS,RM,ES,EM>, PM extends PolynomialMatrix<PS,PM,RS,RM,ES,EM>, RS extends RationalPolynomial<PS,PM,RS,RM,ES,EM>, RM extends RationalPolynomialMatrix<PS,PM,RS,RM,ES,EM>, ES extends NumericalScalar<ES,EM>,EM extends NumericalMatrix<ES,EM>> void printElement(final PS[] elements, final PrintWriter output,
      final int[] printingColumnLengthes, final String coefficientFormat, final boolean bottomAlign) {
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
        PolynomialMatrixUtil.outputSpaces(leftMargin, output);
      }

      for (int column = 0; column < stringLines.length; column++) {
        final int printingColumnLength = printingColumnLengthes[column];
        final String[] lines = stringLines[column];

        if (bottomAlign) {
          if (i < maxLines - lines.length) {
            PolynomialMatrixUtil.outputSpaces(printingColumnLength, output);
          } else {
            final int k = i - (maxLines - lines.length);

            int length = lines[k].length();
            if (lines.length == 1) {
              final int margin = (printingColumnLength - length) / 2;
              PolynomialMatrixUtil.outputSpaces(margin, output);
              length += margin;
            }

            output.print(lines[k]);
            PolynomialMatrixUtil.outputSpaces(1, output);
            PolynomialMatrixUtil.outputSpaces((printingColumnLength - length), output);
          }
        } else {
          if (lines.length <= i) {
            PolynomialMatrixUtil.outputSpaces(printingColumnLength, output);
          } else {
            int length = lines[i].length();
            if (lines.length == 1) {
              final int margin = (printingColumnLength - length) / 2;
              PolynomialMatrixUtil.outputSpaces(margin, output);
              length += margin;
            }

            output.print(lines[i]);
            PolynomialMatrixUtil.outputSpaces(1, output);
            PolynomialMatrixUtil.outputSpaces((printingColumnLength - length), output);
          }
        }

        if (column != stringLines.length - 1) {
          PolynomialMatrixUtil.outputSpaces(columnSeparation, output);
        }
      }
      output.println();
    }
  }

  /**
   * 多項式の出力文字列の最大値を返します。
   * 
   * @param <PS> 多項式スカラーの型
   * @param <PM> 多項式行列の型
   * @param <RS> 有理多項式の型
   * @param <RM> 有理多項式行列の型
   * @param <ES> 係数スカラーの型
   * @param <EM> 係数行列の型
   * 
   * @param polynomials 多項式の配列
   * @param coefficientFormat 係数の出力フォーマット
   * @return 多項式の出力文字列の最大値
   */
  static <PS extends Polynomial<PS,PM,RS,RM,ES,EM>, PM extends PolynomialMatrix<PS,PM,RS,RM,ES,EM>, RS extends RationalPolynomial<PS,PM,RS,RM,ES,EM>, RM extends RationalPolynomialMatrix<PS,PM,RS,RM,ES,EM>, ES extends NumericalScalar<ES,EM>,EM extends NumericalMatrix<ES,EM>>int getMaxLength(final PS[] polynomials, final String coefficientFormat) {
    int maxLength = 0;
    for (final PS polynomial : polynomials) {
      final int length = polynomial.toString(coefficientFormat).length();
      maxLength = Math.max(maxLength, length);
    }

    return maxLength;
  }

//  /**
//   * 多項式の出力文字列の最大値を返します。
//   * 
//   * @param polynomials 多項式の配列
//   * @param coefficientFormat 係数の出力フォーマット
//   * @return 多項式の出力文字列の最大値
//   */
//  static int getMaxLength(final DoublePolynomial[] polynomials, final String coefficientFormat) {
//    int maxLength = 0;
//    for (final DoublePolynomial polynomial : polynomials) {
//      final int length = polynomial.toString(coefficientFormat).length();
//      maxLength = Math.max(maxLength, length);
//    }
//
//    return maxLength;
//  }

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
   * @param <PS> 多項式スカラーの型
   * @param <PM> 多項式行列の型
   * @param <RS> 有理多項式の型
   * @param <RM> 有理多項式行列の型
   * @param <ES> 係数スカラーの型
   * @param <EM> 係数行列の型
   * 
   * @param matrix 対象となる行列
   * @param order 階数
   * @return 多項式行列の不定積分
   */
  public static <PS extends Polynomial<PS,PM,RS,RM,ES,EM>, PM extends PolynomialMatrix<PS,PM,RS,RM,ES,EM>, RS extends RationalPolynomial<PS,PM,RS,RM,ES,EM>, RM extends RationalPolynomialMatrix<PS,PM,RS,RM,ES,EM>, ES extends NumericalScalar<ES,EM>,EM extends NumericalMatrix<ES,EM>> PS[][] integral(final PS[][] matrix, final int order) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final PS[][] ans = GridUtil.<PS> createArray(rowSize, columnSize, matrix);

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = (matrix[i][j]).integral(order);
      }
    }
    return ans;
  }

//  /**
//   * 対角行列を生成します。
//   * 
//   * @param <S> スカラーの型
//   * @param <M> 行列の型
//   * @param <EM> 係数行列の型
//   * @param <ES> 係数の型
//   * @param elements elements 
//   * @return 対角行列
//   */
//  public static <S extends Polynomial<S,M,ES,EM>, M extends PolynomialMatrix<S,M,ES,EM>, ES extends NumericalScalar<ES,EM>, EM extends BaseNumericalMatrix<ES,EM>> PolynomialMatrix<S,M,ES,EM> diagonal(final S[] elements) {
//    final S[][] polynomials = GridUtil.vectorToDiagonal(elements);
//    return new PolynomialMatrix<>(polynomials);
//  }
//  
//
//  /**
//   * 1個の多項式について、行列の各成分の累乗を求めます。
//   * @param <S> スカラーの型
//   * @param <M> 行列の型
//   * @param <EM> 係数行列の型 
//   * @param <ES> 係数の型
//   * @param polynomial 累乗の対象
//   * @param matrix 累乗の指数を成分とする行列
//   * @return 累乗の結果
//   */
//  public static <S extends Polynomial<S,M,ES,EM>, M extends PolynomialMatrix<S,M,ES,EM>, ES extends NumericalScalar<ES,EM>, EM extends BaseNumericalMatrix<ES,EM>> PolynomialMatrix<S,M,ES,EM> powerElementWise(final S polynomial, final IntMatrix matrix) {
//    return new PolynomialMatrix<>(BaseMatrixUtil.powerElementWise(polynomial, matrix.getIntElements()));
//  }
}
