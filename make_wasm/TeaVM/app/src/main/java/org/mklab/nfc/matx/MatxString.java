/*
 * $Id: MatxString.java,v 1.14 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matx;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;

import org.mklab.nfc.matrix.BooleanMatrix;
import org.mklab.nfc.matrix.MatrixSizeException;


/**
 * MaTXのString型を表すクラスです。
 * 
 * @author koga
 * @version $Revision: 1.14 $
 */
public class MatxString extends MatxAbstractObject implements Cloneable {

  /** 文字列を保持するバッファー。 */
  private StringBuffer buffer;

  /**
   * 文字をもたず、初期容量が 16 文字であるMxStringを生成します。
   */
  public MatxString() {
    this.buffer = new StringBuffer();
  }

  /**
   * 文字をもたず、引数 length によって指定された初期容量である MxStringを生成します。
   * 
   * @param length 初期容量
   */
  public MatxString(final int length) {
    this.buffer = new StringBuffer(length);
  }

  /**
   * stringで指定した文字列バッファをもつMxStringを生成します。
   * 
   * @param string 文字列
   */
  public MatxString(final String string) {
    this.buffer = new StringBuffer(string);
  }

  /**
   * 保持するStringを返します。
   * 
   * @return 保持するString
   */
  @Override
  public String toString() {
    return this.buffer.toString();
  }

  /**
   * 文字列stringを追加たMxSringを返します。
   * 
   * @param string 追加する文字列。
   * @return stringを追加したMxString
   */
  public final MatxString add(final String string) {
    return new MatxString(this.buffer.toString() + string);
  }

  /**
   * この文字列バッファの長さ (文字数) を返します。
   * 
   * @return 文字数
   */
  public final int length() {
    return this.buffer.length();
  }

  /**
   * count回繰り返した文字列バッファをもつMxStringを返します。
   * 
   * @param count 繰り返し回数
   * @return n回繰り返した文字をもつMxString
   */
  public final MatxString multiply(final int count) {
    if (count < 0) {
      throw new IllegalArgumentException();
    }

    final String str = this.buffer.toString();

    final StringBuffer ans = new StringBuffer();
    for (int i = 0; i < count; i++) {
      ans.append(str);
    }
    return new MatxString(ans.toString());
  }

  /**
   * indexで指定した位置の文字を返します。
   * 
   * @param index 指定位置
   * @return 文字
   */
  public final char charAt(final int index) {
    return this.buffer.charAt(index - 1);
  }

  /**
   * indexで指定した位置の文字をchを代入します。
   * 
   * @param index 指定位置
   * @param ch 変更文字
   */
  public final void setCharAt(final int index, final char ch) {
    this.buffer.setCharAt(index - 1, ch);
  }

  /**
   * fromからtoまでの部分文字列を返します。
   * 
   * @param from 開始位置
   * @param to 終了位置
   * @return fromからtoまでの部分文字列
   */
  public final String getSubString(final int from, final int to) {
    if (to >= from) {
      return this.buffer.substring(from - 1, to);
    }

    return new StringBuffer(this.buffer.substring(to - 1, from)).reverse().toString();
  }

  /**
   * fromからtoまでstep飛びの位置にある部分文字列を返します。
   * 
   * @param from 開始位置
   * @param step ステップ数
   * @param to 終了位置
   * @return 選択された文字からなる部分文字列
   */
  public final String getSubString(final int from, final int step, final int to) {
    final int length = (to - from) / step + 1;
    final char[] ans = new char[length];
    for (int i = 0; i < length; i++) {
      ans[i] = this.charAt(from + i * step);
    }
    return new String(ans);
  }

  /**
   * indexで指定した位置の文字列を返します。
   * 
   * @param index 位置指定行列
   * @return indexで指定した位置の文字列
   */
  public final String getSubString(final int[] index) {
    final int length = index.length;
    final char[] ans = new char[length];
    for (int i = 0; i < length; i++) {
      ans[i] = this.charAt(index[i]);
    }
    return new String(ans);
  }

