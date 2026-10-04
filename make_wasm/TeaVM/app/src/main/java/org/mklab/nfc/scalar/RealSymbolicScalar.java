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

import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.ComplexSymbolicMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.matrix.RealSymbolicMatrix;


/**
 * 実数式スカラーを表すクラスです。
 * 
 * @author koga
 * @version $Revision$, 2021/07/15
 * @param <RS> 実部と虚部のスカラーの型
 * @param <RM> 行列の型
 * @param <CS> 複素スカラーの型 
 * @param <CM> 複素行列の型
 * @param <RES> 係数スカラーの型
 * @param <REM> 係数行列の型
 * @param <CES> 複素係数スカラーの型 
 * @param <CEM> 複素係数行列の型
 */
public interface RealSymbolicScalar<RS extends RealSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, RM extends RealSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, CS extends ComplexSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, CM extends ComplexSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, RES extends RealNumericalScalar<RES,REM,CES,CEM>, REM extends RealNumericalMatrix<RES,REM,CES,CEM>, CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>> extends SymbolicScalar<RS,RM,RES,REM> {
//    /**
//   * 許容範囲内で等しいか判定します。
//   * 
//   * @param opponent 比較する複素数成分
//   * @param tolerance 許容誤差
//   * @return 許容範囲内で等しければtrue、そうでなければfalse
//   */
//  boolean equals(final RS opponent, final double tolerance);

//  /**
//   * 許容範囲内で等しいか判定します。
//   * 
//   * @param opponent 比較する複素数成分
//   * @param tolerance 許容誤差
//   * @return 許容範囲内で等しければtrue、そうでなければfalse
//   */
//  boolean equals(final RS opponent, final RES tolerance);
  
  /**
   * 複素式スカラーに変換します。
   * 
   * @return 複素式カラー
   */
  CS toComplex();

  /**
   * Adds complex value.
   * 
   * @param value value
   * @return result
   */
  RS add(RS value);

  /**
   * Adds complex value.
   * 
   * @param value value
   * @return result
   */
  CS add(CS value);
  
  /**
   * Subtracts complex value.
   * 
   * @param value value
   * @return result
   */
  RS subtract(RS value);

  /**
   * Subtracts complex value.
   * 
   * @param value value
   * @return result
   */
  CS subtract(CS value);

  /**
   * Multiplies complex value.
   * 
   * @param value value
   * @return result
   */
  RS multiply(RS value);

  /**
   * Multiplies complex value.
   * 
   * @param value value
   * @return result
   */
  CS multiply(CS value);

  /**
   * Divides complex value.
   * 
   * @param value value
   * @return result
   */
  RS divide(RS value);

  /**
   * Divides complex value.
   * 
   * @param value value
   * @return result
   */
  CS divide(CS value);

  /**
   * Divides from left by complex value.
   * 
   * @param value value
   * @return result
   */
  RS leftDivide(RS value);

  /**
   * Divides from left by complex value.
   * 
   * @param value value
   * @return result
   */
  CS leftDivide(CS value);
}
