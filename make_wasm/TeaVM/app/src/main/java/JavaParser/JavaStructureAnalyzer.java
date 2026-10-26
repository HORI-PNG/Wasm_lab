package JavaParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;

/**
 * JavaParser を使って、指定フォルダ以下の .java ファイルの構造（形式）を出力するツールです。
 *
 * <p>各ファイルについて、パッケージ・import・型宣言（class / interface / enum / record / annotation）、
 * 継承関係、フィールド、コンストラクタ、メソッドのシグネチャを表示します。入れ子の型も再帰的に表示します。</p>
 *
 * <p>使い方: {@code ./gradlew :app:parseJava --args="<フォルダ>"}（フォルダ省略時はカレントディレクトリ）</p>
 *
 * <p>{@code --json <出力ファイル>} を付けると、テキスト表示の代わりに public な API を JSON で書き出します
 * （{@link JavaApiJsonExporter}）。</p>
 */
public final class JavaStructureAnalyzer {

    private static final String INDENT = "  ";

    private final com.github.javaparser.JavaParser parser;

    public JavaStructureAnalyzer() {
        ParserConfiguration config = new ParserConfiguration()
                .setLanguageLevel(LanguageLevel.JAVA_21);
        this.parser = new com.github.javaparser.JavaParser(config);
    }

