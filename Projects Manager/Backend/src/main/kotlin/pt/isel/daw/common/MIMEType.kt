package pt.isel.daw.common

import org.springframework.http.MediaType

object MIMEType {

    const val APPLICATION_JSON_VALUE = "application/json"
    const val APPLICATION_JSON_HOME_VALUE = "application/json-home"
    const val APPLICATION_JSON_SIREN_VALUE = "application/vnd.siren+json"

    val APPLICATION_JSON_SIREN = MediaType("application", "vnd.siren+json")
}