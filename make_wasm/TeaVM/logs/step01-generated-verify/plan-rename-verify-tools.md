# 計画：step01 の手直し（`verify/` を `verify-tools/` に名前変更）

- 状態：**承認済み・実行済み**（2026-11-12 承認。選択肢は a①・b①）
- きっかけ：`app/src/verify/`（Java の検算プログラム）と、TeaVM 直下の `verify/`（Node と bash の検算スクリプト）の名前が同じで紛らわしい、という指摘（2026-11-12）
- ブランチ：**`step01-generated-verify` のまま**作業する（新しいブランチは作らない）。step01 で作ったものの手直しで、step01 の理解の確認もまだ終わっていないため

## 1. 目的

TeaVM 直下の `verify/` を `verify-tools/` に名前変更し、Gradle のソースセット `app/src/verify/`（Java）と区別できるようにする。
中身（スクリプトの処理）は変えない。

| 変更後 | 中身 | 誰が動かすか |
|---|---|---|
| `app/src/verify/java/` | `JvmReference.java`（そのまま） | Gradle |
| `verify-tools/` | `wasm-verify.mjs`、`run.sh` | Node と bash |

## 2. やること

1. `git mv verify verify-tools` でフォルダの名前を変える（git に「名前変更」として記録される）。
2. `verify/` を参照している箇所を `verify-tools/` に直す。

   | ファイル | 直す箇所 |
   |---|---|
   | `verify-tools/run.sh` | 先頭のコメント（`node verify/wasm-verify.mjs` の行） |
   | `verify-tools/wasm-verify.mjs` | 先頭の使い方のコメント、引数が足りないときのエラー表示（3 か所） |
   | `app/src/verify/java/verify/JvmReference.java` | Javadoc の `verify/wasm-verify.mjs`（1 か所） |
   | `app/build.gradle.kts` | コメントの `verify/run.sh`（1 か所） |
   | `CLAUDE.md` | ファイル構成の表とコマンドの説明（4 か所） |
   | `.rules/rule.md` | `verify/run.sh`（2 か所） |
   | `next-plan/step02-config-yaml.md` | `verify/run.sh`（2 か所） |
   | `logs/step01-generated-verify/Artifact.md` | 下の「記録の扱い」を参照 |

3. **記録の扱い**：`Artifact.md` は「そのとき何をしたか」の記録なので、過去の作業の説明（ウォークスルーなど）は書き換えない。その代わりに次のようにする。
   - 「8. 確認用コマンド」は、今のフォルダ名で実行できるように `verify-tools/run.sh` に直す。
   - 「保存（コミットと push）」の表の前に、「名前変更」の節を足し、何をなぜ変えたかを書く。
   - この計画は、実行後に `logs/step01-generated-verify/plan-rename-verify-tools.md` に移す。
4. ビルドと検算をやり直す。
5. step01 のブランチでコミットし、GitHub（`origin`）に push する。

## 3. やらないこと

- `app/src/verify/`（Gradle のソースセット）の名前・場所は変えない。
- スクリプトの処理内容は変えない。
- `build/verify/`（検算の出力先。git 管理外）の名前は変えない。

## 4. 確認方法（「できた」の基準）

- [ ] `verify-tools/run.sh` が、step01 と同じ結果（JVM との一致 207/207、数学的な性質 25/25、手書き版との一致 27/27、終了コード 0）で成功する
- [ ] `grep` で探しても、`verify/run.sh`・`verify/wasm-verify.mjs` という古いパスが、記録の過去の説明以外に残っていない
- [ ] `git log --follow verify-tools/run.sh` で、名前変更前の履歴もたどれる

## 5. 決めてほしいこと

| # | 内容 | 選択肢 | おすすめ |
|---|---|---|---|
| a | 作業するブランチ | ① `step01-generated-verify` のまま ② 新しいブランチ（例：`step01b-rename-verify-tools`）を作る | **①**：step01 で作ったものの手直しで、step01 の理解の確認もまだのため |
| b | `Artifact.md` の過去の説明の `verify/` | ① 書き換えず、名前変更の節を足す ② すべて `verify-tools/` に書き換える | **①**：記録は「そのとき何をしたか」を残すもののため |
