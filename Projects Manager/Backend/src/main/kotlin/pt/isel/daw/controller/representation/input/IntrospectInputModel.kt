package pt.isel.daw.controller.representation.input;

import pt.isel.daw.model.Introspect

data class IntrospectInputModel(
        val active: String,
        val sub: String,
        val username: String,
        val client_id: String,
        val scope: String )
{
    fun toIntrospect() = Introspect(
            active = active,
            sub = sub,
            username = username,
            client_id = client_id,
            scope = scope)
}