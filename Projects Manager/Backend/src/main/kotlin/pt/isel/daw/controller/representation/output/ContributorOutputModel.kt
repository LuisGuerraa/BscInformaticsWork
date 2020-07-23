package pt.isel.daw.controller.representation.output

import org.springframework.http.HttpMethod
import pt.isel.daw.common.Actions
import pt.isel.daw.common.Classes
import pt.isel.daw.common.Href
import pt.isel.daw.common.Relations
import pt.isel.daw.controller.representation.output.contract.IOutputModel
import pt.isel.daw.controller.representation.output.representation.Siren
import pt.isel.daw.controller.representation.output.representation.SirenAction
import pt.isel.daw.controller.representation.output.representation.SirenLink
import pt.isel.daw.model.Contributor

data class ContributorOutputModelSiren(
        override val properties: Map<String, Any?>,
        override val links: List<SirenLink>,
        override val actions: List<SirenAction>
) : Siren(clazz = listOf(Classes.COMMENT),
        properties = properties,
        links = links,
        actions = actions,
        entities = listOf()
)

fun Contributor.toOutputModel() = ContributorOutputModel(userId, username, pName)

data class ContributorOutputModel(
        var userID: Int,
        var username: String,
        var pName: String
) : IOutputModel {
    override fun toSirenOutputModel() = ContributorOutputModelSiren(
            mapOf(
                    "userID" to userID,
                    "ProjectName" to pName
            ),
            listOf(
                    SirenLink(
                            listOf(Relations.SELF),
                            Href.forContributor(pName, username),
                            "Contributor $userID"
                    ),
                    SirenLink(
                            listOf(Relations.PROJECT),
                            Href.forProjectName(pName),
                            "Project $pName"
                    )
            ),
            listOf(
                    SirenAction(
                            Actions.DELETE_CONTRIBUTOR,
                            Href.forContributor(pName, username),
                            "Remove contributor",
                            listOf(Classes.CONTRIBUTOR),
                            HttpMethod.DELETE
                    )
            )
    )
}