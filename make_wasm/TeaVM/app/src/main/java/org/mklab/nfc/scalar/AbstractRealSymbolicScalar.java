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
 * @author koga
 * @version $Revision$, 2021/09/16
 * @param <RS> 実スカラーの型
 * @param <RM> 実行列の型
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RES> 実係数スカラーの型
 * @param <REM> 実係数行列の型
 * @param <CES> 複素係数スカラーの型
 * @param <CEM> 複素係数行列の型
 */
public abstract class AbstractRealSymbolicScalar<RS extends RealSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>,  RM extends RealSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, CS extends ComplexSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, CM extends ComplexSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>,   RES extends RealNumericalScalar<RES,REM,CES,CEM>, REM extends RealNumericalMatrix<RES,REM,CES,CEM>,  CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>> extends AbstractSymbolicScalar<RS, RM, RES,REM>
    implements RealSymbolicScalar<RS, RM, CS, CM, RES, REM, CES, CEM> {

  /** */
  private static final long serialVersionUID = 4815564048582164084L;

  /**
   * Creates {@link AbstractRealSymbolicScalar}.
   */
  public AbstractRealSymbolicScalar() {
    super();
  }
  
  /**
   * {@inheritDoc}
   */
  public CS add(CS value) {
    return toComplex().add(value);
  }
  
  /**
   * {@inheritDoc}
   */
  public CS subtract(CS value) {
    return toComplex().subtract(value);
  }

  /**
   * {@inheritDoc}
   */
  public CS multiply(CS value) {
    return toComplex().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public CS divide(CS value) {
    return toComplex().divide(value);
  }

  /**
   * {@inheritDoc}
   */
  public CS leftDivide(CS value) {
    return toComplex().leftDivide(value);
  }
}
