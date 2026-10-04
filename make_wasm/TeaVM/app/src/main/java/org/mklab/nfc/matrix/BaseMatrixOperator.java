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

import org.mklab.nfc.scalar.Scalar;

/**
 * Interface of {@link BaseMatrix}.
 * 
 * @param <S> スカラーの型
 * @param <M> 行列の型
 * @author koga
 * @version $Revision$, 2021/09/18
 */
public interface BaseMatrixOperator<S extends Scalar<S,M>, M extends Matrix<S,M>> extends Matrix<S, M> {
  /**
   * 全ての成分の2次元配列を返します。
   * 
   * @return 全ての成分の2次元配列
   */
  S[][] getElements();

  /**
   * 全ての成分を設定します。
   * 
   * @param elements 全ての成分の2次元配列
   */
  void setElements(final S[][] elements);
}
