package pt.isel.daw.common

import org.springframework.web.util.UriTemplate

object Href {

    object Path {

        const val HOME = "/"

        const val PROJECTS = "/projects"
        const val PROJECT = "/projects/{name}"
        const val PROJECT_NEW_NAME = "/projects/{name}/name"
        const val PROJECT_STATES = "/projects/{name}/issueStates"
        const val PROJECT_LABELS = "/projects/{name}/issueLabels"
        const val PROJECT_DESCRIPTION = "/projects/{name}/description"

        const val PROJECT_ISSUES = "/projects/{name}/issues"
        const val PROJECT_ISSUE = "/projects/{name}/issues/{id}"
        const val PROJECT_CONTRIBUTORS = "/projects/{name}/contributors"
        const val PROJECT_CONTRIBUTOR = "/projects/{name}/contributors/{ctrName}"
        const val PROJECT_ISSUE_NAME = "/projects/{name}/issues/{id}/name"
        const val PROJECT_ISSUE_DESCRIPTION = "/projects/{name}/issues/{id}/description"
        const val PROJECT_ISSUE_STATE = "/projects/{name}/issues/{id}/state"
        const val PROJECT_ISSUE_LABELS = "/projects/{name}/issues/{id}/labels"

        const val PROJECT_ISSUE_COMMENTS = "/projects/{name}/issues/{id}/comments"
        const val PROJECT_ISSUE_COMMENT = "/projects/{name}/issues/{id}/comments/{cid}"

        const val USERS = "/users"
        const val USERS_LOGIN = "/users/login"
        const val USER = "/users/{username}"
    }

    fun forProjects() = UriTemplate(Path.PROJECTS).expand()
    fun forProjectName(name: String) = UriTemplate(Path.PROJECT).expand(name)
    fun forProjectNewName(name: String) = UriTemplate(Path.PROJECT_NEW_NAME).expand(name)
    fun forProjectDescription(name: String) = UriTemplate(Path.PROJECT_DESCRIPTION).expand(name)

    fun forIssues(pname: String) = UriTemplate(Path.PROJECT_ISSUES).expand(pname)
    fun forIssue(pname: String, id: Int) = UriTemplate(Path.PROJECT_ISSUE).expand(pname, id)
    fun forIssueName(pname: String, id: Int) = UriTemplate(Path.PROJECT_ISSUE_NAME).expand(pname, id)
    fun forIssueDescription(pname: String, id: Int) = UriTemplate(Path.PROJECT_ISSUE_DESCRIPTION).expand(pname, id)
    fun forIssueState(pname: String, id: Int) = UriTemplate(Path.PROJECT_ISSUE_STATE).expand(pname, id)

    fun forComments(pname: String, id: Int) = UriTemplate(Path.PROJECT_ISSUE_COMMENTS).expand(pname, id)
    fun forComment(pname: String, issueID: Int, cid: Int) = UriTemplate(Path.PROJECT_ISSUE_COMMENT).expand(pname, issueID, cid)

    fun forContributors(pname: String) = UriTemplate(Path.PROJECT_CONTRIBUTORS).expand(pname)
    fun forContributor(pname: String, ctrName: String) = UriTemplate(Path.PROJECT_CONTRIBUTOR).expand(pname, ctrName)
}
