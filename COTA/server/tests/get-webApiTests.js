const assert = require('assert');
const services = require('../cota-services');

describe('GET Series By Name', () => {
    it('Should return Homeland with the message resource found', done => {
        services.getSeriesWithName('Homeland')
            .then(result => {
                    assert.strictEqual(result.data[0].name, "Homeland")
                    assert.strictEqual(result.short, services.RESOURCE_FOUND_MSG)
                    done();
                }
            )
    })
})

describe('GET Popular Series', () => {
    it('Should return 20 most popular series with the message resource found for page 1', done => {
        services.getPopularSeries(1)
            .then(result => {
                assert.strictEqual(result.data.length, 20)
                assert.strictEqual(result.short, services.RESOURCE_FOUND_MSG)
                done()
            })

    })
});