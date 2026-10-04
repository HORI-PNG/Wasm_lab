/*
 * $Id: MxDataHead.java,v 1.23 2008/02/02 05:53:02 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.matx;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.mklab.nfc.scalar.DoubleComplexNumber;
import org.mklab.nfc.scalar.DoubleComplexPolynomial;
import org.mklab.nfc.scalar.DoubleComplexRationalPolynomial;
import org.mklab.nfc.scalar.DoublePolynomial;
import org.mklab.nfc.scalar.DoubleRationalPolynomial;
import org.mklab.nfc.util.EndianTransformer;


/**
 * MX形式データのヘッダー情報に関するクラスです。
 * 
 * @author koga
 * @version $Revision: 1.23 $
 */
public class MxDataHead {

  /** マジックナンバー。 */
  private int magic = 0xffffffff;
  /** バージョン。 */
  private int version = 0x00000006;
  /** ダミー(将来の拡張のため)。 */
  private int dummy = 0x00000000;
  /** データの精度などの情報。 */
  private int mopt;
  /** 行の数。 */
  private int rowSize;
  /** 列の数。 */
  private int columnSize;
  /** 種類。 */
  private int type;
  /** 名前の長さ。 */
  private int nameLength;
  /** 名前。 */
  private String name;

  /** 文字列型 。 */
  public static final int STRING = 0x1;
  /** 整数型。 */
  public static final int INTEGER = 0x2;
  /** 実数型。 */
  public static final int REAL = 0x3;
  /** 複素数型。 */
  public static final int COMPLEX = 0x4;
  /** 多項式型 。 */
  public static final int POLYNOMIAL = 0x5;
  /** 有理多項式型。 */
  public static final int RATIONAL = 0x6;
  /** 行列型。 */
  public static final int MATRIX = 0x7;
  /** 配列型。 */
  public static final int ARRAY = 0x8;
  /** 指数型。 */
  public static final int INDEX = 0x9;
  /** リスト型。 */
  public static final int LIST = 0xA;
  /** 真偽値型。 */
  public static final int BOOLEAN = 0xB;

  /** 実行列。 */
  public static final int REAL_MATRIX = 0;
  /** 複素行列。 */
  public static final int COMPLEX_MATRIX = 1;
  /** 実多項式行列。 */
  public static final int REAL_POLYNOMIAL_MATRIX = 2;
  /** 複素多項式行列。 */
  public static final int COMPLEX_POLYNOMIAL_MATRIX = 3;
  /** 実有理多項式行列。 */
  public static final int REAL_RATIONAL_POLYNOMIAL_MATRIX = 4;
  /** 複素有理多項式行列。 */
  public static final int COMPLEX_RATIONAL_POLYNOMIAL_MATRIX = 5;
  /** 整数行列。 */
  public static final int INTEGER_MATRIX = 6;
  /** boolean行列。 */
  public static final int BOOLEAN_MATRIX = 7;

  /** 実多項式 */
  public static final int REAL_POLYNOMIAL = 0;
  /** 複素多項式 */
  public static final int COMPLEX_POLYNOIAL = 1;

  /**
   * 行列データの行の数を返します。
   * 
   * @return 行列データの行の数
   */
  public final int getRowSize() {
    return this.rowSize;
  }

  /**
   * 行列データの列の数を返します。
   * 
   * @return 行列データの列の数
   */
  public final int getColumnSize() {
    return this.columnSize;
  }

  /**
   * 実数要素と複素数要素を判定する情報を返します。
   * 
   * @return 実数要素と複素数要素を判定する情報
   */
  public final int getRealOrComplex() {
    return this.type;
  }

  /**
   * 名前を返します。
   * 
   * @return 名前
   */
  public final String getName() {
    return this.name;
  }

  /**
   * 多項式の次数を返します。
   * 
   * @return 多項式の次数
   */
  public final int getDegree() {
    return this.rowSize;
  }

  /**
   * 有理多項式の分子多項式の次数を返します。
   * 
   * @return 有理多項式の分子多項式の次数
   */
  public final int getNumeratorDegree() {
    return this.rowSize;
  }

  /**
   * 有理多項式の分母多項式の次数を返します。
   * 
   * @return 有理多項式の分母多項式の次数
   */
  public final int getDenominatorDegree() {
    return this.columnSize;
  }

