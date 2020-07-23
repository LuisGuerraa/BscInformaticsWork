package pt.isel.daw.common

object Actions {
    const val POST_PROJECT = "/actions/post/project"
    const val DELETE_PROJECT = "/actions/delete/project"
    const val PUT_PROJECT = "/actions/put/project"
    const val PATCH_PROJECT_NAME = "/actions/patch/project/name"
    const val PATCH_PROJECT_DESCRIPTION = "/actions/patch/project/description"

    const val POST_ISSUE = "/actions/post/issue"
    const val DELETE_ISSUE = "/actions/delete/issue"
    const val PUT_ISSUE = "/actions/put/issue"
    const val PATCH_ISSUE_NAME = "/actions/patch/issue/name"
    const val PATCH_ISSUE_DESCRIPTION = "/actions/patch/issue/description"
    const val PATCH_ISSUE_STATE = "/actions/patch/issue/state"

    const val POST_COMMENT = "/actions/post/comment"
    const val PUT_COMMENT = "/actions/put/comment"
    const val DELETE_COMMENT = "/actions/delete/comment"

    const val POST_CONTRIBUTOR = "/actions/post/contributor"
    const val DELETE_CONTRIBUTOR = "/actions/delete/contributor"
}