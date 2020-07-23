package pt.isel.daw.repository.jdbc

object Transitions {

    const val INSERT_PROJECT_STATES = "INSERT INTO transition(projectName, stateType) VALUES (?, ?)"

    const val SELECT_PROJECT_STATES = "SELECT statetype FROM Transition PS WHERE PS.ProjectName = ?"

    const val DELETE_ISSUE_STATES_PROJECT = "DELETE FROM Transition WHERE projectName = ?"
}