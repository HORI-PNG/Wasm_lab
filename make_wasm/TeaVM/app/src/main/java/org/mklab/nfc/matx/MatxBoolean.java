/*
 * $Id: MatxBoolean.java,v 1.8 2008/07/16 08:00:36 koga Exp $
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


/**
 * MX形式とMM形式のbooleanの入出力処理をするクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.8 $, 2004/04/26
 */
public final class MatxBoolean extends MatxAbstractObject implements Cloneable {

  /** booleanデータ。 */
  private boolean data;

  /**
   * 新しく生成された<code>MatxBoolean</code>オブジェクトを初期化します。
   * @param data booleanデータ
   */
  public MatxBoolean(final boolean data) {
    this.data = data;
  }

  /**
   * MM形式でライターへ出力します。
   * 
   * @param value 出力する真偽値
   * @param output ライター
   * @param name 変数名
   * @throws IOException ライターへ出力できない場合
   */
  public static void writeMmFormat(final boolean value, final Writer output, final String name) throws IOException {
    MatxBoolean.writeMmFormat(value, output, name, true);
  }

  /**
   * MM形式でライターへ出力します。
   * 
   * @param value 出力する真偽値
   * @param output ライター
   * @param name 変数名
   * @param withNewLine 改行するならばtrue、そうでなければfalse
   * @throws IOException ライターへ出力できない場合
   */
  public static void writeMmFormat(final boolean value, final Writer output, final String name, final boolean withNewLine) throws IOException {
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
   * @param value 出力する真偽値
   * @param output 出力ストリーム
   * @param name 変数名
   * @throws IOException ストリームへ出力できない場合
   */
  public static void writeMxFormat(final boolean value, final OutputStream output, final String name) throws IOException {
    final MxDataHead head = MxDataHead.createDataHeadForBoolean(name);
    head.write(output);

    final DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(output));
    dataOutputStream.writeBoolean(value);
    dataOutputStream.flush();
  }

  /**
   * MX形式のbooleanを入力ストリームから入力します。
   * 
   * @param input 入力ストリーム
   * @return 入力したboolean
   * @throws IOException 入力ストリームから入力できない場合
   */
  public static boolean readMxFormat(final InputStream input) throws IOException {
    final MxDataHead head = new MxDataHead();
    head.read(input);
    return new DataInputStream(input).readBoolean();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone() {
    MatxBoolean inst = new MatxBoolean(this.data);
    return inst;
  }

  /**
   * {@inheritDoc}
   */
  public void writeMmFormat(final Writer output, final String name, final boolean withNewLine) throws IOException {
    MatxBoolean.writeMmFormat(this.data, output, name, withNewLine);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toMmString() {
    return String.format("%d",  this.data ? Integer.valueOf(1) :  Integer.valueOf(0)); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("boxing")
  public String toMmString(final String format) {
    return String.format(format, this.data ? 1 : 0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeMxFormat(final DataOutputStream output, final String dataName) throws IOException {
    MatxBoolean.writeMxFormat(this.data, output, dataName);
  }
  
  /**
   * boolean型のデータを返します。
   * @return boolean型のデータ
   */
  boolean toBoolean() {
    return this.data;
  }
}