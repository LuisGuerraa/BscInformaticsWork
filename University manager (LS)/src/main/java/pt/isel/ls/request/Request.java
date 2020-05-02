package pt.isel.ls.request;

public class Request {
    private Method method;
    private Path path;
    private Parameter parameters;
    private Header header;

    public Request(String method, String path, Header header, String parameters) {
        this.method = Method.valueOf(method.toUpperCase());
        this.path = new Path(path);
        this.header = header;
        this.parameters = parameters != null ? new Parameter(parameters) : new Parameter();
    }

    public Method getMethod() {
        return method;
    }

    public Path getPath() {
        return path;
    }

    public Parameter getParameters() {
        return parameters;
    }

    public Header getHeader() {
        return header;
    }

    public void closePrintStream() {
        header.close();
    }
}
