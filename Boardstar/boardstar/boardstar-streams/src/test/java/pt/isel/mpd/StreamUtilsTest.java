package pt.isel.mpd;

import org.isel.boardstar.BgaWebApi;
import org.isel.boardstar.BoardstarService;
import org.isel.boardstar.StreamUtils;
import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;
import org.junit.Test;
import pt.isel.mpd.util.request.CountableRequest;
import pt.isel.mpd.util.request.HttpRequest;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;
import static org.junit.Assert.assertEquals;

public class StreamUtilsTest {

    @Test
    public void testInterleave() {
        Stream<String> src = Stream.of("1", "2", "3");
        Stream<String> other = Stream.of("a", "b", "c", "d", "e", "f");

        Stream<String> actual = StreamUtils.interleave(src, other);
        Stream<String> expected = Stream.of("1", "a", "2", "b", "3", "c", "d", "e", "f");

        Iterator<String> iter1 = actual.iterator();
        Iterator<String> iter2 = expected.iterator();

        while (iter1.hasNext() && iter2.hasNext())
            assertEquals(iter1.next(), iter2.next());
        assert !iter1.hasNext() && !iter2.hasNext();
    }

    @Test
    public void testInterleaveWithEmptyStream() {
        Stream<String> src = Stream.of("1", "2", "3");
        Stream<String> other = Stream.of();

        Stream<String> actual = StreamUtils.interleave(src, other);
        Stream<String> expected = Stream.of("1", "2", "3");

        Iterator<String> iter1 = actual.iterator();
        Iterator<String> iter2 = expected.iterator();

        while (iter1.hasNext() && iter2.hasNext())
            assertEquals(iter1.next(), iter2.next());
        assert !iter1.hasNext() && !iter2.hasNext();
    }

    @Test
    public void testIntersectionGames() {
        CountableRequest req = new CountableRequest(new HttpRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));

        Stream<Game> result = StreamUtils.intersection(
                service.searchByCategories("7rV11PKqME"),
                service.searchByCategories("O0ogzwLUe8"));

        int[] foundGames = new int[1];
        result.forEach(game -> {
            if (game.getName().equals("Catan") || game.getName().equals("Monopoly Deal Card Game"))
                foundGames[0]++;
        });

        assert (foundGames[0] == 2);
    }

    @Test
    public void testInterception() {
        Stream<String> src = Stream.of("1", "2", "2", "2", "3", "4", "1");
        Stream<String> other = Stream.of("1", "2", "2", "4");

        Stream<String> actual = StreamUtils.intersection(src, other);
        Stream<String> expected = Stream.of("1", "2", "4");

        Iterator<String> iter1 = actual.iterator();
        Iterator<String> iter2 = expected.iterator();

        while (iter1.hasNext() && iter2.hasNext())
            assertEquals(iter1.next(), iter2.next());
        assert !iter1.hasNext() && !iter2.hasNext();
    }

    @Test
    public void testHeaders() {
        Stream<String> headers = Stream.of("1", "2");

        Stream<Stream<String>> contents = Stream.of(
                Stream.of("abc", "def"),
                Stream.of("123", "456")
        );

        Stream<String> result = StreamUtils.headersContent(headers, contents);
        Stream<String> expected = Stream.of("1", "abc", "def", "2", "123", "456");

        Iterator<String> iter1 = result.iterator();
        Iterator<String> iter2 = expected.iterator();

        while (iter1.hasNext() && iter2.hasNext())
            assertEquals(iter1.next(), iter2.next());
        assert !iter1.hasNext() && !iter2.hasNext();
    }

    @Test
    public void testGameHeaders() {
        CountableRequest req = new CountableRequest(new HttpRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));

        Stream<String> headers = service.getCategories().map(category -> category.getName() + ":");

        Stream<Stream<String>> contents = service.getCategories()
                .map(Category::getGames)
                .map(games -> games.limit(10).map(Game::getName));

        Stream<String> result = StreamUtils.headersContent(headers, contents);

        result.forEach(System.out::println);
    }
}
