/*
 * GNU General Public License v3.0
 *
 * Copyright (c) 2020, Miguel Gamboa (gamboa.pt)
 *
 *   All rights granted under this License are granted for the term of
 * copyright on the Program, and are irrevocable provided the stated
 * conditions are met.  This License explicitly affirms your unlimited
 * permission to run the unmodified Program.  The output from running a
 * covered work is covered by this License only if the output, given its
 * content, constitutes a covered work.  This License acknowledges your
 * rights of fair use or other equivalent, as provided by copyright law.
 *
 *   You may make, run and propagate covered works that you do not
 * convey, without conditions so long as your license otherwise remains
 * in force.  You may convey covered works to others for the sole purpose
 * of having them make modifications exclusively for you, or provide you
 * with facilities for running those works, provided that you comply with
 * the terms of this License in conveying all material for which you do
 * not control copyright.  Those thus making or running the covered works
 * for you must do so exclusively on your behalf, under your direction
 * and control, on terms that prohibit them from making any copies of
 * your copyrighted material outside their relationship with you.
 *
 *   Conveying under any other circumstances is permitted solely under
 * the conditions stated below.  Sublicensing is not allowed; section 10
 * makes it unnecessary.
 */

package org.isel.boardstar;

import com.google.gson.Gson;
import org.isel.boardstar.dto.CategoryDto;
import org.isel.boardstar.dto.GameDto;
import org.isel.boardstar.dto.GetCategoriesDto;
import org.isel.boardstar.dto.SearchDto;
import pt.isel.mpd.util.Request;
import java.util.List;
import java.util.stream.Stream;

public class BgaWebApi {
    final static int PAGE_LIMIT = 5;
    final static String CLIENT_ID = "jJWH7Qb0lD";
    final static String BGA_PATH = "https://www.boardgameatlas.com/api/";
    final static String GET_CATEGORIES_PATH = BGA_PATH + "game/categories?client_id=" + CLIENT_ID;
    final static String BGA_SEARCH_PATH_CATEGORIES = BGA_PATH + "search?limit=" + PAGE_LIMIT + "&skip=%d" + "&categories=%s&client_id=" + CLIENT_ID;
    final static String BGA_SEARCH_PATH_ARTIST = BGA_PATH + "search?limit=" + PAGE_LIMIT + "&skip=%d" + "&artist=%s&client_id=" + CLIENT_ID;

    private final Request req;
    private final Gson gson;

    public BgaWebApi(Request req) {
        this(req, new Gson());
    }

    public BgaWebApi(Request req, Gson gson) {
        this.req = req;
        this.gson = gson;
    }

    public List<CategoryDto> getCategories() {
        String path = GET_CATEGORIES_PATH;
        String body = String.join("", req.getLines(path));
        GetCategoriesDto dto = gson.fromJson(body, GetCategoriesDto.class);
        return dto.getCategories();
    }

    public List<GameDto> searchByCategories(int page, String... categoriesIDs) {
        page = (page - 1) * PAGE_LIMIT;
        StringBuilder categories = new StringBuilder();

        for (int i = 0; i < categoriesIDs.length; i++) {
            categories.append(categoriesIDs[i]);
            if (i != categoriesIDs.length-1)
                categories.append(",");
        }

        String path = String.format(BGA_SEARCH_PATH_CATEGORIES, page, categories.toString());
        String body = String.join("", req.getLines(path));
        SearchDto dto = gson.fromJson(body, SearchDto.class);
        return dto.getGames();
    }

    public List<GameDto> searchByArtist(int page, String artist) {
        page = (page - 1) * PAGE_LIMIT;
        String path = String.format(BGA_SEARCH_PATH_ARTIST, page, artist).replace(" ", "%20");
        String body = String.join("", req.getLines(path));
        SearchDto dto = gson.fromJson(body, SearchDto.class);
        return dto.getGames();
    }
}
