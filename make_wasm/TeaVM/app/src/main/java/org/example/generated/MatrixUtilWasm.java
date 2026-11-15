package org.example.generated;

import org.mklab.nfc.matrix.DoubleMatrixUtil;

/**
 * {@code org.mklab.nfc.matrix} と JS をつなぐクラスです（JavaGenerator.py が自動生成。手で編集しないこと）。
 */
final class MatrixUtilWasm {

    private MatrixUtilWasm() {
    }

    /** {@code DoubleMatrixUtil.equals} → boolean */
    static boolean matrixEquals(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        boolean result = DoubleMatrixUtil.equals(Matrices.toMatrix(a1, a1Rows, a1Columns), Matrices.toMatrix(a2, a2Rows, a2Columns));
        return result;
    }

    /** {@code DoubleMatrixUtil.equals} → boolean */
    static boolean matrixEqualsWithTolerance(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns, double tolerance) {
        boolean result = DoubleMatrixUtil.equals(Matrices.toMatrix(a1, a1Rows, a1Columns), Matrices.toMatrix(a2, a2Rows, a2Columns), tolerance);
        return result;
    }

    /** {@code DoubleMatrixUtil.getSubMatrix} → double[][] */
    static double[] matrixGetSubMatrixWithRowMinRowMaxColumnMinColumnMax(double[] matrix, int matrixRows, int matrixColumns, int rowMin, int rowMax, int columnMin, int columnMax) {
        double[][] result = DoubleMatrixUtil.getSubMatrix(Matrices.toMatrix(matrix, matrixRows, matrixColumns), rowMin, rowMax, columnMin, columnMax);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.getSubMatrix} → double[][] */
    static double[] matrixGetSubMatrixWithColumnMinColumnMax(double[] matrix, int matrixRows, int matrixColumns, int[] rowIndex, int columnMin, int columnMax) {
        double[][] result = DoubleMatrixUtil.getSubMatrix(Matrices.toMatrix(matrix, matrixRows, matrixColumns), rowIndex, columnMin, columnMax);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.getSubMatrix} → double[][] */
    static double[] matrixGetSubMatrixWithRowMinRowMax(double[] matrix, int matrixRows, int matrixColumns, int rowMin, int rowMax, int[] columnIndex) {
        double[][] result = DoubleMatrixUtil.getSubMatrix(Matrices.toMatrix(matrix, matrixRows, matrixColumns), rowMin, rowMax, columnIndex);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.getSubMatrix} → double[][] */
    static double[] matrixGetSubMatrix(double[] matrix, int matrixRows, int matrixColumns, int[] rowIndex, int[] columnIndex) {
        double[][] result = DoubleMatrixUtil.getSubMatrix(Matrices.toMatrix(matrix, matrixRows, matrixColumns), rowIndex, columnIndex);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.getSubVector} → double[] */
    static double[] matrixGetSubVector(double[] vector, int[] index) {
        double[] result = DoubleMatrixUtil.getSubVector(vector, index);
        return Matrices.pack(Matrices.rowVector(result));
    }

