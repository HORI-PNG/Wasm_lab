/*
 * $Id: NumericalMatrixOperator.java,v 1.25 2008/04/13 15:06:52 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

import org.mklab.nfc.eig.BalancedDecomposition;
import org.mklab.nfc.eig.HessenbergDecomposition;
import org.mklab.nfc.eig.QRDecomposition;
import org.mklab.nfc.eig.SchurDecomposition;
import org.mklab.nfc.leq.LUDecomposition;
import org.mklab.nfc.scalar.NumericalScalar;
import org.mklab.nfc.svd.SingularValueDecomposition;


/**
 * 数値行列を表すインターフェースです。
 * 
 * @author koga
 * @version $Revision: 1.25 $
 * @param <M> 行列の型
 * @param <S> スカラーの型
 */
public interface NumericalMatrix<S extends NumericalScalar<S,M>, M extends NumericalMatrix<S,M>> extends BaseMatrixOperator<S,M> {

///**
//* 実部行列を返します。
//* 
//* @return 実部行列
//*/
//  NumericalMatrix<?,?> getRealPart();
//
///**
//* 虚部行列を返します。
//* 
//* @return 虚部行列
//*/
//  NumericalMatrix<?,?> getImaginaryPart();

//  /**
//   * 固有値を成分とする列ベクトルを返します。
//   * 
//   * <p>固有値は、実部の降順に並べられます。
//   * 
//   * @return 固有値を成分とする列ベクトル(実部について降順)
//   */
//  NumericalMatrix<?,?> eigenValue();

//  /**
//   * 固有値を対角成分とする対角行列D、固有値に対応する固有ベクトルを横方向に並べた行列Xを返します。
//   * 
//   * <p>これらの行列の間には、
//   * 
//   * <blockquote> A * X = X * D </blockquote>
//   * 
//   * の関係が成り立ちます。固有ベクトルはノルムが1.0となるよう正規化されます。 固有値は実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。
//   * 
//   * @return 第1成分D、第2成分Xとする{@link Matrix}の配列
//   */
//  EigenSolution<?,?> eigenDecompose();
//
//  /**
//   * 一般化固有値を対角成分とする対角行列 D と対応する一般化固有ベクトルを列とする行列 X を返します。
//   * 
//   * <p>これらの行列の間には、
//   * 
//   * <blockquote> A * X = X * D </blockquote>
//   * 
//   * の関係が成り立ちます。固有ベクトルはノルムが1.0となるよう正規化されます。 固有値は、実部の降順に並べられます。固有ベクトルは、固有値に対応して並べられます。
//   * 
//   * @param B 一般化固有分解を求める対となる行列
//   * @return 第1成分D,第2成分Xとする {@link Matrix}の配列
//   */
//  EigenSolution<?,?> eigenDecompose(M B);

  /**
   * QR分解を返します。
   * 
   * <p>行列Aを直交行列(ユニタリー行列)Qと上三角行列Rの積に分解します。これらの行列の間には、
   * 
   * <blockquote> A = Q * R </blockquote>
   * 
   * <blockquote> Q<sup>#</sup> * Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @return QR分解
   */
  QRDecomposition<S,M> qrDecompose();

  /**
   * 並べ替え付きQR分解を返します。
   * 
   * <p>行列Aを置換行列 P、直交行列(ユニタリー行列)Q、上三角行列Rの積に分解します。これらの行列の間には、
   * 
   * <blockquote> A * P = Q * R </blockquote>
   * 
   * <blockquote> Q<sup>#</sup> * Q = I </blockquote>
   * 
   * の関係が成り立ちます。上三角行列 R の対角成分は、減少する順に並べられます。
   * 
   * @return 並べ替え付きQR分解
   */
  QRDecomposition<S,M> qrDecomposeWithPermutation();

//  /**
//   * QZ分解を返します。
//   * 
//   * <p>行列Aと行列Bをブロック上三角行列 AA, BB、左変換のための直交行列(ユニタリー行列)Q、右変換のための直交行列(ユニタリー行列)Zの積に分解します。 これらの行列の間には、
//   * 
//   * <blockquote> A = Q * AA * Z<sup>#</sup> </blockquote>
//   * 
//   * <blockquote> B = Q * BB * Z<sup>#</sup> </blockquote>
//   * 
//   * <blockquote> Q<sup>#</sup> * Q = I </blockquote>
//   * 
//   * <blockquote> Z<sup>#</sup> * Z = I </blockquote>
//   * 
//   * の関係が成り立ちます。
//   * 
//   * @param B QZ分解をする対となる行列
//   * 
//   * @return QZ分解
//   */
//  QZDecomposition<S,M> qzDecompose(M B);

