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
package org.mklab.nfc.matx;

import org.mklab.nfc.scalar.DoubleComplexNumber;


/**
 * @author koga
 * @version $Revision$, 2021/08/23
 */
public class MatxComplex extends DoubleComplexNumber {

  /** */
  private static final long serialVersionUID = -1538098037824392195L;

  /**
   * Creates {@link MatxComplex}.
   * @param realPart real part
   * @param imagPart imaginary part
   */
  public MatxComplex(double realPart, double imagPart) {
    super(realPart, imagPart);
  }
  
  /**
   * Returns result of addition.
   * 
   * @param value1 value 1
   * @param value2 value 2
   * @return result of addition
   */
  public static DoubleComplexNumber add(double value1, DoubleComplexNumber value2) {
    return value2.add(value1);
  }
  
  /**
   * Returns result of subtraction..
   * 
   * @param value1 value 1
   * @param value2 value 2
   * @return result of subtraction.
   */
  public static DoubleComplexNumber subtract(double  value1, DoubleComplexNumber value2) {
    return value2.subtract(value1);
  }

  /**
   * Returns result of division.
   * 
   * @param value1 value 1
   * @param value2 value 2
   * @return result of division.
   */
  public static DoubleComplexNumber multiply(double  value1, DoubleComplexNumber value2) {
    return value2.multiply(value1);
  }

  /**
   * Returns result of division.
   * 
   * @param value1 value 1
   * @param value2 value 2
   * @return result of division.
   */
  public static DoubleComplexNumber divide(double  value1, DoubleComplexNumber value2) {
    return value2.leftDivide(value1);
  }

  /**
   * Returns result of division from left.
   * 
   * @param value1 value 1
   * @param value2 value 2
   * @return result of division from left.
   */
  public static DoubleComplexNumber leftDivide(double  value1, DoubleComplexNumber value2) {
    return value2.divide(value1);
  }

  /**
   * Returns result of power.
   * 
   * @param value1 value 1
   * @param value2 value 2
   * @return result of power.
   */
  public static DoubleComplexNumber power(double  value1, DoubleComplexNumber value2) {
    return new DoubleComplexNumber(value1).power(value2);
  }

}
