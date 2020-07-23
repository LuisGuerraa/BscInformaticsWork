package pt.isel.daw.service.implementation

import org.springframework.stereotype.Service
import pt.isel.daw.exceptions.AuthenticationException
import pt.isel.daw.exceptions.EntityNotFoundException
import pt.isel.daw.model.User
import pt.isel.daw.repository.contract.IUserRepository
import pt.isel.daw.service.contract.IUserService

@Service
class UserService(
        private val userRepository: IUserRepository
) : IUserService {
    override fun signUp(user: User): Int = userRepository.createUser(user)

    override fun login(user: User): Int = userRepository.getUser(user.username, user.password)

    override fun getUser(name: String): User = userRepository.getUser(name)

    override fun getUsernames(): List<String> = userRepository.getUsernames()

    override fun getUser(uid: Int): User = userRepository.getUser(uid)

    override fun deleteAccount(username: String) {
        val uidPath = userRepository.getUser(username).id
        userRepository.deleteUser(uidPath)
    }


}