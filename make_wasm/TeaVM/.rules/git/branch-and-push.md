# ブランチと push

- 名前は `stepNN-内容`（NN は 2 桁の番号）。番号順に並び、名前を見れば内容が分かるようにする。
- 各ステップのブランチは、1 つ前のステップのブランチから作る。終わったブランチは消さずに残す。
- `main` は作業前の状態のまま触らない。`step00-baseline` は作業前の状態を保存したブランチ。
- コミットは、そのステップのブランチの上で、ステップが終わったときに行う。
- **GitHub への push は Claude が定期的に行う。** 具体的には次のタイミング。
  - ステップが終わってコミットした直後
  - ルールや記録だけを変えたコミットの直後
- push 先と担当：

  | リモート名 | URL | push する人 |
  |---|---|---|
  | `origin`（GitHub） | `git@github.com:HORI-PNG/Wasm_lab.git` | Claude |
  | `gitblit`（Gitblit） | `https://horinouti@birdcage.mk.ics.kyutech.ac.jp/gitblit/r/~horinouti/wasm_lab.git` | **ユーザー**（Claude は push しない） |

- Claude は `git push -u origin <ブランチ名>` で、ステップのブランチ（`stepNN-内容`）だけを送る。`main` は触らないので push しない。
- Gitblit へ push するためのコマンドは、`Artifact.md` の「確認用コマンド」の最後に書いておく（ユーザーが実行する）。
- **パスワードなどの認証情報は、Claude が扱わない。** チャットで受け取らず、ファイル・コマンド・記録・コミットにも書かない。
- 強制 push（`--force`）はしない。push が拒否されたら、作業を止めてユーザーに相談する。
- push したら、`Artifact.md` に push したブランチとコミットを書く。
