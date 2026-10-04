/*
 * $Id: MatxList.java,v 1.22 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matx;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mklab.nfc.matrix.BooleanMatrix;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.IntMatrix;
import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.DoubleComplexNumber;
import org.mklab.nfc.scalar.DoubleComplexPolynomial;
import org.mklab.nfc.scalar.DoubleComplexRationalPolynomial;
import org.mklab.nfc.scalar.DoubleNumber;
import org.mklab.nfc.scalar.DoublePolynomial;
import org.mklab.nfc.scalar.DoubleRationalPolynomial;
import org.mklab.nfc.scalar.Polynomial;
import org.mklab.nfc.scalar.RationalPolynomial;
import org.mklab.nfc.scalar.Scalar;


/**
 * MaTXのList型を表すクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.22 $
 */
public class MatxList implements Cloneable, MatxObject {

  /** 格納された値。 */
  private java.util.List<Object> list;

  /** デフォルトの出力フォーマット。 */
  private static String defaultFormat = "%G"; //$NON-NLS-1$

  /** 成分の出力フォーマット。*/
  private String format = MatxList.defaultFormat;

  /**
   * デフォルト出力フォーマットを設定します。
   * 
   * @param format デフォルト出力フォーマット
   */
  public static void setDefaultFormat(final String format) {
    MatxList.defaultFormat = format;
  }

  /**
   * デフォルト出力フォーマットを返します。
   * 
   * @return デフォルト出力フォーマット
   */
  public static String getDefaultFormat() {
    return MatxList.defaultFormat;
  }

  /**
   * 出力フォーマットを設定します。
   * 
   * @param format 出力フォーマット
   */
  public final void setFormat(final String format) {
    this.format = format;
  }

  /**
   * 出力フォーマットを返します。
   * 
   * @return 出力フォーマット
   */
  public final String getFormat() {
    return this.format;
  }

  /**
   * 空のインスタンスを生成します。
   */
  public MatxList() {
    this.list = new ArrayList<>();
  }

  /**
   * 成分がcount個のインスタンスを生成します。
   * 
   * @param count 成分の個数
   */
  public MatxList(final int count) {
    this();
    for (int i = 1; i <= count; i++) {
      this.list.add(Integer.valueOf(0));
    }
  }

  /**
   * 新しく生成された<code>MatxList</code>オブジェクトを初期化します。
   * @param values 成分
   */
  public MatxList(final Object[] values) {
    this();
    for (int i = 0; i < values.length; i++) {
      this.list.add(values[i]);
    }
  }

  /**
   * 新しく生成された<code>MatxList</code>オブジェクトを初期化します。
   * @param <E> 成分の型
   * @param valueList 成分
   */
  public <E> MatxList(final java.util.List<E> valueList) {
    this();
    this.list.addAll(valueList);
  }

  /**
   * {@link java.util.List}として値のリストを返します。
   * 
   * @return 値のリスト
   */
  public final List<Object> toList() {
    return java.util.Collections.unmodifiableList(this.list);
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

    if (size() != ((MatxList)opponent).size()) {
      return false;
    }

    int n = size();
    for (int i = 0; i < n; i++) {
      if (!this.list.get(i).equals(((MatxList)opponent).list.get(i))) {
        return false;
      }
    }

    return true;
  }

  /**
   * Override hashCode.
   * 
   * @return the Objects hash code.
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    final int prime = 31;
    hashCode = prime * hashCode + (this.list == null ? 0 : this.list.hashCode());
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone() {
    try {
      MatxList ans = (MatxList)super.clone();

      java.util.List<Object> ll = new ArrayList<>();

      for (Object element : this.list) {
        if (element instanceof Boolean) {
          ll.add(element);
        } else if (element instanceof Integer) {
          ll.add(element);
        } else if (element instanceof DoubleNumber) {
          ll.add(element);
        } else if (element instanceof DoubleComplexNumber) {
          ll.add(((DoubleComplexNumber)element).clone());
        } else if (element instanceof String) {
          ll.add(element);
        } else if (element instanceof DoublePolynomial) {
          ll.add(((DoublePolynomial)element).clone());
        } else if (element instanceof DoubleComplexPolynomial) {
          ll.add(((DoubleComplexPolynomial)element).clone());
        } else if (element instanceof DoubleRationalPolynomial) {
          ll.add(((DoubleRationalPolynomial)element).clone());
        } else if (element instanceof DoubleComplexRationalPolynomial) {
          ll.add(((DoubleComplexRationalPolynomial)element).clone());
        } else if (element instanceof Matrix) {
          ll.add(((Matrix<?,?>)element).createClone());
        } else if (element instanceof MatxList) {
          ll.add(((MatxList)element).clone());
        } else {
          throw new UnsupportedOperationException();
        }
      }

      ans.list = ll;
      return ans;
    } catch (CloneNotSupportedException e) {
      throw new InternalError(e.getMessage());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("{"); //$NON-NLS-1$
    for (int i = 0; i < this.list.size(); i++) {
      Object value = getObject(i + 1);
      if (value instanceof String) {
        buffer.append("\"" + value.toString() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
      } else if (value instanceof Scalar<?,?>) {
        ((Scalar<?,?>)value).setFormat(getFormat());
        buffer.append(value.toString());
      } else if (value instanceof DoubleNumber) {
        buffer.append(new DoubleNumber(((DoubleNumber)value).doubleValue()).toString(getFormat()));
      } else {
        buffer.append(value.toString());
      }
      if (i < this.list.size() - 1) {
        buffer.append(", "); //$NON-NLS-1$
      }
    }
    buffer.append("}"); //$NON-NLS-1$
    return buffer.toString();
  }

  /**
   * 要素をboolean型で取り出す。
   * 
   * @param index 要素の番号(1番から始まる
   * @return 指定された成分
   */
  public final boolean getBoolean(final int index) {
    final Object obj = this.list.get(index - 1);
    if (!(obj instanceof Boolean)) {
      throw new RuntimeException(Messages.getString("MatxList.0")); //$NON-NLS-1$
    }
    return ((Boolean)obj).booleanValue();
  }

