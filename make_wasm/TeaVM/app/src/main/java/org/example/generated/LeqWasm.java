package org.example.generated;

import org.mklab.nfc.leq.DoubleComplexLUDecomposer;
import org.mklab.nfc.leq.DoubleComplexLUDecompositionElements;
import org.mklab.nfc.leq.DoubleLUDecompositionElements;
import org.mklab.nfc.leq.DoubleRealCholeskyDecomposer;
import org.mklab.nfc.leq.DoubleRealLUDecomposer;
import org.mklab.nfc.matrix.DoubleMatrix;

/**
 * {@code org.mklab.nfc.leq} と JS をつなぐクラスです（JavaGenerator.py が自動生成。手で編集しないこと）。
 */
final class LeqWasm {

    private LeqWasm() {
    }

    /** {@code DoubleComplexLUDecomposer.decompose} → DoubleComplexLUDecompositionElements */
    static double[] complexLuDecompose(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance, boolean stopIfSingular) {
        DoubleComplexLUDecompositionElements result = new DoubleComplexLUDecomposer().decompose(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns), tolerance, stopIfSingular);
        return Matrices.pack(result.getRealL(), result.getImaginaryL(), result.getRealU(), result.getImaginaryU(), Matrices.toDouble(result.getP()));
    }

    /** {@code DoubleComplexLUDecomposer.decomposeWithPermutation} → DoubleComplexLUDecompositionElements */
    static double[] complexLuDecomposeWithPermutation(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance, boolean stopIfSingular) {
        DoubleComplexLUDecompositionElements result = new DoubleComplexLUDecomposer().decomposeWithPermutation(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns), tolerance, stopIfSingular);
        return Matrices.pack(result.getRealL(), result.getImaginaryL(), result.getRealU(), result.getImaginaryU(), Matrices.toDouble(result.getP()));
    }

    /** {@code DoubleComplexLUDecomposer.inverse} → double[][][] */
    static double[] complexLuInverse(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance, boolean stopIfSingular) {
        double[][][] result = new DoubleComplexLUDecomposer().inverse(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns), tolerance, stopIfSingular);
        return Matrices.pack(result);
    }

    /** {@code DoubleComplexLUDecomposer.leftDivide} → double[][][] */
    static double[] complexLuLeftDivide(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double[] bRe, int bReRows, int bReColumns, double[] bIm, int bImRows, int bImColumns, double tolerance, boolean stopIfSingular) {
        double[][][] result = new DoubleComplexLUDecomposer().leftDivide(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns), Matrices.toMatrix(bRe, bReRows, bReColumns), Matrices.toMatrix(bIm, bImRows, bImColumns), tolerance, stopIfSingular);
        return Matrices.pack(result);
    }

    /** {@code DoubleComplexLUDecomposer.getDeterminant} → double[] */
    static double[] complexLuGetDeterminant(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance, boolean stopIfSingular) {
        double[] result = new DoubleComplexLUDecomposer().getDeterminant(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns), tolerance, stopIfSingular);
        return Matrices.pack(Matrices.rowVector(result));
    }

    /** {@code DoubleRealCholeskyDecomposer.decompose} → DoubleMatrix */
    static double[] realCholeskyDecompose(double[] A, int ARows, int AColumns, double tolerance) {
        DoubleMatrix result = new DoubleRealCholeskyDecomposer().decompose(new DoubleMatrix(Matrices.toMatrix(A, ARows, AColumns)), tolerance);
        return Matrices.pack(result.getDoubleElements());
    }

    /** {@code DoubleRealLUDecomposer.decompose} → DoubleLUDecompositionElements */
    static double[] realLuDecompose(double[] matrix, int matrixRows, int matrixColumns, double tolerance, boolean stopIfSingular) {
        DoubleLUDecompositionElements result = new DoubleRealLUDecomposer().decompose(Matrices.toMatrix(matrix, matrixRows, matrixColumns), tolerance, stopIfSingular);
        return Matrices.pack(result.getL(), result.getU(), Matrices.toDouble(result.getP()));
    }

    /** {@code DoubleRealLUDecomposer.decomposeWithPermutation} → DoubleLUDecompositionElements */
    static double[] realLuDecomposeWithPermutation(double[] matrix, int matrixRows, int matrixColumns, double tolerance, boolean stopIfSingular) {
        DoubleLUDecompositionElements result = new DoubleRealLUDecomposer().decomposeWithPermutation(Matrices.toMatrix(matrix, matrixRows, matrixColumns), tolerance, stopIfSingular);
        return Matrices.pack(result.getL(), result.getU(), Matrices.toDouble(result.getP()));
    }

    /** {@code DoubleRealLUDecomposer.inverse} → double[][] */
    static double[] realLuInverse(double[] a, int aRows, int aColumns, double tolerance, boolean stopIfSingular) {
        double[][] result = new DoubleRealLUDecomposer().inverse(Matrices.toMatrix(a, aRows, aColumns), tolerance, stopIfSingular);
        return Matrices.pack(result);
    }

    /** {@code DoubleRealLUDecomposer.leftDivide} → double[][] */
    static double[] realLuLeftDivide(double[] a, int aRows, int aColumns, double[] b, int bRows, int bColumns, double tolerance, boolean stopIfSingular) {
        double[][] result = new DoubleRealLUDecomposer().leftDivide(Matrices.toMatrix(a, aRows, aColumns), Matrices.toMatrix(b, bRows, bColumns), tolerance, stopIfSingular);
        return Matrices.pack(result);
    }

    /** {@code DoubleRealLUDecomposer.getDeterminant} → double */
    static double realLuGetDeterminant(double[] matrix, int matrixRows, int matrixColumns, double tolerance, boolean stopIfSingular) {
        double result = new DoubleRealLUDecomposer().getDeterminant(Matrices.toMatrix(matrix, matrixRows, matrixColumns), tolerance, stopIfSingular);
        return result;
    }
}
