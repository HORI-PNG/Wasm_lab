package JavaParser;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.TypeParameter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 解析した .java ファイルから「外部から使える API（public な型・コンストラクタ・メソッド）」を集め、
 * JSON として書き出します。
 *
 * <p>この JSON は、Python（jinja2）で Wasm 用のブリッジコードを生成するときの入力になります。
 * 入れ子の型は {@code "Outer.Inner"} という名前で、トップレベルと同じ {@code classes} 配列に並べます。</p>
 */
public final class JavaApiJsonExporter {

    /** JSON 全体。 */
    record ApiJson(String root, List<ClassInfo> classes) {
    }

    /** 型 1 つ分。 */
    record ClassInfo(
            String packageName,
            String name,
            String qualifiedName,
            String file,
            String kind,
            List<String> modifiers,
            List<String> annotations,
            List<String> typeParameters,
            List<String> extendsTypes,
            List<String> implementsTypes,
            List<String> imports,
            List<String> enumConstants,
            List<ConstructorInfo> constructors,
            List<MethodInfo> methods) {
    }

    /** コンストラクタ。implicit = ソースに書かれていない（デフォルトコンストラクタなど）。 */
    record ConstructorInfo(
            List<String> modifiers,
            List<ParameterInfo> parameters,
            List<String> throwsTypes,
            boolean implicit) {
    }

    /** メソッド。 */
    record MethodInfo(
            String name,
            List<String> modifiers,
            List<String> annotations,
            boolean isStatic,
            List<String> typeParameters,
            String returnType,
            List<ParameterInfo> parameters,
            List<String> throwsTypes) {
    }

    /** 引数。可変長引数なら varArgs = true（type は要素の型）。 */
    record ParameterInfo(String type, String name, boolean varArgs) {
    }

    // JSON のキー名を schema（package / extends / throws など）に合わせるため、Gson の名前変換を使う
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .setFieldNamingStrategy(field -> switch (field.getName()) {
                case "packageName" -> "package";
                case "extendsTypes" -> "extends";
                case "implementsTypes" -> "implements";
                case "throwsTypes" -> "throws";
                case "isStatic" -> "static";
                default -> field.getName();
            })
            .create();

    private final Path root;
    private final List<ClassInfo> classes = new ArrayList<>();

    public JavaApiJsonExporter(Path root) {
        this.root = root;
    }

    /** 1 ファイル分の解析結果を取り込みます。 */
    public void add(Path file, CompilationUnit cu) {
        String packageName = cu.getPackageDeclaration().map(pd -> pd.getNameAsString()).orElse("");
        List<String> imports = cu.getImports().stream()
                .map(JavaApiJsonExporter::importString)
                .collect(Collectors.toList());
        String relativeFile = root.relativize(file).toString().replace('\\', '/');

        for (TypeDeclaration<?> type : cu.getTypes()) {
            addType(type, packageName, "", relativeFile, imports);
        }
    }

