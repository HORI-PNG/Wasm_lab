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

import org.mklab.nfc.matrix.AnyComplexPolynomialMatrix;
import org.mklab.nfc.matrix.AnyComplexRationalPolynomialMatrix;
import org.mklab.nfc.matrix.AnyRealPolynomialMatrix;
import org.mklab.nfc.matrix.AnyRealRationalPolynomialMatrix;
import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;

/**
 * @param <RES> 係数スカラーの型
 * @param <REM> 係数行列の型
 * @param <CES> 複素係数スカラーの型 
 * @param <CEM> 複素係数行列の型
 * @author koga
 * @version $Revision$, 2021/09/14
 */
public class AnyRealPolynomial<RES extends RealNumericalScalar<RES, REM, CES, CEM>, REM extends RealNumericalMatrix<RES, REM, CES, CEM>, CES extends ComplexNumericalScalar<RES, REM, CES, CEM>, CEM extends ComplexNumericalMatrix<RES, REM, CES, CEM>> extends AbstractRealPolynomial<AnyRealPolynomial<RES,REM,CES,CEM>, AnyRealPolynomialMatrix<RES,REM,CES,CEM>, AnyComplexPolynomial<RES,REM,CES,CEM>, AnyComplexPolynomialMatrix<RES,REM,CES,CEM>, AnyRealRationalPolynomial<RES, REM, CES, CEM>, AnyRealRationalPolynomialMatrix<RES, REM, CES, CEM>, AnyComplexRationalPolynomial<RES, REM, CES, CEM>, AnyComplexRationalPolynomialMatrix<RES, REM, CES, CEM>,RES, REM, CES, CEM> {

  /** */
  private static final long serialVersionUID = -7220352862190960968L;

  /**
   * Creates {@link AnyRealPolynomial}.
   * @param coefficientVector coefficient vector
   * @param variableName variable name
   */
  public AnyRealPolynomial(REM coefficientVector, String variableName) {
    super(coefficientVector, variableName);
  }

  /**
   * Creates {@link AnyRealPolynomial}.
   * @param coefficientVector coefficient vector
   */
  public AnyRealPolynomial(REM coefficientVector) {
    super(coefficientVector);
  }

  /**
   * Creates {@link AnyRealPolynomial}.
   * @param constant constant
   * @param variableName variable name
   */
  public AnyRealPolynomial(RES constant, String variableName) {
    super(constant, variableName);
  }

  /**
   * Creates {@link AnyRealPolynomial}.
   * @param constant constant
   */
  public AnyRealPolynomial(RES constant) {
    super(constant);
  }

  /**
   * Creates {@link AnyRealPolynomial}.
   * @param coefficients coefficients
   * @param variableName variable name
   */
  public AnyRealPolynomial(RES[] coefficients, String variableName) {
    super(coefficients, variableName);
  }

  /**
   * Creates {@link AnyRealPolynomial}.
   * @param coefficients coefficients
   */
  public AnyRealPolynomial(RES[] coefficients) {
    super(coefficients);
  }

  /**
   * {@inheritDoc}
   */
  public AnyComplexPolynomial<RES, REM, CES, CEM> toComplex() {
    return new AnyComplexPolynomial<>(getCoefficients().toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealPolynomialMatrix<RES, REM, CES, CEM> createGrid(int rowSize, int columnSize, AnyRealPolynomial<RES, REM, CES, CEM>[][] elements) {
    return new AnyRealPolynomialMatrix<>(rowSize, columnSize, elements);
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealPolynomialMatrix<RES, REM, CES, CEM> createGrid(AnyRealPolynomial<RES, REM, CES, CEM>[] elements) {
    return new AnyRealPolynomialMatrix<>(elements);
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealPolynomial<RES, REM, CES, CEM>[] createArray(int size) {
    return new AnyRealPolynomial[size];
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealPolynomial<RES, REM, CES, CEM>[][] createArray(int rowSize, int columnSize) {
    return new AnyRealPolynomial[rowSize][columnSize];
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealPolynomial<RES, REM, CES, CEM> create(RES constant) {
    return new AnyRealPolynomial<>(constant);
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealPolynomial<RES, REM, CES, CEM> create(String variableName) {
    RES[] coefficients = getCoefficient(0).createArray(2);
    coefficients[0] = getCoefficient(0).createZero();
    coefficients[1] = getCoefficient(0).createUnit();
    return new AnyRealPolynomial<>(coefficients);
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealPolynomial<RES, REM, CES, CEM> create(RES constant, String variableName) {
    return new AnyRealPolynomial<>(constant, variableName);
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealPolynomial<RES, REM, CES, CEM> create(double[] coefficients) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealPolynomial<RES, REM, CES, CEM> create(double[] coefficients, String variableName) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealPolynomial<RES, REM, CES, CEM> create(RES[] coefficients) {
    return new AnyRealPolynomial<>(coefficients);
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealPolynomial<RES, REM, CES, CEM> create(RES[] coefficients, String variableName) {
    return new AnyRealPolynomial<>(coefficients, variableName);
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealPolynomial<RES, REM, CES, CEM> create(REM coefficientVector) {
    return new AnyRealPolynomial<>(coefficientVector);
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealPolynomial<RES, REM, CES, CEM> create(REM coefficientVector, String variableName) {
    return new AnyRealPolynomial<>(coefficientVector, variableName);
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomial<RES, REM, CES, CEM> toRational() {
    return new AnyRealRationalPolynomial<>(this);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public AnyComplexPolynomial<RES, REM, CES, CEM> add(AnyComplexPolynomial<RES, REM, CES, CEM> value) {
//    return toComplex().add(value);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public AnyComplexPolynomial<RES, REM, CES, CEM> subtract(AnyComplexPolynomial<RES, REM, CES, CEM> value) {
//    return toComplex().subtract(value);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public AnyComplexPolynomial<RES, REM, CES, CEM> multiply(AnyComplexPolynomial<RES, REM, CES, CEM> value) {
//    return toComplex().multiply(value);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public AnyComplexPolynomial<RES, REM, CES, CEM> divide(AnyComplexPolynomial<RES, REM, CES, CEM> value) {
//    return toComplex().divide(value);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public AnyComplexPolynomial<RES, REM, CES, CEM> leftDivide(AnyComplexPolynomial<RES, REM, CES, CEM> value) {
//    return toComplex().leftDivide(value);
//  }
}
