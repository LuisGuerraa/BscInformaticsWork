'use strict';
const util = require('./cota-utils.js')
const fetch = require('node-fetch')

function getPopularSeries(page) {
    return fetch(`${util.API_URL_START}/3/tv/popular?api_key=${util.API_KEY}&language=${util.LANGUAGE}&page=${page}`)
        .then(response => response.json())
        .then(body => {
            let hit = body.results || [];
            if (hit.length) return hit.map(e => ({
                "name": e.name,
                "overview": e.overview,
                "votes_average": e.vote_average * 10,
                "poster_path" : util.API_IMAGE_START.concat(e.poster_path)
            }));
            return undefined;
        })
        .catch(() => Promise.reject("Error in GetPopularSeries process"))
}

function getSeriesByName(name) {
    return fetch(`${util.API_URL_START}/3/search/tv/?api_key=${util.API_KEY}&language=${util.LANGUAGE}&query=${name}`)
        .then(response => response.json())
        .then(body => {
            let hit = body.results;
            if (hit.length) {
                hit.forEach(series => {
                    series.vote_average = series.vote_average * 10;
                    series.poster_path = util.API_IMAGE_START.concat(series.poster_path);
                });
                return hit;
            }
            return undefined;
        })
        .catch(() => Promise.reject("Error in GetSeriesByName process"))
}

module.exports = {
    getPopularSeries: getPopularSeries,
    getSeriesWithName: getSeriesByName
};