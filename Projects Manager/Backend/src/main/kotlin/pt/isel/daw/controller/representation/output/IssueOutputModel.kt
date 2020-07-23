package pt.isel.daw.controller.representation.output

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import pt.isel.daw.common.Actions
import pt.isel.daw.common.Classes
import pt.isel.daw.common.Href
import pt.isel.daw.common.Relations
import pt.isel.daw.controller.representation.output.contract.IOutputModel
import pt.isel.daw.controller.representation.output.representation.Siren
import pt.isel.daw.controller.representation.output.representation.SirenAction
import pt.isel.daw.controller.representation.output.representation.SirenField
import pt.isel.daw.controller.representation.output.representation.SirenLink
import pt.isel.daw.model.Issue
import pt.isel.daw.model.IssueLabel
import pt.isel.daw.model.IssueState

data class IssueOutputModelSiren(
        override val properties: Map<String, Any?>,
        override val links: List<SirenLink>,
        override val actions: List<SirenAction>
) : Siren(listOf(Classes.ISSUE), properties, links, actions)

fun Issue.toOutputModel() = IssueOutputModel(id, name, pName, description, createdBy, creationDate, closeDate, stateType, labelType)

data class IssueOutputModel(
        var id: Int,
        var name: String,
        @JsonIgnore
        var pName: String,
        var description: String,
        var createdBy: String,
        var creationDate: String,
        var closeDate: String?,
        var stateType: IssueState = IssueState.open,
        var labelType: MutableList<IssueLabel>
) : IOutputModel {
    override fun toSirenOutputModel() = IssueOutputModelSiren(
            mapOf(
                    "id" to id,
                    "name" to name,
                    "description" to description,
                    "createdBy" to createdBy,
                    "creationDate" to creationDate,
                    "closeDate" to closeDate,
                    "stateType" to stateType,
                    "labelType" to labelType),
            listOf(
                    SirenLink(
                            listOf(Relations.SELF, Relations.ISSUE),
                            Href.forIssue(pName, id),
                            "Issue $id"),
                    SirenLink(
                            listOf(Relations.ISSUES, Relations.PARENT, Relations.COLLECTION),
                            Href.forIssues(pName),
                            "Project $pName's Issues"),
                    SirenLink(
                            listOf(Relations.COMMENTS, Relations.COLLECTION),
                            Href.forComments(pName, id),
                            "Issue $id's Comments")),
            listOf(
                    SirenAction(
                            Actions.PUT_ISSUE,
                            Href.forIssue(pName, id),
                            "Update Issue $id",
                            listOf(Classes.ISSUE),
                            HttpMethod.PUT,
                            MediaType.APPLICATION_JSON_VALUE,
                            listOf(
                                    SirenField("name", "text"),
                                    SirenField("description", "text"),
                                    SirenField("labelType", "IssueLabel[]"))),
                    SirenAction(
                            Actions.PATCH_ISSUE_NAME,
                            Href.forIssueName(pName, id),
                            "Update Issue $id's Name",
                            listOf(Classes.ISSUE),
                            HttpMethod.PATCH,
                            MediaType.APPLICATION_JSON_VALUE,
                            listOf(
                                    SirenField("name", "text"))),
                    SirenAction(
                            Actions.PATCH_ISSUE_DESCRIPTION,
                            Href.forIssueDescription(pName, id),
                            "Update Issue $id's Description",
                            listOf(Classes.ISSUE),
                            HttpMethod.PATCH,
                            MediaType.APPLICATION_JSON_VALUE,
                            listOf(
                                    SirenField("description", "text"))),
                    SirenAction(
                            Actions.PATCH_ISSUE_STATE,
                            Href.forIssueState(pName, id),
                            "Update Issue $id's State",
                            listOf(Classes.ISSUE),
                            HttpMethod.PATCH,
                            MediaType.APPLICATION_JSON_VALUE,
                            listOf(
                                    SirenField("state", "text"))),
                    SirenAction(
                            Actions.DELETE_ISSUE,
                            Href.forIssue(pName, id),
                            "Delete Issue $id",
                            listOf(Classes.ISSUE),
                            HttpMethod.DELETE,
                            MediaType.APPLICATION_JSON_VALUE)
            )
    )
}

