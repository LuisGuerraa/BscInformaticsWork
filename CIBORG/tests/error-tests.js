const request  = require('request')
const assert = require('assert')
const utils = require('./../ciborg-utils.js')(request)

const logInWithTestUser = utils.requestServerOptions('POST', 'login', {
    username: "test",
    password: "test"
})

const optionsCreateGroupA = utils.requestServerOptions('POST', 'groups', {
    name: "A",
    desc: "Grupo de testes A"
})

const optionsUpdateGroupA = utils.requestServerOptions('PUT', 'groups/A', {
    name: "A",
    desc: "Groupo de testes X"
})

const optionsDeleteGroupA = utils.requestServerOptions('DELETE', 'groups/A', null)

const optionsAddCatanToGroupA = utils.requestServerOptions('PUT', 'groups/A/games/Catan', null)
const optionsRemoveCatanFromGroupA = utils.requestServerOptions('DELETE', 'groups/A/games/Catan', null)

describe('ERROR No groups found', function() {
    this.timeout(utils.MOCHA_TIMEOUT)

    utils.post(logInWithTestUser, before)

    const options = utils.requestServerOptions('GET', 'groups', null)
    it('Should return an error message with the status code \'404 Not Found\'', function(done) {
        request.get(options, (err, res, body) => {
            console.log(body)
            assert.equal(body.message, "No groups found.")
            assert.equal(res.statusCode, 404)
            done()
        })
    });
});

describe('ERROR No group found', function() {
    this.timeout(utils.MOCHA_TIMEOUT)

    utils.post(logInWithTestUser, before)

    const options = utils.requestServerOptions('GET', 'groups/X', null)
    it('Should return an error message with the status code \'404 Not Found\'', function(done) {
        request.get(options, (err, res, body) => {
            assert.equal(body.message, "Group does not exist.")
            assert.equal(res.statusCode, 404)
            done()
        })
    });
});

describe('ERROR No games between playtime', function() {
    this.timeout(utils.MOCHA_TIMEOUT)

    utils.post(logInWithTestUser, before)
    utils.post(optionsCreateGroupA, before)
    utils.del(optionsDeleteGroupA, after)

    const options = utils.requestServerOptions('GET', 'groups/A/games/50&100', null)
    it('Should return an error message with the status code \'404 Not Found\'', function(done) {
        request.get(options, (err, res, body) => {
            assert.equal(body.message, "No games found.")
            assert.equal(res.statusCode, 404)
            done()
        })
    });
});

describe('POST Existent Group', function() {
    this.timeout(utils.MOCHA_TIMEOUT)

    utils.post(logInWithTestUser, before)
    utils.post(optionsCreateGroupA, before)
    utils.del(optionsDeleteGroupA, after)

    it('Should return an error message with the status code \'409\'', function(done) {
        request.post(optionsCreateGroupA, (err, res, body) => {
            assert.equal(body.message, 'Group already exists.')
            assert.equal(res.statusCode, 409)
            done()
        })
    });
});

describe('ERROR Add Existent Game to Group', function() {
    this.timeout(utils.MOCHA_TIMEOUT)

    utils.post(logInWithTestUser, before)
    utils.post(optionsCreateGroupA, before)
    utils.put(optionsAddCatanToGroupA, before)
    utils.del(optionsDeleteGroupA, after)

    it('Should return an error message with the status code \'409\'', function(done) {
        request.put(optionsAddCatanToGroupA, (err, res, body) => {
            assert.equal(body.message, 'Game already exists in this group.')
            assert.equal(res.statusCode, 409)
            done()
        })
    });
});

describe('ERROR Update Unexistent Group', function() {
    this.timeout(utils.MOCHA_TIMEOUT)

    it('Should return an error message with the status code \'404\'', function(done) {
        request.put(optionsUpdateGroupA, (err, res, body) => {
            assert.equal(body.message, 'Group does not exist.')
            assert.equal(res.statusCode, 404)
            done()
        })
    });
});

describe('DELETE Unexistent Game from Group', function() {
    this.timeout(utils.MOCHA_TIMEOUT)

    utils.post(logInWithTestUser, before)
    utils.post(optionsCreateGroupA, before)
    utils.del(optionsDeleteGroupA, after)

    it('Should return an error message with the status code \'404\'', function(done) {
        request.delete(optionsRemoveCatanFromGroupA, (err, res, body) => {
            assert.equal(body.message, 'Game does not exist in this group.')
            assert.equal(res.statusCode, 404)
            done()
        })
    });
});

describe('DELETE Unexistent Group', function() {
    this.timeout(utils.MOCHA_TIMEOUT)

    it('Should return an error message with the status code \'404\'', function(done) {
        request.delete(optionsDeleteGroupA, (err, res, body) => {
            assert.equal(body.message, 'Group does not exist.')
            assert.equal(res.statusCode, 404)
            done()
        })
    });
});