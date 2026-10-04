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
import org.mklab.nfc.scalar.ComplexSymbolicScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;
import org.mklab.nfc.scalar.RealSymbolicScalar;

/**
 * 複素数値行列を表すインターフェースです。
 * 
 * @author koga
 * @version $Revision$, 2021/08/18
 * @param <CS> 複素スカラーの型 
 * @param <CM> 複素行列の型
 * @param <RS> 実部と虚部のスカラーの型
 * @param <RM> 行列の型
 * @param <RES> 係数スカラーの型
 * @param <REM> 係数行列の型
 * @param <CES> 複素係数スカラーの型 
 * @param <CEM> 複素係数行列の型
 */
public interface ComplexSymbolicMatrix<RS extends RealSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, RM extends RealSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, CS extends ComplexSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, CM extends ComplexSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, RES extends RealNumericalScalar<RES,REM,CES,CEM>, REM extends RealNumericalMatrix<RES,REM,CES,CEM>, CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>> extends SymbolicMatrix<CS,CM,CES,CEM> {
  /**
   * 実部行列を返します。
   * 
   * @return 実部行列
   */
 RM getRealPart();

  /**
   * 虚部行列を返します。
   * 
   * 
   * @return 虚部行列
   */
 RM getImaginaryPart();
  
  /**
   * 実部行列を設定します。
   * 
   * @param realPart 実部行列
   */
  void setRealPart(RM realPart);
  
  /**
   * Sets real parts.
   * 
   * @param realPart real part
   */
  void setRealPart(final IntMatrix realPart);

  /**
   * Sets real part.
   * 
   * @param realPart real part
   */
  void setRealPart(final DoubleMatrix realPart);

  
  /**
   * 実部行列を設定します。
   * 
   * @param imaginaryPart 実部行列
   */
  void setImaginaryPart(RM imaginaryPart);
  
  /**
   * Set imaginary part.
   * 
   * @param imagPart imaginary part.
   */
  void setImaginaryPart(final DoubleMatrix imagPart);

  /**
   * Set imaginary part.
   * 
   * @param imaginaryPart imaginary part. 
   */
  void setImaginaryPart(final IntMatrix imaginaryPart);
  
  /**
   * Adds complex value.
   * 
   * @param value value
   * @return result
   */
  CM add(RM value);
  
  /**
   * Subtracts complex value.
   * 
   * @param value value
   * @return result
   */
  CM subtract(RM value);

  /**
   * Multiplies complex value.
   * 
   * @param value value
   * @return result
   */
  CM multiply(RM value);

  /**
   * Divides complex value.
   * 
   * @param value value
   * @return result
   */
  CM divide(RM value);

  /**
   * Divides from left by complex value.
   * 
   * @param value value
   * @return result
   */
  CM leftDivide(RM value);
  
  /**
   * 下側に行列<code>value</code>を付けた行列を生成します。
   * 
   * @param value 付ける行列
   * @return 下側に<code>value</code>をつけた行列
   */
  CM appendDown(RM value);

  /**
   * 右側に<code>value</code>を付けた行列を生成します。
   * 
   * @param value 付ける複素数
   * @return 右側に<code>value</code>を付けた行列
   */
  CM appendRight(RM value);
  
  /**
   * Multiplies complex value.
   * 
   * @param value value
   * @return result
   */
  CM multiply(RS value);

  /**
   * Divides complex value.
   * 
   * @param value value
   * @return result
   */
  CM divide(RS value);

  /**
   * Divides from left by complex value.
   * 
   * @param value value
   * @return result
   */
  CM leftDivide(RS value);

}
