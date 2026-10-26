package org.example;

import org.mklab.nfc.elf.DoubleRealExponentialMatrix;
import org.mklab.nfc.matrix.DoubleMatrix;

/**
 * {@code org.mklab.nfc.elf}（行列指数関数）と JS をつなぐクラスです。
 */
final class ElfWasm {

    private ElfWasm() {
    }

    /** 行列指数関数 e^A。[e^A] */
    static double[] exp(double[] values, int size) {
        DoubleMatrix result = DoubleRealExponentialMatrix.exp(
                new DoubleMatrix(Matrices.toSquareMatrix(values, size)));
        return Matrices.pack(result.getDoubleElements());
    }
}