  /**
   * 文字列の(長さ+1)又はリストの長さを返します。
   * 
   * @return 文字列の(長さ+1)又はリストの長さ
   */
  public final int getLength() {
    return this.rowSize;
  }

  /**
   * 行列の種類を返します。
   * 
   * @return 行列の種類
   */
  public final int getMatrixType() {
    return this.type;
  }

  /**
   * ヘッダ情報moptの最下位のバイトTを返します。
   * 
   * @return moptのt
   */
  public final int getMoptLeasByte() {
    int localByte = this.mopt;
    if (!isSameEndian()) {
      localByte = EndianTransformer.flip(this.mopt);
    }
    return localByte & 0xff;
  }

  /**
   * バージョン番号を返します。
   * 
   * @return バージョン番号
   */
  public final int getVersion() {
    return this.version;
  }

  /**
   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
   * 
   * @param data 整数行列のデータ
   * @param name 名前
   */
  public MxDataHead(final int[][] data, final String name) {
    byte M = 0x10; // Big endian (Java deals with big endian)
    byte O = 1; // row-wise
    byte P = 0x05; // 32bit singed integer
    byte T = MATRIX; // Matrix

    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;

    this.rowSize = data.length;
    this.columnSize = data[0].length;
    this.type = INTEGER_MATRIX;
    this.nameLength = (name != null ? name.length() + 1 : 0);
    this.name = name;
  }

  /**
   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
   * 
   * @param data boolean行列のデータ
   * @param name 名前
   */
  public MxDataHead(final boolean[][] data, final String name) {
    byte M = 0x10; // Big endian (Java deals with big endian)
    byte O = 1; // row-wise
    byte P = 0x02; // 8bit unsinged integer (要修正)
    byte T = MATRIX; // Matrix

    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;

    this.rowSize = data.length;
    this.columnSize = data[0].length;
    this.type = BOOLEAN_MATRIX;
    this.nameLength = (name != null ? name.length() + 1 : 0);
    this.name = name;
  }

  /**
   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
   * 
   * @param data 実行列のデータ
   * @param name 名前
   */
  public MxDataHead(final double[][] data, final String name) {
    byte M = 0x10; // Big endian (Java deals with big endian)
    byte O = 1; // row-wise
    byte P = 0x20; // double precision
    byte T = MATRIX; // Matrix

    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;

    this.rowSize = data.length;
    this.columnSize = data[0].length;
    this.type = REAL_MATRIX;
    this.nameLength = (name != null ? name.length() + 1 : 0);
    this.name = name;
  }

  /**
   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
   * 
   * @param data 複素行列のデータ
   * @param name 名前
   */
  public MxDataHead(final DoubleComplexNumber[][] data, final String name) {
    byte M = 0x10; // Big endian (Java deals with big endian)
    byte O = 1; // row-wise
    byte P = 0x20; // double precision
    byte T = MATRIX; // Matrix

    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;

    this.rowSize = data.length;
    this.columnSize = data[0].length;
    this.type = COMPLEX_MATRIX;
    this.nameLength = (name != null ? name.length() + 1 : 0);
    this.name = name;
  }

  /**
   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
   * 
   * @param data 多項式行列のデータ
   * @param name 名前
   */
  public MxDataHead(final DoublePolynomial[][] data, final String name) {
    byte M = 0x10; // Big endian (Java deals with big endian)
    byte O = 1; // row-wise
    byte P = 0x20; // double precision
    byte T = MATRIX; // Matrix

    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;
    this.rowSize = data.length;
    this.columnSize = data[0].length;

    this.type = REAL_POLYNOMIAL_MATRIX; // 実多項式行列
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[0].length; j++) {
        if (this.type == COMPLEX_POLYNOMIAL_MATRIX) {
          break;
        }
        if (data[i][j].isComplex()) {
          this.type = COMPLEX_POLYNOMIAL_MATRIX; // 複素多項式行列
          break;
        }
      }
    }
    this.nameLength = (name != null ? name.length() + 1 : 0);
    this.name = name;
  }

  /**
   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
   * 
   * @param data 多項式行列のデータ
   * @param name 名前
   */
  public MxDataHead(final DoubleComplexPolynomial[][] data, final String name) {
    byte M = 0x10; // Big endian (Java deals with big endian)
    byte O = 1; // row-wise
    byte P = 0x20; // double precision
    byte T = MATRIX; // Matrix

    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;
    this.rowSize = data.length;
    this.columnSize = data[0].length;

    this.type = REAL_POLYNOMIAL_MATRIX; // 実多項式行列
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[0].length; j++) {
        if (this.type == COMPLEX_POLYNOMIAL_MATRIX) {
          break;
        }
        if (data[i][j].isComplex()) {
          this.type = COMPLEX_POLYNOMIAL_MATRIX; // 複素多項式行列
          break;
        }
      }
    }
    this.nameLength = (name != null ? name.length() + 1 : 0);
    this.name = name;
  }

