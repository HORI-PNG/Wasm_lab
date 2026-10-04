/*
 * Created on 2008/07/16
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.svd;

import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * @author koga
 * @version $Revision: 1.1 $, 2008/07/16
 */
final class Messages {

  /** バンドルネーム。 */
  private static final String BUNDLE_NAME = "org.mklab.nfc.svd.messages"; //$NON-NLS-1$

  /** リソースバンドル。 */
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

  /**
   * 新しく生成された<code>Messages</code>オブジェクトを初期化します。
   */
  private Messages() {
    // nothing to do
  }

  /**
   * キーに対応する文字列を返します。
   * 
   * @param key キー
   * @return キーに対応する文字列
   */
  static String getString(final String key) {
    try {
      return RESOURCE_BUNDLE.getString(key);
    } catch (@SuppressWarnings("unused") MissingResourceException e) {
      return '!' + key + '!';
    }
  }
}
