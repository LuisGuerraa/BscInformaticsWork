module.exports = function(request) {
    
    const SERVER_HOST = "localhost"
    const SERVER_PORT = 80
    const SERVER_URI = `http://${SERVER_HOST}/`

    const ES_HOST = "localhost"
    const ES_PORT = 9200
    const ES_URI = `http://${ES_HOST}:${ES_PORT}/`

    const API_CLIENT_ID = "SB1VGnDv7M"
    const API_RESULT_LIMIT = 30
    const API_URL_START = "https://www.boardgameatlas.com/api/search?";
    const API_URL_END = `&pretty=true&limit=${API_RESULT_LIMIT}&client_id=${API_CLIENT_ID}`;

    const MOCHA_TIMEOUT = 10000000

    if (!request)
        throw "Invalid request."

    return {
        requestServerOptions: requestServerOptions,
        requestDatabaseOptions: requestDatabaseOptions,
        refresh: refresh,
        post: post,
        put: put,
        del: del,
        getRespObj: getRespObj,
        SERVER_PORT: SERVER_PORT,
        MOCHA_TIMEOUT: MOCHA_TIMEOUT,
        API_URL_START: API_URL_START,
        API_URL_END: API_URL_END
    }

    function requestServerOptions(method, path, body) {
        if(body != null)
            return {
                'method': method,
                'uri': `${SERVER_URI}${path}`,
                'body': body,
                'json': true,
                'jar': true
            }
        else
            return {
                'method': method,
                'uri': `${SERVER_URI}${path}`,
                'json': true,
                'jar': true
            }
    }

    function requestDatabaseOptions(method, path, body) {
        return {
            'method': method,
            'uri': `${ES_URI}${path}`,
            'body': body,
            'json': true
        }
    }

    function refresh(beforeOrAfter) {
        beforeOrAfter(function(done) {
            options = {
                'method': 'POST',
                'uri': `${ES_URI}users/_refresh`,
                'json': true
            }
            request.post(options, (err, res, body) => {
                done()
            })
        })
    }


    function post(options, beforeOrAfter) {
        beforeOrAfter(function(done) {
            request.post(options, (err, res, body) => {
                done()
            })
        })

        refresh(beforeOrAfter)
    }

    function put(options, beforeOrAfter) {
        beforeOrAfter(function(done) {
            request.put(options, (err, res, body) => {
                done()
            })
        })

        refresh(beforeOrAfter)
    }

    function del(options, beforeOrAfter) {
        beforeOrAfter(function(done) {
            request.delete(options, (err, res, body) => {
                done()
            })
        })

        refresh(beforeOrAfter)
    }

    function getRespObj(code, message = "Service Unavailable", body = {}) {
        return {
            'statusCode': code,
            'message': message,
            'body': body
        }
    }
}