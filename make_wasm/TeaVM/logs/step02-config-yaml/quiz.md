# 理解の確認：step02-config-yaml

- 対象：`logs/step02-config-yaml/Artifact.md`
- 出題日：2026-11-13
- 結果：**合格（2026-11-13）**（1 回目：正解 5・一部正解 2・不正解 1 → 出し直しを 2 回行い、全問正解）

## 問題 1（A. 何をしたか）

step02 では、`JavaGenerator.py` から何を取り出して、どこに移しましたか。
また、その結果、**生成されるコード**（`org/example/generated/` の Java や `nfc.generated.js`）はどう変わりましたか。

- 回答：定数を取り出して、YAMLファイルに移した。生成されるコードに変わりはなく、その仕組みのみを変更した
- 判定：正解
- 解説：補足：取り出したのは、NFC に合わせた値（入口クラスの末尾・先頭、型の変換ルール、名前の言い換え、出力先）で、移した先は `codegen/nfc.yaml` です。生成物が変わらないことは、変更の前後で再生成して `git diff` に差分がないこと、`verify-tools/run.sh` が step01 と同じ結果になることで確かめました。

## 問題 2（B. 技術的な質問）

次の (a)〜(e) を、「`codegen/nfc.yaml` に書くもの」と「`JavaGenerator.py` に残すもの」に分けてください。
また、どういう基準で分けたのかを 1 文で答えてください。

- (a) 入口クラスとみなすクラス名の末尾 `Solver`
- (b) 抽象クラスとジェネリックなクラスは入口にしない
- (c) 戻り値が `DoubleMatrix` なら `Matrices.pack(result.getDoubleElements())` で返す
- (d) getter は「`get` で始まり、引数がない」メソッドとして探す
- (e) クラス名の中の `SingularValue` を、公開名では `Svd` と短くする

- 回答：(a)...yaml, 定数だから。(b)...Python, 分離する仕組みだから。(c)...yaml, 定数を含んだ定義だから。(d)...Python, 仕組みに関する内容だから。(e)...yaml, 定数を含んだ定義だから
- 判定：正解
- 解説：分け方はすべて合っています（YAML：a・c・e、Python：b・d）。「定数か、仕組みか」という基準も、step02 の「値は YAML、手順はコード」と同じ考え方です。もう一歩踏み込むと、基準は「**他のライブラリに使うとき、変える必要があるか**」です。`Solver` や `SingularValue` は NFC の名前の付け方なので変わりえますが、「抽象クラスは呼べない」「getter は get で始まる」は Java 全体の決まりなので、どのライブラリでも同じです。

## 問題 3（B. 技術的な質問）

step02 では、生成器を書き換える**前に**、まず「書き換える前の生成器を仮想環境で動かして、生成物に差分が出ないこと」を確かめました。
書き換える前にこれを確かめておくのはなぜですか。もし、この時点で差分が出ていたら、何が分からなくなりますか。

- 回答：今まで仮想環境を使わずに実行していたので、仮想環境をオンにしたことにより生成物に違いが出ると、対照実験（違いがYAMLのせいか仮想環境のせいかわからなくなる）でなくなるから
- 判定：正解
- 解説：「仮想環境に変えた影響」と「YAML に分けた影響」を切り分けるため、で合っています（対照実験という言い方はぴったりです）。補足すると、もしこの時点で差分が出ていたら、リポジトリの生成物と生成器がそもそもずれている（誰かが生成物を手で直した、生成器を変えたのに再生成していない、など）可能性もあります。その場合は、step02 の作業を止めて原因を調べる、と計画に書いていました。

## 問題 4（B. 技術的な質問）

`activate` をしていないのに、`.venv/bin/python app/src/main/Python/JavaGenerator.py` と実行すると、仮想環境のライブラリが使われます。
(1) なぜ `activate` なしで仮想環境が使われるのですか。
(2) 同じフォルダで `python3 app/src/main/Python/JavaGenerator.py` と実行すると、どの Python とどのライブラリが使われますか。

- 回答：(1)どこのPythonを動かすかを明記しているから、問題ない。(2)ubuntu全体に入っているライブラリ
- 判定：正解
- 解説：(1) 仮想環境の Python を直接指定しているから、で合っています。仕組みとしては、`.venv/bin/python` は起動時に `.venv/pyvenv.cfg` を読み、自分が仮想環境の Python だと分かるので、ライブラリを `.venv/` の中から読み込みます。`activate` は「`python` と打ったときにどの Python を選ぶか（PATH）」を切り替えるだけです。(2) システムの Python（`/usr/bin/python3`）と、Ubuntu 全体に入っているライブラリ（`/usr/lib/python3/dist-packages/`）が使われる、で合っています。

## 問題 5（B. 技術的な質問）

