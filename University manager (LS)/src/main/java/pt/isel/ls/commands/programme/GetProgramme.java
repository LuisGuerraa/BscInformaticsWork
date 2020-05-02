package pt.isel.ls.commands.programme;

import java.sql.SQLException;
import java.util.List;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.model.EntitySet;
import pt.isel.ls.model.entities.Programme;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.result.ComposedResult;
import pt.isel.ls.result.Property;
import pt.isel.ls.resultview.CommonResultView;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.PathLink;
import pt.isel.ls.resultview.ResultViewer;


public class GetProgramme extends CommandHandler {

    private final TransactionManager tm;

    public GetProgramme(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String query = "SELECT * FROM Programme";
        String title = "List of Programmes";
        return tm.run(k -> {
            try {
                List<Programme> programmes = EntitySet.getProgrammes(
                        k.getConn().prepareStatement(query)
                );
                k.commitTransaction();
                return new CommonResultView(
                        title,
                        List.of(new PathLink("Form to add a new Programe",
                                Template.NEW_FORM_PROGRAMMES)),
                        new ComposedResult(
                                title,
                                programmes,
                                List.of("Programme Acronym", "Programme Name", "Programme Length"),
                                new Property<>(Programme::getAcr,
                                        c -> Template.getProgrammesPid(c.getAcr())),
                                new Property<>(Programme::getName),
                                new Property<>(p -> String.valueOf(p.getLength())))
                );
            } catch (SQLException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView();
            }
        });
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "Lists all the programmes.";
    }

}
