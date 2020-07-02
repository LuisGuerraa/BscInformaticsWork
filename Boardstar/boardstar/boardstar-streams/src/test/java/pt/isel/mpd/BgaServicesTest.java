package pt.isel.mpd;

import org.isel.boardstar.BgaWebApi;
import org.isel.boardstar.BoardstarService;
import org.isel.boardstar.StreamUtils;
import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;
import org.junit.Test;
import pt.isel.mpd.util.request.CountableRequest;
import pt.isel.mpd.util.request.HttpRequest;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BgaServicesTest {

    @Test
    public void testGetCategories() {
        CountableRequest req = new CountableRequest(new HttpRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));

        Stream<Category> categories = service.getCategories();
        assertEquals(1, req.getCount());
        assertEquals(128, categories.count());

        Stream<Category> categories2 = service.getCategories();
        assertEquals(1, req.getCount());                //categories saved in cache, so only 1 request was made
        assertEquals(128, categories2.count());

        Category category = service.getCategories().findFirst().get();
        assertEquals(1, req.getCount());

        assertEquals(category.getId(), "WnxKtlGfdR");
        assertEquals(category.getName(), "123");
    }

    @Test
    public void testSearchCategories() {
        CountableRequest req = new CountableRequest(new HttpRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));

        Stream<Game> games = service.searchByCategories("7rV11PKqME", "O0ogzwLUe8");
        assertEquals(0, req.getCount());

        Game game = service.searchByCategories("7rV11PKqME", "O0ogzwLUe8").findFirst().get();
        assertEquals(1, req.getCount());
        assertEquals(game.getId(), "OIXt3DmJU0");
        assertEquals(game.getName(), "Catan");

        assertEquals(9, games.count());      //there are 9 games
        assertEquals(4, req.getCount());    //only searched 3 times (4 because of the first request: line 44)
    }

    @Test
    public void testSearchByArtist() {
        CountableRequest req = new CountableRequest(new HttpRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));

        Stream<Game> games = service.searchByArtist("Dimitri%20Bielak");
        assertEquals(0, req.getCount());

        Game game = service.searchByArtist("Dimitri%20Bielak").findFirst().get();
        assertEquals(1, req.getCount());

        assertEquals(19, games.count());     //there are 19 games
        assertEquals(6, req.getCount());    //only searched 5 times (6 because of the first request: line 54)

        assertEquals(game.getId(), "pPZnlKC4G3");
        assertEquals(game.getName(), "Inis");
    }

    @Test
    public void givenCacheTest(){
        Random r = new Random();
        Stream<Integer> nrs = Stream.generate(() -> r.nextInt(100));
        Supplier<Stream<Integer>> numbers = StreamUtils.cache(nrs);
        Object[] expected = numbers.get().limit(10).toArray();
        Object[] actual = numbers.get().limit(10).toArray();
        assertArrayEquals(expected, actual);
    }
}
