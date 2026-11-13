# 作業ルール（目次）

このフォルダ（`make_wasm/TeaVM`）で作業するときのルール。Claude はこのルールに必ず従う。
ルールは内容ごとにフォルダとファイルに分けている。**作業を始める前に、関係するファイルを読むこと。**

## ファイルの一覧

| フォルダ | ファイル | 内容 | いつ読むか |
|---|---|---|---|
| `workflow/` | [`flow.md`](workflow/flow.md) | 作業の流れ：計画 → 確認 → 実行 → 記録 → 理解の確認 | **毎回**（作業を始める前） |
| `workflow/` | [`plan.md`](workflow/plan.md) | 計画（`next-plan/stepNN-内容.md`）の書き方 | 計画を書くとき |
| `workflow/` | [`understanding-check.md`](workflow/understanding-check.md) | 理解の確認（問題）。詳しい手順は skill `step-quiz` | ステップが終わったとき |
| `git/` | [`branch-and-push.md`](git/branch-and-push.md) | ブランチの作り方、コミット、GitHub / Gitblit への push、認証情報の扱い | ブランチを作る・コミットする・push するとき |
| `records/` | [`artifact.md`](records/artifact.md) | 記録（`logs/stepNN-内容/Artifact.md`）の形式と、確認用コマンドの書き方 | 記録を書くとき |
| `coding/` | [`code-style.md`](coding/code-style.md) | コードの書き方（NFC 本体は変えない、生成物は生成器から直す、など） | コードを書くとき |
| `coding/` | [`python-venv.md`](coding/python-venv.md) | Python のライブラリは仮想環境（`.venv/`）を使う | Python を動かす・ライブラリを入れるとき |

## いちばん大事なこと（要点）

1. **ユーザーが計画を承認するまで、作業を始めない。**「進めたい」などの方針の話は承認ではない（`workflow/flow.md`）。
2. **ステップが終わったら説明して問題を出し、ユーザーが合格してから次へ進む**（`workflow/understanding-check.md`）。
3. **GitHub（`origin`）へは Claude が push、Gitblit へはユーザーが push。** パスワードは扱わない（`git/branch-and-push.md`）。
4. **Python のライブラリは仮想環境に入れる。** システムには入れない・消さない（`coding/python-venv.md`）。

## ルールを変えるとき

- ユーザーの指示で変える。変えたら、この目次と `CLAUDE.md` の要点も合わせて直す。
- ルールだけを変えたコミットも、GitHub に push する（`git/branch-and-push.md`）。
