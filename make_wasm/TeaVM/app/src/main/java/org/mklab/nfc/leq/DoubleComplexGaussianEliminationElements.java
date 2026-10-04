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
 * {@link DoubleComplexGaussianEliminationElements} represents the result of gaussian elimination.
 * 
 * @author koga
 * @version $Revision$, 2021/08/04
 */
public class DoubleComplexGaussianEliminationElements {
  /** real part of inverse */
  private double[][] realInverse;
  /** imaginary part of inverse */
  private double[][] imaginaryInverse;
  /** real part of determinant */
  private double realDeterminat;
  /** imaginary part of determinant */
  private double imaginaryDeterminat;
  
  
  /**
   * Creates {@link DoubleComplexGaussianEliminationElements}.
   * 
   * @param realInverse real part of inverse
   * @param imaginaryInverse imaginary part of inverse
   * @param realDeterminat real part of determinant
   * @param imaginaryDeterminat imaginary part of determinant
   */
  public DoubleComplexGaussianEliminationElements(double[][] realInverse, double[][] imaginaryInverse, double realDeterminat, double imaginaryDeterminat) {
    this.realInverse = realInverse;
    this.imaginaryInverse = imaginaryInverse;
    this.realDeterminat = realDeterminat;
    this.imaginaryDeterminat = imaginaryDeterminat;
  }
  
  /**
   * Returns real part of inverse.
   * 
   * @return real part of inverse
   */
  public double[][] getRealInverse() {
    return this.realInverse;
  }

  /**
   * Returns real imaginary of inverse.
   * 
   * @return imaginary part of inverse
   */
  public double[][] getImaginaryInverse() {
    return this.imaginaryInverse;
  }

  /**
   * Returns real part of determinant.
   * 
   * @return real part of determinant
   */
  public double getRealDeterminat() {
    return this.realDeterminat;
  }

  /**
   * Returns imaginary part of determinant.
   * 
   * @return imaginary part of determinant
   */
  public double getImaginaryDeterminat() {
    return this.imaginaryDeterminat;
  }

}
