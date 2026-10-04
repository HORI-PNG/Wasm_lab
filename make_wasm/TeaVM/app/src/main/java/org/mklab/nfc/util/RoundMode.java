/*
 * Created on 2008/01/28
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.util;

/**
 * 丸めモードを表す列挙型です。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.1 $, 2008/01/28
 */
public enum RoundMode {
  /** 上への丸め。 */
  ROUND_UP,
  /** 下への丸め。 */
  ROUND_DOWN,
  /** 最近点への丸め。 */
  ROUND_NEAR,
  /** 0方向への丸め。 */
  ROUND_ZERO;
}
