/*
 * Created on 2007/04/30
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.ode;

import java.util.List;


/**
 * 区分的システムのユーティティクラスです。
 * 
 * @author koga
 * @version $Revision: 1.2 $, 2007/04/30
 */
public final class PiecewiseUtil {
  /**
   * 新しく生成された<code>PiecewiseUtil</code>オブジェクトを初期化します。
   */
  private PiecewiseUtil() {
    // nothing to do
  }

  /**
   * 区分の異なる成分の番号(1から始まる)を返します。
   * 
   * <p>複数の成分の区分が異なる場合、最小の成分の番号を返します。 <p>区分の異なる成分が無い場合、0を返します。
   * 
   * @param pieces1 区分のリスト1
   * @param pieces2 区分のリスト2
   * @return 区分の異なる成分の番号(1から始まる)
   */
  public static int getDistinctPiece(final List<Integer> pieces1, final List<Integer> pieces2) {
    final int size1 = pieces1.size();
    final int size2 = pieces2.size();

    if (size1 != size2) {
      throw new IllegalArgumentException(Messages.getString("PiecewiseUtil.0")); //$NON-NLS-1$
    }

    for (int i = 0; i < size1; i++) {
      final int piece1 = pieces1.get(i).intValue();
      final int piece2 = pieces2.get(i).intValue();

      if (piece1 != piece2) {
        return i + 1;
      }
    }

    return 0;
  }

}
