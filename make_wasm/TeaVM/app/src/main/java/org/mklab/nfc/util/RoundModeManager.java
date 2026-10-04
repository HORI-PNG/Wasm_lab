/*
 * Created on 2008/01/28
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.util;

import java.util.ArrayList;
import java.util.List;


/**
 * 浮動小数点数の丸めモードを管理するクラスです。
 * 
 * @author Koga Laboratory
 * @version $Revision: 1.4 $, 2008/01/28
 */
public final class RoundModeManager {

  /** 計算の丸めモードを設定者。 */
  final List<RoundModeSelector> selectros = new ArrayList<>();

  /** 計算の丸めモードの管理者。 */
  private static final RoundModeManager MANAGER = new RoundModeManager();

  /**
   * 新しく生成された<code>RoundModeObserver</code>オブジェクトを初期化します。
   */
  private RoundModeManager() {
    // For singleton pattern
  }

  /**
   * 計算の丸めモードの管理者を返します。
   * 
   * @return 計算の丸めモードの管理者
   */
  public static RoundModeManager getManager() {
    return RoundModeManager.MANAGER;
  }

  /**
   * 計算の丸めモードの設定者を登録します。
   * 
   * @param selector 計算の丸めモードを設定者
   */
  public void add(final RoundModeSelector selector) {
    this.selectros.add(selector);
  }

  /**
   * 計算の丸めモードの設定者の登録を削除します。
   * 
   * @param selector 計算の丸めモードを設定者
   */
  public void remove(final RoundModeSelector selector) {
    this.selectros.remove(selector);
  }

  /**
   * 指定されたクラスの計算の丸めモードの設定者の登録を削除します。
   * 
   * @param klass 計算の丸めモードを設定者のクラス
   */
  public void remove(final Class<?> klass) {
    final List<RoundModeSelector> removingCandidate = new ArrayList<>(); 
    for (RoundModeSelector selector : this.selectros) {
      if (selector.getClass() == klass) {
        removingCandidate.add(selector);
      }
    }
    
    for (RoundModeSelector selector : removingCandidate) {
      this.selectros.remove(selector);
    }
  }

  /**
   * 計算の丸めモードを設定します。
   * 
   * @param mode 計算の丸めモード
   */
  public void setRoundMode(final RoundMode mode) {
    for (RoundModeSelector selector : this.selectros) {
      selector.setRoundMode(mode);
    }
  }

  /**
   * 指定されたクラスの計算の丸めモードの設定者が登録されているか判定します。
   * 
   * @param klass 計算の丸めモードを設定者のクラス
   * @return 指定されたクラスの計算の丸めモードの設定者が登録されていればtrue、そうでなければfalse
   */
  public boolean contains(final Class<?> klass) {
    for (RoundModeSelector selector : this.selectros) {
      if (selector.getClass() == klass) {
        return true;
      }
    }

    return false;
  }

  /**
   * 指定された計算の丸めモードの設定者が登録されているか判定します。
   * 
   * @param selector 計算の丸めモードを設定者
   * @return 指定された計算の丸めモードの設定者が登録されていればtrue、そうでなければfalse
   */
  public boolean contains(final RoundModeSelector selector) {
    return this.selectros.contains(selector);
  }

  /**
   * 計算の丸めモードを返します。
   * 
   * @return 計算の丸めモード
   */
  public RoundMode getRoundMode() {
    if (this.selectros.size() == 0) {
      throw new IllegalArgumentException(Messages.getString("RoundModeManager.0")); //$NON-NLS-1$
    }

    return this.selectros.get(0).getRoundMode();
  }

  /**
   * 計算の丸めモードの設定者の登録を全て削除します。
   */
  public void clear() {
    this.selectros.clear();
  }
}
