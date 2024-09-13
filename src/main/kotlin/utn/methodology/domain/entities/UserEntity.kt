package utn.methodology.domain.entities

import utn.methodology.application.domain.Events.*
import java.util.UUID


@Suppress("UNCHECKED_CAST")
/*data class UserCreate(
    val id: String,
    val uuId: String,
    var nombre: String,
    val username: String,
    val email: String,
    val contraseña: String

) {

    private var events: List<DomainEvent> = mutableListOf()


    companion object {
        fun fromPrimitives(primitives: Map<String, Any>): UserCreate {
            val userCreate = UserCreate(
                primitives["id"] as String,
                primitives["uuId"] as String,
                nombre.fromPrimitives(primitives["nombre"] as Map<String, Any>),
                username.fromPrimitives(primitives["username"] as Map<String, Any>),
                email.fromPrimitives(primitives["email"] as Map<String, Any>),
                contraseña.fromPrimitives(primitives["contraseña"] as Map<String, String>),
            )

            return userCreate
        }

        fun create(
            uuId: String,
            nombre: String,
            username: String,
            email: String,
            contraseña: String,
        ): UserCreate {

            val userCreate = UserCreate(
                UUID.randomUUID().toString(),
                uuId,
                nombre,
                username,
                email,
                contraseña
            )

            userCreate.recordEvent(
                UserCreatedEvent(
                    userCreate.id,
                    uuId,
                    nombre,
                    username,
                    email,
                    contraseña,
                )
            )

            return userCreate
        }
    }
    private fun recordEvent(event: DomainEvent) {
        this.events = this.events.plus(event)
    }
    fun toPrimitives(): Map<String, Any?> {
        return mapOf(
            "id" to this.id,
            "uuId" to this.uuId,
            "nombre" to this.nombre.toPrimitives(),
            "username" to this.username.toPrimitives(),
            "email" to this.email.toPrimitives(),
            "contraseña" to this.contraseña.toPrimitives()
        )
    }

    fun getUserId(): String {
        return this.id
    }

    fun pullDomainEvents(): List<DomainEvent> {
        return this.events
    }
}*/
data class UserCreate(
    val id: String,
    var nombre: String,
    val username: String,
    val email: String,
    val password: String
) {
    private var events: MutableList<DomainEvent> = mutableListOf()

    companion object {
        fun fromPrimitives(primitives: Map<String, Any>): UserCreate {
            return UserCreate(
                primitives["id"] as? String ?: "",
                primitives["nombre"] as? String ?: "",
                primitives["username"] as? String ?: "",
                primitives["email"] as? String ?: "",
                primitives["password"] as? String ?: ""
            )
        }

        fun create(
            nombre: String,
            username: String,
            email: String,
            password: String // Corregir a singular
        ): UserCreate {

            val userCreate = UserCreate(
                UUID.randomUUID().toString(),
                nombre,
                username,
                email,
                password // Usar singular aquí también
            )
            return userCreate
        }
    }

    fun toPrimitives(): Map<String, Any?> {
        return mapOf(
            "id" to this.id,
            "nombre" to this.nombre,
            "username" to this.username,
            "email" to this.email,
            "password" to this.password
        )
    }

    fun getUserId(): String {
        return this.id
    }


}
