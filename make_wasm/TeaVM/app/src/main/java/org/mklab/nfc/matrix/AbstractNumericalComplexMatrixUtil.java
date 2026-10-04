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

import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;

/**
 * Utility class of {@link AbstractNumericalComplexMatrix}.
 * 
 * @author koga
 * @version $Revision$, 2021/08/20
 */
public class AbstractNumericalComplexMatrixUtil {

  /**
   * Creates {@link AbstractNumericalComplexMatrixUtil}.
   */
  private AbstractNumericalComplexMatrixUtil() {
    // nothing to do
  }

  /**
   * 自身の各成分の偏角を成分に持つ行列を返します。
   * 
 * @param <RS> 実スカラーの型
 * @param <RM> 実行列の型
 * @param <CS> 複素スカラーの型 
 * @param <CM> 複素行列の型
   * 
   * @param matrix 対象となる行列
   * @return 偏角行列
   */
  public static <RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> RS[][] argumentElementWise(final CS[][] matrix) {
    int rowSize = matrix.length;
    int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    RS[][] ans = matrix[0][0].getRealPart().createArray(rowSize, columnSize);
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].arg();
      }
    }
    return ans;
  }


}
