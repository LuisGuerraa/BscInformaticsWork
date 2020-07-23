package pt.isel.daw.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.daw.common.Href
import pt.isel.daw.common.MIMEType
import pt.isel.daw.controller.representation.input.ProjectInputModel
import pt.isel.daw.controller.representation.output.ProjectsSirenOutputModel
import pt.isel.daw.controller.representation.output.toOutputModel
import pt.isel.daw.service.contract.IProjectService

@RestController
class ProjectController(private val projectService: IProjectService) {

    @PostMapping(path = [Href.Path.PROJECTS], consumes = [MIMEType.APPLICATION_JSON_VALUE])
    fun postProject(@RequestAttribute("uid") uid: Int,
                    @RequestBody body: ProjectInputModel): ResponseEntity<String> {
        val pName = projectService.createProject(uid, body.toProject())
        return ResponseEntity.status(HttpStatus.CREATED).body("Project $pName created successfully.")
    }

    @GetMapping(path = [Href.Path.PROJECTS],
            produces = [MIMEType.APPLICATION_JSON_VALUE, MIMEType.APPLICATION_JSON_SIREN_VALUE])
    fun getProjects(): ProjectsSirenOutputModel {
        val projects = projectService.getProjects().map { it.toOutputModel() }
        return ProjectsSirenOutputModel(projects.size, projects)
    }

    @GetMapping(path = [Href.Path.PROJECT],
            produces = [MIMEType.APPLICATION_JSON_VALUE, MIMEType.APPLICATION_JSON_SIREN_VALUE])
    fun getProjectDetails(@PathVariable("name") name: String) = projectService.getProjectDetails(name).toOutputModel()

    @DeleteMapping(path = [Href.Path.PROJECT],
            produces = [MIMEType.APPLICATION_JSON_VALUE, MIMEType.APPLICATION_JSON_SIREN_VALUE])
    fun deleteProject(@RequestAttribute("uid") uid: Int,
                      @PathVariable("name") name: String): ResponseEntity<String> {
        projectService.deleteProject(uid, name)
        return ResponseEntity.noContent().build()
    }

    @PutMapping(path = [Href.Path.PROJECT],
            produces = [MIMEType.APPLICATION_JSON_VALUE, MIMEType.APPLICATION_JSON_SIREN_VALUE])
    fun putProject(@RequestAttribute("uid") uid: Int,
                   @PathVariable("name") name: String,
                   @RequestBody body: ProjectInputModel
    ): ResponseEntity<String> {
        projectService.updateProject(uid, body.toProject(), name)
        return ResponseEntity.status(HttpStatus.OK).body("Project $name updated successfully.")
    }

    @PatchMapping(path = [Href.Path.PROJECT_NEW_NAME], consumes = [MIMEType.APPLICATION_JSON_VALUE])
    fun patchProjectName(@RequestAttribute("uid") uid: Int,
                         @PathVariable("name") name: String,
                         @RequestBody body: ProjectInputModel.ProjectNameInputModel
    ): ResponseEntity<String> {
        projectService.updateProjectName(uid, body.name, name)
        return ResponseEntity.status(HttpStatus.OK).body("Project $name's name updated successfully to '${body.name}'.")
    }

    @PatchMapping(path = [Href.Path.PROJECT_DESCRIPTION], consumes = [MIMEType.APPLICATION_JSON_VALUE])
    fun patchProjectDescription(@RequestAttribute("uid") uid: Int,
                                @PathVariable("name") name: String,
                                @RequestBody body: ProjectInputModel.ProjectDescriptionInputModel
    ): ResponseEntity<String> {
        projectService.updateProjectDescription(uid, body.description, name)
        return ResponseEntity.status(HttpStatus.OK).body("Project $name's description updated successfully.")
    }
}