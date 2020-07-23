package pt.isel.daw.controller.representation.input

import pt.isel.daw.model.IssueLabel
import pt.isel.daw.model.IssueState
import pt.isel.daw.model.Project

data class ProjectInputModel(
        val name: String,
        val description: String?,
        val issueStates: MutableList<IssueState> = arrayListOf(),
        val issueLabels: MutableList<IssueLabel> = arrayListOf()
) {
    data class ProjectNameInputModel(val name: String)

    data class ProjectDescriptionInputModel(val description: String)

    fun toProject() = Project(name = name, description = description, issueStates = issueStates, issueLabels = issueLabels)
}

