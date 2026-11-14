// 自動生成版の nfc.wasm を Node で動かし、次の 2 つを確かめる（step01 で追加）。
//
//   1. JVM との一致：JvmReference.java が書き出した正解データ（jvm-reference.json）と同じ引数で Wasm の関数を呼び、
//      結果が JVM と一致するかを調べる。→ Wasm への変換で結果が変わっていないか
//   2. 数学的な性質：JS ラッパー（nfc.generated.js）経由で計算し、Q·R ≈ A などの式が成り立つかを調べる。
//      → ブリッジの変換（flatten / pack / unpack）と NFC の計算が正しいか
//
// 使い方（TeaVM フォルダで。verify-tools/run.sh がまとめて実行する）:
//   node verify-tools/wasm-verify.mjs <wasm のフォルダ> <jvm-reference.json> <nfc.generated.js> <レポートの出力先 .md>

import fs from "node:fs";
import path from "node:path";
import { createRequire } from "node:module";

const [wasmDir, referencePath, wrapperPath, reportPath] = process.argv.slice(2);
if (!reportPath) {
  console.error("使い方: node verify-tools/wasm-verify.mjs <wasm のフォルダ> <jvm-reference.json> <nfc.generated.js> <report.md>");
  process.exit(2);
}

/** 「ほぼ一致」とみなす相対誤差（JVM との比較） */
const CLOSE = 1e-12;
/** 数学的な性質のチェックで許す相対誤差 */
const PROPERTY_TOLERANCE = 1e-10;

// ---- Wasm の読み込み ----
// nfc.wasm-runtime.js はグローバルに TeaVM を作るスクリプト。Node では require で読み込める
const require = createRequire(import.meta.url);
require(path.resolve(wasmDir, "nfc.wasm-runtime.js"));
const wasmPath = path.resolve(wasmDir, "nfc.wasm");
const teavm = await globalThis.TeaVM.wasmGC.load(wasmPath, { nodejs: true });
const ex = teavm.exports;

// ---- 1. JVM との一致 ----

const reference = JSON.parse(fs.readFileSync(referencePath, "utf8"));

/** JSON では NaN・無限大を文字列で書いているので数値に戻す */
function decode(value) {
  if (Array.isArray(value)) return value.map(decode);
  if (value === "NaN") return NaN;
  if (value === "Infinity") return Infinity;
  if (value === "-Infinity") return -Infinity;
  return value;
}

function sameNumber(a, b) {
  return a === b || (Number.isNaN(a) && Number.isNaN(b));
}

function relativeDiff(a, b) {
  if (sameNumber(a, b)) return 0;
  return Math.abs(a - b) / Math.max(1, Math.abs(a), Math.abs(b));
}

/** JVM の結果と Wasm の結果を比べ、{ verdict, maxDiff, note } を返す。verdict は same / close / differ */
function compareResult(call, wasm) {
  if (call.outcome === "error" || wasm.outcome === "error") {
    if (call.outcome === "error" && wasm.outcome === "error") {
      return { verdict: "same", maxDiff: 0, note: "両方とも例外" };
    }
    return { verdict: "differ", maxDiff: Infinity, note: `JVM: ${call.outcome} / Wasm: ${wasm.outcome}` };
  }
  if (call.outcome === "void") {
    return { verdict: wasm.outcome === "void" ? "same" : "differ", maxDiff: 0, note: "" };
  }
  const expected = decode(call.result);
  const actual = wasm.value;
  if (Array.isArray(expected)) {
    if (!Array.isArray(actual) || actual.length !== expected.length) {
      return { verdict: "differ", maxDiff: Infinity, note: `長さが違う（JVM ${expected.length} / Wasm ${actual?.length}）` };
    }
    let maxDiff = 0;
    for (let i = 0; i < expected.length; i++) maxDiff = Math.max(maxDiff, relativeDiff(expected[i], actual[i]));
    return { verdict: maxDiff === 0 ? "same" : maxDiff <= CLOSE ? "close" : "differ", maxDiff, note: "" };
  }
  if (typeof expected === "boolean") {
    return { verdict: expected === actual ? "same" : "differ", maxDiff: 0, note: "" };
  }
  const diff = relativeDiff(expected, actual);
  return { verdict: diff === 0 ? "same" : diff <= CLOSE ? "close" : "differ", maxDiff: diff, note: "" };
}

