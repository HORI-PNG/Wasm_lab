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
package org.mklab.nfc.eig;

import org.mklab.nfc.matrix.DoubleComplexMatrix;
import org.mklab.nfc.scalar.DoubleComplexNumber;

/**
 * @author koga
 * @version $Revision$, 2021/10/18
 */
public class DoubleComplexHessenbergDecomposition extends HessenbergDecomposition<DoubleComplexNumber, DoubleComplexMatrix> {

  /**
   * Creates {@link DoubleComplexHessenbergDecomposition}.
   * @param q q
   * @param h h
   */
  public DoubleComplexHessenbergDecomposition(DoubleComplexMatrix q, DoubleComplexMatrix h) {
    super(q, h);
  }

}
