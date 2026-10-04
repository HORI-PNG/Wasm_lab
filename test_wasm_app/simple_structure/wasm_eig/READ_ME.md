## 実行時の手順

### TypeScriptのインストール

tsをjsに変換する必要がある。

プロジェクトのルートに移動して、

```bash
npm init -y
npm install -D typescript
```

を実行して、`typeScript`をインポートする。
そのうえでコンパイルをする。

### コンパイル

```bash
npx tsc app/main.ts --target es2020 --module es2020
```

2020は適当な数字なので、特に意味はない。

### サーバー

コンパイルが終わったら、サーバーを立ち上げる。（TeaVMでPythonなどを使ってと書かれていたので、以下はPythonの操作）

```python
python3 -m http.server 8080
```

_おしまい！_
