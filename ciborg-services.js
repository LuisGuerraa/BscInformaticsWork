module.exports = function(api, db, utils) {

    if (!api)
        throw "Invalid API."

    if (!db)
        throw "Invalid DB."
    
    if (!utils)
        throw "Invalid utils."

    return {
        getUser: getUser,
        registerUser: registerUser,
        getPopularGames: getPopularGames,
        getGroups: getGroups,
        getGroup: getGroup,
        getGamesWithName: getGamesWithName,
        getGamesBetweenPlaytime: getGamesBetweenPlaytime,
        createGroup: createGroup,
        updateGroup: updateGroup,
        addGameToGroup: addGameToGroup,
        deleteGameFromGroup: deleteGameFromGroup,
        deleteGroup: deleteGroup
    }

    function getUser(username) {
        return db.getUser(username)
    }

    function registerUser(username, password) {
        return db.getUser(username)
            .then(function (respObj) {
                return Promise.reject(utils.getRespObj(409, "User already registered."))
            })
            .catch(function (errObj) {
                if (errObj.statusCode == 404)
                    return db.registerUser(username, password)
                else
                    return Promise.reject(errObj)
            })
    }

    function getPopularGames() {
        return api.getPopularGames()
    }

    function getGroups(username) {
        return db.getGroups(username)
    }

    function getGroup(username, groupName) {
        return db.getGroup(username, groupName)
    }

    function getGamesWithName(gameName) {
        return api.getGamesWithName(gameName)
    }

    function getGamesBetweenPlaytime(username, groupName, minPlaytime, maxPlaytime) {
        return db.getGroup(username, groupName)
            .then(function (respObj) {
                    games = respObj.body.games
                    games = games.filter(game => parseInt(game.minPlaytime) > parseInt(minPlaytime) &&
                                                 parseInt(game.maxPlaytime) < parseInt(maxPlaytime));
                    if (games.length > 0)
                        return Promise.resolve(utils.getRespObj(200, "Games obtained successfully.", games))
                    else
                        return Promise.reject(utils.getRespObj(404, "No games found."))
            })
            .catch(function (errObj) {
                return Promise.reject(errObj)
            })
    }

    function createGroup(username, groupName, groupDesc) {
        return db.getGroup(username, groupName)
            .then(function (respObj) {
                return Promise.reject(utils.getRespObj(409, "Group already exists."))
            })
            .catch(function (errObj) {
                if (errObj.statusCode == 404)
                    return db.createGroup(username, groupName, groupDesc)
                return Promise.reject(errObj)
            })
    }

    function updateGroup(username, oldGroupName, newGroupName, newGroupDesc) {
        return db.getGroup(username, oldGroupName)
            .then(function(respObj) {
                return db.updateGroup(username, oldGroupName, newGroupName, newGroupDesc)
            })
            .catch(function(errObj) {
                if (errObj.statusCode == 404)
                    return Promise.reject(utils.getRespObj(404, "Group does not exist."))
                return Promise.reject(errObj)
            })
    }

    function addGameToGroup(username, groupName, gameName) {
        return db.getGroup(username, groupName)
            .then(function(respObj) {
                return api.getGamesWithName(gameName)
                    .then(function(respObj) {
                        return db.getIndexOfGameInGroup(username, groupName, gameName)
                            .then(function(respObj) {
                                return Promise.reject(utils.getRespObj(409, "Game already exists in this group."))
                            })
                            .catch(function(errObj) {
                                if (errObj.statusCode == 404) {
                                    return db.addGameToGroup(username, groupName, respObj.body[0])
                                }
                                return Promise.reject(errObj)
                            })
                    })
                    .catch(function(errObj) {
                        return Promise.reject(errObj)
                    })
            })
            .catch (function(errObj) {
                return Promise.reject(errObj)
            })
    }

    function deleteGameFromGroup(username, groupName, gameName) {
        return db.getGroup(username, groupName)
            .then(function(respObj) {
                return db.getIndexOfGameInGroup(username, groupName, gameName)
                    .then(function(respObj) {
                        return db.deleteGameFromGroup(username, groupName, respObj.body)
                    })
                    .catch(function(errObj) {
                        return Promise.reject(errObj)
                    })
            })
            .catch(function(errObj) {
                return Promise.reject(errObj)
            })
    }

    function deleteGroup(username, groupName) {
        return db.getGroup(username, groupName)
            .then(function(respObj) {
                return db.deleteGroup(username, groupName)
            })
            .catch(function(errObj) {
                if (errObj.statusCode == 404)
                    return Promise.reject(utils.getRespObj(404, "Group does not exist."))
                return Promise.reject(errObj)
            })
    }
}