package utn.methodology.application.Contracts

import utn.methodology.domainentities.User


interface UserRepository {
    fun save(user: User)
    fun findOne(id: String): User?
}