/*
 * $Id: ComplexScalar.java,v 1.33 2008/07/16 04:58:02 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.scalar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mklab.nfc.matrix.AbstractNumericalComplexMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.random.ComplexUniformRandom;
import org.mklab.nfc.random.RandomGenerator;


/**
 * 複素数値スカラーを表わすクラスです。
 * 
 * @author koga
 * @version $Revision: 1.33 $, 2004/06/22
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 */
public abstract class AbstractComplexNumericalScalar<RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> extends AbstractNumericalScalar<CS, CM>  implements ComplexNumericalScalar<RS,RM,CS,CM>{

  /** シリアルバージョン。 */
  private static final long serialVersionUID = 9107781747897062449L;

  /** 実部。 */
  private RS realPart;

  /** 虚部。 */
  private RS imaginaryPart;

  /**
   * Creates {@link AbstractComplexNumericalScalar}.
   * 
   * @param realPart 実部
   * @param imaginaryPart 虚部
   */
  public AbstractComplexNumericalScalar(final RS realPart, final RS imaginaryPart) {
    this.realPart = realPart.clone();
    this.imaginaryPart = imaginaryPart.clone();
    setFormat("%G"); //$NON-NLS-1$
  }

  /**
   * Creates {@link AbstractComplexNumericalScalar}.
   * 
   * @param realPart 実部
   */
  public AbstractComplexNumericalScalar(final RS realPart) {
    this.realPart = realPart.clone();
    this.imaginaryPart = realPart.createZero();
    setFormat("%G"); //$NON-NLS-1$
  }

  /**
   * 許容範囲内で等しいか判定します。
   * 
   * @param opponent 比較する複素数成分
   * @param tolerance 許容誤差
   * @return 許容範囲内で等しければtrue、そうでなければfalse
   */
  public final boolean equals(final CS opponent, final RS tolerance) {
    final boolean realEquals = this.getRealPart().equals(opponent.getRealPart(), tolerance);
    final boolean imaginaryEquals = this.getImaginaryPart().equals(opponent.getImaginaryPart(), tolerance);
    return realEquals && imaginaryEquals;
  }

  /**
   * 実数と等しいか判定します。
   * 
   * @param opponent 比較する実数
   * @return 等しければtrue、そうでなければfalse
   */
  public final boolean equals(final double opponent) {
    return this.getRealPart().equals(this.getRealPart().create(opponent)) && this.getImaginaryPart().isZero();
  }
  
  /**
   * 実部を設定します。
   * 
   * @param realPart 実部
   */
  public final void setRealPart(final int realPart) {
    this.realPart = this.realPart.create(realPart);
  }

  /**
   * 実部を設定します。
   * 
   * @param realPart 実部
   */
  public final void setRealPart(final double realPart) {
    this.realPart = this.realPart.create(realPart);
  }

  /**
   * 実部を設定します。
   * 
   * @param realPart 実部
   */
  public final void setRealPart(final RS realPart) {
    this.realPart = realPart.clone();
  }

  /**
   * 実部を返します。
   * 
   * @return 実部
   */
  public final RS getRealPart() {
    return this.realPart;
  }

  /**
   * 虚部を設定します。
   * 
   * @param imaginaryPart 虚部
   */
  public final void setImaginaryPart(final int imaginaryPart) {
    this.imaginaryPart = this.imaginaryPart.create(imaginaryPart);
  }

  /**
   * 虚部を設定します。
   * 
   * @param imaginaryPart 虚部
   */
  public final void setImaginaryPart(final double imaginaryPart) {
    this.imaginaryPart = this.imaginaryPart.create(imaginaryPart);
  }

  /**
   * 虚部を設定します。
   * 
   * @param imaginaryPart 虚部
   */
  public final void setImaginaryPart(final RS imaginaryPart) {
    this.imaginaryPart = imaginaryPart.clone();
  }

  /**
   * 虚部を返します。
   * 
   * @return 虚部
   */
  public final RS getImaginaryPart() {
    return this.imaginaryPart;
  }

  /**
   * 虚部単位を返します。
   * 
   * @return 虚部単位
   */
  public CS createImaginaryUnit() {
    return create(this.realPart.createZero(), this.imaginaryPart.createUnit());
  }

  /**
   * 偏角を返します。
   * 
   * @return 偏角
   */
  public final RS arg() {
    final RS ans = this.imaginaryPart.divide(this.realPart).atan();
    return ans;
  }
  
  /**
   * 標準出力に出力します。
   */
  public final void print() {
    print("ans"); //$NON-NLS-1$
  }

