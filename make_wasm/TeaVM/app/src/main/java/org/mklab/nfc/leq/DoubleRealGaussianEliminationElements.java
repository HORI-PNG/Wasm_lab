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

/**
 * {@link DoubleRealGaussianEliminationElements} represents the result of gaussian elimination.
 * 
 * @author koga
 * @version $Revision$, 2021/08/04
 */
public class DoubleRealGaussianEliminationElements {
  /** inverse */
  private double[][] inverse;
  /** determinant */
  private double determinat;
  
  
  /**
   * Creates {@link DoubleRealGaussianEliminationElements}.
   * @param inverse inverse
   * @param determinat determinat
   */
  public DoubleRealGaussianEliminationElements(double[][] inverse, double determinat) {
    this.inverse = inverse;
    this.determinat = determinat;
  }
  
  /**
   * Returns inverse.
   * 
   * @return inverse
   */
  public double[][] getInverse() {
    return this.inverse;
  }
  
  /**
   * Returns determinant.
   * 
   * @return determinant
   */
  public double getDeterminat() {
    return this.determinat;
  }

}
