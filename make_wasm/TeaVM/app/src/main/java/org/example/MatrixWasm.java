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

    /** {@link #prepareMultiply} で Java 側に置いておく入力（計算だけの時間を測るため） */
    private static double[][] preparedA;
    private static double[][] preparedB;

    private MatrixWasm() {
    }

    /** 行列積 A B。[A B] */
    static double[] multiply(double[] a, int aRows, int aColumns, double[] b, int bColumns) {
        double[][] product = DoubleMatrixUtil.multiply(
                Matrices.toMatrix(a, aRows, aColumns),
                Matrices.toMatrix(b, aColumns, bColumns));
        return Matrices.pack(product);
    }

    /** A と B を Java 側の 2 次元配列に変換して保持します（JS との受け渡しを計測から外すため）。 */
    static void prepareMultiply(double[] a, int aRows, int aColumns, double[] b, int bColumns) {
        preparedA = Matrices.toMatrix(a, aRows, aColumns);
        preparedB = Matrices.toMatrix(b, aColumns, bColumns);
    }

    /**
     * 保持している A と B の積を計算します（受け渡しなしの計算のみ）。
     *
     * @return 結果の先頭と末尾の成分の和（結果を使ったことにして、計算が省かれないようにするため）
     */
    static double multiplyPrepared() {
        double[][] product = DoubleMatrixUtil.multiply(preparedA, preparedB);
        double[] last = product[product.length - 1];
        return product[0][0] + last[last.length - 1];
    }
}
