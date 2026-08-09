package python.runtime.unit_tests;

import python.runtime.PythonEnvironment;

import java.util.ArrayList;
import java.util.List;

public final class PythonEnvironmentTest {

    public static void main(String[] args) {
        PythonEnvironment module =
                PythonEnvironment.module();

        ArrayList<String> products =
                new ArrayList<>(
                        List.of("Laptop", "Phone")
                );

        module.assign("products", products);

        PythonEnvironment function =
                module.createFunctionFrame();

        /*
         * Functions can read module values.
         */
        require(
                function.resolve("products") == products,
                "Function could not read module products"
        );

        /*
         * Normal assignment remains local.
         */
        function.assign("message", "local");

        require(
                !module.contains("message"),
                "Local assignment escaped into module scope"
        );

        /*
         * Mutating the resolved object changes the module object.
         */
        @SuppressWarnings("unchecked")
        List<String> resolvedProducts =
                (List<String>) function.resolve("products");

        resolvedProducts.add("Headphones");

        require(
                products.size() == 3,
                "List mutation was not preserved"
        );

        /*
         * Reassignment after global replaces the module value.
         */
        function.declareGlobal("products");

        function.assign(
                "products",
                new ArrayList<>(
                        List.of("Keyboard")
                )
        );

        require(
                module.resolve("products")
                        .equals(List.of("Keyboard")),
                "Global reassignment did not update module scope"
        );

        /*
         * Python None is defined even though its Java value is null.
         */
        function.defineLocal("nothing", null);

        require(
                function.contains("nothing"),
                "A Python None value became undefined"
        );

        require(
                function.resolve("nothing") == null,
                "Python None did not resolve to Java null"
        );

        System.out.println(
                "PythonEnvironment test passed."
        );

        System.out.println(
                "Final products: "
                        + module.resolve("products")
        );
    }

    private static void require(
            boolean condition,
            String message
    ) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}