const callResults = [];
for (const call of reference.calls) {
  const args = call.args.map(decode);
  let wasm;
  try {
    const value = ex[call.export](...args);
    if (call.returnType === "void") {
      wasm = { outcome: "void" };
    } else {
      wasm = { outcome: "value", value: value !== null && typeof value === "object" ? Array.from(value) : value };
    }
  } catch (e) {
    wasm = { outcome: "error", error: String(e?.message ?? e) };
  }
  callResults.push({ call, wasm, ...compareResult(call, wasm) });
}

// ---- 2. 数学的な性質 ----

// nfc.generated.js は loadNfcGenerated を定義するだけのスクリプト。関数として取り出して使う
const wrapperCode = fs.readFileSync(wrapperPath, "utf8");
const loadNfcGenerated = new Function(`${wrapperCode}\nreturn loadNfcGenerated;`)();
const nfc = await loadNfcGenerated(wasmPath);

// 行列の小さな道具（実数）
const rows = (m) => m.length;
const cols = (m) => (m.length === 0 ? 0 : m[0].length);
const zeros = (r, c) => Array.from({ length: r }, () => new Array(c).fill(0));
const eye = (n) => zeros(n, n).map((row, i) => row.map((_, j) => (i === j ? 1 : 0)));
const T = (m) => zeros(cols(m), rows(m)).map((row, i) => row.map((_, j) => m[j][i]));
function mul(a, b) {
  const out = zeros(rows(a), cols(b));
  for (let i = 0; i < rows(a); i++)
    for (let k = 0; k < cols(a); k++)
      for (let j = 0; j < cols(b); j++) out[i][j] += a[i][k] * b[k][j];
  return out;
}
const add = (a, b) => a.map((row, i) => row.map((v, j) => v + b[i][j]));
const sub = (a, b) => a.map((row, i) => row.map((v, j) => v - b[i][j]));
const maxAbs = (m) => Math.max(0, ...m.flat().map(Math.abs));
const neg = (m) => m.map((row) => row.map((v) => -v));
/** 相対誤差 = max|X − Y| / max(1, max|Y|)。大きさが違えば無限大 */
function relErr(x, y) {
  if (rows(x) !== rows(y) || cols(x) !== cols(y)) return Infinity;
  return maxAbs(sub(x, y)) / Math.max(1, maxAbs(y));
}
// 複素行列 (re, im) の積
const cmul = ([ar, ai], [br, bi]) => [sub(mul(ar, br), mul(ai, bi)), add(mul(ar, bi), mul(ai, br))];
const cErr = ([xr, xi], [yr, yi]) => Math.max(relErr(xr, yr), relErr(xi, yi));
/** 複素共役転置 */
const cH = ([re, im]) => [T(re), neg(T(im))];

// テストの入力（JvmReference.java と同じ値）
const A = [[4, -2, 1, 0.5], [3, 1, -1, 2], [0.5, 2, 3, -1], [1, 0, 2, 5]];
const B = [[1, 0.5, -0.3, 0.2], [0.4, 2, 0.1, -0.5], [-0.2, 0.3, 1.5, 0.6], [0.7, -0.1, 0.2, 3]];
const S = [[4, 1, 0.5, 0.2], [1, 5, 1, 0.3], [0.5, 1, 6, 1], [0.2, 0.3, 1, 3]];
const R = [[1, 2, 0.5], [-1, 0.3, 2], [0.7, 1, -0.4], [2, -0.5, 1], [0.2, 0.8, 0.9]];
const RB = [[1, 0], [0, 1], [2, -1], [0.5, 0.5], [-1, 2]];
const X = [1, 2, 3, 4, 0, -1, -2, -3];
const XI = [0.5, 0, -0.5, 1, 0, 2, 0, -1];
const tol = 1e-12;

