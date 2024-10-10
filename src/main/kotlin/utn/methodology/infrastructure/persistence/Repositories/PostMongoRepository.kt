package utn.methodology.infrastructure.persistence.Repositories

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import org.bson.Document
import org.litote.kmongo.Id
import org.litote.kmongo.eq
import utn.methodology.domainentities.Post

class PostMongoRepository(private val database: MongoDatabase) {

    private val collection: MongoCollection<Any> = database.getCollection("Posts") as MongoCollection<Any>

    fun save(post: Post) {
        val options = UpdateOptions().upsert(true)

        val filter = Document("_id", post.id)
        val update = Document("\$set", post.toPrimitives())

        collection.updateOne(filter, update, options)
    }
    fun findByUserId(userId: String): List<Post> {
        val filter = Document("userId", userId)
        return collection.find(filter).map {
            Post.fromPrimitives(it as Map<String, String>)
        }.toList()
    }
fun deleteById(postId: String){
    collection.deleteOne(Post::id eq postId)
}
}
