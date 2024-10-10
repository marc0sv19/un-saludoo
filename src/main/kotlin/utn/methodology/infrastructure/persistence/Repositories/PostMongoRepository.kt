package utn.methodology.infrastructure.persistence.Repositories

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import org.bson.Document
import org.litote.kmongo.Id
import org.litote.kmongo.eq
import utn.methodology.domainentities.Post

import org.bson.types.ObjectId

class PostMongoRepository(private val database: MongoDatabase) {

    private val collection: MongoCollection<Document> = database.getCollection("Posts")
    fun save(post: Post) {
        val options = UpdateOptions().upsert(true)

        val filter = if (post.id.isNotBlank()) {
            Document("_id", post.id)
        } else {
            Document() // Deja que MongoDB genere el _id automáticamente
        }

        val update = Document("\$set", post.toPrimitives())
        collection.updateOne(filter, update, options)
    }


    fun findByUserId(userId: String): List<Post> {
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



    fun deleteById(postId: String) {
        val objectId = ObjectId(postId) // Asegúrate de convertir el id a ObjectId
        val filter = Document("_id", objectId)
        collection.deleteOne(filter)//elimina de la colleccion el id pasado
    }


}
