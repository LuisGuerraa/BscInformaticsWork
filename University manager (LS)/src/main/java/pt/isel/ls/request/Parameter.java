package pt.isel.ls.request;

import java.util.HashMap;
import java.util.LinkedList;

public class Parameter {
    private String parameter;
    private HashMap<String, LinkedList<String>> params;

    public Parameter(String parameter) {
        this.parameter = parameter;
        params = new HashMap<>();
        setParameters();
    }

    public Parameter() {
        params = new HashMap<>();
    }

    private void setParameters() {
        String[] val;
        String[] args = parameter.split("&");
        for (String s1 : args) {
            val = s1.split("=");
            if (val.length == 1) {
                return;
            }
            addValues(val[0], val[1]);
        }
    }

    public LinkedList<String> getValues(String key) {
        return params.get(key);
    }

    public String getValue(String key) {
        return getValues(key).getFirst();
    }

    private boolean containsKey(String key) {
        return params.containsKey(key);
    }

    public void addValues(String key, String value) {
        LinkedList<String> values;
        if ((values = params.get(key)) == null) {
            values = new LinkedList<>();
        }
        values.add(value);
        params.put(key, values);
    }

    public boolean checkArguments(String[] args) {
        for (String s : args) {
            if (!containsKey(s)) {
                return false;
            }
        }
        return true;
    }
}
