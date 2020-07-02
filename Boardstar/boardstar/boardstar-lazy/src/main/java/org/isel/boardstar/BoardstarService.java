package org.isel.boardstar;

import org.isel.boardstar.dto.CategoryDto;
import org.isel.boardstar.dto.GameDto;
import org.isel.boardstar.model.Artist;
import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static pt.isel.mpd.util.LazyQueries.*;

public class BoardstarService {
    public BgaWebApi api;
    private Iterable<CategoryDto> categories;

    public BoardstarService(BgaWebApi api) {
        this.api = api;
    }

    public Iterable<Category> getCategories() {
        if (categories == null) categories = cache(api.getCategories());
        return map(categories, this::toCategory);
    }

    public Iterable<Game> searchByCategories(String...categoriesIDs) {
        Iterable<Integer> pageNrs = iterate(1, n -> n + 1);
        return map(
                flatMap(
                        takeWhile(
                                map(pageNrs, (pageNr) -> api.searchByCategories(pageNr, categoriesIDs)),
                                this::isNotEmptyList),
                        list -> list),
                this::toGame);
    }

    public Iterable<Game> searchByArtist(String artist) {
        Iterable<Integer> pageNrs = iterate(1, n -> n + 1);
        return map(
                flatMap(
                        takeWhile(
                                map(pageNrs, (index) -> api.searchByArtist(index, artist)),
                                this::isNotEmptyList),
                        list->list),
                this::toGame);
    }

    /*
    * Helper methods
    * */

    private Category toCategory(CategoryDto dto) {
        return new Category(
                dto.getId(),
                dto.getName(),
                this.searchByCategories(dto.getId()));
    }

    private Game toGame(GameDto dto) {
        return new Game(
                dto.getId(),
                dto.getName(),
                dto.getYearPublished(),
                dto.getDescription(),
                toCategoryIterable(dto.getCategories()),
                toArtistIterable(dto.getArtists()));
    }

    private Iterable<Category> toCategoryIterable(List<CategoryDto> categoriesDto) {
        ArrayList<Category> categories = new ArrayList<>();
        for(CategoryDto c : categoriesDto){
            categories.add(toCategory(c));
        }
        return categories;
    }

    private Iterable<Artist> toArtistIterable(List<String> artistsNames) {
        Iterable<Integer> pageNrs = iterate(1, n -> n + 1);

        ArrayList<Artist> artists = new ArrayList<>();
        for(String name : artistsNames) {
            artists.add(new Artist(name, searchByArtist(name)));
        }
        return artists;
    }

    private <T> boolean isNotEmptyList(List<T> list) {
        return list.size() != 0;
    }
}
