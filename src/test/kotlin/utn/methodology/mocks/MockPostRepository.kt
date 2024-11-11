package utn.methodology.mocks

import utn.methodology.application.Contracts.PostRepository
import utn.methodology.domainentities.Post

class MockPostRepository : PostRepository {

    private var posts: Array<Post> = emptyArray()

    override fun save(post: Post) {
        // Eliminar cualquier post con el mismo ID antes de agregarlo
        this.posts = this.posts.filter { it.id != post.id }.toTypedArray()
        this.posts = this.posts.plus(post)
    }

    override fun findByUserId(userId: String): List<Post>? {
        // Devuelve el primer post que coincida con el userId, o null si no se encuentra
        return this.posts.filter { it.userId == userId }
    }

    override fun findByFollows(followIds: List<String>): List<Post> {
        return this.posts.filter { followIds.contains(it.userId) }
    }

    // Implementación del método deleteById
    override fun deleteById(postId: String) {
        this.posts = this.posts.filter { it.id != postId }.toTypedArray()
    }

    fun clean() {
        posts = emptyArray()
    }
}