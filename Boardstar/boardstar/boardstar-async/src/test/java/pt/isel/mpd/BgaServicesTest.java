package pt.isel.mpd;

import io.reactivex.rxjava3.core.Observable;
import org.isel.boardstar.BgaWebApi;
import org.isel.boardstar.BoardstarService;
import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;
import org.junit.Test;
import pt.isel.mpd.util.AsyncRequest;
import pt.isel.mpd.util.request.AsyncHttpRequest;
import java.util.concurrent.CompletableFuture;
import static org.junit.Assert.assertEquals;

public class BgaServicesTest {

    static class CountableRequest implements AsyncRequest {
        final AsyncRequest req;
        private int count = 0;

        public CountableRequest(AsyncRequest req) {
            this.req = req;
        }

        public int getCount() { return count; }

        @Override
        public CompletableFuture<String> getBody(String path) {
            count++;
            System.out.println(path);
            return req.getBody(path);
        }

        @Override
        public void close() throws Exception {
            req.close();
        }
    }

    @Test
    public void testGetCategories() {
        CountableRequest req = new CountableRequest(new AsyncHttpRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));

        Observable<Category> categories = service.getCategories().cache();
        assertEquals(1, req.getCount());
        assertEquals(114, (long)categories.count().blockingGet());
        assertEquals(1, req.getCount());

        Category last = categories.reduce((first, second) -> second).blockingGet();
        assertEquals("Zombies", last.getName());
        assertEquals("FmGV9rVu1c", last.getId());
    }

    @Test
    public void testSearchCategories() {
        CountableRequest req = new CountableRequest(new AsyncHttpRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));

        Observable<Game> games = service.searchByCategories(6, "7rV11PKqME", "O0ogzwLUe8");
        assertEquals(0, req.getCount());
        assertEquals(9, (long)games.count().blockingGet());
        assertEquals(2, req.getCount());

        Game game = service.searchByCategories(1, "7rV11PKqME", "O0ogzwLUe8").firstElement().blockingGet();
        assertEquals(3, req.getCount());
        assertEquals(game.getId(), "OIXt3DmJU0");
        assertEquals(game.getName(), "Catan");
    }

    @Test
    public void testSearchByArtist() {
        CountableRequest req = new CountableRequest(new AsyncHttpRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));

        Observable<Game> games = service.searchByArtist(14, "Dimitri%20Bielak");
        assertEquals(0, req.getCount());
        assertEquals(15, (long)games.count().blockingGet());
        assertEquals(3, req.getCount());

        Game game = service.searchByArtist(1, "Dimitri%20Bielak").firstElement().blockingGet();
        assertEquals(4, req.getCount());
        assertEquals(game.getId(), "pPZnlKC4G3");
        assertEquals(game.getName(), "Inis");
    }
}