const properties = [];
/** check(関数名, 確かめる式, () => 相対誤差) */
function check(fn, formula, compute) {
  let error;
  let note = "";
  try {
    error = compute();
  } catch (e) {
    error = Infinity;
    note = String(e?.message ?? e);
  }
  properties.push({ fn, formula, error, ok: error <= PROPERTY_TOLERANCE, note });
}

/** 固有値・固有ベクトルの組（実部・虚部）について、max_k |A v_k − λ_k v_k| を返す */
function eigenResidual(aRe, aIm, valRe, valIm, vecRe, vecIm) {
  const n = rows(aRe);
  let worst = 0;
  for (let k = 0; k < n; k++) {
    const vr = vecRe.map((row) => [row[k]]);
    const vi = vecIm.map((row) => [row[k]]);
    const [avr, avi] = cmul([aRe, aIm], [vr, vi]);
    const lr = valRe[k];
    const li = valIm[k];
    const lvr = vr.map((row, i) => [lr * row[0] - li * vi[i][0]]);
    const lvi = vr.map((row, i) => [lr * vi[i][0] + li * row[0]]);
    worst = Math.max(worst, cErr([avr, avi], [lvr, lvi]));
  }
  return worst;
}

check("realQrDecompose", "A = Q·R、QᵀQ = I", () => {
  const { Q, R: Rm } = nfc.realQrDecompose(A);
  return Math.max(relErr(mul(Q, Rm), A), relErr(mul(T(Q), Q), eye(4)));
});
check("realQrDecompose（5x3）", "A = Q·R", () => {
  const { Q, R: Rm } = nfc.realQrDecompose(R);
  return relErr(mul(Q, Rm), R);
});
check("realQrDecomposeWithPermutation", "A·P = Q·R", () => {
  const { Q, R: Rm, P } = nfc.realQrDecomposeWithPermutation(A);
  return relErr(mul(Q, Rm), mul(A, P));
});
check("realLuDecompose", "A = L·U（L は行を並べ替えた下三角）", () => {
  const { L, U } = nfc.realLuDecompose(A, tol, false);
  return relErr(mul(L, U), A);
});
check("realLuDecomposeWithPermutation", "P·A = L·U", () => {
  const { L, U, P } = nfc.realLuDecomposeWithPermutation(A, tol, false);
  return relErr(mul(L, U), mul(P, A));
});
check("realCholeskyDecompose", "Rᵀ·R = A（A は対称正定値）", () => {
  const Rc = nfc.realCholeskyDecompose(S, tol);
  return relErr(mul(T(Rc), Rc), S);
});
check("realSchurDecompose", "A = U·T·Uᵀ、UᵀU = I", () => {
  const { U, T: Tm } = nfc.realSchurDecompose(A);
  return Math.max(relErr(mul(mul(U, Tm), T(U)), A), relErr(mul(T(U), U), eye(4)));
});
check("realHessenbergDecompose", "A = Q·H·Qᵀ", () => {
  const { Q, H } = nfc.realHessenbergDecompose(A);
  return relErr(mul(mul(Q, H), T(Q)), A);
});
check("realSvdDecompose", "A = U·D·Vᵀ", () => {
  const { U, D, V } = nfc.realSvdDecompose(A);
  return relErr(mul(mul(U, D), T(V)), A);
});
check("realSvdDecompose（5x3）", "A = U·D·Vᵀ", () => {
  const { U, D, V } = nfc.realSvdDecompose(R);
  return relErr(mul(mul(U, D), T(V)), R);
});
check("realEigenSolve", "A·v = λ·v（固有値は複素数を含む）", () => {
  const { ReValue, ImValue, ReVector, ImVector } = nfc.realEigenSolve(A);
  return eigenResidual(A, zeros(4, 4), ReValue, ImValue, ReVector, ImVector);
});
check("realLuInverse", "A·A⁻¹ = I", () => relErr(mul(A, nfc.realLuInverse(A, tol, false)), eye(4)));
check("realLuLeftDivide", "A·X = B", () => relErr(mul(A, nfc.realLuLeftDivide(A, B, tol, false)), B));
check("realLuGetDeterminant", "det(A) = 固有値の積", () => {
  const det = nfc.realLuGetDeterminant(A, tol, false);
  const { ReValue, ImValue } = nfc.realEigenSolve(A);
  let pr = 1;
  let pi = 0;
  for (let k = 0; k < ReValue.length; k++) {
    [pr, pi] = [pr * ReValue[k] - pi * ImValue[k], pr * ImValue[k] + pi * ReValue[k]];
  }
  return Math.max(Math.abs(det - pr), Math.abs(pi)) / Math.max(1, Math.abs(det));
});
check("realSvdPseudoInverse（5x3）", "A·A⁺·A = A", () => {
  const P = nfc.realSvdPseudoInverse(R, tol);
  return relErr(mul(mul(R, P), R), R);
});
check("realSvdLeastSquare（5x3）", "Aᵀ(A·X − B) = 0（正規方程式）", () => {
  const Xs = nfc.realSvdLeastSquare(R, RB, tol);
  return maxAbs(mul(T(R), sub(mul(R, Xs), RB))) / Math.max(1, maxAbs(RB));
});
check("realSvdRank / realSvdNorm", "rank(A) = 4、‖A‖₂ = 最大特異値", () => {
  const rank = nfc.realSvdRank(A, tol);
  const norm = nfc.realSvdNorm(A);
  const sv = nfc.realSvdSingularValue(A);
  return (rank === 4 ? 0 : Infinity) + Math.abs(norm - Math.max(...sv)) / Math.max(1, norm);
});
check("realExp", "exp([[0,1],[-1,0]]) = [[cos1, sin1],[-sin1, cos1]]", () => {
  const E = nfc.realExp([[0, 1], [-1, 0]]);
  return relErr(E, [[Math.cos(1), Math.sin(1)], [-Math.sin(1), Math.cos(1)]]);
});
check("realFft", "定義どおりの DFT（Σ x_n e^{−2πikn/N}）と一致", () => {
  const F = nfc.realFft(X, 8);
  const N = X.length;
  const re = [];
  const im = [];
  for (let k = 0; k < N; k++) {
    let r = 0;
    let i = 0;
    for (let n = 0; n < N; n++) {
      r += X[n] * Math.cos((-2 * Math.PI * k * n) / N);
      i += X[n] * Math.sin((-2 * Math.PI * k * n) / N);
    }
    re.push(r);
    im.push(i);
  }
  return relErr(F, [re, im]);
});
check("complexFft → complexFftIfft", "ifft(fft(x)) = x", () => {
  const F = nfc.complexFft(X, XI, 8);
  const back = nfc.complexFftIfft(F[0], F[1], 8);
  return relErr(back, [X, XI]);
});
check("complexEigenSolve", "A·v = λ·v（A は複素行列）", () => {
  const { ReValue, ImValue, ReVector, ImVector } = nfc.complexEigenSolve(A, B);
  return eigenResidual(A, B, ReValue, ImValue, ReVector, ImVector);
});
check("complexQrDecompose", "A = Q·R（複素）", () => {
  const { ReQ, ImQ, ReR, ImR } = nfc.complexQrDecompose(A, B);
  return cErr(cmul([ReQ, ImQ], [ReR, ImR]), [A, B]);
});
check("complexLuInverse", "A·A⁻¹ = I（複素）", () => {
  const [Xr, Xi] = nfc.complexLuInverse(A, B, tol, false);
  return cErr(cmul([A, B], [Xr, Xi]), [eye(4), zeros(4, 4)]);
});
check("complexSvdDecompose", "A = U·D·Vᴴ（複素）", () => {
  const { ReU, ImU, D, ReV, ImV } = nfc.complexSvdDecompose(A, B);
  return cErr(cmul(cmul([ReU, ImU], [D, zeros(rows(D), cols(D))]), cH([ReV, ImV])), [A, B]);
});
check("complexSchurDecompose", "A = U·T·Uᴴ（複素）", () => {
  const { ReU, ImU, ReT, ImT } = nfc.complexSchurDecompose(A, B);
  return cErr(cmul(cmul([ReU, ImU], [ReT, ImT]), cH([ReU, ImU])), [A, B]);
});

