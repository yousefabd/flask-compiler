package python.runtime.flask;

public final class FlaskRuntimeDefaults {

    public static final String STATIC_ENDPOINT =
            "static";

    public static final String STATIC_ARGUMENT =
            "filename";

    public static final String STATIC_URL_PREFIX =
            "/static/";
    public static final String STATIC_DIRECTORY_NAME =
            "static";

    public static final String STATIC_RULE =
            STATIC_URL_PREFIX
                    + "<path:"
                    + STATIC_ARGUMENT
                    + ">";

    private FlaskRuntimeDefaults() {
    }
}