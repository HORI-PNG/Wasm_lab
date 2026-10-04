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

import java.nio.channels.UnsupportedAddressTypeException;

import org.mklab.nfc.scalar.AnyComplexPolynomial;
import org.mklab.nfc.scalar.AnyComplexRationalPolynomial;
import org.mklab.nfc.scalar.AnyRealPolynomial;
import org.mklab.nfc.scalar.AnyRealRationalPolynomial;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;

/**
 * @author koga
 * @version $Revision$, 2021/10/20
  * @param <RES> real coefficient scalar
 * @param <REM> real coefficient matrix 
 * @param <CES> complex coefficient scalar
 * @param <CEM> complex coefficient matrix 
*/
public class AnyComplexRationalPolynomialMatrix<RES extends RealNumericalScalar<RES, REM, CES, CEM>, REM extends RealNumericalMatrix<RES, REM, CES, CEM>, CES extends ComplexNumericalScalar<RES, REM, CES, CEM>, CEM extends ComplexNumericalMatrix<RES, REM, CES, CEM>> extends AbstractSymbolicComplexMatrix<AnyRealRationalPolynomial<RES,REM,CES,CEM>, AnyRealRationalPolynomialMatrix<RES,REM,CES,CEM>, AnyComplexRationalPolynomial<RES,REM,CES,CEM>, AnyComplexRationalPolynomialMatrix<RES,REM,CES,CEM>, RES, REM, CES, CEM> 
implements ComplexRationalPolynomialMatrix<AnyRealPolynomial<RES,REM,CES,CEM>, AnyRealPolynomialMatrix<RES,REM,CES,CEM>, AnyComplexPolynomial<RES,REM,CES,CEM>, AnyComplexPolynomialMatrix<RES,REM,CES,CEM>, AnyRealRationalPolynomial<RES,REM,CES,CEM>, AnyRealRationalPolynomialMatrix<RES,REM,CES,CEM>, AnyComplexRationalPolynomial<RES,REM,CES,CEM>, AnyComplexRationalPolynomialMatrix<RES,REM,CES,CEM>, RES, REM, CES, CEM> {

  /** */
  private static final long serialVersionUID = 8032383605790483445L;

  /**
   * Creates {@link AnyComplexRationalPolynomialMatrix}.
   * @param elements elements
   */
  public AnyComplexRationalPolynomialMatrix(AnyComplexRationalPolynomial<RES, REM, CES, CEM>[] elements) {
    super(elements);
  }

  /**
   * Creates {@link AnyComplexRationalPolynomialMatrix}.
   * @param elements elements
   */
  public AnyComplexRationalPolynomialMatrix(AnyComplexRationalPolynomial<RES, REM, CES, CEM>[][] elements) {
    super(elements);
  }

  /**
   * Creates {@link AnyComplexRationalPolynomialMatrix}.
   * @param rowSize row size
   * @param columnSize column size
   * @param elements elements
   */
  public AnyComplexRationalPolynomialMatrix(int rowSize, int columnSize, AnyComplexRationalPolynomial<RES, REM, CES, CEM>[][] elements) {
    super(rowSize, columnSize, elements);
  }

  /**
   * {@inheritDoc}
   */
  public AnyComplexRationalPolynomialMatrix<RES, REM, CES, CEM> multiply(AnyComplexPolynomial<RES, REM, CES, CEM> polynomial) {
    throw new UnsupportedAddressTypeException();
  }

  /**
   * {@inheritDoc}
   */
  public AnyComplexRationalPolynomialMatrix<RES, REM, CES, CEM> divide(AnyComplexPolynomial<RES, REM, CES, CEM> polynomial) {
    throw new UnsupportedAddressTypeException();
  } 

}
