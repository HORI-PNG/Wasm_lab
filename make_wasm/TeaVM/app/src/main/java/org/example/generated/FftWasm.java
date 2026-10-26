package org.example.generated;

import org.mklab.nfc.fft.DoubleComplexFFTAnalyzer;
import org.mklab.nfc.fft.DoubleRealFFTAnalyzer;

/**
 * {@code org.mklab.nfc.fft} と JS をつなぐクラスです（JavaGenerator.py が自動生成。手で編集しないこと）。
 */
final class FftWasm {

    private FftWasm() {
    }

    /** {@code DoubleComplexFFTAnalyzer.fft} → double[][] */
    static double[] complexFft(double[] reData, double[] imData, int size) {
        double[][] result = DoubleComplexFFTAnalyzer.fft(reData, imData, size);
        return Matrices.pack(result);
    }

    /** {@code DoubleComplexFFTAnalyzer.ifft} → double[][] */
    static double[] complexFftIfft(double[] reData, double[] imData, int size) {
        double[][] result = DoubleComplexFFTAnalyzer.ifft(reData, imData, size);
        return Matrices.pack(result);
    }

    /** {@code DoubleRealFFTAnalyzer.fft} → double[][] */
    static double[] realFft(double[] data, int size) {
        double[][] result = DoubleRealFFTAnalyzer.fft(data, size);
        return Matrices.pack(result);
    }

    /** {@code DoubleRealFFTAnalyzer.ifft} → double[][] */
    static double[] realFftIfft(double[] data, int size) {
        double[][] result = DoubleRealFFTAnalyzer.ifft(data, size);
        return Matrices.pack(result);
    }
}