  /**
   * 要素をint型で取り出す。
   * 
   * @param index 要素の番号(1番から始まる
   * @return 指定された成分
   */
  public final int getInt(final int index) {
    final Object obj = this.list.get(index - 1);
    if (!(obj instanceof Integer)) {
      throw new RuntimeException(Messages.getString("MatxList.6")); //$NON-NLS-1$
    }
    return ((Integer)obj).intValue();
  }

  /**
   * 要素をdouble型で取り出す。
   * 
   * @param index 要素の番号(1番から始まる
   * @return 指定された成分
   */
  public final double getDouble(final int index) {
    final Object obj = this.list.get(index - 1);
    if (obj instanceof DoubleNumber) {
      return ((DoubleNumber)obj).doubleValue();
    } 
    
    if (obj instanceof Integer) {
      return ((Integer)obj).intValue();
    } 

    throw new RuntimeException(Messages.getString("MatxList.7")); //$NON-NLS-1$
  }
  
  /**
   * 要素を{@link DoubleNumber}型で取り出す。
   * 
   * @param index 要素の番号(1番から始まる
   * @return 指定された成分
   */
  public final DoubleNumber getDoubleNumber(final int index) {
    final Object obj = this.list.get(index - 1);
    if (obj instanceof DoubleNumber) {
      return (DoubleNumber)obj;
    } 
    
    if (obj instanceof Integer) {
      return new DoubleNumber(((Integer)obj).intValue());
    } 

    throw new RuntimeException(Messages.getString("MatxList.7")); //$NON-NLS-1$
  }

  /**
   * 要素を{@link DoubleComplexNumber}型で取り出す。
   * 
   * @param index 要素の番号(1番から始まる
   * @return 指定された成分
   */
  public final DoubleComplexNumber getComplex(final int index) {
    final Object obj = this.list.get(index - 1);
    if (!(obj instanceof DoubleComplexNumber)) {
      throw new RuntimeException(Messages.getString("MatxList.8")); //$NON-NLS-1$
    }
    return ((DoubleComplexNumber)obj).clone();
  }

  /**
   * 要素を{@link Scalar}型で取り出す。
   * 
   * @param index 要素の番号(1番から始まる
   * @return 指定された成分
   */
  public final Scalar<?,?> getScalar(final int index) {
    final Object obj = this.list.get(index - 1);
    if (!(obj instanceof Scalar<?,?>)) {
      throw new RuntimeException(Messages.getString("MatxList.9")); //$NON-NLS-1$
    }
    return ((Scalar<?,?>)obj).clone();
  }

