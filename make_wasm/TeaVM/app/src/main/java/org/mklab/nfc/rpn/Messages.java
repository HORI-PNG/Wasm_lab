package org.mklab.nfc.rpn;

import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * 文字列の外部化を行うためのクラスです。
 * 
 * @author koga
 * 
 */
final class Messages {

  /** バンドルネーム。 */
  private static final String BUNDLE_NAME = "org.mklab.nfc.rpn.messages"; //$NON-NLS-1$

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
