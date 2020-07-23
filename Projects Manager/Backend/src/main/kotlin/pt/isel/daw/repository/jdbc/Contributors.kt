package pt.isel.daw.repository.jdbc

object Contributors {

    const val INSERT_PROJECT_CONTRIBUTOR = "INSERT INTO contributor(projectName, userid) VALUES (?, ?) RETURNING userid"

    const val SELECT_PROJECT_CONTRIBUTORS = "SELECT projectname, userid, username FROM contributor" +
                                                "    INNER JOIN apiuser  on userid = id " +
                                                "    WHERE projectName = ?"

    const val SELECT_PROJECT_CONTRIBUTOR = "SELECT projectname, userid, username FROM contributor" +
                                                "    INNER JOIN apiuser  on userid = id " +
                                                "    WHERE projectName = ? AND userid = ?"

    const val SELECT_PROJECT_CONTRIBUTOR_BYUSERNAME = "SELECT projectname, userid, username FROM contributor" +
            "    INNER JOIN apiuser  on userid = id " +
            "    WHERE projectName = ? AND userid = ?"

    const val DELETE_PROJECT_CONTRIBUTORS = "DELETE FROM contributor WHERE projectName = ?"

    const val DELETE_PROJECT_CONTRIBUTOR = "$DELETE_PROJECT_CONTRIBUTORS AND userid = ?"

}