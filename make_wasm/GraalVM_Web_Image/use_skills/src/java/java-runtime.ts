// GraalVM Web Image のランチャー（public/wasm/<name>.js）を読み込み、Java VM を起動する。
// ランチャーは document.currentScript.src + ".wasm" を読みに行くため、
// Vite のバンドル対象にせず public/ から classic script として読み込む。

type JavaFn = (...args: unknown[]) => unknown;

declare global {
  // eslint-disable-next-line no-var
  var GraalVM: { run(vmArgs: string[], config?: unknown): Promise<unknown> } | undefined;
  // eslint-disable-next-line no-var
  var __javaExports: Record<string, JavaFn> | undefined;
}

let ready: Promise<void> | null = null;

function loadScript(src: string): Promise<void> {
  return new Promise((resolve, reject) => {
    const s = document.createElement("script");
    s.src = src;
    s.onload = () => resolve();
    s.onerror = () => reject(new Error(`ランチャーを読み込めません: ${src}`));
    document.head.appendChild(s);
  });
}

/** Java(Wasm) を初期化する。何度呼んでも 1 回だけ起動する。 */
export function initJava(name = "app"): Promise<void> {
  ready ??= (async () => {
    if (!globalThis.GraalVM) {
      await loadScript(`${import.meta.env.BASE_URL}wasm/${name}.js`);
    }
    if (!globalThis.GraalVM) throw new Error("GraalVM ランチャーが初期化されていません");
    await globalThis.GraalVM.run([]);
    if (!globalThis.__javaExports) throw new Error("Java 側の関数が登録されていません");
  })();
  return ready;
}

/** 生成された java-api.ts から使う低レベル呼び出し */
export function callJava(key: string, args: unknown[]): unknown {
  const fn = globalThis.__javaExports?.[key];
  if (!fn) throw new Error(`Java 関数 ${key} が未登録です。先に await initJava() を呼んでください`);
  return fn(...args);
}
