/*
 * $Id: MatxInteger.java,v 1.9 2008/07/16 08:00:37 koga Exp $
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

import org.mklab.nfc.util.EndianTransformer;


/**
 * MaTXのInteger型を表すクラスです。
 * 
 * @author koga
 * @version $Revision: 1.9 $, 2004/04/26
 */
public final class MatxInteger extends MatxAbstractObject implements Cloneable {

  /** 整数データ。 */
  private int data;

  /**
   * 新しく生成された<code>MatxInteger</code>オブジェクトを初期化します。
   * @param data 整数データ
   */
  public MatxInteger(final int data) {
    this.data = data;
  }

  /**
   * MX形式の入力ストリームから入力します。
   * 
   * @param input 入力ストリーム
   * @return 入力した整数
   * @throws IOException 入力ストリームから入力できない場合
   */
  public static int readMxFormat(final InputStream input) throws IOException {
    MxDataHead head = new MxDataHead();
    head.read(input);
    return readMxFormat(head, input);
  }

  /**
   * MX形式の入力ストリームから入力します。 ヘッダ情報は先に取得している。
   * 
   * @param input 入力ストリーム
   * @param head ヘッダ情報
   * @return 入力した整数
   * @throws IOException 入力ストリームから入力できない場合
   */
  public static int readMxFormat(final MxDataHead head, final InputStream input) throws IOException {
    final DataInputStream dataInputStream = new DataInputStream(input);

    if (head.isSameEndian()) {
      return dataInputStream.readInt();
    }

    return EndianTransformer.flip(dataInputStream.readInt());
  }

  /**
   * {@inheritDoc}
   */
  public void writeMmFormat(final Writer output, final String name, final boolean withNewLine) throws IOException {
    MatxInteger.writeMmFormat(this.data, output, name, withNewLine);
  }

  /**
   * MM形式でライターへ出力します。
   * 
   * @param value 出力する整数
   * @param output ライター
   * @param name 変数名
   * @throws IOException ライターへ出力できない場合
   */
  public static void writeMmFormat(final int value, final Writer output, final String name) throws IOException {
    MatxInteger.writeMmFormat(value, output, name, true);
  }

  /**
   * MM形式でライターへ出力します。
   * 
   * @param value 出力する整数
   * @param output ライター
   * @param name 変数名
   * @param withNewLine 改行するならばtrue、そうでなければfalse
   * @throws IOException ライター出力できない場合
   */
  public static void writeMmFormat(final int value, final Writer output, final String name, final boolean withNewLine) throws IOException {
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
   * MX形式で出力ストリームに出力します。
   * 
   * @param value 出力する整数
   * @param output 出力ストリーム
   * @param name 変数名
   * @throws IOException ストリームに出力できない場合
   */
  public static void writeMxFormat(final int value, final OutputStream output, final String name) throws IOException {
    final MxDataHead head = MxDataHead.createDataHeadForInt(name);
    head.write(output);
    final DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(output));
    dataOutputStream.writeInt(value);
    dataOutputStream.flush();
  }

  /**
   * {@inheritDoc}
   */
  public void writeMxFormat(final DataOutputStream output, final String name) throws IOException {
    MatxInteger.writeMxFormat(this.data, output, name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone() {
    MatxInteger inst = new MatxInteger(this.data);
    return inst;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toMmString() {
    return String.format("%d", Integer.valueOf(this.data)); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public String toMmString(final String format) {
    return String.format(format, Integer.valueOf(this.data));
  }
  
  /**
   * int型のデータを返します。
   * @return int型のデータ
   */
  public int toInt() {
    return this.data;
  }
}