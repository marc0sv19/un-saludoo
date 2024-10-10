package utn.methodology.domainentities

import kotlinx.serialization.Serializable
import java.util.*
@Serializable
data class Post(
    val id: String,
    val userId: String,
    val message: String,
    val createdAt: String

) {

    companion object {
        fun fromPrimitives(primitives: Map<String, String>): Post {
            return Post(
                primitives["_id"] ?: "",  // Asegúrate de que la clave sea correcta
                primitives["userId"] ?: "",
                primitives["message"] ?: "",
                primitives["createdAt"] ?: ""
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

}
