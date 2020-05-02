package pt.isel.ls.commands.course;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.ResultViewer;


public class CreateCourse extends CommandHandler {

    private final TransactionManager tm;
    private String acr = "acr";
    private String name = "name";
    private String teacher = "teacher";

    public CreateCourse(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String query = "INSERT INTO course VALUES (?,?,?)";
        String acronym = parameter.getValue(acr);
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn()
                        .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, acronym);
                ps.setString(2, parameter.getValue(name));
                ps.setInt(3, Integer.valueOf(parameter.getValue(teacher)));

                ps.executeUpdate();
                k.commitTransaction();
            } catch (SQLException | NumberFormatException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView()
                        .addHeader("Location", Template.NEW_FORM_COURSES + errorQuery);
            }
            return new EmptyResultView()
                    .addHeader("Location", Template.getCoursesAcr(acronym));
        });
    }

    @Override
    public String[] getArguments() {
        return new String[]{acr, name, teacher};
    }

    @Override
    public String getDescription() {
        return "Creates a new course, given the parameters(name - course name, "
               + "acr - course acronym, teacher - number of the coordinator teacher).";
    }

}