  /**
   * boolean行列のtrueのがある位置の文字列を返します。
   * 
   * @param indexMatrix 文字列を取り出す位置の要素がtrueであるboolean行列
   * 
   * @return 生成した文字列
   */
  public final String getSubString(final BooleanMatrix indexMatrix) {
    final int booleanLength = indexMatrix.getColumnSize();
    if (indexMatrix.getRowSize() != 1) {
      throw new MatrixSizeException(MatrixSizeException.INCONSISTENT_ROW_NUMBER);
    } else if (booleanLength > this.buffer.length()) {
      throw new MatrixSizeException(MatrixSizeException.INCONSISTENT_COLUMN_NUMBER);
    }

    final int length = indexMatrix.getNumberOfTrue();
    final char[] ans = new char[length];

    int i = 0;
    int j = 1;
    while (j <= booleanLength) {
      if (indexMatrix.getElement(1, j++)) {
        ans[i++] = this.charAt(j - 1);
      }
    }
    return new String(ans);
  }

  /**
   * fromからtoまでをstringで置き換える。
   * 
   * @param from 開始位置
   * @param to 終了位置
   * @param string 変更文字列
   */
  public final void setSubString(final int from, final int to, final String string) {
    final int length = string.length();

    if (length != (to - from + 1)) {
      throw new IllegalArgumentException();
    }

    this.buffer.replace(from - 1, to, string);
  }

  /**
   * fromからtoまでstep飛びの位置にある文字をstrで置き換える。
   * 
   * @param from 開始位置
   * @param step step数
   * @param to 終了位置
   * @param string 変更文字列
   */
  public final void setSubString(final int from, final int step, final int to, final String string) {
    final int length = string.length();
    int i = from;
    int j = 0;

    while (i <= to && j < length) {
      this.setCharAt(i, string.charAt(j++));
      i += step;
    }
  }

  /**
   * 各文字列とchをoperatorで指定した演算子で比較し、 結果を {@link org.mklab.nfc.matrix.BooleanMatrix} で返します。
   * 
   * @param operator 演算を表す文字列
   * 
   * @param ch 演算の対象となる文字
   * 
   * @return 演算の結果を表すboolean行列
   */
  public final BooleanMatrix compareElementWise(final String operator, final char ch) {
    final String str = this.buffer.toString();

    final int length = str.length();
    final boolean[] ans = new boolean[length];

    if (operator.equals(".>")) { //$NON-NLS-1$
      for (int i = 0; i < length; i++) {
        ans[i] = str.charAt(i) > ch;
      }
    } else if (operator.equals(".>=")) { //$NON-NLS-1$
      for (int i = 0; i < length; i++) {
        ans[i] = str.charAt(i) >= ch;
      }
    } else if (operator.equals(".<")) { //$NON-NLS-1$
      for (int i = 0; i < length; i++) {
        ans[i] = str.charAt(i) < ch;
      }
    } else if (operator.equals(".<=")) { //$NON-NLS-1$
      for (int i = 0; i < length; i++) {
        ans[i] = str.charAt(i) <= ch;
      }
    } else if (operator.equals(".==")) { //$NON-NLS-1$
      for (int i = 0; i < length; i++) {
        ans[i] = str.charAt(i) == ch;
      }
    } else if (operator.equals(".!=")) { //$NON-NLS-1$
      for (int i = 0; i < length; i++) {
        ans[i] = str.charAt(i) != ch;
      }
    } else {
      throw new RuntimeException(Messages.getString("MatxString.0")); //$NON-NLS-1$
    }

    return new BooleanMatrix(ans);
  }

  /**
   * 各文字とopponentの各文字をoperatorで指定した演算子で比較し, 結果を {@link org.mklab.nfc.matrix.BooleanMatrix} で返します。
   * 
   * @param opponent 比較する対象の文字列
   * 
   * @param operator 演算子
   * 
   * @return 比較結果を成分とするboolean行列
   */
  public final BooleanMatrix compareElementWise(final MatxString opponent, final String operator) {
    final String str1 = this.buffer.toString();
    final String str2 = opponent.buffer.toString();

    final int length = str1.length();
    final boolean[] ans = new boolean[length];

    if (operator.equals(".>")) { //$NON-NLS-1$
      for (int i = 0; i < length; i++) {
        ans[i] = str1.charAt(i) > str2.charAt(i);
      }

    } else if (operator.equals(".>=")) { //$NON-NLS-1$
      for (int i = 0; i < length; i++) {
        ans[i] = str1.charAt(i) >= str2.charAt(i);
      }

    } else if (operator.equals(".<")) { //$NON-NLS-1$
      for (int i = 0; i < length; i++) {
        ans[i] = str1.charAt(i) < str2.charAt(i);
      }

    } else if (operator.equals(".<=")) { //$NON-NLS-1$
      for (int i = 0; i < length; i++) {
        ans[i] = str1.charAt(i) <= str2.charAt(i);
      }

    } else if (operator.equals(".==")) { //$NON-NLS-1$
      for (int i = 0; i < length; i++) {
        ans[i] = str1.charAt(i) == str2.charAt(i);
      }

    } else if (operator.equals(".!=")) { //$NON-NLS-1$
      for (int i = 0; i < length; i++) {
        ans[i] = str1.charAt(i) != str2.charAt(i);
      }
    } else {
      throw new RuntimeException(Messages.getString("MatxString.1")); //$NON-NLS-1$
    }

    return new BooleanMatrix(ans);
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMxFormat(final DataOutputStream output, final String name) throws IOException {
    MatxString.writeMxFormat(toString(), output, name);
  }

  /**
   * MX形式で文字列を出力ストリームに出力します。
   * 
   * @param string 出力する文字列
   * @param output 出力ストリーム
   * @param name 名前
   * @throws IOException ストリームに出力できない場合
   */
  public static void writeMxFormat(final String string, final OutputStream output, final String name) throws IOException {
    final MxDataHead head = new MxDataHead(string, name);

    head.write(output);

    final DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(output));
    dataOutputStream.write((string + "\0").getBytes(Charset.forName("UTF-8"))); //$NON-NLS-1$ //$NON-NLS-2$
    dataOutputStream.flush();
  }

