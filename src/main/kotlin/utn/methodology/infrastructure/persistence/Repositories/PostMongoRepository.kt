package utn.methodology.infrastructure.persistence.Repositories

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import utn.methodology.application.Contracts.PostRepository
import utn.methodology.domainentities.Post
import org.bson.types.ObjectId

class PostMongoRepository(private val database: MongoDatabase) : PostRepository {

    private val collection: MongoCollection<Document> = database.getCollection("Posts")

    // Implementación de 'save' para cumplir con la interfaz
    override fun save(post: Post) {
        val document = Document().apply {
            if (post.id.isNotBlank()) {
                put("_id", post.id)
            }
            put("userId", post.userId)
            put("message", post.message)
            put("createdAt", post.createdAt)
        }
        collection.insertOne(document)
    }

    // Implementación de 'findByUserId' para cumplir con la interfaz
   override fun findByUserId(userId: String): List<Post> {
        val filter = Document("userId", userId)
        return collection.find(filter).map { document ->
            // Debug: imprimir el documento recuperado
            println("Documento recuperado: $document")

            // Obtener el _id, que puede ser un ObjectId o String
            val id = (document["_id"] as? ObjectId)?.toHexString() ?: document["_id"].toString()

            val primitives = mapOf(
                "_id" to id,
                "userId" to document["userId"] as String,
                "message" to document["message"] as String,
                "createdAt" to document["createdAt"] as String
            )

            // Debug: imprimir los datos que se convertirán a la clase Post
            println("Datos convertidos: $primitives")

            Post.fromPrimitives(primitives)
        }.toList()
    }

    // Implementación de 'findByFollows' para cumplir con la interfaz
    override fun findByFollows(followIds: List<String>): List<Post> {
        val filter = Document("userId", Document("\$in", followIds))
        return collection.find(filter).map { document ->
            val id = (document["_id"] as? ObjectId)?.toHexString() ?: document["_id"].toString()
            val primitives = mapOf(
                "_id" to id,
                "userId" to document["userId"] as String,
                "message" to document["message"] as String,
                "createdAt" to document["createdAt"] as String
            )
            Post.fromPrimitives(primitives)
        }.toList()
    }

    // Implementación de 'deleteById' para cumplir con la interfaz
    override fun deleteById(postId: String) {
        val objectId = ObjectId(postId)
        val filter = Document("_id", objectId)
        collection.deleteOne(filter)
    }
}
