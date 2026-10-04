/*
 * $Id: SymbolicMatrixUtil.java,v 1.8 2008/03/15 00:23:42 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.ComplexSymbolicScalar;
import org.mklab.nfc.scalar.NumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;
import org.mklab.nfc.scalar.RealSymbolicScalar;
import org.mklab.nfc.scalar.SymbolicScalar;


/**
 * {@link AbstractSymbolicMatrix}のユーティリティクラスです。
 * 
 * @author koga
 * @version $Revision: 1.8 $
 */
public final class AbstractSymbolicMatrixUtil {

  /**
   * 新しく生成された<code>BaseSymbolicMatrixUtil</code>オブジェクトを初期化します。
   */
  private AbstractSymbolicMatrixUtil() {
    // nothing to do
  }

  /**
   * 各成分の<code>order</code>階導関数を成分とする行列を求めます。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param <ES> 係数スカラーの型
   * @param <EM> 係数行列の型
   * 
   * @param matrix 対象となる行列
   * @param order 階数
   * @return 各成分の<code>order</code>階導関数を成分とする行列
   */
  public static <S extends SymbolicScalar<S, M, ES, EM>, M extends SymbolicMatrix<S, M, ES, EM>, ES extends NumericalScalar<ES, EM>, EM extends NumericalMatrix<ES, EM>> S[][] derivative(
      final S[][] matrix, final int order) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final S[][] ans = GridUtil.<S> createArray(rowSize, columnSize, matrix);

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].derivative(order);
      }
    }
    return ans;
  }

  /**
   * 各成分の係数を低次方向に<code>count</code>回シフトした式を成分とする行列を求めます。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param <ES> 係数スカラーの型
   * @param <EM> 係数行列の型
   * 
   * @param matrix 対象となる行列
   * @param count シフトの数
   * @return 各成分の係数を低次方向に<code>count</code>回シフトした式を成分とする行列
   */
  public static <S extends SymbolicScalar<S, M, ES, EM>, M extends SymbolicMatrix<S, M, ES, EM>, ES extends NumericalScalar<ES, EM>, EM extends NumericalMatrix<ES, EM>> S[][] shiftLower(
      final S[][] matrix, final int count) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final S[][] ans = GridUtil.<S> createArray(rowSize, columnSize, matrix);

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].shiftLower(count);
      }
    }
    return ans;
  }

  /**
   * 各成分の係数を高次方向に<code>count</code>回シフトした式を成分とする行列を求めます。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param <ES> 係数スカラーの型
   * @param <EM> 係数行列の型
   * 
   * @param matrix 対象となる行列
   * @param count シフトの数
   * @return 各成分の係数を高次方向に<code>count</code>回シフトした式を成分とする行列
   */
  public static <S extends SymbolicScalar<S, M, ES, EM>, M extends SymbolicMatrix<S, M, ES, EM>, ES extends NumericalScalar<ES, EM>, EM extends NumericalMatrix<ES, EM>> S[][] shiftHigher(
      final S[][] matrix, final int count) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    final S[][] ans = GridUtil.<S> createArray(rowSize, columnSize, matrix);

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].shiftHigher(count);
      }
    }
    return ans;
  }

  /**
   * 変数に値を代入して、評価します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param <ES> 係数スカラーの型
   * @param <EM> 係数行列の型
   * 
   * @param matrix 対象となる行列
   * @param value 変数に代入する値
   * @return 評価した結果
   */
  public static <S extends SymbolicScalar<S, M, ES, EM>, M extends SymbolicMatrix<S, M, ES, EM>, ES extends NumericalScalar<ES, EM>, EM extends NumericalMatrix<ES, EM>> ES[][] evaluate(
      final S[][] matrix, final int value) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    ES ans00 = matrix[0][0].evaluate(value);

    final ES[][] ans = ans00.createArray(rowSize, columnSize);
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].evaluate(value);
      }
    }
    return ans;
  }

  /**
   * 変数に値を代入して、評価します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param <ES> 係数スカラーの型
   * @param <EM> 係数行列の型
   * 
   * @param matrix 対象となる行列
   * @param value 変数に代入する値
   * @return 評価した結果
   */
  public static <S extends SymbolicScalar<S, M, ES, EM>, M extends SymbolicMatrix<S, M, ES, EM>, ES extends NumericalScalar<ES, EM>, EM extends NumericalMatrix<ES, EM>> ES[][] evaluate(
      final S[][] matrix, final double value) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    ES ans00 = matrix[0][0].evaluate(value);

    final ES[][] ans = ans00.createArray(rowSize, columnSize);
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].evaluate(value);
      }
    }
    return ans;
  }

  /**
   * 変数に値を代入して、評価します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param <ES> 係数スカラーの型
   * @param <EM> 係数行列の型
   * 
   * @param matrix 対象となる行列
   * @param value 変数に代入する値
   * @return 評価した結果
   */
  public static <S extends SymbolicScalar<S, M, ES, EM>, M extends SymbolicMatrix<S, M, ES, EM>, ES extends NumericalScalar<ES, EM>, EM extends NumericalMatrix<ES, EM>> ES[][] evaluate(
      final S[][] matrix, final ES value) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    ES ans00 = matrix[0][0].evaluate(value);

    final ES[][] ans = ans00.createArray(rowSize, columnSize);
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].evaluate(value);
      }
    }
    return ans;
  }

  /**
   * 変数に値を代入して、評価します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param <ES> 係数スカラーの型
   * @param <EM> 係数行列の型
   * 
   * @param matrix 対象となる行列
   * @param value 変数に代入する値
   * @return 評価した結果
   */
  public static <S extends SymbolicScalar<S, M, ES, EM>, M extends SymbolicMatrix<S, M, ES, EM>, ES extends NumericalScalar<ES, EM>, EM extends NumericalMatrix<ES, EM>> S[][] evaluate(
      final S[][] matrix, final S value) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    S ans11 = matrix[1][1].evaluate(value);
    final S[][] ans = ans11.createArray(rowSize, columnSize);
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].evaluate(value);
      }
    }
    return ans;
  }

  /**
   * 各成分の数式の変数を<code>variableName</code>で指定した文字列に設定します。
   * 
   * @param <S> スカラーの型
   * @param <M> 行列の型
   * @param <ES> 係数スカラーの型
   * @param <EM> 係数行列の型
   * 
   * @param matrix 対象となる行列
   * @param variableName 設定する式変数
   */
  public static <S extends SymbolicScalar<S, M, ES, EM>, M extends SymbolicMatrix<S, M, ES, EM>, ES extends NumericalScalar<ES, EM>, EM extends NumericalMatrix<ES, EM>> void setVariable(
      final S[][] matrix, final String variableName) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j].setVariable(variableName);
      }
    }
  }

  //  /**
  //   * 複素行列の成分を返します。
  //   * 
  //   * @param <S> スカラーの型
  //   * @param <M> 行列の型
  //   * @param <CS> 係数スカラーの型
  //   * @param <CM> 係数行列の型
  //   * 
  //   * @param rePart 実部
  //   * @param imPart 虚部
  //   * @return 複素行列の成分
  //   */
  //  public static <S extends SymbolicScalar<S, M, CS, CM>, M extends BaseSymbolicMatrix<S, M, CS, CM>, CS extends NumericalScalar<CS, CM>, CM extends BaseNumericalMatrix<CS, CM>> BaseComplexSymbolicScalar<S, M, CS, CM>[][] createComplexArray(
  //      final S[][] rePart, final S[][] imPart) {
  //    int rowSize = rePart.length;
  //    int columnSize = rowSize == 0 ? 0 : rePart[0].length;
  //    int rowSize2 = imPart.length;
  //    int columnSize2 = rowSize2 == 0 ? 0 : imPart[0].length;
  //
  //    if (rowSize != rowSize2 || columnSize != columnSize2) {
  //      throw new MatrixSizeException(Messages.getString("NumericalMatrixUtil.8")); //$NON-NLS-1$
  //    }
  //
  //    final BaseComplexSymbolicScalar<S, M, CS, CM> scalar = new BaseComplexSymbolicScalar<>(rePart[0][0], imPart[0][0]);
  //
  //    final BaseComplexSymbolicScalar<S, M, CS, CM>[][] ans = scalar.createArray(rowSize, columnSize);
  //    for (int i = 0; i < rowSize; i++) {
  //      for (int j = 0; j < columnSize; j++) {
  //        ans[i][j] = new BaseComplexSymbolicScalar<>(rePart[i][j], imPart[i][j]);
  //      }
  //    }
  //
  //    return ans;
  //  }

  //  /**
  //   * 複素行列の成分を返します。
  //   * 
  //   * @param <S> スカラーの型
  //   * @param <M> 行列の型
  //   * @param <CS> 係数スカラーの型
  //   * @param <CM> 係数行列の型
  //   * 
  //   * @param rePart 実部
  //   * @return 複素行列の成分
  //   */
  //  public static <S extends SymbolicScalar<S, M, CS, CM>, M extends BaseSymbolicMatrix<S, M, CS, CM>, CS extends NumericalScalar<CS, CM>, CM extends BaseNumericalMatrix<CS, CM>> BaseComplexSymbolicScalar<S, M, CS, CM>[][] createComplexArray(
  //      final S[][] rePart) {
  //    int rowSize = rePart.length;
  //    int columnSize = rowSize == 0 ? 0 : rePart[0].length;
  //
  //    final BaseComplexSymbolicScalar<S, M, CS, CM> scalar = new BaseComplexSymbolicScalar<>(rePart[0][0], rePart[0][0].createZero());
  //
  //    final BaseComplexSymbolicScalar<S, M, CS, CM>[][] ans = scalar.createArray(rowSize, columnSize);
  //    for (int i = 0; i < rowSize; i++) {
  //      for (int j = 0; j < columnSize; j++) {
  //        ans[i][j] = new BaseComplexSymbolicScalar<>(rePart[i][j], rePart[i][j].createZero());
  //      }
  //    }
  //
  //    return ans;
  //  }

  //  /**
  //   * 複素行列の成分を返します。
  //   * 
  //   * @param <S> スカラーの型
  //   * @param <M> 行列の型
  //   * @param <CS> 係数スカラーの型
  //   * @param <CM> 係数行列の型
  //   * 
  //   * @param rePart 実部
  //   * @param imPart 虚部
  //   * @return 複素行列の成分
  //   */
  //  public static <S extends SymbolicScalar<S, M, CS, CM>, M extends BaseSymbolicMatrix<S, M, CS, CM>, CS extends NumericalScalar<CS, CM>, CM extends BaseNumericalMatrix<CS, CM>> BaseComplexSymbolicScalar<S, M, CS, CM>[] createComplexArray(
  //      final S[] rePart, final S[] imPart) {
  //    int rowSize = rePart.length;
  //    int rowSize2 = imPart.length;
  //
  //    if (rowSize != rowSize2) {
  //      throw new MatrixSizeException(Messages.getString("NumericalMatrixUtil.8")); //$NON-NLS-1$
  //    }
  //
  //    final BaseComplexSymbolicScalar<S, M, CS, CM> scalar = new BaseComplexSymbolicScalar<>(rePart[0], imPart[0]);
  //
  //    final BaseComplexSymbolicScalar<S, M, CS, CM>[] ans = scalar.createArray(rowSize);
  //    for (int i = 0; i < rowSize; i++) {
  //      ans[i] = new BaseComplexSymbolicScalar<>(rePart[i], imPart[i]);
  //    }
  //
  //    return ans;
  //  }

  //  /**
  //   * 複素行列の成分を返します。
  //   * 
  //   * @param <S> スカラーの型
  //   * @param <M> 行列の型
  //   * @param <CS> 係数スカラーの型
  //   * @param <CM> 係数行列の型
  //   * 
  //   * @param rePart 実部
  //   * @return 複素行列の成分
  //   */
  //  public static <S extends SymbolicScalar<S, M, CS, CM>, M extends BaseSymbolicMatrix<S, M, CS, CM>, CS extends NumericalScalar<CS, CM>, CM extends BaseNumericalMatrix<CS, CM>> BaseComplexSymbolicScalar<S, M, CS, CM>[] createComplexArray(
  //      final S[] rePart) {
  //    int rowSize = rePart.length;
  //
  //    final BaseComplexSymbolicScalar<S, M, CS, CM> scalar = new BaseComplexSymbolicScalar<>(rePart[0], rePart[0].createZero());
  //
  //    final BaseComplexSymbolicScalar<S, M, CS, CM>[] ans = scalar.createArray(rowSize);
  //    for (int i = 0; i < rowSize; i++) {
  //      ans[i] = new BaseComplexSymbolicScalar<>(rePart[i], rePart[i].createZero());
  //    }
  //
  //    return ans;
  //  }

  /**
   * 実部の1次元配列を返します。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * @param <RES> 実係数スカラーの型
   * @param <REM> 実係数行列の型
   * @param <CES> 複素係数スカラーの型
   * @param <CEM> 複素係数行列の型
   * 
   * @param elements 対象となる行列
   * @return 実部の1次元配列
   */
  public static <RS extends RealSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, RM extends RealSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>,  CS extends ComplexSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, CM extends ComplexSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, RES extends RealNumericalScalar<RES,REM,CES,CEM>, REM extends RealNumericalMatrix<RES,REM,CES,CEM>,  CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>> RS[] getRealPartElements(
      final CS[] elements) {
    final int size = elements.length;

    final RS[] ans = elements[0].getRealPart().createArray(size);

    for (int i = 0; i < size; i++) {
      ans[i] = elements[i].getRealPart();
    }
    return ans;
  }

  /**
   * 実部の2次元配列を返します。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * @param <RES> 実係数スカラーの型
   * @param <REM> 実係数行列の型
   * @param <CES> 複素係数スカラーの型
   * @param <CEM> 複素係数行列の型
   * 
   * @param elements 対象となる行列
   * @return 実部の2次元配列
   */
  public static <RS extends RealSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, RM extends RealSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>,  CS extends ComplexSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, CM extends ComplexSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, RES extends RealNumericalScalar<RES,REM,CES,CEM>, REM extends RealNumericalMatrix<RES,REM,CES,CEM>,  CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>> RS[][] getRealPartElements(
      final CS[][] elements) {
    final int rowSize = elements.length;
    final int columnSize = rowSize == 0 ? 0 : elements[0].length;

    final RS[][] ans = elements[0][0].getRealPart().createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = elements[i][j].getRealPart();
      }
    }
    return ans;
  }

  /**
   * 虚部の1次元配列を返します。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * @param <RES> 実係数スカラーの型
   * @param <REM> 実係数行列の型
   * @param <CES> 複素係数スカラーの型
   * @param <CEM> 複素係数行列の型
   * 
   * @param elements 対象となる行列
   * @return 虚部の1次元配列
   */
  public static <RS extends RealSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, RM extends RealSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>,  CS extends ComplexSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, CM extends ComplexSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, RES extends RealNumericalScalar<RES,REM,CES,CEM>, REM extends RealNumericalMatrix<RES,REM,CES,CEM>,  CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>> RS[] getImaginaryPartElements(
      final CS[] elements) {
    final int size = elements.length;

    final RS[] ans = elements[0].getImaginaryPart().createArray(size);

    for (int i = 0; i < size; i++) {
      ans[i] = elements[i].getImaginaryPart();
    }
    return ans;
  }

  /**
   * 虚部の2次元配列を返します。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * @param <RES> 実係数スカラーの型
   * @param <REM> 実係数行列の型
   * @param <CES> 複素係数スカラーの型
   * @param <CEM> 複素係数行列の型
   * 
   * @param elements 対象となる行列
   * @return 虚部の2次元配列
   */
  public static <RS extends RealSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, RM extends RealSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>,  CS extends ComplexSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, CM extends ComplexSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, RES extends RealNumericalScalar<RES,REM,CES,CEM>, REM extends RealNumericalMatrix<RES,REM,CES,CEM>,  CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>> RS[][] getImaginaryPartElements(
      final CS[][] elements) {
    final int rowSize = elements.length;
    final int columnSize = rowSize == 0 ? 0 : elements[0].length;

    final RS[][] ans = elements[0][0].getImaginaryPart().createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = elements[i][j].getImaginaryPart();
      }
    }
    return ans;
  }

  /**
   * 実部を設定します。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * @param <RES> 実係数スカラーの型
   * @param <REM> 実係数行列の型
   * @param <CES> 複素係数スカラーの型
   * @param <CEM> 複素係数行列の型
   * 
   * @param matrix 対象となる行列
   * @param realPart 変更値
   */
  public static <RS extends RealSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, RM extends RealSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>,  CS extends ComplexSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, CM extends ComplexSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, RES extends RealNumericalScalar<RES,REM,CES,CEM>, REM extends RealNumericalMatrix<RES,REM,CES,CEM>,  CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>> void setRealPartElements(
      final CS[][] matrix, final int[][] realPart) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j].setRealPart(realPart[i][j]);
      }
    }
  }

  /**
   * 虚部を設定します。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * @param <RES> 実係数スカラーの型
   * @param <REM> 実係数行列の型
   * @param <CES> 複素係数スカラーの型
   * @param <CEM> 複素係数行列の型
   * 
   * @param matrix 対象となる行列
   * @param imagPart 変更値
   */
  public static <RS extends RealSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, RM extends RealSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>,  CS extends ComplexSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, CM extends ComplexSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, RES extends RealNumericalScalar<RES,REM,CES,CEM>, REM extends RealNumericalMatrix<RES,REM,CES,CEM>,  CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>>void setImaginaryPartElements(
      final CS[][] matrix, final int[][] imagPart) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j].setImaginaryPart(imagPart[i][j]);
      }
    }
  }

  /**
   * 実部を設定します。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * @param <RES> 実係数スカラーの型
   * @param <REM> 実係数行列の型
   * @param <CES> 複素係数スカラーの型
   * @param <CEM> 複素係数行列の型
   * 
   * @param matrix 対象となる行列
   * @param realPart 変更値
   */
  public static <RS extends RealSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, RM extends RealSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>,  CS extends ComplexSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, CM extends ComplexSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, RES extends RealNumericalScalar<RES,REM,CES,CEM>, REM extends RealNumericalMatrix<RES,REM,CES,CEM>,  CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>>void setRealPartElements(
      final CS[][] matrix, final double[][] realPart) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j].setRealPart(realPart[i][j]);
      }
    }
  }

  /**
   * 虚部を設定します。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * @param <RES> 実係数スカラーの型
   * @param <REM> 実係数行列の型
   * @param <CES> 複素係数スカラーの型
   * @param <CEM> 複素係数行列の型
   * 
   * @param matrix 対象となる行列
   * @param imagPart 変更値
   */
  public static <RS extends RealSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, RM extends RealSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>,  CS extends ComplexSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, CM extends ComplexSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, RES extends RealNumericalScalar<RES,REM,CES,CEM>, REM extends RealNumericalMatrix<RES,REM,CES,CEM>,  CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>>void setImaginaryPartElements(
      final CS[][] matrix, final double[][] imagPart) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j].setImaginaryPart(imagPart[i][j]);
      }
    }
  }

  /**
   * 実部を設定します。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * @param <RES> 実係数スカラーの型
   * @param <REM> 実係数行列の型
   * @param <CES> 複素係数スカラーの型
   * @param <CEM> 複素係数行列の型
   * 
   * @param matrix 対象となる行列
   * @param realPart 変更値
   */
  public static <RS extends RealSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, RM extends RealSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>,  CS extends ComplexSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, CM extends ComplexSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, RES extends RealNumericalScalar<RES,REM,CES,CEM>, REM extends RealNumericalMatrix<RES,REM,CES,CEM>,  CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>> void setRealPartElements(
      final CS[][] matrix, final RS[][] realPart) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j].setRealPart(realPart[i][j]);
      }
    }
  }

  /**
   * 虚部を設定します。
   * 
   * @param <RS> 実スカラーの型
   * @param <RM> 実行列の型
   * @param <CS> 複素スカラーの型
   * @param <CM> 複素行列の型
   * @param <RES> 実係数スカラーの型
   * @param <REM> 実係数行列の型
   * @param <CES> 複素係数スカラーの型
   * @param <CEM> 複素係数行列の型
   * 
   * @param matrix 対象となる行列
   * @param imagPart 変更値
   */
  public static <RS extends RealSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, RM extends RealSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>,  CS extends ComplexSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, CM extends ComplexSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, RES extends RealNumericalScalar<RES,REM,CES,CEM>, REM extends RealNumericalMatrix<RES,REM,CES,CEM>,  CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>>void setImaginaryPartElements(
      final CS[][] matrix, final RS[][] imagPart) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j].setImaginaryPart(imagPart[i][j]);
      }
    }
  }
}
