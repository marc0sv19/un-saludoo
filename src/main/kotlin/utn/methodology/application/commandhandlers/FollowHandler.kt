package utn.methodology.application.commandhandlers

import utn.methodology.domainentities.Follower
import utn.methodology.infrastructure.persistence.Repositories.FollowerRepository

class FollowHandler(private val followerRepository: FollowerRepository) {

    fun followUser(followerId: String, followedId: String) {
        if (followerId == followedId) {
            throw FollowValidationException("No puedes seguirte a ti mismo.")
        }
        val follower = Follower(followerId, followedId)
        followerRepository.follow(follower)
    }

    fun unfollowUser(followerId: String, followedId: String) {
        followerRepository.unfollow(followerId, followedId)
    }

    /*fun getFollowers(userId: String): List<String> {
        val followers = followerRepository.getFollowers(userId) // Esto devuelve List<Follower>
        return followers.map { it.followerId } // Mapeamos a List<String> extrayendo solo los followerId
    }

    fun getFollowed(userId: String): List<String> {
        val followedUsers = followerRepository.getFollowed(userId) // Esto devuelve List<Follower>
        return followedUsers.map { it.followedId } // Mapeamos a List<String> extrayendo solo los followedId
    }*/
}

class FollowValidationException(message: String) : Exception(message)