package utn.methodology.infrastructure.http.actions

import utn.methodology.application.commandhandlers.ConfirmUserHandler
import utn.methodology.application.commands.UserCommands

class SaveUserAction(
    private val handler: ConfirmUserHandler //delega el proceso al ConfirUserHandler, despues de validar los datos

)
{
    fun execute(body:UserCommands){
        body.validate().let{ //valida que los datos no sean nulos
            handler.handle(it) //delega al ConfirmUserHandler para guardar el registro
        }

    }
}
//Action correspondiente para la creacion de usuarios, el SaveUserAction valida los datos del usuario,
// atraves del body.validate() y si es exitosa pasa los datos al handler para completar la creación.