`requirements.txt` は git に入れ、`.venv/` フォルダは git に入れない（`.gitignore` に書いた）ことにしました。
それぞれ、なぜそうするのですか。

- 回答：.venv/はファイル数が多い + どのバージョンの何を入れればいいかをrequirement.txtに書いておけば、クローンしても同じ環境で動かせるから。githubには.venv/やnode_moduleのようなライブラリはpushしないのが通例
- 判定：正解
- 解説：合っています。補足すると、`.venv/` を git に入れない理由はもう 1 つあります。`.venv/` の中には、作った PC のフォルダの場所（絶対パス）や、その PC の OS・Python 用のファイルが入っているので、別の PC にそのまま写しても動かないことが多いのです。だから「何を入れるか」だけを `requirements.txt` で渡し、各 PC で作り直します。`node_modules` を push しないのと同じ考え方です。

## 問題 6（C. 実行内容）

ある人が、別の PC で次のコマンドを実行したところ、下のように表示されました。

```bash
.venv/bin/python -c "import yaml; print(yaml.__file__)"
```

```
Traceback (most recent call last):
  File "<string>", line 1, in <module>
ModuleNotFoundError: No module named 'yaml'
```

(1) これは何が起きていることを表していますか。
(2) 次にどのコマンドを実行すれば直りますか。

- 回答：(1)...PyYAMLが入ってない。(2)pip install requirement.txt を実行する
- 判定：一部正解
- 解説：(1) 仮想環境に PyYAML が入っていない、で合っています（`.venv/` は作ったが、ライブラリを入れる手順を飛ばした状態）。(2) のコマンドが違います。正しくは `.venv/bin/pip install -r requirements.txt` です。`-r` を付けないと「requirements.txt という名前のライブラリ」を探してしまいます。また、ただの `pip` はシステムの pip を指すことがあり、この Ubuntu ではシステムへの install が禁止されています（`EXTERNALLY-MANAGED`）。仮想環境の pip（`.venv/bin/pip`）を使います。

## 問題 7（C. 実行内容）

`codegen/nfc.yaml` の `types.params` に新しい型のルールを足し、`java` と `arg` は書いたものの、`js` を書き忘れました。
この状態で `.venv/bin/python app/src/main/Python/JavaGenerator.py` を実行すると、
(1) 画面に何が表示されますか（だいたいの内容で構いません）。
(2) `org/example/generated/` の生成物はどうなりますか。

- 回答：？
- 判定：不正解
- 解説：生成器は、生成物を書き出す**前に**設定ファイルを確かめます（`load_config()` の中の `validate_config()`）。(1) 画面には「設定ファイルに問題がある: codegen/nfc.yaml」と「項目 types.params.<型の名前>.js がない」のように、**どこが足りないか**が表示され、終了コード 1 で止まります。(2) 確かめた時点で止まるので、生成物は**書き換えられず、前のまま**です（step02 で、わざと設定を壊して 6 通り試し、確かめました）。

## 問題 8（分かった課題・次とのつながり）

次の step03 では、変換ルールを増やします（例：`DoubleNumber` を JS の数 1 つとして受け渡す）。
step02 が終わった今、`DoubleNumber` のルールを足すには、**どのファイルを変え、そのあと何をして、どうやって結果が正しいことを確かめる**ことになりますか。順番に書いてください。

- 回答：YAMLにDoubleNumber変数を追加し、Parseを実行し（JavaとPythonの２つ）、JvmReferenceとwasm-verifyを実行し、確認する。
- 判定：一部正解
- 解説：「YAML に足す → 検算（JvmReference と wasm-verify）で確かめる」の流れは合っています（検算は `verify-tools/run.sh` 1 つで、JvmReference と wasm-verify の両方が動きます）。ただし「Parse を実行」は要りません。`./gradlew :app:parseJava` は NFC のソースを解析して `nfc_api.json` を作るもので、NFC のソースは変わっていないので作り直す必要がありません。必要なのは**生成器を動かして再生成する**こと（`.venv/bin/python app/src/main/Python/JavaGenerator.py`）です。また、再生成した後に `build/codegen_report.md` で公開数が増えたか（対象外が減ったか）も確かめます。

## まとめ

| 元の問題 | 内容 | 最終的に正解した問題 |
|---|---|---|
| 1 | YAML に移したものと、生成物が変わらないこと | 1 |
| 2 | YAML に書くもの・コードに残すものの分け方 | 2 |
| 3 | 書き換える前に基準を取る理由 | 3 |
| 4 | activate なしで仮想環境が使われる理由 | 4 |
| 5 | `requirements.txt` と `.venv/` の扱い | 5 |
| 6 | 仮想環境にライブラリを入れるコマンド | 6'（`.venv/bin/pip install -r requirements.txt`） |
| 7 | 設定に問題があるときの動き | 7''（確認が先・書き出しが後なので、生成物は前のまま） |
| 8 | step03 でルールを足すときの流れ | 8'（YAML → 再生成 → 検算 → レポート。parseJava は不要） |

