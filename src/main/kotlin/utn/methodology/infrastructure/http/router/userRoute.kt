package utn.methodology.infrastructure.http.router

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import utn.methodology.application.commandhandlers.*
import utn.methodology.infrastructure.persistence.Config.connectToMongoDB
import utn.methodology.application.commands.UserCommands
import utn.methodology.infrastructure.http.actions.SaveUserAction
import utn.methodology.infrastructure.http.actions.SearchUserAction
import utn.methodology.infrastructure.persistence.Repositories.PostMongoRepository
import utn.methodology.infrastructure.persistence.Repositories.UserMongoRepository

fun Application.userRoutes(){
    val mongoDatabase=connectToMongoDB() // Conexión a base de datos
    val repository = UserMongoRepository(mongoDatabase) // Agrega el repositorio a la ruta
    val saveUserAction = SaveUserAction(ConfirmUserHandler(repository)) // Controlador para guardar usuarios
    val searchUserHandler = SearchUserHandler(repository)
    val searchUserAction = SearchUserAction(searchUserHandler)

    // Llamamos a postRoutes
      // Aquí debes pasar la instancia de PostHandler

    routing {
        post("/users") {
            println("Received POST request to /users")
            val body = call.receive<UserCommands>()
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

            try {
                val user = searchUserAction.execute(username)  // Usar el action para buscar el usuario
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)  // Respondemos con el usuario serializado
                } else {
                    call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
                }
            } catch (ex: UserNotFoundException) {
                call.respond(HttpStatusCode.NotFound, ex.message ?: "Usuario no encontrado")
            } catch (ex: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error al procesar la solicitud")
            }
        }
    }
}

// Función postRoutes que maneja el POST para crear posts
