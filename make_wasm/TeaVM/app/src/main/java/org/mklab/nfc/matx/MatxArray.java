/*
 * $Id: MatxArray.java,v 1.5 2008/04/25 09:09:02 tanaka Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matx;

import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.scalar.Scalar;


/**
 * MaTXの配列を表すインターフェースです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.5 $, 2004/06/22
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public interface MatxArray<S extends Scalar<S,M>, M extends Matrix<S,M>> extends  Matrix<S,M>, MatxObject {

  /**
   * Matrix型の値を返します。
   * 
   * @return Matrix型の値
   */
  M toMatrix();
}