    /** {@code DoubleMatrixUtil.appendDown} → double[][] */
    static double[] matrixAppendDown(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        double[][] result = DoubleMatrixUtil.appendDown(Matrices.toMatrix(a1, a1Rows, a1Columns), Matrices.toMatrix(a2, a2Rows, a2Columns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.appendRight} → double[][] */
    static double[] matrixAppendRight(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        double[][] result = DoubleMatrixUtil.appendRight(Matrices.toMatrix(a1, a1Rows, a1Columns), Matrices.toMatrix(a2, a2Rows, a2Columns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.anyZero} → boolean */
    static boolean matrixAnyZero(double[] vector) {
        boolean result = DoubleMatrixUtil.anyZero(vector);
        return result;
    }

    /** {@code DoubleMatrixUtil.clone} → double[][] */
    static double[] matrixClone(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.clone(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.isZero} → boolean */
    static boolean matrixIsZero(double[] matrix, int matrixRows, int matrixColumns, double tolerance) {
        boolean result = DoubleMatrixUtil.isZero(Matrices.toMatrix(matrix, matrixRows, matrixColumns), tolerance);
        return result;
    }

    /** {@code DoubleMatrixUtil.isUnit} → boolean */
    static boolean matrixIsUnit(double[] matrix, int matrixRows, int matrixColumns, double tolerance) {
        boolean result = DoubleMatrixUtil.isUnit(Matrices.toMatrix(matrix, matrixRows, matrixColumns), tolerance);
        return result;
    }

    /** {@code DoubleMatrixUtil.transpose} → double[][] */
    static double[] matrixTranspose(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.transpose(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.transpose} → double[][] */
    static double[] matrixTransposeWithVector(double[] matrix) {
        double[][] result = DoubleMatrixUtil.transpose(matrix);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.inverseElementWise} → double[][] */
    static double[] matrixInverseElementWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.inverseElementWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.multiply} → double[][] */
    static double[] matrixMultiply(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        double[][] result = DoubleMatrixUtil.multiply(Matrices.toMatrix(a1, a1Rows, a1Columns), Matrices.toMatrix(a2, a2Rows, a2Columns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.createUnit} → double[][] */
    static double[] matrixCreateUnit(int rowSize, int columnSize) {
        double[][] result = DoubleMatrixUtil.createUnit(rowSize, columnSize);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.createOnes} → double[][] */
    static double[] matrixCreateOnes(int rowSize, int columnSize) {
        double[][] result = DoubleMatrixUtil.createOnes(rowSize, columnSize);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.createZero} → double[][] */
    static double[] matrixCreateZeroWithRowSizeColumnSize(int rowSize, int columnSize) {
        double[][] result = DoubleMatrixUtil.createZero(rowSize, columnSize);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.createZero} → double[] */
    static double[] matrixCreateZero(int size) {
        double[] result = DoubleMatrixUtil.createZero(size);
        return Matrices.pack(Matrices.rowVector(result));
    }

    /** {@code DoubleMatrixUtil.series} → double[] */
    static double[] matrixSeries(double from, double to, double by) {
        double[] result = DoubleMatrixUtil.series(from, to, by);
        return Matrices.pack(Matrices.rowVector(result));
    }

    /** {@code DoubleMatrixUtil.multiply} → double[][] */
    static double[] matrixMultiplyWithMatrixInt(double[] matrix, int matrixRows, int matrixColumns, int scalar) {
        double[][] result = DoubleMatrixUtil.multiply(Matrices.toMatrix(matrix, matrixRows, matrixColumns), scalar);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.multiply} → double[][] */
    static double[] matrixMultiplyWithMatrixDouble(double[] matrix, int matrixRows, int matrixColumns, double scalar) {
        double[][] result = DoubleMatrixUtil.multiply(Matrices.toMatrix(matrix, matrixRows, matrixColumns), scalar);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.unaryMinus} → double[][] */
    static double[] matrixUnaryMinus(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.unaryMinus(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.vectorToDiagonal} → double[][] */
    static double[] matrixVectorToDiagonal(double[] vector) {
        double[][] result = DoubleMatrixUtil.vectorToDiagonal(vector);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.removeRowVectors} → double[][] */
    static double[] matrixRemoveRowVectorsWithRowMinRowMax(double[] matrix, int matrixRows, int matrixColumns, int rowMin, int rowMax) {
        double[][] result = DoubleMatrixUtil.removeRowVectors(Matrices.toMatrix(matrix, matrixRows, matrixColumns), rowMin, rowMax);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.removeRowVectors} → double[][] */
    static double[] matrixRemoveRowVectors(double[] matrix, int matrixRows, int matrixColumns, int[] rowIndex) {
        double[][] result = DoubleMatrixUtil.removeRowVectors(Matrices.toMatrix(matrix, matrixRows, matrixColumns), rowIndex);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.removeColumnVectors} → double[][] */
    static double[] matrixRemoveColumnVectorsWithColumnMinColumnMax(double[] matrix, int matrixRows, int matrixColumns, int columnMin, int columnMax) {
        double[][] result = DoubleMatrixUtil.removeColumnVectors(Matrices.toMatrix(matrix, matrixRows, matrixColumns), columnMin, columnMax);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.removeColumnVectors} → double[][] */
    static double[] matrixRemoveColumnVectors(double[] matrix, int matrixRows, int matrixColumns, int[] columnIndex) {
        double[][] result = DoubleMatrixUtil.removeColumnVectors(Matrices.toMatrix(matrix, matrixRows, matrixColumns), columnIndex);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.reshape} → double[][] */
    static double[] matrixReshape(double[] matrix, int matrixRows, int matrixColumns, int newRowSize, int newColumnSize) {
        double[][] result = DoubleMatrixUtil.reshape(Matrices.toMatrix(matrix, matrixRows, matrixColumns), newRowSize, newColumnSize);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.multiplyElementWise} → double[][] */
    static double[] matrixMultiplyElementWise(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        double[][] result = DoubleMatrixUtil.multiplyElementWise(Matrices.toMatrix(a1, a1Rows, a1Columns), Matrices.toMatrix(a2, a2Rows, a2Columns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.maxElementWise} → double[][] */
    static double[] matrixMaxElementWise(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        double[][] result = DoubleMatrixUtil.maxElementWise(Matrices.toMatrix(a1, a1Rows, a1Columns), Matrices.toMatrix(a2, a2Rows, a2Columns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.minElementWise} → double[][] */
    static double[] matrixMinElementWise(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        double[][] result = DoubleMatrixUtil.minElementWise(Matrices.toMatrix(a1, a1Rows, a1Columns), Matrices.toMatrix(a2, a2Rows, a2Columns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.divide} → double[][] */
    static double[] matrixDivideWithMatrixInt(double[] matrix, int matrixRows, int matrixColumns, int scalar) {
        double[][] result = DoubleMatrixUtil.divide(Matrices.toMatrix(matrix, matrixRows, matrixColumns), scalar);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.divide} → double[][] */
    static double[] matrixDivide(double[] matrix, int matrixRows, int matrixColumns, double scalar) {
        double[][] result = DoubleMatrixUtil.divide(Matrices.toMatrix(matrix, matrixRows, matrixColumns), scalar);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.divideElementWise} → double[][] */
    static double[] matrixDivideElementWise(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        double[][] result = DoubleMatrixUtil.divideElementWise(Matrices.toMatrix(a1, a1Rows, a1Columns), Matrices.toMatrix(a2, a2Rows, a2Columns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.leftDivideElementWise} → double[][] */
    static double[] matrixLeftDivideElementWise(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        double[][] result = DoubleMatrixUtil.leftDivideElementWise(Matrices.toMatrix(a1, a1Rows, a1Columns), Matrices.toMatrix(a2, a2Rows, a2Columns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.addElementWise} → double[][] */
    static double[] matrixAddElementWise(double[] matrix, int matrixRows, int matrixColumns, double scalar) {
        double[][] result = DoubleMatrixUtil.addElementWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns), scalar);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.addElementWise} → double[][] */
    static double[] matrixAddElementWiseWithIntMatrixDouble(double[] matrix, int matrixRows, int matrixColumns, double scalar) {
        double[][] result = DoubleMatrixUtil.addElementWise(Matrices.toIntMatrix(matrix, matrixRows, matrixColumns), scalar);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.subtractElementWise} → double[][] */
    static double[] matrixSubtractElementWise(double[] matrix, int matrixRows, int matrixColumns, double scalar) {
        double[][] result = DoubleMatrixUtil.subtractElementWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns), scalar);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.subtractElementWise} → double[][] */
    static double[] matrixSubtractElementWiseWithIntMatrixDouble(double[] matrix, int matrixRows, int matrixColumns, double scalar) {
        double[][] result = DoubleMatrixUtil.subtractElementWise(Matrices.toIntMatrix(matrix, matrixRows, matrixColumns), scalar);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.powerElementWise} → double[][] */
    static double[] matrixPowerElementWise(double scalar, double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.powerElementWise(scalar, Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.powerElementWise} → double[][] */
    static double[] matrixPowerElementWiseWithMatrixDouble(double[] matrix, int matrixRows, int matrixColumns, double scalar) {
        double[][] result = DoubleMatrixUtil.powerElementWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns), scalar);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.powerElementWise} → double[][] */
    static double[] matrixPowerElementWiseWithMatrixMatrix(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        double[][] result = DoubleMatrixUtil.powerElementWise(Matrices.toMatrix(a1, a1Rows, a1Columns), Matrices.toMatrix(a2, a2Rows, a2Columns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.powerElementWise} → double[][] */
    static double[] matrixPowerElementWiseWithMatrixIntMatrix(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        double[][] result = DoubleMatrixUtil.powerElementWise(Matrices.toMatrix(a1, a1Rows, a1Columns), Matrices.toIntMatrix(a2, a2Rows, a2Columns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.add} → double[][] */
    static double[] matrixAdd(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        double[][] result = DoubleMatrixUtil.add(Matrices.toMatrix(a1, a1Rows, a1Columns), Matrices.toMatrix(a2, a2Rows, a2Columns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.subtract} → double[][] */
    static double[] matrixSubtract(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        double[][] result = DoubleMatrixUtil.subtract(Matrices.toMatrix(a1, a1Rows, a1Columns), Matrices.toMatrix(a2, a2Rows, a2Columns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.max} → double */
    static double matrixMax(double[] matrix, int matrixRows, int matrixColumns) {
        double result = DoubleMatrixUtil.max(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return result;
    }

    /** {@code DoubleMatrixUtil.max} → double */
    static double matrixMaxWithVector(double[] vector) {
        double result = DoubleMatrixUtil.max(vector);
        return result;
    }

    /** {@code DoubleMatrixUtil.min} → double */
    static double matrixMin(double[] matrix, int matrixRows, int matrixColumns) {
        double result = DoubleMatrixUtil.min(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return result;
    }

    /** {@code DoubleMatrixUtil.min} → double */
    static double matrixMinWithVector(double[] vector) {
        double result = DoubleMatrixUtil.min(vector);
        return result;
    }

    /** {@code DoubleMatrixUtil.sum} → double */
    static double matrixSum(double[] matrix, int matrixRows, int matrixColumns) {
        double result = DoubleMatrixUtil.sum(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return result;
    }

    /** {@code DoubleMatrixUtil.product} → double */
    static double matrixProduct(double[] matrix, int matrixRows, int matrixColumns) {
        double result = DoubleMatrixUtil.product(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return result;
    }

    /** {@code DoubleMatrixUtil.mean} → double */
    static double matrixMean(double[] matrix, int matrixRows, int matrixColumns) {
        double result = DoubleMatrixUtil.mean(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return result;
    }

    /** {@code DoubleMatrixUtil.std} → double */
    static double matrixStd(double[] matrix, int matrixRows, int matrixColumns) {
        double result = DoubleMatrixUtil.std(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return result;
    }

    /** {@code DoubleMatrixUtil.frobNorm} → double */
    static double matrixFrobNorm(double[] matrix, int matrixRows, int matrixColumns) {
        double result = DoubleMatrixUtil.frobNorm(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return result;
    }

    /** {@code DoubleMatrixUtil.frobNormRowWise} → double[][] */
    static double[] matrixFrobNormRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.frobNormRowWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.frobNormColumnWise} → double[][] */
    static double[] matrixFrobNormColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.frobNormColumnWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.infNorm} → double */
    static double matrixInfNorm(double[] matrix, int matrixRows, int matrixColumns) {
        double result = DoubleMatrixUtil.infNorm(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return result;
    }

    /** {@code DoubleMatrixUtil.trace} → double */
    static double matrixTrace(double[] matrix, int matrixRows, int matrixColumns) {
        double result = DoubleMatrixUtil.trace(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return result;
    }

    /** {@code DoubleMatrixUtil.diagonalToVector} → double[][] */
    static double[] matrixDiagonalToVector(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.diagonalToVector(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.productRowWise} → double[][] */
    static double[] matrixProductRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.productRowWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.productColumnWise} → double[][] */
    static double[] matrixProductColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.productColumnWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.sumRowWise} → double[][] */
    static double[] matrixSumRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.sumRowWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.sumColumnWise} → double[][] */
    static double[] matrixSumColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.sumColumnWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.meanColumnWise} → double[][] */
    static double[] matrixMeanColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.meanColumnWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.meanRowWise} → double[][] */
    static double[] matrixMeanRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.meanRowWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.cumulativeProductColumnWise} → double[][] */
    static double[] matrixCumulativeProductColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.cumulativeProductColumnWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.cumulativeProductRowWise} → double[][] */
    static double[] matrixCumulativeProductRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.cumulativeProductRowWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.cumulativeSumColumnWise} → double[][] */
    static double[] matrixCumulativeSumColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.cumulativeSumColumnWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.cumulativeSumRowWise} → double[][] */
    static double[] matrixCumulativeSumRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.cumulativeSumRowWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.minColumnWise} → double[][] */
    static double[] matrixMinColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.minColumnWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.minRowWise} → double[][] */
    static double[] matrixMinRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.minRowWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.maxColumnWise} → double[][] */
    static double[] matrixMaxColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.maxColumnWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.maxRowWise} → double[][] */
    static double[] matrixMaxRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.maxRowWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.stdColumnWise} → double[][] */
    static double[] matrixStdColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.stdColumnWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.stdRowWise} → double[][] */
    static double[] matrixStdRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.stdRowWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.cumulativeProduct} → double[][] */
    static double[] matrixCumulativeProduct(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.cumulativeProduct(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.cumulativeSum} → double[][] */
    static double[] matrixCumulativeSum(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.cumulativeSum(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.indexOfMinimum} → int[] */
    static double[] matrixIndexOfMinimum(double[] matrix, int matrixRows, int matrixColumns) {
        int[] result = DoubleMatrixUtil.indexOfMinimum(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(Matrices.rowVector(Matrices.toDouble(result)));
    }

    /** {@code DoubleMatrixUtil.indexOfMaximum} → int[] */
    static double[] matrixIndexOfMaximum(double[] matrix, int matrixRows, int matrixColumns) {
        int[] result = DoubleMatrixUtil.indexOfMaximum(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(Matrices.rowVector(Matrices.toDouble(result)));
    }

    /** {@code DoubleMatrixUtil.resize} → double[][] */
    static double[] matrixResize(double[] matrix, int matrixRows, int matrixColumns, int newRowSize, int newColumnSize) {
        double[][] result = DoubleMatrixUtil.resize(Matrices.toMatrix(matrix, matrixRows, matrixColumns), newRowSize, newColumnSize);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.powerElementWise} → double[][] */
    static double[] matrixPowerElementWiseWithIntMatrixDouble(double[] matrix, int matrixRows, int matrixColumns, double d) {
        double[][] result = DoubleMatrixUtil.powerElementWise(Matrices.toIntMatrix(matrix, matrixRows, matrixColumns), d);
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.powerElementWise} → double[][] */
    static double[] matrixPowerElementWiseWithIntMatrixMatrix(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        double[][] result = DoubleMatrixUtil.powerElementWise(Matrices.toIntMatrix(a1, a1Rows, a1Columns), Matrices.toMatrix(a2, a2Rows, a2Columns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.powerElementWise} → double[][] */
    static double[] matrixPowerElementWiseWithDoubleIntMatrix(double scalar, double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.powerElementWise(scalar, Matrices.toIntMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.createArray} → double[][] */
    static double[] matrixCreateArray(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.createArray(Matrices.toIntMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.median} → double */
    static double matrixMedian(double[] matrix, int matrixRows, int matrixColumns) {
        double result = DoubleMatrixUtil.median(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return result;
    }

    /** {@code DoubleMatrixUtil.medianColumnWise} → double[][] */
    static double[] matrixMedianColumnWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.medianColumnWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.medianRowWise} → double[][] */
    static double[] matrixMedianRowWise(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.medianRowWise(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.covariance} → double[][] */
    static double[] matrixCovariance(double[] a1, int a1Rows, int a1Columns, double[] a2, int a2Rows, int a2Columns) {
        double[][] result = DoubleMatrixUtil.covariance(Matrices.toMatrix(a1, a1Rows, a1Columns), Matrices.toMatrix(a2, a2Rows, a2Columns));
        return Matrices.pack(result);
    }

    /** {@code DoubleMatrixUtil.variance} → double */
    static double matrixVariance(double[] matrix, int matrixRows, int matrixColumns) {
        double result = DoubleMatrixUtil.variance(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return result;
    }

    /** {@code DoubleMatrixUtil.makeColumnVector} → double[][] */
    static double[] matrixMakeColumnVector(double[] matrix, int matrixRows, int matrixColumns) {
        double[][] result = DoubleMatrixUtil.makeColumnVector(Matrices.toMatrix(matrix, matrixRows, matrixColumns));
        return Matrices.pack(result);
    }
}
