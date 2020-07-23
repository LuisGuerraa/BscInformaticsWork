package pt.isel.daw.service.contract

import pt.isel.daw.model.Contributor
import pt.isel.daw.model.Project

interface IProjectService {
    fun createProject(uid: Int, project: Project): String

    fun getProjects(): List<Project>

    fun getProjectDetails(name: String): Project

    fun deleteProject(uid: Int, name: String)

    fun updateProject(uid: Int, project: Project, pname: String): Boolean

    fun updateProjectName(uid: Int, newName: String, pname: String): Boolean

    fun updateProjectDescription(uid: Int, newDescription: String, pname: String): Boolean


    fun addContributorToProject(uid: Int, pname: String, newUserId: Int): Int

    fun getProjectContributors(pname: String): List<Contributor>

    fun removeContributorFromProject(uid: Int, pname: String, delID: Int)
}