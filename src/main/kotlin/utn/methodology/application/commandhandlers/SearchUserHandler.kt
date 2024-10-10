package utn.methodology.application.commandhandlers

import utn.methodology.infrastructure.persistence.Repositories.UserMongoRepository
import utn.methodology.domainentities.User

class SearchUserHandler(private val repository: UserMongoRepository) {

    fun handler(username: String): User? {
        return try {
            repository.findByUsername(username) ?: throw UserNotFoundException("Usuario no encontrado: $username")
        } catch (ex: Exception) {
            // Manejo de errores, puedes loguear el error aquí si es necesario
            throw ex
        }
    }
}

// Puedes crear una excepción personalizada para manejar errores cuando no se encuentran usuarios
class UserNotFoundException(message: String) : Exception(message)