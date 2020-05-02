package pt.isel.ls.response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.isel.ls.App;
import pt.isel.ls.UserInterface;
import pt.isel.ls.request.Request;
import pt.isel.ls.resultview.HomeResultView;
import pt.isel.ls.resultview.ResultViewer;

public class ResponseServlet extends HttpServlet {
    private App app;
    private static final String home = "/";

    public ResponseServlet(App app) {
        this.app = app;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        UserInterface.log(String.format("Received %s request for %s", req.getMethod(), uri));
        handleResponse(uri, req.getQueryString()).send(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String uri = req.getRequestURI();
        UserInterface.log(String.format("Received %s request for %s", req.getMethod(), uri));
        handleResponsePost(req, uri).send(resp);
    }

    private Response handleResponsePost(HttpServletRequest req, String uri) {
        try {
            Request request = app.getRequest(new String[]{"POST", uri, parseParameters(req)});
            return new Response(HttpStatusCode.SeeOther, app.executeRequest(request));
        } catch (SQLException e) {
            UserInterface.log(e.getMessage());
            return new Response(HttpStatusCode.InternalServerError);
        }
    }

    private Response handleResponse(String uri, String query) {
        if (uri.equals(home)) {
            return new Response(HttpStatusCode.Ok, new HomeResultView());
        }
        try {
            ResultViewer resultViewer = app.executeRequest(app.getRequest(
                    new String[]{"GET", uri}));
            if (resultViewer == null) {
                return new Response(HttpStatusCode.NotFound);
            }
            boolean error = query != null;
            if (error) {
                return new Response(HttpStatusCode.Ok,
                        resultViewer.addInfo(
                                query.replace("%20", " ")));
            }
            return new Response(HttpStatusCode.Ok, resultViewer);
        } catch (SQLException e) {
            UserInterface.info(e.getMessage(), "Error performing command.");
            return new Response(HttpStatusCode.InternalServerError);
        } catch (IllegalArgumentException ex) {
            UserInterface.info(ex.getMessage(), "Error: Please insert a correct method.");
            return new Response(HttpStatusCode.MethodNotAllowed);
        }
    }

    private String parseParameters(HttpServletRequest req) {
        Enumeration<String> parameterNames = req.getParameterNames();
        StringBuilder parameters = new StringBuilder();
        while (parameterNames.hasMoreElements()) {
            String param = parameterNames.nextElement();
            for (String s : req.getParameterValues(param)) {
                parameters.append(param);
                parameters.append("=");
                parameters.append(s);
                parameters.append("&");
            }
            //parameters.deleteCharAt(parameters.length()-1);
            //parameters.append("&");
        }
        parameters.deleteCharAt(parameters.length() - 1);
        return parameters.toString();
    }
}
