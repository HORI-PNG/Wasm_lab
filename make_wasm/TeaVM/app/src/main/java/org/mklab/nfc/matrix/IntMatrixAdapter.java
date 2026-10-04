/*
 * Created on 2007/12/26
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matrix;

/**
 * {IntMatrix}を参照するためのアダプタークラスです。
 * 
 * @author koga
 * @version $Revision$, 2015/05/23
 */
public class IntMatrixAdapter {

  /**
   * {@link IntMatrix}の成分を返します。
   * 
   * @param a 対象となる行列
   * @return 成分
   */
  public static int[][] getElements(IntMatrix a) {
    return a.getIntElements();
  }
}