//  /**
//   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
//   * 
//   * @param data 多項式行列のデータ
//   * @param name 名前
//   */
//  public MxDataHead(final DoublePolynomial[][] data, final String name) {
//    byte M = 0x10; // Big endian (Java deals with big endian)
//    byte O = 1; // row-wise
//    byte P = 0x20; // double precision
//    byte T = MATRIX; // Matrix
//
//    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;
//    this.rowSize = data.length;
//    this.columnSize = data[0].length;
//
//    this.type = REAL_POLYNOMIAL_MATRIX; // 実多項式行列
//    for (int i = 0; i < data.length; i++) {
//      for (int j = 0; j < data[0].length; j++) {
//        if (this.type == COMPLEX_POLYNOMIAL_MATRIX) {
//          break;
//        }
//        if (data[i][j].isComplex()) {
//          this.type = COMPLEX_POLYNOMIAL_MATRIX; // 複素多項式行列
//          break;
//        }
//      }
//    }
//    this.nameLength = (name != null ? name.length() + 1 : 0);
//    this.name = name;
//  }

  /**
   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
   * 
   * @param data 有理多項式行列のデータ
   * @param name 名前
   */
  public MxDataHead(final DoubleRationalPolynomial[][] data, final String name) {
    byte M = 0x10; // Big endian (Java deals with big endian)
    byte O = 1; // row-wise
    byte P = 0x20; // double precision
    byte T = MATRIX; // Matrix

    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;
    this.rowSize = data.length;
    this.columnSize = data[0].length;

    this.type = REAL_RATIONAL_POLYNOMIAL_MATRIX; // 実有理多項式行列
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data.length; j++) {
        if (this.type == COMPLEX_RATIONAL_POLYNOMIAL_MATRIX) {
          break;
        }
        if (data[i][j].isComplex()) {
          this.type = COMPLEX_RATIONAL_POLYNOMIAL_MATRIX; // 複素有理多項式行列
          break;
        }
      }
    }
    this.nameLength = (name != null ? name.length() + 1 : 0);
    this.name = name;
  }

  /**
   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
   * 
   * @param data 有理多項式行列のデータ
   * @param name 名前
   */
  public MxDataHead(final DoubleComplexRationalPolynomial[][] data, final String name) {
    byte M = 0x10; // Big endian (Java deals with big endian)
    byte O = 1; // row-wise
    byte P = 0x20; // double precision
    byte T = MATRIX; // Matrix

    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;
    this.rowSize = data.length;
    this.columnSize = data[0].length;

    this.type = REAL_RATIONAL_POLYNOMIAL_MATRIX; // 実有理多項式行列
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data.length; j++) {
        if (this.type == COMPLEX_RATIONAL_POLYNOMIAL_MATRIX) {
          break;
        }
        if (data[i][j].isComplex()) {
          this.type = COMPLEX_RATIONAL_POLYNOMIAL_MATRIX; // 複素有理多項式行列
          break;
        }
      }
    }
    this.nameLength = (name != null ? name.length() + 1 : 0);
    this.name = name;
  }

