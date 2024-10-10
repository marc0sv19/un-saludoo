package utn.methodology.domainentities

import kotlinx.serialization.Serializable
import java.util.*
@Serializable
data class Post(
    val id: String,
    val userId: String,
    val message: String,

) {
    companion object {
        fun create(userId: String, message: String): Post {
            return Post(
                id = UUID.randomUUID().toString(),
                userId = userId,
                message = message,

            )
        }
    }

    fun toPrimitives(): Map<String, Any> {
        return mapOf(
            "id" to this.id,
            "userId" to this.userId,
            "message" to this.message,

        )
    }
}
