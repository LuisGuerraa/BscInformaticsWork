package pt.isel.daw.repository.contract

import org.springframework.transaction.annotation.Transactional
import pt.isel.daw.model.Issue

interface IIssueRepository {
    @Transactional
    fun createIssue(username: String, issue: Issue): Int

    fun getProjectIssues(pname: String): List<Issue>

    fun getProjectIssueDetails(pname: String, id: Int): Issue

    fun updateProjectIssue(issue: Issue, pname: String): Boolean

    fun deleteProjectIssue(pname: String, id: Int): Boolean

    fun deleteProjectIssues(pname: String): Boolean
}