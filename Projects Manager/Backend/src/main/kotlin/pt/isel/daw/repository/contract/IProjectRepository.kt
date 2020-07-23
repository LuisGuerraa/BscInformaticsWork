package pt.isel.daw.repository.contract

import org.springframework.transaction.annotation.Transactional
import pt.isel.daw.model.Contributor
import pt.isel.daw.model.Project

interface IProjectRepository {
    @Transactional
    fun createProject(project: Project): String

    fun getProjects(): List<Project>

    fun getProjectDetails(pname: String): Project

    fun updateProject(p: Project, pname: String): Boolean

    fun deleteProject(pname: String): Boolean

    fun deleteProjectContributors(pname: String): Boolean

    @Transactional
    fun createProjectContributor(pname: String, uid: Int): Int

    fun deleteProjectContributor(pname: String, uid: Int)

    fun getProjectContributors(pname: String): List<Contributor>

    fun getProjectContributor(uid: Int, pname: String): Contributor
}