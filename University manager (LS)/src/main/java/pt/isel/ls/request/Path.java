package pt.isel.ls.request;

public class Path {
    private final String fullPath;
    private String[] pathArray;

    Path(String fullPath) {
        this.fullPath = fullPath;
        setPathArray(fullPath);
    }

    public String getFullPath() {
        return fullPath;
    }

    public String[] getPathArray() {
        return pathArray;
    }

    private void setPathArray(String path) {
        if (path.equals("/")) {
            pathArray = new String[0];
        } else {
            pathArray = fullPath.split("/");
        }
    }

    @Override
    public String toString() {
        return fullPath;
    }
}
