package pt.isel.ls.commands.classes;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.ResultViewer;



public class CreateClassOnCourseOfAcr extends CommandHandler {

    private final String num = "num"; // id de classes
    private final String acr = "acr"; // acr de course
    private final String sem = "sem";
    private final TransactionManager tm;

    public CreateClassOnCourseOfAcr(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String query = "INSERT INTO CLASS VALUES (?,?,?,?)";
        String semester = parameter.getValue(sem);
        String classNum = parameter.getValue(num);
        String acronym = parameter.getValue(acr);
        String season = getSeasonOfSemester(semester);
        int year;
        try {
            year = getYearOfSemester(semester);
        } catch (NumberFormatException e) {
            UserInterface.info(e.getMessage(), error);
            return new EmptyResultView()
                    .addHeader("Location",
                            Template.newForm(Template.getCoursesAcrClasses(acronym))
                                    + errorQuery);
        }
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn().prepareStatement(query);
                ps.setString(1, classNum);
                ps.setInt(2, year);
                ps.setString(3, season);
                ps.setString(4, acronym);
                ps.executeUpdate();
                k.commitTransaction();
                return new EmptyResultView().addHeader("Location",
                        Template.getCoursesAcrClassesSemNum(acronym, year + season, classNum));
            } catch (SQLException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView()
                        .addHeader("Location",
                                Template.newForm(Template.getCoursesAcrClasses(acronym))
                                        + errorQuery);
            }
        });
    }

    @Override
    public String[] getArguments() {
        return new String[]{acr, sem, num};
    }

    @Override
    public String getDescription() {
        return "Creates a new class on the course with acr acronym, given the parameters"
                + "(sem - semester identifier, num - class number).";
    }

}
