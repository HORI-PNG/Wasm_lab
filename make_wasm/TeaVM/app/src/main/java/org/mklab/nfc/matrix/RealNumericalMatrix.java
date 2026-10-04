/**
 * Copyright (C) 2021 MKLab.org (Koga Laboratory)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mklab.nfc.matrix;

import org.mklab.nfc.eig.EigenSolution;
import org.mklab.nfc.eig.RealQZDecomposition;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;

/**
 * 複素数値行列を表すインターフェースです。
 * 
 * @author koga
 * @version $Revision$, 2021/08/18
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS> type of complex scalar
 * @param <CM> type of complex matrix
 */
public interface RealNumericalMatrix<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> extends NumericalMatrix<RS,RM> {
  /**
   * 複素行列を返します。
   * 
   * @param realPart 実部行列
   * @param imagPart 虚部行列
   * @return 複素行列
   */
  CM createComplex(RM realPart, RM imagPart);
  
  /**
   * 固有値を成分とする列ベクトルを返します。
   * 
   * <p>固有値は、実部の降順に並べられます。
   * 
   * @return 固有値を成分とする列ベクトル(実部について降順)
   */
  CM eigenValue();
  
  /**
   * 一般化固有値からなる列ベクトルを返します。
   * 
   * <p>固有値は、実部の降順に並べられます。
   * 
   * @param B 一般化固有値を求める対となる行列
   * @return 固有値を降順に並べた列ベクトル
   */
  CM eigenValue(RM B);

  /**
   * (右)固有ベクトルを列とする行列を返します。
   * 
   * <p>固有ベクトルはノルムが1.0となるよう正規化されます。 固有ベクトルは、固有値の実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。
   * 
   * @return 固有ベクトル
   */
  CM eigenVector();

  /**
   * 一般化固有ベクトルを返します。
   * 
   * @param B 一般化固有ベクトルを求める対となる行列
   * @return 一般化固有ベクトル
   */
  CM eigenVector(RM B);
  
  /**
   * 固有値を対角成分とする対角行列D、固有値に対応する固有ベクトルを横方向に並べた行列Xを返します。
   * 
   * <p>これらの行列の間には、
   * 
   * <blockquote> A * X = X * D </blockquote>
   * 
   * の関係が成り立ちます。固有ベクトルはノルムが1.0となるよう正規化されます。 固有値は実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。
   * 
   * @return 第1成分D、第2成分Xとする{@link Matrix}の配列
   */
  EigenSolution<RS,RM,CS,CM> eigenDecompose();

  /**
   * 一般化固有値を対角成分とする対角行列 D と対応する一般化固有ベクトルを列とする行列 X を返します。
   * 
   * <p>これらの行列の間には、
   * 
   * <blockquote> A * X = X * D </blockquote>
   * 
   * の関係が成り立ちます。固有ベクトルはノルムが1.0となるよう正規化されます。 固有値は、実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。
   * 
   * @param B 一般化固有分解を求める対となる行列
   * @return 第1成分D,第2成分Xとする {@link Matrix}の配列
   */
  EigenSolution<RS,RM,CS,CM> eigenDecompose(RM B);
  
  /**
   * 2 のべき乗の長さのベクトルについて、基底が 2 の 高速フーリエ変換を計算します。
   * 
   * @return 高速フーリエ変換結果
   */
  CM fft();

  /**
   * <code>dataSize</code>点まで基底が 2 の高速フーリエ変換をします。
   * 
   * <p>自身 の長さが<code>dataSize</code>より短いとき、ゼロが後ろに付け加えられ、 自身 の長さが<code>dataSize</code>より長いとき、<code>dataSize</code> 番目以降が切り捨てられます。
   * 
   * @param dataSize データの個数
   * @return フーリエ変換の結果
   */
  CM fft(int dataSize);

  /**
   * 行毎に基底が 2 の高速フーリエ変換を計算します。
   * 
   * <p>列の数は2のべき乗でなければならない。
   * 
   * @return フーリエ変換の結果
   */
  CM fftRowWise();

  /**
   * 行毎に<code>dataSize</code>点まで,基底が 2 の高速フーリエ変換を計算します。
   * 
   * <p><code>dataSize</code>は2のべき乗でなければならない。
   * 
   * @param dataSize データの個数
   * @return フーリエ変換の結果
   */
  CM fftRowWise(int dataSize);

  /**
   * 列毎に基底が 2 の高速フーリエ変換を計算します。
   * 
   * <p>行の数は2のべき乗でなければならない。
   * 
   * @return フーリエ変換の結果
   */
  CM fftColumnWise();

