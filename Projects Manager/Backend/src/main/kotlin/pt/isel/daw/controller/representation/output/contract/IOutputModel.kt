package pt.isel.daw.controller.representation.output.contract

import pt.isel.daw.controller.representation.output.representation.Siren

interface IOutputModel {
    fun toSirenOutputModel(): Siren
}