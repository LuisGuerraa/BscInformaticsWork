package pt.isel.ls.commands.course;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.ResultViewer;


public class CreateCourseToProgrammeOfPid extends CommandHandler {

    private final String pid = "pid";
    private final String acr = "acr";
    private final String mandatory = "mandatory";
    private final String semesters = "semesters";
    private final TransactionManager tm;

    public CreateCourseToProgrammeOfPid(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        boolean isMandatory = isMandatory(parameter.getValue(mandatory));
        String queryOptional = "INSERT INTO optional_course VALUES(?,?,?)";
        String queryMandatory = "INSERT INTO mandatory_course VALUES(?,?,?)";
        String acronym = parameter.getValue(acr);
        String programme = parameter.getValue(pid);
        return tm.run(k -> {
            try {
                String query = isMandatory ? queryMandatory : queryOptional;
                for (String semester : getSemesters(parameter.getValue(semesters))) {
                    PreparedStatement ps = k.getConn().prepareStatement(query);
                    ps.setString(1, acronym);
                    ps.setString(2, programme);
                    ps.setInt(3, Integer.parseInt(semester));
                    ps.executeUpdate();
                }
                k.commitTransaction();
                return new EmptyResultView()
                        .addHeader("Location", Template.getCoursesAcr(acronym));
            } catch (SQLException | NumberFormatException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView()
                        .addHeader("Location",
                                Template.newForm(
                                        Template.getProgrammesPidCourses(programme))
                                        + errorQuery);
            }
        });
    }

    @Override
    public String[] getArguments() {
        return new String[]{pid, acr, mandatory, semesters};
    }

    @Override
    public String getDescription() {
        return "Adds a new course to the programme pid, given the parameters"
                + "(acr - course acronym, mandatory - true if the course is mandatory, "
                + "semesters - comma separated Lists of curricular semesters).";
    }


}
