import java.util.function.BiFunction;
import org.graalvm.webimage.api.JS;
import org.graalvm.webimage.api.JSNumber;

public class AdderExport {
    @JS(args = {"adder"}, value = "globalThis.adder = adder;")
    private static native void export(BiFunction<JSNumber, JSNumber, JSNumber> adder);

    public static void main(String[] args) {
        export((a, b) -> JSNumber.of(a.asInt() + b.asInt()));
    }
}