# Artifact：step01-generated-verify（自動生成版への切り替えと検算）

## 1. 概要

JS に公開する入口（`mainClass`）を、手書き版から自動生成版（`org.example.generated.NfcWasm`）に切り替えた（公開関数 30 → 69）。
あわせて、自動生成版の結果が正しいかを**コマンド 1 つ（`verify/run.sh`）で確かめられる自動テスト**を作った。結果はすべて一致・OK。

- ブランチ：`step01-generated-verify`（`step00-baseline` から作成、コミット `41b5407`）
- 日付：2026-11-11 〜 2026-11-12
- 対応する資料：「P6P7_対策の検討.pdf」p.3（P6 ①）
- 承認：**事前に計画の確認を受けずに実行した**（「計画 → 確認 → 実行」のルールを決める前）。この反省から `.rules/rule.md` の「1. 作業の流れ」を決めた

## 2. タスク

- [x] `mainClass` を自動生成版に変える（`app/build.gradle.kts`）
- [x] `index.html` を自動生成版のラッパー（`nfc.generated.js`）に合わせて書き換える
- [x] ビルドが通り、Node から Wasm を呼べることを確かめる
- [x] JVM で正解データを作るプログラムを作る（`JvmReference.java`、Gradle タスク `verifyJvm`）
- [x] Node で Wasm を動かして比べるスクリプトを作る（`verify/wasm-verify.mjs`）
- [x] 数学的な性質（Q·R ≈ A など）のチェックを入れる
- [x] 手書き版と自動生成版の結果を比べる
- [x] ビルドから検算までをまとめて実行するスクリプトを作る（`verify/run.sh`）
- [x] 検証の仕組み自体が間違いを見つけられるかを確かめる（わざと値を壊す）
- [x] `CLAUDE.md` を更新する

## 3. 実装計画

### 切り替え

- `build.gradle.kts` の `mainClass` を `org.example.generated.NfcWasm` に変えるだけで、公開関数が 30 個から 69 個になる。
- 手書き版のコード（`org.example` の `NfcWasm` など）は消さずに残す。比較の基準にするため、また計測用の関数（`prepareMultiply` など）の元として後で使うため。

### 検算の方法：3 種類の確認

| 確認 | 何を確かめるか | どう確かめるか |
|---|---|---|
| ① JVM との一致 | Wasm に変換しても結果が変わっていないか | 同じ Java コードを JVM と Wasm で実行し、結果を 1 つずつ比べる |
| ② 数学的な性質 | ブリッジの変換（flatten・pack・unpack）と NFC の計算が正しいか | JS ラッパー経由で計算し、`A = Q·R` などの式が成り立つかを見る |
| ③ 手書き版との一致 | 自動生成版のブリッジが、手書き版と同じ計算をしているか | JVM 上で、同じ入力に対する両方の結果を比べる |

- ②は NFC の Javadoc に書かれた式を使った（LU：`P·A = L·U`、QR：`A = Q·R` / `A·P = Q·R`、Cholesky：`Rᵀ·R = A`、Schur：`A = U·T·Uᵀ` など）。固有ベクトルの符号や並び順のように実装で変わりうるものも、式でなら確かめられる。
- 入力は 3 種類。
  - `general`：4x4 の非対称行列（複素数の固有値を持つ）
  - `spd`：4x4 の対称正定値行列（Cholesky 分解用）
  - `rect`：5x3 と 5x2 の長方形の行列（正方行列が必要な関数は例外になる想定。例外が JVM と Wasm で同じように起きるかも確かめる）

### 仕組み

```
./gradlew :app:verifyJvm
  └ JvmReference.java：69 関数 × 3 入力を JVM で実行
      → build/verify/jvm-reference.json（引数と結果。手書き版との比較結果も含む）
node verify/wasm-verify.mjs
  └ JSON の引数をそのまま Wasm の関数に渡して比べる（①）
  └ nfc.generated.js 経由で式を確かめる（②）
      → build/verify/report.md（検算レポート）
verify/run.sh：ビルド → verifyJvm → wasm-verify.mjs をまとめて実行。1 つでも失敗すれば終了コード 1
```

- 関数ごとに引数を手で書かずに済むよう、引数の型の並びから入力を自動で決める（`double[]` の後に `int, int` が続けば行列、など）。生成される関数が増えても、テストを書き足す必要がない。
- 正解データには引数もそのまま書き出し、Wasm 側はそれを読んで呼ぶだけにした。両方で入力を作ると食い違いが起きうるため。

