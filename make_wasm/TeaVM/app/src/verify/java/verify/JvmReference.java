package verify;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 自動生成版の公開関数（{@code org.example.generated.NfcWasm}）を JVM で実行し、正解データを JSON に書き出すプログラムです。
 *
 * <p>Wasm 側（{@code verify-tools/wasm-verify.mjs}）は、この JSON に書かれた引数をそのまま Wasm の関数に渡し、
 * 結果がここで記録した値と一致するかを調べます。
 * 同じ Java コードを JVM と Wasm で動かして比べるので、「Wasm への変換で結果が変わっていないか」を確かめられます。</p>
 *
 * <p>あわせて、手書き版（{@code org.example.NfcWasm}）と自動生成版で、同じ計算の結果が一致するかも JVM 上で比べます。</p>
 *
 * <p>実行方法：{@code ./gradlew :app:verifyJvm}（出力先は引数で指定。既定は build/verify/jvm-reference.json）</p>
 */
public final class JvmReference {

    /** 許容誤差など、double 1 つの引数に渡す値 */
    private static final double TOLERANCE = 1e-12;
    /** 反復回数など、行列・ベクトルの長さではない int の引数に渡す値 */
    private static final int PLAIN_INT = 50;
    /** 「ほぼ一致」とみなす相対誤差 */
    private static final double CLOSE = 1e-12;

    private JvmReference() {
    }

    /** テストの入力 1 組。関数の行列の引数には matrices を前から順に、ベクトルの引数には vectors を前から順に渡す。 */
    private static final class TestCase {
        final String name;
        final String description;
        final double[][][] matrices;
        final double[][] vectors;

        TestCase(String name, String description, double[][][] matrices, double[][] vectors) {
            this.name = name;
            this.description = description;
            this.matrices = matrices;
            this.vectors = vectors;
        }
    }

    /** 関数 1 回分の引数。invokeArgs は Java の呼び出し用、jsonArgs は JSON に書く用（同じ値）。 */
    private static final class Arguments {
        final List<Object> invokeArgs = new ArrayList<>();
        final List<Object> jsonArgs = new ArrayList<>();
    }

    // ---- テストの入力 ----

    /** 一般的な 4x4 の非対称行列（複素数の固有値を持つ） */
    private static final double[][] GENERAL = {
        {4, -2, 1, 0.5},
        {3, 1, -1, 2},
        {0.5, 2, 3, -1},
        {1, 0, 2, 5},
    };
    private static final double[][] SECOND = {
        {1, 0.5, -0.3, 0.2},
        {0.4, 2, 0.1, -0.5},
        {-0.2, 0.3, 1.5, 0.6},
        {0.7, -0.1, 0.2, 3},
    };
    private static final double[][] THIRD = {
        {2, 1, 0, 0.3},
        {1, 3, 0.5, 0},
        {0, 0.5, 4, 1},
        {0.3, 0, 1, 2},
    };
    private static final double[][] FOURTH = {
        {0.5, -1, 0.2, 0},
        {0.1, 0.4, -0.3, 1},
        {1, 0, 0.6, -0.2},
        {-0.4, 0.2, 0, 0.9},
    };
    /** 対称正定値行列（Cholesky 分解用） */
    private static final double[][] SPD = {
        {4, 1, 0.5, 0.2},
        {1, 5, 1, 0.3},
        {0.5, 1, 6, 1},
        {0.2, 0.3, 1, 3},
    };
    /** 長方形の行列（5x3 と 5x2） */
    private static final double[][] RECT_A = {
        {1, 2, 0.5},
        {-1, 0.3, 2},
        {0.7, 1, -0.4},
        {2, -0.5, 1},
        {0.2, 0.8, 0.9},
    };
    private static final double[][] RECT_A2 = {
        {0.3, -0.2, 0.1},
        {0.5, 0.4, -0.6},
        {-0.1, 0.9, 0.2},
        {0.6, 0, 0.3},
        {-0.7, 0.2, 0.4},
    };
    private static final double[][] RECT_B = {
        {1, 0},
        {0, 1},
        {2, -1},
        {0.5, 0.5},
        {-1, 2},
    };
    private static final double[][] RECT_B2 = {
        {0.2, 0.1},
        {-0.3, 0.4},
        {0.5, 0},
        {0, -0.2},
        {0.1, 0.3},
    };
    private static final double[] VECTOR_RE = {1, 2, 3, 4, 0, -1, -2, -3};
    private static final double[] VECTOR_IM = {0.5, 0, -0.5, 1, 0, 2, 0, -1};

