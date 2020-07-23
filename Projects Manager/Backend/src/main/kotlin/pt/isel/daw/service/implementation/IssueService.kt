package pt.isel.daw.service.implementation

import org.springframework.stereotype.Service
import pt.isel.daw.exceptions.InvalidOperationException
import pt.isel.daw.exceptions.InvalidTransitionException
import pt.isel.daw.model.Issue
import pt.isel.daw.model.IssueState
import pt.isel.daw.repository.implementation.*
import pt.isel.daw.service.contract.IIssueService

@Service
class IssueService(
        private val issueRepository: IssueRepository,
        private val projectRepository: ProjectRepository,
        private val labelRepository: LabelRepository,
        private val commentRepository: CommentRepository,
        private val userRepository: UserRepository
) : IIssueService {

    override fun createProjectIssue(uid: Int, issue: Issue): Int {
        projectRepository.getProjectContributor(uid, issue.pName)
        val username = userRepository.getUser(uid).username
        issue.creationDate = issue.getCurrentDate()
        val issueId = issueRepository.createIssue(username, issue)
        issue.id = issueId
        val projectLabels = labelRepository.getProjectLabels(issue.pName)
        issue.labelType.forEach {
            if (!projectLabels.contains(it)) {
                issueRepository.deleteProjectIssue(issue.pName, issueId)
                throw InvalidOperationException("Label '${it}' is not available in the group ${issue.pName} label's list")
            }
            labelRepository.insertLabelOnIssue(issue, it)
        }

        return issueId
    }

    override fun getProjectIssues(pname: String): List<Issue> {
        val issues = issueRepository.getProjectIssues(pname)
        issues.forEach {
            it.labelType = labelRepository.getIssueLabels(it.id)
        }
        return issues
    }

    override fun getProjectIssueDetails(pname: String, id: Int): Issue {
        projectRepository.getProjectDetails(pname)
        val issue = issueRepository.getProjectIssueDetails(pname, id)
        issue.labelType = labelRepository.getIssueLabels(id)
        return issue
    }

    override fun updateProjectIssue(uid: Int, issue: Issue): Boolean {
        projectRepository.getProjectContributor(uid, issue.pName)
        val projectName = issue.pName
        //Verify if issue exists
        issueRepository.getProjectIssueDetails(projectName, issue.id)

        //update issue labels table
        labelRepository.deleteIssueLabels(projectName, issue.id)

        //Get project labels
        val projectLabels = labelRepository.getProjectLabels(issue.pName)
        issue.labelType.forEach {
            if (!projectLabels.contains(it)) throw InvalidOperationException("Label '${it}' is not available in the group ${issue.pName} label's list")
            labelRepository.insertLabelOnIssue(issue, it)
        }

        //update issue
        return issueRepository.updateProjectIssue(issue, projectName)
    }

    override fun updateProjectIssueName(uid: Int, pname: String, id: Int, newName: String) {
        projectRepository.getProjectContributor(uid, pname)
        val issue = issueRepository.getProjectIssueDetails(pname, id)
        issueRepository.updateProjectIssue(issue.changeIssue(name = newName), pname)
    }

    override fun updateProjectIssueDescription(uid: Int, pname: String, id: Int, newDescription: String) {
        projectRepository.getProjectContributor(uid, pname)
        val issue = issueRepository.getProjectIssueDetails(pname, id)
        issueRepository.updateProjectIssue(issue.changeIssue(description = newDescription), pname)
    }

    override fun updateProjectIssueState(uid: Int, pname: String, id: Int, nextState: IssueState) {
        projectRepository.getProjectContributor(uid, pname)
        val issue = issueRepository.getProjectIssueDetails(pname, id)
        verifyStateTransition(nextState, issue)
        issueRepository.updateProjectIssue(issue.changeIssue(stateType = nextState, closeDate = issue.closeDate), pname)
    }

    override fun deleteProjectIssue(uid: Int, pname: String, id: Int) {
        projectRepository.getProjectContributor(uid, pname)
        issueRepository.getProjectIssueDetails(pname, id)
        labelRepository.deleteIssueLabels(pname, id)
        commentRepository.deleteIssueComments(pname, id)
        issueRepository.deleteProjectIssue(pname, id)
    }

    private fun verifyStateTransition(nextState: IssueState, issue: Issue) {
        if (issue.stateType.name == "archived")
            throw InvalidTransitionException("The issue ${issue.pName} - ${issue.id} is already archived and cannot be reopened.")

        if (nextState == IssueState.closed || nextState == IssueState.archived)
            issue.closeDate = issue.getCurrentDate()
        else if (nextState == IssueState.open) issue.closeDate = null
    }
}