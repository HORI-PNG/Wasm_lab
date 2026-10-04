(() => {
  interface EigenWasmExports {
    solveEigen2x2(a: number, b: number, c: number, d: number): number[];
  }

  interface TeaVMApi {
    wasmGC: {
      load(path: string): Promise<{ exports: EigenWasmExports }>;
    };
  }

  const teaVM = (window as Window & { TeaVM?: TeaVMApi }).TeaVM;
  if (!teaVM) {
    throw new Error("TeaVMのランタイムが読み込まれていません。");
  }
  const resultEl = document.getElementById("result") as HTMLElement;
  let wasmExports: EigenWasmExports | undefined;

  teaVM.wasmGC
    .load("eig_example.wasm")
    .then(({ exports }) => {
      wasmExports = exports;
    })
    .catch((error: unknown) => {
      console.error("WASMの読み込みに失敗しました:", error);
      resultEl.textContent = "WASMの読み込みに失敗しました。";
    });

  document.getElementById("calc-btn")?.addEventListener("click", () => {
    if (!wasmExports) {
      resultEl.textContent = "WASMを読み込み中です。";
      return;
    }

    const a = parseFloat(
      (document.getElementById("m00") as HTMLInputElement).value,
    );
    const b = parseFloat(
      (document.getElementById("m01") as HTMLInputElement).value,
    );
    const c = parseFloat(
      (document.getElementById("m10") as HTMLInputElement).value,
    );
    const d = parseFloat(
      (document.getElementById("m11") as HTMLInputElement).value,
    );

    const result = wasmExports.solveEigen2x2(a, b, c, d);
    resultEl.textContent = `計算結果: [${result.join(", ")}]`;
  });
})();
