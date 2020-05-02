package pt.isel.ls.commands.programme;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.model.EntitySet;
import pt.isel.ls.model.entities.Programme;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.result.Property;
import pt.isel.ls.result.SingleResult;
import pt.isel.ls.resultview.CommonResultView;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.PathLink;
import pt.isel.ls.resultview.ResultViewer;



public class GetProgrammeOfPid extends CommandHandler {
    private final String pid = "pid";
    private final TransactionManager tm;

    public GetProgrammeOfPid(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String programmeId = parameter.getValue(pid);
        String title = "Details of " + programmeId + " programme";
        String query = "SELECT * FROM programme WHERE ACR = ?";
        List<PathLink> linkList = List.of(
                new PathLink("Courses of " + programmeId,
                        Template.getProgrammesPidCourses(programmeId)),
                new PathLink("Programmes", Template.PROGRAMMES));
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn().prepareStatement(query);
                ps.setString(1, programmeId);
                final List<Programme> progAttrs = EntitySet.getProgrammes(ps);

                k.commitTransaction();
                SingleResult progSr = new SingleResult(
                        "Programme Details",
                        progAttrs,
                        List.of("Programme Acronym", "Programme Name",
                                "Programme Length"),
                        new Property<>(Programme::getAcr),
                        new Property<>(Programme::getName),
                        new Property<>(p -> String.valueOf(p.getLength())));
                return new CommonResultView(
                        title,
                        linkList,
                        progSr);
            } catch (SQLException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView();
            }
        });
    }

    @Override
    public String[] getArguments() {
        return new String[]{pid};
    }

    @Override
    public String getDescription() {
        return "Shows the details of programme with pid acronym.";
    }

}
