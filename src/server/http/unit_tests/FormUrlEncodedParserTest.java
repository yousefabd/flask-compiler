package server.http.unit_tests;

import server.http.BadRequestException;
import server.http.FormUrlEncodedParser;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class FormUrlEncodedParserTest {

    public static void main(String[] args) {
        FormUrlEncodedParser parser =
                new FormUrlEncodedParser();

        String encodedForm =
                "name=Gaming+Mouse"
                        + "&price=59.99"
                        + "&image=mouse%2Epng"
                        + "&details=Fast+%26+accurate";

        Map<String, String> form =
                parser.parse(
                        encodedForm.getBytes(
                                StandardCharsets.UTF_8
                        )
                );

        require(
                "Gaming Mouse".equals(
                        form.get("name")
                ),
                "Form name was decoded incorrectly"
        );

        require(
                "59.99".equals(
                        form.get("price")
                ),
                "Form price was decoded incorrectly"
        );

        require(
                "mouse.png".equals(
                        form.get("image")
                ),
                "Form image was decoded incorrectly"
        );

        require(
                "Fast & accurate".equals(
                        form.get("details")
                ),
                "Form details were decoded incorrectly"
        );

        boolean malformedRejected =
                false;

        try {
            parser.parse(
                    "name=%ZZ".getBytes(
                            StandardCharsets.UTF_8
                    )
            );

        } catch (BadRequestException exception) {
            malformedRejected = true;
        }

        require(
                malformedRejected,
                "Malformed form encoding was accepted"
        );

        System.out.println(
                "URL-encoded form parsing passed."
        );

        System.out.println(form);
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