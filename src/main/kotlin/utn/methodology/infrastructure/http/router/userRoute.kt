package utn.methodology.infrastructure.http.router

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import utn.methodology.application.commandhandlers.ConfirmUserHandler
import utn.methodology.infrastructure.persistence.Config.connectToMongoDB
import utn.methodology.application.commands.UserCommands
import utn.methodology.infrastructure.http.actions.SaveUserAction
import utn.methodology.infrastructure.http.actions.FindUserByUserNameAction
import utn.methodology.infrastructure.persistence.Repositories.UserMongoRepository


fun Application.userRoutes(){
    val mongoDatabase=connectToMongoDB()//Conexion a base de datos
    val repository= UserMongoRepository(mongoDatabase)//Agrega el repositorio a la ruta
    val saveUserAction= SaveUserAction(ConfirmUserHandler(repository))//Agrega el controlador al action
    val findUserByUserNameAction = FindUserByUserNameAction(repository)

    routing{
 post("/users"){
     println("Received POST request to /users")
     val body=call.receive<UserCommands>()
     println("Body: $body")
     saveUserAction.execute(body)
     call.respond(HttpStatusCode.Created, mapOf("message" to "ok"))
 }

    get("/users/search") {
        val username = call.request.queryParameters["username"]
        if (username.isNullOrBlank()) {
            call.respond(HttpStatusCode.BadRequest, "El parámetro 'username' es requerido.")
            return@get
        }

        // Ahora llamamos al método 'execute' de la instancia 'searchUserAction'
        val user = findUserByUserNameAction.execute(username)
        if (user != null) {
            call.respond(HttpStatusCode.OK, user.toPrimitives())
        } else {
            call.respond(HttpStatusCode.NotFound, "Usuario no encontrado.")
        }
    }
}
}

