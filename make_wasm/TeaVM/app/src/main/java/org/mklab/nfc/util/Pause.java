/*
 * $Id: Pause.java,v 1.8 2008/07/16 08:00:37 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 */
package org.mklab.nfc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;


/**
 * 確認ボタンがクリック(リターンキーが押されるまで)されるまで、指定された時間が経過するまで停止します。
 * 
 * @author koga
 * @version $Revision: 1.8 $
 */
public final class Pause {
  /**
   * 新しく生成された<code>Pause</code>オブジェクトを初期化します。
   */
  private Pause() {
    // nothing to do
  }

  /** GUIを使用する場合true。 */
  //private static boolean usingGui = true;
  
  /** 入力 */
  private static InputStream input = System.in;
  /** 出力 */
  private static OutputStream output = System.out;

//  /**
//   * GUIの使用・未使用を設定します。
//   * @param usingGui GUIを使用する場合true
//   */
//  public static void setUsingGud(final boolean usingGui) {
//    Pause.usingGui = usingGui;
//  }
  
  /**
   * 入力ストリームを設定します。
   * @param input 入力ストリーム
   */
  public static void setInput(final InputStream input) {
    Pause.input = input;
  }
  
  /**
   * 出力ストリームを設定します。
   * @param output 出力ストリーム
   */
  public static void setOutput(final OutputStream output) {
    Pause.output = output;
  }
  
  /**
   * ダイアログの確認ボタンがクリックするまで待機します。(GUIのとき) 
   * 
   * リターンキーが押されるまで待機します。(CUIのとき)
   * 
   * @throws IOException キーボードから入力できない場合
   */
  public static void pause() throws IOException {
//    if (usingGui) {
//      pause(Messages.getString("Pause.0")); //$NON-NLS-1$
//    } else {
      pause(""); //$NON-NLS-1$
//    }
  }

  /**
   * メッセージを表示して、 ダイアログの確認ボタンがクリックするまで待機する(GUIのとき)、 リターンキーが押されるまで待機する(CUIのとき)。
   * 
   * @param message メッセージ
   * @throws IOException キーボードから入力できない場合
   */
  public static void pause(final String message) throws IOException {
//    if (usingGui) {
//      int returnValue = JOptionPane.showConfirmDialog((Component)null, message, Messages.getString("Pause.1"), JOptionPane.OK_CANCEL_OPTION); //$NON-NLS-1$
//      if (returnValue == JOptionPane.CANCEL_OPTION) {
//        throw new InterruptedException(Messages.getString("Pause.2")); //$NON-NLS-1$
//      }
//    } else {
      final PrintStream out = new PrintStream(Pause.output);
      if (message.length() != 0) {
        out.println(message);
      }
      out.print(Messages.getString("Pause.3")); //$NON-NLS-1$

      final BufferedReader reader = new BufferedReader(new InputStreamReader(Pause.input));
      reader.readLine();

//      final String lineSeparator = System.getProperty("line.separator"); //$NON-NLS-1$
//      
//      if (lineSeparator.equals("\n")) { //$NON-NLS-1$
//        Pause.input.read();
//      } else {
//        Pause.input.read();
//        Pause.input.read();
//      }
//
//    }
  }

  /**
   * 指定された時間(秒)だけ停止します。
   * 
   * @param time 停止する時間(秒)
   * @throws InterruptedException 強制終了された場合
   */
  public static void pause(final double time) throws InterruptedException {
//    try {
      Thread.sleep((int)(time * 1000));
//    } catch (InterruptedException e) {
//      throw new InterruptedException(Messages.getString("Pause.4")); //$NON-NLS-1$
//    }

  }
}