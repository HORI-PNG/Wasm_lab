import "./style.css";
import { initJava, callJava } from "./java/java-runtime";
import { javaFunctions } from "./java/java-api";
// 型付きで呼ぶ場合は、生成されたクラス名で import する。例:
//   import { HelloWasm } from "./java/java-api";
//   const sum: number = HelloWasm.add(3, 4);

type Param = { readonly name: string; readonly type: string };
type Fn = {
  readonly key: string;
  readonly class: string;
  readonly name: string;
  readonly signature: string;
  readonly params: readonly Param[];
  readonly returns: string;
};
const functions: readonly Fn[] = javaFunctions;

const statusEl = document.querySelector<HTMLParagraphElement>("#status")!;
const listEl = document.querySelector<HTMLElement>("#functions")!;

function setStatus(state: "loading" | "ready" | "error", text: string) {
  statusEl.dataset.state = state;
  statusEl.textContent = text;
}

const BASE_LABELS: Record<string, string> = {
  number: "数値",
  string: "文字列",
  boolean: "真偽値",
  bigint: "整数（BigInt）",
  void: "なし",
};

/** "number[][] | null" → { base: "number", dims: 2, nullable: true } */
function parseType(type: string) {
  const nullable = type.endsWith(" | null");
  const t = type.replace(" | null", "");
  const dims = (t.match(/\[\]/g) ?? []).length;
  return { base: t.replace(/\[\]/g, ""), dims, nullable };
}

/** TS の型を日本語で説明する（例: 数値の2次元配列） */
function typeLabel(type: string): string {
  const { base, dims } = parseType(type);
  const b = BASE_LABELS[base] ?? base;
  if (dims === 0) return b;
  return dims === 1 ? `${b}の配列` : `${b}の${dims}次元配列`;
}

function placeholder(type: string): string {
  const { base, dims, nullable } = parseType(type);
  if (dims >= 2) return "JSON 形式で入力 例: [[1, 2], [3, 4]]";
  if (dims === 1) return base === "string" ? "カンマ区切り 例: a, b, c" : "カンマ区切り 例: 1, 2, 3";
  if (base === "bigint") return "整数を入力 例: 12345678901234567890";
  if (base === "string") return nullable ? "文字列を入力（null と入力すると null）" : "文字列を入力";
  return "数値を入力";
}

/** 入力欄の文字列を TS の型に合わせて変換する */
function parseArg(p: Param, el: HTMLInputElement): unknown {
  const { base, dims, nullable } = parseType(p.type);
  const raw = el.value.trim();
  if (nullable && raw === "null") return null;
  if (dims > 0) {
    if (raw.startsWith("[")) {
      let v: unknown;
      try {
        v = JSON.parse(raw);
      } catch {
        throw new Error(`「${p.name}」の形式が正しくありません。JSON の配列で入力してください（例: [[1, 2], [3, 4]]）`);
      }
      if (!Array.isArray(v)) throw new Error(`「${p.name}」は配列で入力してください`);
      return v;
    }
    if (dims >= 2) throw new Error(`「${p.name}」は${typeLabel(p.type)}です。JSON 形式で入力してください（例: [[1, 2], [3, 4]]）`);
    // 1 次元配列はカンマ区切りも受け付ける
    const items = raw === "" ? [] : raw.split(",").map((s) => s.trim());
    if (base === "number") {
      const nums = items.map(Number);
      if (nums.some(Number.isNaN)) throw new Error(`「${p.name}」には数値をカンマ区切りで入力してください`);
      return nums;
    }
    if (base === "boolean") return items.map((s) => s === "true");
    return items;
  }
  switch (base) {
    case "boolean":
      return el.checked;
    case "number": {
      const n = Number(raw);
      if (raw === "" || Number.isNaN(n)) throw new Error(`「${p.name}」には数値を入力してください`);
      return n;
    }
    case "bigint":
      try {
        return BigInt(raw || "0");
      } catch {
        throw new Error(`「${p.name}」には整数を入力してください`);
      }
    default:
      return el.value;
  }
}

function formatResult(v: unknown): string {
  if (v === undefined) return "（戻り値なし）";
  return JSON.stringify(v, (_, x) => (typeof x === "bigint" ? `${x}n` : x)) ?? String(v);
}

/** Java 側の例外（例: java.lang.ArithmeticException: / by zero）は日本語の前置きを付けて表示する */
function formatError(err: unknown): string {
  const msg = err instanceof Error ? err.message : String(err);
  return /^[\w.$]+(Exception|Error)\b/.test(msg) ? `Java で例外が発生しました: ${msg}` : msg;
}

function renderCard(fn: Fn): HTMLElement {
  const card = document.createElement("article");
  card.className = "card";

  const title = document.createElement("h2");
  title.className = "font-mono text-base font-semibold";
  title.textContent = `${fn.class}.${fn.name}`;
  const sig = document.createElement("p");
  sig.className = "mt-0.5 text-xs text-slate-500 dark:text-slate-400";
  sig.textContent = fn.signature;
  const ret = document.createElement("p");
  ret.className = "mt-1 text-xs text-slate-500 dark:text-slate-400";
  ret.textContent = `戻り値: ${typeLabel(fn.returns)}`;
  card.append(title, sig, ret);

  const form = document.createElement("form");
  form.className = "mt-4 space-y-2";
  if (fn.params.length === 0) {
    const none = document.createElement("p");
    none.className = "text-sm text-slate-500 dark:text-slate-400";
    none.textContent = "引数はありません";
    form.append(none);
  }
  const inputs = fn.params.map((p) => {
    const label = document.createElement("label");
    label.className = "flex items-center gap-3 text-sm";
    const name = document.createElement("span");
    name.className = "w-28 shrink-0";
    const pname = document.createElement("span");
    pname.className = "block font-mono text-slate-600 dark:text-slate-300";
    pname.textContent = p.name;
    const ptype = document.createElement("span");
    ptype.className = "block text-xs text-slate-400 dark:text-slate-500";
    ptype.textContent = typeLabel(p.type);
    name.append(pname, ptype);
    const input = document.createElement("input");
    if (p.type === "boolean") {
      input.type = "checkbox";
      input.className = "size-4 accent-indigo-600";
    } else {
      input.type = p.type === "number" ? "number" : "text";
      input.step = "any";
      input.className = "field";
      input.placeholder = placeholder(p.type);
    }
    label.append(name, input);
    form.append(label);
    return input;
  });

  const btn = document.createElement("button");
  btn.type = "submit";
  btn.className = "btn";
  btn.textContent = "実行";
  form.append(btn);

  const out = document.createElement("output");
  out.className = "result block";
  out.textContent = "結果はここに表示されます";

  form.addEventListener("submit", (e) => {
    e.preventDefault();
    try {
      const args = fn.params.map((p, i) => parseArg(p, inputs[i]));
      const t0 = performance.now();
      const r = callJava(fn.key, args);
      const ms = (performance.now() - t0).toFixed(2);
      delete out.dataset.error;
      out.textContent = `結果: ${formatResult(r)}（処理時間 ${ms} ミリ秒）`;
    } catch (err) {
      out.dataset.error = "";
      out.textContent = formatError(err);
    }
  });

  card.append(form, out);
  return card;
}

async function main() {
  try {
    await initJava();
    listEl.replaceChildren(...functions.map(renderCard));
    setStatus("ready", `準備完了 — ${functions.length} 個の Java 関数を利用できます`);
  } catch (err) {
    console.error(err);
    setStatus("error", `起動に失敗しました: ${err instanceof Error ? err.message : err}`);
  }
}

main();
