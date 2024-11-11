package utn.methodology.application.commandhandlers

import utn.methodology.application.Contracts.UserRepository
import utn.methodology.application.commands.UserCommands
import utn.methodology.domainentities.User


class ConfirmUserHandler(
    private val userRepository: UserRepository, //Recibe el UserRepository

) {
    fun handle(command: UserCommands):Int {

        return try {

            command.validate() //Recibe un Command que es un objeto UserCommand, que contiene los datos del Usuario

//Si es exitoso, se crea un objeto User pasandole los datos obtenidos en el command
            val user = User.create(
                command.nombre,
                command.username,
                command.email,
                command.password
            )
//Una vez que se crea, se guarda en el repositorio
            userRepository.save(user)
            201 // OK, creado exitosamente
        } catch (e: IllegalArgumentException) {
            // Si ocurre una excepción en la validación
            400
        }
    }

}
