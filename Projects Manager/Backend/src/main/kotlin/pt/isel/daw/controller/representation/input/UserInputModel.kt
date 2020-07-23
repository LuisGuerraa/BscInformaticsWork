package pt.isel.daw.controller.representation.input

import pt.isel.daw.model.User

data class UserInputModel(val username: String, val password: String) {

    fun mapToUser() = User(username = username, password = password)
}