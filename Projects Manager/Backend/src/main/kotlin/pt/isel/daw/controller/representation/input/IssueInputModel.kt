package pt.isel.daw.controller.representation.input

import pt.isel.daw.model.Issue
import pt.isel.daw.model.IssueLabel
import pt.isel.daw.model.IssueState

data class IssueInputModel(
        val name: String,
        val description: String = "",
        val stateType: IssueState = IssueState.open,
        val labelType: MutableList<IssueLabel> = arrayListOf()
) {
    data class ProjectIssueNameInputModel(val newName: String)

    data class ProjectIssueDescriptionInputModel(val description: String)

    data class ProjectIssueStateInputModel(val nextState: IssueState)

    fun mapToIssue(pname: String) = Issue(pName = pname, name = name, description = description, labelType = labelType)
    fun mapToIssue(pname: String, id: Int) = Issue(id = id, pName = pname, name = name, description = description, stateType = stateType, labelType = labelType)
}

