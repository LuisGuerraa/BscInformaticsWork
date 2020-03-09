const request  = require('request')
const assert = require('assert')
const utils = require('./../ciborg-utils')(request)

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

const optionsAddCatanToGroupA = utils.requestServerOptions('PUT', 'groups/A/games/Catan', null)

const optionsDeleteGroupA = utils.requestServerOptions('DELETE', 'groups/A', null)

describe('PUT Update Group', function() {
    this.timeout(utils.MOCHA_TIMEOUT)

    utils.post(logInWithTestUser, before)
    utils.post(optionsCreateGroupA, before)
    utils.del(optionsDeleteGroupA, after)

    it('Should return success message with the status code \'200\'', function(done) {
        request.put(optionsUpdateGroupA, (err, res, body) => {
            assert.equal(body.message, 'Group updated.')
            assert.equal(res.statusCode, 200)  
            done()
        })
    });
});

describe('PUT Add Game to Group', function() {
    this.timeout(utils.MOCHA_TIMEOUT)

    utils.post(logInWithTestUser, before)
    utils.post(optionsCreateGroupA, before)
    utils.refresh(after)
    utils.del(optionsDeleteGroupA, after)

    it('Should return success message with the status code \'200\'', function(done) {
        request.put(optionsAddCatanToGroupA, (err, res, body) => {
            assert.equal(body.message, 'Game added to group successfully.')
            assert.equal(res.statusCode, 201)
            done()
        })
    });
});