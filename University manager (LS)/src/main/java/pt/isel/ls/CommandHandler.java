package pt.isel.ls;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.ResultViewer;

public abstract class CommandHandler {

    public static final String error =
            "Error performing command with inserted values. Please try again!";
    public static final String errorQuery = "?" + error;

    public abstract ResultViewer execute(Parameter parameter) throws SQLException;

    public abstract String[] getArguments();

    public abstract String getDescription();

    public int getYearOfSemester(String semester) throws NumberFormatException {
        return Integer.parseInt(semester.substring(0, semester.length() - 1));
    }

    public String getSeasonOfSemester(String semester) {
        return semester.substring(semester.length() - 1);
    }

    public boolean isMandatory(String string) {
        return string.toUpperCase().equals("TRUE");
    }

    public List<String> getSemesters(String semester) {
        return Arrays.asList(semester.split(","));
    }

}



