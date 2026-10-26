import { defineConfig, type Plugin } from "vite";
import tailwindcss from "@tailwindcss/vite";

// npm run java で public/wasm/ が更新されたらブラウザを自動リロードする
// （Wasm は HMR で差し替えられないため full-reload）
function reloadOnWasmChange(): Plugin {
  return {
    name: "reload-on-wasm-change",
    configureServer(server) {
      let timer: ReturnType<typeof setTimeout> | undefined;
      server.watcher.on("change", (file) => {
        if (!/[\\/]public[\\/]wasm[\\/]/.test(file)) return;
        clearTimeout(timer);
        timer = setTimeout(() => server.ws.send({ type: "full-reload" }), 300);
      });
    },
  };
}

export default defineConfig({
  plugins: [tailwindcss(), reloadOnWasmChange()],
  server: {
    // WSL の inotify がホスト経由の変更を拾えない場合に備える
    watch: { ignored: ["**/.java-build/**"] },
  },
});
