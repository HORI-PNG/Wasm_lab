/*
 * Created on 2007/02/05
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matrix;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * ファイルの形式を表わす列挙型です。
 * 
 * @author koga
 * @version $Revision: 1.3 $, 2007/02/05
 */
public enum MatrixFileType {
  /** MXデータ。 */
  MX_DATA("mx"), //$NON-NLS-1$
  /** MMデータ。 */
  MM_DATA("mm"), //$NON-NLS-1$
  /** MATデータ。 */
  MAT_DATA("mat"), //$NON-NLS-1$
  /** MATデータ。 */
  CSV_DATA("csv"); //$NON-NLS-1$

  /** 拡張子。 */
  private String extension;

  /** データ型のマップ。 */
  private static Map<String, MatrixFileType> typeMap = new HashMap<>();

  static {
    final Set<MatrixFileType> types = EnumSet.allOf(MatrixFileType.class);
    for (final MatrixFileType type : types) {
      typeMap.put(type.extension, type);
    }
  }

  /**
   * 新しく生成された<code>MatrixFileType</code>オブジェクトを初期化します。
   * 
   * @param extension 拡張子
   */
  private MatrixFileType(final String extension) {
    this.extension = extension;
  }

  /**
   * 拡張子を返します。
   * 
   * @return 拡張子
   */
  public String getExtension() {
    return this.extension;
  }

  /**
   * 拡張子に対応するファイルタイプを返します。
   * 
   * @param extension 拡張子
   * @return 拡張子に対応するファイルタイプ
   */
  public static MatrixFileType getFileType(final String extension) {
    return typeMap.get(extension);
  }
}
