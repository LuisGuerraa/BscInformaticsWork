const webApi = require('./movie-database-data.js');
const db = require('./cota-db.js');
const utils = require('./cota-utils.js');

const RESOURCE_FOUND_MSG = "RESOURCE FOUND";
const RESOURCE_NOT_FOUND_MSG = "RESOURCE NOT FOUND";
const RESOURCE_CREATED_MSG = "RESOURCE CREATED";
const RESOURCE_CONFLICT_MSG = "DUPLICATE RESOURCE FOUND";
const RESOURCE_UPDATED_MSG = "GROUP UPDATED";
const RESOURCE_UNAUTHORIZED_MSG = "RESOURCE UNAUTHORIZED";
const DB_ERROR_MSG = "ERROR IN DB";
const API_ERROR_MSG = "ERROR IN MOVIEDATABASE API";


function getPopularSeries(page) {
    return webApi.getPopularSeries(page)
        .then(seriesObj => {
            if (seriesObj) {
                return utils.success(
                    `Most popular series successful retrieved`,
                    RESOURCE_FOUND_MSG,
                    seriesObj,
                )
            }
            return utils.error(
                `Error at fetching popular series`,
                RESOURCE_NOT_FOUND_MSG
            )
        })
        .catch(err => {
                return errorInvoke(err, API_ERROR_MSG)
            }
        )
}

function getSeriesByName(seriesName) {
    return webApi.getSeriesWithName(seriesName)
        .then(seriesObj => {
            if (seriesObj) {
                return utils.success(
                    `Series ${seriesName} found`,
                    RESOURCE_FOUND_MSG,
                    seriesObj
                )
            } else {
                return utils.error(
                    `Cannot find ${seriesName} series`,
                    RESOURCE_NOT_FOUND_MSG
                )
            }
        })
        .catch(err => {
                return errorInvoke(err, API_ERROR_MSG)
            }
        )
}

function getGroups(request) {
    if (!request.isAuthenticated()) {
        return utils.error(
            `YOU MUST BE LOGGED IN`,
            RESOURCE_UNAUTHORIZED_MSG
        )
    } else {
        return db.getGroups(request.user.data.username)
            .then(allGroups => {
                    return utils.success(
                        "All groups registered",
                        RESOURCE_FOUND_MSG,
                        allGroups
                    )
                }
            )
            .catch(err => {
                    return errorInvoke(err, DB_ERROR_MSG);
                }
            )
    }
}

function giveRating(request){
  /*  if (!request.isAuthenticated()) {
        return utils.error(
            `YOU MUST BE LOGGED IN`,
            RESOURCE_UNAUTHORIZED_MSG
        )
    } else {
        const currentUser = request.user.data.username;
        return db.getGroupByName(request.params.groupName, currentUser)
        .then(group => {
                if (group) {
                    return db.getSeriesIdByGroupName(groupName, seriesName, owner)
                    .then(seriesIndex => {
                            if (seriesIndex !== -1) {
                       return db.giveRating(request.params.groupName,seriesIndex,currentUser,request.body.rating)*/ 
}

function getSharedGroups(request) {
    if (!request.isAuthenticated()) {
        return utils.error(
            `YOU MUST BE LOGGED IN`,
            RESOURCE_UNAUTHORIZED_MSG
        )
    } else {
        const currentUser = request.user.data.username;
        return db.getAllGroups()
            .then(sharedGroups => {
                    sharedGroups = sharedGroups.filter((group) => {
                        return group.sharedWith.some((user) => user.username === currentUser)
                    });
                    if (sharedGroups.length) {
                        return utils.success(
                            `All groups shared with ${currentUser}`,
                            RESOURCE_FOUND_MSG,
                            sharedGroups
                        )
                    } else {
                        return utils.error(
                            `No groups found shared with ${currentUser}`,
                            RESOURCE_NOT_FOUND_MSG
                        )
                    }
                }
            )
            .catch(err => {
                    return errorInvoke(err, DB_ERROR_MSG);
                }
            )
    }
}

