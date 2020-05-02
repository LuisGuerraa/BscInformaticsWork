package pt.isel.ls;

import java.util.HashMap;
import java.util.TreeMap;

import pt.isel.ls.request.Method;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.request.Path;
import pt.isel.ls.request.Request;

public class Router {
    private HashMap<String,
            TreeMap<String, TreeMap<String, CommandHandler>>> hashMapTree;
    private CommandHandler handler;

    public Router() {
        hashMapTree = new HashMap<>();
    }

    private TreeMap<String, TreeMap<String, CommandHandler>> map(Method m) {

        return hashMapTree.get(m.getMethod());
    }

    public void add(Method method, String templatePath, CommandHandler cm) {
        TreeMap<String, TreeMap<String, CommandHandler>> mapTree =
                hashMapTree.computeIfAbsent(method.getMethod(), k -> new TreeMap<>());
        String firstPath = getFirstPath(templatePath);
        TreeMap<String, CommandHandler> treeMap =
                mapTree.computeIfAbsent(firstPath, k -> new TreeMap<>());
        treeMap.put(templatePath, cm);
    }

    CommandHandler get(Request request)  {
        if (match(request.getMethod(), request.getPath(), request.getParameters())) {
            return handler;
        }
        return null;
    }

    private boolean match(Method method, Path path, Parameter parameter) {
        String[] templateArray;
        String[] pathArray = path.getPathArray();
        String template = "/";
        if (pathArray.length != 0) {
            template += pathArray[1];
        }
        if (!map(method).containsKey(template)) {
            return false;
        }
        TreeMap<String, CommandHandler> treeMap = map(method).get(template);
        for (String templateKey : treeMap.tailMap(template).keySet()) {
            templateArray = templateKey.split("/");
            if (checkTemplate(pathArray, templateArray, parameter)) {
                setHandler(treeMap.get(templateKey));
                return true;
            }
        }
        return false;
    }

    private void setHandler(CommandHandler handler) {
        this.handler = handler;
    }

    private String getFirstPath(String key) {
        if (key.equals("/")) {
            return key;
        }
        return "/" + key.split("/")[1];
    }

    private boolean checkTemplate(String[] path, String[] template, Parameter parameter) {
        if (path.length != template.length) {
            return false;
        }
        String pathKey;
        String auxKey;
        for (int i = 2; i < path.length; i++) {
            pathKey = path[i];
            auxKey = template[i];
            if (auxKey.charAt(0) == '{') {
                parameter.addValues(auxKey.replaceAll("[{}]", ""), pathKey);
            } else if (!pathKey.equals(auxKey)) {
                return false;
            }
        }
        return true;
    }

}

