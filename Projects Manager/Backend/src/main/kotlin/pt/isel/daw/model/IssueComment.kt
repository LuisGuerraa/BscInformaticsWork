package pt.isel.daw.model

class IssueComment(
        var id: Int = -1,
        var issueID: Int,
        var pName: String,
        var text: String = "",
        var creationDate: String = "",
        var createdBy: String? = ""
) {
    fun editText(text: String) = IssueComment(id, issueID, pName, text, creationDate, createdBy)
}