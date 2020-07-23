package pt.isel.daw.controller.representation.input

import pt.isel.daw.model.IssueComment

data class CommentInputModel(val text: String) {

    fun mapToComment(id: Int, issueID: Int, pname: String) = IssueComment(id = id, issueID = issueID, pName = pname, text = text)

    fun mapToComment(issueID: Int, pname: String) = IssueComment(issueID = issueID, pName = pname, text = text)
}