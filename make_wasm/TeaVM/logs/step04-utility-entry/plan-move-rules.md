# 計画：ルール `.rules/` を `.claude/rules/` に移す（step04 の中の手直し）

## 目的

- 今の `.rules/` は、`CLAUDE.md` の「読むこと」という指示に頼っていて、Claude が読み忘れることがある。
- Claude Code には、**`.claude/rules/` に置いた md を、セッションの開始時に自動で読み込む**しくみがある（`CLAUDE.md` と同じ扱い。サブフォルダの中も読む）。
- ここに移せば、ルールが毎回必ず Claude に読み込まれる。skill（`.claude/skills/step-quiz/`）とも同じ `.claude/` にまとまる。

## やること

1. `git mv .rules .claude/rules`（フォルダの中の構成 `workflow/`・`git/`・`records/`・`coding/` と `rule.md` はそのまま。git には名前変更として残り、`git log --follow` で前の履歴もたどれる）
2. 参照しているパスを直す（今使っている文書だけ）
   | ファイル | 直す内容 |
   |---|---|
   | `CLAUDE.md` | 先頭の注意・構成の表・作業ルール・作業上の注意の `.rules/…` → `.claude/rules/…`。先頭の注意は「`.claude/rules/` のルールは自動で読み込まれる。目次は `rule.md`」に書き換える |
   | `.claude/rules/rule.md` | 冒頭に「このフォルダは Claude Code が自動で読み込む」ことを書く。中のリンクは相対パスなので変更不要 |
   | `.claude/skills/step-quiz/SKILL.md` | `.rules/…` の 2 か所 |
   | `logs/README.md` | `../.rules/…` の 2 か所 → `../.claude/rules/…` |
3. 記録：`logs/step04-utility-entry/Artifact.md` に「ルールの置き場所の変更」の節を足し、この計画を `logs/step04-utility-entry/plan-move-rules.md` に移す（step01 の `verify-tools/` の名前変更と同じやり方）
4. `step04-utility-entry` ブランチでコミットし、GitHub（`origin`）に push する

## やらないこと

- ルールの中身は変えない（置き場所と、パスの書き換えだけ）。
- 過去の記録（`logs/step00`〜`step02` の `Artifact.md`・`plan.md` に出てくる `.rules/`）は、その時点の記録なので書き換えない。
- 作業中の `logs/step01-generated-verify/quiz.md` の変更（コミットされていないもの）には触らない。
- コード・生成器・ビルドは触らない（検算は不要）。

## 確認方法

- `ls -R .claude/rules` で 8 つの md があり、`ls .rules` が「存在しない」になる。
- `grep -rn "\.rules/" CLAUDE.md .claude logs/README.md` で、`.claude/rules/` 以外の古いパスが出ない。
- 新しいセッションを開いて `/memory` を実行すると、`.claude/rules/` の各ファイルが読み込まれた一覧に出る（または「作業ルールの目次は？」と聞いて答えられる）。

## ブランチ

- 新しいステップではなく step04 の中の手直しなので、今の `step04-utility-entry` の上で行う。

## 決めてほしいこと

1. **読み込みのタイミング**
   - A（おすすめ）：全部のファイルを毎回読み込む。合計 160 行ほどで小さく、「必ず従う」ルールなので常に入っているほうが安全。
   - B：`coding/python-venv.md` などに `paths:`（例：`**/*.py`）を付け、関係するファイルを触るときだけ読み込む。読み込む量は減るが、読み込まれない場面が出る。
2. **記録の置き場所**：上の 3 のとおり step04 の記録に足してよいか（別に `stepNN` を立てる必要はないと考えている）。
