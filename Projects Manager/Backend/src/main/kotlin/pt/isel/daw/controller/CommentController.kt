package pt.isel.daw.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.daw.common.Href
import pt.isel.daw.common.MIMEType
import pt.isel.daw.controller.representation.input.CommentInputModel
import pt.isel.daw.controller.representation.output.CommentOutputModel
import pt.isel.daw.controller.representation.output.CommentsOutputModel
import pt.isel.daw.controller.representation.output.toOutputModel
import pt.isel.daw.service.implementation.CommentService
import pt.isel.daw.service.implementation.ProjectService

@RestController
class CommentController(private val projectService: ProjectService, private val commentService: CommentService) {


    @PostMapping(path = [Href.Path.PROJECT_ISSUE_COMMENTS], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun postComment(@RequestAttribute("uid") uid: Int,
                    @PathVariable("name") pname: String,
                    @PathVariable("id") issueID: Int,
                    @RequestBody body: CommentInputModel
    ): ResponseEntity<String> {
        projectService.getProjectDetails(pname)
        val commentID = commentService.createIssueComment(uid, body.mapToComment(issueID, pname))
        return ResponseEntity.status(HttpStatus.CREATED).body("Comment $commentID created successfully for issue $issueID.")
    }

    @GetMapping(path = [Href.Path.PROJECT_ISSUE_COMMENTS],
            produces = [MediaType.APPLICATION_JSON_VALUE, MIMEType.APPLICATION_JSON_SIREN_VALUE])
    fun getComments(@PathVariable("name") pname: String,
                    @PathVariable("id") issueID: Int
    ): CommentsOutputModel {
        projectService.getProjectDetails(pname)
        val comments = commentService.getIssueComments(pname, issueID).map { it.toOutputModel(pname) }
        return CommentsOutputModel(pname, issueID, comments.size, comments)
    }

    @GetMapping(path = [Href.Path.PROJECT_ISSUE_COMMENT],
            produces = [MediaType.APPLICATION_JSON_VALUE, MIMEType.APPLICATION_JSON_SIREN_VALUE])
    fun getComment(@PathVariable("name") pname: String,
                   @PathVariable("id") issueID: Int,
                   @PathVariable("cid") commentID: Int): CommentOutputModel {
        projectService.getProjectDetails(pname)
        return commentService.getIssueCommentDetails(pname, issueID, commentID).toOutputModel(pname)
    }


    @PutMapping(path = [Href.Path.PROJECT_ISSUE_COMMENT], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun putComment(@RequestAttribute("uid") uid: Int,
                   @PathVariable("name") pname: String,
                   @PathVariable("id") issueID: Int,
                   @PathVariable("cid") commentID: Int,
                   @RequestBody body: CommentInputModel
    ): ResponseEntity<String> {
        projectService.getProjectDetails(pname)
        commentService.updateIssueComment(uid, body.mapToComment(commentID, issueID, pname))
        return ResponseEntity.status(HttpStatus.OK).body("Comment $commentID edited successfully")
    }

    @DeleteMapping(path = [Href.Path.PROJECT_ISSUE_COMMENT])
    fun deleteComment(@RequestAttribute("uid") uid: Int,
                      @PathVariable("name") pname: String,
                      @PathVariable("id") issueID: Int,
                      @PathVariable("cid") commentID: Int
    ): ResponseEntity<Unit> {
        projectService.getProjectDetails(pname)
        commentService.deleteIssueComment(uid, pname, issueID, commentID)
        return ResponseEntity.noContent().build()
    }
}