function getPublicGroups(request) {
    if (!request.isAuthenticated()) {
        return utils.error(
            `YOU MUST BE LOGGED IN`,
            RESOURCE_UNAUTHORIZED_MSG
        )
    } else {
        return db.getPublicGroups()
            .then(publicGroups => {
                    return utils.success(
                        "All public groups registered",
                        RESOURCE_FOUND_MSG,
                        publicGroups
                    )
                }
            )
            .catch(err => {
                    return errorInvoke(err, DB_ERROR_MSG);
                }
            )
    }
}

function getGroupByName(request) {
    const groupName = request.params.groupName;

    if (!request.isAuthenticated()) {
        return utils.error(
            `YOU MUST BE LOGGED IN`,
            RESOURCE_UNAUTHORIZED_MSG
        )
    } else {
        const owner = request.user.data.username;
        return db.getGroupByName(groupName, owner)
            .then(groupObj => {
                if (groupObj) {
                    return utils.success(
                        `Group ${groupName} found`,
                        RESOURCE_FOUND_MSG,
                        groupObj
                    )
                }
                return utils.error(
                    `Group ${groupName} not found`,
                    RESOURCE_NOT_FOUND_MSG
                )
            })
            .catch(err => {
                    return errorInvoke(err, DB_ERROR_MSG);
                }
            )
    }
}

function createGroup(request) {
    const groupName = request.body.name;
    const groupDesc = request.body.desc;
    const groupVisibility = request.body.visibility;

    if (!request.isAuthenticated()) {
        return utils.error(
            `YOU MUST BE LOGGED IN`,
            RESOURCE_UNAUTHORIZED_MSG
        )
    } else {
        const owner = request.user.data.username;
        return db.getGroupByName(groupName, owner)
            .then(replyObj => {
                if (replyObj) {
                    return utils.error(
                        `Group ${groupName} already exists`,
                        RESOURCE_CONFLICT_MSG
                    )
                } else {
                    return db.createGroup(groupName, groupDesc, owner, groupVisibility)
                        .then(result => {
                                if (result === "created")
                                    return utils.success(
                                        `Group ${groupName} created`,
                                        RESOURCE_CREATED_MSG,
                                        result
                                    )
                            }
                        )
                }
            })
            .catch(err => {
                    return errorInvoke(err, DB_ERROR_MSG);
                }
            )
    }
}


function updateGroup(req) {
    const oldGroupName = req.params.groupName;
    const newGroupName = req.body.name;
    const newGroupDesc = req.body.desc;

    if (!req.isAuthenticated()) {
        return utils.error(
            `YOU MUST BE LOGGED IN`,
            RESOURCE_UNAUTHORIZED_MSG
        )
    } else {
        const owner = req.user.data.username;
        return db.getGroupByName(oldGroupName, owner)
            .then(oldGroup => {
                if (oldGroup) {
                    return db.getGroupByName(newGroupName, owner)
                        .then(newGroup => {
                            if (!newGroup) {
                                return db.updateGroup(oldGroupName, newGroupName, newGroupDesc, owner)
                                    .then(result => {
                                            if (result) {
                                                return utils.success(
                                                    `Group ${oldGroupName} updated`,
                                                    RESOURCE_UPDATED_MSG
                                                )
                                            } else return utils.error(
                                                `Problem updating ${oldGroupName} group`,
                                                DB_ERROR_MSG
                                            )
                                        }
                                    )
                            } else {
                                return utils.error(
                                    `Group ${newGroupName} already exists`,
                                    RESOURCE_CONFLICT_MSG
                                )
                            }
                        })

                } else {
                    return utils.error(
                        `Group ${oldGroupName} not found`,
                        RESOURCE_NOT_FOUND_MSG
                    )
                }
            })
            .catch(err => {
                return errorInvoke(err, DB_ERROR_MSG);
            })
    }

}

