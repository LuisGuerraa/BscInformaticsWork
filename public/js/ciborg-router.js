let routes = {}
/*
let routes = {
    "/games/:gameName" : getGame,
    "/games/popular" :  getPopular
}
*/

function register(path, handler) {
    routes[path] = handler;
}

function getHandler(pathFilled) {
    // path = /games/Catan

    for (let [pathTemplate, handler] of Object.entries(routes)) {
        let params = {}
        if (checkPath(pathTemplate, pathFilled, params)) {
            return {"handler": handler, "params": params}
        }
    }
}

/**
* Checks if pathTemplate corresponds to pathFilled and fills request parameters.
* @param {string} pathTemplate  /games/:gameName
* @param {string} pathFilled    /games/catan
* @return {object}              False if pathTemplate and pathFilled are different.
*                               Object 'params' if they correspond.
*/
function checkPath(pathTemplate, pathFilled, params) {
    
    pathTemplateParts = pathTemplate.split('/');
    pathFilledParts = pathFilled.split('/');
    
    if (pathTemplateParts.length != pathFilledParts.length)
        return false;
    
    for (let i = 0; i < pathTemplateParts.length; i++) {
        if (!pathTemplateParts[i].includes(':') && !(pathTemplateParts[i] === pathFilledParts[i])) {
            return false;
        }
        else if (pathTemplateParts[i].includes(':')) {
            let splittedPathTemplate = pathTemplateParts[i].split('&')
            let splittedPathFilled = pathFilledParts[i].split('&')

            // wrong number of parameters
            if (splittedPathTemplate.length != splittedPathFilled.length)
                return false

            for (let j = 0; j < splittedPathFilled.length; j++)
                params[splittedPathTemplate[j].replace(':', '')] = splittedPathFilled[j]
        }
    }
    
    return true;
}