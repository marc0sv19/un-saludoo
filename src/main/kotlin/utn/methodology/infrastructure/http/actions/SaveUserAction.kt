package utn.methodology.infrastructure.http.actions

import utn.methodology.application.commandhandlers.ConfirmUserHandler
import utn.methodology.application.commands.UserCommands

class SaveUserAction(
    private val handler: ConfirmUserHandler

)
{
    fun execute(body:UserCommands){
        body.validate().let{ //valida que los datos no sean nulos
            handler.handle(it)
        }

    }
}