  /**
   * Schur分解を返します。
   * 
   * <p> 行列をAを直交行列(ユニタリ行列)U、Schur行列 Tの積に分解します。 Schur行列Tは対角成分に行列の実固有値を持っています。 これらの行列の間には、
   * 
   * <blockquote> A = U * T * U<sup>#</sup> </blockquote>
   * 
   * <blockquote> U<sup>#</sup> * U = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @return Schur分解
   */
  SchurDecomposition<S,M> schurDecompose();

  /**
   * 対称行列のコレスキー分解を返します。
   * 
   * <p>もし、<code>X</code>が正定なら、
   * 
   * <blockquote>R'*R = X</blockquote>
   * 
   * を満たす、上三角行列<code>R</code>を求めます。 このメソッドは<code>X</code>の対角と上三角部分のみを利用します。 下三角部分は、上三角部分の転置であると仮定されます。
   * 
   * @return コレスキー分解
   */
  M choleskyDecompose();
  
  /**
   * 対称行列のコレスキー分解を返します。
   * 
   * <p>もし、<code>X</code>が正定なら、
   * 
   * <blockquote>R'*R = X</blockquote>
   * 
   * を満たす、上三角行列<code>R</code>を求めます。 このメソッドは<code>X</code>の対角と上三角部分のみを利用します。 下三角部分は、上三角部分の転置であると仮定されます。
   * @param tolerance 許容誤差
   * 
   * @return コレスキー分解
   */
  M choleskyDecompose(double tolerance);

  /**
   * 対称行列のコレスキー分解を返します。
   * 
   * <p>もし、<code>X</code>が正定なら、
   * 
   * <blockquote>R'*R = X</blockquote>
   * 
   * を満たす、上三角行列<code>R</code>を求めます。 このメソッドは<code>X</code>の対角と上三角部分のみを利用します。 下三角部分は、上三角部分の転置であると仮定されます。
   * @param tolerance 許容誤差
   * 
   * @return コレスキー分解
   */
  M choleskyDecompose(S tolerance);

  /**
   * 非対称行列のバランス化分解を返します。
   * 
   * <p>対角成分が 2 のべき乗である対角行列 D と、 次式のバランス化された行列 B を求め、 {@link Matrix}の配列として返します。
   * 
   * <blockquote> B = D <sup>-1 * </sup> A * D </blockquote>
   * 
   * <blockquote> A = D * B * D <sup> -1 </sup> </blockquote>
   * 
   * 行列B は、行列 A より対称行列に近く、各行のノルムが対応する 列のノルムに近いです。
   * 
   * @return バランス化分解
   */
  BalancedDecomposition<S,M> balancedDecompose();

  /**
   * カーネル(零空間)を張るベクトルからなる行列を返します。
   * 
   * @return カーネル
   */
  M kernel();

  /**
   * カーネル(零空間)を張るベクトルからなる行列を返します。
   * 
   * <p><code>tolerance</code>より小さい特異値をゼロと見なします。
   * 
   * @param tolerance 許容誤差
   * @return カーネル
   */
  M kernel(double tolerance);

  /**
   * カーネル(零空間)を張るベクトルからなる行列を返します。
   * 
   * <p><code>tolerance</code>より小さい特異値をゼロと見なします。
   * 
   * @param tolerance 許容誤差
   * @return カーネル
   */
  M kernel(S tolerance);

  /**
   * ヘッセンベルク分解を返します。
   * 
   * <p>行列Aを直交行列(ユニタリ行列)Q、 ヘッセンベルグ行列 H の積に分解します。 これらの行列の間には、
   * 
   * <blockquote> A = Q * H * Q<sup>#</sup> </blockquote>
   * 
   * <blockquote> Q<sup>#</sup> * Q = I </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @return ヘッセンベルク分解
   */
  HessenbergDecomposition<S,M> hessenbergDecompose();

  /**
   * LU分解を返します。
   * 
   * <p>行列Aを下三角行を行置換した行列 L、上三角行列 U の積に分解します。 これらの行列の間には、
   * 
   * <blockquote> A = L * U </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return LU分解
   */
  LUDecomposition<S,M> luDecompose(double tolerance, boolean stopIfSingular);

