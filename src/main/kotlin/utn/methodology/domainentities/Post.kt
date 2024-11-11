package utn.methodology.domainentities

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Post(
    val id: String,
    val userId: String,
    val message: String,
    val createdAt: String
) {
    companion object {
        fun create(content: String, userId: String): Post {  // Cambia para aceptar userId
            return Post(
                id = UUID.randomUUID().toString(),
                userId = userId,
                message = content,
                createdAt = System.currentTimeMillis().toString()
            )
        }

        // Método `fromPrimitives` que crea un `Post` a partir de un mapa de primitivas.
        fun fromPrimitives(primitives: Map<String, Any>): Post {
            return Post(
                primitives["_id"] as? String ?: UUID.randomUUID().toString(),
                primitives["userId"] as? String ?: throw IllegalArgumentException("User ID is required"),
                primitives["message"] as? String ?: throw IllegalArgumentException("Message is required"),
                primitives["createdAt"] as? String ?: System.currentTimeMillis().toString()
            )
        }
    }

    fun toPrimitives(): Map<String, Any> {
        return mapOf(
            "id" to this.id,
            "userId" to this.userId,
            "message" to this.message,
            "createdAt" to this.createdAt
        )
    }


    fun getContent(): String {
        return this.message
    }
}
