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
                val followRequest = call.receive<Follower>() // Deserializa automáticamente
                if (followRequest.followerId.isBlank() || followRequest.followedId.isBlank()) {
                    throw FollowValidationException("Follower ID y Followed ID son requeridos")
                }

                followHandler.followUser(followRequest.followerId, followRequest.followedId)
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

        // Nueva ruta GET para obtener la lista de seguidores de un usuario
        get("/followers/{userId}") {
            try {
                val userId = call.parameters["userId"]

                if (userId.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "User ID es requerido")
                    return@get
                }

                // Obtener la lista de seguidores del usuario
                val followers = followerRepository.getFollowers(userId)

                if (followers.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound, "Este usuario no tiene seguidores")
                } else {
                    call.respond(HttpStatusCode.OK, followers)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error al procesar la solicitud: ${e.message}")
            }
        }
        get("/followed/{userId}") {
            try {
                val userId = call.parameters["userId"]

                if (userId.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "User ID es requerido")
                    return@get
                }

                // Obtener la lista de seguidores del usuario
                val followed = followerRepository.getFollowed(userId)

                if (followed.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound, "Este usuario no sigue a nadie")
                } else {
                    call.respond(HttpStatusCode.OK, followed)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error al procesar la solicitud: ${e.message}")
            }
        }

    }
}
