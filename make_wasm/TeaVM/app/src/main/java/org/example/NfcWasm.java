package org.example;

import org.teavm.jso.JSExport;

/**
 * NFC を WebAssembly として JS/TS に公開する唯一の入口です。
 *
 * <p>TeaVM は mainClass の {@code @JSExport} static メソッドだけを JS に公開するため、
 * ここでは各ブリッジクラス（{@link EigenWasm} など）へ処理を渡すだけにしています。</p>
 *
 * <p>行列は「行優先の1次元配列 + 行数/列数」で受け取り、行列を返すメソッドは
 * {@link Matrices#pack} 形式の配列を返します（JS 側の {@code nfc.js} で2次元配列に戻します）。</p>
 */
public final class NfcWasm {

    private NfcWasm() {
    }

    public static void main(String[] args) {
        // JS から exports を呼ぶだけなので何もしない
    }

    // ---- matrix ----

    @JSExport
    public static double[] multiply(double[] a, int aRows, int aColumns, double[] b, int bColumns) {
        return MatrixWasm.multiply(a, aRows, aColumns, b, bColumns);
    }

    @JSExport
    public static void prepareMultiply(double[] a, int aRows, int aColumns, double[] b, int bColumns) {
        MatrixWasm.prepareMultiply(a, aRows, aColumns, b, bColumns);
    }

    @JSExport
    public static double multiplyPrepared() {
        return MatrixWasm.multiplyPrepared();
    }

    // ---- eig ----

    @JSExport
    public static double[] eigenValues(double[] values, int size) {
        return EigenWasm.eigenValues(values, size);
    }

    @JSExport
    public static double[] eigen(double[] values, int size) {
        return EigenWasm.eigen(values, size);
    }

    @JSExport
    public static double[] generalizedEigen(double[] a, double[] b, int size) {
        return EigenWasm.generalizedEigen(a, b, size);
    }

    @JSExport
    public static double[] hessenberg(double[] values, int size) {
        return EigenWasm.hessenberg(values, size);
    }

    @JSExport
    public static double[] schur(double[] values, int size) {
        return EigenWasm.schur(values, size);
    }

    @JSExport
    public static double[] qr(double[] values, int rows, int columns) {
        return EigenWasm.qr(values, rows, columns);
    }

    @JSExport
    public static double[] qrWithPermutation(double[] values, int rows, int columns) {
        return EigenWasm.qrWithPermutation(values, rows, columns);
    }

    @JSExport
    public static double[] qz(double[] a, double[] b, int size) {
        return EigenWasm.qz(a, b, size);
    }

    @JSExport
    public static double[] balance(double[] values, int size) {
        return EigenWasm.balance(values, size);
    }

    // ---- leq ----

    @JSExport
    public static double[] lu(double[] values, int size, double tolerance) {
        return LeqWasm.lu(values, size, tolerance);
    }

    @JSExport
    public static double[] luWithPermutation(double[] values, int size, double tolerance) {
        return LeqWasm.luWithPermutation(values, size, tolerance);
    }

    @JSExport
    public static double[] inverse(double[] values, int size, double tolerance) {
        return LeqWasm.inverse(values, size, tolerance);
    }

    @JSExport
    public static double[] solve(double[] a, int size, double[] b, int bColumns, double tolerance) {
        return LeqWasm.solve(a, size, b, bColumns, tolerance);
    }

    @JSExport
    public static double determinant(double[] values, int size, double tolerance) {
        return LeqWasm.determinant(values, size, tolerance);
    }

    // ---- svd ----

    @JSExport
    public static double[] svd(double[] values, int rows, int columns) {
        return SvdWasm.svd(values, rows, columns);
    }

    @JSExport
    public static double[] singularValues(double[] values, int rows, int columns) {
        return SvdWasm.singularValues(values, rows, columns);
    }

    @JSExport
    public static int rank(double[] values, int rows, int columns, double tolerance) {
        return SvdWasm.rank(values, rows, columns, tolerance);
    }

    @JSExport
    public static double norm(double[] values, int rows, int columns) {
        return SvdWasm.norm(values, rows, columns);
    }

    @JSExport
    public static double conditionNumber(double[] values, int rows, int columns) {
        return SvdWasm.conditionNumber(values, rows, columns);
    }

    @JSExport
    public static double[] pseudoInverse(double[] values, int rows, int columns, double tolerance) {
        return SvdWasm.pseudoInverse(values, rows, columns, tolerance);
    }

    @JSExport
    public static double[] leastSquare(double[] a, int rows, int columns, double[] b, int bColumns, double tolerance) {
        return SvdWasm.leastSquare(a, rows, columns, b, bColumns, tolerance);
    }

    @JSExport
    public static double[] kernel(double[] values, int rows, int columns, double tolerance) {
        return SvdWasm.kernel(values, rows, columns, tolerance);
    }

    // ---- fft ----

    @JSExport
    public static double[] fft(double[] data, int size) {
        return FftWasm.fft(data, size);
    }

    @JSExport
    public static double[] ifft(double[] data, int size) {
        return FftWasm.ifft(data, size);
    }

    @JSExport
    public static double[] complexFft(double[] re, double[] im, int size) {
        return FftWasm.complexFft(re, im, size);
    }

    @JSExport
    public static double[] complexIfft(double[] re, double[] im, int size) {
        return FftWasm.complexIfft(re, im, size);
    }

    // ---- elf ----

    @JSExport
    public static double[] expm(double[] values, int size) {
        return ElfWasm.exp(values, size);
    }
}
