package utn.methodology.domainentities

import kotlinx.serialization.Serializable
import java.util.*
@Serializable
data class Follower(
    val followerId: String,
    val followedId: String
)