    private static List<TestCase> testCases() {
        List<TestCase> cases = new ArrayList<>();
        cases.add(new TestCase("general", "4x4 の非対称行列（複素固有値あり）",
                new double[][][] {GENERAL, SECOND, THIRD, FOURTH}, new double[][] {VECTOR_RE, VECTOR_IM}));
        cases.add(new TestCase("spd", "4x4 の対称正定値行列",
                new double[][][] {SPD, THIRD, SECOND, FOURTH}, new double[][] {VECTOR_IM, VECTOR_RE}));
        cases.add(new TestCase("rect", "5x3 と 5x2 の長方形の行列（正方行列が必要な関数は例外になる想定）",
                new double[][][] {RECT_A, RECT_A2, RECT_B, RECT_B2}, new double[][] {VECTOR_RE, VECTOR_IM}));
        return cases;
    }

    // ---- main ----

    public static void main(String[] args) throws Exception {
        Path output = Paths.get(args.length > 0 ? args[0] : "build/verify/jvm-reference.json");
        Path parent = output.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        Class<?> generated = Class.forName("org.example.generated.NfcWasm");
        List<Method> methods = exportedMethods(generated);
        List<TestCase> cases = testCases();

        int callCount = 0;
        int errorCount = 0;
        try (Writer out = Files.newBufferedWriter(output, StandardCharsets.UTF_8);
             JsonWriter json = new JsonWriter(out)) {
            json.setIndent(" ");
            json.beginObject();
            json.name("generatedClass").value(generated.getName());
            json.name("tolerance").value(TOLERANCE);

            json.name("cases").beginArray();
            for (TestCase testCase : cases) {
                json.beginObject();
                json.name("name").value(testCase.name);
                json.name("description").value(testCase.description);
                json.endObject();
            }
            json.endArray();

            // 関数ごと・入力ごとに実行して記録する（Wasm 側はこの順番どおりに呼ぶ）
            json.name("calls").beginArray();
            for (Method method : methods) {
                for (TestCase testCase : cases) {
                    Arguments arguments = buildArguments(method, testCase);
                    json.beginObject();
                    json.name("export").value(method.getName());
                    json.name("case").value(testCase.name);
                    json.name("returnType").value(method.getReturnType().getSimpleName());
                    json.name("args");
                    writeValue(json, arguments.jsonArgs);
                    try {
                        Object result = method.invoke(null, arguments.invokeArgs.toArray());
                        if (method.getReturnType() == void.class) {
                            json.name("outcome").value("void");
                        } else {
                            json.name("outcome").value("value");
                            json.name("result");
                            writeValue(json, result);
                        }
                    } catch (InvocationTargetException e) {
                        Throwable cause = e.getCause();
                        json.name("outcome").value("error");
                        json.name("error").value(cause.getClass().getName() + ": " + cause.getMessage());
                        errorCount++;
                    }
                    json.endObject();
                    callCount++;
                }
            }
            json.endArray();

            // 手書き版と自動生成版の比較
            json.name("handwritten").beginArray();
            List<String> summary = compareHandwritten(json);
            json.endArray();
            json.endObject();

            System.out.println("自動生成版の公開関数: " + methods.size() + " 個");
            System.out.println("実行した呼び出し: " + callCount + " 回（うち例外: " + errorCount + " 回）");
            System.out.println("手書き版との比較:");
            for (String line : summary) {
                System.out.println("  " + line);
            }
            System.out.println("書き出し先: " + output);
        }
    }

