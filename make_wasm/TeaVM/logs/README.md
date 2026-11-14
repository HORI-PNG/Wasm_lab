# 作業の記録（logs）

ステップごとに 1 フォルダ。中の `Artifact.md` に、やったことと結果を書く。
書き方のルールは [`../.rules/records/artifact.md`](../.rules/records/artifact.md) を参照（ルール全体の目次は [`../.rules/rule.md`](../.rules/rule.md)）。

## ステップ一覧

| ステップ | ブランチ | 内容 | 状態 | 記録 |
|---|---|---|---|---|
| 00 | `step00-baseline` | 作業前の状態の保存、作業ルールの整備 | 完了 | [Artifact.md](step00-baseline/Artifact.md) |
| 01 | `step01-generated-verify` | ① 自動生成版への切り替えと検算（自動テストの仕組み） | 完了・**理解の確認済み**（2026-11-13 合格、[quiz.md](step01-generated-verify/quiz.md)） | [Artifact.md](step01-generated-verify/Artifact.md) |
| 02 | `step02-config-yaml` | ⑥ 生成設定を YAML に分ける、Python の仮想環境 | 完了・**理解の確認済み**（2026-11-13 合格、[quiz.md](step02-config-yaml/quiz.md)） | [Artifact.md](step02-config-yaml/Artifact.md) |
| 03 | `step03-extra-exports` | ② 手書きの関数（計測用の `multiply` など）を自動生成版に差し込む | 完了・**理解の確認済み**（2026-11-14 合格、[quiz.md](step03-extra-exports/quiz.md)） | [Artifact.md](step03-extra-exports/Artifact.md) |
| 04 | `step04-utility-entry`（予定） | ③ 入口のクラスを広げ（`DoubleMatrixUtil`）、変換ルールを足す | **計画の確認待ち**（[`next-plan/step04-utility-entry.md`](../next-plan/step04-utility-entry.md)） | — |
| 05 | （予定） | ④ ハンドル方式 | 未着手 | — |
| 06 | （予定） | ⑤ 関数渡しの実験、⑦ エラー情報 | 未着手 | — |
| 07 | （予定） | P7 汎用化 | 未着手 | — |

## ブランチの関係

```
main（作業前のまま）
 └ step00-baseline
    └ step01-generated-verify
       └ step02-config-yaml
          └ step03-extra-exports
```
