package utn.methodology.application.Contracts

import utn.methodology.domain.entities.UserCreate


interface UserRepository {
    fun save(user: UserCreate)
    fun findOne(id: String): UserCreate?
}