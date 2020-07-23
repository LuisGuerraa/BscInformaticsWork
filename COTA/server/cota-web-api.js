'use strict';
const seriesService = require('./cota-services');
const respCodes = [];
respCodes[seriesService.RESOURCE_FOUND_MSG] = 200;
respCodes[seriesService.RESOURCE_UPDATED_MSG] = 200;
respCodes[seriesService.RESOURCE_NOT_FOUND_MSG] = 404;
respCodes[seriesService.RESOURCE_CREATED_MSG] = 201;
respCodes[seriesService.RESOURCE_CONFLICT_MSG] = 409;
respCodes[seriesService.RESOURCE_UNAUTHORIZED_MSG] = 401;
respCodes[seriesService.DB_ERROR_MSG] = 500;
respCodes[seriesService.API_ERROR_MSG] = 503;

function getPopularSeries(req, rsp) {
    delegateTask(
        seriesService.getPopularSeries(req.params.page),
        rsp
    )
}

function getSeriesWithName(req, rsp) {
    delegateTask(
        seriesService.getSeriesWithName(req.params.seriesName),
        rsp
    )
}

function getGroups(req, rsp) {
    delegateTask(
        seriesService.getGroups(req),
        rsp
    )
}

function getSharedGroups(req, rsp) {
    delegateTask(
        seriesService.getSharedGroups(req),
        rsp
    )
}

function getPublicGroups(req, rsp) {
    delegateTask(
        seriesService.getPublicGroups(req),
        rsp
    )
}

function getGroupByName(req, rsp) {
    delegateTask(
        seriesService.getGroupByName(req),
        rsp
    )
}

function createGroup(req, rsp) {
    delegateTask(
        seriesService.createGroup(req),
        rsp
    )
}


function updateGroup(req, rsp) {
    delegateTask(
        seriesService.updateGroup(req),
        rsp
    )
}

function addSeriesToGroup(req, rsp) {
    delegateTask(
        seriesService.addSeriesToGroup(req),
        rsp
    )
}

function deleteSeriesFromGroup(req, rsp) {
    delegateTask(
        seriesService.deleteSeriesFromGroup(req),
        rsp
    )
}

function getSeriesBetweenInterval(req, rsp) {
    delegateTask(
        seriesService.getSeriesBetweenInterval(req),
        rsp
    )
}

function signIn(req, rsp) {
    delegateTask(
        seriesService.signIn(req),
        rsp
    )
}

function logout(req, rsp) {
    req.logout();
    rsp.send();
}

function getUser(req, rsp) {
    delegateTask(
        seriesService.getUser(req),
        rsp
    )
}

function signUp(req, rsp) {
    delegateTask(
        seriesService.createUser(req.body.username, req.body.password),
        rsp
    )
}

function delegateTask(promise, rsp) {
    promise
        .then(result => {
            successResponse(rsp, result)
        })
        .catch(err => {
            errorResponse(rsp, err)
        });
}

function errorResponse(rsp, err) {
    rsp.statusCode = respCodes[err.short]
    rsp.json({error: err})
}

function successResponse(rsp, result) {
    rsp.statusCode = respCodes[result.short]
    rsp.json({success: result})
}

function giveRating(req,rsp){
    delegateTask(
        seriesService.giveRating(req),
        rsp
    )

}


module.exports = {
    getUser: getUser,
    signIn: signIn,
    logout: logout,
    signUp: signUp,
    getMostPopularSeries: getPopularSeries,
    getSeriesByName: getSeriesWithName,
    getGroups: getGroups,
    getSharedGroups: getSharedGroups,
    getPublicGroups: getPublicGroups,
    getGroupByName: getGroupByName,
    getSeriesBetweenInterval: getSeriesBetweenInterval,
    createGroup: createGroup,
    updateGroup: updateGroup,
    addSeriesToGroup: addSeriesToGroup,
    deleteSeriesFromGroup: deleteSeriesFromGroup,
    giveRating: giveRating
};