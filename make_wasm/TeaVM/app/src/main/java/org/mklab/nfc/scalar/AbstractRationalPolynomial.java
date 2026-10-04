/**
 * $Id: RationalPolynomial.java,v 1.123 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.scalar;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.matrix.PolynomialMatrix;
import org.mklab.nfc.matrix.RationalPolynomialMatrix;
import org.mklab.nfc.util.PolynomialTokenizer;


/**
 * 有理多項式を表現するクラスです。
 * 
 * @author koga
 * @version $Revision: 1.123 $, 2008/02/16
 * @param <PS> 多項式の型
 * @param <PM> 多項式行列の型
 * @param <RS> 有理多項式の型
 * @param <RM> 有理多項式行列の型
 * @param <ES> 係数スカラーの型
 * @param <EM> 係数行列の型
 */
public abstract class AbstractRationalPolynomial<PS extends Polynomial<PS, PM,RS, RM,ES, EM>, PM extends PolynomialMatrix<PS,PM,RS, RM,ES,EM>, RS extends RationalPolynomial<PS,PM,RS, RM, ES, EM>, RM extends RationalPolynomialMatrix<PS,PM,RS,RM,ES,EM>, ES extends NumericalScalar<ES, EM>, EM extends NumericalMatrix<ES, EM>>   extends AbstractSymbolicScalar<RS, RM, ES, EM> implements RationalPolynomial<PS, PM,RS, RM,ES, EM> {

  /** シリアルバージョン。 */
  private static final long serialVersionUID = 6537697917259508800L;

  /** 分子多項式。 */
  private PS numerator;

  /** 分母多項式。 */
  private PS denominator;

  /** 出力の幅。 */
  private int displayWidth = 1000;

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子定数(実数)
   * @param denominator 分母多項式
   */
  public AbstractRationalPolynomial(final double numerator, final PS denominator) {
    this(denominator.create(numerator, denominator.getVariable()), denominator);
  }

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子定数(スカラー)
   * @param denominator 分母多項式
   */
  public AbstractRationalPolynomial(final ES numerator, final PS denominator) {
    this.numerator = denominator.create(numerator, denominator.getVariable());
    this.denominator = denominator;
  }

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子多項式
   * @param denominator 分母多項式
   */
  public AbstractRationalPolynomial(final PS numerator, final PS denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }

  /**
   * Sets numerator.
   * 
   * @param numerator numerator
   */
  protected void setNumerator(PS numerator) {
    this.numerator = numerator;
  }

  /**
   * Sets denominator.
   * 
   * @param denominator denominator
   */
  protected void setDenominator(PS denominator) {
    this.denominator = denominator;
  }

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子多項式
   * @param denominator 分母定数(実数)
   */
  public AbstractRationalPolynomial(final PS numerator, final double denominator) {
    this(numerator, numerator.create(denominator, numerator.getVariable()));
  }

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子多項式
   * @param denominator 分母定数(スカラー)
   */
  public AbstractRationalPolynomial(final PS numerator, final ES denominator) {
    this.numerator = numerator;
    this.denominator = numerator.create(denominator, numerator.getVariable());
  }

