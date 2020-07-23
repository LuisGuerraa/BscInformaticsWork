package pt.isel.daw.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Issue(
        var id: Int = -1,
        val name: String = "",
        val pName: String,
        val description: String = "",
        val createdBy: String = "",
        var creationDate: String = "",
        var closeDate: String? = "",
        val stateType: IssueState = IssueState.open,
        var labelType: MutableList<IssueLabel> = arrayListOf()
) {
    fun changeIssue(
            name: String = this.name,
            description: String = this.description,
            stateType: IssueState = this.stateType,
            closeDate: String? = this.closeDate
    ) = Issue(id, name, pName, description, createdBy, creationDate, closeDate, stateType, labelType)

    fun getCurrentDate(): String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
}
