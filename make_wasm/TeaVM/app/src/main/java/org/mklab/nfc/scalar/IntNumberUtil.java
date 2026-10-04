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


/**
 * Utility class of {@link IntNumber}.
 * 
 * @author koga
 * @version $Revision$, 2021/08/01
 */
public class IntNumberUtil {

  /**
   * Creates {@link IntNumberUtil}.
   */
  private IntNumberUtil() {
    // nothing to do
  }

  /**
   * DoubleNumber型の2次元配列を返します。
   * 
   * @param elements 元のデータ
   * @return DoubleNumber型の2次元配列
   */
  public static IntNumber[][] createArray(final int[][] elements) {
    final int rowSize = elements.length;
    final int columnSize = rowSize == 0 ? 0 : elements[0].length;

    final IntNumber[][] matrix = new IntNumber[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        matrix[i][j] = new IntNumber(elements[i][j]);
      }
    }

    return matrix;
  }
}
