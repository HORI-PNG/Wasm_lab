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
package org.mklab.nfc.leq;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;

/**
 * {@link GaussianEliminationElements} represents the result of gaussian elimination.
 * 
 * @author koga
 * @version $Revision$, 2021/08/04
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public class GaussianEliminationElements<S extends Scalar<S,M>, M extends Matrix<S,M>> {
  /** inverse */
  private S[][] inverse;
  /** determinant */
  private S determinat;
  
  
  /**
   * Creates {@link GaussianEliminationElements}.
   * @param inverse inverse
   * @param determinat determinat
   */
  public GaussianEliminationElements(S[][] inverse, S determinat) {
    this.inverse = inverse;
    this.determinat = determinat;
  }
  
  /**
   * Returns inverse.
   * 
   * @return inverse
   */
  public S[][] getInverse() {
    return this.inverse;
  }
  
  /**
   * Returns determinant.
   * 
   * @return determinant
   */
  public S getDeterminat() {
    return this.determinat;
  }

}
