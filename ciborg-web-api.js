module.exports = function(gameService) {
    
    if (!gameService)
        throw "Invalid services."


    return {
        deserializeUser: deserializeUser,
        serializeUser: serializeUser,
        validateLogin: validateLogin,
        verifyAuthenticated: verifyAuthenticated,
        registerUser: registerUser,
        logout: logout,
        getPopularGames: getPopularGames,
        getGamesWithName: getGamesWithName,
        getGroups: getGroups,
        getGroup: getGroup,
        getGamesBetweenPlaytime: getGamesBetweenPlaytime,
        createGroup: createGroup,
        updateGroup: updateGroup,
        addGameToGroup: addGameToGroup,
        deleteGameFromGroup: deleteGameFromGroup,
        deleteGroup: deleteGroup
    }
    function deserializeUser(user, done) {
        //console.log("deserializeUserCalled")
        done(null, user)
    }
      
    function serializeUser(user, done) {
        //console.log("serializeUserCalled")
        done(null, user)
    }
    
    function verifyAuthenticated(req, rsp) {
        rsp.json({result: req.isAuthenticated(), username: req.body.username})
    }
    
    function validateLogin(req, rsp) {
        validateUser(req.body.username, req.body.password)
            .then(() => {
                req.logIn({
                    username: req.body.username
                 }, (errObj) => rsp.json({result: true, username: req.body.username}))
            })
            .catch(() => {
                rsp.json({result: false, username: req.body.username})
            })
      
        function validateUser(username, password) { 
            return new Promise(function(resolve, reject) {
                gameService.getUser(username)
                    .then(function (respObj) {
                        if (password == respObj.body.password)
                            resolve()
                        else
                            reject()
                    })
                    .catch(function (errObj) {
                        reject()
                    })
            })
         }
    }

    function registerUser(req, rsp) {
        gameService.registerUser(req.body.username, req.body.password)
            .then(function (respObj) {
                rsp.statusCode = respObj.statusCode
                rsp.json(respObj)
            })
            .catch(function (errObj) {
                rsp.statusCode = errObj.statusCode
                rsp.json(errObj)
            })
    }

    function logout(req, rsp) {
        req.logOut()
        rsp.json({message: "Logged out."})
    }

    function getPopularGames(req, rsp) {
        gameService.getPopularGames()
            .then(function (respObj) {
                rsp.statusCode = respObj.statusCode
                rsp.json(respObj)
            })
            .catch(function (errObj) {
                rsp.statusCode = errObj.statusCode
                rsp.json(errObj)
            })
    }

    function getGamesWithName(req, rsp) {
        gameService.getGamesWithName(req.params.gameName)
            .then(function (respObj) {
                rsp.statusCode = respObj.statusCode
                rsp.json(respObj)
            })
            .catch(function (errObj) {
                rsp.statusCode = errObj.statusCode
                rsp.json(errObj)
            })
    }

    function getGroups(req, rsp) {
        gameService.getGroups(req.user.username)
            .then(function(respObj) {
                rsp.statusCode = respObj.statusCode
                respObj.username = req.user.username
                rsp.json(respObj)
            })
            .catch(function (errObj) {
                rsp.statusCode = errObj.statusCode
                errObj.username = req.user.username
                rsp.json(errObj)
            });
    }

    function getGroup(req, rsp) {
        gameService.getGroup(req.user.username, req.params.groupName)
            .then(function (respObj) {
                rsp.statusCode = respObj.statusCode
                rsp.json(respObj)
            })
            .catch(function (errObj) {
                rsp.statusCode = errObj.statusCode
                rsp.json(errObj)
            })
    }

    function getGamesBetweenPlaytime(req, rsp) {
        gameService.getGamesBetweenPlaytime(req.user.username, req.params.groupName, req.params.minPlaytime, req.params.maxPlaytime)
            .then(function (respObj) {
                rsp.statusCode = respObj.statusCode
                rsp.json(respObj)
            })
            .catch(function (errObj) {
                rsp.statusCode = errObj.statusCode
                rsp.json(errObj)
            })
    }

    function createGroup(req, rsp) {
        gameService.createGroup(req.user.username, req.body.name, req.body.desc)
            .then(function (respObj) {
                rsp.statusCode = respObj.statusCode
                rsp.json(respObj)
            })
            .catch(function (errObj) {
                rsp.statusCode = errObj.statusCode
                rsp.json(errObj)
            })
    }

    function updateGroup(req, rsp) {
        gameService.updateGroup(req.user.username, req.params.groupName, req.body.name, req.body.desc)
            .then(function (respObj) {
                rsp.statusCode = respObj.statusCode
                rsp.json(respObj)
            })
            .catch(function (errObj) {
                rsp.statusCode = errObj.statusCode
                rsp.json(errObj)
            })
    }

    function addGameToGroup(req, rsp) {
        gameService.addGameToGroup(req.user.username, req.params.groupName, req.params.gameName)
            .then(function (respObj) {
                rsp.statusCode = respObj.statusCode
                rsp.json(respObj)
            })
            .catch(function (errObj) {
                rsp.statusCode = errObj.statusCode
                rsp.json(errObj)
            })
    }

    function deleteGameFromGroup(req, rsp) {
        gameService.deleteGameFromGroup(req.user.username, req.params.groupName, req.params.gameName)
            .then(function (respObj) {
                rsp.statusCode = respObj.statusCode
                rsp.json(respObj)
            })
            .catch(function (errObj) {
                rsp.statusCode = errObj.statusCode
                rsp.json(errObj)
            })
    }

    function deleteGroup(req, rsp) {
        gameService.deleteGroup(req.user.username, req.params.groupName)
            .then(function (respObj) {
                rsp.statusCode = respObj.statusCode
                rsp.json(respObj)
            })
            .catch(function (errObj) {
                rsp.statusCode = errObj.statusCode
                rsp.json(errObj)
            })
    }
}