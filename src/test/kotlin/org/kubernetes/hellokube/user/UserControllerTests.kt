package org.kubernetes.hellokube.user

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

class UserControllerTests {

    private lateinit var service: FakeUserService
    private lateinit var client: WebTestClient

    @BeforeEach
    fun setUp() {
        service = FakeUserService()
        client = WebTestClient.bindToController(UserController(service))
            .controllerAdvice(ApiExceptionHandler())
            .build()
    }

    @Test
    fun saveUserReturns201AndTheSavedUser() {
        client.post().uri("/saveUser")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""{"name":"Ada","email":"ada@example.com"}""")
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.id").isEqualTo(1)
            .jsonPath("$.name").isEqualTo("Ada")
            .jsonPath("$.email").isEqualTo("ada@example.com")
    }

    @Test
    fun saveUserRejectsBlankNameAndBadEmail() {
        client.post().uri("/saveUser")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""{"name":"","email":"not-an-email"}""")
            .exchange()
            .expectStatus().isBadRequest
            .expectBody()
            .jsonPath("$.fields.name").exists()
            .jsonPath("$.fields.email").exists()

        assertTrue(service.saved.isEmpty())
    }

    @Test
    fun getUserReturnsEverySavedUser() {
        service.save(SaveUserRequest("Ada", "ada@example.com")).block()
        service.save(SaveUserRequest("Linus", "linus@example.com")).block()

        client.get().uri("/getUser")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.length()").isEqualTo(2)
            .jsonPath("$[0].name").isEqualTo("Ada")
            .jsonPath("$[1].name").isEqualTo("Linus")
    }
}
