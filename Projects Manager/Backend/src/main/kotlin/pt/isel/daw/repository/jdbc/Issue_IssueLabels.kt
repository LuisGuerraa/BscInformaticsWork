package pt.isel.daw.repository.jdbc

object Issue_IssueLabels {

    const val INSERT_PROJECT_ISSUE_LABEL = "INSERT INTO issue_issuelabel (projectname,issueid,statetype,labeltype) " +
            "VALUES (?,?,?,?)"

    const val SELECT_PROJECT_ISSUE_LABELS = "SELECT labeltype FROM issue_issuelabel WHERE issueid = ?"

    const val DELETE_PROJECT_ISSUE_LABEL = "DELETE FROM issue_issuelabel WHERE issueid = ? AND projectName = ?"
}