  /**
   * LU分解を返します。
   * 
   * <p>行列Aを下三角行を行置換した行列 L、上三角行列 U の積に分解します。 これらの行列の間には、
   * 
   * <blockquote> A = L * U </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return LU分解
   */
  LUDecomposition<S,M> luDecompose(S tolerance, boolean stopIfSingular);

  /**
   * LU分解を返します。
   * 
   * <p>行列Aを下三角行を行置換した行列 L、上三角行列 U の積に分解します。 これらの行列の間には、
   * 
   * <blockquote> A = L * U </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return LU分解
   */
  LUDecomposition<S,M> luDecompose(boolean stopIfSingular);

  /**
   * 並べ替え付きLU分解を返します。
   * 
   * <p>行列 Aを置換行列 P、下三角行列 L、上三角行列 Uの積に分解します。 これらの行列の間には、
   * 
   * <blockquote> P * A = L * U </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return 並べ替え付きLU分解
   */
  LUDecomposition<S,M> luDecomposeWithPermutation(boolean stopIfSingular);

  /**
   * 並べ替え付きLU分解を返します。
   * 
   * <p>行列 Aを置換行列 P、下三角行列 L、上三角行列 Uの積に分解します。 これらの行列の間には、
   * 
   * <blockquote> P * A = L * U </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return 並べ替え付きLU分解
   */
  LUDecomposition<S,M> luDecomposeWithPermutation(double tolerance, boolean stopIfSingular);

  /**
   * 並べ替え付きLU分解を返します。
   * 
   * <p>行列 Aを置換行列 P、下三角行列 L、上三角行列 Uの積に分解します。 これらの行列の間には、
   * 
   * <blockquote> P * A = L * U </blockquote>
   * 
   * の関係が成り立ちます。
   * 
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return 並べ替え付きLU分解
   */
  LUDecomposition<S,M> luDecomposeWithPermutation(S tolerance, boolean stopIfSingular);

  /**
   * 擬似逆行列を返します。
   * 
   * <p><code>tolerance</code>より小さい A の特異値をゼロとみなし、行列 A のランクを決定し、 擬似逆行列を求めます。
   * 
   * @param tolerance 許容誤差
   * @return 擬似逆行列
   */
  M pseudoInverse(double tolerance);

  /**
   * 擬似逆行列を返します。
   * 
   * <p><code>tolerance</code>より小さい A の特異値をゼロとみなし、行列 A のランクを決定し、 擬似逆行列を求めます。
   * 
   * @param tolerance 許容誤差
   * @return 擬似逆行列
   */
  M pseudoInverse(S tolerance);

  /**
   * 擬似逆行列を返します。
   * 
   * @return 擬似逆行列
   */
  M pseudoInverse();

  /**
   * ランク(階数)(正の特異値の数)を返します。
   * 
   * @return ランク
   */
  int rank();

  /**
   * ランク(階数)(<code>tolerance</code>より大きい特異値の数)を返します。
   * 
   * @param tolerance 許容誤差
   * @return ランク
   */
  int rank(double tolerance);

  /**
   * ランク(階数)(<code>tolerance</code>より大きい特異値の数)を返します。
   * 
   * @param tolerance 許容誤差
   * @return ランク
   */
  int rank(S tolerance);

  /**
   * フルランクであるか判定します。
   * 
   * @return フルランクならばtrue、そうでなければfalse
   */
  boolean isFullRank();

  /**
   * フルランクであるか判定します。
   * 
   * @param tolerance 許容誤差
   * 
   * @return フルランクならばtrue、そうでなければfalse
   */
  boolean isFullRank(double tolerance);

  /**
   * フルランクであるか判定します。
   * 
   * @param tolerance 許容誤差
   * 
   * @return フルランクならばtrue、そうでなければfalse
   */
  boolean isFullRank(S tolerance);

  /**
   * 零行列であるか判定します。
   * 
   * @param tolerance 許容誤差
   * @return 零行列ならばtrue、そうでなければfalse
   */
  boolean isZero(S tolerance);

  /**
   * 単位行列であるか判定します。
   * 
   * @param tolerance 許容誤差
   * @return 単位行列ならばtrue、そうでなければfalse
   */
  boolean isUnit(S tolerance);

  /**
   * 行列<code>opponent</code>と全ての成分の値が等しい(差の絶対値が許容誤差以下である)か判定します。
   * 
   * @param opponent 比較対象の行列
   * @param tolerance 許容誤差
   * @return 全ての成分の値が等しければtrue、そうでなければfalse
   */
  boolean equals(M opponent, S tolerance);

