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

import org.mklab.nfc.scalar.AnyComplexPolynomial;
import org.mklab.nfc.scalar.AnyComplexRationalPolynomial;
import org.mklab.nfc.scalar.AnyRealPolynomial;
import org.mklab.nfc.scalar.AnyRealRationalPolynomial;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * @param <RES> 係数スカラーの型
 * @param <REM> 係数行列の型
 * @param <CES> 複素係数スカラーの型 
 * @param <CEM> 複素係数行列の型
 * @author koga
 * @version $Revision$, 2021/09/14
 */
public class AnyRealPolynomialMatrix<RES extends RealNumericalScalar<RES,REM,CES,CEM>, REM extends RealNumericalMatrix<RES,REM,CES,CEM>,  CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>> extends AbstractSymbolicRealMatrix<AnyRealPolynomial<RES,REM,CES,CEM>, AnyRealPolynomialMatrix<RES,REM,CES,CEM>, AnyComplexPolynomial<RES,REM,CES,CEM>, AnyComplexPolynomialMatrix<RES,REM,CES,CEM>, RES, REM, CES, CEM>
    implements RealPolynomialMatrix<AnyRealPolynomial<RES,REM,CES,CEM>, AnyRealPolynomialMatrix<RES,REM,CES,CEM>, AnyComplexPolynomial<RES,REM,CES,CEM>, AnyComplexPolynomialMatrix<RES,REM,CES,CEM>, AnyRealRationalPolynomial<RES, REM, CES, CEM>, AnyRealRationalPolynomialMatrix<RES, REM, CES, CEM>, AnyComplexRationalPolynomial<RES, REM, CES, CEM>, AnyComplexRationalPolynomialMatrix<RES, REM, CES, CEM>,RES, REM, CES, CEM> {

  /** */
  private static final long serialVersionUID = 3111344566317652594L;

  /**
   * Creates {@link AnyRealPolynomialMatrix}.
   * @param rowSize row size
   * @param columnSize column size
   * @param elements elements
   */
  public AnyRealPolynomialMatrix(int rowSize, int columnSize, AnyRealPolynomial<RES, REM, CES, CEM>[][] elements) {
    super(rowSize, columnSize, elements);
  }

  /**
   * Creates {@link AnyRealPolynomialMatrix}.
   * @param elements elements
   */
  public AnyRealPolynomialMatrix(AnyRealPolynomial<RES, REM, CES, CEM>[] elements) {
    super(elements);
  }
  
  /**
   * 新しく生成された<code>PolynomialMatrix</code>オブジェクトを初期化します。
   * 
   * @param matrix 実数行列
   */
  public AnyRealPolynomialMatrix(final REM matrix) {
    this(matrix.getRowSize(), matrix.getColumnSize(),  AnyRealPolynomialMatrixUtil.createArray(matrix.getElements()));
  }

  /**
   * {@inheritDoc}
   */
  public AnyRealRationalPolynomialMatrix<RES, REM, CES, CEM> toRational() {
    AnyRealPolynomial<RES, REM, CES, CEM>[][] elements = getElements();
    AnyRealRationalPolynomial<RES, REM, CES, CEM>[][] rationalElements = elements[0][0].toRational().createArray(getRowSize(), getColumnSize());
    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        rationalElements[i][j] = elements[i][j].toRational();
      }
    }
    
    return new AnyRealRationalPolynomialMatrix<>(rationalElements);
  }

}
