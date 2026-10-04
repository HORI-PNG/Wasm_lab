/*
 * $Id: ComplexScalar.java,v 1.33 2008/07/16 04:58:02 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.scalar;

import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;


/**
 * 実数値スカラーを表わすクラスです。
 * 
 * @author koga
 * @version $Revision: 1.33 $, 2004/06/22
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS> type of complex scalar
 * @param <CM> type of complex matrix
 */
public interface RealNumericalScalar<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> extends NumericalScalar<RS,RM> {
  /**
   *  Creates complex array.
   *  
   * @param realPart real part
   * @param imagPart imaginary part
   * @return complex array
   */
  CS[] createComplexArray(RS[] realPart, RS[] imagPart);

  /**
   *  Creates complex array.
   *  
   * @param realPart real part
   * @return complex array
   */
  CS[] createComplexArray(RS[] realPart);

  /**
   * Creates complex array.
   * @param realPart real part
   * @param imagPart imaginary part
   * @return complex array
   */
  CS[][] createComplexArray(RS[][] realPart, RS[][] imagPart);

  /**
   * Creates complex array.
   * @param realPart real part
   * @return complex array
   */
  CS[][] createComplexArray(RS[][] realPart);
  
  /**
   * Transforms to complex value.
   * 
   * @return complex value
   */
  CS toComplex();
  
  /**
   * Transforms to double number.
   * 
   * @return double number
   */
  double toDouble();
  
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
  CS subtract(CS value);

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
  CS divide(CS value);

  /**
   * Divides from left by complex value.
   * 
   * @param value value
   * @return result
   */
  CS leftDivide(CS value);
}