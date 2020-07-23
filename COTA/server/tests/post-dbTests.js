const assert = require('assert');
const services = require('../cota-services');

describe('POST Group', () => {
    it('Should return group created', done => {
        services.createGroup("NewGroupName", "NewGroupDesc")
            .then(result => {
                    assert.strictEqual(result.data, "created")
                    assert.strictEqual(result.short, services.GROUP_CREATED_MSG)

                }
            )
        done();
    })
})

describe('POST Duplicate Group', () => {
    it('Should return duplicate error', done => {
        services.createGroup("NewGroupName", "NewGroupDesc")
            .catch(result => {
                    assert.notStrictEqual(result.detail, undefined)
                    assert.strictEqual(result.short, services.GROUP_CONFLICT_MSG)
                }
            )

        done();
    })
})
