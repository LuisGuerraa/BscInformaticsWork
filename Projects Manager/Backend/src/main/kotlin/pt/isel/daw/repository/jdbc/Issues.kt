package pt.isel.daw.repository.jdbc

object Issues {

    const val INSERT_PROJECT_ISSUE = "INSERT INTO issue(projectname,\"name\",description,createdby,statetype,creationdate,closedate) " +
            "VALUES(?,?,?,?,'open',?, null) returning id"

    const val SELECT_PROJECT_ISSUES = "SELECT * FROM ISSUE WHERE projectname = ? ORDER BY id"

    const val SELECT_PROJECT_ISSUE_NAME = "SELECT * FROM ISSUE WHERE projectname = ? AND id = ?"

    const val UPDATE_PROJECT_ISSUE = "UPDATE issue SET name = ?, description = ?, stateType = ? , closeDate = ? " +
            "WHERE id = ? and projectName = ?"

    const val DELETE_PROJECT_ISSUES = "DELETE FROM issue WHERE projectName = ?"

    const val DELETE_PROJECT_ISSUE = "$DELETE_PROJECT_ISSUES and id = ?"

}