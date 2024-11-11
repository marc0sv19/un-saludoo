package utn.methodology.application


import utn.methodology.application.commandhandlers.ConfirmUserHandler
import utn.methodology.application.commands.UserCommands
import utn.methodology.mocks.MockUserRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CreateUserHandlerTest {

    private val mockUserRepository = MockUserRepository()
    private lateinit var sut:ConfirmUserHandler

    @BeforeTest
    fun setup() {
        mockUserRepository.clean()
        sut = ConfirmUserHandler(mockUserRepository)
    }

    @Test
    fun `create_user_should_returns_201`() {
        val username="potencia"
        val command = UserCommands("ValidUser", "potencia","dsada@gmail.com","231321")

        // act
        val result =sut.handle(command)
        //assertion
        val user = mockUserRepository.findByUsername(username)
        // assert
        assertNotNull(user)
        assert(user.email==command.email)
        assertEquals(201, result)
    }

    @Test
    fun `create_user_should_returns_400_when_invalid_data`() {
        // Arrange
        val invalidCommand = UserCommands(
            nombre = "",
            username = "user123",
            email = "invalid-email",
            password = "password123"
        )

        val handler = ConfirmUserHandler(mockUserRepository)

        // Act
        val result = handler.handle(invalidCommand)

        // Assert
        assertEquals(400, result)
    }
}

