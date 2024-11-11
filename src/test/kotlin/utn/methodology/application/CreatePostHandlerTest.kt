package utn.methodology.application

import utn.methodology.application.commandhandlers.PostHandler
import utn.methodology.application.commandhandlers.PostValidationException
import utn.methodology.application.commands.CreatePostCommand
import utn.methodology.mocks.MockPostRepository
import utn.methodology.mocks.MockUserRepository
import utn.methodology.domainentities.User
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreatePostHandlerTest {

    private val mockUserRepository: MockUserRepository = MockUserRepository()
    private val mockPostRepository: MockPostRepository = MockPostRepository()

    private var sut: PostHandler = PostHandler(mockUserRepository, mockPostRepository)

    @BeforeTest
    fun beforeEach() {
        mockPostRepository.clean()
        mockUserRepository.clean()
    }

    @Test
    fun `create_post_should_returns_201`() {
        // arrange
        val user = User.create("John Doe", "john_doe", "john.doe@example.com", "password123")
        val content = "This is a post message"

        // Guardamos el usuario en el repositorio mock
        mockUserRepository.save(user)

        // Creamos el comando
        val command = CreatePostCommand(
            content = content,
            userId = user.id
        )

        // act
        val responseCode = sut.handle(command)

        // assertions
        val posts = mockPostRepository.findByUserId(user.id)
        assertNotNull(posts)  // Verificamos que la lista de posts no sea nula
        assertEquals(1, posts.size)  // Verificamos que haya exactamente un post
        assertEquals(content, posts[0].getContent())  // Verificamos el contenido del post
        assertEquals(201, responseCode)  // Verificamos que el código de respuesta sea 201
    }

    @Test
    fun `create_post_should_returns_400`() {
        // arrange
        val invalidContent = ""  // Contenido vacío, que debería provocar un error
        val user = User.create("Jane Doe", "jane_doe", "jane.doe@example.com", "password123")
        mockUserRepository.save(user)

        // Creamos el comando con contenido inválido
        val command = CreatePostCommand(
            content = invalidContent,
            userId = user.id
        )

        // act & assert
        val exception = assertFailsWith<PostValidationException> {
            sut.handle(command)
        }
        assertEquals("El contenido del post no puede estar vacío", exception.message)
    }
}