  /**
   * 標準出力に出力します。
   * 
   * @param name 名前
   */
  public final void print(final String name) {
    System.out.println(name + " = " + toString()); //$NON-NLS-1$
  }
  
//  /**
//   * 一次元配列を生成します。
//   * 
//   * @param elements elements
//   * @return 一次元配列
//   */
//  public final CS[] createArray(final RS[] elements) {
//    final int size = elements.length;
//    final CS[] array = createArray(size);
//    for (int i = 0; i < size; i++) {
//      array[i] = create(elements[i]);
//    }
//    return array;
//  }
//
//  /**
//   * 一次元配列を生成します。
//   * 
//   * @param realElements real elements
//   * @param imagElements imaginary elements
//   * @return 一次元配列
//   */
//  public final CS[] createArray(final RS[] realElements, final RS[] imagElements) {
//    final int size = realElements.length;
//    final CS[] array = createArray(size);
//    for (int i = 0; i < size; i++) {
//      array[i] = create(realElements[i], imagElements[i]);
//    }
//    return array;
//  }
//
//  /**
//   *  二次元配列を生成します。
//   * 
//   * @param realElements real part
//   * @param imagElements imaginary part
//   * @return 二次元配列
//   */
//  public final CS[][] createArray(final RS[][] realElements, final RS[][] imagElements) {
//    final int rowSize = realElements.length;
//    final int columnSize = rowSize == 0 ? 0 : realElements[0].length;
//    final CS[][] array = createArray(rowSize, columnSize);
//    for (int i = 0; i < rowSize; i++) {
//      for (int j = 0; j < columnSize; j++) {
//        array[i][j] = create(realElements[i][j], imagElements[i][j]);
//      }
//    }
//    return array;
//  }
//
//  /**
//   *  二次元配列を生成します。
//   * 
//   * @param elements elements
//   * @return 二次元配列
//   */
//  public final CS[][] createArray(final RS[][] elements) {
//    final int rowSize = elements.length;
//    final int columnSize = rowSize == 0 ? 0 : elements[0].length;
//    final CS[][] array = createArray(rowSize, columnSize);
//    for (int i = 0; i < rowSize; i++) {
//      for (int j = 0; j < columnSize; j++) {
//        array[i][j] = create(elements[i][j]);
//      }
//    }
//    return array;
//  }


  /**
   * {@inheritDoc}
   */
  public boolean equals(CS opponent, CS tolerance) {
    return equals(opponent, tolerance.getRealPart());
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
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

    final CS c = (CS)opponent;

    return this.realPart.equals(c.getRealPart()) && this.imaginaryPart.equals(c.getImaginaryPart());
  }

