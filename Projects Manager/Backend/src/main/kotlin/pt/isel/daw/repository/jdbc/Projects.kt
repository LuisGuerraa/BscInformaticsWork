package pt.isel.daw.repository.jdbc

object Projects {

    const val INSERT_PROJECT = "INSERT INTO project(name, description) VALUES (?, ?) RETURNING name"

    const val SELECT_PROJECTS = "SELECT P.name, P.description FROM Project P"

    const val SELECT_PROJECT_NAME = "$SELECT_PROJECTS WHERE P.name = ?"

    const val UPDATE_PROJECT = "UPDATE project SET name = ?, description  = ? WHERE name = ?"

    const val DELETE_PROJECT = "DELETE FROM project WHERE name = ?"
}