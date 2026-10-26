package org.example.generated;

import org.mklab.nfc.elf.DoubleComplexExponentialMatrix;
import org.mklab.nfc.elf.DoubleRealExponentialMatrix;
import org.mklab.nfc.matrix.DoubleMatrix;

/**
 * {@code org.mklab.nfc.elf} と JS をつなぐクラスです（JavaGenerator.py が自動生成。手で編集しないこと）。
 */
final class ElfWasm {

    private ElfWasm() {
    }

    /** {@code DoubleComplexExponentialMatrix.exp} → double[][][] */
    static double[] complexExp(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        double[][][] result = DoubleComplexExponentialMatrix.exp(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleComplexExponentialMatrix.exp} → double[][][] */
    static double[] complexExpWithTolerance(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance) {
        double[][][] result = DoubleComplexExponentialMatrix.exp(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns), tolerance);
        return Matrices.pack(result);
    }

    /** {@code DoubleRealExponentialMatrix.getMaxIteration} → int */
    static int realGetMaxIteration() {
        int result = DoubleRealExponentialMatrix.getMaxIteration();
        return result;
    }

    /** {@code DoubleRealExponentialMatrix.setMaxIteration} → void */
    static void realSetMaxIteration(int maxIteration) {
        DoubleRealExponentialMatrix.setMaxIteration(maxIteration);
    }

    /** {@code DoubleRealExponentialMatrix.exp} → DoubleMatrix */
    static double[] realExp(double[] a, int aRows, int aColumns) {
        DoubleMatrix result = DoubleRealExponentialMatrix.exp(new DoubleMatrix(Matrices.toMatrix(a, aRows, aColumns)));
        return Matrices.pack(result.getDoubleElements());
    }

    /** {@code DoubleRealExponentialMatrix.exp} → DoubleMatrix */
    static double[] realExpWithTolerance(double[] a, int aRows, int aColumns, double tolerance) {
        DoubleMatrix result = DoubleRealExponentialMatrix.exp(new DoubleMatrix(Matrices.toMatrix(a, aRows, aColumns)), tolerance);
        return Matrices.pack(result.getDoubleElements());
    }
}
