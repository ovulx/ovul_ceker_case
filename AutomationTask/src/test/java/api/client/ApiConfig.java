package api.client;

public final class ApiConfig {
    private ApiConfig() {}

    private static final String DEFAULT_BASE_URL = "https://petstore.swagger.io/v2";

    public static String baseUrl() {
        return System.getProperty("api.baseUrl", DEFAULT_BASE_URL);
    }
}
