/**
 * $Id: Polynomial.java,v 1.172 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.scalar;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.mklab.nfc.matrix.BooleanMatrix;
import org.mklab.nfc.matrix.MatrixSizeException;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.matrix.PolynomialMatrix;
import org.mklab.nfc.matrix.RationalPolynomialMatrix;
import org.mklab.nfc.util.PolynomialTokenizer;


/**
 * 多項式を表すクラスです。
 * 
 * @author matsuki
 * @version $Revision: 1.172 $
 * @param <PS> 多項式の型
 * @param <PM> 多項式行列の型
 * @param <RS> 有理多項式の型
 * @param <RM> 有理多項式行列の型
 * @param <ES> 係数スカラーの型
 * @param <EM> 係数行列の型
 */
public abstract class AbstractPolynomial<PS extends Polynomial<PS, PM,RS, RM,ES, EM>, PM extends PolynomialMatrix<PS,PM,RS, RM,ES,EM>, RS extends RationalPolynomial<PS,PM,RS, RM, ES, EM>, RM extends RationalPolynomialMatrix<PS,PM,RS,RM,ES,EM>, ES extends NumericalScalar<ES, EM>, EM extends NumericalMatrix<ES, EM>>  extends AbstractSymbolicScalar<PS, PM, ES, EM> implements Polynomial<PS, PM, RS, RM, ES, EM> {

  /** シリアルバージョン 。 */
  private static final long serialVersionUID = -8394079094815670802L;

  /** 多項式の係数。 */
  private EM coefficients;

  /** 多項式の次数。 */
  private int degree;

  /** 多項式変数。 */
  private String variable;

  /** 出力の幅。 */
  private int displayWidth = 1000;

  /**
   * <code>constant</code>を係数とする0次の多項式を生成します。
   * 
   * <p>多項式変数はデフォルトの"<i>s</i>"です。
   * 
   * @param constant 0次の係数
   */
  public AbstractPolynomial(final ES constant) {
    this(constant, (String)null);
  }

  /**
   * <code>constant</code>を係数とする0次の多項式を生成します。
   * 
   * @param constant 0次の係数
   * @param variableName 多項式変数
   */
  public AbstractPolynomial(final ES constant, final String variableName) {
    final ES[] coefs = constant.createArray(1);
    coefs[0] = constant;

    this.degree = 0;
    this.coefficients = constant.createGrid(coefs);
    this.variable = variableName;
  }

  /**
   * <code>coefficients</code>を係数とする多項式を生成します。
   * 
   * <p>多項式変数はデフォルトの"<i>s</i>"です。
   * 
   * @param coefficients 係数の配列
   */
  public AbstractPolynomial(final ES[] coefficients) {
    this(coefficients, (String)null);
  }

  /**
   * <code>coefficients</code>を係数とする多項式を生成します。
   * 
   * @param coefficients 係数の配列
   * @param variableName 多項式変数
   */
  public AbstractPolynomial(final ES[] coefficients, final String variableName) {
    this.degree = coefficients.length - 1;
    this.coefficients = coefficients[0].createGrid(coefficients);
    this.variable = variableName;
  }

  /**
   * 新しく生成された<code>Polynomial</code>オブジェクトを初期化します。
   * 
   * @param coefficientVector 係数をもつベクトル(行列)
   */
  public AbstractPolynomial(final EM coefficientVector) {
    this(coefficientVector, (String)null);
  }

