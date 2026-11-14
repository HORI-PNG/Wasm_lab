# Python のライブラリは仮想環境を使う

このプロジェクトで使う Python のライブラリ（jinja2・PyYAML など）は、**プロジェクト専用の仮想環境**に入れて使う。
システム（Ubuntu）や、ホームフォルダ（`~/.local`）には入れない。

## 決まり

| 項目 | 決まり |
|---|---|
| 仮想環境の場所 | `make_wasm/TeaVM/.venv/` |
| 使うライブラリと版 | `make_wasm/TeaVM/requirements.txt` に、版を固定して書く（例：`Jinja2==3.1.2`） |
| 実行のしかた | `.venv/bin/python app/src/main/Python/JavaGenerator.py` のように、仮想環境の Python で実行する（`python3 …` で直接実行しない） |
| git | `requirements.txt` は git に入れる。`.venv/` は入れない（`.gitignore` に書く） |
| ライブラリを足す・版を変える | 計画（`next-plan/`）に書いて承認をもらってから、`requirements.txt` を直し、`.venv/bin/pip install -r requirements.txt` で入れる |

## Claude がしてはいけないこと

- `sudo apt install python3-…`、`pip install --user …`、`pip install --break-system-packages …` で、システムやホームフォルダにライブラリを入れること。
- システムに入っている Python のパッケージを、ユーザーの確認なしに削除すること（Ubuntu の他の道具が使っている場合がある）。
- 仮想環境の外の Python（`/usr/bin/python3`）で、生成器などのプロジェクトのプログラムを動かすこと。ただし、仮想環境を作るための `python3 -m venv .venv` は除く。

## 仮想環境の作り方（初めての PC で 1 回だけ）

`make_wasm/TeaVM` フォルダで実行する。

```bash
python3 -m venv .venv
```

```bash
.venv/bin/pip install -r requirements.txt
```

## 理由

- **版を固定できる**：システムのライブラリは、Ubuntu を更新すると版が変わることがある。`requirements.txt` で固定すれば、いつ動かしても同じ版で動く。
- **他の PC・他の人でも同じ状態にできる**：同じ 2 つのコマンドで、同じ版のライブラリがそろう。研究の再現性が上がる。
- **システムを汚さない**：この Ubuntu の Python は、システムへの `pip install` を禁止する設定（`EXTERNALLY-MANAGED`）になっている。仮想環境なら、システムに影響を与えずに入れたり消したりできる。
- **どのライブラリを使っているかが明確になる**：`requirements.txt` を見れば、このプロジェクトが何に依存しているか分かる。

## 例外

- skill `job-hunt-code-report` など、このプロジェクトの外の道具は、それぞれの仮想環境（例：`~/.claude/skills/job-hunt-code-report/.venv/`）を使う。このルールの対象外。
