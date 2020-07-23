package pt.isel.daw.model

data class StateTransition(
        val pName: String,
        val state: IssueState,
        val nextState: IssueState
)