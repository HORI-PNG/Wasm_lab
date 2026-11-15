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

    // ---- org.mklab.nfc.matrix ----

    @JSExport
    public static boolean matrixEquals(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        return MatrixUtilWasm.matrixEquals(a1, a1Rows, a1Columns, a2, a2Rows, a2Columns);
    }

    @JSExport
    public static boolean matrixEqualsWithTolerance(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns, double tolerance) {
        return MatrixUtilWasm.matrixEqualsWithTolerance(a1, a1Rows, a1Columns, a2, a2Rows, a2Columns, tolerance);
    }

    @JSExport
    public static double[] matrixGetSubMatrixWithRowMinRowMaxColumnMinColumnMax(double[] matrix, int matrixRows, int matrixColumns, int rowMin, int rowMax, int columnMin, int columnMax) {
        return MatrixUtilWasm.matrixGetSubMatrixWithRowMinRowMaxColumnMinColumnMax(matrix, matrixRows, matrixColumns, rowMin, rowMax, columnMin, columnMax);
    }

    @JSExport
    public static double[] matrixGetSubMatrixWithColumnMinColumnMax(double[] matrix, int matrixRows, int matrixColumns, int[] rowIndex, int columnMin, int columnMax) {
        return MatrixUtilWasm.matrixGetSubMatrixWithColumnMinColumnMax(matrix, matrixRows, matrixColumns, rowIndex, columnMin, columnMax);
    }

    @JSExport
    public static double[] matrixGetSubMatrixWithRowMinRowMax(double[] matrix, int matrixRows, int matrixColumns, int rowMin, int rowMax, int[] columnIndex) {
        return MatrixUtilWasm.matrixGetSubMatrixWithRowMinRowMax(matrix, matrixRows, matrixColumns, rowMin, rowMax, columnIndex);
    }

    @JSExport
    public static double[] matrixGetSubMatrix(double[] matrix, int matrixRows, int matrixColumns, int[] rowIndex, int[] columnIndex) {
        return MatrixUtilWasm.matrixGetSubMatrix(matrix, matrixRows, matrixColumns, rowIndex, columnIndex);
    }

    @JSExport
    public static double[] matrixGetSubVector(double[] vector, int[] index) {
        return MatrixUtilWasm.matrixGetSubVector(vector, index);
    }

    @JSExport
    public static double[] matrixAppendDown(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        return MatrixUtilWasm.matrixAppendDown(a1, a1Rows, a1Columns, a2, a2Rows, a2Columns);
    }

    @JSExport
    public static double[] matrixAppendRight(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        return MatrixUtilWasm.matrixAppendRight(a1, a1Rows, a1Columns, a2, a2Rows, a2Columns);
    }

    @JSExport
    public static boolean matrixAnyZero(double[] vector) {
        return MatrixUtilWasm.matrixAnyZero(vector);
    }

    @JSExport
    public static double[] matrixClone(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixClone(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static boolean matrixIsZero(double[] matrix, int matrixRows, int matrixColumns, double tolerance) {
        return MatrixUtilWasm.matrixIsZero(matrix, matrixRows, matrixColumns, tolerance);
    }

    @JSExport
    public static boolean matrixIsUnit(double[] matrix, int matrixRows, int matrixColumns, double tolerance) {
        return MatrixUtilWasm.matrixIsUnit(matrix, matrixRows, matrixColumns, tolerance);
    }

    @JSExport
    public static double[] matrixTranspose(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixTranspose(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixTransposeWithVector(double[] matrix) {
        return MatrixUtilWasm.matrixTransposeWithVector(matrix);
    }

    @JSExport
    public static double[] matrixInverseElementWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixInverseElementWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixMultiply(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        return MatrixUtilWasm.matrixMultiply(a1, a1Rows, a1Columns, a2, a2Rows, a2Columns);
    }

    @JSExport
    public static double[] matrixCreateUnit(int rowSize, int columnSize) {
        return MatrixUtilWasm.matrixCreateUnit(rowSize, columnSize);
    }

    @JSExport
    public static double[] matrixCreateOnes(int rowSize, int columnSize) {
        return MatrixUtilWasm.matrixCreateOnes(rowSize, columnSize);
    }

    @JSExport
    public static double[] matrixCreateZeroWithRowSizeColumnSize(int rowSize, int columnSize) {
        return MatrixUtilWasm.matrixCreateZeroWithRowSizeColumnSize(rowSize, columnSize);
    }

    @JSExport
    public static double[] matrixCreateZero(int size) {
        return MatrixUtilWasm.matrixCreateZero(size);
    }

    @JSExport
    public static double[] matrixSeries(double from, double to, double by) {
        return MatrixUtilWasm.matrixSeries(from, to, by);
    }

    @JSExport
    public static double[] matrixMultiplyWithMatrixInt(double[] matrix, int matrixRows, int matrixColumns, int scalar) {
        return MatrixUtilWasm.matrixMultiplyWithMatrixInt(matrix, matrixRows, matrixColumns, scalar);
    }

    @JSExport
    public static double[] matrixMultiplyWithMatrixDouble(double[] matrix, int matrixRows, int matrixColumns, double scalar) {
        return MatrixUtilWasm.matrixMultiplyWithMatrixDouble(matrix, matrixRows, matrixColumns, scalar);
    }

    @JSExport
    public static double[] matrixUnaryMinus(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixUnaryMinus(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixVectorToDiagonal(double[] vector) {
        return MatrixUtilWasm.matrixVectorToDiagonal(vector);
    }

    @JSExport
    public static double[] matrixRemoveRowVectorsWithRowMinRowMax(double[] matrix, int matrixRows, int matrixColumns, int rowMin, int rowMax) {
        return MatrixUtilWasm.matrixRemoveRowVectorsWithRowMinRowMax(matrix, matrixRows, matrixColumns, rowMin, rowMax);
    }

    @JSExport
    public static double[] matrixRemoveRowVectors(double[] matrix, int matrixRows, int matrixColumns, int[] rowIndex) {
        return MatrixUtilWasm.matrixRemoveRowVectors(matrix, matrixRows, matrixColumns, rowIndex);
    }

    @JSExport
    public static double[] matrixRemoveColumnVectorsWithColumnMinColumnMax(double[] matrix, int matrixRows, int matrixColumns, int columnMin, int columnMax) {
        return MatrixUtilWasm.matrixRemoveColumnVectorsWithColumnMinColumnMax(matrix, matrixRows, matrixColumns, columnMin, columnMax);
    }

    @JSExport
    public static double[] matrixRemoveColumnVectors(double[] matrix, int matrixRows, int matrixColumns, int[] columnIndex) {
        return MatrixUtilWasm.matrixRemoveColumnVectors(matrix, matrixRows, matrixColumns, columnIndex);
    }

    @JSExport
    public static double[] matrixReshape(double[] matrix, int matrixRows, int matrixColumns, int newRowSize, int newColumnSize) {
        return MatrixUtilWasm.matrixReshape(matrix, matrixRows, matrixColumns, newRowSize, newColumnSize);
    }

    @JSExport
    public static double[] matrixMultiplyElementWise(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        return MatrixUtilWasm.matrixMultiplyElementWise(a1, a1Rows, a1Columns, a2, a2Rows, a2Columns);
    }

    @JSExport
    public static double[] matrixMaxElementWise(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        return MatrixUtilWasm.matrixMaxElementWise(a1, a1Rows, a1Columns, a2, a2Rows, a2Columns);
    }

    @JSExport
    public static double[] matrixMinElementWise(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        return MatrixUtilWasm.matrixMinElementWise(a1, a1Rows, a1Columns, a2, a2Rows, a2Columns);
    }

    @JSExport
    public static double[] matrixDivideWithMatrixInt(double[] matrix, int matrixRows, int matrixColumns, int scalar) {
        return MatrixUtilWasm.matrixDivideWithMatrixInt(matrix, matrixRows, matrixColumns, scalar);
    }

    @JSExport
    public static double[] matrixDivide(double[] matrix, int matrixRows, int matrixColumns, double scalar) {
        return MatrixUtilWasm.matrixDivide(matrix, matrixRows, matrixColumns, scalar);
    }

    @JSExport
    public static double[] matrixDivideElementWise(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        return MatrixUtilWasm.matrixDivideElementWise(a1, a1Rows, a1Columns, a2, a2Rows, a2Columns);
    }

    @JSExport
    public static double[] matrixLeftDivideElementWise(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        return MatrixUtilWasm.matrixLeftDivideElementWise(a1, a1Rows, a1Columns, a2, a2Rows, a2Columns);
    }

    @JSExport
    public static double[] matrixAddElementWise(double[] matrix, int matrixRows, int matrixColumns, double scalar) {
        return MatrixUtilWasm.matrixAddElementWise(matrix, matrixRows, matrixColumns, scalar);
    }

    @JSExport
    public static double[] matrixAddElementWiseWithIntMatrixDouble(double[] matrix, int matrixRows, int matrixColumns, double scalar) {
        return MatrixUtilWasm.matrixAddElementWiseWithIntMatrixDouble(matrix, matrixRows, matrixColumns, scalar);
    }

    @JSExport
    public static double[] matrixSubtractElementWise(double[] matrix, int matrixRows, int matrixColumns, double scalar) {
        return MatrixUtilWasm.matrixSubtractElementWise(matrix, matrixRows, matrixColumns, scalar);
    }

    @JSExport
    public static double[] matrixSubtractElementWiseWithIntMatrixDouble(double[] matrix, int matrixRows, int matrixColumns, double scalar) {
        return MatrixUtilWasm.matrixSubtractElementWiseWithIntMatrixDouble(matrix, matrixRows, matrixColumns, scalar);
    }

    @JSExport
    public static double[] matrixPowerElementWise(double scalar, double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixPowerElementWise(scalar, matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixPowerElementWiseWithMatrixDouble(double[] matrix, int matrixRows, int matrixColumns, double scalar) {
        return MatrixUtilWasm.matrixPowerElementWiseWithMatrixDouble(matrix, matrixRows, matrixColumns, scalar);
    }

    @JSExport
    public static double[] matrixPowerElementWiseWithMatrixMatrix(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        return MatrixUtilWasm.matrixPowerElementWiseWithMatrixMatrix(a1, a1Rows, a1Columns, a2, a2Rows, a2Columns);
    }

    @JSExport
    public static double[] matrixPowerElementWiseWithMatrixIntMatrix(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        return MatrixUtilWasm.matrixPowerElementWiseWithMatrixIntMatrix(a1, a1Rows, a1Columns, a2, a2Rows, a2Columns);
    }

    @JSExport
    public static double[] matrixAdd(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        return MatrixUtilWasm.matrixAdd(a1, a1Rows, a1Columns, a2, a2Rows, a2Columns);
    }

    @JSExport
    public static double[] matrixSubtract(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        return MatrixUtilWasm.matrixSubtract(a1, a1Rows, a1Columns, a2, a2Rows, a2Columns);
    }

    @JSExport
    public static double matrixMax(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixMax(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double matrixMaxWithVector(double[] vector) {
        return MatrixUtilWasm.matrixMaxWithVector(vector);
    }

    @JSExport
    public static double matrixMin(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixMin(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double matrixMinWithVector(double[] vector) {
        return MatrixUtilWasm.matrixMinWithVector(vector);
    }

    @JSExport
    public static double matrixSum(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixSum(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double matrixProduct(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixProduct(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double matrixMean(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixMean(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double matrixStd(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixStd(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double matrixFrobNorm(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixFrobNorm(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixFrobNormRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixFrobNormRowWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixFrobNormColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixFrobNormColumnWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double matrixInfNorm(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixInfNorm(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double matrixTrace(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixTrace(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixDiagonalToVector(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixDiagonalToVector(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixProductRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixProductRowWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixProductColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixProductColumnWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixSumRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixSumRowWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixSumColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixSumColumnWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixMeanColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixMeanColumnWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixMeanRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixMeanRowWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixCumulativeProductColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixCumulativeProductColumnWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixCumulativeProductRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixCumulativeProductRowWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixCumulativeSumColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixCumulativeSumColumnWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixCumulativeSumRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixCumulativeSumRowWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixMinColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixMinColumnWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixMinRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixMinRowWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixMaxColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixMaxColumnWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixMaxRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixMaxRowWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixStdColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixStdColumnWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixStdRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixStdRowWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixCumulativeProduct(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixCumulativeProduct(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixCumulativeSum(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixCumulativeSum(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixIndexOfMinimum(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixIndexOfMinimum(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixIndexOfMaximum(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixIndexOfMaximum(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixResize(double[] matrix, int matrixRows, int matrixColumns, int newRowSize, int newColumnSize) {
        return MatrixUtilWasm.matrixResize(matrix, matrixRows, matrixColumns, newRowSize, newColumnSize);
    }

    @JSExport
    public static double[] matrixPowerElementWiseWithIntMatrixDouble(double[] matrix, int matrixRows, int matrixColumns, double d) {
        return MatrixUtilWasm.matrixPowerElementWiseWithIntMatrixDouble(matrix, matrixRows, matrixColumns, d);
    }

    @JSExport
    public static double[] matrixPowerElementWiseWithIntMatrixMatrix(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        return MatrixUtilWasm.matrixPowerElementWiseWithIntMatrixMatrix(a1, a1Rows, a1Columns, a2, a2Rows, a2Columns);
    }

    @JSExport
    public static double[] matrixPowerElementWiseWithDoubleIntMatrix(double scalar, double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixPowerElementWiseWithDoubleIntMatrix(scalar, matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixCreateArray(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixCreateArray(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double matrixMedian(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixMedian(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixMedianColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixMedianColumnWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixMedianRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixMedianRowWise(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixCovariance(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        return MatrixUtilWasm.matrixCovariance(a1, a1Rows, a1Columns, a2, a2Rows, a2Columns);
    }

    @JSExport
    public static double matrixVariance(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixVariance(matrix, matrixRows, matrixColumns);
    }

    @JSExport
    public static double[] matrixMakeColumnVector(double[] matrix, int matrixRows, int matrixColumns) {
        return MatrixUtilWasm.matrixMakeColumnVector(matrix, matrixRows, matrixColumns);
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

    // ---- 手書きのブリッジから差し込んだ関数（設定ファイルの extra_exports） ----

    @JSExport
    public static double[] multiply(double[] a, int aRows, int aColumns, double[] b, int bColumns) {
        return org.example.MatrixWasm.multiply(a, aRows, aColumns, b, bColumns);
    }

    @JSExport
    public static void prepareMultiply(double[] a, int aRows, int aColumns, double[] b, int bColumns) {
        org.example.MatrixWasm.prepareMultiply(a, aRows, aColumns, b, bColumns);
    }

    @JSExport
    public static double multiplyPrepared() {
        return org.example.MatrixWasm.multiplyPrepared();
    }
}
