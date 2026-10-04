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

import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.IntMatrix;
import org.mklab.nfc.scalar.DoubleNumber;

/**
 * @author koga
 * @version $Revision$, 2021/10/18
 */
public class DoubleLUDecomposition extends LUDecomposition<DoubleNumber, DoubleMatrix> {

  /**
   * Creates {@link DoubleLUDecomposition}.
   * @param lower lower
   * @param upper upper
   * @param permutation permutation
   */
  public DoubleLUDecomposition(DoubleMatrix lower, DoubleMatrix upper, IntMatrix permutation) {
    super(lower, upper, permutation);
  }

  /**
   * Creates {@link DoubleLUDecomposition}.
   * @param lower lower
   * @param upper upper
   */
  public DoubleLUDecomposition(DoubleMatrix lower, DoubleMatrix upper) {
    super(lower, upper);
  }

}
