module.exports = function(request, utils) {

    if (!request)
        throw "Invalid request."

    if (!utils)
        throw "Invalid utils."

    return {
        getPopularGames: getPopularGames,
        getGamesWithName: getGamesWithName
    }

    function getPopularGames() {
        return new Promise(function(resolve, reject) {
            
            const options = {
                'method': 'GET',
                'uri': `${utils.API_URL_START}&order_by=popularity${utils.API_URL_END}`,
                'json': true
            }

            request.get(options, (err, res, body) => {
                if(err == null) {
                    popularGamesObj = body
                    games = []
                    for(i = 0; i < popularGamesObj.games.length; i++) {
                        g = {}
                        g.id = popularGamesObj.games[i].id
                        g.name = popularGamesObj.games[i].name
                        g.desc = popularGamesObj.games[i].description
                        g.rating = parseFloat(popularGamesObj.games[i].average_user_rating).toFixed(1)
                        g.image = popularGamesObj.games[i].image_url
                        g.minPlaytime = popularGamesObj.games[i].min_playtime
                        g.maxPlaytime = popularGamesObj.games[i].max_playtime
                        games.push(g)
                    }
                    resolve(utils.getRespObj(200, "Games obtained successfully.", games))
                } else 
                    reject(utils.getRespObj(503))
            });
        })
    }

    function getGamesWithName(gameName) {
        return new Promise(function(resolve, reject) {
            const options = {
                'method': 'GET',
                'uri': `${utils.API_URL_START}&name=${gameName}${utils.API_URL_END}`,
            }
        
            request.get(options, (err, res, body) => {
                
                if (err == null) {
                    gamesObj = JSON.parse(body)
                    games = []
                    for(i = 0; i < gamesObj.games.length; i++) {
                        g = {}
                        g.id = gamesObj.games[i].id
                        g.name = gamesObj.games[i].name
                        g.desc = gamesObj.games[i].description
                        g.rating = parseFloat(gamesObj.games[i].average_user_rating).toFixed(1)
                        g.image = gamesObj.games[i].image_url
                        g.minPlaytime = gamesObj.games[i].min_playtime
                        g.maxPlaytime = gamesObj.games[i].max_playtime
                        games.push(g)
                    }
                    if (games.length == 0)
                        reject(getErrObj(404, "Game not found"))
                    
                    resolve(utils.getRespObj(200, "Games obtained successfully.", games))
                } else {
                    reject(utils.getRespObj(503))
                }
            });
        })
    }
}