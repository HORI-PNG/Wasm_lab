// NFC (TeaVM WasmGC) を2次元配列で扱うための薄いラッパー。
// Java 側 org.example.Matrices と同じ形式で行列を詰めたり戻したりする。

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

const complex = ([re, im]) => ({ re: re[0], im: im[0] });

async function loadNfc(wasmUrl = "wasm-gc/nfc.wasm", tolerance = 1e-12) {
  const teavm = await TeaVM.wasmGC.load(wasmUrl);
  const ex = teavm.exports;
  const square = (a) => {
    const f = flatten(a);
    if (f.rows !== f.cols) throw new Error("正方行列を指定してください");
    return f;
  };

  return {
    exports: ex,

    // ---- eig ----
    eigenValues(a) {
      const f = square(a);
      return complex(unpack(ex.eigenValues(f.values, f.rows)));
    },
    eigen(a) {
      const f = square(a);
      const [vr, vi, xr, xi] = unpack(ex.eigen(f.values, f.rows));
      return { values: { re: vr[0], im: vi[0] }, vectors: { re: xr, im: xi } };
    },
    generalizedEigen(a, b) {
      const fa = square(a), fb = square(b);
      const [vr, vi, xr, xi] = unpack(ex.generalizedEigen(fa.values, fb.values, fa.rows));
      return { values: { re: vr[0], im: vi[0] }, vectors: { re: xr, im: xi } };
    },
    hessenberg(a) {
      const f = square(a);
      const [Q, H] = unpack(ex.hessenberg(f.values, f.rows));
      return { Q, H };
    },
    schur(a) {
      const f = square(a);
      const [U, T] = unpack(ex.schur(f.values, f.rows));
      return { U, T };
    },
    qr(a) {
      const f = flatten(a);
      const [Q, R] = unpack(ex.qr(f.values, f.rows, f.cols));
      return { Q, R };
    },
    qz(a, b) {
      const fa = square(a), fb = square(b);
      const [AA, BB, Q, Z, Xre, Xim] = unpack(ex.qz(fa.values, fb.values, fa.rows));
      return { AA, BB, Q, Z, X: { re: Xre, im: Xim } };
    },

    // ---- leq ----
    lu(a) {
      const f = square(a);
      const [L, U] = unpack(ex.lu(f.values, f.rows, tolerance));
      return { L, U };
    },
    inverse(a) {
      const f = square(a);
      return unpack(ex.inverse(f.values, f.rows, tolerance))[0];
    },
    solve(a, b) {
      const fa = square(a), fb = flatten(b);
      return unpack(ex.solve(fa.values, fa.rows, fb.values, fb.cols, tolerance))[0];
    },
    determinant(a) {
      const f = square(a);
      return ex.determinant(f.values, f.rows, tolerance);
    },

    // ---- svd ----
    svd(a) {
      const f = flatten(a);
      const [U, D, V] = unpack(ex.svd(f.values, f.rows, f.cols));
      return { U, D, V };
    },
    singularValues(a) {
      const f = flatten(a);
      return unpack(ex.singularValues(f.values, f.rows, f.cols))[0][0];
    },
    rank(a) {
      const f = flatten(a);
      return ex.rank(f.values, f.rows, f.cols, tolerance);
    },
    norm(a) {
      const f = flatten(a);
      return ex.norm(f.values, f.rows, f.cols);
    },
    conditionNumber(a) {
      const f = flatten(a);
      return ex.conditionNumber(f.values, f.rows, f.cols);
    },
    pseudoInverse(a) {
      const f = flatten(a);
      return unpack(ex.pseudoInverse(f.values, f.rows, f.cols, tolerance))[0];
    },
    leastSquare(a, b) {
      const fa = flatten(a), fb = flatten(b);
      return unpack(ex.leastSquare(fa.values, fa.rows, fa.cols, fb.values, fb.cols, tolerance))[0];
    },
    kernel(a) {
      const f = flatten(a);
      return unpack(ex.kernel(f.values, f.rows, f.cols, tolerance))[0];
    },

    // ---- fft ----
    fft(data, size = data.length) {
      return complex(unpack(ex.fft(data, size)));
    },
    ifft(data, size = data.length) {
      return complex(unpack(ex.ifft(data, size)));
    },
    complexFft(re, im, size = re.length) {
      return complex(unpack(ex.complexFft(re, im, size)));
    },
    complexIfft(re, im, size = re.length) {
      return complex(unpack(ex.complexIfft(re, im, size)));
    },

    // ---- elf ----
    expm(a) {
      const f = square(a);
      return unpack(ex.expm(f.values, f.rows))[0];
    },
  };
}
