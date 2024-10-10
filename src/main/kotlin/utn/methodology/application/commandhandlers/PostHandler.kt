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

        val post = Post.create(userId, message)
        postRepository.save(post)

        return post
    }
}

class PostValidationException(message: String) : Exception(message)