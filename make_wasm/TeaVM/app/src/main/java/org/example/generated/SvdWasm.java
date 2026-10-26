package org.example.generated;

import org.mklab.nfc.svd.DoubleComplexSingularValueDecomposer;
import org.mklab.nfc.svd.DoubleRealSingularValueDecomposer;
import org.mklab.nfc.svd.SingularValueDecompositionDoubleComplexElements;
import org.mklab.nfc.svd.SingularValueDecompositionDoubleRealElements;

/**
 * {@code org.mklab.nfc.svd} と JS をつなぐクラスです（JavaGenerator.py が自動生成。手で編集しないこと）。
 */
final class SvdWasm {

    private SvdWasm() {
    }

    /** {@code DoubleComplexSingularValueDecomposer.decompose} → SingularValueDecompositionDoubleComplexElements */
    static double[] complexSvdDecompose(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        SingularValueDecompositionDoubleComplexElements result = new DoubleComplexSingularValueDecomposer().decompose(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns));
        return Matrices.pack(result.getReU(), result.getImU(), result.getD(), result.getReV(), result.getImV());
    }

    /** {@code DoubleComplexSingularValueDecomposer.singularValue} → double[] */
    static double[] complexSvdSingularValue(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        double[] result = new DoubleComplexSingularValueDecomposer().singularValue(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns));
        return Matrices.pack(Matrices.rowVector(result));
    }

    /** {@code DoubleComplexSingularValueDecomposer.maximumSingularValue} → double */
    static double complexSvdMaximumSingularValue(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        double result = new DoubleComplexSingularValueDecomposer().maximumSingularValue(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns));
        return result;
    }

    /** {@code DoubleComplexSingularValueDecomposer.minimumSingularValue} → double */
    static double complexSvdMinimumSingularValue(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        double result = new DoubleComplexSingularValueDecomposer().minimumSingularValue(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns));
        return result;
    }

    /** {@code DoubleComplexSingularValueDecomposer.isSingular} → boolean */
    static boolean complexSvdIsSingular(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance) {
        boolean result = new DoubleComplexSingularValueDecomposer().isSingular(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns), tolerance);
        return result;
    }

    /** {@code DoubleComplexSingularValueDecomposer.rank} → int */
    static int complexSvdRank(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance) {
        int result = new DoubleComplexSingularValueDecomposer().rank(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns), tolerance);
        return result;
    }

    /** {@code DoubleComplexSingularValueDecomposer.isFullRank} → boolean */
    static boolean complexSvdIsFullRank(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance) {
        boolean result = new DoubleComplexSingularValueDecomposer().isFullRank(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns), tolerance);
        return result;
    }

    /** {@code DoubleComplexSingularValueDecomposer.pseudoInverse} → double[][][] */
    static double[] complexSvdPseudoInverse(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance) {
        double[][][] result = new DoubleComplexSingularValueDecomposer().pseudoInverse(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns), tolerance);
        return Matrices.pack(result);
    }

    /** {@code DoubleComplexSingularValueDecomposer.leastSquare} → double[][][] */
    static double[] complexSvdLeastSquare(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double[] bRe, int bReRows, int bReColumns, double[] bIm, int bImRows, int bImColumns, double toleance) {
        double[][][] result = new DoubleComplexSingularValueDecomposer().leastSquare(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns), Matrices.toMatrix(bRe, bReRows, bReColumns), Matrices.toMatrix(bIm, bImRows, bImColumns), toleance);
        return Matrices.pack(result);
    }

    /** {@code DoubleComplexSingularValueDecomposer.kernel} → double[][][] */
    static double[] complexSvdKernel(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance) {
        double[][][] result = new DoubleComplexSingularValueDecomposer().kernel(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns), tolerance);
        return Matrices.pack(result);
    }

    /** {@code DoubleComplexSingularValueDecomposer.norm} → double */
    static double complexSvdNorm(double[] ar, int arRows, int arColumns, double[] ai, int aiRows, int aiColumns) {
        double result = new DoubleComplexSingularValueDecomposer().norm(Matrices.toMatrix(ar, arRows, arColumns), Matrices.toMatrix(ai, aiRows, aiColumns));
        return result;
    }

    /** {@code DoubleComplexSingularValueDecomposer.conditionNumber} → double */
    static double complexSvdConditionNumber(double[] ar, int arRows, int arColumns, double[] ai, int aiRows, int aiColumns) {
        double result = new DoubleComplexSingularValueDecomposer().conditionNumber(Matrices.toMatrix(ar, arRows, arColumns), Matrices.toMatrix(ai, aiRows, aiColumns));
        return result;
    }

    /** {@code DoubleRealSingularValueDecomposer.decompose} → SingularValueDecompositionDoubleRealElements */
    static double[] realSvdDecompose(double[] a, int aRows, int aColumns) {
        SingularValueDecompositionDoubleRealElements result = new DoubleRealSingularValueDecomposer().decompose(Matrices.toMatrix(a, aRows, aColumns));
        return Matrices.pack(result.getU(), result.getD(), result.getV());
    }

    /** {@code DoubleRealSingularValueDecomposer.singularValue} → double[] */
    static double[] realSvdSingularValue(double[] a, int aRows, int aColumns) {
        double[] result = new DoubleRealSingularValueDecomposer().singularValue(Matrices.toMatrix(a, aRows, aColumns));
        return Matrices.pack(Matrices.rowVector(result));
    }

    /** {@code DoubleRealSingularValueDecomposer.maximumSingularValue} → double */
    static double realSvdMaximumSingularValue(double[] a, int aRows, int aColumns) {
        double result = new DoubleRealSingularValueDecomposer().maximumSingularValue(Matrices.toMatrix(a, aRows, aColumns));
        return result;
    }

    /** {@code DoubleRealSingularValueDecomposer.minimumSingularValue} → double */
    static double realSvdMinimumSingularValue(double[] a, int aRows, int aColumns) {
        double result = new DoubleRealSingularValueDecomposer().minimumSingularValue(Matrices.toMatrix(a, aRows, aColumns));
        return result;
    }

    /** {@code DoubleRealSingularValueDecomposer.isSingular} → boolean */
    static boolean realSvdIsSingular(double[] a, int aRows, int aColumns, double tolerance) {
        boolean result = new DoubleRealSingularValueDecomposer().isSingular(Matrices.toMatrix(a, aRows, aColumns), tolerance);
        return result;
    }

    /** {@code DoubleRealSingularValueDecomposer.rank} → int */
    static int realSvdRank(double[] a, int aRows, int aColumns, double tolerance) {
        int result = new DoubleRealSingularValueDecomposer().rank(Matrices.toMatrix(a, aRows, aColumns), tolerance);
        return result;
    }

    /** {@code DoubleRealSingularValueDecomposer.isFullRank} → boolean */
    static boolean realSvdIsFullRank(double[] a, int aRows, int aColumns, double tolerance) {
        boolean result = new DoubleRealSingularValueDecomposer().isFullRank(Matrices.toMatrix(a, aRows, aColumns), tolerance);
        return result;
    }

    /** {@code DoubleRealSingularValueDecomposer.pseudoInverse} → double[][] */
    static double[] realSvdPseudoInverse(double[] a, int aRows, int aColumns, double tolerance) {
        double[][] result = new DoubleRealSingularValueDecomposer().pseudoInverse(Matrices.toMatrix(a, aRows, aColumns), tolerance);
        return Matrices.pack(result);
    }

    /** {@code DoubleRealSingularValueDecomposer.leastSquare} → double[][] */
    static double[] realSvdLeastSquare(double[] a, int aRows, int aColumns, double[] b, int bRows, int bColumns, double tolerance) {
        double[][] result = new DoubleRealSingularValueDecomposer().leastSquare(Matrices.toMatrix(a, aRows, aColumns), Matrices.toMatrix(b, bRows, bColumns), tolerance);
        return Matrices.pack(result);
    }

    /** {@code DoubleRealSingularValueDecomposer.kernel} → double[][] */
    static double[] realSvdKernel(double[] a, int aRows, int aColumns, double tolerance) {
        double[][] result = new DoubleRealSingularValueDecomposer().kernel(Matrices.toMatrix(a, aRows, aColumns), tolerance);
        return Matrices.pack(result);
    }

    /** {@code DoubleRealSingularValueDecomposer.norm} → double */
    static double realSvdNorm(double[] a, int aRows, int aColumns) {
        double result = new DoubleRealSingularValueDecomposer().norm(Matrices.toMatrix(a, aRows, aColumns));
        return result;
    }

    /** {@code DoubleRealSingularValueDecomposer.conditionNumber} → double */
    static double realSvdConditionNumber(double[] a, int aRows, int aColumns) {
        double result = new DoubleRealSingularValueDecomposer().conditionNumber(Matrices.toMatrix(a, aRows, aColumns));
        return result;
    }
}
