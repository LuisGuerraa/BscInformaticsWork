package pt.isel.daw.repository.contract

import pt.isel.daw.model.User

interface IUserRepository {

    fun createUser(user: User): Int

    fun getUser(username: String, password: String): Int

    fun getUsers(): List<User>

    fun getUser(uid: Int): User

    fun getUser(name: String): User

    fun getUsernames() : List<String>

    fun deleteUser(uid: Int): Boolean

}