import { getLoginService as LoginService } from './LoginService'
//import { getBearerAuthorization } from './LoginOidc'
import Error from '../components/Error'

export const API = {
    host: 'http://localhost:3000/api',

    homeJsonDoc: { },

    relations: {
        /* DEFAULT */
        self: 'self',
        collection: 'collection',
        item: 'item',
        parent: 'parent',

        /* DOMAIN  */
        projects: '/rels/projects',
        project: '/rels/project',
        issues: '/rels/project/issues',
        issue: '/rels/project/issue',
        comments: '/rels/project/issue/comments',
        comment: '/rels/project/issue/comment',
        contributors: '/rels/project/contributors',
        contributor: '/rels/project/contributor',
    },
    actions: {
        /* PROJECT */
        postProject: '/actions/post/project',
        deleteProject: '/actions/delete/project',
        putProject: '/actions/put/project',
        patchProjectName: '/actions/patch/project/name',
        patchProjectDescription: '/actions/patch/project/description',
        
        /* ISSUE */
        postIssue: '/actions/post/issue',
        patchIssueName : '/actions/patch/issue/name',
        patchIssueDescription: '/actions/patch/issue/description',
        patchIssueState: '/actions/patch/issue/state',
        putIssue: '/actions/put/issue',
        deleteIssue: '/actions/delete/issue',
        
        /* CONTRIBUTOR */
        postContributor: '/actions/post/contributor',
        deleteContributor: '/actions/delete/contributor',
        
        /* COMMENT */
        postComment: '/actions/post/comment',
        putComment: '/actions/put/comment',
        deleteComment: '/actions/delete/comment'
    }
}

export async function getHomeJsonDocument() {
    const resp = await fetch(API.host)
    if (resp.ok) API.homeJsonDoc = await resp.json()
    else throw new Error(`[${resp.status} - ${resp.statusText}]`)
    console.log(API.homeJsonDoc)
}

function buildTemplatePath({hrefTemplate, hrefVars}, params) {
    let uri = hrefTemplate
    Object.keys(hrefVars).forEach(param => {
        uri = uri.replace(`{${param}}`, params[param])
    })
    return uri
}

async function fetchGetRequest(uri) {
    const options = {
        method: 'GET',
        headers: { 'Accept': 'application/vnd.siren+json' }
    }
    const resp = await fetch(uri, options)
    if(resp.ok) return await resp.json()
    else throw new Error(`Error on getting request from '${uri}' - [${resp.status} - ${resp.statusText}]`)
}

// ENTITIES LISTS
export async function getProjectsHateoas() {
    if(!API.homeJsonDoc.resources)
        await getHomeJsonDocument()
    const path = API.host + API.homeJsonDoc.resources[API.relations.projects].href
    return await fetchGetRequest(path)
}

export async function getContributorsHateoas(pname) {
    if(!API.homeJsonDoc.resources)
        await getHomeJsonDocument()
    const path = API.host + buildTemplatePath(API.homeJsonDoc.resources[API.relations.contributors], {pname})
    return await fetchGetRequest(path)
}

export async function getIssuesHateoas(pname) {
    if(!API.homeJsonDoc.resources)
        await getHomeJsonDocument()
    const path = API.host + buildTemplatePath(API.homeJsonDoc.resources[API.relations.issues], {pname})
    return await fetchGetRequest(path)
}

export async function getCommentsHateoas(pname, iid) {
    if(!API.homeJsonDoc.resources)
        await getHomeJsonDocument()
    const path = API.host + buildTemplatePath(API.homeJsonDoc.resources[API.relations.comments], {pname, iid})
    return await fetchGetRequest(path)
}

export async function getUsernames() {
    const resp = await fetch(`${API.host}/users`, {
        method: 'GET',
        headers: { 'Accept': 'application/json' }
    })
    return await resp.json()
}

// ENTITIES DETAILS
export async function getProjectDetailsHateoas(pname) {
    if(!API.homeJsonDoc.resources)
        await getHomeJsonDocument()
    const path = API.host + buildTemplatePath(API.homeJsonDoc.resources[API.relations.project], {pname})
    return await fetchGetRequest(path)
}

export async function getIssueDetailsHateoas(pname, iid) {
    if(!API.homeJsonDoc.resources)
        await getHomeJsonDocument()
    const path = API.host + buildTemplatePath(API.homeJsonDoc.resources[API.relations.issue], {pname, iid})
    return await fetchGetRequest(path)
}

export async function getContributorDetailsHateoas(pname, username) {
    if(!API.homeJsonDoc.resources)
        await getHomeJsonDocument()
    const path = API.host + buildTemplatePath(API.homeJsonDoc.resources[API.relations.contributor], {pname, username})
    return await fetchGetRequest(path)
}

export async function getCommentDetailsHateoas(pname, iid, cid) {
    if(!API.homeJsonDoc.resources)
        await getHomeJsonDocument()
    const path = API.host + buildTemplatePath(API.homeJsonDoc.resources[API.relations.comment], {pname, iid, cid})
    return await fetchGetRequest(path)
}

export let loginService = LoginService(API.host)

export async function fetchActionRequest(action, body) {
    const options = {
        method: action.method,
        headers: {
            'Content-Type': action.type,
            'Authorization': loginService.getToken()     //getBearerAuthorization()
        },
        body: JSON.stringify(body)
    }
    return fetchWithTimeout(2000, fetch(`${API.host}${action.href}`, options))
        .then(response => response)
        .catch(err => err)
}

function fetchWithTimeout(ms, promise) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        reject(new Error("timeout"))
      }, ms)
      promise.then(resolve, reject)
    })
  }

export function getAction(actions, actual) {
    return actions.find(action => action.name === actual)
}