package pt.isel.daw.repository.jdbc

object APIUsers {

    const val INSERT_USER = "INSERT INTO apiuser (username, password) VALUES (?, ?) RETURNING id"

    const val SELECT_USERS = "SELECT id, username, password FROM apiuser"

    const val SELECT_USERNAMES = "SELECT username FROM apiuser"

    const val SELECT_USER = "$SELECT_USERS WHERE id = ?"

    const val SELECT_USER_ID = "SELECT * FROM apiuser WHERE username = ?"

    const val SELECT_USER_AUTH = "SELECT id FROM apiuser WHERE username = ? AND password = ?"

    const val DELETE_USER = "DELETE FROM apiuser WHERE id = ?"
}