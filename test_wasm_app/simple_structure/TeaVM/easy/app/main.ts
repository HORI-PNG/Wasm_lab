declare const TeaVM: any;

async function run() {
  const teavm = await TeaVM.wasmGC.load("example.wasm");
  teavm.exports.main([]);
}

run();
