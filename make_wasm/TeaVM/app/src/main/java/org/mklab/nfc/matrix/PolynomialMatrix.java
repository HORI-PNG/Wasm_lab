/*
 * $Id: PolynomialMatrix.java,v 1.144 2008/07/16 04:58:03 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

import org.mklab.nfc.scalar.NumericalScalar;
import org.mklab.nfc.scalar.Polynomial;
import org.mklab.nfc.scalar.RationalPolynomial;


/**
 * 多項式({@link Polynomial})を成分とする行列を表わすクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.144 $
 * @param <PS> 多項式の型
 * @param <PM> 多項式行列の型
 * @param <RS> 有理多項式の型
 * @param <RM> 有理多項式行列の型
 * @param <ES> 係数スカラーの型
 * @param <EM> 係数行列の型
  */
public interface PolynomialMatrix<PS extends Polynomial<PS,PM,RS,RM,ES,EM>, PM extends PolynomialMatrix<PS,PM,RS,RM,ES,EM>, RS extends RationalPolynomial<PS,PM,RS,RM,ES,EM>, RM extends RationalPolynomialMatrix<PS,PM,RS,RM,ES,EM>, ES extends NumericalScalar<ES,EM>,EM extends NumericalMatrix<ES,EM>> extends SymbolicMatrix<PS,PM,ES,EM> {
  /**
   * Generate rational polynomial matrix of denominator of 1. 
   * @return rational polynomial matrix
   */
  RM toRational();
}