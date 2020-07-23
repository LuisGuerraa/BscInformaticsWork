package pt.isel.daw.model

data class Contributor(
        var pName: String = "",
        var username: String = "",
        var userId: Int = -1
) {
    constructor(pName: String, userId: Int) : this(pName, "", userId)
}