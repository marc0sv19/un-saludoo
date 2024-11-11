package utn.methodology.application.commands

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostCommand(
    val content: String,  // Asegúrate de que se llame 'content'
    val userId: String
)
