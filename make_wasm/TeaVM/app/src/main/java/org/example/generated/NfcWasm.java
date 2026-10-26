package org.example.generated;

import org.teavm.jso.JSExport;

/**
 * 自動生成したブリッジを JS に公開する入口です（JavaGenerator.py が自動生成。手で編集しないこと）。
 *
 * <p>TeaVM の mainClass にこのクラスを指定すると、下の @JSExport メソッドが JS から呼べるようになります。</p>
 */
public final class NfcWasm {

    private NfcWasm() {
    }

    public static void main(String[] args) {
        // JS から exports を呼ぶだけなので何もしない
    }

    // ---- org.mklab.nfc.eig ----

    @JSExport
    public static double[] complexBalanceDecompose(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        return EigWasm.complexBalanceDecompose(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns);
    }

    @JSExport
    public static double[] complexEigenSolve(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        return EigWasm.complexEigenSolve(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns);
    }

    @JSExport
    public static double[] complexEigenGetEigenValue(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        return EigWasm.complexEigenGetEigenValue(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns);
    }

    @JSExport
    public static double[] complexEigenGetEigenVector(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        return EigWasm.complexEigenGetEigenVector(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns);
    }

    @JSExport
    public static double[] complexGeneralizedEigenSolve(double[] ar, int arRows, int arColumns, double[] ai, int aiRows, int aiColumns, double[] br, int brRows, int brColumns, double[] bi, int biRows, int biColumns) {
        return EigWasm.complexGeneralizedEigenSolve(ar, arRows, arColumns, ai, aiRows, aiColumns, br, brRows, brColumns, bi, biRows, biColumns);
    }

    @JSExport
    public static double[] complexGeneralizedEigenGetEigenValue(double[] ar, int arRows, int arColumns, double[] ai, int aiRows, int aiColumns, double[] br, int brRows, int brColumns, double[] bi, int biRows, int biColumns) {
        return EigWasm.complexGeneralizedEigenGetEigenValue(ar, arRows, arColumns, ai, aiRows, aiColumns, br, brRows, brColumns, bi, biRows, biColumns);
    }

    @JSExport
    public static double[] complexGeneralizedEigenGetEigenVector(double[] ar, int arRows, int arColumns, double[] ai, int aiRows, int aiColumns, double[] br, int brRows, int brColumns, double[] bi, int biRows, int biColumns) {
        return EigWasm.complexGeneralizedEigenGetEigenVector(ar, arRows, arColumns, ai, aiRows, aiColumns, br, brRows, brColumns, bi, biRows, biColumns);
    }

    @JSExport
    public static double[] complexHessenbergDecompose(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        return EigWasm.complexHessenbergDecompose(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns);
    }

    @JSExport
    public static double[] complexQrDecompose(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        return EigWasm.complexQrDecompose(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns);
    }

    @JSExport
    public static double[] complexQrDecomposeWithPermutation(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        return EigWasm.complexQrDecomposeWithPermutation(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns);
    }

    @JSExport
    public static double[] complexQzDecompose(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double[] bRe, int bReRows, int bReColumns, double[] bIm, int bImRows, int bImColumns) {
        return EigWasm.complexQzDecompose(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns, bRe, bReRows, bReColumns, bIm, bImRows, bImColumns);
    }

    @JSExport
    public static double[] complexSchurDecompose(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        return EigWasm.complexSchurDecompose(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns);
    }

    @JSExport
    public static double[] realBalanceDecompose(double[] a, int aRows, int aColumns) {
        return EigWasm.realBalanceDecompose(a, aRows, aColumns);
    }

    @JSExport
    public static double[] realEigenSolve(double[] a, int aRows, int aColumns) {
        return EigWasm.realEigenSolve(a, aRows, aColumns);
    }

    @JSExport
    public static double[] realEigenGetEigenValue(double[] a, int aRows, int aColumns) {
        return EigWasm.realEigenGetEigenValue(a, aRows, aColumns);
    }

    @JSExport
    public static double[] realEigenGetEigenVector(double[] a, int aRows, int aColumns) {
        return EigWasm.realEigenGetEigenVector(a, aRows, aColumns);
    }

    @JSExport
    public static double[] realGeneralizedEigenSolve(double[] a, int aRows, int aColumns, double[] b, int bRows, int bColumns) {
        return EigWasm.realGeneralizedEigenSolve(a, aRows, aColumns, b, bRows, bColumns);
    }

    @JSExport
    public static double[] realGeneralizedEigenGetEigenValue(double[] a, int aRows, int aColumns, double[] b, int bRows, int bColumns) {
        return EigWasm.realGeneralizedEigenGetEigenValue(a, aRows, aColumns, b, bRows, bColumns);
    }

    @JSExport
    public static double[] realGeneralizedEigenGetEigenVector(double[] a, int aRows, int aColumns, double[] b, int bRows, int bColumns) {
        return EigWasm.realGeneralizedEigenGetEigenVector(a, aRows, aColumns, b, bRows, bColumns);
    }

    @JSExport
    public static double[] realHessenbergDecompose(double[] a, int aRows, int aColumns) {
        return EigWasm.realHessenbergDecompose(a, aRows, aColumns);
    }

