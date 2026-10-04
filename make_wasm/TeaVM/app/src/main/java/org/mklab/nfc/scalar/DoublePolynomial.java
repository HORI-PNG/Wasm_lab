/**
 * $Id: Polynomial.java,v 1.172 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.scalar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;

import org.mklab.nfc.matrix.BooleanMatrix;
import org.mklab.nfc.matrix.DoubleComplexMatrix;
import org.mklab.nfc.matrix.DoubleComplexPolynomialMatrix;
import org.mklab.nfc.matrix.DoubleComplexRationalPolynomialMatrix;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.DoublePolynomialMatrix;
import org.mklab.nfc.matrix.DoubleRationalPolynomialMatrix;
import org.mklab.nfc.matrix.IntMatrix;
import org.mklab.nfc.matrix.MatrixSizeException;
import org.mklab.nfc.matrix.misc.CompanionMatrix;
import org.mklab.nfc.matx.MatxObject;
import org.mklab.nfc.matx.MxDataHead;
import org.mklab.nfc.util.EndianTransformer;
import org.mklab.nfc.util.PolynomialTokenizer;


/**
 * 多項式を表すクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.172 $
 */
public class DoublePolynomial extends AbstractSymbolicScalar<DoublePolynomial,DoublePolynomialMatrix,DoubleNumber,DoubleMatrix> implements RealPolynomial<DoublePolynomial,DoublePolynomialMatrix,DoubleComplexPolynomial,DoubleComplexPolynomialMatrix,DoubleRationalPolynomial,DoubleRationalPolynomialMatrix,DoubleComplexRationalPolynomial,DoubleComplexRationalPolynomialMatrix,DoubleNumber,DoubleMatrix,DoubleComplexNumber,DoubleComplexMatrix>, MatxObject {

  /** シリアルバージョン 。 */
  private static final long serialVersionUID = -8394079094815670802L;

  /** 多項式の係数。 */
  private DoubleMatrix coefficients;

  /** 多項式の次数。 */
  private int degree;

  /** 多項式変数。 */
  private String variable;

  /** 出力の幅。 */
  private int displayWidth = 1000;

  /**
   * <code>constant</code>を係数とする0次多項式を生成します。
   * 
   * <p>多項式変数はデフォルトの"<i>s</i>"です。
   * 
   * @param constant 0次の係数(定数項)
   */
  public DoublePolynomial(final int constant) {
    this(constant, (String)null);
  }

  /**
   * <code>constant</code>を係数とする0次多項式を生成します。
   * 
   * <p>多項式変数はデフォルトの"<i>s</i>"です。
   * 
   * @param constant 0次の係数(定数項)
   */
  public DoublePolynomial(final double constant) {
    this(constant, (String)null);
  }

  /**
   * 0次の実多項式(定数)を生成します。
   * 
   * @param constant 0次の係数
   * @param variableName 多項式変数
   */
  public DoublePolynomial(final double constant, final String variableName) {
    this(new double[] {constant}, variableName);
  }

  /**
   * 多項式変数(1次の係数が1、0次の係数が0である1次の多項式)を生成します。
   * 
   * @param variableName 多項式変数
   */
  public DoublePolynomial(final String variableName) {
    this(new double[] {0, 1}, variableName);
  }

  /**
   * <code>constant</code>を係数とする0次の多項式を生成します。
   * 
   * <p>多項式変数はデフォルトの"<i>s</i>"です。
   * 
   * @param constant 0次の係数
   */
  public DoublePolynomial(DoubleNumber constant) {
    this(constant, (String)null);
  }

  /**
   * <code>constant</code>を係数とする0次の多項式を生成します。
   * 
   * @param constant 0次の係数
   * @param variableName 多項式変数
   */
  public DoublePolynomial(final DoubleNumber constant, final String variableName) {
    final DoubleNumber[] coefs = constant.createArray(1);
    coefs[0] = constant;

    this.degree = 0;
    this.coefficients = new DoubleMatrix(DoubleNumberUtil.createArray(coefs));
    this.variable = variableName;
  }

  /**
   * <code>coefficients</code>を係数とする実多項式を生成します。
   * 
   * <p>多項式変数はデフォルトの"<i>s</i>"です。例えば、
   * 
   * <blockquote> <code>double[] coefficients = new double {3, 2, 1};</code> </blockquote>
   * 
   * とすると、
   * 
   * <blockquote> 3 + 2 s + s^2</blockquote>
   * 
   * となります。
   * 
   * @param coefficients 係数の配列
   */
  public DoublePolynomial(final double[] coefficients) {
    this(coefficients, (String)null);
  }

  /**
   * <code>coefficients</code>を係数とする実多項式を生成します。
   * 
   * <p>例えば、
   * 
   * <blockquote> <code>double[] coefficients = new double {3, 2, 1};</code> </blockquote>
   * 
   * <blockquote> <code>String variable = "x";</code> </blockquote>
   * 
   * とすると、
   * 
   * <blockquote> <code>3 + 2 x + x^2</code> </blockquote>
   * 
   * となります。
   * 
   * @param coefficients 係数の配列
   * @param variableName 多項式変数
   */
  public DoublePolynomial(final double[] coefficients, final String variableName) {
    this.degree = coefficients.length - 1;
    this.coefficients = new DoubleMatrix(coefficients);
    this.variable = variableName;
  }

  /**
   * <code>coefficients</code>を係数とする多項式を生成します。
   * 
   * <p>多項式変数はデフォルトの"<i>s</i>"です。
   * 
   * @param coefficients 係数の配列
   */
  public DoublePolynomial(final DoubleNumber[] coefficients) {
    this(coefficients, (String)null);
  }

  /**
   * <code>coefficients</code>を係数とする多項式を生成します。
   * 
   * @param coefficients 係数の配列
   * @param variableName 多項式変数
   */
  public DoublePolynomial(final DoubleNumber[] coefficients, final String variableName) {
    this.degree = coefficients.length - 1;
    this.coefficients =   new DoubleMatrix(DoubleNumberUtil.createArray(coefficients));
    this.variable = variableName;
  }

  /**
   * 新しく生成された<code>Polynomial</code>オブジェクトを初期化します。
   * 
   * @param coefficientVector 係数をもつベクトル(行列)
   */
  public DoublePolynomial(final IntMatrix coefficientVector) {
    this(new DoubleMatrix(coefficientVector));
  }

  /**
   * 新しく生成された<code>Polynomial</code>オブジェクトを初期化します。
   * 
   * @param coefficientVector 係数をもつベクトル(行列)
   */
  public DoublePolynomial(final DoubleMatrix coefficientVector) {
    this(coefficientVector, (String)null);
  }