//  /**
//   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
//   * 
//   * @param data 有理多項式行列のデータ
//   * @param name 名前
//   */
//  public MxDataHead(final DoubleRationalPolynomial[][] data, final String name) {
//    byte M = 0x10; // Big endian (Java deals with big endian)
//    byte O = 1; // row-wise
//    byte P = 0x20; // double precision
//    byte T = MATRIX; // Matrix
//
//    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;
//    this.rowSize = data.length;
//    this.columnSize = data[0].length;
//
//    this.type = REAL_RATIONAL_POLYNOMIAL_MATRIX; // 実有理多項式行列
//    for (int i = 0; i < data.length; i++) {
//      for (int j = 0; j < data.length; j++) {
//        if (this.type == COMPLEX_RATIONAL_POLYNOMIAL_MATRIX) {
//          break;
//        }
//        if (data[i][j].isComplex()) {
//          this.type = COMPLEX_RATIONAL_POLYNOMIAL_MATRIX; // 複素有理多項式行列
//          break;
//        }
//      }
//    }
//    this.nameLength = (name != null ? name.length() + 1 : 0);
//    this.name = name;
//  }

  /**
   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
   * 
   * @param data 文字列のデータ
   * @param name 名前
   */
  public MxDataHead(final String data, final String name) {
    byte M = 0x10; // Big endian (Java deals with big endian)
    byte O = 1; // row-wise
    byte P = 0x02; // 8bits unsinged integer
    byte T = STRING; // String

    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;
    this.rowSize = data.length() + 1;
    this.columnSize = 0; // dummy
    this.type = 0; // dummy

    this.nameLength = (name != null ? name.length() + 1 : 0);
    this.name = name;
  }

  /**
   * intのためのデータヘッドを生成します。
   * 
   * @param name 名前
   * @return 新しく生成された<code>MxDataHead</code>オブジェクト
   */
  public static MxDataHead createDataHeadForInt(final String name) {
    final MxDataHead dataHead = new MxDataHead();

    byte M = 0x10; // Big endian (Java deals with big endian)
    byte O = 1; // row-wise
    byte P = 0x05; // 32bits singed integer
    byte T = INTEGER; // Integer

    dataHead.mopt = (M << 24) | (O << 16) | (P << 8) | T;
    dataHead.rowSize = 0; // dummy
    dataHead.columnSize = 0; // dummy
    dataHead.type = 0; // dummy

    dataHead.nameLength = name.length() + 1;
    dataHead.name = name;

    return dataHead;
  }

  /**
   * Doubleのためのデータヘッドを生成します。
   * 
   * @param name 名前
   * @return 新しく生成された<code>MxDataHead</code>オブジェクト
   */
  public static MxDataHead createDataHeadForDouble(final String name) {
    final MxDataHead dataHead = new MxDataHead();

    byte M = 0x10; // Big endian (Java deals with big endian)
    byte O = 1; // row-wise
    byte P = 0x20; // double precision
    byte T = REAL; // Real

    dataHead.mopt = (M << 24) | (O << 16) | (P << 8) | T;
    dataHead.rowSize = 0; // dummy
    dataHead.columnSize = 0; // dummy
    dataHead.type = 0; // dummy

    dataHead.nameLength = name.length() + 1;
    dataHead.name = name;

    return dataHead;
  }

  /**
   * 倍精度複素数のためのデータヘッドを生成します。
   * 
   * @param name 名前
   * @return 新しく生成された<code>MxDataHead</code>オブジェクト
   */
  public static MxDataHead createDataHeadForComplexNumber(final String name) {
    final MxDataHead dataHead = new MxDataHead();

    byte M = 0x10; // Big endian (Java deals with big endian)
    byte O = 1; // row-wise
    byte P = 0x20; // double precision
    byte T = COMPLEX; // Complex

    dataHead.mopt = (M << 24) | (O << 16) | (P << 8) | T;
    dataHead.rowSize = 0; // dummy
    dataHead.columnSize = 0; // dummy
    dataHead.type = 0; // dummy

    dataHead.nameLength = name.length() + 1;
    dataHead.name = name;

    return dataHead;
  }

  /**
   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
   * 
   * @param data 多項式のデータ
   * @param name 名前
   */
  public MxDataHead(final DoublePolynomial data, final String name) {
    byte M = 0x10; // Big endian (Java deals with big endian)
    byte O = 1; // row-wise
    byte P = 0x20; // double precision
    byte T = POLYNOMIAL; // Polynomial

    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;

    this.rowSize = data.getDegree();
    this.columnSize = 0; // dummy
    if (data.isReal()) {
      this.type = 0;
    } else {
      this.type = 1;
    }
    this.nameLength = (name != null ? name.length() + 1 : 0);
    this.name = name;

  }

