package utn.methodology.application.commandhandlers

import utn.methodology.domainentities.Post
import utn.methodology.infrastructure.persistence.Repositories.PostMongoRepository

class PostHandler(
    private val postRepository: PostMongoRepository
) {

    fun createPost(userId: String, message: String): Post {
        if (message.length > 280) {
            throw PostValidationException("The post message exceeds the 280 character limit.")
        }

        // Crear un mapa con los parámetros necesarios
        val primitives = mapOf(
            "userId" to userId,
            "message" to message,
            "createdAt" to System.currentTimeMillis().toString() // o el formato que uses
        )

        val post = Post.fromPrimitives(primitives) // Llamar con el mapa
        postRepository.save(post)

        return post
    }

    fun deletePost(postId: String) {
        postRepository.deleteById(postId)
    }

}

class PostValidationException(message: String) : Exception(message)