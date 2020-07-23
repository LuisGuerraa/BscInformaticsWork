package pt.isel.daw.service.implementation

import org.springframework.stereotype.Service
import pt.isel.daw.model.Contributor
import pt.isel.daw.model.Project
import pt.isel.daw.repository.contract.ILabelRepository
import pt.isel.daw.repository.contract.IProjectRepository
import pt.isel.daw.repository.contract.IStateRepository
import pt.isel.daw.repository.implementation.IssueRepository
import pt.isel.daw.service.contract.IProjectService

@Service
class ProjectService(val projectRepository: IProjectRepository,
                     val stateRepository: IStateRepository,
                     val issueRepository: IssueRepository,
                     val labelRepository: ILabelRepository
) : IProjectService {

    override fun createProject(uid: Int, project: Project): String {
        val projectName = projectRepository.createProject(project)
        projectRepository.createProjectContributor(projectName, uid)
        insertStatesLabelsIntoProject(uid, project)
        return projectName
    }

    override fun getProjects(): List<Project> {
        val projects = projectRepository.getProjects()
        projects.forEach {
            it.issueStates = stateRepository.getProjectStates(it.name)
            it.issueLabels = labelRepository.getProjectLabels(it.name)
        }
        return projects
    }

    override fun getProjectDetails(name: String): Project {
        val project: Project = projectRepository.getProjectDetails(name)
        project.issueStates = stateRepository.getProjectStates(project.name)
        project.issueLabels = labelRepository.getProjectLabels(project.name)
        return project
    }

    override fun deleteProject(uid: Int, name: String) {
        validateProjectAndContributor(uid, name)
        issueRepository.deleteProjectIssues(name)
        stateRepository.deleteProjectStates(name)
        labelRepository.deleteProjectLabels(name)
        projectRepository.deleteProjectContributors(name)
        projectRepository.deleteProject(name)
    }

    override fun updateProject(uid: Int, project: Project, pname: String): Boolean {
        validateProjectAndContributor(uid, project.name)
        val projectUpdated = projectRepository.updateProject(project, pname)
        stateRepository.deleteProjectStates(project.name)
        labelRepository.deleteProjectLabels(project.name)

        insertStatesLabelsIntoProject(uid, project)

        return projectUpdated
    }

    override fun updateProjectName(uid: Int, newName: String, pname: String): Boolean {
        val project = validateProjectAndContributor(uid, pname).changeProject(name = newName)
        return projectRepository.updateProject(project, pname)
    }

    override fun updateProjectDescription(uid: Int, newDescription: String, pname: String): Boolean {
        val project = validateProjectAndContributor(uid, pname).changeProject(description = newDescription)
        return projectRepository.updateProject(project, pname)
    }

    override fun addContributorToProject(uid: Int, pname: String, newUserId: Int): Int {
        validateProjectAndContributor(uid, pname)
        projectRepository.createProjectContributor(pname, newUserId)
        val ctr = projectRepository.getProjectContributor(newUserId, pname)
        return ctr.userId
    }

    override fun getProjectContributors(pname: String): List<Contributor> = projectRepository.getProjectContributors(pname)

    override fun removeContributorFromProject(uid: Int, pname: String, delID: Int) {
        validateProjectAndContributor(uid, pname)
        projectRepository.deleteProjectContributor(pname, delID)
    }

    private fun insertStatesLabelsIntoProject(uid: Int, project: Project) {
        validateProjectAndContributor(uid, project.name)
        project.issueStates.forEach {
            stateRepository.insertStateOnProject(project.name, it)
        }
        project.issueLabels.forEach {
            labelRepository.insertLabelOnProject(project.name, it)
        }
    }

    private fun validateProjectAndContributor(uid: Int, pname: String): Project {
        val project = projectRepository.getProjectDetails(pname)
        projectRepository.getProjectContributor(uid, pname)
        return project
    }
}