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
package org.mklab.nfc.scalar;

import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.ComplexSymbolicMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.matrix.RealSymbolicMatrix;


/**
 * 複素数式スカラーを表すクラスです。
 * 
 * @author koga
 * @version $Revision$, 2021/07/15
 * @param <RS> 実スカラーの型
 * @param <RM> 実行列の型
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RES> 実係数スカラーの型
 * @param <REM> 実係数行列の型
 * @param <CES> 複素係数スカラーの型
 * @param <CEM> 複素係数行列の型
 */
public abstract class AbstractComplexSymbolicScalar<RS extends RealSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>,  RM extends RealSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, CS extends ComplexSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, CM extends ComplexSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>,   RES extends RealNumericalScalar<RES,REM,CES,CEM>, REM extends RealNumericalMatrix<RES,REM,CES,CEM>,  CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>> extends AbstractSymbolicScalar<CS,CM,CES,CEM> implements ComplexSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>{

  /** */
  private static final long serialVersionUID = 8184491318494793530L;

  /** 実部。 */
  private RS realPart;

  /** 虚部。 */
  private RS imaginaryPart;

  /**
   * Creates {@link AbstractComplexSymbolicScalar}.
   * 
   * @param realPart 実部
   * @param imaginaryPart 虚部
   */
  public AbstractComplexSymbolicScalar(final RS realPart, final RS imaginaryPart) {
    this.setRealPart(realPart);
    this.setImaginaryPart(imaginaryPart);
    setFormat("%G"); //$NON-NLS-1$
  }

