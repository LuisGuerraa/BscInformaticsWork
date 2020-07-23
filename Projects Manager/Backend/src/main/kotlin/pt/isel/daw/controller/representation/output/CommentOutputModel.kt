package pt.isel.daw.controller.representation.output

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
import pt.isel.daw.model.IssueComment

data class CommentOutputModelSiren(
        override val properties: Map<String, Any?>,
        override val links: List<SirenLink>,
        override val actions: List<SirenAction>
) : Siren(clazz = listOf(Classes.COMMENT),
        properties = properties,
        links = links,
        actions = actions,
        entities = listOf()
)

fun IssueComment.toOutputModel(pName: String) = CommentOutputModel(id, issueID, pName, text, creationDate, createdBy)

data class CommentOutputModel(
        var id: Int = -1,
        var issueID: Int,
        var pName: String,
        var text: String = "",
        var creationDate: String = "",
        var createdBy: String? = null
) : IOutputModel {
    override fun toSirenOutputModel() = CommentOutputModelSiren(
            mapOf(
                    "createdBy" to createdBy,
                    "text" to text,
                    "creationDate" to creationDate),
            listOf(
                    SirenLink(
                            listOf(Relations.SELF, Relations.COMMENT),
                            Href.forComment(pName, issueID, id),
                            "Comment $id"),
                    SirenLink(
                            listOf(Relations.COMMENTS, Relations.PARENT, Relations.COLLECTION),
                            Href.forComments(pName, issueID),
                            "Issue $issueID's Comments")),
            listOf(
                    SirenAction(
                            Actions.DELETE_COMMENT,
                            Href.forComment(pName, issueID, id),
                            "Delete comment",
                            listOf(Classes.COMMENT),
                            HttpMethod.DELETE
                    ),
                    SirenAction(
                            Actions.PUT_COMMENT,
                            Href.forComment(pName, issueID, id),
                            "Update comment",
                            listOf(Classes.COMMENT),
                            HttpMethod.PUT,
                            MediaType.APPLICATION_JSON_VALUE,
                            listOf(SirenField("text", "text"))
                    )
            )
    )
}