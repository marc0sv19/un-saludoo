package utn.methodology.infrastructure.http.router

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException
import utn.methodology.application.commandhandlers.FollowHandler
import utn.methodology.infrastructure.persistence.Repositories.FollowerRepository
import utn.methodology.infrastructure.persistence.Config.connectToMongoDB
import utn.methodology.application.commandhandlers.FollowValidationException
import utn.methodology.domainentities.Follower

fun Application.followRouter() {
    val mongoDatabase = connectToMongoDB() // Conexión a la base de datos
    val followerRepository = FollowerRepository(mongoDatabase)
    val followHandler = FollowHandler(followerRepository)

    routing {
        post("/follow") {
            try {
                // Intentar recibir la solicitud
                val followRequest = call.receive<Follower>() // Deserializa automáticamente
                // Validaciones aquí
                if (followRequest.followerId.isBlank() || followRequest.followedId.isBlank()) {
                    throw FollowValidationException("Follower ID y Followed ID son requeridos")
                }

                // Aquí puedes seguir con la lógica de tu aplicación
                followHandler.followUser(followRequest.followerId, followRequest.followedId)

                // Responder con éxito
                call.respond(HttpStatusCode.Created, "El usuario ahora sigue al otro usuario")
            } catch (e: FollowValidationException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Datos inválidos")
            } catch (e: SerializationException) {
                call.respond(HttpStatusCode.BadRequest, "Error en el formato de la solicitud: ${e.message}")
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error al procesar la solicitud: ${e.message}")
            }
        }




        delete("/unfollow") {
            val followerId = call.request.queryParameters["followerId"]
            val followedId = call.request.queryParameters["followedId"]

            if (followerId.isNullOrBlank() || followedId.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Follower ID y Followed ID son requeridos")
                return@delete
            }

            followHandler.unfollowUser(followerId, followedId)
            call.respond(HttpStatusCode.NoContent) // 204 No Content para indicar que la operación fue exitosa
        }
    }
}