    @JSExport
    public static double[] realQrDecompose(double[] a, int aRows, int aColumns) {
        return EigWasm.realQrDecompose(a, aRows, aColumns);
    }

    @JSExport
    public static double[] realQrDecomposeWithPermutation(double[] a, int aRows, int aColumns) {
        return EigWasm.realQrDecomposeWithPermutation(a, aRows, aColumns);
    }

    @JSExport
    public static double[] realQzDecompose(double[] a, int aRows, int aColumns, double[] b, int bRows, int bColumns) {
        return EigWasm.realQzDecompose(a, aRows, aColumns, b, bRows, bColumns);
    }

    @JSExport
    public static double[] realSchurDecompose(double[] a, int aRows, int aColumns) {
        return EigWasm.realSchurDecompose(a, aRows, aColumns);
    }

    // ---- org.mklab.nfc.elf ----

    @JSExport
    public static double[] complexExp(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        return ElfWasm.complexExp(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns);
    }

    @JSExport
    public static double[] complexExpWithTolerance(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance) {
        return ElfWasm.complexExpWithTolerance(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns, tolerance);
    }

    @JSExport
    public static int realGetMaxIteration() {
        return ElfWasm.realGetMaxIteration();
    }

    @JSExport
    public static void realSetMaxIteration(int maxIteration) {
        ElfWasm.realSetMaxIteration(maxIteration);
    }

    @JSExport
    public static double[] realExp(double[] a, int aRows, int aColumns) {
        return ElfWasm.realExp(a, aRows, aColumns);
    }

    @JSExport
    public static double[] realExpWithTolerance(double[] a, int aRows, int aColumns, double tolerance) {
        return ElfWasm.realExpWithTolerance(a, aRows, aColumns, tolerance);
    }

    // ---- org.mklab.nfc.fft ----

    @JSExport
    public static double[] complexFft(double[] reData, double[] imData, int size) {
        return FftWasm.complexFft(reData, imData, size);
    }

    @JSExport
    public static double[] complexFftIfft(double[] reData, double[] imData, int size) {
        return FftWasm.complexFftIfft(reData, imData, size);
    }

    @JSExport
    public static double[] realFft(double[] data, int size) {
        return FftWasm.realFft(data, size);
    }

    @JSExport
    public static double[] realFftIfft(double[] data, int size) {
        return FftWasm.realFftIfft(data, size);
    }

    // ---- org.mklab.nfc.leq ----

    @JSExport
    public static double[] complexLuDecompose(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance, boolean stopIfSingular) {
        return LeqWasm.complexLuDecompose(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns, tolerance, stopIfSingular);
    }

    @JSExport
    public static double[] complexLuDecomposeWithPermutation(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance, boolean stopIfSingular) {
        return LeqWasm.complexLuDecomposeWithPermutation(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns, tolerance, stopIfSingular);
    }

    @JSExport
    public static double[] complexLuInverse(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance, boolean stopIfSingular) {
        return LeqWasm.complexLuInverse(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns, tolerance, stopIfSingular);
    }

    @JSExport
    public static double[] complexLuLeftDivide(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double[] bRe, int bReRows, int bReColumns, double[] bIm, int bImRows, int bImColumns, double tolerance, boolean stopIfSingular) {
        return LeqWasm.complexLuLeftDivide(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns, bRe, bReRows, bReColumns, bIm, bImRows, bImColumns, tolerance, stopIfSingular);
    }

    @JSExport
    public static double[] complexLuGetDeterminant(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance, boolean stopIfSingular) {
        return LeqWasm.complexLuGetDeterminant(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns, tolerance, stopIfSingular);
    }

    @JSExport
    public static double[] realCholeskyDecompose(double[] A, int ARows, int AColumns, double tolerance) {
        return LeqWasm.realCholeskyDecompose(A, ARows, AColumns, tolerance);
    }

    @JSExport
    public static double[] realLuDecompose(double[] matrix, int matrixRows, int matrixColumns, double tolerance, boolean stopIfSingular) {
        return LeqWasm.realLuDecompose(matrix, matrixRows, matrixColumns, tolerance, stopIfSingular);
    }

    @JSExport
    public static double[] realLuDecomposeWithPermutation(double[] matrix, int matrixRows, int matrixColumns, double tolerance, boolean stopIfSingular) {
        return LeqWasm.realLuDecomposeWithPermutation(matrix, matrixRows, matrixColumns, tolerance, stopIfSingular);
    }

    @JSExport
    public static double[] realLuInverse(double[] a, int aRows, int aColumns, double tolerance, boolean stopIfSingular) {
        return LeqWasm.realLuInverse(a, aRows, aColumns, tolerance, stopIfSingular);
    }

    @JSExport
    public static double[] realLuLeftDivide(double[] a, int aRows, int aColumns, double[] b, int bRows, int bColumns, double tolerance, boolean stopIfSingular) {
        return LeqWasm.realLuLeftDivide(a, aRows, aColumns, b, bRows, bColumns, tolerance, stopIfSingular);
    }

