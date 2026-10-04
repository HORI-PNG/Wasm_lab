/*
 * $Id: MatxDouble.java,v 1.9 2008/07/16 08:00:36 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matx;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import org.mklab.nfc.scalar.DoubleNumber;
import org.mklab.nfc.util.EndianTransformer;


/**
 * MaTXのReal型を表すクラスです。
 * 
 * @author koga
 * @version $Revision: 1.9 $, 2004/04/26
 */
public final class MatxDouble extends MatxAbstractObject implements Cloneable {

  /** 実数データ。 */
  private double data;

  /**
   * 新しく生成された<code>MatxDouble</code>オブジェクトを初期化します。
   * @param data 実数データ
   */
  public MatxDouble(final double data) {
    this.data = data;
  }

  /**
   * MM形式でライターへ出力します。
   * 
   * @param value 出力する実数
   * @param output ライター
   * @param name 変数名
   * @throws IOException ライターへ出力できない場合
   */
  public static void writeMmFormat(final double value, final Writer output, final String name) throws IOException {
    MatxDouble.writeMmFormat(value, output, name, true);
  }

  /**
   * MM形式でライターへ出力します。
   * 
   * @param value 出力する実数
   * @param output ライター
   * @param name 変数名
   * @param withNewLine 改行するならばtrue、そうでなければfalse
   * @throws IOException ライターへ出力できない場合
   */
  public static void writeMmFormat(final double value, final Writer output, final String name, final boolean withNewLine) throws IOException {
    final StringBuffer buffer = new StringBuffer();

    if (name.length() != 0) {
      buffer.append(name);
      buffer.append(" = "); //$NON-NLS-1$
    }

    buffer.append(value);

    if (withNewLine) {
      final String newLine = System.getProperty("line.separator"); //$NON-NLS-1$
      buffer.append(";"); //$NON-NLS-1$
      buffer.append(newLine);
      buffer.append(newLine);
    }

    output.write(buffer.toString());
  }

  /**
   * MX形式で出力ストリームへ出力します。
   * 
   * @param value 出力する実数
   * @param output 出力ストリーム
   * @param name 変数名
   * @throws IOException ストリームへ出力できない場合
   */
  public static void writeMxFormat(final double value, final OutputStream output, final String name) throws IOException {
    final MxDataHead head = MxDataHead.createDataHeadForDouble(name);
    head.write(output);
    final DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(output));
    dataOutputStream.writeDouble(value);
    dataOutputStream.flush();
  }

  /**
   * MX形式の入力ストリームから入力します。
   * 
   * @param input 入力ストリーム
   * @return 入力した実数
   * @throws IOException 入力ストリームから入力できない場合
   */
  public static double readMxFormat(final InputStream input) throws IOException {
    final MxDataHead head = new MxDataHead();
    head.read(input);
    return readMxFormat(head, input);
  }

  /**
   * MX形式の入力ストリームから入力します。 ヘッダ情報は先に取得しています。
   * 
   * @param input 入力ストリーム
   * @param head ヘッダ情報
   * @return 入力した実数
   * @throws IOException 入力ストリームから入力できない場合
   */
  public static double readMxFormat(final MxDataHead head, final InputStream input) throws IOException {
    final DataInputStream dataInputStream = new DataInputStream(input);

    if (head.isSameEndian()) {
      return dataInputStream.readDouble();
    }

    return Double.longBitsToDouble(EndianTransformer.flip(dataInputStream.readLong()));
  }

  /**
   * {@inheritDoc}
   */
  public void writeMmFormat(final Writer output, final String name, final boolean withNewLine) throws IOException {
    MatxDouble.writeMmFormat(this.data, output, name, withNewLine);
  }

  /**
   * {@inheritDoc}
   */
  public void writeMxFormat(final DataOutputStream output, final String name) throws IOException {
    MatxDouble.writeMxFormat(this.data, output, name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone() {
    MatxDouble inst = new MatxDouble(this.data);
    return inst;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toMmString() {
    return DoubleNumber.toString(this.data, "%G"); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public String toMmString(final String format) {
    return DoubleNumber.toString(this.data, format);
  }
  
  /**
   * data型のデータを返します。
   * @return　data型のデータ
   */
  public double toDouble() {
    return this.data;
  }
}