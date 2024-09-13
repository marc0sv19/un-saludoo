package utn.methodology.infrastructure.http.router

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import utn.methodology.application.commandhandlers.ConfirmUserHandler
import utn.methodology.infrastructure.http.actions.ConfirmUserAction
import utn.methodology.infrastructure.persistence.Config.connectToMongoDB
import utn.methodology.application.commands.UserCommands
import utn.methodology.infrastructure.persistence.Repositories.UserMongoRepository


fun Application.userRoutes(){
    val mongoDatabase=connectToMongoDB()//Conexion a base de datos
    val repository= UserMongoRepository(mongoDatabase)//Agrega el repositorio a la ruta
    val userAction= ConfirmUserAction(ConfirmUserHandler(repository))//Agrega el controlador al action

routing{
 post("/users"){
     println("Received POST request to /users")
     val body=call.receive<UserCommands>()
     println("Body: $body")
     userAction.execute(body)
     call.respond(HttpStatusCode.Created, mapOf("message" to "ok"))
 }
}
}

