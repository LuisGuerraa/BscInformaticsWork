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

const optionsAddCatanToGroupA = utils.requestServerOptions('PUT', 'groups/A/games/Catan', null)
const optionsRemoveCatanFromGroupA = utils.requestServerOptions('DELETE', 'groups/A/games/Catan', null)

const optionsDeleteGroupA = utils.requestServerOptions('DELETE', 'groups/A', null)

describe('DELETE Group', function() {
    this.timeout(utils.MOCHA_TIMEOUT)

    utils.post(logInWithTestUser, before)
    utils.post(optionsCreateGroupA, before)
    utils.refresh(after)

    it('Should return success message with the status code \'200\'', function(done) {
        request.delete(optionsDeleteGroupA, (err, res, body) => {
            assert.equal(body.message, 'Group deleted.')
            assert.equal(res.statusCode, 200)  
            done()
        })
    });
});

describe('DELETE Game from Group', function() {
    this.timeout(utils.MOCHA_TIMEOUT)

    utils.post(logInWithTestUser, before)
    utils.post(optionsCreateGroupA, before)
    utils.put(optionsAddCatanToGroupA, before)
    utils.refresh(after)
    utils.del(optionsDeleteGroupA, after)

    it('Should return success message with the status code \'200\'', function(done) {
        request.delete(optionsRemoveCatanFromGroupA, (err, res, body) => {
            assert.equal(body.message, 'Game removed from group successfully.')
            assert.equal(res.statusCode, 200)
            done()
        })
    });
});