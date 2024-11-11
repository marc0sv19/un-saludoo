package utn.methodology.application.commandhandlers

import io.ktor.server.plugins.*
import utn.methodology.application.Contracts.PostRepository
import utn.methodology.application.Contracts.UserRepository
import utn.methodology.application.commands.CreatePostCommand
import utn.methodology.domainentities.Post

class PostHandler(private val userRepository: UserRepository, private val postRepository: PostRepository) {

    fun handle(command: CreatePostCommand): Int {
        // Verificar si el usuario existe
        val user = userRepository.findOne(command.userId)

        if (user == null) {
            throw NotFoundException("User not found")
        }

        // Validación para el contenido vacío
        if (command.content.isBlank()) {
            throw PostValidationException("El contenido del post no puede estar vacío")
        }

        // Crear el post
        val post = Post.create(command.content, command.userId)

        // Guardar el post en el repositorio
        postRepository.save(post)

        // Devolver el código de respuesta HTTP 201 (Created)
        return 201
    }

    fun createPost(userId: String, message: String): Post {
        // Validación para el mensaje vacío o demasiado largo
        if (message.isBlank()) {
            throw PostValidationException("El contenido del post no puede estar vacío")
        }
        if (message.length > 280) {
            throw PostValidationException("The post message exceeds the 280 character limit.")
        }

        // Crear el post
        val post = Post.create(message, userId)
        postRepository.save(post)

        return post
    }

    fun deletePost(postId: String) {
        postRepository.deleteById(postId)
    }
}

class PostValidationException(message: String) : Exception(message)
