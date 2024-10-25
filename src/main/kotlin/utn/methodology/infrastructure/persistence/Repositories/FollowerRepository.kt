package utn.methodology.infrastructure.persistence.Repositories

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.bson.Document

import utn.methodology.domainentities.Follower


class FollowerRepository(private val database: MongoDatabase) {

    private val collection: MongoCollection<Document> = database.getCollection("Followers")

    fun follow(follower: Follower) {
        val document = Document("followerId", follower.followerId)
            .append("followedId", follower.followedId)

        // Lógica para insertar o actualizar la relación
        collection.insertOne(document)
    }


    fun unfollow(followerId: String, followedId: String) {
        val filter = Filters.and(Filters.eq("followerId", followerId), Filters.eq("followedId", followedId))
        collection.deleteOne(filter)
    }

    fun getFollowed(userId: String): List<Follower> {
        val filter = Filters.eq("followedId", userId)
        return collection.find(filter).map { document ->
            Follower(
                followerId = document.getString("followerId"), // Esto debería estar bien
                followedId = userId // Este es correcto ya que buscas "followedId"
            )
        }.toList()
    }
    
    fun getFollowers(userId: String): List<Follower> {
        val filter = Filters.eq("followerId", userId)
        return collection.find(filter).map { document ->
            Follower(
                followerId = userId, // Este es correcto ya que buscas "followerId"
                followedId = document.getString("followedId") // Asegúrate de que esto sea correcto
            )
        }.toList()
    }
}