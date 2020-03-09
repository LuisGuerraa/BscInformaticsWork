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

const optionsCreateGroupB = utils.requestServerOptions('POST', 'groups', {
    name: "B",
    desc: "Grupo de testes B"
})

const optionsDeleteGroupA = utils.requestServerOptions('DELETE', 'groups/A', null)
const optionsDeleteGroupB = utils.requestServerOptions('DELETE', 'groups/B', null)
const optionsAddCatanToGroupA = utils.requestServerOptions('PUT', 'groups/A/games/Catan', null)
const optionsAddChessToGroupA = utils.requestServerOptions('PUT', 'groups/A/games/Chess', null)

describe('GET Popular Games', function() {
    this.timeout(utils.MOCHA_TIMEOUT)

    const options = utils.requestServerOptions('GET', 'games/popular', null)
    it('Should return 30 games with the status code \'200 OK\'', function(done) {
        request.get(options, (err, res, body) => {
            games = body.body
            assert.equal(games.length, 30)
            assert.equal(res.statusCode, 200)
            done()
        })
    });
});

describe('GET Specific Game', function() {
    this.timeout(utils.MOCHA_TIMEOUT)

    const options = utils.requestServerOptions('GET', 'games/Catan', null)
    it('Should return Catan with the status code \'200 OK\'', function(done) {
        request.get(options, (err, res, body) => {
            games = body.body
            assert.equal(games[0].name, "Catan")
            assert.equal(res.statusCode, 200)
            done()
        })
    });
});

describe('GET All Groups', function() {
    this.timeout(utils.MOCHA_TIMEOUT)

    utils.post(logInWithTestUser, before)
    utils.post(optionsCreateGroupA, before)
    utils.post(optionsCreateGroupB, before)
    utils.del(optionsDeleteGroupA, after)
    utils.del(optionsDeleteGroupB, after)

    const options = utils.requestServerOptions('GET', 'groups', null)
    it('Should return all groups with the status code \'200 OK\'', function(done) {
        request.get(options, (err, res, body) => {
            groups = body.body
            assert.equal(groups.length, 2)
            assert.equal(res.statusCode, 200)
            done()
        })
    });
});

describe('GET Specific Group', function() {
    this.timeout(utils.MOCHA_TIMEOUT)

    utils.post(logInWithTestUser, before)
    utils.post(optionsCreateGroupA, before)
    utils.del(optionsDeleteGroupA, after)

    const options = utils.requestServerOptions('GET', 'groups/A', null)
    it('Should return specific group with the status code \'200 OK\'', function(done) {
        request.get(options, (err, res, body) => {
            group = body.body
            assert.equal(group.name, "A")
            assert.equal(res.statusCode, 200)
            done()
        })
    });
});

describe('GET Games from Group between playtime', function() {
    this.timeout(utils.MOCHA_TIMEOUT)
    const options = utils.requestServerOptions('GET', 'groups/A/games/50&100', null)

    utils.post(logInWithTestUser, before)
    utils.post(optionsCreateGroupA, before)
    utils.put(optionsAddCatanToGroupA, before)
    utils.put(optionsAddChessToGroupA, before)
    utils.del(optionsDeleteGroupA, after)

    it('Should return filtered games with the status code \'200 OK\'', function(done) {
        request.get(options, (err, res, body) => {
            games = body.body
            assert.equal(games[0].name, "Chess")
            assert.equal(res.statusCode, 200)
            done()
        })
    });
});