  /**
   * 特異値分解で得られる各行列を返します。
   * 
   * <p>自身と同じ大きさで、負でない実数(特異値) を対角成分にもつ行列を Dとすると、
   * 
   * <blockquote> A = U * D * V<sup>#</sup> </blockquote>
   * 
   * を満たす直交行列(ユニタリ 行列) U と V を求めます。
   * 
   * @return 特異値分解
   */
  SingularValueDecomposition<S,M> singularValueDecompose();

  /**
   * 特異値を成分とする列ベクトルを返します。
   * 
   * @return 特異値を成分とする列ベクトル
   */
  M singularValue();

  /**
   * 最大特異値を返します。
   * 
   * @return 最大特異値
   */
  S maxSingularValue();

  /**
   * 最小特異値を返します。
   * 
   * @return 最小特異値
   */
  S minSingularValue();

  /**
   * 条件数(2-ノルム)を返します。
   * 
   * <p>行列の2-ノルムに関する条件数を求めます。条件数は、 最大特異値と最小特異値の比で与えられ、1 以上です。
   * 
   * @return 条件数
   */
  S conditionNumber();

//  /**
//   * 2 のべき乗の長さのベクトルについて、基底が 2 の 高速フーリエ変換を計算します。
//   * 
//   * @return 高速フーリエ変換結果
//   */
//  NumericalMatrix<?,?> fft();
//
//  /**
//   * <code>dataSize</code>点まで基底が 2 の高速フーリエ変換をします。
//   * 
//   * <p>自身 の長さが<code>dataSize</code>より短いとき、ゼロが後ろに付け加えられ、 自身 の長さが<code>dataSize</code>より長いとき、<code>dataSize</code> 番目以降が切り捨てられます。
//   * 
//   * @param dataSize データの個数
//   * @return フーリエ変換の結果
//   */
//  NumericalMatrix<?,?> fft(int dataSize);
//
//  /**
//   * 行毎に基底が 2 の高速フーリエ変換を計算します。
//   * 
//   * <p>列の数は2のべき乗でなければならない。
//   * 
//   * @return フーリエ変換の結果
//   */
//  NumericalMatrix<?,?> fftRowWise();
//
//  /**
//   * 行毎に<code>dataSize</code>点まで,基底が 2 の高速フーリエ変換を計算します。
//   * 
//   * <p><code>dataSize</code>は2のべき乗でなければならない。
//   * 
//   * @param dataSize データの個数
//   * @return フーリエ変換の結果
//   */
//  NumericalMatrix<?,?> fftRowWise(int dataSize);
//
//  /**
//   * 列毎に基底が 2 の高速フーリエ変換を計算します。
//   * 
//   * <p>行の数は2のべき乗でなければならない。
//   * 
//   * @return フーリエ変換の結果
//   */
//  NumericalMatrix<?,?> fftColumnWise();
//
//  /**
//   * 列毎に<code>dataSize</code>点まで、基底が 2 の高速フーリエ変換を計算します。
//   * 
//   * <p><code>dataSize</code>は2のべき乗でなければならない。
//   * 
//   * @param dataSize データの個数
//   * @return フーリエ変換の結果
//   */
//  NumericalMatrix<?,?> fftColumnWise(int dataSize);

//  /**
//   * 2 のべき乗の長さのベクトルについて、基底が 2 の 逆高速フーリエ変換を計算します。
//   * 
//   * @return フーリエ変換の結果
//   */
//  NumericalMatrix<?,?> ifft();
//
//  /**
//   * <code>dataSize</code>点まで基底が2の逆高速フーリエ変換をします。
//   * 
//   * <p>自身の長さが<code>dataSize</code>より短いとき、ゼロが後ろに付け加えられ、 自身 の長さが<code>dataSize</code>より長いとき、<code>dataSize</code> 番目以降が切り捨てられます。
//   * 
//   * @param dataSize データの個数
//   * @return フーリエ変換の結果
//   */
//  NumericalMatrix<?,?> ifft(int dataSize);
//
//  /**
//   * 行毎に基底が 2 の逆高速フーリエ変換を計算します。
//   * 
//   * <p>列の数は2のべき乗でなければならない。
//   * 
//   * @return 逆フーリエ変換の結果
//   */
//  NumericalMatrix<?,?> ifftRowWise();
//
//  /**
//   * 行毎に<code>dataSize</code>点まで,基底が 2 の逆高速フーリエ変換を計算します。
//   * 
//   * <p><code>dataSize</code>は2のべき乗でなければならない。
//   * 
//   * @param dataSize 変換するデータの数
//   * @return 逆フーリエ変換の結果
//   */
//  NumericalMatrix<?,?> ifftRowWise(int dataSize);
//
//  /**
//   * 列毎に基底が 2 の逆高速フーリエ変換を計算します。
//   * 
//   * <p>行の数は2のべき乗でなければならない。
//   * 
//   * @return 逆フーリエ変換の結果
//   */
//  NumericalMatrix<?,?> ifftColumnWise();
//
//  /**
//   * 列毎に<code>dataSize</code>点まで、基底が 2 の逆高速フーリエ変換を計算します。
//   * 
//   * <p><code>dataSize</code>は2のべき乗でなければならない。
//   * 
//   * @param dataSize データの個数
//   * @return 逆フーリエ変換の結果
//   */
//  NumericalMatrix<?,?> ifftColumnWise(int dataSize);

