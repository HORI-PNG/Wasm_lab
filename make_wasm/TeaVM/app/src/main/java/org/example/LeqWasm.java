package org.example;

import org.mklab.nfc.leq.DoubleLUDecompositionElements;
import org.mklab.nfc.leq.DoubleRealLUDecomposer;

/**
 * {@code org.mklab.nfc.leq}（連立一次方程式・LU 分解）と JS をつなぐクラスです。
 *
 * <p>特異（正則でない）な場合は例外を投げる設定で NFC を呼び出します。</p>
 */
final class LeqWasm {

    private static final boolean STOP_IF_SINGULAR = true;

    private LeqWasm() {
    }

    /** LU 分解。[L, U] */
    static double[] lu(double[] values, int size, double tolerance) {
        DoubleLUDecompositionElements result = new DoubleRealLUDecomposer()
                .decompose(Matrices.toSquareMatrix(values, size), tolerance, STOP_IF_SINGULAR);
        return Matrices.pack(result.getL(), result.getU());
    }

    /** 行置換付き LU 分解。[L, U, P] */
    static double[] luWithPermutation(double[] values, int size, double tolerance) {
        DoubleLUDecompositionElements result = new DoubleRealLUDecomposer()
                .decomposeWithPermutation(Matrices.toSquareMatrix(values, size), tolerance, STOP_IF_SINGULAR);
        return Matrices.pack(result.getL(), result.getU(), Matrices.toDouble(result.getP()));
    }

    /** 逆行列。[A^-1] */
    static double[] inverse(double[] values, int size, double tolerance) {
        double[][] inverse = new DoubleRealLUDecomposer()
                .inverse(Matrices.toSquareMatrix(values, size), tolerance, STOP_IF_SINGULAR);
        return Matrices.pack(inverse);
    }

    /** A X = B の解 X。[X] */
    static double[] solve(double[] a, int size, double[] b, int bColumns, double tolerance) {
        double[][] x = new DoubleRealLUDecomposer().leftDivide(
                Matrices.toSquareMatrix(a, size),
                Matrices.toMatrix(b, size, bColumns),
                tolerance, STOP_IF_SINGULAR);
        return Matrices.pack(x);
    }

    /** 行列式。 */
    static double determinant(double[] values, int size, double tolerance) {
        return new DoubleRealLUDecomposer()
                .getDeterminant(Matrices.toSquareMatrix(values, size), tolerance, false);
    }
}
