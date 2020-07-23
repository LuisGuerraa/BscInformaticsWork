package pt.isel.daw.repository.implementation

import org.mindrot.jbcrypt.BCrypt
import org.springframework.dao.DataAccessException
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import org.springframework.stereotype.Repository
import pt.isel.daw.exceptions.DatabaseAccessException
import pt.isel.daw.exceptions.EntityNotFoundException
import pt.isel.daw.exceptions.UniqueAttributeException
import pt.isel.daw.model.User
import pt.isel.daw.repository.contract.IUserRepository
import pt.isel.daw.repository.jdbc.APIUsers
import pt.isel.daw.repository.util.RepositoryInfo as Info

@Repository
class UserRepository(private val jdbcTemplate: JdbcTemplate) : IUserRepository {
    override fun createUser(user: User): Int {
        try {
            if (user.username.isBlank() || user.password.isBlank())
                throw DatabaseAccessException("User or password cannot be blank!")
            return jdbcTemplate.queryForObject(APIUsers.INSERT_USER, arrayOf(user.username, BCrypt.hashpw(user.password,BCrypt.gensalt(12))))!!
        } catch (e: DuplicateKeyException) {
            throw UniqueAttributeException("User ${user.username} already exists.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun getUsers(): List<User> {
        try {
            return jdbcTemplate.query(APIUsers.SELECT_USERS, Info.userInfo)
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun getUsernames(): List<String> {
        try {
            return jdbcTemplate.query(APIUsers.SELECT_USERNAMES, Info.usernameInfo)
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun getUser(uid: Int): User {
        try {
            return jdbcTemplate.queryForObject(APIUsers.SELECT_USER, arrayOf(uid), Info.userInfo)!!
        } catch (e: IncorrectResultSizeDataAccessException) {
            throw EntityNotFoundException("User not found.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun getUser(username: String, password: String): Int {
        try {
            if (username.isBlank() || password.isBlank())
                throw DatabaseAccessException("User or password cannot be blank!")
            val user = getUser(username)
            if(BCrypt.checkpw(password,user.password))
                return user.id
            throw DatabaseAccessException("Password is incorrect!")
        } catch (e: IncorrectResultSizeDataAccessException) {
            throw EntityNotFoundException("User not found.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun getUser(name: String): User {
        try {
            return jdbcTemplate.queryForObject(APIUsers.SELECT_USER_ID, arrayOf(name), Info.userInfo)!!
        } catch (e: IncorrectResultSizeDataAccessException) {
            throw EntityNotFoundException("User not found.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun deleteUser(uid: Int): Boolean {
        try {
            return jdbcTemplate.update(APIUsers.DELETE_USER, uid) == 1
        } catch (e: IncorrectResultSizeDataAccessException) {
            throw EntityNotFoundException("User $uid not found.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }
}