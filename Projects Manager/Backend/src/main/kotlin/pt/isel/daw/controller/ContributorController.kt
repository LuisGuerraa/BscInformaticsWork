package pt.isel.daw.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.daw.common.Href
import pt.isel.daw.common.MIMEType
import pt.isel.daw.controller.representation.input.ContributorInputModel
import pt.isel.daw.controller.representation.output.ContributorOutputModel
import pt.isel.daw.controller.representation.output.ContributorsOutputModel
import pt.isel.daw.controller.representation.output.toOutputModel
import pt.isel.daw.exceptions.EntityNotFoundException
import pt.isel.daw.service.implementation.ProjectService
import pt.isel.daw.service.implementation.UserService

@RestController
class ContributorController(private val projectService: ProjectService, private val userService: UserService) {

    @PostMapping(path = [Href.Path.PROJECT_CONTRIBUTORS], consumes = [MIMEType.APPLICATION_JSON_VALUE])
    fun postUser(@RequestAttribute("uid") uid: Int,
                 @PathVariable("name") pname: String,
                 @RequestBody body: ContributorInputModel
    ): ResponseEntity<String> {
        val user = userService.getUser(body.username)
        projectService.addContributorToProject(uid, pname, user.id)
        return ResponseEntity.status(HttpStatus.CREATED).body("Contributor ${user.id} added to project $pname.")
    }

    @GetMapping(path = [Href.Path.PROJECT_CONTRIBUTORS],
            produces = [MIMEType.APPLICATION_JSON_VALUE, MIMEType.APPLICATION_JSON_SIREN_VALUE])
    fun getContributors(@PathVariable("name") pname: String): ContributorsOutputModel {
        val contributors = projectService.getProjectContributors(pname).map { it.toOutputModel() }
        return ContributorsOutputModel(pname, contributors.size, contributors)
    }

    @GetMapping(path = [Href.Path.PROJECT_CONTRIBUTOR],
            produces = [MIMEType.APPLICATION_JSON_VALUE, MIMEType.APPLICATION_JSON_SIREN_VALUE])
    fun getContributor(@PathVariable("name") pname: String, @PathVariable("ctrName") username: String): ResponseEntity<ContributorOutputModel> {
        var userID: Int? = null
        projectService.getProjectContributors(pname).forEach{ if(it.username == username) userID = it.userId}
        if(userID == null) throw EntityNotFoundException("User $username not found.")
        return ResponseEntity.ok(ContributorOutputModel(userID!!, username, pname))
    }

    @DeleteMapping(path = [Href.Path.PROJECT_CONTRIBUTOR],
            produces = [MIMEType.APPLICATION_JSON_VALUE, MIMEType.APPLICATION_JSON_SIREN_VALUE])
    fun deleteUser(@RequestAttribute("uid") uidAuth: Int,
                   @PathVariable("name") pname: String,
                   @PathVariable("ctrName") ctrName: String): ResponseEntity<String> {
        val user = userService.getUser(ctrName)
        projectService.removeContributorFromProject(uidAuth, pname, user.id)
        return ResponseEntity.noContent().build()
    }
}