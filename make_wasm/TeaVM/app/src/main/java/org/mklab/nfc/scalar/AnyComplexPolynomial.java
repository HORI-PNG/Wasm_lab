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
public class AnyComplexPolynomial<RES extends RealNumericalScalar<RES, REM, CES, CEM>, REM extends RealNumericalMatrix<RES, REM, CES, CEM>, CES extends ComplexNumericalScalar<RES, REM, CES, CEM>, CEM extends ComplexNumericalMatrix<RES, REM, CES, CEM>> extends AbstractComplexPolynomial<AnyRealPolynomial<RES,REM,CES,CEM>, AnyRealPolynomialMatrix<RES,REM,CES,CEM>, AnyComplexPolynomial<RES,REM,CES,CEM>, AnyComplexPolynomialMatrix<RES,REM,CES,CEM>, AnyRealRationalPolynomial<RES, REM, CES, CEM>, AnyRealRationalPolynomialMatrix<RES, REM, CES, CEM>, AnyComplexRationalPolynomial<RES, REM, CES, CEM>, AnyComplexRationalPolynomialMatrix<RES, REM, CES, CEM>,RES, REM, CES, CEM> {

  /** */
  private static final long serialVersionUID = 1668947079741358603L;

  /**
   * Creates {@link AnyComplexPolynomial}.
   * @param coefficientVector coefficient vector
   * @param variableName variable name
   */
  public AnyComplexPolynomial(CEM coefficientVector, String variableName) {
    super(coefficientVector, variableName);
  }

  /**
   * Creates {@link AnyComplexPolynomial}.
   * @param coefficientVector coefficient vector
   */
  public AnyComplexPolynomial(CEM coefficientVector) {
    super(coefficientVector);
  }

  /**
   * Creates {@link AnyComplexPolynomial}.
   * @param constant constant
   * @param variableName variable name
   */
  public AnyComplexPolynomial(CES constant, String variableName) {
    super(constant, variableName);
  }

  /**
   * Creates {@link AnyComplexPolynomial}.
   * @param constant constant
   */
  public AnyComplexPolynomial(CES constant) {
    super(constant);
  }

  /**
   * Creates {@link AnyComplexPolynomial}.
   * @param coefficients coefficients
   * @param variableName variable name
   */
  public AnyComplexPolynomial(CES[] coefficients, String variableName) {
    super(coefficients, variableName);
  }

  /**
   * Creates {@link AnyComplexPolynomial}.
   * @param coefficients coefficients
   */
  public AnyComplexPolynomial(CES[] coefficients) {
    super(coefficients);
  }

  /**
   * {@inheritDoc}
   */
  public AnyComplexPolynomialMatrix<RES, REM, CES, CEM> createGrid(int rowSize, int columnSize, AnyComplexPolynomial<RES, REM, CES, CEM>[][] elements) {
    return new AnyComplexPolynomialMatrix<>(elements);
  }

  /**
   * {@inheritDoc}
   */
  public AnyComplexPolynomialMatrix<RES, REM, CES, CEM> createGrid(AnyComplexPolynomial<RES, REM, CES, CEM>[] elements) {
    return new AnyComplexPolynomialMatrix<>(elements);
  }

  /**
   * {@inheritDoc}
   */
  public AnyComplexPolynomial<RES, REM, CES, CEM>[] createArray(int size) {
    return new AnyComplexPolynomial[size];
  }

  /**
   * {@inheritDoc}
   */
  public AnyComplexPolynomial<RES, REM, CES, CEM>[][] createArray(int rowSize, int columnSize) {
    return new AnyComplexPolynomial[rowSize][columnSize];
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealPolynomial<RES, REM, CES, CEM> getRealPart() {
    return new AnyRealPolynomial<>(getCoefficients().getRealPart());
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealPolynomial<RES, REM, CES, CEM> getImaginaryPart() {
    return new AnyRealPolynomial<>(getCoefficients().getImaginaryPart());
  }

  /**
   * {@inheritDoc}
   */
  public AnyComplexPolynomial<RES, REM, CES, CEM> create(CES constant) {
    return new AnyComplexPolynomial<>(constant);
  }

  /**
   * {@inheritDoc}
   */
  public AnyComplexPolynomial<RES, REM, CES, CEM> create(String variableName) {
    CES[] coefficients = getCoefficient(0).createArray(2);
    coefficients[0] = getCoefficient(0).createZero();
    coefficients[1] = getCoefficient(0).createUnit();
    return new AnyComplexPolynomial<>(coefficients);
  }

  /**
   * {@inheritDoc}
   */
  public AnyComplexPolynomial<RES, REM, CES, CEM> create(CES constant, String variableName) {
    return new AnyComplexPolynomial<>(constant, variableName);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public AnyComplexPolynomial<RES, REM, CES, CEM> create(double[] coefficients) {
//    throw new UnsupportedOperationException();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public AnyComplexPolynomial<RES, REM, CES, CEM> create(double[] coefficients, String variableName) {
//    throw new UnsupportedOperationException();
//  }

  /**
   * {@inheritDoc}
   */
  public AnyComplexPolynomial<RES, REM, CES, CEM> create(CES[] coefficients) {
    return new AnyComplexPolynomial<>(coefficients);
  }

  /**
   * {@inheritDoc}
   */
  public AnyComplexPolynomial<RES, REM, CES, CEM> create(CES[] coefficients, String variableName) {
    return new AnyComplexPolynomial<>(coefficients, variableName);
  }

  /**
   * {@inheritDoc}
   */
  public AnyComplexPolynomial<RES, REM, CES, CEM> create(CEM coefficientVector) {
    return new AnyComplexPolynomial<>(coefficientVector);
  }

  /**
   * {@inheritDoc}
   */
  public AnyComplexPolynomial<RES, REM, CES, CEM> create(CEM coefficientVector, String variableName) {
    return new AnyComplexPolynomial<>(coefficientVector, variableName);
  }

  /**
   * {@inheritDoc}
   */
  public AnyComplexRationalPolynomial<RES, REM, CES, CEM> toRational() {
    return new AnyComplexRationalPolynomial<>(this);
  }

}
