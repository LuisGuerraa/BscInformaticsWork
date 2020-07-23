package pt.isel.daw.exceptions.representation

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProblemJson(
        val type: String,
        val title: String,
        val detail: String,
        val status: Int
)