function addSeriesToGroup(request) {
    const groupName = request.params.groupName;
    const seriesName = request.body.series;

    if (!request.isAuthenticated()) {
        return utils.error(
            `YOU MUST BE LOGGED IN`,
            RESOURCE_UNAUTHORIZED_MSG
        )
    } else {
        const owner = request.user.data.username;
        return db.getGroupByName(groupName, owner)
            .then(group => {
                    if (group) {
                        const duplicate = (series) => series.name === seriesName;
                        if (!group.series.some(duplicate)) {
                            return webApi.getSeriesWithName(seriesName)
                                .then(series => {
                                    if (series) {
                                        return db.addSeriesToGroup(groupName, series[0])
                                            .then(updated => {
                                                if (updated) {
                                                    return utils.success(
                                                        `Added ${seriesName} series ${groupName} group`,
                                                        RESOURCE_UPDATED_MSG
                                                    )
                                                } else {
                                                    return utils.error(
                                                        `Problem adding ${seriesName} to ${groupName} group `,
                                                        DB_ERROR_MSG
                                                    )
                                                }
                                            })

                                    } else {
                                        return utils.error(
                                            `Series ${seriesName} not found`,
                                            RESOURCE_NOT_FOUND_MSG
                                        )
                                    }
                                })
                        } else {
                            return utils.error(
                                `Series ${seriesName} already at ${groupName} group `,
                                RESOURCE_CONFLICT_MSG
                            )
                        }
                    } else {
                        return utils.error(
                            `Group ${groupName} not found`,
                            RESOURCE_NOT_FOUND_MSG
                        )
                    }
                }
            )
            .catch(err => {
                return errorInvoke(err, DB_ERROR_MSG);
            })
    }
}

function deleteSeriesFromGroup(request) {
    const groupName = request.params.groupName;
    const seriesName = request.params.seriesName;

    if (!request.isAuthenticated()) {
        return utils.error(
            `YOU MUST BE LOGGED IN`,
            RESOURCE_UNAUTHORIZED_MSG
        )
    } else {
        const owner = request.user.data.username;
        return db.getGroupByName(groupName, owner)
            .then(group => {
                    if (group) {
                        return db.getSeriesIdByGroupName(groupName, seriesName, owner)
                            .then(seriesIndex => {
                                    if (seriesIndex !== -1) {
                                        return db.deleteSeriesFromGroup(groupName, seriesIndex, owner)
                                            .then(update => {
                                                if (update) {
                                                    return utils.success(
                                                        `Removed ${seriesName} from ${groupName}`,
                                                        RESOURCE_UPDATED_MSG
                                                    )
                                                } else {
                                                    return utils.error(
                                                        `Problem deleting ${seriesName} from ${groupName} group `,
                                                        DB_ERROR_MSG
                                                    )
                                                }
                                            })
                                    } else {
                                        return utils.error(
                                            `Series ${seriesName} not found at ${groupName}`,
                                            RESOURCE_NOT_FOUND_MSG
                                        )
                                    }
                                }
                            )
                    } else {
                        return utils.error(
                            `Group ${groupName} not found`,
                            RESOURCE_NOT_FOUND_MSG
                        )
                    }
                }
            )
            .catch((err) => {
                return errorInvoke(err, DB_ERROR_MSG);
            })
    }
}

function getSeriesBetweenInterval(request) {
    const groupName = request.params.groupName;
    const min = request.params.min;
    const max = request.params.max;

    if (!req.isAuthenticated()) {
        return utils.error(
            `YOU MUST BE LOGGED IN`,
            RESOURCE_UNAUTHORIZED_MSG
        )
    } else {
        const owner = req.user.data.username;
        return getGroupByName(groupName, owner)
            .then(series => {
                return utils.success(
                    `Series from ${groupName} votes between ${min} and ${max}`,
                    RESOURCE_FOUND_MSG,
                    series.data.series.filter(obj => obj.vote_average > parseInt(min) && obj.vote_average < parseInt(max))
                )
            })
    }
}

function getUserByName(username) {
    return db.getUserByName(username)
        .then(userObj => {
            if (userObj) {
                return utils.success(
                    `User ${username} found`,
                    RESOURCE_FOUND_MSG,
                    userObj
                )
            }
            return utils.error(
                `User ${username} not found`,
                RESOURCE_NOT_FOUND_MSG
            )
        })
        .catch(err => {
                return errorInvoke(err, DB_ERROR_MSG);
            }
        )
}

