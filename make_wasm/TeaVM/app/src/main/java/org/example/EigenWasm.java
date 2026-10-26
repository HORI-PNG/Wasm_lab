package org.example;

import org.mklab.nfc.eig.DoubleRealBalanceDecomposer;
import org.mklab.nfc.eig.DoubleRealEigenSolver;
import org.mklab.nfc.eig.DoubleRealGeneralizedEigenSolver;
import org.mklab.nfc.eig.DoubleRealHessenbergDecomposer;
import org.mklab.nfc.eig.DoubleRealQRDecomposer;
import org.mklab.nfc.eig.DoubleRealQZDecomposer;
import org.mklab.nfc.eig.DoubleRealSchurDecomposer;
import org.mklab.nfc.eig.EigenSolutionDoubleElements;
import org.mklab.nfc.eig.HessenbergDecompositionDoubleRealElements;
import org.mklab.nfc.eig.QRDecompositionDoubleRealElements;
import org.mklab.nfc.eig.QZDecompositionDoubleRealElements;
import org.mklab.nfc.eig.SchurDecompositionDoubleRealElements;
import org.mklab.nfc.eig.BalancedDecompositionDoubleRealElements;

/**
 * {@code org.mklab.nfc.eig}（固有値・行列分解）と JS をつなぐクラスです。
 *
 * <p>戻り値はすべて {@link Matrices#pack} 形式です。</p>
 */
final class EigenWasm {

    private EigenWasm() {
    }

    /** 固有値。[実部(1 x n), 虚部(1 x n)] */
    static double[] eigenValues(double[] values, int size) {
        EigenSolutionDoubleElements solution =
                new DoubleRealEigenSolver().solve(Matrices.toSquareMatrix(values, size));
        return Matrices.pack(
                Matrices.rowVector(solution.getReValue()),
                Matrices.rowVector(solution.getImValue()));
    }

    /** 固有値と固有ベクトル。[値の実部, 値の虚部, ベクトルの実部(n x n), ベクトルの虚部(n x n)] */
    static double[] eigen(double[] values, int size) {
        EigenSolutionDoubleElements solution =
                new DoubleRealEigenSolver().solve(Matrices.toSquareMatrix(values, size));
        return packSolution(solution);
    }

    /** 一般化固有値問題 A x = λ B x。戻り値の形式は {@link #eigen} と同じです。 */
    static double[] generalizedEigen(double[] a, double[] b, int size) {
        EigenSolutionDoubleElements solution = new DoubleRealGeneralizedEigenSolver().solve(
                Matrices.toSquareMatrix(a, size),
                Matrices.toSquareMatrix(b, size));
        return packSolution(solution);
    }

    /** Hessenberg 分解。[Q, H] */
    static double[] hessenberg(double[] values, int size) {
        HessenbergDecompositionDoubleRealElements result =
                new DoubleRealHessenbergDecomposer().decompose(Matrices.toSquareMatrix(values, size));
        return Matrices.pack(result.getQ(), result.getH());
    }

    /** Schur 分解。[U, T] */
    static double[] schur(double[] values, int size) {
        SchurDecompositionDoubleRealElements result =
                new DoubleRealSchurDecomposer().decompose(Matrices.toSquareMatrix(values, size));
        return Matrices.pack(result.getU(), result.getT());
    }

    /** QR 分解。[Q, R] */
    static double[] qr(double[] values, int rows, int columns) {
        QRDecompositionDoubleRealElements result =
                new DoubleRealQRDecomposer().decompose(Matrices.toMatrix(values, rows, columns));
        return Matrices.pack(result.getQ(), result.getR());
    }

    /** 列置換付き QR 分解。[Q, R, P] */
    static double[] qrWithPermutation(double[] values, int rows, int columns) {
        QRDecompositionDoubleRealElements result =
                new DoubleRealQRDecomposer().decomposeWithPermutation(Matrices.toMatrix(values, rows, columns));
        return Matrices.pack(result.getQ(), result.getR(), Matrices.toDouble(result.getP()));
    }

    /** QZ 分解。[AA, BB, Q, Z, X の実部, X の虚部] */
    static double[] qz(double[] a, double[] b, int size) {
        QZDecompositionDoubleRealElements result = new DoubleRealQZDecomposer().decompose(
                Matrices.toSquareMatrix(a, size),
                Matrices.toSquareMatrix(b, size));
        return Matrices.pack(
                result.getAA(), result.getBB(), result.getQ(), result.getZ(),
                result.getReX(), result.getImX());
    }

    /** 平衡化。[D, B] */
    static double[] balance(double[] values, int size) {
        BalancedDecompositionDoubleRealElements result =
                new DoubleRealBalanceDecomposer().decompose(Matrices.toSquareMatrix(values, size));
        return Matrices.pack(result.getD(), result.getB());
    }

    private static double[] packSolution(EigenSolutionDoubleElements solution) {
        return Matrices.pack(
                Matrices.rowVector(solution.getReValue()),
                Matrices.rowVector(solution.getImValue()),
                solution.getReVector(),
                solution.getImVector());
    }
}
