# CLAUDE.md

> **作業を始める前に、必ず [`.rules/rule.md`](.rules/rule.md) を読んで従うこと。**
> 特に「計画 → 確認 → 実行 → 記録」の流れ：次にやることは [`next-plan/`](next-plan/) に md で書き、**ユーザーが承認するまで実行しない**。
> やったことの記録は [`logs/`](logs/)（ステップごとのフォルダに `Artifact.md`）に残す。

このフォルダ（`make_wasm/TeaVM`）は、数値計算ライブラリ **NFC（`org.mklab.nfc`）を TeaVM で WebAssembly（WasmGC）に変換し、ブラウザの JS から呼び出す**ための Gradle プロジェクト。
git リポジトリのルートは 2 つ上の `wasm_lab/`（隣に `test_wasm_app/` がある）。

## 全体の流れ

```
JS (webapp/nfc.generated.js) ──▶ NfcWasm（@JSExport の公開窓口 = mainClass）──▶ 〇〇Wasm（型変換ブリッジ）──▶ org.mklab.nfc（実際の計算）
```

- `org/mklab/nfc` は**変更しない**ライブラリ本体。`org/example` 側から import して使うだけ。
- JS が扱える型（`double[]`・`int`・`double` など）と NFC の型（`DoubleMatrix` など）の変換はブリッジが担当する。
- 行列は「行優先の 1 次元配列 + 行数 + 列数」で渡し、複数の結果は `[k, r1, c1, ..., data...]` に pack して返す（`Matrices.java` ↔ `nfc.js` の `flatten`/`unpack`）。

## ディレクトリ構成と役割

| パス | 役割 |
|---|---|
| `app/build.gradle.kts` | ビルド設定。TeaVM 0.16.0、`mainClass = "org.example.generated.NfcWasm"`（step01 から自動生成版）、出力 `nfc.wasm`。`parseJava`・`verifyJvm` タスクもここで定義 |
| `app/src/main/java/org/mklab/nfc/` | NFC ライブラリ本体（約 400 ファイル、`eig`/`leq`/`svd`/`fft`/`elf`/`matrix`/`ode` など）。**編集禁止** |
| `app/src/main/java/org/example/` | **手書き**のブリッジ。step01 から `mainClass` ではない（自動生成版との比較用、計測用関数の元として残している） |
| 　`NfcWasm.java` | 手書き版の公開窓口。計測用の `prepareMultiply` / `multiplyPrepared` もここ（自動生成版にはまだない） |
| 　`EigenWasm` / `LeqWasm` / `SvdWasm` / `FftWasm` / `ElfWasm` / `MatrixWasm` | NFC のパッケージ単位のブリッジ（型変換 → 呼び出し → pack） |
| 　`Matrices.java` | 1 次元配列 ↔ 2 次元配列の変換などの共通部品 |
| `app/src/main/java/org/example/generated/` | `JavaGenerator.py` による**自動生成**ブリッジ（`NfcWasm` / `EigWasm` / … / `Matrices`）。**手で編集しない**。**現在ビルドに使われている側**（`mainClass`） |
| `app/src/main/java/JavaParser/` | NFC のソースを解析するツール（Wasm には含まれない） |
| 　`JavaStructureAnalyzer.java` | JavaParser で `.java` の構造（クラス・メソッド等）を表示。`parseJava` タスクの main |
| 　`JavaApiJsonExporter.java` | 解析結果を JSON（`build/nfc_api.json`）に書き出す（Gson 使用） |
| `app/src/main/Python/JavaGenerator.py` | `nfc_api.json` からブリッジ・JS ラッパー・レポートを jinja2 で生成するコード生成器 |
| `app/src/main/Python/templates/` | 生成用テンプレート（`bridge.java.j2`, `nfc_wasm.java.j2`, `matrices.java.j2`, `nfc.js.j2`, `report.md.j2`） |
| `app/src/main/webapp/` | ブラウザ側。`index.html`（動作確認ページ。自動生成版を使う）、`nfc.generated.js`（自動生成ラッパー、`loadNfcGenerated()`）、`nfc.js`（手書き版用のラッパー。今の wasm では使えない） |
| `app/src/verify/java/verify/JvmReference.java` | 検算用。自動生成版の公開関数を JVM で実行して正解データ（`build/verify/jvm-reference.json`）を作り、手書き版とも比べる |
| `verify/` | 検算用。`wasm-verify.mjs`（Node で Wasm を動かし、JVM との一致と数学的な性質を確かめる）、`run.sh`（ビルドから検算までまとめて実行） |
| `.rules/rule.md` | 作業ルール（計画・確認・実行・記録の流れ、ブランチ、記録の形式、コードの書き方） |
| `next-plan/` | 次にやることの計画（`stepNN-内容.md`）。ユーザーの承認待ち。実行したら `logs/` に移す |
| `logs/` | ステップごとの記録。`logs/README.md` に一覧、`logs/stepNN-内容/Artifact.md` に各ステップの内容 |
| `build/` | Gradle の出力（git 管理外）。`nfc_api.json` と `codegen_report.md` もここに出る |
| `out/` | TeaVM 出力（war）の展開・検証用（git 管理外） |
| `WASM_EXPORT_GUIDE.md` | 入口クラスの選び方・ブリッジの書き方の手順書（手順A〜D）。生成器のロジックはこの手順に対応 |
| `NFC_CLASS_OVERVIEW.md` | `org.mklab.nfc` のパッケージ・クラス命名規則の概要 |
| `*_解説.pdf` | `JavaGenerator` / `JavaStructureAnalyzer` の解説資料 |