## 4. ウォークスルー

```bash
git switch -c step01-generated-verify

# 1. 切り替え
#   app/build.gradle.kts：mainClass = "org.example.generated.NfcWasm"
#   app/src/main/webapp/index.html：nfc.generated.js を読み込み、自動生成版の関数名で呼ぶ
./gradlew :app:build          # 成功。nfc.wasm は 174 KB → 371 KB

# 2. Node から読み込めるかの確認（行列式 43、exp の回転行列が手計算と一致）

# 3. 検算の仕組みを追加
#   app/build.gradle.kts：verify ソースセットと verifyJvm タスク
#   app/src/verify/java/verify/JvmReference.java
#   verify/wasm-verify.mjs、verify/run.sh
verify/run.sh
```

途中で直したこと：

- 手書き版との比較で、固有値（`eigenValues`）と FFT の 5 組が最初「不一致」と出た。
  調べると値ではなく**形式**の違いだった（手書き版は実部と虚部を 1 行ずつ別の行列で返し、自動生成版は 2 行の行列 1 つで返す）。
  比較を「手書き版の行列を縦に積んで比べ直す」ように直し、値が完全に一致することを確かめた。

## 5. 変更したファイル

| ファイル | 内容 |
|---|---|
| `app/build.gradle.kts` | `mainClass` を自動生成版に変更。検算用の `verify` ソースセットと `verifyJvm` タスクを追加 |
| `app/src/main/webapp/index.html` | 自動生成版のラッパーと関数名に変更 |
| `app/src/verify/java/verify/JvmReference.java` | 新規。JVM で正解データを作り、手書き版と比べる |
| `verify/wasm-verify.mjs` | 新規。Wasm と JVM の比較、数学的な性質のチェック、レポートの出力 |
| `verify/run.sh` | 新規。ビルドから検算までまとめて実行 |
| `CLAUDE.md` | `mainClass` の変更、検算の方法、ファイル構成を反映 |

## 6. 検証結果

`verify/run.sh` の結果（終了コード 0）：

| 確認 | 結果 |
|---|---|
| ① JVM との一致 | **207 回中 207 回が完全一致**（69 関数 × 3 入力。値はビット単位で同じ。例外になった 45 回も、JVM・Wasm の両方で例外） |
| ② 数学的な性質 | **25 項目すべて OK**（相対誤差はどれも 1e-15 程度。判定の基準は 1e-10） |
| ③ 手書き版との一致 | **27 組すべて一致**（値は完全に同じ。うち 5 組は形式だけが違う） |

確かめた式（②）の例：`A = Q·R`、`A·P = Q·R`、`P·A = L·U`、`Rᵀ·R = A`、`A = U·T·Uᵀ`、`A = Q·H·Qᵀ`、`A = U·D·Vᵀ`（実数・複素数、正方・長方形）、`A·v = λ·v`（実数・複素数）、`A·A⁻¹ = I`、`det(A) = 固有値の積`、`A·A⁺·A = A`、FFT と定義どおりの DFT の一致、`ifft(fft(x)) = x`、`exp([[0,1],[-1,0]])` と cos・sin の一致 など。

検証の仕組み自体の確認：

- 正解データの行列式を 1e-9 だけ変えると「不一致」になり、終了コード 1 で止まった。
- 行列の値を 1e-15 だけ変えると「誤差内で一致」と判定された。
- → 小さなずれも見逃さず、丸め誤差程度は区別して扱えている。

詳細は `build/verify/report.md`（`verify/run.sh` を実行すると作られる。git には含めない）。

未確認：ブラウザで `index.html` を開いての動作確認はしていない（Node から同じ Wasm と JS ラッパーを呼ぶ確認のみ）。

## 7. 分かったこと・残った課題

