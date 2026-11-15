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

    // ---- org.mklab.nfc.matrix ----
    /** DoubleMatrixUtil.equals */
    matrixEquals(a1, a2) {
      const a1F = flatten(a1);
      const a2F = flatten(a2);
      return ex.matrixEquals(a1F.values, a1F.rows, a1F.cols, a2F.values, a2F.rows, a2F.cols);
    },
    /** DoubleMatrixUtil.equals */
    matrixEqualsWithTolerance(a1, a2, tolerance) {
      const a1F = flatten(a1);
      const a2F = flatten(a2);
      return ex.matrixEqualsWithTolerance(a1F.values, a1F.rows, a1F.cols, a2F.values, a2F.rows, a2F.cols, tolerance);
    },
    /** DoubleMatrixUtil.getSubMatrix */
    matrixGetSubMatrixWithRowMinRowMaxColumnMinColumnMax(matrix, rowMin, rowMax, columnMin, columnMax) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixGetSubMatrixWithRowMinRowMaxColumnMinColumnMax(matrixF.values, matrixF.rows, matrixF.cols, rowMin, rowMax, columnMin, columnMax))[0];
    },
    /** DoubleMatrixUtil.getSubMatrix */
    matrixGetSubMatrixWithColumnMinColumnMax(matrix, rowIndex, columnMin, columnMax) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixGetSubMatrixWithColumnMinColumnMax(matrixF.values, matrixF.rows, matrixF.cols, rowIndex, columnMin, columnMax))[0];
    },
    /** DoubleMatrixUtil.getSubMatrix */
    matrixGetSubMatrixWithRowMinRowMax(matrix, rowMin, rowMax, columnIndex) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixGetSubMatrixWithRowMinRowMax(matrixF.values, matrixF.rows, matrixF.cols, rowMin, rowMax, columnIndex))[0];
    },
    /** DoubleMatrixUtil.getSubMatrix */
    matrixGetSubMatrix(matrix, rowIndex, columnIndex) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixGetSubMatrix(matrixF.values, matrixF.rows, matrixF.cols, rowIndex, columnIndex))[0];
    },
    /** DoubleMatrixUtil.getSubVector */
    matrixGetSubVector(vector, index) {
      return unpack(ex.matrixGetSubVector(vector, index))[0][0];
    },
    /** DoubleMatrixUtil.appendDown */
    matrixAppendDown(a1, a2) {
      const a1F = flatten(a1);
      const a2F = flatten(a2);
      return unpack(ex.matrixAppendDown(a1F.values, a1F.rows, a1F.cols, a2F.values, a2F.rows, a2F.cols))[0];
    },
    /** DoubleMatrixUtil.appendRight */
    matrixAppendRight(a1, a2) {
      const a1F = flatten(a1);
      const a2F = flatten(a2);
      return unpack(ex.matrixAppendRight(a1F.values, a1F.rows, a1F.cols, a2F.values, a2F.rows, a2F.cols))[0];
    },
    /** DoubleMatrixUtil.anyZero */
    matrixAnyZero(vector) {
      return ex.matrixAnyZero(vector);
    },
    /** DoubleMatrixUtil.clone */
    matrixClone(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixClone(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.isZero */
    matrixIsZero(matrix, tolerance) {
      const matrixF = flatten(matrix);
      return ex.matrixIsZero(matrixF.values, matrixF.rows, matrixF.cols, tolerance);
    },
    /** DoubleMatrixUtil.isUnit */
    matrixIsUnit(matrix, tolerance) {
      const matrixF = flatten(matrix);
      return ex.matrixIsUnit(matrixF.values, matrixF.rows, matrixF.cols, tolerance);
    },
    /** DoubleMatrixUtil.transpose */
    matrixTranspose(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixTranspose(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.transpose */
    matrixTransposeWithVector(matrix) {
      return unpack(ex.matrixTransposeWithVector(matrix))[0];
    },
    /** DoubleMatrixUtil.inverseElementWise */
    matrixInverseElementWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixInverseElementWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.multiply */
    matrixMultiply(a1, a2) {
      const a1F = flatten(a1);
      const a2F = flatten(a2);
      return unpack(ex.matrixMultiply(a1F.values, a1F.rows, a1F.cols, a2F.values, a2F.rows, a2F.cols))[0];
    },
    /** DoubleMatrixUtil.createUnit */
    matrixCreateUnit(rowSize, columnSize) {
      return unpack(ex.matrixCreateUnit(rowSize, columnSize))[0];
    },
    /** DoubleMatrixUtil.createOnes */
    matrixCreateOnes(rowSize, columnSize) {
      return unpack(ex.matrixCreateOnes(rowSize, columnSize))[0];
    },
    /** DoubleMatrixUtil.createZero */
    matrixCreateZeroWithRowSizeColumnSize(rowSize, columnSize) {
      return unpack(ex.matrixCreateZeroWithRowSizeColumnSize(rowSize, columnSize))[0];
    },
    /** DoubleMatrixUtil.createZero */
    matrixCreateZero(size) {
      return unpack(ex.matrixCreateZero(size))[0][0];
    },
    /** DoubleMatrixUtil.series */
    matrixSeries(from, to, by) {
      return unpack(ex.matrixSeries(from, to, by))[0][0];
    },
    /** DoubleMatrixUtil.multiply */
    matrixMultiplyWithMatrixInt(matrix, scalar) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixMultiplyWithMatrixInt(matrixF.values, matrixF.rows, matrixF.cols, scalar))[0];
    },
    /** DoubleMatrixUtil.multiply */
    matrixMultiplyWithMatrixDouble(matrix, scalar) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixMultiplyWithMatrixDouble(matrixF.values, matrixF.rows, matrixF.cols, scalar))[0];
    },
    /** DoubleMatrixUtil.unaryMinus */
    matrixUnaryMinus(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixUnaryMinus(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.vectorToDiagonal */
    matrixVectorToDiagonal(vector) {
      return unpack(ex.matrixVectorToDiagonal(vector))[0];
    },
    /** DoubleMatrixUtil.removeRowVectors */
    matrixRemoveRowVectorsWithRowMinRowMax(matrix, rowMin, rowMax) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixRemoveRowVectorsWithRowMinRowMax(matrixF.values, matrixF.rows, matrixF.cols, rowMin, rowMax))[0];
    },
    /** DoubleMatrixUtil.removeRowVectors */
    matrixRemoveRowVectors(matrix, rowIndex) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixRemoveRowVectors(matrixF.values, matrixF.rows, matrixF.cols, rowIndex))[0];
    },
    /** DoubleMatrixUtil.removeColumnVectors */
    matrixRemoveColumnVectorsWithColumnMinColumnMax(matrix, columnMin, columnMax) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixRemoveColumnVectorsWithColumnMinColumnMax(matrixF.values, matrixF.rows, matrixF.cols, columnMin, columnMax))[0];
    },
    /** DoubleMatrixUtil.removeColumnVectors */
    matrixRemoveColumnVectors(matrix, columnIndex) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixRemoveColumnVectors(matrixF.values, matrixF.rows, matrixF.cols, columnIndex))[0];
    },
    /** DoubleMatrixUtil.reshape */
    matrixReshape(matrix, newRowSize, newColumnSize) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixReshape(matrixF.values, matrixF.rows, matrixF.cols, newRowSize, newColumnSize))[0];
    },
    /** DoubleMatrixUtil.multiplyElementWise */
    matrixMultiplyElementWise(a1, a2) {
      const a1F = flatten(a1);
      const a2F = flatten(a2);
      return unpack(ex.matrixMultiplyElementWise(a1F.values, a1F.rows, a1F.cols, a2F.values, a2F.rows, a2F.cols))[0];
    },
    /** DoubleMatrixUtil.maxElementWise */
    matrixMaxElementWise(a1, a2) {
      const a1F = flatten(a1);
      const a2F = flatten(a2);
      return unpack(ex.matrixMaxElementWise(a1F.values, a1F.rows, a1F.cols, a2F.values, a2F.rows, a2F.cols))[0];
    },
    /** DoubleMatrixUtil.minElementWise */
    matrixMinElementWise(a1, a2) {
      const a1F = flatten(a1);
      const a2F = flatten(a2);
      return unpack(ex.matrixMinElementWise(a1F.values, a1F.rows, a1F.cols, a2F.values, a2F.rows, a2F.cols))[0];
    },
    /** DoubleMatrixUtil.divide */
    matrixDivideWithMatrixInt(matrix, scalar) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixDivideWithMatrixInt(matrixF.values, matrixF.rows, matrixF.cols, scalar))[0];
    },
    /** DoubleMatrixUtil.divide */
    matrixDivide(matrix, scalar) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixDivide(matrixF.values, matrixF.rows, matrixF.cols, scalar))[0];
    },
    /** DoubleMatrixUtil.divideElementWise */
    matrixDivideElementWise(a1, a2) {
      const a1F = flatten(a1);
      const a2F = flatten(a2);
      return unpack(ex.matrixDivideElementWise(a1F.values, a1F.rows, a1F.cols, a2F.values, a2F.rows, a2F.cols))[0];
    },
    /** DoubleMatrixUtil.leftDivideElementWise */
    matrixLeftDivideElementWise(a1, a2) {
      const a1F = flatten(a1);
      const a2F = flatten(a2);
      return unpack(ex.matrixLeftDivideElementWise(a1F.values, a1F.rows, a1F.cols, a2F.values, a2F.rows, a2F.cols))[0];
    },
    /** DoubleMatrixUtil.addElementWise */
    matrixAddElementWise(matrix, scalar) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixAddElementWise(matrixF.values, matrixF.rows, matrixF.cols, scalar))[0];
    },
    /** DoubleMatrixUtil.addElementWise */
    matrixAddElementWiseWithIntMatrixDouble(matrix, scalar) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixAddElementWiseWithIntMatrixDouble(matrixF.values, matrixF.rows, matrixF.cols, scalar))[0];
    },
    /** DoubleMatrixUtil.subtractElementWise */
    matrixSubtractElementWise(matrix, scalar) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixSubtractElementWise(matrixF.values, matrixF.rows, matrixF.cols, scalar))[0];
    },
    /** DoubleMatrixUtil.subtractElementWise */
    matrixSubtractElementWiseWithIntMatrixDouble(matrix, scalar) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixSubtractElementWiseWithIntMatrixDouble(matrixF.values, matrixF.rows, matrixF.cols, scalar))[0];
    },
    /** DoubleMatrixUtil.powerElementWise */
    matrixPowerElementWise(scalar, matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixPowerElementWise(scalar, matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.powerElementWise */
    matrixPowerElementWiseWithMatrixDouble(matrix, scalar) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixPowerElementWiseWithMatrixDouble(matrixF.values, matrixF.rows, matrixF.cols, scalar))[0];
    },
    /** DoubleMatrixUtil.powerElementWise */
    matrixPowerElementWiseWithMatrixMatrix(a1, a2) {
      const a1F = flatten(a1);
      const a2F = flatten(a2);
      return unpack(ex.matrixPowerElementWiseWithMatrixMatrix(a1F.values, a1F.rows, a1F.cols, a2F.values, a2F.rows, a2F.cols))[0];
    },
    /** DoubleMatrixUtil.powerElementWise */
    matrixPowerElementWiseWithMatrixIntMatrix(a1, a2) {
      const a1F = flatten(a1);
      const a2F = flatten(a2);
      return unpack(ex.matrixPowerElementWiseWithMatrixIntMatrix(a1F.values, a1F.rows, a1F.cols, a2F.values, a2F.rows, a2F.cols))[0];
    },
    /** DoubleMatrixUtil.add */
    matrixAdd(a1, a2) {
      const a1F = flatten(a1);
      const a2F = flatten(a2);
      return unpack(ex.matrixAdd(a1F.values, a1F.rows, a1F.cols, a2F.values, a2F.rows, a2F.cols))[0];
    },
    /** DoubleMatrixUtil.subtract */
    matrixSubtract(a1, a2) {
      const a1F = flatten(a1);
      const a2F = flatten(a2);
      return unpack(ex.matrixSubtract(a1F.values, a1F.rows, a1F.cols, a2F.values, a2F.rows, a2F.cols))[0];
    },
    /** DoubleMatrixUtil.max */
    matrixMax(matrix) {
      const matrixF = flatten(matrix);
      return ex.matrixMax(matrixF.values, matrixF.rows, matrixF.cols);
    },
    /** DoubleMatrixUtil.max */
    matrixMaxWithVector(vector) {
      return ex.matrixMaxWithVector(vector);
    },
    /** DoubleMatrixUtil.min */
    matrixMin(matrix) {
      const matrixF = flatten(matrix);
      return ex.matrixMin(matrixF.values, matrixF.rows, matrixF.cols);
    },
    /** DoubleMatrixUtil.min */
    matrixMinWithVector(vector) {
      return ex.matrixMinWithVector(vector);
    },
    /** DoubleMatrixUtil.sum */
    matrixSum(matrix) {
      const matrixF = flatten(matrix);
      return ex.matrixSum(matrixF.values, matrixF.rows, matrixF.cols);
    },
    /** DoubleMatrixUtil.product */
    matrixProduct(matrix) {
      const matrixF = flatten(matrix);
      return ex.matrixProduct(matrixF.values, matrixF.rows, matrixF.cols);
    },
    /** DoubleMatrixUtil.mean */
    matrixMean(matrix) {
      const matrixF = flatten(matrix);
      return ex.matrixMean(matrixF.values, matrixF.rows, matrixF.cols);
    },
    /** DoubleMatrixUtil.std */
    matrixStd(matrix) {
      const matrixF = flatten(matrix);
      return ex.matrixStd(matrixF.values, matrixF.rows, matrixF.cols);
    },
    /** DoubleMatrixUtil.frobNorm */
    matrixFrobNorm(matrix) {
      const matrixF = flatten(matrix);
      return ex.matrixFrobNorm(matrixF.values, matrixF.rows, matrixF.cols);
    },
    /** DoubleMatrixUtil.frobNormRowWise */
    matrixFrobNormRowWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixFrobNormRowWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.frobNormColumnWise */
    matrixFrobNormColumnWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixFrobNormColumnWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.infNorm */
    matrixInfNorm(matrix) {
      const matrixF = flatten(matrix);
      return ex.matrixInfNorm(matrixF.values, matrixF.rows, matrixF.cols);
    },
    /** DoubleMatrixUtil.trace */
    matrixTrace(matrix) {
      const matrixF = flatten(matrix);
      return ex.matrixTrace(matrixF.values, matrixF.rows, matrixF.cols);
    },
    /** DoubleMatrixUtil.diagonalToVector */
    matrixDiagonalToVector(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixDiagonalToVector(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.productRowWise */
    matrixProductRowWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixProductRowWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.productColumnWise */
    matrixProductColumnWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixProductColumnWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.sumRowWise */
    matrixSumRowWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixSumRowWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.sumColumnWise */
    matrixSumColumnWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixSumColumnWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.meanColumnWise */
    matrixMeanColumnWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixMeanColumnWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.meanRowWise */
    matrixMeanRowWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixMeanRowWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.cumulativeProductColumnWise */
    matrixCumulativeProductColumnWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixCumulativeProductColumnWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.cumulativeProductRowWise */
    matrixCumulativeProductRowWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixCumulativeProductRowWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.cumulativeSumColumnWise */
    matrixCumulativeSumColumnWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixCumulativeSumColumnWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.cumulativeSumRowWise */
    matrixCumulativeSumRowWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixCumulativeSumRowWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.minColumnWise */
    matrixMinColumnWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixMinColumnWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.minRowWise */
    matrixMinRowWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixMinRowWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.maxColumnWise */
    matrixMaxColumnWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixMaxColumnWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.maxRowWise */
    matrixMaxRowWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixMaxRowWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.stdColumnWise */
    matrixStdColumnWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixStdColumnWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.stdRowWise */
    matrixStdRowWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixStdRowWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.cumulativeProduct */
    matrixCumulativeProduct(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixCumulativeProduct(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.cumulativeSum */
    matrixCumulativeSum(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixCumulativeSum(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.indexOfMinimum */
    matrixIndexOfMinimum(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixIndexOfMinimum(matrixF.values, matrixF.rows, matrixF.cols))[0][0];
    },
    /** DoubleMatrixUtil.indexOfMaximum */
    matrixIndexOfMaximum(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixIndexOfMaximum(matrixF.values, matrixF.rows, matrixF.cols))[0][0];
    },
    /** DoubleMatrixUtil.resize */
    matrixResize(matrix, newRowSize, newColumnSize) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixResize(matrixF.values, matrixF.rows, matrixF.cols, newRowSize, newColumnSize))[0];
    },
    /** DoubleMatrixUtil.powerElementWise */
    matrixPowerElementWiseWithIntMatrixDouble(matrix, d) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixPowerElementWiseWithIntMatrixDouble(matrixF.values, matrixF.rows, matrixF.cols, d))[0];
    },
    /** DoubleMatrixUtil.powerElementWise */
    matrixPowerElementWiseWithIntMatrixMatrix(a1, a2) {
      const a1F = flatten(a1);
      const a2F = flatten(a2);
      return unpack(ex.matrixPowerElementWiseWithIntMatrixMatrix(a1F.values, a1F.rows, a1F.cols, a2F.values, a2F.rows, a2F.cols))[0];
    },
    /** DoubleMatrixUtil.powerElementWise */
    matrixPowerElementWiseWithDoubleIntMatrix(scalar, matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixPowerElementWiseWithDoubleIntMatrix(scalar, matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.createArray */
    matrixCreateArray(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixCreateArray(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.median */
    matrixMedian(matrix) {
      const matrixF = flatten(matrix);
      return ex.matrixMedian(matrixF.values, matrixF.rows, matrixF.cols);
    },
    /** DoubleMatrixUtil.medianColumnWise */
    matrixMedianColumnWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixMedianColumnWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.medianRowWise */
    matrixMedianRowWise(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixMedianRowWise(matrixF.values, matrixF.rows, matrixF.cols))[0];
    },
    /** DoubleMatrixUtil.covariance */
    matrixCovariance(a1, a2) {
      const a1F = flatten(a1);
      const a2F = flatten(a2);
      return unpack(ex.matrixCovariance(a1F.values, a1F.rows, a1F.cols, a2F.values, a2F.rows, a2F.cols))[0];
    },
    /** DoubleMatrixUtil.variance */
    matrixVariance(matrix) {
      const matrixF = flatten(matrix);
      return ex.matrixVariance(matrixF.values, matrixF.rows, matrixF.cols);
    },
    /** DoubleMatrixUtil.makeColumnVector */
    matrixMakeColumnVector(matrix) {
      const matrixF = flatten(matrix);
      return unpack(ex.matrixMakeColumnVector(matrixF.values, matrixF.rows, matrixF.cols))[0];
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
