package org.example;

import org.mklab.nfc.svd.DoubleRealSingularValueDecomposer;
import org.mklab.nfc.svd.SingularValueDecompositionDoubleRealElements;

/**
 * {@code org.mklab.nfc.svd}（特異値分解）と JS をつなぐクラスです。
 */
final class SvdWasm {

    private SvdWasm() {
    }

    /** 特異値分解 A = U D V^T。[U, D, V] */
    static double[] svd(double[] values, int rows, int columns) {
        SingularValueDecompositionDoubleRealElements result = new DoubleRealSingularValueDecomposer()
                .decompose(Matrices.toMatrix(values, rows, columns));
        return Matrices.pack(result.getU(), result.getD(), result.getV());
    }

    /** 特異値。[1 x k] */
    static double[] singularValues(double[] values, int rows, int columns) {
        double[] singularValues = new DoubleRealSingularValueDecomposer()
                .singularValue(Matrices.toMatrix(values, rows, columns));
        return Matrices.pack(Matrices.rowVector(singularValues));
    }

    /** ランク。 */
    static int rank(double[] values, int rows, int columns, double tolerance) {
        return new DoubleRealSingularValueDecomposer()
                .rank(Matrices.toMatrix(values, rows, columns), tolerance);
    }

    /** 2 ノルム（最大特異値）。 */
    static double norm(double[] values, int rows, int columns) {
        return new DoubleRealSingularValueDecomposer()
                .norm(Matrices.toMatrix(values, rows, columns));
    }

    /** 条件数。 */
    static double conditionNumber(double[] values, int rows, int columns) {
        return new DoubleRealSingularValueDecomposer()
                .conditionNumber(Matrices.toMatrix(values, rows, columns));
    }

    /** 擬似逆行列。[A^+] */
    static double[] pseudoInverse(double[] values, int rows, int columns, double tolerance) {
        double[][] result = new DoubleRealSingularValueDecomposer()
                .pseudoInverse(Matrices.toMatrix(values, rows, columns), tolerance);
        return Matrices.pack(result);
    }

    /** 最小二乗解 min |A X - B|。[X] */
    static double[] leastSquare(double[] a, int rows, int columns, double[] b, int bColumns, double tolerance) {
        double[][] result = new DoubleRealSingularValueDecomposer().leastSquare(
                Matrices.toMatrix(a, rows, columns),
                Matrices.toMatrix(b, rows, bColumns),
                tolerance);
        return Matrices.pack(result);
    }

    /** 零空間（核）の基底。[K] */
    static double[] kernel(double[] values, int rows, int columns, double tolerance) {
        double[][] result = new DoubleRealSingularValueDecomposer()
                .kernel(Matrices.toMatrix(values, rows, columns), tolerance);
        return Matrices.pack(result);
    }
}