// ---- 手書きのブリッジから差し込んだ関数（step03、設定ファイルの extra_exports） ----
// JS ラッパーには入れていないので、wasm の関数（ex.multiply など）を直接呼んで確かめる

/** 2 次元配列 → 行優先の 1 次元配列 */
const flat = (m) => m.flat();
/** Matrices.pack の形式 [k, r1, c1, …, データ…] → 行列の配列 */
function unpackPacked(packed) {
  const values = Array.from(packed);
  const count = values[0];
  let offset = 1 + count * 2;
  const out = [];
  for (let k = 0; k < count; k++) {
    const r = values[1 + k * 2];
    const c = values[2 + k * 2];
    const m = [];
    for (let i = 0; i < r; i++) {
      m.push(values.slice(offset, offset + c));
      offset += c;
    }
    out.push(m);
  }
  return out;
}
const M32 = [[1, 2], [0, -1], [3, 0.5]];

check("multiply（4x4 × 4x4）", "multiply(A, B) = A·B（JS で計算した積と一致）", () => {
  const [C] = unpackPacked(ex.multiply(flat(A), 4, 4, flat(B), 4));
  return relErr(C, mul(A, B));
});
check("multiply（5x3 × 3x2）", "multiply(R, M) = R·M（長方形）", () => {
  const [C] = unpackPacked(ex.multiply(flat(R), 5, 3, flat(M32), 2));
  return relErr(C, mul(R, M32));
});
check("prepareMultiply → multiplyPrepared", "C = A·B の先頭の成分 ＋ 最後の成分", () => {
  ex.prepareMultiply(flat(A), 4, 4, flat(B), 4);
  const value = ex.multiplyPrepared();
  const C = mul(A, B);
  const expected = C[0][0] + C[3][3];
  return Math.abs(value - expected) / Math.max(1, Math.abs(expected));
});