  /**
   * 要素を{@link String}型で取り出す。
   * 
   * @param index 要素の番号(1番から始まる
   * @return 指定された成分
   */
  public final String getString(final int index) {
    final Object obj = this.list.get(index - 1);
    if (!(obj instanceof String)) {
      throw new RuntimeException(Messages.getString("MatxList.10")); //$NON-NLS-1$
    }
    return new String(((String)obj).getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8")); //$NON-NLS-1$ //$NON-NLS-2$
  }

  /**
   * 要素を{@link Matrix}型で取り出す。
   * 
   * @param index 要素の番号(1番から始まる
   * @return 指定された成分
   */
  public final Matrix<?,?> getMatrix(final int index) {
    final Object obj = this.list.get(index - 1);
    if (!(obj instanceof Matrix)) {
      throw new RuntimeException(Messages.getString("MatxList.11")); //$NON-NLS-1$
    }
    return ((Matrix<?,?>)obj).createClone();
  }

  /**
   * 要素を{@link Polynomial}型で取り出す。
   * 
   * @param index 要素の番号(1番から始まる
   * @return 指定された成分
   */
  public final DoublePolynomial getPolynomial(final int index) {
    final Object obj = this.list.get(index - 1);
    if (!(obj instanceof DoublePolynomial)) {
      throw new RuntimeException(Messages.getString("MatxList.12")); //$NON-NLS-1$
    }
    return ((DoublePolynomial)obj).clone();
  }

  /**
   * 要素を{@link RationalPolynomial}型で取り出す。
   * 
   * @param index 要素の番号(1番から始まる
   * @return 指定された成分
   */
  public final DoubleRationalPolynomial getRationalPolynomial(final int index) {
    final Object obj = this.list.get(index - 1);
    if (!(obj instanceof DoubleRationalPolynomial)) {
      throw new RuntimeException(Messages.getString("MatxList.13")); //$NON-NLS-1$
    }
    return ((DoubleRationalPolynomial)obj).clone();
  }

  /**
   * 要素を{@link MatxList}型で取り出す。
   * 
   * @param index 要素の番号(1番から始まる
   * @return 指定された成分
   */
  public final MatxList getList(final int index) {
    final Object obj = this.list.get(index - 1);
    if (!(obj instanceof MatxList)) {
      throw new RuntimeException(Messages.getString("MatxList.14")); //$NON-NLS-1$
    }
    return (MatxList)((MatxList)obj).clone();
  }

  /**
   * 要素を{@link Object}型で取り出す。
   * 
   * @param index 要素の番号(1番から始まる
   * @return 指定された成分
   */
  public final Object getObject(final int index) {
    return this.list.get(index - 1);
  }

  /**
   * 部分リストを返します。
   * 
   * @param from 下端(1番から始まる
   * @param to 上端(1番から始まる
   * @return 部分リスト
   */
  public final MatxList getSubList(final int from, final int to) {
    final java.util.List<Object> ll = this.list.subList(from - 1, to);
    return (MatxList)(new MatxList(ll).clone());
  }

  /**
   * 部分リストを返します。
   * 
   * @param index 成分の指数(1番から始まる
   * @return 部分リスト
   */
  public final MatxList getSubList(final IntMatrix index) {
    if (index.getRowSize() != 1) {
      throw new IllegalArgumentException(Messages.getString("MatxList.15")); //$NON-NLS-1$
    }

    final int length = index.getColumnSize();
    final MatxList ans = new MatxList();
    for (int i = 1; i <= length; i++) {
      final int k = index.getIntElement(i);
      ans.list.add(this.list.get(k - 1));
    }

    return ans;
  }

  /**
   * 部分リストを設定します。
   * 
   * @param from 下端(1番から始まる
   * @param to 上端(1番から始まる
   * @param ll 設定するリスト
   * @return 部分リストを設定後のリスト
   */
  public final MatxList setSubList(final int from, final int to, final MatxList ll) {
    int i = from - 1;
    for (Iterator<Object> it = ll.list.iterator(); it.hasNext(); i++) {
      if (to <= i) {
        throw new IllegalArgumentException();
      }
      this.list.set(i, it.next());
    }

    return this;
  }

  /**
   * 部分リストを設定します。
   * 
   * @param index 指数(1番から始まる
   * @param ll 設定するリスト
   * @return 部分リストを設定後のリスト
   */
  public final MatxList setSubList(final IntMatrix index, final MatxList ll) {
    if (index.getRowSize() != 1) {
      throw new IllegalArgumentException(Messages.getString("MatxList.16")); //$NON-NLS-1$
    }

    int length = index.getColumnSize();
    for (int i = 0; i < length; i++) {
      final int k = index.getIntElement(i + 1);
      this.list.set(k - 1, ll.list.get(i));
    }

    return this;
  }

  /**
   * valueをリストの最後に追加します。
   * 
   * @param value 追加するboolean
   */
  public final void add(final boolean value) {
    this.list.add(Boolean.valueOf(value));
  }
  
  /**
   * valueをリストの最後に追加します。
   * 
   * @param value 追加するboolean
   */
  public final void add(final Boolean value) {
    this.list.add(value);
  }

  /**
   * valueをリストの最後に追加します。
   * 
   * @param value 追加する整数
   */
  public final void add(final int value) {
    this.list.add(Integer.valueOf(value));
  }
  
  /**
   * valueをリストの最後に追加します。
   * 
   * @param value 追加する整数
   */
  public final void add(final Integer value) {
    this.list.add(value);
  }

  /**
   * valueをリストの最後に追加します。
   * 
   * @param value 追加する実数
   */
  public final void add(final double value) {
    this.list.add(new DoubleNumber(value));
  }
  
  /**
   * valueをリストの最後に追加します。
   * 
   * @param value 追加する実数
   */
  public final void add(Double value) {
    this.list.add(new DoubleNumber(value.doubleValue()));
  }
  
  /**
   * valueをリストの最後に追加します。
   * 
   * @param value 追加する実数
   */
  public final void add(final DoubleNumber value) {
    this.list.add(value);
  }

  /**
   * valueをリストの最後に追加します。
   * 
   * @param value 追加する複素数
   */
  public final void add(final DoubleComplexNumber value) {
    this.list.add(value);
  }

  /**
   * valueをリストの最後に追加します。
   * 
   * @param value 追加する文字列
   */
  public final void add(final String value) {
    this.list.add(value);
  }

  /**
   * valueをリストの最後に追加します。
   * 
   * @param value 追加する多項式
   */
  public final void add(final DoubleComplexPolynomial value) {
    this.list.add(value);
  }

  /**
   * valueをリストの最後に追加します。
   * 
   * @param value 追加する多項式
   */
  public final void add(final DoublePolynomial value) {
    this.list.add(value);
  }

  /**
   * valueをリストの最後に追加します。
   * 
   * @param value 追加する有理多項式
   */
  public final void add(final DoubleComplexRationalPolynomial value) {
    this.list.add(value);
  }

  /**
   * valueをリストの最後に追加します。
   * 
   * @param value 追加する有理多項式
   */
  public final void add(final DoubleRationalPolynomial value) {
    this.list.add(value);
  }

  /**
   * valueをリストの最後に追加します。
   * 
   * @param value 追加する行列
   */
  public final void add(final Matrix<?,?> value) {
    this.list.add(value);
  }

  /**
   * valueをリストの最後に追加します。
   * 
   * @param value 追加するリスト
   */
  public final void add(final MatxList value) {
    this.list.add(value);
  }

  /**
   * valueをリストに結合します。
   * 
   * @param value 結合するリスト
   * @return 生成されたリスト
   */
  public final MatxList append(final MatxList value) {
    final MatxList ans = (MatxList)clone();
    ans.list.addAll(value.list);
    return ans;
  }

  /**
   * リストの成分をnセット並べたリストを生成します。
   * 
   * @param count 個数
   * @return 生成されたリスト
   */
  public final MatxList multiply(final int count) {
    final MatxList ll = new MatxList(0);
    for (int i = 0; i < count; i++) {
      ll.list.addAll(((MatxList)this.clone()).list);
    }
    return ll;
  }

  /**
   * リストのi番目にbooleanを設定します。
   * 
   * @param index 指数(1番から始まる
   * @param value 整数
   */
  public final void set(final int index, final boolean value) {
    this.list.set(index - 1, Boolean.valueOf(value));
  }
  

  /**
   * リストのi番目にbooleanを設定します。
   * 
   * @param index 指数(1番から始まる
   * @param value 整数
   */
  public final void set(final int index, final Boolean value) {
    this.list.set(index - 1, value);
  }

  /**
   * リストのi番目に整数valueを設定します。
   * 
   * @param index 指数(1番から始まる
   * @param value 整数
   */
  public final void set(final int index, final int value) {
    this.list.set(index - 1, Integer.valueOf(value));
  }
  
  /**
   * リストのi番目に整数valueを設定します。
   * 
   * @param index 指数(1番から始まる
   * @param value 整数
   */
  public final void set(final int index, final Integer value) {
    this.list.set(index - 1, value);
  }

  /**
   * リストのi番目に実数valueを設定します。
   * 
   * @param index 指数(1番から始まる
   * @param value 実数
   */
  public final void set(final int index, final double value) {
    this.list.set(index - 1, new DoubleNumber(value));
  }

  /**
   * リストのi番目に実数valueを設定します。
   * 
   * @param index 指数(1番から始まる
   * @param value 実数
   */
  public final void set(final int index, final Double value) {
    this.list.set(index - 1, new DoubleNumber(value.doubleValue()));
  }
  
  /**
   * リストのi番目に実数valueを設定します。
   * 
   * @param index 指数(1番から始まる
   * @param value 実数
   */
  public final void set(final int index, final DoubleNumber value) {
    this.list.set(index - 1, value.clone());
  }
  
  /**
   * リストのi番目に複素数valueを設定します。
   * 
   * @param index 指数(1番から始まる
   * @param value 複素数
   */
  public final void set(final int index, final DoubleComplexNumber value) {
    this.list.set(index - 1, value.clone());
  }

  /**
   * リストのi番目に文字列valueを設定します。
   * 
   * @param index 指数(1番から始まる
   * @param value オブジェクト
   */
  public final void set(final int index, final String value) {
    this.list.set(index - 1, value);
  }

  /**
   * リストのi番目に多項式valueを設定します。
   * 
   * @param index 指数(1番から始まる
   * @param value 多項式
   */
  public final void set(final int index, final DoublePolynomial value) {
    this.list.set(index - 1, value.clone());
  }

  /**
   * リストのi番目に有理多項式valueを設定します。
   * 
   * @param index 指数(1番から始まる
   * @param value 有理多項式
   */
  public final void set(final int index, final DoubleRationalPolynomial value) {
    this.list.set(index - 1, value.clone());
  }

  /**
   * リストのi番目に行列valueを設定します。
   * 
   * @param index 指数(1番から始まる
   * @param value 行列
   */
  public final void set(final int index, final Matrix<?,?> value) {
    this.list.set(index - 1, value);
  }

  /**
   * リストのi番目にリストvalueを設定します。
   * 
   * @param index 指数(1番から始まる
   * @param value リスト
   */
  public final void set(final int index, final MatxList value) {
    this.list.set(index - 1, value.clone());
  }

  /**
   * リストの各成分と引数の値が一致するか判定します。
   * 
   * @param opponent 真偽値
   * @return 成分毎の比較結果(boolean行列)
   */
  private BooleanMatrix compareElementWise(final boolean opponent) {
    return compareElementWise(Boolean.valueOf(opponent));
  }

  /**
   * リストの各成分と引数の値が一致するか判定します。
   * 
   * @param opponent 整数
   * @return 成分毎の比較結果(boolean行列)
   */
  private BooleanMatrix compareElementWise(final int opponent) {
    return compareElementWise(Integer.valueOf(opponent));
  }

  /**
   * リストの各成分と引数の値が一致するか判定します。
   * 
   * @param opponent 実数
   * @return 成分毎の比較結果(boolean行列)
   */
  private BooleanMatrix compareElementWise(final double opponent) {
    return compareElementWise(new DoubleNumber(opponent));
  }

  /**
   * リストの各成分と引数の値が一致するか判定します。
   * 
   * @param opponent 比較するオブジェクト
   * @return 成分毎の比較結果(boolean行列)
   */
  private BooleanMatrix compareElementWise(final Object opponent) {
    final int length = size();
    final BooleanMatrix ans = new BooleanMatrix(1, length);
    for (int i = 0; i < length; i++) {
      final Object oo = this.list.get(i);
      ans.setElement(i + 1, opponent.equals(oo));
    }
    return ans;
  }

  /**
   * 各要素とopponentをoperatorで指定された演算子 (".==", ".!=")で比較し, BooleanMatrixで返します。 
   * 
   * @param opponent 比較対象
   * @param operator 比較演算子
   * @return 各要素に比較結果が入ったBooleanMatrix
   */
  public final BooleanMatrix compareElementWise(final boolean opponent, final String operator) {
    if (operator.equals(".==")) { //$NON-NLS-1$
      return compareElementWise(opponent);
    } 
    
    if (operator.equals(".!=")) { //$NON-NLS-1$
      return compareElementWise(opponent).notElementWise();
    } 

    throw new IllegalArgumentException();
  }

  /**
   * 各要素とopponentをoperatorで指定された演算子 (".==", ".!=")で比較し, BooleanMatrixで返します。 
   * 
   * @param opponent 比較対象
   * @param operator 比較演算子
   * @return 各要素に比較結果が入ったBooleanMatrix
   */
  public final BooleanMatrix compareElementWise(final Matrix<?,?> opponent, final String operator) {
    if (operator.equals(".==")) { //$NON-NLS-1$
      return compareElementWise(opponent);
    } 
    
    if (operator.equals(".!=")) { //$NON-NLS-1$
      return compareElementWise(opponent).notElementWise();
    } 

    throw new IllegalArgumentException();
  }

  /**
   * 各要素とopponentをoperatorで指定された演算子 (".==", ".!=")で比較し, BooleanMatrixで返します。 
   * 
   * @param opponent 比較対象
   * @param operator 比較演算子
   * @return 各要素に比較結果が入ったBooleanMatrix
   */
  public final BooleanMatrix compareElementWise(final DoubleComplexRationalPolynomial opponent, final String operator) {
    if (operator.equals(".==")) { //$NON-NLS-1$
      return compareElementWise(opponent);
    } 
    
    if (operator.equals(".!=")) { //$NON-NLS-1$
      return compareElementWise(opponent).notElementWise();
    } 

    throw new IllegalArgumentException();
  }

  /**
   * 各要素とopponentをoperatorで指定された演算子 (".==", ".!=")で比較し, BooleanMatrixで返します。 
   * 
   * @param opponent 比較対象
   * @param operator 比較演算子
   * @return 各要素に比較結果が入ったBooleanMatrix
   */
  public final BooleanMatrix compareElementWise(final DoubleRationalPolynomial opponent, final String operator) {
    if (operator.equals(".==")) { //$NON-NLS-1$
      return compareElementWise(opponent);
    } 
    
    if (operator.equals(".!=")) { //$NON-NLS-1$
      return compareElementWise(opponent).notElementWise();
    } 

    throw new IllegalArgumentException();
  }

  /**
   * 各要素とopponentをoperatorで指定された演算子 (".==", ".!=")で比較し, BooleanMatrixで返します。 
   * 
   * @param opponent 比較対象
   * @param operator 比較演算子
   * @return 各要素に比較結果が入ったBooleanMatrix
   */
  public final BooleanMatrix compareElementWise(final DoubleComplexPolynomial opponent, final String operator) {
    if (operator.equals(".==")) { //$NON-NLS-1$
      return compareElementWise(opponent);
    } 
    
    if (operator.equals(".!=")) { //$NON-NLS-1$
      return compareElementWise(opponent).notElementWise();
    } 

    throw new IllegalArgumentException();
  }

  /**
   * 各要素とopponentをoperatorで指定された演算子 (".==", ".!=")で比較し, BooleanMatrixで返します。 
   * 
   * @param opponent 比較対象
   * @param operator 比較演算子
   * @return 各要素に比較結果が入ったBooleanMatrix
   */
  public final BooleanMatrix compareElementWise(final DoublePolynomial opponent, final String operator) {
    if (operator.equals(".==")) { //$NON-NLS-1$
      return compareElementWise(opponent);
    } 
    
    if (operator.equals(".!=")) { //$NON-NLS-1$
      return compareElementWise(opponent).notElementWise();
    } 

    throw new IllegalArgumentException();
  }

  /**
   * 各要素とopponentをoperatorで指定された演算子 (".==", ".!=")で比較し, BooleanMatrixで返します。 
   * 
   * @param opponent 比較対象
   * @param operator 比較演算子
   * @return 各要素に比較結果が入ったBooleanMatrix
   */
  public final BooleanMatrix compareElementWise(final String opponent, final String operator) {
    if (operator.equals(".==")) { //$NON-NLS-1$
      return compareElementWise(opponent);
    } 
    
    if (operator.equals(".!=")) { //$NON-NLS-1$
      return compareElementWise(opponent).notElementWise();
    }

    throw new IllegalArgumentException();
  }

  /**
   * 各要素とopponentをoperatorで指定された演算子 (".==", ".!=")で比較し, BooleanMatrixで返します。
   * 
   * @param opponent 比較対象
   * @param operator 比較演算子
   * @return 各要素に比較結果が入ったBooleanMatrix
   */
  public final BooleanMatrix compareElementWise(final DoubleComplexNumber opponent, final String operator) {
    if (operator.equals(".==")) { //$NON-NLS-1$
      return compareElementWise(opponent);
    } 
    
    if (operator.equals(".!=")) { //$NON-NLS-1$
      return compareElementWise(opponent).notElementWise();
    }

    throw new IllegalArgumentException();
  }

  /**
   * 各要素とopponentをoperatorで指定された演算子 (". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")で比較し, BooleanMatrixで返します。
   * 
   * @param opponent 比較対象
   * @param operator 比較演算子
   * @return 各要素に比較結果が入ったBooleanMatrix
   */
  public final BooleanMatrix compareElementWise(final double opponent, final String operator) {
    if (operator.equals(".==")) { //$NON-NLS-1$
      return compareElementWise(opponent);
    } 
    
    if (operator.equals(".!=")) { //$NON-NLS-1$
      return compareElementWise(opponent).notElementWise();
    }

    final int size = size();
    final double[] data = new double[size];
    for (int i = 0; i < size; i++) {
      final Object o = this.list.get(i);
      if (o instanceof Integer) {
        data[i] = ((Integer)o).intValue();
      } else if (o instanceof DoubleNumber) {
        data[i] = ((DoubleNumber)o).doubleValue();
      } else {
        throw new IllegalArgumentException();
      }
    }
    
    return new DoubleMatrix(data).compareElementWise(operator, opponent);
  }

  /**
   * 各要素とvalをoperatorで指定された演算子 (". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")で比較し, BooleanMatrixで返します。
   * 
   * @param opponent 比較対象
   * @param operator 比較演算子
   * @return 各要素に比較結果が入ったBooleanMatrix
   */
  public final BooleanMatrix compareElementWise(final int opponent, final String operator) {
    if (operator.equals(".==")) { //$NON-NLS-1$
      return compareElementWise(opponent);
    } 
    
    if (operator.equals(".!=")) { //$NON-NLS-1$
      return compareElementWise(opponent).notElementWise();
    }

    return compareElementWise((double)opponent, operator);
  }

  /**
   * aと要素毎にoperatorで指定された演算子 (". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")で比較し, BooleanMatrixで返します。 
   * 
   * @param opponent 比較対象
   * @param operator 比較演算子
   * @return 各要素に比較結果が入ったBooleanMatrix
   */
  public final BooleanMatrix compareElementWise(final MatxList opponent, final String operator) {
    if (operator.equals(".==")) { //$NON-NLS-1$
      return compareElementWise(opponent);
    } 
    
    if (operator.equals(".!=")) { //$NON-NLS-1$
      return compareElementWise(opponent).notElementWise();
    }

    int size = size();
    if (size != opponent.size()) {
      throw new IllegalArgumentException();
    }

    final double[] data1 = new double[size];
    final double[] data2 = new double[size];
    for (int i = 0; i < size; i++) {
      final Object o1 = this.list.get(i);
      if (o1 instanceof Integer) {
        data1[i] = ((Integer)o1).intValue();
      } else if (o1 instanceof DoubleNumber) {
        data1[i] = ((DoubleNumber)o1).doubleValue();
      } else {
        throw new IllegalArgumentException();
      }

      final Object o2 = opponent.list.get(i);
      if (o2 instanceof Integer) {
        data2[i] = ((Integer)o2).intValue();
      } else if (o2 instanceof DoubleNumber) {
        data2[i] = ((DoubleNumber)o2).doubleValue();
      } else {
        throw new IllegalArgumentException();
      }
    }
    final DoubleMatrix x1 = new DoubleMatrix(data1);
    final DoubleMatrix x2 = new DoubleMatrix(data2);

    return x1.compareElementWise(operator, x2);
  }

  /**
   * 成分毎に値が一致するか判定します。
   * 
   * <p>成分毎にクラスが一致するものを選びます。</p>
   * 
   * @param clazz 比較するクラス
   * @return boolean行列(クラスが一致すると真)
   */
  public final BooleanMatrix isSameClass(final Class<?> clazz) {
    final int length = size();
    final BooleanMatrix ans = new BooleanMatrix(1, length);
    for (int i = 0; i < length; i++) {
      ans.setElement(i + 1, this.list.get(i).getClass() == clazz);
    }

    return ans;
  }

  /**
   * リストのサイズを返します。
   * 
   * @return リストのサイズ
   */
  public final int size() {
    return this.list.size();
  }

  /**
   * ansという名前で標準出力に出力します。
   */
  public final void print() {
    print("ans"); //$NON-NLS-1$
  }

  /**
   * 標準出力に出力します。
   * 
   * @param name 変数名
   */
  public final void print(final String name) {
    System.out.print(name + " = "); //$NON-NLS-1$
    System.out.println(toString());
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
   * {@inheritDoc}
   */
  public final void writeMxFormat(final DataOutputStream output, final String name) throws IOException {
    final MxDataHead head = new MxDataHead(this, name);

    head.write(output);

    //final DataOutputStream ds = new DataOutputStream(new BufferedOutputStream(output));
    final int length = size();

    for (int i = 0; i < length; i++) {
      final Object element = getObject(i + 1);

      if (element instanceof Boolean) {
        output.writeInt(MxDataHead.BOOLEAN);
        MatxBoolean.writeMxFormat(((Boolean)element).booleanValue(), output, name);
      } else if (element instanceof String) {
        output.writeInt(MxDataHead.STRING);
        new MatxString(((String)element)).writeMxFormat(output, name);
      } else if (element instanceof Integer) {
        output.writeInt(MxDataHead.INTEGER);
        MatxInteger.writeMxFormat(((Integer)element).intValue(), output, name);
      } else if (element instanceof DoubleNumber) {
        output.writeInt(MxDataHead.REAL);
        ((DoubleNumber)element).writeMxFormat(output, name);
      } else if (element instanceof DoubleComplexNumber) {
        output.writeInt(MxDataHead.COMPLEX);
        ((DoubleComplexNumber)element).writeMxFormat(output, name);
      } else if (element instanceof DoublePolynomial) {
        output.writeInt(MxDataHead.POLYNOMIAL);
        ((DoublePolynomial)element).writeMxFormat(output, name);
      } else if (element instanceof DoubleRationalPolynomial) {
        output.writeInt(MxDataHead.RATIONAL);
        ((DoubleRationalPolynomial)element).writeMxFormat(output, name);
      } else if (element instanceof Matrix) {
        output.writeInt(MxDataHead.MATRIX);
        ((MatxObject)element).writeMxFormat(output, name);
      } else if (element instanceof MatxList) {
        output.writeInt(MxDataHead.LIST);
        ((MatxList)element).writeMxFormat(output, name);
      } else {
        throw new RuntimeException(Messages.getString("MatxList.19")); //$NON-NLS-1$
      }

      output.flush();
    }
  }

  /**
   * 指定されたMxファイルを読み込みます。
   * 
   * @param file 読み込むmxファイル
   * @return 読み込んだ行列
   * @exception IOException 読み込みエラー
   */
  public static MatxList readMxFormat(final File file) throws IOException {
    try (final DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
      final MatxList ans = MatxList.readMxFormat(input);
      return ans;
    }
  }

  /**
   * 入力ストリームからMx形式のデータを読み込みます。
   * 
   * @param input 入力ストリーム
   * @return 読み込んだ行列
   * @exception IOException 入力ストリームから読み込めない場合
   */
  public static MatxList readMxFormat(final InputStream input) throws IOException {
    final MxDataHead head = new MxDataHead();
    head.read(input);

    final DataInputStream is = new DataInputStream(input);

    final int length = head.getLength();
    final MatxList list = new MatxList(length);

    for (int i = 0; i < length; i++) {
      final int type = is.readInt();

      switch (type) {
        case MxDataHead.BOOLEAN:
          boolean b = MatxBoolean.readMxFormat(input);
          list.set(i + 1, b);
          break;
        case MxDataHead.STRING:
          MatxString s = MatxString.readMxFormat(input);
          list.set(i + 1, s.toString());
          break;
        case MxDataHead.INTEGER:
          int ii = MatxInteger.readMxFormat(input);
          list.set(i + 1, ii);
          break;
        case MxDataHead.REAL:
          double d = MatxDouble.readMxFormat(input);
          list.set(i + 1, d);
          break;
        case MxDataHead.COMPLEX:
          DoubleComplexNumber c = DoubleComplexNumber.readMxFormat(input);
          list.set(i + 1, c);
          break;
        case MxDataHead.POLYNOMIAL:
          DoublePolynomial p = DoublePolynomial.readMxFormat(input);
          list.set(i + 1, p);
          break;
        case MxDataHead.RATIONAL:
          DoubleRationalPolynomial r = DoubleRationalPolynomial.readMxFormat(input);
          list.set(i + 1, r);
          break;
        case MxDataHead.MATRIX:
          Matrix<?,?> m = MatxMatrix.readMxFormat(input);
          list.set(i + 1, m);
          break;
        case MxDataHead.LIST:
          MatxList ls = MatxList.readMxFormat(input);
          list.set(i + 1, ls);
          break;
        default:
          throw new RuntimeException(Messages.getString("MatxList.20")); //$NON-NLS-1$
      }

    }
    return list;
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMmFormat(final File file, final String name) throws IOException {
    try (final Writer output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")))) { //$NON-NLS-1$
      writeMmFormat(output, name, true);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMmFormat(final Writer output, final String name, final boolean withNewLine) throws IOException {
    final String newLine = System.getProperty("line.separator"); //$NON-NLS-1$
    final StringBuffer sb1 = new StringBuffer();
    if (name.length() != 0) {
      sb1.append(name);
      sb1.append(" = "); //$NON-NLS-1$
      sb1.append(newLine);
    }
    sb1.append("{"); //$NON-NLS-1$
    
    final Writer wt = output;
    wt.write(sb1.toString());

    final int n = size();
    for (int i = 0; i < n; i++) {
      final Object element = getObject(i + 1);
      if (element instanceof Boolean) {
        MatxBoolean.writeMmFormat(((Boolean)element).booleanValue(), output, "", false); //$NON-NLS-1$
      } else if (element instanceof String) {
        MatxString.writeMmFormat((String)element, output, "", false); //$NON-NLS-1$
      } else if (element instanceof MatxString) {
        ((MatxString)element).writeMmFormat(output, "", false); //$NON-NLS-1$
      } else if (element instanceof Integer) {
        MatxInteger.writeMmFormat(((Integer)element).intValue(), output, "", false); //$NON-NLS-1$
      } else if (element instanceof MatxInteger) {
        ((MatxInteger)element).writeMmFormat(output, "", false); //$NON-NLS-1$
      } else if (element instanceof DoubleNumber) {
        ((DoubleNumber)element).writeMmFormat(output, "", false); //$NON-NLS-1$
        //MatxDouble.writeMmFormat(((Double)element).doubleValue(), output, "", false); //$NON-NLS-1$
      } else if (element instanceof DoubleNumber) {
        MatxDouble.writeMmFormat(((DoubleNumber)element).doubleValue(), output, "", false); //$NON-NLS-1$
      } else if (element instanceof DoubleComplexNumber) {
        ((DoubleComplexNumber)element).writeMmFormat(output, "", false); //$NON-NLS-1$
      } else if (element instanceof DoublePolynomial) {
        ((DoublePolynomial)element).writeMmFormat(output, "", false); //$NON-NLS-1$
      } else if (element instanceof DoubleRationalPolynomial) {
        ((DoubleRationalPolynomial)element).writeMmFormat(output, "", false); //$NON-NLS-1$
      } else if (element instanceof Matrix) {
        ((MatxObject)element).writeMmFormat(output, "", false); //$NON-NLS-1$
      } else if (element instanceof MatxList) {
        ((MatxList)element).writeMmFormat(output, "", false); //$NON-NLS-1$
      } else {
        throw new RuntimeException(Messages.getString("MatxList.30")); //$NON-NLS-1$
      }

      if (i != n - 1) {
        wt.write(","); //$NON-NLS-1$
      }
    }

    //sb1 = sb1.delete(0, sb1.length());
    
    final StringBuffer sb2 = new StringBuffer();
    sb2.append("}"); //$NON-NLS-1$

    if (withNewLine) {
      sb2.append(";"); //$NON-NLS-1$
      sb2.append(newLine);
      sb2.append(newLine);
    }

    wt.write(sb2.toString());
    wt.flush();
  }

  /**
   * {@inheritDoc}
   */
  public final String toMmString() {
    try (final StringWriter output = new StringWriter()) {
      writeMmFormat(output, "", false); //$NON-NLS-1$
      return output.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final String toMmString(final String valueFormat) {
    return toMmString();
  }
}