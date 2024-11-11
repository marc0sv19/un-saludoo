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

}

class FollowValidationException(message: String) : Exception(message)