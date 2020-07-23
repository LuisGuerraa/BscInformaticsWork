package pt.isel.daw.controller.representation.output

import org.springframework.http.HttpMethod
import pt.isel.daw.common.*
import pt.isel.daw.controller.representation.output.contract.IOutputModel
import pt.isel.daw.controller.representation.output.representation.Siren
import pt.isel.daw.controller.representation.output.representation.SirenAction
import pt.isel.daw.controller.representation.output.representation.SirenField
import pt.isel.daw.controller.representation.output.representation.SirenLink
import pt.isel.daw.model.IssueLabel
import pt.isel.daw.model.IssueState
import pt.isel.daw.model.Project

data class ProjectOutputModelSiren(
        override val properties: Map<String, Any?>,
        override val links: List<SirenLink>,
        override val actions: List<SirenAction>
) : Siren(listOf(Classes.PROJECT), properties, links, actions)

fun Project.toOutputModel() = ProjectOutputModel(name, description, issueStates, issueLabels)

data class ProjectOutputModel(
        val name: String,
        val description: String?,
        val issueStates: MutableList<IssueState>,
        val issueLabels: MutableList<IssueLabel>
) : IOutputModel {
    override fun toSirenOutputModel() = ProjectOutputModelSiren(
            mapOf(
                    "name" to name,
                    "description" to description,
                    "issueStates" to issueStates,
                    "issueLabels" to issueLabels),
            listOf(
                    SirenLink(
                            listOf(Relations.SELF, Relations.PROJECT),
                            Href.forProjectName(name),
                            "Project $name"),
                    SirenLink(
                            listOf(Relations.PROJECTS, Relations.PARENT, Relations.COLLECTION),
                            Href.forProjects(),
                            "All Projects"),
                    SirenLink(
                            listOf(Relations.ISSUES, Relations.COLLECTION),
                            Href.forIssues(name),
                            "Project $name's Issues"),
                    SirenLink(
                            listOf(Relations.CONTRIBUTORS, Relations.COLLECTION),
                            Href.forContributors(name),
                            "Project $name's Contributors")),
            listOf(
                    SirenAction(
                            Actions.PUT_PROJECT,
                            Href.forProjectName(name),
                            "Update Project $name",
                            listOf(Classes.PROJECT),
                            HttpMethod.PUT,
                            MIMEType.APPLICATION_JSON_VALUE,
                            listOf(
                                    SirenField("name", "text"),
                                    SirenField("description", "text"),
                                    SirenField("issueState", "IssueState"),
                                    SirenField("issueLabel", "IssueLabel"))),
                    SirenAction(
                            Actions.DELETE_PROJECT,
                            Href.forProjectName(name),
                            "Delete Project $name",
                            listOf(Classes.PROJECT),
                            HttpMethod.DELETE,
                            MIMEType.APPLICATION_JSON_VALUE),
                    SirenAction(
                            Actions.PATCH_PROJECT_NAME,
                            Href.forProjectNewName(name),
                            "Patch Project $name's Name",
                            listOf(Classes.PROJECT),
                            HttpMethod.PATCH,
                            MIMEType.APPLICATION_JSON_VALUE,
                            listOf(SirenField("newName", "text"))),
                    SirenAction(
                            Actions.PATCH_PROJECT_DESCRIPTION,
                            Href.forProjectDescription(name),
                            "Patch project $name's Description",
                            listOf(Classes.PROJECT),
                            HttpMethod.PATCH,
                            MIMEType.APPLICATION_JSON_VALUE,
                            listOf(SirenField("description", "text")))
            )
    )
}

