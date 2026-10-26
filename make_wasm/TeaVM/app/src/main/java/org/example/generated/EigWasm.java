package org.example.generated;

import org.mklab.nfc.eig.BalancedDecompositionDoubleComplexElements;
import org.mklab.nfc.eig.BalancedDecompositionDoubleRealElements;
import org.mklab.nfc.eig.DoubleComplexBalanceDecomposer;
import org.mklab.nfc.eig.DoubleComplexEigenSolver;
import org.mklab.nfc.eig.DoubleComplexGeneralizedEigenSolver;
import org.mklab.nfc.eig.DoubleComplexHessenbergDecomposer;
import org.mklab.nfc.eig.DoubleComplexQRDecomposer;
import org.mklab.nfc.eig.DoubleComplexQZDecomposer;
import org.mklab.nfc.eig.DoubleComplexSchurDecomposer;
import org.mklab.nfc.eig.DoubleRealBalanceDecomposer;
import org.mklab.nfc.eig.DoubleRealEigenSolver;
import org.mklab.nfc.eig.DoubleRealGeneralizedEigenSolver;
import org.mklab.nfc.eig.DoubleRealHessenbergDecomposer;
import org.mklab.nfc.eig.DoubleRealQRDecomposer;
import org.mklab.nfc.eig.DoubleRealQZDecomposer;
import org.mklab.nfc.eig.DoubleRealSchurDecomposer;
import org.mklab.nfc.eig.EigenSolutionDoubleElements;
import org.mklab.nfc.eig.HessenbergDecompositionComplexRealElements;
import org.mklab.nfc.eig.HessenbergDecompositionDoubleRealElements;
import org.mklab.nfc.eig.QRDecompositionDoubleComplexElements;
import org.mklab.nfc.eig.QRDecompositionDoubleRealElements;
import org.mklab.nfc.eig.QZDecompositionDoubleComplexElements;
import org.mklab.nfc.eig.QZDecompositionDoubleRealElements;
import org.mklab.nfc.eig.SchurDecompositionDoubleComplexElements;
import org.mklab.nfc.eig.SchurDecompositionDoubleRealElements;

/**
 * {@code org.mklab.nfc.eig} と JS をつなぐクラスです（JavaGenerator.py が自動生成。手で編集しないこと）。
 */
final class EigWasm {

    private EigWasm() {
    }

