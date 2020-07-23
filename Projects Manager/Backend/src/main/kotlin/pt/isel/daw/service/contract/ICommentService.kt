package pt.isel.daw.service.contract

import pt.isel.daw.model.Issue
import pt.isel.daw.model.IssueComment

interface ICommentService {
    fun createIssueComment(uid: Int, comment: IssueComment): Int

    fun getIssueComments(pname: String, issueId: Int): List<IssueComment>

    fun getIssueCommentDetails(pname: String, issueID: Int, id: Int): IssueComment

    fun updateIssueComment(uid: Int, oldComment: IssueComment): Boolean

    fun deleteIssueComment(uid: Int, pname: String, issueID: Int, commentID: Int): Boolean

    fun deleteIssueComments(uid: Int, issue: Issue): Boolean

}