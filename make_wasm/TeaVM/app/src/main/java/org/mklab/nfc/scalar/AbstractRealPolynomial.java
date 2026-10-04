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
import org.mklab.nfc.matrix.misc.CompanionMatrix;

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
 * 
 * @author koga
 * @version $Revision$, 2021/09/13
 */
public abstract class AbstractRealPolynomial<RPS extends RealPolynomial<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, RPM extends RealPolynomialMatrix<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, CPS extends ComplexPolynomial<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, CPM extends ComplexPolynomialMatrix<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, RRS extends RealRationalPolynomial<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, RRM extends RealRationalPolynomialMatrix<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, CRS extends ComplexRationalPolynomial<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, CRM extends ComplexRationalPolynomialMatrix<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, RES extends RealNumericalScalar<RES,REM,CES,CEM>,REM extends RealNumericalMatrix<RES,REM,CES,CEM>, CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>> extends AbstractPolynomial<RPS, RPM, RRS, RRM, RES, REM> implements RealPolynomial<RPS, RPM, CPS, CPM, RRS,RRM,CRS,CRM,RES, REM, CES, CEM> {

  /** */
  private static final long serialVersionUID = 4446394166688155591L;

  /**
   * Creates {@link AbstractRealPolynomial}.
   * @param coefficientVector 係数をもつベクトル(行列)
   * @param variableName 多項式変数
   */
  public AbstractRealPolynomial(REM coefficientVector, String variableName) {
    super(coefficientVector, variableName);
  }

  /**
   * Creates {@link AbstractRealPolynomial}.
   * @param coefficientVector 係数をもつベクトル(行列)
   */
  public AbstractRealPolynomial(REM coefficientVector) {
    super(coefficientVector);
  }

  /**
   * Creates {@link AbstractRealPolynomial}.
   * @param constant 0次の係数
   * @param variableName 多項式変数
   */
  public AbstractRealPolynomial(RES constant, String variableName) {
    super(constant, variableName);
  }

  /**
   * Creates {@link AbstractRealPolynomial}.
   * @param constant 0次の係数
   */
  public AbstractRealPolynomial(RES constant) {
    super(constant);
  }

  /**
   * Creates {@link AbstractRealPolynomial}.
   * @param coefficients 係数の配列
   * @param variableName 多項式変数
   */
  public AbstractRealPolynomial(RES[] coefficients, String variableName) {
    super(coefficients, variableName);
  }

  /**
   * Creates {@link AbstractRealPolynomial}.
   * @param coefficients 係数の配列
   */
  public AbstractRealPolynomial(RES[] coefficients) {
    super(coefficients);
  }

  /**
   * {@inheritDoc}
   */
  public final CEM getRoots() {
    int nonZeroCoefficientSize;
    REM m =getCoefficients();

    for (nonZeroCoefficientSize = m.getColumnSize(); nonZeroCoefficientSize > 0; nonZeroCoefficientSize--) {
      final RES c = m.getElement(1, nonZeroCoefficientSize);
      if (c.isUnit()) {
        break;
      } else if (c.isZero() == false) {
        m = m.multiply(c.inverse());
        break;
      }
    }

    if (nonZeroCoefficientSize == 0) {
      throw new RuntimeException(Messages.getString("Polynomial.11")); //$NON-NLS-1$
    }

    if (nonZeroCoefficientSize == 1) {
      final RES c = m.getElement(1, 1);
      return c.createGrid(c.createArray(0)).toComplex();
    }

    final REM companionMatrix = CompanionMatrix.create(m.getSubVector(1, nonZeroCoefficientSize - 1));
    return companionMatrix.eigenValue();
  }
  
  /**
   * {@inheritDoc}
   */
  public RPS leftDivide(int value) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public RPS leftDivide(double value) {
    throw new UnsupportedOperationException();
  }
  

  /**
   * {@inheritDoc}
   */
  public RPS  divide(RPS value) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public RPS leftDivide(RPS value) {
    throw new UnsupportedOperationException();
  }


  /**
   * {@inheritDoc}
   */
  public RPS inverse() {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public RPS create(int value) {
    RPS ans = createZero();
    ans.setCoefficient(0, value);
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public RPS create(double value) {
    RPS ans = createZero();
    ans.setCoefficient(0, value);
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public RPS create(double constant, String variableName) {
    RPS ans = createZero();
    ans.setCoefficient(0, constant);
    ans.setVariable(variableName);
    return ans;
  }
  

  /**
   * {@inheritDoc}
   */
  public CPS add(CPS value) {
    return toComplex().add(value);
  }

  /**
   * {@inheritDoc}
   */
  public CPS subtract(CPS value) {
    return toComplex().subtract(value);
  }

  /**
   * {@inheritDoc}
   */
  public CPS multiply(CPS value) {
    return toComplex().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public CPS divide(CPS value) {
    return toComplex().divide(value);
  }

  /**
   * {@inheritDoc}
   */
  public CPS leftDivide(CPS value) {
    return toComplex().leftDivide(value);
  }


//  /**
//   * {@inheritDoc}
//   */
//  public RS create(String variableName) {
//    RS ans = createZero();
//    ans.setVariable(variableName);
//    return ans;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public RS create(RES constant) {
//    RS ans = createZero();
//    ans.setCoefficient(0, constant);
//    return ans;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public RS create(RES constant, String variableName) {
//    RS ans = createZero();
//    ans.setCoefficient(0, constant);
//    ans.setVariable(variableName);
//    return ans;
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public RS create(double[] coefficients) {
//    RS ans  = createZero().expand(coefficients.length-1);
//    for (int i = 0; i < coefficients.length; i++) {
//      ans.setCoefficient(i, coefficients[i]);
//    }
//    return ans;
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public RS create(double[] coefficients, String variableName) {
//    RS ans  = createZero().expand(coefficients.length-1);
//    ans.setVariable(variableName);
//    for (int i = 0; i < coefficients.length; i++) {
//      ans.setCoefficient(i, coefficients[i]);
//    }
//    return ans;
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public RS create(RES[] coefficients) {
//    RS ans  = createZero().expand(coefficients.length-1);
//    for (int i = 0; i < coefficients.length; i++) {
//      ans.setCoefficient(i, coefficients[i]);
//    }
//    return ans;
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public RS create(RES[] coefficients, String variableName) {
//    RS ans  = createZero().expand(coefficients.length-1);
//    ans.setVariable(variableName);
//    for (int i = 0; i < coefficients.length; i++) {
//      ans.setCoefficient(i, coefficients[i]);
//    }
//    return ans;
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public RS create(REM coefficientVector) {
//    RS ans  = createZero().expand(coefficientVector.length()-1);
//    for (int i = 0; i < coefficientVector.length(); i++) {
//      ans.setCoefficient(i, coefficientVector.getElement(i+1));
//    }
//    return ans;
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public RS create(REM coefficientVector, String variableName) {
//    RS ans  = createZero().expand(coefficientVector.length()-1);
//    ans.setVariable(variableName);
//    for (int i = 0; i < coefficientVector.length(); i++) {
//      ans.setCoefficient(i, coefficientVector.getElement(i+1));
//    }
//    return ans;
//  }
}
