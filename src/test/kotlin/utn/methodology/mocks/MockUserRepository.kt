package utn.methodology.mocks

import utn.methodology.domainentities.User
import  utn.methodology.application.Contracts.UserRepository

class MockUserRepository : UserRepository {
    var users: Array<User> = emptyArray()

    override fun save(user: User) {
        users = users.filter { it.getUserId() != user.getUserId() }.toTypedArray()

        users = users.plus(user)
    }

    override fun findOne(id: String): User? {
        return users.find { it.getUserId() == id }
    }

   override fun delete(user: User) {
        users = users.filter { it.getUserId() != user.getUserId() }.toTypedArray()
    }

    fun clean() {
        users = emptyArray();
    }

    fun findByUsername(username: String): User? {
        return users.find { it.username == username }
    }
}

