'use strict';
const SERVER_HOST = "localhost";
const SERVER_PORT = 8080;
const SERVER_URI = `http://${SERVER_HOST}:${SERVER_PORT}/`;

const ES_HOST = "localhost";
const ES_PORT = 9200;
const ES_URI = `http://${ES_HOST}:${ES_PORT}`;

const API_URL_START = "https://api.themoviedb.org";
const API_KEY = '0c9b2502c13f4dcc2217113f2adf3788';
const LANGUAGE = 'en-US';
const API_IMAGE_START = "https://image.tmdb.org/t/p/w500";

const MOCHA_TIMEOUT = 10000000;

function error(detailedError, shortDescription) {
    return Promise.reject({
        detail: detailedError,
        short: shortDescription
    })
}

function success(message, shortDescription, data) {
    return {
        message: message,
        short: shortDescription,
        data: data
    }
}

module.exports = {
    success: success,
    error: error,
    SERVER_PORT: SERVER_PORT,
    ES_HOST: ES_HOST,
    ES_PORT: ES_PORT,
    ES_URI: ES_URI,
    MOCHA_TIMEOUT: MOCHA_TIMEOUT,
    API_URL_START: API_URL_START,
    API_KEY: API_KEY,
    LANGUAGE: LANGUAGE,
    API_IMAGE_START: API_IMAGE_START

};