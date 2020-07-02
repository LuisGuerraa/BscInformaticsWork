package pt.isel.mpd;

import org.isel.boardstar.BgaWebApi;

import org.isel.boardstar.dto.CategoryDto;
import org.isel.boardstar.dto.GameDto;

import org.junit.Test;
import static org.junit.Assert.*;

import pt.isel.mpd.util.AsyncRequest;
import pt.isel.mpd.util.request.AsyncHttpRequest;
import pt.isel.mpd.util.request.HttpRequest;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BgaWebApiTest {

    @Test
    public void getCategoriesTest() {
        AsyncRequest req = new AsyncHttpRequest();
        BgaWebApi api = new BgaWebApi(req);
        CompletableFuture<List<CategoryDto>> cf = api.getCategories();
        List<CategoryDto> categories = cf.join();

        boolean found = false;

        for (CategoryDto category : categories) {
            if (category.getId().equals("FmGV9rVu1c") && category.getName().equals("Zombies") && !category.isChecked()) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void searchByCategoriesTest() {
        AsyncRequest req = new AsyncHttpRequest();
        BgaWebApi api = new BgaWebApi(req);
        CompletableFuture<List<GameDto>> cf = api.searchByCategories(1, "7rV11PKqME", "O0ogzwLUe8");
        List<GameDto> games = cf.join();
        List<String> gamesNames = new LinkedList<>();
        for (GameDto dto : games)
            gamesNames.add(dto.getName());
        List<String> expected = Arrays.asList("Catan", "Monopoly Deal Card Game", "Munchkin Deluxe", "Paper Tales: Beyond The Gates Expansion", "Unicorn Rescue Society: The Card Game");
        assertEquals(expected, gamesNames);
    }

    @Test
    public void searchByArtistTest() {
        AsyncRequest req = new AsyncHttpRequest();
        BgaWebApi api = new BgaWebApi(req);
        CompletableFuture<List<GameDto>> cf = api.searchByArtist(1, "Dimitri%20Bielak");
        List<GameDto> games = cf.join();
        List<String> gamesNames = new LinkedList<>();
        for (GameDto dto : games)
            gamesNames.add(dto.getName());
        List<String> expected = Arrays.asList("Inis", "Kemet", "Kemet: Ta-Seti", "Relic", "The Gathering Storm");
        assertEquals(expected, gamesNames);
    }
}
