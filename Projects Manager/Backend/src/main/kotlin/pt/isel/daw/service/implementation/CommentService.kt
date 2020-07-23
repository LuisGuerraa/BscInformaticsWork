package pt.isel.daw.service.implementation

import org.springframework.stereotype.Service
import pt.isel.daw.exceptions.ArchivedCommentException
import pt.isel.daw.exceptions.AuthorizationException
import pt.isel.daw.exceptions.EntityNotFoundException
import pt.isel.daw.model.Issue
import pt.isel.daw.model.IssueComment
import pt.isel.daw.repository.implementation.CommentRepository
import pt.isel.daw.repository.implementation.IssueRepository
import pt.isel.daw.repository.implementation.ProjectRepository
import pt.isel.daw.repository.implementation.UserRepository
import pt.isel.daw.service.contract.ICommentService

@Service
class CommentService(
        private val issueRepository: IssueRepository,
        private val commentRepository: CommentRepository,
        private val userRepository: UserRepository,
        private val projectRepository: ProjectRepository
) : ICommentService {
    override fun createIssueComment(uid: Int, comment: IssueComment): Int {
        comment.createdBy = userRepository.getUser(uid).username
        val issue = checkIfIssueExists(comment.pName, comment.issueID)
        comment.creationDate = issue.getCurrentDate()
        if (issue.stateType.name.equals("archived"))
            throw ArchivedCommentException("Issue with id ${issue.id} already archived.")
        return commentRepository.createIssueComment(issue, comment)
    }

    override fun getIssueComments(pname: String, issueId: Int): List<IssueComment> {
        checkIfIssueExists(pname, issueId)
        return commentRepository.getIssueComments(pname, issueId)
    }

    override fun getIssueCommentDetails(pname: String, issueID: Int, id: Int): IssueComment =
            commentRepository.getIssueCommentDetails(pname, issueID, id)

    override fun updateIssueComment(uid: Int, oldComment: IssueComment): Boolean {
        checkIfIssueExists(oldComment.pName, oldComment.issueID)

        val comment = commentRepository
                .getIssueCommentDetails(oldComment.pName, oldComment.issueID, oldComment.id).editText(text = oldComment.text)

        val user = userRepository.getUser(uid).username
        if (user == comment.createdBy)
            return commentRepository.updateIssueComment(comment.text, comment.pName, comment.issueID, comment.id)
        else throw throw AuthorizationException("Comment doesn't belong to this user.")
    }

    override fun deleteIssueComment(uid: Int, pname: String, issueID: Int, commentID: Int): Boolean {
        val comment = commentRepository.getIssueCommentDetails(pname, issueID, commentID)
        val username = userRepository.getUser(uid).username
        return if (username == comment.createdBy)
            commentRepository.deleteIssueComment(pname, issueID, commentID)
        else
            try {
                projectRepository.getProjectContributor(uid, pname)
                commentRepository.deleteIssueComment(pname, issueID, commentID)
            } catch (e: EntityNotFoundException) {
                throw AuthorizationException("User does not have permission to delete comment.")
            }
    }

    override fun deleteIssueComments(uid: Int, issue: Issue): Boolean {
        projectRepository.getProjectContributor(uid, issue.pName)
        return commentRepository.deleteIssueComments(issue.pName, issue.id)
    }

    private fun checkIfIssueExists(pname: String, id: Int): Issue =
            issueRepository.getProjectIssueDetails(pname, id)
}