    /** main 以外の public static メソッドを、名前の順に並べて返す（JSON の順番を毎回同じにするため）。 */
    private static List<Method> exportedMethods(Class<?> type) {
        List<Method> methods = new ArrayList<>();
        for (Method method : type.getDeclaredMethods()) {
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && !method.getName().equals("main")) {
                methods.add(method);
            }
        }
        methods.sort(Comparator.comparing(Method::getName));
        return methods;
    }

    /**
     * 引数の型の並びから、渡す値を決める。
     *
     * <ul>
     *   <li>{@code double[]} の後に {@code int, int} が続く → 行列（中身・行数・列数）。matrices から順に使う</li>
     *   <li>それ以外の {@code double[]} → ベクトル。vectors から順に使う</li>
     *   <li>ベクトルの直後の {@code int} → ベクトルの長さ（FFT の size）</li>
     *   <li>それ以外の {@code int} → {@link #PLAIN_INT}、{@code double} → {@link #TOLERANCE}、{@code boolean} → false</li>
     * </ul>
     */
    private static Arguments buildArguments(Method method, TestCase testCase) {
        Class<?>[] types = method.getParameterTypes();
        Arguments arguments = new Arguments();
        int matrixIndex = 0;
        int vectorIndex = 0;
        int lastVectorLength = -1;
        int i = 0;
        while (i < types.length) {
            Class<?> type = types[i];
            boolean isMatrix = type == double[].class && i + 2 < types.length
                    && types[i + 1] == int.class && types[i + 2] == int.class;
            if (isMatrix) {
                double[][] matrix = testCase.matrices[matrixIndex++];
                double[] values = flatten(matrix);
                int rows = matrix.length;
                int columns = matrix[0].length;
                add(arguments, values);
                add(arguments, rows);
                add(arguments, columns);
                i += 3;
                lastVectorLength = -1;
            } else if (type == double[].class) {
                double[] vector = testCase.vectors[vectorIndex++].clone();
                add(arguments, vector);
                lastVectorLength = vector.length;
                i++;
            } else if (type == int.class) {
                add(arguments, lastVectorLength >= 0 ? lastVectorLength : PLAIN_INT);
                lastVectorLength = -1;
                i++;
            } else if (type == double.class) {
                add(arguments, TOLERANCE);
                i++;
            } else if (type == boolean.class) {
                add(arguments, false);
                i++;
            } else {
                throw new IllegalStateException("想定していない引数の型: " + type + "（" + method.getName() + "）");
            }
        }
        return arguments;
    }

    private static void add(Arguments arguments, Object value) {
        arguments.invokeArgs.add(value);
        if (value instanceof double[]) {
            // 呼び出し先が配列を書き換えても JSON の値が変わらないように、写しを JSON 用に持つ
            arguments.jsonArgs.add(((double[]) value).clone());
        } else {
            arguments.jsonArgs.add(value);
        }
    }

    private static double[] flatten(double[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        double[] values = new double[rows * columns];
        for (int row = 0; row < rows; row++) {
            System.arraycopy(matrix[row], 0, values, row * columns, columns);
        }
        return values;
    }

    // ---- JSON への書き出し ----

    /** 数値・真偽値・配列・リストを JSON に書く。NaN と無限大は JSON の数値で書けないので文字列にする。 */
    private static void writeValue(JsonWriter json, Object value) throws IOException {
        if (value == null) {
            json.nullValue();
        } else if (value instanceof double[]) {
            json.beginArray();
            for (double d : (double[]) value) {
                writeNumber(json, d);
            }
            json.endArray();
        } else if (value instanceof List<?>) {
            json.beginArray();
            for (Object element : (List<?>) value) {
                writeValue(json, element);
            }
            json.endArray();
        } else if (value instanceof Double) {
            writeNumber(json, (Double) value);
        } else if (value instanceof Integer) {
            json.value((Integer) value);
        } else if (value instanceof Boolean) {
            json.value((Boolean) value);
        } else {
            throw new IllegalStateException("JSON に書けない値: " + value.getClass());
        }
    }

    private static void writeNumber(JsonWriter json, double d) throws IOException {
        if (Double.isFinite(d)) {
            json.value(d);
        } else {
            json.value(Double.toString(d));
        }
    }

    // ---- 手書き版との比較 ----

    /** 引数なしで呼べる計算（手書き版・自動生成版のそれぞれ 1 回分） */
    private interface Call {
        Object call() throws Exception;
    }

    /**
     * 手書き版と自動生成版で、同じ計算の結果を比べる。
     *
     * <p>pack した配列どうしは、行列の並び順が違うことがある（例：qz）。そこで、手書き版の各行列について、
     * 自動生成版の中に同じ大きさで値が一致する行列があるかを探す。</p>
     */
    private static List<String> compareHandwritten(JsonWriter json) throws IOException {
        List<String> summary = new ArrayList<>();
        double[] a = flatten(GENERAL);
        double[] b = flatten(SECOND);
        double[] s = flatten(SPD);
        double[] rect = flatten(RECT_A);
        double[] rectB = flatten(RECT_B);
        int n = 4;
        double tol = TOLERANCE;

        List<Object[]> pairs = new ArrayList<>();
        pairs.add(pair("eigen", () -> org.example.NfcWasm.eigen(a.clone(), n),
                "realEigenSolve", () -> org.example.generated.NfcWasm.realEigenSolve(a.clone(), n, n)));
        pairs.add(pair("eigenValues", () -> org.example.NfcWasm.eigenValues(a.clone(), n),
                "realEigenGetEigenValue", () -> org.example.generated.NfcWasm.realEigenGetEigenValue(a.clone(), n, n)));
        pairs.add(pair("generalizedEigen", () -> org.example.NfcWasm.generalizedEigen(a.clone(), b.clone(), n),
                "realGeneralizedEigenSolve", () -> org.example.generated.NfcWasm.realGeneralizedEigenSolve(a.clone(), n, n, b.clone(), n, n)));
        pairs.add(pair("hessenberg", () -> org.example.NfcWasm.hessenberg(a.clone(), n),
                "realHessenbergDecompose", () -> org.example.generated.NfcWasm.realHessenbergDecompose(a.clone(), n, n)));
        pairs.add(pair("schur", () -> org.example.NfcWasm.schur(a.clone(), n),
                "realSchurDecompose", () -> org.example.generated.NfcWasm.realSchurDecompose(a.clone(), n, n)));
        pairs.add(pair("qr", () -> org.example.NfcWasm.qr(a.clone(), n, n),
                "realQrDecompose", () -> org.example.generated.NfcWasm.realQrDecompose(a.clone(), n, n)));
        pairs.add(pair("qrWithPermutation", () -> org.example.NfcWasm.qrWithPermutation(a.clone(), n, n),
                "realQrDecomposeWithPermutation", () -> org.example.generated.NfcWasm.realQrDecomposeWithPermutation(a.clone(), n, n)));
        pairs.add(pair("qz", () -> org.example.NfcWasm.qz(a.clone(), b.clone(), n),
                "realQzDecompose", () -> org.example.generated.NfcWasm.realQzDecompose(a.clone(), n, n, b.clone(), n, n)));
        pairs.add(pair("balance", () -> org.example.NfcWasm.balance(a.clone(), n),
                "realBalanceDecompose", () -> org.example.generated.NfcWasm.realBalanceDecompose(a.clone(), n, n)));
        pairs.add(pair("lu", () -> org.example.NfcWasm.lu(a.clone(), n, tol),
                "realLuDecompose", () -> org.example.generated.NfcWasm.realLuDecompose(a.clone(), n, n, tol, false)));
        pairs.add(pair("luWithPermutation", () -> org.example.NfcWasm.luWithPermutation(a.clone(), n, tol),
                "realLuDecomposeWithPermutation", () -> org.example.generated.NfcWasm.realLuDecomposeWithPermutation(a.clone(), n, n, tol, false)));
        pairs.add(pair("inverse", () -> org.example.NfcWasm.inverse(a.clone(), n, tol),
                "realLuInverse", () -> org.example.generated.NfcWasm.realLuInverse(a.clone(), n, n, tol, false)));
        pairs.add(pair("solve", () -> org.example.NfcWasm.solve(a.clone(), n, b.clone(), n, tol),
                "realLuLeftDivide", () -> org.example.generated.NfcWasm.realLuLeftDivide(a.clone(), n, n, b.clone(), n, n, tol, false)));
        pairs.add(pair("determinant", () -> org.example.NfcWasm.determinant(a.clone(), n, tol),
                "realLuGetDeterminant", () -> org.example.generated.NfcWasm.realLuGetDeterminant(a.clone(), n, n, tol, false)));
        pairs.add(pair("svd", () -> org.example.NfcWasm.svd(rect.clone(), 5, 3),
                "realSvdDecompose", () -> org.example.generated.NfcWasm.realSvdDecompose(rect.clone(), 5, 3)));
        pairs.add(pair("singularValues", () -> org.example.NfcWasm.singularValues(rect.clone(), 5, 3),
                "realSvdSingularValue", () -> org.example.generated.NfcWasm.realSvdSingularValue(rect.clone(), 5, 3)));
        pairs.add(pair("rank", () -> org.example.NfcWasm.rank(a.clone(), n, n, tol),
                "realSvdRank", () -> org.example.generated.NfcWasm.realSvdRank(a.clone(), n, n, tol)));
        pairs.add(pair("norm", () -> org.example.NfcWasm.norm(a.clone(), n, n),
                "realSvdNorm", () -> org.example.generated.NfcWasm.realSvdNorm(a.clone(), n, n)));
        pairs.add(pair("conditionNumber", () -> org.example.NfcWasm.conditionNumber(a.clone(), n, n),
                "realSvdConditionNumber", () -> org.example.generated.NfcWasm.realSvdConditionNumber(a.clone(), n, n)));
        pairs.add(pair("pseudoInverse", () -> org.example.NfcWasm.pseudoInverse(rect.clone(), 5, 3, tol),
                "realSvdPseudoInverse", () -> org.example.generated.NfcWasm.realSvdPseudoInverse(rect.clone(), 5, 3, tol)));
        pairs.add(pair("leastSquare", () -> org.example.NfcWasm.leastSquare(rect.clone(), 5, 3, rectB.clone(), 2, tol),
                "realSvdLeastSquare", () -> org.example.generated.NfcWasm.realSvdLeastSquare(rect.clone(), 5, 3, rectB.clone(), 5, 2, tol)));
        pairs.add(pair("kernel", () -> org.example.NfcWasm.kernel(rect.clone(), 5, 3, tol),
                "realSvdKernel", () -> org.example.generated.NfcWasm.realSvdKernel(rect.clone(), 5, 3, tol)));
        pairs.add(pair("fft", () -> org.example.NfcWasm.fft(VECTOR_RE.clone(), 8),
                "realFft", () -> org.example.generated.NfcWasm.realFft(VECTOR_RE.clone(), 8)));
        pairs.add(pair("ifft", () -> org.example.NfcWasm.ifft(VECTOR_RE.clone(), 8),
                "realFftIfft", () -> org.example.generated.NfcWasm.realFftIfft(VECTOR_RE.clone(), 8)));
        pairs.add(pair("complexFft", () -> org.example.NfcWasm.complexFft(VECTOR_RE.clone(), VECTOR_IM.clone(), 8),
                "complexFft", () -> org.example.generated.NfcWasm.complexFft(VECTOR_RE.clone(), VECTOR_IM.clone(), 8)));
        pairs.add(pair("complexIfft", () -> org.example.NfcWasm.complexIfft(VECTOR_RE.clone(), VECTOR_IM.clone(), 8),
                "complexFftIfft", () -> org.example.generated.NfcWasm.complexFftIfft(VECTOR_RE.clone(), VECTOR_IM.clone(), 8)));
        pairs.add(pair("expm", () -> org.example.NfcWasm.expm(s.clone(), n),
                "realExp", () -> org.example.generated.NfcWasm.realExp(s.clone(), n, n)));

        for (Object[] p : pairs) {
            String handName = (String) p[0];
            String genName = (String) p[2];
            String verdict;
            String detail;
            try {
                Object hand = ((Call) p[1]).call();
                Object gen = ((Call) p[3]).call();
                String[] result = compare(hand, gen);
                verdict = result[0];
                detail = result[1];
            } catch (Exception e) {
                verdict = "error";
                detail = e.getClass().getName() + ": " + e.getMessage();
            }
            json.beginObject();
            json.name("handwritten").value(handName);
            json.name("generated").value(genName);
            json.name("verdict").value(verdict);
            json.name("detail").value(detail);
            json.endObject();
            summary.add(String.format("%-18s ⇔ %-32s %s（%s）", handName, genName, verdict, detail));
        }
        return summary;
    }

    private static Object[] pair(String handName, Call hand, String genName, Call gen) {
        return new Object[] {handName, hand, genName, gen};
    }

    /** 結果を比べ、{判定, 説明} を返す。判定は same（完全一致）/ close（誤差内で一致）/ differ（不一致）。 */
    private static String[] compare(Object hand, Object gen) {
        if (hand instanceof double[] && gen instanceof double[]) {
            List<double[][]> handMatrices = unpack((double[]) hand);
            List<double[][]> genMatrices = unpack((double[]) gen);
            boolean[] used = new boolean[genMatrices.size()];
            int matched = 0;
            boolean allExact = true;
            double maxDiff = 0;
            for (double[][] h : handMatrices) {
                int found = -1;
                double foundDiff = Double.POSITIVE_INFINITY;
                for (int j = 0; j < genMatrices.size(); j++) {
                    if (used[j]) {
                        continue;
                    }
                    double diff = relativeDiff(h, genMatrices.get(j));
                    if (diff < foundDiff) {
                        foundDiff = diff;
                        found = j;
                    }
                }
                if (found >= 0 && foundDiff <= CLOSE) {
                    used[found] = true;
                    matched++;
                    maxDiff = Math.max(maxDiff, foundDiff);
                    if (foundDiff != 0) {
                        allExact = false;
                    }
                }
            }
            String detail = "手書き " + handMatrices.size() + " 行列 / 自動生成 " + genMatrices.size()
                    + " 行列のうち " + matched + " 行列が一致、最大相対誤差 " + maxDiff;
            if (matched < handMatrices.size()) {
                // 形式だけが違う場合：手書き版は 1 行ずつ別の行列（例：実部の行と虚部の行）で返し、
                // 自動生成版は元の double[][] をそのまま 1 つの行列で返すことがある。手書き版の行列を縦に積んで比べ直す
                double[][] stacked = stackRows(handMatrices);
                if (stacked != null && genMatrices.size() == 1) {
                    double diff = relativeDiff(stacked, genMatrices.get(0));
                    if (diff <= CLOSE) {
                        String formatNote = "値は一致（形式だけが違う：手書きは " + handMatrices.size()
                                + " 個の行列、自動生成はそれを縦に積んだ 1 個の行列）、最大相対誤差 " + diff;
                        return new String[] {diff == 0 ? "same" : "close", formatNote};
                    }
                }
                return new String[] {"differ", detail};
            }
            return new String[] {allExact ? "same" : "close", detail};
        }
        if (hand instanceof Number && gen instanceof Number) {
            double h = ((Number) hand).doubleValue();
            double g = ((Number) gen).doubleValue();
            double diff = Math.abs(h - g) / Math.max(1, Math.max(Math.abs(h), Math.abs(g)));
            String detail = "手書き " + h + " / 自動生成 " + g;
            if (h == g) {
                return new String[] {"same", detail};
            }
            return new String[] {diff <= CLOSE ? "close" : "differ", detail};
        }
        return new String[] {"differ", "戻り値の種類が違う: " + describe(hand) + " / " + describe(gen)};
    }

    private static String describe(Object value) {
        return value == null ? "null" : value.getClass().getSimpleName();
    }

    /** 列数がそろっていれば、行列を縦に積んだ 1 つの行列を返す。そろっていなければ null。 */
    private static double[][] stackRows(List<double[][]> matrices) {
        List<double[]> rows = new ArrayList<>();
        int columns = -1;
        for (double[][] matrix : matrices) {
            for (double[] row : matrix) {
                if (columns >= 0 && row.length != columns) {
                    return null;
                }
                columns = row.length;
                rows.add(row);
            }
        }
        return rows.toArray(new double[0][]);
    }

    /** 大きさが違えば無限大。同じなら「差の最大値 / max(1, 値の最大値)」。 */
    private static double relativeDiff(double[][] x, double[][] y) {
        if (x.length != y.length) {
            return Double.POSITIVE_INFINITY;
        }
        double maxDiff = 0;
        double maxAbs = 1;
        for (int i = 0; i < x.length; i++) {
            if (x[i].length != y[i].length) {
                return Double.POSITIVE_INFINITY;
            }
            for (int j = 0; j < x[i].length; j++) {
                maxDiff = Math.max(maxDiff, Math.abs(x[i][j] - y[i][j]));
                maxAbs = Math.max(maxAbs, Math.max(Math.abs(x[i][j]), Math.abs(y[i][j])));
            }
        }
        return maxDiff / maxAbs;
    }

    /** Matrices.pack の形式 [k, r1, c1, ..., data...] を行列のリストに戻す。 */
    private static List<double[][]> unpack(double[] packed) {
        List<double[][]> matrices = new ArrayList<>();
        int count = (int) packed[0];
        int offset = 1 + count * 2;
        for (int k = 0; k < count; k++) {
            int rows = (int) packed[1 + k * 2];
            int columns = (int) packed[2 + k * 2];
            double[][] matrix = new double[rows][];
            for (int r = 0; r < rows; r++) {
                matrix[r] = Arrays.copyOfRange(packed, offset, offset + columns);
                offset += columns;
            }
            matrices.add(matrix);
        }
        return matrices;
    }
}
