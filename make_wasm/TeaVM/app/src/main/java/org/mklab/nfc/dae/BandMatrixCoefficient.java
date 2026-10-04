package org.mklab.nfc.dae;

/**
 * バンド行列を表すインターフェースです。
 * 
 * @author kageyama
 * @version $Revision$, 2011/11/12
 */
public interface BandMatrixCoefficient {

  /**
   * 上部バンド幅の値を返すメソッドです。
   * 
   * @return 上部バンド幅
   */
  int getUpperBandWidth();

  /**
   * 下部バンド幅の値を返すメソッドです。
   * 
   * @return 下部バンド幅
   */
  int getLowerBandWidth();

}
