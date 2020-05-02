package pt.isel.ls.commands.classes;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.model.EntitySet;
import pt.isel.ls.model.entities.Class;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.result.ComposedResult;
import pt.isel.ls.result.Property;
import pt.isel.ls.resultview.CommonResultView;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.PathLink;
import pt.isel.ls.resultview.ResultViewer;



public class GetClassesOfTeacherOfNum extends CommandHandler {
    private final String num = "num";
    private final TransactionManager tm;

    public GetClassesOfTeacherOfNum(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {

        int teacherNum = Integer.parseInt(parameter.getValue(num));
        String title = "List of Classes of Teacher " +  teacherNum;
        String query = "SELECT id, yearSem, season, acrCourse "
                + "FROM teacher_class WHERE numTeacher = ?";
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn().prepareStatement(query);
                ps.setInt(1, teacherNum);
                List<Class> classes = EntitySet.getClasses(ps);
                k.commitTransaction();
                return new CommonResultView(title,
                        List.of(
                                new PathLink("Teacher Info", Template.getTeacherOfNum(teacherNum))),
                        new ComposedResult(
                                title,
                                classes,
                                List.of("Class ID", "Semester Year",
                                        "Season", "Course Acronym", "Class ID"),
                                new Property<>(c -> String.valueOf(c.getId())),
                                new Property<>(c -> String.valueOf(c.getYearSem())),
                                new Property<>(Class::getSeason),
                                new Property<>(Class::getAcrCourse),
                                new Property<>(c -> c.getId() + "-"
                                        + c.getYearSem() + c.getSeason(),
                                        c -> Template.getCoursesAcrClassesSemNum(
                                                c.getAcrCourse(), c.getYearSem()
                                                        + c.getSeason(), c.getId()))
                        )
                );
            } catch (SQLException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView();
            }
        });
    }

    @Override
    public String[] getArguments() {
        return new String[]{num};
    }

    @Override
    public String getDescription() {
        return "Shows all classes for the teacher with num number.";
    }

}