    /** 集めた内容を JSON ファイルに書き出します。 */
    public void write(Path output) throws IOException {
        Path parent = output.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (Writer writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8)) {
            GSON.toJson(new ApiJson(root.toString(), classes), writer);
        }
    }

    public int classCount() {
        return classes.size();
    }

    private void addType(TypeDeclaration<?> type, String packageName, String outerName,
            String file, List<String> imports) {
        if (!isVisible(type)) {
            return;
        }
        String name = outerName.isEmpty() ? type.getNameAsString() : outerName + "." + type.getNameAsString();
        String qualifiedName = packageName.isEmpty() ? name : packageName + "." + name;

        List<String> typeParameters = List.of();
        List<String> extendsTypes = List.of();
        List<String> implementsTypes = List.of();
        List<String> enumConstants = List.of();

        if (type instanceof ClassOrInterfaceDeclaration c) {
            typeParameters = typeParameterStrings(c.getTypeParameters());
            extendsTypes = typeStrings(c.getExtendedTypes());
            implementsTypes = typeStrings(c.getImplementedTypes());
        } else if (type instanceof RecordDeclaration r) {
            typeParameters = typeParameterStrings(r.getTypeParameters());
            implementsTypes = typeStrings(r.getImplementedTypes());
        } else if (type instanceof EnumDeclaration e) {
            implementsTypes = typeStrings(e.getImplementedTypes());
            enumConstants = e.getEntries().stream()
                    .map(EnumConstantDeclaration::getNameAsString)
                    .collect(Collectors.toList());
        }

        List<ConstructorInfo> constructors = new ArrayList<>();
        List<MethodInfo> methods = new ArrayList<>();

        for (BodyDeclaration<?> member : type.getMembers()) {
            if (member instanceof ConstructorDeclaration c && c.isPublic()) {
                constructors.add(new ConstructorInfo(
                        modifierStrings(c.getModifiers()),
                        parameterInfos(c.getParameters()),
                        throwsStrings(c.getThrownExceptions()),
                        false));
            } else if (member instanceof MethodDeclaration m && isPublicMember(type, m.getModifiers())) {
                methods.add(new MethodInfo(
                        m.getNameAsString(),
                        modifierStrings(m.getModifiers()),
                        annotationStrings(m.getAnnotations()),
                        m.isStatic(),
                        typeParameterStrings(m.getTypeParameters()),
                        m.getTypeAsString(),
                        parameterInfos(m.getParameters()),
                        throwsStrings(m.getThrownExceptions())));
            }
        }

        // コンストラクタを 1 つも書いていない public クラスには、引数なしの public コンストラクタが自動で作られる
        boolean declaresConstructor = type.getMembers().stream().anyMatch(m -> m instanceof ConstructorDeclaration);
        if (!declaresConstructor && type instanceof ClassOrInterfaceDeclaration c && !c.isInterface()) {
            constructors.add(new ConstructorInfo(List.of("public"), List.of(), List.of(), true));
        }
        // record は、要素を引数に取るコンストラクタ（標準コンストラクタ）が自動で作られる
        if (type instanceof RecordDeclaration r && constructors.stream()
                .noneMatch(c -> c.parameters().size() == r.getParameters().size())) {
            constructors.add(new ConstructorInfo(List.of("public"), parameterInfos(r.getParameters()), List.of(), true));
        }

        classes.add(new ClassInfo(
                packageName,
                name,
                qualifiedName,
                file,
                kindOf(type),
                modifierStrings(type.getModifiers()),
                annotationStrings(type.getAnnotations()),
                typeParameters,
                extendsTypes,
                implementsTypes,
                imports,
                enumConstants,
                constructors,
                methods));

        for (BodyDeclaration<?> member : type.getMembers()) {
            if (member instanceof TypeDeclaration<?> nested) {
                addType(nested, packageName, name, file, imports);
            }
        }
    }

    /** 外部から見える型か（トップレベルは public、入れ子は public か interface 内）。 */
    private static boolean isVisible(TypeDeclaration<?> type) {
        if (type.isTopLevelType()) {
            return type.isPublic();
        }
        Node parent = type.getParentNode().orElse(null);
        if (parent instanceof TypeDeclaration<?> outer) {
            return isPublicMember(outer, type.getModifiers());
        }
        return false;
    }

    /** interface / @interface のメンバーは private と書かない限り public。それ以外は public の明記が必要。 */
    private static boolean isPublicMember(TypeDeclaration<?> owner, NodeList<Modifier> modifiers) {
        boolean inInterface = (owner instanceof ClassOrInterfaceDeclaration c && c.isInterface())
                || owner instanceof AnnotationDeclaration;
        if (inInterface) {
            return modifiers.stream().noneMatch(m -> m.getKeyword() == Modifier.Keyword.PRIVATE);
        }
        return modifiers.stream().anyMatch(m -> m.getKeyword() == Modifier.Keyword.PUBLIC);
    }

    private static String kindOf(TypeDeclaration<?> type) {
        if (type instanceof ClassOrInterfaceDeclaration c) {
            return c.isInterface() ? "interface" : "class";
        } else if (type instanceof EnumDeclaration) {
            return "enum";
        } else if (type instanceof RecordDeclaration) {
            return "record";
        } else if (type instanceof AnnotationDeclaration) {
            return "annotation";
        }
        return "type";
    }

    private static String importString(ImportDeclaration imp) {
        return (imp.isStatic() ? "static " : "") + imp.getNameAsString() + (imp.isAsterisk() ? ".*" : "");
    }

    private static List<String> modifierStrings(NodeList<Modifier> modifiers) {
        return modifiers.stream().map(m -> m.getKeyword().asString()).collect(Collectors.toList());
    }

    private static List<String> annotationStrings(NodeList<AnnotationExpr> annotations) {
        return annotations.stream().map(AnnotationExpr::toString).collect(Collectors.toList());
    }

    private static List<String> typeParameterStrings(NodeList<TypeParameter> params) {
        return params.stream().map(TypeParameter::toString).collect(Collectors.toList());
    }

    private static List<String> typeStrings(NodeList<ClassOrInterfaceType> types) {
        return types.stream().map(ClassOrInterfaceType::asString).collect(Collectors.toList());
    }

    private static List<String> throwsStrings(NodeList<ReferenceType> types) {
        return types.stream().map(ReferenceType::asString).collect(Collectors.toList());
    }

    private static List<ParameterInfo> parameterInfos(NodeList<com.github.javaparser.ast.body.Parameter> params) {
        return params.stream()
                .map(p -> new ParameterInfo(p.getTypeAsString(), p.getNameAsString(), p.isVarArgs()))
                .collect(Collectors.toList());
    }
}
