package pt.isel.daw.repository.jdbc

object Project_IssueLabels {

    const val INSERT_PROJECT_LABELS = "INSERT INTO project_issuelabel(projectName, labelType) VALUES (?, ?)"

    const val SELECT_PROJECT_LABELS = "SELECT labeltype FROM Project_IssueLabel PL WHERE PL.ProjectName = ?"

    const val DELETE_ISSUE_LABELS_PROJECT = "DELETE FROM project_issuelabel WHERE projectName = ?"
}