## よく使うコマンド（このフォルダで実行）

```bash
# Wasm をビルド（war に wasm-gc/nfc.wasm と webapp が入る）
./gradlew :app:build

# NFC の API を JSON に書き出す
./gradlew :app:parseJava --args="app/src/main/java/org/mklab/nfc --json build/nfc_api.json"

# JSON からブリッジ等を生成（jinja2 が必要）
python3 app/src/main/Python/JavaGenerator.py

# 検算（ビルド → JVM で正解データ → Node で Wasm と比較）。結果は build/verify/report.md
verify/run.sh
```

- JDK 21（`gradle.properties` で `/usr/lib/jvm/java-21-openjdk-amd64` を指定）。
- テストは `verify/run.sh`（JVM と Wasm の結果の一致、Q·R ≈ A などの数学的な性質、手書き版との一致）。Node（v26 で確認）が必要。

## コード生成のしくみ（JavaGenerator.py）

`WASM_EXPORT_GUIDE.md` の手順に対応している:

- 手順A・B `select_entry_classes()` … クラス名の末尾（`Solver` / `Decomposer` / `Analyzer` / `ExponentialMatrix`）と接頭辞（`DoubleReal` / `DoubleComplex`）で入口クラスを選ぶ
- 手順C `build_method()` … `PARAM_RULES` / `RETURN_RULES` の変換表で引数・戻り値の変換方法を決める
- 手順D `find_getters()` … 戻り値が結果の箱（`…Elements` / `…Decomposition`）なら getter を探す
- 対象外にしたメソッドとその理由は `build/codegen_report.md` に出る

## 作業ルール

**[`.rules/rule.md`](.rules/rule.md) に従う。** 要点：

- 次にやることは `next-plan/stepNN-内容.md` に計画として書き、**ユーザーの承認を得てから**実行する。方針の話（「進めたい」など）だけでは承認とみなさない。
- 1 ステップごとに `stepNN-内容` のブランチを、1 つ前のステップのブランチから作る。`main` は触らない。
- やったことは `logs/stepNN-内容/Artifact.md` に記録し、実行した計画は同じフォルダの `plan.md` に移す。
- `Artifact.md` の最後には、ユーザーが結果を自分で確かめるための**確認用コマンド**（期待する結果つき）をまとめる。
- コミットしたら、そのステップのブランチを GitHub（`origin`）と Gitblit（`gitblit`）の両方に push する（ステップ完了時・ルールや記録の変更時）。`--force` は使わない。パスワードなどの認証情報は Claude が扱わない（チャットで受け取らない・どこにも書かない）。
- ユーザーが前のステップを理解してから次に進む。終わったステップは説明し、理解できたと言われるまで次の計画の承認を求めない。

## 作業上の注意

コードの書き方は `.rules/rule.md` の「5. コードの書き方」を参照（NFC 本体は変更しない、生成物は生成器から直す、生成器を変えたら `verify/run.sh` で検算する、`var` を使わない、など）。
