package org.isel.boardstar;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import org.isel.boardstar.model.Artist;
import org.isel.boardstar.model.Game;
import pt.isel.mpd.util.request.AsyncHttpRequest;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class WebApp {
    final static int PORT = 8080;
    static final BoardstarService service = new BoardstarService(new BgaWebApi(new AsyncHttpRequest()));
    private static Map<String, Observable<Artist>> artistsMap = new HashMap<>();

    private static String homeHtml =
            "<html>" +
                "<body>" +
                    "<head><title>Boardstar Games API</title></head>" +
                    "<h1>Boardstar Games API</h1>" +
                    "<h2> Search all categories here:</h2>" +
                    "<a href=\"http://localhost:8080/categories\">Categories</a>" +
                    "<section>" +
                        "<h2>Search games by category:</h2>" +
                        "<div>" +
                            "<div><span>Category Id: </span><input type=\"text\" id=\"categoryId\"/></div>" +
                        "</div> <br>" +
                        "<a href='' onclick=\"this.href='http://localhost:8080/categories/'+document.getElementById('categoryId').value+'/games'\">Search</a>" +
                    "</section>" +
                    "<section>" +
                        "<h2>Search games by artist:</h2>" +
                        "<div>" +
                            "<div><span>Artist Name: </span><input type=\"text\" id=\"artistName\"/></div>" +
                        "</div> <br>" +
                        "<a href='' onclick=\"this.href='http://localhost:8080/artists/'+document.getElementById('artistName').value+'/games'\">Search</a>" +
                    "</section>" +
                    "<section>" +
                        "<h3>Subject: Modelacao e Padroes de Desenho</h3>" +
                        "<h3>Class: 41N</h3>" +
                        "<h3>Professor: Miguel Gamboa de Carvalho</h3>" +
                        "<h3>Group Elements:</h3>" +
                        "<ul>" +
                            "<li>Miguel Achega 42149</li>" +
                            "<li>Luis Guerra   43755</li>" +
                            "<li>Gabriel Cunha 43532</li>" +
                        "</ul>" +
                    "</section>" +
                "</body>" +
            "</html>";

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(PORT);
        addPaths(app);
    }

    private static void addPaths(Javalin app) {
        app.get("/", ctx -> ctx
            .contentType("text/html")
            .result(homeHtml));
        app.get("/categories", ctx -> getHandler(ctx, WebApp::getCategories));
        app.get("/categories/:id/games", ctx -> getHandler(ctx, WebApp::getCategoryGames));
        app.get("/artists/:id/games", ctx -> getHandler(ctx, WebApp::getArtistGames));
        app.get("/games/:id/artists", ctx -> getHandler(ctx, WebApp::getArtistsFromGame));
    }

    private static void getHandler(Context ctx, Function<Context, Observable<String>> func) {
        CompletableFuture<Void> cf = new CompletableFuture<>();
        ctx.result(cf);
        PrintWriter writer;
        try {
            writer = ctx.res.getWriter();
            PrintWriter finalWriter = writer;
            func.apply(ctx)
                    .doOnSubscribe(disp -> ctx.contentType("text/html"))
                    .doOnNext(item -> {
                        finalWriter.write(item);
                        finalWriter.flush();
                    })
                    .doOnComplete(() -> cf.complete(null))
                    .doOnError(cf::completeExceptionally)
                    .subscribe();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static Observable<String> getCategories(Context ctx) {
        if (ctx.queryParam("categories") != null) {
            String[] categories = ctx.queryParam("categories").split(",");
            return service.getCategories()
                    .map(cat -> {
                        if (Arrays.asList(categories).contains(cat.getId()))
                            return String.format("<br><strong>%s</strong>: %s " +
                                    "<a href=\"http://localhost:8080/categories/%s/games?size=10\">Games</a>", cat.getName(), cat.getId(), cat.getId());
                        return "";
                    });
        }
        return service.getCategories()
                    .map(cat -> String.format("<br><strong>%s</strong>: %s " +
                            "<a href=\"http://localhost:8080/categories/%s/games?size=10\">Games</a>", cat.getName(), cat.getId(), cat.getId()));
    }

    private static Observable<String> getCategoryGames(Context ctx) {
        String categoryId = ctx.pathParam("id");
        int size = ctx.queryParam("size") != null ? Integer.parseInt(ctx.queryParam("size")) : 10;
        return service.searchByCategories(size, categoryId)
                .map(game -> showHtmlData(ctx, game));
    }

    private static Observable<String> getArtistGames(Context ctx) {
        String artistName = ctx.pathParam("id");
        int size = ctx.queryParam("size") != null ? Integer.parseInt(ctx.queryParam("size")) : 10;
        return service.searchByArtist(size, artistName)
                .map(game -> showHtmlData(ctx, game));
    }

    private static Observable<String> getArtistsFromGame(Context ctx) {
        String gameId = ctx.pathParam("id");
        CompletableFuture<Void> cf = new CompletableFuture<>();
        ctx.result(cf);
        artistsMap.get(gameId)
                .doOnComplete(() -> cf.complete(null))
                .subscribe();
        return artistsMap.get(gameId)
                .map(artist -> {
                    return String.format("<br><strong>Name: %s</strong> " +
                            "<a href=\"http://localhost:8080/artists/%s/games\">Games</a> ", artist.getName(), artist.getName());
                });
    }

    private static String showHtmlData(Context ctx, Game game) {
        artistsMap.put(game.getId(), game.getArtists());
        String[] categories = {""};
        CompletableFuture<Void> cf = new CompletableFuture<>();
        ctx.result(cf);
        game.getCategories()
                .doOnNext(category -> categories[0] += category.getId() + ",")
                .doOnComplete(() -> cf.complete(null))
                .subscribe();
        return String.format("<br><strong>%s</strong>: %s " +
                "<a href=\"http://localhost:8080/games/%s/artists\">Artists</a> " +
                "<a href=\"http://localhost:8080/categories?categories=%s\">Categories</a>", game.getName(), game.getId(), game.getId(), categories[0]);
    }
}