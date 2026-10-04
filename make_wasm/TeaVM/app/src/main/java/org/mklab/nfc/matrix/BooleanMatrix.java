/*
 * $Id: BooleanMatrix.java,v 1.125 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.matrix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import org.mklab.nfc.matx.MxDataHead;
import org.mklab.nfc.scalar.Scalar;


/**
 * booleanを成分とする行列を表すクラスです。
 * 
 * <p>主に、行列の成分毎の比較 (". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=") の結果を保持するために使用されます。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.125 $
 */
public class BooleanMatrix extends AbstractArray<BooleanMatrix> {

  /** シリアルバージョン。 */
  private static final long serialVersionUID = -6028765140613873578L;

  /** 成分の出力フォーマット。 */
  private static String defaultElementFormat = "%9s"; //$NON-NLS-1$

  /** 行列の成分。 */
  private boolean[][] elements;

  /**
   * 成分のデフォルト出力フォーマットを設定します。
   * 
   * @param format 成分のデフォルト出力フォーマット
   */
  public static void setDefaultElementFormat(final String format) {
    BooleanMatrix.defaultElementFormat = format;
  }

  /**
   * 成分のデフォルト出力フォーマットを返します。
   * 
   * @return 成分のデフォルト出力フォーマット
   */
  public static String getDefaultElementFormat() {
    return BooleanMatrix.defaultElementFormat;
  }

  //  /**
  //   * 新しく生成された<code>BooleanMatrix</code>オブジェクトを初期化します。
  //   * 
  //   * <p>0*0のBooleanMatrixを生成します。
  //   */
  //  public BooleanMatrix() {
  //    this(0, 0);
  //  }

  /**
   * 新しく生成された<code>BooleanMatrix</code>オブジェクトを初期化します。
   * 
   * <p><code>elements</code>で指定した成分をもつ<code>BooleanMatrix</code>行列を生成します。
   * 
   * @param elements ベクトルの成分をもつ配列
   */
  public BooleanMatrix(final boolean[] elements) {
    this(elements.length == 0 ? 0 : 1, elements.length, elements.length == 0 ? new boolean[0][0] : new boolean[][] {elements});
  }

  /**
   * <code>rowSize</code>*<code>columSize</code>の<code>boolean</code>行列(初期値は<code>false</code>)を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   */
  public BooleanMatrix(final int rowSize, final int columnSize) {
    this(rowSize, columnSize, new boolean[rowSize][columnSize]);
  }

  /**
   * <code>elements</code>で与えられた成分をもつ<code>rowSize</code>*<code>columSize</code> の<code>boolean</code>行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 行列の成分をもつ配列
   * 
   */
  public BooleanMatrix(final int rowSize, final int columnSize, final boolean[][] elements) {
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
    setElementFormat(BooleanMatrix.defaultElementFormat);
  }

  /**
   * <code>elements</code>で与えられた成分をもつ<code>boolean</code>行列を生成します。
   * 
   * @param elements 行列の成分をもつ配列
   */
  public BooleanMatrix(final boolean[][] elements) {
    this(elements.length, (elements.length == 0 || elements[0] == null) ? 0 : elements[0].length, elements);
  }

//  /**
//   * <code>matrix</code>と同サイズの<code>boolean</code>行列を生成します。
//   * 
//   * <p>成分は<code>matrix</code>の各成分を調べ、零ならば<code>false</code>、零以外ならば<code>true</code>となります。
//   * 
//   * @param matrix 成分をもつ行列
//   */
//  public BooleanMatrix(final BaseMatrix<?,?> matrix) {
//    this(matrix.getRowSize(), matrix.getColumnSize());
//
////    if (matrix instanceof IntMatrix) {
////      final IntMatrix zero = new IntMatrix(getRowSize(), getColumnSize());
////      copy(((IntMatrix)matrix).compareElementWise(".!=", zero)); //$NON-NLS-1$
////      return;
////    } 
////    
////    if (matrix instanceof DoubleMatrix) {
////      final DoubleMatrix zero = new DoubleMatrix(getRowSize(), getColumnSize());
////      copy(((DoubleMatrix)matrix).compareElementWise(".!=", zero)); //$NON-NLS-1$
////      return;
////    } 
//    
//    if (matrix instanceof BaseMatrix<?, ?>) {
//      final BaseMatrix<?, ?> zero = ((BaseMatrix<?, ?>)matrix).createZero(getRowSize(), getColumnSize());
//      copy(((BaseMatrix<?, ?>)matrix).compareElementWise(".!=", zero)); //$NON-NLS-1$
//      return;
//    }
//
//    throw new UnsupportedOperationException(Messages.getString("BooleanMatrix.4")); //$NON-NLS-1$
//  }

