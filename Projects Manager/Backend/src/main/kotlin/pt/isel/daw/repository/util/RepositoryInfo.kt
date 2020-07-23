package pt.isel.daw.repository.util

import pt.isel.daw.model.*
import java.sql.ResultSet

object RepositoryInfo {

    val userInfo: (ResultSet, Int) -> User = { rs, _ ->
        User(rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"))
    }

    val usernameInfo: (ResultSet, Int) -> String = {
        rs, _ -> rs.getString("username")
    }

    val projectInfo: (ResultSet, Int) -> Project = { rs, _ ->
        Project(rs.getString("name"),
                rs.getString("description"))
    }

    val contributorInfo: (ResultSet, Int) -> Contributor = { rs, _ ->
        Contributor(
                rs.getString("projectName"),
                rs.getString("username"),
                rs.getInt("userid"))
    }

    val stateInfo: (ResultSet, Int) -> IssueState = { rs, _ ->
        IssueState.valueOf(rs.getString("stateType"))
    }

    val labelInfo: (ResultSet, Int) -> IssueLabel = { rs, _ ->
        IssueLabel.valueOf(rs.getString("labelType"))
    }

    val issueInfo: (ResultSet, Int) -> Issue = { rs, _ ->
        Issue(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("projectName"),
                rs.getString("description"),
                rs.getString("createdBy"),
                rs.getString("creationDate"),
                rs.getString("closeDate"),
                IssueState.valueOf(rs.getString("stateType")))
    }

    val commentInfo: (ResultSet, Int) -> IssueComment = { rs, _ ->
        IssueComment(
                rs.getInt("id"),
                rs.getInt("issueID"),
                rs.getString("ProjectName"),
                rs.getString("Text"),
                rs.getString("CreationDate"),
                rs.getString("CreatedBy"))
    }
}