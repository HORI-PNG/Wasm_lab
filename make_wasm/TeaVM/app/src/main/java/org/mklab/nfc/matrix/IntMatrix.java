/*
 * $Id: IntMatrix.java,v 1.152 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import org.mklab.nfc.matx.MatxObject;
import org.mklab.nfc.matx.MxDataHead;
import org.mklab.nfc.scalar.DoubleNumberUtil;
import org.mklab.nfc.scalar.IntNumber;


/**
 * int型の値を成分とする整数行列を表すクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.152 $
 */
public class IntMatrix extends AbstractMatrix<IntNumber,IntMatrix> implements MatxObject {

  /** シリアルバージョン。 */
  private static final long serialVersionUID = 2957028955934269771L;

  /** 成分の出力フォーマット。 */
  private static String defaultElementFormat = "% 10d"; //$NON-NLS-1$

  /** 行列の成分。 */
  private int[][] elements;

  /**
   * 成分のデフォルト出力フォーマットを設定します。
   * 
   * @param format 成分のデフォルト出力フォーマット
   */
  public static void setDefaultElementFormat(final String format) {
    IntMatrix.defaultElementFormat = format;
  }

  /**
   * 成分のデフォルト出力フォーマットを返します。
   * 
   * @return 成分のデフォルト出力フォーマット
   */
  public static String getDefaultElementFormat() {
    return IntMatrix.defaultElementFormat;
  }

  /**
   * 新しく生成された<code>IntMatrix</code>オブジェクトを初期化します。
   * 
   * <p>0*0の整数行列を生成します。
   */
  public IntMatrix() {
    this(0, 0);
  }

  /**
   * <code>elements</code>で与えられた成分をもつ行ベクトルを生成します。
   * 
   * @param elements ベクトルの成分をもつ配列
   */
  public IntMatrix(final int[] elements) {
    this(elements.length == 0 ? 0 : 1, elements.length, elements.length == 0 ? new int[0][0] : new int[][] {elements});
  }

  /**
   * <code>elements</code>で与えられた成分をもつ行ベクトルを生成します。
   * 
   * @param elements ベクトルの成分をもつ配列
   */
  public IntMatrix(final byte[] elements) {
    this(elements.length == 0 ? 0 : 1, elements.length, elements.length == 0 ? new byte[0][0] : new byte[][] {elements});
  }

  /**
   * <code>rowSize</code>*<code>columnSize</code>の成分0の整数行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   */
  public IntMatrix(final int rowSize, final int columnSize) {
    this(rowSize, columnSize, new int[rowSize][columnSize]);
  }

  /**
   * <code>elements</code>で与えられた成分をもつ<code>rowSize</code>*<code>columnSize</code >の整数行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 行列の成分をもつ配列
   */
  public IntMatrix(final int rowSize, final int columnSize, final int[][] elements) {
    super(rowSize, columnSize);

    if (elements == null) {
      throw new MatrixSizeException(MatrixSizeException.INCORRECT_SIZE);
    }
    if (rowSize != 0 && columnSize != 0 && rowSize != elements.length) {
      throw new MatrixSizeException(MatrixSizeException.INCORRECT_SIZE);
    }
    if (rowSize != 0 && columnSize != 0 && columnSize != elements[0].length) {
      throw new MatrixSizeException(MatrixSizeException.INCORRECT_SIZE);
    }

    this.elements = elements;
    setElementFormat(IntMatrix.defaultElementFormat);
    setElementAlignment(GridElementAlignment.RIGHT);
  }