1. **Wasm への変換で結果は変わらない**：69 関数・3 入力のすべてで JVM とビット単位で一致した。
2. **例外のメッセージが失われている（⑦の問題を確認）**：例外 45 回のうち 12 回は、本来のエラーではなく `Messages` クラスの初期化の失敗（`ExceptionInInitializerError` / `NoClassDefFoundError`）になっていた。メッセージファイル（`.properties`）がないため、例外を作ろうとした時点で別の例外が起きている。例：対称でない行列での `realCholeskyDecompose`。→ step05（⑦）で対応する。
3. **今回の入力で失敗する関数がある**（NFC 側か、ブリッジ側かは未調査）：
   - `complexGeneralizedEigenSolve` / `GetEigenValue` / `GetEigenVector`、`complexQzDecompose`：正方行列の入力でも例外（`NullPointerException` やメッセージの初期化失敗）になる。
   - `complexSvdKernel`：零空間が空（フルランクの行列）のとき `NullPointerException`。`realSvdKernel` も同じ入力で例外。
   - → JVM と Wasm で同じように失敗しているので、Wasm 化の問題ではない。
4. **手書き版にしかない関数**：計測用の `multiply` / `prepareMultiply` / `multiplyPrepared` は自動生成版にない。計測を再開する前に ②（手書きの差し込み口）で自動生成版に加える必要がある。
5. **手書き版用の `nfc.js` は今の wasm では使えない**（関数名が違うため）。ファイルは残している。

## 保存（コミットと push）

| 日付 | コミット | 内容 | GitHub（origin） | Gitblit（ユーザーが push） |
|---|---|---|---|---|
| 2026-11-12 | `41b5407` | step01 の作業 | push 済み | 未（接続がタイムアウト） |
| 2026-11-12 | `fe78539` | ルールと記録の構成の整理、step02 の計画の追加 | push 済み | 未（接続がタイムアウト） |
| 2026-11-12 | この表を追記したコミット | 記録の追記 | push 済み | 未（接続がタイムアウト） |
| 2026-11-12 | `304d96b` | ルールの追加（Gitblit はユーザーが push、理解の確認の問題） | push 済み | ユーザーが push |
| 2026-11-12 | この行を追記したコミット | skill `step-quiz` の追加 | push 済み | ユーザーが push |

- GitHub には `step00-baseline` と `step01-generated-verify` の 2 本を push した。
- Gitblit は、パスワードを聞かれる前の段階で 60 秒応答がなかった。サーバーにつながっていない可能性がある（学外からの接続制限など）。
- その後ルールが変わり、Gitblit へはユーザーが push することになった（確認用コマンドの最後を参照）。

## 8. 確認用コマンド

すべて `make_wasm/TeaVM` フォルダで実行する。

**① このステップのブランチに切り替える**

```bash
git switch step01-generated-verify
```

期待する結果：`Switched to branch 'step01-generated-verify'`（すでにこのブランチにいる場合は `Already on ...`）。

**② 入口が自動生成版になっているかを見る**

```bash
grep -n "mainClass" app/build.gradle.kts
```

期待する結果：`mainClass = "org.example.generated.NfcWasm"` の行が出る。

**③ ビルドから検算までをまとめて実行する**（目安：数分）

```bash
verify/run.sh
```

期待する結果：最後の 4 行が次のようになる。

```
JVM との一致: 207 回中 完全一致 207・誤差内 0・不一致 0
数学的な性質: 25 項目中 OK 25
手書き版との比較: 27 組中 一致 27
レポート: build/verify/report.md
```

**④ ③が成功したか（終了コード）を見る**（③の直後に実行する）

```bash
echo $?
```

期待する結果：`0`（1 なら、どこかが不一致か NG）。

**⑤ 検算レポートを読む**

```bash
less build/verify/report.md
```

期待する結果：「1. JVM との一致」「2. 数学的な性質」「3. 手書き版との比較」の表が出る。関数ごとの判定や、例外になった呼び出しの一覧もここで見られる（`q` で閉じる）。

**⑥ ブラウザで動かす**（このステップでは Claude は未確認）

```bash
rm -rf out && mkdir out && unzip -q app/build/libs/app.war -d out
```

```bash
python3 -m http.server 8000 --directory out
```

期待する結果：ブラウザで `http://localhost:8000/index.html` を開くと、`eigen`・`determinant`（43）・`inverse`・`svd`・`expm` などの計算結果が JSON で表示される。終わったらターミナルで `Ctrl+C`。

**⑦ Gitblit に push する**（ユーザーが実行する。初回はパスワードを聞かれる）

```bash
git push gitblit step00-baseline step01-generated-verify
```

期待する結果：`* [new branch]  step00-baseline -> step00-baseline` などと表示される（2 回目以降は `Everything up-to-date` か、送ったコミットの範囲が表示される）。応答がないまま止まる場合は、Gitblit のサーバーにつながっていない（学外からの接続など）。