    public static void main(String[] args) throws IOException {
        String folder = ".";
        Path jsonOutput = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--json")) {
                if (i + 1 >= args.length) {
                    System.err.println("--json の後ろに出力ファイルを指定してください");
                    System.exit(1);
                }
                jsonOutput = Paths.get(args[++i]);
            } else {
                folder = args[i];
            }
        }

        Path root = Paths.get(folder).toAbsolutePath().normalize();
        if (!Files.isDirectory(root)) {
            System.err.println("フォルダが見つかりません: " + root);
            System.exit(1);
        }

        JavaStructureAnalyzer analyzer = new JavaStructureAnalyzer();
        if (jsonOutput != null) {
            analyzer.exportJson(root, jsonOutput);
        } else {
            analyzer.analyzeDirectory(root);
        }
    }

    /** フォルダ以下の .java ファイルの public な API を JSON に書き出します。 */
    public void exportJson(Path root, Path output) throws IOException {
        List<Path> files = listJavaFiles(root);
        JavaApiJsonExporter exporter = new JavaApiJsonExporter(root);
        int errors = 0;

        for (Path file : files) {
            ParseResult<CompilationUnit> result = parser.parse(file);
            if (!result.isSuccessful() || result.getResult().isEmpty()) {
                errors++;
                System.err.println("[解析エラー] " + root.relativize(file));
                for (Problem problem : result.getProblems()) {
                    System.err.println(INDENT + problem.getVerboseMessage());
                }
                continue;
            }
            exporter.add(file, result.getResult().get());
        }

        exporter.write(output);
        System.out.println("Java ファイル数: " + files.size() + "（解析エラー " + errors + " 件）");
        System.out.println("出力した型の数: " + exporter.classCount());
        System.out.println("出力先: " + output.toAbsolutePath().normalize());
    }

    /** フォルダ以下の .java ファイルを、パスの順に並べて返します。 */
    private static List<Path> listJavaFiles(Path root) throws IOException {
        try (Stream<Path> walk = Files.walk(root)) {
            return walk.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .sorted()
                    .collect(Collectors.toList());
        }
    }

    /** フォルダ以下の .java ファイルをすべて解析して出力します。 */
    public void analyzeDirectory(Path root) throws IOException {
        List<Path> files = listJavaFiles(root);

        System.out.println("対象フォルダ: " + root);
        System.out.println("Java ファイル数: " + files.size());

        for (Path file : files) {
            System.out.println();
            System.out.println("==== " + root.relativize(file) + " ====");
            analyzeFile(file);
        }
    }

    /** 1 ファイルを解析して出力します。 */
    public void analyzeFile(Path file) throws IOException {
        ParseResult<CompilationUnit> result = parser.parse(file);
        if (!result.isSuccessful() || result.getResult().isEmpty()) {
            System.out.println("[解析エラー]");
            for (Problem problem : result.getProblems()) {
                System.out.println(INDENT + problem.getVerboseMessage());
            }
            return;
        }
        CompilationUnit cu = result.getResult().get();

        System.out.println("package: " + cu.getPackageDeclaration()
                .map(pd -> pd.getNameAsString())
                .orElse("(default)"));

        if (!cu.getImports().isEmpty()) {
            System.out.println("imports:");
            for (ImportDeclaration imp : cu.getImports()) {
                System.out.println(INDENT + (imp.isStatic() ? "static " : "")
                        + imp.getNameAsString() + (imp.isAsterisk() ? ".*" : ""));
            }
        }

        for (TypeDeclaration<?> type : cu.getTypes()) {
            printType(type, 0);
        }
    }

    private void printType(TypeDeclaration<?> type, int depth) {
        String pad = INDENT.repeat(depth);
        StringBuilder header = new StringBuilder(pad);
        appendAnnotations(header, type.getAnnotations());
        appendModifiers(header, type.getModifiers());
        header.append(kindOf(type)).append(' ').append(type.getNameAsString());

        if (type instanceof ClassOrInterfaceDeclaration c) {
            appendTypeParameters(header, c.getTypeParameters());
            appendTypeList(header, " extends ", c.getExtendedTypes());
            appendTypeList(header, " implements ", c.getImplementedTypes());
            appendTypeList(header, " permits ", c.getPermittedTypes());
        } else if (type instanceof RecordDeclaration r) {
            appendTypeParameters(header, r.getTypeParameters());
            header.append('(').append(joinParameters(r.getParameters())).append(')');
            appendTypeList(header, " implements ", r.getImplementedTypes());
        } else if (type instanceof EnumDeclaration e) {
            appendTypeList(header, " implements ", e.getImplementedTypes());
        }
        System.out.println(header);

        String inner = INDENT.repeat(depth + 1);

        if (type instanceof EnumDeclaration e && !e.getEntries().isEmpty()) {
            System.out.println(inner + "[enum constants] " + e.getEntries().stream()
                    .map(EnumConstantDeclaration::getNameAsString)
                    .collect(Collectors.joining(", ")));
        }

        for (BodyDeclaration<?> member : type.getMembers()) {
            if (member instanceof FieldDeclaration f) {
                for (VariableDeclarator v : f.getVariables()) {
                    StringBuilder sb = new StringBuilder(inner).append("[field] ");
                    appendAnnotations(sb, f.getAnnotations());
                    appendModifiers(sb, f.getModifiers());
                    sb.append(v.getTypeAsString()).append(' ').append(v.getNameAsString());
                    System.out.println(sb);
                }
            } else if (member instanceof ConstructorDeclaration c) {
                StringBuilder sb = new StringBuilder(inner).append("[constructor] ");
                appendAnnotations(sb, c.getAnnotations());
                appendModifiers(sb, c.getModifiers());
                appendTypeParameters(sb, c.getTypeParameters());
                sb.append(c.getNameAsString())
                        .append('(').append(joinParameters(c.getParameters())).append(')');
                appendThrows(sb, c.getThrownExceptions());
                System.out.println(sb);
            } else if (member instanceof MethodDeclaration m) {
                StringBuilder sb = new StringBuilder(inner).append("[method] ");
                appendAnnotations(sb, m.getAnnotations());
                appendModifiers(sb, m.getModifiers());
                if (!m.getTypeParameters().isEmpty()) {
                    appendTypeParameters(sb, m.getTypeParameters());
                    sb.append(' ');
                }
                sb.append(m.getTypeAsString()).append(' ').append(m.getNameAsString())
                        .append('(').append(joinParameters(m.getParameters())).append(')');
                appendThrows(sb, m.getThrownExceptions());
                System.out.println(sb);
            } else if (member instanceof AnnotationMemberDeclaration a) {
                StringBuilder sb = new StringBuilder(inner).append("[element] ");
                sb.append(a.getTypeAsString()).append(' ').append(a.getNameAsString()).append("()");
                a.getDefaultValue().ifPresent(d -> sb.append(" default ").append(d));
                System.out.println(sb);
            } else if (member instanceof TypeDeclaration<?> nested) {
                printType(nested, depth + 1);
            }
        }
    }

    private static String kindOf(TypeDeclaration<?> type) {
        if (type instanceof ClassOrInterfaceDeclaration c) {
            return c.isInterface() ? "interface" : "class";
        } else if (type instanceof EnumDeclaration) {
            return "enum";
        } else if (type instanceof RecordDeclaration) {
            return "record";
        } else if (type instanceof AnnotationDeclaration) {
            return "@interface";
        }
        return "type";
    }

    private static void appendAnnotations(StringBuilder sb, NodeList<AnnotationExpr> annotations) {
        for (AnnotationExpr a : annotations) {
            sb.append(a).append(' ');
        }
    }

    private static void appendModifiers(StringBuilder sb, NodeList<Modifier> modifiers) {
        for (Modifier m : modifiers) {
            sb.append(m.getKeyword().asString()).append(' ');
        }
    }

    private static void appendTypeParameters(StringBuilder sb, NodeList<TypeParameter> params) {
        if (!params.isEmpty()) {
            sb.append('<').append(params.stream()
                    .map(TypeParameter::toString)
                    .collect(Collectors.joining(", "))).append('>');
        }
    }

    private static void appendTypeList(StringBuilder sb, String keyword, NodeList<ClassOrInterfaceType> types) {
        if (!types.isEmpty()) {
            sb.append(keyword).append(types.stream()
                    .map(ClassOrInterfaceType::asString)
                    .collect(Collectors.joining(", ")));
        }
    }

    private static void appendThrows(StringBuilder sb, NodeList<? extends com.github.javaparser.ast.type.ReferenceType> types) {
        if (!types.isEmpty()) {
            sb.append(" throws ").append(types.stream()
                    .map(t -> t.asString())
                    .collect(Collectors.joining(", ")));
        }
    }

    private static String joinParameters(NodeList<Parameter> params) {
        return params.stream()
                .map(p -> p.getTypeAsString() + (p.isVarArgs() ? "..." : "") + " " + p.getNameAsString())
                .collect(Collectors.joining(", "));
    }
}
