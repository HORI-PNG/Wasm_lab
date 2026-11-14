# 計画：step03-extra-exports（手書きの関数を自動生成版に差し込む）

- 状態：**承認済み・実行済み**（2026-11-13 承認。a①・b①・c①・d①）
- 対応する資料：「P6P7_対策の検討.pdf」p.2（進める順番）・p.4（P6 ②）・p.5（P6 ③）
- ブランチ：`step03-extra-exports`（`step02-config-yaml` から作る）

## 0. 計画を書く前に分かったこと（③ 変換ルールの追加について）

資料では step03 で「③ 変換ルールを増やす」と「② 手書きの差し込み口」を一緒に行う予定だった。計画を書く前に `nfc_api.json` を調べたところ、次のことが分かった。

| 調べたこと | 結果 |
|---|---|
| 今の入口クラス 24 個で、型が原因で対象外になっているメソッド | **1 個だけ**（`DoubleRealGaussianEliminationSolver.inverse` の戻り値 `Object[]`） |
| → `DoubleNumber`・`DoubleComplexNumber`・`long` などのルールを足すと | 今の入口クラスでは、公開できる関数は**ほぼ増えない** |
| 入口を広げた場合（例：static メソッドの集まりのクラス） | `DoubleMatrixUtil`：今のルールのままで **92 個**、ルールを足すと **119 個**（static メソッド 145 個中）。ほかに `DoubleNumberUtil` 13 個、`DoubleComplexMatrixUtil` 11 個、`DoubleMatrix` の static メソッド 13〜17 個など |

つまり、③ は「変換ルールを足す」だけでは効果がなく、**「どのクラスを入口にするか」を広げることとセット**で考える必要がある。入口の広げ方は、P7（名前ではなく型で入口を選ぶ）とも関係する大きめの作業になる。

そこで、step03 は **② 手書きの差し込み口だけ**にし、③ は入口の広げ方とあわせて **step04** で計画し直すことを提案する（「5. 決めてほしいこと」の a）。② を先にする理由は、研究の計測（WebGPU と Wasm の速さ比べ）に必要な関数が、今の wasm から消えているため。

## 1. 目的

手書き版にしかない計測用の関数（`multiply`・`prepareMultiply`・`multiplyPrepared`）を、自動生成版の公開窓口（`org.example.generated.NfcWasm`）からも呼べるようにする。

- step01 で `mainClass` を自動生成版に切り替えたため、これらは今の `nfc.wasm` から呼べなくなっている（step01 の記録「残った課題 4」）。
- 自動生成のルールでは作れない関数（Java 側に状態を持つ `prepareMultiply` など）を、**設定ファイルで指定して生成物に差し込む**仕組みを作る。資料 p.4 の「手書きのブリッジを残し、設定で『これも公開する』と指定する（`extra_bridges`）」にあたる。

## 2. やること

### 2-1. 設定ファイルに「差し込む関数」の一覧を書けるようにする

`codegen/nfc.yaml` に次の項目を足す（名前は案）。

```yaml
# 自動生成のルールでは作れない関数を、手書きのブリッジから公開する
extra_exports:
  - name: "multiply"                          # JS から呼ぶ名前
    call: "org.example.MatrixWasm.multiply"   # 呼び出す手書きのメソッド
    params: ["double[] a", "int aRows", "int aColumns", "double[] b", "int bColumns"]
    returns: "double[]"
  - name: "prepareMultiply"
    call: "org.example.MatrixWasm.prepareMultiply"
    params: ["double[] a", "int aRows", "int aColumns", "double[] b", "int bColumns"]
    returns: "void"
  - name: "multiplyPrepared"
    call: "org.example.MatrixWasm.multiplyPrepared"
    params: []
    returns: "double"
```

- 生成器（`JavaGenerator.py`）は、この一覧を読み、生成する `NfcWasm.java` に `@JSExport` 付きの「手書きのメソッドを呼ぶだけ」のメソッドを足す（テンプレート `nfc_wasm.java.j2` を変える）。
- 設定の確認（`validate_config()`）に、`extra_exports` の項目の確認を足す。
- 公開名が、自動生成の関数の名前と重なったら、分かりやすいエラーで止まるようにする。

### 2-2. 手書きのブリッジを、別のパッケージから呼べるようにする

