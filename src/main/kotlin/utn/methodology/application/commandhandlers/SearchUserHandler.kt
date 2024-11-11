package utn.methodology.application.commandhandlers

import utn.methodology.infrastructure.persistence.Repositories.UserMongoRepository
import utn.methodology.domainentities.User

class SearchUserHandler(private val repository: UserMongoRepository) {

    fun handler(username: String): User? {
        return try {
            repository.findByUsername(username) ?: throw UserNotFoundException("Usuario no encontrado: $username")
        } catch (ex: Exception) {

            throw ex
        }
    }
}


class UserNotFoundException(message: String) : Exception(message)