つまずいた点：仮想環境に入れる正しいコマンド（`-r` と `.venv/bin/pip`）、生成器の処理の順番（設定の確認 → 生成 → 書き出し）、parseJava（NFC の解析）と生成器の実行の区別。

## 出し直した問題

### 問題 6'（問題 6 の出し直し）

別の PC で `.venv/` を作った直後に、ライブラリを入れようとしています。次の (a)〜(c) のうち正しいコマンドはどれですか。また、残りの 2 つがだめな理由をそれぞれ答えてください。

- (a) `pip install requirements.txt`
- (b) `.venv/bin/pip install -r requirements.txt`
- (c) `sudo apt install python3-yaml python3-jinja2`

- 回答：b。(a)...pipがPythonのpipをさすかどうかが分からない、(c)...requirementに入っているバージョンではない可能性がある
- 判定：正解
- 解説：(b) で合っています。(a) の理由（どの Python の pip か分からない）も、(c) の理由（`requirements.txt` の版になるとは限らない）も正しいです。補足すると、(a) は `-r` がないので「requirements.txt という名前のライブラリ」を探してしまう、という問題もあります。(c) はシステム全体に入れることになり、「Python のライブラリは仮想環境に入れる」というルールにも反します。

### 問題 7'（問題 7 の出し直し）

生成器を実行したら、次のように表示されて止まりました。

```
設定ファイルに問題がある: codegen/nfc.yaml
  - 項目 types.params.DoubleNumber.js がない
```

(1) `codegen/nfc.yaml` のどこを、どう直せばよいですか。
(2) このとき、`org/example/generated/` の生成物は書き換わっていますか。理由もあわせて答えてください。

- 回答：types.params.DoubleNumberのjsをplainにすう。生成物は変わらない。これはPython実行時にエントリーポイントが作成されたものだから、nfcが変わって実行しない限り変わらない
- 判定：一部正解
- 解説：(1) `types.params.DoubleNumber` に `js: "plain"` を足す、で合っています（数 1 つとして受け渡すので plain）。(2)「生成物は変わらない」も合っています。ただし理由が違います。「NFC が変わらない限り生成物は変わらない」わけではありません。生成物は、生成器が**最後まで動けば**、設定ファイル（YAML）の内容によって変わります（step03 で YAML にルールを足せば、NFC が同じでも生成物は増えます）。今回変わらなかったのは、生成器が**最初に設定ファイルを確かめ、問題を見つけた時点で、生成物を書き出す前に止まる**からです。

### 問題 8'（問題 8 の出し直し）

`DoubleNumber` の変換ルールを足すときの作業の候補が A〜E です。**必要なものだけを、正しい順番に**並べてください。また、必要ないものはなぜ必要ないかを答えてください。

- A. `./gradlew :app:parseJava --args="app/src/main/java/org/mklab/nfc --json build/nfc_api.json"`
- B. `codegen/nfc.yaml` の `types.params`（と `types.returns`）に `DoubleNumber` のルールを書く
- C. `.venv/bin/python app/src/main/Python/JavaGenerator.py`
- D. `verify-tools/run.sh`
- E. `build/codegen_report.md` を見て、公開したメソッドの数が増えたかを確かめる

- 回答：Aはいらない。nfc自体は変わっていないから。B->C->D->E
- 判定：正解
- 解説：B → C → D → E で合っています（E は C の直後に見てもよい）。A が要らない理由（NFC のソースが変わっていないので、`nfc_api.json` を作り直す必要がない）も正しいです。

### 問題 7''（問題 7' の出し直し）

(1) `codegen/nfc.yaml` に `DoubleNumber` のルールを**正しく**（`java`・`arg`・`js` をすべて）書いて、生成器を実行しました。NFC のソースはまったく変えていません。このとき、`org/example/generated/` の生成物は変わることがありますか。理由も答えてください。

(2) 一方、`js` を書き忘れたまま実行したときは、生成物は前のままでした。生成器の中で、**どの処理が、どのタイミングで動く**ので、前のままになるのですか。

- 回答：ある。もともとDoubleNumberのルールがなかったら、今回初めてそのエントリーポイントが作られる。生成物作成が、実行の最後のタイミングで動くので、generatedは変わらない
- 判定：正解
- 解説：(1) 変わる。ルールが足されると、今まで「型を変換できない」として対象外だったメソッドが生成されるようになる、で合っています。(2)「書き出しが最後に動く」も合っています。もう一言足すと、**最初に `load_config()`（その中の `validate_config()`）が設定ファイルを確かめ、問題があればその場で止まる**ので、最後の書き出し（`render_all()`）まで進まない、という順番です。