`org.example.MatrixWasm` は今、同じパッケージの中からしか呼べない（`public` が付いていない）。生成された `org.example.generated.NfcWasm` から呼べるように、`MatrixWasm` のクラスと 3 つのメソッドに `public` を付ける。中身（計算）は変えない。

### 2-3. 検算を足す

- `JvmReference.java` は、公開関数を自動で見つけるので、差し込んだ 3 つも自動で 207 回の比較に加わる（69 個 → 72 個、207 回 → 216 回）。
  - ただし、名前順に呼ぶと `multiplyPrepared` が `prepareMultiply` より先に呼ばれ、行列が用意されていないので例外になる（JVM・Wasm とも）。比較としては「両方とも例外」で一致するが、意味のある確認にならない。
- そこで `wasm-verify.mjs` の「数学的な性質」に、次の確認を足す。
  - `multiply` の答えが、JS で計算した行列の積と一致する（長方形の行列も）
  - `prepareMultiply` → `multiplyPrepared` の順に呼ぶと、積の「先頭の成分 ＋ 最後の成分」が返る（手書き版の仕様どおり）
- `JvmReference.java` の「手書き版との比較」にも `multiply` を足し、手書き版の窓口と同じ値になることを確かめる。

### 2-4. 確認と記録

1. 生成器を再生成し、**今までの 69 個の関数の生成物は変わらず、3 個が足されただけ**であることを `git diff` で確かめる。
2. `verify-tools/run.sh` で検算する（JVM との一致・数学的な性質・手書き版との一致がすべて OK）。
3. `logs/step03-extra-exports/Artifact.md` を書き、この計画を `plan.md` として移す。`CLAUDE.md` を更新する。
4. ブランチ `step03-extra-exports` でコミットし、GitHub に push する。

## 3. やらないこと

- ③ 変換ルールの追加と、入口クラスの広げ方 → step04 で計画し直す（a で ① を選んだ場合）
- 公開名の書き換え（`rename`）・引数の既定値（`defaults`）→ 必要が出てから（「5. 決めてほしいこと」の c）
- JS ラッパー（`nfc.generated.js`）に、差し込んだ関数の「2 次元配列で呼べる版」を作ること → b を参照
- 手書き版の他の関数（`eigen` など）の差し込み → 自動生成版に同じ計算があるので不要

## 4. 確認方法（「できた」の基準）

- [ ] 生成された `NfcWasm.java` に `multiply`・`prepareMultiply`・`multiplyPrepared` があり、Node から `nfc.wasm` の関数として呼べる
- [ ] 今までの 69 個の関数の生成物に変化がない（足されたのは 3 個の関数だけ）
- [ ] `verify-tools/run.sh` がすべて OK（新しく足す性質の確認も含む）
- [ ] `extra_exports` の書き間違い（項目の不足・名前の重なり）で、分かりやすいエラーで止まる

## 5. 決めてほしいこと

| # | 内容 | 選択肢 | おすすめ |
|---|---|---|---|
| a | step03 の範囲 | ① ② 手書きの差し込み口だけにし、③ は入口の広げ方とあわせて step04 で計画し直す ② 資料どおり ③ も一緒に行う（変換ルールを足すだけ） | **①**：「0.」のとおり、ルールを足すだけでは公開できる関数がほぼ増えない。計測に必要な ② を先に終わらせる |
| b | 差し込んだ関数の JS からの呼び方 | ① `nfc.exports.multiply(1 次元配列, 行, 列, …)` のように、wasm の関数を直接呼ぶ ② JS ラッパーにも「2 次元配列で呼べる版」を生成する | **①**：計測では受け渡しの時間を細かく扱うので、余計な変換をしない直接呼び出しの方が都合がよい。手書き版のときも、JS ラッパー（`nfc.js`）には `multiply` がなかった |
| c | `rename`（公開名の書き換え）・`defaults`（引数の既定値）の枠 | ① 今回も作らない ② 今回作る | **①**：今のところ使う予定がない。必要になったときに中身と一緒に作る |
| d | Jinja2 が使う MarkupSafe の版の固定（step02 の残った課題） | ① `requirements.txt` に `MarkupSafe==3.0.4` を足す ② 足さない | **①**：1 行で済み、他の PC でも完全に同じ版になる |