//  /**
//   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
//   * 
//   * @param data 多項式のデータ
//   * @param name 名前
//   */
//  public MxDataHead(final DoubleNumberPolynomial data, final String name) {
//    byte M = 0x10; // Big endian (Java deals with big endian)
//    byte O = 1; // row-wise
//    byte P = 0x20; // double precision
//    byte T = POLYNOMIAL; // Polynomial
//
//    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;
//
//    this.rowSize = data.getDegree();
//    this.columnSize = 0; // dummy
//    if (data.isReal()) {
//      this.type = 0;
//    } else {
//      this.type = 1;
//    }
//    this.nameLength = (name != null ? name.length() + 1 : 0);
//    this.name = name;
//  }

  /**
   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
   * 
   * @param data 多項式のデータ
   * @param name 名前
   */
  public MxDataHead(final DoubleComplexPolynomial data, final String name) {
    byte M = 0x10; // Big endian (Java deals with big endian)
    byte O = 1; // row-wise
    byte P = 0x20; // double precision
    byte T = POLYNOMIAL; // Polynomial

    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;

    this.rowSize = data.getDegree();
    this.columnSize = 0; // dummy
    if (data.isReal()) {
      this.type = 0;
    } else {
      this.type = 1;
    }
    this.nameLength = (name != null ? name.length() + 1 : 0);
    this.name = name;

  }

//  /**
//   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
//   * 
//   * @param data 多項式のデータ
//   * @param name 名前
//   */
//  public MxDataHead(final DoublePolynomial data, final String name) {
//    byte M = 0x10; // Big endian (Java deals with big endian)
//    byte O = 1; // row-wise
//    byte P = 0x20; // double precision
//    byte T = POLYNOMIAL; // Polynomial
//
//    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;
//
//    this.rowSize = data.getDegree();
//    this.columnSize = 0; // dummy
//    if (data.isReal()) {
//      this.type = 0;
//    } else {
//      this.type = 1;
//    }
//    this.nameLength = (name != null ? name.length() + 1 : 0);
//    this.name = name;
//  }

  /**
   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
   * 
   * @param data 有理多項式のデータ
   * @param name 名前
   */
  public MxDataHead(final DoubleRationalPolynomial data, final String name) {
    byte M = 0x10; // Big endian (Java deals with big endian)
    byte O = 1; // row-wise
    byte P = 0x20; // double precision
    byte T = RATIONAL; // RationalPolynomial

    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;

    this.rowSize = data.getNumeratorDegree();
    this.columnSize = data.getDenominatorDegree();
    if (data.isReal()) {
      this.type = 0;
    } else {
      this.type = 1;
    }
    this.nameLength = (name != null ? name.length() + 1 : 0);
    this.name = name;
  }

  /**
   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
   * 
   * @param data 有理多項式のデータ
   * @param name 名前
   */
  public MxDataHead(final DoubleComplexRationalPolynomial data, final String name) {
    byte M = 0x10; // Big endian (Java deals with big endian)
    byte O = 1; // row-wise
    byte P = 0x20; // double precision
    byte T = RATIONAL; // RationalPolynomial

    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;

    this.rowSize = data.getNumeratorDegree();
    this.columnSize = data.getDenominatorDegree();
    if (data.isReal()) {
      this.type = 0;
    } else {
      this.type = 1;
    }
    this.nameLength = (name != null ? name.length() + 1 : 0);
    this.name = name;
  }

