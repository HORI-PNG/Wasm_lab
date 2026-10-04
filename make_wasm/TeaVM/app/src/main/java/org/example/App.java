package org.example;

import org.teavm.jso.JSExport;
import org.mklab.nfc.eig.DoubleRealEigenSolver;

public class App {

    public static void main(String[] args) {
        // pass
    }

    /**
     * JS/TS から呼び出す固有値計算メソッド
     * 2x2 行列 [a, b, c, d] を受け取り、固有値を返す
     */
    @JSExport
    public static double[] solveEigen2x2(double a, double b, double c, double d) {
        // 入力行列の作成
        double[][] matrix = {
            { a, b },
            { c, d }
        };

        // 固有値計算の実行
        DoubleRealEigenSolver solver = new DoubleRealEigenSolver();
        // double[] eigenvalues = solver.solve(matrix);

        // テスト用の仮返却
        return new double[]{ a + d, a * d - b * c };
    }
}