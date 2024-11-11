package utn.methodology.infrastructure.persistence.Repositories


import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
//import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import io.github.cdimascio.dotenv.dotenv
import org.bson.Document
import utn.methodology.application.Contracts.UserRepository
import utn.methodology.domainentities.User




val collectionName: String = dotenv()["USER_COLLECTION_NAME"] ?: "UserCreate"


class UserMongoRepository(private val database: MongoDatabase) : UserRepository {

    private var collection: MongoCollection<Any>;


    init {
        collection = this.database.getCollection(collectionName) as MongoCollection<Any>;

    }


    override fun save(user: User) {
        println("UserMongoRepository - Saving user: $User")
        val options = UpdateOptions().upsert(true);

        val filter = Document("_id",user.getUserId())
        val update = Document("\$set", user.toPrimitives())

        collection.updateOne(filter, update, options)
    }


    override fun findOne(id: String): User? {
        val filter = Document("_id", id);

        val primitives = collection.find(filter).firstOrNull();

        if (primitives == null) {
            return null;
        }

        return User.fromPrimitives(primitives as Map<String, String>)
    }
    fun findByUsername(username: String): User? {
       val filter=Document("username",username);
    val primitives=collection.find(filter).firstOrNull()
        if (primitives==null){
            return null
        }
        return User.fromPrimitives(primitives as Map<String, String>)
    }
    override fun delete(user: User) {
        val filter = Document("_id", user.getUserId());

        collection.deleteOne(filter)
    }
}