// ---- 集計とレポート ----

const count = (list, pred) => list.filter(pred).length;
const exportNames = [...new Set(callResults.map((r) => r.call.export))];
const sameCount = count(callResults, (r) => r.verdict === "same");
const closeCount = count(callResults, (r) => r.verdict === "close");
const differCount = count(callResults, (r) => r.verdict === "differ");
const bothErrorCount = count(callResults, (r) => r.note === "両方とも例外");
const propertyOk = count(properties, (p) => p.ok);

const fmt = (x) => (x === 0 ? "0" : Number.isFinite(x) ? x.toExponential(2) : String(x));
const lines = [];
lines.push("# 検算レポート（自動生成）");
lines.push("");
lines.push(`- 実行日時：${new Date().toISOString()}`);
lines.push(`- Wasm：\`${path.relative(process.cwd(), wasmPath)}\``);
lines.push(`- 正解データ：\`${path.relative(process.cwd(), referencePath)}\`（JVM で実行した結果）`);
lines.push("");
lines.push("## 1. JVM との一致");
lines.push("");
lines.push(`公開関数 ${exportNames.length} 個 × 入力 ${reference.cases.length} 種類 = ${callResults.length} 回の呼び出しを比べた。`);
lines.push("");
lines.push("| 判定 | 回数 | 意味 |");
lines.push("|---|---|---|");
lines.push(`| 完全一致 | ${sameCount} | 結果のすべての値がビット単位で同じ（うち ${bothErrorCount} 回は JVM・Wasm とも例外） |`);
lines.push(`| 誤差内で一致 | ${closeCount} | 相対誤差 ${CLOSE} 以下 |`);
lines.push(`| 不一致 | ${differCount} | 上記以外 |`);
lines.push("");
lines.push("入力の種類：");
for (const c of reference.cases) lines.push(`- \`${c.name}\`：${c.description}`);
lines.push("");
lines.push("### 関数ごとの結果");
lines.push("");
lines.push(`| 関数 | ${reference.cases.map((c) => c.name).join(" | ")} |`);
lines.push(`|---|${reference.cases.map(() => "---").join("|")}|`);
const mark = (r) => {
  if (!r) return "—";
  if (r.note === "両方とも例外") return "例外（一致）";
  if (r.verdict === "same") return "一致";
  if (r.verdict === "close") return `誤差 ${fmt(r.maxDiff)}`;
  return `**不一致** ${r.note || fmt(r.maxDiff)}`;
};
for (const name of exportNames) {
  const cells = reference.cases.map((c) => mark(callResults.find((r) => r.call.export === name && r.call.case === c.name)));
  lines.push(`| \`${name}\` | ${cells.join(" | ")} |`);
}
lines.push("");
lines.push("### 例外になった呼び出し（JVM 側のメッセージ）");
lines.push("");
const errorMessages = new Map();
for (const r of callResults.filter((r) => r.call.outcome === "error")) {
  const key = r.call.error;
  if (!errorMessages.has(key)) errorMessages.set(key, []);
  errorMessages.get(key).push(`${r.call.export}（${r.call.case}）`);
}
for (const [message, where] of errorMessages) {
  lines.push(`- \`${message}\`：${where.length} 回 — ${where.slice(0, 6).join("、")}${where.length > 6 ? " など" : ""}`);
}
lines.push("");
lines.push("## 2. 数学的な性質");
lines.push("");
lines.push(`JS ラッパー（nfc.generated.js）経由で計算し、式が成り立つかを相対誤差 ${PROPERTY_TOLERANCE} 以下で判定した。`);
lines.push("");
lines.push("| 関数 | 確かめた式 | 相対誤差 | 判定 |");
lines.push("|---|---|---|---|");
for (const p of properties) {
  lines.push(`| \`${p.fn}\` | ${p.formula} | ${fmt(p.error)} | ${p.ok ? "OK" : `**NG** ${p.note}`} |`);
}
lines.push("");
lines.push("## 3. 手書き版との比較（JVM 上）");
lines.push("");
lines.push("| 手書き版 | 自動生成版 | 判定 | 説明 |");
lines.push("|---|---|---|---|");
for (const h of reference.handwritten) {
  lines.push(`| \`${h.handwritten}\` | \`${h.generated}\` | ${h.verdict} | ${h.detail} |`);
}
lines.push("");

fs.mkdirSync(path.dirname(reportPath), { recursive: true });
fs.writeFileSync(reportPath, lines.join("\n"), "utf8");

const handSame = count(reference.handwritten, (h) => h.verdict === "same" || h.verdict === "close");
console.log(`JVM との一致: ${callResults.length} 回中 完全一致 ${sameCount}・誤差内 ${closeCount}・不一致 ${differCount}`);
console.log(`数学的な性質: ${properties.length} 項目中 OK ${propertyOk}`);
console.log(`手書き版との比較: ${reference.handwritten.length} 組中 一致 ${handSame}`);
console.log(`レポート: ${reportPath}`);

const failed = differCount > 0 || propertyOk < properties.length || handSame < reference.handwritten.length;
process.exit(failed ? 1 : 0);
