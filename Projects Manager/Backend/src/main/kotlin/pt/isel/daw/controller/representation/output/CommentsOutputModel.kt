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


data class CommentsOutputModelSiren(
        override val properties: Map<String, Any?>,
        override val links: List<SirenLink>,
        override val actions: List<SirenAction>,
        override val entities: List<SirenEntity>
) : Siren(listOf(Classes.COMMENTS), properties, links, actions, entities)

data class CommentsOutputModel(
        @JsonIgnore
        val project: String,
        @JsonIgnore
        val issueID: Int,
        val count: Int,
        val comments: List<CommentOutputModel>
) : IOutputModel {
    override fun toSirenOutputModel() = CommentsOutputModelSiren(
            mapOf("count" to comments.size),
            listOf(
                    SirenLink(
                            listOf(Relations.SELF, Relations.COMMENTS, Relations.COLLECTION),
                            Href.forComments(project, issueID),
                            "Issue $issueID's Comments"),
                    SirenLink(
                            listOf(Relations.ISSUE, Relations.PARENT),
                            Href.forIssue(project, issueID),
                            "Issue $issueID")),
            listOf(
                    SirenAction(
                            Actions.POST_COMMENT,
                            Href.forComments(project, issueID),
                            "Create comment",
                            listOf(Classes.COMMENTS),
                            HttpMethod.POST,
                            MediaType.APPLICATION_JSON_VALUE,
                            listOf(
                                    SirenField("text", "text")))),
            comments.map {
                SirenEntity(
                        listOf(Classes.COMMENT),
                        mapOf(
                                "id" to it.id,
                                "issueID" to it.issueID,
                                "text" to it.text,
                                "creationDate" to it.creationDate,
                                "createdBy" to it.createdBy,
                                "pName" to it.pName
                        ),
                        listOf(SirenLink(
                                listOf(Relations.SELF),
                                Href.forComment(it.pName, it.issueID, it.id),
                                "Comment ${it.id}"
                        ))
                )
            }
    )
}
