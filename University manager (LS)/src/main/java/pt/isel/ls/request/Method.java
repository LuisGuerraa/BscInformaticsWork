package pt.isel.ls.request;

public enum Method {
    GET("GET"),
    POST("POST"),
    DELETE("DELETE"),
    PUT("PUT"),
    OPTION("OPTION"),
    LISTEN("LISTEN"),
    EXIT("EXIT");

    private final String method;

    Method(String method) {

        this.method = method;
    }

    public String getMethod() {

        return method;
    }
}