  /**
   * Creates {@link AbstractComplexSymbolicScalar}.
   * 
   * @param realPart 実部
   */
  public AbstractComplexSymbolicScalar(final RS realPart) {
    this.setRealPart(realPart);
    this.setImaginaryPart(realPart.createZero());
    setFormat("%G"); //$NON-NLS-1$
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
  
  /**
   * 許容範囲内で等しいか判定します。
   * 
   * @param opponent 比較する複素数成分
   * @param tolerance 許容誤差
   * @return 許容範囲内で等しければtrue、そうでなければfalse
   */
  public final boolean equals(CS opponent, final double tolerance) {
    final boolean realEquals = this.realPart.equals(opponent.getRealPart(), tolerance);
    final boolean imagEquals = this.imaginaryPart.equals(opponent.getImaginaryPart(), tolerance);
    return realEquals && imagEquals;
  }

  /**
   * 許容範囲内で等しいか判定します。
   * 
   * @param opponent 比較する複素数成分
   * @param tolerance 許容誤差
   * @return 許容範囲内で等しければtrue、そうでなければfalse
   */
  public final boolean equals(CS opponent, final CES tolerance) {
    final boolean realEquals = this.realPart.equals(opponent.getRealPart(), tolerance.getRealPart());
    final boolean imaginaryEquals = this.imaginaryPart.equals(opponent.getImaginaryPart(), tolerance.getRealPart());
    return realEquals && imaginaryEquals;
  }
  
  /**
   * 実数と等しいか判定します。
   * 
   * @param opponent 比較する実数
   * @return 等しければtrue、そうでなければfalse
   */
  public final boolean equals(final double opponent) {
    return this.realPart.equals(this.realPart.create(opponent)) && this.imaginaryPart.isZero();
  }
  
  /**
   * 実部を設定します。
   * 
   * @param realPart 実部
   */
  public final void setRealPart(final RS realPart) {
    this.setRealPart(realPart.clone());
  }

  /**
   * 実部を返します。
   * 
   * @return 実部
   */
  public final RS getRealPart() {
    return this.realPart.clone();
  }

  /**
   * 虚部を設定します。
   * 
   * @param imaginaryPart 虚部
   */
  public final void setImaginaryPart(final RS imaginaryPart) {
    this.setImaginaryPart(imaginaryPart.clone());
  }

  /**
   * 虚部を返します。
   * 
   * @return 虚部
   */
  public final RS getImaginaryPart() {
    return this.imaginaryPart.clone();
  }

  /**
   * 実部を設定します。
   * 
   * @param realPart 実部
   */
  public final void setRealPart(final int realPart) {
    if (this.isReal()) {
      throw new IllegalArgumentException(Messages.getString("AbstractSymbolicScalar.0")); //$NON-NLS-1$
    }

    setRealPart(this.realPart.create(realPart));
  }

  /**
   * 実部を設定します。
   * 
   * @param realPart 実部
   */
  public final void setRealPart(final double realPart) {
    if (this.isReal()) {
      throw new IllegalArgumentException(Messages.getString("AbstractSymbolicScalar.1")); //$NON-NLS-1$
    }

    setRealPart(this.realPart.create(realPart));
  }

  /**
   * 虚部を設定します。
   * 
   * @param imagPart 虚部
   */
  public final void setImaginaryPart(final int imagPart) {
    if (this.isReal()) {
      throw new IllegalArgumentException(Messages.getString("AbstractSymbolicScalar.2")); //$NON-NLS-1$
    }

    setImaginaryPart(this.imaginaryPart.create(imagPart));
  }

  /**
   * 虚部を設定します。
   * 
   * @param imagPart 虚部
   */
  public final void setImaginaryPart(final double imagPart) {
    if (this.isReal()) {
      throw new IllegalArgumentException(Messages.getString("AbstractSymbolicScalar.3")); //$NON-NLS-1$
    }

    setImaginaryPart(this.imaginaryPart.create(imagPart));
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

    CS c = (CS)opponent;

    return this.realPart.equals(c.getRealPart()) && this.imaginaryPart.equals(c.getImaginaryPart());
  }

  
//  /**
//   * {@inheritDoc}
//   */
//  public boolean equals(BaseComplexSymbolicScalar<S,M,CS,CM> opponent, CS tolerance) {
//    if (isTransformableFrom(opponent)) {
//      return equals((SymbolicScalar<?,?,?,?>)transformFrom(opponent), tolerance);
//    }
//
//    if (opponent.isTransformableFrom(this)) {
//      return ((BaseComplexSymbolicScalar<?,?,CS,?>)opponent.transformFrom(this)).equals(opponent, tolerance);
//    }
//
//    return false;
//  }


  /**
   * {@inheritDoc}
   */
  @Override
  public CS clone() {
    CS ans = super.clone();
    ans.setRealPart(this.realPart.clone());
    ans.setImaginaryPart(this.imaginaryPart.clone());
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
    return "(" + this.realPart.toString(valueFormat) + "," + this.imaginaryPart.toString(valueFormat) + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }


  /**
   * {@inheritDoc}
   */
  public CS add(CS value) {
    final RS rePart = this.realPart.add(value.getRealPart());
    final RS imPart = this.imaginaryPart.add(value.getImaginaryPart());
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS add(final double value) {
    final RS rePart = this.realPart.add(value);
    final RS imPart = this.imaginaryPart.clone();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS add(final int value) {
    final RS rePart = this.realPart.add(value);
    final RS imPart = this.imaginaryPart.clone();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS subtract(CS value) {
    final RS rePart = this.realPart.subtract(value.getRealPart());
    final RS imPart = this.imaginaryPart.subtract(value.getImaginaryPart());
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS subtract(final double value) {
    final RS rePart = this.realPart.subtract(value);
    return create(rePart, this.imaginaryPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS subtract(final int value) {
    final RS rePart = this.realPart.subtract(value);
    return create(rePart, this.imaginaryPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS multiply(CS value) {
    final RS rePart = this.realPart.multiply(value.getRealPart()).subtract(this.imaginaryPart.multiply(value.getImaginaryPart()));
    final RS imPart = this.realPart.multiply(value.getImaginaryPart()).add(this.imaginaryPart.multiply(value.getRealPart()));
    return create(rePart, imPart);
  }

//  /**
//   * 自身に複素数を乗じます。
//   * 
//   * @param value 乗じる複素数
//   * @return 自身
//   */
//  public final BaseComplexSymbolicScalar<S,M,CS,CM> multiplySelf(final BaseComplexSymbolicScalar<S,M,CS,CM> value) {
//    final S rePart = this.realPart.multiply(value.getRealPart()).subtract(this.imaginaryPart.multiply(value.getImaginaryPart()));
//    final S imPart = this.realPart.multiply(value.getImaginaryPart()).add(this.imaginaryPart.multiply(value.getRealPart()));
//    this.setRealPart(rePart);
//    this.setImaginaryPart(imPart);
//    return this;
//  }

  /**
   * {@inheritDoc}
   */
  public CS multiply(final double value) {
    final RS rePart = this.realPart.multiply(value);
    final RS imPart = this.imaginaryPart.multiply(value);
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS multiply(final int value) {
    final RS rePart = this.realPart.multiply(value);
    final RS imPart = this.imaginaryPart.multiply(value);
    return create(rePart, imPart);
  }

//  /**
//   * 自身に実数を乗じます。
//   * 
//   * @param value 乗じる実数
//   * @return 自身
//   */
//  public final BaseComplexSymbolicScalar<S,M,CS,CM> multiplySelf(final double value) {
//    this.setRealPart(this.realPart.multiply(value);
//    this.setImaginaryPart(this.imaginaryPart.multiply(value);
//    return this;
//  }

  /**
   * {@inheritDoc}
   */
  public CS conjugate() {
    final RS rePart = this.realPart.clone();
    final RS imPart = this.imaginaryPart.unaryMinus();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS inverse() {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public CS divide(CS value) {
    return multiply(value.inverse());
  }

  /**
   * {@inheritDoc}
   */
  public CS divide(final double value) {
    final RS rePart = this.realPart.divide(value);
    final RS imPart = this.imaginaryPart.divide(value);
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS divide(final int value) {
    final RS rePart = this.realPart.divide(value);
    final RS imPart = this.imaginaryPart.divide(value);
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS leftDivide(CS value) {
    return inverse().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public CS leftDivide(final double value) {
    return inverse().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public CS leftDivide(final int value) {
    return inverse().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public CS power(final int scalar) {
    CS ans = create(1);
    for (int i = 0; i < scalar; i++) {
      ans = ans.multiply((CS)this);
    }
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public CS unaryMinus() {
    final RS rePart = this.realPart.unaryMinus();
    final RS imPart = this.imaginaryPart.unaryMinus();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS fix() {
    final RS rePart = this.realPart.fix();
    final RS imPart = this.imaginaryPart.fix();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS round() {
    final RS rePart = this.realPart.round();
    final RS imPart = this.imaginaryPart.round();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS roundToZero(final double tolerance) {
    final RS rePart = this.realPart.roundToZero(tolerance);
    final RS imPart = this.imaginaryPart.roundToZero(tolerance);
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS roundToZero(final CES tolerance) {
    final RS rePart = this.realPart.roundToZero(tolerance.getRealPart());
    final RS imPart = this.imaginaryPart.roundToZero(tolerance.getRealPart());
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS ceil() {
    final RS rePart = this.realPart.ceil();
    final RS imPart = this.imaginaryPart.ceil();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS floor() {
    final RS rePart = this.realPart.floor();
    final RS imPart = this.imaginaryPart.floor();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero() {
    return this.realPart.isZero() && this.imaginaryPart.isZero();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero(final double tolerance) {
    return this.realPart.isZero(tolerance) && this.imaginaryPart.isZero(tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isZero(final CES tolerance) {
    return this.realPart.isZero(tolerance.getRealPart()) && this.imaginaryPart.isZero(tolerance.getRealPart());
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit() {
    return this.realPart.isUnit() && this.imaginaryPart.isZero();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit(final double tolerance) {
    return this.realPart.isUnit(tolerance) && this.imaginaryPart.isZero(tolerance);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isUnit(final CES tolerance) {
    return this.realPart.isUnit(tolerance.getRealPart()) && this.imaginaryPart.isZero(tolerance.getRealPart());
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isNaN() {
    return this.realPart.isNaN() || this.imaginaryPart.isNaN();
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
    return this.realPart.isInfinite() || this.imaginaryPart.isInfinite();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean compare(final String operator, CS opponent) {
    if (operator.equals(".!=")) { //$NON-NLS-1$
      return equals(opponent) == false;
    }

    if (operator.equals(".==")) { //$NON-NLS-1$
      return equals(opponent);
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

    throw new IllegalArgumentException();
  }

//  /**
//   * {@inheritDoc}
//   */
//  @SuppressWarnings("unchecked")
//  public final BaseSymbolicComplexMatrix<S,?> createGrid(final int rowSize, final int columnSize, final Scalar<?,?>[][] elements) {
//    return new BaseSymbolicComplexMatrix<>(rowSize, columnSize, (BaseComplexSymbolicScalar<S,M,CS,CM>[][])elements);
//  }

  /**
   * {@inheritDoc}
   */
  public CM createGrid(final int rowSize, final int columnSize, CS[][] elements) {
    return elements[0][0].createGrid(rowSize, columnSize, elements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  @SuppressWarnings("unchecked")
//  public final BaseSymbolicComplexMatrix<S,?> createGrid(final Scalar<?,?>[] elements) {
//    final int ansColumnSize = elements.length;
//    final int ansRowSize = ansColumnSize == 0 ? 0 : 1;
//    final BaseComplexSymbolicScalar<S,M,CS,CM>[][] elements2 = (BaseComplexSymbolicScalar<S,M,CS,CM>[][])elements[0].createArray(1, elements.length);
//    System.arraycopy(elements, 0, elements2[0], 0, elements.length);
//
//    return new BaseSymbolicComplexMatrix<>(ansRowSize, ansColumnSize, elements2);
//  }
  
  /**
   * {@inheritDoc}
   */
  public CM createGrid(CS[] elements) {
    CS[] elements2 = elements[0].createArray(elements.length);
    System.arraycopy(elements, 0, elements2[0], 0, elements.length);

    return elements2[0].createGrid(elements2);
  }

  /**
   * {@inheritDoc}
   */
  public CS createUnit() {
    final RS rePart = this.realPart.createUnit();
    final RS imPart = this.realPart.createZero();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS create(final int value) {
    final RS rePart = this.realPart.create(value);
    final RS imPart = this.realPart.createZero();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS create(final double value) {
    final RS rePart = this.realPart.create(value);
    final RS imPart = this.realPart.createZero();
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS createZero() {
    final RS rePart = this.realPart.createZero();
    final RS imPart = this.realPart.createZero();
    return create(rePart, imPart);
  }

//  /**
//   * {@inheritDoc}
//   */
//  @SuppressWarnings("unchecked")
//  public final boolean compare(final String operator, final GridElement<?> opponent) {
//    if (!(opponent instanceof BaseComplexSymbolicScalar)) {
//      return false;
//    }
//    return compare(operator, (BaseComplexSymbolicScalar<S,M,CS,CM>)opponent);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @SuppressWarnings("unchecked")
//  public CS[] createArray(final int size) {
//    return new BaseComplexSymbolicScalar[size];
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  @SuppressWarnings("unchecked")
//  public CS[][] createArray(final int rowSize, final int columnSize) {
//    return new BaseComplexSymbolicScalar[rowSize][columnSize];
//  }

//  public final BaseComplexSymbolicScalar<S,M,CS,CM>[] createArray(final S[] elements) {
//    final int size = elements.length;
//    final BaseComplexSymbolicScalar<S,M,CS,CM>[] array = elements[0].createArray(size);
//    System.arraycopy(elements, 0, array, 0, size);
//    return array;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @SuppressWarnings("unchecked")
//  public final BaseComplexSymbolicScalar<S,M,CS,CM>[][] createArray(final GridElement<?>[][] elements) {
//    final int rowSize = elements.length;
//    final int columnSize = rowSize == 0 ? 0 : elements[0].length;
//    final BaseComplexSymbolicScalar<S,M,CS,CM>[][] array = (BaseComplexSymbolicScalar<S,M,CS,CM>[][])elements[0][0].createArray(rowSize, columnSize);
//    for (int row = 0; row < rowSize; row++) {
//      System.arraycopy(elements[row], 0, array[row], 0, columnSize);
//    }
//    return array;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final BaseComplexSymbolicScalar<S,M,CS,CM> transformFrom(final int value) {
//    return create(this.realPart.transformFrom(value), this.realPart.createZero());
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final BaseComplexSymbolicScalar<S,M,CS,CM> transformFrom(final double value) {
//    return create(this.realPart.transformFrom(value), this.realPart.createZero());
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
//    if (this.realPart.isTransformableFrom(value)) {
//      return true;
//    }
//
//    if (value instanceof Scalar<?,?> && ((Scalar<?,?>)value).isComplex() && this.realPart.isTransformableFrom(((BaseComplexSymbolicScalar<?,?,?,?>)value).getRealPart())) {
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
//    if (value instanceof Polynomial) {
//      return true;
//    }
//
//    if (value instanceof RationalPolynomial) {
//      return true;
//    }
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
//    throw new IllegalArgumentException(Messages.getString("ComplexScalar.9")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public final BaseComplexSymbolicScalar<S,M,CS,CM> transformFrom(final GridElement<? extends GridElement<?>> value) {
//    if (super.isTransformableFrom(value)) {
//      return super.transformFrom(value);
//    }
//
//    if (value instanceof DoubleNumber) {
//      final S rePart = this.realPart.transformFrom(value);
//      final S imPart = this.realPart.createZero();
//      return create(rePart, imPart);
//    }
//
//    if (value instanceof DoubleComplexNumber) {
//      final S rePart = this.realPart.transformFrom(((DoubleComplexNumber)value).getRealPart());
//      final S imPart = this.realPart.transformFrom(((DoubleComplexNumber)value).getImaginaryPart());
//      return create(rePart, imPart);
//    }
//
//    if (this.realPart.isTransformableFrom(value)) {
//      final S rePart = this.realPart.transformFrom(value);
//      final S imPart = this.realPart.createZero();
//      return create(rePart, imPart);
//    }
//
//    if (value instanceof Scalar<?,?> && ((Scalar<?,?>)value).isComplex() && this.realPart.isTransformableFrom(((BaseComplexSymbolicScalar<?,?,?,?>)value).getRealPart())) {
//      final S rePart = this.realPart.transformFrom(((BaseComplexSymbolicScalar<?,?,?,?>)value).getRealPart());
//      final S imPart = this.realPart.transformFrom(((BaseComplexSymbolicScalar<?,?,?,?>)value).getImaginaryPart());
//      return create(rePart, imPart);
//    }
//
//    throw new IllegalArgumentException(Messages.getString("ComplexScalar.10")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getAddOperator() {
//    return ComplexSymbolicScalarAddOperator.<BaseComplexSymbolicScalar<S,M,CS,CM>,BaseSymbolicComplexMatrix<S,M,CS,CM>,CS,CM> getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getDivideOperator() {
//    return ComplexSymbolicScalarDivideOperator.<BaseComplexSymbolicScalar<S,M,CS,CM>,BaseSymbolicComplexMatrix<S,M,CS,CM>,CS,CM> getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getLeftDivideOperator() {
//    return ComplexSymbolicScalarLeftDivideOperator.<BaseComplexSymbolicScalar<S,M,CS,CM>,BaseSymbolicComplexMatrix<S,M,CS,CM>,CS,CM> getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getMultiplyOperator() {
//    return ComplexSymbolicScalarMultiplyOperator.<BaseComplexSymbolicScalar<S,M,CS,CM>,BaseSymbolicComplexMatrix<S,M,CS,CM>,CS,CM> getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final ScalarOperator getSubtractOperator() {
//    return ComplexSymbolicScalarSubtractOperator.<BaseComplexSymbolicScalar<S,M,CS,CM>,BaseSymbolicComplexMatrix<S,M,CS,CM>,CS,CM> getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public final SymbolicScalarEqual getEqualOperator() {
//    return ComplexSymbolicScalarEqual.getInstance();
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
  //  public final BaseComplexSymbolicScalar<T> toComplex() {
  //    return clone();
  //  }

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
    hashCode = prime * hashCode + (this.realPart == null ? 0 : this.realPart.hashCode());
    hashCode = prime * hashCode + (this.imaginaryPart == null ? 0 : this.imaginaryPart.hashCode());
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  public CS derivative(int order) {
    final RS rePart = this.realPart.derivative(order);
    final RS imPart = this.imaginaryPart.derivative(order);
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS shiftLower(int count) {
    final RS rePart = this.realPart.shiftLower(count);
    final RS imPart = this.imaginaryPart.shiftLower(count);
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public CS shiftHigher(int count) {
    final RS rePart = this.realPart.shiftHigher(count);
    final RS imPart = this.imaginaryPart.shiftHigher(count);
    return create(rePart, imPart);
  }

  /**
   * {@inheritDoc}
   */
  public String getVariable() {
    return this.realPart.getVariable();
  }

  /**
   * {@inheritDoc}
   */
  public void setVariable(String variableName) {
    this.realPart.setVariable(variableName);
    this.imaginaryPart.setVariable(variableName);
  }

  /**
   * {@inheritDoc}
   */
  public CES evaluate(int value) {
    final RES realResult = this.realPart.evaluate(value);
    final RES imagResult = this.imaginaryPart.evaluate(value);
    
    final CES zero = evaluate(0);
    return zero.create(realResult, imagResult);
  }

  /**
   * {@inheritDoc}
   */
  public CES  evaluate(double value) {
    final RES realResult = this.realPart.evaluate(value);
    final RES imagResult = this.imaginaryPart.evaluate(value);
    final CES zero = evaluate(0);
    return zero.create(realResult, imagResult);
  }

  /**
   * {@inheritDoc}
   */
  public CES  evaluate(CES value) {
    final RES realRealResult = this.realPart.evaluate(value.getRealPart());
    final RES realImagResult = this.realPart.evaluate(value.getImaginaryPart());
    final RES imagRealResult = this.imaginaryPart.evaluate(value.getRealPart());
    final RES imagImagResult = this.imaginaryPart.evaluate(value.getImaginaryPart());
    
    final RES realResult = realRealResult.subtract(imagImagResult);
    final RES imagResult = realImagResult.add(imagRealResult);

    return value.create(realResult, imagResult);
  }

  /**
   * {@inheritDoc}
   */
  public CS evaluate(CS scalar) {
    CS realResult = create(this.realPart).evaluate(scalar);
    CS imagResult =create(this.imaginaryPart).evaluate(scalar);
    
    RS real = realResult.getRealPart().subtract(imagResult.getImaginaryPart());
    RS imag = realResult.getImaginaryPart().add(imagResult.getRealPart());
    return create(real,imag);
  }

  /**
   * {@inheritDoc}
   */
  public CEM evaluate(CEM value) {
   final REM realRealResult = this.realPart.evaluate(value.getRealPart());
   final REM realImagResult = this.realPart.evaluate(value.getImaginaryPart());
   final REM imagRealResult = this.imaginaryPart.evaluate(value.getRealPart());
   final REM imagImagResult = this.imaginaryPart.evaluate(value.getImaginaryPart());
    
   final REM realResult = realRealResult.subtract(imagImagResult);
   final REM imagResult = realImagResult.add(imagRealResult);

   return value.create(realResult,imagResult);
    //return new BaseNumericalComplexMatrix<>(realResult,imagResult);
  }


//  /**
//   * {@inheritDoc}
//   */
//  public BaseNumericalComplexMatrix<CS,CM> evaluate(CM value) {
////final BaseNumericalMatrix<?,?> realResult = (BaseNumericalMatrix<?, ?>)this.realPart.evaluate(value);
////final BaseNumericalMatrix<?,?> imagResult = (BaseNumericalMatrix<?, ?>)this.imaginaryPart.evaluate(value);
//    throw new UnsupportedOperationException();
//  }


}
