package utn.methodology.infrastructure.http.router
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import utn.methodology.application.commandhandlers.PostHandler
import utn.methodology.application.commandhandlers.PostValidationException
import utn.methodology.infrastructure.persistence.Repositories.PostMongoRepository
import utn.methodology.infrastructure.persistence.Config.connectToMongoDB
import utn.methodology.infrastructure.persistence.Repositories.UserMongoRepository

fun Application.postRouter() {
    val mongoDatabase = connectToMongoDB() // Conexión a la base de datos
    val postRepository = PostMongoRepository(mongoDatabase) // Repositorio de posts
    val userRepository = UserMongoRepository(mongoDatabase) // Repositorio de usuarios

    // Crear el PostHandler pasando los repositorios en el orden correcto
    val postHandler = PostHandler(userRepository, postRepository)

    routing {
        post("/posts") {
            try {
                val request = call.receive<PostRequest>()
                if (userRepository.findOne(request.userId) != null) {
                    // Pasar ambos parámetros (userId y message)
                    val post = postHandler.createPost(request.userId, request.message)
                    call.respond(HttpStatusCode.Created, mapOf("message" to "ok"))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Usuario no encontrado")
                }
            } catch (e: PostValidationException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid post data")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error al procesar la solicitud")
            }
        }

        // Ruta para obtener posts
        get("/posts") {
            val userId = call.request.queryParameters["userId"]
            val order = call.request.queryParameters["order"] ?: "ASC" // Valor por defecto
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10 // Valor por defecto
            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0 // Valor por defecto

            if (userId.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, "El parámetro 'userId' es requerido.")
                return@get
            }

            try {
                // Convertir MongoIterable a una lista estándar de Kotlin
                val posts = postRepository.findByUserId(userId).toList()



                // Filtrar, ordenar y paginar según los parámetros
                val filteredPosts = when (order.uppercase()) {
                    "DESC" -> posts.sortedByDescending { it.createdAt }.drop(offset).take(limit)
                    else -> posts.sortedBy { it.createdAt }.drop(offset).take(limit)
                }

                call.respond(HttpStatusCode.OK, filteredPosts)
            } catch (ex: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error al procesar la solicitud")
            }
        }

        // Ruta para eliminar un post
        delete("/posts/{id}") {
            val postId = call.parameters["id"]

            if (postId.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, "El ID del post es requerido.")
                return@delete
            }

            try {
                postHandler.deletePost(postId)
                call.respond(HttpStatusCode.NoContent) // 204 No Content indica que la eliminación fue exitosa
            } catch (ex: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error al procesar la solicitud: ${ex.message}")
            }
        }
    }
}

@Serializable
data class PostRequest(val userId: String, val message: String)