  /**
   * {@inheritDoc}
   */
  public final boolean equals(final CS opponent, final double tolerance) {
    final boolean realEquals = this.getRealPart().equals(opponent.getRealPart(), tolerance);
    final boolean imagEquals = this.getImaginaryPart().equals(opponent.getImaginaryPart(), tolerance);
    return realEquals && imagEquals;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CS clone() {
    final CS ans = super.clone();
    ans.setRealPart(this.getRealPart().clone());
    ans.setImaginaryPart(this.getImaginaryPart().clone());
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return toString(getFormat());
  }

  /**
   * {@inheritDoc}
   */
  public final String toString(final String valueFormat) {
    return "(" + this.getRealPart().toString(valueFormat) + "," + this.getImaginaryPart().toString(valueFormat) + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

  /**
   * {@inheritDoc}
   */
  public final CS add(final CS value) {
    final RS rePart = this.getRealPart().add(value.getRealPart());
    final RS imPart = this.getImaginaryPart().add(value.getImaginaryPart());
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS add(final double value) {
    final RS rePart = this.getRealPart().add(value);
    final RS imPart = this.getImaginaryPart().clone();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS add(final int value) {
    final RS rePart = this.getRealPart().add(value);
    final RS imPart = this.getImaginaryPart().clone();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS subtract(final CS value) {
    final RS rePart = this.getRealPart().subtract(value.getRealPart());
    final RS imPart = this.getImaginaryPart().subtract(value.getImaginaryPart());
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS subtract(final double value) {
    final RS rePart = this.getRealPart().subtract(value);
    return create(rePart, this.getImaginaryPart());
  }

  /**
   * {@inheritDoc}
   */
  public final CS subtract(final int value) {
    final RS rePart = this.getRealPart().subtract(value);
    return create(rePart, this.getImaginaryPart());
  }

  /**
   * {@inheritDoc}
   */
  public final CS multiply(final CS value) {
    final RS rePart = this.getRealPart().multiply(value.getRealPart()).subtract(this.getImaginaryPart().multiply(value.getImaginaryPart()));
    final RS imPart = this.getRealPart().multiply(value.getImaginaryPart()).add(this.getImaginaryPart().multiply(value.getRealPart()));
    return create(rePart, imPart);
  }

//  /**
//   * 自身に複素数を乗じます。
//   * 
//   * @param value 乗じる複素数
//   * @return 自身
//   */
//  public final BaseComplexNumericalScalar<S, M> multiplySelf(final BaseComplexNumericalScalar<S, M> value) {
//    final S rePart = this.getRealPart().multiply(value.getRealPart()).subtract(this.getImaginaryPart().multiply(value.getImaginaryPart()));
//    final S imPart = this.getRealPart().multiply(value.getImaginaryPart()).add(this.getImaginaryPart().multiply(value.getRealPart()));
//    this.getRealPart() = rePart;
//    this.getImaginaryPart() = imPart;
//    return this;
//  }

  /**
   * {@inheritDoc}
   */
  public final CS multiply(final double value) {
    final RS rePart = this.getRealPart().multiply(value);
    final RS imPart = this.getImaginaryPart().multiply(value);
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS multiply(final int value) {
    final RS rePart = this.getRealPart().multiply(value);
    final RS imPart = this.getImaginaryPart().multiply(value);
    return create(rePart, imPart);
  }

//  /**
//   * 自身に実数を乗じます。
//   * 
//   * @param value 乗じる実数
//   * @return 自身
//   */
//  public final BaseComplexNumericalScalar<S, M> multiplySelf(final double value) {
//    this.getRealPart() = this.getRealPart().multiply(value);
//    this.getImaginaryPart() = this.getImaginaryPart().multiply(value);
//    return this;
//  }

  /**
   * {@inheritDoc}
   */
  public final CS conjugate() {
    final RS rePart = this.getRealPart().clone();
    final RS imPart = this.getImaginaryPart().unaryMinus();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS inverse() {
    final CS conj = this.conjugate();
    final RS den = this.multiply(conj).getRealPart();
    final RS rePart = conj.getRealPart().divide(den);
    final RS imPart = conj.getImaginaryPart().divide(den);
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final CS divide(final CS value) {
    return multiply(value.inverse());
  }

  /**
   * {@inheritDoc}
   */
  public final CS divide(final double value) {
    final RS rePart = this.getRealPart().divide(value);
    final RS imPart = this.getImaginaryPart().divide(value);
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS divide(final int value) {
    final RS rePart = this.getRealPart().divide(value);
    final RS imPart = this.getImaginaryPart().divide(value);
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final CS leftDivide(final CS value) {
    return inverse().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public final CS leftDivide(final double value) {
    return inverse().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public final CS leftDivide(final int value) {
    return inverse().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public final CS power(final int scalar) {
    final RS length = abs().getRealPart().power(scalar);
    final RS th = (this.getImaginaryPart().divide(this.getRealPart())).atan().multiply(scalar);
    final RS rePart = th.cos().multiply(length);
    final RS imPart = th.sin().multiply(length);
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS unaryMinus() {
    final RS rePart = this.getRealPart().unaryMinus();
    final RS imPart = this.getImaginaryPart().unaryMinus();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS fix() {
    final RS rePart = this.getRealPart().fix();
    final RS imPart = this.getImaginaryPart().fix();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS round() {
    final RS rePart = this.getRealPart().round();
    final RS imPart = this.getImaginaryPart().round();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS roundToZero(final double tolerance) {
    final RS rePart = this.getRealPart().roundToZero(tolerance);
    final RS imPart = this.getImaginaryPart().roundToZero(tolerance);
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS roundToZero(final CS tolerance) {
    final RS rePart = this.getRealPart().roundToZero(tolerance.getRealPart());
    final RS imPart = this.getImaginaryPart().roundToZero(tolerance.getRealPart());
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS ceil() {
    final RS rePart = this.getRealPart().ceil();
    final RS imPart = this.getImaginaryPart().ceil();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS floor() {
    final RS rePart = this.getRealPart().floor();
    final RS imPart = this.getImaginaryPart().floor();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero() {
    return this.getRealPart().isZero() && this.getImaginaryPart().isZero();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero(final double tolerance) {
    return this.getRealPart().isZero(tolerance) && this.getImaginaryPart().isZero(tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero(final CS tolerance) {
    return this.getRealPart().isZero(tolerance.getRealPart()) && this.getImaginaryPart().isZero(tolerance.getRealPart());
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit() {
    return this.getRealPart().isUnit() && this.getImaginaryPart().isZero();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit(final double tolerance) {
    return this.getRealPart().isUnit(tolerance) && this.getImaginaryPart().isZero(tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit(final CS tolerance) {
    return this.getRealPart().isUnit(tolerance.getRealPart()) && this.getImaginaryPart().isZero(tolerance.getRealPart());
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isNaN() {
    return this.getRealPart().isNaN() || this.getImaginaryPart().isNaN();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isFinite() {
    return isInfinite() == false && isNaN() == false;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isInfinite() {
    return this.getRealPart().isInfinite() || this.getImaginaryPart().isInfinite();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean compare(final String operator, final CS opponent) {
    if (operator.equals(".!=")) { //$NON-NLS-1$
      return equals(opponent) == false;
    }

    if (operator.equals(".==")) { //$NON-NLS-1$
      return equals(opponent);
    }

    if (operator.equals(".<")) { //$NON-NLS-1$
      return isLessThan(opponent);
    }

    if (operator.equals(".<=")) { //$NON-NLS-1$
      return isLessThanOrEquals(opponent);
    }

    if (operator.equals(".>")) { //$NON-NLS-1$
      return isGreaterThan(opponent);
    }

    if (operator.equals(".>=")) { //$NON-NLS-1$
      return isGreaterThanOrEquals(opponent);
    }

    throw new IllegalArgumentException();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean compare(final String operator, final double opponent) {
    if (operator.equals(".!=")) { //$NON-NLS-1$
      return equals(opponent) == false;
    } else if (operator.equals(".==")) { //$NON-NLS-1$
      return equals(opponent);
    }

    if (operator.equals(".<")) { //$NON-NLS-1$
      return isLessThan(opponent);
    }

    if (operator.equals(".<=")) { //$NON-NLS-1$
      return isLessThanOrEquals(opponent);
    }

    if (operator.equals(".>")) { //$NON-NLS-1$
      return isGreaterThan(opponent);
    }

    if (operator.equals(".>=")) { //$NON-NLS-1$
      return isGreaterThanOrEquals(opponent);
    }

    throw new IllegalArgumentException();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean compare(final String operator, final int opponent) {
    if (operator.equals(".!=")) { //$NON-NLS-1$
      return equals(opponent) == false;
    } else if (operator.equals(".==")) { //$NON-NLS-1$
      return equals(opponent);
    }

    if (operator.equals(".<")) { //$NON-NLS-1$
      return isLessThan(opponent);
    }

    if (operator.equals(".<=")) { //$NON-NLS-1$
      return isLessThanOrEquals(opponent);
    }

    if (operator.equals(".>")) { //$NON-NLS-1$
      return isGreaterThan(opponent);
    }

    if (operator.equals(".>=")) { //$NON-NLS-1$
      return isGreaterThanOrEquals(opponent);
    }

    throw new IllegalArgumentException();
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @SuppressWarnings("unchecked")
  //  public final BaseNumericalComplexMatrix<S,M> createGrid(final int rowSize, final int columnSize, final Scalar<?,?>[][] elements) {
  //    return new BaseNumericalComplexMatrix<>(rowSize, columnSize, (BaseComplexNumericalScalar<S,M>[][])elements);
  //  }

//  /**
//   * {@inheritDoc}
//   */
//  public final CM createGrid(final int rowSize, final int columnSize, final CS[][] elements) {
//    return elements[0][0].createGrid(rowSize, columnSize, elements);
//  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @SuppressWarnings("unchecked")
  //  public final BaseNumericalComplexMatrix<S,M> createGrid(final Scalar<?,?>[] elements) {
  //    final int ansColumnSize = elements.length;
  //    final int ansRowSize = ansColumnSize == 0 ? 0 : 1;
  //    final BaseComplexNumericalScalar<S,M>[][] elements2 = (BaseComplexNumericalScalar<S,M>[][])elements[0].createArray(1, elements.length);
  //    System.arraycopy(elements, 0, elements2[0], 0, elements.length);
  //
  //    return new BaseNumericalComplexMatrix<>(ansRowSize, ansColumnSize, elements2);
  //  }

//  /**
//   * {@inheritDoc}
//   */
//  public final CM createGrid(final CS[] elements) {
//    final int ansColumnSize = elements.length;
//    final int ansRowSize = ansColumnSize == 0 ? 0 : 1;
//    final CS[][] elements2 = elements[0].createArray(1, elements.length);
//    System.arraycopy(elements, 0, elements2[0], 0, elements.length);
//
//    return elements2[0][0].createGrid(ansRowSize, ansColumnSize, elements2);
//    //return new BaseNumericalComplexMatrix<>(ansRowSize, ansColumnSize, elements2);
//  }

  /**
   * {@inheritDoc}
   */
  public final CS createUnit() {
    final RS rePart = this.getRealPart().createUnit();
    final RS imPart = this.getRealPart().createZero();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS createPI() {
    final RS rePart = this.getRealPart().createPI();
    final RS imPart = this.getRealPart().createZero();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS createE() {
    final RS rePart = this.getRealPart().createE();
    final RS imPart = this.getRealPart().createZero();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS create(final int value) {
    final RS rePart = this.getRealPart().create(value);
    final RS imPart = this.getRealPart().createZero();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS create(final double value) {
    final RS rePart = this.getRealPart().create(value);
    final RS imPart = this.getRealPart().createZero();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS valueOf(final String numberString) {
    final Pattern pattern = Pattern.compile("\\(\\s*([^,\\s]+)\\s*,\\s*([^)\\s]+)\\s*\\)"); //$NON-NLS-1$
    final Matcher matcher = pattern.matcher(numberString);

    if (matcher.find() == false) {
      throw new NumberFormatException(numberString);
    }

    final RS rePart = this.getRealPart().valueOf(matcher.group(1));
    final RS imPart = this.getImaginaryPart().valueOf(matcher.group(2));
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS createZero() {
    final RS rePart = this.getRealPart().createZero();
    final RS imPart = this.getRealPart().createZero();
    return create(rePart, imPart);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @SuppressWarnings("unchecked")
  //  public final boolean compare(final String operator, final GridElement<?> opponent) {
  //    if (!(opponent instanceof BaseComplexNumericalScalar)) {
  //      return false;
  //    }
  //    return compare(operator, (BaseComplexNumericalScalar<S,M>)opponent);
  //  }

//  /**
//   * {@inheritDoc}
//   */
//  public  abstract CS[] createArray(final int size);
//
//  /**
//   * {@inheritDoc}
//   */
//  public abstract CS[][] createArray(final int rowSize, final int columnSize);

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final BaseComplexNumericalScalar<S,M>[] createArray(final GridElement<?>[] elements) {
  //    final int size = elements.length;
  //    final BaseComplexNumericalScalar<S,M>[] array = createArray(size);
  //    for (int i = 0; i < size; i++) {
  //      array[i] = transformFrom(elements[i]);
  //    }
  //    return array;
  //  }


//  /**
//   * {@inheritDoc}
//   */
//  public final BaseComplexNumericalScalar<S, M> transformFrom(final int value) {
//    return create(this.getRealPart().transformFrom(value), this.getRealPart().createZero());
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final BaseComplexNumericalScalar<S, M> transformFrom(final double value) {
//    return create(this.getRealPart().transformFrom(value), this.getRealPart().createZero());
//  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @Override
  //  public final boolean isTransformableFrom(final GridElement<? extends GridElement<?>> value) {
  //    if (super.isTransformableFrom(value)) {
  //      return true;
  //    }
  //
  //    if (value instanceof DoubleNumber) {
  //      return true;
  //    }
  //
  //    if (value instanceof DoubleComplexNumber) {
  //      return true;
  //    }
  //
  //    if (this.getRealPart().isTransformableFrom(value)) {
  //      return true;
  //    }
  //
  //    if (value instanceof Scalar<?,?> && ((Scalar<?,?>)value).isComplex() && this.getRealPart().isTransformableFrom(((BaseComplexNumericalScalar<?,?>)value).getRealPart())) {
  //      return true;
  //    }
  //
  //    return false;
  //  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @Override
  //  public final boolean isTransformableTo(final GridElement<? extends GridElement<?>> value) {
  //    if (super.isTransformableTo(value)) {
  //      return true;
  //    }
  //
  //    //    if (value instanceof Polynomial) {
  //    //      return true;
  //    //    }
  //    //
  //    //    if (value instanceof RationalPolynomial) {
  //    //      return true;
  //    //    }
  //
  //    return false;
  //  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @Override
  //  public final GridElement<?> transformTo(final GridElement<? extends GridElement<?>> value) {
  //    if (super.isTransformableTo(value)) {
  //      return super.transformTo(value);
  //    }
  //
  //    //    if (value instanceof Polynomial) {
  //    //      return new Polynomial(this.clone());
  //    //    }
  //    //
  //    //    if (value instanceof RationalPolynomial) {
  //    //      return new RationalPolynomial(this.clone());
  //    //    }
  //
  //    throw new IllegalArgumentException(Messages.getString("ComplexScalar.9")); //$NON-NLS-1$
  //  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @Override
  //  public final BaseComplexNumericalScalar<S,M> transformFrom(final GridElement<? extends GridElement<?>> value) {
  //    if (super.isTransformableFrom(value)) {
  //      return super.transformFrom(value);
  //    }
  //
  //    if (value instanceof DoubleNumber) {
  //      final S rePart = this.getRealPart().transformFrom(value);
  //      final S imPart = this.getRealPart().createZero();
  //      return create(rePart, imPart);
  //    }
  //
  //    if (value instanceof DoubleComplexNumber) {
  //      final S rePart = this.getRealPart().transformFrom(((DoubleComplexNumber)value).getRealPart());
  //      final S imPart = this.getRealPart().transformFrom(((DoubleComplexNumber)value).getImaginaryPart());
  //      return create(rePart, imPart);
  //    }
  //
  //    if (this.getRealPart().isTransformableFrom(value)) {
  //      final S rePart = this.getRealPart().transformFrom(value);
  //      final S imPart = this.getRealPart().createZero();
  //      return create(rePart, imPart);
  //    }
  //
  //    if (value instanceof Scalar<?,?> && ((Scalar<?,?>)value).isComplex() && this.getRealPart().isTransformableFrom(((BaseComplexNumericalScalar<?,?>)value).getRealPart())) {
  //      final S rePart = this.getRealPart().transformFrom(((BaseComplexNumericalScalar<?,?>)value).getRealPart());
  //      final S imPart = this.getRealPart().transformFrom(((BaseComplexNumericalScalar<?,?>)value).getImaginaryPart());
  //      return create(rePart, imPart);
  //    }
  //
  //    throw new IllegalArgumentException(Messages.getString("ComplexScalar.10")); //$NON-NLS-1$
  //  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final ScalarOperator getAddOperator() {
  //    return ComplexNumericalScalarAddOperator.<BaseComplexNumericalScalar<S,M>,BaseNumericalComplexMatrix<S,M>> getInstance();
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final ScalarOperator getDivideOperator() {
  //    return ComplexNumericalScalarDivideOperator.<BaseComplexNumericalScalar<S,M>,BaseNumericalComplexMatrix<S,M>> getInstance();
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final ScalarOperator getLeftDivideOperator() {
  //    return ComplexNumericalScalarLeftDivideOperator.<BaseComplexNumericalScalar<S,M>,BaseNumericalComplexMatrix<S,M>> getInstance();
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final ScalarOperator getMultiplyOperator() {
  //    return ComplexNumericalScalarMultiplyOperator.<BaseComplexNumericalScalar<S,M>,BaseNumericalComplexMatrix<S,M>> getInstance();
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final ScalarOperator getSubtractOperator() {
  //    return ComplexNumericalScalarSubtractOperator.<BaseComplexNumericalScalar<S,M>,BaseNumericalComplexMatrix<S,M>> getInstance();
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final ComplexNumericalScalarEqual getEqualOperator() {
  //    return ComplexNumericalScalarEqual.getInstance();
  //  }

  /**
   * {@inheritDoc}
   */
  public final boolean isComplex() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isReal() {
    return false;
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final BaseComplexNumericalScalar<T> toComplex() {
  //    return clone();
  //  }

  /**
   * {@inheritDoc}
   */
  public final CS power(final CS scalar) {
    final RS t = (this.getImaginaryPart().divide(this.getRealPart())).atan();
    final RS length = (this.getRealPart().multiply(this.getRealPart()).add(this.getImaginaryPart().multiply(this.getImaginaryPart()))).sqrt();
    final CS poweredLength = ComplexNumericalScalarUtil.power(length, scalar);

    
    final RS e = this.getRealPart().createUnit().exp();
    final RS ePower = e.power(scalar.getImaginaryPart().multiply(t).unaryMinus());
    final CS r = poweredLength.multiply(create(ePower, ePower.createZero()));
    final RS th = scalar.getRealPart().multiply(t);
    return r.multiply(create(th.cos(), th.sin()));
  }

  /**
   * {@inheritDoc}
   */
  public final CS power(final double scalar) {
    final RS length = abs().getRealPart().power(scalar);
    final RS th = (this.getImaginaryPart().divide(this.getRealPart())).atan().multiply(scalar);
    final RS rePart = th.cos().multiply(length);
    final RS imPart = th.sin().multiply(length);
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS abs() {
    final RS ans = (this.getRealPart().multiply(this.getRealPart()).add(this.getImaginaryPart().multiply(this.getImaginaryPart()))).sqrt();
    return create(ans, ans.createZero());
  }

  /**
   * {@inheritDoc}
   */
  public final CS abs2() {
    final RS ans = this.getRealPart().multiply(this.getRealPart()).add(this.getImaginaryPart().multiply(this.getImaginaryPart()));
    return create(ans, ans.createZero());
  }

  /**
   * {@inheritDoc}
   */
  public final CS sqrt() {
    final RS w = this.abs().getRealPart();

    final RS ansReal = (w.add(this.getRealPart()).divide(2)).abs().sqrt();
    RS ansImag = (w.subtract(this.getRealPart()).divide(2)).abs().sqrt();

    if (this.getImaginaryPart().isLessThan(0)) {
      ansImag = ansImag.unaryMinus();
    }

    return create(ansReal, ansImag);
  }

  /**
   * {@inheritDoc}
   */
  public final CS log() {
    final RS w1 = this.getRealPart().multiply(this.getRealPart()).add(this.getImaginaryPart().multiply(this.getImaginaryPart()));
    RS w2;

    if (this.getRealPart().isZero()) {
      final RS halfPI = this.getRealPart().createZero().acos();
      w2 = halfPI;
      if (this.getImaginaryPart().isLessThan(0)) {
        w2 = w2.unaryMinus();
      }
    } else {
      w2 = (this.getImaginaryPart().divide(this.getRealPart())).atan();
    }

    final RS rePart = w1.sqrt().log();

    return create(rePart, w2);
  }

  /**
   * {@inheritDoc}
   */
  public final CS log10() {
    final CS b = this.log();
    final RS logE10 = this.getRealPart().createUnit().multiply(10).log();
    final RS re = b.getRealPart().divide(logE10);
    final RS im = b.getImaginaryPart().divide(logE10);
    return create(re, im);
  }

  /**
   * {@inheritDoc}
   */
  public final CS exp() {
    final RS w = this.getRealPart().exp();
    final RS im = this.getImaginaryPart();
    final RS rePart = w.multiply(im.cos());
    final RS imPart = w.multiply(im.sin());
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS sin() {
    final RS w1 = this.getImaginaryPart().exp();
    final RS w2 = w1.inverse();
    final RS rePart = this.getRealPart().sin().multiply(w1.add(w2)).divide(2);
    final RS imPart = this.getRealPart().cos().multiply(w1.subtract(w2)).divide(2);
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS asin() {
    // asin(a) = - i log(i*a + sqrt(1 - a*a))

    final RS rePart = this.getRealPart().multiply(this.getRealPart()).add(this.getImaginaryPart().multiply(this.getImaginaryPart())).unaryMinus().add(1);
    final RS imPart = this.getRealPart().multiply(this.getImaginaryPart()).unaryMinus().multiply(2);
    CS w1 = create(rePart, imPart);
    w1 = w1.sqrt();
    w1.setRealPart(w1.getRealPart().subtract(this.getImaginaryPart()));
    w1.setImaginaryPart(w1.getImaginaryPart().add(this.getRealPart()));
    w1 = w1.log();

    final RS tmp = w1.getRealPart().unaryMinus();
    w1.setRealPart(w1.getImaginaryPart());
    w1.setImaginaryPart(tmp);

    return w1;
  }

  /**
   * {@inheritDoc}
   */
  public final CS sinh() {
    // sinh(a) = (exp(a) - exp(-a))/2
    final RS w1 = this.getRealPart().exp();
    final RS w2 = this.getRealPart().unaryMinus().exp();

    final RS rePart = w1.multiply(this.getImaginaryPart().cos()).subtract(w2.multiply((this.getImaginaryPart().unaryMinus()).cos())).divide(2);
    final RS imPart = w1.multiply(this.getImaginaryPart().sin()).subtract(w2.multiply((this.getImaginaryPart().unaryMinus()).sin())).divide(2);
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS asinh() {
    // asinh(a) = log(a + sqrt(a*a + 1))
    final RS rePart = this.getRealPart().multiply(this.getRealPart()).subtract(this.getImaginaryPart().multiply(this.getImaginaryPart())).add(1);
    final RS imPart = this.getRealPart().multiply(this.getImaginaryPart()).multiply(2);
    CS w1 = create(rePart, imPart);
    w1 = w1.sqrt();

    w1.setRealPart(w1.getRealPart().add(this.getRealPart()));
    w1.setImaginaryPart(w1.getImaginaryPart().add(this.getImaginaryPart()));
    w1 = w1.log();
    return w1;
  }

  /**
   * {@inheritDoc}
   */
  public final CS cos() {
    final RS w1 = this.getImaginaryPart().exp();
    final RS w2 = w1.inverse();
    final RS rePart = this.getRealPart().cos().multiply(w1.add(w2)).divide(2);
    final RS imPart = this.getRealPart().sin().multiply(w2.subtract(w1)).divide(2);
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS acos() {
    // acos(a) = - i log(a + sqrt(a*a - 1))
    final RS rePart = this.getRealPart().multiply(this.getRealPart()).subtract(this.getImaginaryPart().multiply(this.getImaginaryPart())).subtract(1);
    final RS imPart = this.getRealPart().multiply(this.getImaginaryPart()).multiply(2);
    CS w1 = create(rePart, imPart);
    w1 = w1.sqrt();
    w1.setRealPart(w1.getRealPart().add(this.getRealPart()));
    w1.setImaginaryPart(w1.getImaginaryPart().add(this.getImaginaryPart()));
    w1 = w1.log();

    final RS tmp = w1.getRealPart().unaryMinus();
    w1.setRealPart(w1.getImaginaryPart());
    w1.setImaginaryPart(tmp);

    return w1;
  }

  /**
   * {@inheritDoc}
   */
  public final CS acosh() {
    // acosh(a) = log(a + sqrt(a*a - 1))
    final RS rePart = this.getRealPart().multiply(this.getRealPart()).subtract(this.getImaginaryPart().multiply(this.getImaginaryPart())).subtract(1);
    final RS imPart = this.getRealPart().multiply(this.getImaginaryPart()).multiply(2);
    CS w1 = create(rePart, imPart);
    w1 = w1.sqrt();
    w1.setRealPart(w1.getRealPart().add(this.getRealPart()));
    w1.setImaginaryPart(w1.getImaginaryPart().add(this.getImaginaryPart()));
    w1 = w1.log();
    return w1;
  }

  /**
   * {@inheritDoc}
   */
  public final CS cosh() {
    // cosh(a) = (exp(a) + exp(-a))/2 = (exp(a)*exp(bj) + exp(-a)*exp(-bj))/2
    final RS w1 = this.getRealPart().exp();
    final RS w2 = this.getRealPart().unaryMinus().exp();

    final RS rePart = w1.multiply(this.getImaginaryPart().cos()).add(w2.multiply((this.getImaginaryPart().unaryMinus()).cos())).divide(2);
    final RS imPart = w1.multiply(this.getImaginaryPart().sin()).add(w2.multiply((this.getImaginaryPart().unaryMinus()).sin())).divide(2);
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final CS tan() {
    final CS w1 = this.sin();
    final CS w2 = this.cos();
    return w1.divide(w2);
  }

  /**
   * {@inheritDoc}
   */
  public final CS atan() {
    // atan(a) = 1/2i log((1 + i*a)/(1 - i*a))
    final RS rePart1 = this.getImaginaryPart().unaryMinus().add(1);
    CS w1 = create(rePart1, this.getRealPart());
    final RS rePart2 = this.getImaginaryPart().add(1);
    final RS imPart2 = this.getRealPart().unaryMinus();
    final CS w2 = create(rePart2, imPart2);

    w1 = w1.divide(w2).log();

    w2.setRealPart(w1.getImaginaryPart().divide(2));
    w2.setImaginaryPart(w1.getRealPart().unaryMinus().divide(2));

    return w2;
  }

  /**
   * {@inheritDoc}
   */
  public final CS atan2(final CS value) {
    return this.divide(value).atan();
  }

  /**
   * {@inheritDoc}
   */
  public final CS atan2(final int value) {
    return this.divide(value).atan();
  }

  /**
   * {@inheritDoc}
   */
  public final CS atan2(final double value) {
    return this.divide(value).atan();
    // TODO 象限に関する補正
  }

  /**
   * {@inheritDoc}
   */
  public final CS tanh() {
    final CS w1 = this.sinh();
    final CS w2 = this.cosh();
    return w1.divide(w2);
  }

  /**
   * {@inheritDoc}
   */
  public final CS atanh() {
    // atanh(a) = 1/2 log((1 + a)/(1 - a))
    final RS rePart1 = this.getRealPart().add(1);
    CS w1 = create(rePart1, this.getImaginaryPart());
    final RS rePart2 = this.getRealPart().unaryMinus().add(1);
    final RS imPart2 = this.getImaginaryPart().unaryMinus();
    final CS w2 = create(rePart2, imPart2);
    w1 = w1.divide(w2).log();

    w2.setRealPart(w1.getRealPart().divide(2));
    w2.setImaginaryPart(w1.getImaginaryPart().divide(2));
    return w2;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isGreaterThan(final CS opponent) {
    return abs().getRealPart().isGreaterThan(opponent.abs().getRealPart());
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isGreaterThanOrEquals(final CS opponent) {
    return abs().getRealPart().isGreaterThanOrEquals(opponent.abs().getRealPart());
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isLessThan(final CS opponent) {
    return abs().getRealPart().isLessThan(opponent.abs().getRealPart());
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isLessThanOrEquals(final CS opponent) {
    return abs().getRealPart().isLessThanOrEquals(opponent.abs().getRealPart());
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isGreaterThan(final int opponent) {
    return abs().getRealPart().isGreaterThan(opponent);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isGreaterThan(final double opponent) {
    return abs().getRealPart().isGreaterThan(opponent);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isGreaterThanOrEquals(final int opponent) {
    return abs().getRealPart().isGreaterThanOrEquals(opponent);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isGreaterThanOrEquals(final double opponent) {
    return abs().getRealPart().isGreaterThanOrEquals(opponent);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isLessThan(final int opponent) {
    return abs().getRealPart().isLessThan(opponent);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isLessThan(final double opponent) {
    return abs().getRealPart().isLessThan(opponent);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isLessThanOrEquals(final int opponent) {
    return abs().getRealPart().isLessThanOrEquals(opponent);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isLessThanOrEquals(final double opponent) {
    return abs().getRealPart().isLessThanOrEquals(opponent);
  }

  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final ScalarOperator getPowerOperator() {
  //    return ComplexNumericalScalarPowerOperator.<BaseComplexNumericalScalar<S,M>,BaseNumericalComplexMatrix<S,M>> getInstance();
  //  }
  //
  //  /**
  //   * {@inheritDoc}
  //   */
  //  public final ScalarOperator getAtan2Operator() {
  //    return ComplexNumericalScalarAtan2Operator.<BaseComplexNumericalScalar<S,M>,BaseNumericalComplexMatrix<S,M>> getInstance();
  //  }

  /**
   * {@inheritDoc}
   */
  public final CS getMachineEpsilon() {
    return create(this.getRealPart().getMachineEpsilon(), this.getRealPart().createZero());
  }

  /**
   * {@inheritDoc}
   */
  public final CS getInfinity() {
    return create(this.getRealPart().getInfinity(), this.getImaginaryPart().getInfinity());
  }

  /**
   * {@inheritDoc}
   */
  public final CS getNaN() {
    return create(this.getRealPart().getNaN(), this.getImaginaryPart().getNaN());
  }

  /**
   * {@inheritDoc}
   */
  public final RandomGenerator<CS, CM> createUniformRandomGenerator() {
    return new ComplexUniformRandom<>((CS)this);
  }

  /**
   * Override hashCode.
   * 
   * @return the Objects hashcode.
   */
  @Override
  public final int hashCode() {
    int hashCode = 1;
    final int prime = 31;
    hashCode = prime * hashCode + (int)(+serialVersionUID ^ (serialVersionUID >>> (prime + 1)));
    hashCode = prime * hashCode + (this.getRealPart() == null ? 0 : this.getRealPart().hashCode());
    hashCode = prime * hashCode + (this.getImaginaryPart() == null ? 0 : this.getImaginaryPart().hashCode());
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CS remainder(final CS value2) {
    if (value2.isZero()) {
      return this.clone();
    }

    final CS value = this.divide(value2);

    if ((value.isComplex() && value.getImaginaryPart().isZero() && value.getRealPart().isGreaterThanOrEquals(0))) {
      return this.subtract(value.floor().multiply(value2));
    }

    return this.subtract(value.ceil().multiply(value2));
  }

  /**
   * {@inheritDoc}
   */
  public CS add(RS value) {
    return add(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CS subtract(RS value) {
    return subtract(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CS multiply(RS value) {
    return multiply(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CS divide(RS value) {
    return divide(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CS leftDivide(RS value) {
    return leftDivide(value.toComplex());
  }

}