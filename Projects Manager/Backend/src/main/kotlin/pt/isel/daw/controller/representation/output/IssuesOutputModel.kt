package pt.isel.daw.controller.representation.output

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import pt.isel.daw.common.Actions
import pt.isel.daw.common.Classes
import pt.isel.daw.common.Href
import pt.isel.daw.common.Relations
import pt.isel.daw.controller.representation.output.contract.IOutputModel
import pt.isel.daw.controller.representation.output.representation.*

data class IssuesOutputModelSiren(
        override val properties: Map<String, Any?>,
        override val links: List<SirenLink>,
        override val actions: List<SirenAction>,
        override val entities: List<SirenEntity>
) : Siren(listOf(Classes.PROJECT), properties, links, actions, entities)

data class IssuesOutputModel(
        @JsonIgnore
        val project: String,
        val count: Int,
        val issues: List<IssueOutputModel>
) : IOutputModel {
    override fun toSirenOutputModel() = IssuesOutputModelSiren(
            mapOf("count" to issues.size),
            listOf(
                    SirenLink(
                            listOf(Relations.SELF, Relations.ISSUES, Relations.COLLECTION),
                            Href.forIssues(project),
                            "Project $project's Issues"),
                    SirenLink(
                            listOf(Relations.PROJECT, Relations.PARENT),
                            Href.forProjectName(project),
                            "Project $project")
            ),
            listOf(
                    SirenAction(
                            Actions.POST_ISSUE,
                            Href.forIssues(project),
                            "Create Issue",
                            listOf(Classes.ISSUES),
                            HttpMethod.POST,
                            MediaType.APPLICATION_JSON_VALUE,
                            listOf(
                                    SirenField("name", "text"),
                                    SirenField("description", "text"),
                                    SirenField("labelType", "IssueLabel[]")
                            )
                    )
            ),
            issues.map {
                SirenEntity(
                        listOf(Classes.ISSUE),
                        mapOf(
                                "id" to it.id,
                                "name" to it.name,
                                "description" to it.description,
                                "createdBy" to it.createdBy,
                                "creationDate" to it.creationDate,
                                "closeDate" to it.closeDate,
                                "stateType" to it.stateType,
                                "labelType" to it.labelType,
                                "pname" to it.pName
                        ),
                        listOf(SirenLink(
                                listOf(Relations.SELF),
                                Href.forIssue(project, it.id),
                                "Issue ${it.id}"
                        ))
                )
            }
    )
}