  /**
   * <code>matrix</code>と同サイズの<code>boolean</code>行列を生成します。
   * 
   * <p>成分は<code>matrix</code>の各成分を調べ、零ならば<code>false</code>、零以外ならば<code>true</code>となります。
   * 
   * @param matrix 成分をもつ行列
   */
  public BooleanMatrix(final IntMatrix matrix) {
    this(matrix.getRowSize(), matrix.getColumnSize());
    final IntMatrix zero = new IntMatrix(getRowSize(), getColumnSize());
    copy(matrix.compareElementWise(".!=", zero)); //$NON-NLS-1$
  }

  /**
   * <code>matrix</code>と同サイズの<code>boolean</code>行列を生成します。
   * 
   * <p>成分は<code>matrix</code>の各成分を調べ、零ならば<code>false</code>、零以外ならば<code>true</code>となります。
   * 
   * @param matrix 成分をもつ行列
   */
  public BooleanMatrix(final DoubleMatrix matrix) {
    this(matrix.getRowSize(), matrix.getColumnSize());
    final DoubleMatrix zero = new DoubleMatrix(getRowSize(), getColumnSize());
    copy(matrix.compareElementWise(".!=", zero)); //$NON-NLS-1$
  }

  /**
   * <code>matrix</code>と同サイズの<code>boolean</code>行列を生成します。
   * 
   * <p>成分は<code>matrix</code>の各成分を調べ、零ならば<code>false</code>、零以外ならば<code>true</code>となります。
   * @param <S> スカラーの型 
   * @param <M> 行列の型
   * @param matrix 成分をもつ行列
   * @return  <code>matrix</code>と同サイズの<code>boolean</code>行列
   */
  public static <S extends Scalar<S,M>, M extends BaseMatrixOperator<S,M>> BooleanMatrix create(final M matrix) {
    final BooleanMatrix ans = new BooleanMatrix(matrix.getRowSize(), matrix.getColumnSize());
    final M zero = matrix.createZero(matrix.getRowSize(), matrix.getColumnSize());
    ans.copy(matrix.compareElementWise(".!=", zero)); //$NON-NLS-1$
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone() {
    final BooleanMatrix ans = (BooleanMatrix)super.clone();
    ans.elements = BooleanMatrixUtil.clone(this.elements);
    return ans;
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

    final BooleanMatrix m = (BooleanMatrix)opponent;

    if (isSameSize(m) == false) {
      return false;
    }

    return BooleanMatrixUtil.equals(this.elements, m.elements);
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
        hashCode = prime * hashCode + (this.elements[i0][i1] ? 1231 : 1237);
      }
    }
    return hashCode;
  }

  /**
   * <code>row</code>行<code>column</code>列の成分を返します。
   * 
   * @param row 行番号
   * @param column 列番号
   * @return row行column列の成分
   */
  public final boolean getElement(final int row, final int column) {
    return this.elements[row - 1][column - 1];
  }

  /**
   * 成分を行毎に数え、<code>index</code>番目の成分を返します。
   * 
   * @param index 成分の番号
   * @return 成分
   */
  public final boolean getElement(final int index) {
    return this.elements[(index - 1) / getColumnSize()][(index - 1) % getColumnSize()];
  }
  
  /**
   * 全成分を <code>int</code> の2次元配列として返します。
   * 
   * @return 全成分
   */
  final public boolean[][] getElements() {
    return this.elements;
  }


  /**
   * <code>row</code>行<code>column</code>列に<code>value</code>を代入します。
   * 
   * @param row 行番号
   * @param column 列番号
   * @param value 変更値
   */
  public final void setElement(final int row, final int column, final boolean value) {
    this.elements[row - 1][column - 1] = value;
  }

  /**
   * 成分を行毎に数え<code>index</code>で指定した位置に<code>value</code>を代入します。
   * 
   * @param index 成分の番号
   * @param value 設定する値
   */
  public final void setElement(final int index, final boolean value) {
    this.elements[(index - 1) / getColumnSize()][(index - 1) % getColumnSize()] = value;
  }

  /**
   * 全成分を調べ、<code>true</code>が1個でもあれば<code>true</code>、そうでなければ<code>false</code> を返します。
   * 
   * @return 全成分を調べ、<code>true</code>が1個でもあれば<code>true</code>、そうでなければ<code>false </code>
   */
  public final boolean anyTrue() {
    return BooleanMatrixUtil.anyTrue(this.elements);
  }

  /**
   * 行毎の成分を調べ、行に<code>true</code>が1個でもあれば<code>true</code>、そうでなければ<code>false</code>を対応させ、 <code>boolean</code>値を含む{@link BooleanMatrix}を返します。
   * 
   * @return 調査の結果を成分とするBooleanMatrix
   */
  public final BooleanMatrix anyTrueRowWise() {
    return new BooleanMatrix(BooleanMatrixUtil.anyTrueRowWise(this.elements));
  }

  /**
   * 列毎の成分を調べ、行に<code>true</code>が1個でもあれば<code>true</code>、 そうでなければ<code>false</code>を対応させ、 <code>boolean</code>値を含む {@link BooleanMatrix}を返します。
   * 
   * @return 調査の結果を成分とするBooleanMatrix
   */
  public final BooleanMatrix anyTrueColumnWise() {
    return new BooleanMatrix(BooleanMatrixUtil.anyTrueColumnWise(this.elements));
  }

  /**
   * 全成分を調べ、全成分が<code>true</code>ならばtrue、そうでなければ<code>false</code>を返します。
   * 
   * @return 全成分が<code>true</code>ならばtrue、そうでなければ<code>false</code>
   */
  public final boolean allTrue() {
    return BooleanMatrixUtil.allTrue(this.elements);
  }

  /**
   * 成分を行毎に調べ、行の全成分が<code>true</code>ならば<code>true</code>、 そうでなければ<code>false</code>を対応させ、 行毎の<code>boolean</code>値からなる {@link BooleanMatrix}を返します。
   * 
   * @return 調査の結果を成分とするBooleanMatrix
   */
  public final BooleanMatrix allTrueRowWise() {
    return new BooleanMatrix(BooleanMatrixUtil.allTrueRowWise(this.elements));
  }

  /**
   * 成分を列毎に調べ、列の全成分が<code>true</code>ならば<code>true</code>、 そうでなければ<code>false</code>を対応させ、 行毎の<code>boolean</code>値からなる {@link BooleanMatrix}を返します。
   * 
   * @return 調査の結果を成分とするBooleanMatrix
   */
  public final BooleanMatrix allTrueColumnWise() {
    return new BooleanMatrix(BooleanMatrixUtil.allTrueColumnWise(this.elements));
  }

  /**
   * 各成分の否定(<code>true</code>ならば<code>false</code>、<code>false</code>ならば<code> true</code>) を成分にもつ{@link BooleanMatrix}行列を返します。
   * 
   * @return 調査の結果を成分とするBooleanMatrix
   */
  public final BooleanMatrix notElementWise() {
    return new BooleanMatrix(BooleanMatrixUtil.notElementWise(this.elements));
  }

  /**
   * <code>value</code>の各成分との論理積を成分にもつ{@link BooleanMatrix}返します。
   * 
   * @param value 演算の対象
   * 
   * @return 演算の結果を成分とするBooleanMatrix
   */
  public final BooleanMatrix andElementWise(final BooleanMatrix value) {
    if (isSameSize(value) == false) {
      throw new MatrixSizeException(this, value, MatrixSizeException.NOT_SAME_SIZE);
    }

    return new BooleanMatrix(BooleanMatrixUtil.andElementWise(this.elements, value.elements));
  }

  /**
   * <code>value</code>各成分との論理積を成分にもつ{@link BooleanMatrix}を返します。
   * 
   * @param value 全ての演算に用いるboolean
   * 
   * @return 演算の結果を成分とするBooleanMatrix
   */
  public final BooleanMatrix andElementWise(final boolean value) {
    return new BooleanMatrix(BooleanMatrixUtil.andElementWise(this.elements, value));
  }

  /**
   * <code>value</code>の各成分との論理和を成分にもつBooleanMatrixを返します。
   * 
   * @param value 演算の対象
   * @return 演算の結果を成分とするBooleanMatrix
   */
  public final BooleanMatrix orElementWise(final BooleanMatrix value) {
    if (isSameSize(value) == false) {
      throw new MatrixSizeException(this, value, MatrixSizeException.NOT_SAME_SIZE);
    }

    return new BooleanMatrix(BooleanMatrixUtil.orElementWise(this.elements, value.elements));
  }

  /**
   * <code>value</code>の各成分との論理和を成分にもつ{@link BooleanMatrix}を返します。
   * 
   * @param value 全ての演算に用いるboolean
   * @return 演算の結果を成分とするBooleanMatrix
   */
  public final BooleanMatrix orElementWise(final boolean value) {
    return new BooleanMatrix(BooleanMatrixUtil.orElementWise(this.elements, value));
  }

  /**
   * <code>value</code>の各成分との排他的論理和を成分にもつ{@link BooleanMatrix}を返します。
   * 
   * @param value 演算の対象
   * 
   * @return 演算の結果を成分とするBooleanMatrix
   */
  public final BooleanMatrix exorElementWise(final BooleanMatrix value) {
    if (isSameSize(value) == false) {
      throw new MatrixSizeException(this, value, MatrixSizeException.NOT_SAME_SIZE);
    }
    return new BooleanMatrix(BooleanMatrixUtil.exorElementWise(this.elements, value.elements));
  }

  /**
   * <code>value</code>の各成分との排他的論理和を成分にもつ{@link BooleanMatrix}を返します。
   * 
   * @param value 全ての演算に用いるboolean
   * @return 演算の結果を成分とするBooleanMatrix
   */
  public final BooleanMatrix exorElementWise(final boolean value) {
    return new BooleanMatrix(BooleanMatrixUtil.exorElementWise(this.elements, value));
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
    BooleanMatrixUtil.print(this.elements, output, getElementFormat(), maxColumnSize);
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix reshape(final int newRowSize, final int newColumnSize) {
    if (getRowSize() * getColumnSize() != newRowSize * newColumnSize) {
      throw new MatrixSizeException(""); //$NON-NLS-1$
    }
    return new BooleanMatrix(BooleanMatrixUtil.reshape(this.elements, newRowSize, newColumnSize));
  }

  /**
   * <code>true</code>である成分の数を返します。
   * 
   * @return trueの数
   */
  public final int getNumberOfTrue() {
    return BooleanMatrixUtil.getNumberOfTrue(this.elements);
  }

  /**
   * <code>true</code>の位置を順にもつ整数ベクトルを返します。
   * 
   * @return trueの位置を順にもつ整数ベクトル
   */
  public final IntMatrix find() {
    return new IntMatrix(BooleanMatrixUtil.find(this.elements));
  }

  /**
   * <code>size</code>*<code>size</code>の単位行列(対角成分のみtrue)を生成します。
   * 
   * @param size サイズ
   * @return size*sizeの単位行列
   */
  public static BooleanMatrix unit(final int size) {
    return BooleanMatrix.unit(size, size);
  }

  /**
   * <code>rowSize</code>*<code>columnSize</code>の単位行列(対角成分のみtrue)を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return rowSize*columnSizeの単位行列
   */
  public static BooleanMatrix unit(final int rowSize, final int columnSize) {
    return new BooleanMatrix(BooleanMatrixUtil.unit(rowSize, columnSize));
  }

  /**
   * 行列<code>matrix</code>と同サイズの単位行列(対角成分のみ<code>true</code>)を生成します。
   * 
   * @param matrix 行列
   * @return 単位行列
   */
  public static BooleanMatrix unit(final Grid matrix) {
    return BooleanMatrix.unit(matrix.getRowSize(), matrix.getColumnSize());
  }

  /**
   * 行列<code>matrix</code>と同サイズで全成分<code>true</code>の行列を生成します。
   * 
   * @param matrix 行列
   * @return mと同サイズで全成分trueの行列
   */
  public static BooleanMatrix ones(final Grid matrix) {
    return BooleanMatrix.ones(matrix.getRowSize(), matrix.getColumnSize());
  }

  /**
   * <code>size</code>*<code>size</code>の全成分が<code>true</code>である行列を生成します。
   * 
   * @param size サイズ指定
   * @return rowSize*colSize全成分trueの行列
   */
  public static BooleanMatrix ones(final int size) {
    return BooleanMatrix.ones(size, size);
  }

  /**
   * <code>rowSize</code>*<code>columnSize</code>の全成分が<code>true</code> である行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return rowSize*columnSizeの全成分trueの行列
   */
  public static BooleanMatrix ones(final int rowSize, final int columnSize) {
    return new BooleanMatrix(BooleanMatrixUtil.ones(rowSize, columnSize));
  }

  /**
   * 行列<code>block</code>の<code>rowNum</code> * <code>columnNum</code>の全成分が<code>true</code> である行列を生成します。
   * 
   * @param block 行列
   * @param rowNumber 行方向の倍数
   * @param columnNumber 列方向の倍数
   * @return <code>block</code>の<code>rowNum</code> * <code>columnNum</code>倍の単位行列
   */
  public static BooleanMatrix ones(final int rowNumber, final int columnNumber, final Grid block) {
    return BooleanMatrix.ones(block.getRowSize() * rowNumber, block.getColumnSize() * columnNumber);
  }

  /**
   * 行列<code>block</code>の<code>rowNum</code> * <code>columnNum</code>倍の単位行列を生成します。
   * 
   * @param block 行列
   * @param rowNumber 行方向の倍数
   * @param columnNumber 列方向の倍数
   * @return <code>block</code>の<code>rowNum</code> * <code>columnNum</code>倍の単位行列
   */
  public static BooleanMatrix unit(final int rowNumber, final int columnNumber, final Grid block) {
    return BooleanMatrix.unit(block.getRowSize() * rowNumber, block.getColumnSize() * columnNumber);
  }

  /**
   * 対角行列を生成します。
   * 
   * @param diagonalElements 対角成分
   * @return 対角行列
   */
  public static BooleanMatrix diagonal(final boolean[] diagonalElements) {
    return new BooleanMatrix(BooleanMatrixUtil.vectorToDiagonal(diagonalElements));
  }

  /**
   * MMフォーマットの文字列を生成します。
   * 
   * @return MMフォーマットの文字列
   */
  public final String toMmString() {
    return BooleanMatrixUtil.toMmString(this.elements);
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix transpose() {
    return new BooleanMatrix(BooleanMatrixUtil.transpose(this.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix vectorToDiagonal() {
    if (getRowSize() == 1) {
      final boolean[][] ans = BooleanMatrixUtil.vectorToDiagonal(this.elements[0]);
      return new BooleanMatrix(ans);
    }

    if (getColumnSize() == 1) {
      final boolean[][] vector = BooleanMatrixUtil.transpose(this.elements);
      final boolean[][] ans = BooleanMatrixUtil.vectorToDiagonal(vector[0]);
      return new BooleanMatrix(ans);
    } 

    throw new MatrixSizeException(Messages.getString("BooleanMatrix.9")); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix diagonalToVector() {
    return new BooleanMatrix(BooleanMatrixUtil.diagonalToVector(this.elements)).transpose();
  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  protected final void setSubMatrix(final int rowTo, final int columnTo, final Array<?> source, final int rowMin, final int rowMax, final int columnMin, final int columnMax) {
//    if (!(source instanceof BooleanMatrix)) {
//      throw new IllegalArgumentException(Messages.getString("BooleanMatrix.10")); //$NON-NLS-1$
//    }
//
//    BooleanMatrixUtil.setSubMatrix(this.elements, rowTo, columnTo, ((BooleanMatrix)source).elements, rowMin, rowMax, columnMin, columnMax);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubMatrix(final int rowMin, final int rowMax, final int columnMin, final int columnMax, final BooleanMatrix source) {
//   if (source instanceof BooleanMatrix) {
//     setSubMatrix(rowMin, rowMax, columnMin, columnMax, (BooleanMatrix)source);
//     return;
//   }
//   
//   throw new IllegalArgumentException(Messages.getString("BooleanMatrix.11")); //$NON-NLS-1$
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
  public final void setSubMatrix(final int rowMin, final int rowMax, final int columnMin, final int columnMax, final BooleanMatrix source) {
    if ((rowMax - rowMin + 1) != source.getRowSize()) {
      throw new MatrixSizeException(Messages.getString("AbstractArray.0")); //$NON-NLS-1$
    }
    if ((columnMax - columnMin + 1) != source.getColumnSize()) {
      throw new MatrixSizeException(Messages.getString("AbstractArray.1")); //$NON-NLS-1$
    }
    BooleanMatrixUtil.setSubMatrix(this.elements, rowMin-1, rowMax-1, columnMin-1, columnMax-1, source.elements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubMatrix(final IntMatrix rowIndex, final int columnMin, final int columnMax, final Array<?> m) {
//    if (m instanceof BooleanMatrix) {
//      setSubMatrix(rowIndex, columnMin, columnMax, (BooleanMatrix)m);
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("BooleanMatrix.11")); //$NON-NLS-1$
//  }

  /**
   * <code>rowIndex</code>で指定した行の<code>columnMin</code>列から<code>columnMax</code> 列までの 行列<code>source</code>を代入します。
   * 
   * @param rowIndex 行番号を指定する指数
   * @param columnMin 列の始まり
   * @param columnMax 列の終わり
   * @param source 代入する行列
   */
  public final void setSubMatrix(final IntMatrix rowIndex, final int columnMin, final int columnMax, final BooleanMatrix source) {
    if (rowIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BooleanMatrix.12")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    BooleanMatrixUtil.setSubMatrix(this.elements, index, columnMin - 1, columnMax - 1, source.elements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubMatrix(final int rowMin, final int rowMax, final IntMatrix columnIndex, final Array<?> source) {
//    if (source instanceof BooleanMatrix) {
//      setSubMatrix(rowMin, rowMax, columnIndex, (BooleanMatrix)source);
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("BooleanMatrix.13")); //$NON-NLS-1$
//  }

  /**
   * <code>rowMin</code>列から<code>rowMax</code>列目の成分の<code>columnIndex</code> で指定された行に、 行列<code>source</code>の成分を代入します。
   * 
   * @param rowMin 行の始まり
   * @param rowMax 行の終わり
   * @param columnIndex 列指定ベクトル
   * @param source 代入する行列
   */
  public final void setSubMatrix(final int rowMin, final int rowMax, final IntMatrix columnIndex, final BooleanMatrix source) {
    if (columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BooleanMatrix.14")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    BooleanMatrixUtil.setSubMatrix(this.elements, rowMin - 1, rowMax - 1, index, source.elements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubMatrix(final IntMatrix rowIndex, final IntMatrix columnIndex, final Array<?> source) {
//    if (source instanceof BooleanMatrix) {
//      setSubMatrix(rowIndex, columnIndex, (BooleanMatrix)source);
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("BooleanMatrix.15")); //$NON-NLS-1$
//  }

  /**
   * <code>rowIndex</code>で指定した行の<code>columnIndex</code>で指定した列に行列<code>source</code>を代入します。
   * 
   * @param rowIndex 行番号を指定する指数
   * @param columnIndex 列番号を指定する指数
   * @param source 代入する行列
   */
  public final void setSubMatrix(final IntMatrix rowIndex, final IntMatrix columnIndex, final BooleanMatrix source) {
    if (rowIndex.getRowSize() != 1 || columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BooleanMatrix.16")); //$NON-NLS-1$
    }

    final int[] rowIdx = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    final int[] columnIdx = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    BooleanMatrixUtil.setSubMatrix(this.elements, rowIdx, columnIdx, source.elements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setSubVector(final IntMatrix index, final Array<?> source) {
//    if (source instanceof BooleanMatrix) {
//      setSubVector(index, (BooleanMatrix)source);
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("BooleanMatrix.17")); //$NON-NLS-1$
//  }

  /**
   * <code>index</code>で指定した各成分に行列<code>source</code>の成分を代入します。
   * 
   * @param index 成分の番号を指定する指数
   * @param source 代入するベクトル
   */
  public final void setSubVector(final IntMatrix index, final BooleanMatrix source) {
    if (index.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BooleanMatrix.18")); //$NON-NLS-1$
    }

    final int[] idx = IntMatrixUtil.decrement(index.getIntElements()[0]);
    BooleanMatrixUtil.setElements(this.elements, idx, source.elements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void copy(final BooleanMatrix source) {
//    if (source instanceof BooleanMatrix) {
//      copy((BooleanMatrix)source);
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("BooleanMatrix.19")); //$NON-NLS-1$
//  }

  /**
   * 各成分に行列<code>source</code>の各成分をコピーします。
   * 
   * @param source 行列
   */
  public final void copy(final BooleanMatrix source) {
    BooleanMatrixUtil.copy(source.elements, this.elements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final BooleanMatrix compareElementWise(final String operator, final BooleanMatrix opponent) {
//    if (opponent instanceof BooleanMatrix) {
//      return compareElementWise(operator, (BooleanMatrix)opponent);
//    }
//
//    throw new IllegalArgumentException(Messages.getString("BooleanMatrix.20")); //$NON-NLS-1$
//  }

  /**
   * <code>opponent</code>の各成分と<code>operator</code>で指定された演算子で比較し, {@link BooleanMatrix}で返します。
   * 
   * @param operator 比較演算子(".==", ".!=")
   * @param opponent 比較対象
   * 
   * @return 各成分に比較結果が入ったBooleanMatrix
   */
  public final BooleanMatrix compareElementWise(final String operator, final BooleanMatrix opponent) {
    return new BooleanMatrix(BooleanMatrixUtil.compareElements(this.elements, operator, opponent.elements));
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final BooleanMatrix appendDown(final Array<?> value) {
//    if (value instanceof BooleanMatrix) {
//      return appendDown((BooleanMatrix)value);
//    }
//
//    throw new UnsupportedOperationException();
//  }

  /**
   * 下側に行列<code>value</code>を連結した行列を返します。
   * 
   * @param value つける行列
   * @return 下側にmを連結した行列
   */
  public final BooleanMatrix appendDown(final BooleanMatrix value) {
    if (getColumnSize() != value.getColumnSize() && getColumnSize() != 0 && value.getColumnSize() != 0) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_COLUMN_NUMBER);
    }

    return new BooleanMatrix(BooleanMatrixUtil.appendDown(this.elements, value.elements));
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final BooleanMatrix appendRight(final Array<?> value) {
//    if (value instanceof BooleanMatrix) {
//      return appendRight((BooleanMatrix)value);
//    }
//
//    throw new UnsupportedOperationException();
//  }

  /**
   * 右側に<code>value</code>を連結した行列を返します。
   * 
   * @param value 連結するBooleanMatrix
   * @return 右側に<code>value</code>を連結した行列
   */
  public final BooleanMatrix appendRight(final BooleanMatrix value) {
    if (hasSameRowSize(value) == false && getRowSize() != 0 && value.getRowSize() != 0) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_ROW_NUMBER);
    }

    return new BooleanMatrix(BooleanMatrixUtil.appendRight(this.elements, value.elements));
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix getSubMatrix(final int rowMin, final int rowMax, final int columnMin, final int columnMax) {
    return new BooleanMatrix(BooleanMatrixUtil.getSubMatrix(this.elements, rowMin - 1, rowMax - 1, columnMin - 1, columnMax - 1));
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix getSubMatrix(final int rowMin, final int rowMax, final IntMatrix columnIndex) {
    int rowsize = columnIndex.getRowSize();

    if (rowsize != 1) {
      throw new MatrixSizeException(Messages.getString("BooleanMatrix.21")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    return new BooleanMatrix(BooleanMatrixUtil.getSubMatrix(this.elements, rowMin - 1, rowMax - 1, index));
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix getSubMatrix(final IntMatrix rowIndex, final IntMatrix columnIndex) {
    if (rowIndex.getRowSize() != 1 || columnIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BooleanMatrix.22")); //$NON-NLS-1$
    }

    final int[] rowIdx = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    final int[] columnIdx = IntMatrixUtil.decrement(columnIndex.getIntElements()[0]);
    return new BooleanMatrix(BooleanMatrixUtil.getSubMatrix(this.elements, rowIdx, columnIdx));
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix getSubMatrix(final IntMatrix rowIndex, final int columnMin, final int columnMax) {
    int rowsize = rowIndex.getRowSize();

    if (rowsize != 1) {
      throw new MatrixSizeException(Messages.getString("BooleanMatrix.23")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    return new BooleanMatrix(BooleanMatrixUtil.getSubMatrix(this.elements, index, columnMin - 1, columnMax - 1));
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix getSubVector(final IntMatrix index) {
    if (index.getRowSize() == 0) {
      return new BooleanMatrix(new boolean[0]);      
    }
    
    if (index.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BooleanMatrix.26")); //$NON-NLS-1$
    }

    final int[] idx = IntMatrixUtil.decrement(index.getIntElements()[0]);

    if (getColumnSize() == 1) {
      final boolean[] mat = (transpose().elements)[0];
      return new BooleanMatrix(BooleanMatrixUtil.getSubVector(mat, idx)).transpose();
    }
    
    if (getRowSize() == 1) {
      return new BooleanMatrix(BooleanMatrixUtil.getSubVector(this.elements[0], idx));
    }

    throw new MatrixSizeException(MatrixSizeException.NOT_A_VECTOR_MATRIX);
  }

  /**
   * {@inheritDoc}
   */
  public final void exchangeColumn(final int column1, final int column2) {
    BooleanMatrixUtil.exchangeColumn(this.elements, column1 - 1, column2 - 1);
  }

  /**
   * {@inheritDoc}
   */
  public final void exchangeRow(final int row1, final int row2) {
    BooleanMatrixUtil.exchangeRow(this.elements, row1 - 1, row2 - 1);
  }

  /**
   * {@inheritDoc}
   */
  public final BooleanMatrix resize(final int newRowSize, final int newColSize) {
    this.elements = BooleanMatrixUtil.resize(this.elements, newRowSize, newColSize);
    setRowSize(newRowSize);
    setColumnSize(newColSize);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public final void removeColumnVectors(final int columnMin, final int columnMax) {
    this.elements = BooleanMatrixUtil.removeColumnVectors(this.elements, columnMin - 1, columnMax - 1);
    setColumnSize(getColumnSize() - (columnMax - columnMin + 1));
  }

  /**
   * {@inheritDoc}
   */
  public final void removeColumnVectors(final IntMatrix index) {
    if (index.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BooleanMatrix.27")); //$NON-NLS-1$
    }

    final int[] idx = IntMatrixUtil.decrement(index.getIntElements()[0]);
    final boolean[][] ans = BooleanMatrixUtil.removeColumnVectors(this.elements, idx);

    this.elements = ans;
    int newColumnSize = ans.length == 0 ? 0 : ans[0].length;
    setColumnSize(newColumnSize);
  }

  /**
   * {@inheritDoc}
   */
  public final void removeRowVectors(final int rowMin, final int rowMax) {
    this.elements = BooleanMatrixUtil.removeRowVectors(this.elements, rowMin - 1, rowMax - 1);
    setRowSize(getRowSize() - (rowMax - rowMin + 1));
  }

  /**
   * {@inheritDoc}
   */
  public final void removeRowVectors(final IntMatrix rowIndex) {
    if (rowIndex.getRowSize() != 1) {
      throw new MatrixSizeException(Messages.getString("BooleanMatrix.28")); //$NON-NLS-1$
    }

    final int[] index = IntMatrixUtil.decrement(rowIndex.getIntElements()[0]);
    final boolean[][] ans = BooleanMatrixUtil.removeRowVectors(this.elements, index);

    this.elements = ans;
    setRowSize(ans.length);
  }

  /**
   * <code>scalar</code>乗(<code>this</code> <sup><code>scalar</code> </sup>)を返します。
   * 
   * @param scalar 指数
   * @return scalar乗
   */
  public final BooleanMatrix power(final int scalar) {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    return new BooleanMatrix(BooleanMatrixUtil.power(this.elements, scalar));
  }

  /**
   * 行列<code>value</code>との和(成分毎の論理和)を返します。
   * 
   * @param value 行列
   * @return <code>value</code>との和(成分毎の論理和)
   */
  public final BooleanMatrix add(final BooleanMatrix value) {
    return orElementWise(value);
  }

  /**
   * 行列<code>value</code>との積(成分毎の論理積)を返します。
   * 
   * @param value 行列
   * @return <code>value</code>との積(成分毎の論理積)
   */
  public final BooleanMatrix multiply(final BooleanMatrix value) {
    if (getColumnSize() != value.getRowSize()) {
      throw new MatrixSizeException(this, value, MatrixSizeException.INCONSISTENT_SIZE);
    }

    final boolean[][] ans;
    if (getRowSize() == 0 || value.getRowSize() == 0) {
      ans = new boolean[0][value.getColumnSize()];
    } else {
      ans = BooleanMatrixUtil.multiply(this.elements, value.elements);
    }

    return new BooleanMatrix(ans);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero() {
    return BooleanMatrixUtil.isZero(this.elements);
  }

  /**
   * この行列を{@link IntMatrix}に変換します。
   * 
   * @return 変換した{@link IntMatrix}
   */
  public final IntMatrix convertIntMatrix() {
    final IntMatrix matrix = new IntMatrix(getRowSize(), getColumnSize());
    final IntMatrix intMat = find();
    for (int i = 1; i <= intMat.getColumnSize(); i++) {
      matrix.setElement(intMat.getIntElement(i), 1);
    }
    return matrix;
  }
  
  /**
   * 入力ストリームからMX形式の行列データを読み込みます。
   * 
   * @param input 入力ストリーム
   * @return 読込んだ行列
   * @throws IOException 入力ストリームからデータを読込めない場合
   */
  public static BooleanMatrix readMxFormat(final InputStream input) throws IOException {
    final MxDataHead head = new MxDataHead();
    head.read(input);
    return new BooleanMatrix(BooleanMatrixUtil.readMxFormat(input, head));
  }
  
  /**
   * ファイルからMX形式の行列データを読み込みます。
   * 
   * @param input ファイル
   * @return 読込んだ行列
   * @throws IOException 入力ストリームからデータを読込めない場合
   */
  public static BooleanMatrix readMxFormat(final File input) throws IOException {
    try (final FileInputStream stream = new FileInputStream(input)) {
      final BooleanMatrix ans = readMxFormat(stream);
      return ans;
    }
  }
  
  /**
   * データをMX形式で出力ストリームへ出力します。 outputがcloseされるまで、いくつでも出力可能です。
   * 
   * @param output 出力ストリーム
   * @param dataName データの名前
   * @exception IOException 出力ストリームに出力できない場合
   */
  public final void writeMxFormat(final OutputStream output, final String dataName) throws IOException {
    BooleanMatrixUtil.writeMxFormat(this.elements, output, dataName);
  }
  
  /**
   * データをMX形式でファイルへ出力します。
   * 
   * @param file ファイル
   * @param dataName データの名前
   * @throws IOException ファイルに出力できない場合
   */
  public final void writeMxFormat(final File file, final String dataName) throws IOException {
    try (final OutputStream output = new FileOutputStream(file)) {
      writeMxFormat(output, dataName);
    }
  }
}