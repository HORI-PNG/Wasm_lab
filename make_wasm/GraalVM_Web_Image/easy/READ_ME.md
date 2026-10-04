# GraalVM Web Image の使用法

公式ドキュメント（ https://www.graalvm.org/jdk25/reference-manual/web-image/ ）を参考に、GraalVM Web Image の使用法を知る

また、GraalVM のインストールは、公式ドキュメント( https://docs.oracle.com/en/graalvm/jdk/25/docs/getting-started/linux/ ) を参考に

## 事前準備

### Oracle GraalVM （25.1以降）のインストール

まずはインストール状況の確認

```bash
java -version
native-image --version
```

入っていなかったら、**GraalVM**のインストールが必要

```bash
mkdir -p ~/graalvm && cd ~/graalvm
wget https://download.oracle.com/graalvm/25/latest/graalvm-jdk-25_linux-x64_bin.tar.gz
```

展開する

```bash
tar -xzf graalvm-jdk-25_linux-x64_bin.tar.gz
ls
```

環境変数を設定する。ここに掲載するのは、現在開いているターミナルでのみ有効にするものであるので、ターミナルを開きなおすたびに打ち直す必要がある。（ずっとGraalVMにする方法もあるけど、まだいいかなと）

```bash
export JAVA_HOME=$HOME/graalvm/graalvm-jdk-25.0.4+7.1
export PATH=$JAVA_HOME/bin:$PATH
```

再度バージョン確認

```bash
java -version
```

```
nn081@horiyuuPC:~/graalvm$ java -version
native-image --version
java version "25.0.4" 2026-07-21 LTS
Java(TM) SE Runtime Environment Oracle GraalVM 25.0.4+7.1 (build 25.0.4+7-LTS-jvmci-b01)
Java HotSpot(TM) 64-Bit Server VM Oracle GraalVM 25.0.4+7.1 (build 25.0.4+7-LTS-jvmci-b01, mixed mode, sharing)
native-image 25.0.4 2026-07-21
GraalVM Runtime Environment Oracle GraalVM 25.0.4+7.1 (build 25.0.4+7-LTS-jvmci-b01)
Substrate VM Oracle GraalVM 25.0.4+7.1 (build 25.0.4+7-LTS, serial gc, compressed references)
```

こんな感じになればOK。

Node.js の確認

```bash
node --version
```

私の場合はバージョンが古かったので、LTS(になる予定)の Node.js 26 に更新

```bash
nvm install 26
nvm use 26
node --version
```

ここで私の場合は、インストールしたはずなのにバージョンが古いままだったので、原因を探した

```bash
which -a node
type node
nvm current
```

その結果、私の場合は Volta というパッケージ管理ツールを使っていたことが発覚。これを消して、nvm に移行する。nvm の方が主流だから。ここは面倒なので、AI に聞いて都度対処する

移行の確認とバージョンの変更

```bash
which -a node
nvm alias default 26
node --version
nvm use 26
node --version
```

GrallVMを再設定

先ほど nmv に移行した際、ターミナルを新しく開いたので、再設定

```bash
export JAVA_HOME=$HOME/graalvm/graalvm-jdk-25.0.4+7.1
export PATH=$JAVA_HOME/bin:$PATH
native-image --version
```

native-image の確認

```bash
native-image --version
```

### Binaryenのインストール

macOSなら `brew install binaryen` 、他のOSならGitHubからダウンロードする。私はUbuntuなのでGitHubから
（ https://releasealert.dev/github/WebAssembly/binaryen ）

```bash
mkdir -p ~/binaryen && cd ~/binaryen
wget https://github.com/WebAssembly/binaryen/releases/download/version_129/binaryen-version_129-x86_64-linux.tar.gz
tar -xzf binaryen-version_129-x86_64-linux.tar.gz
ls
```

現在のターミナルだけ Binaryen を有効にする

```bash
export PATH=$HOME/binaryen/binaryen-version_129/bin:$PATH
wasm-as --version
```

**これでやっと事前準備が完了**

#

## Javaファイルの作成

### 1. `.java`ファイルを作成する

```java
 public class HelloWasm {
     public static int add(int a, int b) {
         return a + b;
     }

     public static void main(String[] args) {
         System.out.println(add(3, 4));
     }
 }
```

### 2. コンパイルする

```bash
javac HelloWasm.java
```

`java HelloWasm` で `7` が表示されればOK

### 3. wasmにビルドする

**GraalVM** を設定したターミナルで実行

```bash
native-image --tool:svm-wasm HelloWasm
```

ビルドが終わると、`hellowasm.js` と `hellowasm.wasm` が生成される

### 4. Node.jsで実行

生成された `hellowasm.js` をNode.jsで実行

```bash
node hellowasm.js
```

```bash
$ node hellowasm.js
7
```

と表示されればOK。

**これでおしまい！あとは Web Image API を使う手順。**

## 最後に

新しいターミナルで開始するときは、

```bash
export JAVA_HOME=$HOME/graalvm/graalvm-jdk-25.0.4+7.1
export PATH=$JAVA_HOME/bin:$HOME/binaryen/binaryen-version_129/bin:$PATH
```

の設定をする
