package org.example;

import org.mklab.nfc.fft.DoubleComplexFFTAnalyzer;
import org.mklab.nfc.fft.DoubleRealFFTAnalyzer;

/**
 * {@code org.mklab.nfc.fft}（高速フーリエ変換）と JS をつなぐクラスです。
 *
 * <p>戻り値はすべて [実部(1 x size), 虚部(1 x size)] です。</p>
 */
final class FftWasm {

    private FftWasm() {
    }

    static double[] fft(double[] data, int size) {
        return packComplex(DoubleRealFFTAnalyzer.fft(data, size));
    }

    static double[] ifft(double[] data, int size) {
        return packComplex(DoubleRealFFTAnalyzer.ifft(data, size));
    }

    static double[] complexFft(double[] re, double[] im, int size) {
        return packComplex(DoubleComplexFFTAnalyzer.fft(re, im, size));
    }

    static double[] complexIfft(double[] re, double[] im, int size) {
        return packComplex(DoubleComplexFFTAnalyzer.ifft(re, im, size));
    }

    private static double[] packComplex(double[][] reIm) {
        return Matrices.pack(Matrices.rowVector(reIm[0]), Matrices.rowVector(reIm[1]));
    }
}
