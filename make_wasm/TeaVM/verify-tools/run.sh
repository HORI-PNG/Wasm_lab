#!/usr/bin/env bash
# 自動生成版の検算をまとめて実行する（step01 で追加）。TeaVM フォルダで実行する。
#
#   1. Wasm をビルドする（./gradlew :app:build）
#   2. JVM で正解データを作る（./gradlew :app:verifyJvm → build/verify/jvm-reference.json）
#   3. Node で Wasm を動かして比べる（verify-tools/wasm-verify.mjs → build/verify/report.md）
#
# 不一致や NG が 1 つでもあれば、終了コード 1 で終わる。
set -euo pipefail
cd "$(dirname "$0")/.."

./gradlew :app:build :app:verifyJvm --console=plain -q
node verify-tools/wasm-verify.mjs \
  app/build/generated/teavm/wasm-gc \
  build/verify/jvm-reference.json \
  app/src/main/webapp/nfc.generated.js \
  build/verify/report.md
