package pt.isel.mpd;

import org.isel.boardstar.BgaWebApi;
import org.isel.boardstar.BoardstarService;
import org.isel.boardstar.dto.GameDto;
import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;
import org.junit.Test;
import pt.isel.mpd.util.request.CountableRequest;
import pt.isel.mpd.util.request.HttpRequest;

import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static pt.isel.mpd.util.LazyQueries.*;

public class BgaServicesTest {

    @Test
    public void testGetCategories() {
        CountableRequest req = new CountableRequest(new HttpRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));

        Iterable<Category> categories = service.getCategories();

        Category category = first(service.getCategories()).get();
        assertEquals(1, req.getCount());
        assertEquals(128, count(categories));

        Iterable<Category> categories2 = service.getCategories();
        assertEquals(1, req.getCount());        //categories saved in cache, so only 1 request was made
        assertEquals(128, count(categories2));

        assertEquals(category.getId(), "WnxKtlGfdR");
        assertEquals(category.getName(), "123");
    }

    @Test
    public void testSearchCategories() {
        CountableRequest req = new CountableRequest(new HttpRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));

        Iterable<Game> games = service.searchByCategories("7rV11PKqME", "O0ogzwLUe8");
        assertEquals(0, req.getCount());

        Game game = first(service.searchByCategories("7rV11PKqME", "O0ogzwLUe8")).get();
        assertEquals(1, req.getCount());

        assertEquals(9, count(games));      //there are 9 games
        assertEquals(4, req.getCount());    //only searched 3 times (4 because of the first request: line 44)

        assertEquals(game.getId(), "OIXt3DmJU0");
        assertEquals(game.getName(), "Catan");
    }

    @Test
    public void testSearchByArtist() {
        CountableRequest req = new CountableRequest(new HttpRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));

        Iterable<Game> games = service.searchByArtist("Dimitri%20Bielak");
        assertEquals(0, req.getCount());

        Game game = first(service.searchByArtist("Dimitri%20Bielak")).get();
        assertEquals(1, req.getCount());

        assertEquals(19, count(games));     //there are 19 games
        assertEquals(6, req.getCount());    //only searched 5 times (6 because of the first request: line 54)

        assertEquals(game.getId(), "pPZnlKC4G3");
        assertEquals(game.getName(), "Inis");
    }

    @Test
    public void testSearchWithCache() {
        CountableRequest req = new CountableRequest(new HttpRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));

        Iterable<Game> games = service.searchByArtist("Dimitri%20Bielak");
        assertEquals(0, req.getCount());

        Game game = first(games).get();
        assertEquals(1, req.getCount());

        games = cache(games);
        assertEquals(19, count(games));
            // a page has 5 elements so it will need to search the api 5 times because the number of games is 19
        assertEquals(6, req.getCount());   // 6 because we did the first request and incremented 1

        Game last = last(games).get();
        assertEquals(6, req.getCount());
    }

    @Test
    public void givenCacheTest(){
        Random r = new Random();
        Iterable<Integer> nrs = generate(() -> r.nextInt(100));
        nrs = cache(nrs);
        Object[] expected = toArray(limit(nrs, 10));
        Object[] actual = toArray(limit(nrs, 10));
        assertArrayEquals(expected, actual);
    }
}
