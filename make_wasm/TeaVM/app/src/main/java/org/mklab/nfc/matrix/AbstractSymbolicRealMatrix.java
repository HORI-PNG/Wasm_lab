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
package org.mklab.nfc.matrix;

import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.ComplexSymbolicScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;
import org.mklab.nfc.scalar.RealSymbolicScalar;

/**
 * @author koga
 * @version $Revision$, 2021/08/19
 * @param <RS> 実スカラーの型
 * @param <RM> 実行列の型
 *  @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RES> 実係数スカラーの型
 * @param <REM> 実係数行列の型
 * @param <CES> 複素係数スカラーの型
 * @param <CEM> 複素係数行列の型
 */
public class AbstractSymbolicRealMatrix<RS extends RealSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, RM extends RealSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>,  CS extends ComplexSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, CM extends ComplexSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, RES extends RealNumericalScalar<RES,REM,CES,CEM>, REM extends RealNumericalMatrix<RES,REM,CES,CEM>,  CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>> extends AbstractSymbolicMatrix<RS, RM, RES, REM> implements RealSymbolicMatrix<RS, RM, CS, CM, RES, REM, CES, CEM> {

  /** */
  private static final long serialVersionUID = -6405586322503896313L;

  /**
   * Creates {@link AbstractSymbolicRealMatrix}.
   * @param rowSize row size
   * @param columnSize column size
   * @param elements elements
   */
  public AbstractSymbolicRealMatrix(int rowSize, int columnSize, RS[][] elements) {
    super(rowSize, columnSize, elements);
  }

  /**
   * Creates {@link AbstractSymbolicRealMatrix}.
   * @param elements elements
   */
  public AbstractSymbolicRealMatrix(RS[][] elements) {
    super(elements);
  }

  /**
   * Creates {@link AbstractSymbolicRealMatrix}.
   * @param elements elements
   */
  public AbstractSymbolicRealMatrix(RS[] elements) {
    super(elements);
  }

  /**
   * {@inheritDoc}
   */
  public CM toComplex() {
    final RS[][] elements = getElements();
    final CS[][] complex = elements[0][0].toComplex().createArray(getRowSize(), getColumnSize());
    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        complex[i][j] = elements[i][j].toComplex();
      }
    }
    
    return complex[0][0].createGrid(complex);
  }

  /**
   * {@inheritDoc}
   */
  public CM add(CM value) {
    return toComplex().add(value);
  }
  
  /**
   * {@inheritDoc}
   */
  public CM subtract(CM value) {
    return toComplex().subtract(value);
  }

  /**
   * {@inheritDoc}
   */
  public CM multiply(CM value) {
    return toComplex().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public CM divide(CM value) {
    return toComplex().divide(value);
  }

  /**
   * {@inheritDoc}
   */
  public CM leftDivide(CM value) {
    return toComplex().leftDivide(value);
  }
  
  /**
   * {@inheritDoc}
   */
  public CM appendDown(CM value) {
    return toComplex().appendDown(value);
  }
  
  /**
   * {@inheritDoc}
   */
  public CM appendRight(CM value) {
    return toComplex().appendRight(value);
  }

  /**
   * {@inheritDoc}
   */
  public CM multiply(CS value) {
    return toComplex().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public CM divide(CS value) {
    return toComplex().divide(value);
  }

  /**
   * {@inheritDoc}
   */
  public CM leftDivide(CS value) {
    return toComplex().leftDivide(value);
  }

}
