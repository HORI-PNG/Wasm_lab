/**
 * $Id: RationalPolynomial.java,v 1.123 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.scalar;

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
import java.io.Writer;
import java.nio.charset.Charset;

import org.mklab.nfc.matrix.DoubleComplexMatrix;
import org.mklab.nfc.matrix.DoubleComplexPolynomialMatrix;
import org.mklab.nfc.matrix.DoubleComplexRationalPolynomialMatrix;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.DoublePolynomialMatrix;
import org.mklab.nfc.matrix.DoubleRationalPolynomialMatrix;
import org.mklab.nfc.matx.MatxObject;
import org.mklab.nfc.matx.MxDataHead;
import org.mklab.nfc.util.EndianTransformer;


/**
 * 有理多項式を表現するクラスです。
 * 
 * @author koga
 * @version $Revision: 1.123 $, 2008/02/16
 */
public class DoubleRationalPolynomial extends AbstractRationalPolynomial<DoublePolynomial,DoublePolynomialMatrix,DoubleRationalPolynomial,DoubleRationalPolynomialMatrix,DoubleNumber,DoubleMatrix> implements RealRationalPolynomial<DoublePolynomial,DoublePolynomialMatrix,DoubleComplexPolynomial,DoubleComplexPolynomialMatrix,DoubleRationalPolynomial,DoubleRationalPolynomialMatrix,DoubleComplexRationalPolynomial,DoubleComplexRationalPolynomialMatrix,DoubleNumber,DoubleMatrix,DoubleComplexNumber,DoubleComplexMatrix>, MatxObject {

  /** シリアルバージョン。 */
  //private static final long serialVersionUID = 6537697917259508800L;

  /** 分子多項式。 */
  //private DoublePolynomial numerator;

  /** 分母多項式。 */
  //private DoublePolynomial denominator;

  /** 出力の幅。 */
  //private int displayWidth = 1000;