function createUser(username, password) {
    return db.getUserByName(username)
        .then(userObj => {
            if (userObj) {
                return utils.error(
                    `User ${username} already exists`,
                    RESOURCE_CONFLICT_MSG
                )
            } else {
                return db.createUser(username, password)
                    .then(result => {
                            if (result === "created")
                                return utils.success(
                                    `User ${username} created`,
                                    RESOURCE_CREATED_MSG,
                                    result
                                )
                        }
                    )
            }
        })
        .catch(err => {
                return errorInvoke(err, DB_ERROR_MSG);
            }
        )
}

function signIn(request) {
    const requestBody = request.body;
    const username = requestBody.username;
    const password = requestBody.password;
    return checkValidUser(username, password)
        .then(foundUser => {
            if (foundUser) {
                return userLogin(request, foundUser)
                    .then(() => {
                        return utils.success(
                            `User ${username} found`,
                            RESOURCE_FOUND_MSG,
                            username
                        )
                    })
            } else {
                return utils.error(
                    `User ${username} not found`,
                    RESOURCE_NOT_FOUND_MSG,
                )
            }
        })
        .catch(err => {
            return errorInvoke(err, DB_ERROR_MSG);
        })
}

function userLogin(req, user) {
    return new Promise((resolve, reject) => {
        req.login(user, (err, result) => {
            if (err) {
                reject(err);
            } else {
                resolve(result);
            }
        });
    });
}

function checkValidUser(username, password) {
    return getUserByName(username)
        .then(response => {
            if (response.data && response.data.password === password) {
                return utils.success(
                    `Correct credentials for ${username} `,
                    RESOURCE_FOUND_MSG,
                    response.data
                )
            } else {
                return utils.error(
                    `Invalid credentials for ${username}`,
                    RESOURCE_UNAUTHORIZED_MSG,
                )
            }
        }).catch((err) => {
            return errorInvoke(err, DB_ERROR_MSG);
        });
}

function getUser(request) {
    return new Promise(((resolve, reject) => {
        const user = request.isAuthenticated() && request.user.data;
        if (user) {
            resolve(utils.success(
                `User ${user.username} found`,
                RESOURCE_FOUND_MSG,
                user.username
            ))
        } else {
            reject({
                detail: `User not found`,
                short: RESOURCE_NOT_FOUND_MSG
            })

        }
    }))
}

function errorInvoke(err, msg) {
    if (err.short) return Promise.reject(err)
    else return utils.error(err, msg)
}

module.exports = {
    getPopularSeries: getPopularSeries,
    getSeriesWithName: getSeriesByName,
    getPublicGroups: getPublicGroups,
    getSharedGroups: getSharedGroups,
    getGroups: getGroups,
    getGroupByName: getGroupByName,
    getSeriesBetweenInterval: getSeriesBetweenInterval,
    createGroup: createGroup,
    updateGroup: updateGroup,
    getUser: getUser,
    addSeriesToGroup: addSeriesToGroup,
    deleteSeriesFromGroup: deleteSeriesFromGroup,
    getUserByName: getUserByName,
    createUser: createUser,
    signIn: signIn,
    RESOURCE_FOUND_MSG: RESOURCE_FOUND_MSG,
    RESOURCE_NOT_FOUND_MSG: RESOURCE_NOT_FOUND_MSG,
    RESOURCE_CREATED_MSG: RESOURCE_CREATED_MSG,
    RESOURCE_UPDATED_MSG: RESOURCE_UPDATED_MSG,
    RESOURCE_CONFLICT_MSG: RESOURCE_CONFLICT_MSG,
    RESOURCE_UNAUTHORIZED_MSG: RESOURCE_UNAUTHORIZED_MSG,
    DB_ERROR_MSG: DB_ERROR_MSG,
    API_ERROR_MSG: API_ERROR_MSG
};