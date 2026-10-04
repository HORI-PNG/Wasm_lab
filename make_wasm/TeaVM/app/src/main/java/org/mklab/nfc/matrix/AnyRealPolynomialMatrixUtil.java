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

import org.mklab.nfc.scalar.AnyRealPolynomial;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;

/**
 * @author koga
 * @version $Revision$, 2021/11/16
 */
public class AnyRealPolynomialMatrixUtil {

  /**
   * 実行列の各成分を定数項とする多項式を成分とする多項式行列に変換します。
   * 
 * @param <RES> 係数スカラーの型
 * @param <REM> 係数行列の型
 * @param <CES> 複素係数スカラーの型 
 * @param <CEM> 複素係数行列の型
   * @param matrix 実行列
   * @return 実行列の各成分を定数項とする多項式を成分とする多項式行列
   */
  public static <RES extends RealNumericalScalar<RES, REM, CES, CEM>, REM extends RealNumericalMatrix<RES, REM, CES, CEM>, CES extends ComplexNumericalScalar<RES, REM, CES, CEM>, CEM extends ComplexNumericalMatrix<RES, REM, CES, CEM>> AnyRealPolynomial<RES,REM,CES,CEM>[][] createArray(final RES[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final AnyRealPolynomial<RES,REM,CES,CEM>[][] ans = new AnyRealPolynomial[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      final AnyRealPolynomial<RES,REM,CES,CEM>[] ansi = ans[i];
      final RES[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = new AnyRealPolynomial<>(matrixi[j]);
      }
    }
    return ans;
  }

}
