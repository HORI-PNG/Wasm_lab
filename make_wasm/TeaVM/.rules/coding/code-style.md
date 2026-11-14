# コードの書き方

- `org/mklab/nfc`（NFC 本体）は変更しない。必要な処理は `org/example` 側に書く。
- `org/example/generated/` と `webapp/nfc.generated.js` は生成物。直したいときは生成器（`JavaGenerator.py`）かテンプレートを直して再生成する。
- 生成器を変えたら、`verify-tools/run.sh` で検算する。
- Java コードは `var` を使わず明示的な型で書く。コメント・ドキュメントは日本語。
- Python のライブラリは、プロジェクトの仮想環境に入れて使う（[`python-venv.md`](python-venv.md)）。
