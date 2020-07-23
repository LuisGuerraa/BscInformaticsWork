package pt.isel.daw.repository.implementation

import org.springframework.dao.DataAccessException
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pt.isel.daw.exceptions.AuthorizationException
import pt.isel.daw.exceptions.DatabaseAccessException
import pt.isel.daw.exceptions.EntityNotFoundException
import pt.isel.daw.exceptions.UniqueAttributeException
import pt.isel.daw.model.Contributor
import pt.isel.daw.model.Project
import pt.isel.daw.repository.contract.IProjectRepository
import pt.isel.daw.repository.jdbc.Contributors
import pt.isel.daw.repository.jdbc.Projects
import pt.isel.daw.repository.util.RepositoryInfo as Info

@Repository
class ProjectRepository(private val jdbcTemplate: JdbcTemplate) : IProjectRepository {

    override fun createProject(project: Project): String {
        try {
            return jdbcTemplate.queryForObject(Projects.INSERT_PROJECT,
                    arrayOf(project.name, project.description), String::class.java)
        } catch (e: DuplicateKeyException) {
            throw UniqueAttributeException("Project ${project.name} already exists.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun getProjects(): List<Project> {
        try {
            return jdbcTemplate.query(Projects.SELECT_PROJECTS, Info.projectInfo)
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun getProjectDetails(pname: String): Project {
        try {
            return jdbcTemplate.queryForObject(Projects.SELECT_PROJECT_NAME, arrayOf(pname), Info.projectInfo)
                    ?: throw DatabaseAccessException("Database connection not established.")
        } catch (e: IncorrectResultSizeDataAccessException) {
            throw EntityNotFoundException("Project $pname not found.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun updateProject(p: Project, pname: String): Boolean {
        try {
            return jdbcTemplate.update(Projects.UPDATE_PROJECT, p.name, p.description, pname) == 1
        } catch (e: DuplicateKeyException) {
            throw UniqueAttributeException("Project ${p.name} already exists.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun deleteProject(pname: String): Boolean {
        try {
            return jdbcTemplate.update(Projects.DELETE_PROJECT, pname) == 1
        } catch (e: IncorrectResultSizeDataAccessException) {
            throw EntityNotFoundException("Project $pname not found.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun createProjectContributor(pname: String, uid: Int): Int {
        try {
            return jdbcTemplate.queryForObject(Contributors.INSERT_PROJECT_CONTRIBUTOR, arrayOf(pname, uid), Int::class.java)
        } catch (e: DuplicateKeyException) {
            throw UniqueAttributeException("User is already a contributor.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun getProjectContributors(pname: String): List<Contributor> {
        try {
            return jdbcTemplate.query(Contributors.SELECT_PROJECT_CONTRIBUTORS, arrayOf(pname), Info.contributorInfo)
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database : " + e.message.toString())
        }
    }

    override fun getProjectContributor(uid: Int, pname: String): Contributor {
        try {
            return jdbcTemplate.queryForObject(Contributors.SELECT_PROJECT_CONTRIBUTOR, arrayOf(pname, uid), Info.contributorInfo)!!
        } catch (e: IncorrectResultSizeDataAccessException) {
            throw AuthorizationException("User is not a contributor and doesn't have permission to change project $pname.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun deleteProjectContributors(pname: String): Boolean {
        try {
            return jdbcTemplate.update(Contributors.DELETE_PROJECT_CONTRIBUTORS, pname) == 1
        } catch (e: IncorrectResultSizeDataAccessException) {
            throw EntityNotFoundException("Project $pname not found.")
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }

    override fun deleteProjectContributor(pname: String, uid: Int) {
        try {
            jdbcTemplate.update(Contributors.DELETE_PROJECT_CONTRIBUTOR, pname, uid)
        } catch (e: DataAccessException) {
            throw DatabaseAccessException("Error accessing database: " + e.message.toString())
        }
    }
}
