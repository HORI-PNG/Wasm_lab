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
import org.mklab.nfc.matrix.ComplexPolynomialMatrix;
import org.mklab.nfc.matrix.ComplexRationalPolynomialMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.matrix.RealPolynomialMatrix;
import org.mklab.nfc.matrix.RealRationalPolynomialMatrix;

/**
 * @param <RPS> 実多項式の型
 * @param <RPM> 実多項式行列の型
 * @param <CPS> 複素多項式の型
 * @param <CPM> 複素多項式行列の型
 * @param <RRS> 実有理多項式の型
 * @param <RRM> 実有理多項式行列の型
 * @param <CRS> 複素有理多項式の型 
 * @param <CRM> 複素有理多項式行列の型
 * @param <RES> 実係数スカラーの型
 * @param <REM> 実係数行列の型
 * @param <CES> 複素係数スカラーの型 
 * @param <CEM> 複素係数行列の型
 * @author koga
 * @version $Revision$, 2021/09/18
 */
public abstract class AbstractComplexRationalPolynomial<RPS extends RealPolynomial<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, RPM extends RealPolynomialMatrix<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, CPS extends ComplexPolynomial<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, CPM extends ComplexPolynomialMatrix<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, RRS extends RealRationalPolynomial<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, RRM extends RealRationalPolynomialMatrix<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, CRS extends ComplexRationalPolynomial<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, CRM extends ComplexRationalPolynomialMatrix<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, RES extends RealNumericalScalar<RES,REM,CES,CEM>,REM extends RealNumericalMatrix<RES,REM,CES,CEM>, CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>> extends AbstractRationalPolynomial<CPS, CPM, CRS, CRM, CES, CEM> implements ComplexRationalPolynomial<RPS, RPM, CPS, CPM, RRS, RRM, CRS, CRM, RES, REM, CES, CEM> {

  /** */
  private static final long serialVersionUID = -3503360850392775098L;

  /**
   * Creates {@link AbstractComplexRationalPolynomial}.
   * @param numerator numerator
   * @param denominator denominator
   */
  public AbstractComplexRationalPolynomial(CES numerator, CPS denominator) {
    super(numerator, denominator);
  }

  /**
   * Creates {@link AbstractComplexRationalPolynomial}.
   * @param numerator numerator
   * @param denominator denominator
   */
  public AbstractComplexRationalPolynomial(CPS numerator, CES denominator) {
    super(numerator, denominator);
  }

  /**
   * Creates {@link AbstractComplexRationalPolynomial}.
   * @param numerator numerator
   * @param denominator denominator
   */
  public AbstractComplexRationalPolynomial(CPS numerator, CPS denominator) {
    super(numerator, denominator);
  }

  /**
   * Creates {@link AbstractComplexRationalPolynomial}.
   * @param numerator numerator
   * @param denominator denominator
   */
  public AbstractComplexRationalPolynomial(CPS numerator, double denominator) {
    super(numerator, denominator);
  }

  /**
   * Creates {@link AbstractComplexRationalPolynomial}.
   * @param numerator numerator
   */
  public AbstractComplexRationalPolynomial(CPS numerator) {
    super(numerator);
  }

  /**
   * Creates {@link AbstractComplexRationalPolynomial}.
   * @param numerator numerator
   * @param denominator denominator
   */
  public AbstractComplexRationalPolynomial(double numerator, CPS denominator) {
    super(numerator, denominator);
  }

  /**
   * {@inheritDoc}
   */
  public CRS add(RRS value) {
    return add(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CRS subtract(RRS value) {
    return subtract(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CRS multiply(RRS value) {
    return multiply(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CRS divide(RRS value) {
    return divide(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CRS leftDivide(RRS value) {
    return leftDivide(value.toComplex());
  }
}
