package org.example;

import org.mklab.nfc.matrix.DoubleMatrixUtil;

/**
 * {@code org.mklab.nfc.matrix}（行列の基本演算）と JS をつなぐクラスです。
 *
 * <p>行列積は NFC の {@link DoubleMatrixUtil#multiply(double[][], double[][])}
 * （{@code DoubleMatrix#multiply} の中身と同じ処理）をそのまま呼びます。
 * WebLLM_lab の基礎実験 1（RQ1）で、NFC の行列積の速さを測るために使います。</p>
 */
final class MatrixWasm {

    private MatrixWasm() {
    }

    /** 行列積 A B。[A B] */
    static double[] multiply(double[] a, int aRows, int aColumns, double[] b, int bColumns) {
        double[][] product = DoubleMatrixUtil.multiply(
                Matrices.toMatrix(a, aRows, aColumns),
                Matrices.toMatrix(b, aColumns, bColumns));
        return Matrices.pack(product);
    }
}
