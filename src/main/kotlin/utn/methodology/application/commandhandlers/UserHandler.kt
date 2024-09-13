package utn.methodology.application.commandhandlers

import utn.methodology.application.commands.UserCommands
import utn.methodology.domain.entities.UserCreate
import utn.methodology.infrastructure.persistence.Repositories.UserMongoRepository

class ConfirmUserHandler(
    private val userRepository: UserMongoRepository, // Implement this interface with your preferred data access layer (DAO) implementation.

) {
    fun handle(command: UserCommands) {

        val user = UserCreate.create(
            command.nombre,
            command.username,
            command.email,
            command.password
        )

        userRepository.save(user)

    }

}