  /**
   * 新しく生成された<code>Polynomial</code>オブジェクトを初期化します。
   * 
   * @param coefficientVector 係数をもつベクトル(行列)
   * @param variableName 多項式変数
   */
  public AbstractPolynomial(final EM coefficientVector, final String variableName) {
    setCoefficients(coefficientVector);
    this.variable = variableName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PS clone() {
    final PS ans = super.clone();
    if (ans != null) {
      ans.setVariable(this.variable);
    }
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

    return equals((PS)opponent, 0);
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

  /**
   * {@inheritDoc}
   */
  public final boolean equals(final PS opponent, final ES tolerance) {
    if (hasSameVariable(opponent) == false) {
      return false;
    }

    return this.coefficients.equals(opponent.getCoefficients(), tolerance);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean equals(final PS opponent, final double tolerance) {
    if (hasSameVariable(opponent) == false) {
      return false;
    }

    return this.coefficients.equals(opponent.getCoefficients(), tolerance);
  }
  
  /**
   * 同じクラスの指定された次数の多項式を生成します。
   * 
   * @param newDegree 次数
   * @return 多項式
   */
  private PS createSameClassPolynomial(final int newDegree) {
    return create(this.coefficients.createZero(1, newDegree + 1), this.variable);
  }

  /**
   * {@inheritDoc}
   */
  public final int getDegree() {
    return this.degree;
  }

  /**
   * Sets degree of polynomial.
   * 
   * @param degree degree
   */
  protected void setDegree(int degree) {
    this.degree = degree;
  }

  /**
   * {@inheritDoc}
   */
  public final EM getCoefficients() {
    return this.coefficients;
  }

  /**
   * 多項式の係数を成分とするベクトルを設定します。
   * 
   * @param coefficientsVector 多項式の係数を成分とするベクトル
   */
  protected void setCoefficients(EM coefficientsVector) {
    if (coefficientsVector.getRowSize() == 0 && coefficientsVector.getColumnSize() == 0) {
      setDegree(coefficientsVector.getRowSize() - 1);
      this.coefficients = coefficientsVector.createClone();
    } else if (coefficientsVector.getRowSize() != 1) {
      if (coefficientsVector.getColumnSize() != 1) {
        throw new MatrixSizeException(MatrixSizeException.NOT_A_VECTOR_MATRIX);
      }
      setDegree(coefficientsVector.getRowSize() - 1);
      this.coefficients = coefficientsVector.transpose();
    } else {
      setDegree(coefficientsVector.getColumnSize() - 1);
      this.coefficients = coefficientsVector.createClone();
    }
  }

  /**
   * {@inheritDoc}
   */
  public final ES getCoefficient(final int order) {
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
  public final void setCoefficient(final int order, final ES value) {
    this.coefficients.setElement(1, order + 1, value);
  }

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
  public final PS add(final PS value) {
    checkVariable(value);

    if (hasLargerDgree(value)) {
      return this.add(value.expand(this.degree));
    } else if (hasSmallerDegree(value)) {
      return this.expand(value.getDegree()).add(value);
    }

    final EM ansCoef = this.coefficients.add(value.getCoefficients());
    final String var = (this.variable != null ? this.variable : value.getVariable());
    final PS ans = create(ansCoef, var);
    ans.simplify();
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final PS add(final double value) {
    final EM ansCoef = this.coefficients.createClone();
    ansCoef.setElement(1, 1, ansCoef.getElement(1, 1).add(value));
    final PS ans = create(ansCoef, this.variable);
    ans.simplify();
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final PS add(final int value) {
    final EM ansCoef = this.coefficients.createClone();
    ansCoef.setElement(1, 1, ansCoef.getElement(1, 1).add(value));
    final PS ans = create(ansCoef, this.variable);
    ans.simplify();
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final PS add(final ES value) {
    return add(create(value));
  }

  /**
   * {@inheritDoc}
   */
  public final PS subtract(final PS value) {
    checkVariable(value);
    final String var = (this.variable != null ? this.variable : value.getVariable());

    if (this.hasLargerDgree(value)) {
      return this.subtract(value.expand(this.degree));
    } else if (hasSmallerDegree(value)) {
      return this.expand(value.getDegree()).subtract(value);
    }

    final EM mc = this.coefficients.subtract(value.getCoefficients());
    final PS ans = create(mc, var);
    ans.simplify();
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final PS subtract(final double value) {
    final EM ansCoef = this.coefficients.createClone();
    ansCoef.setElement(1, 1, ansCoef.getElement(1, 1).subtract(value));
    final PS ans = create(ansCoef, this.variable);
    ans.simplify();
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final PS subtract(final int value) {
    final EM ansCoef = this.coefficients.createClone();
    ansCoef.setElement(1, 1, ansCoef.getElement(1, 1).subtract(value));
    final PS ans = create(ansCoef, this.variable);
    ans.simplify();
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final PS multiply(final PS value) {
    this.checkVariable(value);
    final String var = (this.variable != null ? this.variable : value.getVariable());

    final EM coef1 = this.coefficients;
    final EM coef2 = value.getCoefficients();

    final int newDegree = this.degree + value.getDegree();

    final ES element = coef1.getElement(1, 1).multiply(coef2.getElement(1, 1));

    final ES[] ans = element.createArray(newDegree + 1);
    for (int i = 0; i <= newDegree; i++) {
      ans[i] = element.createZero();
    }

    for (int i = 0; i <= this.degree; i++) {
      for (int j = 0; j <= value.getDegree(); j++) {
        ans[i + j] = ans[i + j].add(coef1.getElement(1, i + 1).multiply(coef2.getElement(1, j + 1)));
      }
    }

    final PS polynomial = create(ans, var);
    polynomial.simplify();
    return polynomial;
  }

  /**
   * {@inheritDoc}
   */
  public final PS multiply(final double value) {
    if (value == 0) {
      return create(0, this.variable);
    }

    if (value == 1) {
      return this.clone();
    }

    if (value == -1) {
      return this.unaryMinus();
    }

    return create(this.coefficients.multiply(value), this.variable);
  }

  /**
   * {@inheritDoc}
   */
  public final PS multiply(final int value) {
    if (value == 0) {
      return create(0, this.variable);
    }

    if (value == 1) {
      return this.clone();
    }

    if (value == -1) {
      return this.unaryMinus();
    }

    return create(this.coefficients.multiply(value), this.variable);
  }

  /**
   * {@inheritDoc}
   */
  public final PS divide(final double value) {
    if (value == 0) {
      throw new IllegalArgumentException(Messages.getString("Polynomial.5")); //$NON-NLS-1$
    }

    if (value == 1) {
      return this.clone();
    }

    if (value == -1) {
      return this.unaryMinus();
    }

    return create(this.coefficients.divide(value), this.variable);
  }

  /**
   * {@inheritDoc}
   */
  public final PS divide(final int value) {
    if (value == 0) {
      throw new IllegalArgumentException(Messages.getString("Polynomial.6")); //$NON-NLS-1$
    }

    if (value == 1) {
      return this.clone();
    }

    if (value == -1) {
      return this.unaryMinus();
    }

    return create(this.coefficients.divide(value), this.variable);
  }

  /**
   * {@inheritDoc}
   */
  public final PS power(final int m) {
    if (m < 0) {
      throw new IllegalArgumentException(Messages.getString("Polynomial.7")); //$NON-NLS-1$
    }

    if (m == 0) {
      return create(1, this.variable);
    }

    if (m == 1) {
      return this.clone();
    }

    int n = m;
    PS aa = this.clone();
    PS b2 = create(1, this.variable);
    PS b;

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
  public final PS conjugate() {
    return create(this.coefficients.conjugate(), this.variable);
  }

  /**
   * {@inheritDoc}
   */
  public final PS unaryMinus() {
    return create(this.coefficients.unaryMinus(), this.variable);
  }

  /**
   * 多項式変数が等しいか判定します。
   * 
   * <p>どちらかが<code>null</code>の場合、比較しません。
   * 
   * @param opponent 比較する多項式
   */
  private void checkVariable(final PS opponent) {
    if (this.variable == null || opponent.getVariable() == null) {
      return;
    }
    if (hasSameVariable(opponent) == false) {
      throw new RuntimeException(Messages.getString("Polynomial.8")); //$NON-NLS-1$
    }
  }

  /**
   * {@inheritDoc}
   */
  public final boolean hasSameVariable(final PS opponent) {
    if (this.variable == null || opponent.getVariable() == null) {
      return true;
    }

    return this.variable.equals(opponent.getVariable());
  }

  /**
   * 次数が多項式<code>opponent</code>の次数より大きいか判定します。
   * 
   * @param opponent 多項式
   * @return 次数が<code>opponent</code>より大きければ true、そうでなければ false
   */
  private boolean hasLargerDgree(final PS opponent) {
    return this.degree > opponent.getDegree();
  }

  /**
   * 次数が多項式<code>opponent</code> の次数より小さいか判定します。
   * 
   * @param opponent 多項式
   * @return 次数が<code>opponent</code>より小さければ true、そうでなければ false
   */
  private boolean hasSmallerDegree(final PS opponent) {
    return this.degree < opponent.getDegree();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean hasSameDegree(final PS opponent) {
    return this.degree == opponent.getDegree();
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
  public final boolean isSameClass(final PS opponent) {
    return this.coefficients.getClass() == opponent.getCoefficients().getClass();
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
  public final boolean isConstant(final ES tolerance) {
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

    if ((this.coefficients).getElement(1, 1).isZero(tolerance)) {
      return true;
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero(final ES tolerance) {
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
  public final boolean isUnit(final ES tolerance) {
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
  public PS expand(final int newDegree) {
    if (this.degree == newDegree) {
      return (PS)this;
    }
    final EM ansCoef = this.coefficients.createClone().resize(1, newDegree + 1);
    return create(ansCoef, this.variable);
  }

  /**
   * {@inheritDoc}
   */
  public final ES evaluate(final int value) {
    final EM a = this.coefficients;
    ES sum = a.getElement(this.degree + 1);
    for (int i = this.degree - 1; i >= 0; i--) {
      sum = sum.multiply(value).add(a.getElement(i + 1));
    }
    return sum;
  }

  /**
   * {@inheritDoc}
   */
  public final ES evaluate(final double value) {
    final EM a = this.coefficients;
    ES sum = a.getElement(this.degree + 1);
    for (int i = this.degree - 1; i >= 0; i--) {
      sum = sum.multiply(value).add(a.getElement(i + 1));
    }
    return sum;
  }

  /**
   * {@inheritDoc}
   */
  public final ES evaluate(final ES value) {
    final EM a = this.coefficients;
    ES sum = a.getElement(this.degree + 1).multiply(value.createUnit());
    for (int i = this.degree - 1; i >= 0; i--) {
      sum = sum.multiply(value).add(a.getElement(i + 1));
    }
    return sum;
  }

  /**
   * {@inheritDoc}
   */
  public final PS evaluate(final PS value) {
    PS sum = create(this.coefficients.getElement(this.degree + 1), value.getVariable());
    for (int i = this.degree - 1; i >= 0; i--) {
      sum = sum.multiply(value).add(create(this.coefficients.getElement(i + 1)));
    }

    return sum;
  }

  /**
   * {@inheritDoc}
   */
  public final EM evaluate(final EM value) {
    final EM unit = value.createUnit(value.getColumnSize());
    EM sum = unit.multiply(getCoefficient(this.degree));
    for (int i = this.degree - 1; i >= 0; i--) {
      sum = sum.multiply(value).add(unit.multiply(getCoefficient(i)));
    }

    return sum;
  }

  /**
   * {@inheritDoc}
   */
  public final EM evaluateElementWise(final EM value) {
    final int argRowSize = value.getRowSize();
    final int argColumnSize = value.getColumnSize();

    final ES[][] ans = this.evaluate(value.getElement(1, 1)).createArray(argRowSize, argColumnSize);
    for (int i = 0; i < argRowSize; i++) {
      for (int j = 0; j < argColumnSize; j++) {
        ans[i][j] = this.evaluate(value.getElement(i + 1, j + 1));
      }
    }
    return ans[0][0].createGrid(argRowSize, argColumnSize, ans);
  }

  /**
   * {@inheritDoc}
   */
  public final PS derivative(final int order) {
    if (order < 0) {
      return integral(-order);
    }

    final int newDegree = this.degree - order;

    final PS ans;
    if (newDegree <= 0) {
      ans = createSameClassPolynomial(0);
    } else {
      ans = createSameClassPolynomial(newDegree);
    }

    final EM am = this.coefficients;
    final EM bm = ans.getCoefficients();
    for (int i = 0; i <= newDegree; i++) {
      bm.setElement(i + 1, am.getElement(i + order + 1).clone());
      for (int j = 1; j <= order; j++) {
        bm.setElement(i + 1, bm.getElement(i + 1).multiply(i + j));
      }
    }

    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final PS integral() {
    return integral(1);
  }

  /**
   * {@inheritDoc}
   */
  public final PS integral(final int order) {
    if (order < 0) {
      return derivative(-order);
    }

    final int newDegree = this.degree + order;
    final PS ans = this.createSameClassPolynomial(newDegree);

    final EM am = this.coefficients;
    final EM bm = ans.getCoefficients();
    for (int i = 1; i <= newDegree; i++) {
      bm.setElement(i + 1, am.getElement(i - order + 1).clone());
      for (int j = 0; j < order; j++) {
        bm.setElement(i + 1, bm.getElement(i + 1).divide(i - j));
      }
    }

    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public final PS shiftLower(final int count) {
    final int newDegree = this.degree - count;

    if (newDegree >= 0) {
      final PS ans = this.createSameClassPolynomial(newDegree);
      ans.partCopy(0, newDegree, (PS)this, count, this.degree);
      return ans;
    }

    return this.createSameClassPolynomial(0);
  }

  /**
   * {@inheritDoc}
   */
  public final PS shiftHigher(final int count) {
    final int newDegree = this.degree + count;
    final PS ans = createSameClassPolynomial(newDegree);
    ans.partCopy(count, newDegree, (PS)this, 0, this.degree);
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
      final PS b = this.getSubPolynomial(0, effectiveDegree);

      this.setCoefficients(b.getCoefficients());
      this.setDegree(b.getDegree());
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
      final ES coef = getCoefficient(i);
      final boolean isCoefficientfNotZero = (coef.isZero(tolerance) == false) || coef.isNaN() || coef.isInfinite();
      if (isCoefficientfNotZero) {
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
  private int getEffectiveDegree(final ES tolerance) {
    int effectiveDegree = 0;

    for (int i = this.degree; i >= 0; i--) {
      final ES coef = getCoefficient(i);
      final boolean isCoefficientfNotZero = (coef.isZero(tolerance) == false) || coef.isNaN() || coef.isInfinite();
      if (isCoefficientfNotZero) {
        effectiveDegree = i;
        break;
      }
    }

    return effectiveDegree;
  }

  /**
   * {@inheritDoc}
   */
  public final void simplify(final ES tolerance) {
    final int effectiveDegree = getEffectiveDegree(tolerance);

    if (effectiveDegree < this.degree) {
      final PS b = this.getSubPolynomial(0, effectiveDegree);

      this.setCoefficients(b.getCoefficients());
      this.setDegree(b.getDegree());
    }
  }

  /**
   * <code>degreeMin</code>次から<code>degreeMax</code>次までを切り取り返します。
   * 
   * @param degreeMin 開始次数
   * @param degreeMax 終了次数
   * @return 切り取った多項式
   */
  private PS getSubPolynomial(final int degreeMin, final int degreeMax) {
    return create(this.coefficients.getSubVector(degreeMin + 1, degreeMax + 1), this.variable);
  }

  /**
   * {@inheritDoc}
   */
  public final PS roundToZero(final double tolerance) {
    return create(this.coefficients.roundToZeroElementWise(tolerance));
  }

  /**
   * {@inheritDoc}
   */
  public final PS roundToZero(final ES tolerance) {
    return create(this.coefficients.roundToZeroElementWise(tolerance));
  }

  /**
   * {@inheritDoc}
   */
  public final PS round() {
    return create(this.coefficients.roundElementWise());
  }

  /**
   * {@inheritDoc}
   */
  public final PS ceil() {
    return create(this.coefficients.ceilElementWise());
  }

  /**
   * {@inheritDoc}
   */
  public final PS floor() {
    return create(this.coefficients.floorElementWise());
  }

  /**
   * {@inheritDoc}
   */
  public final PS fix() {
    return create(this.coefficients.fixElementWise());
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
  public final String toString(final boolean asExpression) {
    return toString(asExpression, "%G"); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public final String toString(final boolean asExpression, final String coefficientFormat) {
    return toStringFromPolynomial(asExpression, coefficientFormat);
  }

  /**
   * 多項式を文字列に変換します。
   * 
   * @param asExpression 式として評価できるようにするならばtrue、そうでなければfalse
   * @param coefficientFormat 係数の出力フォーマット
   * @return 変換で生成された文字列
   */
  private String toStringFromPolynomial(final boolean asExpression, final String coefficientFormat) {
    String var = "s"; //$NON-NLS-1$

    final String mulString;
    if (asExpression) {
      mulString = "*"; //$NON-NLS-1$
    } else {
      mulString = " "; //$NON-NLS-1$
    }

    if (this.variable != null) {
      var = this.variable;
    }

    if (this.degree < 0) {
      return "0"; //$NON-NLS-1$
    }

    final ES c0 = getCoefficient(0);
    final ES tolerance = c0.getMachineEpsilon().multiply(100);

    String str;
    if (c0.isZero(tolerance)) {
      str = ""; //$NON-NLS-1$
    } else {
      final String sign = " + "; //$NON-NLS-1$
      str = sign + c0.toString(coefficientFormat);
    }

    for (int i = 1; i <= this.degree; i++) {
      final ES ci = getCoefficient(i);

      if (ci.isZero(tolerance)) {
        continue;
      }

      String sign;
      if (i == this.degree) {
        sign = ""; //$NON-NLS-1$
      } else {
        sign = " + "; //$NON-NLS-1$
      }

      String varString;
      if (i == 1) {
        varString = var;
      } else {
        varString = var + String.format("^%d", Integer.valueOf(i)); //$NON-NLS-1$
      }

      final String coefString = ci.toString(coefficientFormat);

      str = (sign + coefString + mulString + varString) + str;
    }

    if (str.equals("")) { //$NON-NLS-1$
      return "0"; //$NON-NLS-1$
    }

    if (1 < str.length() && str.charAt(1) == '+') {
      return str.substring(3);
    }

    return str;
  }

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
  public final void copy(final PS source) {
    if (!isSameClass(source)) {
      throw new IllegalArgumentException(Messages.getString("Polynomial.41")); //$NON-NLS-1$
    }

    if (!hasSameDegree(source)) {
      throw new IllegalArgumentException(Messages.getString("Polynomial.42")); //$NON-NLS-1$
    }

    this.coefficients.copy(source.getCoefficients());
  }

  /**
   * {@inheritDoc}
   */
  public void partCopy(final int toMin, final int toMax, final PS source, final int fromMin, final int fromMax) {
    final EM sourceCoefficients = source.getCoefficients().getSubVector(fromMin + 1, fromMax + 1);
    this.coefficients.setSubVector(toMin + 1, toMax + 1, sourceCoefficients);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean compare(final String operator, final PS opponent) {
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
  public final PS createZero() {
    final ES zero = this.coefficients.getElement(1, 1).createZero();
    return create(zero);
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

  //
  // MatrixElementの実装
  //

  /**
   * {@inheritDoc}
   */
  public final PS createUnit() {
    //return create((CS)((MatrixElementOperator<?, ?>)this.coefficients).getElement(1, 1).createUnit());
    return create(this.coefficients.getElement(1, 1).createUnit());
  }

  /**
   * {@inheritDoc}
   */
  public final PS subtract(final ES value) {
    return subtract(create(value));
  }

  /**
   * {@inheritDoc}
   */
  public final PS multiply(final ES value) {
    return create(this.coefficients.multiply(value), this.variable);
  }

  /**
   * {@inheritDoc}
   */
  public final PS divide(final ES value) {
    return multiply(value.inverse());
  }

  /**
   * {@inheritDoc}
   */
  public final PS leftDivide(final ES value) {
    return inverse().multiply(value);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public final S transformFrom(final int value) {
//    return create(value, getVariable());
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final S transformFrom(final double value) {
//    return create(value, getVariable());
//  }

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
   * @param <CS> 係数スカラーの型
   * @param <CM> 係数行列の型
   * 
   * @param roots 根
   * @param variable 変数名
   * @param format 係数のフォーマット
   * @return 与えられた根をもつ多項式の因数分解の文字列
   */
  public static <CS extends NumericalScalar<CS, CM>, CM extends NumericalMatrix<CS, CM>> String getStringFactorization(final CM roots, final String variable, final String format) {
    final BooleanMatrix isZeroRoots = roots.compareElementWise(".==", 0); //$NON-NLS-1$
    final CM zeroRoots = roots.getSubVector(isZeroRoots.find());
    final CM nonZeroRoots = roots.getSubVector(isZeroRoots.notElementWise().find());

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
      final CS root = nonZeroRoots.getElement(i);
      if (root.isReal()) {
        final String sign = root.isGreaterThan(0) ? "-" : "+"; //$NON-NLS-1$ //$NON-NLS-2$
        expression.append("(" + variable + " " + sign + " " + root.abs().toString(format) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      } else {
        expression.append("(" + variable + " - " + root.toString(format) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      }
    }

    return expression.toString();
  }
  

  /**
   * {@inheritDoc}
   */
  public PM createGrid(EM constants) {
    int rowSize = constants.getRowSize();
    int columnSize = constants.getColumnSize();
    PS[][] elements = createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        elements[i][j] = create(constants.getElement(i+1, j+1));
      }
    }
    
    return createGrid(elements);
  }

  /**
   * {@inheritDoc}
   */
  public PM createGrid(EM constants, String variableName) {
    int rowSize = constants.getRowSize();
    int columnSize = constants.getColumnSize();
    PS[][] elements = createArray(rowSize, columnSize);

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        elements[i][j] = create(constants.getElement(i+1, j+1), variableName);
      }
    }
    
    return createGrid(elements);
  }

}