package pt.isel.daw.service.contract

import pt.isel.daw.model.Issue
import pt.isel.daw.model.IssueState

interface IIssueService {
    fun createProjectIssue(uid: Int, issue: Issue): Int

    fun getProjectIssues(pname: String): List<Issue>

    fun getProjectIssueDetails(pname: String, id: Int): Issue

    fun updateProjectIssue(uid: Int, issue: Issue): Boolean

    fun updateProjectIssueName(uid: Int, pname: String, id: Int, newName: String)

    fun updateProjectIssueDescription(uid: Int, pname: String, id: Int, newDescription: String)

    fun updateProjectIssueState(uid: Int, pname: String, id: Int, nextState: IssueState)

    fun deleteProjectIssue(uid: Int, pname: String, id: Int)
}