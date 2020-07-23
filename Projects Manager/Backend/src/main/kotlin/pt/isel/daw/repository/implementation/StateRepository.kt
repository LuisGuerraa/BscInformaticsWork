package pt.isel.daw.repository.implementation

import org.springframework.dao.DataAccessException
import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pt.isel.daw.exceptions.DatabaseAccessException
import pt.isel.daw.exceptions.NoStateForProjectException
import pt.isel.daw.exceptions.UniqueAttributeException
import pt.isel.daw.model.IssueState
import pt.isel.daw.repository.contract.IStateRepository
import pt.isel.daw.repository.jdbc.Transitions
import java.sql.PreparedStatement
import java.sql.Types
import javax.sql.DataSource
import pt.isel.daw.repository.util.RepositoryInfo as Info

@Repository
class StateRepository(private val jdbcTemplate: JdbcTemplate) : IStateRepository {

    override fun insertStateOnProject(pname: String, state: IssueState) {
        try {
            val ds: DataSource = jdbcTemplate.dataSource
                    ?: throw DatabaseAccessException("Database connection not established.")
            val connection = ds.connection
            connection.use {
                val stmt: PreparedStatement = connection.prepareStatement(Transitions.INSERT_PROJECT_STATES)
                stmt.use {
                    stmt.setObject(1, pname, Types.VARCHAR)
                    stmt.setObject(2, state.name, Types.OTHER)
                    stmt.executeUpdate()
                }
            }
        } catch (e: DuplicateKeyException) {
            throw UniqueAttributeException("IssueState '${state.name}' already exists on Project '${pname}'.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun getProjectStates(pname: String): MutableList<IssueState> {
        try {
            return jdbcTemplate.query(Transitions.SELECT_PROJECT_STATES, arrayOf(pname), Info.stateInfo)
        } catch (e: DatabaseAccessException) {
            throw NoStateForProjectException("The project $pname has no state provided.")
        }
    }

    override fun deleteProjectStates(pname: String): Boolean {
        return jdbcTemplate.update(Transitions.DELETE_ISSUE_STATES_PROJECT, pname) == 1
    }


}
