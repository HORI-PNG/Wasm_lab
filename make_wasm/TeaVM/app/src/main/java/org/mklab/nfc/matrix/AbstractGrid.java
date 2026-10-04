/**
 * $Id: AbstractGrid.java,v 1.8 2008/07/16 04:58:02 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

import java.io.CharArrayWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.nio.charset.Charset;


/**
 * グリッド(格子状)データを表わすクラスです。
 * 
 * @author koga
 * @version $Revision: 1.8 $
 * @param <M> 行列の型
 */
public abstract class AbstractGrid<M extends Grid> implements Grid, Cloneable, Serializable {

  /** シリアル番号。 */
  private static final long serialVersionUID = 4748615871841225054L;

  /** 成分のデフォルト出力フォーマット。 */
  private static String defaultElementFormat = "%15G"; //$NON-NLS-1$

  /** 成分のデフォルトの配置。 */
  private static GridElementAlignment defaultElementAlignment = GridElementAlignment.CENTER;

  /** 成分の出力フォーマット。 */
  private String elementFormat = AbstractGrid.defaultElementFormat;

  /** 成分の出力配置。 */
  private GridElementAlignment elementAlignment = AbstractGrid.defaultElementAlignment;

  /** 行の数。 */
  private int rowSize;

  /** 列の数。 */
  private int columnSize;

  /**
   * 成分のデフォルト出力フォーマットを設定します。
   * 
   * @param format 成分のデフォルト出力フォーマット
   */
  public static void setDefaultElementFormat(final String format) {
    AbstractGrid.defaultElementFormat = format;
  }

  /**
   * 成分のデフォルト出力フォーマットを返します。
   * 
   * @return 成分のデフォルト出力フォーマット
   */
  public static String getDefaultElementFormat() {
    return AbstractGrid.defaultElementFormat;
  }

  /**
   * 成分のデフォルト出力配置を設定します。
   * 
   * @param alignment 成分のデフォルト出力配置
   */
  public static void setDefaultElementAlignment(final GridElementAlignment alignment) {
    AbstractGrid.defaultElementAlignment = alignment;
  }

  /**
   * 成分のデフォルト出力配置を返します。
   * 
   * @return 成分のデフォルト出力配置
   */
  public static GridElementAlignment getDefaultElementAlignment() {
    return AbstractGrid.defaultElementAlignment;
  }

  /**
   * 新しく生成された<code>Grid</code>オブジェクトを初期化します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   */
  public AbstractGrid(final int rowSize, final int columnSize) {
    this.rowSize = rowSize;
    this.columnSize = columnSize;
  }

  /**
   * {@inheritDoc}
   */
  public final int getRowSize() {
    return this.rowSize;
  }

  /**
   * 行の数を設定します。
   * 
   * @param rowSize 行の数
   */
  protected final void setRowSize(final int rowSize) {
    this.rowSize = rowSize;
  }

  /**
   * {@inheritDoc}
   */
  public final int getColumnSize() {
    return this.columnSize;
  }

  /**
   * 列の数を設定します。
   * 
   * @param columnSize 列の数
   */
  protected final void setColumnSize(final int columnSize) {
    this.columnSize = columnSize;
  }

  /**
   * {@inheritDoc}
   */
  public final int length() {
    if (getRowSize() == 0 || getColumnSize() == 0) {
      return 0;
    }
    return Math.max(getRowSize(), getColumnSize());
  }

  /**
   * {@inheritDoc}
   */
  public final int count() {
    return getRowSize() * getColumnSize();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isEmpty() {
    if (getRowSize() == 0 || getColumnSize() == 0) {
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isSameSize(final Grid opponent) {
    return (getRowSize() == opponent.getRowSize() && getColumnSize() == opponent.getColumnSize());
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isSquare() {
    return getRowSize() == getColumnSize();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean hasSameRowSize(final Grid opponent) {
    return getRowSize() == opponent.getRowSize();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean hasSameColumnSize(final Grid opponent) {
    return getColumnSize() == opponent.getColumnSize();
  }

  /**
   * {@inheritDoc}
   */
  public final void print() {
    print("ans"); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final void print(final String name) {
    print(name, new OutputStreamWriter(System.out, Charset.forName("UTF-8"))); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final void print(final String name, final Writer output) {
    printHeader(name, output);
    printElements(output);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String toString() {
    return getGridClassName() + getSizeString();
  }

  /**
   * "(rowSize*columnSize)"という文字列を返します。
   * 
   * @return 行の数x列の数 の文字列
   */
  private String getSizeString() {
    return "(" + getRowSize() + "x" + getColumnSize() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

  /**
   * 名前とサイズをライターに出力します。
   * 
   * @param name 名前
   * @param writer ライター
   */
  private void printHeader(final String name, final Writer writer) {
    final PrintWriter pw = new PrintWriter(writer);
    //pw.print(" ===  " + name + "  ( " + IntUtil.format(getRowSize(), 3) + "  x " + IntUtil.format(getColumnSize(), 3) + ")  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    pw.print(String.format(" ===  %s  ( %3d  x %3d)  ", name, Integer.valueOf(getRowSize()), Integer.valueOf(getColumnSize()))); //$NON-NLS-1$
    pw.println(getGridClassName() + "  ==="); //$NON-NLS-1$
    pw.flush();
  }

  /**
   * 出力するクラスの名前を返します。
   * 
   * @return 出力するクラスの名前
   */
  protected String getGridClassName() {
    return getClass().getSimpleName();
  }

  /**
   * {@inheritDoc}
   */
  public final String getPrintingString(final String name) {
    try (final CharArrayWriter output = new CharArrayWriter()) {
      print(name, output);
      final String printString = output.toString();
      return printString;
    }
  }

  /**
   * {@inheritDoc}
   */
  public final String getPrintingElementsString(final int maxColumnSize) {
    try (final CharArrayWriter writer = new CharArrayWriter()) {
      printElements(writer, maxColumnSize);
      final String printString = writer.toString();
      return printString;
    }
  }

  /**
   * {@inheritDoc}
   */
  public final void setElementFormat(final String format) {
    this.elementFormat = format;
  }

  /**
   * {@inheritDoc}
   */
  public final String getElementFormat() {
    return this.elementFormat;
  }

  /**
   * {@inheritDoc}
   */
  public final void setElementAlignment(final GridElementAlignment alignment) {
    this.elementAlignment = alignment;
  }

  /**
   * {@inheritDoc}
   */
  public final GridElementAlignment getElementAlignment() {
    return this.elementAlignment;
  }

  /**
   * {@inheritDoc}
   */
  public final void removeRowVector(final int index) {
    removeRowVectors(index, index);
  }

  /**
   * {@inheritDoc}
   */
  public final void removeColumnVector(final int index) {
    removeColumnVectors(index, index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.columnSize;
    result = prime * result + ((this.elementAlignment == null) ? 0 : this.elementAlignment.hashCode());
    result = prime * result + ((this.elementFormat == null) ? 0 : this.elementFormat.hashCode());
    result = prime * result + this.rowSize;
    return result;
  }
}
