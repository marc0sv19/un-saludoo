package utn.methodology.infrastructure.persistence.Repositories

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import org.bson.Document
import utn.methodology.domainentities.Post

class PostMongoRepository(private val database: MongoDatabase) {

    private val collection: MongoCollection<Any> = database.getCollection("Posts") as MongoCollection<Any>

    fun save(post: Post) {
        val options = UpdateOptions().upsert(true)

        val filter = Document("_id", post.id)
        val update = Document("\$set", post.toPrimitives())

        collection.updateOne(filter, update, options)
    }
}
