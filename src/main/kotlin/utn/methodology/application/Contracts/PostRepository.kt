package utn.methodology.application.Contracts

import utn.methodology.domainentities.Post

interface PostRepository {
    fun save(post: Post)
    fun findByUserId(userId: String): List<Post>?
    fun findByFollows(followIds: List<String>): List<Post>  // Asegúrate de agregar este método en la interfaz
    fun deleteById(postId: String)
}
