/*
 * $Id: ComplexScalar.java,v 1.33 2008/07/16 04:58:02 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.scalar;

import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;


/**
 * 複素数値スカラーを表わすクラスです。
 * 
 * @author koga
 * @version $Revision: 1.33 $, 2004/06/22
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
 */
public interface ComplexNumericalScalar<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> extends NumericalScalar<CS,CM> {
  /**
   * 許容範囲内で等しいか判定します。
   * 
   * @param opponent 比較する複素数成分
   * @param tolerance 許容誤差
   * @return 許容範囲内で等しければtrue、そうでなければfalse
   */
  boolean equals(CS opponent, RS tolerance);
  
  /**
   * 実部を設定します。
   * 
   * @param realPart 実部
   */
 void setRealPart(int realPart);

 /**
   * 実部を設定します。
   * 
   * @param realPart 実部
   */
 void setRealPart(double realPart);

 /**
   * 実部を設定します。
   * 
   * @param realPart 実部
   */
 void setRealPart(RS realPart);

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
 void setImaginaryPart(int imaginaryPart);

  /**
   * 虚部を設定します。
   * 
   * @param imaginaryPart 虚部
   */
 void setImaginaryPart(double imaginaryPart);

  /**
   * 虚部を設定します。
   * 
   * @param imaginaryPart 虚部
   */
 void setImaginaryPart(RS imaginaryPart);

  /**
   * 虚部を返します。
   * 
   * @return 虚部
   */
 RS getImaginaryPart();
  
  /**
   * 虚部単位を返します。
   * 
   * @return 虚部単位
   */
 CS createImaginaryUnit();

  /**
   * 偏角を返します。
   * 
   * @return 偏角
   */
 RS arg();
 
 
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