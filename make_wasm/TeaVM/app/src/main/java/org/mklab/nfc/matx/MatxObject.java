/**
 * $Id: MatxObject.java,v 1.6 2007/12/13 22:54:53 koga Exp $
 *
 * Copyright (C) 2004 Masanobu Koga. All rights reserved.
 */
package org.mklab.nfc.matx;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Writer;


/**
 * MaTXのオブジェクトを表わすインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.6 $
 */
public interface MatxObject {

  /**
   * MM形式の文字列を生成します。
   * 
   * @return MM形式の文字列
   */
  String toMmString();

  /**
   * MM形式の文字列を生成します。
   * 
   * @param format 出力フォーマット
   * @return MM形式の文字列
   */
  String toMmString(final String format);

  /**
   * データをMX形式でファイルへ出力します。
   * 
   * @param file ファイル
   * @param dataName データの名前
   * @throws IOException ファイルに出力できない場合
   */
  void writeMxFormat(File file, String dataName) throws IOException;

  /**
   * データをMX形式で出力ストリームへ出力します。 outputがcloseされるまで、いくつでも出力可能です。
   * 
   * @param output 出力ストリーム
   * @param dataName データの名前
   * @exception IOException 出力ストリームに出力できない場合
   */
  void writeMxFormat(DataOutputStream output, String dataName) throws IOException;

  /**
   * MMファイル形式で行列データをファイルに出力します。
   * 
   * @param file ファイル
   * @param dataName 名前
   * @throws IOException ファイルに出力できない場合
   */
  void writeMmFormat(File file, String dataName) throws IOException;

  /**
   * MMフォーマット行列データをライターに出力します。
   * 
   * @param output ライター
   * @param dataName 名前
   * @param withStatementSeparator セミコロンと改行コードを出力するならばtrue、そうでなければfalse
   * @throws IOException ライターに出力できない場合
   */
  void writeMmFormat(Writer output, String dataName, boolean withStatementSeparator) throws IOException;
}
