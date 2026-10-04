/*
 * Created on 2008/01/28
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.util;

/**
 * 計算の丸めモードの選択方法を表すインターフェースです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.1 $, 2008/01/28
 */
public interface RoundModeSelector {

  /**
   * 丸めモードを設定します。
   * 
   * @param mode 丸めモード
   */
  void setRoundMode(RoundMode mode);

  /**
   * 丸めモードを返します。
   * 
   * @return 丸めモード
   */
  RoundMode getRoundMode();
}
