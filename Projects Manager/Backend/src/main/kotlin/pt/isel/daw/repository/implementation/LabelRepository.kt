package pt.isel.daw.repository.implementation

import org.springframework.dao.DataAccessException
import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pt.isel.daw.exceptions.DatabaseAccessException
import pt.isel.daw.exceptions.UniqueAttributeException
import pt.isel.daw.model.Issue
import pt.isel.daw.model.IssueLabel
import pt.isel.daw.repository.contract.ILabelRepository
import java.sql.PreparedStatement
import java.sql.Types
import javax.sql.DataSource
import pt.isel.daw.repository.jdbc.Issue_IssueLabels as LabelIssue
import pt.isel.daw.repository.jdbc.Project_IssueLabels as LabelProject
import pt.isel.daw.repository.util.RepositoryInfo as Info

@Repository
class LabelRepository(private val jdbcTemplate: JdbcTemplate) : ILabelRepository {

    override fun insertLabelOnProject(pname: String, issueLabel: IssueLabel) {
        try {
            val ds: DataSource = jdbcTemplate.dataSource
                    ?: throw DatabaseAccessException("Database connection not established.")
            val connection = ds.connection
            connection.use {
                val stmt: PreparedStatement = connection.prepareStatement(LabelProject.INSERT_PROJECT_LABELS)
                stmt.use {
                    stmt.setObject(1, pname, Types.VARCHAR)
                    stmt.setObject(2, issueLabel.name, Types.OTHER)
                    stmt.executeUpdate()
                }
            }
        } catch (e: DuplicateKeyException) {
            throw UniqueAttributeException("IssueLabel '${issueLabel.name}' already exists on Project '${pname}'.")
        }
    }

    override fun getProjectLabels(pname: String): MutableList<IssueLabel> {
        try {
            return jdbcTemplate.query(LabelProject.SELECT_PROJECT_LABELS, arrayOf(pname), Info.labelInfo)
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun deleteProjectLabels(pname: String): Boolean {
        try {
            return jdbcTemplate.update(LabelProject.DELETE_ISSUE_LABELS_PROJECT, pname) == 1
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun insertLabelOnIssue(i: Issue, issueLabel: IssueLabel) {
        try {
            val ds: DataSource = jdbcTemplate.dataSource
                    ?: throw DatabaseAccessException("Database connection not established.")
            val connection = ds.connection
            connection.use {
                val stmt: PreparedStatement = connection.prepareStatement(LabelIssue.INSERT_PROJECT_ISSUE_LABEL)
                stmt.use {
                    stmt.setObject(1, i.pName, Types.VARCHAR)
                    stmt.setObject(2, i.id, Types.INTEGER)
                    stmt.setObject(3, i.stateType, Types.OTHER)
                    stmt.setObject(4, issueLabel, Types.OTHER)
                    stmt.executeUpdate()
                }
            }
        } catch (e: DuplicateKeyException) {
            throw UniqueAttributeException("Label on issue ${i.id} already exists.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun getIssueLabels(id: Int): MutableList<IssueLabel> {
        try {
            return jdbcTemplate.query(LabelIssue.SELECT_PROJECT_ISSUE_LABELS, arrayOf(id), Info.labelInfo)
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun deleteIssueLabels(pname: String, id: Int): Boolean {
        try {
            return jdbcTemplate.update(LabelIssue.DELETE_PROJECT_ISSUE_LABEL, id, pname) == 1
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }
}