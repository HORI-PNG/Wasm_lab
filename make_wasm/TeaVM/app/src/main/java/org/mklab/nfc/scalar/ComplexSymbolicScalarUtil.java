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
 * @author koga
 * @version $Revision$, 2021/07/15
 */
public class ComplexSymbolicScalarUtil {
  /**
   * 新しく生成された<code>ComplexSymbolicScalarUtil</code>オブジェクトを初期化します。
   */
  private ComplexSymbolicScalarUtil() {
    // nothing to do
  }

//  /**
//   * 実数の逆数と複素数の積を返します。
//   * 
//   * @param <S> 実部と虚部の型
//   * @param <M> 行列の型
//   * @param <CS> 係数スカラーの型
//   * @param <CM> 係数行列の型
//   * 
//   * @param realNumber 実数
//   * @param complexNumber 複素数
//   * @return 実数の逆数と複素数の積
//   */
//  public static <S extends SymbolicScalar<S,M,CS,CM>, M extends BaseSymbolicMatrix<S,M,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends BaseNumericalMatrix<CS,CM>> BaseComplexSymbolicScalar<S,M,CS,CM> leftDivide(final double realNumber, final BaseComplexSymbolicScalar<S,M,CS,CM> complexNumber) {
//    final S rePart = complexNumber.getRealPart().divide(realNumber);
//    final S impart = complexNumber.getImaginaryPart().divide(realNumber);
//    return new BaseComplexSymbolicScalar<>(rePart, impart);
//  }
//
//  /**
//   * 整数の逆数と複素数の積を返します。
//   * 
//   * @param <S> 実部と虚部の型
//   * @param <M> 行列の型
//   * @param <CS> 係数スカラーの型
//   * @param <CM> 係数行列の型
//   * 
//   * @param intNumber 整数
//   * @param complexNumber 複素数
//   * @return 整数の逆数と複素数の積
//   */
//  public static <S extends SymbolicScalar<S,M,CS,CM>, M extends BaseSymbolicMatrix<S,M,CS,CM>, CS extends NumericalScalar<CS,CM>, CM extends BaseNumericalMatrix<CS,CM>> BaseComplexSymbolicScalar<S,M,CS,CM> leftDivide(final int intNumber, final BaseComplexSymbolicScalar<S,M,CS,CM> complexNumber) {
//    final S rePart = complexNumber.getRealPart().divide(intNumber);
//    final S impart = complexNumber.getImaginaryPart().divide(intNumber);
//    return new BaseComplexSymbolicScalar<>(rePart, impart);
//  }
}
