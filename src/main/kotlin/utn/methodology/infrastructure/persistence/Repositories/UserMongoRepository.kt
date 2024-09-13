package utn.methodology.infrastructure.persistence.Repositories


import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import io.github.cdimascio.dotenv.dotenv
import org.bson.Document
import utn.methodology.application.Contracts.UserRepository
import utn.methodology.domain.entities.UserCreate



val collectionName: String = dotenv()["USER_COLLECTION_NAME"] ?: "UserCreate"

class UserMongoRepository(private val database: MongoDatabase) : UserRepository {

    private var collection: MongoCollection<Any>;

    init {
        collection = this.database.getCollection(collectionName) as MongoCollection<Any>;
    }


    override fun save(user: UserCreate) {
        println("UserMongoRepository - Saving user: $UserCreate")
        val options = UpdateOptions().upsert(true);

        val filter = Document("_id",user.getUserId()) // Usa el campo id como filter
        val update = Document("\$set", user.toPrimitives())

        collection.updateOne(filter, update, options)
    }


    override fun findOne(id: String): UserCreate? {
        val filter = Document("_id", id);

        val primitives = collection.find(filter).firstOrNull();

        if (primitives == null) {
            return null;
        }

        return UserCreate.fromPrimitives(primitives as Map<String, String>)
    }
}