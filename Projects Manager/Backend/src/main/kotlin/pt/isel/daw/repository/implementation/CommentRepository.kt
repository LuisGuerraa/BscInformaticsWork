package pt.isel.daw.repository.implementation

import org.springframework.dao.DataAccessException
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pt.isel.daw.exceptions.DatabaseAccessException
import pt.isel.daw.exceptions.EntityNotFoundException
import pt.isel.daw.model.Issue
import pt.isel.daw.model.IssueComment
import pt.isel.daw.repository.contract.ICommentRepository
import pt.isel.daw.repository.jdbc.IssueComments
import java.sql.PreparedStatement
import java.sql.Types
import javax.sql.DataSource
import pt.isel.daw.repository.util.RepositoryInfo as Info

@Repository
class CommentRepository(
        private val jdbcTemplate: JdbcTemplate
) : ICommentRepository {
    override fun createIssueComment(issue: Issue, c: IssueComment): Int {
        try {
            val ds: DataSource = jdbcTemplate.dataSource
                    ?: throw DatabaseAccessException("Database connection not established.")
            val connection = ds.connection
            connection.use {
                val stmt: PreparedStatement = connection.prepareStatement(IssueComments.INSERT_ISSUE_COMMENT)
                stmt.use {
                    stmt.setObject(1, issue.id, Types.INTEGER)
                    stmt.setObject(2, issue.pName, Types.VARCHAR)
                    stmt.setObject(3, issue.stateType.name, Types.OTHER)
                    stmt.setObject(4, c.text, Types.VARCHAR)
                    stmt.setObject(5, c.creationDate, Types.VARCHAR)
                    stmt.setObject(6, c.createdBy, Types.VARCHAR)
                    val resultSet = stmt.executeQuery()
                    resultSet.next()
                    return resultSet.getInt(1)
                }
            }
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun getIssueComments(pname: String, issueId: Int): List<IssueComment> {
        try {
            return jdbcTemplate.query(IssueComments.SELECT_ISSUE_COMMENTS, arrayOf(pname, issueId), Info.commentInfo)
        } catch (e: IncorrectResultSizeDataAccessException) {
            throw EntityNotFoundException("No comments on issue '$issueId' found.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun getIssueCommentDetails(pname: String, issueID: Int, id: Int): IssueComment {
        try {
            return jdbcTemplate.queryForObject(
                    IssueComments.SELECT_ISSUE_COMMENT_NAME, arrayOf(pname, issueID, id), Info.commentInfo)
                    ?: throw DatabaseAccessException("Database connection not established.")
        } catch (e: IncorrectResultSizeDataAccessException) {
            throw EntityNotFoundException("Comment $id not found.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun updateIssueComment(text: String, pname: String, issueID: Int, commentID: Int): Boolean {
        try {
            return jdbcTemplate.update(IssueComments.UPDATE_ISSUE_COMMENT, text, pname, issueID, commentID) == 1
        } catch (e: IncorrectResultSizeDataAccessException) {
            throw EntityNotFoundException("Comment $commentID not found.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun deleteIssueComment(pname: String, issueID: Int, commentID: Int): Boolean {
        try {
            return jdbcTemplate.update(IssueComments.DELETE_ISSUE_COMMENT, pname, issueID, commentID) == 1
        } catch (e: IncorrectResultSizeDataAccessException) {
            throw EntityNotFoundException("Comment $commentID not found.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun deleteIssueComments(pname: String, issueID: Int): Boolean {
        try {
            return jdbcTemplate.update(IssueComments.DELETE_ISSUE_COMMENTS, pname, issueID) == 1
        } catch (e: IncorrectResultSizeDataAccessException) {
            throw EntityNotFoundException("Comments not found.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }
}