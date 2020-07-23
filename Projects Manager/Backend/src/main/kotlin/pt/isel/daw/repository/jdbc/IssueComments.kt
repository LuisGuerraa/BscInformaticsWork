package pt.isel.daw.repository.jdbc

object IssueComments {

    const val INSERT_ISSUE_COMMENT = "INSERT INTO issuecomment (IssueID, ProjectName,StateType,Text,CreationDate,CreatedBy) " +
            "VALUES (?, ?, ?, ?, ?, ?) RETURNING id"

    const val SELECT_ISSUE_COMMENTS = "SELECT * FROM issuecomment WHERE projectname = ? AND issueid = ?"

    const val SELECT_ISSUE_COMMENT_NAME = "$SELECT_ISSUE_COMMENTS AND id = ?"

    const val UPDATE_ISSUE_COMMENT = "UPDATE issuecomment SET Text = ? , CreationDate = CURRENT_DATE " +
            "WHERE ProjectName = ? AND IssueID = ? AND id = ?"

    const val DELETE_ISSUE_COMMENTS = "DELETE FROM issuecomment WHERE ProjectName = ? AND IssueID = ?"

    const val DELETE_ISSUE_COMMENT = "$DELETE_ISSUE_COMMENTS AND id = ?"
}