    /** {@code DoubleComplexBalanceDecomposer.decompose} → BalancedDecompositionDoubleComplexElements */
    static double[] complexBalanceDecompose(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        BalancedDecompositionDoubleComplexElements result = new DoubleComplexBalanceDecomposer().decompose(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns));
        return Matrices.pack(result.getReB(), result.getImB(), result.getD());
    }

    /** {@code DoubleComplexEigenSolver.solve} → EigenSolutionDoubleElements */
    static double[] complexEigenSolve(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        EigenSolutionDoubleElements result = new DoubleComplexEigenSolver().solve(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns));
        return Matrices.pack(Matrices.rowVector(result.getReValue()), Matrices.rowVector(result.getImValue()), result.getReVector(), result.getImVector());
    }

    /** {@code DoubleComplexEigenSolver.getEigenValue} → double[][] */
    static double[] complexEigenGetEigenValue(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        double[][] result = new DoubleComplexEigenSolver().getEigenValue(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleComplexEigenSolver.getEigenVector} → double[][][] */
    static double[] complexEigenGetEigenVector(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        double[][][] result = new DoubleComplexEigenSolver().getEigenVector(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleComplexGeneralizedEigenSolver.solve} → EigenSolutionDoubleElements */
    static double[] complexGeneralizedEigenSolve(double[] ar, int arRows, int arColumns, double[] ai, int aiRows, int aiColumns, double[] br, int brRows, int brColumns, double[] bi, int biRows, int biColumns) {
        EigenSolutionDoubleElements result = new DoubleComplexGeneralizedEigenSolver().solve(Matrices.toMatrix(ar, arRows, arColumns), Matrices.toMatrix(ai, aiRows, aiColumns), Matrices.toMatrix(br, brRows, brColumns), Matrices.toMatrix(bi, biRows, biColumns));
        return Matrices.pack(Matrices.rowVector(result.getReValue()), Matrices.rowVector(result.getImValue()), result.getReVector(), result.getImVector());
    }

    /** {@code DoubleComplexGeneralizedEigenSolver.getEigenValue} → double[][] */
    static double[] complexGeneralizedEigenGetEigenValue(double[] ar, int arRows, int arColumns, double[] ai, int aiRows, int aiColumns, double[] br, int brRows, int brColumns, double[] bi, int biRows, int biColumns) {
        double[][] result = new DoubleComplexGeneralizedEigenSolver().getEigenValue(Matrices.toMatrix(ar, arRows, arColumns), Matrices.toMatrix(ai, aiRows, aiColumns), Matrices.toMatrix(br, brRows, brColumns), Matrices.toMatrix(bi, biRows, biColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleComplexGeneralizedEigenSolver.getEigenVector} → double[][][] */
    static double[] complexGeneralizedEigenGetEigenVector(double[] ar, int arRows, int arColumns, double[] ai, int aiRows, int aiColumns, double[] br, int brRows, int brColumns, double[] bi, int biRows, int biColumns) {
        double[][][] result = new DoubleComplexGeneralizedEigenSolver().getEigenVector(Matrices.toMatrix(ar, arRows, arColumns), Matrices.toMatrix(ai, aiRows, aiColumns), Matrices.toMatrix(br, brRows, brColumns), Matrices.toMatrix(bi, biRows, biColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleComplexHessenbergDecomposer.decompose} → HessenbergDecompositionComplexRealElements */
    static double[] complexHessenbergDecompose(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        HessenbergDecompositionComplexRealElements result = new DoubleComplexHessenbergDecomposer().decompose(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns));
        return Matrices.pack(result.getReQ(), result.getImQ(), result.getReH(), result.getImH());
    }

    /** {@code DoubleComplexQRDecomposer.decompose} → QRDecompositionDoubleComplexElements */
    static double[] complexQrDecompose(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        QRDecompositionDoubleComplexElements result = new DoubleComplexQRDecomposer().decompose(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns));
        return Matrices.pack(result.getReQ(), result.getImQ(), result.getReR(), result.getImR(), Matrices.toDouble(result.getP()));
    }

    /** {@code DoubleComplexQRDecomposer.decomposeWithPermutation} → QRDecompositionDoubleComplexElements */
    static double[] complexQrDecomposeWithPermutation(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        QRDecompositionDoubleComplexElements result = new DoubleComplexQRDecomposer().decomposeWithPermutation(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns));
        return Matrices.pack(result.getReQ(), result.getImQ(), result.getReR(), result.getImR(), Matrices.toDouble(result.getP()));
    }

    /** {@code DoubleComplexQZDecomposer.qzDecompose} → QZDecompositionDoubleComplexElements */
    static double[] complexQzDecompose(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double[] bRe, int bReRows, int bReColumns, double[] bIm, int bImRows, int bImColumns) {
        QZDecompositionDoubleComplexElements result = new DoubleComplexQZDecomposer().qzDecompose(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns), Matrices.toMatrix(bRe, bReRows, bReColumns), Matrices.toMatrix(bIm, bImRows, bImColumns));
        return Matrices.pack(result.getReQ(), result.getImQ(), result.getReZ(), result.getImZ(), result.getReAA(), result.getImAA(), result.getReBB(), result.getImBB(), result.getReX(), result.getImX());
    }

    /** {@code DoubleComplexSchurDecomposer.decompose} → SchurDecompositionDoubleComplexElements */
    static double[] complexSchurDecompose(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        SchurDecompositionDoubleComplexElements result = new DoubleComplexSchurDecomposer().decompose(Matrices.toMatrix(aRe, aReRows, aReColumns), Matrices.toMatrix(aIm, aImRows, aImColumns));
        return Matrices.pack(result.getReU(), result.getImU(), result.getReT(), result.getImT());
    }

    /** {@code DoubleRealBalanceDecomposer.decompose} → BalancedDecompositionDoubleRealElements */
    static double[] realBalanceDecompose(double[] a, int aRows, int aColumns) {
        BalancedDecompositionDoubleRealElements result = new DoubleRealBalanceDecomposer().decompose(Matrices.toMatrix(a, aRows, aColumns));
        return Matrices.pack(result.getB(), result.getD());
    }

    /** {@code DoubleRealEigenSolver.solve} → EigenSolutionDoubleElements */
    static double[] realEigenSolve(double[] a, int aRows, int aColumns) {
        EigenSolutionDoubleElements result = new DoubleRealEigenSolver().solve(Matrices.toMatrix(a, aRows, aColumns));
        return Matrices.pack(Matrices.rowVector(result.getReValue()), Matrices.rowVector(result.getImValue()), result.getReVector(), result.getImVector());
    }

    /** {@code DoubleRealEigenSolver.getEigenValue} → double[][] */
    static double[] realEigenGetEigenValue(double[] a, int aRows, int aColumns) {
        double[][] result = new DoubleRealEigenSolver().getEigenValue(Matrices.toMatrix(a, aRows, aColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleRealEigenSolver.getEigenVector} → double[][][] */
    static double[] realEigenGetEigenVector(double[] a, int aRows, int aColumns) {
        double[][][] result = new DoubleRealEigenSolver().getEigenVector(Matrices.toMatrix(a, aRows, aColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleRealGeneralizedEigenSolver.solve} → EigenSolutionDoubleElements */
    static double[] realGeneralizedEigenSolve(double[] a, int aRows, int aColumns, double[] b, int bRows, int bColumns) {
        EigenSolutionDoubleElements result = new DoubleRealGeneralizedEigenSolver().solve(Matrices.toMatrix(a, aRows, aColumns), Matrices.toMatrix(b, bRows, bColumns));
        return Matrices.pack(Matrices.rowVector(result.getReValue()), Matrices.rowVector(result.getImValue()), result.getReVector(), result.getImVector());
    }

    /** {@code DoubleRealGeneralizedEigenSolver.getEigenValue} → double[][] */
    static double[] realGeneralizedEigenGetEigenValue(double[] a, int aRows, int aColumns, double[] b, int bRows, int bColumns) {
        double[][] result = new DoubleRealGeneralizedEigenSolver().getEigenValue(Matrices.toMatrix(a, aRows, aColumns), Matrices.toMatrix(b, bRows, bColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleRealGeneralizedEigenSolver.getEigenVector} → double[][][] */
    static double[] realGeneralizedEigenGetEigenVector(double[] a, int aRows, int aColumns, double[] b, int bRows, int bColumns) {
        double[][][] result = new DoubleRealGeneralizedEigenSolver().getEigenVector(Matrices.toMatrix(a, aRows, aColumns), Matrices.toMatrix(b, bRows, bColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleRealHessenbergDecomposer.decompose} → HessenbergDecompositionDoubleRealElements */
    static double[] realHessenbergDecompose(double[] a, int aRows, int aColumns) {
        HessenbergDecompositionDoubleRealElements result = new DoubleRealHessenbergDecomposer().decompose(Matrices.toMatrix(a, aRows, aColumns));
        return Matrices.pack(result.getQ(), result.getH());
    }

    /** {@code DoubleRealQRDecomposer.decompose} → QRDecompositionDoubleRealElements */
    static double[] realQrDecompose(double[] a, int aRows, int aColumns) {
        QRDecompositionDoubleRealElements result = new DoubleRealQRDecomposer().decompose(Matrices.toMatrix(a, aRows, aColumns));
        return Matrices.pack(result.getQ(), result.getR(), Matrices.toDouble(result.getP()));
    }

    /** {@code DoubleRealQRDecomposer.decomposeWithPermutation} → QRDecompositionDoubleRealElements */
    static double[] realQrDecomposeWithPermutation(double[] a, int aRows, int aColumns) {
        QRDecompositionDoubleRealElements result = new DoubleRealQRDecomposer().decomposeWithPermutation(Matrices.toMatrix(a, aRows, aColumns));
        return Matrices.pack(result.getQ(), result.getR(), Matrices.toDouble(result.getP()));
    }

    /** {@code DoubleRealQZDecomposer.decompose} → QZDecompositionDoubleRealElements */
    static double[] realQzDecompose(double[] a, int aRows, int aColumns, double[] b, int bRows, int bColumns) {
        QZDecompositionDoubleRealElements result = new DoubleRealQZDecomposer().decompose(Matrices.toMatrix(a, aRows, aColumns), Matrices.toMatrix(b, bRows, bColumns));
        return Matrices.pack(result.getQ(), result.getZ(), result.getAA(), result.getBB(), result.getReX(), result.getImX());
    }

    /** {@code DoubleRealSchurDecomposer.decompose} → SchurDecompositionDoubleRealElements */
    static double[] realSchurDecompose(double[] a, int aRows, int aColumns) {
        SchurDecompositionDoubleRealElements result = new DoubleRealSchurDecomposer().decompose(Matrices.toMatrix(a, aRows, aColumns));
        return Matrices.pack(result.getU(), result.getT());
    }
}
