module.exports = function(request, utils) {

    if (!request)
        throw "Invalid request."

    if (!utils)
        throw "Invalid utils."

    checkUsersIndexExists()
        .then(function(status) {
            if (status != 200)
                createUsersIndex()
        })
        .catch(function(err) {
            console.log(err)
        })

    return {
        getUser: getUser,
        registerUser: registerUser,
        getGroups: getGroups,
        getGroup: getGroup,
        createGroup: createGroup,
        updateGroup: updateGroup,
        addGameToGroup: addGameToGroup,
        getIndexOfGameInGroup: getIndexOfGameInGroup,
        deleteGameFromGroup: deleteGameFromGroup,
        deleteGroup: deleteGroup
    }
    
    function checkUsersIndexExists() {
        return new Promise(function(resolve, reject) {
            const options = utils.requestDatabaseOptions('HEAD', 'users', null)

            request.head(options, (err, res, body) => {
                if (err == null)    
                    resolve(res.statusCode)
                else
                    reject("[WARNING] ElasticSearch is not running.")
            })
        })
    }

    function createUsersIndex() {
        const options = utils.requestDatabaseOptions('PUT', 'users', {
            "mappings": {
                "properties": {
                    "username": { "type": "text" },
                    "groups": {
                        "properties": {
                            "name": { "type": "text" },
                            "desc": { "type": "text" },
                            "games": {
                                "properties": {
                                    "id": { "type": "text" },
                                    "name": { "type": "text" },
                                    "image": { "type": "text" },
                                    "min_playtime": { "type": "text" },
                                    "max_playtime": { "type": "text" }
                                }
                            }
                        }
                    }
                }
            }
        })

        request.put(options, (err, res, body) => {
            if (err == null)
                console.log("[INFO] Index was created successfuly.")
            else 
                console.log("[WARNING] ElasticSearch is not running.")
        });
    }

    function getUser(username) {
        return new Promise(function(resolve, reject) {
            const options = utils.requestDatabaseOptions('GET', 'users/_search', {
                "query": {
                    "match" : {
                        "username" : username
                    }
                }
            })

            request.get(options, (err, res, body) => {
                if (err == null) {
                    if (body.hits.hits.length > 0)
                        resolve(utils.getRespObj(200, "User obtained successfully.", body.hits.hits[0]._source))
                    else
                        reject(utils.getRespObj(404, "User not found."))
                } else 
                    reject(utils.getRespObj(503))
            });
        })
    }

    function registerUser(username, password) {
        return new Promise(function(resolve, reject) {
            const options = utils.requestDatabaseOptions('POST', 'users/_doc', {
                "username": username,
                "password": password,
                "groups": []
            })

            request.post(options, (err, res, body) => {
                if (err == null) {
                    resolve(utils.getRespObj(201, "User registered successfully."))
                } else {
                    reject(utils.getRespObj(503))
                }
            });
        })
    }

    function getGroups(username) {
        return new Promise(function(resolve, reject) {
            const options = utils.requestDatabaseOptions('GET', 'users/_search', {
                "query": {
                    "match" : {
                        "username" : username
                    }
                }
            })

            request.get(options, (err, res, body) => {
                
                if (err == null) {
                    groups = body.hits.hits[0]._source.groups
                    
                    if (groups.length > 0)
                        resolve(utils.getRespObj(200, "Groups obtained successfully.", groups))

                    else 
                        reject(utils.getRespObj(404, "No groups found."))
                    
                } else 
                    reject(utils.getRespObj(503))
                
            });
        })
    }

    function getGroup(username, groupName) {
        return new Promise(function(resolve, reject) {
            const options = utils.requestDatabaseOptions('GET', 'users/_search', {
                "size": 1,
                "query": {
                    "match": {
                        "username": username
                    }
                }
            })

            request.get(options, (err, res, body) => {
                if (err == null) {
                    groups = body.hits.hits[0]._source.groups
                    group = null
                    for (i = 0; i < groups.length; i++)
                        if (groups[i].name == groupName)
                            group = groups[i]

                    if (group)
                        resolve(utils.getRespObj(200, "Group obtained successfully.", group))
                    else
                        reject(utils.getRespObj(404, "Group does not exist."))
                } else 
                    reject(utils.getRespObj(503))

            });
        })
    }

    function createGroup(username, groupName, groupDesc) {
        return new Promise(function(resolve, reject) {
            const options = utils.requestDatabaseOptions('POST', 'users/_doc/_update_by_query', {
                "query": { 
                    "match": {
                        "username": username
                    }
                },
                "script": {
                    "lang": "painless",
                    "inline": "ctx._source.groups.add(params.newGroup)",
                    "params": {
                        "newGroup": {
                            "name": groupName,
                            "desc": groupDesc,
                            "games": []
                        }
                    }
                }
            })

            request.post(options, (err, res, body) => {
                if (err == null) 
                    resolve(utils.getRespObj(201, "Group created."))
                else 
                    reject(utils.getRespObj(503))
            });
        })
    }

    function updateGroup(username, oldGroupName, newGroupName, newGroupDesc) {
        return new Promise(function(resolve, reject) {
            const options = utils.requestDatabaseOptions('POST', 'users/_doc/_update_by_query', {
                "query": {
                    "match" : {
                        "username" : username
                    }
                },
                "script": { 
                    "source": `for (int i=0;i<ctx._source.groups.size();i++) {
                                    if (ctx._source.groups[i][\"name\"] == '${oldGroupName}') { 
                                        ctx._source.groups[i][\"name\"] = '${newGroupName}';
                                        ctx._source.groups[i][\"desc\"] = '${newGroupDesc}';
                                    } 
                                }`
                }
            })

            request.post(options, (err, res, body) => {
                if (err == null) 
                    resolve(utils.getRespObj(200, "Group updated."))
                else
                    reject(utils.getRespObj(503))    
            });
        })
    }

    function addGameToGroup(username, groupName, gameObj) {
        return new Promise(function(resolve, reject) {
            const options = utils.requestDatabaseOptions('POST', 'users/_doc/_update_by_query', {
                "query": {
                    "match": {
                        "username": username
                    }
                },
                "script": {
                    "lang": "painless",
                    "inline": `for (int i = 0; i < ctx._source.groups.size(); i++) {
                                    if (ctx._source.groups[i][\"name\"] == '${groupName}') {
                                        ctx._source.groups[i][\"games\"].add(params.newGame);
                                    }
                                }`,
                    "params": {
                        "newGame": gameObj
                    }
                }
            })

            request.post(options, (err, res, body) => {
                if (err == null) 
                    resolve(utils.getRespObj(201, "Game added to group successfully."))
                else 
                    reject(utils.getRespObj(503))
            });
        })
    }

    function getIndexOfGameInGroup(username, groupName, gameName) {
        return new Promise(function(resolve, reject) {
            const options = utils.requestDatabaseOptions('GET', 'users/_search', {
                "size": 1,
                "query": {
                    "match": {
                        "username": username
                    }
                }
            })

            request.get(options, (err, res, body) => {
                if (err == null) {
                    gameIdx = null
                    group = {}
                    groups = body.hits.hits[0]._source.groups

                    for (i = 0; i < groups.length; i++)
                        if (groups[i]["name"] == groupName)
                            group = groups[i]

                    for (i = 0; i < group.games.length; i++) 
                        if (group.games[i].name == gameName) 
                            gameIdx = i
                        
                    if (gameIdx == null)
                        reject(utils.getRespObj(404, "Game does not exist in this group."))
                    else
                        resolve(utils.getRespObj(200, "Index obtained successfully.", gameIdx))
                } else {
                    reject(utils.getRespObj(503))
                }
            });
        })
    }

    function deleteGameFromGroup(username, groupName, gameIdx) {
        return new Promise(function(resolve, reject) {
            const options = utils.requestDatabaseOptions('POST', 'users/_doc/_update_by_query', {
                "query": {
                    "match": {
                        "username": username
                    }
                },
                "script": {
                    "lang": "painless",
                    "source": `for (int i = 0; i < ctx._source.groups.size(); i++) {
                                    if (ctx._source.groups[i][\"name\"] == '${groupName}') {
                                        ctx._source.groups[i][\"games\"].remove(${gameIdx});
                                    }
                                }`
                }
            })

            request.post(options, (err, res, body) => {
                if (err == null)
                    resolve(utils.getRespObj(200, "Game removed from group successfully."))
                else 
                    reject(utils.getRespObj(503))
            });
        })
    }

    function deleteGroup(username, groupName) {
        return new Promise(function(resolve, reject) {
            const options = utils.requestDatabaseOptions('POST', 'users/_doc/_update_by_query', {
                "query": {
                    "match": {
                        "username": username
                    }
                },
                "script": {
                    "lang": "painless",
                    "source": `for (int i = 0; i < ctx._source.groups.size(); i++) {
                                    if (ctx._source.groups[i][\"name\"] == '${groupName}') {
                                        ctx._source.groups.remove(i);
                                    }
                                }`
                }
            })

            request.post(options, (err, res, body) => {
                if (err == null) 
                    resolve(utils.getRespObj(200, "Group deleted."))
                else 
                    reject(utils.getRespObj(503))
            });
        })
    }  
}