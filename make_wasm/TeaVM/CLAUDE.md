# CLAUDE.md

このフォルダ（`make_wasm/TeaVM`）は、数値計算ライブラリ **NFC（`org.mklab.nfc`）を TeaVM で WebAssembly（WasmGC）に変換し、ブラウザの JS から呼び出す**ための Gradle プロジェクト。
git リポジトリのルートは 2 つ上の `wasm_lab/`（隣に `test_wasm_app/` がある）。

## 全体の流れ

```
JS (webapp/nfc.js) ──▶ NfcWasm（@JSExport の公開窓口 = mainClass）──▶ 〇〇Wasm（型変換ブリッジ）──▶ org.mklab.nfc（実際の計算）
```

- `org/mklab/nfc` は**変更しない**ライブラリ本体。`org/example` 側から import して使うだけ。
- JS が扱える型（`double[]`・`int`・`double` など）と NFC の型（`DoubleMatrix` など）の変換はブリッジが担当する。
- 行列は「行優先の 1 次元配列 + 行数 + 列数」で渡し、複数の結果は `[k, r1, c1, ..., data...]` に pack して返す（`Matrices.java` ↔ `nfc.js` の `flatten`/`unpack`）。

## ディレクトリ構成と役割

| パス | 役割 |
|---|---|
| `app/build.gradle.kts` | ビルド設定。TeaVM 0.16.0、`mainClass = "org.example.NfcWasm"`、出力 `nfc.wasm`。`parseJava` タスクもここで定義 |
| `app/src/main/java/org/mklab/nfc/` | NFC ライブラリ本体（約 400 ファイル、`eig`/`leq`/`svd`/`fft`/`elf`/`matrix`/`ode` など）。**編集禁止** |
| `app/src/main/java/org/example/` | **手書き**のブリッジ。現在ビルドに使われている側 |
| 　`NfcWasm.java` | JS への公開窓口（`mainClass`）。`@JSExport` はここに集める。計測用の `prepareMultiply` / `multiplyPrepared` もここ |
| 　`EigenWasm` / `LeqWasm` / `SvdWasm` / `FftWasm` / `ElfWasm` / `MatrixWasm` | NFC のパッケージ単位のブリッジ（型変換 → 呼び出し → pack） |
| 　`Matrices.java` | 1 次元配列 ↔ 2 次元配列の変換などの共通部品 |
| `app/src/main/java/org/example/generated/` | `JavaGenerator.py` による**自動生成**ブリッジ（`NfcWasm` / `EigWasm` / … / `Matrices`）。**手で編集しない**。現状 `mainClass` には指定されていない |
| `app/src/main/java/JavaParser/` | NFC のソースを解析するツール（Wasm には含まれない） |
| 　`JavaStructureAnalyzer.java` | JavaParser で `.java` の構造（クラス・メソッド等）を表示。`parseJava` タスクの main |
| 　`JavaApiJsonExporter.java` | 解析結果を JSON（`build/nfc_api.json`）に書き出す（Gson 使用） |
| `app/src/main/Python/JavaGenerator.py` | `nfc_api.json` からブリッジ・JS ラッパー・レポートを jinja2 で生成するコード生成器 |
| `app/src/main/Python/templates/` | 生成用テンプレート（`bridge.java.j2`, `nfc_wasm.java.j2`, `matrices.java.j2`, `nfc.js.j2`, `report.md.j2`） |
| `app/src/main/webapp/` | ブラウザ側。`index.html`（動作確認ページ）、`nfc.js`（手書きラッパー、`loadNfc()`）、`nfc.generated.js`（自動生成ラッパー） |
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
```

- JDK 21（`gradle.properties` で `/usr/lib/jvm/java-21-openjdk-amd64` を指定）。
- テストコードは現状なし。

## コード生成のしくみ（JavaGenerator.py）

`WASM_EXPORT_GUIDE.md` の手順に対応している:

- 手順A・B `select_entry_classes()` … クラス名の末尾（`Solver` / `Decomposer` / `Analyzer` / `ExponentialMatrix`）と接頭辞（`DoubleReal` / `DoubleComplex`）で入口クラスを選ぶ
- 手順C `build_method()` … `PARAM_RULES` / `RETURN_RULES` の変換表で引数・戻り値の変換方法を決める
- 手順D `find_getters()` … 戻り値が結果の箱（`…Elements` / `…Decomposition`）なら getter を探す
- 対象外にしたメソッドとその理由は `build/codegen_report.md` に出る

## 作業ルール（ブランチと作業ログ）

詳細は `docs/worklog/README.md`。

- 作業は「P6P7_対策の検討.pdf」の順番で、1 ステップごとに `stepNN-内容` という名前のブランチを、1 つ前のステップのブランチから作って進める。終わったブランチは残す。`main` は作業前の状態のまま触らない。
- 1 ステップごとに `docs/worklog/stepNN-内容.md` を書く（概要・タスク・計画・実施内容・変更したファイル・検証結果・残った課題）。作業の途中でも更新する。
- push は指示があるまでしない。

## 作業上の注意

- `org/mklab/nfc` は変更しない。必要な処理は `org/example` 側に書く。
- `org/example/generated/` と `webapp/nfc.generated.js` は生成物。変更したいときはテンプレートか `JavaGenerator.py` を直して再生成する。
- 新しい関数を JS に公開するときは、ブリッジに実装し、`mainClass`（`org.example.NfcWasm`）に `@JSExport` 付き static メソッドを追加し、必要なら `nfc.js` にラッパーを足す。
- Java コードは `var` を使わず明示的な型で書く。コメント・ドキュメントは日本語。
