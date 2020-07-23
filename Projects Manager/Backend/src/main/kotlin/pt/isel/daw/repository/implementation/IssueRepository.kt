package pt.isel.daw.repository.implementation

import org.springframework.dao.DataAccessException
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pt.isel.daw.exceptions.DatabaseAccessException
import pt.isel.daw.exceptions.EntityNotFoundException
import pt.isel.daw.exceptions.InvalidOperationException
import pt.isel.daw.model.Issue
import pt.isel.daw.repository.contract.IIssueRepository
import pt.isel.daw.repository.jdbc.Issues
import java.sql.PreparedStatement
import java.sql.Types
import javax.sql.DataSource
import pt.isel.daw.repository.util.RepositoryInfo as Info

@Repository
class IssueRepository(private val jdbcTemplate: JdbcTemplate) : IIssueRepository {

    override fun createIssue(username: String, issue: Issue): Int {
        try {
            return jdbcTemplate.queryForObject(Issues.INSERT_PROJECT_ISSUE,
                    arrayOf(issue.pName, issue.name, issue.description, username, issue.creationDate), Int::class.java)
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun getProjectIssues(pname: String): List<Issue> {
        try {
            return jdbcTemplate.query(Issues.SELECT_PROJECT_ISSUES, arrayOf(pname), Info.issueInfo)
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun getProjectIssueDetails(pname: String, id: Int): Issue {
        try {
            return jdbcTemplate.queryForObject(Issues.SELECT_PROJECT_ISSUE_NAME, arrayOf(pname, id), Info.issueInfo)!!
        } catch (e: IncorrectResultSizeDataAccessException) {
            throw EntityNotFoundException("Issue $id of project $pname not found.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun updateProjectIssue(issue: Issue, pname: String): Boolean {
        try {
            val ds: DataSource = jdbcTemplate.dataSource
                    ?: throw DatabaseAccessException("Database connection not established.")
            val connection = ds.connection
            connection.use {
                val stmt: PreparedStatement = connection.prepareStatement(Issues.UPDATE_PROJECT_ISSUE)
                stmt.use {
                    stmt.setObject(1, issue.name, Types.VARCHAR)
                    stmt.setObject(2, issue.description, Types.VARCHAR)
                    stmt.setObject(3, issue.stateType.name, Types.OTHER)
                    stmt.setObject(4, issue.closeDate, Types.VARCHAR)
                    stmt.setObject(5, issue.id, Types.INTEGER)
                    stmt.setObject(6, pname, Types.VARCHAR)
                    return stmt.executeUpdate() == 1
                }
            }
        } catch (e: DataAccessException) {
            throw InvalidOperationException("Invalid state. State [${issue.stateType}] does not exist or transition not possible.")
        }
    }

    override fun deleteProjectIssues(pname: String): Boolean {
        try {
            return jdbcTemplate.update(Issues.DELETE_PROJECT_ISSUES, pname) == 1
        } catch (e: IncorrectResultSizeDataAccessException) {
            throw EntityNotFoundException("Project $pname not found.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun deleteProjectIssue(pname: String, id: Int): Boolean {
        try {
            return jdbcTemplate.update(Issues.DELETE_PROJECT_ISSUE, pname, id) == 1
        } catch (e: IncorrectResultSizeDataAccessException) {
            throw EntityNotFoundException("Issue $id not found.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }
}