  /**
   * 新しく生成された<code>RationalPolynomial</code>オブジェクトを初期化します。
   * 
   * @param numerator 分子多項式
   */
  public AbstractRationalPolynomial(final PS numerator) {
    this.numerator = numerator;
    this.denominator = numerator.create(1, numerator.getVariable());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RS clone() {
    final AbstractRationalPolynomial<PS,PM,RS, RM, ES, EM> ans = (AbstractRationalPolynomial<PS,PM,RS, RM, ES, EM>)super.clone();
    ans.numerator = this.numerator.clone();
    ans.denominator = this.denominator.clone();
    ans.setDisplayWidth(this.displayWidth);
    return (RS)ans;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    final String[] str = toString(false);
    return "(" + str[0] + ") / (" + str[1] + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

  /**
   * {@inheritDoc}
   */
  public String toString(final String valueFormat) {
    final String[] str = toString(false, valueFormat);
    return "(" + str[0] + ") / (" + str[1] + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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

    return equals((RS)opponent, 0);
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
    hashCode = prime * hashCode + (this.numerator == null ? 0 : this.numerator.hashCode());
    hashCode = prime * hashCode + (this.denominator == null ? 0 : this.denominator.hashCode());
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  public boolean equals(final RS opponent, final double tolerance) {
    final ES denLeadingCoef1 = this.denominator.getCoefficient(this.denominator.getDegree());
    final ES denLeadingCoef2 = opponent.getDenominator().getCoefficient(opponent.getDenominator().getDegree());

    if (denLeadingCoef1.equals(denLeadingCoef2)) {
      final boolean isNumEqual = this.numerator.equals(opponent.getNumerator(), tolerance);
      final boolean isDenEqual = this.denominator.equals(opponent.getDenominator(), tolerance);
      return isNumEqual && isDenEqual;
    }

    final ES scale1;
    final ES scale2;

    if (denLeadingCoef1.isZero() == false) {
      scale1 = denLeadingCoef1;
    } else {
      scale1 = this.numerator.getCoefficient(this.numerator.getDegree());
    }
    if (denLeadingCoef2.isZero() == false) {
      scale2 = denLeadingCoef2;
    } else {
      scale2 = opponent.getNumerator().getCoefficient(opponent.getNumerator().getDegree());
    }

    final boolean isNumEqual = this.numerator.divide(scale1).equals(opponent.getNumerator().divide(scale2), tolerance);
    final boolean isDenEqual = this.denominator.divide(scale1).equals(opponent.getDenominator().divide(scale2), tolerance);
    return isNumEqual && isDenEqual;
  }

  /**
   * {@inheritDoc}
   */
  public boolean equals(final RS opponent, final ES tolerance) {
    final ES denLeadingCoef1 = this.denominator.getCoefficient(this.denominator.getDegree());
    final ES denLeadingCoef2 = opponent.getDenominator().getCoefficient(opponent.getDenominator().getDegree());

    if (denLeadingCoef1.equals(denLeadingCoef2)) {
      final boolean isNumEqual = this.numerator.equals(opponent.getNumerator(), tolerance);
      final boolean isDenEqual = this.denominator.equals(opponent.getDenominator(), tolerance);
      return isNumEqual && isDenEqual;
    }

    final ES scale1;
    final ES scale2;
    if (denLeadingCoef1.isZero() == false) {
      scale1 = denLeadingCoef1;
    } else {
      scale1 = this.numerator.getCoefficient(this.numerator.getDegree());
    }

    if (denLeadingCoef2.isZero() == false) {
      scale2 = denLeadingCoef2;
    } else {
      scale2 = opponent.getNumerator().getCoefficient(opponent.getNumerator().getDegree());
    }

    final boolean isNumEqual = this.numerator.divide(scale1).equals(opponent.getNumerator().divide(scale2), tolerance);
    final boolean isDenEqual = this.denominator.divide(scale1).equals(opponent.getDenominator().divide(scale2), tolerance);
    return isNumEqual && isDenEqual;
  }

  /**
   * {@inheritDoc}
   */
  public int getNumeratorDegree() {
    return this.numerator.getDegree();
  }

  /**
   * {@inheritDoc}
   */
  public int getDenominatorDegree() {
    return this.denominator.getDegree();
  }

  /**
   * {@inheritDoc}
   */
  public PS getNumerator() {
    return this.numerator.clone();
  }

  /**
   * {@inheritDoc}
   */
  public PS getDenominator() {
    return this.denominator.clone();
  }

  /**
   * {@inheritDoc}
   */
  public String getVariable() {
    return this.numerator.getVariable();
  }

  /**
   * {@inheritDoc}
   */
  public void setVariable(final String variableName) {
    this.numerator.setVariable(variableName);
    this.denominator.setVariable(variableName);
  }

  /**
   * {@inheritDoc}
   */
  public RS add(final RS value) {
    if (this.isZero() && value.isZero()) {
      return createZero();
    }
    if (this.isZero()) {
      return value.clone();
    }
    if (value.isZero()) {
      return this.clone();
    }

    if (this.denominator.equals(value.getDenominator())) { // 分母が一致する場合。
      return create(this.numerator.add(value.getNumerator()), this.denominator);
    }

    final PS tmp1 = this.numerator.multiply(value.getDenominator());
    final PS tmp2 = value.getNumerator().multiply(this.denominator);
    final PS pn = tmp1.add(tmp2);
    final PS pd = this.denominator.multiply(value.getDenominator());
    return create(pn, pd);
  }

  /**
   * {@inheritDoc}
   */
  public RS add(final PS value) {
    if (isZero() && value.isZero()) {
      return createZero();
    }
    if (isZero()) {
      return create(value);
    }
    if (value.isZero()) {
      return this.clone();
    }

    return create(this.numerator.add(value.multiply(this.denominator)), this.denominator);
  }

  /**
   * {@inheritDoc}
   */
  public RS add(final double value) {
    if (isZero() && value == 0.0) {
      return create(0.0);
    }
    if (isZero()) {
      return create(value);
    }
    if (value == 0.0) {
      return this.clone();
    }

    return create(this.numerator.add(this.denominator.multiply(value)), this.denominator);
  }

  /**
   * {@inheritDoc}
   */
  public RS add(final int value) {
    if (isZero() && value == 0) {
      return create(0);
    }
    if (isZero()) {
      return create(value);
    }
    if (value == 0) {
      return this.clone();
    }

    return create(this.numerator.add(this.denominator.multiply(value)), this.denominator);
  }

  /**
   * {@inheritDoc}
   */
  public RS subtract(final RS value) {
    if (this.isZero() && value.isZero()) {
      return create(0.0);
    }
    if (this.isZero()) {
      return value.unaryMinus();
    }
    if (value.isZero()) {
      return this.clone();
    }

    if (this.denominator.equals(value.getDenominator())) { // 分母が一致する場合。
      return create(this.numerator.subtract(value.getNumerator()), this.denominator);
    }

    final PS tmp1 = this.numerator.multiply(value.getDenominator());
    final PS tmp2 = value.getNumerator().multiply(this.denominator);
    final PS pn = tmp1.subtract(tmp2);
    final PS pd = this.denominator.multiply(value.getDenominator());
    return create(pn, pd);
  }

  /**
   * {@inheritDoc}
   */
  public RS subtract(final PS value) {
    if (isZero() && value.isZero()) {
      return create(0.0);
    }
    if (isZero()) {
      return create(value.unaryMinus());
    }
    if (value.isZero()) {
      return this.clone();
    }

    return create(this.numerator.subtract(value.multiply(this.denominator)), this.denominator);
  }

  /**
   * {@inheritDoc}
   */
  public RS subtract(final double value) {
    if (isZero() && value == 0.0) {
      return create(0.0);
    }
    if (isZero()) {
      return create(-value);
    }
    if (value == 0.0) {
      return this.clone();
    }

    return create(this.numerator.subtract(this.denominator.multiply(value)), this.denominator);
  }

  /**
   * {@inheritDoc}
   */
  public RS subtract(final int value) {
    if (isZero() && value == 0) {
      return create(0);
    }
    if (isZero()) {
      return create(-value);
    }
    if (value == 0) {
      return this.clone();
    }

    return create(this.numerator.subtract(this.denominator.multiply(value)), this.denominator);
  }

  /**
   * {@inheritDoc}
   */
  public RS inverse() {
    return create(this.denominator.clone(), this.numerator.clone());
  }

  /**
   * {@inheritDoc}
   */
  public RS multiply(final RS value) {
    if (isZero() || value.isZero()) {
      return create(0.0);
    }

    return create(this.numerator.multiply(value.getNumerator()), this.denominator.multiply(value.getDenominator()));
  }

  /**
   * {@inheritDoc}
   */
  public RS multiply(final double value) {
    return create(this.numerator.multiply(value), this.denominator.clone());
  }

  /**
   * {@inheritDoc}
   */
  public RS multiply(final int value) {
    return create(this.numerator.multiply(value), this.denominator.clone());
  }

  /**
   * {@inheritDoc}
   */
  public RS multiply(final PS value) {
    return create(this.numerator.multiply(value), this.denominator.clone());
  }

  /**
   * {@inheritDoc}
   */
  public RS divide(final double value) {
    return create(this.numerator.divide(value), this.denominator.clone());
  }

  /**
   * {@inheritDoc}
   */
  public RS divide(final int value) {
    return create(this.numerator.divide(value), this.denominator.clone());
  }

  /**
   * {@inheritDoc}
   */
  public RS divide(final PS value) {
    return create(this.numerator.clone(), this.denominator.multiply(value));
  }

//  /**
//   * {@inheritDoc}
//   */
//  public S divide(final DoublePolynomial value) {
//    return divide(create(value, value.create(1)));
//  }

  /**
   * {@inheritDoc}
   */
  public RS divide(final RS value) {
    if (isZero()) {
      return create(0);
    }

    final PS ansNumerator = this.numerator.multiply(value.getDenominator());
    final PS ansDenominator = this.denominator.multiply(value.getNumerator());
    return create(ansNumerator, ansDenominator);
  }

  /**
   * {@inheritDoc}
   */
  public RS leftDivide(final double value) {
    return create(this.denominator.multiply(value), this.numerator.clone());
  }

  /**
   * {@inheritDoc}
   */
  public RS leftDivide(final int value) {
    return create(this.denominator.multiply(value), this.numerator.clone());
  }

  /**
   * {@inheritDoc}
   */
  public RS leftDivide(final PS value) {
    return create(this.denominator.multiply(value), this.numerator.clone());
  }

  /**
   * {@inheritDoc}
   */
  public RS leftDivide(final RS value) {
    if (value.isZero()) {
      return create(0.0);
    }

    final PS ansNumerator = this.denominator.multiply(value.getNumerator());
    final PS ansDenominator = this.numerator.multiply(value.getDenominator());
    return create(ansNumerator, ansDenominator);
  }

  /**
   * {@inheritDoc}
   */
  public RS power(final int m) {
    if (m < 0) {
      return inverse().power(-m);
      //throw new IllegalArgumentException(Messages.getString("RationalPolynomial.6")); //$NON-NLS-1$
    }
    if (m == 0) {
      return create(1);
    }
    if (m == 1) {
      return this.clone();
    }

    int n = m;
    RS aa = this.clone();
    RS b2 = create(1);
    RS b;

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
  public RS unaryMinus() {
    return create(this.numerator.unaryMinus(), this.denominator.clone());
  }

  /**
   * {@inheritDoc}
   */
  public RS conjugate() {
    return create(this.numerator.conjugate(), this.denominator.conjugate());
  }

  /**
   * {@inheritDoc}
   */
  public RS roundToZero(final double tolerance) {
    return create(this.numerator.roundToZero(tolerance), this.denominator.roundToZero(tolerance));
  }

  /**
   * {@inheritDoc}
   */
  public RS roundToZero(final ES tolerance) {
    return create(this.numerator.roundToZero(tolerance), this.denominator.roundToZero(tolerance));
  }

  /**
   * {@inheritDoc}
   */
  public RS round() {
    return create(this.numerator.round(), this.denominator.round());
  }

  /**
   * {@inheritDoc}
   */
  public RS ceil() {
    return create(this.numerator.ceil(), this.denominator.ceil());
  }

  /**
   * {@inheritDoc}
   */
  public RS floor() {
    return create(this.numerator.floor(), this.denominator.floor());
  }

  /**
   * {@inheritDoc}
   */
  public RS fix() {
    return create(this.numerator.fix(), this.denominator.fix());
  }

  /**
   * {@inheritDoc}
   */
  public ES evaluate(final int value) {
    final ES neval = this.numerator.evaluate(value);
    final ES deval = this.denominator.evaluate(value);
    return neval.divide(deval);
  }

  /**
   * {@inheritDoc}
   */
  public ES evaluate(final double value) {
    final ES neval = this.numerator.evaluate(value);
    final ES deval = this.denominator.evaluate(value);
    return neval.divide(deval);
  }

  /**
   * {@inheritDoc}
   */
  public ES evaluate(final ES value) {
    return this.numerator.evaluate(value).divide(this.denominator.evaluate(value));
  }

  /**
   * {@inheritDoc}
   */
  public RS evaluate(final PS value) {
    return create(this.numerator.evaluate(value), this.denominator.evaluate(value));
  }

  /**
   * {@inheritDoc}
   */
  public RS evaluate(final RS value) {
    RS num = evaluatePolynomialWithRationalPolynomila(this.numerator, value);
    RS den= evaluatePolynomialWithRationalPolynomila(this.denominator, value);
    return num.divide(den);
  }
  
  /**
   * Evaluates polynomial with rational polynomial.
   * 
   * @param polynomial polynomial
   * @param value rational polynomial
   * @return evaluated result
   */
  public RS evaluatePolynomialWithRationalPolynomila(PS polynomial, RS value) {
    RS variable = value;
    RS sum = create(polynomial.getCoefficient(0));
    for (int i = 1; i <= polynomial.getDegree(); i++) {
      sum = sum.add(variable.multiply(polynomial.getCoefficient(i)));
      variable = variable.multiply(value);
    }
    
    return sum;
  }

  /**
   * {@inheritDoc}
   */
  public EM evaluate(final EM value) {
    final EM num = this.numerator.evaluate(value);
    final EM den = this.denominator.evaluate(value);
    return num.divide(den);
  }

  /**
   * {@inheritDoc}
   */
  public EM evaluateElementWise(final EM value) {
    final EM num = this.numerator.evaluateElementWise(value);
    final EM den = this.denominator.evaluateElementWise(value);
    return num.divideElementWise(den);
  }

///**
//* {@inheritDoc}
//*/
//public NumericalMatrix<?, ?> getZeros() {
// return this.numerator.getRoots();
//}
//
///**
//* {@inheritDoc}
//*/
//public NumericalMatrix<?, ?> getPoles() {
// return this.denominator.getRoots();
//}

  /**
   * {@inheritDoc}
   */
  public RS derivative(final int order) {
    PS num = this.numerator;
    final PS dden = this.denominator.derivative(1);

    for (int i = 1; i <= order; i++) {
      final PS tmp1 = num.derivative(1);
      final PS tmp3 = tmp1.multiply(this.denominator);
      final PS tmp2 = num.multiply(i);
      final PS tmp4 = tmp2.multiply(dden);
      num = tmp3.subtract(tmp4);
    }

    final PS den = this.denominator.power(order + 1);

    return create(num, den);
  }

  /**
   * {@inheritDoc}
   */
  public RS shiftLower(final int count) {
    final PS num = this.numerator.shiftLower(count);
    PS den = this.denominator.shiftLower(count);

    if (den.isZero()) {
      den = num.createUnit();
    }
    return create(num, den);
  }

  /**
   * {@inheritDoc}
   */
  public RS shiftHigher(final int count) {
    final PS num = this.numerator.shiftHigher(count);
    final PS den = this.denominator.shiftHigher(count);
    return create(num, den);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isReal() {
    return this.numerator.isReal() && this.denominator.isReal();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isComplex() {
    return this.numerator.isComplex() || this.denominator.isComplex();
  }

  /**
   * {@inheritDoc}
   */
  public PS getQuotient() {
    final int nDegree = this.numerator.getDegree();
    final int dDegree = this.denominator.getDegree();

    if (nDegree < dDegree) {
      return this.numerator.createZero();
    }

    if (dDegree == 0) {
      return this.numerator.divide(this.denominator.getCoefficient(0));
    }

    return getQuotientAndRemainder()[0];
  }

  /**
   * {@inheritDoc}
   */
  public PS getRemainder() {
    final int nDegree = this.numerator.getDegree();
    final int dDegree = this.denominator.getDegree();

    if (nDegree < dDegree) {
      return this.numerator.clone();
    }

    if (dDegree == 0) {
      return this.numerator.createZero();
    }

    return getQuotientAndRemainder()[1];
  }

  /**
   * 分母多項式による分子多項式の除算の商多項式と剰余(余り)多項式を返します。
   * 
   * @return 分母多項式による分子多項式の除算の商多項式と剰余(余り)多項式 ニューメリカルレシピ・イン・シー (p.154)
   */
  private PS[] getQuotientAndRemainder() {
    final int nDegree = this.numerator.getDegree();
    final int dDegree = this.denominator.getDegree();

    final ES element = this.numerator.getCoefficient(0);
    final ES[] r = element.createArray(nDegree + 1);
    final ES[] q = element.createArray(nDegree + 1);

    for (int i = 0; i <= nDegree; i++) {
      r[i] = this.numerator.getCoefficient(i);
      q[i] = element.createZero();
    }

    for (int i = nDegree - dDegree; i >= 0; i--) {
      q[i] = r[dDegree + i].divide(this.denominator.getCoefficient(dDegree));
      for (int j = dDegree + i - 1; j >= i; j--) {
        r[j] = r[j].subtract(q[i].multiply(this.denominator.getCoefficient(j - i)));
      }
    }

    for (int i = dDegree; i <= nDegree; i++) {
      r[i] = element.createZero();
    }

    final PS qut = this.numerator.create(q);
    final PS rem = this.numerator.create(r);
    qut.simplify(q[0].getMachineEpsilon());
    rem.simplify(r[0].getMachineEpsilon());

    final PS[] ans = qut.createArray(2);
    ans[0] = qut;
    ans[1] = rem;
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public void print() {
    print("ans"); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public void print(final String name) {
    try {
      PrintStream output = new PrintStream(System.out, false, "UTF-8"); //$NON-NLS-1$
      print(name, output);
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException(name, e);
    }
  }

  /**
   * 表示文字列を返します。
   * 
   * @param name 名前
   * @return 表示文字列
   */
  public String getPrintingString(final String name) {
    return getPrintingString(name, getFormat());
  }

  /**
   * {@inheritDoc}
   */
  public String getPrintingString(final String name, final String coefficientFormat) {
    final ByteArrayOutputStream stream = new ByteArrayOutputStream();

    try (final PrintStream output = new PrintStream(stream, false, "UTF-8")) { //$NON-NLS-1$
      print(name, output, coefficientFormat);
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException(name, e);
    }

    try {
      return stream.toString("UTF-8"); //$NON-NLS-1$
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException(name, e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void print(final String name, final PrintStream output) {
    print(name, output, getFormat());
  }

  /**
   * {@inheritDoc}
   */
  public void print(final String name, final PrintStream output, final String coefficientFormat) {
    final int width = this.displayWidth - 1;
    final String[] str = toString(false, coefficientFormat);
    String strn = str[0];
    String strd = str[1];
    final int length1 = strn.length();
    final int length2 = strd.length();
    int length = (length1 > length2) ? length1 : length2;
    final int offset = name.length() == 0 ? 1 : name.length() + 4;

    final PolynomialTokenizer ptn = new PolynomialTokenizer(strn);
    final PolynomialTokenizer ptd = new PolynomialTokenizer(strd);
    strn = ptn.nextLine(width - offset);
    strd = ptd.nextLine(width - offset);

    if (offset + length + 1 > width) {
      length = width - offset;// - 9;
    }

    final String marginSpace = getSpace(offset);
    /* Numerator */
    writeSpace(offset, output);
    if (length1 < length) {
      writeSpace((length - length1) / 2, output);
    }

    output.println(strn);
    while (ptn.hasMoreTokens()) {
      output.println(marginSpace + ptn.nextLine(width - offset));
    }

    /* Line */
    if (0 < name.length()) {
      output.print(name + " = "); //$NON-NLS-1$
    }
    writeLine(length + 2, output);

    /* Denominator */
    output.println(""); //$NON-NLS-1$
    writeSpace(offset, output);
    if (length2 < length) {
      writeSpace((length - length2) / 2, output);
    }
    output.println(strd);
    while (ptd.hasMoreTokens()) {
      output.println(marginSpace + ptd.nextLine(width - offset));
    }
  }

  /**
   * {@inheritDoc}
   */
  public String[] toString(final boolean saving) {
    return new String[] {this.numerator.toString(saving), this.denominator.toString(saving)};
  }

  /**
   * {@inheritDoc}
   */
  public String[] toString(final boolean saving, final String coefficientFormat) {
    return new String[] {this.numerator.toString(saving, coefficientFormat), this.denominator.toString(saving, coefficientFormat)};
  }

  /**
   * 空白を標準出力に出力(表示)します。
   * 
   * @param count 空白の数
   * @param output プリントストリーム
   */
  private void writeSpace(final int count, final PrintStream output) {
    final char[] space = new char[count];
    int i = count;
    while (i-- != 0) {
      space[i] = ' ';
    }
    output.print(space);
  }

  /**
   * 指定された個数の空白文字を含む文字列を生成します。
   * 
   * @param count 空白文字の数
   * @return 生成された文字列
   */
  private String getSpace(final int count) {
    final char[] space = new char[count];
    int i = count;
    while (i-- != 0) {
      space[i] = ' ';
    }
    return new String(space);
  }

  /**
   * 分子多項式と分母多項式を分ける線を描くためのマイナス記号を標準出力に出力(表示)します。
   * 
   * @param count マイナス記号の数
   * @param output プリントストリーム
   */
  private void writeLine(final int count, final PrintStream output) {
    final char[] space = new char[count];
    for (int i = 0; i < count; i++) {
      space[i] = '-';
    }
    output.print(space);
  }

  /**
   * {@inheritDoc}
   */
  public boolean compare(final String operator, final RS opponent) {
    if (operator.equals(".!=")) { //$NON-NLS-1$
      return !equals(opponent, 0);
    }
    if (operator.equals(".==")) { //$NON-NLS-1$
      return equals(opponent, 0);
    }

    throw new IllegalArgumentException();
  }

  //
  // GridElementの実装
  //

  /**
   * {@inheritDoc}
   */
  public boolean isZero() {
    return this.numerator.isZero();
  }

  //
  // MatrixElementの実装
  //

  /**
   * {@inheritDoc}
   */
  public boolean isFinite() {
    return this.numerator.isFinite() && this.denominator.isFinite();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isInfinite() {
    return this.numerator.isInfinite() || this.denominator.isInfinite();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isNaN() {
    return this.numerator.isNaN() || this.denominator.isNaN();
  }

  /**
   * {@inheritDoc}
   */
  public boolean compare(final String operator, final double opponent) {
    if (operator.equals(".!=")) { //$NON-NLS-1$
      if (getNumeratorDegree() != 0 || getDenominatorDegree() != 0) {
        return true;
      }

      final ES value = this.numerator.getCoefficient(0).divide(this.denominator.getCoefficient(0));
      return !value.equals(new DoubleNumber(opponent));
    } else if (operator.equals(".==")) { //$NON-NLS-1$
      if (getNumeratorDegree() != 0 || getDenominatorDegree() != 0) {
        return false;
      }

      final ES value = this.numerator.getCoefficient(0).divide(this.denominator.getCoefficient(0));
      return value.equals(new DoubleNumber(opponent));
    }

    throw new IllegalArgumentException();
  }

  /**
   * {@inheritDoc}
   */
  public boolean compare(final String operator, final int opponent) {
    return compare(operator, (double)opponent);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isZero(final double tolerance) {
    return this.numerator.isZero(tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isZero(final ES tolerance) {
    return this.numerator.isZero(tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isUnit() {
    return this.numerator.isUnit() && this.denominator.isUnit();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isUnit(final double tolerance) {
    return this.numerator.isUnit(tolerance) && this.denominator.isUnit(tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isUnit(final ES tolerance) {
    return this.numerator.isUnit(tolerance) && this.denominator.isUnit(tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public RS add(ES value) {
    return create(this.numerator.add(this.denominator.multiply(value)), this.denominator.clone());
  }

  /**
   * {@inheritDoc}
   */
  public RS subtract(ES value) {
    return create(this.numerator.subtract(this.denominator.multiply(value)), this.denominator.clone());
  }

  /**
   * 数値を乗じた有理多項式を生成します。
   * 
   * @param value 乗じられる数値
   * @return 掛け算の結果
   */
  public RS multiply(final ES value) {
    return create(this.numerator.multiply(value), this.denominator.clone());
  }

  /**
   * {@inheritDoc}
   */
  public RS divide(final ES value) {
    return create(this.numerator.divide(value), this.denominator.clone());
  }
  
  /**
   *  Divide from left by a numerical scalar.
   * @param value numerical scalar
   * @return result of division
   */
  public RS leftDivide(final ES value) {
    return create(this.denominator.multiply(value), this.numerator.clone());
  }

//  /**
//   * {@inheritDoc}
//   */
//  public S transformFrom(final int value) {
//    return create(value, getVariable());
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public S transformFrom(final double value) {
//    return create(value, getVariable());
//  }

  /**
   * {@inheritDoc}
   */
  public void setDisplayWidth(final int displayWidth) {
    this.displayWidth = displayWidth;
  }

  /**
   * {@inheritDoc}
   */
  public int getDisplayWidth() {
    return this.displayWidth;
  }
}