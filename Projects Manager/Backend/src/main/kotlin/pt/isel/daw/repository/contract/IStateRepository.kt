package pt.isel.daw.repository.contract

import org.springframework.transaction.annotation.Transactional
import pt.isel.daw.model.IssueState

interface IStateRepository {
    @Transactional
    fun insertStateOnProject(pname: String, state: IssueState)

    fun getProjectStates(pname: String): MutableList<IssueState>

    fun deleteProjectStates(pname: String): Boolean
}