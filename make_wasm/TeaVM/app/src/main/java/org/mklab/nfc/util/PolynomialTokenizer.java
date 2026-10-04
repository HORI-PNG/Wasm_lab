/*
 * $Id: PolynomialTokenizer.java,v 1.13 2008/01/17 23:23:51 koga Exp $
 *
 * Copyright (C) 2004 Koga Laboratory. All rights reserved.
 *
 */

package org.mklab.nfc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <code>PolynomialTokenizer</code> クラスは、 多項式の文字列表現を項ごとに分割するためのクラスです。 内部で {@link java.util.regex.Pattern}を用いており、 区切り文字に使用する {@link java.util.regex.Pattern}は、 デフォルトで正規表現"[+][ ]|[-][ ]"をコンパイルしたものとなっている。
 * 
 * @author koga
 * @version $Revision: 1.13 $
 */
public class PolynomialTokenizer {

  /** 符号のパターン。 */
  private static Pattern SIGN_PATTERN = Pattern.compile("[+][ ]|[-][ ]"); //$NON-NLS-1$

  /** デリミタの長さ。 */
  private static int DELIMITER_LENGTH = 2;

  /** 現在の位置。 */
  private int currentPosition;

  /** 位置の最大値。 */
  private int maxPosition;

  /** トークン。 */
  private String[] tokens;

  /**
   * 指定した文字を分割するPolynomialTokenizerを作成します。
   * 
   * @param input 分割したい文字列
   */
  public PolynomialTokenizer(final CharSequence input) {
    this.currentPosition = 0;
    this.tokens = split(SIGN_PATTERN, input, DELIMITER_LENGTH, 0);
    this.maxPosition = this.tokens.length;
  }

  /**
   * 正規表現 {@link java.util.regex.Pattern}pに基づいて、多項式を表す 文字列inputを分割し {@link java.lang.String}の配列として返します。 <p>
   * 
   * なお、delimterLengthパラメータは区切り文字の長さを表し, limitパラメータはこのパターンの適用回数、 つまり、返される配列の長さを制御します。 delimlengthは区切り文字も分割する文字列に含めたいときに, 区切り文字の長さを指定すれば良い。 区切り文字を文字列に含めないときには0を指定します。
   * 
   * @param p 分割パターン
   * @param input 分割する文字列
   * @param delimterLength 区切り文字の長さ
   * @param limit 分割数
   * @return 多項式を項ごとに分割した文字列の配列
   */
  private static String[] split(final Pattern p, final CharSequence input, final int delimterLength, final int limit) {
    int index = 0;
    final boolean matchLimited = limit > 0;
    final List<String> matches = new ArrayList<>();
    final Matcher m = p.matcher(input);

    // Add segments before each match found
    while (m.find()) {
      if (!matchLimited || matches.size() < limit - 1) {
        final String match = input.subSequence(index, m.start() + delimterLength).toString();
        matches.add(match);
        index = m.end();
      } else if (matches.size() == limit - 1) { // last one
        final String match = input.subSequence(index, input.length()).toString();
        matches.add(match);
        index = m.end();
      }
    }

    // If no match was found, return this
    if (index == 0) {
      return new String[] {input.toString()};
    }

    // Add remaining segment
    if (!matchLimited || matches.size() < limit) {
      matches.add(input.subSequence(index, input.length()).toString());
    }

    // Construct result
    int resultSize = matches.size();
    if (limit == 0) {
      while (resultSize > 0 && matches.get(resultSize - 1).equals("")) { //$NON-NLS-1$
        resultSize--;
      }
    }

    final String[] result = new String[resultSize];
    return matches.subList(0, resultSize).toArray(result);
  }

  /**
   * 多項式を表す文字列polynomialStringを項ごとに分割し、width以内の文字列の配列として返します。 <p>
   * 
   * 例えば,多項式が p = "9 s^4 - 12 s^3 - 2 s^2 + 4 s + 1"で、 width = 20します。 
   * 
   * この時、項ごとに分割された"9 s^4 - ", "12 s^3 - ", "2 s^2 + ", "4 s + ", "1" はwidthに収まる項まで繋げられ <p>
   * 
   * "9 s^4 - 12 s^3 - 2 s^2 + 4 s + 1"<p>
   * 
   * が {@link java.lang.String}の配列として返されます。
   * 
   * @param input 多項式の文字列表現
   * @param width 幅指定
   * @return 項ごとに分割された、width以内の文字列の配列
   */
  public static String[] split(final CharSequence input, final int width) {
    final PolynomialTokenizer tokenizer = new PolynomialTokenizer(input);
    final List<String> matches = new ArrayList<>();
    while (tokenizer.hasMoreTokens()) {
      matches.add(tokenizer.nextLine(width));
    }
    
    return matches.toArray(new String[matches.size()]);
  }

  /**
   * トーカナイザの文字列に、まだ項があるか調べる。
   * 
   * @return 項があればtrue、無ければfalse
   */
  public final boolean hasMoreTokens() {
    return (this.currentPosition < this.maxPosition);
  }

  /**
   * トーカナイザの文字列から次の項を取り出して返します。
   * 
   * @return 項
   */
  private final String nextToken() {
    if (this.currentPosition >= this.maxPosition) {
      throw new NoSuchElementException();
    }
    return this.tokens[this.currentPosition++];
  }

  /**
   * 指定した幅に収まる項までの文字列を返します。
   * 
   * @param width 最大幅
   * @return 指定さた幅に収まる文字列
   */
  public final String nextLine(final int width) {
    final StringBuffer line = new StringBuffer(""); //$NON-NLS-1$
    boolean firstToken = true;
    while (hasMoreTokens()) {
      final String token = nextToken();
      if (line.length() + token.length() <= width) {
        line.append(token);
        firstToken = false;
      } else {
        if (firstToken) {
          return token;
        }
        this.currentPosition--;
        break;
      }
    }
    return line.toString();
  }
}