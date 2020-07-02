package org.isel.boardstar;

import org.isel.boardstar.dto.CategoryDto;
import org.isel.boardstar.dto.GameDto;
import org.isel.boardstar.model.Artist;
import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BoardstarService {
    public BgaWebApi api;
    private Supplier<Stream<CategoryDto>> categories;

    public BoardstarService(BgaWebApi api) {
        this.api = api;
    }

    public Stream<Category> getCategories() {
        if (categories == null) categories = StreamUtils.cache(api.getCategories().stream());
        return categories.get().map(this::toCategory);
    }

    public Stream<Game> searchByCategories(String...categoriesIDs) {
        return Stream.iterate(1, n -> n + 1)
                .map(pageNr -> api.searchByCategories(pageNr, categoriesIDs))
                .takeWhile(gameDto -> gameDto.size() != 0)
                .flatMap(Collection::stream)
                .map(this::toGame);
    }

    public Stream<Game> searchByArtist(String artist) {
        return Stream.iterate(1, n -> n + 1)
                .map(pageNr -> api.searchByArtist(pageNr, artist))
                .takeWhile(gameDto -> gameDto.size() != 0)
                .flatMap(Collection::stream)
                .map(this::toGame);
    }

    /*
    * Helper methods
    * */
    private Category toCategory(CategoryDto dto) {
        Supplier<Stream<Game>> games = () -> searchByCategories(dto.getId());
        return new Category(
                dto.getId(),
                dto.getName(),
                games);
    }

    private Game toGame(GameDto dto) {
        Supplier<Stream<Category>> categories = () -> dto.getCategories().stream().map(this::toCategory);
        Supplier<Stream<Artist>> artists = () -> dto.getArtists().stream()
                .map(artist -> new Artist(artist, () -> searchByArtist(artist)));
        return new Game(
                dto.getId(),
                dto.getName(),
                dto.getYearPublished(),
                dto.getDescription(),
                categories,
                artists);
    }
}