  /**
   * MX形式の文字列を入力ストリームから読み込みます。
   * 
   * @param input 入力ストリーム
   * @return MxStringのインスタンス
   * @throws IOException 入力ストリームから読み込めない場合
   */
  public static MatxString readMxFormat(final InputStream input) throws IOException {
    final MxDataHead head = new MxDataHead();
    head.read(input);
    return readMxFormat(head, input);
  }

  /**
   * MX形式の文字列を入力ストリームから読み込みます。 ヘッダ情報は先に取得している。
   * 
   * @param input 入力ストリーム
   * @param head ヘッダ情報
   * @return MxStringのインスタンス
   * @throws IOException 入力ストリームから読み込めない場合
   */
  public static MatxString readMxFormat(final MxDataHead head, final InputStream input) throws IOException {
    final byte[] s = new byte[head.getLength()];
    input.read(s);
    return new MatxString(new String(s, 0, head.getLength() - 1, Charset.forName("UTF-8"))); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMmFormat(final Writer output, final String name, final boolean withNewLine) throws IOException {
    MatxString.writeMmFormat(toString(), output, name, withNewLine);
  }

  /**
   * MM形式で文字列をライターへ出力します。
   * 
   * @param string 出力する文字列
   * @param output ライター
   * @param name 名前
   * @throws IOException ライターへ出力できない場合
   */
  public static void writeMmFormat(final String string, final Writer output, final String name) throws IOException {
    MatxString.writeMmFormat(string, output, name, true);
  }

  /**
   * MM形式で文字列をライターへ出力します。
   * 
   * @param string 出力する文字列
   * @param output ライター
   * @param name 名前
   * @param withNewLine 改行コードを出力するならばtrue、そうでなければfalse
   * @throws IOException ライターへ出力できない場合
   */
  public static void writeMmFormat(final String string, final Writer output, final String name, final boolean withNewLine) throws IOException {
    final StringBuffer buffer = new StringBuffer();

    if (name.length() != 0) {
      buffer.append(name);
      buffer.append(" = "); //$NON-NLS-1$
    }

    buffer.append("\""); //$NON-NLS-1$
    buffer.append(string);
    buffer.append("\""); //$NON-NLS-1$

    if (withNewLine) {
      final String newLine = System.getProperty("line.separator"); //$NON-NLS-1$
      buffer.append(";"); //$NON-NLS-1$
      buffer.append(newLine);
      buffer.append(newLine);
    }

    output.write(buffer.toString());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toMmString() {
    return String.format("\"%s\"", this.buffer.toString()); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public String toMmString(final String format) {
    return String.format(format, this.buffer.toString());
  }
  
  /**
   * Returns true if str1 is equal to str2.
   * 
   * @param str1 string 1
   * @param str2 string 2
   * @return true if str1 is equal to str2.
   */
  public static int mxEquals(String str1, String str2) {
    if (str1.equals(str2)) {
      return 1;
    }
    return 0;
  }
  
  /**
   * Returns true if str1 is equal to str2.
   * 
   * @param str1 string 1
   * @param str2 string 2
   * @return true if str1 is equal to str2.
   */
  public static int mxNotEquals(String str1, String str2) {
    if (str1.equals(str2)) {
      return 0;
    }
    return 1;
  }

}