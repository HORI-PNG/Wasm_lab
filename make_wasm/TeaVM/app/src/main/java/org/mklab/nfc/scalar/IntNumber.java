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

import org.mklab.nfc.matrix.IntMatrix;


/**
 * @author koga
 * @version $Revision$, 2021/07/30
 */
public class IntNumber extends AbstractScalar<IntNumber,IntMatrix> {

  /** */
  private static final long serialVersionUID = 6297792620762511427L;
  
  /** data. */
  private int data;

  /**
   * Creates {@link IntNumber}.
   * 
   * @param value data 
   */
  public IntNumber(final int value) {
    this.data = value;
  }

  /**
   * Returns int value.
   * 
   * @return int value
   */
  public int intValue() {
    return this.data;
  }
  
  /**
   * {@inheritDoc}
   */
  public IntMatrix createGrid(int rowSize, int columnSize, IntNumber[][] elements) {
    final int[][] realElements = new int[rowSize][columnSize];
    for (int row = 0; row < rowSize; row++) {
      for (int column = 0; column < columnSize; column++) {
        realElements[row][column] = elements[row][column].intValue();
      }
    }

    return new IntMatrix(realElements);
  }

  /**
   * {@inheritDoc}
   */
  public IntMatrix createGrid(IntNumber[] elements) {
    final int[] realElements = new int[elements.length];
    for (int row = 0; row < elements.length; row++) {
      realElements[row] = elements[row].intValue();
    }

    return new IntMatrix(realElements);
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber add(int value) {
    return new IntNumber(this.data + value);
  }

  /**
   * Returns result of addition.
   * 
   * @param value value
   * @return result of addition
   */
  public IntNumber add(IntNumber value) {
    return add(value.data);
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber subtract(int value) {
    return new IntNumber(this.data - value);
  }

  /**
   * Returns result of subtract.
   * 
   * @param value value
   * @return result of subtract.
   */
  public IntNumber subtract(IntNumber value) {
    return subtract(value.data);
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber multiply(int value) {
    return new IntNumber(this.data * value);
  }

  /**
   * Returns result of multiplication.
   * 
   * @param value value
   * @return result of multiplication
   */
  public IntNumber multiply(IntNumber value) {
    return multiply(value.data);
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber divide(int value) {
    return new IntNumber(this.data / value);
  }
  
  /**
   * Returns result of division.
   * 
   * @param value value
   * @return result of division
   */
  public IntNumber remainder(IntNumber value) {
    return remainder(value.data);
  }

  /**
   * Returns result of division.
   * 
   * @param value value
   * @return result of division
   */
  public IntNumber remainder(int value) {
    return new IntNumber(this.data % value);
  }

  /**
   * Returns result of division.
   * 
   * @param value value
   * @return result of division
   */
  public IntNumber remainder(double value) {
    return new IntNumber((int)(this.data - Math.floor(this.data / value) * value));
  }


  /**
   * Returns result of division.
   * 
   * @param value value
   * @return result of division
   */
  public IntNumber divide(IntNumber value) {
    return divide(value.data);
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber leftDivide(int value) {
    return new IntNumber(value / this.data);
  }

  /**
   * Returns result of left division.
   * 
   * @param value value
   * @return result of left division
   */
  public IntNumber leftDivide(IntNumber value) {
    return leftDivide(value.data);
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber add(double value) {
    return new IntNumber((int)(this.data + value));
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber subtract(double value) {
    return new IntNumber((int)(this.data - value));
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber multiply(double value) {
    return new IntNumber((int)(this.data * value));
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber divide(double value) {
    return new IntNumber((int)(this.data / value));
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber leftDivide(double value) {
    return new IntNumber((int)(value / this.data));
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber inverse() {
    return new IntNumber(1 / this.data);
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber conjugate() {
    return new IntNumber(this.data);
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber unaryMinus() {
    return new IntNumber(- this.data);
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber power(int scalar) {
    return new IntNumber((int)Math.pow(this.data,scalar));
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber createUnit() {
    return new IntNumber(1);
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber create(int value) {
    return new IntNumber(value);
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber create(double value) {
    return new IntNumber((int)value);
  }

  /**
   * {@inheritDoc}
   */
  public boolean compare(String operator, int opponent) {
    if (operator.equals(".!=")) { //$NON-NLS-1$
      if (this.data != opponent) {
        return true;
      }
    } else if (operator.equals(".==")) { //$NON-NLS-1$
      if (this.data == opponent) {
        return true;
      }
    } else if (operator.equals(".<")) { //$NON-NLS-1$
      if (this.data < opponent) {
        return true;
      }
    } else if (operator.equals(".<=")) { //$NON-NLS-1$
      if (this.data <= opponent) {
        return true;
      }
    } else if (operator.equals(".>")) { //$NON-NLS-1$
      if (this.data > opponent) {
        return true;
      }
    } else if (operator.equals(".>=")) { //$NON-NLS-1$
      if (this.data >= opponent) {
        return true;
      }
    } else {
      throw new IllegalArgumentException();
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  public boolean compare(String operator, double opponent) {
    if (operator.equals(".!=")) { //$NON-NLS-1$
      if (this.data != opponent) {
        return true;
      }
    } else if (operator.equals(".==")) { //$NON-NLS-1$
      if (this.data == opponent) {
        return true;
      }
    } else if (operator.equals(".<")) { //$NON-NLS-1$
      if (this.data < opponent) {
        return true;
      }
    } else if (operator.equals(".<=")) { //$NON-NLS-1$
      if (this.data <= opponent) {
        return true;
      }
    } else if (operator.equals(".>")) { //$NON-NLS-1$
      if (this.data > opponent) {
        return true;
      }
    } else if (operator.equals(".>=")) { //$NON-NLS-1$
      if (this.data >= opponent) {
        return true;
      }
    } else {
      throw new IllegalArgumentException();
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  public boolean compare(String operator, IntNumber opponent) {
    //if (opponent instanceof IntNumber) {
      return compare(operator, opponent.intValue());
    //}

    //return false;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isZero(double tolerance) {
    return Math.abs(this.data) <= tolerance;
  }

//  /**
//   * {@inheritDoc}
//   */
//  public boolean isZero(NumericalScalar<?,?> tolerance) {
//    return tolerance.isGreaterThanOrEquals(Math.abs(this.data));
//  }

  /**
   * {@inheritDoc}
   */
  public boolean isUnit() {
    return this.data == 1;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isUnit(double tolerance) {
    return Math.abs(this.data - 1) <= tolerance;
  }

//  /**
//   * {@inheritDoc}
//   */
//  public boolean isUnit(NumericalScalar<?,?> tolerance) {
//    return tolerance.isGreaterThanOrEquals(Math.abs(this.data - 1));
//  }

  /**
   * {@inheritDoc}
   */
  public boolean isNaN() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isFinite() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isInfinite() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isReal() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isComplex() {
    return false;
  }

//  /**
//   * {@inheritDoc}
//   */
//  public IntNumber transformFrom(int value) {
//    return new IntNumber(value);
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public IntNumber transformFrom(double value) {
//    return new IntNumber((int)value);
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public ScalarOperator getAddOperator() {
//    return IntNumberAddOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public ScalarOperator getSubtractOperator() {
//    return IntNumberSubtractOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public ScalarOperator getMultiplyOperator() {
//    return IntNumberMultiplyOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public ScalarOperator getDivideOperator() {
//    return IntNumberDivideOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public ScalarOperator getLeftDivideOperator() {
//    return IntNumberLeftDivideOperator.getInstance();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public ScalarEqual getEqualOperator() {
//    return IntNumberEqual.getInstance();
//  }

  /**
   * {@inheritDoc}
   */
  public String toString(String valueFormat) {
    return String.format(valueFormat, Integer.valueOf(this.data));
  }

  /**
   * 文字列に変換します。
   * 
   * @param value 値
   * @param valueFormat 値のフォーマット
   * @return 変換結果の文字列
   */
  public static String toString(final int value, final String valueFormat) {
    return String.format(valueFormat, Integer.valueOf(value));
  }
  
  /**
   * {@inheritDoc}
   */
  public IntNumber createZero() {
    return new IntNumber(0);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isZero() {
    return this.data == 0;
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber[] createArray(int size) {
    return new IntNumber[size];
  }

//  /**
//   * {@inheritDoc}
//   */
//  public IntNumber[] createArray(GridElement<?>[] elements) {
//    final int size = elements.length;
//
//    if (size != 0 && (elements[0] instanceof IntNumber) == false) {
//      throw new IllegalArgumentException();
//    }
//    
//    final IntNumber[] array = new IntNumber[size];
//    System.arraycopy(elements, 0, array, 0, size);
//    return array;
//  }
  
  
//  /**
//   * {@inheritDoc}
//   */
//  public IntNumber[][] createArray(GridElement<?>[][] elements) {
//    final int rowSize = elements.length;
//    final int columnSize = rowSize == 0 ? 0 : elements[0].length;
//    
//    if (rowSize != 0 && columnSize !=0 && (elements[0][0] instanceof IntNumber) == false) {
//      throw new IllegalArgumentException();
//    }
//    
//    final IntNumber[][] array = new IntNumber[rowSize][columnSize];
//    for (int row = 0; row < rowSize; row++) {
//      System.arraycopy(elements[row], 0, array[row], 0, columnSize);
//    }
//    return array;
//  }

  /**
   * {@inheritDoc}
   */
  public IntNumber[][] createArray(int rowSize, int columnSize) {
    return new IntNumber[rowSize][columnSize];
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber ceil() {
    return new IntNumber(this.data);
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber floor() {
    return new IntNumber(this.data);
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber fix() {
    return new IntNumber(this.data);
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber round() {
    return new IntNumber(this.data);
  }

  /**
   * {@inheritDoc}
   */
  public IntNumber roundToZero(double tolerance) {
    return new IntNumber(this.data);
  }

//  /**
//   * {@inheritDoc}
//   */
//  public IntNumber roundToZero(NumericalScalar<?,?> tolerance) {
//    return new IntNumber(this.data);
//  }
  
  /**
   * 許容範囲内で等しいか判定します。
   * 
   * @param opponent 比較する倍精度実数
   * @param tolerance 許容誤差
   * @return 許容範囲内で等しければtrue、そうでなければfalse
   */
  public final boolean equals(final IntNumber opponent, final double tolerance) {
      return Math.abs(this.data - opponent.data) <= tolerance;
  }

  /**
   * 許容範囲内で等しいか判定します。
   * 
   * @param opponent 比較する倍精度実数
   * @param tolerance 許容誤差
   * @return 許容範囲内で等しければtrue、そうでなければfalse
   */
  public final boolean equals(final IntNumber opponent, final DoubleNumber tolerance) {
    return Math.abs(this.data - opponent.data) <= tolerance.doubleValue();
  }

}
