package utn.methodology.application.commands

import kotlinx.serialization.Serializable
import java.util.regex.Pattern

@Serializable
class UserCommands( //DTO, datos necesarios para crear o registrar un usuario
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
//Asegura los valores de los datos para continuar
        val emailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")
        if (!emailPattern.matcher(email).matches()) {
            throw IllegalArgumentException("Invalid email format")
        }


        if (password.length < 6) {
            throw IllegalArgumentException("Password must be at least 6 characters long")
        }

        return this;
        //Si tiene correcto los datos retorna el objeto/instancia validada del UserCommands
    }
}