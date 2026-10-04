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

import org.mklab.nfc.matrix.AbstractNumericalComplexMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;

/**
 * 実数値スカラーを表すクラスです。
 * 
 * @author koga
 * @version $Revision$, 2021/08/31
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 */
public abstract class AbstractRealNumericalScalar<RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> extends AbstractNumericalScalar<RS, RM> implements RealNumericalScalar<RS, RM, CS, CM> {
  /** */
  private static final long serialVersionUID = -1588477115122111423L;

  /**
   * {@inheritDoc}
   */
  public CS[] createComplexArray(RS[] realPart, RS[] imagPart) {
    int size = realPart.length;
    CS[] ans =  realPart[0].toComplex().createArray(size);
    for (int i = 0; i < size; i++) {
      ans[i] = realPart[0].toComplex().create(realPart[i], imagPart[i]);
    }

    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public CS[] createComplexArray(RS[] realPart) {
    int size = realPart.length;
    CS[] ans = realPart[0].toComplex().createArray(size);
    for (int i = 0; i < size; i++) {
      ans[i] = realPart[0].toComplex().create(realPart[i]);
    }

    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public CS[][] createComplexArray(RS[][] realPart, RS[][] imagPart) {
    int rowSize = realPart.length;
    int columnSize = realPart[0].length;
    CS[][] ans = realPart[0][0].toComplex().createArray(rowSize,columnSize);
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = realPart[0][0].toComplex().create(realPart[i][j], imagPart[i][j]);
      }
    }

    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public CS[][] createComplexArray(RS[][] realPart) {
    int rowSize = realPart.length;
    int columnSize = realPart[0].length;
    CS[][] ans = realPart[0][0].toComplex().createArray(rowSize,columnSize);
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = realPart[0][0].toComplex().create(realPart[i][j]);
      }
    }

    return ans;
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
