package pt.isel.daw.controller.representation.output

import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import pt.isel.daw.common.Actions
import pt.isel.daw.common.Classes
import pt.isel.daw.common.Href
import pt.isel.daw.common.Relations
import pt.isel.daw.controller.representation.output.contract.IOutputModel
import pt.isel.daw.controller.representation.output.representation.*

data class ProjectsOutputModelSiren(
        override val properties: Map<String, Any?>,
        override val links: List<SirenLink>,
        override val actions: List<SirenAction>,
        override val entities: List<SirenEntity>
) : Siren(listOf(Classes.PROJECTS), properties, links, actions, entities)

data class ProjectsSirenOutputModel(
        val count: Int,
        val projects: List<ProjectOutputModel>
) : IOutputModel {

    override fun toSirenOutputModel() = ProjectsOutputModelSiren(
            mapOf("count" to projects.size),
            listOf(
                    SirenLink(
                            listOf(Relations.SELF, Relations.COLLECTION, Relations.PROJECTS),
                            Href.forProjects(),
                            "All Projects")
            ),
            listOf(
                    SirenAction(
                            Actions.POST_PROJECT,
                            Href.forProjects(),
                            "Create project",
                            listOf(Classes.PROJECTS),
                            HttpMethod.POST,
                            MediaType.APPLICATION_JSON_VALUE,
                            listOf(
                                    SirenField("name", "text"),
                                    SirenField("description", "text"),
                                    SirenField("issueState", "IssueState[]"),
                                    SirenField("issueLabel", "IssueLabel[]")
                            )
                    )
            ),
            projects.map {
                SirenEntity(
                        listOf(Classes.PROJECT),
                        mapOf(
                                "name" to it.name,
                                "description" to it.description,
                                "issueStates" to it.issueStates,
                                "issueLabels" to it.issueLabels
                        ),
                        listOf(
                                SirenLink(
                                        listOf(Relations.SELF),
                                        Href.forProjectName(it.name),
                                        "Project ${it.name}"
                                )
                        )
                )
            }
    )
}