  /**
   * 対数行列を返します。
   * 
   * @return 対数行列
   */
  M log();

//  /**
//   * 平方根行列を返します。
//   * 
//   * @return 平方根行列
//   */
//  M sqrt();

  /**
   * 指数関数行列を返します。
   * 
   * <p>自身をAとするとき、このメソッドは、
   * 
   * <blockquote> I + A + A^2/(2!) + ... + A^n/(n!) + ... </blockquote>
   * 
   * を求めます。
   * 
   * @return 行列指数関数
   */
  M exp();

  /**
   * 指数関数行列を返します。
   * 
   * <p>自身をAとするとき、このメソッドは、
   * 
   * <blockquote> I + A + A^2/(2!) + ... + A^n/(n!) + ... </blockquote>
   * 
   * を求めます。
   * 
   * @param tolerance 許容誤差
   * @return 行列指数関数
   */
  M exp(double tolerance);

  /**
   * 指数関数行列を返します。
   * 
   * <p>自身をAとするとき、このメソッドは、
   * 
   * <blockquote> I + A + A^2/(2!) + ... + A^n/(n!) + ... </blockquote>
   * 
   * を求めます。
   * 
   * @param tolerance 許容誤差
   * @return 行列指数関数
   */
  M exp(S tolerance);

  /**
   * 成分毎の正弦関数の結果からなる行列を返します。
   * 
   * @return 成分毎正弦関数行列
   */
  M sinElementWise();

  /**
   * 成分毎の双曲線正弦関数の結果からなる行列を返します。
   * 
   * @return 成分毎双曲線正弦関数行列
   */
  M sinhElementWise();

  /**
   * 成分毎の逆正弦関数の結果からなる行列を返します。
   * 
   * @return 成分毎逆正弦関数行列
   */
  M asinElementWise();

  /**
   * 成分毎の逆双曲線正弦関数の結果からなる行列を返します。
   * 
   * @return 成分毎逆双曲線正弦関数行列
   */
  M asinhElementWise();

  /**
   * 成分毎の余弦関数の結果からなる行列を返します。
   * 
   * @return 成分毎余弦関数行列
   */
  M cosElementWise();

  /**
   * 成分毎の双曲線余弦関数の結果からなる行列を返します。
   * 
   * @return 成分毎双曲線余弦関数行列
   */
  M coshElementWise();

  /**
   * 成分毎の逆余弦関数の結果からなる行列を返します。
   * 
   * @return 成分毎逆余弦関数行列
   */
  M acosElementWise();

  /**
   * 成分毎の逆双曲線余弦関数の結果からなる行列を返します。
   * 
   * @return 成分毎逆双曲線余弦関数行列
   */
  M acoshElementWise();

  /**
   * 成分毎の正接関数の結果からなる行列を返します。
   * 
   * @return 成分毎正接関数行列
   */
  M tanElementWise();

  /**
   * 成分毎の双曲線正接関数の結果からなる行列を返します。
   * 
   * @return 成分毎双曲線正接関数行列
   */
  M tanhElementWise();

  /**
   * 成分毎の逆正接関数の結果からなる行列を返します。
   * 
   * @return 成分毎逆正接関数行列
   */
  M atanElementWise();

//  /**
//   * 各成分の逆正接(2)関数の結果を成分とする行列を生成します。
//   * 
//   * @param matrix 分母側の数を成分とする行列
//   * @return 成分毎逆正接(2)関数行列
//   */
//  M atan2ElementWise(final IntMatrix matrix);

