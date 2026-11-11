# step00-baseline：作業前の状態の保存

- ブランチ：`step00-baseline`（`main` の `31d0d43` から作成）
- 日付：2026-11-11

## 概要

「P6P7_対策の検討.pdf」の順番で作業を始める前に、今の状態を 1 本のブランチに保存する。
あわせて、ステップごとにブランチと作業ログを残すルールを決める。

## タスク

- [x] `main` から `step00-baseline` を作る
- [x] まだコミットしていなかった変更を、そのまま保存する
- [x] 作業ルール（`docs/worklog/README.md`）を作る
- [x] `CLAUDE.md` に作業ルールを追記する
- [x] `.gitignore` に Python のキャッシュ（`__pycache__/`）を追加する

## 計画

- ブランチ名は、依頼にあった `pre-version` / `1st-change` の代わりに `stepNN-内容` の形にした。
  - 番号で並ぶので、ステップが増えても順番が分かる。
  - 番号の後ろに内容が付くので、ブランチ名だけで何をしたか分かる。
- `main` は触らず、作業前の状態の基準として残す。

## 実施内容

```bash
git switch -c step00-baseline
# .gitignore に __pycache__/ と *.pyc を追加
# docs/worklog/ を作成し、README.md とこのファイルを追加
# CLAUDE.md（フォルダ構成の説明）に作業ルールを追記
git add -A && git commit
```

このブランチに含めた「まだコミットしていなかった変更」（作業開始時点の状態そのまま）：

| 変更 | 内容 |
|---|---|
| `JavaStructureAnalyzer.java` | 学習用のコメントの追加 |
| `JavaGenerator_解説.pdf`、`JavaStructureAnalyzer_解説.pdf` | 削除 |
| `Gradle_Kotlin解説/` | `build.gradle.kts` の解説 PDF を追加 |
| `CLAUDE.md` | フォルダ構成と役割の説明（以前のセッションで作成） |

## 変更したファイル

- `.gitignore`（リポジトリのルート）
- `make_wasm/TeaVM/CLAUDE.md`
- `make_wasm/TeaVM/docs/worklog/README.md`
- `make_wasm/TeaVM/docs/worklog/step00-baseline.md`

## 検証結果

- コードの変更はないので、ビルドや動作の確認は行っていない。

## 次のステップへ

- `step01-generated-verify` で、① 自動生成版への切り替えと検算を行う。
