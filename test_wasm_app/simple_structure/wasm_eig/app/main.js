"use strict";

const resultEl = document.getElementById("result");
let wasmExports;

TeaVM.wasmGC
  .load("eig_example.wasm")
  .then(({ exports }) => {
    wasmExports = exports;
  })
  .catch((error) => {
    console.error("WASMの読み込みに失敗しました:", error);
    resultEl.textContent = "WASMの読み込みに失敗しました。";
  });

document.getElementById("calc-btn")?.addEventListener("click", () => {
  if (!wasmExports) {
    resultEl.textContent = "WASMを読み込み中です。";
    return;
  }

  const a = parseFloat(document.getElementById("m00").value);
  const b = parseFloat(document.getElementById("m01").value);
  const c = parseFloat(document.getElementById("m10").value);
  const d = parseFloat(document.getElementById("m11").value);

  const result = wasmExports.solveEigen2x2(a, b, c, d);
  resultEl.textContent = `計算結果: [${result.join(", ")}]`;
});

/*
 * 参考にするサイト
 * https://teavm.org/docs/wasm-gc-backend/loader.html
 *
 */
