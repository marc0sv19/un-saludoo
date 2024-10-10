package utn.methodology.infrastructure.http.actions
import utn.methodology.application.commandhandlers.SearchUserHandler
import utn.methodology.domainentities.User
import utn.methodology.infrastructure.persistence.Repositories.UserMongoRepository


/*class FindUserByUserNameAction(
    //private val handler: GetUserByIdHandler
    private val userRepository: UserMongoRepository
) {
    //   fun execute(query: GetUserByIdQuery): User? {
     fun execute(query: String): User? {

        try {
            //println(query.userName)
            println(query)

          val user=userRepository.findByUsername(query)
            return user
        } catch (e: Exception) {
            println(e)
            throw e;
        }
        // Lógica para buscar al usuario en el repositorio por su username
    }
}
*/

    class SearchUserAction(private val handler: SearchUserHandler) {

        fun execute(username: String): User? {
            return handler.handler(username)  // Delegar al handler la búsqueda
        }
    }
