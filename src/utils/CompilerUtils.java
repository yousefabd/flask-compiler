package utils;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import java.io.IOException;
import java.nio.file.Path;

public class CompilerUtils {
    public static String stripStringQuotes(String raw) {
        String value = raw.strip();
        String[] quoteTypes = {"'''", "\"\"\"", "'", "\""};

        for (String quote : quoteTypes) {
            if (value.startsWith(quote) &&
                    value.endsWith(quote) &&
                    value.length() >= quote.length() * 2) {
                return value.substring(
                        quote.length(),
                        value.length() - quote.length()
                );
            }
        }

        return value;
    }
    public static CharStream readSource(Path file) {
        try {
            return CharStreams.fromPath(file);
        } catch (IOException e) {
            throw new errors.ParseError(file.toString(),
                    "Cannot read source file: " + e.getMessage(), e);
        }
    }
}
