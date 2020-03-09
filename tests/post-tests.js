const request = require('request')
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

const optionsDeleteGroupA = utils.requestServerOptions('DELETE', 'groups/A', null)

describe('POST New Group', function() {
    this.timeout(utils.MOCHA_TIMEOUT)

    utils.post(logInWithTestUser, before)
    utils.refresh(after)
    utils.del(optionsDeleteGroupA, after)

    it('Should return success message with the status code \'201\'', function(done) {
        request.post(optionsCreateGroupA, (err, res, body) => {
            assert.equal(body.message, 'Group created.')
            assert.equal(res.statusCode, 201)  
            done()
        })
    });
})