  /**
   * <code>elements</code>で与えられた成分をもつ<code>rowSize</code>*<code>columnSize</code >の整数行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 行列の成分をもつ配列
   */
  public IntMatrix(final int rowSize, final int columnSize, final byte[][] elements) {
    super(rowSize, columnSize);

    if (elements == null) {
      throw new MatrixSizeException(MatrixSizeException.INCORRECT_SIZE);
    }
    if (rowSize != 0 && columnSize != 0 && rowSize != elements.length) {
      throw new MatrixSizeException(MatrixSizeException.INCORRECT_SIZE);
    }
    if (rowSize != 0 && columnSize != 0 && columnSize != elements[0].length) {
      throw new MatrixSizeException(MatrixSizeException.INCORRECT_SIZE);
    }

    this.elements = new int[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        this.elements[i][j] = elements[i][j];
      }
    }
    setElementFormat(IntMatrix.defaultElementFormat);
  }

  /**
   * <code>elements</code>で与えられた成分をもつ整数行列を生成します。
   * 
   * @param elements 行列の成分をもつ配列
   */
  public IntMatrix(final int[][] elements) {
    this(elements.length, elements.length == 0 ? 0 : elements[0].length, elements);
  }

  /**
   * <code>elements</code>で与えられた成分をもつ整数行列を生成します。
   * 
   * @param elements 行列の成分をもつ配列
   */
  public IntMatrix(final byte[][] elements) {
    this(elements.length, elements.length == 0 ? 0 : elements[0].length, elements);
  }

  /**
   * 新しく生成された<code>IntMatrix</code>オブジェクトを初期化します。
   * 
   * @param matrix 実数行列
   */
  public IntMatrix(final DoubleMatrix matrix) {
    this(IntMatrixUtil.createArray(matrix.getDoubleElements()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object opponent) {
    if (this == opponent) {
      return true;
    }
    if (opponent == null) {
      return false;
    }
    if (opponent.getClass() != getClass()) {
      return false;
    }

    if (isSameSize((IntMatrix)opponent) == false) {
      return false;
    }

    return IntMatrixUtil.equals(this.elements, ((IntMatrix)opponent).elements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public boolean equals(final IntMatrix opponent, final double tolerance) {
//    if (!(opponent instanceof IntMatrix)) {
//      return false;
//    }
//    return equals((IntMatrix)opponent, tolerance);
//  }

  /**
   * 行列<code>opponent</code>と等しか判定します。
   * 
   * @param opponent 整数行列
   * @return <code>opponent</code>と等しければtrue,そうでなければfalse
   */
  public boolean equals(final IntMatrix opponent) {
    if (isSameSize(opponent) == false) {
      return false;
    }

    return IntMatrixUtil.equals(this.elements, opponent.elements);
  }

  /**
   * 許容誤差範囲内で全ての成分が等しか判定します。
   * 
   * @param opponent 整数行列
   * @param tolerance 許容誤差
   * @return 許容誤差範囲内で全ての成分が等しければtrue、そうでなければfalse
   */
  public boolean equals(final IntMatrix opponent, final double tolerance) {
    if (isSameSize(opponent) == false) {
      return false;
    }

    return IntMatrixUtil.equals(this.elements, opponent.elements, tolerance);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int hashCode = super.hashCode();
    final int prime = 31;
    hashCode = prime * hashCode + (int)(+serialVersionUID ^ (serialVersionUID >>> 32));
    for (int i0 = 0; this.elements != null && i0 < this.elements.length; i0++) {
      for (int i1 = 0; this.elements != null && i1 < this.elements[0].length; i1++) {
        hashCode = prime * hashCode + this.elements[i0][i1];
      }
    }
    return hashCode;
  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public final boolean isTransformableFrom(final Matrix<?,?> value) {
//    if (value instanceof IntMatrix) {
//      return true;
//    }
//
//    return false;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public final IntMatrix transformFrom(final Matrix<?,?> value) {
//    if (value instanceof IntMatrix) {
//      return ((IntMatrix)value).clone();
//    }
//
//    throw new IllegalArgumentException(Messages.getString("IntMatrix.1") + value); //$NON-NLS-1$
//  }

  /**
   * <code>size</code>*<code>size</code>の単位行列を生成します。
   * 
   * @param size サイズ指定
   * @return size*sizeの単位行列
   */
  public static IntMatrix unit(final int size) {
    return unit(size, size);
  }

  /**
   * <code>rowSize</code>*<code>columnSize</code>の単位行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return rowSize*columnSizeの単位行列
   */
  public static IntMatrix unit(final int rowSize, final int columnSize) {
    return new IntMatrix(IntMatrixUtil.unit(rowSize, columnSize));
  }

  /**
   * 行列<code>matrix</code>と同サイズの実単位行列を返します。
   * 
   * @param matrix 行列
   * @return 単位行列
   */
  public static IntMatrix unit(final Grid matrix) {
    return unit(matrix.getRowSize(), matrix.getColumnSize());
  }

  /**
   * 行列<code>matrix</code>の<code>rowNumber</code>*<code>colNumber</code> 倍の実単位行列を返します。
   * 
   * @param rowNumber 行方向の倍数
   * @param columnNumber 列方向の倍数
   * @param block 基本となる行列
   * @return <code>matrix</code>のrowNum * colNum倍の実単位行列
   */
  public static IntMatrix unit(final int rowNumber, final int columnNumber, final Grid block) {
    return unit(block.getRowSize() * rowNumber, block.getColumnSize() * columnNumber);
  }

  /**
   * 行列<code>matrix</code>と同サイズの零行列を生成します。
   * 
   * @param matrix 行列
   * @return <code>matrix</code>と同サイズの零行列
   */
  public static IntMatrix zero(final Grid matrix) {
    return new IntMatrix(matrix.getRowSize(), matrix.getColumnSize());
  }

  /**
   * 行列<code>block</code>の<code>rowNumber</code>*<code>colNumber</code> 倍の零行列を生成します。
   * 
   * @param rowNumber 行方向の倍数
   * @param columnNumber 列方向の倍数
   * @param block 基本となる行列
   * @return <code>block</code>の<code>rowNumber</code> * <code>columnNumber</code>倍の零行列
   */
  public static IntMatrix zero(final int rowNumber, final int columnNumber, final Grid block) {
    return new IntMatrix(block.getRowSize() * rowNumber, block.getColumnSize() * columnNumber);
  }

  /**
   * <code>size</code>*<code>size</code>の全成分1の行列を生成します。
   * 
   * @param size サイズ指定
   * @return <code>size</code>*<code>size</code>の全成分1の行列
   */
  public static IntMatrix ones(final int size) {
    return ones(size, size);
  }

  /**
   * <code>rowSize</code>*<code>columnSize</code>の全成分1の行列を生成します。
   * 
   * @param rowSize 行番号の指定
   * @param columnSize 列番号の指定
   * @return <code>rowSize</code>*<code>columnSize</code>の全成分1の行列
   */
  public static IntMatrix ones(final int rowSize, final int columnSize) {
    return new IntMatrix(IntMatrixUtil.ones(rowSize, columnSize));
  }

  /**
   * 行列<code>matrix</code>と同サイズの全成分1の行列を生成します。
   * 
   * @param matrix 行列
   * @return 全成分1の行列
   */
  public static IntMatrix ones(final Grid matrix) {
    return ones(matrix.getRowSize(), matrix.getColumnSize());
  }

  /**
   * 行列<code>block</code>の<code>rowNumber</code>*<code>colNumber</code> 倍の全成分1の行列を生成します。
   * 
   * @param rowNumber 行方向の倍数
   * @param columNumber 列方向の倍数
   * @param block 基本となる行列
   * @return <code>block</code>の<code>rowNumber</code> * <code>columnNumber</code>倍の全成分1の行列
   */
  public static IntMatrix ones(final int rowNumber, final int columNumber, final Grid block) {
    return ones(block.getRowSize() * rowNumber, block.getColumnSize() * columNumber);
  }

  /**
   * 対角行列を生成します。
   * 
   * @param diagonalElements 対角成分
   * @return 対角行列
   */
  public static IntMatrix diagonal(final int[] diagonalElements) {
    return new IntMatrix(IntMatrixUtil.diagonal(diagonalElements));
  }

  /**
   * {@inheritDoc}
   */
  public String toString(String format) {
    return IntMatrixUtil.toString(getIntElements(), format);
  }
  
  /**
   * 全成分を <code>int</code> の2次元配列として返します。
   * 
   * @return 全成分
   */
  final public int[][] getIntElements() {
    return this.elements;
  }

  /**
   * 全成分を <code>int</code> の2次元配列として返します。
   * 
   * @return 全成分
   */
  final public IntNumber[][] getElements() {
    final IntNumber[][] ans = new IntNumber[getRowSize()][getColumnSize()];
    for (int i = 0; i <getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        ans[i][j]= new IntNumber(this.elements[i][j]);
      }
    }
    return ans;
  }

  /**
   * ベクトルの成分を返します。
   * 
   * @param index 成分の番号(1から始まる)
   * @return ベクトルの整数成分
   */
  public final int getIntElement(final int index) {
    return this.elements[(index - 1) / getColumnSize()][(index - 1) % getColumnSize()];
  }

  /**
   * {@inheritDoc}
   */
  public final IntNumber getElement(final int index) {
    return new IntNumber(getIntElement(index));
  }

  /**
   * 行列の成分を返します。
   * 
   * @param row 行番号(1から始まる)
   * @param column 列番号(1から始まる)
   * @return 指定した行列の成分
   */
  public final int getIntElement(final int row, final int column) {
    return this.elements[row - 1][column - 1];
  }

  /**
   * {@inheritDoc}
   */
  public final IntNumber getElement(final int row, final int column) {
    return new IntNumber(getIntElement(row, column));
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubMatrix(final IntMatrix rowIndex, final int columnMin, final int columnMax, final IntMatrix source) {
//    if (source instanceof IntMatrix) {
//      setSubMatrix(rowIndex, columnMin, columnMax, (IntMatrix)source);
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("IntMatrix.2")); //$NON-NLS-1$
//  }

  /**
   * <code>rowIndex</code>で指定した行の<code>columnMin</code>列から<code>columnMax</code> 列までの 行列<code>source</code>を代入します。
   * 
   * @param rowIndex 行番号を指定する指数
   * @param columnMin 列の始まり
   * @param columnMax 列の終わり
   * @param source 代入する行列
   */
  public final void setSubMatrix(final IntMatrix rowIndex, final int columnMin, final int columnMax, final IntMatrix source) {
    if (rowIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("IntMatrix.3")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    IntMatrixUtil.setSubMatrix(this.elements, index, columnMin - 1, columnMax - 1, source.elements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubMatrix(final IntMatrix rowIndex, final IntMatrix columnIndex, final Matrix<?,?> source) {
//    if (source instanceof IntMatrix) {
//      setSubMatrix(rowIndex, columnIndex, (IntMatrix)source);
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("IntMatrix.4")); //$NON-NLS-1$
//  }

  /**
   * <code>rowIndex</code>で指定した行の<code>columnIndex</code>で指定した列に行列<code>source</code>を代入します。
   * 
   * @param rowIndex 行番号を指定する指数
   * @param columnIndex 列番号を指定する指数
   * @param source 代入する行列
   */
  public final void setSubMatrix(final IntMatrix rowIndex, final IntMatrix columnIndex, final IntMatrix source) {
    if (rowIndex.getRowSize() != 1 || columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("IntMatrix.5")); //$NON-NLS-1$
    }

    final int[] rowIdx = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    final int[] columnIdx = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    IntMatrixUtil.setSubMatrix(this.elements, rowIdx, columnIdx, source.elements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubMatrix(final int rowMin, final int rowMax, final IntMatrix columnIndex, final Matrix<?,?> source) {
//    if (source instanceof IntMatrix) {
//      setSubMatrix(rowMin, rowMax, columnIndex, (IntMatrix)source);
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("IntMatrix.6")); //$NON-NLS-1$
//  }

  /**
   * <code>rowMin</code>列から<code>rowMax</code>列目の成分の<code>columnIndex</code> で指定された行に、 行列<code>source</code>の成分を代入します。
   * 
   * @param rowMin 行の始まり
   * @param rowMax 行の終わり
   * @param columnIndex 列指定ベクトル
   * @param source 代入する行列
   */
  public final void setSubMatrix(final int rowMin, final int rowMax, final IntMatrix columnIndex, final IntMatrix source) {
    if (columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("IntMatrix.7")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    IntMatrixUtil.setSubMatrix(this.elements, rowMin - 1, rowMax - 1, index, source.elements);
  }

  /**
   * 成分を行毎に数え、<code>min</code>から<code>max</code>までに<code>source</code>の成分を代入します。
   * 
   * @param min 開始位置(1から始まる)
   * @param max 終了位置(1から始まる)
   * @param source 代入するベクトル
   */
  public final void setSubVector(final int min, final int max, final IntMatrix source) {
    if ((max - min + 1) != source.getColumnSize()) {
      throw new IllegalArgumentException(Messages.getString("IntMatrix.8")); //$NON-NLS-1$
    }

    IntMatrixUtil.setSubVector(this.elements[0], min - 1, max - 1, source.elements[0]);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubVector(final int min, final int max, final IntMatrix source) {
//    if (source instanceof IntMatrix) {
//      setSubVector(min, max, (IntMatrix)source);
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("IntMatrix.8")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubVector(final IntMatrix index, final Matrix<?,?> source) {
//    if (source instanceof IntMatrix) {
//      setSubVector(index, (IntMatrix)source);
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("IntMatrix.8")); //$NON-NLS-1$
//  }

  /**
   * <code>index</code>で指定した各成分に行列<code>source</code>の成分を代入します。
   * 
   * @param index 成分の番号を指定する指数
   * @param source 代入するベクトル
   */
  public final void setSubVector(final IntMatrix index, final IntMatrix source) {
    if (index.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("IntMatrix.9")); //$NON-NLS-1$
    }

    final int[] idx = IntMatrixUtil.decrement(index.getIntElements()[0]);
    IntMatrixUtil.setElements(this.elements, idx, source.elements);
  }

  /**
   * 成分を行毎に数え<code>index</code>で指定した位置に整数<code>value</code>を代入します。
   * 
   * @param index 成分の番号
   * @param value 代入する値
   */
  public final void setElement(final int index, final int value) {
    this.elements[(index - 1) / getColumnSize()][(index - 1) % getColumnSize()] = value;
  }

  /**
   * {@inheritDoc}
   */
  public final void setElement(final int index, final double value) {
    this.elements[(index - 1) / getColumnSize()][(index - 1) % getColumnSize()] = (int)value;
    warning("convert from double to int "); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public void setElement(int index, IntNumber value) {
    setElement(index, value.intValue());
  }

  /**
   * 指定した位置に整数<code>value</code>を代入します。
   * 
   * @param row 行番号
   * @param column 列番号
   * @param value 代入する整数
   */
  public final void setElement(final int row, final int column, final int value) {
    this.elements[row - 1][column - 1] = value;
  }

  /**
   * {@inheritDoc}
   */
  public final void setElement(final int row, final int column, final double  value) {
    this.elements[row - 1][column - 1] = (int)value;
    warning("convert from double to int "); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final void setElement(final int row, final int column, final IntNumber value) {
    setElement(row, column, value.intValue());
  }


  /**
   * <code>source</code>から<code>to</code>までの連続する整数を成分にもつ行ベクトルを返します。
   * 
   * @param from 始点
   * @param to 終点
   * @return 連続する整数を成分にもつ行ベクトル
   */
  public static IntMatrix series(final int from, final int to) {
    if (from <= to) {
      return series(from, to, 1);
    }
    return series(from, to, -1);
  }

  /**
   * <code>source</code>から<code>to</code>までの<code>by</code>飛びの整数を成分にもつ行ベクトルを返します 。
   * 
   * @param from 始点
   * @param to 終点
   * @param by 間隔
   * @return sourceからtoまでのby飛びの整数をもつ行ベクトル
   */
  public static IntMatrix series(final int from, final int to, final int by) {
    return new IntMatrix(IntMatrixUtil.series(from, to, by));
  }

  /**
   * 行列<code>value</code>との和(<code>this</code>+<code>value</code>)を返します。
   * 
   * @param value 実数行列
   * @return <code>value</code>との和
   */
  @Override
  public final IntMatrix add(final IntMatrix value) {
    if (isSameSize(value) == false) {
      throw new MatrixSizeException(this, value, MatrixSizeException.NOT_SAME_SIZE);
    }

    return new IntMatrix(IntMatrixUtil.add(this.elements, value.elements));
  }

  /**
   * 行列<code>value</code>との和(<code>this</code>+<code>value</code>)を返します。
   * 
   * @param value 実数行列
   * @return <code>value</code>との和
   */
  public final DoubleMatrix add(final DoubleMatrix value) {
    return new DoubleMatrix(this).add(value);
  }

//  /**
//   * 行列<code>value</code>との和(<code>this</code>+<code>value</code>)を返します。
//   * 
//   * @param value 行列
//   * @return <code>value</code>との和
//   */
//  public final BaseMatrix<?, ?> add(final BaseMatrix<?, ?> value) {
//    return value.transformFrom(this).add(value);
//  }

  /**
   * 行列<code>value</code>との差(<code>this</code>-<code>value</code>)を返します。
   * 
   * @param value 行列
   * @return <code>value</code>との差
   */
  @Override
  public final IntMatrix subtract(final IntMatrix value) {
    if (isSameSize(value) == false) {
      throw new MatrixSizeException(this, value, MatrixSizeException.NOT_SAME_SIZE);
    }

    return new IntMatrix(IntMatrixUtil.subtract(this.elements, value.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix addElementWise(final int value) {
    return new IntMatrix(IntMatrixUtil.addElementWise(this.elements, value));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix subtractElementWise(final int value) {
    return new IntMatrix(IntMatrixUtil.subtractElementWise(this.elements, value));
  }

  /**
   * 行列<code>value</code>との差(<code>this</code>-<code>value</code>)を返します。
   * 
   * @param value 行列
   * @return <code>value</code>との差
   */
  public final DoubleMatrix subtract(final DoubleMatrix value) {
    return new DoubleMatrix(this).subtract(value);
  }

//  /**
//   * 行列<code>value</code>との差(<code>this</code>-<code>value</code>)を返します。
//   * 
//   * @param value 行列
//   * @return <code>value</code>との差
//   */
//  public final BaseMatrix<?, ?> subtract(final BaseMatrix<?, ?> value) {
//    return value.transformFrom(this).subtract(value);
//  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix multiply(final int value) {
    return new IntMatrix(IntMatrixUtil.multiply(this.elements, value));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix multiply(final double value) {
    return multiply((int)value);
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix multiply(final IntNumber value) {
    return value.createGrid(getRowSize(), getColumnSize(), this.elements).multiply(value);
  }

  /**
   * <code>value</code>との成分毎の積を成分にもつ行列を返します。
   * 
   * @param value 実数行列
   * @return 掛け算の結果
   */
  @Override
  public final IntMatrix multiplyElementWise(final IntMatrix value) {
    if (isSameSize(value) == false) {
      throw new MatrixSizeException(this, value, MatrixSizeException.NOT_SAME_SIZE);
    }
    return new IntMatrix(IntMatrixUtil.multiplyElementWise(this.elements, value.elements));
  }

  /**
   * 行列<code>value</code>との積(<code>this</code>*<code>value</code>)を返します。
   * 
   * @param value 整数行列
   * @return <code>value</code>との積
   */
  @Override
  public final IntMatrix multiply(final IntMatrix value) {
    if (getColumnSize() != value.getRowSize()) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_SIZE);
    }
    
    final int[][] ans;
    if (isEmpty() || value.isEmpty()) {
      ans = new int[getRowSize()][value.getColumnSize()];
    } else { 
      ans = IntMatrixUtil.multiply(this.elements, value.elements);
    }
    
    return new IntMatrix(getRowSize(), value.getColumnSize(), ans);
  }

  /**
   * 行列<code>value</code>との積(<code>this</code>*<code>value</code>)を返します。
   * 
   * @param value 実数行列
   * @return <code>value</code>との積
   */
  public final DoubleMatrix multiply(final DoubleMatrix value) {
    return new DoubleMatrix(this).multiply(value);
  }

//  /**
//   * 行列<code>value</code>との積(<code>this</code>*<code>value</code>)を返します。
//   * 
//   * @param value 行列
//   * @return <code>value</code>との積
//   */
//  public final BaseMatrix<?, ?> multiply(final BaseMatrix<?, ?> value) {
//    return value.transformFrom(this).multiply(value);
//  }

  /**
   * 複素共役を返します。
   * 
   * @return 複素共役
   */
  public final IntMatrix conjugate() {
    return createClone();
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix powerElementWise(final int scalar) {
    return new IntMatrix(IntMatrixUtil.powerElementWise(this.elements, scalar));
  }

  /**
   * 成分毎に累乗します。
   * 
   * @param scalar 累乗の指数
   * @return 累乗の結果
   */
  public final DoubleMatrix powerElementWise(final double scalar) {
    return new DoubleMatrix(DoubleMatrixUtil.powerElementWise(this.elements, scalar));
  }

  /**
   * 成分毎に累乗します。
   * 
   * @param scalar 累乗の指数
   * @return 累乗の結果
   */
  public final IntMatrix powerElementWise(final IntNumber scalar) {
    return powerElementWise(scalar.intValue());
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix powerElementWise(final IntMatrix matrix) {
    if (isSameSize(matrix) == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_SAME_SIZE);
    }

    return new IntMatrix(IntMatrixUtil.powerElementWise(this.elements, matrix.getIntElements()));
  }

  /**
   * 成分毎に累乗します。
   * 
   * @param matrix 累乗の指数を成分とする行列
   * @return 累乗の結果
   */
  public final DoubleMatrix powerElementWise(final DoubleMatrix matrix) {
    if (isSameSize(matrix) == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_SAME_SIZE);
    }

    return new DoubleMatrix(DoubleMatrixUtil.powerElementWise(this.elements, matrix.getDoubleElements()));
  }
  
//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public final Matrix<?,?> powerElementWise(final Matrix<?,?> order) {
//    if (order instanceof IntMatrix) {
//      return powerElementWise((IntMatrix)order);
//    }
//    if (order instanceof DoubleMatrix) {
//      return powerElementWise((DoubleMatrix)order);
//    }
//
//    if (order instanceof BaseNumericalMatrix<?,?>) {
//      return powerElementWise((BaseNumericalMatrix<?,?>)order);
//    }
//    
//    throw new IllegalArgumentException();
//  }

//  /**
//   * 成分毎に累乗します。
//   * @param <S> スカラーの型
//   * @param <M> 行列の型
//   * @param a 整数行列
//   * @param b 数値行列
//   * 
//   * @param matrix 累乗の指数を成分とする行列
//   * @return 累乗の結果
//   */
//  public  static <S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> M powerElementWise(final IntMatrix a, final M b) {
//    if (a.isSameSize(b) == false) {
//      throw new MatrixSizeException(MatrixSizeException.NOT_SAME_SIZE);
//    }
//
//    return b.transformFrom(a).powerElementWise(b);
//  }

  /**
   * 1個の整数について、行列の各成分の累乗を求めます。
   * 
   * @param scalar 累乗の対象
   * @param matrix 累乗の指数を成分とする行列
   * @return 累乗の結果
   */
  public static IntMatrix powerElementWise(final int scalar, final IntMatrix matrix) {
    return new IntMatrix(IntMatrixUtil.powerElementWise(scalar, matrix.elements));
  }

//  /**
//   * 1個の整数について、行列の各成分の累乗を求めます。
//   * 
//   * @param scalar 累乗の対象
//   * @param matrix 累乗の指数を成分とする行列
//   * @return 累乗の結果
//   */
//  public static Matrix<?,?> powerElementWise(final int scalar, final Matrix<?,?> matrix) {
//    if (matrix instanceof IntMatrix) {
//      return IntMatrix.powerElementWise(scalar, (IntMatrix)matrix);
//    }
//
//    throw new UnsupportedOperationException();
//  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero(final double tolerance) {
    return IntMatrixUtil.isZero(this.elements, tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit(final double tolerance) {
    return IntMatrixUtil.isUnit(this.elements, tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix inverse() {
    throw new UnsupportedOperationException();
    //final DoubleMatrix a = new DoubleMatrix(this);
    //return (DoubleMatrix)inverse(a.frobNorm().doubleValue() * DoubleNumberUtil.EPS, false);
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix inverse(final double tolerance, final boolean stopIfSingular) {
    throw new UnsupportedOperationException();
    //return new DoubleMatrix(this).inverse(tolerance, stopIfSingular);
  }
  
  /**
   * {@inheritDoc}
   */
  public final IntMatrix divide(final int value) {
    return new IntMatrix(IntMatrixUtil.divide(this.elements, value));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix divide(final double value) {
    return divide((int)value);
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix divide(final IntNumber value) {
    return value.createGrid(getRowSize(), getColumnSize(), this.elements).divide(value);
  }

  /**
   * 行列<code>value</code>の逆行列との積(<code>this</code>*<code>value</code> <sup>-1 </sup>)を返します。
   * 
   * @param value 行列
   * @return mとの逆行列の積
   */
  @Override
  public final IntMatrix divide(final IntMatrix value) {
    throw new UnsupportedOperationException();
    //return new DoubleMatrix(this).divide(new DoubleMatrix(value));
  }

  /**
   * 行列<code>value</code>の逆行列との積(<code>this</code>*<code>value</code> <sup>-1 </sup>)を返します。
   * 
   * @param value 行列
   * @return <code>value</code>の逆行列との積
   */
  public final DoubleMatrix divide(final DoubleMatrix value) {
    return new DoubleMatrix(this).divide(value);
  }

//  /**
//   * 行列<code>value</code>との逆行列の積(<code>this</code>*m <sup>-1 </sup>)を返します。
//   * 
//   * @param value 行列
//   * @return <code>value</code>との逆行列の積
//   */
//  public final BaseMatrix<?, ?> divide(final BaseMatrix<?, ?> value) {
//    return value.transformFrom(this).divide(value);
//  }

  /**
   * <code>value</code>との成分毎の商を成分にもつ行列を返します。
   * 
   * @param value 割る行列
   * 
   * @return 割り算の結果
   */
  @Override
  public final IntMatrix divideElementWise(final IntMatrix value) {
    throw new UnsupportedOperationException();
    //return new DoubleMatrix(this).divideElementWise(new DoubleMatrix(value));
  }

  /**
   * 逆行列と行列<code>value</code>との積(<code>this</code>*<code>value</code> <sup>-1 </sup>)を返します。
   * 
   * @param value 行列
   * @return 逆行列と<code>value</code>との積
   */
  @Override
  public final IntMatrix leftDivide(final IntMatrix value) {
    throw new UnsupportedOperationException();
    //return new DoubleMatrix(this).leftDivide(new DoubleMatrix(value));
  }

  /**
   * 自身と<code>value</code>の成分毎の左からの商を成分にもつ行列を返します。
   * 
   * @param value 割られる行列
   * 
   * @return 割り算の結果
   */
  @Override
  public final IntMatrix leftDivideElementWise(final IntMatrix value) {
    throw new UnsupportedOperationException();
    //return new DoubleMatrix(this).leftDivideElementWise(new DoubleMatrix(value));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix unaryMinus() {
    return new IntMatrix(IntMatrixUtil.unaryMinus(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix transpose() {
    return new IntMatrix(IntMatrixUtil.transpose(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix conjugateTranspose() {
    return new IntMatrix(IntMatrixUtil.transpose(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix getSubMatrix(final int rowMin, final int rowMax, final int columnMin, final int columnMax) {
    final int[][] ans = IntMatrixUtil.getSubMatrix(this.elements, rowMin - 1, rowMax - 1, columnMin - 1, columnMax - 1);
    return new IntMatrix(ans);
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix getSubVector(final IntMatrix index) {
    if (index.getRowSize() == 0) {
      return new IntMatrix(new int[0]);
    }
    if (index.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("IntMatrix.10")); //$NON-NLS-1$
    }

    final int[] idx = IntMatrixUtil.decrement(index.getIntElements()[0]);

    if (getColumnSize() == 1) {
      final int[] mat = (transpose().elements)[0];
      final int[] ans = IntMatrixUtil.getSubVector(mat, idx);
      return new IntMatrix(ans).transpose();
    } 
    if (getRowSize() == 1) {
      final int[] ans = IntMatrixUtil.getSubVector(this.elements[0], idx);
      return new IntMatrix(ans);
    } 

    throw new MatrixSizeException(MatrixSizeException.NOT_A_VECTOR_MATRIX);
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix getSubMatrix(final IntMatrix rowIndex, final int columnMin, final int columnMax) {
    if (rowIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("IntMatrix.12")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    return new IntMatrix(IntMatrixUtil.getSubMatrix(this.elements, index, columnMin - 1, columnMax - 1));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix getSubMatrix(final int rowMin, final int rowMax, final IntMatrix columnIndex) {
    if (columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("IntMatrix.14")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    return new IntMatrix(IntMatrixUtil.getSubMatrix(this.elements, rowMin - 1, rowMax - 1, index));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix getSubMatrix(final IntMatrix rowIndex, final IntMatrix columnIndex) {
    if (rowIndex.getRowSize() != 1 || columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("IntMatrix.15")); //$NON-NLS-1$
    }

    final int[] rowIdx = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    final int[] columnIdx = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    return new IntMatrix(IntMatrixUtil.getSubMatrix(this.elements, rowIdx, columnIdx));
  }

  /**
   * 下側に行列<code>value</code>を連結した行列を返します。
   * 
   * @param value つける行列
   * @return 下側に<code>value</code>を連結した行列
   */
  @Override
  public final IntMatrix appendDown(final IntMatrix value) {
    if (getColumnSize() != value.getColumnSize() && getColumnSize() != 0 && value.getColumnSize() != 0) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_COLUMN_NUMBER);
    }

    return new IntMatrix(IntMatrixUtil.appendDown(this.elements, value.elements));
  }

  /**
   * 右側に<code>value</code>を連結した行列を返します。
   * 
   * @param value つける整数行列
   * @return 右側に<code>value</code>を連結した行列
   * 
   */
  @Override
  public final IntMatrix appendRight(final IntMatrix value) {
    if (hasSameRowSize(value) == false && this.getRowSize() != 0 && value.getRowSize() != 0) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_ROW_NUMBER);
    }

    return new IntMatrix(IntMatrixUtil.appendRight(this.elements, value.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final void printElements(final Writer output) {
    final int maxColumnSize = Integer.MAX_VALUE;
    printElements(output, maxColumnSize);
  }

  /**
   * {@inheritDoc}
   */
  public final void printElements(final Writer output, final int maxColumnSize) {
    IntMatrixUtil.print(this.elements, output, getElementFormat(), maxColumnSize);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone() {
    final IntMatrix m = (IntMatrix)super.clone();
    m.elements = IntMatrixUtil.clone(this.elements);
    return m;
  }

  /**
   * {@inheritDoc}
   */
  public final void exchangeRow(final int row1, final int row2) {
    IntMatrixUtil.exchangeRow(this.elements, row1 - 1, row2 - 1);
  }

  /**
   * {@inheritDoc}
   */
  public final void exchangeColumn(final int column1, final int column2) {
    IntMatrixUtil.exchangeColumn(this.elements, column1 - 1, column2 - 1);
  }

  /**
   * 各成分に実数行列<code>source</code>の各成分をコピーします。
   * 
   * @param source 実数行列
   */
  public final void copy(final IntMatrix source) {
    if (isSameSize(source) == false) {
      throw new MatrixSizeException(this, source, MatrixSizeException.NOT_SAME_SIZE);
    }

    IntMatrixUtil.copy(source.elements, this.elements);
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix diagonalToVector() {
    return new IntMatrix(IntMatrixUtil.diagonalToVector(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix vectorToDiagonal() {
    if (getRowSize() == 1) {
     final int[][] ans = IntMatrixUtil.vectorToDiagonal(this.elements[0]);
      return new IntMatrix(ans);
    } 

    if (getColumnSize() == 1) {
      final int[][] vector = IntMatrixUtil.transpose(this.elements);
      final int[][] ans = IntMatrixUtil.vectorToDiagonal(vector[0]);
      return new IntMatrix(ans);
    } 

    throw new MatrixSizeException(MatrixSizeException.INCONSISTENT_SIZE);
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix reshape(final int newRowSize, final int newColumnSize) {
    if (getRowSize() * getColumnSize() != newRowSize * newColumnSize) {
      throw new MatrixSizeException(MatrixSizeException.INCONSISTENT_SIZE);
    }

    return new IntMatrix(IntMatrixUtil.reshape(this.elements, newRowSize, newColumnSize));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix resize(final int newRowSize, final int newColumnSize) {
    this.elements = IntMatrixUtil.resize(this.elements, newRowSize, newColumnSize);
    setRowSize(newRowSize);
    setColumnSize(newColumnSize);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public final void removeColumnVectors(final int min, final int max) {
    this.elements = IntMatrixUtil.removeColumnVectors(this.elements, min - 1, max - 1);
    setColumnSize(getColumnSize() - (max - min + 1));
  }

  /**
   * {@inheritDoc}
   */
  public final void removeColumnVectors(final IntMatrix index) {
    if (index.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("IntMatrix.17")); //$NON-NLS-1$
    }

    final int[] idx = IntMatrixUtil.decrement(index.getIntElements()[0]);
    final int[][] ans = IntMatrixUtil.removeColumnVectors(this.elements, idx);

    this.elements = ans;
    int newColumnSize = ans.length == 0 ? 0 : ans[0].length;
    setColumnSize(newColumnSize);
  }

  /**
   * {@inheritDoc}
   */
  public final void removeRowVectors(final int min, final int max) {
    this.elements = IntMatrixUtil.removeRowVectors(this.elements, min - 1, max - 1);
    setRowSize(getRowSize() - (max - min + 1));
  }

  /**
   * {@inheritDoc}
   */
  public final void removeRowVectors(final IntMatrix index) {
    if (index.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("IntMatrix.18")); //$NON-NLS-1$
    }

    final int[] idx = IntMatrixUtil.decrement(index.getIntElements()[0]);
    final int[][] ans = IntMatrixUtil.removeRowVectors(this.elements, idx);

    this.elements = ans;
    setRowSize(ans.length);
  }

  /**
   * 全ての成分を昇順に並び替えた行列 と、 元の位置を示す指数({@link IntMatrix})を返します。
   * 
   * 自身が複素行列のときは、絶対値でソートします。
   * 
   * @return ソートされた結果
   */
  public final IndexedMatrix<IntNumber,IntMatrix> sort() {
    final IndexedMatrix<IntNumber,IntMatrix> ans = reshape(1, getRowSize() * getColumnSize()).sortRowWise();
    return new IndexedMatrix<>(ans.getMatrix().reshape(getRowSize(), getColumnSize()), ans.getIndices().reshape(getRowSize(), getColumnSize()));
  }

  /**
   * 行毎に昇順に並び替えた行列 と、 元の位置を示す指数({@link IntMatrix})を返します。
   * 
   * 自身が複素行列のときは、絶対値でソートします。
   * 
   * @return ソートされた結果
   */
  public final IndexedMatrix<IntNumber,IntMatrix> sortRowWise() {
    final IndexedIntElements ans = IntMatrixUtil.sortRowWise(this.elements);
    return new IndexedMatrix<>(new IntMatrix(ans.getElements()), new IntMatrix(ans.getIndices()));
  }

  /**
   * 列毎に昇順に並び替えた行列 と、 元の位置を示す指数({@link IntMatrix})を返します。
   * 
   * 自身が複素行列のときは、絶対値でソートします。
   * 
   * @return ソートされた結果
   */
  public final IndexedMatrix<IntNumber,IntMatrix> sortColumnWise() {
    final IndexedIntElements ans = IntMatrixUtil.sortColumnWise(this.elements);
    return new IndexedMatrix<>(new IntMatrix(ans.getElements()), new IntMatrix(ans.getIndices()));
  }

  /**
   * 全ての成分の和を求めます。
   * 
   * @return 全ての成分の和
   */
  public final int intSum() {
    return IntMatrixUtil.sum(this.elements);
  }

  /**
   * {@inheritDoc}
   */
  public final IntNumber sum() {
    return new IntNumber(intSum());
  }
  
  /**
   * 全ての成分の平均を求めます。
   * 
   * @return 全ての成分の平均
   */
  public final int intMean() {
    return IntMatrixUtil.sum(this.elements)/(getRowSize()*getColumnSize());
  }

  /**
   * {@inheritDoc}
   */
  public final IntNumber mean() {
    return new IntNumber(intMean());
  }
  
  /**
   * 全対角成分の和(トレース)を返します。
   * 
   * @return 対角成分の合計(トレース)
   */
  public final int intTrace() {
    return IntMatrixUtil.trace(this.elements);
  }

  /**
   * {@inheritDoc}
   */
  public final IntNumber trace() {
    return new IntNumber(intTrace());
  }

  /**
   * 行列式を返します。
   * 
   * @return 行列式
   */
  public int intDeterminant() {
    return IntMatrixUtil.determinant(this.elements);
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber determinant() {
    return new IntNumber(intDeterminant());
  }

  /**
   * 全ての成分の積を求めます。
   * 
   * @return 全ての成分の積
   */
  public final int intProduct() {
    return IntMatrixUtil.product(this.elements);
  }

  /**
   * 全ての成分の積を求めます。
   * 
   * @return 全ての成分の積
   */
  public final IntNumber product() {
    return new IntNumber(intProduct());
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix productRowWise() {
    return new IntMatrix(IntMatrixUtil.productRowWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix productColumnWise() {
    return new IntMatrix(IntMatrixUtil.productColumnWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix compareElementWise(final String operator, final int opponent) {
    return new BooleanMatrix(IntMatrixUtil.compareElements(this.elements, operator, opponent));
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix compareElementWise(final String operator, final double opponent) {
    return new BooleanMatrix(IntMatrixUtil.compareElements(this.elements, operator, opponent));
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix compareElementWise(final String operator, final IntNumber opponent) {
    return new BooleanMatrix(IntMatrixUtil.compareElements(this.elements, operator, opponent));
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final BooleanMatrix compareElementWise(final String operator, final IntMatrix opponent) {
//    if (opponent instanceof IntMatrix) {
//      return compareElementWise(operator, (IntMatrix)opponent);
//    }
//
//    throw new UnsupportedOperationException(Messages.getString("IntMatrix.19")); //$NON-NLS-1$
//  }

  /**
   * <code>opponent</code>と成分毎に<code>operator</code>で指定された演算子で比較し, {@link BooleanMatrix}で返します。
   * 
   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
   * @param opponent 比較対象
   * 
   * @return 各成分に比較結果が入ったBooleanMatrix
   */
  public final BooleanMatrix compareElementWise(final String operator, final IntMatrix opponent) {
    return new BooleanMatrix(IntMatrixUtil.compareElements(this.elements, operator, opponent.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMxFormat(final DataOutputStream output, final String name) throws IOException {
    IntMatrixUtil.writeMxFormat(this.elements, output, name);
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMxFormat(final File file, final String name) throws IOException {
    try (DataOutputStream output = new DataOutputStream(new FileOutputStream(file))) {
      writeMxFormat(output, name);
    }
  }

  /**
   * {@link org.mklab.nfc.matx.MatxMatrix#readMxFormat(InputStream)} から呼ばれる中間メソッドです。
   * 
   * このメソッドは直接使わず
   * 
   * <blockquote> {@link org.mklab.nfc.matrix.IntMatrix} A = {@link org.mklab.nfc.matx.MatxMatrix#readMxFormat(InputStream)}<br> </blockquote>
   * 
   * の形で使用してください。
   * 
   * @param input 入力ストリーム
   * 
   * @param head ヘッダー
   * 
   * @return mxファイルから読み込み,生成された行列
   * @exception IOException 入力ストリームから読み込めない場合
   */
  public static IntMatrix readMxFormat(final InputStream input, final MxDataHead head) throws IOException {
    return new IntMatrix(IntMatrixUtil.readMxFormat(input, head));
  }

  /**
   * {@inheritDoc}
   */
  public String toMmString() {
    return IntMatrixUtil.toMmString(this.elements);
  }

  /**
   * {@inheritDoc}
   */
  public final String toMmString( final String format) {
    return IntMatrixUtil.toMmString(this.elements);
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMmFormat(final File file, final String name) throws IOException {
    try (Writer output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")))) { //$NON-NLS-1$
      writeMmFormat(output, name, true);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMmFormat(final Writer output, final String name, final boolean withNewLine) throws IOException {
    final String newLine = System.getProperty("line.separator"); //$NON-NLS-1$

    final StringBuffer sb = new StringBuffer();
    if (name.length() != 0) {
      sb.append(name);
      sb.append(" = "); //$NON-NLS-1$
      sb.append(newLine);
    }

    sb.append(toMmString());

    if (withNewLine) {
      sb.append(";"); //$NON-NLS-1$
      sb.append(newLine);
      sb.append(newLine);
    }

    output.write(sb.toString());
    output.flush();
  }

  /**
   * 指定したファイルにMATフォーマットで保存します。
   * 
   * @param file ファイル
   * @exception IOException ファイルに出力できない場合
   * 
   */
  public final void writeMatFormat(final File file) throws IOException {
    try (Writer output = new OutputStreamWriter(new FileOutputStream(file), "UTF-8")) { //$NON-NLS-1$
      writeMatFormat(output);
    }
  }

  /**
   * データを出力ストリームにMATフォーマットで出力します。
   * 
   * @param output 出力ストリーム
   * @exception IOException 入出力エラーが発生した場合
   */
  public final void writeMatFormat(final Writer output) throws IOException {
    IntMatrixUtil.writeMatFormat(this.elements, output);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubMatrix(final int rowMin, final int rowMax, final int columnMin, final int columnMax, final Matrix<?,?> source) {
//    if (source instanceof IntMatrix) {
//      setSubMatrix(rowMin, rowMax, columnMin, columnMax, (IntMatrix)source);
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("IntMatrix.6")); //$NON-NLS-1$
//  }

  /**
   * 指定した成分に行列<code>source</code>を代入します。
   * 
   * @param rowMin 開始行番号(1から始まります)
   * @param rowMax 終了行番号(1から始まります)
   * @param columnMin 開始列番号(1から始まります)
   * @param columnMax 終了列番号(1から始まります)
   * @param source 代入する行列
   */
  public final void setSubMatrix(final int rowMin, final int rowMax, final int columnMin, final int columnMax, final IntMatrix source) {
    if ((rowMax - rowMin + 1) != source.getRowSize()) {
      throw new MatrixSizeException(Messages.getString("AbstractFundamentalMatrix.2")); //$NON-NLS-1$
    }
    if ((columnMax - columnMin + 1) != source.getColumnSize()) {
      throw new MatrixSizeException(Messages.getString("AbstractFundamentalMatrix.3")); //$NON-NLS-1$
    }

    IntMatrixUtil.setSubMatrix(this.elements, rowMin - 1, rowMax - 1, columnMin - 1, columnMax - 1, source.elements);
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix inverseElementWise() {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix isFiniteElementWise() {
    return new BooleanMatrix(IntMatrixUtil.isFiniteElementWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix isInfiniteElementWise() {
    return new BooleanMatrix(IntMatrixUtil.isInfiniteElementWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix isNanElementWise() {
    boolean[][] ans = IntMatrixUtil.isNanElementWise(this.elements);
    return new BooleanMatrix(ans);
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix addElementWise(final double scalar) {
    return addElementWise((int)scalar);
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix addElementWise(final IntNumber value) {
    return value.createGrid(getRowSize(), getColumnSize(), this.elements).addElementWise(value);
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix subtractElementWise(final double scalar) {
    return subtractElementWise((int)scalar);
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix subtractElementWise(final IntNumber value) {
    return value.createGrid(getRowSize(), getColumnSize(), this.elements).subtractElementWise(value);
  }

  /**
   * 成分毎の剰余の結果からなる行列を返します。
   * 
   * @param scalar 割る数
   * @return 成分毎剰余行列
   */
  public final IntMatrix remainderElementWise(final double scalar) {
    return new IntMatrix(IntMatrixUtil.remainderElementWise(this.elements, scalar));
  }

  /**
   * 成分毎の剰余の結果からなる行列を返します。
   * 
   * @param scalar 割る数
   * @return 成分毎剰余行列
   */
  public final IntMatrix remainderElementWise(final int scalar) {
    return new IntMatrix(IntMatrixUtil.remainderElementWise(this.elements, scalar));
  }

  /**
   * 成分毎の剰余の結果からなる行列を返します。
   * 
   * @param matrix 割る数の行列
   * @return 成分毎剰余行列
   */
  public final IntMatrix remainderElementWise(final IntMatrix matrix) {
    return new IntMatrix(IntMatrixUtil.remainderElementWise(this.elements, matrix.elements));
  }

  /**
   * 成分毎の剰余の結果からなる行列を返します。
   * 
   * @param matrix 割る数の行列
   * @return 成分毎剰余行列
   */
  public final IntMatrix remainderElementWise(final DoubleMatrix matrix) {
    return new IntMatrix(IntMatrixUtil.remainderElementWise(this.elements, matrix.getDoubleElements()));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix cumulativeSum() {
    return new IntMatrix(IntMatrixUtil.cumulativeSum(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix cumulativeSumRowWise() {
    return new IntMatrix(IntMatrixUtil.cumulativeSumRowWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix cumulativeSumColumnWise() {
    return new IntMatrix(IntMatrixUtil.cumulativeSumColumnWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix cumulativeProduct() {
    return new IntMatrix(IntMatrixUtil.cumulativeProduct(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix cumulativeProductRowWise() {
    return new IntMatrix(IntMatrixUtil.cumulativeProductRowWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix cumulativeProductColumnWise() {
    return new IntMatrix(IntMatrixUtil.cumulativeProductColumnWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix sumColumnWise() {
    return new IntMatrix(IntMatrixUtil.sumColumnWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix sumRowWise() {
    return new IntMatrix(IntMatrixUtil.sumRowWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix meanRowWise() {
    return new IntMatrix(IntMatrixUtil.meanRowWise(this.elements));
  }

  /**
   * 行毎のメジアンを成分とする列ベクトルを返します。
   * 
   * @return 中間値(メジアン)
   */
  public final IntMatrix medianRowWise() {
    return new IntMatrix(IntMatrixUtil.medianRowWise(this.elements));
  }

  /**
   * ベクトルの成分の分散を返します。
   * 
   * @return 分散
   */
  public final int intVariance() {
    return IntMatrixUtil.variance(this.elements);
  }

  /**
   * ベクトルの成分の分散を返します。
   * 
   * @return 分散
   */
  public final IntNumber variance() {
    return new IntNumber(intVariance());
  }

  /**
   * <code>opponent</code>との共分散行列を返します。
   * 
   * @param opponent 対となるベクトル
   * @return 共分散行列 (Covariance)
   */
  public final IntMatrix covariance(final IntMatrix opponent) {
    return new IntMatrix(IntMatrixUtil.covariance(this.elements, opponent.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix meanColumnWise() {
    return new IntMatrix(IntMatrixUtil.meanColumnWise(this.elements));
  }
  
  /**
   * 列毎のメジアンを成分とする行ベクトルを返します。
   * 
   * @return 中間値(メジアン)
   */
  public final IntMatrix medianColumnWise() {
    return new IntMatrix(IntMatrixUtil.medianColumnWise(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix ceilElementWise() {
    return createClone();
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix floorElementWise() {
    return createClone();
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix fixElementWise() {
    return createClone();
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix roundElementWise() {
    return createClone();
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix roundToZeroElementWise( final double tolerance) {
    return createClone();
  }

  /**
   * {@inheritDoc}
   */
  public IntMatrix roundToZeroElementWise() {
    return roundToZeroElementWise(DoubleNumberUtil.EPS);
  }
  
  /**
   * 各成分の最大値をもつ行列を生成して返します。
   * 
   * @param opponent 比較対象
   * @return 各成分の最大値をもつ行列
   */
  public final IntMatrix maxElementWise(final IntMatrix opponent) {
    if (isSameSize(opponent) == false) {
      throw new MatrixSizeException(this, opponent, MatrixSizeException.NOT_SAME_SIZE);
    }
    return new IntMatrix(IntMatrixUtil.maxElementWise(this.elements, opponent.elements));
  }

  /**
   * 各成分の最小値をもつ行列を生成して返します。
   * 
   * @param opponent 比較対象
   * @return 各成分の最小値をもつ行列
   */
  public final IntMatrix minElementWise(final IntMatrix opponent) {
    if (isSameSize(opponent) == false) {
      throw new MatrixSizeException(this, opponent, MatrixSizeException.NOT_SAME_SIZE);
    }

    return new IntMatrix(IntMatrixUtil.minElementWise(this.elements, opponent.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix createUnit(final int rowSize, final int columnSize) {
    return new IntMatrix(IntMatrixUtil.unit(rowSize, columnSize));
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix createZero(final int rowSize, final int columnSize) {
    return new IntMatrix(rowSize, columnSize);
  }

  /**
   * {@inheritDoc}
   */
  public final IntMatrix createOnes(final int rowSize, final int columnSize) {
    return new IntMatrix(IntMatrixUtil.ones(rowSize, columnSize));
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isComplex() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isReal() {
    return true;
  }
  
  /**
   * 警告を出力します。
   * 
   * @param message メッセージ
   */
  private void warning(final String message) {
    System.err.println(message);
  }

//  /**
//   * @return
//   */
//  public final IntMatrix getImaginaryPart() {
//    return createZero(getRowSize(), getColumnSize());
//  }

//  /**
//   * @return
//   */
//  public final IntMatrix getRealPart() {
//    return clone();
//  }

//  /**
//   * @param realPart
//   */
//  public final void setRealPart( final Matrix<?,?> realPart) {
//    throw new UnsupportedOperationException(Messages.getString("IntMatrix.24")); //$NON-NLS-1$
//  }
//
//  /**
//   * @param realPart
//   */
//  public final void setRealPart( final IntMatrix realPart) {
//    throw new UnsupportedOperationException(Messages.getString("IntMatrix.25")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setRealPart( final DoubleMatrix realPart) {
//    throw new UnsupportedOperationException(Messages.getString("IntMatrix.26")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setRealPart( final BaseMatrix<?, ?> realPart) {
//    throw new UnsupportedOperationException(Messages.getString("IntMatrix.27")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setImaginaryPart( final Matrix<?,?> imaginaryPart) {
//    throw new UnsupportedOperationException(Messages.getString("IntMatrix.28")); //$NON-NLS-1$
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final void setImaginaryPart( final IntMatrix imaginaryPart) {
//    throw new UnsupportedOperationException(Messages.getString("IntMatrix.29")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setImaginaryPart( final DoubleMatrix imaginaryPart) {
//    throw new UnsupportedOperationException(Messages.getString("IntMatrix.30")); //$NON-NLS-1$
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final void setImaginaryPart( final BaseMatrix<?, ?> imaginaryPart) {
//    throw new UnsupportedOperationException(Messages.getString("IntMatrix.31")); //$NON-NLS-1$
//  }

//  /**
//   * @return
//   */
//  public DoubleComplexMatrix toComplex() {
//    return new DoubleComplexMatrix(this);
//  }
}