  /**
   * 各成分の逆正接(2)関数の結果を成分とする行列を生成します。
   * 
   * @param matrix 分母側の数を成分とする行列
   * @return 成分毎逆正接(2)関数行列
   */
  M atan2ElementWise(final M matrix);

  /**
   * 各成分の逆正接(2)関数の結果を成分とする行列を生成します。
   * 
   * @param value 分母側の数
   * @return 成分毎逆正接(2)関数行列
   */
  M atan2ElementWise(final S value);

  /**
   * 成分毎の逆双曲線正接関数の結果からなる行列を返します。
   * 
   * @return 成分毎逆双曲線正接関数行列
   */
  M atanhElementWise();

  /**
   * 成分毎の指数関数の結果からなる行列を返します。
   * 
   * @return 成分毎指数関数行列
   */
  M expElementWise();

  /**
   * 成分毎の対数関数の結果からなる行列を返します。
   * 
   * @return 成分毎自然対数関数行列
   */
  M logElementWise();

  /**
   * 成分毎の常用対数関数の結果からなる行列を返します。
   * 
   * @return 成分毎常用対数関数行列
   */
  M log10ElementWise();

  /**
   * 成分毎の平方根の結果からなる行列を返します。
   * 
   * @return 成分毎平方根行列
   */
  M sqrtElementWise();

///**
//* 各成分の偏角を成分に持つ行列を返します。
//* 
//* @return 偏角行列
//*/
//  NumericalMatrix<?,?> argumentElementWise();

  /**
   * 各成分の符合(-1,0,1)を成分に持つ行列を返します。
   * 
   * @return 符合行列
   */
  M signumElementWise();

  /**
   * 各成分の絶対値を成分に持つ行列を返します。
   * 
   * @return 絶対値行列
   */
  M absElementWise();

  /**
   * 標準偏差を返します。
   * 
   * @return 標準偏差
   */
  S std();

  /**
   * 各成分行毎の標準偏差列ベクトルを返します。
   * 
   * @return 行毎標準偏差ベクトル
   */
  M stdRowWise();

  /**
   * 各成分列毎の標準偏差行ベクトルを返します。
   * 
   * @return 列毎行標準偏差ベクトル
   */
  M stdColumnWise();

  /**
   * 全ての成分を昇順に並び替えた行列と元の位置を示す指数({@link IntMatrix})を返します。
   * 
   * <p>複素行列のときは、絶対値でソートします。
   * 
   * @return ソートした結果元の位置を示す指数({@link IntMatrix})
   */
  IndexedMatrix<S,M> sort();

  /**
   * 行毎に昇順に並び替えた行列と元の位置を示す指数({@link IntMatrix})を返します。
   * 
   * <p>複素行列のときは、絶対値でソートします。
   * 
   * @return ソート結果と元の位置を示す指数
   */
  IndexedMatrix<S,M> sortRowWise();

  /**
   * 列毎に昇順に並び替えた行列と元の位置を示す指数({@link IntMatrix})を返します。
   * 
   * <p>自身が複素行列のときは、絶対値でソートします。
   * 
   * @return ソート結果と元の位置を示す指数
   */
  IndexedMatrix<S,M> sortColumnWise();

  /**
   * 最大成分を返します。
   * 
   * @return 最大成分
   */
  S max();

  /**
   * 行毎の最大値を成分とする列ベクトルを返します。
   * 
   * @return 行毎最大値ベクトル
   */
  M maxRowWise();

  /**
   * 列毎の最大値を成分とする行ベクトルを返します。
   * 
   * @return 列毎行最大値ベクトル
   */
  M maxColumnWise();

  /**
   * 最大成分とその指数を返します。
   * 
   * @return 最大成分とその指数
   */
  ElementHolder<S> maximum();

  /**
   * 行毎の最大成分とその指数を返します。
   * 
   * @return 行毎最大成分とその指数
   */
  IndexedMatrix<S,M> maximumRowWise();

  /**
   * 最小成分を返します。
   * 
   * @return 最小成分
   */
  S min();

  /**
   * 列毎の最大成分とその指数を返します。
   * 
   * @return 列毎最大成分とその指数
   */
  IndexedMatrix<S,M> maximumColumnWise();

  /**
   * 行毎の最小値を成分とする列ベクトルを返します。
   * 
   * @return 行毎最小値ベクトル
   */
  M minRowWise();

  /**
   * 列毎の最小値を成分とする行ベクトルを返します。
   * 
   * @return 列毎行最小値ベクトル
   */
  M minColumnWise();

