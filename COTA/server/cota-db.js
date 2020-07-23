'use strict';
const utils = require('./cota-utils');
const fetch = require('node-fetch');

const baseUrl = utils.ES_URI;

function getUserByName(username) {
    return fetch(`${baseUrl}/users/_search?q=username:${username}`, {
        headers: {
            "Content-Type": "application/json"
        },
    })
        .then(response => response.json())
        .then(body => {
            let hit = body.hits.hits;
            if (hit.length) return hit[0]._source;
            return undefined;
        })
        .catch(() => Promise.reject("Error in GetUserByName process"))
}

function createUser(username, password) {
    return fetch(`${baseUrl}/users/_doc?refresh=true`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "username": username,
            "password": password
        })
    })
        .then(response => response.json())
        .then(body => body.result)
        .catch(() => Promise.reject("Error in Create User process "))
}

function getGroupByName(groupName, owner) {
    return fetch(`${baseUrl}/groups/_search?q=(owner:${owner})AND(name:${groupName})`, {
        headers: {
            "Content-Type": "application/json"
        },
    })
        .then(response => response.json())
        .then(body => {
            let hit = body.hits.hits;
            if (hit.length) return hit[0]._source;
            return undefined;
        })
        .catch(() => Promise.reject("Error in GetGroupByName process"))
}

function getGroups(owner) {
    return fetch(`${baseUrl}/groups/_search?q=owner:${owner}`, {
        method: 'GET',
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => response.json())
        .then(body => body.hits.hits.map(hit => hit._source))
        .catch(() => Promise.reject("Error in GetGroupByOwner process "))
}

function getAllGroups() {
    return fetch(`${baseUrl}/groups/_search`, {
        method: 'GET',
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => response.json())
        .then(body => body.hits.hits.map(hit => hit._source))
        .catch(() => Promise.reject("Error in GetAllGroups process "))
}

function getPublicGroups() {
    return fetch(`${baseUrl}/groups/_search?q=visibility:public`, {
        method: 'GET',
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => response.json())
        .then(body => body.hits.hits.map(hit => hit._source))
        .catch(() => Promise.reject("Error in GetPublic Groups process "))
}

function createGroup(name, desc, owner, visibility) {
    return fetch(`${baseUrl}/groups/_doc?refresh=true`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "owner": owner,
            "visibility": visibility,
            "sharedWith": [],
            "name": name,
            "desc": desc,
            "series": []
        })
    })
        .then(response => response.json())
        .then(body => body.result)
        .catch(() => Promise.reject("Error in CreateGroup process "))
}


function giveRating(groupName,seriesIdx,owner,rating){
   /* return fetch(`${baseUrl}/groups/_doc/_update_by_query`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "query": {
                "bool": {
                    "must": [
                        {
                            "match": {
                                "owner": `${owner}`
                            }
                        },
                        {
                            "match": {
                                "name": `${groupName}`
                            }
                        }
                    ]
                }
            },
                "script": {
                    "lang": "painless",
                    "inline": "ctx._source.series[$seriesIdx].add(params.rating)",
                    "params": {
                        "rating": rating
                    }
                }
            }
        )
    }

*/
}


function updateGroup(oldName, newName, newDesc, owner) {
    return fetch(`${baseUrl}/groups/_doc/_update_by_query`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "query": {
                "bool": {
                    "must": [
                        {
                            "match": {
                                "owner": `${owner}`
                            }
                        },
                        {
                            "match": {
                                "name": `${oldName}`
                            }
                        }
                    ]
                }
            },
            "script": {
                "source": "ctx._source.name = params.name; ctx._source.desc = params.desc",
                "params": {
                    "name": `${newName}`,
                    "desc": `${newDesc}`,
                }
            }
        })
    })
        .then(response => response.json())
        .then(body => body.updated)
        .catch(() => Promise.reject("Error in UpdateGroup process "))

}

function addSeriesToGroup(groupName, series, owner) {
    return fetch(`${baseUrl}/groups/_doc/_update_by_query`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "query": {
                "dis_max": {
                    "queries": [
                        {
                            "match": {
                                "owner": `${owner}`
                            }
                        },
                        {
                            "match": {
                                "name": `${groupName}`
                            }
                        }
                    ]
                }
            },
            "script": {
                "lang": "painless",
                "inline": "ctx._source.series.add(params.newSeries)",
                "params": {
                    "newSeries": series
                }
            }
        })
    })
        .then(response => response.json())
        .then(body => body.updated)
        .catch(() => Promise.reject("Error in addSeriesToGroup process "))

}

function getSeriesIdByGroupName(groupName, seriesName, owner) {
    return fetch(`${baseUrl}/groups/_search?q=(owner:${owner})AND(name:${groupName})`, {
        headers: {
            "Content-Type": "application/json"
        },
    })
        .then(response => response.json())
        .then(body => {
            let hit = body.hits.hits[0]._source.series;
            if (hit.length) return hit.findIndex(obj => obj.name === seriesName);
            return undefined;
        })
        .catch(() => Promise.reject("Error in getSeriesIdByGroupName process "))
}

function deleteSeriesFromGroup(groupName, seriesIndex, owner) {
    return fetch(`${baseUrl}/groups/_doc/_update_by_query`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                "query": {
                    "bool": {
                        "must": [
                            {
                                "match": {
                                    "owner": `${owner}`
                                }
                            },
                            {
                                "match": {
                                    "name": `${groupName}`
                                }
                            }
                        ]
                    }
                },
                    "script": {
                        "lang": "painless",
                        "inline": "ctx._source.series.remove(params.deleteSeries)",
                        "params": {
                            "deleteSeries": seriesIndex
                        }
                    }
                }
            )
        }
    )
        .then(response => response.json())
        .then(body => body.updated)
        .catch(() => Promise.reject("Error in addSeriesToGroup process "))
}

function deleteGroup(groupName, owner) {
    return fetch(`${baseUrl}/groups/_doc/_delete_by_query`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "query": {
                "dis_max": {
                    "queries": [
                        {
                            "match": {
                                "owner": `${owner}`
                            }
                        },
                        {
                            "match": {
                                "name": `${groupName}`
                            }
                        }
                    ]
                }
            }
        })
    })
        .then(response => response.json())
        .then(body => body.deleted)
        .catch(() => Promise.reject("Error in DeleteGroup process "))
}

module.exports = {
    createGroup: createGroup,
    deleteGroup: deleteGroup,
    getGroupByName: getGroupByName,
    getPublicGroups: getPublicGroups,
    getAllGroups : getAllGroups,
    getGroups: getGroups,
    updateGroup: updateGroup,
    addSeriesToGroup: addSeriesToGroup,
    deleteSeriesFromGroup: deleteSeriesFromGroup,
    getSeriesIdByGroupName: getSeriesIdByGroupName,
    createUser: createUser,
    getUserByName: getUserByName,
};