  /**
   * 列毎に<code>dataSize</code>点まで、基底が 2 の高速フーリエ変換を計算します。
   * 
   * <p><code>dataSize</code>は2のべき乗でなければならない。
   * 
   * @param dataSize データの個数
   * @return フーリエ変換の結果
   */
  CM fftColumnWise(int dataSize);
  
  /**
   * 2 のべき乗の長さのベクトルについて、基底が 2 の 逆高速フーリエ変換を計算します。
   * 
   * @return フーリエ変換の結果
   */
  CM ifft();

  /**
   * <code>dataSize</code>点まで基底が2の逆高速フーリエ変換をします。
   * 
   * <p>自身の長さが<code>dataSize</code>より短いとき、ゼロが後ろに付け加えられ、 自身 の長さが<code>dataSize</code>より長いとき、<code>dataSize</code> 番目以降が切り捨てられます。
   * 
   * @param dataSize データの個数
   * @return フーリエ変換の結果
   */
  CM ifft(int dataSize);

  /**
   * 行毎に基底が 2 の逆高速フーリエ変換を計算します。
   * 
   * <p>列の数は2のべき乗でなければならない。
   * 
   * @return 逆フーリエ変換の結果
   */
  CM ifftRowWise();

  /**
   * 行毎に<code>dataSize</code>点まで,基底が 2 の逆高速フーリエ変換を計算します。
   * 
   * <p><code>dataSize</code>は2のべき乗でなければならない。
   * 
   * @param dataSize 変換するデータの数
   * @return 逆フーリエ変換の結果
   */
  CM ifftRowWise(int dataSize);

  /**
   * 列毎に基底が 2 の逆高速フーリエ変換を計算します。
   * 
   * <p>行の数は2のべき乗でなければならない。
   * 
   * @return 逆フーリエ変換の結果
   */
  CM ifftColumnWise();

  /**
   * 列毎に<code>dataSize</code>点まで、基底が 2 の逆高速フーリエ変換を計算します。
   * 
   * <p><code>dataSize</code>は2のべき乗でなければならない。
   * 
   * @param dataSize データの個数
   * @return 逆フーリエ変換の結果
   */
  CM ifftColumnWise(int dataSize);
  
  /**
   * QZ分解を返します。
   * 
   * <p>行列Aと行列Bをブロック上三角行列 AA, BB、左変換のための直交行列(ユニタリー行列)Q、右変換のための直交行列(ユニタリー行列)Zの積に分解します。 これらの行列の間には、
   * 
   * <blockquote> A = Q * AA * Z<sup>#</sup> </blockquote>
   * 
   * <blockquote> B = Q * BB * Z<sup>#</sup> </blockquote>
   * 
   * <blockquote> Q<sup>#</sup> * Q = I </blockquote>
   * 
   * <blockquote> Z<sup>#</sup> * Z = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param B QZ分解をする対となる行列
   * 
   * @return QZ分解
   */
  RealQZDecomposition<RS,RM,CS,CM> qzDecompose(RM B);

  /**
   * 平方根行列を返します。
   * 
   * @return 平方根行列
   */
  CM sqrt();
  
  /**
   * 複素行列に変換します。
   * 
   * @return 複素行列
   */
  CM toComplex();
  
  /**
   * Adds complex value.
   * 
   * @param value value
   * @return result
   */
  CM add(CM value);
  
  /**
   * Subtracts complex value.
   * 
   * @param value value
   * @return result
   */
  CM subtract(CM value);

  /**
   * Multiplies complex value.
   * 
   * @param value value
   * @return result
   */
  CM multiply(CM value);

  /**
   * Divides complex value.
   * 
   * @param value value
   * @return result
   */
  CM divide(CM value);

  /**
   * Divides from left by complex value.
   * 
   * @param value value
   * @return result
   */
  CM leftDivide(CM value);
  
  /**
   * 下側に行列<code>value</code>を付けた行列を生成します。
   * 
   * @param value 付ける行列
   * @return 下側に<code>value</code>をつけた行列
   */
  CM appendDown(CM value);

  /**
   * 右側に<code>value</code>を付けた行列を生成します。
   * 
   * @param value 付ける複素数
   * @return 右側に<code>value</code>を付けた行列
   */
  CM appendRight(CM value);
  
  /**
   * Multiplies complex value.
   * 
   * @param value value
   * @return result
   */
  CM multiply(CS value);

  /**
   * Divides complex value.
   * 
   * @param value value
   * @return result
   */
  CM divide(CS value);

  /**
   * Divides from left by complex value.
   * 
   * @param value value
   * @return result
   */
  CM leftDivide(CS value);
  

}