  /**
   * 最小成分と指数を返します。
   * 
   * @return 最小成分とその指数
   */
  ElementHolder<S> minimum();

  /**
   * 行毎の最小成分とその指数を返します。
   * 
   * @return 行毎最小成分とその指数
   */
  IndexedMatrix<S,M> minimumRowWise();

  /**
   * 列毎の最小成分とその指数を返します。
   * 
   * @return 列毎最小成分とその指数
   */
  IndexedMatrix<S,M> minimumColumnWise();

//  /**
//   * <code>opponent</code>と成分毎に比較し、大きいほうを成分にもつ行列を返します。
//   * 
//   * 複素行列のときは、絶対値で比較されます。
//   * 
//   * @param opponent 比較する行列
//   * @return 最大値を成分とする行列
//   */
//  M maxElementWise(IntMatrix opponent);

  /**
   * <code>opponent</code>と成分毎に比較し、大きいほうを成分にもつ行列を返します。
   * 
   * 複素行列のときは、絶対値で比較されます。
   * 
   * @param opponent 比較する行列
   * @return 最大値を成分とする行列
   */
  M maxElementWise(M opponent);

//  /**
//   * <code>opponent</code>と成分毎に比較し、小さいほうを成分にもつ行列を返します。
//   * 
//   * 複素行列のときは、絶対値で比較されます。
//   * 
//   * @param opponent 比較する行列
//   * @return 最小値を成分とする行列
//   */
//  M minElementWise(IntMatrix opponent);

  /**
   * <code>opponent</code>と成分毎に比較し、小さいほうを成分にもつ行列を返します。
   * 
   * 複素行列のときは、絶対値で比較されます。
   * 
   * @param opponent 比較する行列
   * @return 最小値を成分とする行列
   */
  M minElementWise(M opponent);

  /**
   * 全ての成分の中間値を返します。
   * 
   * @return 全ての成分の中間値
   */
  S median();

  /**
   * 行列なら、列毎のメジアンを成分とする行ベクトルを返します。
   * 
   * @return 中間値(メジアン) (median vector)
   */
   M medianColumnWise();

  /**
   * 行列なら、行毎のメジアンを成分とする列ベクトルを返します。
   * 
   * @return 中間値(メジアン) (median vector)
   */
  M medianRowWise();

  /**
   * norm({@link NormType#FROBENIUS NormType.FROBENIUS})でフロベニウスノルム、 norm( {@link NormType#INFINITY NormType.INFINITY})で無限大ノルム、 norm( {@link NormType#ONE NormType.ONE})で1-ノルムを、 norm(
   * {@link NormType#TWO NormType.TWO})で最大特異値を返します。
   * 
   * @param normType ノルムの種類を指定
   * @return 指令された種類のノルム
   */
 S norm(NormType normType);

  /**
   * フロベニウスノルムを返します。
   * 
   * @return フロベニウスノルム
   */
  S frobNorm();

  /**
   * 行毎のフロベニウスノルムを成分とする列ベクトルを返します。
   * 
   * @return 行毎のフロベニウスノルムを成分とする列ベクトル
   */
  M frobNormRowWise();

  /**
   * 列毎のフロベニウスノルムを成分とする行ベクトルを返します。
   * 
   * @return 列毎のフロベニウスノルムを成分とする行ベクトル
   */
  M frobNormColumnWise();

  /**
   * 無限大ノルムを返します。
   * 
   * @return 無限大ノルム
   */
  S infNorm();

  /**
   * 成分毎に累乗するした値を成分とする行列を返します。
   * 
   * @param scalar 累乗の指数
   * @return 累乗の結果
   */
  M powerElementWise(double scalar);

  /**
   * 成分毎に累乗するした値を成分とする行列を返します。
   * 
   * @param scalar 累乗の指数(複素数)
   * @return 累乗の結果
   */
  M powerElementWise(S scalar);

  //  /**
  //   * 成分毎に累乗するした値を成分とする行列を返します。
  //   * 
  //   * @param matrix 累乗の指数を成分とする行列
  //   * @return 累乗の結果
  //   */
  //  NumericalMatrixOperator<E> powerElementWise(IntMatrix matrix);

  /**
   * 成分毎に累乗するした値を成分とする行列を返します。
   * 
   * @param matrix 累乗の指数を成分とする行列
   * @return 累乗の結果
   */
  M powerElementWise(M matrix);

