package pt.isel.daw.model

data class Project(
        val name: String,
        val description: String? = null,
        var issueStates: MutableList<IssueState> = arrayListOf(),
        var issueLabels: MutableList<IssueLabel> = arrayListOf()
) {
    fun changeProject(
            name: String = this.name,
            description: String? = this.description,
            issueStates: MutableList<IssueState> = this.issueStates,
            issueLabels: MutableList<IssueLabel> = this.issueLabels
    ) = Project(name, description, issueStates, issueLabels)
}