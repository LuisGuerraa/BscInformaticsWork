package pt.isel.daw.repository.contract

import org.springframework.transaction.annotation.Transactional
import pt.isel.daw.model.Issue
import pt.isel.daw.model.IssueComment

interface ICommentRepository {
    @Transactional
    fun createIssueComment(issue: Issue, c: IssueComment): Int

    fun getIssueComments(pname: String, issueId: Int): List<IssueComment>

    fun getIssueCommentDetails(pname: String, issueID: Int, id: Int): IssueComment

    fun updateIssueComment(text: String, pname: String, issueID: Int, commentID: Int): Boolean

    fun deleteIssueComment(pname: String, issueID: Int, commentID: Int): Boolean

    fun deleteIssueComments(pname: String, issueID: Int): Boolean
}