  /** */
  private static final long serialVersionUID = -7490548819241413862L;

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子定数(実数)
   * @param denominator 分母多項式
   */
  public DoubleRationalPolynomial(final double numerator, final DoublePolynomial denominator) {
    super(numerator,denominator);
  }

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子定数(実数)
   * @param denominator 分母多項式
   */
  public DoubleRationalPolynomial(final DoubleNumber numerator, final DoublePolynomial denominator) {
    super(numerator,denominator);
  }

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子多項式
   * @param denominator 分母多項式
   */
  public DoubleRationalPolynomial(final DoublePolynomial numerator, final DoublePolynomial denominator) {
    super(numerator,denominator);
  }

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子多項式
   * @param denominator 分母定数(実数)
   */
  public DoubleRationalPolynomial(final DoublePolynomial numerator, final double denominator) {
    super(numerator,denominator);
  }

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子多項式
   * @param denominator 分母定数(実数)
   */
  public DoubleRationalPolynomial(final DoublePolynomial numerator, final DoubleNumber denominator) {
    this(numerator, new DoublePolynomial(denominator.doubleValue(), numerator.getVariable()));
  }

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子多項式
   */
  public DoubleRationalPolynomial(final DoublePolynomial numerator) {
    super(numerator);
  }

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子実数
   */
  public DoubleRationalPolynomial(final int numerator) {
    this(new DoublePolynomial(numerator));
  }

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子実数
   * @param variableName 多項式変数
   */
  public DoubleRationalPolynomial(final int numerator, String variableName) {
    super(new DoublePolynomial(numerator, variableName),new DoublePolynomial(1, variableName)); 
  }

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子実数
   */
  public DoubleRationalPolynomial(final double numerator) {
    this(new DoublePolynomial(numerator));
  }

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子実数
   */
  public DoubleRationalPolynomial(final  DoubleNumber numerator) {
    this(new DoublePolynomial(numerator.doubleValue()));
  }

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子実数
   * @param variableName 多項式変数
   * 
   */
  public DoubleRationalPolynomial(final double numerator, final String variableName) {
    super(new DoublePolynomial(numerator, variableName),new DoublePolynomial(1, variableName));
  }

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子実数
   * @param variableName 多項式変数
   * 
   */
  public DoubleRationalPolynomial(final DoubleNumber numerator, final String variableName) {
    super(new DoublePolynomial(numerator.doubleValue(), variableName),new DoublePolynomial(1, variableName));
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleRationalPolynomial create(final int value) {
    return new DoubleRationalPolynomial(value);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleRationalPolynomial create(final double value) {
    return new DoubleRationalPolynomial(value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DoubleRationalPolynomial clone() {
    final DoubleRationalPolynomial ans = super.clone();
    return ans;
  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public String toString() {
//    final String[] str = toString(false);
//    return "(" + str[0] + ") / (" + str[1] + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final String toString(final String valueFormat) {
//    final String[] str = toString(false, valueFormat);
//    return "(" + str[0] + ") / (" + str[1] + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public boolean equals(final Object opponent) {
//    if (this == opponent) {
//      return true;
//    }
//    if (opponent == null) {
//      return false;
//    }
//    if (opponent.getClass() != getClass()) {
//      return false;
//    }
//
//    return equals((DoubleRationalPolynomial)opponent, 0);
//  }

//  /**
//   * Override hashCode.
//   * 
//   * @return the Objects hash code.
//   */
//  @Override
//  public int hashCode() {
//    int hashCode = 1;
//    final int prime = 31;
//    hashCode = prime * hashCode + (getNumerator() == null ? 0 : getNumerator().hashCode());
//    hashCode = prime * hashCode + (getDenominator() == null ? 0 : getDenominator().hashCode());
//    return hashCode;
//  }

//  /**
//   * 許容誤差内で有理多項式<code>opponent</code>と等しいか判定します。
//   * 
//   * @param opponent 比較する有理多項式
//   * @param tolerance 許容誤差
//   * @return 等しければtrue、そうでなければfalse
//   */
//  public final boolean equals(final DoubleRationalPolynomial opponent, final double tolerance) {
//    final DoubleNumber denLeadingCoef1 = getDenominator().getCoefficient(getDenominator().getDegree());
//    final DoubleNumber denLeadingCoef2 = opponent.getDenominator().getCoefficient(opponent.getDenominator().getDegree());
//
//    if (denLeadingCoef1.equals(denLeadingCoef2)) {
//      final boolean isNumEqual = getNumerator().equals(opponent.getNumerator(), tolerance);
//      final boolean isDenEqual = getDenominator().equals(opponent.getDenominator(), tolerance);
//      return isNumEqual && isDenEqual;
//    }
//
//    final DoubleNumber scale1;
//    final DoubleNumber scale2;
//
//    if (denLeadingCoef1.isZero() == false) {
//      scale1 = denLeadingCoef1;
//    } else {
//      scale1 = getNumerator().getCoefficient(getNumerator().getDegree());
//    }
//    if (denLeadingCoef2.isZero() == false) {
//      scale2 = denLeadingCoef2;
//    } else {
//      scale2 = opponent.getNumerator().getCoefficient(opponent.getNumerator().getDegree());
//    }
//
//    final boolean isNumEqual = getNumerator().divide(scale1).equals(opponent.getNumerator().divide(scale2), tolerance);
//    final boolean isDenEqual = getDenominator().divide(scale1).equals(opponent.getDenominator().divide(scale2), tolerance);
//    return isNumEqual && isDenEqual;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final boolean equals(final SymbolicScalar<?,?,?,?> opponent, final DoubleNumber  tolerance) {
//    if (getClass() == opponent.getClass()) {
//      return equals((DoubleRationalPolynomial)opponent, tolerance);
//    }
//
//    if (isTransformableFrom(opponent)) {
//      return equals(transformFrom(opponent), tolerance);
//    }
//
//    if (opponent.isTransformableFrom(this)) {
//      return ((RationalPolynomial<DoubleNumber,?>)opponent.transformFrom(this)).equals(opponent, tolerance);
//    }
//
//    return false;
//  }

//  /**
//   * 許容範囲内で等しいか判定します。
//   * 
//   * @param opponent 比較する複素数成分
//   * @param tolerance 許容誤差
//   * @return 許容範囲内で等しければtrue、そうでなければfalse
//   */
//  public final boolean equals(final DoubleRationalPolynomial opponent, final DoubleNumber tolerance) {
//    final DoubleNumber denLeadingCoef1 = getDenominator().getCoefficient(getDenominator().getDegree());
//    final DoubleNumber denLeadingCoef2 = opponent.getDenominator().getCoefficient(opponent.getDenominator().getDegree());
//
//    if (denLeadingCoef1.equals(denLeadingCoef2)) {
//      final boolean isNumEqual = getNumerator().equals(opponent.getNumerator(), tolerance);
//      final boolean isDenEqual = getDenominator().equals(opponent.getDenominator(), tolerance);
//      return isNumEqual && isDenEqual;
//    }
//
//    final DoubleNumber scale1;
//    final DoubleNumber scale2;
//    if (denLeadingCoef1.isZero() == false) {
//      scale1 = denLeadingCoef1;
//    } else {
//      scale1 = getNumerator().getCoefficient(getNumerator().getDegree());
//    }
//
//    if (denLeadingCoef2.isZero() == false) {
//      scale2 = denLeadingCoef2;
//    } else {
//      scale2 = opponent.getNumerator().getCoefficient(opponent.getNumerator().getDegree());
//    }
//
//    final boolean isNumEqual = getNumerator().divide(scale1).equals(opponent.getNumerator().divide(scale2), tolerance);
//    final boolean isDenEqual = getDenominator().divide(scale1).equals(opponent.getDenominator().divide(scale2), tolerance);
//    return isNumEqual && isDenEqual;
//  }

//  /**
//   * 分子多項式の次数を返します。
//   * 
//   * @return 分子多項式の次数
//   */
//  public final int getNumeratorDegree() {
//    return getNumerator().getDegree();
//  }
//
//  /**
//   * 分母多項式の次数を返します。
//   * 
//   * @return 分母多項式の次数
//   */
//  public final int getDenominatorDegree() {
//    return getDenominator().getDegree();
//  }
//
//  /**
//   * 分子多項式を返します。
//   * 
//   * @return 分子多項式
//   */
//  public final DoublePolynomial getNumerator() {
//    return getNumerator().clone();
//  }
//
//  /**
//   * 分母多項式を返します。
//   * 
//   * @return 分母多項式
//   */
//  public final DoublePolynomial getDenominator() {
//    return getDenominator().clone();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final String getVariable() {
//    return getNumerator().getVariable();
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setVariable(final String variableName) {
//    getNumerator().setVariable(variableName);
//    getDenominator().setVariable(variableName);
//  }

//  /**
//   * 有理多項式を加えた有理多項式を生成します。
//   * 
//   * @param value 加える有理多項式
//   * @return 足し算の結果
//   */
//  public final DoubleRationalPolynomial add(final DoubleRationalPolynomial value) {
//    if (this.isZero() && value.isZero()) {
//      return new DoubleRationalPolynomial(0.0);
//    }
//    if (this.isZero()) {
//      return value.clone();
//    }
//    if (value.isZero()) {
//      return this.clone();
//    }
//
//    if (getDenominator().equals(value.getDenominator())) { // 分母が一致する場合。
//      return new DoubleRationalPolynomial(getNumerator().add(value.getNumerator()), getDenominator());
//    }
//
//    final DoublePolynomial tmp1 = getNumerator().multiply(value.getDenominator());
//    final DoublePolynomial tmp2 = value.getNumerator().multiply(getDenominator());
//    final DoublePolynomial pn = tmp1.add(tmp2);
//    final DoublePolynomial pd = getDenominator().multiply(value.getDenominator());
//    return new DoubleRationalPolynomial(pn, pd);
//  }

//  /**
//   * 多項式を加えた有理多項式を生成します。
//   * 
//   * @param value 加える多項式
//   * @return 足し算の結果
//   */
//  public final DoubleRationalPolynomial add(final DoublePolynomial value) {
//    if (isZero() && value.isZero()) {
//      return new DoubleRationalPolynomial(0.0);
//    }
//    if (isZero()) {
//      return new DoubleRationalPolynomial(value);
//    }
//    if (value.isZero()) {
//      return this.clone();
//    }
//
//    final DoublePolynomial tmp = value.multiply(getDenominator());
//    final DoublePolynomial pn = getNumerator().add(tmp);
//    return new DoubleRationalPolynomial(pn, getDenominator());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial add(final double value) {
//    if (isZero() && value == 0.0) {
//      return new DoubleRationalPolynomial(0.0);
//    }
//    if (isZero()) {
//      return new DoubleRationalPolynomial(value);
//    }
//    if (value == 0.0) {
//      return this.clone();
//    }
//
//    final DoublePolynomial tmp = getDenominator().multiply(value);
//    final DoublePolynomial pn = getNumerator().add(tmp);
//    return new DoubleRationalPolynomial(pn, getDenominator());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial add(final int value) {
//    if (isZero() && value == 0) {
//      return new DoubleRationalPolynomial(0);
//    }
//    if (isZero()) {
//      return new DoubleRationalPolynomial(value);
//    }
//    if (value == 0) {
//      return this.clone();
//    }
//
//    final DoublePolynomial tmp = getDenominator().multiply(value);
//    final DoublePolynomial pn = getNumerator().add(tmp);
//    return new DoubleRationalPolynomial(pn, getDenominator());
//  }

//  /**
//   * 有理多項式を引いた有理多項式を生成します。
//   * 
//   * @param value 引く有理多項式
//   * @return 引き算の結果
//   */
//  public final DoubleRationalPolynomial subtract(final DoubleRationalPolynomial value) {
//    if (this.isZero() && value.isZero()) {
//      return new DoubleRationalPolynomial(0.0);
//    }
//    if (this.isZero()) {
//      return value.unaryMinus();
//    }
//    if (value.isZero()) {
//      return this.clone();
//    }
//
//    if (getDenominator().equals(value.getDenominator())) { // 分母が一致する場合。
//      return new DoubleRationalPolynomial(getNumerator().subtract(value.getNumerator()), getDenominator());
//    }
//
//    final DoublePolynomial tmp1 = getNumerator().multiply(value.getDenominator());
//    final DoublePolynomial tmp2 = value.getNumerator().multiply(getDenominator());
//    final DoublePolynomial pn = tmp1.subtract(tmp2);
//    final DoublePolynomial pd = getDenominator().multiply(value.getDenominator());
//    return new DoubleRationalPolynomial(pn, pd);
//  }

//  /**
//   * 多項式を引いた有理多項式を生成します。
//   * 
//   * @param value 引く多項式
//   * @return 引き算の結果
//   */
//  public final DoubleRationalPolynomial subtract(final DoublePolynomial value) {
//    if (isZero() && value.isZero()) {
//      return new DoubleRationalPolynomial(0.0);
//    }
//    if (isZero()) {
//      return new DoubleRationalPolynomial(value.unaryMinus());
//    }
//    if (value.isZero()) {
//      return this.clone();
//    }
//
//    final DoublePolynomial newNumerator = getNumerator().subtract(value.multiply(getDenominator()));
//    return new DoubleRationalPolynomial(newNumerator, getDenominator());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial subtract(final double value) {
//    if (isZero() && value == 0.0) {
//      return new DoubleRationalPolynomial(0.0);
//    }
//    if (isZero()) {
//      return new DoubleRationalPolynomial(-value);
//    }
//    if (value == 0.0) {
//      return this.clone();
//    }
//
//    final DoublePolynomial newNumerator = getNumerator().subtract(getDenominator().multiply(value));
//    return new DoubleRationalPolynomial(newNumerator, getDenominator());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial subtract(final int value) {
//    if (isZero() && value == 0) {
//      return new DoubleRationalPolynomial(0);
//    }
//    if (isZero()) {
//      return new DoubleRationalPolynomial(-value);
//    }
//    if (value == 0) {
//      return this.clone();
//    }
//
//    final DoublePolynomial newNumerator = getNumerator().subtract(getDenominator().multiply(value));
//    return new DoubleRationalPolynomial(newNumerator, getDenominator());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial inverse() {
//    return new DoubleRationalPolynomial(getDenominator().clone(), getNumerator().clone());
//  }

//  /**
//   * 有理多項式を乗じた有理多項式を生成します。
//   * 
//   * @param value 乗じる有理多項式
//   * @return 掛け算の結果
//   */
//  public final DoubleRationalPolynomial multiply(final DoubleRationalPolynomial value) {
//    if (isZero() || value.isZero()) {
//      return new DoubleRationalPolynomial(0.0);
//    }
//
//    final DoublePolynomial ansNumerator = getNumerator().multiply(value.getNumerator());
//    final DoublePolynomial ansDenominator = getDenominator().multiply(value.getDenominator());
//    return new DoubleRationalPolynomial(ansNumerator, ansDenominator);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial multiply(final double value) {
//    return new DoubleRationalPolynomial(getNumerator().multiply(value), getDenominator().clone());
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial multiply(final int value) {
//    return new DoubleRationalPolynomial(getNumerator().multiply(value), getDenominator().clone());
//  }

//  /**
//   * 多項式を乗じた有理多項式を生成します。
//   * 
//   * @param value 乗じる多項式
//   * @return 掛け算の結果
//   */
//  public final DoubleRationalPolynomial multiply(final DoublePolynomial value) {
//    return new DoubleRationalPolynomial(getNumerator().multiply(value), getDenominator().clone());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial divide(final double value) {
//    return new DoubleRationalPolynomial(getNumerator().divide(value), getDenominator().clone());
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial divide(final int value) {
//    return new DoubleRationalPolynomial(getNumerator().divide(value), getDenominator().clone());
//  }
//
//  /**
//   * 多項式で割った有理多項式を生成します。
//   * 
//   * @param value 割る多項式
//   * @return 割り算の結果
//   */
//  public final DoubleRationalPolynomial divide(final DoublePolynomial value) {
//    return new DoubleRationalPolynomial(getNumerator().clone(), getDenominator().multiply(value));
//  }

//  /**
//   * 有理多項式で割った有理多項式を生成します。
//   * 
//   * @param value 割る有理多項式
//   * @return 割り算の結果
//   */
//  public final DoubleRationalPolynomial divide(final DoubleRationalPolynomial value) {
//    if (isZero()) {
//      return new DoubleRationalPolynomial(0);
//    }
//
//    final DoublePolynomial ansNumerator = getNumerator().multiply(value.getDenominator());
//    final DoublePolynomial ansDenominator = getDenominator().multiply(value.getNumerator());
//    return new DoubleRationalPolynomial(ansNumerator, ansDenominator);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial leftDivide(final double value) {
//    return new DoubleRationalPolynomial(getDenominator().multiply(value), getNumerator().clone());
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial leftDivide(final int value) {
//    return new DoubleRationalPolynomial(getDenominator().multiply(value), getNumerator().clone());
//  }
//
//  /**
//   * 多項式を割った有理多項式を生成します。
//   * 
//   * @param value 割られる多項式
//   * @return 割り算の結果
//   */
//  public final DoubleRationalPolynomial leftDivide(final DoublePolynomial value) {
//    return new DoubleRationalPolynomial(getDenominator().multiply(value), getNumerator().clone());
//  }
//
//  /**
//   * 有理多項式を割った有理多項式を生成します。
//   * 
//   * @param value 割られる有理多項式
//   * @return 割り算の結果
//   */
//  public final DoubleRationalPolynomial leftDivide(final DoubleRationalPolynomial value) {
//    if (value.isZero()) {
//      return new DoubleRationalPolynomial(0.0);
//    }
//
//    final DoublePolynomial ansNumerator = getDenominator().multiply(value.getNumerator());
//    final DoublePolynomial ansDenominator = getNumerator().multiply(value.getDenominator());
//    return new DoubleRationalPolynomial(ansNumerator, ansDenominator);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial power(final int m) {
//    if (m < 0) {
//      return inverse().power(-m);
//      //throw new IllegalArgumentException(Messages.getString("RationalPolynomial.6")); //$NON-NLS-1$
//    }
//    if (m == 0) {
//      return new DoubleRationalPolynomial(1);
//    }
//    if (m == 1) {
//      return this.clone();
//    }
//
//    int n = m;
//    DoubleRationalPolynomial aa = this.clone();
//    DoubleRationalPolynomial b2 = new DoubleRationalPolynomial(1);
//    DoubleRationalPolynomial b;
//
//    for (;;) {
//      if (n % 2 != 0) {
//        b = b2.multiply(aa);
//        n /= 2;
//        if (n != 0) {
//          b2 = b;
//          aa = aa.multiply(aa);
//        } else {
//          break;
//        }
//      } else {
//        n /= 2;
//        aa = aa.multiply(aa);
//      }
//    }
//
//    return b;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial unaryMinus() {
//    return new DoubleRationalPolynomial(getNumerator().unaryMinus(), getDenominator().clone());
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial conjugate() {
//    return new DoubleRationalPolynomial(getNumerator().conjugate(), getDenominator().conjugate());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial getRealPart() {
//    if (isReal()) {
//      clone();
//    }
//
//    final DoublePolynomial numr = getNumerator().getRealPart();
//    final DoublePolynomial numi = getNumerator().getImaginaryPart();
//    final DoublePolynomial denr = getDenominator().getRealPart();
//    final DoublePolynomial deni = getDenominator().getImaginaryPart();
//
//    if (deni.isZero()) {
//      numr.simplify();
//      denr.simplify();
//      return new DoubleRationalPolynomial(numr, denr);
//    }
//
//    final DoublePolynomial ansNumerator = numr.multiply(denr).add(numi.multiply(deni));
//    final DoublePolynomial ansDenominator = denr.multiply(denr).add(deni.multiply(deni));
//    return new DoubleRationalPolynomial(ansNumerator, ansDenominator);
//  }
//
//  /**
//   * 複素有理多項式の実部有理多項式を設定します。
//   * 
//   * @param realPart 実部有理多項式
//   */
//  public final void setRealPart(final DoubleRationalPolynomial realPart) {
//    throw new IllegalArgumentException(Messages.getString("RationalPolynomial.9")); //$NON-NLS-1$
//  }
//
//  /**
//   * 複素有理多項式の虚部有理多項式を設定します。
//   * 
//   * @param imagPart 虚部有理多項式
//   */
//  public final void setImaginaryPart(final DoubleRationalPolynomial imagPart) {
//    throw new IllegalArgumentException(Messages.getString("RationalPolynomial.9")); //$NON-NLS-1$
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial getImaginaryPart() {
//    return new DoubleRationalPolynomial(0);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final BaseComplexSymbolicScalar<DoubleRationalPolynomial> toComplex() {
//    return new BaseComplexSymbolicScalar<>(this.clone(), createZero());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial roundToZero(final double tolerance) {
//    return new DoubleRationalPolynomial(getNumerator().roundToZero(tolerance), getDenominator().roundToZero(tolerance));
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial roundToZero(final DoubleNumber tolerance) {
//    return new DoubleRationalPolynomial(getNumerator().roundToZero(tolerance), getDenominator().roundToZero(tolerance));
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial round() {
//    return new DoubleRationalPolynomial(getNumerator().round(), getDenominator().round());
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial ceil() {
//    return new DoubleRationalPolynomial(getNumerator().ceil(), getDenominator().ceil());
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial floor() {
//    return new DoubleRationalPolynomial(getNumerator().floor(), getDenominator().floor());
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial fix() {
//    return new DoubleRationalPolynomial(getNumerator().fix(), getDenominator().fix());
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleNumber evaluate(final int value) {
//    final DoubleNumber neval = getNumerator().evaluate(value);
//    final DoubleNumber deval = getDenominator().evaluate(value);
//    return neval.divide(deval);
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleNumber evaluate(final double value) {
//    final DoubleNumber neval = getNumerator().evaluate(value);
//    final DoubleNumber deval = getDenominator().evaluate(value);
//    return neval.divide(deval);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleNumber evaluate(final DoubleNumber value) {
//    return getNumerator().evaluate(value).divide(getDenominator().evaluate(value));
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final Scalar<?,?> evaluate(final Scalar<?,?> value) {
//    return getNumerator().evaluate(value).divide(getDenominator().evaluate(value));
//  }

//  /**
//   * 式変数に多項式を代入する評価します。
//   * 
//   * @param value 変数に代入する多項式
//   * @return 評価の結果
//   */
//  public final DoubleRationalPolynomial evaluate(final DoublePolynomial value) {
//    return new DoubleRationalPolynomial(getNumerator().evaluate(value), getDenominator().evaluate(value));
//  }
//
//  /**
//   * 式変数に有理多項式を代入する評価します。
//   * 
//   * @param value 代入する有理多項式
//   * @return 評価の結果
//   */
//  public final DoubleRationalPolynomial evaluate(final DoubleRationalPolynomial value) {
//    return getNumerator().evaluate(value).divide(getDenominator().evaluate(value));
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleMatrix evaluate(final DoubleMatrix value) {
//    final DoubleMatrix num = getNumerator().evaluate(value);
//    final DoubleMatrix den = getDenominator().evaluate(value);
//    return num.multiply(den.inverse());
//  }

//  /**
//   * 式変数に行列の各成分を代入した結果からなる行列を求めて返します。
//   * 
//   * @param value 代入する値を成分とする行列
//   * @return 評価の結果
//   */
//  public final DoubleMatrix evaluateElementWise(final DoubleMatrix value) {
//    final DoubleMatrix num = getNumerator().evaluateElementWise(value);
//    final DoubleMatrix den = getDenominator().evaluateElementWise(value);
//    return num.divideElementWise(den);
//  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix getZeros() {
    return getNumerator().getRoots();
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix getPoles() {
    return getDenominator().getRoots();
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial derivative(final int order) {
//    DoublePolynomial num = getNumerator();
//    final DoublePolynomial dden = getDenominator().derivative(1);
//
//    for (int i = 1; i <= order; i++) {
//      final DoublePolynomial tmp1 = num.derivative(1);
//      final DoublePolynomial tmp3 = tmp1.multiply(getDenominator());
//      final DoublePolynomial tmp2 = num.multiply(i);
//      final DoublePolynomial tmp4 = tmp2.multiply(dden);
//      num = tmp3.subtract(tmp4);
//    }
//
//    final DoublePolynomial den = getDenominator().power(order + 1);
//
//    return new DoubleRationalPolynomial(num, den);
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial shiftLower(final int count) {
//    final DoublePolynomial num = getNumerator().shiftLower(count);
//    DoublePolynomial den = getDenominator().shiftLower(count);
//
//    if (den.isZero()) {
//      den = new DoublePolynomial(1.0);
//    }
//    return new DoubleRationalPolynomial(num, den);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial shiftHigher(final int count) {
//    final DoublePolynomial num = getNumerator().shiftHigher(count);
//    final DoublePolynomial den = getDenominator().shiftHigher(count);
//    return new DoubleRationalPolynomial(num, den);
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final boolean isReal() {
//    return getNumerator().isReal() && getDenominator().isReal();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final boolean isComplex() {
//    return getNumerator().isComplex() || getDenominator().isComplex();
//  }

//  /**
//   * 分母多項式による分子多項式の除算の商多項式を返します。
//   * 
//   * @return 分母多項式による分子多項式の除算の商多項式
//   */
//  public final DoublePolynomial getQuotient() {
//    final int nDegree = getNumerator().getDegree();
//    final int dDegree = getDenominator().getDegree();
//
//    if (nDegree < dDegree) {
//      return new DoublePolynomial(0);
//    }
//
//    if (dDegree == 0) {
//      return getNumerator().divide(getDenominator().getCoefficient(0));
//    }
//
//    return getQuotientAndRemainder()[0];
//  }
//
//  /**
//   * 分母多項式による分子多項式の除算の剰余(余り)多項式を返します。
//   * 
//   * @return 分母多項式による分子多項式の除算の剰余(余り)多項式
//   */
//  public final DoublePolynomial getRemainder() {
//    final int nDegree = getNumerator().getDegree();
//    final int dDegree = getDenominator().getDegree();
//
//    if (nDegree < dDegree) {
//      return getNumerator().clone();
//    }
//
//    if (dDegree == 0) {
//      return new DoublePolynomial(0);
//    }
//
//    return getQuotientAndRemainder()[1];
//  }

//  /**
//   * 分母多項式による分子多項式の除算の商多項式と剰余(余り)多項式を返します。
//   * 
//   * @return 分母多項式による分子多項式の除算の商多項式と剰余(余り)多項式 ニューメリカルレシピ・イン・シー (p.154)
//   */
//  private DoublePolynomial[] getQuotientAndRemainder() {
//    final int nDegree = getNumerator().getDegree();
//    final int dDegree = getDenominator().getDegree();
//
//    final double[] r = new double[nDegree + 1];
//    final double[] q = new double[nDegree + 1];
//
//    for (int i = 0; i <= nDegree; i++) {
//      r[i] = getNumerator().getCoefficient(i).doubleValue();
//      q[i] = 0;
//    }
//
//    for (int i = nDegree - dDegree; i >= 0; i--) {
//      q[i] = r[dDegree + i] / getDenominator().getCoefficient(dDegree).doubleValue();
//      for (int j = dDegree + i - 1; j >= i; j--) {
//        r[j] -= q[i] * getDenominator().getCoefficient(j - i).doubleValue();
//      }
//    }
//
//    for (int i = dDegree; i <= nDegree; i++) {
//      r[i] = 0;
//    }
//
//    final DoublePolynomial qut = new DoublePolynomial(q);
//    final DoublePolynomial rem = new DoublePolynomial(r);
//    qut.simplify();
//    rem.simplify();
//    return new DoublePolynomial[] {qut, rem};
//  }

//  /**
//   * 標準出力に出力(表示)します。 <p> 変数名はansです。
//   */
//  public final void print() {
//    print("ans"); //$NON-NLS-1$
//  }
//
//  /**
//   * 標準出力に出力(表示)します。
//   * 
//   * @param name 名前
//   */
//  public final void print(final String name) {
//    try {
//      PrintStream output = new PrintStream(System.out, false, "UTF-8"); //$NON-NLS-1$
//      print(name, output);
//    } catch (UnsupportedEncodingException e) {
//      throw new IllegalArgumentException(name, e);
//    }
//  }

//  /**
//   * 表示文字列を返します。
//   * 
//   * @param name 名前
//   * @return 表示文字列
//   */
//  public final String getPrintingString(final String name) {
//    return getPrintingString(name, getFormat());
//  }
//
//  /**
//   * 表示文字列を返します。
//   * 
//   * @param name 名前
//   * @param coefficientFormat 出力フォーマット
//   * @return 表示文字列
//   */
//  public final String getPrintingString(final String name, final String coefficientFormat) {
//    final ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//    try (final PrintStream output = new PrintStream(stream, false, "UTF-8")) { //$NON-NLS-1$
//      print(name, output, coefficientFormat);
//    } catch (UnsupportedEncodingException e) {
//      throw new IllegalArgumentException(name, e);
//    }
//
//    try {
//      return stream.toString("UTF-8"); //$NON-NLS-1$
//    } catch (UnsupportedEncodingException e) {
//      throw new IllegalArgumentException(name, e);
//    }
//  }

//  /**
//   * 出力ストリームに出力します。
//   * 
//   * @param name 名前
//   * @param output 出力ストリーム
//   */
//  public final void print(final String name, final PrintStream output) {
//    print(name, output, getFormat());
//  }
//
//  /**
//   * 出力ストリームに出力します。
//   * 
//   * @param name 名前
//   * @param output 出力ストリーム
//   * @param coefficientFormat 出力フォーマット
//   */
//  public final void print(final String name, final PrintStream output, final String coefficientFormat) {
//    final int width = getDisplayWidth() - 1;
//    final String[] str = toString(false, coefficientFormat);
//    String strn = str[0];
//    String strd = str[1];
//    final int length1 = strn.length();
//    final int length2 = strd.length();
//    int length = (length1 > length2) ? length1 : length2;
//    final int offset = name.length() == 0 ? 1 : name.length() + 4;
//
//    final PolynomialTokenizer ptn = new PolynomialTokenizer(strn);
//    final PolynomialTokenizer ptd = new PolynomialTokenizer(strd);
//    strn = ptn.nextLine(width - offset);
//    strd = ptd.nextLine(width - offset);
//
//    if (offset + length + 1 > width) {
//      length = width - offset;// - 9;
//    }
//
//    final String marginSpace = getSpace(offset);
//    /* Numerator */
//    writeSpace(offset, output);
//    if (length1 < length) {
//      writeSpace((length - length1) / 2, output);
//    }
//
//    output.println(strn);
//    while (ptn.hasMoreTokens()) {
//      output.println(marginSpace + ptn.nextLine(width - offset));
//    }
//
//    /* Line */
//    if (0 < name.length()) {
//      output.print(name + " = "); //$NON-NLS-1$
//    }
//    writeLine(length + 2, output);
//
//    /* Denominator */
//    output.println(""); //$NON-NLS-1$
//    writeSpace(offset, output);
//    if (length2 < length) {
//      writeSpace((length - length2) / 2, output);
//    }
//    output.println(strd);
//    while (ptd.hasMoreTokens()) {
//      output.println(marginSpace + ptd.nextLine(width - offset));
//    }
//  }

  /**
   * {@inheritDoc}
   */
  public final String toMmString() {
    return toMmString("%G"); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final String toMmString(final String coefficientFormat) {
    final String numeratorString = getNumerator().toMmString(coefficientFormat);
    final String denominatorString = getDenominator().toMmString(coefficientFormat);
    return "(" + numeratorString + ") / (" + denominatorString + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

//  /**
//   * 文字列に変換します。
//   * 
//   * @param saving ファイルに保存するならばtrue、そうでなければfalse
//   * @return 生成された文字列
//   */
//  public final String[] toString(final boolean saving) {
//    return new String[] {getNumerator().toString(saving), getDenominator().toString(saving)};
//  }
//
//  /**
//   * 文字列に変換します。
//   * 
//   * @param saving ファイルに保存するならばtrue、そうでなければfalse
//   * @param coefficientFormat 係数の出力フォーマット
//   * @return 生成された文字列
//   */
//  public final String[] toString(final boolean saving, final String coefficientFormat) {
//    return new String[] {getNumerator().toString(saving, coefficientFormat), getDenominator().toString(saving, coefficientFormat)};
//  }

//  /**
//   * 空白を標準出力に出力(表示)します。
//   * 
//   * @param count 空白の数
//   * @param output プリントストリーム
//   */
//  private void writeSpace(final int count, final PrintStream output) {
//    final char[] space = new char[count];
//    int i = count;
//    while (i-- != 0) {
//      space[i] = ' ';
//    }
//    output.print(space);
//  }
//
//  /**
//   * 指定された個数の空白文字を含む文字列を生成します。
//   * 
//   * @param count 空白文字の数
//   * @return 生成された文字列
//   */
//  private String getSpace(final int count) {
//    final char[] space = new char[count];
//    int i = count;
//    while (i-- != 0) {
//      space[i] = ' ';
//    }
//    return new String(space);
//  }

//  /**
//   * 分子多項式と分母多項式を分ける線を描くためのマイナス記号を標準出力に出力(表示)します。
//   * 
//   * @param count マイナス記号の数
//   * @param output プリントストリーム
//   */
//  private void writeLine(final int count, final PrintStream output) {
//    final char[] space = new char[count];
//    for (int i = 0; i < count; i++) {
//      space[i] = '-';
//    }
//    output.print(space);
//  }

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

    final String var = getNumerator().getVariable();
    final int varlen;
    final byte[] b;
    if (var != null) {
      varlen = var.length() + 1;
      b = (var + "\0").getBytes(Charset.forName("UTF-8")); //$NON-NLS-1$ //$NON-NLS-2$
    } else {
      varlen = 0;
      b = new byte[0];
    }

    output.writeInt(varlen);
    output.write(b, 0, b.length);

    final DoubleMatrix ndata = getNumerator().getCoefficients();
    final DoubleMatrix ddata = getDenominator().getCoefficients();
    for (int i = 0; i < ndata.getColumnSize(); i++) {
      output.writeDouble(ndata.getDoubleElement(i + 1));
    }
    for (int i = 0; i < ddata.getColumnSize(); i++) {
      output.writeDouble(ddata.getDoubleElement(i + 1));
    }

    output.flush();
  }

  /**
   * MX形式のデータをファイルから入力します。
   * 
   * @param file ファイル
   * @return 読み込んだ有理多項式
   * @throws IOException ファイルから入力できない場合
   */
  public static DoubleRationalPolynomial readMxFormat(final File file) throws IOException {
    try (final DataInputStream ds = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
      final DoubleRationalPolynomial ans = readMxFormat(ds);
      return ans;
    }
  }

  /**
   * MX形式のデータを入力ストリームから入力します。
   * 
   * @param input 入力ストリーム
   * @return 入力した有理多項式
   * @throws IOException 入力ストリームから入力できない場合
   */
  public static DoubleRationalPolynomial readMxFormat(final InputStream input) throws IOException {
    final MxDataHead head = new MxDataHead();
    head.read(input);
    return readMxFormat(head, input);
  }

  /**
   * MX形式のデータを入力ストリームから入力します。 ヘッダ情報は先に指定している。
   * 
   * @param input 入力ストリーム
   * @param head ヘッダ情報
   * @return 入力した有理多項式
   * @throws IOException 入力ストリームから入力できない場合
   */
  public static DoubleRationalPolynomial readMxFormat(final MxDataHead head, final InputStream input) throws IOException {
    final DataInputStream is = new DataInputStream(input);

    final int varlen;
    if (head.isSameEndian()) {
      varlen = is.readInt();
    } else {
      varlen = EndianTransformer.flip(is.readInt());
    }

    final String var;
    if (varlen == 0) {
      var = null;
    } else {
      byte[] b = new byte[varlen];
      new DataInputStream(is).readFully(b);
      var = new String(b, 0, varlen - 1, Charset.forName("UTF-8")); //$NON-NLS-1$
    }

    final int realImag = head.getRealOrComplex();
    final int ndeg = head.getNumeratorDegree();
    final int ddeg = head.getDenominatorDegree();

    if (realImag == 0) {
      final double[] ndata = new double[ndeg + 1];
      final double[] ddata = new double[ddeg + 1];
      if (head.isSameEndian()) {
        for (int i = 0; i <= ndeg; i++) {
          ndata[i] = is.readDouble();
        }
        for (int i = 0; i <= ddeg; i++) {
          ddata[i] = is.readDouble();
        }
      } else {
        for (int i = 0; i <= ndeg; i++) {
          long d = EndianTransformer.flip(is.readLong());
          ndata[i] = Double.longBitsToDouble(d);
        }
        for (int i = 0; i <= ddeg; i++) {
          long d = EndianTransformer.flip(is.readLong());
          ddata[i] = Double.longBitsToDouble(d);
        }
      }

      return new DoubleRationalPolynomial(new DoublePolynomial(ndata, var), new DoublePolynomial(ddata, var));
    }

    throw new UnsupportedOperationException();

    //    final DoubleComplexNumber[] ndata = new DoubleComplexNumber[ndeg + 1];
    //    final DoubleComplexNumber[] ddata = new DoubleComplexNumber[ddeg + 1];
    //
    //    if (head.isSameEndian()) {
    //      for (int i = 0; i <= ndeg; i++) {
    //        final double real = is.readDouble();
    //        final double imag = is.readDouble();
    //        ndata[i] = new DoubleComplexNumber(real, imag);
    //      }
    //      for (int i = 0; i <= ddeg; i++) {
    //        final double real = is.readDouble();
    //        final double imag = is.readDouble();
    //        ddata[i] = new DoubleComplexNumber(real, imag);
    //      }
    //    } else {
    //      for (int i = 0; i <= ndeg; i++) {
    //        final long dr = EndianTransformer.flip(is.readLong());
    //        final long di = EndianTransformer.flip(is.readLong());
    //        final double real = Double.longBitsToDouble(dr);
    //        final double imag = Double.longBitsToDouble(di);
    //        ndata[i] = new DoubleComplexNumber(real, imag);
    //      }
    //      for (int i = 0; i <= ddeg; i++) {
    //        final long dr = EndianTransformer.flip(is.readLong());
    //        final long di = EndianTransformer.flip(is.readLong());
    //        final double real = Double.longBitsToDouble(dr);
    //        final double imag = Double.longBitsToDouble(di);
    //        ddata[i] = new DoubleComplexNumber(real, imag);
    //      }
    //    }
    //
    //    return new DoubleRationalPolynomial(new DoublePolynomial(ndata, var), new DoublePolynomial(ddata, var));
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
    final StringBuffer sb = new StringBuffer();

    if (name.length() != 0) {
      sb.append(name);
      sb.append(" = "); //$NON-NLS-1$
    }

    sb.append(toMmString());

    if (withNewLine) {
      String newLine = System.getProperty("line.separator"); //$NON-NLS-1$
      sb.append(";"); //$NON-NLS-1$
      sb.append(newLine);
      sb.append(newLine);
    }

    output.write(sb.toString());
    output.flush();
  }

//  /**
//   * <code>opponent</code>を<code>operator</code>で指定された演算子で比較します。
//   * 
//   * @param operator 比較演算子(". &lt;", ". &lt;=", ".&gt;", ".&gt;=", ".==", ".!=")
//   * @param opponent 比較対象
//   * @return 比較式が正しければtrue、そうでなければfalse
//   */
//  public final boolean compare(final String operator, final DoubleRationalPolynomial opponent) {
//    if (operator.equals(".!=")) { //$NON-NLS-1$
//      return !equals(opponent, 0);
//    }
//    if (operator.equals(".==")) { //$NON-NLS-1$
//      return equals(opponent, 0);
//    }
//
//    throw new IllegalArgumentException();
//  }
//
//  //
//  // GridElementの実装
//  //
//
  /**
   * {@inheritDoc}
   */
  public final DoubleRationalPolynomial createZero() {
    return new DoubleRationalPolynomial(0);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final boolean isZero() {
//    return getNumerator().isZero();
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final boolean compare(final String operator, final GridElement<?> opponent) {
//    if (!(opponent instanceof DoubleRationalPolynomial)) {
//      return false;
//    }
//
//    return compare(operator, (DoubleRationalPolynomial)opponent);
//  }

  /**
   * {@inheritDoc}
   */
  public final DoubleRationalPolynomial[] createArray(final int size) {
    return new DoubleRationalPolynomial[size];
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleRationalPolynomial[][] createArray(final int rowSize, final int columnSize) {
    return new DoubleRationalPolynomial[rowSize][columnSize];
  }

//  /**
//   * Creates an array.
//   * 
//   * @param elements elements
//   * @return array
//   */
//  public final DoubleRationalPolynomial[] createArray(final DoubleRationalPolynomial[] elements) {
//    final int size = elements.length;
//    final DoubleRationalPolynomial[] array = new DoubleRationalPolynomial[size];
//    System.arraycopy(elements, 0, array, 0, size);
//    return array;
//  }

//  /**
//   * Creates an array.
//   * 
//   * @param elements elements.
//   * @return array
//   */
//  public final DoubleRationalPolynomial[][] createArray(final DoubleRationalPolynomial[][] elements) {
//    final int rowSize = elements.length;
//    final int columnSize = rowSize == 0 ? 0 : elements[0].length;
//
//    final DoubleRationalPolynomial[][] array = new DoubleRationalPolynomial[rowSize][columnSize];
//    for (int row = 0; row < rowSize; row++) {
//      System.arraycopy(elements[row], 0, array[row], 0, columnSize);
//    }
//    return array;
//  }

  //
  // MatrixElementの実装
  //

//  /**
//   * {@inheritDoc}
//   */
//  public final boolean isFinite() {
//    return getNumerator().isFinite() && getDenominator().isFinite();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final boolean isInfinite() {
//    return getNumerator().isInfinite() || getDenominator().isInfinite();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final boolean isNaN() {
//    return getNumerator().isNaN() || getDenominator().isNaN();
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final boolean compare(final String operator, final double opponent) {
//    if (operator.equals(".!=")) { //$NON-NLS-1$
//      if (getNumeratorDegree() != 0 || getDenominatorDegree() != 0) {
//        return true;
//      }
//
//      final DoubleNumber value = getNumerator().getCoefficient(0).divide(getDenominator().getCoefficient(0));
//      return !value.equals(new DoubleNumber(opponent));
//    } else if (operator.equals(".==")) { //$NON-NLS-1$
//      if (getNumeratorDegree() != 0 || getDenominatorDegree() != 0) {
//        return false;
//      }
//
//      final DoubleNumber value = getNumerator().getCoefficient(0).divide(getDenominator().getCoefficient(0));
//      return value.equals(new DoubleNumber(opponent));
//    }
//
//    throw new IllegalArgumentException();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final boolean compare(final String operator, final int opponent) {
//    return compare(operator, (double)opponent);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final boolean isZero(final double tolerance) {
//    return getNumerator().isZero(tolerance);
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final boolean isZero(final DoubleNumber tolerance) {
//    return getNumerator().isZero(tolerance);
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final boolean isUnit() {
//    return getNumerator().isUnit() && getDenominator().isUnit();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final boolean isUnit(final double tolerance) {
//    return getNumerator().isUnit(tolerance) && getDenominator().isUnit(tolerance);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final boolean isUnit(final DoubleNumber tolerance) {
//    return getNumerator().isUnit(tolerance) && getDenominator().isUnit(tolerance);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomialMatrix createGrid(final int rowSize, final int columnSize, final Scalar<?,?>[][] elements) {
//    return new DoubleRationalPolynomialMatrix(rowSize, columnSize, (DoubleRationalPolynomial[][])elements);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomialMatrix createGrid(final int rowSize, final int columnSize, final DoubleRationalPolynomial[][] elements) {
//    return new DoubleRationalPolynomialMatrix(rowSize, columnSize, elements);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomialMatrix createGrid(final DoubleRationalPolynomial[] elements) {
//    return new DoubleRationalPolynomialMatrix((DoubleRationalPolynomial[])elements);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomialMatrix createGrid(final DoubleRationalPolynomial[] elements) {
//    return new DoubleRationalPolynomialMatrix(elements);
//  }

  /**
   * {@inheritDoc}
   */
  public final DoubleRationalPolynomial createUnit() {
    return new DoubleRationalPolynomial(1);
  }

//  /**
//   * 数値を加えた有理多項式を生成します。
//   * 
//   * @param value 加えられる数値
//   * @return 足し算の結果
//   */
//  public final DoubleRationalPolynomial add(final DoubleNumber value) {
//    return add(new DoubleRationalPolynomial(value));
//  }
//
//  /**
//   * 数値を引いた有理多項式を生成します。
//   * 
//   * @param value 引かれる数値
//   * @return 引き算の結果
//   */
//  public final DoubleRationalPolynomial subtract(final DoubleNumber value) {
//    return subtract(new DoubleRationalPolynomial(value));
//  }
//
//  /**
//   * 数値を乗じた有理多項式を生成します。
//   * 
//   * @param value 乗じられる数値
//   * @return 掛け算の結果
//   */
//  public final DoubleRationalPolynomial multiply(final DoubleNumber value) {
//    return new DoubleRationalPolynomial(getNumerator().multiply(value), getDenominator().clone());
//  }

  /**
   * 数値を乗じた有理多項式を生成します。
   * 
   * @param value 乗じられる数値
   * @return 掛け算の結果
   */
  public final DoubleComplexRationalPolynomial multiply(final DoubleComplexNumber value) {
    return new DoubleComplexRationalPolynomial(getNumerator().multiply(value), getDenominator().clone());
  }

//  /**
//   * 数値で割った有理多項式を生成します。
//   * 
//   * @param value 割る数値
//   * @return 割り算の結果
//   */
//  public final DoubleRationalPolynomial divide(final DoubleNumber value) {
//    return new DoubleRationalPolynomial(getNumerator().divide(value), getDenominator().clone());
//  }
//
//  /**
//   * 数値を割った有理多項式を生成します。
//   * 
//   * @param value 割られる数値
//   * @return 掛け算の結果
//   */
//  public final DoubleRationalPolynomial leftDivide(DoubleNumber value) {
//    return new DoubleRationalPolynomial(getDenominator().multiply(value), getNumerator().clone());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial transformFrom(final int value) {
//    return new DoubleRationalPolynomial(value, getVariable());
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoubleRationalPolynomial transformFrom(final double value) {
//    return new DoubleRationalPolynomial(value, getVariable());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public final DoubleRationalPolynomial transformFrom(final GridElement<?> value) {
//    if (super.isTransformableFrom(value)) {
//      return super.transformFrom(value);
//    }
//
//    if (value instanceof DoubleNumber) {
//      return new DoubleRationalPolynomial(((DoubleNumber)value).doubleValue(), getVariable());
//    }
//
////    if (value instanceof DoubleComplexNumber) {
////      return new DoubleRationalPolynomial((DoubleComplexNumber)value, getVariable());
////    }
//
//    if (value instanceof DoublePolynomial) {
//      return new DoubleRationalPolynomial((DoublePolynomial)value);
//    }
//
//    throw new RuntimeException(Messages.getString("RationalPolynomial.21")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public final boolean isTransformableFrom(final GridElement<?> value) {
//    if (super.isTransformableFrom(value)) {
//      return true;
//    }
//
//    if (value instanceof DoubleNumber) {
//      return true;
//    }
//
////    if (value instanceof DoubleComplexNumber) {
////      return true;
////    }
//
//    if (value instanceof DoublePolynomial) {
//      return true;
//    }
//
//    return false;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getAddOperator() {
//    return DoubleRationalPolynomialAddOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getDivideOperator() {
//    return DoubleRationalPolynomialDivideOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getLeftDivideOperator() {
//    return DoubleRationalPolynomialLeftDivideOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getMultiplyOperator() {
//    return DoubleRationalPolynomialMultiplyOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getSubtractOperator() {
//    return DoubleRationalPolynomialSubtractOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarEqual getEqualOperator() {
//    return DoubleRationalPolynomialEqual.getInstance();
//  }

  /**
   * 成分が零である{@link DoubleRationalPolynomial}の2次元配列を返します。
   * 
   * @param rowSize 行の数
   * @param columnSize 行の数
   * @param variableName 変数の名前
   * @return 成分が零である{@link DoubleRationalPolynomial}の2次元配列を返します。
   */
  public static DoubleRationalPolynomial[][] createZeroArray(final int rowSize, final int columnSize, final String variableName) {
    final DoubleRationalPolynomial[][] elements = new DoubleRationalPolynomial[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        elements[i][j] = new DoubleRationalPolynomial(0, variableName);
      }
    }
    return elements;
  }

//  /**
//   * 出力の幅を設定します。
//   * 
//   * @param displayWidth 出力の幅
//   */
//  public final void setDisplayWidth(final int displayWidth) {
//    this.displayWidth = displayWidth;
//  }

//  /**
//   * 出力の幅を返します。
//   * 
//   * @return 出力の幅
//   */
//  public final int getDisplayWidth() {
//    return this.displayWidth;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setRealPart(final Scalar<?> realPart) {
//    if (isTransformableFrom(realPart)) {
//      setRealPart(transformFrom(realPart));
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("AbstractSymbolicScalar.4")); //$NON-NLS-1$
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final void setImaginaryPart(final Scalar<?> imagPart) {
//    if (isTransformableFrom(imagPart)) {
//      setImaginaryPart(transformFrom(imagPart));
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("AbstractSymbolicScalar.5")); //$NON-NLS-1$
//  }
  
//  /**
//   * Generates a RationalPolynomial.
//   * 
//   * @return RationalPolynomial
//   */
//  public RationalPolynomial<DoubleNumber,DoubleNumberMatrix> toRationalPolynomial() {
//    return new RationalPolynomial<>(this);
//  }
  
  /**
   * 値を加えた成分を生成します。
   * 
   * @param value 加える値
   * @return 足し算の結果
   */
  public DoubleComplexRationalPolynomial add(DoubleComplexRationalPolynomial value) {
    return new DoubleComplexRationalPolynomial(this).add(value);
  }

  /**
   * 値を引きます。
   * 
   * @param value 引く値
   * @return 引き算の結果
   */
  public DoubleComplexRationalPolynomial subtract(DoubleComplexRationalPolynomial value) {
    return new DoubleComplexRationalPolynomial(this).subtract(value);
  }

  /**
   * 値を掛けます。
   * 
   * @param value 掛ける値
   * @return 掛け算の結果
   */
  public DoubleComplexRationalPolynomial multiply(DoubleComplexRationalPolynomial value) {
    return new DoubleComplexRationalPolynomial(this).multiply(value);
  }

  /**
   * 値で割ります。
   * 
   * @param value 割る値
   * @return 割り算の結果
   */
  public DoubleComplexRationalPolynomial divide(DoubleComplexRationalPolynomial value) {
    return new DoubleComplexRationalPolynomial(this).divide(value);
  }


  /**
   * 値を割ります。
   * 
   * @param value 割られる値
   * @return 割り算の結果
   */
  public DoubleComplexRationalPolynomial leftDivide(DoubleComplexRationalPolynomial value) {
    return new DoubleComplexRationalPolynomial(this).leftDivide(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomial create(double numerator, DoublePolynomial denominator) {
    return new DoubleRationalPolynomial(numerator,denominator);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomial create(DoubleNumber numerator, DoublePolynomial denominator) {
    return new DoubleRationalPolynomial(numerator,denominator);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomial create(DoublePolynomial numerator, DoublePolynomial denominator) {
    return new DoubleRationalPolynomial(numerator,denominator);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomial create(DoublePolynomial numerator, double denominator) {
    return new DoubleRationalPolynomial(numerator,denominator);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomial create(DoublePolynomial numerator, DoubleNumber denominator) {
    return new DoubleRationalPolynomial(numerator,denominator);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomial create(DoublePolynomial numerator) {
    return new DoubleRationalPolynomial(numerator);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomial create(int numerator, String variableName) {
    return new DoubleRationalPolynomial(numerator,variableName);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomial create(double numerator, String variableName) {
    return new DoubleRationalPolynomial(numerator,variableName);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomial create(DoubleNumber numerator) {
    return new DoubleRationalPolynomial(numerator);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomial create(DoubleNumber numerator, String variableName) {
    return new DoubleRationalPolynomial(numerator,variableName);
  }
  
  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomialMatrix createGrid(final int rowSize, final int columnSize, final DoubleRationalPolynomial[][] elements) {
    return new DoubleRationalPolynomialMatrix(rowSize, columnSize, elements);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomialMatrix createGrid(final DoubleRationalPolynomial[] elements) {
    return new DoubleRationalPolynomialMatrix(elements);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexRationalPolynomial toComplex() {
    return new DoubleComplexRationalPolynomial(this);
  }
  

}