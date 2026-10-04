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
 * @version $Revision$, 2021/08/20
 */
public abstract class AbstractComplexPolynomial<RPS extends RealPolynomial<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, RPM extends RealPolynomialMatrix<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, CPS extends ComplexPolynomial<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, CPM extends ComplexPolynomialMatrix<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, RRS extends RealRationalPolynomial<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, RRM extends RealRationalPolynomialMatrix<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, CRS extends ComplexRationalPolynomial<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, CRM extends ComplexRationalPolynomialMatrix<RPS,RPM,CPS,CPM,RRS,RRM,CRS,CRM,RES,REM,CES,CEM>, RES extends RealNumericalScalar<RES,REM,CES,CEM>,REM extends RealNumericalMatrix<RES,REM,CES,CEM>, CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>>
    extends AbstractPolynomial<CPS, CPM, CRS, CRM, CES, CEM> implements ComplexPolynomial<RPS, RPM, CPS, CPM, RRS,RRM,CRS,CRM,RES, REM, CES, CEM> {

  /** */
  private static final long serialVersionUID = 3361427335679951317L;

  /**
   * Creates {@link AbstractComplexPolynomial}.
   * 
   * @param coefficientVector 係数をもつベクトル(行列)
   * @param variableName 多項式変数
   */
  public AbstractComplexPolynomial(CEM coefficientVector, String variableName) {
    super(coefficientVector, variableName);
  }

  /**
   * Creates {@link AbstractComplexPolynomial}.
   * 
   * @param coefficientVector 係数をもつベクトル(行列)
   */
  public AbstractComplexPolynomial(CEM coefficientVector) {
    super(coefficientVector);
  }

  /**
   * Creates {@link AbstractComplexPolynomial}.
   * 
   * @param constant 0次の係数
   * @param variableName 多項式変数
   */
  public AbstractComplexPolynomial(CES constant, String variableName) {
    super(constant, variableName);
  }

  /**
   * Creates {@link AbstractComplexPolynomial}.
   * 
   * @param constant 0次の係数
   */
  public AbstractComplexPolynomial(CES constant) {
    super(constant);
  }

  /**
   * Creates {@link AbstractComplexPolynomial}.
   * 
   * @param coefficients 係数の配列
   * @param variableName 多項式変数
   */
  public AbstractComplexPolynomial(CES[] coefficients, String variableName) {
    super(coefficients, variableName);
  }

  /**
   * Creates {@link AbstractComplexPolynomial}.
   * 
   * @param coefficients 係数の配列
   */
  public AbstractComplexPolynomial(CES[] coefficients) {
    super(coefficients);
  }

  /**
   * {@inheritDoc}
   */
  public final CEM getRoots() {
    int nonZeroCoefficientSize;
    CEM m =getCoefficients();

    for (nonZeroCoefficientSize = m.getColumnSize(); nonZeroCoefficientSize > 0; nonZeroCoefficientSize--) {
      final CES c = m.getElement(1, nonZeroCoefficientSize);
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
      final CES c = m.getElement(1, 1);
      return c.createGrid(c.createArray(0));
    }

    final CEM companionMatrix = CompanionMatrix.create(m.getSubVector(1, nonZeroCoefficientSize - 1));
    return companionMatrix.eigenValue();
  }
  
  /**
   * {@inheritDoc}
   */
  public void setRealPart(int realPart) {
    setRealPart(getRealPart().create(realPart));
  }

  /**
   * {@inheritDoc}
   */
  public void setRealPart(double realPart) {
    setRealPart(getRealPart().create(realPart));
  }

  /**
   * {@inheritDoc}
   */
  public void setImaginaryPart(int imagPart) {
    setImaginaryPart(getImaginaryPart().create(imagPart));
  }

  /**
   * {@inheritDoc}
   */
  public void setImaginaryPart(double imagPart) {
    setImaginaryPart(getImaginaryPart().create(imagPart));
  }
  
  /**
   * {@inheritDoc}
   */
  public void setRealPart(final RPS realPart) {
    if (getDegree() < realPart.getDegree()) {
      final CEM expandedCoefficients = expand(realPart.getDegree()).getCoefficients();
      expandedCoefficients.setRealPart(realPart.getCoefficients());
      setCoefficients(expandedCoefficients);
    } else if (getDegree() == realPart.getDegree()) {
      final CEM coefficients = getCoefficients();
      coefficients.setRealPart(realPart.getCoefficients());
      setCoefficients(coefficients);
    } else {
      final REM expandedRealPart = realPart.expand(getDegree()).getCoefficients();
      final CEM coefficients = getCoefficients();
      coefficients.setRealPart(expandedRealPart);
      setCoefficients(coefficients);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void setImaginaryPart(final RPS imaginaryPart) {
    if (getDegree() < imaginaryPart.getDegree()) {
      final CEM expandedCoefficients = expand(imaginaryPart.getDegree()).getCoefficients();
      expandedCoefficients.setImaginaryPart(imaginaryPart.getCoefficients());
      setCoefficients(expandedCoefficients);
    } else if (getDegree() == imaginaryPart.getDegree()) {
      final CEM coefficients = getCoefficients();
      coefficients.setImaginaryPart(imaginaryPart.getCoefficients());
      setCoefficients(coefficients);
    } else {
      final REM expandedRealPart = imaginaryPart.expand(getDegree()).getCoefficients();
      final CEM coefficients = getCoefficients();
      coefficients.setImaginaryPart(expandedRealPart);
      setCoefficients(coefficients);
    }
  }
  

  /**
   * {@inheritDoc}
   */
  public CPS leftDivide(int value) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public CPS leftDivide(double value) {
    throw new UnsupportedOperationException();
  }
  

  /**
   * {@inheritDoc}
   */
  public CPS  divide(CPS value) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public CPS leftDivide(CPS value) {
    throw new UnsupportedOperationException();
  }


  /**
   * {@inheritDoc}
   */
  public CPS inverse() {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public CPS add(RPS value) {
    return add(value.toComplex());
  }
  
  /**
   * {@inheritDoc}
   */
  public CPS subtract(RPS value) {
    return subtract(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CPS multiply(RPS value) {
    return multiply(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CPS divide(RPS value) {
    return divide(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CPS leftDivide(RPS value) {
    return leftDivide(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CPS create(int constant) {
    CPS ans  = createZero();
    ans.setCoefficient(0, constant);
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public CPS create(double constant) {
    CPS ans  = createZero();
    ans.setCoefficient(0, constant);
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public CPS create(double constant, String variableName) {
    CPS ans  = createZero();
    ans.setCoefficient(0, constant);
    ans.setVariable(variableName);
    return ans;
  }

//  /**
//   * {@inheritDoc}
//   */
//  public CS create(String variableName) {
//    CS ans  = createZero();
//    ans.setVariable(variableName);
//    return ans;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public CS create(CES constant) {
//    CS ans  = createZero();
//    ans.setCoefficient(0, constant);
//    return ans;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public CS create(CES constant, String variableName) {
//    CS ans  = createZero();
//    ans.setCoefficient(0, constant);
//    ans.setVariable(variableName);
//    return ans;
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public CS create(double[] coefficients) {
//    CS ans  = createZero().expand(coefficients.length-1);
//    for (int i = 0; i < coefficients.length; i++) {
//      ans.setCoefficient(i, coefficients[i]);
//    }
//    return ans;
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public CS create(double[] coefficients, String variableName) {
//    CS ans  = createZero().expand(coefficients.length-1);
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
//  public CS create(CES[] coefficients) {
//    CS ans  = createZero().expand(coefficients.length-1);
//    for (int i = 0; i < coefficients.length; i++) {
//      ans.setCoefficient(i, coefficients[i]);
//    }
//    return ans;
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public CS create(CES[] coefficients, String variableName) {
//    CS ans  = createZero().expand(coefficients.length-1);
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
//  public CS create(CEM coefficientVector) {
//    CS ans  = createZero().expand(coefficientVector.length()-1);
//    for (int i = 0; i < coefficientVector.length(); i++) {
//      ans.setCoefficient(i, coefficientVector.getElement(i+1));
//    }
//    return ans;
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public CS create(CEM coefficientVector, String variableName) {
//    CS ans  = createZero().expand(coefficientVector.length()-1);
//    ans.setVariable(variableName);
//    for (int i = 0; i < coefficientVector.length(); i++) {
//      ans.setCoefficient(i, coefficientVector.getElement(i+1));
//    }
//    return ans;
//  }

  /**
   * {@inheritDoc}
   */
  public CPS create(RPS rePart, RPS imPart) {
    int degree = Math.max(rePart.getDegree(), imPart.getDegree());
    REM re = rePart.expand(degree).getCoefficients();
    REM im = imPart.expand(degree).getCoefficients();
    return create(re.createComplex(re, im));
  }

  /**
   * {@inheritDoc}
   */
  public CPS create(RPS rePart) {
    return create(rePart, rePart.createZero());
  }
  
  /**
   * {@inheritDoc}
   */
  public CPS create(double[] coefficients) {
    CES ces = getCoefficient(0);
    RES res = ces.getRealPart();
    
    int size = coefficients.length;
    CES[] coef = ces.createArray(size);
    for (int i = 0; i < size; i++) {
      coef[i] =  res.create(coefficients[i]).toComplex();
    }

    return create(coef);
  }

  /**
   * {@inheritDoc}
   */
  public CPS create(double[] coefficients, String variableName) {
    CES ces = getCoefficient(0);
    RES res = ces.getRealPart();

    int size = coefficients.length;
    CES[] coef = ces.createArray(size);
    for (int i = 0; i < size; i++) {
      coef[i] = res.create(coefficients[i]).toComplex();
    }

    return create(coef, variableName);
  }

}