  /**
   * 絶対値が小さい成分を0に丸めます。
   * 
   * @param tolerance 許容誤差
   * @return 丸められた結果
   */
  M roundToZeroElementWise(S tolerance);

  /**
   * 各成分の剰余を成分とする行列を生成します。
   * 
   * @param matrix 割る数を成分とする行列
   * @return 各成分の剰余を成分とする行列
   */
  M remainderElementWise(final M matrix);

  /**
   * 各成分の剰余を成分とする行列を生成します。
   * 
   * @param value 割る数
   * @return 各成分の剰余を成分とする行列
   */
  M remainderElementWise(final S value);

  /**
   * 各成分の符合付剰余を成分とする行列を生成します。
   * 
   * @param matrix 割る数を成分とする行列
   * @return 各成分の符合付剰余を成分とする行列
   */
  M modulusElementWise(final M matrix);

  /**
   * 各成分の符合付剰余を成分とする行列を生成します。
   * 
   * @param value 割る数
   * @return 各成分の符合付剰余を成分とする行列
   */
  M modulusElementWise(final S value);

  /**
   * 同サイズの0〜1の範囲の一様分布のランダムな成分を持つ行列を生成します。
   * 
   * @return 同サイズの0〜1の範囲の一様分布のランダムな成分を持つ行列
   */
  M createUniformRandom();

  /**
   * 0〜1の範囲の一様分布のランダムな成分を持つ<code>rowSize</code>*<code>columnSize</code>の行列を生成します 。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return ランダムな成分をもつ行列
   */
  M createUniformRandom(final int rowSize, final int columnSize);

  /**
   * 0〜1の範囲の一様分布のランダムな成分を持つ<code>rowSize</code>*<code>columnSize</code>の行列を生成します 。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param seed 乱数の種
   * @return ランダムな成分をもつ行列
   */
  M createUniformRandom(final int rowSize, final int columnSize, long seed);

  /**
   * 行列<code>block</code>の<code>rowNumber</code>*<code>columnNumber</code>倍の 0〜1の範囲の一様分布のランダムな成分を持つ <code>rowSize</code>*<code>columnSize</code>の行列を生成します。
   * 
   * @param rowNumber 行方向の倍数
   * @param columnNumber 列方向の倍数
   * @param block 基本となる行列
   * @return <code>block</code>の<code>rowNumber</code>* <code>columnNumber</code>倍の 0〜1の範囲の一様分布のランダムな成分を持つ<code>rowSize</code >*<code>columnSize</code>の行列
   */
  M createUniformRandom(final int rowNumber, final int columnNumber, final Grid block);

  /**
   * 同サイズの平均0、分散1の正規分布のランダムな成分を持つ行列を生成します。
   * 
   * @return 同サイズの平均0、分散1の正規分布のランダムな成分を持つ行列
   */
  M createNormalRandom();

  /**
   * 平均0、分散1の正規分布のランダムな成分を持つ<code>rowSize</code>*<code>columnSize</code> の行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @return 平均0、分散1の正規分布のランダムな成分を持つ<code>rowSize</code>*<code>columnSize</code>の行列
   */
  M createNormalRandom(final int rowSize, final int columnSize);

  /**
   * 平均0、分散1の正規分布のランダムな成分を持つ<code>rowSize</code>*<code>columnSize</code> の行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param seed 乱数の種
   * @return 平均0、分散1の正規分布のランダムな成分を持つ<code>rowSize</code>*<code>columnSize</code>の行列
   */
  M createNormalRandom(final int rowSize, final int columnSize, long seed);

  /**
   * 行列<code>block</code>の<code>rowNumber</code>*<code>columnNumber</code>倍の 平均0、 分散1の正規分布のランダムな成分を持つ<code>rowSize</code>*<code>columnSize</code>の行列を生成します。
   * 
   * @param rowNumber 行方向の倍数
   * @param columnNumber 列方向の倍数
   * @param block 基本となる行列
   * @return <code>block</code>の<code>rowNumber</code>* <code>columnNumber</code>倍の 平均0、分散1の正規分布のランダムな成分を持つ<code>rowSize</code >*<code>columnSize</code>の行列
   */
  M createNormalRandom(final int rowNumber, final int columnNumber, final Grid block);

  /**
   * 逆行列(<code>this</code> <sup>-1 </sup>)を返します。
   * 
   * @param tolerance 許容誤差
   * @param stopIfSingular trueならば、正則でない場合、処理を中止し、例外を投げます。
   * @return 逆行列
   */
  M inverse(S tolerance, boolean stopIfSingular);
}
