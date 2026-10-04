/*
 * $Id: NormType.java,v 1.3 2006/08/25 00:43:37 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.matrix;

/**
 * ノルムの種類を表す列挙型です。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.3 $
 */
public enum NormType {
  /** フロベニウスノルム。 */
  FROBENIUS,
  /** １-ノルム。 */
  ONE,
  /** ２-ノルム。 */
  TWO,
  /** 無限大ノルム。 */
  INFINITY;
}