    @JSExport
    public static double realLuGetDeterminant(double[] matrix, int matrixRows, int matrixColumns, double tolerance, boolean stopIfSingular) {
        return LeqWasm.realLuGetDeterminant(matrix, matrixRows, matrixColumns, tolerance, stopIfSingular);
    }

    // ---- org.mklab.nfc.svd ----

    @JSExport
    public static double[] complexSvdDecompose(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        return SvdWasm.complexSvdDecompose(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns);
    }

    @JSExport
    public static double[] complexSvdSingularValue(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        return SvdWasm.complexSvdSingularValue(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns);
    }

    @JSExport
    public static double complexSvdMaximumSingularValue(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        return SvdWasm.complexSvdMaximumSingularValue(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns);
    }

    @JSExport
    public static double complexSvdMinimumSingularValue(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns) {
        return SvdWasm.complexSvdMinimumSingularValue(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns);
    }

    @JSExport
    public static boolean complexSvdIsSingular(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance) {
        return SvdWasm.complexSvdIsSingular(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns, tolerance);
    }

    @JSExport
    public static int complexSvdRank(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance) {
        return SvdWasm.complexSvdRank(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns, tolerance);
    }

    @JSExport
    public static boolean complexSvdIsFullRank(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance) {
        return SvdWasm.complexSvdIsFullRank(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns, tolerance);
    }

    @JSExport
    public static double[] complexSvdPseudoInverse(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance) {
        return SvdWasm.complexSvdPseudoInverse(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns, tolerance);
    }

    @JSExport
    public static double[] complexSvdLeastSquare(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double[] bRe, int bReRows, int bReColumns, double[] bIm, int bImRows, int bImColumns, double toleance) {
        return SvdWasm.complexSvdLeastSquare(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns, bRe, bReRows, bReColumns, bIm, bImRows, bImColumns, toleance);
    }

    @JSExport
    public static double[] complexSvdKernel(double[] aRe, int aReRows, int aReColumns, double[] aIm, int aImRows, int aImColumns, double tolerance) {
        return SvdWasm.complexSvdKernel(aRe, aReRows, aReColumns, aIm, aImRows, aImColumns, tolerance);
    }

    @JSExport
    public static double complexSvdNorm(double[] ar, int arRows, int arColumns, double[] ai, int aiRows, int aiColumns) {
        return SvdWasm.complexSvdNorm(ar, arRows, arColumns, ai, aiRows, aiColumns);
    }

    @JSExport
    public static double complexSvdConditionNumber(double[] ar, int arRows, int arColumns, double[] ai, int aiRows, int aiColumns) {
        return SvdWasm.complexSvdConditionNumber(ar, arRows, arColumns, ai, aiRows, aiColumns);
    }

    @JSExport
    public static double[] realSvdDecompose(double[] a, int aRows, int aColumns) {
        return SvdWasm.realSvdDecompose(a, aRows, aColumns);
    }

    @JSExport
    public static double[] realSvdSingularValue(double[] a, int aRows, int aColumns) {
        return SvdWasm.realSvdSingularValue(a, aRows, aColumns);
    }

    @JSExport
    public static double realSvdMaximumSingularValue(double[] a, int aRows, int aColumns) {
        return SvdWasm.realSvdMaximumSingularValue(a, aRows, aColumns);
    }

    @JSExport
    public static double realSvdMinimumSingularValue(double[] a, int aRows, int aColumns) {
        return SvdWasm.realSvdMinimumSingularValue(a, aRows, aColumns);
    }

    @JSExport
    public static boolean realSvdIsSingular(double[] a, int aRows, int aColumns, double tolerance) {
        return SvdWasm.realSvdIsSingular(a, aRows, aColumns, tolerance);
    }

    @JSExport
    public static int realSvdRank(double[] a, int aRows, int aColumns, double tolerance) {
        return SvdWasm.realSvdRank(a, aRows, aColumns, tolerance);
    }

    @JSExport
    public static boolean realSvdIsFullRank(double[] a, int aRows, int aColumns, double tolerance) {
        return SvdWasm.realSvdIsFullRank(a, aRows, aColumns, tolerance);
    }

    @JSExport
    public static double[] realSvdPseudoInverse(double[] a, int aRows, int aColumns, double tolerance) {
        return SvdWasm.realSvdPseudoInverse(a, aRows, aColumns, tolerance);
    }

    @JSExport
    public static double[] realSvdLeastSquare(double[] a, int aRows, int aColumns, double[] b, int bRows, int bColumns, double tolerance) {
        return SvdWasm.realSvdLeastSquare(a, aRows, aColumns, b, bRows, bColumns, tolerance);
    }

    @JSExport
    public static double[] realSvdKernel(double[] a, int aRows, int aColumns, double tolerance) {
        return SvdWasm.realSvdKernel(a, aRows, aColumns, tolerance);
    }

    @JSExport
    public static double realSvdNorm(double[] a, int aRows, int aColumns) {
        return SvdWasm.realSvdNorm(a, aRows, aColumns);
    }

    @JSExport
    public static double realSvdConditionNumber(double[] a, int aRows, int aColumns) {
        return SvdWasm.realSvdConditionNumber(a, aRows, aColumns);
    }
}
