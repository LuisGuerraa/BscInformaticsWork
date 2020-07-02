package org.isel.boardstar;

import io.reactivex.rxjava3.core.Observable;
import org.isel.boardstar.dto.CategoryDto;
import org.isel.boardstar.dto.GameDto;
import org.isel.boardstar.model.Artist;
import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class BoardstarService {
    public BgaWebApi api;

    public BoardstarService(BgaWebApi api) {
        this.api = api;
    }

    public Observable<Category> getCategories() {
        return fromCF(api.getCategories())
                .flatMap(Observable::fromIterable)
                .map(this::toCategory);
    }

    public Observable<Game> searchByCategories(int totalElems, String...categoriesIDs) {
        int count = getNumberOfRequests(totalElems, api.getPageLimit());
        return Observable
                .intervalRange(1, count + 1, 0, 2000, TimeUnit.MILLISECONDS)
                .map(pageNr -> api.searchByCategories(pageNr.intValue(), categoriesIDs))
                .flatMap(BoardstarService::fromCF)
                .flatMap(Observable::fromIterable)
                .map(this::toGame);
    }


    public Observable<Game> searchByArtist(int totalElems, String artist) {
        int count = getNumberOfRequests(totalElems, api.getPageLimit());
        return Observable
                .intervalRange(1, count + 1, 0, 2000, TimeUnit.MILLISECONDS)
                .map(pageNr -> api.searchByArtist(pageNr.intValue(), artist))
                .flatMap(BoardstarService::fromCF)
                .flatMap(Observable::fromIterable)
                .map(this::toGame);
    }

    /*
    * Helper methods
    * */

    private int getNumberOfRequests(int totalElems, int pageElems) {
        int count = totalElems / pageElems;
        if (totalElems % pageElems == 0) count--;
        return count;
    }

    private Category toCategory(CategoryDto dto) {
        return new Category(
                dto.getId(),
                dto.getName(),
                totalElems -> searchByCategories(totalElems, dto.getId()));
    }

    private Game toGame(GameDto dto) {
        return new Game(
                dto.getId(),
                dto.getName(),
                dto.getYearPublished(),
                dto.getDescription(),
                Observable.fromIterable(dto.getCategories()).map(this::toCategory),
                Observable.fromIterable(dto.getArtists())
                        .map(artist -> new Artist(artist, totalElems -> searchByArtist(totalElems, artist)))
        );
    }

    private static <T> Observable<T> fromCF(CompletableFuture<T> cf) {
        return Observable.create(subscriber -> cf
                .thenAccept(item -> {
                    subscriber.onNext(item);
                    subscriber.onComplete();
                })
                .exceptionally(err -> {
                    subscriber.onError(err);
                    return null;
                })
        );
    }
}
