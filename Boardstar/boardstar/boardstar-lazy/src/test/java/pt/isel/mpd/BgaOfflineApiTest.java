package pt.isel.mpd;

import org.isel.boardstar.BgaWebApi;
import org.isel.boardstar.dto.CategoryDto;
import org.isel.boardstar.dto.GameDto;
import org.junit.Test;
import pt.isel.mpd.util.request.AbstractRequest;
import pt.isel.mpd.util.request.HttpRequest;
import pt.isel.mpd.util.request.MockRequest;

import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BgaOfflineApiTest {

    static class RequestMediator extends AbstractRequest {
        private AbstractRequest req;
        int count;

        public RequestMediator(AbstractRequest req) {
            this.req = req;
        }

        public InputStream getStream(String path) {
            count++;
            return req.getStream(path);
        }

        public Reader getReader(String path) {
            return null;
        }
    }

    @Test
    public void getCategoriesTest() {
        RequestMediator req = new RequestMediator(new MockRequest());
        BgaWebApi api = new BgaWebApi(req);
        List<CategoryDto> categories = api.getCategories();

        boolean found = false;

        for (CategoryDto category : categories) {
            if (category.getId().equals("WnxKtlGfdR") && category.getName().equals("123") && !category.isChecked()) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void searchByCategoriesTest() {
        BgaWebApiTest.RequestMediator req = new BgaWebApiTest.RequestMediator(new MockRequest());
        BgaWebApi api = new BgaWebApi(req);
        List<GameDto> games = api.searchByCategories(1, "7rV11PKqME", "O0ogzwLUe8");
        List<String> gamesNames = new LinkedList<>();
        for (GameDto dto : games)
            gamesNames.add(dto.getName());
        List<String> expected = Arrays.asList("Catan", "Monopoly Deal Card Game", "Munchkin Deluxe", "Unicorn Rescue Society: The Card Game", "NANO Corporation");
        assertEquals(expected, gamesNames);
    }

    @Test
    public void searchByArtistTest() {
        BgaWebApiTest.RequestMediator req = new BgaWebApiTest.RequestMediator(new MockRequest());
        BgaWebApi api = new BgaWebApi(req);
        List<GameDto> games = api.searchByArtist(1, "Dimitri%20Bielak");
        List<String> gamesNames = new LinkedList<>();
        for (GameDto dto : games)
            gamesNames.add(dto.getName());
        List<String> expected = Arrays.asList("Inis", "Kemet", "Kemet: Ta-Seti", "Relic", "The Gathering Storm");
        assertEquals(expected, gamesNames);
    }
}
