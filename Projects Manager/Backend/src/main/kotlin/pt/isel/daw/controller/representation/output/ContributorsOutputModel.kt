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

data class ContributorsOutputModelSiren(
        override val properties: Map<String, Any?>,
        override val links: List<SirenLink>,
        override val actions: List<SirenAction>,
        override val entities: List<SirenEntity>
) : Siren(listOf(Classes.CONTRIBUTORS), properties, links, actions, entities)

data class ContributorsOutputModel(
        @JsonIgnore
        val project: String,
        val count: Int,
        val contributors: List<ContributorOutputModel>
) : IOutputModel {

    override fun toSirenOutputModel() = ContributorsOutputModelSiren(
            mapOf("count" to contributors.size),
            listOf(
                    SirenLink(
                            listOf(Relations.SELF, Relations.CONTRIBUTORS, Relations.COLLECTION),
                            Href.forContributors(project),
                            "All Contributors"),
                    SirenLink(
                            listOf(Relations.PROJECT, Relations.PARENT),
                            Href.forProjectName(project),
                            "Project $project")
            ),
            listOf(
                    SirenAction(
                            Actions.POST_CONTRIBUTOR,
                            Href.forContributors(project),
                            "Add contributor to project $project",
                            listOf(Classes.CONTRIBUTORS),
                            HttpMethod.POST,
                            MediaType.APPLICATION_JSON_VALUE,
                            listOf(
                                    SirenField("userID", "int"),
                                    SirenField("ProjectName", "text")
                            )
                    )
            ),
            contributors.map {
                SirenEntity(
                        listOf(Classes.CONTRIBUTOR),
                        mapOf(
                                "userID" to it.userID,
                                "username" to it.username,
                                "projectName" to it.pName),
                        listOf(
                                SirenLink(
                                        listOf(Relations.SELF),
                                        Href.forContributor(it.pName, it.username),
                                        "Contributor ${it.username}")
                        )
                )
            }
    )
}