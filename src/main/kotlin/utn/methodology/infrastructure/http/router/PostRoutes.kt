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

fun Application.postRouter() {
    val mongoDatabase = connectToMongoDB() // Conexión a la base de datos
    val postRepository = PostMongoRepository(mongoDatabase) // Repositorio de posts
    val postHandler = PostHandler(postRepository)
    routing {
        postRoutes(postHandler)
        get("/posts") {
            // Obtener los parámetros de consulta
            val userId = call.request.queryParameters["userId"]
            val order = call.request.queryParameters["order"] ?: "ASC" // Valor por defecto
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10 // Valor por defecto
            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0 // Valor por defecto

            // Validación de userId
            if (userId.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, "El parámetro 'userId' es requerido.")
                return@get
            }

            try {
                // Buscar los posts del usuario
                val posts = postRepository.findByUserId(userId)

                // Filtrar, ordenar y paginar según los parámetros
                val filteredPosts = when (order.uppercase()) {
                    "DESC" -> posts.sortedByDescending { it.createdAt }.drop(offset).take(limit)
                    else -> posts.sortedBy { it.createdAt }.drop(offset).take(limit)
                }

                // Responder con la lista de posts
                call.respond(HttpStatusCode.OK, filteredPosts)
            } catch (ex: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error al procesar la solicitud")
            }
        }
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
fun Route.postRoutes(postHandler: PostHandler) {
    post("/posts") {
        try {
            println("Recibiendo solicitud para crear un post...")
            val request = call.receive<PostRequest>()
            println("Cuerpo de la solicitud: $request")

            val post = postHandler.createPost(request.userId, request.message)
            println("Post creado: $post")
            call.respond(HttpStatusCode.Created, mapOf("message" to "ok"))
            //call.respond(HttpStatusCode.Created, post)
        } catch (e: PostValidationException) {
            println("Error de validación del post: ${e.message}")
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid post data")
        } catch (e: Exception) {
            println("Error inesperado: ${e.message}")
            call.respond(HttpStatusCode.InternalServerError, "Error al procesar la solicitud")
        }
    }
}


@Serializable
data class PostRequest(val userId: String, val message: String)
