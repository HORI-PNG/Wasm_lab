// NFC (TeaVM WasmGC) を 2 次元配列で扱うためのラッパー（JavaGenerator.py が自動生成。手で編集しないこと）。
// mainClass を org.example.generated.NfcWasm にしてビルドした wasm と組み合わせて使う。
//
//   const nfc = await loadNfcGenerated("wasm-gc/nfc.wasm");
//   const { U, T } = nfc.realSchurDecompose([[1, 2], [3, 4]]);

async function loadNfcGenerated(wasmUrl = "wasm-gc/nfc.wasm") {
  /** 2次元配列 → { values: 行優先の1次元配列, rows, cols } */
  function flatten(matrix) {
    const rows = matrix.length;
    const cols = rows === 0 ? 0 : matrix[0].length;
    const values = new Array(rows * cols);
    for (let r = 0; r < rows; r++) {
      if (matrix[r].length !== cols) throw new Error("行ごとの列数が揃っていません");
      for (let c = 0; c < cols; c++) values[r * cols + c] = matrix[r][c];
    }
    return { values, rows, cols };
  }

  /** [k, r1, c1, ..., data...] → 2次元配列の配列 */
  function unpack(packed) {
    const count = packed[0];
    let offset = 1 + count * 2;
    const result = [];
    for (let i = 0; i < count; i++) {
      const rows = packed[1 + i * 2];
      const cols = packed[2 + i * 2];
      const m = [];
      for (let r = 0; r < rows; r++) {
        m.push(Array.from(packed.slice(offset, offset + cols)));
        offset += cols;
      }
      result.push(m);
    }
    return result;
  }

  /** unpack した行列の並びに名前を付ける。kinds の値が "vector" なら 1 x n 行列を 1 次元配列に戻す */
  function named(matrices, kinds) {
    const out = {};
    Object.keys(kinds).forEach((key, i) => {
      out[key] = kinds[key] === "vector" ? matrices[i][0] : matrices[i];
    });
    return out;
  }

  const teavm = await TeaVM.wasmGC.load(wasmUrl);
  const ex = teavm.exports;

  return {
    exports: ex,

    // ---- org.mklab.nfc.eig ----
    /** DoubleComplexBalanceDecomposer.decompose */
    complexBalanceDecompose(aRe, aIm) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return named(unpack(ex.complexBalanceDecompose(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols)), { ReB: "matrix", ImB: "matrix", D: "matrix" });
    },
    /** DoubleComplexEigenSolver.solve */
    complexEigenSolve(aRe, aIm) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return named(unpack(ex.complexEigenSolve(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols)), { ReValue: "vector", ImValue: "vector", ReVector: "matrix", ImVector: "matrix" });
    },
    /** DoubleComplexEigenSolver.getEigenValue */
    complexEigenGetEigenValue(aRe, aIm) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return unpack(ex.complexEigenGetEigenValue(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols))[0];
    },
    /** DoubleComplexEigenSolver.getEigenVector */
    complexEigenGetEigenVector(aRe, aIm) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return unpack(ex.complexEigenGetEigenVector(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols));
    },
    /** DoubleComplexGeneralizedEigenSolver.solve */
    complexGeneralizedEigenSolve(ar, ai, br, bi) {
      const arF = flatten(ar);
      const aiF = flatten(ai);
      const brF = flatten(br);
      const biF = flatten(bi);
      return named(unpack(ex.complexGeneralizedEigenSolve(arF.values, arF.rows, arF.cols, aiF.values, aiF.rows, aiF.cols, brF.values, brF.rows, brF.cols, biF.values, biF.rows, biF.cols)), { ReValue: "vector", ImValue: "vector", ReVector: "matrix", ImVector: "matrix" });
    },
    /** DoubleComplexGeneralizedEigenSolver.getEigenValue */
    complexGeneralizedEigenGetEigenValue(ar, ai, br, bi) {
      const arF = flatten(ar);
      const aiF = flatten(ai);
      const brF = flatten(br);
      const biF = flatten(bi);
      return unpack(ex.complexGeneralizedEigenGetEigenValue(arF.values, arF.rows, arF.cols, aiF.values, aiF.rows, aiF.cols, brF.values, brF.rows, brF.cols, biF.values, biF.rows, biF.cols))[0];
    },
    /** DoubleComplexGeneralizedEigenSolver.getEigenVector */
    complexGeneralizedEigenGetEigenVector(ar, ai, br, bi) {
      const arF = flatten(ar);
      const aiF = flatten(ai);
      const brF = flatten(br);
      const biF = flatten(bi);
      return unpack(ex.complexGeneralizedEigenGetEigenVector(arF.values, arF.rows, arF.cols, aiF.values, aiF.rows, aiF.cols, brF.values, brF.rows, brF.cols, biF.values, biF.rows, biF.cols));
    },
    /** DoubleComplexHessenbergDecomposer.decompose */
    complexHessenbergDecompose(aRe, aIm) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return named(unpack(ex.complexHessenbergDecompose(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols)), { ReQ: "matrix", ImQ: "matrix", ReH: "matrix", ImH: "matrix" });
    },
    /** DoubleComplexQRDecomposer.decompose */
    complexQrDecompose(aRe, aIm) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return named(unpack(ex.complexQrDecompose(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols)), { ReQ: "matrix", ImQ: "matrix", ReR: "matrix", ImR: "matrix", P: "matrix" });
    },
    /** DoubleComplexQRDecomposer.decomposeWithPermutation */
    complexQrDecomposeWithPermutation(aRe, aIm) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return named(unpack(ex.complexQrDecomposeWithPermutation(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols)), { ReQ: "matrix", ImQ: "matrix", ReR: "matrix", ImR: "matrix", P: "matrix" });
    },
    /** DoubleComplexQZDecomposer.qzDecompose */
    complexQzDecompose(aRe, aIm, bRe, bIm) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      const bReF = flatten(bRe);
      const bImF = flatten(bIm);
      return named(unpack(ex.complexQzDecompose(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols, bReF.values, bReF.rows, bReF.cols, bImF.values, bImF.rows, bImF.cols)), { ReQ: "matrix", ImQ: "matrix", ReZ: "matrix", ImZ: "matrix", ReAA: "matrix", ImAA: "matrix", ReBB: "matrix", ImBB: "matrix", ReX: "matrix", ImX: "matrix" });
    },
    /** DoubleComplexSchurDecomposer.decompose */
    complexSchurDecompose(aRe, aIm) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return named(unpack(ex.complexSchurDecompose(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols)), { ReU: "matrix", ImU: "matrix", ReT: "matrix", ImT: "matrix" });
    },
    /** DoubleRealBalanceDecomposer.decompose */
    realBalanceDecompose(a) {
      const aF = flatten(a);
      return named(unpack(ex.realBalanceDecompose(aF.values, aF.rows, aF.cols)), { B: "matrix", D: "matrix" });
    },
    /** DoubleRealEigenSolver.solve */
    realEigenSolve(a) {
      const aF = flatten(a);
      return named(unpack(ex.realEigenSolve(aF.values, aF.rows, aF.cols)), { ReValue: "vector", ImValue: "vector", ReVector: "matrix", ImVector: "matrix" });
    },
    /** DoubleRealEigenSolver.getEigenValue */
    realEigenGetEigenValue(a) {
      const aF = flatten(a);
      return unpack(ex.realEigenGetEigenValue(aF.values, aF.rows, aF.cols))[0];
    },
    /** DoubleRealEigenSolver.getEigenVector */
    realEigenGetEigenVector(a) {
      const aF = flatten(a);
      return unpack(ex.realEigenGetEigenVector(aF.values, aF.rows, aF.cols));
    },
    /** DoubleRealGeneralizedEigenSolver.solve */
    realGeneralizedEigenSolve(a, b) {
      const aF = flatten(a);
      const bF = flatten(b);
      return named(unpack(ex.realGeneralizedEigenSolve(aF.values, aF.rows, aF.cols, bF.values, bF.rows, bF.cols)), { ReValue: "vector", ImValue: "vector", ReVector: "matrix", ImVector: "matrix" });
    },
    /** DoubleRealGeneralizedEigenSolver.getEigenValue */
    realGeneralizedEigenGetEigenValue(a, b) {
      const aF = flatten(a);
      const bF = flatten(b);
      return unpack(ex.realGeneralizedEigenGetEigenValue(aF.values, aF.rows, aF.cols, bF.values, bF.rows, bF.cols))[0];
    },
    /** DoubleRealGeneralizedEigenSolver.getEigenVector */
    realGeneralizedEigenGetEigenVector(a, b) {
      const aF = flatten(a);
      const bF = flatten(b);
      return unpack(ex.realGeneralizedEigenGetEigenVector(aF.values, aF.rows, aF.cols, bF.values, bF.rows, bF.cols));
    },
    /** DoubleRealHessenbergDecomposer.decompose */
    realHessenbergDecompose(a) {
      const aF = flatten(a);
      return named(unpack(ex.realHessenbergDecompose(aF.values, aF.rows, aF.cols)), { Q: "matrix", H: "matrix" });
    },
    /** DoubleRealQRDecomposer.decompose */
    realQrDecompose(a) {
      const aF = flatten(a);
      return named(unpack(ex.realQrDecompose(aF.values, aF.rows, aF.cols)), { Q: "matrix", R: "matrix", P: "matrix" });
    },
    /** DoubleRealQRDecomposer.decomposeWithPermutation */
    realQrDecomposeWithPermutation(a) {
      const aF = flatten(a);
      return named(unpack(ex.realQrDecomposeWithPermutation(aF.values, aF.rows, aF.cols)), { Q: "matrix", R: "matrix", P: "matrix" });
    },
    /** DoubleRealQZDecomposer.decompose */
    realQzDecompose(a, b) {
      const aF = flatten(a);
      const bF = flatten(b);
      return named(unpack(ex.realQzDecompose(aF.values, aF.rows, aF.cols, bF.values, bF.rows, bF.cols)), { Q: "matrix", Z: "matrix", AA: "matrix", BB: "matrix", ReX: "matrix", ImX: "matrix" });
    },
    /** DoubleRealSchurDecomposer.decompose */
    realSchurDecompose(a) {
      const aF = flatten(a);
      return named(unpack(ex.realSchurDecompose(aF.values, aF.rows, aF.cols)), { U: "matrix", T: "matrix" });
    },

    // ---- org.mklab.nfc.elf ----
    /** DoubleComplexExponentialMatrix.exp */
    complexExp(aRe, aIm) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return unpack(ex.complexExp(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols));
    },
    /** DoubleComplexExponentialMatrix.exp */
    complexExpWithTolerance(aRe, aIm, tolerance) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return unpack(ex.complexExpWithTolerance(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols, tolerance));
    },
    /** DoubleRealExponentialMatrix.getMaxIteration */
    realGetMaxIteration() {
      return ex.realGetMaxIteration();
    },
    /** DoubleRealExponentialMatrix.setMaxIteration */
    realSetMaxIteration(maxIteration) {
      ex.realSetMaxIteration(maxIteration);
    },
    /** DoubleRealExponentialMatrix.exp */
    realExp(a) {
      const aF = flatten(a);
      return unpack(ex.realExp(aF.values, aF.rows, aF.cols))[0];
    },
    /** DoubleRealExponentialMatrix.exp */
    realExpWithTolerance(a, tolerance) {
      const aF = flatten(a);
      return unpack(ex.realExpWithTolerance(aF.values, aF.rows, aF.cols, tolerance))[0];
    },

    // ---- org.mklab.nfc.fft ----
    /** DoubleComplexFFTAnalyzer.fft */
    complexFft(reData, imData, size) {
      return unpack(ex.complexFft(reData, imData, size))[0];
    },
    /** DoubleComplexFFTAnalyzer.ifft */
    complexFftIfft(reData, imData, size) {
      return unpack(ex.complexFftIfft(reData, imData, size))[0];
    },
    /** DoubleRealFFTAnalyzer.fft */
    realFft(data, size) {
      return unpack(ex.realFft(data, size))[0];
    },
    /** DoubleRealFFTAnalyzer.ifft */
    realFftIfft(data, size) {
      return unpack(ex.realFftIfft(data, size))[0];
    },

    // ---- org.mklab.nfc.leq ----
    /** DoubleComplexLUDecomposer.decompose */
    complexLuDecompose(aRe, aIm, tolerance, stopIfSingular) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return named(unpack(ex.complexLuDecompose(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols, tolerance, stopIfSingular)), { RealL: "matrix", ImaginaryL: "matrix", RealU: "matrix", ImaginaryU: "matrix", P: "matrix" });
    },
    /** DoubleComplexLUDecomposer.decomposeWithPermutation */
    complexLuDecomposeWithPermutation(aRe, aIm, tolerance, stopIfSingular) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return named(unpack(ex.complexLuDecomposeWithPermutation(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols, tolerance, stopIfSingular)), { RealL: "matrix", ImaginaryL: "matrix", RealU: "matrix", ImaginaryU: "matrix", P: "matrix" });
    },
    /** DoubleComplexLUDecomposer.inverse */
    complexLuInverse(aRe, aIm, tolerance, stopIfSingular) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return unpack(ex.complexLuInverse(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols, tolerance, stopIfSingular));
    },
    /** DoubleComplexLUDecomposer.leftDivide */
    complexLuLeftDivide(aRe, aIm, bRe, bIm, tolerance, stopIfSingular) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      const bReF = flatten(bRe);
      const bImF = flatten(bIm);
      return unpack(ex.complexLuLeftDivide(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols, bReF.values, bReF.rows, bReF.cols, bImF.values, bImF.rows, bImF.cols, tolerance, stopIfSingular));
    },
    /** DoubleComplexLUDecomposer.getDeterminant */
    complexLuGetDeterminant(aRe, aIm, tolerance, stopIfSingular) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return unpack(ex.complexLuGetDeterminant(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols, tolerance, stopIfSingular))[0][0];
    },
    /** DoubleRealCholeskyDecomposer.decompose */
    realCholeskyDecompose(A, tolerance) {
      const AF = flatten(A);
      return unpack(ex.realCholeskyDecompose(AF.values, AF.rows, AF.cols, tolerance))[0];
    },
    /** DoubleRealLUDecomposer.decompose */
    realLuDecompose(matrix, tolerance, stopIfSingular) {
      const matrixF = flatten(matrix);
      return named(unpack(ex.realLuDecompose(matrixF.values, matrixF.rows, matrixF.cols, tolerance, stopIfSingular)), { L: "matrix", U: "matrix", P: "matrix" });
    },
    /** DoubleRealLUDecomposer.decomposeWithPermutation */
    realLuDecomposeWithPermutation(matrix, tolerance, stopIfSingular) {
      const matrixF = flatten(matrix);
      return named(unpack(ex.realLuDecomposeWithPermutation(matrixF.values, matrixF.rows, matrixF.cols, tolerance, stopIfSingular)), { L: "matrix", U: "matrix", P: "matrix" });
    },
    /** DoubleRealLUDecomposer.inverse */
    realLuInverse(a, tolerance, stopIfSingular) {
      const aF = flatten(a);
      return unpack(ex.realLuInverse(aF.values, aF.rows, aF.cols, tolerance, stopIfSingular))[0];
    },
    /** DoubleRealLUDecomposer.leftDivide */
    realLuLeftDivide(a, b, tolerance, stopIfSingular) {
      const aF = flatten(a);
      const bF = flatten(b);
      return unpack(ex.realLuLeftDivide(aF.values, aF.rows, aF.cols, bF.values, bF.rows, bF.cols, tolerance, stopIfSingular))[0];
    },
    /** DoubleRealLUDecomposer.getDeterminant */
    realLuGetDeterminant(matrix, tolerance, stopIfSingular) {
      const matrixF = flatten(matrix);
      return ex.realLuGetDeterminant(matrixF.values, matrixF.rows, matrixF.cols, tolerance, stopIfSingular);
    },

    // ---- org.mklab.nfc.svd ----
    /** DoubleComplexSingularValueDecomposer.decompose */
    complexSvdDecompose(aRe, aIm) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return named(unpack(ex.complexSvdDecompose(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols)), { ReU: "matrix", ImU: "matrix", D: "matrix", ReV: "matrix", ImV: "matrix" });
    },
    /** DoubleComplexSingularValueDecomposer.singularValue */
    complexSvdSingularValue(aRe, aIm) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return unpack(ex.complexSvdSingularValue(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols))[0][0];
    },
    /** DoubleComplexSingularValueDecomposer.maximumSingularValue */
    complexSvdMaximumSingularValue(aRe, aIm) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return ex.complexSvdMaximumSingularValue(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols);
    },
    /** DoubleComplexSingularValueDecomposer.minimumSingularValue */
    complexSvdMinimumSingularValue(aRe, aIm) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return ex.complexSvdMinimumSingularValue(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols);
    },
    /** DoubleComplexSingularValueDecomposer.isSingular */
    complexSvdIsSingular(aRe, aIm, tolerance) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return ex.complexSvdIsSingular(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols, tolerance);
    },
    /** DoubleComplexSingularValueDecomposer.rank */
    complexSvdRank(aRe, aIm, tolerance) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return ex.complexSvdRank(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols, tolerance);
    },
    /** DoubleComplexSingularValueDecomposer.isFullRank */
    complexSvdIsFullRank(aRe, aIm, tolerance) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return ex.complexSvdIsFullRank(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols, tolerance);
    },
    /** DoubleComplexSingularValueDecomposer.pseudoInverse */
    complexSvdPseudoInverse(aRe, aIm, tolerance) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return unpack(ex.complexSvdPseudoInverse(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols, tolerance));
    },
    /** DoubleComplexSingularValueDecomposer.leastSquare */
    complexSvdLeastSquare(aRe, aIm, bRe, bIm, toleance) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      const bReF = flatten(bRe);
      const bImF = flatten(bIm);
      return unpack(ex.complexSvdLeastSquare(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols, bReF.values, bReF.rows, bReF.cols, bImF.values, bImF.rows, bImF.cols, toleance));
    },
    /** DoubleComplexSingularValueDecomposer.kernel */
    complexSvdKernel(aRe, aIm, tolerance) {
      const aReF = flatten(aRe);
      const aImF = flatten(aIm);
      return unpack(ex.complexSvdKernel(aReF.values, aReF.rows, aReF.cols, aImF.values, aImF.rows, aImF.cols, tolerance));
    },
    /** DoubleComplexSingularValueDecomposer.norm */
    complexSvdNorm(ar, ai) {
      const arF = flatten(ar);
      const aiF = flatten(ai);
      return ex.complexSvdNorm(arF.values, arF.rows, arF.cols, aiF.values, aiF.rows, aiF.cols);
    },
    /** DoubleComplexSingularValueDecomposer.conditionNumber */
    complexSvdConditionNumber(ar, ai) {
      const arF = flatten(ar);
      const aiF = flatten(ai);
      return ex.complexSvdConditionNumber(arF.values, arF.rows, arF.cols, aiF.values, aiF.rows, aiF.cols);
    },
    /** DoubleRealSingularValueDecomposer.decompose */
    realSvdDecompose(a) {
      const aF = flatten(a);
      return named(unpack(ex.realSvdDecompose(aF.values, aF.rows, aF.cols)), { U: "matrix", D: "matrix", V: "matrix" });
    },
    /** DoubleRealSingularValueDecomposer.singularValue */
    realSvdSingularValue(a) {
      const aF = flatten(a);
      return unpack(ex.realSvdSingularValue(aF.values, aF.rows, aF.cols))[0][0];
    },
    /** DoubleRealSingularValueDecomposer.maximumSingularValue */
    realSvdMaximumSingularValue(a) {
      const aF = flatten(a);
      return ex.realSvdMaximumSingularValue(aF.values, aF.rows, aF.cols);
    },
    /** DoubleRealSingularValueDecomposer.minimumSingularValue */
    realSvdMinimumSingularValue(a) {
      const aF = flatten(a);
      return ex.realSvdMinimumSingularValue(aF.values, aF.rows, aF.cols);
    },
    /** DoubleRealSingularValueDecomposer.isSingular */
    realSvdIsSingular(a, tolerance) {
      const aF = flatten(a);
      return ex.realSvdIsSingular(aF.values, aF.rows, aF.cols, tolerance);
    },
    /** DoubleRealSingularValueDecomposer.rank */
    realSvdRank(a, tolerance) {
      const aF = flatten(a);
      return ex.realSvdRank(aF.values, aF.rows, aF.cols, tolerance);
    },
    /** DoubleRealSingularValueDecomposer.isFullRank */
    realSvdIsFullRank(a, tolerance) {
      const aF = flatten(a);
      return ex.realSvdIsFullRank(aF.values, aF.rows, aF.cols, tolerance);
    },
    /** DoubleRealSingularValueDecomposer.pseudoInverse */
    realSvdPseudoInverse(a, tolerance) {
      const aF = flatten(a);
      return unpack(ex.realSvdPseudoInverse(aF.values, aF.rows, aF.cols, tolerance))[0];
    },
    /** DoubleRealSingularValueDecomposer.leastSquare */
    realSvdLeastSquare(a, b, tolerance) {
      const aF = flatten(a);
      const bF = flatten(b);
      return unpack(ex.realSvdLeastSquare(aF.values, aF.rows, aF.cols, bF.values, bF.rows, bF.cols, tolerance))[0];
    },
    /** DoubleRealSingularValueDecomposer.kernel */
    realSvdKernel(a, tolerance) {
      const aF = flatten(a);
      return unpack(ex.realSvdKernel(aF.values, aF.rows, aF.cols, tolerance))[0];
    },
    /** DoubleRealSingularValueDecomposer.norm */
    realSvdNorm(a) {
      const aF = flatten(a);
      return ex.realSvdNorm(aF.values, aF.rows, aF.cols);
    },
    /** DoubleRealSingularValueDecomposer.conditionNumber */
    realSvdConditionNumber(a) {
      const aF = flatten(a);
      return ex.realSvdConditionNumber(aF.values, aF.rows, aF.cols);
    },
  };
}
