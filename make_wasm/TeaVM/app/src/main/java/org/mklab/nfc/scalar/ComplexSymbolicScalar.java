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
 * 複素数式スカラーを表すクラスです。
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
public interface ComplexSymbolicScalar<RS extends RealSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, RM extends RealSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, CS extends ComplexSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, CM extends ComplexSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, RES extends RealNumericalScalar<RES,REM,CES,CEM>, REM extends RealNumericalMatrix<RES,REM,CES,CEM>, CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>> extends SymbolicScalar<CS,CM,CES,CEM> {
  /**
   * 実部を設定します。
   * 
   * @param realPart 実部
   */
  void setRealPart(final RS realPart);

  /**
   * 実部を返します。
   * 
   * @return 実部
   */
  RS getRealPart();

  /**
   * 虚部を設定します。
   * 
   * @param imaginaryPart 虚部
   */
  void setImaginaryPart(final RS imaginaryPart);

  /**
   * 虚部を返します。
   * 
   * @return 虚部
   */
  RS getImaginaryPart();

  /**
   * 実部を設定します。
   * 
   * @param realPart 実部
   */
  void setRealPart(final int realPart);

  /**
   * 実部を設定します。
   * 
   * @param realPart 実部
   */
  void setRealPart(final double realPart);

  /**
   * 虚部を設定します。
   * 
   * @param imagPart 虚部
   */
  void setImaginaryPart(final int imagPart);

  /**
   * 虚部を設定します。
   * 
   * @param imagPart 虚部
   */
  void setImaginaryPart(final double imagPart);
  
  /**
   * @param rePart real part
   * @param imPart imaginary part
   * @return CS
   */
  CS create(RS rePart, RS imPart);

  /**
   * @param rePart real part
   * @return CS
   */
  CS create(RS rePart);
  
  /**
   * 値を加えた成分を生成します。
   * 
   * @param value 加える値
   * @return 足し算の結果
   */
  CS add(RS value);
  
  /**
   * 値を引きます。
   * 
   * @param value 引く値
   * @return 引き算の結果
   */
  CS subtract(RS value);
  
  /**
   * 値を掛けます。
   * 
   * @param value 掛ける値
   * @return 掛け算の結果
   */
  CS multiply(RS value);

  /**
   * 値で割ります。
   * 
   * @param value 割る値
   * @return 割り算の結果
   */
  CS divide(RS value); 

  /**
   * 値を割ります。
   * 
   * @param value 割られる値
   * @return 割り算の結果
   */
  CS leftDivide(RS value);
}
