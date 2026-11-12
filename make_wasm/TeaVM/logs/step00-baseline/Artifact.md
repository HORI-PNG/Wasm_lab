# Artifact：step00-baseline（作業前の状態の保存）

## 1. 概要

「P6P7_対策の検討.pdf」の順番で作業を始める前に、今の状態を 1 本のブランチに保存した。
あわせて、ステップごとにブランチと記録を残すルールを決めた。

- ブランチ：`step00-baseline`（`main` の `31d0d43` から作成）
- 日付：2026-11-11
- 承認：ユーザーの依頼（「現在の内容は pre-version という名前で残す」）に基づいて実行

## 2. タスク

- [x] `main` から `step00-baseline` を作る
- [x] まだコミットしていなかった変更を、そのまま保存する
- [x] 作業ルールを作る（当初は `docs/worklog/README.md`。後に `.rules/rule.md` へ移動）
- [x] `CLAUDE.md` に作業ルールを追記する
- [x] `.gitignore` に Python のキャッシュ（`__pycache__/`）を追加する

## 3. 実装計画

- ブランチ名は、依頼にあった `pre-version` / `1st-change` の代わりに `stepNN-内容` の形にした。
  - 番号で並ぶので、ステップが増えても順番が分かる。
  - 番号の後ろに内容が付くので、ブランチ名だけで何をしたか分かる。
- `main` は触らず、作業前の状態の基準として残す。

## 4. ウォークスルー

```bash
git switch -c step00-baseline
# .gitignore に __pycache__/ と *.pyc を追加
# 作業ルールと記録のファイルを追加
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

## 5. 変更したファイル

| ファイル | 内容 |
|---|---|
| `.gitignore`（リポジトリのルート） | Python のキャッシュを追加 |
| `make_wasm/TeaVM/CLAUDE.md` | 追加 |
| 作業ルール・記録のファイル | 追加（コミット `80e8f01` の時点では `docs/worklog/`。後に `.rules/` と `logs/` に移動） |

## 6. 検証結果

- コードの変更はないので、ビルドや動作の確認は行っていない。

## 7. 分かったこと・残った課題

- なし。次は step01（① 自動生成版への切り替えと検算）。

## 8. 確認用コマンド

すべて `make_wasm/TeaVM` フォルダで実行する。

**① ブランチの一覧を見る**

```bash
git branch
```

期待する結果：`main`・`step00-baseline`・`step01-generated-verify` が並ぶ。

**② step00 のコミットの中身を見る**

```bash
git show --stat step00-baseline
```

期待する結果：コミット `80e8f01`「step00: 作業前の状態を保存し…」と、変更したファイルの一覧（`CLAUDE.md`、`.gitignore`、解説 PDF の削除と追加など）が出る。

**③ 作業前（main）との差を見る**

```bash
git diff --stat main step00-baseline
```

期待する結果：②と同じファイルの一覧が出る（コードの計算部分は変わっていない）。
