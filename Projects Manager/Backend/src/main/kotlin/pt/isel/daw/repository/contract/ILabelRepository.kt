package pt.isel.daw.repository.contract

import org.springframework.transaction.annotation.Transactional
import pt.isel.daw.model.Issue
import pt.isel.daw.model.IssueLabel

interface ILabelRepository {
    @Transactional
    fun insertLabelOnProject(pname: String, issueLabel: IssueLabel)

    fun getProjectLabels(pname: String): MutableList<IssueLabel>

    fun deleteProjectLabels(pname: String): Boolean

    @Transactional
    fun insertLabelOnIssue(i: Issue, issueLabel: IssueLabel)

    fun getIssueLabels(id: Int): MutableList<IssueLabel>

    fun deleteIssueLabels(pname: String, id: Int): Boolean
}