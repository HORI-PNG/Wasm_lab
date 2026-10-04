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
 * @author koga
 * @version $Revision$, 2021/10/20
 * @param <RES> real coefficient scalar
 * @param <REM> real coefficient matrix
 * @param <CES> complex coefficient scalar
 * @param <CEM> complex coefficient matrix
 */
public class AnyRealRationalPolynomial<RES extends RealNumericalScalar<RES, REM, CES, CEM>, REM extends RealNumericalMatrix<RES, REM, CES, CEM>, CES extends ComplexNumericalScalar<RES, REM, CES, CEM>, CEM extends ComplexNumericalMatrix<RES, REM, CES, CEM>>
    extends
    AbstractRealRationalPolynomial<AnyRealPolynomial<RES, REM, CES, CEM>, AnyRealPolynomialMatrix<RES, REM, CES, CEM>, AnyComplexPolynomial<RES, REM, CES, CEM>, AnyComplexPolynomialMatrix<RES, REM, CES, CEM>, AnyRealRationalPolynomial<RES, REM, CES, CEM>, AnyRealRationalPolynomialMatrix<RES, REM, CES, CEM>, AnyComplexRationalPolynomial<RES, REM, CES, CEM>, AnyComplexRationalPolynomialMatrix<RES, REM, CES, CEM>, RES, REM, CES, CEM> {

  /** */
  private static final long serialVersionUID = -5919014169578337970L;

  /**
   * Creates {@link AnyRealRationalPolynomial}.
   * 
   * @param numerator numerator
   * @param denominator denominator
   */
  public AnyRealRationalPolynomial(AnyRealPolynomial<RES, REM, CES, CEM> numerator, AnyRealPolynomial<RES, REM, CES, CEM> denominator) {
    super(numerator, denominator);
  }

  /**
   * Creates {@link AnyRealRationalPolynomial}.
   * 
   * @param numerator numerator
   * @param denominator denominator
   */
  public AnyRealRationalPolynomial(AnyRealPolynomial<RES, REM, CES, CEM> numerator, double denominator) {
    super(numerator, denominator);
  }

  /**
   * Creates {@link AnyRealRationalPolynomial}.
   * 
   * @param numerator numerator
   * @param denominator denominator
   */
  public AnyRealRationalPolynomial(AnyRealPolynomial<RES, REM, CES, CEM> numerator, RES denominator) {
    super(numerator, denominator);
  }

  /**
   * Creates {@link AnyRealRationalPolynomial}.
   * 
   * @param numerator numerator
   */
  public AnyRealRationalPolynomial(AnyRealPolynomial<RES, REM, CES, CEM> numerator) {
    super(numerator);
  }

  /**
   * Creates {@link AnyRealRationalPolynomial}.
   * 
   * @param numerator numerator
   * @param denominator denominator
   */
  public AnyRealRationalPolynomial(double numerator, AnyRealPolynomial<RES, REM, CES, CEM> denominator) {
    super(numerator, denominator);
  }

  /**
   * Creates {@link AnyRealRationalPolynomial}.
   * 
   * @param numerator numerator
   * @param denominator denominator
   */
  public AnyRealRationalPolynomial(RES numerator, AnyRealPolynomial<RES, REM, CES, CEM> denominator) {
    super(numerator, denominator);
  }

  /**
   * {@inheritDoc}
   */
  public CEM getZeros() {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public CEM getPoles() {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyComplexRationalPolynomial<RES, REM, CES, CEM> toComplex() {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomialMatrix<RES, REM, CES, CEM> createGrid(int rowSize, int columnSize, AnyRealRationalPolynomial<RES, REM, CES, CEM>[][] elements) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomialMatrix<RES, REM, CES, CEM> createGrid(AnyRealRationalPolynomial<RES, REM, CES, CEM>[] elements) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomial<RES, REM, CES, CEM> createUnit() {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomial<RES, REM, CES, CEM> create(int value) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomial<RES, REM, CES, CEM> create(double value) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomial<RES, REM, CES, CEM> createZero() {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomial<RES, REM, CES, CEM>[] createArray(int size) {
    return new AnyRealRationalPolynomial[size];
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomial<RES, REM, CES, CEM>[][] createArray(int rowSize, int columnSize) {
    return new AnyRealRationalPolynomial[rowSize][columnSize];
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomial<RES, REM, CES, CEM> create(double numerator, AnyRealPolynomial<RES, REM, CES, CEM> denominator) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomial<RES, REM, CES, CEM> create(RES numerator, AnyRealPolynomial<RES, REM, CES, CEM> denominator) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomial<RES, REM, CES, CEM> create(AnyRealPolynomial<RES, REM, CES, CEM> numerator, AnyRealPolynomial<RES, REM, CES, CEM> denominator) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomial<RES, REM, CES, CEM> create(AnyRealPolynomial<RES, REM, CES, CEM> numerator, double denominator) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomial<RES, REM, CES, CEM> create(AnyRealPolynomial<RES, REM, CES, CEM> numerator, RES denominator) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomial<RES, REM, CES, CEM> create(AnyRealPolynomial<RES, REM, CES, CEM> numerator) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomial<RES, REM, CES, CEM> create(int numerator, String variableName) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomial<RES, REM, CES, CEM> create(double numerator, String variableName) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomial<RES, REM, CES, CEM> create(RES numerator) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomial<RES, REM, CES, CEM> create(RES numerator, String variableName) {
    throw new UnsupportedOperationException();
  }

//  /**
//   * {@inheritDoc}
//   */
//  public AnyComplexRationalPolynomial<RES, REM, CES, CEM> add(AnyComplexRationalPolynomial<RES, REM, CES, CEM> value) {
//    return toComplex().add(value);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public AnyComplexRationalPolynomial<RES, REM, CES, CEM> subtract(AnyComplexRationalPolynomial<RES, REM, CES, CEM> value) {
//    return toComplex().subtract(value);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public AnyComplexRationalPolynomial<RES, REM, CES, CEM> multiply(AnyComplexRationalPolynomial<RES, REM, CES, CEM> value) {
//    return toComplex().multiply(value);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public AnyComplexRationalPolynomial<RES, REM, CES, CEM> divide(AnyComplexRationalPolynomial<RES, REM, CES, CEM> value) {
//    return toComplex().divide(value);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public AnyComplexRationalPolynomial<RES, REM, CES, CEM> leftDivide(AnyComplexRationalPolynomial<RES, REM, CES, CEM> value) {
//    return toComplex().leftDivide(value);
//  }

}
