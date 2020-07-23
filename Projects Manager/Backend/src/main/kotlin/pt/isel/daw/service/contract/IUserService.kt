package pt.isel.daw.service.contract

import pt.isel.daw.model.User

interface IUserService {
    fun signUp(user: User): Int

    fun login(user: User): Int

    fun getUser(uid: Int): User

    fun getUser(name: String): User

    fun getUsernames(): List<String>

    fun deleteAccount(username: String)
}