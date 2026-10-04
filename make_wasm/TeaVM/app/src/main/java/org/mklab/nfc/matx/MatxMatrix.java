/*
 * Created on 2008/01/12
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matx;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;

import org.mklab.nfc.matrix.DoubleComplexMatrix;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.DoublePolynomialMatrix;
import org.mklab.nfc.matrix.DoubleRationalPolynomialMatrix;
import org.mklab.nfc.matrix.IntMatrix;
import org.mklab.nfc.matrix.Matrix;/**
 * MaTXのMatrix型を表すクラスです。
 * 
 * @author koga
 * @version $Revision: 1.5 $, 2008/01/12
 */
public final class MatxMatrix {
  /**
   * 新しく生成された<code>MatxMatrix</code>オブジェクトを初期化します。
   */
  private MatxMatrix() {
    // nothing to do
  }

  /**
   * 入力ストリームからMX形式の行列データを読み込みます。
   * 
   * @param input 入力ストリーム
   * @return 読込んだ行列
   * @throws IOException 入力ストリームからデータを読込めない場合
   */
  public static Matrix<?,?> readMxFormat(final InputStream input) throws IOException {
    final MxDataHead head = new MxDataHead();
    head.read(input);
    return MatxMatrix.readMxFormat(head, input);
  }

  /**
   * 入力ストリームからMX形式の行列データを読み込みます。 ヘッダ情報は先に指定している。
   * 
   * @param input 入力ストリーム
   * @param head ヘッダ情報
   * @return 読込んだ行列
   * @throws IOException 入力ストリームからデータを読込めない場合
   */
  public static Matrix<?,?> readMxFormat(final MxDataHead head, final InputStream input) throws IOException {
    final int matrixType = head.getMatrixType();

    if (matrixType == MxDataHead.INTEGER_MATRIX) {
      return IntMatrix.readMxFormat(input, head);
    }
    if (matrixType == MxDataHead.REAL_MATRIX) {
      return DoubleMatrix.readMxFormat(input, head);
    }
    if (matrixType == MxDataHead.COMPLEX_MATRIX) {
      return DoubleComplexMatrix.readMxFormat(input, head);
    }
    if (matrixType == MxDataHead.REAL_POLYNOMIAL_MATRIX ) {
      return DoublePolynomialMatrix.readMxFormat(input, head);
    }
    if (matrixType == MxDataHead.COMPLEX_POLYNOMIAL_MATRIX) {
      return DoublePolynomialMatrix.readMxFormat(input, head);
    }
    if (matrixType == MxDataHead.REAL_RATIONAL_POLYNOMIAL_MATRIX ) {
      return DoubleRationalPolynomialMatrix.readMxFormat(input, head);
    }
    if (matrixType == MxDataHead.COMPLEX_RATIONAL_POLYNOMIAL_MATRIX) {
      return DoubleRationalPolynomialMatrix.readMxFormat(input, head);
    }

    throw new IllegalArgumentException(Messages.getString("MatxMatrix.0")); //$NON-NLS-1$
  }

  /**
   * MX形式の行列データを読み込みます。
   * 
   * @param file 読み込むファイル
   * @return 読込んだ行列
   * @throws IOException ファイルを読込めない場合
   */
  public static Matrix<?,?> readMxFormat(final File file) throws IOException {
    try (DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
      return readMxFormat(input);
    }
  }

  /**
   * MATフォーマットのデータファイルを読み込みます。
   * 
   * @param file 読み込むファイル
   * @return 読み込んだ行列
   * @exception IOException ファイルから読み込めない場合
   */
  public static Matrix<?,?> readMatFormat(final File file) throws IOException {
    try (Reader input = new InputStreamReader(new FileInputStream(file), "UTF-8")) { //$NON-NLS-1$
      return MatxMatrix.readMatFormat(input);
    }
  }

  /**
   * MATフォーマットのデータファイルを読み込みます。
   * 
   * @param input 読み込むファイルのInputStream
   * @return 読み込んだ行列
   * @exception IOException 読み込みエラーが発生した場合
   */
  public static Matrix<?,?> readMatFormat(final Reader input) throws IOException {
    final BufferedReader br = new BufferedReader(input);
    final StreamTokenizer st = new StreamTokenizer(br);

    st.wordChars('#', '#');

    if (st.nextToken() == StreamTokenizer.TT_EOF || st.ttype != StreamTokenizer.TT_WORD || st.sval.equals("#") == false) { //$NON-NLS-1$
      throw new IOException(Messages.getString("MatxMatrix.2")); //$NON-NLS-1$
    }

    if (st.nextToken() == StreamTokenizer.TT_EOF || st.ttype != StreamTokenizer.TT_NUMBER) {
      throw new IOException(Messages.getString("MatxMatrix.3")); //$NON-NLS-1$
    }
    final int rowSize = (int)st.nval;

    if (st.nextToken() == StreamTokenizer.TT_EOF || st.ttype != StreamTokenizer.TT_NUMBER) {
      throw new IOException(Messages.getString("MatxMatrix.4")); //$NON-NLS-1$
    }

    final int columnSize = (int)st.nval;

    st.resetSyntax();
    st.wordChars('.', '.');
    st.wordChars('-', '-');
    st.wordChars('+', '+');
    st.wordChars('E', 'E');
    st.wordChars('e', 'e');
    st.wordChars('C', 'C');
    st.wordChars('0', '9');
    st.whitespaceChars(' ', ' ');
    st.whitespaceChars('\t', '\t');
    st.whitespaceChars('\n', '\n');
    st.whitespaceChars('\r', '\r');

    if (st.nextToken() == StreamTokenizer.TT_EOF || st.ttype != StreamTokenizer.TT_WORD) {
      throw new IOException(Messages.getString("MatxMatrix.5")); //$NON-NLS-1$
    }

    if (st.sval.equals("C")) { //$NON-NLS-1$
      return DoubleComplexMatrix.readMatFormat(rowSize, columnSize, st);
    }

    st.pushBack();
    return DoubleMatrix.readMatFormat(rowSize, columnSize, st);
  }

}
