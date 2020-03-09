function GroupsApiUris() {
    const baseUri = "http://localhost/"

    this.getGames        = (gameName)  => `${baseUri}games/${gameName}`
    this.getPopularGames = () => `${baseUri}games/popular`
    this.getGroups   = () => `${baseUri}groups`
    this.getGroup        = (groupName) => `${baseUri}groups/${groupName}`
    this.createGroup     = () => `${baseUri}groups`
    this.deleteGroup     = (groupName) => `${baseUri}groups/${groupName}`
    this.addGameToGroup  = (groupName, gameName) => `${baseUri}groups/${groupName}/games/${gameName}`
    this.deleteGameFromGroup = (groupName, gameName) => `${baseUri}groups/${groupName}/games/${gameName}`
    this.getGamesFromGroupBetweenPlaytime = (groupName, minPlaytime, maxPlaytime) => `${baseUri}groups/${groupName}/games/${minPlaytime}&${maxPlaytime}`
    this.updateGroup     = (groupName) => `${baseUri}groups/${groupName}`
    this.login           = () => `${baseUri}login`
    this.logout          = () => `${baseUri}logout`
    this.register        = () => `${baseUri}register`
    this.auth            = () => `${baseUri}auth`
}
  
const apiUris = new GroupsApiUris();

function getGames(gameName) {
    return fetch(apiUris.getGames(gameName)).then(respObj => respObj.json())
}

function getPopularGames() {
    return fetch(apiUris.getPopularGames()).then(respObj => respObj.json())
}
  
function getGroups() {
    return fetch(apiUris.getGroups()).then(respObj => respObj.json())
}
  
function getGroup(groupName) {
    return fetch(apiUris.getGroup(groupName)).then(respObj => respObj.json())
}

function createGroup(groupName, groupDesc) {
    return fetch(apiUris.createGroup(), {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            "name": groupName,
            "desc": groupDesc
        })
    })
    .then(function(respObj) {
        if (respObj.status != 201)
            return respObj.json().then(errObj => Promise.reject(errObj.message))
        else
            return respObj.json()
    })
}

function deleteGroup(groupName) {
    return fetch(apiUris.deleteGroup(groupName), {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(function(respObj) {
        if (respObj.status != 200)
            return respObj.json().then(errObj => Promise.reject(errObj.message))
        else
            return respObj.json()
    })
}

function addGameToGroup(groupName, gameName) {
    return fetch(apiUris.addGameToGroup(groupName, gameName), {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(function(respObj) {
        if (respObj.status != 201) 
            return respObj.json().then(errObj => Promise.reject(errObj.message))
        else
            return respObj.json()
    })
}

function deleteGameFromGroup(groupName, gameName) {
    return fetch(apiUris.deleteGameFromGroup(groupName, gameName), {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(function(respObj) {
        if (respObj.status != 200) 
            return respObj.json().then(errObj => Promise.reject(errObj.message))
        else
            return respObj.json()
    })
}

function getGamesFromGroupBetweenPlaytime(groupName, minPlaytime, maxPlaytime) {
    return fetch(
        apiUris.getGamesFromGroupBetweenPlaytime(groupName, minPlaytime, maxPlaytime)
    ).then(respObj => respObj.json())
}

function updateGroup(oldGroupName, newGroupName, newGroupDesc) {
    return fetch(apiUris.updateGroup(oldGroupName), {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            "name": newGroupName,
            "desc": newGroupDesc
        })
    })
    .then(function(respObj) {
        if(respObj.status != 200)
            return respObj.json().then(errObj => Promise.reject(errObj.message))
        else
            return respObj.json()
    })
}

function login(username, password) {
    return fetch(apiUris.login(), {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            "username": username,
            "password": password
        })
    }).then(respObj => respObj.json())
}

function logout() {
    return fetch(apiUris.logout(), {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(respObj => respObj.json())
}

function registerUser(username, password) {
    return fetch(apiUris.register(), {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            "username": username,
            "password": password
        })
    })
    .then(function(respObj) {
        if (respObj.status != 201)
            return respObj.json().then(errObj => Promise.reject(errObj.message))
        else
            return respObj.json()
    })
}

// Returns true or false
function isAuthenticated() {
    return fetch(apiUris.auth()).then(respObj => respObj.json())
}