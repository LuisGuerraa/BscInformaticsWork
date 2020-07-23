package pt.isel.daw.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.daw.common.Href
import pt.isel.daw.common.MIMEType
import pt.isel.daw.controller.representation.input.IssueInputModel
import pt.isel.daw.controller.representation.output.IssueOutputModel
import pt.isel.daw.controller.representation.output.IssuesOutputModel
import pt.isel.daw.controller.representation.output.toOutputModel
import pt.isel.daw.service.implementation.IssueService
import pt.isel.daw.service.implementation.ProjectService

@RestController
class IssueController(private val projectService: ProjectService, private val issueService: IssueService) {

    @PostMapping(path = [Href.Path.PROJECT_ISSUES], consumes = [MIMEType.APPLICATION_JSON_VALUE])
    fun postIssue(@RequestAttribute("uid") uid: Int,
                  @PathVariable("name") pname: String,
                  @RequestBody body: IssueInputModel
    ): ResponseEntity<String> {
        projectService.getProjectDetails(pname)
        val issueId = issueService.createProjectIssue(uid, body.mapToIssue(pname))
        return ResponseEntity.status(HttpStatus.CREATED).body("Issue $issueId for project $pname created successfully.")
    }

    @GetMapping(path = [Href.Path.PROJECT_ISSUES],
            produces = [MIMEType.APPLICATION_JSON_VALUE, MIMEType.APPLICATION_JSON_SIREN_VALUE])
    fun getIssues(@PathVariable("name") pname: String): IssuesOutputModel {
        projectService.getProjectDetails(pname)
        val issues = issueService.getProjectIssues(pname).map { it.toOutputModel() }
        return IssuesOutputModel(pname, issues.size, issues)
    }

    @GetMapping(path = [Href.Path.PROJECT_ISSUE],
            produces = [MIMEType.APPLICATION_JSON_VALUE, MIMEType.APPLICATION_JSON_SIREN_VALUE])
    fun getIssue(@PathVariable("name") pname: String,
                 @PathVariable("id") id: Int): IssueOutputModel {
        projectService.getProjectDetails(pname)
        return issueService.getProjectIssueDetails(pname, id).toOutputModel()
    }

    @PutMapping(path = [Href.Path.PROJECT_ISSUE], consumes = [MIMEType.APPLICATION_JSON_VALUE])
    fun putIssue(@RequestAttribute("uid") uid: Int,
                 @PathVariable("name") pname: String,
                 @PathVariable("id") id: Int,
                 @RequestBody body: IssueInputModel
    ): ResponseEntity<String> {
        projectService.getProjectDetails(pname)
        issueService.updateProjectIssue(uid, body.mapToIssue(pname, id))
        return ResponseEntity.status(HttpStatus.OK).body("Issue $id updated successfully")
    }

    @PatchMapping(path = [Href.Path.PROJECT_ISSUE_NAME], consumes = [MIMEType.APPLICATION_JSON_VALUE])
    fun patchIssueName(@RequestAttribute("uid") uid: Int,
                       @PathVariable("name") pname: String,
                       @PathVariable("id") id: Int,
                       @RequestBody body: IssueInputModel.ProjectIssueNameInputModel
    ): ResponseEntity<String> {
        projectService.getProjectDetails(pname)
        issueService.updateProjectIssueName(uid, pname, id, body.newName)
        return ResponseEntity.status(HttpStatus.OK).body("Issue $id's name updated successfully")
    }

    @PatchMapping(path = [Href.Path.PROJECT_ISSUE_DESCRIPTION], consumes = [MIMEType.APPLICATION_JSON_VALUE])
    fun patchIssueDescription(@RequestAttribute("uid") uid: Int,
                              @PathVariable("name") pname: String,
                              @PathVariable("id") id: Int,
                              @RequestBody body: IssueInputModel.ProjectIssueDescriptionInputModel
    ): ResponseEntity<String> {
        projectService.getProjectDetails(pname)
        issueService.updateProjectIssueDescription(uid, pname, id, body.description)
        return ResponseEntity.status(HttpStatus.OK).body("Issue $id's description updated successfully")
    }

    @PatchMapping(path = [Href.Path.PROJECT_ISSUE_STATE], consumes = [MIMEType.APPLICATION_JSON_VALUE])
    fun patchIssueState(@RequestAttribute("uid") uid: Int,
                        @PathVariable("name") pname: String,
                        @PathVariable("id") id: Int,
                        @RequestBody body: IssueInputModel.ProjectIssueStateInputModel
    ): ResponseEntity<String> {
        projectService.getProjectDetails(pname)
        issueService.updateProjectIssueState(uid, pname, id, body.nextState)
        return ResponseEntity.status(HttpStatus.OK).body("Issue $id's state updated successfully")
    }

    @DeleteMapping(path = [Href.Path.PROJECT_ISSUE])
    fun deleteIssue(@RequestAttribute("uid") uid: Int,
                    @PathVariable("name") pname: String,
                    @PathVariable("id") id: Int)
            : ResponseEntity<String> {
        projectService.getProjectDetails(pname)
        issueService.deleteProjectIssue(uid, pname, id)
        return ResponseEntity.noContent().build()
    }

}