package utn.methodology.application.commands

import kotlinx.serialization.Serializable

@Serializable
class UserCommands(
    val nombre: String,
    val username: String,
    val email: String,
    val password: String,
) {

    fun validate(): UserCommands {
        checkNotNull(nombre) { throw IllegalArgumentException("nombre must be defined") }
        checkNotNull(username) { throw IllegalArgumentException("username must be defined") }
        checkNotNull(email) { throw IllegalArgumentException("email must be defined") }
        checkNotNull(password) { throw IllegalArgumentException("contraseña unit must be defined") }

        return this;
    }
}