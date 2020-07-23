package pt.isel.daw.controller

import com.google.gson.Gson
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.daw.common.Href
import pt.isel.daw.common.MIMEType
import pt.isel.daw.controller.representation.input.UserInputModel
import pt.isel.daw.service.implementation.UserService

@RestController
class UserController(private val userService: UserService) {

    @PostMapping(path = [Href.Path.USERS], consumes = [MIMEType.APPLICATION_JSON_VALUE],
            produces = [MIMEType.APPLICATION_JSON_VALUE])
    fun postUser(@RequestBody body: UserInputModel): ResponseEntity<String> {
        val userID = userService.signUp(body.mapToUser())
        return ResponseEntity.status(HttpStatus.CREATED).body("""{"id":${userID}}""")
    }

    @DeleteMapping(path = [Href.Path.USER], produces = [MIMEType.APPLICATION_JSON_VALUE])
    fun deleteUser(@PathVariable("username") username: String): ResponseEntity<String> {
        userService.deleteAccount(username)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @PostMapping(path = [Href.Path.USERS_LOGIN], consumes = [MIMEType.APPLICATION_JSON_VALUE],
            produces = [MIMEType.APPLICATION_JSON_VALUE])
    fun postUserLogin(@RequestBody body: UserInputModel): ResponseEntity<String> {
        val userId = userService.login(body.mapToUser())
        return ResponseEntity.ok("""{"id":${userId}}""")
    }

    @GetMapping(path = [Href.Path.USERS], produces = [MIMEType.APPLICATION_JSON_VALUE])
    fun getUsernames(): ResponseEntity<String> {
        val usersJson = Gson().toJson(userService.getUsernames())
        return ResponseEntity.status(HttpStatus.OK).body("""{"usernames": $usersJson}""")
    }
}