  /**
   * 新しく生成された<code>Polynomial</code>オブジェクトを初期化します。
   * 
   * @param coefficientVector 係数をもつベクトル(行列)
   * @param variableName 多項式変数
   */
  public DoublePolynomial(final DoubleMatrix coefficientVector, final String variableName) {
    if (coefficientVector.getRowSize() != 1) {
      if (coefficientVector.getColumnSize() != 1) {
        throw new MatrixSizeException(MatrixSizeException.NOT_A_VECTOR_MATRIX);
      }
      this.degree = coefficientVector.getRowSize() - 1;
      this.coefficients = coefficientVector.transpose();
    } else {
      this.degree = coefficientVector.getColumnSize() - 1;
      this.coefficients = coefficientVector.createClone();
    }
    this.variable = variableName;
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial create(final int value) {
    return new DoublePolynomial(value);
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial create(final double value) {
    return new DoublePolynomial(value);
  }
  
  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial create(final DoubleMatrix value) {
    return new DoublePolynomial(value);
  }
  
  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial create(final DoubleMatrix value, String variableName) {
    return new DoublePolynomial(value, variableName);
  }
  
  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial create(final double[] value) {
    return new DoublePolynomial(value);
  }
  
  
  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial create(final DoubleNumber[] value) {
    return new DoublePolynomial(value);
  }
  
  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial create(final DoubleNumber[] value, String variableName) {
    return new DoublePolynomial(value, variableName);
  }
    
  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial create(final double[] value, String variableName) {
    return new DoublePolynomial(value, variableName);
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial create(final DoubleNumber value) {
    return create(value.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial create(final DoubleNumber value, String variableName) {
    return create(value.doubleValue(), variableName);
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial create(final double value, String variableName) {
    return new DoublePolynomial(value, variableName);
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial create(String variableName) {
    return new DoublePolynomial(variableName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DoublePolynomial clone() {
    final DoublePolynomial ans = super.clone();
    ans.coefficients = this.coefficients.createClone();
    return ans;
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

    return equals((DoublePolynomial)opponent, 0);
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
    hashCode = prime * hashCode + (this.coefficients == null ? 0 : this.coefficients.hashCode());
    hashCode = prime * hashCode + this.degree;
    hashCode = prime * hashCode + (this.variable == null ? 0 : this.variable.hashCode());
    hashCode = prime * hashCode + this.displayWidth;
    return hashCode;
  }

//  /**
//   * {@inheritDoc}
//   */
//  public boolean equals(final SymbolicScalar<?,?,?,?> opponent, final DoubleNumber tolerance) {
//    if (getClass() == opponent.getClass()) {
//      return equals((DoublePolynomial)opponent, tolerance);
//    }
//
//    if (isTransformableFrom(opponent)) {
//      return equals((SymbolicScalar<?,?,?,?>)transformFrom(opponent), tolerance);
//    }
//
//    if (opponent.isTransformableFrom(this)) {
//      return ((Polynomial<DoubleNumber,?>)opponent.transformFrom(this)).equals(opponent, tolerance);
//    }
//
//    return false;
//  }

  /**
   * {@inheritDoc}
   */
  public final boolean equals(final DoublePolynomial opponent, final DoubleNumber tolerance) {
    if (hasSameVariable(opponent) == false) {
      return false;
    }

    return this.coefficients.equals(opponent.coefficients, tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean equals(final DoublePolynomial opponent, final double tolerance) {
    if (hasSameVariable(opponent) == false) {
      return false;
    }

    return this.coefficients.equals(opponent.coefficients, tolerance);
  }

  /**
   * 同じクラスの指定された次数の多項式を生成します。
   * 
   * @param newDegree 次数
   * @return 多項式
   */
  private DoublePolynomial createSameClassPolynomial(final int newDegree) {
    return new DoublePolynomial(this.coefficients.createZero(1, newDegree + 1), this.variable);
  }

  /**
   * {@inheritDoc}
   */
  public final int getDegree() {
    //simplify();
    return this.degree;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix getCoefficients() {
    return this.coefficients.createClone();
  }

  /**
   * (倍精度)実多項式の<code>order</code>次の係数を返します。
   * 
   * @param order 次数
   * @return 実多項式の係数
   */
  private double getDoubleCoefficient(final int order) {
    return this.coefficients.getDoubleElement(order + 1);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber getCoefficient(final int order) {
    return this.coefficients.getElement(order + 1);
  }

  /**
   * {@inheritDoc}
   */
  public final void setCoefficient(final int order, final double value) {
    this.coefficients.setElement(1, order + 1, value);
  }

  /**
   * {@inheritDoc}
   */
  public final void setCoefficient(final int order, final int value) {
    this.coefficients.setElement(1, order + 1, value);
  }

  /**
   * {@inheritDoc}
   */
  public final void setCoefficient(final int order, final DoubleNumber value) {
    this.coefficients.setElement(1, order + 1, value.doubleValue());
  }

//  /**
//   * 複素多項式の実部多項式を設定します。
//   * 
//   * @param realPart 実部多項式
//   */
//  public final void setRealPart(final DoublePolynomial realPart) {
//    if (this.isReal()) {
//      throw new UnsupportedOperationException(Messages.getString("Polynomial.1")); //$NON-NLS-1$
//    }
//
//    if (realPart.isComplex()) {
//      throw new IllegalArgumentException(Messages.getString("Polynomial.2")); //$NON-NLS-1$
//    }
//
//    if (getDegree() < realPart.getDegree()) {
//      this.coefficients = expand(realPart.getDegree()).coefficients;
//      this.coefficients.setRealPart(realPart.getCoefficients());
//    } else if (getDegree() == realPart.getDegree()) {
//      this.coefficients.setRealPart(realPart.getCoefficients());
//    } else {
//      this.coefficients.setRealPart(realPart.expand(getDegree()).coefficients);
//    }
//
//    this.degree = this.coefficients.length() - 1;
//  }

//  /**
//   * 複素多項式の虚部多項式を設定します。
//   * 
//   * @param imagPart 虚部多項式
//   */
//  public final void setImagPart(final DoublePolynomial imagPart) {
//    if (this.isReal()) {
//      throw new UnsupportedOperationException(Messages.getString("Polynomial.3")); //$NON-NLS-1$
//    }
//
//    if (imagPart.isComplex()) {
//      throw new IllegalArgumentException(Messages.getString("Polynomial.4")); //$NON-NLS-1$
//    }
//
//    if (getDegree() < imagPart.getDegree()) {
//      this.coefficients = expand(imagPart.getDegree()).coefficients;
//      this.coefficients.setImaginaryPart(imagPart.getCoefficients());
//    } else if (getDegree() == imagPart.getDegree()) {
//      this.coefficients.setImaginaryPart(imagPart.getCoefficients());
//    } else {
//      this.coefficients.setImaginaryPart(imagPart.expand(getDegree()).coefficients);
//    }
//
//    this.degree = this.coefficients.length() - 1;
//  }

  /**
   * {@inheritDoc}
   */
  public final String getVariable() {
    return this.variable;
  }

  /**
   * {@inheritDoc}
   */
  public final void setVariable(final String variableName) {
    this.variable = variableName;
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial add(final DoublePolynomial value) {
    checkVariable(value);

    if (hasLargerDgree(value)) {
      return this.add(value.expand(this.degree));
    } else if (hasSmallerDegree(value)) {
      return this.expand(value.degree).add(value);
    }

    final DoubleMatrix ansCoef = this.coefficients.add(value.coefficients);
    final String var = (this.variable != null ? this.variable : value.variable);
    final DoublePolynomial ans = new DoublePolynomial(ansCoef, var);
    ans.simplify();
    return ans;
  }
  
//  /**
//   * 多項式との和を生成します。
//   * 
//   * @param value 多項式
//   * @return pとの和
//   */
//  public final DoubleComplexPolynomial add(final DoubleComplexPolynomial value) {
//    return new DoubleComplexPolynomial((Polynomial<DoubleComplexNumber,DoubleComplexMatrix>)value.add(this));
//  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial add(final double value) {
    final DoubleMatrix ansCoef = this.coefficients.createClone();
    ansCoef.setElement(1, 1, ansCoef.getElement(1, 1).add(value));
    final DoublePolynomial ans = new DoublePolynomial(ansCoef, this.variable);
    ans.simplify();
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial add(final DoubleNumber value) {
    return add(value.doubleValue());
  }

  
  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial add(final int value) {
    final DoubleMatrix ansCoef = this.coefficients.createClone();
    ansCoef.setElement(1, 1, ansCoef.getElement(1, 1).add(value));
    final DoublePolynomial ans = new DoublePolynomial(ansCoef, this.variable);
    ans.simplify();
    return ans;
  }

//  /**
//   * 数値との和を生成します。
//   * 
//   * @param value 数値
//   * @return 数値との和
//   */
//  public final DoublePolynomial add(final NumericalScalar<?,?> value) {
//    return (DoublePolynomial)super.add(value);
//  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial subtract(final DoublePolynomial value) {
    checkVariable(value);
    final String var = (this.variable != null ? this.variable : value.variable);

    if (this.hasLargerDgree(value)) {
      return this.subtract(value.expand(this.degree));
    } else if (hasSmallerDegree(value)) {
      return this.expand(value.degree).subtract(value);
    }

    final DoubleMatrix mc = this.coefficients.subtract(value.coefficients);
    final DoublePolynomial ans = new DoublePolynomial(mc, var);
    ans.simplify();
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial subtract(final double value) {
    final DoubleMatrix ansCoef = this.coefficients.createClone();
    ansCoef.setElement(1, 1, ansCoef.getElement(1, 1).subtract(value));
    final DoublePolynomial ans = new DoublePolynomial(ansCoef, this.variable);
    ans.simplify();
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial subtract(final DoubleNumber value) {
    return subtract(value.doubleValue());
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial subtract(final int value) {
    final DoubleMatrix ansCoef = this.coefficients.createClone();
    ansCoef.setElement(1, 1, ansCoef.getElement(1, 1).subtract(value));
    final DoublePolynomial ans = new DoublePolynomial(ansCoef, this.variable);
    ans.simplify();
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial multiply(final DoublePolynomial value) {
    this.checkVariable(value);
    final String var = (this.variable != null ? this.variable : value.variable);

    final DoubleMatrix coef1 = this.coefficients;
    final DoubleMatrix coef2 = value.coefficients;

    final int newDegree = this.degree + value.degree;

    final double[] ans = new double[newDegree + 1];
    for (int i = 0; i <= this.degree; i++) {
      for (int j = 0; j <= value.degree; j++) {
        ans[i + j] += coef1.getDoubleElement(1, i + 1) * coef2.getDoubleElement(1, j + 1);
      }
    }
    final DoublePolynomial ansPolynomial = new DoublePolynomial(new DoubleMatrix(ans), var);
    ansPolynomial.simplify();
    return ansPolynomial;
  }

//  /**
//   * 有理多項式との積を生成します。
//   * 
//   * @param value 有理多項式
//   * @return 有理多項式との積
//   */
//  public final DoubleRationalPolynomial multiply(final DoubleRationalPolynomial value) {
//    return value.multiply(this);
//  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial multiply(final double value) {
    if (value == 0) {
      return new DoublePolynomial(0, this.variable);
    }

    if (value == 1) {
      return this.clone();
    }

    if (value == -1) {
      return this.unaryMinus();
    }

    return new DoublePolynomial(this.coefficients.multiply(value), this.variable);
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial multiply(final int value) {
    if (value == 0) {
      return new DoublePolynomial(0, this.variable);
    }

    if (value == 1) {
      return this.clone();
    }

    if (value == -1) {
      return this.unaryMinus();
    }

    return new DoublePolynomial(this.coefficients.multiply(value), this.variable);
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial divide(final DoublePolynomial value) {
    throw new UnsupportedOperationException();
//    this.checkVariable(value);
//    final DoublePolynomial numerator = this.clone();
//    final DoublePolynomial denominator = value.clone();
//    if (this.variable == null) {
//      denominator.setVariable(value.variable);
//    } else {
//      numerator.setVariable(this.variable);
//    }
//
//    return new DoubleRationalPolynomial(numerator, denominator);
  }

//  /**
//   * 有理多項式による割り算による有理多項式を生成します。
//   * 
//   * @param value 割る有理多項式
//   * @return 割り算の結果
//   */
//  public final DoubleRationalPolynomial divide(final DoubleRationalPolynomial value) {
//    return this.multiply(value.inverse());
//  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial divide(final double value) {
    if (value == 0) {
      throw new IllegalArgumentException(Messages.getString("Polynomial.5")); //$NON-NLS-1$
    }

    if (value == 1) {
      return this.clone();
    }

    if (value == -1) {
      return this.unaryMinus();
    }

    return new DoublePolynomial(this.coefficients.divide(value), this.variable);
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial divide(final int value) {
    if (value == 0) {
      throw new IllegalArgumentException(Messages.getString("Polynomial.6")); //$NON-NLS-1$
    }

    if (value == 1) {
      return this.clone();
    }

    if (value == -1) {
      return this.unaryMinus();
    }

    return new DoublePolynomial(this.coefficients.divide(value), this.variable);
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial leftDivide(final double value) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial leftDivide(final int value) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial leftDivide(final DoublePolynomial value) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial inverse() {
    throw new UnsupportedOperationException();
    }
   
  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial power(final int m) {
    if (m < 0) {
      throw new IllegalArgumentException(Messages.getString("Polynomial.7")); //$NON-NLS-1$
    }

    if (m == 0) {
      return new DoublePolynomial(1, this.variable);
    }

    if (m == 1) {
      return this.clone();
    }

    int n = m;
    DoublePolynomial aa = this.clone();
    DoublePolynomial b2 = new DoublePolynomial(1, this.variable);
    DoublePolynomial b;

    for (;;) {
      if (n % 2 != 0) {
        b = b2.multiply(aa);
        n /= 2;
        if (n != 0) {
          b2 = b;
          aa = aa.multiply(aa);
        } else {
          break;
        }
      } else {
        n /= 2;
        aa = aa.multiply(aa);
      }
    }

    return b;
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial conjugate() {
    return new DoublePolynomial(this.coefficients.conjugate(), this.variable);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoublePolynomial getRealPart() {
//    return new DoublePolynomial(this.coefficients.getRealPart());
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoublePolynomial getImaginaryPart() {
//    return new DoublePolynomial(this.coefficients.getImaginaryPart());
//  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial unaryMinus() {
    return new DoublePolynomial(this.coefficients.unaryMinus(), this.variable);
  }

  /**
   * 多項式変数が等しいか判定します。
   * 
   * <p>どちらかが<code>null</code>の場合、比較しません。
   * 
   * @param opponent 比較する多項式
   */
  private void checkVariable(final DoublePolynomial opponent) {
    if (this.variable == null || opponent.variable == null) {
      return;
    }
    if (hasSameVariable(opponent) == false) {
      throw new RuntimeException(Messages.getString("Polynomial.8")); //$NON-NLS-1$
    }
  }

  /**
   * {@inheritDoc}
   */
  public final boolean hasSameVariable(final DoublePolynomial opponent) {
    if (this.variable == null || opponent.variable == null) {
      return true;
    }

    return this.variable.equals(opponent.variable);
  }

  /**
   * 次数が多項式<code>opponent</code>の次数より大きいか判定します。
   * 
   * @param opponent 多項式
   * @return 次数が<code>opponent</code>より大きければ true、そうでなければ false
   */
  private boolean hasLargerDgree(final DoublePolynomial opponent) {
    return this.degree > opponent.degree;
  }

  /**
   * 次数が多項式<code>opponent</code> の次数より小さいか判定します。
   * 
   * @param opponent 多項式
   * @return 次数が<code>opponent</code>より小さければ true、そうでなければ false
   */
  private boolean hasSmallerDegree(final DoublePolynomial opponent) {
    return this.degree < opponent.degree;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean hasSameDegree(final DoublePolynomial opponent) {
    return this.degree == opponent.degree;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isReal() {
    return this.coefficients.isReal();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isComplex() {
    return this.coefficients.isComplex();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isSameClass(final DoublePolynomial opponent) {
    return this.coefficients.getClass() == opponent.coefficients.getClass();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isConstant() {
    return isConstant(0);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isConstant(double tolerance) {
    final int effectiveDegree = getEffectiveDegree(tolerance);
    return effectiveDegree == 0;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isConstant(final DoubleNumber tolerance) {
    final int effectiveDegree = getEffectiveDegree(tolerance);
    return effectiveDegree == 0;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero(final double tolerance) {
    if (!isConstant(tolerance)) {
      return false;
    }

    if (this.coefficients.getElement(1, 1).isZero(tolerance)) {
      return true;
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero(final DoubleNumber tolerance) {
    if (!isConstant(tolerance)) {
      return false;
    }

    if (this.coefficients.getElement(1, 1).isZero(tolerance)) {
      return true;
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit() {
    if (!isConstant()) {
      return false;
    }

    if (this.coefficients.getElement(1, 1).isUnit()) {
      return true;
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit(final double tolerance) {
    if (!isConstant(tolerance)) {
      return false;
    }

    if (this.coefficients.getElement(1, 1).isUnit(tolerance)) {
      return true;
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit(final DoubleNumber tolerance) {
    if (!isConstant(tolerance)) {
      return false;
    }

    if (this.coefficients.getElement(1, 1).isUnit(tolerance)) {
      return true;
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isFinite() {
    return this.coefficients.isFiniteElementWise().allTrue();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isInfinite() {
    return this.coefficients.isInfiniteElementWise().anyTrue();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isNaN() {
    return this.coefficients.isNanElementWise().allTrue();
  }

   /**
   * {@inheritDoc}
   */
  public DoublePolynomial expand(final int newDegree) {
     if (this.degree == newDegree) {
       return this;
     }
    final DoubleMatrix ansCoef = this.coefficients.createClone().resize(1, newDegree + 1);
    return new DoublePolynomial(ansCoef, this.variable);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber evaluate(final int value) {
    final DoubleMatrix a = this.coefficients;
    double sum = a.getDoubleElement(this.degree + 1);
    for (int i = this.degree - 1; i >= 0; i--) {
      sum = sum * value + a.getDoubleElement(i + 1);
    }
    return new DoubleNumber(sum);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber evaluate(final double value) {
    final DoubleMatrix a = this.coefficients;
    double sum = a.getDoubleElement(this.degree + 1);
    for (int i = this.degree - 1; i >= 0; i--) {
      sum = sum * value + a.getDoubleElement(i + 1);
    }
    return new DoubleNumber(sum);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleNumber evaluate(final DoubleNumber value) {
    final DoubleMatrix a = this.coefficients;
    DoubleNumber sum = new DoubleNumber(a.getDoubleElement(this.degree + 1));
    for (int i = this.degree - 1; i >= 0; i--) {
      sum = sum.multiply(value).add(a.getDoubleElement(i + 1));
    }

    return sum;
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final Scalar<?,?> evaluate(final Scalar<?,?> value) {
//    final DoubleMatrix a = this.coefficients;
//    DoubleNumber sum = new DoubleNumber(a.getDoubleElement(this.degree + 1));
//    for (int i = this.degree - 1; i >= 0; i--) {
//      sum = sum.multiply((DoubleNumber)value).add(a.getDoubleElement(i + 1));
//    }
//
//    return sum;
//  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial evaluate(final DoublePolynomial value) {
    final DoubleMatrix a = this.coefficients;
    DoublePolynomial sum = new DoublePolynomial(a.getDoubleElement(this.degree + 1), value.variable);
    for (int i = this.degree - 1; i >= 0; i--) {
      sum = sum.multiply(value).add(new DoublePolynomial(a.getDoubleElement(i + 1)));
    }

    return sum;
  }

//  /**
//   * 式変数に有理多項式を代入した評価結果を返します。
//   * 
//   * @param value 代入する有理多項式
//   * @return 式変数に有理多項式を代入した評価結果
//   */
//  public final DoubleRationalPolynomial evaluate(final DoubleRationalPolynomial value) {
//    DoubleRationalPolynomial sum = new DoubleRationalPolynomial(this.coefficients.getDoubleElement(1, this.degree + 1));
//    for (int i = 1; i <= this.degree; i++) {
//      final DoubleRationalPolynomial tmp1 = sum.multiply(value);
//      final DoubleRationalPolynomial tmp2 = new DoubleRationalPolynomial(this.coefficients.getDoubleElement(1, this.degree - i + 1));
//      sum = tmp1.add(tmp2);
//    }
//
//    return sum;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final NumericalMatrix<?,?> evaluate(final DoubleMatrix value) {
//    final NumericalMatrix<?,?> unit = value.createUnit(value.getColumnSize());
//    NumericalMatrix<?,?> sum = (NumericalMatrix<?, ?>)unit.multiply(getCoefficient(this.degree).doubleValue());
//    for (int i = this.degree - 1; i >= 0; i--) {
//      sum = (NumericalMatrix<?, ?>)sum.multiply(value).add(unit.multiply(getCoefficient(i).doubleValue()));
//    }
//
//    return sum;
//  }

  /**
   * Evaluate the polynomial with integer.
   * 
   * @param value integer 
   * @return evaluated result
   */
  public final DoubleMatrix evaluate(final IntMatrix value) {
    final IntMatrix unit = value.createUnit(value.getColumnSize());
    DoubleMatrix sum = new DoubleMatrix(unit).multiply(getCoefficient(this.degree).doubleValue());
    for (int i = this.degree - 1; i >= 0; i--) {
      sum = (sum.multiply(new DoubleMatrix(value))).add(new DoubleMatrix(unit).multiply(getCoefficient(i).doubleValue()));
    }

    return sum;
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix evaluate(final DoubleMatrix value) {
    final DoubleMatrix unit = value.createUnit(value.getColumnSize());
    DoubleMatrix sum = unit.multiply(getCoefficient(this.degree));
    for (int i = this.degree - 1; i >= 0; i--) {
      sum = sum.multiply(value).add(unit.multiply(getCoefficient(i)));
    }

    return sum;
  }

//  public final DoubleMatrix evaluate(final DoubleMatrix value) {
//    final DoubleMatrix unit = value.createUnit(value.getColumnSize());
//    DoubleMatrix sum = (DoubleMatrix)unit.multiply(getCoefficient(this.degree));
//    for (int i = this.degree - 1; i >= 0; i--) {
//      sum = (DoubleMatrix)sum.multiply(value).add(unit.multiply(getCoefficient(i)));
//    }
//
//    return sum;
//  }

  /**
   * {@inheritDoc}
   */
  public final DoubleMatrix evaluateElementWise(final DoubleMatrix value) {
    final int argRowSize = value.getRowSize();
    final int argColumnSize = value.getColumnSize();

    final double[][] ans = new double[argRowSize][argColumnSize];
    for (int i = 0; i < argRowSize; i++) {
      for (int j = 0; j < argColumnSize; j++) {
        ans[i][j] = this.evaluate(value.getDoubleElement(i + 1, j + 1)).doubleValue();
      }
    }

    return new DoubleMatrix(ans);
  }

  /**
   * {@inheritDoc}
   */
  public final DoubleComplexMatrix getRoots() {
    int nonZeroCoefficientSize;
    DoubleMatrix m = this.coefficients;

    for (nonZeroCoefficientSize = m.getColumnSize(); nonZeroCoefficientSize > 0; nonZeroCoefficientSize--) {
      final double d = m.getDoubleElement(1, nonZeroCoefficientSize);
      if (d == 1) {
        break;
      } else if (d != 0) {
        m = m.multiply(1 / d);
        break;
      }
    }

    if (nonZeroCoefficientSize == 0) {
      throw new RuntimeException(Messages.getString("Polynomial.11")); //$NON-NLS-1$
    }

    if (nonZeroCoefficientSize == 1) {
      return new DoubleComplexMatrix(0,0);
    }

    final DoubleMatrix companionMatrix = CompanionMatrix.create(m.getSubVector(1, nonZeroCoefficientSize - 1));
    return companionMatrix.eigenValue();
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial derivative(final int order) {
    if (order < 0) {
      return integral(-order);
    }

    final int newDegree = this.degree - order;

    final DoublePolynomial ans;
    if (newDegree <= 0) {
      ans = this.createSameClassPolynomial(0);
    } else {
      ans = this.createSameClassPolynomial(newDegree);
    }

    final DoubleMatrix am = this.coefficients;
    final DoubleMatrix bm = ans.coefficients;
    for (int i = 0; i <= newDegree; i++) {
      bm.setElement(i + 1, am.getDoubleElement(i + order + 1));
      for (int j = 1; j <= order; j++) {
        bm.setElement(i + 1, bm.getDoubleElement(i + 1) * (i + j));
      }
    }

    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial integral() {
    return integral(1);
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial integral(final int order) {
    if (order < 0) {
      return derivative(-order);
    }

    final int newDegree = this.degree + order;
    final DoublePolynomial ans = this.createSameClassPolynomial(newDegree);

    final DoubleMatrix am = this.coefficients;
    final DoubleMatrix bm = ans.coefficients;
    for (int i = order; i <= newDegree; i++) {
      bm.setElement(i + 1, am.getDoubleElement(i - order + 1));
      for (int j = 0; j < order; j++) {
        bm.setElement(i + 1, bm.getDoubleElement(i + 1) / (i - j));
      }
    }

    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial shiftLower(final int count) {
    final int newDegree = this.degree - count;

    if (newDegree >= 0) {
      final DoublePolynomial ans = this.createSameClassPolynomial(newDegree);
      ans.partCopy(0, newDegree, this, count, this.degree);
      return ans;
    }

    return this.createSameClassPolynomial(0);
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial shiftHigher(final int count) {
    final int newDegree = this.degree + count;
    final DoublePolynomial ans = this.createSameClassPolynomial(newDegree);
    ans.partCopy(count, newDegree, this, 0, this.degree);
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final void simplify() {
    if (this.degree < 0) {
      return;
    }
    simplify(this.coefficients.getElement(1, 1).getMachineEpsilon());
  }

  /**
   * {@inheritDoc}
   */
  public final void simplify(final double tolerance) {
    final int effectiveDegree = getEffectiveDegree(tolerance);

    if (effectiveDegree < this.degree) {
      final DoublePolynomial b = this.getSubPolynomial(0, effectiveDegree);

      this.coefficients = b.coefficients;
      this.degree = b.degree;
    }
  }

  /**
   * 実質的な次数を返します。
   * 
   * @param tolerance 許容誤差
   * @return 実質的な次数
   */
  private int getEffectiveDegree(final double tolerance) {
    int effectiveDegree = 0;

    for (int i = this.degree; i >= 0; i--) {
      final double coef = getDoubleCoefficient(i);
      final boolean isCoefficientNotZero = Math.abs(coef) > tolerance || Double.isNaN(coef) || Double.isInfinite(coef);
      if (isCoefficientNotZero) {
        effectiveDegree = i;
        break;
      }
    }

    return effectiveDegree;
  }

  /**
   * 実質的な次数を返します。
   * 
   * @param tolerance 許容誤差
   * @return 実質的な次数
   */
  private int getEffectiveDegree(final DoubleNumber tolerance) {
    int effectiveDegree = 0;

    for (int i = this.degree; i >= 0; i--) {
      final double coef = getDoubleCoefficient(i);
      final boolean isCoefficientNotZero = tolerance.isLessThan(Math.abs(coef)) || Double.isNaN(coef) || Double.isInfinite(coef);
      if (isCoefficientNotZero) {
        effectiveDegree = i;
        break;
      }
    }
    return effectiveDegree;
  }

  /**
   * {@inheritDoc}
   */
  public final void simplify(final DoubleNumber tolerance) {
    final int effectiveDegree = getEffectiveDegree(tolerance);

    if (effectiveDegree < this.degree) {
      final DoublePolynomial b = this.getSubPolynomial(0, effectiveDegree);

      this.coefficients = b.coefficients;
      this.degree = b.degree;
    }
  }

  /**
   * <code>degreeMin</code>次から<code>degreeMax</code>次までを切り取り返します。
   * 
   * @param degreeMin 開始次数
   * @param degreeMax 終了次数
   * @return 切り取った多項式
   */
  private DoublePolynomial getSubPolynomial(final int degreeMin, final int degreeMax) {
    return new DoublePolynomial(this.coefficients.getSubVector(degreeMin + 1, degreeMax + 1), this.variable);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final BaseComplexSymbolicScalar<DoublePolynomial> toComplex() {
//    final BaseComplexSymbolicScalar<DoublePolynomial> ans = new BaseComplexSymbolicScalar<>(this.clone(), createZero());
//    return ans;
//  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial roundToZero(final double tolerance) {
    return new DoublePolynomial(this.coefficients.roundToZeroElementWise(tolerance));
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial roundToZero(final DoubleNumber tolerance) {
    return new DoublePolynomial(this.coefficients.roundToZeroElementWise(tolerance));
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial round() {
    return new DoublePolynomial(this.coefficients.roundElementWise());
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial ceil() {
    return new DoublePolynomial(this.coefficients.ceilElementWise());
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial floor() {
    return new DoublePolynomial(this.coefficients.floorElementWise());
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial fix() {
    return new DoublePolynomial(this.coefficients.fixElementWise());
  }

  /**
   * {@inheritDoc}
   */
  public final void print() {
    print("ans"); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final void print(final String name) {
    try {
      PrintStream output = new PrintStream(System.out, false, "UTF-8"); //$NON-NLS-1$
      print(name, output);
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException(name, e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final void print(final String name, final PrintStream output) {
    final PolynomialTokenizer pt = new PolynomialTokenizer(toString(false));
    final int width = this.displayWidth - 1 - name.length() - 3;
    output.println(name + " = " + pt.nextLine(width)); //$NON-NLS-1$

    final String marginSpace = createSpaceString(name.length() + 3);
    while (pt.hasMoreTokens()) {
      output.println(marginSpace + pt.nextLine(width));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return toString(false);
  }

  /**
   * {@inheritDoc}
   */
  public final String toString(final String coefficientFormat) {
    return toString(false, coefficientFormat);
  }

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
    return toMmStringFromDoublePolynomial(coefficientFormat);
  }

  /**
   * 倍精度実多項式を文字列に変換します。
   * 
   * @param coefficientFormat 係数の出力フォーマット
   * @return 変換で生成された文字列
   */
  private String toMmStringFromDoublePolynomial(final String coefficientFormat) {
    final String coefficientString = this.coefficients.flipLeftRight().toMmString(coefficientFormat);
    return "Polynomial(" + coefficientString + ", \"" + (this.variable == null ? "s" : this.variable) + "\")"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
  }

  /**
   * {@inheritDoc}
   */
  public final String toString(final boolean asExpression) {
    return toString(asExpression, "%G"); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final String toString(final boolean asExpression, final String coefficientFormat) {
    return toStringFromDoublePolynomialAsExpression(asExpression, coefficientFormat);
  }

  /**
   * 倍精度実多項式を文字列に変換します。
   * 
   * @param asExpression 式として評価できるようにするならばtrue、そうでなければfalse
   * @param coefficientFormat 係数の出力フォーマット
   * @return 変換で生成された文字列
   */
  private String toStringFromDoublePolynomialAsExpression(final boolean asExpression, final String coefficientFormat) {
    String var = "s"; //$NON-NLS-1$
    final String mulString;
    if (asExpression) {
      mulString = "*"; //$NON-NLS-1$
    } else {
      mulString = " "; //$NON-NLS-1$
    }

    double tolerance = DoubleNumberUtil.EPS * 100;

    if (this.variable != null) {
      var = this.variable;
    }

    if (this.degree < 0) {
      return "0"; //$NON-NLS-1$
    }

    final double c0 = getDoubleCoefficient(0);

    //    if (asExpression && this.degree == 1 && c0 == 0.0 && getDoubleCoefficient(1) == 1.0) {
    //      return "Polynomial(\"" + var + "\")"; //$NON-NLS-1$ //$NON-NLS-2$
    //    }

    String str;

    if (Math.abs(c0) < tolerance) {
      str = ""; //$NON-NLS-1$
    } else {
      final String sign;
      if (c0 < 0.0) {
        sign = " - "; //$NON-NLS-1$
      } else {
        sign = " + "; //$NON-NLS-1$
      }

      str = sign + DoubleNumber.toString(Math.abs(c0), coefficientFormat);
    }

    for (int i = 1; i <= this.degree; i++) {
      final double ci = getDoubleCoefficient(i);

      if (Math.abs(ci) < tolerance) {
        continue;
      }

      final String sign;
      if (ci < 0.0) {
        sign = " - "; //$NON-NLS-1$
      } else {
        if (i == this.degree) {
          sign = ""; //$NON-NLS-1$
        } else {
          sign = " + "; //$NON-NLS-1$
        }
      }

      final String varString;
      if (i == 1) {
        varString = var;
      } else {
        varString = var + String.format("^%d", Integer.valueOf(i)); //$NON-NLS-1$
      }

      final String coefString = DoubleNumber.toString(Math.abs(ci), coefficientFormat);

      if (coefString.equals("1")) { //$NON-NLS-1$
        str = (sign + varString) + str;
      } else {
        str = (sign + coefString + mulString + varString) + str;
      }
    }

    if (str.equals("")) { //$NON-NLS-1$
      return "0"; //$NON-NLS-1$
    }

    if (1 < str.length() && str.charAt(1) == '+') {
      return str.substring(3);
    }

    if (1 < str.length() && str.charAt(1) == '-') {
      return str.substring(1);
    }

    return str;
  }

//  /**
//   * 多項式を文字列に変換します。
//   * 
//   * @param asExpression 式として評価できるようにするならばtrue、そうでなければfalse
//   * @param coefficientFormat 係数の出力フォーマット
//   * @return 変換で生成された文字列
//   */
//  private String toStringFromPolynomial(final boolean asExpression, final String coefficientFormat) {
//    String var = "s"; //$NON-NLS-1$
//
//    final String mulString;
//    if (asExpression) {
//      mulString = "*"; //$NON-NLS-1$
//    } else {
//      mulString = " "; //$NON-NLS-1$
//    }
//
//    if (this.variable != null) {
//      var = this.variable;
//    }
//
//    if (this.degree < 0) {
//      return "0"; //$NON-NLS-1$
//    }
//
//    final DoubleNumber c0 = getCoefficient(0);
//    final DoubleNumber tolerance = c0.getMachineEpsilon().multiply(100);
//
//    String str;
//    if (c0.isZero(tolerance)) {
//      str = ""; //$NON-NLS-1$
//    } else {
//      final String sign = " + "; //$NON-NLS-1$
//      str = sign + c0.toString(coefficientFormat);
//    }
//
//    for (int i = 1; i <= this.degree; i++) {
//      final DoubleNumber ci = getCoefficient(i);
//
//      if (ci.isZero(tolerance)) {
//        continue;
//      }
//
//      String sign;
//      if (i == this.degree) {
//        sign = ""; //$NON-NLS-1$
//      } else {
//        sign = " + "; //$NON-NLS-1$
//      }
//
//      String varString;
//      if (i == 1) {
//        varString = var;
//      } else {
//        varString = var + String.format("^%d", Integer.valueOf(i)); //$NON-NLS-1$
//      }
//
//      final String coefString = ci.toString(coefficientFormat);
//
//      str = (sign + coefString + mulString + varString) + str;
//    }
//
//    if (str.equals("")) { //$NON-NLS-1$
//      return "0"; //$NON-NLS-1$
//    }
//
//    if (1 < str.length() && str.charAt(1) == '+') {
//      return str.substring(3);
//    }
//
//    return str;
//  }

  /**
   * 空白を連結した文字列を生成します。
   * 
   * @param size 空白の個数
   * @return 連結した空白からなる文字列
   */
  private String createSpaceString(final int size) {
    final char[] space = new char[size];
    int i = size;
    while (i-- != 0) {
      space[i] = ' ';
    }
    return new String(space);
  }

  /**
   * {@inheritDoc}
   */
  public final void copy(final DoublePolynomial source) {
    if (!isSameClass(source)) {
      throw new IllegalArgumentException(Messages.getString("Polynomial.41")); //$NON-NLS-1$
    }

    if (!hasSameDegree(source)) {
      throw new IllegalArgumentException(Messages.getString("Polynomial.42")); //$NON-NLS-1$
    }

    this.coefficients.copy(source.coefficients);
  }

  /**
   * {@inheritDoc}
   */
  public void partCopy(final int toMin, final int toMax, final DoublePolynomial source, final int fromMin, final int fromMax) {
    final DoubleMatrix sourceCoefficients = source.coefficients.getSubVector(fromMin + 1, fromMax + 1);
    this.coefficients.setSubVector(toMin + 1, toMax + 1, sourceCoefficients);
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

    final int varlen;
    final byte[] b;
    if (this.variable != null) {
      varlen = this.variable.length() + 1;
      b = (this.variable + "\0").getBytes(Charset.forName("UTF-8")); //$NON-NLS-1$ //$NON-NLS-2$
    } else {
      varlen = 0;
      b = new byte[0];
    }

    output.writeInt(varlen);
    output.write(b, 0, b.length);

    writeMxFormatWithoutHeader(output, false);

    output.flush();
  }

  //  /**
  //   * データのみを出力ストリームに出力します。
  //   * 
  //   * @param output 出力ストリーム
  //   * @throws IOException ストリームに出力できない場合
  //   */
  //  public final void writeMxFormat(final OutputStream output) throws IOException {
  //    writeMxFormatWithoutHeader(output, false);
  //  }

//  /**
//   * {@inheritDoc}
//   */
  /**
   * データのみを出力ストリームに出力します。
   * 
   * @param output 出力ストリーム
   * @param asComplex 複素数として出力するならばtrue、そうでなければfalse
   * @throws IOException ストリームに出力できない場合
   */
  public final void writeMxFormatWithoutHeader(final OutputStream output, final boolean asComplex) throws IOException {
    final DataOutputStream ds = new DataOutputStream(new BufferedOutputStream(output));

    final DoubleMatrix data = this.coefficients;
    if (!asComplex) {
      for (int i = 0; i <= this.degree; i++) {
        ds.writeDouble(data.getDoubleElement(i + 1));
      }
    } else {
      for (int i = 0; i <= this.degree; i++) {
        ds.writeDouble(data.getDoubleElement(i + 1));
        ds.writeDouble(0.0);
      }
    }

    ds.flush();
  }

  /**
   * MX形式のデータをファイルから読込む。
   * 
   * @param file ファイル
   * @return 読込んだ多項式
   * @throws IOException ファイルから読込めない場合
   */
  public static DoublePolynomial readMxFormat(final File file) throws IOException {
    try (final DataInputStream ds = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
      final DoublePolynomial ans = readMxFormat(ds);
      ds.close();
      return ans;
    }
  }

  /**
   * MX形式のデータを入力ストリームから読込む。
   * 
   * @param input 入力ストリーム
   * @return 読込んだ多項式
   * @throws IOException 入力ストリームから読込めない場合
   */
  public static DoublePolynomial readMxFormat(final InputStream input) throws IOException {
    final MxDataHead head = new MxDataHead();
    head.read(input);
    return readMxFormat(head, input);
  }

  /**
   * MX形式のデータを入力ストリームから読込む。 ヘッダ情報は先に指定している。
   * 
   * @param input 入力ストリーム
   * @param head ヘッダ情報
   * @return 読込んだ多項式
   * @throws IOException 入力ストリームから読込めない場合
   */
  public static DoublePolynomial readMxFormat(final MxDataHead head, final InputStream input) throws IOException {
    final DataInputStream is = new DataInputStream(input);

    final int varlen;
    if (head.isSameEndian()) {
      varlen = is.readInt();
    } else {
      varlen = EndianTransformer.flip(is.readInt());
    }

    final String var;
    if (varlen == 0) {
      var = "s"; //$NON-NLS-1$
    } else {
      byte[] b = new byte[varlen];
      new DataInputStream(is).readFully(b);
      var = new String(b, 0, varlen - 1, Charset.forName("UTF-8")); //$NON-NLS-1$
    }

    final boolean realPolynomial = (head.getRealOrComplex() == 0);
    final int deg = head.getDegree();

    if (realPolynomial) {
      final double[] realCoef = new double[deg + 1];
      if (head.isSameEndian()) {
        for (int i = 0; i <= deg; i++) {
          realCoef[i] = is.readDouble();
        }
      } else {
        for (int i = 0; i <= deg; i++) {
          realCoef[i] = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
        }
      }
      final DoublePolynomial ret = new DoublePolynomial(realCoef, var);
      return ret;
    }

    throw new UnsupportedOperationException();

    //    final DoubleComplexNumber[] comlexCoef = new DoubleComplexNumber[deg + 1];
    //    if (head.isSameEndian()) {
    //      for (int i = 0; i <= deg; i++) {
    //        final double real = is.readDouble();
    //        final double imag = is.readDouble();
    //        comlexCoef[i] = new DoubleComplexNumber(real, imag);
    //      }
    //    } else {
    //      for (int i = 0; i <= deg; i++) {
    //        final double real = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
    //        final double imag = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
    //        comlexCoef[i] = new DoubleComplexNumber(real, imag);
    //      }
    //    }
    //    final DoublePolynomial ret = new DoublePolynomial(comlexCoef, var);
    //    return ret;
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMmFormat(final File file, final String name) throws IOException {
    try (Writer output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")))) { //$NON-NLS-1$
      writeMmFormat(output, name, true);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final void writeMmFormat(final Writer output, final String name, final boolean withStatementSeparator) throws IOException {
    final StringBuffer sb = new StringBuffer();

    if (name.length() != 0) {
      sb.append(name);
      sb.append(" = "); //$NON-NLS-1$
    }

    sb.append(toMmString());

    if (withStatementSeparator) {
      String newLine = System.getProperty("line.separator"); //$NON-NLS-1$
      sb.append(";"); //$NON-NLS-1$
      sb.append(newLine);
      sb.append(newLine);
    }

    output.write(sb.toString());
    output.flush();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean compare(final String operator, final DoublePolynomial opponent) {
    if (operator.equals(".!=")) { //$NON-NLS-1$
      return !equals(opponent);
    }

    if (operator.equals(".==")) { //$NON-NLS-1$
      return equals(opponent);
    }

    throw new IllegalArgumentException();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean compare(final String operator, final int opponent) {
    return compare(operator, (double)opponent);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean compare(final String operator, final double opponent) {
    if (operator.equals(".!=")) { //$NON-NLS-1$
      if (getDegree() != 0) {
        return true;
      }

      return !getCoefficient(0).equals(new DoubleNumber(opponent));
    }

    if (operator.equals(".==")) { //$NON-NLS-1$
      if (getDegree() != 0) {
        return false;
      }

      return getCoefficient(0).equals(new DoubleNumber(opponent));

    }

    throw new IllegalArgumentException();
  }

  //
  // GridElementの実装
  //

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial createZero() {
    return new DoublePolynomial(this.coefficients.getElement(1, 1).createZero());
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero() {
    if (!isConstant()) {
      return false;
    }

    if (this.coefficients.getElement(1, 1).isZero()) {
      return true;
    }

    return false;
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final boolean compare(final String operator, final DoublePolynomial opponent) {
//    if (operator.equals(".==")) { //$NON-NLS-1$
//      //      if (!(opponent instanceof Polynomial)) {
//      //        return false;
//      //      }
//      return equals(opponent);
//    }
//
//    if (operator.equals(".!=")) { //$NON-NLS-1$
//      return !equals(opponent);
//    }
//
//    throw new IllegalArgumentException(Messages.getString("Polynomial.50")); //$NON-NLS-1$
//  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial[] createArray(final int size) {
    return new DoublePolynomial[size];
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial[][] createArray(final int rowSize, final int columnSize) {
    return new DoublePolynomial[rowSize][columnSize];
  }

//  /**
//   * Creates an array.
//   * 
//   * @param elements elements.
//   * @return array
//   */
//  public final DoublePolynomial[] createArray(final DoublePolynomial[] elements) {
//    final int size = elements.length;
//
//    final DoublePolynomial[] array = new DoublePolynomial[size];
//    System.arraycopy(elements, 0, array, 0, size);
//    return array;
//  }
  
//  /**
//   * Creates an array.
//   * 
//   * @param elements elements
//   * @return array
//   */
//  public final DoublePolynomial[][] createArray(final DoublePolynomial[][] elements) {
//    final int rowSize = elements.length;
//    final int columnSize = rowSize == 0 ? 0 : elements[0].length;
//
//    final DoublePolynomial[][] array = new DoublePolynomial[rowSize][columnSize];
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
//  public final DoublePolynomialMatrix createGrid(final int rowSize, final int columnSize, final Scalar<?,?>[][] elements) {
//    return new DoublePolynomialMatrix(rowSize, columnSize, (DoublePolynomial[][])elements);
//  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomialMatrix createGrid(final int rowSize, final int columnSize, final DoublePolynomial[][] elements) {
    return new DoublePolynomialMatrix(rowSize, columnSize, elements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoublePolynomialMatrix createGrid(final Scalar<?,?>[] elements) {
//    return new DoublePolynomialMatrix((DoublePolynomial[])elements);
//  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomialMatrix createGrid(final DoublePolynomial[] elements) {
    return new DoublePolynomialMatrix(elements);
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial createUnit() {
    return new DoublePolynomial(this.coefficients.getElement(1, 1).createUnit());
  }

//  /**
//   * 数値との差を生成します。
//   * 
//   * @param value 数値
//   * @return 数値との差
//   */
//  public final DoublePolynomial subtract(final NumericalScalar<?,?> value) {
//    return (DoublePolynomial)super.subtract(value);
//  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial multiply(final DoubleNumber value) {
    return new DoublePolynomial(this.coefficients.multiply(value), this.variable);
  }

  /**
   * 数値との積を生成します。
   * 
   * @param value 数値
   * @return 数値との積
   */
  public final DoubleComplexPolynomial multiply(final DoubleComplexNumber value) {
    return new DoubleComplexPolynomial(new DoubleComplexMatrix(this.coefficients).multiply(value), this.variable);
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial divide(final DoubleNumber value) {
    return multiply(value.inverse());
  }

  /**
   * {@inheritDoc}
   */
  public final DoublePolynomial leftDivide(final DoubleNumber value) {
    return inverse().multiply(value);
  }

//  /**
//   * 数値との左からの商を生成します。
//   * 
//   * @param value 数値
//   * @return 数値との左からの商
//   */
//  public final DoubleRationalPolynomial leftDivide(final DoubleNumber value) {
//    return new DoubleRationalPolynomial(value, this.clone());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final DoublePolynomial transformFrom(final int value) {
//    return new DoublePolynomial(value, getVariable());
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final DoublePolynomial transformFrom(final double value) {
//    return new DoublePolynomial(value, getVariable());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public final DoublePolynomial transformFrom(final GridElement<? extends GridElement<?>> value) {
//    if (super.isTransformableFrom(value)) {
//      return super.transformFrom(value);
//    }
//
//    if (value instanceof DoubleNumber) {
//      return new DoublePolynomial(((DoubleNumber)value).doubleValue());
//    }
//
//    //    if (value instanceof DoubleComplexNumber) {
//    //      return new DoublePolynomial((DoubleComplexNumber)value, getVariable());
//    //    }
//
//    throw new IllegalArgumentException(Messages.getString("Polynomial.51")); //$NON-NLS-1$
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
//    return false;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getAddOperator() {
//    return DoublePolynomialAddOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getDivideOperator() {
//    return DoublePolynomialDivideOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getLeftDivideOperator() {
//    return DoublePolynomialLeftDivideOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getMultiplyOperator() {
//    return DoublePolynomialMultiplyOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getSubtractOperator() {
//    return DoublePolynomialSubtractOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarEqual getEqualOperator() {
//    return DoublePolynomialEqual.getInstance();
//  }

  /**
   * 成分が零である{@link DoublePolynomial}の2次元配列を返します。
   * 
   * @param rowSize 行の数
   * @param columnSize 行の数
   * @param variableName 変数の名前
   * @return 成分が零である{@link DoublePolynomial}の2次元配列を返します。
   */
  public static DoublePolynomial[][] createZeroArray(final int rowSize, final int columnSize, final String variableName) {
    final DoublePolynomial[][] elements = new DoublePolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        elements[i][j] = new DoublePolynomial(0, variableName);
      }
    }
    return elements;
  }

  /**
   * {@inheritDoc}
   */
  public final void setDisplayWidth(final int displayWidth) {
    this.displayWidth = displayWidth;
  }

  /**
   * {@inheritDoc}
   */
  public final int getDisplayWidth() {
    return this.displayWidth;
  }
  
  /**
   * 与えられた根をもつ多項式の因数分解の文字列を返します。
   * 
   * @param roots 根
   * @param variable 変数名
   * @param format 係数のフォーマット
   * @return 与えられた根をもつ多項式の因数分解の文字列
   */
  public static String getStringFactorization(final DoubleMatrix roots, final String variable, final String format) {
    return getStringFactorization(new DoubleComplexMatrix(roots), variable, format);
  }

  /**
   * 与えられた根をもつ多項式の因数分解の文字列を返します。
   * 
   * @param roots 根
   * @param variable 変数名
   * @param format 係数のフォーマット
   * @return 与えられた根をもつ多項式の因数分解の文字列
   */
  public static String getStringFactorization(final DoubleComplexMatrix roots, final String variable, final String format) {
    final BooleanMatrix isZeroRoots = roots.compareElementWise(".==", 0); //$NON-NLS-1$
    
    final IntMatrix foundedZeroRoots = isZeroRoots.find();
    final DoubleComplexMatrix zeroRoots;
    if (foundedZeroRoots.isEmpty()) {
      zeroRoots = new DoubleComplexMatrix(0,0);
    } else {
      zeroRoots = roots.getSubVector(foundedZeroRoots);
    }
             
    final IntMatrix foundedNonZeroRoots = isZeroRoots.notElementWise().find();
    final DoubleComplexMatrix nonZeroRoots;
    if (foundedNonZeroRoots.isEmpty()) {
      nonZeroRoots = new DoubleComplexMatrix(0,0);
    } else {
      nonZeroRoots = roots.getSubVector(foundedNonZeroRoots);
    }

    if (roots.length() == 0) {
      return "1"; //$NON-NLS-1$
    }

    final StringBuffer expression;

    if (zeroRoots.length() > 0) {
      expression = new StringBuffer(variable + (zeroRoots.length() == 1 ? "" : "^" + zeroRoots.length())); //$NON-NLS-1$ //$NON-NLS-2$
    } else {
      expression = new StringBuffer(""); //$NON-NLS-1$
    }

    for (int i = 1; i <= nonZeroRoots.length(); i++) {
      final DoubleComplexNumber root = nonZeroRoots.getElement(i);
      if (root.getImaginaryPart().isZero()) {
        final DoubleNumber realRoot = root.getRealPart();
        final String sign = realRoot.isGreaterThan(0) ? "-" : "+"; //$NON-NLS-1$ //$NON-NLS-2$
        expression.append("(" + variable + " " + sign + " " + realRoot.abs().toString(format) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      } else {
        expression.append("(" + variable + " - " + root.toString(format) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      }
    }

    return expression.toString();
  }

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
//      setImagPart(transformFrom(imagPart));
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("AbstractSymbolicScalar.5")); //$NON-NLS-1$
//  }

//  /**
//   * Generates a new Polynomial
//   * 
//   * @return Polynomial
//   */
//  public Polynomial<DoubleNumber,DoubleMatrix> toPolynomial() {
//    return new Polynomial<>(this.coefficients);
//  }
  
  /**
   * 値を加えた成分を生成します。
   * 
   * @param value 加える値
   * @return 足し算の結果
   */
  public DoubleComplexPolynomial add(DoubleComplexPolynomial value) {
    return toComplex().add(value);
  }

  /**
   * 値を引きます。
   * 
   * @param value 引く値
   * @return 引き算の結果
   */
  public DoubleComplexPolynomial subtract(DoubleComplexPolynomial value) {
    return toComplex().subtract(value);
  }

  /**
   * 値を掛けます。
   * 
   * @param value 掛ける値
   * @return 掛け算の結果
   */
  public DoubleComplexPolynomial multiply(DoubleComplexPolynomial value) {
    return toComplex().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomial toComplex() {
    return new DoubleComplexPolynomial(this);
  }

  /**
   * {@inheritDoc}
   */
  public DoublePolynomialMatrix createGrid(DoubleMatrix constants) {
    return new DoublePolynomialMatrix(constants);
  }

  /**
   * {@inheritDoc}
   */
  public DoublePolynomialMatrix createGrid(DoubleMatrix constants, String variableName) {
    return new DoublePolynomialMatrix(constants, variableName);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleRationalPolynomial toRational() {
    return new DoubleRationalPolynomial(this);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomial divide(DoubleComplexPolynomial value) {
    return toComplex().divide(value);
  }

  /**
   * {@inheritDoc}
   */
  public DoubleComplexPolynomial leftDivide(DoubleComplexPolynomial value) {
    return toComplex().leftDivide(value);
  }
}