//  /**
//   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
//   * 
//   * @param data 有理多項式のデータ
//   * @param name 名前
//   */
//  public MxDataHead(final DoubleRationalPolynomial data, final String name) {
//    byte M = 0x10; // Big endian (Java deals with big endian)
//    byte O = 1; // row-wise
//    byte P = 0x20; // double precision
//    byte T = RATIONAL; // RationalPolynomial
//
//    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;
//
//    this.rowSize = data.getNumeratorDegree();
//    this.columnSize = data.getDenominatorDegree();
//    if (data.isReal()) {
//      this.type = 0;
//    } else {
//      this.type = 1;
//    }
//    this.nameLength = (name != null ? name.length() + 1 : 0);
//    this.name = name;
//  }

  /**
   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
   * 
   * @param data リストのデータ
   * @param name 名前
   */
  public MxDataHead(final MatxList data, final String name) {
    byte M = 0x10; // Big endian (Java deals with big endian)
    byte O = 1; // row-wise
    byte P = 0x20; // double precision
    byte T = LIST; // List

    this.mopt = (M << 24) | (O << 16) | (P << 8) | T;

    this.rowSize = data.size();
    this.columnSize = 0; // dummy
    this.type = 0; // dummy
    this.nameLength = (name != null ? name.length() + 1 : 0);
    this.name = name;
  }

  /**
   * boolean型データのためのデータヘッドを生成します。
   * 
   * @param name 名前
   * @return 新しく生成された<code>MxDataHead</code>オブジェクト
   */
  public static MxDataHead createDataHeadForBoolean(final String name) {
    final MxDataHead dataHead = new MxDataHead();

    byte M = 0x10; // Big endian (Java deals with big endian)
    byte O = 1; // row-wise
    byte P = 0x02; // 8bits unsinged integer
    byte T = BOOLEAN; // List

    dataHead.mopt = (M << 24) | (O << 16) | (P << 8) | T;

    dataHead.rowSize = 0; // dummy
    dataHead.columnSize = 0; // dummy
    dataHead.type = 0; // dummy
    dataHead.nameLength = name.length() + 1;
    dataHead.name = name;

    return dataHead;
  }

  /**
   * 新しく生成された<code>MxDataHead</code>オブジェクトを初期化します。
   */
  public MxDataHead() {
    // nothing to do
  }

  /**
   * 出力ストリームにヘッダー情報を出力します。
   * 
   * @param output 出力ストリーム
   * @throws IOException 出力ストリームに出力できない場合
   */
  public final void write(final OutputStream output) throws IOException {
    DataOutputStream ds = new DataOutputStream(output);

    ds.writeInt(this.magic);
    ds.writeInt(this.version);
    ds.writeInt(this.dummy);
    ds.writeInt(this.mopt);
    ds.writeInt(this.rowSize);
    ds.writeInt(this.columnSize);
    ds.writeInt(this.type);
    ds.writeInt(this.nameLength);

    final byte[] buffer = (this.name + "\0").getBytes(Charset.forName("UTF-8")); //$NON-NLS-1$ //$NON-NLS-2$
    ds.write(buffer, 0, buffer.length);
  }

  /**
   * 入力ストリームからヘッダー情報を読み込みます。
   * 
   * @param input 入力ストリーム
   * @throws IOException 入力ストリームから読み込めない場合
   */
  public final void read(final InputStream input) throws IOException {
    final DataInputStream is = new DataInputStream(input);

    this.magic = is.readInt();
    this.version = is.readInt();
    this.dummy = is.readInt();
    this.mopt = is.readInt();
    this.rowSize = is.readInt();
    this.columnSize = is.readInt();
    this.type = is.readInt();
    this.nameLength = is.readInt();

    if (!isSameEndian()) {
      if (this.magic == 0xFFFFFFFF) {
        this.version = EndianTransformer.flip(this.version);
      } else {
        this.version = 4;
      }
      this.mopt = EndianTransformer.flip(this.mopt);
      this.rowSize = EndianTransformer.flip(this.rowSize);
      this.columnSize = EndianTransformer.flip(this.columnSize);
      this.type = EndianTransformer.flip(this.type);
      this.nameLength = EndianTransformer.flip(this.nameLength);
    }

    final byte[] buffer = new byte[this.nameLength];
    new DataInputStream(is).readFully(buffer);
    if (this.nameLength <= 1) {
      this.name = ""; //$NON-NLS-1$
    } else {
      this.name = new String(buffer, 0, this.nameLength - 1, Charset.forName("UTF-8")); //$NON-NLS-1$
    }
  }

  /**
   * ヘッダのエンディアンと実行中のCPUのエンディアンが等しいか判定します。
   * 
   * @return ヘッダのエンディアンと実行中のCPUのエンディアンが等しいければtrue、そうでなければfalse
   */
  public final boolean isSameEndian() {
    if ((this.mopt & 0XF00000F0) == 0) {
      // old type mx-format
      if ((this.mopt & 0X0F00000F) == 0) {
        // data was written in little-endian machine
        return false;
      }
      // data was written in big-endian machine
      return true;
    }
    // new type mx-format
    return (this.mopt & 0XFF000000) == 0X10000000;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.columnSize;
    result = prime * result + this.dummy;
    result = prime * result + this.magic;
    result = prime * result + this.mopt;
    result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
    result = prime * result + this.nameLength;
    result = prime * result + this.rowSize;
    result = prime * result + this.type;
    result = prime * result + this.version;
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    MxDataHead other = (MxDataHead)obj;
    if (this.columnSize != other.columnSize) return false;
    if (this.dummy != other.dummy) return false;
    if (this.magic != other.magic) return false;
    if (this.mopt != other.mopt) return false;
    if (this.name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!this.name.equals(other.name)) {
      return false;
    }
    if (this.nameLength != other.nameLength) return false;
    if (this.rowSize != other.rowSize) return false;
    if (this.type != other.type) return false;
    if (this.version != other.version) return false;
    return true;
  }
}